package saleson.shop.community.doamin;

import java.io.File;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import saleson.common.configuration.SalesonProperty;

@Getter
@Setter
public class CmntyRpstrDto extends SearchParam {
	private static final long serialVersionUID = 1L;

	private String adminCheck; //관리자인지 체크
	private Long userId; //유저 아이디
	private String userName;	// 유저 이름
	private String myInfo; //내가 쓴글
	private String locgovNm;	// 지자체
	private String upperLocgovNm;	// 상위 지자체
	private String authority;	// 소속
	private Long atchFileSz; // 파일 사이즈
	private String orgnlAtchFileNm; // 파일명


	private long rpstrId;		//자료실아이디
	private String rpstrTtl;	//자료실제목
	private String rpstrCn;		//자료실내용
	private String useYn;		//사용여부
	private String noticeYn;	//공지여부
	private String inqCnt;		//조회수
	private long frstCrtId;		//최초등록아이디
	private Date frstCrtDt;		//최초등록일시
	private long lastMdfcnId;	//최종수정아이디
	private Date lastMdfcnDt;	//최종수정일시

	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("data-board")
				.toString();
	}
}
