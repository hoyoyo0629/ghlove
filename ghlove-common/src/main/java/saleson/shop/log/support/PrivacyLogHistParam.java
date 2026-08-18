package saleson.shop.log.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class PrivacyLogHistParam extends SearchParam{
	
	private long id;					//privacyAccessLog 업데이트용 일련번호
	private long histId;				//일련번호
	private String createdAt;			//등록일
	private long managerId;				//관리자 userId
	private String loginId;				//관리자 loginId
	private long privacyAccessLogId;	//privacyAccessLog일련번호
	private String reason;				//사유
	private String reasonType;			//사유 타입
	
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getHistId() {
		return histId;
	}
	public void setHistId(long histId) {
		this.histId = histId;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public long getManagerId() {
		return managerId;
	}
	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public long getPrivacyAccessLogId() {
		return privacyAccessLogId;
	}
	public void setPrivacyAccessLogId(long privacyAccessLogId) {
		this.privacyAccessLogId = privacyAccessLogId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
}
