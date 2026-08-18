package saleson.common.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * Spring Session Jdbc 사용 중 Controller에 ajax요청을 동시에 호출하면
 * OP_SESSION_ATTRIBUTE에 데이터를 INSERT하는 과정에서 Key Duplication 오류가 발생할 수 있다.
 *
 * Mysql인 경우 Key 중복이 발생하는 경우 ATTRIBUTE_BYTES 값을 업데이트 하는 쿼리로 변경함.
 * config.database.vendor=mysql 인 경우 이 Config설정이 추가됨.
 *
 * 근본적인 해결책은 다시 확인해 봐야하지만 현재 기준으로는 Spring Session Jdbc를 사용하는 경우 이 이슈가 발생할 수 있는 것으로 보인다.
 * 다른 DB는?? 필요시 추가하자!
 *
 * @Exception SQLIntegrityConstraintViolationException:
 * 			  Duplicate entry '5f825dc9-b344-450b-8e48-48c045b60030-SPRING_SECURITY_CONTEXT' for key 'PRIMARY'
 * @See https://github.com/spring-projects/spring-session/issues/1213
 * @Author skc@onlinepowers.com
 * @Data 2021-03-23
 */
@Configuration
@ConditionalOnProperty(value = "saleson.database.vendor", havingValue = "mysql")
public class MysqlConfig {
	private static final String CREATE_SESSION_ATTRIBUTE_QUERY_ON_DUPLICATE_KEY_UPDATE =
			"INSERT INTO %TABLE_NAME%_ATTRIBUTES(SESSION_PRIMARY_ID, ATTRIBUTE_NAME,  ATTRIBUTE_BYTES) "

					+ "VALUES (?, ?, ?)"
//					+ " SELECT PRIMARY_ID, ?, ? "
//					+ " FROM %TABLE_NAME% "
//					+ " WHERE SESSION_ID = ? "
					+ " ON DUPLICATE KEY UPDATE ATTRIBUTE_BYTES=VALUES(ATTRIBUTE_BYTES) ";

	private final JdbcIndexedSessionRepository jdbcIndexedSessionRepository;

	public MysqlConfig(JdbcIndexedSessionRepository jdbcIndexedSessionRepository) {
		this.jdbcIndexedSessionRepository = jdbcIndexedSessionRepository;
	}

	@PostConstruct
	public void customizedJdbcOperationsSessionRepository() {
		jdbcIndexedSessionRepository.setCreateSessionAttributeQuery(
				StringUtils.replace(
						CREATE_SESSION_ATTRIBUTE_QUERY_ON_DUPLICATE_KEY_UPDATE,
						"%TABLE_NAME%",
						"OP_SESSION"));

	}
}
