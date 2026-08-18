package saleson.shop.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntrstLocGov{

	private String locgovCode;
    private String locgovNm;
    private String upperLocgovCode;
    private String upperLocgovNm;
	private String cntrAmt; // 기부현황내역
	private long userId;
	private int pointRate;	// 포인트 지급율
}
