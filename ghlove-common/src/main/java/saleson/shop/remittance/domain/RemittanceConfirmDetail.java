package saleson.shop.remittance.domain;

import saleson.common.security.masking.DefaultDataMasking;
import saleson.common.security.masking.Masking;
import saleson.common.utils.ShopUtils;

public class RemittanceConfirmDetail {
	private String orderCode;
	private String productType;
	private String remittanceStatusCode;
	private String remittanceDate;
	private String itemName;
	private String itemUserCode;
	private String buyerName;
	private String options;
	private int commissionBasePrice;
	private int commissionPrice;
	private float commissionRate;
	private String commissionType;
	private int sellerDiscountPrice;
	private String sellerDiscountDetail;
	private int sellerPoint;
	private int salePrice;
	private int supplyPrice;
	private int quantity;
	
	// 세트상품
	private String setItemFlag = "N";
	private int setDiscountPrice;
	
	private int remainingAmount;		// 결제 금액
	private String confirmDate;			// 구매확정일
	private String payDate;			// 결제일
	private int orderSequence;
	private long sellerId;
	private long remittanceId;
	
	private long remittancePrice;
	private long etcAmt;		// 리얼커머스 가격 보정용..
	
	private long itemSequence;
	
	
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getRemittanceDate() {
		return remittanceDate;
	}
	public void setRemittanceDate(String remittanceDate) {
		this.remittanceDate = remittanceDate;
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
	public int getCommissionPrice() {
		return commissionPrice;
	}
	public void setCommissionPrice(int commissionPrice) {
		this.commissionPrice = commissionPrice;
	}
	public int getSellerDiscountPrice() {
		return sellerDiscountPrice;
	}
	public void setSellerDiscountPrice(int sellerDiscountPrice) {
		this.sellerDiscountPrice = sellerDiscountPrice;
	}
	public int getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(int salePrice) {
		this.salePrice = salePrice;
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
	public String getSellerDiscountDetail() {
		return sellerDiscountDetail;
	}
	public void setSellerDiscountDetail(String sellerDiscountDetail) {
		this.sellerDiscountDetail = sellerDiscountDetail;
	}
	public int getSellerPoint() {
		return sellerPoint;
	}
	public void setSellerPoint(int sellerPoint) {
		this.sellerPoint = sellerPoint;
	}
	public String getRemittanceStatusCode() {
		return remittanceStatusCode;
	}
	public void setRemittanceStatusCode(String remittanceStatusCode) {
		this.remittanceStatusCode = remittanceStatusCode;
	}
	public int getCommissionBasePrice() {
		return commissionBasePrice;
	}
	public void setCommissionBasePrice(int commissionBasePrice) {
		this.commissionBasePrice = commissionBasePrice;
	}
	public float getCommissionRate() {
		return commissionRate;
	}
	public void setCommissionRate(float commissionRate) {
		this.commissionRate = commissionRate;
	}
//	public double getRemittancePrice() {
//		return (supplyPrice - sellerDiscountPrice - sellerPoint - setDiscountPrice);
//	}

	public String getCommissionType() {
		return commissionType;
	}

	public void setCommissionType(String commissionType) {
		this.commissionType = commissionType;
	}

	public int getSetDiscountPrice() {
		return setDiscountPrice;
	}

	public void setSetDiscountPrice(int setDiscountPrice) {
		this.setDiscountPrice = setDiscountPrice;
	}

	public String getSetItemFlag() {
		return setItemFlag;
	}

	public void setSetItemFlag(String setItemFlag) {
		this.setItemFlag = setItemFlag;
	}
	public String getItemUserCode() {
		return itemUserCode;
	}
	public void setItemUserCode(String itemUserCode) {
		this.itemUserCode = itemUserCode;
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
	public int getRemainingAmount() {
		return remainingAmount;
	}
	public void setRemainingAmount(int remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public int getOrderSequence() {
		return orderSequence;
	}
	public void setOrderSequence(int orderSequence) {
		this.orderSequence = orderSequence;
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public long getRemittanceId() {
		return remittanceId;
	}
	public void setRemittanceId(long remittanceId) {
		this.remittanceId = remittanceId;
	}
	public void setRemittancePrice(long remittancePrice) {
		this.remittancePrice = remittancePrice;
	}
	public long getRemittancePrice() {
		return remittancePrice;
	}
	public long getEtcAmt() {
		return etcAmt;
	}
	public void setEtcAmt(long etcAmt) {
		this.etcAmt = etcAmt;
	}
	public long getItemSequence() {
		return itemSequence;
	}
	public void setItemSequence(long itemSequence) {
		this.itemSequence = itemSequence;
	}
	
}
