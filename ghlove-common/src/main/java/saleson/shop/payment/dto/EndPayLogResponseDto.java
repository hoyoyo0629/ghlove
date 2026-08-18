package saleson.shop.payment.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import saleson.shop.payment.enumeration.PaymentProcess;

@Getter
@Builder
@ToString
public class EndPayLogResponseDto {

	public Long payLogId;
	public PaymentProcess payProcess;
	public LocalDateTime payEndDt;

}

