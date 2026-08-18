package saleson.shop.log;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import saleson.shop.log.domain.ActionLog;
import saleson.shop.log.support.ActionLogParam;

public interface ActionLogService {

	void insertManagerActionLog(HttpServletRequest request);

	/**
	 * 관리자 메뉴 사용 이력 관리 갯수 조회
	 * @param actionLogParam
	 * @return
	 */
	int getManagerActionLogListCountByParam(ActionLogParam actionLogParam);

	/**
	 * 관리자 메뉴 사용 이력 관리 조회
	 * @param actionLogParam
	 * @return
	 */
	List<ActionLog> getManagerActionLogListByParam(ActionLogParam actionLogParam);

	/**
	 * 사용자 메뉴 사용 이력 등록
	 * @param request 
	 * @param actionLog
	 * @return
	 */
	int insertUserActionLog(HttpServletRequest request, ActionLog actionLog);

	/**
	 * 사용자 메뉴 사용 이력 조회
	 * @param actionLogParam
	 * @return
	 */
	int getUserActionLogListCountByParam(ActionLogParam actionLogParam);

	/**
	 * 사용자 메뉴 사용 이력 관리 조회
	 * @param actionLogParam
	 * @return
	 */
	List<ActionLog> getUserActionLogListByParam(ActionLogParam actionLogParam);
}
