package saleson.shop.donation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import saleson.shop.donation.support.ContryParam;
import saleson.shop.donation.support.DonationParam;
import saleson.shop.donation.support.SeoulParam;

public interface NgDonationRelayService {

	/**
	 * <pre>
	 * comment       : 행정망공동이용센터 주소간단조회 서비스 api
	 * preMethodName : rsgstadresinfo
	 * author        : hybrid
	 * date          : 2023. 2. 24.
	 *
	 * </pre>
	 * @param sidoListParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> rsgstadresinfo(DonationParam donationParam) throws Exception;


	/**
	 * <pre>
	 * comment       : 서울세외시스템 부과정보 등록 api
	 * preMethodName : sntrBugaInsert
	 * author        : hybrid
	 * date          : 2023. 2. 24.
	 *
	 * </pre>
	 * @param seoulParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> sntrBugaInsert(SeoulParam seoulParam) throws Exception;

	/**
	 * <pre>
	 * comment       : ETAX 수납 확인 api
	 * preMethodName : etaxSunapInfo
	 * author        : hybrid
	 * date          : 2023. 2. 24.
	 *
	 * </pre>
	 * @param seoulParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> etaxSunapInfo(SeoulParam seoulParam) throws Exception;

	/**
	 * <pre>
	 * comment       : 지방세외(현세대) 부과정보 등록
	 * preMethodName : contryBugaInsert
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @param seoulParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> contryBugaInsert(ContryParam contryParam) throws Exception;

	/**
	 * <pre>
	 * comment       : 지방세외(현세대) 수납확인 요청
	 * preMethodName : contrySunapInfo
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @param contryParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> contrySunapInfo(ContryParam contryParam) throws Exception;

	/**
	 * <pre>
	 * comment       : 지방세외(현세대) 수납확인 요청
	 * preMethodName : contryNextSunapInfo
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @param contryParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> contryNextSunapInfo(ContryParam contryParam) throws Exception;

	/**
	 * <pre>
	 * comment       :국세청 영수증 처리
	 * preMethodName : createEreceipt
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @param seoulParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> sendNtsEreceipt(String accessToken, String sendNtsEreceipt) throws Exception;

	/**
	 * <pre>
	 * comment       : 국세청 영수증 연계를 위한 access token 가져오기
	 * preMethodName : getAccessToken
	 * author        : hybrid
	 * date          : 2023. 2. 28.
	 *
	 * </pre>
	 * @return
	 * String
	 */
	public String getAccessToken() throws Exception;

	/**
	 * <pre>
	 * comment       : 금결원 지로 결제
	 * preMethodName : giroPay
	 * author        : hybrid
	 * date          : 2023. 2. 27.
	 *
	 * </pre>
	 * @param params
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> giroPay(HashMap<String,String> params) throws Exception;

	/**
	 * <pre>
	 * comment       : 금결원에서 요청한 지로 테스트 화면을 위한 서비스
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 11. 6.
	 *
	 * </pre>
	 * @param params
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> giroPayTest(HashMap<String,String> params) throws Exception;

	/**
	 * <pre>
	 * comment       : 행정정보공동이용시스템 api 호출 대민화면(재외국민 조회, 외국인 등록사실 증명)
	 * preMethodName : rsgstadresinfoForeign
	 * date          : 2023. 8. 21.
	 *
	 * </pre>
	 * @param donationParam
	 * @return Map<String,Object>
	 * @throws IOException
	 *
	 */
	public Map<String, Object> rsgstadresinfoForeigner(DonationParam donationParam) throws Exception;

	/**
	 * <pre>
	 * comment       : RSA 암호화 키 생성하기
	 * preMethodName : getPublicKey
	 * date          : 2023. 8. 22.
	 *
	 * </pre>
	 * @param
	 * @return
	 * @throws
	 *
	 */
	public String getRsaPublicKey();

	/**
	 * <pre>
	 * comment       : RSA 복호화
	 * preMethodName : decryptByRsaPrivateKey
	 * date          : 2023. 8. 22.
	 *
	 * </pre>
	 * @param donationParam
	 * @return Map<String,Object>
	 * @throws IOException
	 *
	 */
	public String decryptByRsaPrivateKey(String encrypted) throws IOException;

	/**
	 * <pre>
	 * comment       : 행정정보공동이용시스템 api 호출 공통(재외국민 조회, 외국인 등록사실 증명)
	 * preMethodName : getForeignApiData
	 * date          : 2023. 9. 25.
	 *
	 * </pre>
	 * @param donationParam
	 * @return Map<String,Object>
	 * @throws IOException
	 *
	 */
	public Map<String, Object> getForeignApiData(HashMap<String, Object> requestParam, String checkLocgovCode) throws Exception;

	/**
	 * <pre>
	 * comment       : 지방세외 차세대 부과 등록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 3. 9.
	 *
	 * </pre>
	 * @param nextGnrParam
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> nextBugaRequest(NextBugaRequestDto nextBugaRequestDto) throws Exception;

	/**
	 * <pre>
	 * comment       : 지방세외(현세대) 수납확인 요청
	 * preMethodName : contryNextSunapInfo
	 * author        : ghl004
	 * date          : 2024. 12. 06.
	 *
	 * </pre>
	 * @param NextBugaRequestDto
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public Map<String, Object> localSunapConfirm(NextBugaRequestDto nextBugaRequestDto) throws Exception;
	
	/**
	 * <pre>
	 * comment       : restApi 통신
	 * preMethodName : restFulToRelayServer
	 * author        : ghl008
	 * date          : 2024. 12. 13.
	 *
	 * </pre>
	 * @param NextBugaRequestDto
	 * @return
	 * @throws Exception
	 * Map<String,Object>
	 */
	public String restFulToRelayServer(String API_URL, HashMap<String, Object> API_REQUEST, String accessToken) throws Exception;
	
}
