package saleson.shop.designateddonation.domain;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.common.utils.UserUtils;

@Data
@NoArgsConstructor
public class PrjNoticeFile {
	
	private long prjNoticeId;
	
	private int fileSeq;
	
	private String fileName;
	
	private String pathName;
	
	private String orgFileName;
	
	// 최초등록자 ID
	private long frstRegisterId;
	
	// 최초 등록 시점
	private Timestamp frstRegistPnttm;
	
	public long getUserId() {
		try {
			return UserUtils.getUser().getUserId();			
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
}
