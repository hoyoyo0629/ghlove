package saleson.shop.user;

import java.util.List;

import saleson.shop.user.domain.GeneralCustomer;
import saleson.shop.user.domain.GeneralCustomerCntr;
import saleson.shop.user.domain.GeneralCustomerCumulativeTotal;
import saleson.shop.user.domain.GeneralCustomerPoint;
import saleson.shop.user.domain.GeneralCustomerSecede;
import saleson.shop.user.domain.GeneralCustomerSecedeResult;
import saleson.shop.user.support.GeneralCustomerCntrSearchParam;
import saleson.shop.user.support.GeneralCustomerPointSearchParam;
import saleson.shop.user.support.GeneralCustomerSearchParam;
import saleson.shop.userdelivery.domain.UserDelivery;

public interface GeneralCustomerService {

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
	 * 일반회원관리 상세 조회 (마스킹 미처리)
	 * @param searchParam
	 * @return
	 */
	GeneralCustomer getGeneralCustomerDetailsNoMasking(GeneralCustomerSearchParam searchParam);

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
	 * 일반회원관리 상세 > 회원탈퇴 처리
	 * @param generalCustomerSecede
	 * @return
	 */
	GeneralCustomerSecedeResult updateGeneralCustomerSecedeProcess(GeneralCustomerSecede generalCustomerSecede) throws RuntimeException;
	
	List<UserDelivery> userDeliveryList(Long userId);
}
