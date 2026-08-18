package saleson.shop.log.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class PrivacyLogParam extends SearchParam{
	
	private long id;				//일련번호
	private String createdAt;	//등록일
	private String ip;			//ip
	private String loginType;	//로그인타입
	private long managerId;		//관리자 userId
	private String method;		
	private String name;		//메뉴
	private String reason;		//사유
	private String task;		//사유구분
	private String url;			//URL
	private long userId;		//다운로드 userId
	private String loginId;		//담당자아이디
	private int histCnt;		//수정 이력 카운트
	private long adminUserId;	//권한에 따른 아이디 조건
	private String reasonType;	//사유 타입
	private String reasonTypeNm;	//사유 타입명
	
	private Integer itemsPerPageTemp;	// 임시 목록수
	
	/* 검색조건 */
	private String srchKey;				// 검색구분 - 구분타입
	private String srchValue;			// 검색구분 - 구분값
	private String srchStartLogDate;	// 등록일 - 시작일
	private String srchEndLogDate;		// 등록일 - 종료일
	
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
	public String getSrchStartLogDate() {
		return srchStartLogDate;
	}
	public void setSrchStartLogDate(String srchStartLogDate) {
		this.srchStartLogDate = srchStartLogDate;
	}
	public String getSrchEndLogDate() {
		return srchEndLogDate;
	}
	public void setSrchEndLogDate(String srchEndLogDate) {
		this.srchEndLogDate = srchEndLogDate;
	}
	public Integer getItemsPerPageTemp() {
		return itemsPerPageTemp;
	}
	public void setItemsPerPageTemp(Integer itemsPerPageTemp) {
		this.itemsPerPageTemp = itemsPerPageTemp;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public long getManagerId() {
		return managerId;
	}
	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getHistCnt() {
		return histCnt;
	}
	public void setHistCnt(int histCnt) {
		this.histCnt = histCnt;
	}
	public long getAdminUserId() {
		return adminUserId;
	}
	public void setAdminUserId(long adminUserId) {
		this.adminUserId = adminUserId;
	}
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	public String getReasonTypeNm() {
		return reasonTypeNm;
	}
	public void setReasonTypeNm(String reasonTypeNm) {
		this.reasonTypeNm = reasonTypeNm;
	}
}
