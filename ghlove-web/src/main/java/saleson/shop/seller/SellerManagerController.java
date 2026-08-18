package saleson.shop.seller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.common.enumeration.IdType;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.seller.main.support.SellerListParam;
import saleson.seller.main.support.SellerParam;
import saleson.seller.user.SellerUserService;
import saleson.shop.shadowlogin.ShadowLoginLogService;
import saleson.shop.user.LocgovService;
import saleson.shop.user.UserService;
import saleson.shop.user.support.UserSearchParam;
@Controller
@RequestMapping("/opmanager/seller")
@RequestProperty(title="입점업체관리", layout="default", template="opmanager")
public class SellerManagerController {
	@Autowired
	private SellerService sellerService;

	@Autowired
	private SellerUserService sellerUserService;

	@Autowired
	private UserService userService;

	@Autowired
	private ShadowLoginLogService shadowLoginLogService;

	@Autowired
	private LocgovService locgovService;	// 지자체 관리

	/**
	 * 판매관리자 메인.
	 * @return
	 */
	@GetMapping("list")
	public String list(SellerParam sellerParam, Model model, RequestContext requestContext) {

		String today = DateUtils.getToday("yyyyMMdd");
		sellerParam.setStartDate(StringUtils.defaultIfEmpty(sellerParam.getStartDate(), today));
		sellerParam.setEndDate(StringUtils.defaultIfEmpty(sellerParam.getEndDate(), today));

		// 관리자인 경우
		if (UserUtils.isManagerLogin()) {
			// 지자체관리자일 경우 지자체코드 필요
			sellerParam.setLocgov(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}


		sellerParam.setAdminRole(locgovService.getLoginUserAdminRoleCheck());
		if(sellerParam.getShWdr() != null && sellerParam.getShWdr() != "") {
			sellerParam.setLocgov(sellerParam.getShWdr());
		}
		if(sellerParam.getShLocgovCode() != null && sellerParam.getShLocgovCode() != "") {
			sellerParam.setLocgov(sellerParam.getShLocgovCode());
		}
		Pagination pagination = Pagination.getInstance(0);

		sellerParam.setPagination(pagination);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("totalCount", 0);
		model.addAttribute("sellerSearchParam",sellerParam);
		model.addAttribute("pagination",pagination);
		model.addAttribute("adminRole", sellerParam.getAdminRole());
		//return "view:/opmanager/seller/list";
		return "view";
	}

	@PostMapping("list")
	public String listPost(SellerParam sellerParam, Model model, RequestContext requestContext) {
		String today = DateUtils.getToday("yyyyMMdd");
		sellerParam.setStartDate(StringUtils.defaultIfEmpty(sellerParam.getStartDate(), today));
		sellerParam.setEndDate(StringUtils.defaultIfEmpty(sellerParam.getEndDate(), today));

		// 관리자인 경우
		if (UserUtils.isManagerLogin()) {
			// 지자체관리자일 경우 지자체코드 필요
			sellerParam.setLocgov(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}


		sellerParam.setAdminRole(locgovService.getLoginUserAdminRoleCheck());
		if(sellerParam.getShWdr() != null && sellerParam.getShWdr() != "") {
			sellerParam.setLocgov(sellerParam.getShWdr());
		}
		if(sellerParam.getShLocgovCode() != null && sellerParam.getShLocgovCode() != "") {
			sellerParam.setLocgov(sellerParam.getShLocgovCode());
		}
		Pagination pagination = Pagination.getInstance(sellerService.getSellerCount(sellerParam));

		sellerParam.setPagination(pagination);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("list",sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("totalCount", sellerParam.getPagination().getTotalItems());
		model.addAttribute("sellerSearchParam",sellerParam);
		model.addAttribute("pagination",pagination);
		model.addAttribute("adminRole", sellerParam.getAdminRole());
		//return "view:/opmanager/seller/list";
		return "view";
	}


	/**
	 * 입점업체등록
	 * @param model
	 * @return
	 */
	@GetMapping("create")
	public String create(Model model, RequestContext requestContext) {

		Seller seller =  new Seller();
		seller.setShippingFlag("N");

		model.addAttribute("telCodes", CodeUtils.getCodeList("TEL"));
		model.addAttribute("phoneCodes", CodeUtils.getCodeList("PHONE"));
		model.addAttribute("emailCodes", CodeUtils.getCodeList("EMAIL"));
		model.addAttribute("bankCodes", CodeUtils.getCodeList("CJ_BANK_LIST"));
		model.addAttribute("remittanceTypeCodes", CodeUtils.getCodeList("REMITTANCE_TYPE"));
		model.addAttribute("seller", seller);
		model.addAttribute("mode", "create");
		model.addAttribute("action", "/opmanager/seller/create");
		model.addAttribute("userLocgovCodeDetails", locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId()));

		return ViewUtils.view();
	}


	/**
	 * 아이디 생성
	 * @param requestContext
	 * @param model
	 * @param locgovCode
	 * @return
	 */
	@PostMapping("/makeSellerId")
	public JsonView makeSellerId(RequestContext requestContext, Model model
			, @RequestParam("locgovCode") String locgovCode) {

		Seller seller = new Seller();
		seller.setLocgovNm(locgovCode);

		String loginId = sellerService.makeSellerId(seller);

		return JsonViewUtils.success(loginId);
	}

	/**
	 * 입점업체등록 처리
	 * @param seller
	 * @return
	 */
	@PostMapping("create")
	public String createAction(Seller seller, RequestContext requestContext) {

		seller.setCreatedUserId(UserUtils.getManagerId());
		seller.setLocgovCode(locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId()).getId());	// 지자체코드

		sellerService.insertSeller(seller);
		return ViewUtils.redirect("/opmanager/seller/list", MessageUtils.getMessage("M00288"));	// 등록되었습니다.
	}

	/**
	 * 입점업체 수정
	 * @param sellerId
	 * @param model
	 * @return
	 */
	@GetMapping("edit/{sellerId}")
	@RequestProperty(title="입점업체 수정")
	public String updateSeller(@PathVariable("sellerId") long sellerId, Model model, RequestContext requestContext) {

		Seller seller = sellerService.getSellerById(sellerId);

		// 국번이 없는 경우 Default 값 추가
		seller.parseTelephoneNumber();

		model.addAttribute("telCodes", CodeUtils.getCodeList("TEL"));
		model.addAttribute("phoneCodes", CodeUtils.getCodeList("PHONE"));
		model.addAttribute("emailCodes", CodeUtils.getCodeList("EMAIL"));
		model.addAttribute("bankCodes", CodeUtils.getCodeList("CJ_BANK_LIST"));
		model.addAttribute("remittanceTypeCodes", CodeUtils.getCodeList("REMITTANCE_TYPE"));
		model.addAttribute("seller", seller);
		model.addAttribute("mode", "edit");
		model.addAttribute("action", "/opmanager/seller/edit/" + sellerId);
		model.addAttribute("userLocgovCodeDetails", locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId()));
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return ViewUtils.getView("/seller/form");
	}

	/**
	 * 입점업체 수정처리
	 * @param sellerId
	 * @param seller
	 * @param model
	 * @return
	 */
	@PostMapping("edit/{sellerId}")
	public String updateSellerAction(@PathVariable("sellerId") long sellerId, Seller seller, Model model, RequestContext requestContext) {

		seller.setSellerId(sellerId);
		seller.setLocgovCode(locgovService.getLoginUserLocgovCodeDetails(requestContext.getUser().getUserId()).getId());	// 지자체코드
		sellerService.updateSeller(seller);

		return ViewUtils.redirect("/opmanager/seller/list", MessageUtils.getMessage("M00289"));	// 수정되었습니다.
	}

	/**
	 * 입점업체 아이디 유무
	 * @param requestContext
	 * @param seller
	 * @return
	 */
	@PostMapping("id-duplicate-Check")
	public JsonView idDuplicateCheck(RequestContext requestContext, Seller seller) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}

		Seller selectSeller = sellerService.getSellerByLoginId(seller.getLoginId());
		boolean isExist = sellerUserService.isDuplicateSellerUserByLoginId(seller.getLoginId());

		if (selectSeller != null) {
			isExist = true;
		}

		//model.addAttribute("isExist",isExist);

		//return JsonViewUtils.success();

		if (!isExist) {
			return JsonViewUtils.success();
		} else {
			return JsonViewUtils.exception(MessageUtils.getMessage("M00161"));

		}

	}

	/**
	 * 입점업체 삭제
	 * @param seller
	 * @return
	 */
	@PostMapping("delete")
	public String deleteSeller(Seller seller) {

		sellerService.deleteSeller(seller);

		return ViewUtils.redirect("/opmanager/seller/list", MessageUtils.getMessage("M00205")); // 삭제되었습니다.
	}

	/**
	 * 업체 일괄삭제
	 * @param requestContext
	 * @param SellerListParam
	 * @return
	 */
	@PostMapping("list/delete")
	public JsonView deleteItemData(RequestContext requestContext, SellerListParam sellerListParam) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		sellerService.deleteSellerList(sellerListParam);
		return JsonViewUtils.success(MessageUtils.getMessage("M00205"));
	}

	/**
	 * 판매자 shadow로그인
	 * @param seller
	 * @return
	 */
	@GetMapping("shadow-login")
	public String shadhowLogin(Seller seller) {

		HttpSession session = RequestContextUtils.getSession();

		//개인정보이력을 위한 세션 로그인아이디
		session.setAttribute("OP_LAST_USERNAME", UserUtils.getUser().getLoginId());
		Seller loginInfo = sellerService.getSellerById(seller.getSellerId());
		loginInfo.setShadowLoginLogId(shadowLoginLogService.insertShadowLoginLog("seller", loginInfo.getSellerId(), UserUtils.getManagerId()));
		session.setAttribute("SHADOW_SELLER", loginInfo);

		return ViewUtils.redirect("/seller");
	}

	/*
	 * 회원검색 (팝업창 검색)
	 */
	@GetMapping("find-md")
	@RequestProperty(layout = "base")
	public String searchUser(Model model, UserSearchParam userSearchParam) {

		int userCount = 0;
		List<User> userList = new ArrayList<>();
		Pagination pagination = null;

		if ("mdId".equals(userSearchParam.getTargetId()) || "managerUserId".equals(userSearchParam.getTargetId())) {
			//userSearchParam.setAuthority("ROLE_OPMANAGER");
			userSearchParam.setConditionType("FIND_MD");	// MD만 검색
			userSearchParam.setAuthority("ROLE_MD");
			userCount = userService.getManagerCount(userSearchParam);

			pagination = Pagination.getInstance(userCount);
			userSearchParam.setPagination(pagination);

			userList = userService.getManagerList(userSearchParam);

		} else {
			userSearchParam.setAuthority("ROLE_USER");
			userCount = userService.getUserCount(userSearchParam);

			pagination = Pagination.getInstance(userCount);
			userSearchParam.setPagination(pagination);

			userList = userService.getUserList(userSearchParam);
		}

		model.addAttribute("pagination", pagination);
		model.addAttribute("userList", userList);
		model.addAttribute("userCount", userCount);

		return ViewUtils.view();
	}

	/**
	 * (팝업) 판매자정보 변경 로그 목록 조회
	 * @param searchParam
	 * @param sellerId
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="log")
	@GetMapping("popup/log/{sellerId}")
	public String logList(SellerParam searchParam, @PathVariable("sellerId") long sellerId, Model model) {
		/*
		searchParam.setSellerId(sellerId);

		int sellerLogCount = sellerService.getSellerLogCount(searchParam);

		// 페이징
		Pagination pagination = Pagination.getInstance(sellerLogCount);
		searchParam.setPagination(pagination);

		model.addAttribute("list", sellerService.getSellerLogList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("sellerLogCount", sellerLogCount);
		model.addAttribute("bankCodes", hspCodeService.getHspSubCodeList("04"));	// 은행코드
		*/
		return ViewUtils.getView("/seller/popup/log-list");
	}

	/**
	 * 판매자검색.
	 * @param sellerParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("find")
	public String findSeller(SellerParam sellerParam, Model model) {

		Pagination pagination = Pagination.getInstance(sellerService.getSellerCount(sellerParam));

		sellerParam.setPagination(pagination);

		model.addAttribute("list",sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("totalCount", sellerParam.getPagination().getTotalItems());
		model.addAttribute("sellerSearchParam",sellerParam);
		model.addAttribute("pagination",pagination);

		return ViewUtils.getView("/seller/popup/find-seller");
	}

	/**
	 * 판매자검색.
	 * @param sellerParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("find-popup/{targetId}")
	public String findSellerPopup(SellerParam sellerParam,
			@PathVariable("targetId") String targetId,
			Model model) {

		Pagination pagination = Pagination.getInstance(sellerService.getSellerCount(sellerParam));

		sellerParam.setPagination(pagination);

		model.addAttribute("targetId", targetId);
		model.addAttribute("list",sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("totalCount", sellerParam.getPagination().getTotalItems());
		model.addAttribute("sellerSearchParam",sellerParam);
		model.addAttribute("pagination",pagination);

		return ViewUtils.getView("/seller/popup/find-seller");
	}

	/**
	 * 비밀번호 수정.
	 * @return
	 */
	@GetMapping("/password-change-popup")
	@RequestProperty(template="seller", layout="base")
	public String passwordChangePopup(Model model, @RequestParam("sellerId") String sellerId) {

		model.addAttribute("seller", sellerService.getSellerById(Long.parseLong(sellerId)));
		return "view:/user/passwordPopup";
	}

	/**
	 * 비밀번호 수정처리.
	 * @return
	 */
	@PostMapping("/password-change")
	public String passwordChangeAction(Model model, Seller seller) {
		seller.setSellerId(seller.getSellerId());
		seller.setUpdatedUserId(seller.getSellerId());
		sellerService.updateSellerPassword(seller);

		return ViewUtils.redirect("/opmanager/seller/password-change-popup?sellerId="+seller.getSellerId(), MessageUtils.getMessage("M00289"), "self.close();");
	}

	/**
	 * 판매자 비밀번호 초기화
	 * @return
	 */
	@PostMapping("/init-seller-password")
	@ResponseBody
	public String initSellerPassword(Seller seller) {
		seller.setSellerId(seller.getSellerId());
		seller.setUpdatedUserId(seller.getSellerId());
		sellerService.initSellerPassword(seller);

		return "S";
	}


	/**
	 * 첨부파일 삭제
	 * @param fileName
	 * @return
	 */
	@PostMapping("/deleteFile")
	public JsonView deleteFile(@RequestParam("fileName") String fileName,
			@RequestParam("sellerId") String sellerId,
			@RequestParam("fileType") String fileType,
			Seller seller) {


		seller.setSellerId(Long.parseLong(sellerId));
		seller.setDelelteFile(fileType);

		sellerService.deleteFile(seller, fileName);

		return JsonViewUtils.success();
	}

	/*
	 * 회원검색 (팝업창 검색)
	 */
	@PostMapping("find-md")
	@RequestProperty(layout = "base")
	public String searchUserPost(Model model, UserSearchParam userSearchParam) {
		return searchUser(model, userSearchParam);
	}

	/**
	 * 판매자검색.
	 * @param sellerParam
	 * @param model
	 * @return
	 */
	@RequestProperty(layout="base")
	@PostMapping("find")
	public String findSellerPost(SellerParam sellerParam, Model model) {
		return findSeller(sellerParam, model);
	}
}
