package saleson.api.speciality;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.item.support.ItemDataSupport;
import saleson.api.speciality.support.SpecialityDataSupport;
import saleson.api.speciality.support.SpecialityParam;
import saleson.common.utils.UserUtils;
import saleson.shop.item.ItemFrontService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.support.ItemParam;
import saleson.shop.specialityitem.SpecialityItemService;
import saleson.shop.specialityitem.domain.SpecialityFrontDomain;
import saleson.shop.specialityitem.support.SpecialityItemParam;
import saleson.shop.wishlist.WishlistService;

@RestController("SpecialityController")
@RequestMapping("/api/speciality")
public class SpecialityController {
	private static final Logger log = LoggerFactory.getLogger(SpecialityController.class);

	@Autowired
	private SpecialityItemService specialityItemService;

	@Autowired
	private SpecialityDataSupport specialityDataSupport;

	@Autowired
	private ItemDataSupport itemDataSupport;

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private ItemFrontService itemFrontService;

	/**
	 * 특산물 키워드 조회
	 * */
	@GetMapping("/search")
	public ResponseEntity<?> search(SpecialityParam param) {
		ResponseEntity<?> result = null;
		int listCount = 0;
		List<SpecialityFrontDomain> list = null;
		try {
            if (itemFrontService.isItemRestrict()) {
            	return ApiResponseEntity.error(ApiError.RESTRICT_ITEM);
            }
			SpecialityItemParam itemParam = new SpecialityItemParam();
			itemParam.setPage(param.getPage());
			itemParam.setLocgovCode(param.getLocgov());
			itemParam.setItemsPerPage(param.getItemsPerPage());
			listCount = specialityItemService.getSpecialityFrontListCount(itemParam);
			Pagination pagination = Pagination.getInstance(listCount, itemParam.getItemsPerPage());
			itemParam.setPagination(pagination);
			if (listCount > 0) {
				list = specialityItemService.getSpecialityFrontList(itemParam);
			}
			result = ApiResponseEntity.data().list(listCount > 0 ? specialityDataSupport.bindList(list) : null).pagination(pagination).ok();
		} catch (RuntimeException e) {
			log.error(getClass().getName() +  " :: select RuntimeException error =================");
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 특산물 상품 조회
	 * */
	@GetMapping("/search-item-list")
	public ResponseEntity<?> searchItemList(SpecialityParam param) {
		ResponseEntity<?> result = null;
		int specialityFrontListCount = 0;
		int itemCount = 0;
		List<SpecialityFrontDomain> list = null;
		List<Item> itemList = null;
		try {
            if (itemFrontService.isItemRestrict()) {
            	return ApiResponseEntity.error(ApiError.RESTRICT_ITEM);
            }
			if (param == null) {
				result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
			} else {
				SpecialityItemParam specialityItemParam = new SpecialityItemParam();
				specialityItemParam.setLocgovCode(param.getLocgov());
				specialityFrontListCount = specialityItemService.getSpecialityFrontListCount(specialityItemParam);
				specialityItemParam.setLimit(1);
				if (specialityFrontListCount > 0) {
					list = specialityItemService.getSpecialityFrontList(specialityItemParam);
				}

				ItemParam itemParam = new ItemParam();
				itemParam.setPage(param.getPage());
				itemParam.setPerPages(param.getItemsPerPage());
				itemParam.setLocgov(param.getLocgov());
				itemCount = itemFrontService.getItemCountBySpeciality(itemParam);
				Pagination pagination = Pagination.getInstance(itemCount, itemParam.getPerPages());

		        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
//		        pagination.setCurrentPage(1);
//		        itemParam.setPage(1);
		        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
				
				itemParam.setPagination(pagination);
				if (itemCount > 0) {
					itemList = itemFrontService.getItemListBySpeciality(itemParam);
					wishlistService.setWishlistFlagByItem(itemList);
				}

				result = ApiResponseEntity.data().list(itemCount > 0 ? itemDataSupport.resultItemListInfo(itemList) : null).pagination(pagination).put("adultYn", UserUtils.isAdult() ? "Y" : "N").put("specialityFrontDomain", specialityFrontListCount > 0 ? list.get(0) : null).ok();
			}
		} catch (RuntimeException e) {
			log.error(getClass().getName() +  " :: search-item-list RuntimeException error =================", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}
}
