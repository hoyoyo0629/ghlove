package saleson.shop.user;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.web.domain.ListParam;

import saleson.common.opmanager.count.OpmanagerCount;
import saleson.model.UserEntity;
import saleson.model.user.UserLogin;
import saleson.shop.coupon.domain.ChosenUser;
import saleson.shop.order.domain.Buyer;
import saleson.shop.user.domain.AuthUserInfo;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.ManagerLogin;
import saleson.shop.user.domain.PersonInCharge;
import saleson.shop.user.domain.PkiManagerLogin;
import saleson.shop.user.domain.PkiManagerLoginResult;
import saleson.shop.user.domain.TempPasswordChange;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserInfoByCI;
import saleson.shop.user.domain.UserInfoBySign;
import saleson.shop.user.domain.UserModifyInfo;
import saleson.shop.user.domain.UserParent;
import saleson.shop.user.domain.UserPasswordType;
import saleson.shop.user.support.AgreeDto;
import saleson.shop.user.support.AuthInfo;
import saleson.shop.user.support.ChangePasswordForNoLoginUser;
import saleson.shop.user.support.FinancInfo;
import saleson.shop.user.support.FinancTokenResult;
import saleson.shop.user.support.OpManagerParam;
import saleson.shop.user.support.SignInfo;
import saleson.shop.user.support.UserSearchParam;
import saleson.shop.usersns.domain.UserSns;

public interface UserService {

	/**
	 * 마이페이지 상단 데이터 - 공통
	 * @param model
	 */
	void setMypageUserInfoForFront(Model model);

	/**
	 * 회원 비밀번호 수동 변경
	 * @param user
	 */
	void updateUserPasswod(User user);

	UserDetail getUserDetail(long userId);

	/**
	 * 회원 수를 가져옴.
	 * @param searchParam
	 * @return
	 */
	int getUserCount(UserSearchParam searchParam);

	/**
	 * 검색 조건에 해당하는 회원 목록을 가져옴.
	 * @param searchParam
	 * @return
	 */
	List<User> getUserList(UserSearchParam searchParam);
	/**
	 * 회원등록.
	 * @param user
	 * @return
	 */
	void insertUser(User user);

	void insertUserDetail(UserDetail userDetail);

	void insertUserRole(UserRole userRole);

	void insertUserAndUserDetailByManager(User user, UserDetail userDetail);

	void insertUserAndUserDetail(User user, UserDetail userDetail);

	void insertUserAndUserDetailForSns(User user, UserDetail userDetail , UserSns UserSns);

	/**
	 * 회원수정.
	 * @param user
	 * @return
	 */
	void updateUser(User user);

	void updateUserDetail(UserDetail userDetail);

	void updateUserRole(UserRole userRole);

	void updateUserAndUserDetail(User user, UserDetail userDetail);

	/**
	 * 회원삭제.
	 * @param userId
	 * @return
	 */
	void deleteUser(long userId);

	void deleteUserDetail(long userId);

	void deleteUserRole(long userId);

	int getUserCountByPhoneNumber(String phoneNumber);

	/**
	 * Email 주소를 사용중인 회원수를 조회
	 * @param email
	 * @return
	 */
	int getUserCountByEmail(String email);

	/**
	 * LoginId 를 이용한 회원 수 조회
	 * @param loginId
	 * @return
	 */
	int getUserCountByLoginId(String loginId);

	/**
	 * 닉네임을 사용중인 회원 수를 조회
	 * @param nickname
	 * @return
	 */
	int getUserCountByNickname(String nickname);

	/**
	 * 사용자가 입력한 데이터를 사용중인 회원수를 조회
	 * @param user
	 * @return
	 */
	int getUserCountByUserInfo(User user);

	/**
	 * 로그인 ID에 해당 하는 회원 수를 조회한다..
	 * @param loginId
	 * @return
	 */
	int getUserCountByManagerId(String loginId);

	/**
	 * 전화번호에 해당하는 회원 수를 조회한다.
	 * @param phoneNumber
	 * @return
	 */
	public int getUserCountByManagerPhoneNumber(String phoneNumber);

	/**
	 * 관리자 카운트를 가져옴.
	 * @param searchParam
	 * @return
	 */
	int getUserManagerCount(UserSearchParam searchParam);

	/**
	 * 회원ID로 회원을 조회한다.
	 * @param userId
	 * @return
	 */
	User getUserByUserId(long userId);

	/**
	 * loginID로 회원을 조회한다.
	 * @param loginId
	 * @return
	 */
	User getUserByLoginId(String loginId);


	/**
	 * loginID로 회원을 조회한다. 관리자 로그인시 이메일 발송을 위해 마스킹 처리하지 않은 사용자 정보를 얻는다.
	 * @param loginId
	 * @return
	 */
	public User getPureUserByLoginId(String loginId);

	/**
	 * 회원 전체 카운트를 가져옴.
	 * @param authority
	 * @return
	 */
	int getUserTotalCount(String authority);

	/**
	 * 회원 전체 삭제 및 선택 삭제
	 * @param listParam
	 */
	void deleteUserByListParam(ListParam listParam);

	/**
	 * 회원 구매 정보 업데이트 - 관리자 배송완료 처리 시점
	 * @param userId
	 * @param price
	 */
	void updateUserBuyInfoForOrder(long userId, int price);

	void getUserPasswordSearch(UserSearchParam searchParam);

	void updateFrontUserAndUserDetail(User user);

	void updateFrontUserAndUserDetail(User user, UserDetail userDetail);

	/**
	 * 팝업을 이용한 유저의 알림서비스 업데이트
	 * */
	void updateUserReceive(User user, UserDetail userDetail);

	User getUserByParam(UserSearchParam searchParam);

	/**
	 * 회원 아이디 찾기
	 * @param authUserInfo
	 * @return
	 */
	User getUserInfoByUserName(AuthUserInfo authUserInfo);

	/**
	 * 탈퇴 회원 리스트 카운트
	 * @param searchParam
	 * @return
	 */
	int getSecedeUserCount(UserSearchParam searchParam);

	/**
	 * 탈퇴 회원 리스트
	 * @param searchParam
	 * @return
	 */
	List<User> getSecedeUserList(UserSearchParam searchParam);

	/**
	 * 탈퇴 회원 정보 삭제
	 * @param user
	 */
	void updateSecedeFrontUserAndUserDetail(User user);


	/**
	 * 운영자 메뉴 ROLE 정보 목록.
	 * @return
	 */
	List<HashMap<String, String>> getAdminMenuRoleList();

	void updateUserForAdmin(User user);

	/**
	 * 타입에 따라 문자, 메일 발송. LSW 2016.08.05 추가 (비밀번호 변경 시 발송을 위해..)
	 * 사용하지 않아 주석처리 KSH 2019.06.11 (구 SMS 로직때문에 UMS 작업시 에러)
	 */
	// void sendSmsAndEmail(User user, String templateId);

	List<ChosenUser> getChosenUserList(List<String> list);
	List<ChosenUser> getChosenUserListbyParam(ChosenUser chosenUser);
	List<ChosenUser> getUserListForChosen(UserSearchParam userSearchParam);

	/**
	 * 관리자 카운트
	 * @param searchParam
	 * @return
	 */
	int getManagerCount(UserSearchParam searchParam);

	/**
	 * 관리자 목록
	 * @param searchParam
	 * @return
	 */
	List<User> getManagerList(UserSearchParam searchParam);


	/**
	 * 관리자 정보 조회
	 * @param userId
	 * @return
	 */
	User getManagerByUserId(long userId);

	/**
	 * 이메일로 관리자 카운트 조회
	 * @param email
	 * @return
	 */
	int getManagerCountByEmail(String email);

	/**
	 * 관리자 정보 수정.
	 * @param user
	 */
	void updateManager(User user);

	/**
	 * 관리자 등록
	 * @param user
	 */
	void insertManager(User user);

	/**
	 * 관리자 선택 삭제
	 * @param listParam
	 */
	void deleteManagerByListParam(ListParam listParam);

	/**
	 * 휴면계정 안내메일 발송
	 */
	void sendSleepUserMail();

	/**
	 * 휴면계정 처리
	 */
	void setSleepUser();

	/**
	 * 휴면계정 정상화
	 */
	void wakeupUser(User currentUser);

	/**
	 * 이상우 [2017-05-11 추가]
	 * 관리자 메인 방문자,가입자(오늘, 주중) 카운트
	 */
	List<OpmanagerCount> getOpmanagerUserCountAll();

	/**
	 * 유율선 [2017-05-18 추가]
	 * 주문자 정보 기본 정보로 설정
	 * @param buyer
	 */
	void updateUserDetailForOrder(Buyer buyer);

	/**
	 * 해당 관리자 비밀번호를 임시 비밀번호로 변경
	 * @param userId
	 */
	void updateTempPasswordForManager(long userId) throws UserException;


	/**
	 * 해당 관리자 비밀번호 변경
	 * @param userId
	 * @param passowrd
	 * @param changePassowrd
	 */
	void updatePasswordForManager(long userId, String passowrd, String changePassowrd) throws UserException;

	/**
	 * 해당 회원 비밀번호 변경
	 * @param userId
	 * @param passowrd
	 * @param changePassowrd
	 * @throws UserException
	 */
	void updatePasswordForUser(long userId, String passowrd, String changePassowrd) throws UserException;

	/**
	 * 해당 회원 비밀번호 변경 유예
	 * @param userId
	 */
	void updatePasswordExpiredDateForUser(long userId) throws UserException;

	/**
	 * 해당 관리자 비밀번호 변경 유예
	 * @param userId
	 */
	void updatePasswordExpiredDateForManager(long userId) throws UserException;

	/**
	 * 관리자 로그인 세션정보 등록
	 * @param session
	 * @param userId
	 */
	void insertLoginSessionForManager(HttpSession session, long userId);

	void saveLoginSessionForManager(HttpSession session, long userId);
	/**
	 * 관리자 로그인 세션정보 삭제
	 * @param userId
	 */
	void deleteLoginSessionForManager(long userId);

	void saveLoginSessionForManager(String sessionId, long userId);
	/**
	 * 해당 사용자의 중복 세션 목록 조회
	 * @param userId
	 * @return
	 */
	List<ManagerLogin> getLoginSessionForManagerByUserId(long userId);

	/**
	 * 비밀번호 유효기간
	 * @return
	 */
	String getPasswordExpiredDate();

	/**
	 * 가입불가 아이디 및 회원 아이디 중복 체크
	 * @param user
	 * @return
	 */
	String checkDuplication(User user);

	/**
	 * 기존 Asis 회원 패스워드 업데이트
	 * @param userId
	 * @param password
	 */
	void updatePasswordByAsisUser(long userId, String password);

	/**
	 * 사용되지 않는 사용자 잠금처리
	 * @throws Exception
	 */
	void updateLockForManager() throws Exception;

	/**
	 * 개인정보 항목 저장
	 *
	 * @param dtos
	 * @param user
	 */
	void saveUserAgree(List<AgreeDto> dtos, User user);

	/**
	 * 관리자 본인인증 유효 시간 조회
	 * @param userId
	 * @return
	 */
	LocalDateTime getAuthExpiredDateById(long userId);

	/**
	 * 관리자 본인 인증 시간 업데이트
	 * @param userId
	 */
	void updateAuthExpiredDateById(long userId);


	/**
	 * user에 포함된 userDetail의 processHyphen 진행
	 * @param user
	 */
	void setUserDetailHyphen(User user);

	/**
	 * 회원 디지털원패스로그인키 로 회원정보를 조회한다.
	 * @param userId
	 * @return
	 */
	UserEntity getUserByUserKey(String userKey);

	/**
	 * 회원 디지털원패스로그인키 로 회원정보를 조회한다.
	 * @param userId
	 * @return
	 */
	UserEntity getUserByMberCi(String mberCi, String userKey);

	/**
	 * 디지털원패스 연동해지 시 userkey 삭제 및 탈퇴처리
	 * @param userId
	 * @return
	 */
	void updateUserKeyStatusCode(long userId, String leaveCode, String leaveReason);

	String getCryptoDecByLoginId(String loginId);

	/**
	 * 관심지자체 정보 호출
	 * @param userId
	 * @return
	 */
	List<LocGovInfo> getIntrstLocgovList(long userId);

	/**
	 * 회원정보 수정을 위한 정보 호출
	 * @param userId
	 * @return
	 */
	UserModifyInfo getUserModifyInfo(long userId);

	/**
	 * 마이페이지 > 회원정보 수정 > 비밀번호 수정 전 비밀번호 확인
	 * @param userId
	 * @param presentPassword
	 * @return
	 */
	Boolean confirmUserPassword(long userId, String presentPassword) throws UserException;

	/**
	 * 마이페이지 > 회원정보 수정 > 비밀번호 수정
	 * @param userId
	 * @param password
	 * @return
	 */
	void updateUserPassword(long userId, String changePassowrd) throws UserException;

	/**
	 * 마이페이지 > 회원정보 수정 > 본인인증 휴대폰인증정보 수정
	 * @param authInfo
	 * @return
	 */
	int modifyMobileAuth(AuthInfo authInfo);

	/**
	 * 관리자 > 비밀번호 변경 (관리자 비밀번호 초기화, 초기 비밀번호 설정, 관리자 비밀번호 변경안내 - 3개월)
	 * @param request
	 * @param tempPasswordChange
	 * @return
	 */
	String updateTempPasswordChange(HttpServletRequest request, TempPasswordChange tempPasswordChange);

    /**
    * 회원 로그인 세션정보(토큰) 등록
    * @param sessionId
    * @param userId
    */
    void saveLoginSessionForUser(String sessionId, long userId);

    /**
    * 해당 회원의 중복 세션(토큰) 목록 조회
    * @param userId
    * @return
    */
    List<UserLogin> getLoginSessionForUserByUserId(long userId);

	/**
	 * 휴대폰 본인인증 정보로 정보 조회
	 * @param authInfo
	 * @return
	 */
	String getLoginIdByMobileAuth(AuthInfo authInfo);

	/**
	 * 사용자 > 비밀번호 변경 (비로그인 시)
	 * @param tempPasswordChange
	 * @return
	 */
	void changeUserPasswordForNoLogin(ChangePasswordForNoLoginUser changePassword) throws UserException;

	/**
	 * 비로그인 시 인증 정보 및 기타 정보로 회원정보 조회(비밀번호 변경 시)
	 * @param changePassword
	 * @return
	 */
	User getUserByNoLoginUser(ChangePasswordForNoLoginUser changePassword) throws UserException;

	/**
	 * 비로그인 시 비밀번호 만료일자 업데이트(다음에 변경)
	 * @param changePassword
	 * @return
	 */
	void changeUserPasswordLater(ChangePasswordForNoLoginUser changePassword) throws UserException;

	/**
	 * 로그인 시 userkey 정보 조회
	 * @param user
	 * @return
	 */
	String getUserKeyInfo(long user) throws Exception;

	/**
	 * 로그인 시 loginPath 정보 조회
	 * @param user
	 * @return
	 */
	String getUserLoginPathInfo(long user) throws Exception;

	/**
	 * ci정보로 고객 정보 조회
	 * @param user
	 * @return
	 */
	UserInfoByCI getUserInfoByCi(AuthInfo authInfo);

	/**
	 * PKI 폼 유효성 조회
	 * @param pkiManagerLogin
	 * @return
	 */
	PkiManagerLoginResult getPkiManagerFormValid(PkiManagerLogin pkiManagerLogin);

	/**
	 * 회원 DN 수정
	 * @param pkiManagerLogin
	 * @return
	 */
	PkiManagerLoginResult updatePkiManagerMberDn(PkiManagerLogin pkiManagerLogin) throws Exception;

	/**
	 * 회원 DN FinDN 삭제
	 * @param pkiManagerLogin
	 * @return
	 */
	PkiManagerLoginResult updateDeletePkiManagerMberDn(PkiManagerLogin pkiManagerLogin) throws Exception;

	/**
	 * 회원 DN 수정(사용자)
	 * @param signInfo
	 * @return
	 */
	void signRegisterForUser(SignInfo signInfo);

	/**
	 * 회원 DN 폐기(사용자)
	 * @param signInfo
	 * @return
	 */
	void signRemoveForUser(SignInfo signInfo);

	/**
	 * 회원 DN 으로 아이디 찾기(사용자)
	 * @param signInfo
	 * @return
	 */
	UserInfoBySign checkSignForUser(SignInfo signInfo);

	void changeUserPwdForSignNoLogin(ChangePasswordForNoLoginUser changePassword);

	/**
	 * 비로그인 시 인증 정보 및 기타 정보로 회원정보 조회(비밀번호 변경 시)
	 * @param changePassword
	 * @return
	 */
	User getUserBySign(ChangePasswordForNoLoginUser changePassword);

	/**
	 * 관리자 로그인 유효성 검사
	 * @param request
	 * @param pkiManagerLogin
	 * @return
	 */
	PkiManagerLoginResult savePkiManagerLoginUserValid(HttpServletRequest request, PkiManagerLogin pkiManagerLogin, HttpSession session) throws Exception;

	/**
	 * 원패스 userkey 초기화
	 * @param userId
	 * @return
	 */
	void updateOnepassUnlink(Long userId);

	/**
	 * 금융인증서 본인인증 UCPID
	 * @param ucpid_request_info, cp_code, ucpid_nonce
	 * @return api_tran_id, dn, ci, ci2, ci_update, di, real_name, gender, national_info, birthdate
	 * @throws IOException
	 */
	Map<String, Object> getFinancPid(FinancInfo financInfo) throws IOException;

	Map<String, Object> getFinancUserToken(FinancInfo financInfo) throws Exception;

	/**
	 * 금융인증서 Ci 로그인 정보 조회
	 * @param
	 * @return login_id
	 * @throws IOException
	 */
	//FinancResultInfo getLoginCiInfo(FinancInfo financInfo);
	Map<String, Object> getLoginCiInfo(FinancInfo financInfo);

	/**
	 * 금융인증서 HEX NONCE 값
	 * @param
	 * @return
	 * @throws
	 */
	String bytesToHexString();

	int updatePasswordType(UserPasswordType userPasswordType);

	String setSessionByManager(HttpServletRequest request, User manager);

	/**
	 * 14세 미만 회원 가입 시 부모인증 정보 저장
	 * @param mberDn
	 * @return
	 */
	void insertUserParent(UserParent userParent);

	void setUserBirthdayDecList();

	/**
	 * user_id 채번
	 * @return user_id
	 */
	long selectNewUserId();

	/**
	 * 5부제를 위한 생년월일 조회
	 * @param mberDn
	 * @return
	 */
	UserDetail getUserBirthday(String loginId);

	void setOpUserBirthdayStat();

	/**
	 * 관리자 이메일 등록
	 * @param mberDn
	 * @return
	 */
	int updateManagerInfo(OpManagerParam param);

	/**
	 * 관리자 이메일 등록(op_user)
	 * @param mberDn
	 * @return
	 */
	public int updateUserByLoginId(User user);



	/**
	 * 관리자 정보 조회
	 * @param userId
	 * @return
	 */
	User getManagerByLoginId(String loginId);

	/**
	 * 오프라인 관리자 등록
	 * @param user
	 */
	void insertChargerManager(User user, PersonInCharge personInCharge);

	/**
	 * 관리자 휴면계정 업데이트
	 * @param
	 * @return
	 */
	public void updateSleepManager();
}
