package saleson.shop.maintenance;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
import org.springframework.web.servlet.ModelAndView;

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

import saleson.common.Const;
import saleson.common.enumeration.mapper.EnumMapper;
import saleson.common.file.ExcelDownloadView;
import saleson.common.file.service.CustomFileService;
import saleson.common.userauth.UserAuthService;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.code.support.CodeParam;
import saleson.shop.user.LocgovService;


@Controller
@RequestMapping("/opmanager/maintenance/")
@RequestProperty(title="운영유지관리 SR게시판", layout="default", template="opmanager")
public class MaintenanceController {

	/** 지자체관리 Service */
    @Autowired
    LocgovService locgovService;

    /** 공통코드 Service */
	@Autowired
	private CodeService codeService;

    @Autowired
    private MaintenanceService maintenanceService;

	@Autowired
	private CustomFileService customFileService;

	@Autowired
	EnumMapper enumMapper;

	@Autowired
	private UserAuthService userAuthService;

	private static final Logger log = LoggerFactory.getLogger(MaintenanceController.class);

	/**
	 * <pre>
	 * comment       : 운영유지보수  sr 게시판 목록 화면
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * @param MaintenanceDto
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("/list")
	public String maintenanceList(@ModelAttribute("searchParam") MaintenanceDto maintenanceDto, RequestContext requestContext, Model model) {
		//날짜
		String today = DateUtils.getToday(Const.DATE_FORMAT);
		maintenanceDto.setStartDt(StringUtils.defaultIfEmpty(maintenanceDto.getStartDt(), today));
		maintenanceDto.setEndDt(StringUtils.defaultIfEmpty(maintenanceDto.getEndDt(), today));

		int count = maintenanceService.countMaintenance(maintenanceDto);

        //페이징
        Pagination pagination = Pagination.getInstance(count, maintenanceDto.getItemsPerPage());
        maintenanceDto.setPagination(pagination);


		model.addAttribute("list", maintenanceService.listMaintenance(maintenanceDto));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("procsStateCds", CodeUtils.getCodeList("MAINTEN_STATE_CODE"));			// 완료구분

		return "view:/maintenance/list";
	 }

	@PostMapping("/list")
	public String maintenanceListPost(@ModelAttribute("searchParam") MaintenanceDto maintenanceDto, RequestContext requestContext, Model model) {
		//날짜
		String today = DateUtils.getToday(Const.DATE_FORMAT);
		maintenanceDto.setStartDt(StringUtils.defaultIfEmpty(maintenanceDto.getStartDt(), today));
		maintenanceDto.setEndDt(StringUtils.defaultIfEmpty(maintenanceDto.getEndDt(), today));

		int count = maintenanceService.countMaintenance(maintenanceDto);

        //페이징
        Pagination pagination = Pagination.getInstance(count, maintenanceDto.getItemsPerPage());
        maintenanceDto.setPagination(pagination);


		model.addAttribute("list", maintenanceService.listMaintenance(maintenanceDto));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("procsStateCds", CodeUtils.getCodeList("MAINTEN_STATE_CODE"));			// 완료구분

		return "view:/maintenance/list";
	 }



	/**
	 * <pre>
	 * comment       : 운영유지보수  sr 게시글 등록 화면
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
	@GetMapping("/form")
	public String createMaintenance(MaintenanceDto maintenanceDto, RequestContext requestContext, Model model) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}
		// 0. 공통코드 조회 파라미터 셋팅
		CodeParam codeParam = new CodeParam();

		// 3. 연락처 공통코드 (핸드폰)
		codeParam.setCodeType("PHONE");
		model.addAttribute("phoneCodeList", codeService.getCodeChildList(codeParam));

		// 4. 연락처 공통코드 (전화번호)
		codeParam.setCodeType("TEL");
		model.addAttribute("telCodeList", codeService.getCodeChildList(codeParam));

		// 5. 요청경로(1: 소통방, 2:회의, 3:이메일, 4:SR게시판, 5:내부메신처, 6:SNS, 7:전화, 8:KLID, 9:사업장, 10:기타)
		codeParam.setCodeType("MAINTEN_PATH_CODE");

		model.addAttribute("role",role);
		model.addAttribute("maintenPathList", codeService.getCodeChildList(codeParam));
		return "view:/maintenance/form";
	}

	/**
	 * <pre>
	 * comment       : 운영유지보수  sr 게시글 등록
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
	@PostMapping("/form")
	public String createMaintenanceAction(MaintenanceDto maintenanceDto, RequestContext requestContext, Model model,
			@RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {
		User user = UserUtils.getUser();

		maintenanceDto.setBbsCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "bbsCn"));

		maintenanceService.insertMaintenance(maintenanceDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/maintenance/list","등록되었습니다.");
	}


	/**
	 * <pre>
	 * comment       : 운영유지보수  sr 게시글 상세 조회
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
	@GetMapping("/detail/{bbsId}")
	public String detailMaintenance(RequestContext requestContext, Model model, @PathVariable("bbsId") long bbsId) {

		 // 조회수 증가
		maintenanceService.updateMaintenanceInqCnt(bbsId);

		MaintenanceDto result = maintenanceService.getMaintenanceDetail(bbsId,model);

		model.addAttribute("detail", result);

		return "view:/maintenance/detail";
	}

	/**
	 * <pre>
	 * comment       : 운영유지보수  sr 게시글 파일 다운로드
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
	@GetMapping("/file-download/{fileId}")
	public ResponseEntity<?> fileDownloadMaintenance(@PathVariable long fileId) throws IOException {

	MaintenanceFileDto maintenanceFileDto = maintenanceService.getMaintenanceFileDetail(fileId);
	File file = new File(CommonUtils.dataNvl(maintenanceFileDto.getFileSrc()));


	if (file.exists()) {
		return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(maintenanceFileDto.getOrgnlAtchFileNm()), file);
	}else{
		String script = "<script>alert('파일이 존재하지 않습니다. 시스템관리자에 문의하여 주십시오.'); history.back();</script>";
		return ResponseEntity.ok().contentType(new MediaType("text","html",StandardCharsets.UTF_8)).body(script);
	}
	}


	/**
	 * <pre>
	 * comment       : 운영유지보수  sr 게시글 수정화면 조회
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
	@GetMapping("/edit/{bbsId}")
	public String editMaintenance(@ModelAttribute("searchParam") MaintenanceDto maintenanceDto, RequestContext requestContext, @PathVariable("bbsId") long bbsId, Model model) {

		// 0. 공통코드 조회 파라미터 셋팅
		CodeParam codeParam = new CodeParam();

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {

				role = userRole.getAuthority();
			}
		}

		maintenanceDto = maintenanceService.getMaintenanceDetail(bbsId,model);

		// 2. KI 리스트 (1: 홈페이지, 2:관리자시스템)
		codeParam.setCodeType("MAINTEN_KI_CODE");
		model.addAttribute("maintenKiList", codeService.getCodeChildList(codeParam));

		// 3. 연락처 공통코드 (핸드폰)
		codeParam.setCodeType("PHONE");
		model.addAttribute("phoneCodeList", codeService.getCodeChildList(codeParam));

		// 4. 연락처 공통코드 (전화번호)
		codeParam.setCodeType("TEL");
		model.addAttribute("telCodeList", codeService.getCodeChildList(codeParam));

		// 5. 요청경로(1: 소통방, 2:회의, 3:이메일, 4:SR게시판, 5:내부메신처, 6:SNS, 7:전화, 8:KLID, 9:사업장, 10:기타)
		codeParam.setCodeType("MAINTEN_PATH_CODE");
		model.addAttribute("maintenPathList", codeService.getCodeChildList(codeParam));

		// 6. 업무구분(1: 기부, 2:답례품, 3:기타)
		codeParam.setCodeType("MAINTEN_CATE_CODE");
		model.addAttribute("maintenCateList", codeService.getCodeChildList(codeParam));

		// 7. 완료구분(1: 접수, 2:처리중, 3:처리완료, 4:제외)
		codeParam.setCodeType("MAINTEN_STATE_CODE");
		model.addAttribute("maintenStateList", codeService.getCodeChildList(codeParam));


		model.addAttribute("role",role);
		model.addAttribute("maintenanceDto", maintenanceDto);

		 return "view:/maintenance/edit";
	}

	/**
	 * <pre>
	 * comment       : 운영유지보수  sr 게시글 수정
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
	@PostMapping("/edit/{bbsId}")
	public String updateMaintenance(@ModelAttribute("maintenanceDto") MaintenanceDto maintenanceDto, RequestContext requestContext, @RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {

		maintenanceDto.setBbsCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "bbsCn"));
		maintenanceService.updateMaintenance(maintenanceDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/maintenance/list", "수정에 성공하였습니다.");
	}

	/**
	 * <pre>
	 * comment       : 운영유지보수  sr 게시글 파일 삭제
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
	@PostMapping("/delete-item-image")
	public JsonView deleteMaintenanceFileByFileId(RequestContext requestContext, @RequestParam("fileId") long fileId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			maintenanceService.deleteMaintenanceFileByFileId(fileId);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

	return JsonViewUtils.success("파일이 삭제 되었습니다.");
}

	/**
	 * <pre>
	 * comment       : 운영유지보수  sr 게시글 삭제
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
	@PostMapping("/deleteMaintenance/{bbsId}")
	public JsonView deleteMaintenance(RequestContext requestContext, @PathVariable long bbsId) {

		  if (!requestContext.isAjaxRequest()) {
	            throw new NotAjaxRequestException();
	      }
		  try {
			  maintenanceService.deleteMaintenance(bbsId);
		  } catch (UserException e) {
			  return JsonViewUtils.exception("삭제에 실패했습니다.");
		  }
		  return JsonViewUtils.success("게시글이 삭제 되었습니다.");
	}

	@SuppressWarnings({ "finally", "resource" })
	@GetMapping("list/mainten-excel-download")
	public ModelAndView downloadMaintenanceExcelView(@ModelAttribute MaintenanceDto maintenanceDto, Model model, RequestContext requestContext) {
//		ModelAndView mav = new ModelAndView(new MaintenanceExcelView());
//		mav.addObject("srListMaintenance", maintenanceService.listMaintenance(maintenanceDto));//sr 리스트


		///////////////////////////////////////////////
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
		= "운영관리_SR리스트_"
		+ DateUtils.getToday(Const.DATETIME_FORMAT)
		+ ".xlsx";

		try {
			workbook = maintenanceService.streamAllMaintenanceData(maintenanceDto);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);
			return mav;
		}
		///////////////////////////////////////////////
	}

}
