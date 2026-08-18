package saleson.shop.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.mypage.domain.IntrstLocGov;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CntrPointDetail {

	//기부 포인트 상세 정보 추가
	private String cntrDe;
	private String cntrAmt;
	private String cntrPoint;
	private String cntrUsePoint;
	private String cntrBlcePoint;
	private String orderCode;
	private String urlAdres;
	
	private String upperLocgovNm;
	private String locgovNm;  
	
	public CntrPointDetail(CntrPoint cntrPoint) {
		if(cntrPoint!=null) {
			setCntrDe(cntrPoint.getCntrDe());
			setCntrAmt(cntrPoint.getCntrAmt());
			setCntrPoint(cntrPoint.getCntrPoint());
			setCntrUsePoint(cntrPoint.getCntrUsePoint());
			setCntrBlcePoint(cntrPoint.getCntrBlcePoint());
			setOrderCode(cntrPoint.getOrderCode());
			setUrlAdres(cntrPoint.getUrlAdres());
		}
	}
	
}
