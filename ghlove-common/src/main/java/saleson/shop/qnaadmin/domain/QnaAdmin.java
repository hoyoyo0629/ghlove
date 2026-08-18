package saleson.shop.qnaadmin.domain;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.util.ValidationUtils;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.LocalDateUtils;
import saleson.common.utils.ShopUtils;

import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import javax.validation.constraints.NotEmpty;

public class QnaAdmin {
	
	/**
	 * 상품문의 구분 1
	 */
	public static final String QNA_GROUP_TYPE_ITEM = "1";
	
	/**
	 * 1:1 문의 구분 0
	 */
	public static final String QNA_GROUP_TYPE_INDIVIDUAL = "0";
	
	
	/**
	 * 공개QNA
	 */
	public static final String QNA_GROUP_TYPE_QNA = "2";
	
	private long qnaAdminId;
	/**
	 * 2020.06.03
	 * qnaGroup 검증
	 */
	@NotEmpty
	private String qnaGroup;
	private String qnaType;

	private long userId;
	private String userName;
	private String email;

	private int itemId;
	private String itemName;
	private String itemUserCode;
	private String orderCode;
	private long answerCount;
	private String itemImage;
	/**
	 * 2015.1.8 비밀글 여부 컬럼 추가
	 */
	private String secretFlag;
	private Timestamp createdDate;
	/**
	 * 2014.12.26
	 * subject 컬럼 추가 및 검증
	 */
	@NotEmpty
	private String subject; //제목
	/**
	 * 2014.12.26
	 * 문의자 내용 검증 
	 */
	@NotEmpty
	private String question; 
	private String displayFlag;
	
	QnaAdminAnswer qnaAdminAnswer; 
	
	private String loginId;
	
	private String where;
	private String query;
	private String orderBy;
	private String sort;
	private String language;
	
	private Pagination pagination;
	
	private String searchStartDate;
	private String searchEndDate;
	
	
	private long sellerId;
	private String sellerName; 
	
	// 답변 목록
	List<QnaAdminAnswer> qnaAdminAnswers = new ArrayList<>();
	
	private String userCode;
	private String answer;

	private String qnaImage = "";

	// 문의 이미지
	private MultipartFile qnaImageFile;

	// 지자체 코드
	private String locgovCode = "";
	// 지자체 명
	private String locgovName = "";

	// 첨부 파일
	private List<MultipartFile> addFiles = new ArrayList<>();

	private String qnaGroupName;
	
	public String getItemImage() {
		return itemImage;
	}
	public void setItemImage(String itemImage) {
		this.itemImage = itemImage;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemUserCode() {
		return itemUserCode;
	}
	public void setItemUserCode(String itemUserCode) {
		this.itemUserCode = itemUserCode;
	}
	public QnaAdminAnswer getQnaAdminAnswer() {
		return qnaAdminAnswer;
	}
	public void setQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer) {
		this.qnaAdminAnswer = qnaAdminAnswer;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public long getAnswerCount() {
		return answerCount;
	}
	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}
	public String getSecretFlag() {
		return secretFlag;
	}
	public void setSecretFlag(String secretFlag) {
		this.secretFlag = secretFlag;
	}
	public long getQnaAdminId() {
		return qnaAdminId;
	}
	public void setQnaAdminId(long qnaAdminId) {
		this.qnaAdminId = qnaAdminId;
	}
	public String getQnaGroup() {
		return qnaGroup;
	}
	public void setQnaGroup(String qnaGroup) {
		this.qnaGroup = qnaGroup;
	}
	public String getQnaType() {
		return qnaType;
	}
	public void setQnaType(String qnaType) {
		this.qnaType = qnaType;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getDisplayFlag() {
		return displayFlag;
	}
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
	
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public List<QnaAdminAnswer> getQnaAdminAnswers() {
		return qnaAdminAnswers;
	}
	public void setQnaAdminAnswers(List<QnaAdminAnswer> qnaAdminAnswers) {
		this.qnaAdminAnswers = qnaAdminAnswers;
	}
	public String getMaskUsername() {
		
		try {
			if (StringUtils.isNotEmpty(this.userName)) {
				return this.userName.substring(0, this.userName.length() - 2) + "**";
			}
		} catch(OpRuntimeException e) {
			return "";
		}
		
		return "";
	}
	
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Pagination getPagination() {
		return pagination;
	}
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
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

	public String getQnaImage() {
		return qnaImage;
	}

	public void setQnaImage(String qnaImage) {
		this.qnaImage = qnaImage;
	}

	public MultipartFile getQnaImageFile() {
		return qnaImageFile;
	}

	public void setQnaImageFile(MultipartFile qnaImageFile) {
		this.qnaImageFile = qnaImageFile;
	}

	public void decrypt(DataEncryptor<QnaAdmin> dataEncryptor, boolean needMasking) {
		dataEncryptor.decrypt(this, needMasking);
	}


	public void decrypt(DataEncryptor<QnaAdmin> dataEncryptor) {
		decrypt(dataEncryptor, ShopUtils.needMasking(getUserId()));
	}

	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("inquiry")
				.toString();
	}

	public String getImageSrc() {
		if (ValidationUtils.isEmpty(this.qnaImage)) {
			return ShopUtils.getNoImagePath();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(SalesonProperty.getSalesonUrlCdn());
		sb.append(SalesonProperty.getUploadBaseFolder());
		sb.append("/inquiry/");

		sb.append(this.qnaImage);

		return sb.toString();
	}


	public void encrypt(DataEncryptor<QnaAdmin> dataEncryptor) {
		dataEncryptor.encrypt(this);
	}
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getCreatedDateStr() {
		try {
			return LocalDateUtils.getDate(createdDate.toLocalDateTime());
		} catch (OpRuntimeException e) {
			return "";
		}
	}
	public List<MultipartFile> getAddFiles() {
		return addFiles;
	}
	public void setAddFiles(List<MultipartFile> addFiles) {
		this.addFiles = addFiles;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getQnaGroupName() {
		return qnaGroupName;
	}
	public void setQnaGroupName(String qnaGroupName) {
		this.qnaGroupName = qnaGroupName;
	}
	public String getLocgovName() {
		return locgovName;
	}
	public void setLocgovName(String locgovName) {
		this.locgovName = locgovName;
	}
}
