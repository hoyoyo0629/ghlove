package saleson.shop.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MypageMainInfo {

	private String cntrPoint; // 기부현황내역
	private String cntrAmt; // 기부 포인트
	private String loginId;     // 로그인 ID
    private String userId;
    
    
    
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getLoginId() {
	     return loginId;
	}

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    
	public String getCntrPoint() {
		return cntrPoint;
	}

	public void setCntrPoint(String cntrPoint) {
		this.cntrPoint = cntrPoint;
	}

	public String getCntrAmt() {
		return cntrAmt;
	}

	public void setCntrAmt(String cntrAmt) {
		this.cntrAmt = cntrAmt;
	}
}
