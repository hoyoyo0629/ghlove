package saleson.shop.user;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import saleson.common.enumeration.IdType;
import saleson.shop.code.domain.Code;
import saleson.shop.user.domain.ContributionSetup;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.LocgovDeptHist;
import saleson.shop.user.domain.LocgovItemImage;
import saleson.shop.user.support.ContributionSetupSearchParam;
import saleson.shop.user.support.LocgovDeptSearchParam;
import saleson.shop.user.support.LocgovSearchParam;

public interface LocgovService {

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
	 * 지자체관리 목록 > 지자체 삭제
	 * @param locgov
	 * @return
	 */
	String deleteLocgov(Locgov locgov) throws RuntimeException;

	/**
	 * 로그인 사용자 관리자 권한 확인
	 * @return
	 */
	String getLoginUserAdminRoleCheck();

	/**
	 * 지자체 등록
	 * @param locgov
	 * @return
	 */
	String insertLocgov(Locgov locgov) throws RuntimeException;

	/**
	 * 로그인 사용자 지자체 코드 상세 정보
	 * @param userId
	 * @return
	 */
	Code getLoginUserLocgovCodeDetails(long userId);

	/**
	 * 지자체 상세 정보 조회
	 * @param searchParam
	 * @return
	 */
	Locgov getLocgovDetails(LocgovSearchParam searchParam);

	/**
	 * 기부금 설정 상세 조회
	 * @param searchParam
	 * @return
	 */
	ContributionSetup getContributionSetupDetails(ContributionSetupSearchParam searchParam);

	/**
	 * 기부금 설정 등록
	 * @param contributionSetup
	 * @return
	 */
	int insertContributionSetup(ContributionSetup contributionSetup) throws RuntimeException;

	/**
	 * 기부금 설정 수정
	 * @param contributionSetup
	 * @return
	 */
	int updateContributionSetup(ContributionSetup contributionSetup) throws RuntimeException;

	/**
	 * 지자체 수정
	 * @param locgov
	 * @return
	 * @throws Exception
	 */
	String updateLocgov(Locgov locgov) throws RuntimeException, IOException;

	/**
	 * 부서코드 이력 총갯수
	 * @param locgov
	 * @return
	 * @throws Exception
	 */
	int getLocgovDeptHistListCount(LocgovDeptSearchParam params);

	/**
	 * 부서코드 이력 가져오기
	 * @param locgov
	 * @return
	 * @throws Exception
	 */
	List<LocgovDeptHist> getLocgovDeptHistList(LocgovDeptSearchParam params);

	/**
	 * 파일 저장
	 * @param file 파일
	 * @param filePath 파일경로
	 * @param ableExt 허용 확장자
	 * @param maxSize 최대 용량
	 * @param isEncrypt 암호화 저장 여부
	 * @return String 새로운 파일 이름
	 */
	public String saveFile(MultipartFile file, String filePath, String[] ableExt, int maxSize, boolean isEncrypt);

	/**
	 * 직인 파일 정보 가져오기
	 * @param locgov
	 * @return
	 */
	Locgov getLocgovOffcsInfo(String locgov);

	/**
	 * 직인 파일 삭제
	 * @param searchParam
	 * @return
	 */
	boolean deleteOffcsFile(Locgov locgov);

	/**
	 * 답례품 업체, 답례품 관리자, 관리자 지자체 코드 가져오기
	 * @param userId
	 * @return
	 */
	String getLocgovCodeByOpId(long userId, IdType idType);

	/**
	 * 상위 지자체 코드 가져오기
	 * @param locgov
	 * @return
	 */
	String getUpperLocgovCode(String locgov);

	/**
	 * 지자체 답례품 배경 이미지 가져오기
	 * @param locgov
	 * @return
	 */
	LocgovItemImage getLocgovItemImage(String locgovCode);

	/**
	 * 지자체 답례품 배경 이미지 삭제
	 * @param locgov
	 * @return
	 */
	boolean deleteLocgovItemImage(String locgovCode, String type);
}
