package saleson.shop.log;

import com.onlinepowers.framework.security.userdetails.User;
import saleson.shop.log.domain.PasswordLog;

import java.util.List;

public interface PasswordLogService {

    void insertManagerPasswordLog(User user, boolean isPopup);

    List<String> getPasswordListById(PasswordLog passwordLog);

}
