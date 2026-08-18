package saleson.shop.email.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailFile {
	private int emailFileId;
	private long emailId;
	private String fileName;
	private String fileTy;
	private int ordering;
	private String createdDate;
	private String orgFileName;
}
