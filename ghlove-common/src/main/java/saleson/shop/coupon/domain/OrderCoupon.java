package saleson.shop.coupon.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderCoupon extends CouponUser implements Cloneable {
	
	private int discountPrice;
	private int discountAmount;
	
	private List<CouponItem> couponItems;

	// 시큐어 코딩 적용
	public List<CouponItem> getCouponItems() {
//		return couponItems;
		if (couponItems == null) {
			return null;
		} else {
			List<CouponItem> list = new ArrayList<>();
			for (CouponItem couponItem : list) {
				try {
					list.add((CouponItem) couponItem.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
			return list;
		}
	}

	// 시큐어 코딩 적용
	public void setCouponItems(List<CouponItem> couponItems) {
//		this.couponItems = couponItems;
		if (couponItems == null) {
			this.couponItems =  null;
		} else {
			this.couponItems = new ArrayList<>();
			for (CouponItem couponItem : couponItems) {
				try {
					this.couponItems.add((CouponItem) couponItem.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
		}
	}

	public int getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(int discountPrice) {
		this.discountPrice = discountPrice;
	}

	public int getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(int discountAmount) {
		this.discountAmount = discountAmount;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
