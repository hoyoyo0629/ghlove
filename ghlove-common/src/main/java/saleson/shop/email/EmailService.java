package saleson.shop.email;

import java.util.List;

import com.onlinepowers.framework.security.userdetails.User;

import saleson.shop.email.domain.Email;
import saleson.shop.email.domain.EmailFile;
import saleson.shop.email.domain.EmailSend;
import saleson.shop.email.domain.EmsTotalCnt;
import saleson.shop.email.support.EmailDetailParam;
import saleson.shop.email.support.EmailParam;
import saleson.shop.email.support.SendParam;

public interface EmailService {
	
	int getEmailListCount(EmailParam searchParams);
	
	List<Email> getEmailList(EmailParam searchParams);
	
	Email insertEmail(Email email);
	
	boolean sendEmail(SendParam param);
	
	List<EmailSend> getUserList(String userName);
	
	Email getEmailDetail(long emailId);
	
	EmsTotalCnt getEmsTotalCnt(long emailId);
	
	int getEmsUserCnt(EmailDetailParam params);
	
	List<EmailSend> getEmsUserList(EmailDetailParam params);
	
	EmailFile getEmailFile(int emailFileId);
	
}
