package saleson.api.cache;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.shop.cache.ShopCacheService;

@RestController("ApiCacheController")
@RequestMapping("/api")
public class CacheController {

    @Autowired
    private ShopCacheService shopCacheService;

    @GetMapping("/reload-cache/{cacheName}")
    public ResponseEntity reloadCache(@PathVariable("cacheName") String cacheName) {
        shopCacheService.removeCache(cacheName);
        return ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
    }
}
