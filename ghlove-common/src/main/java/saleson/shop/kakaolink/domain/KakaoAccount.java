package saleson.shop.kakaolink.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonObject;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.api.common.enumerated.ApiError;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAccount {
	
	// 카카오 아이디(시퀀스)
	private String id;
	
	// ci
	@JsonProperty("ci")
	private String mberCi;

	// 이름
	private String name;

	// 이메일
	private String email;
	
	// 전화번호
	@JsonProperty("phone_number")
	private String phoneNumber;
	
	// 출생연도
	private String birthyear;
	
	// 생일
	private String birthday;
	
	// 양력/음력
	@JsonProperty("birthday_type")
	private String birthdayType;

	
	@JsonProperty("has_email")
	private boolean hasEmail;

	
	@JsonProperty("email_needs_agreement")
	private boolean emailNeedsAgreement;

	
	@JsonProperty("is_email_valid")
	private boolean isEmailValid;

	
	@JsonProperty("is_email_verified")
	private boolean isEmailVerified;

	
	@JsonProperty("has_phone_number")
	private boolean hasPhoneNumber;

	
	@JsonProperty("phone_number_needs_agreement")
	private boolean phoneNumberNeedsAgreement;

	
	@JsonProperty("has_birthyear")
	private boolean hasBirthyear;

	
	@JsonProperty("birthyear_needs_agreement")
	private boolean birthyearNeedsAgreement;

	
	@JsonProperty("has_birthday")
	private boolean hasBirthday;

	
	@JsonProperty("birthday_needs_agreement")
	private boolean birthdayNeedsAgreement;
	
	@JsonProperty("request_token")
	private String requestToken;
	
	// 네이버 인증가능 툴킷 데이터
	private String phone;
	
	public String getPhoneNumber() {
		if (StringUtils.hasLength(phoneNumber)) {
			return phoneNumber.replace("+82 ", "0");
		} else if (StringUtils.hasLength(phone)){
			return phone;
		} else {
			throw new UserException("카카오톡 인증 전화번호가 존재하지 않습니다.");
		}
	}
	
	public String getMberCi() {
		if (StringUtils.hasLength(mberCi)) {
			return mberCi.replaceAll("\"", "");			// 카카오 js 라이브러리 2.5.0 -> 2.7.2 변경 후 ci값 앞뒤로 "가 추가되어 수정, 확인 필요
		} else {
			return mberCi;
		}
	}
	
}
