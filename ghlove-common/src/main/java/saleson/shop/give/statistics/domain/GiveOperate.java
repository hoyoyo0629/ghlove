package saleson.shop.give.statistics.domain;

import java.util.List;
import java.util.Map;

import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;

@Data
public class GiveOperate {
	
	private String upperLocgovCode;
	private String upperLocgovNm;
	private String locgovCode;
	private String locgovNm;
	private String amt100;
	private String amt200;
	private String amt300;
	private String amt400;
	private String expndtrSum;
	
	private String cntrYear;
	private String cnt100;
	private String cnt200;
	private String cnt300;
	private String cnt400;
	private String expndtrCnt;
	
	public String getAmt(String code) {
		switch (code) {
		case "100": return this.amt100;
		case "200": return this.amt200;
		case "300": return this.amt300;
		case "400": return this.amt400;
		default: return "0";
		}
	}
	
	
}
