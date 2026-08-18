package saleson.shop.statistics.locgov.domain;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticsLocgovSearch extends SearchParam {
	
	private String shCntrYear;
	private String shWdr;
	private String shLocgovCode;
	
}
