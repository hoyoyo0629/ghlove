package saleson.api.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.user.domain.LocGovInfo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntrstLocGovInfo {

	private String locgovCode;
    private String locgovNm;
    private String upperLocgovNm;

	public IntrstLocGovInfo(LocGovInfo locGov) {
		if(locGov !=null) {
			setLocgovCode(locGov.getLocgovCode());
			setLocgovNm(locGov.getLocgovNm());
			setUpperLocgovNm(locGov.getUpperLocgovNm());
		}
	}
}
