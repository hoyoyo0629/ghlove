package saleson.shop.security;

import com.onlinepowers.framework.security.mapper.SecurityMapper;
import com.onlinepowers.framework.security.userdetails.User;
import lombok.RequiredArgsConstructor;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service("shopSecurityService")
@RequiredArgsConstructor
public class ShopSecurityServiceImpl extends EgovAbstractServiceImpl implements ShopSecurityService {

    private final SecurityMapper securityMapper;

    @Override
    public void updateLoginCount(User user) throws DataAccessException {
        securityMapper.updateLoginCount(user);
    }

    @Override
    public void updateClearLoginFailCountForUser(String loginId) {
        securityMapper.updateClearLoginFailCountForUser(loginId);
    }

    @Override
    public void updateLoginFailCountForUser(String loginId) {
        securityMapper.updateLoginFailCountForUser(loginId);
    }
}
