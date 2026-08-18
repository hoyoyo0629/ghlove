package saleson.shop.menu;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.shop.menu.domain.Menu;
import saleson.shop.menu.support.MenuParam;

@Controller
@RequestMapping("/opmanager/menu")
@RequestProperty(title="MENU", layout="default")
public class MenuManagerController {

	private static final Logger log = LoggerFactory.getLogger(MenuManagerController.class);

    @Autowired
    private MenuService menuService;

    /**
     * 메뉴 리스트 조회
     * @param menuParam
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String menuList(MenuParam menuParam, Model model) {

    	menuParam.setMenuGubun("OPMANAGER");
        int menuCount = menuService.getMenuCount(menuParam);

        Pagination pagination = Pagination.getInstance(menuCount);
        menuParam.setPagination(pagination);

        if(menuParam.getWhere() == null) {
        	// 기본 - 관리자 메뉴로 설정
        	menuParam.setWhere("OPMANAGER");
        }

        List<Menu> menuList = null;

        if(menuParam.getWhere().equals("OPMANAGER")) {	// 관리자 메뉴

        	menuList = menuService.getManagerMenuList(menuParam);

        } else if(menuParam.getWhere().equals("SELLER")) {	// 답례품 관리자 메뉴

        	menuList = menuService.getSellerMenuList(menuParam);

        }

        if (menuList != null && menuList.size() != 0) {
			for (Menu menu : menuList) {

					switch (menu.getStatusCode()) {
						case "1" : menu.setStatusCode(MessageUtils.getMessage("M00083")); // 사용
						break;
						case "2" : menu.setStatusCode(MessageUtils.getMessage("M00089")); // 사용안함
						break;
					}

			}
        }

        model.addAttribute("menuCount", menuCount);
        model.addAttribute("menuList", menuList);
        model.addAttribute("pagination", pagination);

    	return ViewUtils.view();
    }

    /**
     * 메뉴관리 - 등록화면
     * @param model
     * @param menu
     * @return
     */
    @GetMapping("create")
    @RequestProperty(layout="base", title="메뉴등록")
    public String menuInsert(Model model, Menu menu) {


    	if(!menu.getMenuGubun().equals("USER")) {
    		// 1레벨 메뉴조회
    		model.addAttribute("firstMenuList", menuService.getFirstMenuList(menu));
        	// 1레벨 메뉴ID 채번
        	menu.setMenuType(1);
        	int menuId = menuService.getMenuId(menu);
        	menu.setMenuId(menuId);
    	}

        model.addAttribute("menu", menu);

        return ViewUtils.getManagerView("/menu/form");
    }

    /**
     * 메뉴 등록
     * @param menu
     * @return
     */
    @PostMapping("create")
    public String menuInsertAction(Menu menu) {

    	menuService.insertMenu(menu);

    	if(menu.getMenuGubun().equals("OPMANAGER")) {
    		menuService.insertMenuCode(menu); // 관리자 메뉴일때는 op_common_message도 등록해야함
    	}

        String message = MessageUtils.getMessage("M00632"); // 등록되었습니다
        String javascript = "opener.fnMenuSearch(); self.close()";

        return ViewUtils.redirect("/opmanager/menu/create?menuGubun="+menu.getMenuGubun(), message, javascript);
    }

    /**
     * 메뉴관리 - 수정화면
     * @param model
     * @param menu
     * @return
     */
    @GetMapping("edit")
    @RequestProperty(layout="base", title="메뉴수정")
    public String menuUpdate(Model model, Menu menu) {

        Map<String, Object> params = new HashedMap();
        params.put("menuGubun", menu.getMenuGubun());
        params.put("menuId", menu.getMenuId());

		// 1레벨 메뉴조회
    	if(!menu.getMenuGubun().equals("USER")) {
    		model.addAttribute("firstMenuList", menuService.getFirstMenuList(menu));
    	}
        model.addAttribute("menu", menuService.getMenuById(params));

        return ViewUtils.getManagerView("/menu/form");
    }

    /**
     * 메뉴 수정
     * @param menu
     * @return
     */
    @PostMapping("edit")
    public String menuUpdateAction(Menu menu) {

    	menuService.updateMenu(menu);

    	if(menu.getMenuGubun().equals("OPMANAGER")) {
    		menuService.updateMenuCode(menu); // 관리자 메뉴일때는 op_common_message도 수정해야함
    	}

        String javascript = "opener.fnMenuSearch(); self.close()";
        String message = MessageUtils.getMessage("M01673"); // 수정되었습니다 ;

        return ViewUtils.redirect("/opmanager/menu/edit?menuGubun="+menu.getMenuGubun()+"&menuId="+menu.getMenuId(), message, javascript);
    }

    /**
     * 메뉴 삭제
     * @param requestContext
     * @param menu
     */
    @PostMapping("delete")
    public JsonView deleteMenu(RequestContext requestContext, Menu menu) {

        if (!requestContext.isAjaxRequest()) {
            throw new NotAjaxRequestException();
        }

        menuService.deleteMenu(menu);

    	if(menu.getMenuGubun().equals("OPMANAGER")) {
    		menuService.deleteMenuCode(menu); // 관리자 메뉴일때는 op_common_message도 삭제해야함
    	}

        return JsonViewUtils.success();
    }

	/**
	 * 2레벨 메뉴조회
	 * @param requestContext
	 * @param model
	 * @param menuParentId, menuGubun
	 * @return
	 */
	@PostMapping("/secondMenuList")
	public JsonView secondMenuList(RequestContext requestContext, Model model
			, @RequestParam("menuParentId") Integer menuParentId, @RequestParam("menuGubun") String menuGubun) {

		Menu menu = new Menu();
		menu.setMenuGubun(menuGubun);
		menu.setMenuParentId(menuParentId == null ? 0 : menuParentId);
		List<Menu> list = menuService.getSecondMenuList(menu);

		return JsonViewUtils.success(list);
	}


	/**
	 * 메뉴ID 채번
	 * @param requestContext
	 * @param model
	 * @param menuParentId, menuGubun, menuType
	 * @return
	 */
	@PostMapping("/menuId")
	public JsonView getMenuId(RequestContext requestContext, Model model
			, @RequestParam("menuParentId") String menuParentId, @RequestParam("menuGubun") String menuGubun
			, @RequestParam("menuType") Integer menuType) {

		Menu menu = new Menu();
		menu.setMenuGubun(menuGubun);
		menu.setMenuParentId(menuParentId.equals("") ? 0 : Integer.parseInt(menuParentId));
		menu.setMenuType(menuType);
		int menuId = menuService.getMenuId(menu);

		return JsonViewUtils.success(menuId);
	}


    /**
     * 메뉴관리 - 페이지관리
     * @param model
     * @param menu
     * @return
     */
    @GetMapping("page")
    public String page(Model model, Menu menu) {

        Map<String, Object> params = new HashedMap();
        params.put("menuGubun", menu.getMenuGubun());
        params.put("menuId", menu.getMenuId());

        model.addAttribute("menu", menuService.getMenuById(params));

        return ViewUtils.getManagerView("/menu/page");
    }

    /**
     * 페이지 저장
     * @param menu
     * @return
     */
    @PostMapping("page")
    public String savePage(Menu menu) {

    	menuService.savePage(menu);

        return ViewUtils.redirect("/opmanager/menu/list", MessageUtils.getMessage("M00406"));	// 저장되었습니다.
    }

    @PostMapping("/list")
    public String searchMenuList(MenuParam menuParam, Model model) {
    	return menuList(menuParam, model);
    }
}
