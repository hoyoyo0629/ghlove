package saleson.shop.donation.repository;

import saleson.shop.donation.entity.GAdmLocgovEntity;
import saleson.shop.donation.entity.GCntrLmttEntity;
import saleson.shop.donation.entity.GInsttCodeEntity;
import saleson.shop.donation.entity.GLocgovEntity;

public interface LocgovRepositoryCustom {

	GInsttCodeEntity findInsttByLocgovCode(String locgovCode);
	GAdmLocgovEntity findAdmLocgovByAdmCd(String locgovCode);
	GLocgovEntity findByLocgovCode(String locgovCode);
	GCntrLmttEntity findCntrLmttByLmttBgnDeAndLmttEndDe(String locgovCode, String lmttBgnDe, String lmttEndDe);

}
