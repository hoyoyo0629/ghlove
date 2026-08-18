package saleson.shop.order.domain;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderAgencyOrderLog {
	
	private String orderCode;
	
	private int orderSequence;
	
	private int itemSequence;
	
	private Timestamp regDt;
	
	private long managerId;
	
	private String managerLclgvCd;
	
	private String managerNm;
	
	private long pbadmsWlfrCntrId;
	
	private long userId;
	
}
