package saleson.shop.user;

import com.onlinepowers.framework.annotation.handler.Authorize;
import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.token.TokenService;
import com.onlinepowers.framework.security.token.domain.Token;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import saleson.common.enumeration.eventcode.EventCodeLogType;
import saleson.common.enumeration.eventcode.EventCodeType;
import saleson.common.userauth.UserAuthService;
import saleson.common.userauth.domain.UserAuth;
import saleson.common.utils.EventViewUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.eventcode.EventCodeLog;
import saleson.shop.businesscode.BusinessCodeService;
import saleson.shop.categoriesedit.CategoriesEditService;
import saleson.shop.categoriesedit.support.CategoriesEditParam;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.coupon.CouponService;
import saleson.shop.coupon.domain.Coupon;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.eventcode.EventCodeService;
import saleson.shop.log.ChangeLogService;
import saleson.shop.point.PointService;
import saleson.shop.policy.PolicyService;
import saleson.shop.user.domain.AuthUserInfo;
import saleson.shop.user.domain.Customer;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.support.AgreeDto;
import saleson.shop.user.domain.UserEncryptor;
import saleson.shop.user.support.UserSearchParam;
import saleson.shop.usersns.UserSnsService;
import saleson.shop.usersns.domain.UserSns;
import saleson.shop.wishlist.WishlistService;
import saleson.shop.wishlist.domain.WishlistGroup;
import saleson.shop.zipcode.ZipcodeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/users")
@RequestProperty(title = "회원", layout = "default")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private String AUTH_USER_KEY = "AUTH_USER_KEY";

	@Autowired
	private CodeResolver codeResolver;

	@Autowired
	private BusinessCodeService businessCodeService;

	@Autowired
	private UserService userService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private CategoriesEditService categoriesEditService;

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private ZipcodeService zipcodeService;

	@Autowired
	@Qualifier("smsTokenService")
	private TokenService smsTokenService;

	@Autowired
	private PointService pointService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private UserSnsService userSnsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private ChangeLogService changeLogService;

	@Autowired
	private EventCodeService eventCodeService;

	@Autowired
	private PolicyService policyService;

	@Autowired
	private UserEncryptor userEncryptor;

	/**
	 * 본인확인 서비스 화면
	 *
	 * @param requestContext
	 * @return
	 */
	@GetMapping("user-auth")
	@RequestProperty(layout = "base")
	public String ipinAuth(RequestContext requestContext, @RequestParam("type") String type,
		@RequestParam("target") String target, Model model) {

		String encryptString = "";
		if ("ipin".equals(type)) {
			encryptString = userAuthService.getIpinEncryptString(requestContext.getRequest(), target);
		} else {
			encryptString = userAuthService.getPccEncryptString(requestContext.getRequest(), target);
		}

		model.addAttribute("type", type);
		model.addAttribute("target", target);
		model.addAttribute("encryptString", encryptString);
		return "view:/users/user-auth";
	}

	/**
	 * 본인확인 서비스 처리
	 *
	 * @param requestContext
	 * @return
	 */
	@GetMapping(value = "user-auth-success/{type}")
	public String ipinAuthSuccess(RequestContext requestContext, HttpSession session) {

		return handleIpinResponse(requestContext);
	}

	@PostMapping(value = "user-auth-success/{type}")
	public String ipinAuthSuccess2(RequestContext requestContext, HttpSession session) {

		return handleIpinResponse(requestContext);
	}

	private String handleIpinResponse(RequestContext requestContext) {
		UserAuth userAuth = userAuthService.getUserAuth(requestContext.getRequest(), "0");
		if (userAuth == null || "".equals(userAuth.getAuthKey())) {
			throw new PageNotFoundException();
		}

		try {

			if ("IPIN".equals(userAuth.getServiceMode())) {
				userAuthService.setIpinAuthSuccess(requestContext.getRequest());
			} else if ("PCC".equals(userAuth.getServiceMode())) {
				userAuthService.setPccAuthSuccess(requestContext.getRequest());
			}

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "========= handleIpinResponse RuntimeException =========");

			String errorRedirectUrl = "/users/user-auth?type=" + userAuth.getServiceMode().toLowerCase();
			errorRedirectUrl += "&target=" + userAuth.getServiceTarget();
			return ViewUtils.redirect(errorRedirectUrl, "본인인증 처리도중 에러가 발생하였습니다.");

		}

		String javascript = "opener.location.href='/'; self.close()";
		if ("JOIN".equals(userAuth.getServiceTarget())
				|| "FIND-ID".equals(userAuth.getServiceTarget())
				|| "FIND-PASSWORD".equals(userAuth.getServiceTarget())) {
			javascript = "opener.userAuthSuccess(); self.close()";
		}

		return ViewUtils.redirect("/users/blank", "", javascript);
	}

	@GetMapping("join-us")
	@RequestProperty(title = "회원가입")
	public String joinUs() {
		return ViewUtils.getView("/users/join-us");
	}

	@GetMapping("sns-join")
	@RequestProperty(title = "회원가입")
	public String chooseSns(String snsType, Model model) {
		Config config = ShopUtils.getConfig();

		model.addAttribute("snsType", snsType);
		model.addAttribute("config", config);
		model.addAttribute("isJoin", true);
		return ViewUtils.getView("/users/sns-join");
	}

	/**
	 * 본인인증 페이지
	 *
	 * @return
	 */
	@GetMapping("confirm")
	@RequestProperty(title = "회원가입")
	public String confirm(HttpSession session) {

		if (UserUtils.isUserLogin()) {
			return ViewUtils.redirect("/");
		}

		session.removeAttribute(AUTH_USER_KEY);
		return ViewUtils.getView("/users/confirm");
	}


	@PostMapping("/authCheck/{accessToken}/{requestToken}")
	public JsonView authCheck(RequestContext requestContext, HttpSession session,
		AuthUserInfo authUserInfo,
		@PathVariable("accessToken") String smsAuthNumber,
		@PathVariable("requestToken") String requestToken) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		Token token = new Token();
		token.setAccessToken(smsAuthNumber);
		token.setRequestToken(requestToken);

		session.removeAttribute(AUTH_USER_KEY);
		if (!smsTokenService.isValidToken(token)) {
			return JsonViewUtils.failure("인증번호가 올바르지 않습니다.");
		}

		// 인증된 휴대전화번호 및 이름 저장
		session.setAttribute(AUTH_USER_KEY, authUserInfo);
		return JsonViewUtils.success();
	}

	/**
	 * 약관 동의 페이지
	 *
	 * @return
	 */
	@PostMapping("agreement")
	@RequestProperty(title = "회원가입")
	public String agreement(Config config, Model model, HttpSession session, RequestContext requestContext,
		@RequestParam(value = "redirect", required = false, defaultValue = "") String redirect) {

		// 본인확인 성공 유무
		AuthUserInfo authUser = (AuthUserInfo) session.getAttribute(AUTH_USER_KEY);
		if (ValidationUtils.isNull(authUser)) {
			throw new PageNotFoundException();
		}


		model.addAttribute("config", ShopUtils.getConfig());
		model.addAttribute("redirect", redirect);
		return ViewUtils.getView("/users/agreement");
	}

	/**
	 * 정보입력 페이지
	 *
	 * @return
	 */
	@GetMapping("entryForm")
	@RequestProperty(title = "회원가입")
	public String occupied(@ModelAttribute Customer customer, RequestContext requestContext, Model model, HttpSession session,
					@RequestParam(value = "redirect", required = false, defaultValue = "") String redirect,
		User user, UserDetail userDetail) {

		userService.setUserDetailHyphen(user);

		return getJoinEntryForm(customer, model, redirect, user, userDetail);
	}

	@PostMapping("entryForm")
	@RequestProperty(title = "회원가입")
	public String join(@ModelAttribute Customer customer, RequestContext requestContext, Model model, HttpSession session,
				   @RequestParam(value = "redirect", required = false, defaultValue = "") String redirect,
					   User user, UserDetail userDetail) {

		userService.setUserDetailHyphen(user);

		return getJoinEntryForm(customer, model, redirect, user, userDetail);
	}

	private String getJoinEntryForm(@ModelAttribute Customer customer, Model model,
					@RequestParam(value = "redirect", required = false, defaultValue = "") String redirect, User user, UserDetail userDetail) {
    /*
	// 본인확인 성공 유무
	AuthUserInfo authUser = (AuthUserInfo) session.getAttribute(AUTH_USER_KEY);
	if (ValidationUtils.isNull(authUser)) {
		throw new PageNotFoundException();
	}
	*/
		checkAgreeTerms(customer);
		//List<CodeInfo> positionTypeCode = CodeUtils.getCodeInfoList("POSITION_TYPE");
		List<CodeInfo> telCodes = CodeUtils.getCodeInfoList("TEL");
		List<CodeInfo> phoneCodes = CodeUtils.getCodeInfoList("PHONE");
		String years = DateUtils.getToday("yyyy");


		model.addAttribute("user", UserUtils.getGuestLogin() == null ? user : UserUtils.getGuestLogin());
		model.addAttribute("years", years);
		model.addAttribute("telCodes", telCodes);
		model.addAttribute("phoneCodes", phoneCodes);
		model.addAttribute("redirect", redirect);
		model.addAttribute("agrees",userDetail.getAgreeNameDtos());
		return ViewUtils.getView("/users/join");
	}

	private void checkAgreeTerms(Customer customer) {
		if (ValidationUtils.isEmpty(customer.getTerms())
			|| ValidationUtils.isEmpty(customer.getPrivacy())
			|| !customer.getTerms().equals("1")
			|| !customer.getPrivacy().equals("1")) {
			throw new UserException(MessageUtils.getMessage("M01472")); // 정상적인 접근이 아닙니다.
		}
	}

	@PostMapping("join")
	@RequestProperty(title = "회원가입  처리")
	public String joinAction(HttpServletRequest request, Model model,
		@RequestParam(value = "redirect", required = false, defaultValue = "") String redirect,
		User user, UserDetail userDetail) {

		// 가입 불가 아이디 체크
		String checkResult = userService.checkDuplication(user);

		if ("isOccupiedId".equals(checkResult)) {
			return ViewUtils.redirect("/users/entryForm?terms=1&privacy=1", "사용할수 없는 Email 입니다");
		}

		long userId = sequenceService.getLong("OP_USER");
		user.setUserId(userId);
		userDetail.setUserId(userId);

		// PC 회원가입 flag 추가 [LSW 2016.08.05]
		userDetail.setSiteFlag("0");

		userDetail.processHyphen();
		userService.insertUserAndUserDetail(user, userDetail);

		UserCouponParam userCouponParam = new UserCouponParam();
		if (userDetail.getLevelId() != 0) userCouponParam.setUserLevelId(userDetail.getLevelId());

		//신규회원가입 쿠폰 발급[2017-09-08]minae.yun
		userCouponParam.setCouponTargetTimeType("2");
		List<Coupon> newUserCouponList = couponService.getCouponByTargetTimeType(userCouponParam);

		if (newUserCouponList != null && newUserCouponList.size() != 0) {
			for (Coupon coupon : newUserCouponList) {
				userCouponParam.setCouponId(coupon.getCouponId());
				userCouponParam.setUserId(userId);
				couponService.insertCouponTargetUserOne(userCouponParam);
				couponService.userCouponDownload(userCouponParam);
			}
		}

		// 카테고리 정보
		CategoriesEditParam categoriesEditParam = new CategoriesEditParam();

		categoriesEditParam.setCode("main");
		categoriesEditParam.setEditKind("1");

		model.addAttribute("categoryEdit", categoriesEditService.getCategoryFontPosition(categoriesEditParam));


		// 회원가입 후 자동로그인.
		String userLoginId = user.getLoginId();

		String loginId = ShadowUtils.getShadowLoginKey(userLoginId, "");
		String password = ShadowUtils.getShadowLoginPassword(userLoginId);
		String signature = ShadowUtils.getShadowLoginSignature(userLoginId);

		model.addAttribute("loginId", loginId);
		model.addAttribute("password", password);
		model.addAttribute("signature", signature);
		model.addAttribute("redirect", redirect);

		eventCodeService.insertLog(request, userId, EventCodeType.NONE, EventCodeLogType.USER);

		return ViewUtils.getView("/users/join-login");
	}


	@GetMapping("join-complete")
	@RequestProperty(title = "회원가입  완료")
	public String joinComplete(RequestContext requestContext, Model model,
		@RequestParam(value = "redirect", required = false, defaultValue = "") String redirect) {

		CategoriesEditParam categoriesEditParam = new CategoriesEditParam();

		categoriesEditParam.setCode("user");
		categoriesEditParam.setEditKind("1");

		model.addAttribute("categoryEdit", categoriesEditService.getCategoryFontPosition(categoriesEditParam));
		model.addAttribute("redirect", redirect);
		return ViewUtils.getView("/users/join-complete");
	}


	/**
	 * 로그인 페이지
	 *
	 * @param popup
	 * @param redirect
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("login")
	@RequestProperty(title = "로그인")
	public String login(@RequestParam(value = "popup", required = false, defaultValue = "") String popup,
		@RequestParam(value = "redirect", required = false, defaultValue = "") String redirect,
		@RequestParam(value = "target", required = false, defaultValue = "") String target,
		RequestContext requestContext, Model model) {

		String viewName = "/users/login";
		if (popup.equals("1")) {
			RequestContextUtils.setLayout("base");
			if ("order".equals(target)) {
				String prev = RequestContextUtils.getPreviousUri();
				String opener = "";

				if (prev != null) {
					if (prev.contains("/products/view")) {
						opener = "view";
					} else if (prev.contains("/cart")) {
						opener = "cart";
					}

					model.addAttribute("opener", opener);
				}
			}
			model.addAttribute("popup", StringUtils.stripXSS(popup));
			model.addAttribute("target", StringUtils.stripXSS(target));
			viewName = "/users/login-popup";
		}

		model.addAttribute("redirect", StringUtils.stripXSS(redirect));
		return ViewUtils.getView(viewName);
	}

	@GetMapping("popup-login")
	public String popupLogin(RequestContext requestContext) {
		RequestContextUtils.setLayout("base");

		return ViewUtils.view();
	}

	/**
	 * 비회원 인증후 로그인 처리
	 *
	 * @param userDetail
	 * @param guestUsername
	 * @return
	 */
	@PostMapping("guestLogin")
	public String guestLogin(UserDetail userDetail, HttpSession session,
		@RequestParam("guestUsername") String guestUsername) {

		// 본인확인 성공 유무
		AuthUserInfo authUser = (AuthUserInfo) session.getAttribute(AUTH_USER_KEY);
		if (ValidationUtils.isNull(authUser)) {
			throw new PageNotFoundException();
		}

		userDetail.processHyphen();
		UserUtils.setGuestLogin(guestUsername, userDetail.getPhoneNumber());

		return ViewUtils.redirect("/mypage/order");
	}

	/**
	 * 회원 정보 중복 확인
	 *
	 * @param requestContext
	 * @param model
	 * @param type
	 * @param value
	 * @return
	 */
	@PostMapping("user-availability-check")
	public JsonView userAvailabilityCheck(RequestContext requestContext, Model model,
		@RequestParam("type") String type, @RequestParam("value") String value) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		// 가입 불가 아이디 체크
		Config config = configService.getShopConfigCache(Config.SHOP_CONFIG_ID);

		boolean availability = true;
		if (StringUtils.hasText(config.getDeniedId())) {
			String[] denyIds = StringUtils.tokenizeToStringArray(config.getDeniedId(), ",");

			for (String userId : denyIds) {
				if ("".equals(userId.trim())) {
					continue;
				}

				if (userId.trim().equals(value.trim())) {
					availability = false;
					break;
				}
			}
		}

		if (availability == true) {

			int count = 0;
			if ("loginId".equals(type)) {
				count = userService.getUserCountByLoginId(value);
			} else if ("email".equals(type)) {
				count = userService.getUserCountByEmail(value);
			} else if ("nickname".equals(type)) {
				count = userService.getUserCountByNickname(value);
			} else if ("phoneNumber".equals(type)) {
				count = userService.getUserCountByPhoneNumber(value);
			} else {
				throw new RuntimeException("잘못된 접근 - 정의되지 않은 타입");
			}

			if (count > 0) {
				availability = false;
			}
		}

		model.addAttribute("availability", availability);
		return JsonViewUtils.success();
	}


	@PostMapping("user-availability-check-join")
	public JsonView userAvailabilityCheckJoin(RequestContext requestContext, Model model,
		@RequestParam("type") String type, @RequestParam("value") String value) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		// 가입 불가 아이디 체크
		Config config = configService.getShopConfigCache(Config.SHOP_CONFIG_ID);

		boolean availability = true;
		if (StringUtils.hasText(config.getDeniedId())) {
			String[] denyIds = StringUtils.tokenizeToStringArray(config.getDeniedId(), ",");

			for (String userId : denyIds) {
				if ("".equals(userId.trim())) {
					continue;
				}

				if (userId.trim().equals(value.trim())) {
					availability = false;
					break;
				}
			}
		}

		if (availability == true) {

			int count = 0;
			if ("loginId".equals(type)) {
				count = userService.getUserCountByLoginId(value);
			} else if ("email".equals(type)) {
				count = userService.getUserCountByEmail(value);
			} else if ("nickname".equals(type)) {
				count = userService.getUserCountByNickname(value);
			} else if ("phoneNumber".equals(type)) {
				count = userService.getUserCountByPhoneNumber(value);
			} else {
				throw new RuntimeException("잘못된 접근 - 정의되지 않은 타입");
			}

			if (count > 0) {
				availability = false;
			}
		}

		return JsonViewUtils.success(availability);
	}


	@RequestProperty(layout = "blank")
	@PostMapping("find-user")
	public JsonView findUser(RequestContext requestContext, Model model,
		@RequestParam("loginId") String loginId) {

		// 가입 불가 아이디 체크
		Config config = configService.getShopConfigCache(Config.SHOP_CONFIG_ID);

		int userCount = 0;
		if (StringUtils.hasText(config.getDeniedId())) {
			String[] denyIds = StringUtils.tokenizeToStringArray(config.getDeniedId(), ",");

			for (String userId : denyIds) {
				if ("".equals(userId.trim())) {
					continue;
				}

				if (userId.trim().equals(loginId.trim())) {
					userCount = 1;
					break;
				}
			}
		}

		// 회원 테이블에서 조회.
		if (userCount == 0) {
			User user = new User();
			user.setLoginId(loginId);
			userCount = userService.getUserCountByUserInfo(user);
		}

		return JsonViewUtils.success(userCount);
	}

	/**
	 * 회원정보 조회(ID, 비밀번호 찾기)
	 * @param requestContext
	 * @param session
	 * @param authUserInfo
	 * @param userName
	 * @param phoneNumber
	 * @param loginId
	 * @return
	 */
	@PostMapping("/check-account")
	public JsonView checkAccount(RequestContext requestContext, HttpSession session, AuthUserInfo authUserInfo,
		@RequestParam("userName") String userName, @RequestParam("phoneNumber") String phoneNumber,
		@RequestParam("loginId") String loginId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		authUserInfo.setUserName(userName);
		authUserInfo.setPhoneNumber(phoneNumber);
		authUserInfo.setLoginId(loginId);

		User user = userService.getUserInfoByUserName(authUserInfo);

		if (user == null) {
			return JsonViewUtils.failure("회원정보가 존재하지 않습니다.");
		}

		return JsonViewUtils.success();
	}

	/**
	 * 원정보 조회(회원가입시)
	 * @param requestContext
	 * @param session
	 * @param authUserInfo
	 * @param userName
	 * @param phoneNumber
	 * @param email
	 * @return
	 */
	@PostMapping("/check-account-join")
	public JsonView checkAccountJoin(RequestContext requestContext, HttpSession session, AuthUserInfo authUserInfo,
		@RequestParam("userName") String userName, @RequestParam("phoneNumber") String phoneNumber,
		@RequestParam("email") String email) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		authUserInfo.setUserName(userName);
		authUserInfo.setPhoneNumber(phoneNumber);
		authUserInfo.setEmail(email);

		User user = userService.getUserInfoByUserName(authUserInfo);

		if (user != null) {
			return JsonViewUtils.failure("이미 가입되어있습니다.");
		}

		UserUtils.setGuestLogin(userName, phoneNumber);

		return JsonViewUtils.success();
	}

	/**
	 * 회원 아이디 찾기
	 *
	 * @param searchParam
	 * @return
	 */
	@GetMapping(value = "find-id")
	@RequestProperty(title = "아이디 찾기", layout = "base")
	public String findId(UserSearchParam searchParam) {

		return ViewUtils.getView("/users/find-id");
	}

	/**
	 * 회원 아이디 찾기실행
	 *
	 * @param searchParam
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping("find-id")
	@RequestProperty(title = "아이디 찾기", layout = "base")
	public String lostId(UserSearchParam searchParam, Model model, HttpSession session) {

		// 본인확인 성공 유무
		AuthUserInfo authUser = (AuthUserInfo) session.getAttribute(AUTH_USER_KEY);
		if (ValidationUtils.isNull(authUser)) {
			throw new PageNotFoundException();
		}

		User user = userService.getUserInfoByUserName(authUser);

		if (user == null) {
			return ViewUtils.redirect("/users/find-id?error=1");
		}

		model.addAttribute("user", user);
		return ViewUtils.getView("/users/find-id-result");
	}


	/**
	 * 회원 비밀번호 찾기
	 *
	 * @param searchParam
	 * @return
	 */
	@GetMapping(value = "find-password")
	@RequestProperty(title = "비밀번호 찾기", layout = "base")
	public String findPassword(UserSearchParam searchParam) {

		return ViewUtils.getView("/users/find-password");
	}


	/**
	 * 회원 비밀번호 찾기실행
	 *
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@PostMapping("find-password")
	@RequestProperty(title = "비밀번호 찾기", layout = "base")
	public String lostPass(UserSearchParam searchParam, Model model, HttpSession session) {

		// 본인확인 성공 유무
		AuthUserInfo authUser = (AuthUserInfo) session.getAttribute(AUTH_USER_KEY);
		if (ValidationUtils.isNull(authUser)) {
			throw new PageNotFoundException();
		}

		User user = userService.getUserInfoByUserName(authUser);

		if (user == null) {
			return ViewUtils.redirect("/users/find-password?error=1");
		}

		model.addAttribute("user", user);
		return ViewUtils.getView("/users/find-password-result");
	}


	@PostMapping("find-password-change")
	@RequestProperty(title = "비밀번호 변경")
	public JsonView passChange(RequestContext requestContext, User user, Model model) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			userService.updateUser(user);

			//LSW 2016.08.05 비밀번호 변경 시, 메일, SMS 추가
		/*	user = userService.getUserByUserId(user.getUserId());
			UserDetail userDetail = userService.getUserDetail(user.getUserId());

		    if (!ObjectUtils.isEmpty(userDetail.getPhoneNumber())) {
				user.setUserDetail(userDetail);
				userService.sendSmsAndEmail(user, "pwsearch");
		    }*/
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "======== passChange RuntimeException ========");
			return JsonViewUtils.failure("비밀번호 변경중 에러가 발생하였습니다.");
		}

		return JsonViewUtils.success();
	}


	@GetMapping(value = "/editMode")
	@RequestProperty(title = "회원수정", layout = "mypage")
	@Authorize("hasRole('ROLE_USER')")
	public String editMode(Model model) {
		List<UserSns> userSnsList = userSnsService.getUserSnsList(new UserSns(UserUtils.getUserId()));
		for(int i = 0; i < userSnsList.size(); i++){
			UserSns userSns = userSnsList.get(i);
			if (userSns != null && ObjectUtils.isEmpty(userSns.getCertifiedDate())) {
				return ViewUtils.redirect("/users/modify");
			}
		}
		userService.setMypageUserInfoForFront(model);

		model.addAttribute("user", UserUtils.getUser());
		model.addAttribute("wishlistGroups", wishlistGroups());
		return ViewUtils.getView("/users/editMode");
	}

	/**
	 * 회원정보 수정.
	 *
	 * @param model
	 * @param modifyResult
	 * @param userPassword
	 * @return
	 */
	@GetMapping(value = "/modify")
	@RequestProperty(title = "회원수정", layout = "mypage")
	@Authorize("hasRole('ROLE_USER')")
	public String getModify(Model model,
							@RequestParam(value = "modifyResult", required = false) String modifyResult,
							@RequestParam(value = "userPassword", required = false) String userPassword) {

		String redirectUrl = setModifyInfo(model, modifyResult, userPassword);
		if (StringUtils.isNotEmpty(redirectUrl)) {
			return ViewUtils.redirect(redirectUrl);
		}

		return ViewUtils.getView("/users/modify");
	}

	@PostMapping(value = "/modify")
	@RequestProperty(title = "회원수정", layout = "mypage")
	@Authorize("hasRole('ROLE_USER')")
	public String postModify(Model model,
							@RequestParam(value = "modifyResult", required = false) String modifyResult,
							@RequestParam(value = "userPassword", required = false) String userPassword) {

		String redirectUrl = setModifyInfo(model, modifyResult, userPassword);
		if (StringUtils.isNotEmpty(redirectUrl)) {
			return ViewUtils.redirect(redirectUrl);
		}

		return ViewUtils.getView("/users/modify");
	}

	public String setModifyInfo(Model model, String modifyResult, String userPassword) {
		userService.setMypageUserInfoForFront(model);
		User user = userService.getUserByUserId(UserUtils.getUser().getUserId());

		// sns로그인일 경우 비밀번호를 따로 입력받지 않도록 설정 2017-05-29_seungil.lee
		List<UserSns> userSnsList = userSnsService.getUserSnsList(new UserSns(UserUtils.getUserId()));
		if(userSnsList.isEmpty()){
			if (!(RedirectAttributeUtils.get("modify-complete") != null
					&& "Y".equals((String) RedirectAttributeUtils.get("modify-complete")))) {

				if (!"1".equals(modifyResult) || modifyResult == null) {
					//throw new UserException(MessageUtils.getMessage("M01472"));	// 정상적인 접근이 아닙니다.
					return "/users/editMode";
				}

				//ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);

				String password = passwordEncoder.encode(userPassword);

				if(!passwordEncoder.matches(userPassword, user.getPassword())){
					throw new UserException(MessageUtils.getMessage("M01473"));    // 비밀번호가 다릅니다.
				}
			}
		} else {
			for(int i = 0; i < userSnsList.size(); i++){
				UserSns userSns = userSnsList.get(i);
				if(!ObjectUtils.isEmpty(userSns.getCertifiedDate())){
					if (!(RedirectAttributeUtils.get("modify-complete") != null
							&& "Y".equals((String) RedirectAttributeUtils.get("modify-complete")))) {

						if (!"1".equals(modifyResult) || modifyResult == null) {
							//throw new UserException(MessageUtils.getMessage("M01472"));	// 정상적인 접근이 아닙니다.
							return "/users/editMode";
						}

						//ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);

						String password = passwordEncoder.encode(userPassword);

						if(!passwordEncoder.matches(userPassword, user.getPassword())){
							throw new UserException(MessageUtils.getMessage("M01473"));    // 비밀번호가 다릅니다.
						}
					}
				} else {
					model.addAttribute("userSns", userSns);
					model.addAttribute("isSnsLogin", true);
				}
			}
		}

		UserDetail userDetail = (UserDetail) user.getUserDetail();

		List<CodeInfo> telCodes = CodeUtils.getCodeInfoList("TEL");
		List<CodeInfo> phoneCodes = CodeUtils.getCodeInfoList("PHONE");

		String years = DateUtils.getToday("yyyy");

		model.addAttribute("years", years);
		model.addAttribute("telCodes", telCodes);
		model.addAttribute("phoneCodes", phoneCodes);
		model.addAttribute("user", user);

		model.addAttribute("wishlistGroups", wishlistGroups());

		return "";
	}

	@PostMapping("/modify-action")
	@RequestProperty(title = "회원수정", layout = "mypage")
	@Authorize("hasRole('ROLE_USER')")
	public String modifyAction(Model model, User user, UserDetail userDetail, @RequestParam(value = "isSnsLogin", required = false, defaultValue = "false") Boolean isSnsLogin,
							   HttpServletRequest request) {

		// SNS 계정 미인증일 경우 가입 불가 아이디 체크
		if (isSnsLogin) {
			String checkResult = userService.checkDuplication(user);

			if ("isOccupiedId".equals(checkResult)) {
				return ViewUtils.redirect("/users/entryForm?terms=1&privacy=1", "사용할수 없는 Email 입니다");
			}
		}

		userDetail.processHyphen();
		user.setUserDetail(userDetail);
		userService.updateFrontUserAndUserDetail(user);

		// SNS 계정 미인증일 경우 인증일 update
		if (isSnsLogin) {
			user.setPassword("");
			userSnsService.updateUser(user);

			// 복호화  (정보변경 시 화면에서 회원명 암호화로 출현 방지)
			user.decrypt(userEncryptor, ShopUtils.needMasking());

			UserUtils.getUser().setUserName(user.getUserName());
			UserUtils.getUser().setLoginId(user.getLoginId());
		}

		RedirectAttributeUtils.addAttribute("modify-complete", "Y");

		changeLogService.insertUserChangeLog(user.getUserId(), request);
		return ViewUtils.redirect("/users/editMode", MessageUtils.getMessage("M00289"));    // 수정되었습니다.
	}

	/**
	 * 회원 탈퇴
	 * @param model
	 * @return
	 */
	@GetMapping(value = "/secede")
	@RequestProperty(title = "회원탈퇴", layout = "mypage")
	@Authorize("hasRole('ROLE_USER')")
	public String secede(Model model) {

		userService.setMypageUserInfoForFront(model);

		// SNS 미인증 사용자인지 확인
		List<UserSns> userSnsList = userSnsService.getUserSnsList(new UserSns(UserUtils.getUserId()));
		if (userSnsList != null && !userSnsList.isEmpty()) {
			for (UserSns userSns : userSnsList) {
				if (ObjectUtils.isEmpty(userSns.getCertifiedDate())) {
					model.addAttribute("isSnsLogin", true);
					break;
				}
			}
		}

		model.addAttribute("user", UserUtils.getUser());
		model.addAttribute("wishlistGroups", wishlistGroups());
		return ViewUtils.getView("/users/secede");
	}

	/**
	 * 회원 탈퇴 처리
	 * @param userDetail
	 * @param userPassword
	 * @return
	 */
	@PostMapping("/secede")
	@RequestProperty(title = "회원탈퇴", layout = "mypage")
	@Authorize("hasRole('ROLE_USER')")
	public String secedeAction(UserDetail userDetail,
		@RequestParam(value = "userPassword", required = false) String userPassword) {

		User user = userService.getUserByUserId(UserUtils.getUser().getUserId());

		boolean isCheck = true;
		List<UserSns> userSnsList = userSnsService.getUserSnsList(new UserSns(UserUtils.getUserId()));
		if (userSnsList != null && !userSnsList.isEmpty()){
			for (UserSns userSns : userSnsList){
				// SNS 미인증 사용자의 경우 비밀번호 체크하지 않음
				if (ObjectUtils.isEmpty(userSns.getCertifiedDate())) {
					isCheck = false;
					break;
				}
			}
		}

		if (isCheck && !passwordEncoder.matches(userPassword, user.getPassword())) {
			throw new UserException(MessageUtils.getMessage("M01473"));
		}

		user.setLeaveDate(DateUtils.getToday());
		user.setStatusCode("3");

		userDetail.setUserId(user.getUserId());
		userDetail.setUseFlag("N");
		userDetail.processHyphen();
		user.setUserDetail(userDetail);

		userService.updateSecedeFrontUserAndUserDetail(user);

		return ViewUtils.redirect("/op_security_logout", MessageUtils.getMessage("M01474"));    // 회원탈퇴 처리되었습니다.
	}


	/**
	 * 마이페이지 접속 시 공통 모델 바인딩. (관심상품 그룹)
	 *
	 * @return
	 */
	public List<WishlistGroup> wishlistGroups() {
		List<WishlistGroup> wishlistGroups = new ArrayList<>();

		return wishlistGroups;
	}

	/**
	 * 휴면 계정 복구 화면
	 *
	 * @return
	 */
	@GetMapping("sleep-user")
	public String sleepUser() {

		if (!UserUtils.isUserLogin()) {
			throw new UserException("잘못된 접근입니다.");
		}

		if (!"4".equals(UserUtils.getUser().getStatusCode())) {
			throw new UserException("휴면 회원이 아닙니다.");
		}

		return ViewUtils.getView("/users/sleep-user");
	}

	/**
	 * 휴면 계정 복구
	 *
	 * @return
	 */
	@GetMapping("wakeup-user")
	public JsonView wakeupUser() {

		if (!UserUtils.isUserLogin()) {
			return JsonViewUtils.failure("휴면 회원이 아닙니다.");
		}

		if (!"4".equals(UserUtils.getUser().getStatusCode())) {
			return JsonViewUtils.failure("휴면 회원이 아닙니다.");
		}

		try {
			userService.wakeupUser(UserUtils.getUser());
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "======== wakeupUser RuntimeException =========");
			return JsonViewUtils.failure("휴면계정 해지 실패 고객센터에 문의 하세요.");
		}

		SecurityContextHolder.getContext().setAuthentication(null);
		return JsonViewUtils.success();
	}

	@GetMapping("/change-password")
	public String changePassword(Model model) {

		return ViewUtils.view();
	}

	@PostMapping("/change-password")
	public String changePasswordAction(HttpServletRequest request,
									   @RequestParam("password") String password,
									   @RequestParam("changePassword") String changePassword) {

		if (!UserUtils.isUserLogin()) {
			log.error("로그인이 안되어 있습니다.");
			return ViewUtils.redirect("/op_security_logout?target=/");
		}

		String message = "";
		String redirectUrl = "";
		try {
			if (UserUtils.getUserId() != 0) {
				userService.updatePasswordForUser(UserUtils.getUserId(),password,changePassword);
			}
			changeLogService.insertUserChangeLog(UserUtils.getUserId(), request);
			message = "비밀번호가 변경되었습니다.";
			redirectUrl = "/op_security_logout?target=/";
		} catch (UserException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "======= changePasswordAction UserException ========");
			//message = e.getMessage();
			message = "비밀번호 변경에 실패했습니다.";
			redirectUrl = "/users/change-password";
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "========= changePasswordAction RuntimeException ==========");
			message = "비밀번호 변경에 실패했습니다.";
			redirectUrl = "/users/change-password";
		}

		return ViewUtils.redirect(redirectUrl,message);
	}

	@PostMapping("/delay-change-password")
	public JsonView delayChangePassword(RequestContext requestContext) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {

			if (!UserUtils.isUserLogin()) {
				return JsonViewUtils.failure("로그인이 안되어 있습니다.");
			}
			userService.updatePasswordExpiredDateForUser(UserUtils.getUserId());
			return JsonViewUtils.success("비밀번호 유효기간이 연장되었습니다.");
		} catch (UserException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "=========== delayChangePassword UserException ============");
			return JsonViewUtils.failure("비밀번호관련 정보가 변경에 실패 했습니다.");
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "=========== delayChangePassword RuntimeException ===========");
			return JsonViewUtils.failure("비밀번호관련 정보가 변경에 실패 했습니다.");
		}
	}
}
