package saleson.api.donation;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import saleson.api.common.dto.CommonResponseDto;
import saleson.api.common.enumerated.DonationError;
import saleson.api.common.exception.DonationException;
import saleson.shop.donation.DonationTestService;
import saleson.shop.donation.dto.DonationSunapProcessRequestDto;
import saleson.shop.donation.dto.DonationSunapProcessResponseDto;
import saleson.shop.donation.dto.DonatorInfoRequestDto;
import saleson.shop.donation.dto.DonatorInfoResponseDto;
import saleson.shop.donation.dto.RsgstadresRequestDto;
import saleson.shop.donation.dto.RsgstadresResponseDto;

@RestController
@RequestMapping("/api/donationTest")
public class DonationTestController {

	@Autowired
	private DonationTestService donationTestService;

	@ResponseBody
	@PostMapping("/mockRsgstadres")
    public CommonResponseDto<RsgstadresResponseDto> mockRsgstadres(@RequestBody RsgstadresRequestDto rsgstadresRequestDto) {
		RsgstadresResponseDto response;
		try {
			response =  donationTestService.mockRsgstadres(rsgstadresRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

	@ResponseBody
	@GetMapping("/donatorInfo")
    public CommonResponseDto<DonatorInfoResponseDto> donatorInfoTest(DonatorInfoRequestDto donatorInfoRequestDto) {
		DonatorInfoResponseDto response;
		try {
			response =  donationTestService.donatorInfo(donatorInfoRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (UnsupportedEncodingException e) {
			return CommonResponseDto.ofFail(DonationError.UNSUPPORTED_ENCODING_EXCEPTION.name(), DonationError.UNSUPPORTED_ENCODING_EXCEPTION.getMessage());
		}catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

	@ResponseBody
	@PatchMapping("/sunapProcess")
    public CommonResponseDto<DonationSunapProcessResponseDto> sunapProcessTest(@RequestBody DonationSunapProcessRequestDto donationSunapProcessRequestDto) {
		DonationSunapProcessResponseDto response;
		try {
			response =  donationTestService.sunapProcess(donationSunapProcessRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

	@ResponseBody
	@PostMapping("/rsgstadres")
    public CommonResponseDto<RsgstadresResponseDto> rsgstadres(@RequestBody RsgstadresRequestDto rsgstadresRequestDto) {
		RsgstadresResponseDto response;
		try {
			response =  donationTestService.rsgstadres(rsgstadresRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

}
