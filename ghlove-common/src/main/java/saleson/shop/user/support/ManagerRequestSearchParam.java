package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class ManagerRequestSearchParam extends SearchParam {

	private Long userId;			// 회원 고유번호 
	private Integer reqstSn;		// 신청 일련번호
	private String loginId;			// 로그인 ID
	private String locgovCode;		// 지자체 코드
	private String reqstSeCode;		// 신청 구분 코드
	private String psitnCode;		// 소속코드-은행
	private String psitnNm;			// 소속 명
	private String psitnDeptNm;		// 소속부서
	private String ofcpsNm;			// 직위 명
	private String cttpc;			// 연락처
	private String confmSttusCode;	// 승인상태코드
	private String rejectResn;		// 거절사유
	private Long frstRegisterId;	// 최초 등록자 ID
	private String frstRegistPnttm;	// 최초 등록 시점
	private Long lastUpdusrId;		// 최종 수정자 ID
	private String lastUpdtPnttm;	// 최종 수정 시점
	
	/* 검색조건 */
	private Long superUserId;			// 슈퍼담당자 사용자 아이디
	private String srchStartCreated;	// 등록일 (시작)
	private String srchEndCreated;		// 등록일 (종료)
	private String srchKey;				// 검색구분 - 구분타입
	private String srchValue;			// 검색구분 - 구분값
	private String srchConfmSttusCode;	// 검색조건 - 상태
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getReqstSn() {
		return reqstSn;
	}
	public void setReqstSn(Integer reqstSn) {
		this.reqstSn = reqstSn;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getReqstSeCode() {
		return reqstSeCode;
	}
	public void setReqstSeCode(String reqstSeCode) {
		this.reqstSeCode = reqstSeCode;
	}
	public String getPsitnCode() {
		return psitnCode;
	}
	public void setPsitnCode(String psitnCode) {
		this.psitnCode = psitnCode;
	}
	public String getPsitnNm() {
		return psitnNm;
	}
	public void setPsitnNm(String psitnNm) {
		this.psitnNm = psitnNm;
	}
	public String getPsitnDeptNm() {
		return psitnDeptNm;
	}
	public void setPsitnDeptNm(String psitnDeptNm) {
		this.psitnDeptNm = psitnDeptNm;
	}
	public String getOfcpsNm() {
		return ofcpsNm;
	}
	public void setOfcpsNm(String ofcpsNm) {
		this.ofcpsNm = ofcpsNm;
	}
	public String getCttpc() {
		return cttpc;
	}
	public void setCttpc(String cttpc) {
		this.cttpc = cttpc;
	}
	public String getConfmSttusCode() {
		return confmSttusCode;
	}
	public void setConfmSttusCode(String confmSttusCode) {
		this.confmSttusCode = confmSttusCode;
	}
	public String getRejectResn() {
		return rejectResn;
	}
	public void setRejectResn(String rejectResn) {
		this.rejectResn = rejectResn;
	}
	public Long getFrstRegisterId() {
		return frstRegisterId;
	}
	public void setFrstRegisterId(Long frstRegisterId) {
		this.frstRegisterId = frstRegisterId;
	}
	public String getFrstRegistPnttm() {
		return frstRegistPnttm;
	}
	public void setFrstRegistPnttm(String frstRegistPnttm) {
		this.frstRegistPnttm = frstRegistPnttm;
	}
	public Long getLastUpdusrId() {
		return lastUpdusrId;
	}
	public void setLastUpdusrId(Long lastUpdusrId) {
		this.lastUpdusrId = lastUpdusrId;
	}
	public String getLastUpdtPnttm() {
		return lastUpdtPnttm;
	}
	public void setLastUpdtPnttm(String lastUpdtPnttm) {
		this.lastUpdtPnttm = lastUpdtPnttm;
	}
	public Long getSuperUserId() {
		return superUserId;
	}
	public void setSuperUserId(Long superUserId) {
		this.superUserId = superUserId;
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
	public String getSrchConfmSttusCode() {
		return srchConfmSttusCode;
	}
	public void setSrchConfmSttusCode(String srchConfmSttusCode) {
		this.srchConfmSttusCode = srchConfmSttusCode;
	}
}
