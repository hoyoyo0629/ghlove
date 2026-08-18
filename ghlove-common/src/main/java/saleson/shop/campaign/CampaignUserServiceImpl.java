package saleson.shop.campaign;

import com.querydsl.core.types.Predicate;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import saleson.common.security.crypto.Decryptor;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.model.campaign.CampaignUser;
import saleson.shop.campaign.support.CampaignUserDto;

import java.util.Optional;


@Service("CampaignUserService")
public class CampaignUserServiceImpl extends EgovAbstractServiceImpl implements CampaignUserService {

    private static final Logger logger = LoggerFactory.getLogger(CampaignUserServiceImpl.class);

    @Autowired
    private CampaignUserRepository campaignUserRepository;

    @Autowired
    private Decryptor decryptor;

    @Override
    public Page<CampaignUser> findAll(Predicate predicate, Pageable pageable) {
        return campaignUserRepository.findAll(predicate, pageable);
    }

    @Override
    public Page<CampaignUser> getCampaignUserList(Predicate predicate, Pageable pageable) {
        Page<CampaignUser> userList = campaignUserRepository.findAll(predicate, pageable);
        userList.forEach(this::decryptCampaignUsers);
        return userList;
    }

    @Override
    public void updateCampaignUserRedirection(long campaignId, long userId) {
        if (campaignId > 0 && userId > 0) {
            CampaignUserDto dto = new CampaignUserDto();
            dto.setCampaignId(campaignId);
            dto.setUserId(userId);

            Optional<CampaignUser> storeCampaignUser = campaignUserRepository.findOne(dto.getPredicate());

            if (storeCampaignUser.isPresent()) {
                long userRedirection = CommonUtils.longNvl(storeCampaignUser.get().getRedirection());
                campaignUserRepository.updateCampaignUserRedirection(campaignId, userId, userRedirection + 1);
            }
        }
    }

    private void decryptCampaignUsers(CampaignUser c) {
        boolean needMasking = ShopUtils.needMasking();
        c.setUserName(decryptor.decryptName(c.getUserName(), needMasking));
        c.setPhoneNumber(decryptor.decryptPhone(c.getPhoneNumber(), needMasking));
    }
}
