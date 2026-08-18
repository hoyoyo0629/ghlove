//package saleson.common.configuration;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.catalina.connector.Connector;
//import org.apache.coyote.ajp.AbstractAjpProtocol;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//@Slf4j
//@Configuration
//public class TomcatConfig {
//	@Value("${server.ajp.enabled}")
//	boolean ajpEnabled;
//
//	@Value("${server.ajp.protocol}")
//	String ajpProtocol;
//
//	@Value("${server.ajp.port}")
//	int ajpPort;
//
//	@Bean
//	public ServletWebServerFactory servletContainer() {
//		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//		if (ajpEnabled) {
//			tomcat.addAdditionalTomcatConnectors(createAjpConnector());
//		}
//		return tomcat;
//	}
//	private Connector createAjpConnector() {
//		Connector ajpConnector = new Connector(ajpProtocol);
//		ajpConnector.setPort(ajpPort);
//		ajpConnector.setSecure(false);
//		ajpConnector.setAllowTrace(false);
//		ajpConnector.setScheme("http");
//
//		log.debug("[TomcatConfig-WEB] ajpProtocol: {}", ajpProtocol);
//		log.debug("[TomcatConfig-WEB] ajpPort: {}", ajpPort);
//
//		// SKC-20201015 tomcat 8.5 버전과 9.0.x 버전은 secretRequired 속성을 사용하여야 합. ( secretRequired="false" )
//		// 9.0.x 버전에서 AJP port가 제대로 활성화되기 위해서 address="0.0.0.0"을 추가해야한다 (ip4경우)
//		((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setSecretRequired(false);
//		try {
//			((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setAddress(InetAddress.getByName("0.0.0.0"));
//		} catch (UnknownHostException e) {
//			log.debug("###### Tomcat AJP setAddress() ERROR", e);
//		}
//
//		log.debug("[TomcatConfig-WEB] getSecretRequired: {}", ((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).getSecretRequired());
//		log.debug("[TomcatConfig-WEB] getAddress: {}", ((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).getAddress());
//
//		return ajpConnector;
//	}
//}
