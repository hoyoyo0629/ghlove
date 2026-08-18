package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class SleepUserSearchParam extends SearchParam {
	private Long userId;					// 회원 ID
	private String loginId;					// 로그인 ID
	private String loginDate;				// 마지막 로그인 날짜
	private String userName;				// 사용자 이름
	private String address;					// 주소
	private String addressDetail;			// 상세 주소
	private Integer totalCntrAmt;			// 기부누적액
	private Integer totalCntrBlcePoint;		// 포인트잔액
	
	/* 검색조건 */
	private String srchStartLoginDate;		// 최종 방문일 (시작)
	private String srchEndLoginDate;		// 최종 방문일 (종료)
	private String srchKey;					// 검색구분 - 구분타입
	private String srchValue;				// 검색구분 - 구분값
	
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
	public String getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressDetail() {
		return addressDetail;
	}
	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}
	public Integer getTotalCntrAmt() {
		return totalCntrAmt;
	}
	public void setTotalCntrAmt(Integer totalCntrAmt) {
		this.totalCntrAmt = totalCntrAmt;
	}
	public Integer getTotalCntrBlcePoint() {
		return totalCntrBlcePoint;
	}
	public void setTotalCntrBlcePoint(Integer totalCntrBlcePoint) {
		this.totalCntrBlcePoint = totalCntrBlcePoint;
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
}
