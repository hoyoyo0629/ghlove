package saleson.shop.orderagency;

import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.common.enumeration.IdType;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.deliverycompany.DeliveryCompanyService;
import saleson.shop.order.OrderService;
import saleson.shop.order.claimapply.OrderClaimApplyService;
import saleson.shop.order.claimapply.support.ClaimApplyParam;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.support.OrderException;
import saleson.shop.order.support.OrderParam;
import saleson.shop.orderagency.domain.OrderAgencyOrderListInfo;
import saleson.shop.orderagency.domain.OrderAgentOrderData;
import saleson.shop.user.LocgovService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/opmanager/order-agency")
@RequestProperty(layout="default")
public class OrderAgencyController {
	
	@Autowired
	private OrderAgencyService orderAgencyService;
	
	@Autowired
	private LocgovService locgovService;
	
	@Autowired
	private OrderClaimApplyService orderClaimApplyService;
	
	@Autowired
	private DeliveryCompanyService deliveryCompanyService;
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/order/list")
	public String agencyOrderList(OrderParam orderParam, Model model) {

		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate()) || ObjectUtils.isEmpty(orderParam.getSearchEndDate())) {
			orderParam.setSearchStartDate(DateUtils.getToday("yyyyMMdd"));
			orderParam.setSearchEndDate(DateUtils.getToday("yyyyMMdd"));
		}

		List<OrderAgencyOrderListInfo> list = orderAgencyService.selectOrderAgencyOrderList(orderParam);

		model.addAttribute("list", list);
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return ViewUtils.getView("/order-agency/order/list");
	}
	
	@PostMapping("/order/list")
	public String changeAgencyOrderList(OrderParam orderParam, Model model) {
		return agencyOrderList(orderParam, model);
	}
	


	/**
	 * 주문 상세 (종합적인 주문 내역을 보여준다)
	 * @param orderCode
	 * @param model
	 * @return
	 */
	@GetMapping("/order/list/order-detail/{orderCode}")
	public String detail(@PathVariable("orderCode") String orderCode, Model model) {

		try {
			setModelOrderDetail(model, orderCode, "all", 0, "");
		} catch (PageNotFoundException | OrderException e) {
			log.error(getClass().getName() + " detail error :: ", e);
			return ViewUtils.redirect("/order-agency/order/list", "주문 내역이 없습니다.");
		}

		return ViewUtils.getView("/order/detail/order-detail");
	}

	private void setModelOrderDetail(Model model, String orderCode, String pageType, int orderSequence, String mode) {
		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(orderCode);
		orderParam.setOrderSequence(orderSequence);

		orderParam.setConditionType("OPMANAGER");
		
		// 타 지자체 주문 조회해야해서 주석처리
//		if (ShopUtils.isSellerPage()) {
//			orderParam.setSellerId(SellerUtils.getSellerId());
//			orderParam.setConditionType("SELLER");
//		} else {
//			orderParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
//		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new PageNotFoundException();
		}

		bindItemDetail(model, orderParam, pageType);

		model.addAttribute("order", order);
		model.addAttribute("isSellerPage", ShopUtils.isSellerPage());

		model.addAttribute("mode", mode);
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
	 * 
	 * @param orderParam
	 * @param model
	 * @return
	 */
	@PostMapping("/order/list/getTempOrderInfoKey")
	public JsonView getTempOrderInfoKey(OrderParam orderParam) {
		orderParam.setConditionType("OPMANAGER");
		OrderItem orderItem = orderService.getOrderItemByParam(orderParam);
		OrderAgentOrderData orderAgentOrderData = new OrderAgentOrderData();
		orderAgentOrderData.setOrderCode(orderParam.getOrderCode());
		orderAgentOrderData.setUserId(orderItem.getUserId());
		orderAgentOrderData.setManagerId(UserUtils.getUser().getUserId());
		if (orderAgentOrderData.getManagerId() > 0) {
			return JsonViewUtils.success(orderAgencyService.selectOrderAgencyTempDataId(orderAgentOrderData));
		} else {
			return JsonViewUtils.failure("새로고침 후 다시 진행해주세요.");
		}
	}
	
	
	


}
