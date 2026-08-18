package saleson.common.enumeration;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum CancelClaimStatus implements CodeMapperType {

    CANCEL_REQUEST("01","취소신청","취소신청"),
    CANCEL_HOLD("02","취소보류","취소보류"),
    CANCEL_APPROVAL("03","취소승인","취소승인"),
    CANCEL_COMPLETE("04","취소완료","취소완료"),
    CANCEL_REFUSAL_SHIPPING("98","취소거절_배송처리","취소거절_배송처리"),
    CANCEL_REFUSAL("99","취소거절","취소거절")
    ;

    private String code;
    private String title;
    private String description;

    CancelClaimStatus(String code, String title, String description) {
        this.code = code;
        this.title = title;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getTitle() {
        return title;
    }

	@Override
	public Boolean isEnabled() {
		return true;
	}

	@Override
	public String getDescription() {
		return description;
	}


}
