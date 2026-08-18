package saleson.api.common.enumerated;

import lombok.Getter;

public enum DonationError {
		USER_INFORMATION_NOT_EXIST("사용자 정보가 존재하지 않습니다")
	,	DONATION_INFORMATION_DOES_NOT_EXIST("기부 정보가 존재 하지 않습니다")
    ,	UNKNOWN_ERROR("알 수 없는 에러가 발생했습니다.")
    ,	SYSTEM_ERROR("시스템 에러가 발생하였습니다.")
    ,	UNSUPPORTED_ENCODING_EXCEPTION("UNSUPPORTED_ENCODING_EXCEPTION")
    ,	TAX_AMOUNT_SETTING_ERROR("세액 설정 오류")
    ,	LOCGOV_INFORMATION_NOT_EXIST("지자체 정보가 존재하지 않습니다")
    ,	INSTT_INFORMATION_NOT_EXIST("행정기관 정보가 존재하지 않습니다")
    ,	PROBLEMS_JUMIN_NUMBER_KAKAO_BIRTHDAY("주민번호가 잘못 되어 카카오 생년월일 수정에 문제가 발생하였습니다.")
    ,	MOPAS_BODY_NOT_EXIST("미래행공 연계에 응답 body 데이터가 존재하지 않습니다.")
    ,	ADM_CODE_NOT_EXIST("법정동 코드가 존재하지 않습니다.")
    ,	BASS_ADDRESS_NOT_EXIST("bass address 가 존재하지 않습니다.")
    ,	FOREIGNER_EXPIRED("체류만료일이 지나 기부가 불가능합니다.")
    ,	FOREIGN_RESULT_CODE_INFORMATION_NOT_EXIST("외국인 행공센 조회 결과 코드가 올바르지 않습니다.")
    ,	RESPONSE_CODE_NOT_EXIST("응답코드가 올바르지 않습니다.")
    ,	RESPONSE_NOT_EXIST("응답코드가 올바르지 않습니다.")
    ,	CANNOT_DONATE_TO_YOUR_LOCAL_GOVERNMENT("자신의 거소지 지자체에는 기부를 하실 수 없습니다.")
    ,	FOREIGN_STATUS_CODE_NOT_EXIST("외국인 구분 상태코드가 올바르지 않습니다.")
	,	CNTR_LMTT_INFORMATION_NOT_EXIST("기부제한 정보가 존재하지 않습니다")
	,	DESIGNATED_INFO_ERROR("특정사업기부 사업 또는 지자체 정보가 올바르지 않습니다.")
    ,	LOGIN_INFO_NOT_EXIST("사용자 로그인 정보가 존재하지 않습니다.")
    ;


	@Getter
	private String message;

    DonationError(String message) {
    	this.message = message;
	}
}
