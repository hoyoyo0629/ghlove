package saleson.shop.usergroup;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.domain.SearchParam;
import com.onlinepowers.framework.web.opmanager.menu.MenuService;
import com.onlinepowers.framework.web.opmanager.menu.domain.Menu;
import com.onlinepowers.framework.web.opmanager.menu.domain.MenuRight;
import com.onlinepowers.framework.web.opmanager.role.RoleService;
import com.onlinepowers.framework.web.opmanager.role.domain.Role;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import lombok.RequiredArgsConstructor;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import saleson.common.utils.UserUtils;
import saleson.shop.group.domain.Group;
import saleson.shop.group.support.GroupSearchParam;
import saleson.shop.usergroup.domain.UserGroup;
import saleson.shop.usergroup.support.UserGroupParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/opmanager/user-group")
@RequestProperty(title = "사용자권한그룹", layout = "default", template="opmanager")
@RequiredArgsConstructor
public class UserGroupController {
	private static final Logger log = LoggerFactory.getLogger(UserGroupController.class);

	@Autowired
	private UserGroupService userGroupService;

	private final RoleService roleService;
	private final MenuService menuService;

	/**
	 * 사용자권한그룹 리스트
	 * @param messageParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String list(RequestContext requestContext, Model model, UserGroupParam userGroupParam) {

		userGroupParam.setConditionType("ROLE_ADMIN");

		int totalCount = userGroupService.getUserGroupCount(userGroupParam);

		Pagination pagination = Pagination.getInstance(totalCount);
		userGroupParam.setPagination(pagination);

		model.addAttribute("list", userGroupService.getUserGroupList(userGroupParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("totalCount", totalCount);

		return ViewUtils.view();
	}

	/**
     * 사용자권한그룹 등록
     * @param model
     * @param
     * @return
     */
    @GetMapping("create")
    @RequestProperty(layout="base", title="권한그룹등록")
    public String groupInsert(Model model, Role role) {

        model.addAttribute("role", role);
        model.addAttribute("type", "등록");

        return ViewUtils.getManagerView("/user-group/form");
    }

    /**
     * 사용자권한그룹 등록 처리
     * @param group
     * @return
     */
    @PostMapping("create")
    public String groupInsertAction(Role role) {

    	role.setCreatedUserId(UserUtils.getLoginId());
        String message = MessageUtils.getMessage("M00632"); // 등록되었습니다
        String javascript = "opener.location.reload(); self.close();";

		roleService.insertRole(role);

        return ViewUtils.redirect("/opmanager/user-group/create", message, javascript);
    }

    /**
     * 사용자권한그룹 수정
     * @param model
     * @param
     * @return
     */
    @GetMapping("edit")
    @RequestProperty(layout="base", title="권한그룹수정")
    public String groupUpdate(Model model, Role role) {

    	role = roleService.getRoleByAuthority(role.getAuthority());

        model.addAttribute("role", role);
        model.addAttribute("type", "수정");

        return ViewUtils.getManagerView("/user-group/form");
    }

    /**
     * 사용자권한그룹 수정 처리
     * @param code
     * @return
     */
    @PostMapping("edit")
    public String groupUpdateAction(Role role) {

    	role.setUpdatedUserId(UserUtils.getLoginId());

    	roleService.updateRole(role);

        String javascript = "opener.location.reload(); self.close();";
        String message = MessageUtils.getMessage("M01673"); // 수정되었습니다 ;

        return ViewUtils.redirect("/opmanager/user-group/create", message, javascript);
    }

    /**
     * 사용자권한그룹 데이터 삭제
     * @param requestContext
     * @param group
     * @code
     */
    @PostMapping("delete")
    public JsonView deleteListData(RequestContext requestContext, Role role) {

    	ListParam listParam = new ListParam();
    	String[] authority = new String[1];
    	authority[0] = role.getAuthority();
    	listParam.setId(authority);

        if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		String[] notDeleteRoles = {
				"ROLE_EXCEL",
				"ROLE_ISMS",
				"ROLE_MD"
		};

		String[] ids = saleson.common.utils.CommonUtils.copy(listParam.getId());
		if (ids != null) {
			for (String s : notDeleteRoles) {
				long count = Arrays.stream(ids).filter(s1 -> s1.equals(s)).count();
				if (count > 0) {
					return JsonViewUtils.failure("삭제 불가능한 역할이 포함되어 있습니다.");
				}
			}
		}

		roleService.deleteListData(listParam);
		return JsonViewUtils.success();

    }

    /**
     * 사용자권한 관리
     * @param requestContext
     * @param group
     * @code
     */
    @GetMapping(value = "role/list")
	public String groupRoleList(RequestContext requestContext,Model model,UserGroupParam userGroupParam) {

    	String authority = userGroupParam.getUserAuthority();
    	Role role = roleService.getRoleByAuthority(authority);

		List<Menu> menuList = menuService.getAllMenuList();

		List<MenuRight> menuRightList = menuService.getMenuRightListByAuthority(authority);

		userGroupParam.setConditionType("ROLE_ADMIN");

		int totalCount = userGroupService.getUserGroupCount(userGroupParam);

		Pagination pagination = Pagination.getInstance(totalCount, Integer.MAX_VALUE);			// 권한 목록 전체 나오도록 수정
		userGroupParam.setPagination(pagination);

		model.addAttribute("menuList", menuList);
		model.addAttribute("menuRightList", menuRightList);
		model.addAttribute("role", role);
		model.addAttribute("list", userGroupService.getUserGroupList(userGroupParam));
		model.addAttribute("userGroupParam", userGroupParam);
		model.addAttribute("authority", authority);
		if(authority != null || authority == "") {
			model.addAttribute("roleName", String.valueOf(role.getRoleName()));
			model.addAttribute("roleDesc", String.valueOf(role.getRoleDesc()));
		}
		model.addAttribute("pagination", pagination);
		model.addAttribute("totalCount", totalCount);

		return "view:/user-group/role/list";
	}

    /**
     * 사용자권한그룹 등록 처리
     * @param group
     * @return
     */
    @PostMapping("role/list")
    public String groupRoleInsertAction(Role role) {

    	role.setCreatedUserId(UserUtils.getLoginId());
        String message = MessageUtils.getMessage("M00406"); // 저장되었습니다
        String javascript = "";

		roleService.updateRole(role);

        return ViewUtils.redirect("/opmanager/user-group/role/list", message, javascript);
    }

}