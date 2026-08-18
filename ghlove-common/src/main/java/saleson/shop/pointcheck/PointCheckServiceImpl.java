package saleson.shop.pointcheck;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.onlinepowers.framework.web.pagination.Pagination;
import saleson.shop.pointcheck.domain.PointCheck;
import saleson.shop.pointcheck.support.PointCheckParam;

@Service("pointCheckService")
public class PointCheckServiceImpl implements PointCheckService {
	private static final Logger log = LoggerFactory.getLogger(PointCheckServiceImpl.class);
	
	@Autowired
    private PointCheckMapper pointCheckMapper;

    @Override
    public List<PointCheck> getPointCheckList(PointCheckParam pointCheckParam) {
    	
    	if (pointCheckParam.getPage() == 0) {
    		pointCheckParam.setPage(1);
		}
		
		int totalCount = pointCheckMapper.getPointCheckListCount(pointCheckParam);
		
		Pagination pagination = Pagination.getInstance(totalCount, pointCheckParam.getItemsPerPage());
		pointCheckParam.setPagination(pagination);

		List<PointCheck> list = pointCheckMapper.getPointCheckList(pointCheckParam);
		

		return list;
    }
}
