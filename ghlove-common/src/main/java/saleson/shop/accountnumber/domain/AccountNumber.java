package saleson.shop.accountnumber.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class AccountNumber {
	private int shopConfigId = 1;
	private int accountNumberId;
	private String bankName;
	private String accountNumber;
	private String accountHolder;
	private String useFlag = "Y";
	private long userId;
	private String created;

}
