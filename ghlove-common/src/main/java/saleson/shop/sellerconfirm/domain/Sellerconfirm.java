package saleson.shop.sellerconfirm.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import saleson.common.utils.UserUtils;
import saleson.seller.main.domain.Seller;
import saleson.shop.remittance.support.RemittanceParam;

@SuppressWarnings("serial")
public class Sellerconfirm extends Seller {
	private static final Logger log = LoggerFactory.getLogger(Sellerconfirm.class);

	public Sellerconfirm() {}
	public Sellerconfirm(Seller seller, RemittanceParam param, long id) {

		setRemittanceId(id);

		setSellerId(seller.getSellerId());
		setBankName(seller.getBankName());
		setBankInName(seller.getBankInName());
		setBankAccountNumber(seller.getBankAccountNumber());

		setFinishingAmount((int) param.getConfirmAmount());
		setConfirmDate(param.getStartDate());

		try {
			setFinishingManagerName(UserUtils.getManagerName());
		} catch(NullPointerException e) {
			log.error("ERROR: {}", e.getMessage(), e);
		}
	}

	private long remittanceId;
	private String confirmDate;
	private String finishingDate;

	private int finishingAmount;
	private String finishingManagerName;

	private long salesAmount;
	private String remittanceStatusCode;

	private String confirmDateSeller;
	private String paymentDate;

	public long getRemittanceId() {
		return remittanceId;
	}
	public void setRemittanceId(long remittanceId) {
		this.remittanceId = remittanceId;
	}
	public String getFinishingDate() {
		return finishingDate;
	}
	public void setFinishingDate(String finishingDate) {
		this.finishingDate = finishingDate;
	}

	public int getFinishingAmount() {
		return finishingAmount;
	}
	public void setFinishingAmount(int finishingAmount) {
		this.finishingAmount = finishingAmount;
	}
	public String getFinishingManagerName() {
		return finishingManagerName;
	}
	public void setFinishingManagerName(String finishingManagerName) {
		this.finishingManagerName = finishingManagerName;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public long getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(long salesAmount) {
		this.salesAmount = salesAmount;
	}
	public String getRemittanceStatusCode() {
		return remittanceStatusCode;
	}
	public void setRemittanceStatusCode(String remittanceStatusCode) {
		this.remittanceStatusCode = remittanceStatusCode;
	}
	public String getConfirmDateSeller() {
		return confirmDateSeller;
	}
	public void setConfirmDateSeller(String confirmDateSeller) {
		this.confirmDateSeller = confirmDateSeller;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

}
