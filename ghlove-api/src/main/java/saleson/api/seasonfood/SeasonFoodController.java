package saleson.api.seasonfood;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.item.support.ItemDataSupport;
import saleson.api.seasonfood.support.SeasonFoodDataSupport;
import saleson.api.seasonfood.support.SeasonFoodParam;
import saleson.common.utils.UserUtils;
import saleson.shop.item.ItemFrontService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.support.ItemParam;
import saleson.shop.seasonalfood.SeasonalFoodService;
import saleson.shop.seasonalfood.domain.SeasonalFood;
import saleson.shop.wishlist.WishlistService;

@RestController("SeasonFoodController")
@RequestMapping("/api/season-food")
public class SeasonFoodController {

	private static final Logger log = LoggerFactory.getLogger(SeasonFoodController.class);

	@Autowired
	private SeasonalFoodService seasonalFoodService;

	@Autowired
	private SeasonFoodDataSupport seasonFoodDataSupport;

	@Autowired
	private ItemDataSupport itemDataSupport;

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private ItemFrontService itemFrontService;

	/**
	 * 제철식품관 키워드 조회
	 * */
	@GetMapping("/search")
	public ResponseEntity<?> search() {
		ResponseEntity<?> result = null;
		try {
            
            if (itemFrontService.isItemRestrict()) {
            	return ApiResponseEntity.error(ApiError.RESTRICT_ITEM);
            }
			List<SeasonalFood> list = seasonalFoodService.getFrontSeasonFood();
			result = ApiResponseEntity.data().list(seasonFoodDataSupport.bindList(list)).ok();
		} catch (RuntimeException e) {
			log.error(getClass().getName() +  " :: select RuntimeException error =================");
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 제철식품관 상품 조회
	 * */
	@GetMapping("/search-season-detail")
	public ResponseEntity<?> searchSeasonDetail(SeasonFoodParam param, RequestContext requestContext) {
		ResponseEntity<?> result = null;

		try {
            
            if (itemFrontService.isItemRestrict()) {
            	return ApiResponseEntity.error(ApiError.RESTRICT_ITEM);
            }
			if (param == null) {
				result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
			} else {
				ItemParam itemParam = new ItemParam();
				
				itemParam.setPage(param.getPage());
				
				itemParam.setPerPages(4);
				itemParam.setMonth(param.getMonth());
				itemParam.setLocgov(param.getLocgov());
				Pagination pagination = Pagination.getInstance(itemFrontService.getItemCountBySeason(itemParam), itemParam.getPerPages());
		        
		        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
//		        pagination.setCurrentPage(1);
//		        itemParam.setPage(1);
		        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
		        
				itemParam.setPagination(pagination);
		        
				List<Item> list = itemFrontService.getItemListBySeason(itemParam);
				wishlistService.setWishlistFlagByItem(list);

				result = ApiResponseEntity.data().list(itemDataSupport.resultItemListInfo(list)).pagination(pagination).put("adultYn", UserUtils.isAdult() ? "Y" : "N").ok();
			}
		} catch (RuntimeException e) {
			log.error(getClass().getName() +  " :: searchSeasonalFoodItem RuntimeException error =================");
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}


}
