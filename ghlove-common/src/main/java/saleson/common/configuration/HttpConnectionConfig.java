package saleson.common.configuration;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.dreamsecurity.magice2e.util.Log;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class HttpConnectionConfig {

    @Bean
    public RestTemplate customRestTemplate(RestTemplateBuilder builder) {

        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
//            e.printStackTrace();
        	Log.error(getClass().getName() + " :: customRestTemplate Exception ==================");
        }

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        HttpComponentsClientHttpRequestFactory customRequestFactory = new HttpComponentsClientHttpRequestFactory();
        customRequestFactory.setHttpClient(httpClient);

        customRequestFactory.setConnectTimeout(5000); // 연결시간초과
        customRequestFactory.setReadTimeout(10000); // 읽기시간초과


        return builder.requestFactory(() -> customRequestFactory).build();

        // return new RestTemplate(getFactory(5000, 10000, 200));
    }

    @Bean
    public RestTemplate naverPaymentRestTemplate() {
        return new RestTemplate(getFactory(60000, 60000, 200));
    }

    @Bean
    public RestTemplate naverOtherRestTemplate() {
        return new RestTemplate(getFactory(10000, 10000, 200));
    }

    @Bean
    public RestTemplate umsAgentRestTemplate() {
        return new RestTemplate(getFactory(60000, 60000, 200));
    }

    @Bean
    public RestTemplate trustSSLRestTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return new RestTemplate(getTrustSSLFactory(5000, 10000, 200));
    }

    private HttpComponentsClientHttpRequestFactory getFactory (int connectTimeout, int readTimeout, int maxConnTotal) {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(connectTimeout); // 연결시간초과
        factory.setReadTimeout(readTimeout); // 읽기시간초과

        HttpClient client = HttpClientBuilder.create()
                .setMaxConnTotal(maxConnTotal)
                //.setMaxConnPerRoute(20) // ip & port 당 연결 제한
                .build();

        factory.setHttpClient(client);

        return factory;
    }

    private HttpComponentsClientHttpRequestFactory getTrustSSLFactory (int connectTimeout, int readTimeout, int maxConnTotal) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        factory.setConnectTimeout(connectTimeout); // 연결시간초과
        factory.setReadTimeout(readTimeout); // 읽기시간초과

        // 모든 인증서를 신뢰하도록 설정
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (X509Certificate[] chain, String authType) -> true).build();

        HttpClient client = HttpClientBuilder.create()
                .setMaxConnTotal(maxConnTotal)
                .setSSLContext(sslContext)
                .build();
        factory.setHttpClient(client);

        return factory;
    }
}
