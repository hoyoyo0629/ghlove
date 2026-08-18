package saleson.shop.designateddonation.domain;

import lombok.Data;

@Data
public class DesignatedStat {

	private String columnTpDesc;	// 사업구분	
	private long prjBsns100;			// 취약계층
	private long prjBsns200;			// 문화/예술
	private long prjBsns300;			// 자원봉사
	private long prjBsns400;			// 복리증진
	private long prjBsnsTot;			// 합계
	private long targetAmt01;		// 01월 목표금액 
	private long cntrAmt01;			// 01월 모금액		
	private long targetAmt02;		// 02월 목표금액 
	private long cntrAmt02;			// 02월 모금액
	private long targetAmt03;		// 03월 목표금액 	
	private long cntrAmt03;			// 03월 모금액 
	private long targetAmt04;		// 04월 목표금액	
	private long cntrAmt04;			// 04월 모금액	
	private long targetAmt05;		// 05월 목표금액 
	private long cntrAmt05;			// 05월 모금액
	private long targetAmt06;		// 06월 목표금액
	private long cntrAmt06;			// 06월 모금액	
	private long targetAmt07;		// 07월 목표금액 
	private long cntrAmt07;			// 07월 모금액
	private long targetAmt08;		// 08월 목표금액 
	private long cntrAmt08;			// 08월 모금액
	private long targetAmt09;		// 09월 목표금액 
	private long cntrAmt09;			// 09월 모금액 
	private long targetAmt10;		// 10월 목표금액
	private long cntrAmt10;			// 10월 모금액	
	private long targetAmt11;		// 11월 목표금액 
	private long cntrAmt11;			// 11월 모금액
	private long targetAmt12;		// 12월 목표금액
	private long cntrAmt12;			// 12월 모금액
	private long m01;				// 01월 통계
	private long m02;				// 02월 통계
	private long m03;				// 03월 통계
	private long m04;				// 04월 통계
	private long m05;				// 05월 통계
	private long m06;				// 06월 통계
	private long m07;				// 07월 통계
	private long m08;				// 08월 통계
	private long m09;				// 09월 통계
	private long m10;				// 10월 통계
	private long m11;				// 11월 통계
	private long m12;				// 12월 통계
	private long total;				// 합계
	
	private long totTargetAmt;		// 총 목표 금액
	private long totCntrAmt;			// 총 모금액
	private float totAchvRt;			// 총 모금 달성률 
	private long totCntrCnt;			// 총 참여자 수
	private String locgovCode;		// 지자체 코드
	private String locgovNm;		// 지자체명
	private long prjCnt;				// 캠페인(프로젝트) 총건수
	private long statu2Cnt;			// 진행중건수			
	private long statu9Cnt;			// 종료건수
	private long statu1Cnt;			// 대기건수(추가됨)
	private long bsnsType100Cnt;		// 취약게층건수
	private long bsnsType200Cnt;		// 문화/예술(건수)
	private long bsnsType300Cnt;		// 자원봉사(건수)
	private long bsnsType400Cnt;		// 복리증진
	private long targetAmt;			// 목표금액
	private long cntrAmt;			// 모금액
	private float achvRt;				// 달성율
	private String prjStatusDesc;	// 모금상태
	private String prjImage;		// 이미지
	private String bsnsTypeDesc;	// 사업구분
	private String prjSubject;		// 프로젝트명
	private long cntrCnt;			// 참여자수
	private String prjStDt;			// 시작일
	private String prjEdDt;			// 종료일
	
		
}
