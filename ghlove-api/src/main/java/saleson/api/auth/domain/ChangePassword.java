package saleson.api.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {

    private String authToken;
    private String requestToken;
    private String password;
    private String corfirmPassword;
    private String originalPassword;
    private String mberCi;

}
