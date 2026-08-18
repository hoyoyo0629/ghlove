package saleson.shop.report.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.model.QGCntrUsePoint;

@Slf4j
@RequiredArgsConstructor
@Repository
public class GDailyReportRepositoryImpl implements GDailyReportRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;



}
