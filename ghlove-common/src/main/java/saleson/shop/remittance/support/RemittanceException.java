package saleson.shop.remittance.support;

@SuppressWarnings("serial")
public class RemittanceException extends RuntimeException {

	private String redirectUrl;
	
	private String returnMsg;
	
	public RemittanceException(String url) {
		super("잘못된 접근입니다.");
		setRedirectUrl(url);
		returnMsg = "잘못된 접근입니다.";
	}

	public RemittanceException(String url, String message) {
		super(message);
		setRedirectUrl(url);
		returnMsg = message;
	}
	
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getReturnMsg() {
		return returnMsg;
	}
	
}
