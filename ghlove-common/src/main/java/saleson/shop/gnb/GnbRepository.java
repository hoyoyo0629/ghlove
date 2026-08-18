package saleson.shop.gnb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import saleson.model.Gnb;

public interface GnbRepository extends JpaRepository<Gnb, Long>, QuerydslPredicateExecutor<Gnb> {

}
