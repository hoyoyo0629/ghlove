package saleson.shop.qnaadmin.domain;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.security.DataEncryptor;

import saleson.common.utils.LocalDateUtils;

public class QnaAdminAnswer {
	
	private long qnaAdminAnswerId;
	private long qnaAdminId;
	private String answer;
	private long userId;
	
	//2014.1.8 답변자 제목 컬럼 추가
	//public - > private으로 변경
	private String title; 
	//2014.1.14 SMS 추가
	private String sendSmsFlag;
	//2014.1.13 MAIL 추가
	private String sendMailFlag;
	private Timestamp answerDate;
	
	private String userNm;
	
	private List<MultipartFile> addFiles;
	
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
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSendSmsFlag() {
		return sendSmsFlag;
	}
	public void setSendSmsFlag(String sendSmsFlag) {
		this.sendSmsFlag = sendSmsFlag;
	}
	public String getSendMailFlag() {
		return sendMailFlag;
	}
	public void setSendMailFlag(String sendMailFlag) {
		this.sendMailFlag = sendMailFlag;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	

	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(DataEncryptor<QnaAdminAnswer> encryptor) {
		encryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor<QnaAdminAnswer> encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}
	public Timestamp getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(Timestamp answerDate) {
		this.answerDate = answerDate;
	}

	public String getAnswerDateStr() {
		try {
			return LocalDateUtils.getDate(answerDate.toLocalDateTime());
		} catch (RuntimeException e) {
			return "";
		}
	}
	public List<MultipartFile> getAddFiles() {
		return addFiles;
	}
	public void setAddFiles(List<MultipartFile> addFiles) {
		this.addFiles = addFiles;
	}
}
