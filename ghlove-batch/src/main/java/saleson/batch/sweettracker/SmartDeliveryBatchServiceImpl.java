package saleson.batch.sweettracker;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import saleson.common.enumeration.OrderLogType;
import saleson.common.sweettracker.SmartDeliveryService;
import saleson.common.sweettracker.dto.DeliveryRequest;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.shipping.OrderShippingMapper;
import saleson.shop.order.support.OrderParam;

@Service("SmartDeliveryBatchService")
@RequiredArgsConstructor
public class SmartDeliveryBatchServiceImpl implements SmartDeliveryBatchService {

	private Logger log = LoggerFactory.getLogger(SmartDeliveryBatchServiceImpl.class);

	@Autowired
	private OrderShippingMapper orderShippingMapper;

	@Autowired
	private SmartDeliveryService smartDeliveryService;

	@Autowired
	private OrderService orderService;


	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int smartDeliveryShippingCompleteBatch(OrderParam orderParam) {

		int successCount = 0;

		/*
			주문상태코드 = 1 (정상)
			환불유무 = N
			주문상태 (30:배송중 -> 35, 55:교환배송중 -> 58)
			배송시작일이 2일이상 된것
		*/
		List<OrderItem> list = orderShippingMapper.getBuyShippingCompleteListByParam(orderParam);

		if (list != null) {
			for (OrderItem orderItem : list) {

				/* STEP.1
				 * 스마트 택배용 코드 조회
				 * deliveryCompanyId
				 * */
				Map<String,Object> smartDeliveryCode = orderShippingMapper.getSmartDeliveryCode(orderItem.getDeliveryCompanyId());

				if(smartDeliveryCode != null && !smartDeliveryCode.isEmpty()){ //값이 존재할 경우만 사용.

					String carruerCode 	= smartDeliveryCode.get("CODE").toString();
					String invoice 		= orderItem.getDeliveryNumber().replaceAll("[^0-9]", "");

					if(StringUtils.isNotBlank(invoice) && !invoice.isEmpty()) { // 송장번호가 없을 경우는 동작하지 않음.

						/* STEP.2
						 * 스마트 택배 API 전송
						 * 택배사 코드 : carrierCode
						 * 송장 번호  : invoiceNo
						 * */
						DeliveryRequest deliveryRequest = new DeliveryRequest();
						deliveryRequest.setCarrierCode(carruerCode);
						deliveryRequest.setInvoiceNo(invoice);

						/* 스마트 택배 API 전송 */
						Map<String, Object> resultMap = smartDeliveryService.delivery(deliveryRequest);

						if(!resultMap.isEmpty() && !resultMap.containsKey("status")) { //status가 있을 경우는 보통 에러가 발생되는 경우.

							/* level 1:배송준비중, 2:집화완료, 3:배송중, 4:지점도착, 5:배송출발, 6:배송완료 */
							if(Integer.parseInt(resultMap.get("level").toString()) == 6) {

								orderParam.setOrderCode(orderItem.getOrderCode());
								orderParam.setOrderSequence(orderItem.getOrderSequence());
								orderParam.setItemSequence(orderItem.getItemSequence());
								/* 배송완료 코드 셋팅 (30:배송중 -> 35:배송완료, 55:교환배송중 -> 58:교환배송완료) */
								orderParam.setChangeOrderStatus("30".equals(orderParam.getOrderStatus()) ? "35" : "58");

								/* STEP.3
								 * 배송완료 처리
								 * */
								if (orderShippingMapper.updateShppingComplete(orderParam) > 0) {

									// 교환배송완료 시 교환요청내역 클레임상태 04(완료) 로 업데이트
									if ("55".equals(orderParam.getOrderStatus())) {
										orderShippingMapper.updateOpOrderExchangeApplyClaimStatus(orderParam);
									}

									// 주문 로그
									orderService.insertOrderLog(
											OrderLogType.ORDER_BATCH,
											orderItem.getOrderCode(),
											orderItem.getOrderSequence(),
											orderItem.getItemSequence(),
											orderItem.getOrderStatus()
									);
								}
								successCount++;
							};
						}else {
							log.error("[스마트택배 ERROR] :invoice = {}, code = {}, msg = {}", invoice, resultMap.get("code"), resultMap.get("msg"));
						}
					}
				}
			}
		}
		return successCount;
	}
}
