package saleson.shop.user;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CipherUtils;

import lombok.RequiredArgsConstructor;
import saleson.common.security.crypto.CipherUtilsCopy;
import saleson.common.utils.ShopUtils;
import saleson.shop.slave.SlaveSleepUserMapper;
import saleson.shop.user.domain.SleepUser;
import saleson.shop.user.domain.SleepUserEncryptor;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserDetailEncryptor;
import saleson.shop.user.domain.UserEncryptor;
import saleson.shop.user.support.SleepUserSearchParam;

@RequiredArgsConstructor
@Service("SleepUserService")
public class SleepUserServiceImpl extends EgovAbstractServiceImpl implements SleepUserService {
	private static final Logger log = LoggerFactory.getLogger(SleepUserServiceImpl.class);
	
//	private final SleepUserMapper sleepUserMapper;
	private final SlaveSleepUserMapper slaveSleepUserMapper;
	private final SleepUserEncryptor sleepUserEncryptor;
	private final UserMapper userMapper;
	private final UserEncryptor userEncryptor;
	private final UserDetailEncryptor userDetailEncryptor;
	
	/**
	 * 휴면회원관리 목록 갯수 조회  
	 * @param searchParam
	 * @return
	 */
	@Override
	public int getSleepUserCountByParam(SleepUserSearchParam searchParam) {
		int count = 0;
		
		try {
			// 검색조건 암호화
			if("USER_NAME".equals(searchParam.getSrchKey())) {
				searchParam.setSrchValue(CipherUtilsCopy.encrypt(searchParam.getSrchValue()));
			}
			
			// 휴면회원관리 목록 갯수 조회
			count = slaveSleepUserMapper.getSleepUserCountByParam(searchParam);
			
			// 검색조건 복호화
			if("USER_NAME".equals(searchParam.getSrchKey())) {
				searchParam.setSrchValue(CipherUtilsCopy.decrypt(searchParam.getSrchValue()));
			}
			
		} catch (OpRuntimeException e) {
			log.error("ERROR: {}", "=========== getSleepUserCountByParam Exception ============");
		}
		
		return count;
	}
	
	/**
	 * 휴면회원관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<SleepUser> getSleepUserListByParam(SleepUserSearchParam searchParam) {
		List<SleepUser> sleepUserList = null;
		
		try {
			// 검색조건 암호화
			if("USER_NAME".equals(searchParam.getSrchKey())) {
				searchParam.setSrchValue(CipherUtilsCopy.encrypt(searchParam.getSrchValue()));
			}
			
			// 휴면회원관리 목록 조회 
			sleepUserList = slaveSleepUserMapper.getSleepUserListByParam(searchParam);
			
			// 검색조건 복호화
			if("USER_NAME".equals(searchParam.getSrchKey())) {
				searchParam.setSrchValue(CipherUtilsCopy.decrypt(searchParam.getSrchValue()));
			}
			
			// 조회한 목록 정보 복호화
			if(sleepUserList != null && sleepUserList.size() > 0) {
				for(SleepUser sleepUser : sleepUserList) {
					sleepUser.setUserName(CipherUtilsCopy.decrypt(sleepUser.getUserName()));
					sleepUser.setAddress(CipherUtilsCopy.decrypt(sleepUser.getAddress()));
					sleepUser.setAddressDetail(CipherUtilsCopy.decrypt(sleepUser.getAddressDetail()));
					decryptData(sleepUser);
				}
			}
		} catch (OpRuntimeException e) {
			log.error("ERROR: {}", "========= getSleepUserListByParam Exception ==========");
		}	
		
		return sleepUserList;
	}
	
	/**
	 * 휴면회원 해제
	 * @param userId
	 * @return
	 */
	@Override
	public String updateWakeupSleepUser(SleepUser sleepUser) {
		String code = "FAIL";
		List<Long> userIdList = sleepUser.getUserIdList();
		
		// 1. 유효성 체크
		if(userIdList == null || userIdList.size() == 0) {
			code = "ERR_VALID";
			return code;
		}
		
		// 2. 휴면회원 해제
		User user = null;
		for(Long userId : userIdList) {
			
			// 2-1. 휴면회원 조회
			user = new User();
			user.setUserId(userId);
			User wakeUpData = userMapper.getUserForWakeup(user);
			
			if (wakeUpData == null) {
//				throw new Exception("ERR_NOT_FOUND");
				code = "ERR_NOT_FOUND";
				return code;
			}
			
			// 2-2. 데이터 복호화
			decryptSleepUser(wakeUpData);
			wakeUpData.encrypt(userEncryptor);
			UserDetail userDetail = (UserDetail)wakeUpData.getUserDetail();
			userDetail.encrypt(userDetailEncryptor);
			wakeUpData.setUserDetail(userDetail);
			
			// 2-3. 휴면회원 해제
			userMapper.wakeupUser(wakeUpData);
			userMapper.wakeupUserDetail(wakeUpData);
			userMapper.deleteSleepUser(user);
		}
		
		// 3. 결과값
		code = "SUCC";
		
		return code;
	}
	
	/**
	 * 컬럼 데이터 복호화
	 * @param user
	 */
	private void decryptData(SleepUser sleepUser) {
		if (sleepUser != null) {
			sleepUser.decrypt(sleepUserEncryptor, ShopUtils.needMasking());
		}
	}
	
	/**
	 * 휴면대상정보 복호화 (UserServiceImpl 참조)
	 * @param user
	 * @throws Exception
	 */
	private void decryptSleepUser(User user) {
		user.setUserName(CipherUtilsCopy.decrypt(user.getUserName()));
		user.setEmail(CipherUtilsCopy.decrypt(user.getEmail()));

		UserDetail userDetail = (UserDetail) user.getUserDetail();
		userDetail.setNewPost(CipherUtilsCopy.decrypt(userDetail.getNewPost()));
		userDetail.setPost(CipherUtilsCopy.decrypt(userDetail.getPost()));
		userDetail.setAddress(CipherUtilsCopy.decrypt(userDetail.getAddress()));
		userDetail.setAddressDetail(CipherUtilsCopy.decrypt(userDetail.getAddressDetail()));
		userDetail.setTelNumber(CipherUtilsCopy.decrypt(userDetail.getTelNumber()));
		userDetail.setPhoneNumber(CipherUtilsCopy.decrypt(userDetail.getPhoneNumber()));
		userDetail.setFaxNumber(CipherUtilsCopy.decrypt(userDetail.getFaxNumber()));
		userDetail.setBirthdayType(CipherUtilsCopy.decrypt(userDetail.getBirthdayType()));
		userDetail.setBirthday(CipherUtilsCopy.decrypt(userDetail.getBirthday()));
		user.setUserDetail(userDetail);
	}
}
