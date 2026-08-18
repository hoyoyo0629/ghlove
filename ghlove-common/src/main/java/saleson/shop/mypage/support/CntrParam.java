package saleson.shop.mypage.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class CntrParam extends SearchParam{

	private String locgovCode;
	private long userId;
	private String searchYear;
	private String serachPeriod;
	private String upperLocgovCode;

}
