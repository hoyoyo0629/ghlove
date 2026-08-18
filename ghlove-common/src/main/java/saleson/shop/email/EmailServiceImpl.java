package saleson.shop.email;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.privacy.pCrypto;

import saleson.common.configuration.SalesonProperty;
import saleson.shop.email.domain.Email;
import saleson.shop.email.domain.EmailDetail;
import saleson.shop.email.domain.EmailFile;
import saleson.shop.email.domain.EmailSend;
import saleson.shop.email.domain.EmsTotalCnt;
import saleson.shop.email.ems.EmsMapper;
import saleson.shop.email.support.EmailDetailParam;
import saleson.shop.email.support.EmailParam;
import saleson.shop.email.support.SendParam;
import saleson.shop.user.LocgovService;

@Service
public class EmailServiceImpl extends EgovAbstractServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	private final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "hwp", "doc", "docx", "pdf", "zip", "ppt", "pptx"};

	@Autowired
	private EmailMapper emailMapper;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private EmsMapper emsMapper;

	@Override
	public int getEmailListCount(EmailParam searchParams) {
		return emailMapper.getEmailListCount(searchParams);
	}

	@Override
	public List<Email> getEmailList(EmailParam searchParams) {

		List<Email> list = emailMapper.getEmailList(searchParams);

		this.updateEmailStatus(list);

		return list;
	}

	@Override
	public Email insertEmail(Email email) {



		emailMapper.insertEmail(email);


		if (email.getFiles() != null && !email.getFiles().isEmpty()) {
			String newFileName = locgovService.saveFile(email.getFiles(), email.getUploadPath(), AVAILABLE_EXTENSION, 10, false);
			String extension = FileUtils.getExtension(email.getFiles().getOriginalFilename());

			EmailFile file = EmailFile.builder().emailId(email.getEmailId())
												.fileName(newFileName)
												.orgFileName(email.getFiles().getOriginalFilename())
												.fileTy(extension)
												.build();

			emailMapper.insertEmailFile(file);

		}

		if ("A".equals(email.getAuthTarget())) {

			for (EmailDetail detail : email.getAuthList()) {
				detail.setEmailId(email.getEmailId());
				emailMapper.insertEmailDetail(detail);
			}

		}

		return email;
	}

	@Override
	public boolean sendEmail(SendParam param) {
		boolean result = true;
		long emailId = param.getEmailId();

		Email email = this.getEmailDetail(emailId);
		try {
			int idx = 0;
			int size = 30000;
			List<EmailSend> userList = null;


			if ("A".equals(email.getAuthTarget())) {
				userList = emailMapper.sendUserList(email);
			} else if ("E".equals(email.getAuthTarget())) {
				userList = param.getSendUserList();
			} else if ("S".equals(email.getAuthTarget())) {
				userList = emailMapper.sendSellerUserList(email);
			}else if ("L".equals(email.getAuthTarget())) {		// srhan. Login email send
				userList = param.getSendUserList();
			}

			if (userList != null && !userList.isEmpty()) {

				List<List<EmailSend>> partitions = new ArrayList<>();

		        for (int i = 0; i < userList.size(); i += size) {
		            partitions.add(userList.subList(i, Math.min(i + size,
		            		userList.size())));
		        }


		        for (List<EmailSend> pList : partitions) {
		        	StringBuffer sb = new StringBuffer();

		        	for (int i = 0; i < pList.size(); i++) {
		        		EmailSend p = pList.get(i);
		        		sb.append( (i == 0 ? "" : ",") + p.getEmail() + " " + p.getUserName());
		        	}

		        	this.sendEmailData(email, sb);

		        }

			}

			email.setStatus("S");

		} catch (RuntimeException e) {
			logger.error("sendMail Error !!! {}",e.getStackTrace()[0]);
		} catch (Exception e) {
			email.setStatus("F");
			result = false;
			logger.error("sendMail Error !!! {}",e.getStackTrace()[0]);
		} finally {
			emailMapper.updateEmailStatus(email);
		}



		return result;
	}


	@SuppressWarnings("unchecked")
	private String sendEmailData(Email email, StringBuffer sb) throws Exception {
		String msg = "";
		String emsSendUserName = SalesonProperty.getEmsSendUsername();
		String emsSendEmail = SalesonProperty.getEmsSendEmail();
		String categoryNm = "포인트만료";
		String linkNm = "포인트만료";
		// srhan. Login email send
		if ("L".equals(email.getAuthTarget())) {		// srhan. Login email send
			categoryNm = "로그인인증이메일";
			linkNm = "로그인인증이메일";
		}
		String response = "";
		Map<String, Object> mailDataMap = new HashMap<>();
		mailDataMap.put("title", email.getSubject());
		mailDataMap.put("content", email.getContent());
		mailDataMap.put("sendInfo", emsSendEmail+ " "+ emsSendUserName);
		mailDataMap.put("rcvInfo", sb.toString());
		mailDataMap.put("sendDate", ("D".equals(email.getSendType()) ? "" : email.getSendDate()));
		mailDataMap.put("sendType", email.getSendType());
		mailDataMap.put("categoryNm", categoryNm);
		mailDataMap.put("linkNm", linkNm);
		mailDataMap.put("memo", email.getEmailId());
		Map<String, Object> mailMap = new HashMap<>();
		mailMap.put("data", mailDataMap);

		ObjectMapper mapper = new ObjectMapper();
		String jsonValue = mapper.writeValueAsString(mailMap);

		URL url = new URL(SalesonProperty.getEmsHost());

		final HttpURLConnection http = (HttpURLConnection) url.openConnection();
		try (AutoCloseable ac = () -> http.disconnect()) {
			http.setDoOutput(true);
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/json");
			http.setRequestProperty("Accept-Charset", "UTF-8");
			http.setConnectTimeout(10000);
			http.setReadTimeout(10000);

			 try (final OutputStreamWriter osw = new OutputStreamWriter(http.getOutputStream(), "UTF-8")) {
				 PrintWriter writer = new PrintWriter(osw);
				 writer.write(jsonValue);
				 writer.flush();
			 }

			try (final InputStreamReader isr = new InputStreamReader(http.getInputStream(), "UTF-8")){
            	try (final BufferedReader br = new BufferedReader(isr)) {

    				StringBuilder sb2 = new StringBuilder();
    				String str;
    				while ((str = br.readLine()) != null) {
    					sb2.append(str + "\n");
    				}
    				response = sb2.toString();
    			}
            }
			ObjectMapper om = new ObjectMapper();
			Map<String, Map<String, String>> result = om.readValue(response, Map.class);

			msg = result.get("result").get("result");

		}
		return msg;


	}


	@Override
	public List<EmailSend> getUserList(String userName) {

		try {
			String encUser = pCrypto.Encrypt("normal", userName, "");

			return emailMapper.getUserList(encUser);

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			logger.error("■■■ERROR■■■ getUserList Exception {}",e.getStackTrace()[0]);
			return new ArrayList<>();
		}

	}

	@Override
	public Email getEmailDetail(long emailId) {
		Email email = emailMapper.getEmailDetail(emailId);
		this.updateEmailStatus(Arrays.asList(email));
		return email;
	}

	@Override
	public EmsTotalCnt getEmsTotalCnt(long emailId) {
		return emsMapper.getEmsTotalCnt(emailId);
		//return null;
	}

	@Override
	public List<EmailSend> getEmsUserList(EmailDetailParam params) {
		return emsMapper.getEmsUserList(params);
		//return new ArrayList<>();
	}

	@Override
	public int getEmsUserCnt(EmailDetailParam params) {
		return emsMapper.getEmsUserCnt(params);
		//return 0;
	}

	private void updateEmailStatus(List<Email> list) {

		List<Email> searchList = list.stream().filter(e -> "S".equals(e.getStatus())).collect(Collectors.toList());

		if (searchList != null && searchList.size() > 0) {
			List<EmsTotalCnt> mList = emsMapper.getEmsReport(searchList);

			if (mList != null && mList.size() > 0) {

				List<Code> codeList = CodeUtils.getCodeList("EMAIL_STATUS");

				for (Email e : list) {

					Optional<EmsTotalCnt> totOpt = mList.stream().filter(m -> m.equals(e.getEmailId())).findFirst();

					if (totOpt.isPresent()) {

						EmsTotalCnt tot = totOpt.get();
						Code code = null;

						if (tot.getTotCnt() > tot.getSuccCnt() + tot.getFailCnt()) continue;

						if (tot.getTotCnt() == tot.getSuccCnt()) {
							// 전체 성공
							code = codeList.stream().filter(c -> "C".equals(c.getId())).findFirst().get();
						} else if (tot.getTotCnt() == tot.getFailCnt()) {
							// 전체 실패
							code = codeList.stream().filter(c -> "P".equals(c.getId())).findFirst().get();
						} else {
							// 일부성공
							code = codeList.stream().filter(c -> "T".equals(c.getId())).findFirst().get();
						}

						if(code != null) {
							e.setStatus(code.getId());
							e.setStatusName(code.getLabel());
						}

						emailMapper.updateEmailStatus(e);
					}

				}
			}

		}

	}

	@Override
	public EmailFile getEmailFile(int emailFileId) {
		return emailMapper.getEmailFile(emailFileId);
	}

}
