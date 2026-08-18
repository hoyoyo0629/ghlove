package saleson.shop.donation.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import saleson.shop.donation.entity.GAdmLocgovEntity;
import saleson.shop.donation.entity.GCntrLmttEntity;
import saleson.shop.donation.entity.GInsttCodeEntity;
import saleson.shop.donation.entity.GLocgovEntity;

@RequiredArgsConstructor
@Repository
public class LocgovRepositoryImpl implements LocgovRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public GInsttCodeEntity findInsttByLocgovCode(String locgovCode) {
//		QGInsttCodeEntity insttCode = QGInsttCodeEntity.gInsttCodeEntity;
//
//		return jpaQueryFactory.selectFrom(insttCode).where(insttCode.locgovCode.eq(locgovCode)).fetchOne();
		
		return null;
	}

	@Override
	public GAdmLocgovEntity findAdmLocgovByAdmCd(String locgovCode) {
//		QGAdmLocgovEntity admLocgov = QGAdmLocgovEntity.gAdmLocgovEntity;
//		return jpaQueryFactory.selectFrom(admLocgov).where(admLocgov.admCd.like(locgovCode+"%")).fetchOne();
		return null;
	}

	@Override
	public GLocgovEntity findByLocgovCode(String locgovCode) {
//		QGLocgovEntity locgovEntity = QGLocgovEntity.gLocgovEntity;
//		return jpaQueryFactory.selectFrom(locgovEntity).where(locgovEntity.locgovCode.eq(locgovCode)).fetchOne();
		return null;
	}

	@Override
	public GCntrLmttEntity findCntrLmttByLmttBgnDeAndLmttEndDe(String locgovCode, String lmttBgnDe, String lmttEndDe) {
//		QGCntrLmttEntity cntrLmtt = QGCntrLmttEntity.gCntrLmttEntity;
//		return jpaQueryFactory.selectFrom(cntrLmtt).where(cntrLmtt.locgovCode.eq(locgovCode), cntrLmtt.lmttBgnDe.loe(lmttBgnDe), cntrLmtt.lmttEndDe.goe(lmttEndDe)).fetchOne();
		return null;
	}
}
