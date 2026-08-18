package saleson.common.configuration;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.onlinepowers.framework.orm.mybatis.annotation.MapperBatch;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.session.SqlSessionFactory;
import org.egovframe.rte.psl.dataaccess.mapper.MapperConfigurer;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@Getter @Setter
@Configuration
@EnableJpaRepositories(
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager",
		basePackages = {"com.onlinepowers", "saleson"}
)
@ConditionalOnProperty(value = "saleson.database.vendor")
public class DatabaseSaleson extends DatabaseConfig {

	private String DATABASE_PREFIX = "saleson";
	private String[] BASE_PACKAGES = new String[] {"saleson", "com.onlinepowers"};

	/**
	 * DataSource
	 * @return
	 */
	@Bean(name = "dataSource")
	@ConfigurationProperties(prefix = "saleson.datasource")
	public DataSource dataSource() {
		String profile = System.getProperty("spring.profiles.active");
		if (!ObjectUtils.isEmpty(profile) && !"local".equals(profile)) {
			String jndiName = getYamlProperty("saleson.datasource.jndi-name");
			String flag = getYamlProperty("saleson.datasource.jndi-flag");
			if ("true".equals(flag) && !ObjectUtils.isEmpty(jndiName)) {
				JndiDataSourceLookup datasourceLookup = new JndiDataSourceLookup();
				datasourceLookup.setResourceRef(true);
				return datasourceLookup.getDataSource(jndiName);
			}
		}

		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	/**
	 * Mybatis - sqlSesstionFactory
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		setConfigureSqlSessionFactory(sessionFactoryBean, getVendor(DATABASE_PREFIX), false);

		return sessionFactoryBean.getObject();
	}

	@Bean(name = "sqlSessionBatchFactory")
	public SqlSessionFactory sqlSessionBatchFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		setConfigureSqlSessionFactory(sessionFactoryBean, getVendor(DATABASE_PREFIX), true);

		return sessionFactoryBean.getObject();
	}

	@Bean(name = "sqlSessionTemplate")
	public SqlSessionTemplate firstSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	public MapperConfigurer mapperConfigurer() {

		MapperConfigurer configurer = new MapperConfigurer();
		configurer.setBasePackage(getBasePackagesWithComma(BASE_PACKAGES));
		configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		configurer.setAnnotationClass(Mapper.class);
		return configurer;

	}

	@Bean
	public MapperConfigurer mapperBatchConfigurer() {

		MapperConfigurer configurer = new MapperConfigurer();
		configurer.setBasePackage(getBasePackagesWithComma(BASE_PACKAGES));
		configurer.setSqlSessionFactoryBeanName("sqlSessionBatchFactory");
		configurer.setAnnotationClass(MapperBatch.class);
		return configurer;

	}


	/**
	 * JPA - entityManagerFactory
	 * @param dataSource
	 * @return
	 */
	@Bean(name = "entityManagerFactory")
	public EntityManagerFactory entityManagerFactory(@Qualifier("dataSource") DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setPackagesToScan(BASE_PACKAGES);

		setConfigureEntityManagerFactory(emf, getDialect(DATABASE_PREFIX), getHbm2ddl(DATABASE_PREFIX));
		return emf.getObject();
	}

	@Bean(name = "transactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
