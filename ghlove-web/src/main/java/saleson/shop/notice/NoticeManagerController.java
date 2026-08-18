package saleson.shop.notice;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.common.Const;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.support.SellerParam;
import saleson.shop.notice.domain.Notice;
import saleson.shop.notice.support.NoticeParam;
import saleson.shop.user.domain.UserDetail;

@Controller
@RequestMapping("/opmanager/notice/**")
@RequestProperty(title="공지사항", layout="default", template="opmanager")
public class NoticeManagerController {
	private static final Logger log = LoggerFactory.getLogger(NoticeManagerController.class);

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private CodeResolver codeResolver;

	@Autowired
	private SellerService sellerService;


	@GetMapping("list")
	public String list( @ModelAttribute("searchParam") NoticeParam searchParam , Model model) {
		searchParam.setLocgovCode("00000");
		int count = 0;

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);
		//List<Code> subCategoryCode = CodeUtils.getCodeList("NOTICE_SUB_CATEGORY");
		//List<Code> categoryTeamCode = CodeUtils.getCodeList("NOTICE_CATEGORY_TEAM");

        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		//model.addAttribute("subCategoryCode", subCategoryCode);
		//model.addAttribute("categoryTeamCode", categoryTeamCode);

		return "view:/notice/list";
	}

	@GetMapping("create")
	public String create(RequestContext requestContext, Model model, Notice notice){

		//List<Code> subCategoryCode = CodeUtils.getCodeList("NOTICE_SUB_CATEGORY");
		//List<Code> categoryTeamCode = CodeUtils.getCodeList("NOTICE_CATEGORY_TEAM");

		SellerParam sellerParam = new SellerParam();
		sellerParam.setStatusCode("2");
		//model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		//model.addAttribute("subCategoryCode", subCategoryCode);
		//model.addAttribute("categoryTeamCode", categoryTeamCode);

		return ViewUtils.view();
	}

	/**
	 * 공지사항 등록
	 *
	 * @param requestContext
	 * @param notice
	 * @return
	 * @author joo
	 */
	@PostMapping("create")
	public String createAction(RequestContext requestContext, Notice notice){
		noticeService.insertNotice(notice);
		return ViewUtils.redirect("/opmanager/notice/list","등록되었습니다.");
	}

	@GetMapping("edit/{noticeId}")
	public String edit(RequestContext requestContext, Model model, @PathVariable("noticeId") int noticeId, @ModelAttribute("searchParam") NoticeParam searchParam ){

		//List<Code> subCategoryCode = CodeUtils.getCodeList("NOTICE_SUB_CATEGORY");
		//List<Code> categoryTeamCode = CodeUtils.getCodeList("NOTICE_CATEGORY_TEAM");

		SellerParam sellerParam = new SellerParam();
		sellerParam.setStatusCode("2");
		//model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("notice", noticeService.getNotice(noticeId));
		model.addAttribute("noticeSellerList", noticeService.getNoticeSellerList(noticeId));
		model.addAttribute("searchParam", searchParam);
		//model.addAttribute("subCategoryCode", subCategoryCode);
		//model.addAttribute("categoryTeamCode", categoryTeamCode);

		return ViewUtils.view();
	}

	@PostMapping("edit/{noticeId}")
	public String editAction(RequestContext requestContext, Notice notice){

		notice.setUserName(SecurityUtils.getCurrentUser().getUserName());

		if (null == notice.getNoticeFlag()) {
			notice.setNoticeFlag("N");
		}
		noticeService.updateNotice(notice);

		return ViewUtils.redirect("/opmanager/notice/list",MessageUtils.getMessage("M00289"));
	}

	@PostMapping("delete/{noticeId}")
	public JsonView delete(RequestContext requestContext, @PathVariable int noticeId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}


		try {
			noticeService.deleteNotice(noticeId);
		} catch (RuntimeException e) {
//			return JsonViewUtils.exception(e.getMessage());
			//return JsonViewUtils.exception(MessageUtils.getMessage("실패했습니다."));			// 실패했습니다.
			return JsonViewUtils.exception("실패했습니다.");			// 실패했습니다.
		}


		return JsonViewUtils.success();
	}

    /**
     * 공지사항 일괄 삭제
     * @param requestContext
     * @param noticeParam
     * @return
     */
    @PostMapping("/deleteNotice")
    public JsonView noticeListDelete(RequestContext requestContext, NoticeParam noticeParam) {
        String code = "";

        try {
            code = noticeService.deleteListNotice(noticeParam);
        } catch (RuntimeException e) {
//            log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR: {}", getClass().getName() + " :: noticeListDelete RuntimeException ==============");
        }

        return JsonViewUtils.success(code);
    }




	@PostMapping("delete-notice-seller/{noticeSellerId}")
	public JsonView deleteNoticeSeller(@PathVariable("noticeSellerId") int noticeSellerId, RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		try{
			noticeService.deleteNoticeSeller(noticeSellerId);
			return JsonViewUtils.success();
		}catch(RuntimeException e){
			return JsonViewUtils.failure("삭제중 오류가 발생했습니다.");
		}


	}

	@PostMapping("list")
	public String searchList( @ModelAttribute("searchParam") NoticeParam searchParam , Model model) {
		searchParam.setLocgovCode("00000");
		int count = noticeService.getNoticeCount(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);
		//List<Code> subCategoryCode = CodeUtils.getCodeList("NOTICE_SUB_CATEGORY");
		//List<Code> categoryTeamCode = CodeUtils.getCodeList("NOTICE_CATEGORY_TEAM");

        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

		model.addAttribute("list", noticeService.getNoticeList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		//model.addAttribute("subCategoryCode", subCategoryCode);
		//model.addAttribute("categoryTeamCode", categoryTeamCode);

		return "view:/notice/list";
	}
}
