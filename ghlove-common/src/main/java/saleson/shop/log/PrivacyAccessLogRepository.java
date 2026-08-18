package saleson.shop.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import saleson.shop.log.domain.PrivacyAccessLog;

public interface PrivacyAccessLogRepository extends JpaRepository<PrivacyAccessLog, Long>, QuerydslPredicateExecutor<PrivacyAccessLog> {
}