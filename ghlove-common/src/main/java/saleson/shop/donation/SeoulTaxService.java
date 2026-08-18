package saleson.shop.donation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.util.DateUtils;

import lombok.extern.slf4j.Slf4j;
import saleson.api.common.enumerated.CntrSttusCode;
import saleson.api.common.enumerated.DonationError;
import saleson.api.common.exception.DonationException;
import saleson.common.enumeration.SmsType;
import saleson.common.sms.SmsIpsService;
import saleson.common.utils.UserUtils;
import saleson.shop.designateddonation.DesignatedDonationService;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.donation.dto.DonationBugaResponseDto;
import saleson.shop.donation.dto.DonationOverPaymentRequestDto;
import saleson.shop.donation.dto.DonationOverPaymentResponseDto;
import saleson.shop.donation.dto.DonationSunapProcessRequestDto;
import saleson.shop.donation.dto.DonationSunapProcessResponseDto;
import saleson.shop.donation.dto.DonatorInfoRequestDto;
import saleson.shop.donation.dto.DonatorInfoResponseDto;
import saleson.shop.donation.dto.RsgstadresForeignRequestDto;
import saleson.shop.donation.dto.RsgstadresForeignResponseDto;
import saleson.shop.donation.dto.SeoulBugaRequestDto;
import saleson.shop.donation.dto.SeoulParamDto;
import saleson.shop.donation.dto.SeoulSunapResponseDto;
import saleson.shop.donation.entity.GCntrEntity;
import saleson.shop.donation.repository.DonationRepository;
import saleson.shop.donation.repository.DonatorRepository;
import saleson.shop.donation.repository.LocgovRepository;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.support.SSLSocketFactoryMaker;

@Slf4j
@Service
public class SeoulTaxService extends DonationService {

	public SeoulTaxService(DonatorRepository donatorRepository, DonationRepository donationRepository,
			LocgovRepository locgovRepository) {
		super(donatorRepository, donationRepository, locgovRepository);
	}

	@Autowired
	private DesignatedDonationService designatedDonationService;

	@Autowired
	private NgDonationMapper ngDonationMapper;

	@Autowired
    NgDonationService ngDonationService;

	@Autowired
	private DonationRepository donationRepository;

	@Autowired
	private LocgovRepository locgovRepository;

	@Autowired
	private SmsIpsService smsIpsService;

	//서울세외 부과API
	@Value("${outconn.seoul-buga-url}")
    private String seoulBugaUrl;

	//서울세외 수납확인API
	@Value("${outconn.seoul-sunap-url}")
    private String seoulSunapUrl;

//	private SSLSocketFactory _sslSockFactory;

	private boolean CERTIFICATE = true;

	private int TIMEOUT_VALUE = 10000;   // 10초

	/**
	 * <pre>
	 * comment       : 서울세외 부과 요청
	 * preMethodName :
	 * author        :
	 * date          : 2024. 4. 12.
	 *
	 * </pre>
	 * @param seoulBugaRequestDto
	 * @return
	 * DonationBugaResponseDto
	 */
	public DonationBugaResponseDto bugaRequest(SeoulParamDto seoulParamDto) throws Exception {
		log.info("서비스 진입 확인");

		if(!"0".equals(seoulParamDto.getForeignStatusCode())) {
			foreignStatusCheck(seoulParamDto);
		}

		if (seoulParamDto.getPrjId() > 0) {
			DesignatedDonationSearchParam searchParam = new DesignatedDonationSearchParam();
			searchParam.setPrjId(seoulParamDto.getPrjId());
			searchParam.setLocgovCode(seoulParamDto.getJijacheCd());
			DesignatedDonation designatedDonation = designatedDonationService.selectDesignatedDonationDetail(searchParam);
			if (designatedDonation == null) {
				throw new DonationException(DonationError.DESIGNATED_INFO_ERROR);
			}

			if (!designatedDonation.getSUCCESS().equals(designatedDonation.getResultCode())) {
				return DonationBugaResponseDto.builder().resultCode(designatedDonation.getResultCode()).build();
			}
		}

		//시구코드 조회
		var locgovInfo = locgovRepository.findById(seoulParamDto.getJijacheCd()).orElseThrow(() -> new DonationException(DonationError.INSTT_INFORMATION_NOT_EXIST));
		log.info("행정코드 확인 >> : " +locgovInfo.getAdministInsttCode());

		var API_REQUEST = SeoulBugaRequestDto.builder()
											 .comReqDt(DateUtils.getToday("yyyyMMdd"))
											 .comReqTm(DateUtils.getToday("HHmmss"))
											 .systemCd(seoulParamDto.getSystemCd())
											 .jijacheCd(seoulParamDto.getJijacheCd())
											 .siguCd(locgovInfo.getAdministInsttCode())
											 .semokCd(seoulParamDto.getSemokCd())
											 .taxYm(DateUtils.getToday("yyyyMM")).taxGubun(seoulParamDto.getTaxGubun())
											 .buseoCd("")
											 .taxNo("")
											 .sidoCd(seoulParamDto.getSidoCd())
											 .napId(seoulParamDto.getNapId())
											 .napNm(seoulParamDto.getNapNm())
											 .napGubun(seoulParamDto.getNapGubun())
											 .taxAmt(seoulParamDto.getTaxAmt())
											 .sise(seoulParamDto.getTaxAmt())
											 .guse(0L)
											 .gukse(0L)
											 .gigum(0L)
											 .siseIja(0L)
											 .guseIja(0L)
											 .gukseIja(0L)
											 .gigumIja(0L)
											 .siseGasanAmt(0L)
											 .guseGasamAmt(0L)
											 .gukseGasanAmt(0L)
											 .gigumGasanAmt(0L)
											 .napMobilNo("")
											 .napTelNo("")
											 .napEmail("")
											 .resideStatus(seoulParamDto.getResideStatus())
											 .mulGubun(seoulParamDto.getMulGubun())
											 .mulNm(seoulParamDto.getMulNm())
											 .mulOcrSiguCd("")
											 .mulBdongriCd("")
											 .mulSpcCd("")
											 .mulBon("")
											 .mulBu("")
											 .mulTong("")
											 .mulBan("")
											 .mulAptNm("")
											 .mulDong("")
											 .mulHosu("")
											 .mulZipCd("")
											 .mulZipAddr("")
											 .mulDtlAddr("")
											 .hdongCd("")
											 .bookNo(ngDonationMapper.getBookNoSeoul())
											 .hangmok1("").hangmok2("")
											 .hangmok3("").hangmok4("")
											 .hangmok5("").hangmok6("")
											 .gasanRateGubun("")
											 .specialRate(0)
											 .specialRateApplySayu("")
											 .bigo("")
											 .ocrSiguCd("")
											 .ocrBuseoCd("")
											 .etc1("")
											 .lastWorkId("")
											 .lastWorkDate("")
											 .vatAmt(0L).gasanAmtSkipGubun("")
											 .sysGubun(seoulParamDto.getSysGubun())
											 .napDzipCd("")
											 .napDzipAddr("")
											 .napDdtlAddr("")
											 .napDrefAddr("")
											 .etcCm1("").etcCm2("")
											 .etcCm3("").etcCm4("")
											 .etcCm5("")
											 .napBldBon("")
											 .napBldBu("")
											 .napDoroCd("")
											 .napUndYn("")
											 .napbuYmd("")
											 .build();

		if (!UserUtils.isUserLogin()) {
			//throw new DonationException(DonationError.LOGIN_INFO_NOT_EXIST);
		}

		UserDetail userDetail = UserUtils.getUserDetail();

		ObjectMapper mapper = new ObjectMapper();

		/*
		 * API_REQUEST 를 json 타입으로 변환
		String toStrRequest = mapper.writeValueAsString(API_REQUEST);
        log.info(">>>>>>>>>>>>> DTO TO JSON STRING : " + toStrRequest);

		String response = restFulToRelayServer(seoulBugaUrl, toStrRequest);
		*/

		//@SuppressWarnings("unchecked")
		//HashMap<String, Object> requestMap = (HashMap<String, Object>) mapper.convertValue(API_REQUEST, Map.class);
		//String response = restFulToRelayServer(seoulBugaUrl, requestMap);

		//Object objList = JSONValue.parse(response);
        //JSONObject jsonObject = (JSONObject)objList;

		String errorCode =  "0";
		//String errorCode = StringUtils.defaultIfEmpty((String) jsonObject.get("errorCode"), "") ;

		String elctrnPayNo = "";

		// 서울세외 부과 결과가 성공일 경우
		if("0".equals(errorCode)) {
		//if("0".equals(errorCode)) {

			/*
			elctrnPayNo = String.valueOf(jsonObject.get("enapbuNo"));

			GCntrEntity gcntrEntity = GCntrEntity.builder()
									  .cntrSn(ngDonationMapper.getLinkMngKeyNextValue())
									  .cntrDe(DateUtils.getToday("yyyyMMdd"))
									  .userId(userDetail.getUserId())
									  .psitnLocgovCode(seoulParamDto.getUserRegionCd())
									  .cntrLocgovCode(seoulParamDto.getJijacheCd())
									  .cntrAmt(seoulParamDto.getTaxAmt())
									  .cntrPoint(0L)
									  .payValidDe(DateUtils.addDay(DateUtils.getToday("yyyyMMdd"), 1))
									  .cntrPathCode("100")
									  .cntrSttusCode(CntrSttusCode.DECLARATION.getCode())
									  .elctrnPayNo(elctrnPayNo)
									  .seoulTrgetAt("Y")
									  .infoAgreAt("1")
									  .rtnpsntReqstCode(seoulParamDto.getPresentType())
									  .frstRegisterId(userDetail.getUserId())
									  .frstRegistPnttm(LocalDateTime.now())
									  .deleteAt("N")
									  .foreignStatusCode(seoulParamDto.getForeignStatusCode())
									  .prjId(seoulParamDto.getPrjId())
									  .build();
			*/

			/*
			gCntrInfo.setCntrSn(ngDonationMapper.getLinkMngKeyNextValue());
			gCntrInfo.setCntrDe(DateUtils.getToday("yyyyMMdd"));
			gCntrInfo.setUserId(userDetail.getUserId());
			gCntrInfo.setPsitnLocgovCode(seoulParamDto.getUserRegionCd());
			gCntrInfo.setCntrLocgovCode(seoulParamDto.getJijacheCd());
			gCntrInfo.setCntrAmt(seoulParamDto.getTaxAmt());
			gCntrInfo.setCntrPoint(0L);
			gCntrInfo.setPayValidDe(DateUtils.addDay(DateUtils.getToday("yyyyMMdd"), 1));
			gCntrInfo.setCntrPathCode("100");
			gCntrInfo.setCntrSttusCode(CntrSttusCode.DECLARATION.getCode());
			gCntrInfo.setElctrnPayNo(String.valueOf(jsonObject.get("enapbuNo")));
			gCntrInfo.setSeoulTrgetAt("Y");
			gCntrInfo.setInfoAgreAt("1");
			gCntrInfo.setRtnpsntReqstCode(seoulParamDto.getPresentType());
			gCntrInfo.setFrstRegisterId(userDetail.getUserId());
			gCntrInfo.setDeleteAt("N");
			gCntrInfo.setForeignStatusCode(seoulParamDto.getForeignStatusCode());
			gCntrInfo.setPrjId(seoulParamDto.getPrjId());
			*/

			//테스트
			var userId ="3000017";
			elctrnPayNo = "1234567789912303122";

			GCntrEntity gcntrEntity2 = GCntrEntity.builder()
									  .cntrSn(ngDonationMapper.getLinkMngKeyNextValue())
									  .cntrDe(DateUtils.getToday("yyyyMMdd"))
									  .userId(Long.parseLong(userId))
									  .psitnLocgovCode(seoulParamDto.getUserRegionCd())
									  .cntrLocgovCode(seoulParamDto.getJijacheCd())
									  .cntrAmt(seoulParamDto.getTaxAmt())
									  .cntrPoint(0L)
									  .payValidDe(DateUtils.addDay(DateUtils.getToday("yyyyMMdd"), 1))
									  .cntrPathCode("100")
									  .cntrSttusCode(CntrSttusCode.DECLARATION.getCode())
									  .elctrnPayNo(elctrnPayNo)
									  .seoulTrgetAt("Y")
									  .infoAgreAt("1")
									  .rtnpsntReqstCode(seoulParamDto.getPresentType())
									  .frstRegisterId(Long.parseLong(userId))
									  .frstRegistPnttm(LocalDateTime.now())
									  .deleteAt("N")
									  .foreignStatusCode(seoulParamDto.getForeignStatusCode())
									  .dsgnDntnBizId(seoulParamDto.getDsgnDntnBizId())
									  .build();

			donationRepository.save(gcntrEntity2);
		}else {
			/*return DonationBugaResponseDto.builder()
										  .errorCode(errorCode)
										  .errorMsg(StringUtils.defaultIfEmpty((String) jsonObject.get("errorMsg"), ""))
										  .build();*/
		}

		String mngNo = ngDonationMapper.getMngNoEtax();	// 이택스 전문관리 대장번호 채번

		return mockSeoulBugaResponse(mngNo);

		//return DonationBugaResponseDto.builder().errorCode(errorCode).enapbuNo(elctrnPayNo).mngNo(mngNo).build();
	}

	private DonationBugaResponseDto mockSeoulBugaResponse(String mngNo) {
		// elctrnPayNo || enapbuNo 통일 필요?!
		return DonationBugaResponseDto.builder().errorCode("0").elctrnPayNo("1234567789912303").mngNo(mngNo).build();
	}

	private void foreignStatusCheck(SeoulParamDto seoulParamDto) throws Exception {

		RsgstadresForeignRequestDto foreignRequestParam = new RsgstadresForeignRequestDto();
		foreignRequestParam.setUserName(seoulParamDto.getNapNm());
		foreignRequestParam.setLocgovCode(seoulParamDto.getJijacheCd());
		foreignRequestParam.setJuminNo(seoulParamDto.getNapId().substring(6));;

		//외국인 미래행공 연계
		RsgstadresForeignResponseDto rsgstadresForeignResponse = rsgstadresForeigner(foreignRequestParam);
		if(rsgstadresForeignResponse != null) {}
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
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String restFulToRelayServer(String API_URL, HashMap<String, Object> API_REQUEST) throws Exception  {
        String response = "";
		URL url = new URL(API_URL);	//행공센 연계

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
            http.setReadTimeout(TIMEOUT_VALUE);
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            http.setRequestProperty("Content-Type", "application/json");

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
	 * comment       : 기부자 정보 조회
	 * preMethodName :
	 * author        :
	 * date          : 2024. 4. 8.
	 *
	 * </pre>
	 * @param donatorRequestDto
	 * @return
	 * DonatorInfoResponseDto
	 * @throws Exception
	 */
	public DonatorInfoResponseDto donatorInfo() throws Exception {

		UserDetail userDetail = UserUtils.getUserDetail();

		DonatorInfoRequestDto donatorInfoRequestDto = new DonatorInfoRequestDto();
		donatorInfoRequestDto.setUserId(userDetail.getUserId());
		donatorInfoRequestDto.setMberCi(StringUtils.defaultIfEmpty(userDetail.getMberCi(),""));

		return donatorInfo(donatorInfoRequestDto);
	}

	/**
	 * <pre>
	 * comment       : 수납처리
	 * preMethodName :
	 * author        :
	 * date          : 2024. 4. 17.
	 *
	 * </pre>
	 * @param donationSunapProcessRequestDto
	 * @return
	 * DonationSunapProcessResponseDto
	 */
	public DonationSunapProcessResponseDto seoulSunapProcess(DonationSunapProcessRequestDto donationSunapProcessRequestDto) {

		UserDetail userDetail = UserUtils.getUserDetail();

        if (!UserUtils.isUserLogin()) {
            throw new DonationException(DonationError.LOGIN_INFO_NOT_EXIST);
        }

        // enapbu 가 아닌 cntrsn 으로 수납처리하여 확인필요!
		var sunapProcess = sunapProcess(donationSunapProcessRequestDto);

		// 기부 감사 인사
		SeoulParam seoulParam = new SeoulParam();
		seoulParam.setJijacheCd(donationSunapProcessRequestDto.getJijacheCd());
		seoulParam.setUserId(String.valueOf(userDetail.getUserId()));
		GiveUserSmsInfo info = ngDonationMapper.getSmsSendGiveUserInfo(seoulParam);
		smsIpsService.giveSendSms(Arrays.asList(info), SmsType.DONATION);

		//명예기부
		ngDonationService.insertHonorCntrbtr(donationSunapProcessRequestDto.getJijacheCd(), userDetail.getUserId());

		return sunapProcess;
	}

	/**
	 * <pre>
	 * comment       : 과오납 처리
	 * preMethodName :
	 * author        :
	 * date          : 2024. 4. 5.
	 *
	 * </pre>
	 * @param donationOverPaymentRequestDto
	 * @return
	 * DonationOverPaymentResponseDto
	 */
	public DonationOverPaymentResponseDto overPayment(DonationOverPaymentRequestDto donationOverPaymentRequestDto) {

		return DonationOverPaymentResponseDto.builder().build();
	}

	/**
	 * <pre>
	 * comment       : 서울시 이택스 - 실시간 수납정보 조회
	 * preMethodName :
	 * author        :
	 * date          : 2024. 4. 23.
	 *
	 * </pre>
	 * @param seoulParamDto
	 * @return
	 * SeoulSunapResponseDto
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public SeoulSunapResponseDto etaxSunapInfo(SeoulParamDto seoulParamDto) throws Exception {
		HashMap<String, Object> API_REQUEST = new HashMap<>();

		API_REQUEST.put("COM_REQ_MECHE", "GHLOVE");								// 요청매체
		API_REQUEST.put("COM_REQ_DT", DateUtils.getToday("yyyyMMdd"));			// 요청일자
		API_REQUEST.put("COM_REQ_TM", DateUtils.getToday("HHmmss"));			// 요청일시
		API_REQUEST.put("COM_PAY_MSG_NO", ngDonationMapper.getMngNoEtax());		// 전문대장관리번호
		API_REQUEST.put("DATA_CNT", 1);											// 데이터 건수
		API_REQUEST.put("systemCd", seoulParamDto.getSystemCd());				// 인터페이스 구분코드
		API_REQUEST.put("jijacheCd", seoulParamDto.getJijacheCd());				// 지자체코드

        JSONObject jo1 = new JSONObject();
        jo1.put("EPAY_NO", seoulParamDto.getEnapbuNo());
        JSONArray ja = new JSONArray();
        ja.add(jo1);

        API_REQUEST.put("ARR_BU_INFO", ja);

    	String response = restFulToRelayServer(seoulSunapUrl, API_REQUEST);

    	ObjectMapper mapper = new ObjectMapper();

    	HashMap<String, Object> res = (HashMap<String, Object>) mapper.convertValue(response, Map.class);
    	List<Map<String, Object>> arrResult = (List<Map<String, Object>>) res.get("ARR_RESULT");

        String sunapYn = StringUtils.defaultIfEmpty((String) arrResult.get(0).get("SUNAP_YN"), "");
        String rstCd = StringUtils.defaultIfEmpty((String) res.get("RST_CD"), "");
        String rstMsg = StringUtils.defaultIfEmpty((String) res.get("RST_MSG"), "");

		return SeoulSunapResponseDto.builder()
				.rstCd(rstCd)
				.rstMsg(rstMsg)
				.sunapYn(sunapYn)
				.build();
	}

}
