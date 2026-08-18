package saleson.shop.mailconfig.domain;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MailConfig implements Cloneable {
	
	private int mailConfigId;
	private String smsConfig;
	private String templateId;
	private String title;
	private String buyerSubject;
	private String adminSubject;
	private String sellerSubject;
	private String buyerContent;
	private String adminContent;
	private String buyerContentMasking;
	private String adminContentMasking;
	private String buyerSubjectMasking;
	private String adminSubjectMasking;
	private String sellerContent;
	private String buyerSendFlag;
	private String adminSendFlag;
	private String sellerSendFlag;
	private String buyerTagUse;
	private String adminTagUse;
	private String sellerTagUse;
	private String createdDate;
	
	private String mobileBuyerSubject;
	private String mobileAdminSubject;
	private String mobileSellerSubject;
	private String mobileBuyerContent;
	private String mobileAdminContent;
	private String mobileSellerContent;

	private String bcc;
	private String adminEmail;
	
	private String content;

	public String getBuyerSendFlagText(){
		String useText = "발송";
		if (buyerSendFlag != null) {
			if( buyerSendFlag.equals("N") ){
				useText = "발송안함";
			}
		}
		
		return useText;
	}
	
	public String getAdminSendFlagText(){
		String useText = "발송";
		if (adminSendFlag != null) {
			if( adminSendFlag.equals("N") ){
				useText = "발송안함";
			}
		}
		return useText;
	}
	
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
