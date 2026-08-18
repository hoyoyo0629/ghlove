package saleson.api.auth.domain;

import com.onlinepowers.framework.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsisRequestAuth {

    private long userId;
    private String loginId;
    private String password;
    private String time;

    public AsisRequestAuth(String loginId) {
        this(loginId,"");
    }

    public AsisRequestAuth(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
        this.time = DateUtils.getToday("yyyyMMddHHmmss");
    }
}
