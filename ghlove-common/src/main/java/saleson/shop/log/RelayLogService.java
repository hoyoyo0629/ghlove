package saleson.shop.log;

import java.util.HashMap;
import java.util.List;

import saleson.shop.log.support.RelayLogParam;

/**
 * @author hybrid
 *
 */
public interface RelayLogService {
	
	int selectRelayLogListCount(RelayLogParam relayLogParam);
	/**
	 * 연계로그 목록 조회
	 * @param hashMap
	 * @return
	 */
	List<RelayLogParam> selectRelayLogList(RelayLogParam relayLogParam);

}
