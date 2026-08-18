package saleson.api.qustnr;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.shop.qustnr.QustnrService;
import saleson.shop.qustnr.domain.QestnarResponseDto;
import saleson.shop.qustnr.domain.QustnrRspnsResult;


@RestController("ApiQustnrController")
@RequestMapping("/api/qustnr")
public class QustnrController {
	private static final Logger log = LoggerFactory.getLogger(QustnrController.class);

	@Autowired
	QustnrService qustnrService;

	@GetMapping("{qustnrSn}")
	public ResponseEntity<Map<String, Object>> getQestnar(@PathVariable long qustnrSn) {
		ResponseEntity<Map<String, Object>> result = null;

		try {
			result = ApiResponseEntity.data().put("result", qustnrService.getQustnrByApi(qustnrSn)).ok();

		} catch (RuntimeException e) {
			log.error("getQestnar RuntimeException : {}",e.getStackTrace()[0]);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	@PostMapping("{qustnrSn}")
	public ResponseEntity insertQustnrResult(@PathVariable long qustnrSn, @RequestBody List<QustnrRspnsResult> answerList) {

		ResponseEntity result = null;

		try {

//			String checkResult = qustnrService.checkQustnr(qustnrSn);

//			if (!"SUCC".equals(checkResult)) {
//				return ApiResponseEntity.data().put("message", checkResult).put("status", "FAIL").ok();
//			}

			qustnrService.insertQustnrRspnsResult(answerList);
			result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();

		} catch (RuntimeException e) {
			log.error("insertQustnrResult RuntimeException : {}",e.getStackTrace()[0]);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}
}
