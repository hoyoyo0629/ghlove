package saleson.shop.sellerconfirm.domain;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import saleson.seller.main.domain.Seller;
import saleson.shop.remittance.support.RemittanceFileSupport;

public class SellerconfirmFile extends Seller {
	
	private final Logger log = LoggerFactory.getLogger(RemittanceFileSupport.class);

	private static final long serialVersionUID = 808238530632506486L;
	private long remittanceId;
	private int fileSeq;
	private String fileName;
	private String pathName;
	private String orgFileName;
	private String remittanceStatusCode;
	private long frstRegisterId;
	private Timestamp frstRegistPnttm;
	private long lastUpdusrId;
	private Timestamp lastUpdtPnttm;

	public SellerconfirmFile() {}

	public long getRemittanceId() {
		return remittanceId;
	}

	public void setRemittanceId(long remittanceId) {
		this.remittanceId = remittanceId;
	}

	public int getFileSeq() {
		return fileSeq;
	}

	public void setFileSeq(int fileSeq) {
		this.fileSeq = fileSeq;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOrgFileName() {
		return orgFileName;
	}

	public void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
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

	public String getRemittanceStatusCode() {
		return remittanceStatusCode;
	}

	public void setRemittanceStatusCode(String remittanceStatusCode) {
		this.remittanceStatusCode = remittanceStatusCode;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public String getUploadDate() {
		return lastUpdtPnttm.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
	
}
