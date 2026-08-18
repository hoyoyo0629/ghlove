package saleson.shop.statistics.domain.order;

import com.onlinepowers.framework.util.StringUtils;

import lombok.Data;

@Data
public class OrderCountAmtStatMonthLclgv {
	
	/**
	 * 상위 지자체코드
	 */
	private String upperLocgovCode;
	
	/**
	 * 지자체 코드
	 */
	private String locgovCode;
	
	/**
	 * 상위 지자체 명
	 */
	private String upperLocgovNm;
	
	/**
	 * 지자체 명
	 */
	private String locgovNm;
	
	/**
	 * 당해년도 1월 전체 판매 금액
	 */
	private long nowYr1mmWholNtslAmt;
	
	/**
	 * 이전년도 1월 전체 판매 금액
	 */
	private long bfrYr1mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 1월 비율
	 */
	private double wholNtslAmt1mmRt;
	
	/**
	 * 당해년도 2월 전체 판매 금액
	 */
	private long nowYr2mmWholNtslAmt;
	
	/**
	 * 이전년도 2월 전체 판매 금액
	 */
	private long bfrYr2mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 2월 비율
	 */
	private double wholNtslAmt2mmRt;
	
	/**
	 * 당해년도 3월 전체 판매 금액
	 */
	private long nowYr3mmWholNtslAmt;
	
	/**
	 * 이전년도 3월 전체 판매 금액
	 */
	private long bfrYr3mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 3월 비율
	 */
	private double wholNtslAmt3mmRt;
	
	/**
	 * 당해년도 4월 전체 판매 금액
	 */
	private long nowYr4mmWholNtslAmt;
	
	/**
	 * 이전년도 4월 전체 판매 금액
	 */
	private long bfrYr4mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 4월 비율
	 */
	private double wholNtslAmt4mmRt;
	
	/**
	 * 당해년도 5월 전체 판매 금액
	 */
	private long nowYr5mmWholNtslAmt;
	
	/**
	 * 이전년도 5월 전체 판매 금액
	 */
	private long bfrYr5mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 5월 비율
	 */
	private double wholNtslAmt5mmRt;
	
	/**
	 * 당해년도 6월 전체 판매 금액
	 */
	private long nowYr6mmWholNtslAmt;
	
	/**
	 * 이전년도 6월 전체 판매 금액
	 */
	private long bfrYr6mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 6월 비율
	 */
	private double wholNtslAmt6mmRt;
	
	/**
	 * 당해년도 7월 전체 판매 금액
	 */
	private long nowYr7mmWholNtslAmt;
	
	/**
	 * 이전년도 7월 전체 판매 금액
	 */
	private long bfrYr7mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 7월 비율
	 */
	private double wholNtslAmt7mmRt;
	
	/**
	 * 당해년도 8월 전체 판매 금액
	 */
	private long nowYr8mmWholNtslAmt;
	
	/**
	 * 이전년도 8월 전체 판매 금액
	 */
	private long bfrYr8mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 8월 비율
	 */
	private double wholNtslAmt8mmRt;
	
	/**
	 * 당해년도 9월 전체 판매 금액
	 */
	private long nowYr9mmWholNtslAmt;
	
	/**
	 * 이전년도 9월 전체 판매 금액
	 */
	private long bfrYr9mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 9월 비율
	 */
	private double wholNtslAmt9mmRt;
	
	/**
	 * 당해년도 10월 전체 판매 금액
	 */
	private long nowYr10mmWholNtslAmt;
	
	/**
	 * 이전년도 10월 전체 판매 금액
	 */
	private long bfrYr10mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 10월 비율
	 */
	private double wholNtslAmt10mmRt;
	
	/**
	 * 당해년도 11월 전체 판매 금액
	 */
	private long nowYr11mmWholNtslAmt;
	
	/**
	 * 이전년도 11월 전체 판매 금액
	 */
	private long bfrYr11mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 11월 비율
	 */
	private double wholNtslAmt11mmRt;
	
	/**
	 * 당해년도 12월 전체 판매 금액
	 */
	private long nowYr12mmWholNtslAmt;
	
	/**
	 * 이전년도 12월 전체 판매 금액
	 */
	private long bfrYr12mmWholNtslAmt;
	
	/**
	 * 전체 판매 금액 12월 비율
	 */
	private double wholNtslAmt12mmRt;
	
	/**
	 * 당해년도 1월 전체 판매 건수
	 */
	private long nowYr1mmWholNtslNocs;
	
	/**
	 * 이전년도 1월 전체 판매 건수
	 */
	private long bfrYr1mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 1월 비율
	 */
	private double wholNtslNocs1mmRt;
	
	/**
	 * 당해년도 2월 전체 판매 건수
	 */
	private long nowYr2mmWholNtslNocs;
	
	/**
	 * 이전년도 2월 전체 판매 건수
	 */
	private long bfrYr2mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 2월 비율
	 */
	private double wholNtslNocs2mmRt;
	
	/**
	 * 당해년도 3월 전체 판매 건수
	 */
	private long nowYr3mmWholNtslNocs;
	
	/**
	 * 이전년도 3월 전체 판매 건수
	 */
	private long bfrYr3mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 3월 비율
	 */
	private double wholNtslNocs3mmRt;
	
	/**
	 * 당해년도 4월 전체 판매 건수
	 */
	private long nowYr4mmWholNtslNocs;
	
	/**
	 * 이전년도 4월 전체 판매 건수
	 */
	private long bfrYr4mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 4월 비율
	 */
	private double wholNtslNocs4mmRt;
	
	/**
	 * 당해년도 5월 전체 판매 건수
	 */
	private long nowYr5mmWholNtslNocs;
	
	/**
	 * 이전년도 5월 전체 판매 건수
	 */
	private long bfrYr5mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 5월 비율
	 */
	private double wholNtslNocs5mmRt;
	
	/**
	 * 당해년도 6월 전체 판매 건수
	 */
	private long nowYr6mmWholNtslNocs;
	
	/**
	 * 이전년도 6월 전체 판매 건수
	 */
	private long bfrYr6mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 6월 비율
	 */
	private double wholNtslNocs6mmRt;
	
	/**
	 * 당해년도 7월 전체 판매 건수
	 */
	private long nowYr7mmWholNtslNocs;
	
	/**
	 * 이전년도 7월 전체 판매 건수
	 */
	private long bfrYr7mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 7월 비율
	 */
	private double wholNtslNocs7mmRt;
	
	/**
	 * 당해년도 8월 전체 판매 건수
	 */
	private long nowYr8mmWholNtslNocs;
	
	/**
	 * 이전년도 8월 전체 판매 건수
	 */
	private long bfrYr8mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 8월 비율
	 */
	private double wholNtslNocs8mmRt;
	
	/**
	 * 당해년도 9월 전체 판매 건수
	 */
	private long nowYr9mmWholNtslNocs;
	
	/**
	 * 이전년도 9월 전체 판매 건수
	 */
	private long bfrYr9mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 9월 비율
	 */
	private double wholNtslNocs9mmRt;
	
	/**
	 * 당해년도 10월 전체 판매 건수
	 */
	private long nowYr10mmWholNtslNocs;
	
	/**
	 * 이전년도 10월 전체 판매 건수
	 */
	private long bfrYr10mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 10월 비율
	 */
	private double wholNtslNocs10mmRt;
	
	/**
	 * 당해년도 11월 전체 판매 건수
	 */
	private long nowYr11mmWholNtslNocs;
	
	/**
	 * 이전년도 11월 전체 판매 건수
	 */
	private long bfrYr11mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 11월 비율
	 */
	private double wholNtslNocs11mmRt;
	
	/**
	 * 당해년도 12월 전체 판매 건수
	 */
	private long nowYr12mmWholNtslNocs;
	
	/**
	 * 이전년도 12월 전체 판매 건수
	 */
	private long bfrYr12mmWholNtslNocs;
	
	/**
	 * 전체 판매 건수 12월 비율
	 */
	private double wholNtslNocs12mmRt;
	
	/**
	 * 당해년도 전체 금액
	 * @return
	 */
	public String getLclgvNowYrWholNtslAmtStr() {
		return StringUtils.numberFormat(getLclgvNowYrWholNtslAmt());
	}
	
	/**
	 * 이전년도 전체 금액
	 * @return
	 */
	public String getLclgvBfrYrWholNtslAmtStr() {
		return StringUtils.numberFormat(getLclgvBfrYrWholNtslAmt());
	}
	
	/**
	 * 당해년도 전체 금액
	 * @return
	 */
	public long getLclgvNowYrWholNtslAmt() {
		return nowYr1mmWholNtslAmt
				+ nowYr2mmWholNtslAmt
				+ nowYr3mmWholNtslAmt
				+ nowYr4mmWholNtslAmt
				+ nowYr5mmWholNtslAmt
				+ nowYr6mmWholNtslAmt
				+ nowYr7mmWholNtslAmt
				+ nowYr8mmWholNtslAmt
				+ nowYr9mmWholNtslAmt
				+ nowYr10mmWholNtslAmt
				+ nowYr11mmWholNtslAmt
				+ nowYr12mmWholNtslAmt;
	}
	
	/**
	 * 이전년도 전체 금액
	 * @return
	 */
	public long getLclgvBfrYrWholNtslAmt() {
		return bfrYr1mmWholNtslAmt
				+ bfrYr2mmWholNtslAmt
				+ bfrYr3mmWholNtslAmt
				+ bfrYr4mmWholNtslAmt
				+ bfrYr5mmWholNtslAmt
				+ bfrYr6mmWholNtslAmt
				+ bfrYr7mmWholNtslAmt
				+ bfrYr8mmWholNtslAmt
				+ bfrYr9mmWholNtslAmt
				+ bfrYr10mmWholNtslAmt
				+ bfrYr11mmWholNtslAmt
				+ bfrYr12mmWholNtslAmt;
	}
	
	public OrderCountAmtStatMonthLclgv getCopyObj() {
		OrderCountAmtStatMonthLclgv copy = new OrderCountAmtStatMonthLclgv();
		
		copy.setUpperLocgovCode(upperLocgovCode);
		copy.setLocgovCode(locgovCode);
		copy.setUpperLocgovNm(upperLocgovNm);
		copy.setLocgovNm(locgovNm);
		
		copy.setNowYr1mmWholNtslAmt(nowYr1mmWholNtslAmt);
		copy.setNowYr2mmWholNtslAmt(nowYr2mmWholNtslAmt);
		copy.setNowYr3mmWholNtslAmt(nowYr3mmWholNtslAmt);
		copy.setNowYr4mmWholNtslAmt(nowYr4mmWholNtslAmt);
		copy.setNowYr5mmWholNtslAmt(nowYr5mmWholNtslAmt);
		copy.setNowYr6mmWholNtslAmt(nowYr6mmWholNtslAmt);
		copy.setNowYr7mmWholNtslAmt(nowYr7mmWholNtslAmt);
		copy.setNowYr8mmWholNtslAmt(nowYr8mmWholNtslAmt);
		copy.setNowYr9mmWholNtslAmt(nowYr9mmWholNtslAmt);
		copy.setNowYr10mmWholNtslAmt(nowYr10mmWholNtslAmt);
		copy.setNowYr11mmWholNtslAmt(nowYr11mmWholNtslAmt);
		copy.setNowYr12mmWholNtslAmt(nowYr12mmWholNtslAmt);
		
		copy.setNowYr1mmWholNtslNocs(nowYr1mmWholNtslNocs);
		copy.setNowYr2mmWholNtslNocs(nowYr2mmWholNtslNocs);
		copy.setNowYr3mmWholNtslNocs(nowYr3mmWholNtslNocs);
		copy.setNowYr4mmWholNtslNocs(nowYr4mmWholNtslNocs);
		copy.setNowYr5mmWholNtslNocs(nowYr5mmWholNtslNocs);
		copy.setNowYr6mmWholNtslNocs(nowYr6mmWholNtslNocs);
		copy.setNowYr7mmWholNtslNocs(nowYr7mmWholNtslNocs);
		copy.setNowYr8mmWholNtslNocs(nowYr8mmWholNtslNocs);
		copy.setNowYr9mmWholNtslNocs(nowYr9mmWholNtslNocs);
		copy.setNowYr10mmWholNtslNocs(nowYr10mmWholNtslNocs);
		copy.setNowYr11mmWholNtslNocs(nowYr11mmWholNtslNocs);
		copy.setNowYr12mmWholNtslNocs(nowYr12mmWholNtslNocs);
		
		copy.setBfrYr1mmWholNtslAmt(bfrYr1mmWholNtslAmt);
		copy.setBfrYr2mmWholNtslAmt(bfrYr2mmWholNtslAmt);
		copy.setBfrYr3mmWholNtslAmt(bfrYr3mmWholNtslAmt);
		copy.setBfrYr4mmWholNtslAmt(bfrYr4mmWholNtslAmt);
		copy.setBfrYr5mmWholNtslAmt(bfrYr5mmWholNtslAmt);
		copy.setBfrYr6mmWholNtslAmt(bfrYr6mmWholNtslAmt);
		copy.setBfrYr7mmWholNtslAmt(bfrYr7mmWholNtslAmt);
		copy.setBfrYr8mmWholNtslAmt(bfrYr8mmWholNtslAmt);
		copy.setBfrYr9mmWholNtslAmt(bfrYr9mmWholNtslAmt);
		copy.setBfrYr10mmWholNtslAmt(bfrYr10mmWholNtslAmt);
		copy.setBfrYr11mmWholNtslAmt(bfrYr11mmWholNtslAmt);
		copy.setBfrYr12mmWholNtslAmt(bfrYr12mmWholNtslAmt);
		
		copy.setBfrYr1mmWholNtslNocs(bfrYr1mmWholNtslNocs);
		copy.setBfrYr2mmWholNtslNocs(bfrYr2mmWholNtslNocs);
		copy.setBfrYr3mmWholNtslNocs(bfrYr3mmWholNtslNocs);
		copy.setBfrYr4mmWholNtslNocs(bfrYr4mmWholNtslNocs);
		copy.setBfrYr5mmWholNtslNocs(bfrYr5mmWholNtslNocs);
		copy.setBfrYr6mmWholNtslNocs(bfrYr6mmWholNtslNocs);
		copy.setBfrYr7mmWholNtslNocs(bfrYr7mmWholNtslNocs);
		copy.setBfrYr8mmWholNtslNocs(bfrYr8mmWholNtslNocs);
		copy.setBfrYr9mmWholNtslNocs(bfrYr9mmWholNtslNocs);
		copy.setBfrYr10mmWholNtslNocs(bfrYr10mmWholNtslNocs);
		copy.setBfrYr11mmWholNtslNocs(bfrYr11mmWholNtslNocs);
		copy.setBfrYr12mmWholNtslNocs(bfrYr12mmWholNtslNocs);

		copy.setWholNtslAmt1mmRt(wholNtslAmt1mmRt);
		copy.setWholNtslAmt2mmRt(wholNtslAmt2mmRt);
		copy.setWholNtslAmt3mmRt(wholNtslAmt3mmRt);
		copy.setWholNtslAmt4mmRt(wholNtslAmt4mmRt);
		copy.setWholNtslAmt5mmRt(wholNtslAmt5mmRt);
		copy.setWholNtslAmt6mmRt(wholNtslAmt6mmRt);
		copy.setWholNtslAmt7mmRt(wholNtslAmt7mmRt);
		copy.setWholNtslAmt8mmRt(wholNtslAmt8mmRt);
		copy.setWholNtslAmt9mmRt(wholNtslAmt9mmRt);
		copy.setWholNtslAmt10mmRt(wholNtslAmt10mmRt);
		copy.setWholNtslAmt11mmRt(wholNtslAmt11mmRt);
		copy.setWholNtslAmt12mmRt(wholNtslAmt12mmRt);

		copy.setWholNtslNocs1mmRt(wholNtslNocs1mmRt);
		copy.setWholNtslNocs2mmRt(wholNtslNocs2mmRt);
		copy.setWholNtslNocs3mmRt(wholNtslNocs3mmRt);
		copy.setWholNtslNocs4mmRt(wholNtslNocs4mmRt);
		copy.setWholNtslNocs5mmRt(wholNtslNocs5mmRt);
		copy.setWholNtslNocs6mmRt(wholNtslNocs6mmRt);
		copy.setWholNtslNocs7mmRt(wholNtslNocs7mmRt);
		copy.setWholNtslNocs8mmRt(wholNtslNocs8mmRt);
		copy.setWholNtslNocs9mmRt(wholNtslNocs9mmRt);
		copy.setWholNtslNocs10mmRt(wholNtslNocs10mmRt);
		copy.setWholNtslNocs11mmRt(wholNtslNocs11mmRt);
		copy.setWholNtslNocs12mmRt(wholNtslNocs12mmRt);
		
		return copy;
	}
	
	public String getWholNtslAmt1mmRtStr() {
		if (wholNtslAmt1mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt1mmRt + "%";
		}
	}
	
	public String getWholNtslAmt2mmRtStr() {
		if (wholNtslAmt2mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt2mmRt + "%";
		}
	}
	
	public String getWholNtslAmt3mmRtStr() {
		if (wholNtslAmt3mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt3mmRt + "%";
		}
	}
	
	public String getWholNtslAmt4mmRtStr() {
		if (wholNtslAmt4mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt4mmRt + "%";
		}
	}
	
	public String getWholNtslAmt5mmRtStr() {
		if (wholNtslAmt5mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt5mmRt + "%";
		}
	}
	
	public String getWholNtslAmt6mmRtStr() {
		if (wholNtslAmt6mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt6mmRt + "%";
		}
	}
	
	public String getWholNtslAmt7mmRtStr() {
		if (wholNtslAmt7mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt7mmRt + "%";
		}
	}
	
	public String getWholNtslAmt8mmRtStr() {
		if (wholNtslAmt8mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt8mmRt + "%";
		}
	}
	
	public String getWholNtslAmt9mmRtStr() {
		if (wholNtslAmt9mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt9mmRt + "%";
		}
	}
	
	public String getWholNtslAmt10mmRtStr() {
		if (wholNtslAmt10mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt10mmRt + "%";
		}
	}
	
	public String getWholNtslAmt11mmRtStr() {
		if (wholNtslAmt11mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt11mmRt + "%";
		}
	}
	
	public String getWholNtslAmt12mmRtStr() {
		if (wholNtslAmt12mmRt == -1) {
			return "-";
		} else {
			return wholNtslAmt12mmRt + "%";
		}
	}
	
	public String getWholNtslNocs1mmRtStr() {
		if (wholNtslNocs1mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs1mmRt + "%";
		}
	}
	
	public String getWholNtslNocs2mmRtStr() {
		if (wholNtslNocs2mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs2mmRt + "%";
		}
	}
	
	public String getWholNtslNocs3mmRtStr() {
		if (wholNtslNocs3mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs3mmRt + "%";
		}
	}
	
	public String getWholNtslNocs4mmRtStr() {
		if (wholNtslNocs4mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs4mmRt + "%";
		}
	}
	
	public String getWholNtslNocs5mmRtStr() {
		if (wholNtslNocs5mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs5mmRt + "%";
		}
	}
	
	public String getWholNtslNocs6mmRtStr() {
		if (wholNtslNocs6mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs6mmRt + "%";
		}
	}
	
	public String getWholNtslNocs7mmRtStr() {
		if (wholNtslNocs7mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs7mmRt + "%";
		}
	}
	
	public String getWholNtslNocs8mmRtStr() {
		if (wholNtslNocs8mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs8mmRt + "%";
		}
	}
	
	public String getWholNtslNocs9mmRtStr() {
		if (wholNtslNocs9mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs9mmRt + "%";
		}
	}
	
	public String getWholNtslNocs10mmRtStr() {
		if (wholNtslNocs10mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs10mmRt + "%";
		}
	}
	
	public String getWholNtslNocs11mmRtStr() {
		if (wholNtslNocs11mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs11mmRt + "%";
		}
	}
	
	public String getWholNtslNocs12mmRtStr() {
		if (wholNtslNocs12mmRt == -1) {
			return "-";
		} else {
			return wholNtslNocs12mmRt + "%";
		}
	}
	
}
