package saleson.shop.main;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.ThreadContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.DeviceUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import saleson.api.common.ApiResponseEntity;
import saleson.common.context.ShopContext;
import saleson.common.utils.ItemUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.accountnumber.AccountNumberService;
import saleson.shop.accountnumber.domain.AccountNumber;
import saleson.shop.cache.ShopCacheService;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Group;
import saleson.shop.categories.domain.Team;
import saleson.shop.categoriesedit.CategoriesEditService;
import saleson.shop.categoriesteamgroup.CategoriesTeamGroupService;
import saleson.shop.categoriesteamgroup.domain.CategoriesTeam;
import saleson.shop.config.domain.Config;
import saleson.shop.display.DisplayService;
import saleson.shop.featuredbanner.FeaturedBannerService;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.notice.NoticeService;
import saleson.shop.notice.support.NoticeParam;
import saleson.shop.popup.PopupService;
import saleson.shop.ranking.RankingService;
import saleson.shop.ranking.support.RankingParam;
import saleson.shop.rankingbatch.RankingBatchService;
import saleson.shop.stats.StatsService;
import saleson.shop.user.LocgovService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/")
@RequestProperty(template="front", layout="main", title="메인")
public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private StatsService statsService;
	
	@Autowired
	private CategoriesService categoriesService;
	
	@Autowired
	private RankingService rankingService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private PopupService popupService;
	
	@Autowired
	private CategoriesEditService categoriesEditService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private CodeResolver codeResolver;
	
	@Autowired
	private	 CategoriesTeamGroupService categoriesTeamGroupService;
	
	@Autowired
	private DisplayService displayService;
	
	@Autowired
	private FeaturedBannerService featuredBannerService;
	
	@Autowired
	private RankingBatchService rankingBatchService;

	@Autowired
	private ShopCacheService shopCacheService;
	
	@Autowired
	private MainService mainService;
	
	/** 지자체관리 Service */
	@Autowired
	private LocgovService locgovService;

	@Autowired
	Environment environment;

	@Autowired
	AccountNumberService accountNumberService;
	
	
	/**
	 * 메인페이지
	 * @return
	 */
	@GetMapping(value="/")
	@RequestProperty(template="front", layout="main", title="메인")
	public String index(HttpServletRequest request,  HttpServletResponse response, HttpSession session,
			Model model, RequestContext requestContext) {
		
		displayService.setMainDisplayByGroupCode(model, "front-promotion", "front-featured");

		HashMap<String, List<Item>> bestItemsByTeam = new HashMap<String, List<Item>>();
		HashMap<String, List<Item>> groupBannerItemsByGroup = new HashMap<String, List<Item>>();
		ShopContext shopContext = (ShopContext) ThreadContext.get(ShopContext.REQUEST_NAME);

		List<Group> itemsForGroupBanner = new ArrayList<>();
		
		RankingParam rankingParam = new RankingParam();
		rankingParam.setGroups(shopContext.getShopCategoryGroups());
		rankingParam.setViewTarget("WEB");
		rankingParam.setLimit(10);
		rankingParam.setConditionType("FRONT_DISPLAY_ITEM");
		rankingParam.setPrivateTypes(ItemUtils.getPrivateTypes());

		List<Group> bestItems = new ArrayList<>();
		for(Group group : shopContext.getShopCategoryGroups()) {
			boolean isMatched = false;
			for (Group group1 : bestItems) {
				if (group.getUrl().equals(group1.getCode())) {
					bestItemsByTeam.put(group.getUrl(), group1.getItems());
					isMatched = true;
				}
			}
			
			if (!isMatched) {
				bestItemsByTeam.put(group.getUrl(), new ArrayList<Item>());
			}
			
			isMatched = false;
			for (Group group2 : itemsForGroupBanner) {
				if (group.getUrl().equals(group2.getCode())) {
					groupBannerItemsByGroup.put(group.getUrl(), group2.getItems());
					isMatched = true;
				}
			}
			
			if (!isMatched) {
				groupBannerItemsByGroup.put(group.getUrl(), new ArrayList<Item>());
			}
		}

		model.addAttribute("groupBannerItemsByGroup", groupBannerItemsByGroup);
		model.addAttribute("bestItemsByTeam", bestItemsByTeam);

		model.addAttribute("lnbType", "main");
		
		List<AccountNumber> accountNumbers= accountNumberService.getUseAccountNumberListAll();
		model.addAttribute("accountNumbers", accountNumbers);
		
		return ViewUtils.getView("/main/index");

	}
	
	/**
	 * 메인페이지
	 * @return
	 */
	@GetMapping("/main")
	@RequestProperty(template="front", layout="main", title="메인")
	public String index3(HttpServletRequest request,  HttpServletResponse response, HttpSession session,
			Model model, RequestContext requestContext) {

		model.addAttribute("promotion", displayService.getDisplayByGroupCode("front-promotion", "ALL"));
		model.addAttribute("featuredBanner", displayService.getDisplayByGroupCode("front-featured", "ALL"));
		
		// 1. 신상품 / 랭킹 / BEST 상품 조회
		HashMap<String, List<Item>> bestItemsByTeam = new HashMap<String, List<Item>>();
		HashMap<String, List<Item>> groupBannerItemsByGroup = new HashMap<String, List<Item>>();
		ShopContext shopContext = (ShopContext) ThreadContext.get(ShopContext.REQUEST_NAME);
		
		for(Group group : shopContext.getShopCategoryGroups()) {
			if (StringUtils.isNotEmpty(group.getItemList())) {
				groupBannerItemsByGroup.put(group.getUrl(), itemService.getItemListForGroupBanner(group.getItemList()));
			}
			
			// BEST
			RankingParam rankingParam = new RankingParam();
			rankingParam.setConditionType("FRONT_DISPLAY_ITEM");
			rankingParam.setRankingCode(group.getUrl());
			rankingParam.setViewTarget("WEB");
			rankingParam.setLimit(10);
			rankingParam.setPrivateTypes(ItemUtils.getPrivateTypes());

			bestItemsByTeam.put(group.getUrl(), rankingService.getRankingListForFront(rankingParam));
		}

		model.addAttribute("groupBannerItemsByGroup", groupBannerItemsByGroup);
		model.addAttribute("bestItemsByTeam", bestItemsByTeam);
				
		// 2. 공지사항.
		NoticeParam noticeParam = new NoticeParam();
		noticeParam.setConditionType("main");
		noticeParam.setLimit(3);
		model.addAttribute("noticeList", noticeService.getFrontNoticeList(noticeParam));
		
		// 3. 팝업조회
		model.addAttribute("popupList", popupService.displayPopupList());
		
		model.addAttribute("lnbType", "main");
			
		// 4. 스팟 상품
		model.addAttribute("spotItems", displayService.getMainSpotItems(4));
		
		// 5. MD 추천 상품
		model.addAttribute("mdItems", displayService.getDisplayItemList("md"));
		
		// 6. 신상품
		model.addAttribute("newItems", displayService.getDisplayItemList("new"));
	
		// 0. LNB 그룹 배너.
		List<CategoriesTeam> groupBanners = categoriesTeamGroupService.getCategoriesTeamGroupList();
		model.addAttribute("groupBanners", groupBanners);
		
		return ViewUtils.getView("/main/index_old_20170605");
		

	}
	
	
	@PostMapping("/main/lnb-group-banners")	//
	@RequestProperty(layout="blank")
	public String lnbGroupBanners(RequestContext requestContext, Model model) {
		// 1. 카테고리 정보
    	log.debug("[Cache] categoriesService.getCategoriesForFront");
    	List<Team> categories = categoriesService.getCategoriesForFront();
    	
    	List<Group> shopCategoryGroups = new ArrayList<>();
    	HashMap<String, List<Item>> groupBannerItemsByGroup = new HashMap<String, List<Item>>();
    	for(Team team : categories) {
    		if (!Config.SHOP_CATEGORY_GROUP_KEY.equals(team.getUrl())) {
    			continue;
    		}
    		shopCategoryGroups.addAll(team.getGroups());
    	}
    	
    	for(Group group : shopCategoryGroups) {
			if (StringUtils.isNotEmpty(group.getItemList())) {
				groupBannerItemsByGroup.put(group.getUrl(), itemService.getItemListForGroupBanner(group.getItemList()));
			}
		}

    	model.addAttribute("shopCategoryGroups", shopCategoryGroups);
		model.addAttribute("groupBannerItemsByGroup", groupBannerItemsByGroup);
		model.addAttribute("groupBanners", categoriesTeamGroupService.getCategoriesTeamGroupList()); 	// 0. LNB 그룹 배너.   	
		return "view";
	}
	
	/**
	 * MD 추천상품 조회 
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/main/md-items") //
	@RequestProperty(layout="blank")
	public String mdItems(RequestContext requestContext, Model model) {
		model.addAttribute("mdItems", displayService.getDisplayItemList("md"));
		return "view";
	}
	
	/**
	 * 신상품 조회 
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/main/new-items")
	@RequestProperty(layout="blank")
	public String newItems(RequestContext requestContext, Model model) {
		model.addAttribute("newItems", displayService.getDisplayItemList("new"));
		return "view";
	}
	
	/**
	 *BEST 추천상품 조회 
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/main/best-items")
	@RequestProperty(layout="blank")
	public String bestItems(RequestContext requestContext, ShopContext shopContext, Model model) {
		// 1. 카테고리 정보
    	log.debug("[Cache] categoriesService.getCategoriesForFront");
    	List<Team> categories = categoriesService.getCategoriesForFront();
    	
    	List<Group> shopCategoryGroups = new ArrayList<>();
    	for(Team team : categories) {
    		if (!Config.SHOP_CATEGORY_GROUP_KEY.equals(team.getUrl())) {
    			continue;
    		}
    		shopCategoryGroups.addAll(team.getGroups());
    	}
		
		
		HashMap<String, List<Item>> bestItemsByTeam = new HashMap<String, List<Item>>();
		
		RankingParam rankingParam = new RankingParam();
		rankingParam.setGroups(shopCategoryGroups);
		rankingParam.setViewTarget("WEB");
		rankingParam.setLimit(10);
		rankingParam.setConditionType("FRONT_DISPLAY_ITEM");
		rankingParam.setPrivateTypes(ItemUtils.getPrivateTypes());
		List<Group> bestItems = rankingService.getBestItemsForFrontByGroups(rankingParam);
		
    		
		for(Group group : shopCategoryGroups) {
			boolean isMatched = false;
			for (Group group1 : bestItems) {
				if (group.getUrl().equals(group1.getCode())) {
					bestItemsByTeam.put(group.getUrl(), group1.getItems());
					isMatched = true;
				}
			}
		
			if (!isMatched) {
				bestItemsByTeam.put(group.getUrl(), new ArrayList<Item>());
			}
		}
		
		

		model.addAttribute("shopCategoryGroups", shopCategoryGroups);
		model.addAttribute("bestItemsByTeam", bestItemsByTeam);
		
		return "view";
	}


	@PostMapping("/main/spot-items")
	@RequestProperty(layout="blank")
	public String spotitems(Model model) {
		int limit = 4;
		model.addAttribute("spotItems", displayService.getMainSpotItems(limit));

		return "view";
	}

	/**
	 * 메인 - 공지사항
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/main/notice")
	@RequestProperty(layout="blank")
	public String notice(RequestContext requestContext, Model model) {
		NoticeParam noticeParam = new NoticeParam();
		noticeParam.setConditionType("main");
		noticeParam.setLimit(3);
		model.addAttribute("noticeList", noticeService.getFrontNoticeList(noticeParam));
		return "view";
	}
	
	@GetMapping("/main/popup")
	@RequestProperty(layout="blank")
	public JsonView popup() {
		return JsonViewUtils.success(popupService.displayPopupList());
	}
	
	
	/**
	 * 메인페이지 (//로 요청이 오면 404)
	 * @return
	 */
	@ResponseBody
	@GetMapping("/{another}")
	@RequestProperty(template="front", layout="main", title="메인")
	public String index2(HttpServletRequest request,  HttpServletResponse response, HttpSession session,
			Model model, RequestContext requestContext) {
		throw new PageNotFoundException();
	}
	
	
	/**
	 * 모바일 디바이스 처리
	 * @param request
	 * @return
	 */
	private boolean isMobile(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		String siteReference = request.getParameter("SITE_REFERENCE");
		String sessionSiteReference = (String) session.getAttribute("SITE_REFERENCE");
		
		if (DeviceUtils.isMobile(request)) {
			if (siteReference != null && siteReference.equals(DeviceUtils.NORMAL)) {
				session.setAttribute("SITE_REFERENCE", DeviceUtils.NORMAL);
				return false;
			} else if (sessionSiteReference != null && sessionSiteReference.equals(DeviceUtils.NORMAL)) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 헬스 체크 URI
	 * @return
	 */
	@GetMapping(value="/healthcheck")
	@ResponseBody
	public String healthCheck() {

		return "Saleson Server is alive!!";
	}

	/**
	 * 캐시관련 reload
	 * @return
	 */
	@GetMapping("/reload-cache/{cacheName}")
	public ResponseEntity reloadCache(@PathVariable("cacheName") String cacheName) {
		shopCacheService.removeCache(cacheName);
		return ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
	}


	/**
	 * 콜 페이지 
	 * @param requestcontext
	 * @return
	 */
	
	@GetMapping("/opmanager/popup/insertCall")
	@RequestProperty(title = "콜 jsp", layout = "base")
	public String popupGet(RequestContext requestcontext) {
		return ViewUtils.getView("/main/popup/insertCall");
	}

	/**
	 * 콜 삭제 및 등록
	 * @param requestContext
	 * @param giveState
	 * @return
	 */
	
	@PostMapping("/opmanager/popup/insertCall")
	@RequestProperty(title = "콜등록", layout = "base")
	public ResponseEntity opmanagerInsertCall(RequestContext requestContext, @ModelAttribute("giveState") GiveState giveState) {
		
		giveState.setRegistId(UserUtils.getManagerId());
		giveState.setCallDate(DateUtils.getToday());
		ResponseEntity result = null;
		try {
			mainService.opmanagerInsertCall(giveState);
		}catch(OpRuntimeException e){
			return result = ApiResponseEntity.data()
					.put("status", HttpStatus.FAILED_DEPENDENCY)
					.ok();
		}
		return result = ApiResponseEntity.data()
				.put("status", HttpStatus.OK)
				.ok();
	}
	
	


}
