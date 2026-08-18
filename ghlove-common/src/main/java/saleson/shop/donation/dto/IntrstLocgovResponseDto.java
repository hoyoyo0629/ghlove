package saleson.shop.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class IntrstLocgovResponseDto {
	private String loginId;
	private String userName;
}