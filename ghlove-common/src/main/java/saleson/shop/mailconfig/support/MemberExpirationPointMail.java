package saleson.shop.mailconfig.support;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.StringUtils;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.PointType;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.utils.CommonUtils;
import saleson.shop.config.domain.Config;
import saleson.shop.givepointexpiration.domain.GivePointExpirationMail;
import saleson.shop.mailconfig.domain.MailConfig;

import java.text.DecimalFormat;
import java.util.HashMap;

public class MemberExpirationPointMail extends MailTemplate {
	public HashMap<String, String> getMap() {
		HashMap<String, String> map = new HashMap<>();
		map.put("email", "이메일");
		map.put("id", "아이디");
		map.put("name", "이름");
		map.put("expirationDate", "소멸 일자");
		map.put("expirationPoint", "소멸 포인트");
		map.put("searchDate", "조회 일자");
		map.put("siteName", "상점명");
		return map;
	}
	
	public MemberExpirationPointMail() {
		this.setMap(this.getMap());
	}

	/**
	 * 포인트 만료 메일 발송
	 * @param givePointExpirationMail
	 * @param mailConfig
	 * @param cryptor
	 * @param dataMasking
	 */
	public MemberExpirationPointMail(GivePointExpirationMail givePointExpirationMail, MailConfig mailConfig, String siteName, Cryptor cryptor, DataMasking dataMasking) {
		super(mailConfig, cryptor, dataMasking);
		
		HashMap<String, String> map = new HashMap<>();
		map.put("email"				, givePointExpirationMail.getEmail());
		map.put("id"				, givePointExpirationMail.getLoginId());
		map.put("name"				, givePointExpirationMail.getUserName());
		map.put("expirationDate"	, givePointExpirationMail.getPointEndDe());
		map.put("searchDate"		, DateUtils.getToday("yyyy년MM월dd일"));
		map.put("siteName"			, siteName);
		map.put("expirationPoint"	, String.valueOf(givePointExpirationMail.getPoint()));
		
		// 포인트 세자리 콤마 처리
		if(CommonUtils.intNvl(givePointExpirationMail.getPoint()) > 0) {
			DecimalFormat formatter = new DecimalFormat("###,###");
			map.put("expirationPoint", formatter.format(givePointExpirationMail.getPoint()));
		}
		
		this.setMap(map);
	}
	
	/**
	 * SalesOn version (2022.12.13 사용 안함)
	 * @param mailConfig
	 * @param user
	 * @param searchDate 패턴이 yyyy년MM월dd일
	 * @param expirationDate 패턴이 yyyy년MM월dd일
	 * @param pointMap
	 */
	public MemberExpirationPointMail(MailConfig mailConfig, User user, String searchDate, String expirationDate, HashMap<String,Integer> pointMap, Config config, Cryptor cryptor, DataMasking dataMasking) {
		super(mailConfig, cryptor, dataMasking);
	
		HashMap<String, String> map = new HashMap<>();
		//UserDetail userDetail = (UserDetail) user.getUserDetail();

		map.put("email", getCryptor().decrypt(user.getEmail()));
		map.put("id", user.getLoginId());
		map.put("name", getCryptor().decrypt(user.getUserName()));

		map.put("expirationDate", expirationDate);
		map.put("searchDate", searchDate );
		
		/*int expirationEmoney = getPointByPtCode(pointMap, "3002");
		int expirationMileage = getPointByPtCode(pointMap, "3001");
		int expirationPoint = getPointByPtCode(pointMap, "3003");
		int expirationCoupon = getPointByPtCode(pointMap, "3004");*/
		int expirationEmoney = getPointByPtCode(pointMap, PointType.EMONEY.getTitle());
		int expirationMileage = getPointByPtCode(pointMap, PointType.MILEAGE.getTitle());
		int expirationPoint = getPointByPtCode(pointMap, PointType.POINT.getTitle());
		int expirationCoupon = getPointByPtCode(pointMap, PointType.SHIPPING.getTitle());


		String separator = "\n";
		
		// 포인트 상세 내용
		String expirationContent = "";
		
		// 소멸 마일리지
		if (expirationMileage > 0) {
			expirationContent += expirationContentByPtCode(PointType.MILEAGE.getTitle(), expirationMileage)+separator;
		}
		// 소멸 E머니
		if (expirationEmoney > 0) {
			expirationContent += expirationContentByPtCode(PointType.EMONEY.getTitle(), expirationEmoney)+separator;
		}
		// 소멸 포인트
		if (expirationPoint > 0) {
			expirationContent += expirationContentByPtCode(PointType.POINT.getTitle(), expirationPoint)+separator;
		}
		// 소멸 배송쿠폰
		if (expirationCoupon > 0) {
			expirationContent += expirationContentByPtCode(PointType.SHIPPING.getTitle(), expirationCoupon)+separator;
		}
		
		//System.out.println(expirationContent);
		
		map.put("expirationContent", expirationContent);
		
		

		map.put("siteName", config.getShopName());
		map.put("siteUrl", SalesonProperty.getSalesonUrlShoppingmall());
		
		this.setMap(map);
	}
	
	private String expirationContentByPtCode(String ptCode, int point) {
		
		String expirationContent = "";
		
		String contentHeader = "<li style='line-height:22px; color:#333; list-style:none;'>";
		String contentFooter = "</li>";
		String contentSpanHeader = "<span style='color: #1b609a; font-weight: bold;'>";
		String contentSpanFooter = "</span>";
		
		String title = "";
		String unit = "";

		if (PointType.MILEAGE.getTitle().equals(ptCode)) {
			title = "마일리지";
			unit = "M";			
		} else if (PointType.EMONEY.getTitle().equals(ptCode)) {
			title = "E-머니";
			unit = "원";
		} else if (PointType.POINT.getTitle().equals(ptCode)) {
			title = "포인트";
			unit = "P";
			
		} else if (PointType.SHIPPING.getTitle().equals(ptCode)) {
			title = "배송쿠폰";
			unit = "장";
		} else {
			return "";
		}
		
		expirationContent += contentHeader;
		expirationContent += "- 사용하지 않은 "+title+": 총 ";
		expirationContent += contentSpanHeader;
		expirationContent += StringUtils.numberFormat(String.valueOf(point)) + unit;
		expirationContent += contentSpanFooter;
		expirationContent += contentFooter;
		
		return expirationContent;
	}
	
	private int getPointByPtCode (HashMap<String,Integer> pointMap, String ptCode) {
		
		int result = 0;
		
		try {
			result = pointMap.get(ptCode);
		} catch (NullPointerException ne) {
			result = 0;
		}
		return result;
	}
}
