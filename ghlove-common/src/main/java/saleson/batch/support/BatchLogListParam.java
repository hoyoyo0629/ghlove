package saleson.batch.support;

import com.onlinepowers.framework.util.ArrayUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import saleson.batch.domain.BatchLog;
import saleson.common.utils.CommonUtils;

public class BatchLogListParam extends ListParam {

    private String[] batchLogId;

    public String[] getBatchLogId() {
        return CommonUtils.copy(batchLogId);
    }

    public void setBatchLogId(String[] batchLogId) {
        this.batchLogId = CommonUtils.copy(batchLogId);
    }

    public BatchLog getBatchLogId(int index) {

        BatchLog batchLog = new BatchLog();

        batchLog.setBatchLogId(ArrayUtils.get(getBatchLogId(),index));

        return batchLog;
    }
}
