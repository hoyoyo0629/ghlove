package saleson.shop.item.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.util.StringUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;
import saleson.model.review.ItemReviewFilter;

import java.io.File;
import java.util.List;

public class ItemReview {
	private int itemReviewId;
	private int itemId;
	private String itemCode;
	private String subject;
	private String content;
	private int score;
	private String recommendFlag;
	private long userId;
	private String userName;
	private long sellerId;
	private String sellerName;
	private String displayFlag = "N";
	private String createdDate;
	private String pointPayment = "N";
	private int point;
	private String pointPaymentDate;

	private List<ItemReviewImage> itemReviewImages;

	private ItemBase item;
	
	private String recommendFlagCheck;
	
	private String starScore;
	
	private String loginId;
	private String orderCode; //kye 추가

	private String displayOptionsFlag = "N";
	private String options;
	private String adminComment;
	private int likeCount = 0;

	private List<ItemReviewFilter> itemReviewFilters;
	
	private String shLocgovName;		// 지자체 명 추가
	
	private long rownum;
	private String answerLoginId;
	private String answerDate;
	private String answerAdminName;
	
	private String sellerCompanyName;

	public String getStarScore() {
		return starScore;
	}
	public void setStarScore(String starScore) {
		this.starScore = starScore;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	// 상품리뷰이미지
	private MultipartFile itemReviewImageFile;
	private List<MultipartFile> itemReviewImageFiles;

	public String getPointPayment() {
		return pointPayment;
	}
	public void setPointPayment(String pointPayment) {
		this.pointPayment = pointPayment;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getPointPaymentDate() {
		return pointPaymentDate;
	}
	public void setPointPaymentDate(String pointPaymentDate) {
		this.pointPaymentDate = pointPaymentDate;
	}
	public int getItemReviewId() {
		return itemReviewId;
	}
	public void setItemReviewId(int itemReviewId) {
		this.itemReviewId = itemReviewId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getRecommendFlag() {
		return recommendFlag;
	}
	public void setRecommendFlag(String recommendFlag) {
		this.recommendFlag = recommendFlag;
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
	public String getDisplayFlag() {
		return displayFlag;
	}
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public ItemBase getItem() {
		return item;
	}
	public void setItem(ItemBase item) {
		this.item = item;
	}

	/**
	 * 리뷰 이미지 경로
	 * @return
	 */
	@JsonIgnore
	public String getUploadPath() {

		StringBuilder sb = new StringBuilder();
		sb.append(SalesonProperty.getUploadSaveFolder());
		sb.append(File.separator);
		sb.append("item-review");
		sb.append(File.separator);
		sb.append(this.itemReviewId);

		return sb.toString();
	}

	public String getThumbnailSrc() {

		if (this.itemReviewImages != null && !this.itemReviewImages.isEmpty()) {
			for (ItemReviewImage itemReviewImage : this.itemReviewImages) {
				StringBuilder sb = itemReviewImage.getDefaultSrc();
				if (itemReviewImage.getReviewImage() != null && !itemReviewImage.getReviewImage().contains("/")) {			// 이관하지 않은 후기 파일만..
					sb.append("/thumb_");
				}
				sb.append(itemReviewImage.getReviewImage());

				return ObjectUtils.isEmpty(itemReviewImage.getReviewImage()) ? ShopUtils.getNoImagePath() : sb.toString();
			}
		}

		return ShopUtils.getNoImagePath();
	}

	public String getRecommendFlagCheck() {
		return recommendFlagCheck;
	}
	public void setRecommendFlagCheck(String recommendFlagCheck) {
		this.recommendFlagCheck = recommendFlagCheck;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	public String getMaskUsername() {
		
		try {
			if (StringUtils.isNotEmpty(this.userName)) {
				return this.userName.substring(0, this.userName.length() - 2) + "**";
			}
		} catch(RuntimeException e) {
			return "";
		}
		
		return "";
	}
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public List<ItemReviewImage> getItemReviewImages() {
		return itemReviewImages;
	}

	public void setItemReviewImages(List<ItemReviewImage> itemReviewImages) {
		this.itemReviewImages = itemReviewImages;
	}

	public MultipartFile getItemReviewImageFile() {
		return itemReviewImageFile;
	}

	public void setItemReviewImageFile(MultipartFile itemReviewImageFile) {
		this.itemReviewImageFile = itemReviewImageFile;
	}

	public List<MultipartFile> getItemReviewImageFiles() {
		return itemReviewImageFiles;
	}

	public void setItemReviewImageFiles(List<MultipartFile> itemReviewImageFiles) {
		this.itemReviewImageFiles = itemReviewImageFiles;
	}

	public String getDisplayOptionsFlag() {
		return displayOptionsFlag;
	}

	public void setDisplayOptionsFlag(String displayOptionsFlag) {
		this.displayOptionsFlag = displayOptionsFlag;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getAdminComment() {
		return adminComment;
	}

	public void setAdminComment(String adminComment) {
		this.adminComment = adminComment;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public List<ItemReviewFilter> getItemReviewFilters() {
		return itemReviewFilters;
	}

	public void setItemReviewFilters(List<ItemReviewFilter> itemReviewFilters) {
		this.itemReviewFilters = itemReviewFilters;
	}


	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(DataEncryptor dataEncryptor) {
		dataEncryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor dataEncryptor, boolean needMasking) {
		dataEncryptor.decrypt(this, needMasking);
	}

	public void decrypt(DataEncryptor dataEncryptor) {
		decrypt(dataEncryptor, ShopUtils.needMasking(getUserId()));
	}
	public String getShLocgovName() {
		return shLocgovName;
	}
	public void setShLocgovName(String shLocgovName) {
		this.shLocgovName = shLocgovName;
	}
	public long getRownum() {
		return rownum;
	}
	public void setRownum(long rownum) {
		this.rownum = rownum;
	}
	public String getAnswerLoginId() {
		return answerLoginId;
	}
	public void setAnswerLoginId(String answerLoginId) {
		this.answerLoginId = answerLoginId;
	}
	public String getAnswerDate() {
		return answerDate;
	}
	public void setAnswerDate(String answerDate) {
		this.answerDate = answerDate;
	}
	public String getAnswerAdminName() {
		return answerAdminName;
	}
	public void setAnswerAdminName(String answerAdminName) {
		this.answerAdminName = answerAdminName;
	}
	public String getSellerCompanyName() {
		return sellerCompanyName;
	}
	public void setSellerCompanyName(String sellerCompanyName) {
		this.sellerCompanyName = sellerCompanyName;
	}
}
