package saleson.api.auth.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FindUser {

    private String requestToken;
    private String authNumber;

    private String userName;
    private String phoneNumber;
    private String email;
    private String loginId;


}
