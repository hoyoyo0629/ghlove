package saleson.shop.donation;

public interface NgDonationBatchService {

	/**
	 * 기부 데이터 미결재분 삭제(익월 자정까지 미결재시 삭제 처리)
	 * @param
	 */
	void deleteCntrData();

	/**
	 * 지방세외(현세대) 미납건 데이터 삭제 처리(batch)
	 * @param
	 */
	void deleteNotSunapStndBatch();

	/**
	 * 국세청 전자기부영수증 데이터(online) 처리(batch)
	 * @param
	 */
	void sendNtsEreceiptOnBatch();

	/**
	 * 국세청 전자기부영수증 데이터(offline) 처리(batch)
	 * @param
	 */
	void sendNtsEreceiptOffBatch();

	/**
	 * <pre>
	 * comment       : 1월1일~3월13일까지 데이터 처리. (18시 이후~09시 이전)
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 3. 13.
	 *
	 * </pre>
	 * void
	 */
	void sendNtsEreceiptBatchOld();
	/**
	 * <pre>
	 * comment       : 국세청 영수증 테스트
	 * preMethodName : sendNtsEreceiptOnBatchTest
	 * author        : hybrid
	 * date          : 2023. 3. 7.
	 *
	 * </pre>
	 * void
	 */
	void sendNtsEreceiptOnBatchTest();

	/**
	 * <pre>
	 * comment       : 삭제처리된 기부건들의 수납여부 확인
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 4. 6.
	 *
	 * </pre>
	 * void
	 */
	void confirmSunapForDeletedList();

	/**
	 * <pre>
	 * comment       : 기부건수가 없는 지자체 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 12. 11.
	 *
	 * </pre>
	 * void
	 */
	void getNoBugaLocgovList();

	/**
	 * <pre>
	 * comment       : 세외데이터 전체 국세청 신고 배치
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 1. 4.
	 *
	 * </pre>
	 * void
	 */
	void cntrTaxTempBatch();

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

	void cntrSunapBatch();

	/**
	 * <pre>
	 * comment       : 기부 미수납건 데이터 처리 (배치잡 10초에 한번)
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2025. 7. 14.
	 *
	 * </pre>
	 * void
	 */
	void cntrNonSunapFiveMinSunapCheck();

	/**
	 * <pre>
	 * comment       : 기부 미수납건 데이터 처리 (23:40)
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2025. 8. 22.
	 *
	 * </pre>
	 * void
	 */
	void cntrNonSunapdayCompleteSunapCheck();

	/**
	 * 마감일자 확인후 공개여부 변경 배치
	 *
	 */
	void itemDisplayContorlCheck();
}
