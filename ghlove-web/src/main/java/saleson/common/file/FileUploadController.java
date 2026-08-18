package saleson.common.file;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.api.common.enumerated.UserAdminRole;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.give.giveoperation.GiveOperationService;
import saleson.shop.give.giveoperation.domain.CtbnyOpratn;
import saleson.shop.give.giveoperation.domain.CtbnyOpratnFile;
import saleson.shop.give.giveoperation.domain.LocManagerCheck;
import saleson.shop.give.statistics.domain.GiveOperateSearch;
import saleson.shop.give.statistics.domain.GiveStatistics;
import saleson.shop.give.statistics.domain.GiveStatisticsDetailSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsPersonal;
import saleson.shop.give.statistics.domain.GiveStatisticsSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsTotalSearch;
import saleson.shop.give.statistics.support.GiveStatisticsDetailExcelView;
import saleson.shop.give.statistics.support.GiveStatisticsExcelView;
import saleson.shop.give.statistics.support.GiveStatisticsExcelViewByDate;
import saleson.shop.give.statistics.support.GiveStatisticsOperateExcelView;

@Controller
@RequestMapping("/opmanager/file/**")
@RequestProperty(title="서버 업로드용", layout="default", template="opmanager")
public class FileUploadController {

	private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

	private static final int readByteSize = 256;
    private static final String charset= "UTF-8";
    private static final String CRLF= "\r\n";



	@GetMapping("index")
	@RequestProperty(layout = "base")
	public String index(Model model, HttpServletResponse response) {

		model.addAttribute("serverIp", getServerIp());

		if(isAdmin()) return "view:/file/index";
		else return ViewUtils.redirect("/error/404");
	}

	@PostMapping("makeDir")
	public JsonView makeDir(@RequestBody Map<String, String> paramMap)  {

		if(!isAdmin()) {
			return JsonViewUtils.failure("");
		}

		String dirName = paramMap.get("dirName");
		String pathName = paramMap.get("path");

		try {
			String msg = "";
			Path newFolderPath = Paths.get(pathName + "/" + dirName);
			if(Files.exists(newFolderPath) && Files.isDirectory(newFolderPath)){
				msg = "폴더가 존재합니다.";
			}else{
				Files.createDirectory(newFolderPath);
				msg = "폴더가 생성되었습니다.";
			}
			return JsonViewUtils.success(msg);
		} catch (RuntimeException e) {
			return JsonViewUtils.failure(e.getMessage());
		} catch (Exception e) {
			return JsonViewUtils.failure(e.getMessage());
		}


	}

	@PostMapping("uploadFile")
	public JsonView uploadFile(@RequestParam(value="path") String filePath, @RequestParam(value = "files", required = false) MultipartFile[] files) {

		if(!isAdmin()) {
			return JsonViewUtils.failure("");
		}

		Path path = Paths.get(filePath).toAbsolutePath().normalize();
		
		for (MultipartFile file : files) {
			try (InputStream is = file.getInputStream();) {
				
				Files.createDirectories(path);
				if (file.isEmpty()) continue;

				Path target = Paths.get(filePath + File.separator + file.getOriginalFilename());
				Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
	
			} catch (RuntimeException e) {
				log.error(getClass().getName() + " uploadFile RuntimeException ================== fileName :: " + file.getName(), e);
				return JsonViewUtils.failure("업로드 실패 - RuntimeException :: " + file.getName());
			} catch (Exception e) {
				log.error(getClass().getName() + " uploadFile Exception ================== fileName :: " + file.getName(), e);
				return JsonViewUtils.failure("업로드 실패 - Exception :: " + file.getName());
			}
		}
		
		return JsonViewUtils.success("업로드 성공");
	}

	@PostMapping("downloadFile")
	public ResponseEntity<Resource> downloadFile(@RequestParam Map<String, String> data) {


		String fullPath = data.get("path") + File.separator + data.get("fileName");

		Resource resource = new FileSystemResource(fullPath);

		if (!resource.exists()) return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);

//		HttpHeaders header = new HttpHeaders();
//		Path filePath = null;

		try {
			String contentType = Files.probeContentType(Paths.get(fullPath));
			if (contentType == null || "".equals(contentType)) contentType = "application/octet-stream";

			return ResponseEntity.ok()
								 .contentType(MediaType.parseMediaType(contentType))
								 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ URLEncoder.encode(data.get("fileName"),"UTF-8"))
								 .body(resource);

		} catch (IOException e) {
			return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	private boolean isAdmin() {

		if (UserUtils.getUser() == null) return false;

		UserAdminRole role = UserAdminRole.findByUserRole();

		return UserAdminRole.SYS.equals(role);
	}


	private String getServerIp() {

		InetAddress local = null;
		try {
			local = InetAddress.getLocalHost();
		}
		catch ( UnknownHostException e ) {
			log.error("■■■ERROR■■■ getServerIp Exception {}",e.getStackTrace()[0]);
		}

		if( local == null ) {
			return "";
		}
		else {
			String ip = local.getHostAddress();
			return ip;
		}

	}

	@PostMapping("/sendMail")
	public JsonView sendMailTest(@RequestParam(value="email") String email) throws Exception {

		// 4. 메일 전송 데이터 셋팅
		String emsSendUserName = SalesonProperty.getEmsSendUsername();
		String emsSendEmail = SalesonProperty.getEmsSendEmail();
		String categoryNm = "포인트만료";
		String linkNm = "포인트만료";
		String uuid = UUID.randomUUID().toString();
		Map<String, Object> mailDataMap = new HashMap<>();
		mailDataMap.put("title", "메일 테스트");
		mailDataMap.put("content", "메일 내용");
		mailDataMap.put("sendInfo", emsSendEmail+ " "+ emsSendUserName);
		mailDataMap.put("rcvInfo", email + " 테스터");
		mailDataMap.put("sendDate", "");
		mailDataMap.put("sendType", "");
		mailDataMap.put("categoryNm", categoryNm);
		mailDataMap.put("linkNm", linkNm);
		mailDataMap.put("memo", uuid);

		Map<String, Object> mailMap = new HashMap<>();
		mailMap.put("data", mailDataMap);

		// 5. 메일 전송 데이터 문자로 변환
		ObjectMapper mapper = new ObjectMapper();
		String jsonValue = mapper.writeValueAsString(mailMap);

		log.info("**********************************************");
		log.info("ems host: " + SalesonProperty.getEmsHost());
		log.info("**********************************************");

		// 6. 메일 발송 셋팅
		URL url = new URL(SalesonProperty.getEmsHost());

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		
		try (
				OutputStream os = conn.getOutputStream();
				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, "UTF-8");
				BufferedReader in = new BufferedReader(isr);
				) {

			os.write(jsonValue.getBytes("UTF-8"));
			os.flush();

			// 7.리턴된 결과 읽기
			String inputLine = null;
			StringBuffer outResult = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				outResult.append(inputLine);
			}

			log.info("*****************************************");
			log.info(outResult.toString());
			log.info("*****************************************");

			ObjectMapper om = new ObjectMapper();
			Map<String, String> result = om.readValue(outResult.toString(), Map.class);

			return JsonViewUtils.success(result);
		} catch (RuntimeException e) {
			log.error("■■■ERROR■■■ sendMailTest Exception {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure(e.getMessage());
		} catch (Exception e) {
			log.error("■■■ERROR■■■ sendMailTest Exception {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure(e.getMessage());
		} finally {
			if(conn != null) conn.disconnect();
		}


	}

	@PostMapping("/sendMailFile")
	public JsonView sendMailFileTest(@RequestParam(value="email") String email) throws Exception {

		// 4. 메일 전송 데이터 셋팅
		String emsSendUserName = SalesonProperty.getEmsSendUsername();
		String emsSendEmail = SalesonProperty.getEmsSendEmail();
		String categoryNm = "포인트만료";
		String linkNm = "포인트만료";

		Map<String, Object> mailDataMap = new HashMap<>();
		mailDataMap.put("title", "메일 테스트");
		mailDataMap.put("content", "메일 내용");
		mailDataMap.put("sendInfo", emsSendEmail+ " "+ emsSendUserName);
		mailDataMap.put("rcvInfo", email + " 테스터");
		mailDataMap.put("sendDate", "");
		mailDataMap.put("sendType", "");
		mailDataMap.put("categoryNm", categoryNm);
		mailDataMap.put("linkNm", linkNm);
		mailDataMap.put("memo", "");

		Map<String, Object> mailMap = new HashMap<>();
		mailMap.put("data", mailDataMap);

		// 5. 메일 전송 데이터 문자로 변환
//		ObjectMapper mapper = new ObjectMapper();
//		String jsonValue = mapper.writeValueAsString(mailMap);

		log.info("**********************************************");
		log.info("ems host: " + SalesonProperty.getEmsHost());
		log.info("**********************************************");

		// 6. 메일 발송 셋팅
		URL url = new URL("http://192.168.100.250:8380/api/sendFile.do");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		//conn.setRequestProperty("Content-Type", "application/json");
		//conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		


		String boundary = this.setBoundary();

		try (
				OutputStream os = conn.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
				PrintWriter writer = new PrintWriter(osw, true);
				InputStream is = conn.getInputStream();
				InputStreamReader isr = new InputStreamReader(is, charset);
				BufferedReader in = new BufferedReader(isr);
				) {
			
			File file = new File("C:/ghlove/workspace/ghlove/ghlove-web/storage/upload/email/202307061715380194_59RLZ4QV.zip");

			//전송
            this.addString(writer, boundary, "title", "메일 테스트");
            this.addString(writer, boundary, "content", "메일 내용");
            this.addString(writer, boundary, "sendInfo", emsSendEmail+ " "+ emsSendUserName);
            this.addString(writer, boundary, "rcvInfo", email + " 테스터");
            this.addString(writer, boundary, "sendDate", "");
            this.addString(writer, boundary, "sendType", "");
            this.addString(writer, boundary, "categoryNm", categoryNm);
            this.addString(writer, boundary, "linkNm", linkNm);
            this.addString(writer, boundary, "memo", "");
            this.addFile(writer, os, boundary, "file", file);
            this.addEnd(writer, boundary);


			// 7.리턴된 결과 읽기
//            int httpStatus = conn.getResponseCode();
            
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            log.info("Response: "+ sb.toString());


			return JsonViewUtils.success("succ");
		} catch (RuntimeException e) {
			log.error("■■■ERROR■■■ sendMailFileTest Exception {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure(e.getMessage());
		} catch (Exception e) {
			log.error("■■■ERROR■■■ sendMailFileTest Exception {}",e.getStackTrace()[0]);
			return JsonViewUtils.failure(e.getMessage());
		} finally {
			if(conn != null) conn.disconnect();
		}


	}

	//바운더리 셋팅
    private String setBoundary(){
        String boundaryTime = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String boundary = "aaboo"+boundaryTime;
        return boundary;
    }

  //스트링 추가
    private void addString(PrintWriter writer, String boundary, String _key, String _value){// Send normal String
        StringBuilder sb = new StringBuilder();
        sb.append("--"+ boundary).append(CRLF);
        sb.append("Content-Disposition: form-data; name=\""+ _key +"\"").append(CRLF);
        sb.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
        sb.append(CRLF).append(_value).append(CRLF);

        writer.append(sb).flush();
    }
    //파일 추가
    private void addFile(PrintWriter writer, OutputStream os, String boundary, String _key, File _file) throws IOException{// Send File
        StringBuilder sb = new StringBuilder();
        sb.append("--"+ boundary).append(CRLF);
        sb.append("Content-Disposition: form-data; name=\""+ _key +"\"; filename=\"" + _file.getName() + "\"").append(CRLF);
        sb.append("Content-Type: "+ URLConnection.guessContentTypeFromName(_file.getName())).append(CRLF); // Text file itself must be saved in this charset!
        sb.append("Content-Transfer-Encoding: binary").append(CRLF);
        sb.append(CRLF);
        writer.append(sb).flush();

        try(FileInputStream inputStream = new FileInputStream(_file);){

        	byte[] buffer = new byte[(int)_file.length()];
        	int bytesRead = -1;
        	while((bytesRead = inputStream.read(buffer)) != -1){
        		os.write(buffer, 0, bytesRead);
        	}
        	os.flush();
        	inputStream.close();
        }

        writer.append(CRLF).flush();
    }
    //전송처리 끝
    private void addEnd(PrintWriter writer, String boundary){//End of multipart/form-data.
        StringBuilder sb = new StringBuilder();
        sb.append("--").append(boundary).append("--").append(CRLF);
        writer.append(sb).flush();
    }

}
