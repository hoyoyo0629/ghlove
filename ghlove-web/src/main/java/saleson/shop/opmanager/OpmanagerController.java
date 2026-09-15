/*
 * Copyright(c) 2009-2011 Onlinepowers Development Team
 * http://www.onlinepowers.com
 *
 * @author skc
 * @since 2011. 5. 5.
 */
package saleson.shop.opmanager;


import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.support.EarlyLoadingCodeInfoRepository;
import com.onlinepowers.framework.repository.support.EarlyLoadingMessageInfoRepository;
import com.onlinepowers.framework.repository.support.EarlyLoadingRepositoryEvent;
import com.onlinepowers.framework.security.authentication.filter.IdPasswordAuthenticationFilter;
import com.onlinepowers.framework.security.exception.handler.OpAccessDeniedHandlerImpl;
import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.security.token.TokenService;
import com.onlinepowers.framework.security.token.domain.Token;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.privacy.pCrypto;

import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.LocalDateUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.email.EmailService;
import saleson.shop.email.domain.Email;
import saleson.shop.email.domain.EmailSend;
import saleson.shop.email.ems.EmsMapper;
import saleson.shop.email.support.EmailDetailParam;
import saleson.shop.email.support.SendParam;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.item.ItemService;
import saleson.shop.log.ChangeLogService;
import saleson.shop.log.LoginLogMapper;
import saleson.shop.log.LoginLogService;
import saleson.shop.log.PasswordLogService;
import saleson.shop.log.support.ChangeTypeEnum;
import saleson.shop.log.support.LoginLogParam;
import saleson.shop.notice.NoticeService;
import saleson.shop.order.OrderService;
import saleson.shop.qna.QnaService;
import saleson.shop.user.LocgovService;
import saleson.shop.user.ManagerRequestService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.ManagerRequestResult;
import saleson.shop.user.domain.PkiManagerLogin;
import saleson.shop.user.domain.PkiManagerLoginResult;
import saleson.shop.user.domain.TempPasswordChange;
import saleson.shop.user.support.FinancInfo;
import saleson.shop.user.support.OpManagerParam;

@Controller
@RequestProperty(title="관리자페이지", layout="base")
public class OpmanagerController {
	private static final Logger log = LoggerFactory.getLogger(OpmanagerController.class);

	@Value("${saleson.url.shoppingmall}")
    private String serverDomain;


	@Autowired
	QnaService qnaService;

	@Autowired
	OrderService orderService;

	@Autowired
	ItemService itemService;

	@Autowired
	NoticeService noticeService;

	@Autowired
	UserService userService;

	@Autowired
	SecurityService securityService;

	@Autowired
	@Qualifier("smsTokenService")
	TokenService smsTokenService;

	@Autowired
	ManagerRequestService managerRequestService;

	/** 지자체관리 Service */
	@Autowired
	private LocgovService locgovService;

	@Autowired
	private EarlyLoadingCodeInfoRepository codeInfoRepository;

	@Autowired
	private EarlyLoadingMessageInfoRepository messageInfoRepository;


	@Autowired
	private ChangeLogService changeLogService;

	@Autowired
	private PasswordLogService passwordLogService;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
 	private EmailService emailService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private EmsMapper emsMapper;

	@Value("${magicline.conf-path}")
	String confPath;

	@Value("${fincert.manager-use}")
	private String managerUse;

	@GetMapping({"/opmanager", "/opmanager/", "/opmanager/index"})
	@RequestProperty(title="메인페이지", layout="default")
	public String index(RequestContext requestContext, Model model, @ModelAttribute("searchParam") GiveState searchParam	) {

		if (!SecurityUtils.hasRole("ROLE_OPMANAGER")) {
			return ViewUtils.redirect("/opmanager/login");
		}

		User user = UserUtils.getUser();

		model.addAttribute("main", "main");

		model.addAttribute("adminRole", user.getUserRoles().get(0).getAuthority());

		//2023-02-17 추가
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("searchParam",searchParam);
		model.addAttribute("serverIp", getServerIp());

		String loginNow = (String)requestContext.getSession().getAttribute("loginNow");			// 20260518 추가. 로그인 직후 메인화면 진입
		if(loginNow != null && loginNow.equals("Y")) {
			requestContext.getSession().removeAttribute("loginNow");
			String msgOFFManager = managerRequestService.getMsgLoginOFFManager();
			if(!msgOFFManager.isEmpty()) {
				return ViewUtils.getManagerView("/main/index", msgOFFManager);
			}
		}


		// 2026-06-05 인천행정체제 개편으로 인한 중구동구서구 관리자 메시지 표시
//		String todatetime = LocalDateUtils.localDateTimeToString(LocalDateTime.now(), Const.DATETIME_FORMAT);
//		if(null != todatetime && todatetime.compareTo("20260630235959") <= 0) {	// 26년 6월 30일 23시 59분 59초
//
//			if(SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6")) {
//
//				List<String> incheonPopupCode = new ArrayList<String>(List.of("28110", "28140", "28260"));
//
//				Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(UserUtils.getUser().getUserId());
//				if (locgovCodeDetails != null && !"".equals(locgovCodeDetails.getId())) {
//					if (incheonPopupCode.contains(locgovCodeDetails.getId())) {
//						// 로그인한 사용자가 인천 중구동구서구 관리자인 경우
//						model.addAttribute("showIncheonPopup", "Y");
//					}
//				}
//			}
//		}

		CodeParam codeParam = new CodeParam();
		codeParam.setId("period");
		codeParam.setDetail("9990"); // 설문조사
		Code popAdmin = codeService.getCodePopAdmin(codeParam);
		if (null != popAdmin && !"".equals(popAdmin.getCodeType())) {
			model.addAttribute("showPopup", "Y");
		}

		codeParam.setId("period2");
		codeParam.setDetail("9991"); // 엑셀암호화
		Code popAdmin2 = codeService.getCodePopAdmin(codeParam);
		if (null != popAdmin2 && !"".equals(popAdmin2.getCodeType())) {
			model.addAttribute("showPopup2", "Y");
		}


		return ViewUtils.getManagerView("/main/index");
	}

	@GetMapping({"/opmanager/dashboard"})
	@RequestProperty(title="메인페이지", layout="default")
	public String dashboard(RequestContext requestContext, Model model, @ModelAttribute("searchParam") GiveState searchParam	) {
		//model.addAttribute("main", "main");

		if (!SecurityUtils.hasRole("ROLE_OPMANAGER")) {
			return ViewUtils.redirect("/opmanager/login");
		}

		User user = UserUtils.getUser();

		model.addAttribute("main", "main");

		model.addAttribute("adminRole", user.getUserRoles().get(0).getAuthority());

		//2023-02-17 추가
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("searchParam",searchParam);
		model.addAttribute("serverIp", getServerIp());

		return ViewUtils.getManagerView("/main/dashboard");
	}

	@GetMapping("/opmanager/login")
	@RequestProperty(title="관리자페이지 > 로그인")
	public String login(HttpServletRequest request,
			@RequestParam(value="error", required=false) String error,
			HttpSession session,
			Model model) {

		String target = "";
		if (request.getParameter("target") != null) {
			target = request.getParameter("target");
		}

		if ("/opmanager".equals(target)) {
			return ViewUtils.redirect("/opmanager/login");
		}

		model.addAttribute("OP_LOGIN_LAST_USERNAME", session.getAttribute(IdPasswordAuthenticationFilter.OP_LOGIN_LAST_USERNAME_KEY));
		model.addAttribute("error", error);
		model.addAttribute("target", (target.equals("") || target.equals("/")) ? "/opmanager/" : target);

		model.addAttribute("frontendUrl", SalesonProperty.getSalesonUrlFrontend());
		model.addAttribute("conf-path", confPath);
		model.addAttribute("serverIp", getServerIp());

		return ViewUtils.getManagerView("/user/login");
	}

	@GetMapping("/opmanager/login-main")
	@RequestProperty(title="관리자페이지")
	public String loginMain(Model model) {
		model.addAttribute("target", "/opmanager/");
		return ViewUtils.getManagerView("/user/login_main");
	}

	@GetMapping("/opmanager/login-old")
	@RequestProperty(title="관리자페이지 > 로그인")
	public String loginOld(HttpServletRequest request,
			@RequestParam(value="error", required=false) String error,
			HttpSession session,
			Model model) {

		String target = "";
		if (request.getParameter("target") != null) {
			target = request.getParameter("target");
		}

		model.addAttribute("OP_LOGIN_LAST_USERNAME", session.getAttribute(IdPasswordAuthenticationFilter.OP_LOGIN_LAST_USERNAME_KEY));
		model.addAttribute("error", error);
		model.addAttribute("target", target.equals("") ? "/opmanager/" : target);
		return ViewUtils.getManagerView("/user/login_old");
	}

	@GetMapping("/opmanager/accessdenied")
	@RequestProperty(title="접속제한")
	public String accessDenied(HttpSession session, Model model) {


		AccessDeniedException exception = (AccessDeniedException) session.getAttribute(OpAccessDeniedHandlerImpl.SPRING_SECURITY_ACCESS_DENIED_EXCEPTION_KEY);
		session.removeAttribute(OpAccessDeniedHandlerImpl.SPRING_SECURITY_ACCESS_DENIED_EXCEPTION_KEY);

		if (exception != null) {
			model.addAttribute("exception", exception);
		}
		return "redirect:/opmanager/login?error=1";
	}

	@GetMapping("/opmanager/login-disconnect")
	@RequestProperty(title="세션종료")
	public String disconnectSession() {
		return ViewUtils.redirect("/opmanager/login?target=/opmanager&error=98");
	}

	@GetMapping("opmanager/login-change-password")
	@RequestProperty(title="비밀번호 변경", layout="base")
	public String changePassword(Model model,
								 @RequestParam(value = "lock", defaultValue = "") String lock) {

		model.addAttribute("lock", lock);

		return ViewUtils.getManagerView("/main/change-password");
	}

	@PostMapping("opmanager/login-change-password")
		public String changePasswordAction(HttpServletRequest request,
				@RequestParam(value = "lock") String lock,
				@RequestParam("loginId") String loginId,
				@RequestParam("password") String password,
				@RequestParam("changePassword") String changePassword) {

		String message = "";
		String redirectUrl = "";
		boolean isPopup = false;

		try {
			User user = securityService.getManagerByLoginId(loginId);

			if (ValidationUtils.isNull(user)) {
				throw new UserException("사용자가 존재하지 않습니다.");
			}

			try {
				if(!ValidationUtils.isNull(user)) {
					userService.updatePasswordForManager(user.getUserId(), password, changePassword);
				}
			} catch (UserException e) {
//				log.error("ERROR: {}", e.getMessage(), e);
//				message = e.getMessage();
				log.error("ERROR: {}", getClass().getName() + " :: changePasswordAction UserException1 =========");
				message = "실패했습니다.";
				//message = MessageUtils.getMessage("실패했습니다.");
				redirectUrl = "/opmanager/login-change-password";
				return ViewUtils.redirect(redirectUrl, message);
			}

			securityService.updateClearLoginFailCountForManager(loginId);

			changeLogService.insertManagerChangeLog(request, user, ChangeTypeEnum.UPDATE);
			passwordLogService.insertManagerPasswordLog(user, isPopup);

			message = "비밀번호가 변경되었습니다.";
			redirectUrl = "/op_security_logout?target=/opmanager";

		} catch (UserException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
//			message = e.getMessage();
			log.error("ERROR: {}", getClass().getName() + " :: changePasswordAction UserException2 =========");
			message = "실패했습니다.";
			//message = MessageUtils.getMessage("실패했습니다.");
			redirectUrl = "opmanager/login-change-password";
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", getClass().getName() + " :: changePasswordAction RuntimeException =========");
			message = "비밀번호 변경에 실패했습니다.";
			redirectUrl = "opmanager/login-change-password";
		}

		return ViewUtils.redirect(redirectUrl,message);
	}

	@GetMapping(value = "/opmanager/login-lock")
	@RequestProperty(title="접속제한")
	public String loginLock (HttpSession session) {
		return ViewUtils.getManagerView("/main/login-lock");
	}

	@GetMapping(value = "/opmanager/login-lock-old")
	@RequestProperty(title="접속제한")
	public String loginLockOld (HttpSession session) {
		return ViewUtils.getManagerView("/main/login-lock-old");
	}

	@PostMapping("/opmanager/login-lock")
	public String loginLockAction (@RequestParam("requestToken") String requestToken,
								   @RequestParam("loginId") String loginId,
								   @RequestParam("phoneNumber") String phoneNumber,
								   @RequestParam("smsAuth") String smsAuth) {

		Token token = new Token();

		token.setRequestToken(requestToken);
		token.setAccessToken(smsAuth);
		token.setRequestType("SMS");

		if(!smsTokenService.isValidToken(token)) {
			return ViewUtils.redirect("/opmanager/login-lock","SMS 인증에 실패 했습니다.");
		}

		User user = securityService.getManagerByLoginId(loginId);

		if (ValidationUtils.isNull(user)) {
			return ViewUtils.redirect("/opmanager/login-lock","계정이 존재하지 않습니다.");
		}

		if (!phoneNumber.equals(user.getPhoneNumber())) {
			return ViewUtils.redirect("/opmanager/login-lock","계정이 존재하지 않습니다.");
		}

		userService.updateTempPasswordForManager(user.getUserId());
		securityService.updateClearLoginFailCountForManager(loginId);

		return ViewUtils.redirect("/opmanager/login");
	}




	@GetMapping("/opmanager/auth")
	@RequestProperty(title="본인인증", layout="base")
	public String auth(Model model) {
		return ViewUtils.getManagerView("/user/auth");
	}

	@PostMapping("/opmanager/auth")
	@RequestProperty(title="본인인증", layout="base")
	public String authAction(@RequestParam(name="target", defaultValue = "") String target,
							 @RequestParam(name="requestToken", defaultValue = "") String requestToken,
							 @RequestParam(name="smsAuth", defaultValue = "") String smsAuth) {

		if (UserUtils.isManagerLogin()) {

			Token token = new Token();

			token.setRequestToken(requestToken);
			token.setAccessToken(smsAuth);
			token.setRequestType("SMS");

			if(smsTokenService.isValidToken(token)) {
				userService.updateAuthExpiredDateById(UserUtils.getManagerId());
				String redirect = StringUtils.hasText(target) ? target : "/opmanager";
				return ViewUtils.redirect(redirect);
			}
		}

		return ViewUtils.redirect("/opmanager/auth", "인증에 실패 했습니다.");
	}

	@ResponseBody
	@GetMapping("/opmanager/reload-cache")
	public String reloadCache() {
		// reload
		// DB 메시지 reload
		EarlyLoadingRepositoryEvent messageReloadEvent = new EarlyLoadingRepositoryEvent("messageInfoRepository",EarlyLoadingRepositoryEvent.Action.ReloadAll);
		messageInfoRepository.onApplicationEvent(messageReloadEvent);

		// Code reload
		EarlyLoadingRepositoryEvent codeReloadEvent = new EarlyLoadingRepositoryEvent("codeInfoRepository",EarlyLoadingRepositoryEvent.Action.ReloadAll);
		codeInfoRepository.onApplicationEvent(codeReloadEvent);

		return "Success!";
	}

	/**
	 * 관리자 비밀번호 수정 (팝업)
	 * @return
	 */
	@GetMapping("/opmanager/user/manager/change-password")
	@RequestProperty(template = "opmanager", layout = "base")
	public String passwordChangePopup(Model model, @RequestParam("userId") String userId) {

		model.addAttribute("manager", userService.getManagerByUserId(Long.parseLong(userId)));
		return "view:/user/passwordPopup";
	}

	/**
	 * 관리자 비밀번호 수정처리 (팝업)
	 * @return
	 */
	@PostMapping("/opmanager/user/manager/change-password")
	public String passwordChangeAction(User user,
									   HttpServletRequest request,
									   @RequestParam("loginId") String loginId,
									   @RequestParam("password") String password,
									   @RequestParam("changePassword") String changePassword) {

		boolean isPopup = true;

		userService.updatePasswordForManager(user.getUserId(), password, changePassword);

		changeLogService.insertManagerChangeLog(request, user, ChangeTypeEnum.UPDATE);
		passwordLogService.insertManagerPasswordLog(user, isPopup);

		String message = "비밀번호가 변경되었습니다.";

		return ViewUtils.redirect("/opmanager", message, "self.close(); opener.location.reload();");
	}






	@ResponseBody
	@GetMapping("/opmanager/healthcheck")
	public String healthcheck() {
		return "Server is alive.";
	}

	/**
	 * 로그인 > 로그인 사용자 관리자 확인 (비로그인)
	 * @param model
	 * @param loginId
	 * @param password
	 * @return
	 */
	@PostMapping("/opmanager/user/login-user-valid")
	public JsonView loginUserValid(HttpServletRequest request, Model model
			, @RequestParam("loginId") String loginId
			, @RequestParam("password") String password, HttpSession session) {

		//개인정보이력을 위한 세션아이디 등록
		session.setAttribute("OP_LAST_USERNAME", loginId);

		// 로그인 사용자 확인
		// 테스트를 위한 임시 Referer 확인
		ManagerRequestResult result = null;
		String requestUri = request.getHeader("Referer");
		if (requestUri.indexOf("/opmanager/loginFincertTest") > -1) {
			result = managerRequestService.getLoginUserValid(request, loginId, password, managerUse);
		} else {
			result = managerRequestService.getLoginUserValid(request, loginId, password);
		}

		//result = managerRequestService.getLoginUserValid(loginId, password, managerUse);

		try {
			String code = CommonUtils.dataNvl(result.getCode());
			if(!"".equals(code) && ("ERR_MNG_PASS".equals(code) || code.indexOf("PASS_CHANGE_") > -1)) {
				loginLogService.insertLoginLogByManager(request, loginId, false);
			}

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", getClass().getName() + " :: loginUserValid RuntimeException ===========");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 로그인 > 로그인 사용자 관리자 확인 (비로그인)
	 * @param model
	 * @param loginId
	 * @param password
	 * @return
	 */
	@PostMapping("/opmanager/user/login-user-email")
	public JsonView loginUserEmail(HttpServletRequest request, Model model
			, @RequestParam("loginId") String loginId
			, @RequestParam("password") String password, HttpSession session) {

		//개인정보이력을 위한 세션아이디 등록
		session.setAttribute("OP_LAST_USERNAME", loginId);

		//////////////////
		// srhan. DB에 저장된 로그인 시도하는 사람의 이메일을 불러온다.
		// 1. 이메일이 있으면 메일 보내고
		// 2. 없으면 메일 입력하는 UI 보이기
		//////////////////
//		User user = userService.getUserByLoginId(loginId);
		User user = userService.getPureUserByLoginId(loginId);
		if(StringUtils.isEmpty(user.getEmail())) {
			return JsonViewUtils.success("NOEMAIL");
		} else {
			Email result = null;
			try {
				Email email = new Email();

				//email.setAuthTarget("E");
				email.setAuthTarget("L");		// srhan. Login email send
		    	email.setFrstRegisterId(Long.valueOf(0));
		    	email.setSubject("로그인 인증번호 발송");

		    	//int authNum = loginLogService.createAuthCode();
		    	LoginLogParam userParam = new LoginLogParam();
		    	userParam.setLoginId(loginId);
		    	//사무실 내부 인증번호는 '250324'로 고정
		    	if(serverDomain.contains("localhost")) {
		    		userParam.setAuthNum("250324");
		    	}
		    	String authNum = loginLogService.insertLoginEmailLog(userParam);
	    		email.setContent("고향사랑e음 로그인 인증번호: " + authNum);

	    		// 생성된 인증번호를 DB에 저장
		    	email.setSendType("D");
				result = emailService.insertEmail(email);
				SendParam param = new SendParam();
				param.setEmailId(result.getEmailId());

				List<EmailSend> list = new ArrayList<>();
				EmailSend emailSend;// = new EmailSend();

				emailSend = new EmailSend();
				emailSend.setEmail(user.getEmail());
				list.add(emailSend);

				param.setSendUserList(list);
				emailService.sendEmail(param);

				// 이메일 DB에 접근해서 sendDate를 이전 시간으로 변경하여 현재 발송중인 대량메일보다 우선적으로 발송하게 한다.
//				long emailId = result.getEmailId();
//				EmailDetailParam emailParam = new EmailDetailParam();
//				emailParam.setEmailId(emailId);
//				emsMapper.updateEmsSendDate(emailParam);

			} catch (OpRuntimeException e) {
				log.error("■■■LOGIN■■■ loginUserEmail RuntimeException {}", e);
			} catch (Exception e) {
				// LOCAL DEV ONLY: the mass-mail dispatch pipeline (EMS host / multi-datasource
				// email queue) isn't available locally. The auth code is already persisted by
				// insertLoginEmailLog() above, so a downstream mail-send failure shouldn't block login.
				log.warn("[LOCAL DEV] email dispatch unavailable, auth code already stored: {}", e.toString());
			}
		}

		return JsonViewUtils.success("SUCC" + user.getEmail());		// "FAIL"

	}

	@PostMapping("/opmanager/user/login-user-auth-check")
	public JsonView loginUserAuthCheck(HttpServletRequest request, Model model
			, @RequestParam("loginId") String loginId
			, @RequestParam("password") String password
			, @RequestParam("authNum") String authNum
			, @RequestParam(value="opEmail", required=false) String opEmail
			, HttpSession session) throws UnsupportedEncodingException {
		LoginLogParam param = new LoginLogParam();
		param.setLoginId(loginId);
		param.setAuthNum(authNum);
		boolean authChk = loginLogService.getEmailAuthChk(param);
		if(authChk) {
			// op_user 테이블의 email 등록
			User user = new User();
			//user.setLoginId(loginId);		// 암호화해서 넘겨야 함.
			//user.setEmail(opEmail);
			user.setLoginId(pCrypto.Encrypt("normal", loginId, ""));		// 암호화해서 넘겨야 함.

			// 이메일이 없던 사용자는 인증 성공하면 op_user, op_manager 테이블의 email에 등록
			if(StringUtils.isNotEmpty(opEmail)) {
				user.setEmail(pCrypto.Encrypt("normal", opEmail, ""));			// 암호화해서 넘겨야 함.
				userService.updateUserByLoginId(user);


				// op_manager 테이블의 email 등록
				OpManagerParam opmanagerParam = new OpManagerParam();
				opmanagerParam.setLoginId(loginId);
				opmanagerParam.setEmail(opEmail);
				userService.updateManagerInfo(opmanagerParam);


				// 대량메일 솔루션에 이메일 전송 등록 - 20251021. srhan. 수정 필요. 아무 역할도 안한다. 이게 된다면 다시 분석 필요!!!!!!!
				opmanagerParam.setEmail(opEmail);
			}
			ManagerRequestResult result = null;
			result = managerRequestService.getLoginUserValid(request, loginId, password);
			try {
				String code = CommonUtils.dataNvl(result.getCode());
				if(code.equals("PASS_CHANGE_C")) {
					return JsonViewUtils.success(code);
				}
			} catch (RuntimeException e) {
				log.error("ERROR: {}", getClass().getName() + " :: loginUserValid RuntimeException ===========");
			}


			// srhan. 20251021. ID/PW 넣고 로그인 버튼 누르면 인증번호 넣지 않고 새로고침해도 로그인되는 버그 수정
			User userDetail = userService.getManagerByLoginId(loginId);
			user.setUserId(userDetail.getUserId());
			String txt = userService.setSessionByManager(request, user);

			return JsonViewUtils.success(txt);
		}else {
			return JsonViewUtils.success("FAIL");
		}
	}

	@PostMapping("/opmanager/user/login-user-email-save")
	public JsonView loginUserEmailSave(HttpServletRequest request, Model model
			, @RequestParam("loginId") String loginId
			, @RequestParam("password") String password
			, @RequestParam("opEmail") String opEmail, HttpSession session) throws UnsupportedEncodingException {
		OpManagerParam param = new OpManagerParam();
		param.setLoginId(loginId);
		param.setEmail(opEmail);

		// 관리자 이메일 update
		User user = new User();
		user.setLoginId(pCrypto.Encrypt("normal", loginId, ""));
		user.setEmail(pCrypto.Encrypt("normal", opEmail, ""));
		int successUpdateManager	= userService.updateManagerInfo(param);		// op_manager 이메일 등록
		int successUpdateUser		= userService.updateUserByLoginId(user);	// op_user 이메일 등록
		if(successUpdateManager > 0 && successUpdateUser > 0) {
			// 등록된 이메일로 인증번호 생성하여 발송
			Email result = null;
			try {
				Email email = new Email();

				//email.setAuthTarget("E");
				email.setAuthTarget("L");		// srhan. Login email send
		    	email.setFrstRegisterId(Long.valueOf(0));
		    	email.setSubject("로그인 인증번호 발송");

		    	//int authNum = loginLogService.createAuthCode();
		    	LoginLogParam userParam = new LoginLogParam();
		    	userParam.setLoginId(loginId);
		    	//사무실 내부 인증번호는 '250324'로 고정
		    	if(serverDomain.contains("localhost")) {
		    		userParam.setAuthNum("250324");
		    	}

		    	String authNum = loginLogService.insertLoginEmailLog(userParam);
	    		email.setContent("고향사랑e음 로그인 인증번호: " + authNum);

	    		// 생성된 인증번호를 DB에 저장
		    	email.setSendType("D");
				result = emailService.insertEmail(email);
				SendParam sendParam = new SendParam();
				sendParam.setEmailId(result.getEmailId());

				List<EmailSend> list = new ArrayList<>();
				EmailSend emailSend;// = new EmailSend();

				emailSend = new EmailSend();
				emailSend.setProcessedDate("20250101000000");		// 과거 시간으로 설정하여 우선순위 높이기
				emailSend.setEmail(opEmail);			// hanmail.net			3분 40초
				//emailSend.setEmail("gkstofhs@hanmail.net");			// hanmail.net	2분
				//emailSend.setEmail("srhan@u-cube.kr");		// 저장된 다음메일이 느려서 회사메일로 변경하여 테스트
				//emailSend.setEmail("keykjy@gmail.com");		// 저장된 다음메일이 느려서 회사메일로 변경하여 테스트
				//emailSend.setUserName(user.getUserName());
				list.add(emailSend);

				sendParam.setSendUserList(list);
				emailService.sendEmail(sendParam);
			} catch (OpRuntimeException e) {
				log.error("■■■LOGIN■■■ loginUserEmail RuntimeException {}", e);
			} catch (Exception e) {
				// LOCAL DEV ONLY: mass-mail dispatch pipeline unavailable locally; the auth code
				// is already persisted by insertLoginEmailLog() above.
				log.warn("[LOCAL DEV] email dispatch unavailable, auth code already stored: {}", e.toString());
			}

			return JsonViewUtils.success("SUCC");		// "SUCCESS"
		} else {
			return JsonViewUtils.success("FAIL");		// "FAIL"
		}
	}

	/**
	 * 비밀번호 변경 팝업 호출
	 * @param model
	 * @param pageType - I: 관리자 비밀번호 초기화, C: 관리자 비밀번호 변경, F: 초기 비밀번호 설정
	 * @param loginId
	 * @return
	 */
	@PostMapping("/opmanager/user/popup/temp-password-change")
	@RequestProperty(title = "비밀번호 변경", layout = "base")
	public String tempPasswordChangePopup(Model model, @RequestParam("pageType") String pageType, @RequestParam("loginId") String loginId) {
		model.addAttribute("pageType"	, pageType);
		model.addAttribute("loginId"	, loginId);
		return ViewUtils.getView("/user/popup/temp-password-change");
	}

	/**
	 * 비밀번호 변경 (관리자 비밀번호 초기화, 초기 비밀번호 설정, 관리자 비밀번호 변경안내 - 3개월)
	 * @param tempPasswordChange
	 * @return
	 */
	@PostMapping("/opmanager/user/temp-password-change")
	public JsonView tempPasswordChange(HttpServletRequest request, TempPasswordChange tempPasswordChange) {
		String code = "FAIL";

		try {
			code = userService.updateTempPasswordChange(request, tempPasswordChange);
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", getClass().getName() + " :: tempPasswordChange RuntimeException ===========");
		}

		return JsonViewUtils.success(code);
	}

	/**
	 * PKI 인증서 등록
	 * @param model
	 * @return
	 */
	@GetMapping("/opmanager/user/pki/form")
	@RequestProperty(title="관리자페이지 > PKI 등록")
	public String pkiForm(Model model) {
		return ViewUtils.getManagerView("/user/pki/form");
	}

	/**
	 * PKI 인증서 등록 > 유효성 체크
	 * @param pkiManagerLogin
	 * @return
	 */
	@GetMapping("/opmanager/user/pki/form/valid")
	public JsonView getPkiFormValid(PkiManagerLogin pkiManagerLogin) {
		PkiManagerLoginResult result = new PkiManagerLoginResult();
		result.setCode("FAIL");

		try {
			result = userService.getPkiManagerFormValid(pkiManagerLogin);

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", getClass().getName() + " :: getPkiFormValid RuntimeException ===========");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 회원 DN 수정
	 * @param pkiManagerLogin
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/opmanager/user/pki/update")
	public JsonView pkiManagerUpdateProcess(PkiManagerLogin pkiManagerLogin) throws Exception {
		PkiManagerLoginResult result = new PkiManagerLoginResult();
		result.setCode("FAIL");

		try {
			result = userService.updatePkiManagerMberDn(pkiManagerLogin);
		} catch (UserException e) {
			log.error(e.getMessage());
			//log.error("ERROR: {}", e.getMessage(), e);
			//log.error("ERROR: {}", getClass().getName() + " :: pkiManagerUpdateProcess RuntimeException ===========");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 회원 DN, FinDN 삭제
	 * @param pkiManagerLogin
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/opmanager/user/pki/delete")
	public JsonView pkiManagerDeleteProcess(PkiManagerLogin pkiManagerLogin) throws Exception {
		PkiManagerLoginResult result = new PkiManagerLoginResult();
		result.setCode("FAIL");

		try {
			result = userService.updateDeletePkiManagerMberDn(pkiManagerLogin);
		} catch (UserException e) {
			log.error(e.getMessage());
			//log.error("ERROR: {}", e.getMessage(), e);
			//log.error("ERROR: {}", getClass().getName() + " :: pkiManagerUpdateProcess RuntimeException ===========");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * PKI 관리자 로그인 유효성 체크
	 * @param request
	 * @param pkiManagerLogin
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/opmanager/user/pki/login-user-valid")
	public JsonView pkiLoginUserValid(HttpServletRequest request, PkiManagerLogin pkiManagerLogin, HttpSession session) throws Exception {
		PkiManagerLoginResult result = new PkiManagerLoginResult();
		result.setCode("FAIL");

		try {
			//개인정보이력을 위한 세션아이디 등록
			result = userService.savePkiManagerLoginUserValid(request, pkiManagerLogin, session);

		} catch (UserException e) {
			log.error(e.getMessage());
			result.setCode("ERR");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 패스워드 암호화
	 * @param password
	 * @return
	 */
	@GetMapping("/opmanager/user/password-enc")
	public JsonView passwordEncryp(@RequestParam String password) {
		String passwordEnc = "";

		try {
			passwordEnc = pCrypto.Encrypt("hash.5", password, "");
		} catch (UnsupportedEncodingException e) {
			log.error("OpmanagerController :: passwordEncryp");
			return JsonViewUtils.failure("오류가 발생했습니다.");
		}

		return JsonViewUtils.success(passwordEnc);
	}

	/**
	 * <pre>
	 * comment : 임시팝업
	 * preMethodName : 팝업
	 * author :  이광교
	 * date : 2023. 3. 9.
	 *
	 *</pre>
	 * @param requestContext
	 * @param model
	 * @return
	 * String
	 */
	@GetMapping("/opmanager/popup/temp")
	public String tempPopup(Model model) {
		return ViewUtils.getView("/give/popup/popupTemp");
	}

	@GetMapping("/opmanager/popup/tempSeller")
	public String tempSellerPopup(Model model) {
		return ViewUtils.getView("/give/popup/popupTempSeller");
	}

	@PostMapping("/opmanager/user/fin/token")
	public JsonView getFinancUserToken(@RequestBody FinancInfo financInfo) {

		try {
			Map<String, Object> result = userService.getFinancUserToken(financInfo);
			return JsonViewUtils.success(result);
		} catch (RuntimeException e) {
			log.error(e.getMessage());
			return JsonViewUtils.failure(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			return JsonViewUtils.failure(e.getMessage());
		}

	}

	@GetMapping("/opmanager/user/fin/nonce")
	public JsonView getFinancNonce(HttpServletResponse response) {

		try {
			String resultNonce = userService.bytesToHexString();
			return JsonViewUtils.success(resultNonce);
		} catch (UserException e) {
			// TODO Auto-generated catch block
			return JsonViewUtils.failure("난수 생성 중 오류가 발생했습니다.");
		}

	}

	@GetMapping("/opmanager/loginFincertTest")
	@RequestProperty(title="관리자페이지 > 로그인")
	public String fincertLogin(HttpServletRequest request,
			@RequestParam(value="error", required=false) String error,
			HttpSession session,
			Model model) {

		String target = "";
		if (request.getParameter("target") != null) {
			target = request.getParameter("target");
		}

		model.addAttribute("OP_LOGIN_LAST_USERNAME", session.getAttribute(IdPasswordAuthenticationFilter.OP_LOGIN_LAST_USERNAME_KEY));
		model.addAttribute("error", error);
		model.addAttribute("target", target.equals("") ? "/opmanager/" : target);

		model.addAttribute("frontendUrl", SalesonProperty.getSalesonUrlFrontend());
		model.addAttribute("conf-path", confPath);

		return ViewUtils.getManagerView("/user/loginFincertTest");
	}

	/**
	 * PKI 인증서 등록
	 * @param model
	 * @return
	 */
	@GetMapping("/opmanager/user/pki/formFincertTest")
	@RequestProperty(title="관리자페이지 > PKI 등록")
	public String pkiFormFincert(Model model) {
		return ViewUtils.getManagerView("/user/pki/formFincertTest");
	}

	private String getServerIp() {

		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		}
		catch ( UnknownHostException e ) {
			log.error("formFincertTest UnknownHostException {}", e.getStackTrace()[0]);
		}

		if( local == null ) {
			return "";
		}
		else {
			String ip = local.getHostAddress();
			return ip;
		}

	}

	/**
	 * 기부금 전체현황 > 기부내역변경 (팝업)
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/opmanager/user/popup/emailModifyInfo/{loginId}")
	@RequestProperty(title = "이메일 설정", layout = "base")
	public String giveModifyInfo(@PathVariable("loginId") String loginId , Model model) {
		//model.addAttribute("infoPop", giveStateService.getGiveStateModifyInfo(elctrnPayNo));
		model.addAttribute("userDetail", userService.getManagerByLoginId(loginId));
		model.addAttribute("userKey",UserUtils.getUserId());
		return ViewUtils.getView("/user/popup/email-modify-popup");
	}

}
