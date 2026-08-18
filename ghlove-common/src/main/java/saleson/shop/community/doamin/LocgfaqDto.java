package saleson.shop.community.doamin;



import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import saleson.common.enumeration.FaqType;


@Data
public class LocgfaqDto extends SearchParam {
	
	private Map<FaqType, String> faqTypeMap;

	private long id;
	private FaqType faqType;//faq타입
	private String subject; //제목
	private String content; // 내용
	private	long adminId; //만든 사람
	private long updatedBy; // 업데이트한 사람
	private int hits; //조회수
	private String useYn;//사용여부
	private	Date createdDate; //만들날짜
	private Date updatedDate; //업데이트
	private String adminCheck; //어드민 체크
	private String faqTypeName;
	//일괄삭제
	private List<String> databoardList;

}
