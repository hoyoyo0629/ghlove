package saleson.shop.pointcheck;

import java.util.List;

import saleson.shop.pointcheck.domain.PointCheck;
import saleson.shop.pointcheck.support.PointCheckParam;

public interface PointCheckService {
	 /**
     * 포인트 정합성 조회
     * @param batchJobParam
     * @return
     */
	public List<PointCheck> getPointCheckList(PointCheckParam pointCheckParam);
}
