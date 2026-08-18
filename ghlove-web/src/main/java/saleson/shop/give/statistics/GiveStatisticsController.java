package saleson.shop.give.statistics;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.shop.give.giveoperation.GiveOperationService;
import saleson.shop.give.giveoperation.domain.LocManagerCheck;
import saleson.shop.give.statistics.domain.GiveOperateSearch;
import saleson.shop.give.statistics.domain.GiveStatistics;
import saleson.shop.give.statistics.domain.GiveStatisticsDetailSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsPersonal;
import saleson.shop.give.statistics.domain.GiveStatisticsSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsTotalSearch;
import saleson.shop.give.statistics.support.GiveStatisticsDetailExcelView;
import saleson.shop.give.statistics.support.GiveStatisticsExcelView;
import saleson.shop.give.statistics.support.GiveStatisticsExcelViewByDate;
import saleson.shop.give.statistics.support.GiveStatisticsOperateExcelView;

@Controller
@RequestMapping("/opmanager/give/statistics/**")
@RequestProperty(title="기부 통계", layout="default", template="opmanager")
public class GiveStatisticsController {

	private static final Logger log = LoggerFactory.getLogger(GiveStatisticsController.class);

	@Autowired
	private GiveStatisticsService giveStatisticsService;

	@Autowired
	private GiveOperationService giveOperationService;



	/**
	 * 통계 > 기부금 모금현황 > 전체
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@GetMapping("all")
	public String statisticsGiveAll(Model model, @ModelAttribute("searchParam") GiveStatisticsTotalSearch searchParam) {

		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("searchParam", searchParam);
		return "view:/give/statistics/all/detail";
	}

	@PostMapping("all")
	public String searchStatisticsGiveAll(Model model, @ModelAttribute("searchParam") GiveStatisticsTotalSearch searchParam) {

		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("searchParam", searchParam);
		model.addAttribute("total", giveStatisticsService.getGiveStatisticsAllCount(searchParam));
		return "view:/give/statistics/all/detail";
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체(통계데이터 ajax)
	 * @param searchParam : 검색조건
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 월별 기부건수, 인원, 금액, 온라인,오프라인 기부 건수
	 */
	@PostMapping("all/{shCntrYear}/month")
	public JsonView statisticsGiveAllTotalByMonth(GiveStatisticsTotalSearch searchParam, @PathVariable String shCntrYear) {
		searchParam.setShCntrYear(shCntrYear);
		return JsonViewUtils.success(giveStatisticsService.statisticsGiveAllTotalByMonth(searchParam));
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체(통계데이터 ajax)
	 * @param searchParam : 검색조건
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 나이대별 기부건수, 금액
	 */
	@PostMapping("all/{shCntrYear}/age")
	public JsonView statisticsGiveAllTotalByAge(GiveStatisticsTotalSearch searchParam, @PathVariable String shCntrYear) {
		searchParam.setShCntrYear(shCntrYear);
		return JsonViewUtils.success(giveStatisticsService.statisticsGiveAllTotalByAge(searchParam));
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체 > 시간대별 기부금액
	 * @param searchParam : 검색조건
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 나이대별 기부건수, 금액
	 */
	@PostMapping("all/{shCntrDate}/hour")
	public JsonView statisticsGiveAllTotalByHour(GiveStatisticsTotalSearch searchParam, @PathVariable String shCntrDate) {
		searchParam.setShCntrDate(shCntrDate);
		return JsonViewUtils.success(giveStatisticsService.statisticsGiveAllTotalByHour(searchParam));
	}

	/**
	 * 통계 > 기부금 모금현황 > 지자체별
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@GetMapping("locgov")
	public String statisticsLocgov(Model model, @ModelAttribute("searchParam") GiveStatisticsSearch searchParam) {
		String url = this.getStatisticsListByInit(model, searchParam, "locgov");

		if ("SUCC".equals(url)) {
			return "view:/give/statistics/locgov/list";
		} else {
			return url;
		}
	}

	/**
	 * 통계 > 기부금 모금현황 > 지자체별 > 상세화면
	 * @param model
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체 코드
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@GetMapping("locgov/{locgovCode}")
	public String statisticsLocgovDetail(Model model, @ModelAttribute("searchParam") GiveStatisticsTotalSearch searchParam, @PathVariable String locgovCode) {
		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/statistics/locgov/{locgovCode}");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		searchParam.setShLocgovCode(locgovCode);

		CodeInfo locgovInfo = CodeUtils.getCodeInfo("LOCGOV_CODE", locgovCode);
		CodeInfo wdr = CodeUtils.getCodeInfo("WDR", locgovCode.substring(0, 2) + "000");

		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("searchParam", searchParam);
		model.addAttribute("adminRole", ac.getAdminRole());
		model.addAttribute("locgovNm", wdr.getLabel() + " " + locgovInfo.getLabel());

		return "view:/give/statistics/locgov/detail";
	}

	@PostMapping("locgov/{locgovCode}")
	public String searchStatisticsLocgovDetail(Model model, @ModelAttribute("searchParam") GiveStatisticsTotalSearch searchParam, @PathVariable String locgovCode) {
		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/statistics/locgov/{locgovCode}");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		searchParam.setShLocgovCode(locgovCode);

		CodeInfo locgovInfo = CodeUtils.getCodeInfo("LOCGOV_CODE", locgovCode);
		CodeInfo wdr = CodeUtils.getCodeInfo("WDR", locgovCode.substring(0, 2) + "000");

		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("searchParam", searchParam);
		model.addAttribute("total", giveStatisticsService.getGiveStatisticsAllCount(searchParam));
		model.addAttribute("adminRole", ac.getAdminRole());
		model.addAttribute("locgovNm", wdr.getLabel() + " " + locgovInfo.getLabel());

		return "view:/give/statistics/locgov/detail";
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체(통계데이터 ajax)
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체코드
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 월별 기부건수, 인원, 금액, 온라인,오프라인 기부 건수
	 */
	@PostMapping("locgov/{locgovCode}/{shCntrYear}/month")
	public JsonView statisticsGiveLocgovTotalByMonth(GiveStatisticsTotalSearch searchParam, @PathVariable String locgovCode, @PathVariable String shCntrYear) {
		searchParam.setShCntrYear(shCntrYear);
		searchParam.setShLocgovCode(locgovCode);
		return JsonViewUtils.success(giveStatisticsService.statisticsGiveAllTotalByMonth(searchParam));
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체(통계데이터 ajax)
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체코드
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 나이대별 기부건수, 금액
	 */
	@PostMapping("locgov/{locgovCode}/{shCntrYear}/age")
	public JsonView statisticsGiveAllTotalByAge(GiveStatisticsTotalSearch searchParam, @PathVariable String locgovCode, @PathVariable String shCntrYear) {
		searchParam.setShCntrYear(shCntrYear);
		searchParam.setShLocgovCode(locgovCode);
		return JsonViewUtils.success(giveStatisticsService.statisticsGiveAllTotalByAge(searchParam));
	}

	/**
	 * 통계 > 기부금 모금현황 > 기부인원 통계
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@GetMapping({"person", "amount","number","personal","date"})
	public String statisticsPerson(Model model, @ModelAttribute("searchParam") GiveStatisticsSearch searchParam, HttpServletRequest req) {
		String uri = req.getRequestURI();
		String filePath = uri.substring(uri.lastIndexOf("/") + 1);
		String url = this.getStatisticsList(model, searchParam, filePath);

		if ("SUCC".equals(url)) {
			return "view:/give/statistics/" + filePath + "/list";
		} else {
			return url;
		}

	}

	@PostMapping({"person", "amount","number","personal","date"})
	public String searchStatisticsPerson(Model model, @ModelAttribute("searchParam") GiveStatisticsSearch searchParam, HttpServletRequest req) {
		String uri = req.getRequestURI();
		String filePath = uri.substring(uri.lastIndexOf("/") + 1);

		String url = this.getStatisticsList(model, searchParam, filePath);

		if ("SUCC".equals(url)) {
			return "view:/give/statistics/" + filePath + "/list";
		} else {
			return url;
		}

	}

	/**
	 * 통계 > 기부금 모금현황 > 기부인원 통계 > 상세 페이지
	 * @param model
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체 코드
	 * @return list : 월별 기부인원
	 * @return searchParam : 검색조건
	 * @return yyyy : 코드에 등록된 년도
	 */
	@GetMapping({"person/{locgovCode}", "amount/{locgovCode}", "number/{locgovCode}"})
	public String statisticsPersonDetail(Model model, GiveStatisticsDetailSearch searchParam, @PathVariable String locgovCode, HttpServletRequest req) {

		String uri = req.getRequestURI().replace("/" + locgovCode, "");
		String filePath = uri.substring(uri.lastIndexOf("/") + 1);

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/statistics/" + filePath + "/{locgovCode}");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		searchParam.setShLocgovCode(locgovCode);


		if (searchParam.getFromYear() == null || "".equals(searchParam.getFromYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setFromYear(String.valueOf(now.getYear()));
			searchParam.setToYear(String.valueOf(now.getYear()));
		}

		List<GiveStatistics> list = new ArrayList<GiveStatistics>();

		if ("person".equals(filePath)) {
			list = giveStatisticsService.getGivePersonStatisticsDetail(searchParam);
		} else if ("amount".equals(filePath)) {
			list = giveStatisticsService.getGiveAmountStatisticsDetail(searchParam);
		} else if ("number".equals(filePath)) {
			list = giveStatisticsService.getGiveNumberStatisticsDetail(searchParam);
		}


		//int [] arr = {100,200,300,500,1000,5000,10000,50000,1000000};
		//list.stream().forEach(s -> s.setPercent(this.getStatisticsPercent(arr, Integer.parseInt(s.getGivePersons()) )));

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("list", list);
		model.addAttribute("searchParam", searchParam);
		model.addAttribute("adminRole", ac.getAdminRole());


		return "view:/give/statistics/" + filePath + "/detail";
	}


	/**
	 * 통계 > 기부금 모금현황 > 기부인원 통계 > 상세 페이지 > 일별 기부인원 통계
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체 코드
	 * @return list : 일별 기부인원
	 */
	@PostMapping({"person/{locgovCode}", "amount/{locgovCode}", "number/{locgovCode}"})
	public JsonView statisticsPersonDetailByDate(GiveStatisticsDetailSearch searchParam, @PathVariable String locgovCode, HttpServletRequest req) {
		String uri = req.getRequestURI().replace("/" + locgovCode, "");
		String filePath = uri.substring(uri.lastIndexOf("/") + 1);
		searchParam.setShLocgovCode(locgovCode);

		if ("person".equals(filePath)) {
			return JsonViewUtils.success(giveStatisticsService.getGivePersonStatisticsDetailByDate(searchParam));
		} else if ("amount".equals(filePath)) {
			return JsonViewUtils.success(giveStatisticsService.getGiveAmountStatisticsDetailByDate(searchParam));
		} else if ("number".equals(filePath)) {
			return JsonViewUtils.success(giveStatisticsService.getGiveNumberStatisticsDetailByDate(searchParam));
		}


		return JsonViewUtils.failure("조회에 실패하였습니다.");
	}

	/**
	 * 통계 > 기부금 모금현황 > 기부인원 통계 > 엑셀
	 * @param searchParam : 검색조건
	 * @return
	 */
	@GetMapping({"person/excel", "amount/excel", "number/excel","date/excel","personal/excel","locgov/excel"})
	public ModelAndView downloadExcelProcessByPersonList(GiveStatisticsSearch searchParam, HttpServletRequest req) {
		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);
		if (ac.getIsLoc() ) searchParam.setShLocgovCode(ac.getLocgovCode());

		String uri = req.getRequestURI().replace("/excel", "");
		String filePath = uri.substring(uri.lastIndexOf("/") + 1);

		ModelAndView mav = new ModelAndView(new GiveStatisticsExcelView());

		searchParam.setConditionType("EXCEL_DOWNLOAD");
		searchParam.setPagination(null);

		mav.addObject("type", filePath);
		mav.addObject("list", giveStatisticsService.getGivePersonStatisticsList(searchParam));

		return mav;


	}

	/**
	 * 통계 > 기부금 모금현황 > 기부인원, 금액, 건수 상세 페이지 > 엑셀
	 * @param searchParam : 검색조건
	 * @param type : (person : 기부인원, amount : 금액, number : 건수)
	 * @param locgovCode : 지자체 코드
	 * @return
	 */
	@GetMapping({"person/{locgovCode}/excel","amount/{locgovCode}/excel","number/{locgovCode}/excel"})
	public ModelAndView downloadExcelProcessByPersonDetail(GiveStatisticsDetailSearch searchParam, @PathVariable String locgovCode, HttpServletRequest req) {
		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);
		if (ac.getIsLoc() ) searchParam.setShLocgovCode(ac.getLocgovCode());

		ModelAndView mav = new ModelAndView(new GiveStatisticsDetailExcelView());

		String uri = req.getRequestURI().replace("/" + locgovCode + "/excel", "");
		String type = uri.substring(uri.lastIndexOf("/") + 1);

		mav.addObject("searchParam", searchParam);
		mav.addObject("type", type);
		mav.addObject("list", giveStatisticsService.getGivePersonStatisticsDetailExcel(searchParam));

		return mav;
	}


	/**
	 * 통계 > 기부금 모금현황 > 인원별통계 > 상세 페이지
	 * @param model
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체코드
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@GetMapping("personal/{locgovCode}")
	public String statisticsPersonalDetail(Model model, @ModelAttribute("searchParam") GiveStatisticsDetailSearch searchParam, @PathVariable String locgovCode) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/statistics/personal/{locgovCode}");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		searchParam.setShLocgovCode(locgovCode);
		model.addAttribute("searchParam", searchParam);

		if (searchParam.getFromYear() == null || "".equals(searchParam.getFromYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setFromYear(String.valueOf(now.getYear()));
			searchParam.setToYear(String.valueOf(now.getYear()));
		}

		GiveStatisticsPersonal total = giveStatisticsService.getGivePersonalStatisticsDetailTotal(searchParam);

		List<GiveStatisticsPersonal> list = giveStatisticsService.getGivePersonalStatisticsDetail(searchParam);

		list.stream().forEach(s -> s.setUserName(this.userNameMasking(s.getUserName())));

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("total", total);
		model.addAttribute("list", list);
		model.addAttribute("adminRole", ac.getAdminRole());



		return "view:/give/statistics/personal/detail";
	}


	/**
	 * 통계 > 기부금 모금현황 > 인원별통계 > 상세페이지 > 팝업
	 * @param model
	 * @param locgovCode : 지자체 코드
	 * @param userId : 유저ID
	 * @return list : 해당 인원의 기부 상세 리스트
	 */
	@GetMapping("personal/{locgovCode}/popup/{userId}")
	@RequestProperty(layout = "base")
	public String statisticsPersonalDetailPopup(Model model, @PathVariable String locgovCode, @PathVariable String userId) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/statistics/personal/{locgovCode}/popup/" + userId);
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		GiveStatisticsDetailSearch searchParam = new GiveStatisticsDetailSearch();
		searchParam.setUserId(userId);
		searchParam.setShLocgovCode(locgovCode);

		model.addAttribute("list", giveStatisticsService.getGivePersonalStatisticsDetailByUser(searchParam));


		return "view:/give/statistics/personal/popup/detail";
	}


	/**
	 * 통계 > 기부금 모금현황 > 일자별통계 > 상세페이지
	 * @param model
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체 코드
	 * @return list : 일자별 통계 리스트
	 * @return searchParam : 검색조건
	 * @return total : 검색된 일자별 리스트들의 기부건수, 금액, 인원, 포인트의 총합계
	 */
	@GetMapping("date/{locgovCode}")
	public String statisticsDateDetail(Model model, GiveStatisticsDetailSearch searchParam, @PathVariable String locgovCode) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/statistics/date/{locgovCode}");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		model.addAttribute("adminRole", ac.getAdminRole());

		return "view:/give/statistics/date/detail";
	}

	@PostMapping("date/{locgovCode}")
	public String searchStatisticsDateDetail(Model model, GiveStatisticsDetailSearch searchParam, @PathVariable String locgovCode) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/statistics/date/{locgovCode}");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		searchParam.setShLocgovCode(locgovCode);

		GiveStatistics total = giveStatisticsService.getGiveDateStatisticsDetailTotal(searchParam);

		model.addAttribute("searchParam", searchParam);
		model.addAttribute("list", giveStatisticsService.getGiveDateStatisticsDetail(searchParam));
		model.addAttribute("total", total);
		model.addAttribute("adminRole", ac.getAdminRole());

		return "view:/give/statistics/date/detail";
	}


	/**
	 * 통계 > 기부금 모금현황 > 일자별통계 > 상세페이지 > 엑셀
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체 코드
	 * @return
	 */
	@GetMapping("date/{locgovCode}/excel")
	public ModelAndView downloadExcelProcessByDate(GiveStatisticsDetailSearch searchParam, @PathVariable String locgovCode) {
		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, null);
		if (ac.getIsLoc() ) searchParam.setShLocgovCode(ac.getLocgovCode());

		ModelAndView mav = new ModelAndView(new GiveStatisticsExcelViewByDate());

		GiveStatistics total = giveStatisticsService.getGiveDateStatisticsDetailTotal(searchParam);

		mav.addObject("searchParam", searchParam);
		mav.addObject("list", giveStatisticsService.getGiveDateStatisticsDetail(searchParam));
		mav.addObject("total", total);

		return mav;
	}


	/**
	 * 통계 > 기부금 모금현황 > 일자별통계 > 상세페이지 > 팝업
	 * @param model
	 * @param locgovCode : 지자체 코드
	 * @param date : 일자(ex : 20230202)
	 * @return list : 해당 일자의 기부 상세 리스트
	 */
	@GetMapping("date/{locgovCode}/popup/{date}")
	@RequestProperty(layout = "base")
	public String statisticsDateDetailPopup(Model model, @PathVariable String locgovCode, @PathVariable String date) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/statistics/date/{locgovCode}/popup/" + date);
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		GiveStatisticsDetailSearch searchParam = new GiveStatisticsDetailSearch();
		searchParam.setShCntrDeStart(date);
		searchParam.setShLocgovCode(locgovCode);

		model.addAttribute("list", giveStatisticsService.getGiveDateStatisticsDetailByDate(searchParam));

		return "view:/give/statistics/date/popup/detail";
	}

	@GetMapping("operate")
	public String statisticsOperateList(Model model, @ModelAttribute("searchParam") GiveOperateSearch searchParam) {
		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);

		if (ac.getIsLoc() && ac.isPass()) {
			return ViewUtils.redirect("/opmanager/give/statistics/operate/" + ac.getLocgovCode());
		} else if (ac.getIsLoc() && !ac.isPass()) {
			return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());
		} else {
			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		}

		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}


		//searchParam.setCodeList(CodeUtils.getCodeList("CNTR_USE"));

		int count = giveStatisticsService.getGiveOperateListCount(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", giveStatisticsService.getGiveOperateList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);


		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도
		model.addAttribute("useList", CodeUtils.getCodeList("CNTR_USE"));	// 년도
		model.addAttribute("searchParam", searchParam);

		return "view:/give/statistics/operate/list";
	}

	@GetMapping("operate/excel")
	public ModelAndView statisticsOperateExcel(GiveOperateSearch searchParam) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);
		if (ac.getIsLoc() ) searchParam.setShLocgovCode(ac.getLocgovCode());

		ModelAndView mav = new ModelAndView(new GiveStatisticsOperateExcelView());

		searchParam.setConditionType("EXCEL_DOWNLOAD");
		searchParam.setPagination(null);

		mav.addObject("codeList", CodeUtils.getCodeList("CNTR_USE"));
		mav.addObject("list", giveStatisticsService.getGiveOperateList(searchParam));

		return mav;
	}

	@GetMapping("operate/{locgovCode}")
	public String statisticsOperateDetailPage(Model model, GiveOperateSearch searchParam, @PathVariable String locgovCode) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/statistics/operate/{locgovCode}");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		searchParam.setShLocgovCode(locgovCode);
		model.addAttribute("codeList", CodeUtils.getCodeList("CNTR_USE"));
		model.addAttribute("searchParam", searchParam);
		model.addAttribute("adminRole", ac.getAdminRole());

		return "view:/give/statistics/operate/detail";
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체(통계데이터 ajax)
	 * @param
	 */
	@PostMapping("operate/{locgovCode}/detail")
	public JsonView statisticsOperateDetail(GiveOperateSearch searchParam, @PathVariable String locgovCode) {
		searchParam.setShLocgovCode(locgovCode);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("codeList", CodeUtils.getCodeList("CNTR_USE"));
		result.put("list", giveStatisticsService.getGiveOperateListByLocgov(searchParam));

		return JsonViewUtils.success(result);
	}

	private String getStatisticsList(Model model, GiveStatisticsSearch searchParam, String path) {

		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);

		if (ac.getIsLoc() && ac.isPass()) {
			if ("Y".equals(searchParam.getForeignerFlag())) {
				return ViewUtils.redirect("/opmanager/give/statistics/foreigner/" + path + "/" + ac.getLocgovCode());
			} else {
				return ViewUtils.redirect("/opmanager/give/statistics/" + path + "/" + ac.getLocgovCode());
			}
		} else if (ac.getIsLoc() && !ac.isPass()) {
			return ViewUtils.redirect("/opmanager", ac.getMessage());
		}

		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드


		int count = giveStatisticsService.getGivePersonStatisticsCount(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", giveStatisticsService.getGivePersonStatisticsList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		model.addAttribute("adminRole", ac.getAdminRole());

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도
		model.addAttribute("searchParam", searchParam);

		return "SUCC";

	}


	/**
	 * 유저 이름을 마스킹하는 매서드
	 * @param txt : 마스킹할 이름
	 * @return 마스킹된 이름 반환
	 * @return 이혁 > 이*
	 * @return 이종혁 > 이*혁
	 * @return 제갈종혁 > 제**혁
	 */
	private String userNameMasking(String txt) {
		String regExp = "(?<=.{1})(?<m>.*)(?=.$)";
		String masking = "*";

		if (txt.length() < 3) {
			return txt.substring(0,1) + masking;
		} else {
			return txt.replaceFirst(regExp, masking.repeat(txt.length() - 2));
		}
	}

	private String getStatisticsPercent(int[] sectionArr, int value ) {

		int idx = 0, max = 0, val = 0;
		String result = "";
		double per = 0;

		for (int i : sectionArr) {
			if (value <= i) {

				if (idx > 0) {
					val = value - sectionArr[idx - 1];
					max = i - sectionArr[idx - 1];
				} else {
					val = value;
					max = i;
				}

				per = (val * 1.0)/max * 100;

				double sum = idx * 10 + (per/10);
				result = String.valueOf(sum);
				break;
			}

			idx++;
		}

		return result;
	}


	/**
	 * 통계 > 외국인 기부 현황 > 전체
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@GetMapping("/foreigner/all")
	public String statisticsGiveAllForeigner(Model model, @ModelAttribute("searchParam") GiveStatisticsTotalSearch searchParam) {
		searchParam.setForeignerFlag("Y");
		return statisticsGiveAll(model, searchParam);
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체(통계데이터 ajax)
	 * @param searchParam : 검색조건
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 월별 기부건수, 인원, 금액, 온라인,오프라인 기부 건수
	 */
	@PostMapping("/foreigner/all/{shCntrYear}/month")
	public JsonView statisticsGiveAllTotalByMonthForeigner(GiveStatisticsTotalSearch searchParam, @PathVariable String shCntrYear) {
		searchParam.setForeignerFlag("Y");
		return statisticsGiveAllTotalByMonth(searchParam, shCntrYear);
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체(통계데이터 ajax)
	 * @param searchParam : 검색조건
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 나이대별 기부건수, 금액
	 */
	@PostMapping("/foreigner/all/{shCntrYear}/age")
	public JsonView statisticsGiveAllTotalByAgeForeigner(GiveStatisticsTotalSearch searchParam, @PathVariable String shCntrYear) {
		searchParam.setForeignerFlag("Y");
		return statisticsGiveAllTotalByAge(searchParam, shCntrYear, shCntrYear);
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체 > 시간대별 기부금액
	 * @param searchParam : 검색조건
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 나이대별 기부건수, 금액
	 */
	@PostMapping("/foreigner/all/{shCntrDate}/hour")
	public JsonView statisticsGiveAllTotalByHourForeigner(GiveStatisticsTotalSearch searchParam, @PathVariable String shCntrDate) {
		searchParam.setForeignerFlag("Y");
		return statisticsGiveAllTotalByHour(searchParam, shCntrDate);
	}

	/**
	 * 통계 > 외국인 기부 현황 > 지자체별
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@GetMapping("/foreigner/locgov")
	public String statisticsLocgovForeigner(Model model, @ModelAttribute("searchParam") GiveStatisticsSearch searchParam) {
		searchParam.setForeignerFlag("Y");
		return statisticsLocgov(model, searchParam);
	}

	/**
	 * 통계 > 외국인 기부 현황 > 지자체별
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@PostMapping("/foreigner/locgov")
	public String searchStatisticsLocgovForeigner(Model model, @ModelAttribute("searchParam") GiveStatisticsSearch searchParam) {
		searchParam.setForeignerFlag("Y");
		return searchStatisticsLocgov(model, searchParam);
	}

	/**
	 * 통계 > 외국인 기부 현황 > 지자체별 > 상세화면
	 * @param model
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체 코드
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@GetMapping("/foreigner/locgov/{locgovCode}")
	public String statisticsLocgovDetailForeigner(Model model, @ModelAttribute("searchParam") GiveStatisticsTotalSearch searchParam, @PathVariable String locgovCode) {
		searchParam.setForeignerFlag("Y");
		return statisticsLocgovDetail(model, searchParam, locgovCode);
	}

	/**
	 * 통계 > 외국인 기부 현황 > 지자체별 > 상세화면
	 * @param model
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체 코드
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@PostMapping("/foreigner/locgov/{locgovCode}")
	public String searchStatisticsLocgovDetailForeigner(Model model, @ModelAttribute("searchParam") GiveStatisticsTotalSearch searchParam, @PathVariable String locgovCode) {
		searchParam.setForeignerFlag("Y");
		return searchStatisticsLocgovDetail(model, searchParam, locgovCode);
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체(통계데이터 ajax)
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체코드
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 월별 기부건수, 인원, 금액, 온라인,오프라인 기부 건수
	 */
	@PostMapping("/foreigner/locgov/{locgovCode}/{shCntrYear}/month")
	public JsonView statisticsGiveLocgovTotalByMonthForeigner(GiveStatisticsTotalSearch searchParam, @PathVariable String locgovCode, @PathVariable String shCntrYear) {
		searchParam.setForeignerFlag("Y");
		return statisticsGiveLocgovTotalByMonth(searchParam, locgovCode, shCntrYear);
	}

	/**
	 * 통계 > 기부금 모금현황 > 전체(통계데이터 ajax)
	 * @param searchParam : 검색조건
	 * @param locgovCode : 지자체코드
	 * @param shCntrYear : 검색 년도
	 * @return JsonView : 나이대별 기부건수, 금액
	 */
	@PostMapping("/foreigner/locgov/{locgovCode}/{shCntrYear}/age")
	public JsonView statisticsGiveAllTotalByAgeForeigner(GiveStatisticsTotalSearch searchParam, @PathVariable String locgovCode, @PathVariable String shCntrYear) {
		searchParam.setForeignerFlag("Y");
		return statisticsGiveAllTotalByAge(searchParam, locgovCode, shCntrYear);
	}

	/**
	 * 통계 > 기부금 모금현황 > 지자체별
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@PostMapping("locgov")
	public String searchStatisticsLocgov(Model model, @ModelAttribute("searchParam") GiveStatisticsSearch searchParam) {
		String url = this.getStatisticsList(model, searchParam, "locgov");

		if ("SUCC".equals(url)) {
			return "view:/give/statistics/locgov/list";
		} else {
			return url;
		}
	}

	@PostMapping("operate")
	public String searchStatisticsOperateList(Model model, @ModelAttribute("searchParam") GiveOperateSearch searchParam) {
		return statisticsOperateList(model,searchParam);
	}

	private String getStatisticsListByInit(Model model, GiveStatisticsSearch searchParam, String path) {

		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);

		if (ac.getIsLoc() && ac.isPass()) {
			if ("Y".equals(searchParam.getForeignerFlag())) {
				return ViewUtils.redirect("/opmanager/give/statistics/foreigner/" + path + "/" + ac.getLocgovCode());
			} else {
				return ViewUtils.redirect("/opmanager/give/statistics/" + path + "/" + ac.getLocgovCode());
			}
		} else if (ac.getIsLoc() && !ac.isPass()) {
			return ViewUtils.redirect("/opmanager", ac.getMessage());
		}

		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드


		int count = 0;

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		model.addAttribute("adminRole", ac.getAdminRole());

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도
		model.addAttribute("searchParam", searchParam);

		return "SUCC";

	}

}
