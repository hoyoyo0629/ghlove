package saleson.shop.community.locgFaq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.common.Const;
import saleson.common.enumeration.FaqType;
import saleson.common.enumeration.mapper.EnumMapper;
import saleson.common.utils.UserUtils;
import saleson.model.Faq;
import saleson.shop.community.doamin.CommunityDto;
import saleson.shop.community.doamin.LocgfaqDto;
import saleson.shop.community.freeboard.CommunityManagerController;
import saleson.shop.community.locgfaq.LocgFaqService;
import saleson.shop.databoard.support.DataboardParam;
import saleson.shop.faq.FaqDto;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.notice.domain.Notice;

@Controller
@RequestMapping("/opmanager/community/locv-faq")
@RequestProperty(title="지자체faq", layout="default", template="opmanager")
public class LocgFaqManagerController {

	private static final Logger log = LoggerFactory.getLogger(LocgFaqManagerController.class);

	@Autowired
	EnumMapper enumMapper;

	@Autowired
	LocgFaqService locgboardService;

    //시퀀스
    @Autowired
    private SequenceService sequenceService;

	@GetMapping("list")
	public String getList(@ModelAttribute("searchParam") LocgfaqDto locgFaqDto, RequestContext requestContext, Model model) {
		if(locgFaqDto.getFaqType() != null) {
			FaqType  faqType1  =locgFaqDto.getFaqType();
			String faqname = faqType1.toString();
			locgFaqDto.setFaqTypeName(faqname);
		}

		int count = locgboardService.getlocFaqBoardCount(locgFaqDto);
		Pagination pagination = Pagination.getInstance(count, locgFaqDto.getItemsPerPage());
		locgFaqDto.setPagination(pagination);

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

//				ROLE_ADMIN_1	시스템주담당자
//				ROLE_ADMIN_2	시스템부담당자
//				ROLE_ADMIN_3	행안부주담당자
//				ROLE_ADMIN_4	행안부부담당자
//				ROLE_ADMIN_5	지자체주담당자
//				ROLE_ADMIN_6	지자체부담당자
				if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
					role = "mois";
				} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
					role = "locgov";
				}
			}

		if(role.equals("mois")) {
			 model.addAttribute("role",role);
		 }


			//날짜
		String today = DateUtils.getToday(Const.DATE_FORMAT);

		List<LocgfaqDto> faqList= locgboardService.listLocgBoard(locgFaqDto);


		FaqType[] faqType = FaqType.values();

		model.addAttribute("faqTypes", faqType);
		model.addAttribute("pagination", pagination);
		model.addAttribute("list", faqList);
        model.addAttribute("today", today);


		return "view:/community/locgfaq/list";

	}

	@PostMapping("list")
	public String searchGetList(@ModelAttribute("searchParam") LocgfaqDto locgFaqDto, RequestContext requestContext, Model model) {
		if(locgFaqDto.getFaqType() != null) {
			FaqType  faqType1  =locgFaqDto.getFaqType();
			String faqname = faqType1.toString();
			locgFaqDto.setFaqTypeName(faqname);
		}

		int count = locgboardService.getlocFaqBoardCount(locgFaqDto);
		Pagination pagination = Pagination.getInstance(count, locgFaqDto.getItemsPerPage());
		locgFaqDto.setPagination(pagination);

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

//				ROLE_ADMIN_1	시스템주담당자
//				ROLE_ADMIN_2	시스템부담당자
//				ROLE_ADMIN_3	행안부주담당자
//				ROLE_ADMIN_4	행안부부담당자
//				ROLE_ADMIN_5	지자체주담당자
//				ROLE_ADMIN_6	지자체부담당자
				if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
					role = "mois";
				} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
					role = "locgov";
				}
			}

		if(role.equals("mois")) {
			 model.addAttribute("role",role);
		 }


			//날짜
		String today = DateUtils.getToday(Const.DATE_FORMAT);

		List<LocgfaqDto> faqList= locgboardService.listLocgBoard(locgFaqDto);


		FaqType[] faqType = FaqType.values();

		model.addAttribute("faqTypes", faqType);
		model.addAttribute("pagination", pagination);
		model.addAttribute("list", faqList);
        model.addAttribute("today", today);


		return "view:/community/locgfaq/list";

	}
	
	@GetMapping("create")
	public String getLocgFaqForm(@ModelAttribute("searchParam") LocgfaqDto locgFaqDto, RequestContext requestContext, Model model) {

		List<LocgfaqDto> faqList= locgboardService.listLocgBoard(locgFaqDto);
		FaqType[] faqType = FaqType.values();

		model.addAttribute("faq", faqType);
		return "view:/community/locgfaq/form";
	}

	@PostMapping("create")
	public String createLocgFaq(@ModelAttribute("searchParam") LocgfaqDto locgFaqDto, RequestContext requestContext, Model model) {
		long userId = requestContext.getUser().getUserId();
		long id = sequenceService.getLong("OP_COMMUNITY_LOCGOVFAQ");

		locgFaqDto.setAdminId(userId);
		locgFaqDto.setId(id);

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

//				ROLE_ADMIN_1	시스템주담당자
//				ROLE_ADMIN_2	시스템부담당자
//				ROLE_ADMIN_3	행안부주담당자
//				ROLE_ADMIN_4	행안부부담당자
//				ROLE_ADMIN_5	지자체주담당자
//				ROLE_ADMIN_6	지자체부담당자
				if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
					role = "mois";
				} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
					role = "locgov";
				}
			}

		if(role != null && !role.equals("mois")) {
			return ViewUtils.redirect("/opmanager/community/locv-faq/list", "권한이 없습니다.");
		 }

		locgboardService.insertLocgBoard(locgFaqDto);

		return ViewUtils.redirect("/opmanager/community/locv-faq/list", MessageUtils.getMessage("M00632"));
	}

	@GetMapping("list/{id}")
	public String detailLocgFaq(@ModelAttribute("searchParam") LocgfaqDto locgFaqDto, RequestContext requestContext,@PathVariable("id") int id, Model model) {
		locgFaqDto.setId(id);
		LocgfaqDto item = locgboardService.deatilLocgBoard(locgFaqDto);
		//조회수 증가
		locgboardService.addHitCount(id);

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

//				ROLE_ADMIN_1	시스템주담당자
//				ROLE_ADMIN_2	시스템부담당자
//				ROLE_ADMIN_3	행안부주담당자
//				ROLE_ADMIN_4	행안부부담당자
//				ROLE_ADMIN_5	지자체주담당자
//				ROLE_ADMIN_6	지자체부담당자
				if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
					role = "mois";
				} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
					role = "locgov";
				}
			}

		if( !role.equals("mois")) {
			model.addAttribute("item",item);
			return "view:/community/locgfaq/detail";

		 }
		item.setAdminCheck("1");
		model.addAttribute("item",item);
		return "view:/community/locgfaq/detail";
	}

	@GetMapping("edit/{id}")
	public String getEditLocgFaq(@ModelAttribute("searchParam") LocgfaqDto locgFaqDto, RequestContext requestContext,@PathVariable("id") int id, Model model) {
		locgFaqDto.setId(id);
		LocgfaqDto item = locgboardService.deatilLocgBoard(locgFaqDto);

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

//				ROLE_ADMIN_1	시스템주담당자
//				ROLE_ADMIN_2	시스템부담당자
//				ROLE_ADMIN_3	행안부주담당자
//				ROLE_ADMIN_4	행안부부담당자
//				ROLE_ADMIN_5	지자체주담당자
//				ROLE_ADMIN_6	지자체부담당자
				if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
					role = "mois";
				} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
					role = "locgov";
				}
			}

		if(!role.equals("mois")) {
			return ViewUtils.redirect("/opmanager/community/locv-faq/list", "권한이 없습니다.");
		 }
		item.setAdminCheck("1");
		model.addAttribute("item",item);

		return "view:/community/locgfaq/edit";
	}

	@PostMapping("edit/{id}")
	public String updateEditLocgFaq(@ModelAttribute("searchParam") LocgfaqDto locgFaqDto, RequestContext requestContext,@PathVariable("id") int id, Model model) {

		long user = UserUtils.getUserId();
		locgFaqDto.setAdminId(user);
		locgFaqDto.setId(id);


		locgboardService.updateLocgBoard(locgFaqDto);



		 return ViewUtils.redirect("/opmanager/community/locv-faq/list", MessageUtils.getMessage("M00289"));

	}

	@PostMapping("delete/{id}")
	public JsonView deleteEditLocgFaq(@ModelAttribute("searchParam") LocgfaqDto locgFaqDto, RequestContext requestContext,@PathVariable("id") int id, Model model) {
		locgFaqDto.setId(id);

		User user = UserUtils.getUser();
		String role = "";
		for (UserRole userRole : user.getUserRoles()) {

	//					ROLE_ADMIN_1	시스템주담당자
	//					ROLE_ADMIN_2	시스템부담당자
	//					ROLE_ADMIN_3	행안부주담당자
	//					ROLE_ADMIN_4	행안부부담당자
	//					ROLE_ADMIN_5	지자체주담당자
	//					ROLE_ADMIN_6	지자체부담당자
				if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
					role = "mois";
				} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
					role = "locgov";
				}
			}

		if(!role.equals("mois")) {

			return JsonViewUtils.exception("실패했습니다.");
		}
		locgboardService.deleteLocgBoard(locgFaqDto);

		return JsonViewUtils.success();
	}

	 @PostMapping("deleteDataboard")
	   public JsonView databoardListDelete(RequestContext requestContext, LocgfaqDto locgFaqDto) {
	        String code = "";

	        try {
	            code = locgboardService.databoardListDelete(locgFaqDto);
	        } catch (Exception e) {
	            log.error("ERROR: {}", "에러가 발생했습니다.");
	        }

	        return JsonViewUtils.success(code);
	    }


}
