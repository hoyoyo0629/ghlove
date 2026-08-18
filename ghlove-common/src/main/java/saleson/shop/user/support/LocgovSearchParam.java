package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class LocgovSearchParam extends SearchParam {
	
	private String locgovCode;			// 지자체 코드
	private String upperLocgovNm;		// 상위지자체명
	private String locgovNm;			// 지자체 명
	private String upperLocgovCode;		// 상위 지자체 코드
	private String locgovIntrcnCn;		// 지자체 소개 내용
	private String chargerCttpc;		// 담당자 연락처
	private String chargerNm;			// 담당자 성명
	private String chargerPsitnDept;	// 담당자 소속 부서
	private String locgovHmpg;			// 지자체 홈페이지
	private String locgovPopltnCo;		// 지자체 인구 수
	private String locgovAr;			// 지자체 면적
	private String locgovSpcprd;		// 지자체 특산물
	private String gcctUseAt;			// 상품권 사용 여부
	private String etrcshUseAt;			// 전자화폐 사용 여부
	private String useAt;				// 사용 여부
	private Long locgovBudgetAmt;		// 지자체 예산 금액
	private String bizrno;				// 사업자번호
	private String locgovZip;			// 기본 주소
	private String bassAdres;			// 지자체 우편번호
	private String dtlAdres;			// 상세 주소
	private String achlqrSleAt;			// 주류 판매 여부
	private Long frstRegisterId;		// 최초 등록자 ID
	private String frstRegistPnttm;		// 최초 등록 시점
	private Long lastUpdusrId;			// 최종 수정자 ID
	private String lastUpdtPnttm;		// 최종 수정 시점
	private String processDeptCode;		// 처리부서코드
	private String administInsttCode;	// 행정기관코드
	
	/* 검색조건 */
	private String srchStartCreated;	// 등록일 (시작)
	private String srchEndCreated;		// 등록일 (종료)
	private String srchKey;				// 검색구분 - 구분타입
	private String srchValue;			// 검색구분 - 구분값
	
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getUpperLocgovNm() {
		return upperLocgovNm;
	}
	public void setUpperLocgovNm(String upperLocgovNm) {
		this.upperLocgovNm = upperLocgovNm;
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
	public String getLocgovIntrcnCn() {
		return locgovIntrcnCn;
	}
	public void setLocgovIntrcnCn(String locgovIntrcnCn) {
		this.locgovIntrcnCn = locgovIntrcnCn;
	}
	public String getChargerCttpc() {
		return chargerCttpc;
	}
	public void setChargerCttpc(String chargerCttpc) {
		this.chargerCttpc = chargerCttpc;
	}
	public String getChargerNm() {
		return chargerNm;
	}
	public void setChargerNm(String chargerNm) {
		this.chargerNm = chargerNm;
	}
	public String getChargerPsitnDept() {
		return chargerPsitnDept;
	}
	public void setChargerPsitnDept(String chargerPsitnDept) {
		this.chargerPsitnDept = chargerPsitnDept;
	}
	public String getLocgovHmpg() {
		return locgovHmpg;
	}
	public void setLocgovHmpg(String locgovHmpg) {
		this.locgovHmpg = locgovHmpg;
	}
	public String getLocgovPopltnCo() {
		return locgovPopltnCo;
	}
	public void setLocgovPopltnCo(String locgovPopltnCo) {
		this.locgovPopltnCo = locgovPopltnCo;
	}
	public String getLocgovAr() {
		return locgovAr;
	}
	public void setLocgovAr(String locgovAr) {
		this.locgovAr = locgovAr;
	}
	public String getLocgovSpcprd() {
		return locgovSpcprd;
	}
	public void setLocgovSpcprd(String locgovSpcprd) {
		this.locgovSpcprd = locgovSpcprd;
	}
	public String getGcctUseAt() {
		return gcctUseAt;
	}
	public void setGcctUseAt(String gcctUseAt) {
		this.gcctUseAt = gcctUseAt;
	}
	public String getEtrcshUseAt() {
		return etrcshUseAt;
	}
	public void setEtrcshUseAt(String etrcshUseAt) {
		this.etrcshUseAt = etrcshUseAt;
	}
	public String getUseAt() {
		return useAt;
	}
	public void setUseAt(String useAt) {
		this.useAt = useAt;
	}
	public Long getLocgovBudgetAmt() {
		return locgovBudgetAmt;
	}
	public void setLocgovBudgetAmt(Long locgovBudgetAmt) {
		this.locgovBudgetAmt = locgovBudgetAmt;
	}
	public String getBizrno() {
		return bizrno;
	}
	public void setBizrno(String bizrno) {
		this.bizrno = bizrno;
	}
	public String getLocgovZip() {
		return locgovZip;
	}
	public void setLocgovZip(String locgovZip) {
		this.locgovZip = locgovZip;
	}
	public String getBassAdres() {
		return bassAdres;
	}
	public void setBassAdres(String bassAdres) {
		this.bassAdres = bassAdres;
	}
	public String getDtlAdres() {
		return dtlAdres;
	}
	public void setDtlAdres(String dtlAdres) {
		this.dtlAdres = dtlAdres;
	}
	public String getAchlqrSleAt() {
		return achlqrSleAt;
	}
	public void setAchlqrSleAt(String achlqrSleAt) {
		this.achlqrSleAt = achlqrSleAt;
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
	public String getProcessDeptCode() {
		return processDeptCode;
	}
	public void setProcessDeptCode(String processDeptCode) {
		this.processDeptCode = processDeptCode;
	}
	public String getAdministInsttCode() {
		return administInsttCode;
	}
	public void setAdministInsttCode(String administInsttCode) {
		this.administInsttCode = administInsttCode;
	}
}
