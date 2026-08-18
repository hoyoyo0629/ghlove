package saleson.shop.databoard.domain;

import java.io.File;

import com.onlinepowers.framework.util.ValidationUtils;

import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;

public class DataboardFile {
	private String dataFileId;
	private Integer dataId;
	private String fileName;
	private String fileTy;
	private Integer ordering;
	private String createdDate;
	private String orgFileName;

	public String getDataFileId() {
		return dataFileId;
	}
	public void setDataFileId(String dataFileId) {
		this.dataFileId = dataFileId;
	}
	public Integer getDataId() {
		return dataId;
	}
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileTy() {
		return fileTy;
	}
	public void setFileTy(String fileTy) {
		this.fileTy = fileTy;
	}
	public Integer getOrdering() {
		return ordering;
	}
	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getOrgFileName() {
		return orgFileName;
	}
	public void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
	}

	public String getFileSrc() {
		if (ValidationUtils.isEmpty(this.fileName)) {
			return ShopUtils.getNoImagePath();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(SalesonProperty.getUploadSaveFolder());
		sb.append(File.separator);
		sb.append("data-board");
		sb.append(File.separator);
		sb.append(this.fileName);
		return sb.toString();
	}
}
