package saleson.shop.userdelivery;

import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.userdelivery.domain.UserDelivery;
import saleson.shop.userdelivery.domain.UserDeliveryEncryptor;
import saleson.shop.userdelivery.support.UserDeliveryParam;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("userDeliveryService")
public class UserDeliveryServiceImpl  implements UserDeliveryService {

	private final UserDeliveryMapper userDeliveryMapper;
	private final SequenceService sequenceService;
	private final UserDeliveryEncryptor userDeliveryEncryptor;

	@Override
	public List<UserDelivery> getUserDeliveryList(long userId) {
		
		if (userId == 0) {
			return null;
		}
		
		UserDeliveryParam param = new UserDeliveryParam();
		param.setUserId(userId);

		return getUserDeliveryListByParam(param);
	}
	
	@Override
	public UserDelivery getDefaultUserDelivery() {
		UserDeliveryParam param = new UserDeliveryParam();
		
		if (!UserUtils.isUserLogin()) {
			return null;
		}
		
		param.setUserId(UserUtils.getUserId());
		UserDelivery userDelivery = userDeliveryMapper.getDefaultUserDelivery(param);

		decryptData(userDelivery);
		return userDelivery;
	}
	
	@Override
	public UserDelivery getUserDeliveryById(long userId, int userDeliveryId) {
		UserDeliveryParam param = new UserDeliveryParam();
		
		if (userId == 0) {
			return null;
		}
		
		param.setUserId(userId);
		param.setUserDeliveryId(userDeliveryId);

		UserDelivery userDelivery = userDeliveryMapper.getUserDeliveryById(param);

		decryptData(userDelivery);
		return userDelivery;
	}

	private void decryptData(UserDelivery userDelivery) {
		if (userDelivery != null) {
			userDelivery.decrypt(userDeliveryEncryptor, false);
		}
	}
	
	@Override
	public void insertUserDelivery(UserDelivery userDelivery) {
		
		if (userDelivery.getUserId() == 0) {
			return;
		}
		
		userDelivery.setUserDeliveryId(sequenceService.getId("OP_USER_DELIVERY"));
		
		
		if (ObjectUtils.isEmpty(userDelivery.getDefaultFlag())) {
			userDelivery.setDefaultFlag("N");
		} else {
			if ("Y".equals(userDelivery.getDefaultFlag())) {

				// 기본 배송지는 1개만 유지 하기 위해 회원의 전체 데이터를 초기화함
				userDeliveryMapper.initializationDefaultFlag(userDelivery.getUserId());
				
			}
		}

		userDelivery.encrypt(userDeliveryEncryptor);
		userDeliveryMapper.insertUserDelivery(userDelivery);
	}

	@Override
	public void updateUserDelivery(UserDelivery userDelivery) {
		if (userDelivery.getUserId() == 0) {
			return;
		}
		
		if (ObjectUtils.isEmpty(userDelivery.getDefaultFlag())) {
			userDelivery.setDefaultFlag("N");
		} else {
			if ("Y".equals(userDelivery.getDefaultFlag())) {

				// 기본 배송지는 1개만 유지 하기 위해 회원의 전체 데이터를 초기화함
				userDeliveryMapper.initializationDefaultFlag(userDelivery.getUserId());
				
			}
		}

		userDelivery.encrypt(userDeliveryEncryptor);
		userDeliveryMapper.updateUserDelivery(userDelivery);
	}
	
	@Override
	public void listAction(UserDeliveryParam param) {
		
		if (param.getUserId() == 0) {
			return;
		}
		
		
		if ("del".equals(param.getMode())) {
			
			userDeliveryMapper.deleteUserDeliveryByParam(param);
				
		} else if ("mod".equals(param.getMode())) {
				
			// 기본 배송지는 1개만 유지 하기 위해 회원의 전체 데이터를 초기화함
			userDeliveryMapper.initializationDefaultFlag(param.getUserId());
	
			userDeliveryMapper.updateDefaultFlagByParam(param);

		}
		
	}

	@Override
	public int getDeliveryCount(long userId) {

		UserDeliveryParam param = new UserDeliveryParam();
		param.setUserId(userId);

		return userDeliveryMapper.getDeliveryCount(param);
	}

	@Override
	public List<UserDelivery> getUserDeliveryListByParam(UserDeliveryParam param) {
		List<UserDelivery> userDeliveries = userDeliveryMapper.getUserDeliveryListByParam(param);

		userDeliveries.
				forEach(ud ->
						ud.decrypt(userDeliveryEncryptor, ShopUtils.needMasking()));

		return userDeliveries;
	}
}
