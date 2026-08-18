package saleson.shop.menu;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.shop.menu.domain.Menu;
import saleson.shop.menu.support.MenuParam;

import java.util.List;
import java.util.Map;


@Service("menuManagerService")
public class MenuServiceImpl extends EgovAbstractServiceImpl implements MenuService {

	@Autowired
	private MenuMapper menuMapper;

	@Override
	public int getMenuCount(MenuParam menuParam) {
		return menuMapper.getMenuCount(menuParam);
	}

	@Override
	public List<Menu> getManagerMenuList(MenuParam menuParam) {
		return menuMapper.getManagerMenuList(menuParam);
	}
	
	@Override
	public List<Menu> getSellerMenuList(MenuParam menuParam) {
		return menuMapper.getSellerMenuList(menuParam);
	}	
	
	@Override
	public Menu getMenuById(Map<String, Object> params) {
		return menuMapper.getMenuById(params);
	}

	@Override
	public void insertMenu(Menu menu) {
		menuMapper.insertMenu(menu);
	}
	
	@Override
	public void updateMenu(Menu menu) {
		menuMapper.updateMenu(menu);
	}	
	
	@Override
	public void deleteMenu(Menu menu) {
		menuMapper.deleteMenu(menu);
	}
	
	@Override
	public void insertMenuCode(Menu menu) {
		menuMapper.insertMenuCode(menu);
	}
	
	@Override
	public void updateMenuCode(Menu menu) {
		menuMapper.updateMenuCode(menu);
	}
	
	@Override
	public void deleteMenuCode(Menu menu) {
		menuMapper.deleteMenuCode(menu);
	}	
	
	@Override
	public List<Menu> getFirstMenuList(Menu menu) {
		return menuMapper.getFirstMenuList(menu);
	}	
	
	@Override
	public List<Menu> getSecondMenuList(Menu menu) {
		return menuMapper.getSecondMenuList(menu);
	}		
	
	@Override
	public int getMenuId(Menu menu) {
		return menuMapper.getMenuId(menu);
	}	
	
	@Override
	public void savePage(Menu menu) {
		menuMapper.savePage(menu);
	}	
}
