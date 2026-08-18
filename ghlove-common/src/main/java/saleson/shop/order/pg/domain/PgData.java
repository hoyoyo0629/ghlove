package saleson.shop.order.pg.domain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderPayment;
import saleson.shop.order.pg.easypay.domain.EasypayRequest;
import saleson.shop.order.pg.kcp.domain.KcpRequest;
@Getter
@Setter
@NoArgsConstructor @ToString
public class PgData {

	private Order order;
	private OrderPayment orderPayment;

	private String returnUrlParam;

	private String salesonId;
	private String salesonToken;
	private String salesonTokenType;
	private String failUrl;
	private String successUrl;

	private boolean isMobilePage;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String mid;
	private String keypass;



	private String orderCode;
	private String amount;
	private String taxFreeAmount;
	private String approvalType;
	private String deviceType;

	private String instalment;



	// CJ
	private String userID;
	private String username;
	private String userEmail;
	private String userPhone;
	

	// 이니시스
	private String uid;
	private String oid;
	private String goodname;
	private String currency;
	private String encrypted;
	private String sessionkey;
	private String buyername;
	private String buyertel;
	private String buyeremail;
	private String url;
	private String cardcode;
	private String parentemail;
	private String recvname;
	private String recvtel;
	private String recvaddr;
	private String recvpostnum;
	private String recvmsg;
	private String joincard;
	private String joinexpire;
	private String id_customer;
	private String encfield;
	private String certid;
	private String paymethod;


	// 이니시스 웹 결제
	private String resultCode;
	private String authUrl;
	private String authToken;
	private String netCancelUrl;
	
	// 2017.05.25 Jun-Eu Son KSPAY 결제 
	private String sndPaymethod;
	private String sndStoreid;
	private String sndOrdernumber;
	private String sndGoodname;
	private String sndAmount;
	private String sndOrdername;
	private String sndEmail;
	private String sndMobile;
	private String sndServicePeriod;
	private String sndReply;
	private String sndGoodType;
	private String sndShowcard;
	private String sndCurrencytype;
	private String sndInstallmenttype;
	private String sndInteresttype;
	private String reWHCid;
	private String reWHCtype;
	private String reWHHash;


	// Son Jun-Eu 2017.07.28 NHN KCP 결제
	private KcpRequest kcpRequest;
	
	// Son Jun-Eu 2017.09.27 EASYPAY 결제
	private EasypayRequest easypayRequest;
		

	// 이니시스 모바일
	private String P_TID;
	private String P_MID;
	private String P_AUTH_DT;
	private String P_STATUS;
	private String P_TYPE;
	private String P_OID;
	private String P_FN_CD1;
	private String P_FN_CD2;
	private String P_FN_NM;
	private String P_UNAME;
	private String P_AMT;
	private String P_RMESG1;
	private String P_RMESG2;
	private String P_NOTI;
	private String P_AUTH_NO;
	private String P_REQ_URL;
	
	
	// 이니시스 가상계좌
	private String P_VACT_NUM; // 입금할 계좌 번호 
	private String P_VACT_DATE; // 입금 마감 일자
	private String P_VACT_TIME; // 입금 마감 시간
	private String P_VACT_NAME; // 계좌주명
	private String P_VACT_BANK_CODE; // 은행코드
	private String transactionType;


	// 이니시스 가상계좌 입금통보
	private String no_tid;	// 이니시스 거래번호
	private String id_merchant;	//??
	private String no_oid;	// 주문번호
	private String no_vacct;	// 계좌번호
	private String amt_input;	// 입금금액
	private String nm_inputbank;	// 입금은행명
	private String nm_input;	// 입금자명
	private String dt_inputstd;	// 입금일자
	private String no_cshr_appl; // 현금영수증 발급번호
	private String no_cshr_tid;	// 현금영수증 TID
	private String no_req_tid;	// 요청 TID
	private String dt_cshr; // 현금영수증 발급일자
	private String tm_cshr; // 현금영수증 발급시간
	
	

	// 엘지데이콤
	private String LGD_BUYER;
	private String LGD_PRODUCTINFO;
	private String LGD_BUYEREMAIL;
	private String LGD_CUSTOM_USABLEPAY;
	private String LGD_PAYKEY;
	private String LGD_RESPCODE;
	private String LGD_RESPMSG;
	private String LGD_MID;
	private String LGD_OID;
	private String LGD_AMOUNT;
	private String LGD_TID;
	private String LGD_PAYTYPE;
	private String LGD_PAYDATE;
	private String LGD_HASHDATA;
	private String LGD_FINANCECODE;
	private String LGD_FINANCENAME;
	private String LGD_ESCROWYN;
	private String LGD_TIMESTAMP;
	private String LGD_ACCOUNTNUM;
	private String LGD_CASTAMOUNT;
	private String LGD_CASCAMOUNT;
	private String LGD_CASFLAG;
	private String LGD_CASSEQNO;
	private String LGD_CASHRECEIPTNUM;
	private String LGD_CASHRECEIPTSELFYN;
	private String LGD_CASHRECEIPTKIND;
	private String LGD_PAYER;
	private String LGD_BUYERID;
	private String LGD_BUYERADDRESS;
	private String LGD_BUYERPHONE;
	private String LGD_BUYERSSN;
	private String LGD_PRODUCTCODE;
	private String LGD_RECEIVER;
	private String LGD_RECEIVERPHONE;
	private String LGD_DELIVERYINFO;
	


	// 나이스페이
	private String GoodsCnt;
	private String GoodsName;
	private String Amt;
	private String BuyerName;
	private String BuyerTel;
	private String Moid;
	private String PayMethod;
	private String UserIP;
	private String MallIP;
	private String VbankExpDate;
	private String CharSet;
	private String BuyerEmail;
	private String GoodsCl;
	private String TransType;
	private String EncodeParameters;
	private String EdiDate;
	private String EncryptData;
	private String TrKey;
	private String ResultMsg;
	private String AuthDate;
	private String AuthCode;
	private String MallUserID;
	private String Tid;
	private String CardCode;
	private String CardName;
	private String CardQuota;
	private String BankCode;
	private String BankName;
	private String RcptType;
	private String RcptAuthCode;
	private String RcptTID;
	private String Carrier;
	private String DstAddr;
	private String VbankBankCode;
	private String VbankBankName;
	private String VbankNum;
	private String VbankInputName;
	private String OptionList;
	private String SelectCardCode;
	private String SelectQuota;
	private String SocketYN;
	private String ReturnURL;
	private String AcsNoIframe;
	private String AuthResultCode;
	private String AuthResultMsg;
	private String CardCl;
	private String CcPartCl;


	// 나이스페이 가상계좌 입금통보
	private String MID;
	private String TID;
	private String MOID;
	private String FnCd;
	private String name;
	private String CancelDate;


    // 네이버페이 결제형 결제번호
    private String paymentId;


	// 나이스페이 getter/setter


	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getPaymethod() {
		return paymethod;
	}

	public void setPaymethod(String paymethod) {
		this.paymethod = paymethod;
	}

	public String getPayMethod() {
		return PayMethod;
	}

	public String getBuyername() {
		return buyername;
	}

	public void setBuyername(String buyername) {
		this.buyername = buyername;
	}

	public String getBuyertel() {
		return buyertel;
	}

	public void setBuyertel(String buyertel) {
		this.buyertel = buyertel;
	}

	public String getBuyeremail() {
		return buyeremail;
	}

	public void setBuyeremail(String buyeremail) {
		this.buyeremail = buyeremail;
	}

	public String getBuyerName() {
		return BuyerName;
	}

	public String getBuyerTel() {
		return BuyerTel;
	}

	public String getMoid() {
		return Moid;
	}

	public void setMoid(String moid) {
		Moid = moid;
	}

	public String getBuyerEmail() {
		return BuyerEmail;
	}

	public String getCardCode() {
		return CardCode;
	}

	public void setMID(String MID) {
		this.MID = MID;
	}

	public void setTID(String TID) {
		this.TID = TID;
	}

	public void setMOID(String MOID) {
		this.MOID = MOID;
	}

	public void setCardCode(String cardCode) {
		CardCode = cardCode;
	}

	public void setBuyerName(String buyerName) {
		BuyerName = buyerName;
	}

	public void setBuyerTel(String buyerTel) {
		BuyerTel = buyerTel;
	}

	public void setBuyerEmail(String buyerEmail) {
		BuyerEmail = buyerEmail;
	}

	public void setPayMethod(String payMethod) {
		PayMethod = payMethod;
	}

	public String getMID() {
		return MID;
	}

	public String getTID() {
		return TID;
	}

	public String getMOID() {
		return MOID;
	}

	public String getCardcode() {
		return cardcode;
	}

	public void setCardcode(String cardcode) {
		this.cardcode = cardcode;
	}
}
