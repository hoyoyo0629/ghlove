package saleson.shop.manual.domain;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.util.ValidationUtils;

import lombok.Getter;
import lombok.Setter;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;
import saleson.shop.databoard.domain.DataboardFile;

@Getter
@Setter
public class Manual {

	private Integer mnlSn;
	private String menuSeCode;
	private String menuUrl;
	private String menuNm;
	private String fileNm;
	private String orginlFileNm;
	private String fileTy;
	private String inqireCo;
	private String menuSj;
	private String menuCn;

	private long frstRegisterId;
	private String userName;
	private String frstRegistPnttm;
	private long lastUpdusrId;
	private String lastUpdtPnttm;

	private String pageGbn;

	// 관리자
	private String menuId;


	private MultipartFile[] itemDetailImageFiles;

	public String getFileSrc() {
		if (ValidationUtils.isEmpty(this.fileNm)) {
			return ShopUtils.getNoImagePath();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(SalesonProperty.getUploadSaveFolder());
		sb.append(File.separator);
		sb.append("help");
		sb.append(File.separator);
		sb.append(this.fileNm);
		return sb.toString();
	}

	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("help")
				.toString();
	}
}
