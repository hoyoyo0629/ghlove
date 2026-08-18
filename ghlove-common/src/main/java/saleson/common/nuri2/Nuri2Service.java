package saleson.common.nuri2;

import java.util.List;
import java.util.Map;

public interface Nuri2Service {
	void insertAlimtalk(Nuri2NrmsgData nuri2NrmsgData);

	List<Map<String,Object>> selectUserInfoList();

	List<Map<String,Object>> selectPresentOrderList();

}
