package saleson.shop.payment.repository;

import saleson.shop.payment.dto.EndPayLogRequestDto;

public interface PaymentRepositoryCustom {

	long updateEndPayLog(EndPayLogRequestDto endPayLogRequestDto);
}
