package saleson.shop.email.domain;

import lombok.Data;

@Data
public class EmailSend {
	
	private String email;
	private String userName;
	private String resultCode;
	private String processedDate;
	
	public String getResultNm() {
		if (this.resultCode == null || "".equals(this.resultCode)) {
			return null;
		}
		
		String code = "";
		
		if ("C".equals(this.resultCode)) {
			code = "성공";
		} else if ("F".equals(this.resultCode)) {
			code = "실패";
		} else {
			code = this.resultCode;
		}
		
		return code;
		
	}
	
}
