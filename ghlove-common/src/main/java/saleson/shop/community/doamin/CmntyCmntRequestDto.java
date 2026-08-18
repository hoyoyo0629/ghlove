package saleson.shop.community.doamin;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CmntyCmntRequestDto extends SearchParam {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private long bbsId;

}