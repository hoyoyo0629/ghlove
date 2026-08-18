package saleson.shop.user;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;

import lombok.RequiredArgsConstructor;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.slave.SlaveGeneralCustomerMapper;
import saleson.shop.user.domain.GeneralCustomer;
import saleson.shop.user.domain.GeneralCustomerCntr;
import saleson.shop.user.domain.GeneralCustomerCumulativeTotal;
import saleson.shop.user.domain.GeneralCustomerEncryptor;
import saleson.shop.user.domain.GeneralCustomerPoint;
import saleson.shop.user.domain.GeneralCustomerSecede;
import saleson.shop.user.domain.GeneralCustomerSecedeResult;
import saleson.shop.user.domain.SecedeCntr;
import saleson.shop.user.domain.SecedeCntrAmt;
import saleson.shop.user.support.GeneralCustomerCntrSearchParam;
import saleson.shop.user.support.GeneralCustomerPointSearchParam;
import saleson.shop.user.support.GeneralCustomerSearchParam;
import saleson.shop.userdelivery.UserDeliveryService;
import saleson.shop.userdelivery.domain.UserDelivery;

@RequiredArgsConstructor
@Service("generalCustomerService")
public class GeneralCustomerServiceImpl extends EgovAbstractServiceImpl implements GeneralCustomerService {

	private final GeneralCustomerMapper generalCustomerMapper;
	private final SlaveGeneralCustomerMapper slaveGeneralCustomerMapper;
	private final GeneralCustomerEncryptor generalCustomerEncryptor;

	@Autowired SequenceService sequenceService;

	@Autowired
	private UserDeliveryService userDeliveryService;


	/**
	 * 일반회원관리 목록 총 갯수 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public int getGeneralCustomerCountByParam(GeneralCustomerSearchParam searchParam) {
		return slaveGeneralCustomerMapper.getGeneralCustomerCountByParam(searchParam);
	}

	/**
	 * 일반회원관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<GeneralCustomer> getGeneralCustomerListByParam(GeneralCustomerSearchParam searchParam) {
		List<GeneralCustomer> customerList = slaveGeneralCustomerMapper.getGeneralCustomerListByParam(searchParam);

		if(customerList != null && customerList.size() > 0) {
			for(GeneralCustomer customer : customerList) {
				decryptData(customer);
			}
		}

		return customerList;
	}

	/**
	 * 일반회원관리 상세 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public GeneralCustomer getGeneralCustomerDetails(GeneralCustomerSearchParam searchParam) {
		GeneralCustomer details = slaveGeneralCustomerMapper.getGeneralCustomerDetails(searchParam);
		decryptData(details);
		return details;
	}

	/**
	 * 일반회원관리 상세 > 기부&포인트 누적합계 정보 조회
	 * @param userId
	 * @return
	 */
	@Override
	public GeneralCustomerCumulativeTotal getGeneralCustomerCumulativeTotal(Long userId) {
		return slaveGeneralCustomerMapper.getGeneralCustomerCumulativeTotal(userId);
	}

	/**
	 * 일반회원관리 상세 조회 (마스킹 미처리)
	 * @param searchParam
	 * @return
	 */
	@Override
	public GeneralCustomer getGeneralCustomerDetailsNoMasking(GeneralCustomerSearchParam searchParam) {

		//개인정보
		GeneralCustomer generalUserInfo = slaveGeneralCustomerMapper.getGeneralCustomerDetails(searchParam);

		/**
		 * 개인정보열람 이력 저장
		 */
		User user = UserUtils.getUser(); //로그인사용자 정보
		//번호 채번
		long readngSn = sequenceService.getId("G_INDVDLINFO_READNG_HIST");

		searchParam.setReadngSn(readngSn);
		searchParam.setSessionUserId(user.getUserId()); //로그인사용자로 userId치환
		searchParam.setTrgetUserId(generalUserInfo.getUserId()); //대상자 일련번호

		generalCustomerMapper.indvdlinfoReadngHist(searchParam);

		return generalUserInfo;
		//return slaveGeneralCustomerMapper.getGeneralCustomerDetails(searchParam);
	}

	/**
	 * 일반회원관리 상세 > 기부내역 목록 갯수 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public int getGeneralCustomerCntrCountByParam(GeneralCustomerCntrSearchParam searchParam) {
		return slaveGeneralCustomerMapper.getGeneralCustomerCntrCountByParam(searchParam);
	}

	/**
	 * 일반회원관리 상세 > 기부내역 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<GeneralCustomerCntr> getGeneralCustomerCntrListByParam(GeneralCustomerCntrSearchParam searchParam) {
		return slaveGeneralCustomerMapper.getGeneralCustomerCntrListByParam(searchParam);
	}

	/**
	 * 일반회원관리 상세 > 포인트 내역 목록 갯수 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public int getGeneralCustomerPointCountByParam(GeneralCustomerPointSearchParam searchParam) {
		return slaveGeneralCustomerMapper.getGeneralCustomerPointCountByParam(searchParam);
	}

	/**
	 * 일반회원관리 상세 > 포인트 내역 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<GeneralCustomerPoint> getGeneralCustomerPointListByParam(GeneralCustomerPointSearchParam searchParam) {
		return slaveGeneralCustomerMapper.getGeneralCustomerPointListByParam(searchParam);
	}

	/**
	 * 컬럼 데이터 복호화
	 * @param user
	 */
	private void decryptData(GeneralCustomer generalCustomer) {
		if (generalCustomer != null) {
			generalCustomer.decrypt(generalCustomerEncryptor, true);
		}
	}

	/**
	 * 로그인 사용자 비밀번호 조회
	 * @param userId
	 * @return
	 */
	@Override
	public String getPasswordByUserId(Long userId) {
		return slaveGeneralCustomerMapper.getPasswordByUserId(userId);
	}

	/**
	 * 일반회원관리 상세 > 회원탈퇴 처리
	 * @param generalCustomerSecede
	 * @return
	 */
	@Override
	public GeneralCustomerSecedeResult updateGeneralCustomerSecedeProcess(GeneralCustomerSecede generalCustomerSecede) throws RuntimeException {
		GeneralCustomerSecedeResult result = new GeneralCustomerSecedeResult();
		result.setCode("FAIL");

		/*************************************************************************
		 *  1. 유효성 검사
		 *************************************************************************/
		// 1-1. 회원 ID 확인
		if(generalCustomerSecede.getUserId() == null
				|| "".equals(String.valueOf(generalCustomerSecede.getUserId()))) {
			return result;
		};

		// 1-2. 회원 정보 조회
		GeneralCustomerSearchParam searchParam = new GeneralCustomerSearchParam();
		searchParam.setUserId(generalCustomerSecede.getUserId());
		GeneralCustomer customer = generalCustomerMapper.getGeneralCustomerDetailsNotStatusCode(searchParam);

		if(customer == null) {
			return result;
		};

		// 1-3. 회원 상태코드 확인 (1: 가입대기, 2:차단, 3:탈퇴, 4:휴면계정, 9:정상)
		if(customer.getStatusCode() == 3) {
			result.setCode("ERR_ALR_SECEDE");
			return result;
		};

		// 1-4. 디지털 원패스 회원인지 조회
		if(customer != null && customer.getUserKey() != null && !"".equals(customer.getUserKey())) {
			result.setCode("ERR_ONE_PASS");
			return result;
		};


		/*************************************************************************
		 *  2. 당해년도 총 기부납부금액 저장
		 *************************************************************************/
		// 2-1. 당해년도 총 기부납부금액 조회
		Integer totalCntrAmt = generalCustomerMapper.selectTotalCntrAmt(generalCustomerSecede.getUserId());

		// 2-2. 당해년도 총 기부납부금액 존재하면 해당 정보 저장
		if(CommonUtils.intNvl(totalCntrAmt) > 0 && !"".equals(CommonUtils.dataNvl(customer.getMberCi()))) {
			SecedeCntrAmt secedeCntrAmt = new SecedeCntrAmt();
			secedeCntrAmt.setUserId(generalCustomerSecede.getUserId());
			secedeCntrAmt.setMberCi(customer.getMberCi());
			secedeCntrAmt.setCntrAmt(totalCntrAmt);
			secedeCntrAmt.setLoginUserId(UserUtils.getUser().getUserId());
			generalCustomerMapper.insertTotalCntrAmt(secedeCntrAmt);
		}


		/*************************************************************************
		 *  3. 회원 탈퇴 처리
		 *************************************************************************/
		//3-0. 회원 CI 값 별도 보관
		generalCustomerMapper.insertSecedeCustomer(generalCustomerSecede.getUserId());

		// 3-1. 회원 탈퇴
		generalCustomerMapper.updateSecedeGeneralCustomer(generalCustomerSecede.getUserId());

		//부모 인증 정보 있을 시
		int parentCnt = generalCustomerMapper.getSecedeGeneralCustomerParent(generalCustomerSecede.getUserId());
		if(parentCnt > 0) {
			// 3-1-1. 부모인증 탈퇴
			generalCustomerMapper.updateSecedeGeneralCustomerParent(generalCustomerSecede.getUserId());
		}

		// 3-2. 회원 상세 탈퇴
		generalCustomerMapper.updateSecedeGeneralCustomerDetail(generalCustomerSecede);

		// 3-3. 관심 지자체 삭제
		generalCustomerMapper.deleteSecedeGeneralCustomerIntrstLocgov(generalCustomerSecede.getUserId());

		// 3-4. 관심 답례품 삭제
		generalCustomerMapper.deleteSecedeGeneralCustomerIntrstRtnpsnt(generalCustomerSecede.getUserId());


		/*************************************************************************
		 *  4. 관리자 탈퇴 처리
		 *************************************************************************/
		// 4-1. 관리자 탈퇴 (삭제)
		generalCustomerMapper.deleteManagerPasswordLog(generalCustomerSecede.getUserId());
		generalCustomerMapper.deleteSecedeManager(generalCustomerSecede.getUserId());


		/*************************************************************************
		 *  5. 사용자 권한 삭제
		 *************************************************************************/
		// 5-1. 사용자(회원, 관리자) 권한 삭제
		generalCustomerMapper.deleteSecedeUserRole(generalCustomerSecede.getUserId());


		/*************************************************************************
		 *  6. 잔여포인트 삭제
		 *************************************************************************/
		// 6-1. 잔여포인트 조회
		List<SecedeCntr> cntrBlcePoint = generalCustomerMapper.getCntrBlcePointList(generalCustomerSecede.getUserId());

		// 6-2. 잔여포인트 삭제 처리
		if(cntrBlcePoint != null && cntrBlcePoint.size() > 0) {
			for(SecedeCntr cntr : cntrBlcePoint) {
				cntr.setLoginUserId(UserUtils.getUser().getUserId());
				cntr.setUseSeCode("3");
				cntr.setUseCn("회원탈퇴로 인한 포인트 소멸");
				generalCustomerMapper.updateCntrBlcePointDel(cntr);
				generalCustomerMapper.insertCntrUsePoint(cntr);
			}
		}


		/*************************************************************************
		 *  7. 결과값
		 *************************************************************************/
		// 7-1. 결과값 셋팅
		result.setCode("SUCC");


		return result;
	}

	@Override
	public List<UserDelivery> userDeliveryList(Long userId) {
//		return generalCustomerMapper.userDeliveryList(userId);
		return userDeliveryService.getUserDeliveryList(userId);			// 마스킹 처리 위해 변경
	}

}
