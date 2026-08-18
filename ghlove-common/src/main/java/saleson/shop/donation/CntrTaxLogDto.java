package saleson.shop.donation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CntrTaxLogDto {

	private String elctrnPayNo;
	private String sttemntPayDe;
	private String CntrAmt;
	private String conbCd;
	private String cntrType;
	private String taxStatusCode;
	private String ntsResCode;
	private String ntsResMssage;
	private String elcrAplCd;
}
