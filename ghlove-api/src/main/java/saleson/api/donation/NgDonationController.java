package saleson.api.donation;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.donation.DonationVerification;
import saleson.shop.donation.NextBugaRequestDto;
import saleson.shop.donation.NgDonationRelayService;
import saleson.shop.donation.NgDonationService;
import saleson.shop.donation.domain.CntrLmtt;
import saleson.shop.donation.support.ContryParam;
import saleson.shop.donation.support.DonationParam;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.donation.support.SidoListParam;
import saleson.shop.mypage.MyPageService;
import saleson.shop.mypage.domain.Cntr;
import saleson.shop.mypage.support.CntrParam;
import saleson.shop.user.domain.UserDetail;

@RestController("ApiNgDonationController")
@RequestMapping("/api/ngdonation")
@Slf4j
public class NgDonationController {

    @Autowired
    NgDonationService ngDonationService;						//기부 공통 서비스

    @Autowired
    NgDonationRelayService ngDonationRelayService;		//연계서비스

    @Autowired
    CodeService codeService;

    @Autowired
    DonationVerification donationVerification;						//기부 공통 서비스

    @Autowired
    private MyPageService myPageService;



    /**
     * <pre>
     * comment       : 기부자 정보 조회
     * preMethodName : userCntrInfo
     * author        : hybrid
     * date          : 2023. 2. 10.
     *
     * </pre>
     * @return
     * ResponseEntity<Map<String,Object>>
     */
    @PostMapping("/userCntrInfo")
    public ResponseEntity<Map<String, Object>> userCntrInfo() {
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
        	Map<String, Object> resultMap = ngDonationService.getUserCntrInfo();
            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();
        } catch (UserException e) {
        	log.error("/api/ngdonation/userCntrInfo UserException 기부자 정보 조회 실패 {} ", e.getStackTrace()[0]);
        	log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "기부자 정보 조회 실패");
		} catch (Exception e) {
        	log.error("/api/ngdonation/userCntrInfo Exception 기부자 정보 조회 실패 {} ", e.getStackTrace()[0]);
        	log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "기부자 정보 조회 실패");
		}

        return RES_RESULT;
    }


    /**
     * <pre>
     * comment       : 시도 목록 조회
     * preMethodName : sidoList
     * author        : hybrid
     * date          : 2023. 2. 13.
     *
     * </pre>
     * @param donationParam
     * @return
     * ResponseEntity<Map<String,Object>>
     */
    @GetMapping("/sidoList")
    public ResponseEntity<Map<String, Object>> sidoList(DonationParam donationParam) {
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
        	Map<String, Object> resultMap = ngDonationService.getSidoList(donationParam);
			RES_RESULT = ApiResponseEntity.data()
					.put("resultList", resultMap)
					.put("status", HttpStatus.OK)
					.ok();
		} catch (UserException e) {
			log.error("/api/ngdonation/sidoList UserException 시도 조회 실패 {} ", e.getStackTrace()[0]);
			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "시도 조회 실패");
		} catch (Exception e) {
			log.error("/api/ngdonation/sidoList Exception 시도 조회 실패 {} ", e.getStackTrace()[0]);
			log.error(e.getMessage());
			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "시도 조회 실패");
		}
		return RES_RESULT;
    }


    /**
     * <pre>
     * comment       : 시군구 목록 조회
     * preMethodName : sigunguList
     * author        : hybrid
     * date          : 2023. 2. 13.
     *
     * </pre>
     * @param donationParam
     * @return
     * ResponseEntity<Map<String,Object>>
     */
    @GetMapping("/sigunguList")
    public ResponseEntity<Map<String, Object>> sigunguList(DonationParam donationParam) {
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
			Map<String, Object> resultMap = ngDonationService.getSigunguList(donationParam);
			RES_RESULT = ApiResponseEntity.data()
					.put("resultList", resultMap)
					.put("status", HttpStatus.OK)
					.ok();
		} catch (UserException e) {
			log.error("/api/ngdonation/sigunguList UserException 시군구 조회 실패 {} ", e.getStackTrace()[0]);
			log.error(e.getMessage());
			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "시군구 조회 실패");
		} catch (Exception e) {
			log.error("/api/ngdonation/sigunguList Exception 시군구 조회 실패 {} ", e.getStackTrace()[0]);
			log.error(e.getMessage());
			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "시군구 조회 실패");
		}
		return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 지자체 상세 정보
     * preMethodName : getLocGovInfo
     * author        : hybrid
     * date          : 2023. 2. 13.
     *
     * </pre>
     * @param donationParam
     * @return
     * ResponseEntity<Map<String,Object>>
     */
    @PostMapping("/locGovInfo")
    public ResponseEntity<Map<String, Object>> locGovInfo(@RequestBody SidoListParam sidoListParam){
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
			Map<String, Object> returnMap = ngDonationService.getLocGovInfo(sidoListParam);

        	//오늘 기부한 지자체건 체크 - 참이면 알림띄우기
//        	int cntrCnt = ngDonationService.selectTodayCntrSigunguCnt(sidoListParam);
        	UserDetail userDetail = UserUtils.getUserDetail();
        	CntrParam cntrParam = new CntrParam();
      	  	cntrParam.setUserId(userDetail.getUserId());
      	  	cntrParam.setLocgovCode(sidoListParam.getCityCode());
            List<Cntr> CList = ngDonationService.getTodayCntrListInfo(cntrParam);//오늘 부가정보생성 건가져오기

            String rslt = "";

            //금일 생성된 부가정보 생성 리스트 결과로만 중복 기부여부를 판단함.(25.08.01 FROM KLID)
            if(CList.size() > 0) {
            	rslt = "dupl";
            }

        	CodeParam codeParam = new CodeParam();
            // 4. 명예회원 명칭 공통코드
    		codeParam.setCodeType("HONOR_STD");
    		List<Code> list = codeService.getCodeChildList(codeParam);

            RES_RESULT = ApiResponseEntity.data()
            		.put("result", returnMap)
					.put("resultStatus", rslt)
            		.put("honorList", list)
            		.put("status", HttpStatus.OK)
            		.ok();
        } catch(UserException e){
        	log.error("/api/ngdonation/locGovInfo UserException 지자체정보 조회 실패");
        	log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지자체정보 조회 실패");

        } catch(SocketTimeoutException e){
        	RES_RESULT = ApiResponseEntity.data()
            		.put("result", "ERROR")
            		.put("honorList", null)
            		.put("status", null)
            		.ok();
        } catch(Exception e){
        	log.error("/api/ngdonation/locGovInfo Exception 지자체정보 조회 실패");
        	log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지자체정보 조회 실패");
        }
        return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 행정망공동이용센터 주소간단조회 서비스 api 호출
     * preMethodName : rsgstadresinfo
     * author        : hybrid
     * date          : 2023. 2. 13.
     *
     * </pre>
     * @param donationParam
     * @return
     * @throws IOException
     * ResponseEntity<Map<String,Object>>
     */
	@PostMapping("/rsgstadresinfo")
    public ResponseEntity<Map<String, Object>> rsgstadresinfo(@RequestBody DonationParam donationParam) throws IOException{
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
        	Map<String, Object> resultMap = ngDonationRelayService.rsgstadresinfo(donationParam);
            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();
        } catch (IOException e) {
        	log.error("/api/ngdonation/rsgstadresinfo IOException 행공센 조회 실패");
        	log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "행공센 조회 실패");
        }  catch (Exception e) {
        	log.error("/api/ngdonation/rsgstadresinfo Exception 행공센 조회 실패");
        	log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "행공센 조회 실패");
        }
        return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 관심지자체 등록 여부 API
     * preMethodName : getIntrstLocgov
     * author        : hybrid
     * date          : 2023. 2. 27.
     *
     * </pre>
     * @param sidoListParam
     * @return
     * ResponseEntity<Map<String,Object>>
     */
    @PostMapping("/getIntrstLocgov")
    public ResponseEntity<Map<String, Object>> getIntrstLocgov(@RequestBody SidoListParam sidoListParam) {
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
			Map<String, Object> resultMap = ngDonationService.getIntrstLocgovInfo(sidoListParam);
			RES_RESULT = ApiResponseEntity.data()
					.put("result", resultMap)
					.put("status", HttpStatus.OK)
					.ok();
		}catch(UserException e){
			log.error("/api/ngdonation/getIntrstLocgov UserException 관심지자체 등록 여부 조회 실패");
			log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "관심지자체 등록 여부 조회 실패");
		} catch(Exception e){
			log.error("/api/ngdonation/getIntrstLocgov Exception 관심지자체 등록 여부 조회 실패");
			log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "관심지자체 등록 여부 조회 실패");
		}

        return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 관심지자체 등록 API
     * preMethodName : setIntrstLocgov
     * author        : hybrid
     * date          : 2023. 2. 27.
     *
     * </pre>
     * @param sidoListParam
     * @return
     * ResponseEntity<Map<String,Object>>
     */
    @PostMapping("/setIntrstLocgov")
    public ResponseEntity<Map<String, Object>> setIntrstLocgov(@RequestBody SidoListParam sidoListParam) {
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
    	try {
			Map<String, Object> resultMap = ngDonationService.setIntrstLocgov(sidoListParam);
			RES_RESULT = ApiResponseEntity.data()
					.put("result", resultMap)
					.put("status", HttpStatus.OK)
					.ok();
		}catch(UserException e){
			log.error("/api/ngdonation/setIntrstLocgov UserException 관심지자체 등록 실패");
			log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "관심지자체 등록 실패");
		} catch(Exception e){
			log.error("/api/ngdonation/setIntrstLocgov Exception 관심지자체 등록 실패");
			log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "관심지자체 등록 실패");
		}

        return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 서울시 세외 시스템 부과정보 API
     * preMethodName : sntrBugaInsert
     * author        : hybrid
     * date          : 2023. 2. 14.
     *
     * </pre>
     * @param donationParam
     * @return
     * @throws IOException
     * ResponseEntity<Map<String,Object>>
     */
	@PostMapping("/sntrBugaInsert")
    public ResponseEntity<Map<String, Object>> sntrBugaInsert(@RequestBody SeoulParam seoulParam) {
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
    	try {
    		UserDetail userDetail = UserUtils.getUserDetail();
            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            }
            seoulParam.setUserId(String.valueOf(userDetail.getUserId()));

    		if(!donationVerification.isDonationNormalAmount(Long.valueOf(seoulParam.getUserId()) , Long.valueOf(seoulParam.getTaxAmt()))) {
    			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "올바르지 않은 기부금");
    			return RES_RESULT;
    		}

			Map<String, Object> resultMap = ngDonationRelayService.sntrBugaInsert(seoulParam);
			RES_RESULT = ApiResponseEntity.data()
					.put("result", resultMap)
					.put("status", HttpStatus.OK)
					.ok();

		} catch (UserException e) {
			log.error("/api/ngdonation/sntrBugaInsert UserException 서울시 세외 시스템 부과정보 등록 실패 {} ", e.getStackTrace()[0]);
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
    	} catch(Exception e){
			log.error("/api/ngdonation/sntrBugaInsert Exception 서울시 세외 시스템 부과정보 등록 실패 {} ", e.getStackTrace()[0]);
			log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "서울시 세외 시스템 부과정보 등록 실패");
		}

        return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 서울시 이택스 - 실시간 수납정보 조회
     * preMethodName : getEtaxSunapInfo
     * author        : hybrid
     * date          : 2023. 2. 14.
     *
     * </pre>
     * @param donationParam
     * @return
     * @throws IOException
     * ResponseEntity<Map<String,Object>>
     */
	@PostMapping("/etaxSunapInfo")
    public ResponseEntity<Map<String, Object>> etaxSunapInfo(@RequestBody SeoulParam donationParam) throws IOException{
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
    	try {
			Map<String, Object> resultMap = ngDonationRelayService.etaxSunapInfo(donationParam);
			RES_RESULT = ApiResponseEntity.data()
					.put("result", resultMap)
					.put("status", HttpStatus.OK)
					.ok();

		} catch(UserException e){
			log.error("/api/ngdonation/etaxSunapInfo UserException 서울시 세외 시스템 부과정보 등록 실패 {} ", e.getStackTrace()[0]);
			log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "서울시 세외 시스템 부과정보 등록 실패");
		}catch(Exception e){
			log.error("/api/ngdonation/etaxSunapInfo Exception 서울시 세외 시스템 부과정보 등록 실패 {} ", e.getStackTrace()[0]);
			log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "서울시 세외 시스템 부과정보 등록 실패");
		}

        return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 기부상태 신고에서 성공 처리
     * 	데이터를 납부일자 및 포인트 처리를 해준다.
     * preMethodName : sunapSuccess
     * author        : hybrid
     * date          : 2023. 2. 14.
     *
     * </pre>
     * @param donationParam
     * @return
     * @throws Exception
     * ResponseEntity<Map<String,Object>>
     */
    @PostMapping("/sunapSuccess")
    public ResponseEntity<Map<String, Object>> sunapSuccess(@RequestBody SeoulParam seoulParam) throws Exception{
    	ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
        try {
        	UserDetail userDetail = UserUtils.getUserDetail();
            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            }
            seoulParam.setUserId(String.valueOf(userDetail.getUserId()));


            Map<String, Object> resultMap = ngDonationService.sunapSuccess(seoulParam);

            // 명예기부자 선정 로직 추가
            ngDonationService.insertHonorCntrbtr(seoulParam.getJijacheCd(), userDetail.getUserId());

            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();

    	} catch (UserException e) {
    		log.error("/api/ngdonation/sunapSuccess  UserException 수납확인처리 실패 {}", e);
    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "수납확인처리 실패");
    	}catch (Exception e) {
    		log.error("/api/ngdonation/sunapSuccess  Exception 수납확인처리 실패 {}", e);
    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "수납확인처리 실패");
    	}
        return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 지방 세외 시스템(현세대) 부과정보 등록 웹서비스
     * preMethodName : enisBugaInsert
     * author        : hybrid
     * date          : 2023. 2. 14.
     *
     * </pre>
     * @param donationParam
     * @return
     * @throws IOException
     * ResponseEntity<Map<String,Object>>
     */
	@PostMapping("/contryBugaInsert")
    public ResponseEntity<Map<String, Object>> contryBugaInsert(@RequestBody ContryParam contryParam) throws IOException {
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
    	try {
            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            }

            Map<String, Object> resultMap = ngDonationRelayService.contryBugaInsert(contryParam);
            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();

    	} catch (IOException e) {
    		log.error("/api/ngdonation/contryBugaInsert  IOException 지방세외 부과등록 실패 : "+contryParam.getTxprNo().substring(6));
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지방세외 부과등록 실패");
    	} catch (Exception e) {
    		log.error("/api/ngdonation/contryBugaInsert  Exception 지방세외 부과등록 실패 : "+contryParam.getTxprNo().substring(6));
    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지방세외 부과등록 실패");
    	}
        return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 지방 세외(현세대) - 실시간 수납정보 조회
     * preMethodName : getContrySunapInfo
     * author        : hybrid
     * date          : 2023. 2. 14.
     *
     * </pre>
     * @param contryParam
     * @return
     * @throws IOException
     * ResponseEntity<Map<String,Object>>
     */
	@PostMapping("/contrySunapInfo")
    public ResponseEntity<Map<String, Object>> contrySunapInfo(@RequestBody ContryParam contryParam) throws IOException{
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
    	try {
            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            }

            Map<String, Object> resultMap = ngDonationRelayService.contrySunapInfo(contryParam);
            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();

    	} catch (IOException e) {
    		log.error("/api/ngdonation/contrySunapInfo  IOException 지방세외 수납확인처리 실패");
    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지방세외 수납확인처리 실패");
    	} catch (Exception e) {
    		log.error("/api/ngdonation/contrySunapInfo  Exception 지방세외 수납확인처리 실패");
    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지방세외 수납확인처리 실패");
    	}
        return RES_RESULT;
    }

	/**
	 * <pre>
	 * comment       : 지방 세외(현세대) - 실시간 수납정보 조회
	 * preMethodName : contryNextSunapInfo
	 * author        : hybrid
	 * date          : 2023. 2. 14.
	 *
	 * </pre>
	 * @param contryParam
	 * @return
	 * @throws IOException
	 * ResponseEntity<Map<String,Object>>
	 */
	@PostMapping("/contryNextSunapInfo")
	public ResponseEntity<Map<String, Object>> contryNextSunapInfo(@RequestBody ContryParam contryParam) throws IOException{
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
		try {
			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			Map<String, Object> resultMap = ngDonationRelayService.contryNextSunapInfo(contryParam);
			RES_RESULT = ApiResponseEntity.data()
					.put("result", resultMap)
					.put("status", HttpStatus.OK)
					.ok();

		} catch (IOException e) {
			log.error("/api/ngdonation/contryNextSunapInfo  UserException 지방세외 수납확인처리 실패");
			log.error(e.getMessage());
			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지방세외 수납확인처리 실패");
		} catch (Exception e) {
			log.error("/api/ngdonation/contryNextSunapInfo  Exception 지방세외 수납확인처리 실패");
			log.error(e.getMessage());
			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지방세외 수납확인처리 실패");
		}
		return RES_RESULT;
	}

    /**
     * <pre>
     * comment       : 금결원 지로 호출
     * preMethodName : giroPay
     * author        : hybrid
     * date          : 2023. 2. 14.
     *
     * </pre>
     * @param params
     * @param req
     * @return
     * @throws IOException
     * ResponseEntity<?>
     */
	@PostMapping("/giroPay")
    public ResponseEntity<Map<String, Object>> giroPay(@RequestBody HashMap<String,String> params, HttpServletRequest req) throws IOException{
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
    	try {
    		String userAgent = req.getHeader("User-Agent").toUpperCase();
    		params.put("userAgent", userAgent);

    		Map<String, Object> resultMap = ngDonationRelayService.giroPay(params);
            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();

    	} catch (IOException e) {
    		log.error("/api/ngdonation/giroPay  IOException 지로 연결 실패");
    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지로 연결 실패");
    	} catch (Exception e) {
    		log.error("/api/ngdonation/giroPay  Exception 지로 연결 실패");
    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지로 연결 실패");
    	}
        return RES_RESULT;
    }

	@PostMapping("/giroPayTest")
    public ResponseEntity<Map<String, Object>> giroPayTest(@RequestBody HashMap<String,String> params, HttpServletRequest req) throws IOException{
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
    	try {
    		String userAgent = req.getHeader("User-Agent").toUpperCase();
    		params.put("userAgent", userAgent);

    		Map<String, Object> resultMap = ngDonationRelayService.giroPayTest(params);
            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();

    	} catch (IOException e) {
    		log.error("/api/ngdonation/giroPayTest  IOException 지로 연결 실패");
    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지로 연결 실패");
    	} catch (Exception e) {
    		log.error("/api/ngdonation/giroPayTest  Exception 지로 연결 실패");
    		log.error(e.getMessage());
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지로 연결 실패");
    	}
        return RES_RESULT;
    }

	/**
	 *  지자체 기부 제한 정보
	 * @param
	 * @return
	 */
    @PostMapping("/getLocGovLmtt")
    public ResponseEntity<Map<String, Object>> getLocGovLmtt(@RequestBody SidoListParam donationParam){
    	ResponseEntity<Map<String, Object>> result = null;

        try {

        	CntrLmtt cntrLmtt = ngDonationService.getCntrLmtt(donationParam);

            result = ApiResponseEntity.data()
            		.put("data", cntrLmtt)
            		.ok();
        } catch(RuntimeException e){
//            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
            log.error(e.getMessage());
        }
        return result;
    }

    /**
	 * <pre>
	 * comment       : 고향사랑 부과정보 등록 요청
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 3. 10.
	 *
	 * </pre>
	 * @param nextGnrParam
	 * @return
	 * @throws IOException
	 * ResponseEntity<Map<String,Object>>
	 */
	@PostMapping("/nextBugaRequest")
    public ResponseEntity<Map<String, Object>> nextBugaRequest(@RequestBody NextBugaRequestDto nextBugaRequestDto) throws IOException {
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String, Object>>(HttpStatus.OK);
    	try {
    		nextBugaRequestDto.setCntrPathCode("100");
    		UserDetail userDetail = UserUtils.getUserDetail();
            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            }
            nextBugaRequestDto.setUserId(userDetail.getUserId());

    		if(!donationVerification.isDonationNormalAmount(Long.valueOf(nextBugaRequestDto.getUserId()) , Long.valueOf(nextBugaRequestDto.getFrstPctAmt()))) {
    			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "올바르지 않은 기부금");
    			return RES_RESULT;
    		}

            Map<String, Object> resultMap = ngDonationRelayService.nextBugaRequest(nextBugaRequestDto);

            RES_RESULT = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();

    	} catch (UserException e) {
    		log.error("/api/ngdonation/nextBugaRequest  UserException", e);
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
    	} catch (Exception e) {
    		log.error("/api/ngdonation/nextBugaRequest  Exception {} " ,e.getStackTrace()[0]);
    		log.error("/api/ngdonation/nextBugaRequest  Exception");
        	RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "부과정보연계 실패");
    	}
        return RES_RESULT;
    }

    /**
     * <pre>
     * comment       : 행정정보공동이용시스템 api 호출
     * preMethodName : rsgstadresinfoForeigner
     * date          : 2023. 8. 21.
     *
     * </pre>
     * @param donationParam
     * @return
     * @throws
     * ResponseEntity<Map<String,Object>>
     */
	@PostMapping("/rsgstadresinfoForeigner")
    public ResponseEntity<Map<String, Object>> rsgstadresinfoForeigner(HttpServletRequest request, @RequestBody DonationParam donationParam) {
		ResponseEntity<Map<String, Object>> rst = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);

        try {
        	String juminNo = ngDonationRelayService.decryptByRsaPrivateKey(donationParam.getJuminNo());

        	donationParam.setJuminNo(juminNo);

        	Map<String, Object> resultMap = ngDonationRelayService.rsgstadresinfoForeigner(donationParam);
			rst = ApiResponseEntity.data()
            		.put("result", resultMap)
            		.put("status", HttpStatus.OK)
            		.ok();
        } catch (IOException | NullPointerException e) {
        	log.error("/api/ngdonation/rsgstadresinfoForeigner IOException 행공센 조회 실패", e);
//        	rst = ApiResponseEntity.error(ApiError.BAD_REQUEST, "행정정보공동이용시스템 조회 실패");
        	Map<String, Object> result = new HashMap<>();
        	result.put("errCode", "BAD_REQUEST");
        	result.put("errMsg", "행정정보공동이용시스템 조회 통신 실패");
			rst = ApiResponseEntity.data()
            		.put("result", result)
            		.put("status", HttpStatus.OK)
            		.ok();
        } catch (UserException e) {
        	log.error("/api/ngdonation/rsgstadresinfoForeigner UserException 행공센 조회 실패", e.getStackTrace()[0]);
        	Map<String, Object> result = new HashMap<>();
        	if (StringUtils.hasLength(e.getErrorCode())) {
            	switch (ApiError.valueOf(e.getErrorCode())) {
    				case NOT_EXIST_AUTH:
    				case NOT_FOUND:
//    		        	rst = ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
    					result.put("errCode", "BAD_REQUEST");
    		        	result.put("errMsg", e.getErrorMessage());
    					break;
    				default:
//    		        	rst = ApiResponseEntity.error(ApiError.BAD_REQUEST, "행정정보공동이용시스템 조회 실패");
    					result.put("errCode", "BAD_REQUEST");
    		        	result.put("errMsg", "행정정보공동이용시스템 조회 오류1");
    					break;
    			}
        	} else {
//	        	rst = ApiResponseEntity.error(ApiError.BAD_REQUEST, "행정정보공동이용시스템 조회 실패");
        		result.put("errCode", "BAD_REQUEST");
            	result.put("errMsg", "행정정보공동이용시스템 조회 오류");
        	}

			rst = ApiResponseEntity.data()
            		.put("result", result)
            		.put("status", HttpStatus.OK)
            		.ok();
        }catch (Exception e) {
        	Map<String, Object> result = new HashMap<>();
        	result.put("errCode", "BAD_REQUEST");
        	result.put("errMsg", "행정정보공동이용시스템 조회 통신 실패");
        	rst = ApiResponseEntity.data()
            		.put("result", result)
            		.put("status", HttpStatus.OK)
            		.ok();
    	}

        return rst;
    }


    /**
     * <pre>
     * comment       : 암호화 키 생성 및 전달
     * preMethodName : getPublicKey
     * date          : 2023. 8. 22.
     *
     * </pre>
     * @param
     * @return
     * @throws
     * ResponseEntity<Map<String,Object>>
     */
	@PostMapping("/getPublicKey")
    public ResponseEntity<Map<String, Object>> getPublicKey(HttpServletRequest request) {
		ResponseEntity<Map<String, Object>> rst = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);

		String publicKey = "";

		try {
			publicKey = ngDonationRelayService.getRsaPublicKey();
		} catch (UserException e) {
			log.error("/api/ngdonation/getPublicKey UserException 암호화키 생성 실패", e.getStackTrace()[0]);

        	if (StringUtils.hasLength(e.getErrorCode())) {
            	switch (ApiError.valueOf(e.getErrorCode())) {
    				case NOT_EXIST_AUTH:
    		        	rst = ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
    					break;
    				default:
    		        	rst = ApiResponseEntity.error(ApiError.BAD_REQUEST, "암호화키 생성 실패");
    					break;
    			}
        	} else {
	        	rst = ApiResponseEntity.error(ApiError.BAD_REQUEST, "암호화키 생성 실패");
        	}
            return rst;
		}

    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("publicKey", publicKey);

		rst = ApiResponseEntity.data()
        		.put("result", resultMap)
        		.put("status", HttpStatus.OK)
        		.ok();

        return rst;
    }

	/**
	 * <pre>
	 * comment       : 지방 세외(현세대) - 실시간 수납정보 조회
	 * preMethodName : localSunapConfirm
	 * author        : ghl004
	 * date          : 2024. 12. 06.
	 *
	 * </pre>
	 * @param NextBugaRequestDto
	 * @return
	 * @throws IOException
	 * ResponseEntity<Map<String,Object>>
	 */
	@PostMapping("/local-sunap-confirm")
	public ResponseEntity<Map<String, Object>> localSunapConfirm(@RequestBody NextBugaRequestDto nextBugaRequestDto) throws IOException{
		ResponseEntity<Map<String, Object>> RES_RESULT = new ResponseEntity<Map<String,Object>>(HttpStatus.OK);
		try {
			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			Map<String, Object> result = ngDonationRelayService.localSunapConfirm(nextBugaRequestDto);
			RES_RESULT = ApiResponseEntity.data()
					.put("result", result)
					.put("status", HttpStatus.OK)
					.ok();

		} catch (IOException e) {
			log.error("/api/ngdonation/local-sunap-confirm  UserException 지방세외 수납확인처리 실패");
			log.error(e.getMessage());
			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지방세외 수납확인처리 실패");
		} catch (Exception e) {
			log.error("/api/ngdonation/local-sunap-confirm  Exception 지방세외 수납확인처리 실패");
			log.error(e.getMessage());
			RES_RESULT = ApiResponseEntity.error(ApiError.BAD_REQUEST, "지방세외 수납확인처리 실패");
		}
		return RES_RESULT;
	}

}
