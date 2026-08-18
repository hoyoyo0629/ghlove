package saleson.shop.designateddonation.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.utils.UserUtils;

@Setter
@Getter
@NoArgsConstructor
public class DesignatedPartParam extends SearchParam {

	private static final long serialVersionUID = -4503364443782759655L;

	// 지정기부 부서 아이디
	private long dsgncntrPartId;
	
	// 지정기부 부서 명
	private String dsgncntrPartName;
	
	// 상위 지자체 코드
	private String upperLocgovCode;
	
	// 지자체 코드
	private String locgovCode;
	
	// 사용 유무
	private String useYn;
	
	// 조회 시작일
	private String searchStDt;
	
	// 조회 종료
	private String searchEdDt;
	

	
	public long getUserId() {
		try {
			return UserUtils.getUser().getUserId();
		} catch(NullPointerException e) {
			return 0;
		}
	}
	
}
