package saleson.common.enumeration.kakao.alimtalk;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum TemplateEmphasizeType implements CodeMapperType {

    NONE("NONE","선택안함","선택안함"),
    TEXT("TEXT","강조표기형","강조표기형"),
    IMAGE("IMAGE","이미지형","이미지형");

    private String code;
    private String title;
    private String description;

    TemplateEmphasizeType(String code, String title, String description) {
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
    public String getDescription() {
        return description;
    }

    @Override
    public Boolean isEnabled() {
        return true;
    }
}
