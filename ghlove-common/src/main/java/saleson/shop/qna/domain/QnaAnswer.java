package saleson.shop.qna.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.security.DataEncryptor;

import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.CommonUtils;

public class QnaAnswer {

	private int qnaAnswerId;
	private int qnaId;
	private String answer;
	private long userId;

	//2014.1.8 답변자 제목 컬럼 추가
	//public - > private으로 변경
	private String title;
	//2014.1.14 SMS 추가
	private String sendSmsFlag;
	//2014.1.13 MAIL 추가
	private String sendMailFlag;
	private String answerDate;

	private String userNm;
	private String loginId;
	private String roleNm;
	private String qnaDetailType;
	private String answerLoginId;

	private String dataStatusCode;


	List<QnaOpenFile> qnaFiles = new ArrayList<>();

	private MultipartFile[] itemDetailImageFiles;

	public String getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(String answerDate) {
		this.answerDate = answerDate;
	}
	public int getQnaAnswerId() {
		return qnaAnswerId;
	}
	public void setQnaAnswerId(int qnaAnswerId) {
		this.qnaAnswerId = qnaAnswerId;
	}
	public int getQnaId() {
		return qnaId;
	}
	public void setQnaId(int qnaId) {
		this.qnaId = qnaId;
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
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getRoleNm() {
		return roleNm;
	}
	public void setRoleNm(String roleNm) {
		this.roleNm = roleNm;
	}
	public MultipartFile[] getItemDetailImageFiles() {
		return CommonUtils.copy(itemDetailImageFiles);
	}
	public void setItemDetailImageFiles(MultipartFile[] itemDetailImageFiles) {
		this.itemDetailImageFiles = CommonUtils.copy(itemDetailImageFiles);
	}
	public String getQnaDetailType() {
		return qnaDetailType;
	}
	public void setQnaDetailType(String qnaDetailType) {
		this.qnaDetailType = qnaDetailType;
	}
	public String getAnswerLoginId() {
		return answerLoginId;
	}
	public void setAnswerLoginId(String answerLoginId) {
		this.answerLoginId = answerLoginId;
	}

	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(DataEncryptor encryptor) {
		encryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}
	public List<QnaOpenFile> getQnaFiles() {
		return qnaFiles;
	}
	public void setQnaFiles(List<QnaOpenFile> qnaFiles) {
		this.qnaFiles = qnaFiles;
	}

	@JsonIgnore
	public String getUploadPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(SalesonProperty.getUploadSaveFolder());
		sb.append(File.separator);

		if("qna-open".equals(this.qnaDetailType)) {
			sb.append("qna-open-answer");
		} else {
			sb.append("qna-answer");
		}

		sb.append(File.separator);
		return sb.toString();
	}

	public String getDataStatusCode() {
		return dataStatusCode;
	}
	public void setDataStatusCode(String dataStatusCode) {
		this.dataStatusCode = dataStatusCode;
	}

}
