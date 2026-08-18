package saleson.shop.donation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import saleson.shop.donation.domain.TempPrivateKeyInfo;

public interface TempPrivateKeyInfoRepository extends JpaRepository<TempPrivateKeyInfo, Long>, QuerydslPredicateExecutor<TempPrivateKeyInfo> {
	
	int deleteByUserId(Long userId);
	
	TempPrivateKeyInfo findByUserId(Long userId);
	
}
