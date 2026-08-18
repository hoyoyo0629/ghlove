package saleson.api.search;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.pagination.Pagination;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.item.domain.ItemList;
import saleson.api.item.support.ItemDataSupport;
import saleson.common.utils.ItemUtils;
import saleson.shop.item.ItemService;
import saleson.shop.item.support.ItemParam;
import saleson.shop.keyword.KeywordService;
import saleson.shop.keyword.domain.Keyword;
import saleson.shop.search.SearchService;
import saleson.shop.search.domain.Search;
import saleson.shop.search.support.SearchRecommendParam;

import javax.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController("ApiSearchController")
@RequestMapping("/api/search")
public class SearchController {

	@Autowired
	SearchService searchService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemDataSupport itemDataSupport;

	@Autowired
	private KeywordService keywordService;

	@GetMapping("/best-keyword")
	public ResponseEntity bestKeyword(HttpServletRequest request) {
		ResponseEntity result = null;
		List<Keyword> list = null;
		try {
			list = keywordService.getBestKeyword(10);
			result = ApiResponseEntity.data().list(list).ok();
		} catch (OpRuntimeException e) {
			//result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getMessage());
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "ERROR-53: best-keyword 조회 실패");
		}
		return result;
	}

	@GetMapping("/recommend-keyword")
	public ResponseEntity recommendKeyword(HttpServletRequest request) {

		ResponseEntity result = null;

		try {
			SearchRecommendParam searchRecommendParam = new SearchRecommendParam();
			searchRecommendParam.setDisplayFlag("Y");
			Search search = searchService.getSearchForFront(searchRecommendParam);

			HashMap<String, Object> map = new HashMap<>();
			map.put("search", search);

			result = ApiResponseEntity.data().map(map).ok();

		} catch (OpRuntimeException e) {
			//result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getMessage());
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "ERROR-54: recommend-keyword 조회 실패");
		}
		return result;
	}

	/**
	 * 상단 검색 API
	 *
	 * @param itemParam
	 * @param
	 * @return
	 */
	@GetMapping("/result")
	public ResponseEntity searchResult(ItemParam itemParam) {
		ResponseEntity result = null;
		List<ItemList> resultList = null;
		itemParam.setWhere("ITEM_NAME");

		if (itemParam == null) {
			itemParam = new ItemParam();
		}

		String query = itemParam.getQuery();

		if (!ObjectUtils.isEmpty(query)) {
			try {
				itemParam.setQuery(URLDecoder.decode(query, StandardCharsets.UTF_8.toString()));
			} catch (UnsupportedEncodingException ignore) {
				log.info("URL Decode Error (ignore): {}", query, ignore);
			}
		}

		try {
			if (itemParam.getItemsPerPage() < 20) {
				itemParam.setItemsPerPage(20);
			}

			keywordService.mergeItemKeyword(itemParam);

			// 사용자단에 노출될 상품 조회에 필요한 기본적인 itemParam bind
			itemParam = ItemUtils.bindItemParam(itemParam);

			int itemCount = itemService.getItemCount(itemParam);

			Pagination pagination = Pagination.getInstance(itemCount, itemParam.getItemsPerPage());
			itemParam.setPagination(pagination);
	        
	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
//	        pagination.setCurrentPage(1);
//	        itemParam.setPage(1);
	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회

			result = ApiResponseEntity.data().list(itemDataSupport.resultItemListInfo(itemService.getItemList(itemParam)))
					.pagination(pagination).ok();

		} catch (OpRuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

}
