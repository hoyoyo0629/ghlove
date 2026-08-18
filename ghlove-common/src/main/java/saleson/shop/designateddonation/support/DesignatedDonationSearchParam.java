package saleson.shop.designateddonation.support;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.common.utils.UserUtils;

public class DesignatedDonationSearchParam extends SearchParam {

	private static final long serialVersionUID = 4097024713358173537L;

	// 상위 지자체 코드
	private String upperLocgovCode;
	
	// 지자체 코드
	private String locgovCode;
	
	// 프로젝트 시작일
	private String prjStDt;
	
	// 프로젝트 종료일
	private String prjEdDt;
	
	// 제목
	private String prjSubject;
	
	// 사업구분코드
	private String bsnsType;
	
	// 프로젝트 상태코드
	private String prjStatus;
	
	// 프로젝트 공개여부
	private String displayFlag;
	
	private long prjId;
	// 호출화면
	private String display;
	
	// 검색어
	private String searchKeyword;
	
	// 부서아이디
	private long dsgncntrPartId;
	
	// 기부 체크 조회 여부
	private boolean checkDonation = false;
	
	public long getPrjId() {
		return prjId;
	}

	public void setPrjId(long prjId) {
		this.prjId = prjId;
	}

	
	
	public DesignatedDonationSearchParam() {
		super();
	}

	public String getUpperLocgovCode() {
		return upperLocgovCode;
	}

	public void setUpperLocgovCode(String upperLocgovCode) {
		this.upperLocgovCode = upperLocgovCode;
	}

	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

	public String getPrjStDt() {
		return prjStDt;
	}

	public void setPrjStDt(String prjStDt) {
		this.prjStDt = prjStDt;
	}

	public String getPrjEdDt() {
		return prjEdDt;
	}

	public void setPrjEdDt(String prjEdDt) {
		this.prjEdDt = prjEdDt;
	}

	public String getPrjSubject() {
		return prjSubject;
	}

	public void setPrjSubject(String prjSubject) {
		this.prjSubject = prjSubject;
	}

	public String getBsnsType() {
		return bsnsType;
	}

	public void setBsnsType(String bsnsType) {
		this.bsnsType = bsnsType;
	}

	public String getPrjStatus() {
		if (StringUtils.isEmpty(prjStatus)) {
			return "";
		}
		return prjStatus;
	}

	public void setPrjStatus(String prjStatus) {
		this.prjStatus = prjStatus;
	}

	public String getDisplayFlag() {
		if (StringUtils.isEmpty(displayFlag)) {
			return "";
		}
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

//	public boolean isAscending() {
//		return ascending;
//	}
//
//	public void setAscending(boolean ascending) {
//		this.ascending = ascending;
//	}
//
//	public boolean getAscending() {
//		return ascending;
//	}
	
	public long getUserId() {
		try {
			return UserUtils.getUser().getUserId();
		} catch (OpRuntimeException | NullPointerException e) {
			return 0;
		}
	}

	public long getDsgncntrPartId() {
		return dsgncntrPartId;
	}

	public void setDsgncntrPartId(long dsgncntrPartId) {
		this.dsgncntrPartId = dsgncntrPartId;
	}
	
	// 아이템 코드 리스트(더보기)
	private String itemPrjIdList;
	
	public String[] getItemPrjIdListArr() {
		if(itemPrjIdList == null) {
			return null;
		} else {
			String[] itemList = itemPrjIdList.split(",");
			if(itemList.length == 0) {
				return null;
			} else {
				return itemList;
			}
		}
	}

	public boolean isCheckDonation() {
		return checkDonation;
	}

	public boolean getCheckDonation() {
		return checkDonation;
	}

	public void setCheckDonation(boolean checkDonation) {
		this.checkDonation = checkDonation;
	}

	/**
	 * 지정기부 권한 여부
	 */
	public boolean getDsgncntrManagerCheck() {
		return UserUtils.hasDsgncntrManagerRole();
	}
	
}
