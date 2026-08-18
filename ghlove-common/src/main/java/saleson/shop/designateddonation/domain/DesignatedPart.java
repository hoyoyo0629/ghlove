package saleson.shop.designateddonation.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.onlinepowers.framework.web.domain.ListParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import saleson.common.utils.UserUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DesignatedPart extends ListParam{

	// 지정기부 부서 아이디
	private long dsgncntrPartId;
	
	// 지정기부 부서 명
	private String dsgncntrPartName;
		
	// 상위 지자체 코드
	private String upperLocgovCode;
	
	// 지자체 코드
	private String locgovCode;
	
	// 지자체 명
	private String locgovNm;
	
	// 사용 유무
	private String useYn;
	
	// 최초등록자 ID
	private long frstRegisterId;
	
	// 최초 등록 시점
	private Timestamp frstRegistPnttm;
	
	// 최종수정자 ID
	private long lastUpdusrId;
	
	// 최종 수정 시점
	private Timestamp lastUpdtPnttm;
	
	// 부서 수정할 담당자 아이디
	private long partUserId;
	
	// 요청 순번
	private int reqstSn;
	
	// 사용자 아이디
	private long userId;
	
	
	public long getUserId() {
		try {
			return UserUtils.getUser().getUserId();
		} catch(NullPointerException e) {
			return userId;
		}
	}
	
	public String getCreatedDateStr() {
		if (frstRegistPnttm != null) {
			LocalDateTime date = frstRegistPnttm.toLocalDateTime();
			return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} else {
			return "";
		}
	}
	
}
