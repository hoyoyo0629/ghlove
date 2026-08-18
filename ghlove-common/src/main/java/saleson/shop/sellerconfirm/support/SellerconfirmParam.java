package saleson.shop.sellerconfirm.support;

import java.util.HashMap;

import org.springframework.util.ObjectUtils;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.common.Const;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.SellerUtils;
import saleson.shop.remittance.support.EditAddPaymentRemittance;
import saleson.shop.remittance.support.EditItemRemittance;
import saleson.shop.remittance.support.EditShippingRemittance;
import saleson.shop.remittance.support.FinishingRemittance;

@SuppressWarnings("serial")
public class SellerconfirmParam extends SearchParam {

	private String startDate;
	private String endDate;
	
	private long sellerId;
	private String viewTarget;
	
	private String[] id;
	
	// 정산 데이터 수정 - 상품
	private HashMap<String, EditItemRemittance> editItemRemittanceMap;
	private HashMap<String, EditShippingRemittance> editShippingRemittanceMap;
	private HashMap<String, EditAddPaymentRemittance> editAddPaymentRemittanceMap;
	// 정산 데이터 수정 - 상품
	
	// 정산 마감용
	private HashMap<String, FinishingRemittance> finishingRemittanceMap;
	private double confirmAmount;
	private long remittanceId;
	// 정산 마감용
	
	private String statusCode;
	
	// 지자체 코드
	private String shWdr;
	private String shLocgovCode;

	// 판매자 정산확정 화면용
	private String startDateYear;
	private String startDateMonth;
	private String endDateYear;
	private String endDateMonth;
	
	private String confirm;
	
	private String locgovCode;
	private String locgovNm;
	
	private String searchStatus;
	private long fileSeq;
	
	private String uploadDate;

	public long getRemittanceId() {
		return remittanceId;
	}
	public void setRemittanceId(long remittanceId) {
		this.remittanceId = remittanceId;
	}
	public double getConfirmAmount() {
		return confirmAmount;
	}
	public void setConfirmAmount(double confirmAmount) {
		this.confirmAmount = confirmAmount;
	}
	public HashMap<String, EditShippingRemittance> getEditShippingRemittanceMap() {
		return editShippingRemittanceMap;
	}
	public void setEditShippingRemittanceMap(
			HashMap<String, EditShippingRemittance> editShippingRemittanceMap) {
		this.editShippingRemittanceMap = editShippingRemittanceMap;
	}
	public HashMap<String, FinishingRemittance> getFinishingRemittanceMap() {
		return finishingRemittanceMap;
	}
	public void setFinishingRemittanceMap(
			HashMap<String, FinishingRemittance> finishingRemittanceMap) {
		this.finishingRemittanceMap = finishingRemittanceMap;
	}
	public String getStatusCode() {
		
		if (ObjectUtils.isEmpty(statusCode)) {
			return "1";
		}
		
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	public String getViewTarget() {
		
		if (ObjectUtils.isEmpty(viewTarget)) {
			return "ITEM";
		}
		
		return viewTarget.toUpperCase();
	}
	public String[] getId() {
		return CommonUtils.copy(id);
	}
	public void setId(String[] id) {
		this.id = CommonUtils.copy(id);
	}
	public HashMap<String, EditItemRemittance> getEditItemRemittanceMap() {
		return editItemRemittanceMap;
	}
	public void setEditItemRemittanceMap(
			HashMap<String, EditItemRemittance> editItemRemittanceMap) {
		this.editItemRemittanceMap = editItemRemittanceMap;
	}
	public void setViewTarget(String viewTarget) {
		this.viewTarget = viewTarget;
	}
	public String getStartDate() {
		if (!ObjectUtils.isEmpty(startDateYear) && !ObjectUtils.isEmpty(startDateMonth)) {
			startDateMonth = "0" + startDateMonth;
			startDateMonth = startDateMonth.substring(startDateMonth.length() - 2);
			String date = startDateYear + startDateMonth + "01";
			if (DateUtils.checkDate(date)) {
				return date;
			}
		}
		
		if (ObjectUtils.isEmpty(startDate)) {
			return DateUtils.getToday(Const.DATE_FORMAT);
		}
		
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		if (!ObjectUtils.isEmpty(endDateYear) && !ObjectUtils.isEmpty(endDateMonth)) {
			endDateMonth = "0" + endDateMonth;
			endDateMonth = endDateMonth.substring(endDateMonth.length() - 2);
			String date = endDateYear + endDateMonth + "01";
			if (DateUtils.checkDate(date)) {
				return date;
			}
		}
		
		if (ObjectUtils.isEmpty(endDate)) {
			return DateUtils.getToday(Const.DATE_FORMAT);
		}
		
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public long getDefaultOpmanagerSellerId() {
		return SellerUtils.DEFAULT_OPMANAGER_SELLER_ID;
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public HashMap<String, EditAddPaymentRemittance> getEditAddPaymentRemittanceMap() {
		return editAddPaymentRemittanceMap;
	}
	public void setEditAddPaymentRemittanceMap(
			HashMap<String, EditAddPaymentRemittance> editAddPaymentRemittanceMap) {
		this.editAddPaymentRemittanceMap = editAddPaymentRemittanceMap;
	}
	
	public String getShWdr() {
		return shWdr;
	}

	public void setShWdr(String shWdr) {
		this.shWdr = shWdr;
	}

	public String getShLocgovCode() {
		return shLocgovCode;
	}

	public void setShLocgovCode(String shLocgovCode) {
		this.shLocgovCode = shLocgovCode;
	}
	public String getStartDateYear() {
		return startDateYear;
	}
	public void setStartDateYear(String startDateYear) {
		this.startDateYear = startDateYear;
	}
	public String getStartDateMonth() {
		return startDateMonth;
	}
	public void setStartDateMonth(String startDateMonth) {
		this.startDateMonth = startDateMonth;
	}
	public String getEndDateYear() {
		return endDateYear;
	}
	public void setEndDateYear(String endDateYear) {
		this.endDateYear = endDateYear;
	}
	public String getEndDateMonth() {
		return endDateMonth;
	}
	public void setEndDateMonth(String endDateMonth) {
		this.endDateMonth = endDateMonth;
	}
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getLocgovNm() {
		return locgovNm;
	}
	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}
	public String getConfirm() {
		return confirm;
	}
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public String getSearchStatus() {
		return searchStatus;
	}
	public void setSearchStatus(String searchStatus) {
		this.searchStatus = searchStatus;
	}
	public long getFileSeq() {
		return fileSeq;
	}
	public void setFileSeq(long fileSeq) {
		this.fileSeq = fileSeq;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
}
