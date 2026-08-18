package saleson.shop.statistics;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import saleson.common.Const;
import saleson.common.enumeration.IdType;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.support.SellerParam;
import saleson.shop.brand.BrandService;
import saleson.shop.brand.support.BrandParam;
import saleson.shop.catalog.domain.CatalogMng;
import saleson.shop.categoriesteamgroup.CategoriesTeamGroupService;
import saleson.shop.customer.CustomerService;
import saleson.shop.give.givestate.GiveStateService;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.givestate.support.GiveStateExcelView;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.manual.domain.Manual;
import saleson.shop.manual.support.ManualParam;
import saleson.shop.order.OrderService;
import saleson.shop.remittance.support.RemittanceExcelView2;
import saleson.shop.statistics.domain.*;
import saleson.shop.statistics.domain.order.OrderCountAmtStat;
import saleson.shop.statistics.support.*;
import saleson.shop.user.LocgovService;
import saleson.shop.user.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Controller
@RequestMapping("/opmanager/shop-statistics")
@RequestProperty(template = "opmanager", layout = "default")
public class ShopStatisticsManagerController {
	private static final Logger log = LoggerFactory.getLogger(ShopStatisticsManagerController.class);

	@Autowired
	ShopStatisticsService shopStatisticsService;

	@Autowired
	CategoriesTeamGroupService categoriesTeamGroupService;

	@Autowired
	ItemService itemService;

	@Autowired
	UserService userService;

	@Autowired
	OrderService orderService;

	@Autowired
	CustomerService customerService;

	@Autowired
	SellerService sellerService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private GiveStateService giveStateService;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private ShopStatisticsBatchService shopStatisticsBatchService;

	@Autowired
	private ShopStatisticsYearBatchService shopStatisticsYearBatchService;

	private TotalSellStatistics makeTotalSellStatisticsData(TotalSellStatistics total, BaseSellStatistics obj) {

		total.setTotalPayCount(total.getTotalPayCount() + obj.getPayCount());
		total.setTotalItemPrice(total.getTotalItemPrice() + obj.getItemPrice());
		//total.setTotalDiscountAmount(total.getTotalDiscountAmount() + obj.getTotalDiscountAmount());
		total.setTotalItemCouponDiscountAmount(total.getTotalItemCouponDiscountAmount() + obj.getItemCouponDiscountAmount());
		total.setTotalSellerDiscountPrice(total.getTotalSellerDiscountPrice() + obj.getSellerDiscountPrice());
		total.setTotalSpotDiscountPrice(total.getTotalSpotDiscountPrice() + obj.getSpotDiscountPrice());
		total.setTotalPayAmount(total.getTotalPayAmount() + obj.getPayTotal());

		total.setTotalCancelCount(total.getTotalCancelCount() + obj.getCancelCount());
		total.setTotalCancelItemPrice(total.getTotalCancelItemPrice() + obj.getCancelItemPrice());
		//total.setTotalCancelDiscountAmount(total.getTotalCancelDiscountAmount() + obj.getCancelTotalDiscountAmount());
		total.setTotalCancelItemCouponDiscountAmount(total.getTotalCancelItemCouponDiscountAmount() + obj.getCancelItemCouponDiscountAmount());
		total.setTotalCancelSellerDiscountPrice(total.getTotalCancelSellerDiscountPrice() + obj.getCancelSellerDiscountPrice());
		total.setTotalCancelSpotDiscountPrice(total.getTotalCancelSpotDiscountPrice() + obj.getCancelSpotDiscountPrice());
		total.setTotalCancelAmount(total.getTotalCancelAmount() + obj.getCancelTotal());

		total.setTotalRevenueItemPrice(total.getTotalRevenueItemPrice() + obj.getSumItemPrice());
		//total.setTotalRevenueDiscountAmount(total.getTotalRevenueDiscountAmount() + obj.getSumDiscountAmount());
		total.setTotalRevenueItemCouponDiscountAmount(total.getTotalRevenueItemCouponDiscountAmount() + obj.getSumItemCouponDiscountAmount());
		total.setTotalRevenueSellerDiscountPrice(total.getTotalRevenueSellerDiscountPrice() + obj.getSumSellerDiscountPrice());
		total.setTotalRevenueSpotDiscountPrice(total.getTotalRevenueSpotDiscountPrice() + obj.getSumSpotDiscountPrice());
		total.setTotalRevenueAmount(total.getTotalRevenueAmount() + obj.getSumTotalAmount());
		return total;
	}

	/**
	 * 카테고리별 통계 합계
	 * @param list
	 * @return
	 */
	private TotalSellStatistics makeTotalForBrand(List<ShopBrandStatistics> list) {

		if (list == null) {
			return null;
		}

		TotalSellStatistics total = new TotalSellStatistics();
		for(ShopBrandStatistics group : list) {
			for(BaseSellStatistics arr : group.getGroupList()) {
				total = makeTotalSellStatisticsData(total, arr);
			}
		}

		return total;
	}

	/**
	 * 카테고리별 통계 합계
	 * @param list
	 * @return
	 */
	private StatsSummary makeTotalForCategory(List<CategoryStatsSummary> list) {

		if (list == null) {
			return null;
		}

		List<BaseStats> baseStats = new ArrayList<>();
		for(CategoryStatsSummary group : list) {
			for(BaseStats arr : group.getGroupStats()) {
				baseStats.add(arr);
			}
		}

		return new StatsSummary(baseStats);
	}

	private StatsSummary makeTotalForArea(List<AreaStatsSummary> list) {

		if (list == null) {
			return null;
		}

		List<BaseStats> baseStats = new ArrayList<>();
		for(AreaStatsSummary group : list) {
			for(BaseStats arr : group.getGroupStats()) {
				baseStats.add(arr);
			}
		}

		return new StatsSummary(baseStats);
	}

	/**
	 * 월별, 년도별, 일별 매출 통계 합계
	 * @param list
	 * @return
	 */
	private StatsSummary makeTotalForDate(List<DateStatsSummary> list) {

		if (list == null) {
			return null;
		}

		List<BaseStats> baseStats = new ArrayList<>();
		for (DateStatsSummary group : list) {
			for (BaseStats arr : group.getGroupStats()) {
				baseStats.add(arr);
			}
		}

		return new StatsSummary(baseStats);
	}

	@GetMapping("/sales/payment")
	public String payment(Model model, StatisticsParam statisticsParam) {

		List<String> days = new ArrayList<>();

		String day = statisticsParam.getEndDate();
		String startDate = statisticsParam.getStartDate();

		try {
			if (day != null && startDate != null) {
				while (Integer.parseInt(day) >= Integer.parseInt(startDate)) {
					days.add(day);
					day = DateUtils.addDay(day, -1);
				}
			}
		} catch (NumberFormatException e) {
//			log.warn(e.getMessage());
			log.warn("========== payment  NumberFormatException =========");
		}
		model.addAttribute("days", days);
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("approvalTypes", CodeUtils.getCodeInfoList("ORDER_PAY_TYPE"));
		model.addAttribute("list", Collections.EMPTY_LIST);
		return ViewUtils.view();
	}

	@PostMapping("/sales/payment")
	public String searchPayment(Model model, StatisticsParam statisticsParam) {

		List<String> days = new ArrayList<>();

		String day = statisticsParam.getEndDate();
		String startDate = statisticsParam.getStartDate();

		try {
			if (day != null && startDate != null) {
				while (Integer.parseInt(day) >= Integer.parseInt(startDate)) {
					days.add(day);
					day = DateUtils.addDay(day, -1);
				}
			}
		} catch (NumberFormatException e) {
//			log.warn(e.getMessage());
			log.warn("========== payment  NumberFormatException =========");
		}
		model.addAttribute("days", days);
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("approvalTypes", CodeUtils.getCodeInfoList("ORDER_PAY_TYPE"));
		model.addAttribute("list", shopStatisticsService.getPaymentStatisticsListByParam(statisticsParam));

		return ViewUtils.view();
	}

	/**
	 * 결제타입별 매출 통계 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/payment/excel-download")
	public ModelAndView paymentExcelDownload(StatisticsParam statisticsParam) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new PaymentStatisticsExcelView());

		List<String> days = new ArrayList<>();

		String day = statisticsParam.getEndDate();
		String startDate = statisticsParam.getStartDate();

		try {
			if (day != null && startDate != null) {
				while (Integer.parseInt(day) >= Integer.parseInt(startDate)) {
					days.add(day);
					day = DateUtils.addDay(day, -1);
				}
			}
		} catch (NumberFormatException e) {
//			log.warn(e.getMessage());
			log.warn("========= paymentExcelDownload NumberFormatException ===========");
		}

		mav.addObject("days", days);
		mav.addObject("statisticsParam", statisticsParam);
		mav.addObject("approvalTypes", CodeUtils.getCodeInfoList("ORDER_PAY_TYPE"));
		mav.addObject("list", shopStatisticsService.getPaymentStatisticsListByParam(statisticsParam));

		return mav;
	}

	/**
	 * 일별 매출 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/day")
	public String salesDay(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		return "view:/shop-statistics/sales/day";
	}

	@PostMapping("/sales/day")
	public String searchSalesDay(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		List<DateStatsSummary> list = shopStatisticsService.getDateStatsList(statisticsParam);

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);

		model.addAttribute("total", makeTotalForDate(list));
		model.addAttribute("dateList", list);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));

		return "view:/shop-statistics/sales/day";
	}

	/**
	 * 일별 매출 통계 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/day/excel-download")
	public ModelAndView dayExcelDownload(StatisticsParam statisticsParam) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new DayStatisticsExcelView());

		List<DateStatsSummary> list = shopStatisticsService.getDateStatsList(statisticsParam);

		mav.addObject("total", makeTotalForDate(list));
		mav.addObject("list", list);

		return mav;
	}

	/**
	 * 월별 매출 통계 엑셀 다운로드
	 * @param type
	 * @param param
	 * @return
	 */
	@GetMapping("/sales/{type}/detail-excel-download")
	public ModelAndView detailExcelDownload(@PathVariable("type") String type, StatisticsParam param) {

		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		param.setType("detail");
		String sellerIdForParam = null;
		if (param.getSellerId() > 0) {
			sellerIdForParam = Long.toString(param.getSellerId());
		}

		ModelAndView mav = new ModelAndView(new RevenueDetailExcelView(type));
		mav.addObject("sellerIdForParam", sellerIdForParam);
		mav.addObject("list", shopStatisticsService.getRevenueDetailListForDateByParam(param));

		return mav;
	}

	/**
	 * 일자별로 매출 상세 화면을 보여줌.
	 * @param model
	 * @param type
	 * @param param
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/{type}/detail")
	@RequestProperty(layout = "base")
	public String revenueDetail(Model model, @PathVariable("type") String type, StatisticsParam param, RequestContext requestContext) {
		param.setType("detail");

		String title = DateUtils.date(param.getStartDate()) + "~" + DateUtils.date(param.getEndDate()) + " 판매 상세";
		/*if ("user".equals(type)) {

			String userName = "";
			if (param.getCustomerCode() != null) {

				Customer customer = customerService.getCustomerById(param.getCustomerCode());
				if (customer != null) {
					model.addAttribute("userName", customer.getCustomerName());
				}
			}

			param.setUserName(userName);
			title = param.getUserName() + MessageUtils.getMessage("M01401") + "(" + DateUtils.date(param.getStartDate()) + "~" + DateUtils.date(param.getEndDate()) + ")";
		}*/
		String sellerIdForParam = null;
		if (param.getSellerId() > 0) {
			sellerIdForParam = Long.toString(param.getSellerId());
		}
		List<RevenueBaseForDate> statsList = shopStatisticsService.getRevenueDetailListForDateByParam(param);
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("mode", type);
		model.addAttribute("title", title);
		model.addAttribute("orderList",	statsList);
		model.addAttribute("sellerIdForParam", sellerIdForParam);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		// 음수 표기 여부
		model.addAttribute("nagativeNumber", ShopUtils.DISPLAY_STATS_NEGATIVE_NUMBER);

		return ViewUtils.getManagerView("/shop-statistics/sales/day-detail");
	}

	@PostMapping("/sales/{type}/detail")
	@RequestProperty(layout = "base")
	public String searchRevenueDetail(Model model, @PathVariable("type") String type, StatisticsParam param, RequestContext requestContext) {
		param.setType("detail");

		String title = DateUtils.date(param.getStartDate()) + "~" + DateUtils.date(param.getEndDate()) + " 판매 상세";
		/*if ("user".equals(type)) {

			String userName = "";
			if (param.getCustomerCode() != null) {

				Customer customer = customerService.getCustomerById(param.getCustomerCode());
				if (customer != null) {
					model.addAttribute("userName", customer.getCustomerName());
				}
			}

			param.setUserName(userName);
			title = param.getUserName() + MessageUtils.getMessage("M01401") + "(" + DateUtils.date(param.getStartDate()) + "~" + DateUtils.date(param.getEndDate()) + ")";
		}*/
		String sellerIdForParam = null;
		if (param.getSellerId() > 0) {
			sellerIdForParam = Long.toString(param.getSellerId());
		}
		List<RevenueBaseForDate> statsList = shopStatisticsService.getRevenueDetailListForDateByParam(param);
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("mode", type);
		model.addAttribute("title", title);
		model.addAttribute("orderList",	statsList);
		model.addAttribute("sellerIdForParam", sellerIdForParam);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		// 음수 표기 여부
		model.addAttribute("nagativeNumber", ShopUtils.DISPLAY_STATS_NEGATIVE_NUMBER);

		return ViewUtils.getManagerView("/shop-statistics/sales/day-detail");
	}

	/**
	 * 관리자 팀별 그룹 관리 리스트
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/day/order")
	@RequestProperty(title = "매출 통계", layout = "base")
	public String salesDayOrder(Model model, StatisticsParam statisticsParam) {

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("orderList",	shopStatisticsService.getOrderListByParam(statisticsParam));

		return ViewUtils.getManagerView("/shop-statistics/sales/order");
	}

	/**
	 * 월별 매출 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/month")
	public String salesMonth(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		model.addAttribute("lastYear", DateUtils.getToday("yyyy"));
		return "view:/shop-statistics/sales/month";
	}

	@PostMapping("/sales/month")
	public String searchSalesMonth(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		statisticsParam.setType("month");

		List<DateStatsSummary> list = shopStatisticsService.getDateStatsList(statisticsParam);

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("lastYear", DateUtils.getToday("yyyy"));
		model.addAttribute("statisticsParam", statisticsParam);

		model.addAttribute("total", makeTotalForDate(list));
		model.addAttribute("dateList", list);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));

		return "view:/shop-statistics/sales/month";

	}

	/**
	 * 월별 매출 통계 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/month/excel-download")
	public ModelAndView monthExcelDownload(StatisticsParam statisticsParam) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		statisticsParam.setType("month");

		List<DateStatsSummary> list = shopStatisticsService.getDateStatsList(statisticsParam);

		ModelAndView mav = new ModelAndView(new MonthStatisticsExcelView());

		mav.addObject("list", list);
		mav.addObject("total", makeTotalForDate(list));

		return mav;
	}

	/**
	 * 년별 매출 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/year")
	public String salesYear(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		statisticsParam.setType("year");

		model.addAttribute("lastYear", DateUtils.getToday("yyyy"));
		return "view:/shop-statistics/sales/year";
	}

	@PostMapping("/sales/year")
	public String searchSalesYear(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		statisticsParam.setType("year");

 		List<DateStatsSummary> list = shopStatisticsService.getDateStatsList(statisticsParam);

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("lastYear", DateUtils.getToday("yyyy"));
		model.addAttribute("statisticsParam", statisticsParam);

		model.addAttribute("total", makeTotalForDate(list));
		model.addAttribute("dateList", list);
	//	model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));

		return "view:/shop-statistics/sales/year";
	}

	/**
	 * 통계 > 답례품 구매현황 > 전체 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/all")
	public String salesAll(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		if (ObjectUtils.isEmpty(statisticsParam.getYear())) {
			statisticsParam.setYear(DateUtils.getToday("yyyy"));
		}

		if (ObjectUtils.isEmpty(statisticsParam.getLocgovSearchMonth())) {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + "01");
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + "12");
		} else {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
		}

		statisticsParam.setExtra("DATE");
		statisticsParam.setType("year");
		List<DateStatsSummary> list = shopStatisticsService.getDateStatsListGhlove(statisticsParam);

		statisticsParam.setType("month");
		List<DateStatsSummary> monthList = shopStatisticsService.getDateStatsListGhlove(statisticsParam);

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("dateList", list);
		model.addAttribute("monthList", monthList);

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		return ViewUtils.view();
	}

	@PostMapping("/sales/all")
	public String searchSalesAll(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		if (ObjectUtils.isEmpty(statisticsParam.getYear())) {
			statisticsParam.setYear(DateUtils.getToday("yyyy"));
		}

		if (ObjectUtils.isEmpty(statisticsParam.getLocgovSearchMonth())) {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + "01");
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + "12");
		} else {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
		}

		statisticsParam.setExtra("DATE");
		statisticsParam.setType("year");
		List<DateStatsSummary> list = shopStatisticsService.getDateStatsListGhlove(statisticsParam);

		statisticsParam.setType("month");
		List<DateStatsSummary> monthList = shopStatisticsService.getDateStatsListGhlove(statisticsParam);

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("dateList", list);
		model.addAttribute("monthList", monthList);

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		return ViewUtils.view();
	}

	/**
	 * 통계 > 답례품 구매현황 > 전체 (고향사랑e음) 월차트
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/sales/all/month")
	public JsonView salesAllMonth(StatisticsParam statisticsParam) {
		statisticsParam.setExtra("DATE");
		statisticsParam.setType("month");

		if (ObjectUtils.isEmpty(statisticsParam.getLocgovSearchMonth())) {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + "01");
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + "12");
		} else {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
		}

		return JsonViewUtils.success(shopStatisticsService.getDateStatsListGhlove(statisticsParam));
	}

	/**
	 * 통계 > 답례품 구매현황 > 지자체별 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/locgov")
	public String salesLocgov(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {

			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_4".equals(userRole.getAuthority())) {	// ROLE_ADMIN_1(시스템주담당자),ROLE_ADMIN_2(시스템부담당자),ROLE_ADMIN_3(행안부주담당자),ROLE_ADMIN_4(행안부부담당자)
				role = "mois";
				break;
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
				break;
			}
		}

		if (ObjectUtils.isEmpty(statisticsParam.getYear())) {
			statisticsParam.setYear(DateUtils.getToday("yyyy"));
		}

		if (ObjectUtils.isEmpty(statisticsParam.getLocgovSearchMonth())) {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + "01");
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + "12");
		} else {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
		}

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("role", role);	//
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 조회조건 : 년도

		if ("mois".equals(role) && StringUtils.isEmpty(statisticsParam.getLocgovCode())) {
			statisticsParam.setExtra("LOCGOV");

			// 목록(페이징)
			int count = 0;
			Pagination pagination = Pagination.getInstance(count, statisticsParam.getItemsPerPage());
			statisticsParam.setPagination(pagination);

			List<DateStatsSummary> list = Collections.EMPTY_LIST;

			model.addAttribute("dateList", list);
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);
			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드


			return "view:/shop-statistics/sales/locgov-mois";
		} else if ("locgov".equals(role) || ("mois".equals(role) && StringUtils.isNotEmpty(statisticsParam.getLocgovCode()))) {

			if(role == "locgov") {
				HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
				String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
				statisticsParam.setLocgovCode(locgovCode);
			}

			statisticsParam.setExtra("ITEM");

			// 목록(페이징)
			int count = 0;
			Pagination pagination = Pagination.getInstance(count, statisticsParam.getItemsPerPage());
			statisticsParam.setPagination(pagination);

			List<DateStatsSummary> list = Collections.EMPTY_LIST;

			model.addAttribute("dateList", list);
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			return ViewUtils.view();
		} else {
			return "redirect:/";
		}
	}

	@PostMapping("/sales/locgov")
	public String searchSalesLocgov(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {

			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_4".equals(userRole.getAuthority())) {	// ROLE_ADMIN_1(시스템주담당자),ROLE_ADMIN_2(시스템부담당자),ROLE_ADMIN_3(행안부주담당자),ROLE_ADMIN_4(행안부부담당자)
				role = "mois";
				break;
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
				break;
			}
		}

		if (ObjectUtils.isEmpty(statisticsParam.getYear())) {
			statisticsParam.setYear(DateUtils.getToday("yyyy"));
		}

		if (ObjectUtils.isEmpty(statisticsParam.getLocgovSearchMonth())) {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + "01");
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + "12");
		} else {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
		}

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("role", role);	//
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 조회조건 : 년도

		if ("mois".equals(role) && StringUtils.isEmpty(statisticsParam.getLocgovCode())) {
			statisticsParam.setExtra("LOCGOV");

			// 목록(페이징)
			int count = shopStatisticsService.getDateStatsListCountGhlove(statisticsParam);
			Pagination pagination = Pagination.getInstance(count, statisticsParam.getItemsPerPage());
			statisticsParam.setPagination(pagination);

			List<DateStatsSummary> list = shopStatisticsService.getDateStatsListGhlove(statisticsParam);

			model.addAttribute("dateList", list);
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);
			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드


			return "view:/shop-statistics/sales/locgov-mois";
		} else if ("locgov".equals(role) || ("mois".equals(role) && StringUtils.isNotEmpty(statisticsParam.getLocgovCode()))) {

			if(role == "locgov") {
				HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
				String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
				statisticsParam.setLocgovCode(locgovCode);
			}

			statisticsParam.setExtra("ITEM");

			// 목록(페이징)
			int count = shopStatisticsService.getDateStatsListCountGhlove(statisticsParam);
			Pagination pagination = Pagination.getInstance(count, statisticsParam.getItemsPerPage());
			statisticsParam.setPagination(pagination);

			List<DateStatsSummary> list = shopStatisticsService.getDateStatsListGhlove(statisticsParam);

			model.addAttribute("dateList", list);
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			return ViewUtils.view();
		} else {
			return "redirect:/";
		}

	}

	/**
	 * 통계 > 답례품 구매현황 > 지자체별 (고향사랑e음) 엑셀 다운로드 (행안부-지자체전체)
	 * @return
	 */
	@GetMapping(value="/sales/locgov/mois/download-excel")
	public ModelAndView downloadExcelShopStatisiecsMois(StatisticsParam statisticsParam) {
		ModelAndView mav = new ModelAndView(new ShopStatisticsMoisExcelView());

		statisticsParam.setConditionType("EXCEL_DOWNLOAD");
		statisticsParam.setPagination(null);

		statisticsParam.setExtra("LOCGOV");

		if (ObjectUtils.isEmpty(statisticsParam.getLocgovSearchMonth())) {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + "01");
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + "12");
		} else {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
		}

		mav.addObject("list", shopStatisticsService.getDateStatsListGhlove(statisticsParam));

		return mav;
	}

	/**
	 * 통계 > 답례품 구매현황 > 지자체별 (고향사랑e음) 엑셀 다운로드 (지자체)
	 * @return
	 */
	@GetMapping(value="/sales/locgov/download-excel")
	public ModelAndView downloadExcelShopStatisiecs(StatisticsParam statisticsParam) {
		ModelAndView mav = new ModelAndView(new ShopStatisticsLocgovExcelView());

		statisticsParam.setConditionType("EXCEL_DOWNLOAD");
		statisticsParam.setPagination(null);

		statisticsParam.setExtra("ITEM");

		if (ObjectUtils.isEmpty(statisticsParam.getLocgovSearchMonth())) {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + "01");
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + "12");
		} else {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
		}

		mav.addObject("list", shopStatisticsService.getDateStatsListGhlove(statisticsParam));

		return mav;
	}

	/**
	 * 통계 > 답례품 구매현황 > 지자체별 상세 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("sales/locgov/detail")
	@RequestProperty(layout = "base")
	public String managerCreate(Model model, StatisticsParam statisticsParam, RequestContext requestContext){

		statisticsParam.setExtra("USER");

		if (ObjectUtils.isEmpty(statisticsParam.getLocgovSearchMonth())) {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + "01");
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + "12");
		} else {
			statisticsParam.setLocgovSearchStartYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
			statisticsParam.setLocgovSearchEndYearMonth(statisticsParam.getYear() + statisticsParam.getLocgovSearchMonth());
		}

		// 목록(페이징)
		int count = shopStatisticsService.getDateStatsListCountGhlove(statisticsParam);
		Pagination pagination = Pagination.getInstance(count, 5);
		statisticsParam.setPagination(pagination);

		List<DateStatsSummary> list = shopStatisticsService.getDateStatsListGhlove(statisticsParam);

		model.addAttribute("dateList", list);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		if (list.size() > 0) {
			model.addAttribute("itemName", list.get(0).getGroupStats().get(0).getItemName());
		}

		return "view:/shop-statistics/sales/detail";
	}

	/**
	 * 통계 > 답례품 선호도 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/wish/list")
	public String wishList(Model model, WishlistParam wishlistParam, RequestContext requestContext) {
		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {

			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_4".equals(userRole.getAuthority())) {	// ROLE_ADMIN_1(시스템주담당자),ROLE_ADMIN_2(시스템부담당자),ROLE_ADMIN_3(행안부주담당자),ROLE_ADMIN_4(행안부부담당자)
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if(role == "locgov") {
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			wishlistParam.setLocgovCode(locgovCode);

			// 지자체명
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCode);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());
		}

		// 목록(페이징)
		int count = 0;
		Pagination pagination = Pagination.getInstance(count, wishlistParam.getItemsPerPage());
		wishlistParam.setPagination(pagination);

		List<Wishlist> list = Collections.EMPTY_LIST;

		model.addAttribute("dateList", list);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		model.addAttribute("role", role);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("wishlistParam", wishlistParam);

		return ViewUtils.view();
	}

	@PostMapping("/wish/list")
	public String wishListPost(Model model, WishlistParam wishlistParam, RequestContext requestContext) {
		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {

			if ("ROLE_ADMIN_1".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_2".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_3".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_4".equals(userRole.getAuthority())) {	// ROLE_ADMIN_1(시스템주담당자),ROLE_ADMIN_2(시스템부담당자),ROLE_ADMIN_3(행안부주담당자),ROLE_ADMIN_4(행안부부담당자)
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority())
					|| "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if(role == "locgov") {
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			wishlistParam.setLocgovCode(locgovCode);

			// 지자체명
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCode);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());
		}

		// 목록(페이징)
		int count = shopStatisticsService.getOpWishlistListCount(wishlistParam);
		Pagination pagination = Pagination.getInstance(count, wishlistParam.getItemsPerPage());
		wishlistParam.setPagination(pagination);

		List<Wishlist> list = shopStatisticsService.getOpWishlistList(wishlistParam);

		model.addAttribute("dateList", list);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		model.addAttribute("role", role);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("wishlistParam", wishlistParam);

		return ViewUtils.view();
	}

	/**
	 * 통계 > 답례품 선호도 > 엑셀다운로드 (행안부)
	 * @return
	 */
	@GetMapping(value="/wish/list/mois/download-excel")
	public ModelAndView downloadExcelShopWishMois(WishlistParam wishlistParam) {
		ModelAndView mav = new ModelAndView(new ShopWishMoisExcelView());

		wishlistParam.setConditionType("EXCEL_DOWNLOAD");
		wishlistParam.setPagination(null);

		mav.addObject("list", shopStatisticsService.getOpWishlistList(wishlistParam));

		return mav;
	}

	/**
	 * 통계 > 답례품 선호도 > 엑셀다운로드 (지자체)
	 * @return
	 */
	@GetMapping(value="/wish/list/locgov/download-excel")
	public ModelAndView downloadExcelShopWishLocgov(WishlistParam wishlistParam) {
		String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

		if (StringUtils.hasLength(locgovCode)) {
			wishlistParam.setLocgovCode(locgovCode);
		}

		ModelAndView mav = new ModelAndView(new ShopWishLocgovExcelView());

		wishlistParam.setConditionType("EXCEL_DOWNLOAD");
		wishlistParam.setPagination(null);

		mav.addObject("list", shopStatisticsService.getOpWishlistList(wishlistParam));

		return mav;
	}

	/**
	 * 년별 매출 통계 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/year/excel-download")
	public ModelAndView yearExcelDownload(StatisticsParam statisticsParam) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		statisticsParam.setType("year");

		List<DateStatsSummary> list = shopStatisticsService.getDateStatsList(statisticsParam);

		ModelAndView mav = new ModelAndView(new YearStatisticsExcelView());

		mav.addObject("list", list);
		mav.addObject("total", makeTotalForDate(list));

		return mav;
	}

	/**
	 * 회원별 매출 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/user")
	public String salesUser(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		if (statisticsParam.getOrderBy() == null) {
			statisticsParam.setOrderBy("PRICE");
		}

		if (statisticsParam.getSort() == null) {
			statisticsParam.setSort("DESC");
		}

		int totalCount = shopStatisticsService.getUserStatisticsCountByParam(statisticsParam);

		Pagination pagination = Pagination.getInstance(totalCount, 30);
		statisticsParam.setPagination(pagination);

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("pagination", pagination);

		model.addAttribute("total", shopStatisticsService.getUserTotalRevenueStatisticsByParam(statisticsParam));
		model.addAttribute("userList", shopStatisticsService.getUserStatisticsListByParam(statisticsParam));

		return ViewUtils.view();
	}

	@PostMapping("/sales/user")
	public String searchSalesUser(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		if (statisticsParam.getOrderBy() == null) {
			statisticsParam.setOrderBy("PRICE");
		}

		if (statisticsParam.getSort() == null) {
			statisticsParam.setSort("DESC");
		}

		int totalCount = shopStatisticsService.getUserStatisticsCountByParam(statisticsParam);

		Pagination pagination = Pagination.getInstance(totalCount, 30);
		statisticsParam.setPagination(pagination);

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("pagination", pagination);

		model.addAttribute("total", shopStatisticsService.getUserTotalRevenueStatisticsByParam(statisticsParam));
		model.addAttribute("userList", shopStatisticsService.getUserStatisticsListByParam(statisticsParam));

		return ViewUtils.view();
	}

	/**
	 * 회원별 매출 통계 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/user/excel-download")
	public ModelAndView userExcelDownload(StatisticsParam statisticsParam) {

		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new UserStatisticsExcelView());
		mav.addObject("list", shopStatisticsService.getUserStatisticsListByParam(statisticsParam));

		return mav;
	}

	/**
	 * 관리자 팀별 그룹 관리 리스트
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/user/order-detail")
	@RequestProperty(title = "매출 통계", layout = "base")
	public String salesUserOrderDetail(Model model, StatisticsParam statisticsParam) {

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("order", shopStatisticsService.getUsetOrderTotalDetailById(statisticsParam));
		model.addAttribute("userOrderItemList", shopStatisticsService.getUserOrderItemListByParam(statisticsParam));

		return ViewUtils.getManagerView("/shop-statistics/sales/order-detail");
	}

	/**
	 * 관리자 팀별 그룹 관리 리스트
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/user/user-order")
	@RequestProperty(title = "매출 통계", layout = "base")
	public String salesUserOrder(Model model, StatisticsParam statisticsParam) {

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("order", shopStatisticsService.getUsetOrderTotalDetailById(statisticsParam));
		model.addAttribute("userOrderList",	shopStatisticsService.getUserOrderListByParam(statisticsParam));

		return ViewUtils.getManagerView("/shop-statistics/sales/user-order");
	}

	@GetMapping("/sales/brand")
	public String salesBrand(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		if (statisticsParam.getOrderBy() == null) {
			statisticsParam.setOrderBy("PRICE");
		}

		if (statisticsParam.getSort() == null) {
			statisticsParam.setSort("DESC");
		}

		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);

		List<ShopBrandStatistics> list = shopStatisticsService.getBrandStatisticsListByParam(statisticsParam);

		model.addAttribute("brandList", brandService.getBrandList(new BrandParam()));
		model.addAttribute("total", this.makeTotalForBrand(list));
		model.addAttribute("brandStatisticsList", list);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		return ViewUtils.getView("/shop-statistics/sales/brand");

	}

	@PostMapping("/sales/brand")
	public String searchSalesBrand(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		if (statisticsParam.getOrderBy() == null) {
			statisticsParam.setOrderBy("PRICE");
		}

		if (statisticsParam.getSort() == null) {
			statisticsParam.setSort("DESC");
		}

		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);

		List<ShopBrandStatistics> list = shopStatisticsService.getBrandStatisticsListByParam(statisticsParam);

		model.addAttribute("brandList", brandService.getBrandList(new BrandParam()));
		model.addAttribute("total", this.makeTotalForBrand(list));
		model.addAttribute("brandStatisticsList", list);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		return ViewUtils.getView("/shop-statistics/sales/brand");

	}

	@GetMapping("/sales/brand/brand-detail")
	@RequestProperty(title = "브랜드 매출 통계", layout = "base")
	public String salesBrandDetail(Model model, StatisticsParam statisticsParam){

		model.addAttribute("brandName",statisticsParam.getBrand());

		/* 정보없음 비교 수정 2017-02-27 yulsun.yoo
		 if (statisticsParam.getBrand().equals("정보없음")) {
			statisticsParam.setBrand("");
		}*/
		if ("정보없음".equals(statisticsParam.getBrand())) {
			statisticsParam.setBrand("");
		}

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("brandDetailList", shopStatisticsService.getBrandStatisticsDetailByParam(statisticsParam));


		return ViewUtils.getView("/shop-statistics/sales/brand-detail");
	}


	/**
	 * 상품별 판매액 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/brand/excel-download")
	public ModelAndView salesBrandExcelDownload(StatisticsParam statisticsParam) {

		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new BrandStatisticsExcelView());

		if (statisticsParam.getOrderBy() == null) {
			statisticsParam.setOrderBy("PRICE");
		}

		if (statisticsParam.getSort() == null) {
			statisticsParam.setSort("DESC");
		}

		mav.addObject("itemList", shopStatisticsService.getBrandStatisticsListByParam(statisticsParam));

		return mav;
	}

	/**
	 * 판매자별 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/seller")
	public String salesSeller(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		StatsSummary statsSummary = shopStatisticsService.getSellerStatsSummary(statisticsParam);

		int totalCount = statsSummary.getTotalRecord();
		Pagination pagination = Pagination.getInstance(totalCount, statisticsParam.getItemsPerPage());

		statisticsParam.setPagination(pagination);

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("pagination", pagination);
		model.addAttribute("statisticsParam", statisticsParam);

		model.addAttribute("total", statsSummary);
		model.addAttribute("sellerStatsList", shopStatisticsService.getSellerStatsList(statisticsParam));
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));

		return ViewUtils.view();
	}

	@PostMapping("/sales/seller")
	public String searchSalesSeller(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		StatsSummary statsSummary = shopStatisticsService.getSellerStatsSummary(statisticsParam);

		int totalCount = statsSummary.getTotalRecord();
		Pagination pagination = Pagination.getInstance(totalCount, statisticsParam.getItemsPerPage());

		statisticsParam.setPagination(pagination);

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("pagination", pagination);
		model.addAttribute("statisticsParam", statisticsParam);

		model.addAttribute("total", statsSummary);
		model.addAttribute("sellerStatsList", shopStatisticsService.getSellerStatsList(statisticsParam));
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));

		return ViewUtils.view();
	}

	/**
	 * 판매자별 통계 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/seller/excel-download")
	public ModelAndView salesSellerExcelDownload(StatisticsParam statisticsParam) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new SellerStatisticsExcelView());

		mav.addObject("statisticsParam", statisticsParam);
		mav.addObject("sellerStatsList", shopStatisticsService.getSellerStatsList(statisticsParam));

		return mav;
	}

	/**
	 * 상품별 판매액 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/item")
	public String salesItem(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		if (statisticsParam.getOrderBy() == null) {
			statisticsParam.setOrderBy("PRICE");
		}

		if (statisticsParam.getSort() == null) {
			statisticsParam.setSort("DESC");
		}

		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());

		StatsSummary statsSummary = shopStatisticsService.getItemStatsSummary(statisticsParam);

		int totalCount = statsSummary.getTotalRecord();

		Pagination pagination = Pagination.getInstance(totalCount, statisticsParam.getItemsPerPage());

		statisticsParam.setPagination(pagination);

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("pagination", pagination);
		model.addAttribute("statisticsParam", statisticsParam);

		model.addAttribute("total", statsSummary);
		model.addAttribute("itemStatsList", shopStatisticsService.getItemStatsList(statisticsParam));
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));

		return ViewUtils.view();
	}

	/**
	 * 상품별 판매액 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/item/excel-download")
	public ModelAndView salesItemExcelDownload(StatisticsParam statisticsParam) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new ItemStatisticsExcelView());

		if (statisticsParam.getOrderBy() == null) {
			statisticsParam.setOrderBy("PRICE");
		}

		if (statisticsParam.getSort() == null) {
			statisticsParam.setSort("DESC");
		}

		mav.addObject("itemStatsList", shopStatisticsService.getItemStatsList(statisticsParam));

		return mav;
	}

	/**
	 * 관리자 팀별 그룹 관리 리스트
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/item/day")
	public String salesItemDay(Model model, StatisticsParam statisticsParam,
			RequestContext requestContext) {

		List<HashMap<String, String>> weekList = getWeekArraySelect();

		model.addAttribute("weekList", weekList);
		model.addAttribute("categoryTeamGroupList",
				categoriesTeamGroupService.getCategoriesTeamGroupList());

		if (statisticsParam == null || statisticsParam.getStartDate() == null || statisticsParam.getType() == null) {
			return ViewUtils.getManagerView("/shop-statistics/sales/item-day");
		}

		if (statisticsParam.getWeekType() == null) {
			statisticsParam.setWeekType("7");
		}
		if (model != null) {
			String dateType = statisticsParam.getType();
			if ("1".equals(dateType)) {
				typeOfSearch(statisticsParam, model);

			} else if ("2".equals(dateType)) {
				typeOfMonth(statisticsParam, model);

			} else if ("3".equals(dateType)) {
				typeOfWeek(statisticsParam, model);

			} else if ("4".equals(dateType)) {
				typeOfDay(statisticsParam, model);

			}
		}

		if(requestContext.getQueryString() != null) {
			model.addAttribute("queryString", requestContext.getQueryString());
		}

		return ViewUtils.getManagerView("/shop-statistics/sales/item-day");
	}

	/**
	 * 관리자 팀별 그룹 관리 리스트
	 *
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/item/day-excel-download")
	public ModelAndView salesItemDayExcelDownload(StatisticsParam statisticsParam) {

		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new ItemDateStatisticsExcelView());

		/*
		 * List<HashMap<String,String>> weekList = getWeekArraySelect();
		 *
		 * model.addAttribute("weekList", weekList);
		 * model.addAttribute("categoryTeamGroupList",
		 * categoriesTeamGroupService.getCategoriesTeamGroupList());
		 *
		 * if(statisticsParam.getStartDate() == null ){ return mav; }
		 *
		 * if(statisticsParam.getWeekType() == null){
		 * statisticsParam.setWeekType("7"); }
		 */

		if (mav != null) {
			String dateType = statisticsParam.getType();
			if ("1".equals(dateType)) {
				typeOfSearch(statisticsParam, mav);

			} else if ("2".equals(dateType)) {
				typeOfMonth(statisticsParam, mav);

			} else if ("3".equals(dateType)) {
				typeOfWeek(statisticsParam, mav);

			} else if ("4".equals(dateType)) {
				typeOfDay(statisticsParam, mav);

			}
		}

		return mav;

	}

	/**
	 * 상품별 판매 상세 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/item/{itemId}/excel-download")
	public ModelAndView salesItemDetailExcelDownload(StatisticsParam statisticsParam) {

		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		Item item = itemService.getItemById(Integer.parseInt(statisticsParam.getItemId()));

		ModelAndView mav = new ModelAndView(new ItemDetailExcelView(item));
		mav.addObject("list", shopStatisticsService.getShopItemDetailList(statisticsParam));

		return mav;
	}

	/**
	 * 관리자 팀별 그룹 관리 리스트
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/item/{itemId}")
	@RequestProperty(title = "매출 통계", layout = "base")
	public String salesItemDetail(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		model.addAttribute("item", itemService.getItemById(Integer
				.parseInt(statisticsParam.getItemId())));
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("itemStatisticsList", shopStatisticsService.getShopItemDetailList(statisticsParam));
		if (requestContext.getQueryString() != null) {
			model.addAttribute("queryString", requestContext.getQueryString());
		}
		return ViewUtils.getManagerView("/shop-statistics/sales/item-detail");
	}

	/**
	 * 판매 현황(미사용) 카테고리별 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/category")
	public String salesCategory(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		List<CategoryStatsSummary> categoryStatsList = shopStatisticsService.getCategoryStatsList(statisticsParam);

		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());
		model.addAttribute("categoryStatsList", categoryStatsList);

		model.addAttribute("total", this.makeTotalForCategory(categoryStatsList));
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));

		return ViewUtils.view();
	}

	/**
	 * 답례품 구매현황 카테고리별 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/categories")
	public String salesCategories(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		// statisticsParam의 getStartDate/getEndDate는
		// 각각 해당월의 1일/31일로 자동 설정되어
		// startDate = null 이더라도, statisticsParam.getStartDate은 "yyyyMM01"로 출력
		statisticsParam.setStartDate(DateUtils.getToday("yyyyMM") + "01");
		statisticsParam.setEndDate(DateUtils.getToday(Const.DATE_FORMAT));

		List<CategoriesStatsSummary> categoriesStatsList = Collections.EMPTY_LIST;;

		model.addAttribute("categoriesStatsList", categoriesStatsList);
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return ViewUtils.view();
	}

	/**
	 * 답례품 구매현황 카테고리별 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/sales/categories")
	public String searchSalesCategories(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		String role = "";
		User user = UserUtils.getUser();
		// user.getUserRoles() = ArrayList<E>
		for (UserRole userRole : user.getUserRoles()) {
			if (
				"ROLE_ADMIN_5".equals(userRole.getAuthority())
				|| "ROLE_ADMIN_6".equals(userRole.getAuthority())
			) {
				// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		// 지역변수 세팅
		if (role == "locgov") {
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			String locgovCode = locgovObj.get("LOCGOV_CODE").toString();
			statisticsParam.setLocgovCode(locgovCode);
		} else {
			statisticsParam.setLocgovCode(statisticsParam.getShLocgovCode());
		}

		// pagination
		int totalItems = shopStatisticsService.getCategoriesStatsListCount(statisticsParam);

		List<CategoriesStatsSummary> categoriesStatsList = shopStatisticsService.getCategoriesStatsList(statisticsParam);

		model.addAttribute("categoriesStatsList", categoriesStatsList);
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("totalCount", totalItems);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return ViewUtils.view();
	}

	/**
	 * 카테고리별 통계 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/category/excel-download")
	public ModelAndView salesCategoryExcelDownload(StatisticsParam statisticsParam) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new CategoryStatisticsExcelView());

		mav.addObject("statisticsParam", statisticsParam);
		mav.addObject("categoryStatsList", shopStatisticsService.getCategoryStatsList(statisticsParam));

		return mav;
	}

	/**
	 * 지역별 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/area")
	public String salesArea(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		if (statisticsParam.getOrderBy() == null) {
			statisticsParam.setOrderBy("PRICE");
		}

		if (statisticsParam.getSort() == null) {
			statisticsParam.setSort("DESC");
		}

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);

		List<AreaStatsSummary> areaStatsList = shopStatisticsService.getAreaStatsList(statisticsParam);

		model.addAttribute("total", this.makeTotalForArea(areaStatsList));
		model.addAttribute("areaStatsList", areaStatsList);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		return ViewUtils.view();
	}

	@PostMapping("/sales/area")
	public String searchSalesArea(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		if (statisticsParam.getOrderBy() == null) {
			statisticsParam.setOrderBy("PRICE");
		}

		if (statisticsParam.getSort() == null) {
			statisticsParam.setSort("DESC");
		}

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);

		List<AreaStatsSummary> areaStatsList = shopStatisticsService.getAreaStatsList(statisticsParam);

		model.addAttribute("total", this.makeTotalForArea(areaStatsList));
		model.addAttribute("areaStatsList", areaStatsList);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		return ViewUtils.view();
	}

	/**
	 * 지역별 통계 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping(value = "/sales/area/excel-download")
	public ModelAndView downloadExcelArea(StatisticsParam statisticsParam) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new AreaStatisticsExcelView(statisticsParam.getConditionType()));
		mav.addObject("areaStatsList", shopStatisticsService.getAreaStatsList(statisticsParam));
		return mav;
	}

	/**
	 * 상품별 판매 상세 엑셀 다운로드
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/sales/area/detail/excel-download")
	public ModelAndView salesAreaDetailExcelDownload(StatisticsParam statisticsParam) {
		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new AreaDetailExcelView(statisticsParam.getDodobuhyun()));
		mav.addObject("list", shopStatisticsService.getAreaDetailList(statisticsParam));

		return mav;
	}

	/**
	 * 관리자 팀별 그룹 관리 리스트
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/area/detail")
	@RequestProperty(title = "매출 통계", layout = "base")
	public String salesAreaDetail(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("areaList", shopStatisticsService.getAreaDetailList(statisticsParam));
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		return ViewUtils.getManagerView("/shop-statistics/sales/area-detail");
	}

	@PostMapping("/sales/area/detail")
	@RequestProperty(title = "매출 통계", layout = "base")
	public String searchSalesAreaDetail(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("areaList", shopStatisticsService.getAreaDetailList(statisticsParam));
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		return ViewUtils.getManagerView("/shop-statistics/sales/area-detail");
	}


	/**
	 * 매출 제로 상품 내역
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/no-sales")
	public String notSales(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {

		int count = shopStatisticsService.getDoNotSellItemCountByParam(statisticsParam);

		Pagination pagination = Pagination.getInstance(count, 15);
		statisticsParam.setPagination(pagination);

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("pagination", pagination);
		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("itemList", shopStatisticsService.getDoNotSellItemListByParam(statisticsParam));

		return ViewUtils.view();
	}

	@GetMapping(value = "/sales/no-sales/excel-download")
	public ModelAndView downloadExcelNoSales(StatisticsParam statisticsParam) {

		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new NoSalesStatisticsExcelView());
		mav.addObject("list", shopStatisticsService.getDoNotSellItemListByParam(statisticsParam));
		return mav;
	}

	/**
	 * 관리자 팀별 그룹 관리 리스트
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/no-user")
	public String noUser(Model model, StatisticsParam statisticsParam) {

		Pagination pagination = Pagination.getInstance(shopStatisticsService.getNotUserCount(statisticsParam), 15);
		statisticsParam.setPagination(pagination);

		model.addAttribute("pagination", pagination);
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("userList", shopStatisticsService.getNotUserList(statisticsParam));

		return ViewUtils.view();
	}

	@PostMapping("/no-user")
	public String searchNoUser(Model model, StatisticsParam statisticsParam) {
		Pagination pagination = Pagination.getInstance(shopStatisticsService.getNotUserCount(statisticsParam), 15);
		statisticsParam.setPagination(pagination);

		model.addAttribute("pagination", pagination);
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("userList", shopStatisticsService.getNotUserList(statisticsParam));

		return ViewUtils.view();
	}

	@GetMapping(value = "/no-user/excel-download")
	public ModelAndView downloadExcelNoUser(StatisticsParam statisticsParam) {

		if(!SecurityUtils.hasRole("ROLE_EXCEL")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		}

		if (statisticsParam.getStartDate() == null) {
			statisticsParam.setStartDate(DateUtils.getToday("yyyyMM") + "01");
			statisticsParam.setEndDate(DateUtils.getToday(Const.DATE_FORMAT));
		}

		Pagination pagination = Pagination.getInstance(10000, 10000);
		statisticsParam.setPagination(pagination);

		ModelAndView mav = new ModelAndView(new NoUserStatisticsExcelView());
		mav.addObject("list", shopStatisticsService.getNotUserList(statisticsParam));
		return mav;
	}


	public static List<String> itemDateSearchHeader(String startDate,
			String endDate, String type) {
		List<String> list = new ArrayList<>();

		if ("web".equals(type)) {
			list.add("<th class=\"border_left\">" + DateUtils.date(startDate)
					+ "(" + week(startDate, "0") + ") ~ "
					+ DateUtils.date(endDate) + "(" + week(endDate, "0")
					+ ") </th>");
		} else {
			list.add(DateUtils.date(startDate) + "(" + week(startDate, "0")
					+ ") ~ " + DateUtils.date(endDate) + "("
					+ week(endDate, "0") + ")");
		}

		return list;
	}

	public static List<String> itemDateDayHeader(String startDate,
			String endDate, String type) {
		List<String> list = new ArrayList<>();

		String[] days = DateUtils.getDateArray(startDate, endDate);
		for (int i = 0; i < days.length; i++) {
			if ("web".equals(type)) {
				list.add("<th class=\"border_left\">" + DateUtils.date(days[i])
						+ "(" + week(days[i], "0") + ") </th>");
			} else {
				list.add(DateUtils.date(days[i]) + " (" + week(days[i], "0")
						+ ")");
			}
		}

		return list;
	}

	public static List<String> itemDateMonthHeader(String startDate,
			String endDate, String type) {

		List<String> list = new ArrayList<>();

		String[] days = DateUtils.getDateArray(startDate, endDate);

		String prevMonth = "";
		for (int i = 0; i < days.length; i++) {
			if (i == 0 || !days[i].substring(4, 6).equals(prevMonth)) {
				if ("web".equals(type)) {
					list.add("<th class=\"border_left\">"
							+ DateUtils.dateMonth(days[i]) + "</th>");
				} else {
					list.add(DateUtils.dateMonth(days[i]));
				}
			}

			prevMonth = days[i].substring(4, 6);
		}

		return list;

	}

	public static List<String> itemDateWeekHeader(String startDate,
			String endDate, String weekType, String type) {

		String htmlStart = "";
		String htmlEnd = "";

		if ("web".equals(type)) {
			htmlStart = "<th class=\"border_left\">";
			htmlEnd = "</th>";
		}

		String weekTypePrev = "" + (Integer.parseInt(weekType) - 1);
		if (weekType.equals("1"))
			weekTypePrev = "7";

		List<String> list = new ArrayList<>();

		String[] days = DateUtils.getDateArray(startDate, endDate);

		int j = 0;
		int k = 0;
		String weeks = "";

		for (int i = 0; i < days.length; i++) {

			if ((j == 0 && k == 0)
					|| (week(days[i], "1").equals(weekType) && k > 0)) {
				weeks += htmlStart + DateUtils.date(days[i]) + "("
						+ week(days[i], "0") + ")";
			}

			if (week(days[i], "1").equals(weekTypePrev) || i == days.length - 1) {
				weeks += " ~ " + DateUtils.date(days[i]) + "("
						+ week(days[i], "0") + ")" + htmlEnd;
				list.add(weeks);
				j = -1;
				weeks = "";
				k++;
			}

			j++;
		}
		return list;

	}

	public static String week(String sDate, String type) {
		String dateStr = sDate;

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(Const.DATE_FORMAT, Locale.JAPAN);
		try {
			cal.setTime(sdf.parse(dateStr));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date format: "
					+ dateStr);
		}

		SimpleDateFormat rsdf = new SimpleDateFormat("E", Locale.JAPAN);
		String week = rsdf.format(cal.getTime());

		if (type.equals("1")) {
			if (week.equals("日")) {
				week = "1";
			} else if (week.equals("月")) {
				week = "2";
			} else if (week.equals("火")) {
				week = "3";
			} else if (week.equals("水")) {
				week = "4";
			} else if (week.equals("木")) {
				week = "5";
			} else if (week.equals("金")) {
				week = "6";
			} else if (week.equals("土")) {
				week = "7";
			}
		}

		return week;
	}

	public static List<HashMap<String, String>> getWeekArraySelect() {

		List<HashMap<String, String>> list = new ArrayList<>();

		String[] weeks = { "日", "月", "火", "水", "木", "金", "土" };

		int j = 1;

		for (String week : weeks) {

			HashMap<String, String> map = new HashMap<>();

			map.put("week", week);
			map.put("value", "" + j);

			list.add(map);

			j++;
		}

		return list;
	}

	private List<ShopItemDateStatistics> getSearchOfItemList(
			StatisticsParam statisticsParam) {

		List<ShopItemDateStatistics> itemDateList = shopStatisticsService.getItemDateListByParam(statisticsParam);

		for (ShopItemDateStatistics shopItemDateStatistics : itemDateList) {
			List<ShopItemPrice> list = new ArrayList<>();

			List<ShopDateStatistics> dateList = shopItemDateStatistics.getDateList();

			for (ShopDateStatistics shopDateStatistics : dateList) {
				ShopItemPrice itemprice = new ShopItemPrice();

				/*
				itemprice.setWebPriceTotal(Double.toString(shopDateStatistics.getWebItemPrice()));
				itemprice.setWebPayCount(Double.toString(shopDateStatistics.getWebPayCount()));
				*/
				list.add(itemprice);

				shopItemDateStatistics.setDateList2(list);
			}

		}

		return itemDateList;
	}

	private void typeOfSearch(StatisticsParam statisticsParam, ModelAndView mav) {
		String startDate = statisticsParam.getStartDate();
		String endDate = statisticsParam.getEndDate();

		List<ShopItemDateStatistics> itemDateList = getSearchOfItemList(statisticsParam);

		mav.addObject("itemDateList", itemDateList);
		mav.addObject("hedarList", itemDateSearchHeader(startDate, endDate, "excel"));

	}

	private void typeOfSearch(StatisticsParam statisticsParam, Model model) {

		String startDate = statisticsParam.getStartDate();
		String endDate = statisticsParam.getEndDate();

		List<ShopItemDateStatistics> itemDateList = getSearchOfItemList(statisticsParam);

		model.addAttribute("itemDateList", itemDateList);
		model.addAttribute("hedarList",	itemDateSearchHeader(startDate, endDate, "web"));

	}

	private List<ShopItemDateStatistics> getDayOfItemList(
			StatisticsParam statisticsParam) {

		String[] days = DateUtils.getDateArray(statisticsParam.getStartDate(),
				statisticsParam.getEndDate());
		List<ShopItemDateStatistics> itemDateList = shopStatisticsService.getItemDateListByParam(statisticsParam);

		for (ShopItemDateStatistics shopItemDateStatistics : itemDateList) {
			List<ShopItemPrice> list = new ArrayList<>();
			int price = 0;
			int quantity = 0;

			List<ShopDateStatistics> dateList = shopItemDateStatistics
					.getDateList();
			for (int i = 0; i < days.length; i++) {
				String prevDay = "";
				for (ShopDateStatistics shopDateStatistics : dateList) {

					ShopItemPrice itemprice = new ShopItemPrice();

					/*
					if (shopDateStatistics.getSearchDate().equals(days[i])) {
						price = (int) shopDateStatistics
								.getWebItemPrice();
						quantity = (int) shopDateStatistics
								.getWebPayCount();
					}
					*/

					if (!days[i].equals(prevDay)) {

						itemprice.setWebPriceTotal("" + price);
						itemprice.setWebPayCount("" + quantity);
						list.add(itemprice);

						shopItemDateStatistics.setDateList2(list);
						price = 0;
						quantity = 0;

					}

					prevDay = days[i];

				}
			}
		}

		return itemDateList;
	}

	private void typeOfDay(StatisticsParam statisticsParam, ModelAndView mav) {

		String startDate = statisticsParam.getStartDate();
		String endDate = statisticsParam.getEndDate();

		List<ShopItemDateStatistics> itemDateList = getDayOfItemList(statisticsParam);

		mav.addObject("itemDateList", itemDateList);
		mav.addObject("hedarList",
				itemDateDayHeader(startDate, endDate, "excel"));

	}

	private void typeOfDay(StatisticsParam statisticsParam, Model model) {

		String startDate = statisticsParam.getStartDate();
		String endDate = statisticsParam.getEndDate();

		List<ShopItemDateStatistics> itemDateList = getDayOfItemList(statisticsParam);

		model.addAttribute("itemDateList", itemDateList);
		model.addAttribute("hedarList",
				itemDateDayHeader(startDate, endDate, "web"));
	}

	private List<ShopItemDateStatistics> getMonthOfItemList(
			StatisticsParam statisticsParam) {

		List<String> monthList = itemDateMonthHeader(
				statisticsParam.getStartDate(), statisticsParam.getEndDate(),
				"web");
		List<ShopItemDateStatistics> itemDateList = shopStatisticsService.getItemDateListByParam(statisticsParam);

		for (ShopItemDateStatistics shopItemDateStatistics : itemDateList) {
			List<ShopItemPrice> list = new ArrayList<>();

			int price = 0;
			int quantity = 0;

			List<ShopDateStatistics> dateList = shopItemDateStatistics
					.getDateList();
			for (String month : monthList) {
				String prevMonth = "";
				for (ShopDateStatistics shopDateStatistics : dateList) {

					ShopItemPrice itemprice = new ShopItemPrice();

					/*
					if (month.replaceAll("-", "")
							.replaceAll("<th class=\"border_left\">", "")
							.replaceAll("</th>", "")
							.equals(shopDateStatistics.getSearchDate())) {
						price = (int) shopDateStatistics
								.getWebItemPrice();
						quantity = (int) shopDateStatistics
								.getWebPayCount();
					}
					*/

					if (!month.equals(prevMonth)) {
						itemprice.setWebPriceTotal("" + price);
						itemprice.setWebPayCount("" + quantity);
						list.add(itemprice);

						shopItemDateStatistics.setDateList2(list);
						price = 0;
						quantity = 0;
					}

					prevMonth = month;

				}

			}

		}

		return itemDateList;

	}

	private void typeOfMonth(StatisticsParam statisticsParam, ModelAndView mav) {

		List<ShopItemDateStatistics> itemDateList = getMonthOfItemList(statisticsParam);

		mav.addObject("itemDateList", itemDateList);
		mav.addObject("hedarList", itemDateMonthHeader(statisticsParam.getStartDate(), statisticsParam.getEndDate(), "excel"));

	}

	private void typeOfMonth(StatisticsParam statisticsParam, Model model) {

		List<ShopItemDateStatistics> itemDateList = getMonthOfItemList(statisticsParam);

		model.addAttribute("itemDateList", itemDateList);
		model.addAttribute("hedarList", itemDateMonthHeader(statisticsParam.getStartDate(), statisticsParam.getEndDate(), "web"));
	}

	private List<ShopItemDateStatistics> getWeekOfItemList(
			StatisticsParam statisticsParam) {

		String weekType = statisticsParam.getWeekType();
		String weekTypePrev = ""
				+ (Integer.parseInt(statisticsParam.getWeekType()) - 1);
		if (weekType.equals("1"))
			weekTypePrev = "7";

		List<ShopItemDateStatistics> itemDateList = shopStatisticsService.getItemDateListByParam(statisticsParam);

		for (ShopItemDateStatistics shopItemDateStatistics : itemDateList) {

			List<ShopItemPrice> list = new ArrayList<>();

			String[] days = DateUtils.getDateArray(statisticsParam.getStartDate(),statisticsParam.getEndDate());

			int j = 0;
			int price = 0;
			int quantity = 0;

			for (int i = 0; i < days.length; i++) {

				List<ShopDateStatistics> dateList = shopItemDateStatistics.getDateList();

				/*
				for (ShopDateStatistics shopDateStatistics : dateList) {
					if (days[i].equals(shopDateStatistics.getSearchDate())) {
						price = price+ (int) shopDateStatistics.getWebItemPrice();
						quantity = quantity	+ (int) shopDateStatistics.getWebPayCount();
					}
				}
*/
				if (week(days[i], "1").equals(weekTypePrev)
						|| i == days.length - 1) {
					ShopItemPrice itemprice = new ShopItemPrice();
					itemprice.setWebPriceTotal("" + price);
					itemprice.setWebPayCount("" + quantity);
					list.add(itemprice);
					shopItemDateStatistics.setDateList2(list);
					j = -1;
					price = 0;
					quantity = 0;
				}

				j++;
			}

		}

		return itemDateList;

	}

	private void typeOfWeek(StatisticsParam statisticsParam, ModelAndView mav) {

		List<ShopItemDateStatistics> itemDateList = getWeekOfItemList(statisticsParam);

		mav.addObject("itemDateList", itemDateList);
		mav.addObject("hedarList", itemDateWeekHeader(statisticsParam.getStartDate(), statisticsParam.getEndDate(), statisticsParam.getWeekType(), "excel"));

	}

	private void typeOfWeek(StatisticsParam statisticsParam, Model model) {

		List<ShopItemDateStatistics> itemDateList = getWeekOfItemList(statisticsParam);

		model.addAttribute("itemDateList", itemDateList);
		model.addAttribute("hedarList",	itemDateWeekHeader(statisticsParam.getStartDate(), statisticsParam.getEndDate(), statisticsParam.getWeekType(), "web"));

	}

	/**
	 * 통계 > 답례품 구매현황 > 월별 지자체별 답례품 현황 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/month-locgov")
	public String monthLocgovStat(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}
		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setSearchYear(DateUtils.getToday("yyyy"));
			statisticsParam.setSearchMonth(DateUtils.getToday("MM"));
		}

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		List<MonthLocgovItemStat> list = Collections.EMPTY_LIST;

		if (list != null && !list.isEmpty()) {
			StringBuffer allTop1 = new StringBuffer();
			StringBuffer allTop2 = new StringBuffer();
			StringBuffer allTop3 = new StringBuffer();

			int idx = 0;

			for (MonthLocgovItemStat monthLocgovItemStat : list) {
				allTop1.append(monthLocgovItemStat.getAllTop1());
				allTop2.append(monthLocgovItemStat.getAllTop2());
				allTop3.append(monthLocgovItemStat.getAllTop3());

				if(idx == 0) {
					monthLocgovItemStat.setAllTop1(allTop1.toString());
					monthLocgovItemStat.setAllTop2(allTop2.toString());
					monthLocgovItemStat.setAllTop3(allTop3.toString());
				}
				idx ++;
			}

			MonthLocgovItemStat firstMonthLocgovItemStat = list.get(0);

			firstMonthLocgovItemStat.setAllTop1(allTop1.toString());
			firstMonthLocgovItemStat.setAllTop2(allTop2.toString());
			firstMonthLocgovItemStat.setAllTop3(allTop3.toString());
		}

		model.addAttribute("list", list);

		return ViewUtils.view();
	}

	@PostMapping("/sales/month-locgov")
	public String searchMonthLocgovStat(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}
		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setSearchYear(DateUtils.getToday("yyyy"));
			statisticsParam.setSearchMonth(DateUtils.getToday("MM"));
		}

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		List<MonthLocgovItemStat> list = shopStatisticsService.getMonthLocgovItemStat(statisticsParam);

		if (list != null && !list.isEmpty()) {
			StringBuffer allTop1 = new StringBuffer();
			StringBuffer allTop2 = new StringBuffer();
			StringBuffer allTop3 = new StringBuffer();

			int idx = 0;

			for (MonthLocgovItemStat monthLocgovItemStat : list) {
				allTop1.append(monthLocgovItemStat.getAllTop1());
				allTop2.append(monthLocgovItemStat.getAllTop2());
				allTop3.append(monthLocgovItemStat.getAllTop3());

				if(idx == 0) {
					monthLocgovItemStat.setAllTop1(allTop1.toString());
					monthLocgovItemStat.setAllTop2(allTop2.toString());
					monthLocgovItemStat.setAllTop3(allTop3.toString());
				}
				idx ++;
			}

			MonthLocgovItemStat firstMonthLocgovItemStat = list.get(0);

			firstMonthLocgovItemStat.setAllTop1(allTop1.toString());
			firstMonthLocgovItemStat.setAllTop2(allTop2.toString());
			firstMonthLocgovItemStat.setAllTop3(allTop3.toString());
		}

		model.addAttribute("list", list);
		return ViewUtils.view();
	}

	/**
	 * 통계 > 답례품 구매현황 > 월별 지자체별 답례품 현황 엑셀 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/month-locgov/excel-download")
	public ModelAndView monthLocgovStatExcelDownload(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		if (!SecurityUtils.isManager()) {
			return new ModelAndView(ViewUtils.redirect("/", "권한이 없습니다."));
		}

		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setSearchYear(DateUtils.getToday("yyyy"));
			statisticsParam.setSearchMonth(DateUtils.getToday("MM"));
		}

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		List<MonthLocgovItemStat> list = shopStatisticsService.getMonthLocgovItemStat(statisticsParam);

		if (list != null && !list.isEmpty()) {
			StringBuffer allTop1 = new StringBuffer();
			StringBuffer allTop2 = new StringBuffer();
			StringBuffer allTop3 = new StringBuffer();

			int idx = 0;

			for (MonthLocgovItemStat monthLocgovItemStat : list) {
				allTop1.append(monthLocgovItemStat.getAllTop1());
				allTop2.append(monthLocgovItemStat.getAllTop2());
				allTop3.append(monthLocgovItemStat.getAllTop3());

				if(idx == 0) {
					monthLocgovItemStat.setAllTop1(allTop1.toString());
					monthLocgovItemStat.setAllTop2(allTop2.toString());
					monthLocgovItemStat.setAllTop3(allTop3.toString());
				}
				idx ++;
			}

			MonthLocgovItemStat firstMonthLocgovItemStat = list.get(0);

			firstMonthLocgovItemStat.setAllTop1(allTop1.toString());
			firstMonthLocgovItemStat.setAllTop2(allTop2.toString());
			firstMonthLocgovItemStat.setAllTop3(allTop3.toString());
		}

		ModelAndView mav = new ModelAndView(new MonthLocgovStatExcel("월별_지자체별_답례품_현황"));

		mav.addObject("monthLocgovStatExcelList", list);
		mav.addObject("title", "월별 지자체별 답례품 현황");

		return mav;
	}

	/**
	 * 통계 > 답례품 구매현황 > 전체 (고향사랑e음) 주차트
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/dashboard/order/week")
	public JsonView dashboardOrderWeek(StatisticsParam statisticsParam) {
		if (UserUtils.hasLocgovManagerRole() || UserUtils.hasMasterManagerRole()) {
			try {
				LocalDate.parse(statisticsParam.getSearchDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
			} catch (NullPointerException | DateTimeParseException e) {
				statisticsParam.setSearchDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			}

			return JsonViewUtils.success(shopStatisticsService.getOrderCountAmtForWeek(statisticsParam));
		} else {
			throw new OpRuntimeException("권한이 없습니다.");
		}
	}

	/**
	 * 통계 > 답례품 구매현황 > 전체 (고향사랑e음) 월차트
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@PostMapping("dashboard/month/{startYear}")
	public JsonView dashboardOrderMonth(StatisticsParam statisticsParam, @PathVariable String startYear) {
//		if (UserUtils.hasLocgovManagerRole() || UserUtils.hasMasterManagerRole()) {
			try {
				statisticsParam.setStartYear(startYear);
			} catch (NullPointerException | DateTimeParseException e) {
				statisticsParam.setStartYear(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy")));
			}
			return JsonViewUtils.success(shopStatisticsService.getOrderCountAmtForMonth(statisticsParam));
//		} else {
//			throw new OpRuntimeException("권한이 없습니다.");
//		}
	}

	/**
	 * 통계 > 답례품 구매현황 > 일일 현황 보고 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/dashboard/day")
	public String dayStat(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}
		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setSearchYear(DateUtils.getToday("yyyy"));
			statisticsParam.setSearchMonth(DateUtils.getToday("MM"));
		}

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		return ViewUtils.view();
	}

	@PostMapping("/dashboard/day")
	public String SearchDayStat(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}
		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setSearchYear(DateUtils.getToday("yyyy"));
			statisticsParam.setSearchMonth(DateUtils.getToday("MM"));
		}

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		return ViewUtils.view();
	}

	/**
	 * 통계 > 답례품 구매현황 > 월별 현황 보고 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/dashboard/month")
	public String monthStat(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setStartYear(DateUtils.getToday("yyyy"));
			statisticsParam.setStartMonth(DateUtils.getToday("MM"));
		}

		model.addAttribute("lastYear", DateUtils.getToday("yyyy"));
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("statisticsMonthInfo", shopStatisticsService.getStatisticsMonthInfo(statisticsParam));

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		return ViewUtils.view();
	}

	@PostMapping("/dashboard/month")
	public String searchMonthStat(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setStartYear(DateUtils.getToday("yyyy"));
			statisticsParam.setStartMonth(DateUtils.getToday("MM"));
		}

		model.addAttribute("lastYear", DateUtils.getToday("yyyy"));
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("statisticsMonthInfo", shopStatisticsService.getStatisticsMonthInfo(statisticsParam));

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		return ViewUtils.view();
	}


	private ModelAndView excelDownloadRedirect(boolean isManagerPage, String url, String msg) {
		return new ModelAndView(ViewUtils.redirect(makeUrl(isManagerPage, url), msg));
	}

	private String makeUrl(boolean isManagerPage, String url) {
		if (isManagerPage) {
			return "/opmanager" + url;
		} else {
			return "/seller" + url;
		}
	}

	/**
	 * 정산 예정 내역 엑셀
	 * @return
	 */
	@GetMapping("dashboard/day/list-excel")
	public ModelAndView expectedListExcel(StatisticsParam statisticsParam, Model model, RequestContext requestContext) {
		String queryString = "";
		if (requestContext.getQueryString() != null) {
			queryString = "?" + requestContext.getQueryString();
		}
		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/dashboard/day/list" + queryString, "엑셀 다운로드 권한이 없습니다.");
		}

		if (SecurityUtils.hasRole("ROLE_ADMIN_5")
				|| SecurityUtils.hasRole("ROLE_ADMIN_6")
				|| SecurityUtils.hasRole("ROLE_ADMIN_7")
				|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
			if (!StringUtils.isEmpty(locgovCode)) {
				statisticsParam.setLocgovCode(locgovCode);
			} else {
				statisticsParam.setLocgovCode("00000");
			}
		} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
					|| SecurityUtils.hasRole("ROLE_ADMIN_2")
					|| SecurityUtils.hasRole("ROLE_ADMIN_3")
					|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

		} else {
			statisticsParam.setLocgovCode("00000");
		}
		LocalDate nowDate = LocalDate.now();

		String startDateStr = statisticsParam.getStartDate();

		LocalDate startDate = LocalDate.parse(startDateStr.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));

		if (nowDate.getYear() < startDate.getYear()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/dashboard/day/list" + queryString, "이번달까지 조회 가능합니다.");
		} else if (nowDate.getYear() == startDate.getYear() && nowDate.getMonthValue() < startDate.getMonthValue()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/dashboard/day/list" + queryString, "이번달까지 조회 가능합니다.");
		}

		statisticsParam.setPage(1);
		statisticsParam.setItemsPerPage(Integer.MAX_VALUE);
		List<OrderCountAmtStat> list = shopStatisticsService.getOrderCountAmtForWeek(statisticsParam);

		if (list == null || list.isEmpty()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/dashboard/day/list" + queryString, "조회된 내역이 없습니다.");
		}

		ModelAndView mav = new ModelAndView(new WeekExcelView());
		mav.addObject("title", "정산 예정내역 목록");
		mav.addObject("list", list);
		mav.addObject("date", requestContext.getQueryString());
		return mav;
	}

	/**
	 * 통계 > 보고서 > 총괄 현황
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/report/general")
	public String generalStats(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		String tabId = statisticsParam.getTabId();
		statisticsParam.setTabId(tabId);
		List<StatisticsReport> crtrYr;

		if("1".equals(tabId)) {
			crtrYr = shopStatisticsService.getCrtrYearForMbrList();
		} else if("2".equals(tabId) || "3".equals(tabId) || "5".equals(tabId)) {
			crtrYr = shopStatisticsService.getCrtrYearForDntnList();
		} else {
			crtrYr = shopStatisticsService.getCrtrYearForGdsList();
		}

		LocalDate now = LocalDate.now();

		if(statisticsParam.getShMonth() < 1) {
			LocalDate lastMonth = now.minusMonths(1);
			statisticsParam.setShMonth(lastMonth.getMonthValue());
			statisticsParam.setShYear(""+lastMonth.getYear());
		}

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("crtrYear",crtrYr);
		model.addAttribute("nowYear", String.valueOf(now.getYear()));
		model.addAttribute("nowMonth",String.valueOf(now.getMonthValue()));
		model.addAttribute("tabId", tabId);
		model.addAttribute("mbrTnocsStats", Collections.EMPTY_LIST);
		model.addAttribute("dntnTnocsStats", Collections.EMPTY_LIST);
		model.addAttribute("dntnTnocs500Stats", Collections.EMPTY_LIST);
		model.addAttribute("gdsTnocsStats", Collections.EMPTY_LIST);
		model.addAttribute("dntnTnocsMaxAmtStats", Collections.EMPTY_LIST);

		return ViewUtils.view();
	}

	@PostMapping("/report/general")
	public String searchGeneralStats(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		String tabId = statisticsParam.getTabId();
		statisticsParam.setTabId(tabId);
		List<StatisticsReport> crtrYr;

		if("1".equals(tabId)) {
			crtrYr = shopStatisticsService.getCrtrYearForMbrList();
		} else if("2".equals(tabId) || "3".equals(tabId)) {
			crtrYr = shopStatisticsService.getCrtrYearForDntnList();
		} else {
			crtrYr = shopStatisticsService.getCrtrYearForGdsList();
		}

		LocalDate now = LocalDate.now();

		if(statisticsParam.getShMonth() < 1) {
			LocalDate lastMonth = now.minusMonths(1);
			statisticsParam.setShMonth(lastMonth.getMonthValue());
			statisticsParam.setShYear(""+lastMonth.getYear());
		}

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("crtrYear",crtrYr);
		model.addAttribute("nowYear", String.valueOf(now.getYear()));
		model.addAttribute("nowMonth",String.valueOf(now.getMonthValue()));
		model.addAttribute("tabId", tabId);
		model.addAttribute("mbrTnocsStats", shopStatisticsService.getListMbrTnocsStats(statisticsParam));
		model.addAttribute("dntnTnocsStats", shopStatisticsService.getListDntnTnocsStats(statisticsParam));
//		model.addAttribute("dntnTnocs500Stats", shopStatisticsService.getListDntnTnocs500Stats(statisticsParam));
		model.addAttribute("dntnTnocsMaxAmtStats", shopStatisticsService.getListDntnTnocsMaxAmtStats(statisticsParam));
		model.addAttribute("gdsTnocsStats", shopStatisticsService.getListGdsTnocsStats(statisticsParam));

		return ViewUtils.view();
	}

	/**
	 * 통계 > 보고서 > 총괄 누계 현황
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/report/generalTotal")
	public String generalTotalStats(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		LocalDate now = LocalDate.now();

		String tabId = statisticsParam.getTabId();
		statisticsParam.setTabId(tabId);

		if(statisticsParam.getShMonth() < 1) {
			LocalDate lastMonth = now.minusMonths(1);
			statisticsParam.setShMonth(lastMonth.getMonthValue());
			statisticsParam.setShYear(""+lastMonth.getYear());
		}

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("nowYear", String.valueOf(now.getYear()));
		model.addAttribute("nowMonth",String.valueOf(now.getMonthValue()));
		model.addAttribute("crtrYear", shopStatisticsService.getCrtrYearForDntnList());
		model.addAttribute("tabId", tabId);
		model.addAttribute("dntnPathTnocsStats", Collections.EMPTY_LIST);
		model.addAttribute("dntnAmtTnocsStats", Collections.EMPTY_LIST);
		model.addAttribute("dntnAgeTnocsStats", Collections.EMPTY_LIST);
		model.addAttribute("habDntnMctpvTnocsStats", Collections.EMPTY_LIST);

		return ViewUtils.view();
	}

	@PostMapping("/report/generalTotal")
	public String searchGeneralTotalStats(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		LocalDate now = LocalDate.now();

		String tabId = statisticsParam.getTabId();
		statisticsParam.setTabId(tabId);

		if(statisticsParam.getShMonth() < 1) {
			LocalDate lastMonth = now.minusMonths(1);
			statisticsParam.setShMonth(lastMonth.getMonthValue());
			statisticsParam.setShYear(""+lastMonth.getYear());
		}

		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("nowYear", String.valueOf(now.getYear()));
		model.addAttribute("nowMonth",String.valueOf(now.getMonthValue()));
		model.addAttribute("crtrYear", shopStatisticsService.getCrtrYearForDntnList());
		model.addAttribute("tabId", tabId);
		model.addAttribute("dntnPathTnocsStats", shopStatisticsService.getListDntnPathTnocsStats(statisticsParam));
		model.addAttribute("dntnAmtTnocsStats", shopStatisticsService.getListDntnAmtTnocsStats(statisticsParam));
		model.addAttribute("dntnAgeTnocsStats", shopStatisticsService.getListDntnAgeTnocsStats(statisticsParam));
		model.addAttribute("habDntnMctpvTnocsStats", shopStatisticsService.getListHabDntnMctpvTnocsStats(statisticsParam));

		return ViewUtils.view();
	}

	/**
	 * 통계 > 보고서 > 지자체별 현황
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/report/mctpv")
	public String mctpvStats(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		LocalDate now = LocalDate.now();

		if(statisticsParam.getShMonth() < 1) {
			LocalDate lastMonth = now.minusMonths(1);
			statisticsParam.setShMonth(lastMonth.getMonthValue());
			statisticsParam.setShYear(""+lastMonth.getYear());
		}

		String tabId = statisticsParam.getTabId();
		statisticsParam.setTabId(tabId);

		model.addAttribute("mctpv", CodeUtils.getCodeList("WDR")); // 지자체코드
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("nowYear", String.valueOf(now.getYear()));
		model.addAttribute("nowMonth",String.valueOf(now.getMonthValue()));
		model.addAttribute("tabId", tabId);
		model.addAttribute("crtrYear", shopStatisticsService.getCrtrYearForDntnList());
		model.addAttribute("mctpvDntnStats", Collections.EMPTY_LIST);
//		model.addAttribute("mctpvDntn500Stats", Collections.EMPTY_LIST);
		model.addAttribute("mctpvGdsStats", Collections.EMPTY_LIST);
		model.addAttribute("mctpvDntnMaxAmtStats", Collections.EMPTY_LIST);

		return ViewUtils.view();
	}

	@PostMapping("/report/mctpv")
	public String searchMctpvStats(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		LocalDate now = LocalDate.now();

		if(statisticsParam.getShMonth() < 1) {
			LocalDate lastMonth = now.minusMonths(1);
			statisticsParam.setShMonth(lastMonth.getMonthValue());
			statisticsParam.setShYear(""+lastMonth.getYear());
		}

		String tabId = statisticsParam.getTabId();
		statisticsParam.setTabId(tabId);

		model.addAttribute("mctpv", CodeUtils.getCodeList("WDR")); // 지자체코드
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("nowYear", String.valueOf(now.getYear()));
		model.addAttribute("nowMonth",String.valueOf(now.getMonthValue()));
		model.addAttribute("tabId", tabId);
		model.addAttribute("crtrYear", shopStatisticsService.getCrtrYearForDntnList());
		model.addAttribute("mctpvDntnStats", shopStatisticsService.getListMctpvDntnStats(statisticsParam));
//		model.addAttribute("mctpvDntn500Stats", shopStatisticsService.getListMctpvDntn500Stats(statisticsParam));
		model.addAttribute("mctpvGdsStats", shopStatisticsService.getListMctpvGdsStats(statisticsParam));
		model.addAttribute("mctpvDntnMaxAmtStats", shopStatisticsService.getListMctpvDntnMaxAmtStats(statisticsParam));

		return ViewUtils.view();
	}

	/**
	 * 통계 > 보고서 > 지자체(243개) 현황
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/report/lclgv")
	public String lclgvStats(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		String tabId = statisticsParam.getTabId();
		statisticsParam.setTabId(tabId);
		List<StatisticsReport> mctpv;

		if("3".equals(tabId)) {
			mctpv = shopStatisticsService.getMctpvForGdsList();
		} else {
			mctpv = shopStatisticsService.getMctpvForDntnList();
		}

		LocalDate now = LocalDate.now();

		if(statisticsParam.getShMonth() < 1) {
			LocalDate lastMonth = now.minusMonths(1);
			statisticsParam.setShMonth(lastMonth.getMonthValue());
			statisticsParam.setShYear(""+lastMonth.getYear());
		}

		String lclgvCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER); // 지자체코드 세팅
		String mctpvCd = locgovService.getUpperLocgovCode(lclgvCd);

		if(SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			if(lclgvCd.length() > 0) {
				if(lclgvCd.equals(mctpvCd)) {
					statisticsParam.setShMctpv(mctpvCd);
				} else {
					statisticsParam.setShMctpv(mctpvCd);
					statisticsParam.setShLclgv(lclgvCd);
				}
			}
		}

		model.addAttribute("mctpvCd", mctpvCd);
		model.addAttribute("lclgvCd", lclgvCd);
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("nowYear", String.valueOf(now.getYear()));
		model.addAttribute("nowMonth",String.valueOf(now.getMonthValue()));
		model.addAttribute("tabId", tabId);
		model.addAttribute("crtrYear", shopStatisticsService.getCrtrYearForDntnList());
		model.addAttribute("mctpv", mctpv);
		model.addAttribute("lclgv", shopStatisticsService.getLclgvForGdsList(mctpvCd));
		model.addAttribute("lclgvAodStats", Collections.EMPTY_LIST);
//		model.addAttribute("lclgvAod500Stats", Collections.EMPTY_LIST);
		model.addAttribute("lclgvGdsStats", Collections.EMPTY_LIST);
		model.addAttribute("lclgvAodMaxAmtStats", Collections.EMPTY_LIST);

		return ViewUtils.view();
	}

	@PostMapping("/report/lclgv")
	public String searchLclgvStats(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}

		String tabId = statisticsParam.getTabId();
		statisticsParam.setTabId(tabId);
		List<StatisticsReport> mctpv;

		if("3".equals(tabId)) {
			mctpv = shopStatisticsService.getMctpvForGdsList();
		} else {
			mctpv = shopStatisticsService.getMctpvForDntnList();
		}

		LocalDate now = LocalDate.now();

		if(statisticsParam.getShMonth() < 1) {
			LocalDate lastMonth = now.minusMonths(1);
			statisticsParam.setShMonth(lastMonth.getMonthValue());
			statisticsParam.setShYear(""+lastMonth.getYear());
		}

		String lclgvCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER); // 지자체코드 세팅
		String mctpvCd = locgovService.getUpperLocgovCode(lclgvCd);

		if(SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			if(lclgvCd.length() > 0) {
				if(lclgvCd.equals(mctpvCd)) {
					statisticsParam.setShMctpv(mctpvCd);
				} else {
					statisticsParam.setShMctpv(mctpvCd);
					statisticsParam.setShLclgv(lclgvCd);
				}
			}
		}

		model.addAttribute("mctpvCd", mctpvCd);
		model.addAttribute("lclgvCd", lclgvCd);
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("nowYear", String.valueOf(now.getYear()));
		model.addAttribute("nowMonth",String.valueOf(now.getMonthValue()));
		model.addAttribute("tabId", tabId);
		model.addAttribute("crtrYear", shopStatisticsService.getCrtrYearForDntnList());
		model.addAttribute("mctpv", mctpv);
		model.addAttribute("lclgv", shopStatisticsService.getLclgvForGdsList(mctpvCd));
		model.addAttribute("lclgvAodStats", shopStatisticsService.getListLclgvAodStats(statisticsParam));
//		model.addAttribute("lclgvAod500Stats", shopStatisticsService.getListLclgvAod500Stats(statisticsParam));
		model.addAttribute("lclgvGdsStats", shopStatisticsService.getListLclgvGdsStats(statisticsParam));
		model.addAttribute("lclgvAodMaxAmtStats", shopStatisticsService.getListLclgvAodMaxAmtStats(statisticsParam));

		return ViewUtils.view();
	}

	/**
	 * 통계 > 보고서 > 연간통계 현황
	 * @param model
	 * @param statisticsParam
	 * @return
	 */
	@GetMapping("/report/year")
	public String yearStats(Model model, StatisticsParam statisticsParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}
		LocalDate date = LocalDate.now();//이번년도는 뺌
		List<Code> codeList = CodeUtils.getCodeList("YYYY");
		List<Code> rstCdList = new ArrayList<>();
		for(Code cd : codeList) {
			if(Integer.parseInt(cd.getId()) < date.getYear()) {//이번년도는 뺌
				if(!cd.getId().equals("2022") && !cd.getId().equals("2023") && !cd.getId().equals("")) {
					rstCdList.add(cd);
				}
			}
		}
		// 코드
		model.addAttribute("yyyy", rstCdList);	// 년도
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		String tabId = statisticsParam.getTabId();
		statisticsParam.setTabId(tabId);
		List<StatisticsReport> mctpv;

		if("3".equals(tabId)) {
			mctpv = shopStatisticsService.getMctpvForGdsList();
		} else {
			mctpv = shopStatisticsService.getMctpvForDntnList();
		}

		String lclgvCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER); // 지자체코드 세팅
		String mctpvCd = locgovService.getUpperLocgovCode(lclgvCd);

		if(SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			if(lclgvCd.length() > 0) {
				if(lclgvCd.equals(mctpvCd)) {
					statisticsParam.setShMctpv(mctpvCd);
				} else {
					statisticsParam.setShMctpv(mctpvCd);
					statisticsParam.setShLclgv(lclgvCd);
				}
			}
		}
		model.addAttribute("mctpvCd", mctpvCd);
		model.addAttribute("lclgvCd", lclgvCd);
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("tabId", tabId);
		model.addAttribute("mctpv", mctpv);
		model.addAttribute("crtrYear", shopStatisticsService.getCrtrYearForDntnList());
		model.addAttribute("lclgv", shopStatisticsService.getLclgvForGdsList(mctpvCd));

		return ViewUtils.view();
	}




	@PostMapping("/report/lclgv/options-by-lclgv")
	public @ResponseBody List<StatisticsReport> optionslclgvByLclgv(@RequestParam(name="mctpv", defaultValue = "") String mctpv,
			@RequestParam(name="tabId", defaultValue = "1") String tabId, Model model) {
		if("3".equals(tabId)) {
			return shopStatisticsService.getLclgvForGdsList(mctpv);
		}
		else {
			return shopStatisticsService.getLclgvForDntnList(mctpv);
		}

	}

	/**
	 * 엑셀 다운로드
	 * @return
	 */
	@GetMapping(value="/report/lclgv/excel-download")
	public ModelAndView downloadExcelProcessByList(StatisticsParam statisticsParam) {
		ModelAndView mav = new ModelAndView(new ShopStatisticsReportExcelView());

		String lclgvCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER); // 지자체코드 세팅
		String mctpvCd = locgovService.getUpperLocgovCode(lclgvCd);

		if(SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			if(lclgvCd.length() > 0) {
				if(lclgvCd.equals(mctpvCd)) {
					statisticsParam.setShMctpv(mctpvCd);
				} else {
					statisticsParam.setShMctpv(mctpvCd);
					statisticsParam.setShLclgv(lclgvCd);
				}
			}
		} else {
			mav.addObject("mbrTnocsStats", shopStatisticsService.getListMbrTnocsStats(statisticsParam));
			mav.addObject("dntnTnocsStats", shopStatisticsService.getListDntnTnocsStats(statisticsParam));
//			mav.addObject("dntnTnocs500Stats", shopStatisticsService.getListDntnTnocs500Stats(statisticsParam));
			mav.addObject("dntnTnocsMaxAmtStats", shopStatisticsService.getListDntnTnocsMaxAmtStats(statisticsParam));//2000 max amt
			mav.addObject("gdsTnocsStats", shopStatisticsService.getListGdsTnocsStats(statisticsParam));
			mav.addObject("dntnPathTnocsStats", shopStatisticsService.getListDntnPathTnocsStats(statisticsParam));
			mav.addObject("dntnAmtTnocsStats", shopStatisticsService.getListDntnAmtTnocsStats(statisticsParam));
			mav.addObject("dntnAgeTnocsStats", shopStatisticsService.getListDntnAgeTnocsStats(statisticsParam));
			mav.addObject("habDntnMctpvTnocsStats", shopStatisticsService.getListHabDntnMctpvTnocsStats(statisticsParam));
			mav.addObject("mctpvDntnStats", shopStatisticsService.getListMctpvDntnStats(statisticsParam));
//			mav.addObject("mctpvDntn500Stats", shopStatisticsService.getListMctpvDntn500Stats(statisticsParam));
			mav.addObject("mctpvDntnMaxAmtStats", shopStatisticsService.getListMctpvDntnMaxAmtStats(statisticsParam));//2000 max amt
			mav.addObject("mctpvGdsStats", shopStatisticsService.getListMctpvGdsStats(statisticsParam));
		}
		mav.addObject("statisticsParam", statisticsParam);
		mav.addObject("lclgvAodStats", shopStatisticsService.getListLclgvAodStats(statisticsParam));
//		mav.addObject("lclgvAod500Stats", shopStatisticsService.getListLclgvAod500Stats(statisticsParam));
		mav.addObject("lclgvAodMaxAmtStats", shopStatisticsService.getListLclgvAodMaxAmtStats(statisticsParam));//2000 max amt
		mav.addObject("lclgvGdsStats", shopStatisticsService.getListLclgvGdsStats(statisticsParam));

		return mav;
	}

	/**
	 * 엑셀 연통계 다운로드 - 현재상태 테이블에서 집계한 데이터 다운로드 (나중에 사용안할수 있음)
	 * @return
	 */
	@GetMapping(value="/report/lclgv/excel-pastYearStats")
	public ModelAndView downloadExcelPastYearProcessByList(StatisticsParam statisticsParam) {
		ModelAndView mav = new ModelAndView(new ShopStatisticsPastYearExcelView());

//		mav.addObject("list", shopStatisticsService.getDateStatsListGhlove(statisticsParam));
		mav.addObject("statisticsParam", statisticsParam);
		mav.addObject("dntnTnocsPastYearStats", shopStatisticsService.getListDntnTnocsPastYearStats(statisticsParam));//1-1.기부현황: 총괄 기부 건수
		mav.addObject("dntnMctpvPastYearStats", shopStatisticsService.getListMctpvDntnPastYearStats(statisticsParam));//1-2.기부현황: 총괄 기부 금액
		mav.addObject("dntnDntnPathPastYearStats", shopStatisticsService.getListDntnPathPastYearStats(statisticsParam));//2.기부현황: 기부 방법별
		mav.addObject("dntnDntnAmtPastYearStats", shopStatisticsService.getListDntnAmtPastYearStats(statisticsParam));//3.기부현황: 기부 금액별
		mav.addObject("dntnAgeDntnAmtPastYearStats", shopStatisticsService.getListAgeDntnAmtPastYearStats(statisticsParam));//4.기부현황: 기부 연령별

		//5.와 6-1은 기존데이터로 사용함
		mav.addObject("dntnMonthMctpvUniqIdPastYearStats", shopStatisticsService.getListMctpvUniqIdPastYearStats(statisticsParam));//6-2.기부현황: 월별 금액, 고유아이디(쿼리 새로만듬-> 금액 고유아이디 데이터)

		mav.addObject("dntnPubGoodsPastYearStats", shopStatisticsService.getListPubGoodsPastYearStats(statisticsParam));//7.답례품 현황: 인기답례품현황(판매량순 상위 30개)


		return mav;
	}

	/**
	 * 엑셀 연통계 다운로드 - 2월 28일 집계완료된 통계테이블에서 다운로드
	 * @return
	 */
	@GetMapping(value="/report/lclgv/excel-pastYearStatsStored")
	public ModelAndView downloadExcelPastYearTableProcessByList(StatisticsParam statisticsParam) {
		ModelAndView mav = new ModelAndView(new ShopStatisticsStoredPastYearExcelView());

		String lclgvCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER); // 지자체코드 세팅
		String mctpvCd = locgovService.getUpperLocgovCode(lclgvCd);

		if(SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			if(lclgvCd.length() > 0) {
				statisticsParam.setShWdr(mctpvCd);
				statisticsParam.setShLocgovCode(lclgvCd);
			}
		}

		mav.addObject("statisticsParam", statisticsParam);
		mav.addObject("mbrTnocsStatsStored", shopStatisticsService.getListMbrTnocsStatsStored(statisticsParam));//총괄
		mav.addObject("dntnPathPastYearStats", shopStatisticsService.getListDntnPathPastYearStatsStored(statisticsParam));//기부 방법별
		mav.addObject("dntnPricePastYearStats", shopStatisticsService.getListDntnPricePastYearStatsStored(statisticsParam));//기부 금액별
		mav.addObject("dntnAgesPastYearStats", shopStatisticsService.getListDntnAgesPastYearStatsStored(statisticsParam));//기부 금액별
		mav.addObject("dntnPsintPastYearStats", shopStatisticsService.getListDntnPsintPastYearStatsStored(statisticsParam));//기부 기부자 주소지별_광역시 별
		mav.addObject("dntnLocGovPastYearStats", shopStatisticsService.getListDntnLocGovPastYearStatsStored(statisticsParam));//기부 기부자 주소지별_지역 별

		mav.addObject("dntnMonthPastYearStats", shopStatisticsService.getListDntnMonthPastYearStatsStored(statisticsParam));//기부 월별

		mav.addObject("dntnGoodsPastYearStats", shopStatisticsService.getListDntnGoodsPastYearStatsStored(statisticsParam));//답례품 인기순


		return mav;
	}

	/**
	 * 연령배치 적재
	 * @return
	 */
	@GetMapping(value="/report/general/birthdayBatch")
	public String birthdayBatch(){
		try {
			userService.setOpUserBirthdayStat();
		} catch (RuntimeException e) {
			log.error("[setOpUserBirthdayStat] [배치] : [{}]", e);
		}

		return ("redirect:/opmanager/shop-statistics/report/general");
	}

	/**
	 * 통계배치 적재
	 * @return
	 */
	@GetMapping(value="/report/general/statisticsBatch")
	public String statisticsBatch(){
		try {
			shopStatisticsBatchService.setStatisticsReport();
		} catch (RuntimeException e) {
			log.error("[setStatisticsReport] [배치] : [{}]", e);
		}

		return ("redirect:/opmanager/shop-statistics/report/general");
	}

	/**
	 * 연간 통계 배치 적재 (수동 버튼으로 동작함)
	 * @return
	 */
	@GetMapping(value="/report/general/statisticsYearBatch")
	public String statisticsYearBatch(){
		try {
			log.debug("[statisticsYearBatch] [ok]");

			shopStatisticsYearBatchService.setStatisticsYearReport(); // 테이블3개 적재
			shopStatisticsYearBatchService.statisticsYearReportAgeMonthBatch(); // 테이블2개 적재
			shopStatisticsYearBatchService.statisticsYearReportPsintBatch(); // 테이블1개 적재
			shopStatisticsYearBatchService.statisticsYearReportGoodsBatch(); // 테이블1개 적재
		} catch (RuntimeException e) {
			log.error("[statisticsYearBatch] [bad] : [{}]", e);
		}

		return ("redirect:/opmanager/shop-statistics/report/year");
	}


	/**
	 * 통계 > 답례품 구매현황 > 농협 사업자 답례품 현황 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/nh-item-sales")
	public String nhItemSalesStat(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}
		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setSearchYear(DateUtils.getToday("yyyy"));
			statisticsParam.setSearchMonth(DateUtils.getToday("MM"));
		}

		int totalCount = 0;

		Pagination pagination = Pagination.getInstance(totalCount, statisticsParam.getItemsPerPage());
		statisticsParam.setPagination(pagination);

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		List<NhItemSalesStatistics> list = Collections.EMPTY_LIST;

		model.addAttribute("list", list);
		model.addAttribute("totalCount", statisticsParam.getPagination().getTotalItems());
		model.addAttribute("pagination", statisticsParam.getPagination());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		return ViewUtils.view();
	}

	@PostMapping("/sales/nh-item-sales")
	public String serchnhItemSalesStat(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/", "권한이 없습니다.");
		}
		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setSearchYear(DateUtils.getToday("yyyy"));
			statisticsParam.setSearchMonth(DateUtils.getToday("MM"));
		}

		int totalCount = shopStatisticsService.getNhItemSalesStatsCount(statisticsParam);

		Pagination pagination = Pagination.getInstance(totalCount, statisticsParam.getItemsPerPage());
		statisticsParam.setPagination(pagination);

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		List<NhItemSalesStatistics> list = shopStatisticsService.getNhItemSalesStats(statisticsParam);

		model.addAttribute("list", list);
		model.addAttribute("totalCount", statisticsParam.getPagination().getTotalItems());
		model.addAttribute("pagination", statisticsParam.getPagination());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		return ViewUtils.view();
	}


	/**
	 * 통계 > 답례품 구매현황 > 농협 사업자 답례품 현황 (고향사랑e음)
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/sales/nh-item-sales/excel")
	public ModelAndView nhItemSalesStatExcel(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		if (!SecurityUtils.isManager()) {
			return new ModelAndView(ViewUtils.redirect("/opmanager/shop-statistics/sales/nh-item-sales?" + requestContext.getQueryString(), "권한이 없습니다."));
		}
		if (ObjectUtils.isEmpty(statisticsParam.getSearchYear())) {
			statisticsParam.setSearchYear(DateUtils.getToday("yyyy"));
			statisticsParam.setSearchMonth(DateUtils.getToday("MM"));
		}

		Pagination pagination = Pagination.getInstance(shopStatisticsService.getNhItemSalesStatsCount(statisticsParam), Integer.MAX_VALUE);
		pagination.setItemsPerPage(Integer.MAX_VALUE);
		statisticsParam.setPagination(pagination);

		model.addAttribute("queryString",requestContext.getQueryString());
		model.addAttribute("statisticsParam", statisticsParam);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드

		// 조회조건
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도

		List<NhItemSalesStatistics> list = shopStatisticsService.getNhItemSalesStats(statisticsParam);

		ModelAndView mav = new ModelAndView(new RemittanceExcelView2("농협_사업자_답례품_현황"));

		HeaderCell[] headerCells = new HeaderCell[]{
				new HeaderCell(300, 	"No",	""),
				new HeaderCell(3000, 	"사업자번호",	""),
				new HeaderCell(3000, 	"아이디",	""),
				new HeaderCell(4000, 	"상호명",	""),
				new HeaderCell(3000, 	"지자체", 	""),
				new HeaderCell(12000, 	"답례품명", 	""),
				new HeaderCell(3000, 	"카테고리", 		""),
				new HeaderCell(4000, 	"하위카테고리", 	""),
				new HeaderCell(3000, 	"가격(단가)", 	""),
				new HeaderCell(3000, 	"주문수", 	""),
				new HeaderCell(3000, 	"판매개수", 	""),
				new HeaderCell(3000, 	"총 판매액", 	""),
				new HeaderCell(3000, 	"상품코드", 	"")
		};

		List<List<String>> excelList = new ArrayList<>();
		long idx = list.size();

		for (NhItemSalesStatistics nhItemSalesStatistics : list) {
			List<String> nhExcel = new ArrayList<>();
			nhExcel.add(StringUtils.numberFormat(idx--));
			nhExcel.add(nhItemSalesStatistics.getBusinessNumber());
			nhExcel.add(nhItemSalesStatistics.getLoginId());
			nhExcel.add(nhItemSalesStatistics.getCompanyName());
			nhExcel.add(nhItemSalesStatistics.getUpperLocgovNm() + " " + nhItemSalesStatistics.getLocgovNm());
			nhExcel.add(nhItemSalesStatistics.getItemName());
			nhExcel.add(nhItemSalesStatistics.getCategoryNms());
			nhExcel.add(nhItemSalesStatistics.getCategoryDetailNms());
			nhExcel.add(StringUtils.numberFormat(nhItemSalesStatistics.getPrice()));
			nhExcel.add(StringUtils.numberFormat(nhItemSalesStatistics.getOrderCnt()));
			nhExcel.add(StringUtils.numberFormat(nhItemSalesStatistics.getSaleCnt()));
			nhExcel.add(StringUtils.numberFormat(nhItemSalesStatistics.getTotalSalePrice()));
			nhExcel.add(nhItemSalesStatistics.getItemUserCode());

			excelList.add(nhExcel);
		}

		mav.addObject("headerCells", headerCells);
		mav.addObject("remittanceExcelList", excelList);
		mav.addObject("title", "농협 사업자 답례품 현황");
		return mav;
	}

//	@PostMapping("/sales/growth")
//	public String growthPost(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
//		return growth(model, statisticsParam, requestContext);
//	}


	/**
	 * 관리자 팀별 그룹 관리 리스트
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/sales/item/day")
	public String salesItemDayPost(Model model, StatisticsParam statisticsParam,
			RequestContext requestContext) {
		return salesItemDay(model, statisticsParam, requestContext);
	}

	/**
	 * 관리자 팀별 그룹 관리 리스트
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/sales/item/{itemId}")
	@RequestProperty(title = "매출 통계", layout = "base")
	public String salesItemDetailPost(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		return salesItemDetail(model, statisticsParam, requestContext);
	}

	/**
	 * 상품별 판매액 통계
	 * @param model
	 * @param statisticsParam
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/sales/item")
	public String salesItemPost(Model model, StatisticsParam statisticsParam, RequestContext requestContext) {
		return salesItem(model, statisticsParam, requestContext);
	}
}
