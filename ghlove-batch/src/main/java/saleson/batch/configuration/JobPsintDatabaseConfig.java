package saleson.batch.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

@Configuration
public class JobPsintDatabaseConfig {

	@Value("${saleson.datasource.driver-class-name}")
    private String driverClassName;

	@Value("${saleson.datasource.jdbc-url")
	private String url;

	@Value("${saleson.datasource.username}")
	private String userName;

	@Value("${saleson.datasource.password}")
	private String passWord;


	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(userName);
		dataSource.setPassword(passWord);
		return dataSource;
	}
}
