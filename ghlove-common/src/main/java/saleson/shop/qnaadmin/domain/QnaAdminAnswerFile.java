package saleson.shop.qnaadmin.domain;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;

import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;

public class QnaAdminAnswerFile extends QnaAdminFileBase {

	private long qnaAdminAnswerFileId;		// 관리자 문의답변 파일 아이디
	
	private long qnaAdminAnswerId;			// 관리자 문의답변 아이디
	
	private long qnaAdminId;
	
	public QnaAdminAnswerFile() {
		super();
	}

	@Override
	public void setMultipartFile(MultipartFile multipartFile) {
		super.setMultipartFile(multipartFile);
		
		if (fileService == null || fileStorage == null || multipartFile == null) {
			return;
		}
		// 1. 업로드 경로설정
		String uploadPath = getUploadPath();
		fileService.makeUploadPath(uploadPath);

		fileName = DateUtils.getToday(Const.DATENANO_FORMAT) + "." + FileUtils.getExtension(multipartFile.getOriginalFilename());
		
		// 2. 파일명 중복파일 삭제
		fileStorage.delete(uploadPath, ShopUtils.unescapeHtml(fileName));

		// 2-1. 새로운 파일명.
		fileName = FileUtils.getNewFileName(uploadPath, fileName);

		// 3. 저장될 파일
		File saveFile = new File(uploadPath + File.separator + fileName);

		// 생성.
		try {
			fileStorage.upload(multipartFile.getBytes(), saveFile);
		} catch (IOException e) {
			// 파일 저장 실패
			fileName = "";
			fileTy = "";
			orgFileName = "";
			this.multipartFile = null;
		}
	}

	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("qnaadminanswer")
				.append(File.separator)
				.append(qnaAdminId)
				.append(File.separator)
				.toString();
	}

	public long getQnaAdminAnswerFileId() {
		return qnaAdminAnswerFileId;
	}

	public void setQnaAdminAnswerFileId(long qnaAdminAnswerFileId) {
		this.qnaAdminAnswerFileId = qnaAdminAnswerFileId;
	}

	public long getQnaAdminAnswerId() {
		return qnaAdminAnswerId;
	}

	public void setQnaAdminAnswerId(long qnaAdminAnswerId) {
		this.qnaAdminAnswerId = qnaAdminAnswerId;
	}

	public long getQnaAdminId() {
		return qnaAdminId;
	}

	public void setQnaAdminId(long qnaAdminId) {
		this.qnaAdminId = qnaAdminId;
	}
	
}
