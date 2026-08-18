package saleson.shop.specialityitem.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import saleson.shop.item.domain.Item;

public class SpecialityItem extends Item {

	// 특산물관 관리 아이디
	private long specialityItemManageId;
	
	// 등록된 상품
//	private long specialityItemId;
	
	// 표시 순서
	private int displayOrder;
	
	private String keywords;
	
	private long frstRegisterId;
	
	private Timestamp frstRegistPnttm;
	
	private long lastUpdusrId;
	
	private Timestamp lastUpdtPnttm;
	

	public SpecialityItem() {
		
	}


	public long getSpecialityItemManageId() {
		return specialityItemManageId;
	}


	public void setSpecialityItemManageId(long specialityItemManageId) {
		this.specialityItemManageId = specialityItemManageId;
	}


//	public long getSpecialityItemId() {
//		return specialityItemId;
//	}
//
//
//	public void setSpecialityItemId(long specialityItemId) {
//		this.specialityItemId = specialityItemId;
//	}


	public int getDisplayOrder() {
		return displayOrder;
	}


	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}


	public String getKeywords() {
		return keywords;
	}


	public void setKeywords(String keywords) {
		this.keywords = keywords;
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
