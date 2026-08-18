package saleson.api.stsfdg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.OpRuntimeException;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.shop.stsfdg.StsfdgService;
import saleson.shop.stsfdg.support.StsfdgParam;

@RestController("ApiStsfdgController")
@RequestMapping("/api/stsfdg")
public class StsfdgController {
	
	private Logger log = LoggerFactory.getLogger(StsfdgController.class);
	
	@Autowired
	private StsfdgService stsfdgService;
	
	/**
	 * 콘텐츠 만족도 저장
	 * */
	@PostMapping("/joinSurvey")
	public ResponseEntity joinSurvey(@RequestBody StsfdgParam stsfdgInfo) {
		ResponseEntity result = null;
		
		try {
			
			stsfdgService.joinSurvey(stsfdgInfo);
			
		}catch(OpRuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}
		
		return result;
	}

}
