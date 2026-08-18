package saleson.shop.sellerconfirm;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import saleson.common.enumeration.IdType;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.shop.sellerconfirm.support.SellerconfirmParam;
import saleson.shop.log.ExceldownloadLogService;
import saleson.shop.order.claimapply.support.ClaimApplyParam;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.support.OrderParam;
import saleson.shop.remittance.RemittanceService;
import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.domain.RemittanceConfirm;
import saleson.shop.remittance.domain.RemittanceConfirmDetail;
import saleson.shop.remittance.domain.RemittanceDetail;
import saleson.shop.remittance.domain.RemittanceExpected;
import saleson.shop.remittance.support.*;
import saleson.shop.user.LocgovService;

//import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.*;
//import saleson.shop.sellerconfirm.support.SellerconfirmParam;

@Controller
@RequestMapping("/opmanager")
@RequestProperty(template="opmanager", layout="default")
public class SellerconfirmManagerController {
	private static final Logger log = LoggerFactory.getLogger(SellerconfirmManagerController.class);
	
	@Autowired
	private RemittanceService remittanceService;
	
	@Autowired
	private SellerconfirmService sellerconfirmService;
	
	@Autowired
	private SellerService sellerService;

//	@Autowired
//	private SequenceService sequenceService;

	@Autowired
	private LocgovService locgovService;
	
	@Autowired
	private ExceldownloadLogService exceldownloadLogService;
	
	
	/**
	 * 판매자 확인 필요건 리스트 - 정산
	 * @return
	 */
	@GetMapping("/sellerconfirm/list")
	//public String confirmList(SellerconfirmParam remittanceParam, Model model, RequestContext requestContext) {
	public String confirmList(SellerconfirmParam sellerconfirmParam, Model model, RequestContext requestContext) {
//		if (ShopUtils.isSellerPage()) {
//			sellerconfirmParam.setSellerId(SellerUtils.getSellerId());
//		} else {
//			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
//					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
//					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
//					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
//				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
//				if (!StringUtils.isEmpty(locgovCode)) {
//					sellerconfirmParam.setShLocgovCode(locgovCode);
//				} else {
//					sellerconfirmParam.setShLocgovCode("00000");
//				}
//			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
//						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
//						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
//						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
//				
//			} else {
//				sellerconfirmParam.setShLocgovCode("00000");
//			}
//		}
		
		model.addAttribute("queryString", requestContext.getQueryString());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("list", sellerconfirmService.getSellerconfirmConfirmListByParam(sellerconfirmParam));
		model.addAttribute("pagination", sellerconfirmParam.getPagination());
		model.addAttribute("totalCount", sellerconfirmParam.getPagination().getTotalItems());
		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		
		return "view:/sellerconfirm/list";
	}
	
	@PostMapping("/sellerconfirm/list")
	//public String confirmList(SellerconfirmParam remittanceParam, Model model, RequestContext requestContext) {
	public String confirmListPost(SellerconfirmParam sellerconfirmParam, Model model, RequestContext requestContext) {
		return confirmList(sellerconfirmParam,model,requestContext);
	}
	
	/**
	 * 판매자 확인 필요건 리스트 업데이트 - 정산내역
	 * @return
	 */
	@PostMapping("/sellerconfirm/confirm/list/updateNew")
	public String updateSellerconfirmPayProcess(SellerconfirmParam param, RequestContext requestContext) {

		if (!SecurityUtils.isManager()) {
			throw new PageNotFoundException();
		}

		long errCnt = 0;
		// 지자체 주/부 담당자만 처리
		if (SecurityUtils.hasRole("ROLE_ADMIN_5")
				|| SecurityUtils.hasRole("ROLE_ADMIN_6")
				|| SecurityUtils.hasRole("ROLE_ADMIN_7")
				|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
			if (param.getId() != null && param.getId().length > 0) {
				for(String remittanceId : param.getId()) {
					SellerconfirmParam data = new SellerconfirmParam();
					data.setLocgovCode(locgovCode);
					data.setRemittanceId(Long.valueOf(remittanceId));
					try {
						sellerconfirmService.updateSellerconfirmPayProcess(data);
					} catch (RemittanceException e) {
//						log.error("ERROR: {}", e.getMessage(), e);
//						return ViewUtils.redirect(e.getRedirectUrl(), e.getMessage());
						errCnt++;
					}
				}
			}
		}
		String redirect = "/opmanager/sellerconfirm/list";
		//String redirect = "/seller/remittance/confirm/list";
		//if (SecurityUtils.isManager()) {
			//redirect = "/opmanager/sellerconfirm/list";
		//}
		if (!ObjectUtils.isEmpty(requestContext.getQueryString())) {
			redirect += "?" + requestContext.getQueryString();
		}
		if (errCnt > 0) {
			return ViewUtils.redirect(redirect, "처리 중 " + errCnt + "건이 문제가 발생했습니다.");
		}
		return ViewUtils.redirect(redirect, "수정되었습니다.");
	}
	
	/**
	 * 판매자 확인 필요건 리스트 - 주문내역
	 * @return
	 */
	@GetMapping("/sellerconfirmOrder/list")
	public String list(@ModelAttribute OrderParam orderParam, Model model) {

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CREATED_DATE");
		}

		if (ObjectUtils.isEmpty(orderParam.getSearchStartDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
		}

		model.addAttribute("list", sellerconfirmService.getAllSellerConfirmOrderListByParamForManager(orderParam));
		model.addAttribute("pagination", orderParam.getPagination());
		model.addAttribute("totalCount", orderParam.getPagination().getTotalItems());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return "view:/order/list/all";
	}
	
	/**
	 * 정산 확정 내역 상세 - Item
	 * @return
	 */
	@GetMapping("/sellerconfirm/confirm/detailNew/{viewType}/{remittanceId}")
	public String sellerConfirmDetailNew(@PathVariable("remittanceId") long remittanceId,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		
		if (!SecurityUtils.isLogin()) {
			throw new PageNotFoundException();
		}
		
		remittanceParam.setRemittanceId(remittanceId);
		
//		List<RemittanceConfirmDetail> list = remittanceService.getRemittanceConfirmDetailListByParam(remittanceParam);
		List<RemittanceConfirmDetail> list = remittanceService.getRemittanceConfirmDetailListByParamNew(remittanceParam);
		
		Remittance remittance = remittanceService.getRemittanceInfoById(remittanceId);
		
		Seller seller = sellerService.getSellerById(remittance.getSellerId());
		
		if (seller == null) {
			throw new PageNotFoundException();
		}
//		remittanceParam.setStartDate(list.get(0).getRemittanceDate());
		
		model.addAttribute("seller", seller);
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("list", list);
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("remittanceStatusCode", remittance.getRemittanceStatusCode());

		return "view:/remittance/confirm/detail";

	}
	
	/**
	 * 주문 상세 (종합적인 주문 내역을 보여준다)
	 * @param orderCode
	 * @param model
	 * @return
	 */
//	@GetMapping("{pageType}/order-detail/{orderSequence}/{orderCode}")
//	public String detail(@PathVariable("orderCode") String orderCode,
//			@PathVariable("pageType") String pageType,
//			@PathVariable("orderSequence") int orderSequence, Model model) {
//
//		setModelOrderDetail(model, orderCode, pageType, orderSequence, "");
//
//		return "view:/order/detail/order-detail";
//	}
	
//	private void setModelOrderDetail(Model model, String orderCode, String pageType, int orderSequence, String mode) {
//		OrderParam orderParam = new OrderParam();
//		orderParam.setOrderCode(orderCode);
//		orderParam.setOrderSequence(orderSequence);
//
//		orderParam.setConditionType("OPMANAGER");
//		if (ShopUtils.isSellerPage()) {
//			orderParam.setSellerId(SellerUtils.getSellerId());
//			orderParam.setConditionType("SELLER");
//		}
//
//		Order order = sellerconfirmService.getOrderByParam(orderParam);
//		if (order == null) {
//			throw new PageNotFoundException();
//		}
//
//		bindItemDetail(model, orderParam, pageType);
//
//		model.addAttribute("order", order);
//		model.addAttribute("isSellerPage", ShopUtils.isSellerPage());
//
//		model.addAttribute("mode", mode);
//	}
	
	/**
	 * 주문상세에 상품관련 정보를 조회 - 클레임
	 * @param model
	 * @param orderParam
	 */
//	private void bindItemDetail(Model model, OrderParam orderParam, String pageType) {
//
//		ClaimApplyParam claimApplyParam = new ClaimApplyParam();
//		claimApplyParam.setOrderCode(orderParam.getOrderCode());
//		claimApplyParam.setOrderSequence(orderParam.getOrderSequence());
//
//		claimApplyParam.setConditionType("OPMANAGER");
//		if (ShopUtils.isSellerPage()) {
//			claimApplyParam.setConditionType("SELLER");
//			claimApplyParam.setSellerId(SellerUtils.getSellerId());
//		}
//
//		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
//			// 지자체관리자일 경우 지자체코드 필요
//			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
//		}
//
//		model.addAttribute("activeExchanges", orderClaimApplyService.getActiveExchangeListByParam(claimApplyParam));
//		model.addAttribute("exchangeHistorys", orderClaimApplyService.getExchangeHistoryListByParam(claimApplyParam));
//
//		model.addAttribute("activeCancels", orderClaimApplyService.getActiveCancelListByParam(claimApplyParam));
//		model.addAttribute("cancelHistorys", orderClaimApplyService.getCancelHistoryListByParam(claimApplyParam));
//
//		model.addAttribute("activeReturns", orderClaimApplyService.getActiveReturnListByParam(claimApplyParam));
//		model.addAttribute("returnHistorys", orderClaimApplyService.getReturnHistoryListByParam(claimApplyParam));
//
//		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());
//
//		model.addAttribute("cancelClaimReasons", CodeUtils.getCodeInfoList("CANCEL_REASON"));	// 취소사유
//		model.addAttribute("exchangeClaimReasons", CodeUtils.getCodeInfoList("EXCHANGE_REASON"));	// 교환사유
//		model.addAttribute("returnClaimReasons", CodeUtils.getCodeInfoList("RETURN_REASON"));	// 환불사유
//
//		String viewTabIndex = "0";
//		if ("cancel".equals(pageType)) {
//			viewTabIndex = "1";
//		} else if ("return".equals(pageType)) {
//			viewTabIndex = "2";
//		} else if ("exchange".equals(pageType)) {
//			viewTabIndex = "3";
//		}
//
//		model.addAttribute("pageType", pageType);
//		model.addAttribute("viewTabIndex", viewTabIndex);
//		model.addAttribute("orderLogs", orderClaimApplyService.getOrderLogList(claimApplyParam));
//		// 주문 전체 사은품 조회
//		//model.addAttribute("orderGiftItems", orderGiftItemService.getOrderGiftItemListByOrderCode(orderParam.getOrderCode()));
//
//	}
//	
//	private boolean isUpperAdmin() {
//		if (SecurityUtils.hasRole("ROLE_ADMIN_1")
//				|| SecurityUtils.hasRole("ROLE_ADMIN_2")
//				|| SecurityUtils.hasRole("ROLE_ADMIN_3")
//				|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
//			return true;
//		}
//		return false;
//	}
}
