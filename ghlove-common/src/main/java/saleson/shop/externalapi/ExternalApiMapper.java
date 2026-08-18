package saleson.shop.externalapi;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.donation.domain.UserCntrInfo;

import java.util.Map;

@Mapper("externalApiMapper")
public interface ExternalApiMapper {

    /**
     * 민간개방 API 결제 상태 조회
     * @param paramMap
     * @return
     */
    UserCntrInfo getUserCntrInfo(Map<String, String> paramMap);
    
    /**
     * 민간개방 API 수납 완료 처리
     * @param paramMap
     * @return
     */
    int sunapSuccess(Map<String, String> paramMap);

	/**
	 * 민간개방 API 국민비서 알리미(기부감사인사) 사용자 정보
	 * @param paramMap
	 * @return
	 */
	GiveUserSmsInfo getSmsSendGiveUserInfo(Map<String, String> paramMap);
	
	/**
	 * 민간개방 API 국민비서 알리미(기부감사인사) 사용자 정보
	 * @param paramMap
	 * @return
	 */
	public UserCntrInfo getCntrInfoByPayNo(Map<String, String> paramMap);
	
	
}
