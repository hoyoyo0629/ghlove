package saleson.shop.donation.support;

import org.springframework.util.StringUtils;

import com.onlinepowers.framework.web.domain.ListParam;

public class ContryParam extends ListParam{
	
	private String systemCd;		/* 인터페이스구분코드 */
	private String jijacheCd;		/* 기부지자체코드 */
	
	private String deptCd;			/* 부서코드 */
	private String fisyy;			/* 회계연도 */
	private String fisSp;			/* 회계구분코드 */
	private String ptclCd;			/* 세목코드 */
	private String taxAmt;			/* 기부금액 */
	private String impsSp;			/* 부과구분 */
	private String decsSp;			/* 감경구분 */
	private String txprSp;			/* 납부자구분 */
	private String txprNo;			// 납부자번호
	private String txprNm;			// 납부자성명
	private String newAddrYn;			// 새주소여부
    private String txprRoadCd;			// 납부자도로명주소
    private String txprBdFlrSp;			// 납부자지하여부
    private String txprBdPrcpNo;		// 납부자건물본번
    private String txprBdSubNo;			// 납부자건물부번
    private String statCd;			// 납부자상태코드
    private String spclFisBizCd;			// 특별회계사업코드
    private String txprZipCd;			// 우편번호
    private String txprTwnvilCd;			// 행정동코드
    private String txprBdMngNo;			// 납부자건물관리번호
    private String txprDtlAddr;			// 납부자상세주소
    private String objNm;			// 물건지명
    private String taxObjSp;			// 부과대상구분코드
    private String taxObjNewAddrYn;			// 물건지새주소여부
    private String mngHtm1;			// 기부금명칭 및 기타항목
    private String sysCd;			// 시스템코드
	private String cntrId;		/* 기부 sn */
	private String selectedRegionCd;		/* 선택 자치단체코드 */
	private String userRegionCd;		/* 사용자거주지 자치단체코드 */
	private String enapbuNo;		/* 전자납부번호 */
	private String userId;		/* 사용자id */
	
	private String presentType; /* 답례품 신청 코드 */
	private String mngNo; 		/* 대장관리번호 */
	
	private String payValidDe;  /*납부유효일자*/ 
	private String useInsttCode;    // 분류코드 
	private String giroNo;          // 지로번호
	private String host;            // 사용자접속URL

	private String foreignStatusCode; /* 내/외국인 구분 코드 */
	private long prjId; /* 지정기부 프로젝트 아이디 */
	
	public String getUseInsttCode() {
		return useInsttCode;
	}
	public void setUseInsttCode(String useInsttCode) {
		this.useInsttCode = useInsttCode;
	}
	public String getGiroNo() {
		return giroNo;
	}
	public void setGiroNo(String giroNo) {
		this.giroNo = giroNo;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getPayValidDe() {
		return payValidDe;
	}
	public void setPayValidDe(String payValidDe) {
		this.payValidDe = payValidDe;
	}
	public String getSystemCd() {
		return systemCd;
	}
	public void setSystemCd(String systemCd) {
		this.systemCd = systemCd;
	}
	public String getJijacheCd() {
		return jijacheCd;
	}
	public void setJijacheCd(String jijacheCd) {
		this.jijacheCd = jijacheCd;
	}
	public String getDeptCd() {
		return deptCd;
	}
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	public String getFisyy() {
		return fisyy;
	}
	public void setFisyy(String fisyy) {
		this.fisyy = fisyy;
	}
	public String getFisSp() {
		return fisSp;
	}
	public void setFisSp(String fisSp) {
		this.fisSp = fisSp;
	}
	public String getPtclCd() {
		return ptclCd;
	}
	public void setPtclCd(String ptclCd) {
		this.ptclCd = ptclCd;
	}
	public String getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(String taxAmt) {
		this.taxAmt = taxAmt;
	}
	public String getImpsSp() {
		return impsSp;
	}
	public void setImpsSp(String impsSp) {
		this.impsSp = impsSp;
	}
	public String getDecsSp() {
		return decsSp;
	}
	public void setDecsSp(String decsSp) {
		this.decsSp = decsSp;
	}
	public String getTxprSp() {
		return txprSp;
	}
	public void setTxprSp(String txprSp) {
		this.txprSp = txprSp;
	}
	public String getTxprNo() {
		return txprNo;
	}
	public void setTxprNo(String txprNo) {
		this.txprNo = txprNo;
	}
	public String getTxprNm() {
		return txprNm;
	}
	public void setTxprNm(String txprNm) {
		this.txprNm = txprNm;
	}
	public String getNewAddrYn() {
		return newAddrYn;
	}
	public void setNewAddrYn(String newAddrYn) {
		this.newAddrYn = newAddrYn;
	}
	public String getTxprRoadCd() {
		return txprRoadCd;
	}
	public void setTxprRoadCd(String txprRoadCd) {
		this.txprRoadCd = txprRoadCd;
	}
	public String getTxprBdFlrSp() {
		return txprBdFlrSp;
	}
	public void setTxprBdFlrSp(String txprBdFlrSp) {
		this.txprBdFlrSp = txprBdFlrSp;
	}
	public String getTxprBdPrcpNo() {
		return txprBdPrcpNo;
	}
	public void setTxprBdPrcpNo(String txprBdPrcpNo) {
		this.txprBdPrcpNo = txprBdPrcpNo;
	}
	public String getTxprBdSubNo() {
		return txprBdSubNo;
	}
	public void setTxprBdSubNo(String txprBdSubNo) {
		this.txprBdSubNo = txprBdSubNo;
	}
	public String getStatCd() {
		return statCd;
	}
	public void setStatCd(String statCd) {
		this.statCd = statCd;
	}
	public String getSpclFisBizCd() {
		return spclFisBizCd;
	}
	public void setSpclFisBizCd(String spclFisBizCd) {
		this.spclFisBizCd = spclFisBizCd;
	}
	public String getTxprZipCd() {
		return txprZipCd;
	}
	public void setTxprZipCd(String txprZipCd) {
		this.txprZipCd = txprZipCd;
	}
	public String getTxprTwnvilCd() {
		return txprTwnvilCd;
	}
	public void setTxprTwnvilCd(String txprTwnvilCd) {
		this.txprTwnvilCd = txprTwnvilCd;
	}
	public String getTxprBdMngNo() {
		return txprBdMngNo;
	}
	public void setTxprBdMngNo(String txprBdMngNo) {
		this.txprBdMngNo = txprBdMngNo;
	}
	public String getTxprDtlAddr() {
		return txprDtlAddr;
	}
	public void setTxprDtlAddr(String txprDtlAddr) {
		this.txprDtlAddr = txprDtlAddr;
	}
	public String getObjNm() {
		return objNm;
	}
	public void setObjNm(String objNm) {
		this.objNm = objNm;
	}
	public String getTaxObjSp() {
		return taxObjSp;
	}
	public void setTaxObjSp(String taxObjSp) {
		this.taxObjSp = taxObjSp;
	}
	public String getTaxObjNewAddrYn() {
		return taxObjNewAddrYn;
	}
	public void setTaxObjNewAddrYn(String taxObjNewAddrYn) {
		this.taxObjNewAddrYn = taxObjNewAddrYn;
	}
	public String getMngHtm1() {
		return mngHtm1;
	}
	public void setMngHtm1(String mngHtm1) {
		this.mngHtm1 = mngHtm1;
	}
	public String getSysCd() {
		return sysCd;
	}
	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}
	public String getCntrId() {
		return cntrId;
	}
	public void setCntrId(String cntrId) {
		this.cntrId = cntrId;
	}
	public String getSelectedRegionCd() {
		return selectedRegionCd;
	}
	public void setSelectedRegionCd(String selectedRegionCd) {
		this.selectedRegionCd = selectedRegionCd;
	}
	public String getUserRegionCd() {
		return userRegionCd;
	}
	public void setUserRegionCd(String userRegionCd) {
		this.userRegionCd = userRegionCd;
	}
	public String getEnapbuNo() {
		return enapbuNo;
	}
	public void setEnapbuNo(String enapbuNo) {
		this.enapbuNo = enapbuNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPresentType() {
		return presentType;
	}
	public void setPresentType(String presentType) {
		this.presentType = presentType;
	}
	public String getMngNo() {
		return mngNo;
	}
	public void setMngNo(String mngNo) {
		this.mngNo = mngNo;
	}
	public String getForeignStatusCode() {
		if (StringUtils.hasLength(foreignStatusCode)) {
			return foreignStatusCode;
		} else {
			return "0";
		}
	}
	public void setForeignStatusCode(String foreignStatusCode) {
		this.foreignStatusCode = foreignStatusCode;
	}
	public long getPrjId() {
		return prjId;
	}
	public void setPrjId(long prjId) {
		this.prjId = prjId;
	}
	
}

