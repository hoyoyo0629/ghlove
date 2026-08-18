package saleson.shop.log.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@ToString
public class GifStndParam extends SearchParam{

	private String ifNo;
	private String elctPayNo;
	private String comMsgLen;
	private String comIfId;
	private String com_source;
	private String comTarget;
	private String comMsgKey;
	private String comTypeCd;
	private String comRstCd;
	private String sysCd;
	private String deptCd;
	private String fisyy;
	private String fisSp;
	private String ptclCd;
	private String impsDt;
	private String initPrcpTaxAmt;
	private String lstPrcpTaxAmt;
	private String initDueDt;
	private String lstDueDt;
	private String afDueDt;
	private String afDueAmt;
	private String impsSp;
	private String decsSp;
	private String txprSp;
	private String txprTelNo;
	private String txprMphnNo;
	private String txprEml;
	private String newAddrYn;
	private String txprRoadCd;
	private String txprBdFlrSp;
	private String txprBdPrcpNo;
	private String txprBdSubNo;
	private String statCd;
	private String spclFisBizCd;
	private String txprZipNo;
	private String txprLglvilCd;
	private String txprTwnvilCd;
	private String txprMt;
	private String txprAddrNo;
	private String txprAddrHo;
	private String txprSpclAddr;
	private String txprSpclAddrDong;
	private String txprSpclAddrHo;
	private String txprAddrTong;
	private String txprAddrBan;
	private String txprBdMngNo;
	private String objNm;
	private String taxObjSp;
	private String taxObjNewAddrYn;
	private String mngHtm1;
	private String mngHtm2;
	private String mngHtm3;
	private String mngHtm4;
	private String mngHtm5;
	private String mngHtm6;
	private String rmk;
	private String initWrkrId;
	private String bankCd;
	private String txprFullAddr;
	private String txprBasicAddr;
	private String txprBasicDtlAddr;
	private String resultCode;
	private String resultMsg;
	private String impsKey;
	private String result;
	private String ifStDt;
	private String ifEdDt;
	private String txprNo;
	private String txprNm;
	private String txprDtlAddr;

	private String connKey;
	private String rcptPrcpTaxAmt;
	private String rcptAddAmt;
	private String divdRecplttAmt;
	private String fisDt;
	private String wrkDt;
	private String rcptSp;
	private String rcptDt;
	private String rcptTyp;
	private String bndlNo;
	private String trnrDt;

	private String srchStartLogDate;				// 등록일
	private String srchEndLogDate;				// 등록일
	private String srchResultCode;				//검색결과여부
	private String srchTxt;							//검색어

	private Integer itemsPerPageTemp;			// 임시 목록수
}
