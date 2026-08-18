package saleson.shop.email;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.opmanager.menu.MenuService;
import com.onlinepowers.framework.web.opmanager.role.RoleService;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import lombok.RequiredArgsConstructor;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.support.SellerParam;
import saleson.shop.databoard.DataboardService;
import saleson.shop.databoard.domain.Databoard;
import saleson.shop.databoard.domain.DataboardFile;
import saleson.shop.databoard.support.DataboardParam;
import saleson.shop.email.domain.Email;
import saleson.shop.email.domain.EmailFile;
import saleson.shop.email.domain.EmailSend;
import saleson.shop.email.domain.EmsTotalCnt;
import saleson.shop.email.support.EmailDetailParam;
import saleson.shop.email.support.EmailParam;
import saleson.shop.email.support.SendParam;
import saleson.shop.notice.domain.Notice;
import saleson.shop.notice.support.NoticeParam;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.usergroup.UserGroupService;
import saleson.shop.usergroup.support.UserGroupParam;

@Controller
@RequestMapping("/opmanager/email/**")
@RequestProperty(title="이메일 발송", layout="default", template="opmanager")
@RequiredArgsConstructor
public class EmailManagerController {
    private static final Logger log = LoggerFactory.getLogger(EmailManagerController.class);

    private final EmailService emailService;

	private final UserGroupService userGroupService;

    @GetMapping("list")
    public String list(RequestContext requestContext, @ModelAttribute("searchParam") EmailParam emailParam , Model model) {


    	int count = emailService.getEmailListCount(emailParam);

		Pagination pagination = Pagination.getInstance(count, emailParam.getItemsPerPage());
		emailParam.setPagination(pagination);


		model.addAttribute("list", emailService.getEmailList(emailParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);


        return "view:/mail/list";

    }

    @GetMapping("form")
    public String form(Model model) {

    	UserGroupParam userGroupParam = new UserGroupParam();
    	userGroupParam.setConditionType("ROLE_ADMIN");

    	model.addAttribute("groupList", userGroupService.getUserGroupList(userGroupParam));

    	return "view:/mail/form";
    }

    @PostMapping("form")
    public JsonView submit(Email email, @RequestParam(value="files", required=false) MultipartFile files) {

    	User user = UserUtils.getUser();
    	email.setFrstRegisterId(user.getUserId());

    	email.setFiles(files);

    	Email result = emailService.insertEmail(email);

    	return JsonViewUtils.success(result.getEmailId());
    }

    @PostMapping("send")
    public JsonView send(SendParam param) {

    	emailService.sendEmail(param);

    	return JsonViewUtils.success();
    }

    @PostMapping("search")
    public JsonView searchUser( String userName) {

    	List<EmailSend> list = emailService.getUserList(userName);

    	return JsonViewUtils.success(list);
    }

    @GetMapping("{emailId}")
    public String detail(@PathVariable long emailId, Model model) {

    	Email mail = emailService.getEmailDetail(emailId);
    	if (mail.isEmailSend()) {
    		EmsTotalCnt cnt = emailService.getEmsTotalCnt(emailId);
    		model.addAttribute("cnt", cnt);
    	}

    	model.addAttribute("mail", mail);

    	return "view:/mail/detail";
    }

    @PostMapping("{emailId}")
    public JsonView emsDetail(@PathVariable long emailId, EmailDetailParam params) {

    	params.setEmailId(emailId);

    	int count = emailService.getEmsUserCnt(params);

    	Pagination pagination = Pagination.getInstance(count, params.getItemsPerPage());
    	params.setPagination(pagination);

    	List<EmailSend> list = emailService.getEmsUserList(params);

    	Map<String, Object> resultMap = new HashMap<String, Object>();

    	resultMap.put("count", count);
    	resultMap.put("list", list);
    	resultMap.put("pagination", pagination);

    	return JsonViewUtils.success(resultMap);
    }

    @GetMapping("/download/{emailFileId}")
    public ResponseEntity<Resource> download(@PathVariable int emailFileId) {

    	EmailFile ef = emailService.getEmailFile(emailFileId);

    	if (ef == null) {
    		return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
    	}

    	String fullPath = new Email().getUploadPath() + File.separator + ef.getFileName();

		Resource resource = new FileSystemResource(fullPath);

		if (!resource.exists()) return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);

		HttpHeaders header = new HttpHeaders();
		Path filePath = null;

		try {
			String contentType = Files.probeContentType(Paths.get(fullPath));
			if (contentType == null || "".equals(contentType)) contentType = "application/octet-stream";

			return ResponseEntity.ok()
								 .contentType(MediaType.parseMediaType(contentType))
								 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ URLEncoder.encode(ef.getOrgFileName(),"UTF-8"))
								 .body(resource);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

    }

    @PostMapping("list")
    public String searchList(RequestContext requestContext, @ModelAttribute("searchParam") EmailParam emailParam , Model model) {
    	return list(requestContext,emailParam,model);
    }

}
