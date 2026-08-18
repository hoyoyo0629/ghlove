package saleson.shop.disposable;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;
import saleson.common.utils.RandomStringUtils;
import saleson.shop.disposable.domain.TempData;

@Slf4j
@Service("tempEncDataService")
public class TempDataServiceImpl implements TempDataService {

	@Autowired
	private TempDataMapper tempDataMapper;
	
	@Override
	public TempData insertTempData(Object object) {
		Gson gson = new Gson();
		String dataId = randomDataId();
		
		TempData tempEncData = new TempData();
		tempEncData.setDataId(dataId);
		tempEncData.setEncData(gson.toJson(object));
		
		tempDataMapper.insertTempData(tempEncData);
		
		return tempEncData;
	}

	@Override
	public Object getTempData(TempData tempDataParam, Class<?> classObj) {
		Gson gson = new Gson();
		
		TempData tempData = tempDataMapper.selectTempData(tempDataParam);
		
		tempDataMapper.deleteTempDataByDataId(tempDataParam);
		tempDataMapper.deleteTempData();
		
		if (tempData == null) {
			return null;
		}
		try {
			return gson.fromJson(tempData.getEncData(), classObj) ;
		} catch (JsonSyntaxException e) {
			log.error(getClass().getName() + " getTempData error :: " + tempDataParam.getDataId() + ", className :: " + classObj.getClass().getName());
			return null;
		}
	}
	
	// JoinController 함수 복사
    private String randomDataId() {
    	String ramdomId = "";
        int result = 0;
        while(result == 0) {
        	SecureRandom random = new SecureRandom();
        	StringBuffer temp = new StringBuffer();
        	temp.append((char) ((int) (random.nextInt(26)) + 97));
        	ramdomId = RandomStringUtils.getRandomString(temp.toString(), 15, 49).toLowerCase();		// 길이 변경
            
            int count = tempDataMapper.selectTempDataCnt(ramdomId);
            if (count == 0) {
            	result = 1;
            }
        }
		return ramdomId;
    }
	
}
