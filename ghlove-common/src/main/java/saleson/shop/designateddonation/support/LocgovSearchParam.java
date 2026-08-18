package saleson.shop.designateddonation.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import saleson.common.utils.UserUtils;

@Getter
@Setter
public class LocgovSearchParam extends SearchParam {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2196964508131993984L;
	
	private String shWdr;			// 상위 지자체 코드
	private String shLocgovCode; 	// 지자체 코드
	private String locgovNm;		// 지자체 명
	private String selYear;			// 조회년도
	private String prjStatus;		// 상태(0:전체, 1.승인대기(삭제됨), 2:진행, 9:종료)
	
	private String bsnsType;		// 사업구분코드
	private String frDt;			// 조회 시작일
	private String toDt;			// 조회 종료일

	private String displayFlag;		// 공개여부
	private String searchKeyword;	// 검색어
	
	private long dsgncntrPartId;	// 부서아이디

	/**
	 * 지정기부 권한 여부
	 */
	public boolean getDsgncntrManagerCheck() {
		return UserUtils.hasDsgncntrManagerRole();
	}

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

}
