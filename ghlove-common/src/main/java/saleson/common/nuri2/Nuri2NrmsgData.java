package saleson.common.nuri2;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONObject;

public class Nuri2NrmsgData {

	/**
	 * [ nuru2 ] 중요 컬럼 (INSERT 할때 사용)
	 * msgKey			: 숫자 11자리, 메시지 일련번호, 반드시 고유값, 중복시 발송실패, 시퀀스(시리얼번호사용)
	 * msgState			: 1:전송대기,  3:전송수집중(QUE 수집), 5:전송완료(결과대기), 6:결과처리 완료(결과회신)
	 * inputDate		: DB 입력시간(DB서버 시간 기준)
	 * resDate			: 발송요청시간, 예약전송:미래시간, 즉시전송:SYSDATE  -- CUBRID : now() -- 날짜형식 : YYYYMMDDHH24MISS, 날짜 포멧으로 입력 권장
	 * altCountryCode	: 국가코드 : 기본 값 82, 해외 카톡 발송시 해당국가코드 입력
	 * phone			: 수신번호(숫자형태의 문자, 11~12자리),     [*][중요]반드시 휴대폰 번호 형식으로만 입력, 01X0000XXXX
	 * callback			: 발신번호(숫자형태의 문자, 지역번호 필수), [*][중요]휴대폰번호 사용시에는 발신도용 해제 여부 필요, 스미싱 악용 방지용 '번호도용 차단서비스' 가입자는 해제 후 설정가능 합니다.
	 * ##### 첫번째 컨텐츠(1차) 카카오 #####
	 * msgType1			: [대분류] :SMS:단문 메시지, MMS:멀티메시지(장문, 첨부), ALT:카카오 알림톡 메시지, RCS: 안심문자
	 * contentsType1	: [소분류] :SMS:단문 메시지, LMS:장문, MMS:멀티메시지(장문+첨부, 첨부), ALT:카카오 알림톡 메시지, RCS: 안심문자
	 * altSenderKey		: 발송키(발신 프로필키), 발송키는 채널을 의미합니다. 채널이 다르면 다른 발송키를 설정
	 * altTemplateCode	: 템플릿코드
	 * altJson			: 발송할 내용을 JSON 형태(한줄로입력)로 직접 입력, '{"text":"모바일메시지서비스 운영 및 발송 가이드 안내\n\n카카오톡\n모바일메시지 테스트입니다\n\n042-250-5537\n감사합니다."}'
	 * 전체 1000자리(Length), 줄바꿈 치환 필수 '\r\n' ▶ '\n'(1 Length로 계산), 실제로 1줄로 입력처리 일부db에서 '\n'만 입력하게되면 줄바꿈처리로 에러, 실제 줄바꿈 기호를 메시지를 db에 입력시 '\n' 처리해서 입력(db 마다 다를수 있음)
	 * ##### 두번째 컨텐츠(첫번째 컨텐츠 실패시 수행)(2차) 문자 #####
	 * msgType2			: [대분류] :SMS:단문 메시지, MMS:멀티메시지(장문, 첨부), ALT:카카오 알림톡 메시지, RCS: 안심문자
	 * contentsType2	: [소분류] :SMS:단문 메시지, LMS:장문, MMS:멀티메시지(장문+첨부, 첨부), ALT:카카오 알림톡 메시지, RCS: 안심문자
	 * xmsSubject		: 생략가능, LMS에서만 사용 최대 30Byte 이하 한글(2Byte)로 계산, 줄바꿈은 '\n' 처리 1Byte 처리, 1Byte로 계산
	 * xmsText			: SMS: 90byte 까지 입력(줄바꿈은 '\n' 처리 1Byte 처리, 1Byte로 계산), LMS:2000 Byte 이하
	 * */

	Long msgKey;
	String subId;
	Long userKey;
	String userGroup;
	String userId;
	String userJobid;
	Integer centerKey;
	Integer msgPriority;
	Integer msgState;
	LocalDateTime inputDate;
	LocalDateTime resDate;
	LocalDateTime queDate;
	LocalDateTime sentDate;
	LocalDateTime rsltDate;
	LocalDateTime reportDate;
	String rsltCode;
	String rsltNet;
	String rsltType;
	Integer sentCount;
	String historyMsgType;
	String historyRsltCode;
	String identifier;
	String phone;
	String callback;
	String msgType1;
	String contentsType1;
	LocalDateTime queDate1;
	LocalDateTime sentDate1;
	String msgType2;
	String contentsType2;
	LocalDateTime queDate2;
	LocalDateTime sentDate2;
	String msgType3;
	String contentsType3;
	LocalDateTime queDate3;
	LocalDateTime sentDate3;
	String xmsRsltCode;
	String xmsRsltNet;
	LocalDateTime xmsRsltDate;
	LocalDateTime xmsReportDate;
	String altRsltCode;
	String altRsltNet;
	LocalDateTime altRsltDate;
	LocalDateTime altReportDate;
	String rcsRsltCode;
	String rcsRsltNet;
	LocalDateTime rcsRsltDate;
	LocalDateTime rcsReportDate;
	String xmsSubject;
	String xmsText;
	String xmsFileName1;
	String xmsFileName2;
	String xmsFileName3;
	String altCountryCode;
	String altSenderKey;
	String altTemplateCode;
	String altJson;
	String rcsBrandKey;
	String rcsBrandId;
	String rcsMessageBaseId;
	String rcsJson;
	public Long getMsgKey() {
		return msgKey;
	}
	public void setMsgKey(Long msgKey) {
		this.msgKey = msgKey;
	}
	public String getSubId() {
		return subId;
	}
	public void setSubId(String subId) {
		this.subId = subId;
	}
	public Long getUserKey() {
		return userKey;
	}
	public void setUserKey(Long userKey) {
		this.userKey = userKey;
	}
	public String getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserJobid() {
		return userJobid;
	}
	public void setUserJobid(String userJobid) {
		this.userJobid = userJobid;
	}
	public Integer getCenterKey() {
		return centerKey;
	}
	public void setCenterKey(Integer centerKey) {
		this.centerKey = centerKey;
	}
	public Integer getMsgPriority() {
		return msgPriority;
	}
	public void setMsgPriority(Integer msgPriority) {
		this.msgPriority = msgPriority;
	}
	public Integer getMsgState() {
		return msgState;
	}
	public void setMsgState(Integer msgState) {
		this.msgState = msgState;
	}
	public LocalDateTime getInputDate() {
		return inputDate;
	}
	public void setInputDate(LocalDateTime inputDate) {
		this.inputDate = inputDate;
	}
	public LocalDateTime getResDate() {
		return resDate;
	}
	public void setResDate(LocalDateTime resDate) {
		this.resDate = resDate;
	}
	public LocalDateTime getQueDate() {
		return queDate;
	}
	public void setQueDate(LocalDateTime queDate) {
		this.queDate = queDate;
	}
	public LocalDateTime getSentDate() {
		return sentDate;
	}
	public void setSentDate(LocalDateTime sentDate) {
		this.sentDate = sentDate;
	}
	public LocalDateTime getRsltDate() {
		return rsltDate;
	}
	public void setRsltDate(LocalDateTime rsltDate) {
		this.rsltDate = rsltDate;
	}
	public LocalDateTime getReportDate() {
		return reportDate;
	}
	public void setReportDate(LocalDateTime reportDate) {
		this.reportDate = reportDate;
	}
	public String getRsltCode() {
		return rsltCode;
	}
	public void setRsltCode(String rsltCode) {
		this.rsltCode = rsltCode;
	}
	public String getRsltNet() {
		return rsltNet;
	}
	public void setRsltNet(String rsltNet) {
		this.rsltNet = rsltNet;
	}
	public String getRsltType() {
		return rsltType;
	}
	public void setRsltType(String rsltType) {
		this.rsltType = rsltType;
	}
	public Integer getSentCount() {
		return sentCount;
	}
	public void setSentCount(Integer sentCount) {
		this.sentCount = sentCount;
	}
	public String getHistoryMsgType() {
		return historyMsgType;
	}
	public void setHistoryMsgType(String historyMsgType) {
		this.historyMsgType = historyMsgType;
	}
	public String getHistoryRsltCode() {
		return historyRsltCode;
	}
	public void setHistoryRsltCode(String historyRsltCode) {
		this.historyRsltCode = historyRsltCode;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public String getMsgType1() {
		return msgType1;
	}
	public void setMsgType1(String msgType1) {
		this.msgType1 = msgType1;
	}
	public String getContentsType1() {
		return contentsType1;
	}
	public void setContentsType1(String contentsType1) {
		this.contentsType1 = contentsType1;
	}
	public LocalDateTime getQueDate1() {
		return queDate1;
	}
	public void setQueDate1(LocalDateTime queDate1) {
		this.queDate1 = queDate1;
	}
	public LocalDateTime getSentDate1() {
		return sentDate1;
	}
	public void setSentDate1(LocalDateTime sentDate1) {
		this.sentDate1 = sentDate1;
	}
	public String getMsgType2() {
		return msgType2;
	}
	public void setMsgType2(String msgType2) {
		this.msgType2 = msgType2;
	}
	public String getContentsType2() {
		return contentsType2;
	}
	public void setContentsType2(String contentsType2) {
		this.contentsType2 = contentsType2;
	}
	public LocalDateTime getQueDate2() {
		return queDate2;
	}
	public void setQueDate2(LocalDateTime queDate2) {
		this.queDate2 = queDate2;
	}
	public LocalDateTime getSentDate2() {
		return sentDate2;
	}
	public void setSentDate2(LocalDateTime sentDate2) {
		this.sentDate2 = sentDate2;
	}
	public String getMsgType3() {
		return msgType3;
	}
	public void setMsgType3(String msgType3) {
		this.msgType3 = msgType3;
	}
	public String getContentsType3() {
		return contentsType3;
	}
	public void setContentsType3(String contentsType3) {
		this.contentsType3 = contentsType3;
	}
	public LocalDateTime getQueDate3() {
		return queDate3;
	}
	public void setQueDate3(LocalDateTime queDate3) {
		this.queDate3 = queDate3;
	}
	public LocalDateTime getSentDate3() {
		return sentDate3;
	}
	public void setSentDate3(LocalDateTime sentDate3) {
		this.sentDate3 = sentDate3;
	}
	public String getXmsRsltCode() {
		return xmsRsltCode;
	}
	public void setXmsRsltCode(String xmsRsltCode) {
		this.xmsRsltCode = xmsRsltCode;
	}
	public String getXmsRsltNet() {
		return xmsRsltNet;
	}
	public void setXmsRsltNet(String xmsRsltNet) {
		this.xmsRsltNet = xmsRsltNet;
	}
	public LocalDateTime getXmsRsltDate() {
		return xmsRsltDate;
	}
	public void setXmsRsltDate(LocalDateTime xmsRsltDate) {
		this.xmsRsltDate = xmsRsltDate;
	}
	public LocalDateTime getXmsReportDate() {
		return xmsReportDate;
	}
	public void setXmsReportDate(LocalDateTime xmsReportDate) {
		this.xmsReportDate = xmsReportDate;
	}
	public String getAltRsltCode() {
		return altRsltCode;
	}
	public void setAltRsltCode(String altRsltCode) {
		this.altRsltCode = altRsltCode;
	}
	public String getAltRsltNet() {
		return altRsltNet;
	}
	public void setAltRsltNet(String altRsltNet) {
		this.altRsltNet = altRsltNet;
	}
	public LocalDateTime getAltRsltDate() {
		return altRsltDate;
	}
	public void setAltRsltDate(LocalDateTime altRsltDate) {
		this.altRsltDate = altRsltDate;
	}
	public LocalDateTime getAltReportDate() {
		return altReportDate;
	}
	public void setAltReportDate(LocalDateTime altReportDate) {
		this.altReportDate = altReportDate;
	}
	public String getRcsRsltCode() {
		return rcsRsltCode;
	}
	public void setRcsRsltCode(String rcsRsltCode) {
		this.rcsRsltCode = rcsRsltCode;
	}
	public String getRcsRsltNet() {
		return rcsRsltNet;
	}
	public void setRcsRsltNet(String rcsRsltNet) {
		this.rcsRsltNet = rcsRsltNet;
	}
	public LocalDateTime getRcsRsltDate() {
		return rcsRsltDate;
	}
	public void setRcsRsltDate(LocalDateTime rcsRsltDate) {
		this.rcsRsltDate = rcsRsltDate;
	}
	public LocalDateTime getRcsReportDate() {
		return rcsReportDate;
	}
	public void setRcsReportDate(LocalDateTime rcsReportDate) {
		this.rcsReportDate = rcsReportDate;
	}
	public String getXmsSubject() {
		return xmsSubject;
	}
	public void setXmsSubject(String xmsSubject) {
		this.xmsSubject = xmsSubject;
	}
	public String getXmsText() {
		return xmsText;
	}
	public void setXmsText(String xmsText) {
		this.xmsText = xmsText;
	}
	public String getXmsFileName1() {
		return xmsFileName1;
	}
	public void setXmsFileName1(String xmsFileName1) {
		this.xmsFileName1 = xmsFileName1;
	}
	public String getXmsFileName2() {
		return xmsFileName2;
	}
	public void setXmsFileName2(String xmsFileName2) {
		this.xmsFileName2 = xmsFileName2;
	}
	public String getXmsFileName3() {
		return xmsFileName3;
	}
	public void setXmsFileName3(String xmsFileName3) {
		this.xmsFileName3 = xmsFileName3;
	}
	public String getAltCountryCode() {
		return altCountryCode;
	}
	public void setAltCountryCode(String altCountryCode) {
		this.altCountryCode = altCountryCode;
	}
	public String getAltSenderKey() {
		return altSenderKey;
	}
	public void setAltSenderKey(String altSenderKey) {
		this.altSenderKey = altSenderKey;
	}
	public String getAltTemplateCode() {
		return altTemplateCode;
	}
	public void setAltTemplateCode(String altTemplateCode) {
		this.altTemplateCode = altTemplateCode;
	}
	@SuppressWarnings("unchecked")
	public String getAltJson() {
		JSONObject obj = new JSONObject();
		obj.put("text", this.altJson);
		return obj.toString();
	}
	public void setAltJson(String altJson) {
		this.altJson = altJson;
	}
	public String getRcsBrandKey() {
		return rcsBrandKey;
	}
	public void setRcsBrandKey(String rcsBrandKey) {
		this.rcsBrandKey = rcsBrandKey;
	}
	public String getRcsBrandId() {
		return rcsBrandId;
	}
	public void setRcsBrandId(String rcsBrandId) {
		this.rcsBrandId = rcsBrandId;
	}
	public String getRcsMessageBaseId() {
		return rcsMessageBaseId;
	}
	public void setRcsMessageBaseId(String rcsMessageBaseId) {
		this.rcsMessageBaseId = rcsMessageBaseId;
	}
	public String getRcsJson() {
		return rcsJson;
	}
	public void setRcsJson(String rcsJson) {
		this.rcsJson = rcsJson;
	}

	public String getContent(String type, String receiverName, String orderDate, String orderNo, String orderName, String claimReasonDetail) {
    	StringBuffer sb = new StringBuffer();

		try {
			switch (type) {
				case "PRESENT_ORDER":
					sb.append("[고향사랑e음 답례품 주문 알림]\n");
					sb.append("  - [" + receiverName + "]님의 답례품 주문이 정상적으로 완료되었습니다.\n");
					sb.append("\n");
					sb.append("  ∙ 신청일시 : " + orderDate + "\n");
					sb.append("  ∙ 주문번호 : " + orderNo + "\n");
					sb.append("  ∙ 상품명 : " + orderName + "\n");
					sb.append("\n");
					sb.append("☎ 주문내역 문의 : 1522-2431");
					break;
				case "PRESENT_CANCEL_REGISTER":
					sb.append("[고향사랑e음 답례품 주문 취소 접수 알림]\n");
					sb.append("  - [" + receiverName + "]님이 주문하신 답례품 취소가 접수되었습니다.\r\n");
					sb.append("\n");
					sb.append("  ∙ 신청일시 : " + orderDate + "\n");
					sb.append("  ∙ 주문번호 : " + orderNo + "\n");
					sb.append("  ∙ 상품명 : " + orderName + "\n");
					sb.append("  ∙ 사유 : " + claimReasonDetail + "\n");
					sb.append("\n");
					sb.append("☎ 주문내역 문의 : 1522-2431");
					break;
				case "PRESENT_REFUND_REGISTER":
					sb.append("[고향사랑e음 답례품 환불/교환 접수 알림]\n");
					sb.append("  - [" + receiverName + "]님이 주문하신 답례품의 환불/교환이 접수 되었습니다.\n");
					sb.append("\n");
					sb.append("  ∙ 신청일시 : " + orderDate + "\n");
					sb.append("  ∙ 주문번호 : " + orderNo + "\n");
					sb.append("  ∙ 상품명 : " + orderName + "\n");
					sb.append("  ∙ 사유 : " + claimReasonDetail + "\n");
					sb.append("\n");
					sb.append("☎ 주문내역 문의 : 1522-2431");
					break;
			}
		}catch (Exception e) {
    		//System.out.println("getAlimtalkContent error :" + e);
    		sb = new StringBuffer();
		}

    	return sb.toString();
	}

	public String getOrderContent(String type, String orderCnt) {
    	StringBuffer sb = new StringBuffer();

		try {
			switch (type) {
				case "TOTAL_PRESENT_ORDER":
					sb.append("[고향사랑e음 답례품 주문 알림]\n");
					sb.append("  - 결제완료된 답례품 주문 내역을 안내드립니다.\n");
					sb.append("\n");
					sb.append("  ∙ 주문 일자 : " + getLocalDateTimeToStr() + "\n");
					sb.append("  ∙ 총 주문건수 : " + orderCnt + "건\n");
					sb.append("\n");
					sb.append("상세 주문내역을 확인하시어 출고를 진행하여 주시기 바랍니다.");
					sb.append("\n");
					sb.append("☎ 주문내역 문의 : 1522-2431");
					break;
			}
		}catch (Exception e) {
    		//System.out.println("getAlimtalkOrderContent error :" + e);
    		sb = new StringBuffer();
		}

    	return sb.toString();
	}

    public String getLocalDateTimeToStr() {
    	//2026.05.18.(월) ~ 2026.05.19.(화) 오전 08:00
    	LocalDateTime nowTime = LocalDateTime.now();

    	LocalDateTime yesterday = nowTime.minusDays(1);

    	String ymd = nowTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd."));
    	String hm = nowTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH"));

    	String yesterday_ymd = yesterday.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd."));

    	String time = "";
    	if (nowTime.getHour() < 12) {
    		time = "오전";
    		hm = String.join(":", hm, "00");
    	} else {
    		time = "오후";
    		hm = nowTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh"));
    		hm = String.join(":", hm, "00");
    	}
    	return yesterday_ymd + "(" + weekStr(yesterday.getDayOfWeek()) + ") ~ " + ymd + "(" + weekStr(nowTime.getDayOfWeek()) + ") " + time + " " + hm;
    }

    public String weekStr(DayOfWeek week) {
    	String weekStr = "";
    	switch (week) {
			case MONDAY:
				weekStr = "월";
				break;
			case TUESDAY:
				weekStr = "화";
				break;
			case WEDNESDAY:
				weekStr = "수";
				break;
			case THURSDAY:
				weekStr = "목";
				break;
			case FRIDAY:
				weekStr = "금";
				break;
			case SATURDAY:
				weekStr = "토";
				break;
			case SUNDAY:
				weekStr = "일";
				break;
		}
    	return weekStr;
    }

}
