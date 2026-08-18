package saleson.common.sms.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class HomeTownDayInfo {
	
	private String date;		// 일시 ex) 20230904133000
	private String location;	// 장소 ex) 예술의 전당
	private String partyList;	// 주요행사 ex) 기념식, 박람회등
	private String phoneNumber;	// 수신전화 ex) 01011223344
	
	public Date getParseDate() {
		String REGEX = "^[0-9]{14}$";
		
		if ("".equals(this.date) || this.date == null) return null;
		if (!this.date.matches(REGEX)) return null;
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date d = formatter.parse(this.date);
			return d;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
}
