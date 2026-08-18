package saleson.shop.email.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Setter
@Getter
public class EmailDetailParam extends SearchParam {
	
	private long emailId;
}

