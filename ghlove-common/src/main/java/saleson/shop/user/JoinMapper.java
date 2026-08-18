package saleson.shop.user;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import saleson.shop.categories.domain.Categories;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.PolicyInfo;
import saleson.shop.user.domain.RtnpsntInfo;
import saleson.shop.user.domain.UserDetail;

import java.util.List;

@Mapper("JoinMapper")
public interface JoinMapper {

	/**
	 * 회원가입시 지자체 정보
	 * 
	 * @param locGovInfo
	 */
	List<LocGovInfo> getLocGovList(LocGovInfo locGovInfo);

	/**
	 * 회원가입시 아이디 중복 체크
	 * 
	 * @param AuthUserInfo
	 */
	int getUserInfoByUserId(String userID);

	/**
	 * 관심 지자체 추가
	 * 
	 * @param user
	 */
	int insertIntrstLocgovInfo(LocGovInfo locGovInfo);

	/**
	 * 관심 답례품 추가
	 * 
	 * @param user
	 */
	int insertInststRtnpsntInfo(RtnpsntInfo rtnpsntInfo);

	/**
	 * 관심 답례품 목록
	 * 
	 * @param Categories
	 */
	List<Categories> getCategoryList();

	/**
	 * 사용자 정보 업데이트
	 * 
	 * @param LocGovInfo
	 */
	int updateUserInfo(UserDetail userDetail);

	/**
	 * 약관 정보 호출
	 * 
	 * @param policyType
	 */
	PolicyInfo getPolicyInfo(String policyType);
	
	int updateloginPathCode(UserDetail userDetail);

}
