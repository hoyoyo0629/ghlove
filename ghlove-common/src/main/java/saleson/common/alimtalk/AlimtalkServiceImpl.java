package saleson.common.alimtalk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("alimtalkService")
public class AlimtalkServiceImpl implements AlimtalkService{

	private static final Logger log = LoggerFactory.getLogger(AlimtalkServiceImpl.class);

    @Value("${ums.kakao.alimtalk.if-url}")
    private String API_URL;

    @Autowired
    AlimtalkMapper alimtalkMapper;

    @Override
    public void sendAlimtalk(Alimtalk alimtalk){

    	String response = "";
    	try {
			alimtalk.setIfReqId(alimtalk.timer("id"));
			alimtalk.setIfStartTime(alimtalk.timer("start"));

    		// 1. API REQUEST
			response = restFulToAlimtalkServerHttpsPOST(alimtalk);

			// 2. 연계 테이블 INSERT
	    	ObjectMapper mapper = new ObjectMapper();
			AlimtalkResponse res = mapper.readValue(response, AlimtalkResponse.class);

			if(res.isSuccess()) {
				alimtalk.setReceiptNum(res.getData().getReceiptNum());
				alimtalk.setIfStatusCd("S");
			}else{
				alimtalk.setIfStatusCd("F");
				alimtalk.setIfErrMsg(String.valueOf(res.getMessage()));
			};

    	}catch (JsonProcessingException e) {
    		log.error("sendAlimTalk JsonProcessingException {} : ", e);
			alimtalk.setIfStatusCd("F");
			alimtalk.setIfErrMsg(e.toString());
    	}catch (Exception e) {
            log.error("sendAlimTalk Error {} : ", e);
			alimtalk.setIfStatusCd("F");
			alimtalk.setIfErrMsg(e.toString());
		}

    	alimtalk.setIfEndTime(alimtalk.timer("end"));
		alimtalkMapper.insertTifAlimtalkInfo(alimtalk);

    }

	/**
	 * <pre>
	 * comment       : RESTFUL 카카오 알림톡 연계
	 * preMethodName : restFulToAlimtalkServerHttpsPOST
	 * author        : --
	 * date          : 2026. 3. 17.
	 *
	 * </pre>
	 * @param AlimTalk
	 * @return
	 * @throws Exception
	 */
	public String restFulToAlimtalkServerHttpsPOST(Alimtalk alimtalk) throws Exception  {
		String response = "";
		URL url = new URL(API_URL);
		final HttpURLConnection http = (HttpURLConnection) url.openConnection();

		try (AutoCloseable ac = () -> http.disconnect()) {
			http.setConnectTimeout(10);
			http.setReadTimeout(30000);
			http.setDefaultUseCaches(false);
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setRequestMethod("POST");

			http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

			ObjectMapper objectMapper = new ObjectMapper();
			String requestBody = objectMapper.writeValueAsString(alimtalk);

			try (OutputStream os = http.getOutputStream()) {
            	byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
				os.write(input);
				os.flush();
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

}
