package saleson.shop.pointcheck.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class PointCheckParam extends SearchParam {
	private String id;
    private String orderCode;
    private String orderAmt;
    private String orderAmtCancel;
    private String orderAmt0;
    private String remittanceAmt;
    private String cntrUsePoint;
    private String matchYn;
    private String createdDate;
    
    private String triType;
    private String searchDateType;
	private String searchStartDate;
    private String searchEndDate;
    private String searchType;
    private String matchYnType;
    
	public String getMatchYnType() {
		return matchYnType;
	}
	public void setMatchYnType(String matchYnType) {
		this.matchYnType = matchYnType;
	}
	public String getSearchDateType() {
		return searchDateType;
	}
	public void setSearchDateType(String searchDateType) {
		this.searchDateType = searchDateType;
	}
	public String getTriType() {
		return triType;
	}
	public void setTriType(String triType) {
		this.triType = triType;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
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
}
