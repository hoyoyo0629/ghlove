package saleson.shop.mypage.support;

import lombok.Data;

@Data
public class HonorInfo {
	
	// 기부 지자체 코드
	private String locgovCode;
	
	// 명예 등급 : GOLD - 특급 명예, SILVER - 최우수 명예, BRONZE - 우수명예
	// 조회일자 포함 1년 기부(2024년 1월 1일 조회시 2023.01.02. ~ 2024.01.01. 기부) 기준 지자체별 목표기부금액 조건 달성자
	private String grade;
	
	// 기부 지자체 명
	private String locNm;
	
	// 지자체별 마지막 기부 기준 거주지자체 명
	private String userLocNm;

	// 특급 명예 획득 수
	private int goldCnt;

	// 최우수 명예 획득 수
	private int silverCnt;
	
	// 우수 명예 획득 수
	private int bronzeCnt;
	
	// qr 코드
	private String qrCode;
	
	private String userName;
	
	private String makeQrDateTime;
	
}
