package saleson.shop.statistics.locgov;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.common.utils.UserUtils;
import saleson.shop.code.domain.Code;
import saleson.shop.give.giveoperation.GiveOperationService;
import saleson.shop.give.giveoperation.domain.LocManagerCheck;
import saleson.shop.give.statistics.support.GiveStatisticsExcelView;
import saleson.shop.statistics.locgov.domain.StatisticsLocgovSearch;
import saleson.shop.statistics.locgov.support.StatisticsLocgovExcelView;
import saleson.shop.user.LocgovService;

@Controller
@RequestMapping("/opmanager/statistics/locgov/**")
@RequestProperty(title="관심 지자체", layout="default", template="opmanager")
public class StatisticsLocgovManagerController {

	private static final Logger log = LoggerFactory.getLogger(StatisticsLocgovManagerController.class);

	@Autowired
	private StatisticsLocgovService statisticsLocgovService;

	@Autowired
	private GiveOperationService giveOperationService;


	/**
	 * 통계 > 관심 지자체 > 관심 지자체
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 지자체 리스트
	 * @return searchParam : 검색조건
	 * @return yyyy 코드에 등록된 년도
	 * @return wdr 코드에 등록된 지자체(시,도)
	 */
	@GetMapping("like")
	public String statisticsLocgovLike(Model model, @ModelAttribute("searchParam") StatisticsLocgovSearch searchParam) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);

		if (ac.getIsLoc() && ac.isPass()) {
			return ViewUtils.redirect("/opmanager/statistics/locgov/like/" + ac.getLocgovCode());
		} else if (ac.getIsLoc() && !ac.isPass()) {
			return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());
		} else {
			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		}


		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		int count = 0;


		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		model.addAttribute("searchParam", searchParam);

		return "view:/statistics/locgov/list";
	}

	@PostMapping("like")
	public String statisticsLocgovLikePost(Model model, @ModelAttribute("searchParam") StatisticsLocgovSearch searchParam) {
		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);

		if (ac.getIsLoc() && ac.isPass()) {
			return ViewUtils.redirect("/opmanager/statistics/locgov/like/" + ac.getLocgovCode());
		} else if (ac.getIsLoc() && !ac.isPass()) {
			return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());
		} else {
			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		}


		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		int count = statisticsLocgovService.getLocgovLikeListTotal(searchParam);


		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", statisticsLocgovService.getLocgovLikeList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		model.addAttribute("searchParam", searchParam);

		return "view:/statistics/locgov/list";
	}

	/**
	 * 통계 > 관심 지자체 > 관심 지자체 > 상세
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 지자체 리스트
	 * @return searchParam : 검색조건
	 * @return yyyy 코드에 등록된 년도
	 * @return wdr 코드에 등록된 지자체(시,도)
	 */
	@GetMapping("like/{locgovCode}")
	public String statisticsLocgovLike(Model model, @ModelAttribute("searchParam") StatisticsLocgovSearch searchParam, @PathVariable String locgovCode) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/statistics/locgov/like/{locgovCode}");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		searchParam.setShLocgovCode(locgovCode);
		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		model.addAttribute("total", statisticsLocgovService.getLikeCntByLocgov(searchParam));
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("searchParam", searchParam);
		model.addAttribute("adminRole", ac.getAdminRole());

		return "view:/statistics/locgov/detail";
	}

	@PostMapping("like/{locgovCode}")
	public String searchStatisticsLocgovLike(Model model, @ModelAttribute("searchParam") StatisticsLocgovSearch searchParam, @PathVariable String locgovCode) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/statistics/locgov/like/{locgovCode}");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		searchParam.setShLocgovCode(locgovCode);
		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}

		model.addAttribute("total", statisticsLocgovService.getLikeCntByLocgov(searchParam));
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("searchParam", searchParam);
		model.addAttribute("adminRole", ac.getAdminRole());

		return "view:/statistics/locgov/detail";
	}

	/**
	 * 통계 > 관심 지자체 > 관심 지자체 > 상세
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 지자체 리스트
	 * @return searchParam : 검색조건
	 * @return yyyy 코드에 등록된 년도
	 * @return wdr 코드에 등록된 지자체(시,도)
	 */
	@PostMapping("like/{locgovCode}/{shCntrYear}")
	public JsonView statisticsLocgovLikeByMonth(StatisticsLocgovSearch searchParam, @PathVariable String locgovCode, @PathVariable String shCntrYear) {

		searchParam.setShLocgovCode(locgovCode);
		searchParam.setShCntrYear(shCntrYear);


		return JsonViewUtils.success(statisticsLocgovService.statisticsLocgovLikeByMonth(searchParam));
	}

	/**
	 * 통계 > 관심 지자체 > 관심 지자체 > 상세
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 지자체 리스트
	 * @return searchParam : 검색조건
	 * @return yyyy 코드에 등록된 년도
	 * @return wdr 코드에 등록된 지자체(시,도)
	 */
	@GetMapping("like/excel")
	public ModelAndView statisticsLocgovLikeExcel(StatisticsLocgovSearch searchParam) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);
		if (ac.getIsLoc()) searchParam.setShLocgovCode(ac.getLocgovCode());

		ModelAndView mav = new ModelAndView(new StatisticsLocgovExcelView());

		searchParam.setConditionType("EXCEL_DOWNLOAD");
		searchParam.setPagination(null);

		mav.addObject("list", statisticsLocgovService.getLocgovLikeList(searchParam));


		return mav;
	}

}
