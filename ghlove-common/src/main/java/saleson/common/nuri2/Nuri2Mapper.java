package saleson.common.nuri2;

import java.util.List;
import java.util.Map;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("nuri2Mapper")
public interface Nuri2Mapper {
	void insertTifAlimtalkInfo(Nuri2NrmsgData nuri2NrmsgData);

	List<Map<String,Object>> selectUserInfoList();

	List<Map<String,Object>> selectPresentOrderList();
}
