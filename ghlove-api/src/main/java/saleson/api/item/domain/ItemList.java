package saleson.api.item.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.shop.item.domain.Item;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemList {

    public ItemList(Item item) {

        if (item != null) {
            setItemId(item.getItemId());
            setItemUserCode(item.getItemUserCode());
            setItemName(item.getItemName());
            setItemDataType(item.getItemDataType());
            setItemType(item.getItemType());
            setSalePrice(item.getSalePrice());
            setPresentPrice(item.getPresentPrice());

            setItemSoldOutFlag(item.getItemSoldOutFlag());

            if (!ObjectUtils.isEmpty(item.getItemImage())) {
                setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItemImage(), "M"));
            }

            setSpotDiscountAmount(item.getSpotDiscountAmount());
            setSpotStartDate(item.getSpotStartDate());
            setSpotEndDate(item.getSpotEndDate());
            setSpotStartTime(item.getSpotStartTime());
            setSpotEndTime(item.getSpotEndTime());
            setSpotDateType(item.getSpotDateType());
            setSpotFlag(item.getSpotFlag());

            setShippingType(item.getShippingType());
            setShippingFreeAmount(item.getShippingFreeAmount());
            setShippingItemCount(item.getShippingItemCount());

            setItemPrice(item.getItemPrice());
            setShipping(item.getShipping());

            setItemLabel(item.getItemLabel());

            setItemType1(CommonUtils.dataNvl(item.getItemType1()));
            setItemType2(CommonUtils.dataNvl(item.getItemType2()));
            setItemType3(CommonUtils.dataNvl(item.getItemType3()));
            setItemType4(CommonUtils.dataNvl(item.getItemType4()));
            setItemType5(CommonUtils.dataNvl(item.getItemType5()));
            setItemNewFlag(item.getItemNewFlag());
            setTotalDiscountAmount(item.getTotalDiscountAmount());
            setDiscountRate(item.getDiscountRate());
            setUserLevelDiscountRate(item.getUserLevelDiscountRate());
            setItemOptionFlag(item.getItemOptionFlag());
            setStockFlag(item.getStockFlag());
            setStockQuantity(item.getStockQuantity());
            setOrderMinQuantity(item.getOrderMinQuantity());
            setBrandId(item.getBrandId());
            setBrand(item.getBrand());

            setWishlistFlag(item.isWishlistFlag());
            
            setLocgovNm(item.getLocgovNm());
            setLocgovCode(item.getLocgovCode());
            setAdultItemYn(item.getAdultItemYn());
        }

    }

    // 상품 ID
    private Integer itemId;

    // 상품코드
    private String itemUserCode;

    // 상품명
    private String itemName;

    // 상품 데이터 형태
    private String itemDataType;

    // 상품구분
    private String itemType;

    // 판매가
    private int salePrice;

    // 할인적용 판매가
    private int presentPrice;

    // 정가
    private String itemPrice;

    // 품절여부
    private String itemSoldOutFlag;

    // 상품대표이미지
    private String itemImage;

    // 스팟할인여부
    private String spotFlag;

    // 스팟할인금액
    private int spotDiscountAmount;

    // 스팟 판매 시작일
    private String spotStartDate;

    // 스팟 판매 종료일
    private String spotEndDate;

    // 스팟 판매 시작 시간
    private String spotStartTime;

    // 스팟 판매 종료 시간
    private String spotEndTime;

    // 스팟 기간 구분 (1: 시점, 2: 기간)
    private String spotDateType;

    // 배송비 구분
    private String shippingType;

    // 배송비
    private int shipping;

    // 조건부 무료배송금액
    private int shippingItemCount;

    // 개당 배송비 부과 시 기준 상품 수량
    private int shippingFreeAmount;

    private String itemLabel = "0";

    private String itemType1;
    private String itemType2;
    private String itemType3;
    private String itemType4;
    private String itemType5;
    private String itemNewFlag = "N";	// 신상품여부


    private int totalDiscountAmount;    // 할인금액
    private int discountRate;           // 총 할인율
    private float userLevelDiscountRate; // 회원 등급별 할인
    private String itemOptionFlag;      // 상품옵션 사용유무
    private String stockFlag;           // 재고 연동 여부
    private int stockQuantity;          // 상품 재고
    private int orderMinQuantity;       // 최소 구매 수량
    private int brandId;                // 브랜드 ID
    private String brand;               // 브랜드

    private boolean wishlistFlag;

    private String locgovNm;		// 자치구 명
    private String locgovCode;		// 자치구 코드
	private String adultItemYn;	// 성인상품여부
}
