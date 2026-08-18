package saleson.shop.donation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saleson.shop.donation.domain.CntrLmtt;
import saleson.shop.donation.support.DonationParam;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.donation.support.SidoListParam;
import saleson.shop.log.support.GifSeoulParam;
import saleson.shop.mypage.domain.Cntr;
import saleson.shop.mypage.support.CntrParam;
import saleson.shop.user.domain.LocGovInfo;

public interface NgDonationService {

	/**
	 * <pre>
	 * comment       : 기부자 정보 조회
	 * preMethodName : userCntrInfo
	 * author        : hybrid
	 * date          : 2023. 2. 24.
	 *
	 * </pre>
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> getUserCntrInfo() throws Exception;

	/**
	 * <pre>
	 * comment       : 시도 목록 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 2. 24.
	 *
	 * </pre>
	 * @param donationParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> getSidoList(DonationParam donationParam) throws Exception;


	/**
	 * <pre>
	 * comment       : 시군구 목록 조회
	 * preMethodName : getSigunguList
	 * author        : hybrid
	 * date          : 2023. 2. 24.
	 *
	 * </pre>
	 * @param donationParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> getSigunguList(DonationParam donationParam) throws Exception;

	/**
	 * <pre>
	 * comment       : 지자체 상세 정보
	 * preMethodName : getLocGovInfo
	 * author        : hybrid
	 * date          : 2023. 2. 24.
	 *
	 * </pre>
	 * @param sidoListParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> getLocGovInfo(SidoListParam sidoListParam) throws Exception;

	/**
	 * <pre>
	 * comment       : 관심지자체 여부 조회
	 * preMethodName : getIntrstLocgovInfo
	 * author        : hybrid
	 * date          : 2023. 2. 24.
	 *
	 * </pre>
	 * @param locGovInfo
	 * @return
	 * Map<String,Object>
	 */
	public Map<String, Object> getIntrstLocgovInfo(SidoListParam sidoListParam);

	/**
	 * <pre>
	 * comment       : 관심지자체 등록
	 * preMethodName : setIntrstLocgov
	 * author        : hybrid
	 * date          : 2023. 2. 24.
	 *
	 * </pre>
	 * @param donationParam
	 * @return
	 * Map<String,Object>
	 */
	public Map<String, Object> setIntrstLocgov(SidoListParam sidoListParam);

	/**
	 * <pre>
	 * comment       : 수납확인 처리
	 * preMethodName : sunapSuccess
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @param seoulParam
	 * void
	 * @throws Exception
	 */
	public Map<String, Object> sunapSuccess(SeoulParam seoulParam) throws Exception;

	/**
	 * <pre>
	 * comment       : 알림서비스 제공하지 않는 수납확인 처리
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 28.
	 *
	 * </pre>
	 * @param seoulParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> sunapSuccessNoSms(SeoulParam seoulParam) throws Exception;

	/**
	 * <pre>
	 * comment       : 홈텍스 저장
	 * preMethodName : insertHometaxGif
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @param seoulParam
	 * @return
	 * Map<String,Object>
	 */
	public int insertHometaxGif(SeoulParam seoulParam);

	/**
	 * <pre>
	 * comment       : 국세청 전자기부영수증 등록결과 메세지
	 * preMethodName : updateNtsStatus
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @param seoulParam
	 * @return
	 * Map<String,Object>
	 */
	public int updateNtsStatus(SeoulParam seoulParam);

	public CntrLmtt getCntrLmtt(SidoListParam donationParam);

	public int insertHonorCntrbtr(String locgovCode, long userId);

	/**
	 * 지방세외 부과등록 로그 개수
	 * @param nextBugaRequestLogDto
	 * @return
	 */

	public int selectNextBugaLogCount(NextBugaRequestLogDto nextBugaRequestLogDto);


	/**
	 * 지방세외 부과등록 로그 목록
	 * @param nextBugaRequestLogDto
	 * @return
	 */
	public List<NextBugaResponseLogDto> selectNextBugaLogList(NextBugaRequestLogDto nextBugaRequestLogDto);

	/**
	 * 지방세외 수납 로그 개수
	 * @param nextSunapRequestLogDto
	 * @return
	 */
	public int selectNextSunapLogCount(NextSunapRequestLogDto nextSunapRequestLogDto);

	/**
	 * 지방세외 수납 로그 목록
	 * @param nextSunapRequestLogDto
	 * @return
	 */
	public List<NextSunapResponseLogDto> selectNextSunapLogList(NextSunapRequestLogDto nextSunapRequestLogDto);

	/**
	 * 지방세외 부과등록 로그 개수
	 * @param gifStndParam
	 * @return
	 */
	public int selectGifSeoulBugaCount(GifSeoulParam gifSeoulParam);

	/**
	 * 지방세외 부과등록 로그 목록
	 * @param gifStndParam
	 * @return
	 */
	public List<GifSeoulParam> selectGifSeoulBugaList(GifSeoulParam gifSeoulParam);

	/**
	 * 지방세외 수납 로그 개수
	 * @param gifStndParam
	 * @return
	 */
	public int selectGifSeoulSunapCount(GifSeoulParam gifSeoulParam);

	/**
	 * 지방세외 수납 로그 목록
	 * @param gifStndParam
	 * @return
	 */
	public List<GifSeoulParam> selectGifSeoulSunapList(GifSeoulParam gifSeoulParam);

	/**
	 * 취소된 기부건에 대한 재수납 확인 목록 조회
	 * @param hashMap
	 * @return
	 */
	public List<HashMap<String, Object>> selectDeleteCntrReSunapList(HashMap<String, Object> hashMap);

	/**
	 * 오늘 해당 지자체에 기부한건이 있는지 체크
	 * @param hashMap
	 * @return
	 */
	public int selectTodayCntrSigunguCnt(SidoListParam sidoListParam);
	/**
	 * 오늘 해당 지자체에 기부한건 리스트
	 * @param getTotalCntrAmt
	 */
	List<Cntr> getTodayCntrListInfo(CntrParam cntrParam);



	/**
	 * <pre>
	 * comment       : 연 최대 기부 한도금액 조회
	 * preMethodName :
	 * author        : kyk
	 * date          : 2024. 11. 26.
	 *
	 * </pre>
	 * @param codeParam
	 * @return
	 * Code
	 */
//	public Code donationLimitAmt();

	/**
	 * <pre>
	 * comment       : 기부자의 부과 요청 기부금이 정상인지 확인
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 12. 12.
	 *
	 * </pre>
	 * @param UserId
	 * @param amount
	 * @return
	 * boolean
	 */
//	public boolean isDonationNormalAmount(Long UserId, Long amount);
}
