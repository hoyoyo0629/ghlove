package saleson.shop.givepointexpiration;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.givepointexpiration.domain.GivePointExpirationMail;
import saleson.shop.givepointexpiration.domain.GivePointExpirationTarget;

@Mapper("givePointExpiration")
public interface GivePointExpirationMapper {

	List<GivePointExpirationTarget> getCntrBlcePointList();

	void updateCntrBlcePointDel(List<GivePointExpirationTarget> list);

	void insertCntrUsePoint(List<GivePointExpirationTarget> list);

	List<GivePointExpirationMail> getGivePointExpirationMailInfoList();

	String getShopName(Integer shopConfigId);

}
