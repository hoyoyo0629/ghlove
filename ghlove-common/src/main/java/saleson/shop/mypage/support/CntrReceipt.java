package saleson.shop.mypage.support;

import java.util.List;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class CntrReceipt extends SearchParam{

	/*
	 * 조회조건을 위한 파라미터
	 * userId 		: 유저의 ID (long 타입으로 login_id 와는 다름)
	 * locgovCode 	: 지자체 코드(시.군.구)
	 * upperLocgovCode 	: 상위 지자체 코드(시.도)
	 * searchStartDate	: 조회 시작 날짜
	 * searchEndDate	: 조회 종료 날짜
	 * cntrSnList		: 기부id리스트
	 * */
	private long userId;
	private String locgovCode;
	private String upperLocgovCode;
	private String searchStartDate;
	private String searchEndDate;
	private List<String> cntrSnList;

}
