package saleson.shop.log.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@ToString
public class GifSeoulParam extends SearchParam{

	private String ifNo;
	private String enapbuNo;
	private String siguCd;
	private String semokCd;
	private String taxYm;
	private String taxGubun;
	private String sidoCd;
	private String napNm;
	private String napGubun;
	private String taxAmt;
	private String sise;
	private String resideStatus;
	private String mulGubun;
	private String mulNm;
	private String bookNo;
	private String sysGubun;
	private String errorCd;
	private String errorMsg;
	private String insertKey;
	private String insertAk;
	private String resultCnt;
	private String ifStDt;
	private String ifEdDt;
	private String napId;

	private String epayNo;
	private String comReqMeche;
	private String comReqDt;
	private String comReqTm;
	private String comPayMsgNo;
	private String accessKey;
	private String orgC;
	private String sunapYn;
	private String sunapAmt;
	private String sunapDt;
	private String rstCd;
	private String rstMsg;

	private String srchStartLogDate;				// 등록일
	private String srchEndLogDate;				// 등록일
	private String srchErrorCd;
	private String srchTxt;							//검색어
	private String srchRstCd;

	private Integer itemsPerPageTemp;			// 임시 목록수
}
