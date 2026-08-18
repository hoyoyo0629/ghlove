package saleson.shop.notice.domain;

import java.io.File;
import java.util.Date;

import com.onlinepowers.framework.util.ValidationUtils;

import lombok.Getter;
import lombok.Setter;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;

@Getter
@Setter
public class SysNoticeSellerFileDto {

	private long fileId;
	private int noticeId;
	private String orgnlAtchFileNm;
	private String atchFileNm;
	private String atchFileExtnNm;
	private long atchFileSz;
	private Integer atchFileSeq;
	private String atchFilePathNm;
	private String useYn;
	private long frstCrtId;
	private Date frstCrtDt;
	private long lastMdfcnId;
	private Date lastMdfcnDt;



	// 첨부파일 경로
	public String getFileSrc() {
		if (ValidationUtils.isEmpty(this.atchFileNm)) {
			return ShopUtils.getNoImagePath();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(SalesonProperty.getUploadSaveFolder());
		sb.append(File.separator);
		sb.append("sysNoticeSeller");
		sb.append(File.separator);
		sb.append(this.atchFileNm);
		return sb.toString();
	}

}
