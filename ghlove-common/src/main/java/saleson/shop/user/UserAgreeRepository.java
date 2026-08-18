package saleson.shop.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import saleson.model.UserAgree;

public interface UserAgreeRepository extends JpaRepository<UserAgree, Long>, QuerydslPredicateExecutor<UserAgree> {
}
