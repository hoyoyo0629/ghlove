package saleson.shop.log;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.log.domain.LoginLog;
import saleson.shop.log.support.LoginLogParam;

@Mapper("loginLogMapper")
public interface LoginLogMapper {

	/**
	 * 로그인 로그 등록
	 * @param loginLog
	 * @return
	 */
	public int insertLoginLog(LoginLog loginLog);

	/**
	 * 로그인 로그 목록 조회
	 * @param param
	 * @return
	 */
	public int getLoginLogListCountByParam(LoginLogParam param);

	/**
	 * 로그인 로그 목록 조회
	 * @param param
	 * @return
	 */
	public List<LoginLog> getLoginLogListByParam(LoginLogParam param);

	/**
	 * 사용자 로그인 로그 등록
	 * @param loginLog
	 * @return
	 */
	int insertLoginLogByUser(LoginLog loginLog);

	/**
	 * 선택된 로그인 로그 날짜 범위 조회
	 * @param loginLogId
	 * @return
	 */
	public LoginLog getLoginLogDetailsDateInfo(Integer loginLogId);

	/**
	 * 사용자 로그인 로그 목록 갯수 조회
	 * @param loginLogParam
	 * @return
	 */
	public int getUserLoginLogListCountByParam(LoginLogParam loginLogParam);

	/**
	 * 사용자 로그인 로그 목록 조회
	 * @param loginLogParam
	 * @return
	 */
	public List<LoginLog> getUserLoginLogListByParam(LoginLogParam loginLogParam);

	/**
	 * 선택된 사용자 로그인 로그 날짜 범위 조회
	 * @param loginLogId
	 * @return
	 */
	public LoginLog getUserLoginLogDetailsDateInfo(Integer loginLogId);



	/**
	 * 관리자 로그인시 이메일에 보낼 인증번호 생성 & 저장 & 이메일 발송
	 * @param loginId
	 * @description 관리자가 ID/PW 로그인시 auth key를 생성하여 이메일로 보내서 2차 인증
	 * @return 인증번호
	 */
	public void insertLoginEmailLog(LoginLogParam param);



	/**
	 * 인증번호 확인
	 * @param request
	 * @description 관리자가 ID/PW 로그인시 auth key를 생성하여 이메일로 보내서 2차 인증
	 * @return
	 */
	int getEmailAuthChk(LoginLogParam param);
}
