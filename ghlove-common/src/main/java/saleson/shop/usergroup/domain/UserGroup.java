package saleson.shop.usergroup.domain;

import saleson.common.utils.CommonUtils;

public class UserGroup {
	
	private String groupCode;
	private long userId;
	private String resetUserLevel;
	
	private String authority;
	private String groupName;
	private String groupExplanation;
	private String createdDate;
	private String createdUserId;
	private String updatedDate;
	private	String updatedUserId;
	private int userCount;
	
	public String getResetUserLevel() {
		return resetUserLevel;
	}
	public void setResetUserLevel(String resetUserLevel) {
		this.resetUserLevel = resetUserLevel;
	}
	private String[] userIds = null;


	public String[] getUserIds() {
		return CommonUtils.copy(userIds);
	}
	public void setUserIds(String[] userIds) {
		this.userIds = CommonUtils.copy(userIds);
	}

	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupExplanation() {
		return groupExplanation;
	}
	public void setGroupExplanation(String groupExplanation) {
		this.groupExplanation = groupExplanation;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(String createdUserId) {
		this.createdUserId = createdUserId;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUpdatedUserId() {
		return updatedUserId;
	}
	public void setUpdatedUserId(String updatedUserId) {
		this.updatedUserId = updatedUserId;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	
}
