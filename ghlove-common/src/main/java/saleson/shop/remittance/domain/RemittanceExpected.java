package saleson.shop.remittance.domain;

import saleson.seller.main.domain.Seller;

@SuppressWarnings("serial")
public class RemittanceExpected extends Seller {
	private double itemTotalCommissionBaseAmount;
	private double itemTotalCommissionAmount;
	private double itemTotalSupplyAmount;
	private double itemTotalSellerDiscountAmount;
	private double itemTotalSellerPointAmount;
	private double itemTotalSetDiscountAmount;
	private double shippingTotalAmount;
	private double addPaymentTotalAmount;
	private String remittanceExpectedDate;
	private String remittanceDate;

	private String remittanceTargetMonth;
	private String orCnt;


	public String getOrCnt() {
		return orCnt;
	}
	public void setOrCnt(String orCnt) {
		this.orCnt = orCnt;
	}
	public double getAddPaymentTotalAmount() {
		return addPaymentTotalAmount;
	}
	public void setAddPaymentTotalAmount(double addPaymentTotalAmount) {
		this.addPaymentTotalAmount = addPaymentTotalAmount;
	}
	public double getItemTotalCommissionAmount() {
		return itemTotalCommissionAmount;
	}
	public void setItemTotalCommissionAmount(double itemTotalCommissionAmount) {
		this.itemTotalCommissionAmount = itemTotalCommissionAmount;
	}
	public double getItemTotalSupplyAmount() {
		return itemTotalSupplyAmount;
	}
	public void setItemTotalSupplyAmount(double itemTotalSupplyAmount) {
		this.itemTotalSupplyAmount = itemTotalSupplyAmount;
	}
	public double getItemTotalSellerDiscountAmount() {
		return itemTotalSellerDiscountAmount;
	}
	public void setItemTotalSellerDiscountAmount(
			double itemTotalSellerDiscountAmount) {
		this.itemTotalSellerDiscountAmount = itemTotalSellerDiscountAmount;
	}
	public double getShippingTotalAmount() {
		return shippingTotalAmount;
	}
	public void setShippingTotalAmount(double shippingTotalAmount) {
		this.shippingTotalAmount = shippingTotalAmount;
	}
	public double getItemTotalCommissionBaseAmount() {
		return itemTotalCommissionBaseAmount;
	}
	public void setItemTotalCommissionBaseAmount(
			double itemTotalCommissionBaseAmount) {
		this.itemTotalCommissionBaseAmount = itemTotalCommissionBaseAmount;
	}
	public double getItemTotalSellerPointAmount() {
		return itemTotalSellerPointAmount;
	}
	public double getItemTotalSetDiscountAmount() {
		return itemTotalSetDiscountAmount;
	}

	public void setItemTotalSetDiscountAmount(double itemTotalSetDiscountAmount) {
		this.itemTotalSetDiscountAmount = itemTotalSetDiscountAmount;
	}
	public void setItemTotalSellerPointAmount(double itemTotalSellerPointAmount) {
		this.itemTotalSellerPointAmount = itemTotalSellerPointAmount;
	}
	public double getItemRemittanceAmount() {
		return itemTotalSupplyAmount - itemTotalSellerDiscountAmount - itemTotalSellerPointAmount - itemTotalSetDiscountAmount;
	}
	public String getRemittanceExpectedDate() {
		return remittanceExpectedDate;
	}
	public void setRemittanceExpectedDate(String remittanceExpectedDate) {
		this.remittanceExpectedDate = remittanceExpectedDate;
	}
	public String getRemittanceDate() {
		return remittanceDate;
	}
	public void setRemittanceDate(String remittanceDate) {
		this.remittanceDate = remittanceDate;
	}
	public String getRemittanceTargetMonth() {
		return remittanceTargetMonth;
	}
	public void setRemittanceTargetMonth(String remittanceTargetMonth) {
		this.remittanceTargetMonth = remittanceTargetMonth;
	}
	@Override
	public String toString() {
		return "RemittanceExpected [itemTotalCommissionBaseAmount=" + itemTotalCommissionBaseAmount
				+ ", itemTotalCommissionAmount=" + itemTotalCommissionAmount + ", itemTotalSupplyAmount="
				+ itemTotalSupplyAmount + ", itemTotalSellerDiscountAmount=" + itemTotalSellerDiscountAmount
				+ ", itemTotalSellerPointAmount=" + itemTotalSellerPointAmount + ", itemTotalSetDiscountAmount="
				+ itemTotalSetDiscountAmount + ", shippingTotalAmount=" + shippingTotalAmount
				+ ", addPaymentTotalAmount=" + addPaymentTotalAmount + ", remittanceExpectedDate="
				+ remittanceExpectedDate + ", remittanceDate=" + remittanceDate + ", remittanceTargetMonth="
				+ remittanceTargetMonth + ", orCnt=" + orCnt + "]";
	}



}
