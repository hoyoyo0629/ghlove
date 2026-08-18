package saleson.batch.job;


/**
 * 배치가 실행되는 메서드 목록
 * 메서드 명이 Batch()로 끝나야 배치 실행 로그 (OP_BATCH_EXCUTION)가 기록됨.
 *
 * 연통계 거주지 전용
 *
 * @author dbclose
 *
 */
public interface JobPsintService {


	/**
	 * <pre>
	 * comment       : 연통계 거주지 전용
	 * preMethodName :
	 * author        : ghl009
	 * date          : 2024. 7. 15.
	 *
	 * </pre>
	 * void
	 */
	public void statisticsYearReportPsintBatch();

	/**
	 * <pre>
	 * comment       : 연통계 답례품 순위 전용
	 * preMethodName :
	 * author        : ghl009
	 * date          : 2024. 7. 15.
	 *
	 * </pre>
	 * void
	 */
	public void statisticsYearReportGoodsBatch();
}
