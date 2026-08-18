package saleson.shop.qna.domain;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import saleson.common.configuration.SalesonProperty;

public class QnaOpen {
	private Integer qnaId;			// 문의 ID
	private Integer qnaAnswerId;	// 문의 답변 ID
	private String createdDate;		// 등록일
	private Long userId;			// 회원 ID
	private String userName;		// 회원명
	private String qnaDetailType;	// Q&A 상세 타입 (Q:질문, A:답변)
	private String subject;			// 제목
	private Integer hits;			// 조회수
	private String secretFlag;		// 비밀글 여부 (Y:비밀글, N:일반)
	private String question;		// 내용
	private Integer answerCount;	// 문의답변 수
	private String prevQnaInfo;		// 이전글 QNA 정보 	(ex: qnaId|qnaAnswerId|subject)
	private String nextQnaInfo;		// 다음글 QNA 정보 	(ex: qnaId|qnaAnswerId|subject)
	private String email;			// 회원 이메일
	private String qnaType;			// 2차 분류 (0: 1:1문의,1: 상품문의, 2: QNA)
	private String qnaGroup;		// 1차 분류 (공통코드 codeType : QNA_GROUPS)
	
	private String myCheck;			// 본인글 확인
	private Long qnaUserId;			// QNA 작성자 UserId(답변글)
	private String qnaSecretFlag;	// QNA 비밀글여부(답변글)
	
	private String loginId;

	/* 첨부파일 */
	private List<MultipartFile> qnaOpenFileList;
	private List<Integer> delQnaOpenFileList;
	
	/* 답변 관련 */
	private String answerCnt;		//답변 카운트
	private Long answerUserId;		//답변 userId
	private String answerLoginId;	//답변자 아이디
	private String answerUserName;	//답변자명
	private String answerDate;		//답변일
	
	QnaAnswer qnaAnswer;
	
	public Integer getQnaId() {
		return qnaId;
	}
	public void setQnaId(Integer qnaId) {
		this.qnaId = qnaId;
	}
	public Integer getQnaAnswerId() {
		return qnaAnswerId;
	}
	public void setQnaAnswerId(Integer qnaAnswerId) {
		this.qnaAnswerId = qnaAnswerId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getQnaDetailType() {
		return qnaDetailType;
	}
	public void setQnaDetailType(String qnaDetailType) {
		this.qnaDetailType = qnaDetailType;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Integer getHits() {
		return hits;
	}
	public void setHits(Integer hits) {
		this.hits = hits;
	}
	public String getSecretFlag() {
		return secretFlag;
	}
	public void setSecretFlag(String secretFlag) {
		this.secretFlag = secretFlag;
	}
	public String getPrevQnaInfo() {
		return prevQnaInfo;
	}
	public void setPrevQnaInfo(String prevQnaInfo) {
		this.prevQnaInfo = prevQnaInfo;
	}
	public String getNextQnaInfo() {
		return nextQnaInfo;
	}
	public void setNextQnaInfo(String nextQnaInfo) {
		this.nextQnaInfo = nextQnaInfo;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQnaType() {
		return qnaType;
	}
	public void setQnaType(String qnaType) {
		this.qnaType = qnaType;
	}
	public String getQnaGroup() {
		return qnaGroup;
	}
	public void setQnaGroup(String qnaGroup) {
		this.qnaGroup = qnaGroup;
	}
	public List<MultipartFile> getQnaOpenFileList() {
		return qnaOpenFileList;
	}
	public void setQnaOpenFileList(List<MultipartFile> qnaOpenFileList) {
		this.qnaOpenFileList = qnaOpenFileList;
	}
	public Integer getAnswerCount() {
		return answerCount;
	}
	public void setAnswerCount(Integer answerCount) {
		this.answerCount = answerCount;
	}
	public List<Integer> getDelQnaOpenFileList() {
		return delQnaOpenFileList;
	}
	public void setDelQnaOpenFileList(List<Integer> delQnaOpenFileList) {
		this.delQnaOpenFileList = delQnaOpenFileList;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getAnswerCnt() {
		return answerCnt;
	}
	public void setAnswerCnt(String answerCnt) {
		this.answerCnt = answerCnt;
	}
	public Long getAnswerUserId() {
		return answerUserId;
	}
	public void setAnswerUserId(Long answerUserId) {
		this.answerUserId = answerUserId;
	}
	public String getAnswerLoginId() {
		return answerLoginId;
	}
	public void setAnswerLoginId(String answerLoginId) {
		this.answerLoginId = answerLoginId;
	}
	public String getAnswerUserName() {
		return answerUserName;
	}
	public void setAnswerUserName(String answerUserName) {
		this.answerUserName = answerUserName;
	}
	public String getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(String answerDate) {
		this.answerDate = answerDate;
	}
	public QnaAnswer getQnaAnswer() {
		return qnaAnswer;
	}
	public void setQnaAnswer(QnaAnswer qnaAnswer) {
		this.qnaAnswer = qnaAnswer;
	}
	public String getMyCheck() {
		return myCheck;
	}
	public void setMyCheck(String myCheck) {
		this.myCheck = myCheck;
	}
	public Long getQnaUserId() {
		return qnaUserId;
	}
	public void setQnaUserId(Long qnaUserId) {
		this.qnaUserId = qnaUserId;
	}
	public String getQnaSecretFlag() {
		return qnaSecretFlag;
	}
	public void setQnaSecretFlag(String qnaSecretFlag) {
		this.qnaSecretFlag = qnaSecretFlag;
	}
	@JsonIgnore
	public String getUploadPath() {
		String uploadFolder = "qna-open";

		if("0".equals(qnaType)) {
			uploadFolder = "qna";
		}

		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append(uploadFolder)
				.toString();
	}
}
