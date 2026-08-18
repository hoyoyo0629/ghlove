package saleson.shop.log.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class LoginLogParam extends SearchParam{
	private Integer loginLogId;
	private String loginType;
	private String loginId;
	private String successFlag;
	private String remoteAddr;
	private String memo;
	private String loginDate;

	private String authNum;				// 이메일 인증번호

	/* 검색조건 */
	private String srchKey;				// 검색구분 - 구분타입
	private String srchValue;			// 검색구분 - 구분값
	private String srchStartLoginDate;	// 접속일 - 시작일
	private String srchEndLoginDate;	// 접속일 - 종료일
	private String srchSuccessFlag;		// 로그인 성공여부 - 성공(Y)/실패(N)
	private String srcRole;				// 권한그룹

	/* 정렬 */
	private Integer itemsPerPageTemp;	// 임시 목록수

	public Integer getLoginLogId() {
		return loginLogId;
	}
	public void setLoginLogId(Integer loginLogId) {
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
	public String getSrchKey() {
		return srchKey;
	}
	public void setSrchKey(String srchKey) {
		this.srchKey = srchKey;
	}
	public String getSrchValue() {
		return srchValue;
	}
	public void setSrchValue(String srchValue) {
		this.srchValue = srchValue;
	}
	public String getSrchStartLoginDate() {
		return srchStartLoginDate;
	}
	public void setSrchStartLoginDate(String srchStartLoginDate) {
		this.srchStartLoginDate = srchStartLoginDate;
	}
	public String getSrchEndLoginDate() {
		return srchEndLoginDate;
	}
	public void setSrchEndLoginDate(String srchEndLoginDate) {
		this.srchEndLoginDate = srchEndLoginDate;
	}
	public String getSrchSuccessFlag() {
		return srchSuccessFlag;
	}
	public void setSrchSuccessFlag(String srchSuccessFlag) {
		this.srchSuccessFlag = srchSuccessFlag;
	}
	public String getSrcRole() {
		return srcRole;
	}
	public void setSrcRole(String srcRole) {
		this.srcRole = srcRole;
	}
	public Integer getItemsPerPageTemp() {
		return itemsPerPageTemp;
	}
	public void setItemsPerPageTemp(Integer itemsPerPageTemp) {
		this.itemsPerPageTemp = itemsPerPageTemp;
	}


	public String getAuthNum() {
		return authNum;
	}
	public void setAuthNum(String authNum) {
		this.authNum = authNum;
	}

}
