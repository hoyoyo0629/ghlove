package saleson.shop.specialityitem.support;

import java.sql.Timestamp;
import java.util.List;

import com.onlinepowers.framework.web.domain.SearchParam;

public class SpecialityItemParam extends SearchParam {

	/**
	 *
	 */
	private static final long serialVersionUID = -1643346787834956843L;

	private long specialityItemManageId;
	private String upperLocgovCode;		// 시도 코드
	private String locgovCode;			// 시군구 코드
	private String keywords;			// 검색 키워드

	// 선택 상품(저장시 파라미터)
	private String prodString;

	// 전용상품 구분
	private List<String> privateTypes;
	private String privateType;

	private int regSeq;

	private long frstRegisterId;

	private Timestamp frstRegistPnttm;

	private long lastUpdusrId;

	private Timestamp lastUpdtPnttm;

	public SpecialityItemParam() {

	}
	public String getUpperLocgovCode() {
		return upperLocgovCode;
	}
	public void setUpperLocgovCode(String upperLocgovCode) {
		this.upperLocgovCode = upperLocgovCode;
	}
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public long getSpecialityItemManageId() {
		return specialityItemManageId;
	}
	public void setSpecialityItemManageId(long specialityItemManageId) {
		this.specialityItemManageId = specialityItemManageId;
	}
	public String getProdString() {
		return prodString;
	}
	public void setProdString(String prodString) {
		this.prodString = prodString;
	}
	public List<String> getPrivateTypes() {
		return privateTypes;
	}
	public void setPrivateTypes(List<String> privateTypes) {
		this.privateTypes = privateTypes;
	}
	public String getPrivateType() {
		return privateType;
	}
	public void setPrivateType(String privateType) {
		this.privateType = privateType;
	}
	public int getRegSeq() {
		return regSeq;
	}
	public void setRegSeq(int regSeq) {
		this.regSeq = regSeq;
	}
	public long getFrstRegisterId() {
		return frstRegisterId;
	}
	public void setFrstRegisterId(long frstRegisterId) {
		this.frstRegisterId = frstRegisterId;
	}
	public Timestamp getFrstRegistPnttm() {
		return frstRegistPnttm;
	}
	public void setFrstRegistPnttm(Timestamp frstRegistPnttm) {
		this.frstRegistPnttm = frstRegistPnttm;
	}
	public long getLastUpdusrId() {
		return lastUpdusrId;
	}
	public void setLastUpdusrId(long lastUpdusrId) {
		this.lastUpdusrId = lastUpdusrId;
	}
	public Timestamp getLastUpdtPnttm() {
		return lastUpdtPnttm;
	}
	public void setLastUpdtPnttm(Timestamp lastUpdtPnttm) {
		this.lastUpdtPnttm = lastUpdtPnttm;
	}

}
