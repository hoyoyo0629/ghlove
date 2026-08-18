package saleson.shop.item.domain;

import java.util.List;

public class ItemSet {
    private long itemSetId;
    private int parentItemId;
    private int itemId;
    private int quantity;
    private int ordering;
    private String createdDate;

    // 세트상품에 속한 상품정보
    private Item item;

    public long getItemSetId() {
        return itemSetId;
    }

    public void setItemSetId(long itemSetId) {
        this.itemSetId = itemSetId;
    }

    public int getParentItemId() {
        return parentItemId;
    }

    public void setParentItemId(int parentItemId) {
        this.parentItemId = parentItemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getSoldOut() {
        // 1. 상품 검사
        Item item = this.item;

        if (item != null) {
            // 1.1. 공개 유무
            if ("N".equals(item.getDisplayFlag())) {
                return "1";
            }

            // 1.2. 데이터 상태코드 (1: 정상, 20:등록신청, 21: 등록반려, 30:수정신청, 31: 수정반려, 40: 삭제신청, 41:삭제 반려, 90:판매종료, 99:삭제)
            if (!"1".equals(item.getDataStatusCode())) {
                return "1";
            }

            // 1.3. 재고 체크 (재고가 0이거나, 세트수량 > 재고일 경우)
            if ("Y".equals(item.getStockFlag()) && item.getStockQuantity() > -1) {
                if (item.getStockQuantity() == 0 || (this.quantity > item.getStockQuantity())) {
                    return "1";
                }
            }

            // 1.4. 품절여부
            if ("Y".equals(item.getItemSoldOutFlag())) {
                return "1";
            }

            // 2. 상품 옵션 검사 (최소 1개 이상이 정상 상태일 경우 품절 체크 X)
            if ("Y".equals(this.item.getItemOptionFlag())) {
                boolean isSoldOut = true;
                List<ItemOption> itemOptions = this.item.getItemOptions();

                if (itemOptions != null && !itemOptions.isEmpty()) {
                    for (ItemOption itemOption : itemOptions) {
                        // 2.1. 세트 옵션 > 옵션 수량인 옵션 품절 처리
                        if ("Y".equals(itemOption.getOptionStockFlag()) && itemOption.getOptionStockQuantity() > -1
                                && this.quantity > itemOption.getOptionStockQuantity()) {
                            itemOption.setOptionSoldOutFlag("Y");
                        }

                        // 2.2. 옵션 품절, 노출 여부 체크
                        if (!itemOption.isSoldOut() && "Y".equals(itemOption.getOptionDisplayFlag())) {
                            isSoldOut = false;
                        }
                    }
                }

                if (isSoldOut) {
                    return "1";
                }
            }
        }

        return "0";
    }
}
