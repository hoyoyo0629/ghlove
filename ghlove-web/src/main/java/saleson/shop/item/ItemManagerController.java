package saleson.shop.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.enumeration.JavaScript;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.repository.CodeService;
import com.onlinepowers.framework.repository.support.EarlyLoadingCodeInfoRepository;
import com.onlinepowers.framework.repository.support.EarlyLoadingRepositoryEvent;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import saleson.api.common.ApiResponseEntity;
import saleson.common.Const;
import saleson.common.enumeration.IdType;
import saleson.common.file.ExcelDownloadView;
import saleson.common.userauth.UserAuthService;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ItemUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.seller.main.domain.SellerEncryptor;
import saleson.seller.main.domain.SellerUser;
import saleson.seller.main.support.SellerParam;
import saleson.shop.brand.BrandService;
import saleson.shop.brand.support.BrandParam;
import saleson.shop.categories.domain.Breadcrumb;
import saleson.shop.categoriesfilter.CategoriesFilterService;
import saleson.shop.categoriesteamgroup.CategoriesTeamGroupService;
import saleson.shop.categoriesteamgroup.domain.CategoriesTeam;
import saleson.shop.deliverycompany.DeliveryCompanyService;
import saleson.shop.deliverycompany.domain.DeliveryCompany;
import saleson.shop.deliverycompany.support.DeliveryCompanyParam;
import saleson.shop.give.givestate.GiveStateService;
import saleson.shop.item.domain.*;
import saleson.shop.item.support.*;
import saleson.shop.point.PointService;
import saleson.shop.restocknotice.RestockNoticeService;
import saleson.shop.restocknotice.domain.RestockNotice;
import saleson.shop.shipment.ShipmentService;
import saleson.shop.shipment.domain.Shipment;
import saleson.shop.shipment.support.ShipmentParam;
import saleson.shop.shipmentreturn.ShipmentReturnService;
import saleson.shop.shipmentreturn.domain.ShipmentReturn;
import saleson.shop.shipmentreturn.support.ShipmentReturnParam;
import saleson.shop.user.LocgovService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Controller
@RequestMapping("/opmanager/item")
@RequestProperty(title="상품관리", layout="default", template="opmanager")
public class ItemManagerController {
	private static final Logger log = LoggerFactory.getLogger(ItemManagerController.class);

	@Autowired
	private EarlyLoadingCodeInfoRepository codeInfoRepository;

	@Autowired
	private LocgovService locgovService;	// 지자체 관리

	@Autowired
	private ItemService itemService;

	@Autowired
	private PointService pointService;

	@Autowired
	private CategoriesTeamGroupService categoriesTeamGroupService;

	@Autowired
	private DeliveryCompanyService deliveryCompanyService;

	@Autowired
	private SellerService sellerService;	// 판매자 정보.

	@Autowired
	private ShipmentService shipmentService;	// 출고지/배송비.

	@Autowired
	private ShipmentReturnService shipmentReturnService;	// 반송지.

	@Autowired
	private CodeService codeService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private RestockNoticeService restockNoticeService;

	@Autowired
	CategoriesFilterService categoriesFilterService;

	@Autowired
	private GiveStateService giveStateService;

	@Autowired
	Environment environment;

	@Autowired
	UserAuthService userAuthService;

	@Autowired
	private SellerEncryptor sellerEncryptor;

    private final ExecutorService threadService = Executors.newFixedThreadPool(5);

	@RequestProperty(layout="base")
	@GetMapping("make-shopping-how")
	public String makeShoppingHow(Model model, @RequestParam(value="r", required=false,defaultValue="0") String reMake) {

		String fileName = environment.getProperty("shop.api.text.save.folder") + "/shopping_how.txt";
		if ("1".equals(reMake)) {
			itemService.makeShoppingHowFile(fileName);
		}

		model.addAttribute("fileName", fileName);
		return ViewUtils.view();
	}

	/**
	 * 상품 목록 페이지
	 * @param itemParam
	 * @param model
	 * @return
	 */
	@GetMapping("list")
	public String list(ItemParam itemParam, Model model) {
		// 승인완료 / 판매 종료건만.
		//if(!itemParam.getSoldOut().equals("")) itemParam.setConditionType("ITEM_LIST_APPROVAL");
		//if(itemParam.getSoldOut().equals("pending")) itemParam.setConditionType("ITEM_LIST_PENDING_APPROVAL");

		// 승인완료 / 판매 종료건만.
		// itemParam.setConditionType("ITEM_LIST_APPROVAL");
		String today = DateUtils.getToday("yyyyMMdd");


		itemParam.setSearchStartDate(StringUtils.defaultIfEmpty(itemParam.getSearchStartDate(), today));
		itemParam.setSearchEndDate(StringUtils.defaultIfEmpty(itemParam.getSearchEndDate(), today));


		itemParam.setConditionType("ITEM_LIST_MIG");			// 이관 상품까지 조회되도록 수정
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드


		invokeItemList(itemParam, model);

		return "view";
	}

	@GetMapping("/seller/list")
	public String listPending(ItemParam itemParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		itemParam.setSearchStartDate(StringUtils.defaultIfEmpty(itemParam.getSearchStartDate(), today));
		itemParam.setSearchEndDate(StringUtils.defaultIfEmpty(itemParam.getSearchEndDate(), today));

		// 승인완료 / 판매 종료건만.
		itemParam.setConditionType("ITEM_LIST_PENDING_APPROVAL");
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());	// 권한

		invokeItemList(itemParam, model);

		return "view";
	}

	@GetMapping("/representative-item/list")
	public String representativeItemlist(ItemParam itemParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		itemParam.setSearchStartDate(StringUtils.defaultIfEmpty(itemParam.getSearchStartDate(), today));
		itemParam.setSearchEndDate(StringUtils.defaultIfEmpty(itemParam.getSearchEndDate(), today));

		// 승인완료 / 판매 종료건만.
		itemParam.setConditionType("ITEM_LIST_APPROVAL");
		// 대표상품여부
		itemParam.setRepresentativeItemYn("Y");

		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		invokeItemList(itemParam, model);

		return "view";
	}

	@GetMapping("/simple/list")
	public String listSimple(ItemParam itemParam, Model model) {

		// 승인완료 / 판매 종료건만.
		itemParam.setConditionType("ITEM_SIMPLE_LIST");

		invokeItemList(itemParam, model);
		return "view";
	}

	/**
	 * 상품 리스트 처리.
	 * @param itemParam
	 * @param model
	 */
	protected void invokeItemList(ItemParam itemParam, Model model) {

		// 정렬조건은 CategoryId가 있는 경우에만 허용.
		if (itemParam.getOrderBy() != null && itemParam.getOrderBy().equals("ORDERING")
			&& (itemParam.getCategoryId() == null || itemParam.getCategoryId().equals(""))) {
			itemParam.setOrderBy("");
			itemParam.setSort("DESC");
		}

		itemParam.setItemDataType("1");

		//itemParam.setDataStatusCode("1");
		// 관리자인 경우
		if (UserUtils.isManagerLogin()) {
			// 지자체관리자일 경우 지자체코드 필요
			itemParam.setLocgov(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		// 판매자인 경우
		if (ShopUtils.isSellerPage() && SellerUtils.isSellerLogin()) {
			itemParam.setSellerId(SellerUtils.getSellerId());
		}

		//2017-01-18 손준의 로그인 유저가 MD일때 담당MD명 검색 초기값을 자신의 유저명으로 설정
		if (!UserUtils.isSupervisor() && UserUtils.isMd() && itemParam.getMdName() == null) {
			itemParam.setMdName(UserUtils.getUser().getUserName());
		}

		Pagination pagination = Pagination.getInstance(itemService.getItemCount(itemParam));

		itemParam.setPagination(pagination);

		List<Item> items = itemService.getItemList(itemParam);

		// 소속팀 (무소속 포함)
		List<CategoriesTeam> categoryTeamList = getCategoryTeamList(categoriesTeamGroupService.getCategoriesTeamGroupList());

		model.addAttribute("brandList", brandService.getBrandList(new BrandParam()));
		model.addAttribute("list", items);
		model.addAttribute("categoryTeamList", categoryTeamList);		// 카테고리 팀 목록 (무소속 포함)
		model.addAttribute("pagination", pagination);
		model.addAttribute("itemParam", itemParam);
		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam()));

		ObjectMapper objectMapper = new ObjectMapper();	// jsp script에서 LIST 사용을 위해 JSON형태로 변환 필요

		if (SecurityUtils.hasRole("ROLE_ADMIN_5")
				|| SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			model.addAttribute("lclgvRprsGdsList", itemService.getLclgvRprsGdsList(UserUtils.getUser().getUserId()));
			model.addAttribute("lclgvOffRprsGdsList", itemService.getLclgvOffRprsGdsList(UserUtils.getUser().getUserId()));
			List<Item> offgiveItemList =  itemService.getLclgvOffRprsGdsList(UserUtils.getUser().getUserId());
			String lclgvOffRprsGdsListJSON;
			try {
				lclgvOffRprsGdsListJSON = objectMapper.writeValueAsString(offgiveItemList);
				model.addAttribute("lclgvOffRprsGdsListJSON", lclgvOffRprsGdsListJSON);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

		} else if (org.springframework.util.StringUtils.hasLength(itemParam.getShLocgovCode())) {
			model.addAttribute("lclgvRprsGdsList", itemService.getLclgvRprsGdsListManager(itemParam.getShLocgovCode()));
			model.addAttribute("lclgvOffRprsGdsList", itemService.getLclgvOffRprsGdsListManager(itemParam.getShLocgovCode()));
			List<Item> offgiveItemList = itemService.getLclgvOffRprsGdsListManager(itemParam.getShLocgovCode());
			String lclgvOffRprsGdsListJSON;
			try {
				lclgvOffRprsGdsListJSON = objectMapper.writeValueAsString(offgiveItemList);
				model.addAttribute("lclgvOffRprsGdsListJSON", lclgvOffRprsGdsListJSON);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 상품 등록 & 수정 폼
	 * @param item
	 * @param model
	 */
	protected Item invokeItemForm(Item item, String mode, Model model) {

		Integer itemId = item.getItemId();
		String itemUserCode = item.getItemUserCode();

		Seller seller = null;
		ShipmentParam shipmentParam = new ShipmentParam();
		ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();

		Shipment shipment = null;
		ShipmentReturn shipmentReturn = null;

		int restockNoticeCount = 0;


		if ("create".equals(mode)) {//상품 생성일 경우
			item.setItemReturnFlag("Y");
			item.setNonmemberOrderType("1");
			item.setDisplayFlag("Y");
			item.setItemLabel("0");
			item.setShippingType("1");

			if (ShopUtils.isSellerPage()) {
				item.setDeliveryType("2");	// 업체배송
			}

			// 판매자 정보
			seller = sellerService.getSellerById(SellerUtils.getSellerId());
			if (seller != null) {
				item.setSellerId(seller.getSellerId());
				item.setCommissionRate(seller.getCommissionRate());
			}

			// 기본 출고지 정보
			shipmentParam.setSellerId(SellerUtils.getSellerId());
			shipmentParam.setDefaultAddressFlag("Y");

			shipment = shipmentService.getShipmentByParam(shipmentParam);
			if (shipment != null) {
				item.setShipmentId(shipment.getShipmentId());
				item.setShipmentAddress(shipment.getFullAddress());
			}

			// 기본 반품 & 교환지
			shipmentReturnParam.setSellerId(SellerUtils.getSellerId());
			shipmentReturnParam.setDefaultAddressFlag("Y");

			shipmentReturn = shipmentReturnService.getShipmentReturnByParam(shipmentReturnParam);
			if (shipmentReturn != null) {
				item.setShipmentReturnId(shipmentReturn.getShipmentReturnId());
				item.setShipmentReturnAddress(shipmentReturn.getFullAddress());
			}

			// 스팟 할인 -> CJH 2016.12.01 해당 스팟 상품의 등록 타입을 설정 1 : 운영사 부담, 2 : 판매자 부담
			item.setSpotType(ShopUtils.isSellerPage() ? "2" :  "1");
		} else if ("edit".equals(mode)) { //상품 수정 일 경우
			// 가격변경대기중 상품필터
			ItemSaleEditParam itemSaleEditParam = new ItemSaleEditParam();

			itemId = itemService.getItemIdByItemUserCode(itemUserCode);
			if (itemId == null) {
				throw new UserException("상품정보가 없습니다.");
			}

			itemSaleEditParam.setSellerId(SellerUtils.getSellerId());
			itemSaleEditParam.setItemId(itemId);
			itemSaleEditParam.setStatus("0");

			int count = itemService.getItemSaleEditCountByParam(itemSaleEditParam);

			if (count > 0) {
				throw new UserException("해당 상품은 가격변경대기중 입니다.");
			}

			item = itemService.getItemByIdForManager(itemId);

			if (ShopUtils.isSellerPage() && item.getSellerId() != SellerUtils.getSellerId()) {
				throw new PageNotFoundException();
			}

			// 판매자 정보
			seller = sellerService.getSellerById(item.getSellerId());

			// 기본 출고지 정보
			shipmentParam.setShipmentId(item.getShipmentId());

			shipment = shipmentService.getShipmentByParam(shipmentParam);
			if (shipment != null) {
				item.setShipmentAddress(shipment.getFullAddress());
			}

			// 기본 반품 & 교환지
			if (item.getShipmentReturnId() > 0) {
				shipmentReturnParam.setShipmentReturnId(item.getShipmentReturnId());

				shipmentReturn = shipmentReturnService.getShipmentReturnByParam(shipmentReturnParam);
				if (shipmentReturn != null) {
					item.setShipmentReturnAddress(shipmentReturn.getFullAddress());
				}
			}

			// 스팟할인 -> 상품 사용 & 종료인 경우 (자동종료)
			if ("Y".equals(item.getSpotFlag()) && !ObjectUtils.isEmpty(item.getSpotEndDate())
					&& Integer.parseInt(item.getSpotEndDate()) < Integer.parseInt(DateUtils.getToday())) {
				item.setSpotFlag("N");
				item.setSpotStartDate("");
				item.setSpotEndDate("");
				item.setSpotDiscountAmount(0);
			}

			// CJH 2016.12.01 스팟 종료일때 화면별 등록 타입을 설정한다.
			if ("N".equals(item.getSpotFlag())) {
				item.setSpotType(ShopUtils.isSellerPage() ? "2" :  "1");
			}

			if (!ObjectUtils.isEmpty(item.getSpotStartTime()) && !ObjectUtils.isEmpty(item.getSpotEndTime())) {
				String spotStarttime = item.getSpotStartTime();
				String spotEndtime = item.getSpotEndTime();

				model.addAttribute("spotStartHour", spotStarttime.substring(0, 2));
				model.addAttribute("spotStartMinute", spotStarttime.substring(2, 4));
				model.addAttribute("spotEndHour", spotEndtime.substring(0, 2));
				model.addAttribute("spotEndMinute", spotEndtime.substring(2, 4));
			}

			// 카테고리 ID
			int firstCategoryId = getFirstCategoryIdByBreadcrumbs(item.getBreadcrumbs());
			if (firstCategoryId > 0){
				model.addAttribute("categoryId", firstCategoryId);
			}

			// 재입고알림
			RestockNotice restockNotice = new RestockNotice();
			restockNotice.setItemId(item.getItemId());
			restockNoticeCount = restockNoticeService.getRestockNoticeCount(restockNotice);

			//상품 수정일 경우 이미지 설명 불러온다
			//List<ItemImageExplain> imageList = new ArrayList();
			//item = itemService.getItemImagesExplain(item.getItemId());
			 //if (item.getItemImageExplain().isEmpty()) {
                 List<ItemImageExplain> itemImageExplain = itemService.getItemImagesExplain(item.getItemId());
                 item.setItemImageExplain(itemImageExplain);
             //}


			model.addAttribute("pointConfigList", pointService.getPointConfigListByItemId(itemId));
			model.addAttribute("listPage", RequestContextUtils.getRequestContext().getPrevPageUrl());
		} else if ("copy".equals(mode)) {
			item = itemService.getItemByIdForManager(itemId);

			item.setItemId(0);
			item.setItemUserCode("");
			item.setItemSellerCode("");
			item.setItemCode("");

			// 상품 복사일때 옵션 아이디를 초기화 해야 새로 등록됨
			if (item.getItemOptions() != null) {
				for (ItemOption itemOption : item.getItemOptions()) {
					itemOption.setItemOptionId(0);
				}
			}

			// 상품 복사일때 추가 구성상품 아이디를 초기화 해야 새로 등록됨
			if (item.getItemAdditions() != null) {
				for (Item itemAddition : item.getItemAdditions()) {
					itemAddition.setItemId(0);
				}
			}

			// 판매자 정보
			seller = sellerService.getSellerById(item.getSellerId());

			// 기본 출고지 정보
			shipmentParam.setShipmentId(item.getShipmentId());

			shipment = shipmentService.getShipmentByParam(shipmentParam);
			if (shipment != null) {
				item.setShipmentAddress(shipment.getFullAddress());
			}

			// 기본 반품 / 교환지
			if (item.getShipmentReturnId() == 0) {
				shipmentReturnParam.setDefaultAddressFlag("Y");
			}

			shipmentReturnParam.setShipmentReturnId(item.getShipmentReturnId());

			shipmentReturn = shipmentReturnService.getShipmentReturnByParam(shipmentReturnParam);
			if (shipmentReturn != null) {
				item.setShipmentReturnAddress(shipmentReturn.getFullAddress());
			}

			// 카테고리 ID
			int firstCategoryId = getFirstCategoryIdByBreadcrumbs(item.getBreadcrumbs());
			if (firstCategoryId > 0){
				model.addAttribute("categoryId", firstCategoryId);
			}

			model.addAttribute("pointConfigList", pointService.getPointConfigListByItemId(itemId));
		}

		// 카테고리 팀 & 그룹 목록
		List<CategoriesTeam> categoryTeamGroupList = categoriesTeamGroupService.getCategoriesTeamGroupList();

		// 택배사 목록
		DeliveryCompanyParam deliveryCompanyParam = new DeliveryCompanyParam();
		deliveryCompanyParam.setLimit(200);
		List<DeliveryCompany> deliveryCompanyList = deliveryCompanyService.getDeliveryCompanyList(deliveryCompanyParam);

		String today = DateUtils.getToday(Const.DATE_FORMAT);

		// 공급사 목록
//		SellerParam sellerParam = new SellerParam();
//		sellerParam.setStatusCode("2");		// 정상 건
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));

		// 전용상품 - 고정
		model.addAttribute("privateTypes", ItemUtils.getPrivateTypeCodes());

		model.addAttribute("today", today);
		model.addAttribute("hours", ShopUtils.getHours());

		// 브랜드 목록
		model.addAttribute("brandList", brandService.getBrandList(new BrandParam()));

		model.addAttribute("mode", mode);
		model.addAttribute("useItemUserCode", ("edit".equals(mode) ? "Y" : "N"));

		model.addAttribute("categoryTeamGroupList", categoryTeamGroupList);
		model.addAttribute("categoryTeamList", getCategoryTeamList(categoryTeamGroupList));	// 카테고리 팀 목록 (무소속 포함)
		model.addAttribute("deliveryCompanyList", deliveryCompanyList);
		model.addAttribute("colors", CodeUtils.getCodeList("ITEM_COLOR"));

		model.addAttribute("item", item);
		model.addAttribute("itemNoticeCodes", itemService.getItemNoticeCodes());				// 상품고시유형 목록

		model.addAttribute("seller", seller);							// 판매자 정보
		model.addAttribute("shipment", shipment);						// 기본 출고지 정보
		model.addAttribute("shipmentReturn", shipmentReturn);			// 기본 반품 출고지 정보

		model.addAttribute("restockNoticeCount", restockNoticeCount);	// 재입고알림
		model.addAttribute("isSellerPage", ShopUtils.isSellerPage());
		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);

		// 제철 월 선택 조회 20230307
		model.addAttribute("seasonFoodMonthList", itemService.getSeasonFoodItem(item.getItemId()));
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return item;
	}

	/**
	 * 목록데이터 수정 - 선택수정
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
	@PostMapping("list/update")
	public JsonView updateListData(RequestContext requestContext, ItemListParam itemListParam) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		itemService.updateListData(itemListParam);
		return JsonViewUtils.success();
	}

	/**
	 * Seller상품 승인
	 * @param requestContext
	 * @param itemId
	 * @return
	 */
	@PostMapping("edit/seller-item-approval/{itemId}")
	public JsonView sellerItemApproval(RequestContext requestContext, @PathVariable("itemId") int itemId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		Item item = new Item();
		item.setItemId(itemId);

		itemService.updateItemApproval(item);
		return JsonViewUtils.success();
	}

	/**
	 * 목록데이터 공개여부 수정 - 선택수정
	 * @param flag
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
	@PostMapping("list/update-display/{flag}")
	public JsonView updateListDataByDisplay(@PathVariable("flag") String flag, RequestContext requestContext, ItemListParam itemListParam) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		itemListParam.setDisplayFlag(flag);

		itemService.updateListDataByDisplay(itemListParam);
		return JsonViewUtils.success();
	}

	/**
	 * 목록데이터 상품라벨 수정 - 선택수정
	 * @param flag
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
	@PostMapping("list/update-label/{flag}")
	public JsonView updateListDataByLabel(@PathVariable("flag") String flag, RequestContext requestContext, ItemListParam itemListParam) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}


		itemListParam.setSoldOut(flag);

		//itemListParam.setStockQuantity(0);

		itemService.updateListDataByLabel(itemListParam);
		return JsonViewUtils.success();
	}

	/**
	 * 목록데이터 수정 - 선택삭제.
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
	@PostMapping("list/delete")
	public JsonView deleteListData(RequestContext requestContext, ItemListParam itemListParam) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		itemService.deleteListData(itemListParam);
		return JsonViewUtils.success();
	}

	/**
	 * 대표상품 삭제
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
	@PostMapping("representative-item/list/delete")
	public JsonView deleteItemData(RequestContext requestContext, ItemListParam itemListParam) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		itemService.deleteItemData(itemListParam);
		return JsonViewUtils.success();
	}



	/**
	 * 목록데이터 수정 - 상품 노출 순서를 설정한다.
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
	@PostMapping("list/change-ordering")
	public JsonView changeOrdering(RequestContext requestContext, ItemListParam itemListParam) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		itemService.updateItemOrdering(itemListParam);
		return JsonViewUtils.success();

	}


	/**
	 * 목록에서 선택한 상품을 특정 카테고리에 추가한다.
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping(value = "add-items-to-category")
	public String addItemsToCategory(ItemParam itemParam, Model model) {

		String user = "seller";
		if(UserUtils.isManagerLogin()) {
			user = "opmanager";
		}

		model.addAttribute("itemParam", itemParam);
		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());
		return "view:/" + user + "/item/add-items-to-category";
	}


	/**
	 * 목록에서 선택한 상품을 특정 카테고리에 추가한다.
	 * 등록 처리..
	 * @return
	 */
	@RequestProperty(layout="base")
	@PostMapping("add-items-to-category")
	public String addItemsToCategoryAction(ItemListParam itemListParam, Model model) {

		String user = "seller";
		if(UserUtils.isManagerLogin()) {
			user = "opmanager";
		}

		itemService.insertItemCategoryByItemListParam(itemListParam);

		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());

		return ViewUtils.redirect("/" + user + "/item/add-items-to-category", MessageUtils.getMessage("M00691"), JavaScript.CLOSE);	// 카테고리가 추가되었습니다.
	}


	/**
	 * 상품등록
	 * @param model
	 * @return
	 */
	@GetMapping(value={"create", "set-create"})
	public String create(Model model) {
		Item item = new Item();
		if (RequestContextUtils.getRequestUri().indexOf("/set-create") > -1) {
			item.setItemType("3");
		}

		invokeItemForm(item, "create", model);
		return "view:/item/form";
	}


	/**
	 * 대표상품등록
	 * @param model
	 * @return
	 */
	@GetMapping("edit/representative-item/create")
	public String createMainItem(ItemParam itemParam, Model model, RequestContext requestContext) {
		// 승인완료 / 판매 종료건만.
		itemParam.setConditionType("ITEM_LIST_APPROVAL");
		// 대표상품여부
		itemParam.setRepresentativeItemYn("Y");
		// 관리자 권한
//		itemParam.setAdminRole(locgovService.getLoginUserAdminRoleCheck(requestContext));
		itemParam.setAdminRole(locgovService.getLoginUserAdminRoleCheck());

		// 해당 지자체 상품만 조회
		saleson.shop.code.domain.Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId());
		if(locgovCodeDetails != null) {
			itemParam.setShLocgovCode(locgovCodeDetails.getId());	// 지자체 코드
		}

		invokeItemList(itemParam, model);

		return "view:/item/representative-item/form";
	}

	/**
	 * 상품등록 처리
	 * @param item
	 * @param imageFile
	 * @param detailImageFiles
	 * @return
	 */
	@PostMapping(value={"create", "set-create"})
	public String createAction(Item item, RequestContext requestContext,
		@RequestParam(value="imageFile", required=false) MultipartFile imageFile,
		@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles) {

		item.setItemImageFile(imageFile);
		item.setItemDetailImageFiles(detailImageFiles);

		SellerUser sellerUser = itemService.getLoginSellerInfo(SellerUtils.getSellerId());
		// 판매자인 경우
		if (ShopUtils.isSellerPage() && SellerUtils.isSellerLogin()) {
//			saleson.shop.code.domain.Code locgovCodeDetails = itemService.getLoginSellerLocgovCodeDetails(requestContext.getUser().getUserId());
//			item.setLocgovCode(locgovCodeDetails.getId());	// 지자체 코드

			item.setLocgovCode(sellerUser.getLocgovCode());
		}

		try {
			itemService.insertItem(item);
		} catch (UserException e) {
			throw new UserException(e.getErrorMessage());
		}

		RequestContextUtils.setMessage(MessageUtils.getMessage("M00288"));	// 등록되었습니다.

		if (ShopUtils.isSellerPage()) {
			String message = "상품이 등록되었습니다.<br/>";
			String returnUrl = "/seller/item/list";

			Seller seller = sellerService.getSellerById(item.getSellerId());

			// 판매관리자의 상품승인타입이 운영자 승인일 경우
			if (seller.getItemApprovalType().equals("1")) {
				message += "관리자의 승인 후 판매개시 됩니다.";
				returnUrl = "/seller/item/pending/list";
			}

			return ViewUtils.redirect(returnUrl, message + item.getItemCode()); // 사방넷 요청으로 상품코드 alert로 노출, 지우면 안됨
		}

		return "redirect:/opmanager/item/list";
	}

	/**
	 * 상품 수정
	 * @param itemUserCode
	 * @param model
	 * @return
	 */
	@GetMapping(value={"edit/{itemUserCode}", "seller/edit/{itemUserCode}"})
	public String edit(@PathVariable("itemUserCode") String itemUserCode, Model model) {
		Item item = new Item();
		item.setItemUserCode(itemUserCode);

		invokeItemForm(item, "edit", model);
		return "view:/item/form";
	}

	/**
	 * 상품 수정 처리
	 * @param itemUserCode
	 * @param item
	 * @param imageFile
	 * @param detailImageFiles
	 * @param listPage
	 * @return
	 */
	@PostMapping(value={"edit/{itemUserCode}", "seller/edit/{itemUserCode}"})
	public String editAction(@PathVariable("itemUserCode") String itemUserCode, Item item,
		@RequestParam(value="imageFile", required=false) MultipartFile imageFile,
		@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles,
		@RequestParam(required = false) String listPage) {
		Integer itemId = itemService.getItemIdByItemUserCode(itemUserCode);
		if (itemId == null) {
			throw new UserException("상품정보가 없습니다.");
		}

		item.setItemId(itemId);
		item.setItemImageFile(imageFile);
		item.setItemDetailImageFiles(detailImageFiles);

		itemService.updateItem(item);

		String returnUrl = "";

		if (ObjectUtils.isEmpty(listPage)) {
			returnUrl = "/opmanager/item/list";
		} else {
			returnUrl = listPage;
		}

		String message = MessageUtils.getMessage("M00289");	// 수정되었습니다.

		if (ShopUtils.isSellerPage()) {
			Seller seller = sellerService.getSellerById(item.getSellerId());
			if (seller.getItemApprovalType().equals("1")) {
				message += " 관리자의 승인 후 판매개시 됩니다.";
				returnUrl = "/seller/item/pending/list";
			}
		}

		return ViewUtils.redirect(returnUrl, message);
	}

	@PostMapping("seller-info/{sellerId}")
	public JsonView sellerInfo(@PathVariable("sellerId") long sellerId) {
		// 판매자 정보.
		Seller seller = sellerService.getSellerById(sellerId);

		// 기본 출고지 정보
		ShipmentParam shipmentParam = new ShipmentParam();
		shipmentParam.setSellerId(sellerId);

		Shipment shipment = shipmentService.getShipmentByParam(shipmentParam);

		// 기본 반품 / 교환지
		ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();
		shipmentReturnParam.setSellerId(sellerId);
		ShipmentReturn shipmentReturn = shipmentReturnService.getShipmentReturnByParam(shipmentReturnParam);

		HashMap<String, Object> result = new HashMap<>();
		result.put("seller", seller);
		result.put("shipment", shipment);
		result.put("shipmentReturn", shipmentReturn);

		return JsonViewUtils.success(result);
	}

	/**
	 * 상품 복사
	 * @param itemId
	 * @param model
	 * @return
	 */
	@GetMapping("copy/{itemId}")
	public String copy(@PathVariable("itemId") int itemId, Model model) {
		Item item = new Item();
		item.setItemId(itemId);

		invokeItemForm(item, "copy", model);
		return "view:/item/form";
	}

	/**
	 * 상품 복사 처리.
	 * @param item
	 * @param imageFile
	 * @param detailImageFiles
	 * @return
	 */
	@PostMapping("copy/{itemId}")
	public String copyAction(Item item, RequestContext requestContext,
		@RequestParam(value="imageFile", required=false) MultipartFile imageFile,
		@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles) {

		try {
			categoriesFilterService.saveItemFilter(item.getItemId(), item.getFilterCodes());
		} catch(RuntimeException e){
			throw new UserException("상품 카테고리 매핑에 실패하였습니다.");
		}

		String result = createAction(item, requestContext, imageFile, detailImageFiles);
		if (result == null) {
			return "redirect:/opmanager/item/list";
		}

		return result;
	}


	/**
	 * 대표상품 등록 (지자체)
	 * @param requestContext
	 * @param itemId
	 * @return
	 */
	@PostMapping("edit/representative-item/register")
	public JsonView registerMainItem(RequestContext requestContext, @RequestParam("itemId") int itemId, Item item) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		saleson.shop.code.domain.Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId());

		item.setItemId(itemId);	// 상품ID
		item.setLocgovCode(locgovCodeDetails.getId());	// 지자체 코드

		itemService.registerMainItem(item);

		return JsonViewUtils.success();
	}

	/**
	 * 대표상품 삭제 (지자체)
	 * @param requestContext
	 * @param itemId
	 * @return
	 */
	@PostMapping("edit/representative-item/delete")
	public JsonView deleteMainItem(RequestContext requestContext, @RequestParam("itemId") int itemId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		itemService.deleteMainItem(itemId);

		return JsonViewUtils.success();
	}


	/**
	 * 상품 대표이미지를 삭제한다.
	 * @param requestContext
	 * @param itemId
	 * @return
	 */
	@PostMapping("delete-item-image")
	public JsonView deleteItemImage(RequestContext requestContext, @RequestParam("itemId") int itemId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		itemService.deleteItemImageByItemId(itemId);

		return JsonViewUtils.success();
	}


	/**
	 * 상품 상세 이미지를 삭제한다.
	 * @param requestContext
	 * @param itemImageId
	 * @return
	 */
	@PostMapping("delete-item-details-image")
	public JsonView deleteItemDetailsImage(RequestContext requestContext, @RequestParam("itemImageId") int itemImageId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		itemService.deleteItemImageById(itemImageId);

		return JsonViewUtils.success();
	}



	/**
	 * 상품검색.
	 * @param itemParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("find-item")
	public String findItem(ItemParam itemParam, Model model) {
		itemParam.setLocgov(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));
		if (StringUtils.isEmpty(itemParam.getLocgov())) {
			throw new PageNotFoundException();
		}
		setFindItemModel(itemParam, model);
		return ViewUtils.view();
	}

	protected void setFindItemModel(ItemParam itemParam, Model model) {

		itemParam.setDataStatusCode("1");
		itemParam.setDisplayFlag("Y");
		itemParam.setDiscriminator("item_option");

		// 일반상품만
		/*
		itemParam.setItemType("1");
		*/


		// 추천상품인 경우에
		if ("recommend".equals(itemParam.getTargetId())) {
			itemParam.setRecommendFlag("Y");
		}

		itemParam.setConditionType("FIND_ITEM_POPUP");
		// 스팟상품인 경우
		if ("spot".equals(itemParam.getTargetId())) {
			itemParam.setConditionType("SPOT_ITEM");
		}

		// 세트상품인 경우
		if ("set".equals(itemParam.getTargetId())) {
			if (SellerUtils.isSellerLogin()) {
				itemParam.setSellerId(SellerUtils.getSellerId());
			}
			itemParam.setConditionType("FIND_ITEM_FOR_SET");
			itemParam.setSaleStatus("sale");
		}

		int totalItems = itemService.getItemCount(itemParam);

		Pagination pagination = Pagination.getInstance(totalItems);
		//pagination.setItemsPerPage(6);

		itemParam.setPagination(pagination);

		SellerParam sellerParam = new SellerParam();
		if (SellerUtils.isSellerLogin()) {
			if(SellerUtils.getSeller() != null) {
				sellerParam.setSellerId(CommonUtils.longNvl(SellerUtils.getSeller().getSellerId()));
			}
		} else {
			sellerParam.setLocgov(itemParam.getLocgov());
		}

		List<Item> list = itemService.getItemList(itemParam);
		if (list != null && !list.isEmpty()) {
			for (Item item : list) {
				Seller seller = item.getSeller();
				seller.decrypt(sellerEncryptor, ShopUtils.needMasking());
				item.setSeller(seller);
			}
		}
		model.addAttribute("list", list);
		model.addAttribute("itemParam", itemParam);
		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("pagination", pagination);

	}


	/**
	 * 상품검색.
	 * @param itemParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("edit/find-item")
	public String findItemLoc(RequestContext requestContext, ItemParam itemParam, Model model) {
		setFindItemLoc(requestContext, itemParam, model);
		return "view:/item/representative-item/find-item";
	}

	protected void setFindItemLoc(RequestContext requestContext, ItemParam itemParam, Model model) {

		itemParam.setDataStatusCode("1");
		itemParam.setDisplayFlag("Y");
		itemParam.setDiscriminator("item_option");
		itemParam.setOrderBy("MAIN");

		// 해당 지자체 상품만 조회
		saleson.shop.code.domain.Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId());
		itemParam.setShLocgovCode(locgovCodeDetails.getId());

		// 해당 지자체 판매자만 조회되도록 수정
		SellerParam sellerParam = new SellerParam();
		sellerParam.setLocgov(locgovCodeDetails.getId());


		// 일반상품만
		/*
		itemParam.setItemType("1");
		*/


		int totalItems = itemService.getItemCount(itemParam);

		Pagination pagination = Pagination.getInstance(totalItems);
		//pagination.setItemsPerPage(6);

		itemParam.setPagination(pagination);

		List<Item> list = itemService.getItemList(itemParam);
		model.addAttribute("list",list);
		model.addAttribute("itemParam",itemParam);
		model.addAttribute("categoryTeamGroupList", categoriesTeamGroupService.getCategoriesTeamGroupList());
		model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("pagination",pagination);


	}

	@RequestProperty(layout="base")
	@GetMapping("color")
	public String color(Model model) {

		model.addAttribute("colors", CodeUtils.getCodeList("ITEM_COLOR"));
		return ViewUtils.view();
	}


	@PostMapping("color")
	public JsonView color(RequestContext requestContext, Code code) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		code.setLanguage(LocaleContextHolder.getLocale().getLanguage());
		codeService.insertCommonCode(code);


		// Code reload
		EarlyLoadingRepositoryEvent codeReloadEvent = new EarlyLoadingRepositoryEvent("codeInfoRepository",EarlyLoadingRepositoryEvent.Action.ReloadAll);
		codeInfoRepository.onApplicationEvent(codeReloadEvent);

		return JsonViewUtils.success();

	}


	/**
	 * 상품 색상정보를 삭제하다.
	 * @param requestContext
	 * @param code
	 * @return
	 */
	@PostMapping("delete-color")
	public JsonView deleteColor(RequestContext requestContext, Code code) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		code.setLanguage(LocaleContextHolder.getLocale().getLanguage());
		codeService.deleteCommonCode(code);


		// Code reload
		EarlyLoadingRepositoryEvent codeReloadEvent = new EarlyLoadingRepositoryEvent("codeInfoRepository",EarlyLoadingRepositoryEvent.Action.ReloadAll);
		codeInfoRepository.onApplicationEvent(codeReloadEvent);

		return JsonViewUtils.success();

	}


	/**
	 * 판매자 상품코드 중복체크
	 * @param itemUserCode
	 * @return
	 */
	@ResponseBody
	@PostMapping("check-for-duplicate-item-user-code")
	public JsonView checkForDuplicateItemUserCode(@RequestParam("itemUserCode") String itemUserCode) {
		int count = itemService.getItemCountByItemUserCode(itemUserCode);

		if (count == 0) {
			return JsonViewUtils.success();
		} else {
			return JsonViewUtils.failure("중복");
		}

	}

	/**
	 * 상품 등록/수정 페이지의 소속팀 목록을 팀/그룹 리스트에서 가져온다.
	 * 에스테틱, 미용, 네일, 속눈썹 에크 스테, 세일/아울렛, 무소속
	 * @param categoryTeamGroupList
	 * @return
	 */
	private List<CategoriesTeam> getCategoryTeamList(List<CategoriesTeam> categoryTeamGroupList) {
		List<CategoriesTeam> categoryTeamList = new ArrayList<>();

		// 무소속
		CategoriesTeam noTeam = new CategoriesTeam();
		noTeam.setCode("-");
		noTeam.setName("없음");
		categoryTeamList.add(noTeam);

		for (CategoriesTeam categoriesTeam : categoryTeamGroupList) {
			if (categoriesTeam.getCategoryTeamFlag().equals("Y")) {
				categoryTeamList.add(categoriesTeam);
			}
		}

		return categoryTeamList;
	}

	/**
	 * 리뷰 리스트
	 * @param itemParam
	 * @param itemReview
	 * @param model
	 * @return
	 */
	@GetMapping("/review/list")
	public String reviewList(ItemParam itemParam, ItemReview itemReview, Model model) {
		String today1 = DateUtils.getToday("yyyyMMdd");
		itemParam.setSearchStartDate(StringUtils.defaultIfEmpty(itemParam.getSearchStartDate(), today1));
		itemParam.setSearchEndDate(StringUtils.defaultIfEmpty(itemParam.getSearchEndDate(), today1));

		if (!UserUtils.isManagerLogin() && !UserUtils.isSellerLogin()) {
			return ViewUtils.redirect("/opmanager/", "로그인 상태가 아닙니다.");
		}

		if (ShopUtils.isSellerPage()) {
			itemParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER));
			itemParam.setSellerId(SellerUtils.getSellerId());
			if (!org.springframework.util.StringUtils.hasLength(itemParam.getShLocgovCode())) {
				return ViewUtils.redirect("/seller/", "지자체 정보가 없습니다.");
			}
		} else if (ShopUtils.isOpmanagerPage() && !isUpperAdmin()) {
			itemParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			if (!org.springframework.util.StringUtils.hasLength(itemParam.getShLocgovCode())) {
				return ViewUtils.redirect("/opmanager/", "지자체 정보가 없습니다.");
			}
		} else if (ShopUtils.isOpmanagerPage() && isUpperAdmin()) {
			// 지자체 선택한 항목으로 조회
		} else {
			if (ShopUtils.isSellerPage()) {
				return ViewUtils.redirect("/seller/", "올바른 접근이 아닙니다.");
			} else {
				return ViewUtils.redirect("/opmanager/", "올바른 접근이 아닙니다.");
			}
		}

		int reviewCount = 0;

		Pagination pagination = Pagination.getInstance(reviewCount);

		itemParam.setPagination(pagination);

		String today = DateUtils.getToday(Const.DATE_FORMAT);

		List<ItemReview> reviewList = Collections.EMPTY_LIST;

		for (ItemReview review : reviewList) {
			String star = "";
			for (int i = 0; i < review.getScore(); i++) {
				star += "★";
			}
			review.setStarScore(star);
		}

		SellerParam sellerParam = new SellerParam();
		sellerParam.setStatusCode("2");
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("reviewCount", reviewCount);
		model.addAttribute("itemParam", itemParam);
		model.addAttribute("today", today);
		model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
		model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
		model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("pagination", pagination);

		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 시도 지자체 리스트

		return "view";
	}

	/**
	 * 상품리뷰 수정
	 * @param itemReviewId
	 * @param itemParam
	 * @param model
	 * @return
	 */
	@GetMapping("/review/edit/{itemReviewId}")
	public String updateReview(@PathVariable("itemReviewId") int itemReviewId, ItemParam itemParam, Model model) {

		ItemReview itemReview = itemService.getItemReviewById(itemReviewId);

		model.addAttribute("itemReview", itemReview);
		model.addAttribute("itemParam", itemParam);

		return ViewUtils.getView("/item/review/form");
	}

	/**
	 * 리뷰수정처리
	 * @param itemReviewId
	 * @param itemReview
	 * @return
	 */
	@PostMapping("/review/edit/{itemReviewId}")
	public String updateReviewAction(@PathVariable("itemReviewId") int itemReviewId, ItemReview itemReview) {

		itemReview.setRecommendFlag(itemReview.getRecommendFlag() == null ? "N" : "Y");
		itemReview.setAnswerLoginId(UserUtils.getLoginId());

		itemService.updateItemReview(itemReview);

		return ViewUtils.redirect("/opmanager/item/review/list", MessageUtils.getMessage("M00289"));	// 수정되었습니다.
	}

	/**
	 * 리뷰삭제처리
	 * @param requestContext
	 * @param itemReviewId
	 * @return
	 */
	@PostMapping("/review/delete/{itemReviewId}")
	public JsonView deleteReview(RequestContext requestContext, @PathVariable("itemReviewId") int itemReviewId) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			itemService.deleteItemReview(itemReviewId);
			return JsonViewUtils.success();
		} catch (RuntimeException e) {
//			return JsonViewUtils.failure(e.getMessage());
//			return JsonViewUtils.failure(MessageUtils.getMessage("실패했습니다."));			// 실패했습니다.
			return JsonViewUtils.failure("실패했습니다.");			// 실패했습니다.
		}
	}

	/**
	 * 리뷰 이미지 삭제
	 * @param requestContext
	 * @param itemReview
	 * @param itemReviewImage
	 * @return
	 */
	@PostMapping("/delete-item-review-image")
	public JsonView deleteItemReviewImage(RequestContext requestContext,
										  ItemReview itemReview, ItemReviewImage itemReviewImage) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		itemService.deleteItemReviewImage(itemReview, itemReviewImage);

		return JsonViewUtils.success();
	}

	/**
	 * Q&A 데이터 삭제
	 * @param requestContext
	 * @param listParam
	 * @return
	 */
	@GetMapping("/review/recommend")
	public String recommendReview(RequestContext requestContext, ListParam listParam, Model model) {

		model.addAttribute("listParam", listParam);

		return ViewUtils.getView("/item/review/popup");
	}


	@RequestProperty(layout="base")
	@GetMapping("/download-item-csv")
	public ModelAndView downloadItemCsv(ItemParam itemParam) {

		// 정렬조건은 CategoryId가 있는 경우에만 허용.
		if (itemParam.getOrderBy() != null && itemParam.getOrderBy().equals("ORDERING")
			&& (itemParam.getCategoryId() == null || itemParam.getCategoryId().equals(""))) {
			itemParam.setOrderBy("");
			itemParam.setSort("DESC");
		}
		itemParam.setDataStatusCode("1");

		Pagination pagination = Pagination.getInstance(itemService.getItemCount(itemParam), 40000);
		pagination.setItemsPerPage(100000);
		itemParam.setPagination(pagination);


		// CSV
		//ModelAndView mav = new ModelAndView(new ItemCsvView("item_" + DateUtils.getToday() + ".csv"));

		// Excel
		ModelAndView mav = new ModelAndView(new ItemExcelView());
		mav.addObject("itemList", itemService.getItemList(itemParam));
		mav.addObject("itemCategoryList", itemService.getItemCategoryListForExcel(itemParam));		// 상품 카테고리
		mav.addObject("itemRelationList", itemService.getItemRelationListForExcel(itemParam));		// 상품별 관련상품
		mav.addObject("itemPointConfigList", itemService.getItemPointListForExcel(itemParam));		// 상품별 포인트 설정.


		return mav;
	}

	/**
	 * 엑셀 다운로드 팝업
	 * @param itemParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("/download-excel")
	public String downloadExcel(ItemParam itemParam, Model model) {
		model.addAttribute("itemParam", itemParam);
		return "view";
	}


	/**
	 * 엑셀 다운로드
	 * @param itemParam
	 * @return
	 */
//	@RequestProperty(layout="base")
	@PostMapping("/download-excel")
	public ModelAndView downloadExcelProcess(ItemParam itemParam, RequestContext requestContext) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			throw new UserException("엑셀 다운로드 권한이 없습니다.");
//			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/item/download-excel", "엑셀 다운로드 권한이 없습니다.");
		}

		// 관리자인 경우
		if (UserUtils.isManagerLogin()) {
			// 지자체관리자일 경우 지자체코드 필요
			itemParam.setLocgov(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

		// Excel
		ModelAndView mav = new ModelAndView(new ItemExcelView());

		// 브랜드 목록.
//		mav.addObject("brandList", brandService.getBrandList(new BrandParam()));

		mav.addObject("itemParam", itemParam);

		itemParam.setConditionType("EXCEL_DOWNLOAD");

		// 상품코드를 직접입력해서 다운로드 하는 경우 모든 조건은 무시되고 입력한 상품만 다운로드함.
		if (itemParam.getExcelItemUserCodes().size() > 0) {
			itemParam.setConditionType("EXCEL_ITEM_USER_CODE_FIX");
		}


		// 상품 조회 (ITEM, ITEM_OPTION, ITEM_IMAGE) 인 경우 OrderManagerController
		if (Arrays.asList(itemParam.getExcelDownloadData()).contains("item_main")
			|| Arrays.asList(itemParam.getExcelDownloadData()).contains("item_seo")
			|| Arrays.asList(itemParam.getExcelDownloadData()).contains("item_check")
			|| Arrays.asList(itemParam.getExcelDownloadData()).contains("item_option")
			|| Arrays.asList(itemParam.getExcelDownloadData()).contains("item_image")) {


			// 정렬조건은 CategoryId가 있는 경우에만 허용.
			if (itemParam.getOrderBy() != null && itemParam.getOrderBy().equals("ORDERING")
				&& (itemParam.getCategoryId() == null || itemParam.getCategoryId().equals(""))) {
				itemParam.setOrderBy("");
				itemParam.setSort("DESC");
			}

//			Pagination pagination = Pagination.getInstance(itemService.getItemCount(itemParam), 40000);
//			pagination.setItemsPerPage(100000);
			Pagination pagination = Pagination.getInstance(Integer.MAX_VALUE, Integer.MAX_VALUE);
			itemParam.setPagination(pagination);
			itemParam.setItemsPerPage(pagination.getItemsPerPage());

			// 택배사 목록
//			DeliveryCompanyParam deliveryCompanyParam = new DeliveryCompanyParam();
//			deliveryCompanyParam.setLimit(200);
//			List<DeliveryCompany> deliveryCompanyList = deliveryCompanyService.getDeliveryCompanyList(deliveryCompanyParam);

			SellerParam sellerParam = new SellerParam();

			// 판매관리자일 경우 본인만 조회
			if(ShopUtils.isSellerPage()) {
				itemParam.setSellerId(SellerUtils.getSellerId());
				sellerParam.setSellerId(SellerUtils.getSellerId());
			}

			mav.addObject("itemList", itemService.getItemList(itemParam));
//			mav.addObject("deliveryCompanyList", deliveryCompanyList);	// 택배사 설정

			sellerParam.setStatusCode("2"); // 정상 건.
			sellerParam.setConditionType("SELLER_LIST_FOR_SELECTBOX");

//			mav.addObject("sellerList", sellerService.getSellerListByParam(sellerParam));	// 공급사 설정

			//mav.addObject("itemPointConfigList", itemService.getItemPointListForExcel(itemParam));			// 상품별 포인트 설정.

//			long deliverySellerId = itemParam.getSellerId();
//			long shipmentReturnSellerId = itemParam.getSellerId();

//			if ("1".equals(itemParam.getDeliveryType())) {
//				deliverySellerId = 90000000;
//			}
//
//			if ("1".equals(itemParam.getShipmentReturnType())) {
//				shipmentReturnSellerId = 90000000;
//			}

//			mav.addObject("shipmentList", shipmentService.getShipmentListBySellerId(deliverySellerId));	// 출고지 주소 설정
//			mav.addObject("shipmentReturnList", shipmentReturnService.getShipmentReturnListBySellerId(shipmentReturnSellerId));	// 반품/교환 주소 설정
		}

		if (Arrays.asList(itemParam.getExcelDownloadData()).contains("item_table")) {
			mav.addObject("itemInfoList", itemService.getItemInfoListForExcel(itemParam));				// 상품 기본 정보
//			mav.addObject("itemNoticeCodes", itemService.getItemNoticeCodes());
		}

		/*
		if (Arrays.asList(itemParam.getExcelDownloadData()).contains("item_table_mobile")) {
			mav.addObject("itemInfoMobileList", itemService.getItemInfoMobileListForExcel(itemParam));				// 상품 기본 정보 (모바일)
		}
		*/

		if (Arrays.asList(itemParam.getExcelDownloadData()).contains("item_category")) {
			mav.addObject("itemCategoryList", itemService.getItemCategoryListForExcel(itemParam));		// 상품 카테고리
		}

//		if (Arrays.asList(itemParam.getExcelDownloadData()).contains("item_relation")) {
//			mav.addObject("itemRelationList", itemService.getItemRelationListForExcel(itemParam));		// 상품별 관련상품
//		}

		/*
		if (Arrays.asList(itemParam.getExcelDownloadData()).contains("item_point")) {
			mav.addObject("itemPointConfigList", itemService.getItemPointListForExcel(itemParam));			// 상품별 포인트 설정.
		}
		*/

//		if (Arrays.asList(itemParam.getExcelDownloadData()).contains("item_keyword")) {
//			mav.addObject("itemKeywordList", itemService.getItemKeywordListForExcel(itemParam));			// 상품별 검색어
//		}
		Cookie cookie = new Cookie("DOWNLOAD_STATUS", "complete");
		cookie.setHttpOnly(true);
		cookie.setPath("/");					// 모든 경로에서 접근 가능하도록
		return mav;
	}




	/**
	 * 엑셀 업로드
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("/upload-excel")
	public String uploadExcel(Model model) {

		if (RedirectAttributeUtils.hasRedirectAttributes()) {
			model.addAttribute("result", RedirectAttributeUtils.get("result"));
		}
		return "view";
	}

	/**
	 * 엑셀 업로드 처리.
	 * @param multipartFile
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@PostMapping("/upload-excel")
	public String uploadExcelProcess(@RequestParam(value="file", required=false) MultipartFile multipartFile, Model model) {
		String result = itemService.insertExcelData(multipartFile);

		model.addAttribute("result", result);
		//redirectAttribute.addAttribute("result", result);
		RedirectAttributeUtils.addAttribute("result", result);

		if(ShopUtils.isSellerPage()) {
			return "redirect:/seller/item/upload-excel";
		}

		return ViewUtils.redirect("/opmanager/item/upload-excel");
	}



	@PostMapping("item-notice-list")
	@RequestProperty(layout="base")
	public JsonView itemNoticeList(RequestContext requestContext, String itemNoticeCode) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		List<ItemNotice> itemNoticeList = itemService.getItemNoticeListByCode(itemNoticeCode);
		return JsonViewUtils.success(itemNoticeList);
	}



	@RequestProperty(layout="base")
	@GetMapping("/upload-csv")
	public String uploadCsv(HttpSession session) {

		return ViewUtils.view();
	}


	@PostMapping("/upload-csv")
	public String uploadCsvProcess(@RequestParam(value="file[]", required=false) MultipartFile[] multipartFiles, HttpSession session) {
		Future<AsyncReport> asyncReport = itemService.uploadCsv(multipartFiles);

		// session.setAttribute("asyncReport", asyncReport);

		return ViewUtils.redirect("/opmanager/item/upload-csv","", JavaScript.CLOSE_AND_OPENER_RELOAD);
	}



	@SuppressWarnings("unchecked")
	@GetMapping("/upload-csv-status")
	public JsonView uploadCsvStatus(HttpSession session) {
		if (session.getAttribute("asyncReport") == null || session.getAttribute("asyncReport").equals("")) {
			return JsonViewUtils.success(new AsyncReport());
		}

		Future<AsyncReport> future = (Future<AsyncReport>) session.getAttribute("asyncReport");
		AsyncReport asyncReport;
		try {
			asyncReport = (AsyncReport) future.get();

			if(future.isDone()) {
				asyncReport.setStatus(AsyncReport.COMPLETE);
				// session.removeAttribute("asyncReport");
				return JsonViewUtils.success(asyncReport);
			}

			asyncReport.setStatus(AsyncReport.WORKING);

			return JsonViewUtils.success(asyncReport);

		} catch (InterruptedException | ExecutionException e) {
//			log.warn("[Excepton] uploadCsvStatus : {}", e.getMessage());
			log.warn("[Excepton] uploadCsvStatus : {}", getClass().getName() + " :: uploadCsvStatus InterruptedException | ExecutionException ==========", e);
			asyncReport = new AsyncReport();
//			asyncReport.setMessage(e.getMessage());
//			asyncReport.setMessage(MessageUtils.getMessage("실패했습니다."));			// 실패했습니다.
			asyncReport.setMessage("실패했습니다.");			// 실패했습니다.
			return JsonViewUtils.success(asyncReport);
		}

	}




	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(int.class, "salePrice", new ItemPricePropertyEditor());
		dataBinder.registerCustomEditor(int.class, "salePriceNonmember", new ItemPricePropertyEditor());
		dataBinder.registerCustomEditor(int.class, "stockQuantity", new ItemQuantityPropertyEditor());
		dataBinder.registerCustomEditor(int.class, "orderMinQuantity", new ItemQuantityPropertyEditor());
		dataBinder.registerCustomEditor(int.class, "orderMaxQuantity", new ItemQuantityPropertyEditor());
		dataBinder.registerCustomEditor(String[].class, new StringArrayPropertyEditor(null));
	}

	/**
	 * (팝업) 상품 변경 로그 목록 조회
	 * @param itemParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="log")
	@GetMapping("popup/log/{itemId}")
	public String logList(ItemParam itemParam, @PathVariable("itemId") int itemId, Model model) {

		itemParam.setItemId(itemId);

		int itemCount = itemService.getItemLogCountById(itemParam);

		// 페이징
		Pagination pagination = Pagination.getInstance(itemCount);
		itemParam.setPagination(pagination);

		model.addAttribute("list", itemService.getItemLogListById(itemParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("itemCount", itemCount);

		return ViewUtils.getView("/item/popup/log-list");
	}

	/**
	 * 가격변경대기 상품목록
	 * @param itemSaleEditParam
	 * @param model
	 * @return
	 */
	@GetMapping("sale-edit/list")
	public String editSaleList(ItemSaleEditParam itemSaleEditParam, Model model) {
		String today1 = DateUtils.getToday("yyyyMMdd");
		itemSaleEditParam.setSearchStartDate(StringUtils.defaultIfEmpty(itemSaleEditParam.getSearchStartDate(), today1));
		itemSaleEditParam.setSearchEndDate(StringUtils.defaultIfEmpty(itemSaleEditParam.getSearchEndDate(), today1));

		if ( itemSaleEditParam.getStatus() == null ) { //기본값 : 승인대기
			itemSaleEditParam.setStatus("0");
		}

		Pagination pagination = Pagination.getInstance(itemService.getItemSaleEditCountByParam(itemSaleEditParam));
		itemSaleEditParam.setPagination(pagination);

		List<ItemSaleEdit> list = itemService.getItemSaleEdit(itemSaleEditParam);

		model.addAttribute("list", list);
		model.addAttribute("itemSaleEditParam",itemSaleEditParam);
		model.addAttribute("pagination",pagination);
		return "view:sale-edit/list";
	}

	@PostMapping("sale-edit/update")
	public JsonView updateSaleEdit(HttpServletRequest request, RequestContext requestContext,
		ItemListParam itemListParam) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		String message = "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = new Date();

		ItemSaleEdit itemSaleEdit = new ItemSaleEdit();

		if ("complete".equals(itemListParam.getRequestStatus())) {
			message = "[ "+sdf.format(today)+"_"+UserUtils.getLoginId()+" ] 승인완료처리\n";
			itemSaleEdit.setMessage(message);
			itemSaleEdit.setStatus("1");//승인완료
		} else {
			message = "[ "+sdf.format(today)+"_"+UserUtils.getLoginId()+" ] 승인거절처리\n";
			itemSaleEdit.setMessage(message);
			itemSaleEdit.setStatus("2");//승인거절
		}

		ItemSaleEditParam itemSaleEditParam = new ItemSaleEditParam();
		ItemSaleEdit itemPriceInfo = new ItemSaleEdit();

		for (String itemSaleEditId : itemListParam.getId()) {

			if ("complete".equals(itemListParam.getRequestStatus())) { //승인완료 시, 상품가격업데이트
				//상품가격업데이트
				itemSaleEditParam.setItemSaleEditId(Integer.parseInt(itemSaleEditId));
				itemPriceInfo = itemService.getItemSaleEditByParam(itemSaleEditParam);//업데이트할 가격정보 가져오기

				if (itemPriceInfo == null) {
					throw new UserException("가격정보를 가져올수없습니다.");
				}

				itemService.updateItemPrice(itemPriceInfo);//가격정보업데이트
			}

			itemSaleEdit.setItemSaleEditId(Integer.parseInt(itemSaleEditId));
			itemService.updateSaleEditStatus(itemSaleEdit);//상태변경
		}

		return JsonViewUtils.success();
	}

	/**
	 * 메시지 보기
	 * @param itemSaleEditId
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("sale-edit/view/{itemSaleEditId}")
	public String viewMessage(@PathVariable("itemSaleEditId") int itemSaleEditId, Model model) {

		ItemSaleEditParam itemSaleEditParam = new ItemSaleEditParam();
		itemSaleEditParam.setItemSaleEditId(itemSaleEditId);

		ItemSaleEdit itemSaleEdit = itemService.getItemSaleEditByParam(itemSaleEditParam);

		model.addAttribute("itemSaleEdit", itemSaleEdit);
		model.addAttribute("listPage", RequestContextUtils.getRequestContext().getPrevPageUrl());

		return "view:sale-edit/view";
	}

	/**
	 * 재입고알림 메시지 전송
	 * @param requestContext
	 * @param itemId
	 * @return
	 */
	@ResponseBody
	@PostMapping("restock-notice/message")
	public JsonView message(RequestContext requestContext, @RequestParam("itemId") int itemId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			restockNoticeService.sendRestockNotice(itemId);
		} catch(RuntimeException e) {
//			return JsonViewUtils.failure(e.getMessage());
//			return JsonViewUtils.failure(MessageUtils.getMessage("실패했습니다."));			// 실패했습니다.
			return JsonViewUtils.failure("실패했습니다.");			// 실패했습니다.
		}

		return JsonViewUtils.success();
	}

	private int getFirstCategoryIdByBreadcrumbs(List<Breadcrumb> breadcrumbs) {

		if(breadcrumbs != null && !breadcrumbs.isEmpty()){
			Breadcrumb temp = breadcrumbs.get(0);
			String categoryId = temp.getBreadcrumbCategories().get(temp.getBreadcrumbCategories().size()-1).getCategoryId();
			return com.onlinepowers.framework.util.StringUtils.string2integer(categoryId);
		}

		return 0;
	}

	/**
	 * 리뷰삭제처리(다건)
	 * @param requestContext
	 * @param itemReviewId
	 * @return
	 */
	@PostMapping("/review/delete-list")
	public JsonView deleteReviewList(RequestContext requestContext, ListParam listParam) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		try {
			itemService.deleteItemReviewList(listParam);
			return JsonViewUtils.success();
		} catch (RuntimeException e) {
//			return JsonViewUtils.failure(e.getMessage());
			return JsonViewUtils.failure("실패했습니다.");
		}
	}


	/**
	 * 2지자체 조회(콤보)
	 *
	 * @param code
	 * @param model
	 * @return
	 */
	@PostMapping("options-by-locgovCode")
	public @ResponseBody List<HashMap<String, Object>> optionsByGroupId(@RequestParam(name="code", defaultValue = "0") String code, Model model) {
		return giveStateService.getLocgovCodeList(code);
	}

	private boolean isUpperAdmin() {
		if (SecurityUtils.hasRole("ROLE_ADMIN_1")
				|| SecurityUtils.hasRole("ROLE_ADMIN_2")
				|| SecurityUtils.hasRole("ROLE_ADMIN_3")
				|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			return true;
		}
		return false;
	}


	private ModelAndView excelDownloadRedirect(boolean isManagerPage, String url, String msg) {
		return new ModelAndView(ViewUtils.redirect(makeUrl(isManagerPage, url), msg));
	}

	private String makeUrl(boolean isManagerPage, String url) {
		if (isManagerPage) {
			return "/opmanager" + url;
		} else {
			return "/seller" + url;
		}
	}


	/**
	 * 지자체 대표 답례품 관리 - 현재 상태 저장
	 *
	 * @param code
	 * @param model
	 * @return
	 */
	@PostMapping("saveLclgvRprsGds")
	public @ResponseBody JsonView saveLclgvRprsGds(ItemParam param, Model model) {
		if (SecurityUtils.hasRole("ROLE_ADMIN_5")
				|| SecurityUtils.hasRole("ROLE_ADMIN_6")){
			try {
				if (param.getItemIds() == null) {
					throw new NullPointerException();
				}
				int length = param.getItemIds().length;
				if (length == 0) {
					throw new NullPointerException();
				}
				if (length > 4) {
					return JsonViewUtils.failure("쵀대 4개까지 등록 가능합니다.");
				}
				itemService.insertLclgvRprsGds(param, true);
			} catch (NullPointerException e) {
				itemService.insertLclgvRprsGds(param, false);
			}
			return JsonViewUtils.success();
		} else {
			return JsonViewUtils.failure("권한이 없습니다.");
		}
	}

	/**
	 * 지자체 오프라인 대표 답례품 관리 - 현재 상태 저장
	 *
	 * @param code
	 * @param model
	 * @return
	 */
	@PostMapping("saveLclgvOffRprsGds")
	public @ResponseBody JsonView saveLclgvOffRprsGds(@RequestBody List<Map<String, String>> optionList) {
		if (SecurityUtils.hasRole("ROLE_ADMIN_5")
				|| SecurityUtils.hasRole("ROLE_ADMIN_6")){
			try {
				int length = optionList.size();
				if (optionList == null || length == 0) {
					throw new NullPointerException("item_id가 존재하지 않습니다.");
				}
				if (length > 2) {
					return JsonViewUtils.failure("쵀대 2개까지 등록 가능합니다.");
				}
				 itemService.insertLclgvOffRprsGds(optionList, true);
			} catch (NullPointerException e) {
				 itemService.insertLclgvOffRprsGds(optionList, false);
			}
			return JsonViewUtils.success();
		} else {
			return JsonViewUtils.failure("권한이 없습니다.");
		}
	}

	/**
	 * 답례품 옵션 조회
	 *
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("getOptionList")
	public @ResponseBody List<ItemOption> getLclgvOffRprsGdsOptionList(Integer itemId) throws Exception {
		if(SecurityUtils.hasRole("ROLE_ADMIN_5")
			|| SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			try {
				if(itemId == null) {
					throw new NullPointerException();
				}
				// itemId로 OP_ITEM 테이블 조회
				Item item = itemService.getItem(itemId);
				// 조회된 데이터에서 ITEM_OPTION_TYPE 확인
				if("N".equals(item.getItemOptionFlag()))
					return null;
				return itemService.getItemOption(item);
			}catch (Exception e) {
				throw new Exception("옵션 조회에 문제가 발생하였습니다.");
			}
		}else {
			throw new Exception("권한이 없습니다.");
		}
	}

	/**
	 * 답례품 조합형 옵션 종류 및 가격 조회
	 *
	 * @param optionMap
	 * @return
	 * @throws Exception
	 */
	@PostMapping("getComboOptionList")
	public @ResponseBody List<Map<String, String>> getComboOptionList(@RequestBody(required = false) Map<String, Object> optionMap) throws Exception {
		if(SecurityUtils.hasRole("ROLE_ADMIN_5")
			|| SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			try {
				return itemService.getComboOptionList(optionMap);
			}catch (Exception e) {
				throw new Exception("조합형 옵션 조회에 문제가 발생하였습니다.");
			}
		}else {
			throw new Exception("권한이 없습니다.");
		}
	}

	/**
	 * 오프라인 답례품의 선택한 옵션 조회
	 *
	 * @param itemId
	 * @return
	 * @throws Exception
	 */
	@PostMapping("getSelectOption")
	public @ResponseBody String getSelectOption(Integer itemId) throws Exception {
		if(SecurityUtils.hasRole("ROLE_ADMIN_5")
			|| SecurityUtils.hasRole("ROLE_ADMIN_6")) {
			try {
				return itemService.getSelectOption(itemId);
			}catch (Exception e) {
				throw new Exception("으프라인 답례품의 선택한 옵션 조회에 오류 발생하였습니다.");
			}
		}else {
			throw new Exception("권한이 없습니다.");
		}
	}


	private void getLinkviewData(Item item) {
    	threadService.execute(new Runnable() {
			@Override
			public void run() {
				int runCnt = 0;
				log.info(getClass().getName() + " saveItemImgDescByLinkview thread start : " + runCnt);
				for (int i = 0 ; i < 10 ; i++) {
					log.info(getClass().getName() + " saveItemImgDescByLinkview thread is running : " + runCnt);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						log.info(getClass().getName() + " saveItemImgDescByLinkview thread is interrupted : " + runCnt);
					}
				}
				log.info("end : " + runCnt);
			}
		});
	}


    @PostMapping("/saveItemImgDescByLinkview")
    public ResponseEntity<Map<String, Object>> saveItemImgDescByLinkview(HttpServletRequest request
    		, @RequestBody Map<String, Object> param) {

//    	ObjectMapper mapper = new ObjectMapper();
//    	JsonObject obj = new JsonObject();
//    	JsonArray arr = new JsonArray();
//
//    	try {
//			String data = mapper.writeValueAsString(param);
//			obj = JsonParser.parseString(data).getAsJsonObject();
////			arr = JsonParser.parseString(data).getAsJsonArray();
//		} catch (JsonProcessingException e) {
//			log.error(getClass().getName() + " saveItemImgDescByLinkview error", e);
//		}
//
//    	threadService.execute(new Runnable() {
//			@Override
//			public void run() {
//		    	int runCnt = 0;
//
//				log.info(getClass().getName() + " saveItemImgDescByLinkview thread start : " + runCnt);
//				for (int i = 0 ; i < 10 ; i++) {
//					log.info(getClass().getName() + " saveItemImgDescByLinkview thread is running : " + runCnt);
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						log.info(getClass().getName() + " saveItemImgDescByLinkview thread is interrupted : " + runCnt);
//					}
//				}
//				log.info("end : " + runCnt);
//			}
//		});
//
////    	Future<Map<String, Object>> future = threadService.submit(thread);
////
////    	try {
////    		Object threadResult = future.get();
////    		if (threadResult instanceof Map<?, ?>) {
////    			Map<String, Object> result = (Map<String, Object>) threadResult;
////    			log.info(result.toString());
////    		}
////    	} catch (InterruptedException | ExecutionException e) {
////
////    	}

//    	log.info("============= param :: " + param.toString());

		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			log.info("============= parameters :: name :: " + name + ", value :: " + request.getParameter(name));
		}
		log.info("============= parameters :: " + param.toString());
    	getLinkviewData(null);

    	return ApiResponseEntity.data().put("data", "data1111111 success").ok();
    }

    /**
	 * 리뷰 리스트
	 * @param itemParam
	 * @param itemReview
	 * @param model
	 * @return
	 */
	@PostMapping("/review/list")
	public String searchReviewList(ItemParam itemParam, ItemReview itemReview, Model model) {
		String today1 = DateUtils.getToday("yyyyMMdd");
		itemParam.setSearchStartDate(StringUtils.defaultIfEmpty(itemParam.getSearchStartDate(), today1));
		itemParam.setSearchEndDate(StringUtils.defaultIfEmpty(itemParam.getSearchEndDate(), today1));

		if (!UserUtils.isManagerLogin() && !UserUtils.isSellerLogin()) {
			return ViewUtils.redirect("/opmanager/", "로그인 상태가 아닙니다.");
		}

		if (ShopUtils.isSellerPage()) {
			itemParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER));
			itemParam.setSellerId(SellerUtils.getSellerId());
			if (!org.springframework.util.StringUtils.hasLength(itemParam.getShLocgovCode())) {
				return ViewUtils.redirect("/seller/", "지자체 정보가 없습니다.");
			}
		} else if (ShopUtils.isOpmanagerPage() && !isUpperAdmin()) {
			itemParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			if (!org.springframework.util.StringUtils.hasLength(itemParam.getShLocgovCode())) {
				return ViewUtils.redirect("/opmanager/", "지자체 정보가 없습니다.");
			}
		} else if (ShopUtils.isOpmanagerPage() && isUpperAdmin()) {
			// 지자체 선택한 항목으로 조회
		} else {
			if (ShopUtils.isSellerPage()) {
				return ViewUtils.redirect("/seller/", "올바른 접근이 아닙니다.");
			} else {
				return ViewUtils.redirect("/opmanager/", "올바른 접근이 아닙니다.");
			}
		}

		int reviewCount = itemService.getItemReviewCountByParam(itemParam);

		Pagination pagination = Pagination.getInstance(reviewCount);

		itemParam.setPagination(pagination);

		String today = DateUtils.getToday(Const.DATE_FORMAT);

		List<ItemReview> reviewList = itemService.getItemReviewListByParam(itemParam);

		for (ItemReview review : reviewList) {
			String star = "";
			for (int i = 0; i < review.getScore(); i++) {
				star += "★";
			}
			review.setStarScore(star);
		}

		SellerParam sellerParam = new SellerParam();
		sellerParam.setStatusCode("2");
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("reviewCount", reviewCount);
		model.addAttribute("itemParam", itemParam);
		model.addAttribute("today", today);
		model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
		model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
		model.addAttribute("month3", DateUtils.addYearMonthDay(today, 0, -3, 0));
		model.addAttribute("reviewList", reviewList);
		model.addAttribute("pagination", pagination);

		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 시도 지자체 리스트

		return "view";
	}

	/**
	 * 상품검색.
	 * @param itemParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@PostMapping("find-item")
	public String findItemPost(ItemParam itemParam, Model model) {
		return findItem(itemParam, model);
	}

	/**
	 * 상품검색.
	 * @param itemParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@PostMapping("edit/find-item")
	public String findItemLocPost(RequestContext requestContext, ItemParam itemParam, Model model) {
		return findItemLoc(requestContext, itemParam, model);
	}

	/**
	 * 대표상품등록
	 * @param model
	 * @return
	 */
	@PostMapping("edit/representative-item/create")
	public String createMainItemPost(ItemParam itemParam, Model model, RequestContext requestContext) {
		return createMainItem(itemParam, model, requestContext);
	}

	/**
	 * 상품 목록 페이지
	 * @param itemParam
	 * @param model
	 * @return
	 */
	@PostMapping("list")
	public String listPost(ItemParam itemParam, Model model) {
		return list(itemParam, model);
	}

	/**
	 * 답례품승인관리 미리보기
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestProperty(title="타이틀 정보", layout="blank")
	@GetMapping("/seller/preview")
	public String preview(@RequestParam("itemUserCode") String itemUserCode, ItemParam itemParam, Model model) {

		//아이템 정보 조회
		itemParam.setItemUserCode(itemUserCode);
		Item item = itemService.getItemByItemUserCodePreview(itemUserCode);

		//이미지 사이즈 조정(L)
        if (item.getItemImages().size() > 0) {
            for (ItemImage itemImage : item.getItemImages()) {
                if (!ObjectUtils.isEmpty(itemImage.getImageName())) {
                    itemImage.setImageName(ShopUtils.loadImage(item.getItemUserCode(), itemImage.getImageName(), "L"));
                }
            }
        }

        // 판매자 정보
        Seller seller = sellerService.getSellerByIdPreview(item.getSellerId());
        seller.decrypt(sellerEncryptor, false);

        model.addAttribute("itemInfo", item);
        model.addAttribute("sellerInfo", seller);
		return "view";
	}

    /**
	 * 리뷰 리스트 엑셀 다운로드
	 * @param itemParam
	 * @return
	 */
	@SuppressWarnings({ "finally", "resource" })
	@GetMapping("/review/download-excel")
	public ModelAndView searchReviewListExcelDownload(
			ItemParam itemParam,
			RedirectAttributes redirectAttributes,
			HttpSession httpSession
	) {
		// 답례품 조회 로직과 동일
		String today1 = DateUtils.getToday("yyyyMMdd");
		itemParam.setSearchStartDate(StringUtils.defaultIfEmpty(itemParam.getSearchStartDate(), today1));
		itemParam.setSearchEndDate(StringUtils.defaultIfEmpty(itemParam.getSearchEndDate(), today1));

		if (!UserUtils.isManagerLogin() && !UserUtils.isSellerLogin()) {
			redirectAttributes.addFlashAttribute("message", "로그인 상태가 아닙니다.");
			return new ModelAndView("redirect:/opmanager/login-main");
		}

		if (ShopUtils.isSellerPage()) {
			itemParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER));
			itemParam.setSellerId(SellerUtils.getSellerId());
			if (!org.springframework.util.StringUtils.hasLength(itemParam.getShLocgovCode())) {
				redirectAttributes.addFlashAttribute("message", "지자체 정보가 없습니다.");
				return new ModelAndView("redirect:/seller/");
			}
		} else if (ShopUtils.isOpmanagerPage() && !isUpperAdmin()) {
			itemParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			if (!org.springframework.util.StringUtils.hasLength(itemParam.getShLocgovCode())) {
				redirectAttributes.addFlashAttribute("message", "지자체 정보가 없습니다.");
				return new ModelAndView("redirect:/opmanager/");
			}
		} else if (ShopUtils.isOpmanagerPage() && isUpperAdmin()) {
			// 지자체 선택한 항목으로 조회
		} else {
			if (ShopUtils.isSellerPage()) {
				redirectAttributes.addFlashAttribute("message", "올바른 접근이 아닙니다.");
				return new ModelAndView("redirect:/seller/");
			} else {
				redirectAttributes.addFlashAttribute("message", "올바른 접근이 아닙니다.");
				return new ModelAndView("redirect:/opmanager/");
			}
		}

		// 스트리밍 방식의 엑셀 다운로들 위한 변수 설정(pageSize, offset)을 위한 Pagination 클래스 사용
		// 변수에 대한 custom은 itemService 에서 수행
		int reviewCount = itemService.getItemReviewCountByParam(itemParam);

		Pagination pagination = Pagination.getInstance(reviewCount);
		itemParam.setPagination(pagination);

		// SXSSFWorkbook 인스턴스 생성 사유
		// - 엑셀 암호화로 인해 itemService에서 response를 종료 불가
		// - itemService에서 생성된 workbook을 controller통해 ItemReviewExcelView로 일괄 전송
		// - ItemReviewExcelView에서 엑셀 파일 생성 및 암호화, 파일 전송 일괄 수행
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		// 사용자 다운로드 파일 명
		// ALL_ITEM_REVIEW_20260115142019.xlsx
		String fileName
			= "답례품후기관리목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = itemService.streamReviewData(itemParam);
		} finally {
			/** ModelAndView에서 view이름이 아닌 객체(ItemReviewExcelView)를 담게된 경우,
			 * ModelAndView를 반환받은 DispatcherServlet에서 ViewResolver를 실행시키지 않고 객체를 실행
			 */
			// ItemReviewExcelView에 workbook과 암호화에 사용할 비밀번호를 담아서 전송
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}
}
