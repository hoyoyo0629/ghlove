package saleson.shop.donation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.DateUtils;

import lombok.extern.slf4j.Slf4j;
import saleson.api.common.enumerated.ApiError;
import saleson.common.enumeration.SmsType;
import saleson.common.security.crypto.RsaCryptor;
import saleson.common.sms.SmsIpsService;
import saleson.common.utils.UserUtils;
import saleson.shop.designateddonation.DesignatedDonationService;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.donation.domain.CntrLmtt;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.donation.domain.TempPrivateKeyInfo;
import saleson.shop.donation.dto.CntrLmttRequestDto;
import saleson.shop.donation.dto.CntrLmttResponseDto;
import saleson.shop.donation.dto.DonationBugaResponseDto;
import saleson.shop.donation.dto.DonationOverPaymentRequestDto;
import saleson.shop.donation.dto.DonationOverPaymentResponseDto;
import saleson.shop.donation.dto.DonationSunapProcessRequestDto;
import saleson.shop.donation.dto.DonationSunapProcessResponseDto;
import saleson.shop.donation.dto.DonatorInfoRequestDto;
import saleson.shop.donation.dto.DonatorInfoResponseDto;
import saleson.shop.donation.dto.GiroPayRequestDto;
import saleson.shop.donation.dto.GiroPayResponseDto;
import saleson.shop.donation.dto.IntrstLocgovRequestDto;
import saleson.shop.donation.dto.IntrstLocgovResponseDto;
import saleson.shop.donation.dto.RegionBugaParamDto;
import saleson.shop.donation.dto.RegionBugaRequestDto;
import saleson.shop.donation.dto.RsgstadresForeignRequestDto;
import saleson.shop.donation.dto.RsgstadresForeignResponseDto;
import saleson.shop.donation.dto.RsgstadresRequestDto;
import saleson.shop.donation.dto.RsgstadresResponseDto;
import saleson.shop.donation.entity.GCntrEntity;
import saleson.shop.donation.entity.GIntrstLocgovEntity;
import saleson.shop.donation.entity.GLocgovEntity;
import saleson.shop.donation.repository.DonationRepository;
import saleson.shop.donation.repository.DonatorRepository;
import saleson.shop.donation.repository.IntrstLocgovRepository;
import saleson.shop.donation.repository.LocgovRepository;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.donation.support.SidoListParam;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.support.SSLSocketFactoryMaker;

@Slf4j
@Service
public class RegionTaxService extends DonationService {

	@Autowired
    NgDonationService ngDonationService;						//기부 공통 서비스

	@Autowired
	private DonationService donationService;

	@Autowired
	private SmsIpsService smsIpsService;

	@Autowired
	private DesignatedDonationService designatedDonationService;

	@Autowired
	private NgDonationMapper ngDonationMapper;

	@Autowired
	private DonationRepository donationRepository;

	@Autowired
	private LocgovRepository locgovRepository;

	@Autowired
	private IntrstLocgovRepository intrstLocgovRepository;

	@Autowired
	private TempPrivateKeyInfoRepository tempPrivateKeyInfoRepository;

	private static String LINK_TRGT_CD = "1741000HLE01001";

//	private SSLSocketFactory _sslSockFactory;

	private static final int CONNECTION_TIMEOUT = 10000;   // 10초

	private static final int READ_TIMEOUT = 30000;

	@Value("${outconn.korea-api.loginUrl}")
    private String koreaApiLoginUrl;

	@Value("${outconn.korea-api.loginIp}")
    private String koreaApiLoginIp;

	@Value("${outconn.korea-api.actionUrl}")
    private String koreaApiActionUrl;

	@Value("${next.buga-request-url}")
    private String nextBugaRequestUrl;

	private boolean CERTIFICATE = true;

	public RegionTaxService(DonatorRepository donatorRepository, DonationRepository donationRepository,
			LocgovRepository locgovRepository) {
		super(donatorRepository, donationRepository, locgovRepository);
	}

	/**
	 * <pre>
	 * comment       : 지방세외 부과 요청
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 5.
	 *
	 * </pre>
	 * @param regionBugaRequestDto
	 * @return
	 * DonationBugaResponseDto
	 */
	public DonationBugaResponseDto bugaRequest(RegionBugaParamDto regionBugaParamDto) {
		RegionBugaRequestDto regionBugaRequestDto;
		DonationBugaResponseDto donationBugaResponseDto = null;
		DonationBugaResponseDto donationBugaResponseDto1;

		if (Long.valueOf(regionBugaParamDto.getPrjId()) > 0) {
			DesignatedDonationSearchParam searchParam = new DesignatedDonationSearchParam();
			searchParam.setPrjId(Long.valueOf(regionBugaParamDto.getPrjId()));
			searchParam.setLocgovCode(regionBugaParamDto.getCntrLocgovCode());
			DesignatedDonation designatedDonation = designatedDonationService.selectDesignatedDonationDetail(searchParam);
			if (designatedDonation == null) {
				throw new UserException("특정사업기부 사업 또는 지자체 정보가 올바르지 않습니다.");
			}
			if (!designatedDonation.getSUCCESS().equals(designatedDonation.getResultCode())) {
				donationBugaResponseDto = DonationBugaResponseDto.builder()
												.linkRstCd(designatedDonation.getResultCode())
												.build();
				return donationBugaResponseDto;
			}
		}

		String locgovCode = regionBugaParamDto.getCntrLocgovCode();
		GLocgovEntity locgovEntity = locgovRepository.findByLocgovCode(locgovCode);

		String fisSp = locgovEntity.getFisSp();
		String strLocgovCode = locgovEntity.getLocgovCode().substring(2);
		if(!fisSp.isEmpty()) {
			if("000".equals(strLocgovCode)) {
				fisSp = "31";
			}else if(!"000".equals(strLocgovCode)) {
				fisSp = "41";
			}
		}
    	String linkMngKey = ngDonationMapper.getLinkMngKeyNextValue();

    	// 2024.03.04 개발원 요청 특별회계(51, 61)일 경우 특별회계사업코드 0000이 아닌 7092로 변경
    	String strSpclFisBizCd = "0000";
		if(fisSp.equals("51") || fisSp.equals("61"))  strSpclFisBizCd = "7092";

		regionBugaRequestDto = RegionBugaRequestDto.builder()
				.sgbCd(locgovEntity.getAdministInsttCode())
				.linkTrgtCd(LINK_TRGT_CD)
				.linkMngKey(linkMngKey)
				.dptCd(locgovEntity.getProcessDeptCode().substring(0, locgovEntity.getProcessDeptCode().length() - 4))
				.spclFisBizCd(strSpclFisBizCd)
				.fyr(DateUtils.getToday("yyyy"))
				.actSeCd(fisSp)
				.rprsTxmCd("224102")
				.operItemCd("000")
				.lvyYmd(DateUtils.getToday("yyyyMMdd"))
				.frstPctAmt(regionBugaParamDto.getFrstPctAmt())
				.frstPidYmd(DateUtils.getToday("yyyyMMdd"))
				.pyrSeCd("01")
				.pyrSttCd("10")
				.lotnoRoadAddrSeCd("02")
				.mngItemCn1("고향사랑기부금")
				.cntrPathCode(regionBugaParamDto.getCntrPathCode())
				.build();

		/*ObjectMapper objectMapper = new ObjectMapper();
		@SuppressWarnings("unchecked")
		HashMap<String, Object> requestMap = (HashMap<String, Object>) objectMapper.convertValue(regionBugaRequestDto, Map.class);

		try {
			donationBugaResponseDto = this.restFulToRelayServerHttps(requestMap, "");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		donationBugaResponseDto = DonationBugaResponseDto.builder()
														.linkRstCd("000").linkRstMsg("테스트:1111111")
														.linkMngKey(linkMngKey).linkTrgtCd(LINK_TRGT_CD)
														.build();

		String linkRstCd 	= StringUtils.defaultIfEmpty((String) donationBugaResponseDto.getLinkRstCd(), "");
        String linkRstMsg 	= StringUtils.defaultIfEmpty((String) donationBugaResponseDto.getLinkRstMsg(), "");
        String epayNo 		= "";
        if(!linkRstMsg.isEmpty()) epayNo = StringUtils.defaultIfEmpty(linkRstMsg.split(":")[1], "").trim();

		if(linkRstCd.equals("000") && !epayNo.equals("")) {

			regionBugaRequestDto.setElctrnPayNo(epayNo);

			UserDetail userDetail = UserUtils.getUserDetail();

			long userId = userDetail.getUserId();
			userId = 39447;

			GCntrEntity gcntrEntity = GCntrEntity.builder()
								.cntrSn(regionBugaRequestDto.getLinkMngKey())
								.cntrDe(regionBugaRequestDto.getLvyYmd())
								.psitnLocgovCode(regionBugaParamDto.getPsitnLocgovCode())
								.cntrLocgovCode(regionBugaParamDto.getCntrLocgovCode())
								.cntrAmt(Long.parseLong(regionBugaRequestDto.getFrstPctAmt()))
								.cntrPoint(Long.parseLong("0"))
								.payValidDe(regionBugaRequestDto.getLvyYmd())
								.cntrPathCode(regionBugaParamDto.getCntrPathCode())
								.cntrSttusCode("100")
								.elctrnPayNo(epayNo)
								.seoulTrgetAt("N")
								.infoAgreAt("1")
								.rtnpsntReqstCode(regionBugaParamDto.getPresentType())
								.deleteAt("N")
								.userId(userId)
								.foreignStatusCode(regionBugaParamDto.getForeignStatusCode())
								.dsgnDntnBizId(regionBugaParamDto.getDsgnDntnBizId())
								.frstRegisterId(userId)
								.frstRegistPnttm(LocalDateTime.now())
								.build();

			donationRepository.save(gcntrEntity);
        }

		donationBugaResponseDto1 = donationBugaResponseDto.toBuilder()
								.elctrnPayNo(epayNo)
								.build();

		return donationBugaResponseDto1;
	}

	public DonationBugaResponseDto mockBugaRequest(RegionBugaParamDto regionBugaParamDto) {

		log.debug("======= : "+regionBugaParamDto);
//		throw new DonationException(DonationError.BUGA_REQUEST_FAIL, "세액설정오류");
		return donationService.mockRegionBugaRequest(regionBugaParamDto);
		//return null;
	}

	/**
	 * <pre>
	 * comment       : 수납 처리
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 5.
	 *
	 * </pre>
	 * @param donationSunapProcessRequestDto
	 * @return
	 * DonationSunapProcessResponseDto
	 */
	public DonationSunapProcessResponseDto sunapProcess(DonationSunapProcessRequestDto donationSunapProcessRequestDto) {
		var sunapProcess = donationService.sunapProcess(donationSunapProcessRequestDto);

		UserDetail userDetail = UserUtils.getUserDetail();
		long userId = userDetail.getUserId();
		userId = 39447;

		// 기부 감사 인사
		SeoulParam seoulParam = new SeoulParam();
		seoulParam.setUserId(String.valueOf(userId));
		seoulParam.setJijacheCd(donationSunapProcessRequestDto.getJijacheCd());
		GiveUserSmsInfo info = ngDonationMapper.getSmsSendGiveUserInfo(seoulParam);
		smsIpsService.giveSendSms(Arrays.asList(info), SmsType.DONATION);

		// 명예기부자 선정 로직 추가
        ngDonationService.insertHonorCntrbtr(seoulParam.getJijacheCd(), userId);

		return sunapProcess;
	}

	/**
	 * <pre>
	 * comment       : 과오납 처리
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 5.
	 *
	 * </pre>
	 * @param donationOverPaymentRequestDto
	 * @return
	 * DonationOverPaymentResponseDto
	 */
	public DonationOverPaymentResponseDto overPayment(DonationOverPaymentRequestDto donationOverPaymentRequestDto) {
		//return DonationOverPaymentResponseDto.builder().build();
		return donationService.overPayment(donationOverPaymentRequestDto);
	}

	/**
	 * <pre>
	 * comment       : 기부자 정보 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 8.
	 *
	 * </pre>
	 * @param donatorRequestDto
	 * @return
	 * DonatorInfoResponseDto
	 * @throws Exception
	 */
	public DonatorInfoResponseDto regionDonatorInfo(DonatorInfoRequestDto donatorInfoRequestDto) throws Exception {

		return donatorInfo(donatorInfoRequestDto);
		//return donatorInfo(userDetail.getUserId());
		//return DonatorInfoResponseDto.builder().build();
	}

	/**
	 * <pre>
	 * comment       : 금결원 지로 호출
	 * preMethodName :
	 * author        : csh
	 * date          : 2024. 4. 18.
	 *
	 * </pre>
	 * @param 	giroPayRequestDto
	 * @return 	GiroPayResponseDto
	 * @throws 	Exception
	 */
	public GiroPayResponseDto giroPay(GiroPayRequestDto giroPayRequestDto) throws Exception {
		/*Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();
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

        if (!UserUtils.isUserLogin()) {
        	errorMsg = "인증이 필요합니다";
        	log.error("/api/ngdonation/giroPay ERROR {}", errorMsg);
        	SERVICE_RELAY_RESULT.put("errorMsg", errorMsg);
        	return SERVICE_RELAY_RESULT;
        }

        if(StringUtils.isBlank(StringUtils.defaultIfEmpty(params.get("enapbuNo"), ""))) {
        	errorMsg = "전자납부번호가 존재하지 않습니다.";
        	log.error("/api/ngdonation/giroPay ERROR {}", errorMsg);
        	SERVICE_RELAY_RESULT.put("errorMsg", errorMsg);
        	return SERVICE_RELAY_RESULT;
        }

		GiroParam paramVo = new GiroParam();
    	paramVo.setJijacheCd(params.get("jijacheCd"));

		UserDetail userDetail = UserUtils.getUserDetail();
		userDetail = ngDonationMapper.getUserDetail(userDetail.getUserId());
        GiroParam giroMap = ngDonationMapper.getGiroData(paramVo);

        HashMap<String, String> API_REQUEST = new HashMap<String, String>();
        API_REQUEST.put("sortCode", giroMap.getUseInsttCode());
        API_REQUEST.put("giroNo", giroMap.getGiroNo());
        API_REQUEST.put("elecNo", StringUtils.defaultIfEmpty(params.get("enapbuNo"), ""));
        API_REQUEST.put("birthDate", userDetail.getBirthday().substring(2,8));
        API_REQUEST.put("payerName", UserUtils.getUser().getUserName());
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

		return SERVICE_RELAY_RESULT;*/

		//디바이스(PC,모바일) 구분하여 팝업창 사이즈 조절
		GiroPayResponseDto giroPayResponseDto = null;

		String isMobile = null;
		String userAgent = StringUtils.defaultIfEmpty(giroPayRequestDto.getUserAgent(), "");
		String errorMsg = "";

	    if(userAgent.indexOf("MOBI") > -1) {
	    	isMobile = "Y";
	    } else {
	    	isMobile = "N";
	    }

        if(!UserUtils.isUserLogin()) errorMsg = "인증이 필요합니다";
        if(StringUtils.isBlank(StringUtils.defaultIfEmpty(giroPayRequestDto.getEnapbuNo(), ""))) errorMsg = "전자납부번호가 존재하지 않습니다.";
        if(!"".equals(errorMsg)) {
        	giroPayResponseDto = GiroPayResponseDto.builder()
        			.errorMsg(errorMsg)
        			.build();

        	return giroPayResponseDto;
        }



		giroPayResponseDto = GiroPayResponseDto.builder()
				.isMobile(isMobile)
				.build();

		return giroPayResponseDto;
	}

	/**
	 * <pre>
	 * comment       : 지자체 기부 제한 조회
	 * preMethodName :
	 * author        : csh
	 * date          : 2024. 4. 19.
	 *
	 * </pre>
	 * @param 	cntrLmttRequestDto
	 * @return 	cntrLmttResponseDto
	 * @throws 	Exception
	 */
	public CntrLmttResponseDto locGovLmtt(CntrLmttRequestDto cntrLmttRequestDto) throws Exception {
		CntrLmttResponseDto cntrLmttResponseDto = null;
		SidoListParam donationParam = new SidoListParam();
		//donationParam.setCityCode(cntrLmttRequestDto.getCityCode());

		CntrLmtt cntrLmtt = ngDonationMapper.getCntrLmtt(donationParam);
		if(cntrLmtt != null) {
			cntrLmttResponseDto = CntrLmttResponseDto.builder()
													.lmttBgnDe(cntrLmtt.getLmttBgnDe())
													.lmttEndDe(cntrLmtt.getLmttEndDe())
													.violtResnCn(cntrLmtt.getVioltResnCn())
													.build();
		}

		return cntrLmttResponseDto;
	}

	/**
	 * <pre>
	 * comment       : 기부자 정보 조회
	 * preMethodName :
	 * author        : csh
	 * date          : 2024. 4. 24.
	 *
	 * </pre>
	 * @param 	donatorInfoRequestDto
	 * @return 	donatorInfoResponseDto
	 * @throws 	Exception
	 */
	public DonatorInfoResponseDto userCntrInfo(DonatorInfoRequestDto donatorInfoRequestDto) throws Exception {
		UserDetail userDetail = UserUtils.getUserDetail();
		long userId = userDetail.getUserId();
		userId = 39447;

		donatorInfoRequestDto.setUserId(userId);

		return donationService.donatorInfo(donatorInfoRequestDto);
	}

	/**
	 * <pre>
	 * comment       : 암호화 키 생성 및 전달
	 * preMethodName :
	 * author        : csh
	 * date          : 2024. 4. 24.
	 *
	 * </pre>
	 * @param
	 * @return 	Map<String, Object>
	 * @throws 	Exception
	 */
	public String getPublicKey() throws Exception {
		//Map<String, Object> resultMap = new HashMap<String, Object>();
		//resultMap.put("publicKeyStr", publicKeyStr);

		return this.getRsaPublicKey();
	}

	/**
     * <pre>
     * comment       : 행정망공동이용센터 주소간단조회 서비스 api 호출
     * preMethodName : rsgstadresinfo
     * author        : csh
     * date          : 2024. 4. 24.
     *
     * </pre>
     * @param
     * @return
     * @throws Exception
     *
     */
	public RsgstadresResponseDto rsgstadresInfo(RsgstadresRequestDto rsgstadresRequestDto) throws Exception {
		var rsgstadresInfo = donationService.rsgstadres(rsgstadresRequestDto);
		return rsgstadresInfo;
	}

	/**
	 * <pre>
	 * comment       : 행정정보공동이용시스템 api 호출 - 외국인
	 * preMethodName : rsgstadresinfoForeigner
	 * author        : csh
	 * date          : 2024. 4. 24.
	 *
	 * </pre>
	 * @param
	 * @return
	 * @throws 	Exception
	 */
	public RsgstadresForeignResponseDto rsgstadresInfoForeigner(RsgstadresForeignRequestDto rsgstadresForeignRequestDto) throws Exception {
		var rsgstadresForeigner = donationService.rsgstadresForeigner(rsgstadresForeignRequestDto);
		return rsgstadresForeigner;
	}

	/**
	 * <pre>
	 * comment       : 관심지자체 등록 API
	 * preMethodName : setIntrstLocgov
	 * author        : csh
	 * date          : 2024. 4. 24.
	 *
	 * </pre>
	 * @param
	 * @return
	 * @throws 	Exception
	 */
	public IntrstLocgovResponseDto setIntrstLocgov(IntrstLocgovRequestDto intrstLocgovRequestDto) throws Exception {
		UserDetail userDetail = UserUtils.getUserDetail();
		long userId = userDetail.getUserId();
			 userId = 0;
		GIntrstLocgovEntity gIntrstLocgovEntity = GIntrstLocgovEntity.builder()
													.userId(userId)
													.locgovCode(intrstLocgovRequestDto.getCityCode())
													.build();

		intrstLocgovRepository.save(gIntrstLocgovEntity);

		return IntrstLocgovResponseDto.builder().loginId(UserUtils.getLoginId()).userName(UserUtils.getUser().getUserName()).build();
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
	public DonationBugaResponseDto restFulToRelayServerHttps(HashMap<String, Object> API_REQUEST, String accessToken) throws Exception  {
		String response = "";
		URL url = new URL(nextBugaRequestUrl);	//연계
		final HttpsURLConnection https = (HttpsURLConnection) url.openConnection();

		/* test 환경에서는 인증서 오류가 날 수도 있다. 이 코드를 이용해 인증서 오류를 회피한다. */
		if (https instanceof HttpsURLConnection) {
			SSLSocketFactoryMaker factory = new SSLSocketFactoryMaker();
			((HttpsURLConnection) https).setSSLSocketFactory(factory.getSSLSocketFactory());
			((HttpsURLConnection) https).setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return CERTIFICATE;
				}
			});
		}

		try (AutoCloseable ac = () -> https.disconnect()) {
			https.setConnectTimeout(CONNECTION_TIMEOUT);
			https.setReadTimeout(READ_TIMEOUT);
			https.setDefaultUseCaches(false);
			https.setDoInput(true);
			https.setDoOutput(true);
			https.setRequestMethod("POST");

			https.setRequestProperty("Content-Type", "application/json");

			//국세청 TOKEN 가져올 시
			if(nextBugaRequestUrl.equals(koreaApiLoginUrl) || nextBugaRequestUrl.equals(koreaApiActionUrl)) {
				https.setRequestProperty("X-Forwarded-For", koreaApiLoginIp);
				if(nextBugaRequestUrl.equals(koreaApiActionUrl)) {
					https.setRequestProperty("Authorization","Bearer " + accessToken);
				}
			}

			JSONObject json = new JSONObject();
			for (String key : API_REQUEST.keySet()) {
				json.put(key, API_REQUEST.get(key));
			}

			try (final OutputStreamWriter osw = new OutputStreamWriter(https.getOutputStream(), "UTF-8")) {
            	PrintWriter writer = new PrintWriter(osw);
				writer.write(json.toString());
				writer.flush();
			}


            try (final InputStreamReader isr = new InputStreamReader(https.getInputStream(), "UTF-8")){
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

		ObjectMapper mapper = new ObjectMapper();
		DonationBugaResponseDto donationBugaResponseDto = mapper.readValue(response, DonationBugaResponseDto.class);

		return DonationBugaResponseDto.builder()
				.linkMngKey(donationBugaResponseDto.getLinkMngKey())
				.linkRstCd(donationBugaResponseDto.getLinkRstCd())
				.linkRstMsg(donationBugaResponseDto.getLinkRstMsg())
				.linkTrgtCd(donationBugaResponseDto.getLinkTrgtCd())
				.build();
	}


	private String getRsaPublicKey() {
		long userId = UserUtils.getUser().getUserId();
		if (userId <= 0) {
			throw new UserException(ApiError.NOT_EXIST_AUTH.toString(), "로그인 상태가 아닙니다.", "");
		}
		HashMap<String, String> keyPair = RsaCryptor.createKeypairAsString();
		TempPrivateKeyInfo keyInfo = new TempPrivateKeyInfo();
		keyInfo.setUserId(userId);

		if(keyPair.get("privateKeyStr") != null) {
			keyInfo.setPrivateKey(keyPair.get("privateKeyStr"));
		}

		tempPrivateKeyInfoRepository.save(keyInfo);
		String result = null;

		if(keyPair.get("publicKeyStr") != null) {
			result = keyPair.get("publicKeyStr");
		}

		return result ;
	}
}
