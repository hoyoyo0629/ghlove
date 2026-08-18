package saleson.shop.donation.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import saleson.shop.donation.dto.DonatorInfoDto;
import saleson.shop.donation.entity.OpUserDetailEntity;
import saleson.shop.donation.entity.QOpUserDetailEntity;
import saleson.shop.donation.entity.QOpUserEntity;

@RequiredArgsConstructor
@Repository
public class DonatorRepositoryImpl implements DonatorRepositoryCustom{

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public DonatorInfoDto donatorInfo(Long userId) {
		QOpUserEntity opUser = QOpUserEntity.opUserEntity;
		QOpUserDetailEntity opUserDetail = QOpUserDetailEntity.opUserDetailEntity;
		return null;/*jpaQueryFactory
				.select(
						new QDonatorInfoDto(opUser.userId
								, opUserDetail.address
								, opUserDetail.addressDetail
								, opUserDetail.phoneNumber
								, opUserDetail.birthday
								, opUserDetail.birthdayType
								, opUser.loginPathCode
								, opUser.email
								, opUser.userName
								, opUser.loginId
								, opUser.mberCi
								, new CaseBuilder()
								.when(opUser.mberCi.isNull())
								.then(false)
								.otherwise(true).as("isMberCi")
						))
				.from(opUser)
				.innerJoin(opUserDetail).on(opUser.userId.eq(opUserDetail.userId))
				.where(opUser.userId.eq(userId)).fetchOne();*/
	}

	@Override
	public OpUserDetailEntity findByUserId(Long userId) {
		QOpUserDetailEntity opUserDetail = QOpUserDetailEntity.opUserDetailEntity;
		return jpaQueryFactory.selectFrom(opUserDetail).where(opUserDetail.userId.eq(userId)).fetchOne();
	}


	@Override
	public Long sumCntrAmtBySecsnYearAndMberCi(String secsnYear, String mberCi) {
		/*QGmberSecsnEntity gmberSecsn = QGmberSecsnEntity.gmberSecsnEntity;
		return jpaQueryFactory
				.select(gmberSecsn.cntrAmt.sum())
				.from(gmberSecsn)
				.where(gmberSecsn.secsnYear.eq(secsnYear), gmberSecsn.mberCi.eq(mberCi))
				.fetchOne();*/
		return 0L;
	}

	@Override
	public void updateKakaoBirthday(OpUserDetailEntity opUserDetailEntity) {
		QOpUserDetailEntity opUserDetail = QOpUserDetailEntity.opUserDetailEntity;

		jpaQueryFactory.update(opUserDetail).set(opUserDetail.birthday, opUserDetailEntity.getBirthday()).set(opUserDetail.birthdayType, "1").where(opUserDetail.userId.eq(opUserDetailEntity.getUserId()));

	}




}
