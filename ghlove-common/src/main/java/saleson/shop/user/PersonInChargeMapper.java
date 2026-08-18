package saleson.shop.user;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.ManagerRequest;
import saleson.shop.user.domain.PersonInCharge;
import saleson.shop.user.support.PersonInChargeSearchParam;

@Mapper("chargerMapper")
public interface PersonInChargeMapper {

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
	 * 담당자 삭제
	 * @param charger
	 * @return
	 */
	int deleteCharger(PersonInCharge charger);

	/**
	 * 담당자 권한 삭제
	 * @param charger
	 * @return
	 */
	int deleteChargerRole(PersonInCharge charger);

	/**
	 * 담당자 상세
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
	 * 담당자 수정
	 * @param personInCharge
	 * @return
	 */
	int updatePersonInCharge(PersonInCharge personInCharge);

	/**
	 * 담당자 권한 삭제
	 * @param personInCharge
	 * @return
	 */
	int deletePersonInChargeUserRole(PersonInCharge personInCharge);

	/**
	 * 담당자 권한 등록
	 * @param personInCharge
	 * @return
	 */
	int insertPersonInChargeUserRole(PersonInCharge personInCharge);

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
	 * 오프라인담당자 수정 처리
	 * @param personInCharge
	 * @return
	 */
	int updateOffPersonInCharge(PersonInCharge personInCharge);

	/**
	 * 오프라인담당자 :  지점연락처 op_user_detail/tel_number 가져오기
	 * @param userId
	 * @return
	 */
	String getUserOffTelNumber(long userId);

	/**
	 * 오프라인담당자 :  지점연락처 op_user_detail/tel_number 삭제
	 * @param userId
	 * @return
	 */
	int deleteChargerTelNumber(PersonInCharge charger);

	/**
	 * 회원 정보 수정
	 * @param userId
	 * @return
	 */
	int updateUserSync(Long userId);

	/**
	 * 회원 상세 정보 수정
	 * @param userId
	 * @return
	 */
	int updateUserDetailSync(Long userId);

	/**
	 * 회원 패스워드 변경
	 * @param charger
	 * @return
	 */
	int updateUserPassword(PersonInCharge charger);

	/**
	 * 관리자 패스워드 변경
	 * @param charger
	 * @return
	 */
	int updateManagerPassword(PersonInCharge charger);

	/**
	 * 관리자 정보갱신일자 조회
	 * @param userId
	 * @return
	 */
	String getManagerInfoUpdateDate(long userId);

	/**
	 * 사용자 관리자 권한 조회
	 * @param userId
	 * @return
	 */
	String getUserAdminRole(Long userId);

	/**
	 * 관리자 비밀번호 변경 이력 삭제
	 * @param charger
	 * @return
	 */
	int deleteManagerPasswordLog(PersonInCharge charger);

	/**
	 * 로그인 ID 조회
	 * @param userId
	 * @return
	 */
	String getLoginIdByUserId(Long userId);

	/**
	 * 담당자 목록 갯수 조회
	 * @param searchParam
	 * @return
	 */
	int getLocgovChargerCountByParam(PersonInChargeSearchParam searchParam);

	/**
	 * 담당자 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<PersonInCharge> getLocgovChargerListByParam(PersonInChargeSearchParam searchParam);

	/**
	 * 오프라인담당자 지점연락처 넣기
	 * @param managerRequest
	 * @return
	 */
	int updateUserDetailOffPersonTel(ManagerRequest managerRequest);
}
