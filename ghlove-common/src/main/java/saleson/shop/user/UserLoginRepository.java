package saleson.shop.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import saleson.model.user.UserLogin;

import java.util.List;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long>, QuerydslPredicateExecutor<UserLogin> {

    List<UserLogin> findUserLoginsByUserId(long userId);

    void deleteUserLoginsByUserId(long userId);
}
