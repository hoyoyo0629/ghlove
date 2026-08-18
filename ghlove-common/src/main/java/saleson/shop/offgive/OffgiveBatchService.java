package saleson.shop.offgive;

public interface OffgiveBatchService {

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
	void ItemStatusUpdateBatch();
}
