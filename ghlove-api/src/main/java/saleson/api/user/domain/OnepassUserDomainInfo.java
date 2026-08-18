package saleson.api.user.domain;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OnepassUserDomainInfo {

	 private long userId;
    @NotEmpty
    private String userName;
//    @NotEmpty
    private String loginId;
    @NotEmpty
    private String email;
    private String password;
    private String gender;
    private String telNumber;
    @NotEmpty
    private String phoneNumber;
    private String newPost;
    @NotEmpty
    private String post;
    @NotEmpty
    private String address;
    @NotEmpty
    private String addressDetail;
    private String birthdayType;
    @NotEmpty
    private String receiveEmail;
    @NotEmpty
    private String receiveSms;
    private String receivePbanc;

//    private boolean isSns;
    private boolean isAuth;

    private String requestToken;
    private String authNumber;

    private String[] locGovList;
    private String[] rtnpsntList;

    private String mberCi;
    private String mberDi;
    @NotEmpty
    private String locgovCode;
    private String birthdayFull;

    private String userKey;
    private String isOnepass;
	private String leaveCode;		// 탈퇴코드
    private String leaveReason;

}
