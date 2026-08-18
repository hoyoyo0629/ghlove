package saleson.shop.log.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@ToString
public class RelayLogParam extends SearchParam{
	
	private long relayLogId;							//연계로그아이디
	private long userId;								//사용자 아이디
	private String loginId;							//로그인 아이디
	private String userName;						//사용자 이름
	private String relayType;						//연계타입
	private String relayTypeName;				//연계타입명
	private String cntrLocgovCode;				//기부지자체코드
	private String cntrLocgovCodeName;		//기부지자체명
	private String cntrSn;							//기부연계키
	private String elctrnPayNo;					//전자납부번호
	private String relayResultCode;				//연계결과코드	 {100:성공, 999:실패} 
	private String frstRegistPnttm;				//등록일시
	
	/* 검색조건 */
	private String srchRelayType;					// 연계구분
	private String srchRelayResultCode;			// 연계결과
	private String srchStartLogDate;				// 등록일
	private String srchEndLogDate;				// 등록일
	private String srchType;						//검색조건
	private String srchTxt;							//검색어
	
	private Integer itemsPerPageTemp;			// 임시 목록수
}
