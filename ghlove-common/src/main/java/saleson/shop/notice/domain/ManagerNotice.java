package saleson.shop.notice.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerNotice {
	
	private int ManagerNoticeId;
	private String subject;
	private String content;
	private String startDate;
	private String endDate;
	private String useYn;
	private int width;
	private int height;
	private Long frstRegisterId;
	private String frstRegistPnttm;
	private Long lastUpdusrId;
	private String lastUpdtPnttm;
}
