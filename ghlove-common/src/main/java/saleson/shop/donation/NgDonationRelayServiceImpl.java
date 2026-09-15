package saleson.shop.donation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.DateUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.api.common.enumerated.ApiError;
import saleson.common.security.crypto.RsaCryptor;
import saleson.common.utils.AES256Utils;
import saleson.common.utils.UserUtils;
import saleson.shop.designateddonation.DesignatedDonationService;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.donation.domain.DoLocGovInfo;
import saleson.shop.donation.domain.TempPrivateKeyInfo;
import saleson.shop.donation.support.ContryParam;
import saleson.shop.donation.support.DonationParam;
import saleson.shop.donation.support.ForeignStatusParam;
import saleson.shop.donation.support.GiroParam;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.support.SSLSocketFactoryMaker;

@Service("ngDonationRelayService")
@RequiredArgsConstructor
@Slf4j
public class NgDonationRelayServiceImpl extends EgovAbstractServiceImpl implements NgDonationRelayService {

	@Value("${outconn.admin-addressinfo-url}")
    private String adminAddressinfoUrl;

	@Value("${outconn.seoul-buga-url}")
    private String seoulBugaUrl;

	@Value("${outconn.seoul-sunap-url}")
    private String seoulSunapUrl;

	@Value("${outconn.korea-api.loginUrl}")
    private String koreaApiLoginUrl;

	@Value("${outconn.korea-api.loginIp}")
    private String koreaApiLoginIp;

	@Value("${outconn.korea-api.key}")
    private String koreaApiKey;

    @Value("${outconn.korea-api.id}")
    private String koreaApiId;

    @Value("${outconn.korea-api.password}")
    private String koreaApiPassword;

    @Value("${outconn.korea-api.actionUrl}")
    private String koreaApiActionUrl;

    @Value("${outconn.contry-now-buga-url}")
    private String contryNowBugaUrl;

    @Value("${outconn.contry-now-sunap-url}")
    private String contryNowSunapUrl;

    @Value("${outconn.giro-aes256-key}")
    private String aes256Key;

    @Value("${outconn.giro-aes256-iv}")
    private String aes256Iv;

    @Value("${outconn.giro-url}")
    private String giroUrl;

    @Value("${next.buga-request-url}")
    private String nextBugaRequestUrl;

    @Value("${next.sunap-info-request-url}")
    private String nextSunapInfoRequestUrl;

    private final NgDonationMapper ngDonationMapper;


	private static String API_RESONSE = "api_response";

	private int TIMEOUT_VALUE = 10000;   // 10초

	private boolean CERTIFICATE = true;


	private static String LINK_TRGT_CD = "1741000HLE01001";

	private SSLSocketFactory _sslSockFactory;

	@Autowired
	private TempPrivateKeyInfoRepository tempPrivateKeyInfoRepository;

	// 행정정보 공동이용시스템 URL
	@Value("${outconn.in-link-url}")
    private String inLinkUrl;

	@Autowired
	private DesignatedDonationService designatedDonationService;


	@Override
	public Map<String, Object> rsgstadresinfo(DonationParam donationParam) throws Exception {
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();
		String mapLocgov = "";
		String bassAdres = "";
		UserDetail userDetail = UserUtils.getUserDetail();
        userDetail = ngDonationMapper.getUserDetail(userDetail.getUserId());
        HashMap<String, Object> API_REQUEST = new HashMap<String, Object>();
        API_REQUEST.put("comReqDt", DateUtils.getToday("yyyyMMdd"));		//요청일자
        API_REQUEST.put("comReqTm", DateUtils.getToday("HHmmss"));		//요청일시
        if ("500".equals(userDetail.getLoginPathCode()) && !"1".equals(userDetail.getBirthdayType()))  {
        	API_REQUEST.put("id", donationParam.getJuminNo());	//주민번호
        } else {
            API_REQUEST.put("id", userDetail.getBirthday().toString().substring(2)+donationParam.getJuminNo());	//주민번호
        }
        API_REQUEST.put("name", UserUtils.getUser().getUserName());	//이름

    	String response = restFulToRelayServer(adminAddressinfoUrl, API_REQUEST, "");
        Object objList = JSONValue.parse(response);
        JSONObject jsonObject = (JSONObject)objList;

        String serviceResult =  StringUtils.defaultIfEmpty((String) jsonObject.get("serviceResult"), "");

        if(serviceResult.equals("1")) {
        	String locgovCode = jsonObject.get("hangkikcd").toString().substring(0,5);
        	mapLocgov = ngDonationMapper.getMappingLocgovCode(locgovCode);

        	LocGovInfo sidoListParam = new LocGovInfo();
        	sidoListParam.setLocgovCode(mapLocgov);
        	DoLocGovInfo locGovInfo= ngDonationMapper.getLocGovInfo(sidoListParam);
        	bassAdres = locGovInfo.getBassAdres();

            if ("500".equals(userDetail.getLoginPathCode()) && !"1".equals(userDetail.getBirthdayType()))  {		// 카카오 가입자 양력생일로 세팅하여 차후 생년월일 변경 못하게 수정
            	String genderCode = donationParam.getJuminNo().substring(6, 7);
            	if ("1".equals(genderCode) || "2".equals(genderCode)) {
            		userDetail.setBirthday("19" + donationParam.getJuminNo().substring(0, 6));
            		ngDonationMapper.updateKakaoBirthdayInfo(userDetail);
            	} else if ("3".equals(genderCode) || "4".equals(genderCode)) {
            		userDetail.setBirthday("20" + donationParam.getJuminNo().substring(0, 6));
            		ngDonationMapper.updateKakaoBirthdayInfo(userDetail);
            	} else {
            		throw new UserException("생년월일 수정 오류");
            	}
            }
        }

        SERVICE_RELAY_RESULT.put(API_RESONSE, response);
        SERVICE_RELAY_RESULT.put("bassAdres", bassAdres);
        SERVICE_RELAY_RESULT.put("mapLocgov", mapLocgov);
		return SERVICE_RELAY_RESULT;
	}

	@Override
	public Map<String, Object> sntrBugaInsert(SeoulParam seoulParam) throws Exception {
		HashMap<String, Object> API_REQUEST = new HashMap<>();
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();

		if (!"0".equals(seoulParam.getForeignStatusCode())) {				// 외국인일 경우 만료날짜 한번 더 체크
			DonationParam param = new DonationParam();
			param.setUserName(seoulParam.getNapNm());
			param.setJuminNo(seoulParam.getNapId().substring(6));
			param.setLocgovCode(seoulParam.getJijacheCd());
			Map<String, Object> rst = rsgstadresinfoForeigner(param);
			String rstCode = rst.get("rst").toString();
			if (!"SUCCESS".equalsIgnoreCase(rstCode)) {			// 성공이 아닐 경우
				String errMsg = "외국인 기부 가능 상태가 아닙니다.";

				switch (rstCode) {
					case "NONE_DATA" :
						errMsg = "외국인 거소 조회 정보가 없습니다.";
						break;
					case "EXPIRED" :
						errMsg = "외국인 거소 만료일이 지났습니다.";
						break;
					case "IN_LOCGOV" :
						errMsg = "외국인 거소 자치구에는 기부가 불가능합니다.";
						break;
				}
				errMsg += " USER_ID :: " + UserUtils.getUser().getUserId();
				throw new UserException(errMsg);
			}
		}

		if (seoulParam.getPrjId() > 0) {
			DesignatedDonationSearchParam searchParam = new DesignatedDonationSearchParam();
			searchParam.setPrjId(seoulParam.getPrjId());
			searchParam.setCheckDonation(true);
//			searchParam.setLocgovCode(seoulParam.getJijacheCd());
			DesignatedDonation designatedDonation = designatedDonationService.selectDesignatedDonationDetail(searchParam);
			if (designatedDonation == null) {
				throw new UserException("현재 기부 가능한 특정사업에 기부하기 사업이 아닙니다.");
			}
			if (!designatedDonation.getSUCCESS().equals(designatedDonation.getResultCode())) {
				SERVICE_RELAY_RESULT.put("resultCode", designatedDonation.getResultCode());
				return SERVICE_RELAY_RESULT;
			}
		}

		API_REQUEST.put("comReqDt", DateUtils.getToday("yyyyMMdd"));	// 요청일자
        API_REQUEST.put("comReqTm", DateUtils.getToday("HHmmss"));		// 요청일시
        API_REQUEST.put("systemCd", seoulParam.getSystemCd());		// 인터페이스 구분코드
        API_REQUEST.put("jijacheCd", seoulParam.getJijacheCd());		// 지자체코드
        API_REQUEST.put("siguCd", ngDonationMapper.getSiguCdSeoul(seoulParam.getJijacheCd()));		// 시구코드
        API_REQUEST.put("semokCd", seoulParam.getSemokCd());			// 세목코드
        API_REQUEST.put("taxYm", DateUtils.getToday("yyyyMM"));			// 과세년월
        API_REQUEST.put("taxGubun", seoulParam.getTaxGubun());		// 과세구분
        API_REQUEST.put("buseoCd", "");		// 부서코드 7자리(세외수입시스템에서 자동 채번) => 개발테스트를 위해 하드코딩
        API_REQUEST.put("taxNo", "");			// 6자리(세외수입시스템에서 자동 채번) => 개발테스트를 위해 하드코딩
        API_REQUEST.put("sidoCd", seoulParam.getSidoCd());			// 시도코드
        API_REQUEST.put("napId", seoulParam.getNapId());	// 납세자 ID
        API_REQUEST.put("napNm", seoulParam.getNapNm());			// 납세자명
        API_REQUEST.put("napGubun", seoulParam.getNapGubun());			// 납세자구분
        API_REQUEST.put("taxAmt", Integer.parseInt(seoulParam.getTaxAmt()));		// 본세합계
        API_REQUEST.put("sise", Integer.parseInt(seoulParam.getTaxAmt()));			// 병기항목아닌경우 본세
        API_REQUEST.put("guse", 0);			// 병기항목아닌경우 구세
        API_REQUEST.put("gukse", 0);			// 병기항목아닌경우 국세
        API_REQUEST.put("gigum", 0);			// 기금 => 개발테스트를 위해 하드코딩
        API_REQUEST.put("siseIja", 0);			// 시세이자
        API_REQUEST.put("guseIja", 0);			// 구세이자
        API_REQUEST.put("gukseIja", 0);			// 국세이자
        API_REQUEST.put("gigumIja", 0);			// 기금이자
        API_REQUEST.put("siseGasanAmt", 0);			// 시세가산금
        API_REQUEST.put("guseGasamAmt", 0);			// 구세가산금
        API_REQUEST.put("gukseGasanAmt", 0);			// 국세가산금
        API_REQUEST.put("gigumGasanAmt", 0);			// 기금가산금
        API_REQUEST.put("napMobilNo", "");			// 납세자휴대폰
        API_REQUEST.put("napTelNo", "");			// 납세자전화
        API_REQUEST.put("napEmail", "");			// 납세자이메일
        API_REQUEST.put("resideStatus", seoulParam.getResideStatus());	// 거주상태
        API_REQUEST.put("mulGubun", seoulParam.getMulGubun());		// 물건구분
        API_REQUEST.put("mulNm", seoulParam.getMulNm());			// 물건명
        API_REQUEST.put("mulOcrSiguCd", "");			//
        API_REQUEST.put("mulBdongriCd", "");			//
        API_REQUEST.put("mulSpcCd", "");			//
        API_REQUEST.put("mulBon", "");			//
        API_REQUEST.put("mulBu", "");			//
        API_REQUEST.put("mulTong", "");			//
        API_REQUEST.put("mulBan", "");			//
        API_REQUEST.put("mulAptNm", "");			//
        API_REQUEST.put("mulDong", "");			//
        API_REQUEST.put("mulHosu", "");			//
        API_REQUEST.put("mulZipCd", "");			//
        API_REQUEST.put("mulZipAddr", "");			//
        API_REQUEST.put("mulDtlAddr", "");			//
        API_REQUEST.put("hdongCd", "");			//
        API_REQUEST.put("bookNo", ngDonationMapper.getBookNoSeoul());		// 원천 시스템의 대장번호(유일 key 값) , 중복체크 => 채번 필요
        API_REQUEST.put("hangmok1", "");			//
        API_REQUEST.put("hangmok2", "");			//
        API_REQUEST.put("hangmok3", "");			//
        API_REQUEST.put("hangmok4", "");			//
        API_REQUEST.put("hangmok5", "");			//
        API_REQUEST.put("hangmok6", "");			//
        API_REQUEST.put("gasanRateGubun", "");			//
        API_REQUEST.put("specialRate", 0);			//
        API_REQUEST.put("specialRateApplySayu", "");			//
        API_REQUEST.put("bigo", "");			//
        API_REQUEST.put("ocrSiguCd", "");			//
        API_REQUEST.put("ocrBuseoCd", "");			//
        API_REQUEST.put("etc1", "");			//
        API_REQUEST.put("lastWorkId", "");			//
        API_REQUEST.put("lastWorkDate", "");			//
        API_REQUEST.put("vatAmt", 0);			//
        API_REQUEST.put("gasanAmtSkipGubun", "");			//
        API_REQUEST.put("sysGubun", seoulParam.getSysGubun());		// 시스템 고유번호 LVHT  (임시코드, 별도요청 없으면 수정없이 사용)
        API_REQUEST.put("napDzipCd", "");			//
        API_REQUEST.put("napDzipAddr", "");			//
        API_REQUEST.put("napDdtlAddr", "");			//
        API_REQUEST.put("napDrefAddr", "");			//
        API_REQUEST.put("etcCm1", "");			//
        API_REQUEST.put("etcCm2", "");			//
        API_REQUEST.put("etcCm3", "");			//
        API_REQUEST.put("etcCm4", "");			//
        API_REQUEST.put("etcCm5", "");			//
        API_REQUEST.put("napBldBon", "");			//
        API_REQUEST.put("napBldBu", "");			//
        API_REQUEST.put("napDoroCd", "");			//
        API_REQUEST.put("napUndYn", "");			//
        API_REQUEST.put("napbuYmd", "");			//

    	String response;
    	if (com.onlinepowers.framework.common.ServiceType.LOCAL) {
    		// LOCAL DEV ONLY: 서울 세외수입 부과등록 연계서버에 닿지 않으므로 가짜 전자납부번호로 성공 응답을 만든다.
    		JSONObject fakeResponse = new JSONObject();
    		fakeResponse.put("errorCode", "0");
    		fakeResponse.put("errorMsg", "");
    		fakeResponse.put("enapbuNo", "99" + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
    		response = fakeResponse.toJSONString();
    		log.warn("[LOCAL DEV] sntrBugaInsert relay skipped, fake enapbuNo {}", fakeResponse.get("enapbuNo"));
    	} else {
    		response = restFulToRelayServer(seoulBugaUrl, API_REQUEST, "");
    	}
		Object objList = JSONValue.parse(response);
        JSONObject jsonObject = (JSONObject)objList;

        UserDetail userDetail = UserUtils.getUserDetail();

        if (!UserUtils.isUserLogin()) {
            return SERVICE_RELAY_RESULT;
        }

        seoulParam.setUserId(String.valueOf(userDetail.getUserId()));
        seoulParam.setEnapbuNo(String.valueOf(jsonObject.get("enapbuNo")));
        String errorCode =  StringUtils.defaultIfEmpty((String) jsonObject.get("errorCode"), "") ;

        if(errorCode.equals("0")) {
        	ngDonationMapper.insertSntrBuga(seoulParam);	// 서울시 기부신청 데이터 등록

        	// 사용자 정보 내/외국인 코드 업데이트
        	ForeignStatusParam foreignStatusUpdateInfo = new ForeignStatusParam();
        	foreignStatusUpdateInfo.setUserId(userDetail.getUserId());
        	foreignStatusUpdateInfo.setForeignStatusCode(seoulParam.getForeignStatusCode());
        	ngDonationMapper.updateForeignStatusCode(foreignStatusUpdateInfo);
        }

        String mngNo = ngDonationMapper.getMngNoEtax();	// 이택스 전문관리 대장번호 채번

        SERVICE_RELAY_RESULT.put(API_RESONSE, response);
        SERVICE_RELAY_RESULT.put("mngNo", mngNo);

		return SERVICE_RELAY_RESULT;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> etaxSunapInfo(SeoulParam seoulParam) throws Exception {
		HashMap<String, Object> API_REQUEST = new HashMap<>();
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();

		API_REQUEST.put("COM_REQ_MECHE", "GHLOVE");											// 요청매체
		API_REQUEST.put("COM_REQ_DT", DateUtils.getToday("yyyyMMdd"));				// 요청일자
        API_REQUEST.put("COM_REQ_TM", DateUtils.getToday("HHmmss"));					// 요청일시
        API_REQUEST.put("COM_PAY_MSG_NO", ngDonationMapper.getMngNoEtax());		// 전문대장관리번호
        API_REQUEST.put("DATA_CNT", 1);																// 데이터 건수
        API_REQUEST.put("systemCd", seoulParam.getSystemCd());							// 인터페이스 구분코드
        API_REQUEST.put("jijacheCd", seoulParam.getJijacheCd());							// 지자체코드

        JSONObject jo1 = new JSONObject();
        jo1.put("EPAY_NO", seoulParam.getEnapbuNo());
        JSONArray ja = new JSONArray();
        ja.add(jo1);

        API_REQUEST.put("ARR_BU_INFO", ja);								// 전자납부번호 json array
    	String response;
    	if (com.onlinepowers.framework.common.ServiceType.LOCAL) {
    		// LOCAL DEV ONLY: 이택스 수납조회 연계서버에 닿지 않으므로 수납완료 응답을 만든다.
    		JSONObject fakeSunap = new JSONObject();
    		fakeSunap.put("SUNAP_YN", "Y");
    		fakeSunap.put("SUNAP_DT", DateUtils.getToday("yyyyMMdd"));
    		JSONArray fakeResult = new JSONArray();
    		fakeResult.add(fakeSunap);
    		JSONObject fakeResponse = new JSONObject();
    		fakeResponse.put("RST_CD", "100");
    		fakeResponse.put("ARR_RESULT", fakeResult);
    		response = fakeResponse.toJSONString();
    	} else {
    		response = restFulToRelayServer(seoulSunapUrl, API_REQUEST, "");
    	}
    	SERVICE_RELAY_RESULT.put(API_RESONSE, response);

		return SERVICE_RELAY_RESULT;
	}


	@Override
	public Map<String, Object> sendNtsEreceipt(String accessToken, String enapbuNo) throws Exception {
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();
		SeoulParam seoulParam = ngDonationMapper.getNtsEreceipt(enapbuNo);
		if(seoulParam != null) {
			HashMap<String, Object> dataMap = new HashMap<>();
			HashMap<String, Object> encDataMap = new HashMap<>();
			UserDetail userDetail = UserUtils.getUserDetail();
			String encryptedCI = StringUtils.defaultIfEmpty(getRSA(seoulParam.getUserCi(), koreaApiKey), "");
			String encryptedBizNo = StringUtils.defaultIfEmpty(getRSA(seoulParam.getBizNo(), koreaApiKey), "");

			if(encryptedCI.equals("") || encryptedBizNo.equals("")) {
				throw new NullPointerException();
			}

			dataMap.put("dntDt", seoulParam.getSttemntPayDe());										// 기부일자
			dataMap.put("conbCd", "43");																				// 기부금코드
			dataMap.put("dntAmt", Integer.parseInt(String.valueOf(seoulParam.getTaxAmt())));	// 기부금액
			dataMap.put("cnbtSpstCnfrClCd", "01");																	// 기부자신분확인구분코드

			encDataMap.put("extrOrgnNtplDscmEncCntn", encryptedCI);												// 실명인증값
	        encDataMap.put("conbOrgTxprDscmNoEncCntn", encryptedBizNo);										// 기부단체사업자등록번호

	        HashMap<String, Object> API_REQUEST = new HashMap<>();
	        API_REQUEST.put("data", dataMap);
	        API_REQUEST.put("enc_data", encDataMap);
	    	String response = restFulToRelayServer(koreaApiActionUrl, API_REQUEST, accessToken);
	    	Object objList = JSONValue.parse(response);
	        JSONObject jsonObject = (JSONObject)objList;

	        seoulParam.setSttemntPayDe(DateUtils.getToday("yyyyMMdd"));
	        seoulParam.setUserId(String.valueOf(userDetail.getUserId()));
	        seoulParam.setUserCi(encryptedCI);
	        seoulParam.setBizNo(encryptedBizNo);
	        seoulParam.setConbCd("43");
	        seoulParam.setCntrType("01");
	        seoulParam.setResCode(String.valueOf(jsonObject.get("res-code")));
	        seoulParam.setResMsg(String.valueOf(jsonObject.get("res-msg")));

	        ngDonationMapper.insertHometaxGif(seoulParam);
	        ngDonationMapper.updateNtsStatus(seoulParam);
	        SERVICE_RELAY_RESULT.put(API_RESONSE, response);
		} else {
			throw new NullPointerException();
		}

		return SERVICE_RELAY_RESULT;
	}

	@Override
	public Map<String, Object> contryBugaInsert(ContryParam contryParam) throws Exception {
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();
		String payValidDe = DateUtils.getToday("yyyyMMdd");

		if (!"0".equals(contryParam.getForeignStatusCode())) {				// 외국인일 경우 만료날짜 한번 더 체크
			DonationParam param = new DonationParam();
			param.setUserName(contryParam.getTxprNm());
			param.setJuminNo(contryParam.getTxprNo().substring(6));
			param.setLocgovCode(contryParam.getJijacheCd());
			Map<String, Object> rst = rsgstadresinfoForeigner(param);

			String rstCode = rst.get("rst").toString();
			if (!"SUCCESS".equalsIgnoreCase(rstCode)) {			// 성공이 아닐 경우
				String errMsg = "외국인 기부 가능 상태가 아닙니다.";

				switch (rstCode) {
					case "NONE_DATA" :
						errMsg = "외국인 거소 조회 정보가 없습니다.";
						break;
					case "EXPIRED" :
						errMsg = "외국인 거소 만료일이 지났습니다.";
						break;
					case "IN_LOCGOV" :
						errMsg = "외국인 거소 자치구에는 기부가 불가능합니다.";
						break;
				}
				errMsg += " USER_ID :: " + UserUtils.getUser().getUserId();
				throw new UserException(errMsg);
			}
		}

		if (contryParam.getPrjId() > 0) {
			DesignatedDonationSearchParam searchParam = new DesignatedDonationSearchParam();
			searchParam.setPrjId(contryParam.getPrjId());
			searchParam.setCheckDonation(true);
//			searchParam.setLocgovCode(contryParam.getJijacheCd());
			DesignatedDonation designatedDonation = designatedDonationService.selectDesignatedDonationDetail(searchParam);
			if (designatedDonation == null) {
				throw new UserException("현재 기부 가능한 특정사업에 기부하기 사업이 아닙니다.");
			}
			if (!designatedDonation.getSUCCESS().equals(designatedDonation.getResultCode())) {
				SERVICE_RELAY_RESULT.put("resultCode", designatedDonation.getResultCode());
				return SERVICE_RELAY_RESULT;
			}
		}

    	// 요청변수 설정
    	String mngNo = ngDonationMapper.getBookNoContry();		// 기부금 시스템키(채번)
    	String bizNo = ngDonationMapper.getJijachDeptCd(contryParam.getJijacheCd());	// 지자체 사업자번호
    	String fisSp = ngDonationMapper.getLocgovFisSp(contryParam.getJijacheCd());

    	HashMap<String, Object> API_REQUEST = new HashMap<>();
        API_REQUEST.put("comReqDt", DateUtils.getToday("yyyyMMdd"));	// 요청일자
        API_REQUEST.put("comReqTm", DateUtils.getToday("HHmmss"));		// 요청일시
        API_REQUEST.put("systemCd", contryParam.getSystemCd());		// 인터페이스 구분코드
        API_REQUEST.put("jijacheCd", contryParam.getJijacheCd());		// 지자체코드
        API_REQUEST.put("DEPT_CD", bizNo);			// 부서코드
        API_REQUEST.put("FISYY", contryParam.getFisyy());				// 회계연도
        API_REQUEST.put("FIS_SP", fisSp);			// 회계구분코드
        API_REQUEST.put("PTCL_CD", contryParam.getPtclCd());			// 세목코드
        API_REQUEST.put("IMPS_DT", DateUtils.getToday("yyyyMMdd"));			// 부과일자
        API_REQUEST.put("INIT_PRCP_TAX_AMT", contryParam.getTaxAmt());	// 최초본세
        API_REQUEST.put("LST_PRCP_TAX_AMT", contryParam.getTaxAmt());		// 최종본세
        API_REQUEST.put("INIT_DUE_DT", payValidDe);		// 최초납기일자
        API_REQUEST.put("LST_DUE_DT" , payValidDe);		// 최종납기일자
        API_REQUEST.put("AF_DUE_DT", "");		// 납기후일자
        API_REQUEST.put("AF_DUE_AMT", 0);		// 납기후금액
        API_REQUEST.put("IMPS_SP", contryParam.getImpsSp());			// 부과구분
        API_REQUEST.put("DECS_SP", contryParam.getDecsSp());			// 감경구분
        API_REQUEST.put("TXPR_SP", contryParam.getTxprSp());			// 납부자구분
        API_REQUEST.put("TXPR_NO", contryParam.getTxprNo());			// 납부자번호
        API_REQUEST.put("TXPR_NM", contryParam.getTxprNm());			// 납부자성명
        API_REQUEST.put("TXPR_TEL_NO", "");			// 납부자전화번호
        API_REQUEST.put("TXPR_MPHN_NO", "");			// 납부자휴대폰
        API_REQUEST.put("TXPR_EML", "");			// 납부자이메일
        API_REQUEST.put("NEW_ADDR_YN", contryParam.getNewAddrYn());			// 새주소여부
        API_REQUEST.put("TXPR_ROAD_CD", contryParam.getTxprRoadCd());			// 납부자도로명주소
        API_REQUEST.put("TXPR_BD_FLR_SP", contryParam.getTxprBdFlrSp());			// 납부자지하여부
        API_REQUEST.put("TXPR_BD_PRCP_NO", contryParam.getTxprBdPrcpNo());		// 납부자건물본번
        API_REQUEST.put("TXPR_BD_SUB_NO", contryParam.getTxprBdSubNo());			// 납부자건물부번
        API_REQUEST.put("STAT_CD", contryParam.getStatCd());			// 납부자상태코드
        API_REQUEST.put("SPCL_FIS_BIZ_CD", contryParam.getSpclFisBizCd());			// 특별회계사업코드
        API_REQUEST.put("TXPR_ZIP_CD", contryParam.getTxprZipCd());			// 우편번호
        API_REQUEST.put("TXPR_LGLVIL_CD", "");			// 법정동코드
        API_REQUEST.put("TXPR_TWNVIL_CD", contryParam.getTxprTwnvilCd());			// 행정동코드
        API_REQUEST.put("TXPR_MT", "01");			// 납부자산
        API_REQUEST.put("TXPR_ADDR_NO", "");			// 납부자번지
        API_REQUEST.put("TXPR_ADDR_HO", "");			// 납부자호
        API_REQUEST.put("TXPR_SPCL_ADDR", "");			// 납부자특수주소
        API_REQUEST.put("TXPR_SPCL_ADDR_DONG", "");			// 납부자특수주소동
        API_REQUEST.put("TXPR_SPCL_ADDR_HO", "");			// 납부자특수주소호
        API_REQUEST.put("TXPR_ADDR_TONG", "");			// 납부자통
        API_REQUEST.put("TXPR_ADDR_BAN", "");			// 납부자반
        API_REQUEST.put("TXPR_BD_MNG_NO", contryParam.getTxprBdMngNo());			// 납부자건물관리번호
        API_REQUEST.put("TXPR_DTL_ADDR", contryParam.getTxprDtlAddr());			// 납부자상세주소
        API_REQUEST.put("OBJ_NM", contryParam.getObjNm());			// 물건지명
        API_REQUEST.put("TAX_OBJ_SP", contryParam.getTaxObjSp());			// 부과대상구분코드
        API_REQUEST.put("TAX_OBJ_NEW_ADDR_YN", contryParam.getTaxObjNewAddrYn());			// 물건지새주소여부
        API_REQUEST.put("MNG_HTM1", contryParam.getMngHtm1());			// 기부금명칭 및 기타항목
        API_REQUEST.put("MNG_HTM2", "");			// 기타항목
        API_REQUEST.put("MNG_HTM3", "");			// 기타항목
        API_REQUEST.put("MNG_HTM4", "");			// 기타항목
        API_REQUEST.put("MNG_HTM5", mngNo);			// 기부금 시스템키(채번)
        API_REQUEST.put("MNG_HTM6", "");			// 기타항목
        API_REQUEST.put("RMK", "");			// 기타항목
        API_REQUEST.put("INIT_WRKR_ID", "");			// 최초작업자(세외)id
        API_REQUEST.put("BANK_CD", "");			// 은행코드
        API_REQUEST.put("TXPR_FULL_ADDR", "");			// 납부자통주소
        API_REQUEST.put("TXPR_BASIC_ADDR", "");			// 납부자기본주소
        API_REQUEST.put("TXPR_BASIC_DTL_ADDR", "");			// 납부자기본상세주소
        API_REQUEST.put("SYS_CD", contryParam.getSysCd());			// 시스템코드
        API_REQUEST.put("enisTargetIdKey", "S0204820294");			// target system code-고정값
    	String response = restFulToRelayServer(contryNowBugaUrl, API_REQUEST, "");
    	Object objList = JSONValue.parse(response);
        JSONObject jsonObject = (JSONObject)objList;

        UserDetail userDetail = UserUtils.getUserDetail();

        contryParam.setUserId(String.valueOf(userDetail.getUserId()));
        contryParam.setEnapbuNo(String.valueOf(jsonObject.get("elctPayNo")));
        contryParam.setPayValidDe(payValidDe);

        if("100".equals(StringUtils.defaultIfEmpty((String) jsonObject.get("result_code"), ""))) {
        	ngDonationMapper.insertContryBuga(contryParam);	//insert g_cntr

        	// 사용자 정보 내/외국인 코드 업데이트
        	ForeignStatusParam foreignStatusUpdateInfo = new ForeignStatusParam();
        	foreignStatusUpdateInfo.setUserId(userDetail.getUserId());
        	foreignStatusUpdateInfo.setForeignStatusCode(contryParam.getForeignStatusCode());
        	ngDonationMapper.updateForeignStatusCode(foreignStatusUpdateInfo);
        }
        SERVICE_RELAY_RESULT.put(API_RESONSE, response);

		return SERVICE_RELAY_RESULT;
	}

	@Override
	public Map<String, Object> contrySunapInfo(ContryParam contryParam) throws Exception {
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();
		HashMap<String, Object> API_REQUEST = new HashMap<>();
		API_REQUEST.put("comReqDt", DateUtils.getToday("yyyyMMdd"));			// 요청일자
		API_REQUEST.put("comReqTm", DateUtils.getToday("HHmmss"));			// 요청일시
		API_REQUEST.put("systemCd", contryParam.getSystemCd());				// 인터페이스 구분코드
		API_REQUEST.put("jijacheCd", contryParam.getJijacheCd());				// 지자체코드
		API_REQUEST.put("CONN_KEY", contryParam.getMngNo());				// 시스템키(대장관리번호)
		API_REQUEST.put("ELCT_PAY_NO", contryParam.getEnapbuNo());		// 전자납부번호
		API_REQUEST.put("enisTargetIdKey", "S0204820294");							// target system code-고정값

    	String response = restFulToRelayServer(contryNowSunapUrl, API_REQUEST, "");
    	SERVICE_RELAY_RESULT.put(API_RESONSE, response);

		return SERVICE_RELAY_RESULT;
	}

	@Override
	public Map<String, Object> contryNextSunapInfo(ContryParam contryParam) throws Exception {
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();
		HashMap<String, Object> API_REQUEST = new HashMap<>();
		API_REQUEST.put("ELCT_PAY_NO", contryParam.getEnapbuNo());		// 전자납부번호

		int cnt = ngDonationMapper.getSunapInfo(contryParam.getEnapbuNo());
    	SERVICE_RELAY_RESULT.put(API_RESONSE, cnt);
		return SERVICE_RELAY_RESULT;
	}



	/**
	 * <pre>
	 * comment       : 국세청 연계를 위한 token 가져오기
	 * preMethodName : getAccessToken
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @return
	 * @throws IOException
	 * String
	 */
	public String getAccessToken() throws IOException {
		String accessToken = "";
		HashMap<String, Object> API_REQUEST = new HashMap<>();
		try {
			String encryptedPW = getRSA(koreaApiPassword, koreaApiKey);
			HashMap<String, Object> dataMap = new HashMap<>();
			HashMap<String, Object> encDataMap = new HashMap<>();
			dataMap.put("id", koreaApiId);
			encDataMap.put("pw", encryptedPW);

			API_REQUEST.put("data", dataMap);
			API_REQUEST.put("enc_data", encDataMap);

			String response = restFulToRelayServer(koreaApiLoginUrl, API_REQUEST, "");
			Object objList = JSONValue.parse(response);
			JSONObject jsonObject = (JSONObject)objList;

			accessToken = String.valueOf(jsonObject.get("AccessToken"));
		} catch (IOException e) {
			log.error(e.getMessage());
        	log.error("getAccessToken error {}", e.getStackTrace()[0]);
        } catch (Exception e) {
			log.error(e.getMessage());
        	log.error("getAccessToken error {}", e.getStackTrace()[0]);
        }

        return accessToken;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> giroPay(HashMap<String, String> params) throws Exception {
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();
		String errorMsg = "";

        //디바이스(PC,모바일) 구분하여 팝업창 사이즈 조절
		String isMobile = null;
		String userAgent = StringUtils.defaultIfEmpty(params.get("userAgent"), "");

	    if(userAgent.indexOf("MOBI") > -1) {
	    	isMobile = "Y";
	    } else {
	    	isMobile = "N";
	    }

	    params.put("isMobile", isMobile);

        if(StringUtils.isBlank(StringUtils.defaultIfEmpty(params.get("enapbuNo"), ""))) {
        	errorMsg = "전자납부번호가 존재하지 않습니다.";
        	log.error("/api/ngdonation/giroPay ERROR {}", errorMsg);
        	SERVICE_RELAY_RESULT.put("errorMsg", errorMsg);
        	return SERVICE_RELAY_RESULT;
        }

		// 공공 개방 플랫폼 연계시 추가 2024-11-12 시작
		UserDetail userDetail = null;
		if(params.get("exGubun") != null && params.get("exGubun").equals("EXTERNAL")){
			// 공공 개방 플랫폼 연계시
			try {
				userDetail = ngDonationMapper.getUserDetail(Long.parseLong(params.get("userId")));
			}
			catch (NullPointerException | NumberFormatException e) {
				log.error("ngDonationMapper.getUserDetail ERROR :: userId :: {} , {}", params.get("userId"), e);
			}
		}
		else{
			// 내부 사용시
			if (!UserUtils.isUserLogin()) {
				errorMsg = "인증이 필요합니다";
				log.error("/api/ngdonation/giroPay ERROR {}", errorMsg);
				SERVICE_RELAY_RESULT.put("errorMsg", errorMsg);
				return SERVICE_RELAY_RESULT;
			}

			userDetail = UserUtils.getUserDetail();
			userDetail = ngDonationMapper.getUserDetail(userDetail.getUserId());
		}
		// 공공 개방 플랫폼 연계시 추가 2024-11-12 종료

		GiroParam paramVo = new GiroParam();
		paramVo.setJijacheCd(params.get("jijacheCd"));

        GiroParam giroMap = ngDonationMapper.getGiroData(paramVo);

        HashMap<String, String> API_REQUEST = new HashMap<String, String>();
        API_REQUEST.put("sortCode", giroMap.getUseInsttCode());
        API_REQUEST.put("giroNo", giroMap.getGiroNo());
        API_REQUEST.put("elecNo", StringUtils.defaultIfEmpty(params.get("enapbuNo"), ""));
        API_REQUEST.put("birthDate", userDetail.getBirthday().substring(2,8));

		// 공공 개방 플랫폼 연계시 추가 2024-11-12 시작
		if(params.get("exGubun") != null && params.get("exGubun").equals("EXTERNAL")){
			API_REQUEST.put("payerName", StringUtils.defaultIfEmpty(params.get("userName"), ""));
		}
		else{
			API_REQUEST.put("payerName", UserUtils.getUser().getUserName());
		}
		// 공공 개방 플랫폼 연계시 추가 2024-11-12 종료

		API_REQUEST.put("callDt", DateUtils.getToday("yyyyMMddHHmmss"));
        API_REQUEST.put("successRU", StringUtils.defaultIfEmpty(params.get("domain"), "")+"/donation/giro-success.html");
        API_REQUEST.put("failRU", StringUtils.defaultIfEmpty(params.get("domain"), "")+"/donation/giro-fail.html");

        JSONObject json = new JSONObject();
        for (String key : API_REQUEST.keySet()) {
            json.put(key, API_REQUEST.get(key));
        }
        AES256Utils aes256 = new AES256Utils(aes256Key, aes256Iv);

        String encData = aes256.encrypt(json.toString());

        SERVICE_RELAY_RESULT.put("data", encData);
        SERVICE_RELAY_RESULT.put("isMobile", StringUtils.defaultIfEmpty(params.get("isMobile"), "N"));
        SERVICE_RELAY_RESULT.put("popUrl", giroUrl);

		return SERVICE_RELAY_RESULT;
	}

	/**
	 * <pre>
	 * comment       : RESTFUL 연계
	 * preMethodName : restFulToRelayServer
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @param API_URL
	 * @param API_REQUEST
	 * @param accessToken
	 * @return
	 * @throws IOException
	 * String
	 */
	@SuppressWarnings("unchecked")
	public String restFulToRelayServer(String API_URL, HashMap<String, Object> API_REQUEST, String accessToken) throws Exception  {
        String response = "";

		URL url = new URL(API_URL);	//행공센 연계

		//_initHttps();
		final HttpURLConnection http = (HttpURLConnection) url.openConnection();

        /* test 환경에서는 인증서 오류가 날 수도 있다. 이 코드를 이용해 인증서 오류를 회피한다. */
		if (http instanceof HttpsURLConnection) {
			SSLSocketFactoryMaker factory = new SSLSocketFactoryMaker();
			((HttpsURLConnection) http).setSSLSocketFactory(factory.getSSLSocketFactory());
			((HttpsURLConnection) http).setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return CERTIFICATE;
				}
			});
		}

		try (AutoCloseable ac = () -> http.disconnect()) {
			http.setConnectTimeout(TIMEOUT_VALUE);
	        http.setReadTimeout(30000);
	        http.setDefaultUseCaches(false);
	        http.setDoInput(true);
	        http.setDoOutput(true);
	        http.setRequestMethod("POST");

	        http.setRequestProperty("Content-Type", "application/json");

	        //국세청 TOKEN 가져올 시
	        if(API_URL.equals(koreaApiLoginUrl) || API_URL.equals(koreaApiActionUrl)) {
	        	http.setRequestProperty("X-Forwarded-For", koreaApiLoginIp);
	        	if(API_URL.equals(koreaApiActionUrl)) {
	        		http.setRequestProperty("Authorization","Bearer " + accessToken);
	        	}
	        }

	        JSONObject json = new JSONObject();
	        for (String key : API_REQUEST.keySet()) {
	            json.put(key, API_REQUEST.get(key));
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
    				response = sb.toString();
    			}
            }
		}
		return response;
	}

	/**
	 * <pre>
	 * comment       : RESTFUL 연계
	 * preMethodName : restFulToRelayServerHttps
	 * author        : hybrid
	 * date          : 2024. 2. 6.
	 *
	 * </pre>
	 * @param API_URL
	 * @param API_REQUEST
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String restFulToRelayServerHttps(String API_URL, HashMap<String, Object> API_REQUEST, String accessToken) throws Exception  {
		String response = "";

		URL url = new URL(API_URL);	//행공센 연계

		final HttpsURLConnection http = (HttpsURLConnection) url.openConnection();

		/* test 환경에서는 인증서 오류가 날 수도 있다. 이 코드를 이용해 인증서 오류를 회피한다. */
		if (http instanceof HttpsURLConnection) {

			SSLSocketFactoryMaker factory = new SSLSocketFactoryMaker();
			((HttpsURLConnection) http).setSSLSocketFactory(factory.getSSLSocketFactory());
			((HttpsURLConnection) http).setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return CERTIFICATE;
				}
			});
		}

		try (AutoCloseable ac = () -> http.disconnect()) {
			http.setConnectTimeout(TIMEOUT_VALUE);
			http.setReadTimeout(30000);
			http.setDefaultUseCaches(false);
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setRequestMethod("POST");

			http.setRequestProperty("Content-Type", "application/json");

			//국세청 TOKEN 가져올 시
			if(API_URL.equals(koreaApiLoginUrl) || API_URL.equals(koreaApiActionUrl)) {
				http.setRequestProperty("X-Forwarded-For", koreaApiLoginIp);
				if(API_URL.equals(koreaApiActionUrl)) {
					http.setRequestProperty("Authorization","Bearer " + accessToken);
				}
			}

			JSONObject json = new JSONObject();
			for (String key : API_REQUEST.keySet()) {
				json.put(key, API_REQUEST.get(key));
			}

			try (final OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "UTF-8")) {
				PrintWriter writer = new PrintWriter(osw);
				writer.write(json.toString());
				writer.flush();
			}


			try (final BufferedReader isr = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"))) {
				StringBuilder sb = new StringBuilder();
				String str;
				while ((str = isr.readLine()) != null) {
					sb.append(str + "\n");
				}
				response = sb.toString();
			}
		}

		return response;
	}

	public String  getRSA(String plainData, String stringPublicKey) {
		String encryptedData = "";
        try {
            //평문으로 전달받은 공개키를 공개키객체로 만드는 과정
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            //만들어진 공개키객체를 기반으로 암호화모드로 설정하는 과정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //평문을 암호화하는 과정
            byte[] byteEncryptedData = cipher.doFinal(plainData.getBytes());
            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);

        } catch (NoSuchAlgorithmException e) {
        	log.error("RSA 암호화 : NoSuchAlgorithmException ERROR-35: 암호 알고리즘 사용불가 오류", e.getStackTrace()[0]);
        } catch (InvalidKeySpecException e) {
        	log.error("RSA 암호화 : InvalidKeySpecException ERROR-36: Key 표준 부적합 오류", e.getStackTrace()[0]);
        } catch (NoSuchPaddingException e) {
        	log.error("RSA 암호화 : NoSuchPaddingException ERROR-37: 패딩 사용불가 오류", e.getStackTrace()[0]);
        } catch (InvalidKeyException e) {
        	log.error("RSA 암호화 : InvalidKeyException ERROR-38: Key 길이 초과 오류", e.getStackTrace()[0]);
		} catch (IllegalBlockSizeException e) {
			log.error("RSA 암호화 : IllegalBlockSizeException ERROR-39: 암호화 데이터 Size 오류", e.getStackTrace()[0]);
		} catch (BadPaddingException e) {
			log.error("RSA 암호화 : BadPaddingException ERROR-40: 복호화 Key 불일치 오류", e.getStackTrace()[0]);
		} catch (NullPointerException e) {
			log.error("RSA 암호화 : NullPointerException ", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("RSA 암호화 : Exception ", e.getStackTrace()[0]);
		}

        return encryptedData;
	}

//	private void _initHttps() {
//		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//				if (!CERTIFICATE) {
//					throw new CertificateException();
//				}
//			}
//
//			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//				if (!CERTIFICATE) {
//					throw new CertificateException();
//				}
//			}
//
//			public X509Certificate[] getAcceptedIssuers() {
//				return new X509Certificate[0];
//			}
//		} };
//		try {
//			// SSL -> TLSv1.2 by SonarQube
//			SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
//			sslContext.init(null, trustAllCerts, new SecureRandom());
//			_sslSockFactory = sslContext.getSocketFactory();
//		} catch (RuntimeException e) {
//			RuntimeException re = new RuntimeException(e);
//			re.setStackTrace(e.getStackTrace());
//			throw re;
//		} catch (KeyManagementException e) {
//			log.error("ERROR-34: Key 관리 예외오류 {}", e.getStackTrace()[0]);
//		} catch (NoSuchAlgorithmException  e) {
//			log.error("ERROR-35: 암호 알고리즘 사용불가 오류 {}", e.getStackTrace()[0]);
//		}
//	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> nextBugaRequest(NextBugaRequestDto nextBugaRequestDto) throws Exception {
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();
		HashMap<String,String> searchMap = new HashMap<>();
    	try {
    		if (Long.valueOf(nextBugaRequestDto.getPrjId()) > 0) {
    			DesignatedDonationSearchParam searchParam = new DesignatedDonationSearchParam();
    			searchParam.setPrjId(Long.valueOf(nextBugaRequestDto.getPrjId()));
    			searchParam.setCheckDonation(true);
//    			searchParam.setLocgovCode(nextBugaRequestDto.getCntrLocgovCode());
    			DesignatedDonation designatedDonation = designatedDonationService.selectDesignatedDonationDetail(searchParam);
    			if (designatedDonation == null) {
    				throw new UserException("현재 기부 가능한 특정사업에 기부하기 사업이 아닙니다.");
    			}
    			if (!designatedDonation.getSUCCESS().equals(designatedDonation.getResultCode())) {
    				SERVICE_RELAY_RESULT.put("resultCode", designatedDonation.getResultCode());
    				return SERVICE_RELAY_RESULT;
    			}
    		}

    		searchMap.put("locgovCode", nextBugaRequestDto.getCntrLocgovCode());
        	// 요청변수 설정
    		Locgov locgov = ngDonationMapper.getLocgovInfo(searchMap);	// 처리부서코드
        	String fisSp = ngDonationMapper.getLocgovFisSp(nextBugaRequestDto.getCntrLocgovCode());
        	String linkMngKey = ngDonationMapper.getLinkMngKeyNextValue();

        	// 2024.03.04 개발원 요청 특별회계(51, 61)일 경우 특별회계사업코드 0000이 아닌 7092로 변경
        	String strSpclFisBizCd = "0000";
        	if(fisSp.equals("51") || fisSp.equals("61"))  strSpclFisBizCd = "7092";

        	nextBugaRequestDto.setSgbCd(locgov.getAdministInsttCode());
        	nextBugaRequestDto.setLinkTrgtCd(LINK_TRGT_CD);
        	nextBugaRequestDto.setLinkMngKey(linkMngKey);
        	nextBugaRequestDto.setDptCd(locgov.getProcessDeptCode().substring(0, locgov.getProcessDeptCode().length() - 4));
        	nextBugaRequestDto.setSpclFisBizCd(strSpclFisBizCd);
        	nextBugaRequestDto.setFyr(DateUtils.getToday("yyyy"));
        	nextBugaRequestDto.setActSeCd(fisSp);
        	nextBugaRequestDto.setRprsTxmCd("224102");
        	nextBugaRequestDto.setOperItemCd("000");
        	nextBugaRequestDto.setLvyYmd(DateUtils.getToday("yyyyMMdd"));
        	nextBugaRequestDto.setFrstPidYmd(DateUtils.getToday("yyyyMMdd"));
        	nextBugaRequestDto.setPyrSeCd("01");
        	nextBugaRequestDto.setRprsPyrNo("");
        	nextBugaRequestDto.setRprsPyrNm("");
        	nextBugaRequestDto.setPyrSttCd("10");

        	nextBugaRequestDto.setLotnoRoadAddrSeCd("02");
        	nextBugaRequestDto.setMngItemCn1("고향사랑기부금");
        	nextBugaRequestDto.setDsgnDntnBizId(nextBugaRequestDto.getPrjId());

        	ObjectMapper objectMapper = new ObjectMapper();
        	HashMap<String, Object> requestMap = (HashMap<String, Object>) objectMapper.convertValue(nextBugaRequestDto, Map.class);

        	String response;
        	if (com.onlinepowers.framework.common.ServiceType.LOCAL) {
        		// LOCAL DEV ONLY: 부과등록 연계서버에 닿지 않으므로 가짜 전자납부번호로 성공 응답을 만든다. G_CNTR 등록은 실제대로 탄다.
        		String fakeEpayNo = "99" + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        		JSONObject fakeResponse = new JSONObject();
        		fakeResponse.put("linkRstCd", "000");
        		fakeResponse.put("linkRstMsg", "LOCAL FAKE : " + fakeEpayNo);
        		response = fakeResponse.toJSONString();
        		log.warn("[LOCAL DEV] nextBugaRequest relay skipped, fake epayNo {}", fakeEpayNo);
        	} else {
        		response = restFulToRelayServerHttps(nextBugaRequestUrl, requestMap, "");
        	}
        	Object objList = JSONValue.parse(response);
            JSONObject jsonObject = (JSONObject)objList;

            UserDetail userDetail = UserUtils.getUserDetail();

            nextBugaRequestDto.setUserId(userDetail.getUserId());

            String linkRstCd = StringUtils.defaultIfEmpty((String) jsonObject.get("linkRstCd"), "");
            String linkRstMsg = StringUtils.defaultIfEmpty((String) jsonObject.get("linkRstMsg"), "");
            String epayNo = StringUtils.defaultIfEmpty(linkRstMsg.split(":")[1], "").trim();

            if(linkRstCd.equals("000") && !epayNo.equals("")) {

            	nextBugaRequestDto.setEpayNo(epayNo);
            	if(nextBugaRequestDto.getCntrPathCode().equals("100")) {
            		ngDonationMapper.insertNextBugaCntr(nextBugaRequestDto);	//insert g_cntr
            	}
            }

            jsonObject.put("epayNo", epayNo);
            SERVICE_RELAY_RESULT.putAll(jsonObject);
    	} catch (UserException e) {
    		log.error("■■■API■■■ nextBugaRequest UserException : {} {}", e);
        	throw e;
    	} catch(Exception e) {
    		log.error("■■■API■■■ nextBugaRequest EXCEPTION : {} {}", nextBugaRequestDto.getEpayNo(), e.getStackTrace()[0]);
    		log.error(e.getMessage());
    	}

		return SERVICE_RELAY_RESULT;
	}

	// 행정정보공동이용시스템 api 호출(재외국민 조회, 외국인 등록사실 증명)
	@Override
	public Map<String, Object> rsgstadresinfoForeigner(DonationParam donationParam) throws Exception {
		UserDetail userDetail = UserUtils.getUserDetail();
        userDetail = ngDonationMapper.getUserDetail(userDetail.getUserId());
        HashMap<String, Object> requestParam = new HashMap<String, Object>();
        requestParam.put("foreignNo", userDetail.getBirthday().toString().substring(2)+donationParam.getJuminNo());	//주민번호
        requestParam.put("foreignName", UserUtils.getUser().getUserName());	//이름

    	return getForeignApiData(requestParam, donationParam.getLocgovCode());
	}

	@Override
	public String getRsaPublicKey() {
		long userId = UserUtils.getUser().getUserId();
		if (userId <= 0) {
			throw new UserException(ApiError.NOT_EXIST_AUTH.toString(), "로그인 상태가 아닙니다.", "");
		}
		HashMap<String, String> keyPair = RsaCryptor.createKeypairAsString();
		if (keyPair == null) {
			throw new UserException(ApiError.SYSTEM_ERROR.toString(), "암호화키 생성에 실패했습니다..", "");
		}
		TempPrivateKeyInfo keyInfo = new TempPrivateKeyInfo();
		keyInfo.setUserId(userId);
		keyInfo.setPrivateKey(keyPair.get("privateKeyStr"));

		tempPrivateKeyInfoRepository.save(keyInfo);

		return keyPair.get("publicKeyStr");
	}

	@Override
	public String decryptByRsaPrivateKey(String encrypted) throws IOException {
		long userId = UserUtils.getUser().getUserId();
		if (userId <= 0) {
			throw new UserException(ApiError.NOT_EXIST_AUTH.toString(), "로그인 상태가 아닙니다.", "");
		}

		TempPrivateKeyInfo keyInfo = tempPrivateKeyInfoRepository.findByUserId(userId);

		if (keyInfo == null || !com.onlinepowers.framework.util.StringUtils.hasLength(keyInfo.getPrivateKey())) {
			throw new UserException(ApiError.NOT_FOUND.toString(), "복호화 정보가 없습니다. 다시 진행해주세요.", "");
		}

		String decrypted = RsaCryptor.decrypt(encrypted, keyInfo.getPrivateKey());

		return decrypted;
	}



	private JsonObject getDataJsonObject(Gson gson, String jsonString) {
		JsonObject jsonObj = null;
//		jsonObj = gson.fromJson(jsonString, JsonObject.class).getAsJsonObject().get("data").getAsJsonArray().get(0).getAsJsonObject();
		jsonObj = gson.fromJson(jsonString, JsonObject.class);
		JsonElement element = jsonObj.get("data");
		JsonArray array = element.getAsJsonArray();
		int size = array.size();
		if (size > 0) {
			element = array.get(0);
			jsonObj = element.getAsJsonObject();
		} else {
			jsonObj = null;
		}
		return jsonObj;
	}

	@Override
	public Map<String, Object> getForeignApiData(HashMap<String, Object> requestParam, String checkLocgovCode) throws Exception {
		String response = restFulToRelayServerHttps(inLinkUrl + "/api/mopas/rsgstadresinfoForeigner", requestParam, "");
		//String response = restFulToRelayServer(inLinkUrl + "/api/mopas/rsgstadresinfoForeigner", requestParam, "");
		Map<String, Object> result = new HashMap<>();

		String bassAdres = "";

		String SUC_RST_CD = "SUC.WS.000";

    	if (com.onlinepowers.framework.util.StringUtils.hasLength(response)) {
    		Gson gson = new Gson();
    		JsonObject responseData = gson.fromJson(response, JsonObject.class);
//    		JsonObject responseData = getTestData();

    		int responseCode = responseData.get("responseCode").getAsInt();

    		if (HttpURLConnection.HTTP_OK == responseCode) {

    			JsonObject data = getDataJsonObject(gson, responseData.get("body").getAsString());

    			if (data == null) {
            		result.put("errCode", "NONE_DATA");
            		result.put("errMsg", "조회된 정보가 없습니다.");
            		return result;
    			}

    			String rstCd = data.get("mojResultCode").getAsString();
    			if (SUC_RST_CD.equals(rstCd)) {

            		String frgnrSttusSe = responseData.get("frgnrSttusSe").getAsString();

        			JsonObject addressInfo = null;
        			String locgovCheckCode = "";
        			String locgovCode = "";
        			String upperLocgovCode = "";
//        			String checkLocgovCode = "";
        			String address = "";

        			LocGovInfo sidoListParam = new LocGovInfo();
        			DoLocGovInfo locGovInfo = null;
        			JsonObject jusoInfo = null;

    				LocalDate now = LocalDate.now();

            		switch (frgnrSttusSe) {
        				case "1":			// 등록외국인
        					String stayQualfEndDate = data.get("stayQualfEndDate").getAsString();			// 만료일?
        					if (com.onlinepowers.framework.util.StringUtils.isEmpty(stayQualfEndDate)) {
        						stayQualfEndDate = "99991231";
        					}
        					LocalDate checkDate = LocalDate.parse(stayQualfEndDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

        					if (now.isAfter(checkDate)) {		// 현재 날짜가 만료일 이후 기부 불가
        			    		result.put("rst", "EXPIRED");
        					} else {

        						addressInfo = data.get("stayAreaInfoResponse").getAsJsonObject().get("stayAreaInfoList").getAsJsonArray().get(0).getAsJsonObject();
        						locgovCheckCode = addressInfo.get("admnstmachCddr").getAsString();

        						//locgovCode = locgovCheckCode.substring(0, 5);
        						locgovCode = convertAdmCdToLocgovCode(locgovCheckCode);

        						upperLocgovCode = locgovCode.substring(0, 2) + "000";
//        						checkLocgovCode = donationParam.getLocgovCode();
        						address = addressInfo.get("adres").getAsString();

        						result.put("locgovCode", locgovCode);

        						if (locgovCode.equals(checkLocgovCode) || upperLocgovCode.equals(checkLocgovCode)) {			// 기부 지자체 코드가 같거나 기부 지자체가 기부자 지자체의 상위 지자체이면 기부 불가
        							result.put("rst", "IN_LOCGOV");
        						} else {
        							result.put("rst", "SUCCESS");
        						}

        			        	sidoListParam.setLocgovCode(locgovCode);
        			        	locGovInfo = ngDonationMapper.getLocGovInfo(sidoListParam);
        			        	bassAdres = locGovInfo.getBassAdres();

        						result.put("bassAdres", bassAdres);

        						// 이름, 주민번호 등 값은 화면에서 쓰지 않아서 주소만 추가전송
        						jusoInfo = new JsonObject();
        						jusoInfo.addProperty("juso", address);
        						result.put("adressData", jusoInfo.toString());

        						// 만료일이 당일일 경우 알림 표시..
//        						if (now.isEqual(checkDate)) {
//        							StringBuffer buf = new StringBuffer();
//        							buf.append(checkDate.getYear());
//        							buf.append("년 ");
//        							buf.append(checkDate.getMonthValue());
//        							buf.append("월 ");
//        							buf.append(checkDate.getDayOfMonth());
//        							buf.append("일까지 기부가능합니다.");
//
//        							result.put("isTodayMsg", buf.toString());
//        						}
        					}
        					break;
        				case "2":			// 재외국민
//        					result.put("rst", "SUCCESS");

        					addressInfo = data.get("ovrsekrnOndssResponse").getAsJsonObject().get("ovrsekrnOndssInfoList").getAsJsonArray().get(0).getAsJsonObject();
        					locgovCheckCode = addressInfo.get("admnstmachCddr").getAsString();
//        					locgovCode = locgovCheckCode.substring(0, 5);
        					locgovCode = convertAdmCdToLocgovCode(locgovCheckCode);
        					upperLocgovCode = locgovCode.substring(0, 2) + "000";
//        					checkLocgovCode = donationParam.getLocgovCode();
        					address = addressInfo.get("adres").getAsString();

        					if (locgovCode.equals(checkLocgovCode) || upperLocgovCode.equals(checkLocgovCode)) {			// 기부 지자체 코드가 같거나 기부 지자체가 기부자 지자체의 상위 지자체이면 기부 불가
        						result.put("rst", "IN_LOCGOV");
        					} else {
        						result.put("rst", "SUCCESS");
        					}

        					result.put("locgovCode", locgovCode);

        					sidoListParam = new LocGovInfo();
        		        	sidoListParam.setLocgovCode(locgovCode);
        		        	locGovInfo = ngDonationMapper.getLocGovInfo(sidoListParam);

        		        	bassAdres = locGovInfo.getBassAdres();

        					result.put("bassAdres", bassAdres);

        					// 이름, 주민번호 등 값은 화면에서 쓰지 않아서 주소만 추가전송
        					jusoInfo = new JsonObject();
        					jusoInfo.addProperty("juso", address);
        					result.put("adressData", jusoInfo.toString());

        					break;
        				case "3":			// 외국국적동포
//        					result.put("rst", "SUCCESS");
        					addressInfo = data.get("frntnKoreanOndssResponse").getAsJsonObject().get("frntnKoreanOndssInfoList").getAsJsonArray().get(0).getAsJsonObject();

        					String stayEndDateStr = null;		// 만료일 체크 추가, 확인 필요

        					if (data.get("stayEndDe") != null) {
        						stayEndDateStr = data.get("stayEndDe").getAsString();
        					} else if (addressInfo.get("stayEndDe") != null) {
        						stayEndDateStr = addressInfo.get("stayEndDe").getAsString();
        					}

        					if (stayEndDateStr != null && com.onlinepowers.framework.util.StringUtils.isEmpty(stayEndDateStr.trim())) {
        						stayEndDateStr = "99991231";
        					}

        					LocalDate stayEndDate = LocalDate.parse(stayEndDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

        					if (now.isAfter(stayEndDate)) {		// 현재 날짜가 만료일 이후 기부 불가
        			    		result.put("rst", "EXPIRED");
        					} else {
    	    					locgovCheckCode = addressInfo.get("admnstmachCddr").getAsString();

    	    					//2023_10_25 행공센 응답코드에서 지자체코드 5번째 자리 대응 필요하여 0으로 수동 변경
//    	    					StringBuilder sb = new StringBuilder(locgovCheckCode.substring(0, 5));
//    	    					sb.setCharAt(4, '0');
//    	    					locgovCode = sb.toString();

//    	    					locgovCode = locgovCheckCode.substring(0, 5);
    	    					locgovCode = convertAdmCdToLocgovCode(locgovCheckCode);
    	    					upperLocgovCode = locgovCode.substring(0, 2) + "000";
//    	    					checkLocgovCode = donationParam.getLocgovCode();
    	    					address = addressInfo.get("adres").getAsString();

    	    					if (locgovCode.equals(checkLocgovCode) || upperLocgovCode.equals(checkLocgovCode)) {			// 기부 지자체 코드가 같거나 기부 지자체가 기부자 지자체의 상위 지자체이면 기부 불가
    	    						result.put("rst", "IN_LOCGOV");
    	    					} else {
    	    						result.put("rst", "SUCCESS");
    	    					}

    	    					result.put("locgovCode", locgovCode);

    	    					sidoListParam = new LocGovInfo();
    	    		        	sidoListParam.setLocgovCode(locgovCode);
    	    		        	locGovInfo= ngDonationMapper.getLocGovInfo(sidoListParam);
    	    		        	bassAdres = locGovInfo.getBassAdres();

    	    					result.put("bassAdres", bassAdres);

    	    					// 이름, 주민번호 등 값은 화면에서 쓰지 않아서 주소만 추가전송
    	    					jusoInfo = new JsonObject();
    	    					jusoInfo.addProperty("juso", address);
    	    					result.put("adressData", jusoInfo.toString());
        					}
        					break;
        				default:
        					log.error(getClass().getName() + " rsgstadresinfoForeigner frgnrSttusSe is invalid data :: " + frgnrSttusSe);
        					throw new IOException("거소 정보 조회 중 문제가 발생했습니다.");
        			}
            		result.put("foreignStatusCode", frgnrSttusSe);
    			} else {
        			JsonObject body = responseData.get("body").getAsJsonObject();
        			JsonObject errObj = body.get("message").getAsJsonObject();
            		result.put("errCode", errObj.get("errorCode").getAsString());
            		result.put("errMsg", errObj.get("errorMessage").getAsString());
    			}
    		} else {
    			JsonObject body = responseData.get("body").getAsJsonObject();
    			JsonObject errObj = body.get("message").getAsJsonObject();
        		result.put("errCode", errObj.get("errorCode").getAsString());
        		result.put("errMsg", errObj.get("errorMessage").getAsString());
    		}
    	} else {
    		result.put("errCode", "EMPTY_RESPONSE");
    		result.put("errMsg", "조회된 정보가 없습니다.");
    	}

		return result;
	}

	@Override
	public Map<String, Object> giroPayTest(HashMap<String, String> params) throws Exception {
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();
		String errorMsg = "";

        //디바이스(PC,모바일) 구분하여 팝업창 사이즈 조절
		String isMobile = null;
		String userAgent = StringUtils.defaultIfEmpty(params.get("userAgent"), "");

	    if(userAgent.indexOf("MOBI") > -1) {
	    	isMobile = "Y";
	    } else {
	    	isMobile = "N";
	    }

	    params.put("isMobile", isMobile);

        if(StringUtils.isBlank(StringUtils.defaultIfEmpty(params.get("giroNo"), ""))) {
        	errorMsg = "전자납부번호가 존재하지 않습니다.";
        	SERVICE_RELAY_RESULT.put("errorMsg", errorMsg);
        	return SERVICE_RELAY_RESULT;
        }

        HashMap<String, String> API_REQUEST = new HashMap<String, String>();
        API_REQUEST.put("sortCode", params.get("sortCode"));
        API_REQUEST.put("giroNo", params.get("giroNo"));
        API_REQUEST.put("elecNo", StringUtils.defaultIfEmpty(params.get("elecNo"), ""));
        API_REQUEST.put("payerName", params.get("payerName"));
        API_REQUEST.put("birthDate", StringUtils.defaultIfEmpty(params.get("birthDate"), ""));
        API_REQUEST.put("callDt", params.get("callDt"));
        API_REQUEST.put("successRU", StringUtils.defaultIfEmpty(params.get("domain"), "")+"/donation/giro-success.html");
        API_REQUEST.put("failRU", StringUtils.defaultIfEmpty(params.get("domain"), "")+"/donation/giro-fail.html");
        String json = new ObjectMapper().writeValueAsString(API_REQUEST);

        AES256Utils aes256 = new AES256Utils(aes256Key, aes256Iv);
        String encData = aes256.encrypt(json);

        SERVICE_RELAY_RESULT.put("data", encData);
        SERVICE_RELAY_RESULT.put("isMobile", StringUtils.defaultIfEmpty(params.get("isMobile"), "N"));
        SERVICE_RELAY_RESULT.put("popUrl", "https://test.giro.or.kr/epay/hometown/index.do");

		return SERVICE_RELAY_RESULT;
	}

	// 행정기관코드로 법정동 코드 조회
	private String convertAdmCdToLocgovCode(String locgovCheckCode) {
//		String locgovCode = ngDonationMapper.selectLocgovByAdmCd(locgovCheckCode);
//		if (com.onlinepowers.framework.util.StringUtils.isEmpty(locgovCode)) {			// 테이블에 없는 케이스가 있어서 처리
//			locgovCode = locgovCheckCode.substring(0, 5);
//			String mappingLocgovCode = ngDonationMapper.getMappingLocgovCode(locgovCode);
//			if (com.onlinepowers.framework.util.StringUtils.isEmpty(mappingLocgovCode)) {
//				if (locgovCode.startsWith("36")) {
//					locgovCode = "36000";
//				} else if (locgovCode.startsWith("50")) {
//					locgovCode = "50000";
//				} else {
//					locgovCode = locgovCode.substring(0, 4) + "0";
//				}
//			}
//		}
//
//		return locgovCode;
		locgovCheckCode = locgovCheckCode.substring(0, 5);
		return ngDonationMapper.selectLocgovByAdmCd(locgovCheckCode);
	}


//	private JsonObject getTestData() {
////		String test = "{"
////		+ "    \"data\": ["
////		+ "        {"
//////		+ "            \"stayQualfEndDate\": \"20231220\","
////		+ "            \"stayQualfEndDate\": \"\","
////		+ "            \"mojResultCode\": \"SUC.WS.000\","
////		+ "            \"mojResultMssage\": \"message success\","
////		+ "            \"stayAreaInfoResponse\": {"
////		+ "                \"allInqireCo\": \"1\","
////		+ "                \"stayAreaInfoList\": ["
////		+ "                    {"
////		+ "						\"stayAreaChangeDate\": \"20220801\","
////		+ "                        \"adres\": \"서울특별시 중구 남대문로5가 6-8, 302호 사무실\","
////		+ "                        \"admnstmachCddr\": \"4511355000\""
////		+ "                    }"
////		+ "                ],"
////		+ "                \"mojResultCode\": \"SUC.WS.000\","
////		+ "                \"mojResultMssage\": \"message success\""
////		+ "            }"
////		+ "        }"
////		+ "    ]"
////		+ "}";
//
//		String test = "{"
//		+ "    \"data\": ["
//		+ "        {"
//		+ "				\"stayEndDe\" : \"20231230\""
//		+ "             , \"mojResultCode\": \"SUC.WS.000\""
//		+ "             , \"mojResultMssage\": \"message success\""
//		+ "             , \"frntnKoreanOndssResponse\": {"
//		+ "                \"frntnKoreanOndssInfoList\": ["
//		+ "                    {"
//		+ "                        \"ondssChangeSttemntRceptDe\": \"20211029\","
//		+ "                        \"adres\": \"서울특별시 중구 남대문로5가 6-8, 302호 사무실\","
//		+ "                        \"admnstmachCddr\": \"4146555500\""
//		+ "                    }"
//		+ "                ]"
//		+ "                , \"mojResultCode\": \"SUC.WS.000\""
//		+ "                , \"mojResultMssage\": \"message success\""
//		+ "            }"
//		+ "        }"
//		+ "    ]"
//		+ "}";
//		//4511355000
//		//4139059600
//		JsonObject testData = new JsonObject();
//		testData.addProperty("body", test);
//		testData.addProperty("responseCode", 200);
//		testData.addProperty("frgnrSttusSe", "3");
//		return testData;
//	}

	@Override
	public Map<String, Object> localSunapConfirm(NextBugaRequestDto nextBugaRequestDto) throws Exception {
		HashMap<String, Object> API_REQUEST = new HashMap<>();
		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();

		try {
			//자치단체코드 조회
			HashMap<String,String> searchMap = new HashMap<>();
			searchMap.put("locgovCode", nextBugaRequestDto.getLocgovCode());
			Locgov locgov = ngDonationMapper.getLocgovInfo(searchMap);

			API_REQUEST.put("epayNo", nextBugaRequestDto.getEpayNo());
			API_REQUEST.put("linkTrgtCd", LINK_TRGT_CD);
			API_REQUEST.put("sgbCd", locgov.getAdministInsttCode());
			API_REQUEST.put("linkMngKey", nextBugaRequestDto.getLinkMngKey());

			//세외수입 수납이력확인 연계요청
			String response;
			if (com.onlinepowers.framework.common.ServiceType.LOCAL) {
				// LOCAL DEV ONLY: 세외수입 수납이력 연계서버에 닿지 않으므로, 로컬에서 우회한 결제는 납부된 것으로 응답한다.
				response = "{\"status\":\"SUCCESS\"}";
			} else {
				response = restFulToRelayServerHttps(nextSunapInfoRequestUrl, API_REQUEST, "");
			}
			ObjectMapper objectMapper = new ObjectMapper();
			SERVICE_RELAY_RESULT = objectMapper.readValue(response, new TypeReference<Map<String, Object>>() {});

		} catch (UserException e) {
			log.error("■■■API■■■ localSunapConfirm UserException : {} , {}", nextBugaRequestDto.getEpayNo(), e.getStackTrace()[0]);
			SERVICE_RELAY_RESULT.put("status", "ERROR");
			SERVICE_RELAY_RESULT.put("errorMsg", e.getMessage());
		} catch(Exception e) {
    		log.error("■■■API■■■ localSunapConfirm EXCEPTION : {} , {} ", nextBugaRequestDto.getEpayNo(), e.getStackTrace()[0]);
    		log.error(e.getMessage());
    		SERVICE_RELAY_RESULT.put("status", "ERROR");
			SERVICE_RELAY_RESULT.put("errorMsg", e.getMessage());
    	}

		return SERVICE_RELAY_RESULT;
	}

}
