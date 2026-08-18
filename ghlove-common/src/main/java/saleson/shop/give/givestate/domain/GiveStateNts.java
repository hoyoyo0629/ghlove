package saleson.shop.give.givestate.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class GiveStateNts {
	private String elctrnPayNo;
	private String sttemntPayDe;
	private String cntrAmt;
	private String elcrAplCd;
	private String ntsResCode;
	private String ntsResMssage;
	private String conbCd;
	private String cntrType;
	private String frstRegistPnttm;
	private String lastUpdtPnttm;
}
