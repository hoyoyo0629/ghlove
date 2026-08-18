package saleson.api.category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.OpRuntimeException;

import saleson.api.category.domain.TeamInfo;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.item.domain.ItemList;
import saleson.api.notice.NoticeController;
import saleson.common.utils.ItemUtils;
import saleson.common.utils.LocalDateUtils;
import saleson.model.FilterGroup;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Breadcrumb;
import saleson.shop.categories.domain.CategoryPath;
import saleson.shop.categories.domain.PriceArea;
import saleson.shop.categories.domain.Team;
import saleson.shop.categories.support.CategoryParam;
import saleson.shop.categoriesfilter.CategoriesFilterService;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.item.domain.Item;
import saleson.shop.ranking.RankingService;
import saleson.shop.ranking.support.RankingParam;


@RestController("ApiCategoryController")
@RequestMapping("/api/category")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    CategoriesFilterService categoriesFilterService;

    @Autowired
    ConfigService configService;

    private String getUpdatedDate() {
        Config config = configService.getShopConfig(Config.SHOP_CONFIG_ID);
        String updateDate = "";
        if (config != null) {
            updateDate = config.getCategoryUpdatedDate();
        }

        return updateDate;
    }

    @GetMapping("updated-check")
    public ResponseEntity updatedCheck(@RequestParam(name = "d") String d) {

        String key = "result";

        try {

            String updateDate = getUpdatedDate();

            if (ObjectUtils.isEmpty(d) || ObjectUtils.isEmpty(updateDate)) {
                return ApiResponseEntity.data().put(key, false).ok();
            }

            LocalDateTime dateTime1 = LocalDateUtils.getLocalDateTime(d);
            LocalDateTime dateTime2 = LocalDateUtils.getLocalDateTime(updateDate);

            return ApiResponseEntity.data().put(key, dateTime1.isBefore(dateTime2)).ok();
        } catch (OpRuntimeException e) {
            logger.error("CategoriesController checkUpdate", e);
            return ApiResponseEntity.data().put(key, false).ok();
        }

    }

    /**
     * 상품 카테고리 리스트
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity list() {
        ResponseEntity result = null;
        List<Team> categories = null;

        try {

            categories = categoriesService.getCategoriesForFront();
            result = ApiResponseEntity.data().put("updatedDate", getUpdatedDate()).list(categoriesDataSet(categories)).ok();
        } catch (OpRuntimeException e) {
            logger.error("CategoriesController list", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    private List<TeamInfo> categoriesDataSet(List<Team> categories) {
        List<TeamInfo> categoriesInfos = new ArrayList<>();

        if (categories != null && !categories.isEmpty()) {
            for (Team team : categories) {
                TeamInfo categoriesInfo = new TeamInfo(team);
                categoriesInfos.add(categoriesInfo);
            }
        }

        return categoriesInfos;
    }

    /**
     * 현재 카테고리 리스트
     *
     * @return
     */
    @GetMapping("/current")
    public ResponseEntity currentList(CategoryParam categoryParam) {
        ResponseEntity result = null;
        List<Breadcrumb> breadcrumbs = null;

        try {
            // 1. 현재 경로
            breadcrumbs = categoriesService.getBreadcrumbListByCategoryUrl(categoryParam.getCategoryCode());
            result = ApiResponseEntity.data().list(breadcrumbs).ok();
        } catch (OpRuntimeException e) {
            logger.error("CategoriesController currentList", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 카테고리 베스트 상품 조회 API
     *
     * @param rankingParam
     * @return
     */
    @GetMapping("/best")
    public ResponseEntity categoryBest(RankingParam rankingParam) {
        ResponseEntity result = null;
        List<Item> resultList = null;

        if (rankingParam == null) {
            rankingParam = new RankingParam();
        }

        rankingParam.setConditionType("FRONT_DISPLAY_ITEM");
        rankingParam.setPrivateTypes(ItemUtils.getPrivateTypes());

        try {
            resultList = rankingService.getRankingListForFront(rankingParam);
            result = ApiResponseEntity.data().list(resultCategoryItem(resultList)).ok();
        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    private List<ItemList> resultCategoryItem(List<Item> list) {
        List<ItemList> resultList = new ArrayList<>();
        int t_size = list.size();
        if (t_size > 10) t_size = 10;

        if (t_size > 0) {
            for (int i = 0; i < t_size; i++) {
                resultList.add(new ItemList(list.get(i)));
            }
        }

        return resultList;
    }

    /**
     * 카테고리 필터 조회 API
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/filter")
    public ResponseEntity categoryFilter(int categoryId) {
        ResponseEntity result = null;
        List<FilterGroup> resultList = null;
        try {
            resultList = categoriesFilterService.getBreadcrumbFilterGroupList(categoryId);
            result = ApiResponseEntity.data().list(resultList).ok();
        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 카테고리 상품 금액 리스트 조회
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/price-areas")
    public ResponseEntity getPriceAreaListById(int categoryId) {
        ResponseEntity result = null;
        List<PriceArea> resultList = null;
        try {
            resultList = categoriesService.getPriceAreaListById(categoryId);
            result = ApiResponseEntity.data().list(resultList).ok();
        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 카테고리 path 조회
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/category-path")
    public ResponseEntity getCategoryPath(CategoryParam categoryParam) {
        ResponseEntity result = null;
        List<CategoryPath> resultList = null;
        try {
            resultList = categoriesService.getCategoryPath(categoryParam);
            result = ApiResponseEntity.data().list(resultList).ok();
        } catch (OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

}
