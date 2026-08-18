package saleson.shop.remittance;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import saleson.common.Const;
import saleson.common.enumeration.IdType;
import saleson.common.file.ExcelDownloadView;
import saleson.common.userauth.UserAuthService;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.shop.log.ExceldownloadLogService;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.domain.RemittanceConfirm;
import saleson.shop.remittance.domain.RemittanceConfirmDetail;
import saleson.shop.remittance.domain.RemittanceDetail;
import saleson.shop.remittance.domain.RemittanceExpected;
import saleson.shop.remittance.support.RemittanceExcelView2;
import saleson.shop.remittance.support.RemittanceException;
import saleson.shop.remittance.support.RemittanceFileSupport;
import saleson.shop.remittance.support.RemittanceParam;
import saleson.shop.user.LocgovService;

@Controller
@RequestMapping("/opmanager/remittance")
@RequestProperty(template="opmanager", layout="default")
public class RemittanceManagerController {
	private static final Logger log = LoggerFactory.getLogger(RemittanceManagerController.class);

	@Autowired
	private RemittanceService remittanceService;

	@Autowired
	private SellerService sellerService;

//	@Autowired
//	private SequenceService sequenceService;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private ExceldownloadLogService exceldownloadLogService;

	@Autowired
	private UserAuthService userAuthService;


	/**
	 * 정산 예정 내역
	 * @return
	 */
	@GetMapping("expected/list")
	public String expectedList(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		if (ShopUtils.isSellerPage()) {
			String locgovCode = locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER);
			if (!StringUtils.isEmpty(locgovCode)) {
				remittanceParam.setShLocgovCode(locgovCode);
			} else {
				remittanceParam.setShLocgovCode("00000");
			}
			remittanceParam.setShLocgovCode(locgovCode);
			remittanceParam.setSellerId(SellerUtils.getSellerId());
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					remittanceParam.setShLocgovCode(locgovCode);
				} else {
					remittanceParam.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

			} else {
				remittanceParam.setShLocgovCode("00000");
			}
		}

		LocalDate nowDate = LocalDate.now();

		String startDateStr = remittanceParam.getStartDate() + "01";
		String endDateStr = remittanceParam.getEndDate() + "01";

		LocalDate startDate = LocalDate.parse(startDateStr.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate endDate = LocalDate.parse(endDateStr.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));

		if (nowDate.getYear() < startDate.getYear() || nowDate.getYear() < endDate.getYear()) {
			return ViewUtils.view("이번달까지 조회 가능합니다.");
		} else if (nowDate.getYear() == startDate.getYear() && nowDate.getMonthValue() < startDate.getMonthValue()) {
			return ViewUtils.view("이번달까지 조회 가능합니다.");
		} else if (nowDate.getYear() == endDate.getYear() && nowDate.getMonthValue() < endDate.getMonthValue()) {
			return ViewUtils.view("이번달까지 조회 가능합니다.");
		}

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);
//		model.addAttribute("expectedDate", expectedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		model.addAttribute("confirmDate", nowDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		return "view:/remittance/expected/list";

	}

	@PostMapping("expected/list")
	public String expectedListPost(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		if (ShopUtils.isSellerPage()) {
//			throw new PageNotFoundException();
			String locgovCode = locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER);
			if (!StringUtils.isEmpty(locgovCode)) {
				remittanceParam.setShLocgovCode(locgovCode);
			} else {
				remittanceParam.setShLocgovCode("00000");
			}
			remittanceParam.setShLocgovCode(locgovCode);
			remittanceParam.setSellerId(SellerUtils.getSellerId());
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					remittanceParam.setShLocgovCode(locgovCode);
				} else {
					remittanceParam.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

			} else {
				remittanceParam.setShLocgovCode("00000");
			}
		}

		LocalDate nowDate = LocalDate.now();
//		LocalDate expectedDate;
//		if (nowDate.getDayOfMonth() > 15) {			// 15일 이후일 경우 다음달 15일로 표시
//			nowDate = nowDate.plusMonths(1);
//		}
//		expectedDate = LocalDate.of(nowDate.getYear(), nowDate.getMonth(), 15);

		String startDateStr = remittanceParam.getStartDate() + "01";
		String endDateStr = remittanceParam.getEndDate() + "01";

		LocalDate startDate = LocalDate.parse(startDateStr.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate endDate = LocalDate.parse(endDateStr.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));

		if (nowDate.getYear() < startDate.getYear() || nowDate.getYear() < endDate.getYear()) {
			return ViewUtils.view("이번달까지 조회 가능합니다.");
		} else if (nowDate.getYear() == startDate.getYear() && nowDate.getMonthValue() < startDate.getMonthValue()) {
			return ViewUtils.view("이번달까지 조회 가능합니다.");
		} else if (nowDate.getYear() == endDate.getYear() && nowDate.getMonthValue() < endDate.getMonthValue()) {
			return ViewUtils.view("이번달까지 조회 가능합니다.");
		}

		model.addAttribute("queryString", requestContext.getQueryString());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("list", remittanceService.getRemittanceExpectedListByParam(remittanceParam));
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);
//		model.addAttribute("expectedDate", expectedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		model.addAttribute("confirmDate", nowDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		return "view:/remittance/expected/list";
	}

	/**
	 * 정산 예정 내역 수정 - 확정 기능
	 * @return
	 */
	@PostMapping("expected/list/update")
	public String expectedListProcess(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		if (ShopUtils.isSellerPage()) {
			throw new PageNotFoundException();
		}

		String redirect = "/opmanager/remittance/expected/list";
		if (ObjectUtils.isEmpty(requestContext.getQueryString()) == false) {
			redirect += "?" + requestContext.getQueryString();
		}

		try {
//			// 상품 확정
//			remittanceService.updateRemittanceItemExpectedForList(remittanceParam);
//
//			// 배송비 확정
//			remittanceService.updateRemittanceShippingExpectedForList(remittanceParam);
//
//			// 추가금 확정
//			remittanceService.updateRemittanceAddPaymentExpectedForList(remittanceParam);

			// 확정상태에서 정산테이블 추가하도록 수정
			remittanceService.expectedListProcessNew(remittanceParam);
		} catch (RemittanceException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
//			return ViewUtils.redirect(e.getRedirectUrl() + "?" + requestContext.getQueryString(), e.getMessage());
			log.error("ERROR: {}", "========== expectedListProcess RemittanceException ===========", e);
			return ViewUtils.view(e.getReturnMsg());
		}

		return ViewUtils.redirect(redirect, "수정되었습니다.");

	}

	/**
	 * 정산 예정 내역 상세 - Item
	 * @return
	 */
	@GetMapping("expected/detail/item/{sellerId}/{startDate}/{endDate}")
	public String itemExpectedDetail(@PathVariable("sellerId") long sellerId,
		@PathVariable("startDate") String startDate,
		@PathVariable("endDate") String endDate,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		/*
		 * if (ShopUtils.isSellerPage()) { throw new PageNotFoundException(); }
		 */

		if (!SecurityUtils.isLogin()) {
			throw new PageNotFoundException();
		}
		if (SellerUtils.isSellerLogin()) {
			sellerId = SellerUtils.getSellerId();
		}
		Seller seller = sellerService.getSellerById(sellerId);
		if (seller == null) {
			throw new PageNotFoundException();
		}

//		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//		LocalDate startDateObj = LocalDate.parse(startDate, dateTimeFormatter);
//
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(Date.valueOf(startDateObj));
//		int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
//		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//
//		String year = String.valueOf(startDateObj.getYear());
//		String month = startDateObj.getMonthValue() < 10 ? "0" + String.valueOf(startDateObj.getMonthValue()) : String.valueOf(startDateObj.getMonthValue());
//
//		remittanceParam.setStartDate(year + month + "0" + String.valueOf(firstDay));
//		remittanceParam.setEndDate(year + month + String.valueOf(lastDay));
//		remittanceParam.setSellerId(sellerId);

		remittanceParam.setStartDate(startDate);
		remittanceParam.setEndDate(endDate);
		remittanceParam.setSellerId(sellerId);

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("seller", seller);
		model.addAttribute("list", remittanceService.getRemittanceItemExpectedDetailListByParam(remittanceParam));
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());

		return "view:/remittance/expected/item-detail";

	}

	/**
	 * 정산 예정 내역 상세 수정 - Item
	 * @return
	 */
	/*
	 * @PostMapping("expected/detail/item/{sellerId}/{startDate}/{endDate}") public
	 * String itemExpectedProcess(@PathVariable("sellerId") long sellerId,
	 *
	 * @PathVariable("startDate") String startDate,
	 *
	 * @PathVariable("endDate") String endDate, RemittanceParam remittanceParam,
	 * RequestContext requestContext) {
	 *
	 * if (ShopUtils.isSellerPage()) { throw new PageNotFoundException(); }
	 *
	 * Seller seller = sellerService.getSellerById(sellerId); if (seller == null) {
	 * throw new PageNotFoundException(); }
	 *
	 * remittanceParam.setStartDate(startDate); remittanceParam.setEndDate(endDate);
	 * remittanceParam.setSellerId(sellerId);
	 *
	 * String[] checkTypes = new String[]{"update", "confirm"}; if
	 * (!ArrayUtils.contains(checkTypes, remittanceParam.getConditionType())) {
	 * throw new PageNotFoundException(); }
	 *
	 * try { remittanceService.updateRemittanceItemExpected(remittanceParam); }
	 * catch(RemittanceException e) { // log.error("ERROR: {}", e.getMessage(), e);
	 * log.error("ERROR: {}",
	 * "========== itemExpectedProcess RemittanceException ==========="); String
	 * redirect = e.getRedirectUrl(); if
	 * (ObjectUtils.isEmpty(requestContext.getQueryString()) == false) { redirect +=
	 * "?" + requestContext.getQueryString(); }
	 *
	 * // return ViewUtils.redirect(redirect, e.getMessage()); return
	 * ViewUtils.redirect(redirect, MessageUtils.getMessage("실패했습니다.")); // 실패했습니다.
	 * }
	 *
	 * String redirect = "/opmanager/remittance/expected/detail/item/" + sellerId +
	 * "/" + startDate + "/" + endDate; if
	 * (ObjectUtils.isEmpty(requestContext.getQueryString()) == false) { redirect +=
	 * "?" + requestContext.getQueryString(); }
	 *
	 * return ViewUtils.redirect(redirect, "수정되었습니다.");
	 *
	 * }
	 */

	/**
	 * 정산 예정 내역 상세 - Shipping
	 * @return
	 */
	/*
	 * @GetMapping("expected/detail/shipping/{sellerId}/{startDate}/{endDate}")
	 * public String shippingExpectedDetail(@PathVariable("sellerId") long sellerId,
	 *
	 * @PathVariable("startDate") String startDate,
	 *
	 * @PathVariable("endDate") String endDate, RemittanceParam remittanceParam,
	 * Model model, RequestContext requestContext) {
	 *
	 * if (ShopUtils.isSellerPage()) { throw new PageNotFoundException(); }
	 *
	 * Seller seller = sellerService.getSellerById(sellerId); if (seller == null) {
	 * throw new PageNotFoundException(); }
	 *
	 * remittanceParam.setStartDate(startDate); remittanceParam.setEndDate(endDate);
	 * remittanceParam.setSellerId(sellerId);
	 *
	 * model.addAttribute("queryString", requestContext.getQueryString());
	 * model.addAttribute("seller", seller); model.addAttribute("list",
	 * remittanceService.getRemittanceShippingExpectedDetailListByParam(
	 * remittanceParam)); model.addAttribute("pagination",
	 * remittanceParam.getPagination()); model.addAttribute("totalCount",
	 * remittanceParam.getPagination().getTotalItems());
	 *
	 * return "view:/remittance/expected/shipping-detail";
	 *
	 * }
	 */

	/**
	 * 정산 예정 내역 상세 수정 - Shipping
	 * @return
	 */
	/*
	 * @PostMapping("expected/detail/shipping/{sellerId}/{startDate}/{endDate}")
	 * public String shippingExpectedProcess(@PathVariable("sellerId") long
	 * sellerId,
	 *
	 * @PathVariable("startDate") String startDate,
	 *
	 * @PathVariable("endDate") String endDate, RemittanceParam remittanceParam,
	 * RequestContext requestContext) {
	 *
	 * if (ShopUtils.isSellerPage()) { throw new PageNotFoundException(); }
	 *
	 * Seller seller = sellerService.getSellerById(sellerId); if (seller == null) {
	 * throw new PageNotFoundException(); }
	 *
	 * remittanceParam.setStartDate(startDate); remittanceParam.setEndDate(endDate);
	 * remittanceParam.setSellerId(sellerId);
	 *
	 * String[] checkTypes = new String[]{"update", "confirm"}; if
	 * (!ArrayUtils.contains(checkTypes, remittanceParam.getConditionType())) {
	 * throw new PageNotFoundException(); }
	 *
	 * try { remittanceService.updateRemittanceShippingExpected(remittanceParam); }
	 * catch(RemittanceException e) { // log.error("ERROR: {}", e.getMessage(), e);
	 * String redirect = e.getRedirectUrl(); if
	 * (ObjectUtils.isEmpty(requestContext.getQueryString()) == false) { redirect +=
	 * "?" + requestContext.getQueryString(); }
	 *
	 * return ViewUtils.redirect(redirect, "문제가 발생했습니다."); }
	 *
	 * String redirect = "/opmanager/remittance/expected/detail/shipping/" +
	 * sellerId + "/" + startDate + "/" + endDate; if
	 * (ObjectUtils.isEmpty(requestContext.getQueryString()) == false) { redirect +=
	 * "?" + requestContext.getQueryString(); }
	 *
	 * return ViewUtils.redirect(redirect, "수정되었습니다.");
	 *
	 * }
	 */

	/**
	 * 정산 예정 내역 상세 - Shipping
	 * @return
	 */
	/*
	 * @GetMapping("expected/detail/add-payment/{sellerId}/{startDate}/{endDate}")
	 * public String addPaymentExpectedDetail(@PathVariable("sellerId") long
	 * sellerId,
	 *
	 * @PathVariable("startDate") String startDate,
	 *
	 * @PathVariable("endDate") String endDate, RemittanceParam remittanceParam,
	 * Model model, RequestContext requestContext) {
	 *
	 * if (ShopUtils.isSellerPage()) { throw new PageNotFoundException(); }
	 *
	 * Seller seller = sellerService.getSellerById(sellerId); if (seller == null) {
	 * throw new PageNotFoundException(); }
	 *
	 * remittanceParam.setStartDate(startDate); remittanceParam.setEndDate(endDate);
	 * remittanceParam.setSellerId(sellerId);
	 *
	 * model.addAttribute("queryString", requestContext.getQueryString());
	 * model.addAttribute("seller", seller); model.addAttribute("list",
	 * remittanceService.getRemittanceAddPaymentExpectedDetailListByParam(
	 * remittanceParam)); model.addAttribute("pagination",
	 * remittanceParam.getPagination()); model.addAttribute("totalCount",
	 * remittanceParam.getPagination().getTotalItems());
	 *
	 * return "view:/remittance/expected/add-payment-detail";
	 *
	 * }
	 */

	/**
	 * 정산 예정 내역 상세 수정 - Shipping
	 * @return
	 */
	/*
	 * @PostMapping("expected/detail/add-payment/{sellerId}/{startDate}/{endDate}")
	 * public String addPaymentExpectedProcess(@PathVariable("sellerId") long
	 * sellerId,
	 *
	 * @PathVariable("startDate") String startDate,
	 *
	 * @PathVariable("endDate") String endDate, RemittanceParam remittanceParam,
	 * RequestContext requestContext) {
	 *
	 * if (ShopUtils.isSellerPage()) { throw new PageNotFoundException(); }
	 *
	 * Seller seller = sellerService.getSellerById(sellerId); if (seller == null) {
	 * throw new PageNotFoundException(); }
	 *
	 * remittanceParam.setStartDate(startDate); remittanceParam.setEndDate(endDate);
	 * remittanceParam.setSellerId(sellerId);
	 *
	 * String[] checkTypes = new String[]{"update", "confirm"}; if
	 * (!ArrayUtils.contains(checkTypes, remittanceParam.getConditionType())) {
	 * throw new PageNotFoundException(); }
	 *
	 * try { remittanceService.updateRemittanceAddPaymentExpected(remittanceParam);
	 * } catch(RemittanceException e) { // log.error("ERROR: {}", e.getMessage(),
	 * e); String redirect = e.getRedirectUrl(); if
	 * (ObjectUtils.isEmpty(requestContext.getQueryString()) == false) { redirect +=
	 * "?" + requestContext.getQueryString(); }
	 *
	 * return ViewUtils.redirect(redirect, "문제가 발생했습니다."); }
	 *
	 * String redirect = "/opmanager/remittance/expected/detail/add-payment/" +
	 * sellerId + "/" + startDate + "/" + endDate; if
	 * (ObjectUtils.isEmpty(requestContext.getQueryString()) == false) { redirect +=
	 * "?" + requestContext.getQueryString(); }
	 *
	 * return ViewUtils.redirect(redirect, "수정되었습니다.");
	 *
	 * }
	 */

	/**
	 * 정산 확정 내역
	 * @return
	 */
	@GetMapping("confirm/list")
	public String confirmList(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					remittanceParam.setShLocgovCode(locgovCode);
				} else {
					remittanceParam.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

			} else {
				remittanceParam.setShLocgovCode("00000");
			}
		}

		model.addAttribute("queryString", requestContext.getQueryString());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

		return "view:/remittance/confirm/list";

	}

	@PostMapping("confirm/list")
	public String confirmListPost(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					remittanceParam.setShLocgovCode(locgovCode);
				} else {
					remittanceParam.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

			} else {
				remittanceParam.setShLocgovCode("00000");
			}
		}

		model.addAttribute("queryString", requestContext.getQueryString());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
		model.addAttribute("list", remittanceService.getRemittanceConfirmListByParam(remittanceParam));
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

		return "view:/remittance/confirm/list";
	}

	/**
	 * 정산 마감
	 * @return
	 */
	/*
	 * @PostMapping("confirm/list/update") public String
	 * remittanceFinishingProcess(RemittanceParam param, RequestContext
	 * requestContext) {
	 *
	 * if (ShopUtils.isSellerPage()) { throw new PageNotFoundException(); }
	 *
	 * if (param.getId() != null) { for(String key : param.getId()) { String[] temp
	 * = StringUtils.delimitedListToStringArray(key, "^^^"); if (temp.length != 2) {
	 * continue; }
	 *
	 * FinishingRemittance finishingRemittance =
	 * param.getFinishingRemittanceMap().get(key); if (finishingRemittance == null)
	 * { continue; }
	 *
	 * param.setSellerId(Integer.parseInt(temp[0])); param.setStartDate(temp[1]);
	 *
	 * RemittanceConfirm item =
	 * remittanceService.getRemittanceConfirmByParam(param); if (item == null) {
	 * continue; }
	 *
	 * // if (SellerUtils.getSellerId() != param.getSellerId()) { // throw new
	 * PageNotFoundException(); // }
	 *
	 * if (finishingRemittance.getAmount() != item.getItemRemittanceAmount() +
	 * item.getShippingTotalAmount() + item.getAddPaymentTotalAmount()) { continue;
	 * }
	 *
	 * param.setConfirmAmount(finishingRemittance.getAmount());
	 *
	 * try { remittanceService.remittanceFinishingProcess(param); } catch
	 * (RemittanceException e) { // log.error("ERROR: {}", e.getMessage(), e);
	 * return ViewUtils.redirect(e.getRedirectUrl(), "문제가 발생했습니다."); } } }
	 *
	 * String redirect = "/seller/remittance/confirm/list"; if
	 * (!ObjectUtils.isEmpty(requestContext.getQueryString())) { redirect += "?" +
	 * requestContext.getQueryString(); }
	 *
	 * return ViewUtils.redirect(redirect, "수정되었습니다."); }
	 */

	// 정산확정 상세화면 팝업
	@RequestProperty(layout="base")
	@GetMapping(value="/confirm/popup")
	public String confirmPop(Model model, HttpServletRequest request) {
//		if (!ShopUtils.isSellerPage()) {
//			throw new PageNotFoundException();
//		}

		return ViewUtils.getView("/remittance/confirm/file-upload");
	}

	/**
	 * 정산 확정 확인처리
	 * @return
	 */
	@PostMapping("confirm/list/check")
	@ResponseBody
//	public String remittanceConfirmCheckProcess(RemittanceParam param, RequestContext requestContext) {
	public JsonView remittanceConfirmCheckProcess(@RequestBody RemittanceParam param, HttpServletRequest request, RequestContext requestContext) {

//		if (!ShopUtils.isSellerPage()) {
//			throw new PageNotFoundException();
//		}
//
//		param.setSellerId(SellerUtils.getSellerId());
//		// 상품 확정
//		remittanceService.remittanceConfirmCheckProcess(param);
//
//		String redirect = "/seller/remittance/confirm/list";
//		/*
//		 * if (!ObjectUtils.isEmpty(requestContext.getQueryString())) { redirect += "?"
//		 * + requestContext.getQueryString(); }
//		 */
//
//		return ViewUtils.redirect(redirect, "확인처리 되었습니다.");

		if (param.getRemittanceId() == 0) {
			return JsonViewUtils.failure("정산정보가 없습니다.");
		}
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
//		if (!requestContext.isSellerPage()) {
//			return JsonViewUtils.failure("권한이 없습니다.");
//		}

		param.setSellerId(SellerUtils.getSellerId());
		try {
			remittanceService.remittanceConfirmCheckProcess(param);
			return JsonViewUtils.success("정산확정 확인 처리 되었습니다.");
		} catch (RemittanceException e) {
			return JsonViewUtils.failure("문제가 발생했습니다.");
		}
	}

	/**
	 * 정산 확정 내역 상세 - Item
	 * @return
	 */
	/*
	 * @GetMapping("confirm/detail/{viewType}/{sellerId}/{startDate}") public String
	 * confirmDetail(@PathVariable("sellerId") long sellerId,
	 *
	 * @PathVariable("viewType") String viewType,
	 *
	 * @PathVariable("startDate") String startDate, RemittanceParam remittanceParam,
	 * Model model, RequestContext requestContext) {
	 *
	 * if (ShopUtils.isSellerPage()) { sellerId = SellerUtils.getSellerId(); }
	 *
	 * Seller seller = sellerService.getSellerById(sellerId); if (seller == null) {
	 * throw new PageNotFoundException(); }
	 *
	 * remittanceParam.setViewTarget("NO_CANCEL"); if ("cancel".equals(viewType)) {
	 * remittanceParam.setViewTarget("CANCEL"); }
	 *
	 * remittanceParam.setStartDate(startDate);
	 * remittanceParam.setSellerId(sellerId);
	 *
	 * // List<RemittanceConfirmDetail> list =
	 * remittanceService.getRemittanceConfirmDetailListByParam(remittanceParam);
	 * List<RemittanceConfirmDetail> list =
	 * remittanceService.getRemittanceConfirmDetailListByParamNew(remittanceParam);
	 *
	 * model.addAttribute("seller", seller); model.addAttribute("queryString",
	 * requestContext.getQueryString()); model.addAttribute("list", list);
	 * model.addAttribute("pagination", remittanceParam.getPagination());
	 * model.addAttribute("totalCount",
	 * remittanceParam.getPagination().getTotalItems()); model.addAttribute("years",
	 * DateUtils.getToday("yyyy")); String remittanceStatusCode = ""; if (list !=
	 * null && !list.isEmpty()) { remittanceStatusCode =
	 * list.get(0).getRemittanceStatusCode(); }
	 * model.addAttribute("remittanceStatusCode", remittanceStatusCode);
	 *
	 * return "view:/remittance/confirm/detail";
	 *
	 * }
	 */

	/**
	 * 정산 확정 내역 상세 - Item
	 * @return
	 */
	@GetMapping("confirm/detailNew/{viewType}/{remittanceId}")
	public String confirmDetailNew(@PathVariable("remittanceId") long remittanceId,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		if (!SecurityUtils.isLogin()) {
			throw new PageNotFoundException();
		}

		remittanceParam.setRemittanceId(remittanceId);

//		List<RemittanceConfirmDetail> list = remittanceService.getRemittanceConfirmDetailListByParam(remittanceParam);
		List<RemittanceConfirmDetail> list = remittanceService.getRemittanceConfirmDetailListByParamNew(remittanceParam);

		Remittance remittance = remittanceService.getRemittanceInfoById(remittanceId);

		Seller seller = sellerService.getSellerById(remittance.getSellerId());

		if (seller == null) {
			throw new PageNotFoundException();
		}
//		remittanceParam.setStartDate(list.get(0).getRemittanceDate());

		model.addAttribute("seller", seller);
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("list", list);
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("remittanceStatusCode", remittance.getRemittanceStatusCode());

		return "view:/remittance/confirm/detail";

	}

	@PostMapping("confirm/detailNew/{viewType}/{remittanceId}")
	public String searchConfirmDetailNew(@PathVariable("remittanceId") long remittanceId,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		if (!SecurityUtils.isLogin()) {
			throw new PageNotFoundException();
		}

		remittanceParam.setRemittanceId(remittanceId);

//		List<RemittanceConfirmDetail> list = remittanceService.getRemittanceConfirmDetailListByParam(remittanceParam);
		List<RemittanceConfirmDetail> list = remittanceService.getRemittanceConfirmDetailListByParamNew(remittanceParam);

		Remittance remittance = remittanceService.getRemittanceInfoById(remittanceId);

		Seller seller = sellerService.getSellerById(remittance.getSellerId());

		if (seller == null) {
			throw new PageNotFoundException();
		}
//		remittanceParam.setStartDate(list.get(0).getRemittanceDate());

		model.addAttribute("seller", seller);
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("list", list);
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("remittanceStatusCode", remittance.getRemittanceStatusCode());

		return "view:/remittance/confirm/detail";

	}

	/**
	 * 정산 마감 내역
	 * @return
	 */
	@GetMapping("finish/list")
	public String finishingList(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());
			remittanceParam.setConditionType("SELLER_LIST");
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					remittanceParam.setShLocgovCode(locgovCode);
				} else {
					remittanceParam.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

			} else {
				remittanceParam.setShLocgovCode("00000");
			}
		}

		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", 0);
		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

		return "view:/remittance/finishing/list";

	}

	@PostMapping("finish/list")
	public String finishingListPost(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());
			remittanceParam.setConditionType("SELLER_LIST");
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					remittanceParam.setShLocgovCode(locgovCode);
				} else {
					remittanceParam.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

			} else {
				remittanceParam.setShLocgovCode("00000");
			}
		}

		model.addAttribute("queryString", requestContext.getQueryString());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
//		model.addAttribute("list", remittanceService.getRemittanceFinishingListByParam(remittanceParam));
		model.addAttribute("list", remittanceService.getRemittanceFinishingListByParamNew(remittanceParam));
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

		return "view:/remittance/finishing/list";
	}

	/**
	 * 정산 마감 상세 내역
	 * @return
	 */
	@GetMapping("finish/detail/{sellerId}/{remittanceId}")
	public String finishingDetail(@PathVariable("remittanceId") int remittanceId,
		@PathVariable("sellerId") long sellerId,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		/*
		 * if (ShopUtils.isSellerPage()) { sellerId = SellerUtils.getSellerId(); }
		 */

		if (!SecurityUtils.isLogin()) {
			throw new PageNotFoundException();
		}

		Seller seller = sellerService.getSellerById(sellerId);
		if (seller == null) {
			throw new PageNotFoundException();
		}

		model.addAttribute("seller", seller);
		model.addAttribute("queryString", requestContext.getQueryString());
//		model.addAttribute("list", remittanceService.getRemittanceFinishingDetailListByParam(remittanceParam));
		model.addAttribute("list", remittanceService.getRemittanceFinishingDetailListByParamNew(remittanceParam));
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		remittanceParam.setSellerId(sellerId);
		remittanceParam.setRemittanceId(remittanceId);
		model.addAttribute("remittanceConfirmDate", remittanceService.selectRemittanceDateForDetail(remittanceParam).getRemittanceDate());

		return "view:/remittance/finishing/detail";

	}

	@PostMapping("finish/detail/{sellerId}/{remittanceId}")
	public String searchFinishingDetail(@PathVariable("remittanceId") int remittanceId,
		@PathVariable("sellerId") long sellerId,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		/*
		 * if (ShopUtils.isSellerPage()) { sellerId = SellerUtils.getSellerId(); }
		 */

		if (!SecurityUtils.isLogin()) {
			throw new PageNotFoundException();
		}

		Seller seller = sellerService.getSellerById(sellerId);
		if (seller == null) {
			throw new PageNotFoundException();
		}

		model.addAttribute("seller", seller);
		model.addAttribute("queryString", requestContext.getQueryString());
//		model.addAttribute("list", remittanceService.getRemittanceFinishingDetailListByParam(remittanceParam));
		model.addAttribute("list", remittanceService.getRemittanceFinishingDetailListByParamNew(remittanceParam));
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		remittanceParam.setSellerId(sellerId);
		remittanceParam.setRemittanceId(remittanceId);
		model.addAttribute("remittanceConfirmDate", remittanceService.selectRemittanceDateForDetail(remittanceParam).getRemittanceDate());

		return "view:/remittance/finishing/detail";

	}

	/**
	 * 정산 예정 내역 상세 엑셀 다운로드
	 * @param sellerId
	 * @param viewType
	 * @param startDate
	 * @param remittanceParam
	 * @return
	 */
	/*
	 * @GetMapping(
	 * "confirm/detail/{viewType}/{sellerId}/{startDate}/excel-download") public
	 * ModelAndView confirmDetail_excel(@PathVariable("sellerId") long sellerId,
	 *
	 * @PathVariable("viewType") String viewType,
	 *
	 * @PathVariable("startDate") String startDate, RemittanceParam remittanceParam)
	 * {
	 *
	 * if (!SecurityUtils.hasRole("ROLE_EXCEL") &&
	 * !SecurityUtils.hasRole("ROLE_SUPERVISOR")) { throw new
	 * UserException("엑셀 다운로드 권한이 없습니다."); }
	 *
	 * ModelAndView mav = new ModelAndView(new RemittanceConfirmDetailExeclView());
	 *
	 * remittanceParam.setViewTarget("NO_CANCEL"); if ("cancel".equals(viewType)) {
	 * remittanceParam.setViewTarget("CANCEL"); }
	 *
	 * remittanceParam.setStartDate(startDate);
	 * remittanceParam.setSellerId(sellerId);
	 *
	 * mav.addObject("remittanceConfirmDetail",
	 * remittanceService.getRemittanceConfirmDetailListByParam(remittanceParam));
	 *
	 * return mav;
	 *
	 * }
	 */

	/**
	 * 정산 마감 상세 내역 엑셀 다운로드
	 * @param remittanceId
	 * @param sellerId
	 * @param remittanceParam
	 * @return
	 */
	/*
	 * @GetMapping("finish/detail/{sellerId}/{remittanceId}/excel-download") public
	 * ModelAndView finishingDetail_excel(@PathVariable("remittanceId") int
	 * remittanceId,
	 *
	 * @PathVariable("sellerId") long sellerId, RemittanceParam remittanceParam) {
	 *
	 * if(!SecurityUtils.hasRole("ROLE_EXCEL") &&
	 * !SecurityUtils.hasRole("ROLE_SUPERVISOR")){ throw new
	 * UserException("엑셀 다운로드 권한이 없습니다."); }
	 *
	 * ModelAndView mav = new ModelAndView(new RemittanceFinishDetailExeclView());
	 *
	 * remittanceParam.setRemittanceId(remittanceId);
	 * remittanceParam.setSellerId(sellerId);
	 *
	 * mav.addObject("remittanceDetail",
	 * remittanceService.getRemittanceFinishingDetailListByParam(remittanceParam));
	 *
	 * return mav;
	 *
	 * }
	 */


	/**
	 * 정산 지급 처리
	 * @return
	 */
	@PostMapping("confirm/list/updateNew")
	public String updateRemittancePayProcess(RemittanceParam param, RequestContext requestContext) {

		if (!SecurityUtils.isManager()) {
			throw new PageNotFoundException();
		}

		long errCnt = 0;
		// 지자체 주/부 담당자만 처리
		if (SecurityUtils.hasRole("ROLE_ADMIN_5")
				|| SecurityUtils.hasRole("ROLE_ADMIN_6")
				|| SecurityUtils.hasRole("ROLE_ADMIN_7")
				|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
			if (param.getId() != null && param.getId().length > 0) {
				for(String remittanceId : param.getId()) {
					RemittanceParam data = new RemittanceParam();
					data.setLocgovCode(locgovCode);
					data.setRemittanceId(Long.valueOf(remittanceId));
					try {
						remittanceService.updateRemittancePayProcess(data);
					} catch (RemittanceException e) {
//						log.error("ERROR: {}", e.getMessage(), e);
//						return ViewUtils.redirect(e.getRedirectUrl(), e.getMessage());
						errCnt++;
					}
				}
			}
		}

		String redirect = "/seller/remittance/confirm/list";
		if (SecurityUtils.isManager()) {
			redirect = "/opmanager/remittance/confirm/list";
		}
		if (!ObjectUtils.isEmpty(requestContext.getQueryString())) {
			redirect += "?" + requestContext.getQueryString();
		}
		if (errCnt > 0) {
			return ViewUtils.redirect(redirect, "처리 중 " + errCnt + "건이 문제가 발생했습니다.");
		}
		return ViewUtils.redirect(redirect, "수정되었습니다.");
	}

	/**
	 * 입금 처리 확인 (정산 마감)
	 * @return
	 */
	@PostMapping("finish/list/check")
	public String checkFinishingList(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());
		} else {
			throw new PageNotFoundException();
		}

		remittanceService.updateRemittanceFinishingProcessNew(remittanceParam);

//		model.addAttribute("queryString", requestContext.getQueryString());
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(new SellerParam("SELLER_LIST_FOR_SELECTBOX")));
//		model.addAttribute("list", remittanceService.getRemittanceFinishingListByParam(remittanceParam));
//		model.addAttribute("pagination", remittanceParam.getPagination());
//		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
//		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);
//		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
//		model.addAttribute("years", DateUtils.getToday("yyyy"));
//		model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
//
//		return "view:/remittance/finishing/list";
		return finishingList(remittanceParam, model, requestContext);
	}

	/**
	 * 정산 관련 파일 조회
	 * @return
	 */
	@PostMapping("confirm/file-list")
	@ResponseBody
	public JsonView getFileList(@RequestBody RemittanceParam param, RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		if (param.getRemittanceId() == 0) {
			return JsonViewUtils.failure("정산정보가 없습니다.");
		}
		// 지자체 주/부 담당자만 처리
		if (ShopUtils.isOpmanagerPage() && (SecurityUtils.hasRole("ROLE_ADMIN_5")
				|| SecurityUtils.hasRole("ROLE_ADMIN_6")
				|| SecurityUtils.hasRole("ROLE_ADMIN_7")
				|| SecurityUtils.hasRole("ROLE_ADMIN_8"))) {
			param.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));
			if (StringUtils.isEmpty(param.getLocgovCode())) {
				return JsonViewUtils.failure("자치구 정보가 없습니다.");
			}
		} else if (ShopUtils.isSellerPage()) {
			param.setSellerId(SellerUtils.getSellerId());
		} else {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		RemittanceFileSupport result;
		try {
			result = remittanceService.getRemittanceFileList(param);
		} catch (RemittanceException e) {
			log.error("RemittanceManagerController getFileList error", e);
			return JsonViewUtils.failure(e.getReturnMsg());
		}
		return JsonViewUtils.success(result);
	}

	/**
	 * 정산 관련 파일 업로드
	 * @return
	 */
	@PostMapping("confirm/upload-file")
	@ResponseBody
	public JsonView uploadFile(RemittanceParam param, MultipartHttpServletRequest request, RequestContext requestContext) {
		if (param.getRemittanceId() == 0) {
			return JsonViewUtils.failure("정산정보가 없습니다.");
		}
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
//		if (!requestContext.isSellerPage()) {
//			return JsonViewUtils.failure("권한이 없습니다.");
//		}

		param.setSellerId(SellerUtils.getSellerId());

		MultiValueMap<String, MultipartFile> fileMap = request.getMultiFileMap();

		List<MultipartFile> files = fileMap.get("addFile");

		RemittanceFileSupport result = null;
		try {
			if (requestContext.isSellerPage()) {
				result = remittanceService.addRemittanceFiles(param, files);
			} else {
				throw new RemittanceException("", "올바른 접근 경로가 아닙니다.");
			}
		} catch (RemittanceException e) {
			log.error("RemittanceManagerController getFileList error", e);
			return JsonViewUtils.failure(e.getReturnMsg());
		}
		return JsonViewUtils.success(result);
	}

	/**
	 * 첨부파일 다운로드
	 * @return
	 */
	@GetMapping("confirm/file/{remittanceId}/{fileSeq}")
	@ResponseBody
	public ResponseEntity<byte[]> downloadRemittanceFile(@PathVariable("remittanceId") long remittanceId
			, @PathVariable("fileSeq") long fileSeq, Model model, RequestContext requestContext) {
		RemittanceParam param = new RemittanceParam();
		param.setRemittanceId(remittanceId);

		if (requestContext.isSellerPage()) {
			param.setSellerId(SellerUtils.getSellerId());
		} else {
			param.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));
		}

		if (fileSeq > 0) {
			param.setFileSeq(fileSeq);
			return remittanceService.fileDownload(param);
		} else {
			throw new NullPointerException("파일 정보가 없습니다.");
		}

	}

	/**
	 * 첨부파일 삭제
	 * @return
	 */
	@GetMapping("confirm/delete-file/{remittanceId}/{fileSeq}")
	@ResponseBody
	public JsonView deleteRemittanceFile(@PathVariable("remittanceId") long remittanceId
			, @PathVariable("fileSeq") long fileSeq, Model model, RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		RemittanceParam param = new RemittanceParam();
		param.setRemittanceId(remittanceId);
		param.setFileSeq(fileSeq);
		if (requestContext.isSellerPage()) {
			param.setSellerId(SellerUtils.getSellerId());
		} else {
			param.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));
		}

		return JsonViewUtils.success(remittanceService.deleteRemittanceFile(param));
	}

	/**
	 * 첨부파일 다운로드
	 * @return
	 */
	@GetMapping("confirm/allFileDownload/{remittanceId}/{uploadDate}")
	@ResponseBody
	public ResponseEntity<byte[]> allFileDownload(@PathVariable("remittanceId") long remittanceId
			, @PathVariable("uploadDate") String uploadDate, Model model, RequestContext requestContext) {
		if (!SecurityUtils.isManager()) {
			throw new PageNotFoundException();
		}
		RemittanceParam param = new RemittanceParam();
		param.setRemittanceId(remittanceId);
		param.setUploadDate(uploadDate);
		if (requestContext.isSellerPage()) {
			param.setSellerId(SellerUtils.getSellerId());
		} else {
			param.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));
		}

		return remittanceService.allFileDownload(param);
	}


	/**
	 * 정산 예정 내역 엑셀
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping("expected/list-excel")
	public ModelAndView expectedListExcel(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		String queryString = "";
		if (requestContext.getQueryString() != null) {
			queryString = "?" + requestContext.getQueryString();
		}
		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/list" + queryString, "엑셀 다운로드 권한이 없습니다.");
		}

		if (ShopUtils.isSellerPage()) {
			String locgovCode = locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER);
			if (!StringUtils.isEmpty(locgovCode)) {
				remittanceParam.setShLocgovCode(locgovCode);
			} else {
				remittanceParam.setShLocgovCode("00000");
			}
			remittanceParam.setShLocgovCode(locgovCode);
			remittanceParam.setSellerId(SellerUtils.getSellerId());
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					remittanceParam.setShLocgovCode(locgovCode);
				} else {
					remittanceParam.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			} else {
				remittanceParam.setShLocgovCode("00000");
			}
		}

		LocalDate nowDate = LocalDate.now();

		String startDateStr = remittanceParam.getStartDate() + "01";
		String endDateStr = remittanceParam.getEndDate() + "01";

		LocalDate startDate = LocalDate.parse(startDateStr.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate endDate = LocalDate.parse(endDateStr.substring(0, 8), DateTimeFormatter.ofPattern("yyyyMMdd"));

		if (nowDate.getYear() < startDate.getYear() || nowDate.getYear() < endDate.getYear()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/list" + queryString, "이번달까지 조회 가능합니다.");
		} else if (nowDate.getYear() == startDate.getYear() && nowDate.getMonthValue() < startDate.getMonthValue()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/list" + queryString, "이번달까지 조회 가능합니다.");
		} else if (nowDate.getYear() == endDate.getYear() && nowDate.getMonthValue() < endDate.getMonthValue()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/list" + queryString, "이번달까지 조회 가능합니다.");
		}

		if (!exceldownloadLogService.existExcelAccessLog(makeUrl(requestContext.isOpmanagerPage(), "/remittance/expected/list-excel"))) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/list" + queryString, "엑셀 다운로드 사유 정보가 없습니다.");
		}

		remittanceParam.setPage(1);
		remittanceParam.setItemsPerPage(1);

		List<RemittanceExpected> list = remittanceService.getRemittanceExpectedListByParam(remittanceParam);

		if (list == null || list.isEmpty()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/list" + queryString, "조회된 내역이 없습니다.");
		}
		// 엑셀 다운로드
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "정산예정내역_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = remittanceService.streamRemittanceExpectedData(remittanceParam);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 정산 예정 내역 상세 - Item 엑셀
	 * @return
	 */
	@GetMapping("expected/detail/item-excel")
	public ModelAndView itemExpectedDetailExcel(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		long sellerId = remittanceParam.getSellerId();
		String startDate = remittanceParam.getStartDate();
		String endDate = remittanceParam.getEndDate();

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/detail/item/" + sellerId + "/" + startDate + "/" + endDate, "엑셀 다운로드 권한이 없습니다.");
		}

		if (SellerUtils.isSellerLogin()) {
			sellerId = SellerUtils.getSellerId();
		}
		Seller seller = sellerService.getSellerById(sellerId);
		if (seller == null) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/list", "올바른 접근이 아닙니다.");
		}

		remittanceParam.setStartDate(startDate);
		remittanceParam.setEndDate(endDate);
		remittanceParam.setSellerId(sellerId);

		remittanceParam.setPage(1);
		remittanceParam.setItemsPerPage(Integer.MAX_VALUE);

		if (!exceldownloadLogService.existExcelAccessLog(makeUrl(requestContext.isOpmanagerPage(), "/remittance/expected/detail/item-excel"))) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/detail/item/" + sellerId + "/" + startDate + "/" + endDate, "엑셀 다운로드 사유 정보가 없습니다.");
		}

		List<OrderItem> list = remittanceService.getRemittanceItemExpectedDetailListByParam(remittanceParam);

		if (list == null || list.isEmpty()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/expected/detail/item/" + sellerId + "/" + startDate + "/" + endDate, "조회된 내역이 없습니다.");
		}

		HeaderCell[] headerCells = new HeaderCell[]{
				new HeaderCell(300, 	"No",	""),
				new HeaderCell(3000, 	"결제일",	""),
				new HeaderCell(3000, 	"구매확정일",	""),
				new HeaderCell(4000, 	"주문번호",	""),
				new HeaderCell(4000, 	"답례품번호",	""),
				new HeaderCell(10000, 	"답례품명", 	""),
				new HeaderCell(10000, 	"옵션", 	""),
				new HeaderCell(500, 	"구분", 		""),
				new HeaderCell(3000, 	"판매가(1개)", 	""),
				new HeaderCell(2000, 	"주문수량", 	""),
				new HeaderCell(3000, 	"정산금액", 	""),
				new HeaderCell(3000, 	"정산예정일", 	"")
		};

		List<List<String>> excelList = new ArrayList<>();

//		long idx = 0;
		long idx = list.size();
		for (OrderItem orderItem : list) {
			List<String> remittanceExcel = new ArrayList<>();
//			remittanceExcel.add(StringUtils.numberFormat(++idx));
			remittanceExcel.add(StringUtils.numberFormat(idx--));
			remittanceExcel.add(DateUtils.datetime(orderItem.getPayDate()));
			remittanceExcel.add(DateUtils.datetime(orderItem.getConfirmDate()));
			remittanceExcel.add(orderItem.getOrderCode());
			remittanceExcel.add(orderItem.getItemUserCode());
			remittanceExcel.add(orderItem.getItemName());
			remittanceExcel.add(ShopUtils.viewItemOptions(orderItem.getSetItemFlag(), orderItem.getOptions()));
			if ("1".equals(orderItem.getTaxType())) {
				remittanceExcel.add("과세");
			} else if ("2".equals(orderItem.getTaxType())) {
				remittanceExcel.add("면세");
			}
			if (orderItem.getCommissionBasePrice() > 0) {
//				remittanceExcel.add(StringUtils.numberFormat(orderItem.getCommissionBasePrice() * orderItem.getQuantity()));
				remittanceExcel.add(StringUtils.numberFormat(orderItem.getCommissionBasePrice()) + " P");
			} else {
//				remittanceExcel.add(StringUtils.numberFormat((orderItem.getSupplyPrice() - orderItem.getSellerDiscountPrice() - orderItem.getSellerPoint() - orderItem.getSetDiscountPrice())* orderItem.getQuantity()));
				remittanceExcel.add(StringUtils.numberFormat(orderItem.getSupplyPrice() - orderItem.getSellerDiscountPrice() - orderItem.getSellerPoint() - orderItem.getSetDiscountPrice()) + " P");
			}

			remittanceExcel.add(StringUtils.numberFormat(orderItem.getQuantity()) + " 개");
			remittanceExcel.add(StringUtils.numberFormat((orderItem.getSupplyPrice() - orderItem.getSellerDiscountPrice() - orderItem.getSellerPoint() - orderItem.getSetDiscountPrice())* orderItem.getQuantity()) + " 원");
			remittanceExcel.add(DateUtils.date(orderItem.getRemittanceExpectedDate()));

			excelList.add(remittanceExcel);
		}

		ModelAndView mav = new ModelAndView(new RemittanceExcelView2("정산예정_상세조회"));

		mav.addObject("headerCells", headerCells);
		mav.addObject("remittanceExcelList", excelList);
		mav.addObject("title", "정산 예정내역 상세목록");

		return mav;

	}

	/**
	 * 정산 확정 내역 엑셀
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping("confirm/list-excel")
	public ModelAndView confirmListExcel(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		String queryString = "";
		if (requestContext.getQueryString() != null) {
			queryString = "?" + requestContext.getQueryString();
		}
		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/confirm/list" + queryString, "엑셀 다운로드 권한이 없습니다.");
		}

		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					remittanceParam.setShLocgovCode(locgovCode);
				} else {
					remittanceParam.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

			} else {
				remittanceParam.setShLocgovCode("00000");
			}
		}

		if (!exceldownloadLogService.existExcelAccessLog(makeUrl(requestContext.isOpmanagerPage(), "/remittance/confirm/list-excel"))) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/confirm/list" + queryString, "엑셀 다운로드 사유 정보가 없습니다.");
		}

		remittanceParam.setPage(1);
		remittanceParam.setItemsPerPage(1);

		List<RemittanceConfirm> list = remittanceService.getRemittanceConfirmListByParam(remittanceParam);

		if (list == null || list.isEmpty()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/confirm/list" + queryString, "조회된 내역이 없습니다.");
		}
		// 엑셀 다운로드
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "정산확정내역_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = remittanceService.streamRemittanceConfirmedData(remittanceParam);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 정산 확정 내역 상세 - Item 엑셀
	 * @return
	 */
	@GetMapping("confirm/detailNew-excel/{viewType}/{remittanceId}")
	public ModelAndView confirmDetailNewExcel(@PathVariable("remittanceId") long remittanceId, @PathVariable("viewType") String viewType,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/confirm/detailNew/" + viewType + "/" + remittanceId, "엑셀 다운로드 권한이 없습니다.");
		}

		remittanceParam.setRemittanceId(remittanceId);
		remittanceParam.setPage(1);
		remittanceParam.setItemsPerPage(Integer.MAX_VALUE);

		if (!exceldownloadLogService.existExcelAccessLog(makeUrl(requestContext.isOpmanagerPage(), "/remittance/confirm/detailNew-excel/" + viewType + "/" + remittanceId))) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/confirm/detailNew/" + viewType + "/" + remittanceId, "엑셀 다운로드 사유 정보가 없습니다.");
		}

		List<RemittanceConfirmDetail> list = remittanceService.getRemittanceConfirmDetailListByParamNew(remittanceParam);

		if (list == null || list.isEmpty()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/confirm/detailNew/" + viewType + "/" + remittanceId, "조회된 내역이 없습니다.");
		}

		HeaderCell[] headerCells = new HeaderCell[]{
				new HeaderCell(300, 	"No",	""),
				new HeaderCell(3000, 	"결제일",	""),
				new HeaderCell(3000, 	"구매확정일",	""),
				new HeaderCell(4000, 	"주문번호",	""),
				new HeaderCell(4000, 	"답례품번호",	""),
				new HeaderCell(10000, 	"답례품명", 	""),
				new HeaderCell(10000, 	"옵션", 	""),
				new HeaderCell(1000, 	"주문자명", 	""),
				new HeaderCell(3000, 	"판매가(1개)", 	""),
				new HeaderCell(2000, 	"주문수량", 	""),
				new HeaderCell(3000, 	"정산금액", 	"")
		};

		List<List<String>> excelList = new ArrayList<>();

//		long idx = 0;
		long idx = list.size();
		for (RemittanceConfirmDetail remittanceConfirmDetail : list) {
			List<String> remittanceExcel = new ArrayList<>();
//			remittanceExcel.add(StringUtils.numberFormat(++idx));
			remittanceExcel.add(StringUtils.numberFormat(idx--));
			remittanceExcel.add(DateUtils.datetime(remittanceConfirmDetail.getPayDate()));
			remittanceExcel.add(DateUtils.datetime(remittanceConfirmDetail.getConfirmDate()));
			remittanceExcel.add(remittanceConfirmDetail.getOrderCode());
			remittanceExcel.add(remittanceConfirmDetail.getItemUserCode());
			remittanceExcel.add(remittanceConfirmDetail.getItemName());
			remittanceExcel.add(ShopUtils.viewItemOptions(remittanceConfirmDetail.getSetItemFlag(), remittanceConfirmDetail.getOptions()));
			remittanceExcel.add(remittanceConfirmDetail.getBuyerName());
			if (remittanceConfirmDetail.getCommissionBasePrice() > 0) {
//				remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getCommissionBasePrice() * remittanceConfirmDetail.getQuantity() + remittanceConfirmDetail.getEtcAmt()) + " P");
				remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getCommissionBasePrice()) + " P");
			} else {
//				remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getSalePrice() * remittanceConfirmDetail.getQuantity() + remittanceConfirmDetail.getEtcAmt()) + " P");
				remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getSalePrice()) + " P");
			}
			remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getQuantity()) + " 개");
			remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getRemittancePrice() * remittanceConfirmDetail.getQuantity() + remittanceConfirmDetail.getEtcAmt()) + " 원");

			excelList.add(remittanceExcel);
		}

		ModelAndView mav = new ModelAndView(new RemittanceExcelView2("정산확정_상세조회"));

		mav.addObject("headerCells", headerCells);
		mav.addObject("remittanceExcelList", excelList);
		mav.addObject("title", "정산 확정내역 상세목록");

		return mav;
	}

	/**
	 * 정산 마감 내역 엑셀
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping("finish/list-excel")
	public ModelAndView finishingListExcel(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		String queryString = "";
		if (requestContext.getQueryString() != null) {
			queryString = "?" + requestContext.getQueryString();
		}

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/finish/list" + queryString, "엑셀 다운로드 권한이 없습니다.");
		}

		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());
			remittanceParam.setConditionType("SELLER_LIST");
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					remittanceParam.setShLocgovCode(locgovCode);
				} else {
					remittanceParam.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

			} else {
				remittanceParam.setShLocgovCode("00000");
			}
		}

		remittanceParam.setPage(1);
		remittanceParam.setItemsPerPage(1);

		if (!exceldownloadLogService.existExcelAccessLog(makeUrl(requestContext.isOpmanagerPage(), "/remittance/finish/list-excel"))) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/finish/list" + queryString, "엑셀 다운로드 사유 정보가 없습니다.");
		}

		List<Remittance> list = remittanceService.getRemittanceFinishingListByParamNew(remittanceParam);

		if (list == null || list.isEmpty()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/finish/list" + queryString, "조회된 내역이 없습니다.");
		}
		// 엑셀 다운로드
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "정산마감내역_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = remittanceService.streamRemittanceFinishedData(remittanceParam);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 정산 마감 상세 내역 엑셀
	 * @return
	 */
	@GetMapping("finish/detail-excel/{sellerId}/{remittanceId}")
	public ModelAndView finishingDetailExcel(@PathVariable("remittanceId") int remittanceId,
		@PathVariable("sellerId") long sellerId,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		String queryString = "";
		if (requestContext.getQueryString() != null) {
			queryString = "?" + requestContext.getQueryString();
		}

		if (!SecurityUtils.isLogin()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/finish/detail/" + sellerId + "/" + remittanceId + queryString, "엑셀 다운로드 권한이 없습니다.");
		}

		Seller seller = sellerService.getSellerById(sellerId);
		if (seller == null) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/finish/detail/" + sellerId + "/" + remittanceId + queryString, "답례품제공자 정보가 없습니다.");
		}

		if (!exceldownloadLogService.existExcelAccessLog(makeUrl(requestContext.isOpmanagerPage(), "/remittance/finish/detail-excel/" + sellerId + "/" + remittanceId))) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/finish/detail/" + sellerId + "/" + remittanceId + queryString, "엑셀 다운로드 사유 정보가 없습니다.");
		}

		remittanceParam.setItemsPerPage(Integer.MAX_VALUE);
		List<RemittanceDetail> list = remittanceService.getRemittanceFinishingDetailListByParamNew(remittanceParam);

		if (list == null || list.isEmpty()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/remittance/finish/detail/" + sellerId + "/" + remittanceId + queryString, "조회된 내역이 없습니다.");
		}

		HeaderCell[] headerCells = new HeaderCell[]{
				new HeaderCell(300, 	"No",	""),
				new HeaderCell(3000, 	"결제일",	""),
				new HeaderCell(3000, 	"주문확정일",	""),
				new HeaderCell(4000, 	"주문번호",	""),
				new HeaderCell(4000, 	"답례품번호",	""),
				new HeaderCell(10000, 	"답례품명", 	""),
				new HeaderCell(10000, 	"옵션", 	""),
				new HeaderCell(1000, 	"주문자명", 	""),
				new HeaderCell(3000, 	"판매가(1개)", 	""),
				new HeaderCell(2000, 	"주문수량", 	""),
				new HeaderCell(3000, 	"정산금액", 	"")
		};

		List<List<String>> excelList = new ArrayList<>();

		long idx = list.size();
		for (RemittanceDetail remittanceDetail : list) {
			List<String> remittanceExcel = new ArrayList<>();
			remittanceExcel.add(StringUtils.numberFormat(idx--));
			remittanceExcel.add(DateUtils.datetime(remittanceDetail.getPayDate()));
			remittanceExcel.add(DateUtils.datetime(remittanceDetail.getConfirmDate()));
			remittanceExcel.add(remittanceDetail.getOrderCode());
			remittanceExcel.add(remittanceDetail.getItemUserCode());
			remittanceExcel.add(remittanceDetail.getItemName());
			remittanceExcel.add(ShopUtils.viewItemOptions(remittanceDetail.getSetItemFlag(), remittanceDetail.getOptions()));
			remittanceExcel.add(remittanceDetail.getBuyerName());
			if (remittanceDetail.getCommissionBasePrice() > 0) {
				remittanceExcel.add(StringUtils.numberFormat(remittanceDetail.getCommissionBasePrice()+ remittanceDetail.getEtcAmt()) + " P");
			} else {
				remittanceExcel.add(StringUtils.numberFormat(remittanceDetail.getSalePrice() + remittanceDetail.getEtcAmt()) + " P");
			}
			remittanceExcel.add(StringUtils.numberFormat(remittanceDetail.getQuantity()) + " 개");
			remittanceExcel.add(StringUtils.numberFormat(remittanceDetail.getRemittancePrice() * remittanceDetail.getQuantity() + remittanceDetail.getEtcAmt()) + " 원");

			excelList.add(remittanceExcel);
		}

		ModelAndView mav = new ModelAndView(new RemittanceExcelView2("정산마감_상세조회"));

		mav.addObject("headerCells", headerCells);
		mav.addObject("remittanceExcelList", excelList);
		mav.addObject("title", "정산 마감내역 상세목록");

		return mav;
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

	private String getAuth() {
		if (SecurityUtils.hasRole("ROLE_ADMIN_1")
				|| SecurityUtils.hasRole("ROLE_ADMIN_2")
				|| SecurityUtils.hasRole("ROLE_ADMIN_3")
				|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			return "SYSTEM";
		} else if (SellerUtils.isSellerLogin()) {
			return "SELLER";
		}
		return "";
	}

	/**
	 * 정산 예정 내역 상세 - Item
	 * @return
	 */
	@PostMapping("expected/detail/item/{sellerId}/{startDate}/{endDate}")
	public String itemExpectedDetailPost(@PathVariable("sellerId") long sellerId,
		@PathVariable("startDate") String startDate,
		@PathVariable("endDate") String endDate,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		return itemExpectedDetail(sellerId, startDate, endDate, remittanceParam, model, requestContext);
	}

}
