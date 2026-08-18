package saleson.shop.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import saleson.shop.donation.entity.OpUserEntity;

public interface UserReportRepository extends JpaRepository<OpUserEntity, Long>, UserReportRepositoryCustom {

}
