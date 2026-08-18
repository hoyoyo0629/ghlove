package saleson.shop.donation;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NextSunapRequestLogDto extends SearchParam{

	private static final long serialVersionUID = 1L;
	private String rcvmtYmd;    //수납일자
	private String epayNo;		//전자납부번호 
	private String srchStartLogDate;	// 수납일 시작
	private String srchEndLogDate;		// 수납일 종료
	private Integer itemsPerPageTemp;	// 임시 목록수
	
	
	
	
}

