package saleson.common.interceptor;

import com.onlinepowers.framework.util.ValidationUtils;
import com.privacy.pCrypto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import saleson.common.enumeration.PrivacyAccess;
import saleson.common.enumeration.PrivacyTask;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.domain.Seller;
import saleson.shop.log.ActionLogService;
import saleson.shop.log.PrivacyAccessLogRepository;
import saleson.shop.log.domain.PrivacyAccessLog;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.Buyer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ActionLogHandlerInterceptor implements HandlerInterceptor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String MANAGER_URI = "/opmanager/";
    private String SELLER_URI = "/seller/";

    @Autowired
    private ActionLogService actionLogService;

    @Autowired
    private PrivacyAccessLogRepository privacyAccessLogRepository;

    @Autowired
    private OrderService orderService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        PathMatcher antPathMatcher = new AntPathMatcher();
        String requestUri = request.getRequestURI();

        boolean isManager = UserUtils.isManagerLogin();
        Seller seller = SellerUtils.getSeller();

        if ((isManager && isManagePage(requestUri)) || (ValidationUtils.isNotNull(seller) && ShopUtils.isSellerPage())) {
            actionLogService.insertManagerActionLog(request);

            // 개인정보 관련(회원, 주문) URL 접근시 로그 저장
            PrivacyAccess privacyAccess = PrivacyAccess.findByUrl(antPathMatcher, requestUri);
            if (privacyAccess != null && privacyAccess.getTask() != PrivacyTask.EXCEL_DOWNLOAD) {
                PrivacyAccessLog privacyAccessLog = new PrivacyAccessLog(privacyAccess, request);

                try {
                    Map<String, String> pathVariable = antPathMatcher.extractUriTemplateVariables(privacyAccess.getPattern(), requestUri);
//                    if (pathVariable.get("userId") != null) {
//                        privacyAccessLog.setUserId(Long.parseLong(pathVariable.get("userId")));
//                    } else {
                    	privacyAccessLog.setUserId(UserUtils.getUser().getUserId());
//                    }
                    
                    if (pathVariable.get("orderCode") != null) {
                        Buyer buyer = orderService.getBuyerByOrderCode(pathVariable.get("orderCode"));
                        privacyAccessLog.setUserId(buyer.getUserId());
                    }
                } catch (NumberFormatException ignore) {
                	logger.error(getClass().getName() + " preHandle error", ignore);
                }
                
                try {
                	privacyAccessLog.setIp(pCrypto.Encrypt("normal", privacyAccessLog.getIp(), ""));
                } catch (UnsupportedEncodingException e) {
                	logger.error(getClass().getName() + " :: accessLog pCrypto Error ", e);
                }
                
                privacyAccessLogRepository.save(privacyAccessLog);
            }
        }

        return true;
    }

    /**
     * 관리자/판매자 페이지인가?
     * @param requestUri
     * @return
     */
    private boolean isManagePage(String requestUri) {
        String[] uriPatterns = new String[]{
                MANAGER_URI, SELLER_URI
        };

        for (String uri : uriPatterns) {
            if (requestUri.indexOf(uri + "/login") > -1 || requestUri.indexOf(uri + "/accessdenied") > -1) {
                return false;
            }

            if (requestUri.startsWith(uri)) {
                return true;
            }
        }

        return false;
    }
}
