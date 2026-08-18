package saleson.shop.qna.domain;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.util.ValidationUtils;

import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;

public class QnaOpenFile implements Cloneable {
	private String qnaDetailType;
	private String qnaFileId;
	private Integer qnaId;
	private String fileName;
	private String fileTy;
	private Integer ordering;
	private String createdDate;
	private String orgFileName;
	private String qnaType;

	public String getQnaFileId() {
		return qnaFileId;
	}
	public void setQnaFileId(String qnaFileId) {
		this.qnaFileId = qnaFileId;
	}
	public Integer getQnaId() {
		return qnaId;
	}
	public void setQnaId(Integer qnaId) {
		this.qnaId = qnaId;
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
	public String getQnaDetailType() {
		return qnaDetailType;
	}
	public void setQnaDetailType(String qnaDetailType) {
		this.qnaDetailType = qnaDetailType;
	}
	public String getQnaType() {
		return qnaType;
	}
	public void setQnaType(String qnaType) {
		this.qnaType = qnaType;
	}
	public String getFileSrc() {
		if (ValidationUtils.isEmpty(this.fileName)) {
			return ShopUtils.getNoImagePath();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(SalesonProperty.getUploadSaveFolder());
		sb.append(File.separator);

		if("Q".equals(this.qnaDetailType) && "2".equals(this.qnaType)) {
			sb.append("qna-open");
		} else if("Q".equals(this.qnaDetailType) && "0".equals(this.qnaType)) {
			sb.append("qna");
		} else if("A".equals(this.qnaDetailType) && "2".equals(this.qnaType)) {
			sb.append("qna-open-answer");
		} else {
			sb.append("qna-answer");
		}

		sb.append(File.separator);
		sb.append(this.fileName);
		return sb.toString();
	}

	@JsonIgnore
	public String getUploadPath() {
		String uploadFolder = "qna-open";

		if("0".equals(this.qnaType)) {
			uploadFolder = "qna";
		}

		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append(uploadFolder)
				.toString();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
