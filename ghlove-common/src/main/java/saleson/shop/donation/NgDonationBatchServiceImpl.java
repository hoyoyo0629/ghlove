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
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang.StringUtils;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;

import lombok.RequiredArgsConstructor;
import saleson.common.enumeration.SmsType;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.seller.user.SellerUserService;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.email.EmailService;
import saleson.shop.email.domain.Email;
import saleson.shop.email.domain.EmailSend;
import saleson.shop.email.support.SendParam;
import saleson.shop.give.givestate.GiveStateService;
import saleson.shop.give.givestate.domain.GiveStateTest;
import saleson.shop.item.domain.ItemBase;
import saleson.shop.offgive.support.OffStockInfo;
import saleson.shop.order.domain.BuyItem;
import saleson.shop.order.givepoint.OrderGivePointMapper;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.SellerUser;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserDetailEncryptor;
import saleson.shop.user.support.SSLSocketFactoryMaker;

/**
 * @author hybrid
 *
 */
@Service("ngDonationBatchService")
@RequiredArgsConstructor
public class NgDonationBatchServiceImpl extends EgovAbstractServiceImpl implements NgDonationBatchService {

	private static final Logger logger = LoggerFactory.getLogger(NgDonationBatchServiceImpl.class);

	private final NgDonationMapper ngDonationMapper;

	private final OrderGivePointMapper orderGivePointMapper;

	@Autowired
	private NgDonationRelayService ngDonationRelayService;

	@Autowired
	SequenceService sequenceService;

	@Autowired
	private GiveStateService giveStateService;

	@Autowired
 	private EmailService emailService;

	@Autowired
	private SmsIpsService smsIpsService;

	@Autowired
	private SellerService sellerService;

	private final UserService userService;

	@Autowired
	private SellerUserService sellerUserService;

	@Autowired
	private UserDetailEncryptor userDetailEncryptor;

	@Value("${outconn.contry-now-sunap-url}")
    private String contryNowSunapUrl;

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

    private int TIMEOUT_VALUE = 10000;   // 10초

    private boolean CERTIFICATE = true;

    private int ENO_CNT = 1000;
	/**
	 *  기부 데이터 미결재분 삭제(익월 자정까지 미결재시 삭제 처리)
	 *  @param
	 */
	@Override
	public void deleteCntrData() {
		logger.info("========= deleteCntrData {}", " 서울 미결제분 삭제");
		ngDonationMapper.deleteCntrData();
	}

	/**
	 *  국세청 전자기부영수증 데이터(online) 처리(batch)
	 *  @param
	 */
	@Override
	public void sendNtsEreceiptOnBatch() {
		try {
			List<SeoulParam>  list = ngDonationMapper.getNtsEreceiptNotList("100");
			if (list.size() > 0) {
				String accessToken = getAccessToken();
				HashMap<String, Object> API_REQUEST = new HashMap<String, Object>();
				for(SeoulParam seoulParam : list) {
					HashMap<String, Object> dataMap = new HashMap<>();
					HashMap<String, Object> encDataMap = new HashMap<>();
					String encryptedCI = StringUtils.defaultIfEmpty(getRSA(seoulParam.getUserCi(), koreaApiKey), "");
					String encryptedBizNo = StringUtils.defaultIfEmpty(getRSA(seoulParam.getBizNo(), koreaApiKey), "");
					dataMap.put("dntDt", seoulParam.getSttemntPayDe());
					dataMap.put("conbCd", "43");																				// 기부금코드
					dataMap.put("dntAmt", Integer.parseInt(String.valueOf(seoulParam.getTaxAmt())));	// 기부금액
					dataMap.put("cnbtSpstCnfrClCd", "01");																	// 기부자신분확인구분코드

					encDataMap.put("extrOrgnNtplDscmEncCntn", encryptedCI);												// 실명인증값
					encDataMap.put("conbOrgTxprDscmNoEncCntn", encryptedBizNo);										// 기부단체사업자등록번호

			        API_REQUEST.put("data", dataMap);
			        API_REQUEST.put("enc_data", encDataMap);
			        String response = "";
			        try {
						response = restFulToRelayServer(koreaApiActionUrl, API_REQUEST, accessToken);
						Object objList = JSONValue.parse(response);
						JSONObject jsonObject = (JSONObject)objList;

						seoulParam.setUserCi(encryptedCI);
						seoulParam.setBizNo(encryptedBizNo);
						seoulParam.setConbCd("43");
						seoulParam.setCntrType("01");

						seoulParam.setResCode(String.valueOf(jsonObject.get("res-code")));
						seoulParam.setResMsg(String.valueOf(jsonObject.get("res-msg")));

						ngDonationMapper.insertHometaxGif(seoulParam);
						ngDonationMapper.updateNtsStatus(seoulParam);
					} catch (IOException e) {
			        	logger.error("sendNtsEreceiptOnBatch API_RESONSE IOException  eno : {} {}", seoulParam.getEnapbuNo(), e);
			        } catch(Exception e) {
			        	logger.error("sendNtsEreceiptOnBatch API_RESONSE Exception  eno : {} {}", seoulParam.getEnapbuNo(), e);
			        }

					TimeUnit.SECONDS.sleep(1);
				}
			}
		} catch(InterruptedException e) {
			logger.error("sendNtsEreceiptOnBatch InterruptedException {}", e);
		}
	}

	/**
	 *  국세청 전자기부영수증 데이터(online) 처리(batch)
	 *  @param
	 */
	@Override
	public void sendNtsEreceiptOffBatch() {
		try {
			List<SeoulParam>  list = ngDonationMapper.getNtsEreceiptNotList("200");
			if (list.size() > 0) {
				String accessToken = getAccessToken();

				for(SeoulParam seoulParam : list) {
					HashMap<String, Object> API_REQUEST = new HashMap<>();
					HashMap<String, Object> dataMap = new HashMap<>();
					HashMap<String, Object> encDataMap = new HashMap<>();
					String encryptedCI = StringUtils.defaultIfEmpty(getRSA(seoulParam.getUserCi(), koreaApiKey), "");
					String encryptedBizNo = StringUtils.defaultIfEmpty(getRSA(seoulParam.getBizNo(), koreaApiKey), "");

					dataMap.put("dntDt", seoulParam.getSttemntPayDe());
					dataMap.put("conbCd", "43");																				// 기부금코드
					dataMap.put("dntAmt", Integer.parseInt(String.valueOf(seoulParam.getTaxAmt())));	// 기부금액
					dataMap.put("cnbtSpstCnfrClCd", "01");																	// 기부자신분확인구분코드

					encDataMap.put("extrOrgnNtplDscmEncCntn", encryptedCI);												// 실명인증값
			        encDataMap.put("conbOrgTxprDscmNoEncCntn", encryptedBizNo);								// 기부단체사업자등록번호

			        API_REQUEST.put("data", dataMap);
			        API_REQUEST.put("enc_data", encDataMap);

			        String response = "";
			        try {
						response = restFulToRelayServer(koreaApiActionUrl, API_REQUEST, accessToken);
						Object objList = JSONValue.parse(response);
						JSONObject jsonObject = (JSONObject)objList;

						seoulParam.setUserCi(encryptedCI);
						seoulParam.setBizNo(encryptedBizNo);
						seoulParam.setConbCd("43");
						seoulParam.setCntrType("01");
						seoulParam.setResCode(String.valueOf(jsonObject.get("res-code")));
						seoulParam.setResMsg(String.valueOf(jsonObject.get("res-msg")));

						ngDonationMapper.insertHometaxGif(seoulParam);
						ngDonationMapper.updateNtsStatus(seoulParam);
			        } catch (IOException e) {
			        	logger.error("sendNtsEreceiptOffBatch API_RESONSE IOException {} {}",seoulParam.getEnapbuNo(), e);
			        } catch(Exception e) {
			        	logger.error("sendNtsEreceiptOffBatch API_RESONSE Exception  eno : {} {}", seoulParam.getEnapbuNo(), e);
			        }

					TimeUnit.SECONDS.sleep(1);
				}
			}
		} catch(InterruptedException e) {
			logger.error("sendNtsEreceiptOffBatch InterruptedException {}", e);
		}
	}

	public void sendNtsEreceiptBatchOld() {

		try {
			List<SeoulParam>  list = ngDonationMapper.getNtsEreceiptNotListOld();
			if (list.size() > 0) {
				String accessToken = getAccessToken();

				for(SeoulParam seoulParam : list) {
					HashMap<String, Object> API_REQUEST = new HashMap<>();
					HashMap<String, Object> dataMap = new HashMap<>();
					HashMap<String, Object> encDataMap = new HashMap<>();
					String encryptedCI = StringUtils.defaultIfEmpty(getRSA(seoulParam.getUserCi(), koreaApiKey), "");
					String encryptedBizNo = StringUtils.defaultIfEmpty(getRSA(seoulParam.getBizNo(), koreaApiKey), "");

					if(encryptedCI.equals("") || encryptedBizNo.equals("")) {
						continue;
					}

					dataMap.put("dntDt", seoulParam.getSttemntPayDe());
					dataMap.put("conbCd", "43");																				// 기부금코드
					dataMap.put("dntAmt", Integer.parseInt(String.valueOf(seoulParam.getTaxAmt())));	// 기부금액
					dataMap.put("cnbtSpstCnfrClCd", "01");																	// 기부자신분확인구분코드

					encDataMap.put("extrOrgnNtplDscmEncCntn", encryptedCI);												// 실명인증값
			        encDataMap.put("conbOrgTxprDscmNoEncCntn", encryptedBizNo);										// 기부단체사업자등록번호

			        API_REQUEST.put("data", dataMap);
			        API_REQUEST.put("enc_data", encDataMap);

			        String response = "";
			        try {
						response = restFulToRelayServer(koreaApiActionUrl, API_REQUEST, accessToken);
						Object objList = JSONValue.parse(response);
						JSONObject jsonObject = (JSONObject)objList;

						seoulParam.setUserCi(encryptedCI);
						seoulParam.setBizNo(encryptedBizNo);
						seoulParam.setConbCd("43");
						seoulParam.setCntrType("01");
						seoulParam.setResCode(String.valueOf(jsonObject.get("res-code")));
						seoulParam.setResMsg(String.valueOf(jsonObject.get("res-msg")));

						ngDonationMapper.insertHometaxGif(seoulParam);
						ngDonationMapper.updateNtsStatus(seoulParam);
			        } catch (IOException e) {
			        	logger.error("sendNtsEreceiptBatchOld API_RESONSE IOException eno : {}, {}", seoulParam.getEnapbuNo(), e);
			        } catch(Exception e) {
			        	logger.error("sendNtsEreceiptBatchOld API_RESONSE Exception  eno : {} {}", seoulParam.getEnapbuNo(), e);
			        }

					TimeUnit.SECONDS.sleep(1);
				}
			}
		} catch(InterruptedException e) {
			logger.error("sendNtsEreceiptBatchOld InterruptedException {}", e);
		}
	}

	@Override
	public void sendNtsEreceiptOnBatchTest() {
		try {
			int batchCnt = 10;

			String accessToken = getAccessToken();
			HashMap<String, Object> API_REQUEST = new HashMap<String, Object>();

			for(int i =0; i<=batchCnt; i++) {
				String eNo = "100000000000000" + String.valueOf(ENO_CNT);
				SeoulParam seoulParam = new SeoulParam();
				SecureRandom secureRandom = new SecureRandom();

				//int randomData = (int) Math.random()*(100000-10000+1)+10000;
				//int rData = secureRandom.nextInt();
				//int randomData = rData*(100000-10000+1)+10000;
				int randomData = (int) secureRandom.nextInt()*(100000-10000+1)+10000;
				String amt = "";
				if(randomData < Integer.MAX_VALUE) {
					amt = String.valueOf(randomData);
				}
				HashMap<String, Object> dataMap = new HashMap<>();
				HashMap<String, Object> encDataMap = new HashMap<>();
				String encryptedCI = getRSA("Gbml+vWYg/3J1yvpyHJd4pjF3JPojkGYHtkLAqb/tW5PPKC/gHcfIm+c7Ccr6H30+3rC4Nxc/EdhZBrdukYOKQ==", koreaApiKey);
				String encryptedBizNo = getRSA("1028301521", koreaApiKey);
				dataMap.put("dntDt", "20230307");
				dataMap.put("conbCd", "43");																				// 기부금코드
				dataMap.put("dntAmt", amt);	// 기부금액
				dataMap.put("cnbtSpstCnfrClCd", "01");																	// 기부자신분확인구분코드

				encDataMap.put("extrOrgnNtplDscmEncCntn", encryptedCI);												// 실명인증값
				encDataMap.put("conbOrgTxprDscmNoEncCntn", encryptedBizNo);										// 기부단체사업자등록번호

		        API_REQUEST.put("data", dataMap);
		        API_REQUEST.put("enc_data", encDataMap);

	        	String response = restFulToRelayServer(koreaApiActionUrl, API_REQUEST, accessToken);
	        	Object objList = JSONValue.parse(response);
	            JSONObject jsonObject = (JSONObject)objList;
	            seoulParam.setEnapbuNo(eNo);
	            seoulParam.setUserId("99999");
	            seoulParam.setSttemntPayDe("20230307");
	            seoulParam.setConbCd("43");
	            seoulParam.setTaxAmt(amt);
	            seoulParam.setCntrType("01");
	            seoulParam.setUserCi(encryptedCI);
	            seoulParam.setBizNo(encryptedBizNo);
	            seoulParam.setResCode(String.valueOf(jsonObject.get("res-code")));
	            seoulParam.setResMsg(String.valueOf(jsonObject.get("res-msg")));

	            ngDonationMapper.insertHometaxGif(seoulParam);
	            ENO_CNT ++;
				TimeUnit.SECONDS.sleep(1);
			}
		} catch(IOException | InterruptedException e) {
			logger.error("sendNtsEreceiptOnBatchTest error {}", e);
		} catch(Exception e) {
        	logger.error("sendNtsEreceiptOnBatchTest : {}",  e.getStackTrace()[0]);
        }

	}

	/**
	 *  지방세외(현세대) 미납건 데이터 삭제 처리(batch)
	 *  @param
	 */
	@Override
	public void deleteNotSunapStndBatch() {

		ngDonationMapper.deleteNotSunapStndBatch();
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
	public String getAccessToken() {
		String accessToken = null;
		try {
			HashMap<String, Object> API_REQUEST = new HashMap<>();
	        String encryptedPW = StringUtils.defaultIfEmpty(getRSA(koreaApiPassword, koreaApiKey), "");
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
        	logger.error("getAccessToken error {}", e);
        } catch(Exception e) {
        	logger.error("getAccessToken : {}",  e.getStackTrace()[0]);
        }


        return accessToken;
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
	 * @throws Exception
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
            http.setReadTimeout(TIMEOUT_VALUE);
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            http.setRequestProperty("Content-Type", "application/json");

            //국세청 TOKEN 가져올 시
            if(API_URL.equals(koreaApiLoginUrl) || API_URL.equals(koreaApiActionUrl)) {
            	logger.debug("restFulToRelayServer koreaApiLoginUrl ==> ", koreaApiLoginUrl);
            	http.setRequestProperty("X-Forwarded-For", koreaApiLoginIp);
            	if(API_URL.equals(koreaApiActionUrl)) {
            		logger.debug("restFulToRelayServer koreaApiActionUrl ==> ", koreaApiActionUrl);
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
        }

		return response;
	}

//	@SuppressWarnings("unused")
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
//			logger.error("ERROR-34: Key 관리 예외오류 {}", e);
//		} catch (NoSuchAlgorithmException  e) {
//			logger.error("ERROR-35: 암호 알고리즘 사용불가 오류 {}", e);
//		}
//	}

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

        } catch(NoSuchAlgorithmException | NoSuchPaddingException
        		| BadPaddingException | IllegalBlockSizeException
        		| InvalidKeyException | InvalidKeySpecException e) {
			logger.error("RSA 암호화 : Exception ", e);
			return encryptedData;
		}
        return encryptedData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void confirmSunapForDeletedList() {
		try {
			List<HashMap<String, Object>>  deleteList = ngDonationMapper.confirmSunapForDeletedList();
			for(HashMap<String, Object> data : deleteList) {
				if(data.get("seoul_trget_at").toString().equals("Y")) {
					SeoulParam seoulParam = new SeoulParam();
					seoulParam.setEnapbuNo(data.get("elctrn_pay_no").toString());
					seoulParam.setJijacheCd(data.get("jijache_cd").toString());
					seoulParam.setSystemCd("02");
					Map<String, Object> sunapResultMap = ngDonationRelayService.etaxSunapInfo(seoulParam);

					String api_response = StringUtils.defaultIfEmpty((String) sunapResultMap.get("api_response"), "");
					Object objList = JSONValue.parse(api_response);
			        JSONObject jsonObject = (JSONObject)objList;

					if(!api_response.equals("")) {
						List<Map<String, Object>> arrResult = (List<Map<String, Object>>) jsonObject.get("ARR_RESULT");
						String sunapYn = StringUtils.defaultIfEmpty((String) arrResult.get(0).get("SUNAP_YN"), "");
						if(StringUtils.defaultIfEmpty((String) jsonObject.get("RST_CD"), "").equals("100") && sunapYn.equals("Y")) {
				            seoulParam.setUserId(String.valueOf(data.get("user_id")));
				            seoulParam.setSttemntPayDe(data.get("cntr_de").toString());
				            ngDonationMapper.sunapSuccess(seoulParam);
			            }
						else
						{
							ngDonationMapper.updateSunapBatchCompleted(seoulParam);
						}
					}
				}
				TimeUnit.SECONDS.sleep(1);
			}
		} catch(IOException | InterruptedException e) {
			logger.error("confirmSunapForDeletedList ", e);
		} catch(Exception e) {
			logger.error("confirmSunapForDeletedList ", e.getStackTrace()[0]);
		}

	}

	@Override
	public void getNoBugaLocgovList() {
		try {
			int i = 1;
			LocalDate date = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

			List<HashMap<String, Object>> noBugaList = ngDonationMapper.getNoBugaLocgovList();
			Email email = new Email();

			email.setAuthTarget("E");
	    	email.setFrstRegisterId(Long.valueOf(0));
	    	email.setSubject(date.minusDays(3).format(formatter)+" ~ "+date.format(formatter)+""+" 기부내역이 없는 지자체 목록");
	    	if(noBugaList.isEmpty())
	    	{
	    		email.setContent("전 지역 기부 내역 존재");
	    	}
	    	else
	    	{
	    		StringBuilder stringBuilder = new StringBuilder();
	    		StringJoiner stringLJoiner = new StringJoiner(",");
	    		for(HashMap<String, Object> bugaMap : noBugaList) {
	    			stringBuilder.append("<p>"+i+". "+bugaMap.get("upper_locgov_nm")+" "+bugaMap.get("locgov_nm")+"</p>");
	    			i++;

	    			stringLJoiner.add(bugaMap.get("locgov_code").toString());
	    		}
	    		email.setContent(stringBuilder.toString()+"<br>"+stringLJoiner.toString());
	    	}

	    	email.setSendType("D");
			Email result = emailService.insertEmail(email);
			SendParam param = new SendParam();
			param.setEmailId(result.getEmailId());

			List<EmailSend> list = new ArrayList<>();
			EmailSend emailSend = new EmailSend();
//			emailSend.setEmail("hybrid1007@gmail.com");
//			emailSend.setUserName("조한영");
//			list.add(emailSend);
//
//			emailSend = new EmailSend();
//			emailSend.setEmail("hwangwonjong@iteyes.co.kr");
//			emailSend.setUserName("황원종");
//			list.add(emailSend);

//			emailSend = new EmailSend();
//			emailSend.setEmail("sklee@u-cube.kr");
//			emailSend.setUserName("이상기");
//			list.add(emailSend);

			emailSend = new EmailSend();
			emailSend.setEmail("rain2199@archivsoft.com");
			emailSend.setUserName("이현민");
			list.add(emailSend);

			emailSend = new EmailSend();
			emailSend.setEmail("scorplim@naver.com");
			emailSend.setUserName("임승빈");
			list.add(emailSend);

			emailSend = new EmailSend();
			emailSend.setEmail("leeshinwoo93@archivsoft.com");
			emailSend.setUserName("이신우");
			list.add(emailSend);

			emailSend = new EmailSend();
			emailSend.setEmail("hyeong3381@archivsoft.com");
			emailSend.setUserName("조형원");
			list.add(emailSend);

			emailSend = new EmailSend();
			emailSend.setEmail("lpureall@naver.com");
			emailSend.setUserName("신승엽");
			list.add(emailSend);

			emailSend = new EmailSend();
			emailSend.setEmail("aayu0123@naver.com ");
			emailSend.setUserName("유하영");
			list.add(emailSend);

			emailSend = new EmailSend();
			emailSend.setEmail("akim2810@naver.com");
			emailSend.setUserName("김애경");
			list.add(emailSend);

			emailSend = new EmailSend();
			emailSend.setEmail("sapiensjiny@naver.com");
			emailSend.setUserName("전표진");
			list.add(emailSend);

//			emailSend = new EmailSend();
//			emailSend.setEmail("ansimi2023@gmail.com");
//			emailSend.setUserName("박철용");
//			list.add(emailSend);

			param.setSendUserList(list);
			emailService.sendEmail(param);
		} catch (OpRuntimeException e) {
			logger.error("■■■BATCH■■■ getNoBugaLocgovList OpRuntimeException {}", e);
		}


	}

	@Override
	public void cntrTaxTempBatch() {
		try {
			Optional<String> optionalToken = Optional.ofNullable(getAccessToken());
			String accessToken = optionalToken.orElse("");

			if(accessToken.equals("")) {
				return;
			} else {
				// is_send 0 : 미처리 | 1 : 처리
				List<SeoulParam>  list = ngDonationMapper.getCntrTaxTempList();

				if (list.size() > 0) {

					for(SeoulParam seoulParam : list) {
						String encryptedCI = StringUtils.defaultIfEmpty(getRSA(seoulParam.getUserCi(), koreaApiKey), "");//유저ci
						String encryptedBizNo = StringUtils.defaultIfEmpty(getRSA(seoulParam.getBizNo(), koreaApiKey), "");//지자체의 사업자번호

				        /**
				         * taxStatusCode : {100 : 신고, 200 : 정상 처리, 300 : 비정상 처리, 301 : CI NULL, 302 : BIZNO NULL}
				         */
				        CntrTaxLogDto taxLogDto = new CntrTaxLogDto();
				        taxLogDto.setElctrnPayNo(seoulParam.getEnapbuNo());
				        taxLogDto.setSttemntPayDe(seoulParam.getSttemntPayDe());
				        taxLogDto.setCntrAmt(seoulParam.getTaxAmt());
				        taxLogDto.setTaxStatusCode("100");
//				        taxLogDto.setConbCd("43"); 					//기부금코드 : 특별재난지역 적용에 따라서 *** 일반지역기부:43, 특별재난지역기부:44 *** 로 표기함
				        taxLogDto.setConbCd(seoulParam.getConbCd());//기부금코드 : 특별재난지역 적용에 따라서 *** 일반지역기부:43, 특별재난지역기부:44 *** 로 표기함
						taxLogDto.setCntrType("01");
						taxLogDto.setElcrAplCd(seoulParam.getElcrAplCd()); 	// /2024.02 '전자기부금영수증신청구분코드' 항목 추가 {01 : 영수증 발급 / 02 : 영수증 수정 / 03 : 영수증 삭제}

			        	if(encryptedCI.equals("") || encryptedBizNo.equals("")) {
				        	if(encryptedCI.equals("")) taxLogDto.setTaxStatusCode("301");
				        	if(encryptedBizNo.equals("")) taxLogDto.setTaxStatusCode("302");
				        	ngDonationMapper.insertCntrTaxLog(taxLogDto);
				        	ngDonationMapper.updateCntrTaxTemp(taxLogDto);
							continue;
						}

			        	ngDonationMapper.insertCntrTaxLog(taxLogDto);
				        try {
				        	HashMap<String, Object> API_REQUEST = new HashMap<>();
							HashMap<String, Object> dataMap = new HashMap<>();
							HashMap<String, Object> encDataMap = new HashMap<>();

				        	//API 요청 정보
							dataMap.put("dntDt", seoulParam.getSttemntPayDe());
//							dataMap.put("conbCd", "43"); 					//기부금코드 : 특별재난지역 적용에 따라서 *** 일반지역기부:43, 특별재난지역:44 *** 로 표기함
							dataMap.put("conbCd", seoulParam.getConbCd());	//기부금코드 : 특별재난지역 적용에 따라서 *** 일반지역기부:43, 특별재난지역:44 *** 로 표기함
							dataMap.put("dntAmt", Integer.parseInt(String.valueOf(seoulParam.getTaxAmt())));	// 기부금액
							dataMap.put("cnbtSpstCnfrClCd", "01");												// 기부자신분확인구분코드
							dataMap.put("elcrAplcd", seoulParam.getElcrAplCd());								// (추가)전자기부금영수증신청구분코드
							dataMap.put("elcrAdmNo", seoulParam.getEnapbuNo());									// (추가)전자기부금영수증관리번호

							encDataMap.put("extrOrgnNtplDscmEncCntn", encryptedCI);								// 실명인증값
					        encDataMap.put("conbOrgTxprDscmNoEncCntn", encryptedBizNo);							// 기부단체사업자등록번호
					        API_REQUEST.put("data", dataMap);
					        API_REQUEST.put("enc_data", encDataMap);

							String response = restFulToRelayServer(koreaApiActionUrl, API_REQUEST, accessToken);

					        Object objList = JSONValue.parse(response);
							JSONObject jsonObject = (JSONObject)objList;
							String resCode = String.valueOf(jsonObject.get("res-code"));
							String resMsg = String.valueOf(jsonObject.get("res-msg"));

							/* 안쓰는거 같음 추후 확인후 삭제요망 */
							seoulParam.setUserCi(encryptedCI);
							seoulParam.setBizNo(encryptedBizNo);
							seoulParam.setConbCd(seoulParam.getConbCd());//기부금코드 : 특별재난지역 적용에 따라서 *** 일반:43, 특별재난지역:44 *** 로 표기함
//							seoulParam.setCtbnyCode(seoulParam.getCtbnyCode());
							seoulParam.setCntrType("01");
							seoulParam.setResCode(resCode);
							seoulParam.setResMsg(resMsg);

							taxLogDto.setNtsResCode(resCode);
							taxLogDto.setNtsResMssage(resMsg);
							if(resCode.equals("200")) {
								taxLogDto.setTaxStatusCode("200");
							} else {
								taxLogDto.setTaxStatusCode("300");
							}
							ngDonationMapper.updateCntrTaxLog(taxLogDto);
							ngDonationMapper.updateCntrTaxTemp(taxLogDto);
				        } catch (IOException e) {
				        	taxLogDto.setTaxStatusCode("500");
				        	ngDonationMapper.updateCntrTaxLog(taxLogDto);
				        	logger.error("■■■BATCH ERROR■■■cntrTaxTempBatch API_RESONSE Exception : {}", e);
				        } catch(Exception e) {
				        	taxLogDto.setTaxStatusCode("500");
				        	ngDonationMapper.updateCntrTaxLog(taxLogDto);
				        	logger.error("■■■BATCH ERROR■■■cntrTaxTempBatch API_RESONSE Exception : {}", e.getStackTrace()[0]);
				        }
					}
				}
			}

		} catch(OpRuntimeException e) {
			logger.error("cntrTaxTempBatch error {}", e);
		}
	}

	@Override
	public void cntrSunapBatch() {
		try {

			/*
			 * X 1. (2023년 이후 기부 기준) 수납처리 되지 않은 기부건중 수납 확인완료된 건 조회 (기부일자 내림차순기준 200건씩)
			 * X 2. g_cntr 수납 UPDATE
			 * */

			/*
			 * 2025년도 인수인계 받아 확인해보니 userId를 0으로 하드코딩해놔서 제대로 처리되지 않는 로직임
			 * 해당로직 삭제하고 오프라인 기탁서 답례품 주문건 확인시 처리하는 로직으로 변경
			 */

			this.procOffOrder();

			/*
			LocalDate now = LocalDate.now();
			String cntrDt = String.valueOf(now.getYear())+"0101";

			List<SeoulParam> list = ngDonationMapper.getCntrNonSunapDataList(cntrDt);

			if (list.size() > 0) {
				for(SeoulParam param : list) {


					param.setUserId("0");
					ngDonationMapper.sunapResultSuccess(param);

					//오프라인 답례품 주문건의 경우 주문번호가 있음
					if (param.getOrderCode() != null && param.getOrderCode().trim().length() > 0) {
						logger.error("offline cntr logging start :" + param.getOrderCode() );
						this.procOffOrder();
						logger.error("offline cntr logging end :" + param.getOrderCode() );

					}
				}
			}
			*/
		}catch (OpRuntimeException e) {
			logger.error("cntrSunapBatch error {}", e);
		}
	}


	/**
	 * 오프라인 주문 처리
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor={Exception.class})
	public void procOffOrder() {
		try {
			long buyUserId;
			int resultCnt = 0;

			logger.debug("오프라인 주문처리 시작");
			//0. 해당 배치에서 기부 수납완료를 하고 있지 않아서 주문테이블에 오프라인 답례품 처리 여부 컬럼을 추가하여
			// 수납완료건중에 처리여부가 N이거나 NULL 인 데이터를 조회하여 처라한다
			List<OffStockInfo> offOrderList = orderGivePointMapper.selectOffOrderList();

			for(OffStockInfo param : offOrderList) {
				//1. 주문테이블 상태 변경 0:입금대기 -> 10:결제완료
				logger.error("offline cntr logging updateOrderStatus :" + param.getOrderCode() );

				resultCnt = ngDonationMapper.updateOrderStatus(param.getOrderCode());

				logger.error("offline cntr logging updateOrderStatus resultCnt:" + resultCnt );

				if (resultCnt <= 0) {
					logger.error("주문테이블 상태 변경중 오류 발생");
					logger.error("CntrSn : " , param.getCntrSn());
					logger.error("OrderCode : " , param.getOrderCode());
					throw new Exception();
				}

				logger.error("offline cntr logging getGivePayOrder");
				//2. 포인트 사용내역 등록
				OrderGivePoint orderResult = orderGivePointMapper.getGivePayOrder(param.getOrderCode());

				//구매자 국민비서 발송을 위해 세팅
				buyUserId = orderResult.getUserId();

				long locgovAmount = orderResult.getSumPrice();

				List<OrderGivePoint> locgovPointList = orderGivePointMapper.getGiveBlcePointListByUserIdAndLocgov(orderResult);

				locgovPay: for (OrderGivePoint orderGivePoint : locgovPointList) {
					long leftPoint = orderGivePoint.getCntrBlcePoint();
					orderGivePoint.setUserId(orderResult.getUserId());
					orderGivePoint.setUseCn("답례품 구매");
					orderGivePoint.setUseSeCode("1"); // 1 : 사용, 2 : 소멸, 3 : 탈퇴
					orderGivePoint.setOrderCode(param.getOrderCode());

					if (locgovAmount > leftPoint) {	// 결제할 금액이 잔여 포인트보다 클 경우
						orderGivePoint.setCntrUsePoint(leftPoint); // 사용금액은 잔여포인트 전체
						orderGivePoint.setCntrBlcePoint(0);
						locgovAmount -= leftPoint;
						if (locgovAmount < 0) {
							//throw new Exception("답례품 주문 처리중 문제가 발생 하였습니다.");
							logger.error("답례품 주문 처리중 문제가 발생 하였습니다.");
							logger.error("OrderCode : " , param.getOrderCode());
							throw new Exception("0310"); //주문에 실패 했습니다.
						}
					} else {
						orderGivePoint.setCntrUsePoint(locgovAmount); // 사용금액은 남은 결제 금액 전체
						leftPoint -= locgovAmount;
						locgovAmount = 0;
						if (leftPoint < 0) {
							//throw new Exception("기부포인트가 부족합니다.");
							logger.error("기부포인트가 부족합니다.");
							logger.error("OrderCode : " , param.getOrderCode());
							throw new Exception("0329");
						}
						orderGivePoint.setCntrBlcePoint(leftPoint);
					}

					orderGivePoint.setLastUpdtPnttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
					orderGivePoint.setLastUpdusrId(orderResult.getUserId());

					//orderGivePointService.updateGiveBlcePoint(orderGivePoint); // 잔여포인트 업데이트 로직 제외

					orderGivePoint.setFrstRegisterId(orderResult.getUserId());

					logger.error("offline cntr logging insertGiveUsePoint :" + param.getOrderCode() );

					resultCnt = orderGivePointMapper.insertGiveUsePoint(orderGivePoint);

					logger.error("offline cntr logging insertGiveUsePoint resultCnt :" + resultCnt);

					if (resultCnt <= 0) {
						logger.error("포인트 사용내역 등록중 오류 발생");
						logger.error("CntrSn : " , param.getCntrSn());
						logger.error("OrderCode : " , param.getOrderCode());
						throw new Exception();
					}

					if (locgovAmount == 0) {
						break locgovPay;
					}
				}

				//재고처리
				//주문내역조회
				List<OffStockInfo> offStockInfoList = orderGivePointMapper.selectItemStockInfo(param.getOrderCode());

				OffStockInfo offOptionStockInfo = null;

				for(OffStockInfo offStockInfo : offStockInfoList) {
					//재고연동을 안하면 차감안함. 대신 옵션 리스트 확인해야함
					if ("N".equals(offStockInfo.getStockFlag())) {

						//options 가 있으면 text split 해서 해당 옵션 정보 조회함
						String options = offStockInfo.getOptions();
						if (options != null && options.trim().length() > 0) {

							//상품옵션을 주문테이블의 options 컬럼에 텍스트로 넣어놓음. 근데 유형도 여러개임. 오프라인 주문건은 / 로 함
							String[] firstOptionSplit = options.split("/");
							// 구분자 "/"로 잘라서 1개이상나오면
							if (firstOptionSplit != null && firstOptionSplit.length > 1) {

								for (int i=0 ; i<firstOptionSplit.length ; i++) {
									String optionSplitStr = firstOptionSplit[i];

									//옵션 타이틀이 있는 경우가 있으니 :로 나눠봄
									String[] optionValueSplit = optionSplitStr.split(":");

									// 구분자 ":" 로 나눠지면 실제 옵션값을 뺌
									if (optionValueSplit != null && optionValueSplit.length > 1) {
										if (i==0) offStockInfo.setOptionName1(StringUtils.trim(optionValueSplit[1]));
										if (i==1) offStockInfo.setOptionName2(StringUtils.trim(optionValueSplit[1]));
										if (i==2) offStockInfo.setOptionName3(StringUtils.trim(optionValueSplit[1]));
									}else {
										if (i==0) offStockInfo.setOptionName1(StringUtils.trim(optionValueSplit[0]));
										if (i==1) offStockInfo.setOptionName2(StringUtils.trim(optionValueSplit[0]));
										if (i==2) offStockInfo.setOptionName3(StringUtils.trim(optionValueSplit[0]));
									}
								}

								logger.error("offline cntr logging updateItemOptionStock :" + param.getOrderCode() );
								//옵션수량차감
								resultCnt = orderGivePointMapper.updateItemOptionStock(offStockInfo);

								logger.error("offline cntr logging updateItemOptionStock resultCnt :" + resultCnt );

//								if (resultCnt <= 0) {
//									logger.error("아이템 옵션 재고 차감중 오류 발생");
//									logger.error("CntrSn : " , param.getCntrSn());
//									logger.error("OrderCode : " , param.getOrderCode());
//									throw new Exception();
//								}

							}
						}
					}
					//재고연동을 하면 차감수정함
					else {
						logger.error("offline cntr logging updateItemStock :" + param.getOrderCode() );

						orderGivePointMapper.updateItemStock(offStockInfo);

//						if (resultCnt <= 0) {
//							logger.error("아이템 재고 차감중 오류 발생");
//							logger.error("CntrSn : " , param.getCntrSn());
//							logger.error("OrderCode : " , param.getOrderCode());
//							throw new Exception();
//						}
					}
				}

				logger.error("offline cntr logging updateOrderOffProcYn :" + param.getOrderCode() );

				//오프라인 답례품 처리 여부 업데이트 : Y
				orderGivePointMapper.updateOrderOffProcYn(param.getOrderCode());


				// 국민비서 알림 전송 - 구매자

				List<BuyItem> buyItems = orderGivePointMapper.selectBuyItemList(param.getOrderCode());

				String userName = "";

				try {
					User user = userService.getUserByUserId(buyUserId);

					userName = user.getUsername();

					UserDetail userDetail = (UserDetail) user.getUserDetail();
					userDetail.decrypt(userDetailEncryptor, false);
					if ("0".equals(userDetail.getReceiveSms()) && StringUtils.isNotEmpty(userDetail.getPhoneNumber()) && StringUtils.isNotEmpty(userDetail.getMberCi())) {			// 문자 수신 동의했을 경우
						List<ReceiverInfo> receiverInfos = new ArrayList<>();
						ReceiverInfo receiverInfo = new ReceiverInfo();
						receiverInfo.setSmsType(SmsType.PRESENT_ORDER);
						receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
						StringBuilder sb = new StringBuilder();
						sb.append(user.getUserName());
						sb.append("|");
						sb.append(receiverInfo.getLocalDateTimeToStr());
						sb.append("|");
						sb.append(param.getOrderCode());
						sb.append("|");
						BuyItem buyItem = buyItems.get(0);
						sb.append(StringEscapeUtils.unescapeHtml(buyItem.getItemName()).replaceAll("\\|", "-"));
						if (StringUtils.isNotEmpty(buyItem.getOptionsDisplay()) && !buyItem.getOptionsDisplay().contains("|")) {
							sb.append("(");
							sb.append(StringEscapeUtils.unescapeHtml(buyItem.getOptionsDisplay()));
							sb.append(")");
						}
						if (buyItems.size() > 1) {
							sb.append(" 등");
						}
						sb.append("|");
						sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
						receiverInfo.setSndngCntnts(sb.toString());
						receiverInfos.add(receiverInfo);
						smsIpsService.insertTifIpsSndngM(receiverInfos);
					}
				} catch (NullPointerException | ClassCastException e) {
					logger.error("CntrSn : " , param.getCntrSn());
					logger.error("OrderCode : " , param.getOrderCode());
					logger.error(getClass().getName() +  " :: cntrSunapBatch send user sms error", e);
				}

				// 같은 상품 여러 건일 경우 한번만 발송하도록 변경(답례품 제공자)
				List<BuyItem> sendSmsItemList = new ArrayList<>();
				// 국민비서 알림 전송 - 답례품 제공자
				for (BuyItem buyItem : buyItems) {
					boolean exist = false;

					seller : for (BuyItem sendSmsItem : sendSmsItemList) {
						if (buyItem.getSellerId() == sendSmsItem.getSellerId() && buyItem.getItemId() == sendSmsItem.getItemId()) {
							exist = true;
							break seller;
						}
					}

					if (!exist) {
						sendSmsItemList.add(buyItem);
					}
				}

				for (BuyItem sendSmsItem : sendSmsItemList) {
					try {

						Seller seller = sellerService.getSellerById(sendSmsItem.getSellerId());
						SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdForSms(seller.getLoginId());
						if ("0".equals(sellerUser.getReceiveSms()) && StringUtils.isNotEmpty(sellerUser.getPhoneNumber()) && StringUtils.isNotEmpty(sellerUser.getMberCi())) {			// 문자 수신 동의했을 경우
							List<ReceiverInfo> receiverInfos = new ArrayList<>();
							ReceiverInfo receiverInfo = new ReceiverInfo();
							receiverInfo.setSmsType(SmsType.PRESENT_ORDER);
							receiverInfo.setPrvcIdntfcInfo(sellerUser.getMberCi());
							StringBuilder sb = new StringBuilder();
							sb.append(userName);
							sb.append("|");
							sb.append(receiverInfo.getLocalDateTimeToStr());
							sb.append("|");
							sb.append(param.getOrderCode());
							sb.append("|");
							sb.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getItemName()).replaceAll("\\|", "-"));
							if (StringUtils.isNotEmpty(sendSmsItem.getOptionsDisplay()) && !sendSmsItem.getOptionsDisplay().contains("|")) {
								sb.append("(");
								sb.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getOptionsDisplay()));
								sb.append(")");
							}
							sb.append("|");
							sb.append(sellerUser.getPhoneNumber().replaceAll("-", ""));
							receiverInfo.setSndngCntnts(sb.toString());
							receiverInfos.add(receiverInfo);
							smsIpsService.insertTifIpsSndngM(receiverInfos);
						}
					} catch (NullPointerException | ClassCastException e) {
						logger.error("CntrSn : " , param.getCntrSn());
						logger.error("OrderCode : " , param.getOrderCode());
						logger.error(getClass().getName() +  " :: cntrSunapBatch send seller sms error :: sellerId :: " + sendSmsItem.getSellerId(), e);
					}
				}
			}
		}catch(Exception e2) {
			logger.error("offline order error {} " , e2);
		}
	}

	@Override
	public void cntrNonSunapFiveMinSunapCheck() {

		// 5분에 한번씩 배치 돌면서 세외 실시간 수납확인 후 기부테이블에 수납정보 업데이트하기
		List<GiveStateTest> nonSunapList = ngDonationMapper.getCntrNonSunapFiveMinList();
		try {

			for(GiveStateTest sunapInfo : nonSunapList) {
				Map<String, Object> resultMap = new HashMap<>();
				sunapInfo.setElctrnPayNo(sunapInfo.getEnapbuNo());
				// 서울 수납 확인
				if(sunapInfo.getSeoulTrgetAt().equals("Y")){
					SeoulParam seoulParam = new SeoulParam();
					seoulParam.setEnapbuNo(sunapInfo.getEnapbuNo());
					seoulParam.setJijacheCd(sunapInfo.getCntrLocgovCode());
					seoulParam.setSystemCd("02");

					resultMap = ngDonationRelayService.etaxSunapInfo(seoulParam);

					String api_response = StringUtils.defaultIfEmpty((String) resultMap.get("api_response"), "");

					if (api_response.equals("")) {// 수납 확인 실패

						logger.error("■■■debug■■■ 수납확인에 실패하였습니다. ");

					} else {

						Object objList = JSONValue.parse(api_response);
				        JSONObject jsonObject = (JSONObject)objList;

				        List<Map<String, Object>> arrResult = (List<Map<String, Object>>) jsonObject.get("ARR_RESULT");
				        String sunapYn = StringUtils.defaultIfEmpty((String) arrResult.get(0).get("SUNAP_YN"), "");

				        if("100".equals(StringUtils.defaultIfEmpty((String) jsonObject.get("RST_CD"), "")) && sunapYn.equals("Y")) {

				        	extractedSunapSuccess(sunapInfo);

				        } else {

						}
					}
				} else { // 서울외 세외
					NextBugaRequestDto nextBugaRequestDto = new NextBugaRequestDto();
					nextBugaRequestDto.setEpayNo(sunapInfo.getEnapbuNo());
					nextBugaRequestDto.setLinkMngKey(sunapInfo.getCntrSn());
					nextBugaRequestDto.setLocgovCode(sunapInfo.getCntrLocgovCode());

					resultMap = ngDonationRelayService.localSunapConfirm(nextBugaRequestDto);


					String status = StringUtils.defaultIfBlank((String)resultMap.get("status"), "");

					if("FAIL".equals(status)) {

						logger.error("■■■debug■■■ 세외수입이 없는 수납자료 입니다. ::  {}",sunapInfo.getCntrSn());

					} else if("ERROR".equals(status)) {

						String errMsg = StringUtils.defaultIfBlank((String)resultMap.get("errorMsg"), "");
						logger.error("■■■debug■■■ 수납결과 확인 중 오류가 발생했습니다.. ::  {} , {}",sunapInfo.getCntrSn(),errMsg);

					} else {
						extractedSunapSuccess(sunapInfo);

					}

				}
				// 대상 배치 조회  상태 업데이트
				ngDonationMapper.updateSunapBatchProcessById(sunapInfo);

			}
		} catch (OpRuntimeException e) {
			logger.error("■■■ERROR■■■ cntrNonSunapFiveMinSunapCheck7 RuntimeException {}",e.getCause());

		} catch (Exception e) {
			logger.error("■■■ERROR■■■ cntrNonSunapFiveMinSunapCheck8 Exception {}",e.getCause());
		}

	}

	private void extractedSunapSuccess(GiveStateTest sunapInfo) {
		//  수납 확인 됨 수납 처리 시도
		HashMap<String, Object> reqmngApproveMap = new HashMap<>();

		sunapInfo.setCntrReqmngCode("300");
		sunapInfo.setDiscription("배치동작에 의해 처리됨");
		sunapInfo.setSttemntPayDe(sunapInfo.getCntrDe());

		long reqId = sequenceService.getId("G_CNTR_REQMNG");
		sunapInfo.setReqId(reqId);
		sunapInfo.setFrstRegisterId(sunapInfo.getUserId());
		sunapInfo.setLocgovCode(sunapInfo.getCntrLocgovCode());

		giveStateService.giveReqmngInsert(sunapInfo);
		sunapInfo.setLastUpdusrId(sunapInfo.getUserId());
		reqmngApproveMap = giveStateService.giveReqmngApprove(sunapInfo);

	}


	@Override
	public void cntrNonSunapdayCompleteSunapCheck() {
		// 23시 40분 배치 돌면서 세외 실시간 수납확인 후 기부테이블에 수납정보 업데이트하기
		List<GiveStateTest> nonSunapList = ngDonationMapper.getCntrNonSunapAlldayList();
		try {

			for(GiveStateTest sunapInfo : nonSunapList) {
				Map<String, Object> resultMap = new HashMap<>();
				sunapInfo.setElctrnPayNo(sunapInfo.getEnapbuNo());
				// 서울 수납 확인
				if(sunapInfo.getSeoulTrgetAt().equals("Y")){
					SeoulParam seoulParam = new SeoulParam();
					seoulParam.setEnapbuNo(sunapInfo.getEnapbuNo());
					seoulParam.setJijacheCd(sunapInfo.getCntrLocgovCode());
					seoulParam.setSystemCd("02");

					resultMap = ngDonationRelayService.etaxSunapInfo(seoulParam);
					String api_response = StringUtils.defaultIfEmpty((String) resultMap.get("api_response"), "");

					if (api_response.equals("")) {// 수납 확인 실패

						logger.error("■■■debug■■■ 수납확인에 실패하였습니다. ");

					} else {

						Object objList = JSONValue.parse(api_response);
				        JSONObject jsonObject = (JSONObject)objList;
				        List<Map<String, Object>> arrResult = (List<Map<String, Object>>) jsonObject.get("ARR_RESULT");
				        String sunapYn = StringUtils.defaultIfEmpty((String) arrResult.get(0).get("SUNAP_YN"), "");

				        if("100".equals(StringUtils.defaultIfEmpty((String) jsonObject.get("RST_CD"), "")) && sunapYn.equals("Y")) {

				        	extractedSunapSuccess(sunapInfo);

				        } else {

						}
					}
				} else { // 서울외 세외
					NextBugaRequestDto nextBugaRequestDto = new NextBugaRequestDto();
					nextBugaRequestDto.setEpayNo(sunapInfo.getEnapbuNo());
					nextBugaRequestDto.setLinkMngKey(sunapInfo.getCntrSn());
					nextBugaRequestDto.setLocgovCode(sunapInfo.getCntrLocgovCode());

					resultMap = ngDonationRelayService.localSunapConfirm(nextBugaRequestDto);


					String status = StringUtils.defaultIfBlank((String)resultMap.get("status"), "");

					if("FAIL".equals(status)) {

						logger.error("■■■debug■■■ 세외수입이 없는 수납자료 입니다. ::  {}",sunapInfo.getCntrSn());

					} else if("ERROR".equals(status)) {

						String errMsg = StringUtils.defaultIfBlank((String)resultMap.get("errorMsg"), "");
						logger.error("■■■debug■■■ 수납결과 확인 중 오류가 발생했습니다.. ::  {} , {}",sunapInfo.getCntrSn(),errMsg);

					} else {
						extractedSunapSuccess(sunapInfo);

					}

				}
				// 대상 배치 조회  상태 업데이트
				ngDonationMapper.updateSunapBatchProcessById(sunapInfo);

			}
		} catch (OpRuntimeException e) {
			logger.error("■■■ERROR■■■ cntrNonSunapFiveMinSunapCheck7 RuntimeException {}",e.getCause());

		} catch (Exception e) {
			logger.error("■■■ERROR■■■ cntrNonSunapFiveMinSunapCheck8 Exception {}",e.getCause());
		}

	}

	@Override
	public void itemDisplayContorlCheck() {
		// 공개여부 변경할 대상 리스트 답례품 정보 조회
		List<ItemBase> itemList = ngDonationMapper.getItemDisplayCheckList();
		try {

			for(ItemBase targetItem : itemList) {
				if (targetItem.getDisplayFlag().equalsIgnoreCase("Y")) {
					ngDonationMapper.updateItemDisplayFlag(targetItem);
				}
			}

		} catch (OpRuntimeException e) {
			logger.error("■■■ERROR■■■ itemDisplayContorlCheck RuntimeException {}",e.getCause());

		} catch (Exception e) {
			logger.error("■■■ERROR■■■ itemDisplayContorlCheck Exception {}",e.getCause());
		}

	}

}
