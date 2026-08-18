package saleson.shop.log.domain;

public class LoginLog {
	
	public static final String LOGIN_TYPE_USER = "user";
	public static final String LOGIN_TYPE_MANAGER = "manager";
	public static final String LOGIN_TYPE_SELLER = "seller";
	
	private int loginLogId;
	private String loginType;
	private String loginId;
	private String successFlag;
	private String remoteAddr;
	private String memo;
	private String loginDate;
	private String authority;
	private String nextLoginDate;
	private String roleName;
	
	public int getLoginLogId() {
		return loginLogId;
	}
	public void setLoginLogId(int loginLogId) {
		this.loginLogId = loginLogId;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getSuccessFlag() {
		return successFlag;
	}
	public void setSuccessFlag(String successFlag) {
		this.successFlag = successFlag;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getNextLoginDate() {
		return nextLoginDate;
	}
	public void setNextLoginDate(String nextLoginDate) {
		this.nextLoginDate = nextLoginDate;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
