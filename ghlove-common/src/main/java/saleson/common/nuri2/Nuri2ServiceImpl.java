package saleson.common.nuri2;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("nuri2Service")
public class Nuri2ServiceImpl implements Nuri2Service{

	private static final Logger log = LoggerFactory.getLogger(Nuri2ServiceImpl.class);

	@Autowired
	Nuri2Mapper nuri2Mapper;

    @Value("${ums.nuri2.sender-key}")
    private String SENDER_KEY;

    @Override
	public void insertAlimtalk(Nuri2NrmsgData nuri2NrmsgData) {

    	try {
    		// SENDER_KEY
    		nuri2NrmsgData.setAltSenderKey(SENDER_KEY);

    		// 데이터 insert
    		nuri2Mapper.insertTifAlimtalkInfo(nuri2NrmsgData);

    	}catch(Exception e) {
            log.error("insertAlimtalk Error {} : ", e);
    	}
	};

	@Override
	public List<Map<String,Object>> selectUserInfoList(){
		return nuri2Mapper.selectUserInfoList();
	}

	@Override
	public List<Map<String,Object>> selectPresentOrderList(){
		return nuri2Mapper.selectPresentOrderList();
	}

}
