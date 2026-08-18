package saleson.common.enumeration.kakao.alimtalk;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum TemplateMessageType implements CodeMapperType {

    BA("BA","기본형","기본형"),
    EX("EX","부가 정보형","부가 정보형"),
    AD("AD","광고 추가형","광고 추가형"),
    MI("MI","복합형","복합형");

    private String code;
    private String title;
    private String description;

    TemplateMessageType(String code, String title, String description) {
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
