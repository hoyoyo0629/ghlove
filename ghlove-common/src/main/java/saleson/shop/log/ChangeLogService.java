package saleson.shop.log;

import com.onlinepowers.framework.security.userdetails.User;
import saleson.shop.log.support.ChangeTypeEnum;

import javax.servlet.http.HttpServletRequest;

public interface ChangeLogService {

    void insertUserChangeLog(long userId, HttpServletRequest request);

    void insertManagerChangeLog(HttpServletRequest request, User user, ChangeTypeEnum type);

}
