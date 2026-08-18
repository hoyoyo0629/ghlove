package saleson.shop.user.domain;

import java.sql.Timestamp;

public class LocgovItemImage {
	
	private String locgovCode;
	
	private String pcFileName;
	
	private String mobileFileName;
	
	private long frstRegisterId;
	
	private Timestamp frstRegistPnttm;
	
	private long lastUpdusrId;
	
	private Timestamp lastUpdtPnttm;

	public LocgovItemImage() {
		super();
	}

	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

	public String getPcFileName() {
		return pcFileName;
	}

	public void setPcFileName(String pcFileName) {
		this.pcFileName = pcFileName;
	}

	public String getMobileFileName() {
		return mobileFileName;
	}

	public void setMobileFileName(String mobileFileName) {
		this.mobileFileName = mobileFileName;
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
