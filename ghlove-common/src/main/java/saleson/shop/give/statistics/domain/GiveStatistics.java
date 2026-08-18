package saleson.shop.give.statistics.domain;

import lombok.Data;

@Data
public class GiveStatistics {
	
	private String cntrYear;
	private String cntrMonth;
	private String cntrDate;
	private String cntrDe;
	private String upperLocgovCode;
	private String upperLocgovNm;
	private String locgovCode;
	private String locgovNm;
	private String cntrAmt;
	private String givePersons;
	private String giveCnt;
	
	private String giveAmt;
	private String givePoint;
	
	private String percent;
	
	private String onlineCnt;
	private String offlineCnt;
	
	private String ageGroup;
	private String cntrHour;
	
}
