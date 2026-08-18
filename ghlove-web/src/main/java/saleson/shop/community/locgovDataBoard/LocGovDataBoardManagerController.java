package saleson.shop.community.locgovDataBoard;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.UserException;
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
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.community.CmntyService;
import saleson.shop.community.doamin.CmntyFileDto;
import saleson.shop.community.doamin.CmntyRpstrDto;
import saleson.shop.community.doamin.CmntyRpstrRequestDto;
import saleson.shop.community.locgovdataboard.LocgovDataBoardService;
import saleson.shop.databoard.support.DataboardParam;

@Controller("locGovDataBoard")
@RequestMapping("/opmanager/community/databoard/")
@RequestProperty(title="지자체data-board", layout="default", template="opmanager")
public class LocGovDataBoardManagerController {

	private static final Logger log = LoggerFactory.getLogger(LocGovDataBoardManagerController.class);

	@Autowired
	private LocgovDataBoardService locgovDataBoardService;

	@Autowired
	private CmntyService cmntyService;

	@Autowired
	private CustomFileService customFileService;

	@GetMapping("list")
	public String list(RequestContext requestContext, @ModelAttribute("searchParam") CmntyRpstrRequestDto cmntyRpstrRequestDto , Model model) {

	   int count = cmntyService.countRpstr(cmntyRpstrRequestDto);

        Pagination pagination = Pagination.getInstance(count, cmntyRpstrRequestDto.getItemsPerPage());
        cmntyRpstrRequestDto.setPagination(pagination);

        String today = DateUtils.getToday(Const.DATE_FORMAT);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));
        model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
        model.addAttribute("list", cmntyService.listRpstr(cmntyRpstrRequestDto));
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);

        return "view:/community/databoard/list";

	}

	@PostMapping("list")
	public String searchList(RequestContext requestContext, @ModelAttribute("searchParam") CmntyRpstrRequestDto cmntyRpstrRequestDto , Model model) {

        int count = cmntyService.countRpstr(cmntyRpstrRequestDto);

        Pagination pagination = Pagination.getInstance(count, cmntyRpstrRequestDto.getItemsPerPage());
        cmntyRpstrRequestDto.setPagination(pagination);

        String today = DateUtils.getToday(Const.DATE_FORMAT);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));
        model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
        model.addAttribute("list", cmntyService.listRpstr(cmntyRpstrRequestDto));
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);

        return "view:/community/databoard/list";

	}

	@GetMapping("create")
	public String create(RequestContext requestContext, Model model, CmntyRpstrDto cmntyRpstr){

		//권한 가져오기
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

			//	ROLE_ADMIN_1	시스템주담당자 / ROLE_ADMIN_2	시스템부담당자
			//	ROLE_ADMIN_3	행안부주담당자 / ROLE_ADMIN_4	행안부부담당자
			//	ROLE_ADMIN_5	지자체주담당자 / ROLE_ADMIN_6	지자체부담당자 / ROLE_ADMIN_10	지정기부담당자

			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_10".equals(userRole.getAuthority())) {

				role = userRole.getAuthority();
			}
		}

		model.addAttribute("role",role);
		return "view:/community/databoard/form";
	}

	@PostMapping("create")
	public String createAction(RequestContext requestContext,
			CmntyRpstrDto cmntyRpstr,
    		@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles){
		cmntyRpstr.setRpstrCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "rpstrCn"));
        cmntyService.insertRpstr(cmntyRpstr, detailImageFiles);
        return ViewUtils.redirect("/opmanager/community/databoard/list","등록되었습니다.");
	 }

	@GetMapping("detail/{rpstrId}")
	public String detail(RequestContext requestContext, Model model, @PathVariable("rpstrId") long rpstrId) {

		 // 조회수 증가
		 cmntyService.updateRpstrInqCnt(rpstrId);

		 model.addAttribute("detail", cmntyService.getRpstrDetail(rpstrId,model));
 		 return "view:/community/databoard/detail";
	}

	@GetMapping("edit/{rpstrId}")
	public String edit(RequestContext requestContext, Model model, @PathVariable("rpstrId") long rpstrId){

		 model.addAttribute("cmntyRpstrDto", cmntyService.getRpstrDetail(rpstrId, model));
		 return "view:/community/databoard/form";
	}

	@PostMapping("edit/{rpstrId}")
	public String editAction(RequestContext requestContext, CmntyRpstrDto cmntyRpstr,
			@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles){

		cmntyRpstr.setRpstrCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "rpstrCn"));
		cmntyService.updateRpstr(cmntyRpstr,detailImageFiles);
		return ViewUtils.redirect("/opmanager/community/databoard/list", MessageUtils.getMessage("M00289"));
	}


	@PostMapping("delete/{rpstrId}")
	public JsonView delete(RequestContext requestContext, @PathVariable long rpstrId) {

		  if (!requestContext.isAjaxRequest()) {
	            throw new NotAjaxRequestException();
	      }

		  try {
			  cmntyService.deleteRpstr(rpstrId);
		  } catch (UserException e) {
			  return JsonViewUtils.exception("삭제에 실패했습니다.");
		  }
		  return JsonViewUtils.success();
	}


	@PostMapping("/deleteDataboard")
	public JsonView databoardListDelete(RequestContext requestContext, DataboardParam databoardParam) {

		String code = "";

		try {
			code = locgovDataBoardService.databoardListDelete(databoardParam);
		} catch (UserException e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

		return JsonViewUtils.success(code);

	}


	  /** Date 조회
	  *
	  * @param requestContextCREATED_DATE
	  * @return
	  */
	@PostMapping("search-date")
	public JsonView searchDate(RequestContext requestContext, Model model,
                            @RequestParam(value = "date", required = false) int date,
                            @RequestParam(value = "type", required = false) String type) {


		int month = 0;
		int day = 0;


		if (type.equals("week")) {
			day = date;
		} else if (type.equals("month")) {
			month = date;
		}

		String nowDate = DateUtils.getToday(Const.DATE_FORMAT);
		String searchDate = DateUtils.addYearMonthDay(nowDate, 0, month, day);


		model.addAttribute("nowDate", nowDate);
		model.addAttribute("searchDate", searchDate);

		return JsonViewUtils.success();
	}

	    /**
		 * 파일를 삭제한다.
		 * @param requestContext
		 * @param itemId
		 * @return
		 */
	@PostMapping("delete-item-image")
	public JsonView deleteItemImage(RequestContext requestContext, @RequestParam("fileId") long fileId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			cmntyService.deleteFileByFileId(fileId);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

		return JsonViewUtils.success();
	}

		/**
		 * 첨부파일 다운로드
		 * @param dataFileId
		 * @return
		 */
	@GetMapping("/file-download/{fileId}")
	public ResponseEntity<?> fileDownload(@PathVariable long fileId) throws IOException {

		CmntyFileDto cmntyFileDto = cmntyService.getRpstrFileDetail(fileId);
		File file = new File(CommonUtils.dataNvl(cmntyFileDto.getFileSrc()));


		if (file.exists()) {
			return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(cmntyFileDto.getOrgnlAtchFileNm()), file);
		}

		return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
	}

}
