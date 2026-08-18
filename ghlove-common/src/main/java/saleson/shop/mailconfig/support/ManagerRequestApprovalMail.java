package saleson.shop.mailconfig.support;

import java.util.HashMap;

import saleson.common.configuration.SalesonProperty;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.shop.config.domain.Config;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.user.domain.ManagerRequest;

public class ManagerRequestApprovalMail extends MailTemplate {
	public HashMap<String, String> getMap() {
		HashMap<String, String> map = new HashMap<>();
		map.put("loginId"	, "아이디");
		map.put("adminRole"	, "권한");
		map.put("siteName"	, "상점명");
		return map;
	}
	
	public ManagerRequestApprovalMail() {
		this.setMap(this.getMap());
	}
	
	public ManagerRequestApprovalMail(ManagerRequest managerRequest, MailConfig mailConfig, Cryptor cryptor, DataMasking dataMasking) {
		super(mailConfig, cryptor, dataMasking);
		
		HashMap<String, String> map = new HashMap<>();
		map.put("loginId"	, managerRequest.getLoginId());
		map.put("adminRole"	, this.getAdminRoleText(managerRequest));
		
		Config config = ShopUtils.getConfig();
		map.put("siteName", config.getShopName());
		map.put("siteUrl", SalesonProperty.getSalesonUrlShoppingmall());
		map.put("compName", config.getCompanyName());
		
		this.setMap(map);
	}
	
	private String getAdminRoleText(ManagerRequest managerRequest) {
		String adminRoleType = "";
		String adminRoleGrade = "";
		String adminRole = CommonUtils.dataNvl(managerRequest.getReqstSeCode());
		
		if(!"".equals(adminRole)) {
			switch(adminRole) {
				case "ROLE_ADMIN_1" : 
					adminRoleType = "시스템";
					adminRoleGrade = "주담당자";
					break;
				case "ROLE_ADMIN_2" : 
					adminRoleType = "시스템";
					adminRoleGrade = "부담당자";
					break;
				case "ROLE_ADMIN_3" : 
					adminRoleType = "행정안전부";
					adminRoleGrade = "주담당자";
					break;
				case "ROLE_ADMIN_4" : 
					adminRoleType = "행정안전부";
					adminRoleGrade = "부담당자";
					break;
				case "ROLE_ADMIN_5" : 
					adminRoleType = CommonUtils.dataNvl(managerRequest.getUpperlocgovNm());
					adminRoleType += " ";
					adminRoleType += CommonUtils.dataNvl(managerRequest.getLocgovNm());
					adminRoleGrade = "주담당자";
					break;
				case "ROLE_ADMIN_6" : 
					adminRoleType = CommonUtils.dataNvl(managerRequest.getUpperlocgovNm());
					adminRoleType += " ";
					adminRoleType += CommonUtils.dataNvl(managerRequest.getLocgovNm());
					adminRoleGrade = "부담당자";
					break;
				case "ROLE_ADMIN_7" : 
					adminRoleType = "오프라인";
					adminRoleGrade = "주담당자";
					break;
				case "ROLE_ADMIN_8" : 
					adminRoleType = "오프라인";
					adminRoleGrade = "부담당자";
					break;
				default : 
					break;
			};
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append(adminRoleType);
		sb.append(" ");
		sb.append(adminRoleGrade);
		return sb.toString();
	}
}
