package saleson.shop.item.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.shop.item.support.ItemParam;

@Slf4j
@Component
public class ItemReviewCriteriaEncryptor extends BaseDataEncryptor implements DataEncryptor<ItemParam> {

    private final String USER_NAME = "USER_NAME";

    private Cryptor cryptor;

    public ItemReviewCriteriaEncryptor(Cryptor cryptor) {
        super(cryptor);
        this.cryptor = cryptor;
    }

    @Override
    public void encrypt(ItemParam criteria) {

        if (StringUtils.hasText(criteria.getWhere())
                && StringUtils.hasText(criteria.getQuery())) {

            if (USER_NAME.equals(criteria.getWhere()) ) {

                criteria.setQuery(encrypt(criteria.getQuery(), 100));
            }
        }

    }


    @Override
    public void decrypt(ItemParam criteria, boolean needMasking) {
        if (StringUtils.hasText(criteria.getWhere())
                && StringUtils.hasText(criteria.getQuery())) {

            if (USER_NAME.equals(criteria.getWhere())) {
                criteria.setQuery(decrypt(criteria.getQuery()));
            }
        }
    }

    @Override
    public void masking(ItemParam object) {

    }
}
