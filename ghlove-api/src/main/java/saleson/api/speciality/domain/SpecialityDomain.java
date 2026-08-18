package saleson.api.speciality.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityDomain{
	private long spclItemMngId;
	private String locgovCode;
	private String locgovNm;
	private String upperLocgovNm;
	private String spclItemInfo;
}
