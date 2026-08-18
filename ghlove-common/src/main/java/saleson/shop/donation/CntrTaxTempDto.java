package saleson.shop.donation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CntrTaxTempDto {

	private String cntrSn;
	private String sttemntPayDe;
	private Long userId;
	private String mberCi;
	private String cntrAmt;
	private String elctrnPayNo;
	private String bizNo;
	private String elcrAplCd;
	private String cntrSttusCode;
	private String etc;
	private Long confirmUserId;
	private int logCnt;
}
