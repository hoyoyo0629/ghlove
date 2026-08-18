package saleson.shop.cache;

import saleson.common.enumeration.cache.ReloadType;

public interface ShopCacheService {

    void removeCache(String... cacheNames);

    void sendFrontReload(ReloadType reloadType);

}
