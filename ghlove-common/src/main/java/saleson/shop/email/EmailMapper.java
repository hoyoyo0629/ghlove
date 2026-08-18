package saleson.shop.email;

import java.util.List;
import java.util.Map;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.onlinepowers.framework.security.userdetails.User;

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
import saleson.shop.email.support.EmailParam;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.UserDetail;

@Mapper("emailMapper")
public interface EmailMapper {
	
	int getEmailListCount(EmailParam searchParams);
	
	List<Email> getEmailList(EmailParam searchParams);
	
	int insertEmail(Email email);
	
	int insertEmailFile(EmailFile file);
	
	List<EmailSend> sendUserList(Email email);
	
	int updateEmailStatus(Email email);
	
	int insertEmailDetail(EmailDetail email);
	
	Email getEmailDetail(long emailId);
	
	List<EmailSend> getUserList(String userName);
	
	List<EmailSend> sendSellerUserList(Email email);
	
	EmailFile getEmailFile(int emailFileId);
	
}
