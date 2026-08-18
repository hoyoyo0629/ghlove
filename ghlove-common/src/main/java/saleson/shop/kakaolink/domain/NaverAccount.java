package saleson.shop.kakaolink.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.onlinepowers.framework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverAccount {
	
	// 카카오 아이디(시퀀스)
	private String id;

	// 이름
	private String name;

	// 이메일
	private String email;
	
	// 전화번호
	@JsonProperty("phone")
	private String phoneNumber;
	
	// 생년월일
	private String birthday;
	
	// 양력/음력
	@JsonProperty("birthday_type")
	private String birthdayType;

	private String gender;
	
}
