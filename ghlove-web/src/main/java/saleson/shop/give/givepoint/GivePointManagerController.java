package saleson.shop.give.givepoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.common.Const;
import saleson.common.file.ExcelDownloadView;
import saleson.common.userauth.UserAuthService;
import saleson.common.utils.UserUtils;
import saleson.shop.give.givepoint.domain.GivePoint;
import saleson.shop.give.givepoint.support.GivePointDetailExcelView;
import saleson.shop.give.givepoint.support.GivePointExcelView;
import saleson.shop.give.givepoint.support.GivePointLocgovExcelView;

@Controller
@RequestMapping("/opmanager/give/give-point/**")
@RequestProperty(title="기부 포인트현황", layout="default", template="opmanager")
public class GivePointManagerController {
	private static final Logger log = LoggerFactory.getLogger(GivePointManagerController.class);

	@Autowired
	private GivePointService givePointService;

	@Autowired
	private CodeResolver codeResolver;

	@Autowired
	private UserAuthService userAuthService;


	/**
	 * 기부 포인트현황 목록
	 *
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("list")
	public String list(@ModelAttribute("searchParam") GivePoint searchParam , Model model) {

		String role = "";
		String today = DateUtils.getToday("yyyy");

		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {

//			ROLE_ADMIN_1	시스템주담당자
//			ROLE_ADMIN_2	시스템부담당자
//			ROLE_ADMIN_3	행안부주담당자
//			ROLE_ADMIN_4	행안부부담당자
//			ROLE_ADMIN_5	지자체주담당자
//			ROLE_ADMIN_6	지자체부담당자
			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("mois".equals(role)) {

			// 합계
			model.addAttribute("sum", new GivePoint());

			// 목록
			int count = 0;

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", Collections.EMPTY_LIST);
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-point/list";

		} else if ("locgov".equals(role)) {
			HashMap<String, Object> locgovObj = givePointService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			searchParam.setShLocgovCode(locgovCode);

			// 지자체 포인트 총합계
			model.addAttribute("sum", givePointService.getGivePointSum(searchParam));

			// 목록
			int count = givePointService.getGivePointLocgovListCount(searchParam);

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", givePointService.getGivePointLocgovList(searchParam));
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			// 지자체명
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCode);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());

			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-point/list_locgov";

		} else {
			return "redirect:/";
		}

	}

	/**
	 * 기부 포인트현황 목록
	 *
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@PostMapping("list")
	public String searchList(@ModelAttribute("searchParam") GivePoint searchParam , Model model) {

		String role = "";
		String today = DateUtils.getToday("yyyy");

		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {

//			ROLE_ADMIN_1	시스템주담당자
//			ROLE_ADMIN_2	시스템부담당자
//			ROLE_ADMIN_3	행안부주담당자
//			ROLE_ADMIN_4	행안부부담당자
//			ROLE_ADMIN_5	지자체주담당자
//			ROLE_ADMIN_6	지자체부담당자
			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("mois".equals(role)) {

			// 합계
			model.addAttribute("sum", givePointService.getGivePointSum(searchParam));

			// 목록
			int count = givePointService.getGivePointListCount(searchParam);

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", givePointService.getGivePointList(searchParam));
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-point/list";

		} else if ("locgov".equals(role)) {
			HashMap<String, Object> locgovObj = givePointService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			searchParam.setShLocgovCode(locgovCode);

			// 지자체 포인트 총합계
			model.addAttribute("sum", givePointService.getGivePointSum(searchParam));

			// 목록
			int count = givePointService.getGivePointLocgovListCount(searchParam);

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", givePointService.getGivePointLocgovList(searchParam));
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			// 지자체명
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCode);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());

			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-point/list_locgov";

		} else {
			return "redirect:/";
		}

	}

	/**
	 * 기부 포인트현황 상세현황
	 *
	 * @param model
	 * @param searchParam
	 * @return
	 */
	@GetMapping("detail")
	public String view(Model model
						, @RequestParam(name="locgovFullNm", defaultValue = "0") String locgovFullNm
						, @ModelAttribute("searchParam") GivePoint searchParam ){

		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {

			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			HashMap<String, Object> locgovObj = givePointService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			if (!locgovCode.equals(searchParam.getShLocgovCode())) {
				return "redirect:/";
			}
		}

		//기부년도 조회
		List<Code> codeList = CodeUtils.getCodeList("YYYY");
		List<Code> rstCdList = new ArrayList<>();
		int currentYear = Integer.parseInt(DateUtils.getToday("YYYY"));

		for(Code cd : codeList) {
			if( !cd.getId().equals("") && !cd.getId().equals("2022") && currentYear >= Integer.parseInt(cd.getId())) {
				rstCdList.add(cd);
			}
		}

		//기부년도
		model.addAttribute("yyyy", rstCdList);

		// 누적합계
		model.addAttribute("totalSum", givePointService.getGivePointSum(searchParam));

		GivePoint cntrSumResult= null;
		if(searchParam.getShUserName() != null && !searchParam.getShUserName().isBlank()) {
			cntrSumResult = givePointService.getGivePointDetailSum(searchParam);
		}else {
			cntrSumResult = givePointService.getGivePointDetail(searchParam);
		}

		model.addAttribute("cntrSum", cntrSumResult);

		// 목록(페이징)
		int count = givePointService.getGivePointDetailListCount(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", givePointService.getGivePointDetailList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		// 조회조건
		model.addAttribute("searchParam",searchParam);
		model.addAttribute("locgovFullNm", locgovFullNm);

		return "view:/give/give-point/form";
	}

	@PostMapping("detail")
	public String searchView(Model model
						, @RequestParam(name="locgovFullNm", defaultValue = "0") String locgovFullNm
						, @ModelAttribute("searchParam") GivePoint searchParam ){

		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {

			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			HashMap<String, Object> locgovObj = givePointService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			if (!locgovCode.equals(searchParam.getShLocgovCode())) {
				return "redirect:/";
			}
		}

		//기부년도 조회
		List<Code> codeList = CodeUtils.getCodeList("YYYY");
		List<Code> rstCdList = new ArrayList<>();
		int currentYear = Integer.parseInt(DateUtils.getToday("YYYY"));

		for(Code cd : codeList) {
			if( !cd.getId().equals("") && !cd.getId().equals("2022") && currentYear >= Integer.parseInt(cd.getId())) {
				rstCdList.add(cd);
			}
		}

		//기부년도
		model.addAttribute("yyyy", rstCdList);

		// 누적합계
		model.addAttribute("totalSum", givePointService.getGivePointSum(searchParam));

		GivePoint cntrSumResult= null;
		if(searchParam.getShUserName() != null && !searchParam.getShUserName().isBlank()) {
			cntrSumResult = givePointService.getGivePointDetailSum(searchParam);
		}else {
			cntrSumResult = givePointService.getGivePointDetail(searchParam);
		}

		model.addAttribute("cntrSum", cntrSumResult);

		// 목록(페이징)
		int count = givePointService.getGivePointDetailListCount(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", givePointService.getGivePointDetailList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		// 조회조건
		model.addAttribute("searchParam",searchParam);
		model.addAttribute("locgovFullNm", locgovFullNm);

		return "view:/give/give-point/form";
	}

	/**
	 * 엑셀 다운로드 (행안부)
	 * @return
	 */
	@SuppressWarnings({ "finally", "resource" })
	@GetMapping(value="list/download-excel")
	public ModelAndView downloadExcelProcessByList(GivePoint givePoint) {
		givePoint.setConditionType("EXCEL_DOWNLOAD");
		givePoint.setPagination(null);

		Pagination pagination = Pagination.getInstance(0);
		givePoint.setPagination(pagination);

		boolean isLocgov = false;

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "기부포인트현황목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = givePointService.streamGivePointList(givePoint, isLocgov);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 엑셀 다운로드 (지자체)
	 * @return
	 */
	@SuppressWarnings({ "finally", "resource" })
	@GetMapping(value="list/locgov/download-excel")
	public ModelAndView downloadExcelProcessByLocgovList(GivePoint givePoint) {
		HashMap<String, Object> locgovObj = givePointService.getLocgovCode(UserUtils.getUserId());
		String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
		givePoint.setShLocgovCode(locgovCode);

		givePoint.setConditionType("EXCEL_DOWNLOAD");
		givePoint.setPagination(null);

		Pagination pagination = Pagination.getInstance(0);
		givePoint.setPagination(pagination);

		boolean isLocgov = true;

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "기부포인트현황목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = givePointService.streamGivePointList(givePoint, isLocgov);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 엑셀 다운로드 (상세)
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping(value="detail/download-excel")
	public ModelAndView downloadExcelProcessByDetailList(GivePoint searchParam) {
		// ConditionType 설정
		searchParam.setConditionType("EXCEL_DOWNLOAD");
		// 목록(페이징)
		int totalCount = givePointService.getGivePointDetailListCount(searchParam);

		Pagination pagination = Pagination.getInstance(totalCount, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "기부포인트상세현황_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = givePointService.streamGivePointDetailData(searchParam, totalCount);
		} finally {
			// ItemReviewExcelView에 workbook과 암호화에 사용할 비밀번호를 담아서 전송
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}
}
