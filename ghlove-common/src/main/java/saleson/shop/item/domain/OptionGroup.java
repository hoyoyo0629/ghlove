package saleson.shop.item.domain;

import java.util.ArrayList;
import java.util.List;

public class OptionGroup {
	private String title;
	private List<String> options = new ArrayList<>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
}
