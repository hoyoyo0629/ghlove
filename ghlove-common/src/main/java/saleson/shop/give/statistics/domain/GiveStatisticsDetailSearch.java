package saleson.shop.give.statistics.domain;

import lombok.Data;

@Data
public class GiveStatisticsDetailSearch {
	private String fromYear;
	private String toYear;
	private String cntrMonth;
	private String shLocgovCode;
	
	private String shCntrDeStart;
	private String shCntrDeEnd;
	
	private String userId;
	private String itemsOrder;
}
