package saleson.common.enumeration;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum IdType implements CodeMapperType {

    SELLER("SELLER","답례품업체","답례품업체"),
    SELLER_USER("SELLER_USER","답례품관리자","답례품관리자"),
    MANAGER("MANAGER","관리자","관리자")
    ;
	

    private String code;
    private String title;
    private String description;

    IdType(String code, String title, String description) {
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
