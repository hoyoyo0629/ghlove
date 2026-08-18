package saleson.shop.order.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;
import saleson.shop.order.domain.OrderPayment;

@Slf4j
@RequiredArgsConstructor
@Service("orderPaymentService")
public class OrderPaymentServiceImpl extends EgovAbstractServiceImpl implements OrderPaymentService {

    private final OrderPaymentMapper orderPaymentMapper;

    @Override
    public OrderPayment getOrderPaymentByPgDataForKcpVacct(String order_no) {
        return orderPaymentMapper.getOrderPaymentByPgDataForKcpVacct(order_no);
    }
}
