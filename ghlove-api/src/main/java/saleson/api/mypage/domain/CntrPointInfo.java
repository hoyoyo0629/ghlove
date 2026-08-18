package saleson.api.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.mypage.domain.CntrPoint;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CntrPointInfo {

	private String locgovCode;
    private String upperLocgovNm;
    private String locgovNm;
	private String cntrSn;
	private String cntrPoint;
	private String cntrUsePoint;
	private String cntrBlcePoint;
	private String detail;

	public CntrPointInfo(CntrPoint cntrPoint2) {
		if(cntrPoint2 !=null) {
			setLocgovCode(cntrPoint2.getLocgovCode());
			setUpperLocgovNm(cntrPoint2.getUpperLocgovNm());
			setLocgovNm(cntrPoint2.getLocgovNm());
			setCntrSn(cntrPoint2.getCntrSn());
			setCntrPoint(cntrPoint2.getCntrPoint());
			setCntrUsePoint(cntrPoint2.getCntrUsePoint());
			setCntrBlcePoint(cntrPoint2.getCntrBlcePoint());
			setDetail(cntrPoint2.getDetail());
		}
	}
}
