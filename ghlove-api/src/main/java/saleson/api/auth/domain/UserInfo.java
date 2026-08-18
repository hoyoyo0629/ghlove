package saleson.api.auth.domain;

import javax.validation.constraints.NotEmpty;

public class UserInfo {

    private long userId;
    @NotEmpty
    private String userName;
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String email;
    private String password;
    private String gender;
    private String telNumber;
    @NotEmpty
    private String phoneNumber;
    private String frontPhoneNumber;
    private String backPhoneNumber;
    private String newPost;
    private String post;
    private String address;
    private String addressDetail;
    private String birthdayYear;
    private String birthdayMonth;
    private String birthdayDay;
    private String birthdayType;
    @NotEmpty
    private String receiveSms;
    @NotEmpty
    private String receiveEmail;

    private String receivePush;
    private String receivePbanc;

    /* 20260120 추가 */
    private String receiveKakao;

	private boolean isSns;
    private boolean isAuth;

    private String requestToken;
    private String authNumber;

    private String leaveReason;
    private String terms;
    private String privacy;
    private String marketing;

    private String[] locGovList;
    private String[] rtnpsntList;

    private String mberCi;
    private String mberDi;
    private String locgovCode;
    private String birthday;
    private String birthdayFull;

    private String userKey;
    private String isOnepass;
	private String leaveCode;		// 탈퇴코드

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /* 20260120 추가 */
    public String getReceiveKakao() {
        return receiveKakao;
    }

    public void setReceiveKakao(String receiveKakao) {
        this.receiveKakao = receiveKakao;
    }


    public String getReceiveSms() {
        return receiveSms;
    }

    public void setReceiveSms(String receiveSms) {
        this.receiveSms = receiveSms;
    }

    public String getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(String receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public String getReceivePbanc() {
		return receivePbanc;
	}

	public void setReceivePbanc(String receivePbanc) {
		this.receivePbanc = receivePbanc;
	}

    public String getReceivePush() {
        return receivePush;
    }

    public void setReceivePush(String receivePush) {
        this.receivePush = receivePush;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getNewPost() {
        return newPost;
    }

    public void setNewPost(String newPost) {
        this.newPost = newPost;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getBirthdayType() {
        return birthdayType;
    }

    public void setBirthdayType(String birthdayType) {
        this.birthdayType = birthdayType;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSns() {
        return isSns;
    }

    public void setSns(boolean sns) {
        isSns = sns;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(String birthdayYear) {
        this.birthdayYear = birthdayYear;
    }

    public String getBirthdayMonth() {
        return birthdayMonth;
    }

    public void setBirthdayMonth(String birthdayMonth) {
        this.birthdayMonth = birthdayMonth;
    }

    public String getBirthdayDay() {
        return birthdayDay;
    }

    public void setBirthdayDay(String birthdayDay) {
        this.birthdayDay = birthdayDay;
    }

    public String getFrontPhoneNumber() {
        return frontPhoneNumber;
    }

    public void setFrontPhoneNumber(String frontPhoneNumber) {
        this.frontPhoneNumber = frontPhoneNumber;
    }

    public String getBackPhoneNumber() {
        return backPhoneNumber;
    }

    public void setBackPhoneNumber(String backPhoneNumber) {
        this.backPhoneNumber = backPhoneNumber;
    }

    public String getAuthNumber() {
        return authNumber;
    }

    public void setAuthNumber(String authNumber) {
        this.authNumber = authNumber;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getMarketing() {
        return marketing;
    }

    public void setMarketing(String marketing) {
        this.marketing = marketing;
    }

	public String[] getLocGovList() {
		if (locGovList == null) {
			return null;
		} else {
	    	int length = locGovList.length;
	    	String[] array = new String[length];
	    	for (int i = 0; i < length; i++) {
				array[i] = locGovList[i];
	    	}
		return array;
		}
	}

	public void setLocGovList(String[] locGovList) {
	    if (locGovList == null) {
	        this.locGovList = null;
	    } else {
	        int length = locGovList.length;
	        this.locGovList = new String[length];
	        for (int i = 0; i < length; i++) {
	            this.locGovList[i] = locGovList[i];
	        }
	    }
	}

	public String[] getRtnpsntList() {
		String[] copyRtnpsntList = new String[this.rtnpsntList.length];
		// private 배열의 값을 복사한 별도의 배열을 리턴
		for (int i=0; i<this.rtnpsntList.length; i++) {
			copyRtnpsntList[i] = this.rtnpsntList[i];
		}
		return copyRtnpsntList;
		//return rtnpsntList;
	}

	public void setRtnpsntList(String[] rtnpsntList) {
	    if (rtnpsntList == null) {
	        this.rtnpsntList = null;
	    } else {
	        int length = rtnpsntList.length;
	        this.rtnpsntList = new String[length];
	        for (int i = 0; i < length; i++) {
	            this.rtnpsntList[i] = rtnpsntList[i];
	        }
	    }
	}

	public String getMberCi() {
		return mberCi;
	}
	public void setMberCi(String mberCi) {
		this.mberCi = mberCi;
	}
	public String getMberDi() {
		return mberDi;
	}
	public void setMberDi(String mberDi) {
		this.mberDi = mberDi;
	}

	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getBirthdayFull() {
		return birthdayFull;
	}

	public void setBirthdayFull(String birthdayFull) {
		this.birthdayFull = birthdayFull;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getIsOnepass() {
		return isOnepass;
	}

	public void setIsOnepass(String isOnepass) {
		this.isOnepass = isOnepass;
	}

	public String getLeaveCode() {
		return leaveCode;
	}

	public void setLeaveCode(String leaveCode) {
		this.leaveCode = leaveCode;
	}
}
