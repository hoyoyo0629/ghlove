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

@Getter @Setter
@Configuration
public class DatabaseSlave extends DatabaseConfig {

	private final String DATABASE_PREFIX = "slave";
	private final String[] BASE_PACKAGES = new String[] {"saleson.shop.slave"};



	@Bean(name = DATABASE_PREFIX + "DataSource")
	@ConfigurationProperties(prefix = "slave.datasource")
	public DataSource dataSource() {
		String profile = System.getProperty("spring.profiles.active");
		if (!ObjectUtils.isEmpty(profile) && !"local".equals(profile)) {
			String jndiName = getYamlProperty("slave.datasource.jndi-name");
			String flag = getYamlProperty("slave.datasource.jndi-flag");
			if ("true".equals(flag) && !ObjectUtils.isEmpty(jndiName)) {
				JndiDataSourceLookup datasourceLookup = new JndiDataSourceLookup();
				datasourceLookup.setResourceRef(true);
				return datasourceLookup.getDataSource(jndiName);
			}
		}

		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

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
		MapperScanConfigurer mapper = new MapperScanConfigurer(MapperSlave.class, DATABASE_PREFIX + "SqlSessionFactory");
		mapper.setBasePackage(getBasePackagesWithComma(BASE_PACKAGES));
		return mapper;
	}

}
