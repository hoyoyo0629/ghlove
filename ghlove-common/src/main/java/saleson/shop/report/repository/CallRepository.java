package saleson.shop.report.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import saleson.shop.report.entity.OpCall;

public interface CallRepository extends JpaRepository<OpCall, String>, CallRepositoryCustom {

	OpCall findByCallDate(String callDate);

	List<OpCall> findByCallDateBetween(String startDay, String endDay);

}
