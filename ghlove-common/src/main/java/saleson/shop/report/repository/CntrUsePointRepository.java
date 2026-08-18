package saleson.shop.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import saleson.model.GCntrUsePoint;
import saleson.shop.report.entity.OpCall;

public interface CntrUsePointRepository extends JpaRepository<GCntrUsePoint, String>, CntrUsePointRepositoryCustom {


}
