package saleson.seller.main.domain;

import java.util.ArrayList;
import java.util.List;

import saleson.common.utils.SellerUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.user.domain.SellerUser;

public class PkiSellerLogin {
	private String loginId;		// 사용자 ID
	private String phoneNumber;	// 휴대폰번호
	private String businessNumber;	// 사업자 번호
	private String mberDn;		// 회원 DN(공동인증서)
	private String mberFinDn;		// 회원 DN(금융인증서)

	private String mberCi;		// 회원 Ci
	private String type;        // 인증서 종류 구분
	private String mode;

	private String code;
	private String errMsg;

	private String[] loginIds;

	public PkiSellerLogin() {
		super();
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}

	public String getMberDn() {
		return mberDn;
	}

	public void setMberDn(String mberDn) {
		this.mberDn = mberDn;
	}

	public String getMberCi() {
		return mberCi;
	}

	public void setMberCi(String mberCi) {
		this.mberCi = mberCi;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	// 다중 로그인 용도..
	public String[] getLoginIds() {
		if (loginIds == null) {
			return null;
		} else {
			int length = loginIds.length;
			String[] data = new String[length];
			for (int i = 0 ; i < length ; i++) {
				data[i] = loginIds[i];
			}
			return data;
		}
	}

	// 다중 로그인 용도..
	public void setLoginIds(List<SellerUser> sellerUsers) {
		if (sellerUsers == null) {
			this.loginIds = null;
		} else {
			int length = sellerUsers.size();
			this.loginIds = new String[length];
			for (int i = 0 ; i < length ; i++) {
				loginIds[i] = sellerUsers.get(i).getLoginId() + "|" + sellerUsers.get(i).getLocgovName();
			}
		}
	}

	public long getUserId() {
		try {
			return Long.valueOf(UserUtils.getUser().getUserId());
		} catch (NullPointerException | NumberFormatException e) {
			return 0;
		}
	}

	public String getMberFinDn() {
		return mberFinDn;
	}

	public void setMberFinDn(String mberFinDn) {
		this.mberFinDn = mberFinDn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
