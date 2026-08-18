package saleson.shop.designateddonation.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class DsgncntrConfirmLog {
	
	private long logNo;

	/**
	 *  승인이력 아이디
	 */
	private long dsgncntrConfirmId;
	
	/**
	 * 지정기부 아이디
	 */
	private long prjId;
	
	/**
	 * 승인 전 상태
	 */
	private String bfStatus;
	
	/**
	 * 승인 후 상태
	 */
	private String tbStatus;
	
	/**
	 * 승인자 아이디
	 */
	private long registerId;
	
	/**
	 * 승인일시
	 */
	private Timestamp registPnttm;
	
	/**
	 * 승인자 이름
	 */
	private String registerName;
	
	/**
	 * 지정기부 사업명
	 */
	private String prjSubject;
	
	/**
	 * 승인자 로그인 아이디
	 */
//	private String registerLoginId;
	
	/**
	 * 승인 전 상태 
	 * @return
	 */
	public String getBfStatusStr() {
		return getStatusStr(bfStatus);
	}
	
	/**
	 * 승인 후 상태
	 * @return
	 */
	public String getTbStatusStr() {
		return getStatusStr(tbStatus);
	}
	
	/**
	 * 승인 상태 코드를 승인상태 이름으로 리턴
	 * @param status
	 * @return
	 */
	private String getStatusStr(String status) {
		switch (status) {
		case "1":
			return "대기";
		case "2":
			return "진행";
		case "9":
			return "종료";
		default:
			return "";
		}
	}
	
	
	/**
	 * 시간 timestamp 를 'yyyy-MM-dd HH:mm:ss' 형태 문자열로 리턴
	 * @return
	 */
	public String getRegistPnttmStr() {
		if (registPnttm == null) {
			return "";
		} else {
			LocalDateTime dateTime = registPnttm.toLocalDateTime();
			return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
	}
	
	
	
}
