package saleson.shop.statistics.domain;

import lombok.Data;

@Data
public class NhItemSalesStatistics {
	
	/* 사업자 번호 */
	String businessNumber;
	
	/* 로그인 아이디 */
	String loginId;
	
	/* 상호명 */
	String companyName;
	
	/* 지자체 명 */
	String upperLocgovNm;
	
	/* 지자체 명 */
	String locgovNm;
	
	/* 답례품명 */
	String itemName;
	
	/* 카테고리 명 */
	String categoryNms;
	
	/* 하위 카테고리 명 */
	String categoryDetailNms;
	
	/* 가격(단가) */
	long price;
	
	/* 주문 수 */
	long orderCnt;
	
	/* 판매개수 */
	long saleCnt;
	
	/* 총 판매액 */
	long totalSalePrice;
	
	/* 상품코드 */
	String itemUserCode;
	
}
