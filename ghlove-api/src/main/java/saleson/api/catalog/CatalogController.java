package saleson.api.catalog;

import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.item.domain.ItemList;
import saleson.api.item.support.ItemDataSupport;
import saleson.shop.catalog.CatalogMngMapper;
import saleson.shop.catalog.CatalogMngService;
import saleson.shop.catalog.domain.CatalogMng;
import saleson.shop.catalog.domain.LocgovFavItemMng;
import saleson.shop.catalog.support.CatalogMngParam;
import saleson.shop.catalog.support.LocgovFavItemMngParam;
import saleson.shop.designateddonation.DesignatedDonationMapper;
import saleson.shop.designateddonation.DesignatedDonationService;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.item.domain.Item;
import saleson.shop.wishlist.WishlistService;

@RestController("ApiCatalogController")
@RequestMapping("/api/catalog")
public class CatalogController {

	private static final Logger log = LoggerFactory.getLogger(CatalogController.class);

    @Autowired
    private CatalogMngService catalogMngService;
    
//    @Autowired
//	private CatalogMngMapper catalogMngMapper;
//    
//    @Autowired
//    private DesignatedDonationMapper designatedDonationMapper;
    
    @Autowired
    private DesignatedDonationService designatedDonationService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private ItemDataSupport itemDataSupport;
    
    
    /**
	 * 소식지 년도 호수 조회
	 * @param
	 * @return
	 */
    @GetMapping("/getCatalogYearNoList")
    public ResponseEntity<Map<String, Object>> selectCatalogList(CatalogMngParam param){
    	ResponseEntity<Map<String, Object>> result = null;
    	
        try {
//        	List<CatalogMng> list = catalogMngMapper.selectCatalogMngList(param);
        	param.setItemsPerPage(Integer.MAX_VALUE);
        	List<CatalogMng> list = catalogMngService.selectCatalogMngList(param);
	        
            result = ApiResponseEntity.data()
            		.put("catalogYearNoList", list)
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            log.error(getClass().getName() + " selectNewRgstItemMng error :: " + e.getErrorMessage());
        }
        return result;
    }

	
	/**
	 * 소식지 주요 답례품 목록 조회
	 * @param
	 * @return
	 */
    @GetMapping("/getCatalogNewItem")
    public ResponseEntity<Map<String, Object>> selectNewRgstItemMng(CatalogMngParam param){
    	ResponseEntity<Map<String, Object>> result = null;
    	
        try {
        	Pagination pagination = Pagination.getInstance(catalogMngService.selectNewRgstItemMngCount(param), param.getItemsPerPage());
        	
        	// 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
	        pagination.setCurrentPage(1);
	        param.setPage(1);
	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회

	        if (param.getPage() > 0) {
	        	pagination.setCurrentPage(param.getPage());
	        } else {
	        	pagination.setCurrentPage(1);
	        }

	        param.setPagination(pagination);
        	
        	List<Item> list = catalogMngService.selectNewRgstItemMng(param);
        	
	        wishlistService.setWishlistFlagByItem(list);

	        List<ItemList> resultList = itemDataSupport.resultItemListInfo(list);

            result = ApiResponseEntity.data()
            		.put("content", resultList)
            		.put("pageInfo", param.getPagination())
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
                   log.error(getClass().getName() + " selectNewRgstItemMng error :: " + e.getErrorMessage());
        }
        return result;
    }
    
    
    /**
	 * 소식지 기금사업 조회
	 * @param
	 * @return
	 */
    @GetMapping("/getDsgnDonationItem")
    public ResponseEntity<Map<String, Object>> selectDsgnDonationItem(DesignatedDonationSearchParam param){
    	ResponseEntity<Map<String, Object>> result = null;
    	
        try {
//        	Pagination pagination = Pagination.getInstance(designatedDonationService.selectDesignatedDonationListCount(param), param.getItemsPerPage());        	
//        	// 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
//	        pagination.setCurrentPage(1);
//	        param.setPage(1);
//	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
//
//	        if (param.getPage() > 0) {
//	        	pagination.setCurrentPage(param.getPage());
//	        } else {
//	        	pagination.setCurrentPage(1);
//	        }
//
//	        param.setPagination(pagination);
//	        
//	        param.setDisplay("FRONT");
//	        
//	        List<DesignatedDonation> list = designatedDonationMapper.selectDesignatedDonationList(param);

        	param.setDisplay("FRONT");
        	// 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
	        param.setPage(1);
	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
        	
        	List<DesignatedDonation> list = designatedDonationService.selectDesignatedDonationList(param);
        	
            result = ApiResponseEntity.data()
            		.put("dsgnDonationItem", list)
            		.put("pageInfo", param.getPagination())
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            log.error(getClass().getName() + " selectDsgnDonationItem error :: " + e.getErrorMessage());
        }
        return result;
    }
    
    
    /**
	 * 소식지 인기답례품 조회
	 * @param
	 * @return
	 */
    @GetMapping("/getLocgovFavItem")
    public ResponseEntity<Map<String, Object>> selectLocgovFavItem(LocgovFavItemMngParam param){
    	ResponseEntity<Map<String, Object>> result = null;
    	
        try {
//	        Pagination pagination = Pagination.getInstance(catalogMngMapper.selectLocgovFavItemMngCount(param), param.getItemsPerPage());
//        	// 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
//	        pagination.setCurrentPage(1);
//	        param.setPage(1);
//	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
//
//	        if (param.getPage() > 0) {
//	        	pagination.setCurrentPage(param.getPage());
//	        } else {
//	        	pagination.setCurrentPage(1);
//	        }
//
//	        param.setPagination(pagination);
//	        
//	        List<LocgovFavItemMng> list = catalogMngMapper.selectLocgovFavItemMngList(param);
	        
        	// 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
	        param.setPage(1);
	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
	        
	        List<LocgovFavItemMng> list = catalogMngService.selectLocgovFavItemMngList(param);
        	
            result = ApiResponseEntity.data()
            		.put("locgovFavItem", list)
            		.put("pageInfo", param.getPagination())
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            log.error(getClass().getName() + " selectLocgovFavItem error :: " + e.getErrorMessage());
        }
        return result;
    }

	
	/**
	 * 소식지 제철 답례품 목록 조회
	 * @param
	 * @return
	 */
    @GetMapping("/getCatalogSeasonalItem")
    public ResponseEntity<Map<String, Object>> selectSeasonCatalogItemMng(CatalogMngParam param){
    	ResponseEntity<Map<String, Object>> result = null;
    	
        try {        	
	        Pagination pagination = Pagination.getInstance(catalogMngService.selectSeasonCatalogItemMngCount(param), param.getItemsPerPage());
	        
	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
	        pagination.setCurrentPage(1);
	        param.setPage(1);
	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회

	        if (param.getPage() > 0) {
	        	pagination.setCurrentPage(param.getPage());
	        } else {
	        	pagination.setCurrentPage(1);
	        }

	        param.setPagination(pagination);
        	
        	List<Item> list = catalogMngService.selectSeasonCatalogItemMng(param);
        	
	        wishlistService.setWishlistFlagByItem(list);

	        List<ItemList> resultList = itemDataSupport.resultItemListInfo(list);

            result = ApiResponseEntity.data()
            		.put("content", resultList)
            		.put("pageInfo", param.getPagination())
            		.ok();
        } catch(UserException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            log.error(getClass().getName() + " selectSeasonCatalogItemMng error :: " + e.getErrorMessage());
        }
        return result;
    }

	@PostMapping("/getCatalogMainInfo")
    public ResponseEntity<Map<String, Object>> getCatalogMainInfo(CatalogMngParam param){
		
		CatalogMngParam tempParam = new CatalogMngParam();
		tempParam.setDisplayYn("Y");
		tempParam.setItemsPerPage(Integer.MAX_VALUE);
    	List<CatalogMng> catalogMngList = catalogMngService.selectCatalogMngList(tempParam);
    	
		if (catalogMngList == null || catalogMngList.isEmpty()) {
			throw new UserException("등록된 소식지 정보가 없습니다.");
		}
		
		if (param.getCatalogYear() == 0 || param.getCatalogNo() == 0) {
			CatalogMng recentCatalogMng = catalogMngList.get(0);
			param.setCatalogYear(recentCatalogMng.getCatalogYear());
			param.setCatalogNo(recentCatalogMng.getCatalogNo());
		}
		
		
		ResponseEntity<Map<String, Object>> result = null;
		
		result = ApiResponseEntity.data()
				.put("catalogMngList", catalogMngList)
				.ok();
		
		return result;
	}
    
    
}
