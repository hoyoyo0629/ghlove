package saleson.shop.order.claimapply.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.common.enumeration.OrderLogType;
import saleson.common.enumeration.UserType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLog  {
	private Long id;
	private OrderLogType logType;
	private String  orderCode;
	private Integer orderSequence;
	private Integer itemSequence;
    private String itemName;
	private String orderStatus;
	private String orgOrderStatus;
	private UserType userType;
	private String ip;
	private String createdBy;
	private String updatedBy;
	private String createdAtText;
	private String updatedAtText;
	private String createdAt;
	private String updatedAt;
	private String options;
	private String textOption;
}
