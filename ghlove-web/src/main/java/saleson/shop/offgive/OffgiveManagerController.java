package saleson.shop.offgive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.api.common.enumerated.ApiError;
import saleson.common.Const;
import saleson.common.enumeration.IdType;
import saleson.common.file.ExcelDownloadView;
import saleson.common.security.crypto.RsaCryptor;
import saleson.common.userauth.UserAuthService;
import saleson.common.utils.UserUtils;
import saleson.shop.code.domain.Code;
import saleson.shop.designateddonation.DesignatedDonationService;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.donation.DonationVerification;
import saleson.shop.donation.NextBugaRequestDto;
import saleson.shop.donation.NgDonationMapper;
import saleson.shop.donation.NgDonationRelayService;
import saleson.shop.donation.NgDonationService;
import saleson.shop.donation.support.DonationParam;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.donation.support.SidoListParam;
import saleson.shop.give.givestate.GiveStateService;
import saleson.shop.give.givestate.domain.GiveStateTest;
import saleson.shop.offgive.domain.Manager;
import saleson.shop.offgive.domain.Offgive;
import saleson.shop.offgive.support.OffgiveExcelViewPrj;
import saleson.shop.offgive.support.OffgiveExcelViewPrjList;
import saleson.shop.user.LocgovService;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.support.LocgovSearchParam;

@Controller
@RequestMapping("/opmanager/offgive/**")
@RequestProperty(title="오프라인접수", layout="default", template="opmanager")
public class OffgiveManagerController {
	private static final Logger log = LoggerFactory.getLogger(OffgiveManagerController.class);

	@Autowired
	private OffgiveService offgiveService;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private NgDonationService ngDonationService;

	@Autowired
	GiveStateService giveStateService;

	@Autowired
	SequenceService sequenceService;

	@Autowired
	private NgDonationMapper ngDonationMapper;

	@Value("${outconn.admin-addressinfo-url}")
    private String adminAddressinfoUrl;

	@Value("${outconn.contry-now-buga-url}")
    private String contryNowBugaUrl;

	@Value("${outconn.seoul-buga-url}")
    private String seoulBugaUrl;

	@Value("${resource.storage.location}")
	private String storageResourceLocation;

	int TIMEOUT_VALUE = 10000;   // 10초

	@Autowired
	private NgDonationRelayService ngDonationRelayService;

	@Autowired
	private DesignatedDonationService designatedDonationService;

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	DonationVerification donationVerification;


	/**
	 * 오프라인 등록
	 *
	 * @param requestContext
	 * @param offgive
	 * @return
	 */
	@PostMapping("create")
	public JsonView createAction(RequestContext requestContext, Offgive offgive, Model model){
		String cntrSn = "";
		String adminRole = locgovService.getLoginUserAdminRoleCheck();
		log.error("Offgive: {}", offgive.toString());
		try {
			if("OFF".equals(adminRole) || "WCM".equals(adminRole)) {
				cntrSn = offgiveService.insertOffgive(offgive);
			} else {
				return JsonViewUtils.failure("오프라인 담당자만 등록이 가능합니다.");
			}
		} catch(NullPointerException e) {
			log.error(e.getMessage());
			log.error("기탁서 등록 실패 : {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure("기탁서 등록에 실패하였습니다. 고객센터에 문의 하세요.");
		} catch(Exception e) {
			log.error(e.getMessage());
			log.error("기탁서 등록 실패 : {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure("기탁서 등록에 실패하였습니다. 고객센터에 문의 하세요.");
		}
		return JsonViewUtils.success(cntrSn);
	}


	/**
	 * 실명인증 call
	 *
	 * @param requestContext
	 * @param model
	 * @param offgive
	 * @return
	 */
	@PostMapping("sci-call")
	public String sciCall(RequestContext requestContext, Model model, Offgive offgive){
		return ViewUtils.getView("/offgive/sci-call");
	}

	/**
	 * 실명인증 callback
	 *
	 * @param requestContext
	 * @param model
	 * @param offgive
	 * @return
	 */
	@RequestMapping(value = "sci-result", method = {RequestMethod.GET, RequestMethod.POST})
	public String sciResult(RequestContext requestContext, Model model, Offgive offgive){
		return ViewUtils.getView("/offgive/sci-result");
	}

	/**
	 * 사용자 확인
	 *
	 * @param requestContext
	 * @param model
	 * @param offgive
	 * @return
	 */
	@PostMapping("user-check")
	public JsonView userCheck(@RequestParam(name="mberCi") String mberCi){
		HashMap<String, Object> map = offgiveService.getUserByMberCi(mberCi);

		User user = (User) map.get("user");

		if(user.getUserId() == 0) {
			return JsonViewUtils.success();
		}

		return JsonViewUtils.success(map);

	}

	/**
	 * 한도체크
	 *
	 * @param requestContext
	 * @param model
	 * @param offgive
	 * @return
	 */
	@PostMapping("maxCheck")
	public JsonView maxCheck(@RequestParam(name="userId") String userId, @RequestParam(name="mberCi") String mberCi){
		Integer sumCntrAmt = offgiveService.getMaxCheck(userId, mberCi);
		return JsonViewUtils.success(sumCntrAmt);
	}

	/**
	 * 우편번호로 지자체코드 조회
	 *
	 * @param requestContext
	 * @param model
	 * @param offgive
	 * @return
	 */
	@PostMapping("getLocgovMapngCode")
	public JsonView getLocgovMapngCode (@RequestParam(name="locgovCode") String locgovCode){
		HashMap<String, Object> locgovMapngCode  = offgiveService.getLocgovMapngCode(locgovCode);
		return JsonViewUtils.success(locgovMapngCode);
	}

	/**
	 * window 용 암호화 모듈 테스트
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("pdbtest")
	public String pdbtest(Model model) {
		return ViewUtils.view();
	}

	/**
	 * 행정망공동이용센터 주소간단조회 서비스 api 호출
	 * @param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("getUserAddressInfo")
    public JsonView getUserAddressInfo(DonationParam donationParam) throws Exception {
    	HashMap<String, Object> map = new HashMap<>();

        // 요청변수 설정
        Map<String, Object> reqParamMap = new HashMap<>();
        reqParamMap.put("comReqDt", DateUtils.getToday("yyyyMMdd"));
        reqParamMap.put("comReqTm", DateUtils.getToday("HHmmss"));
        reqParamMap.put("id", donationParam.getJuminNo());
        reqParamMap.put("name", donationParam.getUserName());



    	// OPEN API 호출
    	URL url = new URL(adminAddressinfoUrl);
    	final HttpURLConnection http = (HttpURLConnection) url.openConnection();

    	try (AutoCloseable ac = () -> http.disconnect()) {
    		http.setConnectTimeout(TIMEOUT_VALUE);
            http.setReadTimeout(30000);
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            http.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            for (String key : reqParamMap.keySet()) {
                json.put(key, reqParamMap.get(key));
            }
            try (final OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "UTF-8")) {
            	osw.write(json.toString());
            	osw.flush();

            	try (final BufferedReader isr = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"))) {
            		StringBuilder sb = new StringBuilder();
            		String str;
            		while ((str = isr.readLine()) != null) {
            			sb.append(str + "\n");
   	       		}

            		String response = sb.toString();
            		Object objList = JSONValue.parse(response);
            		JSONObject jsonObject = (JSONObject)objList;

   	//            1:성공
   	//            2:주민등록번호오류
   	//            3:성명오류
   	//            99:등록된이용기관이아님
            		map.put("serviceResult", jsonObject.get("serviceResult").toString());
            		if ("1".equals(jsonObject.get("serviceResult").toString())) {
            			HashMap<String, Object> LocgovMapngInfo = offgiveService.getLocgovMapngCode(jsonObject.get("hangkikcd").toString().substring(0,5));
            			map.put("upper_locgov_mapng_code", LocgovMapngInfo.get("upper_locgov_mapng_code"));
            			map.put("locgov_mapng_code", LocgovMapngInfo.get("locgov_mapng_code"));
            			map.put("locgov_nm", LocgovMapngInfo.get("locgov_nm"));
            			map.put("juso", jsonObject.get("juso").toString());
            		} else if ("2".equals(jsonObject.get("serviceResult").toString())) {
            			map.put("serviceResultMsg", "주민등록번호가 잘못 되었습니다.");
            			return JsonViewUtils.success(map);
            		} else if ("3".equals(jsonObject.get("serviceResult").toString())) {
            			map.put("serviceResultMsg", "주민등록상 성함과 다릅니다. 실명인증과 별개로 주민등록상 성함을 입력해 주세요.");
            			return JsonViewUtils.success(map);

            		} else if ("99".equals(jsonObject.get("serviceResult").toString())) {
            			map.put("serviceResultMsg", "등록된 이용기관이 아닙니다. ");
            			return JsonViewUtils.success(map);
            		} else {
            			return JsonViewUtils.failure("serviceResult 코드값 오류");
            		}
            	}

            } catch (IOException e) {
            	log.error(getClass().getName() + " getUserAddressInfo IOException1 =========================", e.getStackTrace()[0]);
            	return JsonViewUtils.failure("행정망공동이용센터 주소 검색에 실패했습니다.");			// 실패했습니다.
            } catch (Exception e) {
            	log.error(getClass().getName() + " getUserAddressInfo Exception =========================", e.getStackTrace()[0]);
            	return JsonViewUtils.failure("행정망공동이용센터 주소 검색에 실패했습니다.");			// 실패했습니다.
            }
    	}

    	return JsonViewUtils.success(map);
    }

	@PostMapping("nextBugaRequest")
    public JsonView nextBugaRequest(NextBugaRequestDto nextBugaRequestDto) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			nextBugaRequestDto.setCntrPathCode("200");
			resultMap = ngDonationRelayService.nextBugaRequest(nextBugaRequestDto);
		} catch (UserException e) {
			resultMap.put("errorMessage", e.getErrorMessage());
			log.error("■■■API■■■ nextBugaRequest UserException : {}",e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			log.error("■■■API■■■ nextBugaRequest Exception : {}",e);
		}
        return JsonViewUtils.success(resultMap);
    }


    /**
	 * 서울시 세외 시스템 부과정보 웹서비스 (임시 - 수정 필요, url 및 연계 방식)
	 * @param
	 * @return
     * @throws IOException
	 */
	@SuppressWarnings("unchecked")
    @PostMapping("/getSdonationCharge")
	public JsonView getSdonationCharge(SeoulParam donationParam) {
    	HashMap<String, Object> map = new HashMap<>();

        try {

        	// 요청변수 설정
            Map<String, Object> reqParamMap = new HashMap<>();

            reqParamMap.put("comReqDt", DateUtils.getToday("yyyyMMdd"));	// 요청일자
            reqParamMap.put("comReqTm", DateUtils.getToday("HHmmss"));		// 요청일시
            reqParamMap.put("systemCd", donationParam.getSystemCd());		// 인터페이스 구분코드
            reqParamMap.put("jijacheCd", donationParam.getJijacheCd());		// 지자체코드

            reqParamMap.put("siguCd", ngDonationMapper.getSiguCdSeoul(donationParam.getJijacheCd()));		// 시구코드
            reqParamMap.put("semokCd", donationParam.getSemokCd());			// 세목코드
            reqParamMap.put("taxYm", DateUtils.getToday("yyyyMM"));			// 과세년월
            reqParamMap.put("taxGubun", donationParam.getTaxGubun());		// 과세구분

            reqParamMap.put("buseoCd", "");		// 부서코드 7자리(세외수입시스템에서 자동 채번) => 개발테스트를 위해 하드코딩
            reqParamMap.put("taxNo", "");			// 6자리(세외수입시스템에서 자동 채번) => 개발테스트를 위해 하드코딩

            reqParamMap.put("sidoCd", donationParam.getSidoCd());			// 시도코드
            reqParamMap.put("napId", donationParam.getNapId());	// 납세자 ID
            reqParamMap.put("napNm", donationParam.getNapNm());			// 납세자명
            reqParamMap.put("napGubun", donationParam.getNapGubun());			// 납세자구분
            reqParamMap.put("taxAmt", Integer.parseInt(donationParam.getTaxAmt()));		// 본세합계
            reqParamMap.put("sise", Integer.parseInt(donationParam.getTaxAmt()));			// 병기항목아닌경우 본세

            reqParamMap.put("guse", 0);			// 병기항목아닌경우 구세
            reqParamMap.put("gukse", 0);			// 병기항목아닌경우 국세
            reqParamMap.put("gigum", 0);			// 기금 => 개발테스트를 위해 하드코딩
            reqParamMap.put("siseIja", 0);			// 시세이자
            reqParamMap.put("guseIja", 0);			// 구세이자
            reqParamMap.put("gukseIja", 0);			// 국세이자
            reqParamMap.put("gigumIja", 0);			// 기금이자
            reqParamMap.put("siseGasanAmt", 0);			// 시세가산금
            reqParamMap.put("guseGasamAmt", 0);			// 구세가산금
            reqParamMap.put("gukseGasanAmt", 0);			// 국세가산금
            reqParamMap.put("gigumGasanAmt", 0);			// 기금가산금

            reqParamMap.put("napMobilNo", "");			// 납세자휴대폰
            reqParamMap.put("napTelNo", "");			// 납세자전화
            reqParamMap.put("napEmail", "");			// 납세자이메일

            reqParamMap.put("resideStatus", donationParam.getResideStatus());	// 거주상태
            reqParamMap.put("mulGubun", donationParam.getMulGubun());		// 물건구분
            reqParamMap.put("mulNm", donationParam.getMulNm());			// 물건명

            reqParamMap.put("mulOcrSiguCd", "");			//
            reqParamMap.put("mulBdongriCd", "");			//
            reqParamMap.put("mulSpcCd", "");			//
            reqParamMap.put("mulBon", "");			//
            reqParamMap.put("mulBu", "");			//
            reqParamMap.put("mulTong", "");			//
            reqParamMap.put("mulBan", "");			//
            reqParamMap.put("mulAptNm", "");			//
            reqParamMap.put("mulDong", "");			//
            reqParamMap.put("mulHosu", "");			//
            reqParamMap.put("mulZipCd", "");			//
            reqParamMap.put("mulZipAddr", "");			//
            reqParamMap.put("mulDtlAddr", "");			//
            reqParamMap.put("hdongCd", "");			//

            reqParamMap.put("bookNo", ngDonationMapper.getBookNoSeoul());		// 원천 시스템의 대장번호(유일 key 값) , 중복체크 => 채번 필요

            reqParamMap.put("hangmok1", "");			//
            reqParamMap.put("hangmok2", "");			//
            reqParamMap.put("hangmok3", "");			//
            reqParamMap.put("hangmok4", "");			//
            reqParamMap.put("hangmok5", "");			//
            reqParamMap.put("hangmok6", "");			//
            reqParamMap.put("gasanRateGubun", "");			//
            reqParamMap.put("specialRate", 0);			//
            reqParamMap.put("specialRateApplySayu", "");			//
            reqParamMap.put("bigo", "");			//
            reqParamMap.put("ocrSiguCd", "");			//
            reqParamMap.put("ocrBuseoCd", "");			//
            reqParamMap.put("etc1", "");			//
            reqParamMap.put("lastWorkId", "");			//
            reqParamMap.put("lastWorkDate", "");			//
            reqParamMap.put("vatAmt", 0);			//
            reqParamMap.put("gasanAmtSkipGubun", "");			//

            reqParamMap.put("sysGubun", donationParam.getSysGubun());		// 시스템 고유번호 LVHT  (임시코드, 별도요청 없으면 수정없이 사용)

            reqParamMap.put("napDzipCd", "");			//
            reqParamMap.put("napDzipAddr", "");			//
            reqParamMap.put("napDdtlAddr", "");			//
            reqParamMap.put("napDrefAddr", "");			//
            reqParamMap.put("etcCm1", "");			//
            reqParamMap.put("etcCm2", "");			//
            reqParamMap.put("etcCm3", "");			//
            reqParamMap.put("etcCm4", "");			//
            reqParamMap.put("etcCm5", "");			//
            reqParamMap.put("napBldBon", "");			//
            reqParamMap.put("napBldBu", "");			//
            reqParamMap.put("napDoroCd", "");			//
            reqParamMap.put("napUndYn", "");			//
            reqParamMap.put("napbuYmd", "");			//

        	// OPEN API 호출
        	URL url = new URL(seoulBugaUrl);
        	final HttpURLConnection http = (HttpURLConnection) url.openConnection();

        	try (AutoCloseable ac = () -> http.disconnect()) {
        		http.setConnectTimeout(TIMEOUT_VALUE);
                http.setReadTimeout(30000);
                http.setDefaultUseCaches(false);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestMethod("POST");

                http.setRequestProperty("Content-Type", "application/json");

                JSONObject json = new JSONObject();
                for (String key : reqParamMap.keySet()) {
                    json.put(key, reqParamMap.get(key));
                }


    			 try (final OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "UTF-8")) {
    				 PrintWriter writer = new PrintWriter(osw);
    				 writer.write(json.toString());
    				 writer.flush();
    			 }


    			try (final InputStreamReader isr = new InputStreamReader(http.getInputStream(), "UTF-8")){
                	try (final BufferedReader br = new BufferedReader(isr)) {

        				StringBuilder sb = new StringBuilder();
        				String str;
        				while ((str = br.readLine()) != null) {
        					sb.append(str + "\n");
        				}
        				String response = sb.toString();
        	            Object objList = JSONValue.parse(response);
        	            JSONObject jsonObject = (JSONObject)objList;

        	            UserDetail userDetail = UserUtils.getUserDetail();

        	            if (!UserUtils.isUserLogin()) {
        	                return JsonViewUtils.failure(ApiError.UNAUTHORIZED.toString());
        	            }

        	            donationParam.setUserId(String.valueOf(userDetail.getUserId()));
        	            donationParam.setEnapbuNo(String.valueOf(jsonObject.get("enapbuNo")));

        	            map.put("elctPayNo", String.valueOf(jsonObject.get("enapbuNo")));
        			}
                }
    		}

        } catch (MalformedURLException e) {
        	log.error("■■■API■■■ getSdonationCharge MalformedURLException userid: {} EnapbuNo: {} {}",donationParam.getUserId(), donationParam.getEnapbuNo(), e);
        	return JsonViewUtils.failure("서울시 세외 시스템 MalformedURLException : \" + \"ERROR-31: 잘못된 URL 오류");			// 실패했습니다.
        } catch (IOException e) {
          	log.error("■■■API■■■ getSdonationCharge IOException userid: {} EnapbuNo: {} {}",donationParam.getUserId(), donationParam.getEnapbuNo(), e);
        } catch (Exception e) {
        	log.error("■■■API■■■ getSdonationCharge Exception userid: {} EnapbuNo: {} {}",donationParam.getUserId(), donationParam.getEnapbuNo(), e);
		}
        return JsonViewUtils.success(map);
    }

    /**
	 * 사용자 지자체코드정보 조회
	 * @param
	 * @param locgovCode
	 * @return
	 */
	@GetMapping("locgov/{userId}")
	public JsonView getLocgovDetails(Offgive searchParam, @PathVariable("userId") Long userId, Model model) {

		return JsonViewUtils.success(offgiveService.getLocgovCodeByUserId(userId));
	}

    /**
	 * 지자체 상세 조회
	 * @param
	 * @param locgovCode
	 * @return
	 */
	@GetMapping("view/{locgovCode}")
	public JsonView getLocgovDetails(@PathVariable("locgovCode") String locgovCode) {
		LocgovSearchParam searchParam = new LocgovSearchParam();
		searchParam.setLocgovCode(locgovCode);

		return JsonViewUtils.success(locgovService.getLocgovDetails(searchParam));
	}

	/**
	 * 오프라인 등록
	 *
	 * @param requestContext
	 * @param offgive
	 * @return
	 */
	@PostMapping("getLocGovLmtt")
	public JsonView getLocGovLmtt(SidoListParam donationParam){

		return JsonViewUtils.success(ngDonationService.getCntrLmtt(donationParam));

	}

	/**
	 * 오프라인 기부취소 신청
	 * @param
	 * @param
	 * @return
	 */
	@PostMapping("/offgiveCancel")
	public JsonView offgiveCancel(@ModelAttribute("searchParam") GiveStateTest searchParam) {
		long reqId = sequenceService.getId("G_CNTR_REQMNG");
		searchParam.setReqId(reqId);
		searchParam.setFrstRegisterId(String.valueOf(UserUtils.getUserId()));
		HashMap<String,Object> resultMap = giveStateService.giveReqmngInsert(searchParam);
		if(resultMap.get("result_code").equals("FAIL")) {
			return JsonViewUtils.failure(org.apache.commons.lang.StringUtils.defaultIfEmpty((String)resultMap.get("result_msg"), "결과코드가 없습니다."));
		}
		return JsonViewUtils.success();
	}
    /**
	 * 오프라인 기부취소 신청취소
	 * @param
	 * @param
	 * @return
	 */
	@PostMapping("/offgiveCancelReq")
	public JsonView offgiveCancelReq(@ModelAttribute("searchParam") GiveStateTest searchParam) {
		searchParam.setLastUpdusrId(String.valueOf(UserUtils.getUserId()));
		HashMap<String,Object> resultMap = giveStateService.giveReqmngCancel(searchParam);
		if(resultMap.get("result_code").equals("FAIL")) {
			return JsonViewUtils.failure(org.apache.commons.lang.StringUtils.defaultIfEmpty((String)resultMap.get("result_msg"), "결과코드가 없습니다."));
		}
		return JsonViewUtils.success();
	}


	/**
	 * 행정망공동이용센터 주소간단조회 서비스 api 호출
	 * @param
	 * @return
	 */
	@PostMapping("getUserAddressInfoForeign")
    public JsonView getUserAddressInfoForeign(HttpServletRequest request, DonationParam donationParam){
		String foreignPrivateKey = "";
		try {
			foreignPrivateKey = request.getSession().getAttribute("foreignPrivateKey").toString();

			request.getSession().removeAttribute("foreignPrivateKey");

			if (!StringUtils.hasLength(foreignPrivateKey)) {
				throw new NullPointerException("외국인등록번호 정보가 없습니다.");
			}
		} catch (NullPointerException e) {
        	log.error("/opmanager/offgive/getUserAddressInfoForeign NullPointerException", e);
			return JsonViewUtils.failure("데이터 복호화에 실패했습니다.");
		}

		Map<String, Object> resultMap = new HashMap<>();

        try {
        	String juminNo = RsaCryptor.decrypt(donationParam.getJuminNo(), foreignPrivateKey);

        	HashMap<String, Object> requestParam = new HashMap<String, Object>();
            requestParam.put("foreignNo", juminNo);	//주민번호
            requestParam.put("foreignName", donationParam.getUserName());	//이름

            String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

            Map<String, Object> apiData = ngDonationRelayService.getForeignApiData(requestParam, locgovCode);

            String rst = apiData.get("rst").toString();
            if ("SUCCESS".equalsIgnoreCase(rst) || "IN_LOCGOV".equalsIgnoreCase(rst)) {
                HashMap<String, Object> locgovMapngInfo = offgiveService.getLocgovMapngCode(apiData.get("locgovCode").toString());
                resultMap.put("upper_locgov_mapng_code", locgovMapngInfo.get("upper_locgov_mapng_code"));
                resultMap.put("locgov_mapng_code", locgovMapngInfo.get("locgov_mapng_code"));
                resultMap.put("locgov_nm", locgovMapngInfo.get("locgov_nm"));
                resultMap.put("rst", rst);
            } else {
            	resultMap = apiData;
            }
        } catch (IOException | NullPointerException e) {
        	log.error("/opmanager/offgive/getUserAddressInfoForeign IOException 행공센 조회 실패", e);
        	resultMap.put("errCode", "BAD_REQUEST");
        	resultMap.put("errMsg", "행정정보공동이용시스템 조회 통신 실패");
        } catch (UserException e) {
        	log.error("/opmanager/offgive/getUserAddressInfoForeign UserException 행공센 조회 실패", e);
        	if (StringUtils.hasLength(e.getErrorCode())) {
            	switch (ApiError.valueOf(e.getErrorCode())) {
    				case NOT_EXIST_AUTH:
    				case NOT_FOUND:
    					resultMap.put("errCode", "BAD_REQUEST");
    					resultMap.put("errMsg", e.getErrorMessage());
    					break;
    				default:
    					resultMap.put("errCode", "BAD_REQUEST");
    					resultMap.put("errMsg", "행정정보공동이용시스템 조회 오류1");
    					break;
    			}
        	} else {
        		resultMap.put("errCode", "BAD_REQUEST");
        		resultMap.put("errMsg", "행정정보공동이용시스템 조회 오류");
        	}
        } catch (Exception e) {
        	log.error("/opmanager/offgive/getUserAddressInfoForeign Exception 행공센 조회 실패", e.getStackTrace()[0]);
        	resultMap.put("errCode", "Exception");
    		resultMap.put("errMsg", "행정정보공동이용시스템 조회 오류");
		}

        return JsonViewUtils.success(resultMap);
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
    public JsonView getPublicKey(HttpServletRequest request) {
		HashMap<String, String> keyPair = RsaCryptor.createKeypairAsString();
		if (keyPair == null) {
			return JsonViewUtils.failure("암호화키 생성에 실패했습니다.");
		}
		String publicKey = keyPair.get("publicKeyStr");
		String privateKey = keyPair.get("privateKeyStr");

		request.getSession().setAttribute("foreignPrivateKey", privateKey);

    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	resultMap.put("publicKey", publicKey);

    	JsonView result = JsonViewUtils.success(resultMap);

    	return result;
    }


	/**
	 * 지정기부 목록 조회
	 * @param params
	 * @return
	 */
    @PostMapping("/getDesignatedDonationList")
    public JsonView selectDesignatedDonationList(DesignatedDonationSearchParam params){
    	JsonView result;

        try {
        	params.setDisplay("FRONT");
        	params.setPage(1);
        	params.setItemsPerPage(9999);
        	params.setPrjStatus("2");
        	params.setConditionType("DONATION");

        	List<DesignatedDonation> list = designatedDonationService.selectDesignatedDonationList(params);

            result = JsonViewUtils.success(list);
        } catch(UserException e){
            result = JsonViewUtils.failure(e.getErrorMessage());
            log.error(getClass().getName() + " selectDesignatedDonationList error :: " + e.getErrorMessage());
        }
        return result;
    }


	/**
	 * 오프라인접수 목록
	 *
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String listPrj(@ModelAttribute("searchParam") Offgive searchParam , Model model) {
		try {
			// 권한 Role
			String role = "";
			User user = UserUtils.getUser();

			String today1 = DateUtils.getToday("yyyyMMdd");
			searchParam.setShFrstRegistPnttmStart(org.apache.commons.lang.StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmStart(), today1));
			searchParam.setShFrstRegistPnttmEnd(org.apache.commons.lang.StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmEnd(), today1));

			Manager manager = offgiveService.getManager(UserUtils.getUser().getUserId());
			for (UserRole userRole : user.getUserRoles()) {
				if (userRole != null && userRole.getAuthority().startsWith("ROLE_ADMIN_")) {
					if ("ROLE_ADMIN_7".equals(userRole.getAuthority())) {
						role = "OFF_MAIN";
						searchParam.setShRceptBankCode(manager.getBankCode());
						searchParam.setShRceptBankNm("");
						break;
					} else if ("ROLE_ADMIN_8".equals(userRole.getAuthority())) {
						role = "OFF_SUB";
						searchParam.setShRceptBankCode(manager.getBankCode());
						searchParam.setShRceptBankNm(manager.getPsitnNm());
						break;
					} else if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority()) || "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())) {
						role = "SYSTEM";
//						searchParam.setShRceptBankCode("");
						searchParam.setShRceptBankNm("");
						break;
					} else if ("ROLE_ADMIN_11".equals(userRole.getAuthority())) {
						role = "OFF_CENTER";
						searchParam.setShRceptBankCode(manager.getBankCode());
						searchParam.setShRceptBankNm(manager.getPsitnNm());
						break;
					} else {
						role = "LOC";
						searchParam.setShRceptBankCode("");
						searchParam.setShRceptBankNm("");
						break;
					}
				}
			}

			if (role.isEmpty()) {
				throw new OpRuntimeException("권한이 없습니다.");
			}

			searchParam.setRole(role);

			// 목록
			int count = 0;

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", Collections.EMPTY_LIST);
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			model.addAttribute("searchParam",searchParam);

			// 조회조건
			String today = DateUtils.getToday(Const.DATE_FORMAT);
			model.addAttribute("today", today);
			model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
			model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
			model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
			model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));


			model.addAttribute("role", role);						// 권한
			model.addAttribute("offBank", manager.getBankCode());	// 소속
			model.addAttribute("psitnNm", manager.getPsitnNm());	// 소속지점
			if (StringUtils.isNotEmpty(manager.getBankCode())) {
				model.addAttribute("offBankNm", CodeUtils.getCode("OFF_BANK_LIST", manager.getBankCode()));
			}
			model.addAttribute("shRceptBankCode", CodeUtils.getCodeList("OFF_BANK_LIST"));	// 오프라인 은행(011:농협은행/012:농축협/035:제주은행)

		} catch(UserException e) {
			log.error(e.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage());
		}

		return ViewUtils.getView("/offgive/list");
	}

	/**
	 * 오프라인접수 등록 화면
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String createPrj(RequestContext requestContext, Model model, Offgive offgive){

		// 접수은행
		Manager manager = offgiveService.getManager(UserUtils.getUser().getUserId());
		if (StringUtils.isNotEmpty(manager.getBankCode())) {
			if (SecurityUtils.hasRole("ROLE_ADMIN_11")) {
//				WlfrCntrMng wlfrCntrMng = welfareCenterService.selectwlfrCntrMngDetail(Long.parseLong(manager.getBankCode()));
				Code code = new Code();
//				code.setLabel(wlfrCntrMng.getPbadmsWlfrCntrNm());
//				code.setId(String.valueOf(wlfrCntrMng.getPbadmsWlfrCntrId()));
				model.addAttribute("offBank", code);
			} else {
				model.addAttribute("offBank", CodeUtils.getCode("OFF_BANK_LIST", manager.getBankCode()));
			}
		}

		String limitAmtString = donationVerification.donationLimitAmt().getDetail();

		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 기부자 행정지 주소
		model.addAttribute("phone", CodeUtils.getCodeList("PHONE"));	// 휴댜폰
		model.addAttribute("psitnNm", manager.getPsitnNm());		// 지점
		model.addAttribute("limitAmtString", limitAmtString);	// 연 최대 한도금액 (한글 단위)
		model.addAttribute("offgive", offgive);

		return ViewUtils.getView("/offgive/form");
	}

	/**
	 * 기부금 모금 상세현황
	 *
	 * @param model
	 * @param searchParam
	 * @return
	 */
	@GetMapping("/detail/{id}")
	public String viewPrj(Model model
						, @PathVariable("id") String id){

		// 상세정보조회
		model.addAttribute("offgive", offgiveService.getOffgive(id));
		model.addAttribute("today", DateUtils.getToday());

		return ViewUtils.getView("/offgive/detail");
	}

	/**
	 * 기탁서 양식 및 약관 출력
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/popup/form-policy")
	@RequestProperty(layout = "base")
	public String formPolicyPrj(RequestContext requestContext, Model model){
		return ViewUtils.getView("/offgive/form-policy");
	}

	/**
	 * 납부신청서
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/popup/form-apply/{cntrSn}")
	@RequestProperty(layout = "base")
	public String formApplyPrj(RequestContext requestContext, Model model, @PathVariable("cntrSn") String cntrSn){
		model.addAttribute("cntrSn", cntrSn);
		return ViewUtils.getView("/offgive/form-apply");
	}

	/**
	 * 목록 출력
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/popup/form-list")
	@RequestProperty(layout = "base")
	public String formListPrj(RequestContext requestContext,
			Model model,
			@RequestParam(name="startDt") String startDt,
			@RequestParam(name="endDt") String endDt,
			@RequestParam(name="rceptBankCode") String rceptBankCode,
			@RequestParam(name="rceptBankNm") String rceptBankNm,
			@RequestParam(name="rceptBankCodeNm") String rceptBankCodeNm,
			@RequestParam(name="cntrSttusCode") String cntrSttusCode,
			@RequestParam(name="cntrAmtStart") String cntrAmtStart,
			@RequestParam(name="cntrAmtEnd") String cntrAmtEnd){



		model.addAttribute("startDt", startDt);
		model.addAttribute("endDt", endDt);

		model.addAttribute("rceptBankCode", rceptBankCode);	// 은행
		model.addAttribute("rceptBankNm", rceptBankNm);	// 지점
		model.addAttribute("rceptBankCodeNm", rceptBankCodeNm);	// 은행명

		model.addAttribute("cntrSttusCode", cntrSttusCode);	// 기부상태
		model.addAttribute("cntrAmtStart", cntrAmtStart);	// 검색 최소금액
		model.addAttribute("cntrAmtEnd", cntrAmtEnd);	// 검색 최대금액
		return ViewUtils.getView("/offgive/form-list");
	}

	/**
	 * 엑셀 다운로드
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping(value="/list/download-excel")
	public ModelAndView downloadExcelProcessByLocgovListPrj(Offgive searchParam) {
		// 권한 Role
		User user = UserUtils.getUser();
		Manager manager = offgiveService.getManager(UserUtils.getUser().getUserId());

		String role = "";

		for (UserRole userRole : user.getUserRoles()) {
			if (userRole != null && userRole.getAuthority().startsWith("ROLE_ADMIN_")) {
				if ("ROLE_ADMIN_7".equals(userRole.getAuthority())) {
					role = "OFF_MAIN";
					searchParam.setShRceptBankCode(manager.getBankCode());
					searchParam.setShRceptBankNm("");
					break;
				} else if ("ROLE_ADMIN_8".equals(userRole.getAuthority())) {
					role = "OFF_SUB";
					searchParam.setShRceptBankCode(manager.getBankCode());
					searchParam.setShRceptBankNm(manager.getPsitnNm());
					break;
				} else if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority()) || "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())) {
					role = "SYSTEM";
//					searchParam.setShRceptBankCode("");
					searchParam.setShRceptBankNm("");
					break;
				} else if ("ROLE_ADMIN_11".equals(userRole.getAuthority())) {
					role = "OFF_CENTER";
					searchParam.setShRceptBankCode(manager.getBankCode());
					searchParam.setShRceptBankNm(manager.getPsitnNm());
					break;
				} else {
					role = "LOC";
					searchParam.setShRceptBankCode("");
					searchParam.setShRceptBankNm("");
					break;
				}
			}
		}

		if (role.isEmpty()) {
			throw new OpRuntimeException("권한이 없습니다.");
		}

		searchParam.setConditionType("EXCEL_DOWNLOAD");
		searchParam.setRole(role);

		// 목록(페이징)
		int totalCount = offgiveService.getOffgiveListCount(searchParam);

		Pagination pagination = Pagination.getInstance(totalCount, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		// SXSSFWorkbook 인스턴스 생성 사유
		// - 엑셀 암호화로 인해 offgiveService에서 response를 종료 불가
		// - offgiveService에서 생성된 workbook을 controller통해 ExcelDownloadView로 일괄 전송
		// - ExcelDownloadView에서 엑셀 파일 생성 및 암호화, 파일 전송 일괄 수행
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		// 사용자 다운로드 파일 명
		// 오프라인기부금접수현황목록_20260115142019.xlsx
		String fileName
			= "오프라인기부금접수현황목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = offgiveService.streamOffgiveList(searchParam, totalCount);
		} finally {
			// ExcelDownloadView에 workbook과 암호화에 사용할 비밀번호를 담아서 전송
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	@GetMapping(value="/list/prj/download-excel")
	public ModelAndView downloadExcelProcessByListPrj() {
		ModelAndView mav = new ModelAndView(new OffgiveExcelViewPrjList());

		DesignatedDonationSearchParam designatedDonationSearchParam = new DesignatedDonationSearchParam();

		designatedDonationSearchParam.setDisplay("FRONT");
		designatedDonationSearchParam.setPage(1);
		designatedDonationSearchParam.setItemsPerPage(9999);
		designatedDonationSearchParam.setPrjStatus("2");
		designatedDonationSearchParam.setConditionType("DONATION");
		designatedDonationSearchParam.setSort("BSNS");

		mav.addObject("list", designatedDonationService.selectDesignatedDonationList(designatedDonationSearchParam));
		mav.addObject("bsnsList", designatedDonationService.getDesignatedDonationBsnsTypes());
		mav.addObject("subBsnsList", designatedDonationService.getDesignatedDonationBsnsSubTypes(""));
		mav.addObject("storageResourceLocation",storageResourceLocation);

		return mav;
	}

	/**
	 * 오프라인접수 목록
	 *
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@PostMapping("/list")
	public String searchListPrj(@ModelAttribute("searchParam") Offgive searchParam , Model model) {
		try {
			// 권한 Role
			String role = "";
			User user = UserUtils.getUser();

			String today1 = DateUtils.getToday("yyyyMMdd");
			searchParam.setShFrstRegistPnttmStart(org.apache.commons.lang.StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmStart(), today1));
			searchParam.setShFrstRegistPnttmEnd(org.apache.commons.lang.StringUtils.defaultIfEmpty(searchParam.getShFrstRegistPnttmEnd(), today1));

			Manager manager = offgiveService.getManager(UserUtils.getUser().getUserId());
			for (UserRole userRole : user.getUserRoles()) {
				if (userRole != null && userRole.getAuthority().startsWith("ROLE_ADMIN_")) {
					if ("ROLE_ADMIN_7".equals(userRole.getAuthority())) {
						role = "OFF_MAIN";
						searchParam.setShRceptBankCode(manager.getBankCode());
						searchParam.setShRceptBankNm("");
						break;
					} else if ("ROLE_ADMIN_8".equals(userRole.getAuthority())) {
						role = "OFF_SUB";
						searchParam.setShRceptBankCode(manager.getBankCode());
						searchParam.setShRceptBankNm(manager.getPsitnNm());
						break;
					} else if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority()) || "ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority())) {
						role = "SYSTEM";
//						searchParam.setShRceptBankCode("");
						searchParam.setShRceptBankNm("");
						break;
					} else if ("ROLE_ADMIN_11".equals(userRole.getAuthority())) {
						role = "OFF_CENTER";
						searchParam.setShRceptBankCode(manager.getBankCode());
						searchParam.setShRceptBankNm(manager.getPsitnNm());
						break;
					} else {
						role = "LOC";
						searchParam.setShRceptBankCode("");
						searchParam.setShRceptBankNm("");
						break;
					}
				}
			}

			if (role.isEmpty()) {
				throw new OpRuntimeException("권한이 없습니다.");
			}

			searchParam.setRole(role);

			// 목록
			int count = offgiveService.getOffgiveListCount(searchParam);

			Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
			searchParam.setPagination(pagination);

			model.addAttribute("list", offgiveService.getOffgiveList(searchParam));
			model.addAttribute("pagination", pagination);
			model.addAttribute("count", count);

			model.addAttribute("searchParam",searchParam);

			// 조회조건
			String today = DateUtils.getToday(Const.DATE_FORMAT);
			model.addAttribute("today", today);
			model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
			model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
			model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
			model.addAttribute("year", DateUtils.addYearMonthDay(today, -1, 0, 0));


			model.addAttribute("role", role);						// 권한
			model.addAttribute("offBank", manager.getBankCode());	// 소속
			model.addAttribute("psitnNm", manager.getPsitnNm());	// 소속지점
			if (StringUtils.isNotEmpty(manager.getBankCode())) {
				model.addAttribute("offBankNm", CodeUtils.getCode("OFF_BANK_LIST", manager.getBankCode()));
			}
			model.addAttribute("shRceptBankCode", CodeUtils.getCodeList("OFF_BANK_LIST"));	// 오프라인 은행(011:농협은행/012:농축협/035:제주은행)

		} catch(UserException e) {
			log.error(e.getMessage());
		} catch(Exception e) {
			log.error(e.getMessage());
		}

		return ViewUtils.getView("/offgive/list");
	}


	/**
	 * 오프라인 기탁서 등록
	 * 대표 답례품 조회
	 */
	@PostMapping("/offRprs")
	@ResponseBody
	public JsonView selectOffRprs(@RequestParam(name="lclgv_cd") String lclgvCd) {
		JsonView result;

		try {
			List<Map<String,Object>> res = offgiveService.selectOffRprs(lclgvCd);
			result = JsonViewUtils.success(res);
		} catch(Exception e) {
			result = JsonViewUtils.failure(e.getMessage());
			log.error(e.getMessage());
		}


		return result;
	}

	@PostMapping("updatePhoneNumber")
	public JsonView updatePhoneNumber(@RequestParam(name="userId") Integer userId, @RequestParam(name="phoneNumber") String phoneNumber) throws Exception{
		JsonView result;
		try {
			int updateResult = offgiveService.updatePhoneNumber(userId, phoneNumber);

			if(updateResult > 0) {
				result = JsonViewUtils.success();
			}else {
				result = JsonViewUtils.failure("연락처 수정을 실패하였습니다");
			}
		} catch(Exception e) {
			result = JsonViewUtils.failure(e.getMessage());
			log.error(e.getMessage());
		}

		return result;

	}
}
