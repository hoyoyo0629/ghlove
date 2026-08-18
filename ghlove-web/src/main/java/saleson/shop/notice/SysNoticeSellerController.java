package saleson.shop.notice;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.ThreadContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import saleson.common.Const;
import saleson.common.context.ShopContext;
import saleson.common.file.service.CustomFileService;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.community.doamin.CmntyBbsDto;
import saleson.shop.community.doamin.CmntyFaqBbsDto;
import saleson.shop.community.doamin.CmntyFaqBbsFileDto;
import saleson.shop.community.freeboard.CommunityManagerController;
import saleson.shop.notice.domain.Notice;
import saleson.shop.notice.domain.SysNoticeSellerDto;
import saleson.shop.notice.domain.SysNoticeSellerFileDto;
import saleson.shop.seo.domain.Seo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/opmanager/sellerNotice")
@RequestProperty(title="공지사항", layout="default", template="opmanager")
public class SysNoticeSellerController{

	@Autowired
	private SysNoticeSellerService sysNoticeSellerService;

	@Autowired
	private CustomFileService customFileService;

	@Autowired
	private CodeResolver codeResolver;

	private static final Logger log = LoggerFactory.getLogger(SysNoticeSellerController.class);

	@GetMapping("/list")
	public String list(@ModelAttribute("searchParam") SysNoticeSellerDto noticeParam , Model model) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}
		if(role.isEmpty()) {
		//	return ViewUtils.redirect("/opmanager", "게시글 열람 권한이 없습니다.");
		}

		//카운트
		int count = sysNoticeSellerService.getNoticeCount(noticeParam);
        //페이징
		 Pagination pagination = Pagination.getInstance(count, noticeParam.getItemsPerPage());
        noticeParam.setPagination(pagination);

        //날짜
        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

        model.addAttribute("list", sysNoticeSellerService.getNoticeList(noticeParam));
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);

	      //  model.addAttribute("role", adminRole);


		return "view:/sellerNotice/list";
	}

	@PostMapping("/list")
	public String searchList(@ModelAttribute("searchParam") SysNoticeSellerDto noticeParam , Model model) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {	role = userRole.getAuthority();
			}
		}
		if(role.isEmpty()) {
		//	return ViewUtils.redirect("/opmanager", "게시글 열람 권한이 없습니다.");
		}

		//카운트
		int count = sysNoticeSellerService.getNoticeCount(noticeParam);
        //페이징
		 Pagination pagination = Pagination.getInstance(count, noticeParam.getItemsPerPage());
        noticeParam.setPagination(pagination);

        //날짜
        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

        model.addAttribute("list", sysNoticeSellerService.getNoticeList(noticeParam));
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);

	      //  model.addAttribute("role", adminRole);


		return "view:/sellerNotice/list";
	}

	@GetMapping("/create")
	 public String createNotice(SysNoticeSellerDto sysNoticeSellerDto, RequestContext requestContext, Model model) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {	role = userRole.getAuthority();
			}
		}
		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager", "게시글 작성 권한이 없습니다.");
		}

		model.addAttribute("role",role);
	//	model.addAttribute("sysNoticeSellerDto",sysNoticeSellerDto);
		return "view:/sellerNotice/form";
	 }

	@PostMapping("/create")
	 public String createNoticeAction(SysNoticeSellerDto sysNoticeSellerDto, RequestContext requestContext, Model model, @RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {	role = userRole.getAuthority();
			}
		}
		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager", "게시글 작성 권한이 없습니다.");
		}

		sysNoticeSellerService.insertNotice(sysNoticeSellerDto, detailImageFiles);
		return ViewUtils.redirect("/opmanager/sellerNotice/list","등록되었습니다.");
	}

	@GetMapping("/edit/{noticeId}")
	public String view(@PathVariable("noticeId") int noticeId, Model model, SysNoticeSellerDto noticeParam) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {	role = userRole.getAuthority();
			}
		}
		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager", "게시글 작성 권한이 없습니다.");
		}

		 // 조회수 증가
		// sysNoticeSellerService.addHitCount(noticeId);

		SysNoticeSellerDto result = sysNoticeSellerService.getNotice(noticeId,model);
		if(result == null) {
			return ViewUtils.redirect("/opmanager/sellerNotice/list", "존재하지 않는 게시글입니다.");
		}


		model.addAttribute("sysNoticeSellerDto", result);
		model.addAttribute("role",role);
		return ViewUtils.getView("/sellerNotice/form");
	}

	@PostMapping("/edit/{noticeId}")
	public String updateNotice(SysNoticeSellerDto sysNoticeSellerDto, RequestContext requestContext, Model model, @RequestParam(value="detailImageFiles", required=false) MultipartFile[] detailImageFiles) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}
		if(role.isEmpty()) {
			return ViewUtils.redirect("/opmanager/sellerNotice/list", "답례품제공자 공지사항 수정 권한이 없습니다.");
		}

		sysNoticeSellerService.updateSysNoticeSeller(sysNoticeSellerDto, detailImageFiles);

		return ViewUtils.redirect("/opmanager/sellerNotice/list", "수정에 성공하였습니다.");
	}

	@GetMapping("/file-download/{fileId}")
	public ResponseEntity<?> fileDownloadNoitice(@PathVariable long fileId) throws IOException {
		SysNoticeSellerFileDto sysNoticeSellerFileDto = sysNoticeSellerService.getSysNoticeSellerFileDetail(fileId);
		File file = new File(CommonUtils.dataNvl(sysNoticeSellerFileDto.getFileSrc()));


		if (file.exists()) {
			return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(sysNoticeSellerFileDto.getOrgnlAtchFileNm()), file);
		}else{
			String script = "<script>alert('파일이 존재하지 않습니다. 시스템관리자에 문의하여 주십시오.'); history.back();</script>";
			return ResponseEntity.ok().contentType(new MediaType("text","html",StandardCharsets.UTF_8)).body(script);
		}
	}

	/**
	 * <pre>
	 * comment       : faq 게시글 파일 삭제
	 * preMethodName :
	 * author        : ucubepym
	 * date          : 2026. 5. 16.
	 *
	 * </pre>
	 * @param cmntyBbsDto
	 * @param requestContext
	 * @return
	 * JsonView
	 */
	@PostMapping("/delete-item-image")
	public JsonView deleteFaqFileByFileId(RequestContext requestContext, @RequestParam("fileId") long fileId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			sysNoticeSellerService.deleteNoticeFileByFileId(fileId);
		} catch (RuntimeException e) {
			log.error("ERROR: {}", e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("ERROR: {}", "에러가 발생했습니다.");
		}

	return JsonViewUtils.success("파일이 삭제 되었습니다.");
	}


	@PostMapping("delete/{noticeId}")
	public JsonView deleteFaq(RequestContext requestContext, @PathVariable int noticeId) {
		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
			}
		}
		// 시스템관리자만 등록수정삭제 가능
		if(role.isEmpty()) {
			return JsonViewUtils.exception("게시글 삭제 권한이 없습니다.");
		}

		if (!requestContext.isAjaxRequest()) {
	            throw new NotAjaxRequestException();
	    }
		try {
			sysNoticeSellerService.deleteNotice(noticeId);
		} catch (UserException e) {
			  return JsonViewUtils.exception("삭제에 실패했습니다.");
		}

		return JsonViewUtils.success("게시글이 삭제 되었습니다.");
	}

}
