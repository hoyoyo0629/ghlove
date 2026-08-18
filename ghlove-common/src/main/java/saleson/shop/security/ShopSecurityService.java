package saleson.shop.security;

import com.onlinepowers.framework.security.userdetails.User;
import org.springframework.dao.DataAccessException;

public interface ShopSecurityService {

    void updateLoginCount(User user) throws DataAccessException;
    void updateClearLoginFailCountForUser(String loginId);
    void updateLoginFailCountForUser(String loginId);
}
