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

import lombok.extern.slf4j.Slf4j;
import saleson.api.common.dto.CommonResponseDto;
import saleson.api.common.enumerated.DonationError;
import saleson.api.common.exception.DonationException;
import saleson.shop.donation.SeoulTaxService;
import saleson.shop.donation.dto.DonationBugaResponseDto;
import saleson.shop.donation.dto.DonationSunapProcessRequestDto;
import saleson.shop.donation.dto.DonationSunapProcessResponseDto;
import saleson.shop.donation.dto.DonatorInfoResponseDto;
import saleson.shop.donation.dto.SeoulParamDto;
import saleson.shop.donation.dto.SeoulSunapResponseDto;

@Slf4j
@RestController
@RequestMapping("/api/seoultax")
public class SeoulTaxController {

	@Autowired
	private SeoulTaxService seoulTaxService;

	@ResponseBody
    @PostMapping("/bugaRequest")
	public CommonResponseDto<DonationBugaResponseDto> bugaRequest(@RequestBody SeoulParamDto seoulParamDto) {
		DonationBugaResponseDto response = null;
		try {
			response =  seoulTaxService.bugaRequest(seoulParamDto);
		} catch (DonationException e) {
			log.error(e.getMessage());
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

	@ResponseBody
	@PatchMapping("/sunapProcess")
    public CommonResponseDto<DonationSunapProcessResponseDto> sunapProcess(@RequestBody DonationSunapProcessRequestDto donationSunapProcessRequestDto) {
		DonationSunapProcessResponseDto response;
		try {
			//화면단 enapbuNo → elctrnPayNo 변경필요 또는 Dto에 추가 필요!
			response =  seoulTaxService.seoulSunapProcess(donationSunapProcessRequestDto);
		} catch (DonationException e) {
			log.error(e.getMessage());
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

	@ResponseBody
	@GetMapping("/etaxSunapInfo")
    public CommonResponseDto<SeoulSunapResponseDto> etaxSunapInfo(@RequestBody SeoulParamDto seoulParamDto) {
		SeoulSunapResponseDto response;
		try {
			response =  seoulTaxService.etaxSunapInfo(seoulParamDto);
		} catch (DonationException e) {
			log.error(e.getMessage());
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
	}

	@ResponseBody
	@GetMapping("/donatorInfo")
    public CommonResponseDto<DonatorInfoResponseDto> donatorInfo() {
		DonatorInfoResponseDto response;
		try {
			response =  seoulTaxService.donatorInfo();
		} catch (DonationException e) {
			log.error(e.getMessage());
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return CommonResponseDto.ofFail(DonationError.UNSUPPORTED_ENCODING_EXCEPTION.name(), DonationError.UNSUPPORTED_ENCODING_EXCEPTION.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage());
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

}
