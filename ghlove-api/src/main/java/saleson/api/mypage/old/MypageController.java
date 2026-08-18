package saleson.api.mypage.old;

import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.shop.item.domain.api.ItemInfo;
import saleson.api.mypage.domain.ReviewFilterInfo;
import saleson.api.mypage.domain.ItemReviewInfo;
import saleson.api.mypage.domain.MypageInfo;
import saleson.api.mypage.domain.UserLevelInfo;
import saleson.shop.order.api.ApiOrderList;
import saleson.common.utils.PointUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.FilterGroup;
import saleson.model.review.ItemReviewFilter;
import saleson.shop.coupon.CouponService;
import saleson.shop.coupon.domain.CouponUser;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemReview;
import saleson.shop.item.support.ItemParam;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderCount;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.support.OrderParam;
import saleson.shop.point.PointService;
import saleson.shop.point.domain.AvailablePoint;
import saleson.shop.point.domain.Point;
import saleson.shop.point.domain.PointUsed;
import saleson.shop.point.support.PointParam;
import saleson.shop.qna.QnaService;
import saleson.shop.reviewfilter.ReviewFilterService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.userlevel.UserLevelService;
import saleson.shop.userlevel.domain.UserLevel;
import saleson.shop.userlevel.support.UserLevelSearchParam;
import saleson.shop.wishlist.WishlistService;
import saleson.shop.wishlist.domain.Wishlist;
import saleson.shop.wishlist.support.WishlistListParam;
import saleson.shop.wishlist.support.WishlistParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController("ApiMypageController2")
@RequestMapping("/api/mypage/2")
public class MypageController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private QnaService qnaService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private UserService userService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private ReviewFilterService reviewFilterService;

    @GetMapping("/wishlist")
    public ResponseEntity wishlist(HttpServletRequest request, WishlistParam wishlistParam) {
        ResponseEntity result = null;
        List<Wishlist> wishlists = null;
        Pagination pagination = null;
        int wishlistCount = 0;
        try {
            wishlistCount = wishlistService.getWishlistCountByUserId(UserUtils.getUserId());
            pagination = Pagination.getInstance(wishlistCount, wishlistParam.getItemsPerPage());
            wishlistParam.setPagination(pagination);
            wishlistParam.setUserId(UserUtils.getUserId());
            wishlists = wishListDataSet(wishlistService.getWishlistList(wishlistParam));
            result = ApiResponseEntity.data().list(wishlists).pagination(pagination).ok();
        }catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    private List<Wishlist> wishListDataSet(List<Wishlist> wishlistList){

        List<Integer> ids = new ArrayList<>();
        List<Item> sItemList = new ArrayList<>();

        List<Wishlist> resultList = new ArrayList<>();

        if (wishlistList != null) {

            // 세트상품 정보 set
            for (Wishlist wishlist : wishlistList) {
                if ("3".equals(wishlist.getItem().getItemType())) {
                    ids.add(wishlist.getItem().getItemId());
                }
            }

            if (ids.size() > 0) {
                sItemList = itemService.getItemListForItemSet(ids);
            }

            for (Wishlist wishlist : wishlistList) {
                wishlist.getItem().setSupplyPrice(0);    // 공급자 0으로 설정
                wishlist.getItem().setItemImage(ShopUtils.loadImage(wishlist.getItem().getItemUserCode(), wishlist.getItem().getItemImage(), "M"));

                if ("3".equals(wishlist.getItem().getItemType()) && sItemList != null && !sItemList.isEmpty()) {
                    for (Item sItem : sItemList) {
                        if (wishlist.getItem().getItemId() == sItem.getItemId()) {
                            wishlist.getItem().setItemSets(sItem.getItemSets());
                            break;
                        }
                    }
                }

                resultList.add(wishlist);
            }
        }
        return resultList;
    }

    @PostMapping("/delete-wishlist")
    public ResponseEntity deleteWishlist(HttpServletRequest request, @RequestBody(required = false) WishlistListParam listParm){
        ResponseEntity result = null;
        try {
            listParm.setUserId(UserUtils.getUserId());
            wishlistService.deleteWishlistByListParam(listParm);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }




    @GetMapping("/points")
    public ResponseEntity pointList(HttpServletRequest request, Model model, PointParam pointParam){
        ResponseEntity result = null;
        List<Point> earnPointList = null;
        List<PointUsed> usePointList = null;
        AvailablePoint avilablePoint = null;
        String pointType = "";

        if(pointParam == null){
            pointParam = new PointParam();
        }



        Map<String, Object> resultMap = new HashMap<>();
        pointType = pointParam.getConditionType() == null ? "" : pointParam.getConditionType();


        pointParam.setConditionType(ShopUtils.PAGE_MOBILE_DEFAULT_CONDITION_TYPE);

        if (pointParam.getPage() == 0) {
            pointParam.setPage(1);
        }
        pointParam.setPointType("point");
        pointParam.setUserId(UserUtils.getUserId());

        int totalCount = 0;
        int expirationPointAmount = 0;

        try {
            Pagination pagination = null;

            // 적립 리스트
            if(pointType.equals("EARN_POINT")){
                totalCount = pointService.getPointCountByParam(pointParam);
                pagination = Pagination.getInstance(totalCount, pointParam.getItemsPerPage());
                ShopUtils.setPaginationInfo(pagination, pointParam.getConditionType(), pointParam.getPage());
                pointParam.setPagination(pagination);
                earnPointList = pointService.getPointListByParam(pointParam);
                // 사용 리스트
            } else {
                totalCount = pointService.getPointUsedCountByParam(pointParam);
                pagination = Pagination.getInstance(totalCount, pointParam.getItemsPerPage());
                ShopUtils.setPaginationInfo(pagination, pointParam.getConditionType(), pointParam.getPage());
                pointParam.setPagination(pagination);
                usePointList = pointService.getPointUsedListByParam(pointParam);
            }
            userService.setMypageUserInfoForFront(model);

            avilablePoint = pointService.getAvailablePointByUserId(UserUtils.getUserId(), PointUtils.DEFAULT_POINT_CODE);
            expirationPointAmount = pointService.getNextMonthExpirationPointAmountByParam(pointParam);

            resultMap.put("userPoint",avilablePoint.getAvailablePoint());
            resultMap.put("expirationPointAmount",expirationPointAmount);

            if(pointType.equals("EARN_POINT")){
                result = ApiResponseEntity.data().list(earnPointList).put("pointInfo", resultMap).pagination(pagination).ok();
            } else {
                result = ApiResponseEntity.data().list(usePointList).put("pointInfo", resultMap).pagination(pagination).ok();
            }
        } catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("")
    public ResponseEntity details(HttpServletRequest request, Model model){
        ResponseEntity result = null;

        try {
            List<OrderCount> orderCount = orderService.getUserOrderCountAll();

            model.addAttribute("user", UserUtils.getUser());

            model.addAttribute("userDetail", UserUtils.getUserDetail());
            model.addAttribute("orderCount", orderCount);

            // 20181122 수정 : 찜록록 추가 (임시사용 향수 변경예정)
            WishlistParam wishlistParam = new WishlistParam();
            invokeWishlistProcessSummary(wishlistParam, model);

            UserCouponParam userCouponParam = new UserCouponParam();
            initDownloadCouponListSummary(userCouponParam, model);
            userService.setMypageUserInfoForFront(model);

            // 주문내역 리스트
            OrderParam orderParam = new OrderParam();
            orderList(orderParam, model);

            int point = 0;
            int userCouponCount = 0;
            int userShippingCount = 0;

            List<Wishlist> wishlist = new ArrayList<>();
            List<CouponUser> couponList = new ArrayList<>();
            List<Order> orderList = new ArrayList<>();
            MypageInfo mInfo = new MypageInfo();
            mInfo.setUserName(UserUtils.getUser().getUserName());
            mInfo.setLoginId(UserUtils.getUser().getLoginId());
            mInfo.setUserLevel(UserUtils.getUserDetail().getUserlevel().getLevelName());

            if(((HashMap)model).get("userPoint") != null){
                point = (Integer)((HashMap)model).get("userPoint");
            }
            if(((HashMap)model).get("userCouponCount") != null){
                userCouponCount = (Integer)((HashMap)model).get("userCouponCount");
            }
            if(((HashMap)model).get("wishlists") != null){
                wishlist = (List<Wishlist>)((HashMap)model).get("wishlists");
            }
            if(((HashMap)model).get("userShippingCount") != null){
                userShippingCount = (Integer)((HashMap)model).get("userShippingCount");
            }
            if(((HashMap)model).get("list") != null){
                couponList = (List<CouponUser>)((HashMap)model).get("list");
            }
            if(((HashMap)model).get("orderList") != null){
                orderList = (List<Order>)((HashMap)model).get("orderList");
            }
            mInfo.setUserPoint(point);
            mInfo.setUserCouponCount(userCouponCount);
            mInfo.setWishlist(orderWishListDataSet(wishlist));
            mInfo.setUserShippingCount(userShippingCount);
            mInfo.setCouponList(couponList);
            mInfo.setOrderList(orderListDataSet(orderList));

            // 주문/배송조회 데이터 셋
            mInfo.setOrderShippingCount(orderService.getUserOrderCountAll());

            result = ApiResponseEntity.data().put("data", mInfo).ok();
        } catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 관심상품 그룹에 해당하는 상품 조회 (임시사용 향후 수정예정)
     * @param wishlistParam
     * @param model
     */
    private void invokeWishlistProcessSummary(WishlistParam wishlistParam, Model model) {

        wishlistParam.setItemsPerPage(4);

        if (wishlistParam.getPage() == 0) {
            wishlistParam.setPage(1);
        }

        wishlistParam.setUserId(UserUtils.getUserId());

        int totalCount = wishlistService.getWishlistCountByParam(wishlistParam);
        Pagination pagination = Pagination.getInstance(totalCount, wishlistParam.getItemsPerPage());

        ShopUtils.setPaginationInfo(pagination, wishlistParam.getConditionType(), wishlistParam.getPage());

        wishlistParam.setPagination(pagination);

        List<Wishlist> wishlists = wishlistService.getWishlistListByParam(wishlistParam);


        model.addAttribute("totalItemCount", totalCount);
        model.addAttribute("wishlists", wishlists);
        model.addAttribute("pagination", pagination);
    }

    /**
     * 보유 쿠폰 내역 (임시 사용 향후 수정예정)
     * @param userCouponParam
     * @param model
     */
    private void initDownloadCouponListSummary(UserCouponParam userCouponParam, Model model) {
        userCouponParam.setItemsPerPage(3);

        if (userCouponParam.getPage() == 0) {
            userCouponParam.setPage(1);
        }

        userCouponParam.setUserId(UserUtils.getUserId());
        int totalCount = couponService.getDownloadUserCouponCountByUserCouponParam(userCouponParam);

        Pagination pagination = Pagination.getInstance(totalCount, userCouponParam.getItemsPerPage());

        ShopUtils.setPaginationInfo(pagination, userCouponParam.getConditionType(), userCouponParam.getPage());

        userCouponParam.setPagination(pagination);

        List<CouponUser> list = couponService.getDownloadUserCouponListByUserCouponParam(userCouponParam);

        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pagination", pagination);
        model.addAttribute("completedUserCouponCount", couponService.getCompletedUserCouponCountByUserCouponParam(userCouponParam));

    }

    private void orderList(OrderParam orderParam, Model model){
        orderParam.setItemsPerPage(10);
        if (orderParam.getPage() == 0) {
            orderParam.setPage(1);
        }
        orderParam.setUserId(UserUtils.getUserId());
        List<Order> orderList = orderService.getOrderListByParam(orderParam);
        model.addAttribute("orderList", orderList);
    }

    private List<Wishlist> orderWishListDataSet(List<Wishlist> wishlistList){
        List<Wishlist> resultList = wishlistList;
        if(wishlistList != null){
            for(int i = 0; i < wishlistList.size(); i++){
                Item item = wishlistList.get(i).getItem();
                item.setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItemImage(), "M"));
            }
        }
        return resultList;
    }

    private List<ApiOrderList> orderListDataSet(List<Order> orderList){
        List<ApiOrderList> resultList = new ArrayList<>();

        if (orderList != null && !orderList.isEmpty()) {
            for(int i = 0; i < orderList.size(); i++){
                ApiOrderList order = new ApiOrderList();
                List<OrderItem> orderItems = orderList.get(i).getOrderShippingInfos().get(0).getOrderItems();
                List<ItemInfo> items = new ArrayList<>();
                order.setOrderCode(orderList.get(i).getOrderCode());
                order.setOrderSequence(orderList.get(i).getOrderSequence());
                order.setCreatedDate(orderList.get(i).getCreatedDate());

                for(int j = 0; j < orderItems.size(); j++){
                    ItemInfo itemInfo = new ItemInfo();
                    itemInfo.setItemUserCode(orderItems.get(j).getItemUserCode());
                    itemInfo.setImageSrc(ShopUtils.loadImage(orderItems.get(j).getImageSrc(), "M"));
                    itemInfo.setItemName(orderItems.get(j).getItemName());
                    itemInfo.setOptions(ShopUtils.viewItemOptions(orderItems.get(j).getSetItemFlag(), orderItems.get(j).getOptions()));
                    itemInfo.setQuantity(orderItems.get(j).getQuantity());
                    itemInfo.setItemAmount(orderItems.get(j).getItemAmount());
                    itemInfo.setOrderStatus(orderItems.get(j).getOrderStatus());
                    itemInfo.setClaimRefusalReasonText(orderItems.get(j).getClaimRefusalReasonText());

                    itemInfo.setDeliveryCompanyId(orderItems.get(j).getDeliveryCompanyId());
                    itemInfo.setDeliveryCompanyName(orderItems.get(j).getDeliveryCompanyName());
                    itemInfo.setDeliveryCompanyUrl(orderItems.get(j).getDeliveryCompanyUrl());
                    itemInfo.setDeliveryNumber(orderItems.get(j).getDeliveryNumber());

                    items.add(itemInfo);
                }
                order.setItems(items);
                resultList.add(order);
            }
        }

        return resultList;
    }

    /**
     * 등록된 이용후기
     *
     * @param searchParam
     * @return
     */
    @GetMapping("reviews")
    public ResponseEntity review(ItemParam searchParam) {

        ResponseEntity result = null;

        try {

            searchParam.setUserId(UserUtils.getUserId());
            searchParam.setConditionType(searchParam.getConditionType());

            int reviewCount = itemService.getItemReviewCountByParam(searchParam);
            Pagination pagination = Pagination.getInstance(reviewCount, searchParam.getItemsPerPage());

            searchParam.setPagination(pagination);

            List<ItemReview> itemReviews = itemService.getItemReviewListByParam(searchParam);
            List<ItemReviewInfo> infos = new ArrayList<>();
            List<ItemReviewFilter> itemReviewFilters = new ArrayList<>();
            List<ReviewFilterInfo> reviewFilterInfos = new ArrayList<>();

            if (itemReviews != null && !itemReviews.isEmpty()) {

                for (ItemReview itemReview : itemReviews) {
                    infos.add(new ItemReviewInfo((itemReview)));

                    List<ItemReviewFilter> tempItemReviewFilters = itemReview.getItemReviewFilters();
                    if (tempItemReviewFilters != null && !tempItemReviewFilters.isEmpty()) {
                        itemReviewFilters.addAll(tempItemReviewFilters);
                    }
                }

                List<FilterGroup> filterGroups = reviewFilterService.getFilterGroupsByItemReviewFilters(itemReviewFilters);

                if (filterGroups != null && !filterGroups.isEmpty()) {
                    filterGroups.forEach(f -> {
                        reviewFilterInfos.add(new ReviewFilterInfo(f));
                    });
                }
            }

            result = ApiResponseEntity.data()
                    .pagination(pagination)
                    .list(infos)
                    .put("reviewFilters", reviewFilterInfos)
                    .ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 미등록 이용후기
     *
     * @param searchParam
     * @return
     */
    @GetMapping("nonregistered-reviews")
    public ResponseEntity reviewNone(ItemParam searchParam) {

        Pagination pagination = null;
        ResponseEntity result = null;

        try {

            searchParam.setUserId(UserUtils.getUserId());

            if (searchParam.isPaging()) {
                int nonReviewCount = itemService.getItemNonregisteredReviewCount(searchParam);

                pagination = Pagination.getInstance(nonReviewCount, searchParam.getItemsPerPage());
                searchParam.setPagination(pagination);
            }

            List<OrderItem> orderItems = itemService.getItemNonregisteredReviewList(searchParam);

            result = ApiResponseEntity.data().list(orderItems).pagination(pagination).ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }


    /**
     * 리뷰삭제처리
     *
     * @param itemReviewId
     * @return
     */
    @PostMapping("delete-review/{itemReviewId}")
    public ResponseEntity deleteReview(@PathVariable("itemReviewId") int itemReviewId) {

        ResponseEntity result = null;

        try {

            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
            }

            ItemReview itemReview = itemService.getItemReviewById(itemReviewId);

            if (itemReview == null) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            if (UserUtils.getUserId() != itemReview.getUserId()) {
                return ApiResponseEntity.error(ApiError.BAD_REQUEST);
            }

            itemService.deleteItemReview(itemReviewId);

            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    /**
     * 회원 그룹
     * @return
     */
    @GetMapping("grade")
    public ResponseEntity grade() {

        ResponseEntity result = null;

        UserLevelSearchParam userLevelSearchParam = new UserLevelSearchParam();
        UserLevelSearchParam groupSearchParam = new UserLevelSearchParam();

        try {

            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.NOT_VALID_USER);
            }

            UserDetail userDetail = UserUtils.getUserDetail();
            userLevelSearchParam.setLevelId(userDetail.getLevelId());

            if (ObjectUtils.isEmpty(userDetail.getGroupCode())) {
                userDetail.setGroupCode("default");
            }

            groupSearchParam.setGroupCode(userDetail.getGroupCode());

            UserLevel userLevel = UserUtils.getUserDetail().getUserlevel();
            List<UserLevel> userLevelList = userLevelService.getUserLevelList(groupSearchParam);
            List<UserLevelInfo> list = new ArrayList<>();

            if (userLevelList != null && !userLevelList.isEmpty()) {
                for (UserLevel level : userLevelList) {
                    list.add(new UserLevelInfo(level));
                }
            }

            Map<String, Object> map = new LinkedHashMap<>();

            map.put("userLevel", userLevel.getLevelName());
            map.put("userLevelList", list);

            result = ApiResponseEntity.data().put("result", map).ok();
        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

}

