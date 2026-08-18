package saleson.common.security.authentication.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.onlinepowers.framework.common.OpKeyHolder;
import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.security.userdetails.OpUserDetails;
import com.onlinepowers.framework.security.userdetails.OpUserDetailsService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CipherUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.privacy.pCrypto;

import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.UnsupportedJwtException;
import saleson.common.Const;
import saleson.common.exception.DuplicationLoginException;
import saleson.common.exception.InvalidAuthenticationException;
import saleson.common.exception.SecureServletException;
import saleson.common.security.api.JwtCode;
import saleson.common.security.api.JwtTokenService;
import saleson.common.security.api.TokenAuthGuestUserInfo;
import saleson.common.security.api.TokenAuthUserInfo;
import saleson.common.security.authentication.JwtTokenAuthenticationToken;
import saleson.common.utils.JwtUtils;
import saleson.model.user.SellerUserLogin;
import saleson.model.user.UserLogin;
import saleson.seller.user.SellerUserService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.GuestUser;
import saleson.shop.user.domain.ManagerLogin;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.userlevel.UserLevelMapper;
import saleson.shop.userlevel.domain.UserLevel;

public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    private Logger log = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);

    @Autowired
    private OpUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserLevelMapper userLevelMapper;

    @Autowired
    private SellerUserService sellerUserService;
    
    private List<String> urlList;

    private List<String> ignoreApiList;

    // 파라미터로 토큰처리 API 목록
    private List<String> paramTokenApiList;

    public JwtTokenAuthenticationFilter(String... url) {
        urlList = new ArrayList<>();

        if (url != null && url.length > 0) {
            for (String m : url) {
                urlList.add(m);
            }
        }

        ignoreApiList = new ArrayList<>();

        ignoreApiList.add("/api/auth/token");
        ignoreApiList.add("/api/auth/sns-token");
        ignoreApiList.add("/api/auth/send-auth-number");

        ignoreApiList.add("/api/auth/find-id");
        ignoreApiList.add("/api/auth/find-password-step1");
        ignoreApiList.add("/api/auth/find-password-step2");

        ignoreApiList.add("/api/auth/change-password");
        ignoreApiList.add("/api/auth/delay-change-password");

        ignoreApiList.add("/api/auth/recovery");
        ignoreApiList.add("/api/auth/join");
        ignoreApiList.add("/api/auth/check-auth-number");

        // header.vue & footer.vue - search
        ignoreApiList.add("/api/category");
        ignoreApiList.add("/api/policy/clause");
        ignoreApiList.add("/api/display/lately");
        ignoreApiList.add("/api/search/best-keyword");

        ignoreApiList.add("/api/seo");

        ignoreApiList.add("/api/reload-cache/*");
        
        // 서울시 이택스 결재 return url
        ignoreApiList.add("/api/donation/eTax/return-url");

        // 외부연계관련
        ignoreApiList.add("/api/external/userCntrInfo");
        ignoreApiList.add("/api/external/etaxResult");
        ignoreApiList.add("/api/external/sunapSuccess");
        ignoreApiList.add("/api/external/sunapSuccess2");
        
        // 주문대행 관련
        ignoreApiList.add("/api/item");
        ignoreApiList.add("/api/ngdonation/sidoList");
        ignoreApiList.add("/api/ngdonation/sigunguList");
        ignoreApiList.add("/api/category/updated-check");
        ignoreApiList.add("/api/item/qna");
        ignoreApiList.add("/api/item/reviews");

        paramTokenApiList = new ArrayList<>();
        paramTokenApiList.add("/api/order/naverpay/payment");

    }

    private boolean isIgnoreAuthUri (HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String uri = request.getRequestURI();
        for (String pattern : ignoreApiList) {
            if (antPathMatcher.match(pattern, uri)) {
                return true;
            }
        }

        return false;
    }

    private boolean isParamTokenApiUrl(HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String uri = request.getRequestURI();

        for (String pattern : paramTokenApiList) {
            if (antPathMatcher.match(pattern, uri)) {
                return true;
            }
        }

        return false;
    }


    private boolean isApiUrl(HttpServletRequest request) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String uri = request.getRequestURI();

        if (isIgnoreAuthUri(request)) {
            return false;
        }

        for (String pattern : urlList) {
            if (antPathMatcher.match(pattern, uri)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        boolean hasError = false;
        Exception messageException = null;
        int status = HttpServletResponse.SC_OK;
        String error = "";
        String code = "";
        String description = "";
        String errorType = "";
        String token = "";

        if (isApiUrl(request)) {

            try {

                SecurityContextHolder.clearContext();

                token = JwtUtils.getToken(request);

                if (isParamTokenApiUrl(request)) {
                    String decryptedString = CipherUtils.decrypt(request.getParameter("encryptedString"));
                    token = decryptedString.split("\\|")[0];
                }
                
                if (isAgencyApiUrl(request)) {
                    token = selectOrderAgencyDecryptedToken(token);
                }

                if (!JwtUtils.isEmpty(token)) {

                    // claims 가져오면서 토큰에 기본적인 체크를 함.
                    // 부합되면 거기에 맞는 Exception 발생
                    Jws<Claims> claims = tokenService.getClaimsByToken(token);

                    // 자체 시스템 인증 시작
                    if (!tokenService.isTokenAuthentication(token, claims)) {
                        throw new InvalidAuthenticationException();
                    }

                    String loginType = (String) JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_LOGIN_TYPE));

                    if (ObjectUtils.isEmpty(loginType)) {
                        throw new InvalidAuthenticationException();
                    }


                    // 게스트
                    if ("ROLE_GUEST".equals(loginType)) {
                        processGuest(token, request, response, chain);
                    } else {
                        processUser(token, loginType, request, response , chain);
                    }

                } else {
                    chain.doFilter(request, response);
                }

            } catch (CompressionException e) {

                messageException = e;
                errorType = "CompressionException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Invalid Token Forment";
                description = "유효한 토큰형식이 아닙니다.";

            } catch (IncorrectClaimException e) {

                messageException = e;
                errorType = "IncorrectClaimException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Incorrect Claim";
                description = "잘못된 Claim 입니다.";

            } catch (ExpiredJwtException e) {

                messageException = e;
                errorType = "ExpiredJwtException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "UNAUTHORIZED";
                error = "Invalid Token Time Out";
                description = "토큰이 만료 되었습니다.";

            } catch (MalformedJwtException e) {

                messageException = e;
                errorType = "MalformedJwtException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Invalid Token Format";
                description = "유효한 토큰형식이 아닙니다.";

            } catch (MissingClaimException e) {

                messageException = e;
                errorType = "MissingClaimException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Missing Claim";
                description = "Claim이 존재하지 않습니다.";

            } catch (PrematureJwtException e) {

                messageException = e;
                errorType = "PrematureJwtException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Invalid Token";
                description = "유효한 토큰이 아닙니다.";

            } catch (RequiredTypeException e) {

                messageException = e;
                errorType = "RequiredTypeException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Required Token Type";
                description = "토큰에 필수 입력 요소가 없습니다.";

            } catch (io.jsonwebtoken.security.SignatureException e) {

                messageException = e;
                errorType = "SignatureException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Invalid Token";
                description = "유효한 토큰이 아닙니다.";

            } catch (InvalidClaimException e) {

                messageException = e;
                errorType = "InvalidClaimException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Invalid Claim";
                description = "유효한 토큰이 아닙니다.";

            } catch (ClaimJwtException e) {

                messageException = e;
                errorType = "ClaimJwtException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Jwt Token Error";
                description = "Jwt Token Error";

            } catch (UnsupportedJwtException e) {

                messageException = e;
                errorType = "UnsupportedJwtException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Not Supported Token";
                description = "유효한 토큰이 아닙니다.";

            } catch (JwtException e) {

                messageException = e;
                errorType = "JwtException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "BAD_REQUEST";
                error = "Jwt Token Error";
                description = "Jwt Token Error";

            } catch (InvalidAuthenticationException e) {

                messageException = e;
                errorType = "InvalidAuthenticationException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "UNAUTHORIZED";
                error = "Invalid Token";
                description = "유효한 토큰이 아닙니다.";

                if (e.isSleepUserFlag()) {
                    code = "SLEEP_USER";
                }

                if (e.isPasswordExpiredFlag()) {
                    code = "PASSWORD_EXPIRED";
                }
            } catch (UsernameNotFoundException e) {

                messageException = e;
                errorType = "UsernameNotFoundException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "UNAUTHORIZED";
                error = "Invalid Token";
                description = "유효한 토큰이 아닙니다.";

            } catch (BadCredentialsException e) {

                messageException = e;
                errorType = "BadCredentialsException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "UNAUTHORIZED";
                error = "Invalid Token";
                description = "유효한 토큰이 아닙니다.";

            } catch(SecureServletException e) {

                messageException = e;
                errorType = "SecureServletException";
                hasError = true;
                status = HttpServletResponse.SC_BAD_REQUEST;
                code = "BAD_REQUEST";
                error = "Not Https";
                description = "Https만 이용 가능합니다.";

            } catch (DuplicationLoginException e) {
				
            	messageException = e;
                errorType = "DuplicationLoginException";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "DUPLICATION_LOGIN";
                error = "DUPLICATION_LOGIN";
                description = "다른 기기에서 로그인하여 자동으로 로그아웃 되었습니다.";
            	
			} catch (Exception e) {

                messageException = e;
                errorType = "Exception";
                hasError = true;
                status = HttpServletResponse.SC_UNAUTHORIZED;
                code = "INTERNAL_SERVER_ERROR";
                error = "Server Side Process Error";
                description = "관리자에게 문의 부탁 드립니다.";

            }
			if (hasError) {

				String errorMessage = JwtUtils.getErrorMessage(status, code, error, description);
				log.error(JwtUtils.getErrorLog(errorType, request.getRequestURL().toString(), token), messageException);
				response.setContentType("application/json");
				response.setStatus(status);
				//response.getOutputStream().write(errorMessage.getBytes(JwtUtils.getCode(JwtCode.JWT_CHARSET)));
				ServletOutputStream out = response.getOutputStream();

                out.write(errorMessage.getBytes(JwtUtils.getCode(JwtCode.JWT_CHARSET)));
                out.flush();
                out.close();
			}

        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean validateToken(String token, TokenAuthUserInfo tokenAuthUserInfo) throws Exception{
        return tokenService.validateToken(token, tokenAuthUserInfo);
    }

    private boolean validateGuestToken(String token, TokenAuthGuestUserInfo tokenAuthGuestUserInfo) throws Exception{
        return tokenService.validateGuestToken(token, tokenAuthGuestUserInfo);
    }

    private void processGuest(String token, HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Exception{

        GuestUser guestUser = JwtUtils.getGuestUser(token);

        if (guestUser == null) {
            throw new InvalidAuthenticationException();
        }

        TokenAuthGuestUserInfo tokenAuthGuestUserInfo = new TokenAuthGuestUserInfo(
                guestUser,
                JwtUtils.getJti(token)
        );

        if (!validateGuestToken(token, tokenAuthGuestUserInfo)) {
            throw new InvalidAuthenticationException();
        }

        chain.doFilter(request, response);
    }

    private void processUser(String token, String loginType, HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Exception{

        String loginId = (String) JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_ID));

        if ("ROLE_OPMANAGER".equals(loginType)) {
            loginId = loginId + OpKeyHolder.OPMANAGER_LOGIN_KEY;
        } else if("ROLE_SELLER".equals(loginType)){
            loginId = loginId + OpKeyHolder.SELLER_LOGIN_KEY;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);

        if (userDetails == null) {
            throw new InvalidAuthenticationException();
        }

        if ("ROLE_OPMANAGER".equals(loginType)) {
            loginId = loginId.replaceAll(OpKeyHolder.OPMANAGER_LOGIN_KEY,"");
        } else if("ROLE_SELLER".equals(loginType)){
            loginId = loginId.replaceAll(OpKeyHolder.SELLER_LOGIN_KEY,"");
        }

        User user = ((OpUserDetails) userDetails).getUser();

        TokenAuthUserInfo tokenAuthUserInfo = new TokenAuthUserInfo(
                loginType,
                loginId,
                /*userDetails.getPassword(),*/
                user.getPassword(),
                JwtUtils.getJti(token)
        );

        if (validateToken(token, tokenAuthUserInfo)) {

        	//사용자 중복로그인 추가(2022.11.01)
        	checkedLoginToken(loginType, user.getUserId(), token);
        	 
            if("ROLE_USER".equals(loginType)) {
                if ("4".equals(user.getStatusCode())) {
                    throw new InvalidAuthenticationException("휴면 전환된 계정입니다.", true, false);
                }

                int passwordExpiredDateDiff = DateUtils.getDaysDiff(DateUtils.getToday(Const.DATE_FORMAT), user.getPasswordExpiredDate());
				if(!"P".equals(user.getPasswordType())) {
                	if ("T".equals(user.getPasswordType()) || passwordExpiredDateDiff <= 0) {
                        throw new InvalidAuthenticationException("비밀번호 기한이 만료 되었습니다.", false, true);
                    }
                }
            }

            JwtTokenAuthenticationToken authenticationToken = new JwtTokenAuthenticationToken(userDetails);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);


            UserDetail userDetail = userService.getUserDetail(securityService.getCurrentUserId());

            logger.debug("로그인체크>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>::"+userDetail);

            
            // 회원 Level이 설정되어있는경우 해당 Level 정보를 조회해서 상품별 할인율을 구한다.
            // 상품 상세, 장바구니, 주문을 재외한 페이지에서 등급별 할인을 Session으로 처리하기 위함
            if (userDetail.getLevelId() > 0) {
                UserLevel userLevel = userLevelMapper.getUserLevelById(userDetail.getLevelId());
                if (userLevel != null) {
                    userDetail.setUserLevelDiscountRate(userLevel.getDiscountRate());
                    userDetail.setUserLevelPointRate(userLevel.getPointRate());
                }
            }

            Object princial = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (princial instanceof UserDetails) {
                ((OpUserDetails) princial).setUserDetail(userDetail);
            }

        } else {
            throw new InvalidAuthenticationException();
        }

        chain.doFilter(request, response);
    }
    
    /**
     * 토큰 체크
     * @param loginType
     * @param userId
     * @param token
     * @throws Exception
     */
    private void checkedLoginToken(String loginType, long userId, String token) throws Exception{
        // 중복 로그인 체크
        String sign = (String)JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_SIGN));
        long count = 0;

        if ("ROLE_OPMANAGER".equals(loginType)) {
            List<ManagerLogin> list = userService.getLoginSessionForManagerByUserId(userId);
            count = list.stream()
                    .filter(userLogin -> sign.equals(userLogin.getSessionId()))
                    .count();
        } else if("ROLE_SELLER".equals(loginType)){
            List<SellerUserLogin> list = sellerUserService.getLoginSessionByUserId(userId);
            count = list.stream()
                    .filter(userLogin -> sign.equals(userLogin.getSessionId()))
                    .count();
        } else if("ROLE_USER".equals(loginType)) {
            List<UserLogin> list = userService.getLoginSessionForUserByUserId(userId);
            count = list.stream()
                    .filter(userLogin -> sign.equals(userLogin.getSessionId()))
                    .count();
        } else if("ROLE_AGENCY".equals(loginType)) {
            List<UserLogin> list = userService.getLoginSessionForUserByUserId(userId);
            count = list.stream()
                    .filter(userLogin -> sign.equals(userLogin.getSessionId()))
                    .count();
        }
        if (count == 0) { throw new DuplicationLoginException(); }
    }
    
    private String selectOrderAgencyDecryptedToken(String encToken) {
    	try {
        	return pCrypto.Decrypt("normal", encToken, "", 0);	
    	} catch(UnsupportedEncodingException | NullPointerException e) {
    		return encToken;
    	}
    }

    private boolean isAgencyApiUrl(HttpServletRequest request) {
        String uri = request.getRequestURI();

        if (uri != null && uri.startsWith("/api/order-agency/")) {
        	return true;
        }

        return false;
    }
    
}
