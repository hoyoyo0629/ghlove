package saleson.shop.kakaolink.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;

import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.api.common.enumerated.ApiError;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoLink {
	
	// 토큰 타입(bearer 로 고정)
	@JsonProperty("grant_type")
	private String grantType;
	
	// 
	@JsonProperty("client_id")
	private String clientId;
	
	
	@JsonProperty("redirect_uri")
	private String redirectUri;
	
	private String code;
	
	@JsonProperty("client_secret")
	private String clientSecret;
	
	@JsonProperty("token_type")
	private String tokenType;
	
	@JsonProperty("app_id")
	private String appId;
	
	@JsonProperty("user_id")
	private String userId;
	
	@JsonProperty("referrer_type")
	private String referrerType;
	
	// 사용자 액세스 토큰값
	@JsonProperty("access_token")
	private String accessToken;
	
	// 전자서명 접수번호(전자서명 검증 API 요청시 사용)
	@JsonProperty("tx_id")
	private String txId;
	
	// 사용자 리프레시 토큰값
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	// 사용자 만료시간
	@JsonProperty("expires_in")
	private String expiresIn;
	
	// 사용자 리프레시 토큰 만료 시간
	@JsonProperty("refresh_token_expires_in")
	private String refreshTokenExpiresIn;
	
	// 동의 항목
	private String scope;
	
	// 카카오 사용자 아이디
	private String id;
	
	// 연결 시각
	@JsonProperty("connected_at")
	private String connectedAt;
	
	
	// 고향사랑e음 가입 구분
	private String loginPathCode;
	
	// 카카오 사용자 키
	private String kakaoUserKey;
	
	// 로그인아이디
	private String loginId;
	
	// ci
//	@JsonProperty("ci")
//	private String mberCi;
	
	// 오류
	private ApiError apiError;
	
	// 토큰
	private String token;	

	// 카카오계정 정보
	@JsonProperty("kakao_account")
	private KakaoAccount kakaoAccount;
	
	// 이름
	private String name;
	
	// 검증용 signData
	private String requestToken;
	
	// 요청 화면 - JOIN 일 경우 회원가입화면
	private String type;
	
	// 5부제 관련 메시지
	private String alternateSystemMsg;
	

//	// 이메일
//	private String email;
//	
//	// 전화번호
//	@JsonProperty("phone_number")
//	private String phoneNumber;
//	
//	// 출생연도
//	private String birthyear;
//	
//	// 생일
//	private String birthday;
//	
//	// 양력/음력
//	@JsonProperty("birthday_type")
//	private String birthdayType;
//
//	
//	@JsonProperty("has_email")
//	private boolean hasEmail;
//
//	
//	@JsonProperty("email_needs_agreement")
//	private boolean emailNeedsAgreement;
//
//	
//	@JsonProperty("is_email_valid")
//	private boolean isEmailValid;
//
//	
//	@JsonProperty("is_email_verified")
//	private boolean isEmailVerified;
//
//	
//	@JsonProperty("has_phone_number")
//	private boolean hasPhoneNumber;
//
//	
//	@JsonProperty("phone_number_needs_agreement")
//	private boolean phoneNumberNeedsAgreement;
//
//	
//	@JsonProperty("has_birthyear")
//	private boolean hasBirthyear;
//
//	
//	@JsonProperty("birthyear_needs_agreement")
//	private boolean birthyearNeedsAgreement;
//
//	
//	@JsonProperty("has_birthday")
//	private boolean hasBirthday;
//
//	
//	@JsonProperty("birthday_needs_agreement")
//	private boolean birthdayNeedsAgreement;
	
	
}
