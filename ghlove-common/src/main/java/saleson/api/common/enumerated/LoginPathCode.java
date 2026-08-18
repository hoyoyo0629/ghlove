package saleson.api.common.enumerated;

import lombok.Getter;

public enum LoginPathCode {
	ID_PASSWORD("100", "아이디/패스워드")
	,SIMPLE_AUTHENTICATION("200", "간편인증")
	,ONEPASS("300","디지털원패스")
	,YESKEY("400","금융인증서")
	,KAKAO("500","카카오")
	;

	@Getter
    private final String code;

    @Getter
    private final String codeName;

    LoginPathCode(String code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }
}
