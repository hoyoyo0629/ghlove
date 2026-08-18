package saleson.shop.usergroup;

import java.util.List;
import java.util.Map;

import saleson.shop.group.domain.Group;
import saleson.shop.usergroup.domain.UserGroup;
import saleson.shop.usergroup.domain.UserGroupLog;
import saleson.shop.usergroup.support.UserGroupParam;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("usergroupMapper")
public interface UserGroupMapper {

	
	/**
	 * 해당 유저가 속한 그룹 삭제.
	 * @param userId
	 */
	public void deleteUserGroupByUserId(long userId);
	
	/**
	 * userId로 가입된 그룹을 불러온다.
	 * @param userId
	 * @return
	 */
	public void insertUserGroups(Map map);
	
	/**
	 * 해당 유저들의 그룹을 수정.
	 * @param map
	 */
	public void insertGroupsOfUsers(Map map);
	
	/**
	 * 해당 유저들의 그룹을 삭제
	 * @param map
	 */
	public void deleteGroupsOfUsers(Map map);
	
	/**
	 * 회원 정보 DB에 그룹 설정 하기
	 * @param userGroup
	 */
	public void updateUserGroupCodeForUserDetail(UserGroup userGroup);
	
	/**
	 * 그룹 삭제
	 * @param groupCode
	 */
	public void deleteUserGroupCodeForUserDetail(String groupCode);
	
	/**
	 * 회원 그룹변경 로그 기록
	 * @param log
	 */
	public void insertUserGroupLog(UserGroupLog log);
	
	/**
	 * 사용자 권한 그룹 카운트
	 * @param UserGroupParam
	 * @return
	 */
	public int getUserGroupCount(UserGroupParam userGroupParam);
	
	/**
	 * 사용자 권한 그룹 리스트
	 * @return
	 */
	public List<Group> getUserGroupList(UserGroupParam userGroupParam);
	
	/**
	 * 사용자 권한 그룹 등록
	 * @param Group
	 */
	public void insertGroup(UserGroup Group);
	
	/**
	 * 사용자 권한 그룹 수정 항목
	 * @param params
	 * @return
	 */
	 UserGroup getGroupById(Map<String, Object> params);
	 
	 /**
	  * 사용자 권한 그룹 수정
	  * @param Group
	  */
	 public void updateGroup(UserGroup Group);
	 
	 /**
	  * 사용자 권한 그룹 삭제
	  * @param group
	  */
	 void deleteGroup(UserGroup Group);
}
