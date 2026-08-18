package saleson.shop.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserParent  {

	private long parentId;
	private long userId;
	private String userName;
	private String gender;
	private String birthday;
	private String national_info;
	private String ci;
	private String di;
	private String dn;
	private String cellCorp;
	private String cellNo;
	private String createdAt;
	
	public String gender() {
		if(gender == "M") {
			gender = "0";
		}else if(gender == "W") {
			gender = "1";
		}
		return gender;
	}
	
}
