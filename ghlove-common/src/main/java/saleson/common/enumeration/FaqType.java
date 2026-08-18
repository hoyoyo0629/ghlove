package saleson.common.enumeration;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum FaqType implements CodeMapperType {

	F_LOGIN("회원가입/로그인", "회원가입/로그인", true),
    F_CNTR_SYSTEM("기부하기", "기부하기", true),
    F_CNTR_POINT("기부포인트", "기부포인트", true),
    F_OFF_CNTR("오프라인기부", "오프라인기부", true),
    F_CNTR_DESIGNATED("특정사업기부", "특정사업기부", true),
    F_API_PLATFORM("세액공제", "세액공제", true),
    F_PRESENT_PURC("답례품", "답례품", true),
    F_ORDER("주문/배송", "주문/배송", true),
    F_OPEN("민간플랫폼", "민간플랫폼", true),
    F_SYSTEM("시스템", "시스템", true),
    F_ETC("기타", "기타", true);

    private String title;
    private String description;
    private Boolean enabled;

    FaqType(String title, String description, Boolean enabled) {
        this.title = title;
        this.description = description;
        this.enabled = enabled;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

	@Override
	public Boolean isEnabled() {
		return enabled;
	}
}
