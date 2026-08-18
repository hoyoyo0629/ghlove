package saleson.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.onlinepowers.framework.web.opmanager.menu.domain.Menu;

public class EmptyPageOpmanagerMenu implements Serializable {

	
	/**
	 * OpmanagerMenu session 저장용
	 */
	private static final long serialVersionUID = -1701166466975022684L;
	
	private List<EmptypageMenu> firstEmptyPageMenu;
	
	private List<EmptypageMenu> secondEmptyPageMenu;
	
	private int firstMenuId;
	
	private String menuCode;

	public EmptyPageOpmanagerMenu() {
		
	}

	public List<EmptypageMenu> getFirstEmptyPageMenu() {
		if (firstEmptyPageMenu == null) {
			return null;
		} else {
			List<EmptypageMenu> copy = new ArrayList<>();
			
			for (EmptypageMenu emptypageMenu : firstEmptyPageMenu) {
				copy.add((EmptypageMenu) emptypageMenu.clone());
			}
			
			return copy;
		}
	}

	public void setFirstEmptyPageMenu(List<EmptypageMenu> firstEmptyPageMenu) {
		if (firstEmptyPageMenu == null) {
			this.firstEmptyPageMenu = null;
		} else {
			this.firstEmptyPageMenu = new ArrayList<>();
			
			for (EmptypageMenu emptypageMenu : firstEmptyPageMenu) {
				this.firstEmptyPageMenu.add((EmptypageMenu) emptypageMenu.clone());
			}
		}
	}

	public List<EmptypageMenu> getSecondEmptyPageMenu() {
		if (secondEmptyPageMenu == null) {
			return null;
		} else {
			List<EmptypageMenu> copy = new ArrayList<>();
			
			for (EmptypageMenu emptypageMenu : secondEmptyPageMenu) {
				copy.add((EmptypageMenu) emptypageMenu.clone());
			}
			
			return copy;
		}
	}

	public void setSecondEmptyPageMenu(List<EmptypageMenu> secondEmptyPageMenu) {
		if (secondEmptyPageMenu == null) {
			this.secondEmptyPageMenu = null;
		} else {
			this.secondEmptyPageMenu = new ArrayList<>();
			
			for (EmptypageMenu emptypageMenu : secondEmptyPageMenu) {
				this.secondEmptyPageMenu.add((EmptypageMenu) emptypageMenu.clone());
			}
		}
	}

	public List<Menu> getFirstMenu() {
		if (firstEmptyPageMenu == null) {
			return null;
		} else {
			List<Menu> copy = new ArrayList<>();
			
			for (EmptypageMenu emptypageMenu : firstEmptyPageMenu) {
				copy.add(emptypageMenu.getMenu());
			}
			
			return copy;
		}
	}

	public List<Menu> getSecondMenu() {
		if (secondEmptyPageMenu == null) {
			return null;
		} else {
			List<Menu> copy = new ArrayList<>();
			
			for (EmptypageMenu emptypageMenu : secondEmptyPageMenu) {
				copy.add(emptypageMenu.getMenu());
			}
			
			return copy;
		}
	}

	public int getFirstMenuId() {
		return firstMenuId;
	}

	public void setFirstMenuId(int firstMenuId) {
		this.firstMenuId = firstMenuId;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	
}
