package saleson.shop.cntntsstsfdg;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.Const;
import saleson.common.notification.ApplicationInfoService;
import saleson.common.notification.UnifiedMessagingService;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.model.Ums;
import saleson.model.campaign.ApplicationInfo;
import saleson.seller.main.SellerService;
import saleson.seller.main.support.SellerParam;
import saleson.shop.cntntsstsfdg.support.CntntsStsfdgParam;
import saleson.shop.item.support.ItemParam;
import saleson.shop.mailconfig.MailConfigService;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.mailconfig.support.QnaCompleteMail;
import saleson.shop.notice.domain.Notice;
import saleson.shop.notice.support.NoticeParam;
import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.domain.QnaAnswer;
import saleson.shop.qna.domain.QnaOpen;
import saleson.shop.qna.domain.QnaOpenFile;
import saleson.shop.qna.support.QnaExcelView;
import saleson.shop.qna.support.QnaOpenParam;
import saleson.shop.qna.support.QnaParam;
import saleson.shop.sendmaillog.SendMailLogService;
import saleson.shop.sendmaillog.domain.SendMailLog;
import saleson.shop.sendsmslog.SendSmsLogService;
import saleson.shop.smsconfig.SmsConfigService;
import saleson.shop.ums.UmsService;
import saleson.shop.ums.support.QnaComplete;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.UserDetail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/opmanager/cntnts-stsfdg")
@RequestProperty(title = "콘텐츠만족도", template="opmanager", layout = "default")
public class CntntsStsfdgManagerController {

	private static final Logger log = LoggerFactory
			.getLogger(CntntsStsfdgManagerController.class);

	@Autowired
	private CntntsStsfdgService service;

	/**
	 * 콘텐츠만족도 목록
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("list")
	public String cntntsStsfdgList(@ModelAttribute CntntsStsfdgParam searchParam,
			RequestContext requestContext, Model model) {

		int count = 0;

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

        String today = DateUtils.getToday(Const.DATE_FORMAT);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		model.addAttribute("searchParam", searchParam);

		return "view";
	}

	/**
	 * 콘텐츠만족도 목록
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("list")
	public String searchCntntsStsfdgList(@ModelAttribute CntntsStsfdgParam searchParam,
			RequestContext requestContext, Model model) {

		int count = service.getCntntsStsfdgCount(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

        String today = DateUtils.getToday(Const.DATE_FORMAT);
        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

		model.addAttribute("list", service.getCntntsStsfdgList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		model.addAttribute("searchParam", searchParam);

		return "view";
	}

	/**
	 * 콘텐츠만족도 상세
	 *
	 * @param requestContext
	 * @param model
	 * @param noticeId
	 * @param searchParam
	 * @return
	 */
	@GetMapping("detail")
	public String detail(RequestContext requestContext, Model model, @ModelAttribute("searchParam") CntntsStsfdgParam searchParam ){


		if (searchParam.getSearchYear() == null || "".equals(searchParam.getSearchYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setSearchYear(String.valueOf(now.getYear()));
		}


		model.addAttribute("sum", service.getCntntsStsfdgSum(searchParam));
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("searchParam", searchParam);

		return "view:/cntnts-stsfdg/form";
	}

	@PostMapping("detail")
	public String searchDetail(RequestContext requestContext, Model model, @ModelAttribute("searchParam") CntntsStsfdgParam searchParam ){


		if (searchParam.getSearchYear() == null || "".equals(searchParam.getSearchYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setSearchYear(String.valueOf(now.getYear()));
		}


		model.addAttribute("sum", service.getCntntsStsfdgSum(searchParam));
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		model.addAttribute("searchParam", searchParam);

		return "view:/cntnts-stsfdg/form";
	}


	/**
	 * 콘텐츠만족도 통계
	 *
	 * @param requestContext
	 * @param model
	 * @param noticeId
	 * @param searchParam
	 * @return
	 */
	@PostMapping("statistics")
	public JsonView statistics(RequestContext requestContext, Model model, @ModelAttribute("searchParam") CntntsStsfdgParam searchParam ){

		return JsonViewUtils.success(service.getStatisticsByMonth(searchParam));
	}


}
