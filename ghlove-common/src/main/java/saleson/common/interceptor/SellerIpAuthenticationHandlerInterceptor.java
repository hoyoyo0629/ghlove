package saleson.common.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import saleson.shop.access.AccessService;
import saleson.shop.access.domain.Access;
import saleson.shop.access.support.AccessParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SellerIpAuthenticationHandlerInterceptor implements HandlerInterceptor {
protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccessService accessService;

	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();

		if(requestUri.indexOf("/seller") > -1) {

			AccessParam accessParam = new AccessParam();
			accessParam.setAccessType("2");
        	accessParam.setDisplayFlag("Y");
			List<Access> allowIps = accessService.getAllowIpList(accessParam);

			boolean isMatched = false;

			AntPathMatcher pathMatcher = new AntPathMatcher();

			for (Access access : allowIps) {
				String ip = saleson.common.utils.CommonUtils.getClientIp(request);
				if (pathMatcher.match(access.getRemoteAddr().trim(), saleson.common.utils.CommonUtils.getClientIp(request))) {
					isMatched = true;
					break;
				}

			}
			if (!isMatched) {
				log.warn("답례품제공자 페이지 접근 제한 : {}", saleson.common.utils.CommonUtils.getClientIp(request));
				response.sendRedirect("/error/404");
				return false;
			}
        }
        return true;
    }

}
