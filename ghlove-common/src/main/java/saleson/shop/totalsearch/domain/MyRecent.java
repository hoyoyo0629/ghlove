package saleson.shop.totalsearch.domain;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class MyRecent extends SearchParam {
	
	
	private int recentId;
	private long userId;
	private String keyword;
	private String createdDate;
	
	public int getRecentId() {
		return recentId;
	}
	public void setRecentId(int recentId) {
		this.recentId = recentId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
}
