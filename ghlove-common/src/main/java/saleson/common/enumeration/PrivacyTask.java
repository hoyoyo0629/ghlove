package saleson.common.enumeration;

import saleson.common.enumeration.mapper.CodeMapperType;

public enum PrivacyTask implements CodeMapperType {
    LIST("list", "목록 열람", "목록 데이터 조회"),
    VIEW("view", "열람", "상세 데이터 조회"),
    UPDATE("update", "수정", "데이터 수정"),
    DELETE("delete", "삭제","데이터 삭제"),
    EXCEL_DOWNLOAD("excel-download", "엑셀 다운로드", "데이터 엑셀 다운로드");

    PrivacyTask(String code, String title, String description) {
        this.code = code;
        this.title = title;
        this.description = description;
    }

    private String code;
    private String title;
    private String description;

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