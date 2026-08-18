package saleson.common.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.ObjectUtils;

import com.onlinepowers.framework.orm.mybatis.MapperScanConfigurer;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import lombok.Setter;


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
@Configuration
public class DatabaseNhApi extends DatabaseConfig {

	private final String DATABASE_PREFIX = "nhapi";
	private final String[] BASE_PACKAGES = new String[] {"saleson.shop.nhapi"};



	@Bean(name = DATABASE_PREFIX + "DataSource")
	@ConfigurationProperties(prefix = "nhapi.datasource")
	public DataSource dataSource() {
		String profile = System.getProperty("spring.profiles.active");
		if (!ObjectUtils.isEmpty(profile) && !"local".equals(profile)) {
			String jndiName = getYamlProperty("nhapi.datasource.jndi-name");
			String flag = getYamlProperty("nhapi.datasource.jndi-flag");
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
		MapperScanConfigurer mapper = new MapperScanConfigurer(MapperNhApi.class, DATABASE_PREFIX + "SqlSessionFactory");
		mapper.setBasePackage(getBasePackagesWithComma(BASE_PACKAGES));
		return mapper;
	}




}
