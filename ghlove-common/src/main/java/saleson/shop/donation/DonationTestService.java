package saleson.shop.donation;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import saleson.common.utils.UserUtils;
import saleson.shop.donation.dto.DonationOverPaymentRequestDto;
import saleson.shop.donation.dto.DonationOverPaymentResponseDto;
import saleson.shop.donation.dto.DonationSunapProcessRequestDto;
import saleson.shop.donation.dto.DonationSunapProcessResponseDto;
import saleson.shop.donation.dto.DonatorInfoRequestDto;
import saleson.shop.donation.dto.DonatorInfoResponseDto;
import saleson.shop.donation.dto.RsgstadresRequestDto;
import saleson.shop.donation.dto.RsgstadresResponseDto;
import saleson.shop.donation.repository.DonationRepository;
import saleson.shop.donation.repository.DonatorRepository;
import saleson.shop.donation.repository.LocgovRepository;
import saleson.shop.user.domain.UserDetail;

@Slf4j
@Service
public class DonationTestService extends DonationService{


	public DonationTestService(DonatorRepository donatorRepository, DonationRepository donationRepository,
			LocgovRepository locgovRepository) {
		super(donatorRepository, donationRepository, locgovRepository);
		// TODO Auto-generated constructor stub
	}

	public RsgstadresResponseDto mockRsgstadresResponseDto(RsgstadresRequestDto rsgstadresRequestDto) throws Exception {

		return mockRsgstadres(rsgstadresRequestDto);
	}

	/**
	 * <pre>
	 * comment       : 수납 처리
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 5.
	 *
	 * </pre>
	 * @param donationSunapProcessRequestDto
	 * @return
	 * DonationSunapProcessResponseDto
	 */
	public DonationSunapProcessResponseDto sunapProcessTest(DonationSunapProcessRequestDto donationSunapProcessRequestDto) {
		var sunapProcess = sunapProcess(donationSunapProcessRequestDto);
		return sunapProcess;
	}

	/**
	 * <pre>
	 * comment       : 과오납 처리
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 5.
	 *
	 * </pre>
	 * @param donationOverPaymentRequestDto
	 * @return
	 * DonationOverPaymentResponseDto
	 */
	public DonationOverPaymentResponseDto overPayment(DonationOverPaymentRequestDto donationOverPaymentRequestDto) {

		return DonationOverPaymentResponseDto.builder().build();
	}

	/**
	 * <pre>
	 * comment       : 기부자 정보 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 8.
	 *
	 * </pre>
	 * @param donatorRequestDto
	 * @return
	 * DonatorInfoResponseDto
	 * @throws Exception
	 */
	public DonatorInfoResponseDto donatorInfoTest(DonatorInfoRequestDto donatorInfoRequestDto) throws Exception {

		UserDetail userDetail = UserUtils.getUserDetail();
		donatorInfoRequestDto.setUserId(userDetail.getUserId());
		return donatorInfo(donatorInfoRequestDto);
	}

}
