package saleson.shop.designateddonation.domain;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import saleson.common.utils.UserUtils;

@Data
@EqualsAndHashCode(callSuper = false)
public class DsgncntrConfirmLogParam extends SearchParam {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6612960490569946281L;
	/**
	 * 지정기부 아이디
	 */
	private long prjId;

	/**
	 * 로그인 사용자 아이디
	 */
	public long getUserId() {
		try {
			return UserUtils.getUser().getUserId();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	/**
	 * 시스템 권한 여부
	 */
	public boolean getMasterManagerCheck() {
		return UserUtils.hasMasterManagerRole();
	}

	/**
	 * 지정기부 권한 여부
	 */
	public boolean getDsgncntrManagerCheck() {
		return UserUtils.hasDsgncntrManagerRole();
	}
}
