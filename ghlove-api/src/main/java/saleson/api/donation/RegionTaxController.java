package saleson.api.donation;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import saleson.api.common.dto.CommonResponseDto;
import saleson.api.common.enumerated.DonationError;
import saleson.api.common.exception.DonationException;
import saleson.shop.donation.RegionTaxService;
import saleson.shop.donation.dto.CntrLmttRequestDto;
import saleson.shop.donation.dto.CntrLmttResponseDto;
import saleson.shop.donation.dto.DonationBugaResponseDto;
import saleson.shop.donation.dto.DonationSunapProcessRequestDto;
import saleson.shop.donation.dto.DonationSunapProcessResponseDto;
import saleson.shop.donation.dto.DonatorInfoRequestDto;
import saleson.shop.donation.dto.DonatorInfoResponseDto;
import saleson.shop.donation.dto.GiroPayRequestDto;
import saleson.shop.donation.dto.GiroPayResponseDto;
import saleson.shop.donation.dto.IntrstLocgovRequestDto;
import saleson.shop.donation.dto.IntrstLocgovResponseDto;
import saleson.shop.donation.dto.RegionBugaParamDto;
import saleson.shop.donation.dto.RsgstadresForeignRequestDto;
import saleson.shop.donation.dto.RsgstadresForeignResponseDto;
import saleson.shop.donation.dto.RsgstadresRequestDto;
import saleson.shop.donation.dto.RsgstadresResponseDto;

@RestController
@RequestMapping("/api/regiontax")
public class RegionTaxController {

	@Autowired
	private RegionTaxService regionTaxService;

	@ResponseBody
    @PostMapping("/bugaRequest")
    public CommonResponseDto<DonationBugaResponseDto> bugaRequest(@RequestBody RegionBugaParamDto regionBugaParamDto) {
		DonationBugaResponseDto response;
		try {
			response = regionTaxService.bugaRequest(regionBugaParamDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

	@ResponseBody
	@PostMapping("/mockBugaRequest")
    public CommonResponseDto<DonationBugaResponseDto> mockBugaRequest(@RequestBody RegionBugaParamDto regionBugaParamDto) {
		DonationBugaResponseDto response;
		try {
			response =  regionTaxService.mockBugaRequest(regionBugaParamDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

	@ResponseBody
	@GetMapping("/donatorInfo")
    public CommonResponseDto<DonatorInfoResponseDto> donatorInfo(DonatorInfoRequestDto donatorInfoRequestDto) {
		DonatorInfoResponseDto response;
		try {
			response =  regionTaxService.donatorInfo(donatorInfoRequestDto);
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
	@PostMapping("/sunapProcess")
    public CommonResponseDto<DonationSunapProcessResponseDto> sunapProcess(@RequestBody DonationSunapProcessRequestDto donationSunapProcessRequestDto) {
		DonationSunapProcessResponseDto response;
		try {
			response =  regionTaxService.sunapProcess(donationSunapProcessRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
    }

	@ResponseBody
	@PostMapping("/giroPay")
	public CommonResponseDto<GiroPayResponseDto> giroPay(@RequestBody GiroPayRequestDto giroPayRequestDto, HttpServletRequest request){
		GiroPayResponseDto response;
		String userAgent = request.getHeader("User-Agent").toUpperCase();
		giroPayRequestDto.setUserAgent(userAgent);

		try {
			response = regionTaxService.giroPay(giroPayRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
	}

	@ResponseBody
	@PostMapping("/locGovLmtt")	//getLocGovLmtt
	public CommonResponseDto<CntrLmttResponseDto> locGovLmtt(@RequestBody CntrLmttRequestDto cntrLmttRequestDto){
		CntrLmttResponseDto response = null;
		try {
			response = regionTaxService.locGovLmtt(cntrLmttRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
	}

	@ResponseBody
	@PostMapping("/userCntrInfo") //userCntrInfo
	public CommonResponseDto<DonatorInfoResponseDto> userCntrInfo() {
		DonatorInfoResponseDto response;
		DonatorInfoRequestDto donatorInfoRequestDto = new DonatorInfoRequestDto();
		try {
			response = regionTaxService.userCntrInfo(donatorInfoRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
	}

	@ResponseBody
	@PostMapping("/getPublicKey") //getPublicKey
	public CommonResponseDto<String> getPublicKey() {
		String response;
		try {
			response = regionTaxService.getPublicKey();
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
	}

	@ResponseBody
	@PostMapping("/rsgstadresinfo")
	public CommonResponseDto<RsgstadresResponseDto> rsgstadresinfo(@RequestBody RsgstadresRequestDto rsgstadresRequestDto) {
		RsgstadresResponseDto response;
		try {
			response = regionTaxService.rsgstadresInfo(rsgstadresRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
	}

	@ResponseBody
	@PostMapping("/mockRsgstadres")
	public CommonResponseDto<RsgstadresResponseDto> mockRsgstadres(@RequestBody RsgstadresRequestDto rsgstadresRequestDto) {
		RsgstadresResponseDto response;
		try {
			response = regionTaxService.mockRsgstadres(rsgstadresRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
		return CommonResponseDto.ofSuccess(response);
	}

	@ResponseBody
	@PostMapping("/rsgstadresinfoForeigner")
	public CommonResponseDto<RsgstadresForeignResponseDto> rsgstadresinfoForeigner(@RequestBody RsgstadresForeignRequestDto rsgstadresForeignRequestDto) {
		RsgstadresForeignResponseDto response;
		try {
			response = regionTaxService.rsgstadresInfoForeigner(rsgstadresForeignRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
        return CommonResponseDto.ofSuccess(response);
	}

	@ResponseBody
	@PostMapping("/setIntrstLocgov")
	public CommonResponseDto<IntrstLocgovResponseDto> setIntrstLocgov(@RequestBody IntrstLocgovRequestDto intrstLocgovRequestDto) {
		IntrstLocgovResponseDto response;
		try {
			response = regionTaxService.setIntrstLocgov(intrstLocgovRequestDto);
		} catch (DonationException e) {
			return CommonResponseDto.ofFail(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return CommonResponseDto.ofFail(DonationError.SYSTEM_ERROR.name(), DonationError.SYSTEM_ERROR.getMessage());
		}
		return CommonResponseDto.ofSuccess(response);
	}
}
