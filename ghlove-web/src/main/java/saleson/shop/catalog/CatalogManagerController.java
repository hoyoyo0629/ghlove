package saleson.shop.catalog;
//import saleson.shop.user.LocgovService;
import saleson.shop.user.PersonInChargeService;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import saleson.common.utils.UserUtils;
import saleson.shop.catalog.domain.CatalogCardNews;
import saleson.shop.catalog.domain.CatalogContentMng;
import saleson.shop.catalog.domain.CatalogMng;
import saleson.shop.catalog.domain.LocgovFavItemMng;
import saleson.shop.catalog.support.CatalogCardNewsParam;
import saleson.shop.catalog.support.CatalogContentMngParam;
import saleson.shop.catalog.support.CatalogMngParam;
import saleson.shop.catalog.support.LocgovFavItemMngParam;
import saleson.shop.code.CodeService;
import saleson.shop.code.support.CodeParam;
import saleson.shop.designateddonation.domain.DesignatedDonationNotice;
import saleson.shop.give.givestate.GiveStateService;

@Controller
@RequestMapping("/opmanager/catalog")
@RequestProperty(title="소식지관리", layout="default")
public class CatalogManagerController {

	private static final Logger log = LoggerFactory.getLogger(CatalogManagerController.class);

//	@Autowired
//	private LocgovService locgovService;	// 지자체 관리

	@Autowired
	private CatalogMngService catalogMngService;

	@Autowired
	private PersonInChargeService personInChargeService;

	@Autowired
	private GiveStateService giveStateService;

	@Autowired
    CodeService codeService;

	/**
	 * 소식지 목록 조회
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/catalogMng/list")
	public String selectCatalogMngList(Model model, RequestContext requestContext, CatalogMngParam catalogMngParam) {

		int count = catalogMngService.selectCatalogMngListCnt(catalogMngParam);
		int catalogYear = catalogMngParam.getCatalogYear();

		List<CatalogMng> catalogMngList = catalogMngService.selectCatalogMngList(catalogMngParam);
		Pagination pagination = Pagination.getInstance(count, catalogMngParam.getItemsPerPage());

		catalogMngParam.setPagination(pagination);
		catalogMngParam.setCatalogYear(catalogYear);

		model.addAttribute("catalogMngParam", catalogMngParam);
		model.addAttribute("catalogMngList", catalogMngList);
		model.addAttribute("catalogYear", catalogYear);
		model.addAttribute("catalogYearList", catalogMngService.selectCatalogMngYearList());
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		return ViewUtils.view();
	}

	/**
	 * 소식지 목록 조회
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/catalogMng/list")
	public String searchSelectCatalogMngList(Model model, RequestContext requestContext, CatalogMngParam catalogMngParam) {

		int count = catalogMngService.selectCatalogMngListCnt(catalogMngParam);
		int catalogYear = catalogMngParam.getCatalogYear();

		List<CatalogMng> catalogMngList = catalogMngService.selectCatalogMngList(catalogMngParam);
		Pagination pagination = Pagination.getInstance(count, catalogMngParam.getItemsPerPage());

		catalogMngParam.setPagination(pagination);
		catalogMngParam.setCatalogYear(catalogYear);

		model.addAttribute("catalogMngParam", catalogMngParam);
		model.addAttribute("catalogMngList", catalogMngList);
		model.addAttribute("catalogYear", catalogYear);
		model.addAttribute("catalogYearList", catalogMngService.selectCatalogMngYearList());
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		return ViewUtils.view();
	}
	
	/**
	 * 소식지 목록 - 등록 화면
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/catalogMng/create")
    @RequestProperty(layout="base", title="소식지 등록")
	public String popCatalogMng(Model model, CatalogMngParam catalogMngParam) {

		model.addAttribute("catalogYearList", catalogMngService.selectCatalogMngYearList());

		return ViewUtils.getView("/catalog/catalogMng/form");
	}

	/**
	 * 소식지 목록 - 등록
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/catalogMng/create")
	public JsonView insertCatalogMng(Model model, @RequestBody CatalogMng catalogMng) {
	    // session에서 id 가져오는 함수
	    long userId = UserUtils.getUser().getUserId();
	    catalogMng.setUserId(userId);

	    // catalogYear와 catalogNo의 기본값을 빈 문자열로 설정
	    if (catalogMng.getCatalogYearStr() == null) {
	        catalogMng.setCatalogYearStr("");
	    }
	    if (catalogMng.getCatalogNoStr() == null) {
	        catalogMng.setCatalogNoStr("");
	    }

	    // 중복 체크
	    boolean isDuplicate = catalogMngService.isDuplicateCatalog(catalogMng);

	    if (isDuplicate) {
	    	// 중복인 경우
	    	return JsonViewUtils.success("failure");
	    } else {
	    	// 중복이 아닌 경우
	        catalogMngService.insertCatalogMng(catalogMng);
	        return JsonViewUtils.success("success");
	    }
	}

	/**
	 * 소식지 목록 - 수정 화면
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/catalogMng/update")
	@RequestProperty(layout="base", title="소식지 수정")
	public String selectCatalogMngDetail(Model model, CatalogMngParam catalogMngParam, @RequestParam("rowNumber") int rowNumber) {

		model.addAttribute("catalogYearList", catalogMngService.selectCatalogMngYearList());
		model.addAttribute("catalogYear", catalogMngParam.getCatalogYearStr());
		model.addAttribute("catalogNo", catalogMngParam.getCatalogNoStr());
		model.addAttribute("displayYn", catalogMngParam.getDisplayYn());
		model.addAttribute("rowNumber", rowNumber);

		return ViewUtils.getView("/catalog/catalogMng/form");
	}

	/**
	 * 소식지 목록 - 수정
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/catalogMng/update")
	public String updateCatalogMng(Model model, RequestContext requestContext, CatalogMngParam catalogMngParam, @RequestParam("rowNumber") int rowNumber) {

		// session에서 id 가져오는 함수
		long userId = UserUtils.getUser().getUserId();

		catalogMngParam.setUserId(userId);

        catalogMngService.updateCatalogMng(catalogMngParam);

        String message = MessageUtils.getMessage("M00406"); // 저장되었습니다
		String javascript = "window.close();";
		String url = "/opmanager/catalog/catalogMng/list";

        return ViewUtils.redirect(url, message, javascript);
	}

	/**
	 * 인기답례품 발간 호 리스트 조회
	 *
	 * @param listParam
	 * @param model
	 * @return
	 */
	@PostMapping("/locgovFavItem/catalogMngNoList")
	public @ResponseBody JsonView selectCatalogMngNoList(@RequestParam(name="code", defaultValue = "0") String code) {
		return JsonViewUtils.success(catalogMngService.selectCatalogMngNoList(code));
	}

	/**
	 * 인기답례품 목록 조회
	 *
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/locgovFavItem/list")
	public String selectLocgovFavItemList(Model model, RequestContext requestContext,
			LocgovFavItemMngParam locgovFavItemMngParam) {
		LocalDate now = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		if (!StringUtils.hasLength(locgovFavItemMngParam.getSearchStartDate()) && !StringUtils.hasLength(locgovFavItemMngParam.getSearchEndDate())) {
			locgovFavItemMngParam.setSearchStartDate(format.format(now));
			locgovFavItemMngParam.setSearchEndDate(format.format(now));
		} else if (!StringUtils.hasLength(locgovFavItemMngParam.getSearchStartDate())) {
			locgovFavItemMngParam.setSearchStartDate(locgovFavItemMngParam.getSearchEndDate());
		} else if (!StringUtils.hasLength(locgovFavItemMngParam.getSearchEndDate())) {
			locgovFavItemMngParam.setSearchEndDate(locgovFavItemMngParam.getSearchStartDate());
		}

		List<LocgovFavItemMng> locgovFavItemMngList = catalogMngService.selectLocgovFavItemMngList(locgovFavItemMngParam);
		List<CatalogMng> catalogMngYearList = catalogMngService.selectCatalogMngYearList();

		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());

//			ROLE_ADMIN_1	시스템주담당자
//			ROLE_ADMIN_2	시스템부담당자
//			ROLE_ADMIN_3	행안부주담당자
//			ROLE_ADMIN_4	행안부부담당자
//			ROLE_ADMIN_5	지자체주담당자
//			ROLE_ADMIN_6	지자체부담당자
			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(requestContext.getUser().getUserId());
			String locgovCd = locgovObj.get("LOCGOV_CODE").toString();
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCd);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());
		}

		model.addAttribute("yyyy", catalogMngYearList);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("locgovFavItemMngList", locgovFavItemMngList);
		model.addAttribute("locgovFavItemMngParam", locgovFavItemMngParam);
		model.addAttribute("pagination", locgovFavItemMngParam.getPagination());
		return ViewUtils.view();
	}

	/**
	 * 인기답례품 목록 조회
	 *
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/locgovFavItem/list")
	public String searchSelectLocgovFavItemList(Model model, RequestContext requestContext,
			LocgovFavItemMngParam locgovFavItemMngParam) {
		LocalDate now = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		if (!StringUtils.hasLength(locgovFavItemMngParam.getSearchStartDate()) && !StringUtils.hasLength(locgovFavItemMngParam.getSearchEndDate())) {
			locgovFavItemMngParam.setSearchStartDate(format.format(now));
			locgovFavItemMngParam.setSearchEndDate(format.format(now));
		} else if (!StringUtils.hasLength(locgovFavItemMngParam.getSearchStartDate())) {
			locgovFavItemMngParam.setSearchStartDate(locgovFavItemMngParam.getSearchEndDate());
		} else if (!StringUtils.hasLength(locgovFavItemMngParam.getSearchEndDate())) {
			locgovFavItemMngParam.setSearchEndDate(locgovFavItemMngParam.getSearchStartDate());
		}

		List<LocgovFavItemMng> locgovFavItemMngList = catalogMngService.selectLocgovFavItemMngList(locgovFavItemMngParam);
		List<CatalogMng> catalogMngYearList = catalogMngService.selectCatalogMngYearList();

		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());

//			ROLE_ADMIN_1	시스템주담당자
//			ROLE_ADMIN_2	시스템부담당자
//			ROLE_ADMIN_3	행안부주담당자
//			ROLE_ADMIN_4	행안부부담당자
//			ROLE_ADMIN_5	지자체주담당자
//			ROLE_ADMIN_6	지자체부담당자
			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(requestContext.getUser().getUserId());
			String locgovCd = locgovObj.get("LOCGOV_CODE").toString();
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCd);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());
		}

		model.addAttribute("yyyy", catalogMngYearList);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("locgovFavItemMngList", locgovFavItemMngList);
		model.addAttribute("locgovFavItemMngParam", locgovFavItemMngParam);
		model.addAttribute("pagination", locgovFavItemMngParam.getPagination());
		return ViewUtils.view();
	}
	
	/**
	 * 인기답례품관리 수정
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/locgovFavItem/form/{catalogYearNo}/{locgovCode}/{locgovNm}")
	public String selectlocgovFavItemMngInfo(@PathVariable("catalogYearNo") String catalogYearNo
			, @PathVariable("locgovCode") String locgovCode
			, @PathVariable("locgovNm") String locgovNm
			, RequestContext requestContext, Model model, LocgovFavItemMng locgovFavItemMng) {
		LocgovFavItemMngParam locgovFavItemMngParam = new LocgovFavItemMngParam();
		List<CatalogMng> catalogMngYearList = catalogMngService.selectCatalogMngYearList();

		locgovFavItemMngParam.setCatalogYearNo(catalogYearNo);
		locgovFavItemMngParam.setLocgovCode(locgovCode);
		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());

//			ROLE_ADMIN_1	시스템주담당자
//			ROLE_ADMIN_2	시스템부담당자
//			ROLE_ADMIN_3	행안부주담당자
//			ROLE_ADMIN_4	행안부부담당자
//			ROLE_ADMIN_5	지자체주담당자
//			ROLE_ADMIN_6	지자체부담당자
			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(requestContext.getUser().getUserId());
			String locgovCd = locgovObj.get("LOCGOV_CODE").toString();
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCd);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());
			model.addAttribute("locgovCode", locgovObj.get("LOCGOV_CODE").toString());
		} else {
			model.addAttribute("locgovNm",locgovNm);

		}
		locgovFavItemMng = catalogMngService.selectLocgovFavItemMng(locgovFavItemMngParam);
		model.addAttribute("yyyy", catalogMngYearList);
		model.addAttribute("adminRole", personInChargeService.getLoginUserAdminAuthority());
		model.addAttribute("locgovFavItemMng", locgovFavItemMng);
		return ViewUtils.getView("/catalog/locgovFavItem/form");
	}


	 /**
	 * 인기답례품관리 수정 처리
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/locgovFavItem/form/{catalogYearNo}/{locgovCode}/{locgovNm}")
	public String updatelocgovFavItemMngInfo(
				RequestContext requestContext
					, @PathVariable("catalogYearNo") String catalogYearNo
					, @PathVariable("locgovCode") String locgovCode
					, @PathVariable("locgovNm") String locgovNm
				  	, LocgovFavItemMng locgovFavItemMng)
				  	 {
		LocgovFavItemMngParam locgovFavItemMngParam = new LocgovFavItemMngParam();
		locgovFavItemMngParam.setCatalogYearNo(catalogYearNo);

		locgovFavItemMng.setCatalogNo(locgovFavItemMngParam.getCatalogNo());
		locgovFavItemMng.setCatalogYear(locgovFavItemMngParam.getCatalogYear());
		locgovFavItemMng.setLocgovCode(locgovCode);
		locgovFavItemMng.setUserId(requestContext.getUser().getUserId());
		try {
			catalogMngService.updateLocgovFavItemMng(locgovFavItemMng);
		} catch (OpRuntimeException e) {
			throw new UserException(e.getErrorMessage());
		}
		if(locgovFavItemMng.getDeleteYn() != null && !locgovFavItemMng.getDeleteYn().equals("")) {
			if(locgovFavItemMng.getDeleteYn().equals("Y")) {
				return ViewUtils.redirect("/opmanager/catalog/locgovFavItem/list");// 삭제되었습니다.
			} else {
				return ViewUtils.redirect("/opmanager/catalog/locgovFavItem/list", MessageUtils.getMessage("M00289"));// 수정되었습니다.
			}
		} else {
			return ViewUtils.redirect("/opmanager/catalog/locgovFavItem/list", MessageUtils.getMessage("M00289"));// 수정되었습니다.
		}
	}


	/**
	 * 인기답례품 등록
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/locgovFavItem/form")
	public String makeDesignatedDonationInfo(LocgovFavItemMng locgovFavItemMng, Model model, RequestContext requestContext) {
		List<CatalogMng> catalogMngYearList = catalogMngService.selectCatalogMngYearList();
		LocgovFavItemMngParam locgovFavItemMngParam = new LocgovFavItemMngParam();
		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());

//			ROLE_ADMIN_1	시스템주담당자
//			ROLE_ADMIN_2	시스템부담당자
//			ROLE_ADMIN_3	행안부주담당자
//			ROLE_ADMIN_4	행안부부담당자
//			ROLE_ADMIN_5	지자체주담당자
//			ROLE_ADMIN_6	지자체부담당자
			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(requestContext.getUser().getUserId());
			String locgovCd = locgovObj.get("LOCGOV_CODE").toString();
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovCd);
			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());
			model.addAttribute("locgovCode", locgovCd);
			locgovFavItemMngParam.setLocgovCode(locgovObj.get("LOCGOV_CODE").toString());
		}
		model.addAttribute("adminRole", personInChargeService.getLoginUserAdminAuthority());
		model.addAttribute("yyyy", catalogMngYearList);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		return ViewUtils.view();
	}

	/**
	 * 인기답례품 등록 처리
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/locgovFavItem/form")
	public String insertDesignatedDonationInfo(RequestContext requestContext
			,LocgovFavItemMng locgovFavItemMng ) {
		catalogMngService.insertLocgovFavItemMng(locgovFavItemMng);

		return ViewUtils.redirect("/opmanager/catalog/locgovFavItem/list", MessageUtils.getMessage("M00288"));// 등록되었습니다.
	}

	/**
	 * 인기답례품관리 다중삭제 처리
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/locgovFavItem/form/delete-list")
	public JsonView deleteListData(RequestContext requestContext, ListParam listParam) {
		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}
		catalogMngService.deleteListLocgovFavItemMng(listParam);
		return JsonViewUtils.success();
	}

	@GetMapping("/card-news/list")
	public String selectCardNewsList(Model model, CatalogCardNewsParam param) {
		model.addAttribute("list", catalogMngService.selectCatalogCardNewsList(param));
		model.addAttribute("cardNewsParam", param);
		return ViewUtils.getView("/catalog/cardNews/list");
	}

	@PostMapping("/card-news/list")
	public String searchSelectCardNewsList(Model model, CatalogCardNewsParam param) {
		model.addAttribute("list", catalogMngService.selectCatalogCardNewsList(param));
		model.addAttribute("cardNewsParam", param);
		return ViewUtils.getView("/catalog/cardNews/list");
	}

	@GetMapping("/card-news/form")
	public String newCardNews(Model model) {
		model.addAttribute("cardNews", new CatalogCardNews());
		model.addAttribute("regCatalogYearList", catalogMngService.selectCatalogMngYearList());
		return ViewUtils.getView("/catalog/cardNews/form");
	}


	@GetMapping("/card-news/form/{cardNewsId}")
	public String selectCardNews(Model model, CatalogCardNewsParam param, @PathVariable(name = "cardNewsId") long cardNewsId) {
		param.setCardNewsId(cardNewsId);
		model.addAttribute("cardNews", catalogMngService.selectCatalogCardNews(param));
		model.addAttribute("regCatalogYearList", catalogMngService.selectCatalogMngYearList());
		return ViewUtils.getView("/catalog/cardNews/form");
	}


	@PostMapping("/card-news/form/save")
	public String saveCardNews(Model model, CatalogCardNews catalogCardNews) {
		String msg = "";
		long cardNewsId = catalogCardNews.getCardNewsId();

		if (catalogMngService.saveCatalogCardNews(catalogCardNews) == 1) {
			if (cardNewsId > 0) {
				msg = "수정되었습니다.";
			} else {
				msg = "등록되었습니다.";
			}
		} else {
			if (cardNewsId > 0) {
				msg = "수정에 실패했습니다.";
			} else {
				msg = "등록에 실패했습니다.";
			}
		}

		return ViewUtils.redirect("/opmanager/catalog/card-news/list", msg);
	}

	/**
	 * 목록데이터 상태변경
	 * @param flag
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
	@PostMapping("/card-news/list/update-label/{flag}")
	public JsonView updateCardNewsListDataByLabel(@PathVariable("flag") String flag, RequestContext requestContext, CatalogCardNews catalogCardNews) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		catalogCardNews.setDeleteYn(flag);
		catalogMngService.updateCardNewsListDataByLabel(catalogCardNews);

		return JsonViewUtils.success();
	}

	// 2024-04-25 컨텐츠 관리
	// 2024-04-25 컨텐츠 관리
	// 2024-04-25 컨텐츠 관리
	// 2024-04-25 컨텐츠 관리
	// 2024-04-25 컨텐츠 관리
	/**
	 * 컨텐츠 발간 호 리스트 조회
	 *
	 * @param listParam
	 * @param model
	 * @return
	 */
	/*
	 * @PostMapping("/list") public @ResponseBody JsonView
	 * selectCatalogContentMngNoList(@RequestParam(name="code", defaultValue = "0")
	 * String code, Model model) { return
	 * JsonViewUtils.success(catalogMngService.selectCatalogMngNoList(code)); }
	 */

	/**
	 * 컨텐츠 목록 조회
	 *
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/content/list")
	public String selectCatalogContentMngList(Model model, RequestContext requestContext,
			CatalogContentMngParam catalogContentMngParam) {
		List<CatalogContentMng> catalogContentList = catalogMngService.selectCatalogContentMngList(catalogContentMngParam);

		LocalDate now = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");

		model.addAttribute("catalogYearList", catalogMngService.selectCatalogMngYearList());
		model.addAttribute("catalogContentList", catalogContentList);

		model.addAttribute("catalogContentMngParam", catalogContentMngParam);
		model.addAttribute("pagination", catalogContentMngParam.getPagination());
		return ViewUtils.view();
	}

	/**
	 * 컨텐츠 목록 조회
	 *
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/content/list")
	public String searchSelectCatalogContentMngList(Model model, RequestContext requestContext,
			CatalogContentMngParam catalogContentMngParam) {
		List<CatalogContentMng> catalogContentList = catalogMngService.selectCatalogContentMngList(catalogContentMngParam);

		LocalDate now = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");

		model.addAttribute("catalogYearList", catalogMngService.selectCatalogMngYearList());
		model.addAttribute("catalogContentList", catalogContentList);

		model.addAttribute("catalogContentMngParam", catalogContentMngParam);
		model.addAttribute("pagination", catalogContentMngParam.getPagination());
		return ViewUtils.view();
	}
	
	/**
	 * 컨텐츠 상세 조회
	 *
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/content/form/{catalogContentId}")
	public String selectCatalogContentMngInfo(@PathVariable("catalogContentId") long catalogContentId
			, RequestContext requestContext, Model model, CatalogContentMng catalogContent) {
		CatalogContentMngParam catalogContentMngParam = new CatalogContentMngParam();

		catalogContentMngParam.setCatalogContentId(catalogContentId);
		catalogContent = catalogMngService.selectCatalogContentMngDetail(catalogContentMngParam);

		// 구분 공통코드
		CodeParam codeParam = new CodeParam();
		CodeParam subCodeParam = new CodeParam();
		codeParam.setCodeType("CONTENT_TYPE");
		model.addAttribute("contentTypeList", codeService.getCodeChildList(codeParam));
		subCodeParam.setCodeType("CONTENT_SUB_TYPE");
		model.addAttribute("contentSubTypeList", codeService.getCodeChildList(subCodeParam));

		model.addAttribute("catalogYearList", catalogMngService.selectCatalogMngYearList());
		model.addAttribute("catalogContent", catalogContent);

		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));

		return ViewUtils.getView("/catalog/content/form");
	}

	/**
	 * 컨텐츠 등록 화면
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/content/form")
	public String makeCatalogContentMngInfo(CatalogContentMng catalogContent, Model model, RequestContext requestContext) {
		List<CatalogMng> catalogMngYearList = catalogMngService.selectCatalogMngYearList();

		model.addAttribute("adminRole", personInChargeService.getLoginUserAdminAuthority());
		model.addAttribute("yyyy", catalogMngYearList);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("catalogContent", new CatalogContentMng());
		// 구분 공통코드
		CodeParam codeParam = new CodeParam();
		CodeParam subCodeParam = new CodeParam();
		codeParam.setCodeType("CONTENT_TYPE");
		model.addAttribute("contentTypeList", codeService.getCodeChildList(codeParam));
		subCodeParam.setCodeType("CONTENT_SUB_TYPE");
		model.addAttribute("contentSubTypeList", codeService.getCodeChildList(subCodeParam));
		model.addAttribute("catalogYearList", catalogMngService.selectCatalogMngYearList());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));

		return ViewUtils.getView("/catalog/content/form");
	}

	/**
	 * 컨텐츠 등록,수정 처리
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/content/form/save")
	public String saveCatalogContent(Model model, CatalogContentMng catalogContent,
			@RequestParam(value="prjImageFiles[]", required=false) MultipartFile[] prjImageFiles) {
		String msg = "";
		long catalogContentId = catalogContent.getCatalogContentId();
		try {
			if (catalogMngService.saveCatalogContent(catalogContent, prjImageFiles) == 1) {
				if (catalogContentId > 0) {
					msg = "수정되었습니다.";
				} else {
					msg = "등록되었습니다.";
				}
			} else {
				if (catalogContentId > 0) {
					msg = "수정에 실패했습니다.";
				} else {
					msg = "등록에 실패했습니다.";
				}
			}	
		} catch (IOException e) {
			log.error(getClass().getName() +  " saveCatalogContent error :: ", e);
			throw new UserException("저장에 실패했습니다.");
		}

		return ViewUtils.redirect("/opmanager/catalog/content/list", msg);
	}

	/**
	 * 구분(세부) 리스트 조회
	 *
	 * @param listParam
	 * @param model
	 * @return
	 */
	@PostMapping("/content/contentSubTypeList")
	public @ResponseBody JsonView selectContentSubTypeList(@RequestParam(name="code") String code) {
		// 구분 공통코드
				CodeParam codeParam = new CodeParam();
				codeParam.setCodeType("CONTENT_SUB_TYPE");
				codeParam.setUpId(code);
		return JsonViewUtils.success(codeService.getCodeChildList(codeParam));
	}
}