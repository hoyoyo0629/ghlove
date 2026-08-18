package saleson.common.sms.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.common.enumeration.SmsType;

@Data
@NoArgsConstructor
public class ReceiverInfo {

//	private String infoCrtDt;				// 정보생성일시 ex)2021/12/16 10:39:00
//    private int listSn;						// 목록일련번호 ex)1
//    private long insttCrtSn;				// 기관생성일련번호 ex)1234
//    private String svcId;					// 서비스아이디 (별도전달)
//    private final String svcGrpId;				// 서비스그룹아이디 (별도전달)
//    private String insttCrtDocId;			// 기관생성문서아이디
    private String prvcIdntfcInfo;			// 개인정보식별정보
//    private final String prvcIdntfcSeCd;			// 개인정보식별구분코드 (C0090001 : CI정보, C0090002 : 주민등록번호, C0090003 : 이름+전화번호 해시)
    private String sndngCntnts;				// 발송내용 (가변정보를 순서에 따라 입력. | 으로 구분) ex)xxx|2021년 12월 11일|통지서|사용자명|날짜|문서명
    
//    private String esbIfId;					// 인터페이스 아이디
//    private String esbInitTime;				// 연계발생일시
//    private String esbStatusCd;				// 연계처리 상태 코드 (N 입력, 솔루션에서 성공시 S, 실패시 F 로 변경함)
//    private final String esbWorkGbn;				// 작업 구분 코드
    
    private SmsType smsType;				// 알림 종류
    
    private LocalDateTime msgDateTime;
    
    // XXXX.XX.XX.(X) XX XX:XX 형태 표출
    public String getLocalDateTimeToStr() {
    	if (msgDateTime == null) {
    		msgDateTime = LocalDateTime.now();
    	}
    	String ymd = msgDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd."));
    	String hm = msgDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    	DayOfWeek week = msgDateTime.getDayOfWeek();
    	String weekStr = "";
    	switch (week) {
			case MONDAY:
				weekStr = "월";
				break;
			case TUESDAY:
				weekStr = "화";
				break;
			case WEDNESDAY:
				weekStr = "수";
				break;
			case THURSDAY:
				weekStr = "목";
				break;
			case FRIDAY:
				weekStr = "금";
				break;
			case SATURDAY:
				weekStr = "토";
				break;
			case SUNDAY:
				weekStr = "일";
				break;
		}
    	String time = "";
    	if (msgDateTime.getHour() < 12) {
    		time = "오전";
    	} else {
    		time = "오후";
    		hm = msgDateTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm"));
    	}
    	return ymd + "(" + weekStr + ") " + time + " " + hm;
    }
    
    // `, ', #, 탭 버튼으로 생긴 간격 문자가 포함되면 국민비서 전체 앱 서비스 오류 발생한다고 하므로 주의 필요
    public String getSndngCntnts() {
    	if (sndngCntnts != null) {
    		sndngCntnts.replaceAll("`", "\"");
    		sndngCntnts.replaceAll("'", "\"");
    		sndngCntnts.replaceAll("#", "▲");
    		sndngCntnts.replaceAll("\t", " ");
    	}
    	return sndngCntnts;
    }
}
