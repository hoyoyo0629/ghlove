package saleson.shop.give.statistics.domain;

import lombok.Data;

@Data
public class GiveStatisticsPersonal {
	
	private String userId;
	private String userName;
	private String loginId;
	private String giveCnt;
	private String giveAmt;
	private String givePoint;
	
	private String sttemntPayDe;
	private String elctrnPayNo;
	private String cntrPathName;
	
}
