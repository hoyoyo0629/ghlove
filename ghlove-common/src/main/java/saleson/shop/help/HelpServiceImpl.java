package saleson.shop.help;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.shop.help.domain.Help;
import saleson.shop.help.support.HelpParam;

@Service("helpService")
public class HelpServiceImpl extends EgovAbstractServiceImpl implements HelpService {
	
	/** 도움말 Mapper */
	@Autowired
	HelpMapper helpMapper;
	
	/**
	 * 도움말 목록 갯수 조회
	 * @param helpParam
	 * @return
	 */
	@Override
	public int getFrontHelpListCount(HelpParam helpParam) {
		return helpMapper.getFrontHelpListCount(helpParam);
	}
	
	/**
	 * 도움말 목록 조회
	 * @param helpParam
	 * @return
	 */
	@Override
	public List<Help> getFrontHelpList(HelpParam helpParam) {
		return helpMapper.getFrontHelpList(helpParam);
	}
	
	/**
	 * 도움말 상세 조회
	 * @param helpParam
	 * @return
	 */
	@Override
	public Help getFrontHelpDetail(HelpParam helpParam) {
		helpMapper.addHitCount(helpParam.getMnlSn());
		return helpMapper.getFrontHelpDetail(helpParam);
	}
	
	/**
	 * 도움말 파일 정보 조회
	 * @param helpParam
	 * @return
	 */
	@Override
	public Help getFrontHelpFileInfo(HelpParam helpParam) {
		return helpMapper.getFrontHelpFileInfo(helpParam);
	}
}
