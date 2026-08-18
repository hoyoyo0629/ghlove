package saleson.shop.ums.support;

import com.onlinepowers.framework.security.userdetails.User;
import saleson.common.enumeration.UmsType;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.model.Ums;
import saleson.common.notification.domain.UmsTemplate;

import java.util.HashMap;

public class UserSms extends UmsTemplate {

    private User user;

    public UserSms(Cryptor cryptor) {
        super(cryptor);
        intiCodeMapView();
    }

    public UserSms(User user, Ums ums, String phoneNumber, Cryptor cryptor, DataMasking dataMasking) {
        super(ums, phoneNumber, cryptor, dataMasking);

        if (user == null) {
            return;
        }

        this.user = user;
        intiCodeMapView();
        intiCodeMap();

        super.initialize(user.getUserId());
    }

    private void intiCodeMapView() {

        HashMap<String, String> map = new HashMap<>();

        map.put("user_name", "이름");

        addCodeViewMap(map);
    }

    private void intiCodeMap() {

        HashMap<String, String> map = new HashMap<>();

        map.put("user_name", getDataMasking().mask(getCryptor().decrypt(user.getUserName()), Masking.NAME));

        addCodeMap(map);

    }

}
