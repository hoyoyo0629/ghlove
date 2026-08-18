package saleson.shop.give.statistics.domain;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GiveStatisticsSearch extends SearchParam {
	
	private static final long serialVersionUID = -4012248661467027656L;
	
	private String shCntrYear;
	private String shWdr;
	private String shLocgovCode;
	private String itemsOrder;
	
	private String shCntrDate;
	private String foreignerFlag;
	
}
