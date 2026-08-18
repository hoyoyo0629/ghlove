package saleson.shop.payment.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import saleson.shop.payment.enumeration.PaymentProcess;

@Getter
@Setter
@ToString
public class EndPayLogRequestDto {

	public long payLogId;
	public PaymentProcess payProcess;
	public LocalDateTime payEndDt;
	public long userId;

}

