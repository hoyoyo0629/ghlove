package saleson.api.store;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.CodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.enumeration.StoreType;
import saleson.model.Store;
import saleson.shop.store.StoreService;
import saleson.shop.store.support.StoreDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("ApiStoreController")
@RequestMapping("/api/store")
public class StoreController {

    private static final Logger log = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private StoreService storeService;

    /**
     * 판매처 목록 조회
     * @param storeDto
     * @param pageable
     * @return
     */
    @GetMapping("")
    public ResponseEntity list(StoreDto storeDto,
                               @PageableDefault(sort="id", direction= Sort.Direction.DESC) Pageable pageable) {
        ResponseEntity result = null;
        Page<Store> content = null;

        try {
            content = storeService.findAll(storeDto.getPredicate(), pageable);
            result = ApiResponseEntity.data().put("content", content).ok();
        } catch (OpRuntimeException e) {
            //log.error("[/api/store] ERROR : {}", e.getMessage(), e);
        	log.error("ERROR-48: 판매처 목록 return 실패", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 판매처 검색 정보 세팅
     * @return
     */
    @GetMapping("search-info")
    public ResponseEntity searchInfo() {
        ResponseEntity result = null;

        try {
            Map<String, Object> info = new HashMap<>();
            info.put("sidoList", CodeUtils.getCodeList("SIDO_LIST"));

            List<Object> storeTypes = new ArrayList<>();
            HashMap<String, Object> tempMap = new HashMap<>();
            for (StoreType storeType : StoreType.values()) {
                tempMap.put("code", storeType.getCode());
                tempMap.put("title", storeType.getTitle());
                tempMap.put("description", storeType.getDescription());

                storeTypes.add(tempMap);
            }

            info.put("storeTypes", storeTypes);

            result = ApiResponseEntity.data().put("searchInfo", info).ok();
        } catch (OpRuntimeException e) {
            //log.error("[/api/store/info] ERROR : {}", e.getMessage(), e);
            log.error("ERROR-49: 판매처 검색정보 return 실패", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }
}
