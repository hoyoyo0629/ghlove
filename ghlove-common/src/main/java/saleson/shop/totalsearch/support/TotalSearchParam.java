package saleson.shop.totalsearch.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class TotalSearchParam extends SearchParam {
	private String siteNm;
    private String userid;
    private String searchKeyword;
    private String category;
    private int pageNumber;
    private int pageSize;
    private String searchField;
    private String selectFields;
    private String gender;
    
    
    /**
     * KSF 관련 파라미터
     */
    private int domainNo;
    private int maxCount;
    private String akcModes;
	
    public TotalSearchParam(String siteNm, String userid, String searchKeyword, String category, String pageNumber, String pageSize, String searchField, String selectFields, String domainNo, String maxCount, String akcModes) {
        this.siteNm = siteNm!=null?siteNm:"고향사랑기부제";
        this.userid = userid!=null?userid:"";
        this.searchKeyword = searchKeyword;
        this.category = category!=null?category:"total";
        this.pageNumber = pageNumber!=null?Integer.parseInt(pageNumber):1;
        this.pageSize = pageSize!=null?Integer.parseInt(pageSize):5;
        this.searchField = searchField!=null?searchField:"";
        this.selectFields = selectFields!=null?selectFields:"*";
        this.domainNo = domainNo!=null?Integer.parseInt(domainNo):0;
        this.maxCount = maxCount!=null?Integer.parseInt(maxCount):10;
        this.akcModes = akcModes!=null?akcModes:"sc";
        this.gender = gender!=null?gender:"";
    }
    
    public String getSiteNm() {
		return siteNm;
	}
	public void setSiteNm(String siteNm) {
		this.siteNm = siteNm;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getSearchField() {
		return searchField;
	}
	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}
	public String getSelectFields() {
		return selectFields;
	}
	public void setSelectFields(String selectFields) {
		this.selectFields = selectFields;
	}
	public int getDomainNo() {
		return domainNo;
	}
	public void setDomainNo(int domainNo) {
		this.domainNo = domainNo;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public String getAkcModes() {
		return akcModes;
	}
	public void setAkcModes(String akcModes) {
		this.akcModes = akcModes;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@Override
	public String toString() {
		return "TotalSearchParam [siteNm=" + siteNm + ", userid=" + userid + ", searchKeyword=" + searchKeyword
				+ ", category=" + category + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + ", searchField="
				+ searchField + ", selectFields=" + selectFields + ", gender=" + gender + ", domainNo=" + domainNo
				+ ", maxCount=" + maxCount + ", akcModes=" + akcModes + "]";
	}
}
