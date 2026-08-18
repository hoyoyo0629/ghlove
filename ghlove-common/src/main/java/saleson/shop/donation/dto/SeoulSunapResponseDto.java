package saleson.shop.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SeoulSunapResponseDto {
	private String sunapYn;
	private Long sunapAmt;
	private String sunapDt;
	private String rstCd;
	private String rstMsg;
}
