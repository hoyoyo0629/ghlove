package saleson.shop.statistics.locgov.domain;

import lombok.Data;

@Data
public class StatisticsLocgov {
	private String cntrYear;
	private String upperLocgovCode;
	private String upperLocgovNm;
	private String locgovCode;
	private String locgovNm;
	private String likeCnt;
	private String yearLikeCnt;
	
	private String cntrMonth;
}
