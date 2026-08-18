package saleson.common.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.ObjectUtils;

import com.zaxxer.hikari.HikariConfig;

import lombok.Getter;

@Getter
public class DatabaseConfig extends HikariConfig {

	private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

	private Properties yamlProperties;

	public DatabaseConfig() {
		String profile = System.getProperty("spring.profiles.active");
		YamlPropertiesFactoryBean yamlProperties = new YamlPropertiesFactoryBean();
		ClassPathResource application = new ClassPathResource("application.yml");
		if (!ObjectUtils.isEmpty(profile)) {
			yamlProperties.setResources(application, new ClassPathResource("application-"+profile+".yml"));
		} else {
			yamlProperties.setResources(application);
		}
		yamlProperties.afterPropertiesSet();
		this.yamlProperties = yamlProperties.getObject();
	}

	protected void setConfigureEntityManagerFactory(LocalContainerEntityManagerFactoryBean factory,
													String dialect,
													String hbm2ddl) {
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		factory.setJpaVendorAdapter(vendorAdapter);
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", dialect);
		jpaProperties.put("hibernate.hbm2ddl.auto", hbm2ddl);
		jpaProperties.put("hibernate.jdbc.lob.non_contextual_creation", "true");
		jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "true");
		jpaProperties.put("hibernate.physical_naming_strategy", "saleson.common.hibenate.naming.UnderscoreNamingStrategy");

		factory.setJpaProperties(jpaProperties);
		factory.afterPropertiesSet();
	}

	protected void setConfigureSqlSessionFactory(SqlSessionFactoryBean sessionFactoryBean, String database, boolean isBatch) throws Exception {

		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();


		String configLocation = "classpath:mybatis/mybatis-config.xml";

		if (isBatch) {
			configLocation = "classpath:mybatis/mybatis-config-batch.xml";
		}
		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource(configLocation);

		sessionFactoryBean.setConfigLocation(myBatisConfig);
		sessionFactoryBean.setMapperLocations(getResources(database, isBatch));
	}


	private Resource[] getResources(String database) throws Exception {
		return getResources(database, false);
	}
	private Resource[] getResources(String database, boolean isBatch) throws Exception {
		if (isBatch) {
			Resource[] res1 = new PathMatchingResourcePatternResolver().getResources("classpath*:sqlmapper/" + database + "/common-mapper.xml");
			Resource[] res2 = new PathMatchingResourcePatternResolver().getResources("classpath*:sqlmapper/" + database + "/batch/**/*-mapper-batch.xml");
			return Stream.concat(Arrays.stream(res1), Arrays.stream(res2)).toArray(Resource[]::new);
		}

		Resource[] res1 = new PathMatchingResourcePatternResolver().getResources("classpath*:sqlmapper/" + database + "/*-mapper.xml");
		Resource[] res2 = new PathMatchingResourcePatternResolver().getResources("classpath*:sqlmapper/" + database + "/seller/*-mapper.xml");
		return Stream.concat(Arrays.stream(res1), Arrays.stream(res2)).toArray(Resource[]::new);
	}

	protected String getYamlProperty(String key) {
		return yamlProperties.getProperty(key);
	}


	protected String getVendor(String dataSourceKey) {
		return getYamlProperty(dataSourceKey + ".database.vendor");
	}

	protected String getDialect(String dataSourceKey) {
		return getYamlProperty(dataSourceKey + ".database.dialect");
	}

	protected String getHbm2ddl(String dataSourceKey) {
		return getYamlProperty(dataSourceKey + ".database.hbm2ddl");
	}

	protected String getBasePackagesWithComma(String[] basePackage) {
		List<String> names = Arrays.asList(basePackage);
		return String.join(",", names);
	}

}
