package saleson.common.file.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.file.FileDownloadCustom;
import saleson.common.file.infra.FileStorage;

@Slf4j
@RequiredArgsConstructor
@Service("customFileService")
public class CustomFileService {			// 테스트 필요

	private final FileService fileService;
	private final FileStorage fileStorage;

	private final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "hwpx","zip"};

	/**
	 * 실제 파일 저장 경로	ex) '/storage/upload/item/1'
	 * @param programName
	 * @param programId
	 * @return
	 */
	private String getUploadFilePath(String programName, long programId) {
		return new StringBuffer().append(SalesonProperty.getUploadSaveFolder())
				.append("/" + programName + "/")
				.append(programId)
				.append("/")
				.toString();
	}

	/**
	 * DB 저장할 파일경로 ex) '/item/1'
	 * @param programName
	 * @param programId
	 * @return
	 */
	public String getFilePathForSave(String programName, long programId) {
		return new StringBuffer()
				.append("/" + programName + "/")
				.append(programId)
				.append("/")
				.toString();
	}

	/**
	 * 프로그램명 및 아이디로(ex : 'item', 1) 파일 저장 후 저장된 파일명(저장시간) 리턴
	 * @param programName
	 * @param programId
	 * @param multipartFile
	 * @return
	 */
	public String saveFileByProgramNameId(String programName, long programId, MultipartFile multipartFile) throws IOException {
		String uploadPath = getUploadFilePath(programName, programId);
		fileService.makeUploadPath(uploadPath);

		File saveFile = getFile(multipartFile, uploadPath, 1);
		String fileName = saveFile.getName();
		try {
			fileStorage.upload(multipartFile.getBytes(), saveFile);
		} catch (IOException e) {
			log.error("CustomFileService saveFile error", e);
//			saveFile = null;
//			fileName = "";
			throw e;
		}
		return fileName;
	}

	/**
	 * 경로(ex : '/storage/upload/item/1')로 파일 저장 후 저장된 파일명(저장시간) 리턴
	 * @param programName
	 * @param programId
	 * @param multipartFile
	 * @return
	 */
	public String saveFileByPath(String pathName, MultipartFile multipartFile) throws IOException {
		fileService.makeUploadPath(pathName);

		File saveFile = getFile(multipartFile, pathName, 1);
		String fileName = saveFile.getName();
		try {
			fileStorage.upload(multipartFile.getBytes(), saveFile);
		} catch (IOException e) {
			log.error("CustomFileService saveFile error", e);
//			saveFile = null;
//			fileName = "";
			throw e;
		}
		return fileName;
	}

	private File getFile(MultipartFile multipartFile, String uploadPath, int seq) {
		String ext = FileUtils.getExtension(multipartFile.getOriginalFilename());

		boolean isAvailable = false;
		for (String string : AVAILABLE_EXTENSION) {
			if (string.equalsIgnoreCase(ext)) {
				isAvailable = true;
				break;
			}
		}
		if (!isAvailable) {
			throw new UserException(ext + " 파일은 업로드 불가합니다.");
		}

		String fileName = DateUtils.getToday(Const.DATENANO_FORMAT) + "." + ext;
		fileName = FileUtils.getNewFileName(uploadPath, fileName);
		File saveFile = new File(uploadPath + "/" + fileName);
		if (saveFile != null && saveFile.exists()) {
			if (seq > 10) {		// 10회 실패할 경우
//				return null;
				throw new UserException("저장에 실패했습니다.");
			} else {
				return getFile(multipartFile, uploadPath, ++seq);
			}
		} else {
			return saveFile;
		}
	}

	/**
	 * 파일 저장경로(ex : '/item/1'), 저장된 파일명, 다운로드할 파일명으로 파일 다운로드 진행
	 * @param pathName
	 * @param fileName
	 * @param orgFileName
	 * @return
	 */
	public ResponseEntity<byte[]> getFileDownloadDataByPathFileOrgName(String pathName, String fileName, String orgFileName) throws IOException {
		try {
			return FileDownloadCustom.fileDownloadCustom(SalesonProperty.getUploadSaveFolder()
					+ pathName + "/" + fileName, orgFileName);
		} catch (IOException e) {
			log.error("CustomFileService getFileDownloadDataByPathFileOrgName error", e);
			throw e;
		}
	}

	/**
	 * 프로그램 명(ex : 'item'), 프로그램 아이디(ex : 1), 저장된 파일명(ex : '20240101100000.jpg')으로 파일 다운로드 진행
	 * @param programName
	 * @param programId
	 * @param filename
	 * @return
	 */
	public ResponseEntity<byte[]> getFileDownloadDataByProgramNameIdFileName(String programName, long programId, String filename) throws IOException {
		try {
			return FileDownloadCustom.fileDownloadCustom(getUploadFilePath(programName, programId) + filename, filename);
		} catch (IOException e) {
			log.error("CustomFileService getFileDownloadDataByProgramNameIdFileName error", e);
			throw e;
		}
	}

	/**
	 * 프로그램 명(ex : 'item'), 프로그램 아이디(ex : 1), 저장된 파일명(ex : '20240101100000.jpg'), 다운로드할 파일명(ex : 'background.jpg')으로 파일 다운로드 진행
	 * @param programName
	 * @param programId
	 * @param filename
	 * @param orgFileName
	 * @return
	 */
	public ResponseEntity<byte[]> getFileDownloadDataByProgramNameIdFileOrgName(String programName, long programId, String filename, String orgFileName) throws IOException {
		try {
			return FileDownloadCustom.fileDownloadCustom(getUploadFilePath(programName, programId) + filename, orgFileName);
		} catch (IOException e) {
			log.error("CustomFileService getFileDownloadDataByProgramNameIdFileOrgName error", e);
			throw e;
		}
	}

	/**
	 * 파일 경로(ex: '/item/1')와 파일명(ex : '20240101100000.jpg')으로 파일삭제
	 * @param pathName
	 * @param fileName
	 */
	public void deleteFileByPathFileName(String pathName, String fileName) {
		fileStorage.delete(pathName, fileName);
	}

	/**
	 * 프로그램명(ex: 'item')과 프로그램아이디(ex : 1)와 파일명(ex : '20240101100000.jpg')으로 파일삭제
	 * @param programName
	 * @param programId
	 * @param fileName
	 */
	public void deleteFileByProgramNameId(String programName, long programId, String fileName) {
		fileStorage.delete(getUploadFilePath(programName, programId), fileName);
	}

	public ResponseEntity<ByteArrayResource> getFileDownloadByGhlove(String reqFileName, File file) throws FileNotFoundException, IOException {
		return FileDownloadCustom.fileDownload(reqFileName, file);
	}
}
