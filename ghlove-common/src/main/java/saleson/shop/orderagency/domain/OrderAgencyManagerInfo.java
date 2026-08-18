package saleson.shop.orderagency.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderAgencyManagerInfo {

	// 지자체 명
	private String locgovNm;
	
	// 관리센터 / 권한 명
	private long pbadmsWlfrCntrId;
	
	// 관리센터 / 권한 명
	private String pbadmsWlfrCntrNm;
	
	// 관리자 명
	private String userName;
	
}
