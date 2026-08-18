package saleson.shop.item.domain.api;

import org.springframework.util.ObjectUtils;
import saleson.common.utils.ShopUtils;
import saleson.shop.giftitem.domain.GiftItemInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemInfo {

    private Integer itemId; // 상품 ID
    private String itemUserCode; // 상품코드
    private String imageSrc; // 상품이미지
    private String itemName; // 상품명
    private String options; // 상품옵션
    // 필수 추가정보
    private String textOption;

	private int optionId;   // 상품옵션 ID
    private int quantity; // 수량
    private int itemAmount; // 상품금액
    private String orderStatus; // 주문 상태코드(59:교환, 반품)
    private String orderStatusLabel;
    private String claimRefusalReasonText; // 취소거절사유
    private String cancelClaimStatus; // 주문취소 클레임 상태코드(03:환불대기, 04:환불완료)
    private String cancelRefusalReasonText; // 취소 거부 사유

    private String deliveryCompanyName; // 배송업체명
    private int deliveryCompanyId;   // 배송업체 ID
    private String deliveryCompanyUrl;  // 배송조회 URL
    private String deliveryNumber;  // 송장번호

    private String brand;   // 브랜드
    private String locgovNm;	// 지자체
    private String locgovCode;	// 지자체

    private String companyName;	// 상호명

    private int earnPoint;
    private String earnPointFlag;

    private int orderSequence;
    private int itemSequence;
    private int shippingSequence;
    private int shippingInfoSequence;
    private String shippingGroupCode;

    private int realShipping;   // 배송비
    private int shipping;   // 기본 배송비
    private boolean singleShipping; // 개별&묶음 구분(개별:true / 묶음:false)
    private int shippingExtraCharge1;
    private int shippingExtraCharge2;

    private String shippingType;    // 배송비 구분 (1: 무료배송, 2: 판매자조건부, 3:출고지조건부, 4:상품조건부, 5:개당배송비, 6:고정배송비)
    private int shippingFreeAmount; // 조건부 무료배송 금액
    private int shippingItemCount; // 박스당 제한수량

    private String itemReturnFlag;

    private int baseAmountForShipping;

    // 사은품
    private String freeGiftName;
    private String freeGiftItemText;
    private List<GiftItemInfo> freeGiftItemList;

    private String setItemFlag = "N";       // 세트상품 여부
    private String setDiscountType;			// 세트상품 할인구분 (1:금액, 2:비율)
    private int setDiscountAmount;			// 세트상품 할인금액(율)
    private String mobileItemYn;			// 모바일 상품여부

    private String telephoneNumber;
    private String phoneNumber;

    public String getMobileItemYn() {
		return mobileItemYn;
	}

	public void setMobileItemYn(String mobileItemYn) {
		this.mobileItemYn = mobileItemYn;
	}

	public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemUserCode() {
        return itemUserCode;
    }

    public void setItemUserCode(String itemUserCode) {
        this.itemUserCode = itemUserCode;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getTextOption() {
		return textOption;
	}

	public void setTextOption(String textOption) {
		this.textOption = textOption;
	}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getClaimRefusalReasonText() {
        return claimRefusalReasonText;
    }

    public void setClaimRefusalReasonText(String claimRefusalReasonText) {
        this.claimRefusalReasonText = claimRefusalReasonText;
    }

    public String getDeliveryCompanyName() {
        return deliveryCompanyName;
    }

    public void setDeliveryCompanyName(String deliveryCompanyName) {
        this.deliveryCompanyName = deliveryCompanyName;
    }

    public int getDeliveryCompanyId() {
        return deliveryCompanyId;
    }

    public void setDeliveryCompanyId(int deliveryCompanyId) {
        this.deliveryCompanyId = deliveryCompanyId;
    }

    public String getDeliveryCompanyUrl() {
        return deliveryCompanyUrl;
    }

    public void setDeliveryCompanyUrl(String deliveryCompanyUrl) {
        this.deliveryCompanyUrl = deliveryCompanyUrl;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public int getOrderSequence() {
        return orderSequence;
    }

    public void setOrderSequence(int orderSequence) {
        this.orderSequence = orderSequence;
    }

    public int getItemSequence() {
        return itemSequence;
    }

    public void setItemSequence(int itemSequence) {
        this.itemSequence = itemSequence;
    }

    public int getShippingSequence() {
        return shippingSequence;
    }

    public void setShippingSequence(int shippingSequence) {
        this.shippingSequence = shippingSequence;
    }

    public int getShippingInfoSequence() {
        return shippingInfoSequence;
    }

    public void setShippingInfoSequence(int shippingInfoSequence) {
        this.shippingInfoSequence = shippingInfoSequence;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getRealShipping() {
        return realShipping;
    }

    public void setRealShipping(int realShipping) {
        this.realShipping = realShipping;
    }

    public String getShippingGroupCode() {
        return shippingGroupCode;
    }

    public void setShippingGroupCode(String shippingGroupCode) {
        this.shippingGroupCode = shippingGroupCode;
    }

    public boolean isSingleShipping() {
        return singleShipping;
    }

    public void setSingleShipping(boolean singleShipping) {
        this.singleShipping = singleShipping;
    }

    public int getShippingExtraCharge1() {
        return shippingExtraCharge1;
    }

    public void setShippingExtraCharge1(int shippingExtraCharge1) {
        this.shippingExtraCharge1 = shippingExtraCharge1;
    }

    public int getShippingExtraCharge2() {
        return shippingExtraCharge2;
    }

    public void setShippingExtraCharge2(int shippingExtraCharge2) {
        this.shippingExtraCharge2 = shippingExtraCharge2;
    }

    public int getShipping() {
        return shipping;
    }

    public void setShipping(int shipping) {
        this.shipping = shipping;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public int getShippingFreeAmount() {
        return shippingFreeAmount;
    }

    public void setShippingFreeAmount(int shippingFreeAmount) {
        this.shippingFreeAmount = shippingFreeAmount;
    }

    public int getShippingItemCount() {
        return shippingItemCount;
    }

    public void setShippingItemCount(int shippingItemCount) {
        this.shippingItemCount = shippingItemCount;
    }

    public int getBaseAmountForShipping() {
        return baseAmountForShipping;
    }

    public void setBaseAmountForShipping(int baseAmountForShipping) {
        this.baseAmountForShipping = baseAmountForShipping;
    }

    public String getItemReturnFlag() {
        return itemReturnFlag;
    }

    public void setItemReturnFlag(String itemReturnFlag) {
        this.itemReturnFlag = itemReturnFlag;
    }

    public String getFreeGiftName() {
        return freeGiftName;
    }

    public void setFreeGiftName(String freeGiftName) {
        this.freeGiftName = freeGiftName;
    }

    public String getFreeGiftItemText() {
        return freeGiftItemText;
    }

    public void setFreeGiftItemText(String freeGiftItemText) {
        this.freeGiftItemText = freeGiftItemText;
    }

    public List<GiftItemInfo> getFreeGiftItemList() {
        return freeGiftItemList;
    }

    public void setFreeGiftItemList(List<GiftItemInfo> freeGiftItemList) {
        this.freeGiftItemList = freeGiftItemList;
    }

    public String getOrderStatusLabel() {
        return orderStatusLabel;
    }

    public void setOrderStatusLabel(String orderStatusLabel) {
        this.orderStatusLabel = orderStatusLabel;
    }

    public int getEarnPoint() {
        return earnPoint;
    }

    public void setEarnPoint(int earnPoint) {
        this.earnPoint = earnPoint;
    }

    public String getEarnPointFlag() {
        return earnPointFlag;
    }

    public void setEarnPointFlag(String earnPointFlag) {
        this.earnPointFlag = earnPointFlag;
    }

    public String getBrand() { return brand; }

    public void setBrand(String brand) { this.brand = brand; }

    public List<Map<String, Object>> getOptionMaps() {

        if (!ObjectUtils.isEmpty(this.options)) {
            return ShopUtils.getOptions(this.options);
        }

        return new ArrayList<>();
    }

    public String getSetDiscountType() {
        return setDiscountType;
    }

    public void setSetDiscountType(String setDiscountType) {
        this.setDiscountType = setDiscountType;
    }

    public int getSetDiscountAmount() {
        return setDiscountAmount;
    }

    public void setSetDiscountAmount(int setDiscountAmount) {
        this.setDiscountAmount = setDiscountAmount;
    }

    public String getSetItemFlag() {
        return setItemFlag;
    }

    public void setSetItemFlag(String setItemFlag) {
        this.setItemFlag = setItemFlag;
    }

	public String getLocgovNm() {
		return locgovNm;
	}

	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}

	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

	public String getCancelClaimStatus() {
		return cancelClaimStatus;
	}

	public void setCancelClaimStatus(String cancelClaimStatus) {
		this.cancelClaimStatus = cancelClaimStatus;
	}

	public String getCancelRefusalReasonText() {
		return cancelRefusalReasonText;
	}

	public void setCancelRefusalReasonText(String cancelRefusalReasonText) {
		this.cancelRefusalReasonText = cancelRefusalReasonText;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
