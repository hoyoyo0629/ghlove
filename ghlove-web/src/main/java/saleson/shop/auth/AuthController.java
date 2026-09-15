package saleson.shop.auth;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.BusinessException;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import saleson.common.utils.UserUtils;
import saleson.shop.user.domain.Customer;

@Controller
@RequestMapping("/auth")
@RequestProperty(layout="blank")
public class AuthController {
	private static Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	/**
	 * SMS 인증번호 요청
	 * @param phoneNumber
	 * @return
	 */
	@PostMapping("sms-request")
	public JsonView smsAuthNumber(RequestContext requestContext,
			@RequestParam(value="loginId", required=false, defaultValue="") String loginId,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam(value="loginType", required=false, defaultValue="") String loginType
			) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			String requestToken = "";

			if ("".equals(loginId)) {
				requestToken = authService.getSmsAuthNumber(phoneNumber);
			} else {
				if ("opmanager".equals(loginType)) {
					requestToken = authService.getSmsAuthNumber(loginId, phoneNumber, true);
				} else {
					requestToken = authService.getSmsAuthNumber(loginId, phoneNumber);
				}

			}

			return JsonViewUtils.success(requestToken);

		} catch (BusinessException e) {
			return JsonViewUtils.exception("인증번호 발송에 실패하였습니다.");

		} catch (Exception e) {
			log.error("API sms-request ERROR: 인증번호 발송에 실패하였습니다.");
			return JsonViewUtils.exception("인증번호 발송에 실패하였습니다.");

		}

	}


	/**
	 * 이메일 인증번호 요청
	 * @param phoneNumber
	 * @return
	 */
	@PostMapping("email-request")
	public JsonView emailAuthNumber(RequestContext requestContext,
			Customer customer) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			String requestToken = authService.getEmailAuthNumber(customer);
			return JsonViewUtils.success(requestToken);

		} catch (BusinessException e) {
			log.error("ERROR: 인증번호 발송에 실패하였습니다.");
			return JsonViewUtils.exception("인증번호 발송에 실패하였습니다.");
		} catch (Exception e) {
			log.error("ERROR: 인증번호 발송에 실패하였습니다.");
			return JsonViewUtils.exception("인증번호 발송에 실패하였습니다.");
		}

	}

	@PostMapping("manager-sms-request")
	public JsonView managerAuthNumber(RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			String requestToken = "";

			if (!UserUtils.isManagerLogin()) {
				return JsonViewUtils.failure("관리자용 입니다.");
			}

			User user = UserUtils.getUser();

			if (!StringUtils.hasText(user.getPhoneNumber())) {
				return JsonViewUtils.failure("핸드폰 번호가 없습니다.");
			}

			requestToken = authService.getSmsAuthNumber(user.getLoginId(), user.getPhoneNumber(), true);

			// LOCAL DEV ONLY: no SMS gateway locally, so surface the generated code to the screen
			// instead of a real text message. In a real environment this is always null.
			java.util.Map<String, Object> data = new java.util.HashMap<>();
			data.put("requestToken", requestToken);
			data.put("localDevCode", authService.getLocalDevAuthCode(requestToken));

			return JsonViewUtils.success(data);

		} catch (BusinessException e) {
			return JsonViewUtils.exception("인증번호 발송에 실패하였습니다.");

		} catch (Exception e) {
			log.error("API manager-sms-request ERROR: 인증번호 발송에 실패하였습니다.");
			return JsonViewUtils.exception("인증번호 발송에 실패하였습니다.");

		}
	}
}
