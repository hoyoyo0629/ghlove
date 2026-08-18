package saleson.shop.log;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.onlinepowers.framework.web.domain.SearchParam;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.common.utils.CommonUtils;
import saleson.shop.donation.NgDonationMapper;
import saleson.shop.log.support.PrivacyLogParam;
import saleson.shop.log.support.RelayLogParam;
import saleson.shop.user.domain.SecedeUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("relayLogService")
public class RelayLogServiceImpl extends EgovAbstractServiceImpl implements RelayLogService {

	@Autowired
	private NgDonationMapper ngDonationMapper;

	@Override
	public int selectRelayLogListCount(RelayLogParam relayLogParam) {
		int count = 0;
		try {
			count = ngDonationMapper.selectRelayLogListCount(relayLogParam);
		} catch(NullPointerException e) {
			e.getMessage();
		}
		return count;
	}

	@Override
	public List<RelayLogParam> selectRelayLogList(RelayLogParam relayLogParam) {
		List<RelayLogParam>  resultList = new ArrayList<>();
		try {
			resultList = ngDonationMapper.selectRelayLogList(relayLogParam);
		}catch(NullPointerException e) {
			e.getMessage();
		}
		return resultList;
	}
}