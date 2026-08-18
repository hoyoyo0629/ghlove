package saleson.shop.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import saleson.shop.payment.enumeration.PaymentMethod;
import saleson.shop.payment.enumeration.PaymentProcess;

@Getter
@Setter
@ToString
public class StartPayLogRequestDto {

	public String elctrnPayNo;
	public PaymentProcess payProcess;
	public PaymentMethod payMethod;

}

