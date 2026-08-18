package saleson.shop.menu;

import java.util.List;
import java.util.Map;

import saleson.shop.menu.domain.Menu;
import saleson.shop.menu.support.MenuParam;

public interface MenuService {

	/**
	 * Menu 카운트 조회
	 * @param menuParam
	 * @return
	 */
	int getMenuCount(MenuParam menuParam);

	/**
	 * 관리자 메뉴 리스트 조회
	 * @param menuParam
	 * @return
	 */
	List<Menu> getManagerMenuList(MenuParam menuParam);
	
	/**
	 * 답례품 관리자 메뉴 리스트 조회
	 * @param menu
	 * @return
	 */
	List<Menu> getSellerMenuList(MenuParam menuParam);	
	
	/**
	 * 메뉴 상세조회
	 * @param params
	 * @return
	 */
	Menu getMenuById(Map<String, Object> params);
	
	/**
	 * 메뉴 등록
	 * @param menu
	 */
	void insertMenu(Menu menu);
	
	/**
	 * 메뉴 수정
	 * @param menu
	 */
	void updateMenu(Menu menu);	
	
	/**
	 * 메뉴 삭제
	 * @param menu
	 */
	void deleteMenu(Menu menu);	
	
	/**
	 * 메뉴코드 등록
	 * @param menu
	 */
	void insertMenuCode(Menu menu);
	
	/**
	 * 메뉴코드 수정
	 * @param menu
	 */
	void updateMenuCode(Menu menu);		
	
	/**
	 * 메뉴코드 삭제
	 * @param menu
	 */
	void deleteMenuCode(Menu menu);			
	
	/**
	 * 1레벨 메뉴 조회
	 * @param menuParam
	 * @return
	 */
	public List<Menu> getFirstMenuList(Menu menu);	
	
	/**
	 * 2레벨 메뉴 조회
	 * @param menuParam
	 * @return
	 */
	public List<Menu> getSecondMenuList(Menu menu);		
	
	/**
	 * 메뉴ID 채번
	 * @param menuParam
	 * @return
	 */	
	public int getMenuId(Menu menu);
	
	/**
	 * 페이지 저장
	 * @param menu
	 */
	void savePage(Menu menu);		
	
}
