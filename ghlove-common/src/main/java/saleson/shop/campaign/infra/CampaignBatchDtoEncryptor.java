package saleson.shop.campaign.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.shop.campaign.support.CampaignBatchDto;

@Slf4j
@Component
public class CampaignBatchDtoEncryptor extends BaseDataEncryptor implements DataEncryptor<CampaignBatchDto> {
    private final String USER_NAME = "userName";
    private Cryptor cryptor;

    public CampaignBatchDtoEncryptor(Cryptor cryptor) {
        super(cryptor);
        this.cryptor = cryptor;
    }

    @Override
    public void encrypt(CampaignBatchDto campaignBatchDto) {
        if (campaignBatchDto.getWhere() != null && !"".equals(campaignBatchDto.getWhere().trim())
                && campaignBatchDto.getQuery() != null && !"".equals(campaignBatchDto.getQuery().trim())) {

            if (USER_NAME.equals(campaignBatchDto.getWhere())) {
                campaignBatchDto.setQuery(encrypt(campaignBatchDto.getQuery(), 100));
            }
        }
    }

    @Override
    public void decrypt(CampaignBatchDto campaignBatchDto, boolean needMasking) {
        if (campaignBatchDto.getWhere() != null && !"".equals(campaignBatchDto.getWhere().trim())
            && campaignBatchDto.getQuery() != null && !"".equals(campaignBatchDto.getQuery().trim())) {

            if (USER_NAME.equals(campaignBatchDto.getWhere())) {
                campaignBatchDto.setQuery(decrypt(campaignBatchDto.getQuery()));
            }
        }

        if (needMasking) masking(campaignBatchDto);
    }

    @Override
    public void masking(CampaignBatchDto object) {
    }
}
