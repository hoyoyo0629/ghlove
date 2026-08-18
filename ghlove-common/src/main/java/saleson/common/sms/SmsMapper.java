package saleson.common.sms;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.common.sms.domain.TifIpsSndngM;

@Mapper("smsMapper")
public interface SmsMapper {

	/**
	 * 국민비서발송기본 등록
	 * @param tifIpsSndngM
	 */
	void insertTifIpsSndngM(TifIpsSndngM tifIpsSndngM);
	
	/**
	 * 070-A003 세액 공제 알림(전체 인원 대상)
	 * @param tifIpsSndngM
	 */
	void insertTifIpsSndngMTaxCreditInfo(TifIpsSndngM tifIpsSndngM);
	
	/**
	 * 070-A006 고향의 날 알림 서비스(전체 인원 대상) 
	 * @param tifIpsSndngM
	 */
	void insertTifIpsSndngMHometownDayInfo(TifIpsSndngM tifIpsSndngM);
	
	/**
	 * sms 시퀀스 업데이트
	 */
	public void updateOpSequence();
	
	/**
	 * sms 이력 조회
	 */
	List<TifIpsSndngM> getSmsSendList(SearchParam searchParam);
	
	/**
	 * sms 이력 카운트
	 */
	int getSmsSendCnt(SearchParam searchParam);
	
	/**
	 * 문자 시퀀스 채번
	 * @return
	 */
	long getInsttCrtSn();

}
