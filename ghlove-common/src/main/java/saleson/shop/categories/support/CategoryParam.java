package saleson.shop.categories.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class CategoryParam extends SearchParam {
	private String categoryTeamCode = "";
	private String categoryGroupCode = "";
	private String categoryUrl = "";
	private String categoryCode = "";		// 선택된 카테고리 코드.
	
	private String group = "";			// 지자체
	private String category = "";			// 지자체
	private String locgov = "";			// 지자체
	private String keyword = "";		// 상품검색 키워드
	private long price = 0l;		// 상품검색 키워드
	private String categoryLevel = "";		// 상품검색 카테고리 레벨
	private String categoryClass1 = "";		// 상품검색 카테고리 1차
	private String categoryClass2 = "";		// 상품검색 카테고리 2차
	private String categoryClass3 = "";		// 상품검색 카테고리 3차
	private String categoryClass4 = "";		// 상품검색 카테고리 4차
	public String getCategoryTeamCode() {
		return categoryTeamCode;
	}
	public void setCategoryTeamCode(String categoryTeamCode) {
		this.categoryTeamCode = categoryTeamCode;
	}
	public String getCategoryGroupCode() {
		return categoryGroupCode;
	}
	public void setCategoryGroupCode(String categoryGroupCode) {
		this.categoryGroupCode = categoryGroupCode;
	}
	public String getCategoryUrl() {
		return categoryUrl;
	}
	public void setCategoryUrl(String categoryUrl) {
		this.categoryUrl = categoryUrl;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLocgov() {
		return locgov;
	}
	public void setLocgov(String locgov) {
		this.locgov = locgov;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}
	public String getCategoryLevel() {
		return categoryLevel;
	}
	public void setCategoryLevel(String categoryLevel) {
		this.categoryLevel = categoryLevel;
	}
	public String getCategoryClass1() {
		return categoryClass1;
	}
	public void setCategoryClass1(String categoryClass1) {
		this.categoryClass1 = categoryClass1;
	}
	public String getCategoryClass2() {
		return categoryClass2;
	}
	public void setCategoryClass2(String categoryClass2) {
		this.categoryClass2 = categoryClass2;
	}
	public String getCategoryClass3() {
		return categoryClass3;
	}
	public void setCategoryClass3(String categoryClass3) {
		this.categoryClass3 = categoryClass3;
	}
	public String getCategoryClass4() {
		return categoryClass4;
	}
	public void setCategoryClass4(String categoryClass4) {
		this.categoryClass4 = categoryClass4;
	}
	
}
