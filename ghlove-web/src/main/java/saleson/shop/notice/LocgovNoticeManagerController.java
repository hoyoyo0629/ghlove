package saleson.shop.notice;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import saleson.common.Const;
import saleson.seller.main.SellerService;
import saleson.seller.main.support.SellerParam;
import saleson.shop.code.domain.Code;
import saleson.shop.notice.domain.Notice;
import saleson.shop.notice.support.NoticeParam;
import saleson.shop.user.LocgovService;
import saleson.shop.user.domain.Locgov;

@Controller
@RequestMapping("/opmanager/locgov-notice/**")
@RequestProperty(title="지자체 공지사항", layout="default", template="opmanager")
public class LocgovNoticeManagerController {
    private static final Logger log = LoggerFactory.getLogger(NoticeManagerController.class);

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CodeResolver codeResolver;

    @Autowired
    private SellerService sellerService;

    /** 지자체관리 Service */
    @Autowired
    LocgovService locgovService;

    @GetMapping("list")
    public String list(RequestContext requestContext, @ModelAttribute("searchParam") NoticeParam noticeParam , Model model) {

    	String today1 = DateUtils.getToday("yyyyMMdd");
    	noticeParam.setStartCreateDate(StringUtils.defaultIfEmpty(noticeParam.getStartCreateDate(), today1));
    	noticeParam.setEndCreateDate(StringUtils.defaultIfEmpty(noticeParam.getEndCreateDate(), today1));

    	String adminRole = locgovService.getLoginUserAdminRoleCheck();
    	if ("LOC".equals(adminRole)) {
    		Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId());
            if (locgovCodeDetails != null) {
            	noticeParam.setLocgovCode(locgovCodeDetails.getId());
            }
    	}

        int count = 0;

        Pagination pagination = Pagination.getInstance(count, noticeParam.getItemsPerPage());
        noticeParam.setPagination(pagination);
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

        model.addAttribute("role", adminRole);
        //model.addAttribute("subCategoryCode", subCategoryCode);
        //model.addAttribute("categoryTeamCode", categoryTeamCode);

        return "view:/locgov-notice/list";

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

    @PostMapping("create")
    public String createAction(RequestContext requestContext, Notice notice){

        Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId());
        if (locgovCodeDetails != null) {
        	notice.setLocgovCode(locgovCodeDetails.getId());
        }

        notice.setUserName(SecurityUtils.getCurrentUser().getUserName());
        noticeService.insertLocgovNotice(notice);

        return ViewUtils.redirect("/opmanager/locgov-notice/list","등록되었습니다.");
    }

    @GetMapping("edit/{noticeId}")
    public String edit(RequestContext requestContext, Model model, @PathVariable("noticeId") int noticeId, @ModelAttribute("searchParam") NoticeParam searchParam ){

    	String adminRole = locgovService.getLoginUserAdminRoleCheck();
    	model.addAttribute("role", adminRole);

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
        noticeService.updateNotice(notice);

        return ViewUtils.redirect("/opmanager/locgov-notice/list", MessageUtils.getMessage("M00289"));
    }

    @PostMapping("delete/{noticeId}")
    public JsonView delete(RequestContext requestContext, @PathVariable int noticeId) {
        if (!requestContext.isAjaxRequest()) {
            throw new NotAjaxRequestException();
        }

        try {
            noticeService.deleteNotice(noticeId);
        } catch (RuntimeException e) {
//            return JsonViewUtils.exception(e.getMessage());
            //return JsonViewUtils.exception(MessageUtils.getMessage("실패했습니다."));			// 실패했습니다.
            return JsonViewUtils.exception("실패했습니다.");			// 실패했습니다.
        }
        return JsonViewUtils.success();
    }

    @PostMapping("/deleteNotice")
    public JsonView locgovNoticeDelete(RequestContext requestContext, NoticeParam noticeParam) {
        String code = "";

        try {
            code = noticeService.locgovNoticeDelete(noticeParam);
        } catch (RuntimeException e) {
//            log.error("ERROR: {}", e.getMessage(), e);
            log.error("ERROR: {}", getClass().getName() + " :: locgovNoticeDelete RuntimeException ==========");
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

    /**
     * /** Date 조회
     *
     * @param requestContextCREATED_DATE
     * @return
     */
    @PostMapping("search-date")
    public JsonView searchDate(RequestContext requestContext, Model model,
                               @RequestParam(value = "date", required = false) int date,
                               @RequestParam(value = "type", required = false) String type) {

        int month = 0;
        int day = 0;

        if (type.equals("week")) {
            day = date;
        } else if (type.equals("month")) {
            month = date;
        }

        String nowDate = DateUtils.getToday(Const.DATE_FORMAT);
        String searchDate = DateUtils.addYearMonthDay(nowDate, 0, month, day);

        model.addAttribute("nowDate", nowDate);
        model.addAttribute("searchDate", searchDate);

        return JsonViewUtils.success();
    }

    @PostMapping("list")
    public String searchList(RequestContext requestContext, @ModelAttribute("searchParam") NoticeParam noticeParam , Model model) {
    	String today1 = DateUtils.getToday("yyyyMMdd");
    	noticeParam.setStartCreateDate(StringUtils.defaultIfEmpty(noticeParam.getStartCreateDate(), today1));
    	noticeParam.setEndCreateDate(StringUtils.defaultIfEmpty(noticeParam.getEndCreateDate(), today1));

    	String adminRole = locgovService.getLoginUserAdminRoleCheck();
    	if ("LOC".equals(adminRole)) {
    		Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId());
            if (locgovCodeDetails != null) {
            	noticeParam.setLocgovCode(locgovCodeDetails.getId());
            }
    	}

        int count = noticeService.getLocgovNoticeCount(noticeParam);

        Pagination pagination = Pagination.getInstance(count, noticeParam.getItemsPerPage());
        noticeParam.setPagination(pagination);
        //List<Code> subCategoryCode = CodeUtils.getCodeList("NOTICE_SUB_CATEGORY");
        //List<Code> categoryTeamCode = CodeUtils.getCodeList("NOTICE_CATEGORY_TEAM");

        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

        model.addAttribute("list", noticeService.getLocgovNoticeList(noticeParam));
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);

        model.addAttribute("role", adminRole);
        //model.addAttribute("subCategoryCode", subCategoryCode);
        //model.addAttribute("categoryTeamCode", categoryTeamCode);

        return "view:/locgov-notice/list";
    }
}
