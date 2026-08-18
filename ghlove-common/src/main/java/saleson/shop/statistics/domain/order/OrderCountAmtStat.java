package saleson.shop.statistics.domain.order;

import lombok.Data;

@Data
public class OrderCountAmtStat {
	
	/**
	 * 일수 + 요일 : ex) 23(목)
	 */
	private String dayOfWeek;
	
	/**
	 * 주문금액 합계
	 */
	private long totalSaleAmt;
	
	/**
	 * 주문건수 합계
	 */
	private long totalCnt;
	
	/**
	 * 주문월
	 */
	private int payMonth;
	
	/**
	 * 누적 주문금액 합계
	 */
	private long addTotalSaleAmt;
	
	/**
	 * 누적 주문건수 합계
	 */
	private long addTotalCnt;
	
	/**
	 * 주문 월 문자열 : ex) 1월
	 */
	private String payMonthStr;
	
	
	
	
	/**
	 * 금일 주문금액
	 */
	private long nowDaySaleAmt;
	
	/**
	 * 전일 주문금액
	 */
	private long ystDaySaleAmt;
	
	/**
	 * 금년 1월 1일부터 금년 금월 금일까지 누적 주문 금액
	 * (24년 4월 1일 조회할 경우 24년 1월 1일 부터 24년 4월 1일까지 누적 주문 금액)
	 */
	private long nowYearAddTotalSaleAmt;
	
	/**
	 * 전년 1월 1일부터 전년 금월 금일까지 누적 주문 금액
	 * (24년 4월 1일 조회할 경우 23년 1월 1일 부터 23년 4월 1일까지 누적 주문 금액)
	 */
	private long bfYearAddTotalSaleAmt;
	
	/**
	 * 전일 주문금액 대비 금일 주문금액 비율
	 * nowDaySaleAmt / ystDaySaleAmt * 100
	 */
	private float difDaySaleAmt;
	
	/**
	 * 전년 금월 금일 누적 주문금액 대비 금년 금월 금일 누적 주문금액 비율
	 * nowYearAddTotalSaleAmt / bfYearAddTotalSaleAmt * 100
	 */
	private float difYearSaleAmt;
	
	/**
	 * 금일 주문 건수
	 */
	private int nowDayCnt;
	
	/**
	 * 전일 주문 건수
	 */
	private int ystDayCnt;
	
	/**
	 * 금년 1월 1일부터 금년 금월 금일까지 누적 주문 건수
	 */
	private int nowYearAddTotalCnt;
	
	/**
	 * 전년 1월 1일부터 전년 금월 금일까지 누적 주문 건수
	 */
	private int bfYearAddTotalCnt;
	
	/**
	 * 전일 대비 금일 주문 건수 비율
	 * nowDayCnt / ystDayCnt * 100
	 */
	private float difDayCnt;
	
	/**
	 * 전년 금월 금일 대비 금년 금월 금일 누적 주문 건수 비율
	 * nowYearAddTotalCnt / bfYearAddTotalCnt * 100
	 */
	private float difYearCnt;
	
}
