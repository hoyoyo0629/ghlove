package saleson.shop.externalapi;

import saleson.shop.donation.domain.UserCntrInfo;

import java.util.Map;

public interface ExternalApiService {

    /**
     * 결제 상태 조회
     * @param paramMap
     * @return
     * @throws Exception
     */
    public UserCntrInfo getUserCntrInfo(Map<String, String> paramMap) throws Exception;

    /**
     * etax 조회 용 mng no 생성
     * @return
     * @throws Exception
     */
    public String getMngNo() throws Exception;

    /**
     * 민간개방 API 수납 완료 처리
     * @return
     * @throws Exception
     */
    public Map<String, Object> sunapSuccess(Map<String, String> paramMap);

    /**
     * 민간개방 API 수납 완료 처리2
     * @return
     * @throws Exception
     */
    public Map<String, Object> sunapSuccess2(Map<String, String> paramMap);
}
