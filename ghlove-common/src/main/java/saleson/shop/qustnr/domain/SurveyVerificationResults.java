package saleson.shop.qustnr.domain;

public enum SurveyVerificationResults {
	FAIL_INCORRECT_APPROACH("100", "올바르지 않은 접근"),
	FAIL_NOT_EXIST("101", "존재하지 않은 설문조사."),
	FAIL_AlREADY_DONE("102", "이미 진행한 설문"),

	SUCCESS("200", "설문 조사 진행 가능")
	;

	private final String code;
	private final String msg;

	SurveyVerificationResults(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}


}
