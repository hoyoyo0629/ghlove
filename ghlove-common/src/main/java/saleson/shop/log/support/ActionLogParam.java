package saleson.shop.log.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class ActionLogParam extends SearchParam {
	private long actionLogId;			// ID
	private String loginId;				// 로그인 ID
	private String loginType;			// 로그인 타입
	private String requestUri;			// URI
	private String requestMethod;		// 요청 메소드
	private String remoteAddr;			// IP
	private String createdDate;			// 생성일
	private Integer loginLogId;			// 로그인 로그 ID
	
	/* 상세 검색조건 */
	private String srchStartCreated;	// 상세 - 시작일
	private String srchEndCreated;		// 상세 - 종료일
	
	public long getActionLogId() {
		return actionLogId;
	}
	public void setActionLogId(long actionLogId) {
		this.actionLogId = actionLogId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getRequestUri() {
		return requestUri;
	}
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getSrchStartCreated() {
		return srchStartCreated;
	}
	public void setSrchStartCreated(String srchStartCreated) {
		this.srchStartCreated = srchStartCreated;
	}
	public String getSrchEndCreated() {
		return srchEndCreated;
	}
	public void setSrchEndCreated(String srchEndCreated) {
		this.srchEndCreated = srchEndCreated;
	}
	public Integer getLoginLogId() {
		return loginLogId;
	}
	public void setLoginLogId(Integer loginLogId) {
		this.loginLogId = loginLogId;
	}
}
