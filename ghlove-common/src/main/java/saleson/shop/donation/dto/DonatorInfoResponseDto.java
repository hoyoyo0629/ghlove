package saleson.shop.donation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DonatorInfoResponseDto {
	private Long userId;
	private String address;
	private String addressDetail;
	private String phoneNumber;
	private String birthday;
	private String birthdayType;
	private String loginPathCode;
	private String email;
	private String userName;
	private String loginId;
	private Long userCntrLimitAmt;
	private Boolean isMberCi;


}
