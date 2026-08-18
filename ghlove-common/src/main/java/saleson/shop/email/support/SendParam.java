package saleson.shop.email.support;

import java.util.List;

import lombok.Data;
import saleson.shop.email.domain.EmailSend;

@Data
public class SendParam {
	
	private long emailId;
	private List<EmailSend> sendUserList;
	
}
