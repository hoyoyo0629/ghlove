package saleson.common.enumeration;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum OrderLogType implements CodeMapperType {

    WAITING_DEPOSIT("WAITING_DEPOSIT", "입금 대기", "입금 대기 FlOW"),
    ORDER_PAYMENT("ORDER_PAYMENT", "주문 결제", "주문 결제 FlOW"),
    ORDER_SHIPPING("ORDER_SHIPPING", "주문 배송", "주문 배송 FlOW"),
    ORDER_SHIPPING_READY("ORDER_SHIPPING_READY", "주문 배송 준비중(리얼커머스)", "주문 배송 FlOW"),
    ORDER_SHIPPING_COMPLETE("ORDER_SHIPPING_COMPLETE", "주문 배송완료", "주문 배송 FlOW"),
    ORDER_CONFIRM("ORDER_CONFIRM", "주문 구매확정", "주문 구매확정 FlOW"),
    ORDER_REFUND("ORDER_REFUND", "주문 환불", "주문 환불 FlOW"),
    CLAIM_CANCEL("CLAIM_CANCEL", "주문 취소", "주문 취소 FlOW"),
    CLAIM_RETURN("CLAIM_RETURN", "주문 반품", "주문 반품 FlOW"),
    CLAIM_EXCHANGE("CLAIM_EXCHANGE", "주문 교환", "주문 교환 FlOW"),
    ORDER_BATCH("ORDER_BATCH", "주문 BATCH", "주문 BATCH FlOW"),
    SHIPPING_BATCH("SHIPPING_BATCH", "배송 BATCH", "배송 BATCH FlOW"),
    
	// 리얼커머스 로그로 인해 추가
    D("D", "결제대기(리얼커머스)", "결제대기(리얼커머스)"),
    G("G", "결제완료(리얼커머스)", "결제완료(리얼커머스)"),
    H("H", "배송준비중(리얼커머스)", "배송준비중(리얼커머스)"),
    K("K", "배송중(리얼커머스)", "배송중(리얼커머스)"),
    N("N", "배송완료(리얼커머스)", "배송완료(리얼커머스)"),
    Q("Q", "취소(리얼커머스)", "취소(리얼커머스)"),
    Q_Q1("Q_Q1", "취소접수(리얼커머스)", "취소접수(리얼커머스)"),
    Q_Q2("Q_Q2", "취소완료(리얼커머스)", "취소완료(리얼커머스)"),
    Q_Q3("Q_Q3", "관리자취소(리얼커머스)", "관리자취소(리얼커머스)"),
    Q_Q4("Q_Q4", "자동취소(리얼커머스)", "자동취소(리얼커머스)"),
    S("S", "교환(리얼커머스)", "교환(리얼커머스)"),
    S_S1("S_S1", "교환접수(리얼커머스)", "교환접수(리얼커머스)"),
    S_S2("S_S2", "반송중(리얼커머스)", "반송중(리얼커머스)"),
    S_S3("S_S3", "재배송중(리얼커머스)", "재배송중(리얼커머스)"),
    S_S4("S_S4", "교환완료(리얼커머스)", "교환완료(리얼커머스)"),
    S_S5("S_S5", "교환보류(리얼커머스)", "교환보류(리얼커머스)"),
    T("T", "반품(리얼커머스)", "반품(리얼커머스)"),
    T_T1("T_T1", "반품접수(리얼커머스)", "반품접수(리얼커머스)"),
    T_T2("T_T2", "반송중(리얼커머스)", "반송중(리얼커머스)"),
    T_T3("T_T3", "반품완료(리얼커머스)", "반품완료(리얼커머스)"),
    T_T4("T_T4", "반품보류(리얼커머스)", "반품보류(리얼커머스)"),
    R("R", "환불(리얼커머스)", "환불(리얼커머스)"),
    R_R1("R_R1", "환불접수(리얼커머스)", "환불접수(리얼커머스)"),
    R_R2("R_R2", "환불완료(리얼커머스)", "환불완료(리얼커머스)"),
    R_R3("R_R3", "환불보류(리얼커머스)", "환불보류(리얼커머스)")
    ;

    OrderLogType(String code, String title, String description) {
        this.code = code;
        this.title = title;
        this.description = description;
    }

    private String code;
    private String title;
    private String description;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

	@Override
	public Boolean isEnabled() {
		return true;
	}
}
