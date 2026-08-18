package saleson.shop.kakaolink.domain;


import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoToolkitData {
	
	private String result;
	
	@JsonProperty("request_token")
	private String requestToken;
	
	private String ci;
	
	private KakaoAccount profile;
	
	
	// 네이버 툴킷용
	private String status;

	// 네이버 툴킷용
//	private String document;
	
	@JsonProperty("access_token")
	private String accessToken;
	
	public String getRequestToken() {
		if (StringUtils.hasLength(requestToken)) {
			return requestToken;
		} else {
			return accessToken;
		}
	}
	
}
