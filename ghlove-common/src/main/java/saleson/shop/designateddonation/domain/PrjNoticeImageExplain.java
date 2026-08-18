package saleson.shop.designateddonation.domain;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.common.utils.UserUtils;

@Data
@NoArgsConstructor
public class PrjNoticeImageExplain {
	
	private long prjNoticeId;
	
	private int imgSeq;
	
	private String imgDesc;
	
	private long frstRegisterId;
	
	private Timestamp frstRegistPnttm;
	
	public long getUserId() {
		return UserUtils.getUser().getUserId();
	}
	
}
