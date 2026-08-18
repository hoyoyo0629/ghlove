package saleson.shop.qustnr.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QestnarResponseDto {

	private Qestnar qestnar;
	private String resultCode;
	private String resultMsg;
	private long rspnsCount; // 응답 결과수


}