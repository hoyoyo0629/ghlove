package saleson.shop.transfer;

import java.util.HashMap;
import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.offgive.domain.Manager;
import saleson.shop.offgive.domain.Offgive;

@Mapper("transferMapper")
public interface TransferMapper {

//	public List<HashMap<String, Object>> getTempItemImageMigTablesGroupByItemIdList(HashMap<String, Object> map);
	public List<HashMap<String, Object>> getTempItemImageMigTablesByItemIdList(HashMap<String, Object> map);
//	public List<HashMap<String, Object>> getTempItemImageMigTablesByItemIdList();
	public String getItemCode(String item_id);
	public int insertOpItemImage(HashMap<String, Object> map);
	public int updateTempItemImageMigTables(HashMap<String, Object> map);
	public int updateOpItem(HashMap<String, Object> map);

	// 테스트 용도
	public String getTempItemCode(String item_id);
	public int insertTempOpItemImage(HashMap<String, Object> map);
	public int updateTempOpItem(HashMap<String, Object> map);


}
