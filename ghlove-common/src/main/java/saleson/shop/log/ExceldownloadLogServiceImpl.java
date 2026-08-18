package saleson.shop.log;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.context.RequestContext;
import com.privacy.pCrypto;

import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.log.support.PrivacyLogParam;
import saleson.shop.user.domain.SecedeUser;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service("exceldownloadLogService")
public class ExceldownloadLogServiceImpl extends EgovAbstractServiceImpl implements ExceldownloadLogService {

	@Autowired
	private ExceldownloadLogMapper exceldownloadLogMapper;
	
	/**
	 * 엑셀다운로드 사유 관리 갯수 조회
	 * @param privacyAccessLog
	 * @return
	 */
	@Override
	public int getExceldownloadLogListCountByParam(PrivacyLogParam privacyLog) {
		return exceldownloadLogMapper.getExceldownloadLogListCountByParam(privacyLog);
	}
	
	/**
	 * 엑셀다운로드 사유 관리 조회
	 * @param privacyAccessLog
	 * @return
	 */
	@Override
	public List<PrivacyLogParam> getExceldownloadLogListByParam(PrivacyLogParam privacyLog) {
		return exceldownloadLogMapper.getExceldownloadLogListByParam(privacyLog);
	}
	
	/**
	 * 엑셀다운로드 사유 조회
	 * @param id
	 * @return
	 */
	@Override
	public PrivacyLogParam getExceldownloadLogDetail(Long id) {
		return exceldownloadLogMapper.getExceldownloadLogDetail(id);
	}

	// 1분 이내 개인정보 처리 로그 있는지 여부 체크
	@Override
	public boolean existExcelAccessLog(String url) {
		PrivacyLogParam param = new PrivacyLogParam();
		try {
			param.setIp(pCrypto.Encrypt("normal", saleson.common.utils.CommonUtils.getClientIp(), ""));
		} catch(UnsupportedEncodingException e) {
			return false;
		}
		if (ShopUtils.isSellerPage()) {
			param.setLoginType("SELLER");
		} else {
			param.setLoginType("MANAGER");
		}
		param.setUserId(UserUtils.getUser().getUserId());
		param.setUrl(url);
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		param.setSrchEndLogDate(dtf.format(now));
		now = now.minusMinutes(1);
		param.setSrchStartLogDate(dtf.format(now));
		return exceldownloadLogMapper.getExcelAccessLogCheck(param) > 0;
	}
}