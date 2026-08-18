package saleson.api.common.enumerated;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.onlinepowers.framework.security.userdetails.UserRole;

import saleson.common.utils.UserUtils;

public enum UserAdminRole {
	
	LOC("지자체 관리자", Arrays.asList("ROLE_ADMIN_5","ROLE_ADMIN_6")),
	MOIS("행안부 관리자", Arrays.asList("ROLE_ADMIN_3","ROLE_ADMIN_4")),
	OFF("오프라인 관리자", Arrays.asList("ROLE_ADMIN_7","ROLE_ADMIN_8")),
	SYS("시스템 관리자", Collections.EMPTY_LIST);
	
	private String title;
	private List<String> list;
	
	UserAdminRole(String title, List<String> list) {
		this.title = title;
		this.list = list;
	}
	
	/**
	 * 현재 사용자의 role을 가져온다
	 * @return UserAdminRole
	 */
	public static UserAdminRole findByUserRole() {
		List<UserRole> userRoleList = UserUtils.getUser().getUserRoles();
		return findByUserRole(userRoleList);
		
	}
	
	/**
	 * roleList의 role을 가져온다
	 * @param userRoleList
	 * @return UserAdminRole
	 */
	public static UserAdminRole findByUserRole(List<UserRole> userRoleList) {
		UserAdminRole uar = SYS;
		if(userRoleList != null) {
			for(UserRole userRole : userRoleList) {
				String auth = userRole.getAuthority();
				if(auth != null && auth.indexOf("ROLE_ADMIN_") > -1) {
					uar = Arrays.stream(UserAdminRole.values()).filter(userAdminRole -> userAdminRole.isUserRole(auth)).findAny().orElse(SYS);
					break;
				};				
			};
		}
		
		return uar;
	}
	
	public boolean isUserRole(String code) {
		return list.stream().anyMatch(u -> u.equals(code));
	}
	
}
