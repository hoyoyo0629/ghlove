package saleson.api.auth.domain;

import com.onlinepowers.framework.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsisRequestGuestAuth {

    private String userName;
    private String phoneNumber;
    private String time;

    public AsisRequestGuestAuth(String userName, String phoneNumber) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.time = DateUtils.getToday("yyyyMMddHHmmss");
    }
}
