package saleson.shop.pointcheck.domain;

public class PointCheck {
	private String orderCode;
	private String orderAmt;
	private String orderAmtCancel;
	private String orderAmt0;
	private String remittanceAmt;
	private String cntrUsePoint;
	private String matchYn;
	private String createdDate;
	private String holdCnt;
	private String searchStartDate;
    private String searchEndDate;
	
    private String orderStatusDesc;
    private String locgovNm;
    
    
	public String getLocgovNm() {
		return locgovNm;
	}
	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}
	public String getHoldCnt() {
		return holdCnt;
	}
	public void setHoldCnt(String holdCnt) {
		this.holdCnt = holdCnt;
	}
	public String getSearchStartDate() {
		return searchStartDate;
	}
	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}
	public String getSearchEndDate() {
		return searchEndDate;
	}
	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getOrderAmt() {
		return orderAmt;
	}
	public void setOrderAmt(String orderAmt) {
		this.orderAmt = orderAmt;
	}
	public String getOrderAmtCancel() {
		return orderAmtCancel;
	}
	public void setOrderAmtCancel(String orderAmtCancel) {
		this.orderAmtCancel = orderAmtCancel;
	}
	public String getOrderAmt0() {
		return orderAmt0;
	}
	public void setOrderAmt0(String orderAmt0) {
		this.orderAmt0 = orderAmt0;
	}
	public String getRemittanceAmt() {
		return remittanceAmt;
	}
	public void setRemittanceAmt(String remittanceAmt) {
		this.remittanceAmt = remittanceAmt;
	}
	public String getCntrUsePoint() {
		return cntrUsePoint;
	}
	public void setCntrUsePoint(String cntrUsePoint) {
		this.cntrUsePoint = cntrUsePoint;
	}
	public String getMatchYn() {
		return matchYn;
	}
	public void setMatchYn(String matchYn) {
		this.matchYn = matchYn;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getMatchMessage() {
		if("Y".equals(matchYn)) {
			return "금액일치";
		}else if("N".equals(matchYn)){
			return "금액불일치";
		}else {
			return matchYn;
		}
	}
	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}
	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}
	
}
