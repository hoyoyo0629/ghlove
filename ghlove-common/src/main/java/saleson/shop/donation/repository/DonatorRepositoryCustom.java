package saleson.shop.donation.repository;

import saleson.shop.donation.dto.DonatorInfoDto;
import saleson.shop.donation.entity.OpUserDetailEntity;

public interface DonatorRepositoryCustom {

	DonatorInfoDto donatorInfo(Long userId);

	OpUserDetailEntity findByUserId(Long userId);

	Long sumCntrAmtBySecsnYearAndMberCi(String secsnYear, String mberCi);

	void updateKakaoBirthday(OpUserDetailEntity opUserDetailEntity);
}
