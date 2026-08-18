package saleson.shop.community.doamin;

import java.io.File;
import java.util.Date;
import java.util.List;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import saleson.common.configuration.SalesonProperty;

@Getter
@Setter
@ToString
public class CmntyFaqBbsCmntDto  extends SearchParam {

	private static final long serialVersionUID = 1L;
	private Long cmntId;		//댓글 아이디
	private Long bbsId;			//게시글 아이디
	private String cmntCn;		//댓글 내용
	private String useYn;		//사용 여부
	private Date frstCrtDt;		//최초등록일시
	private Long frstCrtId;		//최초등록아이디
	private Date lastMdfcnDt;	//최종수정일시
	private Long lastMdfcnId;	//최종수정아이디
	private String authority;	//권한
	private String upperLocgovNm;
	private String locgovNm;
	private String userName;

	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("faqBbsCmnt")
				.toString();
	}

	//첨부파일 리스트
	private List<CmntyFaqBbsCmntFileDto> cmntyFaqBbsCmntFileList;
}
