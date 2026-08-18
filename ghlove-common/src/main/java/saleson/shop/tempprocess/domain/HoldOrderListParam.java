package saleson.shop.tempprocess.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import saleson.common.utils.UserUtils;
import saleson.shop.order.support.OrderParam;

public class HoldOrderListParam extends OrderParam {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1932004584435039574L;

	private String holdConfirmStatus;
	
	private String[] holdStatus;
	
	private String inputStatus;
	
	private String filler5;

	public HoldOrderListParam() {
		super();
	}

	public String getHoldConfirmStatus() {
		return holdConfirmStatus;
	}

	public void setHoldConfirmStatus(String holdConfirmStatus) {
		this.holdConfirmStatus = holdConfirmStatus;
	}

	public String getHoldConfirmDate() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}

	public long getHolidConfirmUserId() {
		return UserUtils.getUser().getUserId();
	}

	public String[] getHoldStatus() {
		if (holdStatus == null || holdStatus.length == 0) {
//			return new String[]{"N", "S", "M", "R"};
			return new String[]{"N", "S", "R"};
		} else {
			int length = holdStatus.length;
			String[] value = new String[length];
			for (int i = 0 ; i < length ; i++) {
				value[i] = holdStatus[i];
			}
			return value;	
		}
	}

	public void setHoldStatus(String[] holdStatus) {
		if (holdStatus == null) {
			this.holdStatus = null;
		} else {
			int length = holdStatus.length;
			this.holdStatus = new String[length];
			for (int i = 0 ; i < length ; i++) {
				this.holdStatus[i] = holdStatus[i];
			}
		}
	}

	public String getInputStatus() {
		return inputStatus;
	}

	public void setInputStatus(String inputStatus) {
		this.inputStatus = inputStatus;
	}

	public String getFiller5() {
		return filler5;
	}

	public void setFiller5(String filler5) {
		this.filler5 = filler5;
	}

}
