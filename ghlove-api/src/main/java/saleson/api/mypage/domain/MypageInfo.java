package saleson.api.mypage.domain;

import saleson.shop.order.api.ApiOrderList;
import saleson.shop.coupon.domain.CouponUser;
import saleson.shop.order.domain.OrderCount;
import saleson.shop.userlevel.domain.UserLevel;
import saleson.shop.wishlist.domain.Wishlist;

import java.util.List;

public class MypageInfo {

    // 유저정보
    private String userName;    // 유저이름
    private String loginId;     // 로그인 ID
    private String userLevel;   // 유저 레벨
    private int userPoint;      // 보유 포인트
    private int userCouponCount;    // 보유 쿠폰
    private int userShippingCount;  // 보유 배송지 쿠폰

    List<OrderCount> orderShippingCount;    // 주문/배송 카운트

    List<ApiOrderList> orderList;  // 주문내역 리스트

    // 주문/배송정보
    List<CouponUser> couponList;    // 보유쿠폰 리스트

    // 등급정보
    List<UserLevel> userLevelList;  // 회원등급 리스트

    private List<Wishlist> wishlist;    // 찜 리스트

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public int getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }

    public int getUserCouponCount() {
        return userCouponCount;
    }

    public void setUserCouponCount(int userCouponCount) {
        this.userCouponCount = userCouponCount;
    }

    public int getUserShippingCount() {
        return userShippingCount;
    }

    public void setUserShippingCount(int userShippingCount) {
        this.userShippingCount = userShippingCount;
    }

    public List<CouponUser> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponUser> couponList) {
        this.couponList = couponList;
    }

    public List<Wishlist> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Wishlist> wishlist) {
        this.wishlist = wishlist;
    }

    public List<OrderCount> getOrderShippingCount() {
        return orderShippingCount;
    }

    public void setOrderShippingCount(List<OrderCount> orderShippingCount) {
        this.orderShippingCount = orderShippingCount;
    }

    public List<ApiOrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<ApiOrderList> orderList) {
        this.orderList = orderList;
    }

    public List<UserLevel> getUserLevelList() {
        return userLevelList;
    }

    public void setUserLevelList(List<UserLevel> userLevelList) {
        this.userLevelList = userLevelList;
    }
}
