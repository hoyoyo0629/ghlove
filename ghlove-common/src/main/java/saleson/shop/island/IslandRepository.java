package saleson.shop.island;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import saleson.model.Island;


public interface IslandRepository extends JpaRepository<Island, Long>, QuerydslPredicateExecutor<Island> {
}
