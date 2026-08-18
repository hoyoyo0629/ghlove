package saleson.common.sms.domain;

import java.util.regex.Pattern;

import org.apache.commons.math3.exception.OutOfRangeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.StringUtils;

import lombok.Data;
import saleson.common.enumeration.SmsType;
import saleson.shop.donation.NgDonationService;

@Data
public class TifIpsSndngMDisplay {

	private Logger log = LoggerFactory.getLogger(TifIpsSndngMDisplay.class);

    private String insttCrtSn;					// 기관생성일련번호 ex)1234
    private String svcId;						// 서비스아이디 (별도전달)
    private String sndngCntnts;					// 발송내용 (가변정보를 순서에 따라 입력. | 으로 구분) ex)xxx|2021년 12월 11일|통지서|사용자명|날짜|문서명

    private String esbInitTime;					// 연계발생일시 ex)2021-12-15 00:00:00
    private String esbTxTime;					// 연계시작일시
    private String esbComptTime;				// 연계완료일시
    private String esbStatusCd;					// 연계처리상태코드 (S:성공, F:실패)
    private String esbErrMsg;					// 연계오류메세지

    private String limitAmtString;				// 연 최대 기부 한도금액, this.limitAmtString 사용

    public String getSvcName() {
    	SmsType smsType = getSmsType();
    	if (smsType == null) {
    		return "";
    	} else {
        	return smsType.getDescription();
    	}
    }

    public SmsType getSmsType() {
    	SmsType result = null;
    	SmsType[] smsTypes = SmsType.values();
    	for (SmsType smsType : smsTypes) {
			if (smsType.getCode().equals(svcId)) {
				result = smsType;
			}
		}
    	return result;
    }

    public String getSmsContent() {
    	if (StringUtils.hasLength(sndngCntnts)) {
        	String[] params = sndngCntnts.split("\\|");
        	StringBuffer sb = new StringBuffer();

        	try {
            	switch (getSmsType()) {
    				case JOIN_MEMBERSHIP:
    					sb.append("[고향사랑e음 회원가입 안내]\n");
    					sb.append("	 - [" + params[0] + "]님 회원가입을 진심으로 축하드립니다. 감사합니다.\n");
    					sb.append("	 ∙ 고향사랑 기부제란? 개인이 거주지 이외 지자체(기초, 광역)에 기부하면\n 세액공제 혜택과 답례품을 제공하는 제도\n");
    					sb.append("	 ∙ 기부자 혜택 (세액공제) 10만원까지는 전액, 10만원 초과금액은 16.5%,\n(답례품) 기부금의 30%내 답례품 제공\n");
    					sb.append("	 ∙ 기부처 및 기부한도 (기부처) 주민등록상 거주지 제외 지자체 기부 가능,\n(기부한도) 1인당 연간 최대 "+ this.limitAmtString +"원까지 가능\n\n");
    					sb.append("【고향사랑e음 바로가기】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case DONATION:
    					sb.append("[고향사랑e음 기부 감사 인사]\n");
    					sb.append("	 -[" + params[0] + "]님 " + params[1] + "에 보내주신 사랑에 진심으로 감사드립니다.\n");
    					sb.append("	   " + params[0] + "님의 고향을 사랑하는 마음을 간직하고 고향의 좋은 변화를 만들어 가겠습니다.\n");
    					sb.append("	 ∙ 현재까지 기부하신 총 금액 : " + params[2] + "원\n");
    					sb.append("	 ◈ 지자체 답례품을 준비하였습니다.\n");
    					sb.append("	   ★ 지역에서 생산ㆍ제조한 농ㆍ수ㆍ축ㆍ임산물ㆍ제조품 등\n");
    					sb.append("    ★ 지역에서 통용되는 상품권\n");
    					sb.append("    ★ 지역의 경제활성화에 필요하다고 인정된 관광ㆍ숙박 등의 서비스 상품\n\n");
    					sb.append("【 답례품 신청하러 가기 】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case TAX_CREDIT:
    					sb.append("[고향사랑e음 기부실적 및 예상 세액공제 알림]\n");
    					sb.append("  - [" + params[0] + "]님의 " + params[1] + "년 기부실적 및 세액공제입니다.\n\n");
    					sb.append("  ∙ 총 기부액 : " + params[2] + "원\n");
    					sb.append("  ∙ 예상 세액공제액 : " + params[3] + "원\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case PRESENT_ORDER:
    					sb.append("[고향사랑e음 답례품 주문 알림]\n");
    					sb.append("  - [" + params[0] + "]님의 답례품 주문이 정상적으로 완료되었습니다.\n\n");
    					sb.append("  ∙ 신청일시 : " + params[1] + "\n");
    					sb.append("  ∙ 주문번호 : " + params[2] + "\n");
    					sb.append("  ∙ 상품명 : " + params[3] + "\n\n");
    					sb.append("【 주문내역 확인 】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case PRESENT_SHIPPING:
    					sb.append("[고향사랑e음 답례품 배송 알림]\n");
    					sb.append("  - [" + params[0] + "]님이 주문하신 답례품이 발송되었습니다.\n\n");
    					sb.append("  ∙ 신청일시 : " + params[1] + "\n");
    					sb.append("  ∙ 주문번호 : " + params[2] + "\n");
    					sb.append("  ∙ 상품명 : " + params[3] + "\n");
    					sb.append("  ∙ 배송사 : " + params[4] + "\n\n");
    					sb.append("【 주문내역 확인 】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case ILOVEGOHYANG_DAY:
    					sb.append("[고향사랑e음 고향사랑의 날 안내]\n");
    					sb.append("  - [" + params[0] + "]님 9월 4일은 고향사랑의 날입니다.\n");
    					sb.append("    고향사랑의 날에는 기념식과 박람회가 개최될 예정이니 많은 참여 바랍니다.\n\n");
    					sb.append("  ∙ 일시/장소 : " + params[1] + "\n");
    					sb.append("  ∙ 주요행사 : " + params[2] + "\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case OVERPAYMENT:
    					sb.append("[고향사랑e음 과오납 처리 알림]\n");
    					sb.append("  - [" + params[0] + "]님 " + params[1] + "에 기부하신 금액이 과오납 처리되었습니다.\n\n");
    					sb.append("  ∙ 과오납금액 : " + params[2] + "원\n\n");
    					sb.append("  ∙ 기부처 및 기부한도 (기부처) 주민등록상 거주지 제외 지자체 기부 가능,\n");
    					sb.append("    (기부한도) 1인당 연간 최대 "+ this.limitAmtString +"원까지 가능\n\n");
    					sb.append("【 기부하러 가기 】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case OVERPAYMENT_CANCEL:
    					sb.append("[고향사랑e음 과오납 취소 알림]\n");
    					sb.append("  - [" + params[0] + "]님 " + params[1] + "에 기부하신 금액이 과오납 취소 요청이 처리되었습니다.\n\n");
    					sb.append("  ∙ 과오납 취소 금액 : " + params[2] + "원\n\n");
    					sb.append("  ∙ 기부처 및 기부한도 (기부처) 주민등록상 거주지 제외 지자체 기부 가능, (기부한도) 1인당 연간 최대 "+ this.limitAmtString +"원까지 가능\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case PRESENT_CANCEL_REGISTER:
    					sb.append("[고향사랑e음 답례품 주문 취소 접수 알림]\n");
    					sb.append("  - [" + params[0] + "]님이 주문하신 답례품 취소가 접수되었습니다.\n\n");
    					sb.append("  ∙ 신청일시 : " + params[1] + "\n");
    					sb.append("  ∙ 주문번호 : " + params[2] + "\n");
    					sb.append("  ∙ 상품명 : " + params[3] + "\n");
    					sb.append("  ∙ 사유 : " + params[4] + "\n\n");
    					sb.append("【 주문내역 확인 】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case PRESENT_CANCEL_COMPLETE:
    					sb.append("[고향사랑e음 답례품 주문 취소 완료 알림]\n");
    					sb.append("  - [" + params[0] + "]님이 주문하신 답례품 취소가 완료되었습니다.\n\n");
    					sb.append("  ∙ 신청일시 : " + params[1] + "\n");
    					sb.append("  ∙ 주문번호 : " + params[2] + "\n");
    					sb.append("  ∙ 상품명 : " + params[3] + "\n");
    					sb.append("  ∙ 사유 : " + params[4] + "\n\n");
    					sb.append("【 주문내역 확인 】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case PRESENT_REFUND_REGISTER:
    					sb.append("[고향사랑e음 답례품 환불/교환 접수 알림]\n");
    					sb.append("  - [" + params[0] + "]님의 주문하신 답례품의 환불/교환이 접수 되었습니다.\n\n");
    					sb.append("  ∙ 신청일시 : " + params[1] + "\n");
    					sb.append("  ∙ 주문번호 : " + params[2] + "\n");
    					sb.append("  ∙ 상품명 : " + params[3] + "\n");
    					sb.append("  ∙ 사유 : " + params[4] + "\n\n");
    					sb.append("【 주문내역 확인 】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case PRESENT_REFUND_COMPLETE:
    					sb.append("[고향사랑e음 답례품 환불/교환 완료 알림]\n");
    					sb.append("  - [" + params[0] + "]님의 주문하신 답례품의 환불/교환이 완료 되었습니다.\n\n");
    					sb.append("  ∙ 신청일시 : " + params[1] + "\n");
    					sb.append("  ∙ 주문번호 : " + params[2] + "\n");
    					sb.append("  ∙ 상품명 : " + params[3] + "\n");
    					sb.append("  ∙ 사유 : " + params[4] + "\n\n");
    					sb.append("【 주문내역 확인 】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case HONOR_DONATION:
    					sb.append("[고향사랑e음 명예기부자 선정 알림]\n");
    					sb.append("  - [" + params[0] + "]님 " + params[1] + "의 명예기부자로 선정되었습니다. 축하합니다.\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case SECESSION:
    					sb.append("[고향사랑e음 회원탈퇴 알림]\n");
    					sb.append("  - [" + params[0] + "]님 고향사랑e음 회원을 탈퇴하셨습니다.\n");
    					sb.append("    고향사랑e음은 언제든 재가입이 가능합니다.\n");
    					sb.append("    그동안 감사했습니다.\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case ITEM_APPROVAL_REGISTER:
    					sb.append("[고향사랑e음 상품등록 승인 요청 알림]\n");
    					sb.append("  - [" + params[0] + "] 답례품 제공자님이 상품등록 승인을 요청하였습니다.\n\n");
    					sb.append("  ∙ 상품승인요청 일시 : " + params[1] + "\n");
    					sb.append("  ∙ 답례품 업체명 : " + params[0] + "\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case ITEM_APPROVAL_COMPLETE:
    					sb.append("[고향사랑e음 상품등록 승인 알림]\n");
    					sb.append("  - [" + params[0] + "] 답례품 제공자님 상품등록이 승인되었습니다.\n\n");
    					sb.append("  ∙ 상품승인요청 일시 : " + params[1] + "\n");
    					sb.append("  ∙ 답례품 업체명 : " + params[0] + "\n\n");
    					sb.append("【 답례품 몰 사이트 바로가기 】\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case ITEM_APPROVAL_CANCEL:
    					sb.append("[고향사랑e음 상품등록 승인 거절 알림]\n");
    					sb.append("  - [" + params[0] + "] 답례품 제공자님 상품등록 승인이 거절되었습니다.\n\n");
    					sb.append("  ∙ 상품승인요청 일시 : " + params[1] + "\n");
    					sb.append("  ∙ 답례품 업체명 : " + params[0] + "\n");
    					sb.append("  ∙ 승인거절사유 : " + params[2] + "\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case COMPANY_REGISTER:
    					sb.append("[고향사랑e음 답례품 제공업체 등록 알림]\n");
    					sb.append("  - [" + params[0] + "] 답례품 제공자님 업체 등록이 완료되었습니다.\n\n");
    					sb.append("  ∙ 업체(답례품제공자) 등록 일시 : " + params[1] + "\n");
    					sb.append("  ∙ 답례품 업체명 : " + params[0] + "\n");
    					sb.append("  ∙ 대표자명 : " + params[2] + "\n");
    					sb.append("  ∙ 임시 ID/PW : " + params[3] + "\n");
    					sb.append("  ※ 임시 ID/PW로 로그인 하시어 반드시 비밀번호 변경 및 금융인증서를 등록하시고 사용하시기 바랍니다.\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case CALCULATE_CHECK:
    					sb.append("[고향사랑e음 답례품 정산예정 확인 알림]\n");
    					sb.append(" - [" + params[0] + "] 답례품 제공자님 " + params[1] + "월 정산예정 내역 확인바랍니다.\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case CALCULATE_CONFIRM:
    					sb.append("[고향사랑e음 답례품 정산확정 확인 알림]\n");
    					sb.append(" - [" + params[0] + "] 답례품 제공자님 " + params[1] + "월 정산확정 내역 확인바랍니다.\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case CALCULATE_CLOSE:
    					sb.append("[고향사랑e음 답례품 정산마감 확인 알림]\n");
    					sb.append(" - [" + params[0] + "] 답례품 제공자님 " + params[1] + "월 정산마감 내역(지급완료) 확인바랍니다.\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case QNA:
    					sb.append("[고향사랑e음 질의답변 등록 알림]\n");
    					sb.append("  - [" + params[0] + "]님 문의하신 내용에 대하여 답변을 드렸습니다.\n");
    					sb.append("   ※ 답변확인 경로 : 고향사랑e음 접속 → 로그인 → 고객센터 → Q&A\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case PASSWORD_CHANGE_COMPLETE:
    					sb.append("[고향사랑e음 비밀번호 변경 알림]\n");
    					sb.append(" - [" + params[0] + "]님 비밀번호 변경이 완료되었습니다. 감사합니다.\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    				case MANAGER_LOGIN:
    					sb.append("[고향사랑e음 시스템 관리자 로그인 알림]\n");
    					sb.append("  - [" + params[0] + "]님의 관리자 계정으로 로그인 하였습니다.\n\n");
    					sb.append("☎ 국민비서 문의 : 1577-2558");
    					break;
    			}
        	} catch (NullPointerException | OutOfRangeException e) {
        		log.error(getClass().getName() + " :: getSmsContent error ", e);
        		sb = new StringBuffer();
        	}
        	return sb.toString();
    	} else {
    		return "";
    	}
    }

    public String getEsbInitTimeF() {
    	try {
        	return DateUtils.datetime(esbInitTime);
    	} catch (NullPointerException e) {
    		return "";
    	}
    }

    public String getEsbTxTimeF() {
    	try {
        	return DateUtils.datetime(esbTxTime);
    	} catch (NullPointerException e) {
    		return "";
    	}
    }

    public String getEsbComptTimeF() {
    	try {
        	return DateUtils.datetime(esbComptTime);
    	} catch (NullPointerException e) {
    		return "";
    	}
    }

    public String getEsbStatus() {
    	if ("N".equalsIgnoreCase(esbStatusCd)) {
    		return "대기";
    	} else if ("S".equalsIgnoreCase(esbStatusCd)) {
    		return "성공";
    	} else if ("F".equalsIgnoreCase(esbStatusCd)) {
    		return "실패";
    	} else {
    		return "";
    	}
    }

    public String getPhoneNumber() {
    	if (StringUtils.hasLength(sndngCntnts)) {
    		String[] contents = sndngCntnts.split("\\|");
    		int length = contents.length;
    		String phoneNumber = contents[length - 1];
    		String phoneNumber2 = phoneNumber.replaceAll("-", "");
    		length = phoneNumber2.length();
    		if (Pattern.matches("^\\d{3}\\d{3,4}\\d{4}$", phoneNumber2)) {
//    			return phoneNumber2.substring(0, 3) + "-" + phoneNumber2.substring(3, length - 4) + "-" + phoneNumber2.substring(length - 4);
    			return phoneNumber;
    		} else {
    			return "";
    		}
    	} else {
    		return "";
    	}
    }


}
