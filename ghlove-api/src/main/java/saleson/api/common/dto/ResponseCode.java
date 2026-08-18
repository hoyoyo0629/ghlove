package saleson.api.common.dto;

import lombok.Getter;

public enum ResponseCode {
    SUCCESS("API요청 성공"),
	FAIL("API 요청 실패")
	;

    @Getter
    private final String message;

    ResponseCode(String message) {
        this.message = message;
    }
}
