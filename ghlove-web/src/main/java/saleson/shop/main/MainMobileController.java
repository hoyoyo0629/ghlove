package saleson.shop.main;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import saleson.common.context.ShopContext;
import saleson.common.utils.ItemUtils;
import saleson.shop.accountnumber.AccountNumberService;
import saleson.shop.accountnumber.domain.AccountNumber;
import saleson.shop.categories.CategoriesService;
import saleson.shop.categories.domain.Group;
import saleson.shop.categories.domain.Team;
import saleson.shop.config.domain.Config;
import saleson.shop.display.DisplayService;
import saleson.shop.featuredbanner.FeaturedBannerService;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.mobilecategoriesedit.MobileCategoriesEditService;
import saleson.shop.notice.NoticeService;
import saleson.shop.notice.support.NoticeParam;
import saleson.shop.popup.PopupService;
import saleson.shop.ranking.RankingService;
import saleson.shop.ranking.support.RankingParam;
import saleson.shop.stats.StatsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/m")
@RequestProperty(template="mobile", layout="main")
public class MainMobileController {

	private static final Logger log = LoggerFactory.getLogger(MainMobileController.class);

	@Autowired
	private StatsService statsService;
	
	@Autowired
	MobileCategoriesEditService mobileCategoriesEditService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	RankingService rankingService;
	
	@Autowired
	CodeResolver codeResolver;
	
	@Autowired
	NoticeService noticeService;
	
	@Autowired
	private DisplayService displayService;
	
	@Autowired
	private FeaturedBannerService featuredBannerService;

	@Autowired
	private CategoriesService categoriesService;

	@Autowired
	private AccountNumberService accountNumberService;

	@Autowired
	private PopupService popupService;


	@GetMapping("/")
	public ModelAndView indexRedirect(HttpServletResponse response, Model model) {
		final RedirectView rv = new RedirectView("/m");
		rv.setStatusCode(HttpStatus.MOVED_PERMANENTLY);		// 301 Redirect
		return new ModelAndView(rv);
	}
	
	@GetMapping
	public String index(HttpServletRequest request, Model model, HttpServletResponse response) {

		displayService.setMainDisplayByGroupCode(model, "mobile-promotion", "mobile-featured");

		List<AccountNumber> accountNumbers= accountNumberService.getUseAccountNumberListAll();
		model.addAttribute("accountNumbers", accountNumbers);

		return ViewUtils.getView("/main/index");
		
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

	/**
	 * 헬스 체크 URI
	 * @return
	 */
	@GetMapping(value="/healthcheck")
	@ResponseBody
	public String healthCheck() {

		return "OK";
	}

	/**
	 * 메인 - 팝업
	 * @return
	 */
	@GetMapping("/main/popup")
	@RequestProperty(layout="blank")
	public JsonView popup() {
		return JsonViewUtils.success(popupService.displayPopupList());
	}

}
