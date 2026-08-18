//package saleson.shop.donation;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import saleson.shop.donation.dto.DonationSunapProcessRequestDto;
//import saleson.shop.donation.repository.DonationRepository;
//import saleson.shop.donation.repository.DonatorRepository;
//import saleson.shop.donation.repository.LocgovRepository;
//
//@SpringBootTest
//@ActiveProfiles("local")
//public class DonationServiceTest {
//
//	@Mock
//	private DonationRepository donationRepository;
//
//	@Mock
//	private DonatorRepository donatorRepository;
//
//	@Mock
//	private LocgovRepository locgovRepository;
//
//	private DonationService donationService;
//
//
//	@BeforeEach
//	void setUp() {
//		this.donationService = new DonationService(donatorRepository, donationRepository, locgovRepository);
//	}
//
//	@Test
//	public void 수납확인() {
////		DonationSunapProcessRequestDto donationSunapProcessRequestDto = new DonationSunapProcessRequestDto();
////
////		donationSunapProcessRequestDto.setCntrSn("2023010148850000002");
////
////		var result = donationService.sunapProcess(donationSunapProcessRequestDto);
//////		UserDetail userDetail = UserUtils.getUserDetail();
//////		String today = DateUtils.getToday("yyyyMMdd");
//////		BigDecimal pointRate = new BigDecimal("30");
//////		Long zeroPoint = Long.valueOf(0);
//////
//////		var gCntrInfo = donationRepository.findById(donationSunapProcessRequestDto.getCntrSn()).orElseThrow(() -> new DonationException(DonationError.DONATION_INFORMATION_DOES_NOT_EXIST));
//////		Optional<GCtbnySetupEntity> optional =Optional.ofNullable(donationRepository.findByStdrYearAndLocgovCode(today.substring(0, 4), gCntrInfo.getCntrLocgovCode()));
//////
//////		if(optional.isPresent()) {
//////			pointRate = optional.get().getPointRate();
//////		}
//////
//////		String pointEndDe = String.valueOf(Integer.parseInt(gCntrInfo.getCntrDe().substring(0, 4))+ 5) + gCntrInfo.getCntrDe().substring(4, 6) + gCntrInfo.getCntrDe().substring(6, 8);
////
////		assertThat(result.getGCntr().getCntrPoint()== 3000);
//
//	}
//
//}
