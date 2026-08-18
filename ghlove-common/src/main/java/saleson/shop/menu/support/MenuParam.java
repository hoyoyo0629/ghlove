package saleson.shop.menu.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class MenuParam extends SearchParam {
	
	private Integer menuId;
	private Integer menuParentId;
	private Integer menuType;
	private String menuName;
	private String menuCode;
	private String menuUrl;
	private Integer menuSeq;
	private String menuIcon;
	private String displayFlag;
	private String statusCode;
	private Integer menuChild;
	private String menuGubun;
	private String menuPath;
	
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public Integer getMenuParentId() {
		return menuParentId;
	}
	public void setMenuParentId(Integer menuParentId) {
		this.menuParentId = menuParentId;
	}
	public Integer getMenuType() {
		return menuType;
	}
	public void setMenuType(Integer menuType) {
		this.menuType = menuType;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public Integer getMenuSeq() {
		return menuSeq;
	}
	public void setMenuSeq(Integer menuSeq) {
		this.menuSeq = menuSeq;
	}
	public String getMenuIcon() {
		return menuIcon;
	}
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}
	public String getDisplayFlag() {
		return displayFlag;
	}
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public Integer getMenuChild() {
		return menuChild;
	}
	public void setMenuChild(Integer menuChild) {
		this.menuChild = menuChild;
	}
	public String getMenuGubun() {
		return menuGubun;
	}
	public void setMenuGubun(String menuGubun) {
		this.menuGubun = menuGubun;
	}
	public String getMenuPath() {
		return menuPath;
	}
	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}
	
}
