package saleson.shop.user.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancTokenResult {
	
	private String access_token;
	private String token_type;
	private int expires_in;
	private String scope;
	private String api_tran_id;
	private String server_id;
	
	private String err_msg;
	private String err_code;
	
}
