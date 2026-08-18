package saleson.common.security;

import com.onlinepowers.framework.common.LoginRequest;
import com.onlinepowers.framework.security.exception.LoginTypeNotMatchException;
import com.onlinepowers.framework.security.exception.TokenValidationException;
import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.util.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.log.LoginLogService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFailureHandlerService extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler{
	private static final Logger log = LoggerFactory.getLogger(AuthenticationFailureHandlerService.class);

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private SecurityService securityService;
	
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authentication)
			throws IOException, ServletException {

		log.error("ERROR: {}", authentication.getMessage(), authentication);

		String failureUrl = request.getParameter("failureUrl");
        String loginType = request.getParameter("op_login_type");
        String loginId = request.getParameter("op_username");

		securityService.updateLoginFailCount(loginType, loginId);

        if (LoginRequest.USER.equals(loginType)) {
			loginLogService.insertLoginLogByUser(request, false);

        } else if (LoginRequest.SELLER.equals(loginType)) {
        	loginLogService.insertLoginLogBySeller(request, false);

		} else if (LoginRequest.OPMANAGER.equals(loginType)) {
			loginLogService.insertLoginLogByManager(request, false);

		}

		failureUrl = UserUtils.resolveFailureUrl(failureUrl, loginType);


		// BadCredentialsException
		// LoginTypeNotMatchException
		// TokenValidationException
		String errorCode = "";
		if (authentication instanceof BadCredentialsException) {
			errorCode = "1";
		}

		if (authentication instanceof LoginTypeNotMatchException) {
			errorCode = "2";
		}

		if (authentication instanceof TokenValidationException) {
			errorCode = "3";
		}

		if (authentication instanceof LockedException) {
			errorCode = "4";
		}

		if (authentication instanceof CredentialsExpiredException) {
			errorCode = "5";
		}

		if (authentication instanceof AuthenticationServiceException) {
			errorCode = "6";
		}

		if (!errorCode.isEmpty()) {
			failureUrl = ShopUtils.appendUri(failureUrl, "error=" + errorCode);
		}

		log.debug("Redirecting to failure Url: {}", failureUrl);
		getRedirectStrategy().sendRedirect(request, response, failureUrl);
	}
}