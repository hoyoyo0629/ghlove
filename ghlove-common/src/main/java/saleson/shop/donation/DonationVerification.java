package saleson.shop.donation;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import saleson.model.UserEntity;
import saleson.shop.code.CodeMapper;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.donation.repository.DonationRepository;
import saleson.shop.user.UserRepository;

@Component
@RequiredArgsConstructor
public class DonationVerification {

	@Autowired
	private NgDonationMapper ngDonationMapper;

	@Autowired
	private DonationRepository donationRepository;

	@Autowired
	private CodeMapper codeMapper;

	@Autowired
	UserRepository userRepository;

	public boolean isDonationNormalAmount(Long userId, Long amount) throws UnsupportedEncodingException {
		Integer getGCntrSumCntrAmt = 0;
		Integer getGMberSecsnSumCntrAmt = 0;
    	Integer limitAmt = Integer.parseInt(donationLimitAmt().getLabel());
    	Integer getGcntrWegiveCntrAmt = 0;

    	if (userId != null) {
    		UserEntity userEntity = userRepository.findByUserId(userId);
    		getGCntrSumCntrAmt = ngDonationMapper.getGCntrSumCntrAmt(userId);
    		getGMberSecsnSumCntrAmt = ngDonationMapper.getGMberSecsnSumCntrAmt(userEntity.getMberCi());
    		getGcntrWegiveCntrAmt = donationRepository.sumWegiveCntrAmtByWegiveAmtAndMberCi(userEntity.getMberCi());
    	}


    	Integer maxCntrAmt =  limitAmt - getGCntrSumCntrAmt - getGMberSecsnSumCntrAmt - getGcntrWegiveCntrAmt;

		if(amount % 100 != 0 || amount < 0 || amount.intValue() >  maxCntrAmt.intValue()) {
			return false;
		}

		return true;
	}

	public Code donationLimitAmt() {
		Map<String, Object> params = new HashMap<String, Object>();
		Code result = new Code();
		LocalDate localDate = LocalDate.now();
		String currentYear = String.valueOf(localDate.getYear());
//		currentYear = "2026";

		params.put("codeType" ,"DONATION_LIMIT_AMT");
		params.put("id", currentYear);
//		if(codeMapper.getCodeList(codeParam).size() > 0) {
//			result = codeMapper.getCodeList(codeParam).get(0);
//		}

		if(codeMapper.getCodeById(params) != null) {
			result = codeMapper.getCodeById(params);
		}
		return result;
	}

}
