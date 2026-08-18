package saleson.shop.campaign.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.campaign.support.CampaignUserParam;

@Slf4j
@Component
public class CampaignUserParamEncryptor extends BaseDataEncryptor implements DataEncryptor<CampaignUserParam> {

    private Cryptor cryptor;
    private DataMasking dataMasking;

    public CampaignUserParamEncryptor(Cryptor cryptor, DataMasking dataMasking) {
        super(cryptor);
        this.cryptor = cryptor;
        this.dataMasking = dataMasking;
    }

    @Override
    public void encrypt(CampaignUserParam campaignUserParam) {
        String userName = campaignUserParam.getUserName();
        if ("%%".equals(userName)) {
            userName = userName.replaceAll("%%", "");
        }

        campaignUserParam.setUserName(encrypt(userName, 100));
    }

    @Override
    public void decrypt(CampaignUserParam campaignUserParam, boolean needMasking) {
        campaignUserParam.setUserName(decrypt(campaignUserParam.getUserName()));

        if (needMasking) masking(campaignUserParam);
    }

    @Override
    public void masking(CampaignUserParam campaignUserParam) {
        campaignUserParam.setUserName(dataMasking.mask(campaignUserParam.getUserName(), Masking.NAME));
    }
}
