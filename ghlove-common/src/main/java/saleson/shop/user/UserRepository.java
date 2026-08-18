package saleson.shop.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import saleson.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>, QuerydslPredicateExecutor<UserEntity> {

	UserEntity findByUserKey(String userKey);
	UserEntity findByMberCi(String mberCi);
	UserEntity findByLoginId(String loginId);
	UserEntity findByUserId(Long userId);
}
