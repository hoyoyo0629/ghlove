package saleson.shop.representativebanner.support;

@SuppressWarnings("serial")
public class RepresentativeBannerManagerException extends RuntimeException {
	private static String BASE_REDIRECT_URL = "/opmanager/representative-banner/list";
	private String redirectUrl;
	
	public RepresentativeBannerManagerException() {
		super("잘못된 접근입니다.");
		setRedirectUrl(BASE_REDIRECT_URL);
	}
	
	public RepresentativeBannerManagerException(String message) {
		super(message);
		setRedirectUrl(BASE_REDIRECT_URL);
	}
	public RepresentativeBannerManagerException(String message, Throwable e) {
		super(message, e);
		setRedirectUrl(BASE_REDIRECT_URL);
	}
	
	public RepresentativeBannerManagerException(String message, String redirectUrl) {
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
