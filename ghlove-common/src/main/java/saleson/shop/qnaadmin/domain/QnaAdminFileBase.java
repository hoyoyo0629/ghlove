package saleson.shop.qnaadmin.domain;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;

import saleson.common.file.infra.FileStorage;

public class QnaAdminFileBase {

	String fileName;		// 서버에 저장된 파일경로/명
	String fileTy;			// 파일 타입
	int ordering;			// 순서
	Timestamp createdDate;		// 등록일
	String orgFileName;			// 업로드 시 파일 명
	
	MultipartFile multipartFile;
	
	FileService fileService;
	FileStorage fileStorage;
	
	String pathName;
	
	public QnaAdminFileBase() {
		super();
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
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getOrgFileName() {
		return orgFileName;
	}
	public void setOrgFileName(String orgFileName) {
		this.orgFileName = orgFileName;
	}
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}
	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
		if (multipartFile != null) {
			setOrgFileName(multipartFile.getOriginalFilename());
			setFileTy(multipartFile.getContentType());
			if (StringUtils.isEmpty(getFileTy())) {
				setFileTy(FileUtils.getExtension(getOrgFileName()));
			}
		}
	}

	public FileService getFileService() {
		return fileService;
	}

	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public FileStorage getFileStorage() {
		return fileStorage;
	}

	public void setFileStorage(FileStorage fileStorage) {
		this.fileStorage = fileStorage;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
}
