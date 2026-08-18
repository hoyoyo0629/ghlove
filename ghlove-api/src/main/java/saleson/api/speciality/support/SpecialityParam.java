package saleson.api.speciality.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialityParam extends SearchParam{

	private static final long serialVersionUID = 4867385074527168359L;
	private String locgov;
	private String spclItemMngId;
	
	
	public String getLocgov() {
		return locgov;
	}
	public void setLocgov(String locgov) {
		this.locgov = locgov;
	}
	public String getSpclItemMngId() {
		return spclItemMngId;
	}
	public void setSpclItemMngId(String spclItemMngId) {
		this.spclItemMngId = spclItemMngId;
	}
	
	
}
