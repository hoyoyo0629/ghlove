package saleson.common.alimtalk;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Alimtalk {

	private LocalDateTime nowTime;

	public Alimtalk() {
		this.nowTime = LocalDateTime.now();
		this.ifReqId = timer("id");
		this.ifStartTime = timer("start");
	}

	/**
	 * 카카오 알림톡 API
	 * templateCode 	: 승인된 알림톡 템플릿코드
	 * - 026030000361 	: 답례품 주문(답례품신청자/답례품제공자)
	 * - 026030000377 	: 답례품 주문 취소 접수(답례품신청자/답례품제공자)
	 * - 026030000378 	: 답례품 환불/교환 접수(답례품신청자/답례품제공자)
	 * receiptNum		: 생성번호 (팝빌이 접수 단위를 식별할 수 있도록 파트너가 할당한 식별번호.)
	 * receiverNum		: 수신번호
	 * receiverName		: 수신자명
	 * orderDate		: 신청일시
	 * orderNo			: 주문번호
	 * orderName		: 상품명
	 * claimReasonDetail: 사유
	 * */
	String templateCode ;
	String receiptNum ;
	String receiverNum ;
	String receiverName ;
	String orderDate ;
	String orderNo ;
	String orderName ;
	String claimReasonDetail ;

	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public String getReceiptNum() {
		return receiptNum;
	}
	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}
	public String getReceiverNum() {
		return receiverNum;
	}
	public void setReceiverNum(String receiverNum) {
		this.receiverNum = receiverNum;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public String getClaimReasonDetail() {
		return claimReasonDetail;
	}
	public void setClaimReasonDetail(String claimReasonDetail) {
		this.claimReasonDetail = claimReasonDetail;
	}

	/**
	 * 카카오 알림톡 DB 저장
	 * ifReqId		: 연계 생성 아이디
	 * ifStartTime	: 연계발생일시
	 * ifEndTime	: 연계종료일시
	 * ifStatusCd	: 연계처리상태코드 (S:성공, F:실패)
	 * ifErrMsg		: 연계오류메시지
	 * */
	String ifReqId ;
	String ifStartTime ;
	String ifEndTime ;
	String ifStatusCd ;
	String ifErrMsg ;

	public String getIfReqId() {
    	return ifReqId;
    }
	public void setIfReqId(String ifReqId) {
		this.ifReqId = ifReqId;
	}
	public String getIfStartTime() {
		return ifStartTime;
	}
	public void setIfStartTime(String ifStartTime) {
		this.ifStartTime = ifStartTime;
	}
	public String getIfEndTime() {
		return ifEndTime;
	}
	public void setIfEndTime(String ifEndTime) {
		this.ifEndTime = ifEndTime;
	}
	public String getIfStatusCd() {
		return ifStatusCd;
	}
	public void setIfStatusCd(String ifStatusCd) {
		this.ifStatusCd = ifStatusCd;
	}
	public String getIfErrMsg() {
		return ifErrMsg;
	}
	public void setIfErrMsg(String ifErrMsg) {
		this.ifErrMsg = ifErrMsg;
	}

    // XXXX.XX.XX.(X) XX XX:XX 형태 표출
    public String getLocalDateTimeToStr() {

    	String ymd = nowTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd."));
    	String hm = nowTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
    	DayOfWeek week = nowTime.getDayOfWeek();

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
    	String time = "";
    	if (nowTime.getHour() < 12) {
    		time = "오전";
    	} else {
    		time = "오후";
    		hm = nowTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm"));
    	}
    	return ymd + "(" + weekStr + ") " + time + " " + hm;
    }

    public String timer(String type) {
    	String result = "";
    	if("id".equals(type)) {
    		String ymd = nowTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        	String hms = nowTime.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmssSSS"));

        	result = ymd + hms;
    	}else if("start".equals(type)) {
    		String ymd = nowTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        	String hms = nowTime.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss"));

        	result = ymd + hms;
    	}else if("end".equals(type)) {
        	this.nowTime = LocalDateTime.now();

    		String ymd = nowTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        	String hms = nowTime.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss"));

        	result = ymd + hms;
    	}
    	return result;
    }



}
