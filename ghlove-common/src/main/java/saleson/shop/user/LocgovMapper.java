package saleson.shop.user;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.code.domain.Code;
import saleson.shop.donation.domain.HonorCntr;
import saleson.shop.user.domain.ContributionSetup;
import saleson.shop.user.domain.HonorCntrbtr;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.LocgovDeptHist;
import saleson.shop.user.support.ContributionSetupSearchParam;
import saleson.shop.user.support.LocgovDeptSearchParam;
import saleson.shop.user.support.LocgovSearchParam;

@Mapper("locgovMapper")
public interface LocgovMapper {

	/**
	 * 지자체관리 목록 총 갯수 조회
	 * @param searchParam
	 * @return
	 */
	int getLocgovCountByParam(LocgovSearchParam searchParam);

	/**
	 * 지자체관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<Locgov> getLocgovListByParam(LocgovSearchParam searchParam);

	/**
	 * 지자체관리 목록 > 포인트 목록 총 갯수 조회 
	 * @param searchParam
	 * @return
	 */
	int getLocgovPointCountBySearchParam(ContributionSetupSearchParam searchParam);

	/**
	 * 지자체관리 목록 > 포인트 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<ContributionSetup> getLocgovPointListBySearchParam(ContributionSetupSearchParam searchParam);

	/**
	 * 지자체 코드에 해당하는 기부 건수 조회
	 * @param locgov
	 * @return
	 */
	int getContributionCountByLocgovCode(Locgov locgov);

	/**
	 * 지자체 삭제
	 * @param locgov
	 * @return
	 */
	int deleteLocgov(Locgov locgov);

	/**
	 * 기부금 설정 삭제
	 * @param contributionSetup
	 * @return
	 */
	int deleteContributionSetup(ContributionSetup contributionSetup);

	/**
	 * 지자체 상세 정보 조회
	 * @param searchParam
	 * @return
	 */
	Locgov getLocgovDetails(LocgovSearchParam searchParam);

	/**
	 * 지자체 등록
	 * @param locgov
	 * @return
	 */
	int insertLocgov(Locgov locgov);

	/**
	 * 기부금 설정 등록
	 * @param contributionSetup
	 * @return
	 */
	int insertContributionSetup(ContributionSetup contributionSetup);

	/**
	 * 로그인 사용자 관리자 권한 확인
	 * @param userId
	 * @return
	 */
	Code getLoginUserLocgovCodeDetails(long userId);

	/**
	 * 기부금 설정 상세 조회
	 * @param searchParam
	 * @return
	 */
	ContributionSetup getContributionSetupDetails(ContributionSetupSearchParam searchParam);

	/**
	 * 기부금 설정 수정
	 * @param contributionSetup
	 * @return
	 */
	int updateContributionSetup(ContributionSetup contributionSetup);

	/**
	 * 지자체 수정
	 * @param locgov
	 * @return
	 */
	int updateLocgov(Locgov locgov);
	
	/** 명예기부자 전체 삭제
	 * @param locgov
	 */
	int deleteHonorCntrUser(Locgov locgov);
	
	/**
	 * 명예기부자 등록
	 * @param locgov
	 */
	int insertHonorCntrUser(Locgov locgov);
	
	/**
	 * 기부 제한 등록 여부 확인
	 * @param locgov
	 */
	int getCntrLmttCount(String locgovCode);
	
	/**
	 * 기부 제한 등록
	 * @param locgov
	 * @return
	 */
	int insertCntrLmtt(Locgov locgov);
	
	/**
	 * 기부 제한 수정
	 * @param locgov
	 * @return
	 */
	int updateCntrLmmt(Locgov locgov);
	
	/**
	 * 기부 제한 삭제
	 * @param locgov
	 * @return
	 */
	int deleteCntrLmmt(Locgov locgov);
	
	/**
	 * 명예기부자 전체 초기화(삭제)
	 * @param locgov
	 * @return
	 */
	int resetHonorCntrUser();
	
	/**
	 * 명예회원 기부액 기준 초기화
	 * @param locgov
	 * @return
	 */
	int resetStdrAmt();
	
	/**
	 * 부서코드 이력 총갯수
	 * @param locgov
	 * @return
	 */
	int getLocgovDeptHistListCount(LocgovDeptSearchParam params);
	
	/**
	 * 부서코드 이력 가져오기
	 * @param locgov
	 * @return
	 */
	List<LocgovDeptHist> getLocgovDeptHistList(LocgovDeptSearchParam param);
	
	/**
	 * 부서코드 이력 등록
	 * @param locgov
	 * @return
	 */
	int insertLocgovDeptHist(LocgovDeptHist locgov);
	
	/**
	 * 부서코드 및 명예회원기준금액 가져오기
	 * @param locgovCode
	 * @return
	 */
	Locgov getProcessDeptCodeAndHonorAmt(String locgovCode);
	
	/**
	 * 직인 파일 정보 가져오기
	 * @param locgovCode
	 */
	Locgov getLocgovOffcsInfo(String locgovCode);
	
	/**
	 * 직인 파일 정보 삭제
	 * @param locgovCode
	 */
	int updateLocgovOffcsInfo(String locgovCode); 
	
	/**
	 * 관리자 자치구 코드 가져오기
	 * @param userId
	 * @return
	 */
	String getLocgovCodeByManagerId(long userId); 
	
	/**
	 * 답례품업체 자치구 코드 가져오기
	 * @param userId
	 * @return
	 */
	String getLocgovCodeBySellerId(long userId); 
	
	/**
	 * 답례품관리자 자치구 코드 가져오기
	 * @param userId
	 * @return
	 */
	String getLocgovCodeBySellerUserId(long userId);
	
	/**
	 * 상위 지자체 코드 가져오기
	 * @param locgov
	 * @return
	 */
	String getUpperLocgovCode(String locgov);	
	
	/**
	 * 명예회원 리스트 가져오기
	 * @param locgov
	 * @return
	 */
	List<HonorCntr> getHonorUserList(String locgovCode);
	
}
