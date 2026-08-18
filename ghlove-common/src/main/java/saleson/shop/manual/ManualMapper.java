package saleson.shop.manual;

import java.util.HashMap;
import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.onlinepowers.framework.web.opmanager.menu.domain.Menu;

import saleson.shop.manual.domain.Manual;
import saleson.shop.manual.support.ManualParam;

@Mapper("manualMapper")
public interface ManualMapper {

	/**
	 * 메뉴얼사용자 카운트
	 * @param manualParam
	 * @return
	 */
	int getManualCount(ManualParam manualParam);

	/**
	 * 메뉴얼사용자 리스트(사용자)
	 * @param manualParam
	 * @return
	 */
	List<Manual> getManualList(ManualParam manualParam);

	/**
	 * 메뉴얼사용자 조회
	 * @param dataId
	 * @return
	 */
	Manual getManual(int dataId);
	void insertManual(Manual databoard);
	void insertManualFile(Manual databoardFile);
	void updateManual(Manual databoard);
	void updateManagerManual(Manual databoard);
	void deleteManual(int dataId);

	void deleteManualFile(Integer dataFileId);
	void managerDeleteManualFile(String menuId);

	Manual getManualByMenuSeCode(String menuSeCode);


	List<HashMap<String, Object>> getAllMenuListDept2(String authority);
	List<HashMap<String, Object>> getAllMenuListDept3(HashMap<String, Object> map);
	Manual getOpMenu(String menuId);
}
