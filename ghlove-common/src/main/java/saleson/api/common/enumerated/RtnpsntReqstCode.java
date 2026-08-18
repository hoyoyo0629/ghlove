package saleson.api.common.enumerated;

import lombok.Getter;

public enum RtnpsntReqstCode {
	PROVIDED("100", "답례품 제공 받음")
	,CARRY_OVER("200", "이월")
	,NOT_PROVIDED("300","답례품 제공 받지 않음")
	;

	@Getter
    private final String code;

    @Getter
    private final String codeName;

    RtnpsntReqstCode(String code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }
}
