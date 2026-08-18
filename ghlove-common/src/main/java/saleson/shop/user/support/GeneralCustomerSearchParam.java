package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class GeneralCustomerSearchParam extends SearchParam {
	private Long userId;					// 회원 ID
	private String loginId;					// 로그인 ID
	private String userName;				// 이름
	private String email;					// 이메일
	private String createdDate;				// 등록일
	private String sbscrbSeCode;			// 가입구분코드
	private String sbscrbSeNm;				// 가입구분명
	private String post;					// 우편주소
	private String address;					// 주소
	private String addressDetail;			// 상세 주소
	private String receiveEmail;			// 수신여부 (0: 수신, 1:비수신)
	private String phoneNumber;				// 휴대폰번호
	private Integer totalCntrAmt;			// 기부누적액
	private Integer totalCntrBlcePoint;		// 포인트잔액
	private String password;				// 비밀번호
	
	/* 검색조건 */
	private String srchStartCreated;		// 등록일 (시작)
	private String srchEndCreated;			// 등록일 (종료)
	private String srchKey;					// 검색구분 - 구분타입
	private String srchValue;				// 검색구분 - 구분값
	private String srchReceiveEmail;		// Email 수신동의 
	private String srchSbscrbSeCode;		// 가입구분
	
	private Long readngSn;					// 열랄일련번호 
	private Long sessionUserId;				// 로그인ID
	private Long trgetUserId;				// 대상자ID
	
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getSbscrbSeCode() {
		return sbscrbSeCode;
	}
	public void setSbscrbSeCode(String sbscrbSeCode) {
		this.sbscrbSeCode = sbscrbSeCode;
	}
	public String getSbscrbSeNm() {
		return sbscrbSeNm;
	}
	public void setSbscrbSeNm(String sbscrbSeNm) {
		this.sbscrbSeNm = sbscrbSeNm;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
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
	public String getReceiveEmail() {
		return receiveEmail;
	}
	public void setReceiveEmail(String receiveEmail) {
		this.receiveEmail = receiveEmail;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
	public String getSrchReceiveEmail() {
		return srchReceiveEmail;
	}
	public void setSrchReceiveEmail(String srchReceiveEmail) {
		this.srchReceiveEmail = srchReceiveEmail;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSrchSbscrbSeCode() {
		return srchSbscrbSeCode;
	}
	public void setSrchSbscrbSeCode(String srchSbscrbSeCode) {
		this.srchSbscrbSeCode = srchSbscrbSeCode;
	}
	public Long getReadngSn() {
		return readngSn;
	}
	public void setReadngSn(Long readngSn) {
		this.readngSn = readngSn;
	}	
	public Long getSessionUserId() {
		return sessionUserId;
	}
	public void setSessionUserId(Long sessionUserId) {
		this.sessionUserId = sessionUserId;
	}	
	
	public Long getTrgetUserId() {
		return trgetUserId;
	}
	public void setTrgetUserId(Long trgetUserId) {
		this.trgetUserId = trgetUserId;
	}
}
