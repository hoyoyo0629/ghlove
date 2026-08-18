package saleson.shop.give.statistics.domain;

import java.util.List;

import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.web.domain.SearchParam;

//import lombok.Data;

//@Data			// 2차 운영서버 에러 발생하여 수정
public class GiveOperateSearch extends SearchParam {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6513958658011318685L;
	
	private String shCntrYear;
	private String shWdr;
	private String shLocgovCode;
	
	private String shCntrDate;
	
	private List<Code> codeList;

	public String getShCntrYear() {
		return shCntrYear;
	}

	public void setShCntrYear(String shCntrYear) {
		this.shCntrYear = shCntrYear;
	}

	public String getShWdr() {
		return shWdr;
	}

	public void setShWdr(String shWdr) {
		this.shWdr = shWdr;
	}

	public String getShLocgovCode() {
		return shLocgovCode;
	}

	public void setShLocgovCode(String shLocgovCode) {
		this.shLocgovCode = shLocgovCode;
	}

	public String getShCntrDate() {
		return shCntrDate;
	}

	public void setShCntrDate(String shCntrDate) {
		this.shCntrDate = shCntrDate;
	}

	public List<Code> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<Code> codeList) {
		this.codeList = codeList;
	}
	
}
