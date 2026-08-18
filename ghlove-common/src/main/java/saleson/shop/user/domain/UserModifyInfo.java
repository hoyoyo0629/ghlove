package saleson.shop.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyInfo {

    private String locgovCode;
    private String upperLocgovNm;
    private String locgovNm;
    private String userName;
    private String loginId;
    private String phoneNumber;
    private String email;
    private String birthday;
    private String post;
    private String address;
    private String addressDetail;
    private String receiveEmail;
    private String receiveSms;
    private String receivePbanc;
    private String receiveKakao;
    private String userKeyYN;
    private String mberCiYN;
    private String loginPathCode;
    private String kakaoUserKeyYN;

}