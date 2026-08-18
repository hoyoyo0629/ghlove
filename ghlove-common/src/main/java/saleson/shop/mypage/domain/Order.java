package saleson.shop.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	
	private String orderStatus;
	private String orderLabel;
	private String orderCount;

}
