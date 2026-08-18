package saleson.shop.externalapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.joda.time.LocalDate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dreamsecurity.magicline.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;

import saleson.common.enumeration.SmsType;
import saleson.common.sms.SmsIpsService;
import saleson.shop.donation.NextBugaRequestDto;
import saleson.shop.donation.NgDonationMapper;
import saleson.shop.donation.NgDonationRelayService;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.donation.domain.UserCntrInfo;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service("externalApiServiceImpl")
@RequiredArgsConstructor
public class ExternalApiServiceImpl extends EgovAbstractServiceImpl implements ExternalApiService {

    private final ExternalApiMapper externalApiMapper;

    private final NgDonationMapper ngDonationMapper;

    private final SmsIpsService smsIpsService;


	@Value("${outconn.seoul-sunap-url}")
    private String seoulSunapUrl;

    @Autowired
    private final NgDonationRelayService ngDonationRelayService;

    /**
     * 민간개방 API 용 결제 상태 조회
     * @param paramMap
     * @return
     * @throws Exception
     */
    @Override
    public UserCntrInfo getUserCntrInfo(Map<String, String> paramMap) throws Exception {
        return externalApiMapper.getUserCntrInfo(paramMap);
    }

    /**
     * 민간개방 API etax 조회 용 mng no 생성
     * @return
     * @throws Exception
     */
    @Override
    public String getMngNo() throws Exception {
        return ngDonationMapper.getMngNoEtax();
    }

    /**
     * 민간개방 API 용 수납 처리
     * @return
     */
	@Override
	public Map<String, Object> sunapSuccess(Map<String, String> paramMap) {
		Map<String, Object> SERVICE_RESULT  = new HashMap<String, Object>();
		int sunap = externalApiMapper.sunapSuccess(paramMap);
		if(sunap > 0) {
			SERVICE_RESULT.put("sunap", "SUCCESS");

			// 기부 감사 인사
			GiveUserSmsInfo info = externalApiMapper.getSmsSendGiveUserInfo(paramMap);
//			smsIpsService.giveSendSms(Arrays.asList(info), SmsType.DONATION);

			SERVICE_RESULT.put("userId", info.getUserId());
		} else {
			SERVICE_RESULT.put("sunap", "FAIL");
		}

		return SERVICE_RESULT;
	}

	@Override
	public Map<String, Object> sunapSuccess2(Map<String, String> paramMap) {
		Map<String, Object> SERVICE_RESULT  = new HashMap<String, Object>();

		UserCntrInfo cntrInfo = externalApiMapper.getCntrInfoByPayNo(paramMap);
		if (cntrInfo == null) {			// 기부 내역 없을 경우
			SERVICE_RESULT.put("sunap", "FAIL");
			SERVICE_RESULT.put("msg", "내역 없음");
		} else {
			if ("100".equals(cntrInfo.getCntr_sttus_code())
					&& (cntrInfo.getSttemnt_pay_de() == null
							|| cntrInfo.getSttemnt_pay_de().isEmpty())) {		// 미납 상태일 경우

				if (cntrInfo.getCntr_locgov_code() != null) {
					boolean isChecked = false;
					if (cntrInfo.getCntr_locgov_code().startsWith("11")
							&& "Y".equals(cntrInfo.getSeoul_trget_at())) {		// 서울 세외 확인
						Gson gson = new Gson();

						HashMap<String, Object> API_REQUEST = new HashMap<>();
//						Map<String, Object> SERVICE_RELAY_RESULT = new HashMap<>();

						API_REQUEST.put("COM_REQ_MECHE", "GHLOVE");											// 요청매체
						API_REQUEST.put("COM_REQ_DT", DateUtils.getToday("yyyyMMdd"));				// 요청일자
				        API_REQUEST.put("COM_REQ_TM", DateUtils.getToday("HHmmss"));					// 요청일시
				        API_REQUEST.put("COM_PAY_MSG_NO", ngDonationMapper.getMngNoEtax());		// 전문대장관리번호
				        API_REQUEST.put("DATA_CNT", 1);																// 데이터 건수
				        API_REQUEST.put("systemCd", "02");							// 인터페이스 구분코드
				        API_REQUEST.put("jijacheCd", cntrInfo.getCntr_locgov_code());							// 지자체코드

				        JSONObject jo1 = new JSONObject();
				        jo1.put("EPAY_NO", cntrInfo.getElctrn_pay_no());
				        JSONArray ja = new JSONArray();
				        ja.add(jo1);

				        API_REQUEST.put("ARR_BU_INFO", ja);							// 전자납부번호 json array
				        String response;
				        try {
				        	response = ngDonationRelayService.restFulToRelayServer(seoulSunapUrl, API_REQUEST, "");
				        } catch(IOException e) {
				        	log.error("=================" + getClass().getName() + " sunapSuccess2 IOException :: API_REQUEST :: " + API_REQUEST.toString());
							SERVICE_RESULT.put("sunap", "FAIL");
							SERVICE_RESULT.put("msg", "IOEXCEPTION");
							return SERVICE_RESULT;
				        } catch (Exception e) {
				        	log.error("=================" + getClass().getName() + " sunapSuccess2 Exception :: API_REQUEST :: " + API_REQUEST.toString());
							SERVICE_RESULT.put("sunap", "FAIL");
							SERVICE_RESULT.put("msg", "EXCEPTION");
							return SERVICE_RESULT;
				        }

				    	JsonObject result = gson.fromJson(response, JsonObject.class);

				    	String resultCd = result.get("RST_CD").getAsString();
				    	String sunapYn = result.get("ARR_RESULT").getAsJsonArray().get(0).getAsJsonObject().get("SUNAP_YN").getAsString();

				    	if ("100".equals(resultCd) && "Y".equalsIgnoreCase(sunapYn)) {
				    		isChecked = true;
				    	}

					} else if (!cntrInfo.getCntr_locgov_code().startsWith("11")
							&& !"Y".equals(cntrInfo.getSeoul_trget_at())) {		// 지방 세외 확인
						Map<String, Object> result;
				        try {
				        	NextBugaRequestDto nextBugaRequestDto = new NextBugaRequestDto();

				        	nextBugaRequestDto.setLocgovCode(cntrInfo.getCntr_locgov_code());
				        	nextBugaRequestDto.setEpayNo(cntrInfo.getElctrn_pay_no());
				        	nextBugaRequestDto.setLinkMngKey(cntrInfo.getCntrSn());

				        	result = ngDonationRelayService.localSunapConfirm(nextBugaRequestDto);
				        } catch(IOException e) {
				        	log.error("=================" + getClass().getName() + " sunapSuccess2 IOException :: EpayNo :: " + cntrInfo.getElctrn_pay_no());
							SERVICE_RESULT.put("sunap", "FAIL");
							SERVICE_RESULT.put("msg", "IOEXCEPTION");
							return SERVICE_RESULT;
				        } catch (Exception e) {
				        	log.error("=================" + getClass().getName() + " sunapSuccess2 Exception :: EpayNo :: " + cntrInfo.getElctrn_pay_no());
							SERVICE_RESULT.put("sunap", "FAIL");
							SERVICE_RESULT.put("msg", "EXCEPTION");
							return SERVICE_RESULT;
				        }

				        if (result != null && result.get("status") != null && "SUCCESS".equals(result.get("status").toString())) {
							isChecked = true;
				        }
					} else {
						SERVICE_RESULT.put("sunap", "FAIL");
						SERVICE_RESULT.put("msg", "서울/지방세외 체크 못함 - 데이터 이상");
						return SERVICE_RESULT;
					}

					if (isChecked) {
						int sunap = externalApiMapper.sunapSuccess(paramMap);
						if(sunap > 0) {
							SERVICE_RESULT.put("sunap", "SUCCESS");

							// 기부 감사 인사
//							GiveUserSmsInfo info = externalApiMapper.getSmsSendGiveUserInfo(paramMap);
//							smsIpsService.giveSendSms(Arrays.asList(info), SmsType.DONATION);
							SERVICE_RESULT.put("msg", "성공");
//							SERVICE_RESULT.put("userId", info.getUserId());
						} else {
							SERVICE_RESULT.put("sunap", "FAIL");
							SERVICE_RESULT.put("msg", "수납 처리 UPDATE 실패");
						}
					} else {
						SERVICE_RESULT.put("sunap", "FAIL");
						SERVICE_RESULT.put("msg", "서울/지방세외 미수납");
					}
				} else {
					SERVICE_RESULT.put("sunap", "FAIL");
					SERVICE_RESULT.put("msg", "지자체 정보 없음 - 데이터 이상");
				}
			} else if ("200".equals(cntrInfo.getCntr_sttus_code())
						&& cntrInfo.getSttemnt_pay_de() != null
						&& !cntrInfo.getSttemnt_pay_de().isEmpty()) {			// 납부상태일 경우
				SERVICE_RESULT.put("sunap", "SUCCESS");
				SERVICE_RESULT.put("msg", "이미 납부처리");
			} else {
				SERVICE_RESULT.put("sunap", "FAIL");
				SERVICE_RESULT.put("msg", "미납 - 납부 체크 못함 : 데이터 이상");
			}
		}

		return SERVICE_RESULT;
	}
}
