package saleson.common.configuration;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.util.ObjectUtils;

@Configuration
public class LogDataSourceConfig {

	@Bean(name = "logDataSource")
	@ConfigurationProperties("log.datasource")
//	public HikariDataSource logDataSource() {
	public DataSource logDataSource() {
		String profile = System.getProperty("spring.profiles.active");
		if (!ObjectUtils.isEmpty(profile) && !"local".equals(profile)) {
			DatabaseConfig config = new DatabaseConfig();
			String jndiName = config.getYamlProperty("log.datasource.jndi-name");
			String flag = config.getYamlProperty("log.datasource.jndi-flag");
			if ("true".equals(flag) && !ObjectUtils.isEmpty(jndiName)) {
				JndiDataSourceLookup datasourceLookup = new JndiDataSourceLookup();
				datasourceLookup.setResourceRef(true);
				return datasourceLookup.getDataSource(jndiName);
			}
		}

		return DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build();
	}

	@Bean(name = "logJdbcTemplate")
	public JdbcTemplate logJdbcTemplate() {
		return new JdbcTemplate(logDataSource());
	}
}
