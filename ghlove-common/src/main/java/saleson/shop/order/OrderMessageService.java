package saleson.shop.order;

import saleson.shop.config.domain.Config;
import saleson.shop.order.domain.Buy;
import saleson.shop.order.domain.Order;

public interface OrderMessageService {

    /**
     * 주문관련 메시지 발송
     * @param buy
     */
    void sendOrderMessageTx(Buy buy);

    /**
     * 주문관련 메시지 발송
     * @param order
     * @param templateId
     * @param config
     */
    void sendOrderMessageTx(Order order, String templateId, Config config);

}
