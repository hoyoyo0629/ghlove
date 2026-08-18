package saleson.shop.email.ems;

import java.util.List;
import java.util.Map;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.common.configuration.MapperEms;
import saleson.shop.donation.domain.CntrLmtt;
import saleson.shop.donation.domain.DoLocGovInfo;
import saleson.shop.donation.domain.HonorCntr;
import saleson.shop.donation.domain.UserCntrInfo;
import saleson.shop.donation.support.ContryParam;
import saleson.shop.donation.support.DonationParam;
											   
import saleson.shop.donation.support.NtsParam;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.donation.support.SidoListParam;
import saleson.shop.email.domain.Email;
import saleson.shop.email.domain.EmailDetail;
import saleson.shop.email.domain.EmailFile;
import saleson.shop.email.domain.EmailSend;
import saleson.shop.email.domain.EmsTotalCnt;
import saleson.shop.email.support.EmailDetailParam;
import saleson.shop.email.support.EmailParam;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.UserDetail;

@MapperEms("emsMapper")
public interface EmsMapper {
	
	Map<String, Object> getEmsData(String msgId);
	
	List<EmsTotalCnt> getEmsReport(List<Email> list);
	
	EmsTotalCnt getEmsTotalCnt(long emailId);
	
	int getEmsUserCnt(EmailDetailParam params);
	
	List<EmailSend> getEmsUserList(EmailDetailParam params);
	
}
