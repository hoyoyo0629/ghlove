package saleson.common.exception;

public class DuplicationLoginException extends RuntimeException {
	
	public DuplicationLoginException() {
		super("다른 기기에서 로그인하여 자동으로 로그아웃 되었습니다.");
	}
	
	public DuplicationLoginException(String message) {
		super(message);
	}

}
