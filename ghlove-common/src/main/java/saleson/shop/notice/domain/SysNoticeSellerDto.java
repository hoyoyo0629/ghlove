package saleson.shop.notice.domain;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import saleson.common.configuration.SalesonProperty;
import saleson.shop.community.doamin.CmntyFaqBbsFileDto;
import saleson.shop.community.doamin.CmntySrBbsCmntFileDto;

@Getter
@Setter
@ToString
public class SysNoticeSellerDto extends SearchParam {

	private static final long serialVersionUID = 1L;

	private int noticeId;
	private String subject;
	private String content;
	private int hits;
	private String boardCode;
	private String subCategory;
	private String noticeFlag = "N";
	private String displayFlag;
	private String locgovCode;
	private String useYn;
	private String newFlag;
	private String locgovNm;
	private String startDt;
	private String endDt;

	private Date frstCrtDt;						//최초등록일시
	private Long frstCrtId;						//최초등록아이디
	private String frstCrtUserName;				//최초등록자 이름
	private Date lastMdfcnDt;					//최종수정일시
	private Long lastMdfcnId;					//최종수정아이디
	private String lastMdfcnUserName;			//최종수정자 이름

	private int attachedFileCnt;				//첨부파일갯수
	private Long atchFileSz; 					// 파일 사이즈


	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("sysNoticeSeller")
				.toString();
	}

	//첨부파일 리스트
	private List<SysNoticeSellerFileDto> sysNoticeSellerFileList;


}
