package saleson.shop.donation.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DonatorInfoDto {
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
	private String mberCi;
	private Boolean isMberCi;

	public DonatorInfoDto(){

	}

	@QueryProjection
	public DonatorInfoDto(Long userId, String address, String addressDetail, String phoneNumber, String birthday,
			String birthdayType, String loginPathCode, String email, String userName, String loginId, String mberCi, Boolean isMberCi) {
		super();
		this.userId = userId;
		this.address = address;
		this.addressDetail = addressDetail;
		this.phoneNumber = phoneNumber;
		this.birthday = birthday;
		this.birthdayType = birthdayType;
		this.loginPathCode = loginPathCode;
		this.email = email;
		this.userName = userName;
		this.loginId = loginId;
		this.mberCi = mberCi;
		this.isMberCi = isMberCi;
	}
}
