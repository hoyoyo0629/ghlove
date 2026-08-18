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
public class CmntySrBbsDto extends SearchParam {

	private static final long serialVersionUID = 1L;

	private String locgovCode;		//지자체
	private String startDt;			//시작일시
	private String endDt;			//종료일시
	private String searchRole;		//소속
	private String query;
	private String where;
	private String shWdr;

	private Long userId; 			//유저 아이디
	private String userName;		// 유저 이름
	private String locgovNm;		// 지자체
	private String upperLocgovNm;	// 상위 지자체
	private String authority;		// 소속


	private String bbsCn;			//게시글 내용
	private Long bbsId;				//게시글 아이디
	private String bbsTtl;			//게시글 제목
	private Date frstCrtDt;			//최초등록일시
	private Long frstCrtId;			//최초등록아이디
	private Long inqCnt;			//조회 수
	private Date lastMdfcnDt;		//최종수정일시
	private Long lastMdfcnId;		//최종수정아이디
	private String noticeYn;		//공지 여부
	private String useYn;			//사용 여부
	private int cmntCnt;			//댓글 수
	private String isSecret; 		//비밀글 사용여부
	private String isSecretYn; 		//비밀글 사용

	private int attachedFileCnt;		//첨부파일갯수
	private Long atchFileSz; // 파일 사이즈

	//첨부파일리스트
	private List<CmntySrBbsFileDto> cmntySrBbsFileList;

	//댓글 리스트
	private List<CmntySrBbsCmntDto> cmntySrBbsCmntList;

	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("srBbs")
				.toString();
	}

	public String getIsSecret(){
		if(null == this.isSecret) {
			return "N";
		}
		return this.isSecret;
	}

	public String getNoticeYn(){
		if(null == this.noticeYn) {
			return "N";
		}
		return this.noticeYn;
	}

}