package saleson.shop.mailconfig.support;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.onlinepowers.framework.util.MessageUtils;
import org.springframework.util.ObjectUtils;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.mailconfig.domain.MailConfig;

import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;

public class MailTemplate {

	private final String defaultPath = FileUtils.getDefaultUploadPath() + File.separator + "mail-template";
	private HashMap<String, String> map;
	private MailConfig mailConfig;
	private HashMap<String, String> codes;

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public MailTemplate() {	}
	public MailTemplate(Cryptor cryptor) {
		this.cryptor = cryptor;
	}

	public MailTemplate(Cryptor cryptor, DataMasking dataMasking) {
		this(cryptor);
		this.dataMasking = dataMasking;
	}

	public MailTemplate(MailConfig mailConfig, Cryptor cryptor) {
		this(cryptor);
		this.mailConfig = mailConfig;
	}

	public MailTemplate(MailConfig mailConfig, Cryptor cryptor, DataMasking dataMasking) {
		this(mailConfig, cryptor);
		this.dataMasking = dataMasking;
	}
	
	/**
	 * code <templateId, messageCode>
	 * @return
	 */
	public HashMap<String, String> getTemplateCodes() {
		if (codes == null) {
			codes = new LinkedHashMap<>();
			
			codes.put("order_deposit_wait", "입금대기");
			codes.put("order_cready_payment", "결제완료");
			codes.put("order_delivering", "배송중");
			
			codes.put("expiration_point", MessageUtils.getMessage("M00246") + "소멸예정");
			codes.put("member_join", "회원가입");  // 회원가입
			codes.put("pwsearch", "임시비밀번호 안내");  // 임시비밀번호 안내
			codes.put("qna_complete", "문의답변");  // 문의답변
			
			codes.put("member_sleep", "휴면안내");  // 휴면안내
			codes.put("manager_request_approval", "관리자 권한 승인");
			codes.put("manager_request_reject", "관리자 권한 거절");
		}
		
		return codes;
	}
	
	/**
	 * code <templateId, messageCode>
	 * @return
	 */
	public HashMap<String, String> getOrderMailCodes() {
		if (codes == null) {
			codes = new LinkedHashMap<>();
			//codes.put("order_stats_7", "M00381");  // 주문승락
			//codes.put("order_stats_3", "M00382");  // 배송완료
		}
		
		return codes;
	}
	
	public String getTemplateCodeTitle(String key) {
		HashMap<String, String> code = this.getTemplateCodes();
		if (ValidationUtils.isNotNull(code.get(key))) {
			//return MessageUtils.getMessage(code.get(key));
			return code.get(key);
		}
		
		return "";
	}
	
	public String getFirstTemplateCodeKey() {
		HashMap<String, String> code = this.getTemplateCodes();
		
		String r = "";
		if (code != null) {
			int i = 0;

			for (String s : code.keySet()) {
				if (i == 0) {
					r = s;
				}
				i++;
			}
		}
		return r;
	}
	
	public HashMap<String, String> getCodes() {
		
		LinkedHashMap<String, String> code = new LinkedHashMap<>();
		if (map != null) {
			for(String key : this.map.keySet()) {
				code.put(this.getTemplatePattern(key), this.map.get(key));
			}
		}
		
		return code;
	}
	
	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}
	
	/**
	 * 템플릿에 사용된 패턴을 리턴함
	 * @param codeName
	 * @return
	 */
	private String getTemplatePattern(String codeName) {
		String str = "";
		if (StringUtils.isNotEmpty(codeName)) {
			str = "{" + StringUtils.convertToUnderScore(codeName) + "}";
		}
		
		return str;
	}
	
	/**
	 * 최종 전송할 데이터를 만듬
	 * @return
	 */
	public MailConfig getMailConfig() {
		
		if (mailConfig == null || map == null) {
			return null;
		}
		
		String buyerContent = mailConfig.getBuyerContent();
		String buyerSubject = mailConfig.getBuyerSubject();

		String adminContent = mailConfig.getAdminContent();
		String adminSubject = mailConfig.getAdminSubject();


		String buyerContentMasking = mailConfig.getBuyerContent();
		String adminContentMasking = mailConfig.getAdminContent();

		String buyerSubjectMasking = mailConfig.getBuyerSubject();
		String adminSubjectMasking = mailConfig.getAdminSubject();


		if ("Y".equals(mailConfig.getBuyerSendFlag()) || "Y".equals(mailConfig.getAdminSendFlag())) {
			for(String key : map.keySet()) {
				
				String fieldValue = (String) map.get(key);
				fieldValue = fieldValue == null ? "" : fieldValue;
				fieldValue = fieldValue.equals("null") ? "" : fieldValue;
				
				String pattern = this.getTemplatePattern(key);
				
				if ("Y".equals(mailConfig.getBuyerSendFlag())) {
					buyerContent = buyerContent.replace(pattern, fieldValue);
					buyerSubject = buyerSubject.replace(pattern, fieldValue);

					buyerSubjectMasking = replaceContent(buyerSubjectMasking, pattern, fieldValue);
					buyerContentMasking = replaceContent(buyerContentMasking, pattern, fieldValue);
				}
				
				
				if ("Y".equals(mailConfig.getAdminSendFlag())) {
					adminContent = adminContent.replace(pattern, fieldValue);
					adminSubject = adminSubject.replace(pattern, fieldValue);

					adminSubjectMasking = replaceContent(adminSubjectMasking, pattern, fieldValue);
					adminContentMasking = replaceContent(adminContentMasking, pattern, fieldValue);
				}
			}
			
			
			if ("Y".equals(mailConfig.getBuyerSendFlag())) {
				String htmlBody = this.getBodyUserFile("mailTemplate01");
				if (htmlBody != null) {
					buyerContent = htmlBody.replace("{CONTENT}", buyerContent);;
					mailConfig.setBuyerTagUse("Y");
				}
			}

			if ("Y".equals(mailConfig.getBuyerSendFlag())) {
				String htmlBody = this.getBodyUserFile("mailTemplate01");
				if (htmlBody != null) {
					buyerContent = htmlBody.replace("{CONTENT}", buyerContent);;
					mailConfig.setBuyerTagUse("Y");
				}
			}
		}
		
		mailConfig.setBuyerSubject(buyerSubject);
		mailConfig.setBuyerContent(buyerContent);
		mailConfig.setAdminSubject(adminSubject);
		mailConfig.setAdminContent(adminContent);

		mailConfig.setBuyerContentMasking(buyerContentMasking);
		mailConfig.setAdminContentMasking(adminContentMasking);

		mailConfig.setBuyerSubjectMasking(buyerSubjectMasking);
		mailConfig.setAdminSubjectMasking(adminSubjectMasking);
		
		return mailConfig;
	}

	private String replaceContent(String buyerContentMasking, String pattern, String value) {
		String defaultContent = buyerContentMasking.replace(pattern, value);
		if (dataMasking == null) return defaultContent;

		List<String> privacyKeys = Arrays.asList("name", "userName", "orderName", "bankInName", "email", "phone", "mobile");
		long matchedCount = privacyKeys.stream()
				.filter(s -> pattern.contains(s) && !pattern.contains("site_name"))
				.count();

		if (matchedCount > 0) {
			return buyerContentMasking.replace(pattern, dataMasking.mask(value, detectMaskinType(pattern)));
		}

		return defaultContent;
	}

	private Masking detectMaskinType(String pattern) {
		if (pattern.contains("name") || pattern.contains("userName")
				|| pattern.contains("orderName") || pattern.contains("bankInName")) {
			return Masking.NAME;
		}

		if (pattern.contains("email")) {
			return Masking.EMAIL;
		}

		if (pattern.contains("phone") || pattern.contains("mobile")) {
			return Masking.PHONE_NUMBER;
		}

		return Masking.NAME;
	}

	/**
	 * 전송 내용 가져오기
	 * @param fileName
	 * @return
	 * @throws IOException 
	 */
	public String getBodyUserFile(String fileName) {
		return null;
		/*try {
			//return FileUtils.readFileToString(defaultPath + File.separator + fileName + ".html");
			return null;
		} catch (Exception e) {
			return null;
		}*/
	}

	public Cryptor getCryptor() {
		return cryptor;
	}

	public DataMasking getDataMasking() {
		return dataMasking;
	}

	public String getDecryptAndDataMasking(String content, Masking maskingtype) {
		if (!ObjectUtils.isEmpty(content)) {
			return getDataMasking().mask(getCryptor().decrypt(content), maskingtype);
		}

		return content;
	}
	
}
