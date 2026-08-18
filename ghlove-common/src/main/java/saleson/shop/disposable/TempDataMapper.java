package saleson.shop.disposable;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.disposable.domain.TempData;

@Mapper("tempDataMapper")
public interface TempDataMapper {
	
	public int insertTempData(TempData tempData);
	
	public int deleteTempDataByDataId(TempData tempData);
	
	public int deleteTempData();
	
	public TempData selectTempData(TempData tempData);
	
	public int selectTempDataCnt(String dataId);
	
}
