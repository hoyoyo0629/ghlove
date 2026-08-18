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
public class IntrstLocGovParam extends SearchParam{

	private String locgovCode;
    private String locgovNm;
    private String upperLocgovNm;
	private String cntrAmt; //  기부현황내역
	private long userId;
    
}
