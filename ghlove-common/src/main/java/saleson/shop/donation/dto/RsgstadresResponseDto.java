package saleson.shop.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RsgstadresResponseDto {

	private String mapLocgov;
	private String bassAdres;
	private String juso;
	private String serviceResult;
}
