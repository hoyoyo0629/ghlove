package saleson.common.alimtalk;

public class AlimtalkResponse {
	private boolean success;
	private String code;
	private String message;
	private Alimtalk data;

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Alimtalk getData() {
		return data;
	}
	public void setData(Alimtalk data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "AlimtalkResponse [success=" + success + ", code=" + code + ", message=" + message + ", data=" + data
				+ "]";
	}

}
