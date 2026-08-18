package saleson.shop.nhapi;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.shop.analysis.AnalysisMapper;
import saleson.shop.analysis.domain.AnalysisVo;
import saleson.shop.donation.NgDonationMapper;

@Service("nhApiBatchService")
public class NhApiBatchServiceImpl extends EgovAbstractServiceImpl implements NhApiBatchService {
	private static final Logger logger = LoggerFactory.getLogger(NhApiBatchServiceImpl.class);

	@Autowired
	private AnalysisMapper analysisMapper;

	@Autowired
	private NgDonationMapper ngDonationMapper;

	@Override
	public void setUserCntrForNh() {
		try {
			HashMap<String, Object> searchMap = new HashMap<>();
			AnalysisVo regVo = new AnalysisVo();

			Calendar day = Calendar.getInstance();
		    day.add(Calendar.DATE , -1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		    String beforeDate = sdf.format(day.getTime());
			searchMap.put("sttemntPayDe", beforeDate);


			List<HashMap<String, Object>> userCntrList = ngDonationMapper.selectUserCntrForNhList(searchMap);

			analysisMapper.deleteUserCntrForNh();

			for(HashMap<String, Object> regMap : userCntrList) {
				try {
					regVo = new AnalysisVo();
					regVo.setUserId(new BigInteger(regMap.get("userId").toString()));
					regVo.setMberCi(regMap.get("mberCi").toString());
					regVo.setSttemntPayDe(regMap.get("sttemntPayDe").toString());
					regVo.setCntrAmt(new BigInteger(regMap.get("cntrAmt").toString()));
					analysisMapper.insertUserCntrForNh(regVo);
				} catch(NullPointerException e) {
					logger.debug("===========insertUserCntrForNh NullPointerException {} {} ",regVo, e);
					logger.error(e.getMessage());
				} catch(Exception e) {
					logger.debug("===========insertUserCntrForNh {} {} ",regVo, e);
					logger.error(e.getMessage());
				}
			}

		} catch(NullPointerException e) {
			logger.error("setUserCntrForNh NullPointerException {}", e);
			logger.error(e.getMessage());
		} catch(Exception e) {
			logger.error("setUserCntrForNh Exception {}", e);
			logger.error(e.getMessage());
		}
	}

	@Override
	public void setUserCntrLocForNh() {
		try {
			HashMap<String, Object> searchMap = new HashMap<>();
			AnalysisVo regVo = new AnalysisVo();

			Calendar day = Calendar.getInstance();
		    day.add(Calendar.DATE , -1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		    String beforeDate = sdf.format(day.getTime());
			searchMap.put("sttemntPayDe", beforeDate);

			List<HashMap<String, Object>> userCntrLocgovList = ngDonationMapper.selectUserCntrLocgovForNhList(searchMap);

			analysisMapper.deleteUserCntrLocgovForNh();

			for(HashMap<String, Object> regMap : userCntrLocgovList) {
				try {
					regVo = new AnalysisVo();
					regVo.setUserId(new BigInteger(regMap.get("userId").toString()));
					regVo.setMberCi(regMap.get("mberCi").toString());
					regVo.setSttemntPayDe(regMap.get("sttemntPayDe").toString());
					regVo.setCntrLocgovCode(regMap.get("cntrLocgovCode").toString());
					regVo.setCntrAmt(new BigInteger(regMap.get("cntrAmt").toString()));

					analysisMapper.insertUserCntrLocgovForNh(regVo);
				} catch(NullPointerException e) {
					logger.debug("===========insertUserCntrLocgovForNh NullPointerException {} {} ",regVo, e);
					logger.error(e.getMessage());
				} catch(Exception e) {
					logger.debug("===========insertUserCntrLocgovForNh {} {} ",regVo, e);
					logger.error(e.getMessage());
				}
			}

		} catch(NullPointerException e) {
			logger.error("setUserCntrLocForNh NullPointerException {}", e);
			logger.error(e.getMessage());
		} catch(Exception e) {
			logger.error("setUserCntrLocForNh Exception {}", e);
			logger.error(e.getMessage());
		}
	}

	@Override
	public void setLocForNh() {
		try {
			AnalysisVo regVo = new AnalysisVo();
			List<HashMap<String, Object>> locgovList = ngDonationMapper.selectLocgovForNhList();
			analysisMapper.deleteLocgovForNh();

			for(HashMap<String, Object> regMap : locgovList) {
				try {
					regVo = new AnalysisVo();
					regVo.setLocgovCode(regMap.get("locgovCode").toString());
					regVo.setLocgovNm(regMap.get("locgovNm").toString());
					regVo.setUpperLocgovCode(regMap.get("upperLocgovCode").toString());
					regVo.setUpperLocgovNm(regMap.get("upperLocgovNm").toString());
					analysisMapper.insertLocgovForNh(regVo);
				} catch(NullPointerException e) {
					logger.debug("===========insertLocgovForNh NullPointerException {} {} ",regVo, e);
					logger.error(e.getMessage());
				} catch(Exception e) {
					logger.debug("===========insertLocgovForNh Exception {} {} ",regVo, e);
					logger.error(e.getMessage());
				}
			}
		} catch(NullPointerException e) {
			logger.error("setLocForNh NullPointerException {}", e);
			logger.error(e.getMessage());
		} catch(Exception e) {
			logger.error("setLocForNh Exception {}", e);
			logger.error(e.getMessage());
		}
	}

}
