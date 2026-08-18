package saleson.shop.donation.support;

import com.onlinepowers.framework.web.domain.ListParam;

public class StandardParam extends ListParam{

	private String siguCd;			/* 시구코드 */
	private String semokCd;			/* 세목코드 */
	private String setTaxYm;		/* 과세년월 */
	private String taxGubun;		/* 과세구분 */
	private String sidoCd;			/* 시도코드 */
	
	private String napId;			/* 납세자ID */
	private String napNm;			/* 납세자명 */
	private String napGubun;		/* 납세자구분 */
	private int taxAmt;				/* 과세금액 */
	private String sise;			/* 시세 */
	
	private String resideStatus;	/* 거주상태 */
	private String mulGubun;		/* 물건구분 */
	private String mulNm;			/* 물건명 */
	private String bookNo;			/* 대장번호 */
	private String sysGubun;		/* 시스템구분 */
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
	public int getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(int taxAmt) {
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
	
}

