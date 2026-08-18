package saleson.shop.remittance.domain;

import saleson.seller.main.domain.Seller;

@SuppressWarnings("serial")
public class RemittanceConfirm extends Seller {

	private String remittanceDate;
	private double itemTotalCommissionBaseAmount;
	private double itemTotalCommissionAmount;
	private double itemTotalSupplyAmount;
	private double itemTotalSellerDiscountAmount;
	private double itemTotalSellerPointAmount;
	private double itemTotalSetDiscountAmount;
	private double addPaymentTotalAmount;
	private double shippingTotalAmount;

	private double cancelItemTotalSupplyAmount;
	private double cancelShippingTotalAmount;


	private String remittanceStatusCode;

	private String confirmDate;
	private String remittanceId;
	private long finishingAmount;
	private long fileCnt;

	private String remittanceYm;
	private String remiDetailCnt;

	private String remittanceTargetMonth;



	public String getRemiDetailCnt() {
		return remiDetailCnt;
	}
	public void setRemiDetailCnt(String remiDetailCnt) {
		this.remiDetailCnt = remiDetailCnt;
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
	public double getCancelItemTotalSupplyAmount() {
		return cancelItemTotalSupplyAmount;
	}
	public void setCancelItemTotalSupplyAmount(double cancelItemTotalSupplyAmount) {
		this.cancelItemTotalSupplyAmount = cancelItemTotalSupplyAmount;
	}
	public double getCancelShippingTotalAmount() {
		return cancelShippingTotalAmount;
	}
	public void setCancelShippingTotalAmount(double cancelShippingTotalAmount) {
		this.cancelShippingTotalAmount = cancelShippingTotalAmount;
	}
	public String getRemittanceDate() {
		return remittanceDate;
	}
	public void setRemittanceDate(String remittanceDate) {
		this.remittanceDate = remittanceDate;
	}
	public double getAddPaymentTotalAmount() {
		return addPaymentTotalAmount;
	}
	public void setAddPaymentTotalAmount(double addPaymentTotalAmount) {
		this.addPaymentTotalAmount = addPaymentTotalAmount;
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
	public String getRemittanceStatusCode() {
		return remittanceStatusCode;
	}
	public void setRemittanceStatusCode(String remittanceStatusCode) {
		this.remittanceStatusCode = remittanceStatusCode;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getRemittanceId() {
		return remittanceId;
	}
	public void setRemittanceId(String remittanceId) {
		this.remittanceId = remittanceId;
	}
	public long getFinishingAmount() {
		return finishingAmount;
	}
	public void setFinishingAmount(long finishingAmount) {
		this.finishingAmount = finishingAmount;
	}
	public long getFileCnt() {
		return fileCnt;
	}
	public void setFileCnt(long fileCnt) {
		this.fileCnt = fileCnt;
	}
	public String getRemittanceYm() {
		return remittanceYm;
	}
	public void setRemittanceYm(String remittanceYm) {
		this.remittanceYm = remittanceYm;
	}
	public String getRemittanceYmDate() {
		try {
			return remittanceYm.substring(0, 4) + "년 " + remittanceYm.substring(4) + "월";
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			return "";
		}
	}
	public String getRemittanceTargetMonth() {
		return remittanceTargetMonth;
	}
	public void setRemittanceTargetMonth(String remittanceTargetMonth) {
		this.remittanceTargetMonth = remittanceTargetMonth;
	}
}
