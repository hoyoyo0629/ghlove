package saleson.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.onlinepowers.framework.web.opmanager.menu.domain.Menu;

public class EmptypageMenu implements Serializable, Cloneable {

	private String authority;
	
	private String cacheKey;
	
	private List<EmptypageMenu> childMenu;
	
	private String displayFlag;
	
	private String menuCode;
	
	private String menuIcon;
	
	private int menuId;
	
	private String menuName;
	
	private int menuParentId;
	
	private int menuSeq;
	
	private int menuType;
	
	private String menuUrl;
	
	private String statusCode;
	
	private long userId;
	
	/**
	 * OpmanagerMenu session 저장용
	 */
	private static final long serialVersionUID = 1230383929712215118L;

	public EmptypageMenu() {
		super();
	}
	
	public void setMenu(Menu menu) {
		if (menu != null) {
			setAuthority(menu.getAuthority());
			setCacheKey(menu.getCacheKey());
			setChildMenu(menu.getChildMenu());
			setDisplayFlag(menu.getDisplayFlag());
			setMenuCode(menu.getMenuCode());
			setMenuIcon(menu.getMenuIcon());
			setMenuId(menu.getMenuId());
			setMenuName(menu.getMenuName());
			setMenuParentId(menu.getMenuParentId());
			setMenuSeq(menu.getMenuSeq());
			setMenuType(menu.getMenuType());
			setMenuUrl(menu.getMenuUrl());
			setStatusCode(menu.getStatusCode());
			setUserId(menu.getUserId());
		} else {
			setAuthority(null);
			setCacheKey(null);
			setChildMenu(null);
			setDisplayFlag(null);
			setMenuCode(null);
			setMenuIcon(null);
			setMenuId(0);
			setMenuName(null);
			setMenuParentId(0);
			setMenuSeq(0);
			setMenuType(0);
			setMenuUrl(null);
			setStatusCode(null);
			setUserId(0);
		}
	}

	public List<Menu> getChildMenu() {
		if (this.childMenu == null) {
			return null;
		} else {
			List<Menu> menuList = new ArrayList<>();
			for (EmptypageMenu menu : this.childMenu) {
//				Menu addMenu = new Menu();
//				addMenu.setAuthority(menu.getAuthority());
//				addMenu.setCacheKey(menu.getCacheKey());
//				addMenu.setDisplayFlag(menu.getDisplayFlag());
//				addMenu.setMenuCode(menu.getMenuCode());
//				addMenu.setMenuIcon(menu.getMenuIcon());
//				addMenu.setMenuId(menu.getMenuId());
//				addMenu.setMenuName(menu.getMenuName());
//				addMenu.setMenuParentId(menu.getMenuParentId());
//				addMenu.setMenuSeq(menu.getMenuSeq());
//				addMenu.setMenuType(menu.getMenuType());
//				addMenu.setMenuUrl(menu.getMenuUrl());
//				addMenu.setStatusCode(menu.getStatusCode());
//				addMenu.setUserId(menu.getUserId());
//				
//				menuList.add(addMenu);
				menuList.add(menu.getMenu());
			}
			
			return menuList;
		}
	}

	public void setChildMenu(List<Menu> childMenu) {
		if (childMenu == null) {
			this.childMenu = null;
		} else {
			this.childMenu = new ArrayList<>();
			for (Menu menu : childMenu) {
				EmptypageMenu addMenu = new EmptypageMenu();
				addMenu.setAuthority(menu.getAuthority());
				addMenu.setCacheKey(menu.getCacheKey());
				addMenu.setDisplayFlag(menu.getDisplayFlag());
				addMenu.setMenuCode(menu.getMenuCode());
				addMenu.setMenuIcon(menu.getMenuIcon());
				addMenu.setMenuId(menu.getMenuId());
				addMenu.setMenuName(menu.getMenuName());
				addMenu.setMenuParentId(menu.getMenuParentId());
				addMenu.setMenuSeq(menu.getMenuSeq());
				addMenu.setMenuType(menu.getMenuType());
				addMenu.setMenuUrl(menu.getMenuUrl());
				addMenu.setStatusCode(menu.getStatusCode());
				addMenu.setUserId(menu.getUserId());
				
				this.childMenu.add(addMenu);
			}
		}
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getMenuParentId() {
		return menuParentId;
	}

	public void setMenuParentId(int menuParentId) {
		this.menuParentId = menuParentId;
	}

	public int getMenuSeq() {
		return menuSeq;
	}

	public void setMenuSeq(int menuSeq) {
		this.menuSeq = menuSeq;
	}

	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Menu getMenu() {
		Menu menu = new Menu();
		menu.setAuthority(authority);
		menu.setCacheKey(cacheKey);
		menu.setChildMenu(getChildMenu());
		menu.setDisplayFlag(displayFlag);
		menu.setMenuCode(menuCode);
		menu.setMenuIcon(menuIcon);
		menu.setMenuId(menuId);
		menu.setMenuName(menuName);
		menu.setMenuParentId(menuParentId);
		menu.setMenuSeq(menuSeq);
		menu.setMenuType(menuType);
		menu.setMenuUrl(menuUrl);
		menu.setStatusCode(statusCode);
		menu.setUserId(userId);
		
		return menu;
	}
	
}
