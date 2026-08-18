package saleson.shop.log;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import io.netty.util.internal.ThreadLocalRandom;
import saleson.common.utils.CommonUtils;
import saleson.shop.email.support.SendParam;
import saleson.shop.log.domain.LoginLog;
import saleson.shop.log.support.LoginLogParam;

import javax.servlet.http.HttpServletRequest;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service("loginLogService")
public class LoginLogServiceImpl extends EgovAbstractServiceImpl implements LoginLogService{

	@Autowired
	private LoginLogMapper loginLogMapper;

	@Override
	public List<LoginLog> getLoginLogListByParam(LoginLogParam param) {
		return loginLogMapper.getLoginLogListByParam(param);
	}

	@Override
	public int getLoginLogListCountByParam(LoginLogParam param) {
		return loginLogMapper.getLoginLogListCountByParam(param);
	}

	@Override
	public int insertLoginLogByManager(HttpServletRequest request, boolean isSuccess) {
		return insertLoginLog(request, LoginLog.LOGIN_TYPE_MANAGER, isSuccess, "");
	}

	@Override
	public int insertLoginLogByManager(HttpServletRequest request, String opUsername, boolean isSuccess) {
		return insertLoginLog(request, opUsername, LoginLog.LOGIN_TYPE_MANAGER, isSuccess, "");
	}

	@Override
	public int insertLoginLogBySeller(HttpServletRequest request, boolean isSuccess) {
		return insertLoginLog(request, LoginLog.LOGIN_TYPE_SELLER, isSuccess, "");
	}

	private int insertLoginLog(HttpServletRequest request, String loginType, boolean isSuccess, String memo) {
		String loginId = request.getParameter("op_username");
		String sellerMemo;
		if (LoginLog.LOGIN_TYPE_SELLER.equals(loginType)) {

			sellerMemo = "Seller User";

			if (ObjectUtils.isEmpty(loginId)) {
				loginId = request.getParameter("loginId");
				sellerMemo = "Seller";
			}

			memo += " / "+sellerMemo;
		}

		LoginLog loginLog = new LoginLog();
		loginLog.setLoginType(loginType);
		setLoginLogData(request, isSuccess, memo, loginLog, loginId);

		return loginLogMapper.insertLoginLog(loginLog);
	}

	private int insertLoginLog(HttpServletRequest request, String opUsername, String loginType, boolean isSuccess, String memo) {
		String loginId = request.getParameter("op_username");
		String sellerMemo;
		if (LoginLog.LOGIN_TYPE_SELLER.equals(loginType)) {

			sellerMemo = "Seller User";

			if (ObjectUtils.isEmpty(loginId)) {
				loginId = request.getParameter("loginId");
				sellerMemo = "Seller";
			}

			memo += " / "+sellerMemo;
		}

		if(loginId == null || "".equals(CommonUtils.dataNvl(loginId))) {
			loginId = opUsername;
		}

		LoginLog loginLog = new LoginLog();
		loginLog.setLoginType(loginType);
		setLoginLogData(request, isSuccess, memo, loginLog, loginId);

		return loginLogMapper.insertLoginLog(loginLog);
	}


	@Override
	public int insertLoginLogByUser(HttpServletRequest request, boolean isSuccess) {
		String loginId = request.getParameter("op_username");
		LoginLog loginLog = getBaseUserLoginLog(request, loginId, "", isSuccess);
		return loginLogMapper.insertLoginLogByUser(loginLog);
	}

	@Override
	public int insertLoginLogByUser(HttpServletRequest request, String loginId, boolean isSuccess) {
		LoginLog loginLog = getBaseUserLoginLog(request, loginId, "", isSuccess);
		return loginLogMapper.insertLoginLogByUser(loginLog);
	}

	private LoginLog getBaseUserLoginLog(HttpServletRequest request, String loginId, String memo, boolean isSuccess) {
		if (ObjectUtils.isEmpty(loginId)) {
			loginId = "-";
		}

		LoginLog loginLog = new LoginLog();
		loginLog.setLoginType(LoginLog.LOGIN_TYPE_USER);
		setLoginLogData(request, isSuccess, memo, loginLog, loginId);
		return loginLog;
	}

	private void setLoginLogData(HttpServletRequest request, boolean isSuccess, String memo, LoginLog loginLog, String loginId) {
		if (loginId != null && loginId.length() > 60) {
			loginId = loginId.substring(0, 57) + "...";
		}

		loginLog.setLoginId(loginId);

		String successFlag = isSuccess ? "Y" : "N" ;
		loginLog.setSuccessFlag(successFlag);

		loginLog.setRemoteAddr(saleson.common.utils.CommonUtils.getClientIp(request));
		loginLog.setMemo(memo);
	}

	/**
	 * 선택된 로그인 로그 날짜 범위 조회
	 * @param loginLogId
	 * @return
	 */
	@Override
	public LoginLog getLoginLogDetailsDateInfo(Integer loginLogId) {
		return loginLogMapper.getLoginLogDetailsDateInfo(loginLogId);
	}

	/**
	 * 사용자 로그인 로그 목록 갯수 조회
	 * @param loginLogParam
	 * @return
	 */
	@Override
	public int getUserLoginLogListCountByParam(LoginLogParam loginLogParam) {
		return loginLogMapper.getUserLoginLogListCountByParam(loginLogParam);
	}

	/**
	 * 사용자 로그인 로그 목록 조회
	 * @param loginLogParam
	 * @return
	 */
	@Override
	public List<LoginLog> getUserLoginLogListByParam(LoginLogParam loginLogParam) {
		return loginLogMapper.getUserLoginLogListByParam(loginLogParam);
	}

	/**
	 * 선택된 사용자 로그인 로그 날짜 범위 조회
	 * @param loginLogId
	 * @return
	 */
	@Override
	public LoginLog getUserLoginLogDetailsDateInfo(Integer loginLogId) {
		return loginLogMapper.getUserLoginLogDetailsDateInfo(loginLogId);
	}


	/**
	 * 판매자 로그인 로그 추가
	 * @param loginLogId
	 * @return
	 */
	@Override
	public int insertLoginLogBySeller2(HttpServletRequest request, String loginId, boolean isSuccess, String memo) {
		String sellerMemo = "Seller";

		memo += " / "+sellerMemo;

		LoginLog loginLog = new LoginLog();
		loginLog.setLoginType(LoginLog.LOGIN_TYPE_SELLER);
		setLoginLogData(request, isSuccess, memo, loginLog, loginId);

		return loginLogMapper.insertLoginLog(loginLog);
	}



	/**
	 * 관리자 로그인시 이메일에 보낼 인증번호 생성 & 저장 & 이메일 발송
	 * @param loginId
	 * @description 관리자가 ID/PW 로그인시 auth key를 생성하여 이메일로 보내서 2차 인증
	 * @return 인증번호
	 */
	@Override
	public String insertLoginEmailLog(LoginLogParam param) {
		loginLogMapper.insertLoginEmailLog(param);
		return param.getAuthNum();
	}



	/**
	 * 인증번호 확인
	 * @param request
	 * @description 관리자가 ID/PW 로그인시 auth key를 생성하여 이메일로 보내서 2차 인증
	 * @return
	 */
	public boolean getEmailAuthChk(LoginLogParam param) {
		return loginLogMapper.getEmailAuthChk(param) > 0 ? true : false;
	}
}
