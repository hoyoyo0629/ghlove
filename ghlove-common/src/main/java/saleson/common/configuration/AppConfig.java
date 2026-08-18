package saleson.common.configuration;

import com.onlinepowers.framework.file.security.OpCrypto;
import com.onlinepowers.framework.file.security.service.CipherServiceImpl;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.file.view.FileDownloadView;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.util.AntPathMatcher;
import saleson.common.file.infra.AmazonS3Storage;
import saleson.common.file.infra.FileStorage;
import saleson.common.file.infra.LocalFileStorage;
import saleson.common.file.service.AmazonFileService;
import saleson.common.file.service.LocalFileService;

//@Configuration
//@ComponentScan(basePackages = {"saleson", "com.onlinepowers"},
//		excludeFilters = @ComponentScan.Filter(type=FilterType.ANNOTATION, pattern="org.springframework.stereotype.Controller"))

@Configuration
@EnableJpaAuditing
public class AppConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


	@Bean
	public AntPathMatcher antPathMatcher() {
		return new AntPathMatcher();
	}

	// 암호화
	@Bean
	public CipherServiceImpl cipherService() {
		CipherServiceImpl cipherService = new CipherServiceImpl();
		cipherService.setCrypto(new OpCrypto());
		return cipherService;
	}

	@Bean
	public FileDownloadView fileDownloadView() {
		return new FileDownloadView();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public FileStorage fileStorage() {
		return new LocalFileStorage();
	}

	@Bean
	public FileService fileService() {
		return new LocalFileService();
	}

}

