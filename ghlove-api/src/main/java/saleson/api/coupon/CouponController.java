package saleson.api.coupon;

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
import saleson.api.coupon.domain.CouponInfo;
import saleson.common.utils.PointUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.coupon.CouponService;
import saleson.shop.coupon.domain.Coupon;
import saleson.shop.coupon.domain.CouponOffline;
import saleson.shop.coupon.domain.CouponUser;
import saleson.shop.coupon.support.CouponParam;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.order.OrderService;
import saleson.shop.order.support.OrderParam;
import saleson.shop.point.PointService;
import saleson.shop.point.domain.AvailablePoint;
import saleson.shop.point.domain.Point;
import saleson.shop.point.domain.PointUsed;
import saleson.shop.point.support.PointParam;
import saleson.shop.user.domain.UserDetail;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController("ApiCouponController")
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PointService pointService;

    /**
     * 쿠폰 리스트 API (complete, page, itemsPerPage)
     * @Param request
     * @param userCouponParam
     * @return
     */
    @GetMapping("")
    public ResponseEntity list(HttpServletRequest request, UserCouponParam userCouponParam){

        ResponseEntity result = null;
        Pagination pagination = null;
        List<CouponUser> list = null;
        boolean complete = false;

        if (userCouponParam == null) {
            userCouponParam = new UserCouponParam();
        }

        complete = ObjectUtils.isEmpty(request.getParameter("complete")) ?
                userCouponParam.isComplete() : Boolean.parseBoolean(request.getParameter("complete"));

        userCouponParam.setUserId(UserUtils.getUserId());

        int totalCount = 0;
        int completedCount = 0;

        try {
            completedCount = couponService.getCompletedUserCouponCountByUserCouponParam(userCouponParam);

            if (complete) {
                // 사용한 쿠폰 리스트
                pagination = Pagination.getInstance(completedCount, userCouponParam.getItemsPerPage());
                userCouponParam.setPagination(pagination);
                list = couponService.getCompletedUserCouponListByUserCouponParam(userCouponParam);
                result = ApiResponseEntity.data().list(list).pagination(pagination).put("completedUserCouponCount",completedCount).ok();
            } else {
                // 사용가능한 쿠폰 리스트
                totalCount = couponService.getDownloadUserCouponCountByUserCouponParam(userCouponParam);
                pagination = Pagination.getInstance(totalCount, userCouponParam.getItemsPerPage());
                userCouponParam.setPagination(pagination);
                list = couponService.getDownloadUserCouponListByUserCouponParam(userCouponParam);
                result = ApiResponseEntity.data().list(list).pagination(pagination).put("completedUserCouponCount",completedCount).ok();
            }
        } catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }


    /**
     * 쿠폰 다운로드 API (couponId)
     * @param couponUser
     * @return
     */
    @PostMapping("/download")
    public ResponseEntity downloadCoupon(@RequestBody(required = false) CouponUser couponUser){
        ResponseEntity result = null;
        try {
            UserCouponParam userCouponParam = new UserCouponParam();
            userCouponParam.setCouponId(couponUser.getCouponId());
            if (couponService.userCouponDownload(userCouponParam) == 0) {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_FAIL_DOWNLOAD_COUPON);
            } else {
                result = ApiResponseEntity.data().put("status", HttpStatus.OK).put("message", "쿠폰이 다운로드 되었습니다.").ok();
            }
        } catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/shipping-coupons")
    public ResponseEntity shippingCoupon(HttpServletRequest request, Model model, PointParam pointParam){
        ResponseEntity result = null;
        Map<String, Object> resultMap = new HashMap<>();
        List<Point> earnCouponList = null;
        List<PointUsed> useCouponList = null;

        if(pointParam == null){
            pointParam = new PointParam();
        }
        pointParam.setPointType("shipping");
        pointParam.setUserId(UserUtils.getUserId());

        String conditionType = pointParam.getConditionType()==null?"":pointParam.getConditionType();
        pointParam.setConditionType(ShopUtils.PAGE_MOBILE_DEFAULT_CONDITION_TYPE);
        int totalCount = 0;

        try {

            Pagination pagination = null;

            if(conditionType.equals("EARN_COUPON")){
                totalCount = pointService.getPointCountByParam(pointParam);
                pagination = Pagination.getInstance(totalCount, pointParam.getItemsPerPage());
                ShopUtils.setPaginationInfo(pagination, pointParam.getConditionType(), pointParam.getPage());
                pointParam.setPagination(pagination);
                earnCouponList = pointService.getPointListByParam(pointParam);
            } else {
                totalCount = pointService.getPointUsedCountByParam(pointParam);
                pagination = Pagination.getInstance(totalCount, pointParam.getItemsPerPage());
                ShopUtils.setPaginationInfo(pagination, pointParam.getConditionType(), pointParam.getPage());
                pointParam.setPagination(pagination);
                useCouponList = pointService.getPointUsedListByParam(pointParam);
            }

            AvailablePoint avilableShippingCoupon = pointService.getAvailablePointByUserId(UserUtils.getUserId(), PointUtils.SHIPPING_COUPON_CODE);

            resultMap.put("userShippingCount", avilableShippingCoupon.getAvailablePoint());
            resultMap.put("expirationShippingCouponCount", pointService.getNextMonthExpirationPointAmountByParam(pointParam));

            if(conditionType.equals("EARN_COUPON")){
                result = ApiResponseEntity.data().list(earnCouponList).put("shippingCouponInfo",resultMap).pagination(pagination).ok();
            } else {
                result = ApiResponseEntity.data().list(useCouponList).put("shippingCouponInfo",resultMap).pagination(pagination).ok();
            }

        } catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    // 적용가능상품 확인
    @GetMapping("/applies-to/{couponId}/{target}")
    public ResponseEntity appliesTo(Model model, @PathVariable("couponId") int couponId,
                                    @PathVariable("target") String target){
        ResponseEntity result = null;
        try {

            CouponParam couponParam = new CouponParam();
            couponParam.setCouponId(couponId);

            int totalCount = 0;
            Pagination pagination = null;
            Coupon coupon = null;

            if ("coupon-user".equals(target)) {
                totalCount = couponService.getCouponAppliesItemCountParamForCouponUser(couponParam);

                pagination = Pagination.getInstance(totalCount, couponParam.getItemsPerPage());
                couponParam.setPagination(pagination);

                coupon = couponService.getCouponAppliesItemListParamForCouponUser(couponParam);
            } else {
                totalCount = couponService.getCouponAppliesItemCountParamForCoupon(couponParam);

                pagination = Pagination.getInstance(totalCount, couponParam.getItemsPerPage());
                couponParam.setPagination(pagination);

                coupon = couponService.getCouponAppliesItemListParamForCoupon(couponParam);
            }

            for (Item item : coupon.getItems()) {
                if (!ObjectUtils.isEmpty(item.getItemImage())) {
                    item.setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItemImage(), "M"));
                }
            }

            model.addAttribute("coupon", coupon);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("pagination", pagination);
            result = ApiResponseEntity.data().put("couponItems",model).ok();
        }catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    // 적용가능상품 확인
    @GetMapping("/exchange-offline-coupon")
    public ResponseEntity offlineCoupon(@RequestParam(name="offlineCode", defaultValue = "") String offlineCode){
        ResponseEntity result = null;
        try {
            long userId = UserUtils.getUserId();

            if (couponService.downloadDirectInputCoupon(offlineCode)) {
                return ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
            }

            //오프라인 쿠폰 전환시 전환 가능 확인
            offlineCode = couponService.getOfflineCode(offlineCode);

            CouponOffline couponOffline = new CouponOffline();
            couponOffline.setCouponOfflineCode(offlineCode);
            couponOffline.setUserId(userId);
            couponOffline = couponService.getCouponOfflineByOfflineCode(couponOffline);
            if (org.springframework.util.ObjectUtils.isEmpty(couponOffline)) {
//                return JsonViewUtils.failure(MessageUtils.getMessage("M00297")); // 조회된 데이터가 없습니다.
                result = ApiResponseEntity.data().put("status",ApiError.BAD_REQUEST_FAIL_DOWNLOAD_COUPON).ok();
            } else {
                //사용가능하도록 업데이트 상태 업데이트 후 사용가능 쿠폰에 넣기
                couponOffline.setUserId(userId);
                couponService.updateCouponOffline(couponOffline);

                UserCouponParam userCouponParam = new UserCouponParam();
                userCouponParam.setCouponId(couponOffline.getCouponId());
                userCouponParam.setViewTarget("offlineCoupon"); //쿼리에서 where 조건 구분하기 위해 viewTarget사용
                if (couponService.userCouponDownload(userCouponParam) == 0) {
//                    return JsonViewUtils.exception("쿠폰이 다운로드 되지 않았습니다.");
                    result = ApiResponseEntity.data().put("status",ApiError.BAD_REQUEST_FAIL_DOWNLOAD_COUPON).ok();
                } else {
                    result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
                }

            }
        }catch(RuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }

    @GetMapping("/download-coupons")
    public ResponseEntity downloadCoupons(UserCouponParam userCouponParam) {
        ResponseEntity result = null;

        try {

            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.NOT_VALID_LOGIN);
            }

            UserDetail userDetail = UserUtils.getUserDetail();

            userCouponParam.setUserId(UserUtils.getUserId());
            userCouponParam.setUserLevelId(userDetail.getLevelId());

            //orderCount로 첫구매인지 아닌지 판단
            OrderParam orderParam = new OrderParam();
            orderParam.setUserId(userCouponParam.getUserId());
            int orderCount = orderService.getOrderCountByParam(orderParam);
            userCouponParam.setOrderCount(orderCount);
            userCouponParam.setViewTarget("list"); // 자동발행 쿠폰은 목록에서 보이지 않도록 구분[2017-09-18]minae.yun

            int totalCount = couponService.getUserDownloadableCouponListCountByParam(userCouponParam);
            Pagination pagination
                    = Pagination.getInstance(totalCount, userCouponParam.getItemsPerPage());
            userCouponParam.setPagination(pagination);

            List<Coupon> coupons = couponService.getUserDownloadableCouponListByParam(userCouponParam);
            List<CouponInfo> list = new ArrayList<>();

            if (coupons != null && !coupons.isEmpty()) {

                coupons.forEach(c->{
                    list.add(new CouponInfo(c));
                });

            }

            result = ApiResponseEntity.data().list(list).pagination(pagination).put("status", HttpStatus.OK).ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }


    @PostMapping("/download-all-coupons")
    public ResponseEntity downloadAllCoupons(@RequestBody UserCouponParam userCouponParam) {
        ResponseEntity result = null;

        if (userCouponParam == null) {
            return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        try {

            if (!UserUtils.isUserLogin()) {
                return ApiResponseEntity.error(ApiError.NOT_VALID_LOGIN);
            }

            UserDetail userDetail = UserUtils.getUserDetail();

            userCouponParam.setPagination(null);
            userCouponParam.setUserId(UserUtils.getUserId());
            userCouponParam.setUserLevelId(userDetail.getLevelId());
            itemService.setDownloadableCouponListPagination(userCouponParam);
            List<Coupon> coupons = couponService.getUserDownloadableCouponListByParam(userCouponParam);

            int errorCount = 0;
            int downloadCount = 0;
            for (Coupon c : coupons) {
                UserCouponParam downloadCouponParam = new UserCouponParam();
                downloadCouponParam.setCouponId(c.getCouponId());
                if (couponService.userCouponDownload(downloadCouponParam) == 0) {
                    errorCount++;
                } else {
                    downloadCount++;
                }
            }

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("downloadCount", downloadCount);
            map.put("errorCount", errorCount);
            map.put("status", HttpStatus.OK);

            result = ApiResponseEntity.data().map(map).ok();

        } catch (RuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
        return result;
    }
}
