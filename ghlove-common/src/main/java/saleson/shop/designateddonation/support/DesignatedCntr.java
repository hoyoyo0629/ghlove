package saleson.shop.designateddonation.support;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.StringUtils;

import lombok.Data;
import saleson.common.utils.UserUtils;

@Data
public class DesignatedCntr {

	// 아이디
	private long prjId;
	
	// 로그인 아이디
	private String loginId;
	
	// 이름
	private String userName;
	
	// 기부일
	private String cntrDe;

	// 기부금액
	private int cntrAmt;
	
	// 본인 기부 여부
	private int giveOrder;
	
	// 응원 메시지
	private String cheerMsg;
	
	// 기부번호
	private String cntrSn;
	
	public String getCntrDeFormat() {
		if (!StringUtils.isEmpty(cntrDe)) {
			int length = cntrDe.length();
			if (length == 8) {
				LocalDate localDate = LocalDate.parse(cntrDe, DateTimeFormatter.ofPattern("yyyyMMdd"));
				return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			}
		}
		return "";
	}
	
	public long getUserId() {
		try {
			return UserUtils.getUser().getUserId();	
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
}
