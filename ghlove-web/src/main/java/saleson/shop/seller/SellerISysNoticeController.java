package saleson.shop.seller;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import saleson.common.Const;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.support.SellerParam;
import saleson.shop.categoriesteamgroup.CategoriesTeamGroupService;
import saleson.shop.community.doamin.CmntySrBbsDto;
import saleson.shop.item.ItemManagerController;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemReview;
import saleson.shop.item.domain.ItemReviewImage;
import saleson.shop.item.domain.ItemSaleEdit;
import saleson.shop.item.support.ItemListParam;
import saleson.shop.item.support.ItemParam;
import saleson.shop.item.support.ItemSaleEditParam;
import saleson.shop.notice.SysNoticeSellerController;
import saleson.shop.notice.SysNoticeSellerService;
import saleson.shop.notice.domain.SysNoticeSellerDto;
import saleson.shop.notice.domain.SysNoticeSellerFileDto;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestProperty(template="seller", layout="default")
@RequestMapping("/seller/sys-notice")
public class SellerISysNoticeController extends SysNoticeSellerController {

	private static final Logger log = LoggerFactory.getLogger(SellerISysNoticeController.class);


	@Autowired
	private SysNoticeSellerService sysNoticeSellerService;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private SellerService sellerService;



	@GetMapping("/list")
	public String list(@ModelAttribute("searchParam") SysNoticeSellerDto noticeParam , Model model) {
		//카운트
		int count = sysNoticeSellerService.getFrontNoticeListCount(noticeParam);
        //페이징
		 Pagination pagination = Pagination.getInstance(count, noticeParam.getItemsPerPage());
        noticeParam.setPagination(pagination);

        //날짜
        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

        model.addAttribute("list", sysNoticeSellerService.getFrontNoticeList(noticeParam));
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);

	      //  model.addAttribute("role", adminRole);


		return "view:/sellerNotice/list";
	}

	@PostMapping("/list")
	public String searchList(@ModelAttribute("searchParam") SysNoticeSellerDto noticeParam , Model model) {
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

        model.addAttribute("list", sysNoticeSellerService.getFrontNoticeList(noticeParam));
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);

	      //  model.addAttribute("role", adminRole);


		return "view:/sellerNotice/list";
	}

	@GetMapping("/detail/{noticeId}")
	public String detail(RequestContext requestContext, Model model, @PathVariable("noticeId") int noticeId) {

		// 조회수 증가
		sysNoticeSellerService.addHitCount(noticeId);

		SysNoticeSellerDto result = sysNoticeSellerService.getNotice(noticeId,model);
		if(result == null) {
			return ViewUtils.redirect("/opmanager/sellerNotice/list", "존재하지 않는 게시글입니다.");
		}

		model.addAttribute("detail", result);

		return "view:/sellerNotice/detail";
	}

	@GetMapping("/file-download/{fileId}")
	public ResponseEntity<?> fileDownloadNoitice(@PathVariable long fileId) throws IOException {
		return super.fileDownloadNoitice(fileId);
	}

}
