package saleson.shop.kakaolink.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoToolkit {
	
	private String status;
	
	private KakaoToolkitData data;
}
