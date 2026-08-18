package saleson.shop.log;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.DateUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import saleson.common.Const;
import saleson.shop.log.domain.PasswordLog;

import java.util.List;

@Service("passwordLogService")
public class PasswordLogServiceImpl extends EgovAbstractServiceImpl implements PasswordLogService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordLogMapper passwordLogMapper;


    @Override
    public void insertManagerPasswordLog(User user, boolean isPopup) {

        String password = isPopup ? passwordEncoder.encode(user.getPassword()) : user.getPassword();

        PasswordLog passwordLog = new PasswordLog();
        passwordLog.setUserId(user.getUserId());
        passwordLog.setPassword(password);
        passwordLog.setCreatedDate(DateUtils.getToday(Const.DATETIME_FORMAT));

        passwordLogMapper.insertManagerPasswordLog(passwordLog);
    }

    @Override
    public List<String> getPasswordListById(PasswordLog passwordLog) {
        return passwordLogMapper.getPasswordListById(passwordLog);
    }
}
