package saleson.shop.cache;

import com.onlinepowers.framework.repository.support.EarlyLoadingCodeInfoRepository;
import com.onlinepowers.framework.repository.support.EarlyLoadingMessageInfoRepository;
import com.onlinepowers.framework.repository.support.EarlyLoadingRepositoryEvent;
import com.onlinepowers.framework.util.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.cache.ReloadType;

import java.util.Arrays;

@Service("shopCacheService")
public class ShopCacheServiceImpl extends EgovAbstractServiceImpl implements ShopCacheService{

    private static final Logger log = LoggerFactory.getLogger(ShopCacheServiceImpl.class);

    @Autowired
    private EarlyLoadingCodeInfoRepository codeInfoRepository;

    @Autowired
    private EarlyLoadingMessageInfoRepository messageInfoRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RestTemplate customRestTemplate;

    @Override
    public void removeCache(String... cacheNames) {

        if (cacheNames != null && cacheNames.length > 0) {
            Arrays.stream(cacheNames).forEach(s -> {
                removeCacheByName(s);
            });
        }
    }

    private void removeCacheByName(String name) {
        try {
            if (!StringUtils.isEmpty(name)) {
                switch (name) {
                    case "common-code" :
                        EarlyLoadingRepositoryEvent codeReloadEvent = new EarlyLoadingRepositoryEvent("codeInfoRepository",EarlyLoadingRepositoryEvent.Action.ReloadAll);
                        codeInfoRepository.onApplicationEvent(codeReloadEvent);
                        break;
                    case "common-message" :
                        EarlyLoadingRepositoryEvent messageReloadEvent = new EarlyLoadingRepositoryEvent("messageInfoRepository",EarlyLoadingRepositoryEvent.Action.ReloadAll);
                        messageInfoRepository.onApplicationEvent(messageReloadEvent);
                        break;
                    case "shop-config" :
                        evictAllCache("shopConfig");
                        break;
                    case "front-categories" :
                        evictAllCache("frontCategories");
                        break;
                    case "fixed-meta-data" :
                        evictAllCache("fixedMetaData");
                        break;
                    default:
                }
            }
        } catch (RuntimeException ignore) {
//            log.error("removeCache error {}",ignore.getMessage(), ignore);
            log.error("removeCache error {}", getClass().getName() + " :: removeCacheByName RuntimeException =============");
        }

    }

    private String getCode(ReloadType reloadType) {

        if (reloadType != null && StringUtils.hasText(reloadType.getCode())) {
            return reloadType.getCode();
        }

        return "";
    }

    void evictAllCache(String name) {
        cacheManager.getCache(name).clear();
    }

    @Override
    public void sendFrontReload(ReloadType reloadType) {

        String code = getCode(reloadType);
        try {

            if (!StringUtils.isEmpty(code)) {

                String uri = SalesonProperty.getSalesonUrlShoppingmall() + "/reload-cache/"+code;

                if ("api".equals(SalesonProperty.getSalesonViewType())) {
                    uri = SalesonProperty.getSalesonUrlApi() + "/api/reload-cache/"+code;
                }

                log.debug("sendFrontReload uri [{}]", uri);
                customRestTemplate.getForObject(uri, String.class);

            }
        } catch (RuntimeException ignore) {
//            log.error("sendFrontReload error [{}]{}",code, ignore.getMessage(), ignore);
            log.error("sendFrontReload error [{}]{}", getClass().getName() + " :: sendFrontReload RuntimeException =============", ignore);
        }

    }

}
