package saleson.shop.user;

import java.util.List;

import com.onlinepowers.framework.security.userdetails.User;

import saleson.shop.categories.domain.Categories;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.PolicyInfo;
import saleson.shop.user.domain.RtnpsntInfo;
import saleson.shop.user.domain.UserDetail;

public interface JoinService {

	/**
	 * 회원가입시 지자체 정보
	 * 
	 * @param LocGovInfo
	 */
	List<LocGovInfo> getLocGovList(LocGovInfo locGovInfo);

	/**
	 * 아이디 중복 확인
	 * 
	 * @param String
	 */
	int getUserInfoByUserId(String userID);

	/**
	 * 관심 지자체 추가
	 * 
	 * @param UserDetail
	 */
	int insertIntrstLocgovInfo(LocGovInfo locGovInfo);

	/**
	 * 관심 답례품 추가
	 * 
	 * @param UserDetail
	 */
	int insertInststRtnpsntInfo(RtnpsntInfo rtnpsntInfo);

	/**
	 * 관심 답례품 목록
	 * 
	 * @param LocGovInfo
	 */
	List<Categories> getCategoryList();

	/**
	 * 사용자 정보 업데이트
	 * 
	 * @param LocGovInfo
	 */
	int updateUserInfo(UserDetail userDetail);

	/**
	 * 사용자 정보 업데이트
	 * 
	 * @param policyType
	 */
	PolicyInfo getPolicyInfo(String policyType);

	/**
	 * 회원가입
	 * 
	 * @param policyType
	 */
	void insertUserAndUserDetail(User user, UserDetail userDetail);
	

}
