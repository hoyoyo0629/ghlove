package saleson.api.common.enumerated;

import org.springframework.http.HttpStatus;

public enum ApiError {

    NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 API 입니다.", "API 대상에 오타가 없는지 확인해 보세요."),
    UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", "토큰 인증정보가 올바르지 않습니다."),
    UNAUTHORIZED_SMS(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", "SMS 인증이 필요합니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.", "인증 API를 통해 AccessToken을 발급받은 후 다시 요청해 보세요."),
    UNAUTHORIZED_LOCK(HttpStatus.UNAUTHORIZED, "회원이 잠긴 상태 입니다.", "회원이 잠긴 상태 입니다."),
    INCORRECT_PASSWORD_LOCK(HttpStatus.UNAUTHORIZED, "5회 이상 로그인에 실패하여\n계정이 잠겼습니다.\n비밀번호 찾기를 통해 해제해 주세요", "회원이 잠긴 상태 입니다."),
    NOT_VALID_USER(HttpStatus.UNAUTHORIZED, "회원이 존재하지 않습니다.", "회원 정보가 존재 하지 않습니다."),
    NOT_VALID_LOGIN(HttpStatus.UNAUTHORIZED, "로그인 정보 불일치", "아이디/비밀번호가 일치하지 않습니다."),
    DUPLICATION_LOGIN_ID(HttpStatus.CONFLICT, "로그인 아이디 중복 Error", "이미 존재하는 아이디입니다."),
    DUPLICATION_SMS_INFO(HttpStatus.CONFLICT, "SMS 가입정보 중복 Error", "이미 가입되어있습니다."),
    //SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "System Error (시스템 에러)", "서버 내부 에러가 발생하였습니다. 신속히 조치하겠습니다."),
    //SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "자동납부결과 확인중입니다", "잠시만 기다려 주세요."),
	SYSTEM_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생하였습니다. 신속히 조치하겠습니다.", "잠시만 기다려 주세요."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용하지 않는 Method 입니다.", "API에 맞는 Method를 확인 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.", "API 요청에 오류가 있습니다. 요청 URL, 필수 요청 변수가 정확한지 확인 바랍니다."),
    BAD_REQUEST_DISPLAY(HttpStatus.BAD_REQUEST, "부적절한 display 값입니다.", "display 요청 변수값이 허용 범위(1~100)인지 확인해 보세요."),
    BAD_REQUEST_START_VALUE(HttpStatus.BAD_REQUEST, "부적절한 start 값입니다.", "start 요청 변수값이 허용 범위(1~1000)인지 확인해 보세요."),
    BAD_REQUEST_SORT_VALUE(HttpStatus.BAD_REQUEST, "부적절한 sort 값입니다.", "sort 요청 변수 값에 오타가 없는지 확인해 보세요."),
    BAD_REQUEST_NO_ITEM(HttpStatus.BAD_REQUEST, "답례품정보가 없거나 \n승인처리가 되지 않았습니다.", "답례품ID를 확인해주세요."),
	BAD_REQUEST_NO_OFFLINE(HttpStatus.BAD_REQUEST, "오프라인 담당자는 사용 할 수 없는 기능입니다.", "오프라인 담당자는 사용 할 수 없는 기능입니다."),
    BAD_REQUEST_NO_ITEM_OPTION(HttpStatus.BAD_REQUEST, "답례품의 옵션정보가 변경되었습니다.", "옵션정보를 확인해주세요."),
    BAD_REQUEST_FAIL_DOWNLOAD_COUPON(HttpStatus.BAD_REQUEST, "쿠폰이 다운되지 않았습니다.", "쿠폰ID를 확인해주세요."),
    BAD_REQUEST_NOT_CONFIRM_STATUS(HttpStatus.BAD_REQUEST, "답례품 구매확정을 할 수 있는 상태가 아닙니다.", "주문상태를 확인해주세요"),
    BAD_REQUEST_INQUIRY_FAIL_USER(HttpStatus.BAD_REQUEST, "비밀글입니다.", "비밀글입니다."),
    BAD_REQUEST_INQUIRY_DELETE_FAIL(HttpStatus.BAD_REQUEST, "답변완료 된 문의는 삭제하실 수 없습니다.", "문의를 삭제 할 수 없습니다."),
    BAD_REQUEST_INQUIRY_DELETE_FAIL_USER(HttpStatus.BAD_REQUEST, "문의를 작성한 회원만 삭제 할 수 있습니다.", "문의를 작성한 회원이 아니므로 삭제 할 수 없습니다."),
    BAD_REQUEST_ITEM_REVIEW_WRITING_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "구매확정한 답례품에만 리뷰를 작성하실 수 있습니다", "답례품을 구매하지 않았거나, 구매확정된 주문이 아닙니다."),
    BAD_REQUEST_CART_BUY_MAX_FAIL(HttpStatus.BAD_REQUEST, "장바구니에 담긴 수량이 최대 구매 가능 수량을 초과하였습니다.", "수량을 확인해주세요."),
    BAD_REQUEST_CART_BUY_LIMIT_FAIL(HttpStatus.BAD_REQUEST, "장바구니에 담긴 수량은 999개를 초과할 수 없습니다.", "수량을 확인해주세요."),
    BAD_REQUEST_CART_BUY_MIN_FAIL(HttpStatus.BAD_REQUEST, "장바구니에 담긴 수량이 최소 구매 가능 수량을 미달하였습니다.", "수량을 확인해주세요."),
    BAD_REQUEST_NO_FEATURED(HttpStatus.BAD_REQUEST, "기획전이 존재하지 않습니다.", "기획전 ID/공개 여부를 확인해주세요."),
    BAD_REQUEST_FEATURED_FINISH(HttpStatus.BAD_REQUEST, "해당 기획전은 종료되었습니다.", "기획전 기간/공개 여부를 확인해주세요."),
    BAD_REQUEST_FAIL_PASSWD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", "비밀번호가 일치하지 않습니다."),
    BAD_REQUEST_NOT_ALLOWED_WORD(HttpStatus.BAD_REQUEST, "금지어를 확인해주세요.", "내용에 금지어가 포함되어 있습니다."),
    BAD_REQUEST_NO_PAYMENT(HttpStatus.BAD_REQUEST, "설정된 결제 방법이 없습니다.", "고객센터로 문의 부탁 드립니다."),
    BAD_REQUEST_NO_SHIPPING(HttpStatus.BAD_REQUEST, "배송지 필수정보 미입력", "배송지 정보를 입력해주세요."),
    BAD_REQUEST_PAY_MIN_FAIL(HttpStatus.BAD_REQUEST, "실시간 계좌이체 결제 요청 실패 : 금액오류(200원 이하 이체불가)", "최소 결제 금액을 확인해주세요."),
    BAD_REQUEST_BUY_MIN_FAIL(HttpStatus.BAD_REQUEST, "최소 결제가능 금액보다 작습니다.", "최소 결제가능 금액을 확인해주세요."),
    BAD_BUY_NO_ITEM(HttpStatus.BAD_REQUEST, "결제가능 답례품이 없습니다.", "결제가능 답례품이 없습니다."),
    BAD_REQUEST_OUT_STOCK(HttpStatus.BAD_REQUEST, "답례품의 재고가 없습니다.", "답례품의 재고가 없습니다."),
    FAIL_CALL_NPAY(HttpStatus.BAD_REQUEST, "네이버 페이 실행에 실패 했습니다.", "네이버 페이 실행에 실패 했습니다."),
    FAIL_GOOGLE_ANALYTICS(HttpStatus.BAD_REQUEST, "Google Analytics 처리에 실패 했습니다.", "Google Analytics 처리에 실패 했습니다."),
    FAIL_GOOGLE_ANALYTICS_COMMON_TRACKING(HttpStatus.BAD_REQUEST, "Google Analytics 공통 처리에 실패 했습니다.", "Google Analytics 공통 처리에 실패 했습니다."),
    DUPLICATION_CI(HttpStatus.BAD_REQUEST, "기존 가입 정보가 있습니다.", "기존 가입 정보가 있습니다."),
    DUPLICATION_CI_JOIN_USER(HttpStatus.BAD_REQUEST, "기존 가입정보가 있습니다. 아이디 찾기를 진행해 주세요.", "기존 가입정보가 있습니다. 아이디 찾기를 진행해 주세요."),
    NOT_JOIN_USER(HttpStatus.BAD_REQUEST, "입력한 값과 일치하는 회원에 대한 정보가 없습니다.", "입력한 값과 일치하는 회원에 대한 정보가 없습니다."),
	NOT_EXIST_AUTH(HttpStatus.BAD_REQUEST, "본인인증 정보가 없습니다.", "본인인증 정보가 없습니다."),
	RESTRICT_ITEM(HttpStatus.BAD_REQUEST, "서버 과부하 방지를 위해 접속 제한 중입니다.", "서버 과부하 방지를 위해 접속 제한 중입니다."),
	KAKAO_LINK_NO_BIRTH(HttpStatus.BAD_REQUEST, "카카오 계정 정보에 생년월일 정보가 없습니다.", "카카오 계정 정보에 생년월일 정보가 없습니다."),
	KAKAO_LINK_NO_NAME(HttpStatus.BAD_REQUEST, "카카오 계정 정보에 이름 정보가 없습니다.", "카카오 계정 정보에 이름 정보가 없습니다."),
	KAKAO_LINK_NO_EMAIL(HttpStatus.BAD_REQUEST, "카카오 계정 정보에 이메일 정보가 없습니다.", "카카오 계정 정보에 이메일 정보가 없습니다."),
	KAKAO_LINK_NO_PHONE(HttpStatus.BAD_REQUEST, "카카오 계정 정보에 전화번호 정보가 없습니다.", "카카오 계정 정보에 전화번호 정보가 없습니다."),
	KAKAO_LINK_NO_CI(HttpStatus.BAD_REQUEST, "카카오 계정 정보에 개인식별 정보가 없습니다.", "카카오 계정 정보에 개인식별 정보가 없습니다."),
	NO_LOGIN(HttpStatus.BAD_REQUEST, "로그인 상태가 아닙니다.", "로그인 후 진행해주세요."),
	DESIGNATED_DONATION_WRONG_DATE(HttpStatus.BAD_REQUEST, "유효하지 않은 날짜입니다. 다시 한번 확인해주세요.", "유효하지 않은 날짜입니다. 다시 한번 확인해주세요."),
	DESIGNATED_DONATION_WRONG_AMT(HttpStatus.BAD_REQUEST, "목표금액을 확인해주세요.(1 ~ 999999999999)", "목표금액을 확인해주세요.(1 ~ 999999999999)"),
	END_FEATURED(HttpStatus.BAD_REQUEST, "해당 기획전은 종료되었습니다.", "해당 기획전은 종료되었습니다."),
	OFF_ACCESS_FRONT(HttpStatus.BAD_REQUEST, "오프라인 담당자 계정은 사용자 페이지에 접근할 수 없습니다.", "오프라인 담당자 계정은 사용자 페이지에 접근할 수 없습니다."),
	;

    private HttpStatus httpStatus;
    private String message;
    private String description;

    ApiError(HttpStatus httpStatus, String message, String description) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.description = description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
