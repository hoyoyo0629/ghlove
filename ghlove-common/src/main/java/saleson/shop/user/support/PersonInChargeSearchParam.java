package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class PersonInChargeSearchParam extends SearchParam {

	private String locgovCode;			// 지자체코드
	private String locgovNm;			// 지자체명
	private String upperLocgovCode;		// 상위지자체코드
	private String upperLocgovNm;		// 상위지자체명
	private String authority;			// 권한 ID
	private String loginId;				// 로그인 ID
	private String userName;			// 이름
	private String phoneNumber;			// 휴대폰번호
	private String createdDate;			// 등록일
	private Long statusCode;			// 상태코드(1: 가입대기, 2:차단, 3:탈퇴, 4:휴면계정, 5:휴면대기 6:탈퇴대기, 9:정상)
	private String denyDate;			// 차단일
	private Long userId;				// 회원 ID

	/* 검색조건 */
	private String srchStartCreated;	// 등록일 (시작)
	private String srchEndCreated;		// 등록일 (종료)
	private String srchKey;				// 검색구분 - 구분타입
	private String srchValue;			// 검색구분 - 구분값
	private String srchStatusCode;		// 사용여부
	private String[] srchArrAuthority;	// 권한 ID

	/* 조회조건 */
	private String[] arrAuthority;		// 권한 ID
	private String adminRole;			// 관리자 권한 (로그인 사용자)
	private Long loginUserId;			// 사용자 ID (로그인 사용

	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getLocgovNm() {
		return locgovNm;
	}
	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}
	public String getUpperLocgovCode() {
		return upperLocgovCode;
	}
	public void setUpperLocgovCode(String upperLocgovCode) {
		this.upperLocgovCode = upperLocgovCode;
	}
	public String getUpperLocgovNm() {
		return upperLocgovNm;
	}
	public void setUpperLocgovNm(String upperLocgovNm) {
		this.upperLocgovNm = upperLocgovNm;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Long getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Long statusCode) {
		this.statusCode = statusCode;
	}
	public String getDenyDate() {
		return denyDate;
	}
	public void setDenyDate(String denyDate) {
		this.denyDate = denyDate;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getSrchStatusCode() {
		return srchStatusCode;
	}
	public void setSrchStatusCode(String srchStatusCode) {
		this.srchStatusCode = srchStatusCode;
	}
	public String[] getSrchArrAuthority() {
		if(this.srchArrAuthority != null && this.srchArrAuthority.length > 0) {
			int authLength = this.srchArrAuthority.length;
			String[] returnSrchArrAuthority = new String[authLength];
			for(int i=0; i<authLength; i++) {
				returnSrchArrAuthority[i] = this.srchArrAuthority[i];
			}
			return returnSrchArrAuthority;
		} else {
			return null;
		}
	}
	public void setSrchArrAuthority(String[] srchArrAuthority) {
        if (srchArrAuthority == null) {
            this.srchArrAuthority = null;
        } else {
            int length = srchArrAuthority.length;
            this.srchArrAuthority = new String[length];
            for (int i = 0; i < length; i++) {
                this.srchArrAuthority[i] = srchArrAuthority[i];
            }
        }
	}
	public String[] getArrAuthority() {
		if(this.arrAuthority != null && this.arrAuthority.length > 0) {
			int authLength = this.arrAuthority.length;
			String[] returnArrAuthority = new String[authLength];
			for(int i=0; i<authLength; i++) {
				returnArrAuthority[i] = this.arrAuthority[i];
			}
			return returnArrAuthority;
		} else {
			return null;
		}
	}
	public void setArrAuthority(String[] arrAuthority) {
		 if (arrAuthority == null) {
	            this.arrAuthority = null;
        } else {
            int length = arrAuthority.length;
            this.arrAuthority = new String[length];
            for (int i = 0; i < length; i++) {
                this.arrAuthority[i] = arrAuthority[i];
            }
        }
	}
	public String getAdminRole() {
		return adminRole;
	}
	public void setAdminRole(String adminRole) {
		this.adminRole = adminRole;
	}
	public Long getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(Long loginUserId) {
		this.loginUserId = loginUserId;
	}
}
