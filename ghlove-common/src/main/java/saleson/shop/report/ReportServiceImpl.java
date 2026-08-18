package saleson.shop.report;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.util.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.shop.code.CodeMapper;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.donation.repository.DonationRepository;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.report.entity.GDailyReport;
import saleson.shop.report.entity.OpCall;
import saleson.shop.report.repository.CallRepository;
import saleson.shop.report.repository.CntrUsePointRepository;
import saleson.shop.report.repository.GDailyReportRepository;
import saleson.shop.report.repository.UserReportRepository;
import saleson.shop.slave.SlaveMainMapper;

@Slf4j
@Service("reportService")
@RequiredArgsConstructor
public class ReportServiceImpl extends EgovAbstractServiceImpl implements ReportService {

	@Autowired
	private DonationRepository donationRepository;

	@Autowired
	private CallRepository callRepository;

	@Autowired
	private CntrUsePointRepository cntrUsePointRepository;

	@Autowired
	private GDailyReportRepository gDailyReportRepository;

	@Autowired
	private UserReportRepository userReportRepository;

	@Autowired
	private CodeMapper codeMapper;

	@Autowired
	private SlaveMainMapper slaveMainMapper;

	@Override
	public Map<String, Object> dailyReportTotalStatistics(String searchDay) {
		Map<String, Object> result = new HashMap<>();
		result.put("donation", donationStatistics(searchDay));					//기부
		result.put("item", itemStatistics(searchDay));							//답례품
		result.put("customerCenter", customerCenterCallStatistics(searchDay));	//콜센터 현황
		result.put("user", userStatistics(searchDay));							//가입된 회원
		return result;
	}

	public Map<String, Object> userStatistics(String searchDay) {
		Map<String, Object> result = new HashMap<>();
		result.put("userCount", userReportRepository.countByStatusCodeAndCreatedDate(9, searchDay));

		return result;
	}

	public Map<String, Object> donationStatistics(String searchDay) {
		Map<String, Object> result = new HashMap<>();

//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//		LocalDate objYmd = LocalDate.parse(searchDay, formatter);
//		DayOfWeek dayOfWeek = objYmd.getDayOfWeek();
//		int dayOfWeekNumber = dayOfWeek.getValue();

//		LocalDate preYmd = dayOfWeekNumber == 1 ? preYmd = objYmd.minusDays(3) : objYmd.minusDays(1);

//		String preSearchDay = preYmd.format(formatter);

//		log.debug("======= preSearchDay : " +preSearchDay);
		Long totalCount = donationRepository.countByCntrDe(searchDay);
//		Long preTotalCount = donationRepository.countByCntrDe(preSearchDay);
//		Long increasedCount = totalCount - preTotalCount;
		Long increasedCount = donationRepository.countByCntrDeAndCurrentDate(searchDay); /* 2025-04-03 당일데이터 값 */
		Long onlineCount =  donationRepository.countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(searchDay, "100");
		Long offlineCount =  donationRepository.countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(searchDay, "200");
		Long onlineDsgnDntnBizCount =  donationRepository.countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNotNull(searchDay, "100");
		Long offlineDsgnDntnBizCount =  donationRepository.countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNotNull(searchDay, "200");



		Long totalCntrAmt = donationRepository.sumCntrAmtByCntrDe(searchDay);
//		Long preTotalCntrAmt = donationRepository.sumCntrAmtByCntrDe(preSearchDay);
//		Long increasedAmt = totalCntrAmt - preTotalCntrAmt;
		Long increasedAmt = donationRepository.sumCntrAmtByCntrDeAndCurrentDate(searchDay); /* 2025-04-03 당일데이터 값 */
		Long onlineCntrAmt =  donationRepository.sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(searchDay, "100");
		Long offlineCntrAmt =  donationRepository.sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(searchDay, "200");
		Long onlineDsgnDntnBizAmt =  donationRepository.sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdNotNull(searchDay, "100");
		Long offlineDsgnDntnBizAmt =  donationRepository.sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdNotNull(searchDay, "200");

		Long totalCountNotLinkInstt = donationRepository.countByCntrDeAndNotLinkInstt(searchDay);
		Long totalCntrAmtNotLinkInstt = donationRepository.sumCntrAmtByCntrDeAndNotLinkInstt(searchDay);

		/* 민간플랫폼 누적 합계 시작 */
		List<HashMap<String, Object>> linkInsttCumulativeList = new ArrayList<HashMap<String,Object>>();

		for(Code code : commonCodeForLinkInsttList()) {
			HashMap<String, Object> codeMap = new HashMap<>();
			Long linkInsttCnt = donationRepository.countByCntrDeAndLinkInsttCd(searchDay, code.getId());
			Long linkInsttAmt = donationRepository.sumCntrAmtByCntrDeAndLinkInsttCd(searchDay, code.getId());
			codeMap.put("linkInsttNm", code.getLabel());
			codeMap.put("linkInsttCnt", linkInsttCnt);
			codeMap.put("linkInsttAmt", linkInsttAmt);
			linkInsttCumulativeList.add(codeMap);
		}
		result.put("linkInsttCumulative", linkInsttCumulativeList);
		/* 민간플랫폼 누적 합계 종료 */


		/* 민간플랫폼 일일 합계 시작 */
		List<HashMap<String, Object>> linkCurrentList = new ArrayList<HashMap<String,Object>>();

		Long dayTotalCountNotLinkInstt = 0L;
		Long dayTotalCntrAmtNotLinkInstt = 0L;

		for(Code code : commonCodeForLinkInsttList()) {
			HashMap<String, Object> codeMap = new HashMap<>();
			Long linkInsttCnt = donationRepository.countByCntrDeAndLinkInsttCdAndCurrentDate(searchDay, code.getId());
			Long linkInsttAmt = donationRepository.sumCntrAmtByCntrDeAndLinkInsttCdAndCurrentDate(searchDay, code.getId());

			dayTotalCountNotLinkInstt += linkInsttCnt;
			dayTotalCntrAmtNotLinkInstt += linkInsttAmt;

			codeMap.put("linkInsttNm", code.getLabel());
			codeMap.put("linkInsttCnt", linkInsttCnt);
			codeMap.put("linkInsttAmt", linkInsttAmt);

			linkCurrentList.add(codeMap);
		}
		result.put("linkInsttCurrent", linkCurrentList);
		/* 민간플랫폼 일일 합계 종료 */

		result.put("totalCount", totalCount);
		result.put("increasedCount", increasedCount);
		result.put("onlineCount", onlineCount);
		result.put("offlineCount", offlineCount);
		result.put("onlineDsgnDntnBizCount", onlineDsgnDntnBizCount);
		result.put("offlineDsgnDntnBizCount", offlineDsgnDntnBizCount);

		result.put("totalCntrAmt", totalCntrAmt);
		result.put("increasedAmt", increasedAmt);
//		result.put("preTotalCntrAmt", preTotalCntrAmt);
		result.put("onlineCntrAmt", onlineCntrAmt);
		result.put("offlineCntrAmt", offlineCntrAmt);
		result.put("onlineDsgnDntnBizAmt", onlineDsgnDntnBizAmt);
		result.put("offlineDsgnDntnBizAmt", offlineDsgnDntnBizAmt);
		result.put("totalCountNotLinkInstt", totalCountNotLinkInstt);
		result.put("totalCntrAmtNotLinkInstt", totalCntrAmtNotLinkInstt);

		// 일일 기부 현황 (민간제외)
		result.put("increasedTotalCountNotLinkInstt", increasedCount - dayTotalCountNotLinkInstt);
		result.put("increasedTotalCntrAmtNotLinkInstt", increasedAmt - dayTotalCntrAmtNotLinkInstt);
		return result;
	}

	public Map<String, Object> itemStatistics(String searchDay) {
		Map<String, Object> result = new HashMap<>();

		Long orderCount = cntrUsePointRepository.countOrderCodeByPointUseDe(searchDay);
		Long usePointAmt = cntrUsePointRepository.sumCntrUsePointByPointUseDe(searchDay);

		GiveState giveState = new GiveState();
		giveState.setShCntrDeStart(searchDay);
		long giftB = slaveMainMapper.opmanageMainAmountGiftB(giveState);
		long giftA = slaveMainMapper.opmanageMainAmountGiftA(giveState);
		long giftC = giftA + giftB;

		result.put("orderCount", orderCount);
		result.put("usePointAmt", usePointAmt);
		result.put("itemRegCount", giftC);
		return result;
	}

	public OpCall customerCenterCallStatistics(String searchDay) {

		return callRepository.findByCallDate(searchDay);
	}

	@Transactional
	@Override
	public void saveDailyReport() throws JacksonException {
		String today = DateUtils.getToday("yyyyMMdd");
		Map<String, Object> statistics = dailyReportTotalStatistics(today);

		ObjectMapper mapper = new ObjectMapper();

		String resultData = mapper.writeValueAsString(statistics);

		GDailyReport report =GDailyReport.builder().reportDate(today).resultData(resultData).frstRegId(0L).frstRegDt(LocalDateTime.now()).build();

		gDailyReportRepository.save(report);

	}

	public List<Code> commonCodeForLinkInsttList() {
		CodeParam codeParam = new CodeParam();
		codeParam.setCodeType("LINK_INSTT_CD");
		return codeMapper.getCodeChildList(codeParam);
	}

	@Override
	public Map<String, Object> dayReportTotalStatistics(String startDay, String endDay) {
		Map<String, Object> result = new HashMap<>();
		result.put("donation", dayDonationStatistics(startDay, endDay));					//기간 안에 기부
		result.put("item", dayItemStatistics(startDay, endDay));							//기간안에 답례품 과 총답례품
		result.put("user", dayUserStatistics(startDay,endDay));							//기간안에 가입된 회원
		return result;
	}

	public Map<String, Object> dayDonationStatistics(String startDay, String endDay) {
		Map<String, Object> result = new HashMap<>();

		Long totalCount = donationRepository.countByCntrDe(endDay);
		Long increasedCount = donationRepository.countByCntrDeAndCurrentDate(startDay, endDay);
		Long onlineCount =  donationRepository.countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(endDay, "100");
		Long offlineCount =  donationRepository.countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(endDay, "200");
		Long onlineDsgnDntnBizCount =  donationRepository.countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNotNull(endDay, "100");
		Long offlineDsgnDntnBizCount =  donationRepository.countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNotNull(endDay, "200");


		Long totalCntrAmt = donationRepository.sumCntrAmtByCntrDe(endDay);
		Long increasedAmt = donationRepository.sumCntrAmtByCntrDeAndCurrentDate(startDay, endDay);
		Long onlineCntrAmt =  donationRepository.sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(endDay, "100");
		Long offlineCntrAmt =  donationRepository.sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(endDay, "200");
		Long onlineDsgnDntnBizAmt =  donationRepository.sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdNotNull(endDay, "100");
		Long offlineDsgnDntnBizAmt =  donationRepository.sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdNotNull(endDay, "200");

		Long totalCountNotLinkInstt = donationRepository.countByCntrDeAndNotLinkInstt(endDay);
		Long totalCntrAmtNotLinkInstt = donationRepository.sumCntrAmtByCntrDeAndNotLinkInstt(endDay);

		/* 민간플랫폼 누적 합계 시작 */
		List<HashMap<String, Object>> linkInsttCumulativeList = new ArrayList<HashMap<String,Object>>();

		for(Code code : commonCodeForLinkInsttList()) {
			HashMap<String, Object> codeMap = new HashMap<>();
			Long linkInsttCnt = donationRepository.countByCntrDeAndLinkInsttCd(endDay, code.getId());
			Long linkInsttAmt = donationRepository.sumCntrAmtByCntrDeAndLinkInsttCd(endDay, code.getId());
			codeMap.put("linkInsttNm", code.getLabel());
			codeMap.put("linkInsttCnt", linkInsttCnt);
			codeMap.put("linkInsttAmt", linkInsttAmt);
			linkInsttCumulativeList.add(codeMap);
		}
		result.put("linkInsttCumulative", linkInsttCumulativeList);
		/* 민간플랫폼 누적 합계 종료 */

		result.put("totalCount", totalCount);
		result.put("increasedCount", increasedCount);
		result.put("onlineCount", onlineCount);
		result.put("offlineCount", offlineCount);
		result.put("onlineDsgnDntnBizCount", onlineDsgnDntnBizCount);
		result.put("offlineDsgnDntnBizCount", offlineDsgnDntnBizCount);

		result.put("totalCntrAmt", totalCntrAmt);
		result.put("increasedAmt", increasedAmt);
		result.put("onlineCntrAmt", onlineCntrAmt);
		result.put("offlineCntrAmt", offlineCntrAmt);
		result.put("onlineDsgnDntnBizAmt", onlineDsgnDntnBizAmt);
		result.put("offlineDsgnDntnBizAmt", offlineDsgnDntnBizAmt);
		result.put("totalCountNotLinkInstt", totalCountNotLinkInstt);
		result.put("totalCntrAmtNotLinkInstt", totalCntrAmtNotLinkInstt);

		return result;
	}

	public Map<String, Object> dayItemStatistics(String startDay, String endDay) {
		Map<String, Object> result = new HashMap<>();

		Long orderCount = cntrUsePointRepository.countOrderCodeByPointUseDe(endDay);
		Long usePointAmt = cntrUsePointRepository.sumCntrUsePointByPointUseDe(endDay);

		GiveState giveState = new GiveState();
		giveState.setShCntrDeStart(endDay);
		long giftB = slaveMainMapper.opmanageMainAmountGiftB(giveState);
		long giftA = slaveMainMapper.opmanageMainAmountGiftA(giveState);
		long giftC = giftA + giftB;

		result.put("orderCount", orderCount);
		result.put("usePointAmt", usePointAmt);
		result.put("itemRegCount", giftC);
		return result;
	}

	public Map<String, Object> dayUserStatistics(String startDay, String endDay) {
		Map<String, Object> result = new HashMap<>();

		// 기간지정용
//		result.put("userCount", userReportRepository.countByStatusCodeAndCreatedDate(9, startDay, endDay));

		//년초 부터 지정날
//		result.put("userCount", userReportRepository.countByStatusCodeAndCreatedDate(9, endDay));

		// 전체 정상 유저
		result.put("userCount", userReportRepository.countByStatusCodeAndCreatedDate(9));
		result.put("increaseUserCount", userReportRepository.countByStatusCodeAndCreatedDate(9, startDay, endDay));
		return result;
	}

	// 미사용
	public List<OpCall> dayCustomerCenterCallStatistics(String startDay, String endDay) {

		return callRepository.findByCallDateBetween(startDay, endDay);
	}


}
