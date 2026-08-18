package saleson.shop.specialityitem.domain;

import java.sql.Timestamp;
import java.util.List;

public class SpecialityItemManage {

	// 특산물관 관리 아이디
	private long specialityItemManageId;
	
	// 지자체 코드
	private String locgovCode;
	
	// 특산물 소개
	private String specialityItemInfo;
	
	// 키워드
	private String keywords;
	
	// 지자체 명
	private String locgovNm;
	
	// 선택 상품(호출시 데이터)
	private List<SpecialityItem> specialityItems;
	
	// 선택 상품(저장시 파라미터)
	private String prodString;

	
	private long frstRegisterId;
	
	private Timestamp frstRegistPnttm;
	
	private long lastUpdusrId;
	
	private Timestamp lastUpdtPnttm;
	
	private long rownum;
	

	public SpecialityItemManage() {
		
	}

	public long getSpecialityItemManageId() {
		return specialityItemManageId;
	}

	public void setSpecialityItemManageId(long specialityItemManageId) {
		this.specialityItemManageId = specialityItemManageId;
	}

	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

	public String getSpecialityItemInfo() {
		return specialityItemInfo;
	}

	public void setSpecialityItemInfo(String specialityItemInfo) {
		this.specialityItemInfo = specialityItemInfo;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public List<SpecialityItem> getSpecialityItems() {
		return specialityItems;
	}

	public void setSpecialityItems(List<SpecialityItem> specialityItems) {
		this.specialityItems = specialityItems;
	}

	public String getLocgovNm() {
		return locgovNm;
	}

	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}

	public String getProdString() {
		return prodString;
	}

	public void setProdString(String prodString) {
		this.prodString = prodString;
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

	public long getRownum() {
		return rownum;
	}

	public void setRownum(long rownum) {
		this.rownum = rownum;
	}
}
