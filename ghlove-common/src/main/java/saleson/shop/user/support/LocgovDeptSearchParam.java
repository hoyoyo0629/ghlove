package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@SuppressWarnings("serial")
public class LocgovDeptSearchParam extends SearchParam {
	
	private String locgovCode;			// 지자체 코드
	
}
