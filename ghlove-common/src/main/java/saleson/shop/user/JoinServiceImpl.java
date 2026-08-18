package saleson.shop.user;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;

import lombok.RequiredArgsConstructor;
import saleson.common.enumeration.SmsType;
import saleson.common.sms.SmsIpsService;
import saleson.shop.categories.domain.Categories;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.PolicyInfo;
import saleson.shop.user.domain.RtnpsntInfo;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.userrole.UserRoleService;

@RequiredArgsConstructor
@Service("joinService")
public class JoinServiceImpl implements JoinService {
	private static final Logger log = LoggerFactory.getLogger(JoinServiceImpl.class);

	private final JoinMapper JoinMapper;
	private final UserMapper userMapper;

	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;

	@Autowired
	private SmsIpsService smsIpsService;

	@Override
	public List<LocGovInfo> getLocGovList(LocGovInfo locGovInfo) {

		return JoinMapper.getLocGovList(locGovInfo);
	}

	@Override
	public int getUserInfoByUserId(String userID) {

		return JoinMapper.getUserInfoByUserId(userID);
	}

	@Override
	public int insertIntrstLocgovInfo(LocGovInfo locGovInfo) {

		return JoinMapper.insertIntrstLocgovInfo(locGovInfo);
	}

	@Override
	public int insertInststRtnpsntInfo(RtnpsntInfo rtnpsntInfo) {
		return JoinMapper.insertInststRtnpsntInfo(rtnpsntInfo);
	}

	@Override
	public List<Categories> getCategoryList() {
		return JoinMapper.getCategoryList();
	}

	@Override
	public int updateUserInfo(UserDetail userDetail) {
		return JoinMapper.updateUserInfo(userDetail);
	}

	@Override
	public PolicyInfo getPolicyInfo(String policyType) {
		return JoinMapper.getPolicyInfo(policyType);
	}

	@Override
	public void insertUserAndUserDetail(User user, UserDetail userDetail) {

		long userId = user.getUserId();
		String userName = user.getUserName();
		userService.insertUser(user);
		userService.insertUserDetail(userDetail);

		UserRole userRole = new UserRole();
		userRole.setUserId(userId);
		userRole.setAuthority("ROLE_USER");
		userRoleService.insertUserRole(userRole);

		user = userService.getUserByUserId(userId);

		JoinMapper.updateUserInfo(userDetail);
		userMapper.updateUserEtcInfo(userDetail);

		// 2022.02.13 loginPathCode 추가
		JoinMapper.updateloginPathCode(userDetail);

		userDetail.setUserId(userId);

		if(userDetail.getLocGovList() !=null) {
			for(int i =0; i<userDetail.getLocGovList().length; i++) {
				LocGovInfo locGovInfo = new LocGovInfo();
				locGovInfo.setUserId(String.valueOf(userDetail.getUserId()));
				locGovInfo.setLocgovCode(userDetail.getLocGovList()[i]);
				JoinMapper.insertIntrstLocgovInfo(locGovInfo);
			}
		}

		if(userDetail.getRtnpsntList() !=null) {
			for(int i =0; i<userDetail.getRtnpsntList().length; i++) {
				RtnpsntInfo rtnpsntInfo = new RtnpsntInfo();
				rtnpsntInfo.setLocgovCode(userDetail.getLocGovCode());
				rtnpsntInfo.setUserId(String.valueOf(userDetail.getUserId()));
				rtnpsntInfo.setCategoryCode(userDetail.getRtnpsntList()[i]);
				JoinMapper.insertInststRtnpsntInfo(rtnpsntInfo);
			}
		}

		//userMapper.insertUserBirthday(userDetail);		// 통계용 생년월일 추가

		// 국민비서 알리미 추가
		GiveUserSmsInfo info = new GiveUserSmsInfo();
		info.setUserId(userId);
		info.setUserName(userName);
		info.setMberCi(userDetail.getMberCi());
		info.setReceiveSms("0");
		info.setPhoneNumber(userDetail.getPhoneNumber().replaceAll("-", ""));
		smsIpsService.giveSendSms(Arrays.asList(info), SmsType.JOIN_MEMBERSHIP);

	}

}
