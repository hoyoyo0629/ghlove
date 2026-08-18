package saleson.api.display.domain;


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
            setSalePrice(item.getSalePrice());
            setPresentPrice(item.getPresentPrice());
            setItemSoldOutFlag(item.getItemSoldOutFlag());

            if (!ObjectUtils.isEmpty(item.getItemImage())) {
                setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItemImage(), "M"));
            }

            setItemLabel(item.getItemLabel());
            setItemType1(item.getItemType1());
            setItemType2(item.getItemType2());
            setItemType3(item.getItemType3());
            setItemType4(item.getItemType4());
            setItemType5(item.getItemType5());
            setItemNewFlag(item.getItemNewFlag());

            setItemType1(CommonUtils.dataNvl(item.getItemType1()));
            setItemType2(CommonUtils.dataNvl(item.getItemType2()));
            setItemType3(CommonUtils.dataNvl(item.getItemType3()));
            setItemType4(CommonUtils.dataNvl(item.getItemType4()));
            setItemType5(CommonUtils.dataNvl(item.getItemType5()));

            setTotalDiscountAmount(item.getTotalDiscountAmount());
            setDiscountRate(item.getDiscountRate());
            setUserLevelDiscountRate(item.getUserLevelDiscountRate());
            setItemOptionFlag(item.getItemOptionFlag());
            setStockFlag(item.getStockFlag());
            setStockQuantity(item.getStockQuantity());
            setOrderMinQuantity(item.getOrderMinQuantity());

            setBrand(item.getBrand());
        }

    }
    // 상품 ID
    private Integer itemId;

    // 상품코드
    private String itemUserCode;

    // 상품명
    private String itemName;

    // 판매가
    private int salePrice;

    // 할인적용 판매가
    private int presentPrice;

    // 품절여부
    private String itemSoldOutFlag;

    // 상품대표이미지
    private String itemImage;

    private String itemLabel = "0";

    private String itemType1;
    private String itemType2;
    private String itemType3;
    private String itemType4;
    private String itemType5;
    private String itemNewFlag = "N";

    private int totalDiscountAmount;    // 할인금액
    private int discountRate;           // 총 할인율
    private float userLevelDiscountRate; // 회원 등급별 할인
    private String itemOptionFlag;      // 상품옵션 사용유무
    private String stockFlag;           // 재고 연동 여부
    private int stockQuantity;          // 상품 재고
    private int orderMinQuantity;       // 최소 구매 수량

    private String brand;               // 브랜드

}
