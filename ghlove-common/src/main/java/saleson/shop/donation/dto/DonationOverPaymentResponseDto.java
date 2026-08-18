package saleson.shop.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import saleson.shop.donation.entity.GCntrEntity;

@Getter
@Builder
@ToString
public class DonationOverPaymentResponseDto {
	GCntrEntity gCntr;
}
