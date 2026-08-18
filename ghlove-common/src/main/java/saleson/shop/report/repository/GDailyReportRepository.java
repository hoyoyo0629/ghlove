package saleson.shop.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import saleson.shop.report.entity.GDailyReport;

public interface GDailyReportRepository extends JpaRepository<GDailyReport, Long>, GDailyReportRepositoryCustom {


	
}
