package saleson.shop.mypage;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import saleson.common.utils.UserUtils;
import saleson.shop.mypage.domain.Cntr;
import saleson.shop.mypage.domain.CntrPoint;
import saleson.shop.mypage.domain.CntrPointDetail;
import saleson.shop.mypage.domain.IntrstLocGov;
import saleson.shop.mypage.domain.MypageMain;
import saleson.shop.mypage.support.CntrParam;
import saleson.shop.mypage.support.CntrPointParam;
import saleson.shop.mypage.support.CntrReceipt;
import saleson.shop.mypage.support.HonorInfo;
import saleson.shop.mypage.support.IntrsLocgovParam;
import saleson.shop.mypage.support.IntrstLocGovParam;
import saleson.shop.mypage.support.QrInfo;
import saleson.shop.mypage.support.ReceiptParam;
import saleson.shop.user.domain.HonorCntrbtr;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.mypage.domain.Order;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("myPageService")
public class MyPageServiceImpl implements MyPageService {
	private static final Logger log = LoggerFactory.getLogger(MyPageServiceImpl.class);

	private final MypageMapper mypageMapper;

	@Override
	public List<IntrstLocGov> intrstLocGovInfo(IntrstLocGovParam intrstLocGovParam) {
		return mypageMapper.intrstLocGovInfo(intrstLocGovParam);
	}

	@Override
	public int intrstLocGovCnt(IntrstLocGovParam intrstLocGovParam) {
		return mypageMapper.intrstLocGovCnt(intrstLocGovParam);
	}

	@Override
	public int deleteIntrstLocGov(IntrstLocGovParam intrstLocGovParam) {
		return mypageMapper.deleteIntrstLocGov(intrstLocGovParam);
	}

	@Override
	public int getCntrPointCnt(CntrPointParam gntrPointParam) {
		return mypageMapper.getCntrPointCnt(gntrPointParam);
	}

	@Override
	public List<CntrPoint> getCntrPointInfo(CntrPointParam gntrPointParam) {
		return mypageMapper.getCntrPointInfo(gntrPointParam);
	}

	@Override
	public int cntrPointTotal(CntrPointParam gntrPointParam) {
		return mypageMapper.cntrPointTotal(gntrPointParam);
	}

	@Override
	public int usePointTotal(CntrPointParam gntrPointParam) {
		return mypageMapper.usePointTotal(gntrPointParam);
	}

	@Override
	public int getTotalCntrAmt(CntrParam cntrParam) {
		return mypageMapper.getTotalCntrAmt(cntrParam);
	}

	@Override
	public int getThisYearTotalCntrAmt(Long userId) {
		return mypageMapper.getThisYearTotalCntrAmt(userId);
	}

	@Override
	public int getGntrListTotalCnt(CntrParam cntrParam) {
		return mypageMapper.getGntrListTotalCnt(cntrParam);
	}

	@Override
	public List<Cntr> getCntrListInfo(CntrParam cntrParam) {
		return mypageMapper.getCntrListInfo(cntrParam);
	}

	@Override
	public List<LocGovInfo> getLocGovList(LocGovInfo locGovInfo) {
		return mypageMapper.getLocGovList(locGovInfo);
	}

	@Override
	public int getCntrPointDetailCnt(CntrPointParam gntrPointParam) {
		return mypageMapper.getCntrPointDetailCnt(gntrPointParam);
	}

	@Override
	public List<CntrPoint> getCntrPointDetail(CntrPointParam gntrPointParam) {
		return mypageMapper.getCntrPointDetail(gntrPointParam);
	}

	@Override
	public int blcePointTotal(CntrPointParam gntrPointParam) {
		return mypageMapper.blcePointTotal(gntrPointParam);
	}

	@Override
	public CntrPointDetail getLocgovNm(CntrPointParam gntrPointParam) {
		return mypageMapper.getLocgovNm(gntrPointParam);
	}

	@Override
	public int getTaxRedutionEstimate(CntrParam cntrParam) {
		return mypageMapper.getTaxRedutionEstimate(cntrParam);
	}

	@Override
	public List<Order> getOrderLevelList(CntrParam cntrParam) {
		return mypageMapper.getOrderLevelList(cntrParam);
	}

	@Override
	public List<Order> getOrderClaimLevelList(CntrParam cntrParam) {
		return mypageMapper.getOrderClaimLevelList(cntrParam);
	}

	@Override
	public Cntr getCntrReceipt(ReceiptParam receiptParam) {
		return mypageMapper.getCntrReceipt(receiptParam);
	}

	@Override
	public int insertCntrReceipt(ReceiptParam receiptParam) {
		return mypageMapper.insertCntrReceipt(receiptParam);
	}

	@Override
	public List<HonorCntrbtr> getUserHonorListTotal(long userId) {
		return mypageMapper.getUserHonorListTotal(userId);
	}

	@Override
	public List<HonorCntrbtr> getUserHonorList(long userId) {
		return mypageMapper.getUserHonorList(userId);
	}

	@Override
	public int deleteIntrstLocGovAll(IntrsLocgovParam intrstLocGovParam) {
		// TODO Auto-generated method stub
		return mypageMapper.deleteIntrstLocGovAll(intrstLocGovParam);
	}

	@Override
	public QrInfo getQrData() {
		QrInfo result = new QrInfo();

		long userId = UserUtils.getUser().getUserId();

		if (userId > 0) {
			List<HonorInfo> list = mypageMapper.getUserCntrHonorList(userId);
			if (list != null && !list.isEmpty()) {
				result.setHonorInfos(list);
				result.setSuccess(true);
			} else {
				result.setErrCode("NOT_HONOR");
				result.setErrMsg("명예시민증 정보가 없습니다.");
			}
		} else {
			result.setErrCode("NOT_LOGIN");
			result.setErrMsg("로그인 상태가 아닙니다.");
		}

		return result;
	}

	@Override
	public Map<String, Object> getReceiptCntrAmtAndTotalCnt(CntrReceipt receiptParam) {
		return mypageMapper.getReceiptCntrAmtAndTotalCnt(receiptParam);
	}

	@Override
	public List<Cntr> getReceiptListInfo(CntrReceipt receiptParam) {
		return mypageMapper.getReceiptListInfo(receiptParam);
	}

	@Override
	public Map<String, Object> getReceiptCntrPopInfo(CntrReceipt receiptParam){
		return mypageMapper.getReceiptCntrPopInfo(receiptParam);
	};

	// 20260122 특별재난지역 기부 체크 추가
	@Override
	public List<Cntr> getReceiptPopListInfo(CntrReceipt receiptParam) {
		List<Cntr> returnList = new ArrayList<>();

		// 기부확인증 목록
		List<Cntr> cntrList = mypageMapper.getReceiptPopListInfo(receiptParam);

		// 특별재난지역 정보 목록
		List<Map<String, Object>> spelDstrZnList = mypageMapper.getSpelDstrZn();

		// 데이터 없을때 Array만 리턴
		if(cntrList == null || cntrList.isEmpty() || spelDstrZnList == null || spelDstrZnList.isEmpty()) {
			return cntrList;
		}

		// 특별재난 목록을 기부 목록에 그룹핑
		Map<String, List<Map<String, Object>>> spelByLocgov = spelDstrZnList.stream()
				.filter(m -> m.get("locgov_code") != null)
				.collect(Collectors.groupingBy(m -> String.valueOf(m.get("locgov_code"))));

		// 순회해서 날짜 지자체코드 비교 후 flag값 set
		for(Cntr cntr : cntrList) {

			String cntrDe = cntr.getOriginCntrDe();
			String cntrLocgov = cntr.getLocgovCode();

			if(cntrDe == null || cntrLocgov == null) continue;

			List<Map<String, Object>> period = spelByLocgov.get(String.valueOf(cntrLocgov));
			if(period == null || period.isEmpty()) {
				cntr.setSpelDstrYn("N");
				returnList.add(cntr);
				continue;
			}

			for(Map<String, Object> spel: period) {
				String start = spel.get("noti_date") == null ? null : String.valueOf(spel.get("noti_date"));
				String end = spel.get("end_date") == null ? null : String.valueOf(spel.get("end_date"));

				if(start == null || end == null) continue;

				boolean inRange = cntrDe.compareTo(start) >= 0 && cntrDe.compareTo(end) <= 0;

				if(inRange) {
					cntr.setSpelDstrYn("Y");
				}

			}
			returnList.add(cntr);
		}

		return returnList;
	}

	@Override
	public Map<String, Object> getTopLocGov(CntrReceipt receiptParam) {
		return mypageMapper.getTopLocGov(receiptParam);
	}

	@Override
	public void saveHonorViewHist(ReceiptParam receiptParam) {
		mypageMapper.saveHonorViewHist(receiptParam);
	}

}
