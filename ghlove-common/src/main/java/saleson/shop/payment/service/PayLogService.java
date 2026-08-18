package saleson.shop.payment.service;

import saleson.api.common.exception.DonationException;
import saleson.shop.payment.dto.EndPayLogRequestDto;
import saleson.shop.payment.dto.EndPayLogResponseDto;
import saleson.shop.payment.dto.StartPayLogRequestDto;
import saleson.shop.payment.dto.StartPayLogResponseDto;

public interface PayLogService {

	public StartPayLogResponseDto startPayLog(StartPayLogRequestDto payLogRequestDto) throws DonationException;

	public EndPayLogResponseDto endPayLog(EndPayLogRequestDto payLogRequestDto) throws DonationException;
}
