package saleson.api.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import saleson.api.common.dto.CommonResponseDto;
import saleson.api.common.enumerated.DonationError;
import saleson.api.common.exception.DonationException;
import saleson.shop.payment.dto.EndPayLogRequestDto;
import saleson.shop.payment.dto.EndPayLogResponseDto;
import saleson.shop.payment.dto.StartPayLogRequestDto;
import saleson.shop.payment.dto.StartPayLogResponseDto;
import saleson.shop.payment.service.PayLogService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

	@Autowired
	private PayLogService payLogService;

	@ResponseBody
    @PostMapping("/start-pay-log")
    public CommonResponseDto<StartPayLogResponseDto> startPayLog(@RequestBody StartPayLogRequestDto startPayLogRequestDto) {
		StartPayLogResponseDto response;
		try {
			response = payLogService.startPayLog(startPayLogRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

	@ResponseBody
    @PostMapping("/end-pay-log")
    public CommonResponseDto<EndPayLogResponseDto> endPayLog(@RequestBody EndPayLogRequestDto endPayLogRequestDto) {
		EndPayLogResponseDto response;
		try {
			response = payLogService.endPayLog(endPayLogRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }
}
