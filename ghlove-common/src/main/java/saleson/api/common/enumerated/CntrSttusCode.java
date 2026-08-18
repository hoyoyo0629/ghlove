package saleson.api.common.enumerated;

import lombok.Getter;

public enum CntrSttusCode {
	DECLARATION("100", "신고")
	,PAY("200", "수납")
	,OVERPAYMENT("300","과오납")
	;

	@Getter
    private final String code;

    @Getter
    private final String codeName;

    CntrSttusCode(String code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }
}
