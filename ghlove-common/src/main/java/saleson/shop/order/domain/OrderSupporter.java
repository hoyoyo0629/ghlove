package saleson.shop.order.domain;

import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.model.OrderGiftItem;
import saleson.shop.deliverycompany.DeliveryCompanyMapper;
import saleson.shop.deliverycompany.domain.DeliveryCompany;
import saleson.shop.order.OrderMapper;
import saleson.shop.order.claimapply.OrderClaimApplyMapper;
import saleson.shop.order.claimapply.domain.ClaimApply;
import saleson.shop.order.claimapply.domain.ClaimApplyItem;
import saleson.shop.order.claimapply.domain.OrderCancelApply;
import saleson.shop.order.claimapply.domain.OrderCancelShipping;
import saleson.shop.order.claimapply.domain.OrderReturnApply;
import saleson.shop.order.giftitem.OrderGiftItemService;
import saleson.shop.order.infra.OrderEncryptor;
import saleson.shop.order.infra.OrderPaymentEncryptor;
import saleson.shop.order.infra.OrderShippingInfoEncryptor;
import saleson.shop.order.support.OrderException;
import saleson.shop.order.support.OrderParam;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderSupporter {

    private final OrderMapper orderMapper;
    private final OrderEncryptor orderEncryptor;
    private final OrderPaymentEncryptor orderPaymentEncryptor;
    private final OrderShippingInfoEncryptor orderShippingInfoEncryptor;
    private final OrderGiftItemService orderGiftItemService;
    private final OrderClaimApplyMapper orderClaimApplyMapper;
    private final DeliveryCompanyMapper deliveryCompanyMapper;

    public List<OrderShipping> getReShippingAmountForUser(ClaimApply claimApply) {

        if (claimApply.getId() == null) {
            return null;
        }


        List<OrderShipping> orderShippings = getShippingsForUser(claimApply.getOrder(), claimApply);
        resetPayShippingAmount(orderShippings);
        return orderShippings;
    }

    public List<OrderShipping> getReShippingAmountForManager(ClaimApply claimApply) {

        if (claimApply.getCancelIds() == null) {
            return null;
        }

        if (claimApply.getCancelShippingMap() == null) {
            return null;
        }

        boolean isRePayShipping = false;
        List<OrderShipping> orderShippings = null;
        for(OrderCancelShipping orderCancelShipping : claimApply.getCancelShippingMap().values()) {

            // 배송비 재계산??
            if ("Y".equals(orderCancelShipping.getRePayShipping())) {
                isRePayShipping = true;
                break;

            }

        }

        if (isRePayShipping == true) {

            OrderParam orderParam = new OrderParam(claimApply);
            orderParam.setConditionType("OPMANAGER");

            if (ShopUtils.isSellerPage()) {
                orderParam.setConditionType("SELLER");
                orderParam.setSellerId(SellerUtils.getSellerId());
            }

            Order order = this.getOrderByParam(orderParam);
            if (order == null) {
                throw new OrderException();
            }

            orderShippings = getShippingsForManager(order, claimApply);
            resetPayShippingAmount(orderShippings);
        }

        return orderShippings;
    }



    /**
     * 사용자용 배송 정책 리스트 만들기
     * @param order
     * @param claimApply
     * @return
     */
    private List<OrderShipping> getShippingsForUser(Order order, ClaimApply claimApply) {

        List<OrderShipping> orderShippings = new ArrayList<>();
        List<saleson.shop.order.claimapply.domain.OrderCancelApply> orderCancelApplys = new ArrayList<>();

        for(OrderShippingInfo info : order.getOrderShippingInfos()) {
            for(OrderItem orderItem : info.getOrderItems()) {

                // 이미 취소된 상품인 경우
                if ("Y".equals(orderItem.getCancelFlag())) {
                    continue;
                }

                // CJH 2017.01.03 환불 대기 상태 추가
                if ("1".equals(orderItem.getRefundStatus())) {
                    continue;
                }

                int quantity = 0;
                boolean isClaim = false;
                for(String key : claimApply.getId()) {

                    ClaimApplyItem claimApplyItem = (claimApply.getClaimApplyItemMap()).get(key);

                    if (claimApplyItem == null) {
                        continue;
                    }

                    if (claimApplyItem.getItemSequence() == orderItem.getItemSequence()) {
                        quantity = orderItem.getQuantity() - claimApplyItem.getApplyQuantity();

                        if ("1".equals(claimApply.getClaimType()) || "4".equals(claimApply.getClaimType())) {
                            saleson.shop.order.claimapply.domain.OrderCancelApply orderCancelApply = new saleson.shop.order.claimapply.domain.OrderCancelApply();
                            orderCancelApply.setOrderCode(claimApply.getOrderCode());
                            orderCancelApply.setOrderSequence(claimApply.getOrderSequence());
                            orderCancelApply.setItemSequence(claimApplyItem.getItemSequence());
                            orderCancelApply.setClaimApplyQuantity(claimApplyItem.getApplyQuantity());
                            orderCancelApply.setClaimApplyAmount(orderItem.getSalePrice() * claimApplyItem.getApplyQuantity());
                            orderCancelApply.setOrderItem(orderItem);
                            orderCancelApplys.add(orderCancelApply);
                        }

                        isClaim = true;

                        // 주문 총수량보다 기존 클래임 수량 + 신청 수량이 큰경우 에러
                        if (orderItem.getOrderQuantity() < claimApplyItem.getApplyQuantity() + orderItem.getClaimQuantity()) {
                            throw new OrderException();
                        }

                        break;
                    }

                }

                if (isClaim == true) {
                    if (quantity < 0) {
                        throw new OrderException();
                    }
                } else {
                    quantity = orderItem.getQuantity();
                }

                orderItem.setQuantity(quantity);
                OrderShipping orderShipping = orderItem.getOrderShipping();

                boolean inArray = false;

                for(OrderShipping shipping : orderShippings) {
                    if (shipping.getShippingSequence() == orderShipping.getShippingSequence()) {
                        inArray = true;

                        if (quantity > 0) {
                            List<OrderItem> list = shipping.getOrderItems();
                            if (list == null) {
                                list = new ArrayList<>();
                            }

                            list.add(orderItem);
                            shipping.setOrderItems(list);
                        }

                        if (isClaim == true) {

                            if ("2".equals(claimApply.getClaimType()) && "1".equals(claimApply.getClaimReason())) {
                                // 반품이면서 판매자 사유이면 배송비 계산 안함
                            } else {
                                shipping.setRePayShipping("Y");
                            }

                        }

                        break;
                    }
                }

                if (inArray == false) {

                    // 도서산간 지역??
                    String islandType = "";
                    if (ObjectUtils.isEmpty(info.getReceiveZipcode()) == false) {
                        islandType = this.getIslandTypeByZipcode(info.getReceiveZipcode());
                    }

                    orderShipping.setIslandType(islandType);

                    if (quantity > 0) {
                        List<OrderItem> list = new ArrayList<>();
                        list.add(orderItem);
                        orderShipping.setOrderItems(list);
                    }

//                    if (isClaim == true) {
//
//                        if ("2".equals(claimApply.getClaimType()) && "1".equals(claimApply.getClaimReason())) {
//                            // 반품이면서 판매자 사유이면 배송비 계산 안함
//                        } else {
//                            orderShipping.setRePayShipping("Y");
//                        }
//
//                    }

                    orderShippings.add(orderShipping);
                }
            }
        }

        for(OrderShipping orderShipping : orderShippings) {

            if (!"Y".equals(orderShipping.getRePayShipping())) {
                continue;
            }

            // 통합 물류 배송일때 같은 통합물류 정책 배송비를 재계산 한다
            if (ObjectUtils.isEmpty(orderShipping.getShipmentGroupCode()) == false) {

                if (ValidationUtils.isNull(orderShippings) == false) {
                    for(OrderShipping os : orderShippings) {

                        if (orderShipping.getShipmentGroupCode().equals(os.getShipmentGroupCode())) {
                            os.setRePayShipping("Y");
                        }

                    }
                }
            }
        }

        claimApply.setOrderCancelApplys(orderCancelApplys);
        return orderShippings;
    }

    /**
     * 관리자용 배송 정책 리스트 만들기
     * @param order
     * @return
     */
    private List<OrderShipping> getShippingsForManager(Order order, ClaimApply claimApply) {

        List<OrderShipping> orderShippings = new ArrayList<>();

        for(OrderShippingInfo info : order.getOrderShippingInfos()) {
            for(OrderItem orderItem : info.getOrderItems()) {

                if ("Y".equals(orderItem.getCancelFlag())) {
                    continue;
                }

                // CJH 2017.01.03 환불 대기 상태 추가
                if ("1".equals(orderItem.getRefundStatus())) {
                    continue;
                }

                boolean isClaim = false;
                int quantity = 0;
                for(String key : claimApply.getCancelIds()) {

                    saleson.shop.order.claimapply.domain.OrderCancelApply orderCancelApply = claimApply.getCancelApplyMap().get(key);
                    if (orderCancelApply.getItemSequence() == orderItem.getItemSequence()) {
                        isClaim = true;

                        quantity = orderItem.getQuantity() - orderCancelApply.getClaimApplyQuantity();
                    }
                }

                OrderCancelApply cancelApply = orderItem.getCancelApply();
                if (cancelApply != null) {
                    if ("03".equals(cancelApply.getClaimStatus()) || "04".equals(cancelApply.getClaimStatus())) {
                        isClaim = true;
                    }
                }

                if (isClaim == true) {
                    if (quantity < 0) {
                        throw new OrderException();
                    }
                } else {
                    quantity = orderItem.getQuantity();
                }

                orderItem.setQuantity(quantity);
                OrderShipping orderShipping = orderItem.getOrderShipping();

                boolean inArray = false;
                for(OrderShipping shipping : orderShippings) {
                    if (shipping.getShippingSequence() == orderShipping.getShippingSequence()) {
                        inArray = true;

                        if (quantity > 0) {
                            List<OrderItem> list = shipping.getOrderItems();
                            if (list == null) {
                                list = new ArrayList<>();
                            }

                            list.add(orderItem);
                            shipping.setOrderItems(list);
                        }

                        if (isClaim == true) {

                            if ("2".equals(claimApply.getClaimType()) && "1".equals(claimApply.getClaimReason())) {
                                // 반품이면서 판매자 사유이면 배송비 계산 안함
                            } else {
                                shipping.setRePayShipping("Y");
                            }

                        }

                        break;
                    }
                }

                if (inArray == false) {

                    // 도서산간 지역??
                    String islandType = "";
                    if (ObjectUtils.isEmpty(info.getReceiveZipcode()) == false) {
                        islandType = this.getIslandTypeByZipcode(info.getReceiveZipcode());
                    }

                    orderShipping.setIslandType(islandType);

                    if (quantity > 0) {
                        List<OrderItem> list = new ArrayList<>();
                        list.add(orderItem);
                        orderShipping.setOrderItems(list);
                    }

                    if (isClaim == true) {

                        if ("2".equals(claimApply.getClaimType()) && "1".equals(claimApply.getClaimReason())) {
                            // 반품이면서 판매자 사유이면 배송비 계산 안함
                        } else {
                            orderShipping.setRePayShipping("Y");
                        }

                    }

                    orderShippings.add(orderShipping);
                }
            }
        }

        for(OrderShipping orderShipping : orderShippings) {

            if (!"Y".equals(orderShipping.getRePayShipping())) {
                continue;
            }

            // 통합 물류 배송일때 같은 통합물류 정책 배송비를 재계산 한다
            if (ObjectUtils.isEmpty(orderShipping.getShipmentGroupCode()) == false) {

                if (ValidationUtils.isNull(orderShippings) == false) {
                    for(OrderShipping os : orderShippings) {

                        if (orderShipping.getShipmentGroupCode().equals(os.getShipmentGroupCode())) {
                            os.setRePayShipping("Y");
                        }

                    }
                }
            }
        }

        return orderShippings;
    }


    /**
     * 배송비 재계산
     * @param shippings
     */
    public void resetPayShippingAmount(List<OrderShipping> shippings) {

        if (shippings == null) {
            return;
        }

        for(OrderShipping shipping : shippings) {

            if (shipping.getOrderItems() == null) {
                continue;
            }

            if (!"Y".equals(shipping.getRePayShipping())) {
                continue;
            }

            int addDeliveryCharge = 0;
            if ("JEJU".equals(shipping.getIslandType())) {
                addDeliveryCharge = shipping.getShippingExtraCharge1();
            } else if ("ISLAND".equals(shipping.getIslandType())) {
                addDeliveryCharge = shipping.getShippingExtraCharge2();
            }

            int realShipping = 0;
            if ("1".equals(shipping.getShippingType())) { // 무료 배송

                realShipping = addDeliveryCharge;


            } else if ("2".equals(shipping.getShippingType()) || "3".equals(shipping.getShippingType())) { // 2 : 판매자 조건부, 3 : 출고지 조건부,

                int totalItemAmount = 0;
                if ("3".equals(shipping.getShippingType())) {

                    if (ObjectUtils.isEmpty(shipping.getShipmentGroupCode())) {

                        for(OrderItem buyItem : shipping.getOrderItems()) {
                            totalItemAmount += buyItem.getSaleAmount();
                        }

                    } else {

                        List<OrderItem> list = new ArrayList<>();
                        for(OrderShipping tShipping : shippings) {
                            if (ValidationUtils.isEmpty(tShipping.getShipmentGroupCode()) == false) {
                                if (tShipping.getShipmentGroupCode().equals(shipping.getShipmentGroupCode()) && tShipping.getOrderItems() != null) {
                                    list.addAll(tShipping.getOrderItems());
                                }
                            }
                        }

                        // 통합 물류 배송으로 조건부 배송 조건 금액을 물류 배송 상품 전체로 한다.
                        for(OrderItem buyItem : list) {

                            if (ObjectUtils.isEmpty(buyItem.getShipmentGroupCode())) {
                                continue;
                            }

                            if (shipping.getShipmentGroupCode().equals(buyItem.getShipmentGroupCode())) {
                                totalItemAmount += buyItem.getSaleAmount();
                            }
                        }
                    }
                } else {
                    for(OrderItem buyItem : shipping.getOrderItems()) {
                        totalItemAmount += buyItem.getSaleAmount();
                    }
                }

                //System.out.println(totalItemAmount);

                if (shipping.getShippingFreeAmount() <= totalItemAmount) {
                    realShipping = addDeliveryCharge;
                } else {
                    realShipping = shipping.getShipping() + addDeliveryCharge;
                }

            } else if ("4".equals(shipping.getShippingType())) { // 상품 조건부

                int totalItemAmount = 0;
                for(OrderItem buyItem : shipping.getOrderItems()) {
                    totalItemAmount += buyItem.getSaleAmount();
                }

                if (shipping.getShippingFreeAmount() <= totalItemAmount) {
                    realShipping = addDeliveryCharge;
                } else {
                    realShipping = shipping.getShipping() + addDeliveryCharge;
                }

            } else if ("5".equals(shipping.getShippingType())) { // 개당배송비 - BOX 당 배송비

                int totalItemQuantity = 0;
                for(OrderItem buyItem : shipping.getOrderItems()) {
                    totalItemQuantity += buyItem.getQuantity();
                }

                if (shipping.getShippingItemCount() == 0) {
                    shipping.setShippingItemCount(1);
                }

                int boxCount = (int) Math.ceil((float) totalItemQuantity / shipping.getShippingItemCount());
                realShipping = (shipping.getShipping() + addDeliveryCharge) * boxCount;

            } else { // 고정 배송비
                realShipping = shipping.getShipping() + addDeliveryCharge;
            }

            shipping.setRealShipping(realShipping);
            shipping.setRePayShippingAmount(realShipping);

            // 착불인경우 사용자 배송비 금액을 0으로 잡는다
            if ("2".equals(shipping.getShippingPaymentType())) {
                shipping.setRePayShippingAmount(0);
            }
        }

        for(OrderShipping orderShipping : shippings) {

            if (!"Y".equals(orderShipping.getRePayShipping())) {
                continue;
            }

            int addPayAmount = 0;
            String addPaymentType = "";

            /**
             * 배송정책에 상품이 안담겨있으면 배송비를 환불해줘야함..
             */
            if (orderShipping.getOrderItems() == null) {
                if (orderShipping.getPayShipping() == 0) {
                    continue;
                }

                addPayAmount = orderShipping.getPayShipping();
                addPaymentType = "2";

                orderShipping.setRePayShippingAmount(0);
            } else {

                if (orderShipping.getRePayShippingAmount() == orderShipping.getPayShipping()) {
                    continue;
                }

                addPayAmount = orderShipping.getRePayShippingAmount() - orderShipping.getPayShipping();

                // 배송비 추가 : 1, 배송비 환불 : 2
                addPaymentType = orderShipping.getRePayShippingAmount() > orderShipping.getPayShipping() ? "1" : "2";

                if (addPayAmount < 0) {
                    addPayAmount = -addPayAmount;
                }
            }

            orderShipping.setAddPayAmount(addPayAmount);
            orderShipping.setAddPaymentType(addPaymentType);
        }
    }


    public String getIslandTypeByZipcode(String zipcode) {

        if (ObjectUtils.isEmpty(zipcode)) {
            return "";
        }

        return orderMapper.getIslandTypeByZipcode(zipcode);
    }


    public Order getOrderByParam(OrderParam orderParam) {

        Order order = getByParam(orderParam);

        if (order == null) {
            return null;
        }

        /* 교환, 반품 거절 사유 상품별로 불러오기 */
        for (OrderShippingInfo shippingInfos : order.getOrderShippingInfos()) {
            for (OrderItem orderItem : shippingInfos.getOrderItems()) {
                if("59".equals(orderItem.getOrderStatus())) { //교환 거절
                    orderItem.setClaimRefusalReasonText(orderClaimApplyMapper.getClaimRefusalReasonText(orderItem));
                } else if ("69".equals(orderItem.getOrderStatus())) {
                    orderItem.setClaimRefusalReasonText(orderClaimApplyMapper.getClaimRefusalReasonText(orderItem));
                }
                
                if (orderItem.getDeliveryCompanyId() > 0 && StringUtils.isEmpty(orderItem.getDeliveryCompanyUrl())) {
                	DeliveryCompany deliveryCompany = deliveryCompanyMapper.getDeliveryCompanyById(orderItem.getDeliveryCompanyId());
                	orderItem.setDeliveryCompanyUrl(deliveryCompany.getDeliveryCompanyUrl());
                }
            }
        }

        bindOrder(order, orderParam);
        setOrderItemOther(order);

        return order;

    }


    /**
     * 주문정보 세팅
     * @param order
     * @param orderParam
     */
    public void bindOrder(Order order, OrderParam orderParam) {

        // 주문 결제정보 조회
        order.setOrderPayments(getOrderPaymentListByParam(orderParam));

		/*
		ClaimApplyParam claimApplyParam = new ClaimApplyParam();
		claimApplyParam.setOrderCode(order.getOrderCode());
		claimApplyParam.setOrderSequence(order.getOrderSequence());

		if (ShopUtils.isOpmanagerPage()) {
			claimApplyParam.setConditionType("OPMANAGER");
		} else if (ShopUtils.isSellerPage()) {
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
			claimApplyParam.setConditionType("SELLER");
		}

		// 주문취소 데이터
		List<OrderCancelApply> cancelApplyList = orderClaimApplyMapper.getOrderCancelApplyListByParam(claimApplyParam);
		if (cancelApplyList != null) {

			List<OrderCancelShipping> cancelShippingGroups = new ArrayList<>();

			/**
			 * CJH 2016.08.20
			 * 취소는 신청, 보류, 승인대기 상태의 데이터는 1Row의 데이터만 존재해야 한다.
			 * 나머지는 히스토리 만들다 보니 이렇게 되었다... 엉망이 되어가고있다..
			 *
			for(OrderCancelApply cancelApply : cancelApplyList) {
				for(OrderShippingInfo info : order.getOrderShippingInfos()) {
					for(OrderItem orderItem : info.getOrderItems()) {
						if (cancelApply.getItemSequence() == orderItem.getItemSequence()) {

							// 상품 환불금액
							cancelApply.setClaimApplyAmount(orderItem.getSalePrice() * cancelApply.getClaimApplyQuantity());

							// 상태가 거절이 아니면 클레임 수량을 반영
							if (!"99".equals(cancelApply.getClaimStatus())) {
								orderItem.setClaimApplyQuantity(orderItem.getClaimApplyQuantity() + cancelApply.getClaimApplyQuantity());
							}

							if (("01".equals(cancelApply.getClaimStatus()) || "02".equals(cancelApply.getClaimStatus())) || "03".equals(cancelApply.getClaimStatus())) {

								orderItem.setCancelApply(cancelApply);
								if (("01".equals(cancelApply.getClaimStatus()) || "02".equals(cancelApply.getClaimStatus()))) {
									OrderShipping orderShipping = orderItem.getOrderShipping();
									OrderCancelShipping shippingGroup = null;
									for(OrderCancelShipping group : cancelShippingGroups) {
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
										cancelShippingGroups.add(shippingGroup);
									}
								}
							}
						}
					}
				}
			}

			order.setCancelShippingGroups(cancelShippingGroups);
		}
		*/

        // 반품신청 데이터
		/*
		List<OrderReturnApply> returnApplyList = orderClaimApplyMapper.getOrderReturnApplyListByParam(claimApplyParam);
		if (returnApplyList != null) {

			for(OrderReturnApply returnApply : returnApplyList) {

				for(OrderShippingInfo info : order.getOrderShippingInfos()) {
					for(OrderItem orderItem : info.getOrderItems()) {
						if (returnApply.getItemSequence() == orderItem.getItemSequence()) {

							// 상품 환불금액
							returnApply.setClaimApplyAmount(orderItem.getSalePrice() * returnApply.getClaimApplyQuantity());

							// 상태가 거절이 아니면 클레임 수량을 반영
							if (!"99".equals(returnApply.getClaimStatus())) {
								orderItem.setClaimApplyQuantity(orderItem.getClaimApplyQuantity() + returnApply.getClaimApplyQuantity());
							}

							if (("01".equals(returnApply.getClaimStatus()) || "02".equals(returnApply.getClaimStatus())) || "03".equals(returnApply.getClaimStatus())
									|| "10".equals(returnApply.getClaimStatus())) {

								orderItem.setReturnApply(returnApply);
							}
						}
					}
				}


				ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();
				shipmentReturnParam.setShipmentReturnId(returnApply.getShipmentReturnId());
				ShipmentReturn shipmentReturn = shipmentReturnMapper.getShipmentReturnByParam(shipmentReturnParam);
				if (shipmentReturn != null) {
					returnApply.setShipmentReturn(shipmentReturn);
				}

				Seller shipmentReturnSeller = sellerMapper.getSellerById(returnApply.getShipmentReturnSellerId());
				if (shipmentReturnSeller != null) {
					returnApply.setSeller(shipmentReturnSeller);
				}
			}

			order.setReturnApplys(returnApplyList);
		}
		*/
    }

    public void setOrderItemOther(List<OrderList> orderList) {
        if (orderList != null && !orderList.isEmpty()) {

            HashSet<String> orderCodeSet = new HashSet<>();

            orderList.stream().forEach(order -> orderCodeSet.add(order.getOrderCode()));

            String[] orderCodes = orderCodeSet.toArray(new String[orderCodeSet.size()]);

            List<OrderGiftItem> orderGiftItems = orderGiftItemService.getOrderGiftItemListByOrderCodes(orderCodes);

            for (OrderList order : orderList) {
                // 사은품
                order.setOrderGiftItemList(filterOrderGiftItems(orderGiftItems, order.getOrderCode(), order.getOrderSequence(), order.getItemSequence()));
            }
        }

    }

    public void setOrderItemOther(OrderItem orderItem) {
        if (orderItem != null) {
            // 사은품
            List<OrderGiftItem> orderGiftItems = orderGiftItemService.getOrderGiftItemListByOrderCode(orderItem.getOrderCode());
            orderItem.setOrderGiftItemList(filterOrderGiftItems(orderGiftItems, orderItem.getOrderCode(), orderItem.getOrderSequence(), orderItem.getItemSequence()));

            // 세트상품
            // OrderParam orderParam = new OrderParam();
            // orderParam.setOrderCode(orderItem.getOrderCode());
            // orderParam.setOrderSequence(orderItem.getOrderSequence());
            // orderParam.setSetItemSequence(orderItem.getItemSequence());
            // orderItem.setItemSets(orderMapper.getOrderItemSetList(orderParam));
        }
    }

    public void setOrderItemOther(Order order) {
        if (order != null) {
            List<OrderGiftItem> orderGiftItems = orderGiftItemService.getOrderGiftItemListByOrderCode(order.getOrderCode());

            for (OrderShippingInfo orderShippingInfo : order.getOrderShippingInfos()) {
                for (OrderItem orderItem: orderShippingInfo.getOrderItems()) {
                    // 사은품
                    orderItem.setOrderGiftItemList(filterOrderGiftItems(orderGiftItems, orderItem.getOrderCode(), orderItem.getOrderSequence(), orderItem.getItemSequence()));
                }
            }
        }
    }


    public Order getByParam(OrderParam param) {
        Order order = orderMapper.getOrderByParam(param);

        if (order != null) {
            decryptData(order);
        }
        
        if (order == null) {
//            throw new OrderException("주문내역이 존재하지 않습니다.", "/order/list");
        	return null;
        }        

        return order;
    }

    public List<OrderPayment> getOrderPaymentListByParam(OrderParam orderParam) {
    	if (!StringUtils.hasLength(orderParam.getOrderCode())) {
    		return new ArrayList<>();
    	}
        List<OrderPayment> orderPayments = orderMapper.getOrderPaymentListByParam(orderParam);

        orderPayments.forEach(op -> {
            op.decrypt(orderPaymentEncryptor, ShopUtils.needMasking());
        });

        return orderPayments;
    }

    public void decryptData(Order order) {
        if (order != null) {
            order.decrypt(orderEncryptor, ShopUtils.needMasking());

            if (order.getOrderPayments() != null && !order.getOrderPayments().isEmpty()) {
                order.getOrderPayments().forEach(op -> op.decrypt(orderPaymentEncryptor, ShopUtils.needMasking()));
            }

            if (order.getOrderShippingInfos() != null && !order.getOrderShippingInfos().isEmpty()) {
                order.getOrderShippingInfos().forEach(osi -> osi.decrypt(orderShippingInfoEncryptor, ShopUtils.needMasking()));
            }
        }
    }

    public List<OrderGiftItem> filterOrderGiftItems(List<OrderGiftItem> orderGiftItems, String orderCode, int orderSequence, int itemSequence) {
        List<OrderGiftItem> filters = orderGiftItems.stream().filter(
            giftItem -> orderCode.equals(giftItem.getOrderCode())
                && orderSequence == giftItem.getOrderSequence()
                && itemSequence == giftItem.getItemSequence()
        ).collect(Collectors.toList());

        return filters;
    }

    public List<OrderItem> filterOrderItemSets(List<OrderItem> orderItemSets, String orderCode, int orderSequence, int itemSequence) {
        List<OrderItem> filters = orderItemSets.stream().filter(
            itemSet -> orderCode.equals(itemSet.getOrderCode())
                && orderSequence == itemSet.getOrderSequence()
                && itemSequence == itemSet.getSetItemSequence()
        ).collect(Collectors.toList());

        return filters;
    }


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

    public void setOrderItemOtherForReturnApply(List<saleson.shop.order.claimapply.domain.OrderReturnApply> list) {

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


    /**
     * 클레임 공통 주문 상품 정보 세팅
     * @param orderItem
     * @param orderGiftItems
     * @param orderItemSets
     */
    public void setOrderItemOtherForClaimApply(OrderItem orderItem, List<OrderGiftItem> orderGiftItems, List<OrderItem> orderItemSets) {
        if (orderItem != null) {
            // 사은품
            orderItem.setOrderGiftItemList(
                orderGiftItems.stream().filter(
                    giftItem -> orderItem.getOrderCode().equals(giftItem.getOrderCode())
                        && orderItem.getOrderSequence() == giftItem.getOrderSequence()
                        && orderItem.getItemSequence() == giftItem.getItemSequence()
                ).collect(Collectors.toList())
            );

            // 세트상품
            orderItem.setItemSets(
                orderItemSets.stream().filter(
                    itemSet -> orderItem.getOrderCode().equals(itemSet.getOrderCode())
                        && orderItem.getOrderSequence() == itemSet.getOrderSequence()
                        && orderItem.getItemSequence() == itemSet.getSetItemSequence()
                ).collect(Collectors.toList())
            );
        }
    }
}
