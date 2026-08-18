package saleson.shop.donation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import saleson.common.enumeration.SmsType;
import saleson.common.sms.SmsIpsService;
import saleson.common.utils.UserUtils;
import saleson.model.UserEntity;
import saleson.shop.code.domain.Code;
import saleson.shop.donation.domain.CntrLmtt;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.donation.domain.HonorCntr;
import saleson.shop.donation.entity.GCntrEntity;
import saleson.shop.donation.repository.DonationRepository;
import saleson.shop.donation.support.DonationParam;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.donation.support.SidoListParam;
import saleson.shop.log.support.GifSeoulParam;
import saleson.shop.mypage.domain.Cntr;
import saleson.shop.mypage.support.CntrParam;
import saleson.shop.user.UserRepository;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.UserDetail;

@Service("ngDonationService")
@RequiredArgsConstructor
public class NgDonationServiceImpl extends EgovAbstractServiceImpl  implements NgDonationService {

	private static final Logger logger = LoggerFactory.getLogger(NgDonationServiceImpl.class);

	@Autowired
	private NgDonationMapper ngDonationMapper;

	@Autowired
	private SmsIpsService smsIpsService;

	@Autowired
	private DonationRepository donationRepository;

	@Autowired
	private DonationVerification donationVerification;

	@Autowired
	UserRepository userRepository;

	@Override
	public Map<String, Object> getUserCntrInfo() throws Exception {
		Map<String, Object> SERVICE_RESULT  = new HashMap<String, Object>();

		Map<String, Object> resultMap = new HashMap<>();

		UserDetail userDetail = UserUtils.getUserDetail();

        if (userDetail.getPhoneNumber() != null) {
            if ("--".equals(userDetail.getPhoneNumber())) {
                userDetail.setPhoneNumber("");
            }
            if ("--".equals(userDetail.getTelNumber())) {
                userDetail.setTelNumber("");
            }
        }

        userDetail = ngDonationMapper.getUserDetail(userDetail.getUserId());
        Long userId = userDetail.getUserId();
        int getGCntrSumCntrAmt = ngDonationMapper.getGCntrSumCntrAmt(userId);
        int getGMberSecsnSumCntrAmt = ngDonationMapper.getGMberSecsnSumCntrAmt(userDetail.getMberCi());

        Code limitAmtInfo = donationVerification.donationLimitAmt();

		Integer limitAmt =  Integer.parseInt(limitAmtInfo.getLabel());
		// 연 최대 기부 한도 금액
		String limitAmtString = limitAmtInfo.getDetail();
		// 연 최대 기부 한도 금액 (한글 단위)

    	Integer userCntrLimitAmt = limitAmt - getGCntrSumCntrAmt - getGMberSecsnSumCntrAmt;
    	// 현재 기부 가능 한도금

        resultMap.put("userId", userDetail.getUserId());
        resultMap.put("loginId", UserUtils.getUser().getLoginId());
        resultMap.put("userName", UserUtils.getUser().getUserName());
        resultMap.put("email", UserUtils.getUser().getEmail());
        resultMap.put("phoneNumber", userDetail.getPhoneNumber());
        resultMap.put("address", userDetail.getAddress());
        resultMap.put("addressDetail", userDetail.getAddressDetail());
        resultMap.put("birthday", userDetail.getBirthday());
        resultMap.put("mberCiYn", userDetail.getMberCiYn());
        resultMap.put("limitAmtString", limitAmtString);
        resultMap.put("userCntrLimitAmt", userCntrLimitAmt);
        resultMap.put("birthdayType", userDetail.getBirthdayType());
        resultMap.put("loginPathCode", userDetail.getLoginPathCode());

		SERVICE_RESULT = resultMap;
		return SERVICE_RESULT;
	}

	@Override
	public Map<String, Object> getSidoList(DonationParam donationParam) throws Exception {
		Map<String, Object> SERVICE_RESULT = new HashMap<>();
		List<SidoListParam> sidoList = ngDonationMapper.getSidoList(donationParam);
		SERVICE_RESULT.put("sidoList", sidoList);
		return SERVICE_RESULT;
	}

	@Override
	public Map<String, Object> getSigunguList(DonationParam donationParam) throws Exception {
		Map<String, Object> SERVICE_RESULT = new HashMap<>();
		List<SidoListParam> sidoList = ngDonationMapper.getSigunguList(donationParam);
		SERVICE_RESULT.put("cityList", sidoList);
		return SERVICE_RESULT;
	}

	@Override
	public Map<String, Object> getLocGovInfo(SidoListParam sidoListParam) throws Exception {
		Map<String, Object> SERVICE_RESULT  = new HashMap<String, Object>();
		UserDetail userDetail = UserUtils.getUserDetail();

    	LocGovInfo locGovParam = new LocGovInfo();
    	locGovParam.setUserId(String.valueOf(userDetail.getUserId()));
    	locGovParam.setLocgovCode(sidoListParam.getCityCode());

		SERVICE_RESULT.put("locGovInfo", ngDonationMapper.getLocGovInfo(locGovParam));
		return SERVICE_RESULT;
	}

	@Override
	public Map<String, Object> getIntrstLocgovInfo(SidoListParam sidoListParam) {
		Map<String, Object> SERVICE_RESULT  = new HashMap<String, Object>();
		UserDetail userDetail = UserUtils.getUserDetail();
    	LocGovInfo locGovInfo = new LocGovInfo();
    	if (userDetail.getUserId() > 0) {
    		locGovInfo.setUserId(String.valueOf(userDetail.getUserId()));
    		locGovInfo.setLocgovCode(sidoListParam.getCityCode());
    		int intrstLocgovCnt = ngDonationMapper.getIntrstLocgovInfo(locGovInfo);
    		SERVICE_RESULT.put("intrstLocgovCnt", intrstLocgovCnt);
    	} else {
    		SERVICE_RESULT.put("intrstLocgovCnt", 0);
    	}
		return SERVICE_RESULT;
	}

	@Override
	public Map<String, Object> setIntrstLocgov(SidoListParam sidoListParam) {
		Map<String, Object> SERVICE_RESULT  = new HashMap<String, Object>();
		UserDetail userDetail = UserUtils.getUserDetail();

    	LocGovInfo locGovInfo = new LocGovInfo();
		locGovInfo.setUserId(String.valueOf(userDetail.getUserId()));
		locGovInfo.setLocgovCode(sidoListParam.getCityCode());

		ngDonationMapper.insertIntrstLocgovInfo(locGovInfo);
		SERVICE_RESULT.put("loginId", UserUtils.getLoginId());
		SERVICE_RESULT.put("userName", UserUtils.getUser().getUserName());
		return SERVICE_RESULT;
	}

	@Override
	public Map<String, Object> sunapSuccess(SeoulParam seoulParam) throws Exception {
		Map<String, Object> SERVICE_RESULT  = new HashMap<String, Object>();
		SERVICE_RESULT.put("sunap", "FAIL");

		Optional<GCntrEntity> cntrEntitiy = Optional.ofNullable(donationRepository.findByElctrnPayNoAndUserId(seoulParam.getEnapbuNo(), Long.parseLong(seoulParam.getUserId())));
		cntrEntitiy.ifPresent(entity -> {
			ngDonationMapper.sunapSuccess(seoulParam);
			SERVICE_RESULT.put("sunap", "SUCCESS");

			// 기부 감사 인사
			GiveUserSmsInfo info = ngDonationMapper.getSmsSendGiveUserInfo(seoulParam);
			smsIpsService.giveSendSms(Arrays.asList(info), SmsType.DONATION);
		});

		return SERVICE_RESULT;
	}

	@Override
	public Map<String, Object> sunapSuccessNoSms(SeoulParam seoulParam) throws Exception {
		Map<String, Object> SERVICE_RESULT  = new HashMap<String, Object>();
		int sunap = ngDonationMapper.sunapSuccess(seoulParam);
		if(sunap > 0) {
			SERVICE_RESULT.put("sunap", "SUCCESS");
		} else {
			SERVICE_RESULT.put("sunap", "FAIL");
		}
		return SERVICE_RESULT;
	}

	@Override
	public int insertHometaxGif(SeoulParam seoulParam) {
		return ngDonationMapper.insertHometaxGif(seoulParam);
	}

	@Override
	public int updateNtsStatus(SeoulParam seoulParam) {
		return ngDonationMapper.updateNtsStatus(seoulParam);
	}

	@Override
	public CntrLmtt getCntrLmtt(SidoListParam donationParam) {
		// TODO Auto-generated method stub
		return ngDonationMapper.getCntrLmtt(donationParam);
	}

	@Override
	public int insertHonorCntrbtr(String locgovCode, long userId) {
		int result = 0;
		boolean isSms = false;

		LocGovInfo param = new LocGovInfo();
		param.setLocgovCode(locgovCode);
		param.setUserId(String.valueOf(userId));

		HonorCntr honorCntr = ngDonationMapper.getHonorCntrLevel(param);

		if (honorCntr != null) {

			// 기존에 명예 기부 등록이 되어있지 않는데 명예기부조건을 충족한 경우
			if ("0".equals(honorCntr.getHonorCntrbtrLevelCode()) && !"0".equals(honorCntr.getCode())) {

				// 등록
				result = ngDonationMapper.insertHonorCntrbtr(honorCntr);
				isSms = true;

			// 기존에 등록되어있는 명예기부 조건이 현재와 다른 경우
			} else if (!"0".equals(honorCntr.getHonorCntrbtrLevelCode()) && !"0".equals(honorCntr.getCode()) && !honorCntr.isSame()) {

				// 수정
				result = ngDonationMapper.updateHonorCntrbtr(honorCntr);
				isSms = true;

			// 기존에 명예 기부 등록 되어있지만 현재 조건을 충족하지 않는 경우
			} else if (!"0".equals(honorCntr.getHonorCntrbtrLevelCode()) && "0".equals(honorCntr.getCode())) {

				// 삭제
				result = ngDonationMapper.deleteHonorCntrbtr(honorCntr);

			}

			// 명예기부자 등록 또는 수정인 경우 국민비서 SMS 발송
			if (isSms) {
				if(locgovCode.equals("50000")) {
					smsIpsService.giveSendSms(Arrays.asList(honorCntr.getSmsInfo()), SmsType.HONOR_DONATION);
				}
			}
		}

		return result;
	}

	@Override
	public int selectNextBugaLogCount(NextBugaRequestLogDto nextBugaRequestLogDto) {
		// TODO Auto-generated method stub
		return ngDonationMapper.selectNextBugaLogCount(nextBugaRequestLogDto);

	}

	@Override
	public List<NextBugaResponseLogDto> selectNextBugaLogList(NextBugaRequestLogDto nextBugaRequestLogDto) {
		// TODO Auto-generated method stub
		return ngDonationMapper.selectNextBugaLogList(nextBugaRequestLogDto);
	}

	@Override
	public int selectNextSunapLogCount(NextSunapRequestLogDto nextSunapRequestLogDto) {
		// TODO Auto-generated method stub
		return ngDonationMapper.selectNextSunapLogCount(nextSunapRequestLogDto);
	}

	@Override
	public List<NextSunapResponseLogDto> selectNextSunapLogList(NextSunapRequestLogDto nextSunapRequestLogDto) {
		// TODO Auto-generated method stub
		return ngDonationMapper.selectNextSunapLogList(nextSunapRequestLogDto);
	}

	@Override
	public int selectGifSeoulBugaCount(GifSeoulParam gifSeoulParam) {
		// TODO Auto-generated method stub
		return ngDonationMapper.selectGifSeoulSunapCount(gifSeoulParam);
	}

	@Override
	public List<GifSeoulParam> selectGifSeoulBugaList(GifSeoulParam gifSeoulParam) {
		// TODO Auto-generated method stub
		return ngDonationMapper.selectGifSeoulBugaList(gifSeoulParam);
	}

	@Override
	public int selectGifSeoulSunapCount(GifSeoulParam gifSeoulParam) {
		// TODO Auto-generated method stub
		return ngDonationMapper.selectGifSeoulSunapCount(gifSeoulParam);
	}

	@Override
	public List<GifSeoulParam> selectGifSeoulSunapList(GifSeoulParam gifSeoulParam) {
		// TODO Auto-generated method stub
		return ngDonationMapper.selectGifSeoulSunapList(gifSeoulParam);
	}

	@Override
	public List<HashMap<String, Object>> selectDeleteCntrReSunapList(HashMap<String, Object> hashMap) {
		// TODO Auto-generated method stub
		return ngDonationMapper.selectDeleteCntrReSunapList(hashMap);
	}

	@Override
	public int selectTodayCntrSigunguCnt(SidoListParam sidoListParam ) {
		// TODO Auto-generated method stub
		UserDetail userDetail = UserUtils.getUserDetail();

    	LocGovInfo locGovParam = new LocGovInfo();
    	locGovParam.setUserId(String.valueOf(userDetail.getUserId()));
    	locGovParam.setLocgovCode(sidoListParam.getCityCode());
		return ngDonationMapper.selectTodayCntrSigunguCnt(locGovParam);
	}

	@Override
	public List<Cntr> getTodayCntrListInfo(CntrParam cntrParam) {
		return ngDonationMapper.getTodayCntrListInfo(cntrParam);
	}

}
