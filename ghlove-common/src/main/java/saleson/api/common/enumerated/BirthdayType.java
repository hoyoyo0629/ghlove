package saleson.api.common.enumerated;

import lombok.Getter;

public enum BirthdayType {
	SOLAR_CALENDAR("1", "양력")
	,LUNARCALENDAR("2", "음력")
	;

	@Getter
    private final String code;

    @Getter
    private final String codeName;

    BirthdayType(String code, String codeName) {
        this.code = code;
        this.codeName = codeName;
    }
}
