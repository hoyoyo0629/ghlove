package saleson.shop.order.log;

import com.onlinepowers.framework.util.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.UserUtils;

import java.time.Instant;

@Service("orderLogService")
public class OrderLogServiceImpl extends EgovAbstractServiceImpl implements OrderLogService{

    private static final Logger log = LoggerFactory.getLogger(OrderLogServiceImpl.class);

    @Override
    public void put(String orderCode) {
        put(orderCode, false);
    }

    @Override
    public void put(String orderCode, boolean processPgFlag) {

        try {

            String txId = MDC.get("txId");

            if (ObjectUtils.isEmpty(txId)) {
                initialize(processPgFlag);
            }

            if (ObjectUtils.isEmpty(orderCode)) {
                orderCode = "-";
            }

            MDC.put("orderCode", orderCode);

        } catch (RuntimeException ignore) {
//            log.error("OrderLogService put error {}", ignore.getMessage(), ignore);
            log.error("OrderLogService put error {}", getClass().getName() + " :: put RuntimeException ==========", ignore);
        }



    }

    private void initialize(boolean processPgFlag) {

        try {

            Instant now = Instant.now();

            String userType = "";
            String loginId = "";

            if (processPgFlag) {
                userType = "PG";
                loginId = "pg";
            } else {
                userType = UserUtils.isUserLogin()? "USER" : "GUEST";
                loginId = UserUtils.isUserLogin() ? UserUtils.getLoginId() : "guest";
            }

            MDC.put("userId", String.valueOf(UserUtils.getUserId()));
            MDC.put("userType", userType);
            MDC.put("loginId", loginId);
            MDC.put("txId","TX-"+now.toEpochMilli());

        } catch (RuntimeException ignore) {
//            log.error("OrderLogService initialize error {}", ignore.getMessage(), ignore);
            log.error("OrderLogService initialize error {}", getClass().getName() + " :: initialize RuntimeException ==========", ignore);
        }
    }

}
