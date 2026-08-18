package saleson.shop.usergroup;

import java.util.List;
import java.util.Map;

import saleson.shop.group.domain.Group;
import saleson.shop.usergroup.domain.UserGroup;
import saleson.shop.usergroup.support.UserGroupParam;


public interface UserGroupService {
		

	/**
	 * 유저아이디로 해당 유저가 속한 group을 삭제
	 * @param userId
	 */
	public void deleteUserGroupByUserId(long userId);
	
	
	/**
	 * 해당 유저를 그룹에 추가, 수정
	 * @param map
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
	 * 유저 그룹 등록, 삭제 처리
	 * @param userGroup
	 * @param model
	 */
	public void userGroupSetting(UserGroup userGroup);
	
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
	 * 권한그룹 등록
	 * @param group
	 */
	void insertGroup(UserGroup group);
	
	/**
	 * 권한그룹 수정 항목
	 * @param params
	 * @return
	 */
	UserGroup getGroupById(Map<String, Object> params);
	
	/**
	 * 권한그룹 수정 처리
	 * @param params
	 * @return
	 */
	void updateGroup(UserGroup group);
	
	/**
	 * 권한그룹 삭제
	 * @param group
	 */
	void deleteGroup(UserGroup group);

}
