package saleson.shop.give.giveoperation.domain;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiveOperationSearchParam extends SearchParam {
	
	private String shCntrYear;
	private String shWdr;
	private String shLocgovCode;
	
	private String searchType;
	private String searchTxt;
	
	private String shCntrDeStart;
	private String shCntrDeEnd;
	
	private Long registSn;
	
}
