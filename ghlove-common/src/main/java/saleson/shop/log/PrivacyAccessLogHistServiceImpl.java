package saleson.shop.log;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.shop.log.domain.PrivacyAccessLog;
import saleson.shop.log.support.PrivacyLogHistParam;

import java.util.ArrayList;
import java.util.List;

@Service("privacyAccessLogHistService")
public class PrivacyAccessLogHistServiceImpl extends EgovAbstractServiceImpl implements PrivacyAccessLogHistService {

	@Autowired
	private PrivacyAccessLogHistMapper privacyAccessLogHistMapper;

	@Override
	public void insertPrivacyAccessLogHist(PrivacyLogHistParam privacyLogHistParam) {

		privacyAccessLogHistMapper.insertPrivacyAccessLogHist(privacyLogHistParam);
	}

	@Override
	public int updatePrivacyAccessLog(PrivacyAccessLog privacyAccessLog) {

		return privacyAccessLogHistMapper.updatePrivacyAccessLog(privacyAccessLog);
	}

	@Override
	public int getPrivacyAccessLogHistListCountByParam(long privacyAccessLogId) {

		int result = privacyAccessLogHistMapper.getPrivacyAccessLogHistListCountByParam(privacyAccessLogId);

		return result;
	}

	@Override
	public List<PrivacyLogHistParam> getPrivacyAccessLogHistListByParam(long privacyAccessLogId) {
		List<PrivacyLogHistParam>  resultList = new ArrayList<>();
		try {
			resultList = privacyAccessLogHistMapper.getPrivacyAccessLogHistListByParam(privacyAccessLogId);
		}catch(NullPointerException e) {
			e.getMessage();
		}
		return resultList;
	}
}