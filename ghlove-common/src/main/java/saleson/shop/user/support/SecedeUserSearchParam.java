package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class SecedeUserSearchParam extends SearchParam  {
	private Long userId;				// 사용자 ID
	private String loginId;				// 로그인 ID
	private String leaveDate;			// 탈퇴일자
	private String leaveReason;			// 탈퇴사유
	private String leaveCode;			// 탈퇴코드
	private String leaveCodeLabel;		// 탈퇴코드라벨
	private String leaveUserId;			// 강제탈퇴 ID
	private String leaveUserName;		// 강제탈퇴 사용자명
	private String roleName;			// 권한명
	
	/* 검색조건 */
	private String srchKey;				// 검색구분 - 구분타입
	private String srchValue;			// 검색구분 - 구분값
	private String srchStartLeaveDate;	// 탈퇴일자 (시작)
	private String srchEndLeaveDate;	// 탈퇴일자 (종료)
	private String srchLeaveType;		// 탈퇴구분 (U: 회원탈퇴, M: 관리자탈퇴)
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}
	public String getLeaveReason() {
		return leaveReason;
	}
	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	public String getLeaveCode() {
		return leaveCode;
	}
	public void setLeaveCode(String leaveCode) {
		this.leaveCode = leaveCode;
	}
	public String getLeaveCodeLabel() {
		return leaveCodeLabel;
	}
	public void setLeaveCodeLabel(String leaveCodeLabel) {
		this.leaveCodeLabel = leaveCodeLabel;
	}
	public String getLeaveUserId() {
		return leaveUserId;
	}
	public void setLeaveUserId(String leaveUserId) {
		this.leaveUserId = leaveUserId;
	}
	public String getLeaveUserName() {
		return leaveUserName;
	}
	public void setLeaveUserName(String leaveUserName) {
		this.leaveUserName = leaveUserName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	public String getSrchStartLeaveDate() {
		return srchStartLeaveDate;
	}
	public void setSrchStartLeaveDate(String srchStartLeaveDate) {
		this.srchStartLeaveDate = srchStartLeaveDate;
	}
	public String getSrchEndLeaveDate() {
		return srchEndLeaveDate;
	}
	public void setSrchEndLeaveDate(String srchEndLeaveDate) {
		this.srchEndLeaveDate = srchEndLeaveDate;
	}
	public String getSrchLeaveType() {
		return srchLeaveType;
	}
	public void setSrchLeaveType(String srchLeaveType) {
		this.srchLeaveType = srchLeaveType;
	}
}
