package saleson.shop.slave;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.common.configuration.MapperSlave;
import saleson.shop.user.domain.GeneralCustomer;
import saleson.shop.user.domain.GeneralCustomerCntr;
import saleson.shop.user.domain.GeneralCustomerCumulativeTotal;
import saleson.shop.user.domain.GeneralCustomerPoint;
import saleson.shop.user.domain.GeneralCustomerSecede;
import saleson.shop.user.domain.SecedeCntr;
import saleson.shop.user.domain.SecedeCntrAmt;
import saleson.shop.user.support.GeneralCustomerCntrSearchParam;
import saleson.shop.user.support.GeneralCustomerPointSearchParam;
import saleson.shop.user.support.GeneralCustomerSearchParam;
import saleson.shop.userdelivery.domain.UserDelivery;

@MapperSlave("slaveGeneralCustomerMapper")
public interface SlaveGeneralCustomerMapper {

	/**
	 * 일반회원관리 목록 총 갯수 조회
	 * @param searchParam
	 * @return
	 */
	int getGeneralCustomerCountByParam(GeneralCustomerSearchParam searchParam);

	/**
	 * 일반회원관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<GeneralCustomer> getGeneralCustomerListByParam(GeneralCustomerSearchParam searchParam);

	/**
	 * 일반회원관리 상세 조회
	 * @param searchParam
	 * @return
	 */
	GeneralCustomer getGeneralCustomerDetails(GeneralCustomerSearchParam searchParam);

	/**
	 * 일반회원관리 상세 > 기부&포인트 누적합계 정보 조회
	 * @param userId
	 * @return
	 */
	GeneralCustomerCumulativeTotal getGeneralCustomerCumulativeTotal(Long userId);

	/**
	 * 일반회원관리 상세 > 기부내역 목록 갯수 조회
	 * @param searchParam
	 * @return
	 */
	int getGeneralCustomerCntrCountByParam(GeneralCustomerCntrSearchParam searchParam);

	/**
	 * 일반회원관리 상세 > 기부내역 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<GeneralCustomerCntr> getGeneralCustomerCntrListByParam(GeneralCustomerCntrSearchParam searchParam);

	/**
	 * 일반회원관리 상세 > 포인트 내역 목록 갯수 조회
	 * @param searchParam
	 * @return
	 */
	int getGeneralCustomerPointCountByParam(GeneralCustomerPointSearchParam searchParam);

	/**
	 * 일반회원관리 상세 > 포인트 내역 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<GeneralCustomerPoint> getGeneralCustomerPointListByParam(GeneralCustomerPointSearchParam searchParam);

	/**
	 * 로그인 사용자 비밀번호 조회
	 * @param userId
	 * @return
	 */
	String getPasswordByUserId(Long userId);

	/**
	 * 회원 탈퇴 처리 
	 * @param user
	 * @return
	 */
	int updateSecedeGeneralCustomer(Long userId);

	/**
	 * 회원 탈퇴 시 부모 인증 처리 
	 * @param user
	 * @return
	 */
	int updateSecedeGeneralCustomerParent(Long userId);
	
	/**
	 * 회원 탈퇴 시 부모 인증 정보 
	 * @param user
	 * @return
	 */
	int getSecedeGeneralCustomerParent(Long userId);
	
	/**
	 * 회원 상세 탈퇴 처리
	 * @param generalCustomerSecede
	 * @return
	 */
	int updateSecedeGeneralCustomerDetail(GeneralCustomerSecede generalCustomerSecede);

	/**
	 * 회원 > 관심 지자체 삭제
	 * @param userId
	 * @return
	 */
	int deleteSecedeGeneralCustomerIntrstLocgov(Long userId);

	/**
	 * 회원 > 관심 답례품 삭제
	 * @param userId
	 * @return
	 */
	int deleteSecedeGeneralCustomerIntrstRtnpsnt(Long userId);

	/**
	 * 관리자 탈퇴 처리 (삭제)
	 * @param userId
	 * @return
	 */
	int deleteSecedeManager(Long userId);

	/**
	 * 회원별 권한 삭제
	 * @param userId
	 * @return
	 */
	int deleteSecedeUserRole(Long userId);

	/**
	 * 일반회원관리 상세 조회 (상태 코드 미적용)
	 * @param searchParam
	 * @return
	 */
	GeneralCustomer getGeneralCustomerDetailsNotStatusCode(GeneralCustomerSearchParam searchParam);

	/**
	 * 관리자 비밀번호 변경 이력 삭제
	 * @param userId
	 * @return
	 */
	int deleteManagerPasswordLog(Long userId);

	/**
	 * 잔여포인트 조회
	 * @param userId
	 * @return
	 */
	List<SecedeCntr> getCntrBlcePointList(Long userId);
	
	/**
	 * 잔여포인트 삭제 처리
	 * @param cntr
	 * @return
	 */
	int updateCntrBlcePointDel(SecedeCntr cntr);

	/**
	 * 기부 사용 포인트 (탈퇴)
	 * @param cntr
	 * @return
	 */
	int insertCntrUsePoint(SecedeCntr cntr);

	/**
	 * 당해년도 총 기부납부금액
	 * @param userId
	 * @return
	 */
	Integer selectTotalCntrAmt(Long userId);

	/**
	 * 당해년도 총 기부납부금액 저장
	 * @param cntrInfo
	 * @return
	 */
	int insertTotalCntrAmt(SecedeCntrAmt cntrInfo);
	
	List<UserDelivery> userDeliveryList(Long userId);
	
	/**
	 * 개인정보열람결과 저장 (2023.02.22)
	 * @param searchParam
	 * @return
	 */
	int indvdlinfoReadngHist(GeneralCustomerSearchParam searchParam);
	
	
}
