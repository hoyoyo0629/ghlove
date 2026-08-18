package saleson.shop.usergroup.support;

import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.domain.SearchParam;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.ItemUtils;

@SuppressWarnings("serial")
public class UserGroupParam extends SearchParam {
	private String groupCode;
	private String groupName;
	private String prefix;
	private String userAuthority;
	
	
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	public String getPrefix() {
		if (ObjectUtils.isEmpty(prefix)) {
			prefix = ItemUtils.PREFIX_GROUP;
		}

		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getUserAuthority() {
		return userAuthority;
	}
	public void setUserAuthority(String userAuthority) {
		this.userAuthority = userAuthority;
	}	
	
}
