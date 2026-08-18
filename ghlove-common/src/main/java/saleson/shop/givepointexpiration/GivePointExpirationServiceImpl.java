package saleson.shop.givepointexpiration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.util.CipherUtils;

import saleson.common.configuration.SalesonProperty;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.shop.givepointexpiration.domain.GivePointExpirationMail;
import saleson.shop.givepointexpiration.domain.GivePointExpirationTarget;
import saleson.shop.mailconfig.MailConfigService;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.mailconfig.support.MemberExpirationPointMail;

@Service("givePointExpirationService")
public class GivePointExpirationServiceImpl extends EgovAbstractServiceImpl implements GivePointExpirationService{

	private static final Logger log = LoggerFactory.getLogger(GivePointExpirationServiceImpl.class);

	@Autowired
	private GivePointExpirationMapper givePointExpirationMapper;

	@Autowired
	private MailConfigService mailConfigService;

	@Autowired
	private Cryptor cryptor;

	@Autowired
	private DataMasking dataMasking;

	@Override
	public void givePointExpiration() {
		// TODO Auto-generated method stub
		List<GivePointExpirationTarget> list = givePointExpirationMapper.getCntrBlcePointList();
		if(!list.isEmpty()) {
			givePointExpirationMapper.updateCntrBlcePointDel(list);
			givePointExpirationMapper.insertCntrUsePoint(list);
		}
	}

	@Override
	public void givePointExpirationMailSend() {
		List<GivePointExpirationMail> list = givePointExpirationMapper.getGivePointExpirationMailInfoList();

		if(list != null && list.size() > 0) {
			String siteName = CommonUtils.dataNvl(givePointExpirationMapper.getShopName(1));

			for(GivePointExpirationMail details : list) {
				try {
					// 0. 유효성 검사
					if("".equals(CommonUtils.dataNvl(details.getUserName())) || "".equals(CommonUtils.dataNvl(details.getEmail()))) {
						continue;
					}

					// 1. 메일 템플릿 조회
					MailConfig mailConfig = mailConfigService.getMailConfigByTemplateId("expiration_point");
					if (mailConfig == null || !"Y".equals(mailConfig.getBuyerSendFlag())) continue;

					// 2. 메일 템플릿 대체코드 변환 데이터 셋팅 (휴면 회원일 경우 복호화)
					if("4".equals(CommonUtils.dataNvl(details.getStatusCode()))) {
						details.setUserName(CipherUtils.decrypt(CommonUtils.dataNvl(details.getUserName())));
						details.setEmail(CipherUtils.decrypt(CommonUtils.dataNvl(details.getEmail())));
					}

					// 3. 메일 템플릿 대체코드 변환 적용
					MemberExpirationPointMail mail = new MemberExpirationPointMail(details, mailConfig, siteName, cryptor, dataMasking);
					MailConfig mConfig = mail.getMailConfig();

					// 4. 메일 전송 데이터 셋팅
					String emsSendUserName = SalesonProperty.getEmsSendUsername();
					String emsSendEmail = SalesonProperty.getEmsSendEmail();
					String categoryNm = "포인트만료";
					String linkNm = "포인트만료";

					Map<String, Object> mailDataMap = new HashMap<>();
					mailDataMap.put("title", ShopUtils.unescapeHtml(CommonUtils.dataNvl(mConfig.getBuyerSubject())));
					mailDataMap.put("content", ShopUtils.unescapeHtml(CommonUtils.dataNvl(mConfig.getBuyerContent())));
					mailDataMap.put("sendInfo", emsSendEmail+ " "+ emsSendUserName);
					mailDataMap.put("rcvInfo", details.getEmail()+" "+details.getUserName());
					mailDataMap.put("sendDate", "");
					mailDataMap.put("sendType", "");
					mailDataMap.put("categoryNm", categoryNm);
					mailDataMap.put("linkNm", linkNm);
					mailDataMap.put("memo", "");

					Map<String, Object> mailMap = new HashMap<>();
					mailMap.put("data", mailDataMap);

					// 5. 메일 전송 데이터 문자로 변환
					ObjectMapper mapper = new ObjectMapper();
					String jsonValue = mapper.writeValueAsString(mailMap);

					// 6. 메일 발송 셋팅
					URL url = new URL(SalesonProperty.getEmsHost());

					final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

					try (AutoCloseable ac = () -> conn.disconnect()) {
						conn.setDoOutput(true);
						conn.setRequestMethod("POST");
						conn.setRequestProperty("Content-Type", "application/json");
						conn.setRequestProperty("Accept-Charset", "UTF-8");
						conn.setConnectTimeout(10000);
						conn.setReadTimeout(10000);

						try (final OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
							 PrintWriter writer = new PrintWriter(osw);
							 writer.write(jsonValue);
							 writer.flush();
						 }

						try (final InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8")){
			            	try (final BufferedReader br = new BufferedReader(isr)) {

			    				StringBuilder sb2 = new StringBuilder();
			    				String str;
			    				while ((str = br.readLine()) != null) {
			    					sb2.append(str + "\n");
			    				}
			    			}
			            }
					}

				} catch (RuntimeException e) {
					log.error("메일발송 송출 에러::: {}",e.getStackTrace()[0]);
				} catch (Exception e) {
					log.error("메일발송 송출 에러::: {}");
				}
			}
		}
	}
}
