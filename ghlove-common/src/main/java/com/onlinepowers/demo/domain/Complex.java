package com.onlinepowers.demo.domain;

public class Complex {

	private String id;
	private String userName;
	private String createdDate;
	
	private String[] itemId;
	private String[] itemName;
	
	private Simple rankSeo;
	private Simple reviewSeo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Simple getRankSeo() {
		return rankSeo;
	}
	public void setRankSeo(Simple rankSeo) {
		this.rankSeo = rankSeo;
	}
	public Simple getReviewSeo() {
		return reviewSeo;
	}
	public void setReviewSeo(Simple reviewSeo) {
		this.reviewSeo = reviewSeo;
	}
	
	public String[] getItemId() {
		if (itemId == null) {
			return null;
		} else {
			int length = itemId.length;
			String[] array = new String[length];
			for(int i = 0 ; i < length ; i++) {
				array[i] = itemId[i];
			}
			return array;
		}
	}
	public void setItemId(String[] itemId) {
		if (itemId == null) {
			this.itemId = null;
		} else {
			int length = itemId.length;
			this.itemId = new String[length];
			for(int i = 0 ; i < length ; i++) {
				this.itemId[i] = itemId[i];
			}
		}
	}
	public String[] getItemName() {
		if (itemName == null) {
			return null;
		} else {
			int length = itemName.length;
			String[] array = new String[length];
			for(int i = 0 ; i < length ; i++) {
				array[i] = itemName[i];
			}
			return array;
		}
	}
	public void setItemName(String[] itemName) {
		if (itemName == null) {
			this.itemName = null;
		} else {
			int length = itemName.length;
			this.itemName = new String[length];
			for(int i = 0 ; i < length ; i++) {
				this.itemName[i] = itemName[i];
			}
		}
	}
	
}
