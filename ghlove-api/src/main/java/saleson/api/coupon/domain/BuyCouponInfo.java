package saleson.api.coupon.domain;

public class BuyCouponInfo {

    private String couponConcurrently;
    private String couponKey;
    private String couponType;
    private int couponUserId;
    private int discountAmount;
    private int discountPrice;
    private int itemSequence;
    private String key;
    private int shippingIndex;

    public String getCouponConcurrently() {
        return couponConcurrently;
    }

    public void setCouponConcurrently(String couponConcurrently) {
        this.couponConcurrently = couponConcurrently;
    }

    public String getCouponKey() {
        return couponKey;
    }

    public void setCouponKey(String couponKey) {
        this.couponKey = couponKey;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public int getCouponUserId() {
        return couponUserId;
    }

    public void setCouponUserId(int couponUserId) {
        this.couponUserId = couponUserId;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getItemSequence() {
        return itemSequence;
    }

    public void setItemSequence(int itemSequence) {
        this.itemSequence = itemSequence;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getShippingIndex() {
        return shippingIndex;
    }

    public void setShippingIndex(int shippingIndex) {
        this.shippingIndex = shippingIndex;
    }
}
