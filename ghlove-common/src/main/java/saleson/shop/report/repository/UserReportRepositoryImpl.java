package saleson.shop.report.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import saleson.shop.donation.entity.QOpUserEntity;

@RequiredArgsConstructor
@Repository
public class UserReportRepositoryImpl implements UserReportRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public long countByStatusCodeAndCreatedDate(Integer statusCode, String createdDate) {
		QOpUserEntity userEntity = QOpUserEntity.opUserEntity;

		return jpaQueryFactory.select(userEntity.count())
				.from(userEntity)
				.where(userEntity.statusCode.eq(statusCode), userEntity.createdDate.loe(createdDate+"235959"))
				.fetchOne();
	}

	@Override
	public long countByStatusCodeAndCreatedDate(Integer statusCode, String startDay, String endDay) {
		QOpUserEntity userEntity = QOpUserEntity.opUserEntity;

		return jpaQueryFactory.select(userEntity.count())
				.from(userEntity)
				.where(userEntity.statusCode.eq(statusCode), userEntity.createdDate.between(startDay+"000000", endDay+"235959"))
				.fetchOne();
	}

	@Override
	public long countByStatusCodeAndCreatedDate(Integer statusCode) {
		QOpUserEntity userEntity = QOpUserEntity.opUserEntity;

		return jpaQueryFactory.select(userEntity.count())
				.from(userEntity)
				.where(userEntity.statusCode.eq(statusCode))
				.fetchOne();
	}

}
