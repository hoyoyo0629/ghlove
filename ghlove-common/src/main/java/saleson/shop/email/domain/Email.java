package saleson.shop.email.domain;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import saleson.common.configuration.SalesonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Email {

	private long emailId;
	private String subject;
	private String content;
	private String sendDate;
	private String status;
	private String statusName;
	private String userName;
	private Long frstRegisterId;
	private String frstRegistPnttm;
	private Long lastUpdusrId;
	private String lastUpdtPnttm;

	private String sendType;
	private String authTarget;
	private List<EmailDetail> authList;
	private List<EmailFile> fileList;

	private MultipartFile files;

	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("email")
				.toString();
	}

	public boolean isEmailSend() {
		if ("C".equals(this.status) || "P".equals(this.status) || "T".equals(this.status)) {
			return true;
		} else {
			return false;
		}
	}

}
