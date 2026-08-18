package saleson.seller.user;

import java.util.List;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.onlinepowers.framework.security.userdetails.User;

import saleson.seller.main.domain.PkiSellerLogin;
import saleson.seller.main.domain.Seller;
import saleson.shop.user.domain.SellerUser;
import saleson.shop.user.support.UserSearchParam;

@Mapper("sellerUserMapper")
public interface SellerUserMapper {

    /**
     * 판매관리자 등록
     * @param user
     */
    int insertSellerUser(User user);

    /**
     * 판매관리자 수정
     * @param user
     */
    int updateSellerUser(User user);

    /**
     * 판매관리자 삭제
     * @param userId
     */
    int deleteSellerUserById(long userId);

    /**
     * 판매관리자 목록 조회
     * @param userSearchParam
     * @return
     */
    int getSellerUserListCount(UserSearchParam userSearchParam);

    /**
     * 판매관리자 목록 조회
     * @param userSearchParam
     * @return
     */
    List<User> getSellerUserList(UserSearchParam userSearchParam);

    /**
     * 판매관리자 상세 조회
     * @param userId
     * @return
     */
    User getSellerUserById(long userId);

    /**
     * 판매관리자 상세 조회
     * @param loginId
     * @return
     */
    User getSellerUserByLoginId(String loginId);

    /**
     * 판매관리자 로그인 ID 중복 체크
     * @param loginId
     * @return
     */
    int getDuplicateSellerUserByLoginId(String loginId);

    int updatePasswordForSellerUser(User user);

    int updatePasswordForSellerUser2(SellerUser user);
    /**
     * 판매관리자 userId 조회
     * @param loginId
     */
    long getSellerUserIdByLoginId(String loginId);

    /**
     *  관리자에서 판매관리자 비밀번호 변경
     * @param user
     * @return
     */
    int updateSellerUserPassword(User user);

    /**
     * 아이디, 비밀번호 일치 정보 조회
     * @param sellerUser
     * @return
     */
    SellerUser getSellerUserByLoginIdAndPwd(SellerUser sellerUser);

    /**
     * 초기 비밀번호/만료 에 의한 비밀번호 변경
     * @param sellerUser
     * @return
     */
    int updatePasswordForSellerUserLogin(SellerUser sellerUser);


    /**
     * 인증서 체크
     * @param pkiSellerLogin
     * @return
     */
    PkiSellerLogin getPkiSellerFormValid(PkiSellerLogin pkiSellerLogin);


    /**
     * 인증서 수정/삭제
     * @param pkiSellerLogin
     * @return
     */
    int updatePkiManagerMberDn(PkiSellerLogin pkiSellerLogin);

    /**
     * 인증서 정보로 판매자 조회
     * @param pkiSellerLogin
     * @return
     */
    List<SellerUser> getSellerUserByMberDn(PkiSellerLogin pkiSellerLogin);

    /**
     * 로그인 실패 카운트 증가
     * @param loginId
     * @return
     */
    int updateLoginFailCnt(String loginId);

    /**
     * 로그인 실패 카운트 초기화
     * @param loginId
     * @return
     */
    int clearLoginFailCnt(String loginId);

    /**
     * 답례품 제공자 ci 등록
     * @param pkiSellerLogin
     * @return
     */
    int modSellerMberCi(PkiSellerLogin pkiSellerLogin);

    /**
     * 답례품 제공자 ci 삭제
     * @param userId
     * @return
     */
    int delSellerMberCi(long userId);

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
