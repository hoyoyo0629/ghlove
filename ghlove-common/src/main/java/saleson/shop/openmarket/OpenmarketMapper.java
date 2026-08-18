package saleson.shop.openmarket;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import saleson.shop.openmarket.domain.SyncNaverItem;

import java.util.List;

@Mapper
public interface OpenmarketMapper {

    List<SyncNaverItem> getSyncNaverItems(List<String>itemUserCodes);

}
