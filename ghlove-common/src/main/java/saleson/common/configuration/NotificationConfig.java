package saleson.common.configuration;

import com.onlinepowers.framework.notification.mail.MailService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import saleson.common.notification.micesoft.MiceAlimtalkSender;
import saleson.common.notification.micesoft.MiceMessageSender;
import saleson.common.notification.micesoft.MicePushSender;

import java.util.Properties;

@Slf4j
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "saleson.mail")
public class NotificationConfig {
	private static final String MAIL_DEBUG = "mail.debug";
	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	private static final String MAIL_SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
	private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
	private static final String MAIL_SMTP_CONNECTIONTIMEOUT = "mail.smtp.connectiontimeout";
	private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
	private static final String DEFAULT_ENCODING_UTF_8 = "UTF-8";

	@Data
	public static class Smtp {
		private boolean auth;
		private boolean startTlsRequired;
		private boolean startTlsEnable;
	}
	private String host;
	private String protocol;
	private int port;
	private String username;
	private String password;
	private String defaultEncoding;
	private Smtp smtp;


	@Bean
	public MailService mailService() {
		return new MailService(mailSender());
	}

	@Bean
	public JavaMailSender mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(getHost());
		mailSender.setProtocol(getProtocol());
		mailSender.setPort(getPort());
		mailSender.setUsername(getUsername());
		mailSender.setPassword(getPassword());
		mailSender.setDefaultEncoding(DEFAULT_ENCODING_UTF_8);

		Properties properties = mailSender.getJavaMailProperties();
		properties.put(MAIL_SMTP_STARTTLS_REQUIRED, getSmtp().isStartTlsRequired());
		properties.put(MAIL_SMTP_STARTTLS_ENABLE, getSmtp().isStartTlsEnable());
		properties.put(MAIL_SMTP_AUTH, getSmtp().isAuth());
		properties.put(MAIL_DEBUG, true);
		properties.put(MAIL_SMTP_CONNECTIONTIMEOUT, 10000);
		properties.put(MAIL_SMTP_TIMEOUT, 10000);

		mailSender.setJavaMailProperties(properties);

		return mailSender;

	}

	@Bean
	public MiceMessageSender smsSender() {
		return new MiceMessageSender();
	}

	@Bean
	public MiceAlimtalkSender alimtalkSender() {
		return new MiceAlimtalkSender();
	}

	@Bean
	public MicePushSender pushSender() {
		return new MicePushSender();
	}
}
