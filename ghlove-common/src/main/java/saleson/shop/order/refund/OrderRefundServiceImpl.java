package saleson.shop.order.refund;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringEscapeUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CommonUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.common.enumeration.IdType;
import saleson.common.enumeration.OrderLogType;
import saleson.common.enumeration.SmsType;
import saleson.common.notification.ApplicationInfoService;
import saleson.common.notification.UnifiedMessagingService;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerMapper;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.shop.config.ConfigMapper;
import saleson.shop.coupon.CouponMapper;
import saleson.shop.coupon.domain.OrderCoupon;
import saleson.shop.order.OrderMapper;
import saleson.shop.order.OrderService;
import saleson.shop.order.addpayment.OrderAddPaymentMapper;
import saleson.shop.order.addpayment.domain.OrderAddPayment;
import saleson.shop.order.claimapply.OrderClaimApplyMapper;
import saleson.shop.order.claimapply.domain.ClaimApply;
import saleson.shop.order.claimapply.domain.OrderCancelApply;
import saleson.shop.order.claimapply.domain.OrderReturnApply;
import saleson.shop.order.claimapply.support.ClaimException;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.domain.OrderPayment;
import saleson.shop.order.domain.OrderPgData;
import saleson.shop.order.domain.OrderShipping;
import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.order.domain.OrderSupporter;
import saleson.shop.order.giftitem.OrderGiftItemService;
import saleson.shop.order.givepoint.OrderGivePointMapper;
import saleson.shop.order.givepoint.OrderGivePointService;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.order.infra.OrderPaymentEncryptor;
import saleson.shop.order.infra.OrderRefundEncryptor;
import saleson.shop.order.infra.OrderRefundParamEncryptor;
import saleson.shop.order.infra.OrderReturnApplyEncryptor;
import saleson.shop.order.payment.OrderPaymentMapper;
import saleson.shop.order.refund.domain.OrderRefund;
import saleson.shop.order.refund.domain.OrderRefundDetail;
import saleson.shop.order.refund.support.OrderRefundParam;
import saleson.shop.order.shipping.OrderShippingMapper;
import saleson.shop.order.support.EditPayment;
import saleson.shop.order.support.OrderException;
import saleson.shop.order.support.OrderParam;
import saleson.shop.point.PointService;
import saleson.shop.remittance.RemittanceMapper;
import saleson.shop.shipmentreturn.ShipmentReturnMapper;
import saleson.shop.ums.UmsService;
import saleson.shop.user.LocgovService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserDetailEncryptor;

@Slf4j
@RequiredArgsConstructor
@Service("orderRefundService")
public class OrderRefundServiceImpl extends EgovAbstractServiceImpl implements OrderRefundService {

	public static final String ROLE_ISMS = "ROLE_ISMS";

	private final OrderRefundMapper orderRefundMapper;
	private final OrderClaimApplyMapper orderClaimApplyMapper;
	private final SellerMapper sellerMapper;
	private final OrderMapper orderMapper;
	private final OrderAddPaymentMapper orderAddPaymentMapper;
	private final OrderSupporter orderSupporter;
	private final OrderService orderService;
	private final ShipmentReturnMapper shipmentReturnMapper;
	private final OrderShippingMapper orderShippingMapper;
	private final RemittanceMapper remittanceMapper;
	private final CouponMapper couponMapper;
	private final PointService pointService;
    private final SequenceService sequenceService;
	private final OrderGiftItemService orderGiftItemService;
	private final UmsService umsService;
	private final UnifiedMessagingService unifiedMessagingService;
	private final ApplicationInfoService applicationInfoService;
	private final ConfigMapper configMapper;
	private final OrderRefundEncryptor orderRefundEncryptor;
	private final OrderPaymentEncryptor orderPaymentEncryptor;
	private final OrderRefundParamEncryptor orderRefundParamEncryptor;
	private final SellerService sellerService;
	private final OrderReturnApplyEncryptor orderReturnApplyEncryptor;
	private final Cryptor cryptor;
	private final DataMasking dataMasking;
	private final OrderPaymentMapper orderPaymentMapper;

	@Autowired
	private OrderGivePointService orderGivePointService;
	
	@Autowired
	private SmsIpsService smsIpsService;	
	
	private final UserService userService;	
	
	@Autowired
	private UserDetailEncryptor userDetailEncryptor;
	
	@Autowired
	private LocgovService locgovService;

	@Override
	public OrderRefund getOrderCancelRefundForUser(ClaimApply claimApply) {

		OrderRefund orderRefund = new OrderRefund(claimApply);

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(UserUtils.getUserId());
		orderParam.setOrderCode(claimApply.getOrderCode());
		orderParam.setOrderSequence(claimApply.getOrderSequence());

		if (claimApply.getOrder() == null) {
			Order order = orderService.getOrderByParam(orderParam);
			if (order == null) {
				throw new OrderException();
			}

			claimApply.setOrder(order);
		}

		List<OrderShipping> orderShippings = orderSupporter.getReShippingAmountForUser(claimApply);

		List<OrderCancelApply> cancelApplyList = claimApply.getOrderCancelApplys();
		List<Long> sellerIds = new ArrayList<>();

		int totalItemReturnAmount = 0;
		if (cancelApplyList != null) {
			for(OrderCancelApply orderCancelApply : cancelApplyList) {
				totalItemReturnAmount += orderCancelApply.getClaimApplyAmount();
				appendSellerIds(sellerIds, orderCancelApply.getOrderItem().getSellerId());
			}
		}

		if (sellerIds.isEmpty()) {
			throw new OrderException();
		}

		List<OrderAddPayment> orderAddPayments = new ArrayList<>();
		int totalAddShippingAmount = 0;

		if (orderShippings != null) {
			for(OrderShipping orderShipping : orderShippings) {

				if (orderShipping.getAddPayAmount() != 0) {

					OrderAddPayment orderAddPayment = new OrderAddPayment(orderShipping);
					orderAddPayment.setAddPaymentType(orderShipping.getAddPaymentType());
					orderAddPayment.setAmount(orderShipping.getAddPayAmount());
					orderAddPayment.setRefundCode(claimApply.getRefundCode());
					orderAddPayment.setSalesDate(DateUtils.getToday());
					orderAddPayment.setSubject("주문 취소로 인한 배송비 변경");
					orderAddPayment.setIssueCode("CANCEL-ADD-SHIPPING-" + orderShipping.getShippingSequence());
					orderAddPayments.add(orderAddPayment);

					// 배송비 추가?
					if ("1".equals(orderShipping.getAddPaymentType())) {
						totalAddShippingAmount -= orderShipping.getAddPayAmount();
					} else {
						totalAddShippingAmount += orderShipping.getAddPayAmount();
					}

				}
			}
		}

		if (orderAddPayments != null) {
			for(OrderAddPayment addPayment : orderAddPayments) {
				appendSellerIds(sellerIds, addPayment.getSellerId());
			}
		}

		List<OrderRefundDetail> details = new ArrayList<>();
		for(Long sellerId : sellerIds) {

			Seller seller = sellerMapper.getSellerById(sellerId);
			if (seller == null) {
				seller = new Seller();
				seller.setSellerId(sellerId);
				seller.setSellerName("업체정보 없음");
			} else {
				sellerService.decryptSellerData(seller);
			}

			details.add(new OrderRefundDetail(seller, cancelApplyList, null, orderAddPayments));
		}

		// 총 주문수량
		int orderQuantity = 0;
		for (OrderShippingInfo orderShippingInfo : claimApply.getOrder().getOrderShippingInfos()) {
			for (OrderItem orderItem : orderShippingInfo.getOrderItems()) {
				orderQuantity += orderItem.getOrderQuantity();
			}
		}

		orderRefund.setOrderPayments(getOrderPaymentListByParam(orderParam));

		orderRefund.setGroups(details);
		orderRefund.setTotalAddShippingAmount(totalAddShippingAmount);
		orderRefund.setTotalItemReturnAmount(totalItemReturnAmount);
		orderRefund.setTotalOrderQuantity(orderQuantity);

		decryptOrderRefundData(orderRefund);
		return orderRefund;
	}

	@Override
	public void appendSellerIds(List<Long> list, long id) {

		if (list.isEmpty()) {
			list.add(id);
		} else {
			for(Long sellerId : list) {
				if (sellerId == id) {
					return;
				}
			}

			list.add(id);
		}
	}

	@Override
	public OrderRefund getOrderRefundByCode(String refundCode) {

		OrderRefund orderRefund = orderRefundMapper.getOrderRefundByCode(refundCode);
		if (orderRefund == null) {
			throw new OrderException();
		}

		List<OrderAddPayment> addPaymentList = orderAddPaymentMapper.getOrderAddPaymentListByRefundCode(refundCode);
		List<OrderCancelApply> cancelApplyList = orderClaimApplyMapper.getOrderCancelApplyListByRefundCode(refundCode);
		List<OrderReturnApply> returnApplyList = orderClaimApplyMapper.getOrderReturnApplyListByRefundCode(refundCode);

		List<Long> sellerIds = new ArrayList<>();

		if (cancelApplyList != null) {
			for(OrderCancelApply orderCancelApply : cancelApplyList) {
				appendSellerIds(sellerIds, orderCancelApply.getOrderItem().getSellerId());
			}
			orderSupporter.setOrderItemOtherForCancelApply(cancelApplyList);
		}

		if (returnApplyList != null) {
			for(OrderReturnApply orderReturnApply : returnApplyList) {
				appendSellerIds(sellerIds, orderReturnApply.getShipmentReturnSellerId());
			}

			orderSupporter.setOrderItemOtherForReturnApply(returnApplyList);
		}

		if (addPaymentList != null) {
			for(OrderAddPayment addPayment : addPaymentList) {
				appendSellerIds(sellerIds, addPayment.getSellerId());
			}
		}

		if (sellerIds.isEmpty()) {
			throw new OrderException();
		}

		List<OrderRefundDetail> details = new ArrayList<>();
		for(Long sellerId : sellerIds) {

			Seller seller = sellerMapper.getSellerById(sellerId);
			if (seller == null) {
				seller = new Seller();
				seller.setSellerId(sellerId);
				seller.setSellerName("업체정보 없음");

			} else {
				sellerService.decryptSellerData(seller);
			}

			details.add(new OrderRefundDetail(seller, cancelApplyList, returnApplyList, addPaymentList));
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(orderRefund.getOrderCode());
		orderParam.setOrderSequence(orderRefund.getOrderSequence());

		Order returnInfo = orderMapper.getOrderReturnInfo(orderParam);
		if (ValidationUtils.isNull(returnInfo) == false) {
			orderRefund.setReturnBankName(returnInfo.getReturnBankName());
			orderRefund.setReturnBankInName(returnInfo.getReturnBankInName());
			orderRefund.setReturnVirtualNo(returnInfo.getReturnVirtualNo());
		}

		List<OrderPayment> payments = getOrderPaymentListByParam(orderParam);
		String escrowStatus = "N";

		for(OrderPayment payment : payments) {
			if ("bank".equals(payment.getApprovalType()) || "vbank".equals(payment.getApprovalType()))  {

				if ("1".equals(payment.getPaymentType())) {
					// 데이터 복호화
					decryptOrderRefundData(orderRefund);
				}

				payment.setReturnBankInName(orderRefund.getReturnBankInName());
				payment.setReturnBankVirtualNo(orderRefund.getReturnVirtualNo());

				OrderPgData pgData = payment.getOrderPgData();

				if ("vbank".equals(payment.getApprovalType())) {
					if ("inicis".equals(pgData.getPgServiceType())) {
						escrowStatus = orderMapper.getOrderItemByEscrow(payment.getOrderCode());
						payment.setEscrowStatus(escrowStatus);
					}
				}

				List<CodeInfo> list = ShopUtils.getBankListByKey(pgData.getPgServiceType());
				for(CodeInfo code : list) {
					if (code.getLabel().equals(orderRefund.getReturnBankName())) {
						payment.setReturnBankName(code.getKey().getId());

						break;
					}
				}

				payment.setReturnBankList(list);
			}
		}

		orderRefund.setOrderPayments(payments);
		orderRefund.setGroups(details);

		for (OrderPayment payment : payments) {
			if (!("bank".equals(payment.getApprovalType())
					|| "vbank".equals(payment.getApprovalType()))) {

				decryptOrderRefundData(orderRefund);
			}
		}

		return orderRefund;

	}

	@Override
	public List<OrderRefund> getOrderRefundListByParam(OrderRefundParam param) {

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			
			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				param.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				param.setShLocgovCode(locgovCode);
			}
		}
		param.encrypt(orderRefundParamEncryptor);
		int totalCount = orderRefundMapper.getOrderRefundCountByParam(param);

		//if (param.getItemsPerPage() == 10) {
		//	param.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		param.setPagination(pagination);
		param.setLanguage(CommonUtils.getLanguage());

		List<OrderRefund> list =  orderRefundMapper.getOrderRefundListByParam(param);

		if (list != null) {
			list.forEach(or -> decryptOrderRefundData(or));
		}

		param.decrypt(orderRefundParamEncryptor);

		return list;

	}

	@Override
	public String getActiveRefundCodeByParam(OrderRefundParam param) {

		String refundCode = orderRefundMapper.getActiveRefundCodeByParam(param);
		if (ObjectUtils.isEmpty(refundCode)) {
			refundCode = getNewRefundCodeByParam(param);
		}


		return refundCode;
	}

	@Override
	public String getNewRefundCodeByParam(OrderRefundParam param) {

        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setOrderCode(param.getOrderCode());
        orderRefund.setOrderSequence(param.getOrderSequence());

        if (UserUtils.isManagerLogin()) {
            orderRefund.setRequestManagerUserName(UserUtils.getManagerName());
        }

//        orderRefund.setReturnBankName(param.getBankName());
//        orderRefund.setReturnBankInName(param.getBankInName());
//        orderRefund.setReturnVirtualNo(param.getVirtualNo());

        orderRefund.setRefundStatusCode("1");
        if ("REFUND_FINISH".equals(param.getConditionType())) {
            orderRefund.setRefundStatusCode("2");
        }

        String newRefundCode = "RE-" + sequenceService.getLong("OP_ORDER_REFUND_CODE");

        orderRefund.setRefundCode(newRefundCode);

		// 데이터 암호화
		orderRefund.encrypt(orderRefundEncryptor);
        orderRefundMapper.insertOrderRefund(orderRefund);
        return orderRefund.getRefundCode();

	}

	@Override
//	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public void orderRefundProcess(OrderRefund orderRefund, EditPayment editPayment) {
		int totalReturnAmount = orderRefund.getTotalReturnAmount();
		int totalEditAmount = 0;
		String refundReason = " ";
		AtomicInteger returnPayAmount = new AtomicInteger(0);
		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(editPayment.getOrderCode());
		orderParam.setOrderSequence(editPayment.getOrderSequence());

        // 2019.04.25 sje 환불신청 취소할 경우를 대비해서 배송비 정보 백업
        if (orderShippingMapper.updatePreviousShipping(orderParam) == 0) {
            throw new OrderException();
        }

		List<Integer> returnCoupons = new ArrayList<>();
		HashMap<String, Integer> stockMap = new HashMap<>();
		HashMap<String, Integer> stockSetMap = new HashMap<>();
		OrderParam orderSetParam = new OrderParam();

		// 환불 상품은 정산 완료 여부를 파악해서 마이너스 정산을 만들어야 한다.
		// 정산은 '구매완료' 이후 데이터만 취급하므로 op_order_return_apply 테이블 사용안함.
		List<OrderReturnApply> returnApplyList = orderClaimApplyMapper.getOrderReturnApplyListByRefundCode(orderRefund.getRefundCode());

		if (returnApplyList != null && returnApplyList.size() > 0) {
			List<OrderGivePoint> refundRequestPointList = new ArrayList<>();
			
			for(OrderReturnApply returnApply : returnApplyList) {
				totalEditAmount += returnApply.getOrderItem().getSaleAmount();
			}

			log.debug(">>>>>>>>>>>> returnPayAmount=[{}]", totalEditAmount);
			if (totalReturnAmount != totalEditAmount) {
				throw new OrderException("환불 금액을 확인하세요.");
			}

//			int notFrequencyPointAmount = 0;
//			int frequencyPointAmount = 0;

			for(OrderReturnApply returnApply : returnApplyList) {
				/*
                if ("기타".equals(returnApply.getReturnReasonText())) {
                    refundReason = returnApply.getReturnReasonDetail();
                } else {
                    refundReason = returnApply.getReturnReasonText();
                }
				 */
				// 반품건 확인 해야됨. - kdj
                if ("1".equals(returnApply.getReturnReason())) {
                	refundReason = returnApply.getReturnReasonDetail();
                } else {
                	refundReason = returnApply.getReturnReasonText();
                }

				OrderItem orderItem = returnApply.getOrderItem();


				//정산 관련 주석

				if (orderItem.getRemittanceId() > 0) {
//					orderItem.setRemittanceExpectedDate(remittanceMapper.getRemittanceDateBySellerId(orderItem.getSellerId()));
//					orderItem.setRemittanceStatusCode("2");
//
//					if (remittanceMapper.updateItemRemittanceInfo(orderItem) == 0) {
						throw new OrderException("정산처리된 답례품은 환불이 불가능합니다.");
//					}
//				} else {
//					orderMapper.updateReturnPointFlag(orderItem);
				}


				//orderMapper.updateReturnPointFlag(orderItem);

				// begin
				List<OrderPayment> payments = getOrderPaymentListByParam(orderParam);
				if (payments == null) {
					throw new PageNotFoundException();
				}

//				int refundPoint = orderItem.getSaleAmount();
//				returnPayAmount.addAndGet(refundPoint);
//				OrderGivePoint orderGivePointParam = new OrderGivePoint();
//				orderGivePointParam.setUserId(returnApply.getUserId());
//				orderGivePointParam.setCntrLocgovCode(returnApply.getOrderItem().getLocgovCode());
//				orderGivePointParam.setOrderCode(returnApply.getOrderCode());
//
//				processPayback(orderGivePointParam, payments.get(0), refundPoint);
				// end


				// 복원할 쿠폰 List 생성 및 재고 복원해야 하는 목록 만들기
				returnCoupons = makeReturnCouponsAndStockRestorationMap(orderItem, stockMap);

				// 세트상품 정보 set
				orderSetParam.setOrderCode(orderItem.getOrderCode());
				orderSetParam.setOrderSequence(orderItem.getOrderSequence());
				orderSetParam.setSetItemSequence(orderItem.getItemSequence());

				List<OrderItem> itemSets = orderMapper.getOrderItemSetList(orderSetParam);

				// 세트상품 재고 복원 목록

				if (itemSets != null && itemSets.size() > 0) {
					for (OrderItem orderItemSet : itemSets) {
						orderService.makeStockRestorationMap(stockSetMap, orderItemSet, orderItem.getQuantity() * orderItemSet.getQuantity());
					}
				}
				
				
				// 환불로직 필요한 데이터 생성
				String orderItemLocgovCode = returnApply.getOrderItem().getLocgovCode();
				long refundPoint = returnApply.getClaimApplyQuantity() * returnApply.getOrderItem().getSalePrice();
				OrderGivePoint refundRequestPoint = new OrderGivePoint();
				boolean exists = false;
				check : for (OrderGivePoint orderGivePoint : refundRequestPointList) {
					if (orderGivePoint.getCntrLocgovCode().equals(orderItemLocgovCode)) {
						refundRequestPoint = orderGivePoint;
						exists = true;
						break check;
					}
				}
				refundRequestPoint.setCntrBlcePoint(refundRequestPoint.getCntrBlcePoint() + refundPoint);
				if (!exists) {
					refundRequestPoint.setCntrLocgovCode(orderItemLocgovCode);
					refundRequestPointList.add(refundRequestPoint);
				}
			}
			
			
			OrderGivePoint orderGivePoint = new OrderGivePoint();
			orderGivePoint.setOrderCode(editPayment.getOrderCode());
			List<OrderGivePoint> giveUsePoint = orderGivePointService.getGiveUsePoint(orderGivePoint);
			List<OrderGivePoint> givePointList = orderGivePointService.getCntrPointList(giveUsePoint);
			
			orderService.refundGiveGoodsPoint2(refundRequestPointList, giveUsePoint, givePointList);
			// CJH 2016.11.12 환불시 포인트 회수 처리
		}

		//int returnPayAmount = 0;
		// 취소 상품은 재고량을 복원한다.
		List<OrderCancelApply> cancelApplyList = orderClaimApplyMapper.getOrderCancelApplyListByRefundCode(orderRefund.getRefundCode());
		if (cancelApplyList != null && cancelApplyList.size() > 0) {
			for(OrderCancelApply cancelApply : cancelApplyList) {
				totalEditAmount += cancelApply.getOrderItem().getSaleAmount();
			}

			log.debug(">>>>>>>>>>>> returnPayAmount=[{}]", totalEditAmount);
			if (totalReturnAmount != totalEditAmount) {
				throw new OrderException("환불 금액을 확인하세요.");
			}

			for(OrderCancelApply cancelApply : cancelApplyList) {
				//kdj 변경 reason 1일때 CancelReasonDetail, 2일때 CancelReasonText
				/*if ("기타".equals(cancelApply.getCancelReasonText())) {
				    refundReason = cancelApply.getCancelReasonDetail();
				} else {
				    refundReason = cancelApply.getCancelReasonText();
				}*/
                if ("1".equals(cancelApply.getCancelReason())) {
                	refundReason = cancelApply.getCancelReasonDetail();
                } else {
                	refundReason = cancelApply.getCancelReasonText();
                }

				OrderItem orderItem = cancelApply.getOrderItem();

				// begin
				List<OrderPayment> payments = getOrderPaymentListByParam(orderParam);
				if (payments == null) {
					throw new PageNotFoundException();
				}

				int refundPoint = orderItem.getSaleAmount();
				returnPayAmount.addAndGet(refundPoint);
				OrderGivePoint orderGivePointParam = new OrderGivePoint();
				orderGivePointParam.setUserId(cancelApply.getUserId());
				orderGivePointParam.setCntrLocgovCode(cancelApply.getOrderItem().getLocgovCode());
				orderGivePointParam.setOrderCode(cancelApply.getOrderCode());
				processPayback(orderGivePointParam, payments.get(0), refundPoint);
				// end

				// 복원할 쿠폰 List 생성 및 재고 복원해야 하는 목록 만들기
				returnCoupons = makeReturnCouponsAndStockRestorationMap(orderItem, stockMap);

				// 세트상품 정보 set
				orderSetParam.setOrderCode(orderItem.getOrderCode());
				orderSetParam.setOrderSequence(orderItem.getOrderSequence());
				orderSetParam.setSetItemSequence(orderItem.getItemSequence());

				List<OrderItem> itemSets = orderMapper.getOrderItemSetList(orderSetParam);

				// 세트상품 재고 복원 목록
				if (itemSets != null && !itemSets.isEmpty()) {
					for (OrderItem orderItemSet : itemSets) {
						orderService.makeStockRestorationMap(stockSetMap, orderItemSet, orderItem.getQuantity() * orderItemSet.getQuantity());
					}
				}

			}

		}

		// 사용자 쿠폰 반환
		// returnUserCoupons(returnCoupons, orderRefund.getUserId(), orderRefund.getOrderCode());

		// 실제 환불될 금액 (포인트 제외) - 메시지 발송용

/*
		if (editPayment.getChangePayments() != null) {
			for(ChangePayment changePayment : editPayment.getChangePayments()) {

				int paymentSequence = changePayment.getPaymentSequence();
				int cancelAmount = changePayment.getCancelAmount();
				if (changePayment.getCancelAmount() > 0) {

					OrderPayment eqPayment = null;
					for(OrderPayment p : payments) {
						if (p.getPaymentSequence() == paymentSequence) {
							eqPayment = p;
							break;
						}
					}

					if (eqPayment == null) {
						throw new OrderException();
					}

					if (eqPayment.getRemainingAmount() < cancelAmount) {
						throw new OrderException("결제잔액을 확인하세요.");
					}

					String approvalType = eqPayment.getApprovalType();
					if (PointUtils.isPointType(approvalType)) {
						if (orderRefund.getUserId() == 0) {
							throw new OrderException("비회원 " + MessageUtils.getMessage("M00246") + " 사용 에러.");
						}
					} else {
						returnPayAmount += cancelAmount;
					}

					totalEditAmount += cancelAmount;
				}
			}
		}
*/
		// [SKC] 결제 수단과 상관 없이 은행입금으로 환불 처리하는 경우
		/*
		if (editPayment.getRefundAmount() != null && editPayment.getRefundAmount() > 0) {
			totalEditAmount += editPayment.getRefundAmount();

			returnPayAmount += editPayment.getRefundAmount();
		}
*/


/*
		try {
            editPayment.setRefundReason(refundReason);
			orderService.changePayment(editPayment);

		} catch(OrderException e) {
			log.error("ERROR: {}", e.getErrorMessage(), e);
			throw new OrderException(e.getErrorMessage());
		}
*/

		//if (UserUtils.isManagerLogin()) {
//			orderRefund.setProcessManagerUserName(UserUtils.getManagerName());
			orderRefund.setProcessManagerUserName(UserUtils.getUser().getUserName());
		//}

		// 데이터 암호화
		orderRefund.encrypt(orderRefundEncryptor);

		// 환불 마스터 수정
		orderRefundMapper.updateRefundStatus(orderRefund);

		// 주문 로그 데이터를 위해 임시 조회
		List<OrderItem> logOrderItems
				= orderService.getOrderItemListForOrderLog(orderRefund.getOrderCode(), orderRefund.getOrderSequence());

		// 상품 데이터 수정
		orderRefundMapper.updateRefundFinishedForItem(orderRefund);

		// 주문 로그
		try {

			List<Integer> logItemSequences= new ArrayList<>();

			for (OrderCancelApply cancelApply : cancelApplyList) {
				logItemSequences.add(cancelApply.getItemSequence());
			}

			for (OrderReturnApply returnApply : returnApplyList) {
				logItemSequences.add(returnApply.getItemSequence());
				
				// 국민비서 알림 전송 (답례품 환불 완료)
				try {
					OrderParam orderReturnParam = new OrderParam();
					orderReturnParam.setOrderCode(returnApply.getOrderCode()); 
					orderReturnParam.setOrderSequence(returnApply.getOrderSequence());
					orderReturnParam.setItemSequence(returnApply.getItemSequence());
					List<Order> orderList = orderService.getOrderListByParam(orderReturnParam);
					
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
						sb.append(returnApply.getOrderCode());	// 주문번호
						sb.append("|");
						sb.append(StringEscapeUtils.unescapeHtml(orderList.get(0).getItemName()).replaceAll("\\|", "-"));		// 답례품명
						if (StringUtils.hasLength(orderList.get(0).getOptions()) && !orderList.get(0).getOptions().contains("|")) {
							sb.append("(");
							sb.append(StringEscapeUtils.unescapeHtml(orderList.get(0).getOptions()));	// 옵션명
							sb.append(")");
						}						
						sb.append("|");
						sb.append(ObjectUtils.isEmpty(returnApply.getReturnReasonDetail()) ? returnApply.getReturnReasonText() : returnApply.getReturnReasonDetail());		// 사유
						sb.append("|");
						sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));						
						receiverInfo.setSndngCntnts(sb.toString());
						receiverInfos.add(receiverInfo);
						smsIpsService.insertTifIpsSndngM(receiverInfos);
	    			}
				} catch (NullPointerException | ClassCastException e) {
					log.error(getClass().getName() +  " :: orderRefundProcess send sms error", e);
				}
				
				// 국민비서 알림 전송 (답례품 환불 완료) - 답례품 제공자
				try {
//					OrderParam orderReturnParam = new OrderParam();
//					orderReturnParam.setOrderCode(returnApply.getOrderCode()); 
//					orderReturnParam.setOrderSequence(returnApply.getOrderSequence());
//					orderReturnParam.setItemSequence(returnApply.getItemSequence());
//					List<Order> orderList = orderService.getOrderListByParam(orderReturnParam);
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
//						sb.append(returnApply.getOrderCode());	// 주문번호
//						sb.append("|");
//						sb.append(StringEscapeUtils.unescapeHtml(orderList.get(0).getItemName()));		// 답례품명
//						if (StringUtils.hasLength(orderList.get(0).getOptions()) && !orderList.get(0).getOptions().contains("|")) {
//							sb.append("(");
//							sb.append(StringEscapeUtils.unescapeHtml(orderList.get(0).getOptions()));	// 옵션명
//							sb.append(")");
//						}						
//						sb.append("|");
//						sb.append(ObjectUtils.isEmpty(returnApply.getReturnReasonDetail()) ? returnApply.getReturnReasonText() : returnApply.getReturnReasonDetail());		// 사유
//						sb.append("|");
//						sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));						
//						receiverInfo.setSndngCntnts(sb.toString());
//						receiverInfos.add(receiverInfo);
//						smsIpsService.insertTifIpsSndngM(receiverInfos);
//	    			}
				} catch (NullPointerException | ClassCastException e) {
					log.error(getClass().getName() +  " :: orderRefundProcess send sms error", e);
				}
			}

			for (OrderItem orderItem : logOrderItems) {
				for (int itemSequence : logItemSequences) {
					if (itemSequence == orderItem.getItemSequence()) {
						orderService.insertOrderLog(
								OrderLogType.ORDER_REFUND,
								orderItem.getOrderCode(),
								orderItem.getOrderSequence(),
								orderItem.getItemSequence(),
								orderItem.getOrderStatus()
						);
					}
				}
			}

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", getClass().getName() + " :: orderRefundProcess RuntimeException2 ========", e);
		}

		// 클레임 신청정보 수정
		orderClaimApplyMapper.updateCancelStatusForRefund(orderRefund.getRefundCode());
		orderClaimApplyMapper.updateReturnStatusForRefund(orderRefund.getRefundCode());

		// 재고량 복원
		orderService.stockRestoration(stockMap);

		// 세트상품 재고량 복원
		orderService.stockRestoration(stockSetMap);

		// 잔여 배송비 정산일자 셋팅 (oracle과 mysql로직이 다름) [2017-07-12]minae.yun
		/*
		if ("oracle".equals(SalesonProperty.getConfigDatabaseVendor())) {
			List<OrderItem> refundItemList = remittanceMapper.getOrderShippingForRemittance(orderRefund.getRefundCode());

			for (int i=0; i<refundItemList.size(); i++) {
				remittanceMapper.updateResidualShippingRemittanceByRefundCodeForOracle(refundItemList.get(i));
			}

		} else {
			remittanceMapper.updateResidualShippingRemittanceByRefundCode(orderRefund.getRefundCode());
		}
		 */
		/*
		if (returnPayAmount > 0) {
			orderRefund.setReturnPayAmount(returnPayAmount);

			String templateCode = "order_refund";
			Ums ums = umsService.getUms(templateCode);

			ApplicationInfo applicationInfo = applicationInfoService.getApplicationInfo(orderRefund.getUserId());
			Config config = configMapper.getShopConfig(Config.SHOP_CONFIG_ID);
			Buyer buyer = orderService.getBuyerByOrderCode(orderParam.getOrderCode());

			unifiedMessagingService.sendMessage(new OrderRefundApproval(ums, orderRefund, orderRefund.getUserId(), config, buyer.getMobile(), applicationInfo, cryptor, dataMasking));
		}
		*/
	}

	@Override
	public List<OrderPayment> getOrderPaymentListByParam(OrderParam orderParam) {
		List<OrderPayment> orderPayments = orderMapper.getOrderPaymentListByParam(orderParam);

		orderPayments.forEach(op -> {
			op.decrypt(orderPaymentEncryptor, ShopUtils.needMasking());
		});

		return orderPayments;
	}

	@Override
	public void cancelRefund(String refundCode) {

		List<OrderCancelApply> cancelApplyList = orderClaimApplyMapper.getOrderRefundCancel(refundCode);

		if (!cancelApplyList.isEmpty()) {
			for(OrderCancelApply orderCancelApply : cancelApplyList) {

				// 로그 추가로 인해 임시 조회
				OrderItem logOrderItem
						= orderService.getOrderItemForOrderLog(orderCancelApply.getOrderCode(), orderCancelApply.getOrderSequence(), orderCancelApply.getItemSequence());

                // 2019.02.25 Son Jun-Eu 환불 취소 신청 시 OrderAddPayment row삭제
                orderAddPaymentMapper.deleteOrderAddPaymentByCancel(orderCancelApply);

                orderCancelApply.setCancelRefusalReasonText("환불 신청 취소");
                orderCancelApply.setRefundCode(null);
                orderCancelApply.setClaimStatus("01");
                orderCancelApply.setRefundCancelFlag("Y");

				orderClaimApplyMapper.updateOrderCancelApply(orderCancelApply);
				orderClaimApplyMapper.updateClaimQuantityForCancel(orderCancelApply);

				orderCancelApply.setRefundCode(refundCode);
				orderRefundMapper.deleteOrderRefundInfo(orderCancelApply);

                OrderShipping orderShipping = new OrderShipping();
                orderShipping.setOrderCode(orderCancelApply.getOrderCode());
                orderShipping.setOrderSequence(orderCancelApply.getOrderSequence());
                orderShipping.setShippingSequence(orderCancelApply.getShippingSequence());

				// 2019.04.25 sje 환불 신청 취소 시 배송비 정보를 재계산 이전으로 되돌림
                orderShippingMapper.updateShippingForCancelRefund(orderShipping);

				// 로그 추가
				try {
					orderService.insertOrderLog(
							OrderLogType.ORDER_REFUND,
							orderCancelApply.getOrderCode(),
							orderCancelApply.getOrderSequence(),
							orderCancelApply.getItemSequence(),
							logOrderItem.getOrderStatus()
					);
				} catch (RuntimeException e) {
//					log.error("ERROR: {}", e.getMessage(), e);
					log.error("ERROR: {}", getClass().getName() + " :: cancelRefund RuntimeException1 ========", e);
				}
			}
		} else {

			List<OrderReturnApply> returnApplyList = orderClaimApplyMapper.getOrderRefundReturn(refundCode);

			if (!returnApplyList.isEmpty()) {
				for (OrderReturnApply orderReturnApply : returnApplyList) {

					// 로그 추가로 인해 임시 조회
					OrderItem logOrderItem
							= orderService.getOrderItemForOrderLog(orderReturnApply.getOrderCode(), orderReturnApply.getOrderSequence(), orderReturnApply.getItemSequence());

                    // 2019.02.25 Son Jun-Eu 환불 취소 신청 시 OrderAddPayment row삭제
                    orderAddPaymentMapper.deleteOrderAddPaymentByReturn(orderReturnApply);

                    orderReturnApply.setReturnRefusalReasonText("환불 신청 취소");
                    orderReturnApply.setRefundCode(null);
                    orderReturnApply.setReturnShippingNumber(null);
                    orderReturnApply.setReturnShippingCompanyName(null);
                    orderReturnApply.setClaimStatus("01");
                    orderReturnApply.setRefundCancelFlag("Y");

					orderClaimApplyMapper.updateOrderReturnApply(orderReturnApply);
					orderClaimApplyMapper.updateClaimQuantityForReturn(orderReturnApply);

					orderReturnApply.setRefundCode(refundCode);
					orderRefundMapper.deleteOrderRefundInfoByReturn(orderReturnApply);

					// 로그 추가
					try {
						orderService.insertOrderLog(
								OrderLogType.ORDER_REFUND,
								orderReturnApply.getOrderCode(),
								orderReturnApply.getOrderSequence(),
								orderReturnApply.getItemSequence(),
								logOrderItem.getOrderStatus()
						);
					} catch (RuntimeException e) {
//						log.error("ERROR: {}", e.getMessage(), e);
						log.error("ERROR: {}", getClass().getName() + " :: cancelRefund RuntimeException2 ========", e);
					}
				}
			}
		}
	}

	/**
	 * 사용자 반환 예정 쿠폰 및 재고 복원해야하는 목록 생성
	 * @param orderItem
	 * @param stockMap
	 * @return
	 */
	private List<Integer> makeReturnCouponsAndStockRestorationMap(OrderItem orderItem, HashMap<String, Integer> stockMap) {

		List<Integer> returnCoupons = new ArrayList<>();

		if (orderItem != null) {

			if (orderItem.getCouponUserId() > 0) {
				returnCoupons.add(orderItem.getCouponUserId());
			}

			if (orderItem.getAddCouponUserId() > 0) {
				returnCoupons.add(orderItem.getAddCouponUserId());
			}

			// 재고 복원해야하는 목록 만들기
			orderService.makeStockRestorationMap(stockMap, orderItem, orderItem.getQuantity());
		}


		return returnCoupons;
	}

	/**
	 * 사용자 쿠폰 반환
	 * @param returnCoupons
	 */
	private void returnUserCoupons(List<Integer> returnCoupons, Long userId, String orderCode) {
		// 쿠폰 반환
		if (!returnCoupons.isEmpty()) {

			OrderParam orderParam = new OrderParam();
			orderParam.setOrderCode(orderCode);
			orderParam.setOrderSequence(0);
			orderParam.setConditionType("OPMANAGER");

			// 주문상품 목록 조회
			List<OrderItem> orderItemList = orderMapper.getOrderItemListByParam(orderParam);

			for(Integer couponUserId : returnCoupons) {
				List<String> orderStatusCodes = Arrays.asList("60", "70", "65", "75"); // 제외 대상 주문 상태 코드
				boolean refundAll =  (int)orderItemList.stream()
						.filter(i -> couponUserId == i.getCouponUserId() && !orderStatusCodes.contains(i.getOrderStatus()))
						.count() > 0 ? false : true;

				if (userId > 0 && refundAll) {
					OrderCoupon orderCoupon = new OrderCoupon();
					orderCoupon.setUserId(userId);
					orderCoupon.setCouponUserId(couponUserId);
					couponMapper.updateCouponUserReturnsByOrderCouponUser(orderCoupon);
				}

			}
		}
	}

	private void decryptOrderRefundData(OrderRefund orderRefund) {
		// 복호화
		if (orderRefund != null) {
			orderRefund.decrypt(orderRefundEncryptor, ShopUtils.needMasking());
		}
	}

	@Override
	public OrderRefund getGiveGoodsOrderCancelRefundForUser(ClaimApply claimApply) throws OrderException {
		OrderRefund orderRefund = new OrderRefund(claimApply);

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(UserUtils.getUserId());
		orderParam.setOrderCode(claimApply.getOrderCode());
		orderParam.setOrderSequence(claimApply.getOrderSequence());

		if (claimApply.getOrder() == null) {
			Order order = orderService.getOrderByParam(orderParam);
			if (order == null) {
				throw new OrderException();
			}

			claimApply.setOrder(order);
		}

		/* List<OrderShipping> orderShippings = */orderSupporter.getReShippingAmountForUser(claimApply);

		List<OrderCancelApply> cancelApplyList = claimApply.getOrderCancelApplys();
		List<Long> sellerIds = new ArrayList<>();

		int totalItemReturnAmount = 0;
		if (cancelApplyList != null) {
			for(OrderCancelApply orderCancelApply : cancelApplyList) {
				totalItemReturnAmount += orderCancelApply.getClaimApplyAmount();
				appendSellerIds(sellerIds, orderCancelApply.getOrderItem().getSellerId());
			}
		}

		if (sellerIds.isEmpty()) {
			throw new OrderException();
		}

		List<OrderAddPayment> orderAddPayments = new ArrayList<>();
		int totalAddShippingAmount = 0;

//		if (orderShippings != null) {
//			for(OrderShipping orderShipping : orderShippings) {
//
//				if (orderShipping.getAddPayAmount() != 0) {
//
//					OrderAddPayment orderAddPayment = new OrderAddPayment(orderShipping);
//					orderAddPayment.setAddPaymentType(orderShipping.getAddPaymentType());
//					orderAddPayment.setAmount(orderShipping.getAddPayAmount());
//					orderAddPayment.setRefundCode(claimApply.getRefundCode());
//					orderAddPayment.setSalesDate(DateUtils.getToday());
//					orderAddPayment.setSubject("주문 취소로 인한 배송비 변경");
//					orderAddPayment.setIssueCode("CANCEL-ADD-SHIPPING-" + orderShipping.getShippingSequence());
//					orderAddPayments.add(orderAddPayment);
//
//					// 배송비 추가?
//					if ("1".equals(orderShipping.getAddPaymentType())) {
//						totalAddShippingAmount -= orderShipping.getAddPayAmount();
//					} else {
//						totalAddShippingAmount += orderShipping.getAddPayAmount();
//					}
//
//				}
//			}
//		}

		if (orderAddPayments != null) {
			for(OrderAddPayment addPayment : orderAddPayments) {
				appendSellerIds(sellerIds, addPayment.getSellerId());
			}
		}

		List<OrderRefundDetail> details = new ArrayList<>();
		for(Long sellerId : sellerIds) {

			Seller seller = sellerMapper.getSellerById(sellerId);
			if (seller == null) {
				seller = new Seller();
				seller.setSellerId(sellerId);
				seller.setSellerName("업체정보 없음");
			} else {
				sellerService.decryptSellerData(seller);
			}

			details.add(new OrderRefundDetail(seller, cancelApplyList, null, orderAddPayments));
		}

		// 총 주문수량
		int orderQuantity = 0;
		for (OrderShippingInfo orderShippingInfo : claimApply.getOrder().getOrderShippingInfos()) {
			for (OrderItem orderItem : orderShippingInfo.getOrderItems()) {
				orderQuantity += orderItem.getOrderQuantity();
			}
		}

		orderRefund.setOrderPayments(getOrderPaymentListByParam(orderParam));

		orderRefund.setGroups(details);
		orderRefund.setTotalAddShippingAmount(totalAddShippingAmount);
		orderRefund.setTotalItemReturnAmount(totalItemReturnAmount);
		orderRefund.setTotalOrderQuantity(orderQuantity);

		decryptOrderRefundData(orderRefund);
		return orderRefund;
	}

	@Override
//	@Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
	public void processPayback(OrderGivePoint orderGivePointParam, OrderPayment orderPayment, int refundPoint) throws ClaimException{
		int paymentCancelAmount = orderPayment.getCancelAmount();
		int paymentRemainingAmount = orderPayment.getRemainingAmount();

		List<OrderGivePoint> useGivePoints = orderGivePointService.getGiveUsePoint(orderGivePointParam);
		for (OrderGivePoint useGivePoint : useGivePoints) {
			long usePoint = useGivePoint.getCntrUsePoint();
			useGivePoint.setOrderCode(orderGivePointParam.getOrderCode());
			useGivePoint.setAddZero(true);
			OrderGivePoint remainGivePoint = orderGivePointService.getGiveBlcePointListByCntrSn(useGivePoint);
			long cntrBlcePoint = remainGivePoint.getCntrBlcePoint();
			if (refundPoint < usePoint) {		// 환불해야하는 포인트가 사용포인트보다 작을 경우
				usePoint -= refundPoint;
				useGivePoint.setCntrUsePoint(usePoint);
				cntrBlcePoint += refundPoint;
				remainGivePoint.setCntrBlcePoint(cntrBlcePoint);
				paymentCancelAmount += refundPoint;
				paymentRemainingAmount -= refundPoint;

				useGivePoint.setLastUpdtPnttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
				useGivePoint.setLastUpdusrId(UserUtils.getUser().getUserId());
				
				orderGivePointService.updateGiveUsePoint(useGivePoint);		// 사용내역 수정
				// TODO :: 환불 로그 추가
				refundPoint = 0;
			} else {
				refundPoint -= usePoint;
				cntrBlcePoint += usePoint;
				remainGivePoint.setCntrBlcePoint(cntrBlcePoint);
				paymentCancelAmount += usePoint;
				paymentRemainingAmount -= usePoint;
				useGivePoint.setOrderCode(orderGivePointParam.getOrderCode());
				orderGivePointService.deleteGiveUsePoint(useGivePoint);		// 사용내역 삭제
				// TODO :: 환불 로그 추가	
			}

			remainGivePoint.setLastUpdtPnttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
			remainGivePoint.setLastUpdusrId(UserUtils.getUser().getUserId());

			orderGivePointService.updateGiveBlcePoint(remainGivePoint);		// 잔여포인트 수정				// 잔여포인트 업데이트 로직 제외
			if (refundPoint == 0) {
				break;
			}
		}

		orderPayment.setCancelAmount(paymentCancelAmount);
		orderPayment.setRemainingAmount(paymentRemainingAmount);
		orderPayment.setRefundFlag("Y");
		
		orderPaymentMapper.updateOrderPaymentForCancel(orderPayment);		// 결제 데이터 수정
	}
}
