package saleson.shop.log.support;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum ChangeTypeEnum implements CodeMapperType {

    CREATE("CREATE", "생성", "생성"),
    UPDATE("UPDATE", "수정", "수정"),
    DELETE("DELETE", "삭제", "삭제");

    private String code;
    private String title;
    private String description;

    ChangeTypeEnum(String code, String title, String description) {
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
