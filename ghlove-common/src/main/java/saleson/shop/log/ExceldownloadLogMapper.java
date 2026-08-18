package saleson.shop.log;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.log.support.PrivacyLogParam;

@Mapper("exceldownloadLogMapper")
public interface ExceldownloadLogMapper {

	/**
	 * 엑셀다운로드 사유 관리 갯수 조회
	 * @param privacyAccessLog
	 * @return
	 */
	int getExceldownloadLogListCountByParam(PrivacyLogParam privacyLog);

	/**
	 * 엑셀다운로드 사유 관리 조회
	 * @param privacyAccessLog
	 * @return
	 */
	List<PrivacyLogParam> getExceldownloadLogListByParam(PrivacyLogParam privacyLog);
	
	/**
	 * 엑셀다운로드 사유 조회
	 * @param id
	 * @return
	 */
	PrivacyLogParam getExceldownloadLogDetail(Long id);
	
	/**
	 * 엑셀다운로드 로그 존재 여부 체크
	 * @param id
	 * @return
	 */
	int getExcelAccessLogCheck(PrivacyLogParam privacyLog);
}
