package saleson.shop.pointcheck;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.pointcheck.domain.PointCheck;
import saleson.shop.pointcheck.support.PointCheckParam;

@Mapper("PointCheckMapper")
public interface PointCheckMapper {
	 /**
     * 포인트 정합성 조회
     * @param PointCheckParam
     * @return
     */
    public List<PointCheck> getPointCheckList(PointCheckParam pointCheckParam);

    /**
     * 포인트 정합성 조회 카운트
     * @param PointCheckParam
     * @return
     */
    public int getPointCheckListCount(PointCheckParam pointCheckParam);
}
