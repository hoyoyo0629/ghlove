package saleson.shop.databoard.support;

import java.util.List;

import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.common.utils.CommonUtils;

@SuppressWarnings("serial")
public class DataboardParam extends SearchParam {
	private Integer dataId;
	private String subCategory;
	private String categoryTeam;
	private String subject;
	private String noticeFlag;
	private int visibleType;
	private long sellerId;
	
	// 조회용 팀코드
	private String[] teamCodes;
	private String startCreateDate;
	private String endCreateDate;

	/* 일괄삭제기능 사용 */
	private List<String> databoardList;

	public Integer getDataId() {
		return dataId;
	}
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	public String getNoticeFlag() {
		return noticeFlag;
	}
	public void setNoticeFlag(String noticeFlag) {
		this.noticeFlag = noticeFlag;
	}
	public int getVisibleType() {
		return visibleType;
	}
	public void setVisibleType(int visibleType) {
		this.visibleType = visibleType;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getCategoryTeam() {
		return categoryTeam;
	}
	public void setCategoryTeam(String categoryTeam) {
		this.categoryTeam = categoryTeam;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String[] getTeamCodes() {
		return CommonUtils.copy(teamCodes);
	}
	public void setTeamCodes(String[] teamCodes) {
		this.teamCodes = CommonUtils.copy(teamCodes);
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	public String getStartCreateDate() {
		return startCreateDate;
	}

	public void setStartCreateDate(String startCreateDate) {
		this.startCreateDate = startCreateDate;
	}

	public String getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(String endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public List<String> getDataboardList() {
		return databoardList;
	}
	public void setDataboardList(List<String> databoardList) {
		this.databoardList = databoardList;
	}


}
