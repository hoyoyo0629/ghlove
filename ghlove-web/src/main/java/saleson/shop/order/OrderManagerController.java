package saleson.shop.order;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.exception.UserException;
//import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.NumberUtils;
import com.onlinepowers.framework.util.RedirectAttributeUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.IdType;
import saleson.common.userauth.UserAuthService;
import saleson.common.utils.PointUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.common.file.ExcelDownloadView;
import saleson.model.ConfigPg;
import saleson.seller.main.SellerService;
import saleson.seller.main.support.SellerParam;
//import saleson.shop.accountnumber.AccountNumberService;
//import saleson.shop.cart.CartService;
//import saleson.shop.categoriesteamgroup.CategoriesTeamGroupService;
import saleson.shop.claim.ClaimService;
import saleson.shop.claim.domain.ClaimMemo;
import saleson.shop.claim.support.ClaimMemoParam;
//import saleson.shop.customer.CustomerService;
import saleson.shop.deliverycompany.DeliveryCompanyService;
//import saleson.shop.item.ItemService;
import saleson.shop.log.ExceldownloadLogService;
//import saleson.shop.mailconfig.MailConfigService;
import saleson.shop.order.addpayment.domain.OrderAddPayment;
import saleson.shop.order.admin.OrderAdminService;
import saleson.shop.order.admin.support.OrderAdminException;
import saleson.shop.order.admin.support.OrderAdminParam;
import saleson.shop.order.claimapply.OrderClaimApplyService;
import saleson.shop.order.claimapply.domain.AdminClaimApply;
import saleson.shop.order.claimapply.domain.ClaimApply;
import saleson.shop.order.claimapply.domain.OrderCancelApply;
import saleson.shop.order.claimapply.domain.OrderCancelShipping;
import saleson.shop.order.claimapply.domain.OrderExchangeApply;
import saleson.shop.order.claimapply.domain.OrderReturnApply;
import saleson.shop.order.claimapply.support.ClaimApplyParam;
import saleson.shop.order.claimapply.support.ClaimException;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.domain.OrderList;
import saleson.shop.order.domain.OrderPayment;
import saleson.shop.order.domain.OrderShipping;
import saleson.shop.order.domain.OrderShippingInfo;
//import saleson.shop.order.giftitem.OrderGiftItemService;
//import saleson.shop.order.pg.PgService;
import saleson.shop.order.pg.config.ConfigPgService;
import saleson.shop.order.refund.OrderRefundService;
import saleson.shop.order.refund.domain.OrderRefund;
import saleson.shop.order.refund.support.OrderRefundParam;
import saleson.shop.order.shipping.support.ShippingParam;
import saleson.shop.order.shipping.support.ShippingReadyParam;
import saleson.shop.order.support.EditPayment;
import saleson.shop.order.support.OrderDetailExcelView;
import saleson.shop.order.support.OrderException;
import saleson.shop.order.support.OrderManagerException;
import saleson.shop.order.support.OrderParam;
import saleson.shop.order.support.OrderWaitingDetailExcelView;
import saleson.shop.order.support.ShippingDetailExcelView;
import saleson.shop.point.PointService;
import saleson.shop.point.domain.AvailablePoint;
import saleson.shop.point.exception.PointException;
import saleson.shop.receipt.ReceiptService;
//import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.support.RemittanceExcelView2;
//import saleson.shop.sendmaillog.SendMailLogService;
import saleson.shop.user.LocgovService;
import saleson.shop.user.UserService;

@Controller
@RequestMapping("/opmanager/order")
@RequestProperty(title="주문관리", layout="default", template="opmanager")
public class OrderManagerController {

	private static final Logger log = LoggerFactory.getLogger(OrderManagerController.class);

	@Autowired
	private OrderService orderService;

//	@Autowired
//	private CodeResolver codeResolver;

//	@Autowired
//	private SendMailLogService sendMailLogService;

//	@Autowired
//	private CategoriesTeamGroupService categoryCategoriesTeamGroupService;

//	@Autowired
//	private MailConfigService mailConfigService;

//	@Autowired
//	private ItemService itemService;

//	@Autowired
//	private CategoriesTeamGroupService categoriesTeamGroupService;

//	@Autowired
//	private CartService cartService;

	@Autowired
	private PointService pointService;

//	@Autowired
//	private AccountNumberService accountNumberService;

	@Autowired
	private UserService userService;

//	@Autowired
//	private CustomerService customerService;

	@Autowired
	private ClaimService claimService;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private OrderClaimApplyService orderClaimApplyService;

	@Autowired
	private OrderRefundService orderRefundService;

	@Autowired
	private OrderAdminService orderAdminService;

//	@Autowired
//	private PgService inicisService;

	@Autowired
    private ReceiptService receiptService;

//	@Autowired
//	private OrderGiftItemService orderGiftItemService;

	@Autowired
	private ConfigPgService configPgService;

	@Autowired
	private DeliveryCompanyService deliveryCompanyService;

	@Autowired
	private LocgovService locgovService;	// 지자체 관리

	@Autowired
	private ExceldownloadLogService exceldownloadLogService;

	@Autowired
	private UserAuthService userAuthService;

	@GetMapping("list")
	public String list(@ModelAttribute OrderParam orderParam, Model model) {

		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
//		} else if (SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//			LocalDate startDate = LocalDate.parse(orderParam.getSearchStartDate(), formatter);
//			LocalDate endDate = LocalDate.parse(orderParam.getSearchEndDate(), formatter);
//
//			if (startDate.plusDays(30).isBefore(endDate)) {
//				return ViewUtils.redirect("/opmanager/order/list", "엑셀 다운로드 기능으로 인해 최대 31일 간격까지 조회가능합니다.");
//			}
		}

		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CREATED_DATE");
		}

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", 0);
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return "view:/order/list/all";
	}




	@PostMapping("list")
	public String searchList(@ModelAttribute OrderParam orderParam, Model model) {

		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
//		} else if (SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//			LocalDate startDate = LocalDate.parse(orderParam.getSearchStartDate(), formatter);
//			LocalDate endDate = LocalDate.parse(orderParam.getSearchEndDate(), formatter);
//
//			if (startDate.plusDays(30).isBefore(endDate)) {
//				return ViewUtils.redirect("/opmanager/order/list", "엑셀 다운로드 기능으로 인해 최대 31일 간격까지 조회가능합니다.");
//			}
		}

		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CREATED_DATE");
		}

		List<OrderList> list = orderService.getAllOrderListByParamForManager(orderParam);
		model.addAttribute("list", list);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return "view:/order/list/all";
	}


	/**
	 * 오프라인 주문관리 화면
	 * @param orderParam
	 * @param model
	 * @return
	 */
	@GetMapping("offList")
	public String offList(@ModelAttribute OrderParam orderParam, Model model) {

		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CREATED_DATE");
		}

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return "view:/order/list/offList";
	}

	/**
	 * 오프라인 주문관리 조회
	 * @param orderParam
	 * @param model
	 * @return
	 */
	@PostMapping("offList")
	public String searchOffList(@ModelAttribute OrderParam orderParam, Model model) {

		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CREATED_DATE");
		}

		model.addAttribute("list", orderService.getOfflineAllOrderListByParamForManager(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return "view:/order/list/offList";
	}




	/**
	 * 주문상세 배송정보 등록 처리
	 * @param requestContext
	 * @param order
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("{pageType}/order-info/change")
	public JsonView orderInfoChange(RequestContext requestContext, @Valid Order order, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return JsonViewUtils.failure("필수 입력 항목을 정확히 입력해 주세요.");
		}

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}
		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		try {
			orderService.saveOrderInfo(order);
			return JsonViewUtils.success();
		} catch (OrderException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
//			return JsonViewUtils.failure(e.getMessage());
			log.error("ERROR: {}", getClass().getName() + " :: orderInfoChange OrderException =============");
			return JsonViewUtils.failure("실패했습니다.");				// 실패했습니다.
		}
	}

	/**
	 * 주문상세 관리자 메모 수정
	 * @param requestContext
	 * @param order
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("{pageType}/admin-memo/change")
	public JsonView changeAdminMemo(RequestContext requestContext, @Valid Order order, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return JsonViewUtils.failure("필수 입력 항목을 정확히 입력해 주세요.");
		}
		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}
		try {
			orderService.updateAdminMemo(order);
			return JsonViewUtils.success();
		} catch (OrderException e) {
			log.error("ERROR: {}", e.getErrorMessage(), e);
			return JsonViewUtils.failure(e.getErrorMessage());
		}
	}

	/**
	 * 주문상세 배송정보 갱신
	 * @param requestContext
	 * @param orderParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="blank")
	@PostMapping("{pageType}/order-info/reload")
	public String saveOrderInfo(RequestContext requestContext, OrderParam orderParam, Model model) {

		orderParam.setConditionType("OPMANAGER");

		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		if (isUpperAdmin()) {
			return ViewUtils.getView("view:/order/include/order-shipping-info","권한이 없습니다.");
		}


		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new PageNotFoundException();
		}

		model.addAttribute("order", order);
		return "view:/order/include/order-shipping-info";
	}

	/**
	 * 등록 처리
	 * @param claimMemo
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("{pageType}/claim-memo/create")
	public JsonView claimMemoCreateAction(RequestContext requestContext, @Valid ClaimMemo claimMemo, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return JsonViewUtils.failure("필수 입력 항목을 정확히 입력해 주세요.");
		}

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		try {
			claimService.insertClaimMemo(claimMemo);
			return JsonViewUtils.success();
		} catch (OrderException e) {
			log.error("ERROR: {}", e.getErrorMessage(), e);
			return JsonViewUtils.failure(e.getErrorMessage());
		}
	}

	/**
	 * 메모 리스트
	 * @param requestContext
	 * @param param
	 * @param model
	 * @param pageType
	 * @return
	 */
	@PostMapping(value="{pageType}/claim-memo/list")
	@RequestProperty(layout="blank")
	public String claimMemoList(RequestContext requestContext, ClaimMemoParam param, Model model,
			@PathVariable("pageType") String pageType) {

		int totalCount = claimService.getClaimMemoCount(param);

		Pagination pagination = Pagination.getInstance(totalCount, 5);
		pagination.setLink("javascript:claimMemoList('" + param.getOrderCode() + "', [page])");
		param.setPagination(pagination);

		List<ClaimMemo> list = claimService.getClaimMemoList(param);

		model.addAttribute("pagination", pagination);
		model.addAttribute("pageType", pageType);
		model.addAttribute("list", list);
		return "view:/order/memo/list";
	}

	/**
	 * 메모 리스트
	 * @param requestContext
	 * @param param
	 * @param model
	 * @param claimMemoId
	 * @param pageType
	 * @return
	 */
	@GetMapping(value="{pageType}/claim-memo/update/{claimMemoId}")
	@RequestProperty(layout="base")
	public String claimMemoUpdate(RequestContext requestContext, ClaimMemoParam param, Model model,
			@PathVariable("claimMemoId") int claimMemoId,
			@PathVariable("pageType") String pageType) {

		ClaimMemo memo = claimService.getClaimMemoById(claimMemoId);
		if (memo == null) {
			throw new PageNotFoundException();
		}

		User user = userService.getUserByUserId(memo.getUserId());
		if (user == null) {
			throw new PageNotFoundException();
		}

		user.setUserId(user.getUserId());
		user.setUserName(user.getUserName());
		model.addAttribute("user", user);
		model.addAttribute("pageType", pageType);
		model.addAttribute("memo", memo);
		return "view:/order/memo/form";
	}

	@PostMapping("{pageType}/claim-memo/update/{claimMemoId}")
	public String claimMemoUpdate(RequestContext requestContext, ClaimMemo memo, Model model,
			@PathVariable("claimMemoId") int claimMemoId,
			@PathVariable("pageType") String pageType) {

		if (isUpperAdmin()) {
			return ViewUtils.redirect("/opmanager/order/"+ pageType +"/claim-memo/update/" + claimMemoId, "권한이 없습니다.");
		}

		claimService.updateClaimMemo(memo);

		return ViewUtils.redirect("/opmanager/order/"+ pageType +"/claim-memo/update/" + claimMemoId,
				MessageUtils.getMessage("M01221"), "try{opener.claimMemoList('"+ memo.getOrderCode() +"', 1);}catch(e){}"); //처리되었습니다.
	}

	/**
	 * 결제현황 페이지 주문 상세 POPUP (종합적인 주문 내역을 보여준다)
	 * @param orderCode
	 * @param model
	 * @return
	 */
	@GetMapping("{pageType}/order-detail-popup/{orderSequence}/{orderCode}")
	@RequestProperty(layout = "base")
	public String detailPopup(@PathVariable("orderCode") String orderCode,
							  @PathVariable("orderSequence") int orderSequence,
							  @PathVariable("pageType") String pageType,
							  Model model) {
		try {
			setModelOrderDetail(model, orderCode, pageType, orderSequence, "popup");
		} catch (PageNotFoundException | OrderException e) {
			log.error(getClass().getName() + " detailPopup error :: ", e);
			String type = "opmanager";
			if (ShopUtils.isSellerPage()) {
				type = "seller";
			}
			return ViewUtils.redirect("/" + type + "/order/list", "주문 내역이 없습니다.");
		}
		return "view:/order/detail/order-detail";
	}

	private void setModelOrderDetail(Model model, String orderCode, String pageType, int orderSequence, String mode) {
		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(orderCode);
		orderParam.setOrderSequence(orderSequence);

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		} else {
			orderParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new PageNotFoundException();
		}

		bindItemDetail(model, orderParam, pageType);

		model.addAttribute("order", order);
		model.addAttribute("isSellerPage", ShopUtils.isSellerPage());

		/*
		CashbillParam cashbillParam = new CashbillParam();
		cashbillParam.setWhere("orderCode");
		cashbillParam.setQuery(orderCode);*/

		// 증빙서류 신청 내역 (현금영수증)
		// model.addAttribute("cashbillIssues", receiptService.findAllCashbillIssue(cashbillParam.getPredicate()));

		/*
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderCode(orderCode);

		model.addAttribute("orderLogs", orderService.getOrderLogListByOrderCode(orderLog));
		*/

		model.addAttribute("mode", mode);
	}

	/**
	 * 주문 상세 (종합적인 주문 내역을 보여준다)
	 * @param orderCode
	 * @param model
	 * @return
	 */
	@GetMapping("{pageType}/order-detail/{orderSequence}/{orderCode}")
	public String detail(
			@PathVariable("orderCode") String orderCode,
			@PathVariable("pageType") String pageType,
			@PathVariable("orderSequence") int orderSequence, Model model
	) {
		try {
			setModelOrderDetail(model, orderCode, pageType, orderSequence, "");
			OrderParam orderParam = new OrderParam();
			orderParam.setOrderCode(orderCode);
			orderParam.setOrderSequence(orderSequence);

			orderParam.setConditionType("OPMANAGER");
			if (ShopUtils.isSellerPage()) {
				orderParam.setSellerId(SellerUtils.getSellerId());
				orderParam.setConditionType("SELLER");
			} else {
				orderParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			}

			Order order = (Order) model.getAttribute("order");

			boolean mobileFlag = orderService.getMobileFlag(orderParam);
			if (mobileFlag) {
				order.setMobileItemYn("Y");
			} else {
				order.setMobileItemYn("N");
			}

			model.addAttribute("order", order);
		} catch (PageNotFoundException | OrderException e) {
			log.error(getClass().getName() + " detail error :: ", e);
			String type = "opmanager";
			if (ShopUtils.isSellerPage()) {
				type = "seller";
			}
			return ViewUtils.redirect("/" + type + "/order/list", "주문 내역이 없습니다.");
		}
		return "view:/order/detail/order-detail";
	}

	/**
	 * 주문 상세 (종합적인 주문 내역을 보여준다)
	 * @param orderCode
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="blank")
	@PostMapping("{pageType}/item-detail/{orderSequence}/{orderCode}")
	public String itemDetail(@PathVariable("orderCode") String orderCode,
			@PathVariable("pageType") String pageType,
			@PathVariable("orderSequence") int orderSequence, Model model) {

		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(orderCode);
		orderParam.setOrderSequence(orderSequence);

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new PageNotFoundException();
		}

		bindItemDetail(model, orderParam, pageType);
		model.addAttribute("order", order);
		model.addAttribute("isSellerPage", ShopUtils.isSellerPage());
		return "view:/order/include/item-detail";
	}


	/**
	 * 주문상세에 상품관련 정보를 조회 - 클레임
	 * @param model
	 * @param orderParam
	 */
	private void bindItemDetail(Model model, OrderParam orderParam, String pageType) {

		ClaimApplyParam claimApplyParam = new ClaimApplyParam();
		claimApplyParam.setOrderCode(orderParam.getOrderCode());
		claimApplyParam.setOrderSequence(orderParam.getOrderSequence());

		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setConditionType("SELLER");
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
		}

		if (UserUtils.isManagerLogin() && !UserUtils.hasMasterManagerRole()) {
			// 지자체관리자일 경우 지자체코드 필요
			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		model.addAttribute("activeExchanges", orderClaimApplyService.getActiveExchangeListByParam(claimApplyParam));
		model.addAttribute("exchangeHistorys", orderClaimApplyService.getExchangeHistoryListByParam(claimApplyParam));

		model.addAttribute("activeCancels", orderClaimApplyService.getActiveCancelListByParam(claimApplyParam));
		model.addAttribute("cancelHistorys", orderClaimApplyService.getCancelHistoryListByParam(claimApplyParam));

		model.addAttribute("activeReturns", orderClaimApplyService.getActiveReturnListByParam(claimApplyParam));
		model.addAttribute("returnHistorys", orderClaimApplyService.getReturnHistoryListByParam(claimApplyParam));

		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());

		model.addAttribute("cancelClaimReasons", CodeUtils.getCodeInfoList("CANCEL_REASON"));	// 취소사유
		model.addAttribute("exchangeClaimReasons", CodeUtils.getCodeInfoList("EXCHANGE_REASON"));	// 교환사유
		model.addAttribute("returnClaimReasons", CodeUtils.getCodeInfoList("RETURN_REASON"));	// 환불사유

		String viewTabIndex = "0";
		if ("cancel".equals(pageType)) {
			viewTabIndex = "1";
		} else if ("return".equals(pageType)) {
			viewTabIndex = "2";
		} else if ("exchange".equals(pageType)) {
			viewTabIndex = "3";
		}

		model.addAttribute("pageType", pageType);
		model.addAttribute("viewTabIndex", viewTabIndex);
		model.addAttribute("orderLogs", orderClaimApplyService.getOrderLogList(claimApplyParam));
		// 주문 전체 사은품 조회
		//model.addAttribute("orderGiftItems", orderGiftItemService.getOrderGiftItemListByOrderCode(orderParam.getOrderCode()));

	}

	/**
	 * 환불 처리
	 * @return
	 */
	@PostMapping("{pageType}/claim/return/process")
	public JsonView claimReturnProcess(ClaimApply claimApply, HttpServletResponse response,
			@PathVariable("pageType") String pageType, RequestContext requestContext,HttpServletRequest request) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		try {
			/*
			String orderCode = claimApply.getOrderCode();
			int orderSequence = claimApply.getOrderSequence();

			boolean isGetRefundCode = false;

			// 반품
			String[] returnIds = claimApply.getReturnIds();
			if (returnIds != null) {

				for(String claimCode : claimApply.getReturnIds()) {
					OrderReturnApply returnApply = claimApply.getReturnApplyMap().get(claimCode);
					if (returnApply == null) {
						throw new OrderException();
					}

					String claimStatus = returnApply.getClaimStatus();
					if ("03".equals(claimStatus)) {
						isGetRefundCode = true;
					}
				}
			}
			*/
/*
			if (isGetRefundCode) {
				OrderRefundParam orderRefundParam = new OrderRefundParam();
				orderRefundParam.setOrderCode(orderCode);
				orderRefundParam.setOrderSequence(orderSequence);

				String refundCode = orderRefundService.getActiveRefundCodeByParam(orderRefundParam);
				if (ObjectUtils.isEmpty(refundCode)) {
					throw new OrderException();
				}

				claimApply.setRefundCode(refundCode);
			}
*/
			orderClaimApplyService.orderReturnProcess(claimApply);

		} catch (ClaimException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.debug(getClass().getName() + " :: claimReturnProcess ClaimException =============", e);
			try {
				response.sendError(Integer.parseInt(e.getErrorCode()));
			} catch (NumberFormatException | IOException e1) {
//				log.debug(e1.getMessage());
				log.debug(getClass().getName() + " :: claimReturnProcess NumberFormatException | IOException =============", e1);
			}
		} catch (OrderException oe) {
			return JsonViewUtils.failure(oe.getErrorMessage());
		}

		return JsonViewUtils.success();
	}

	/**
	 * 환불 - 미리보기 (배송비 수정 및 안내 화면)
	 * @return
	 */
	@RequestProperty(layout="blank")
	@PostMapping("{pageType}/claim/return/view")
	public String claimReturnView(ClaimApply claimApply,
			@PathVariable("pageType") String pageType, RequestContext requestContext,
			Model model, HttpServletResponse response) {

		try {
			if (!requestContext.isAjaxRequest()) {
			    throw new NotAjaxRequestException();
			}

			String[] returnIds = claimApply.getReturnIds();
			if (returnIds == null) {
				throw new ClaimException("9000");
			}

			OrderParam orderParam = new OrderParam();
			orderParam.setConditionType("OPMANAGER");
			orderParam.setOrderCode(claimApply.getOrderCode());
			orderParam.setOrderSequence(claimApply.getOrderSequence());
			Order order = orderService.getOrderByParam(orderParam);

			if (order == null) {
				throw new ClaimException();
			}

			List<OrderAddPayment> list = null;

			boolean isError = false;
			String errorMessage = "";
			//이상우 [2017-03-23 추가] exception 에러메시지, 에러여부를 html에 뿌린 후 ajax 요청에서 분기 처리
			try {
				list = orderClaimApplyService.orderReturnViewData(claimApply, order);
			} catch (OrderException oe) {
//				log.debug(oe.getMessage());
//				isError = true;
//				errorMessage = oe.getMessage();
				log.debug(getClass().getName() + " :: claimReturnView OrderException ==========");
				isError = true;
				errorMessage = "실패했습니다.";			// 실패했습니다.
			}
			model.addAttribute("errorMessage", errorMessage);
			model.addAttribute("isError", isError);

			int returnAmount = 0;
			int addAmount = 0;
			if (list != null) {
				for(OrderAddPayment addPayment : list) {
					if ("2".equals(addPayment.getAddPaymentType())) {
						returnAmount += addPayment.getAmount();
					} else {
						addAmount += addPayment.getAmount();
					}
				}
			}

			List<OrderItem> itemList = new ArrayList<>();
			for(OrderShippingInfo info : order.getOrderShippingInfos()) {
				for(OrderItem item : info.getOrderItems()) {
					itemList.add(item);
				}
			}

			int itemReturnAmount = 0;

			List<OrderReturnApply> newApplyList = new ArrayList<>();
			for(String claimCode : claimApply.getReturnIds()) {

				OrderReturnApply returnApply = claimApply.getReturnApplyMap().get(claimCode);
				if (returnApply == null) {
					throw new ClaimException();
				}

				for(OrderItem item : itemList) {

					if (returnApply.getItemSequence() == item.getItemSequence()) {

						if (returnApply.getClaimCode().equals(claimCode)) {

							if (!"99".equals(returnApply.getClaimStatus())) {
								item.setQuantity(item.getQuantity() - returnApply.getClaimApplyQuantity());
							}

							if (!"99".equals(returnApply.getClaimStatus())) {
								item.setQuantity(item.getQuantity() - returnApply.getClaimApplyQuantity());
							}

							returnApply.setOrderItem(item);
							newApplyList.add(returnApply);

							itemReturnAmount += (item.getSalePrice() * item.getQuantity());
							break;
						}
					}
				}
			}

			long sellerId = SellerUtils.DEFAULT_OPMANAGER_SELLER_ID;
			if (ShopUtils.isSellerPage()) {
				sellerId = SellerUtils.getSellerId();
			}

			model.addAttribute("loginSellerId", sellerId);
			model.addAttribute("order", order);
			model.addAttribute("applys", newApplyList);
			model.addAttribute("itemReturnAmount", itemReturnAmount);
			model.addAttribute("returnAmount", returnAmount + itemReturnAmount);
			model.addAttribute("addAmount", addAmount);
			model.addAttribute("addPayments", list);

		} catch (ClaimException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.debug(getClass().getName() + " :: claimReturnView ClaimException ==========", e);
			try {
				response.sendError(Integer.parseInt(e.getErrorCode()));
			} catch (NumberFormatException | IOException e1) {
//				log.debug(e1.getMessage(), e1);
				log.debug(getClass().getName() + " :: claimReturnView NumberFormatException | IOException ==========", e1);
			}
		}

		return "view:/order/popup/claim-return-view";
	}

	/**
	 * 환불 - 저장 (03 : 환불 처리건이 있을때 해당 함수 호출 불가)
	 * @return
	 */
	@RequestProperty(layout="blank")
	@PostMapping("{pageType}/claim/return/save")
	public JsonView claimReturnSave(ClaimApply claimApply,
			@PathVariable("pageType") String pageType, RequestContext requestContext,
			HttpServletResponse response) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		try {
			orderClaimApplyService.orderReturnSaveProcess(claimApply);
		} catch (ClaimException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", getClass().getName() + " :: claimReturnSave ClaimException =============", e);
			try {
				response.sendError(Integer.parseInt(e.getErrorCode()));
			} catch (NumberFormatException | IOException e1) {
//				log.debug(e1.getMessage(), e);
				log.debug(getClass().getName() + " :: claimReturnSave NumberFormatException | IOException =============", e1);
			}
		} catch (OrderException oe) {
			return JsonViewUtils.failure(oe.getErrorMessage());
		}

		return JsonViewUtils.success();
	}

	/**
	 * 교환 처리
	 * @return
	 */
	@PostMapping("{pageType}/claim/exchange/process")
	public JsonView claimExchangeProcess(ClaimApply claimApply,
			@PathVariable("pageType") String pageType, RequestContext requestContext) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		orderClaimApplyService.orderExchangeProcess(claimApply);
		return JsonViewUtils.success();

	}

	/**
	 * 취소 처리 - 파라미터 및 로직 확인 필요
	 * @return
	 */
	@PostMapping("{pageType}/claim/cancel/process")
	public JsonView claimCancelProcess(ClaimApply claimApply,
			@PathVariable("pageType") String pageType, RequestContext requestContext) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

/*
		String orderCode = claimApply.getOrderCode();
		int orderSequence = claimApply.getOrderSequence();

		String[] cancelIds = claimApply.getCancelIds();
		boolean isGetRefundCode = false;

		// 취소
		if (cancelIds != null) {

			for(String claimCode : cancelIds) {
				OrderCancelApply cancelApply = claimApply.getCancelApplyMap().get(claimCode);
				if (cancelApply == null) {
					throw new OrderException();
				}

				String claimStatus = cancelApply.getClaimStatus();
				if ("03".equals(claimStatus)) {
					isGetRefundCode = true;
				}
			}
		}
		if (isGetRefundCode) {
			OrderRefundParam orderRefundParam = new OrderRefundParam();
			orderRefundParam.setOrderCode(orderCode);
			orderRefundParam.setOrderSequence(orderSequence);

			String refundCode = orderRefundService.getActiveRefundCodeByParam(orderRefundParam);
			if (ObjectUtils.isEmpty(refundCode)) {
				throw new OrderException();
			}

			claimApply.setRefundCode(refundCode);
		}
*/
//		orderClaimApplyService.orderCancelProcess(claimApply);

		try {
			orderClaimApplyService.giveGoodsOrderCancelProcess(claimApply);		// 로직 확인 필요
//			orderClaimApplyService.giveGoodsInsertOrderCancelApply(claimApply, true);
		} catch (OrderException e) {
			return JsonViewUtils.failure(e.getErrorMessage());
		}

		return JsonViewUtils.success();

	}

	/**
	 * 취소탭 갱신
	 * @param pageType
	 * @param claimApplyParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="blank")
	@PostMapping("{pageType}/claim/cancel/list")
	public String claimCancelForm(@PathVariable("pageType") String pageType, RequestContext requestContext,
			ClaimApplyParam claimApplyParam, Model model) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(claimApplyParam.getOrderCode());
		orderParam.setOrderSequence(claimApplyParam.getOrderSequence());

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			 throw new NotAjaxRequestException();
		}

		model.addAttribute("order", order);
		model.addAttribute("activeCancels", orderClaimApplyService.getActiveCancelListByParam(claimApplyParam));
		model.addAttribute("cancelHistorys", orderClaimApplyService.getCancelHistoryListByParam(claimApplyParam));
		model.addAttribute("pageType", pageType);

		return "view:/order/include/order-item-cancel";
	}

	/**
	 * 환불탭 갱신
	 * @param pageType
	 * @param claimApplyParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="blank")
	@PostMapping("{pageType}/claim/return/list")
	public String claimReturnForm(@PathVariable("pageType") String pageType, RequestContext requestContext,
			ClaimApplyParam claimApplyParam, Model model) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(claimApplyParam.getOrderCode());
		orderParam.setOrderSequence(claimApplyParam.getOrderSequence());

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}
		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			 throw new NotAjaxRequestException();
		}

		model.addAttribute("order", order);
		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());
		model.addAttribute("activeReturns", orderClaimApplyService.getActiveReturnListByParam(claimApplyParam));
		model.addAttribute("returnHistorys", orderClaimApplyService.getReturnHistoryListByParam(claimApplyParam));
		model.addAttribute("pageType", pageType);

		return "view:/order/include/order-item-return";
	}

	/**
	 * 반품 로그
	 * @param claimCode
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping(value="/claim/return-log/{claimCode}")
	public String returnLog(@PathVariable("claimCode") String claimCode, Model model) {

		OrderReturnApply apply = orderClaimApplyService.getReturnApplyByClaimCode(claimCode);
		if (apply == null) {
			throw new PageNotFoundException();
		}

		model.addAttribute("apply", apply);
		return "view:/order/popup/return-log";
	}

	/**
	 * 반품 로그
	 * @param claimCode
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping(value="/claim/exchange-log/{claimCode}")
	public String exchangeLog(@PathVariable("claimCode") String claimCode, Model model) {

		OrderExchangeApply apply = orderClaimApplyService.getExchangeApplyByClaimCode(claimCode);
		if (apply == null) {
			throw new PageNotFoundException();
		}

		model.addAttribute("apply", apply);
		return "view:/order/popup/exchange-log";
	}

	/**
	 * 교환탭 갱신
	 * @param pageType
	 * @param claimApplyParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="blank")
	@PostMapping("{pageType}/claim/exchange/list")
	public String claimExchangeForm(@PathVariable("pageType") String pageType, RequestContext requestContext,
			ClaimApplyParam claimApplyParam, Model model) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(claimApplyParam.getOrderCode());
		orderParam.setOrderSequence(claimApplyParam.getOrderSequence());

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			 throw new NotAjaxRequestException();
		}

		model.addAttribute("order", order);
		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());
		model.addAttribute("activeExchanges", orderClaimApplyService.getActiveExchangeListByParam(claimApplyParam));
		model.addAttribute("exchangeHistorys", orderClaimApplyService.getExchangeHistoryListByParam(claimApplyParam));
		model.addAttribute("pageType", pageType);

		return "view:/order/include/order-item-exchange";
	}

	/**
	 * 배송비 금액을 재계산한다 - 주문취소 데이터에 사용
	 * @param requestContext
	 * @return
	 */
	@PostMapping("{pageType}/re-shipping-amount")
	public JsonView reShippingAmount(RequestContext requestContext, ClaimApply claimApply) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		List<OrderShipping> list = orderClaimApplyService.getReShippingAmountForManager(claimApply);

		if (list != null) {

			/**
			 * CJH JSON데이터 만들면서 오류가 발생하는 경우가 있다.
			 */
			for(OrderShipping shipping : list) {
				shipping.setOrderItems(null);
			}
		}

		return JsonViewUtils.success(list);
	}

	/**
	 * 입금대기 목록
	 * @return
	 */
	@GetMapping("waiting-deposit")
	public String waitingDeposit(@ModelAttribute OrderParam orderParam, Model model) {

		model.addAttribute("list", orderService.getWaitingDepositListByParam(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
		return "view:/order/list/waiting-deposit";

	}

	/**
	 * 입금대기 목록 리스트 상태 변경
	 * @return
	 */
	@PostMapping("waiting-deposit/listUpdate/{mode}")
	public JsonView waitingDepositListUpdate(OrderParam orderParam,
			@PathVariable("mode") String mode, RequestContext requestContext) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		try {
			orderService.waitingDepositListUpdate(mode, orderParam);
			return JsonViewUtils.success();
		} catch (OrderManagerException e) {
			log.error("ERROR: {}", e.getErrorMessage(), e);
			return JsonViewUtils.failure("입금확인에 실패했습니다. ( " + e.getErrorMessage() + ")");
		}
	}

	/**
	 * 입금확인 취소
	 * @param orderParam
	 * @param key
	 * @param requestContext
	 * @return
	 */
	@PostMapping("waiting-deposit/payment-verification-cancel")
	public JsonView paymentVerificationCancel(OrderParam orderParam,
			@RequestParam("key") String key, RequestContext requestContext) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		try {

			String[] temp = StringUtils.delimitedListToStringArray(key, OrderPayment.PAYMENT_KEY_DIVISION_STRING);

			if (temp.length != 3) {
				throw new NotAjaxRequestException();
			}

			orderParam.setOrderCode(temp[0]);
			orderParam.setOrderSequence(Integer.parseInt(temp[1]));
			orderParam.setPaymentSequence(Integer.parseInt(temp[2]));

			orderService.paymentVerificationCancel(orderParam);
			return JsonViewUtils.success();
		} catch (OrderManagerException e) {
			log.error("ERROR: {}", e.getErrorMessage(), e);
			return JsonViewUtils.failure(e.getErrorMessage());
		}
	}

	/**
	 * 신규주문 목록
	 * @return
	 */
	@GetMapping("new-order")
	public String newOrder(@ModelAttribute OrderParam orderParam, Model model) {

		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
//			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchStartDate(DateUtils.addDay(DateUtils.getToday("yyyyMMdd"), -7));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", 0);
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/new-order";

	}

	@PostMapping("new-order")
	public String searchNewOrder(@ModelAttribute OrderParam orderParam, Model model) {
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
//			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchStartDate(DateUtils.addDay(DateUtils.getToday("yyyyMMdd"), -7));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", orderService.getNewOrderListByParam(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/new-order";
	}


	/**
	 * 신규주문(모바일) 목록
	 * @return
	 */
	@GetMapping("new-order-mobile")
	public String newOrderMobile(@ModelAttribute OrderParam orderParam, Model model) {

		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
//			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchStartDate(DateUtils.addDay(DateUtils.getToday("yyyyMMdd"), -7));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", 0);
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/new-order-mobile";

	}

	@PostMapping("new-order-mobile")
	public String searchNewOrderMobile(@ModelAttribute OrderParam orderParam, Model model) {
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
//			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchStartDate(DateUtils.addDay(DateUtils.getToday("yyyyMMdd"), -7));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", orderService.getNewOrderMobileListByParam(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/new-order-mobile";
	}


	/**
	 * 신규주문 목록 리스트 상태 변경
	 * @return
	 */
	@PostMapping("new-order/listUpdate/{mode}")
	public JsonView newOrderListUpdate(OrderParam orderParam,
			@PathVariable("mode") String mode, RequestContext requestContext) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (orderParam.getId() == null) {
			throw new OrderManagerException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		orderParam.setConditionType("OPMANAGER");
		// 판매자 아이디 셋팅
		String adminName = UserUtils.getManagerName();
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		orderParam.setAdminUserName(adminName);

		int totalCount = orderParam.getId().length;
		int updateCount = 0;
		for(String key : orderParam.getId()) {

			String[] temp = StringUtils.delimitedListToStringArray(key, OrderItem.ITEM_KEY_DIVISION_STRING);

			if (temp.length != 3) {
				continue;
			}

			orderParam.setOrderCode(temp[0]);
			orderParam.setOrderSequence(Integer.parseInt(temp[1]));
			orderParam.setItemSequence(Integer.parseInt(temp[2]));

			OrderItem orderItem = orderService.getOrderItemByParam(orderParam);
			if (orderItem == null) {
				continue;
			}

			ShippingReadyParam shippingReadyParam = null;
			for(ShippingReadyParam param : orderParam.getShippingReadys()) {
				if (key.equals(param.getKey())) {
					shippingReadyParam = param;
					break;
				}
			}

			if (shippingReadyParam == null) {
				continue;
			}

			// 출고 가능수량보다 출고 지시수량이 더 큰경우 뭔가 주문상태가 변했을수 있다.
			if (shippingReadyParam.getQuantity() > orderItem.getShippingReadyPossibleQuantity()) {
				continue;
			}

			shippingReadyParam.setOrderCode(orderParam.getOrderCode());
			shippingReadyParam.setOrderSequence(orderParam.getOrderSequence());
			shippingReadyParam.setItemSequence(orderParam.getItemSequence());
			shippingReadyParam.setSellerId(orderParam.getSellerId());
			shippingReadyParam.setAdminUserName(adminName);
			shippingReadyParam.setConditionType(orderParam.getConditionType());

			try {
				orderService.newOrderListUpdate(mode, shippingReadyParam, orderItem, orderParam);
				updateCount++;
			} catch (OrderException e) {
				log.error("orderService.newOrderListUpdate(..) : {}", e.getErrorMessage(), e);
			}
		}

		// 상태 변경수와 적용수가 같은가?
		if (totalCount != updateCount) {
			String errorMessage = "요청하신건중에 상태변경이 되지 않은것이 있습니다.\n(요청 : " + NumberUtils.formatNumber(totalCount, "#,###") + ", 처리 : " + NumberUtils.formatNumber(updateCount, "#,###") + ")";
			return JsonViewUtils.failure(errorMessage);
		}

		return JsonViewUtils.success();
	}

	/**
	 * 배송준비중 목록
	 * @return
	 */
	@GetMapping("shipping-ready")
	public String shippingReady(@ModelAttribute OrderParam orderParam, Model model) {

		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchDateType("OI.SHIPPING_READY_DATE");
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/shipping-ready";

	}

	@PostMapping("shipping-ready")
	public String searchShippingReady(@ModelAttribute OrderParam orderParam, Model model) {
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchDateType("OI.SHIPPING_READY_DATE");
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", orderService.getShippingReadyListByParam(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/shipping-ready";
	}


	/**
	 * 배송준비중 목록
	 * @return
	 */
	@GetMapping("shipping-ready-mobile")
	public String shippingReadyMobile(@ModelAttribute OrderParam orderParam, Model model) {

		/*if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}*/

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		orderParam.setMobile(true);

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", 0);
//		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/shipping-ready-mobile";

	}

	@PostMapping("shipping-ready-mobile")
	public String searchShippingReadyMobile(@ModelAttribute OrderParam orderParam, Model model) {
		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		orderParam.setMobile(true);

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", orderService.getShippingReadyListByParam(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
//		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/shipping-ready-mobile";
	}


	/**
	 * 배송준비중 목록 리스트 상태 변경
	 * @return
	 */
	@PostMapping("shipping-ready/listUpdate/{mode}")
	public JsonView shippingReadyListUpdate(OrderParam orderParam,
			@PathVariable("mode") String mode, RequestContext requestContext) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (orderParam.getId() == null) {
			throw new OrderManagerException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		orderParam.setConditionType("OPMANAGER");
		// 판매자 아이디 셋팅
		// 이상우 [2017-05-15 수정] 판매자 페이지에서 배송중 처리 시 getUser() null로 인해 else로 이동.
		String adminName = "";
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());

			if (SellerUtils.getSeller() != null) {
				adminName = SellerUtils.getSeller().getSellerName();
			}
		} else {
			if (UserUtils.getUser() != null) {
				adminName = UserUtils.getUser().getUserName();
			}
		}

		orderParam.setAdminUserName(adminName);

		int totalCount = orderParam.getId().length;
		int updateCount = 0;
		for(String key : orderParam.getId()) {

			String[] temp = StringUtils.delimitedListToStringArray(key, OrderItem.ITEM_KEY_DIVISION_STRING);

			if (temp.length != 3) {
				continue;
			}

			orderParam.setOrderCode(temp[0]);
			orderParam.setOrderSequence(Integer.parseInt(temp[1]));
			orderParam.setItemSequence(Integer.parseInt(temp[2]));

			OrderItem orderItem = orderService.getOrderItemByParam(orderParam);
			if (orderItem == null) {
				continue;
			}

			ShippingParam shippingParam = null;
			for(ShippingParam param : orderParam.getShippings()) {
				if (key.equals(param.getKey())) {
					shippingParam = param;
					break;
				}
			}

			if (shippingParam == null) {
				continue;
			}

			shippingParam.setOrderCode(orderParam.getOrderCode());
			shippingParam.setOrderSequence(orderParam.getOrderSequence());
			shippingParam.setItemSequence(orderParam.getItemSequence());
			shippingParam.setSellerId(orderParam.getSellerId());
			shippingParam.setAdminUserName(adminName);
			shippingParam.setConditionType(orderParam.getConditionType());

			try {
				orderService.shippingReadyListUpdate(mode, shippingParam, orderItem, orderParam);
				updateCount++;
			} catch (OrderException e) {
				log.error("orderService.shippingReadyListUpdate(..) : {}", e.getErrorMessage(), e);
			}
		}

		// 상태 변경수와 적용수가 같은가?
		if (totalCount != updateCount) {
			String errorMessage = "요청하신건중에 상태변경이 되지 않은것이 있습니다.\n(요청 : " + NumberUtils.formatNumber(totalCount, "#,###") + ", 처리 : " + NumberUtils.formatNumber(updateCount, "#,###") + ")";
			return JsonViewUtils.failure(errorMessage);
		}

		return JsonViewUtils.success();
	}

	/**
	 * 배송중 목록
	 * @return
	 */
	@GetMapping("shipping")
	public String shipping(@ModelAttribute OrderParam orderParam, Model model) {
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		orderParam.setStatusType("shipping");

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/shipping";

	}

	@PostMapping("shipping")
	public String searchShipping(@ModelAttribute OrderParam orderParam, Model model) {
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		orderParam.setStatusType("shipping");

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", orderService.getShippingListByParam(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/shipping";
	}


	/**
	 * 배송중 목록 리스트 상태 변경
	 * @author minae.yun[2017-09-06]
	 * @return
	 */
	@PostMapping("shipping/listUpdate/{mode}")
	public JsonView shippingListUpdate(OrderParam orderParam,
			@PathVariable("mode") String mode, RequestContext requestContext) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		int updateCount = 0;
		int totalCount = 0;
		if (orderParam.getId() != null) {
			totalCount = orderParam.getId().length;
		}

		for (String key : orderParam.getId()) {
			String[] temp = StringUtils.delimitedListToStringArray(key, OrderItem.ITEM_KEY_DIVISION_STRING);

			if (temp.length != 3) continue;

			orderParam.setOrderCode(temp[0]);
			orderParam.setOrderSequence(Integer.parseInt(temp[1]));
			orderParam.setItemSequence(Integer.parseInt(temp[2]));

			OrderItem orderItem = orderService.getOrderItemByParam(orderParam);

			ShippingParam shippingParam = new ShippingParam();
			shippingParam.setOrderCode(orderItem.getOrderCode());
			shippingParam.setOrderSequence(orderItem.getOrderSequence());
			shippingParam.setItemSequence(orderItem.getItemSequence());
			shippingParam.setDeliveryCompanyId(orderItem.getDeliveryCompanyId());
			shippingParam.setDeliveryCompanyName(orderItem.getDeliveryCompanyName());

			try {
				orderService.shippingListUpdate(mode, orderParam, shippingParam);
				updateCount++;
			} catch (OrderManagerException e) {
				log.error("ERROR: 배송준비중으로 상태 변경 중 오류  {}", e.getErrorMessage(), e);
			}

		}

		// 상태 변경수와 적용수가 같은가?
		if (totalCount != updateCount) {
			String errorMessage = "요청하신건중에 상태변경이 되지 않은것이 있습니다.\n(요청 : " + NumberUtils.formatNumber(totalCount, "#,###") + ", 처리 : " + NumberUtils.formatNumber(updateCount, "#,###") + ")";
			return JsonViewUtils.failure(errorMessage);
		}

		return JsonViewUtils.success();

	}

	/**
	 * 배송완료 목록
	 * @return
	 */
	@GetMapping("finish")
	public String finish(@ModelAttribute OrderParam orderParam, Model model) {
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		orderParam.setStatusType("finish");

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/finish";

	}

	/**
	 * 구매확정 목록
	 * @return
	 */
	@GetMapping("confirm")
	public String confirm(@ModelAttribute OrderParam orderParam, Model model) {
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", 0);
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/confirm";

	}

	/**
	 * 주문취소 리스트
	 * @param claimApplyParam
	 * @param model
	 * @return
	 */
	@GetMapping("cancel/list")
	public String cancelList(@ModelAttribute ClaimApplyParam claimApplyParam, Model model) {
		if (ObjectUtils.isEmpty(claimApplyParam.getSearchStartDate()) || ObjectUtils.isEmpty(claimApplyParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			claimApplyParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			claimApplyParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setConditionType("SELLER");
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
		}

		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			// 지자체관리자일 경우 지자체코드 필요
			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", claimApplyParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		return "view:/order/list/cancel";
	}

	/**
	 * 주문반품 리스트
	 * @param claimApplyParam
	 * @param model
	 * @return
	 */
	@GetMapping("return/list")
	public String returnList(@ModelAttribute ClaimApplyParam claimApplyParam, Model model) {
		if (ObjectUtils.isEmpty(claimApplyParam.getSearchStartDate()) || ObjectUtils.isEmpty(claimApplyParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			claimApplyParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			claimApplyParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setConditionType("SELLER");
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
		}

		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			// 지자체관리자일 경우 지자체코드 필요
			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", claimApplyParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		return "view:/order/list/return";
	}

	@PostMapping("return/list")
	public String searchReturnList(@ModelAttribute ClaimApplyParam claimApplyParam, Model model) {
		if (ObjectUtils.isEmpty(claimApplyParam.getSearchStartDate()) || ObjectUtils.isEmpty(claimApplyParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			claimApplyParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			claimApplyParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setConditionType("SELLER");
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
		}

		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			// 지자체관리자일 경우 지자체코드 필요
			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		model.addAttribute("list", orderClaimApplyService.getReturnListByParam(claimApplyParam));
		model.addAttribute("pagination", claimApplyParam.getPagination());
		model.addAttribute("totalCount", claimApplyParam.getPagination().getTotalItems());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		return "view:/order/list/return";
	}

	/**
	 * 주문교환 리스트
	 * @param claimApplyParam
	 * @param model
	 * @return
	 */
	@GetMapping("exchange/list")
	public String exchangeList(@ModelAttribute ClaimApplyParam claimApplyParam, Model model) {
		if (ObjectUtils.isEmpty(claimApplyParam.getSearchStartDate()) || ObjectUtils.isEmpty(claimApplyParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			claimApplyParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			claimApplyParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setConditionType("SELLER");
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
			claimApplyParam.setClaimStatus(new String[] {"01", "10", "11", "03", "99"});
		}

		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			// 지자체관리자일 경우 지자체코드 필요
			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", claimApplyParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		return "view:/order/list/exchange";
	}

	/**
	 * 환불 내역 리스트
	 * @param param
	 * @param model
	 * @return
	 */
	@GetMapping("refund/list")
	public String refund(@ModelAttribute OrderRefundParam param, Model model) {
		if (ObjectUtils.isEmpty(param.getSearchStartDate()) || ObjectUtils.isEmpty(param.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			param.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			param.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		if (ShopUtils.isSellerPage()) {
			param.setConditionType("SELLER");
			param.setSellerId(SellerUtils.getSellerId());
		}

		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			// 지자체관리자일 경우 지자체코드 필요
			param.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", param.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		return "view:/order/list/refund";
	}

	@PostMapping("refund/list")
	public String searchRefund(@ModelAttribute OrderRefundParam param, Model model) {
		if (ObjectUtils.isEmpty(param.getSearchStartDate()) || ObjectUtils.isEmpty(param.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			param.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			param.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		if (ShopUtils.isSellerPage()) {
			param.setConditionType("SELLER");
			param.setSellerId(SellerUtils.getSellerId());
		}

		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			// 지자체관리자일 경우 지자체코드 필요
			param.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		model.addAttribute("list", orderRefundService.getOrderRefundListByParam(param));
		model.addAttribute("pagination", param.getPagination());
		model.addAttribute("totalCount", param.getPagination().getTotalItems());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		return "view:/order/list/refund";
	}

	/**
	 * 환불 내역 리스트
	 * @param refundCode
	 * @param model
	 * @return
	 */
	@GetMapping("refund/detail/{refundCode}")
	public String refundDetail(@PathVariable("refundCode") String refundCode, Model model) {
		/*
		if (ShopUtils.isSellerPage()) {
			param.setConditionType("SELLER");
			param.setSellerId(SellerUtils.getSellerId());
		}

		if (UserUtils.isManagerLogin()) {
			// 지자체관리자일 경우 지자체코드 필요
			param.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}
		*/

		OrderRefund refund = orderRefundService.getOrderRefundByCode(refundCode);
		if (refund == null) {
			throw new PageNotFoundException();
		}

		String refundLocgovCode = refund.getLocgovCode();

		if (!isUpperAdmin()) {
			if (ShopUtils.isSellerPage()) {

			} else {
				if (!refundLocgovCode.equals(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER))) {
					return ViewUtils.redirect("/opmanager/order/refund/list", "권한이 없습니다.");
				}
			}
		}

		if (refund.getUserId() > 0) {
			AvailablePoint avilablePoint = pointService.getAvailablePointByUserId(refund.getUserId(), PointUtils.DEFAULT_POINT_CODE);
			model.addAttribute("avilablePoint", avilablePoint.getAvailablePoint());
		}

		/*
		ConfigPg configPg = configPgService.getConfigPg();
		String pgType = "";

		if (configPg != null) {
			pgType = configPg.getPgType().getCode().toLowerCase();
		} else {
			pgType = SalesonProperty.getPgService();
		}

		if ("nicepay".equals(pgType)) {

			String partCancel = "0";
			int payTotal = 0;
			OrderPgData orderPgData = null;

			for(OrderPayment orderPayment : refund.getOrderPayments()) {
				payTotal += orderPayment.getRemainingAmount();

				if (orderPayment.getOrderPgData().getOrderPgDataId() > 0) {
					orderPgData = orderPayment.getOrderPgData();
				}
			}

			if (refund.getTotalReturnAmount() != payTotal) {
				partCancel = "1";
			}

			model.addAttribute("partCancel", partCancel);
			model.addAttribute("nicepayPgData", orderPgData);
		}
		 */
		// model.addAttribute("bankListByKey", ShopUtils.getBankListByKey(pgType));
		model.addAttribute("refund", refund);
		return "view:/order/detail/refund";
	}

	/**
	 * 환불 처리
	 * @param refundCode
     * @param  editPayment
	 * @param model
	 * @return
	 */
	@PostMapping("refund/detail/{refundCode}")
	public String refundProcess(@PathVariable("refundCode") String refundCode, EditPayment editPayment, Model model, HttpServletRequest request, HttpServletResponse response) {

		/*if (ShopUtils.isSellerPage()) {
			throw new PageNotFoundException();
		}*/
		String redirectUrl = "/opmanager";
		if (ShopUtils.isSellerPage()) {
			redirectUrl = "/seller";
		}

		if (isUpperAdmin()) {
			return ViewUtils.redirect(redirectUrl + "/order/refund/detail/" + refundCode, "권한이 없습니다.");
		}


		OrderRefund refund = orderRefundService.getOrderRefundByCode(refundCode);

		if (refund == null) {
			throw new PageNotFoundException();
		}

		try {

			editPayment.setRequest(request);
			editPayment.setResponse(response);
			orderRefundService.orderRefundProcess(refund, editPayment);

            // 현금영수증 취소,재발급
            /*
            OrderParam orderParam = new OrderParam();
            orderParam.setOrderCode(editPayment.getOrderCode());
            orderParam.setOrderSequence(editPayment.getOrderSequence());
            orderParam.setConditionType("OPMANAGER");
            orderParam.setUserId(refund.getUserId());

            if (editPayment.isCashbillReissueFlag()) {
                receiptService.cashbillReIssue(orderParam);
            }
*/
		} catch(PointException e) {
			log.error("ERROR: {}", e.getErrorMessage(), e);
			return ViewUtils.redirect(redirectUrl + "/order/refund/detail/" + refundCode, e.getErrorMessage());
		} catch(OrderException e) {
			log.error("ERROR: {}", e.getErrorMessage(), e);
			return ViewUtils.redirect(redirectUrl + "/order/refund/detail/" + refundCode, e.getErrorMessage());
		}

		return ViewUtils.redirect(redirectUrl + "/order/refund/detail/" + refundCode, "처리되었습니다.");
	}

	/**
	 * 환불 신청 취소
	 * @param requestContext
	 * @param refundCode
	 * @return
	 */
	@PostMapping("refund/cancel/{refundCode}")
	public JsonView cancelRefund(RequestContext requestContext,
								 @PathVariable("refundCode") String refundCode) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		try {
			orderRefundService.cancelRefund(refundCode);
			return JsonViewUtils.success();
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", getClass().getName() + " :: cancelRefund RuntimeException ============", e);
			return JsonViewUtils.failure("환불 신청 취소 중 오류가 발생하였습니다");
		}
	}

	/**
	 * 송장번호 개별수정
	 * @param orderSequence
	 * @param itemSequence
	 * @param orderCode
	 * @param orderParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping(value="/shipping/change-shipping-number/{orderSequence}/{itemSequence}/{orderCode}")
	public String changeShippingNumber(@PathVariable("orderSequence") int orderSequence,
			@PathVariable("itemSequence") int itemSequence,
			@PathVariable("orderCode") String orderCode,
			OrderParam orderParam, Model model) {


		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		if (isUpperAdmin()) {
			return ViewUtils.getView("view:/order/popup/change-shipping-number", "권한이 없습니다.") ;
		}

		orderParam.setOrderCode(orderCode);
		orderParam.setOrderSequence(orderSequence);
		orderParam.setItemSequence(itemSequence);
		OrderItem orderItem = orderService.getOrderItemByParam(orderParam);
		if (orderItem == null) {
			throw new PageNotFoundException();
		}

		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());
		model.addAttribute("orderItem", orderItem);
		return "view:/order/popup/change-shipping-number";
	}

	/**
	 * 송장번호 개별수정 저장
	 * @param orderSequence
	 * @param itemSequence
	 * @param orderCode
	 * @param shippingParam
	 * @return
	 */
	@PostMapping("/shipping/change-shipping-number/{orderSequence}/{itemSequence}/{orderCode}")
	public String changeShippingNumberProcess(@PathVariable("orderSequence") int orderSequence,
			@PathVariable("itemSequence") int itemSequence,
			@PathVariable("orderCode") String orderCode, ShippingParam shippingParam) {

		String redirectUrl = "/opmanager";
		if (ShopUtils.isSellerPage()) {
			redirectUrl = "/seller";
			shippingParam.setSellerId(SellerUtils.getSellerId());
		}

		if (isUpperAdmin()) {
			return ViewUtils.redirect(redirectUrl, "권한이 없습니다.", "opener.location.reload()");
		}

		redirectUrl += "/order/shipping/change-shipping-number/" + orderSequence + "/" + itemSequence + "/" + orderCode;

		shippingParam.setOrderCode(orderCode);
		shippingParam.setOrderSequence(orderSequence);
		shippingParam.setItemSequence(itemSequence);

		try {
			orderService.changeShippingNumber(shippingParam);
			return ViewUtils.redirect(redirectUrl, "수정되었습니다.", "opener.location.reload()");
		} catch(OrderException oe) {
			return ViewUtils.redirect(redirectUrl, oe.getErrorMessage(), "opener.location.reload()");
		}
	}

	/**
	 * 빈페이지로 팝업닫기
	 * @return
	 */
	@GetMapping("{pageType}/success-popup-close")
	public @ResponseBody String popupClose() {

		StringBuffer sb = new StringBuffer();
		sb.append("<script>");

		//sb.append("alert(\"처리 완료되었습니다.\");");
		sb.append("opener.location.reload();");
		sb.append("self.close();");

		sb.append("</script>");
		return sb.toString();
	}

	@GetMapping("waiting-deposit/order-excel-download")
	public ModelAndView WaitingDepositExcelDownload(OrderParam orderParam, HttpServletRequest request) {

		// 엑셀 권한이 없는 경우
		if (!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
			// 엑셀 다운로드 + 개인정보 권한이 있는 경우
		} else if(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS")){
			// excelReadingLogService.insertExcelReadingUserLog(request,ExcelReadingLog.EXCEL_TYPE_WAITING_DEPOSIT,"");
		}

		ModelAndView mav = new ModelAndView(new OrderWaitingDetailExcelView());

		orderParam.setDownloadExcel(true);

		if (orderParam.getId() != null) {

			List<String> orderCodes = new ArrayList<>();

			for(String id : orderParam.getId()) {

				String[] temp = StringUtils.delimitedListToStringArray(id, OrderPayment.PAYMENT_KEY_DIVISION_STRING);

				if (temp.length != 3) {
					continue;
				}

				orderCodes.add(temp[0]);

			}

			orderParam.setOrderCodes(orderCodes);
		}

		mav.addObject("orderWaitingdetailList", orderService.getWaitingDepositListByParam(orderParam));

		return mav;

	}

	@GetMapping("new-order/order-excel-download")
	public ModelAndView NewOrderExcelDownload(OrderParam orderParam, HttpServletRequest request) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
			// 엑셀 다운로드 + 개인정보 권한이 있는 경우
		} else if(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS")){
			// excelReadingLogService.insertExcelReadingUserLog(request,ExcelReadingLog.EXCEL_TYPE_NEW_ORDER,"");
		}

		ModelAndView mav = new ModelAndView(new OrderDetailExcelView("신규주문목록"));

		// HttpSession session = RequestContextUtils.getSession();
		// OrderParam orderParam = (OrderParam)session.getAttribute("reOrderParam");

		orderParam.setDownloadExcel(true);
		orderParam.setConditionType("OPMANAGER");

		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}

		if (orderParam.getId() != null) {

			List<OrderItem> orderItems = new ArrayList<>();

			for(String id : orderParam.getId()) {

				String[] temp = StringUtils.delimitedListToStringArray(id, OrderItem.ITEM_KEY_DIVISION_STRING);

				if (temp.length != 3) {
					continue;
				}

				OrderItem orderItem = new OrderItem();
				orderItem.setOrderCode(temp[0]);
				orderItem.setItemSequence(Integer.parseInt(temp[2]));

				orderItems.add(orderItem);

			}

			orderParam.setOrderItems(orderItems);
		}

		mav.addObject("orderDetailList", orderService.getNewOrderListByParam(orderParam));
		mav.addObject("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());

		return mav;

	}

	@SuppressWarnings({ "resource", "finally" })
	@GetMapping("shipping-ready/order-excel-download")
	public ModelAndView shippingReadyExcelDownload(OrderParam orderParam, HttpServletRequest request, RequestContext requestContext) {
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		} else if(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS")){
		}
		orderParam.setDownloadExcel(true);
		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}
		String checkUrl;
		String redirectUrl;
		if (requestContext.isOpmanagerPage()) {
			checkUrl = "/opmanager/order/shipping-ready/order-excel-download";
			redirectUrl = "/opmanager/order/shipping-ready";
		} else {
			checkUrl = "/seller/order/shipping-ready/order-excel-download";
			redirectUrl = "/seller/order/shipping-ready";
		}
		if (!exceldownloadLogService.existExcelAccessLog(checkUrl)) {
			return new ModelAndView(ViewUtils.redirect(redirectUrl, "엑셀 다운로드 사유 정보가 없습니다."));
		}
		orderParam.setMobile(false);
		if (orderParam.getId() != null) {
			List<OrderItem> orderItems = new ArrayList<>();
			for(String id : orderParam.getId()) {
				String[] temp = StringUtils.delimitedListToStringArray(id, OrderItem.ITEM_KEY_DIVISION_STRING);
				if (temp.length != 3) {
					continue;
				}
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderCode(temp[0]);
				orderItem.setItemSequence(Integer.parseInt(temp[2]));
				orderItems.add(orderItem);
			}
			orderParam.setOrderItems(orderItems);
		}

		Pagination pagination = Pagination.getInstance(0);
		orderParam.setPagination(pagination);

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		// 사용자 다운로드 파일 명
		String fileName
			= "배송준비목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = orderService.streamShippingReadyData(orderParam, false);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	@SuppressWarnings({ "resource", "finally" })
	@GetMapping("shipping-ready-mobile/order-excel-download")
	public ModelAndView shippingReadyMobileExcelDownload(OrderParam orderParam, HttpServletRequest request, RequestContext requestContext) {
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		} else if(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS")){
		}
		orderParam.setDownloadExcel(true);
		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}
		String checkUrl;
		String redirectUrl;
		if (requestContext.isOpmanagerPage()) {
			checkUrl = "/opmanager/order/shipping-ready-mobile/order-excel-download";
			redirectUrl = "/opmanager/order/shipping-ready-mobile";
		} else {
			checkUrl = "/seller/order/shipping-ready-mobile/order-excel-download";
			redirectUrl = "/seller/order/shipping-ready-mobile";
		}
		if (!exceldownloadLogService.existExcelAccessLog(checkUrl)) {
			return new ModelAndView(ViewUtils.redirect(redirectUrl, "엑셀 다운로드 사유 정보가 없습니다."));
		}
		orderParam.setMobile(true);
		if (orderParam.getId() != null) {
			List<OrderItem> orderItems = new ArrayList<>();
			for(String id : orderParam.getId()) {
				String[] temp = StringUtils.delimitedListToStringArray(id, OrderItem.ITEM_KEY_DIVISION_STRING);
				if (temp.length != 3) {
					continue;
				}
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderCode(temp[0]);
				orderItem.setItemSequence(Integer.parseInt(temp[2]));
				orderItems.add(orderItem);
			}
			orderParam.setOrderItems(orderItems);
		}

		Pagination pagination = Pagination.getInstance(0);
		orderParam.setPagination(pagination);

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		// 사용자 다운로드 파일 명
		String fileName
			= "발송준비(모바일)목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = orderService.streamShippingReadyData(orderParam, true);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}

	}

	// 엑셀 부분 선택 다운로드(체크박스 사용) 시 POST로 URL 요청
	@SuppressWarnings({ "resource", "finally" })
	@PostMapping("shipping-ready-mobile/order-excel-download")
	public ModelAndView shippingReadyMobileExcelDownloadPost(OrderParam orderParam, @RequestParam(required = false) String param, HttpServletRequest request, RequestContext requestContext) throws UnsupportedEncodingException {
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
		} else if(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS")){
		}
		param = URLDecoder.decode(param, StandardCharsets.UTF_8.name());
		String[] parseId = Arrays.stream(param.split("&"))
				.map(s -> s.replace("id=", ""))
				.toArray(String[]::new);
		orderParam.setId(parseId);
		orderParam.setDownloadExcel(true);
		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}
		String checkUrl;
		String redirectUrl;
		if (requestContext.isOpmanagerPage()) {
			checkUrl = "/opmanager/order/shipping-ready-mobile/order-excel-download";
			redirectUrl = "/opmanager/order/shipping-ready-mobile";
		} else {
			checkUrl = "/seller/order/shipping-ready-mobile/order-excel-download";
			redirectUrl = "/seller/order/shipping-ready-mobile";
		}
		if (!exceldownloadLogService.existExcelAccessLog(checkUrl)) {
			return new ModelAndView(ViewUtils.redirect(redirectUrl, "엑셀 다운로드 사유 정보가 없습니다."));
		}
		orderParam.setMobile(true);
		if (orderParam.getId() != null) {
			List<OrderItem> orderItems = new ArrayList<>();
			for(String id : orderParam.getId()) {
				String[] temp = StringUtils.delimitedListToStringArray(id, OrderItem.ITEM_KEY_DIVISION_STRING);
				if (temp.length != 3) {
					continue;
				}
				OrderItem orderItem = new OrderItem();
				orderItem.setOrderCode(temp[0]);
				orderItem.setItemSequence(Integer.parseInt(temp[2]));
				orderItems.add(orderItem);
			}

			orderParam.setOrderItems(orderItems);
		}

		Pagination pagination = Pagination.getInstance(0);
		orderParam.setPagination(pagination);

		SXSSFWorkbook workbook = new SXSSFWorkbook();

		// 사용자 다운로드 파일 명
		String fileName
			= "발송준비(모바일)목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = orderService.streamShippingReadyData(orderParam, true);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}

	}

	@GetMapping("shipping/order-excel-download")
	public ModelAndView ShippingExcelDownload(OrderParam orderParam, HttpServletRequest request) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
			// 엑셀 다운로드 + 개인정보 권한이 있는 경우
		} else if(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS")){
			// excelReadingLogService.insertExcelReadingUserLog(request,ExcelReadingLog.EXCEL_TYPE_SHIPPING,"");
		}

		ModelAndView mav = new ModelAndView(new ShippingDetailExcelView());

		// HttpSession session = RequestContextUtils.getSession();
		// OrderParam orderParam = (OrderParam)session.getAttribute("reOrderParam");

		orderParam.setDownloadExcel(true);
		orderParam.setConditionType("OPMANAGER");

		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}

		if (orderParam.getId() != null) {

			List<OrderItem> orderItems = new ArrayList<>();

			for(String id : orderParam.getId()) {

				String[] temp = StringUtils.delimitedListToStringArray(id, OrderItem.ITEM_KEY_DIVISION_STRING);

				if (temp.length != 3) {
					continue;
				}

				OrderItem orderItem = new OrderItem();
				orderItem.setOrderCode(temp[0]);
				orderItem.setItemSequence(Integer.parseInt(temp[2]));

				orderItems.add(orderItem);

			}

			orderParam.setOrderItems(orderItems);
		}

		mav.addObject("ShippingOrderDetailList", orderService.getShippingListByParam(orderParam));

		return mav;

	}

	/**
	 * 배송완료 목록 - 엑셀 다운로드
	 * @return
	 */
	@GetMapping("finish/order-excel-download")
	public ModelAndView finishExcelDownload(@ModelAttribute OrderParam orderParam, Model model, RequestContext requestContext) {
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
			// 엑셀 다운로드 + 개인정보 권한이 있는 경우
		} else if(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS")){
			// excelReadingLogService.insertExcelReadingUserLog(request,ExcelReadingLog.EXCEL_TYPE_SHIPPING_READY,"");
		}

		ModelAndView mav = new ModelAndView(new RemittanceExcelView2("배송완료목록"));

		// HttpSession session = RequestContextUtils.getSession();
		// OrderParam orderParam = (OrderParam)session.getAttribute("reOrderParam");

		orderParam.setDownloadExcel(true);
		orderParam.setConditionType("OPMANAGER");

		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}

		String checkUrl;
		String redirectUrl;
		if (requestContext.isOpmanagerPage()) {
			checkUrl = "/opmanager/order/finish/order-excel-download";
			redirectUrl = "/opmanager/order/finish";
		} else {
			checkUrl = "/seller/order/finish/order-excel-download";
			redirectUrl = "/seller/order/finish";
		}

		String queryString = "";
		if (requestContext.getQueryString() != null) {
			queryString = "?" + requestContext.getQueryString();
		}

		if (!exceldownloadLogService.existExcelAccessLog(checkUrl)) {
			return new ModelAndView(ViewUtils.redirect(redirectUrl + queryString, "엑셀 다운로드 사유 정보가 없습니다."));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		orderParam.setStatusType("finish");

		if (orderParam.getId() != null) {

			List<OrderItem> orderItems = new ArrayList<>();

			for(String id : orderParam.getId()) {

				String[] temp = StringUtils.delimitedListToStringArray(id, OrderItem.ITEM_KEY_DIVISION_STRING);

				if (temp.length != 3) {
					continue;
				}

				OrderItem orderItem = new OrderItem();
				orderItem.setOrderCode(temp[0]);
				orderItem.setItemSequence(Integer.parseInt(temp[2]));

				orderItems.add(orderItem);

			}

			orderParam.setOrderItems(orderItems);
		}

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		orderParam.setPage(1);
		orderParam.setItemsPerPage(Integer.MAX_VALUE);

		List<OrderList> list = orderService.getShippingFinishListByParam(orderParam);

		if (list == null || list.isEmpty()) {
			return new ModelAndView(ViewUtils.redirect(redirectUrl + queryString, "조회된 내역이 없습니다."));
		}

		HeaderCell[] headerCells;

		String auth = getAuth();

		switch (auth) {
			case "SYSTEM":
				headerCells = new HeaderCell[]{
						new HeaderCell(300, 	"No",	""),
						new HeaderCell(3000, 	"지자체",	""),
						new HeaderCell(3000, 	"배송완료일",	""),
						new HeaderCell(3000, 	"주문번호",	""),
						new HeaderCell(3000, 	"주문자",	""),
						new HeaderCell(3000, 	"수취인",	""),
						new HeaderCell(5000, 	"상호명",	""),
						new HeaderCell(8000, 	"답례품정보",	""),
						new HeaderCell(2000, 	"수량",	""),
						new HeaderCell(3000, 	"판매가",	""),
						new HeaderCell(10000, 	"배송정보",	"")
				};
				break;
			case "SELLER":
				headerCells = new HeaderCell[]{
						new HeaderCell(300, 	"No",	""),
						new HeaderCell(3000, 	"배송완료일",	""),
						new HeaderCell(3000, 	"주문번호",	""),
						new HeaderCell(3000, 	"주문자",	""),
						new HeaderCell(3000, 	"수취인",	""),
						new HeaderCell(8000, 	"답례품정보",	""),
						new HeaderCell(2000, 	"수량",	""),
						new HeaderCell(3000, 	"판매가",	""),
						new HeaderCell(10000, 	"배송정보",	"")
				};
				break;
			default:
				headerCells = new HeaderCell[]{
						new HeaderCell(300, 	"No",	""),
						new HeaderCell(3000, 	"배송완료일",	""),
						new HeaderCell(3000, 	"주문번호",	""),
						new HeaderCell(3000, 	"주문자",	""),
						new HeaderCell(3000, 	"수취인",	""),
						new HeaderCell(5000, 	"상호명",	""),
						new HeaderCell(8000, 	"답례품정보",	""),
						new HeaderCell(2000, 	"수량",	""),
						new HeaderCell(3000, 	"판매가",	""),
						new HeaderCell(10000, 	"배송정보",	"")
				};
				break;
		}

		List<List<String>> excelList = new ArrayList<>();

		long idx = list.size();
		for (OrderList orderList : list) {
			List<String> excelData = new ArrayList<>();
			excelData.add(StringUtils.numberFormat(idx--));
			if ("SYSTEM".equalsIgnoreCase(auth)) {
				excelData.add(orderList.getLocgovNm());
			}
			excelData.add(DateUtils.datetime(orderList.getShippingFinishDate()));
			excelData.add(orderList.getOrderCode());
			excelData.add(orderList.getUserName());
			excelData.add(orderList.getReceiveName());
			if (!"SELLER".equalsIgnoreCase(auth)) {
				excelData.add(orderList.getCompanyName());
			}
			excelData.add(orderList.getItemName());
			excelData.add(StringUtils.numberFormat(orderList.getQuantity()));
			excelData.add(StringUtils.numberFormat(orderList.getSaleAmount()));
			if ("Y".equalsIgnoreCase(orderList.getMobileItemYn())) {
				if (StringUtils.hasLength(orderList.getMobileNumber())) {
					excelData.add("모바일 : " + orderList.getMobileNumber());
				} else {
					excelData.add("모바일 번호 없음");
				}
			} else {
				if (StringUtils.hasLength(orderList.getDeliveryNumber())) {
					excelData.add("송장번호 : " + orderList.getDeliveryNumber() + "(" + orderList.getDeliveryCompanyName() + ")");
				} else {
					excelData.add("송장 번호 없음");
				}
			}

			excelList.add(excelData);
		}

		mav.addObject("headerCells", headerCells);
		mav.addObject("remittanceExcelList", excelList);
		mav.addObject("title", "배송 완료 목록");

		return mav;

	}

	/**
	 * 구매확정 목록 - 엑셀 다운로드
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping("confirm/order-excel-download")
	public ModelAndView confirmExcelDownload(@ModelAttribute OrderParam orderParam, Model model, RequestContext requestContext) {// 엑셀 권한이 없는 경우
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		String checkUrl;
		String redirectUrl;
		if (requestContext.isOpmanagerPage()) {
			checkUrl = "/opmanager/order/confirm/order-excel-download";
			redirectUrl = "/opmanager/order/confirm";
		} else {
			checkUrl = "/seller/order/confirm/order-excel-download";
			redirectUrl = "/seller/order/confirm";
		}

		String queryString = "";
		if (requestContext.getQueryString() != null) {
			queryString = "?" + requestContext.getQueryString();
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CREATED_DATE");
		}

		if (!exceldownloadLogService.existExcelAccessLog(checkUrl)) {
			return new ModelAndView(ViewUtils.redirect(redirectUrl + queryString, "엑셀 다운로드 사유 정보가 없습니다."));
		}

		orderParam.setPage(1);
		orderParam.setItemsPerPage(1);

		List<OrderList> list = orderService.getConfirmListByParam(orderParam);

		if (list == null || list.isEmpty()) {
			return new ModelAndView(ViewUtils.redirect(redirectUrl + queryString, "조회된 내역이 없습니다."));
		}

		// 엑셀 다운로드
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "구매확정목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = orderService.streamConfirmOrderData(orderParam);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 전체주문관리 목록 - 엑셀 다운로드
	 * @return
	 */
	@SuppressWarnings({ "finally", "resource" })
	@GetMapping("list/order-excel-download")
	public ModelAndView allListExcelDownload(@ModelAttribute OrderParam orderParam, Model model, RequestContext requestContext) {// 엑셀 권한이 없는 경우
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		String checkUrl;
		String redirectUrl;
		if (requestContext.isOpmanagerPage()) {
			checkUrl = "/opmanager/order/list/order-excel-download";
			redirectUrl = "/opmanager/order/list";
		} else {
			checkUrl = "/seller/order/list/order-excel-download";
			redirectUrl = "/seller/order/list";
		}

		String queryString = "";
		if (requestContext.getQueryString() != null) {
			queryString = "?" + requestContext.getQueryString();
		}

		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CREATED_DATE");
		}

		orderParam.setDownloadExcel(true);
		orderParam.setConditionType("OPMANAGER");

		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}

		if (!exceldownloadLogService.existExcelAccessLog(checkUrl)) {
			return new ModelAndView(ViewUtils.redirect(redirectUrl + queryString, "엑셀 다운로드 사유 정보가 없습니다."));
		}

		// 데이터 존재 여부에 대한 확인
		orderParam.setPage(1);
		orderParam.setItemsPerPage(1);
		List<OrderList> list = orderService.getAllOrderListByParamForManager(orderParam);

		if (list == null || list.isEmpty()) {
			return new ModelAndView(ViewUtils.redirect(redirectUrl + queryString, "조회된 내역이 없습니다."));
		}

		// SXSSFWorkbook 인스턴스 생성 사유
		// - 엑셀 암호화로 인해 itemService에서 response를 종료 불가
		// - itemService에서 생성된 workbook을 controller통해 ItemReviewExcelView로 일괄 전송
		// - ItemReviewExcelView에서 엑셀 파일 생성 및 암호화, 파일 전송 일괄 수행
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		// 사용자 다운로드 파일 명
		// ALL_ITEM_REVIEW_20260115142019.xlsx
		String fileName
			= "전체주문목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = orderService.streamAllOrderData(orderParam);
		} finally {
			/** ModelAndView에서 view이름이 아닌 객체(ItemReviewExcelView)를 담게된 경우,
			 * ModelAndView를 반환받은 DispatcherServlet에서 ViewResolver를 실행시키지 않고 객체를 실행
			 */
			// ItemReviewExcelView에 workbook과 암호화에 사용할 비밀번호를 담아서 전송
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 엑셀 업로드
	 * @author minae.yun[2017-09-05]
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("/upload-excel")
	public String shippingReadyExcelUpload(Model model) {

		if (RedirectAttributeUtils.hasRedirectAttributes()) {
			model.addAttribute("result", RedirectAttributeUtils.get("result"));
		}
        return "view";
	}


	/**
	 * 배송준비중 목록에서 엑셀업로드로 배송정보 등록
	 * @author minae.yun[2017-09-05]
	 * @param orderParam
	 * @param multipartFile
	 * @param model
	 * @param redirectAttribute
	 * @return
	 */
	@RequestProperty(layout="base")
	@PostMapping("/upload-excel")
	public String shippingReadyExcelUpload(OrderParam orderParam, @RequestParam(value="file", required=false) MultipartFile multipartFile
			, Model model, RedirectAttributes redirectAttribute) {

		//엑셀에서 택배사, 운송장번호 읽어와서 shippingReadyListUpdate로 업데이트 작업처리
		Map<String, Object> resp = orderService.shippingReadyExcelUpload(orderParam, multipartFile, false);
		String result = (String) resp.get("result");

		// 배송중 메시지 발송
//		List<String> keyList = (List<String>) resp.get("key");
//		if (keyList != null) {
//			orderService.sendOrderDeliveryMessageByExcelUpload(keyList);
//		}

		model.addAttribute("result", result);
		RedirectAttributeUtils.addAttribute("result", result);

		String redirectUrl = "/opmanager";
		if (ShopUtils.isSellerPage()) {
			redirectUrl = "/seller";
		}

		return ViewUtils.redirect(redirectUrl+"/order/upload-excel");
	}

	/**
	 * 엑셀 업로드
	 * @author minae.yun[2017-09-05]
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("/upload-excel-mobile")
	public String shippingReadyExcelUploadMobile(Model model) {

		if (RedirectAttributeUtils.hasRedirectAttributes()) {
			model.addAttribute("result", RedirectAttributeUtils.get("result"));
		}
        return "view";
	}


	/**
	 * 배송준비중 목록에서 엑셀업로드로 배송정보 등록
	 * @author minae.yun[2017-09-05]
	 * @param orderParam
	 * @param multipartFile
	 * @param model
	 * @param redirectAttribute
	 * @return
	 */
	@RequestProperty(layout="base")
	@PostMapping("/upload-excel-mobile")
	public String shippingReadyExcelUploadMobile(OrderParam orderParam, @RequestParam(value="file", required=false) MultipartFile multipartFile
			, Model model, RedirectAttributes redirectAttribute) {

		//엑셀에서 택배사, 운송장번호 읽어와서 shippingReadyListUpdate로 업데이트 작업처리
		Map<String, Object> resp = orderService.shippingReadyExcelUpload(orderParam, multipartFile, true);
		String result = (String) resp.get("result");

		// 배송중 메시지 발송
//		List<String> keyList = (List<String>) resp.get("key");
//		if (keyList != null) {
//			orderService.sendOrderDeliveryMessageByExcelUpload(keyList);
//		}

		model.addAttribute("result", result);
		RedirectAttributeUtils.addAttribute("result", result);

		String redirectUrl = "/opmanager";
		if (ShopUtils.isSellerPage()) {
			redirectUrl = "/seller";
		}

		return ViewUtils.redirect(redirectUrl+"/order/upload-excel");
	}

	/**
	 * 결제 정보 변경
	 * @param pageType
	 * @param orderSequence
	 * @param orderCode
	 * @param model
	 * @return
	 */
	@GetMapping("{pageType}/change-pay/{orderSequence}/{orderCode}")
	@RequestProperty(layout="base")
	public String payChange(@PathVariable("pageType") String pageType,
			@PathVariable("orderSequence") int orderSequence,
			@PathVariable("orderCode") String orderCode , Model model) {


		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(orderCode);
		orderParam.setOrderSequence(orderSequence);
		orderParam.setPayChangeType("Y");

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}

		if (isUpperAdmin()) {
			return ViewUtils.getView("view:/order/popup/change-pay", "권한이 없습니다.");
		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new PageNotFoundException();
		}

		if (order.getUserId() > 0) {
			AvailablePoint avilablePoint = pointService.getAvailablePointByUserId(order.getUserId(), PointUtils.DEFAULT_POINT_CODE);
			model.addAttribute("avilablePoint", avilablePoint.getAvailablePoint());
		}

		ConfigPg configPg = configPgService.getConfigPg();
		String pgType = "";

		if (configPg != null) {
			pgType = configPg.getPgType().getCode().toLowerCase();
		} else {
			pgType = SalesonProperty.getPgService();
		}

		model.addAttribute("bankListByKey", ShopUtils.getBankListByKey(pgType));

		model.addAttribute("pageType", pageType);
		model.addAttribute("order", order);

		return "view:/order/popup/change-pay";
	}

    /**
     * 결제 정보 변경 저장
     * @param pageType
     * @param editPayment
     * @return
     */
	@PostMapping("{pageType}/change-pay/process")
	public String payChangeProcess(@PathVariable("pageType") String pageType,
			EditPayment editPayment) {

		if (isUpperAdmin()) {
			return ViewUtils.redirect("/opmanager/order/"+pageType+"/change-pay/"+ editPayment.getOrderSequence() +"/" + editPayment.getOrderCode(), "권한이 없습니다.", "opener.location.reload()");
		}

		try {
			orderService.changePayment(editPayment);

            OrderParam orderParam = new OrderParam();
            orderParam.setOrderCode(editPayment.getOrderCode());
            orderParam.setOrderSequence(editPayment.getOrderSequence());
            orderParam.setConditionType("OPMANAGER");
            orderParam.setUserId(editPayment.getUserId());

            // 현금영수증 취소,재발급
            if (editPayment.isCashbillReissueFlag()) {
                receiptService.cashbillReIssue(orderParam);
            }
		} catch (OrderException e) {
			log.error("orderService.changePayment(..) : {}", e.getErrorMessage(), e);
			return ViewUtils.redirect("/opmanager/order/"+pageType+"/change-pay/"+ editPayment.getOrderSequence() +"/" + editPayment.getOrderCode(), e.getErrorMessage());
		}

		return ViewUtils.redirect("/opmanager/order/"+pageType+"/change-pay/"+ editPayment.getOrderSequence() +"/" + editPayment.getOrderCode(), "수정되었습니다.", "opener.location.reload()");
	}

	/**
	 * 관리자 대량주문 등록 화면
	 * @param model
	 * @param orderAdminParam
	 * @return
	 */
	@GetMapping("admin/list")
	public String orderAdminList(Model model, OrderAdminParam orderAdminParam) {

		model.addAttribute("list", orderAdminService.getOrderAdminListByParam(orderAdminParam));
		model.addAttribute("pagination", orderAdminParam.getPagination());
		model.addAttribute("totalCount", orderAdminParam.getPagination().getTotalItems());

		return "view:/order/admin/list";
	}


	/**
	 * 관리자 대량주문 등록 화면
	 * @param model
	 * @param orderAdminParam
	 * @return
	 */
	@PostMapping("admin/list")
	public String orderAdminListProcess(Model model, OrderAdminParam orderAdminParam, HttpServletRequest request) {

		if (isUpperAdmin()) {
			return ViewUtils.redirect("/opmanager/order/admin/list", "권한이 없습니다.");
		}

		try {
			orderAdminService.insertOrderAdmin(orderAdminParam, request);
		} catch (OrderAdminException oae) {
			log.debug(oae.getErrorMessage(), oae);
			return ViewUtils.redirect("/opmanager/order/admin/list", oae.getErrorMessage());
		}

		return ViewUtils.redirect("/opmanager/order/admin/list", "처리되었습니다.");
	}

	/**
	 * 엑셀 업로드
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("/admin/upload-excel")
	public String uploadExcel(Model model) {

		if (RedirectAttributeUtils.hasRedirectAttributes()) {
			model.addAttribute("result", RedirectAttributeUtils.get("result"));
		}

        return "view:/order/admin/upload-excel";
	}

	/**
	 * 엑셀 업로드 처리.
	 * @param multipartFile
	 * @param model
	 * @param redirectAttribute
	 * @return
	 */
	@RequestProperty(layout="base")
	@PostMapping("/admin/upload-excel")
	public String uploadExcelProcess(@RequestParam(value="file", required=false) MultipartFile multipartFile
			, Model model, RedirectAttributes redirectAttribute) {

		String result = orderAdminService.insertExcelData("v01", multipartFile);

		model.addAttribute("result", result);
		RedirectAttributeUtils.addAttribute("result", result);
        return ViewUtils.redirect("/opmanager/order/admin/upload-excel");
	}

	/**
	 * 관리자 클래임 신청
	 * @param pageType
	 * @param adminClaimApply
	 * @return
	 */
	@PostMapping("{pageType}/admin-claim-apply")
	public String adminClaimApply(@PathVariable("pageType") String pageType, AdminClaimApply adminClaimApply) {

		String redirectUrl = "/opmanager";
		if (ShopUtils.isSellerPage()) {
			redirectUrl = "/seller";
		}

		orderService.adminClaimApply(adminClaimApply);

		// 주문취소시 즉시 환불 목록으로 보내기
		if ("1".equals(adminClaimApply.getClaimType())) { // 취소
//			OrderCancelApply apply = adminClaimApply.getOrderCancelApply();
			if ("Y".equals(adminClaimApply.getRefundFlag())) {

				List<OrderCancelApply> list = orderClaimApplyService.getAdminApplyCancelListByIds(adminClaimApply.getAdminClaimApplyKey());
				if (list != null) {

					ClaimApply claimApply = new ClaimApply();

					OrderRefundParam orderRefundParam = new OrderRefundParam();
					orderRefundParam.setOrderCode(adminClaimApply.getOrderCode());
					orderRefundParam.setOrderSequence(adminClaimApply.getOrderSequence());

					String refundCode = orderRefundService.getActiveRefundCodeByParam(orderRefundParam);
					if (ObjectUtils.isEmpty(refundCode)) {
						throw new OrderException();
					}

					claimApply.setRefundCode(refundCode);
					HashMap<String, OrderCancelApply> cancelApplyMap = new HashMap<String, OrderCancelApply>();
					HashMap<String, OrderCancelShipping> cancelShippingMap = new HashMap<String, OrderCancelShipping>();
					List<String> keyList = new ArrayList<>();
					for(OrderCancelApply item : list) {

						item.setClaimStatus("03");
						OrderCancelShipping cancelShipping = new OrderCancelShipping();

						cancelShipping.setShippingSequence(item.getShippingSequence());
						cancelShipping.setRePayShipping("Y");

						keyList.add(item.getClaimCode());
						cancelApplyMap.put(item.getClaimCode(), item);
						cancelShippingMap.put(Integer.toString(item.getShippingSequence()), cancelShipping);
					}

					String[] cancelIds = new String[keyList.size()];
					for(int i = 0; i < keyList.size(); i++) {
						cancelIds[i] = keyList.get(i);
					}


					claimApply.setOrderCode(adminClaimApply.getOrderCode());
					claimApply.setOrderSequence(adminClaimApply.getOrderSequence());
					claimApply.setCancelShippingMap(cancelShippingMap);
					claimApply.setCancelApplyMap(cancelApplyMap);
					claimApply.setCancelIds(cancelIds);
//					orderClaimApplyService.orderCancelProcess(claimApply);
					orderClaimApplyService.giveGoodsOrderCancelProcess(claimApply);		// 로직 확인 필요
				}

			}
		}

		redirectUrl += "/order/" + pageType + "/order-detail/" + adminClaimApply.getOrderSequence() + "/" + adminClaimApply.getOrderCode();
		return ViewUtils.redirect(redirectUrl, "처리되었습니다.");
	}

	private boolean isUpperAdmin() {
		if (SecurityUtils.hasRole("ROLE_ADMIN_1")
				|| SecurityUtils.hasRole("ROLE_ADMIN_2")
				|| SecurityUtils.hasRole("ROLE_ADMIN_3")
				|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			return true;
		}
		return false;
	}

	/**
	 * 모바일 신규주문 발송정보 임시 저장
	 * @return
	 */
	@PostMapping("mobile-temp-save/listUpdate/{mode}")
	public JsonView mobileTempSave(OrderParam orderParam,
			@PathVariable("mode") String mode, RequestContext requestContext) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		if (orderParam.getId() == null) {
			throw new OrderManagerException();
		}

		if (isUpperAdmin()) {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		orderParam.setConditionType("OPMANAGER");
		// 판매자 아이디 셋팅
		// 이상우 [2017-05-15 수정] 판매자 페이지에서 배송중 처리 시 getUser() null로 인해 else로 이동.
		String adminName = "";
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());

			if (SellerUtils.getSeller() != null) {
				adminName = SellerUtils.getSeller().getSellerName();
			}
		} else {
			if (UserUtils.getUser() != null) {
				adminName = UserUtils.getUser().getUserName();
			}
		}

		orderParam.setAdminUserName(adminName);

		int totalCount = orderParam.getId().length;
		int updateCount = 0;
		for(String key : orderParam.getId()) {

			String[] temp = StringUtils.delimitedListToStringArray(key, OrderItem.ITEM_KEY_DIVISION_STRING);

			if (temp.length != 3) {
				continue;
			}

			orderParam.setOrderCode(temp[0]);
			orderParam.setOrderSequence(Integer.parseInt(temp[1]));
			orderParam.setItemSequence(Integer.parseInt(temp[2]));

			OrderItem orderItem = orderService.getOrderItemByParam(orderParam);
			if (orderItem == null) {
				continue;
			}

			ShippingParam shippingParam = null;
			for(ShippingParam param : orderParam.getShippings()) {
				if (key.equals(param.getKey())) {
					shippingParam = param;
					break;
				}
			}

			if (shippingParam == null) {
				continue;
			}

			shippingParam.setOrderCode(orderParam.getOrderCode());
			shippingParam.setOrderSequence(orderParam.getOrderSequence());
			shippingParam.setItemSequence(orderParam.getItemSequence());
			shippingParam.setSellerId(orderParam.getSellerId());
			shippingParam.setAdminUserName(adminName);
			shippingParam.setConditionType(orderParam.getConditionType());

			try {
				orderService.shippingReadyListUpdate(mode, shippingParam, orderItem, orderParam);
				updateCount++;
			} catch (OrderException e) {
				log.error("orderService.shippingReadyListUpdate(..) : {}", e.getErrorMessage(), e);
			}
		}

		// 상태 변경수와 적용수가 같은가?
		if (totalCount != updateCount) {
			String errorMessage = "요청하신건중에 저장이 되지 않은 것이 있습니다.\n(요청 : " + NumberUtils.formatNumber(totalCount, "#,###") + ", 처리 : " + NumberUtils.formatNumber(updateCount, "#,###") + ")";
			return JsonViewUtils.failure(errorMessage);
		}

		return JsonViewUtils.success("수정되었습니다.");
	}

	private String getAuth() {
		if (SecurityUtils.hasRole("ROLE_ADMIN_1")
				|| SecurityUtils.hasRole("ROLE_ADMIN_2")
				|| SecurityUtils.hasRole("ROLE_ADMIN_3")
				|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			return "SYSTEM";
		} else if (SellerUtils.isSellerLogin()) {
			return "SELLER";
		}
		return "";
	}

	/**
	 * 주문취소 리스트
	 * @param claimApplyParam
	 * @param model
	 * @return
	 */
	@PostMapping("cancel/list")
	public String cancelListPost(@ModelAttribute ClaimApplyParam claimApplyParam, Model model) {
		if (ObjectUtils.isEmpty(claimApplyParam.getSearchStartDate()) || ObjectUtils.isEmpty(claimApplyParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			claimApplyParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			claimApplyParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setConditionType("SELLER");
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
		}

		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			// 지자체관리자일 경우 지자체코드 필요
			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		model.addAttribute("list", orderClaimApplyService.getCancelListByParam(claimApplyParam));
		model.addAttribute("pagination", claimApplyParam.getPagination());
		model.addAttribute("totalCount", claimApplyParam.getPagination().getTotalItems());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		return "view:/order/list/cancel";
	}

	/**
	 * 구매확정 목록
	 * @return
	 */
	@PostMapping("confirm")
	public String confirmPost(@ModelAttribute OrderParam orderParam, Model model) {
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
//		} else if (SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//			LocalDate startDate = LocalDate.parse(orderParam.getSearchStartDate(), formatter);
//			LocalDate endDate = LocalDate.parse(orderParam.getSearchEndDate(), formatter);
//
//			if (startDate.plusDays(30).isBefore(endDate)) {
//				return ViewUtils.redirect("/opmanager/order/confirm", "엑셀 다운로드 기능으로 인해 최대 31일 간격까지 조회가능합니다.");
//			}
		}

		model.addAttribute("list", orderService.getConfirmListByParam(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/confirm";
	}

	/**
	 * 주문교환 리스트
	 * @param claimApplyParam
	 * @param model
	 * @return
	 */
	@PostMapping("exchange/list")
	public String exchangeListPost(@ModelAttribute ClaimApplyParam claimApplyParam, Model model) {
		if (ObjectUtils.isEmpty(claimApplyParam.getSearchStartDate()) || ObjectUtils.isEmpty(claimApplyParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			claimApplyParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			claimApplyParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setConditionType("SELLER");
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
			claimApplyParam.setClaimStatus(new String[] {"01", "10", "11", "03", "99"});
		}

		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			// 지자체관리자일 경우 지자체코드 필요
			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		model.addAttribute("list", orderClaimApplyService.getExchangeListByParam(claimApplyParam));
		model.addAttribute("pagination", claimApplyParam.getPagination());
		model.addAttribute("totalCount", claimApplyParam.getPagination().getTotalItems());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		return "view:/order/list/exchange";
	}

	/**
	 * 배송완료 목록
	 * @return
	 */
	@PostMapping("finish")
	public String finishPost(@ModelAttribute OrderParam orderParam, Model model) {
		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		orderParam.setStatusType("finish");

		HttpSession session = RequestContextUtils.getSession();
		session.setAttribute("reOrderParam", orderParam);

		model.addAttribute("list", orderService.getShippingFinishListByParam(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return "view:/order/list/finish";
	}

}
