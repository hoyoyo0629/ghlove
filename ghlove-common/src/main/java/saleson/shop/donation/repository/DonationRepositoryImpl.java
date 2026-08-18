package saleson.shop.donation.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.shop.donation.entity.GCntrEntity;
import saleson.shop.donation.entity.GCtbnySetupEntity;
import saleson.shop.donation.entity.QGCntrEntity;
import saleson.shop.donation.entity.QGCtbnySetupEntity;

@Slf4j
@RequiredArgsConstructor
@Repository
public class DonationRepositoryImpl implements DonationRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public GCtbnySetupEntity findByStdrYearAndLocgovCode(String stdrYear, String locgovCode) {
		QGCtbnySetupEntity gCtbnySetupEntity = QGCtbnySetupEntity.gCtbnySetupEntity;
		return jpaQueryFactory.selectFrom(gCtbnySetupEntity).where(gCtbnySetupEntity.stdrYear.eq(stdrYear), gCtbnySetupEntity.locgovCode.eq(locgovCode)).fetchOne();
	}

	@Override
	public Long sumCntrAmtByUserIdAndCntrDe(String year, Long userId) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;
		return jpaQueryFactory
				.select(gCntr.cntrAmt.sum())
				.from(gCntr)
				.where(gCntr.userId.eq(userId), gCntr.cntrDe.substring(0, 4).eq(year), gCntr.cntrSttusCode.eq("200"), gCntr.deleteAt.eq("N"))
				.fetchOne();
	}

	@Override
	public Long countByCntrDe(String searchDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;
		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						)
				.fetchOne();
	}
	@Override
	public Long countByCntrDe(String startDay, String endDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;
		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						)
				.fetchOne();
	}

	@Override
	public Long countByCntrDeAndCurrentDate(String searchDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;
		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.eq(searchDay)
						)
				.fetchOne();
	}

	@Override
	public Long countByCntrDeAndCurrentDate(String startDay,String endDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;
		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						)
				.fetchOne();
	}

	@Override
	public Long countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(String searchDay, String cntrPathCode) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						, gCntr.cntrPathCode.eq(cntrPathCode)
						, gCntr.dsgnDntnBizId.eq((long) 0)
						)
				.fetchOne();
	}

	@Override
	public Long countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(String startDay, String endDay, String cntrPathCode) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						, gCntr.cntrPathCode.eq(cntrPathCode)
						, gCntr.dsgnDntnBizId.eq((long) 0)
						)
				.fetchOne();
	}

	@Override
	public Long countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNotNull(String searchDay, String cntrPathCode) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						, gCntr.cntrPathCode.eq(cntrPathCode)
						, gCntr.dsgnDntnBizId.ne((long) 0)
						)
				.fetchOne();
	}

	@Override
	public Long countByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNotNull(String startDay, String endDay, String cntrPathCode) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						, gCntr.cntrPathCode.eq(cntrPathCode)
						, gCntr.dsgnDntnBizId.ne((long) 0)
						)
				.fetchOne();
	}

	@Override
	public Long sumCntrAmtByCntrDe(String searchDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						)
				.fetchOne();

	}

	@Override
	public Long sumCntrAmtByCntrDe(String startDay, String endDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						)
				.fetchOne();

	}

	@Override
	public Long sumCntrAmtByCntrDeAndCurrentDate(String searchDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.eq(searchDay)
						)
				.fetchOne();

	}

	@Override
	public Long sumCntrAmtByCntrDeAndCurrentDate(String startDay, String endDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						)
				.fetchOne();

	}

	@Override
	public Long sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(String searchDay, String cntrPathCode) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;
		NumberExpression<Long> sumCntrAmt =
			Expressions.cases()
			.when(gCntr.cntrAmt.sum().isNull()).then(0L)
			.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						, gCntr.cntrPathCode.eq(cntrPathCode)
						, gCntr.dsgnDntnBizId.eq((long) 0)
						)
				.fetchOne();
	}

	@Override
	public Long sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdIsNull(String startDay, String endDay, String cntrPathCode) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;
		NumberExpression<Long> sumCntrAmt =
			Expressions.cases()
			.when(gCntr.cntrAmt.sum().isNull()).then(0L)
			.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						, gCntr.cntrPathCode.eq(cntrPathCode)
						, gCntr.dsgnDntnBizId.eq((long) 0)
						)
				.fetchOne();
	}

	@Override
	public Long sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdNotNull(String searchDay, String cntrPathCode) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						, gCntr.cntrPathCode.eq(cntrPathCode)
						, gCntr.dsgnDntnBizId.ne((long) 0)
						)
				.fetchOne();
	}

	@Override
	public Long sumCntrAmtByCntrDeAndCntrPathCodeAndDsgnDntnBizIdNotNull(String startDay, String endDay, String cntrPathCode) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						, gCntr.cntrPathCode.eq(cntrPathCode)
						, gCntr.dsgnDntnBizId.ne((long) 0)
						)
				.fetchOne();
	}

	@Override
	public Long countByCntrDeAndLinkInsttCd(String searchDay, String linkInsttCd) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						,gCntr.linkInsttCd.eq(linkInsttCd)
						)
				.fetchOne();
	}


	@Override
	public Long sumCntrAmtByCntrDeAndLinkInsttCd(String searchDay, String linkInsttCd) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						,gCntr.linkInsttCd.eq(linkInsttCd)
						)
				.fetchOne();
	}

	@Override
	public Long countByCntrDeAndLinkInsttCdAndCurrentDate(String searchDay, String linkInsttCd) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.eq(searchDay)
						,gCntr.linkInsttCd.eq(linkInsttCd)
						)
				.fetchOne();
	}
	@Override
	public Long countByCntrDeAndLinkInsttCdAndCurrentDate(String searchDay, String endDay, String linkInsttCd) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay,endDay)
						,gCntr.linkInsttCd.eq(linkInsttCd)
						)
				.fetchOne();
	}

	@Override
	public Long sumCntrAmtByCntrDeAndLinkInsttCdAndCurrentDate(String searchDay, String linkInsttCd) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.eq(searchDay)
						,gCntr.linkInsttCd.eq(linkInsttCd)
						)
				.fetchOne();
	}
	@Override
	public Long sumCntrAmtByCntrDeAndLinkInsttCdAndCurrentDate(String searchDay, String endDay, String linkInsttCd) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay, endDay)
						,gCntr.linkInsttCd.eq(linkInsttCd)
						)
				.fetchOne();
	}

	@Override
	public GCntrEntity findByElctrnPayNoAndUserId(String elctrnPayNo, Long userId) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		return jpaQueryFactory
				.select(gCntr)
				.from(gCntr)
				.where(gCntr.elctrnPayNo.eq(elctrnPayNo), gCntr.userId.eq(userId))
				.fetchOne();
	}

	@Override
	public Integer sumWegiveCntrAmtByWegiveAmtAndMberCi(String mberCi) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Long countByCntrDeAndNotLinkInstt(String searchDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;
		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						,gCntr.linkInsttCd.isNull()
						)
				.fetchOne();
	}

	@Override
	public Long countByCntrDeAndNotLinkInstt(String startDay, String endDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;
		return jpaQueryFactory
				.select(gCntr.count())
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						,gCntr.linkInsttCd.isNull()
						)
				.fetchOne();
	}

	@Override
	public Long sumCntrAmtByCntrDeAndNotLinkInstt(String searchDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(searchDay.substring(0, 4)+"0101", searchDay)
						,gCntr.linkInsttCd.isNull()
						)
				.fetchOne();

	}@Override

	public Long sumCntrAmtByCntrDeAndNotLinkInstt(String startDay, String endDay) {
		QGCntrEntity gCntr = QGCntrEntity.gCntrEntity;

		NumberExpression<Long> sumCntrAmt =
				Expressions.cases()
				.when(gCntr.cntrAmt.sum().isNull()).then(0L)
				.otherwise(gCntr.cntrAmt.sum());

		return jpaQueryFactory
				.select(sumCntrAmt)
				.from(gCntr)
				.where(
						gCntr.cntrSttusCode.eq("200")
						, gCntr.cntrDe.between(startDay, endDay)
						,gCntr.linkInsttCd.isNull()
						)
				.fetchOne();

	}
}
