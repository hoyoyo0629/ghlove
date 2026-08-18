package saleson.shop.specialityitem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialityFrontDomain {
	private long spclItemMngId;
	private String locgovCode;
	private String locgovNm;
	private String upperLocgovNm;
	private String spclItemInfo;
	private long rownum;
}
