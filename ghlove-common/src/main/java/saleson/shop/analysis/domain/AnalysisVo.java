package saleson.shop.analysis.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class AnalysisVo {
	private String statsYear;
	private String statsMonth;
	private String statsDt;
	private String statsYm;

	private String companyName;
	private String itemName;

	private String deviceType;
	private String locgovCode;
	private String locgovNm;
	private String cntrLocgovCode;
	private String upperLocgovCode;
	private String upperLocgovNm;
	private String sttemntPayDe;
	private String mberCi;
	private String approvalType;
	private String stsfdg1;
	private String stsfdg2;
	private String stsfdg3;
	private String stsfdg4;
	private String menuUrl;
	private String menuNm;
	private String categoryName;
	private String domains;
	private String browser;
	private String os;
	private String title;

	private BigInteger userId;

	private BigInteger cntrCnt;
	private BigInteger onlineCntrCnt;
	private BigInteger offlineCntrCnt;
	private BigInteger cntrPerson;
	private BigInteger cntrAge;
	private BigInteger cntrHour;
	private BigInteger cntrAmt;

	private BigInteger likeCnt;
	private BigInteger stsfdgCnt;
	private BigInteger orderCnt;
	private BigInteger orderAmt;


	private BigInteger itemTotalCnt;
	private BigInteger tourCnt;
	private BigInteger processCnt;
	private BigInteger dailyCnt;
	private BigInteger ticketCnt;
	private BigInteger farmCnt;
	private BigInteger aquaticCnt;
	private BigInteger orderTotalCnt;
	private BigInteger top1;
	private BigInteger top2;
	private BigInteger top3;
	private BigInteger itemTotalAll;
	private BigInteger tourTotalAll;
	private BigInteger processTotalAll;
	private BigInteger dailyTotalAll;
	private BigInteger ticketTotalAll;
	private BigInteger farmTotalAll;
	private BigInteger aquaticTotalAll;
	private BigInteger orderTotalAll;
	private BigInteger salePriceTotalAll;

	private BigInteger payCount;
	private BigInteger payAmount;

	private BigInteger visitCount;
	private String percent;

	private long saleCount;
	private long cancelCount;
	private long saleAmount;
	private long cancelAmount;
	private long sumCount;
	private long sumAmount;

	private long cancelItemPrice;
	private long itemPrice;
	private long couponDiscountPrice;
	private long sellerDiscountPrice;
	private long spotDiscountPrice;
	private long LevelDiscountPrice;
	private long setDiscountPrice;
	private long cancelCouponDiscountPrice;
	private long cancelSellerDiscountPrice;
	private long cancelSpotDiscountPrice;
	private long cancelLevelDiscountPrice;
	private long cancelSetDiscountPrice;
	private long shipping;
	private long cancelShipping;



	private String frstRegisterId;
	private String lastUpdusrId;
	private LocalDateTime frstRegistPnttm;
	private LocalDateTime lastUpdtPnttm;

}
