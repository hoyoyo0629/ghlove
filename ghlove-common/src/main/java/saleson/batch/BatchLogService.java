package saleson.batch;

import java.util.List;

import saleson.batch.domain.BatchLog;
import saleson.batch.support.BatchLogParam;

public interface BatchLogService {
	 /**
     * 배치 작업 조회
     * @param batchJobParam
     * @return
     */
    public List<BatchLog> getBatchLogList(BatchLogParam batchLogParam);
}
