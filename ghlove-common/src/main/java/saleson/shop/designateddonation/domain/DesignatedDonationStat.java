package saleson.shop.designateddonation.domain;

import lombok.Data;

@Data
public class DesignatedDonationStat {

	// 총 목표금액
	private long totTargetAmt;
	
	// 총 모금액
	private long totCntrAmt;
	
	// 총 모금달성율
	private double totAchvRt;
	
	// 총 참여자 수
	private long totCntrCnt;
	
	
	
	
	// 지자체 코드
	private String locgovCode;
	
	// 지자체 명
	private String locgovNm;
	
	// 캠페인(프로젝트) 총 건수
	private long prjCnt;
	
	// 진행 중 건수
	private long statu2Cnt;
	
	// 종료 건수
	private long statu9Cnt;
	
	// 승인대기 건수
	private long statu1Cnt;
	
	// 취약계층 건수
	private long bsnsType100Cnt;
		
	// 문화/예술 건수
	private long bsnsType200Cnt;
	
	// 자원봉사 건수
	private long bsnsType300Cnt;
	
	// 복리증진 건수
	private long bsnsType400Cnt;
	
	// 모금액
	private long cntrAmt;
	
	// 모금달성율
	private double achvRt;
	
	
	
	
}
