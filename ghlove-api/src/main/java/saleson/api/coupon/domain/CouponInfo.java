package saleson.api.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.shop.coupon.domain.Coupon;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponInfo {

    private int couponId;
    private String couponPay;
    private String couponPayType;
    private int couponPayRestriction;
    private String couponName;
    private String couponApplyType;
    private int couponApplyDay;
    private String couponApplyStartDate;
    private String couponApplyEndDate;
    private int couponDiscountLimitPrice;
    private String couponTargetItemType;


    public CouponInfo(Coupon coupon) {

        if (coupon != null) {
            setCouponId(coupon.getCouponId());
            setCouponPay(coupon.getCouponPay());
            setCouponPayType(coupon.getCouponPayType());
            setCouponPayRestriction(coupon.getCouponPayRestriction());
            setCouponName(coupon.getCouponName());
            setCouponApplyType(coupon.getCouponApplyType());
            setCouponApplyStartDate(coupon.getCouponApplyStartDate());
            setCouponApplyEndDate(coupon.getCouponApplyEndDate());
            setCouponApplyDay(coupon.getCouponApplyDay());
            setCouponDiscountLimitPrice(coupon.getCouponDiscountLimitPrice());
            setCouponTargetItemType(coupon.getCouponTargetItemType());

        }

    }
}
