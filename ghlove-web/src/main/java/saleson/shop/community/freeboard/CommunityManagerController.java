package saleson.shop.community.freeboard;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.api.common.exception.ApiException;
import saleson.common.Const;
import saleson.common.enumeration.mapper.EnumMapper;
import saleson.common.file.service.CustomFileService;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.community.CmntyService;
import saleson.shop.community.doamin.CmntyBbsDto;
import saleson.shop.community.doamin.CmntyBbsRequestDto;
import saleson.shop.community.doamin.CmntyCmntDto;
import saleson.shop.community.doamin.CmntyFaqBbsCmntDto;
import saleson.shop.community.doamin.CmntyFaqBbsCmntFileDto;
import saleson.shop.community.doamin.CmntyFaqBbsDto;
import saleson.shop.community.doamin.CmntyFaqBbsFileDto;
import saleson.shop.community.doamin.CmntyOffSrBbsCmntDto;
import saleson.shop.community.doamin.CmntyOffSrBbsCmntFileDto;
import saleson.shop.community.doamin.CmntyOffSrBbsDto;
import saleson.shop.community.doamin.CmntyOffSrBbsFileDto;
import saleson.shop.community.doamin.CmntySrBbsCmntDto;
import saleson.shop.community.doamin.CmntySrBbsCmntFileDto;
import saleson.shop.community.doamin.CmntySrBbsDto;
import saleson.shop.community.doamin.CmntySrBbsFileDto;
import saleson.shop.user.LocgovService;


@Controller
@RequestMapping("/opmanager/community/")
@RequestProperty(title="커뮤니티", layout="default", template="opmanager")
public class CommunityManagerController {

	/** 지자체관리 Service */
    @Autowired
    LocgovService locgovService;

    @Autowired
    private CmntyService cmntyService;

	@Autowired
	private CustomFileService customFileService;

	@Autowired
	EnumMapper enumMapper;

	private static final Logger log = LoggerFactory.getLogger(CommunityManagerController.class);

	/**
	 * <pre>
	 * comment       : 게시글 목록 화면
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 25.
	 *
	 * </pre>
	 * @param cmntyBbsRequestDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("bbs/list")
	public String list(@ModelAttribute("searchParam") CmntyBbsRequestDto cmntyBbsRequestDto, RequestContext requestContext, Model model) {

		//카운트
        int count = cmntyService.countBbs(cmntyBbsRequestDto);

        //페이징
        Pagination pagination = Pagination.getInstance(count, cmntyBbsRequestDto.getItemsPerPage());
        cmntyBbsRequestDto.setPagination(pagination);

		//날짜
		String today = DateUtils.getToday(Const.DATE_FORMAT);

		model.addAttribute("pagination", pagination);
		model.addAttribute("list", cmntyService.listBbs(cmntyBbsRequestDto));
		model.addAttribute("count", count);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드


		return "view:/community/freeboard/list";
	 }

	/**
	 * <pre>
	 * comment       : 게시글 목록 화면
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 25.
	 *
	 * </pre>
	 * @param cmntyBbsRequestDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@PostMapping("bbs/list")
	public String searchList(@ModelAttribute("searchParam") CmntyBbsRequestDto cmntyBbsRequestDto, RequestContext requestContext, Model model) {

		//카운트
        int count = cmntyService.countBbs(cmntyBbsRequestDto);

        //페이징
        Pagination pagination = Pagination.getInstance(count, cmntyBbsRequestDto.getItemsPerPage());
        cmntyBbsRequestDto.setPagination(pagination);

		//날짜
		String today = DateUtils.getToday(Const.DATE_FORMAT);

		model.addAttribute("pagination", pagination);
		model.addAttribute("list", cmntyService.listBbs(cmntyBbsRequestDto));
		model.addAttribute("count", count);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드


		return "view:/community/freeboard/list";
	 }


	/**
	 * <pre>
	 * comment       : 게시글 등록 화면
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 25.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("bbs/form")
	 public String createBbs(@ModelAttribute("searchParam") CmntyBbsDto cmntyBbsDto, RequestContext requestContext, Model model) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {

				role = userRole.getAuthority();
			}
		}
		model.addAttribute("role",role);
		return "view:/community/freeboard/form";
	 }

	/**
	 * <pre>
	 * comment       : 게시글 등록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 25.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("bbs/add")
	public JsonView add(@ModelAttribute("cmntyBbsInfo") CmntyBbsDto cmntyBbsDto, RequestContext requestContext) {
		try {
			cmntyService.addBbs(cmntyBbsDto, requestContext);
		} catch (UnsupportedEncodingException e) {
			return JsonViewUtils.failure("UnsupportedEncodingException");
		}

		 return JsonViewUtils.success("등록에 성공하였습니다.");
	 }

	/**
	 * <pre>
	 * comment       : 게시판 상세 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param cmntyBbsRequestDto
	 * @param requestContext
	 * @param bbsId
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("bbs/detail/{bbsId}")
	public String detailBbs(@ModelAttribute("searchParam") CmntyBbsRequestDto cmntyBbsRequestDto, RequestContext requestContext,@PathVariable("bbsId") long bbsId, Model model) {
		CmntyBbsDto cmntyBbsDto = cmntyService.detailBbs(requestContext, bbsId, model, "detail");

		if(cmntyBbsDto == null) {
			return ViewUtils.redirect("/opmanager/community/bbs/list", "해당 글은 존재하지 않습니다.");
		}

		if(cmntyBbsDto.getIsSecretYn().equalsIgnoreCase("Y")) {
			return ViewUtils.redirect("/opmanager/community/bbs/list", "비공개 처리된 글은 작성자만 확인 가능 합니다.");
		}

		return "view:/community/freeboard/detail";
	}

	/**
	 * <pre>
	 * comment       : 게시판 삭제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("bbs/delete/{bbsId}")
	public JsonView deleteBbs(RequestContext requestContext, @PathVariable("bbsId") int bbsId) {
		try {
			cmntyService.deleteBbs(requestContext, bbsId);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("삭제에 성공하였습니다.");
	}


	/**
	 * <pre>
	 * comment       : 수정화면 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 25.
	 *
	 * </pre>
	 * @param cmntyBbsRequestDto
	 * @param requestContext
	 * @param bbsId
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("bbs/edit/{bbsId}")
	public String editBbs(@ModelAttribute("searchParam") CmntyBbsRequestDto cmntyBbsRequestDto, RequestContext requestContext, @PathVariable("bbsId") long bbsId, Model model) {
		CmntyBbsDto cmntyBbsDto = cmntyService.detailBbs(requestContext, bbsId, model, "edit");
		if(cmntyBbsDto == null) {
			return ViewUtils.redirect("/opmanager/community/bbs/list", "해당 글은 존재하지 않습니다.");
		}

		if(cmntyBbsDto.getIsSecretYn().equalsIgnoreCase("Y")) {
			return ViewUtils.redirect("/opmanager/community/bbs/list", "비밀글은 본인만 확인 가능 합니다.");
		}

		 return "view:/community/freeboard/edit";
	}

	/**
	 * <pre>
	 * comment       : 수정
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 25.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("bbs/update")
	public JsonView updateBbs(@ModelAttribute("cmntyBbsInfo") CmntyBbsDto cmntyBbsDto, RequestContext requestContext) {
		try {
			cmntyService.updateBbs(cmntyBbsDto, requestContext);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			return JsonViewUtils.failure("UnsupportedEncodingException");
		}

		 return JsonViewUtils.success("수정에 성공하였습니다.");
	}


	/**
	 * <pre>
	 * comment       : 댓글 등록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 24.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("cmnt/add")
	public JsonView addCmnt(@ModelAttribute("cmntyCmntInfo") CmntyCmntDto cmntyCmntDto, RequestContext requestContext) {
		try {
			cmntyService.addCmnt(cmntyCmntDto, requestContext);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("등록에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : 댓글 삭제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 19.
	 *
	 * </pre>
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * JsonView
	 */
	@PostMapping("cmnt/delete/{cmntId}")
	public JsonView deleteCmnt(RequestContext requestContext, @PathVariable("cmntId") int cmntId) {
		try {
			cmntyService.deleteCmnt(requestContext, cmntId);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("삭제에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : 댓글 수정
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 24.
	 *
	 * </pre>
	 * @param cmntyCmntDto
	 * @param requestContext
	 * @param cmntId
	 * @return
	 * JsonView
	 */
	@PostMapping("cmnt/update")
	public JsonView updateCmntCn(@ModelAttribute("cmntyCmntInfo") CmntyCmntDto cmntyCmntDto, RequestContext requestContext) {
		try {
			cmntyService.updateCmntCn(cmntyCmntDto, requestContext);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("수정에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : sr 게시판 목록 화면
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param CmntySrBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@RequestMapping("srBbs/list")
	public String srBbsList(@ModelAttribute("searchParam") CmntySrBbsDto cmntySrBbsDto, RequestContext requestContext, Model model) {

		String requestMethod = requestContext.getRequest().getMethod();

		int count = cmntyService.countSrBbs(cmntySrBbsDto);

        //페이징
        Pagination pagination = Pagination.getInstance(count, cmntySrBbsDto.getItemsPerPage());
        cmntySrBbsDto.setPagination(pagination);

		//날짜
		String today = DateUtils.getToday(Const.DATE_FORMAT);

		if(requestMethod.equals("GET")) {
			model.addAttribute("list", cmntyService.listSrBbs(cmntySrBbsDto));
		}else if(requestMethod.equals("POST")) {
			model.addAttribute("list", cmntyService.listSrBbs(cmntySrBbsDto));
		}

		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		return "view:/community/srBbs/list";
	 }



	/**
	 * <pre>
	 * comment       : sr 게시글 등록 화면
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("srBbs/form")
	public String createSrBbs(CmntySrBbsDto cmntySrBbsDto, RequestContext requestContext, Model model) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {

				role = userRole.getAuthority();
			}
		}
		model.addAttribute("role",role);
		return "view:/community/srBbs/form";
	}

	/**
	 * <pre>
	 * comment       : sr 게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 15.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@PostMapping("srBbs/form")
	public String createSrBbsAction(CmntySrBbsDto cmntySrBbsDto, RequestContext requestContext, Model model,
			@RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {
		User user = UserUtils.getUser();

		cmntySrBbsDto.setBbsCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "bbsCn"));

		cmntyService.insertSrBbs(cmntySrBbsDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/community/srBbs/list","등록되었습니다.");
	}


	/**
	 * <pre>
	 * comment       : sr 게시글 상세 조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("srBbs/detail/{bbsId}")
	public String detail(RequestContext requestContext, Model model, @PathVariable("bbsId") long bbsId) {

		 // 조회수 증가
		cmntyService.updateSrBbsInqCnt(bbsId);

		CmntySrBbsDto result = cmntyService.getSrBbsDetail(bbsId,model);
		result.setCmntySrBbsCmntList(cmntyService.selectSrBbsCmntList(bbsId));

		if(result.getIsSecretYn().equals("Y")) {
			return ViewUtils.redirect("/opmanager/community/srBbs/list", "비밀글입니다.");
		}else{
			model.addAttribute("srBbsCmntListCnt", result.getCmntySrBbsCmntList().size());
			model.addAttribute("detail", result);
		}

		return "view:/community/srBbs/detail";
	}

	/**
	 * <pre>
	 * comment       : sr 게시글 파일 다운로드
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("srBbs/file-download/{fileId}")
	public ResponseEntity<?> fileDownload(@PathVariable long fileId) throws IOException {

	CmntySrBbsFileDto cmntySrBbsFileDto = cmntyService.getSrBbsFileDetail(fileId);
	File file = new File(CommonUtils.dataNvl(cmntySrBbsFileDto.getFileSrc()));


	if (file.exists()) {
		return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(cmntySrBbsFileDto.getOrgnlAtchFileNm()), file);
	}else{
		String script = "<script>alert('파일이 존재하지 않습니다. 시스템관리자에 문의하여 주십시오.'); history.back();</script>";
		return ResponseEntity.ok().contentType(new MediaType("text","html",StandardCharsets.UTF_8)).body(script);
	}
	}


	/**
	 * <pre>
	 * comment       : sr 게시글 수정화면 조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param cmntyBbsRequestDto
	 * @param requestContext
	 * @param bbsId
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("srBbs/edit/{bbsId}")
	public String editSrBbs(@ModelAttribute("searchParam") CmntySrBbsDto cmntySrBbsDto, RequestContext requestContext, @PathVariable("bbsId") long bbsId, Model model) {

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {

				role = userRole.getAuthority();
			}
		}

		cmntySrBbsDto = cmntyService.getSrBbsDetail(bbsId,model);

		if(cmntySrBbsDto.getIsSecretYn().equalsIgnoreCase("Y")) {
			return ViewUtils.redirect("/opmanager/community/srBbs/list", "비밀글은 본인만 수정 가능 합니다.");
		}

		model.addAttribute("role",role);
		model.addAttribute("cmntySrBbsDto", cmntySrBbsDto);

		 return "view:/community/srBbs/edit";
	}

	/**
	 * <pre>
	 * comment       : sr 게시글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("srBbs/edit/{bbsId}")
	public String updateSrBbs(@ModelAttribute("cmntySrBbsDto") CmntySrBbsDto cmntySrBbsDto, RequestContext requestContext, @RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {

		cmntySrBbsDto.setBbsCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "bbsCn"));
		cmntyService.updateSrBbs(cmntySrBbsDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/community/srBbs/list", "수정에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : sr 게시글 파일 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("srBbs/delete-item-image")
	public JsonView deleteSrBbsFileByFileId(RequestContext requestContext, @RequestParam("fileId") long fileId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			cmntyService.deleteSrBbsFileByFileId(fileId);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

	return JsonViewUtils.success("파일이 삭제 되었습니다.");
}

	/**
	 * <pre>
	 * comment       : sr 게시글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("srBbs/deleteSrBbs/{bbsId}")
	public JsonView deleteSrBbs(RequestContext requestContext, @PathVariable long bbsId) {

		  if (!requestContext.isAjaxRequest()) {
	            throw new NotAjaxRequestException();
	      }
		  try {
			  cmntyService.deleteSrBbs(bbsId);
		  } catch (UserException e) {
			  return JsonViewUtils.exception("삭제에 실패했습니다.");
		  }
		  return JsonViewUtils.success("게시글이 삭제 되었습니다.");
	}

	/**
	 * <pre>
	 * comment       : sr게시판 댓글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 18.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("srBbs/cmnt/add")
	public JsonView addSrBbsCmnt(@ModelAttribute("cmntySrBbsCmntDto") CmntySrBbsCmntDto cmntySrBbsCmntDto, RequestContext requestContext) {
		try {
			cmntyService.addSrBbsCmnt(cmntySrBbsCmntDto, requestContext);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("등록에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : sr게시판 댓글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 18.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("srBbs/cmnt/delete/{cmntId}")
	public JsonView deleteSrBbsCmnt(RequestContext requestContext, @PathVariable("cmntId") int cmntId) {
		try {
			cmntyService.deleteSrBbsCmnt(requestContext, cmntId);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("삭제에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : sr게시판 댓글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 18.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("srBbs/cmnt/update")
	public JsonView updateSrBbsCmntCn(@ModelAttribute("cmntySrBbsCmntDto") CmntySrBbsCmntDto cmntySrBbsCmntDto, RequestContext requestContext) {
		try {
			cmntyService.updateSrBbsCmnt(cmntySrBbsCmntDto, requestContext);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("수정에 성공하였습니다.");
	}


	/**
	 * <pre>
	 * comment       : sr게시판 댓글 파일등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 18.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@PostMapping("srBbs/cmnt/upload")
	public String srBbsCmntFileUpload(CmntySrBbsCmntDto cmntySrBbsCmntDto, RequestContext requestContext, Model model,
			@RequestParam(value="detailImageFile", required=false) MultipartFile[] detailImageFiles) {
		User user = UserUtils.getUser();

		cmntyService.fileUploadHandlerSrBbsCmnt(cmntySrBbsCmntDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/community/srBbs/detail/" +cmntySrBbsCmntDto.getBbsId(), "파일이 등록되었습니다.");
	}

	/**
	 * <pre>
	 * comment       : sr 게시글 댓글 파일 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("srBbs/cmnt/deleteFile")
	public JsonView deleteSrBbsCmntFileByFileId(RequestContext requestContext, @RequestParam("fileId") long fileId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		long userId = requestContext.getUser().getUserId();

		CmntySrBbsCmntFileDto cmntySrBbsCmntFileDto = new CmntySrBbsCmntFileDto();
		cmntySrBbsCmntFileDto.setFileId(fileId);
		cmntySrBbsCmntFileDto.setLastMdfcnId(userId);
		try {
			cmntyService.deleteSrBbsCmntFileByFileId(cmntySrBbsCmntFileDto);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

	return JsonViewUtils.success("파일이 삭제 되었습니다.");
}

	/**
	 * <pre>
	 * comment       : sr 게시글 댓글 파일 다운로드
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("srBbs/cmnt/file-download/{fileId}")
	public ResponseEntity<?> cmntFileDownload(@PathVariable long fileId) throws IOException {

		CmntySrBbsCmntFileDto cmntySrBbsCmntFileDto = cmntyService.getSrBbsCmntFileDetail(fileId);
		File file = new File(CommonUtils.dataNvl(cmntySrBbsCmntFileDto.getFileSrc()));

		if (file.exists()) {
			return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(cmntySrBbsCmntFileDto.getOrgnlAtchFileNm()), file);
		}else{
			String script = "<script>alert('파일이 존재하지 않습니다. 시스템관리자에 문의하여 주십시오.'); history.back();</script>";
			return ResponseEntity.ok().contentType(new MediaType("text","html",StandardCharsets.UTF_8)).body(script);
		}

	}

	/**
	 * <pre>
	 * comment       : faq 게시판 목록 화면
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 12.
	 *
	 * </pre>
	 * @param CmntyFaqBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@RequestMapping("faqBbs/list")
	public String faqList(@ModelAttribute("searchParam") CmntyFaqBbsDto cmntyFaqBbsDto, RequestContext requestContext, Model model) {

		String requestMethod = requestContext.getRequest().getMethod();
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}

		int count = cmntyService.countFaqBbs(cmntyFaqBbsDto);

        //페이징
        Pagination pagination = Pagination.getInstance(count, cmntyFaqBbsDto.getItemsPerPage());
        cmntyFaqBbsDto.setPagination(pagination);

		//날짜
		String today = DateUtils.getToday(Const.DATE_FORMAT);

		if(requestMethod.equals("GET")) {
			model.addAttribute("list", cmntyService.listFaqBbs(cmntyFaqBbsDto));
		}else if(requestMethod.equals("POST")) {
			model.addAttribute("list", cmntyService.listFaqBbs(cmntyFaqBbsDto));
		}

		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("role",role);

		model.addAttribute("faqTypes", enumMapper.get("FaqType"));

		return "view:/community/faqBbs/list";
	 }



	/**
	 * <pre>
	 * comment       : faq 게시글 등록 화면
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 15.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("faqBbs/form")
	public String createFaq(CmntyFaqBbsDto cmntyFaqBbsDto, RequestContext requestContext, Model model) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}
		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager/community/faqBbs/list", "FAQ 작성 권한이 없습니다.");
		}

		model.addAttribute("role",role);
		model.addAttribute("faqTypes", enumMapper.get("FaqType"));
		return "view:/community/faqBbs/form";
	}

	/**
	 * <pre>
	 * comment       : faq 게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 15.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@PostMapping("faqBbs/form")
	public String createFaqAction(CmntyFaqBbsDto cmntyFaqBbsDto, RequestContext requestContext, Model model,
			@RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}
		// ROLE_ADMIN_1만 등록수정삭제 가능
		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager/community/faqBbs/list", "FAQ 작성 권한이 없습니다.");
		}

		cmntyFaqBbsDto.setBbsCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "bbsCn"));

		cmntyService.insertFaqBbs(cmntyFaqBbsDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/community/faqBbs/list","등록되었습니다.");
	}


	/**
	 * <pre>
	 * comment       : faq 게시글 상세 조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("faqBbs/detail/{bbsId}")
	public String detailFaq(RequestContext requestContext, Model model, @PathVariable("bbsId") long bbsId) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}

		 // 조회수 증가
		cmntyService.updateFaqBbsInqCnt(bbsId);

		CmntyFaqBbsDto result = cmntyService.getFaqBbsDetail(bbsId,model);
		if(result == null) {
			return ViewUtils.redirect("/opmanager/community/faqBbs/list", "존재하지 않는 게시글입니다.");
		}

		result.setCmntyFaqBbsCmntList(cmntyService.selectFaqBbsCmntList(bbsId));

		if(result.getIsSecretYn().equals("Y")) {
			return ViewUtils.redirect("/opmanager/community/faqBbs/list", "비밀글입니다.");
		}else{
			model.addAttribute("faqBbsCmntListCnt", result.getCmntyFaqBbsCmntList().size());
			model.addAttribute("detail", result);
		}

		model.addAttribute("role",role);
		return "view:/community/faqBbs/detail";
	}

	/**
	 * <pre>
	 * comment       : faq 게시글 파일 다운로드
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("faqBbs/file-download/{fileId}")
	public ResponseEntity<?> fileDownloadFaq(@PathVariable long fileId) throws IOException {

	CmntyFaqBbsFileDto cmntyFaqBbsFileDto = cmntyService.getFaqBbsFileDetail(fileId);
	File file = new File(CommonUtils.dataNvl(cmntyFaqBbsFileDto.getFileSrc()));


	if (file.exists()) {
		return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(cmntyFaqBbsFileDto.getOrgnlAtchFileNm()), file);
	}else{
		String script = "<script>alert('파일이 존재하지 않습니다. 시스템관리자에 문의하여 주십시오.'); history.back();</script>";
		return ResponseEntity.ok().contentType(new MediaType("text","html",StandardCharsets.UTF_8)).body(script);
	}
	}


	/**
	 * <pre>
	 * comment       : faq 게시글 수정화면 조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param cmntyBbsRequestDto
	 * @param requestContext
	 * @param bbsId
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("faqBbs/edit/{bbsId}")
	public String editFaq(@ModelAttribute("searchParam") CmntyFaqBbsDto cmntyFaqBbsDto, RequestContext requestContext, @PathVariable("bbsId") long bbsId, Model model) {

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}
		// ROLE_ADMIN_1만 등록수정삭제 가능
		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager/community/faqBbs/list", "FAQ 수정 권한이 없습니다.");
		}

		cmntyFaqBbsDto = cmntyService.getFaqBbsDetail(bbsId,model);
		if(cmntyFaqBbsDto == null) {
			return ViewUtils.redirect("/opmanager/community/faqBbs/list", "존재하지 않는 게시글입니다.");
		}

		/*
		 * if(cmntyFaqBbsDto.getIsSecretYn().equalsIgnoreCase("Y")) { return
		 * ViewUtils.redirect("/opmanager/community/faqBbs/list",
		 * "비밀글은 본인만 수정 가능 합니다."); }
		 */

		model.addAttribute("role",role);
		model.addAttribute("cmntyFaqBbsDto", cmntyFaqBbsDto);
		model.addAttribute("faqTypes", enumMapper.get("FaqType"));

		 return "view:/community/faqBbs/edit";
	}

	/**
	 * <pre>
	 * comment       : faq 게시글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("faqBbs/edit/{bbsId}")
	public String updateFaq(@ModelAttribute("cmntyFaqBbsDto") CmntyFaqBbsDto cmntyFaqBbsDto, RequestContext requestContext, @RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}
		// ROLE_ADMIN_1만 등록수정삭제 가능
		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager/community/faqBbs/list", "FAQ 수정 권한이 없습니다.");
		}


		cmntyFaqBbsDto.setBbsCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "bbsCn"));
		cmntyService.updateFaqBbs(cmntyFaqBbsDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/community/faqBbs/list", "수정에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : faq 게시글 파일 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("faqBbs/delete-item-image")
	public JsonView deleteFaqFileByFileId(RequestContext requestContext, @RequestParam("fileId") long fileId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			cmntyService.deleteFaqBbsFileByFileId(fileId);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

	return JsonViewUtils.success("파일이 삭제 되었습니다.");
}

	/**
	 * <pre>
	 * comment       : faq 게시글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("faqBbs/deleteFaqBbs/{bbsId}")
	public JsonView deleteFaq(RequestContext requestContext, @PathVariable long bbsId) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}
		// ROLE_ADMIN_1만 등록수정삭제 가능
		if(role.isEmpty()) {
			return JsonViewUtils.exception("게시글 삭제 권한이 없습니다.");
		}

		if (!requestContext.isAjaxRequest()) {
	            throw new NotAjaxRequestException();
	    }
		try {
			  cmntyService.deleteFaqBbs(bbsId);
		} catch (UserException e) {
			  return JsonViewUtils.exception("삭제에 실패했습니다.");
		}

		return JsonViewUtils.success("게시글이 삭제 되었습니다.");
	}

	/**
	 * <pre>
	 * comment       : faq게시판 댓글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 18.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("faqBbs/cmnt/add")
	public JsonView addFaqCmnt(@ModelAttribute("cmntyFaqBbsCmntDto") CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto, RequestContext requestContext) {
		try {
			cmntyService.addFaqBbsCmnt(cmntyFaqBbsCmntDto, requestContext);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("등록에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : faq게시판 댓글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 18.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("faqBbs/cmnt/delete/{cmntId}")
	public JsonView deleteFaqCmnt(RequestContext requestContext, @PathVariable("cmntId") int cmntId) {
		try {
			cmntyService.deleteFaqBbsCmnt(requestContext, cmntId);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("삭제에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : faq게시판 댓글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 18.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("faqBbs/cmnt/update")
	public JsonView updateFaqCmntCn(@ModelAttribute("cmntyFaqBbsCmntDto") CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto, RequestContext requestContext) {
		try {
			cmntyService.updateFaqBbsCmnt(cmntyFaqBbsCmntDto, requestContext);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("수정에 성공하였습니다.");
	}


	/**
	 * <pre>
	 * comment       : faq게시판 댓글 파일등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 18.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@PostMapping("faqBbs/cmnt/upload")
	public String FaqCmntFileUpload(CmntyFaqBbsCmntDto cmntyFaqBbsCmntDto, RequestContext requestContext, Model model,
			@RequestParam(value="detailImageFile", required=false) MultipartFile[] detailImageFiles) {
		User user = UserUtils.getUser();

		cmntyService.fileUploadHandlerFaqBbsCmnt(cmntyFaqBbsCmntDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/community/faqBbs/detail/" +cmntyFaqBbsCmntDto.getBbsId(), "파일이 등록되었습니다.");
	}

	/**
	 * <pre>
	 * comment       : faq 게시글 댓글 파일 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("faqBbs/cmnt/deleteFile")
	public JsonView deleteFaqCmntFileByFileId(RequestContext requestContext, @RequestParam("fileId") long fileId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		long userId = requestContext.getUser().getUserId();

		CmntyFaqBbsCmntFileDto cmntyFaqBbsCmntFileDto = new CmntyFaqBbsCmntFileDto();
		cmntyFaqBbsCmntFileDto.setFileId(fileId);
		cmntyFaqBbsCmntFileDto.setLastMdfcnId(userId);
		try {
			cmntyService.deleteFaqBbsCmntFileByFileId(cmntyFaqBbsCmntFileDto);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

	return JsonViewUtils.success("파일이 삭제 되었습니다.");
}

	/**
	 * <pre>
	 * comment       : faq 게시글 댓글 파일 다운로드
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("faqBbs/cmnt/file-download/{fileId}")
	public ResponseEntity<?> cmntFileDownloadFaq(@PathVariable long fileId) throws IOException {

		CmntyFaqBbsCmntFileDto cmntyFaqBbsCmntFileDto = cmntyService.getFaqBbsCmntFileDetail(fileId);
		File file = new File(CommonUtils.dataNvl(cmntyFaqBbsCmntFileDto.getFileSrc()));

		if (file.exists()) {
			return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(cmntyFaqBbsCmntFileDto.getOrgnlAtchFileNm()), file);
		}else{
			String script = "<script>alert('파일이 존재하지 않습니다. 시스템관리자에 문의하여 주십시오.'); history.back();</script>";
			return ResponseEntity.ok().contentType(new MediaType("text","html",StandardCharsets.UTF_8)).body(script);
		}

	}










	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 목록 화면
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param CmntyOffSrBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@RequestMapping("offSrBbs/list")
	public String offSrBbsList(@ModelAttribute("searchParam") CmntyOffSrBbsDto cmntyOffSrBbsDto, RequestContext requestContext, Model model) {

		String requestMethod = requestContext.getRequest().getMethod();
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}

		int count = cmntyService.countOffSrBbs(cmntyOffSrBbsDto);

        //페이징
        Pagination pagination = Pagination.getInstance(count, cmntyOffSrBbsDto.getItemsPerPage());
        cmntyOffSrBbsDto.setPagination(pagination);

		//날짜
		String today = DateUtils.getToday(Const.DATE_FORMAT);

		if(requestMethod.equals("GET")) {
			model.addAttribute("list", cmntyService.listOffSrBbs(cmntyOffSrBbsDto));
		}else if(requestMethod.equals("POST")) {
			model.addAttribute("list", cmntyService.listOffSrBbs(cmntyOffSrBbsDto));
		}

		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));
		model.addAttribute("offBanks", CodeUtils.getCodeList("OFF_BANK_LIST"));			// 지자체코드
		model.addAttribute("role",role);

		return "view:/community/offSrBbs/list";
	 }



	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 등록 화면
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("offSrBbs/form")
	public String createOffSrBbs(CmntyOffSrBbsDto cmntyOffSrBbsDto, RequestContext requestContext, Model model) {

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_7".equals(userRole.getAuthority()) || "ROLE_ADMIN_8".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}

		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager/community/offSrBbs/list", "게시글 작성 권한이 없습니다.");
		}

		model.addAttribute("role",role);
		return "view:/community/offSrBbs/form";
	}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@PostMapping("offSrBbs/form")
	public String createOffSrBbsAction(CmntyOffSrBbsDto cmntyOffSrBbsDto, RequestContext requestContext, Model model,
			@RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_7".equals(userRole.getAuthority()) || "ROLE_ADMIN_8".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}

		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager/community/offSrBbs/list", "게시글 작성 권한이 없습니다.");
		}

		cmntyOffSrBbsDto.setBbsCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "bbsCn"));

		cmntyService.insertOffSrBbs(cmntyOffSrBbsDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/community/offSrBbs/list","등록되었습니다.");
	}


	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 상세 조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("offSrBbs/detail/{bbsId}")
	public String detailOffSrBbs(RequestContext requestContext, Model model, @PathVariable("bbsId") long bbsId) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_7".equals(userRole.getAuthority()) || "ROLE_ADMIN_8".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}

		 // 조회수 증가
		cmntyService.updateOffSrBbsInqCnt(bbsId);

		CmntyOffSrBbsDto result = cmntyService.getOffSrBbsDetail(bbsId,model);
		if(result == null) {
			return ViewUtils.redirect("/opmanager/community/offSrBbs/list", "존재하지 않는 게시글입니다.");
		}

		result.setCmntyOffSrBbsCmntList(cmntyService.selectOffSrBbsCmntList(bbsId));

		if(result.getIsSecretYn().equals("Y")) {
			return ViewUtils.redirect("/opmanager/community/offSrBbs/list", "비밀글입니다.");
		}else{
			model.addAttribute("offSrBbsCmntListCnt", result.getCmntyOffSrBbsCmntList().size());
			model.addAttribute("detail", result);
		}

		model.addAttribute("role",role);
		return "view:/community/offSrBbs/detail";
	}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 파일 다운로드
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("offSrBbs/file-download/{fileId}")
	public ResponseEntity<?> fileDownloadOffSrBbs(@PathVariable long fileId) throws IOException {

	CmntyOffSrBbsFileDto cmntyOffSrBbsFileDto = cmntyService.getOffSrBbsFileDetail(fileId);
	File file = new File(CommonUtils.dataNvl(cmntyOffSrBbsFileDto.getFileSrc()));


	if (file.exists()) {
		return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(cmntyOffSrBbsFileDto.getOrgnlAtchFileNm()), file);
	}else{
		String script = "<script>alert('파일이 존재하지 않습니다. 시스템관리자에 문의하여 주십시오.'); history.back();</script>";
		return ResponseEntity.ok().contentType(new MediaType("text","html",StandardCharsets.UTF_8)).body(script);
	}
	}


	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 수정화면 조회
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsRequestDto
	 * @param requestContext
	 * @param bbsId
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("offSrBbs/edit/{bbsId}")
	public String editOffSrBbs(@ModelAttribute("searchParam") CmntyOffSrBbsDto cmntyOffSrBbsDto, RequestContext requestContext, @PathVariable("bbsId") long bbsId, Model model) {

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_7".equals(userRole.getAuthority()) || "ROLE_ADMIN_8".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}

		cmntyOffSrBbsDto = cmntyService.getOffSrBbsDetail(bbsId,model);
		if(cmntyOffSrBbsDto == null) {
			return ViewUtils.redirect("/opmanager/community/offSrBbs/list", "존재하지 않는 게시글입니다.");
		}

		if(cmntyOffSrBbsDto.getIsSecretYn().equalsIgnoreCase("Y")) {
			return ViewUtils.redirect("/opmanager/community/offSrBbs/list", "비밀글은 본인만 수정 가능 합니다.");
		}

		if(cmntyOffSrBbsDto.getFrstCrtId() != user.getUserId() && !(role.equals("ROLE_ADMIN_1") || role.equals("ROLE_ADMIN_2")) ) {
			return ViewUtils.redirect("/opmanager/community/offSrBbs/list", "본인만 수정 가능 합니다.");
		}

		/*
		 * if(cmntyOffSrBbsDto.getIsSecretYn().equalsIgnoreCase("Y")) { return
		 * ViewUtils.redirect("/opmanager/community/offSrBbs/list",
		 * "비밀글은 본인만 수정 가능 합니다."); }
		 */

		model.addAttribute("role",role);
		model.addAttribute("cmntyOffSrBbsDto", cmntyOffSrBbsDto);

		 return "view:/community/offSrBbs/edit";
	}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("offSrBbs/edit/{bbsId}")
	public String updateOffSrBbs(@ModelAttribute("cmntyOffSrBbsDto") CmntyOffSrBbsDto cmntyOffSrBbsDto, RequestContext requestContext, @RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {
		cmntyOffSrBbsDto.setBbsCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "bbsCn"));
		cmntyService.updateOffSrBbs(cmntyOffSrBbsDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/community/offSrBbs/list", "수정에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 파일 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("offSrBbs/delete-item-image")
	public JsonView deleteOffSrBbsFileByFileId(RequestContext requestContext, @RequestParam("fileId") long fileId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			cmntyService.deleteOffSrBbsFileByFileId(fileId);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

	return JsonViewUtils.success("파일이 삭제 되었습니다.");
}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("offSrBbs/deleteOffSrBbs/{bbsId}")
	public JsonView deleteOffSrBbs(RequestContext requestContext, @PathVariable long bbsId) {
		if (!requestContext.isAjaxRequest()) {
	            throw new NotAjaxRequestException();
	    }
		try {
			  cmntyService.deleteOffSrBbs(bbsId);
		} catch (UserException e) {
			  return JsonViewUtils.exception("삭제에 실패했습니다.");
		}

		return JsonViewUtils.success("게시글이 삭제 되었습니다.");
	}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 댓글 등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("offSrBbs/cmnt/add")
	public JsonView addOffSrBbsCmnt(@ModelAttribute("cmntyOffSrBbsCmntDto") CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto, RequestContext requestContext) {
		try {
			cmntyService.addOffSrBbsCmnt(cmntyOffSrBbsCmntDto, requestContext);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("등록에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 댓글 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("offSrBbs/cmnt/delete/{cmntId}")
	public JsonView deleteOffSrBbsCmnt(RequestContext requestContext, @PathVariable("cmntId") int cmntId) {
		try {
			cmntyService.deleteOffSrBbsCmnt(requestContext, cmntId);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("삭제에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 댓글 수정
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param requestContext
	 * @param bbsId
	 * @return
	 * JsonView
	 */
	@PostMapping("offSrBbs/cmnt/update")
	public JsonView updateOffSrBbsCmntCn(@ModelAttribute("cmntyOffSrBbsCmntDto") CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto, RequestContext requestContext) {
		try {
			cmntyService.updateOffSrBbsCmnt(cmntyOffSrBbsCmntDto, requestContext);
		} catch (ApiException e) {
			return JsonViewUtils.failure(e.getMessage());
		}

		return JsonViewUtils.success("수정에 성공하였습니다.");
	}


	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시판 댓글 파일등록
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@PostMapping("offSrBbs/cmnt/upload")
	public String OffSrBbsCmntFileUpload(CmntyOffSrBbsCmntDto cmntyOffSrBbsCmntDto, RequestContext requestContext, Model model,
			@RequestParam(value="detailImageFile", required=false) MultipartFile[] detailImageFiles) {
		cmntyService.fileUploadHandlerOffSrBbsCmnt(cmntyOffSrBbsCmntDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/community/offSrBbs/detail/" +cmntyOffSrBbsCmntDto.getBbsId(), "파일이 등록되었습니다.");
	}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 댓글 파일 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("offSrBbs/cmnt/deleteFile")
	public JsonView deleteOffSrBbsCmntFileByFileId(RequestContext requestContext, @RequestParam("fileId") long fileId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		long userId = requestContext.getUser().getUserId();

		CmntyOffSrBbsCmntFileDto cmntyOffSrBbsCmntFileDto = new CmntyOffSrBbsCmntFileDto();
		cmntyOffSrBbsCmntFileDto.setFileId(fileId);
		cmntyOffSrBbsCmntFileDto.setLastMdfcnId(userId);
		try {
			cmntyService.deleteOffSrBbsCmntFileByFileId(cmntyOffSrBbsCmntFileDto);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

	return JsonViewUtils.success("파일이 삭제 되었습니다.");
}

	/**
	 * <pre>
	 * comment       : 오프라인담당자 sr게시글 댓글 파일 다운로드
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 6. 08.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("offSrBbs/cmnt/file-download/{fileId}")
	public ResponseEntity<?> cmntFileDownloadOffSrBbs(@PathVariable long fileId) throws IOException {

		CmntyOffSrBbsCmntFileDto cmntyOffSrBbsCmntFileDto = cmntyService.getOffSrBbsCmntFileDetail(fileId);
		File file = new File(CommonUtils.dataNvl(cmntyOffSrBbsCmntFileDto.getFileSrc()));

		if (file.exists()) {
			return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(cmntyOffSrBbsCmntFileDto.getOrgnlAtchFileNm()), file);
		}else{
			String script = "<script>alert('파일이 존재하지 않습니다. 시스템관리자에 문의하여 주십시오.'); history.back();</script>";
			return ResponseEntity.ok().contentType(new MediaType("text","html",StandardCharsets.UTF_8)).body(script);
		}

	}
}
