package saleson.shop.community.doamin;

import java.util.Date;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CmntyCmntDto extends SearchParam {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Long cmntId;	//게시판 아이디
	private Long bbsId;	//게시글 아이디
	private String cmntCn;	//코멘트 내용
	private String useYn;	//사용 여부
	private Date frstCrtDt;	//최초등록일시
	private Long frstCrtId;	//최초등록아이디
	private Date lastMdfcnDt;	//최종수정일시
	private Long lastMdfcnId;	//최종수정아이디
	private String authority;	//권한
	private String upperLocgovNm;
	private String locgovNm;
	private String userName;

}