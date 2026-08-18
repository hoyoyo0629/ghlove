package saleson.shop.designateddonation.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DesignatedDonationNoticeParam extends SearchParam {

	private static final long serialVersionUID = 4097024713358173537L;

	// 상위 지자체 코드
	private String upperLocgovCode;
	
	// 지자체 코드
	private String locgovCode;
	
	// 부서아이디
	private long dsgncntrPartId;
	
	// 지정기부 공지사항 아이디
	private long prjNoticeId;
	
	// 지정기부 아이디	
	private long prjId;
	
	// 지정기부 공지사항 이미지 일련번호
	private int fileSeq;
	
	
}
