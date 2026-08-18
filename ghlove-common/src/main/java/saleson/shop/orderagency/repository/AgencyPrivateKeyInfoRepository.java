package saleson.shop.orderagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import saleson.shop.orderagency.entity.AgencyPrivateKeyInfo;

public interface AgencyPrivateKeyInfoRepository extends JpaRepository<AgencyPrivateKeyInfo, Long>, QuerydslPredicateExecutor<AgencyPrivateKeyInfo> {
	
	int deleteByUserSessionId(String userSessionId);
	
	AgencyPrivateKeyInfo findByUserSessionId(String userSessionId);
	
}
