package saleson.shop.donation.support;

import org.springframework.util.StringUtils;

import com.onlinepowers.framework.web.domain.ListParam;

public class SeoulParam extends ListParam{

	private String systemCd;		/* 인터페이스구분코드 */
	private String jijacheCd;		/* 지자체코드 */
	private String siguCd;			/* 시구코드 */
	private String semokCd;			/* 세목코드 */
	private String setTaxYm;		/* 과세년월 */
	private String taxGubun;		/* 과세구분 */
	private String sidoCd;			/* 시도코드 */

	private String napId;			/* 납세자ID */
	private String napNm;			/* 납세자명 */
	private String napGubun;		/* 납세자구분 */
	private String taxAmt;				/* 과세금액 */
	private String sise;			/* 시세 */

	private String resideStatus;	/* 거주상태 */
	private String mulGubun;		/* 물건구분 */
	private String mulNm;			/* 물건명 */
	private String bookNo;			/* 대장번호 */
	private String sysGubun;		/* 시스템구분 */

	private String cntrId;		/* 기부 sn */
	private String selectedRegionCd;		/* 선택 자치단체코드 */
	private String userRegionCd;		/* 사용자거주지 자치단체코드 */
	private String enapbuNo;		/* 전자납부번호 */
	private String userId;		/* 사용자id */

	private String presentType; /* 답례품 신청 코드 */
	private String userCntrPoint; /* 기부포인트 */
	private String sttemntPayDe; /* 신고납부일자 */
	private String userCi; /* 사용자ci */
	private String bizNo; /* 지자체 사업자등록번호 */
	private String conbCd; /* 기부금코드 */
	private String cntrType; /* 기부자신분확인구분코드 */
	private String resCode; /* 결과코드 */
	private String resMsg; /* 결과메세지 */

	private String foreignStatusCode; /* 내/외국인 구분 코드 */
	private long prjId; /* 지정기부 ID */

	private String elcrAplCd; /* 영수증신청구분코드 */
	private String linkMngKey; /* cntr_sn */
	private String orderCode;	/* 주문번호 */

	public long getPrjId() {
		return prjId;
	}
	public void setPrjId(long prjId) {
		this.prjId = prjId;
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
	public String getSiguCd() {
		return siguCd;
	}
	public void setSiguCd(String siguCd) {
		this.siguCd = siguCd;
	}
	public String getSemokCd() {
		return semokCd;
	}
	public void setSemokCd(String semokCd) {
		this.semokCd = semokCd;
	}
	public String getSetTaxYm() {
		return setTaxYm;
	}
	public void setSetTaxYm(String setTaxYm) {
		this.setTaxYm = setTaxYm;
	}
	public String getTaxGubun() {
		return taxGubun;
	}
	public void setTaxGubun(String taxGubun) {
		this.taxGubun = taxGubun;
	}
	public String getSidoCd() {
		return sidoCd;
	}
	public void setSidoCd(String sidoCd) {
		this.sidoCd = sidoCd;
	}
	public String getNapId() {
		return napId;
	}
	public void setNapId(String napId) {
		this.napId = napId;
	}
	public String getNapNm() {
		return napNm;
	}
	public void setNapNm(String napNm) {
		this.napNm = napNm;
	}
	public String getNapGubun() {
		return napGubun;
	}
	public void setNapGubun(String napGubun) {
		this.napGubun = napGubun;
	}
	public String getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(String taxAmt) {
		this.taxAmt = taxAmt;
	}
	public String getSise() {
		return sise;
	}
	public void setSise(String sise) {
		this.sise = sise;
	}
	public String getResideStatus() {
		return resideStatus;
	}
	public void setResideStatus(String resideStatus) {
		this.resideStatus = resideStatus;
	}
	public String getMulGubun() {
		return mulGubun;
	}
	public void setMulGubun(String mulGubun) {
		this.mulGubun = mulGubun;
	}
	public String getMulNm() {
		return mulNm;
	}
	public void setMulNm(String mulNm) {
		this.mulNm = mulNm;
	}
	public String getBookNo() {
		return bookNo;
	}
	public void setBookNo(String bookNo) {
		this.bookNo = bookNo;
	}
	public String getSysGubun() {
		return sysGubun;
	}
	public void setSysGubun(String sysGubun) {
		this.sysGubun = sysGubun;
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
	public String getUserCntrPoint() {
		return userCntrPoint;
	}
	public void setUserCntrPoint(String userCntrPoint) {
		this.userCntrPoint = userCntrPoint;
	}
	public String getSttemntPayDe() {
		return sttemntPayDe;
	}
	public void setSttemntPayDe(String sttemntPayDe) {
		this.sttemntPayDe = sttemntPayDe;
	}
	public String getUserCi() {
		return userCi;
	}
	public void setUserCi(String userCi) {
		this.userCi = userCi;
	}
	public String getBizNo() {
		return bizNo;
	}
	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}
	public String getResCode() {
		return resCode;
	}
	public void setResCode(String resCode) {
		this.resCode = resCode;
	}
	public String getResMsg() {
		return resMsg;
	}
	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	public String getConbCd() {
		return conbCd;
	}
	public void setConbCd(String conbCd) {
		this.conbCd = conbCd;
	}
	public String getCntrType() {
		return cntrType;
	}
	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
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
	public String getElcrAplCd() {
		return elcrAplCd;
	}
	public void setElcrAplCd(String elcrAplCd) {
		this.elcrAplCd = elcrAplCd;
	}
	public String getLinkMngKey() {
		return linkMngKey;
	}
	public void setLinkMngKey(String linkMngKey) {
		this.linkMngKey = linkMngKey;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}

