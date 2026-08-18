package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class ContributionSetupSearchParam extends SearchParam {
	
	private String stdrYear;			// 기준 년도
	private String locgovCode;			// 지자체 코드
	private Integer lmtAmt;				// 한도 금액
	private Double pointRate;			// 포인트 비율
	private Integer pointValidPd;		// 포인트 유효 기간
	private Long frstRegisterId;		// 최초 등록자 ID
	private String frstRegistPnttm;		// 최초 등록 시점
	private Long lastUpdusrId;			// 최종 수정자 ID
	private String lastUpdtPnttm;		// 최종 수정 시점
	
	public String getStdrYear() {
		return stdrYear;
	}
	public void setStdrYear(String stdrYear) {
		this.stdrYear = stdrYear;
	}
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public Integer getLmtAmt() {
		return lmtAmt;
	}
	public void setLmtAmt(Integer lmtAmt) {
		this.lmtAmt = lmtAmt;
	}
	public Double getPointRate() {
		return pointRate;
	}
	public void setPointRate(Double pointRate) {
		this.pointRate = pointRate;
	}
	public Integer getPointValidPd() {
		return pointValidPd;
	}
	public void setPointValidPd(Integer pointValidPd) {
		this.pointValidPd = pointValidPd;
	}
	public Long getFrstRegisterId() {
		return frstRegisterId;
	}
	public void setFrstRegisterId(Long frstRegisterId) {
		this.frstRegisterId = frstRegisterId;
	}
	public String getFrstRegistPnttm() {
		return frstRegistPnttm;
	}
	public void setFrstRegistPnttm(String frstRegistPnttm) {
		this.frstRegistPnttm = frstRegistPnttm;
	}
	public Long getLastUpdusrId() {
		return lastUpdusrId;
	}
	public void setLastUpdusrId(Long lastUpdusrId) {
		this.lastUpdusrId = lastUpdusrId;
	}
	public String getLastUpdtPnttm() {
		return lastUpdtPnttm;
	}
	public void setLastUpdtPnttm(String lastUpdtPnttm) {
		this.lastUpdtPnttm = lastUpdtPnttm;
	}
}
