package saleson.shop.seller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.isms.ConfigIsmsService;
import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.security.token.TokenService;
import com.onlinepowers.framework.security.token.domain.Token;
//import com.onlinepowers.framework.security.userdetails.OpUserDetails;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.privacy.pCrypto;

import saleson.common.Const;
import saleson.common.security.crypto.RsaCryptor;
import saleson.common.utils.LocalDateUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.PkiSellerLogin;
import saleson.seller.main.domain.Seller;
import saleson.seller.user.SellerUserService;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.email.EmailService;
import saleson.shop.email.domain.Email;
import saleson.shop.email.domain.EmailSend;
import saleson.shop.email.ems.EmsMapper;
import saleson.shop.email.support.EmailDetailParam;
import saleson.shop.email.support.SendParam;
import saleson.shop.item.ItemService;
import saleson.shop.item.support.ItemParam;
import saleson.shop.log.LoginLogService;
import saleson.shop.log.support.LoginLogParam;
//import saleson.shop.notice.NoticeService;
import saleson.shop.notice.support.NoticeParam;
import saleson.shop.qna.QnaService;
import saleson.shop.qna.support.QnaParam;
import saleson.shop.seller.common.ResponseData;
import saleson.shop.shadowlogin.ShadowLoginLogService;
import saleson.shop.user.domain.SellerUser;
import saleson.shop.user.support.OpManagerParam;

@Controller
@RequestProperty(template="seller", layout="default")
public class SellerController {

	private static final Logger log = LoggerFactory.getLogger(SellerController.class);

	@Value("${saleson.url.shoppingmall}")
    private String serverDomain;

	@Autowired
	private SellerService sellerService;

//	@Autowired
//	private NoticeService notiveService;

	@Autowired
	private QnaService qnaService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private ShadowLoginLogService shadowLoginLogService;

	@Autowired
 	private EmailService emailService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SellerUserService sellerUserService;

	@Autowired
	@Qualifier("smsTokenService")
	TokenService smsTokenService;

	@Autowired
	private ConfigIsmsService configIsmsService;

	@Autowired
	private CodeService codeService;

	@Autowired
	private EmsMapper emsMapper;

	@Value("${pkiSellerLoginDate}")
	private String pkiSellerLoginDate;

	@Value("${salt.adminPwd}")
	private String adminPwd;




	/**
	 * 판매관리자 메인.
	 * @return
	 */
	@GetMapping({"/seller", "/seller/index"})
	public String index(Model model) {

		long sellerId = SellerUtils.getSellerId();

		NoticeParam noticeParam = new NoticeParam();
		noticeParam.setVisibleType(3);
		noticeParam.setLimit(1);
		noticeParam.setSellerId(sellerId);

		QnaParam qnaParam = new QnaParam();
		qnaParam.setSellerId(sellerId);
		qnaParam.setLimit(5);
		qnaParam.setAnswerCount(2);

		ItemParam itemParam = new ItemParam();
		itemParam.setReviewDisplayFlag("N");
		itemParam.setSellerId(sellerId);
		itemParam.setConditionType("SELLER");
		itemParam.setLimit(5);

		// 이상우 [2017-05-15 추가] 배송,교환,반품 지연 설정일 - 추후 shopConfig에서 가져오는 방향으로 설정
		HashMap<String, Integer> map = new HashMap<>();
		map.put("shippingDelay", 1);
		map.put("exchangeDelay", 3);
		map.put("returnDelay", 3);

		model.addAttribute("delayDays", map);
		model.addAttribute("main", "main");
//		model.addAttribute("noticeList", notiveService.getNoticeList(noticeParam));			// ajax 로 변경됨
		model.addAttribute("qnaList", qnaService.getQnaListByParam(qnaParam));
		model.addAttribute("qnaCount", qnaService.getQnaListCountByParam(qnaParam));
		model.addAttribute("reviewCount", itemService.getItemReviewCountByParam(itemParam));
		model.addAttribute("sellerReviewList", itemService.getItemReviewListByParam(itemParam));

		// 2026-06-05 인천행정체제 개편으로 인한 중구동구서구 관리자 메시지 표시
//		String todatetime = LocalDateUtils.localDateTimeToString(LocalDateTime.now(), Const.DATETIME_FORMAT);
//		if(null != todatetime && todatetime.compareTo("20260630235959") <= 0) {	// 26년 6월 30일 23시 59분 59초
//			if(null != SellerUtils.getSeller().getLocgovCode() && !"".equals(SellerUtils.getSeller().getLocgovCode())) {
//				List<String> incheonPopupCode = new ArrayList<String>(List.of("28110", "28140", "28260"));
//
//				if(incheonPopupCode.contains(SellerUtils.getSeller().getLocgovCode())) {
//					// 로그인한 사용자가 인천 중구동구서구 답례품제공자인 경우
//					model.addAttribute("showIncheonPopup", "Y");
//				}
//
//			}
//		}

		CodeParam codeParam = new CodeParam();
		codeParam.setId("period2");
		codeParam.setDetail("9991"); // 엑셀암호화
		Code popAdmin2 = codeService.getCodePopAdmin(codeParam);
		if (null != popAdmin2 && !"".equals(popAdmin2.getCodeType())) {
			model.addAttribute("showPopup2", "Y");
		}

		return "view:/main/index";
	}


	/**
	 * 판매자 정보 수정.
	 * @return
	 */
	@GetMapping("/seller/edit")
	public String edit(Model model) {

		Seller seller = sellerService.getSellerById(SellerUtils.getSellerId());

		// 국번이 없는 경우 Default 값 추가
		seller.parseTelephoneNumber();

		model.addAttribute("telCodes", CodeUtils.getCodeList("TEL"));
		model.addAttribute("phoneCodes", CodeUtils.getCodeList("PHONE"));
		model.addAttribute("seller", seller);
		model.addAttribute("isShadowSellerLogin", SellerUtils.isShadowSellerLogin());
		return "view:/user/edit";
	}

	/**
	 * 판매자 정보 수정처리.
	 * @return
	 */
	@PostMapping("/seller/edit")
	public String editAction(Model model, Seller seller) {

		sellerService.updateSeller(seller);
		sellerService.updateSellerMinimall(seller);
		return ViewUtils.redirect("/seller/edit", MessageUtils.getMessage("M00289"));	// 수정되었습니다.
	}

	/**
	 * 비밀번호 수정.
	 * @return
	 */
	@GetMapping("/seller/edit/password-change-popup")
	@RequestProperty(template="seller", layout="base")
	public String passwordChangePopup(Model model) {

		model.addAttribute("sellerId", SellerUtils.getSellerId());
		return "view:/user/passwordPopup";
	}


	/**
	 * 비밀번호 수정처리.
	 * @return
	 */
	@PostMapping("/seller/edit/password-change")
	public String passwordChangeAction(Model model, Seller seller) {
		seller.setSellerId(SellerUtils.getSellerId());
		seller.setUpdatedUserId(SellerUtils.getSellerId());
		sellerService.updateSellerPassword(seller);
		return ViewUtils.redirect("/seller/edit/password-change-popup", MessageUtils.getMessage("M00289"), "self.close();");
	}


	/**
	 * 판매자 정보 수정.
	 * @return
	 */
	@GetMapping("/seller/edit-minimall")
	public String editMinimall(Model model) {

		Seller seller = sellerService.getSellerById(SellerUtils.getSellerId());

		model.addAttribute("seller", seller);
		return "view:/user/edit-minimall";
	}

	/**
	 * 판매자 정보 수정처리.
	 * @return
	 */
	@PostMapping("/seller/edit-minimall")
	public String editMinimall(Model model, Seller seller) {

		sellerService.updateSellerMinimall(seller);

		return ViewUtils.redirect("/seller/edit-minimall", MessageUtils.getMessage("M00289"));	// 수정되었습니다.
	}


	@GetMapping("/seller/login")
	@RequestProperty(layout="base")
	public String login(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("mberDn");
		session.removeAttribute("mberFinDn");
		session.removeAttribute("SELLER");
		session.removeAttribute("chgPwdLoginId");
		session.removeAttribute("pkiCheckLoginId");

		/*
		 * Object princial =
		 * SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 * if(princial instanceof UserDetails) { return
		 * ViewUtils.redirect("/seller/index"); }
		 */

		return "view:/user/login";
	}

	@GetMapping("/seller/login-main")
	@RequestProperty(layout="base")
	public String loginMain(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("mberDn");
		session.removeAttribute("mberFinDn");
		session.removeAttribute("SELLER");
		session.removeAttribute("chgPwdLoginId");
		session.removeAttribute("pkiCheckLoginId");
		HashMap<String, String> encKey = RsaCryptor.createKeypairAsString();

		if (encKey == null) {
    		throw new UserException("암복호화 키 생성에 문제가 발생했습니다.");
		}

		session.setAttribute("privateKeyStr", encKey.get("privateKeyStr"));
		session.setAttribute("publicKeyStr", encKey.get("publicKeyStr"));

		LocalDate nowDate = LocalDate.now();
		LocalDate pkiLoginDate = LocalDate.parse(pkiSellerLoginDate, DateTimeFormatter.ofPattern("yyyyMMdd"));		// 인증서 로그인 필수 적용 날짜 2023.08.01.

		boolean setIdLogin = nowDate.isBefore(pkiLoginDate);

		model.addAttribute("setIdLogin", setIdLogin);
		model.addAttribute("publicKeyStr", encKey.get("publicKeyStr"));

		return "view:/user/login_main";
	}

	@PostMapping("/seller/login")
	@RequestProperty(layout="base")
	public String loginProcess(Seller loginInfo,
			@RequestParam(value="target", defaultValue="/seller") String target,
			HttpSession session) {

		if (ObjectUtils.isEmpty(loginInfo.getLoginId())
				|| ObjectUtils.isEmpty(loginInfo.getPassword())) {

			RequestContextUtils.setMessage("아이디/비밀번호를 입력해 주세요.");

			insertLoginLog(false);

			return "view:/user/login";
		}

		try {
			String encLogindId = pCrypto.Encrypt("normal", loginInfo.getLoginId(), "");
			String encPwd = pCrypto.Encrypt("hash.5", loginInfo.getPassword(), "");

			long userId = sellerUserService.getSellerUserIdByLoginId(encLogindId);		// 암호화 로그인 아이디로 찾기
			if (userId == 0) {
				userId = sellerUserService.getSellerUserIdByLoginId(loginInfo.getLoginId());		// 로그인 아이디로 찾기
			}
			Seller seller;
			try {
				seller = sellerService.getSellerByLoginId(encLogindId);		// 암호화 로그인 아이디로 찾기
				if (seller == null || seller.getSellerId() <= 0) {
					seller = sellerService.getSellerByLoginId(loginInfo.getLoginId());		// 로그인 아이디로 찾기
				}
				if (seller == null || seller.getSellerId() <= 0) {
					throw new RuntimeException();
				}
			} catch (RuntimeException e) {
				throw new UnsupportedEncodingException();
			}

			User user = sellerUserService.getSellerUserById(seller.getSellerId(), userId);

			if (!passwordEncoder.matches(encPwd, user.getPassword())
					|| "3".equals(seller.getStatusCode())
					|| "4".equals(seller.getStatusCode())) {
				RequestContextUtils.setMessage("아이디/비밀번호가 일치하지 않습니다.");

				insertLoginLog(false);

				return "view:/user/login";
			}

			if ("1".equals(seller.getStatusCode())) {
				RequestContextUtils.setMessage("판매자 승인 대기 중입니다.");

				insertLoginLog(false);

				return "view:/user/login";
			}

			session.setAttribute("SELLER", seller);
			insertLoginLog(true);
			sellerUserService.saveLoginSession(session, UserUtils.getSellerUserId());
			if (target != null && (target.contains("excel")
									|| target.contains("download")
									|| target.contains("file")
									)
				) {			// 다운로드 일 경우 메인화면 으로 이동
				target = "";
			}
			return "redirect:" + target;
		} catch (UnsupportedEncodingException e) {
			RequestContextUtils.setMessage("아이디/비밀번호가 일치하지 않습니다.");
			insertLoginLog(false);
			return "view:/user/login";
		}
	}

	@GetMapping("/seller/logout")
	@RequestProperty(layout="base")
	public String logout(HttpSession session) {
		session.removeAttribute("SELLER");
		session.removeAttribute("SHADOW_SELLER");

		return "redirect:/seller/login";
	}

	@PostMapping("/seller/shadow-logout")
	@RequestProperty(layout="base")
	public String shadowLogout(HttpSession session) {

		try {
			Seller seller = SellerUtils.getShadowSeller();
			if (seller != null) {
				shadowLoginLogService.updateShadowLogoutLog(seller.getShadowLoginLogId());
			}
		} catch (RuntimeException e) {
//			log.error("shadowLoginLogService.updateShadowLogoutLog(..) : {}", e.getMessage(), e);
			log.error("shadowLoginLogService.updateShadowLogoutLog(..) : {}", "========= shadowLogout RuntimeException ==========", e);
		}

		session.removeAttribute("SELLER");
		session.removeAttribute("SHADOW_SELLER");

		return "redirect:/opmanager/seller/list";
	}

	private void insertLoginLog(boolean isSuccess) {
		loginLogService.insertLoginLogBySeller(RequestContextUtils.getRequestContext().getRequest(), isSuccess);
	}

	@GetMapping(value = "/seller/login-lock")
	@RequestProperty(title="접속제한", layout="base")
	public String loginLock (HttpSession session) {
		return "view:/user/login-lock";
	}

	@PostMapping("/seller/login-lock")
	public String loginLockAction (@RequestParam("requestToken") String requestToken,
								   @RequestParam("loginId") String loginId,
								   @RequestParam("phoneNumber") String phoneNumber,
								   @RequestParam("smsAuth") String smsAuth) {

		Token token = new Token();

		token.setRequestToken(requestToken);
		token.setAccessToken(smsAuth);
		token.setRequestType("SMS");

		if(!smsTokenService.isValidToken(token)) {
			return ViewUtils.redirect("/seller/login-lock","SMS 인증에 실패 했습니다.");
		}

		User user = securityService.getSellerUserByLoginId(loginId);

		if (ValidationUtils.isNull(user)) {
			return ViewUtils.redirect("/seller/login-lock","계정이 존재하지 않습니다.");
		}

		if (!phoneNumber.equals(user.getPhoneNumber())) {
			return ViewUtils.redirect("/seller/login-lock","계정이 존재하지 않습니다.");
		}

		sellerUserService.updateTempPasswordForSellerUser(user.getUserId());
		securityService.updateClearLoginFailCountForSellerUser(loginId);

		return ViewUtils.redirect("/seller/login");
	}

    @GetMapping("/seller/login-disconnect")
    @RequestProperty(title="세션종료")
    public String disconnectSession() {
        return ViewUtils.redirect("/seller/login?target=/seller&error=98");
    }

    @PostMapping("/seller/login-user-email")
    @ResponseBody
    public JsonView selectLoginUserEmail(HttpServletRequest request, HttpServletResponse response) {


		//////////////////
		// srhan. DB에 저장된 로그인 시도하는 사람의 이메일을 불러온다.
		// 1. 이메일이 있으면 메일 보내고
		// 2. 없으면 메일 입력하는 UI 보이기
		//////////////////

    	String encId = request.getParameter("loginId");
    	String encPwd = request.getParameter("pwd");

    	HttpSession session = request.getSession();

    	ResponseData data = new ResponseData();

    	/*	// srhan. 20251022. 로그인 버튼에서 아래의 코드로 바로 로그인 성공 페이지로 넘겨주는 듯 하다.
		Object princial = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (princial instanceof UserDetails) {
    		data.setCode("LOGIN_STATE");
			return JsonViewUtils.success(data);
		}
		*/

    	String privateKeyStr = "";
    	String publicKeyStr = "";
    	try {

    		if(session.getAttribute("privateKeyStr") != null) {
    			privateKeyStr = session.getAttribute("privateKeyStr").toString();
    		}

    		if(session.getAttribute("publicKeyStr") != null) {
    			publicKeyStr = session.getAttribute("publicKeyStr").toString();
    		}

        	if (!StringUtils.hasLength(privateKeyStr) || !StringUtils.hasLength(publicKeyStr)) {
        		throw new NullPointerException();
        	}
    	} catch (NullPointerException e) {		// 세션 오래되어 초기화 될 경우
    		HashMap<String, String> encKey = RsaCryptor.createKeypairAsString();

    		if (encKey == null) {
    			throw new UserException("암복호화 키 생성에 문제가 발생했습니다.");
    		}

    		session.setAttribute("privateKeyStr", encKey.get("privateKeyStr"));
    		session.setAttribute("publicKeyStr", encKey.get("publicKeyStr"));

    		publicKeyStr = encKey.get("publicKeyStr");

    		data.setPublicKey(publicKeyStr);
    		data.setCode("INIT_KEY");

    		return JsonViewUtils.success(data);
    	}

    	String id = encId;
    	String pwd = encPwd;

    	SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdAndPwd(id, pwd);

    	if(StringUtils.isEmpty(sellerUser.getEmail())) {
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
		    	userParam.setLoginId(id);		// 암호화 전
		    	//사무실 내부 인증번호는 '250324'로 고정
		    	if(serverDomain.contains("localhost")) {
		    		userParam.setAuthNum("250324");
		    	}
		    	//userParam.setLoginId(encId);		// 복호화 전
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
				emailSend.setEmail(sellerUser.getEmail());			// hanmail.net			3분 40초
				//emailSend.setEmail("gkstofhs@hanmail.net");			// hanmail.net	2분
//				emailSend.setEmail("srhan@u-cube.kr");		// 저장된 다음메일이 느려서 회사메일로 변경하여 테스트
				//emailSend.setEmail("keykjy@gmail.com");		// 저장된 다음메일이 느려서 회사메일로 변경하여 테스트
//				emailSend.setUserName(sellerUser.getUserName());
				list.add(emailSend);

				param.setSendUserList(list);
				boolean bRetval = emailService.sendEmail(param);

				// 이메일 DB에 접근해서 sendDate를 이전 시간으로 변경하여 현재 발송중인 대량메일보다 우선적으로 발송하게 한다.
//				long emailId = result.getEmailId();
//				EmailDetailParam emailParam = new EmailDetailParam();
//				emailParam.setEmailId(emailId);
//				emsMapper.updateEmsSendDate(emailParam);

				if(bRetval) {
					return JsonViewUtils.success("SUCC" + sellerUser.getEmail());
				}else {
					return JsonViewUtils.success("FAIL");
				}
			} catch (OpRuntimeException e) {
				log.error("■■■LOGIN■■■ loginUserEmail RuntimeException {}", e);
				return JsonViewUtils.success("FAIL");
			}
    	}

    }

    @PostMapping("/seller/login-user-auth-check")
    @ResponseBody
    public JsonView loginUserAuthCheck(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {



    	String loginId = request.getParameter("loginId");
    	String authNum = request.getParameter("authNum");
    	String opEmail = request.getParameter("opEmail");		// 이메일이 없을 때는 받아서 update 해야 함
    	String pwd = request.getParameter("password");

    	HttpSession session = request.getSession();

    	LoginLogParam param = new LoginLogParam();
		param.setLoginId(loginId);
		param.setAuthNum(authNum);
		boolean authChk = loginLogService.getEmailAuthChk(param);
		if(authChk) {
			// op_user 테이블의 email 등록
			User user = new User();
			user.setLoginId(loginId);
			if(StringUtils.isNotEmpty(opEmail)) {		// 파라미터로 넘어온 이메일이 있다면, 셀러 DB에 등록되어 있지 않은 것. 등록해주자.
				long userId = sellerUserService.getSellerUserIdByLoginId(loginId);
				user.setUserId(userId);
				user.setEmail(opEmail);
				sellerUserService.updateSellerUser(user);
			}

			// 로그인 로직
			SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdAndPwd(loginId, pwd);
			//sellerUser.setLoginId(loginId);
    		Seller seller = sellerService.getSellerByLoginId(sellerUser.getLoginId());
    		LocalDate nowDate = LocalDate.now();
    		LocalDate expiredDate = LocalDate.parse(sellerUser.getPasswordExpiredDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));

			if (!nowDate.isBefore(expiredDate)) {		// 비밀번호 만료일 또는 이후일 경우
        		return JsonViewUtils.success("EXPIRED_PWD");
			}



            sellerUserService.loginProcess(sellerUser, seller, request);		// srhan. 202510230810. 실제로 로그인 처리하는 로직
	    	return JsonViewUtils.success("SUCC");
		}else {
			return JsonViewUtils.success("FAIL");		// "FAIL"
		}
    }

	@PostMapping("/seller/login-user-email-save")
	public JsonView loginUserEmailSave(HttpServletRequest request, HttpServletResponse response, Model model) throws UnsupportedEncodingException {

    	String loginId = request.getParameter("loginId");
    	String opEmail = request.getParameter("opEmail");		// 이메일이 없을 때는 받아서 update 해야 함

    	// 판매자 이메일 update
		Seller seller = new Seller();
		if (loginId == null || loginId.isEmpty() || opEmail == null || opEmail.isEmpty()) {
			return JsonViewUtils.success("FAIL");		// "FAIL"
		}
		seller.setLoginId(loginId);
		seller.setEmail(pCrypto.Encrypt("normal", opEmail, ""));
		int successUpdateSeller			= sellerService.updateSellerInfo(seller);			// op_seller		이메일 등록
		int successUpdateSellerUser		= sellerUserService.updateSellerUserInfo(seller);	// op_seller_user	이메일 등록

		if (successUpdateSeller > 0 && successUpdateSellerUser > 0) {
			// 등록된 이메일로 인증번호 생성하여 발송
			Email result = null;
			try {
				Email email = new Email();

				email.setAuthTarget("L");		// srhan. Login email send
		    	email.setFrstRegisterId(Long.valueOf(0));
		    	email.setSubject("답례품제공자 로그인 인증번호 발송");

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
				emailSend.setEmail(opEmail);
				list.add(emailSend);

				sendParam.setSendUserList(list);
				emailService.sendEmail(sendParam);
			} catch (OpRuntimeException e) {
				log.error("■■■LOGIN■■■ loginUserEmail RuntimeException {}", e);
			}

			return JsonViewUtils.success("SUCC");		// "SUCCESS"
		} else {
			return JsonViewUtils.success("FAIL");		// "FAIL"
		}
	}

    @PostMapping("/seller/login-seller")
    @ResponseBody
    public JsonView sellerLoginNew(HttpServletRequest request, HttpServletResponse response) {
    	if ("adminLogin".equals(request.getParameter("type"))) {
    		return sellerLoginNewAdmin(request, response);
    	}

    	String encId = request.getParameter("loginId");
    	String encPwd = request.getParameter("pwd");

    	HttpSession session = request.getSession();

    	ResponseData data = new ResponseData();

		/* 251217 페이지에러제거 테스트
		 * Object princial =
		 * SecurityContextHolder.getContext().getAuthentication().getPrincipal(); if
		 * (princial instanceof UserDetails) { data.setCode("LOGIN_STATE"); return
		 * JsonViewUtils.success(data); }
		 */

    	String privateKeyStr = "";
    	String publicKeyStr = "";
    	try {

    		if(session.getAttribute("privateKeyStr") != null) {
    			privateKeyStr = session.getAttribute("privateKeyStr").toString();
    		}

    		if(session.getAttribute("publicKeyStr") != null) {
    			publicKeyStr = session.getAttribute("publicKeyStr").toString();
    		}

        	if (!StringUtils.hasLength(privateKeyStr) || !StringUtils.hasLength(publicKeyStr)) {
        		throw new NullPointerException();
        	}
    	} catch (NullPointerException e) {		// 세션 오래되어 초기화 될 경우
    		HashMap<String, String> encKey = RsaCryptor.createKeypairAsString();

    		if (encKey == null) {
    			throw new UserException("암복호화 키 생성에 문제가 발생했습니다.");
    		}

    		session.setAttribute("privateKeyStr", encKey.get("privateKeyStr"));
    		session.setAttribute("publicKeyStr", encKey.get("publicKeyStr"));

    		publicKeyStr = encKey.get("publicKeyStr");

    		data.setPublicKey(publicKeyStr);
    		data.setCode("INIT_KEY");

    		return JsonViewUtils.success(data);
    	}

		/* 251217 페이지에러제거 테스트
		 * String id = RsaCryptor.decrypt(encId, privateKeyStr); String pwd =
		 * RsaCryptor.decrypt(encPwd, privateKeyStr);
		 * 관리자 로그인 로그 확인하면 ID 값이 Null 인것을 확인
		 * ID 또는 PWD 에서 복호화 ERROR 발생시 ID : Null 또는 PWD: Null로 처리됨
		 * 제공자 조회시 ID, PWD 둘중 하나만 Null이여도 제공자 조회 못함
		 */

    	String id = encId;
    	String pwd = encPwd;

    	session.setAttribute("chgPwdLoginId", id);

    	SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdAndPwd(id, pwd);

    	if (sellerUser == null) {			// 로그인 정보 없음
    		String failureUrl = "";
            String loginType = "ROLE_SELLER";

    		failureUrl = UserUtils.resolveFailureUrl(failureUrl, loginType);

    		sellerUserService.updateLoginFailCnt(id);

//        	loginLogService.insertLoginLogBySeller(request, false);
        	loginLogService.insertLoginLogBySeller2(request, id, false, "");
        	//loginLogService.insertLoginLogBySeller2(request, id, false, pwd);			// 로그인 실패시 비밀번호 확인 체크용으로 추가

            return JsonViewUtils.failure("답례품제공자 정보가 존재하지 않아 로그인할 수 없습니다.");
    	} else {
			JsonView result = JsonViewUtils.success();

			long pwdFailCnt = 5;
			try {
				pwdFailCnt = Long.valueOf(configIsmsService.getIsmsConfigValueByKey("FAIL_PASSWORD_COUNT"));
			} catch (NumberFormatException e) {
				log.error(getClass().getName() + "sellerLoginNew pwdFailCnt error :: ", e);
			}

    		LocalDate nowDate = LocalDate.now();
    		LocalDate expiredDate = LocalDate.parse(sellerUser.getPasswordExpiredDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
			LocalDate pkiLoginDate = LocalDate.parse(pkiSellerLoginDate, DateTimeFormatter.ofPattern("yyyyMMdd"));		// 인증서 로그인 필수 적용 날짜 2023.08.01.
    		if (!"N".equalsIgnoreCase(sellerUser.getPwdChgSellerYn())) {			// 비밀번호 변경해야함
        		data.setCode("INIT_PWD");
    		} else if (sellerUser.getLoginFailCount() >= pwdFailCnt) {		// 비밀번호 5회 이상 틀린 이력 있을 경우
        		data.setCode("FAIL_CNT");
        		data.setErrMsg("비밀번호가 " + pwdFailCnt + "회 이상 틀린 이력이 있습니다.\n지자체 담당자에게 비밀번호 초기화 요청 후\n재시도 해주세요.");
    		} else if (!nowDate.isBefore(expiredDate)) {		// 비밀번호 만료일 또는 이후일 경우
        		data.setCode("EXPIRED_PWD");
//    		} else if(sellerUser.getLoginCount() == 0) {		// 최초 로그인일 경우 로그인 처리
//        		data.setCode("INIT_LOGIN");
//
//        		sellerUser.setLoginId(id);
//        		sellerUser.setPassword(pwd);
//
//        		Seller seller = sellerService.getSellerByLoginId(sellerUser.getLoginId());
//
//                sellerUserService.loginProcess(sellerUser, seller, request);
    		} else if (!StringUtils.hasLength(sellerUser.getMberDn()) && !StringUtils.hasLength(sellerUser.getMberFinDn())) {		// 인증서 없을경우
    			if (nowDate.isBefore(pkiLoginDate)) {		// 인증서 미적용
            		if (!"N".equalsIgnoreCase(sellerUser.getPwdChgSellerYn())) {			// 비밀번호 변경해야함
                		data.setCode("INIT_PWD");
            		} else if (sellerUser.getLoginFailCount() >= pwdFailCnt) {		// 비밀번호 5회 이상 틀린 이력 있을 경우
                		data.setCode("FAIL_CNT");
                		data.setErrMsg("비밀번호가 " + pwdFailCnt + "회 이상 틀린 이력이 있습니다.\n지자체 담당자에게 비밀번호 초기화 요청 후\n재시도 해주세요.");
            		} else if (!nowDate.isBefore(expiredDate)) {		// 비밀번호 만료일 또는 이후일 경우
                		data.setCode("EXPIRED_PWD");
            		} else {
                		data.setCode("SUCC");
                		sellerUser.setLoginId(id);
                		sellerUser.setPassword(pwd);
                	   //Seller seller = sellerService.getSellerByLoginId(sellerUser.getLoginId());
                       // sellerUserService.loginProcess(sellerUser, seller, request);		// srhan. 202510230810. 실제로 로그인 처리하는 로직. 그래서 인증번호 입력 안해도 새 탭에서는 로그인 된 상태로 나옴.
            		}
    			} else {		// 인증서 적용
            		data.setCode("NONE_DN");
            		session.setAttribute("pkiCheckLoginId", id);
    			}
    		} else if ("3".equals(sellerUser.getStatusCode())) {		// 영업 중지
    			return JsonViewUtils.failure("입력하신 아이디는 권한 중지 상태입니다. 지자체 담당자에게 문의해주세요.");
    		} else {		// 아이디/비밀번호 일치, 인증서 로그인 진행
    			if (nowDate.isBefore(pkiLoginDate)) {		// 인증서 미적용
            		data.setCode("SUCC");
            		sellerUser.setLoginId(id);
            		sellerUser.setPassword(pwd);
            		//Seller seller = sellerService.getSellerByLoginId(sellerUser.getLoginId());
                    //sellerUserService.loginProcess(sellerUser, seller, request);
    			} else {		// 인증서 적용
            		session.setAttribute("pkiCheckLoginId", id);		// 같은 인증서 아이디 여러개일 경우 필터링용
    			}
    		}
    		result.setData(data);

    		session.setAttribute("OP_LAST_USERNAME", id);

    		return result;
    	}
    }

	// 비밀번호 변경
    @GetMapping("/seller/user/pki/form")
	@RequestProperty(layout="base")
    public String sellerLoginNew(HttpServletRequest request, Model model) {
    	HttpSession session = request.getSession();
//    	String privateKeyStr = "";
    	String publicKeyStr = "";
    	try {
//        	privateKeyStr = session.getAttribute("privateKeyStr").toString();
        	publicKeyStr = session.getAttribute("publicKeyStr").toString();
    	} catch (NullPointerException e) {		// 세션 오래되어 초기화 될 경우
    		return ViewUtils.redirect("/seller/login", "인증 정보가 만료되었습니다.");
    	}
    	model.addAttribute("publicKeyStr", publicKeyStr);
        return ViewUtils.getView("seller/user/pki/form");
    }

	// 초기비밀번호/비밀번호만료 시 비밀번호 변경 화면 호출
	@GetMapping("/seller/change-password")
	@RequestProperty(title="비밀번호 변경", layout="base")
	public String changePasswordNew(Model model, HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();

    	try {
//        	String privateKeyStr = session.getAttribute("privateKeyStr").toString();
        	String publicKeyStr = session.getAttribute("publicKeyStr").toString();
        	String loginId = "";
    		String loginIdParam = session.getAttribute("chgPwdLoginId").toString();
        	if (session.getAttribute("mberDn") != null) {
        		String mberDn = session.getAttribute("mberDn").toString();
        		PkiSellerLogin param = new PkiSellerLogin();
        		param.setMberDn(mberDn);
        		List<SellerUser> searchSellerUsers = sellerUserService.getSellerUserByMberDn(param);
        		if (searchSellerUsers == null || searchSellerUsers.isEmpty()) {
        			return ViewUtils.redirect("/seller/login", "일치하는 사용자 정보가 없습니다.");
        		} else {
        			boolean hasLoginId = false;
        			for (SellerUser sellerUser : searchSellerUsers) {
						if (loginIdParam.equalsIgnoreCase(sellerUser.getLoginId())) {
							hasLoginId = true;
							loginId = sellerUser.getLoginId();
							break;
						}
					}
        			if (!hasLoginId) {
        				return ViewUtils.redirect("/seller/login", "일치하는 사용자 정보가 없습니다.");
        			}
        		}
//        		loginId = searchSellerUser.getLoginId();
        		session.setAttribute("chgPwdLoginId", loginId);
        	} else if (session.getAttribute("mberFinDn") != null) {
        		String mberFinDn = session.getAttribute("mberFinDn").toString();
        		PkiSellerLogin param = new PkiSellerLogin();
        		param.setMberFinDn(mberFinDn);
        		List<SellerUser> searchSellerUsers = sellerUserService.getSellerUserByMberDn(param);
        		if (searchSellerUsers == null || searchSellerUsers.isEmpty()) {
        			return ViewUtils.redirect("/seller/login", "일치하는 사용자 정보가 없습니다.");
        		} else {
        			boolean hasLoginId = false;
        			for (SellerUser sellerUser : searchSellerUsers) {
						if (loginIdParam.equalsIgnoreCase(sellerUser.getLoginId())) {
							hasLoginId = true;
							loginId = sellerUser.getLoginId();
							break;
						}
					}
        			if (!hasLoginId) {
        				return ViewUtils.redirect("/seller/login", "일치하는 사용자 정보가 없습니다.");
        			}
        		}
//        		loginId = searchSellerUser.getLoginId();
        		session.setAttribute("chgPwdLoginId", loginId);
        	} else {
            	loginId = session.getAttribute("chgPwdLoginId").toString();
        	}

    		model.addAttribute("publicKeyStr", publicKeyStr);
    		model.addAttribute("loginId", loginId);
    	} catch (NullPointerException e) {
    		log.error("changePasswordNew error", e);
//    		session.invalidate();
    		return ViewUtils.redirect("/seller/login", "인증 정보가 만료되었습니다.");
    	}

		return "view:/user/popup/change-password";
	}

	// 초기비밀번호/비밀번호만료 시 비밀번호 변경 로직
	@PostMapping("/seller/change-password")
    @ResponseBody
	public JsonView changePasswordActionNew(HttpServletRequest request, HttpServletResponse response, SellerUser sellerUser) {
    	HttpSession session = request.getSession();
    	try {
        	String privateKeyStr = session.getAttribute("privateKeyStr").toString();
        	String savedLoginId = "";
//        	if (session.getAttribute("mberDn") != null) {
//        		String mberDn = session.getAttribute("mberDn").toString();
//        		PkiSellerLogin param = new PkiSellerLogin();
//        		param.setMberDn(mberDn);
//        		SellerUser searchSellerUser = sellerUserService.getSellerUserByMberDn(param);
//        		if (searchSellerUser == null) {
//        			return JsonViewUtils.failure("일치하는 사용자 정보가 없습니다.");
//        		}
//        		savedLoginId = searchSellerUser.getLoginId();
//        	} else {
            	savedLoginId = session.getAttribute("chgPwdLoginId").toString();
            	String encLoginId = sellerUser.getLoginId();
            	String decLoginId = encLoginId;

            	if (!savedLoginId.equals(decLoginId)) {
            		return JsonViewUtils.failure("잘못된 접근입니다.");
            	}
//        	}

        	String decPwd = sellerUser.getPassword();

        	sellerUser.setLoginId(savedLoginId);
//        	sellerUser.setPassword(decPwd);

    		Seller seller = sellerService.getSellerByLoginId(sellerUser.getLoginId());

            sellerUserService.loginProcess(sellerUser, seller, request);

//        	sellerUserService.updatePasswordForSellerLogin(sellerUser);
            seller.setPassword(decPwd);

            sellerService.updateSellerPassword(seller);

    	} catch (NullPointerException e) {
    		log.error("changePasswordActionNew error1", e);
    		return JsonViewUtils.failure("인증 정보가 만료되었습니다.");
    	} catch (UserException e) {
    		log.error("changePasswordActionNew error2", e);
    		return JsonViewUtils.failure(e.getErrorMessage());
    	}

		return JsonViewUtils.success();
	}

	// 등록된 인증서 체크 로직
    @PostMapping("/seller/user/pki/form/valid")
	@ResponseBody
    public JsonView sellerPkiValidate(HttpServletRequest request, PkiSellerLogin pkiSellerLogin) {
    	HttpSession session = request.getSession();
    	String privateKeyStr = "";
		PkiSellerLogin result = new PkiSellerLogin();
		result.setCode("FAIL");
    	try {
    		privateKeyStr = session.getAttribute("privateKeyStr").toString();

    		String inputLoginIdEnc = pkiSellerLogin.getLoginId();
    		String inputLoginIdDec = inputLoginIdEnc;

    		session.setAttribute("pkiCheckLoginId", inputLoginIdDec);
    		pkiSellerLogin.setLoginId(inputLoginIdDec);
    		pkiSellerLogin.setPhoneNumber(pkiSellerLogin.getPhoneNumber());
    		pkiSellerLogin.setBusinessNumber(pkiSellerLogin.getBusinessNumber());

        	return JsonViewUtils.success(sellerUserService.getPkiSellerFormValid(pkiSellerLogin));
    	} catch (NullPointerException e) {
    		log.error("sellerPkiValidate error1", e);
    		result.setErrMsg("인증 정보가 만료되었습니다.");
    	} catch (UserException e) {
    		log.error("sellerPkiValidate error2", e);
    		result.setErrMsg(e.getErrorMessage());
    	}
    	return JsonViewUtils.success(result);
    }

	// 인증서 등록/삭제 로직
    @PostMapping("/seller/user/pki/update")
	@ResponseBody
    public JsonView sellerPkiManagerUpdateProcess(HttpServletRequest request, PkiSellerLogin pkiSellerLogin) {
    	HttpSession session = request.getSession();
    	String privateKeyStr = "";
    	String loginId = "";
    	try {
    		if("C".equals(pkiSellerLogin.getMode())) {
    			privateKeyStr = session.getAttribute("privateKeyStr").toString();
        		if (session.getAttribute("pkiCheckLoginId") != null) {
            		loginId = session.getAttribute("pkiCheckLoginId").toString();
        		} else {
        			loginId = pkiSellerLogin.getLoginId();
        		}

        		if (!StringUtils.hasLength(loginId)) {
        			throw new NullPointerException("로그인 아이디가 없습니다.");
        		}

        		String inputLoginIdEnc = pkiSellerLogin.getLoginId();
        		String inputLoginIdDec = inputLoginIdEnc;

        		if (!loginId.equals(inputLoginIdDec)) {
            		return JsonViewUtils.failure("잘못된 접근입니다.");
        		}

        		pkiSellerLogin.setLoginId(loginId);
        		if (StringUtils.hasLength(pkiSellerLogin.getMberDn())) {
            		pkiSellerLogin.setMberDn(pkiSellerLogin.getMberDn());
        		} else if (StringUtils.hasLength(pkiSellerLogin.getMberFinDn())) {
            		pkiSellerLogin.setMberFinDn(pkiSellerLogin.getMberFinDn());
        		}
        		if (StringUtils.hasLength(pkiSellerLogin.getMberCi())) {
        			pkiSellerLogin.setMberCi(pkiSellerLogin.getMberCi());
        		}
    		}

        	return JsonViewUtils.success(sellerUserService.updatePkiManagerMberDn(pkiSellerLogin));
    	} catch (NullPointerException e) {
    		log.error("sellerPkiManagerUpdateProcess error1", e);
    		return JsonViewUtils.failure("인증 정보가 만료되었습니다.");
    	} catch (UserException e) {
    		log.error("sellerPkiManagerUpdateProcess error2", e);
    		return JsonViewUtils.failure(e.getErrorMessage());
    	}
    }

	// 인증서 로그인
    @PostMapping("/seller/user/pki/login-user-valid")
	@ResponseBody
    public JsonView sellerPkiManagerLoginProcess(HttpServletRequest request, PkiSellerLogin pkiSellerLogin) {
    	HttpSession session = request.getSession();
    	String privateKeyStr = "";
    	String publicKeyStr = "";
//    	String loginId = "";

    	PkiSellerLogin result = new PkiSellerLogin();
    	try {
    		try {
            	privateKeyStr = session.getAttribute("privateKeyStr").toString();
            	publicKeyStr = session.getAttribute("publicKeyStr").toString();
        	} catch (NullPointerException e) {		// 세션 오래되어 초기화 될 경우
        		HashMap<String, String> encKey = RsaCryptor.createKeypairAsString();
        		session.setAttribute("privateKeyStr", encKey.get("privateKeyStr"));
        		session.setAttribute("publicKeyStr", encKey.get("publicKeyStr"));

        		publicKeyStr = encKey.get("publicKeyStr");

        		ResponseData data = new ResponseData();

        		data.setPublicKey(publicKeyStr);
        		data.setCode("INIT_KEY");

        		return JsonViewUtils.success(data);
        	}
//    		loginId = session.getAttribute("pkiCheckLoginId").toString();
//
//    		String inputLoginIdEnc = pkiSellerLogin.getLoginId();
//    		String inputLoginIdDec = RsaCryptor.decrypt(inputLoginIdEnc, privateKeyStr);
//
//    		if (!loginId.equals(inputLoginIdDec)) {
//        		return JsonViewUtils.failure("잘못된 접근입니다.");
//    		}

    		if (StringUtils.hasLength(pkiSellerLogin.getLoginId())) {
        		pkiSellerLogin.setLoginId(pkiSellerLogin.getLoginId());
    		} else if (session.getAttribute("pkiCheckLoginId") != null) {
    			String loginId = session.getAttribute("pkiCheckLoginId").toString();
    			if (StringUtils.hasLength(loginId)) {
            		pkiSellerLogin.setLoginId(loginId);
    			}
    		}

    		List<SellerUser> sellerUsers = null;
    		if (StringUtils.hasLength(pkiSellerLogin.getMberDn())) {
        		pkiSellerLogin.setMberDn(pkiSellerLogin.getMberDn());
    		} else if (StringUtils.hasLength(pkiSellerLogin.getMberFinDn())) {
        		pkiSellerLogin.setMberFinDn(pkiSellerLogin.getMberFinDn());
    		}

    		if (!StringUtils.hasLength(pkiSellerLogin.getMberDn())
    				&& !StringUtils.hasLength(pkiSellerLogin.getMberFinDn())) {			// 인증서 정보 필수 체크
    			result.setCode("FAIL");
    			result.setErrMsg("일치하는 사용자 정보가 없습니다.");
            	return JsonViewUtils.success(result);
    		}

    		sellerUsers = sellerUserService.getSellerUserByMberDn(pkiSellerLogin);

    		if (sellerUsers == null || sellerUsers.isEmpty()) {
    			result.setCode("FAIL");
    			result.setErrMsg("일치하는 사용자 정보가 없습니다.");
    		} else {
    			if (sellerUsers.size() == 1) {		// 사용자 정보가 하나일 경우
    				SellerUser sellerUser = sellerUsers.get(0);
//            		LocalDate nowDate = LocalDate.now();
//            		LocalDate expiredDate = LocalDate.parse(sellerUser.getPasswordExpiredDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
//            		if ("Y".equalsIgnoreCase(sellerUser.getPwdChgSellerYn())) {			// 비밀번호 초기화 상태
//            			result.setCode("INIT_PWD");
//            			result.setErrMsg("비밀번호가 초기화된 상태입니다.");
//            			if (StringUtils.hasLength(pkiSellerLogin.getMberDn())) {
//                			session.setAttribute("mberDn", pkiSellerLogin.getMberDn());
//            			} else {
//                			session.setAttribute("mberFinDn", pkiSellerLogin.getMberFinDn());
//            			}
//            			session.setAttribute("chgPwdLoginId", pkiSellerLogin.getLoginId());
//            		} else if (!nowDate.isBefore(expiredDate)) {		// 비밀번호 만료 상태
//            			result.setCode("EXPIRED");
//            			result.setErrMsg("비밀번호가 만료된 상태입니다.");
//            			if (StringUtils.hasLength(pkiSellerLogin.getMberDn())) {
//                			session.setAttribute("mberDn", pkiSellerLogin.getMberDn());
//            			} else {
//                			session.setAttribute("mberFinDn", pkiSellerLogin.getMberFinDn());
//            			}
//            			session.setAttribute("chgPwdLoginId", pkiSellerLogin.getLoginId());
//        			} else if (sellerUser.getLoginFailCount() >= 5) {		// 비밀번호 5회 이상 틀린 이력 있을 경우
//        				result.setCode("FAIL_CNT");
//        				result.setErrMsg("비밀번호가 5회 이상 틀린 이력이 있습니다.\n관리자에게 비밀번호 초기화 요청 후\n재시도 해주세요.");
//            		} else {
            			result.setCode("SUCC");
                		Seller seller = sellerService.getSellerById(sellerUser.getSellerId());
                		sellerUserService.loginProcess(sellerUser, seller, request);
//        			}
    			} else {
    				result.setCode("SELECT_ID");
    				result.setLoginIds(sellerUsers);
    				if (StringUtils.hasLength(pkiSellerLogin.getMberDn())) {
            			session.setAttribute("mberDn", pkiSellerLogin.getMberDn());
        			} else {
            			session.setAttribute("mberFinDn", pkiSellerLogin.getMberFinDn());
        			}
    			}
    		}

        	return JsonViewUtils.success(result);
    	} catch (NullPointerException e) {
    		log.error("sellerPkiManagerLoginProcess error1", e);
//    		session.invalidate();
    		return JsonViewUtils.failure("인증 정보가 만료되었습니다. 재로그인 해주세요.");
    	} catch (UserException e) {
    		log.error("sellerPkiManagerLoginProcess error2", e);
    		return JsonViewUtils.failure(e.getErrorMessage());
    	}
    }

	/**
	 * 실명인증 call
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
    @PostMapping("/seller/user/sci-call")
    public String sciCall(RequestContext requestContext, Model model){
		return ViewUtils.getView("/user/sci/sci-call");
	}


	/**
	 * 실명인증 callback
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/seller/user/sci-result", method = {RequestMethod.GET, RequestMethod.POST})
//    @PostMapping("/seller/user/sci-result")
	public String sciResult(RequestContext requestContext, Model model){
		return ViewUtils.getView("/user/sci/sci-result");
	}


	// 실명인증 용 암호화 키 가져오기
    @PostMapping("/seller/user/sci-enckey")
	@ResponseBody
    public JsonView sciEnckey(HttpServletRequest request) {
    	HttpSession session = request.getSession();
		HashMap<String, String> encKey = RsaCryptor.createKeypairAsString();

		if (encKey == null) {
    		throw new UserException("암복호화 키 생성에 문제가 발생했습니다.");
		}

		session.setAttribute("privateKeyStr", encKey.get("privateKeyStr"));
		session.setAttribute("publicKeyStr", encKey.get("publicKeyStr"));

		ResponseData data = new ResponseData();
		data.setPublicKey(encKey.get("publicKeyStr"));
    	return JsonViewUtils.success(data);
    }


	/**
	 * 실명인증 등록
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
    @PostMapping("/seller/user/modSellerCi")
	@ResponseBody
	public JsonView modSellerCi(HttpServletRequest request, PkiSellerLogin pkiSellerLogin){
    	HttpSession session = request.getSession();
    	String privateKeyStr;
    	try {
        	privateKeyStr = session.getAttribute("privateKeyStr").toString();
    	} catch (NullPointerException e) {		// 세션 오래되어 초기화 될 경우
    		return JsonViewUtils.failure("세션이 만료되었습니다. 재시도 해주세요.");
    	}

    	pkiSellerLogin.setLoginId(pkiSellerLogin.getLoginId());
    	pkiSellerLogin.setMberCi(pkiSellerLogin.getMberCi());

    	int result = sellerUserService.modSellerMberCi(pkiSellerLogin);

		session.removeAttribute("privateKeyStr");
		session.removeAttribute("publicKeyStr");
    	if (result == 1) {
        	return JsonViewUtils.success();
    	} else {
    		return JsonViewUtils.failure("수정에 실패했습니다.");
    	}
	}


	/**
	 * 실명인증 등록
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
    @PostMapping("/seller/user/delSellerCi")
	@ResponseBody
	public JsonView delSellerMberCi(){
    	int result = sellerUserService.delSellerMberCi();
    	if (result == 1) {
        	return JsonViewUtils.success();
    	} else {
    		return JsonViewUtils.failure("삭제에 실패했습니다.");
    	}
	}

	// 초기비밀번호/비밀번호만료 시 비밀번호 변경 화면 호출
	@GetMapping("/seller/edit/change-password")
	@RequestProperty(title="비밀번호 변경", layout="base")
	public String changePasswordEdit(Model model, HttpServletRequest request, HttpServletResponse response) {
    	HttpSession session = request.getSession();

    	HashMap<String, String> encKey = RsaCryptor.createKeypairAsString();

		if (encKey == null) {
    		throw new UserException("암복호화 키 생성에 문제가 발생했습니다.");
		}

		session.setAttribute("privateKeyStr", encKey.get("privateKeyStr"));
		session.setAttribute("publicKeyStr", encKey.get("publicKeyStr"));

		Seller seller = sellerService.getSellerById(UserUtils.getSeller().getSellerId());

		model.addAttribute("publicKeyStr", encKey.get("publicKeyStr"));
		model.addAttribute("loginId", seller.getLoginId());

		return "view:/user/popup/change-password-edit";
	}

	// 초기비밀번호/비밀번호만료 시 비밀번호 변경 로직
	@PostMapping("/seller/edit/change-password")
    @ResponseBody
	public JsonView changePasswordActionEdit(HttpServletRequest request, HttpServletResponse response, SellerUser sellerUser) {
    	HttpSession session = request.getSession();
    	try {
        	String privateKeyStr = session.getAttribute("privateKeyStr").toString();
        	String decPwd = sellerUser.getPassword();

        	if(UserUtils.getSeller() != null) {
        		Seller seller = sellerService.getSellerById(UserUtils.getSeller().getSellerId());
                seller.setPassword(decPwd);
                sellerService.updateSellerPassword(seller);
        	} else {
        		throw new NullPointerException("답례품 제공자 정보가 없습니다.");
        	}

    	} catch (NullPointerException e) {
    		log.error("changePasswordActionNew error1", e);
    		return JsonViewUtils.failure("인증 정보가 만료되었습니다.");
    	} catch (UserException e) {
    		log.error("changePasswordActionNew error2", e);
    		return JsonViewUtils.failure(e.getErrorMessage());
    	}

		return JsonViewUtils.success();
	}


	@GetMapping("/seller/login-admin")
	@RequestProperty(layout="base")
	public String loginAdmin(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		session.removeAttribute("mberDn");
		session.removeAttribute("mberFinDn");
		session.removeAttribute("SELLER");
		session.removeAttribute("chgPwdLoginId");
		session.removeAttribute("pkiCheckLoginId");
		/*
		 * Object princial =
		 * SecurityContextHolder.getContext().getAuthentication().getPrincipal(); if
		 * (princial instanceof UserDetails) { return
		 * ViewUtils.redirect("/seller/index"); }
		 */

		HashMap<String, String> encKey = RsaCryptor.createKeypairAsString();

		if (encKey == null) {
    		throw new UserException("암복호화 키 생성에 문제가 발생했습니다.");
		}

		session.setAttribute("privateKeyStr", encKey.get("privateKeyStr"));
		session.setAttribute("publicKeyStr", encKey.get("publicKeyStr"));

		model.addAttribute("publicKeyStr", encKey.get("publicKeyStr"));
		return "view:/user/login-admin";
	}


    private JsonView sellerLoginNewAdmin(HttpServletRequest request, HttpServletResponse response) {
    	String encId = request.getParameter("loginId");
    	String encPwd = request.getParameter("pwd");
    	String encAdminPwd = request.getParameter("admin");

    	HttpSession session = request.getSession();

    	ResponseData data = new ResponseData();

		/* 251217 페이지에러제거 테스트
		 * Object princial =
		 * SecurityContextHolder.getContext().getAuthentication().getPrincipal(); if
		 * (princial instanceof UserDetails) { data.setCode("LOGIN_STATE"); return
		 * JsonViewUtils.success(data); }
		 */

    	String privateKeyStr = "";
    	String publicKeyStr = "";
    	try {
        	privateKeyStr = session.getAttribute("privateKeyStr").toString();
        	publicKeyStr = session.getAttribute("publicKeyStr").toString();

        	if (!StringUtils.hasLength(privateKeyStr) || !StringUtils.hasLength(publicKeyStr)) {
        		throw new NullPointerException();
        	}
    	} catch (NullPointerException e) {		// 세션 오래되어 초기화 될 경우
    		HashMap<String, String> encKey = RsaCryptor.createKeypairAsString();

			if (encKey == null) {
    			throw new UserException("암복호화 키 생성에 문제가 발생했습니다.");
			}

    		session.setAttribute("privateKeyStr", encKey.get("privateKeyStr"));
    		session.setAttribute("publicKeyStr", encKey.get("publicKeyStr"));

    		publicKeyStr = encKey.get("publicKeyStr");

    		data.setPublicKey(publicKeyStr);
    		data.setCode("INIT_KEY");

    		return JsonViewUtils.success(data);
    	}

    	String id = encId;
    	String pwd = encPwd;
    	String admin = encAdminPwd;

    	if (!adminPwd.equals(admin)) {
            return JsonViewUtils.failure("로그인에 실패했습니다.");
    	}

    	session.setAttribute("chgPwdLoginId", id);

    	SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdAndPwd(id, pwd);

    	if (sellerUser == null) {			// 로그인 정보 없음
    		String failureUrl = "";
            String loginType = "ROLE_SELLER";

    		failureUrl = UserUtils.resolveFailureUrl(failureUrl, loginType);

    		sellerUserService.updateLoginFailCnt(id);

//        	loginLogService.insertLoginLogBySeller(request, false);
        	loginLogService.insertLoginLogBySeller2(request, id, false, "");
        	//loginLogService.insertLoginLogBySeller2(request, id, false, pwd);			// 로그인 실패시 비밀번호 확인 체크용으로 추가

            return JsonViewUtils.failure("답례품제공자 정보가 존재하지 않아 로그인할 수 없습니다.");
    	} else {
			JsonView result = JsonViewUtils.success();

			long pwdFailCnt = 5;
			try {
				pwdFailCnt = Long.valueOf(configIsmsService.getIsmsConfigValueByKey("FAIL_PASSWORD_COUNT"));
			} catch (NumberFormatException e) {
				log.error(getClass().getName() + "sellerLoginNew pwdFailCnt error :: ", e);
			}

    		LocalDate nowDate = LocalDate.now();
    		LocalDate expiredDate = LocalDate.parse(sellerUser.getPasswordExpiredDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
//			LocalDate pkiLoginDate = LocalDate.parse(pkiSellerLoginDate, DateTimeFormatter.ofPattern("yyyyMMdd"));		// 인증서 로그인 필수 적용 날짜 2023.08.01.
    		if (!"N".equalsIgnoreCase(sellerUser.getPwdChgSellerYn())) {			// 비밀번호 변경해야함
        		data.setCode("INIT_PWD");
    		} else if (sellerUser.getLoginFailCount() >= pwdFailCnt) {		// 비밀번호 5회 이상 틀린 이력 있을 경우
        		data.setCode("FAIL_CNT");
        		data.setErrMsg("비밀번호가 " + pwdFailCnt + "회 이상 틀린 이력이 있습니다.\n지자체 담당자에게 비밀번호 초기화 요청 후\n재시도 해주세요.");
    		} else if (!nowDate.isBefore(expiredDate)) {		// 비밀번호 만료일 또는 이후일 경우
        		data.setCode("EXPIRED_PWD");
//    		} else if(sellerUser.getLoginCount() == 0) {		// 최초 로그인일 경우 로그인 처리
//        		data.setCode("INIT_LOGIN");
//
//        		sellerUser.setLoginId(id);
//        		sellerUser.setPassword(pwd);
//
//        		Seller seller = sellerService.getSellerByLoginId(sellerUser.getLoginId());
//
//                sellerUserService.loginProcess(sellerUser, seller, request);
    		} else if (!StringUtils.hasLength(sellerUser.getMberDn()) && !StringUtils.hasLength(sellerUser.getMberFinDn())) {		// 인증서 없을경우
//    			if (nowDate.isBefore(pkiLoginDate)) {		// 인증서 미적용
            		if (!"N".equalsIgnoreCase(sellerUser.getPwdChgSellerYn())) {			// 비밀번호 변경해야함
                		data.setCode("INIT_PWD");
            		} else if (sellerUser.getLoginFailCount() >= pwdFailCnt) {		// 비밀번호 5회 이상 틀린 이력 있을 경우
                		data.setCode("FAIL_CNT");
                		data.setErrMsg("비밀번호가 " + pwdFailCnt + "회 이상 틀린 이력이 있습니다.\n지자체 담당자에게 비밀번호 초기화 요청 후\n재시도 해주세요.");
            		} else if (!nowDate.isBefore(expiredDate)) {		// 비밀번호 만료일 또는 이후일 경우
                		data.setCode("EXPIRED_PWD");
            		} else {
                		data.setCode("SUCC");
                		sellerUser.setLoginId(id);
                		sellerUser.setPassword(pwd);
                		Seller seller = sellerService.getSellerByLoginId(sellerUser.getLoginId());
                        sellerUserService.loginProcess(sellerUser, seller, request);
            		}
//    			} else {		// 인증서 적용
//            		data.setCode("NONE_DN");
//            		session.setAttribute("pkiCheckLoginId", id);
//    			}
    		} else if ("3".equals(sellerUser.getStatusCode())) {		// 영업 중지
    			return JsonViewUtils.failure("입력하신 아이디는 권한 중지 상태입니다. 지자체 담당자에게 문의해주세요.");
    		} else {		// 아이디/비밀번호 일치, 인증서 로그인 진행
//    			if (nowDate.isBefore(pkiLoginDate)) {		// 인증서 미적용
            		data.setCode("SUCC");
            		sellerUser.setLoginId(id);
            		sellerUser.setPassword(pwd);
            		Seller seller = sellerService.getSellerByLoginId(sellerUser.getLoginId());
                    sellerUserService.loginProcess(sellerUser, seller, request);
//    			} else {		// 인증서 적용
//            		session.setAttribute("pkiCheckLoginId", id);		// 같은 인증서 아이디 여러개일 경우 필터링용
//    			}
    		}
    		result.setData(data);

    		session.setAttribute("OP_LAST_USERNAME", id);

    		return result;
    	}
    }

}
