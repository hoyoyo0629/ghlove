package saleson.shop.totalsearch.support;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("restUtils")
public class RESTUtils {
	private static final Logger logger = LoggerFactory.getLogger(RESTUtils.class);

	public String request(String targetURL) {
		return request(targetURL, "", "GET");
	}

	public String request(String targetURL, String payload, String httpMethod) {
		return request(targetURL, payload, httpMethod, "UTF-8");
	}

	public String request(String targetURL, String payload, String httpMethod, final String charset) {

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			// Create a custom response handler
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

				@Override
				public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status == HttpStatus.SC_OK) {
						HttpEntity entity = response.getEntity();
						//try {
							return entity != null ? EntityUtils.toString(entity, Charset.forName(charset)) : null;
						//} catch (ParseException e) {
							//throw new RuntimeException(e);
						//}
					} else {
						throw new IOException();
					}
				}
			};


			String responseBody;

			if ("POST".equalsIgnoreCase(httpMethod)) { //create
				HttpPost http = new HttpPost(targetURL);
				http.setEntity(new StringEntity(payload, Charset.forName(charset)));
				responseBody = httpClient.execute(http, responseHandler);
			}
			else if("PUT".equalsIgnoreCase(httpMethod)) { //update
				//System.out.println("update : " + payload);
				HttpPut http = new HttpPut(targetURL);
				http.setHeader("Accept", "application/json");
				http.setHeader("Content-type", "text/plain; charset="+Charset.forName(charset));
				if(payload != null)
					http.setEntity(new StringEntity(payload, Charset.forName(charset)));
				responseBody = httpClient.execute(http, responseHandler);
			}
			else if("DELETE".equalsIgnoreCase(httpMethod)) { //delete
				HttpDelete http = new HttpDelete(targetURL);
				responseBody = httpClient.execute(http, responseHandler);
			}
			else { //get
				HttpGet http = new HttpGet(targetURL);
				responseBody = httpClient.execute(http, responseHandler);
			}

			return responseBody;

		} catch (MalformedURLException e) {
			logger.error("MalformedURLException failed : " + targetURL );
			return null;
		} catch (IOException e) {
			logger.error("IOException failed : " + targetURL);
			return null;
		} catch (Exception e) {
			logger.error("responseBody failed : ");
			return null;
		}
	}
}
