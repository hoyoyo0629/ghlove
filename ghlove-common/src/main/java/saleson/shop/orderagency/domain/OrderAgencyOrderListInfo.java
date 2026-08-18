package saleson.shop.orderagency.domain;

import org.springframework.util.StringUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.common.utils.UserUtils;

@Data
@NoArgsConstructor
public class OrderAgencyOrderListInfo {

	// 지자체명
	private String locgovNm;
	
	// 주문일
	private String payDate;
	
	// 주문번호
	private String orderCode;
	
	// 주문자 이름
	private String buyerName;
	
	// 수령인 이름
	private String receiveName;
	
	// 답례품명
	private String itemName;
	
	// 주문대행 관리자 이름
	private String managerNm;
	
	// 대행주문 관리자 아이디
	private long managerId;
	
	// 주문대행 관리자 행정복지센터명(또는 권한명)
	private String pbadmsWlfrCntrNm;
	
	public String getBuyerName() {
		if (UserUtils.getUser() != null && managerId == UserUtils.getUser().getUserId()) {
			return buyerName;
		}
		if (StringUtils.hasLength(buyerName)) {
			if (UserUtils.hasLocgovManagerRole()) {
				return buyerName;
			} else {
				int length = buyerName.length();
				if (length == 2) {
					return buyerName.substring(0, 1) + "*";
				} else if (length > 2) {
					return buyerName.substring(0, 1) + "*" + buyerName.substring(length - 1);
				}
			}
		}
		return buyerName;
	}
	
	public String getReceiveName() {
		if (UserUtils.getUser() != null && managerId == UserUtils.getUser().getUserId()) {
			return receiveName;
		}
		if (StringUtils.hasLength(receiveName)) {
			if (UserUtils.hasLocgovManagerRole()) {
				return receiveName;
			} else {
				int length = receiveName.length();
				if (length == 2) {
					return receiveName.substring(0, 1) + "*";
				} else if (length > 2) {
					StringBuffer buf = new StringBuffer();
					buf.append(receiveName.substring(0, 1));
					for(int i = 0 ; i < length - 2 ; i++) {
						buf.append("*");
					}
					buf.append(receiveName.substring(length - 1));
					return buf.toString();
				}
			}
		}
		return receiveName;
	}
	
}
