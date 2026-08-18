package saleson.common.sms;

import java.util.List;

import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.common.enumeration.SmsType;
import saleson.common.sms.domain.HomeTownDayInfo;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.sms.domain.SmsIpsParam;
import saleson.common.sms.domain.TifIpsSndngM;
import saleson.common.sms.domain.TifIpsSndngMDisplay;
import saleson.shop.donation.domain.GiveUserSmsInfo;

public interface SmsIpsService {

	/**
	 * 국민비서발송기본 등록
	 * @param smsType		sms타입
	 * @param receiverInfo	수신정보
	 */
	public void insertTifIpsSndngM(List<ReceiverInfo> receiverInfo);

	/**
	 * 기부 sms 전송
	 * @param smsType		sms타입
	 * @param receiverInfo	수신정보
	 */
	public void giveSendSms(List<GiveUserSmsInfo> info, SmsType type);

	/**
	 * 070-A003 세액 공제 알림(전체 인원 대상)
	 * @param smsType		sms타입
	 * @param receiverInfo	수신정보
	 */
	public void sendTaxCreditInfo(String year);

	/**
	 * 070-A006 고향의날 알림(전체 인원 대상)
	 * @param smsType		sms타입
	 * @param receiverInfo	수신정보
	 */
	public void sendHometownDayInfo(HomeTownDayInfo info);


	/**
	 * sms 이력 조회
	 */
	List<TifIpsSndngMDisplay> getSmsSendList(SmsIpsParam searchParam);


	/**
	 * sms 이력 카운트
	 */
	int getSmsSendCnt(SmsIpsParam searchParam);

	/**
	 * <pre>
	 * comment       :
	 * preMethodName :
	 * author        : csh
	 * date          : 2024. 11. 27.
	 *
	 * </pre>
	 * void
	 */
	public String donationLimitAmtString();

}
