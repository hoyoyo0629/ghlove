package saleson.shop.report.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CallFindResponseDto {

	public String callDate;
	public Long callKookmin;
	public Long callLov;
	public Long callGiver;
	public Long callNhbank;
	public Long callPlatform;

}

