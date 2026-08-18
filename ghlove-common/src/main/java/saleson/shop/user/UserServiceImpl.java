package saleson.shop.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.common.OpKeyHolder;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.isms.ConfigIsmsService;
import com.onlinepowers.framework.security.mapper.SecurityMapper;
import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.security.userdetails.OpUserDetails;
import com.onlinepowers.framework.security.userdetails.OpUserDetailsService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.SmsType;
import saleson.common.notification.UnifiedMessagingService;
import saleson.common.opmanager.count.OpmanagerCount;
import saleson.common.security.crypto.CipherUtilsCopy;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.sms.SmsIpsService;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.LocalDateUtils;
import saleson.common.utils.PointUtils;
import saleson.common.utils.RandomStringUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.Ums;
import saleson.model.UserAgree;
import saleson.model.UserEntity;
import saleson.model.user.UserLogin;
import saleson.shop.analysis.AnalysisMapper;
import saleson.shop.config.ConfigMapper;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.coupon.CouponService;
import saleson.shop.coupon.domain.ChosenUser;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.group.GroupMapper;
import saleson.shop.group.domain.Group;
import saleson.shop.group.support.GroupSearchParam;
import saleson.shop.log.ChangeLogService;
import saleson.shop.log.LoginLogService;
import saleson.shop.log.ManagerHistService;
import saleson.shop.log.PasswordLogService;
import saleson.shop.log.domain.ManagerHist;
import saleson.shop.log.domain.PasswordLog;
import saleson.shop.log.support.ChangeTypeEnum;
import saleson.shop.mailconfig.MailConfigService;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.mailconfig.support.MemberJoinMail;
import saleson.shop.mailconfig.support.MemberSleepMail;
import saleson.shop.mailconfig.support.PwsearchMail;
import saleson.shop.offgive.domain.Manager;
import saleson.shop.order.domain.Buyer;
import saleson.shop.point.PointService;
import saleson.shop.point.domain.AvailablePoint;
import saleson.shop.point.domain.Point;
import saleson.shop.policy.PolicyService;
import saleson.shop.policy.domain.Policy;
import saleson.shop.sendmaillog.SendMailLogService;
import saleson.shop.sendmaillog.domain.SendMailLog;
import saleson.shop.sendsmslog.SendSmsLogService;
import saleson.shop.sendsmslog.domain.SendSmsLog;
import saleson.shop.smsconfig.domain.SmsConfig;
import saleson.shop.ums.UmsService;
import saleson.shop.ums.support.MemberJoin;
import saleson.shop.ums.support.Pwsearch;
import saleson.shop.user.domain.AuthUserInfo;
import saleson.shop.user.domain.GeneralCustomer;
import saleson.shop.user.domain.GeneralCustomerSecede;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.ManagerLogin;
import saleson.shop.user.domain.PersonInCharge;
import saleson.shop.user.domain.PkiManagerLogin;
import saleson.shop.user.domain.PkiManagerLoginResult;
import saleson.shop.user.domain.SecedeCntrAmt;
import saleson.shop.user.domain.TempPasswordChange;
import saleson.shop.user.domain.UserCriteriaEncryptor;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserDetailEncryptor;
import saleson.shop.user.domain.UserEncryptor;
import saleson.shop.user.domain.UserInfoByCI;
import saleson.shop.user.domain.UserInfoBySign;
import saleson.shop.user.domain.UserModifyInfo;
import saleson.shop.user.domain.UserParent;
import saleson.shop.user.domain.UserPasswordType;
import saleson.shop.user.support.AgreeDto;
import saleson.shop.user.support.AuthInfo;
import saleson.shop.user.support.ChangePasswordForNoLoginUser;
import saleson.shop.user.support.FinancInfo;
import saleson.shop.user.support.FinancResultInfo;
import saleson.shop.user.support.FinancTokenResult;
import saleson.shop.user.support.GeneralCustomerSearchParam;
import saleson.shop.user.support.OpManagerParam;
import saleson.shop.user.support.SSLSocketFactoryMaker;
import saleson.shop.user.support.SignInfo;
import saleson.shop.user.support.UserSearchParam;
import saleson.shop.usergroup.UserGroupMapper;
import saleson.shop.usergroup.domain.UserGroupLog;
import saleson.shop.userlevel.UserLevelMapper;
import saleson.shop.userlevel.UserLevelService;
import saleson.shop.userlevel.domain.UserLevel;
import saleson.shop.userlevel.domain.UserLevelLog;
import saleson.shop.userlevel.support.UserLevelSearchParam;
import saleson.shop.userrole.UserRoleService;
import saleson.shop.usersns.UserSnsMapper;
import saleson.shop.usersns.domain.UserSns;
import saleson.shop.usersns.domain.UserSnsEncryptor;

@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl extends EgovAbstractServiceImpl implements UserService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	private final UserMapper userMapper;
	private final SequenceService sequenceService;
	private final UserRoleService userRoleService;
	private final UserLevelService userLevelService;
	private final ConfigService configService;
	private final MailConfigService mailConfigService;
	private final SendMailLogService sendMailLogService;
	private final PointService pointService;
	private final SendSmsLogService sendSmsLogService;
	private final UserLevelMapper userLevelMapper;
	private final UserGroupMapper userGroupMapper;
	private final GroupMapper groupMapper;
	private final PasswordEncoder passwordEncoder;
	private final ConfigMapper configMapper;
	private final CouponService couponService;
	private final UserSnsMapper userSnsMapper;
	private final ConfigIsmsService configIsmsService;
	private final ManagerLoginRepository managerLoginRepository;
	private final UmsService umsService;
	private final UnifiedMessagingService unifiedMessagingService;
	private final ChangeLogService changeLogService;
	private final UserCriteriaEncryptor userCriteriaEncryptor;
	private final UserEncryptor userEncryptor;
	private final UserDetailEncryptor userDetailEncryptor;
	private final Cryptor cryptor;
	private final DataMasking dataMasking;
	private final UserSnsEncryptor userSnsEncryptor;
	private final JoinMapper joinMapper;
	private final ManagerRequestMapper managerReqeustMapper;
	private final SecurityService securityService;
	private final OpUserDetailsService userDetailsService;
	private final UserLoginRepository userLoginRepository;
	private final SecurityMapper securityMapper;
	private final LoginLogService loginLogService;
	private final SmsIpsService smsIpsService;
	private final AnalysisMapper analysisMapper;

	private final ManagerHistService managerHistService;

	@Autowired
	private UserAgreeRepository userAgreeRepository;

	@Autowired
	private PolicyService policyService;

	@Autowired
	private PasswordLogService passwordLogService;

	@Autowired
	private UserRepository userRepository;

	@Value("${fincert.url}")
    private String finUrl;

	@Value("${fincert.ucpidUrl}")
    private String ucpidUrl;

    @Value("${fincert.client-id}")
    private String finClientId;

    @Value("${fincert.client-secret}")
    private String finClientSecret;

    @Value("${fincert.scope}")
    private String finScope;

    @Value("${fincert.grant-type}")
    private String finGrantType;

    @Value("${fincert.server-id}")
    private String finServerId;

    @Value("${fincert.cp-code}")
    private String fincertCpCode;

    int TIMEOUT_VALUE = 10000;   // 10초

	private final GeneralCustomerMapper generalCustomerMapper;

	private static final int ARRAY_LENGTH = 16;

	private boolean CERTIFICATE = true;

    //private SSLSocketFactory _sslSockFactory;

	@Override
	public void setMypageUserInfoForFront(Model model) {
		UserCouponParam userCouponParam = new UserCouponParam();
		userCouponParam.setUserId(UserUtils.getUserId());

		int totalCount = couponService.getDownloadUserCouponCountByUserCouponParam(userCouponParam);

		// 총 사용가능 포인트
		AvailablePoint avilablePoint = pointService.getAvailablePointByUserId(UserUtils.getUserId(), PointUtils.DEFAULT_POINT_CODE);

		// 총 사용가능 배송 쿠폰
		AvailablePoint avilableShippingCoupon = pointService.getAvailablePointByUserId(UserUtils.getUserId(), PointUtils.SHIPPING_COUPON_CODE);

		// 총 사용가능 캐시
		AvailablePoint avilableEmoney = pointService.getAvailablePointByUserId(UserUtils.getUserId(), PointUtils.EMONEY_CODE);

		String today = DateUtils.getToday(Const.DATE_FORMAT);

		model.addAttribute("userCouponCount", totalCount);
		model.addAttribute("userShippingCount", avilableShippingCoupon.getAvailablePoint());
		model.addAttribute("userPoint", avilablePoint.getAvailablePoint());
		model.addAttribute("userEmoney", avilableEmoney.getAvailablePoint());

		// 유저 레벨
		model.addAttribute("userLevel", UserUtils.getUserDetail().getUserlevel());
		model.addAttribute("today", today);
	}

	@Override
	public void updateUserPasswod(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setPasswordExpiredDate(this.getPasswordExpiredDate());
		userMapper.updateUserPasswordByUserId(user);
	}

	@Override
	public UserDetail getUserDetail(long userId) {
		UserDetail userDetail = userMapper.getUserDetail(userId);

		if (userDetail != null) {
			userDetail.decrypt(userDetailEncryptor, ShopUtils.needMasking());
		}

		return userDetail;
	}

	@Override
	public int getUserCount(UserSearchParam searchParam) {

		searchParam.encrypt(userCriteriaEncryptor);
		int count = userMapper.getUserCount(searchParam);
		searchParam.decrypt(userCriteriaEncryptor);

		return count;
	}

	@Override
	public int getUserCountByPhoneNumber(String phoneNumber) {
		return userMapper.getUserCountByPhoneNumber(phoneNumber);
	}


	@Override
	public List<User> getUserList(UserSearchParam searchParam) {

		searchParam.encrypt(userCriteriaEncryptor);
		List<User> list = userMapper.getUserList(searchParam);

		// 복호화
		list.forEach(u -> decryptData(u));

		searchParam.decrypt(userCriteriaEncryptor);
		return list;

	}

	private void privacyMaskingDataSet(User user){
		UserDetail detail = (UserDetail)user.getUserDetail();
		String telNo = detail.getTelNumber();		// 전화번호
		String phoneNo = detail.getPhoneNumber();	// 휴대번호
		String birthDay = detail.getBirthday();		// 생일
		String email = user.getEmail();				// 이메일
		String name = user.getUserName();			// 이름
		String zipCode = detail.getPost();			// 우편번호
		String address = detail.getAddress(); // 주소
		String addressDetail = detail.getAddressDetail(); // 상세주소

		if(user.getUserName() != null && user.getUserName().length() > 1) {
			user.setUserName(UserUtils.masking(user.getUserName(), "name"));
		}
		if(address != null){
			detail.setAddress(UserUtils.reMasking(address, "addr"));
		}
		if(addressDetail != null){
			detail.setAddressDetail(UserUtils.reMasking(addressDetail, "addrDetail"));
		}
		if(zipCode != null){
			detail.setPost(UserUtils.reMasking(zipCode, "zipCode"));
		}
		if(name != null){
			user.setUserName(UserUtils.reMasking(name, "name"));
		}
		if(email != null){
			user.setEmail(UserUtils.reMasking(email, "email"));
		}
		if(telNo != null && telNo.length() > 9) {
			detail.setTelNumber(UserUtils.reMasking(telNo, "tel"));
		}
		if(phoneNo != null && phoneNo.length() > 9) {
			detail.setPhoneNumber(UserUtils.reMasking(phoneNo, "tel"));
		}
		if(birthDay != null) {
			detail.setBirthday(UserUtils.masking(birthDay, "day"));
		}
	}

	@Override
	public void insertManager(User user) {

//		long userId = sequenceService.getLong("OP_USER");
		long userId = userMapper.selectNewUserId();
		user.setUserId(userId);
		user.setStatusCode("9");

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setPasswordExpiredDate(this.getPasswordExpiredDate());

		user.encrypt(userEncryptor);
		userMapper.insertManager(user);


		List<UserRole> userRoles = user.getUserRoles();

		if (ValidationUtils.isNotNull(user.getUserRoles())) {
			for (UserRole userRole : userRoles) {
				if (ValidationUtils.isNotNull(userRole.getAuthority())) {
					userRole.setUserId(userId);
					userRole.setAuthority(userRole.getAuthority());
					userRoleService.insertUserRole(userRole);
				}
			}
		}

		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		userRole.setAuthority("ROLE_OPMANAGER");
		userRoleService.insertUserRole(userRole);
	}

	@Override
	public void insertChargerManager(User user, PersonInCharge personInCharge) {

		Manager manager = new Manager();
		manager.setUserId(String.valueOf(user.getUserId()));
		manager.setLoginId(user.getLoginId());
		manager.setPhoneNumber(user.getPhoneNumber());
		manager.setUserName(user.getUserName());
		manager.setEmail(personInCharge.getEmail());
		manager.setBankCode(personInCharge.getBankCode());
		manager.setOfcpsNm(personInCharge.getOfcpsNm());
		manager.setPsitnNm(personInCharge.getPsitnNm());
		manager.setEmpId(personInCharge.getEmpId());

		// 고정
		manager.setStatusCode("2");

		manager.setPassword(passwordEncoder.encode(user.getPassword()));
		manager.setPasswordExpiredDate(this.getPasswordExpiredDate());

		user.encrypt(userEncryptor);
		userMapper.insertChargerManager(manager);

		// [GGSR-26-369] 이력저장
		managerHistService.insertManagerHist(manager, "H");

	}

	@Override
	public void insertUser(User user) {
		try {
			user.setPassword(passwordEncoder.encode(pCrypto.Encrypt("hash.5", user.getPassword(), "")));
		} catch (UnsupportedEncodingException e) {
			log.error("insertUser 에러가 발생하였습니다.");
			throw new UserException("에러가 발생하였습니다.");
		}

		if (!StringUtils.hasText(user.getPasswordExpiredDate())) {
			user.setPasswordExpiredDate(this.getPasswordExpiredDate());
		}

		if (!StringUtils.hasText(user.getLoginId())) {
			user.setLoginId("rndm" + (int) (System.currentTimeMillis() / 1000L));
		}

		// 이메일이 입력되지 않았을 경우 로그인 아이디를 이메일 컬럼에 insert
		if (!StringUtils.hasText(user.getEmail())) {
			user.setEmail(user.getLoginId());
		}

		// 데이터암호화
		user.encrypt(userEncryptor);
		userMapper.insertUser(user);
	}

	@Override
	public void insertUserAndUserDetailByManager(User user, UserDetail userDetail){

//		long userId = sequenceService.getLong("OP_USER");
		long userId = userMapper.selectNewUserId();
		user.setUserId(userId);
		userDetail.setUserId(userId);
		//userDetail.setVenderId(1000);

		this.insertManager(user);
		this.insertUserDetail(userDetail);

		List<UserRole> userRoles = user.getUserRoles();

		if (ValidationUtils.isNotNull(user.getUserRoles())) {
			for (UserRole userRole : userRoles) {
				if (ValidationUtils.isNotNull(userRole.getAuthority())) {
					userRole.setUserId(userId);
					userRole.setAuthority(userRole.getAuthority());
					userRoleService.insertUserRole(userRole);
				}
			}
		}

		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		userRole.setAuthority("ROLE_OPMANAGER");
		userRoleService.insertUserRole(userRole);

		saveUserAgree(userDetail.getAgreeTypeDtos(), user);
	}

	@Override
	public void insertUserAndUserDetail(User user, UserDetail userDetail){

		/*
		//이부분 Controller로 이동
		long userId = sequenceService.getLong("OP_USER");
		user.setUserId(userId);

		userDetail.setUserId(userId);*/
		long userId = user.getUserId();

		userDetail.setPoint(ShopUtils.getConfig().getPointJoin());
		userDetail.setBirthday(userDetail.getBirthday());
		//userDetail.setBusinessNumber(userDetail.getFullBusinessNumber());

		this.insertUser(user);
		this.insertUserDetail(userDetail);

		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		userRole.setAuthority("ROLE_USER");
		userRoleService.insertUserRole(userRole);

		user = getUserByUserId(userId);

		// 회원 등급 적용
		userLevelService.setUserLevel(user);

		// 포인트 지급
		Point point = new Point();
		point.setUserId(userId);
		point.setPointType(PointUtils.DEFAULT_POINT_CODE);
		pointService.earnPoint("join", point);

		String templateId = "member_join";

		MailConfig checkMailConfig = mailConfigService.getMailConfigByTemplateId(templateId);
		Ums ums = umsService.getUms(templateId);

		if (checkMailConfig != null) {
			sendMail(user, templateId);
		}

		sendSms(ums, user, templateId);

		saveUserAgree(userDetail.getAgreeTypeDtos(), user);
	}

	@Override
	public void insertUserAndUserDetailForSns(User user, UserDetail userDetail, UserSns userSns) {
//		long userId = sequenceService.getLong("OP_USER");
		long userId = userMapper.selectNewUserId();
		user.setUserId(userId);
		user.setPasswordExpiredDate("99991231");

		userDetail.setUserId(userId);
		userDetail.setPoint(ShopUtils.getConfig().getPointJoin());
		userDetail.setBirthday(userDetail.getBirthday());
		//userDetail.setBusinessNumber(userDetail.getFullBusinessNumber());

		this.insertUser(user);
		this.insertUserDetail(userDetail);

		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		userRole.setAuthority("ROLE_USER");
		userRoleService.insertUserRole(userRole);

		user = getUserByUserId(userId);

		// 회원 등급 적용
		userLevelService.setUserLevel(user);

		// 포인트 지급
		Point point = new Point();
		point.setUserId(userId);
		point.setPointType(PointUtils.DEFAULT_POINT_CODE);
		pointService.earnPoint("join", point);

		userSns.setUserId(userId);


		// sns아이디 정보 삽입
		userSns.setSnsUserId(sequenceService.getId("OP_USER_SNS"));
		if (!userSns.getIsMypage() || userSns.getSnsUserId()==0) {
			if (UserUtils.isUserLogin()) {
				userSns.setUserId(UserUtils.getUserId());
				userSns.setCertifiedDate("Y");
			} else {
				userSns.setUserId(userSns.getUserId());
			}
		}

		// 암호화
		userSns.encrypt(userSnsEncryptor);
		userSnsMapper.insertUserSnsInfo(userSns);

		saveUserAgree(userDetail.getAgreeTypeDtos(), user);
	}

	private void sendMail(User user, String templateId) {

		MailConfig mailConfig = null;

		user.encrypt(userEncryptor);

		if ("member_join".equals(templateId)) {

			MemberJoinMail memberJoin = new MemberJoinMail(user, mailConfigService.getMailConfigByTemplateId(templateId), cryptor, dataMasking);
			mailConfig = memberJoin.getMailConfig();

		} else {

			PwsearchMail pwsearch = new PwsearchMail(user, mailConfigService.getMailConfigByTemplateId(templateId), cryptor, dataMasking);
			mailConfig = pwsearch.getMailConfig();

		}

		SendMailLog sendMailLog = new SendMailLog();
		sendMailLog.setReceiveLoginId(""+user.getUserId());
		sendMailLog.setUserId(user.getUserId()); // CRM의 메일발송내역 확인용
		sendMailLog.setSendType(templateId);

		sendMailLogService.sendMail(mailConfig, sendMailLog, user.getEmail(), user.getUserName());

	}

	private void sendSms(Ums ums, User user, String templateId){

		try {

			UserDetail userDetail = (UserDetail)user.getUserDetail();

			String phoneNumber = userDetail.getPhoneNumber();

			if (StringUtils.isNotEmpty(phoneNumber)) {

				Config config = ShopUtils.getConfig();

				// 가입 완료
				if ("member_join".equals(templateId)) {
					if (config == null) {
						throw new NullPointerException("설정 정보가 없습니다.");
					}
					unifiedMessagingService.sendMessage(new MemberJoin(ums, user, config, phoneNumber, cryptor, dataMasking));
				}

				// PW 확인 수정 KSH 2019.06.11
				if ("pwsearch".equals(templateId)) {
					unifiedMessagingService.sendMessage(new Pwsearch(ums, phoneNumber, user, cryptor, dataMasking));

				}

			}

		} catch (NullPointerException e) {
			log.error("User sendSms Error templateId- > [{}] userId -> [{}]", templateId, user.getUserId(), e);
		}
	}


	@Override
	public void insertUserDetail(UserDetail userDetail) {
		// 데이터암호화
		userDetail.encrypt(userDetailEncryptor);
		userMapper.insertUserDetail(userDetail);
		userMapper.insertUserBirthday(userDetail);
	}


	@Override
	public void insertUserRole(UserRole userRole) {
		userMapper.insertUserRole(userRole);
	}

	@Override
	public void updateUser(User user) {
		user = passwordFlagResult(user);
		user.encrypt(userEncryptor);
		userMapper.updateUser(user);
	}

	@Override
	public void updateUserDetail(UserDetail userDetail) {
		if (userMapper.getUserDetailCountByUserId(userDetail.getUserId()) == 0) {
			insertUserDetail(userDetail);
		} else {
			userDetail.encrypt(userDetailEncryptor);
			userMapper.updateUserDetail(userDetail);
		}
	}

	@Override
	public void updateUserRole(UserRole userRole) {
		userMapper.updateUserRole(userRole);
	}

	@Override
	public void updateUserAndUserDetail(User user, UserDetail userDetail) {

		this.updateUser(user);
		this.updateUserDetail(userDetail);

		userRoleService.deleteUserRole(user.getUserId());

		List<UserRole> userRoles = user.getUserRoles();

		if (ValidationUtils.isNotNull(user.getUserRoles())) {
			for (UserRole userRole : userRoles) {
				if (ValidationUtils.isNotNull(userRole.getAuthority())) {
					userRole.setUserId(user.getUserId());
					userRole.setAuthority(userRole.getAuthority());
					userRoleService.insertUserRole(userRole);
				}
			}
		}

		UserRole userRole = new UserRole();
		userRole.setUserId(user.getUserId());
		userRole.setAuthority("ROLE_OPMANAGER");
		userRoleService.insertUserRole(userRole);
	}

	@Override
	public void deleteUser(long userId) {
		userMapper.deleteUser(userId);
	}

	@Override
	public void deleteUserDetail(long userId) {
		userMapper.deleteUserDetail(userId);
	}

	@Override
	public void deleteUserRole(long userId) {
		userMapper.deleteUserRole(userId);
	}


	@Override
	public int getUserManagerCount(UserSearchParam searchParam) {
		searchParam.encrypt(userCriteriaEncryptor);
		int count = userMapper.getUserManagerCount(searchParam);
		searchParam.decrypt(userCriteriaEncryptor);
		return count;
	}

	@Override
	public int getUserCountByLoginId(String loginId) {
		return userMapper.getUserCountByLoginId(loginId);
	}

	@Override
	public int getUserCountByEmail(String email) {
		return userMapper.getUserCountByEmail(email);
	}

	@Override
	public int getUserCountByNickname(String nickname) {
		return userMapper.getUserCountByNickname(nickname);
	}

	@Override
	public int getUserCountByUserInfo(User user) {
		return userMapper.getUserCountByUserInfo(user);
	}

	@Override
	public int getUserCountByManagerId(String loginId) {
		return userMapper.getUserCountByManagerId(loginId);
	}

	@Override
	public int getUserCountByManagerPhoneNumber(String phoneNumber) {
		return userMapper.getUserCountByManagerPhoneNumber(phoneNumber);
	}

	@Override
	public User getUserByUserId(long userId) {
		User user = userMapper.getUserByUserId(userId);

		if (user != null) {
			// 복호화
			decryptData(user);
		}
		return user;
	}

	@Override
	public User getUserByLoginId(String loginId) {

		String deLoginStr = "";
		try {
			deLoginStr = pCrypto.Encrypt("normal", loginId, "");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}

		User user = userMapper.getUserByLoginId(deLoginStr);

		if (user != null) {
			// 복호화
			decryptData(user);
		}
		return user;
	}

	@Override
	public User getPureUserByLoginId(String loginId) {

		String deLoginStr = "";
		try {
			deLoginStr = pCrypto.Encrypt("normal", loginId, "");
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}

		User user = userMapper.getUserByLoginId(deLoginStr);

		return user;
	}

	@Override
	public int getUserTotalCount(String authority) {

		return userMapper.getUserTotalCount(authority);
	}

	@Override
	public void deleteUserByListParam(ListParam listParam) {

		if (UserUtils.isManagerLogin()) {
			long loginManagerId = UserUtils.getManagerId();

			if (listParam.getId() != null) {
				for (String userId : listParam.getId()) {
					if (StringUtils.isNotEmpty(userId)) {

						if (loginManagerId == Long.parseLong(userId)) {
							// 삭제되는 리스트에 현재 로그인한 관리자 아이디가 포함되는 지 확인
							throw new IllegalArgumentException("현재 로그인 중인 관리자는 삭제할 수 없습니다.");
						} else {
							//userMapper.deleteUser(Integer.parseInt(userId));
							userMapper.deleteManagerByUserId(Integer.parseInt(userId));
						}
					}
				}
			}
		}
	}

	@Override
	public void updateUserBuyInfoForOrder(long userId, int price) {

		UserDetail userDetail = new UserDetail();
		userDetail.setUserId(userId);
		userDetail.setBuyPrice(price);

		userMapper.updateUserBuyInfoForOrder(userDetail);
	}

	@Override
	public void getUserPasswordSearch(UserSearchParam searchParam) {

		User user = getUserByParam(searchParam);
		user.encrypt(userEncryptor);

		if (user == null) {
			throw new PageNotFoundException();
		}

		String radomPassword = UUID.randomUUID().toString().substring(0, 8);

		user.setPassword(passwordEncoder.encode(radomPassword));

		userMapper.updateUserByLoginId(user);

		user.setPassword(radomPassword);

		String templateId = "pwsearch";

		int sendCount = 0;
		if ("email".equals(searchParam.getSendType()) || "all".equals(searchParam.getSendType())) {
			MailConfig mailConfig = mailConfigService.getMailConfigByTemplateId(templateId);
			if (mailConfig != null) {
				if (StringUtils.isNotEmpty(user.getEmail())) {

					if ("Y".equals(mailConfig.getBuyerSendFlag())) {
						PwsearchMail pwsearch = new PwsearchMail(user, mailConfig, cryptor, dataMasking);
						mailConfig = pwsearch.getMailConfig();

						SendMailLog sendMailLog = new SendMailLog();
						sendMailLog.setUserId(user.getUserId());
						sendMailLog.setSendType(templateId);

						sendMailLogService.sendMail(mailConfig, sendMailLog, user.getEmail(), user.getUserName());
						sendCount++;
					}
				}
			}
		}

		if ("sms".equals(searchParam.getSendType()) || "all".equals(searchParam.getSendType())) {
			UserDetail userDetail = (UserDetail) user.getUserDetail();
			String phoneNumber = userDetail.getPhoneNumber();

			if (StringUtils.isNotEmpty(phoneNumber)) {
				Ums ums = umsService.getUms(templateId);

				if (umsService.isValidUms(ums)) {
					unifiedMessagingService.sendMessage(new Pwsearch(ums, phoneNumber, user, cryptor, dataMasking));
					sendCount++;
				}
			}
		}

		if (sendCount == 0) {
			throw new UserException("비밀번호는 변경 되었으나, 비밀번호 변경 알림에 실패 하였습니다.");
		}
	}

	@Override
	public void updateFrontUserAndUserDetail(User user) {

		this.updateUser(user);
		this.updateUserDetail((UserDetail) user.getUserDetail());

		// 로그인 한 경우 세션정보 업데이트 (우선 이렇게 처리 - 추후 변경 로직 추가 하자!!)
		if (UserUtils.isUserLogin()) {
			UserDetail userDetailInfo = getUserDetail(user.getUserId());

			// 회원 정보에 추가 정보가 있는 경우 조회하여 설정해줌
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal = auth.getPrincipal();

			if (principal instanceof UserDetails) {

				if (user != null) {
					// 복호화
					decryptData(user);
				}

				if( ((OpUserDetails) principal) != null && ((OpUserDetails) principal).getUser() != null && (user.getUserName() != null || user.getEmail() != null)) {
                    ((OpUserDetails) principal).getUser().setUserName(user.getUserName());
                    ((OpUserDetails) principal).getUser().setEmail(user.getEmail());
				}
				((OpUserDetails) principal).setUserDetail(userDetailInfo);

				// Authentication 인증 객체 업데이트, 변경시에는 인증 객체 재설정이 필요함
				Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, auth.getCredentials(), auth.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
			}
		}
	}

	@Override
	public void updateUserReceive(User user, UserDetail userDetail) {
		userMapper.updateUserDetailInfo(userDetail);

		// 로그인 한 경우 세션정보 업데이트 (우선 이렇게 처리 - 추후 변경 로직 추가 하자!!)
		if (UserUtils.isUserLogin()) {
			UserDetail userDetailInfo = getUserDetail(user.getUserId());

			// 회원 정보에 추가 정보가 있는 경우 조회하여 설정해줌
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal = auth.getPrincipal();

			if (principal instanceof UserDetails) {

				if (user != null) {
					// 복호화
					decryptData(user);
				}

				if(null != user.getUserName() || null != user.getEmail()) {
					((OpUserDetails) principal).getUser().setUserName(user.getUserName());
					((OpUserDetails) principal).getUser().setEmail(user.getEmail());
				}
				((OpUserDetails) principal).setUserDetail(userDetailInfo);

				// Authentication 인증 객체 업데이트, 변경시에는 인증 객체 재설정이 필요함
				Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, auth.getCredentials(), auth.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
			}
		}

	}

	@Override
	public void updateFrontUserAndUserDetail(User user, UserDetail userDetail) {

		UserEntity userEntity = userRepository.findByUserId(user.getUserId());

		user = passwordFlagResult(user);
		user.encrypt(userEncryptor);
		userMapper.updateUserInfo(user);

		userMapper.updateUserEtcInfo(userDetail);

		userDetail.encrypt(userDetailEncryptor);
		userMapper.updateUserDetailInfo(userDetail);

		userDetail.setCreatedDate(userEntity.getCreateDate());

		userMapper.deleteUserBirthdayAndUserId(user.getUserId());
		userDetail.setBirthdayFull(userDetail.getBirthday());
		userMapper.insertUserBirthday(userDetail);


		//관리자 정보와 동기화
		OpManagerParam opmanagerParam = new OpManagerParam();
		opmanagerParam.setLoginId(UserUtils.getLoginId());
		opmanagerParam.setEmail(user.getEmail());
		opmanagerParam.setPhoneNumber(userDetail.getPhoneNumber());
		int mngRslt = userMapper.updateManagerInfo(opmanagerParam);
		if (mngRslt > 0) {
			// [GGSR-26-369] 이력저장
			managerHistService.insertManagerHist(opmanagerParam, "H");
		}

		//관심 지자체 저장
		if(userDetail.getLocGovList() !=null) {

			//관심지자체 전체 삭제
			userMapper.deleteIntrstLocgov(UserUtils.getUserId());

			for(int i =0; i<userDetail.getLocGovList().length; i++) {
				LocGovInfo locGovInfo = new LocGovInfo();
				locGovInfo.setUserId(String.valueOf(userDetail.getUserId()));
				locGovInfo.setLocgovCode(userDetail.getLocGovList()[i]);
				joinMapper.insertIntrstLocgovInfo(locGovInfo);
			}
		}

		// 로그인 한 경우 세션정보 업데이트 (우선 이렇게 처리 - 추후 변경 로직 추가 하자!!)
		if (UserUtils.isUserLogin()) {
			UserDetail userDetailInfo = getUserDetail(user.getUserId());

			// 회원 정보에 추가 정보가 있는 경우 조회하여 설정해줌
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal = auth.getPrincipal();

			if (principal instanceof UserDetails) {

				if (user != null) {
					// 복호화
					decryptData(user);
				}

				if(null != user.getUserName() || null != user.getEmail()) {
					((OpUserDetails) principal).getUser().setUserName(user.getUserName());
					((OpUserDetails) principal).getUser().setEmail(user.getEmail());
				}
				((OpUserDetails) principal).setUserDetail(userDetailInfo);

				// Authentication 인증 객체 업데이트, 변경시에는 인증 객체 재설정이 필요함
				Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, auth.getCredentials(), auth.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
			}
		}
	}

	@Override
	public void updateUserForAdmin(User user) {

		UserDetail userDetail = (UserDetail) user.getUserDetail();

		User orgUserInfo = getUserByUserId(user.getUserId());

		if (!orgUserInfo.getEmail().equals(user.getEmail())) {

			// 가입 불가 아이디 체크
			Config config = configService.getShopConfigCache(Config.SHOP_CONFIG_ID);

			int userCount = 0;
			if (StringUtils.hasText(config.getDeniedId())) {
				String[] denyIds = StringUtils.tokenizeToStringArray(config.getDeniedId(), ",");

				for (String id : denyIds) {
					if ("".equals(id.trim())) {
						continue;
					}

					if (id.trim().equals(user.getLoginId().trim())) {
						userCount = 1;
						break;
					}
				}
			}

			// 회원 테이블에서 조회.
			if (userCount == 0) {
				userCount = userMapper.getUserCountByLoginId(user.getLoginId());
			}

			if (userCount > 0) {

			}
		}


		UserDetail orgUserDetail = (UserDetail) orgUserInfo.getUserDetail();
		/*if (StringUtils.isNotEmpty(userDetail.getNickname())) {

			if(ObjectUtils.isEmpty(orgUserDetail.getNickname())){
				orgUserDetail.setNickname("");
			}

			if (!orgUserDetail.getNickname().equals(userDetail.getNickname())) {
				if (userMapper.getUserCountByNickname(userDetail.getNickname()) > 0) {
					throw new UserException("이미 등록된 닉네임입니다.");
				}
			}
		}*/

		this.updateUser(user);
		userDetail.setConditionType("OPMANAGER");

		// 회원 그룹 변경
		if (!userDetail.getGroupCode().equals(orgUserDetail.getGroupCode())) {

			GroupSearchParam groupSearchParam = new GroupSearchParam();
			groupSearchParam.setGroupCode(userDetail.getGroupCode());

			Group group = groupMapper.getGroupDetail(groupSearchParam);
			if (group == null) {
				userDetail.setGroupCode("");
			} else {

				UserGroupLog log = new UserGroupLog();
				log.setUserId(user.getUserId());
				log.setGroupCode(userDetail.getGroupCode());
				log.setGroupName(group.getGroupName());
				log.setAdminUserName(UserUtils.getManagerName());

				userGroupMapper.insertUserGroupLog(log);

			}
		}

		// 회원 레벨이 수동 변경되는경우
		if (userDetail.getLevelId() != orgUserDetail.getLevelId()) {

			UserLevelSearchParam userLevelSearchParam = new UserLevelSearchParam();
			userLevelSearchParam.setLevelId(userDetail.getLevelId());

			UserLevel userLevel = userLevelService.getUserLevelDetail(userLevelSearchParam);
			if (userLevel == null) {
				userDetail.setLevelId(0);
				userDetail.setUserLevelExpirationDate("");
			} else {

				// 등급 유지기간을 설정된 개월수를 더해서 변경
				userDetail.setUserLevelExpirationDate(DateUtils.addMonth(DateUtils.getToday(Const.DATE_FORMAT), userLevel.getRetentionPeriod()));

				UserLevelLog log = new UserLevelLog();
				log.setUserId(user.getUserId());
				log.setGroupCode(userDetail.getGroupCode());
				log.setLevelId(userLevel.getLevelId());
				log.setLevelName(userLevel.getLevelName());
				log.setAdminUserName(UserUtils.getManagerName());
				userLevelMapper.insertUserLevelLog(log);

			}
		} else {
			userDetail.setUserLevelExpirationDate(orgUserDetail.getUserLevelExpirationDate());
		}

		this.updateUserDetail(userDetail);

	}

	@Override
	public User getUserByParam(UserSearchParam searchParam) {

		searchParam.encrypt(userCriteriaEncryptor);
		User user = userMapper.getUserByParam(searchParam);
		searchParam.decrypt(userCriteriaEncryptor);

		// 복호화
		decryptData(user);

		return user;
	}

	public User passwordFlagResult(User user){

		if(ObjectUtils.isEmpty(user.getPassword())){
			//user.setPassword(UserUtils.getUser().getPassword());
		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		return user;
	}


	@Override
	public User getUserInfoByUserName(AuthUserInfo authUserInfo) {

		return userMapper.getUserInfoByUserName(authUserInfo);
	}

	@Override
	public int getSecedeUserCount(UserSearchParam searchParam) {
		searchParam.encrypt(userCriteriaEncryptor);
		int count = userMapper.getSecedeUserCount(searchParam);
		searchParam.decrypt(userCriteriaEncryptor);

		return count;
	}

	@Override
	public List<User> getSecedeUserList(UserSearchParam searchParam) {
		searchParam.encrypt(userCriteriaEncryptor);
		List<User> users = userMapper.getSecedeUserList(searchParam);
		searchParam.decrypt(userCriteriaEncryptor);

		return users;
	}

	@Override
	public void updateSecedeFrontUserAndUserDetail(User user) {

		// 로그인아이디의가 회원 sns 고유 아이디라 재가입을 위해 탈퇴일 추가
		user.setLoginId(user.getLoginId() + "_" + DateUtils.getToday(Const.DATETIME_FORMAT));

		userMapper.updateSecedeUser(user);
		userMapper.updateSecedeUpdateUserDetail((UserDetail)user.getUserDetail());
		userSnsMapper.secedeSnsProcess(user);

		// 로그인 한 경우 세션정보 업데이트 (우선 이렇게 처리 - 추후 변경 로직 추가 하자!!)
		if (UserUtils.isUserLogin()) {
			UserDetail userDetail = getUserDetail(user.getUserId());

			// 회원 정보에 추가 정보가 있는 경우 조회하여 설정해줌
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal = auth.getPrincipal();

			if (principal instanceof UserDetails) {
				((OpUserDetails) principal).getUser().setUserName(user.getUserName());
				((OpUserDetails) principal).getUser().setEmail(user.getEmail());
				((OpUserDetails) principal).setUserDetail(userDetail);

				// Authentication 인증 객체 업데이트, 변경시에는 인증 객체 재설정이 필요함
				Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, auth.getCredentials(), auth.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(newAuth);
			}
		}
	}

	@Override
	public List<HashMap<String, String>> getAdminMenuRoleList() {
		return userMapper.getAdminMenuRoleList();
	}

	@Override
	public List<User> getManagerList(UserSearchParam searchParam) {
		searchParam.encrypt(userCriteriaEncryptor);
		List<User> managerList = userMapper.getManagerList(searchParam);
		searchParam.decrypt(userCriteriaEncryptor);

		for (User user : managerList) {


			decryptData(user);

			List<UserRole> userRoles = userMapper.getManagerRoleListByUserId(user.getUserId());
			user.setUserRoles(userRoles);
		}

		return managerList;
	}

	/*@Override
	public void sendSmsAndEmail(User user, String templateId) {

		this.sendMail(user, templateId);
		this.sendSms(user, templateId);
	}*/

	public List<ChosenUser> getChosenUserList(List<String> list){
		return userMapper.getChosenUserList(list);
	}

	public List<ChosenUser> getChosenUserListbyParam(ChosenUser chosenUser){
		return userMapper.getChosenUserListbyParam(chosenUser);
	}

	@Override
	public List<ChosenUser> getUserListForChosen(UserSearchParam searchParam){
		searchParam.encrypt(userCriteriaEncryptor);
		List<ChosenUser> users = userMapper.getUserListForChosen(searchParam);
		searchParam.decrypt(userCriteriaEncryptor);
		return users;
	}

	@Override
	public int getManagerCount(UserSearchParam searchParam) {
		searchParam.encrypt(userCriteriaEncryptor);
		int count = userMapper.getManagerCount(searchParam);
		searchParam.decrypt(userCriteriaEncryptor);

		return count;
	}

	@Override
	public User getManagerByUserId(long userId) {
		User user = getManagerBy(userId);

		List<UserRole> userRoles = userMapper.getManagerRoleListByUserId(user.getUserId());
		user.setUserRoles(userRoles);
		return user;
	}

	private User getManagerBy(long userId) {
		User user = userMapper.getManagerByUserId(userId);
		// 복호화
		decryptData(user);
		return user;
	}

	@Override
	public int getManagerCountByEmail(String email) {
		return userMapper.getManagerCountByEmail(email);
	}

	@Override
	public void updateManager(User user) {
		if (!ObjectUtils.isEmpty(user.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		user.encrypt(userEncryptor);
		userMapper.updateManager(user);

		userRoleService.deleteUserRole(user.getUserId());

		List<UserRole> userRoles = user.getUserRoles();

		String logRoles = "";

		if (ValidationUtils.isNotNull(user.getUserRoles())) {
			for (UserRole userRole : userRoles) {
				if (ValidationUtils.isNotNull(userRole.getAuthority())) {
					userRole.setUserId(user.getUserId());
					userRole.setAuthority(userRole.getAuthority());
					userRoleService.insertUserRole(userRole);
					logRoles += userRole.getAuthority() + "/"; // 로그 남길 때 권한 명 yulsun.yoo [2018-10-18]
				}
			}
		}

		UserRole userRole = new UserRole();
		userRole.setUserId(user.getUserId());
		userRole.setAuthority("ROLE_OPMANAGER");
		userRoleService.insertUserRole(userRole);

		logRoles += "ROLE_OPMANAGER";

		changeLogService.insertManagerChangeLog(RequestContextUtils.getRequestContext().getRequest(), user, ChangeTypeEnum.UPDATE);
	}

	@Override
	public void deleteManagerByListParam(ListParam listParam) {

		if (listParam.getId() != null) {
			for (String userId : listParam.getId()) {
				if (StringUtils.isNotEmpty(userId)) {
					userMapper.deleteManagerByUserId(Integer.parseInt(userId));
					userMapper.deleteUserRole(Integer.parseInt(userId));
				}
			}
		}
	}

	/**
	 * 휴면계정 안내메일 송신
	 */
	@Override
	public void sendSleepUserMail() {
		List<User> list = userMapper.getWaitSleepUser();

		if (list == null) {
			return;
		}

		if (list.isEmpty()) {
			return;
		}

		String templateId = "member_sleep";
		Config shopConfig = configMapper.getShopConfig(Config.SHOP_CONFIG_ID);

		MailConfig mailConfig = mailConfigService.getMailConfigByTemplateId(templateId);
		if (mailConfig == null) {
			return;
		}

		if (!"Y".equals(mailConfig.getBuyerSendFlag())) {
			return;
		}

		try {
			for(User mail : list) {
				MemberSleepMail memberSleepMail = new MemberSleepMail(mail, mailConfig, shopConfig, cryptor, dataMasking);
				MailConfig mConfig = memberSleepMail.getMailConfig();

				SendMailLog sendMailLog = new SendMailLog();
				sendMailLog.setUserId(mail.getUserId());
				sendMailLog.setSendType(templateId);
				if(mConfig != null) {
					sendMailLogService.sendMail(mConfig, sendMailLog, mail.getEmail(), mail.getUserName(), shopConfig);
				}
			}
		} catch (NullPointerException e) {
			log.error("메일발송 송출 에러::: {}");
		}

		userMapper.updateWaitSleepUser(list);
	}

	@Override
	public void setSleepUser() {
		List<User> list = userMapper.getUserListForSleepTarget();
		if (list == null) {
			return;
		}

		List<User> insertList = new ArrayList<>();

		int count = 0;
		for(User user : list) {
			// 복호화
			decryptDataNoMasking(user);
			encryptSleepUser(user);							//휴면유저정보 암호화

			insertList.add(user);

			if (count % 100 == 0 && count > 0) {

				userMapper.insertSleepUser(insertList);
				userMapper.updateUserToSleep(insertList);
				userMapper.updateUserDetailToSleep(insertList);
				insertList = new ArrayList<>();

			}

			count++;
		}

		if (!insertList.isEmpty()) {
			userMapper.insertSleepUser(insertList);
			userMapper.updateUserToSleep(insertList);
			userMapper.updateUserDetailToSleep(insertList);
		}

	}

	@Override
	public void wakeupUser(User currentUser) {
		User wakeUpData = userMapper.getUserForWakeup(currentUser);
		if (wakeUpData == null) {
			throw new UserException("휴면회원 정보를 찾을수 없습니다.");
		}

		//데이터 복호화
		try {
			decryptSleepUser(wakeUpData);

			// op_user, op_user_detail 관련 암호화
			wakeUpData.encrypt(userEncryptor);
			UserDetail userDetail = (UserDetail)wakeUpData.getUserDetail();
			userDetail.encrypt(userDetailEncryptor);
			wakeUpData.setUserDetail(userDetail);

		} catch (OpRuntimeException e) {
			log.error("휴면계정정보 복호화중 오류 ::  {}");
		}

		//user, userDetail 정보 업데이트
		userMapper.wakeupUser(wakeUpData);
		userMapper.wakeupUserDetail(wakeUpData);
		//OP_USER_SLEEP데이터 삭제
		userMapper.deleteSleepUser(currentUser);
	}

	private String encrypt(String message) {
		return CipherUtilsCopy.encrypt(message);
	}

	/**
	 * 휴면대상정보 암호화
	 * @param user
	 * @throws Exception
	 */
	public void encryptSleepUser(User user) {
		user.setUserName(encrypt(user.getUserName()));
		user.setEmail(encrypt(user.getEmail()));

		UserDetail userDetail = (UserDetail) user.getUserDetail();
		//userDetail.setNickname(encrypt(userDetail.getNickname()));
		userDetail.setNewPost(encrypt(userDetail.getNewPost()));
		userDetail.setPost(encrypt(userDetail.getPost()));
		userDetail.setAddress(encrypt(userDetail.getAddress()));
		userDetail.setAddressDetail(encrypt(userDetail.getAddressDetail()));
		userDetail.setTelNumber(encrypt(userDetail.getTelNumber()));
		userDetail.setPhoneNumber(encrypt(userDetail.getPhoneNumber()));
		userDetail.setFaxNumber(encrypt(userDetail.getFaxNumber()));
		userDetail.setBirthdayType(encrypt(userDetail.getBirthdayType()));
		userDetail.setBirthday(encrypt(userDetail.getBirthday()));
		//userDetail.setCompanyName(encrypt(userDetail.getCompanyName()));

		user.setUserDetail(userDetail);
	}

	/**
	 * 휴면대상정보 복호화
	 * @Date 2017-03-07
	 * @author 이상우
	 * @param user
	 * @throws Exception
	 */
	public void decryptSleepUser(User user) {
		user.setUserName(CipherUtilsCopy.decrypt(user.getUserName()));
		user.setEmail(CipherUtilsCopy.decrypt(user.getEmail()));

		UserDetail userDetail = (UserDetail) user.getUserDetail();
		//userDetail.setNickname(CipherUtils.decrypt(userDetail.getNickname()));
		userDetail.setNewPost(CipherUtilsCopy.decrypt(userDetail.getNewPost()));
		userDetail.setPost(CipherUtilsCopy.decrypt(userDetail.getPost()));
		userDetail.setAddress(CipherUtilsCopy.decrypt(userDetail.getAddress()));
		userDetail.setAddressDetail(CipherUtilsCopy.decrypt(userDetail.getAddressDetail()));
		userDetail.setTelNumber(CipherUtilsCopy.decrypt(userDetail.getTelNumber()));
		userDetail.setPhoneNumber(CipherUtilsCopy.decrypt(userDetail.getPhoneNumber()));
		userDetail.setFaxNumber(CipherUtilsCopy.decrypt(userDetail.getFaxNumber()));
		userDetail.setBirthdayType(CipherUtilsCopy.decrypt(userDetail.getBirthdayType()));
		userDetail.setBirthday(CipherUtilsCopy.decrypt(userDetail.getBirthday()));
		//userDetail.setCompanyName(CipherUtils.decrypt(userDetail.getCompanyName()));

		user.setUserDetail(userDetail);
	}

	@Override
	public List<OpmanagerCount> getOpmanagerUserCountAll() {

		// SKC 쿼리 튜닝
		Map<String, String> param = new HashMap<>();
		param.put("today", LocalDateUtils.localDateToString(LocalDate.now()));
		param.put("days7ago", LocalDateUtils.localDateToString(LocalDate.now().minusWeeks(1)));


		return userMapper.getOpmanagerUserCountAll(param);
	}

	@Override
	public void updateUserDetailForOrder(Buyer buyer) {
		userMapper.updateUserDetailForOrder(buyer);

	}

	@Override
	public void updateTempPasswordForManager(long userId) throws UserException{
		User user = getManagerBy(userId);

		if (user.getPhoneNumber() == null) {
			throw new UserException("핸드폰 번호가 없습니다.");
		} else {

			String tempPassword = RandomStringUtils.getRandomString("!",7,10);

			User tempUser = new User();

			tempUser.setUserId(userId);
			tempUser.setPasswordType("T");
			tempUser.setPassword(passwordEncoder.encode(tempPassword));
			tempUser.setPasswordExpiredDate(this.getPasswordExpiredDate());

			int count = userMapper.updatePasswordForManager(tempUser);

			if (count > 0) {

				SmsConfig smsConfig = new SmsConfig();
				smsConfig.setBuyerSendFlag("Y");
				smsConfig.setBuyerContent("임시비밀번호\n"+tempPassword);
				smsConfig.setSmsType("sms");

				SendSmsLog sendSmsLog = new SendSmsLog();

				sendSmsLog.setContent(smsConfig.getBuyerContent());
				sendSmsLog.setSendType("MANAGER_TEMP_PASSOWRD");
				sendSmsLogService.sendSms(smsConfig, sendSmsLog, user.getPhoneNumber());

			}
		}
	}

	@Override
	public void updatePasswordForManager(long userId, String password, String changePassword) throws UserException {

		User user = getManagerBy(userId);

		if (passwordEncoder.matches(password, user.getPassword())) {

			if (passwordEncoder.matches(changePassword, user.getPassword())) {
				throw new UserException("변경 하려는 비밀번호가 동일합니다.");
			}

			// 바꾸려는 비밀번호가 기존의 비밀번호와 동일한지 확인.
			PasswordLog passwordLog = new PasswordLog();
			passwordLog.setUserId(userId);

			List<String> passwords = passwordLogService.getPasswordListById(passwordLog);
			for (String pw : passwords) {
				if (passwordEncoder.matches(changePassword, pw)) {
					throw new UserException("기존에 사용하셨던 비밀번호로는 변경할 수 없습니다.");
				}
			}

			User tempUser = new User();

			tempUser.setUserId(userId);
			tempUser.setPasswordType("N");
			tempUser.setPassword(passwordEncoder.encode(changePassword));
			tempUser.setPasswordExpiredDate(this.getPasswordExpiredDate());

			int count = userMapper.updatePasswordForManager(tempUser);

			if (count == 0) {
				throw new UserException("비밀번호가 변경되지 않았습니다.");
			}

		} else {
			throw new UserException("비밀번호가 일치하지 않습니다.");
		}
	}

	@Override
	public void updatePasswordForUser(long userId, String passowrd, String changePassowrd) throws UserException {
		User user = getUserByUserId(userId);

		if (passwordEncoder.matches(passowrd, user.getPassword())) {

			if (passwordEncoder.matches(changePassowrd, user.getPassword())) {
				throw new UserException("변경 하려는 비밀번호가 동일합니다.");
			}

			User tempUser = new User();

			tempUser.setUserId(userId);
			tempUser.setPasswordType("N");
			tempUser.setPassword(passwordEncoder.encode(changePassowrd));
			tempUser.setPasswordExpiredDate(this.getPasswordExpiredDate());

			int count = userMapper.updatePasswordForUser(tempUser);

			if (count == 0) {
				throw new UserException("비밀번호가 변경되지 않았습니다.");
			}

		} else {
			throw new UserException("비밀번호가 일치하지 않습니다.");
		}
	}

	@Override
	public void updatePasswordExpiredDateForUser(long userId) throws UserException {

		try {


			User user = new User();

			user.setUserId(userId);
			user.setPasswordExpiredDate(this.getPasswordExpiredDate());

			if (userMapper.updatePasswordExpiredDateForUser(user) == 0) {
				throw new UserException("비밀번호관련 정보가 변경에 실패 했습니다.");
			}

		} catch (NullPointerException e) {
			log.error("UserServiceImpl.updatePasswordExpiredDateForUser ERROR: {}");
			throw new UserException("비밀번호관련 정보가 변경에 실패 했습니다.");
		}
	}

	@Override
	public String getPasswordExpiredDate() {

		String passwordExpiredDate = "";

		try {
			String lifeTimePassword = configIsmsService.getIsmsConfigValueByKey("LIFE_TIME_PASSWORD");
			passwordExpiredDate = DateUtils.addDay(DateUtils.getToday(Const.DATE_FORMAT), StringUtils.string2integer(lifeTimePassword));
		} catch (NullPointerException e) {
			passwordExpiredDate = DateUtils.addDay(DateUtils.getToday(Const.DATE_FORMAT), 180);
			log.error("getPasswordExpiredDate ERROR: {}");
		}

		return passwordExpiredDate;
	}

	@Override
	public void updatePasswordExpiredDateForManager(long userId) throws UserException {

		try {
			User user = new User();

			user.setUserId(userId);
			user.setPasswordExpiredDate(this.getPasswordExpiredDate());

			if (userMapper.updatePasswordExpiredDateForManager(user) == 0) {
				throw new UserException("비밀번호관련 정보가 변경에 실패 했습니다.");
			}

		} catch (NullPointerException e) {
			log.error("UserServiceImpl.updatePasswordExpiredDateForManager ERROR: {}");
			throw new UserException("비밀번호관련 정보가 변경에 실패 했습니다.");
		}
	}

	@Override
	public void insertLoginSessionForManager(HttpSession session, long userId) {
		// 삭제 추가
		managerLoginRepository.deleteManagerLoginsByUserId(userId);

		ManagerLogin loginSession = new ManagerLogin(userId, session.getId());
		managerLoginRepository.save(loginSession);
	}

	@Override
	public void deleteLoginSessionForManager(long userId) {
		managerLoginRepository.deleteManagerLoginsByUserId(userId);
	}


    @Override
    public void saveLoginSessionForManager(HttpSession session, long userId) {
        saveLoginSessionForManager(session.getId(), userId);
    }

    @Override
    public void saveLoginSessionForManager(String sessionId, long userId) {
        managerLoginRepository.deleteManagerLoginsByUserId(userId);

        ManagerLogin loginSession = new ManagerLogin(userId, sessionId);
        managerLoginRepository.save(loginSession);
    }



	@Override
	public List<ManagerLogin> getLoginSessionForManagerByUserId(long userId) {
		return  managerLoginRepository.findManagerLoginsByUserId(userId);
	}

	@Override
	public String checkDuplication(User user) {
		String checkResult = "";

		// 가입 불가 아이디 체크
		Config config = configService.getShopConfigCache(Config.SHOP_CONFIG_ID);

		int userCount = 0;
		if (StringUtils.hasText(config.getDeniedId())) {
			String[] denyIds = StringUtils.tokenizeToStringArray(config.getDeniedId(), ",");

			for (String userId : denyIds) {
				if ("".equals(userId.trim())) {
					continue;
				}

				if (userId.trim().equals(user.getLoginId().trim())) {
					userCount = 1;
					checkResult = "isOccupiedId";
					break;
				}
			}
		}

		// 회원 테이블에서 조회.
		if (userCount == 0) {
			userCount = getUserCountByUserInfo(user);
		}

		if (userCount > 0) {
			checkResult = "isOccupiedId";
		}

		return checkResult;
	}

	@Override
	public void updatePasswordByAsisUser(long userId, String password) {
		User user = getUserByUserId(userId);

		if (user != null && "PASSWORD".equals(user.getPassword())) {

			user.setPassword(passwordEncoder.encode(password));
			user.setPasswordExpiredDate(this.getPasswordExpiredDate());

			userMapper.updatePasswordByAsisUser(user);
		}
	}

	@Override
	public void updateLockForManager() throws Exception{

		String unusedManager = configIsmsService.getIsmsConfigValueByKey("UNUSED_MANAGER");

		if (!ObjectUtils.isEmpty(unusedManager)) {
			userMapper.updateLockForManager(StringUtils.string2integer(unusedManager));
		}

	}

	@Override
	public void saveUserAgree(List<AgreeDto> dtos, User user) {
		if (dtos != null && !dtos.isEmpty()) {

			for (AgreeDto dto :dtos) {
				Policy policy = policyService.getCurrentPolicyByType(dto.getType());
				userAgreeRepository.save(new UserAgree(policy, user.getLoginId(), user.getUserId(), dto.isAgree()));
			}

		}
	}

	@Override
	public LocalDateTime getAuthExpiredDateById(long userId) {

		String date = userMapper.getAuthExpiredDateById(userId);

		try {
			if (StringUtils.hasText(date)) {
				return LocalDateUtils.getLocalDateTime(date);
			}
		} catch (NullPointerException e) {
			log.error("getAuthExpiredDateById error [{}]"+userId);
		}

		return null;
	}

	@Override
	public void updateAuthExpiredDateById(long userId) {

		Map<String, Object> map = new HashMap<>();
		LocalDateTime now = LocalDateTime.now();

		String authExpiredDate
				= LocalDateUtils.localDateTimeToString(now.plusSeconds(SalesonProperty.getSalesonAuthManagerTime()), Const.DATETIME_FORMAT);
		map.put("userId", userId);
		map.put("authExpiredDate", authExpiredDate);
		userMapper.updateAuthExpiredDateById(map);
	}

	/**
	 * 컬럼 데이터 복호화
	 * @param user
	 */
	private void decryptData(User user) {
		// 복호화
		if (user != null) {
			user.decrypt(userEncryptor, ShopUtils.needMasking());

			if (user.getUserDetail() != null) {
				UserDetail userDetail = (UserDetail) user.getUserDetail();
				userDetail.decrypt(userDetailEncryptor, ShopUtils.needMasking());
			}
		}
	}

	/**
	 * 컬럼 데이터 복호화 (마스킹 처리 하지 않음)
	 * @param user
	 */
	private void decryptDataNoMasking(User user) {
		// 복호화
		if (user != null) {
			user.decrypt(userEncryptor, false);

			if (user.getUserDetail() != null) {
				UserDetail userDetail = (UserDetail) user.getUserDetail();
				userDetail.decrypt(userDetailEncryptor, false);
			}
		}
	}

	@Override
	public void setUserDetailHyphen(User user) {

		if (user != null) {
			UserDetail detail = (UserDetail)user.getUserDetail();
			if (detail != null) {
				detail.processHyphen();
				user.setUserDetail(detail);
			}
		}
	}

	@Override
	public UserEntity getUserByUserKey(String userKey) {
//		User user = userMapper.getUserByUserKey(userKey);

		UserEntity user = userRepository.findByUserKey(userKey);

		return user;
	}

	@Override
	public UserEntity getUserByMberCi(String mberCi, String userKey) {
//		User user = userMapper.getUserByMberCi(mberCi);

		UserEntity user = userRepository.findByMberCi(mberCi);

		if (user != null) {
			user.setUserKey(userKey);
			user = userRepository.save(user);
		}

		return user;
	}

	@Override
	public void updateUserKeyStatusCode(long userId, String leaveCode, String leaveReason) {

		GeneralCustomerSecede generalCustomerSecede = new GeneralCustomerSecede();
		generalCustomerSecede.setUserId(userId);
		generalCustomerSecede.setLeaveCode(leaveCode);
		generalCustomerSecede.setLeaveReason(leaveReason);
//		generalCustomerSecede.setLeaveUserId(userId);


		/*************************************************************************
		 *  1. 당해년도 총 기부납부금액 저장
		 *************************************************************************/
		// 1-2. 회원 정보 조회
		GeneralCustomerSearchParam searchParam = new GeneralCustomerSearchParam();
		searchParam.setUserId(generalCustomerSecede.getUserId());
		GeneralCustomer customer = generalCustomerMapper.getGeneralCustomerDetailsNotStatusCode(searchParam);

		if(customer == null) {
			throw new UserException("사용자 정보가 없습니다.");
		};

		Integer totalCntrAmt = generalCustomerMapper.selectTotalCntrAmt(generalCustomerSecede.getUserId());
		if(CommonUtils.intNvl(totalCntrAmt) > 0 && !"".equals(CommonUtils.dataNvl(customer.getMberCi()))) {
			SecedeCntrAmt secedeCntrAmt = new SecedeCntrAmt();
			secedeCntrAmt.setUserId(generalCustomerSecede.getUserId());
			secedeCntrAmt.setMberCi(customer.getMberCi());
			secedeCntrAmt.setCntrAmt(totalCntrAmt);
			secedeCntrAmt.setLoginUserId(UserUtils.getUser().getUserId());
			generalCustomerMapper.insertTotalCntrAmt(secedeCntrAmt);
		}

		/*************************************************************************
		 *  2. 회원 탈퇴 처리
		 *************************************************************************/
		// 2-1. 회원 탈퇴
		generalCustomerMapper.updateSecedeGeneralCustomer(generalCustomerSecede.getUserId());

		// 2-2. 회원 상세 탈퇴
		generalCustomerMapper.updateSecedeGeneralCustomerDetail(generalCustomerSecede);

		// 2-3. 관심 지자체 삭제
		generalCustomerMapper.deleteSecedeGeneralCustomerIntrstLocgov(generalCustomerSecede.getUserId());

		// 2-4. 관심 답례품 삭제
		generalCustomerMapper.deleteSecedeGeneralCustomerIntrstRtnpsnt(generalCustomerSecede.getUserId());


		/*************************************************************************
		 *  3. 관리자 탈퇴 처리
		 *************************************************************************/
		// 3-1. 관리자 탈퇴 (삭제)
		generalCustomerMapper.deleteManagerPasswordLog(generalCustomerSecede.getUserId());
		generalCustomerMapper.deleteSecedeManager(generalCustomerSecede.getUserId());


		/*************************************************************************
		 *  4. 사용자 권한 삭제
		 *************************************************************************/
		// 4-1. 사용자(회원, 관리자) 권한 삭제
		generalCustomerMapper.deleteSecedeUserRole(generalCustomerSecede.getUserId());

	}

	@Override
	public String getCryptoDecByLoginId(String loginId) {
		return userMapper.getCryptoDecByLoginId(loginId);
	}

	@Override
	public List<LocGovInfo> getIntrstLocgovList(long userId) {
		return userMapper.getIntrstLocgovList(userId);
	}

	@Override
	public UserModifyInfo getUserModifyInfo(long userId) {
		return userMapper.getUserModifyInfo(userId);
	}

	@Override
	public Boolean confirmUserPassword(long userId, String presentPassword) {
		Boolean confrimUserPassword = false;
		try {
			User presentUser = new User();
			presentUser.setUserId(userId);
			presentUser.setPassword(passwordEncoder.encode(pCrypto.Encrypt("hash.5", presentPassword, "")));
			int isConfirmedPassword = userMapper.confirmUserPassword(presentUser);
			if (isConfirmedPassword == 1) {
				confrimUserPassword = true;
			}
		} catch (UnsupportedEncodingException e) {
			throw new UserException("오류가 발생하였습니다.");
		}

		return confrimUserPassword;
	}

	@Override
	public void updateUserPassword(long userId, String changePassowrd) {
		User user = getUserByUserId(userId);

		try {
			if (passwordEncoder.matches(pCrypto.Encrypt("hash.5", changePassowrd, ""), user.getPassword())) {
			throw new UserException("변경 하려는 비밀번호가 동일합니다.");
		}

		User tempUser = new User();

		tempUser.setUserId(userId);
		tempUser.setPasswordType("N");
			tempUser.setPassword(passwordEncoder.encode(pCrypto.Encrypt("hash.5", changePassowrd, "")));
		tempUser.setPasswordExpiredDate(this.getPasswordExpiredDate());

		int count = userMapper.updatePasswordForUser(tempUser);

		if (count == 0) {
			throw new UserException("비밀번호가 변경되지 않았습니다.");
		}

		//사용자 로그인 failcount 초기화
		securityService.updateClearLoginFailCountForUser(user.getLoginId());

		//관리자동기화
		User manager = managerReqeustMapper.getManagerByLoginId(user.getLoginId());
		if(manager!=null) {
			int resultCnt = userMapper.updatePasswordForManager(tempUser);
			if(resultCnt == 1) {
				// 1. 관리자 조회
				TempPasswordChange tempPasswordChange = new TempPasswordChange();
				tempPasswordChange.setLoginId(manager.getLoginId());
				securityService.updateClearLoginFailCountForManager(tempPasswordChange.getLoginId()); //매니저
				passwordLogService.insertManagerPasswordLog(manager, false); //관리자 비밀번호 암호화
			}
		}

		} catch (UnsupportedEncodingException e) {
			throw new UserException("오류가 발생하였습니다.");
		}
	}

	@Override
	public int modifyMobileAuth(AuthInfo authInfo) {
		authInfo.setUserId(UserUtils.getUserId());
		int result = 0;
		result+=userMapper.modifyMobileAuth(authInfo);
		result+=userMapper.modifyMobileAuthDetail(authInfo);
		return result;
	}

	/**
	 * 관리자 > 비밀번호 변경 (관리자 비밀번호 초기화, 초기 비밀번호 설정, 관리자 비밀번호 변경안내 - 3개월)
	 * @param tempPasswordChange
	 * @return
	 */
	@Override
	public String updateTempPasswordChange(HttpServletRequest request, TempPasswordChange tempPasswordChange) {
		String code = "FAIL";
		String encCurrentPassword = "";
		String encPassword = "";

		// 0. 패스워드 암호화
		try {
			encCurrentPassword = pCrypto.Encrypt("hash.5", tempPasswordChange.getCurrentPassword(), "");
			encPassword = pCrypto.Encrypt("hash.5", tempPasswordChange.getPassword(), "");
		} catch (UnsupportedEncodingException e) {
			log.error("ERROR: {}", getClass().getName() + " :: updateTempPasswordChange UnsupportedEncodingException ===========");
		}

		// 1. 관리자 조회
		User manager = managerReqeustMapper.getManagerByLoginId(tempPasswordChange.getLoginId());

		// 1-1. [유효성] 관리자 정보
		if(manager == null) {
			code = "ERR_NOT_FOUND";
			return code;
		}

		// 1-2. [유효성] 관리자 비밀번호 확인
		if(!passwordEncoder.matches(encCurrentPassword, manager.getPassword())) {
			code = "ERR_PASS_MATCH";
			return code;
		}

		// 1-3. [유효성] 기존 비밀번호와 변경된 비밀번호가 동일하면 수정 불가
		if(passwordEncoder.matches(encPassword, manager.getPassword())) {
			code = "ERR_PASS_CHANGE";
			return code;
		}

		// 2. 비밀번호 수정 데이터 셋팅
		User tempUser = new User();
		tempUser.setUserId(manager.getUserId());
		tempUser.setPasswordType("N");
		tempUser.setPassword(encPassword);
		tempUser.setPasswordExpiredDate(this.getPasswordExpiredDate());

		// 3. 관리자 비밀번호 수정
		userMapper.updatePasswordForManager(tempUser);

		// 4. 회원 비밀번호 수정
		userMapper.updatePasswordForUser(tempUser);

		// 5. 로그 추가
		try {
			securityService.updateClearLoginFailCountForManager(tempPasswordChange.getLoginId());
			securityService.updateClearLoginFailCountForUser(tempPasswordChange.getLoginId());
			passwordLogService.insertManagerPasswordLog(manager, false);
		} catch (OpRuntimeException e) {
			log.error("ERROR: {}", getClass().getName() + " :: updateTempPasswordChange Exception ===========");
		}

		// 6. 결과
		code = "SUCC";

		return code;
	}

    @Override
    public void saveLoginSessionForUser(String sessionId, long userId) {
        userLoginRepository.deleteUserLoginsByUserId(userId);
        userLoginRepository.save(new UserLogin(userId, sessionId));
    }

    @Override
    public List<UserLogin> getLoginSessionForUserByUserId(long userId) {
        return userLoginRepository.findUserLoginsByUserId(userId);
    }

	@Override
	public String getLoginIdByMobileAuth(AuthInfo authInfo) {
		return userMapper.getLoginIdByMobileAuth(authInfo);
	}

	@Override
	public void changeUserPasswordForNoLogin(ChangePasswordForNoLoginUser changePassword){
		User user = getUserByNoLoginUser(changePassword);

		if (user==null) {
			throw new UserException("유저정보가 없습니다.");
		}

		try {
			if (passwordEncoder.matches(pCrypto.Encrypt("hash.5", changePassword.getPassword(), ""), user.getPassword())) {
			throw new UserException("변경 하려는 비밀번호가 동일합니다.");
		}

		User tempUser = new User();

		tempUser.setUserId(user.getUserId());
		tempUser.setPasswordType("N");
			tempUser.setPassword(passwordEncoder.encode(pCrypto.Encrypt("hash.5", changePassword.getPassword(), "")));
		tempUser.setPasswordExpiredDate(this.getPasswordExpiredDate());

		int count = userMapper.updatePasswordForUser(tempUser);

		if (count == 0) {
			throw new UserException("비밀번호가 변경되지 않았습니다.");
		}

		//사용자 로그인 failcount 초기화
		securityService.updateClearLoginFailCountForUser(user.getLoginId());

		//관리자동기화
		User manager = managerReqeustMapper.getManagerByLoginId(user.getLoginId());
		if(manager!=null) {
			int resultCnt = userMapper.updatePasswordForManager(tempUser);
			if(resultCnt == 1) {
				// 1. 관리자 조회
				TempPasswordChange tempPasswordChange = new TempPasswordChange();
				tempPasswordChange.setLoginId(manager.getLoginId());
				securityService.updateClearLoginFailCountForManager(tempPasswordChange.getLoginId()); //매니저
				passwordLogService.insertManagerPasswordLog(manager, false); //관리자 비밀번호 암호화
			}
		}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			throw new UserException("오류가 발생하였습니다.");
//			log.error(e.getMessage());
		}
	}

	@Override
	public User getUserByNoLoginUser(ChangePasswordForNoLoginUser changePassword) throws UserException{
		User user = userMapper.getUserByNoLoginUser(changePassword);

		if(user !=null) {
			//복호화
			decryptData(user);
		}
		return user;
	}

	@Override
	public void changeUserPasswordLater(ChangePasswordForNoLoginUser changePassword) throws UserException{
		User user = getUserByNoLoginUser(changePassword);

		if (user==null) {
			throw new UserException("유저정보가 없습니다.");
		}

		changePassword.setUserId(user.getUserId());
		changePassword.setPasswordExpiredDate(this.getPasswordExpiredDate());

		int count = userMapper.changeUserPasswordLater(changePassword);

		if(count == 0) {
			throw new UserException("비밀번호 유효일자가 변경되지 않았습니다.");
		}
	}

	@Override
	public String getUserKeyInfo(long user) throws Exception {
		return userMapper.getUserKeyInfo(user);
	}

	@Override
	public String getUserLoginPathInfo(long user) throws Exception {
		return userMapper.getUserLoginPathInfo(user);
	}

	/**
	 * ci정보로 고객 정보 조회
	 * @param user
	 * @return
	 */
	@Override
	public UserInfoByCI getUserInfoByCi(AuthInfo authInfo) {
		return userMapper.getUserInfoByCi(authInfo);
	}

	/**
	 * PKI 폼 유효성 조회
	 * @param pkiManagerLogin
	 * @return
	 */
	@Override
	public PkiManagerLoginResult getPkiManagerFormValid(PkiManagerLogin pkiManagerLogin) {
		PkiManagerLoginResult result = new PkiManagerLoginResult();
		result.setCode("FAIL");

		PkiManagerLogin userInfo = null;
		if(pkiManagerLogin.getCharge().equals("loc")) { // 지자체 담당자
			userInfo = userMapper.getPkiManagerFormValid(pkiManagerLogin);
			if(userInfo != null) {
				if("".equals(CommonUtils.dataNvl(userInfo.getPhoneNumber()))) {
					result.setCode("ERR_PHONE");

				} else if ("".equals(CommonUtils.dataNvl(userInfo.getMberCi()))) {
					result.setCode("ERR_CI");
				} else if(CommonUtils.dataNvl(pkiManagerLogin.getPhoneNumber()).equals(CommonUtils.dataNvl(userInfo.getPhoneNumber()))) {
					result.setCode("SUCC");
					result.setUserId(userInfo.getUserId());
					result.setMberDn(userInfo.getMberDn());
					result.setMberFinDn(userInfo.getMberFinDn());
					//result.setMberDn((userInfo.getMberDn() == null || "".equals(userInfo.getMberDn())) ? "" : userInfo.getMberDn());
					//result.setMberFinDn((userInfo.getMberFinDn() == null || "".equals(userInfo.getMberDn())) ? "" : userInfo.getMberFinDn());
					result.setMberCi(userInfo.getMberCi());
				}
			}
		} else if (pkiManagerLogin.getCharge().equals("off")) { // 오프라인 담당자
			userInfo = userMapper.getPkiManagerFormValidOff(pkiManagerLogin);
//			userInfo = userMapper.getPkiManagerFormValid(pkiManagerLogin);
			if(userInfo != null) {
				if("".equals(CommonUtils.dataNvl(userInfo.getPhoneNumber()))) { //userInfo.getTelNumber()
					result.setCode("ERR_PHONE");

//				} else if ("".equals(CommonUtils.dataNvl(userInfo.getMberCi()))) {
//					result.setCode("ERR_CI");
				} else if(CommonUtils.dataNvl(pkiManagerLogin.getPhoneNumber()).equals(CommonUtils.dataNvl(userInfo.getPhoneNumber()))) { //userInfo.getTelNumber()
					result.setCode("SUCC");
					result.setUserId(userInfo.getUserId());
					result.setMberDn(userInfo.getMberDn());
					result.setMberFinDn(userInfo.getMberFinDn());
					result.setMberCi(userInfo.getMberCi());
				}
			}
		}


		if (userInfo.getUserId() != null) {
			this.updatePasswordExpiredDateForManager(userInfo.getUserId());
		}

		return result;
	}

	/**
	 * 회원 DN 수정
	 * @param pkiManagerLogin
	 * @return
	 */
	@Override
	public PkiManagerLoginResult updatePkiManagerMberDn(PkiManagerLogin pkiManagerLogin) throws Exception{
		PkiManagerLoginResult result = new PkiManagerLoginResult();
		result.setCode("FAIL");

		// 1. 회원 DN 등록일 경우 기존에 내역 조회 후 삭제 처리
		if(!"".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberDn()))) {

			// 1-1. 회원 DN을 사용하는 회원 조회
			List<User> userList = userMapper.getUserListByMberDn(pkiManagerLogin.getMberDn());

			// 1-2. 논리적 삭제 처리
			if(userList != null && userList.size() > 0) {
				PkiManagerLogin tempPkiManagerLogin = null;

				for(User user : userList) {
					tempPkiManagerLogin = new PkiManagerLogin();
					tempPkiManagerLogin.setUserId(user.getUserId());
					tempPkiManagerLogin.setMberDn("");
					userMapper.updatePkiManagerMberDn(tempPkiManagerLogin);
				}
			}

			if(userMapper.updatePkiManagerMberDn(pkiManagerLogin) > 0) {
				result.setCode("SUCC");
			}
		// 2. 회원 FINDN 등록일 경우 기존에 내역 조회 후 삭제 처리
		}else if(!"".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberFinDn()))) {

			// 2-1. 회원 FINDN을 사용하는 회원 조회
			List<User> userList = userMapper.getUserListByMberFinDn(pkiManagerLogin.getMberFinDn());

			// 2-2. 논리적 삭제 처리
			if(userList != null && userList.size() > 0) {
				PkiManagerLogin tempPkiManagerLogin = null;

				for(User user : userList) {
					tempPkiManagerLogin = new PkiManagerLogin();
					tempPkiManagerLogin.setUserId(user.getUserId());
					//tempPkiManagerLogin.setMberDn("");
					tempPkiManagerLogin.setMberFinDn("");
					userMapper.updatePkiManagerMberFinDn(tempPkiManagerLogin);
				}
			}

			if(userMapper.updatePkiManagerMberFinDn(pkiManagerLogin) > 0) {
				result.setCode("SUCC");
			}
		} else {// 3. 회원 DN 삭제 & 결과값 / mberDn && mberFinDn null
			if(userMapper.updatePkiManagerMberDn(pkiManagerLogin) > 0) {
				result.setCode("SUCC");
			}
		}

		return result;
	}

	/**
	 * 회원 DN,FinDN 삭제
	 * @param pkiManagerLogin
	 * @return
	 */
	@Override
	public PkiManagerLoginResult updateDeletePkiManagerMberDn(PkiManagerLogin pkiManagerLogin) throws Exception{
		PkiManagerLoginResult result = new PkiManagerLoginResult();
		result.setCode("FAIL");

		// 1. 회원 DN
		if("P".equals(pkiManagerLogin.getType()) && !"".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberDn()))) {

			if(userMapper.deletePkiManagerMberDn(pkiManagerLogin) > 0) {
				result.setCode("SUCC");
			}

		}

		// 2. 회원 FINDN
		if("F".equals(pkiManagerLogin.getType()) && !"".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberFinDn()))) {

			if(userMapper.deletePkiManagerMberFinDn(pkiManagerLogin) > 0) {
				result.setCode("SUCC");
			}
		}

		return result;
	}

	/**
	 * 관리자 로그인 유효성 검사
	 * @param request
	 * @param pkiManagerLogin
	 * @return
	 */
	@Override
	public PkiManagerLoginResult savePkiManagerLoginUserValid(HttpServletRequest request, PkiManagerLogin pkiManagerLogin, HttpSession session) throws Exception {
		PkiManagerLoginResult result = new PkiManagerLoginResult();
		result.setCode("FAIL");

		try {
			String loginId = userMapper.getLoginId("DN", pkiManagerLogin.getMberDn());
			if(loginId == null) loginId = userMapper.getLoginId("FINDN", pkiManagerLogin.getMberFinDn());
			session.setAttribute("OP_LAST_USERNAME", loginId);
		} catch (NullPointerException e) {
			log.error(e.getMessage());
		}

		// 1. DN 정보 확인
		if(("".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberDn()))) && ("".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberFinDn())))) {
		//if("".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberDn()))) {
			result.setCode("ERR_DN");
			return result;
		}

		// 2. 사용자 정보 확인
		User user = null;
		User userFin = null;

		if(!"".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberDn()))) {
			user = userMapper.getUserByMberDn(pkiManagerLogin.getMberDn());
		}
		if(!"".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberFinDn()))) {
			userFin = userMapper.getUserByMberFinDn(pkiManagerLogin.getMberFinDn());
		}

		if(user == null && userFin == null) {
			if(!"".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberCi()))) {
				User userCi = userMapper.getUserByMberCi(pkiManagerLogin.getMberCi());
				if(userCi == null) {
					result.setCode("ERR_USER_NOT");
					return result;
				}else {
					result.setCode("ERR_USER_NOT_CI");
					return result;
				}
			}else {
				result.setCode("ERR_USER_NOT");
				return result;
			}
		}

		/*User user = userMapper.getUserByMberDn(pkiManagerLogin.getMberDn());
		if(user == null) {
			result.setCode("ERR_USER_NOT");
			return result;
		}*/
		long userId= 0;
		String pass = "";
		String loginId = "";
		if(user != null) {
			userId = user.getUserId();
			pass = user.getPassword();
			loginId = user.getLoginId();
		}
		if(userFin != null) {
			userId = userFin.getUserId();
			pass = userFin.getPassword();
			loginId = userFin.getLoginId();
		}

		result.setUserId(userId);
		result.setPassword(pass);

		// 3. 관리자 정보 확인
		User manager = managerReqeustMapper.getManagerByLoginId(loginId);
		if(manager == null) {
			result.setCode("ERR_MNG_NOT");
			if(user != null) {
				result.setMberDn(pkiManagerLogin.getMberDn());
			}
			if(userFin != null) {
				result.setMberFinDn(pkiManagerLogin.getMberFinDn());
			}
			return result;
		}

		// 4. 관리자 상태값 체크
		if(!"9".equals(String.valueOf(manager.getStatusCode()))) {
			result.setStatusCode(String.valueOf(manager.getStatusCode()));
			result.setCode("ERR_STS");
			return result;
		}

		String txt = this.setSessionByManager(request, manager);
		result.setCode(txt);

		return result;
	}

	/**
	 * 회원 DN 수정(사용자)
	 * @param signInfo
	 * @return
	 */
	@Override
	public void signRegisterForUser(SignInfo signInfo) {

		if(ObjectUtils.isEmpty(signInfo.getMberDn())){
			throw new UserException("인증서 정보가 없습니다.");
		}

		//사용자 정보 조회
		SignInfo userInfo = userMapper.getUserForSignRegister(signInfo);

		if(userInfo==null) {
			throw new UserException("유저정보가 없습니다.");
		}

		if(!StringUtils.hasText(userInfo.getPhoneNumber())
				|| 	!userInfo.getPhoneNumber().equals(signInfo.getPhoneNumber())
			) {
			throw new UserException("휴대폰번호가 존재하지 않습니다.");
		}

		signInfo.setUserId(userInfo.getUserId());
		int result = userMapper.updateUserSignRegister(signInfo);

		if(result == 0 ) {
			throw new UserException("인증서가 등록되지 않았습니다.");
		}
	}

	@Override
	public void signRemoveForUser(SignInfo signInfo) {
		//사용자 정보 조회
		SignInfo userInfo = userMapper.getUserForSignRegister(signInfo);

		if(userInfo==null) {
			throw new UserException("유저정보가 없습니다.");
		}

		if(!StringUtils.hasText(userInfo.getPhoneNumber())
			|| 	!userInfo.getPhoneNumber().equals(signInfo.getPhoneNumber())
			) {
			throw new UserException("휴대폰번호가 존재하지 않습니다.");
		}

		if(!StringUtils.hasText(userInfo.getMberDn())) {
			throw new UserException("등록된 인증서가 없습니다.");
		}

		signInfo.setUserId(userInfo.getUserId());
		int result = userMapper.signRemoveForUser(signInfo);

		if(result == 0 ) {
			throw new UserException("인증서가 폐기되지 않았습니다.");
		}

	}

	@Override
	public UserInfoBySign checkSignForUser(SignInfo signInfo) {
		return userMapper.checkSignForUser(signInfo);
	}

	@Override
	public void changeUserPwdForSignNoLogin(ChangePasswordForNoLoginUser changePassword) {
		User user = getUserBySign(changePassword);

		if (user==null) {
			throw new UserException("유저정보가 없습니다.");
		}

		try {
			if (passwordEncoder.matches(pCrypto.Encrypt("hash.5", changePassword.getPassword(), ""), user.getPassword())) {
			throw new UserException("변경 하려는 비밀번호가 동일합니다.");
		}


		User tempUser = new User();

		tempUser.setUserId(user.getUserId());
		tempUser.setPasswordType("N");
			tempUser.setPassword(passwordEncoder.encode(pCrypto.Encrypt("hash.5", changePassword.getPassword(), "")));
		tempUser.setPasswordExpiredDate(this.getPasswordExpiredDate());

		int count = userMapper.updatePasswordForUser(tempUser);

		if (count == 0) {
			throw new UserException("비밀번호가 변경되지 않았습니다.");
		}

		//사용자 로그인 failcount 초기화
		securityService.updateClearLoginFailCountForUser(user.getLoginId());

		//관리자동기화
		User manager = managerReqeustMapper.getManagerByLoginId(user.getLoginId());
		if(manager!=null) {
			int resultCnt = userMapper.updatePasswordForManager(tempUser);
			if(resultCnt == 1) {
				// 1. 관리자 조회
				TempPasswordChange tempPasswordChange = new TempPasswordChange();
				tempPasswordChange.setLoginId(manager.getLoginId());
				securityService.updateClearLoginFailCountForManager(tempPasswordChange.getLoginId()); //매니저
				passwordLogService.insertManagerPasswordLog(manager, false); //관리자 비밀번호 암호화
			}
			}

		} catch (UnsupportedEncodingException e) {
			log.error("changeUserPwdForSignNoLogin 에러가 발생하였습니다.");
			throw new UserException("에러가 발생하였습니다.");
		}
	}

	@Override
	public User getUserBySign(ChangePasswordForNoLoginUser changePassword) {
		User user = userMapper.getUserBySign(changePassword);

		if(user !=null) {
			//복호화
			decryptData(user);
		}
		return user;
	}

	@Override
	public void updateOnepassUnlink(Long userId) {
		// TODO Auto-generated method stub
		userMapper.updateOnepassUnlink(userId);
	}

	@Override
	public Map<String, Object> getFinancPid(FinancInfo financInfo) throws IOException {
		PrintWriter writer = null;
        BufferedReader reader = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HttpURLConnection conn = null;

        String strSignedVals = financInfo.getSignedVals();
        String ucpidNonce = financInfo.getUcpidNonce();

        FinancTokenResult tokenResult = getFinancToken(finUrl);
        String strToken = tokenResult.getAccess_token();

    	String tUrl = ucpidUrl;	//금융인증서 UCPID URL
    	OutputStreamWriter osw = null;

    	Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("ucpid_request_info", strSignedVals);	//금융인증 SDK에서 ucpidInfo를 sign() 또는 signWithoutUI()으로 전자서명한 결과 값
        reqParamMap.put("cp_code", fincertCpCode);				//DI를 생성하고자 하는 이용기관코드 12자리
        reqParamMap.put("ucpid_nonce", ucpidNonce);			//금융인증 SDK에서 ucpidInfo에 설정한 ucpidNonce 값 (HEXA 인코딩)

        log.debug("getFinancPid reqParamMap : {} ", reqParamMap.toString());

        try {
        	URL url = new URL(tUrl);
//        	_initHttps();
    		conn = (HttpURLConnection) url.openConnection();

    		/* test 환경에서는 인증서 오류가 날 수도 있다. 이 코드를 이용해 인증서 오류를 회피한다. */
			if (conn instanceof HttpsURLConnection) {
				SSLSocketFactoryMaker factory = new SSLSocketFactoryMaker();

				((HttpsURLConnection) conn).setSSLSocketFactory(factory.getSSLSocketFactory());
				((HttpsURLConnection) conn).setHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return CERTIFICATE;
					}
				});
			}

    		conn.setRequestMethod("POST"); // 전송 방식
    		conn.setRequestProperty("Authorization", "bearer " + strToken);
    		conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
    		conn.setDoInput(true);
    		conn.setDoOutput(true);
    		conn.setConnectTimeout(TIMEOUT_VALUE);	// 20초
    		conn.setReadTimeout(30000);	// 20초

    		conn.setRequestProperty("client_id", finClientId);
    		conn.setRequestProperty("X-Forwarded-For", "152.99.104.11");
    		conn.setRequestProperty("X-Forwarded-Host", "certapi.yeskey.or.kr");
    		conn.setRequestProperty("Host", "certapi.yeskey.or.kr");

            JSONObject json = new JSONObject();
            for (String key : reqParamMap.keySet()) {
                json.put(key, reqParamMap.get(key));
            }
            try(OutputStream os = conn.getOutputStream();) {
            	osw = new OutputStreamWriter(os, "UTF-8");
            }
            writer = new PrintWriter(osw);
            writer.write(json.toString());
            writer.flush();

    		if(conn.getResponseCode() < HttpsURLConnection.HTTP_BAD_REQUEST) {
    			try(InputStream is = conn.getInputStream();
    					InputStreamReader isr = new InputStreamReader(is, "UTF-8");) {
    				reader = new BufferedReader(isr);
    			}
            }else {
            	try(InputStream es = conn.getErrorStream();
            			InputStreamReader isr = new InputStreamReader(es, "UTF-8");){
            		reader = new BufferedReader(isr);
            	}
            }
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                sb.append(str + "\n");
            }

            String response = sb.toString();
            Object objList = JSONValue.parse(response);
            JSONObject jsonObject = (JSONObject)objList;

            log.debug("@@@@@response : {} ", response);

            if(!"".equals(response)) resultMap.put("api_tran_id", jsonObject.get("api_tran_id").toString());

            //응답 결과 세팅
            if(jsonObject.get("err_code") != null) {
            	resultMap.put("err_code", jsonObject.get("err_code"));
            	resultMap.put("err_msg", jsonObject.get("err_msg"));
            }else {
            	resultMap.put("ci", jsonObject.get("ci"));
                resultMap.put("di", jsonObject.get("di"));
                resultMap.put("national_info", jsonObject.get("national_info"));
                resultMap.put("dn", jsonObject.get("dn"));
                resultMap.put("birth_date", jsonObject.get("birth_date"));
                resultMap.put("real_name", jsonObject.get("real_name"));
                resultMap.put("gender", jsonObject.get("gender"));
            }
        } catch (MalformedURLException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}catch (IllegalStateException e) {
			log.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
        	log.error(getClass().getName() + " getFinancPid NoSuchAlgorithmException ====================", e);
		} catch (KeyManagementException e) {
        	log.error(getClass().getName() + " getFinancPid KeyManagementException ====================", e);
		} finally {
			if(osw != null) { osw.close(); }
        	if(writer != null) { writer.close(); }
        	if(reader != null) { reader.close(); }
        	if(conn != null) { conn.disconnect(); }
        }

		return resultMap;

	}

	//금융인증서 OAuth Token
	public FinancTokenResult getFinancToken(String finUrl) throws IOException {
		PrintWriter writer = null;
        BufferedReader reader = null;

        Map<String, Object> reqParamMap = new HashMap<>();
        HttpURLConnection conn = null;
        PrintWriter pw = null;


    	String tUrl = finUrl;

        reqParamMap.put("client_secret", finClientSecret);	//서비스 신청시 발급 받은 client_secret
        reqParamMap.put("scope", finScope); 				//ucpid : 인증서 본인확인서비스
        reqParamMap.put("grant_type", finGrantType);		//인증 권한 타입
        reqParamMap.put("server_id", finServerId);			//이용기관의 서버를 구분하기 위한 구별값 (이용기관이 설정)
        reqParamMap.put("reissue", "y");					//토큰 재발급 여부 (y, n)  (미설정시 n으로 동작)
        log.debug("@@@@@getFinancToken tokenUrl : {}", tUrl);
        log.debug("@@@@@getFinancToken reqParamMap : {} ", reqParamMap.toString());

		try {
			URL url = new URL(tUrl);
//			_initHttps();
            conn = (HttpURLConnection) url.openConnection();

            /* test 환경에서는 인증서 오류가 날 수도 있다. 이 코드를 이용해 인증서 오류를 회피한다. */
			if (conn instanceof HttpsURLConnection)
			{
				SSLSocketFactoryMaker factory = new SSLSocketFactoryMaker();
				((HttpsURLConnection) conn).setSSLSocketFactory(factory.getSSLSocketFactory());
				((HttpsURLConnection) conn).setHostnameVerifier(new HostnameVerifier() {
					@Override
					public boolean verify(String hostname, SSLSession session) {
						return CERTIFICATE;
					}
				});
			}

			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setDefaultUseCaches(false);
			conn.setConnectTimeout(TIMEOUT_VALUE);	// 10초
			conn.setReadTimeout(30000);	// 10초
			conn.setInstanceFollowRedirects(false);

			conn.setRequestMethod("POST"); // 전송 방식

			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
			conn.setRequestProperty("client_id", finClientId);	//서비스 신청시 발급 받은 client_id
			conn.setRequestProperty("X-Forwarded-For", "152.99.104.11");
    		conn.setRequestProperty("X-Forwarded-Host", "certapi.yeskey.or.kr");
    		conn.setRequestProperty("Host", "certapi.yeskey.or.kr");

			log.debug("@@@@getFinancToken Host : {}", conn.getRequestProperty("X-Forwarded-For"));

	        String responseData = "";
			BufferedReader br = null;
			StringBuffer sb = null;

			boolean first = true;
			StringBuilder result = new StringBuilder();
			for (String key : reqParamMap.keySet()) {
				if (first) first = false;
				else result.append("&");

				result.append(URLEncoder.encode(key, "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(String.valueOf(reqParamMap.get(key)), "UTF-8"));
			}
			log.debug("@@@@getFinancToken PrintWriter : {}");
			pw = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));

			pw.write(result.toString());
			pw.flush();
			log.debug("@@@@getFinancToken flush : {}");
	        conn.connect();
	        log.debug("@@@@getFinancToken connect : {}");

	        log.debug("@@@@getFinancToken ResponseCode : {}", conn.getResponseCode());
			if(conn.getResponseCode() < HttpsURLConnection.HTTP_BAD_REQUEST) {
            	br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            }else if(conn.getResponseCode() == HttpsURLConnection.HTTP_MOVED_TEMP || conn.getResponseCode() == HttpsURLConnection.HTTP_MOVED_PERM) {
            	String redirectUrl = conn.getHeaderField("Location");
            	log.debug("@@@@@redirectUrl : {}" , redirectUrl);
            	if (redirectUrl == null || redirectUrl.equals(finUrl)) {
            		throw new UserException("getFinancToken 통신에 실패했습니다.");
            	}
            	//finUrl = redirectUrl;
            	return getFinancToken(redirectUrl);
            }else {
            	br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }

			sb = new StringBuffer();

			if (br != null) {
				while ((responseData = br.readLine()) != null) {
					sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
				}
			}

			//메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
			String returnData = sb.toString();
	        log.debug("@@@@@returnData : {} ", returnData);

	        String response = sb.toString();
	        Object objList = JSONValue.parse(response);
	        JSONObject jsonObject = (JSONObject)objList;

	        ObjectMapper mapper = new ObjectMapper();

	        FinancTokenResult ftr  = mapper.readValue(jsonObject.toJSONString(), FinancTokenResult.class);

	        return ftr;

		} catch (MalformedURLException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}catch (IllegalStateException e) {
			log.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
        	log.error(getClass().getName() + " getFinancToken NoSuchAlgorithmException ====================", e);
		} catch (KeyManagementException e) {
        	log.error(getClass().getName() + " getFinancToken KeyManagementException ====================", e);
		} finally {
        	if(writer != null) { writer.close(); }
        	if(reader != null) { reader.close(); }
        	if(conn != null) {conn.disconnect(); }
        	if(pw != null) { pw.close(); }
        }
		return null;
    }

	@Override
	public Map<String, Object> getFinancUserToken(FinancInfo financInfo) throws Exception {
		//FinancTokenResult result = this.getFinancToken();
		Map<String, Object> resultMap = this.getFinancPid(financInfo);

		return resultMap;
	}

	@Override
	public Map<String, Object> getLoginCiInfo(FinancInfo financInfo) {
		//public FinancResultInfo getLoginCiInfo(FinancInfo financInfo) {
			FinancResultInfo resultInfo = userMapper.getLoginCiInfo(financInfo);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if(resultInfo == null) {
				throw new UserException("회원 정보를 확인 할 수 없습니다.");
			}

			String bfMberCi = financInfo.getMberCi();
			String afMberCi = resultInfo.getMberCi();

			if(!afMberCi.equals(bfMberCi)) {
				throw new UserException("회원 정보를 확인 할 수 없습니다.");
			}

			SignInfo signInfo = new SignInfo();
			signInfo.setUserId(resultInfo.getUserId());
			//signInfo.setMberFinDn(financInfo.getMberFinDn());
			userMapper.updateUserSignRegister(signInfo);

			resultMap.put("loginId", resultInfo.getLoginId());
			resultMap.put("password", resultInfo.getPassword());

			return resultMap;
			//return resultInfo;
		}

	@Override
	public String bytesToHexString(){
		/*StringBuilder sb = new StringBuilder(bytes.length*2);
		Formatter formatter = new Formatter(sb);

		for(byte  b : bytes){
			formatter.format("%02x", b);
		}
		return sb.toString();*/

		SecureRandom genRandom = new SecureRandom();
        byte[] serverRandom = new byte[16];
        genRandom.nextBytes(serverRandom);

		final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		char[] hexChars = new char[serverRandom.length * 2];
		int v;
		for ( int j = 0; j < serverRandom.length; j++ ) {
			v = serverRandom[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	@Override
	public int updatePasswordType(UserPasswordType userPasswordType) {
		return userMapper.updatePasswordType(userPasswordType);
	}

//	private void _initHttps() {
//		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//				if (!CERTIFICATE) {
//					throw new CertificateException();
//				}
//			}
//
//			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//				if (!CERTIFICATE) {
//					throw new CertificateException();
//				}
//			}
//
//			public X509Certificate[] getAcceptedIssuers() {
//				return new X509Certificate[0];
//			}
//		} };
//		try {
//			// SSL -> TLSv1.2 by SonarQube
//			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
//			sslContext.init(null, trustAllCerts, new SecureRandom());
//			_sslSockFactory = sslContext.getSocketFactory();
//		} catch (RuntimeException e) {
//			RuntimeException re = new RuntimeException(e);
//			re.setStackTrace(e.getStackTrace());
//			throw re;
//		} catch (KeyManagementException e) {
//			log.error("ERROR-34: Key 관리 예외오류 {}", e);
//		} catch (NoSuchAlgorithmException  e) {
//			log.error("ERROR-35: 암호 알고리즘 사용불가 오류 {}", e);
//		}
//	}

	@Override
	public String setSessionByManager(HttpServletRequest request, User manager) {
		String result = "SUCC";

		try {
			// 1. 로그인 사용자 정보 조회
			String loginId = CommonUtils.dataNvl(manager.getLoginId()) + OpKeyHolder.OPMANAGER_LOGIN_KEY;
			UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);

			// 2. 아이디, 권한 설정
			Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(newAuth);


			// 3. 세션 설정
			HttpSession session = request.getSession(true);
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, newAuth);

			// 4. 세션 저장
			this.saveLoginSessionForManager(request.getSession(), manager.getUserId());

			// 5. 로그인 카운트 증가, 실패 카운트 증가, 로그인 로그 추가
			//securityMapper.updateManagerLoginCount(manager);
			securityMapper.updateClearLoginFailCountForManager(manager.getLoginId());
			loginLogService.insertLoginLogByManager(request, manager.getLoginId(), true);

			session.setAttribute("loginNow", "Y");											// 20260518 추가. 로그인 직후 상태 세션

		} catch (NullPointerException e) {
			log.error(e.getMessage());
			log.error("setSessionByManager 세션 등록 실패 userId : " + manager.getUserId(), e);
			result = "ERR";
		}

		try {
			// 6. 관리자 로그인 국민비서 발송
			User u = userMapper.getUserByUserId(manager.getUserId());
			if (u != null) {
				UserDetail detail = (UserDetail) u.getUserDetail();
				if (detail != null) {
					GiveUserSmsInfo info = new GiveUserSmsInfo();
					info.setUserId(manager.getUserId());
					info.setUserName(u.getUserName());
					info.setMberCi(detail.getMberCi());
					info.setReceiveSms("0");
					if (!"".equals(u.getPhoneNumber()) && u.getPhoneNumber() != null) info.setPhoneNumber(u.getPhoneNumber().replaceAll("-", ""));
					smsIpsService.giveSendSms(Arrays.asList(info), SmsType.MANAGER_LOGIN);
				}
			}
		} catch (NullPointerException e) {
			log.error("setSessionByManager 국민비서 발송 실패 : " + manager.getUserId(), e.getMessage());
		}

		return result;

	}

	@Override
	public void insertUserParent(UserParent userParent) {
		userMapper.insertUserParent(userParent);
	}

	@Override
	public void setUserBirthdayDecList() {
		List<HashMap<String, Object>> birthdayList = new ArrayList<>();

		try {
			birthdayList = userMapper.getUserBirthdayDecList();
			userMapper.deleteUserBirthdayDec();
			for(HashMap<String, Object> hashMap : birthdayList) {
				hashMap.put("birthday", pCrypto.Decrypt("normal", hashMap.get("birthday").toString(), "", 0));
				//userMapper.insertUserBirthdayDec(hashMap);
			}
			if (birthdayList != null && !birthdayList.isEmpty()) {
				userMapper.insertUserBirthdayDec(birthdayList);
			}
		} catch (NullPointerException | UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}

	}

	@Override
	public long selectNewUserId() {
		return userMapper.selectNewUserId();
	}

	@Override
	public UserDetail getUserBirthday(String loginId) {
		return userMapper.getUserBirthday(loginId);
	}

	@Override
	public void setOpUserBirthdayStat() {
		List<HashMap<String, Object>> substrList = new ArrayList<>();
		List<HashMap<String, Object>> paramDayList = new ArrayList<>();
		List<HashMap<String, Object>> ouBirthdayList = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar c1 = Calendar.getInstance();
		String strToday = sdf.format(c1.getTime());

		try {
			substrList = userMapper.getOpUserBirthdayDecSubstrList(); // op_user_birthday 테이블로 적재해야 할 가입일 리스트 추출

			if(substrList.size() > 0) { // 적재 필요 건 있을 시
				String strSubstr = substrList.toString();

				if(strSubstr.indexOf(strToday) < 0) { // 배치 실행 시점 당일 가입자 없는 경우 적재 필요일 리스트 전체
					for(HashMap<String, Object> hashMap : substrList) {
						hashMap.put("paramDate", hashMap.get("paramDate").toString());
						paramDayList = userMapper.getOpUserBirthdayDecListParam(hashMap);
						if (paramDayList != null && !paramDayList.isEmpty()) {
							userMapper.insertUserBirthdayDec(paramDayList);
						}
					}
				} else {
					for(int i=0;i<substrList.size()-1;i++) { // 배치 실행 시점 당일 가입자 있는 경우	적재 필요일 리스트에서 당일 날짜 제외
						HashMap<String, Object> hashMap = new HashMap<>();
						hashMap.put("paramDate", substrList.get(i).get("paramDate").toString());
						paramDayList = userMapper.getOpUserBirthdayDecListParam(hashMap);
						if (paramDayList != null && !paramDayList.isEmpty()) {
							userMapper.insertUserBirthdayDec(paramDayList);
						}
					}
				}
			}


			// 운영DB -> 통계DB 적재

			String paramId = analysisMapper.selectMaxUserIdOpUserBirthday();
			ouBirthdayList = userMapper.getOpUserBirthdayAllList(paramId);
			for(HashMap<String, Object> hMap : ouBirthdayList) {
				hMap.put("user_id", hMap.get("user_id").toString());
				hMap.put("birthday", hMap.get("birthday").toString());
				hMap.put("created_date", hMap.get("created_date").toString());
//				analysisMapper.insertOpuserBirthdayDecListBatch(hMap);
			}

			int remain = ouBirthdayList.size() % 1000;
			int length = 0;
			if (remain == 0) {
				length = ouBirthdayList.size() / 1000;
			} else {
				length = ouBirthdayList.size() / 1000 + 1;
			}

			List<HashMap<String, Object>> ouBirthdayList2;
			for(int i = 0 ; i < length ; i++) {
				ouBirthdayList2 = new ArrayList<>();
				int last = (i + 1) * 1000;
				if (i == length - 1) {
					last = i * 1000 + remain;
				}
				int start = i * 1000;
				for (;start < last ; start++) {
					ouBirthdayList2.add(ouBirthdayList.get(start));
				}
				analysisMapper.insertOpuserBirthdayDecListBatch(ouBirthdayList2);
			}
		} catch (NullPointerException e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 관리자 이메일 등록(op_manager)
	 * @param mberDn
	 * @return
	 */
	@Override
	public int updateManagerInfo(OpManagerParam param) {
		int updRslt = userMapper.updateManagerInfo(param);
		// [GGSR-26-369] 이력저장
		managerHistService.insertManagerHist(param, "H");
		return updRslt;
	}

	/**
	 * 관리자 이메일 등록(op_user)
	 * @param mberDn
	 * @return
	 */
	@Override
	public int updateUserByLoginId(User user) {
		return userMapper.updateUserByLoginId(user);
	}

	/**
	 * 관리자 정보 조회
	 * @param userId
	 * @return
	 */
	@Override
	public User getManagerByLoginId(String loginId) {
		return userMapper.getManagerByLoginId(loginId);
	}

	/**
	 * 관리자 휴면계정 업데이트
	 * @param
	 * @return
	 */
	@Override
	public void updateSleepManager() {
		List<String> targets = userMapper.getSleepManagerTarget();
		userMapper.updateSleepManager();
		// [GGSR-26-369] 이력저장
		if (targets != null && targets.size() > 0) {
			ManagerHist histParam = new ManagerHist();
			histParam.setUserIdList(targets);
			managerHistService.insertManagerHist(histParam, "B");
		}
	};

}
