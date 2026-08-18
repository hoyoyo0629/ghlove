package saleson.common.sms.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GiveTifIpsSndngM extends TifIpsSndngM {

    
    private String taxYear;		// 년도

    public String getEsbInitTime() {
    	return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
