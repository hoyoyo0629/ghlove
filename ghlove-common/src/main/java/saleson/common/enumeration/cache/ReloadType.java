package saleson.common.enumeration.cache;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum ReloadType implements CodeMapperType {

    COMMON_CODE("공통코드","common-code"),
    COMMON_MESSAGE("공통메세지","common-message"),
    SHOP_CONFIG("상점설정","shop-config"),
    FRONT_CATEGORIES("Front 카테고리","front-categories"),
    FIXED_META_DATA("메타 데이터","fixed-meta-data");

    private String title;
    private String code;

    ReloadType(String title, String code) {
        this.title = title;
        this.code = code;
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
        return title;
    }

    @Override
    public Boolean isEnabled() {
        return true;
    }

}
