package saleson.api.display;

import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.web.pagination.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.display.domain.ItemList;
import saleson.api.display.support.DisplayDataSupport;
import saleson.api.item.support.ItemDataSupport;
import saleson.common.Const;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Group;
import saleson.shop.categories.domain.Team;
import saleson.shop.config.domain.Config;
import saleson.shop.display.DisplayService;
import saleson.shop.display.domain.Display;
import saleson.shop.display.domain.DisplayImage;
import saleson.shop.display.domain.DisplayItem;
import saleson.shop.display.support.DisplayItemParam;
import saleson.shop.display.support.DisplayParam;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.support.ItemParam;
import saleson.shop.ranking.RankingService;
import saleson.shop.ranking.support.RankingParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController("ApiDisplayController")
@RequestMapping("/api/display")
public class DisplayController {
    private static Logger log = LoggerFactory.getLogger(DisplayController.class);

    @Autowired
    private RankingService rankingService;

    @Autowired
    SequenceService sequenceService;

    @Autowired
    private DisplayService displayService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private DisplayDataSupport displayDataSupport;

    @Autowired
    private ItemDataSupport itemDataSupport;

    /**
     * 베스트 상품 리스트 조회 API
     *
     * @param rankingParam
     * @return
     */
    @GetMapping("/best")
    public ResponseEntity bestList(RankingParam rankingParam) {
        ResponseEntity result = null;
        List<Item> list = null;

        if (rankingParam == null) {
            rankingParam = new RankingParam();
            rankingParam.setViewTarget("WEB");
            rankingParam.setLimit(100);
        }

        rankingParam.setRankingCode("TOP_100");

        try {
            list = rankingService.getRankingListForFront(rankingParam);
            result = ApiResponseEntity.data().list(itemDataSupport.resultItemListInfo(list)).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * MD 추천 상품 리스트 API
     *
     * @param limit
     * @param displayItemParam
     * @return
     */
    @GetMapping("/md")
    public ResponseEntity mdItems(@RequestParam(name = "limit", defaultValue = "0") int limit, DisplayItemParam displayItemParam) {
        ResponseEntity result = null;

        try {
            displayItemParam.setDisplayGroupCode("md");
            result = ApiResponseEntity.data().list(getItemList(limit, displayItemParam)).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 신상품 리스트 API
     *
     * @param limit
     * @param displayItemParam
     * @return
     */
    @GetMapping("/new")
    public ResponseEntity newItems(@RequestParam(name = "limit", defaultValue = "0") int limit, DisplayItemParam displayItemParam) {
        ResponseEntity result = null;

        try {
            displayItemParam.setDisplayGroupCode("new");

            Pagination pagination = Pagination.getInstance(displayService.getDisplayItemListCountByParam(displayItemParam), displayItemParam.getItemsPerPage());
            displayItemParam.setPagination(pagination);

            result = ApiResponseEntity.data()
                    .list(getItemList(limit, displayItemParam))
                    .pagination(pagination)
                    .put("displaySubCodeCount", displayService.getDisplayItemSubCodeCountByGroupCode("new")).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 그룹별 베스트 상품 리스트 API
     * @param limit
     * @return
     */
    @GetMapping("/group-best")
    public ResponseEntity groupBest(@RequestParam(name="limit", defaultValue = "10") int limit) {
        ResponseEntity result = null;

        try {

            DisplayItemParam displayItemParam = new DisplayItemParam();
            displayItemParam.setDisplayGroupCode("best");

            if (limit <= 0) {
                limit = 10;
            }

            log.debug("[Cache] categoriesService.getCategoriesForFront");
            List<Team> categories = categoriesService.getCategoriesForFront();

            List<Group> shopCategoryGroups = new ArrayList<>();
            for (Team team : categories) {
                if (!Config.SHOP_CATEGORY_GROUP_KEY.equals(team.getUrl())) {
                    continue;
                }
                shopCategoryGroups.addAll(team.getGroups());
            }

            List<String> codes = new ArrayList<>();

            for (Group group : shopCategoryGroups) {
                codes.add(group.getUrl());
            }

            if (codes.isEmpty()) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST, "카테고리 그룹 정보가 없습니다.");
            }

            List<DisplayItem> displayItems = getDisplayItems("", 0, displayItemParam);

            displayItemParam.setDisplaySubCodes(codes);
            List<Map> groupBestItems = new ArrayList<>();

            for (String code : codes) {
                List<ItemList> items = new ArrayList<>();
                int index = 0;
                for (DisplayItem i : displayItems) {
                    if (code.equals(i.getDisplaySubCode()) && index < limit) {
                        items.add(new ItemList(i));
                        index++;

                        if (index == limit) {
                            break;
                        }
                    }
                }

                Map<String, Object> map = new LinkedHashMap<>();
                map.put("url", code);
                map.put("items", items);

                groupBestItems.add(map);
            }

            result = ApiResponseEntity.data().list(groupBestItems).ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 최근 본 상품 리스트
     *
     * @param ids
     * @param limit
     * @return
     */
    @GetMapping("/lately")
    public ResponseEntity latelyItems(@RequestParam(name = "ids", defaultValue = "") String ids,
                                      @RequestParam(name = "limit", defaultValue = "4") int limit) {

        ResponseEntity result = null;

        try {

            List<Item> todayItems = new ArrayList<>();

            if (!ObjectUtils.isEmpty(ids)) {

                try {
                    ItemParam itemParam = new ItemParam();
                    Pagination pagination = Pagination.getInstance(limit, limit);
                    itemParam.setPagination(pagination);

                    itemParam.setTodayItemIds(ids);
                    itemParam.setDisplayFlag("Y");
                    todayItems = itemService.getTodayItemList(itemParam);

                } catch (DataAccessException e) {
                    log.error("lately Items error", e);
                    return ApiResponseEntity.error(ApiError.BAD_REQUEST);
                }

            }

            result = ApiResponseEntity.data().list(displayDataSupport.getItemListByItem(todayItems)).ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }


    private List<ItemList> getItemList(int limit, DisplayItemParam displayItemParam) {

        List<DisplayItem> displayItems = getDisplayItems("", limit, displayItemParam);

        return displayDataSupport.getItemListByDisplayItem(displayItems);
    }

    private List<DisplayItem> getDisplayItems(String displaySubCode, int limit, DisplayItemParam displayItemParam) {

        displayItemParam.setConditionType("FRONT");
        displayItemParam.setDisplaySubCode(displaySubCode);

        if (limit > 0) {
            displayItemParam.setLimit(limit);
        }

        return displayService.getFrontDisplayItemListByParam(displayItemParam);
    }

    /**
     * 프로모션 리스트 API
     *
     * @return
     */
    @GetMapping("/promotion")
    public ResponseEntity promotion() {
        ResponseEntity result = null;
        Display promotion = null;
        Display featured = null;
        try {
            promotion = displayService.getDisplayByGroupCode("front-promotion", "ALL");
            featured = displayService.getDisplayByGroupCode("front-featured", "ALL");

            result = ApiResponseEntity.data().put("promotion", promotion).put("featured", featured).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/mobile-promotion")
    public ResponseEntity mobilePromotion() {
        ResponseEntity result = null;
        Display promotion = null;
        try {
            promotion = displayService.getDisplayByGroupCode("mobile-promotion", "ALL");
            result = ApiResponseEntity.data().put("promotion", promotion).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/style-book")
    public ResponseEntity styleBook() {
        ResponseEntity result = null;
        try {

            List<String> groupCodes = new ArrayList<>();

            groupCodes.add("style-book-1");
            groupCodes.add("style-book-2");
            groupCodes.add("style-book-3");
            groupCodes.add("style-book-4");
            groupCodes.add("style-book-5");
            groupCodes.add("style-book-6");
            groupCodes.add("style-book-7");

            DisplayParam displayParam = new DisplayParam();
            displayParam.setViewTarget("ALL");
            displayParam.setDisplayGroupCodes(groupCodes);

            List<DisplayImage> images = displayService.getDisplayImageListByParam(displayParam);

            result = ApiResponseEntity.data().list(images).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }
}
