package saleson.api.externalapi;

import com.onlinepowers.framework.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.shop.donation.NgDonationService;
import saleson.shop.donation.domain.UserCntrInfo;
import saleson.shop.externalapi.ExternalApiService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

@RestController("ApiExternalApiController")
@RequestMapping("/api/external")
@Slf4j
public class ExternalApiController {

    @Autowired
    private ExternalApiService externalApiService;
    
    @Autowired
    private NgDonationService ngDonationService;

    /**
     * 결제 상태 조회
     * @param paramMap
     * @return
     */
    @PostMapping("/userCntrInfo")
    public ResponseEntity<Map<String, Object>> userCntrInfo(@RequestBody Map<String, String> paramMap) {
        ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);

        try {
            UserCntrInfo resultMap = externalApiService.getUserCntrInfo(paramMap);

            String mngNo = externalApiService.getMngNo();

            RES_RESULT = ApiResponseEntity.data()
                    .put("result", resultMap)
                    .put("mngNo", mngNo)
                    .put("status", HttpStatus.OK)
                    .ok();
        } catch (UserException e) {
            log.error("/api/external/userCntrInfo UserException 기부자 정보 조회 실패 {} ", e.getStackTrace()[0]);
            log.error(e.getMessage());
            RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "기부자 정보 조회 실패");
        } catch (Exception e) {
            log.error("/api/external/userCntrInfo Exception 기부자 정보 조회 실패 {} ", e.getStackTrace()[0]);
            log.error(e.getMessage());
            RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "기부자 정보 조회 실패");
        }

        return RES_RESULT;
    }

    /**
     * 이텍스 결과 정보 수신
     * @param request
     * @param response
     */
    @RequestMapping("/etaxResult")
    public void etaxResult(HttpServletRequest request, HttpServletResponse response) {
        try {
            String bankCd = request.getParameter("bank_cd");
            String inpGubun = request.getParameter("inp_gubun");
            String sunapYn = request.getParameter("sunap_yn");
            String mngNo = request.getParameter("mng_no");

            log.info("etaxResult ===================================");
            log.info("bankCd : {}", bankCd);
            log.info("inpGubun : {}", inpGubun);
            log.info("sunapYn : {}", sunapYn);
            log.info("mngNo : {}", mngNo);
            log.info("etaxResult ===================================");

        } catch (UserException e) {
            log.error("/api/etaxResult/etaxResult UserException 이텍스 결과 정보 수신 실패 {} ", e.getStackTrace()[0]);
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error("/api/ngdonation/etaxResult Exception 이텍스 결과 정보 수신 실패 {} ", e.getStackTrace()[0]);
            log.error(e.getMessage());
        }
    }

    /**
     * 민간개방 API 수납 확인 처리
     * @param paramMap
     */
    @PostMapping("/sunapSuccess")
    public ResponseEntity<Map<String, Object>> sunapSuccess(@RequestBody Map<String, String> paramMap) throws Exception{
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
            Map<String, Object> resultMap = externalApiService.sunapSuccess(paramMap);

            // 명예기부자 선정 로직 추가
            ngDonationService.insertHonorCntrbtr(paramMap.get("jijacheCd").toString(), Long.valueOf(resultMap.get("userId").toString()));

            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();

    	} catch (UserException e) {
    		log.error("/api/external/sunapSuccess  UserException 수납확인처리 실패 {}", e);
//    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "수납확인처리 실패");
    	}catch (Exception e) {
    		log.error("/api/external/sunapSuccess  Exception 수납확인처리 실패 {}", e);
//    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "수납확인처리 실패");
    	}
        return RES_RESULT;
    }

    /**
     * 민간개방 API 수납 확인 후 완료 처리
     * @param paramMap
     */
    @PostMapping("/sunapSuccess2")
    public ResponseEntity<Map<String, Object>> sunapSuccess2(@RequestBody Map<String, String> paramMap) throws Exception{
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
            Map<String, Object> resultMap = externalApiService.sunapSuccess2(paramMap);

            if ("SUCCESS".equals(resultMap.get("sunap").toString())) {
                // 명예기부자 선정 로직 추가
            	try {
                    ngDonationService.insertHonorCntrbtr(paramMap.get("jijacheCd").toString(), Long.valueOf(resultMap.get("userId").toString()));
            	} catch(NullPointerException e) {
            		log.error("/api/external/sunapSuccess2  insertHonorCntrbtr 명예기부자 문자 발송 실패 {}", e);
            	}
            }

            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();

    	} catch (UserException e) {
    		log.error("/api/external/sunapSuccess2  UserException 수납확인처리 실패 {}", e);
//    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "수납확인처리 실패1");
    	}catch (Exception e) {
    		log.error("/api/external/sunapSuccess2  Exception 수납확인처리 실패 {}", e);
//    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "수납확인처리 실패2");
    	}
        return RES_RESULT;
    }
    
    
}
