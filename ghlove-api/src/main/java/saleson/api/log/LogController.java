package saleson.api.log;

import javax.servlet.http.HttpServletRequest;

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
import saleson.shop.log.ActionLogService;
import saleson.shop.log.domain.ActionLog;

@RestController("ApiLogController")
@RequestMapping("/api/log")
public class LogController {
	private Logger log = LoggerFactory.getLogger(LogController.class);
	
	/** 메뉴 사용 이력 Service */
	@Autowired
	private ActionLogService actionLogService;
	
	/**
	 * 사용자 메뉴 사용 이력 등록
	 * @param actionLog
	 * @return
	 */
	@PostMapping("/action")
	public ResponseEntity<?> saveUserActionLog(HttpServletRequest request, @RequestBody ActionLog actionLog) {
		ResponseEntity<?> result = ApiResponseEntity.data().ok();
		
		try {
			actionLogService.insertUserActionLog(request, actionLog);
			
		} catch (OpRuntimeException e) {
			log.error("[/api/log/action] ERROR : 사용자 메뉴 사용 이력 등록", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}
		
		return result;
	}
}
