package saleson.seller.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import saleson.model.user.SellerUserLogin;

import java.util.List;

public interface SellerUserLoginRepository extends JpaRepository<SellerUserLogin, Long>, QuerydslPredicateExecutor<SellerUserLogin> {

    List<SellerUserLogin> findSellerUserLoginsByUserId(long userId);

    void deleteSellerUserLoginsByUserId(long userId);
}
