package saleson.batch;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.batch.domain.BatchLog;
import saleson.batch.support.BatchLogParam;

@Mapper("batchLogMapper")
public interface BatchLogMapper {
    /**
     * 배치 작업 조회
     * @param batchLogParam
     * @return
     */
    public List<BatchLog> getBatchLogList(BatchLogParam batchLogParam);

    /**
     * 배치 작업 조회
     * @param batchLogParam
     * @return
     */
    public int getBatchLogListCount(BatchLogParam batchLogParam);

}
