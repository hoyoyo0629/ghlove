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
public class NaverToolkitData {
	
	private String status;
	
	private String result;
	
	private String ci;
	
	private String type;

	@JsonProperty("tx_id")
	private String txId;

	@JsonProperty("access_token")
	private String accessToken;
	
	/**
	 * 네이버 로그인 페이지 정보
	 */
	@JsonProperty("login_url")
	private String loginUrl;
	
	/**
	 * 네이버 인증 로그인 페이지 정보
	 */
	@JsonProperty("auth_polling_url")
	private String authPollingUrl;
	
	/**
	 * 네이버 계정 정보
	 */
	private NaverAccount profile;
	
	public KakaoLink convertToKakaoLink() {
		if (profile == null || !StringUtils.hasLength(profile.getId())) {
			throw new UserException("네이버 인증 로그인에 실패했습니다.");
		}
		
		KakaoLink result = new KakaoLink();
		KakaoAccount account = new KakaoAccount();
		account.setBirthyear(profile.getBirthday().substring(0, 4));
//		account.setBirthday(profile.getBirthday().substring(4));
		account.setBirthday(profile.getBirthday());
		account.setBirthdayType(profile.getBirthdayType());
		account.setEmail(profile.getEmail());
		account.setMberCi(ci);
		account.setId(profile.getId());
		account.setName(profile.getName());
		account.setPhoneNumber(profile.getPhoneNumber());
		result.setKakaoAccount(account);
		
		return result;
	}
	
	
}
