package saleson.api.auth;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.common.OpKeyHolder;
import com.onlinepowers.framework.common.ServiceType;
import com.onlinepowers.framework.exception.BusinessException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.isms.ConfigIsmsService;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.authentication.IdPasswordAuthenticationToken;
import com.onlinepowers.framework.security.token.TokenService;
import com.onlinepowers.framework.security.token.domain.Token;
import com.onlinepowers.framework.security.userdetails.OpUserDetails;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ShadowUtils;
import com.privacy.pCrypto;

import OACX.OacxUtil;
import OACX.util.json.JSONObject;
import OACX.util.json.parser.JSONParser;
//import OACX.OacxUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import kr.go.onepass.client.dto.api.send.OnepassUserResponse;
import kr.go.onepass.client.dto.saml.OnepassResponse;
import kr.go.onepass.client.handler.api.ApiSendHandler;
import kr.go.onepass.client.handler.saml.OnepassRequestHandler;
import kr.go.onepass.client.handler.saml.OnepassResponseHandler;
import saleson.api.auth.domain.ChangePassword;
import saleson.api.auth.domain.FindUser;
import saleson.api.auth.domain.SendAuthNumber;
import saleson.api.auth.domain.UserInfo;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.siren24.SciPhoneUtils;
import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.exception.InvalidAuthenticationException;
import saleson.common.exception.ProcessApiException;
import saleson.common.exception.SecureServletException;
import saleson.common.security.api.JwtCode;
import saleson.common.security.api.JwtTokenService;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.EncryptionUtils;
import saleson.common.utils.JwtUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.UserEntity;
import saleson.seller.user.SellerUserService;
import saleson.shop.auth.AuthService;
import saleson.shop.auth.domain.GuestRequestAuth;
import saleson.shop.auth.domain.RequestAuth;
import saleson.shop.auth.domain.ResponseAuth;
import saleson.shop.auth.domain.SnsRequestAuth;
import saleson.shop.config.ConfigService;
import saleson.shop.coupon.CouponService;
import saleson.shop.coupon.domain.Coupon;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.eventcode.EventCodeService;
import saleson.shop.log.LoginLogService;
import saleson.shop.security.ShopSecurityService;
import saleson.shop.stats.StatsService;
import saleson.shop.user.GeneralCustomerService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.AuthUserInfo;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserInfoByCI;
import saleson.shop.user.support.AuthInfo;
import saleson.shop.usersns.UserSnsService;
import saleson.shop.usersns.domain.UserSns;


@RestController("ApiAuthController")
@RequestMapping("/api/auth")
public class AuthController {

    private Logger log = LoggerFactory.getLogger(AuthController.class);

    public static final String STATUS = "status";

    @Autowired
    private UserService userService;

    @Autowired
    private StatsService statsService;

    @Autowired
    private UserSnsService userSnsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    @Qualifier("smsTokenService")
    private TokenService smsTokenService;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private CouponService couponService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private AsisAuthService asisAuthService;

    @Autowired
    private EventCodeService eventCodeService;

    @Autowired
    private ConfigIsmsService configIsmsService;

    @Autowired
    private ShopSecurityService shopSecurityService;

    @Autowired
    private GeneralCustomerService generalCustomerService;

    @Autowired
    private SellerUserService sellerUserService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SciPhoneUtils sciPhoneUtils;

    @Value("${sci.phone.id}")
    private String sciPhoneId;

    @Value("${sci.phone.srvNo}")
    private String sciPhoneSrvNo;

    @Value("${sci.phone.key}")
    private String sciPhoneKey;

	@Value("${simepleAuth.jsonpath}")
	String simpleAuthJsonPath;


    String signTargetOri = ""; //전자 서명시 요청 서명 전문을 가지고 있기 위한 변수

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 회원 로그인 통계 API
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/login-statistics")
    public ResponseEntity loginStatistics(HttpServletRequest request, HttpServletResponse response) {
        ResponseEntity result = null;
        try {
            User user = getUserByToken(request);
            if (user != null
                    && user.getLoginId() != null
                    && !user.isShadowLogin()) {
                //shopSecurityService.updateLoginCount(user);
            }

            // 접속 통계 저장.
            statsService.saveVisitData(request, response);
            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 회원정보 수정 API (userName, loginId, phoneNumber, telNumber, post, newPost, address, addressDetail, birthDay, birthDayType, gender, receiveSms, receiveEmail,receivePbanc)
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/me")
    public ResponseEntity modify(@RequestBody @Valid UserInfo userInfo, BindingResult bindingResult) {
        if (userInfo == null) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        }

        ResponseEntity result = null;
        String checkResult = "";

        try {
            User user = new User();
            UserDetail userDetail = new UserDetail();

            String loginId = ObjectUtils.isEmpty(userInfo.getLoginId()) ? user.getLoginId() : userInfo.getLoginId();
            String email = ObjectUtils.isEmpty(userInfo.getEmail()) ? user.getEmail() : userInfo.getEmail();

            userModifyDataSet(userInfo, userDetail);

            user.setUserId(UserUtils.getUserId());
            user.setUserName(userInfo.getUserName());
            user.setEmail(email);
            user.setPassword(userInfo.getPassword());

            userDetail.processHyphen();
            user.setUserDetail(userDetail);

            if (bindingResult.hasErrors() || (userInfo.isSns() && ObjectUtils.isEmpty(user.getPassword()))) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            // SNS 계정 미인증일 경우 가입 불가 아이디 체크
            if (userInfo.isSns()) {
                user.setLoginId(loginId);
                checkResult = userService.checkDuplication(user);

                if ("isOccupiedId".equals(checkResult)) {
                    return ApiResponseEntity.error(ApiError.DUPLICATION_LOGIN_ID);
                }
            }

            userService.updateFrontUserAndUserDetail(user);

            if (userInfo.isSns()) {
                user.setPassword("");
                userSnsService.updateUser(user);
            }

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * SNS 연동 정보 확인
     *
     * @return
     */
    @GetMapping("/sns-info")
    public ResponseEntity getSnsInfo() {
        ResponseEntity result = null;
        Map<String, Object> info = new HashMap<>();

        boolean isSns = false;
        try {
            List<UserSns> list = userSnsService.getUserSnsList(new UserSns(UserUtils.getUserId()));
            int size = list.size();
            if (size > 0) {
                for (UserSns userSns : list) {
                    info.put(userSns.getSnsType(), userSns.getSnsUserId());
                    if (!ObjectUtils.isEmpty(userSns.getCertifiedDate())) {
                        size--;
                    }
                }
            }

            if (size > 0) {
                isSns = true;
            }

            result = ApiResponseEntity.data().put("info", info).put("sns", isSns).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 회원 비밀번호 확인
     *
     * @return
     */
    @PostMapping("/check-password")
    public ResponseEntity checkPassword(@RequestBody UserInfo userInfo, HttpServletRequest request) {
        ResponseEntity result = null;

        try {
            User user = getUserByToken(request);

            if (!userInfo.isSns() && !passwordEncoder.matches(userInfo.getPassword(), user.getPassword())) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_FAIL_PASSWD);
            }

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 회원정보 조회 API
     *
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity modify(HttpServletRequest request) {
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

            info.put("userId", user.getUserId());
            info.put("loginId", user.getLoginId());
            info.put("userName", user.getUserName());
            info.put("email", user.getEmail());
            info.put("frontPhoneNumber", userDetail.getFrontPhoneNumber());
            info.put("backPhoneNumber", userDetail.getBackPhoneNumber());
            info.put("phoneNumber", userDetail.getPhoneNumber());
            info.put("telNumber", userDetail.getTelNumber());
            info.put("post", userDetail.getPost());
            info.put("newPost", userDetail.getNewPost());
            info.put("address", userDetail.getAddress());
            info.put("addressDetail", userDetail.getAddressDetail());

            String addressInfo = "";
            if (!ObjectUtils.isEmpty(userDetail.getNewPost())) {
                addressInfo += "[" + userDetail.getNewPost() + "] ";
            }

            if (!ObjectUtils.isEmpty(userDetail.getAddress())) {
                addressInfo += userDetail.getAddress();
            }

            info.put("addressInfo", addressInfo);
            info.put("birthdayYear", userDetail.getBirthdayYear());
            info.put("birthdayMonth", userDetail.getBirthdayMonth());
            info.put("birthdayDay", userDetail.getBirthdayDay());
            info.put("birthdayType", ObjectUtils.isEmpty(userDetail.getBirthdayType()) ? "1" : userDetail.getBirthdayType());
            info.put("gender", ObjectUtils.isEmpty(userDetail.getGender()) ? "F" : userDetail.getGender());
            info.put("receiveSms", userDetail.getReceiveSms());
            info.put("receiveEmail", userDetail.getReceiveEmail());

            List<CodeInfo> phoneCodes = CodeUtils.getCodeInfoList("PHONE");
            info.put("phoneCodes", phoneCodes);

            result = ApiResponseEntity.data().put("info", info).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
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
            if (!userInfo.isSns() && (ObjectUtils.isEmpty(userInfo.getLoginId()) || ObjectUtils.isEmpty(userInfo.getPassword()))) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            User user = getUserByToken(request);
            UserDetail userDetail = (UserDetail) user.getUserDetail();

            if (!userInfo.isSns()
                    && (!userInfo.getLoginId().equals(user.getLoginId()) || !passwordEncoder.matches(userInfo.getPassword(), user.getPassword()))) {
                return ApiResponseEntity.error(ApiError.NOT_VALID_LOGIN);
            }

            user.setLeaveDate(DateUtils.getToday());
            user.setStatusCode("3");

            userDetail.setUserId(user.getUserId());
            userDetail.setLeaveReason(userInfo.getLeaveReason());
            userDetail.setUseFlag("N");
            user.setUserDetail(userDetail);
            userService.updateSecedeFrontUserAndUserDetail(user);

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @PostMapping("/send-auth-number")
    public ResponseEntity sendAuthNumber(@RequestBody SendAuthNumber sendAuthNumber) {

        ResponseEntity result = null;
        try {

            String loginId = sendAuthNumber.getLoginId();
            String userName = sendAuthNumber.getUserName();
            String phoneNumber = sendAuthNumber.getPhoneNumber();

            String requestToken = "";

            if (ObjectUtils.isEmpty(phoneNumber)) {
                throw new BusinessException("휴대폰번호를 입력해주세요.");
            }

            if (sendAuthNumber.isDuplicateCheck()) {
                if (ObjectUtils.isEmpty(userName)) {
                    throw new BusinessException("이름을 입력해주세요.");
                }

                AuthUserInfo authUserInfo = new AuthUserInfo();
                authUserInfo.setPhoneNumber(phoneNumber);

                User user = userService.getUserInfoByUserName(authUserInfo);

                if (user != null) {
                    return ApiResponseEntity.error(ApiError.DUPLICATION_SMS_INFO);
                }
            }

            if (ObjectUtils.isEmpty(loginId)) {
                requestToken = authService.getSmsAuthNumber(phoneNumber);
            } else {
                requestToken = authService.getSmsAuthNumber(loginId, phoneNumber);
            }

            result = ApiResponseEntity.data().put("requestToken", requestToken).put(STATUS, HttpStatus.OK).ok();

        } catch (BusinessException be) {
            //result = ApiResponseEntity.error(ApiError.BAD_REQUEST, be.getMessage());
            result = ApiResponseEntity.error(ApiError.BAD_REQUEST, "ERROR-06: 잘못된 API 요청 오류");
        } catch (Exception e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @PostMapping("/find-id")
    public ResponseEntity findId(@RequestBody FindUser findUser) {

        ResponseEntity result = null;

        try {

            Token token = new Token();
            token.setAccessToken(findUser.getAuthNumber());
            token.setRequestToken(findUser.getRequestToken());

            if (!smsTokenService.isValidToken(token)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            String phoneNumber = findUser.getPhoneNumber();

            if (ObjectUtils.isEmpty(phoneNumber)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            phoneNumber = phoneNumber.replaceAll("-", "");

            AuthUserInfo authUserInfo = new AuthUserInfo();
            authUserInfo.setUserName(findUser.getUserName());
            authUserInfo.setPhoneNumber(com.onlinepowers.framework.util.StringUtils.phoneNumberPattern(phoneNumber));

            User user = userService.getUserInfoByUserName(authUserInfo);

            if (user == null) {
                return ApiResponseEntity.error(ApiError.NOT_VALID_USER);
            }

            result = ApiResponseEntity.data().put("loginId", user.getLoginId()).put(STATUS, HttpStatus.OK).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @PostMapping("/find-password-step1")
    public ResponseEntity findPasswordStep1(@RequestBody FindUser findUser) {

        ResponseEntity result = null;

        try {

            long minute = 5L;

            if (ServiceType.LOCAL) {
                minute = 1440L;
            }

            String requestToken = findUser.getRequestToken();

            Token token = new Token();
            token.setAccessToken(findUser.getAuthNumber());
            token.setRequestToken(findUser.getRequestToken());

            if (!smsTokenService.isValidToken(token)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            String phoneNumber = findUser.getPhoneNumber();

            if (ObjectUtils.isEmpty(phoneNumber)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            phoneNumber = phoneNumber.replaceAll("-", "");

            AuthUserInfo authUserInfo = new AuthUserInfo();
            authUserInfo.setUserName(findUser.getUserName());
            authUserInfo.setLoginId(findUser.getLoginId());
            authUserInfo.setPhoneNumber(com.onlinepowers.framework.util.StringUtils.phoneNumberPattern(phoneNumber));

            User user = userService.getUserInfoByUserName(authUserInfo);

            if (user == null) {
                return ApiResponseEntity.error(ApiError.NOT_VALID_USER);
            }

            Map<String, Object> map = new LinkedHashMap<>();

            map.put("requestToken", requestToken);
            map.put("authToken", tokenService.getJwtRequestToken(user.getUserId(), requestToken, minute));

            result = ApiResponseEntity.data().put("token", map).put(STATUS, HttpStatus.OK).ok();

        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @PostMapping("/find-password-step2")
    public ResponseEntity findPasswordStep2(@RequestBody ChangePassword changePassword) {

        ResponseEntity result = null;

        try {

            String authToken = changePassword.getAuthToken();
            String requestToken = changePassword.getRequestToken();

            if (ObjectUtils.isEmpty(authToken)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            Jws<Claims> claims = tokenService.getClaimsByToken(authToken);

            if (!tokenService.isRequestTokenAuthentication(authToken, requestToken, claims)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            String password = changePassword.getPassword();
            String corfirmPassword = changePassword.getCorfirmPassword();

            if (ObjectUtils.isEmpty(password)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            if (!password.equals(corfirmPassword)) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_FAIL_PASSWD);
            }

            try {
                String tempUserId = String.valueOf(JwtUtils.getValueByToken(authToken, JwtUtils.getCode(JwtCode.JWT_CLAIM_ID)));

                long userId = com.onlinepowers.framework.util.StringUtils.string2long(tempUserId);

                if (userId <= 0) {
                    return ApiResponseEntity.error(ApiError.NOT_VALID_USER);
                }

                User user = new User();

                user.setUserId(userId);
                user.setPassword(password);

                userService.updateUser(user);

            } catch (UserException e) {
            	log.error("UserException {}", e.getStackTrace()[0]);
                //return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getMessage());
                return ApiResponseEntity.error(ApiError.BAD_REQUEST, "ERROR-05: 회원정보 수정 실패");
            } catch (Exception e) {
                return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            }


            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();

        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
            if (tokenService.isJwtException(e)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @PostMapping("/change-password")
    public ResponseEntity changePassword(@RequestBody ChangePassword changePassword, HttpServletRequest request) {
        ResponseEntity result = null;

        try {

            String password = changePassword.getPassword();
            String corfirmPassword = changePassword.getCorfirmPassword();
            String originalPassword = changePassword.getOriginalPassword();

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

            userService.updatePasswordForUser(user.getUserId(), originalPassword, password);

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();

        } catch (InvalidAuthenticationException e) {
//            log.error("비밀번호 변경 에러 {}", e.getMessage(), e);
            log.error("비밀번호 변경 에러");
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);

        } catch (UserException e) {
            log.error("비밀번호 변경 에러");
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, "updatePasswordForUser UserException");
        } catch (Exception e) {
            //log.error("비밀번호 변경 에러 {}", e.getMessage(), e);
        	log.error("ERROR-04: 비밀번호 변경 실패");

            if (tokenService.isJwtException(e)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @PostMapping("/delay-change-password")
    public ResponseEntity delayChangePassword(HttpServletRequest request) {
        ResponseEntity result = null;

        try {

            User user = getUserByToken(request);

            if (user == null) {
                throw new InvalidAuthenticationException("사용자 없음");
            }

            userService.updatePasswordExpiredDateForUser(user.getUserId());

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();

        } catch (InvalidAuthenticationException e) {
            log.error("비밀번호 변경 에러");
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);

        } catch (UserException e) {
            log.error("비밀번호 변경 에러");
            return ApiResponseEntity.error(ApiError.BAD_REQUEST);

        } catch (Exception e) {
            //log.error("비밀번호 변경 에러 {}", e.getMessage(), e);
            log.error("ERROR-04: 비밀번호 변경 실패");

            if (tokenService.isJwtException(e)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    private User getUserByToken(HttpServletRequest request) throws RuntimeException, SQLException {
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

    private void userModifyDataSet(UserInfo userInfo, UserDetail userDetail) {
        userDetail.setUserId(UserUtils.getUserId());
        userDetail.setTelNumber(userInfo.getTelNumber());

        if (!ObjectUtils.isEmpty(userInfo.getPhoneNumber())) {
            userDetail.setPhoneNumber(ShopUtils.phoneNumberPattern(userInfo.getPhoneNumber()));
        }

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

        if (!ObjectUtils.isEmpty(userInfo.getBirthdayYear())
                && !ObjectUtils.isEmpty(userInfo.getBirthdayMonth())
                && !ObjectUtils.isEmpty(userInfo.getBirthdayDay())) {
            userDetail.setBirthdayYear(userInfo.getBirthdayYear());
            userDetail.setBirthdayMonth(userInfo.getBirthdayMonth());
            userDetail.setBirthdayDay(userInfo.getBirthdayDay());
        }

        if (!ObjectUtils.isEmpty(userInfo.getBirthdayType())) {
            userDetail.setBirthdayType(userInfo.getBirthdayType());
        }

        if (!ObjectUtils.isEmpty(userInfo.getGender())) {
            userDetail.setGender(userInfo.getGender());
        }



        if (!ObjectUtils.isEmpty(userInfo.getReceiveEmail())) {
            userDetail.setReceiveEmail(userInfo.getReceiveEmail());
        }
        if (!ObjectUtils.isEmpty(userInfo.getReceiveSms())) {
            userDetail.setReceiveSms(userInfo.getReceiveSms());
        }

        if (!ObjectUtils.isEmpty(userInfo.getReceivePush())) {
            userDetail.setReceivePush(userInfo.getReceivePush());
        }
        if (!ObjectUtils.isEmpty(userInfo.getReceivePbanc())) {
            userDetail.setReceivePbanc(userInfo.getReceivePbanc());
        }
        if (!ObjectUtils.isEmpty(userInfo.getMarketing())) {
            userDetail.setMarketing(userInfo.getMarketing().equals("true") ? "1" : "0");
        }

        if (!ObjectUtils.isEmpty(userInfo.getPrivacy())) {
            userDetail.setPrivacy(userInfo.getPrivacy().equals("true") ? "1" : "0");
        }

        if (!ObjectUtils.isEmpty(userInfo.getTerms())) {
            userDetail.setTerms(userInfo.getTerms().equals("true") ? "1" : "0");
        }
    }

    /**
     * 휴면 계정 복구
     *
     * @return
     */
    @PostMapping("/recovery")
    public ResponseEntity recovery(HttpServletRequest request) {
        ResponseEntity result = null;

        try {
            User user = getUserByToken(request);

            if (user == null || !"4".equals(user.getStatusCode())) {
                throw new UserException("휴면 회원이 아닙니다.");
            }

            userService.wakeupUser(user);
            SecurityContextHolder.getContext().setAuthentication(null);

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (UserException e) {
            log.error("[/api/auth/recovery] ERROR : {}", e.getStackTrace()[0]);
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, "wakeupUser UserException");
        } catch (Exception e) {
        	log.error("[/api/auth/recovery] ERROR : {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 회원가입 API
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid UserInfo userInfo, BindingResult bindingResult) {
        if (userInfo == null) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        }

        User user = new User();
        ResponseEntity result = null;
        UserDetail userDetail = new UserDetail();

        try {
            if (bindingResult.hasErrors() || ObjectUtils.isEmpty(userInfo.getPassword())) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            userModifyDataSet(userInfo, userDetail);

            user.setLoginId(userInfo.getLoginId());
            user.setPassword(userInfo.getPassword());
            user.setUserName(userInfo.getUserName());
            user.setEmail(userInfo.getEmail());
            user.setUserDetail(userDetail);

            // 인증여부 체크
            if (!userInfo.isAuth()) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_SMS);
            }

            // 가입 불가 아이디 체크
            String checkResult = userService.checkDuplication(user);
            if ("isOccupiedId".equals(checkResult)) {
                return ApiResponseEntity.error(ApiError.DUPLICATION_LOGIN_ID);
            }

//            long userId = sequenceService.getLong("OP_USER");
            long userId = userService.selectNewUserId();
            user.setUserId(userId);
            userDetail.setUserId(userId);

            userService.insertUserAndUserDetail(user, userDetail);

            //신규회원가입 쿠폰 발급[2017-09-08]minae.yun
            UserCouponParam userCouponParam = new UserCouponParam();
            if (userDetail.getLevelId() != 0) {
                userCouponParam.setUserLevelId(userDetail.getLevelId());
            }

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
            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).put("userId", userId).ok();

        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 인증번호 체크 API
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/check-auth-number")
    public ResponseEntity checkAuthNumber(@RequestBody UserInfo userInfo) {
        ResponseEntity result = null;

        try {
            Token token = new Token();
            token.setAccessToken(userInfo.getAuthNumber());
            token.setRequestToken(userInfo.getRequestToken());

            // 인증번호 체크
            if (!smsTokenService.isValidToken(token)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED_TOKEN);
            }

            result = ApiResponseEntity.data().put(STATUS, HttpStatus.OK).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     *  SNS 계정 연동
     *  status { 00: 정상, 01: 해당 이메일로 아이디 존재 }
     * @param userSnsData
     * @return
     */
    @PostMapping("/sns-join")
    public ResponseEntity snsJoin(@RequestBody UserSns userSnsData) {
        ResponseEntity result = null;

        UserSns userSns = new UserSns();
        Map<String, String> map = new HashMap<>();

        try {
            map = userSnsService.joinProcess(userSns, map, userSnsData);
            UserCouponParam userCouponParam = new UserCouponParam();

            if ("00".equals(map.get("value"))) {
                //신규회원가입 쿠폰 발급[2017-09-08]minae.yun
                userCouponParam.setCouponTargetTimeType("2");
                List<Coupon> newUserCouponList = couponService.getCouponByTargetTimeType(userCouponParam);

                if (newUserCouponList != null && newUserCouponList.size() != 0) {
                    for (Coupon coupon : newUserCouponList) {
                        userCouponParam.setCouponId(coupon.getCouponId());
                        userCouponParam.setUserId(Long.parseLong(map.get("userId")));
                        int count = couponService.getUserCouponListForNewUserCoupon(userCouponParam);
                        if (count == 0) {
                            couponService.insertCouponTargetUserOne(userCouponParam);
                            couponService.userCouponDownload(userCouponParam);
                        }
                    }
                }
            }

            map.put("status", "00");

            result = ApiResponseEntity.data().put("info", map).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     *  SNS 계정 연동 해제
     * status { 00: 정상, 01: error }
     * @param userSnsData
     * @return
     */
    @PostMapping("/disconnect-sns")
    public ResponseEntity disconnectSns(@RequestBody UserSns userSnsData) {
        ResponseEntity result = null;

        UserSns userSnsParam = new UserSns();
        Map<String, String> map = new HashMap<>();

        try {
            userSnsParam.setUserId(UserUtils.getUserId());

            UserSns userSns = userSnsService.getUserSnsInfo(userSnsData);
            List<UserSns> userSnsList = userSnsService.getUserSnsList(userSnsParam);
            // 연결 된 SNS 계정이 2개 이상이고 CertifiedDate 값이 하나라도 존재한다면 현재 들어온 SNS는 연결해제 진행.
            if (userSnsList != null && userSnsList.size() >= 2) {
                for (int i = 0; i < userSnsList.size(); i++) {
                    if (!ObjectUtils.isEmpty(userSnsList.get(i).getCertifiedDate())) {
                        userSns.setCertifiedDate(userSnsList.get(i).getCertifiedDate());
                    }
                }
            }

            if (userSns != null) {
                // 아이디를 등록한 상태이거나, SNS 계정이 2개 이상 연결되어 있으면 해당 SNS 계정은 연결해제 가능
                if (!ObjectUtils.isEmpty(userSns.getCertifiedDate()) || (userSnsList != null && userSnsList.size() >= 2)) {
                    userSnsService.disconnectSns(userSnsData);
                    map.put("status", "00");
                    map.put("message", "정상적으로 연결이 해제되었습니다.");
                    map.put("value", "00");
                } else {
                    map.put("status", "00");
                    map.put("message", "해제할 수 없는 상태입니다. SNS를 추가 등록 하시거나 회원정보 수정에서 아이디를 등록 해주세요.");
                    map.put("value", "01");
                }
            } else {
                map.put("status", "00");
                map.put("message", "해제할 수 없는 상태입니다. SNS를 추가 등록 하시거나 회원정보 수정에서 아이디를 등록 해주세요.");
                map.put("value", "01");
            }

            result = ApiResponseEntity.data().put("info", map).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * SNS 가입 정보 확인
     * @param userSnsParam
     * @return
     */
    @PostMapping("/check-sns-join")
    public ResponseEntity snsJoinedCheck(@RequestBody UserSns userSnsParam) {
        ResponseEntity result = null;
        Map<String, String> map = new HashMap<>();

        try {
            UserSns userSns = userSnsService.getUserSnsInfo(userSnsParam);
            map.put("status", "00");
            map.put("message", "조회되었습니다.");
            map.put("value", userSns == null ? "0" : "1");

            result = ApiResponseEntity.data().put("info", map).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @GetMapping("/saleson-id")
    public ResponseEntity salesonId() {
        String id = UUID.randomUUID().toString();
        return ApiResponseEntity.data().put("id", id).ok();
    }

    @PostMapping("/token")
    public ResponseEntity getUserToken(HttpServletRequest request,
                                                     @RequestBody RequestAuth requestAuth,
                                       @RequestParam(name="uid", defaultValue = "") String uid) {
        HttpStatus httpStatus = HttpStatus.OK;
        ResponseEntity result = null;
        String loginType = "";
        String loginId = "";
        String password = "";
        Map<String, Object> map = new HashMap<>();
        try {
        	checkedHmac(request, JsonViewUtils.objectToJson(requestAuth));

        	loginType = requestAuth.getLoginType();
            loginId = requestAuth.getLoginId().trim();

        	if(requestAuth.getFinanc() == "") {
        		password = pCrypto.Encrypt("hash.5", requestAuth.getPassword(), "");
        	}else {
        		password = userService.getCryptoDecByLoginId(loginId);	// DB 에서 password 를 decodeing 해 온다.
        	}

            if (ObjectUtils.isEmpty(loginType)) {
                throw new InvalidAuthenticationException();
            }

            if ("ROLE_OPMANAGER".equals(loginType)) {
                loginId = loginId + OpKeyHolder.OPMANAGER_LOGIN_KEY;
            } else if ("ROLE_SELLER".equals(loginType)) {
                loginId = loginId + OpKeyHolder.SELLER_LOGIN_KEY;
            }

            User userByLoginId = userService.getUserByLoginId(loginId);

            // 비밀번호 5회 이상 실패한 계정의 경우 계정 잠금 안내 문구 노출 - passwordType N = 일반, T = 임시, P = 카카오
			if (!"P".equals(userByLoginId.getPasswordType()) && userByLoginId.getLoginFailCount() >= 5) {
				throw new LockedException("INCORRECT_PASSWORD_LOCK");
			}

			// 오프라인 담당자 계정은 사용자 페이지에 접근할 수 없음
			String authority = null;
			for(UserRole userRoles : userByLoginId.getUserRoles()) {
				authority = userRoles.getAuthority();

				if(authority.equals("ROLE_ADMIN_7") || authority.equals("ROLE_ADMIN_8")) {
					result = ApiResponseEntity.error(ApiError.OFF_ACCESS_FRONT);
					return result;
				}
			}

            // ASIS 인증 API 통신
            IdPasswordAuthenticationToken authRequest = new IdPasswordAuthenticationToken(loginId, password, loginType, null);

            Authentication authentication = authenticationManager.authenticate(authRequest);

            UserDetails userDetails = (OpUserDetails) authentication.getPrincipal();
            User user = ((OpUserDetails) userDetails).getUser();

            String code = "";

            String userKey = userService.getUserKeyInfo(user.getUserId());
			String loginPath = userService.getUserLoginPathInfo(user.getUserId());

            if("ROLE_USER".equals(loginType)) {

                int passwordExpiredDateDiff = DateUtils.getDaysDiff(DateUtils.getToday(Const.DATE_FORMAT), user.getPasswordExpiredDate());

                if (!"P".equals(user.getPasswordType()) && passwordExpiredDateDiff <= 0) {
                    code = "PASSWORD_EXPIRED";
                }
                if ("T".equals(user.getPasswordType())) {
                    code = "PASSWORD_TEMP";
                }
                if ("4".equals(user.getStatusCode())) {
                    code = "SLEEP_USER";
                }
                if(!ObjectUtils.isEmpty(userKey) && "300".equals(loginPath)) { //원패스 회원인 경우
                	code = "ONEPASS_USER";
                }
            }

            if ("ROLE_OPMANAGER".equals(loginType)) {
                loginId = loginId.replaceAll(OpKeyHolder.OPMANAGER_LOGIN_KEY, "");
            } else if ("ROLE_SELLER".equals(loginType)) {
                loginId = loginId.replaceAll(OpKeyHolder.SELLER_LOGIN_KEY, "");
            }

            String token = jwtTokenService.getJwtToken(
                    loginType,
                    loginId,
                    user.getPassword(),
                    JwtUtils.getClientIpAddress(request)
            );

            map.put("token", token);
            map.put("code", code);

            result = ApiResponseEntity.data().map(map).ok();

            //중복로그인 추가 (2022.11.01)
            long userId = user.getUserId();
            if ("ROLE_USER".equals(loginType)) {
                saveLoginSessionForUser(userId, token);
            } else if ("ROLE_OPMANAGER".equals(loginType)) {
                saveLoginSessionForManager(userId, token);
            } else if ("ROLE_SELLER".equals(loginType)) {
                saveLoginSessionForSeller(userId, token);
            }

            updateLoginCount(loginType, user);

        } catch (LockedException e) {
            log.error("ERROR-07: 회원 계정 잠김 오류 {}", e.getStackTrace()[0]);
            loginLogService.insertLoginLogByUser(request, loginId, false);
        	if(e.getMessage().equals("INCORRECT_PASSWORD_LOCK")){
				result = ApiResponseEntity.error(ApiError.INCORRECT_PASSWORD_LOCK);
        	}else {
	            result = ApiResponseEntity.error(ApiError.UNAUTHORIZED_LOCK, getUserLockMessage());
        	}
        } catch (Exception e) {
            log.error("ERROR: token api {}", e.getStackTrace()[0]);
            loginLogService.insertLoginLogByUser(request, loginId, false);
            result = ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            updateLoginFailCountForUser(loginType, loginId);
        }
        return result;
    }

    @PostMapping("/sns-token")
    public ResponseEntity<ResponseAuth> getSnsUserToken(HttpServletRequest request,
                                                        @RequestBody SnsRequestAuth requestAuth,
                                                        @RequestParam(name="uid", defaultValue = "") String uid) {
        HttpStatus httpStatus = HttpStatus.OK;
        ResponseAuth responseAuth;

        String loginType = "";
        String snsLoginId = "";
        try {

            checkedHmac(request, JsonViewUtils.objectToJson(requestAuth));

            UserSns userSnsData = new UserSns();
            userSnsData.setSnsType(requestAuth.getSnsType());
            userSnsData.setSnsId(requestAuth.getSnsId());
            userSnsData.setEmail(requestAuth.getEmail());
            userSnsData.setSnsName(requestAuth.getSnsName());

            Map<String, String> map = new HashMap<>();
            map = userSnsService.joinProcess(new UserSns(), map, userSnsData);

            UserCouponParam userCouponParam = new UserCouponParam();

            if ("03".equals(map.get("value"))) {
                //신규회원가입 쿠폰 발급[2017-09-08]minae.yun
                userCouponParam.setCouponTargetTimeType("2");
                List<Coupon> newUserCouponList = couponService.getCouponByTargetTimeType(userCouponParam);

                if (newUserCouponList != null && newUserCouponList.size() != 0) {
                    for (Coupon coupon : newUserCouponList) {
                        userCouponParam.setCouponId(coupon.getCouponId());
                        userCouponParam.setUserId(Long.valueOf(map.get("userId")));
                        couponService.insertCouponTargetUserOne(userCouponParam);
                        couponService.userCouponDownload(userCouponParam);
                    }
                }
            }

            if (userSnsData.getIsMypage()) {
                throw new RuntimeException("마이페이지 호출이면 발급이 불가합니다.");
            }

            snsLoginId = map.get("loginId");

            if (ObjectUtils.isEmpty(snsLoginId)) {
                throw new RuntimeException("로그인 ID가 존재하지 않습니다.");
            }

            loginType = "ROLE_USER";
            String loginId = ShadowUtils.getShadowLoginKey(snsLoginId, "");
            String password = ShadowUtils.getShadowLoginPassword(snsLoginId);
            String signature = ShadowUtils.getShadowLoginSignature(snsLoginId);

            IdPasswordAuthenticationToken authRequest = new IdPasswordAuthenticationToken(loginId, password, loginType, signature);
            Authentication authentication = authenticationManager.authenticate(authRequest);

            UserDetails userDetails = (OpUserDetails) authentication.getPrincipal();
            User user = ((OpUserDetails) userDetails).getUser();

            String token = jwtTokenService.getJwtToken(
                    loginType,
                    snsLoginId,
                    user.getPassword(),
                    JwtUtils.getClientIpAddress(request)
            );

            responseAuth = new ResponseAuth(true, "발행되었습니다.", token);

            // 이벤트 코드 로그 userId 업데이트
            if (!ObjectUtils.isEmpty(uid)) {
                eventCodeService.updateLogForUserId(uid, user.getUserId());
            }

            //updateLoginCount(loginType, user);
            //중복로그인 추가(2022.11.01)
            if ("ROLE_USER".equals(loginType)) {
                saveLoginSessionForUser(user.getUserId(), token);
            }
        } catch (LockedException e) {
            log.error("ERROR: sns-token");
            httpStatus = HttpStatus.UNAUTHORIZED;
            responseAuth = new ResponseAuth(false, getUserLockMessage(), "");
        } catch (SecureServletException e) {

        	log.error("ERROR: sns-token");
            httpStatus = HttpStatus.BAD_REQUEST;
            responseAuth = new ResponseAuth(false, "Https만 이용 가능합니다.", "");

            updateLoginFailCountForUser(loginType, snsLoginId);

        } catch (Exception e) {
            //log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR-19: sns-token 발행 실패");
            httpStatus = HttpStatus.UNAUTHORIZED;
            responseAuth = new ResponseAuth(false, "발행에 실패하였습니다.", "");

            updateLoginFailCountForUser(loginType, snsLoginId);
        }

        return new ResponseEntity<>(responseAuth, httpStatus);
    }

  //중복로그인 추가(2022.11.01)
    /**
     * 회원 로그인 세션 정보 등록
     * @param userId
     * @param token
     */
    private void saveLoginSessionForUser(long userId, String token) {
        try {
            String sign = (String)JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_SIGN));
            userService.saveLoginSessionForUser(sign, userId);
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        } catch (Exception e) {
            //log.error("saveLoginSessionForUser ERROR: {}", e.getMessage(), e);
        	log.error("ERROR-14: 사용자 세션정보 저장실패 {}", e.getStackTrace()[0]);
        }
    }

    private void saveLoginSessionForManager(long userId, String token) {
        try {
            String sign = (String)JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_SIGN));
            userService.saveLoginSessionForManager(sign, userId);
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
        }
    }

    private void saveLoginSessionForSeller(long userId, String token) {
        try {
            String sign = (String)JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_SIGN));
            sellerUserService.saveLoginSession(sign, userId);
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
        }
    }

    @PostMapping("/guest-token")
    public ResponseEntity getGuestToken(HttpServletRequest request,
                                        @RequestBody GuestRequestAuth requestAuth) {
        HttpStatus httpStatus = HttpStatus.OK;
        ResponseAuth responseAuth;

        try {

            checkedHmac(request, JsonViewUtils.objectToJson(requestAuth));

            Token smsToken = new Token();

            smsToken.setRequestToken(requestAuth.getRequestToken());
            smsToken.setAccessToken(requestAuth.getAuthNumber());

            if (!smsTokenService.isValidToken(smsToken)) {

                httpStatus = HttpStatus.UNAUTHORIZED;
                responseAuth = new ResponseAuth(false, "발행에 실패하였습니다.", "");

            } else {

                String token = jwtTokenService.getJwtGuestToken(requestAuth.getUserName(), requestAuth.getPhoneNumber());
                responseAuth = new ResponseAuth(true, "발행되었습니다.", token);
            }

        } catch (SecureServletException e) {

        	log.error("ERROR-20: guest-token - Https만 이용 가능합니다.");
            httpStatus = HttpStatus.BAD_REQUEST;
            responseAuth = new ResponseAuth(false, "Https만 이용 가능합니다.", "");

        } catch (Exception e) {
            //log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR-20: guest-token 발행 실패");
            httpStatus = HttpStatus.UNAUTHORIZED;
            responseAuth = new ResponseAuth(false, "발행에 실패하였습니다.", "");
        }

        return new ResponseEntity<>(responseAuth, httpStatus);
    }

    private void updateLoginCount(String loginType, User user) {
        if("ROLE_USER".equals(loginType)) {
            //shopSecurityService.updateLoginCount(user);
            shopSecurityService.updateClearLoginFailCountForUser(user.getLoginId());
        }
    }

    private void updateLoginFailCountForUser(String loginType, String loginId) {
        if("ROLE_USER".equals(loginType) && !ObjectUtils.isEmpty(loginId)) {
            shopSecurityService.updateLoginFailCountForUser(loginId);
        }
    }

    private String getUserLockMessage() {
        String lockTime = configIsmsService.getIsmsConfigValueByKey("LOCK_TIME_PASSWORD");

        return lockTime + "분후에 다시 로그인해 주세요.";
    }

    /**
     * Hamc 체크
     *
     * @param request
     * @param jsonParam
     * @throws ProcessApiException
     */
    private void checkedHmac(HttpServletRequest request, String jsonParam) throws ProcessApiException {

        String hmac = request.getHeader("Hmac");

        if (ObjectUtils.isEmpty(hmac)) {
            throw new ProcessApiException("Hmac이 header에 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        // 웹취약점 대응 JwtUtils.getCode(JwtCode.REFRESH_TOKEN_SECURE_KEY) -> SalesonProperty.getSalesonUrlApi()
        if (!EncryptionUtils.isMatchesHmacSha256Hex(hmac, SalesonProperty.getSalesonUrlApi(), jsonParam)) {
            throw new ProcessApiException("인증에 실패 했습니다.", HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/auth-me")
    public ResponseEntity getAuthMe(HttpServletRequest request) throws Exception{

        String token = JwtUtils.getToken(request);

        if (ObjectUtils.isEmpty(token)) {
            throw new InvalidAuthenticationException("토큰이 존재하지 않음");
        }

        Jws<Claims> claims = tokenService.getClaimsByToken(token);
        // 자체 시스템 인증 시작
        if (!tokenService.isTokenAuthentication(token, claims)) {
            throw new InvalidAuthenticationException("토큰 자체인증 실패");
        }

        String loginType = (String) JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_LOGIN_TYPE));

        Map<String, Object> map = new LinkedHashMap<>();

        map.put("type", loginType);

        return ResponseEntity.ok(map);
    }

    @PostMapping("/asis-token")
    public ResponseEntity getAsisUserToken() {

        try {

            String token = asisAuthService.getAsisToken();

            if (ObjectUtils.isEmpty(token)) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            }

            return ApiResponseEntity.data().put("token", token).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
        }
    }

    /**
     * 드루 유저 마이그레이션 여부
     *
     * @return
     */
    @GetMapping("/druh-mig-check")
    public ResponseEntity getDruhMigCheck(HttpServletRequest request) {
        ResponseEntity result = null;
        try {
            User user = getUserByToken(request);
            boolean druhMig = false;

            // 가입일이 배포일(20.06.19 이전 이라면 기존 드루 Mig 대상
            if(Integer.parseInt(user.getCreatedDate().substring(0,8)) < 20200619){
                druhMig = true;
            }
            result = ApiResponseEntity.data().put("druhMig", druhMig).ok();
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 디지털원패스 로그인
     *
     * @param request
     * @return
     */
    @GetMapping("/onepass-login")
    public ResponseEntity getOnePassLogin(HttpServletRequest request) {
        ResponseEntity result = null;

        String action = "";
    	String inputName = "";
    	String inputValue = "";
    	String serviceType = request.getParameter("serviceType");

        try {

        	if ("LOGIN".equals(serviceType)) {
        		// 2. 로그인처리
    			result = ApiResponseEntity
        				.data()
        				.put("LOGIN_DEST", OnepassRequestHandler.LOGIN_DEST)
        				.put("LOGIN_INPUT_NAME", OnepassRequestHandler.LOGIN_INPUT_NAME)
        				.put("LOGIN", OnepassRequestHandler.login())
        				.ok();
        	} else if ("LOGOUT".equals(serviceType)) {
    			result = ApiResponseEntity
        				.data()
        				.put("action", OnepassRequestHandler.logoutDest(request))
        				.put("inputName", OnepassRequestHandler.LOGOUT_INPUT_NAME)
        				.put("inputValue", OnepassRequestHandler.logout(request))
        				.put("pageType", OnepassRequestHandler.pageType(request))
        				.put("JSESSIONID", request.getSession().getId())
        				.ok();
        	} else {
        	}


        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @PostMapping("/onepass-callback")
    public RedirectView getOnePassCallback(HttpServletRequest request, HttpServletResponse response, RedirectAttributes re) {
        try {

        	String returnUrl = request.getParameter("returnUrl");

        	// 인증체크
        	OnepassResponse onepassResponse = OnepassResponseHandler.check(request);

        	// 인증 성공
        	if ("SUCCESS".equals(onepassResponse.getStatus().toString()) && "SUCCESS".equals(onepassResponse.getResultCode().toString())) {

        		// 로그인일 경우
        		if ("LOGIN".equals(onepassResponse.getType().toString())) {
                	// 4. UserKey 로 사용자 검색 (자체)
        			UserEntity userEntity = userService.getUserByUserKey(onepassResponse.getUserKey());

                	if (userEntity != null) {
                		response.sendRedirect(SalesonProperty.getSalesonUrlFrontend() + "/users/onepass-result.html?result=success&loginId=" + userEntity.getLoginId() + "&uk=" + onepassResponse.getUserKey() + "&iT=" + onepassResponse.getIntfToken());
                	} else {
                		// 6. UserKey 로 사용자정보 요청
                    	ApiSendHandler apiSendHandler = new ApiSendHandler();
                    	String userKey = onepassResponse.getUserKey();
                    	String intfToken = onepassResponse.getIntfToken();

                    	// 7. 사용자 조회
                    	OnepassUserResponse onepassUser = apiSendHandler.findUser(userKey, intfToken);

                    	// test
                    	// 사용자 조회 성공
                    	if (onepassUser != null && "USE".equals(onepassUser.getStatus().toString())) {
                        	// 9. CI 로 사용자 검색
                    		// 10. CI 값으로 UserKey 매핑
                    		UserEntity user = userService.getUserByMberCi(onepassUser.getCi(), onepassResponse.getUserKey());

                        	if (user != null) {
                        		// 디지털원패스 회원연동
                        		response.sendRedirect(SalesonProperty.getSalesonUrlFrontend() + "/users/onepass-result.html?result=success&loginId="+user.getLoginId() + "&uk=" + onepassResponse.getUserKey() + "&iT=" + onepassResponse.getIntfToken());
                        	} else {
                        		// 11. 신규회원연동(가입) - 휴대폰인증, 아이핀인증 화면으로 이동
//                        		cookie = new Cookie("onepass-id", onepassUser.getId());
////                        	    cookie.setMaxAge(60*30); //쿠키 유효 기간: 하루로 설정(60초 * 60분 * 24시간)
//                        	    cookie.setPath("/"); //모든 경로에서 접근 가능하도록 설정
//                        	    response.addCookie(cookie); //response에 Cookie 추가
//
//                        	    cookie = new Cookie("onepass-email", onepassUser.getEmail());
////                        	    cookie.setMaxAge(60*30); //쿠키 유효 기간: 하루로 설정(60초 * 60분 * 24시간)
//                        	    cookie.setPath("/"); //모든 경로에서 접근 가능하도록 설정
//                        	    response.addCookie(cookie); //response에 Cookie 추가
                        		response.sendRedirect(SalesonProperty.getSalesonUrlFrontend() + "/users/onepass-result.html?result=new" + "&uk=" + onepassResponse.getUserKey() + "&iT=" + onepassResponse.getIntfToken() + "&oi=" + onepassUser.getId() + "&oe=" + onepassUser.getEmail() + "&ci=" + URLEncoder.encode(CommonUtils.dataNvl(onepassUser.getCi()), StandardCharsets.UTF_8));
                        	}
                    	} else {
                    		// 사용자 조회 실패
                    		response.sendRedirect(SalesonProperty.getSalesonUrlFrontend() + "/users/onepass-result.html?result=fail");
                    	}
                	}
        		} else {	// type 이 LOGIN 이 아님 (LOGOUT)
        			// 로그아웃처리
        			response.sendRedirect(SalesonProperty.getSalesonUrlFrontend() + "/users/onepass-result.html?result=logout");
        		}
        	} else {	// 인증실패
        		// 인증실패
        		response.sendRedirect(SalesonProperty.getSalesonUrlFrontend() + "/users/onepass-result.html?result=fail");
        	}
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        	RedirectView redirectView = new RedirectView();
            redirectView.setUrl(SalesonProperty.getSalesonUrlFrontend() + "/users/onepass-result.html?result=fail");
            return redirectView;
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(SalesonProperty.getSalesonUrlFrontend() + "/users/onepass-result.html?result=fail");
            return redirectView;
        }

        return null;
    }

    @PostMapping("/onepass-token")
    public ResponseEntity getOnePassToken(HttpServletRequest request,
                                                     @RequestBody RequestAuth requestAuth,
                                       @RequestParam(name="uid", defaultValue = "") String uid) {
    	HttpStatus httpStatus = HttpStatus.OK;
        ResponseEntity result = null;
        String loginType = "";
        String loginId = "";
        try {

//            checkedHmac(request, JsonViewUtils.objectToJson(requestAuth));

            loginType = "ROLE_USER"; 	// requestAuth.getLoginType();
            loginId = requestAuth.getLoginId().trim();

            String password = userService.getCryptoDecByLoginId(loginId);	// DB 에서 password 를 decodeing 해 온다.

            if (ObjectUtils.isEmpty(loginType)) {
                throw new InvalidAuthenticationException();
            }

            if ("ROLE_OPMANAGER".equals(loginType)) {
                loginId = loginId + OpKeyHolder.OPMANAGER_LOGIN_KEY;
            } else if ("ROLE_SELLER".equals(loginType)) {
                loginId = loginId + OpKeyHolder.SELLER_LOGIN_KEY;
            }

            // ASIS 인증 API 통신
            //asisAuthService.processAuthUser(loginId, password);

            IdPasswordAuthenticationToken authRequest = new IdPasswordAuthenticationToken(loginId, password, loginType, null);

//            Authentication authentication = authenticationManager.authenticate(authRequest);
//
//            UserDetails userDetails = (OpUserDetails) authentication.getPrincipal();
//            User user = ((OpUserDetails) userDetails).getUser();
//
//            String code = "";
//            if("ROLE_USER".equals(loginType)) {
//                if ("4".equals(user.getStatusCode())) {
//                    code = "SLEEP_USER";
//                }
//
//                int passwordExpiredDateDiff = DateUtils.getDaysDiff(DateUtils.getToday(Const.DATE_FORMAT), user.getPasswordExpiredDate());
//                if ("T".equals(user.getPasswordType()) || passwordExpiredDateDiff <= 0) {
//                    code = "PASSWORD_EXPIRED";
//                }
//
//                // 이벤트 코드 로그 userId 업데이트
//                if (!ObjectUtils.isEmpty(uid)) {
//                    eventCodeService.updateLogForUserId(uid, user.getUserId());
//                }
//
//            }
//
//            if ("ROLE_OPMANAGER".equals(loginType)) {
//                loginId = loginId.replaceAll(OpKeyHolder.OPMANAGER_LOGIN_KEY, "");
//            } else if ("ROLE_SELLER".equals(loginType)) {
//                loginId = loginId.replaceAll(OpKeyHolder.SELLER_LOGIN_KEY, "");
//            }

            String token = jwtTokenService.getJwtToken(
                    loginType,
                    loginId,
                    password,
                    JwtUtils.getClientIpAddress(request)
            );

            Map<String, Object> map = new HashMap<>();

            map.put("token", token);
            map.put("code", "SLEEP_USER");

            result = ApiResponseEntity.data().map(map).ok();

            User user = userService.getUserByLoginId(loginId);
            //updateLoginCount(loginType, user);
            loginLogService.insertLoginLogByUser(request, loginId, true);

            //중복로그인 추가 (2022.11.01)
            long userId = user.getUserId();
            if ("ROLE_USER".equals(loginType)) {
                saveLoginSessionForUser(userId, token);
            } else if ("ROLE_OPMANAGER".equals(loginType)) {
                saveLoginSessionForManager(userId, token);
            } else if ("ROLE_SELLER".equals(loginType)) {
                saveLoginSessionForSeller(userId, token);
            }

	    } catch (LockedException e) {
	    	//log.error("[/api/auth/onepass-token] ERROR : {}", e.getMessage(), e);
	    	log.error("ERROR-12: onepass-token 실패");
	        loginLogService.insertLoginLogByUser(request, loginId, false);
	        result = ApiResponseEntity.error(ApiError.UNAUTHORIZED_LOCK, getUserLockMessage());
	    } catch (Exception e) {
	    	log.error("[/api/auth/onepass-token] ERROR");
	        loginLogService.insertLoginLogByUser(request, loginId, false);
	        result = ApiResponseEntity.error(ApiError.UNAUTHORIZED);

	        updateLoginFailCountForUser(loginType, loginId);
	    }

        return result;

    }

    @PostMapping("/onepass-cancel")
    public ResponseEntity onepassCancel(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> paramMap ) {
    	ResponseEntity result = null;
    	Map<String, String> map = new HashMap<>();

    	try {
	    	ApiSendHandler apiSendHandler = new ApiSendHandler();

	    	String userKey = paramMap.get("userKey");
	    	String intfToken = paramMap.get("intfToken");

	    	// 탈퇴이유
	    	String leaveCode = paramMap.get("leaveCode");
	    	String leaveReason = paramMap.get("leaveReason");

	    	OnepassUserResponse onepassUser = apiSendHandler.InterLockRelease(userKey, intfToken);

	    	if (Objects.isNull(onepassUser)) {
	    		onepassUser = new OnepassUserResponse();

	    		map.put("status", "00");
                map.put("message", "디지털원패스 연동해지 할 수 없는 상태입니다");
                map.put("value", "01");

	    	} else {
	    		if (onepassUser.getProcess_result().toString() == "SUCESS") {
	    			// userkey 삭제 및 탈퇴처리
	    			userService.updateUserKeyStatusCode(UserUtils.getUserId(), leaveCode, leaveReason);

	    			map.put("status", "00");
		            map.put("message", "디지털원패스 연동해지가 완료되었습니다.");
		            map.put("value", "00");
	    		} else {
	    			map.put("status", "00");
                    map.put("message", "디지털원패스 연동해지 할 수 없는 상태입니다.");
                    map.put("value", "01");
	    		}
	    	}

	    	result = ApiResponseEntity.data().put("info", map).ok();
    	} catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
    	} catch (Exception e) {
    		log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
    	return result;
    }

    /**
     * 휴댜폰 본인인증
     *
     * @param request
     * @return
     */
//    @GetMapping("/mobile-auth")
//    public ResponseEntity getMobileAuth(HttpServletRequest request) {
//    	ResponseEntity result = null;
//
//    	try {
//
//    		//날짜 생성
//            Calendar today = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//            String day = sdf.format(today.getTime());
//
//    		String id       = sciPhoneId; // request.getParameter("id");                               // 본인실명확인 회원사 아이디
//    	    String srvNo    = sciPhoneSrvNo; // request.getParameter("srvNo");                            // 본인실명확인 서비스번호
//    	    String reqNum   = "123456789";	// request.getParameter("reqNum");                           // 본인실명확인 요청번호 (sample 페이지와 result 페이지가  동일하지 않으면 결과페이지 복호화 시 에러)
//    		String exVar    = "0000000000000000";
//    	    String retUrl   = "32"+request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/api/auth/mobile-auth-callback"; // request.getParameter("retUrl");  // 본인실명확인 결과수신 URL                         // 본인실명확인 결과수신 URL
//    		String certDate	= day; // request.getParameter("certDate");                         // 본인실명확인 요청시간
//    		String certGb	= "H"; // request.getParameter("certGb");                           // 본인실명확인 본인확인 인증수단
//    		String addVar	= ""; // request.getParameter("addVar");                           // 본인실명확인 추가 파라메터
//
//    	    //01. 암호화 모듈 선언
//    		com.sci.v2.pccv2.secu.SciSecuManager seed  = new com.sci.v2.pccv2.secu.SciSecuManager();
//
//    		//02. 1차 암호화
//    		String encStr = "";
//    		String reqInfo      = id+"^"+srvNo+"^"+reqNum+"^"+certDate+"^"+certGb+"^"+addVar+"^"+exVar;  // 데이터 암호화
//
//    		seed.setInfoPublic(id, sciPhoneKey);  //bizsiren.com > 회원사전용 로그인후 확인.
//
//    		encStr               = seed.getEncPublic(reqInfo);
//
//    		//03. 위변조 검증 값 생성
//    		com.sci.v2.pccv2.secu.hmac.SciHmac hmac = new com.sci.v2.pccv2.secu.hmac.SciHmac();
//    		String hmacMsg  = seed.getEncReq(encStr,"HMAC");
//
//    		//03. 2차 암호화
//    		reqInfo  = seed.getEncPublic(encStr + "^" + hmacMsg + "^" + "0000000000000000");  //2차암호화
//
//    		//04. 회원사 ID 처리를 위한 암호화
//    		reqInfo = seed.EncPublic(reqInfo + "^" + id + "^"  + "00000000");
//
//    		result = ApiResponseEntity.data().put("reqInfo", reqInfo).put("retUrl", retUrl).put("verSion", "2").ok();
//
//    	} catch (RuntimeException e) {
//        	log.error("RuntimeException {}", e.getStackTrace()[0]);
//        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
//    	 } catch (Exception e) {
//    		 log.error("Exception {}", e.getStackTrace()[0]);
//             result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
//         }
//         return result;
//
//    }

    /**
    *
    * 휴대폰 인증 callback SCI
    * @param request
    * @param response
    * @throws IOException
    */
//    @RequestMapping("/mobile-auth-callback")
//    public RedirectView getMobileAuthCallback(HttpServletRequest request, HttpServletResponse response, RedirectAttributes re) {
//
//		// 변수 --------------------------------------------------------------------------------
//	    String retInfo		= "";																// 결과정보
//
//		String id			= "";                                                               //회원사 비즈사이렌아이디
//		String name			= "";                                                               //성명
//		String sex			= "";																//성별
//		String birYMD		= "";																//생년월일
//		String fgnGbn		= "";																//내외국인 구분값
//		String scCode		= "";																//가상식별번호
//	    String di			= "";																//DI
//	    String ci1			= "";																//CI
//	    String ci2			= "";																//CI
//	    String civersion    = "";                                                               //CI Version
//
//	    String reqNum		= "";                                                               // 본인확인 요청번호
//	    String result		= "";                                                               // 본인확인결과 (Y/N)
//	    String certDate		= "";                                                               // 검증시간
//	    String certGb		= "";                                                               // 인증수단
//		String cellNo		= "";																// 핸드폰 번호
//		String cellCorp		= "";																// 이동통신사
//		String addVar		= "";
//
//		//예약 필드
//		String ext1			= "";
//		String ext2			= "";
//		String ext3			= "";
//		String ext4			= "";
//		String ext5			= "";
//
//		//복화화용 변수
//		String encPara		= "";
//		String encMsg		= "";
//		String msgChk       = "N";
//
//	    //-----------------------------------------------------------------------------------------------------------------
//
//		reqNum = "123456789"; //sample 페이지의 reqNum과 동일하지 않으면 결과페이지 복호화 시 에러
//
//	    try{
//
//	        // Parameter 수신 --------------------------------------------------------------------
//	        retInfo  = StringUtils.defaultIfEmpty(request.getParameter("retInfo"), "").trim(); //반드시 get과 post 방식 둘 다 받을수있게 허용해놔야함.
//
//	        // 1. 암호화 모듈 (jar) Loading
//	        com.sci.v2.pccv2.secu.SciSecuManager sciSecuMg = new com.sci.v2.pccv2.secu.SciSecuManager();
//			sciSecuMg.setInfoPublic(id,sciPhoneKey);  //bizsiren.com > 회원사전용 로그인후 확인.
//
//	        // 3. 1차 파싱---------------------------------------------------------------
//
//			retInfo  = sciSecuMg.getDec(retInfo, reqNum);
//
//			// 4. 요청결과 복호화
//	        String[] aRetInfo1 = retInfo.split("\\^");
//
//			encPara  = aRetInfo1[0];         //암호화된 통합 파라미터
//	        encMsg   = aRetInfo1[1];    //암호화된 통합 파라미터의 Hash값
//
//			String encMsg2   = sciSecuMg.getMsg(encPara);
//
//			// 5. 위/변조 검증 ---------------------------------------------------------------
//
//	        if(encMsg2.equals(encMsg)){
//	            msgChk="Y";
//	        }
//
//	        // 복호화 및 위/변조 검증 ---------------------------------------------------------------
//			retInfo  = sciSecuMg.getDec(encPara, reqNum);
//
//	        String[] aRetInfo = retInfo.split("\\^");
//
//	        name		= aRetInfo[0];
//			birYMD		= aRetInfo[1];
//	        sex			= aRetInfo[2];
//	        fgnGbn		= aRetInfo[3];
//	        di			= aRetInfo[4];
//	        ci1			= aRetInfo[5];
//	        ci2			= aRetInfo[6];
//	        civersion	= aRetInfo[7];
//	        reqNum		= aRetInfo[8];
//	        result		= aRetInfo[9];
//	        certGb		= aRetInfo[10];
//			cellNo		= aRetInfo[11];
//			cellCorp	= aRetInfo[12];
//	        certDate	= aRetInfo[13];
//			addVar		= aRetInfo[14];
//
//			//예약 필드
//			ext1		= aRetInfo[15];
//			ext2		= aRetInfo[16];
//			ext3		= aRetInfo[17];
//			ext4		= aRetInfo[18];
//			ext5		= aRetInfo[19];
//
//			String praram = "result=success&userName=" + name +
//					"&birthday=" + birYMD +
//					"&gender=" + sex +
//					"&mberDi=" + di +
//					"&mberCi=" + ci1 +
//					"&fgnGbn=" + fgnGbn +
//					"&cellNo=" + cellNo +
//					"&cellCorp=" + cellCorp +
//					"&certDate=" + certDate;
//			praram = praram.replaceAll("\\r\\n", "");
//			response.sendRedirect(SalesonProperty.getSalesonUrlFrontend() + "/users/mobile-auth-result.html?" + praram);
//
//	    } catch (RuntimeException e) {
//        	log.error("RuntimeException {}", e.getStackTrace()[0]);
//        	RedirectView redirectView = new RedirectView();
//            redirectView.setUrl(ShopUtils.removeNewlineCharaters(SalesonProperty.getSalesonUrlFrontend() + "/users/mobile-auth-result.html?result=fail"));
//            return redirectView;
//       } catch (Exception e) {
//    	   log.error("Exception {}", e.getStackTrace()[0]);
//           RedirectView redirectView = new RedirectView();
//           redirectView.setUrl(ShopUtils.removeNewlineCharaters(SalesonProperty.getSalesonUrlFrontend() + "/users/mobile-auth-result.html?result=fail"));
//           return redirectView;
//       }
//
//       return null;
//   }


    /**
     * 휴대폰 본인인증
     *
     * @param request
     * @return
     */
    @GetMapping("/mobile-auth")
    public ResponseEntity getMobileAuth(HttpServletRequest request) {
    	ResponseEntity result = null;

    	try {

    		/* 01. PARAM 정의 */
            Calendar today = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String day = sdf.format(today.getTime());

            // 본인실명확인 회원사 아이디
    		String id       = sciPhoneId;
    		// 본인실명확인 서비스번호
    	    String srvNo    = sciPhoneSrvNo;
    	    // 본인실명확인 요청번호 (sample 페이지와 result 페이지가  동일하지 않으면 결과페이지 복호화 시 에러)
    	    String reqNum   = day + sciPhoneUtils.makeReqNo();
    	    // 본인실명확인 결과수신 URL
    	    String retUrl   = "72" + request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/api/auth/mobile-auth-callback";
    	    // 본인실명확인 요청시간
    		String certDate	= day;
    		// 본인실명확인 본인확인 인증수단
    		String certGb	= "H";

    		/* 02. Crypto Token 발급 */
    		String tokenResp = sciPhoneUtils.callCreateCryptoTokenAPI(day, reqNum);
    		JSONObject dataBody = (JSONObject)((JSONObject) new JSONParser().parse(tokenResp)).get("dataBody");
    	    String crypto_token_id = (String) dataBody.get("crypto_token_id");
    	    String crypto_token_val = (String) dataBody.get("crypto_token");


    	    /* 03. 대칭키 생성 */
    	    String symmetricKey = sciPhoneUtils.createSymmetricKey(day, reqNum, crypto_token_val);
    	    String key = symmetricKey.substring(0, 16);
    	    String iv = symmetricKey.substring(symmetricKey.length() - 16, symmetricKey.length());

    	    /* 04. reqInfo 암호화 */
    		String reqInfoData = sciPhoneUtils.getReqData(id, srvNo, reqNum, retUrl, certDate, certGb);
    	    String reqInfoEnc = sciPhoneUtils.getEncReqData(key, iv, reqInfoData);

    	    /* 05. integrity_value (HMAC-SHA256) */
    	    String hmac_key = symmetricKey.substring(0, 32); // 암복호화 위변조 체크용req
    	    byte[] hmacSha256 = sciPhoneUtils.hmac256(hmac_key.getBytes(), reqInfoEnc.getBytes());
    	    String integrityValue = Base64.getEncoder().encodeToString(hmacSha256);

    		result = ApiResponseEntity.data()
    					.put("crypto_token_id", crypto_token_id)
    					.put("integrity_value",	integrityValue)
    					.put("reqInfo", 		reqInfoEnc)
    					.put("verSion", 		"3")
    					.ok();

    	} catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
    	 } catch (Exception e) {
    		 log.error("Exception {}", e.getStackTrace()[0]);
             result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
         }
         return result;

    }

    /**
     *
     * 휴대폰 인증 callback SCI
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/mobile-auth-callback")
    public RedirectView getMobileAuthCallback(HttpServletRequest request, HttpServletResponse response, RedirectAttributes re) {

    	// 변수 --------------------------------------------------------------------------------
    	String retInfo		= "";																// 결과정보

    	String id			= "";                                                               //회원사 비즈사이렌아이디
    	String name			= "";                                                               //성명
    	String gender		= "";																//성별
    	String birYMD		= "";																//생년월일
    	String fgnGbn		= "";																//내외국인 구분값
    	String scCode		= "";																//가상식별번호
    	String di			= "";																//DI
    	String ci1			= "";																//CI
    	String ci2			= "";																//CI
    	String civersion    = "";                                                               //CI Version

    	String reqNum		= "";                                                               // 본인확인 요청번호
    	String result		= "";                                                               // 본인확인결과 (Y/N)
    	String certDate		= "";                                                               // 검증시간
    	String certGb		= "";                                                               // 인증수단
    	String cellNo		= "";																// 핸드폰 번호
    	String cellCorp		= "";																// 이동통신사
    	String addVar		= "";

    	//예약 필드
    	String ext1			= "";
    	String ext2			= "";
    	String ext3			= "";
    	String ext4			= "";
    	String ext5			= "";

    	//복화화용 변수
    	String encPara		= "";
    	String encMsg		= "";
    	String msgChk       = "N";

    	try{

    		/************** Server to Server 추가 **************/
    		/* 01. crypto_token_id 수신 */
	    	String reqCryptoTokenId = request.getParameter("crypto_token_id") != null ? request.getParameter("crypto_token_id").trim() : null ;

	    	/* 02. 새 Crypto Token 발급 */
	    	String reqDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	    	String reqNo 	= sciPhoneUtils.makeReqNo();

	    	String tokenResp 		= sciPhoneUtils.callCreateCryptoTokenAPI(reqDate, reqNo);
    		JSONObject tokenBody 	= (JSONObject)((JSONObject) new JSONParser().parse(tokenResp)).get("dataBody");
    		String cryptoTokenId 	= (String) tokenBody.get("crypto_token_id");
    		String cryptoToken 		= (String) tokenBody.get("crypto_token");
    	    /* 03. 대칭키 생성 */
    	    String symmetricKey = sciPhoneUtils.createSymmetricKey(reqDate, reqNo, cryptoToken);
    	    String key = symmetricKey.substring(0, 16);
    	    String iv = symmetricKey.substring(symmetricKey.length() - 16, symmetricKey.length());

    	    /* 04. reqInto 암호화 및 integrityValue 생성 */
    	    String reqInfoPlain = sciPhoneUtils.getReqData(sciPhoneId,reqCryptoTokenId);
    	    String reqInfoEnc	= sciPhoneUtils.getEncReqData(key, iv, reqInfoPlain);

    	    String integrityValue = sciPhoneUtils.base64Sha256(reqDate + reqNo + cryptoToken);

    	    /* 05. Server to Server API 호출 */
    	    String stosResp 	= sciPhoneUtils.callServerToServerAPI(cryptoTokenId, reqInfoEnc, integrityValue, sciPhoneId);
    	    JSONObject stosBody	= (JSONObject)((JSONObject) new JSONParser().parse(stosResp)).get("dataBody");
    	    String rspCd 		= (String) stosBody.get("rsp_cd");
    	    String retInfoBody	= (String) stosBody.get("RET_INFO");

    	    if(!"P000".equals(rspCd)) {
    	    	throw new RuntimeException("Server to Server Error" + rspCd);
    	    }

    	    /* 06. RET_INFO AES 복호화 -> JSON */
    	    String resData = sciPhoneUtils.getDecReqData(key, iv, retInfoBody);

    	    JSONObject finalJson = (JSONObject) new JSONParser().parse(resData);

    	    name 		= (String) finalJson.get("userName");
    	    birYMD 		= (String) finalJson.get("birYMD");
    	    gender 		= (String) finalJson.get("gender");
    	    fgnGbn 		= (String) finalJson.get("fgnGbn");
    	    di 			= (String) finalJson.get("di");
    	    ci1 		= (String) finalJson.get("ci");
    	    ci2 		= (String) finalJson.get("ci2");
    	    civersion 	= (String) finalJson.get("ciVersion");
    	    reqNum 		= (String) finalJson.get("reqNum");
    	    result		= (String) finalJson.get("result");
    	    certGb 		= (String) finalJson.get("certGb");
    	    cellNo 		= (String) finalJson.get("celNo");
    	    cellCorp 	= (String) finalJson.get("Commid");
    	    certDate 	= (String) finalJson.get("certdate");
    	    addVar 		= (String) finalJson.get("addVar");

    	    /* 07. 프론트로 리다이렉트 */
    		String param = "result=success&userName=" + URLEncoder.encode(name,"UTF-8") +
    				"&birthday=" + birYMD +
    				"&gender=" + gender +
    				"&mberDi=" + URLEncoder.encode(di,"UTF-8") +
    				"&mberCi=" + URLEncoder.encode(ci1,"UTF-8") +
    				"&fgnGbn=" + fgnGbn +
    				"&cellNo=" + cellNo +
    				"&cellCorp=" + cellCorp +
    				"&certDate=" + certDate;
    		param = param.replaceAll("\\r\\n", "");
    		response.sendRedirect(SalesonProperty.getSalesonUrlFrontend() + "/users/mobile-auth-result.html?" + param);

    	} catch (RuntimeException e) {
    		log.error("RuntimeException {}", e.getStackTrace()[0]);
    		RedirectView redirectView = new RedirectView();
    		redirectView.setUrl(ShopUtils.removeNewlineCharaters(SalesonProperty.getSalesonUrlFrontend() + "/users/mobile-auth-result.html?result=fail"));
    		return redirectView;
    	} catch (Exception e) {
    		log.error("Exception {}", e.getStackTrace()[0]);
    		RedirectView redirectView = new RedirectView();
    		redirectView.setUrl(ShopUtils.removeNewlineCharaters(SalesonProperty.getSalesonUrlFrontend() + "/users/mobile-auth-result.html?result=fail"));
    		return redirectView;
    	}

    	return null;
    }

    public String requestReplace (String paramValue, String gubun) {

        String result = "";

        if (paramValue != null) {

        	paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        	paramValue = paramValue.replaceAll("\\*", "");
        	paramValue = paramValue.replaceAll("\\?", "");
        	paramValue = paramValue.replaceAll("\\[", "");
        	paramValue = paramValue.replaceAll("\\{", "");
        	paramValue = paramValue.replaceAll("\\(", "");
        	paramValue = paramValue.replaceAll("\\)", "");
        	paramValue = paramValue.replaceAll("\\^", "");
        	paramValue = paramValue.replaceAll("\\$", "");
        	paramValue = paramValue.replaceAll("'", "");
        	paramValue = paramValue.replaceAll("@", "");
        	paramValue = paramValue.replaceAll("%", "");
        	paramValue = paramValue.replaceAll(";", "");
        	paramValue = paramValue.replaceAll(":", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll("#", "");
        	paramValue = paramValue.replaceAll("--", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll(",", "");

        	if(gubun != "encodeData"){
        		paramValue = paramValue.replaceAll("\\+", "");
        		paramValue = paramValue.replaceAll("/", "");
            paramValue = paramValue.replaceAll("=", "");
        	}

        	result = paramValue;

        }
        return result;
  }

    /**
     * 간편인증 접속정보 수집을 위한 api
     *
     * @param request
     * @return
     */
    @PostMapping("/getAccessInfo")
    public ResponseEntity getAccessInfo(@RequestBody String fn) {
    	ResponseEntity result = null;
    	String accKey = "";
		String accToken = "";
		String errorMsg = "";
		String resultMsg = "";
    	try {
    		//이용기관 억세스 정보 가져오기
    		OacxUtil oacx = new OacxUtil();		//OacxUtil 선언
    		String jsonPath = "";
    		jsonPath = simpleAuthJsonPath; //json 파일 위치의 절대경로 입력
    		log.debug(">>>>>>>>>>>>>>> simpleAuthJsonPath");
    		log.debug("getAccessInfo jsonPath :: " + jsonPath);

    		Map<String,String> loadJsonResult = oacx.loadJSONInfo(jsonPath);
    		log.debug("########### loadJsonResult=[{}]", loadJsonResult.toString());
    		if(!"success".equals(loadJsonResult.get("status"))) {
    			//error
    			errorMsg = loadJsonResult.get("message");
    			resultMsg = "msg="+errorMsg+",result=error";
    		} else {
    			Map<String,String> accMap = oacx.getAccessInfo();	//접속정보를 읽어 온다.

    			if("success".equals(accMap.get("status"))){

    				accKey = accMap.get("accKey");
    				accToken = accMap.get("accToken");
    				resultMsg = "accKey="+accKey+",accToken="+accToken+",result=success";
    				log.debug("AccToken 생성 성공 : result="+ resultMsg);
    			} else {
    				errorMsg = accMap.get("message");
    				resultMsg = "msg="+errorMsg+",result=error";
    				log.debug("AccToken 생성 실패 : result="+ resultMsg);
    			}
    		}

    		result = ApiResponseEntity.data()
    				.put("fn", fn)
    				.put("accKey", accKey)
    				.put("accToken", accToken)
    				.put("errorMsg", errorMsg)
    				.put("resultMsg", resultMsg)
    				.put(STATUS, HttpStatus.OK).ok();
    	} catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
    	} catch (Exception e) {
    		log.error("Exception {}", e.getStackTrace()[0]);
    		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
    	}
    	return result;

    }

    /**
     * 간편인증 접속정보 수집을 위한 api
     *
     * @param request
     * @return
     */
    @PostMapping("/getSimpleAuthResult")
    public ResponseEntity getSimpleAuthResult(@RequestBody String jsonData, HttpServletRequest request) {
    	ResponseEntity result = null;

    	Map<String, String> returnMap = new HashMap<String,String>();
    	String loginType = "ROLE_USER";
        String loginId = "";
    	try {
    		OacxUtil oacx = new OacxUtil();		//OacxUtil 선언

            JSONObject jsonObj = new JSONObject();
            JSONParser parse = new JSONParser();
            jsonObj = (JSONObject) parse.parse(jsonData);
            JSONObject resObj = (JSONObject) jsonObj.get("res");
//            String jsonPath = (String) jsonObj.get("jsonPath");
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
            } catch (RuntimeException e) {
            	log.error("/api/auth/getSimpleAuthResult] ERROR {}",e.getStackTrace()[0]);
            } catch (Exception e){
                errorMsg = e.toString();
                resultMsg = "msg="+errorMsg+",result=error";
                log.debug("resultMsg =" + resultMsg);
            }
            log.debug("map=" +map);

            oacx.loadJSONInfo(jsonPath);		//json 파일 정보 로드

            String fnName = (String) map.get("fn");
            //간편인증 결과
            Map<String, Object> resMap = new HashMap<String, Object>();
            resMap = (Map<String, Object>) map.get("res");

            //간편인증(휴대폰번호 인증 일 시)
            if(fnName.equals("authComplete") && "200".equals((String)resMap.get("resultCode"))) {
            	//생일, 이름, 전화번호, CI 복호화
            	Map<String, String> oacxMap = oacx.decryptResult(map);
            	if("success".equals(oacxMap.get("status"))){

                    //ci로 user 정보 조회
                    AuthInfo authInfo = new AuthInfo();
                    authInfo.setMberCi(oacxMap.get("ci"));
                    UserInfoByCI ciInfo = userService.getUserInfoByCi(authInfo);

                    if(ciInfo==null) {
                    	returnMap.put("message", "NoUser");
                    }else {

                    	loginId = ciInfo.getLoginId();
                    	String password = userService.getCryptoDecByLoginId(loginId);	// DB 에서 password 를 decodeing 해 온다.

                     	IdPasswordAuthenticationToken authRequest = new IdPasswordAuthenticationToken(loginId, password, loginType, null);

                     	User user = userService.getUserByLoginId(loginId);
                        String userKey = userService.getUserKeyInfo(user.getUserId());
                        String code = "";
//                        if("ROLE_USER".equals(loginType)) {}

                        String token = jwtTokenService.getJwtToken(
                                 loginType,
                                 loginId,
                                 password,
                                 JwtUtils.getClientIpAddress(request)
                         );

                         returnMap.put("token", token);
                         returnMap.put("code", code);

                         //updateLoginCount(loginType, user);
                         loginLogService.insertLoginLogByUser(request, loginId, true);

                         //중복로그인 추가 (2022.11.01)
                         long userId = user.getUserId();
                         if ("ROLE_USER".equals(loginType)) {
                             saveLoginSessionForUser(userId, token);
                         } else if ("ROLE_OPMANAGER".equals(loginType)) {
                             saveLoginSessionForManager(userId, token);
                         } else if ("ROLE_SELLER".equals(loginType)) {
                             saveLoginSessionForSeller(userId, token);
                         }

                    	returnMap.put("message", "Success");
                    	returnMap.put("loginId", ciInfo.getLoginId());
                    }

            	} else {
            		log.debug("@@@@ returnMap  fail message  ==  "+oacxMap.get("message"));
            		returnMap.put("message", "Error");
            	}
            }

    		result = ApiResponseEntity.data()
    				.put("data", returnMap)
    				.put(STATUS, HttpStatus.OK).ok();
    	} catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		 } catch (Exception e) {
			 log.error("Exception {}", e.getStackTrace()[0]);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		 }

    	return result;

    }

    @GetMapping("/session-timeout")
    public ResponseEntity getSessionTimeout(HttpServletRequest request) {
        int timeout = -1;
        try {
            if (UserUtils.isUserLogin()) {
                timeout = UserUtils.getUser().getSessionTimeout();
            }
        } catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        } catch (Exception e) {
        	log.error("Exception {}", e.getStackTrace()[0]);
        }
        return ApiResponseEntity.data().put("timeout", timeout).ok();
    }

    @PostMapping("/onepass-unlink")
    public ResponseEntity onepassUnlink(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> paramMap ) {
    	ResponseEntity result = null;
    	Map<String, String> map = new HashMap<>();

    	try {
	    	ApiSendHandler apiSendHandler = new ApiSendHandler();

	    	String userKey = paramMap.get("userKey");
	    	String intfToken = paramMap.get("intfToken");

	    	OnepassUserResponse onepassUser = apiSendHandler.InterLockRelease(userKey, intfToken);

	    	if (Objects.isNull(onepassUser)) {
	    		onepassUser = new OnepassUserResponse();

	    		map.put("status", "00");
                map.put("message", "디지털원패스 연동해지 할 수 없는 상태입니다");
                map.put("value", "01");

	    	} else {
	    		if (onepassUser.getProcess_result().toString() == "SUCESS") {
	    			// userkey 삭제
	    			userService.updateOnepassUnlink(UserUtils.getUserId());

	    			map.put("status", "00");
		            map.put("message", "디지털원패스 연동해지가 완료되었습니다.");
		            map.put("value", "00");
	    		} else {
	    			map.put("status", "00");
                    map.put("message", "디지털원패스 연동해지 할 수 없는 상태입니다.");
                    map.put("value", "01");
	    		}
	    	}

	    	result = ApiResponseEntity.data().put("info", map).ok();

    	} catch (RuntimeException e) {
        	log.error("RuntimeException {}", e.getStackTrace()[0]);
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
    	} catch (Exception e) {
    		log.error("Exception {}", e.getStackTrace()[0]);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
    	return result;
    }
}
