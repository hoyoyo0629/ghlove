package saleson.shop.give.giveoperation.domain;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import saleson.common.configuration.SalesonProperty;

@Data
public class CtbnyOpratn {
	
	private Long registSn;					// 사용채번
	private String locgovCode;				// 지자체 코드
	private String bsnsPurpsCode;			// 사용목적코드
	private String bsnsPurpsNm;				// 사용목적
	private String expndtrAmt;				// 사용 금액
	private String bsnsNm;					// 사업명
	private String bsnsCn;					// 내용
	private int fileCnt;					// 파일 갯수
	private String loginId;					// 등록자 ID
	private String userName;				// 등록자명
	private Long frstRegisterId;			// 등록자 userID
	private String expndtrDe; 				// 사용일자
	private Long lastUpdusrId;				// 최종수정자
	private String rm;						// 비고
	
	private MultipartFile[] operationFiles;	// 증빙서류 파일
	
	private List<CtbnyOpratnFile> fileList;
	
	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("operation")
				.toString();
	}
	
	
}
