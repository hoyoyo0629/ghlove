package saleson.shop.remittance.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import saleson.shop.remittance.domain.RemittanceFile;

public class RemittanceFileSupport implements Serializable {
	private static final long serialVersionUID = 1918754599200850455L;

	private final Logger log = LoggerFactory.getLogger(RemittanceFileSupport.class);

	private long remittanceId;
	private String remittanceStatusCode;
	
	private List<RemittanceFile> remittanceFileList;

	public RemittanceFileSupport() {
		
	}

	public long getRemittanceId() {
		return remittanceId;
	}

	public void setRemittanceId(long remittanceId) {
		this.remittanceId = remittanceId;
	}

	public String getRemittanceStatusCode() {
		return remittanceStatusCode;
	}

	public void setRemittanceStatusCode(String remittanceStatusCode) {
		this.remittanceStatusCode = remittanceStatusCode;
	}

	public List<RemittanceFile> getRemittanceFileList() {
		if (remittanceFileList == null) {
			return null;
		} else {
			List<RemittanceFile> list = new ArrayList<>();
			for (RemittanceFile remittanceFile : remittanceFileList) {
				list.add(remittanceFile);
			}
			return list;
		}
	}

	public void setRemittanceFileList(List<RemittanceFile> remittanceFileList) {
		if (remittanceFileList == null) {
			this.remittanceFileList = null;
		} else {
			this.remittanceFileList = new ArrayList<>();
			for (RemittanceFile remittanceFile : remittanceFileList) {
				this.remittanceFileList.add(remittanceFile);
			}
		}
	}
	
}
