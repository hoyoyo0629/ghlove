package saleson.shop.manual;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.Const;
import saleson.common.file.service.CustomFileService;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.manual.domain.Manual;
import saleson.shop.manual.support.ManualParam;

@Controller
@RequestMapping("/opmanager/manual/**")
@RequestProperty(title="메뉴얼", layout="default", template="opmanager")
public class ManualManagerController {
    private static final Logger log = LoggerFactory.getLogger(ManualManagerController.class);

    @Autowired
    private ManualService manualService;

    @Autowired
    private CustomFileService customFileService;

    @GetMapping("list")
    public String list(RequestContext requestContext, @ModelAttribute("searchParam") ManualParam manualParam , Model model) {

		String today1 = DateUtils.getToday("yyyyMMdd");
		manualParam.setStartCreateDate(StringUtils.defaultIfEmpty(manualParam.getStartCreateDate(), today1));
		manualParam.setEndCreateDate(StringUtils.defaultIfEmpty(manualParam.getEndCreateDate(), today1));

        int count = 0;

        Pagination pagination = Pagination.getInstance(count, manualParam.getItemsPerPage());
        manualParam.setPagination(pagination);
        //List<Code> subCategoryCode = CodeUtils.getCodeList("NOTICE_SUB_CATEGORY");
        //List<Code> categoryTeamCode = CodeUtils.getCodeList("NOTICE_CATEGORY_TEAM");

        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

        model.addAttribute("list", Collections.EMPTY_LIST);
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);

        model.addAttribute("searchParam", manualParam);


        //model.addAttribute("subCategoryCode", subCategoryCode);
        //model.addAttribute("categoryTeamCode", categoryTeamCode);

        return "view:/manual/user/list";

    }

	@GetMapping("create")
	public String create(RequestContext requestContext, Model model, Manual manual, @ModelAttribute("searchParam") ManualParam searchParam){

		List<Code> menuUrlList = CodeUtils.getCodeList("MENU_URL");
		model.addAttribute("menuUrlList", menuUrlList);

		model.addAttribute("manual", manual);
		model.addAttribute("searchParam", searchParam);

		return "view:/manual/user/form";
	}

    @PostMapping("create")
    public String createAction(RequestContext requestContext,
    		Manual manual,
    		@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles){

//        manual.setUserName(SecurityUtils.getCurrentUser().getUserName());
        manual.setItemDetailImageFiles(detailImageFiles);
        manualService.insertManual(manual);

        return ViewUtils.redirect("/opmanager/manual/user/list","등록되었습니다.");
    }

	@GetMapping("edit/{mnlSn}")
	public String edit(RequestContext requestContext, Model model, @PathVariable("mnlSn") int mnlSn, @ModelAttribute("searchParam") ManualParam searchParam ){

		List<Code> menuUrlList = CodeUtils.getCodeList("MENU_URL");
		model.addAttribute("menuUrlList", menuUrlList);

		model.addAttribute("manual", manualService.getManual(mnlSn));
		model.addAttribute("searchParam", searchParam);

		return "view:/manual/user/form";
	}


    @PostMapping("edit/{dataId}")
    public String editAction(RequestContext requestContext, Manual manual,
    		@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles){
//        manual.setUserName(SecurityUtils.getCurrentUser().getUserName());
        manual.setItemDetailImageFiles(detailImageFiles);
        manualService.updateManual(manual);

        return ViewUtils.redirect("/opmanager/manual/user/list", MessageUtils.getMessage("M00289"));
    }

    @PostMapping("delete/{mnlSn}")
    public JsonView delete(RequestContext requestContext, @PathVariable int mnlSn) {
        if (!requestContext.isAjaxRequest()) {
            throw new NotAjaxRequestException();
        }

        try {
            manualService.deleteManual(mnlSn);
        } catch (RuntimeException e) {
//            return JsonViewUtils.exception(e.getMessage());
            return JsonViewUtils.exception("실패했습니다.");			// 실패했습니다.
        }
        return JsonViewUtils.success();
    }

    @PostMapping("/deleteManual")
    public JsonView manualListDelete(RequestContext requestContext, ManualParam manualParam) {
        String code = "";

        try {
            code = manualService.manualListDelete(manualParam);
        } catch (RuntimeException e) {
//            log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR: {}", getClass().getName() + " :: manualListDelete RuntimeException =============");
        }

        return JsonViewUtils.success(code);
    }

    /**
	 * 파일를 삭제한다.
	 * @param requestContext
	 * @param itemId
	 * @return
	 */
	@PostMapping("delete-item-image")
	public JsonView deleteItemImage(RequestContext requestContext, @RequestParam("itemId") int itemId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		manualService.deleteItemImageByItemId(itemId);

		return JsonViewUtils.success();
	}

	/**
	 * 첨부파일 다운로드
	 * @param mnlSn
	 * @return
	 */
	@GetMapping("file-download/{mnlSn}")
	public ResponseEntity<?> fileDownload(@PathVariable Integer mnlSn) throws IOException {
		Manual manual = manualService.getManual(mnlSn);
		File file = new File(CommonUtils.dataNvl(manual.getFileSrc()));

		if (file.exists()) {
			return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(manual.getOrginlFileNm()), file);
		}

		return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
	}

	@GetMapping("manager/list")
    public String managerList(RequestContext requestContext, @ModelAttribute("searchParam") ManualParam manualParam , Model model) {

    	List<HashMap<String, Object>> menuList = manualService.getAllMenuList();
    	model.addAttribute("menuList", menuList);

    	// 권한 Role
		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority()) || "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())) {
				role = "SYSTEM";
				break;
			} else {
				role = "SYSTEM_NO";
				break;
			}
		}
		model.addAttribute("role", role);

        return "view:/manual/manager/list";
    }

	@GetMapping("manager/create")
	@RequestProperty(layout = "base")
	public String managerCreate(RequestContext requestContext, Model model, Manual manual, @ModelAttribute("searchParam") ManualParam searchParam){

		// 메뉴명
		model.addAttribute("menuId", searchParam.getMenuId());
		// 첨부파일
		model.addAttribute("menuName", searchParam.getMenuName());

		// 최종수정일

		// 메뉴상세
		model.addAttribute("manual", manualService.getOpMenu(searchParam.getMenuId()));


		return "view:/manual/manager/form";
	}

    @PostMapping("manager/create")
    public String managerCreateAction(RequestContext requestContext,
    		Manual manual,
    		@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles){

//        manual.setUserName(SecurityUtils.getCurrentUser().getUserName());
    	manual.setItemDetailImageFiles(detailImageFiles);
    	manualService.insertManagerManual(manual);

    	String javascript = "opener.location.reload(); self.close();";
    	return ViewUtils.redirect("/opmanager/manual/manager/create?menuId="+manual.getMenuId() + "&menuName="+manual.getMenuNm()+"","등록되었습니다.", javascript);
    }

	/**
	 * 파일를 삭제한다.
	 * @param requestContext
	 * @param itemId
	 * @return
	 */
	@PostMapping("manager/delete-item-image")
	public JsonView managerDeleteItemImage(RequestContext requestContext, @RequestParam("menuId") String menuId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		manualService.managerDeleteItemImageByItemId(menuId);

		return JsonViewUtils.success();
	}

	/**
	 * 첨부파일 다운로드
	 * @param mnlSn
	 * @return
	 */
	@GetMapping("manager/file-download/{menuId}")
	public ResponseEntity<?> managerFileDownload(@PathVariable String menuId) throws IOException {
		Manual manual = manualService.getOpMenu(menuId);
		File file = new File(CommonUtils.dataNvl(manual.getFileSrc()));

		if (file.exists()) {
			HttpHeaders header = new HttpHeaders();

			String fileName  = URLEncoder.encode(CommonUtils.dataNvl(manual.getOrginlFileNm()), StandardCharsets.UTF_8);

			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");

			try (FileInputStream fis = new FileInputStream(file);) {
//				InputStreamResource resource3 = new InputStreamResource(fis);
				byte[] bytes = FileCopyUtils.copyToByteArray(fis);
				ByteArrayResource resource = new ByteArrayResource(bytes);

				return ResponseEntity.ok()
						.headers(header)
						.contentLength(file.length())
						.contentType(MediaType.parseMediaType("application/octet-stream"))
//						.body(resource3);
						.body(resource);
			} catch (IOException e) {
				return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
			}
		}

//		HttpHeaders responseHeaders = new HttpHeaders();
//		responseHeaders.add("Content-type", "text/html; charset=UTF-8");
//		String msg = "<script>";
//		msg += "alert('파일이 존재하지 않습니다.');";
//		msg += "history.back();";
//		msg += "</script>";
//
//		return new ResponseEntity<>(msg, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
	}

//	@GetMapping("manager/file-download/checkList/{menuIdList}")
//    public ResponseEntity<?> managerFileDownloadCheckList(@PathVariable List<String> menuIdList) throws IOException {
//
//		for (String menuId : menuIdList) {
//			Manual manual = manualService.getOpMenu(menuId);
//			File file = new File(CommonUtils.dataNvl(manual.getFileSrc()));
//
//			if (file.exists()) {
//				HttpHeaders header = new HttpHeaders();
//
//				String fileName  = URLEncoder.encode(CommonUtils.dataNvl(manual.getOrginlFileNm()), StandardCharsets.UTF_8);
//
//				header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
//				header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//				header.add("Pragma", "no-cache");
//				header.add("Expires", "0");
//
//				InputStreamResource resource3 = new InputStreamResource(new FileInputStream(file));
//
//				return ResponseEntity.ok()
//						.headers(header)
//						.contentLength(file.length())
//						.contentType(MediaType.parseMediaType("application/octet-stream"))
//						.body(resource3);
//			}
//		}
//
//		return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
//    }

	/**
	 * 관리자메뉴얼리스트
	 * @param requestContext
	 * @param manualParam
	 * @param model
	 * @return
	 */
	@PostMapping("manager/list")
    public String searchManagerList(RequestContext requestContext, @ModelAttribute("searchParam") ManualParam manualParam , Model model) {
		return managerList(requestContext, manualParam , model);
	}

	/**
	 * 사용자메뉴얼리스트
	 * @param requestContext
	 * @param manualParam
	 * @param model
	 * @return
	 */
	@PostMapping("list")
    public String searchList(RequestContext requestContext, @ModelAttribute("searchParam") ManualParam manualParam , Model model) {
		String today1 = DateUtils.getToday("yyyyMMdd");
		manualParam.setStartCreateDate(StringUtils.defaultIfEmpty(manualParam.getStartCreateDate(), today1));
		manualParam.setEndCreateDate(StringUtils.defaultIfEmpty(manualParam.getEndCreateDate(), today1));

        int count = manualService.getManualCount(manualParam);

        Pagination pagination = Pagination.getInstance(count, manualParam.getItemsPerPage());
        manualParam.setPagination(pagination);
        //List<Code> subCategoryCode = CodeUtils.getCodeList("NOTICE_SUB_CATEGORY");
        //List<Code> categoryTeamCode = CodeUtils.getCodeList("NOTICE_CATEGORY_TEAM");

        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

        model.addAttribute("list", manualService.getManualList(manualParam));
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);

        model.addAttribute("searchParam", manualParam);


        //model.addAttribute("subCategoryCode", subCategoryCode);
        //model.addAttribute("categoryTeamCode", categoryTeamCode);

        return "view:/manual/user/list";
	}
}
