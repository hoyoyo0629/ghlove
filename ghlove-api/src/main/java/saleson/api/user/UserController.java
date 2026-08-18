package saleson.api.user;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.OpUserDetails;
import com.onlinepowers.framework.security.userdetails.User;

import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.privacy.pCrypto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import OACX.OacxUtil;
import OACX.util.json.JSONObject;
import OACX.util.json.parser.JSONParser;
import OACX.util.json.parser.ParseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import saleson.api.auth.domain.ChangePassword;
import saleson.api.auth.domain.IntrstLocGovInfo;
import saleson.api.auth.domain.UserInfo;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.mypage.domain.CntrPointInfo;
import saleson.api.user.domain.UserByCI;
import saleson.api.user.domain.UserBySign;
import saleson.common.enumeration.SmsType;
import saleson.common.exception.InvalidAuthenticationException;
import saleson.common.security.api.JwtCode;
import saleson.common.security.api.JwtTokenService;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.utils.JwtUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.config.ConfigService;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.mypage.MyPageService;
import saleson.shop.mypage.domain.CntrPoint;
import saleson.shop.mypage.support.CntrPointParam;
import saleson.shop.user.GeneralCustomerService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.GeneralCustomerSecede;
import saleson.shop.user.domain.GeneralCustomerSecedeResult;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserInfoByCI;
import saleson.shop.user.domain.UserInfoBySign;
import saleson.shop.user.domain.UserModifyInfo;
import saleson.shop.user.domain.UserPasswordType;
import saleson.shop.user.support.AuthInfo;
import saleson.shop.user.support.ChangePasswordForNoLoginUser;
import saleson.shop.user.support.FinancInfo;
import saleson.shop.user.support.SignInfo;


@RestController("ApiUserController")
@RequestMapping("/api/user")
public class UserController {

	private Logger log = LoggerFactory.getLogger(UserController.class);

	public static final String STATUS = "status";

	private static final String SPECIALS = "!@#$%^&*(),.?\":{}|<>[]\\\\/;'+=_-";

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GeneralCustomerService generalCustomerService;

    @Autowired
    private MyPageService myPageService;

	@Value("${simepleAuth.jsonpath}")
	String simpleAuthJsonPath;

    @Autowired
    private SmsIpsService smsIpsService;

    @Autowired
    private ConfigService configService;

	/**
	 * 회원 탈퇴를 위한 초기 정보
	 * */
	@PostMapping("/getSecedeInfo")
	public ResponseEntity getSecedeInfo() {
		ResponseEntity result = null;

		try {
			CntrPointParam gntrPointParam = new CntrPointParam();
			gntrPointParam.setUserId(UserUtils.getUserId());
			gntrPointParam.setPagination(null);

			List<CntrPoint> list = myPageService.getCntrPointInfo(gntrPointParam);
			List<CntrPointInfo> pointList = list.stream().map(cntrPoint -> new CntrPointInfo(cntrPoint)).collect(Collectors.toList());

			UserModifyInfo userInfo = userService.getUserModifyInfo(gntrPointParam.getUserId());

			result = ApiResponseEntity.data()
					.put("loginId", UserUtils.getLoginId())
					.put("userName", UserUtils.getUser().getUserName())
					.put("leaveCodeList", CodeUtils.getCodeInfoList("LEAVE_CODE"))
					.put("pointList", pointList)
					.put("loginPathCode", userInfo.getLoginPathCode())
					.put("status", HttpStatus.OK).ok();
		} catch (NullPointerException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 사용자 비밀번호 변경 전 비밀번호 확인
	 *
	 * @param presentPassword
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/confirmPresentPassword")
    public ResponseEntity confirmPresentPassword(@RequestBody String presentPassword, HttpServletRequest request) {
        ResponseEntity result = null;
        if(SecurityUtils.getCurrentUser() == null) {
        	return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        }

        try {
            if (ObjectUtils.isEmpty(presentPassword)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            User user = getUserByToken(request);
            if (user == null) {
                throw new InvalidAuthenticationException("사용자 없음");
            }
            Long userId = user.getUserId();
            Boolean confirmPassword = userService.confirmUserPassword(userId, presentPassword);
        	result = ApiResponseEntity
        			.data()
        			.put(STATUS, HttpStatus.OK)
        			.put("confirmPassword", confirmPassword)
        			.ok();

        } catch (InvalidAuthenticationException e) {
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getMessage());
        } catch (NullPointerException e) {
            if (tokenService.isJwtException(e)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }


	/**
	 * 사용자 비밀번호 변경
	 *
	 * @param password
	 * @return
	 */
	@PostMapping("/changeUserPassword")
    public ResponseEntity changeUserPassword(@RequestBody ChangePassword changePassword, HttpServletRequest request) {
        ResponseEntity result = null;

        if(SecurityUtils.getCurrentUser() == null) {
        	return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        }

        try {

            String password = changePassword.getPassword();
            String corfirmPassword = changePassword.getCorfirmPassword();

            //password 에 대한 비밀 번호 검증
            Boolean serverCheck = this.passwordCheckRegexAll(password);


            if(!serverCheck) {
            	return ApiResponseEntity.error(ApiError.BAD_REQUEST, "규칙에 맞지 않은 비밀번호 입니다.");
            }

            if (ObjectUtils.isEmpty(password)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            if (!password.equals(corfirmPassword)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_FAIL_PASSWD);
            }

            User user = getUserByToken(request);

            if (user == null) {
                throw new InvalidAuthenticationException("사용자 없음");
            }

            //기존 ci가입 여부 확인
            AuthInfo authInfo = new AuthInfo();
            authInfo.setMberCi(changePassword.getMberCi());
            UserInfoByCI ciInfo = userService.getUserInfoByCi(authInfo);

            if(ciInfo==null) {
            	return ApiResponseEntity.error(ApiError.NOT_EXIST_AUTH);
            }else {
            	if(!user.getLoginId().equals(ciInfo.getLoginId())) {
            		return ApiResponseEntity.error(ApiError.NOT_EXIST_AUTH);
            	}
            }

            userService.updateUserPassword(user.getUserId(), password);

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();


            // SMS 전송
            GiveUserSmsInfo info = new GiveUserSmsInfo();
            info.setUserId(user.getUserId());
            info.setUserName(ciInfo.getUserName());
            info.setMberCi(ciInfo.getMberCi());
            // 수신여부 동의와 관계없이 전송
            info.setReceiveSms("0");
            info.setPhoneNumber(UserUtils.getUserDetail().getPhoneNumber().replaceAll("-", ""));
            smsIpsService.giveSendSms(Arrays.asList(info), SmsType.PASSWORD_CHANGE_COMPLETE);


        } catch (InvalidAuthenticationException e) {
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);

        } catch (UserException e) {
           // return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getMessage());
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getMessage());
        } catch (NullPointerException e) {

            if (tokenService.isJwtException(e)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

	/**
	 * 비밀번호 검증 규칙
	 *
	 * @param password
	 * @return
	 */
	private Boolean passwordCheckRegexAll(String password) {

		String length = "(?=.{9,20}$)";
		String hasDigit = "(?=.*\\d)";
		String hasLetter = "(?=.*[A-Za-z])";
		String hasSpecial = "(?=.*[" + Pattern.quote(SPECIALS) + "])";
		String forbidUser = "";
		String userId = UserUtils.getLoginId();

		if( userId !=null && !userId.isEmpty() ) {
			forbidUser = "(?!.*(?i)" + Pattern.quote(userId) + ")";
		}

		String forbidAlphaSeq = this.builAlphaSeqNegativeLookUp(); //문자 순번
		String forbidDidgitSeq = this.builDigitSeqNegativeLookUp(); //숫자 순번
		String forbidTripleSame = "(?!.*(.)\\1\\1)"; //동일 문자 3회

		String fullRegex = "^"+ length + hasDigit
				+ hasLetter + hasSpecial
				+ forbidUser + forbidAlphaSeq
				+ forbidDidgitSeq + forbidTripleSame
				+ ".*$";


		return Pattern.compile(fullRegex).matcher(password).matches();
	}

	/**
	 * 비밀번호 검증 랸속된숫자 비교
	 *
	 * @return
	 */
	private String builDigitSeqNegativeLookUp() {
		String digits = "0123456789";

		List<String> asc = new ArrayList<>();
		List<String> desc = new ArrayList<>();

		for ( int i = 0 ; i <= digits.length() -3 ; i++) {
			String trip = digits.substring(i, i + 3);
			asc.add(trip);
			desc.add(new StringBuilder(trip).reverse().toString());
		}
		String alternation = asc.stream().collect(Collectors.joining("|")) + "|" + desc.stream().collect(Collectors.joining("|"));

		return "(?!.*(?:" + alternation + "))";
	}

	/**
	 * 비밀번호 검증 랸속된문자 비교
	 *
	 * @return
	 */
	private String builAlphaSeqNegativeLookUp() {
		String letters = "abcdefghijklmnopqrstuvwxyz";

		List<String> asc = new ArrayList<>();
		List<String> desc = new ArrayList<>();

		for ( int i = 0 ; i <= letters.length() -3 ; i++) {
			String trip = letters.substring(i, i + 3);
			asc.add(trip);
			desc.add(new StringBuilder(trip).reverse().toString());
		}
		String alternation = String.join("|", asc) + "|" + String.join("|", desc);

		//대소문자 무시
		return "(?!.*(?i)(?:" + alternation + "))";
	}

	@PostMapping("/changeUserPasswordForNoLogin")
    public ResponseEntity changeUserPasswordForNoLogin(@RequestBody ChangePasswordForNoLoginUser changePassword, HttpServletRequest request) {
        ResponseEntity result = null;

        try {

            String password = changePassword.getPassword();
            String corfirmPassword = changePassword.getCorfirmPassword();

            if (ObjectUtils.isEmpty(password) || !password.equals(corfirmPassword)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            if (!password.equals(corfirmPassword)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_FAIL_PASSWD);
            }

            //기존 ci가입 여부 확인
            AuthInfo authInfo = new AuthInfo();
            authInfo.setMberCi(changePassword.getMberCi());
            UserInfoByCI ciInfo = userService.getUserInfoByCi(authInfo);

            if(ciInfo==null) {
            	return ApiResponseEntity.error(ApiError.NOT_EXIST_AUTH);
            }else {
            	if(!changePassword.getLoginId().equals(ciInfo.getLoginId())) {
            		return ApiResponseEntity.error(ApiError.NOT_EXIST_AUTH);
            	}
            }

            userService.changeUserPasswordForNoLogin(changePassword);

            result = ApiResponseEntity.data()
            		.put(STATUS, HttpStatus.OK).ok();

        } catch (InvalidAuthenticationException e) {
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);

        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getMessage());
        } catch (NullPointerException e) {

            if (tokenService.isJwtException(e)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

	 private User getUserByToken(HttpServletRequest request) throws UserException {
        String token = JwtUtils.getToken(request);

        if (ObjectUtils.isEmpty(token)) {
            throw new InvalidAuthenticationException("토큰이 존재하지 않음");
        }

        Jws<Claims> claims = tokenService.getClaimsByToken(token);

        // 자체 시스템 인증 시작
        if (!tokenService.isTokenAuthentication(token, claims)) {
            throw new InvalidAuthenticationException("토큰 자체인증 실패");
        }

        String loginId = (String) JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_ID));
        String loginType = (String) JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_LOGIN_TYPE));

        if (!"ROLE_USER".equals(loginType)) {
            throw new InvalidAuthenticationException("loginType 이 다름 > " + loginType);
        }

        return userService.getUserByLoginId(loginId);
    }

	 /**
     * 회원탈퇴 API (leaveReason)
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/secede")
    public ResponseEntity secede(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        ResponseEntity result = null;

        try {

        	if (ObjectUtils.isEmpty(userInfo.getLoginId()) || ObjectUtils.isEmpty(userInfo.getPassword())) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            User user = getUserByToken(request);

            if (!passwordEncoder.matches(pCrypto.Encrypt("hash.5", userInfo.getPassword(), ""), user.getPassword())) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_FAIL_PASSWD);
            }

            if (!userInfo.getLoginId().equals(user.getLoginId()) || !passwordEncoder.matches(pCrypto.Encrypt("hash.5", userInfo.getPassword(), ""), user.getPassword())) {
                return ApiResponseEntity.error(ApiError.NOT_VALID_LOGIN);
            }

            GeneralCustomerSecede generalCustomerSecede = new GeneralCustomerSecede();
            generalCustomerSecede.setLeaveCode(userInfo.getLeaveCode());
            generalCustomerSecede.setLeaveReason(userInfo.getLeaveReason());
            generalCustomerSecede.setUserId(user.getUserId());

            GeneralCustomerSecedeResult resultCode = generalCustomerService.updateGeneralCustomerSecedeProcess(generalCustomerSecede);

            if(!"SUCC".equals(resultCode.getCode())) {
            	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            	return result;
            }

            // 회원탈퇴 국민비서 SMS
            GiveUserSmsInfo info = new GiveUserSmsInfo();
            info.setUserId(user.getUserId());
            info.setUserName(user.getUserName());
            info.setMberCi(UserUtils.getUserDetail().getMberCi());
            info.setReceiveSms("0");
            info.setPhoneNumber(UserUtils.getUserDetail().getPhoneNumber().replaceAll("-", ""));
            smsIpsService.giveSendSms(Arrays.asList(info), SmsType.SECESSION);

            // 로그인 한 경우 세션정보 업데이트 (우선 이렇게 처리 - 추후 변경 로직 추가 하자!!)
    		if (UserUtils.isUserLogin()) {
    			UserDetail userDetailInfo = userService.getUserDetail(user.getUserId());

    			// 회원 정보에 추가 정보가 있는 경우 조회하여 설정해줌
    			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    			Object principal = auth.getPrincipal();

    			if (principal instanceof UserDetails) {
    				((OpUserDetails) principal).getUser().setUserName(user.getUserName());
    				((OpUserDetails) principal).getUser().setEmail(user.getEmail());
    				((OpUserDetails) principal).setUserDetail(userDetailInfo);

    				// Authentication 인증 객체 업데이트, 변경시에는 인증 객체 재설정이 필요함
    				Authentication newAuth = new UsernamePasswordAuthenticationToken(principal, auth.getCredentials(), auth.getAuthorities());
    				SecurityContextHolder.getContext().setAuthentication(newAuth);
    			}
    		}

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (UserException | UnsupportedEncodingException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 회원정보 조회 API
     *
     * @return
     */
    @PostMapping("/getUserInfo")
    public ResponseEntity getUserInfo(HttpServletRequest request) {
        ResponseEntity result = null;
        Map<String, Object> info = new HashMap<>();

        try {

            User user = getUserByToken(request);
            UserDetail userDetail = (UserDetail) user.getUserDetail();

            if (userDetail.getPhoneNumber() != null) {
                if ("--".equals(userDetail.getPhoneNumber())) {
                    userDetail.setPhoneNumber("");
                }
                if ("--".equals(userDetail.getTelNumber())) {
                    userDetail.setTelNumber("");
                }
                if ("-".equals(userDetail.getPost())) {
                    userDetail.setPost("");
                }
            }

            UserModifyInfo userInfo = userService.getUserModifyInfo(user.getUserId());

            List<LocGovInfo> interestLocGovList = userService.getIntrstLocgovList(user.getUserId());

            List<IntrstLocGovInfo> interestLocGov = new ArrayList<>();

            if(!interestLocGovList.isEmpty()) {
            	for(LocGovInfo locGovInfo : interestLocGovList) {
            		interestLocGov.add(new IntrstLocGovInfo(locGovInfo));
            	}
            }

            result = ApiResponseEntity.data()
            		.put("userInfo", userInfo)
            		.put("phoneCodes", CodeUtils.getCodeInfoList("PHONE"))
            		.put("emailCodes", CodeUtils.getCodeInfoList("EMAIL"))
            		.put("wdrList", CodeUtils.getCodeInfoList("WDR"))
            		.put("interestLocGov", interestLocGov)
            		.ok();
        } catch (UserException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 회원정보 수정 API
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/modifyUser")
    public ResponseEntity<?> modifyUser(@RequestBody @Valid UserInfo userInfo, BindingResult bindingResult) {
        if (userInfo == null) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        }

        ResponseEntity<?> result = null;

        try {
            User user = new User();
            UserDetail userDetail = new UserDetail();

            String email = ObjectUtils.isEmpty(userInfo.getEmail()) ? user.getEmail() : userInfo.getEmail();

            userModifyDataSetData(userInfo, userDetail);

            user.setUserId(UserUtils.getUserId());
            user.setUserName(userInfo.getUserName());
            user.setEmail(email);
            user.setPassword(userInfo.getPassword());

//            userDetail.processHyphen();
            user.setUserDetail(userDetail);

            if (bindingResult.hasErrors()) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            userService.updateFrontUserAndUserDetail(user, userDetail);

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    private void userModifyDataSetData(UserInfo userInfo, UserDetail userDetail) {
        userDetail.setUserId(UserUtils.getUserId());

        if (!ObjectUtils.isEmpty(userInfo.getPhoneNumber())) {
            userDetail.setPhoneNumber(ShopUtils.phoneNumberPattern(userInfo.getPhoneNumber()));
        }

        userDetail.setBirthday(userInfo.getBirthday());

        userDetail.setPost(userInfo.getPost());

        if (!ObjectUtils.isEmpty(userInfo.getNewPost())) {
            userDetail.setNewPost(userInfo.getNewPost());
        }

        if (!ObjectUtils.isEmpty(userInfo.getAddress())) {
            userDetail.setAddress(userInfo.getAddress());
        }

        if (!ObjectUtils.isEmpty(userInfo.getAddressDetail())) {
            userDetail.setAddressDetail(userInfo.getAddressDetail());
        }

        if(!ObjectUtils.isEmpty(userInfo.getBirthdayFull())) {
        	userDetail.setBirthdayFull(userInfo.getBirthdayFull());
        }

        if (!ObjectUtils.isEmpty(userInfo.getReceiveEmail())) {
            userDetail.setReceiveEmail(userInfo.getReceiveEmail());
        }
        if (!ObjectUtils.isEmpty(userInfo.getReceiveSms())) {
            userDetail.setReceiveSms(userInfo.getReceiveSms());
        }
        if (!ObjectUtils.isEmpty(userInfo.getReceivePbanc())) {
            userDetail.setReceivePbanc(userInfo.getReceivePbanc());
        }
        if(!ObjectUtils.isEmpty(userInfo.getLocGovList())) {
        	userDetail.setLocGovList(userInfo.getLocGovList());
        }

        if(!ObjectUtils.isEmpty(userInfo.getLocgovCode())) {
        	userDetail.setLocgovCode(userInfo.getLocgovCode());
        }
        if (!ObjectUtils.isEmpty(userInfo.getReceiveKakao())) {
            userDetail.setReceiveKakao(userInfo.getReceiveKakao());
        }
    }

    /**
     * 회원 휴대폰 인증 업데이트
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/modifyMobileAuth")
    public ResponseEntity modifyMobileAuth(@RequestBody AuthInfo authInfo, HttpServletRequest request) {
        ResponseEntity result = null;

        try {

        	//기존 ci가입 여부 확인
            UserInfoByCI ciInfo = userService.getUserInfoByCi(authInfo);

            if(ciInfo!=null) {
            	return ApiResponseEntity.error(ApiError.DUPLICATION_CI);
            }

        	userService.modifyMobileAuth(authInfo);

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 회원 휴대폰 인증 정보 확인
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/checkMobileAuth")
    public ResponseEntity checkMobileAuth(@RequestBody AuthInfo authInfo, HttpServletRequest request) {
        ResponseEntity result = null;

        try {

        	UserInfoByCI userInfo = new UserInfoByCI();
        	UserByCI info = new UserByCI();

    		userInfo = userService.getUserInfoByCi(authInfo);

    		if(userInfo!=null) {
    			info.setLoginId(userInfo.getLoginId());
    			info.setUserKeyYN(userInfo.getUserKeyYN());
    		}

            result = ApiResponseEntity.data()
            		.put("info", info)
            		.put(STATUS, HttpStatus.OK).ok();
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 회원 휴대폰 인증 정보 확인(비밀번호 찾기(비로그인 상태)에서 디지털원패스 사용자여부 판단
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/checkMobileAuthPwd")
    public ResponseEntity checkMobileAuthPwd(@RequestBody AuthInfo authInfo, HttpServletRequest request) {
        ResponseEntity result = null;

        try {

        	UserInfoByCI userInfo = new UserInfoByCI();
        	UserByCI info = new UserByCI();

    		userInfo = userService.getUserInfoByCi(authInfo);

    		if(userInfo!=null) {
    			info.setLoginId(userInfo.getLoginId());
    			info.setUserKeyYN(userInfo.getUserKeyYN());
    		}
            result = ApiResponseEntity.data()
            		.put("info", info)
            		.put(STATUS, HttpStatus.OK).ok();
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @PostMapping("/changeUserPasswordLater")
    public ResponseEntity changeUserPasswordLater(@RequestBody ChangePasswordForNoLoginUser changePassword) {
        ResponseEntity result = null;

        try {
			String strLoginId = changePassword.getLoginId();

        	FinancInfo financInfo = new FinancInfo();
        	financInfo.setMberCi(changePassword.getMberCi());

        	if(strLoginId == "") {
    			//FinancResultInfo resultInfo = userService.getLoginCiInfo(financInfo);
    			Map<String, Object> resultInfo = userService.getLoginCiInfo(financInfo);
        		//changePassword.setUserId(resultInfo.getUserId());
        		//changePassword.setLoginId(resultInfo.getLoginId());
    			long userId = (long) resultInfo.get("userId");
    			changePassword.setUserId(userId);
        		changePassword.setLoginId(resultInfo.get("loginId").toString());
        	}

            userService.changeUserPasswordLater(changePassword);

            result = ApiResponseEntity.data()
            		.put(STATUS, HttpStatus.OK).ok();

        } catch (InvalidAuthenticationException e) {
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);

        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, "비밀번호 만료일자 변경에 실패했습니다.");
        } catch (NullPointerException e) {
            if (tokenService.isJwtException(e)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 인증서 등록
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/signRegister")
    public ResponseEntity signRegister(@RequestBody SignInfo signInfo) {
        ResponseEntity result = null;

        try {

            userService.signRegisterForUser(signInfo);

            result = ApiResponseEntity.data()
            		.put(STATUS, HttpStatus.OK).ok();

        } catch (UserException e) {
        	log.error("UserController-signRegister : {}",e.getStackTrace()[0]);
            return ApiResponseEntity.error(ApiError.NOT_JOIN_USER, "signRegisterForUser UserException");
        } catch (NullPointerException e) {
        	log.error("UserController-signRegister : {}",e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 인증서 삭제
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/signRemove")
    public ResponseEntity signRemove(@RequestBody SignInfo signInfo) {
        ResponseEntity result = null;

        try {

            userService.signRemoveForUser(signInfo);

            result = ApiResponseEntity.data()
            		.put(STATUS, HttpStatus.OK).ok();

        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST,"signRemoveForUser UserException");
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 인증서로 id 찾기
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/checkSign")
    public ResponseEntity checkSign(@RequestBody SignInfo signInfo) {
        ResponseEntity result = null;

        try {

        	UserInfoBySign userInfo = new UserInfoBySign();
        	UserBySign info = new UserBySign();

        	userInfo = userService.checkSignForUser(signInfo);

    		if(userInfo!=null) {
    			info.setLoginId(userInfo.getLoginId());
    			info.setUserKeyYN(userInfo.getUserKeyYN());
    		}

            result = ApiResponseEntity.data()
            		.put("info", info)
            		.put(STATUS, HttpStatus.OK).ok();

        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, "checkSignForUser UserException");
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 인증서로 비밀번호 변경
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/changeUserPwdForSignNoLogin")
    public ResponseEntity changeUserPwdForSignNoLogin(@RequestBody ChangePasswordForNoLoginUser changePassword, HttpServletRequest request) {
        ResponseEntity result = null;

        try {

            String password = changePassword.getPassword();
            String corfirmPassword = changePassword.getCorfirmPassword();

            if (ObjectUtils.isEmpty(password) || !password.equals(corfirmPassword)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            if (!password.equals(corfirmPassword)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_FAIL_PASSWD);
            }

            //기존 인증서 등록 여부 확인
        	SignInfo signInfo = new SignInfo();
        	signInfo.setMberDn(changePassword.getMberDn());
        	UserInfoBySign userInfo = userService.checkSignForUser(signInfo);

    		if(userInfo==null) {
    			return ApiResponseEntity.error(ApiError.NOT_EXIST_AUTH);
    		}else {
            	if(!changePassword.getLoginId().equals(userInfo.getLoginId())) {
            		return ApiResponseEntity.error(ApiError.NOT_EXIST_AUTH);
            	}
            }

            userService.changeUserPwdForSignNoLogin(changePassword);

            result = ApiResponseEntity.data()
            		.put(STATUS, HttpStatus.OK).ok();

        } catch (InvalidAuthenticationException e) {
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);

        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST,"changeUserPwdForSignNoLogin UserException" );
        } catch (NullPointerException e) {
            if (tokenService.isJwtException(e)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }


    @PostMapping("/financPid")
    public ResponseEntity<Map<String, Object>> fincertPid(@RequestBody FinancInfo financInfo, HttpServletRequest request) throws Exception {
    	ResponseEntity<Map<String, Object>> result = null;
    	Map<String, Object> resultMap = userService.getFinancPid(financInfo);

    	result = ApiResponseEntity.data()
        		.put("result", resultMap)
        		.put("status", HttpStatus.OK)
        		.ok();

    	return result;
    }

    /**
     * 금융인증서 CI 회원정보 조회 API
     *
     * @return
     */
    @PostMapping("/getLoginCiInfo")
    public ResponseEntity<Map<String, Object>> getLoginCiInfo(@RequestBody FinancInfo financInfo, HttpServletRequest request) {
        ResponseEntity<Map<String, Object>> result = null;


        try {
			//FinancResultInfo resultInfo = userService.getLoginCiInfo(financInfo);
			Map<String, Object> resultMap = userService.getLoginCiInfo(financInfo);

			result = ApiResponseEntity.data()
					//.put("result", resultInfo)
					.put("result", resultMap)
	        		.put("status", HttpStatus.OK)
	        		.ok();
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @PostMapping("/getNonce")
    public ResponseEntity<Map<String, Object>> getNonce(){
    	ResponseEntity<Map<String, Object>> result = null;

        try {

			String resultNonce = userService.bytesToHexString();

			result = ApiResponseEntity.data()
					.put("resultNonce", resultNonce)
	        		.put("status", HttpStatus.OK)
	        		.ok();
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

	@PostMapping("/passwordtype")
    public ResponseEntity<Map<String, Object>> updatePasswordType(@RequestBody UserPasswordType userPasswordType){
		ResponseEntity<Map<String, Object>> result = null;

        try {
        	int resultCnt = userService.updatePasswordType(userPasswordType);

			result = ApiResponseEntity.data()
					.put("resultCnt", resultCnt)
	        		.put("status", HttpStatus.OK)
	        		.ok();
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @PostMapping("/simpleMberCi")
    public ResponseEntity getSimpleMberCi(@RequestBody String jsonData, HttpServletRequest request) {
    	ResponseEntity result = null;

    	Map<String, String> returnMap = new HashMap<String,String>();
    	try {
    		OacxUtil oacx = new OacxUtil();		//OacxUtil 선언

            JSONObject jsonObj = new JSONObject();
            JSONParser parse = new JSONParser();
            jsonObj = (JSONObject) parse.parse(jsonData);
            String jsonPath = simpleAuthJsonPath;
            log.debug("jsonObj" + jsonObj);

            String fn = (String) jsonObj.get("fn");
            String signTargetOri = (String) jsonObj.get("signTargetOri");
            if(fn == "signComplete" || fn == "signNonMemberComplete"){
                signTargetOri = (String) jsonObj.get("signTargetOri");
            }

            String resultMsg = "{";
            String errorMsg = "";
            String message = "";

            Map<String, Object> map = null;
            try{
                map = new ObjectMapper().readValue(jsonData, Map.class);
            } catch (JsonProcessingException e){
                errorMsg = e.toString();
                resultMsg = "msg="+errorMsg+",result=error";
                log.debug("resultMsg =" + resultMsg);
            }
            log.debug("map=" +map);

            oacx.loadJSONInfo(jsonPath);		//json 파일 정보 로드



           	//생일, 이름, 전화번호, CI 복호화
          	Map<String, String> oacxMap = oacx.decryptResult(map);

          	AuthInfo authInfo = new AuthInfo();

          	String authCi = oacxMap.get("ci");

          	if(ObjectUtils.isEmpty(authCi)) {
          		log.error("[/api/auth/getSimpleAuthResult] ERROR : {}");
    			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
          	}else {
          		authInfo.setMberCi(authCi);
                UserInfoByCI ciInfo = userService.getUserInfoByCi(authInfo);

              	result = ApiResponseEntity.data()
        				.put("data", ciInfo)
        				.put(STATUS, HttpStatus.OK).ok();
          	}
    	} catch (ParseException e) {
			log.error("[/api/auth/getSimpleAuthResult] ERROR : {}");
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		 }

    	return result;
    }

    /**
     * 회원정보 수정 API
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/modifyReceive")
    public ResponseEntity<?> modifyReceive(@RequestBody @Valid UserInfo userInfo, BindingResult bindingResult) {

        ResponseEntity<?> result = null;

        try {
        	User user = new User();
            UserDetail userDetail = new UserDetail();

            String email = ObjectUtils.isEmpty(userInfo.getEmail()) ? user.getEmail() : userInfo.getEmail();

            userModifyDataSetData(userInfo, userDetail);

            user.setUserId(UserUtils.getUserId());
            user.setUserName(userInfo.getUserName());
            user.setEmail(email);
            user.setPassword(userInfo.getPassword());

            user.setUserDetail(userDetail);

            if (bindingResult.hasErrors()) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            userService.updateUserReceive(user, userDetail);

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (NullPointerException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

}
