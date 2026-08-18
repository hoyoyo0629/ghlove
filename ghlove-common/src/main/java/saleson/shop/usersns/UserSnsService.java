package saleson.shop.usersns;

import com.onlinepowers.framework.security.userdetails.User;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.usersns.domain.UserSns;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface UserSnsService {

    // sns연결 정보 받아오기 - SNS 연동설정
    List<UserSns> getUserSnsList(UserSns userSns);

    // 해당 SNS 타입 계정 정보 조회
    UserSns getUserSnsInfo(UserSns userSns);

    // loginId, userName update
    void updateUser(User user);

    // 탈퇴시 SNS인증 내역 삭제 - OP_USER_SNS
    void secedeSnsProcess(User user);

    // sns 연결 끊기
    void disconnectSns(UserSns userSns);

    // email로 등록 된 아이디가 있는지 확인
    int getDuplicatedUserCount(UserSns userSns);

    // sns연결 정보 받아오기 - OP_USER_SNS
    UserSns getUserSns(UserSns userSns);

    // SNS로 회원가입 process
    Map<String, String> joinProcess(UserSns userSns, Map<String, String> map, UserSns userSnsData);

    // SNS 연결 정보 등록
    void insertUserSns(UserSns userSns);


}
