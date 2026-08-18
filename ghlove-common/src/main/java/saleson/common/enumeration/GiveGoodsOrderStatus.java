package saleson.common.enumeration;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum GiveGoodsOrderStatus implements CodeMapperType {

    WAITING_DEPOSIT("0","입금대기","입금대기"),
    COMPLETE_PAYMENT("10","결제완료","결제완료"),
    PREPARING_DELIVERY("20","배송준비중","배송준비중"),
    SHIPPING_ITEM("30","배송중","배송중"),
    DELIVERY_COMPLETED("35","배송완료","배송완료"),
    PURCHASE_CONFIRMATION("40","구매확정","구매확정"),
    EXCHANGE_PROCESSING("50","교환처리중","교환처리중"),
    EXCHANGE_SHIPPING("55","교환배송중","교환배송중"),
    EXCHANGE_REFUSAL("59","교환거절","교환거절"),
    RETURN_PROCESSING("60","반품처리중","반품처리중"),
    RETURN_SHIPPING("65","반품완료","반품완료"),
    RETURN_REFUSAL("69","반품거절","반품거절"),
    CANCEL_PROCESSING("70","취소처리중","취소처리중"),
    CANCEL_COMPLETED("75","취소완료","취소완료"),
    CANCEL_REFUSAL("79","취소거절","취소거절")
    ;
	

    private String code;
    private String title;
    private String description;

    GiveGoodsOrderStatus(String code, String title, String description) {
        this.code = code;
        this.title = title;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }

	@Override
	public Boolean isEnabled() {
		return true;
	}

	@Override
	public String getDescription() {
		return description;
	}


}
