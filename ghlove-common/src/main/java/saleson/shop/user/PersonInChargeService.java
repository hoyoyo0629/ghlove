package saleson.shop.user;

import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.PersonInCharge;
import saleson.shop.user.domain.PersonInChargeResult;
import saleson.shop.user.support.PersonInChargeSearchParam;

public interface PersonInChargeService {

	/**
	 * 담당자 목록 갯수 조회
	 * @param searchParam
	 * @return
	 */
	int getChargerCountByParam(PersonInChargeSearchParam searchParam);

	/**
	 * 담당자 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<PersonInCharge> getChargerListByParam(PersonInChargeSearchParam searchParam);

	/**
	 * 담당자 목록 엑셀 다운로드
	 * @param searchParam
	 * @return
	 */
	SXSSFWorkbook streamChargerData(PersonInChargeSearchParam searchParam, int totalCount) throws Exception;

	/**
	 * 로그인 사용자 관리자 권한 조회
	 * @return
	 */
	String getLoginUserAdminAuthority();

	/**
	 * 담당자 삭제
	 * @param charger
	 * @return
	 */
	PersonInChargeResult deleteCharger(PersonInCharge charger) throws RuntimeException;

	/**
	 * 담당자 상세 조회
	 * @param searchParam
	 * @return
	 */
	PersonInCharge getChargerDetails(PersonInChargeSearchParam searchParam);

	/**
	 * 하위 지자체 정보 조회
	 * @param upperLocgovCode
	 * @return
	 */
	List<Locgov> getLocgovList(String upperLocgovCode);

	/**
	 * 지자체 주담당자 인원수 확인
	 * @param personInCharge
	 * @return
	 */
	int getLocgovMainPersonInChargeCount(PersonInCharge personInCharge);

	/**
	 * 담당자 정보 수정
	 * @param personInCharge
	 * @return
	 */
	PersonInChargeResult updatePersonInCharge(PersonInCharge personInCharge) throws RuntimeException;

	/**
	 * 행안부 주담당자 인원수 확인
	 * @param personInCharge
	 * @return
	 */
	int getGovMainPersonInChargeCount(PersonInCharge personInCharge);

	/**
	 * 오프라인 담당자관리 목록 > 상태코드 수정 (사용, 중지)
	 * @param personInCharge
	 * @return
	 */
	int updateStatusCode(PersonInCharge personInCharge);

	/**
	 * 오프라인 주담당자 인원수 확인
	 * @param personInCharge
	 * @return
	 */
	int getOffPersonInChargeMainCount(PersonInCharge personInCharge);

	/**
	 * 오프라인 담당자 정보 등록
	 * @param personInCharge
	 * @return
	 */
	PersonInChargeResult insertOffPersonInCharge(PersonInCharge personInCharge) throws RuntimeException;

	/**
	 * 오프라인 담당자 정보 수정
	 * @param personInCharge
	 * @return
	 */
	PersonInChargeResult updateOffPersonInCharge(PersonInCharge personInCharge) throws RuntimeException;

	/**
	 * 임시 비밀번호 발급
	 * @param charger
	 * @return
	 */
	String updatePasswordInit(PersonInCharge charger);

	/**
	 * 관리자 정보갱신 여부 - Y/N
	 * @param userId
	 * @return
	 */
	String getOffPersonInChargeInfoUpdateFlag();

	/**
	 * 지자체 담당자 카운트
	 * @param searchParam
	 * @return
	 */
	int getLocgovChargerCountByParam(PersonInChargeSearchParam searchParam);

	/**
	 * 지자체 담당자 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<PersonInCharge> getLocgovChargerListByParam(PersonInChargeSearchParam searchParam);
}
