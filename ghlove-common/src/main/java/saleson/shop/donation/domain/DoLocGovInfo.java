package saleson.shop.donation.domain;

public class DoLocGovInfo {

    // 지자체정보
    private String locgovCode;    		// 지자체 코드
    private String upperLocgovNm;   	// 상위지자체명
    private String locgovNm;   			// 지자체명
    private String upperLocgovCode;   	// 상위지자체 코드
    private String locgovIntrcnCn;   	// 지자체 소개내용
    private String chargerCttpc;   		// 담당자 연락처
    private String chargerNm;   		// 담당자명
    private String chargerPsitnDept;	// 담당자 소속부서
    private String locgovHmpg;   		// 지자체 홈페이지
    private String locgovPopltnCo; 		// 지자체 인구수
    private String locgovAr;	   		// 지자체 면적
    private String locgovSpcprd;   		// 지자체 특산물
    private String gcctUseAt;   		// 상품권 사용여부
    private String etrcshUseAt;   		// 전자화폐 사용여부
    private String locgovBudgetAmt;		// 지자체 예산금액
    private String bizrno;   			// 사업자번호
    private String locgovZip;   		// 지자체 우편번호
    private String bassAdres;   		// 기본주소
    private String dtlAdres;   			// 상세주소
    private String achlqrSleAt;   		// 주류판매 여부
    private String useAt;   			// 사용여부
    private String frstRegisterId;		// 최초등록자 ID
    private String frstRegistPnttm;		// 최초등록자 시점
    private String lastUpdusrId;		// 최종수정자 ID
    private String lastUpdtPnttm;		// 최종수정자 시점
    private int pointRate;				// 포인트 지급율
    private int stdr1levelAmt;
    private int stdr2levelAmt;
    private int stdr3levelAmt;
    private int intrstCnt;
    private String lmttBgnDe;
    private String lmttEndDe;
    private String violtResnCn;
    
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
	
	public String getLocgovBudgetAmt() {
		return locgovBudgetAmt;
	}
	public void setLocgovBudgetAmt(String locgovBudgetAmt) {
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
	public String getUseAt() {
		return useAt;
	}
	public void setUseAt(String useAt) {
		this.useAt = useAt;
	}
	public String getFrstRegisterId() {
		return frstRegisterId;
	}
	public void setFrstRegisterId(String frstRegisterId) {
		this.frstRegisterId = frstRegisterId;
	}
	
	public String getFrstRegistPnttm() {
		return frstRegistPnttm;
	}
	public void setFrstRegistPnttm(String frstRegistPnttm) {
		this.frstRegistPnttm = frstRegistPnttm;
	}
	public String getLastUpdusrId() {
		return lastUpdusrId;
	}
	public void setLastUpdusrId(String lastUpdusrId) {
		this.lastUpdusrId = lastUpdusrId;
	}
	public String getLastUpdtPnttm() {
		return lastUpdtPnttm;
	}
	public void setLastUpdtPnttm(String lastUpdtPnttm) {
		this.lastUpdtPnttm = lastUpdtPnttm;
	}
	public int getPointRate() {
		return pointRate;
	}
	public void setPointRate(int pointRate) {
		this.pointRate = pointRate;
	}
	public int getStdr1levelAmt() {
		return stdr1levelAmt;
	}
	public void setStdr1levelAmt(int stdr1levelAmt) {
		this.stdr1levelAmt = stdr1levelAmt;
	}
	public int getStdr2levelAmt() {
		return stdr2levelAmt;
	}
	public void setStdr2levelAmt(int stdr2levelAmt) {
		this.stdr2levelAmt = stdr2levelAmt;
	}
	public int getStdr3levelAmt() {
		return stdr3levelAmt;
	}
	public void setStdr3levelAmt(int stdr3levelAmt) {
		this.stdr3levelAmt = stdr3levelAmt;
	}
	public int getIntrstCnt() {
		return intrstCnt;
	}
	public void setIntrstCnt(int intrstCnt) {
		this.intrstCnt = intrstCnt;
	}
	public String getLmttBgnDe() {
		return lmttBgnDe;
	}
	public void setLmttBgnDe(String lmttBgnDe) {
		this.lmttBgnDe = lmttBgnDe;
	}
	public String getLmttEndDe() {
		return lmttEndDe;
	}
	public void setLmttEndDe(String lmttEndDe) {
		this.lmttEndDe = lmttEndDe;
	}
	public String getVioltResnCn() {
		return violtResnCn;
	}
	public void setVioltResnCn(String violtResnCn) {
		this.violtResnCn = violtResnCn;
	}
	
}
