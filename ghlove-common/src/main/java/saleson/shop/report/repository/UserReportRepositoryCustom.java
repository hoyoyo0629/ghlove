package saleson.shop.report.repository;

public interface UserReportRepositoryCustom {

	long countByStatusCodeAndCreatedDate(Integer statusCode);
	long countByStatusCodeAndCreatedDate(Integer statusCode, String createdDate);
	long countByStatusCodeAndCreatedDate(Integer statusCode, String startDay, String endDay);
}
