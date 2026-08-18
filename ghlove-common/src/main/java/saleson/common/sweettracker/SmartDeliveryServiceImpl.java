package saleson.common.sweettracker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import saleson.common.sweettracker.dto.DeliveryRequest;
import saleson.common.sweettracker.dto.TrackingDetail;
import saleson.common.sweettracker.dto.TrackingInfoResponse;
import saleson.shop.user.support.SSLSocketFactoryMaker;


@Service("SmartDeliveryService")
@RequiredArgsConstructor
public class SmartDeliveryServiceImpl implements SmartDeliveryService {

	private Logger log = LoggerFactory.getLogger(SmartDeliveryServiceImpl.class);

	@Value("${sweettracker.url}")
	private String TRACKING_INFO_URL;

	@Value("${sweettracker.api_key}")
	private String SWEETTRACKER_API_KEY;

    // 타임아웃 상수
    private static final int CONNECT_TIMEOUT_DELIVERY = 10;

    private boolean CERTIFICATE = true;

    @SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> delivery(DeliveryRequest req) {
		log.debug("Delivery request received. carrierCode={}, invoiceNo={}", req.getCarrierCode(), req.getInvoiceNo());

		Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();

        String url = buildTrackingInfoUrl(
                TRACKING_INFO_URL,
                req.getCarrierCode(),
                req.getInvoiceNo(),
                SWEETTRACKER_API_KEY
        );

        try {
            String response = restFulToRelayServerHttpsGET(url);
//        	String response = "{\"adUrl\":null,\"complete\":true,\"invoiceNo\":\"752049281513\",\"itemImage\":null,\"itemName\":\"\",\"level\":6,\"receiverAddr\":\"\",\"receiverName\":\"\",\"recipient\":\"\",\"result\":\"Y\",\"senderName\":\"\",\"trackingDetails\":[{\"kind\":\"집하완료\",\"level\":2,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758884400000,\"timeString\":\"2025-09-26 20:00:00\",\"where\":\"완주봉동(집)\",\"code\":null,\"remark\":null},{\"kind\":\"간선상차\",\"level\":3,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758888000000,\"timeString\":\"2025-09-26 21:00:00\",\"where\":\"전주\",\"code\":null,\"remark\":null},{\"kind\":\"입고\",\"level\":3,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758888000000,\"timeString\":\"2025-09-26 21:00:00\",\"where\":\"전주\",\"code\":null,\"remark\":null},{\"kind\":\"간선상차\",\"level\":3,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758906000000,\"timeString\":\"2025-09-27 02:00:00\",\"where\":\"Mega-Hub\",\"code\":null,\"remark\":null},{\"kind\":\"간선하차\",\"level\":3,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758906000000,\"timeString\":\"2025-09-27 02:00:00\",\"where\":\"Mega-Hub\",\"code\":null,\"remark\":null},{\"kind\":\"간선하차\",\"level\":3,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758924000000,\"timeString\":\"2025-09-27 07:00:00\",\"where\":\"조치원\",\"code\":null,\"remark\":null},{\"kind\":\"배송출발\",\"level\":5,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758931200000,\"timeString\":\"2025-09-27 09:00:00\",\"where\":\"세종중앙(집)\",\"code\":null,\"remark\":null},{\"kind\":\"배송완료\",\"level\":6,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758934800000,\"timeString\":\"2025-09-27 10:00:00\",\"where\":\"세종중앙(집)\",\"code\":null,\"remark\":null}],\"orderNumber\":null,\"estimate\":null,\"productInfo\":null,\"zipCode\":null,\"lastDetail\":{\"kind\":\"배송완료\",\"level\":6,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758934800000,\"timeString\":\"2025-09-27 10:00:00\",\"where\":\"세종중앙(집)\",\"code\":null,\"remark\":null},\"lastStateDetail\":{\"kind\":\"배송완료\",\"level\":6,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758934800000,\"timeString\":\"2025-09-27 10:00:00\",\"where\":\"세종중앙(집)\",\"code\":null,\"remark\":null},\"firstDetail\":{\"kind\":\"집하완료\",\"level\":2,\"manName\":\"\",\"manPic\":\"\",\"telno\":\"\",\"telno2\":\"\",\"time\":1758884400000,\"timeString\":\"2025-09-26 20:00:00\",\"where\":\"완주봉동(집)\",\"code\":null,\"remark\":null},\"completeYN\":\"Y\"}";

            log.debug("SweetTracker response: {}", response);

            Object objList = JSONValue.parse(response);
            JSONObject jsonObject = (JSONObject)objList;

//            try {
//
//                TrackingInfoResponse tracking = new ObjectMapper().readValue(response, TrackingInfoResponse.class);
//                if (tracking != null) {
//                    logTrackingInfo(tracking);
//                } else {
//                    log.warn("TrackingInfoResponse parsed as null. Raw kept as string.");
//                }
//            } catch (Exception parseEx) {
//                log.error("Failed to parse SweetTracker response: {}", parseEx.getMessage(), parseEx);
//            }
            SERVICE_RELAY_RESULT.putAll(jsonObject);

            return SERVICE_RELAY_RESULT;

    	} catch(Exception e) {
    		log.error("■■■API■■■ deliveryRequest EXCEPTION : {} {}", req.getInvoiceNo(), e.getStackTrace());
    		log.error(e.toString());
    	}

        return SERVICE_RELAY_RESULT;
	}

    private static String buildTrackingInfoUrl(String base, String code, String invoice, String apiKey) {
        String tCode = code == null ? "" : code;
        String tInvoice = invoice == null ? "" : invoice;
        return String.format(
                "%s?t_code=%s&t_invoice=%s&t_key=%s",
                base,
                tCode,
                tInvoice,
                apiKey
        );
    }

    private void logTrackingInfo(TrackingInfoResponse tracking) {
        int cnt = (tracking.getTrackingDetails() == null) ? 0 : tracking.getTrackingDetails().size();
        log.info("[TrackingInfo] invoiceNo={}, result={}, complete={}, completeYN={}, level={}, itemName={}",
                tracking.getInvoiceNo(), tracking.getResult(), tracking.isComplete(),
                tracking.getCompleteYN(), tracking.getLevel(), tracking.getItemName());
        log.info("  receiverName={}, receiverAddr={}, senderName={}",
                tracking.getReceiverName(), tracking.getReceiverAddr(), tracking.getSenderName());
        log.info("  detailsCount={}", cnt);

        if (cnt > 0) {
            TrackingDetail first = tracking.getFirstDetail() != null
                    ? tracking.getFirstDetail()
                    : tracking.getTrackingDetails().get(0);
            TrackingDetail last = tracking.getLastStateDetail() != null
                    ? tracking.getLastStateDetail()
                    : tracking.getTrackingDetails().get(cnt - 1);

            log.info("  firstDetail: kind={}, where={}, timeString={}, level={}",
                    first.getKind(), first.getWhere(), first.getTimeString(), first.getLevel());
            log.info("  lastDetail : kind={}, where={}, timeString={}, level={}",
                    last.getKind(), last.getWhere(), last.getTimeString(), last.getLevel());

            for (int i = 0; i < Math.min(5, cnt); i++) {
                TrackingDetail d = tracking.getTrackingDetails().get(i);
                log.info("    - detail[{}] kind={}, where={}, timeString={}, level={}",
                        i, d.getKind(), d.getWhere(), d.getTimeString(), d.getLevel());
            }
            if (cnt > 5) {
                log.info("    - ... ({} more details)", cnt - 5);
            }
        }
    }


	/**
	 * <pre>
	 * comment       : RESTFUL 연계
	 * preMethodName : restFulToRelayServerHttpsGET
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
	public String restFulToRelayServerHttpsGET(String API_URL) throws Exception  {
		String response = "";

		URL url = new URL(API_URL);

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
			http.setConnectTimeout(CONNECT_TIMEOUT_DELIVERY);
			http.setReadTimeout(30000);
			http.setDefaultUseCaches(false);
			http.setDoInput(true);
			http.setDoOutput(false);

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
