package saleson.shop.community.doamin;

import java.io.File;
import java.util.Date;

import com.onlinepowers.framework.util.ValidationUtils;

import lombok.Getter;
import lombok.Setter;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;

@Getter
@Setter
public class CmntyFileDto{

	private long fileId;
	private long rpstrId;
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
		sb.append("data-board");
		sb.append(File.separator);
		sb.append(this.atchFileNm);
		return sb.toString();
	}
}
