package saleson.shop.order.claimapply;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringEscapeUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CommonUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.common.Const;
import saleson.common.alimtalk.Alimtalk;
import saleson.common.alimtalk.AlimtalkService;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.CancelClaimStatus;
import saleson.common.enumeration.CashbillStatus;
import saleson.common.enumeration.GiveGoodsOrderStatus;
import saleson.common.enumeration.IdType;
import saleson.common.enumeration.OrderLogType;
import saleson.common.enumeration.SmsType;
import saleson.common.nuri2.Nuri2NrmsgData;
import saleson.common.nuri2.Nuri2Service;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.utils.PaycoUtils;
import saleson.common.utils.PointUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.CashbillIssue;
import saleson.model.ConfigPg;
import saleson.model.OrderGiftItem;
import saleson.seller.main.SellerMapper;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.seller.user.SellerUserService;
import saleson.shop.coupon.CouponMapper;
import saleson.shop.coupon.domain.OrderCoupon;
import saleson.shop.deliverycompany.DeliveryCompanyService;
import saleson.shop.deliverycompany.domain.DeliveryCompany;
import saleson.shop.item.domain.Item;
import saleson.shop.naverpay.NaverPaymentApi;
import saleson.shop.order.OrderMapper;
import saleson.shop.order.OrderService;
//import saleson.shop.order.addpayment.OrderAddPaymentMapper;
import saleson.shop.order.addpayment.OrderAddPaymentService;
import saleson.shop.order.addpayment.domain.OrderAddPayment;
import saleson.shop.order.claimapply.domain.ClaimApply;
import saleson.shop.order.claimapply.domain.OrderCancelApply;
import saleson.shop.order.claimapply.domain.OrderCancelShipping;
import saleson.shop.order.claimapply.domain.OrderExchangeApply;
import saleson.shop.order.claimapply.domain.OrderLog;
import saleson.shop.order.claimapply.domain.OrderReturnApply;
import saleson.shop.order.claimapply.support.ClaimApplyParam;
import saleson.shop.order.claimapply.support.ClaimException;
import saleson.shop.order.claimapply.support.ExchangeApply;
import saleson.shop.order.claimapply.support.ReturnApply;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.domain.OrderPayment;
import saleson.shop.order.domain.OrderPgData;
import saleson.shop.order.domain.OrderShipping;
import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.order.domain.OrderSupporter;
import saleson.shop.order.giftitem.OrderGiftItemService;
import saleson.shop.order.givepoint.OrderGivePointService;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.order.infra.ClaimApplyParamEncryptor;
import saleson.shop.order.infra.OrderCancelApplyEncryptor;
import saleson.shop.order.infra.OrderExchangeApplyEncryptor;
import saleson.shop.order.infra.OrderParamEncryptor;
import saleson.shop.order.infra.OrderReturnApplyEncryptor;
import saleson.shop.order.payment.OrderPaymentMapper;
import saleson.shop.order.pg.PgService;
import saleson.shop.order.pg.config.ConfigPgService;
import saleson.shop.order.refund.OrderRefundService;
import saleson.shop.order.refund.domain.OrderRefund;
import saleson.shop.order.refund.domain.OrderRefundDetail;
import saleson.shop.order.refund.support.OrderRefundParam;
import saleson.shop.order.shipping.OrderShippingMapper;
import saleson.shop.order.shipping.support.ShippingParam;
import saleson.shop.order.support.OrderException;
import saleson.shop.order.support.OrderParam;
import saleson.shop.point.PointService;
import saleson.shop.point.domain.Point;
import saleson.shop.receipt.ReceiptService;
import saleson.shop.receipt.support.CashbillIssueRepository;
import saleson.shop.receipt.support.CashbillParam;
//import saleson.shop.receipt.support.CashbillRepository;
import saleson.shop.shipmentreturn.ShipmentReturnMapper;
import saleson.shop.shipmentreturn.domain.ShipmentReturn;
import saleson.shop.shipmentreturn.support.ShipmentReturnParam;
import saleson.shop.user.LocgovService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.SellerUser;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserDetailEncryptor;

@Slf4j
@RequiredArgsConstructor
@Service("orderClaimApplyService")
public class OrderClaimApplyServiceImpl extends EgovAbstractServiceImpl implements OrderClaimApplyService {
	private final OrderClaimApplyMapper orderClaimApplyMapper;
	private final DeliveryCompanyService deliveryCompanyService;
	private final OrderShippingMapper orderShippingMapper;
	private final OrderService orderService;
	private final OrderRefundService orderRefundService;
	private final OrderAddPaymentService orderAddPaymentService;
	private final ShipmentReturnMapper shipmentReturnMapper;
	private final SellerMapper sellerMapper;
	private final OrderMapper orderMapper;
	private final PointService pointService;
	private final OrderPaymentMapper orderPaymentMapper;
	private final CouponMapper couponMapper;
//    private final CashbillRepository cashbillRepository;
    private final CashbillIssueRepository cashbillIssueRepository;
    private final ReceiptService receiptService;
//	private final OrderAddPaymentMapper orderAddPaymentMapper;
	private final OrderGiftItemService orderGiftItemService;
    private final ConfigPgService configPgService;
    private final NaverPaymentApi naverPaymentApi;
	private final OrderExchangeApplyEncryptor orderExchangeApplyEncryptor;
	private final OrderReturnApplyEncryptor orderReturnApplyEncryptor;
	private final OrderCancelApplyEncryptor orderCancelApplyEncryptor;
	private final ClaimApplyParamEncryptor claimApplyParamEncryptor;


	// @Qualifier("inicisService")
	private final PgService inicisService;
	// @Qualifier("lgDacomService")
	private final PgService lgDacomService;
	// @Qualifier("cjService")
//	private final PgService cjService;
	// @Qualifier("paycoService")
	private final PgService paycoService;
	// @Qualifier("kakaopayService")
	private final PgService kakaopayService;
	// @Qualifier("kspayService")
	private final PgService kspayService;
	// @Qualifier("kcpService")
	private final PgService kcpService;
	// @Qualifier("easypayService")
	private final PgService easypayService;
	// @Qualifier("nicepayService")
	private final PgService nicepayService;

	private final OrderParamEncryptor orderParamEncryptor;
	private final OrderSupporter orderSupporter;

	private final OrderGivePointService orderGivePointService;
	@Autowired
	private LocgovService locgovService;	// 지자체 관리

	@Autowired
	private SmsIpsService smsIpsService;

	private final UserService userService;

	@Autowired
	private UserDetailEncryptor userDetailEncryptor;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private SellerUserService sellerUserService;

	@Autowired
	private AlimtalkService alimtalkService;

	@Autowired
	private Nuri2Service nuri2Service;

	@Override
	public OrderExchangeApply getExchangeApplyByClaimCode(String claimCode) {
		OrderExchangeApply orderExchangeApply = orderClaimApplyMapper.getExchangeApplyByClaimCode(claimCode);

		if (orderExchangeApply != null) {
			orderExchangeApply.decrypt(orderExchangeApplyEncryptor, ShopUtils.needMasking());
		}

		return orderExchangeApply;
	}

	@Override
	public OrderReturnApply getReturnApplyByClaimCode(String claimCode) {
		OrderReturnApply orderReturnApply = orderClaimApplyMapper.getReturnApplyByClaimCode(claimCode);
		if (orderReturnApply != null) {
			orderReturnApply.decrypt(orderReturnApplyEncryptor, ShopUtils.needMasking());
		}

		return orderReturnApply;
	}

	@Override
	public List<OrderReturnApply> getReturnHistoryListByParam(ClaimApplyParam claimApplyParam) {
		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
			claimApplyParam.setConditionType("SELLER");
		}

		List<OrderReturnApply> list = orderClaimApplyMapper.getReturnHistoryListByParam(claimApplyParam);

		setOrderItemOtherForReturnApply(list);

		return list;
	}

	@Override
	public List<OrderCancelApply> getCancelHistoryListByParam(ClaimApplyParam claimApplyParam) {

		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
			claimApplyParam.setConditionType("SELLER");
		}

		List<OrderCancelApply> list = orderClaimApplyMapper.getCancelHistoryListByParam(claimApplyParam);

		setOrderItemOtherForCancelApply(list);

		return list;
	}

	@Override
	public List<OrderCancelApply> getCancelListByParam(ClaimApplyParam claimApplyParam) {
		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				claimApplyParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				claimApplyParam.setShLocgovCode(locgovCode);
			}
		}

		claimApplyParam.encrypt(claimApplyParamEncryptor);

		int totalCount = orderClaimApplyMapper.getCancelCountByParam(claimApplyParam);

		//if (claimApplyParam.getItemsPerPage() == 10) {
		//	claimApplyParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, claimApplyParam.getItemsPerPage());
		claimApplyParam.setPagination(pagination);
		claimApplyParam.setLanguage(CommonUtils.getLanguage());

		List<OrderCancelApply> list = orderClaimApplyMapper.getCancelListByParam(claimApplyParam);

		if (list != null) {

			list.forEach(oca -> {
				oca.decrypt(orderCancelApplyEncryptor, ShopUtils.needMasking());
			});

		}

		setOrderItemOtherForCancelApply(list);
		claimApplyParam.decrypt(claimApplyParamEncryptor);

		return list;

	}

	@Override
	public List<OrderReturnApply> getReturnListByParam(ClaimApplyParam claimApplyParam) {

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				claimApplyParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				claimApplyParam.setShLocgovCode(locgovCode);
			}
		}
		claimApplyParam.encrypt(claimApplyParamEncryptor);
		int totalCount = orderClaimApplyMapper.getReturnCountByParam(claimApplyParam);

		//if (claimApplyParam.getItemsPerPage() == 10) {
		//	claimApplyParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, claimApplyParam.getItemsPerPage());
		claimApplyParam.setPagination(pagination);
		claimApplyParam.setLanguage(CommonUtils.getLanguage());

		List<OrderReturnApply> list = orderClaimApplyMapper.getReturnListByParam(claimApplyParam);

		if (list != null) {
			list.forEach(ora -> {
				ora.decrypt(orderReturnApplyEncryptor, ShopUtils.needMasking());
			});
		}

		setOrderItemOtherForReturnApply(list);
		claimApplyParam.decrypt(claimApplyParamEncryptor);

		return list;

	}

	@Override
	public List<OrderExchangeApply> getExchangeListByParam(ClaimApplyParam claimApplyParam) {

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				claimApplyParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				claimApplyParam.setShLocgovCode(locgovCode);
			}
		}
		claimApplyParam.encrypt(claimApplyParamEncryptor);
		int totalCount = orderClaimApplyMapper.getExchangeCountByParam(claimApplyParam);

		//if (claimApplyParam.getItemsPerPage() == 10) {
		//	claimApplyParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, claimApplyParam.getItemsPerPage());
		claimApplyParam.setPagination(pagination);
		claimApplyParam.setLanguage(CommonUtils.getLanguage());

		List<OrderExchangeApply> list = orderClaimApplyMapper.getExchangeListByParam(claimApplyParam);

		if (list != null) {
			list.forEach(oea -> {
				oea.decrypt(orderExchangeApplyEncryptor, ShopUtils.needMasking());
			});
		}

		setOrderItemOtherForExchangeApply(list);
		claimApplyParam.decrypt(claimApplyParamEncryptor);

		return list;

	}

	@Override
	public void insertOrderReturnApply(ReturnApply returnApply) {

		OrderParam orderParam = new OrderParam(returnApply);

		if (UserUtils.isUserLogin()) {
			orderParam.setUserId(UserUtils.getUserId());
		} else if (UserUtils.isGuestLogin()) {

		} else {
			throw new PageNotFoundException();
		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new OrderException();
		}

		OrderItem orderItem = orderMapper.getOrderItemByParam(orderParam);
		if (orderItem == null) {
			throw new OrderException();
		} else if (!"Y".equalsIgnoreCase(orderItem.getItemReturnFlag())) {		// 환불 불가 주문건
			throw new OrderException("반품 불가능한 주문건입니다.");
		} else {
			// 세트상품
			OrderParam orderSetParam = new OrderParam();
			orderSetParam.setOrderCode(orderItem.getOrderCode());
			orderSetParam.setOrderSequence(orderItem.getOrderSequence());
			orderSetParam.setSetItemSequence(orderItem.getItemSequence());
			orderItem.setItemSets(orderMapper.getOrderItemSetList(orderSetParam));
		}

		// CJH 2016.11.01 환불 신청시 주문 수량보다 신청수량이 더 많은경우 에러
		if (orderItem.getOrderQuantity() < returnApply.getApplyQuantity() + orderItem.getClaimQuantity()) {
			throw new OrderException("환불을 신청하실수 없습니다.");
		}

		orderParam.setShippingInfoSequence(orderItem.getShippingInfoSequence());

		//CJH 2016.10.09 화면에서 환불 신청정보를 넘겨받음
		//returnApply.setReturnApply(orderMapper.getOrderShippingInfoByParam(orderParam));

		OrderReturnApply detail = new OrderReturnApply(orderItem, returnApply);

		ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();
		shipmentReturnParam.setShipmentReturnId(returnApply.getShipmentReturnId());
		shipmentReturnParam.setItemId(orderItem.getItemId());
		shipmentReturnParam.setSellerId(orderItem.getSellerId());

//		ShipmentReturn shipmentReturn = shipmentReturnMapper.getShipmentReturnByParam(shipmentReturnParam);

		ShipmentReturn shipmentReturn;
		if (shipmentReturnParam.getShipmentReturnId() == 0
				&& shipmentReturnParam.getItemId() == 0
				&& shipmentReturnParam.getSellerId() == 0){
			shipmentReturn = null;
		} else {
			shipmentReturn = shipmentReturnMapper.getShipmentReturnByParam(shipmentReturnParam);
		}

		if (shipmentReturn == null) {
			throw new OrderException("반송지 정보가 잘못되었습니다. 판매자에게 문의해 주십시오");
		}

		// 데이터 암호화
		orderParam.encrypt(orderParamEncryptor);

		//환불정보업데이트
		orderMapper.updateOrderReturnInfo(orderParam);

		// 1 : 본사반송, 2 업체 반송
//		long sellerId = "2".equals(orderItem.getShipmentReturnType()) ? orderItem.getSellerId() : SellerUtils.DEFAULT_OPMANAGER_SELLER_ID;
		long sellerId = orderItem.getSellerId();

		detail.setShipmentReturnId(shipmentReturn.getShipmentReturnId());
		detail.setShipmentReturnSellerId(sellerId);

		// 주문 수량과 신청수량이 같으면 기존 주문 변경
		boolean isEqualQuantity = orderItem.getQuantity() == detail.getClaimApplyQuantity();
		if (isEqualQuantity) {
			insertOrderReturnApply(detail);
			orderClaimApplyMapper.updateClaimQuantityForReturnApply(detail);
		} else {
			orderClaimApplyMapper.copyOrderItemForReturnApply(detail);
			orderClaimApplyMapper.updateOrderItemQuantityForReturn(detail);

			// 중요!!
			detail.setItemSequence(detail.getCopyItemSequence());
			insertOrderReturnApply(detail);

		}

		// ====== 세트상품 수량 설정 ======================
		if (orderItem.getItemSets() != null && !orderItem.getItemSets().isEmpty()) {
			int copySetItemSequence = orderClaimApplyMapper.getMaxSetItemSequence(orderParam);
			detail.setSetClaimCode(detail.getClaimCode());
			detail.setCopySetItemSequence(copySetItemSequence);

			for (OrderItem itemSet : orderItem.getItemSets()) {
				detail.setSetItemSequence(itemSet.getSetItemSequence());
				detail.setItemSequence(itemSet.getItemSequence());
				detail.setClaimApplyQuantity(itemSet.getQuantity());

				// 주문 수량과 신청수량이 같으면 기존 주문 변경
				if (isEqualQuantity) {
					orderClaimApplyMapper.insertOrderSetReturnApply(detail);
					orderClaimApplyMapper.updateSetClaimQuantityForReturnApply(detail);
				} else {
					// 부분취소 시 row 추가 생성
					orderClaimApplyMapper.copyOrderItemSetForReturnApply(detail);

					detail.setSetItemSequence(copySetItemSequence);
					orderClaimApplyMapper.insertOrderSetReturnApply(detail);
				}
			}
		}

		// 국민비서 알림 전송 (답례품 환불 접수) - 구매자
		try {
			User user = userService.getUserByUserId(UserUtils.getUserId());
			UserDetail userDetail = (UserDetail) user.getUserDetail();
			userDetail.decrypt(userDetailEncryptor, false);
			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {
				List<ReceiverInfo> receiverInfos = new ArrayList<>();
				ReceiverInfo receiverInfo = new ReceiverInfo();
				receiverInfo.setSmsType(SmsType.PRESENT_REFUND_REGISTER);
				receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
				StringBuilder sb = new StringBuilder();
				sb.append(user.getUserName());	// 이름
				sb.append("|");
				sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
				sb.append("|");
				sb.append(orderItem.getOrderCode());	// 주문번호
				sb.append("|");
				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));		// 답례품명
				if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
					sb.append("(");
					sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
					sb.append(")");
				}
				sb.append("|");
				sb.append(ObjectUtils.isEmpty(detail.getReturnReasonDetail()) ? detail.getReturnReasonText() : detail.getReturnReasonDetail());		// 사유
				sb.append("|");
				sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
				receiverInfo.setSndngCntnts(sb.toString());
				receiverInfos.add(receiverInfo);
				smsIpsService.insertTifIpsSndngM(receiverInfos);
			}
		} catch (NullPointerException | ClassCastException e) {
			log.error(getClass().getName() +  " :: insertOrderReturnApply send sms error", e);
		}

		// 국민비서 알림 전송 (답례품 환불 접수) - 판매자
		try {
			User user = userService.getUserByUserId(UserUtils.getUserId());

			Seller seller = sellerService.getSellerById(orderItem.getSellerId());
			SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdForSms(seller.getLoginId());
			if ("0".equals(sellerUser.getReceiveSms()) && StringUtils.hasLength(sellerUser.getPhoneNumber()) && StringUtils.hasLength(sellerUser.getMberCi())) {
				List<ReceiverInfo> receiverInfos = new ArrayList<>();
				ReceiverInfo receiverInfo = new ReceiverInfo();
				receiverInfo.setSmsType(SmsType.PRESENT_REFUND_REGISTER);
				receiverInfo.setPrvcIdntfcInfo(sellerUser.getMberCi());
				StringBuilder sb = new StringBuilder();
				sb.append(user.getUserName());	// 이름
				sb.append("|");
				sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
				sb.append("|");
				sb.append(orderItem.getOrderCode());	// 주문번호
				sb.append("|");
				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));		// 답례품명
				if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
					sb.append("(");
					sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
					sb.append(")");
				}
				sb.append("|");
				sb.append(ObjectUtils.isEmpty(detail.getReturnReasonDetail()) ? detail.getReturnReasonText() : detail.getReturnReasonDetail());		// 사유
				sb.append("|");
				sb.append(sellerUser.getPhoneNumber().replaceAll("-", ""));
				receiverInfo.setSndngCntnts(sb.toString());
				receiverInfos.add(receiverInfo);
				smsIpsService.insertTifIpsSndngM(receiverInfos);

//				/* 카카오 알림톡 전송 */
//				Alimtalk alimtalk = new Alimtalk();
//				alimtalk.setTemplateCode("026030000378");
//				alimtalk.setReceiverNum(sellerUser.getPhoneNumber());
//				alimtalk.setReceiverName(user.getUserName());
//				alimtalk.setOrderDate(alimtalk.getLocalDateTimeToStr());
//				alimtalk.setOrderNo(orderItem.getOrderCode());
//
//				StringBuilder orderItemName = new StringBuilder();
//				orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));
//				if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
//					orderItemName.append("(");
//					orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));
//					orderItemName.append(")");
//				}
//				alimtalk.setOrderName(orderItemName.toString());
//
//				alimtalk.setClaimReasonDetail(ObjectUtils.isEmpty(detail.getReturnReasonDetail()) ? detail.getReturnReasonText() : detail.getReturnReasonDetail());
//
//				alimtalkService.sendAlimtalk(alimtalk);

				/* 국자원 모바일 메신저 알림톡 전송 */
				Nuri2NrmsgData nuri2NrmsgData = new Nuri2NrmsgData();

				// 아이템(옵션)
				StringBuilder orderItemName = new StringBuilder();
				orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));
				if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
					orderItemName.append("(");
					orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));
					orderItemName.append(")");
				}

				// 템플릿코드
				nuri2NrmsgData.setAltTemplateCode("KR002");
				// 수신번호
				nuri2NrmsgData.setPhone(sellerUser.getPhoneNumber().replaceAll("-", ""));
				// ALT_JSON
				nuri2NrmsgData.setAltJson(nuri2NrmsgData.getContent("PRESENT_REFUND_REGISTER", user.getUserName(), receiverInfo.getLocalDateTimeToStr(), orderItem.getOrderCode(), orderItemName.toString(), ""));
				// XMS_TEST
				nuri2NrmsgData.setXmsText(nuri2NrmsgData.getContent("PRESENT_REFUND_REGISTER", user.getUserName(), receiverInfo.getLocalDateTimeToStr(), orderItem.getOrderCode(), orderItemName.toString(), ""));

				nuri2Service.insertAlimtalk(nuri2NrmsgData);
			}
		} catch (NullPointerException | ClassCastException e) {
			log.error(getClass().getName() +  " :: insertOrderReturnApply send sms error", e);
		}

		// 주문 로그
		orderService.insertOrderLog(
				OrderLogType.CLAIM_RETURN,
				detail.getOrderCode(),
				detail.getOrderSequence(),
				detail.getItemSequence(),
				orderItem.getOrderStatus()
		);
	}

	private void insertOrderReturnApply(OrderReturnApply detail) {
		// 데이터 암호화
		detail.encrypt(orderReturnApplyEncryptor);
		orderClaimApplyMapper.insertOrderReturnApply(detail);
	}

	@Override
	public void insertOrderCancelApply(ClaimApply claimApply) {

		ConfigPg configPg = configPgService.getConfigPg();
		String pgType = "";

		if (configPg != null) {
			pgType = configPg.getPgType().getCode().toLowerCase();
		} else {
			pgType = SalesonProperty.getPgService();
		}

		if (claimApply.getId() == null) {
			throw new OrderException();
		}

		String orderCode = claimApply.getOrderCode();
		int orderSequence = claimApply.getOrderSequence();

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(UserUtils.getUserId());
		orderParam.setOrderCode(claimApply.getOrderCode());
		orderParam.setOrderSequence(claimApply.getOrderSequence());

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new OrderException();
		}

		List<OrderItem> orderItemSets = orderMapper.getOrderItemSetList(orderParam);

		claimApply.setOrder(order);
		OrderRefund orderRefund = orderRefundService.getOrderCancelRefundForUser(claimApply);
		if (orderRefund == null) {
			throw new OrderException();
		}

		OrderRefundParam orderRefundParam = new OrderRefundParam();
		orderRefundParam.setOrderCode(orderCode);
		orderRefundParam.setOrderSequence(orderSequence);
		orderRefundParam.setBankInName(claimApply.getReturnBankInName());
		orderRefundParam.setBankName(claimApply.getReturnBankName());
		orderRefundParam.setVirtualNo(claimApply.getReturnVirtualNo());

		// 카드 전체취소일 경우 부분취소 가능여부(PART_CANCEL_FLAG)와 상관없이 PG취소
		boolean isCard = false;
		for (OrderPayment payment : orderRefund.getOrderPayments()) {
			// 1. 카드 결제 여부
			if ("card".equals(payment.getApprovalType())) {
				isCard = true;
			}
		}

		// 2. 총 취소신청 수량
		int claimApplyQuantity = 0;
 		for (OrderCancelApply orderCancelApply : claimApply.getOrderCancelApplys()) {
			claimApplyQuantity += orderCancelApply.getClaimApplyQuantity();
 		}

		// 3. 즉시취소 처리를 위한 flag - 총 취소신청 수량과 주문 수량이 같으면 즉시 취소
		boolean isCardAutoCancel = false;
 		if (isCard && claimApplyQuantity == orderRefund.getTotalOrderQuantity()) {
			isCardAutoCancel = true;
		}
/*
		// 환불 코드 따기 - 즉시환불일때는 무조건 신규 생성
		if ("1".equals(claimApply.getClaimRefundType())) {
			if (orderRefund.isAutoCancel() == false && isCardAutoCancel == false) {
				String refundCode = orderRefundService.getActiveRefundCodeByParam(orderRefundParam);
				if (ObjectUtils.isEmpty(refundCode)) {
					throw new OrderException();
				}

				claimApply.setRefundCode(refundCode);
			} else {
				orderRefundParam.setConditionType("REFUND_FINISH");
				String refundCode = orderRefundService.getNewRefundCodeByParam(orderRefundParam);
				if (ObjectUtils.isEmpty(refundCode)) {
					throw new OrderException();
				}

				claimApply.setRefundCode(refundCode);
			}
		}
*/
		orderParam.setReturnBankInName(orderRefundParam.getBankInName());
		orderParam.setReturnBankName(orderRefundParam.getBankName());
		if (ObjectUtils.isEmpty(claimApply.getReturnBankName()) == false) {
			List<CodeInfo> list = ShopUtils.getBankListByKey(pgType);
			for (CodeInfo code : list) {
				if (code.getKey().getId().equals(claimApply.getReturnBankName())) {
					orderParam.setReturnBankName(code.getLabel());
					break;
				}
			}
		}

		orderParam.setReturnVirtualNo(orderRefundParam.getVirtualNo());

		// 데이터 암호화
		orderParam.encrypt(orderParamEncryptor);

		// 환불정보업데이트
		orderMapper.updateOrderReturnInfo(orderParam);
		OrderItem orderItem = new OrderItem();
		OrderShipping orderShipping = new OrderShipping();
		List<Integer> returnCoupons = new ArrayList<>();

		HashMap<String, Integer> stockMap = new HashMap<>();

		// 세트상품 용
		HashMap<String, Integer> stockSetMap = new HashMap<>();

		for (OrderRefundDetail group : orderRefund.getGroups()) {

			for (OrderCancelApply orderCancelApply : group.getOrderCancelApplys()) {
				orderItem = orderCancelApply.getOrderItem();

				if ("1".equals(claimApply.getClaimRefundType()) && (orderRefund.isAutoCancel() || isCardAutoCancel)) {
					orderCancelApply.setRefundCode(claimApply.getRefundCode());
					orderCancelApply.setClaimStatus("04");

					if (orderItem.getCouponUserId() > 0) {
						returnCoupons.add(orderItem.getCouponUserId());
					}

					if (orderItem.getAddCouponUserId() > 0) {
						returnCoupons.add(orderItem.getAddCouponUserId());
					}

					// 재고 복원해야하는 목록 만들기
					orderService.makeStockRestorationMap(stockMap, orderItem, orderCancelApply.getClaimApplyQuantity());

					// 세트상품 재고 복원 목록
					if ("Y".equals(orderItem.getSetItemFlag()) && orderItemSets != null && !orderItemSets.isEmpty()) {
						orderItem.setItemSets(orderService.filterOrderItemSets(orderItemSets, orderItem.getOrderCode(), orderItem.getOrderSequence(), orderItem.getItemSequence()));

						for (OrderItem orderItemSet : orderItem.getItemSets()) {
							orderService.makeStockRestorationMap(stockSetMap, orderItemSet, orderItem.getQuantity() * orderItemSet.getQuantity());
						}
					}
				} else {
					orderCancelApply.setClaimStatus("01");
				}

				orderCancelApply.setCancelReason(claimApply.getClaimReason());
				orderCancelApply.setCancelReasonText(claimApply.getClaimReasonText());
				orderCancelApply.setCancelReasonDetail(claimApply.getClaimReasonDetail());
				orderCancelApply.setItemSequence(orderItem.getItemSequence());
				orderCancelApply.setClaimApplySubject("01");

				orderCancelApply.setClaimApplyQuantity(orderCancelApply.getClaimApplyQuantity());

				if ("1".equals(claimApply.getClaimRefundType()) && (orderRefund.isAutoCancel() || isCardAutoCancel)) {
					orderCancelApply.setSalesCancel(true);
				}

				// 잔여 수량이 0이라면..
				if (orderItem.getQuantity() == 0) {
					orderClaimApplyMapper.insertOrderCancelApply(orderCancelApply);
					orderClaimApplyMapper.updateClaimQuantityForCancelApply(orderCancelApply);
				} else {
					orderClaimApplyMapper.copyOrderItemForCancelApply(orderCancelApply);
					orderClaimApplyMapper.updateOrderItemQuantityForCancel(orderCancelApply);

					// 중요!!
					orderCancelApply.setItemSequence(orderCancelApply.getCopyItemSequence());
					orderClaimApplyMapper.insertOrderCancelApply(orderCancelApply);
				}

				// ====== 세트상품 수량 설정 ======================
				if (orderCancelApply.getOrderItem().getItemSets() != null && orderCancelApply.getOrderItem().getItemSets().size() > 0) {
					int copySetItemSequence = orderClaimApplyMapper.getMaxSetItemSequence(orderParam);
					orderCancelApply.setSetClaimCode(orderCancelApply.getClaimCode());
					orderCancelApply.setCopySetItemSequence(copySetItemSequence);

					for (OrderItem itemSet : orderCancelApply.getOrderItem().getItemSets()) {
						orderCancelApply.setSetItemSequence(itemSet.getSetItemSequence());
						orderCancelApply.setItemSequence(itemSet.getItemSequence());
						orderCancelApply.setClaimApplyQuantity(itemSet.getQuantity());

						if (orderItem.getQuantity() == 0) {
							orderClaimApplyMapper.insertOrderSetCancelApply(orderCancelApply);
							orderClaimApplyMapper.updateSetClaimQuantityForCancelApply(orderCancelApply);
						} else {
							// 부분취소 시 row 추가 생성
							orderClaimApplyMapper.copyOrderItemSetForCancelApply(orderCancelApply);

							orderCancelApply.setSetItemSequence(copySetItemSequence);
							orderClaimApplyMapper.insertOrderSetCancelApply(orderCancelApply);
						}
					}
				}
			}

			if ("1".equals(claimApply.getClaimRefundType()) && (orderRefund.isAutoCancel() || isCardAutoCancel)) {
				for(OrderAddPayment orderAddPayment : group.getOrderAddPayments()) {

					if ("NO_INSERT".equals(orderAddPayment.getIssueCode())) {
						continue;
					}

					orderAddPayment.setRefundCode(claimApply.getRefundCode());
					orderAddPaymentService.insertOrderAddPayment(orderAddPayment);

					orderShipping = orderAddPayment.getOrderShipping();
					orderShipping.setPayShipping(orderShipping.getRePayShippingAmount());
					orderShipping.setRemittanceAmount(orderShipping.getRePayShippingAmount());

					if ("2".equals(orderShipping.getAddPaymentType())) {
						orderShipping.setReturnShipping(orderShipping.getReturnShipping() + orderShipping.getAddPayAmount());
					}

					orderShippingMapper.updateCancelShipping(orderShipping);

				}
			}
		}

		if ("1".equals(claimApply.getClaimRefundType()) && (orderRefund.isAutoCancel() || isCardAutoCancel)) {

			// 포인트부터 취소
			int returnAmount = orderRefund.getTotalReturnAmount();
			for (OrderPayment payment : orderRefund.getOrderPayments()) {
				if (payment.getRemainingAmount() > 0) {

					if (PointUtils.isPointType(payment.getApprovalType())) {
                        int cancelAmount = returnAmount > payment.getRemainingAmount() ? payment.getRemainingAmount() : returnAmount;

						returnPoint(order, payment, cancelAmount);
						returnAmount = returnAmount - cancelAmount;

					}
				}

				if (returnAmount <= 0) {
					break;
				}
			}

			if (returnAmount > 0) {

				for (OrderPayment payment : orderRefund.getOrderPayments()) {

					if (PointUtils.isPointType(payment.getApprovalType())) {
						continue;
					}

					if (payment.getRemainingAmount() > 0) {

						// Mall 결제 타입 말고는 PG밖에 없겠지?
						OrderPgData orderPgData = payment.getOrderPgData();
						if (orderPgData.getOrderPgDataId() > 0) {

							boolean isSuccess = false;
							int cancelAmount = returnAmount > payment.getRemainingAmount() ? payment.getRemainingAmount() : returnAmount;

							// 취소 금액과 잔여액이 같으면 전체 취소
							boolean isPartCancel = cancelAmount == payment.getRemainingAmount() ? false : true;
							// Jun-Eu 2017.03.02 - 실시간계좌이체는 부분취소 기능 미지원
							if ("kspay".equals(orderPgData.getPgServiceType()) && !"N".equals(orderPgData.getPartCancelDetail())) {
								isPartCancel = true;
							} else if ("N".equals(orderPgData.getPartCancelFlag())) {
								isPartCancel = false;
							}

							if (("inicis".equals(orderPgData.getPgServiceType())	// CJH 2016.12.07 - 이니시스의 경우 부분취소를 한번하면 부분취소만 되는듯!!
									|| "kcp".equals(orderPgData.getPgServiceType())
									|| "nicepay".equals(orderPgData.getPgServiceType())
									|| "naverpay".equals(orderPgData.getPgServiceType())
									|| "lgdacom".equals(orderPgData.getPgServiceType()))
									&& "PART_CANCEL".equals(orderPgData.getPartCancelDetail())) {

								isPartCancel = true;
							}

							/*else if ("N".equals(orderPgData.getPartCancelFlag())) {
								isPartCancel = false;
							} */

							orderPgData.setMessage("주문취소");
							orderPgData.setCancelAmount(cancelAmount);
							orderPgData.setRemainAmount(payment.getRemainingAmount() - cancelAmount);

							if (isPartCancel == false) {

								// PG 취소 - 즉시 취소
								if ("inicis".equals(orderPgData.getPgServiceType())) {
									if ("vbank".equals(payment.getApprovalType())) {
										orderPgData.setReturnBankName(claimApply.getReturnBankName());
										orderPgData.setReturnAccountNo(claimApply.getReturnVirtualNo());
										orderPgData.setReturnName(claimApply.getReturnBankInName());
									}

									isSuccess = inicisService.cancel(orderPgData);
								} else if ("lgdacom".equals(orderPgData.getPgServiceType())) {
									if ("vbank".equals(payment.getApprovalType())) {
										orderPgData.setReturnBankName(claimApply.getReturnBankName());
										orderPgData.setReturnAccountNo(claimApply.getReturnVirtualNo());
										orderPgData.setReturnName(claimApply.getReturnBankInName());
									}
									isSuccess = lgDacomService.cancel(orderPgData);
								} else if ("kakaopay".equals(orderPgData.getPgServiceType())) {
									orderPgData.setRemainAmount(cancelAmount);
									isSuccess = kakaopayService.cancel(orderPgData);
								} else if ("payco".equals(orderPgData.getPgServiceType())) {
									orderPgData.setRemainAmount(cancelAmount);
									isSuccess = paycoService.cancel(orderPgData);
								} else if ("kspay".equals(orderPgData.getPgServiceType())) {
									isSuccess = kspayService.cancel(orderPgData);
								} else if ("kcp".equals(orderPgData.getPgServiceType())) {
									isSuccess = kcpService.cancel(orderPgData);
								} else if ("easypay".equals(orderPgData.getPgServiceType())) {
									if ("vbank".equals(payment.getApprovalType())) {
										orderPgData.setReturnBankName(claimApply.getReturnBankName());
										orderPgData.setReturnAccountNo(claimApply.getReturnVirtualNo());
										orderPgData.setReturnName(claimApply.getReturnBankInName());
									}
									isSuccess = easypayService.cancel(orderPgData);
								} else if ("nicepay".equals(orderPgData.getPgServiceType())) {
									if (!" ".equals(claimApply.getClaimReasonDetail())) {
										orderPgData.setCancelReason(claimApply.getClaimReasonDetail());
									} else {
										orderPgData.setCancelReason(claimApply.getClaimReasonText());
									}
									orderPgData.setRequest(claimApply.getRequest());
									orderPgData.setResponse(claimApply.getResponse());

									if ("vbank".equals(payment.getApprovalType())) {
										orderPgData.setReturnBankName(claimApply.getReturnBankName());
										orderPgData.setReturnAccountNo(claimApply.getReturnVirtualNo());
										orderPgData.setReturnName(claimApply.getReturnBankInName());
									}

									isSuccess = nicepayService.cancel(orderPgData);
                                } else if ("naverpay".equals(orderPgData.getPgServiceType())) {

                                    if (!" ".equals(claimApply.getClaimReasonDetail())) {
                                        orderPgData.setCancelReason(claimApply.getClaimReasonDetail());
                                    } else {
                                        orderPgData.setCancelReason(claimApply.getClaimReasonText());
                                    }
                                    orderPgData = naverPaymentApi.cancel(orderPgData, configPg);

                                    isSuccess = orderPgData.isSuccess();
                                }

							} else {

								if ("inicis".equals(orderPgData.getPgServiceType())) {

									if ("vbank".equals(payment.getApprovalType())) {
										orderPgData.setReturnBankName(claimApply.getReturnBankName());
										orderPgData.setReturnAccountNo(claimApply.getReturnVirtualNo());
										orderPgData.setReturnName(claimApply.getReturnBankInName());
									}

									orderPgData = inicisService.partCancel(orderPgData);
								} else if ("lgdacom".equals(orderPgData.getPgServiceType())) {
									if ("vbank".equals(payment.getApprovalType())) {
										orderPgData.setReturnBankName(claimApply.getReturnBankName());
										orderPgData.setReturnAccountNo(claimApply.getReturnVirtualNo());
										orderPgData.setReturnName(claimApply.getReturnBankInName());
									}
									orderPgData = lgDacomService.partCancel(orderPgData);
								} else if ("kakaopay".equals(orderPgData.getPgServiceType())) {
									orderPgData.setRemainAmount(cancelAmount);
									orderPgData = kakaopayService.partCancel(orderPgData);
								} else if ("payco".equals(orderPgData.getPgServiceType())) {
									orderPgData.setRemainAmount(cancelAmount);
									orderPgData.setPaycoCancelProducts(PaycoUtils.makePaycoCancelProducts(orderItem, returnAmount, orderShipping.getPayShipping(), orderPgData.getPgProcInfo()));
									orderPgData = paycoService.partCancel(orderPgData);
								} else if ("kspay".equals(orderPgData.getPgServiceType())) {
									orderPgData = kspayService.partCancel(orderPgData);
								} else if ("kcp".equals(orderPgData.getPgServiceType())) {
									orderPgData = kcpService.partCancel(orderPgData);
								} else if ("easypay".equals(orderPgData.getPgServiceType())) {
									if ("vbank".equals(payment.getApprovalType())) {
										orderPgData.setReturnBankName(claimApply.getReturnBankName());
										orderPgData.setReturnAccountNo(claimApply.getReturnVirtualNo());
										orderPgData.setReturnName(claimApply.getReturnBankInName());
									}
									orderPgData = easypayService.partCancel(orderPgData);

								} else if ("nicepay".equals(orderPgData.getPgServiceType())) {
									if ("vbank".equals(payment.getApprovalType())) {
										orderPgData.setReturnBankName(claimApply.getReturnBankName());
										orderPgData.setReturnAccountNo(claimApply.getReturnVirtualNo());
										orderPgData.setReturnName(claimApply.getReturnBankInName());
									}

									if (!" ".equals(claimApply.getClaimReasonDetail())) {
										orderPgData.setCancelReason(claimApply.getClaimReasonDetail());
									} else {
										orderPgData.setCancelReason(claimApply.getClaimReasonText());
									}

									orderPgData.setRequest(claimApply.getRequest());
									orderPgData.setResponse(claimApply.getResponse());
									orderPgData.setMessage("주문취소");
									orderPgData = nicepayService.partCancel(orderPgData);
                                } else if ("naverpay".equals(orderPgData.getPgServiceType())) {
                                    if (!" ".equals(claimApply.getClaimReasonDetail())) {
                                        orderPgData.setCancelReason(claimApply.getClaimReasonDetail());
                                    } else {
                                        orderPgData.setCancelReason(claimApply.getClaimReasonText());
                                    }
                                    orderPgData.setPartCancelDetail("PART_CANCEL");
                                    orderPgData = naverPaymentApi.cancel(orderPgData, configPg);
                                }

								isSuccess = orderPgData.isSuccess();

							}

							if (isSuccess) {

								payment.setRemainingAmount(orderPgData.getRemainAmount());
								payment.setCancelAmount(payment.getCancelAmount() + orderPgData.getCancelAmount());

                                if (("inicis".equals(orderPgData.getPgServiceType())
                                        || "kcp".equals(orderPgData.getPgServiceType())
                                        || "nicepay".equals(orderPgData.getPgServiceType())
                                        || "naverpay".equals(orderPgData.getPgServiceType())
										|| "lgdacom".equals(orderPgData.getPgServiceType()))
                                        && isPartCancel) {
                                    orderMapper.updateOrderPgData(orderPgData);
                                }

								orderPaymentMapper.updateOrderPaymentForCancel(payment);

								OrderPayment orderPayment = new OrderPayment(order, payment.getApprovalType());
								orderPayment.setPaymentType("2");
								orderPayment.setCancelAmount(orderPgData.getCancelAmount());
								orderPayment.setPayDate(DateUtils.getToday(Const.DATETIME_FORMAT));
								orderPayment.setNowPaymentFlag("Y");

								orderPaymentMapper.insertOrderPayment(orderPayment);

								// 현금영수증 취소,재발급
								if ("realtimebank".equals(payment.getApprovalType()) || "vbank".equals(payment.getApprovalType())) {
									receiptService.cashbillReIssue(orderParam);
								}
							} else {
								String errorMessage = "PG 취소 연동 에러";

								if (!ObjectUtils.isEmpty(orderPgData.getErrorMessage())) {
									errorMessage = orderPgData.getErrorMessage();
								}

								log.error("주문 취소 오류 PG사 응답 - (code:{}, message:{})", orderPgData.getPgAuthCode(), orderPgData.getErrorMessage());
								throw new OrderException(errorMessage, orderCode, orderSequence);
							}

							returnAmount = returnAmount - cancelAmount;
						}
					}

					if (returnAmount <= 0) {
						break;
					}
				}
			}

			// 쿠폰 반환
			if (!returnCoupons.isEmpty()) {
				for (Integer couponUserId : returnCoupons) {

					if (order.getUserId() > 0) {
						OrderCoupon orderCoupon = new OrderCoupon();
						orderCoupon.setUserId(order.getUserId());
						orderCoupon.setCouponUserId(couponUserId);
						couponMapper.updateCouponUserReturnsByOrderCouponUser(orderCoupon);
					}

				}
			}

			// 재고량 복원
			orderService.stockRestoration(stockMap);

			// 세트상품 재고량 복원
			orderService.stockRestoration(stockSetMap);
		}

		// 주문 로그
		OrderItem logOrderItem = new OrderItem();
		for(OrderRefundDetail group : orderRefund.getGroups()) {
			for (OrderCancelApply orderCancelApply : group.getOrderCancelApplys()) {
				logOrderItem = orderCancelApply.getOrderItem();

				orderService.insertOrderLog(
						OrderLogType.CLAIM_CANCEL,
						orderCancelApply.getOrderCode(),
						orderCancelApply.getOrderSequence(),
						logOrderItem.getItemSequence(),
						logOrderItem.getOrderStatus()
				);

			}
		}
	}

	/**
	 * 포인트 환불
	 * @param order
	 * @param payment
	 * @param amount
	 */
	private void returnPoint(Order order, OrderPayment payment, int amount) {
		Point point = new Point();
		point.setUserId(order.getUserId());
		point.setOrderCode(order.getOrderCode());
		point.setOrderSequence(order.getOrderSequence());
		point.setPoint(amount);
		point.setPointType(payment.getApprovalType());
		point.setReason(MessageUtils.getMessage("M00246") + " 환불 - 주문취소["+ order.getOrderCode() +"]");

		// 포인트 반환
		pointService.earnPoint("return", point);

		payment.setCancelAmount(amount);
		payment.setRemainingAmount(payment.getRemainingAmount()  - amount);

		orderPaymentMapper.updateOrderPaymentForCancel(payment);

		OrderPayment orderPayment = new OrderPayment();
		orderPayment.setPaymentType("2");
		orderPayment.setOrderCode(order.getOrderCode());
		orderPayment.setOrderSequence(order.getOrderSequence());
		orderPayment.setApprovalType(payment.getApprovalType());
		orderPayment.setCancelAmount(amount);
		orderPayment.setPayDate(DateUtils.getToday(Const.DATETIME_FORMAT));
		orderPayment.setDeviceType("WEB");

		if (ShopUtils.isMobilePage()) {
			orderPayment.setDeviceType("MOBILE");
		}

		orderPayment.setNowPaymentFlag("Y");
		orderPaymentMapper.insertOrderPayment(orderPayment);
	}



//	@Override
//	public void orderCancelProcess(ClaimApply claimApply) {
//
//		if (claimApply.getCancelIds() == null) {
//			return;
//		}
//
//		boolean rePayShipping = false;
//		int currentItemCount [] = {0,0};
//
//		for(String claimCode : claimApply.getCancelIds()) {
//			OrderCancelApply cancelApply = claimApply.getCancelApplyMap().get(claimCode);
//			if (cancelApply == null) {
//				throw new OrderException();
//			}
//
//			String claimStatus = cancelApply.getClaimStatus();
//			if ("03".equals(claimStatus)) {
//
//			    currentItemCount[cancelApply.getShippingSequence()] ++;
//
//				for(OrderCancelShipping orderCancelShipping : claimApply.getCancelShippingMap().values()) {
//                    int addPaymentCount = orderAddPaymentMapper.getOrderAddPaymentCount(cancelApply);
//                    int leftItem = orderMapper.getOrderItemCountForCancel(cancelApply);
//
//                    if (addPaymentCount > 0 && leftItem > currentItemCount[cancelApply.getShippingSequence()]) {
//                        rePayShipping = false;
//                        break;
//                    }
//
//					// 배송비 재계산??
//					if ("Y".equals(orderCancelShipping.getRePayShipping())) {
//						rePayShipping = true;
//						break;
//
//					}
//
//				}
//
//			}
//
//			if (rePayShipping) {
//				break;
//			}
//		}
//
//
//		if (rePayShipping == true) {
//
//			List<OrderShipping> orderShippings = getReShippingAmountForManager(claimApply);
//
//			/**
//			 * CJH 2016.08.20
//			 * 주문 취소는 무조건!! 정산일자가 확정되기 전에 일어나는일이라고 가정하고 작성한다..
//			 * 배송비 변경건은 OP_ORDER_SHIPPING에 정산 금액을 조절하고 OP_ADD_PAYMENT 테이블에는 환불 금액관련 정보를 기록하고 사용한다.
//			 */
//			if (orderShippings != null) {
//				for(OrderShipping orderShipping : orderShippings) {
//
//					if (orderShipping.getAddPayAmount() == 0) {
//
//						if ("2".equals(orderShipping.getShippingPaymentType())) {
//							orderShipping.setPayShipping(orderShipping.getRePayShippingAmount());
//							orderShipping.setRemittanceAmount(orderShipping.getRePayShippingAmount());
//							orderShipping.setReturnShipping(orderShipping.getReturnShipping() + orderShipping.getAddPayAmount());
//
//							orderShippingMapper.updateCancelShipping(orderShipping);
//						}
//
//					} else {
//
//						OrderAddPayment orderAddPayment = new OrderAddPayment(orderShipping);
//						orderAddPayment.setAddPaymentType(orderShipping.getAddPaymentType());
//						orderAddPayment.setAmount(orderShipping.getAddPayAmount());
//						orderAddPayment.setRefundCode(claimApply.getRefundCode());
//						orderAddPayment.setSalesDate(DateUtils.getToday());
//						orderAddPayment.setSubject("주문 취소로 인한 배송비 변경");
//						orderAddPayment.setIssueCode("CANCEL-ADD-SHIPPING-" + orderShipping.getShippingSequence());
//						orderAddPaymentService.insertOrderAddPayment(orderAddPayment);
//
//                        if ("1".equals(orderShipping.getAddPaymentType())) {
//                            orderShipping.setPayShipping(orderShipping.getRePayShippingAmount());
//                            orderShipping.setRemittanceAmount(orderShipping.getRePayShippingAmount());
//                            orderShipping.setRealShipping(orderShipping.getRePayShippingAmount());
//                        } else if ("2".equals(orderShipping.getAddPaymentType())) {
//                            orderShipping.setPayShipping(orderShipping.getRePayShippingAmount());
//                            orderShipping.setRemittanceAmount(orderShipping.getRePayShippingAmount());
//                            orderShipping.setReturnShipping(orderShipping.getReturnShipping() + orderShipping.getAddPayAmount());
//                            orderShipping.setRealShipping(orderShipping.getRePayShippingAmount());
//                        }
//
//                        orderShippingMapper.updateCancelShipping(orderShipping);
//					}
//				}
//			}
//		}
//
//		HashMap<String, Integer> stockMap = new HashMap<>();
//
//		// 세트상품 용
//		HashMap<String, Integer> stockSetMap = new HashMap<>();
//
//		for(String key : claimApply.getCancelIds()) {
//
//			OrderCancelApply orderCancelApply = claimApply.getCancelApplyMap().get(key);
//			if (orderCancelApply == null) {
//				throw new OrderException();
//			}
//
//			orderCancelApply.setClaimCode(key);
//
//			if ("98".equals(orderCancelApply.getClaimStatus())) { // 배송처리
//
//               ShippingParam shippingParam = new ShippingParam(orderCancelApply);
//
//				DeliveryCompany deliveryCompany = deliveryCompanyService.getDeliveryCompanyById(orderCancelApply.getDeliveryCompanyId());
//				if (deliveryCompany == null) {
//					throw new OrderException();
//				}
//
//				shippingParam.setDeliveryCompanyName(deliveryCompany.getDeliveryCompanyName());
//				shippingParam.setDeliveryCompanyUrl(deliveryCompany.getDeliveryCompanyUrl());
//				shippingParam.setAdminUserName(UserUtils.getManagerName());
//
//				shippingParam.setConditionType("OPMANAGER");
//				if (ShopUtils.isSellerPage()) {
//					shippingParam.setConditionType("SELLER");
//					shippingParam.setSellerId(SellerUtils.getSellerId());
//				}
//
//                // 주문 로그인해 임시로 주문상품 조회
////                OrderItem logOrderItem = orderService.getOrderItemForOrderLog(
////                        shippingParam.getOrderCode(),
////                        shippingParam.getOrderSequence(),
////                        shippingParam.getItemSequence()
////                );
//
//				if (orderShippingMapper.updateShippingStart(shippingParam) == 0) {
//					throw new OrderException();
//				}
//
//				orderCancelApply.setClaimStatus("99");
//
//				// 주문 로그
//				/*try {
//				    orderService.insertOrderLog(
//				            OrderLogType.CLAIM_CANCEL,
//				            shippingParam.getOrderCode(),
//				            shippingParam.getOrderSequence(),
//				            shippingParam.getItemSequence(),
//				            logOrderItem.getOrderStatus()
//				    );
//				} catch (RuntimeException e) {
//				//				    log.error("ERROR: {}", e.getMessage(), e);
//				    log.error("ERROR: {}", getClass().getName() + " :: orderCancelProcess RuntimeException1 ==========");
//				}*/
//			}
//
//			// 환불 승인 대기이면 환불 코드를 등록
//			if ("03".equals(orderCancelApply.getClaimStatus())) {
//				orderCancelApply.setRefundCode(claimApply.getRefundCode());
//			}
//
//            // 주문 로그인해 임시로 주문상품 조회
//            OrderItem logOrderItem = orderService.getOrderItemForOrderLog(
//                    orderCancelApply.getOrderCode(),
//                    orderCancelApply.getOrderSequence(),
//                    orderCancelApply.getItemSequence()
//            );
//
//			int count = orderClaimApplyMapper.updateOrderCancelApply(orderCancelApply);
//			if (("99".equals(orderCancelApply.getClaimStatus())) && count > 0) {
//
//				orderClaimApplyMapper.updateClaimQuantityForCancel(orderCancelApply);
//
//                // 주문 로그
//                try {
//                    orderService.insertOrderLog(
//                            OrderLogType.CLAIM_CANCEL,
//                            orderCancelApply.getOrderCode(),
//                            orderCancelApply.getOrderSequence(),
//                            orderCancelApply.getItemSequence(),
//                            logOrderItem.getOrderStatus()
//                    );
//                } catch (RuntimeException e) {
////                    log.error("ERROR: {}", e.getMessage(), e);
//				    log.error("ERROR: {}", getClass().getName() + " :: orderCancelProcess RuntimeException2 ==========");
//                }
//			} else if (("03".equals(orderCancelApply.getClaimStatus())) && count > 0) {
//				// 주문 아이템 정보를 임의로 조회한다. 20230322
//				OrderItem orderItem = orderClaimApplyMapper.getOpOrderItemLocgovCode(orderCancelApply);
//
//				// 재고량 복원 추가
//				// 재고 복원해야하는 목록 만들기
////				orderService.makeStockRestorationMap(stockMap, orderItem, orderCancelApply.getClaimApplyQuantity());
////
////				List<OrderItem> orderItemSets = orderItem.getItemSets();
////
////				if(orderItemSets != null && !orderItemSets.isEmpty()) {
////					// 세트상품 재고 복원 목록
////					if ("Y".equals(orderItem.getSetItemFlag()) && orderItemSets != null && !orderItemSets.isEmpty()) {
////						orderItem.setItemSets(orderService.filterOrderItemSets(orderItemSets, orderItem.getOrderCode(), orderItem.getOrderSequence(), orderItem.getItemSequence()));
////
////						for (OrderItem orderItemSet : orderItem.getItemSets()) {
////							orderService.makeStockRestorationMap(stockSetMap, orderItemSet, orderItem.getQuantity() * orderItemSet.getQuantity());
////						}
////					}
////				}
//				// 재고량 복원 추가
//
//				// 취소 수량
//				orderCancelApply.getClaimApplyQuantity();
//                orderClaimApplyMapper.updateClaimQuantityForCancelApply(orderCancelApply);
//
//                // 포인트 복구 작업(잔여포인트, 포인트사용내역, 결제내역, 결제 상품, 주문내역 등등) start -->
//                List<OrderGivePoint> orderGivePoints = new ArrayList<>();
//				List<OrderGivePoint> remainGivePoints = new ArrayList<>();
//
//				orderCancelApply.setOrderItem(orderItem);
//
//				// 사용포인트 조회
//				OrderGivePoint orderGivePointParam = new OrderGivePoint();
//				orderGivePointParam.setCntrLocgovCode(orderCancelApply.getOrderItem().getLocgovCode());
//				orderGivePointParam.setOrderCode(orderCancelApply.getOrderCode());
//				List<OrderGivePoint> useGivePoints = orderGivePointService.getGiveUsePoint(orderGivePointParam);
//
//				boolean exist = false;
//				check: for (OrderGivePoint orderGivePoint : orderGivePoints) {
//					exist = false;
//					for (OrderGivePoint useGivePoint : useGivePoints) {
//						if (orderGivePoint.getOrderCode().equalsIgnoreCase(useGivePoint.getOrderCode())) {
//							exist = true;
//							break check;
//						}
//					}
//				}
//
//				if (!exist) {
//					orderGivePoints.addAll(useGivePoints);
//					for (OrderGivePoint useGivePoint : useGivePoints) {
//						exist = false;
//						OrderGivePoint remainGivePoint = orderGivePointService.getGiveBlcePointListByCntrSn(useGivePoint);
//						for (OrderGivePoint givePoint : remainGivePoints) {
//							if (remainGivePoint.getCntrSn().equalsIgnoreCase(givePoint.getCntrSn())) {
//								exist = true;
//							}
//						}
//						if (!exist) {
//							remainGivePoints.add(remainGivePoint);
//						}
//					}
//				}
//
//				OrderParam orderParam = new OrderParam();
//				orderParam.setOrderCode(orderCancelApply.getOrderCode());
//				orderParam.setOrderSequence(orderCancelApply.getOrderSequence());
//				orderParam.setItemSequence(orderCancelApply.getItemSequence());
//				orderParam.setPaymentSequence(0);
//				OrderPayment payment = orderPaymentMapper.getOrderPayment(orderParam);
//				orderCancelApply.setClaimApplyAmount(orderCancelApply.getClaimApplyQuantity() * orderCancelApply.getOrderItem().getSalePrice());
//				orderService.refundGiveGoodsPoint(payment, orderCancelApply, orderGivePoints, remainGivePoints);
//				// <-- end 포인트 복구 작업(잔여포인트, 포인트사용내역, 결제내역, 결제 상품, 주문내역 등등)
//
//				// 상태 변경 추가... 03->04
//				orderCancelApply.setClaimStatus("04");
//				orderClaimApplyMapper.updateOrderCancelApply(orderCancelApply);
//
//				orderMapper.updateOrderCancel(orderParam);
//
//				// 주문 로그 추가
//                try {
//                    orderService.insertOrderLog(
//                            OrderLogType.CLAIM_CANCEL,
//                            orderCancelApply.getOrderCode(),
//                            orderCancelApply.getOrderSequence(),
//                            orderCancelApply.getItemSequence(),
//                            logOrderItem.getOrderStatus()
//                    );
//                } catch (RuntimeException e) {
////                    log.error("ERROR: {}", e.getMessage(), e);
//				    log.error("ERROR: {}", getClass().getName() + " :: orderCancelProcess RuntimeException3 ==========", e);
//                }
//            }
//		}
//
//
//		// 재고량 복원
//		orderService.stockRestoration(stockMap);
//
//		// 세트상품 재고량 복원
//		orderService.stockRestoration(stockSetMap);
//
// 	}

	@Override
	public void orderReturnSaveProcess(ClaimApply claimApply) {

		String[] returnIds = claimApply.getReturnIds();
		if (returnIds == null) {
			return;
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}
		orderParam.setOrderCode(claimApply.getOrderCode());
		orderParam.setOrderSequence(claimApply.getOrderSequence());
		Order order = orderMapper.getOrderByParam(orderParam);

		if (order == null) {
			throw new OrderException();
		}
		//구매자 배송지 조회
		List<OrderItem> itemList = new ArrayList<>();
		for(OrderShippingInfo info : order.getOrderShippingInfos()) {
			for(OrderItem item : info.getOrderItems()) {
				itemList.add(item);
			}
		}

		List<OrderReturnApply> newApplyList = new ArrayList<>();
//		long shipmentReturnSellerId = SellerUtils.getSellerId();
		for(String claimCode : returnIds) {

			OrderReturnApply returnApply = claimApply.getReturnApplyMap().get(claimCode);
			if (returnApply == null) {
				throw new OrderException();
			}

			boolean hasItem = false;
			for(OrderItem item : itemList) {

				if (returnApply.getItemSequence() == item.getItemSequence()) {
/*
					if (shipmentReturnSellerId != returnApply.getShipmentReturnSellerId() && !ShopUtils.isOpmanagerPage()) {
						throw new OrderException("[처리권한 없음] 반품 처리 권한이 없습니다.");
					}
*/
					if (returnApply.getClaimCode().equals(claimCode)) {

						if (!"99".equals(returnApply.getClaimStatus())) {
							item.setQuantity(item.getQuantity() - returnApply.getClaimApplyQuantity());
						}

						returnApply.setOrderItem(item);
						newApplyList.add(returnApply);

						hasItem = true;
						break;
					}
				}

			}

			if (hasItem == false) {
				throw new OrderException(claimCode + "정보 없음");
			}

		}

		if (newApplyList.isEmpty()) {
			throw new OrderException();
		}

		for(OrderReturnApply apply : newApplyList) {
			if ("03".equals(apply.getClaimStatus())) {
				throw new ClaimException("1000");
			}
		}

		for(OrderReturnApply apply : newApplyList) {

			// 데이터 암호화
			apply.encrypt(orderReturnApplyEncryptor);
			int count = orderClaimApplyMapper.updateOrderReturnApply(apply);
			if (count == 0) {
				throw new OrderException();
			}

			if ("99".equals(apply.getClaimStatus())) {
				orderClaimApplyMapper.updateClaimQuantityForReturn(apply);

				try {
					orderService.insertOrderLog(
							OrderLogType.CLAIM_RETURN,
							apply.getOrderCode(),
							apply.getOrderSequence(),
							apply.getItemSequence(),
							apply.getOrderItem().getOrderStatus()
					);
				} catch (RuntimeException e) {
//					log.error("ERROR: {}", e.getMessage(), e);
				    log.error("ERROR: {}", getClass().getName() + " :: orderReturnSaveProcess RuntimeException ==========");
				}
			}

		}
	}

	@Override
	public List<OrderAddPayment> orderReturnViewData(ClaimApply claimApply, Order order) {

		List<OrderItem> itemList = new ArrayList<>();

		long shipmentReturnSellerId = SellerUtils.getSellerId();

		/* 선택 된 상품정보에 해당되는 판매자(sellerId) 설정 *
		 * 관리자가 반품처리 시 관리자의 ID(sellerId)로 정산처리가 되는 현상을 막기위함
		 * 판매자인 경우는 자기가 판매하는 상품이기 때문에 sellerId는 동일하며, 관리자인 경우 서버단으로 넘어올 때 서로 다른 판매자의 상품들은 넘길 수 없도록 validation 체크를 하고 있기 때문에
		 * 넘어온 상품 중 하나의 ID만 꺼내서 설정해 주면 됨
		 * */
		if(claimApply.getReturnIds() != null && claimApply.getReturnIds().length > 0){
			shipmentReturnSellerId = claimApply.getReturnApplyMap().get(claimApply.getReturnIds()[0]).getShipmentReturnSellerId();
		}

		for(OrderShippingInfo info : order.getOrderShippingInfos()) {
			for(OrderItem item : info.getOrderItems()) {
				itemList.add(item);
			}
		}

		List<OrderAddPayment> orderAddPayments = new ArrayList<>();
		List<OrderReturnApply> newApplyList = new ArrayList<>();
		for(String claimCode : claimApply.getReturnIds()) {

			OrderReturnApply returnApply = claimApply.getReturnApplyMap().get(claimCode);
			if (returnApply == null) {
				throw new OrderException();
			}

			boolean hasItem = false;
			for(OrderItem item : itemList) {

				if (returnApply.getItemSequence() == item.getItemSequence()) {

					if (shipmentReturnSellerId != returnApply.getShipmentReturnSellerId() && !ShopUtils.isOpmanagerPage()) {
						throw new OrderException("[처리권한 없음] 반품 처리 권한이 없습니다.");
					}

					if (returnApply.getClaimCode().equals(claimCode)) {

						if (!"99".equals(returnApply.getClaimStatus())) {
							item.setQuantity(item.getQuantity() - returnApply.getClaimApplyQuantity());
						}

						returnApply.setOrderItem(item);
						returnApply.setShippingSequence(item.getShippingSequence());
						returnApply.setUserId(item.getUserId());
						newApplyList.add(returnApply);

						hasItem = true;
						break;
					}
				}

			}

			if (hasItem == false) {
				throw new OrderException(claimCode + "정보 없음");
			}

		}

		if (newApplyList.isEmpty()) {
			throw new OrderException();
		}

		int collectionShippingAmount = 0; // 회수비용
        int initShippingAmount = 0; // 초기 배송비

		for(OrderReturnApply apply : newApplyList) {

			// 고객 사유
			if ("2".equals(apply.getReturnReason())) {
				collectionShippingAmount += apply.getCollectionShippingAmount();

                // 2020.08.08 juneu.son 클레임 상품의 배송타입이 상품,출고지 조건부 일 경우
                // 클레임 처리 후 남은 잔여금액이 배송비 무료 조건을 충족시키지 못하면 초기 배송비 발생
                if ("2".equals(apply.getOrderItem().getOrderShipping().getShippingType())
                        || "3".equals(apply.getOrderItem().getOrderShipping().getShippingType())) {
                    int remainingAmount = 0;
                    int claimAmount = apply.getOrderItem().getClaimQuantity() * apply.getOrderItem().getSalePrice();

                    for (OrderItem item : order.getOrderShippingInfos().get(0).getOrderItems()) {
                        if (item.getItemId() == apply.getOrderItem().getItemId() && "N".equals(item.getCancelFlag())) {
                            remainingAmount += item.getSaleAmount();
                        }
                    }

                    if (remainingAmount - claimAmount  < apply.getOrderItem().getOrderShipping().getShippingFreeAmount()) {
                        initShippingAmount += apply.getOrderItem().getOrderShipping().getShipping();
                    }
                }
			}
		}


		// 고객사유가 있는경우
		int defaultShippingAmount = 0;
		int customersReasonCount = 0;

		// 판매자사유인 경우
		int sellerReasonCount = 0;

		List<String> deliveryNumbers = new ArrayList<>();
		for(OrderReturnApply apply : newApplyList) {

			OrderItem orderItem = apply.getOrderItem();

			if ("03".equals(apply.getClaimStatus()) && "2".equals(apply.getReturnReason())) {
				customersReasonCount++;
			}

			if ("03".equals(apply.getClaimStatus()) && "1".equals(apply.getReturnReason())) {
				sellerReasonCount++;

				String deliveryNumber = orderItem.getDeliveryNumber();

				boolean isNew = true;
				for(String s : deliveryNumbers) {
					if (s.equals(deliveryNumber)) {
						isNew = false;
					}
				}

				if (isNew) {
					if (ObjectUtils.isEmpty(deliveryNumber) == false) {
						deliveryNumbers.add(deliveryNumber);
					}
				}
			}

			defaultShippingAmount = (defaultShippingAmount == 0 || orderItem.getShippingReturn() > defaultShippingAmount) ? orderItem.getShippingReturn() : defaultShippingAmount;
		}

		//판매자 사유인 경우
		if(sellerReasonCount > 0) {

			HashMap<Integer, OrderShipping> returnShippingMap = new HashMap<Integer, OrderShipping>();
			for(OrderReturnApply apply : newApplyList) {

				OrderItem orderItem = apply.getOrderItem();
				OrderShipping orderShipping = orderItem.getOrderShipping();
				if (returnShippingMap.get(orderShipping.getShippingSequence()) != null) {
					continue;
				}

				if ("03".equals(apply.getClaimStatus()) && "1".equals(apply.getReturnReason())) {

					if (!"1".equals(orderShipping.getShippingType()) && orderShipping.getPayShipping() > 0) {

						OrderAddPayment orderAddPayment = new OrderAddPayment();
						orderAddPayment.setOrderCode(claimApply.getOrderCode());
						orderAddPayment.setOrderSequence(claimApply.getOrderSequence());
						orderAddPayment.setAddPaymentType("2");
						orderAddPayment.setAmount(orderShipping.getPayShipping());
						orderAddPayment.setRefundCode(claimApply.getRefundCode());
						orderAddPayment.setSellerId(orderShipping.getSellerId());
						orderAddPayment.setSalesDate(DateUtils.getToday());
						orderAddPayment.setSalesDate(DateUtils.getToday());
						orderAddPayment.setSubject("배송비 환불");

						orderAddPayment.setIssueCode("RETURN-SHIPPING-" + orderShipping.getShippingSequence());

						if (orderAddPaymentService.getDuplicateRegistrationCheckByIssueCode(orderAddPayment) == 0) {

							// CJH 2016.12.29 같은 배송정책 상품중 취소 처리 되지 않은 상품이 모두 클레임 처리되었을때만 환불..
							OrderParam orderParam = new OrderParam();
							orderParam.setOrderCode(orderItem.getOrderCode());
							orderParam.setItemSequence(orderItem.getItemSequence());
							orderParam.setShippingSequence(orderItem.getShippingSequence());
							List<OrderItem> items  = orderShippingMapper.getOrderItemListByShippingParam(orderParam);

							int applyCount = 0;
							for(OrderReturnApply checkApply : newApplyList) {
								if ("03".equals(checkApply.getClaimStatus()) && checkApply.getShippingSequence() == orderShipping.getShippingSequence()) {
									applyCount++;
								}
							}

							boolean isReturnPayShipping = true;
							if (items != null) {
								isReturnPayShipping = (items.size() == applyCount) ? true : false;
							}

							if (isReturnPayShipping) {
								orderAddPayments.add(orderAddPayment);
								returnShippingMap.put(orderShipping.getShippingSequence(), orderShipping);
							}

						}
					}
				}
			}
		}

		// 환불 승인건이 있는경우 추가 배송비등의 정보를 OP_ORDER_ADD_PAYMENT 테이블에 기록
		if (customersReasonCount > 0) {

			// 회수 비용
			if (collectionShippingAmount > 0) {

				OrderAddPayment orderAddPayment = new OrderAddPayment();

				orderAddPayment.setOrderCode(claimApply.getOrderCode());
				orderAddPayment.setOrderSequence(claimApply.getOrderSequence());
				orderAddPayment.setSellerId(shipmentReturnSellerId);
				orderAddPayment.setAddPaymentType("1");
				orderAddPayment.setAmount(collectionShippingAmount);
				orderAddPayment.setRefundCode(claimApply.getRefundCode());
				orderAddPayment.setSalesDate(DateUtils.getToday());
				orderAddPayment.setSubject("회수비용");
				orderAddPayment.setIssueCode("COLLECTION-SHIPPING-" + shipmentReturnSellerId + "-" + claimApply.getOrderCode());
				orderAddPayments.add(orderAddPayment);
			}

			// 최초 배송비
            if (initShippingAmount > 0) {
                OrderAddPayment orderAddPayment = new OrderAddPayment();

                orderAddPayment.setOrderCode(claimApply.getOrderCode());
                orderAddPayment.setOrderSequence(claimApply.getOrderSequence());
                orderAddPayment.setAddPaymentType("1");
                orderAddPayment.setAmount(initShippingAmount);
                orderAddPayment.setRefundCode(claimApply.getRefundCode());
                orderAddPayment.setSellerId(shipmentReturnSellerId);
                orderAddPayment.setSalesDate(DateUtils.getToday());
                orderAddPayment.setSalesDate(DateUtils.getToday());
                orderAddPayment.setSubject("최초 배송비");

                orderAddPayment.setIssueCode("INIT-SHIPPING-" + shipmentReturnSellerId + "-" + claimApply.getOrderCode());

                if (orderAddPaymentService.getDuplicateRegistrationCheckByIssueCode(orderAddPayment) == 0) {
                    orderAddPayments.add(orderAddPayment);
                }
            }

			for(String deliveryNumber : deliveryNumbers) {

				boolean isFreeShipping = true;
				long shipmentSellerId = 0; // 무료 발송 배송비는 배송 주체가 배송비를 정산받음
				for(OrderItem item : itemList) {

					if (deliveryNumber.equals(item.getDeliveryNumber())) {

						OrderShipping orderShipping = item.getOrderShipping();
						shipmentSellerId = orderShipping.getSellerId();
						if (orderShipping.getPayShipping() > 0 || "2".equals(orderShipping.getShippingPaymentType())) {
							isFreeShipping = false;
						}

					}

				}

				if (isFreeShipping) {
					OrderAddPayment orderAddPayment = new OrderAddPayment();

					orderAddPayment.setOrderCode(claimApply.getOrderCode());
					orderAddPayment.setOrderSequence(claimApply.getOrderSequence());
					orderAddPayment.setAddPaymentType("1");
					orderAddPayment.setAmount(defaultShippingAmount);
					orderAddPayment.setRefundCode(claimApply.getRefundCode());
					orderAddPayment.setSellerId(shipmentSellerId);
					orderAddPayment.setSalesDate(DateUtils.getToday());
					orderAddPayment.setSalesDate(DateUtils.getToday());
					orderAddPayment.setSubject("송장번호["+ deliveryNumber +"] 무료발송 배송비");

					orderAddPayment.setIssueCode("FREE-SHIPPING-" + deliveryNumber);

					if (orderAddPaymentService.getDuplicateRegistrationCheckByIssueCode(orderAddPayment) == 0) {
						orderAddPayments.add(orderAddPayment);
					}
				}
			}


			// 잔여상품 배송비
			HashMap<Integer, OrderShipping> returnShippingMap = new HashMap<Integer, OrderShipping>();
			for(OrderItem item : itemList) {


				if ("0".equals(item.getOrderStatus()) || "10".equals(item.getOrderStatus())) {

					OrderShipping orderShipping = item.getOrderShipping();

					// 착불이 아니고 배송비가 없으며 조건부 인경우
					if (orderShipping.getPayShipping() == 0 && !"2".equals(orderShipping.getShippingPaymentType())
							&& ("2".equals(orderShipping.getShippingType()) || "3".equals(orderShipping.getShippingType()))) {

						returnShippingMap.put(orderShipping.getShippingSequence(), orderShipping);
					}

				}

			}


			for(int shippingSequence : returnShippingMap.keySet()) {

				OrderShipping shipping = returnShippingMap.get(shippingSequence);

				int notSendTotalItemAmount = 0;
				for(OrderItem item : itemList) {

					if ("0".equals(item.getOrderStatus()) || "10".equals(item.getOrderStatus())) {
						OrderShipping orderShipping = item.getOrderShipping();

						if (shippingSequence == orderShipping.getShippingSequence()) {
							notSendTotalItemAmount += item.getSaleAmount();//총액
						}
					}
				}

				if (notSendTotalItemAmount > 0 && shipping.getShipping() > 0) {
					if (notSendTotalItemAmount < shipping.getShippingFreeAmount()) {

						OrderAddPayment orderAddPayment = new OrderAddPayment();

						orderAddPayment.setOrderCode(claimApply.getOrderCode());
						orderAddPayment.setOrderSequence(claimApply.getOrderSequence());
						orderAddPayment.setAddPaymentType("1");
						orderAddPayment.setAmount(shipping.getShipping());
						orderAddPayment.setRefundCode(claimApply.getRefundCode());
						orderAddPayment.setSellerId(shipping.getSellerId());
						orderAddPayment.setSalesDate(DateUtils.getToday());
						orderAddPayment.setSalesDate(DateUtils.getToday());
						orderAddPayment.setSubject("잔여상품 배송비 - 조건부 무료배송[" + StringUtils.numberFormat(shipping.getShippingFreeAmount()) + "원 이상 무료]");

						orderAddPayment.setIssueCode("NOSEND-ITEM-" + shipping.getShippingSequence());

						if (orderAddPaymentService.getDuplicateRegistrationCheckByIssueCode(orderAddPayment) == 0) {
							orderAddPayments.add(orderAddPayment);
						}
					}
				}
			}
		}

		return orderAddPayments;
	}

	@Override
	public void orderReturnProcess(ClaimApply claimApply) {
		List<OrderRefundParam> listRerundParam = new ArrayList<>();
		String[] returnIds = claimApply.getReturnIds();
		if (returnIds == null) {
			return;
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		orderParam.setOrderCode(claimApply.getOrderCode());
		orderParam.setOrderSequence(claimApply.getOrderSequence());
		Order order = orderMapper.getOrderByParam(orderParam);

		if (order == null) {
			throw new ClaimException();
		}

		List<OrderItem> itemList = new ArrayList<>();
		for(OrderShippingInfo info : order.getOrderShippingInfos()) {
			for(OrderItem item : info.getOrderItems()) {
				itemList.add(item);
			}
		}

		List<OrderReturnApply> newApplyList = new ArrayList<>();

		// long shipmentReturnSellerId = SellerUtils.getSellerId();
		for(String claimCode : returnIds) {

			OrderReturnApply returnApply = claimApply.getReturnApplyMap().get(claimCode);
			if (returnApply == null) {
				throw new ClaimException();
			}

			boolean hasItem = false;
			for(OrderItem item : itemList) {

				if (returnApply.getItemSequence() == item.getItemSequence()) {
/*
					if (shipmentReturnSellerId != returnApply.getShipmentReturnSellerId() && !ShopUtils.isOpmanagerPage()) {
						throw new OrderException("[처리권한 없음] 반품 처리 권한이 없습니다.");
					}
*/
					if (returnApply.getClaimCode().equals(claimCode)) {

						if (!"99".equals(returnApply.getClaimStatus())) {
							item.setQuantity(item.getQuantity() - returnApply.getClaimApplyQuantity());
						}

						if ("03".equals(returnApply.getClaimStatus())) {
							OrderRefundParam orderRefundParam = new OrderRefundParam();
							orderRefundParam.setOrderCode(returnApply.getOrderCode());
							orderRefundParam.setOrderSequence(returnApply.getOrderSequence());
							orderRefundParam.setSellerId(item.getSellerId());

							OrderRefundParam refundParamOld = (OrderRefundParam)org.apache.commons.collections.CollectionUtils.find(listRerundParam, new Predicate() {
								@Override
								public boolean evaluate(Object obj) {
									return orderRefundParam.getSellerId() == ((OrderRefundParam)obj).getSellerId();
						        }
							});

							if (refundParamOld != null) {
								orderRefundParam.setRefundCode(refundParamOld.getRefundCode());
								listRerundParam.add(orderRefundParam);
							} else {
								if (ShopUtils.isSellerPage()) {
									orderRefundParam.setSellerId(SellerUtils.getSellerId());
								}

								if (UserUtils.isManagerLogin()) {
									orderRefundParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
								}

								String refundCode = orderRefundService.getActiveRefundCodeByParam(orderRefundParam);
								if (ObjectUtils.isEmpty(refundCode)) {
									throw new OrderException();
								}

								orderRefundParam.setRefundCode(refundCode);
								listRerundParam.add(orderRefundParam);
							}

							returnApply.setRefundCode(orderRefundParam.getRefundCode());
						}

						returnApply.setOrderItem(item);
						newApplyList.add(returnApply);

						hasItem = true;
						break;
					}
				}

			}

			if (hasItem == false) {
				throw new OrderException(claimCode + "정보 없음");
			}

		}

		if (newApplyList.isEmpty()) {
			throw new ClaimException();
		}


		for(OrderReturnApply apply : newApplyList) {

			// 승인일경우 환불 코드를 입력
//			if ("03".equals(apply.getClaimStatus())) {
//				apply.setRefundCode(claimApply.getRefundCode());
//			}

			// 데이터 암호화
			apply.encrypt(orderReturnApplyEncryptor);
			int count = orderClaimApplyMapper.updateOrderReturnApply(apply);
			if (count == 0) {
				throw new ClaimException();
			}

			if ("99".equals(apply.getClaimStatus())) {
				orderClaimApplyMapper.updateClaimQuantityForReturn(apply);
			} else if ("03".equals(apply.getClaimStatus())) {
				orderClaimApplyMapper.updateClaimQuantityForReturnApply(apply);
			}

			// 주문로그 추가
			if ("99".equals(apply.getClaimStatus()) || ("03".equals(apply.getClaimStatus()))) {
				try {
					orderService.insertOrderLog(
							OrderLogType.CLAIM_RETURN,
							apply.getOrderCode(),
							apply.getOrderSequence(),
							apply.getItemSequence(),
							apply.getOrderItem().getOrderStatus()
					);
				} catch (RuntimeException e) {
//					log.error("ERROR: {}", e.getMessage(), e);
				    log.error("ERROR: {}", getClass().getName() + " :: orderReturnProcess RuntimeException ==========");
				}
			}
		}

		// 추가금 별도 청구가 아니면
		if (!"1".equals(claimApply.getSeparateCharges())) {
			if (claimApply.getAddPayments() != null) {
				for(OrderAddPayment addPayment : claimApply.getAddPayments()) {

					if (addPayment.getAmount() <= 0) {
						continue;
					}

					addPayment.setOrderCode(claimApply.getOrderCode());
					addPayment.setOrderSequence(claimApply.getOrderSequence());
					addPayment.setRefundCode(claimApply.getRefundCode());
					addPayment.setSalesDate(DateUtils.getToday());
					addPayment.setSalesDate(DateUtils.getToday());
					addPayment.setRemittanceAmount(addPayment.getAmount());
					orderAddPaymentService.insertOrderAddPayment(addPayment);
				}
			}
		}
	}

	@Override
	public void orderExchangeProcess(ClaimApply claimApply) {
		String[] exchangeIds = claimApply.getExchangeIds();
		if (exchangeIds == null) {
			return;
		}

		for(String id : exchangeIds) {

			OrderExchangeApply apply = claimApply.getExchangeApplyMap().get(id);
			if (apply == null) {
				throw new OrderException();
			}

			// 배송 처리
			if ("03".equals(apply.getClaimStatus())) {

				if (ObjectUtils.isEmpty(apply.getExchangeDeliveryNumber()) || apply.getExchangeDeliveryCompanyId() == 0) {
					throw new OrderException();
				}

				ShippingParam shippingParam = new ShippingParam(apply);

				DeliveryCompany deliveryCompany = deliveryCompanyService.getDeliveryCompanyById(apply.getExchangeDeliveryCompanyId());
				if (deliveryCompany == null) {
					throw new OrderException();
				}

				shippingParam.setDeliveryCompanyName(deliveryCompany.getDeliveryCompanyName());
				shippingParam.setDeliveryCompanyUrl(deliveryCompany.getDeliveryCompanyUrl());
				shippingParam.setAdminUserName(UserUtils.getManagerName());

				shippingParam.setConditionType("OPMANAGER");
				if (ShopUtils.isSellerPage()) {
					shippingParam.setConditionType("SELLER");
					shippingParam.setSellerId(SellerUtils.getSellerId());
				}
				if ("모바일".equals(shippingParam.getDeliveryCompanyName())) {
					shippingParam.setMode("EXCHANGE_MOBILE");
				} else {
					shippingParam.setMode("EXCHANGE");
				}
				if (orderShippingMapper.updateShippingStart(shippingParam) == 0) {
					throw new OrderException();
				}

				// 국민비서 알림 전송 (답례품 교환 완료)
				try {
					OrderParam orderParam = new OrderParam(claimApply);
					orderParam.setOrderCode(apply.getOrderCode());
					orderParam.setOrderSequence(apply.getOrderSequence());
					orderParam.setItemSequence(apply.getItemSequence());
					List<Order> orderList = orderService.getOrderListByParam(orderParam);

					User user = userService.getUserByUserId(orderList.get(0).getUserId());
					UserDetail userDetail = (UserDetail) user.getUserDetail();
    				userDetail.decrypt(userDetailEncryptor, false);
	    			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {
						List<ReceiverInfo> receiverInfos = new ArrayList<>();
						ReceiverInfo receiverInfo = new ReceiverInfo();
						receiverInfo.setSmsType(SmsType.PRESENT_REFUND_COMPLETE);
						receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
						StringBuilder sb = new StringBuilder();
						sb.append(user.getUserName());	// 이름
						sb.append("|");
						sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
						sb.append("|");
						sb.append(apply.getOrderCode());	// 주문번호
						sb.append("|");
						sb.append(StringEscapeUtils.unescapeHtml(orderList.get(0).getItemName()).replaceAll("\\|", "-"));		// 답례품명
						if (StringUtils.hasLength(orderList.get(0).getOptions()) && !orderList.get(0).getOptions().contains("|")) {
							sb.append("(");
							sb.append(StringEscapeUtils.unescapeHtml(orderList.get(0).getOptions()));	// 옵션명
							sb.append(")");
						}
						sb.append("|");
						sb.append(ObjectUtils.isEmpty(apply.getExchangeReasonDetail()) ? apply.getExchangeReasonText() : apply.getExchangeReasonDetail());		// 사유
						sb.append("|");
						sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
						receiverInfo.setSndngCntnts(sb.toString());
						receiverInfos.add(receiverInfo);
						smsIpsService.insertTifIpsSndngM(receiverInfos);
	    			}
				} catch (NullPointerException | ClassCastException e) {
					log.error(getClass().getName() +  " :: orderExchangeProcess send sms error", e);
				}

				// 국민비서 알림 전송 (답례품 교환 완료)
				try {
//					OrderParam orderParam = new OrderParam(claimApply);
//					orderParam.setOrderCode(apply.getOrderCode());
//					orderParam.setOrderSequence(apply.getOrderSequence());
//					orderParam.setItemSequence(apply.getItemSequence());
//					List<Order> orderList = orderService.getOrderListByParam(orderParam);
//
//					User user = userService.getUserByUserId(orderList.get(0).getUserId());
//					UserDetail userDetail = (UserDetail) user.getUserDetail();
//    				userDetail.decrypt(userDetailEncryptor, false);
//	    			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {
//						List<ReceiverInfo> receiverInfos = new ArrayList<>();
//						ReceiverInfo receiverInfo = new ReceiverInfo();
//						receiverInfo.setSmsType(SmsType.PRESENT_REFUND_COMPLETE);
//						receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
//						StringBuilder sb = new StringBuilder();
//						sb.append(user.getUserName());	// 이름
//						sb.append("|");
//						sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
//						sb.append("|");
//						sb.append(apply.getOrderCode());	// 주문번호
//						sb.append("|");
//						sb.append(StringEscapeUtils.unescapeHtml(orderList.get(0).getItemName()));		// 답례품명
//						if (StringUtils.hasLength(orderList.get(0).getOptions()) && !orderList.get(0).getOptions().contains("|")) {
//							sb.append("(");
//							sb.append(StringEscapeUtils.unescapeHtml(orderList.get(0).getOptions()));	// 옵션명
//							sb.append(")");
//						}
//						sb.append("|");
//						sb.append(ObjectUtils.isEmpty(apply.getExchangeReasonDetail()) ? apply.getExchangeReasonText() : apply.getExchangeReasonDetail());		// 사유
//						sb.append("|");
//						sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
//						receiverInfo.setSndngCntnts(sb.toString());
//						receiverInfos.add(receiverInfo);
//						smsIpsService.insertTifIpsSndngM(receiverInfos);
//	    			}
				} catch (NullPointerException | ClassCastException e) {
					log.error(getClass().getName() +  " :: orderExchangeProcess send sms error", e);
				}

			}

			// 데이터 암호화
			apply.encrypt(orderExchangeApplyEncryptor);
			int count = orderClaimApplyMapper.updateOrderExchangeApply(apply);
			if (count == 0) {
				throw new OrderException();
			}

			// 주문 로그로 인해 임시로 조회
			OrderItem logOrderItem = orderService.getOrderItemForOrderLog(
					apply.getOrderCode(),
					apply.getOrderSequence(),
					apply.getItemSequence()
			);

			if ("99".equals(apply.getClaimStatus())) {
				orderClaimApplyMapper.updateClaimQuantityForExchange(apply);
			}

			// 주문로그 추가
			if ("99".equals(apply.getClaimStatus()) || ("03".equals(apply.getClaimStatus()))) {
				try {
					orderService.insertOrderLog(
							OrderLogType.CLAIM_RETURN,
							apply.getOrderCode(),
							apply.getOrderSequence(),
							apply.getItemSequence(),
							logOrderItem.getOrderStatus()
					);
				} catch (RuntimeException e) {
//					log.error("ERROR: {}", e.getMessage(), e);
				    log.error("ERROR: {}", getClass().getName() + " :: orderExchangeProcess RuntimeException ==========");
				}
			}
		}
	}

	@Override
	public void insertOrderExchangeApply(ExchangeApply exchangeApply) {

		OrderParam orderParam = new OrderParam(exchangeApply);

		if (UserUtils.isUserLogin()) {
			orderParam.setUserId(UserUtils.getUserId());
		} else if (UserUtils.isGuestLogin()) {

		} else {
			throw new PageNotFoundException();
		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new OrderException();
		}

		OrderItem orderItem = orderMapper.getOrderItemByParam(orderParam);
		if (orderItem == null) {
			throw new OrderException();
		} else {
			// 세트상품
			OrderParam orderSetParam = new OrderParam();
			orderSetParam.setOrderCode(orderItem.getOrderCode());
			orderSetParam.setOrderSequence(orderItem.getOrderSequence());
			orderSetParam.setSetItemSequence(orderItem.getItemSequence());
			orderItem.setItemSets(orderMapper.getOrderItemSetList(orderSetParam));
		}

		// CJH 2016.11.01 교환 신청시 주문 수량보다 신청수량이 더 많은경우 에러
		if (orderItem.getQuantity() < orderItem.getClaimQuantity() + exchangeApply.getApplyQuantity()) {
			throw new OrderException("교환을 신청하실수 없습니다.");
		}

		orderParam.setShippingInfoSequence(orderItem.getShippingInfoSequence());

		// CJH 2016.11.09 회수 배송지역을 사용자 화면에서 넘겨받음
		//OrderExchangeApply apply = new OrderExchangeApply(exchangeApply, orderMapper.getOrderShippingInfoByParam(orderParam));
		OrderExchangeApply apply = new OrderExchangeApply(exchangeApply);

		ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();
		shipmentReturnParam.setShipmentReturnId(orderItem.getShipmentReturnId());
		shipmentReturnParam.setItemId(orderItem.getItemId());
		shipmentReturnParam.setSellerId(orderItem.getSellerId());
		ShipmentReturn shipmentReturn = shipmentReturnMapper.getShipmentReturnByParam(shipmentReturnParam);
		if (shipmentReturn == null) {
			throw new OrderException("반송지 정보가 잘못되었습니다. 판매자에게 문의해 주십시오");
		}

		// 1 : 본사반송, 2 업체 반송
		long sellerId = "2".equals(orderItem.getShipmentReturnType()) ? orderItem.getSellerId() : SellerUtils.DEFAULT_OPMANAGER_SELLER_ID;

		apply.setShipmentReturnId(shipmentReturn.getShipmentReturnId());
		apply.setShipmentReturnSellerId(sellerId);

		// 주문 수량과 신청수량이 같으면 기존 주문 변경
		boolean isEqualQuantity = orderItem.getQuantity() == exchangeApply.getApplyQuantity();
		if (isEqualQuantity) {
			insertOrderExchangeApply(apply);
			orderClaimApplyMapper.updateClaimQuantityForExchangeApply(apply);
		} else {
			orderClaimApplyMapper.copyOrderItemForExchangeApply(apply);
			orderClaimApplyMapper.updateOrderItemQuantityForExchange(apply);

			// 중요!!
			apply.setItemSequence(apply.getCopyItemSequence());
			insertOrderExchangeApply(apply);
		}

		// 세트상품
		if (orderItem.getItemSets() != null && !orderItem.getItemSets().isEmpty()) {
			int copySetItemSequence = orderClaimApplyMapper.getMaxSetItemSequence(orderParam);
			apply.setSetClaimCode(apply.getClaimCode());
			apply.setCopySetItemSequence(copySetItemSequence);

			for (OrderItem itemSet : orderItem.getItemSets()) {
				apply.setSetItemSequence(itemSet.getSetItemSequence());
				apply.setItemSequence(itemSet.getItemSequence());
				apply.setClaimApplyQuantity(itemSet.getQuantity());

				// 주문 수량과 신청수량이 같으면 기존 주문 변경
				if (isEqualQuantity) {
					orderClaimApplyMapper.insertOrderSetExchangeApply(apply);
					orderClaimApplyMapper.updateSetClaimQuantityForExchangeApply(apply);
				} else {
					// 부분취소 시 row 추가 생성
					orderClaimApplyMapper.copyOrderItemSetForExchangeApply(apply);

					apply.setSetItemSequence(copySetItemSequence);
					orderClaimApplyMapper.insertOrderSetExchangeApply(apply);
				}
			}
		}

		// 국민비서 알림 전송 (답례품 교환 접수) - 구매자
		try {
			User user = userService.getUserByUserId(UserUtils.getUserId());
			UserDetail userDetail = (UserDetail) user.getUserDetail();
			userDetail.decrypt(userDetailEncryptor, false);
			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {
				List<ReceiverInfo> receiverInfos = new ArrayList<>();
				ReceiverInfo receiverInfo = new ReceiverInfo();
				receiverInfo.setSmsType(SmsType.PRESENT_REFUND_REGISTER);
				receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
				StringBuilder sb = new StringBuilder();
				sb.append(user.getUserName());	// 이름
				sb.append("|");
				sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
				sb.append("|");
				sb.append(orderItem.getOrderCode());	// 주문번호
				sb.append("|");
				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));		// 답례품명
				if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
					sb.append("(");
					sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
					sb.append(")");
				}
				sb.append("|");
				sb.append(ObjectUtils.isEmpty(apply.getExchangeReasonDetail()) ? apply.getExchangeReasonText() : apply.getExchangeReasonDetail());		// 사유
				sb.append("|");
				sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
				receiverInfo.setSndngCntnts(sb.toString());
				receiverInfos.add(receiverInfo);
				smsIpsService.insertTifIpsSndngM(receiverInfos);
			}
		} catch (NullPointerException | ClassCastException e) {
			log.error(getClass().getName() +  " :: insertOrderExchangeApply send sms error", e);
		}

		// 국민비서 알림 전송 (답례품 교환 접수) -- 판매자
		try {
			User user = userService.getUserByUserId(UserUtils.getUserId());

			Seller seller = sellerService.getSellerById(orderItem.getSellerId());
			SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdForSms(seller.getLoginId());
			if ("0".equals(sellerUser.getReceiveSms()) && StringUtils.hasLength(sellerUser.getPhoneNumber()) && StringUtils.hasLength(sellerUser.getMberCi())) {
				List<ReceiverInfo> receiverInfos = new ArrayList<>();
				ReceiverInfo receiverInfo = new ReceiverInfo();
				receiverInfo.setSmsType(SmsType.PRESENT_REFUND_REGISTER);
				receiverInfo.setPrvcIdntfcInfo(sellerUser.getMberCi());
				StringBuilder sb = new StringBuilder();
				sb.append(user.getUserName());	// 이름
				sb.append("|");
				sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
				sb.append("|");
				sb.append(orderItem.getOrderCode());	// 주문번호
				sb.append("|");
				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));		// 답례품명
				if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
					sb.append("(");
					sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
					sb.append(")");
				}
				sb.append("|");
				sb.append(ObjectUtils.isEmpty(apply.getExchangeReasonDetail()) ? apply.getExchangeReasonText() : apply.getExchangeReasonDetail());		// 사유
				sb.append("|");
				sb.append(sellerUser.getPhoneNumber().replaceAll("-", ""));
				receiverInfo.setSndngCntnts(sb.toString());
				receiverInfos.add(receiverInfo);
				smsIpsService.insertTifIpsSndngM(receiverInfos);

//				/* 팝빌 카카오 알림톡 전송 */
//				Alimtalk alimtalk = new Alimtalk();
//				alimtalk.setTemplateCode("026030000378");
//				alimtalk.setReceiverNum(sellerUser.getPhoneNumber());
//				alimtalk.setReceiverName(user.getUserName());
//				alimtalk.setOrderDate(alimtalk.getLocalDateTimeToStr());
//				alimtalk.setOrderNo(orderItem.getOrderCode());
//
//				StringBuilder orderItemName = new StringBuilder();
//				orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));
//				if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
//					orderItemName.append("(");
//					orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));
//					orderItemName.append(")");
//				}
//				alimtalk.setOrderName(orderItemName.toString());
//
//				alimtalk.setClaimReasonDetail(ObjectUtils.isEmpty(apply.getExchangeReasonDetail()) ? apply.getExchangeReasonText() : apply.getExchangeReasonDetail());
//
//				alimtalkService.sendAlimtalk(alimtalk);

				/* 국자원 모바일 메신저 알림톡 전송 */
				Nuri2NrmsgData nuri2NrmsgData = new Nuri2NrmsgData();

				// 아이템(옵션)
				StringBuilder orderItemName = new StringBuilder();
				orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));
				if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
					orderItemName.append("(");
					orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));
					orderItemName.append(")");
				}

				// 템플릿코드
				nuri2NrmsgData.setAltTemplateCode("KR002");
				// 수신번호
				nuri2NrmsgData.setPhone(sellerUser.getPhoneNumber().replaceAll("-", ""));
				// ALT_JSON
				nuri2NrmsgData.setAltJson(nuri2NrmsgData.getContent("PRESENT_REFUND_REGISTER", user.getUserName(), receiverInfo.getLocalDateTimeToStr(), orderItem.getOrderCode(), orderItemName.toString(), ""));
				// XMS_TEST
				nuri2NrmsgData.setXmsText(nuri2NrmsgData.getContent("PRESENT_REFUND_REGISTER", user.getUserName(), receiverInfo.getLocalDateTimeToStr(), orderItem.getOrderCode(), orderItemName.toString(), ""));

				nuri2Service.insertAlimtalk(nuri2NrmsgData);

			}
		} catch (NullPointerException | ClassCastException e) {
			log.error(getClass().getName() +  " :: insertOrderExchangeApply send sms error", e);
		} catch (Exception e) {
			log.error(getClass().getName() +  " :: insertOrderExchangeApply send error", e);
		}

		// 주문 로그
		orderService.insertOrderLog(
				OrderLogType.CLAIM_EXCHANGE,
				apply.getOrderCode(),
				apply.getOrderSequence(),
				apply.getItemSequence(),
				orderItem.getOrderStatus()
		);
	}

	private void insertOrderExchangeApply(OrderExchangeApply apply) {

		apply.encrypt(orderExchangeApplyEncryptor);
		orderClaimApplyMapper.insertOrderExchangeApply(apply);
	}

	@Override
	public List<OrderExchangeApply> getExchangeHistoryListByParam(ClaimApplyParam claimApplyParam) {

		List<OrderExchangeApply> list =orderClaimApplyMapper.getExchangeHistoryListByParam(claimApplyParam);

		setOrderItemOtherForExchangeApply(list);
		return list;
	}

	@Override
	public List<OrderReturnApply> getActiveReturnListByParam(ClaimApplyParam claimApplyParam) {
		List<OrderReturnApply> list = orderClaimApplyMapper.getActiveReturnListByParam(claimApplyParam);

		if (ValidationUtils.isNull(list) == false) {
			for(OrderReturnApply apply : list) {

				OrderParam orderParam = new OrderParam();
				orderParam.setOrderCode(apply.getOrderCode());
				orderParam.setOrderSequence(apply.getOrderSequence());

				Order returnInfo = orderMapper.getOrderReturnInfo(orderParam);
				if (ValidationUtils.isNull(returnInfo) == false) {
					apply.setReturnBankName(returnInfo.getReturnBankName());
					apply.setReturnBankInName(returnInfo.getReturnBankInName());
					apply.setReturnVirtualNo(returnInfo.getReturnVirtualNo());
				}

				OrderItem orderItem = apply.getOrderItem();

				// 상품 환불금액
				apply.setClaimApplyAmount(orderItem.getSalePrice() * apply.getClaimApplyQuantity());

				ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();
				shipmentReturnParam.setShipmentReturnId(apply.getShipmentReturnId());
				ShipmentReturn shipmentReturn = shipmentReturnMapper.getShipmentReturnByParam(shipmentReturnParam);
				if (shipmentReturn != null) {
					apply.setShipmentReturn(shipmentReturn);
				}

				Seller shipmentReturnSeller = sellerMapper.getSellerById(apply.getShipmentReturnSellerId());
				if (shipmentReturnSeller != null) {
					apply.setSeller(shipmentReturnSeller);
				}

				// CJH 2016.11.13 환불이 신청중일때 회수비용을 상품에 설정된것으로 디폴트 설정
				if ("01".equals(apply.getClaimStatus()) && apply.getCollectionShippingAmount() == 0) {
					apply.setCollectionShippingAmount(orderItem.getShippingReturn());
				}
			}

			setOrderItemOtherForReturnApply(list);

			if (list != null) {
				list.forEach(ora -> {
					ora.decrypt(orderReturnApplyEncryptor, ShopUtils.needMasking());
				});
			}
		}

		return list;
	}

	@Override
	public List<OrderExchangeApply> getActiveExchangeListByParam(ClaimApplyParam claimApplyParam) {

		List<OrderExchangeApply> list = orderClaimApplyMapper.getActiveExchangeListByParam(claimApplyParam);
		if (list != null && !list.isEmpty()) {
			for(OrderExchangeApply apply : list) {
				ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();
				shipmentReturnParam.setShipmentReturnId(apply.getShipmentReturnId());
				ShipmentReturn shipmentReturn = shipmentReturnMapper.getShipmentReturnByParam(shipmentReturnParam);
				if (shipmentReturn != null) {
					apply.setShipmentReturn(shipmentReturn);
				}

				Seller shipmentReturnSeller = sellerMapper.getSellerById(apply.getShipmentReturnSellerId());
				if (shipmentReturnSeller != null) {
					apply.setSeller(shipmentReturnSeller);
				}
			}

			setOrderItemOtherForExchangeApply(list);

			list.forEach(osi -> osi.decrypt(orderExchangeApplyEncryptor, ShopUtils.needMasking()));
		}

		return list;
	}

	@Override
	public List<OrderCancelShipping> getActiveCancelListByParam(ClaimApplyParam claimApplyParam) {

		List<OrderCancelApply> list = orderClaimApplyMapper.getActiveCancelListByParam(claimApplyParam);
		List<OrderCancelShipping> groups = null;
		if (ValidationUtils.isNull(list) == false) {
			groups = new ArrayList<>();

			for(OrderCancelApply apply : list) {

				OrderItem orderItem = apply.getOrderItem();

				// 상품 환불금액
				apply.setClaimApplyAmount(orderItem.getSalePrice() * apply.getClaimApplyQuantity());
				orderItem.setCancelApply(apply);

				OrderShipping orderShipping = orderItem.getOrderShipping();
				OrderCancelShipping shippingGroup = null;
				for(OrderCancelShipping group : groups) {
					if (orderShipping.getShippingSequence() == group.getShippingSequence()) {

						List<OrderItem> items = group.getOrderItems();
						items.add(orderItem);
						shippingGroup = group;
						break;

					}
				}

				if (ValidationUtils.isNull(shippingGroup)) {
					shippingGroup = new OrderCancelShipping(orderShipping);
					List<OrderItem> items = new ArrayList<>();
					items.add(orderItem);
					shippingGroup.setOrderItems(items);
					groups.add(shippingGroup);
				}
			}

			setOrderItemOtherForCancelApply(list);
		}

		return groups;
	}

	@Override
	public void orderCancelAllProcess(OrderParam orderParam, HttpServletRequest request) {

		boolean isCancelBatch = "CANCEL-BATCH".equals(orderParam.getConditionType());

		if (UserUtils.isUserLogin()) {
			orderParam.setUserId(UserUtils.getUserId());
		} else if (UserUtils.isGuestLogin()) {
			User user = UserUtils.getGuestLogin();
            if (user == null) {
                throw new PageNotFoundException();
            }
			UserDetail userDetail = (UserDetail) user.getUserDetail();

			orderParam.setGuestUserName(user.getUserName());
			orderParam.setGuestPhoneNumber(userDetail.getPhoneNumber());

		} else if (UserUtils.isManagerLogin()) {

		} else if (isCancelBatch) {
			if (orderParam.getUserId() > 0L) {
				orderParam.setUserId(orderParam.getUserId());
			} else {
				orderParam.setGuestUserName(orderParam.getUserName());
			}
		} else {
			throw new PageNotFoundException();
		}

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new OrderException();
		}

		List<OrderItem> orderItemSets = orderMapper.getOrderItemSetList(orderParam);

		boolean isAllCancel = true;
		List<Integer> returnCoupons = new ArrayList<>();
		HashMap<String, Integer> stockMap = new HashMap<>();
		HashMap<String, Integer> stockSetMap = new HashMap<>();
		List<OrderGiftItem> orderGiftItemList = new ArrayList<>();

		for (OrderShippingInfo info : order.getOrderShippingInfos()) {
			for (OrderItem item : info.getOrderItems()) {
				if (!"0".equals(item.getOrderStatus())) {
					throw new ClaimException("1001", "[주문전체취소] 입금대기 상태가 아닌 주문 건이 있습니다. (" + orderParam.getOrderCode() + ")");
				}

				if (item.getCouponUserId() > 0) {
					returnCoupons.add(item.getCouponUserId());
				}

				if (item.getAddCouponUserId() > 0) {
					returnCoupons.add(item.getAddCouponUserId());
				}

				// 재고 복원해야하는 목록 만들기
				orderService.makeStockRestorationMap(stockMap, item, item.getQuantity());

				// 세트상품 재고복원목록
				if ("Y".equals(item.getSetItemFlag()) && orderItemSets != null && !orderItemSets.isEmpty()) {
					item.setItemSets(orderService.filterOrderItemSets(orderItemSets, item.getOrderCode(), item.getOrderSequence(), item.getItemSequence()));

					for (OrderItem orderItemSet : item.getItemSets()) {
						orderService.makeStockRestorationMap(stockSetMap, orderItemSet, item.getQuantity() * orderItemSet.getQuantity());
					}
				}

				// 사은품 목록 만들기
				List<OrderGiftItem> tempOrderGiftItemList = item.getOrderGiftItemList();
				if (tempOrderGiftItemList != null && !tempOrderGiftItemList.isEmpty()) {
					tempOrderGiftItemList.stream().forEach(orderGiftItem -> {orderGiftItemList.add(orderGiftItem);});
				}
			}
		}

		List<OrderPayment> payments = order.getOrderPayments();
		ConfigPg configPg = configPgService.getConfigPg();

		if (isAllCancel) {
			for(OrderPayment payment : payments) {
				OrderPgData pgData = payment.getOrderPgData();
				if (configPg != null && (ObjectUtils.isEmpty(payment.getPayDate()) == false && ("bank".equals(payment.getApprovalType()) || ("vbank".equals(payment.getApprovalType()) && !configPg.isUseVbackRefundService())))) {

					// 무통장 입금타입 중에 결제 확인이 된것이 있으면 즉시취소 불가..
					isAllCancel = false;
					break;

				} else if (ValidationUtils.isNull(pgData.getPgServiceType()) == false && "Y".equals(payment.getNowPaymentFlag())) {

					// PG 결제 타입중에 부분취소 기록이 있으면 즉시취소 불가
					if (payment.getAmount() != payment.getRemainingAmount()) {
						isAllCancel = false;
						break;
					}

				}
			}
		}

		if (isAllCancel == false) {
			throw new ClaimException("1000", "[주문전체취소] 결제가 확인된 건이 있거나 부분 취소 기록이 있습니다. (" + orderParam.getOrderCode() + ")");
		}

		String approvalType = "";

		for(OrderPayment payment : payments) {

			// CJH 2016.11.13 잔여액이 0보다 작거나 같은데 즉시 결제 타입이면 무시 - 반대의 경우는 무통장?
			if (payment.getRemainingAmount() <= 0 && "Y".equals(payment.getNowPaymentFlag())) {
				continue;
			}

			approvalType = payment.getApprovalType();

			if (PointUtils.isPointType(approvalType)) {
				returnPoint(order, payment, payment.getRemainingAmount());

			} else if ("bank".equals(approvalType) && !isCancelBatch) { // 무통장입금 대기건 배치 돌 때 취소X.  사용자나 관리자가 직접 취소시엔 가능.
				if (ObjectUtils.isEmpty(payment.getPayDate()) == true) {
					orderPaymentMapper.updateOrderPaymentForBankCancel(payment);

					// 현금영수증 상태 업데이트
					CashbillParam cashbillParam = new CashbillParam();
					cashbillParam.setWhere("orderCode");
					cashbillParam.setQuery(payment.getOrderCode());

					Iterable<CashbillIssue> cashbillIssues = cashbillIssueRepository.findAll(cashbillParam.getPredicate());

					for (CashbillIssue cashbillIssue : cashbillIssues) {
						cashbillIssue.setCashbillStatus(CashbillStatus.CANCELED);
						cashbillIssue.setCanceledDate(DateUtils.getToday(Const.DATETIME_FORMAT));
						cashbillIssue.setUpdatedDate(DateUtils.getToday(Const.DATETIME_FORMAT));

						if (UserUtils.isUserLogin() || UserUtils.isManagerLogin()) {
							cashbillIssue.setUpdateBy(UserUtils.getUser().getUserName() + "(" + UserUtils.getLoginId() + ")");
						} else {
							cashbillIssue.setUpdateBy("관리자");
						}
					}
				} else {
					payment.setCancelAmount(payment.getRemainingAmount());
					orderPaymentMapper.updateOrderPaymentForCancel(payment);

					OrderPayment orderPayment = new OrderPayment();
					orderPayment.setPaymentType("2");
					orderPayment.setOrderCode(order.getOrderCode());
					orderPayment.setOrderSequence(order.getOrderSequence());
					orderPayment.setApprovalType(approvalType);
					orderPayment.setCancelAmount(payment.getRemainingAmount());
					orderPayment.setPayDate(DateUtils.getToday(Const.DATETIME_FORMAT));
					orderPayment.setDeviceType("WEB");

					if (ShopUtils.isMobilePage()) {
						orderPayment.setDeviceType("MOBILE");
					}

					orderPayment.setNowPaymentFlag("Y");
					orderPaymentMapper.insertOrderPayment(orderPayment);
				}
			} else if (ValidationUtils.isNull(payment.getOrderPgData()) == false) {
				OrderPgData orderPgData = payment.getOrderPgData();
				if (orderPgData.getOrderPgDataId() > 0) {
					boolean isSuccess = false;

					// PG 취소 - 즉시 취소
					if ("inicis".equals(orderPgData.getPgServiceType())) {
						isSuccess = inicisService.cancel(orderPgData);
					} else if ("lgdacom".equals(orderPgData.getPgServiceType())) {
						isSuccess = lgDacomService.cancel(orderPgData);

					} else if ("kakaopay".equals(orderPgData.getPgServiceType())) {
						orderPgData.setRemainAmount(payment.getRemainingAmount());
						isSuccess = kakaopayService.cancel(orderPgData);

					} else if ("payco".equals(orderPgData.getPgServiceType())) {
						orderPgData.setRemainAmount(payment.getRemainingAmount());
						isSuccess = paycoService.cancel(orderPgData);

					}else if ("kspay".equals(orderPgData.getPgServiceType())) {
						orderPgData.setRemainAmount(payment.getRemainingAmount());
						isSuccess = kspayService.cancel(orderPgData);
					}else if ("kcp".equals(orderPgData.getPgServiceType())) {
						orderPgData.setRemainAmount(payment.getRemainingAmount());
						isSuccess = kcpService.cancel(orderPgData);
					}else if ("easypay".equals(orderPgData.getPgServiceType())) {
						orderPgData.setRemainAmount(payment.getRemainingAmount());
						isSuccess = easypayService.cancel(orderPgData);
					} else if ("nicepay".equals(orderPgData.getPgServiceType())) {
						orderPgData.setRequest(request);
						orderPgData.setCancelAmount(orderPgData.getPgAmount());
						orderPgData.setRemainAmount(payment.getRemainingAmount());
						orderPgData.setCancelReason("주문취소");
						isSuccess = nicepayService.cancel(orderPgData);
                    } else if ("naverpay".equals(orderPgData.getPgServiceType())) {
                        orderPgData.setCancelAmount(orderPgData.getPgAmount());
                        orderPgData.setRemainAmount(payment.getRemainingAmount());
                        orderPgData = naverPaymentApi.cancel(orderPgData, configPg);

                        isSuccess = orderPgData.isSuccess();
                    }

					// 입금지연 주문 취소 배치 돌 때는 pg로 취소요청 보내지 않음(pg 기취소)
					if (isCancelBatch) {
						isSuccess = true;
					}

					if (isSuccess) {

						if ("vbank".equals(approvalType) && ObjectUtils.isEmpty(payment.getPayDate()) == true) {	//2017.06.21 손준의 가상계좌 입금대기상태의 주문인경우
							orderPaymentMapper.updateOrderPaymentForBankCancel(payment);

							// 현금영수증 상태 업데이트
							CashbillParam cashbillParam = new CashbillParam();
							cashbillParam.setWhere("orderCode");
							cashbillParam.setQuery(payment.getOrderCode());

							Iterable<CashbillIssue> cashbillIssues = cashbillIssueRepository.findAll(cashbillParam.getPredicate());

							for (CashbillIssue cashbillIssue : cashbillIssues) {
								cashbillIssue.setCashbillStatus(CashbillStatus.CANCELED);
								cashbillIssue.setCanceledDate(DateUtils.getToday(Const.DATETIME_FORMAT));
								cashbillIssue.setUpdatedDate(DateUtils.getToday(Const.DATETIME_FORMAT));

								if (UserUtils.isUserLogin() || UserUtils.isManagerLogin()) {
									cashbillIssue.setUpdateBy(UserUtils.getUser().getUserName() + "(" + UserUtils.getLoginId() + ")");
								} else {
									cashbillIssue.setUpdateBy("관리자");
								}
							}
						} else {
							payment.setCancelAmount(payment.getRemainingAmount());
							orderPaymentMapper.updateOrderPaymentForCancel(payment);

							OrderPayment orderPayment = new OrderPayment();
							orderPayment.setPaymentType("2");
							orderPayment.setOrderCode(order.getOrderCode());
							orderPayment.setOrderSequence(order.getOrderSequence());
							orderPayment.setApprovalType(approvalType);
							orderPayment.setCancelAmount(payment.getRemainingAmount());
							orderPayment.setPayDate(DateUtils.getToday(Const.DATETIME_FORMAT));
							orderPayment.setDeviceType("WEB");

							if (ShopUtils.isMobilePage()) {
								orderPayment.setDeviceType("MOBILE");
							}

							orderPayment.setNowPaymentFlag("Y");
							orderPaymentMapper.insertOrderPayment(orderPayment);
						}
					} else {
						throw new OrderException("PG 취소 연동 에러.");
					}
				}
			}
		}

		// 무통장입금 대기건 배치 돌 때 취소X.  사용자나 관리자가 직접 취소 가능.
		if (!"bank".equals(approvalType) || ("bank".equals(approvalType) && !isCancelBatch)) {

			// 쿠폰 반환
			if (!returnCoupons.isEmpty()) {
				for (Integer couponUserId : returnCoupons) {

					if (order.getUserId() > 0) {
						OrderCoupon orderCoupon = new OrderCoupon();
						orderCoupon.setUserId(order.getUserId());
						orderCoupon.setCouponUserId(couponUserId);
						couponMapper.updateCouponUserReturnsByOrderCouponUser(orderCoupon);
					}

				}
			}

			// 재고량 복원
			orderService.stockRestoration(stockMap);

		// 세트상품 재고량 복원
		orderService.stockRestoration(stockSetMap);

		orderMapper.updateOrderCancelAll(orderParam);

			// 사은품 취소 처리
			try {

				if (orderGiftItemList != null && !orderGiftItemList.isEmpty()) {

					HashSet<String> orderGiftItemKeySet = new HashSet<>();

					for (OrderGiftItem giftItem : orderGiftItemList) {

						StringBuffer sb = new StringBuffer();
						String orderCode = giftItem.getOrderCode();
						int orderSequence = giftItem.getOrderSequence();
						int itemSequence = giftItem.getItemSequence();

						sb.append(orderCode);
						sb.append("-");
						sb.append(orderSequence);
						sb.append("-");
						sb.append(itemSequence);

						String key = sb.toString();

						if (orderGiftItemKeySet.contains(key)) {
							continue;
						}

						orderGiftItemService.cancelOrderGiftItem(orderCode, orderSequence, itemSequence);

						orderGiftItemKeySet.add(key);
					}
				}

			} catch (RuntimeException e) {
				throw new ClaimException("1002", "[주문전체취소] 사은품 취소시 문제가 발생했습니다. (" + orderParam.getOrderCode() + ")");
			}

			// 주문 로그
			List<OrderShippingInfo> logOrderShippingInfos = order.getOrderShippingInfos();
			if (logOrderShippingInfos != null && !logOrderShippingInfos.isEmpty()) {
				for (OrderShippingInfo orderShippingInfo : logOrderShippingInfos) {

					List<OrderItem> orderItems = orderShippingInfo.getOrderItems();
					if (orderItems != null && !orderItems.isEmpty()) {
						for (OrderItem orderItem : orderItems) {
							orderService.insertOrderLog(
									OrderLogType.CLAIM_CANCEL,
									orderItem.getOrderCode(),
									orderItem.getOrderSequence(),
									orderItem.getItemSequence(),
									orderItem.getOrderStatus()
							);
						}
					}

				}
			}

		} // if(!isCancelBatch) end

	}

	@Override
	public void orderCancelAllProcessNewTx(OrderParam orderParam) {

		orderCancelAllProcess(orderParam, null);
	}

	@Override
	public List<OrderCancelApply> getAdminApplyCancelListByIds(String[] ids) {

		if (ids == null) {
			return null;
		}

		List<HashMap<String, String>> param = new ArrayList<>();

		for(String key : ids) {
			HashMap<String, String> map = new HashMap<>();

			String[] temp = StringUtils.delimitedListToStringArray(key, "-");
			if (temp.length == 3) {
				map.put("orderCode", temp[0]);
				map.put("orderSequence", temp[1]);
				map.put("itemSequence", temp[2]);

				param.add(map);
			}
		}

		return orderClaimApplyMapper.getAdminApplyCancelListByParam(param);
	}

	@Override
	public void setOrderItemOtherForExchangeApply(List<OrderExchangeApply> list) {

		if (list != null && !list.isEmpty()) {

			HashSet<String> orderCodeSet = new HashSet<>();

			list.stream().forEach(apply -> orderCodeSet.add(apply.getOrderCode()));

			String[] orderCodes = orderCodeSet.toArray(new String[orderCodeSet.size()]);

			List<OrderGiftItem> orderGiftItems = orderGiftItemService.getOrderGiftItemListByOrderCodes(orderCodes);
			List<OrderItem> orderItemSets = orderMapper.getOrderItemSetListByOrderCodes(orderCodes);

			for (OrderExchangeApply apply : list) {
				setOrderItemOtherForClaimApply(apply.getOrderItem(), orderGiftItems, orderItemSets);
			}
		}

	}

	@Override
	public void setOrderItemOtherForReturnApply(List<OrderReturnApply> list) {

		if (list != null && !list.isEmpty()) {

			HashSet<String> orderCodeSet = new HashSet<>();

			list.stream().forEach(apply -> orderCodeSet.add(apply.getOrderCode()));

			String[] orderCodes = orderCodeSet.toArray(new String[orderCodeSet.size()]);

			List<OrderGiftItem> orderGiftItems = orderGiftItemService.getOrderGiftItemListByOrderCodes(orderCodes);
			List<OrderItem> orderItemSets = orderMapper.getOrderItemSetListByOrderCodes(orderCodes);

			for (OrderReturnApply apply : list) {
				setOrderItemOtherForClaimApply(apply.getOrderItem(), orderGiftItems, orderItemSets);
			}
		}

	}

	@Override
	public void setOrderItemOtherForCancelApply(List<OrderCancelApply> list) {
		// 사은품
		if (list != null && !list.isEmpty()) {
			HashSet<String> orderCodeSet = new HashSet<>();

			list.stream().forEach(apply -> orderCodeSet.add(apply.getOrderCode()));

			String[] orderCodes = orderCodeSet.toArray(new String[orderCodeSet.size()]);

			List<OrderGiftItem> orderGiftItems = orderGiftItemService.getOrderGiftItemListByOrderCodes(orderCodes);
			List<OrderItem> orderItemSets = orderMapper.getOrderItemSetListByOrderCodes(orderCodes);

			for (OrderCancelApply apply : list) {
				setOrderItemOtherForClaimApply(apply.getOrderItem(), orderGiftItems, orderItemSets);
			}
		}
	}

	/**
	 * 클레임 공통 주문 상품 정보 세팅
	 * @param orderItem
	 * @param orderGiftItems
	 * @param orderItemSets
	 */
	private void setOrderItemOtherForClaimApply(OrderItem orderItem, List<OrderGiftItem> orderGiftItems, List<OrderItem> orderItemSets) {
		orderSupporter.setOrderItemOtherForClaimApply(orderItem, orderGiftItems, orderItemSets);
	}


	@Override
	public List<OrderShipping> getReShippingAmountForUser(ClaimApply claimApply) {
		return orderSupporter.getReShippingAmountForUser(claimApply);
	}

	@Override
	public List<OrderShipping> getReShippingAmountForManager(ClaimApply claimApply) {
		return orderSupporter.getReShippingAmountForManager(claimApply);
	}

	// 주문취소 신청(기부포인트), isCancel 값이 true 이면 즉시 취소
	@Override
//	@Transactional(rollbackFor = OrderException.class, isolation = Isolation.READ_COMMITTED)
	public void giveGoodsInsertOrderCancelApply(ClaimApply claimApply, boolean isCancel) throws OrderException {
		// 임시
//		String [] ids = {String.valueOf(claimApply.getItemSequence())};
//		claimApply.setId(ids);

		if (claimApply.getId() == null) {
			throw new OrderException();
		}

		String orderCode = claimApply.getOrderCode();
		int orderSequence = claimApply.getOrderSequence();

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(UserUtils.getUserId());
		orderParam.setOrderCode(claimApply.getOrderCode());
		orderParam.setOrderSequence(claimApply.getOrderSequence());

		Order order = orderService.getOrderByParam(orderParam);
		if (order == null) {
			throw new OrderException();
		}

		List<OrderItem> orderItemSets = orderMapper.getOrderItemSetList(orderParam);

		claimApply.setOrder(order);
//		OrderRefund orderRefund = orderRefundService.getOrderCancelRefundForUser(claimApply);
		OrderRefund orderRefund = orderRefundService.getGiveGoodsOrderCancelRefundForUser(claimApply);
		if (orderRefund == null) {
			throw new OrderException();
		}

		OrderGivePoint orderGivePoint = new OrderGivePoint();
		orderGivePoint.setOrderCode(claimApply.getOrderCancelApplys().get(0).getOrderCode());
		List<OrderGivePoint> giveUsePoint = orderGivePointService.getGiveUsePoint(orderGivePoint);

		// 취소 포인트 중 만료된 포인트가 있으면 취소불가 처리해야 할 경우 아래 주석 해제
//		LocalDate now = LocalDate.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//		for (OrderGivePoint usePoint : giveUsePoint) {
//			String endDeStr = usePoint.getPointEndDe();
//			LocalDate endDe = LocalDate.parse(endDeStr, formatter);
//			if (now.isAfter(endDe)) {
//				throw new OrderException("사용된 포인트 중 만료기간이 지난 포인트가 존재하여 취소가 불가능합니다.");
//			}
//		}


		OrderRefundParam orderRefundParam = new OrderRefundParam();
		orderRefundParam.setOrderCode(orderCode);
		orderRefundParam.setOrderSequence(orderSequence);
//		orderRefundParam.setBankInName(claimApply.getReturnBankInName());
//		orderRefundParam.setBankName(claimApply.getReturnBankName());
//		orderRefundParam.setVirtualNo(claimApply.getReturnVirtualNo());

		// 카드 전체취소일 경우 부분취소 가능여부(PART_CANCEL_FLAG)와 상관없이 PG취소
//		boolean isCard = false;
//		for (OrderPayment payment : orderRefund.getOrderPayments()) {
//			// 1. 카드 결제 여부
//			if ("card".equals(payment.getApprovalType())) {
//				isCard = true;
//			}
//		}

		// 2. 총 취소신청 수량
//		int claimApplyQuantity = 0;
// 		for (OrderCancelApply orderCancelApply : claimApply.getOrderCancelApplys()) {
//			claimApplyQuantity += orderCancelApply.getClaimApplyQuantity();
// 		}

		// 3. 즉시취소 처리를 위한 flag - 총 취소신청 수량과 주문 수량이 같으면 즉시 취소
//		boolean isCardAutoCancel = false;
// 		if (isCard && claimApplyQuantity == orderRefund.getTotalOrderQuantity()) {
//			isCardAutoCancel = true;
//		}
 		// 모두 즉시 취소

		// 환불 코드 따기 - 즉시환불일때는 무조건 신규 생성
		/*
		 * if ("1".equals(claimApply.getClaimRefundType())) { // if
		 * (orderRefund.isAutoCancel() == false && isCardAutoCancel == false) { if
		 * (!isCancel) { String refundCode =
		 * orderRefundService.getActiveRefundCodeByParam(orderRefundParam); if
		 * (ObjectUtils.isEmpty(refundCode)) { throw new OrderException(); }
		 *
		 * claimApply.setRefundCode(refundCode); } else {
		 * orderRefundParam.setConditionType("REFUND_FINISH"); String refundCode =
		 * orderRefundService.getNewRefundCodeByParam(orderRefundParam); if
		 * (ObjectUtils.isEmpty(refundCode)) { throw new OrderException(); }
		 *
		 * claimApply.setRefundCode(refundCode); } }
		 */

//		orderParam.setReturnBankInName(orderRefundParam.getBankInName());
//		orderParam.setReturnBankName(orderRefundParam.getBankName());
//		if (ObjectUtils.isEmpty(claimApply.getReturnBankName()) == false) {
//			List<CodeInfo> list = ShopUtils.getBankListByKey(pgType);
//			for (CodeInfo code : list) {
//				if (code.getKey().getId().equals(claimApply.getReturnBankName())) {
//					orderParam.setReturnBankName(code.getLabel());
//					break;
//				}
//			}
//		}
//
//		orderParam.setReturnVirtualNo(orderRefundParam.getVirtualNo());

		// 데이터 암호화
		orderParam.encrypt(orderParamEncryptor);

		// 환불정보업데이트
		orderMapper.updateOrderReturnInfo(orderParam);
		OrderItem orderItem = new OrderItem();
		OrderShipping orderShipping = new OrderShipping();
		List<Integer> returnCoupons = new ArrayList<>();
		HashMap<String, Integer> stockMap = new HashMap<>();

		// 세트상품 용
		HashMap<String, Integer> stockSetMap = new HashMap<>();

		if (isCancel) {
			for (OrderRefundDetail group : orderRefund.getGroups()) {		// 배송 준비 중인 건이 있는지 확인 후 있으면 즉시 취소 해제
				for (OrderCancelApply orderCancelApply : group.getOrderCancelApplys()) {
					orderItem = orderCancelApply.getOrderItem();
					 if (GiveGoodsOrderStatus.PREPARING_DELIVERY.getCode().equals(orderItem.getOrderStatus())) {
						isCancel = false;
						break;
					}
				}
			}
		}
		int itemCnt = 0;

		List<Item> sendSellerList = new ArrayList<>();

		for (OrderRefundDetail group : orderRefund.getGroups()) {

			for (OrderCancelApply orderCancelApply : group.getOrderCancelApplys()) {
				orderItem = orderCancelApply.getOrderItem();
				itemCnt++;
//				if (!"10".equals(orderItem.getOrderStatus()) && !"20".equals(orderItem.getOrderStatus())) {		// 결제 완료, 배송준비중 상태가 아니면 취소 처리 해제
				if (!GiveGoodsOrderStatus.COMPLETE_PAYMENT.getCode().equals(orderItem.getOrderStatus())
						&& !GiveGoodsOrderStatus.PREPARING_DELIVERY.getCode().equals(orderItem.getOrderStatus())) {		// 결제 완료, 배송준비중 상태가 아니면 취소 처리 해제
					throw new OrderException("주문 취소 가능한 상태가 아닌 답례품이 있습니다.");
				}

				if ("1".equals(
						claimApply.getClaimRefundType()) /* && (orderRefund.isAutoCancel() || isCardAutoCancel) */ && isCancel) {
					orderCancelApply.setRefundCode(claimApply.getRefundCode());
//					orderCancelApply.setClaimStatus("04");
					orderCancelApply.setClaimStatus(CancelClaimStatus.CANCEL_COMPLETE.getCode());

					if (orderItem.getCouponUserId() > 0) {
						returnCoupons.add(orderItem.getCouponUserId());
					}

					if (orderItem.getAddCouponUserId() > 0) {
						returnCoupons.add(orderItem.getAddCouponUserId());
					}

					// 재고 복원해야하는 목록 만들기
					orderService.makeStockRestorationMap(stockMap, orderItem, orderCancelApply.getClaimApplyQuantity());

					// 세트상품 재고 복원 목록
					if ("Y".equals(orderItem.getSetItemFlag()) && orderItemSets != null && !orderItemSets.isEmpty()) {
						orderItem.setItemSets(orderService.filterOrderItemSets(orderItemSets, orderItem.getOrderCode(), orderItem.getOrderSequence(), orderItem.getItemSequence()));

						for (OrderItem orderItemSet : orderItem.getItemSets()) {
							orderService.makeStockRestorationMap(stockSetMap, orderItemSet, orderItem.getQuantity() * orderItemSet.getQuantity());
						}
					}
				} else {
//					orderCancelApply.setClaimStatus("01");
					orderCancelApply.setClaimStatus(CancelClaimStatus.CANCEL_REQUEST.getCode());
				}

				orderCancelApply.setCancelReason(claimApply.getClaimReason());
				orderCancelApply.setCancelReasonText(claimApply.getClaimReasonText());
				orderCancelApply.setCancelReasonDetail(claimApply.getClaimReasonDetail());
				orderCancelApply.setItemSequence(orderItem.getItemSequence());
				orderCancelApply.setClaimApplySubject("01");

				orderCancelApply.setClaimApplyQuantity(orderCancelApply.getClaimApplyQuantity());

				if ("1".equals(
						claimApply.getClaimRefundType()) /* && (orderRefund.isAutoCancel() || isCardAutoCancel) */ && isCancel) {
					orderCancelApply.setSalesCancel(true);
				}

				// 잔여 수량이 0이라면..
				if (orderItem.getQuantity() == 0) {
					// 취소 생성
					orderClaimApplyMapper.insertOrderCancelApply(orderCancelApply);
					// update order_item claim quantity
					orderClaimApplyMapper.updateClaimQuantityForCancelApply(orderCancelApply);
				} else {
					// order item 복제, item_sequence +1
					orderClaimApplyMapper.copyOrderItemForCancelApply(orderCancelApply);
					// update order_item claim quantity = cancel quantity 복원
					orderClaimApplyMapper.updateOrderItemQuantityForCancel(orderCancelApply);

					// 중요!!
					orderCancelApply.setItemSequence(orderCancelApply.getCopyItemSequence());
					//취소 생성
					orderClaimApplyMapper.insertOrderCancelApply(orderCancelApply);
				}

				// ====== 세트상품 수량 설정 ======================
				/*
				if (orderCancelApply.getOrderItem().getItemSets() != null && orderCancelApply.getOrderItem().getItemSets().size() > 0) {
					int copySetItemSequence = orderClaimApplyMapper.getMaxSetItemSequence(orderParam);
					orderCancelApply.setSetClaimCode(orderCancelApply.getClaimCode());
					orderCancelApply.setCopySetItemSequence(copySetItemSequence);

					for (OrderItem itemSet : orderCancelApply.getOrderItem().getItemSets()) {
						orderCancelApply.setSetItemSequence(itemSet.getSetItemSequence());
						orderCancelApply.setItemSequence(itemSet.getItemSequence());
						orderCancelApply.setClaimApplyQuantity(itemSet.getQuantity());

						if (orderItem.getQuantity() == 0) {
							orderClaimApplyMapper.insertOrderSetCancelApply(orderCancelApply);
							orderClaimApplyMapper.updateSetClaimQuantityForCancelApply(orderCancelApply);
						} else {
							// 부분취소 시 row 추가 생성
							orderClaimApplyMapper.copyOrderItemSetForCancelApply(orderCancelApply);

							orderCancelApply.setSetItemSequence(copySetItemSequence);
							orderClaimApplyMapper.insertOrderSetCancelApply(orderCancelApply);
						}
					}
				}
				*/


				if (!isCancel) {			// 판매자 명단
					String sellerLoginId = group.getSeller().getLoginId();
					boolean exist = false;
					seller : for (Item seller : sendSellerList) {
						if (sellerLoginId.equals(seller.getManagerLoginId()) && orderItem.getItemId() == seller.getItemId()) {
							exist = true;
							break seller;
						}
					}

					if (!exist) {
						Item sellerInfo = new Item();
						sellerInfo.setManagerLoginId(sellerLoginId);
						sellerInfo.setItemId(orderItem.getItemId());
						sellerInfo.setItemNoticeCode(claimApply.getOrderCode());
						sellerInfo.setItemOptionTitle1(orderItem.getOptions());
						sellerInfo.setItemSummary(claimApply.getClaimReasonText().equals("기타") ? claimApply.getClaimReasonDetail() : claimApply.getClaimReasonText());

						sendSellerList.add(sellerInfo);
					}
				}

//				같은 상품 여러 건일 경우 답례품 제공자에게 한번만 발송하도록 변경
//				// 국민비서 알림 전송 (답례품 취소 접수시) - 판매자
//				try {
//					if (!isCancel) {		// 즉시 취소건이 아닐 경우
//						User user = userService.getUserByUserId(UserUtils.getUserId());
//
//						//Seller seller = sellerService.getSellerById(orderItem.getSellerId());
//						SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdForSms(group.getSeller().getLoginId());
//		    			if ("0".equals(sellerUser.getReceiveSms())
//		    					&& StringUtils.hasLength(sellerUser.getPhoneNumber())
//		    					&& StringUtils.hasLength(sellerUser.getMberCi())) {
//							List<ReceiverInfo> receiverInfos = new ArrayList<>();
//							ReceiverInfo receiverInfo = new ReceiverInfo();
//							receiverInfo.setSmsType(SmsType.PRESENT_CANCEL_REGISTER);
//							receiverInfo.setPrvcIdntfcInfo(sellerUser.getMberCi());
//							StringBuilder sb = new StringBuilder();
//							sb.append(user.getUserName());	// 이름
//							sb.append("|");
//							sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
//							sb.append("|");
//							sb.append(claimApply.getOrderCode());	// 주문번호
//							sb.append("|");
//							sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()));		// 답례품명
//			    			if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
//			    				sb.append("(");
//			    				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
//			    				sb.append(")");
//			    			}
//							sb.append("|");
//							sb.append(claimApply.getClaimReasonText().equals("기타") ? claimApply.getClaimReasonDetail() : claimApply.getClaimReasonText());		// 사유
//							sb.append("|");
//							sb.append(sellerUser.getPhoneNumber().replaceAll("-", ""));
//							receiverInfo.setSndngCntnts(sb.toString());
//							receiverInfos.add(receiverInfo);
//							smsIpsService.insertTifIpsSndngM(receiverInfos);
//		    			}
//					}
//				} catch (NullPointerException | ClassCastException e) {
//					log.error(getClass().getName() +  " :: giveGoodsInsertOrderCancelApply send sms error", e);
//				}
			}

			if ("1".equals(claimApply.getClaimRefundType()) /* && (orderRefund.isAutoCancel() || isCardAutoCancel) */ && isCancel) {
				for(OrderAddPayment orderAddPayment : group.getOrderAddPayments()) {

					if ("NO_INSERT".equals(orderAddPayment.getIssueCode())) {
						continue;
					}

					orderAddPayment.setRefundCode(claimApply.getRefundCode());
					orderAddPaymentService.insertOrderAddPayment(orderAddPayment);

					orderShipping = orderAddPayment.getOrderShipping();
					orderShipping.setPayShipping(orderShipping.getRePayShippingAmount());
					orderShipping.setRemittanceAmount(orderShipping.getRePayShippingAmount());

					if ("2".equals(orderShipping.getAddPaymentType())) {
						orderShipping.setReturnShipping(orderShipping.getReturnShipping() + orderShipping.getAddPayAmount());
					}

					orderShippingMapper.updateCancelShipping(orderShipping);

				}
			}
		}

//		같은 상품 여러 건일 경우 답례품 제공자에게 한번만 발송하도록 변경(모바일 등)
		if (!isCancel) {
//			for (Item sellerInfo : sendSellerList) {
				// 국민비서 알림 전송 (답례품 취소 접수시) - 판매자
				try {
					if (!isCancel) {		// 즉시 취소건이 아닐 경우
						User user = userService.getUserByUserId(order.getUserId());
						Seller seller = sellerService.getSellerById(orderItem.getSellerId());
						SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdForSms(seller.getLoginId());
		    			if ("0".equals(sellerUser.getReceiveSms())
		    					&& StringUtils.hasLength(sellerUser.getPhoneNumber())
		    					&& StringUtils.hasLength(sellerUser.getMberCi())) {
							List<ReceiverInfo> receiverInfos = new ArrayList<>();
							ReceiverInfo receiverInfo = new ReceiverInfo();
							receiverInfo.setSmsType(SmsType.PRESENT_CANCEL_REGISTER);
							receiverInfo.setPrvcIdntfcInfo(sellerUser.getMberCi());
							StringBuilder sb = new StringBuilder();
							sb.append(user.getUserName());	// 주문자 이름
							sb.append("|");
							sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
							sb.append("|");
							sb.append(claimApply.getOrderCode());	// 주문번호
							sb.append("|");
							sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));		// 답례품명
			    			if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
			    				sb.append("(");
			    				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
			    				sb.append(")");
			    			}
							sb.append("|");
							sb.append(claimApply.getClaimReasonText().equals("기타") ? claimApply.getClaimReasonDetail() : claimApply.getClaimReasonText());		// 사유
							sb.append("|");
							sb.append(sellerUser.getPhoneNumber().replaceAll("-", ""));	// 판매자 전화번호
							receiverInfo.setSndngCntnts(sb.toString());
							receiverInfos.add(receiverInfo);
							smsIpsService.insertTifIpsSndngM(receiverInfos);

//							/* 팝빌 카카오 알림톡 전송 */
//							Alimtalk alimtalk = new Alimtalk();
//							alimtalk.setTemplateCode("026030000377");
//							alimtalk.setReceiverNum(sellerUser.getPhoneNumber());
//							alimtalk.setReceiverName(user.getUserName());
//							alimtalk.setOrderDate(alimtalk.getLocalDateTimeToStr());
//							alimtalk.setOrderNo(orderCode);
//
//							StringBuilder orderItemName = new StringBuilder();
//							orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));
//							if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
//								orderItemName.append("(");
//								orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));
//								orderItemName.append(")");
//							}
//							alimtalk.setOrderName(orderItemName.toString());
//
//							alimtalk.setClaimReasonDetail(claimApply.getClaimReasonText().equals("기타") ? claimApply.getClaimReasonDetail() : claimApply.getClaimReasonText());
//
//							alimtalkService.sendAlimtalk(alimtalk);

							/* 국자원 모바일 메신저 알림톡 전송 */
							Nuri2NrmsgData nuri2NrmsgData = new Nuri2NrmsgData();

							// 아이템(옵션)
							StringBuilder orderItemName = new StringBuilder();
							orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));
							if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
								orderItemName.append("(");
								orderItemName.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));
								orderItemName.append(")");
							}

							// 템플릿코드
							nuri2NrmsgData.setAltTemplateCode("KR002");
							// 수신번호
							nuri2NrmsgData.setPhone(sellerUser.getPhoneNumber().replaceAll("-", ""));
							// ALT_JSON
							nuri2NrmsgData.setAltJson(nuri2NrmsgData.getContent("PRESENT_CANCEL_REGISTER", user.getUserName(), receiverInfo.getLocalDateTimeToStr(), orderCode, orderItemName.toString(), ""));
							// XMS_TEST
							nuri2NrmsgData.setXmsText(nuri2NrmsgData.getContent("PRESENT_CANCEL_REGISTER", user.getUserName(), receiverInfo.getLocalDateTimeToStr(), orderCode, orderItemName.toString(), ""));

							nuri2Service.insertAlimtalk(nuri2NrmsgData);


		    			}
					}
				} catch (NullPointerException | ClassCastException e) {
					log.error(getClass().getName() +  " :: giveGoodsInsertOrderCancelApply send sms error", e);
				} catch (Exception e) {
					log.error(getClass().getName() +  " :: giveGoodsInsertOrderCancelApply send error", e);
				}
//			}
		}

		// 답례품 취소 접수시
		if("2".equals(claimApply.getClaimRefundType())) {
			// 국민비서 알림 전송 (답례품 취소 접수시) - 구매자
			try {
				if (!isCancel) {		// 즉시 취소건이 아닐 경우
					User user = userService.getUserByUserId(order.getUserId());
					UserDetail userDetail = (UserDetail) user.getUserDetail();
					userDetail.decrypt(userDetailEncryptor, false);
	    			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {
						List<ReceiverInfo> receiverInfos = new ArrayList<>();
						ReceiverInfo receiverInfo = new ReceiverInfo();
						receiverInfo.setSmsType(SmsType.PRESENT_CANCEL_REGISTER);
						receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
						StringBuilder sb = new StringBuilder();
						sb.append(user.getUserName());	// 주문자 이름
						sb.append("|");
						sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
						sb.append("|");
						sb.append(claimApply.getOrderCode());	// 주문번호
						sb.append("|");
						OrderItem smsOrderItem = orderRefund.getGroups().get(0).getOrderCancelApplys().get(0).getOrderItem();
						sb.append(StringEscapeUtils.unescapeHtml(smsOrderItem.getItemName()).replaceAll("\\|", "-"));		// 답례품명
		    			if (StringUtils.hasLength(smsOrderItem.getOptions()) && !smsOrderItem.getOptions().contains("|")) {
		    				sb.append("(");
		    				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
		    				sb.append(")");
		    			}
		    			if (itemCnt > 1) {
		    				sb.append(" 등");
		    			}
						sb.append("|");
						sb.append(claimApply.getClaimReasonText().equals("기타") ? claimApply.getClaimReasonDetail() : claimApply.getClaimReasonText());		// 사유
						sb.append("|");
						sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));	// 구매자 전화번호
						receiverInfo.setSndngCntnts(sb.toString());
						receiverInfos.add(receiverInfo);
						smsIpsService.insertTifIpsSndngM(receiverInfos);
	    			}
				}
			} catch (NullPointerException | ClassCastException e) {
				log.error(getClass().getName() +  " :: giveGoodsInsertOrderCancelApply send sms error", e);
			}
		}

		if ("1".equals(claimApply.getClaimRefundType()) /* && (orderRefund.isAutoCancel() || isCardAutoCancel) */ && isCancel) {

			List<OrderGivePoint> cancelRequestPointList = new ArrayList<>();

			for (OrderCancelApply orderCancelApply : claimApply.getOrderCancelApplys()) {
				String orderItemLocgovCode = orderCancelApply.getOrderItem().getLocgovCode();
				long cancelPoint = orderCancelApply.getClaimApplyQuantity() * orderCancelApply.getOrderItem().getSalePrice();
				OrderGivePoint cancelRequestPoint = new OrderGivePoint();
				boolean exists = false;
				check : for (OrderGivePoint givePoint : cancelRequestPointList) {
					if (givePoint.getCntrLocgovCode().equals(orderItemLocgovCode)) {
						cancelRequestPoint = givePoint;
						exists = true;
						break check;
					}
				}
				cancelRequestPoint.setCntrBlcePoint(cancelRequestPoint.getCntrBlcePoint() + cancelPoint);
				if (!exists) {
					cancelRequestPoint.setCntrLocgovCode(orderItemLocgovCode);
					cancelRequestPointList.add(cancelRequestPoint);
				}
			}

//			OrderGivePoint orderGivePoint = new OrderGivePoint();
//			orderGivePoint.setOrderCode(claimApply.getOrderCancelApplys().get(0).getOrderCode());
//			List<OrderGivePoint> giveUsePoint = orderGivePointService.getGiveUsePoint(orderGivePoint);
			List<OrderGivePoint> givePointList = orderGivePointService.getCntrPointList(giveUsePoint);

			orderService.refundGiveGoodsPoint2(cancelRequestPointList, giveUsePoint, givePointList);

			// 재고량 복원
			orderService.stockRestoration(stockMap);

			// 세트상품 재고량 복원
			orderService.stockRestoration(stockSetMap);
		}

		// 주문 로그
		OrderItem logOrderItem = new OrderItem();
		for(OrderRefundDetail group : orderRefund.getGroups()) {
			for (OrderCancelApply orderCancelApply : group.getOrderCancelApplys()) {
				logOrderItem = orderCancelApply.getOrderItem();

				orderService.insertOrderLog(
						OrderLogType.CLAIM_CANCEL,
						orderCancelApply.getOrderCode(),
						orderCancelApply.getOrderSequence(),
						logOrderItem.getItemSequence(),
						logOrderItem.getOrderStatus()
				);

			}
		}

	}

	@Override
//	@Transactional(rollbackFor = OrderException.class, isolation = Isolation.READ_COMMITTED)
	public void giveGoodsOrderCancelProcess(ClaimApply claimApply) {

		if (claimApply.getCancelIds() == null) {
			return;
		}

//		boolean rePayShipping = false;
//		int currentItemCount [] = {0,0};
//
//		// 배송비 관련 주석 처리
//		for(String claimCode : claimApply.getCancelIds()) {
//			OrderCancelApply cancelApply = claimApply.getCancelApplyMap().get(claimCode);
//			if (cancelApply == null) {
//				throw new OrderException();
//			}
//
//			String claimStatus = cancelApply.getClaimStatus();
//			if ("03".equals(claimStatus)) {
//
//			    currentItemCount[cancelApply.getShippingSequence()] ++;
//
//				for(OrderCancelShipping orderCancelShipping : claimApply.getCancelShippingMap().values()) {
//                    int addPaymentCount = orderAddPaymentMapper.getOrderAddPaymentCount(cancelApply);
//                    int leftItem = orderMapper.getOrderItemCountForCancel(cancelApply);
//
//                    if (addPaymentCount > 0 && leftItem > currentItemCount[cancelApply.getShippingSequence()]) {
//                        rePayShipping = false;
//                        break;
//                    }
//
//					// 배송비 재계산??
//					if ("Y".equals(orderCancelShipping.getRePayShipping())) {
//						rePayShipping = true;
//						break;
//
//					}
//
//				}
//
//			}
//
//			if (rePayShipping) {
//				break;
//			}
//		}
//
//
//		if (rePayShipping) {
//
//			List<OrderShipping> orderShippings = getReShippingAmountForManager(claimApply);
//
//			/**
//			 * CJH 2016.08.20
//			 * 주문 취소는 무조건!! 정산일자가 확정되기 전에 일어나는일이라고 가정하고 작성한다..
//			 * 배송비 변경건은 OP_ORDER_SHIPPING에 정산 금액을 조절하고 OP_ADD_PAYMENT 테이블에는 환불 금액관련 정보를 기록하고 사용한다.
//			 */
//			if (orderShippings != null) {
//				for(OrderShipping orderShipping : orderShippings) {
//
//					if (orderShipping.getAddPayAmount() == 0) {
//
//						if ("2".equals(orderShipping.getShippingPaymentType())) {
//							orderShipping.setPayShipping(orderShipping.getRePayShippingAmount());
//							orderShipping.setRemittanceAmount(orderShipping.getRePayShippingAmount());
//							orderShipping.setReturnShipping(orderShipping.getReturnShipping() + orderShipping.getAddPayAmount());
//
//							orderShippingMapper.updateCancelShipping(orderShipping);
//						}
//
//					} else {
//
//						OrderAddPayment orderAddPayment = new OrderAddPayment(orderShipping);
//						orderAddPayment.setAddPaymentType(orderShipping.getAddPaymentType());
//						orderAddPayment.setAmount(orderShipping.getAddPayAmount());
//						orderAddPayment.setRefundCode(claimApply.getRefundCode());
//						orderAddPayment.setSalesDate(DateUtils.getToday());
//						orderAddPayment.setSubject("주문 취소로 인한 배송비 변경");
//						orderAddPayment.setIssueCode("CANCEL-ADD-SHIPPING-" + orderShipping.getShippingSequence());
//						orderAddPaymentService.insertOrderAddPayment(orderAddPayment);
//
//                        if ("1".equals(orderShipping.getAddPaymentType())) {
//                            orderShipping.setPayShipping(orderShipping.getRePayShippingAmount());
//                            orderShipping.setRemittanceAmount(orderShipping.getRePayShippingAmount());
//                            orderShipping.setRealShipping(orderShipping.getRePayShippingAmount());
//                        } else if ("2".equals(orderShipping.getAddPaymentType())) {
//                            orderShipping.setPayShipping(orderShipping.getRePayShippingAmount());
//                            orderShipping.setRemittanceAmount(orderShipping.getRePayShippingAmount());
//                            orderShipping.setReturnShipping(orderShipping.getReturnShipping() + orderShipping.getAddPayAmount());
//                            orderShipping.setRealShipping(orderShipping.getRePayShippingAmount());
//                        }
//
//                        orderShippingMapper.updateCancelShipping(orderShipping);
//					}
//				}
//			}
//		}

		List<OrderCancelApply> pointCancelList = new ArrayList<>();

		HashMap<String, Integer> stockMap = new HashMap<>();

		// 세트상품 용
		HashMap<String, Integer> stockSetMap = new HashMap<>();

		int totalProcessCnt = 0;

		for(String key : claimApply.getCancelIds()) {

			OrderCancelApply orderCancelApplyParam = claimApply.getCancelApplyMap().get(key);

			String inputClaimStatus = orderCancelApplyParam.getClaimStatus();
			OrderCancelApply orderCancelApply = orderClaimApplyMapper.getOrderCancelApplyByClaimCode(orderCancelApplyParam.getClaimCode());

			if (orderCancelApply == null) {
				throw new OrderException("주문 취소 처리 중 문제가 발생했습니다.");
			}

			orderCancelApply.setClaimStatus(inputClaimStatus);
			orderCancelApply.setSalesCancel(true);			// 바로 환불 처리

            // 포인트 취소 계산 / 주문 로그 위해 주문상품 조회
            OrderItem logOrderItem = orderService.getOrderItemForOrderLog(
                    orderCancelApply.getOrderCode(),
                    orderCancelApply.getOrderSequence(),
                    orderCancelApply.getItemSequence()
            );

//			orderCancelApply.setClaimCode(key);

//			if ("98".equals(orderCancelApply.getClaimStatus())) { // 배송처리
			if (CancelClaimStatus.CANCEL_REFUSAL_SHIPPING.getCode().equals(orderCancelApply.getClaimStatus())) { // 배송처리
				if (!StringUtils.hasLength(orderCancelApplyParam.getCancelRefusalReasonText())) {
					throw new OrderException("거절사유를 입력해주세요.");
				}

				orderCancelApply.setDeliveryCompanyId(orderCancelApplyParam.getDeliveryCompanyId());
				orderCancelApply.setCancelRefusalReasonText(orderCancelApplyParam.getCancelRefusalReasonText());
				orderCancelApply.setDeliveryNumber(orderCancelApplyParam.getDeliveryNumber());

				ShippingParam shippingParam = new ShippingParam(orderCancelApply);

				OrderItem orderItem = orderCancelApply.getOrderItem();
				if ("MOBILE".equalsIgnoreCase(orderItem.getCampaignCode())) {
					if (!StringUtils.hasLength(orderCancelApplyParam.getDeliveryNumber())) {
						throw new OrderException("모바일번호를 입력해주세요.");
					}
					shippingParam.setDeliveryCompanyName("모바일");
					shippingParam.setMobileNumber(orderCancelApplyParam.getDeliveryNumber());
					shippingParam.setMode("MOBILE");
				} else {
					DeliveryCompany deliveryCompany = deliveryCompanyService.getDeliveryCompanyById(orderCancelApply.getDeliveryCompanyId());
					if (deliveryCompany == null) {
						throw new OrderException("택배사 정보가 없습니다.");
					}
					if (!StringUtils.hasLength(orderCancelApplyParam.getDeliveryNumber())) {
						throw new OrderException("송장번호를 입력해주세요.");
					}
					shippingParam.setDeliveryCompanyName(deliveryCompany.getDeliveryCompanyName());
					shippingParam.setDeliveryCompanyUrl(deliveryCompany.getDeliveryCompanyUrl());
					shippingParam.setDeliveryNumber(orderCancelApplyParam.getDeliveryNumber());
				}
				shippingParam.setAdminUserName(UserUtils.getManagerName());
				shippingParam.setConditionType("OPMANAGER");

				if (ShopUtils.isSellerPage()) {
					shippingParam.setConditionType("SELLER");
					shippingParam.setSellerId(SellerUtils.getSellerId());
				}

                // 주문 로그인해 임시로 주문상품 조회
//                OrderItem logOrderItem = orderService.getOrderItemForOrderLog(
//                        shippingParam.getOrderCode(),
//                        shippingParam.getOrderSequence(),
//                        shippingParam.getItemSequence()
//                );
				if ("MOBILE".equalsIgnoreCase(orderItem.getCampaignCode())) {
					shippingParam.setMode("CANCEL_MOBILE");
				} else {
					shippingParam.setMode("CANCEL");
				}
				if (orderShippingMapper.updateShippingStart(shippingParam) == 0) {
					throw new OrderException("주문 취소 처리 중 문제가 발생했습니다.");
				}

//				orderCancelApply.setClaimStatus("99");
				orderCancelApply.setClaimStatus(CancelClaimStatus.CANCEL_REFUSAL.getCode());

				// 주문 로그
				try {
                    orderService.insertOrderLog(
                            OrderLogType.CLAIM_CANCEL,
                            shippingParam.getOrderCode(),
                            shippingParam.getOrderSequence(),
                            shippingParam.getItemSequence(),
                            logOrderItem.getOrderStatus()
                    );
                } catch (RuntimeException e) {
//				    log.error("ERROR: {}", e.getMessage(), e);
				    log.error("ERROR: {}", getClass().getName() + " :: giveGoodsOrderCancelProcess RuntimeException1 ==========", e);
                }
			}

			// 환불 승인 대기이면 환불 코드를 등록
			// 클래임 상태 (01:신청, 02:보류(처리중), 03:승인(환불대기), 04:완료(환불완료), 99:거절)
//			if ("03".equals(orderCancelApply.getClaimStatus())) {
//				orderCancelApply.setRefundCode(claimApply.getRefundCode());
//			}


			// 신청 완료 처리 환불코드 채번
//			if ("03".equals(orderCancelApply.getClaimStatus())) {
				if (CancelClaimStatus.CANCEL_APPROVAL.getCode().equals(orderCancelApply.getClaimStatus())) {
	//				orderCancelApply.setClaimStatus("04");
					orderCancelApply.setClaimStatus(CancelClaimStatus.CANCEL_COMPLETE.getCode());
/* 2023-06-14 modify by junghh
					if (StringUtils.isEmpty(orderCancelApply.getRefundCode())) {
						OrderRefundParam orderRefundParam = new OrderRefundParam();
						orderRefundParam.setOrderCode(claimApply.getOrderCode());
						orderRefundParam.setOrderSequence(claimApply.getOrderSequence());
						orderRefundParam.setConditionType("REFUND_FINISH");

						String refundCode = orderRefundService.getNewRefundCodeByParam(orderRefundParam);
						if (ObjectUtils.isEmpty(refundCode)) {
							throw new OrderException("주문 취소 처리 중 문제가 발생했습니다.");
						}

	//					claimApply.setRefundCode(refundCode);
						orderCancelApply.setRefundCode(refundCode);
					}
*/
				}

			int count = orderClaimApplyMapper.updateOrderCancelApply(orderCancelApply);
//			if (("99".equals(orderCancelApply.getClaimStatus())) && count > 0) {
			if ((CancelClaimStatus.CANCEL_REFUSAL.getCode().equals(orderCancelApply.getClaimStatus())) && count > 0) {		// 위에 취소 후 배송처리 존재하고 취소거절만 하는 경우는 없어서 주석처리

//                // 주문 로그인해 임시로 주문상품 조회
//                OrderItem logOrderItem = orderService.getOrderItemForOrderLog(
//                        orderCancelApply.getOrderCode(),
//                        orderCancelApply.getOrderSequence(),
//                        orderCancelApply.getItemSequence()
//                );

				orderClaimApplyMapper.updateClaimQuantityForCancel(orderCancelApply);

                // 주문 로그
                try {
                    orderService.insertOrderLog(
                            OrderLogType.CLAIM_CANCEL,
                            orderCancelApply.getOrderCode(),
                            orderCancelApply.getOrderSequence(),
                            orderCancelApply.getItemSequence(),
                            logOrderItem.getOrderStatus()
                    );
                } catch (RuntimeException e) {
//                    log.error("ERROR: {}", e.getMessage(), e);
				    log.error("ERROR: {}", getClass().getName() + " :: giveGoodsOrderCancelProcess RuntimeException2 ==========", e);
                }
//			} else if (("04".equals(orderCancelApply.getClaimStatus())) && count > 0) {
			} else if ((CancelClaimStatus.CANCEL_COMPLETE.getCode().equals(orderCancelApply.getClaimStatus())) && count > 0) {
				// 취소 완료 처리
                orderClaimApplyMapper.updateClaimQuantityForCancelApply(orderCancelApply);

				// 재고량 복원 추가
				// 재고 복원해야하는 목록 만들기
                OrderItem orderItem = orderCancelApply.getOrderItem();
				orderService.makeStockRestorationMap(stockMap, orderItem, orderCancelApply.getClaimApplyQuantity());

				List<OrderItem> orderItemSets = orderItem.getItemSets();

				if(orderItemSets != null && !orderItemSets.isEmpty()) {
					// 세트상품 재고 복원 목록
					if ("Y".equals(orderItem.getSetItemFlag()) && orderItemSets != null && !orderItemSets.isEmpty()) {
						orderItem.setItemSets(orderService.filterOrderItemSets(orderItemSets, orderItem.getOrderCode(), orderItem.getOrderSequence(), orderItem.getItemSequence()));

						for (OrderItem orderItemSet : orderItem.getItemSets()) {
							orderService.makeStockRestorationMap(stockSetMap, orderItemSet, orderItem.getQuantity() * orderItemSet.getQuantity());
						}
					}
				}
				// 재고량 복원 추가


                // 주문 로그 추가
                try {
                    orderService.insertOrderLog(
                            OrderLogType.CLAIM_CANCEL,
                            orderCancelApply.getOrderCode(),
                            orderCancelApply.getOrderSequence(),
                            orderCancelApply.getItemSequence(),
                            logOrderItem.getOrderStatus()
                    );
                } catch (RuntimeException e) {
//                    log.error("ERROR: {}", e.getMessage(), e);
				    log.error("ERROR: {}", getClass().getName() + " :: giveGoodsOrderCancelProcess RuntimeException3 ==========", e);
                }
            }
//			if (count > 0 && logOrderItem != null && "04".equals(orderCancelApply.getClaimStatus())) {			// 포인트 환불 세팅
			if (count > 0 && logOrderItem != null && CancelClaimStatus.CANCEL_COMPLETE.getCode().equals(orderCancelApply.getClaimStatus())) {			// 포인트 환불 세팅
				orderCancelApply.setClaimApplyAmount(logOrderItem.getSalePrice() * orderCancelApply.getClaimApplyQuantity());
				pointCancelList.add(orderCancelApply);
				totalProcessCnt++;
			}
		}


		// 재고량 복원
		orderService.stockRestoration(stockMap);

		// 세트상품 재고량 복원
		orderService.stockRestoration(stockSetMap);

		// 포인트 환불
//		if (claimApply.getId() == null) {
//			throw new OrderException();
//		}

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(UserUtils.getUserId());
		orderParam.setOrderCode(claimApply.getOrderCode());
		orderParam.setOrderSequence(claimApply.getOrderSequence());

//		List<OrderCancelApply> cancelApplyList = claimApply.getOrderCancelApplys();
//
//		if (cancelApplyList != null) {
//			for(OrderCancelApply orderCancelApply : cancelApplyList) {
//				totalItemReturnAmount += orderCancelApply.getClaimApplyAmount();
//			}
//		}

		List<OrderPayment> orderPaymentList = orderRefundService.getOrderPaymentListByParam(orderParam);

		List<OrderGivePoint> orderGivePoints = new ArrayList<>();
		List<OrderGivePoint> remainGivePoints = new ArrayList<>();
		// 사용포인트 조회
//		for (OrderCancelApply orderCancelApply : claimApply.getOrderCancelApplys()) {
		for(String key : claimApply.getCancelIds()) {
			OrderCancelApply orderCancelApply = claimApply.getCancelApplyMap().get(key);
			orderCancelApply = orderClaimApplyMapper.getOrderCancelApplyByClaimCode(orderCancelApply.getClaimCode());

			if (orderCancelApply == null) {
				throw new OrderException("주문 취소 처리 중 문제가 발생했습니다.");
			}

			OrderGivePoint orderGivePointParam = new OrderGivePoint();
			orderGivePointParam.setUserId(orderCancelApply.getUserId());
			orderGivePointParam.setCntrLocgovCode(orderCancelApply.getOrderItem().getLocgovCode());
			orderGivePointParam.setOrderCode(orderCancelApply.getOrderCode());

			orderGivePointParam.setCntrLocgovCode(orderCancelApply.getOrderItem().getLocgovCode());
			List<OrderGivePoint> useGivePoints = orderGivePointService.getGiveUsePoint(orderGivePointParam);

			boolean exist = false;
			check: for (OrderGivePoint orderGivePoint : orderGivePoints) {
				for (OrderGivePoint useGivePoint : useGivePoints) {
					if (orderGivePoint.getOrderCode().equalsIgnoreCase(useGivePoint.getOrderCode())) {
						exist = true;
						break check;
					}
				}
			}

			if (!exist) {
				orderGivePoints.addAll(useGivePoints);
				for (OrderGivePoint useGivePoint : useGivePoints) {
					useGivePoint.setAddZero(true);
					exist = false;
					OrderGivePoint remainGivePoint = orderGivePointService.getGiveBlcePointListByCntrSn(useGivePoint);
					for (OrderGivePoint givePoint : remainGivePoints) {
						if (remainGivePoint.getCntrSn().equalsIgnoreCase(givePoint.getCntrSn())) {
							exist = true;
						}
					}
					if (!exist) {
						remainGivePoints.add(remainGivePoint);
					}
				}
			}
		}

		int refundCnt = 0;
		for (OrderPayment payment : orderPaymentList) {
			for (OrderCancelApply orderCancelApply : pointCancelList) {
				if (orderCancelApply.getOrderCode().equals(payment.getOrderCode())
						&& orderCancelApply.getOrderSequence() == payment.getOrderSequence()
						/*&& orderCancelApply.getItemSequence() == payment.getItemSequence()*/			// 결제 정보에 상품 순번 정보 없음
//						&& "04".equals(orderCancelApply.getClaimStatus())) {
						&& CancelClaimStatus.CANCEL_COMPLETE.getCode().equals(orderCancelApply.getClaimStatus())) {
					orderService.refundGiveGoodsPoint(payment, orderCancelApply, orderGivePoints, remainGivePoints);
					refundCnt++;
				}
			}
		}
		if (totalProcessCnt != refundCnt) {
			throw new OrderException("옵션 구성이 변경되었습니다.");
		}
	}

	public List<OrderLog> getOrderLogList(ClaimApplyParam claimApplyParam) {
		// return orderClaimApplyMapper.getOrderLogList(claimApplyParam);
		List<OrderLog> logs = orderClaimApplyMapper.getOrderLogList(claimApplyParam);
		if (logs != null && !logs.isEmpty()) {
			for (OrderLog orderLog : logs) {
				orderLog.setOptions(ShopUtils.viewOptionTextNoUl(orderLog.getOptions()));
			}
		}
		return logs;
	}

}
