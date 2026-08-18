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
public class CntrPointParam extends SearchParam{

	private long userId;
	private String locgovCode;
    
}
