package saleson.shop.tempprocess.domain;

import saleson.shop.order.domain.OrderList;

public class HoldOrderList extends OrderList {

	private String holdConfirmStatus;
	
	private String confirmDateSeller;
	
	private String holdConfirmDateMgr;
	
	private String holdRejectDateMgr;
	
	private long holidConfirmSellerUserId;
	
	private String holidConfirmSellerName;
	
	private long holidConfirmMgrId;
	
	private String holidConfirmMgrName;
	
	private long holidRejectMgrId;
	
	private String holidRejectMgrName;

	private String rcOrderCode;			// 리얼커머스 주문코드
	
	private String matchYn;

	public HoldOrderList() {
		super();
	}

	public String getHoldConfirmStatus() {
		return holdConfirmStatus;
	}

	public void setHoldConfirmStatus(String holdConfirmStatus) {
		this.holdConfirmStatus = holdConfirmStatus;
	}

	public String getConfirmDateSeller() {
		return confirmDateSeller;
	}

	public void setConfirmDateSeller(String confirmDateSeller) {
		this.confirmDateSeller = confirmDateSeller;
	}

	public String getHoldConfirmDateMgr() {
		return holdConfirmDateMgr;
	}

	public void setHoldConfirmDateMgr(String holdConfirmDateMgr) {
		this.holdConfirmDateMgr = holdConfirmDateMgr;
	}

	public String getHoldRejectDateMgr() {
		return holdRejectDateMgr;
	}

	public void setHoldRejectDateMgr(String holdRejectDateMgr) {
		this.holdRejectDateMgr = holdRejectDateMgr;
	}

	public long getHolidConfirmSellerUserId() {
		return holidConfirmSellerUserId;
	}

	public void setHolidConfirmSellerUserId(long holidConfirmSellerUserId) {
		this.holidConfirmSellerUserId = holidConfirmSellerUserId;
	}

	public String getHolidConfirmSellerName() {
		return holidConfirmSellerName;
	}

	public void setHolidConfirmSellerName(String holidConfirmSellerName) {
		this.holidConfirmSellerName = holidConfirmSellerName;
	}

	public long getHolidConfirmMgrId() {
		return holidConfirmMgrId;
	}

	public void setHolidConfirmMgrId(long holidConfirmMgrId) {
		this.holidConfirmMgrId = holidConfirmMgrId;
	}

	public String getHolidConfirmMgrName() {
		return holidConfirmMgrName;
	}

	public void setHolidConfirmMgrName(String holidConfirmMgrName) {
		this.holidConfirmMgrName = holidConfirmMgrName;
	}

	public long getHolidRejectMgrId() {
		return holidRejectMgrId;
	}

	public void setHolidRejectMgrId(long holidRejectMgrId) {
		this.holidRejectMgrId = holidRejectMgrId;
	}

	public String getHolidRejectMgrName() {
		return holidRejectMgrName;
	}

	public void setHolidRejectMgrName(String holidRejectMgrName) {
		this.holidRejectMgrName = holidRejectMgrName;
	}
	
	public String getHoldConfirmStatusName() {
		switch (holdConfirmStatus) {
			case "S":
				return "판매자 승인요청";
			case "M":
				return "관리자 승인완료";
			case "R":
				return "관리자 승인반려";
			default:
				return "미확인 상태";
		}
	}

	public String getRcOrderCode() {
		return rcOrderCode;
	}

	public void setRcOrderCode(String rcOrderCode) {
		this.rcOrderCode = rcOrderCode;
	}

	public String getMatchYn() {
		return matchYn;
	}

	public void setMatchYn(String matchYn) {
		this.matchYn = matchYn;
	}
	
}
