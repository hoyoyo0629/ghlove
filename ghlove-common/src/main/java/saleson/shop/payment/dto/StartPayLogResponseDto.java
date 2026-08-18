package saleson.shop.payment.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import saleson.shop.payment.enumeration.PaymentMethod;
import saleson.shop.payment.enumeration.PaymentProcess;

@Getter
@Builder
@ToString
public class StartPayLogResponseDto {

	public long payLogId;
	public String elctrnPayNo;
	public PaymentMethod payMethod;
	public PaymentProcess payProcess;
	public LocalDateTime payStartDt;

}

