package saleson.batch.job;


/**
 * 배치가 실행되는 메서드 목록
 * 메서드 명이 Batch()로 끝나야 배치 실행 로그 (OP_BATCH_EXCUTION)가 기록됨.
 * @author dbclose
 *
 */
public interface JobService {

	/**
	 * 상품 검색어 정보를 갱신한다.
	 */
	public void itemKeywordBatch();

	/**
	 * 자동 구매확정처리
	 */
	public void autoConfirmPurchaseBatch();

	/**
	 * 자동 배송완료처리
	 * 2023.02.09
	 */
	public void autoShippingCompleteBatch();

	/**
	 * 자동 배송완료처리(스마트택배)
	 * 2025.11.14
	 */
	public void autoSmartDeliveryShippingCompleteBatch();

	/**
	 * 포인트 만료 처리
	 */
	public void expirationPointBatch();

	/**
	 * 포인트 만료 안내 메시지 발송
	 */
	public void expirationPointSendMessageBatch();

	/**
	 * 휴면계정 안내 메일 발송
	 */
	public void sendMailToInactiveUserBatch();

	/**
	 * 휴면계정으로 전환 처리.
	 */
	public void processInactiveUserBatch();

	/**
	 * 관리자 휴면계정 업데이트
	 */
	public void updateSleepManager();

	/**
	 * 판매자 주문안내 SMS 발송.
	 */
	public void sendOrderSmsToSellerBatch();

	/**
	 * 상품 옵션 품절 배치
	 */
	public void updateItemOptionSoldoutBatch();

	/**
	 * 카테고리 구분없이 전체 상품에서 랭킹을 정한다.
	 * TOP 100
	 */
	public void itemRankingType1Batch();

	/**
	 * 카테고리 그룹을 기준으로 상품의 랭킹을 정한다.
	 */
	public void itemRankingType2Batch();

	/**
	 * 1~4 카테고리를 기준으로 상품의 랭킹을 정한다.
	 */
	public void itemRankingType3Batch();

	/**
	 * 정기발행쿠폰을 쿠폰리스트에 등록.
	 */
	public void couponRegularBatch();

	/**
	 * 회원 등급 산정
	 */
	public void userLevelBatch();

	/**
	 * 회원 등급 산정 (날짜지정)
	 */
	public void userLevelBatch(String date);

	/**
	 * 입금지연 주문 취소 배치
	 */
	public void cancelWaitingDepositOrderBatch();

	/**
	 * 자동 완성 데이터 처리
	 */
	public void autoCompleteKeywordBatch();

	/**
	 * 카카오 알림톡 업데이트 배치
	 */
	void updateKakaoAlimTalkBatch();

	/**
	 * 캠페인용 유저 업데이트 배치
	 */
	void updateUserCampaignBatch();

	/**
	 * 캠페인용 예약발송 발송처리 배치
	 */
	void sendCampaignMessageBatch();

	/**
	 * 캠페인용 발송통계 배치
	 */
	void updateCampaignSentBatch();

    /**
     * 임시 주문 정보 삭제처리 배치
     */
	void deleteOrderTempInfoBatch();

	/**
	 * 구매확정 UMS 배치
	 */
	void autoConfirmPurchaseUmsBatch();

	/**
	 * 구매확정 요청 UMS 배치
	 */
	void autoConfirmPurchaseRequestUmsBatch();

	/**
	 * 쿠폰만료기간 안내 메시지 발송 배치
	 */
	void expirationCouponSendMessageBatch();

	/**
	 * 포인트만료 안내 메시지 발송 배치
	 */
	void expirationPointMessageBatch();

	/**
	 * 사용되지 않는 사용자 잠금처리
	 */
	void updateLockForManagerBatch();

	/**
	 * 기부 포인트 만료 처리
	 */
	public void expirationCntrPointBatch();

	/**
	 * 기부 데이터 미결재분 삭제(익월 자정까지 미결재시 삭제 처리)
	 */
	public void deleteCntrDataBatch();

 	/**
	 * 국세청 전자기부영수증 데이터 처리(online)
	 */
	public void sendNtsEreceiptOnBatch();

	/**
	 * <pre>
	 * comment       : 3월14일 이전 데이터 배치
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 3. 13.
	 *
	 * </pre>
	 * void
	 */
	public void sendNtsEreceiptBatchOld();

	/**
	 * <pre>
	 * comment       : 국세청 전자기부영수증 데이터 처리(online) 테스트
	 * preMethodName : sendNtsEreceiptOnBatchTest
	 * author        : hybrid
	 * date          : 2023. 3. 7.
	 *
	 * </pre>
	 * void
	 */
	public void sendNtsEreceiptOnBatchTest();

	/**
	 * 국세청 전자기부영수증 데이터 처리(offline)
	 */
	public void sendNtsEreceiptOffBatch();

	/**
	 * 지방세외(현세대) 미납건 데이터 삭제 처리
	 */
	public void deleteNotSunapStndBatch();

	/**
	 * 상품 NEW 삭제 배치
	 */
	public void deleteItemNewBatch();

	/**
	 * 2023.03.24 장바구니 30일 지난 데이터 삭제 처리
	 */
	public void deleteCartBatch();

	/**
	 * 답례품 제공자 정산예정확인 문자 전송 배치 매월 1일 9시(0 0 9 1 * ?)
	 */
	public void sendRemittanceExpectedMsgBatch();

	/**
	 * <pre>
	 * comment       : 삭제된 기부건 재 수납확인
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 4. 6.
	 *
	 * </pre>
	 * void
	 */
	public void confirmSunapForDeletedListBatch();

	/**
	 * 기부, 콜 6시기준 보고용 배치
	 *
	 * @param
	 */

	public void confirmdonationForReportBatch();

	/**
	 * <pre>
	 * comment       : 농협연계 관련 배치
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 30.
	 *
	 * </pre>
	 * void
	 */
	public void userCntrForNhBatch();

	public void userCntrLocForNhBatch();

	public void locForNhBatch();


	/**
	 * <pre>
	 * comment       : 유저의 생년월일 테이블에 등록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 10. 13.
	 *
	 * </pre>
	 * void
	 */
	public void userBirthdayDecBatch();

	/**
	 * <pre>
	 * comment       : 1시간마다 기부건수가 없는 지자체 목록 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 12. 11.
	 *
	 * </pre>
	 * void
	 */
	public void getNoBugaLocgovListBatch();

	/**
	 * <pre>
	 * comment       : 세외데이터 기부금 영수증 처리
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 1. 4.
	 *
	 * </pre>
	 * void
	 */
	public void cntrTaxTempBatch();

	/**
	 * <pre>
	 * comment       : 기부 미수납건 데이터 처리 (18시~24시)
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 2. 13.
	 *
	 * </pre>
	 * void
	 */
	public void cntrSunapBatch();

	/**
	 * <pre>
	 * comment       : 사용자 연령데이터 통계 배치
	 * preMethodName :
	 * author        : ghl009
	 * date          : 2024. 3. 14.
	 *
	 * </pre>
	 * void
	 */
	public void opUserBirthdayStatBatch();

	/**
	 * <pre>
	 * comment       : 월통계 현황보고 배치
	 * preMethodName :
	 * author        : ghl009
	 * date          : 2024. 7. 15.
	 *
	 * </pre>
	 * void
	 */
	public void statisticsReportBatch();

	/**
	 * <pre>
	 * comment       : 년통계 현황보고 배치
	 * preMethodName :
	 * author        : ghl009
	 * date          : 2024. 7. 15.
	 *
	 * </pre>
	 * void
	 */
	public void statisticsYearReportBatch();

	/**
	 * <pre>
	 * comment       : 년통계 현황보고 배치 - 연령, 월간
	 * preMethodName :
	 * author        : ghl009
	 * date          : 2024. 7. 15.
	 *
	 * </pre>
	 * void
	 */
	public void statisticsYearReportAgeMonthBatch();

	/**
	 * <pre>
	 * comment       : 주기적으로 세외 수납결과 확인하여 g_cntr에 수납결과 반영하기
	 * preMethodName :
	 * author        : ghl0008
	 * date          : 2025. 7. 14.
	 *
	 * </pre>
	 * void
	 */
	public void fiveMinuteSunapCheckBatch();

	/**
	 * <pre>
	 * comment       : 하루한번 세외 수납결과 확인하여 g_cntr에 수납결과 반영하기
	 * preMethodName :
	 * author        : ucube
	 * date          : 2025. 8. 22.
	 *
	 * </pre>
	 * void
	 */
	public void dayCompleteSunapCheckBatch();


	/**
	 * <pre>
	 * comment       : 마감일자 체크 하여 공겨 여부 변경
	 * preMethodName :
	 * author        : ucube
	 * date          : 2025. 9. 12.
	 *
	 * </pre>
	 * void
	 */
	public void itemDisplayContorlBatch();

	/**
	 * <pre>
	 * comment       : 오프라인기부 답례품주문 건 중 미수납된 기부 답례품 상태값 변경(쓰레기 데이터 방지)
	 * preMethodName :
	 * author        : ghl0006
	 * date          : 2025. 8. 18.
	 *
	 * </pre>
	 * void
	 */
	public void allDayOffItemUpdateBatch();

	public void dailyGramtInsertBatch();

	/**
	 * 넷퍼넬 대기자 수 배치
	 * n분마다 mainServiceImpl의 Cache 업데이트
	 * */
	void updateWaitUserCacheBatch();


	/**
	 * 특정기부사업 상태 진행 -> 종료 배치
	 * 특정기부사업 목표금액이 완료시 20분마다 한번씩 체크후
	 * 종료상태로 변경
	 */
	public void dsgnBizStatusUpdateBatch();

	/**
	 * 국자원 모바일 메시지 배치
	 * 주문 결제완료 상태 체크하여 메시지 전송(카카오 알림톡)
	 *
	 */
	public void sendNuri2PresentOrder();

	/**
	 * BIX5 통계를 위한 배치
	 * 1. 기부금액(전년비)
	 * 2. 기부횟수(전년비)
	 */
	void bix5CntrAmt();

	/**
	 * 3. 평균 기부금액 비교
	 */

	/**
	 * 4. 전년대비 월별 기부금액
	 */
	void bix5CntrAmtMonth();

	/**
	 * 5. 대상별 기부금액
	 */
	void bix5CntrAmtBiz();

	/**
	 * 6. 특정사업별 모금현황
	 */
	void bix5CntrBiz();

	/**
	 * 7. 기부 접수창구별 모금액
	 */
	void bix5CntrPath();

	/**
	 * 8. 기부 금액별 건수
	 */
	void bix5CntrAmtCat();

	/**
	 * 9. 연령대별 기부 건수
	 */
	void bix5CntrOld();

	/**
	 * 10. 기부자 주소지 기준 모금 순위 TOP 10
	 */
	void bix5CntrAddr();

	/**
	 * 11. 판매 포인트(전년비)
	 */
	void bix5GiftUsePoint();

	/**
	 * 12. 답례품 종 개수
	 */
	void bix5GiftCnt();
	/**
	 * 13. 평균 판매 포인트 비교
	 */
	void bix5GiftUsePointCpr();

	/**
	 * 14. 답례품 등록 현황-카테고리 기준
	 */
	void bix5GiftCat();

	/**
	 * 15. 답례품 등록 현황(포인트 구간 기준)
	 */
	void bix5GiftPoint();

	/**
	 * 16. 카테고리별 판매수량
	 */
	void bix5GiftCatSell();

	/**
	 * 17. 답례품 제공자 발송 소요 시간 TOP 5
	 */
	void bix5GiftDeli();

	/**
	 * 18. 총 잔여 포인트(소진율)
	 */
	void bix5PointUse();

	/**
	 * 19. 연간 발생 포인트
	 */
	void bix5PointUseYear();

	/**
	 * 20. 포인트 현황
	 */
	void bix5PointYear();
}
