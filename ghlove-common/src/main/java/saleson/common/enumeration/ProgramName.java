package saleson.common.enumeration;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum ProgramName implements CodeMapperType {

    PRJ_NOTICE("prjNotice", "특정사업기부 공지사항", true),
//    HNR_USER_MNG("lclgvHnrUserMng", "명예시민 등록 이미지", true),
    ;

    private String title;
    private String description;
    private Boolean enabled;

    ProgramName(String title, String description, Boolean enabled) {
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
	
	public String getProgramPath() {
		return title;
	}
}
