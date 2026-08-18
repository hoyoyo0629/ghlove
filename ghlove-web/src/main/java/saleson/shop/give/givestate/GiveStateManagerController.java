package saleson.shop.give.givestate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.common.Const;
import saleson.common.file.ExcelDownloadView;
import saleson.common.userauth.UserAuthService;
import saleson.common.utils.UserUtils;
import saleson.shop.donation.NextBugaRequestDto;
import saleson.shop.donation.NgDonationRelayService;
import saleson.shop.donation.NgDonationService;
import saleson.shop.donation.support.ContryParam;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.givestate.domain.GiveStateCntrUsePoint;
import saleson.shop.give.givestate.domain.GiveStateTest;
import saleson.shop.give.givestate.support.GiveStateAllExcelView;
import saleson.shop.give.givestate.support.GiveStateDetailExcelView;
import saleson.shop.give.givestate.support.GiveStateExcelView;
import saleson.shop.give.givestate.support.GiveStateLocgovExcelView;

@Controller
@RequestMapping("/opmanager/give/give-state/**")
@RequestProperty(title="기부금 모금현황", layout="default", template="opmanager")
public class GiveStateManagerController {
	private static final Logger log = LoggerFactory.getLogger(GiveStateManagerController.class);

	@Autowired
	private GiveStateService giveStateService;

	@Autowired
	private NgDonationRelayService ngDonationRelayService;

	@Autowired
	private NgDonationService ngDonationService;

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	SequenceService sequenceService;


	/**
	 * 기부금 모금현황 목록
	 *
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("list")
	public String list(@ModelAttribute("searchParam") GiveState searchParam , Model model) {

		String today = DateUtils.getToday("yyyy");
		searchParam.setShCntrYear(StringUtils.defaultIfEmpty(searchParam.getShCntrYear(), today));



		String role = "";
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
			if(searchParam.getIsSubmit() != null && searchParam.getIsSubmit()) {
				// 합계
				model.addAttribute("sum", StringUtils.EMPTY);

				// 목록
				int count = 0;

				Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
				searchParam.setPagination(pagination);

				model.addAttribute("list", Collections.EMPTY_LIST);
				model.addAttribute("pagination", pagination);
				model.addAttribute("count", count);

			}

			List<Code> codeList = CodeUtils.getCodeList("YYYY");
			List<Code> rstCdList = new ArrayList<>();
			for(Code cd : codeList) {
				if(!cd.getId().equals("2022") && !cd.getId().equals("")) {
					rstCdList.add(cd);
				}
			}

			// 코드
			model.addAttribute("yyyy", rstCdList);	// 년도
			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-state/list";

		} else if ("locgov".equals(role)) {
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			searchParam.setShLocgovCode(locgovCode);

			// 합계
			model.addAttribute("sum", new GiveState());

			// 목록
			int count = 0;

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", Collections.EMPTY_LIST);
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			List<Code> codeList = CodeUtils.getCodeList("YYYY");
			List<Code> rstCdList = new ArrayList<>();
			for(Code cd : codeList) {
				if(!cd.getId().equals("2022") && !cd.getId().equals("")) {
					rstCdList.add(cd);
				}
			}

			// 코드
			model.addAttribute("yyyy", rstCdList);	// 년도

			// 지자체명
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCode);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());

			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-state/list_locgov";

		} else {
			return "redirect:/";
		}

	}

	/**
	 * 기부금 모금현황 목록
	 *
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@PostMapping("list")
	public String searchList(@ModelAttribute("searchParam") GiveState searchParam , Model model) {
		String today = DateUtils.getToday("yyyy");
		searchParam.setShCntrYear(StringUtils.defaultIfEmpty(searchParam.getShCntrYear(), today));



		String role = "";
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
			if(searchParam.getIsSubmit() != null && searchParam.getIsSubmit()) {
				// 합계
				model.addAttribute("sum", giveStateService.getGiveStateSum(searchParam));

				// 목록
				int count = giveStateService.getGiveStateListCount(searchParam);

				Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
				searchParam.setPagination(pagination);

				model.addAttribute("list", giveStateService.getGiveStateList(searchParam));
				model.addAttribute("pagination", pagination);
				model.addAttribute("count", count);

			}

			List<Code> codeList = CodeUtils.getCodeList("YYYY");
			List<Code> rstCdList = new ArrayList<>();
			for(Code cd : codeList) {
				if(!cd.getId().equals("2022") && !cd.getId().equals("")) {
					rstCdList.add(cd);
				}
			}

			// 코드
			model.addAttribute("yyyy", rstCdList);	// 년도
			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-state/list";

		} else if ("locgov".equals(role)) {
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			searchParam.setShLocgovCode(locgovCode);

			// 합계
			model.addAttribute("sum", giveStateService.getGiveStateSum(searchParam));

			// 목록
			int count = giveStateService.getGiveStateListCount(searchParam);

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", giveStateService.getGiveStateList(searchParam));
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			List<Code> codeList = CodeUtils.getCodeList("YYYY");
			List<Code> rstCdList = new ArrayList<>();
			for(Code cd : codeList) {
				if(!cd.getId().equals("2022") && !cd.getId().equals("")) {
					rstCdList.add(cd);
				}
			}

			// 코드
			model.addAttribute("yyyy", rstCdList);	// 년도

			// 지자체명
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCode);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());

			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-state/list_locgov";

		} else {
			return "redirect:/";
		}
	}

	/**
	 * 기부금 모금 상세현황
	 *
	 * @param model
	 * @param searchParam
	 * @return
	 */
	@GetMapping("detail")
	public String view(Model model
						, @RequestParam(name="locgovFullNm", defaultValue = "0") String locgovFullNm
						, @ModelAttribute("searchParam") GiveState searchParam ){

		String today1 = DateUtils.getToday("yyyyMMdd");
//		searchParam.setShCntrDeStart(StringUtils.defaultIfEmpty(searchParam.getShCntrDeStart(), today1));
		searchParam.setShCntrDeStart(StringUtils.defaultIfEmpty(searchParam.getShCntrDeStart(), today1.substring(0, 6).concat("01"))); // 해당월 1일자로 변경
		searchParam.setShCntrDeEnd(StringUtils.defaultIfEmpty(searchParam.getShCntrDeEnd(), today1));

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
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			if (!locgovCode.equals(searchParam.getShLocgovCode())) {
				return "redirect:/";
			}
		}

		// 누적합계
		model.addAttribute("currentSum", giveStateService.getGiveStateDetail(searchParam));

		// 검색합계
		model.addAttribute("sum", giveStateService.getGiveStateDetailSum(searchParam));

		// 목록(페이징)
		int count = giveStateService.getGiveStateDetailListCount(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", giveStateService.getGiveStateDetailList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		// 조회조건
		String today = DateUtils.getToday(Const.DATE_FORMAT);
		model.addAttribute("today", today);
		model.addAttribute("yesterday", DateUtils.addDay(today, -1)); // 어제 추가
		model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
		model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
		model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
		model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));

		model.addAttribute("searchParam",searchParam);
		model.addAttribute("locgovFullNm", locgovFullNm);

		model.addAttribute("linkInsttCdList", CodeUtils.getCodeList("LINK_INSTT_CD"));	// 민간연계기관 목록

		return "view:/give/give-state/form";
	}

	/**
	 * 기부금 전체 현황 (확인용 TEST)
	 *
	 * @param model
	 * @param searchParam
	 * @return
	 */
	@GetMapping("detail_list")
	public String viewTest(Model model, @ModelAttribute("searchParam") GiveState searchParam ){


		// 목록(페이징)
		String todays = DateUtils.getToday("yyyyMMdd");
		searchParam.setShCntrDeStart(StringUtils.defaultIfEmpty(searchParam.getShCntrDeStart(), todays));
		searchParam.setShCntrDeEnd(StringUtils.defaultIfEmpty(searchParam.getShCntrDeEnd(), todays));



		// 조회조건
		String today = DateUtils.getToday(Const.DATE_FORMAT);
		model.addAttribute("today", today);
		model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
		model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
		model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
		model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));

		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("linkInsttCdList", CodeUtils.getCodeList("LINK_INSTT_CD"));	// 민간연계기관 목록
		model.addAttribute("searchParam",searchParam);

		return "view:/give/give-state/cntr_status";
	}

	@PostMapping("detail_list")
	public String overallStatusOfDonations(Model model, @ModelAttribute("searchParam") GiveState searchParam ){


		// 목록(페이징)
		String todays = DateUtils.getToday("yyyyMMdd");
		searchParam.setShCntrDeStart(StringUtils.defaultIfEmpty(searchParam.getShCntrDeStart(), todays));
		searchParam.setShCntrDeEnd(StringUtils.defaultIfEmpty(searchParam.getShCntrDeEnd(), todays));


		int count = giveStateService.getGiveStateDetailListCountTest(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", giveStateService.getGiveStateDetailListTest(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		// 조회조건
		String today = DateUtils.getToday(Const.DATE_FORMAT);
		model.addAttribute("today", today);
		model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
		model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
		model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
		model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));

		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("linkInsttCdList", CodeUtils.getCodeList("LINK_INSTT_CD"));	// 민간연계기관 목록
		model.addAttribute("searchParam",searchParam);

		return "view:/give/give-state/cntr_status";
	}

	/**
	 * 기부금 모금 상세현황 (확인용 TEST) - 기부 & 기부사용
	 *
	 * @param model
	 * @param searchParam
	 * @return
	 */
	@GetMapping("detail_all")
	public String viewCntrInit(Model model
			, @ModelAttribute("searchParam") GiveState searchParam ){

		// 조회조건
		String today = DateUtils.getToday(Const.DATE_FORMAT);
		model.addAttribute("today", today);
		model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
		model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
		model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
		model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));

		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("searchParam",searchParam);

		return "view:/give/give-state/form_cntr";
	}

	/**
	 * 기부금 모금 상세현황 (확인용 TEST) - 기부 & 기부사용
	 *
	 * @param model
	 * @param searchParam
	 * @return
	 */
	@GetMapping("detail_cntr")
	public String viewCntr(Model model
			, @ModelAttribute("searchParam") GiveState searchParam ){

		// 목록(페이징)
		int count = giveStateService.getGiveStateDetailListCountCntr(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		List<GiveStateTest> list = giveStateService.getGiveStateDetailListCntr(searchParam);

		model.addAttribute("list", list);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		List<GiveStateCntrUsePoint> list2All = new ArrayList<>();
		if ("Y".equals(searchParam.getShUsePointInclude())) {
			for (GiveStateTest cntrList : list) {
				GiveState searchParam2 = new GiveState();
				searchParam2.setShCntrSn(cntrList.getCntrSn());
				List<GiveStateCntrUsePoint> list2 = giveStateService.getGiveStateDetailListCntrUsePoint(searchParam2);
				for (GiveStateCntrUsePoint usePointList : list2) {
					list2All.add(usePointList);
				}
			}
		}
		model.addAttribute("list2", list2All);

		// 조회조건
		String today = DateUtils.getToday(Const.DATE_FORMAT);
		model.addAttribute("today", today);
		model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
		model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
		model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
		model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));

		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("searchParam",searchParam);

		return "view:/give/give-state/form_cntr";
	}

	/**
	 * 엑셀 다운로드 (행안부)
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping(value="list/download-excel")
	public ModelAndView downloadExcelProcessByList(GiveState giveState) {

		giveState.setConditionType("EXCEL_DOWNLOAD");
		giveState.setPagination(null);

		Pagination pagination = Pagination.getInstance(0);
		giveState.setPagination(pagination);

		boolean isLocgov = false;

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "기부금모금현황목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = giveStateService.streamGiveStateList(giveState, isLocgov);
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
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping(value="list/locgov/download-excel")
	public ModelAndView downloadExcelProcessByLocgovList(GiveState giveState) {
		HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(UserUtils.getUserId());
		String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
		giveState.setShLocgovCode(locgovCode);

		giveState.setConditionType("EXCEL_DOWNLOAD");
		giveState.setPagination(null);

		Pagination pagination = Pagination.getInstance(0);
		giveState.setPagination(pagination);

		boolean isLocgov = true;

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "기부금모금현황목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = giveStateService.streamGiveStateList(giveState, isLocgov);
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
	public ModelAndView downloadExcelProcessByDetailList(GiveState searchParam) {
		String today1 = DateUtils.getToday("yyyyMMdd");
		searchParam.setShCntrDeStart(StringUtils.defaultIfEmpty(searchParam.getShCntrDeStart(), today1));
		searchParam.setShCntrDeEnd(StringUtils.defaultIfEmpty(searchParam.getShCntrDeEnd(), today1));

		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			if (
				"ROLE_ADMIN_1".equals(userRole.getAuthority()) ||
				"ROLE_ADMIN_2".equals(userRole.getAuthority()) ||
				"ROLE_ADMIN_3".equals(userRole.getAuthority()) ||
				"ROLE_ADMIN_4".equals(userRole.getAuthority())
			) {
				role = "mois";
			} else if (
				"ROLE_ADMIN_5".equals(userRole.getAuthority()) ||
				"ROLE_ADMIN_6".equals(userRole.getAuthority())
			) {
				// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			if (!locgovCode.equals(searchParam.getShLocgovCode())) {
				return new ModelAndView("redirect:/");
			}
		}

		// 목록(페이징)
		int totalCount = giveStateService.getGiveStateDetailListCount(searchParam);

		Pagination pagination = Pagination.getInstance(totalCount, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "기부금모금상세현황_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = giveStateService.streamGiveStateDetailData(searchParam, totalCount);
		} finally {
			// ItemReviewExcelView에 workbook과 암호화에 사용할 비밀번호를 담아서 전송
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 기부금 전체현황 엑셀 다운로드
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping(value="detail_list/download-excel")
	public ModelAndView downloadExcelProcessByDetailListAll(GiveState searchParam) {
		searchParam.setConditionType("EXCEL_DOWNLOAD");

		// 목록(페이징)
		int totalCount = giveStateService.getGiveStateDetailListCountTest(searchParam);

		Pagination pagination = Pagination.getInstance(totalCount, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "기부금전체현황_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = giveStateService.streamGiveStateDetailListTest(searchParam, totalCount);
		} finally {
			// ItemReviewExcelView에 workbook과 암호화에 사용할 비밀번호를 담아서 전송
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 2지자체 조회(콤보)
	 *
	 * @param code
	 * @param model
	 * @return
	 */
	@PostMapping("options-by-locgovCode")
	public @ResponseBody List<HashMap<String, Object>> optionsByGroupId(@RequestParam(name="code", defaultValue = "0") String code, Model model) {
		return giveStateService.getLocgovCodeList(code);
	}

	/**
	 * 기부금 전체현황 > 기부내역변경 (팝업)
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/popup/giveModifyInfo/{elctrnPayNo}")
	@RequestProperty(title = "기부내역변경", layout = "base")
	public String giveModifyInfo(@PathVariable("elctrnPayNo") String elctrnPayNo , Model model) {
		model.addAttribute("infoPop", giveStateService.getGiveStateModifyInfo(elctrnPayNo));
		model.addAttribute("userKey",UserUtils.getUserId());
		return ViewUtils.getView("/give/popup/give-modify-popup");
	}

	/* 기부내역변경통합(과오납/포인트/수납확인Y/수납취소) */
	@PostMapping("/popup/giveModifyInfo/modifyGive")
	public JsonView modifyGive(@ModelAttribute("searchParam") GiveStateTest searchParam) {
		HashMap<String, Object> reqmngApproveMap = new HashMap<>();
		try {
			long reqId = sequenceService.getId("G_CNTR_REQMNG");
			searchParam.setReqId(reqId);
			searchParam.setFrstRegisterId(String.valueOf(UserUtils.getUserId()));
			giveStateService.giveReqmngInsert(searchParam);
			searchParam.setLastUpdusrId(String.valueOf(UserUtils.getUserId()));
			reqmngApproveMap = giveStateService.giveReqmngApprove(searchParam);
		} catch (RuntimeException e) {
			log.error("■■■ERROR■■■ modifyGive Exception {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure("기부내역변경 중 오류 발생");
		} catch(Exception e) {
			return JsonViewUtils.failure("기부내역변경 중 오류 발생");
		}
		return  JsonViewUtils.success(reqmngApproveMap);
	}

	@PostMapping("/popup/giveModifyInfo/sendNts")
	public JsonView sendNts(@RequestParam(name="enapbuNo", defaultValue = "") String enapbuNo) {
		String accessToken = "";
		try {
			accessToken = ngDonationRelayService.getAccessToken();
			ngDonationRelayService.sendNtsEreceipt(accessToken, enapbuNo);
		} catch (RuntimeException e) {
			log.error("■■■ERROR■■■ sendNts Exception {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure("영수증 처리 실패");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return JsonViewUtils.failure("영수증 처리 실패");
		}
		return JsonViewUtils.success();

	}

	@SuppressWarnings("unchecked")
	@PostMapping("/popup/giveModifyInfo/confirmSunap")
	public JsonView confirmSunap(@ModelAttribute("Param") GiveStateTest Param) {
		Map<String, Object> resultMap = new HashMap<>();

		try {
			if(Param.getSeoulTrgetAt().equals("Y")) {

				SeoulParam seoulParam = new SeoulParam();
				seoulParam.setEnapbuNo(Param.getEnapbuNo());
				seoulParam.setJijacheCd(Param.getJijacheCd());
				seoulParam.setSystemCd("02");
				resultMap = ngDonationRelayService.etaxSunapInfo(seoulParam);

				String api_response = StringUtils.defaultIfEmpty((String) resultMap.get("api_response"), "");

				if(api_response.equals("")) {
					return JsonViewUtils.failure("수납확인에 실패하였습니다.");
				} else {
					Object objList = JSONValue.parse(api_response);
			        JSONObject jsonObject = (JSONObject)objList;
			        List<Map<String, Object>> arrResult = (List<Map<String, Object>>) jsonObject.get("ARR_RESULT");
			        String sunapYn = StringUtils.defaultIfEmpty((String) arrResult.get(0).get("SUNAP_YN"), "");

			        if("100".equals(StringUtils.defaultIfEmpty((String) jsonObject.get("RST_CD"), "")) && sunapYn.equals("Y")) {
			        	return JsonViewUtils.success();
			        } else {
						return JsonViewUtils.failure("세외수입이 없는 수납자료 입니다");
					}
				}
			} else {
				NextBugaRequestDto nextBugaRequestDto = new NextBugaRequestDto();
				nextBugaRequestDto.setEpayNo(Param.getEnapbuNo());
				nextBugaRequestDto.setLinkMngKey(Param.getCntrSn());
				nextBugaRequestDto.setLocgovCode(Param.getJijacheCd());

				resultMap = ngDonationRelayService.localSunapConfirm(nextBugaRequestDto);

				String status = StringUtils.defaultIfBlank((String)resultMap.get("status"), "");

				if("FAIL".equals(status)) {
					return JsonViewUtils.failure("세외수입이 없는 수납자료 입니다.");
				} else if("ERROR".equals(status)) {

					String errMsg = StringUtils.defaultIfBlank((String)resultMap.get("errorMsg"), "");
					return JsonViewUtils.failure("수납결과 확인 중 오류가 발생했습니다.\n"+errMsg);
				}
			}
		} catch (RuntimeException e) {
			log.error("■■■ERROR■■■ confirmSunap Exception {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure("수납확인에 실패하였습니다.");
		} catch (Exception e) {
			log.error("■■■ERROR■■■ confirmSunap Exception {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure("수납확인에 실패하였습니다.");
		}
		return JsonViewUtils.success();

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/reConfirmSunap")
	public @ResponseBody Map<String, Object> reConfirmSunap(@RequestBody HashMap<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> resList = new ArrayList<>();
		try {
			List<HashMap<String, Object>>  deleteList = ngDonationService.selectDeleteCntrReSunapList(params);	//기부상태 : 신고 건

			if(deleteList.isEmpty()) {
				resultMap.put("msg", "재수납 데이터가 존재하지 않습니다.");
				return resultMap;
			}

			for(HashMap<String, Object> data : deleteList) {
				if(data.get("seoul_trget_at").toString().equals("Y")) {
					SeoulParam seoulParam = new SeoulParam();
					seoulParam.setEnapbuNo(data.get("elctrn_pay_no").toString());
					seoulParam.setJijacheCd(data.get("cntr_locgov_code").toString());
					seoulParam.setSystemCd("02");
					Map<String, Object> sunapResultMap = ngDonationRelayService.etaxSunapInfo(seoulParam);

					String api_response = StringUtils.defaultIfEmpty((String) sunapResultMap.get("api_response"), "");
					Object objList = JSONValue.parse(api_response);
			        JSONObject jsonObject = (JSONObject)objList;

					if(!api_response.equals("")) {
						List<Map<String, Object>> arrResult = (List<Map<String, Object>>) jsonObject.get("ARR_RESULT");
				        String sunapYn = StringUtils.defaultIfEmpty((String) arrResult.get(0).get("SUNAP_YN"), "");

						if("100".equals(StringUtils.defaultIfEmpty((String) jsonObject.get("RST_CD"), "")) && sunapYn.equals("Y")) {
							seoulParam.setUserId(String.valueOf(data.get("user_id")));
				            seoulParam.setSttemntPayDe(data.get("cntr_de").toString());
				            ngDonationService.sunapSuccessNoSms(seoulParam);
				            resList.add(sunapResultMap);
				        }
					}
				} else {
					ContryParam contryParam = new ContryParam();
					contryParam.setEnapbuNo(data.get("elctrn_pay_no").toString());
					contryParam.setJijacheCd(data.get("cntr_locgov_code").toString());
					contryParam.setMngNo(StringUtils.defaultIfEmpty((String) data.get("book_no"), ""));
					contryParam.setSystemCd("03");
					Map<String, Object> sunapResultMap = ngDonationRelayService.contrySunapInfo(contryParam);

					String api_response = StringUtils.defaultIfEmpty((String) sunapResultMap.get("api_response"), "");
					Object objList = JSONValue.parse(api_response);
			        JSONObject jsonObject = (JSONObject)objList;

			        if(!api_response.equals("")) {
						if(StringUtils.defaultIfEmpty((String) jsonObject.get("result_code"), "").equals("100")) {
							SeoulParam seoulParam = new SeoulParam();
							seoulParam.setUserId(String.valueOf(data.get("user_id")));
				            seoulParam.setSttemntPayDe(data.get("cntr_de").toString());
				            seoulParam.setEnapbuNo(data.get("elctrn_pay_no").toString());
				            ngDonationService.sunapSuccessNoSms(seoulParam);
				            resList.add(sunapResultMap);
						}
					}
				}
				TimeUnit.SECONDS.sleep(1);
			}
		} catch (RuntimeException e) {
			log.error("■■■ERROR■■■ reConfirmSunap Exception {}",e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("■■■ERROR■■■ reConfirmSunap Exception {}",e.getStackTrace()[0]);
			// TODO Auto-generated catch block
		}
		resultMap.put("resultList", resList);
		resultMap.put("msg", "("+resList.size()+") 건에 대해 수납확인을 성공하였습니다.");
		return resultMap;
	}

	/**
	 * 기부금 변경신청 관리
	 *
	 * @param model
	 * @param searchParam
	 * @return
	 */
	@GetMapping("reqmng-list")
	public String giveReqMng(Model model, @ModelAttribute("searchParam") GiveState searchParam ){

		String role = "";
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
		if("mois".equals(role)) { //행안부,시스템관리자

			// 조회조건
			String today = DateUtils.getToday(Const.DATE_FORMAT);
			model.addAttribute("today", today);
			model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
			model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
			model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
			model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));

			//날짜 초기 세팅 (당일)
			searchParam.setShFrstRegistPnttmStart(StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmStart(), today));
			searchParam.setShFrstRegistPnttmEnd(StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmEnd(), today));

			// 목록(페이징)
			int count = 0;

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", Collections.EMPTY_LIST);
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			model.addAttribute("wdr", CodeUtils.getCodeList("WDR")); // 지자체코드
			model.addAttribute("reqmngCodeList", CodeUtils.getCodeList("CNTR_REQMNG"));	 //요청분류코드
			model.addAttribute("reqStatusCodeList", CodeUtils.getCodeList("REQ_STATUS"));  //승인여부코드
			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-reqmng/list";

	}else if("locgov".equals(role)){ //지자체 관리자

		HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
		String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
		searchParam.setShLocgovCode(locgovCode);

		// 조회조건
		String today = DateUtils.getToday(Const.DATE_FORMAT);
		model.addAttribute("today", today);
		model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
		model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
		model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
		model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));

		searchParam.setShFrstRegistPnttmStart(StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmStart(), today));
		searchParam.setShFrstRegistPnttmEnd(StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmEnd(), today));

		// 목록(페이징)
		int count = giveStateService.getGiveReqmngCount(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", giveStateService.getGiveReqmngList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		// 지자체명
		Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCode);
		model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());

		model.addAttribute("reqmngCodeList", CodeUtils.getCodeList("CNTR_REQMNG"));
		model.addAttribute("reqStatusCodeList", CodeUtils.getCodeList("REQ_STATUS"));  //승인여부코드
		model.addAttribute("searchParam",searchParam);

		return "view:/give/give-reqmng/list_locgov";

	}else {
		return "redirect:/";
	}

}

	/**
	 * 기부금 변경신청 등록페이지 이동
	 * @param
	 * @return
	 */
	@GetMapping("/req_form/{elctrnPayNo}")
	public String giveReqForm(@PathVariable("elctrnPayNo") String elctrnPayNo , Model model) {

		model.addAttribute("reqmngCodeList", CodeUtils.getCodeList("CNTR_REQMNG"));
		model.addAttribute("reqinfo", giveStateService.getGiveStateModifyInfo(elctrnPayNo));
		return "view:/give/give-reqmng/req_form";
	}

	/**
	 * 기부금 변경신청 등록
	 * @param
	 * @return
	 */

	@PostMapping("/reqReg")
	public JsonView giveReqmngInsert(@ModelAttribute("searchParam") GiveStateTest searchParam) {
		HashMap<String,Object> resultMap = new HashMap<>();
		try {
			long reqId = sequenceService.getId("G_CNTR_REQMNG");
			searchParam.setReqId(reqId);
			searchParam.setFrstRegisterId(String.valueOf(UserUtils.getUserId()));

			// G_CNTR_USE_POINT에서 해당 기부건에 대한 포인트 사용내역 조회
			int pointChk = giveStateService.getCntrUsePointCheck(searchParam.getCntrSn());
			if(pointChk > 0) return JsonViewUtils.failure("기부금 사용이력이 존재해 변경 신청을 할 수 없습니다.");

			resultMap = giveStateService.giveReqmngInsert(searchParam);
			if(resultMap.get("result_code").equals("FAIL")) {
				return JsonViewUtils.failure(StringUtils.defaultIfEmpty((String)resultMap.get("result_msg"), "결과코드가 없습니다."));
			}

			searchParam.setLastUpdusrId(String.valueOf(UserUtils.getUserId()));
			resultMap = giveStateService.giveReqmngApprove(searchParam);

			if(resultMap.get("result_code").equals("FAIL")) {
				return JsonViewUtils.failure(StringUtils.defaultIfEmpty((String)resultMap.get("result_msg"), "결과코드가 없습니다."));
			}

			} catch (RuntimeException e) {
				log.error("■■■ERROR■■■ modifyGive Exception {}",e.getStackTrace()[0]);
				return JsonViewUtils.failure("기부내역변경 중 오류 발생");
			} catch(Exception e) {
				return JsonViewUtils.failure("기부내역변경 중 오류 발생");
			}

			return  JsonViewUtils.success();
			//return  JsonViewUtils.success(resultMap);
	}

	/**
	 * 기부금 변경신청 승인
	 * @param
	 * @return
	 */
	@PostMapping("/approveReq")
	public JsonView giveReqmngUpdate(@ModelAttribute("searchParam") GiveStateTest searchParam) {
		searchParam.setLastUpdusrId(String.valueOf(UserUtils.getUserId()));

		int pointChk = giveStateService.getCntrUsePointCheck(searchParam.getCntrSn());
		if(pointChk > 0) return JsonViewUtils.failure("기부금 사용이력이 존재해 승인을 할 수 없습니다.");

		HashMap<String, Object> resultMap =  giveStateService.giveReqmngApprove(searchParam);
		if(resultMap.get("result_code").equals("FAIL")) {
			return JsonViewUtils.failure(StringUtils.defaultIfEmpty((String) resultMap.get("result_msg"), "결과코드가 없습니다.") );
		}
		return JsonViewUtils.success();
	}

	/**
	 * 기부금 변경신청 취소
	 * @param
	 * @return
	 */
	@PostMapping("/cancelReq")
	public JsonView giveReqmngCancel(@ModelAttribute("searchParam") GiveStateTest searchParam) {
		searchParam.setLastUpdusrId(String.valueOf(UserUtils.getUserId()));
		HashMap<String,Object> resultMap = giveStateService.giveReqmngCancel(searchParam);
		if(resultMap.get("result_code").equals("FAIL")) {
			return JsonViewUtils.failure(StringUtils.defaultIfEmpty((String) resultMap.get("result_msg"), "결과코드가 없습니다.") );
		}
		return JsonViewUtils.success();
	}

	/**
	 * 기부금 영수증 이력
	 * @param elctrnPayNo
	 * @return List<GiveStateNts>
	 */
	@GetMapping("/popup/ntsList/{elctrnPayNo}")
	@RequestProperty(title = "기부금 영수증 이력", layout = "base")
	public String getNtsList(@PathVariable("elctrnPayNo") String elctrnPayNo, Model model) {
		model.addAttribute("list", giveStateService.getNtsList(elctrnPayNo));
		return "view:/give/popup/ntsList";
	}

	/**
	 * 기부금 변경신청 관리
	 *
	 * @param model
	 * @param searchParam
	 * @return
	 */
	@PostMapping("reqmng-list")
	public String searchGiveReqMng(Model model, @ModelAttribute("searchParam") GiveState searchParam ){
		String role = "";
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
		if("mois".equals(role)) { //행안부,시스템관리자

			// 조회조건
				String today = DateUtils.getToday(Const.DATE_FORMAT);
				model.addAttribute("today", today);
				model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
				model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
				model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
				model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));

				//날짜 초기 세팅 (당일)
				searchParam.setShFrstRegistPnttmStart(StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmStart(), today));
				searchParam.setShFrstRegistPnttmEnd(StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmEnd(), today));

				// 목록(페이징)
				int count = giveStateService.getGiveReqmngCount(searchParam);

				Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
				searchParam.setPagination(pagination);

				model.addAttribute("list", giveStateService.getGiveReqmngList(searchParam));
				model.addAttribute("pagination", pagination);
				model.addAttribute("count", count);

				model.addAttribute("wdr", CodeUtils.getCodeList("WDR")); // 지자체코드
				model.addAttribute("reqmngCodeList", CodeUtils.getCodeList("CNTR_REQMNG"));	 //요청분류코드
				model.addAttribute("reqStatusCodeList", CodeUtils.getCodeList("REQ_STATUS"));  //승인여부코드
				model.addAttribute("searchParam",searchParam);

				return "view:/give/give-reqmng/list";

		}else if("locgov".equals(role)){ //지자체 관리자

			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			searchParam.setShLocgovCode(locgovCode);

			// 조회조건
			String today = DateUtils.getToday(Const.DATE_FORMAT);
			model.addAttribute("today", today);
			model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
			model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
			model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
			model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));

			searchParam.setShFrstRegistPnttmStart(StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmStart(), today));
			searchParam.setShFrstRegistPnttmEnd(StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmEnd(), today));

			// 목록(페이징)
			int count = giveStateService.getGiveReqmngCount(searchParam);

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", giveStateService.getGiveReqmngList(searchParam));
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			// 지자체명
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCode);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());

			model.addAttribute("reqmngCodeList", CodeUtils.getCodeList("CNTR_REQMNG"));
			model.addAttribute("reqStatusCodeList", CodeUtils.getCodeList("REQ_STATUS"));  //승인여부코드
			model.addAttribute("searchParam",searchParam);

			return "view:/give/give-reqmng/list_locgov";

		}else {
			return "redirect:/";
		}
	}


}