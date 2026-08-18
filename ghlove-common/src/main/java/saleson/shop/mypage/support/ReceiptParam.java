package saleson.shop.mypage.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class ReceiptParam {

	private long userId;
	private String cntrSn;
	private String lclgvCd;

}
