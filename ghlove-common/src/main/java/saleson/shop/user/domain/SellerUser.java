package saleson.shop.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.onlinepowers.framework.security.userdetails.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerUser extends User {
	
    private static final long serialVersionUID = -4198839810725515350L;
    
	private String pwdChgSellerYn;
	
	private String mberDn;
	
	private String mberFinDn;
	
	private long sellerId;
	
	private String mberCi;
	
	private String locgovName;
	
	private String receiveSms;
	
}
