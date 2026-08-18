package saleson.api.seasonfood.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonFoodParam extends SearchParam{

	private static final long serialVersionUID = 4867385074527168359L;
	private String month;
	private String locgov;
	
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getLocgov() {
		return locgov;
	}
	public void setLocgov(String locgov) {
		this.locgov = locgov;
	}
	
	
}
