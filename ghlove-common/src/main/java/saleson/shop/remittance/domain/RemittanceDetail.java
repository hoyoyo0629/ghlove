package saleson.shop.remittance.domain;

import saleson.common.security.masking.DefaultDataMasking;
import saleson.common.security.masking.Masking;
import saleson.common.utils.ShopUtils;

public class RemittanceDetail {
	
	private long remittanceDetailId;
	private long remittanceId;
	private String orderCode;
	private String itemType;
	private String itemUserCode;
	private String itemName;
	private String options;
	
	private int salePrice;
	private int sellerDiscountPrice;
	private String sellerDiscountDetail;
	private int sellerPoint;

	private String setItemFlag = "N";
	private int setDiscountPrice;

	private int supplyPrice;
	private int quantity;
	private int commissionBasePrice;
	
	private float commissionRate;
	private String commissionType;
	private int commissionPrice;
	private int remittancePrice;
	
	private String createdDate;
	private String buyerName;
	private String payDate;
	private String confirmDate;
	
	private int etcAmt;
	public long getRemittanceDetailId() {
		return remittanceDetailId;
	}
	public void setRemittanceDetailId(long remittanceDetailId) {
		this.remittanceDetailId = remittanceDetailId;
	}
	public long getRemittanceId() {
		return remittanceId;
	}
	public void setRemittanceId(long remittanceId) {
		this.remittanceId = remittanceId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemUserCode() {
		return itemUserCode;
	}
	public void setItemUserCode(String itemUserCode) {
		this.itemUserCode = itemUserCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public int getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
	}
	public int getSellerDiscountPrice() {
		return sellerDiscountPrice;
	}
	public void setSellerDiscountPrice(int sellerDiscountPrice) {
		this.sellerDiscountPrice = sellerDiscountPrice;
	}
	public String getSellerDiscountDetail() {
		return sellerDiscountDetail;
	}
	public void setSellerDiscountDetail(String sellerDiscountDetail) {
		this.sellerDiscountDetail = sellerDiscountDetail;
	}
	public int getSupplyPrice() {
		return supplyPrice;
	}
	public void setSupplyPrice(int supplyPrice) {
		this.supplyPrice = supplyPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getCommissionRate() {
		return commissionRate;
	}
	public void setCommissionRate(float commissionRate) {
		this.commissionRate = commissionRate;
	}
	public int getCommissionPrice() {
		return commissionPrice;
	}
	public void setCommissionPrice(int commissionPrice) {
		this.commissionPrice = commissionPrice;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public int getSellerPoint() {
		return sellerPoint;
	}
	public void setSellerPoint(int sellerPoint) {
		this.sellerPoint = sellerPoint;
	}
	public int getRemittancePrice() {
		return remittancePrice;
	}
	public void setRemittancePrice(int remittancePrice) {
		this.remittancePrice = remittancePrice;
	}
	public int getCommissionBasePrice() {
		return commissionBasePrice;
	}
	public void setCommissionBasePrice(int commissionBasePrice) {
		this.commissionBasePrice = commissionBasePrice;
	}

	public String getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}

	public String getSetItemFlag() {
		return setItemFlag;
	}

	public void setSetItemFlag(String setItemFlag) {
		this.setItemFlag = setItemFlag;
	}

	public int getSetDiscountPrice() {
		return setDiscountPrice;
	}

	public void setSetDiscountPrice(int setDiscountPrice) {
		this.setDiscountPrice = setDiscountPrice;
	}
	public String getBuyerName() {
		if (ShopUtils.needMasking()) {
			DefaultDataMasking masking = new DefaultDataMasking();
			return masking.mask(buyerName, Masking.NAME);
		} else {
			return buyerName;
		}
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public int getEtcAmt() {
		return etcAmt;
	}
	public void setEtcAmt(int etcAmt) {
		this.etcAmt = etcAmt;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
}
