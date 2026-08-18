package saleson.common.configuration;

import com.onlinepowers.framework.context.support.ReloadableResourceBundleMessageSourceWithDatabase;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.i18n.support.I18nResolverUsingDatabase;
import com.onlinepowers.framework.i18n.support.I18nResolverUsingSpring;
import com.onlinepowers.framework.i18n.support.MessageResolver;
import com.onlinepowers.framework.repository.CodeInfoRepository;
import com.onlinepowers.framework.repository.MessageInfoRepository;
import com.onlinepowers.framework.repository.code.CodeFactoryBean;
import com.onlinepowers.framework.repository.message.MessageFactoryBean;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MessageSourceConfig {
	private final CodeInfoRepository codeInfoRepository;

	private final MessageInfoRepository messageInfoRepository;

	@Bean
	public ReloadableResourceBundleMessageSourceWithDatabase messageSource() {
		ReloadableResourceBundleMessageSourceWithDatabase source = new ReloadableResourceBundleMessageSourceWithDatabase();

		source.setDefaultEncoding("UTF-8");

		// 메세지 프로퍼티파일의 위치와 이름을 지정한다.
		source.setBasenames(new String[] {
				"classpath:/messages/message"
		});

		source.setI18nResolverUsingDatabase(i18nResolverUsingDatabase());

		// 프로퍼티 파일의 변경을 감지할 시간 간격을 지정한다.
		source.setCacheSeconds(60);

		// 없는 메세지일 경우 예외를 발생시키는 대신 코드를 기본 메세지로 한다.
		source.setUseCodeAsDefaultMessage(true);

		return source;

	}

	@Bean
	public I18nResolverUsingDatabase i18nResolverUsingDatabase() {
		I18nResolverUsingDatabase source = new I18nResolverUsingDatabase();

		source.setCodeInfoRepository(codeInfoRepository);
		source.setMessageInfoRepository(messageInfoRepository);

		return source;
	}

	@Bean
	public MessageFactoryBean MessageFactoryBean() {
		MessageFactoryBean source = MessageFactoryBean.getInstance();
		source.setMessageResolver(i18nResolverUsingSpring());
		return source;
	}

	@Bean
	public I18nResolverUsingSpring i18nResolverUsingSpring() {
		I18nResolverUsingSpring source = new I18nResolverUsingSpring();
		source.setMessageSource(messageSource());
		return source;
	}

	@Bean
	public CodeResolver codeResolver() {
		CodeResolver source = new CodeResolver();
		source.setI18nResolverUsingDatabase(i18nResolverUsingDatabase());
		return source;
	}

	@Bean
	public MessageResolver messageResolver() {
		MessageResolver source = new MessageResolver();
		source.setI18nResolverUsingSpring(i18nResolverUsingSpring());
		return source;
	}

	@Bean
	public CodeFactoryBean CodeFactoryBean() {
		CodeFactoryBean source = CodeFactoryBean.getInstance();
		source.setCodeResolver(codeResolver());
		return source;
	}


}
