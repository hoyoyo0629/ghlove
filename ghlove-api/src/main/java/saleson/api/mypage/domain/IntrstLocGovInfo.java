package saleson.api.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.mypage.domain.IntrstLocGov;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntrstLocGovInfo {

	private String locgovCode;
    private String locgovNm;
    private String upperLocgovCode;
    private String upperLocgovNm;
	private String cntrAmt; // 기부현황내역
	private int pointRate;	// 포인트 지급율

	public IntrstLocGovInfo(IntrstLocGov intrstLocGov) {
		if(intrstLocGov !=null) {
			setLocgovCode(intrstLocGov.getLocgovCode());
			setLocgovNm(intrstLocGov.getLocgovNm());
			setUpperLocgovCode(intrstLocGov.getUpperLocgovCode());
			setUpperLocgovNm(intrstLocGov.getUpperLocgovNm());
			setCntrAmt(intrstLocGov.getCntrAmt());
			setPointRate(intrstLocGov.getPointRate());
		}
	}
}
