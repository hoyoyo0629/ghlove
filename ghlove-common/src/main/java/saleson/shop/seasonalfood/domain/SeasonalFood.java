package saleson.shop.seasonalfood.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.onlinepowers.framework.web.domain.SearchParam;

public class SeasonalFood extends SearchParam {

	private static final long serialVersionUID = 6805274008876533821L;

	private int seasonalFoodMonth;
	
	private String seasonalFoodKeyword;
	
	private String upperLocgovCode;
	
	private String locgovCode;
	
	private int regSeq;
	
	private long frstRegisterId;
	
	private Timestamp frstRegistPnttm;
	
	private long lastUpdusrId;
	
	private Timestamp lastUpdtPnttm;

	public SeasonalFood() {
		
	}

	public int getSeasonalFoodMonth() {
		return seasonalFoodMonth;
	}

	public void setSeasonalFoodMonth(int seasonalFoodMonth) {
		this.seasonalFoodMonth = seasonalFoodMonth;
	}

	public String getSeasonalFoodKeyword() {
		return seasonalFoodKeyword;
	}

	public void setSeasonalFoodKeyword(String seasonalFoodKeyword) {
		this.seasonalFoodKeyword = seasonalFoodKeyword;
	}

	public String getUpperLocgovCode() {
		return upperLocgovCode;
	}

	public void setUpperLocgovCode(String upperLocgovCode) {
		this.upperLocgovCode = upperLocgovCode;
	}

	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

	public int getRegSeq() {
		return regSeq;
	}

	public void setRegSeq(int regSeq) {
		this.regSeq = regSeq;
	}

	public long getFrstRegisterId() {
		return frstRegisterId;
	}

	public void setFrstRegisterId(long frstRegisterId) {
		this.frstRegisterId = frstRegisterId;
	}

	public Timestamp getFrstRegistPnttm() {
		return frstRegistPnttm;
	}

	public void setFrstRegistPnttm(Timestamp frstRegistPnttm) {
		this.frstRegistPnttm = frstRegistPnttm;
	}

	public long getLastUpdusrId() {
		return lastUpdusrId;
	}

	public void setLastUpdusrId(long lastUpdusrId) {
		this.lastUpdusrId = lastUpdusrId;
	}

	public Timestamp getLastUpdtPnttm() {
		return lastUpdtPnttm;
	}

	public void setLastUpdtPnttm(Timestamp lastUpdtPnttm) {
		this.lastUpdtPnttm = lastUpdtPnttm;
	}
	
}
