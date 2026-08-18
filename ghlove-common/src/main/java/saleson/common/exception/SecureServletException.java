package saleson.common.exception;

import com.onlinepowers.framework.exception.OpRuntimeException;

public class SecureServletException extends OpRuntimeException{

    public SecureServletException() {
        super("Https 요청이 아닙니다.");
    }

    public SecureServletException(String message) {
        super(message);
    }

}
