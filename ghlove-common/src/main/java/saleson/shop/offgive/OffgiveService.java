package saleson.shop.offgive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import saleson.shop.offgive.domain.Manager;
import saleson.shop.offgive.domain.Offgive;

public interface OffgiveService {

	// 오프라인접수 목록 count
	public int getOffgiveListCount(Offgive offgive);

	// 오프라인접수 목록
	public List<Offgive> getOffgiveList(Offgive offgive);

	// 오프라인접수 목록 엑셀 다운로드
	public SXSSFWorkbook streamOffgiveList(Offgive offgive, int totalCount);

	public String insertOffgive(Offgive offgive);
	public void updateOffgive(Offgive offgive);
//	public void deleteOffgive(String cntrSn);

	// 오프라인접수 상세
	public Offgive getOffgive(String cntrSn);

	// 오프라인접수 상세 출력
	public Offgive getOffgivePrint(Offgive offgive);

	// 관리자 정보 조회
	public Manager getManager(long userId);

	// 회원정보 조회
	public HashMap<String, Object> getUserByMberCi(String mberCi);

	// 한도체크
	public Integer getMaxCheck(String userId, String mberCi);

	// 우편번호로 지자체코드 조회
	public HashMap<String, Object> getLocgovMapngCode(String locgovCode);

	/**
	 * 로그인 사용자 지자체코드정보 조회
	 * @param userId
	 * @return
	 */
	public String getLocgovCodeByUserId(Long userId);


	/**
	 * 오프라인 기탁서 등록
	 * 대표 답례품 조회
	 */
	public List<Map<String,Object>> selectOffRprs(String lclgvCd);

	/**
	 * 오프라인 기탁서
	 * 기탁자 휴대전화번호 수정
	 */
	public int updatePhoneNumber(Integer userId, String phoneNumber);

}
