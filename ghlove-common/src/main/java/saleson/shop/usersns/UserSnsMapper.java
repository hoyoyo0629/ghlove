package saleson.shop.usersns;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.onlinepowers.framework.security.userdetails.User;
import saleson.shop.usersns.domain.UserSns;

import java.util.List;

@Mapper("userSnsMapper")
public interface UserSnsMapper {

    // sns연결 정보 받아오기 - SNS 연동 설정
    List<UserSns> getUserSnsList(UserSns userSns);

    // 탈퇴시 SNS인증 내역 삭제 - OP_USER_SNS
    void secedeSnsProcess(User user);

    // loginId, userName update
    void updateUser(User user);

    // SNS -> 쇼핑몰 아이디로 인증일 update (OP_USER_SNS)
    void updateUserSns(User user);

    // sns 연결 끊기
    void disconnectSns(UserSns userSns);

    // email로 등록 된 아이디가 있는지 확인
    int getDuplicatedUserCount(UserSns userSns);

    // SNS 계정정보 등록
    int insertUserSnsInfo(UserSns userSns);

    UserSns getUserSns(UserSns userSns);

    UserSns getUserSnsInfo(UserSns userSns);

}
