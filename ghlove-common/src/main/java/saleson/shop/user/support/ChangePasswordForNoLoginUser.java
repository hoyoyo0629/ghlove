package saleson.shop.user.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordForNoLoginUser {

    private String userName;
    private String loginId;
    private String mberCi;
    private String password;
    private String corfirmPassword;
    
    private long userId;
    private String passwordExpiredDate;
    private String mberDn;
    
}
