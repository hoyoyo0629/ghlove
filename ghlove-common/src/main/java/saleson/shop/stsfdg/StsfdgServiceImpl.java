package saleson.shop.stsfdg;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.sequence.service.SequenceService;

//import com.onlinepowers.framework.sequence.service.SequenceService;

import lombok.RequiredArgsConstructor;
import saleson.shop.stsfdg.support.StsfdgParam;
import saleson.shop.stylebook.StyleBookServiceImpl;

@RequiredArgsConstructor
@Service("stsfdgService")
public class StsfdgServiceImpl extends EgovAbstractServiceImpl implements StsfdgService{

    private static final Logger log = LoggerFactory.getLogger(StyleBookServiceImpl.class);

    private final StsfdgMapper stsfdgMapper;
    private final SequenceService sequenceService;
    
	@Override
	public int joinSurvey(StsfdgParam stsfdgInfo) {
		int stsfdgSn = sequenceService.getId("OP_STSFDG");
		stsfdgInfo.setStsfdgSn(stsfdgSn);
		return stsfdgMapper.joinSurvey(stsfdgInfo);
	}

}
