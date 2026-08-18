package saleson.api.auth;

import java.net.URI;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onlinepowers.framework.exception.BizException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CipherUtils;
import com.onlinepowers.framework.util.JsonViewUtils;

import saleson.api.auth.domain.AsisRequestAuth;
import saleson.api.auth.domain.AsisRequestGuestAuth;
import saleson.common.utils.UserUtils;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.UserDetail;

@Service("asisAuthService")
public class AsisAuthService extends EgovAbstractServiceImpl {

    private Logger log = LoggerFactory.getLogger(AsisAuthService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate customRestTemplate;

    @Autowired
    private Environment environment;

    public void processAuthUser(String loginId, String password) throws Exception{


        if (ObjectUtils.isEmpty(loginId) || ObjectUtils.isEmpty(password)) {
            throw new IllegalArgumentException("아이디 또는 패스워드가 존재 하지 않습니다.");
        }

        User user = userService.getUserByLoginId(loginId);

        if (user != null && "PASSWORD".equals(user.getPassword())) {

            // 통신
            String token;
            boolean result = false;
			token = CipherUtils.encrypt(JsonViewUtils.objectToJson(new AsisRequestAuth(loginId, password)));

            URI uri = new URI(getUriString("/auth/login", token));
            RequestEntity requestEntity = new RequestEntity( new HttpHeaders(), HttpMethod.POST, uri);

            ResponseEntity<String> entity = customRestTemplate.exchange(requestEntity, String.class);

	            result = this.getResult(entity);
            if (result) {
                userService.updatePasswordByAsisUser(user.getUserId(), password);
            } else {
                throw new IllegalArgumentException("ASIS 로그인 인증 실패");
            }
        }

    }

    public String getAsisToken() throws Exception{
        String token = "";
        User user;

        if (UserUtils.isUserLogin()) {
            user = UserUtils.getUser();
            token = JsonViewUtils.objectToJson(new AsisRequestAuth(user.getLoginId()));

        } else if (UserUtils.isGuestLogin()){
            user = UserUtils.getGuestLogin();
            if(user.getUserName() != null && user.getUserDetail() != null) {
            	UserDetail userDetail = (UserDetail) user.getUserDetail();
            	token = JsonViewUtils.objectToJson(new AsisRequestGuestAuth(StringUtils.defaultIfEmpty(user.getUserName(), ""), StringUtils.defaultIfEmpty(userDetail.getPhoneNumber(), "")));
            }
        }

        if (!ObjectUtils.isEmpty(token)) {
            token = CipherUtils.encrypt(token);
        }

        return token;
    }

    private boolean getResult(ResponseEntity<String> entity) {

        if (entity != null) {

            if (HttpStatus.OK == entity.getStatusCode()) {

                HashMap<String, Object> body = getBody(entity);
                if (body != null) {
                        return (Boolean) body.get("result");

                }
            } else {
                log.error(JsonViewUtils.objectToJson(entity));
            }
        }

        return false;
    }

    private HashMap<String, Object> getBody(ResponseEntity<String> entity) {

        if (entity != null) {

            String body = entity.getBody();

            if (!ObjectUtils.isEmpty(body)) {
            	return (HashMap<String, Object>) JsonViewUtils.jsonToObject(body, new TypeReference<HashMap<String, Object>>() {});
            }
        }

        return null;
    }

    private String getUriString(String path, String token) throws IllegalArgumentException{
        String host = environment.getProperty("asis.host");

        if (ObjectUtils.isEmpty(host) || ObjectUtils.isEmpty(path)) {
            throw new IllegalArgumentException("ASIS 통신 URI가 없습니다");
        }

        if (ObjectUtils.isEmpty(token)) {
            throw new IllegalArgumentException("Token이 없습니다");
        }

        String uriString = host + path;
        uriString += "?q="+token;

        return uriString;
    }
}
