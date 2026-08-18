package saleson.shop.donation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.onlinepowers.framework.util.DateUtils;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.api.common.enumerated.BirthdayType;
import saleson.api.common.enumerated.CntrSttusCode;
import saleson.api.common.enumerated.DonationError;
import saleson.api.common.enumerated.LoginPathCode;
import saleson.api.common.enumerated.RtnpsntReqstCode;
import saleson.api.common.exception.DonationException;
import saleson.common.utils.UserUtils;
import saleson.shop.donation.dto.CntrLmttRequestDto;
import saleson.shop.donation.dto.CntrLmttResponseDto;
import saleson.shop.donation.dto.DonationBugaResponseDto;
import saleson.shop.donation.dto.DonationOverPaymentRequestDto;
import saleson.shop.donation.dto.DonationOverPaymentResponseDto;
import saleson.shop.donation.dto.DonationSunapProcessRequestDto;
import saleson.shop.donation.dto.DonationSunapProcessResponseDto;
import saleson.shop.donation.dto.DonatorInfoDto;
import saleson.shop.donation.dto.DonatorInfoRequestDto;
import saleson.shop.donation.dto.DonatorInfoResponseDto;
import saleson.shop.donation.dto.RegionBugaParamDto;
import saleson.shop.donation.dto.RsgstadresForeignRequestDto;
import saleson.shop.donation.dto.RsgstadresForeignResponseDto;
import saleson.shop.donation.dto.RsgstadresRequestDto;
import saleson.shop.donation.dto.RsgstadresResponseDto;
import saleson.shop.donation.entity.GAdmLocgovEntity;
import saleson.shop.donation.entity.GCtbnySetupEntity;
import saleson.shop.donation.entity.GInsttCodeEntity;
import saleson.shop.donation.entity.GLocgovEntity;
import saleson.shop.donation.entity.OpUserDetailEntity;
import saleson.shop.donation.repository.DonationRepository;
import saleson.shop.donation.repository.DonatorRepository;
import saleson.shop.donation.repository.LocgovRepository;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.support.SSLSocketFactoryMaker;

@Slf4j
@Service
@RequiredArgsConstructor
public class DonationService {

	@Autowired
	private final DonatorRepository donatorRepository;

	@Autowired
	private final DonationRepository donationRepository;

	@Autowired
	private final LocgovRepository locgovRepository;

	@Value("${outconn.admin-addressinfo-url}")
    private String adminAddressinfoUrl;

	@Value("${outconn.in-link-url}")
    private String inLinkUrl;

	private static final long MAX_CNTR_AMT_LIMIT = 5000000;

	private static final int CONNECTION_TIMEOUT = 10000;   // 10초

	private static final int READ_TIMEOUT = 30000;

	private SSLSocketFactory _sslSockFactory;

	private boolean CERTIFICATE = true;

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
	@Transactional
	public DonationSunapProcessResponseDto sunapProcess(DonationSunapProcessRequestDto donationSunapProcessRequestDto) {

		UserDetail userDetail = UserUtils.getUserDetail();
		String today = DateUtils.getToday("yyyyMMdd");
		BigDecimal pointRate = new BigDecimal("30");
		Long zeroPoint = Long.valueOf(0);

		var gCntrInfo = donationRepository.findById(donationSunapProcessRequestDto.getCntrSn()).orElseThrow(() -> new DonationException(DonationError.DONATION_INFORMATION_DOES_NOT_EXIST));
		Optional<GCtbnySetupEntity> optional =Optional.ofNullable(donationRepository.findByStdrYearAndLocgovCode(today.substring(0, 4), gCntrInfo.getCntrLocgovCode()));

		if(optional.isPresent()) {
			pointRate = optional.get().getPointRate();
		}

		String pointEndDe = String.valueOf(Integer.parseInt(gCntrInfo.getCntrDe().substring(0, 4))+ 5) + gCntrInfo.getCntrDe().substring(4, 6) + gCntrInfo.getCntrDe().substring(6, 8);

		gCntrInfo.setSttemntPayDe(gCntrInfo.getCntrDe());
		gCntrInfo.setPointEndDe(pointEndDe);
		if(gCntrInfo.getRtnpsntReqstCode().equals(RtnpsntReqstCode.PROVIDED.getCode())) {
			BigDecimal point = pointRate.divide(new BigDecimal("100")).multiply(new BigDecimal(gCntrInfo.getCntrAmt()));
			gCntrInfo.setCntrPoint(point.longValue());
			gCntrInfo.setCntrBlcePoint(point.longValue());
		} else {
			gCntrInfo.setCntrPoint(zeroPoint);
			gCntrInfo.setCntrBlcePoint(zeroPoint);
		}
		gCntrInfo.setCntrSttusCode(CntrSttusCode.PAY.getCode());
		gCntrInfo.setDeleteAt("N");
		gCntrInfo.setLastUpdtPnttm(LocalDateTime.now());
		gCntrInfo.setLastUpdusrId(userDetail.getUserId());

		donationRepository.save(gCntrInfo);
		return DonationSunapProcessResponseDto.builder().gCntr(gCntrInfo).build();
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
		UserDetail userDetail = UserUtils.getUserDetail();
		var gCntrInfo = donationRepository.findById(donationOverPaymentRequestDto.getCntrSn()).orElseThrow(() -> new DonationException(DonationError.DONATION_INFORMATION_DOES_NOT_EXIST));
		gCntrInfo.setSttemntPayDe(null);
		gCntrInfo.setPointEndDe(null);
		gCntrInfo.setCntrPoint(null);
		gCntrInfo.setCntrBlcePoint(null);
		gCntrInfo.setCntrSttusCode(CntrSttusCode.OVERPAYMENT.getCode());
		gCntrInfo.setDeleteAt("Y");
		gCntrInfo.setLastUpdtPnttm(LocalDateTime.now());
		gCntrInfo.setLastUpdusrId(userDetail.getUserId());

		donationRepository.save(gCntrInfo);
		return DonationOverPaymentResponseDto.builder().gCntr(gCntrInfo).build();
	}

	/**
	 * <pre>
	 * comment       : 수납 취소
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 26.
	 *
	 * </pre>
	 * @param donationOverPaymentRequestDto
	 * @return
	 * DonationSunapProcessResponseDto
	 */
	public DonationSunapProcessResponseDto paymentCancellation(DonationOverPaymentRequestDto donationOverPaymentRequestDto) {
		UserDetail userDetail = UserUtils.getUserDetail();
		var gCntrInfo = donationRepository.findById(donationOverPaymentRequestDto.getCntrSn()).orElseThrow(() -> new DonationException(DonationError.DONATION_INFORMATION_DOES_NOT_EXIST));
		gCntrInfo.setSttemntPayDe(null);
		gCntrInfo.setPointEndDe(null);
		gCntrInfo.setCntrPoint(null);
		gCntrInfo.setCntrBlcePoint(null);
		gCntrInfo.setCntrSttusCode(CntrSttusCode.DECLARATION.getCode());
		gCntrInfo.setDeleteAt("Y");
		gCntrInfo.setLastUpdtPnttm(LocalDateTime.now());
		gCntrInfo.setLastUpdusrId(userDetail.getUserId());

		donationRepository.save(gCntrInfo);
		return DonationSunapProcessResponseDto.builder().gCntr(gCntrInfo).build();
	}

	/**
	 * <pre>
	 * comment       : 기부자 정보 조회. (기부할 수 있는 최대 금액 정보 포함)/ 온라인은 DonatorInfoRequestDto userId에 값이 필수 이며 오프라인은 가입자는 필수, 미가입자는 mberCi가 필수
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 26.
	 *
	 * </pre>
	 * @param donatorInfoRequestDto
	 * @return
	 * @throws Exception
	 * DonatorInfoResponseDto
	 */
	public DonatorInfoResponseDto donatorInfo(DonatorInfoRequestDto donatorInfoRequestDto) throws Exception{
		String today = DateUtils.getToday("yyyyMMdd");
		DonatorInfoResponseDto resultInfo = null ;
		Optional<Long> optionalUserId = Optional.ofNullable(donatorInfoRequestDto.getUserId());
		Long sumSecsnAmt = 0L;
		Long sumcntrAmt = 0L;
		if(optionalUserId.isPresent()) {
			Optional<DonatorInfoDto> optional = Optional.ofNullable(donatorRepository.donatorInfo(optionalUserId.get()));
			DonatorInfoDto donatorInfoDto;
			if(optional.isPresent()) {
				donatorInfoDto = optional.get();
				sumSecsnAmt = donatorRepository.sumCntrAmtBySecsnYearAndMberCi(today.substring(0, 4), donatorInfoDto.getMberCi());
				sumcntrAmt = donationRepository.sumCntrAmtByUserIdAndCntrDe(today.substring(0, 4), donatorInfoDto.getUserId());

				try {
					resultInfo = DonatorInfoResponseDto.builder()
					.userId(donatorInfoDto.getUserId())
					.address(pCrypto.Decrypt("normal", donatorInfoDto.getAddress(), "", 0))
					.addressDetail(pCrypto.Decrypt("normal", donatorInfoDto.getAddressDetail(), "", 0))
					.birthday(pCrypto.Decrypt("normal", donatorInfoDto.getBirthday(), "", 0))
					.birthdayType(donatorInfoDto.getBirthdayType())
					.phoneNumber(pCrypto.Decrypt("normal", donatorInfoDto.getPhoneNumber(), "", 0))
					.loginPathCode(donatorInfoDto.getLoginPathCode())
					.email(pCrypto.Decrypt("normal", donatorInfoDto.getEmail(), "", 0))
					.userName(pCrypto.Decrypt("normal", donatorInfoDto.getUserName(), "", 0))
					.loginId(pCrypto.Decrypt("normal", donatorInfoDto.getLoginId(), "", 0))
					.userCntrLimitAmt(MAX_CNTR_AMT_LIMIT - (sumSecsnAmt != null ? sumSecsnAmt : 0) - (sumcntrAmt != null ? sumcntrAmt : 0))
					.isMberCi(donatorInfoDto.getIsMberCi())
					.build();
				} catch (UnsupportedEncodingException e) {
					log.error("■■■ERROR■■■ donatorInfo Exception {}",e.getStackTrace()[0]);
					throw new UnsupportedEncodingException("복호화 처리에 실패하였습니다.");
				}
			} else {
				sumSecsnAmt = donatorRepository.sumCntrAmtBySecsnYearAndMberCi(today.substring(0, 4), donatorInfoRequestDto.getMberCi());
				resultInfo = DonatorInfoResponseDto.builder()
						.userCntrLimitAmt(MAX_CNTR_AMT_LIMIT - sumSecsnAmt- sumcntrAmt)
						.build();

			}
		}

		return resultInfo;
	}

	/**
	 * <pre>
	 * comment       : 행공센 연계를 통한 정보 조회 (내국인)
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 17.
	 *
	 * </pre>
	 * @param rsgstadresRequestDto
	 * @return
	 * RsgstadresResponseDto
	 * @throws Exception
	 */
	public RsgstadresResponseDto rsgstadres(RsgstadresRequestDto rsgstadresRequestDto) throws Exception {

        String response = "";
        String mapLocgov = "";
        String bassAdres = "";
        String juso = "";
        String serviceResult = "";

		URL url = new URL(adminAddressinfoUrl);	//행공센 연계

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
			http.setConnectTimeout(CONNECTION_TIMEOUT);
	        http.setReadTimeout(READ_TIMEOUT);
	        http.setDefaultUseCaches(false);
	        http.setDoInput(true);
	        http.setDoOutput(true);
	        http.setRequestMethod("POST");

	        http.setRequestProperty("Content-Type", "application/json");

	        HashMap<String, String> hashMap = new HashMap<String, String>();
	        hashMap.put("id", rsgstadresRequestDto.getJuminNo());
	        hashMap.put("name", rsgstadresRequestDto.getUserName());
	        hashMap.put("comReqDt", DateUtils.getToday("yyyyMMdd"));
	        hashMap.put("comReqTm", DateUtils.getToday("HHmmss"));

	        JSONObject json = new JSONObject(hashMap);

	        try(final OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "UTF-8")){
	        	PrintWriter writer = new PrintWriter(osw);
	        	writer.write(String.valueOf(json));
	        	writer.flush();
	        }

	        try (final InputStreamReader isr = new InputStreamReader(http.getInputStream(), "UTF-8");){
	        	try (final BufferedReader br = new BufferedReader(isr)) {
					StringBuilder sb = new StringBuilder();
					String str;
					while ((str = br.readLine()) != null) {
						sb.append(str + "\n");
					}
					response = sb.toString();
				}
	        }

	        Object objList = JSONValue.parse(response);
	        JSONObject jsonObject = (JSONObject)objList;

	        juso = StringUtils.defaultIfEmpty(String.valueOf(jsonObject.get("juso")), "");

	        serviceResult =  StringUtils.defaultIfEmpty((String) jsonObject.get("serviceResult"), "");
	        if(serviceResult.equals("1") && !StringUtils.defaultIfEmpty((String) jsonObject.get("hangkikcd"), "").equals("")) {
	        	String locgovCode = String.valueOf(jsonObject.get("hangkikcd")).substring(0,5);
	        	GInsttCodeEntity insttCode = Optional.ofNullable(locgovRepository.findInsttByLocgovCode(locgovCode)).orElseThrow(() -> new DonationException(DonationError.INSTT_INFORMATION_NOT_EXIST));
	        	mapLocgov = insttCode.getLocgovMapngCode();
	        	var locgov = locgovRepository.findById(mapLocgov).orElseThrow(() -> new DonationException(DonationError.LOCGOV_INFORMATION_NOT_EXIST));
	        	bassAdres = locgov.getBassAdres();

	        	UserDetail userDetail = UserUtils.getUserDetail();
	        	OpUserDetailEntity opUserDetailEntity = new OpUserDetailEntity();
	        	var donator = Optional.ofNullable(donatorRepository.donatorInfo(userDetail.getUserId())).orElseThrow(() -> new DonationException(DonationError.USER_INFORMATION_NOT_EXIST));

	        	//카카오 가입자 양력생일로 세팅하여 차후 생년월일 변경 못하게 수정
	        	if(donator.getLoginPathCode().equals(LoginPathCode.KAKAO.getCode()) && !donator.getBirthdayType().equals(BirthdayType.SOLAR_CALENDAR.getCode())) {
	        		String genderCode = StringUtils.defaultIfEmpty(rsgstadresRequestDto.getJuminNo().substring(6, 7), "");
	        		opUserDetailEntity.setUserId(userDetail.getUserId());
	        		if (genderCode.equals("1") || genderCode.equals("2")) {
	        			opUserDetailEntity.setBirthday("19" + StringUtils.defaultIfEmpty(rsgstadresRequestDto.getJuminNo().substring(0, 6), ""));
	        			donatorRepository.updateKakaoBirthday(opUserDetailEntity);
	            	} else if (genderCode.equals("3") || genderCode.equals("4")) {
	            		userDetail.setBirthday("20" + StringUtils.defaultIfEmpty(rsgstadresRequestDto.getJuminNo().substring(0, 6), ""));
	            		donatorRepository.updateKakaoBirthday(opUserDetailEntity);
	            	} else {
	            		throw new DonationException(DonationError.PROBLEMS_JUMIN_NUMBER_KAKAO_BIRTHDAY);
	            	}
	        	}
	        }
		}

		return RsgstadresResponseDto.builder().mapLocgov(mapLocgov).bassAdres(bassAdres).juso(juso).serviceResult(serviceResult).build();

	}

	/**
	 * <pre>
	 * comment       : 외국인 미래행공 연계
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 4. 19.
	 *
	 * </pre>
	 * @param rsgstadresRequestDto
	 * @return
	 * @throws Exception
	 */
	public RsgstadresForeignResponseDto rsgstadresForeigner(RsgstadresForeignRequestDto rsgstadresForeignRequestDto) throws Exception {

        String mapLocgov = "";
        String bassAdres = "";
        String juso = "";
        String foreignStatusCode = "";
		URL url = new URL(inLinkUrl + "/api/mopas/rsgstadresinfoForeigner");	//미래행공

		//_initHttps();
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

	        HashMap<String, String> hashMap = new HashMap<String, String>();
	        hashMap.put("foreignNo", rsgstadresForeignRequestDto.getJuminNo());
	        hashMap.put("foreignName", rsgstadresForeignRequestDto.getUserName());

	        JSONObject json = new JSONObject(hashMap);

	        try(final OutputStreamWriter osw = new OutputStreamWriter(https.getOutputStream(), "UTF-8");){
	        	PrintWriter writer = new PrintWriter(osw);
	        	writer.write(String.valueOf(json));
	        	writer.flush();
	        }

	        String response = "";
	        try (final InputStreamReader isr = new InputStreamReader(https.getInputStream(), "UTF-8");){
            	try (final BufferedReader br = new BufferedReader(isr)) {

    				StringBuilder sb = new StringBuilder();
    				String str;
    				while ((str = br.readLine()) != null) {
    					sb.append(str + "\n");
    				}
    				response = sb.toString();
    			}
            }

			String SUC_RST_CD = "SUC.WS.000";

	    	if (com.onlinepowers.framework.util.StringUtils.hasLength(response)) throw new DonationException(DonationError.RESPONSE_NOT_EXIST);
			Gson gson = new Gson();
			JsonObject responseData = gson.fromJson(response, JsonObject.class);

			int responseCode = responseData.get("responseCode").getAsInt();

			if (HttpURLConnection.HTTP_OK == responseCode) throw new DonationException(DonationError.RESPONSE_CODE_NOT_EXIST);

			var data = Optional.ofNullable(getDataJsonObject(gson, responseData.get("body").getAsString())).orElseThrow(() -> new DonationException(DonationError.MOPAS_BODY_NOT_EXIST));

			String rstCd = data.get("mojResultCode").getAsString();
			if (SUC_RST_CD.equals(rstCd)) throw new DonationException(DonationError.FOREIGN_RESULT_CODE_INFORMATION_NOT_EXIST);

			String frgnrSttusSe = responseData.get("frgnrSttusSe").getAsString();

			JsonObject addressInfo = null;
			String locgovCheckCode = "";
			String upperLocgovCode = "";
			String address = "";

			LocalDate now = LocalDate.now();

			GAdmLocgovEntity admLocgov = null;
			GLocgovEntity locgov;

			// 등록외국인
			if(frgnrSttusSe.equals("1")) {
				String stayQualfEndDate = data.get("stayQualfEndDate").getAsString();			// 만료일?
				if (com.onlinepowers.framework.util.StringUtils.isEmpty(stayQualfEndDate)) {
					stayQualfEndDate = "99991231";
				}
				LocalDate checkDate = LocalDate.parse(stayQualfEndDate, DateTimeFormatter.ofPattern("yyyyMMdd"));

				if (now.isAfter(checkDate)) throw new DonationException(DonationError.FOREIGNER_EXPIRED);

				addressInfo = data.get("stayAreaInfoResponse").getAsJsonObject().get("stayAreaInfoList").getAsJsonArray().get(0).getAsJsonObject();
				locgovCheckCode = addressInfo.get("admnstmachCddr").getAsString();
				admLocgov = Optional.ofNullable(locgovRepository.findAdmLocgovByAdmCd(locgovCheckCode)).orElseThrow(() -> new DonationException(DonationError.ADM_CODE_NOT_EXIST));
				if(admLocgov.getLocgovCode() != null) {
					upperLocgovCode = StringUtils.defaultIfBlank(admLocgov.getLocgovCode(), "00").substring(0, 2) + "000";
				}
				address = addressInfo.get("adres").getAsString();

				if(admLocgov.getLocgovCode() != null && rsgstadresForeignRequestDto.getLocgovCode() != null) {
					if (StringUtils.defaultString(admLocgov.getLocgovCode()).equals(rsgstadresForeignRequestDto.getLocgovCode()) || upperLocgovCode.equals(rsgstadresForeignRequestDto.getLocgovCode()))
						throw new DonationException(DonationError.CANNOT_DONATE_TO_YOUR_LOCAL_GOVERNMENT);
				}
			}

			//재외국민
			if(frgnrSttusSe.equals("2")) {
				addressInfo = data.get("ovrsekrnOndssResponse").getAsJsonObject().get("ovrsekrnOndssInfoList").getAsJsonArray().get(0).getAsJsonObject();
				locgovCheckCode = addressInfo.get("admnstmachCddr").getAsString();

				admLocgov = Optional.ofNullable(locgovRepository.findAdmLocgovByAdmCd(locgovCheckCode)).orElseThrow(() -> new DonationException(DonationError.ADM_CODE_NOT_EXIST));
				if(admLocgov.getLocgovCode() != null) {
					upperLocgovCode = StringUtils.defaultIfBlank(admLocgov.getLocgovCode(), "00").substring(0, 2) + "000";
				}
				address = addressInfo.get("adres").getAsString();

				if(admLocgov.getLocgovCode() != null && rsgstadresForeignRequestDto.getLocgovCode() != null) {
					if (StringUtils.defaultString(admLocgov.getLocgovCode()).equals(rsgstadresForeignRequestDto.getLocgovCode()) || upperLocgovCode.equals(rsgstadresForeignRequestDto.getLocgovCode()))
						throw new DonationException(DonationError.CANNOT_DONATE_TO_YOUR_LOCAL_GOVERNMENT);
				}
			}

			//외국국적동포
			if(frgnrSttusSe.equals("3")) {
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

				if (now.isAfter(stayEndDate)) throw new DonationException(DonationError.FOREIGNER_EXPIRED);

				locgovCheckCode = addressInfo.get("admnstmachCddr").getAsString();

				admLocgov = Optional.ofNullable(locgovRepository.findAdmLocgovByAdmCd(locgovCheckCode)).orElseThrow(() -> new DonationException(DonationError.ADM_CODE_NOT_EXIST));
				if(admLocgov.getLocgovCode() != null) {
					upperLocgovCode = StringUtils.defaultIfBlank(admLocgov.getLocgovCode(), "00").substring(0, 2) + "000";
				}
				address = addressInfo.get("adres").getAsString();

				if(admLocgov.getLocgovCode() != null && rsgstadresForeignRequestDto.getLocgovCode() != null) {
					if (StringUtils.defaultString(admLocgov.getLocgovCode()).equals(rsgstadresForeignRequestDto.getLocgovCode()) || upperLocgovCode.equals(rsgstadresForeignRequestDto.getLocgovCode()))
						throw new DonationException(DonationError.CANNOT_DONATE_TO_YOUR_LOCAL_GOVERNMENT);
				}

			}

			foreignStatusCode = frgnrSttusSe;

			if(!foreignStatusCode.equals("1") && !foreignStatusCode.equals("2") && !foreignStatusCode.equals("3")) throw new DonationException(DonationError.FOREIGN_STATUS_CODE_NOT_EXIST);

			locgov = locgovRepository.findById(admLocgov.getLocgovCode()).orElseThrow(() -> new DonationException(DonationError.BASS_ADDRESS_NOT_EXIST));

			if(admLocgov.getLocgovCode() != null) {
				mapLocgov = StringUtils.defaultString(admLocgov.getLocgovCode());
			}

			bassAdres = locgov.getBassAdres();
			juso = StringUtils.defaultIfEmpty(address, "");
		}


		return RsgstadresForeignResponseDto.builder()
				.mapLocgov(mapLocgov)
				.bassAdres(bassAdres)
				.juso(juso)
				.foreignStatusCode(foreignStatusCode)
				.build();

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

		String today = DateUtils.getToday("yyyyMMdd");

		var cntrLmtt = Optional.ofNullable(locgovRepository.findCntrLmttByLmttBgnDeAndLmttEndDe(cntrLmttRequestDto.getLocgovCode(), today, today)).orElseThrow(() -> new DonationException(DonationError.CNTR_LMTT_INFORMATION_NOT_EXIST));

		return CntrLmttResponseDto.builder()
				.lmttBgnDe(cntrLmtt.getLmttBgnDe())
				.lmttEndDe(cntrLmtt.getLmttEndDe())
				.violtResnCn(cntrLmtt.getVioltResnCn())
				.build();
	}

	public RsgstadresResponseDto mockRsgstadres(RsgstadresRequestDto rsgstadresRequestDto) throws IOException {

        String mapLocgov = "";
        String bassAdres = "";

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", rsgstadresRequestDto.getJuminNo());
        hashMap.put("name", rsgstadresRequestDto.getUserName());
        hashMap.put("comReqDt", DateUtils.getToday("yyyyMMdd"));
        hashMap.put("comReqTm", DateUtils.getToday("HHmmss"));

        hashMap.put("juso", "경기도 부천시 원미구 계남로 196");
        hashMap.put("residentsta", "거주자");
        hashMap.put("hangkikcd", "4119265000");
        hashMap.put("serviceResult", "1");
        JSONObject json = new JSONObject(hashMap);

        String locgovCode = String.valueOf(json.get("hangkikcd")).substring(0,5);
    	GInsttCodeEntity insttCode = Optional.ofNullable(locgovRepository.findInsttByLocgovCode(locgovCode)).orElseThrow(() -> new DonationException(DonationError.INSTT_INFORMATION_NOT_EXIST));
    	mapLocgov = insttCode.getLocgovMapngCode();
    	var locgov = locgovRepository.findById(mapLocgov).orElseThrow(() -> new DonationException(DonationError.LOCGOV_INFORMATION_NOT_EXIST));
    	bassAdres = locgov.getBassAdres();

    	UserDetail userDetail = UserUtils.getUserDetail();
    	OpUserDetailEntity opUserDetailEntity = new OpUserDetailEntity();
    	var donator = Optional.ofNullable(donatorRepository.donatorInfo(userDetail.getUserId())).orElseThrow(() -> new DonationException(DonationError.USER_INFORMATION_NOT_EXIST));

    	//카카오 가입자 양력생일로 세팅하여 차후 생년월일 변경 못하게 수정
    	if(donator.getLoginPathCode().equals(LoginPathCode.KAKAO.getCode()) && !donator.getBirthdayType().equals(BirthdayType.SOLAR_CALENDAR.getCode())) {
    		String genderCode = StringUtils.defaultIfEmpty(rsgstadresRequestDto.getJuminNo().substring(6, 7), "");
    		opUserDetailEntity.setUserId(userDetail.getUserId());
    		if (genderCode.equals("1") || genderCode.equals("2")) {
    			opUserDetailEntity.setBirthday("19" + StringUtils.defaultIfEmpty(rsgstadresRequestDto.getJuminNo().substring(0, 6), ""));
    			donatorRepository.updateKakaoBirthday(opUserDetailEntity);
        	} else if (genderCode.equals("3") || genderCode.equals("4")) {
        		userDetail.setBirthday("20" + StringUtils.defaultIfEmpty(rsgstadresRequestDto.getJuminNo().substring(0, 6), ""));
        		donatorRepository.updateKakaoBirthday(opUserDetailEntity);
        	} else {
        		throw new DonationException(DonationError.PROBLEMS_JUMIN_NUMBER_KAKAO_BIRTHDAY);
        	}
    	}

		return RsgstadresResponseDto.builder().mapLocgov(mapLocgov).bassAdres(bassAdres).juso(String.valueOf(json.get("juso"))).serviceResult("1").build();

	}

//	private void _initHttps() throws KeyManagementException, NoSuchAlgorithmException {
//		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//			}
//
//			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
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
//			throw new KeyManagementException(e);
//		} catch (NoSuchAlgorithmException  e) {
//			throw new NoSuchAlgorithmException(e);
//		}
//	}

	private JsonObject getDataJsonObject(Gson gson, String jsonString) {
		JsonObject jsonObj = null;
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

	public DonationBugaResponseDto mockRegionBugaRequest(RegionBugaParamDto regionBugaParamDto) {
		return DonationBugaResponseDto.builder()
				.sgbCd("3550000")
				.linkTrgtCd("XXXXXXXXXXXXXXX")
				.linkMngKey("20221000000001319668")
				.linkRstCd("000")
				.linkRstMsg("정상 납부번호 : 123456789")
				.elctrnPayNo(StringUtils.defaultIfEmpty("정상 납부번호 : 123456789".split(":")[1], "").trim())
				.build();
	}
}
