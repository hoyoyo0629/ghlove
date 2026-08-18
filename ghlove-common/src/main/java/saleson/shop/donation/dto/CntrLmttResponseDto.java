package saleson.shop.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class CntrLmttResponseDto {
	private String lmttBgnDe;
	private String lmttEndDe;
	private String violtResnCn;
}