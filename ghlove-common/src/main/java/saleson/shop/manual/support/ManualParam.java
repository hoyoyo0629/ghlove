package saleson.shop.manual.support;

import java.util.List;

import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.common.utils.CommonUtils;

@SuppressWarnings("serial")
public class ManualParam extends SearchParam {
	private int mnlSn;
	private String pageGbn;
	private String menuSj;
	private String menuCn;
	private String startCreateDate;
	private String endCreateDate;

	private String menuId;
	private String menuName;



	/* 일괄삭제기능 & 일괄 다운로드 사용 */
	private List<String> manualList;

	public int getMnlSn() {
		return mnlSn;
	}

	public void setMnlSn(int mnlSn) {
		this.mnlSn = mnlSn;
	}

	public String getPageGbn() {
		return pageGbn;
	}

	public void setPageGbn(String pageGbn) {
		this.pageGbn = pageGbn;
	}

	public String getMenuSj() {
		return menuSj;
	}

	public void setMenuSj(String menuSj) {
		this.menuSj = menuSj;
	}

	public String getMenuCn() {
		return menuCn;
	}

	public void setMenuCn(String menuCn) {
		this.menuCn = menuCn;
	}

	public String getStartCreateDate() {
		return startCreateDate;
	}

	public void setStartCreateDate(String startCreateDate) {
		this.startCreateDate = startCreateDate;
	}

	public String getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(String endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public List<String> getManualList() {
		return manualList;
	}

	public void setManualList(List<String> manualList) {
		this.manualList = manualList;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}




}
