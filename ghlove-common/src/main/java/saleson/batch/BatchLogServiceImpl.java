package saleson.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.batch.domain.BatchLog;
import saleson.batch.support.BatchLogParam;
import saleson.common.utils.ShopUtils;
import saleson.shop.remittance.domain.RemittanceExpected;

@Service("batchLogService")
public class BatchLogServiceImpl implements BatchLogService {
	private static final Logger log = LoggerFactory.getLogger(BatchLogServiceImpl.class);

    @Autowired
    private BatchLogMapper batchLogMapper;

    @Override
    public List<BatchLog> getBatchLogList(BatchLogParam batchLogParam) {
    	
    	if (batchLogParam.getPage() == 0) {
    		batchLogParam.setPage(1);
		}
		
		int totalCount = batchLogMapper.getBatchLogListCount(batchLogParam);
		
		Pagination pagination = Pagination.getInstance(totalCount, batchLogParam.getItemsPerPage());
		batchLogParam.setPagination(pagination);

		List<BatchLog> list = batchLogMapper.getBatchLogList(batchLogParam);


		return list;
    }
}
