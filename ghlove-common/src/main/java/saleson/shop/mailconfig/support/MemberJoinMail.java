package saleson.shop.mailconfig.support;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.MessageUtils;
import saleson.common.configuration.SalesonProperty;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.utils.ShopUtils;
import saleson.shop.config.domain.Config;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.user.domain.UserDetail;

import java.util.HashMap;

public class MemberJoinMail extends MailTemplate {
	public HashMap<String, String> getMap() {
		HashMap<String, String> map = new HashMap<>();

		map.put("addr", "주소");
		map.put("addrDetail", "상세주소");
		map.put("email", "이메일");
		map.put("emoney", "E-Money");
		map.put("id", "아이디");
		map.put("mileage", MessageUtils.getMessage("M00246")); // 포인트
		map.put("mobile", "핸드폰번호");
		map.put("name", "이름");
		map.put("phone", "전화번호");
		//map.put("regday", "등록일");
		//map.put("regmonth", "등록월");
		//map.put("regyear", "등록년");
		map.put("regdate", "등록일자");
		
		map.put("siteName", "상점명");
		map.put("siteUrl", "상점URL");
		map.put("zipcode", "우편번호");
		
		return map;
	}
	
	public MemberJoinMail() {
		this.setMap(this.getMap());
	}
	
	public MemberJoinMail(User user, MailConfig mailConfig, Cryptor cryptor, DataMasking dataMasking) {
		super(mailConfig, cryptor, dataMasking);
	
		HashMap<String, String> map = new HashMap<>();
		UserDetail userDetail = (UserDetail) user.getUserDetail();
		
		String date = DateUtils.date(user.getCreatedDate());
		
		map.put("addr", userDetail.getAddress());
		map.put("addrDetail", getCryptor().decrypt(userDetail.getAddressDetail()));
		map.put("email", getCryptor().decrypt(user.getEmail()));
		map.put("emoney", "");
		map.put("id", user.getLoginId());
		map.put("mileage", ""+userDetail.getPoint());
		map.put("mobile", getCryptor().decrypt(userDetail.getPhoneNumber()));
		map.put("name", getCryptor().decrypt(user.getUserName()));
		map.put("phone", getCryptor().decrypt(userDetail.getTelNumber()));
		//map.put("regday", DateUtils.formatDate(date, "dd"));
		//map.put("regmonth", DateUtils.formatDate(date, "MM"));
		//map.put("regyear", DateUtils.formatDate(date, "yyyy"));
		map.put("regdate", date);
		map.put("zipcode", userDetail.getPost());
		
		
		Config config = ShopUtils.getConfig();
		map.put("siteName", config.getShopName());
		map.put("siteUrl", SalesonProperty.getSalesonUrlShoppingmall());
		
		this.setMap(map);
	}
	
}
