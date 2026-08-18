package saleson.common.sms;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import saleson.common.enumeration.SmsType;
import saleson.common.sms.domain.GiveTifIpsSndngM;
import saleson.common.sms.domain.HomeTownDayInfo;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.sms.domain.SmsIpsParam;
import saleson.common.sms.domain.TifIpsSndngM;
import saleson.common.sms.domain.TifIpsSndngMDisplay;
import saleson.shop.code.CodeMapper;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.donation.DonationVerification;
import saleson.shop.donation.domain.GiveUserSmsInfo;

@Service("smsIpsService")
public class SmsIpsServiceImpl implements SmsIpsService{

	private Logger log = LoggerFactory.getLogger(SmsIpsServiceImpl.class);

	@Autowired
	DonationVerification donationVerification;

	@Autowired
	private SmsMapper smsMapper;

	@Autowired
	private CodeMapper codeMapper;


//	@Autowired
//	private SequenceService sequenceService;

	// 서비스 그룹아이디
	@Value("${sms.ips.svcGrpId}")
	String smsIpsSvcGrpId;

	// 인터페이스 아이디
	@Value("${sms.ips.esbIfId}")
	String smsIpsEsbIfId;

	// 개인정보 식별 구분 코드
	@Value("${sms.ips.prvcIdntfcSeCd}")
	String smsIpsPrvcIdntfcSeCd;

	/**
	 * 국민비서발송기본 등록
	 * @param smsType		sms타입
	 * @param receiverInfo	수신정보
	 */
	@Override
	public void insertTifIpsSndngM(List<ReceiverInfo> receiverInfo) {
		if (receiverInfo == null || receiverInfo.isEmpty()) {
			return;
		}

		TifIpsSndngM tifIpsSndngM = new TifIpsSndngM();
		this.initTifIpsSndngM(tifIpsSndngM);

		for (ReceiverInfo info : receiverInfo) {
			try {
				if (!StringUtils.hasLength(info.getSndngCntnts())
						|| !StringUtils.hasLength(info.getPrvcIdntfcInfo())
						|| info.getSmsType() == null) {
					String smsTypeStr = "";
					if (info.getSmsType() != null) {
						smsTypeStr = info.getSmsType().toString();
					}
					log.error("send sms fail :: have not info :: smsType :: " + smsTypeStr + ", prvcIdntfcSeCd :: " +  info.getPrvcIdntfcInfo() + ", sndngCntnts :: " + info.getSndngCntnts());
					continue;
				}
				// 수신대상 List
//				tifIpsSndngM.setListSn(++cnt);									// 목록일련번호
				long insttCrtSn = smsMapper.getInsttCrtSn();
				tifIpsSndngM.setListSn(insttCrtSn);
				tifIpsSndngM.setInsttCrtSn(insttCrtSn);							// 기관생성일련번호
				tifIpsSndngM.setPrvcIdntfcInfo(info.getPrvcIdntfcInfo());		// 개인정보식별정보 CI
				tifIpsSndngM.setSndngCntnts(info.getSndngCntnts());				// 발송내용( 구분자 | )
//				tifIpsSndngM.setImg1UrlInfo(info.getImg1UrlInfo());				// 이미지1URL정보
//				tifIpsSndngM.setImg2UrlInfo(info.getImg2UrlInfo());				// 이미지2URL정보
//				tifIpsSndngM.setImg3UrlInfo(info.getImg3UrlInfo());				// 이미지3URL정보
				tifIpsSndngM.setSvcId(info.getSmsType().getCode());				// 서비스 아이디

				smsMapper.insertTifIpsSndngM(tifIpsSndngM);
			} catch (NullPointerException | DataAccessException e) {
				String smsTypeStr = "";
				if (info.getSmsType() != null) {
					smsTypeStr = info.getSmsType().toString();
				}
				log.error("send sms fail :: error :: smsType :: " + smsTypeStr + ", prvcIdntfcSeCd :: " +  info.getPrvcIdntfcInfo() + ", sndngCntnts :: " + info.getSndngCntnts(), e);
			}
		}


	}

	@Override
	public void giveSendSms(List<GiveUserSmsInfo> infoList, SmsType type) {

		if (infoList == null || infoList.isEmpty()) {
			return;
		}

		List<ReceiverInfo> list = new ArrayList<ReceiverInfo>();

		for (GiveUserSmsInfo info : infoList) {
			// TODO Auto-generated method stub
			if (info.getReceiveSms() != null && "0".equals(info.getReceiveSms())) {
				ReceiverInfo receiverInfo = info.getReceiverInfo(type);

				if (receiverInfo != null ) {
					list.add(receiverInfo);
				} else {
					log.error("사용자 ID : " + info.getUserId() + " " + type.getTitle() + " SMS 전송 실패");
				}

			} else {
				log.error("사용자 ID : " + info.getUserId() + " SMS 수신 미동의로 전송 실패");
			}
		}

		if (list.size() > 0) this.insertTifIpsSndngM(list);
	}

	@Override
	public void sendTaxCreditInfo(String year) {
		// TODO Auto-generated method stub
		TifIpsSndngM tifIpsSndngM = new GiveTifIpsSndngM();
		this.initTifIpsSndngM(tifIpsSndngM);
		tifIpsSndngM.setSvcId(SmsType.TAX_CREDIT.getCode());

		tifIpsSndngM.setTaxYear(year);

		smsMapper.insertTifIpsSndngMTaxCreditInfo(tifIpsSndngM);
		smsMapper.updateOpSequence();
	}

	@Override
	public void sendHometownDayInfo(HomeTownDayInfo info) {

		// TODO Auto-generated method stub
		TifIpsSndngM tifIpsSndngM = new GiveTifIpsSndngM();
		this.initTifIpsSndngM(tifIpsSndngM);
		tifIpsSndngM.setSvcId(SmsType.ILOVEGOHYANG_DAY.getCode());

		if (info.getParseDate() != null &&
			info.getLocation() != null && !"".equals(info.getLocation()) &&
			info.getPartyList() != null && !"".equals(info.getPartyList())) {

			SimpleDateFormat sdf = new SimpleDateFormat("yy. M. d.(E) a h:mm");
			String data = sdf.format(info.getParseDate()) + " / " + info.getLocation() + "|" + info.getPartyList();
			tifIpsSndngM.setSndngCntnts(data);


			smsMapper.insertTifIpsSndngMHometownDayInfo(tifIpsSndngM);
			smsMapper.updateOpSequence();
		}

	}

	private void initTifIpsSndngM(TifIpsSndngM tifIpsSndngM) {
		LocalDateTime insertLd = LocalDateTime.now().withNano(0);

		tifIpsSndngM.setInfoCrtDt(Timestamp.valueOf(insertLd));			// 정보생성일시(공통)
		tifIpsSndngM.setSvcGrpId(smsIpsSvcGrpId);						// 서비스그룹아이디(공통)
		tifIpsSndngM.setPrvcIdntfcSeCd(smsIpsPrvcIdntfcSeCd);			// 개인정보식별구분코드(공통) (C0090001 : CI정보, C0090002 : 주민등록번호, C0090003 : 이름+전화번호 해시)
		tifIpsSndngM.setEsbStatusCd("N");								// 연계처리상태코드(공통)
		tifIpsSndngM.setEsbWorkGbn("I");								// 작업 구분 코드(공통)
		tifIpsSndngM.setEsbIfId(smsIpsEsbIfId);							// 인터페이스 아이디(공통)
	}

	@Override
	public List<TifIpsSndngMDisplay> getSmsSendList(SmsIpsParam searchParam) {

		List<TifIpsSndngM> list = smsMapper.getSmsSendList(searchParam);
//		List<TifIpsSndngM> list = smsMapper.getSmsSendList();

		List<TifIpsSndngMDisplay> result = new ArrayList<>();

		if (list != null && !list.isEmpty()) {
			for (TifIpsSndngM tifIpsSndngM : list) {
				TifIpsSndngMDisplay tifIpsSndngMDisplay = new TifIpsSndngMDisplay();
				tifIpsSndngMDisplay.setInsttCrtSn(String.valueOf(tifIpsSndngM.getInsttCrtSn()));
				tifIpsSndngMDisplay.setSvcId(tifIpsSndngM.getSvcId());
				tifIpsSndngMDisplay.setLimitAmtString(donationVerification.donationLimitAmt().getDetail());
				tifIpsSndngMDisplay.setSndngCntnts(tifIpsSndngM.getSndngCntnts());
				tifIpsSndngMDisplay.setEsbInitTime(tifIpsSndngM.getEsbInitTime());
				tifIpsSndngMDisplay.setEsbTxTime(tifIpsSndngM.getEsbTxTime());
				tifIpsSndngMDisplay.setEsbComptTime(tifIpsSndngM.getEsbComptTime());
				tifIpsSndngMDisplay.setEsbStatusCd(tifIpsSndngM.getEsbStatusCd());
				tifIpsSndngMDisplay.setEsbErrMsg(tifIpsSndngM.getEsbErrMsg());

				result.add(tifIpsSndngMDisplay);
			}
		}

		return result;
	}

	@Override
	public int getSmsSendCnt(SmsIpsParam searchParam) {
		return smsMapper.getSmsSendCnt(searchParam);
	}

	@Override
	public String donationLimitAmtString() {
		CodeParam codeParam = new CodeParam();
		Code result = new Code();
		String limitAmtString = null;
		LocalDate localDate = LocalDate.now();
		String currentYear = String.valueOf(localDate.getYear());

		codeParam.setCodeType("DONATION_LIMIT_AMT");
		codeParam.setId(currentYear);
		if(codeMapper.getCodeList(codeParam).size() > 0) {
			result = codeMapper.getCodeList(codeParam).get(0);
			limitAmtString = result.getDetail();
		}
		return limitAmtString;
	}



}
