package saleson.shop.manual;

import java.util.HashMap;
import java.util.List;

import com.onlinepowers.framework.web.opmanager.menu.domain.Menu;

import saleson.shop.manual.domain.Manual;
import saleson.shop.manual.support.ManualParam;

public interface ManualService {

	/**
	 * 자료실 카운트
	 * @param manualParam
	 * @return
	 */
	public int getManualCount(ManualParam manualParam);

	/**
	 * 자료실 리스트(사용자)
	 * @param manualParam
	 * @return
	 */
	public List<Manual> getManualList(ManualParam manualParam);

	/**
	 * 자료실 조회
	 * @param dataId
	 * @return
	 */
	public Manual getManual(int mnlSn);
	public void insertManual(Manual databoard);
	public void insertManagerManual(Manual databoard);
	public void updateManual(Manual databoard);
	public void deleteManual(int mnlSn);

	/**
	 * 리스트 삭제
	 * @param manualParam
	 * @return
	 */
	public String manualListDelete(ManualParam manualParam);


	/**
	 * 첨부파일 삭제
	 * @param dataFileId
	 * @return
	 */
	public void deleteItemImageByItemId(Integer dataFileId);
	public void managerDeleteItemImageByItemId(String menuId);


	public List<HashMap<String, Object>> getAllMenuList();

	public Manual getOpMenu(String menuId);


}
