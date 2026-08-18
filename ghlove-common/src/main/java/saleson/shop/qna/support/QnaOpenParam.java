package saleson.shop.qna.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class QnaOpenParam extends SearchParam {
	private Integer qnaId;			// 문의 ID
	private Integer qnaAnswerId;	// 문의 답변 ID
	private String createdDate;		// 등록일
	private Integer userId;			// 회원 ID
	private String userName;		// 회원명
	private String qnaDetailType;	// Q&A 상세 타입 (Q:질문, A:답변)
	private String subject;			// 제목
	private Integer hits;			// 조회수
	private String secretFlag;		// 비밀글 여부 (Y:비밀글, N:일반)
	private String qnaFileId;		// 문의 파일 ID

	private String searchStartDate;
	private String searchEndDate;

	private String qnaOpenAnswerCode;

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
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
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
	public String getQnaFileId() {
		return qnaFileId;
	}
	public void setQnaFileId(String qnaFileId) {
		this.qnaFileId = qnaFileId;
	}
	public String getSearchStartDate() {
		return searchStartDate;
	}
	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}
	public String getSearchEndDate() {
		return searchEndDate;
	}
	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}
	public String getQnaOpenAnswerCode() {
		return qnaOpenAnswerCode;
	}
	public void setQnaOpenAnswerCode(String qnaOpenAnswerCode) {
		this.qnaOpenAnswerCode = qnaOpenAnswerCode;
	}
}
