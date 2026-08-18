package saleson.api.siren24;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import saleson.shop.user.support.SSLSocketFactoryMaker;

@Component
public class SciPhoneUtils {

	private Logger log = LoggerFactory.getLogger(SciPhoneUtils.class);

	@Value("${sci.phone.url}")
    public String PHONE_URL;

    @Value("${sci.token.url}")
    public String CRYPTO_TOKEN_URL;

    @Value("${sci.token.access}")
	public String ACCESS_TOKEN;

    @Value("${sci.token.client}")
	public String CLIENT_ID;

    // 타임아웃 상수
    private static final int CONNECT_TIMEOUT = 10;

    private boolean CERTIFICATE = true;

    /***************************************************************************************
     * API 호출
     ***************************************************************************************/

	/* Crypto Token 발급 */
	public String callCreateCryptoTokenAPI(String req_date, String req_no) throws Exception {
	    Map<String, String> headers = new LinkedHashMap<>();
	    headers.put("Content-Type", "application/json; charset=utf-8");
	    headers.put("Authorization", "bearer " + ACCESS_TOKEN);

	    JSONObject dataHeader = new JSONObject();
	    dataHeader.put("lang_code", "kr");
	    JSONObject dataBody = new JSONObject();
	    dataBody.put("client_id", 	CLIENT_ID);
	    dataBody.put("req_date", 	req_date);
	    dataBody.put("req_no", 		req_no);
	    dataBody.put("enc_mode",	"1");

	    JSONObject msg = new JSONObject();
	    msg.put("dataHeader", 	dataHeader);
	    msg.put("dataBody", 	dataBody);

	    HttpURLConnection conn = getURLConnection(CRYPTO_TOKEN_URL, "POST", headers, true);
	    send(conn.getOutputStream(), msg.toJSONString());
	    return receive(conn);
	}

	/* Server To Server API 호출 */
	public String callServerToServerAPI(String crypto_token_id, String reqInfoEnc, String integrityValue, String tranId) throws Exception {
	    Map<String, String> headers = new LinkedHashMap<>();
	    headers.put("Content-Type", "application/json; charset=utf-8");

	    JSONObject dataHeader = new JSONObject();
	    dataHeader.put("CNTY_CD", "kr");
	    dataHeader.put("TRAN_ID", tranId);

	    JSONObject dataBody = new JSONObject();
	    dataBody.put("crypto_token_id", crypto_token_id);
	    dataBody.put("reqInfo", 		reqInfoEnc);
	    dataBody.put("integrity_value", integrityValue);

	    JSONObject msg = new JSONObject();
	    msg.put("dataHeader", 	dataHeader);
	    msg.put("dataBody", 	dataBody);

	    HttpURLConnection conn = getURLConnection(PHONE_URL, "POST", headers, true);
	    send(conn.getOutputStream(), msg.toJSONString());
	    return receive(conn);
	}

    /***************************************************************************************
     * 암호화 / 복호화
     ***************************************************************************************/

	/* 대칭키 생성 ( SHA-256 -> Base64 ) */
	public String createSymmetricKey(String req_dtim, String req_no, String token_val) throws Exception {
	    String value = req_dtim.trim() + req_no.trim() + token_val.trim();
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(value.getBytes(StandardCharsets.UTF_8));
	    return Base64.getEncoder().encodeToString(md.digest());
	}

	/* AES/CBC 암호화 */
	public String getEncReqData(String key, String iv, String reqData) throws Exception {
        SecretKey secureKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8)));
	    return Base64.getEncoder().encodeToString(cipher.doFinal(reqData.trim().getBytes(StandardCharsets.UTF_8)));
	}

	/* AES/CBC 복호화 */
	public String getDecReqData(String key, String iv, String encData) throws Exception {
        SecretKey secureKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(iv.getBytes("UTF-8")));
	    return new String(cipher.doFinal(Base64.getDecoder().decode(encData)), StandardCharsets.UTF_8);
	}

	/* HMAC-SHA256 (mobile-auth integrity_value용) */
	public byte[] hmac256(byte[] secretKey,byte[] message) throws Exception{
	    Mac mac = Mac.getInstance("HmacSHA256");
	    mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
	    return mac.doFinal(message);
	}

	/* SHA-256 -> Base64(mobile-auth-callback integrity_value용) */
	public String base64Sha256(String s) throws Exception{
		byte[] d = MessageDigest.getInstance("SHA-256").digest(s.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(d);
	}

    /***************************************************************************************
     * reqInfo JSON 구성
     ***************************************************************************************/

	/* Server to Server용 reqInfo (mobile-auth-callback) */
	public String getReqData(String id, String reqcryptotokenid) {
	    JSONObject o = new JSONObject();
	    o.put("id", id);
	    o.put("reqcryptotokenid", reqcryptotokenid);
	    return o.toJSONString();
	}

	/* 요청용 reqInfo (mobile-auth) */
	public String getReqData(String id, String srvNo, String reqNum, String retUrl, String certDate, String certGb) {
    	JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("srvNo", srvNo);
        o.put("reqNum", reqNum);
        o.put("retUrl", retUrl);
        o.put("certDate", certDate);
        o.put("certGb", certGb);
        return o.toJSONString();
    }

    /***************************************************************************************
     * 기타
     ***************************************************************************************/
	/* reqNo 생성 */
	public String makeReqNo() {
	    return String.valueOf(System.currentTimeMillis()).substring(3);
	}

	/* HTTP 커넥션 (HTTPS 포함) */
	private HttpURLConnection getURLConnection(String urlStr, String method, Map<String, String> headers, boolean doOutput) throws Exception {
	    URL url = new URL(urlStr);
	    final HttpURLConnection http = (HttpURLConnection) url.openConnection();

	    // 기존 restFulToRelayServer 방식 그대로
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

	    http.setConnectTimeout(CONNECT_TIMEOUT);
	    http.setReadTimeout(30000);
	    http.setDefaultUseCaches(false);
	    http.setDoInput(true);
	    http.setDoOutput(doOutput);
	    http.setRequestMethod(method);
	    for (Map.Entry<String, String> e : headers.entrySet()) {
	        http.setRequestProperty(e.getKey(), e.getValue());
	    }

	    return http;
	}

	private void send(OutputStream os, String msg) throws Exception {
	    try (final OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
	        PrintWriter writer = new PrintWriter(osw);
	        writer.write(msg);
	        writer.flush();
	    }
	}
	private String receive(InputStream is) throws Exception {
	    StringBuilder sb = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
	        String line;
	        while ((line = br.readLine()) != null) sb.append(line);
	    }
	    return sb.toString();
	}

	private String receive(HttpURLConnection conn) throws Exception {
	    int statusCode = conn.getResponseCode();
	    InputStream is = (statusCode >= 200 && statusCode < 300)
	                     ? conn.getInputStream()
	                     : conn.getErrorStream();  // 4xx/5xx일 때 에러스트림

	    StringBuilder sb = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
	        String line;
	        while ((line = br.readLine()) != null) sb.append(line);
	    }
	    return sb.toString();
	}

}
