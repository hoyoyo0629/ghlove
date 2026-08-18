package saleson.shop.user.support;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLSocketFactoryMaker {
	
	final private SSLSocketFactory _sslSockFactory;
	
//	final private boolean CERTIFICATE = true;

	public SSLSocketFactoryMaker() throws KeyManagementException, NoSuchAlgorithmException {
		super();
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//				if (!CERTIFICATE) {
//					throw new CertificateException();
//				}
			}
			
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//				if (!CERTIFICATE) {
//					throw new CertificateException();
//				}
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		} };
		// SSL -> TLSv1.2 by SonarQube
		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		sslContext.init(null, trustAllCerts, new SecureRandom());
		_sslSockFactory = sslContext.getSocketFactory();
	}
	
	public SSLSocketFactory getSSLSocketFactory() {
		return _sslSockFactory;
	}
	
}
