package saleson.shop.give.giveoperation.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CtbnyOpratnFile {
	
	private Long registFileId;
	private Long registSn;
	private String fileNm;
	private String orginlFileNm;
	private String fileTy;
	private int sortOrdr;
	private Long frstRegisterId;
	private String frstRegistPnttm;
	private Long lastUpdusrId;
	private String lastUpdtPnttm;
	
	
}
