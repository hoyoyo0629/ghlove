package saleson.shop.featured;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import saleson.common.enumeration.IdType;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.brand.BrandService;
import saleson.shop.categoriesedit.CategoriesEditService;
import saleson.shop.categoriesedit.domain.CategoriesEdit;
import saleson.shop.categoriesedit.support.CategoriesEditParam;
import saleson.shop.categoriesteamgroup.CategoriesTeamGroupService;
import saleson.shop.featured.domain.Featured;
import saleson.shop.featured.domain.FeaturedReply;
import saleson.shop.featured.support.*;
import saleson.shop.user.LocgovService;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping({"/opmanager/featured"})
@RequestProperty(title="이벤트 관리", layout="default", template="opmanager")
public class FeaturedManagerController {

	private static final Logger log = LoggerFactory.getLogger(FeaturedManagerController.class);

	@Autowired
	private FeaturedService featuredService;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private CategoriesEditService categoriesEditService;

	@Autowired
	private CategoriesTeamGroupService categoriesTeamGroupService;


	/**
	 * 관리자 특집페이지 관리 리스트
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String list(@RequestParam(value="itemsPerPage", required=false) String itemsPerPage,
			FeaturedParam featuredParam, Model model) {

		setLocgovCode(featuredParam);

		// 모바일 / PC 구분
		if (getFeaturedTypeUri().equals("featured")) {
			featuredParam.setFeaturedType("1");
		} else {
			featuredParam.setFeaturedType("2");
		}

		//진행상태
		/*if (ObjectUtils.isEmpty(featuredParam.getProgression())) {
			featuredParam.setProgression("2"); //진행중
		}*/
		if (itemsPerPage == null) {
			featuredParam.setItemsPerPage(10);
		} else {
			try {
				featuredParam.setItemsPerPage(Integer.valueOf(itemsPerPage));
			} catch (NumberFormatException e) {
				featuredParam.setItemsPerPage(10);
			}
		}
		featuredParam.setSort("date");		// 정렬 기준
		int count = 0;
		String query ="";

		if(featuredParam.getQuery() != null){
			query = featuredParam.getQuery();
			featuredParam.setQuery(featuredParam.getQuery().replace("/pages/", "").replace("/pages", "").replace("pages/", ""));
		}

		Pagination pagination = Pagination.getInstance(count, featuredParam.getItemsPerPage());
		featuredParam.setPagination(pagination);

		model.addAttribute("featuredList", Collections.EMPTY_LIST);
		featuredParam.setQuery(query);
		model.addAttribute("featuredParam",featuredParam);
		model.addAttribute("featuredCount",count);
		model.addAttribute("pagination",pagination);
		model.addAttribute("featuredTypeUri", getFeaturedTypeUri());
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드
		model.addAttribute("itemsPerPage", itemsPerPage);

		return ViewUtils.getView("/featured/list");

	}

	/**
	 * 관리자 특집페이지 관리 리스트
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@PostMapping("/list")
	public String searchList(@RequestParam(value="itemsPerPage", required=false) String itemsPerPage,
			FeaturedParam featuredParam, Model model) {

		setLocgovCode(featuredParam);

		// 모바일 / PC 구분
		if (getFeaturedTypeUri().equals("featured")) {
			featuredParam.setFeaturedType("1");
		} else {
			featuredParam.setFeaturedType("2");
		}

		//진행상태
		/*if (ObjectUtils.isEmpty(featuredParam.getProgression())) {
			featuredParam.setProgression("2"); //진행중
		}*/
		if (itemsPerPage == null) {
			featuredParam.setItemsPerPage(10);
		} else {
			try {
				featuredParam.setItemsPerPage(Integer.valueOf(itemsPerPage));
			} catch (NumberFormatException e) {
				featuredParam.setItemsPerPage(10);
			}
		}
		featuredParam.setSort("date");		// 정렬 기준
		int count = featuredService.getFeaturedCountByParam(featuredParam);
		String query ="";

		if(featuredParam.getQuery() != null){
			query = featuredParam.getQuery();
			featuredParam.setQuery(featuredParam.getQuery().replace("/pages/", "").replace("/pages", "").replace("pages/", ""));
		}

		Pagination pagination = Pagination.getInstance(count, featuredParam.getItemsPerPage());
		featuredParam.setPagination(pagination);

		model.addAttribute("featuredList",featuredService.getFeaturedListByParam(featuredParam));
		featuredParam.setQuery(query);
		model.addAttribute("featuredParam",featuredParam);
		model.addAttribute("featuredCount",count);
		model.addAttribute("pagination",pagination);
		model.addAttribute("featuredTypeUri", getFeaturedTypeUri());
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드
		model.addAttribute("itemsPerPage", itemsPerPage);

		return ViewUtils.getView("/featured/list");

	}

	/**
	 * 목록데이터 수정 - 기획페이지 노출 순서를 설정한다.
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
//	@PostMapping("list/change-ordering")
//	public JsonView changeOrdering(RequestContext requestContext, FeaturedListParam featuredListParam) {
//
//		if (!requestContext.isAjaxRequest()) {
//		    throw new NotAjaxRequestException();
//		}
//
//		String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
//		if (StringUtils.isEmpty(locgovCode)) {
//			return JsonViewUtils.failure("소속된 지자체 정보가 없습니다.");
//		}
//
//		featuredService.updateFeaturedOrdering(featuredListParam);
//		return JsonViewUtils.success();
//
//	}

	/**
	 * 관리자 특집페이지 관리 등록
	 * @param model
	 * @param featured
	 * @return
	 */
	@GetMapping("/create")
	public String create(Model model, Featured featured) {
		if (!SecurityUtils.hasRole("ROLE_ADMIN_5")
				&& !SecurityUtils.hasRole("ROLE_ADMIN_6")
				&& !SecurityUtils.hasRole("ROLE_ADMIN_7")
				&& !SecurityUtils.hasRole("ROLE_ADMIN_8")) {
			return ViewUtils.redirect("/featured/list", "등록 불가능 합니다.");
		}

		if (getFeaturedTypeUri().equals("featured")) {
			featured.setFeaturedType("1");
		} else {
			featured.setFeaturedType("2");
		}

		model.addAttribute("mode", "create");
		model.addAttribute("featured", featured);
		model.addAttribute("featuredCheck", "0");
		model.addAttribute("featuredTypeUri", getFeaturedTypeUri());
		model.addAttribute("mainPart", MessageUtils.getMessage("MENU_12000")); /* 이벤트 */
		model.addAttribute("hours", ShopUtils.getHours());
//		model.addAttribute("categoryGroupList", categoriesTeamGroupService.getCategoriesGroupList());
//		model.addAttribute("pdExGubnCodes", hspCodeService.getHspSubCodeList("82"));			// 전용구분 코드

		List<Code> phone = CodeUtils.getCodeList("PHONE");
		List<Code> tel = CodeUtils.getCodeList("TEL");

		phone.addAll(tel);
		model.addAttribute("phone", phone);

		return ViewUtils.getView("/featured/form");

	}

	@PostMapping("/create")
	public String createAction(Model model, Featured featured, FeaturedItemParam featuredItemParam, HttpServletRequest request) {
		if (!SecurityUtils.hasRole("ROLE_ADMIN_5")
				&& !SecurityUtils.hasRole("ROLE_ADMIN_6")
				&& !SecurityUtils.hasRole("ROLE_ADMIN_7")
				&& !SecurityUtils.hasRole("ROLE_ADMIN_8")) {
			return ViewUtils.redirect("/featured/list", "등록 불가능 합니다.");
		}

		/*if (getFeaturedTypeUri().equals("featured")) {
			featured.setFeaturedType("1");
		} else {
			featured.setFeaturedType("2");
		}*/

//		featured.setFeaturedFile(imageFile);

		// 지자체 코드 추가
		if (ShopUtils.isSellerPage(request.getRequestURL().toString())) {
			featured.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.SELLER_USER));
		} else {
			featured.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));
		}

		if (StringUtils.isEmpty(featured.getLocgovCode())) {
			return ViewUtils.view("소속된 지자체 정보가 없습니다.");
		}
//		int featuredId =
				featuredService.insertFeatured(featured, featuredItemParam);

		return ViewUtils.redirect("/opmanager/" + getFeaturedTypeUri() + "/list", MessageUtils.getMessage("M00288")); 	// 등록되었습니다.

	}


	/**
	 * 관리자 특집페이지 관리 수정
	 * @param model
	 * @param featuredParam
	 * @return
	 */
	@GetMapping("/edit/{featuredId}")
	public String edit(Model model, FeaturedParam featuredParam) {
		Featured featured = featuredService.getFeaturedById(featuredParam);

		List<FeaturedItem> list = featuredService.getFeaturedItemListByParam(featuredParam);
		List<String> userGroupList = featuredService.getUserDefGroupById(featuredParam);

		String mainPart = featured.getFeaturedClass() == 1 ? MessageUtils.getMessage("기획전") : MessageUtils.getMessage("MENU_12000");		// 기획전 : 이벤트

		model.addAttribute("mode","edit");
		model.addAttribute("mainPart", mainPart);
		model.addAttribute("list",list);
		model.addAttribute("userGroupList", userGroupList);
		model.addAttribute("featured", featured);
		model.addAttribute("featuredCheck","1");
		model.addAttribute("featuredTypeUri", getFeaturedTypeUri());
		model.addAttribute("hours", ShopUtils.getHours());
//		model.addAttribute("categoryGroupList", categoriesTeamGroupService.getCategoriesGroupList());
//		model.addAttribute("pdExGubnCodes", hspCodeService.getHspSubCodeList("82"));			// 전용구분 코드

		List<Code> phone = CodeUtils.getCodeList("PHONE");		// 휴대폰 앞자리 코드
		List<Code> tel = CodeUtils.getCodeList("TEL");			// 지역번호 코드

		phone.addAll(tel);
		model.addAttribute("phone", phone);

 		return ViewUtils.getView("/featured/form");
	}

	@PostMapping("/edit/{featuredId}")
	public String editAction(Model model, Featured featured, FeaturedItemParam featuredItemParam) {
		if (!SecurityUtils.hasRole("ROLE_ADMIN_5")
				&& !SecurityUtils.hasRole("ROLE_ADMIN_6")
				&& !SecurityUtils.hasRole("ROLE_ADMIN_7")
				&& !SecurityUtils.hasRole("ROLE_ADMIN_8")) {
			return ViewUtils.redirect("/featured/list", "수정 불가능 합니다.");
		}

		if (getFeaturedTypeUri().equals("featured")) {
			featured.setFeaturedType("1");
		} else {
			featured.setFeaturedType("2");
		}

		if (ShopUtils.isSellerPage()) {
			featured.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.SELLER_USER));
		} else {
			featured.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));
		}

		if (StringUtils.isEmpty(featured.getLocgovCode())) {
			return ViewUtils.view("소속된 지자체 정보가 없습니다.");
		}

//		featured.setFeaturedFile(imageFile);
		featuredService.updateFeaturedById(featured, featuredItemParam);

//		return ViewUtils.redirect("/opmanager/" + getFeaturedTypeUri() + "/edit/"+featured.getFeaturedId(), MessageUtils.getMessage("M00289")); 	// 수정되었습니다.
		return ViewUtils.redirect("/opmanager/" + getFeaturedTypeUri() + "/list", MessageUtils.getMessage("M00289")); 	// 수정되었습니다.
	}

	/**
	 * 관리자 특집페이지 관리 선택 삭제
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@PostMapping("/checked-delete")
	public String checkedDelete(Model model, FeaturedParam featuredParam) {
		if (!SecurityUtils.isManager()) {
			return ViewUtils.redirect("/featured/list", "삭제 불가능 합니다.");
		}

		featuredService.deleteFeaturedsById(featuredParam);

		return ViewUtils.redirect("/opmanager/" + getFeaturedTypeUri() + "/list", MessageUtils.getMessage("M00205")); // 삭제되었습니다.

	}

	/**
	 * 특집페이지 이미지를 삭제한다.
	 * @param requestContext
	 * @param itemId
	 * @return
	 */
	@PostMapping("delete-image")
	public JsonView deleteImage(RequestContext requestContext, FeaturedParam featuredParam) {
		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}
		if (!SecurityUtils.isManager()) {
			return JsonViewUtils.failure("삭제 불가능 합니다.");
		}

		featuredService.deleteImageByItemId(featuredParam);

		return JsonViewUtils.success();
	}

	/**
	 * 관리자 특집페이지 관리 등록
	 * @param searchParam
	 * @param model
	 * @return
	 */
//	@GetMapping("/banner-create")
//	public String bannerCreate(Model model, Featured featured, CategoriesEditParam categoriesEditParam) {
//		categoriesEditParam.setCode("featured");
//		categoriesEditParam.setEditPosition("promotion");
//		categoriesEditParam.setEditKind("1");
//		categoriesEditParam.setType("1");
//
//		model.addAttribute("categoriesEditParam",categoriesEditParam);
//		model.addAttribute("categoryPromotionList",categoriesEditService.getCategoryPromotionListByParam(categoriesEditParam));
//
//
//		return ViewUtils.view();
//
//	}

	/**
	 * 관리자 특집페이지 관리 등록
	 * @param searchParam
	 * @param model
	 * @return
	 */
//	@PostMapping("/banner-create")
//	public String bannerCreateAction(Model model, Featured featured, CategoriesEdit categoriesEdit) {
//
//		categoriesEditService.insertCategoryEditFiles(categoriesEdit);
//
//		return ViewUtils.redirect("/opmanager/" + getFeaturedTypeUri() + "/banner-create", MessageUtils.getMessage("M00288")); 	// 등록되었습니다.
//
//	}

//	@PostMapping("/url-search")
//	public JsonView codeCheck(Model model, @RequestParam("featuredUrl") String featuredUrl,
//			FeaturedParam featuredParam) {
//
//		if (getFeaturedTypeUri().equals("featured")) {
//			featuredParam.setFeaturedType("1");
//		} else {
//			featuredParam.setFeaturedType("2");
//		}
//
//
//		// url 조회 (pc/모바일 구분없음)
//		int count = featuredService.getFeaturedCountByParam(featuredParam);
//
//
//
//		/* url 조회 (pc/모바일 구분없음)
//		int count = featuredService.getFeaturedCountByUrl(featuredUrl);
//		*/
//		if(count > 0){
//			return JsonViewUtils.failure(MessageUtils.getMessage("M01486"));	// 코드 또는 URL 존재 합니다.
//		}
//
//		return JsonViewUtils.success();
//
//	}

	private String getFeaturedTypeUri() {
		if (RequestContextUtils.getRequestUri().indexOf("/opmanager/featured-mobile") > -1) {
			return "featured-mobile";
		}
		return "featured";
	}

//	@GetMapping("/manage-event-reply")
//	@RequestProperty(layout = "base")
//	public String manageEventReply(int featuredId, Model model, @ModelAttribute("featuredReplyParam") FeaturedReplyParam featuredReplyParam) {
//
//		int replyCount = featuredService.getFeaturedReplyCountByParam(featuredReplyParam);
//
//		Pagination pagination = Pagination.getInstance(replyCount);
//
//		featuredReplyParam.setPagination(pagination);
//
//		List<FeaturedReply> replyList = featuredService.getFeaturedReplyByParam(featuredReplyParam);
//
//		model.addAttribute("pagination", pagination);
//		model.addAttribute("replyList", replyList);
//
//		return "view:/featured/reply-list";
//	}

//	@PostMapping("/display-reply")
//	public JsonView updateDisplayReply(FeaturedReply featuredReply) {
//
//		if (featuredReply.getIds().length > 0) {
//			featuredReply.setUpdatedBy(UserUtils.getManagerId());
//			featuredService.updateDisplayReply(featuredReply);
//		}
//
//		return JsonViewUtils.success();
//	}

//	@PostMapping("/update-event-code/{id}")
//	public JsonView updateEventCode(@PathVariable("id")int id) {
//
//		String errorMessage = "이벤트 코드 생성에 실패 했습니다.";
//
//		try {
//
//			if (featuredService.updateEventCode(id) > 0) {
//				return JsonViewUtils.success();
//			} else {
//				return JsonViewUtils.failure(errorMessage);
//			}
//
//		} catch (RuntimeException ignore) {
////			log.error("updateEventCode {}", ignore.getMessage(), ignore);
//			log.error("updateEventCode {}", getClass().getName() + " :: updateEventCode RuntimeException =============");
//			return JsonViewUtils.failure(errorMessage);
//		}
//	}

	private void setLocgovCode(FeaturedParam featuredParam) {
		if (featuredParam == null) {
			featuredParam = new FeaturedParam();
		}
		if (SecurityUtils.hasRole("ROLE_ADMIN_5")
				|| SecurityUtils.hasRole("ROLE_ADMIN_6")
				|| SecurityUtils.hasRole("ROLE_ADMIN_7")
				|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
			if (!StringUtils.isEmpty(locgovCode)) {
				featuredParam.setLocgovCode(locgovCode);
			} else {
				featuredParam.setLocgovCode("00000");
			}
		} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
					|| SecurityUtils.hasRole("ROLE_ADMIN_2")
					|| SecurityUtils.hasRole("ROLE_ADMIN_3")
					|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

		} else {
			featuredParam.setLocgovCode("00000");
		}
	}

}