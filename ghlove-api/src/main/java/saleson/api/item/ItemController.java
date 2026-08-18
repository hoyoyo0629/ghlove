package saleson.api.item;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
//import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.BusinessException;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.coupon.domain.CouponInfo;
import saleson.api.item.domain.ItemDetailInfo;
import saleson.api.item.domain.ItemList;
import saleson.api.item.domain.RelationItem;
import saleson.api.item.domain.SellerInfo;
import saleson.api.item.support.ItemDataSupport;
import saleson.api.mypage.domain.CntrPointInfo;
import saleson.api.mypage.domain.ItemReviewInfo;
import saleson.api.mypage.domain.ReviewFilterInfo;
import saleson.api.qna.support.QnaDataSupport;
import saleson.common.utils.EmojiUtils;
import saleson.common.utils.ItemUtils;
import saleson.common.utils.UserUtils;
import saleson.model.FilterGroup;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.seller.main.domain.SellerEncryptor;
import saleson.shop.cardbenefits.CardBenefitsService;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Breadcrumb;
import saleson.shop.categories.domain.BreadcrumbCategory;
import saleson.shop.categories.domain.Categories;
import saleson.shop.categories.support.CategoryParam;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.coupon.CouponService;
import saleson.shop.coupon.domain.Coupon;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.display.DisplayService;
import saleson.shop.item.ItemFrontService;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemEarnPoint;
import saleson.shop.item.domain.ItemOther;
import saleson.shop.item.domain.ItemRelation;
import saleson.shop.item.domain.ItemReview;
import saleson.shop.item.support.ItemParam;
import saleson.shop.mypage.domain.CntrPoint;
import saleson.shop.mypage.support.CntrPointParam;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.Order;
import saleson.shop.order.givepoint.OrderGivePointService;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.order.support.OrderException;
import saleson.shop.order.support.OrderParam;
import saleson.shop.point.PointService;
import saleson.shop.point.domain.PointPolicy;
import saleson.shop.point.support.OrderPointParam;
import saleson.shop.qna.QnaService;
import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.support.QnaParam;
import saleson.shop.restocknotice.RestockNoticeService;
import saleson.shop.restocknotice.domain.RestockNotice;
import saleson.shop.reviewfilter.ReviewFilterService;
import saleson.shop.user.LocgovService;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.LocgovItemImage;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.wishlist.WishlistService;
import saleson.shop.wishlist.domain.Wishlist;


@RestController("ApiItemController")
@RequestMapping("/api/item")
public class ItemController {
    private static Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private DisplayService displayService;

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    SequenceService sequenceService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private PointService pointService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private CardBenefitsService cardBenefitsService;

    @Autowired
    private QnaService qnaService;

    @Autowired
    private RestockNoticeService restockNoticeService;

    @Autowired
    private ReviewFilterService reviewFilterService;

    @Autowired
    private ItemDataSupport itemDataSupport;

    @Autowired
    private QnaDataSupport qnaDataSupport;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemFrontService itemFrontService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private LocgovService locgovService;
    
    @Autowired
    private SellerEncryptor sellerEncryptor;
    
    @Autowired
    private OrderGivePointService orderGivePointService;
    
    
    /**
     * 상품후기 리스트 API (itemId, page, itemsPerPage)
     *
     * @param request
     * @param itemReviewParam
     * @return
     */
    @GetMapping("/reviews")
    public ResponseEntity review(HttpServletRequest request, ItemParam itemReviewParam) {
        ResponseEntity result = null;
        List<ItemReview> reviewList = null;
        Pagination pagination = null;

        itemReviewParam.setConditionType("FRONT_ITEM_DETAIL");

        try {
            
            if (itemFrontService.isItemRestrict()) {
            	pagination = Pagination.getInstance(0); 
            	return ApiResponseEntity.data().list(new ArrayList<>()).pagination(pagination).ok();
            }

            Item item = itemService.getItemBy(itemReviewParam.getItemUserCode());

            if (item == null) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            }

            itemReviewParam.setItemId(item.getItemId());

            if (itemReviewParam.getItemId() > 0) {

                String fcIds = itemReviewParam.getFcIds();
                if (!ObjectUtils.isEmpty(fcIds)) {

                    boolean reviewFilterFlag = false;

                    try {

                        String[] filterCodeIds = StringUtils.delimitedListToStringArray(fcIds,"||");
                        if (filterCodeIds != null && filterCodeIds.length > 0) {
                            itemReviewParam.setFilterCodeIds(filterCodeIds);
                            reviewFilterFlag = true;
                        }
                    } catch (OpRuntimeException ignore) {
                        //log.error("set review filter error [{}] {}", itemReviewParam.getItemUserCode(), ignore.getMessage(), ignore);
                    	log.error("set review filter error [{}] {}", itemReviewParam.getItemUserCode(), "ERROR-60: set review filter error");
                    }

                    itemReviewParam.setReviewFilterFlag(reviewFilterFlag);
                }

                // 상품상세 리뷰 조회
                pagination = Pagination.getInstance(itemService.getItemReviewCountByParam(itemReviewParam), itemReviewParam.getItemsPerPage());
                itemReviewParam.setPagination(pagination);

                reviewList = itemService.getItemReviewListByParam(itemReviewParam);

                List<ItemReviewInfo> infos = new ArrayList<>();

                for (ItemReview itemReview : reviewList) {
                    infos.add(new ItemReviewInfo((itemReview)));
                }

                result = ApiResponseEntity.data().list(infos).pagination(pagination).ok();
            } else {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            }
        } catch (RuntimeException e) {
            log.error("Error (/reviews)", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 상품후기 작성 API (itemId, score, subject, content, orderCode, reviewImageFile)
     *
     * @param itemReview
     * @return
     */
    @PostMapping("/review")
    public ResponseEntity reviewAdd(ItemReview itemReview) {
        ResponseEntity result = null;
        try {

            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            }

            Item item = itemService.getItemById(itemReview.getItemId());
            if (item == null) {
            	result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            }

            // 상품 유효성 체크
            if (!(ValidationUtils.isNull(item) || !"Y".equals(item.getDisplayFlag()))) {

                OrderParam orderParam = new OrderParam();
                orderParam.setOrderCode(itemReview.getOrderCode());
                orderParam.setOrderSequence(0);
                orderParam.setUserId(UserUtils.getUserId());

                Order order = orderService.getOrderByParam(orderParam);
                List<String> orderItemCodes = order.getOrderItemUserCodes();
                long itemCount = orderItemCodes.stream().filter(oic -> oic.equals(item.getItemUserCode())).count();

                if (itemCount <= 0) {
                    return ApiResponseEntity.error(ApiError.BAD_REQUEST_ITEM_REVIEW_WRITING_NOT_ALLOWED);
                }

                // 이모티콘 제거 (얼굴 모양, 등등)
                itemReview.setSubject(EmojiUtils.removeEmoticon(itemReview.getSubject()));
                itemReview.setContent(EmojiUtils.removeEmoticon(itemReview.getContent()));

                itemReview.setOptions(itemReview.getOptions().replaceAll("\n", "<br/>"));
                itemService.insertItemReview(itemReview);

                result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
            } else {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            }
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "문제가 발생했습니다.");
        }
        return result;
    }


    @GetMapping("/review/info/{itemUserCode}")
    public ResponseEntity reviewInfo(@PathVariable("itemUserCode") String itemUserCode) {
        try {
            Config config = configService.getShopConfig(Config.SHOP_CONFIG_ID);

            if (config == null) {
                config = new Config();
            }
            
            if (itemFrontService.isItemRestrict()) {
            	return ApiResponseEntity.data()
                        .put("reviewFilters", new ArrayList<>())
                        .ok();
            }

            Item item = itemService.getItemByItemUserCode(itemUserCode);

            if (item == null) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            }

            return ApiResponseEntity.data()
                    .put("pointReview", config.getPointReview())
                    .put("photoPointReview", config.getPhotoPointReview())
                    .put("reviewFilters", getReviewFilterInfos(item))
                    .ok();

        } catch (OpRuntimeException e) {
            log.error("get review info error itemUserCode => {}", itemUserCode, e);
            if (StringUtils.hasLength(e.getErrorMessage())) {
            	return ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
            }
            return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
    }

    /**
     * 상품 상세정보 API (itemUserCode)
     *
     * @param request
     * @param itemUserCode
     * @return
     */
    @GetMapping("/{itemUserCode}")
    public ResponseEntity details(HttpServletRequest request, @PathVariable("itemUserCode") String itemUserCode) {
        ResponseEntity result = null;
        
        if (itemFrontService.isItemRestrict()) {
        	return ApiResponseEntity.error(ApiError.RESTRICT_ITEM);
        }

//        PointPolicy pointPolicy = null;
//        List<ItemOther> itemOthers = null;

        try {
            Item item = itemService.getItemByItemUserCode(itemUserCode);
            
            if (item == null || "N".equalsIgnoreCase(item.getDisplayFlag())) {
                //throw new OrderException("주문 가능한 상품이 없습니다.");
            	return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            }
            

            itemDataSupport.setItemDataNvl(item);
            
            String adultErr = "N";
            if ("Y".equalsIgnoreCase(item.getAdultItemYn()) && !UserUtils.isAdult()) {
            	adultErr = "Y";
            }

            // 지급 포인트 정보
            Config config = configService.getShopConfigCache(Config.SHOP_CONFIG_ID);

            OrderPointParam orderPointParam = new OrderPointParam();
            orderPointParam.setItemId(item.getItemId());
            orderPointParam.setRepeatDayEndTime(config.getRepeatDayEndTime());
            orderPointParam.setRepeatDayStartTime(config.getRepeatDayStartTime());

//            pointPolicy = pointService.getPointPolicyByOrderPointParam(orderPointParam);

            // 판매자 정보
            Seller seller = sellerService.getSellerById(item.getSellerId());
            seller.decrypt(sellerEncryptor, false);

            CodeParam param = new CodeParam();
            param.setCodeType("QNA_GROUPS");
            List<Code> qnaGroups = codeService.getCodeList(param);
            
            List<Code> qnaGroupsItem = new ArrayList<>();
            
            for (Code code : qnaGroups) {
				if (code.getExtentionCode() != null && code.getExtentionCode().contains("item")) {
					qnaGroupsItem.add(code);
				}
			}
            
            String userName = "";
            if (SecurityUtils.isLogin()) {
            	userName = UserUtils.getUser().getUserName();
            }
            
            // 같이 구매한 상품 목록
//            itemOthers = itemService.getItemOtherList(item.getItemId());
            result = ApiResponseEntity.data()
                    .put("item", new ItemDetailInfo(item))
//                    .put("pointPolicy", pointPolicyDataSet(pointPolicy))
//                    .put("earnPoint", new ItemEarnPoint(pointPolicy, item))
//                    .put("seller", seller)
                    .put("seller", new SellerInfo(seller))
                    .put("userId",UserUtils.getUserId())
                    .put("userName", userName)
                    .put("breadcrumbs", item.getBreadcrumbs())
//                    .put("cardBenefits", cardBenefitsService.getTodayCardBenefits(DateUtils.getToday()))
//                    .put("reviewFilters", getReviewFilterInfos(item))
                    .put("config", config)
                    .put("qnaGroups", qnaGroupsItem)
                    .put("adultErr", adultErr)
                    .put("imgDescList", itemService.getItemImagesExplain(item.getItemId()))
                    .put("imgDescListLinkView", itemFrontService.getItemImagesExplainByLinkView(itemUserCode))
//                    .list(itemOthers)
                    .ok();

//        } catch (OrderException e) {
//            if (e.getErrorMessage().equals("주문 가능한 상품이 없습니다.")) {
//                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
//            } else {
//                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
//            }
        } catch (OpRuntimeException e) {
            if (e.getErrorMessage().equals("답례품정보가 없습니다.") || e.getErrorMessage().equals("상품정보가 없습니다.")) {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            } else {
                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            }
        }
        return result;
    }

//    private Map<String, Object> pointPolicyDataSet(PointPolicy pointPolicy) {
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("pointType", pointPolicy.getPointType());
//        resultMap.put("point", pointPolicy.getPoint());
//        return resultMap;
//    }


    private List<ReviewFilterInfo> getReviewFilterInfos (Item item) {
        int categoryId = 0;

        try {
            if (item.getBreadcrumbs() != null && !item.getBreadcrumbs().isEmpty()) {
                Breadcrumb breadcrumb = item.getBreadcrumbs().get(0);
                List<BreadcrumbCategory> breadcrumbCategories  = breadcrumb.getBreadcrumbCategories();
                if (breadcrumbCategories != null  && !breadcrumbCategories.isEmpty()) {
                    int size = breadcrumbCategories.size();
                    BreadcrumbCategory breadcrumbCategory = breadcrumbCategories.get(size - 1);
                    if (breadcrumbCategory != null) {
                        categoryId = Integer.parseInt(breadcrumbCategory.getCategoryId());
                    }
                }
            }
        } catch (OpRuntimeException ignore) {
            //log.error("get item categoryId error [{}] {}", item.getItemUserCode(), ignore.getMessage(), ignore);
            log.error("get item categoryId error [{}] {}", item.getItemUserCode(), "ERROR-61: get item categoryId error", ignore);
        }

        List<ReviewFilterInfo> reviewFilterInfos = new ArrayList<>();

        if (categoryId > 0) {
            List<FilterGroup> filterGroups = reviewFilterService.getBreadcrumbFilterGroupList(categoryId);

            if (filterGroups != null && !filterGroups.isEmpty()) {
                filterGroups.forEach(f -> {
                    reviewFilterInfos.add(new ReviewFilterInfo(f));
                });
            }
        }

        return reviewFilterInfos;
    }


    /**
     * 상품리스트 조회 API (categoryCode, orderBy, sort, page, itemsPerPage, listType)
     *
     * @param itemParam
     * @return
     */
    @GetMapping("")
    public ResponseEntity list(ItemParam itemParam, HttpServletRequest request) {

        if (itemFrontService.isItemRestrict()) {
        	return ApiResponseEntity.error(ApiError.RESTRICT_ITEM);
        }
    	
        ResponseEntity result = null;
        List<ItemList> resultList = null;
        Pagination pagination = null;
        Categories category = null;
        String categoryClass = "";

        if (itemParam == null) {
            itemParam = new ItemParam();
        }

        try {
        	/*
	    	if ("ALL".equals(itemParam.getCategoryCode())) {

	    	} else {
	            category = categoriesService.getCategoryByCategoryUrl(itemParam.getCategoryCode());

	            categoryClass = category.getCategoryCode().substring(0, Integer.parseInt(category.getCategoryLevel()) * 3);
	            itemParam.setCategoryClass(categoryClass);
	            itemParam.setDisplayNewItemListTop("1");
	            itemParam.setCategoryId(Integer.toString(category.getCategoryId()));
	    	}
	    	*/

	    	if (!StringUtils.isEmpty(itemParam.getCategory())) {
	    		CategoryParam categoryParam = new CategoryParam();
	    		categoryParam.setCategory(itemParam.getCategory());
	    		Categories categories = categoriesService.getCategoryLevelClassByCategoryParam(categoryParam);
	    		if (categories == null) {
	    			throw new RuntimeException();
	    		}
	    		itemParam.setCategoryLevel(categories.getCategoryLevel());
	    		itemParam.setCategoryClass1(categories.getCategoryClass1());
	    		itemParam.setCategoryClass2(categories.getCategoryClass2());
	    		itemParam.setCategoryClass3(categories.getCategoryClass3());
	    		itemParam.setCategoryClass4(categories.getCategoryClass4());
	    	}

	    	// kdj 수정
	        if (StringUtils.isNull(request.getParameter("itemsPerPage")) || StringUtils.isEmpty(request.getParameter("itemsPerPage"))) {
	            itemParam.setItemsPerPage(12);
	        } else {
	        	itemParam.setItemsPerPage(Integer.parseInt(request.getParameter("itemsPerPage")));
	        }

	        // 사용자단에 노출될 상품 조회에 필요한 기본적인 itemParam bind
	        itemParam = ItemUtils.bindItemParam(itemParam);

	        // 필터 값이 존재 할 경우
	        if(!"".equals(itemParam.getFcIds()) && itemParam.getFcIds() != null){
	            itemParam.setFilterCodeIds(itemParam.getFcIds().split("N"));
	        }

	        pagination = Pagination.getInstance(itemFrontService.getItemCount(itemParam), itemParam.getItemsPerPage());
	        
	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회
//	        pagination.setCurrentPage(1);
//	        itemParam.setPage(1);
	        // 답례품 수가 페이지당 표시 개수보다 많을 경우 무한 랜덤 조회

	        if (itemParam.getPage() > 0) {
	        	pagination.setCurrentPage(itemParam.getPage());
	        } else {
	        	pagination.setCurrentPage(1);
	        }

	        pagination.setItemsPerPage(itemParam.getItemsPerPage());

	        itemParam.setPagination(pagination);

	        if(itemParam.isHistoryPage()){
	            itemParam.setPagination(null);
	            itemParam.setLimit(itemParam.getPage()*itemParam.getItemsPerPage());
	        }

	        List<Item> itemList = itemFrontService.getItemList(itemParam);
	        wishlistService.setWishlistFlagByItem(itemList);

	        resultList = itemDataSupport.resultItemListInfo(itemList);
	        
	        String locgovPcImage = "";
	        String locgovMbImage = "";
	        if (itemParam != null && !StringUtils.isEmpty(itemParam.getLocgov())) {
		        LocgovItemImage locgovItemImage = locgovService.getLocgovItemImage(itemParam.getLocgov().replaceAll("U", ""));
		        if (locgovItemImage != null) {
		        	locgovPcImage = "/upload/locgovItem/" + locgovItemImage.getLocgovCode() + "/" + locgovItemImage.getPcFileName();
		        	locgovMbImage = "/upload/locgovItem/" + locgovItemImage.getLocgovCode() + "/" + locgovItemImage.getMobileFileName();
//		        	locgovPcImage = "http://localhost:8080/upload/locgovItem/" + locgovItemImage.getLocgovCode() + "/" + locgovItemImage.getPcFileName();			// 로컬 테스트용
//		        	locgovMbImage = "http://localhost:8080/upload/locgovItem/" + locgovItemImage.getLocgovCode() + "/" + locgovItemImage.getMobileFileName();		// 로컬 테스트용
		        }
	        }
	        
	        result = ApiResponseEntity.data().list(resultList).pagination(pagination)
	        		.put("adultYn", UserUtils.isAdult() ? "Y" : "N")
	        		.put("locgovPcImage", locgovPcImage)
	        		.put("locgovMbImage", locgovMbImage)
	        		.ok();


        } catch (RuntimeException e) {
//        	e.printStackTrace();
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }





    /**
     * 다운 가능한 상품 쿠폰 리스트 (itemId, couponType)
     *
     * @param userCouponParam
     * @return
     */
    @GetMapping("/coupons")
    public ResponseEntity itemDownloadCoupons(UserCouponParam userCouponParam) {
        ResponseEntity result = null;
//        Item item = null;
//        Pagination pagination = null;
//
//        if (userCouponParam == null) {
//            return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
//        }
//        try {
//
//            item = itemService.getItemBy(userCouponParam.getItemUserCode());
//
//            if (item == null) {
//                return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
//            }
//
//            UserDetail userDetail = UserUtils.getUserDetail();
//            List<CouponInfo> list = new ArrayList<>();
//
//            if (item.getItemId() > 0) {
//                userCouponParam.setItemId(item.getItemId());
//
//                if ("N".equals(item.getCouponUseFlag())) {
//                    return ApiResponseEntity.data().put("content", list).ok();
//                }
//            }
//
//            userCouponParam.setUserId(UserUtils.getUserId());
//            userCouponParam.setUserLevelId(userDetail.getLevelId());
//            itemService.setDownloadableCouponListPagination(userCouponParam);
//            List<Coupon> coupons = couponService.getUserDownloadableCouponListByParam(userCouponParam);
//
//            if (coupons != null && !coupons.isEmpty() && UserUtils.isUserLogin()) {
//
//                coupons.forEach(c->{
//                    list.add(new CouponInfo(c));
//                });
//
//            }
//
//            result = ApiResponseEntity.data().list(list).pagination(userCouponParam.getPagination()).put("status", HttpStatus.OK).ok();
//
//        } catch (RuntimeException e) {
//            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
//        }
        return result;
    }

    /**
     * 다운 가능한 상품 쿠폰 다운로드
     *
     * @param userCouponParam
     * @return
     */
    @PostMapping("/download-all-coupons")
    public ResponseEntity downloadAllCoupons(@RequestBody UserCouponParam userCouponParam) {
        ResponseEntity result = null;
//        Item item = null;
//        Pagination pagination = null;
//
//        if (userCouponParam == null) {
//            return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
//        }
//        try {
//
//            item = itemService.getItemBy(userCouponParam.getItemUserCode());
//
//            if (item == null) {
//                return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
//            }
//
//            UserDetail userDetail = UserUtils.getUserDetail();
//
//            if (item.getItemId() > 0) {
//                userCouponParam.setItemId(item.getItemId());
//
//                if ("N".equals(item.getCouponUseFlag())) {
//                    return ApiResponseEntity.error(ApiError.BAD_REQUEST_FAIL_DOWNLOAD_COUPON,"쿠폰 정보가 없습니다.");
//                }
//            }
//
//            userCouponParam.setPagination(null);
//            userCouponParam.setUserId(UserUtils.getUserId());
//            userCouponParam.setUserLevelId(userDetail.getLevelId());
//            itemService.setDownloadableCouponListPagination(userCouponParam);
//            List<Coupon> coupons = couponService.getUserDownloadableCouponListByParam(userCouponParam);
//
//            int errorCount = 0;
//            int downloadCount = 0;
//            for (Coupon c : coupons) {
//                UserCouponParam downloadCouponParam = new UserCouponParam();
//                downloadCouponParam.setCouponId(c.getCouponId());
//                if (couponService.userCouponDownload(downloadCouponParam) == 0) {
//                    errorCount++;
//                } else {
//                    downloadCount++;
//                }
//            }
//
//            Map<String, Object> map = new LinkedHashMap<>();
//            map.put("downloadCount", downloadCount);
//            map.put("errorCount", errorCount);
//
//            result = ApiResponseEntity.data().map(map).ok();
//
//        } catch (RuntimeException e) {
//            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
//        }
        return result;
    }

    private List<Map<String, Object>> downloadCouponData(List<Coupon> coupons) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (!coupons.isEmpty()) {
            for (int i = 0; i < coupons.size(); i++) {
                Map<String, Object> couponMap = new HashMap<>();

                couponMap.put("couponId", coupons.get(i).getCouponId());  // 쿠폰 ID
                couponMap.put("couponName", coupons.get(i).getCouponName());  // 쿠폰명
                couponMap.put("couponPay", coupons.get(i).getCouponPay());  // 쿠폰할인금액
                couponMap.put("couponPayType", coupons.get(i).getCouponPayType());  // 쿠폰할인금액 타입
                couponMap.put("couponPayRestriction", coupons.get(i).getCouponPayRestriction());  // 사용가능 상품판매가(개당)
                couponMap.put("couponApplyType", coupons.get(i).getCouponApplyType());  // 쿠폰사용기간
                couponMap.put("couponApplyDay", coupons.get(i).getCouponApplyDay());  // 일자설정
                couponMap.put("couponApplyStartDate", coupons.get(i).getCouponApplyStartDate());  // 쿠폰 사용기간 시작일
                couponMap.put("couponApplyEndDate", coupons.get(i).getCouponApplyEndDate());  // 쿠폰 사용기간 종료일
                couponMap.put("couponTargetItemType", coupons.get(i).getCouponTargetItemType());    //
                couponMap.put("couponDiscountLimitPrice", coupons.get(i).getCouponDiscountLimitPrice());    // 쿠폰 최대 사용금액 (2019.02.26 김태영)
                resultList.add(couponMap);
            }
        }
        return resultList;
    }

    @PostMapping("/wishlist")
    public ResponseEntity addWishlist(@RequestBody(required = false) Wishlist wishlist) {
        ResponseEntity result = null;
        String errorMsg = "";	// 에러메시지

        wishlist.setUserId(UserUtils.getUserId());
        int addedItemCount = 0;

        try {

    		if (wishlist.getItemId() == 0) {
    			errorMsg = "상품정보가 없습니다.";
    			throw new BusinessException("상품정보가 없습니다.");
    		}

            addedItemCount = wishlistService.insertWishlist(wishlist);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch (RuntimeException e) {
            if (errorMsg.equals("상품정보가 없습니다.")) {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            } else {
                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            }
        }
        return result;
    }

    /**
     * 관련상품 조회
     * @param itemParam
     * @return
     */
    @GetMapping("relation")
    public ResponseEntity getItemRelations(ItemParam itemParam) {
        ResponseEntity result = null;

        try {

            Item item = itemService.getItemBy(itemParam.getItemUserCode());

            if (item == null) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            }

            List<RelationItem> list = new ArrayList<>();
            List<ItemRelation> relations = itemService.getItemRelationsByItemId("1", item.getItemId());

            if (relations != null && !relations.isEmpty()) {
                for (ItemRelation relation : relations) {
                    list.add(new RelationItem(relation));
                }
            }

            result = ApiResponseEntity.data().list(list).ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }


    @GetMapping("/qna")
    public ResponseEntity itemInquiry(QnaParam qnaParam) {

        ResponseEntity result = null;

        try {

            Item item = itemService.getItemBy(qnaParam.getItemUserCode());

            if (item == null) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            }

            qnaParam.setItemId(item.getItemId());

            if (qnaParam.getItemId() <= 0) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            qnaParam.setQnaType(Qna.QNA_GROUP_TYPE_ITEM);
            Pagination pagination = Pagination.getInstance(qnaService.getQnaListCountByParam(qnaParam));
            qnaParam.setPagination(pagination);
            List<Qna> list = qnaService.getQnaListByParam(qnaParam);
            result = ApiResponseEntity.data().list(qnaDataSupport.qnaDataSet(list, UserUtils.getUserId())).pagination(pagination).put("status", HttpStatus.OK).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 재입고알림 API (restockNotice)
     *
     * @param restockNotice
     * @return
     */
    @GetMapping("/restock")
    public ResponseEntity getRestockNotice(RestockNotice restockNotice) {
        ResponseEntity result = null;
        boolean isRestockNotice = false;

        try {
            // 재입고알림 정보
            if (UserUtils.isUserLogin()) {
                restockNotice.setItemId(restockNotice.getItemId());
                restockNotice.setUserId(UserUtils.getUserId());
                isRestockNotice = restockNoticeService.isRestockNotice(restockNotice);
            }

            result = ApiResponseEntity.data().put("isRestockNotice", isRestockNotice).ok();

        } catch(RuntimeException e) {
            //result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getMessage());
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "ERROR-30: System Error (시스템 에러)");
        }

        return result;
    }

    /**
     * 재입고알림 신청 API (restockNotice)
     *
     * @param restockNotice
     * @return
     */
    @PostMapping("/restock")
    public ResponseEntity restockNotice(@RequestBody(required = false) RestockNotice restockNotice) {
        ResponseEntity result = null;

        try {
            User user = UserUtils.getUser();
            restockNotice.setUserId(user.getUserId());
            restockNotice.setSendFlag("N");

            restockNoticeService.insertRestockNotice(restockNotice);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch(RuntimeException e) {
            //result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getMessage());
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "ERROR-30: System Error (시스템 에러)");
        }

        return result;
    }


    @PostMapping("/review/add-like/{id}")
    public ResponseEntity addItemReviewLike(HttpServletRequest request, @PathVariable("id") int itemReviewId) {

        ResponseEntity result = null;

        try {
            boolean flag = itemService.saveItemReviewLike(request, itemReviewId);
            result = ApiResponseEntity.data().put("flag", flag).put("status", HttpStatus.OK).ok();
        } catch(RuntimeException e) {
            //log.error("addItemReviewLike error {}", e.getMessage(), e);
        	log.error("addItemReviewLike error {}", "ERROR-62: addItemReviewLike error", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "오류가 발생 했습니다.");
        }

        return result;

    }

    @PostMapping("/remove-wishlist")
    public ResponseEntity<Map<String, Object>> removeWishlist(@RequestBody(required = false) Wishlist wishlist) {
        ResponseEntity<Map<String, Object>> result = null;
        String errorMsg = "";	// 에러메시지

        wishlist.setUserId(UserUtils.getUserId());
        try {

    		if (wishlist.getItemId() == 0) {
    			errorMsg = "상품정보가 없습니다.";
    			throw new BusinessException("상품정보가 없습니다.");
    		}
            wishlistService.removeWishlist(wishlist);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch (RuntimeException e) {
            if (errorMsg.equals("상품정보가 없습니다.")) {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            } else {
                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            }
        }
        return result;
    }
    


    @GetMapping("/getUserCntrPoint")
    public ResponseEntity getGntrPoint(){
        ResponseEntity result = null;

        try {
        	long userId = UserUtils.getUser().getUserId();
        	
        	List<OrderGivePoint> pointList;
        	if (userId > 0) {
        		pointList = orderGivePointService.getGiveBlcePointListByUserId(userId);
        	} else {
        		pointList = new ArrayList<>();
        	}

            // 화면 리턴용 api-domain ex) NoticeInfo.java
            List<CntrPointInfo> infoList = new ArrayList<>();

            if(!pointList.isEmpty()) {
            	for(OrderGivePoint cntrPoint : pointList) {
            		CntrPointInfo info = new CntrPointInfo();
            		info.setLocgovCode(cntrPoint.getCntrLocgovCode());
            		info.setCntrSn(cntrPoint.getCntrSn());
            		info.setCntrPoint(String.valueOf(cntrPoint.getCntrPoint()));
            		info.setCntrUsePoint(String.valueOf(cntrPoint.getCntrUsePoint()));
            		info.setCntrBlcePoint(String.valueOf(cntrPoint.getCntrBlcePoint()));
            		infoList.add(info);
            	}
            }

            result = ApiResponseEntity.data()
            		.list(infoList)
            		.ok();
            
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        } catch(Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }


    /**
     * 상품리스트 조회 API (categoryCode, orderBy, sort, page, itemsPerPage, listType)
     * 국정자원진단 제시 코드 적용
     *
     * @param itemParam
     * @return
     */
    @GetMapping("/list/list-new")
    public ResponseEntity listNew(ItemParam itemParam, HttpServletRequest request) {

        if (itemFrontService.isItemRestrict()) {
        	return ApiResponseEntity.error(ApiError.RESTRICT_ITEM);
        }
    	
        ResponseEntity result = null;
        List<ItemList> resultList = null;
        Categories category = null;
        String categoryClass = "";

        if (itemParam == null) {
            itemParam = new ItemParam();
        }

        try {

	    	if (!StringUtils.isEmpty(itemParam.getCategory())) {
	    		CategoryParam categoryParam = new CategoryParam();
	    		categoryParam.setCategory(itemParam.getCategory());
	    		Categories categories = categoriesService.getCategoryLevelClassByCategoryParam(categoryParam);
	    		if (categories == null) {
	    			throw new RuntimeException();
	    		}
	    		itemParam.setCategoryLevel(categories.getCategoryLevel());
	    		itemParam.setCategoryClass1(categories.getCategoryClass1());
	    		itemParam.setCategoryClass2(categories.getCategoryClass2());
	    		itemParam.setCategoryClass3(categories.getCategoryClass3());
	    		itemParam.setCategoryClass4(categories.getCategoryClass4());
	    	}

	    	// kdj 수정
	        if (StringUtils.isNull(request.getParameter("itemsPerPage")) || StringUtils.isEmpty(request.getParameter("itemsPerPage"))) {
	            itemParam.setItemsPerPage(12);
	        } else {
	        	itemParam.setItemsPerPage(Integer.parseInt(request.getParameter("itemsPerPage")));
	        }

	        // 사용자단에 노출될 상품 조회에 필요한 기본적인 itemParam bind
	        itemParam = ItemUtils.bindItemParam(itemParam);

	        // 필터 값이 존재 할 경우
	        if(!"".equals(itemParam.getFcIds()) && itemParam.getFcIds() != null){
	            itemParam.setFilterCodeIds(itemParam.getFcIds().split("N"));
	        }

	        if(itemParam.isHistoryPage()){
	            itemParam.setPagination(null);
	            itemParam.setLimit(itemParam.getPage()*itemParam.getItemsPerPage());
	        }

	        List<Item> itemList = itemFrontService.getItemListNew(itemParam);
	        wishlistService.setWishlistFlagByItem(itemList);

	        resultList = itemDataSupport.resultItemListInfo(itemList);

	        String locgovPcImage = "";
	        String locgovMbImage = "";
	        if (itemParam != null && !StringUtils.isEmpty(itemParam.getLocgov())) {
		        LocgovItemImage locgovItemImage = locgovService.getLocgovItemImage(itemParam.getLocgov().replaceAll("U", ""));
		        if (locgovItemImage != null) {
		        	locgovPcImage = "/upload/locgovItem/" + locgovItemImage.getLocgovCode() + "/" + locgovItemImage.getPcFileName();
		        	locgovMbImage = "/upload/locgovItem/" + locgovItemImage.getLocgovCode() + "/" + locgovItemImage.getMobileFileName();
//		        	locgovPcImage = "http://localhost:8080/upload/locgovItem/" + locgovItemImage.getLocgovCode() + "/" + locgovItemImage.getPcFileName();			// 로컬 테스트용
//		        	locgovMbImage = "http://localhost:8080/upload/locgovItem/" + locgovItemImage.getLocgovCode() + "/" + locgovItemImage.getMobileFileName();		// 로컬 테스트용
		        }
	        }
	        
	        result = ApiResponseEntity.data().list(resultList).pagination(itemParam.getPagination())
	        		.put("adultYn", UserUtils.isAdult() ? "Y" : "N")
	        		.put("locgovPcImage", locgovPcImage)
	        		.put("locgovMbImage", locgovMbImage)
	        		.ok();


        } catch (RuntimeException e) {
//        	e.printStackTrace();
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }
    
    
}
