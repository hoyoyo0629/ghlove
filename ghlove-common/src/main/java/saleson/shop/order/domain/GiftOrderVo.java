package saleson.shop.order.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class GiftOrderVo{

	Buyer buyer;

    Receiver receiver;

    String[] orderItems;

    String mbrCi;

    long userId;

    String loginId;

    String linkMngKey;

    String updatedAdminUserName;

	public String getUpdatedAdminUserName() {
		return updatedAdminUserName;
	}

	public void setUpdatedAdminUserName(String updatedAdminUserName) {
		this.updatedAdminUserName = updatedAdminUserName;
	}

	public String getLinkMngKey() {
		return linkMngKey;
	}

	public void setLinkMngKey(String linkMngKey) {
		this.linkMngKey = linkMngKey;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Buyer getBuyer() {
		return buyer;
	}

	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	public Receiver getReceiver() {
		return receiver;
	}

	public void setReceiver(Receiver receiver) {
		this.receiver = receiver;
	}

	public String[] getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(String[] orderItems) {
		this.orderItems = orderItems;
	}

	public String getMbrCi() {
		return mbrCi;
	}

	public void setMbrCi(String mbrCi) {
		this.mbrCi = mbrCi;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}


}
