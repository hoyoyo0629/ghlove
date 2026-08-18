package saleson.api.category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.item.support.ItemDataSupport;
import saleson.api.notice.NoticeController;
import saleson.common.utils.UserUtils;
import saleson.model.FilterGroup;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Categories;
import saleson.shop.categories.support.CategoryParam;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.api.ItemInfo;
import saleson.shop.wishlist.WishlistService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController("ApiCategoriesController")
@RequestMapping("/api/categories")
public class CategoriesController {

    private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private CategoriesService categoriesService;

//    @Autowired
//    private ItemService itemService;

    @Autowired
    private ItemDataSupport itemDataSupport;
    
    @Autowired
    private WishlistService wishlistService;
    

    /**
     * 카테고리 조회 API
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/searchResult")
    public ResponseEntity<Map<String, Object>> searchResult(CategoryParam categoryParam) {
        ResponseEntity<Map<String, Object>> result = null;
        try {
        	if (!StringUtils.isEmpty(categoryParam.getCategory())) {
        		Categories categories = categoriesService.getCategoryLevelClassByCategoryParam(categoryParam);
        		if (categories == null) {
        			throw new RuntimeException();
        		}
        		categoryParam.setCategoryLevel(categories.getCategoryLevel());
        		categoryParam.setCategoryClass1(categories.getCategoryClass1());
        		categoryParam.setCategoryClass2(categories.getCategoryClass2());
        		categoryParam.setCategoryClass3(categories.getCategoryClass3());
        		categoryParam.setCategoryClass4(categories.getCategoryClass4());
        	}
        	
        	Pagination pagination = Pagination.getInstance(categoriesService.getItemListCountByCategoryParam(categoryParam), categoryParam.getItemsPerPage());
        	if (categoryParam.getItemsPerPage() < 12) {
        		pagination.setItemsPerPage(12);
        	} else {
        		pagination.setItemsPerPage(categoryParam.getItemsPerPage());
        	}
        	categoryParam.setPagination(pagination);
        	List<Item> searchList = categoriesService.getItemListByCategoryParam(categoryParam);
        	
        	wishlistService.setWishlistFlagByItem(searchList);
        	
        	result = ApiResponseEntity.data().list(itemDataSupport.resultItemListInfo(searchList)).pagination(pagination).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

}
