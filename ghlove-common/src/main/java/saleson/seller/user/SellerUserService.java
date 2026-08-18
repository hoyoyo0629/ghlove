package saleson.seller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.web.domain.ListParam;

import saleson.model.user.SellerUserLogin;
import saleson.seller.main.domain.PkiSellerLogin;
import saleson.seller.main.domain.Seller;
import saleson.shop.user.domain.PkiManagerLoginResult;
import saleson.shop.user.domain.SellerUser;
import saleson.shop.user.support.UserSearchParam;

public interface SellerUserService {

    /**
     * 운영자 등록
     * @param sellerId
     * @param user
     * @return
     * @throws Exception
     */
    int insertSellerUser(long sellerId, User user) throws UserException;

    /**
     * 마스터 운영자 등록
     * @param sellerId
     * @param user
     * @throws Exception
     */
    int insertSellerMasterUser(long sellerId, User user) throws UserException;
    /**
     * 운영자 수정
     * @param sellerId
     * @param user
     * @return
     * @throws Exception
     */
    public int updateSellerUser(User user) throws UserException;
    /**
     * 셀러 수정
     * @param sellerId
     * @param user
     * @return
     * @throws Exception
     */
    int updateSellerUser(long sellerId, User user) throws UserException;

    /**
     * 운영자 삭제
     * @param sellerId
     * @param listParam
     * @return
     * @throws Exception
     */
    int deleteSellerUserByList(long sellerId, ListParam listParam) throws UserException;

    /**
     * 운영자 목록 조회
     * @param userSearchParam
     * @return
     */
    int getSellerUserListCount(UserSearchParam userSearchParam);

    /**
     * 운영자 목록 조회
     * @param userSearchParam
     * @return
     */
    List<User> getSellerUserList(UserSearchParam userSearchParam);

    /**
     * 운영자 상세 조회
     * @param sellerId
     * @param userId
     * @return
     * @throws Exception
     */
    User getSellerUserById(long sellerId, long userId) throws UserException;

    /**
     * 운영자 상세 조회
     * @param sellerId
     * @param loginId
     * @return
     * @throws Exception
     */
    User getSellerUserByLoginId(long sellerId, String loginId) throws UserException;

    /**
     * 운영자 로그인 ID 중복 체크
     * @param loginId
     * @return
     */
    boolean isDuplicateSellerUserByLoginId(String loginId);

    /**
     * 해당 관리자 비밀번호를 임시 비밀번호로 변경
     * @param userId
     */
    void updateTempPasswordForSellerUser(long userId) throws UserException;


    /**
     * 해당 관리자 비밀번호 변경
     * @param user
     * @param passowrd
     * @param changePassowrd
     */
    void updatePasswordForSellerUser(User user, String passowrd, String changePassowrd) throws UserException;

    /**
     * Seller 비밀번호 변경
     * @param userId
     * @param changePassowrd
     */
    void updatePasswordForSellerUser(User user, String changePassowrd) throws UserException;

    /**
     * 판매관리자 userId 조회
     * @param loginId
     */
    long getSellerUserIdByLoginId(String loginId);

    /**
     * 관리자에서 판매관리자 비밀번호 변경
     * @param user
     */
    void updateSellerUserPassword(User user);

    /**
     * 판매관리자 로그인 세션정보 등록
     * @param session
     * @param userId
     */
    void saveLoginSession(HttpSession session, long userId);

    /**
     * 판매관리자 로그인 세션정보 등록
     * @param sessionId
     * @param userId
     */
    void saveLoginSession(String sessionId, long userId);


    /**
     * 해당 판매관리자의 중복 세션(토큰) 목록 조회
     * @param userId
     * @return
     */
    List<SellerUserLogin> getLoginSessionByUserId(long userId);

    /**
     * 판매자 로그인 ID, 비밀번호 일치여부
     * @param loginId, password
     * @return
     */
    SellerUser getSellerUserByLoginIdAndPwd(String loginId, String password);

    /**
     * Seller 로그인시 초기화, 비밀번호 만료시 비밀번호 변경
     * @param sellerUser
     */
    void updatePasswordForSellerLogin(SellerUser sellerUser) throws UserException;


    /**
     * 판매관리자 로그인 처리
     *
     */
    void loginProcess(SellerUser sellerUser, Seller seller, HttpServletRequest request);

    /**
     * 판매관리자 인증서 체크
     * @param pkiSellerLogin
     */
    PkiSellerLogin getPkiSellerFormValid(PkiSellerLogin pkiSellerLogin);

    /**
     * 판매관리자 인증서 갱신
     * @param pkiSellerLogin
     */
    PkiSellerLogin updatePkiManagerMberDn(PkiSellerLogin pkiSellerLogin);

    /**
     * 판매관리자 인증서 조회
     * @param pkiSellerLogin
     */
    List<SellerUser> getSellerUserByMberDn(PkiSellerLogin pkiSellerLogin);

    /**
     * 로그인 실패 카운트 증가
     * @param loginId
     * @return
     */
    int updateLoginFailCnt(String loginId);

    /**
     * 답례품 제공자 ci 등록
     * @param pkiSellerLogin
     * @return
     */
    int modSellerMberCi(PkiSellerLogin pkiSellerLogin);

    /**
     * 답례품 제공자 ci 삭제
     * @param
     * @return
     */
    int delSellerMberCi();

    /**
     * 답례품 제공자 조회
     * @param sellerId
     * @param userId
     * @return
     * @throws Exception
     */
    SellerUser getSellerUserByLoginIdForSms(String loginId) throws UserException;

	/**
	 * 판매자 LOGIN_ID로 이메일 UPDATE 등록(op_seller_user)
	 * @param Seller seller
	 * @return
	 */
    int updateSellerUserInfo(Seller seller);
}
