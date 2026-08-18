package saleson.shop.order.payment;

import saleson.shop.order.domain.OrderPayment;

public interface OrderPaymentService {

    /**
     * KCP용 가상계좌 결제 정보 조회
     * @param order_no
     * @return
     */
    OrderPayment getOrderPaymentByPgDataForKcpVacct(String order_no);
}
