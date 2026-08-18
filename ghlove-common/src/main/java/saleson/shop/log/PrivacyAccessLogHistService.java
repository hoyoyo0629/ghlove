package saleson.shop.log;

import java.util.List;

import saleson.shop.log.domain.PrivacyAccessLog;
import saleson.shop.log.support.PrivacyLogHistParam;

public interface PrivacyAccessLogHistService {

	/**
	 * 엑셀다운로드 수정 이력 등록
	 * @param privacyLogHistParam
	 * @return
	 */
	void insertPrivacyAccessLogHist(PrivacyLogHistParam privacyLogHistParam);
	
	/**
	 * 엑셀다운로드 이력 원본 수정
	 * @param privacyAccessLog
	 * @return
	 */
	int updatePrivacyAccessLog(PrivacyAccessLog privacyAccessLog);
	
	/**
	 * 엑셀다운로드 수정 이력 카운트
	 * @param privacyAccessLogId
	 * @return
	 */
	int getPrivacyAccessLogHistListCountByParam(long privacyAccessLogId);

	/**
	 * 엑셀다운로드 수정 이력 조회
	 * @param privacyAccessLogId
	 * @return
	 */
	List<PrivacyLogHistParam> getPrivacyAccessLogHistListByParam(long privacyAccessLogId);

}
