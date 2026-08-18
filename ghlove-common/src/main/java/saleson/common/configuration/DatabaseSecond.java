package saleson.common.configuration;

import com.onlinepowers.framework.orm.mybatis.MapperScanConfigurer;
import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;


/**
 * 여러개의 DB 접속이 필요한 경우 이 클래스를 활성화 한다.
 * - @Configuration, @EnableJpaRepositories 주석 해제
 * - DATABASE_PREFIX 이름 설정 (샘플은 'second'로 되어 있음)
 * - @MapperXXX annotation 생성
 * - MapperScanConfigurer 설정에서 Mapper.class => MapperXXX.class 로 변경
 *
 * @Author skc@onlinepowers.com
 * @Data 2021-05-28
 */
@Getter @Setter
//@Configuration
//@EnableJpaRepositories(
//		entityManagerFactoryRef = "secondEntityManagerFactory",
//		transactionManagerRef = "secondTransactionManager",
//		basePackages = {"com.onlinepowers", "saleson"}
//)
public class DatabaseSecond extends DatabaseConfig {

	private final String DATABASE_PREFIX = "second";
	private final String[] BASE_PACKAGES = new String[] {"saleson", "com.onlinepowers"};



	@Bean(name = DATABASE_PREFIX + "DataSource")
	@ConfigurationProperties(prefix = "second.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}


	/**
	 * Mybatis - sqlSesstionFactory
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean(name = DATABASE_PREFIX + "SqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier(DATABASE_PREFIX + "DataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		setConfigureSqlSessionFactory(sessionFactoryBean, DATABASE_PREFIX, false);

		return sessionFactoryBean.getObject();
	}

	@Bean(name = DATABASE_PREFIX + "SqlSessionTemplate")
	public SqlSessionTemplate firstSqlSessionTemplate(@Qualifier(DATABASE_PREFIX + "SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}


	@Bean(name = DATABASE_PREFIX + "MapperConfigurer")
	public MapperScanConfigurer mapperConfigurer() {
		MapperScanConfigurer mapper = new MapperScanConfigurer(Mapper.class, DATABASE_PREFIX + "SqlSessionFactory");
		mapper.setBasePackage(getBasePackagesWithComma(BASE_PACKAGES));
		return mapper;
	}


	/**
	 * JPA - entityManagerFactory
	 * @param dataSource
	 * @return
	 */
	@Bean(name = DATABASE_PREFIX + "EntityManagerFactory")
	public EntityManagerFactory entityManagerFactory(@Qualifier(DATABASE_PREFIX + "DataSource") DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource);
		emf.setPackagesToScan(BASE_PACKAGES);

		setConfigureEntityManagerFactory(emf, getDialect(DATABASE_PREFIX), getHbm2ddl(DATABASE_PREFIX));
		return emf.getObject();
	}


	@Bean(name = DATABASE_PREFIX + "TransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier(DATABASE_PREFIX + "EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
