package saleson.shop.tempprocess;

import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.support.HeaderCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import saleson.common.enumeration.IdType;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.shop.log.ExceldownloadLogService;
import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.domain.RemittanceConfirmDetail;
import saleson.shop.remittance.support.RemittanceExcelView2;
import saleson.shop.remittance.support.RemittanceParam;
import saleson.shop.tempprocess.domain.HoldOrderListParam;
import saleson.shop.user.LocgovService;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.*;

@Controller
@RequestMapping("/opmanager/temp-process")
@RequestProperty(template="opmanager", layout="default")
public class TempProcessManagerController {

	private static final Logger log = LoggerFactory.getLogger(TempProcessManagerController.class);

	@Autowired
	private LocgovService locgovService;	// 지자체 관리

	@Autowired
	private TempProcessService tempProcessService;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private ExceldownloadLogService exceldownloadLogService;



	/**
	 * 주문 내역
	 * @return
	 */
	@GetMapping("order/all")
	public String orderList(@ModelAttribute HoldOrderListParam holdOrderListParam, Model model) {
		holdOrderListParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			holdOrderListParam.setConditionType("SELLER");
			holdOrderListParam.setSellerId(SellerUtils.getSellerId());

			String locgovCode = locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER);
			if (!StringUtils.isEmpty(locgovCode)) {
				holdOrderListParam.setShLocgovCode(locgovCode);
			} else {
				holdOrderListParam.setShLocgovCode("00000");
			}
		} else {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
			if (!StringUtils.isEmpty(locgovCode)) {
				holdOrderListParam.setShLocgovCode(locgovCode);
			}
		}

		if (ObjectUtils.isEmpty(holdOrderListParam.getSearchDateType())) {
			holdOrderListParam.setSearchDateType("OI.CREATED_DATE");
		}

		if (ObjectUtils.isEmpty(holdOrderListParam.getSearchStartDate())) {
			//orderParam.setSearchStartDate(DateUtils.getToday(Const.DATE_FORMAT));
			//orderParam.setSearchEndDate(DateUtils.getToday(Const.DATE_FORMAT));
		}

		model.addAttribute("list", tempProcessService.getTempOrderListByParam(holdOrderListParam));
		model.addAttribute("pagination", holdOrderListParam.getPagination());
		model.addAttribute("totalCount", holdOrderListParam.getPagination().getTotalItems());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return ViewUtils.getView("/temp-process/order/all");
	}


	/**
	 * 주문 처리
	 * @param orderCode
	 * @param model
	 * @return
	 */
	@PostMapping("order/all")
	public String orderModify(@ModelAttribute HoldOrderListParam holdOrderListParam, Model model) {
		try {
			tempProcessService.tempOrderListProcess(holdOrderListParam);
		} catch (OpRuntimeException e) {
			log.error("orderModify error =============", e);
			if (UserUtils.isSellerLogin()) {
				return ViewUtils.redirect("/seller/temp-process/order/all", e.getErrorMessage());
			} else {
				return ViewUtils.redirect("/opmanager/temp-process/order/all", e.getErrorMessage());
			}
		}

		model.addAttribute("holdOrderListParam", holdOrderListParam);

		if (UserUtils.isSellerLogin()) {
			return ViewUtils.redirect("/seller/temp-process/order/all", "처리되었습니다.");
		} else {
			return ViewUtils.redirect("/opmanager/temp-process/order/all", "처리되었습니다.");
		}
	}

	/**
	 * 주문 상세 (종합적인 주문 내역을 보여준다)
	 * @param orderCode
	 * @param model
	 * @return
	 */
	@GetMapping("order/order-detail/{rcOrderCode}")
	public String detail(@PathVariable("rcOrderCode") String rcOrderCode, Model model) {

		tempProcessService.setModelOrderDetail(model, rcOrderCode, "");

		return ViewUtils.getView("/temp-process/order/order-detail");
	}

	/**
	 * 정산 내역
	 * @return
	 */
	@GetMapping("remittance/list")
	public String remittanceList(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());

			String locgovCode = locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER);
			if (!StringUtils.isEmpty(locgovCode)) {
				remittanceParam.setShLocgovCode(locgovCode);
			} else {
				remittanceParam.setShLocgovCode("00000");
			}
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
		model.addAttribute("list", tempProcessService.getTempRemittanceConfirmListByParam(remittanceParam));
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		model.addAttribute("defaultOpmanagerSellerId", SellerUtils.DEFAULT_OPMANAGER_SELLER_ID);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

		return ViewUtils.getView("/temp-process/remittance/list");
	}
	
	@PostMapping("remittance/list")
	public String remittanceListPost(RemittanceParam remittanceParam, Model model, RequestContext requestContext) {
		return remittanceList(remittanceParam,model,requestContext);
	}

	/**
	 * 정산 마감 상세 내역
	 * @return
	 */
	@GetMapping("remittance/view/{remittanceId}")
	public String confirmDetailNew(@PathVariable("remittanceId") long remittanceId,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		if (!SecurityUtils.isLogin()) {
			throw new PageNotFoundException();
		}

		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());

			String locgovCode = locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER);
			if (!StringUtils.isEmpty(locgovCode)) {
				remittanceParam.setShLocgovCode(locgovCode);
			} else {
				remittanceParam.setShLocgovCode("00000");
			}
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

			}
		}

		remittanceParam.setRemittanceId(remittanceId);

		List<RemittanceConfirmDetail> list = tempProcessService.getRemittanceConfirmDetailListByParamNew(remittanceParam, false);

		Remittance remittance = tempProcessService.getRemittanceInfoById(remittanceId);

		Seller seller = sellerService.getSellerById(remittance.getSellerId());

		if (seller == null) {
			throw new PageNotFoundException();
		}

		model.addAttribute("seller", seller);
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("list", list);
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("remittanceStatusCode", remittance.getRemittanceStatusCode());

		return ViewUtils.getView("/temp-process/remittance/detail");

	}

	@PostMapping("remittance/view/{remittanceId}")
	public String searchConfirmDetailNew(@PathVariable("remittanceId") long remittanceId,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		if (!SecurityUtils.isLogin()) {
			throw new PageNotFoundException();
		}

		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());

			String locgovCode = locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER);
			if (!StringUtils.isEmpty(locgovCode)) {
				remittanceParam.setShLocgovCode(locgovCode);
			} else {
				remittanceParam.setShLocgovCode("00000");
			}
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

			}
		}

		remittanceParam.setRemittanceId(remittanceId);

		List<RemittanceConfirmDetail> list = tempProcessService.getRemittanceConfirmDetailListByParamNew(remittanceParam, false);

		Remittance remittance = tempProcessService.getRemittanceInfoById(remittanceId);

		Seller seller = sellerService.getSellerById(remittance.getSellerId());

		if (seller == null) {
			throw new PageNotFoundException();
		}

		model.addAttribute("seller", seller);
		model.addAttribute("queryString", requestContext.getQueryString());
		model.addAttribute("list", list);
		model.addAttribute("pagination", remittanceParam.getPagination());
		model.addAttribute("totalCount", remittanceParam.getPagination().getTotalItems());
		model.addAttribute("years", DateUtils.getToday("yyyy"));
		model.addAttribute("remittanceStatusCode", remittance.getRemittanceStatusCode());

		return ViewUtils.getView("/temp-process/remittance/detail");

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
	 * 정산 확정 확인처리
	 * @return
	 */
	@PostMapping("remittance/list-update")
	public String remittanceConfirmCheckProcess(RemittanceParam param, RequestContext requestContext, Model model) {
		if (!SecurityUtils.isManager() || "SYSTEM".equalsIgnoreCase(getAuth())) {
			if (SecurityUtils.isManager()) {
				return ViewUtils.redirect("/opmanager/temp-process/remittance/list", "권한이 없습니다.");
			} else {
				return ViewUtils.redirect("/seller/temp-process/remittance/list", "권한이 없습니다.");
			}
		}

		if (ShopUtils.isSellerPage()) {
			param.setSellerId(SellerUtils.getSellerId());

			String locgovCode = locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER);
			if (!StringUtils.isEmpty(locgovCode)) {
				param.setShLocgovCode(locgovCode);
			} else {
				param.setShLocgovCode("00000");
			}
		} else {
			if (SecurityUtils.hasRole("ROLE_ADMIN_5")
					|| SecurityUtils.hasRole("ROLE_ADMIN_6")
					|| SecurityUtils.hasRole("ROLE_ADMIN_7")
					|| SecurityUtils.hasRole("ROLE_ADMIN_8")) {
				String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER);
				if (!StringUtils.isEmpty(locgovCode)) {
					param.setShLocgovCode(locgovCode);
				} else {
					param.setShLocgovCode("00000");
				}
			} else if (SecurityUtils.hasRole("ROLE_ADMIN_1")
						|| SecurityUtils.hasRole("ROLE_ADMIN_2")
						|| SecurityUtils.hasRole("ROLE_ADMIN_3")
						|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {

			}
		}

		tempProcessService.remittanceConfirmProcess(param);

		return ViewUtils.redirect("/opmanager/temp-process/remittance/list", "확정처리 되었습니다.");
	}

	/**
	 * 정산 확정 내역 상세 - Item 엑셀
	 * @return
	 */
	@GetMapping("remittance/view-excel/{remittanceId}")
	public ModelAndView confirmDetailNewExcel(@PathVariable("remittanceId") long remittanceId,
		RemittanceParam remittanceParam, Model model, RequestContext requestContext) {

		// 엑셀 권한이 없는 경우
		if(!SecurityUtils.hasRole("ROLE_EXCEL") && !SecurityUtils.hasRole("ROLE_OPMANAGER")){
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/temp-process/remittance/view/" + remittanceId, "엑셀 다운로드 권한이 없습니다.");
		}

		if (ShopUtils.isSellerPage()) {
			remittanceParam.setSellerId(SellerUtils.getSellerId());

			String locgovCode = locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER);
			if (!StringUtils.isEmpty(locgovCode)) {
				remittanceParam.setShLocgovCode(locgovCode);
			} else {
				remittanceParam.setShLocgovCode("00000");
			}
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

			}
		}

		remittanceParam.setRemittanceId(remittanceId);

		List<RemittanceConfirmDetail> list = tempProcessService.getRemittanceConfirmDetailListByParamNew(remittanceParam, true);

		if (list == null || list.isEmpty()) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/temp-process/remittance/view/" + remittanceId, "조회된 내역이 없습니다.");
		}

		if (!exceldownloadLogService.existExcelAccessLog(makeUrl(requestContext.isOpmanagerPage(), "/temp-process/remittance/view-excel/" + remittanceId))) {
			return excelDownloadRedirect(requestContext.isOpmanagerPage(), "/temp-process/remittance/view/" + remittanceId, "엑셀 다운로드 사유 정보가 없습니다.");
		}

		HeaderCell[] headerCells = new HeaderCell[]{
				new HeaderCell(300, 	"No",	""),
				new HeaderCell(3000, 	"결제일",	""),
				new HeaderCell(4000, 	"주문번호",	""),
				new HeaderCell(4000, 	"답례품번호",	""),
				new HeaderCell(10000, 	"답례품명", 	""),
				new HeaderCell(10000, 	"옵션", 	""),
				new HeaderCell(1000, 	"주문자명", 	""),
				new HeaderCell(3000, 	"판매가", 	""),
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
			remittanceExcel.add(DateUtils.date(remittanceConfirmDetail.getPayDate()));
			remittanceExcel.add(remittanceConfirmDetail.getOrderCode());
			remittanceExcel.add(remittanceConfirmDetail.getItemUserCode());
			remittanceExcel.add(remittanceConfirmDetail.getItemName());
			remittanceExcel.add(ShopUtils.viewItemOptions(remittanceConfirmDetail.getSetItemFlag(), remittanceConfirmDetail.getOptions()));
			remittanceExcel.add(remittanceConfirmDetail.getBuyerName());
			if (remittanceConfirmDetail.getCommissionBasePrice() > 0) {
				remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getCommissionBasePrice() * remittanceConfirmDetail.getQuantity() + remittanceConfirmDetail.getEtcAmt()));
			} else {
				remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getSalePrice() * remittanceConfirmDetail.getQuantity() + remittanceConfirmDetail.getEtcAmt()));
			}
			remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getQuantity()) + "개");
			remittanceExcel.add(StringUtils.numberFormat(remittanceConfirmDetail.getRemittancePrice() * remittanceConfirmDetail.getQuantity() + remittanceConfirmDetail.getEtcAmt()));

			excelList.add(remittanceExcel);
		}

		ModelAndView mav = new ModelAndView(new RemittanceExcelView2("정산미정_상세조회"));

		mav.addObject("headerCells", headerCells);
		mav.addObject("remittanceExcelList", excelList);
		mav.addObject("title", "정산 미정내역 상세목록");

		return mav;
	}

}
