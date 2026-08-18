package saleson.api.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendAuthNumber {

    private String loginId;
    private String userName;
    private String phoneNumber;

    private boolean isDuplicateCheck;

}
