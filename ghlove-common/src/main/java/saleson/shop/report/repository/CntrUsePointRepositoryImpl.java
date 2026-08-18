package saleson.shop.report.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.model.QGCntrUsePoint;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CntrUsePointRepositoryImpl implements CntrUsePointRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Long countOrderCodeByPointUseDe(String searchDay) {
		QGCntrUsePoint usePoint = QGCntrUsePoint.gCntrUsePoint;

		return jpaQueryFactory
				.select(usePoint.orderCode.countDistinct())
				.from(usePoint)
				.where(usePoint.useSeCode.eq("1"), usePoint.pointUseDe.between(searchDay.substring(0, 4)+"0101", searchDay))
				.fetchOne();
	}

	@Override
	public Long countOrderCodeByPointUseDe(String startDay, String endDay) {
		QGCntrUsePoint usePoint = QGCntrUsePoint.gCntrUsePoint;

		return jpaQueryFactory
				.select(usePoint.orderCode.countDistinct())
				.from(usePoint)
				.where(usePoint.useSeCode.eq("1"), usePoint.pointUseDe.between(startDay, endDay))
				.fetchOne();
	}

	@Override
	public Long sumCntrUsePointByPointUseDe(String searchDay) {
		QGCntrUsePoint usePoint = QGCntrUsePoint.gCntrUsePoint;

		NumberExpression<Long> sumUsePoint =
				Expressions.cases()
				.when(usePoint.cntrUsePoint.sum().isNull()).then(0L)
				.otherwise(usePoint.cntrUsePoint.sum());

		return jpaQueryFactory
				.select(sumUsePoint)
				.from(usePoint)
				.where(usePoint.useSeCode.eq("1"), usePoint.pointUseDe.between(searchDay.substring(0, 4)+"0101", searchDay))
				.fetchOne();
	}

	@Override
	public Long sumCntrUsePointByPointUseDe(String startDay, String endDay) {
		QGCntrUsePoint usePoint = QGCntrUsePoint.gCntrUsePoint;

		NumberExpression<Long> sumUsePoint =
				Expressions.cases()
				.when(usePoint.cntrUsePoint.sum().isNull()).then(0L)
				.otherwise(usePoint.cntrUsePoint.sum());

		return jpaQueryFactory
				.select(sumUsePoint)
				.from(usePoint)
				.where(usePoint.useSeCode.eq("1"), usePoint.pointUseDe.between(startDay, endDay))
				.fetchOne();
	}

}
