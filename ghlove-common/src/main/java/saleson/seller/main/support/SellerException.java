package saleson.seller.main.support;

@SuppressWarnings("serial")
public class SellerException extends RuntimeException {
	private static String BASE_REDIRECT_URL = "/opmanager/seller/list";
	private String redirectUrl;
	
	public SellerException() {
		super("잘못된 접근입니다.");
		setRedirectUrl(BASE_REDIRECT_URL);
	}
	
	public SellerException(String message) {
		super(message);
		setRedirectUrl(BASE_REDIRECT_URL);
	}
	public SellerException(String message, Throwable e) {
		super(message, e);
		setRedirectUrl(BASE_REDIRECT_URL);
	}
	
	public SellerException(String message, String redirectUrl) {
		super(message);
		setRedirectUrl(redirectUrl);
	}
	
	public String getRedirectUrl() {
		return this.redirectUrl;
	}
	
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}
