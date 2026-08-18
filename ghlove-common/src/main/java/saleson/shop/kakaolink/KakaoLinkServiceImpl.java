package saleson.shop.kakaolink;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.api.common.enumerated.ApiError;
import saleson.common.enumeration.SmsType;
import saleson.common.security.api.JwtCode;
import saleson.common.security.api.JwtTokenService;
import saleson.common.sms.SmsIpsService;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.JwtUtils;
import saleson.common.utils.RandomStringUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.UserEntity;
import saleson.shop.config.ConfigService;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.kakaolink.domain.KakaoAccount;
import saleson.shop.kakaolink.domain.KakaoLink;
import saleson.shop.kakaolink.domain.KakaoToolkit;
import saleson.shop.kakaolink.domain.KakaoToolkitData;
import saleson.shop.kakaolink.domain.NaverToolkitData;
import saleson.shop.log.LoginLogService;
import saleson.shop.security.ShopSecurityService;
import saleson.shop.user.GeneralCustomerMapper;
import saleson.shop.user.JoinMapper;
import saleson.shop.user.UserMapper;
import saleson.shop.user.UserRepository;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.GeneralCustomer;
import saleson.shop.user.domain.GeneralCustomerSecede;
import saleson.shop.user.domain.SecedeCntrAmt;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.support.GeneralCustomerSearchParam;
import saleson.shop.userrole.UserRoleMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ThreadContextUtils;

@Slf4j
@RequiredArgsConstructor
@Service("kakaoLinkService")
public class KakaoLinkServiceImpl extends EgovAbstractServiceImpl implements KakaoLinkService{

	@Autowired
	private KakaoLinkMapper kakaoLinkMapper;

	@Autowired
	private UserService userService;

	@Value("${kakao.token-url}")
    private String tokenUrl;

	@Value("${kakao.sign-check-url}")
    private String signCheckUrl;

	@Value("${kakao.api-url}")
    private String apiUrl;

	@Value("${kakao.rest-api-key}")
    private String restApiKey;

	@Value("${kakao.redirect-uri}")
    private String redirectUri;

	@Value("${kakao.redirect-uri2}")
    private String redirectUri2;

	@Value("${kakao.app-admin-key}")
    private String kakaoAppAdminKey;

	@Value("${kakao.toolkit-url}")
    private String kakaoToolkitUrl;

	@Value("${kakao.toolkit-url2}")
    private String kakaoToolkitUrl2;

	@Value("${kakao.access-token}")
    private String kakaoToolkitAccessToken;

	@Value("${saleson.url.shoppingmall}")
    private String serverDomain;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenService jwtTokenService;

	@Autowired
	private ShopSecurityService shopSecurityService;

	@Autowired
	private LoginLogService loginLogService;

//	@Autowired
//	private JoinService joinService;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private JoinMapper joinMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private SmsIpsService smsIpsService;

	@Autowired
	private final GeneralCustomerMapper generalCustomerMapper;

	@Autowired
	private ConfigService configService;

	// 카카오톡 인증시 네이버 인증 겸용 툴킷 사용 여부
	private boolean isKakaoLoginByNaverToolkit = true;

	// 카카오 사용자 일련번호로 연계해제 처리
	// 탈퇴여부 동의를 받을 수 없어서 해제 처리만 진행
	@Override
	public void kakaoLinkClearByKakaoUserSequence(String userSequence) {
		int cnt = kakaoLinkMapper.kakaoLinkClearByKakaoUserKey(userSequence);

		if (cnt == 0) {
			throw new UserException("사용자 정보가 없습니다.(2)");
		}
	}

	// 로그인아이디로 연계해제 처리
	@Override
	public KakaoLink kakaoLinkClearByUserId(long userId) {
		KakaoLink result = new KakaoLink();
		KakaoLink kakaoLink = kakaoLinkMapper.selectLoginPathCode(userId);
		if (kakaoLink == null) {
			result.setApiError(ApiError.NOT_VALID_LOGIN);
		} else {
			if ("100".equals(kakaoLink.getLoginPathCode()) || "300".equals(kakaoLink.getLoginPathCode())) {		// 아이디 가입 또는 원패스 가입 후 연동했을 경우 - 카카오 연결 해제 처리
				// 카카오 유저 키 삭제
				int cnt = kakaoLinkMapper.kakaoLinkClear(userId);

				if (cnt == 0) {		// 롤백 위해서 오류 발생
					throw new UserException("사용자 정보가 없습니다.");
				}
				try {
					// 카카오 통신하여 연동 해제
//					result = kakaoLinkClear(kakaoLink);
					result = kakaoLinkToolkitClear(kakaoLink);
				} catch (IOException e) {
					log.error(getClass().getName() + " kakaoLinkClearByUserId kakaoLinkClear error ::", e);
					// 롤백 위해서 오류 발생
//					throw new UserException("카카오 연동 해제에 실패했습니다.");
				}
			} else if ("500".equals(kakaoLink.getLoginPathCode())) {			// 카카오 연동만 되어있을 경우 - 탈퇴 처리(탈퇴페이지 이동)
				// 탈퇴 처리 진행할지 팝업
				result.setCode("CHECK_SECEDE");
			}
		}
		return result;
	}

	// 카카오 회원 연계/로그인 처리
	@Override
	public KakaoLink kakaoLinkProcess(String kakaoCode, String signData, String type) {
		KakaoLink result;
		KakaoLink kakaoLink;
		// 카카오 토큰 획득
		try {
//			kakaoLink = getKakaoUserInfo(getKakaoToken(kakaoCode));
			kakaoLink = getToolkitAuth(kakaoCode, type);
//			kakaoLink = getToolkitAuthHardcoding(kakaoCode, type);
//		} catch (IOException e) {
		} catch (UserException e) {
			result = new KakaoLink();
			result.setApiError(ApiError.BAD_REQUEST);
			log.error(getClass().getName() + "kakaoLinkProcess getKakaoUserInfo error :: ", e);
			return result;
		}

		// 우선 주석처리..
//		if (!signData.equals(kakaoLink.getRequestToken())) {
//			result = new KakaoLink();
//			result.setApiError(ApiError.UNAUTHORIZED_TOKEN);
//			return result;
//		}

        if (!StringUtils.hasLength(kakaoLink.getKakaoAccount().getMberCi())) {
	        result = new KakaoLink();
	        result.setApiError(ApiError.KAKAO_LINK_NO_CI);
	        return result;
        }

        return kakaoLinkJoinAndLoginProcess(kakaoLink, "KAKAO", isKakaoLoginByNaverToolkit);
	}

	private KakaoLink kakaoLinkJoinAndLoginProcess(KakaoLink kakaoLink, String loginAuthType, boolean isNaverToolkit) {
		KakaoLink result;
		// 가입 여부 체크
		UserEntity userEntity = userRepository.findByMberCi(kakaoLink.getKakaoAccount().getMberCi());

		if (userEntity == null) {			// 회원가입
	        if (!StringUtils.hasLength(kakaoLink.getKakaoAccount().getName())) {
		        result = new KakaoLink();
		        result.setApiError(ApiError.KAKAO_LINK_NO_NAME);
		        return result;
	        }

	        if (!StringUtils.hasLength(kakaoLink.getKakaoAccount().getEmail())) {
		        result = new KakaoLink();
		        result.setApiError(ApiError.KAKAO_LINK_NO_EMAIL);
		        return result;
	        }

	        if (!StringUtils.hasLength(kakaoLink.getKakaoAccount().getPhoneNumber())) {
		        result = new KakaoLink();
		        result.setApiError(ApiError.KAKAO_LINK_NO_PHONE);
		        return result;
	        }

			LocalDate nowDate = LocalDate.now();

			LocalDate birthDate = null;
			try {
				if (isNaverToolkit) {
					birthDate = LocalDate.parse(kakaoLink.getKakaoAccount().getBirthday(), DateTimeFormatter.ofPattern("yyyyMMdd"));
				} else {
					birthDate = LocalDate.parse(kakaoLink.getKakaoAccount().getBirthyear() + kakaoLink.getKakaoAccount().getBirthday(), DateTimeFormatter.ofPattern("yyyyMMdd"));
				}
			} catch (DateTimeParseException e) {
		        result = new KakaoLink();
		        result.setApiError(ApiError.KAKAO_LINK_NO_BIRTH);
		        return result;
			}

			// 테스트
//			birthDate = LocalDate.parse("20090901", DateTimeFormatter.ofPattern("yyyyMMdd"));

			int checkYear = nowDate.getYear() - birthDate.getYear();
			int nowMonth = nowDate.getMonthValue();
			int nowDayOfMonth = nowDate.getDayOfMonth();
			int birthMonth = birthDate.getMonthValue();
			int birthDayOfMonth = birthDate.getDayOfMonth();
			if (nowMonth == birthMonth) {
				if (nowDayOfMonth < birthDayOfMonth) {
					checkYear--;
				}
			} else if (nowMonth < birthMonth) {
				checkYear--;
			}

			if (checkYear < 14) {
		        result = new KakaoLink();
		        result.setApiError(ApiError.BAD_REQUEST_NOT_CONFIRM_STATUS);
		        return result;
			}

//	        long userId = sequenceService.getLong("OP_USER");
			long userId = userService.selectNewUserId();

	        User user = new User();
	        UserDetail userDetail = new UserDetail();

	        user.setUserId(userId);
	        userDetail.setUserId(userId);

	        //랜덤 아이디 세팅
	        user.setLoginId(randomUserId());
	        user.setPassword(RandomStringUtils.getRandomString("", 4, 8));
	        user.setUserName(kakaoLink.getKakaoAccount().getName());
	        user.setEmail(kakaoLink.getKakaoAccount().getEmail());
	        user.setPhoneNumber(kakaoLink.getKakaoAccount().getPhoneNumber());

			userModifyDataSet(kakaoLink, userDetail);

	        user.setUserDetail(userDetail);
	        if ("KAKAO".equalsIgnoreCase(loginAuthType)) {
		        userDetail.setLoginPathCode("500");
		        userDetail.setKakaoUserKey(kakaoLink.getId());
	        } else if ("NAVER".equalsIgnoreCase(loginAuthType)) {
		        userDetail.setLoginPathCode("600");
		        userDetail.setNaverUserKey(kakaoLink.getId());
	        } else {
		        result = new KakaoLink();
		        result.setApiError(ApiError.SYSTEM_ERROR);
		        log.error("=================" + getClass().getName() + " kakaoLinkJoinAndLoginProcess error :: loginAuthType :: " + loginAuthType);
		        return result;
	        }
	        insertUserAndUserDetail(user, userDetail);

			int alternateSystemLevel = configService.selectAlternateSystem();
			if (alternateSystemLevel > 0) {								// 접속제한 실시할 경우
				String birth = userDetail.getBirthday();
				HashMap<String, Object> checkResult = UserUtils.isAlternateSystem(alternateSystemLevel, birth);
				StringBuffer buf = new StringBuffer();
				buf.append(birth.substring(0, 4) + "년");
				buf.append(" " + Integer.valueOf(birth.substring(4, 6)) + "월");
				buf.append(" " + Integer.valueOf(birth.substring(6, 8)) + "일");
				if (!(boolean)checkResult.get("result")) {
			        result = new KakaoLink();
					result.setAlternateSystemMsg(checkResult.get("resultMsg").toString() + "\n등록된 생년월일 :" + birth);
					result.setGrantType("ALTERNATE_SYSTEM_DENIED");
			        result.setLoginId(user.getLoginId());
			        result.setCode("JOIN_MEMBER");
					return result;
				}
			}


	        HttpServletRequest request = ThreadContextUtils.getRequestContext().getRequest();

			String loginType = "ROLE_USER";

	        String token = jwtTokenService.getJwtToken(
	                loginType,
	                user.getLoginId().trim(),
	                user.getPassword().trim(),
	                JwtUtils.getClientIpAddress(request)
	        );

	        String sign = (String)JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_SIGN));
	        userService.saveLoginSessionForUser(sign, userId);

	        result = new KakaoLink();
	        result.setToken(token);
	        result.setLoginId(user.getLoginId().trim());
	        result.setName(user.getUserName());
	        result.setCode("JOIN_MEMBER");
		} else {
			result = kakaoLoginProcess(kakaoLink, userEntity, loginAuthType);
	        result.setCode("LOGIN");
//	        result.setCode("JOIN_MEMBER"); // 테스트용
		}

        return result;
	}

//	// 카카오 통신하여 토큰 획득
//	private KakaoLink getKakaoToken(String kakaoCode) {
//		StringBuffer paramBuf = new StringBuffer();
//		ObjectMapper mapper = new ObjectMapper();
//
//		String clientSecretKey = RandomStringUtils.getRandomString("", 4, 10);
//
//		addParam(paramBuf, "grant_type", "authorization_code");					// authorization_code 고정값, 필수
//		addParam(paramBuf, "client_id", restApiKey);							// 앱 REST API 키, 필수
//		addParam(paramBuf, "redirect_uri", redirectUri);						// 인가 코드가 리다이렉트 된 URI, 필수
//		addParam(paramBuf, "code", kakaoCode);									// 카카오 로그인 후 수신된 인가 코드, 필수
//		addParam(paramBuf, "client_secret", clientSecretKey);				// 보안 강화용 키, 카카오 개발자 사이트에서 On 상태일 경우 필수
//
//		try {
//			URL url = new URL(tokenUrl);
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("POST"); // 전송 방식
//			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");		// header 필수
//			conn.setDoOutput(true);
//			conn.setConnectTimeout(5000);	// 5초
//			conn.setReadTimeout(5000);	// 5초
//
//			OutputStream wr1 = conn.getOutputStream();
//			wr1.write(paramBuf.toString().getBytes("UTF-8"));
//			wr1.flush();
//			wr1.close();
//
//			try(
//					InputStream is = conn.getInputStream();
//					InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
//					BufferedReader br = new BufferedReader(isr);
//				) {
//				String inputLine;
//				StringBuffer sb = new StringBuffer();
//				while ((inputLine = br.readLine()) != null) {
//					sb.append(inputLine);
//				}
//				br.close();
//
//				KakaoLink responseData = mapper.readValue(sb.toString(), KakaoLink.class);
//
////				if (!clientSecretKey.equals(responseData.getClientSecret())) {
////					throw new UserException("", "카카오 API 검증 오류 입니다. 다시 시도해주세요.", "");
////				}
//
//				return responseData;
//			} catch (IOException e) {
//				throw new IOException(e);
//			}
//		} catch (IOException e) {
//			log.error(getClass().getName() + " getKakaoToken IOException ::", e);
//			throw new UserException("카카오 token 획득에 실패했습니다.");
//		} catch (UserException e) {
//			log.error(getClass().getName() + " getKakaoToken UserException ::", e);
//			throw new UserException(e.getErrorMessage());
//		}
//	}
//
//	private KakaoLink getKakaoUserInfo(KakaoLink kakaoLink) throws IOException {
//		StringBuffer paramBuf = new StringBuffer();
//		ObjectMapper mapper = new ObjectMapper();
//
//		addParam(paramBuf, "property_keys", "[\"kakao_account.phone_number\", \"kakao_account.email\", \"kakao_account.birthyear\""
//										+ ", \"kakao_account.birthday_type\", \"kakao_account.ci\", \"kakao_account.birthday\", \"kakao_account.name\"]");
//		URL url = new URL(apiUrl + "/v2/user/me");
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setRequestMethod("POST"); // 전송 방식
//		conn.setRequestProperty("Authorization", "Bearer " + kakaoLink.getAccessToken());
//		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//		conn.setDoOutput(true);
//		conn.setConnectTimeout(5000);	// 5초
//		conn.setReadTimeout(5000);	// 5초
//
//		try (OutputStream wr1 = conn.getOutputStream();) {
//
//			wr1.write(paramBuf.toString().getBytes("UTF-8"));
//			wr1.flush();
//			wr1.close();
//
//			try(
//					InputStream is = conn.getInputStream();
//					InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
//					BufferedReader br = new BufferedReader(isr);
//				) {
//				String inputLine;
//				StringBuffer sb = new StringBuffer();
//				while ((inputLine = br.readLine()) != null) {
//					sb.append(inputLine);
//				}
//				br.close();
//
//				KakaoLink responseData = mapper.readValue(sb.toString(), KakaoLink.class);
//
//				return responseData;
//			} catch (IOException e) {
//				throw new IOException(e);
//			}
//		} catch (IOException e) {
//			log.error(getClass().getName() + " getKakaoUserInfo error ::", e);
//			throw new IOException(e);
//		} finally {
//			if (conn != null) {
//				conn.disconnect();
//			}
//		}
//	}
//
//	private KakaoLink kakaoLinkClear(KakaoLink kakaoLink) throws IOException {
//		StringBuffer paramBuf = new StringBuffer();
//		ObjectMapper mapper = new ObjectMapper();
//
//		addParam(paramBuf, "target_id_type", "user_id");
//		addParam(paramBuf, "target_id", kakaoLink.getKakaoUserKey());
//		URL url = new URL(apiUrl + "/v1/user/unlink");
//
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setRequestMethod("POST"); // 전송 방식
//		conn.setRequestProperty("Authorization", "KakaoAK " + kakaoAppAdminKey);
//		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
//		conn.setDoOutput(true);
//		conn.setConnectTimeout(5000);	// 5초
//		conn.setReadTimeout(5000);	// 5초
//
//		try (OutputStream wr1 = conn.getOutputStream();) {
//
//			wr1.write(paramBuf.toString().getBytes("UTF-8"));
//			wr1.flush();
//			wr1.close();
//
//			try(
//					InputStream is = conn.getInputStream();
//					InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
//					BufferedReader br = new BufferedReader(isr);
//				) {
//				String inputLine;
//				StringBuffer sb = new StringBuffer();
//				while ((inputLine = br.readLine()) != null) {
//					sb.append(inputLine);
//				}
//				br.close();
//
//				KakaoLink responseData = mapper.readValue(sb.toString(), KakaoLink.class);
//
//				return responseData;
//			} catch (IOException e) {
//				throw new IOException(e);
//			}
//		} catch (IOException e) {
//			log.error(getClass().getName() + " kakaoLinkClear error ::", e);
//			throw new IOException(e);
//		} finally {
//			if (conn != null) {
//				conn.disconnect();
//			}
//		}
//	}

	private void addParam(StringBuffer buf, String paramKey, String paramValue) {
		if (buf == null) {
			buf = new StringBuffer();
		}
		int length = buf.length();
		if (length > 0) {
			buf.append("&");
		}
		buf.append(paramKey);
		buf.append("=");
		buf.append(paramValue);
	}

	// JoinController 함수 복사
    private String randomUserId() {
    	String ramdomId = "";
        int count = 0;
        while(count ==0) {
        	SecureRandom random = new SecureRandom();
        	StringBuffer temp = new StringBuffer();
        	temp.append((char) ((int) (random.nextInt(26)) + 97));
//        	ramdomId = RandomStringUtils.getRandomString(temp.toString(), 3, 7).toLowerCase();
        	ramdomId = RandomStringUtils.getRandomString(temp.toString(), 4, 10).toLowerCase();		// 길이 변경
            User user = new User();
            user.setLoginId(ramdomId);
            String checkResult = userService.checkDuplication(user);

           if (!"isOccupiedId".equals(checkResult)) {
        	  count = 1;
           }
        }
		return ramdomId;
    }

	// JoinController 함수 복사, 카카오 정보로 정보 세팅 필요
	private void userModifyDataSet(KakaoLink kakaoUserInfo, UserDetail userDetail) {
		KakaoAccount userInfo = kakaoUserInfo.getKakaoAccount();
        if (StringUtils.hasLength(userInfo.getPhoneNumber())) {
            userDetail.setPhoneNumber(ShopUtils.phoneNumberPattern(userInfo.getPhoneNumber()));
        }

//        userDetail.setPost(userInfo.getPost());
//
//        if (StringUtils.hasLength(userInfo.getNewPost())) {
//            userDetail.setNewPost(userInfo.getNewPost());
//        }
//
//        if (StringUtils.hasLength(userInfo.getAddress())) {
//            userDetail.setAddress(userInfo.getAddress());
//        }
//
//        if (StringUtils.hasLength(userInfo.getAddressDetail())) {
//            userDetail.setAddressDetail(userInfo.getAddressDetail());
//        }

//        if (!ObjectUtils.isEmpty(userInfo.getBirthdayYear())){
//            userDetail.setBirthdayDay(userInfo.getBirthdayDay());
//        }

//    	if ("SOLAR".equalsIgnoreCase(userInfo.getBirthdayType())) {			// 생년월일이 주민번호와 다른 경우가 있어서 넣지 않도록 수정
//            userDetail.setBirthdayType("1");
//    	} else if ("LUNAR".equalsIgnoreCase(userInfo.getBirthdayType())) {
//            userDetail.setBirthdayType("2");
//    	}

//        if (StringUtils.hasLength(userInfo.getGender())) {
//            userDetail.setGender(userInfo.getGender());
//        }

    	// 수신비동의 설정
//        userDetail.setReceiveEmail("1");
//        userDetail.setReceiveSms("1");

    	// 수신 동의 설정
        userDetail.setReceiveEmail("0");
        userDetail.setReceiveSms("0");
        userDetail.setReceivePbanc("0");

//        if (!ObjectUtils.isEmpty(userInfo.getReceivePush())) {
//            userDetail.setReceivePush(userInfo.getReceivePush());
//        }

//        if(StringUtils.hasLength(userInfo.getLocGovList())) {
//        	userDetail.setLocGovList(userInfo.getLocGovList());
//        }
//
//        if(StringUtils.hasLength(userInfo.getRtnpsntList())) {
//        	userDetail.setRtnpsntList(userInfo.getRtnpsntList());
//        }

        if(StringUtils.hasLength(userInfo.getMberCi())) {
        	userDetail.setMberCi(userInfo.getMberCi());
        }

//        if(StringUtils.hasLength(userInfo.getMberDi())) {
//        	userDetail.setMberDi(userInfo.getMberDi());
//        }
//
//        if(StringUtils.hasLength(userInfo.getMberDn())) {
//        	userDetail.setMberDn(userInfo.getMberDn());
//        }
//
//        if(StringUtils.hasLength(userInfo.getLocgovCode())) {
//        	userDetail.setLocgovCode(userInfo.getLocgovCode());
//        }

//        if(!ObjectUtils.isEmpty(userInfo.getBirthday())) {
//        	userDetail.setBirthday(userInfo.getBirthday());
//        }

//        if(StringUtils.hasLength(userInfo.getBirthday())) {
//        	userDetail.setBirthday(userInfo.getBirthday());
//        }
//        if (StringUtils.hasLength(userInfo.getBirthday()) && StringUtils.hasLength(userInfo.getBirthyear())) {
//	        userDetail.setBirthdayFull(userInfo.getBirthyear() + userInfo.getBirthday());
//        }
        userDetail.setBirthdayFull(userInfo.getBirthday());

        // 툴킷 미사용시
//        if(StringUtils.hasLength(kakaoUserInfo.getId())) {
//        	userDetail.setKakaoUserKey(kakaoUserInfo.getId());
//        }

        // 툴킷 사용시
        if(StringUtils.hasLength(userInfo.getId())) {
        	userDetail.setKakaoUserKey(userInfo.getId());
        }
    }

	@Override
	public boolean isEqualAdminKey(String appAdminKey) {
		if (StringUtils.hasLength(kakaoAppAdminKey)
				&& StringUtils.hasLength(appAdminKey)
				&& kakaoAppAdminKey.equals(appAdminKey)) {
			return true;
		}
		return false;
	}


	private KakaoLink kakaoLoginProcess(KakaoLink kakaoLink, UserEntity userEntity, String loginAuthType) {
		KakaoLink result = new KakaoLink();

		User user = userService.getUserByLoginId(userEntity.getLoginId());

		int alternateSystemLevel = configService.selectAlternateSystem();
		if (alternateSystemLevel > 0) {								// 접속제한 실시할 경우
			String birth = ((UserDetail) user.getUserDetail()).getBirthday();
			HashMap<String, Object> checkResult = UserUtils.isAlternateSystem(alternateSystemLevel, birth);
			boolean check = Boolean.valueOf(checkResult.get("result").toString());
			if (!check) {
				StringBuffer buf = new StringBuffer();
				buf.append(birth.substring(0, 4) + "년");
				buf.append(" " + Integer.valueOf(birth.substring(4, 6)) + "월");
				buf.append(" " + Integer.valueOf(birth.substring(6, 8)) + "일");
				result.setAlternateSystemMsg(checkResult.get("resultMsg").toString() + "\n등록된 생년월일 : " + buf.toString());
				result.setGrantType("ALTERNATE_SYSTEM_DENIED");
		        result.setLoginId(user.getLoginId());
				return result;
			}
		}

		HttpServletRequest request = ThreadContextUtils.getRequestContext().getRequest();

		String loginType = "ROLE_USER";
		String token = jwtTokenService.getJwtToken(
                loginType,
                userEntity.getLoginId().trim(),
                userEntity.getPassword().trim(),
                JwtUtils.getClientIpAddress(request)
        );

//		userEntity.setKakaoUserKey(kakaoLink.getId());		// 툴킷 미사용시

		if ("KAKAO".equals(loginAuthType)) {
			userEntity.setKakaoUserKey(kakaoLink.getKakaoAccount().getId());		// 툴킷 사용시
		} else if ("NAVER".equals(loginAuthType)) {
			userEntity.setNaverUserKey(kakaoLink.getKakaoAccount().getId());		// 툴킷 사용시
		}
		userEntity.setPasswordType("P");										// 비밀번호 만료일 체크하지 않도록 처리추가

		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String loginDate = format.format(LocalDateTime.now());
		userEntity.setLoginDate(loginDate);

		userRepository.save(userEntity);

//        shopSecurityService.updateLoginCount(user);
        shopSecurityService.updateClearLoginFailCountForUser(user.getLoginId());

        loginLogService.insertLoginLogByUser(request, user.getLoginId(), true);

        String sign = (String)JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_SIGN));
        userService.saveLoginSessionForUser(sign, userEntity.getUserId());

        result.setLoginId(user.getLoginId());
		result.setToken(token);

		return result;
	}


	private void insertUserAndUserDetail(User user, UserDetail userDetail) {

		long userId = user.getUserId();
		String userName = user.getUserName();
		userService.insertUser(user);
		userService.insertUserDetail(userDetail);

		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		userRole.setAuthority("ROLE_USER");
		userRoleMapper.insertUserRole(userRole);

		user = userService.getUserByUserId(userId);

		joinMapper.updateUserInfo(userDetail);
//		userMapper.updateUserEtcInfo(userDetail);

		// 2022.02.13 loginPathCode 추가
		joinMapper.updateloginPathCode(userDetail);

		userDetail.setUserId(userId);

//		if(userDetail.getLocGovList() !=null) {
//			for(int i =0; i<userDetail.getLocGovList().length; i++) {
//				LocGovInfo locGovInfo = new LocGovInfo();
//				locGovInfo.setUserId(String.valueOf(userDetail.getUserId()));
//				locGovInfo.setLocgovCode(userDetail.getLocGovList()[i]);
//				JoinMapper.insertIntrstLocgovInfo(locGovInfo);
//			}
//		}
//
//		if(userDetail.getRtnpsntList() !=null) {
//			for(int i =0; i<userDetail.getRtnpsntList().length; i++) {
//				RtnpsntInfo rtnpsntInfo = new RtnpsntInfo();
//				rtnpsntInfo.setLocgovCode(userDetail.getLocGovCode());
//				rtnpsntInfo.setUserId(String.valueOf(userDetail.getUserId()));
//				rtnpsntInfo.setCategoryCode(userDetail.getRtnpsntList()[i]);
//				JoinMapper.insertInststRtnpsntInfo(rtnpsntInfo);
//			}
//		}

//		userMapper.insertUserBirthday(userDetail);		// 통계용 생년월일 추가

		// 국민비서 알리미 추가
		GiveUserSmsInfo info = new GiveUserSmsInfo();
		info.setUserId(userId);
		info.setUserName(userName);
		info.setMberCi(userDetail.getMberCi());
		info.setReceiveSms("0");
		info.setPhoneNumber(userDetail.getPhoneNumber().replaceAll("-", ""));
		smsIpsService.giveSendSms(Arrays.asList(info), SmsType.JOIN_MEMBERSHIP);

	}

	@Override
	public KakaoLink kakaoLinkSecedeByUserId(long userId, String leaveCode, String leaveReason) {
		KakaoLink result = new KakaoLink();
		KakaoLink kakaoLink = kakaoLinkMapper.selectLoginPathCode(userId);

		if (kakaoLink == null) {
			result.setApiError(ApiError.NOT_VALID_LOGIN);
		} else {
			int loginPathCodeInt = Integer.valueOf(kakaoLink.getLoginPathCode());

//			if (!"500".equals(kakaoLink.getLoginPathCode()) && !"600".equals(kakaoLink.getLoginPathCode())) {
			if (loginPathCodeInt < 500) {
				result.setApiError(ApiError.NOT_VALID_LOGIN);
			} else {
				secedeKakaoUser(userId, leaveCode, leaveReason);

				if (loginPathCodeInt == 500) {		// 카카오
					try {
	//					result = kakaoLinkClear(kakaoLink);
						result = kakaoLinkToolkitClear(kakaoLink);
					} catch (IOException e) {
	//					result.setApiError(ApiError.BAD_REQUEST);
	//					throw new UserException("카카오 연동 해제에 실패했습니다.");
					}

				}
			}
		}
		return result;
	}


	private void secedeKakaoUser(long userId, String leaveCode, String leaveReason) {

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
		// 2-0. 회원 CI 값 별도 보관
		generalCustomerMapper.insertSecedeCustomer(generalCustomerSecede.getUserId());

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

	// 카카오 통신하여 토큰 획득
	private KakaoLink getToolkitAuth(String kakaoCode, String type) throws UserException {
		StringBuffer paramBuf = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();

//		String clientSecretKey = RandomStringUtils.getRandomString("", 4, 10);
//
//		addParam(paramBuf, "grant_type", "authorization_code");					// authorization_code 고정값, 필수
//		addParam(paramBuf, "client_id", restApiKey);							// 앱 REST API 키, 필수
		if ("JOIN".equalsIgnoreCase(type)) {
			addParam(paramBuf, "redirect_uri", redirectUri2);						// 인가 코드가 리다이렉트 된 URI, 필수
		} else {
			addParam(paramBuf, "redirect_uri", redirectUri);						// 인가 코드가 리다이렉트 된 URI, 필수
		}
		addParam(paramBuf, "code", kakaoCode);									// 카카오 로그인 후 수신된 인가 코드, 필수
//		addParam(paramBuf, "client_secret", clientSecretKey);				// 보안 강화용 키, 카카오 개발자 사이트에서 On 상태일 경우 필수

//		paramBuf.append("{\"code\" : \"" + kakaoCode + "\"}");
//		addParam(paramBuf, "code", kakaoCode);

//		JsonObject json = new JsonObject();
//		json.addProperty("code", kakaoCode);

		try {
			KakaoLink responseData = new KakaoLink();
			String responseDataStr = restApiWithToolkit("/api/s1/login/request/SAK201", paramBuf.toString(), "POST", isKakaoLoginByNaverToolkit);

			KakaoToolkit kakaoToolkit = mapper.readValue(responseDataStr, KakaoToolkit.class);
			KakaoToolkitData data;

			String status;

			if (kakaoToolkit != null && kakaoToolkit.getData() != null) {
				data = kakaoToolkit.getData();
				status = kakaoToolkit.getStatus();
			} else {
				data = mapper.readValue(responseDataStr, KakaoToolkitData.class);
				status = data.getStatus();
			}

			if ("COMPLETED".equalsIgnoreCase(status) && "Y".equalsIgnoreCase(data.getResult())) {
				KakaoAccount kakaoAccount = data.getProfile();
				kakaoAccount.setMberCi(data.getCi());
				responseData.setKakaoAccount(kakaoAccount);
				responseData.setRequestToken(data.getRequestToken());
			}

			return responseData;
		} catch (IOException e) {
			log.error(getClass().getName() + " getKakaoToken IOException ::", e);
			throw new UserException("카카오 token 획득에 실패했습니다.");
		} catch (UserException e) {
			log.error(getClass().getName() + " getKakaoToken UserException ::", e);
			throw new UserException(e.getErrorMessage());
		}
	}

	private KakaoLink kakaoLinkToolkitClear(KakaoLink kakaoLink) throws IOException {
		StringBuffer paramBuf = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();

		addParam(paramBuf, "user_id", kakaoLink.getKakaoUserKey());
		try {
			return mapper.readValue(restApiWithToolkit("/api/s1/login/unlink", paramBuf.toString(), "POST", isKakaoLoginByNaverToolkit), KakaoLink.class);
		} catch (IOException e) {
			log.error(getClass().getName() + " kakaoLinkToolkitClear error ::");
			throw new IOException(e);
		}
	}


//	// 카카오 통신하여 토큰 획득
//	private KakaoLink getToolkitAuthHardcoding(String kakaoCode, String type) throws UserException {
//		KakaoLink kakaoLink = new KakaoLink();
//		KakaoAccount kakaoAccount = new KakaoAccount();
//		kakaoAccount.setName("카카오");
//		kakaoAccount.setHasEmail(true);
//		kakaoAccount.setEmailNeedsAgreement(false);
//		kakaoAccount.setEmailValid(true);
//		kakaoAccount.setEmailVerified(true);
//		kakaoAccount.setEmail("kakao@kakao.com");
//		kakaoAccount.setHasPhoneNumber(true);
//		kakaoAccount.setPhoneNumberNeedsAgreement(false);
//		kakaoAccount.setPhoneNumber("+82 10-1234-5678");
//		kakaoAccount.setHasBirthyear(true);
//		kakaoAccount.setBirthdayNeedsAgreement(false);
//		kakaoAccount.setBirthyear("2000");
//		kakaoAccount.setHasBirthday(true);
//		kakaoAccount.setBirthdayNeedsAgreement(false);
//		kakaoAccount.setBirthday("0101");
//		kakaoAccount.setBirthdayType("SOLAR");
//		kakaoAccount.setMberCi("testCi");
//		kakaoLink.setKakaoAccount(kakaoAccount);
//		kakaoLink.setKakaoUserKey("-1234");
//
//		return kakaoLink;
//	}

	private String restApiWithToolkit(String subUrl, String param, String requestMethod, boolean isNaverToolkit) throws IOException {
		HttpURLConnection conn = null;
		OutputStream wr1 = null;

		String toolkitUrl;

		if (isNaverToolkit) {
			toolkitUrl = kakaoToolkitUrl2;
		} else {
			toolkitUrl = kakaoToolkitUrl;
		}

		try {
			URL url;
			if ("POST".equals(requestMethod)) {
				url = new URL(toolkitUrl + subUrl);
			} else {
				url = new URL(toolkitUrl + subUrl + "?" + param);
			}

			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(requestMethod); // 전송 방식
			conn.setRequestProperty("Authorization", "SsolAuth " + kakaoToolkitAccessToken);
			if ("POST".equals(requestMethod)) {
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			}
			conn.setDoOutput(true);
			conn.setConnectTimeout(5000);	// 5초
			conn.setReadTimeout(5000);	// 5초

			if ("POST".equals(requestMethod)) {
				wr1 = conn.getOutputStream();
				wr1.write(param.getBytes("UTF-8"));
				wr1.flush();
				wr1.close();
			}

			try(
					InputStream is = conn.getInputStream();
					InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
					BufferedReader br = new BufferedReader(isr);
				) {
				String inputLine;
				StringBuffer sb = new StringBuffer();
				while ((inputLine = br.readLine()) != null) {
					sb.append(inputLine);
				}
				br.close();

				return sb.toString();
			} catch (IOException e) {
				throw new IOException(e);
			}
		} catch (IOException e) {
			log.error(getClass().getName() + " restApiWithToolkit error :: param :: " + param);
			throw new IOException(e);
		} finally {
			if (conn != null) {conn.disconnect();}
			if (wr1 != null) {wr1.close();}
		}
	}

	/**
	 * 네이버 로그인 URL 조회
	 */
	@Override
	public NaverToolkitData getNaverLoginUrl(boolean isJoin) {
		StringBuffer paramBuf = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();

		if (isJoin) {
			addParam(paramBuf, "callback_url", URLEncoder.encode(serverDomain + "/users/join.html", Charset.forName("UTF-8")));
		} else {
			addParam(paramBuf, "callback_url", URLEncoder.encode(serverDomain + "/users/login.html", Charset.forName("UTF-8")));
		}
		try {
			return mapper.readValue(restApiWithToolkit("/api/s1/login/request/SAN201", paramBuf.toString(), "GET", true), NaverToolkitData.class);
		} catch (IOException | NullPointerException e) {
			log.error(getClass().getName() + " getNaverLoginUri IOException ::");
			throw new UserException("네이버 인증 로그인 페이지 정보 조회에 실패했습니다.");
		}
	}

	/**
	 * 네이버 인증 로그인 URL 조회
	 */
	@Override
	public NaverToolkitData getNaverPollingUrl(String code, boolean isJoin) {
		StringBuffer paramBuf = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();

		addParam(paramBuf, "code", code);
		addParam(paramBuf, "auth_type", "authorization");
		addParam(paramBuf, "device_type", "WEB");
		if (isJoin) {
			addParam(paramBuf, "callback_url", URLEncoder.encode(serverDomain + "/users/join.html", Charset.forName("UTF-8")));
		} else {
			addParam(paramBuf, "callback_url", URLEncoder.encode(serverDomain + "/users/login.html", Charset.forName("UTF-8")));
		}

		try {
			NaverToolkitData result = mapper.readValue(restApiWithToolkit("/api/s1/login/token-auth", paramBuf.toString(), "GET", true), NaverToolkitData.class);
			if (StringUtils.hasLength(result.getTxId())
					&& StringUtils.hasLength(result.getAccessToken())) {
				kakaoLinkMapper.deleteNaverAuthLoginInfoByAccessToken(result.getAccessToken());
				kakaoLinkMapper.deleteNaverAuthLoginInfoByTxId(result.getTxId());

				kakaoLinkMapper.insertNaverAuthLoginInfo(result);
			} else {
				throw new UserException("네이버 인증 로그인 페이지 정보 조회에 실패했습니다.");
			}
			return result;
		} catch (IOException | NullPointerException e) {
			log.error(getClass().getName() + " getNaverPollingUrl IOException ::");
			throw new UserException("네이버 인증 로그인 페이지 정보 조회에 실패했습니다.");
		}
	}

	/**
	 * 네이버 인증 로그인 사용자 정보 조회
	 */
	@Override
	public KakaoLink getNaverUserInfo(String txId, boolean isJoin) {
		StringBuffer paramBuf = new StringBuffer();
		ObjectMapper mapper = new ObjectMapper();

		NaverToolkitData naverAuthInfo = kakaoLinkMapper.selectNaverAuthLoginInfoByTxId(txId);

		if (naverAuthInfo == null) {
			throw new UserException("유효한 인증 정보가 아닙니다. 다시 진행해주세요.");
		}

		addParam(paramBuf, "tx_id", txId);
		addParam(paramBuf, "access_token", naverAuthInfo.getAccessToken());
		if (isJoin) {
			addParam(paramBuf, "callback_url", URLEncoder.encode(serverDomain + "/users/join.html", Charset.forName("UTF-8")));
		} else {
			addParam(paramBuf, "callback_url", URLEncoder.encode(serverDomain + "/users/login.html", Charset.forName("UTF-8")));
		}

		try {
			return mapper.readValue(restApiWithToolkit("/api/n1/sign/verify-login", paramBuf.toString(), "GET", true), NaverToolkitData.class).convertToKakaoLink();
		} catch (IOException | NullPointerException e) {
			log.error(getClass().getName() + " getNaverUserInfo IOException ::");
			throw new UserException("네이버 인증 로그인 페이지 정보 조회에 실패했습니다.");
		}
	}

	/**
	 * 네이버 인증 로그인 사용자 정보로 회원가입 또는 로그인 처리
	 */
	@Override
	public KakaoLink naverAuthProcess(String txId, boolean isJoin) {
		return kakaoLinkJoinAndLoginProcess(getNaverUserInfo(txId, isJoin), "NAVER", true);
	}



}
