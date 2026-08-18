package saleson.shop.log;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import saleson.shop.log.domain.ActionLog;
import saleson.shop.log.support.ActionLogParam;

@Mapper("actionLogMapper")
public interface ActionLogMapper {

	void insertManagerActionLog(ActionLog actionLog);

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
	 * @param actionLog
	 * @return
	 */
	int insertUserActionLog(ActionLog actionLog);

	/**
	 * 사용자 메뉴 사용 이력 관리 갯수 조회
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
