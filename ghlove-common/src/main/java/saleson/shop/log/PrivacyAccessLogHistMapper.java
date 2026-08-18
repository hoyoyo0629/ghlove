package saleson.shop.log;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.log.domain.PrivacyAccessLog;
import saleson.shop.log.support.PrivacyLogHistParam;

@Mapper("privacyAccessLogHistMapper")
public interface PrivacyAccessLogHistMapper {

	/**
	 * 엑셀다운로드 수정 이력 등록
	 * @param PrivacyLogHistParam
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
