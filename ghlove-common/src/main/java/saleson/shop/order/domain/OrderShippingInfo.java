package saleson.shop.order.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class OrderShippingInfo {

	public OrderShippingInfo(String orderCode, int orderSequence, int shippingInfoSequence, Receiver receiver) {
		
		setOrderCode(orderCode);
		setOrderSequence(orderSequence);
		setShippingInfoSequence(shippingInfoSequence);
		
		setReceiveNewZipcode(receiver.getReceiveNewZipcode());
		setReceiveZipcode(receiver.getReceiveZipcode());
		setReceiveSido(receiver.getReceiveSido());
		setReceiveSigungu(receiver.getReceiveSigungu());
		setReceiveEupmyeondong(receiver.getReceiveEupmyeondong());
		setReceiveAddress(receiver.getReceiveAddress());
		setReceiveAddressDetail(receiver.getReceiveAddressDetail());
		setReceiveName(receiver.getReceiveName());
		setReceivePhone(receiver.getReceivePhone());
		setReceiveMobile(receiver.getReceiveMobile());
		setMemo(receiver.getContent());
		
	}
	
	private List<OrderItem> orderItems;
	private String orderCode;
	private int orderSequence;
	private int shippingInfoSequence;

	private String receiveZipcode;
	private String receiveNewZipcode;
	private String receiveCompanyName;
	private String receiveSido;
	private String receiveSigungu;
	private String receiveEupmyeondong;
	private String receiveAddress;
	private String receiveAddressDetail;

	private String receiveName;
	private String receivePhone;
	private String receiveMobile;

	private String receiveZipcode1;
	private String receiveZipcode2;

	private String receivePhone1;
	private String receivePhone2;
	private String receivePhone3;

	private String receiveMobile1;
	private String receiveMobile2;
	private String receiveMobile3;

	private String memo;
	private String createdDate;
	private String updatedDate;

	public String getReceiveMobile1() {
		if (receiveMobile1 != null && !receiveMobile1.isEmpty()) return receiveMobile1;
		if (receiveMobile == null || receiveMobile.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receiveMobile)[0];
	}

	public String getReceiveMobile2() {
		if (receiveMobile2 != null && !receiveMobile2.isEmpty()) return receiveMobile2;
		if (receiveMobile == null || receiveMobile.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receiveMobile)[1];
	}

	public String getReceiveMobile3() {
		if (receiveMobile3 != null && !receiveMobile3.isEmpty()) return receiveMobile3;
		if (receiveMobile == null || receiveMobile.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receiveMobile)[2];
	}

	public String getReceivePhone1() {
		if (receivePhone1 != null && !receivePhone1.isEmpty()) return receivePhone1;
		if (receivePhone == null || receivePhone.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receivePhone)[0];
	}

	public String getReceivePhone2() {
		if (receivePhone2 != null && !receivePhone2.isEmpty()) return receivePhone2;
		if (receivePhone == null || receivePhone.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receivePhone)[1];
	}

	public String getReceivePhone3() {
		if (receivePhone3 != null && !receivePhone3.isEmpty()) return receivePhone3;
		if (receivePhone == null || receivePhone.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receivePhone)[2];
	}

	public void processHyphen() {
		this.receiveMobile = StringUtils.hasText(this.receiveMobile1) ? this.receiveMobile1 + '-' + this.receiveMobile2 + '-' + this.receiveMobile3 : "";
		this.receivePhone = StringUtils.hasText(this.receivePhone1) ? this.receivePhone1 + '-' + this.receivePhone2 + '-' + this.receivePhone3 : "";
	}

	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(DataEncryptor encryptor) {
		encryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}
	
	public String getExistsMobileItem() {
		if (SellerUtils.isSellerLogin()) {
			if (orderItems != null && orderItems.size() > 0) {
				for (OrderItem orderItem : orderItems) {
					if (orderItem != null && "Y".equalsIgnoreCase(orderItem.getMobileItemYn())) {
						return "Y";
					}
				}
			}
		}
		return "N";
	}
	
	public String getExistsNewOrderItem() {
		if (SellerUtils.isSellerLogin()) {
			if (orderItems != null && orderItems.size() > 0) {
				for (OrderItem orderItem : orderItems) {
					if (orderItem != null && "10".equals(orderItem.getOrderStatus())) {
						return "Y";
					}
				}
			}	
		}
		return "N";
	}
	
	
	
}
