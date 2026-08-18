package saleson.common.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.privacy.pCrypto;

import saleson.common.enumeration.PrivacyAccess;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.PointUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.cart.CartService;
import saleson.shop.cart.support.CartParam;
import saleson.shop.coupon.CouponService;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.give.givestate.GiveStateService;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.gnb.GnbService;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.log.PrivacyAccessLogHistService;
import saleson.shop.log.PrivacyAccessLogRepository;
import saleson.shop.log.domain.PrivacyAccessLog;
import saleson.shop.log.support.PrivacyLogHistParam;
import saleson.shop.main.MainService;
import saleson.shop.order.OrderService;
import saleson.shop.order.domain.BuyItem;
import saleson.shop.order.domain.ItemPrice;
import saleson.shop.point.PointService;
import saleson.shop.point.domain.AvailablePoint;
import saleson.shop.remittance.RemittanceService;
import saleson.shop.remittance.support.RemittanceParam;
import saleson.shop.shadowlogin.ShadowLoginLogService;
import saleson.shop.stats.StatsService;
import saleson.shop.storeinquiry.StoreInquiryService;
import saleson.shop.user.ManagerRequestService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.wishlist.WishlistService;



@Controller
@RequestMapping("/common/")
public class CommonController {
	private static final Logger log = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CartService cartService;

	@Autowired
	private WishlistService wishlistService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private StoreInquiryService storeInquiryService;

	@Autowired
	private StatsService statsService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private PointService pointService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private RemittanceService remittanceService;

	@Autowired
	private ShadowLoginLogService shadowLoginLogService;


	@Autowired
	private GnbService gnbService;

	@Autowired
	private MainService mainService;



	@Autowired
	private PrivacyAccessLogRepository privacyAccessLogRepository;

	@Autowired
	private PrivacyAccessLogHistService privacyAccessLogHistService;

	@Autowired
	private GiveStateService giveStateService;		// 지자체 코드 조회용

	@Autowired
	private ManagerRequestService managerRequestService;

	@Autowired
    private SequenceService sequenceService;

	@GetMapping("shadow-logout-log")
	public JsonView shadowLogoutLog() {

		try {

			User user = UserUtils.getUser();
			if (user != null && user.isShadowLogin()) {
				UserDetail userDetail = (UserDetail) user.getUserDetail();
				shadowLoginLogService.updateShadowLogoutLog(userDetail.getShadowLoginLogId());
			}

		} catch (OpRuntimeException e) {
			log.warn("shadow logout error : {}");
		}

		return JsonViewUtils.success();
	}


	@PostMapping("/{pageType}/privacy-access-log")
	public JsonView privacyAccessLog(@RequestParam("url") String url,
									 PrivacyAccessLog privacyAccessLog, HttpServletRequest request) {
		try {
			if (privacyAccessLog == null || !StringUtils.hasText(privacyAccessLog.getReason())) {
				throw new OpRuntimeException("엑셀 다운로드 사유를 입력해주세요.");
			}

			PrivacyAccess privacyAccess = PrivacyAccess.findByUrl(new AntPathMatcher(), url);
			if (privacyAccess == null) {
				throw new OpRuntimeException("개인정보 접근 URL이 아닙니다. 설정파일을 확인해주세요.");
			}

			privacyAccessLog.setDefaultInfo(privacyAccess, request);
			privacyAccessLog.setUrl(url);

			privacyAccessLog.setUserId(UserUtils.getUser().getUserId());

            try {
            	privacyAccessLog.setIp(pCrypto.Encrypt("normal", privacyAccessLog.getIp(), ""));
            } catch (UnsupportedEncodingException e) {
            	log.error(getClass().getName() + " :: privacyAccessLog pCrypto Error ", e);
            }

			privacyAccessLogRepository.save(privacyAccessLog);

			PrivacyLogHistParam privacyLogHistParam = new PrivacyLogHistParam();

			long hsitId = sequenceService.getId("OP_PRIVACY_ACCESS_LOG_HIST");
			privacyLogHistParam.setHistId(hsitId);

			long userId = UserUtils.getUser().getUserId();
			//long userId = SecurityUtils.getCurrentUserId();
			if (SellerUtils.isSellerLogin()) {
				userId = SellerUtils.getSellerId();
            }

			privacyLogHistParam.setManagerId(userId);
			privacyLogHistParam.setPrivacyAccessLogId(privacyAccessLog.getId());
			privacyLogHistParam.setReason(privacyAccessLog.getReason());
			privacyLogHistParam.setReasonType(privacyAccessLog.getReasonType());
			this.privacyAccessLogUpdate(privacyLogHistParam, request);

		} catch (OpRuntimeException e) {
			log.debug("[/opmanager/privacy-access] Error: ");
			return JsonViewUtils.failure("오류가 발생했습니다.");
		}

		return JsonViewUtils.success();
	}

	@PostMapping("/{pageType}/privacy-access-log-update")
	public JsonView privacyAccessLogUpdate(PrivacyLogHistParam privacyLogHistParam, HttpServletRequest request) {
		try {
			if (privacyLogHistParam == null || !StringUtils.hasText(privacyLogHistParam.getReason())) {
				throw new OpRuntimeException("엑셀 다운로드 사유를 입력해주세요.");
			}

			long managerId = UserUtils.getUser().getUserId();

			if(SellerUtils.isSellerLogin()) {
				managerId = SellerUtils.getSellerId();
			}

			if(managerId != privacyLogHistParam.getManagerId()) throw new OpRuntimeException("엑셀 다운로드 사유를 등록한 본인만 수정이 가능합니다.");

			long hsitId = sequenceService.getId("OP_PRIVACY_ACCESS_LOG_HIST");
			privacyLogHistParam.setHistId(hsitId);

			privacyLogHistParam.setManagerId(managerId);

			//privacyAccessLogHistRepository.save(privacyAccessLogHist);
			privacyAccessLogHistService.insertPrivacyAccessLogHist(privacyLogHistParam);

			PrivacyAccessLog privacyAccessLog = new PrivacyAccessLog();

			privacyAccessLog.setId(privacyLogHistParam.getPrivacyAccessLogId());
			privacyAccessLog.setReason(privacyLogHistParam.getReason());
			privacyAccessLog.setReasonType(privacyLogHistParam.getReasonType());
			privacyAccessLogHistService.updatePrivacyAccessLog(privacyAccessLog);

		} catch (OpRuntimeException e) {
			log.debug("[/opmanager/privacy-access] Error: ");
			return JsonViewUtils.failure("오류가 발생했습니다.");
		}

		return JsonViewUtils.success();
	}

	@PostMapping("opmanager/order-count")
	public JsonView opmanagerOrderLnbCount(RequestContext requestContext,
										   @RequestParam(name = "month", defaultValue = "0") int month) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		if (month > 0) {
			return JsonViewUtils.success(orderService.getOpmanagerOrderCountAllByMonth(month));
		}

		return JsonViewUtils.success(orderService.getOpmanagerOrderCountAll());
	}


	/**
	 * 이상우 [2017-05-11 추가]
	 * 관리자 메인 방문자, 신규가입자 카운트
	 * @param requestContext
	 * @return
	 */
	@PostMapping("opmanager/user-count")
	public JsonView opmanagerUserLnbCount(RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		return JsonViewUtils.success(userService.getOpmanagerUserCountAll());
	}

	/**
	 * 이상우 [2017-05-11 추가]
	 * 정산 예정, 확정 카운트
	 * @param requestContext
	 * @return
	 */
	@PostMapping("opmanager/remittance-count")
	public JsonView opmanagerRemittanceLnbCount(RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		//날짜 설정
		RemittanceParam remittanceParam = new RemittanceParam();
		remittanceParam.setStartDate(DateUtils.getToday());
		remittanceParam.setEndDate(DateUtils.getToday());

		return JsonViewUtils.success(remittanceService.getOpmanagerRemittanceCountAll(remittanceParam));
	}

	/**
	 * 이상우 [2017-05-11 추가]
	 * 출고,교환,반품 지연 카운트
	 * @param requestContext
	 * @param type
	 * @return
	 */
	@PostMapping("opmanager/shipping-delay-count")
	public JsonView opmanagerShippingDelayLnbCount(RequestContext requestContext, String type) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		//지연일 설정 - 추후 shopconfig에서 가져오는 방향으로 수정해야함.
		HashMap<String, Object> map = new HashMap<>();
		map.put("shippingDelay", 1);
		map.put("exchangeDelay", 3);
		map.put("returnDelay", 3);


		if (SellerUtils.isSellerLogin()) {
			long sellerId = 0;
			if(SellerUtils.getShadowSeller() != null && SellerUtils.getSeller() != null) {
				if (SecurityUtils.hasRole("OPMANAGER") && SellerUtils.isShadowSellerLogin()) {
					sellerId = CommonUtils.longNvl(SellerUtils.getShadowSeller().getSellerId());
				} else {
					sellerId = CommonUtils.longNvl(SellerUtils.getSeller().getSellerId());
				}
			}

			map.put("sellerId", sellerId);
		}

		return JsonViewUtils.success(orderService.getOpmanagerShippingDelayCountAll(map));
	}

	@PostMapping("user/order-count")
	public JsonView userOrderLnbCount(RequestContext requestContext,
									  @RequestParam(name = "month", defaultValue = "0") int month) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		if (month > 0) {
			return JsonViewUtils.success(orderService.getUserOrderCountAllByMonth(month));
		}


		return JsonViewUtils.success(orderService.getUserOrderCountAll());
	}

	@PostMapping("seller/order-count")
	public JsonView sellerOrderLnbCount(RequestContext requestContext,
										@RequestParam(name = "month", defaultValue = "0") int month) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		if (month > 0) {
			return JsonViewUtils.success(orderService.getSellerOrderCountAllByMonth(month));
		}

		return JsonViewUtils.success(orderService.getSellerOrderCountAll());
	}

	@PostMapping("opmanager/store-count")
	public JsonView opmanagerStoreLnbCount(RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		return JsonViewUtils.success(storeInquiryService.getOpmanagerStoreCountAll());
	}





	@PostMapping("island-type")
	public JsonView island(@RequestParam("zipcode") String zipcode) {

		HashMap<String, String> map = new HashMap<>();
		map.put("islandType", orderService.getIslandTypeByZipcode(zipcode));
		return JsonViewUtils.success(map);

	}




	@PostMapping("message")
	public JsonView message(@RequestParam("messageCode") String messageCode) {
		return JsonViewUtils.success(MessageUtils.getMessage(messageCode));
	}


	/**
	 * 관심상품 수 가져오기 (모바일에서 아이콘 표시)
	 * @param session
	 * @return
	 */
	@PostMapping("wishlist")
	public JsonView wishlist(HttpSession session) {

		int count = 0;
		if (UserUtils.isUserLogin()) {
			count = wishlistService.getWishlistCountByUserId(UserUtils.getUserId());
		}

		return JsonViewUtils.success(count);
	}
//
//	/**
//	 * 카트 정보 가져오기
//	 * @param session
//	 * @return
//	 */
//	@PostMapping("cart")
//	public JsonView cart(HttpSession session) {
//		// 장바구니 요약 정보 조회
//    	CartParam cartParam = new CartParam();
//
//    	cartParam.setUserId(UserUtils.getUserId());
//    	cartParam.setSessionId(session.getId());
//    	List<BuyItem> cartList = cartService.getCartList(cartParam, false);
//
//    	int cartQuantity = 0;
//    	int cartPrice = 0;
//    	for (BuyItem buyItem : cartList) {
//
//    		ItemPrice itemPrice = buyItem.getItemPrice();
//
//    		cartQuantity += itemPrice.getQuantity();
//    		//cartQuantity++;
//    		cartPrice += (itemPrice.getSaleAmount());
//		}
//
//
//    	HashMap<String, Object> result = new HashMap<>();
//    	result.put("cartQuantity", cartQuantity);
//    	result.put("cartPrice", StringUtils.numberFormat(cartPrice));
//    	result.put("cartList", cartList);
//
//
//		return JsonViewUtils.success(result);
//	}
//

	/**
	 * @param session
	 * @return
	* */
	@PostMapping("cart")
	public JsonView cart(HttpSession session){
		CartParam cartParam = new CartParam();

		cartParam.setUserId(UserUtils.getUserId());
		cartParam.setSessionId(session.getId());

		List<BuyItem> cartList = cartService.getCartList(cartParam, false);

		int cartQuantity = 0;
		int cartPrice = 0;

		if (cartList != null && !cartList.isEmpty()) {
			for(BuyItem buyItem : cartList){
				if (buyItem.getItemPrice() != null) {
					ItemPrice itemPrice = buyItem.getItemPrice();
					cartQuantity += CommonUtils.intNvl(itemPrice.getQuantity());
					cartPrice += CommonUtils.intNvl(itemPrice.getSaleAmount());
				}
			}
		}
		HashMap<String, Object> result = new HashMap<>();
		result.put("cartQuantity", cartQuantity);
		result.put("carPrice",StringUtils.numberFormat(cartPrice));
		result.put("cartList",cartList);

		return JsonViewUtils.success(result);

	}





	/**
	 * 접속 통계 비동기 처리
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("visitor-log")
	public JsonView visitorLog(HttpServletRequest request,  HttpServletResponse response) {
		try {
			// 접속 통계 저장.
			statsService.saveVisitData(request, response);
		} catch(OpRuntimeException e) {
			log.warn("statsService.saveVisitData(..) : {}");
		}

		return JsonViewUtils.success();
	}

	/**
	 * 주문 쿠폰 및 배송비 쿠폰 수량 가져오기
	 * @param session
	 * @return
	 */
	@PostMapping("coupon")
	public JsonView coupon (HttpSession session) {

		int userCouponCount = 0;
		int userShippingCount = 0;
		if (UserUtils.isUserLogin()) {
			UserCouponParam userCouponParam = new UserCouponParam();
			userCouponParam.setUserId(UserUtils.getUserId());

			userCouponCount = couponService.getDownloadUserCouponCountByUserCouponParam(userCouponParam);

			// 총 사용가능 배송 쿠폰
			AvailablePoint avilableShippingCoupon = pointService.getAvailablePointByUserId(UserUtils.getUserId(), PointUtils.SHIPPING_COUPON_CODE);

			userShippingCount = avilableShippingCoupon.getAvailablePoint();
		}
		HashMap<String, Integer> map = new HashMap<>();

		map.put("userCouponCount", userCouponCount);
		map.put("userShippingCount", userShippingCount);

		return JsonViewUtils.success(map);
	}

	/**
	 * 오늘본 상품
	 * @param request
	 * @param response
	 * @return
	 */
	// 미사용하여 주석처리(시큐어코딩 검출)
//	@PostMapping("today-items")
//	public JsonView todayItems(HttpServletRequest request, HttpServletResponse response) {
//		Cookie[] cookies = request.getCookies();
//		List<Item> todayItems = new ArrayList<>();
//		ItemParam itemParam = new ItemParam();
//		Pagination pagination = Pagination.getInstance(6, 6);
//		itemParam.setPagination(pagination);
//
//		try{
//    		if (cookies != null && cookies.length > 0) {
//    			for (int i = 0; i < cookies.length; i++) {
//    				String imageRoot = "";
//    				if (cookies[i].getName().equals("TODAY_ITEMS")) {
//    					String todayItemsInfo = cookies[i].getValue();
//
//    					if (!"".equals(todayItemsInfo)) {
//	    					itemParam.setTodayItemIds(todayItemsInfo.toUpperCase().replaceAll("SELECT", "").replaceAll("DELETE", "").replaceAll("UPDATE", "").replaceAll("INSERT", "").replaceAll(":", ","));
//	    					itemParam.setDisplayFlag("Y");
//
//	    					//todayItems = itemService.getItemList(itemParam);
//	    					todayItems = itemService.getTodayItemList(itemParam);
//	    					//관리자 썸네일 설정에 따라 이미지URL이 달라짐[2017-06-05]minae.yun
//	    					for (int j = 0; j < todayItems.size(); j++) {
//	    						imageRoot = ShopUtils.loadImage(todayItems.get(j).getItemCode(), todayItems.get(j).getItemImage(), "XS");
//	    						if (imageRoot != null) todayItems.get(j).setItemImage(imageRoot);
//	    					}
//    					}
//    				}
//    			}
//    		}
//		} catch(OpRuntimeException e) {
//			log.error("ERROR: {}", e.getErrorMessage(), e);
//
//			Cookie cookie = new Cookie("TODAY_ITEMS", "");
//			cookie.setHttpOnly(true);
//			cookie.setSecure(true);
//		    cookie.setMaxAge(60*60*24);				// 쿠키 유지 기간 - 1일
//		    cookie.setPath("/");					// 모든 경로에서 접근 가능하도록
//		    response.addCookie(cookie);				// 쿠키저장
//
//		}
//		return JsonViewUtils.success(todayItems);
//	}

	/**
	 * 상품 조회수 증가
	 * @param item
	 * @return
	 */
	@PostMapping("/update-item-hits")
	public JsonView updateItemHits(Item item) {
		// 상품 조회 수 증가.
		itemService.updateItemHitsByItemId(item.getItemId());
		return JsonViewUtils.success();
	}

	@GetMapping("gnb")
	@ResponseBody
	public JsonView getGnbList () {

		return JsonViewUtils.success(gnbService.getFrontGnbList());
	}

	/**
	 * 관리자 메인 전체회원/금일가입회원/관리자권한요청(대기)/오프라인접수현황/총기부현황/총답례품현황 카운트
	 * @param requestContext
	 * @return
	 */
	@PostMapping("opmanager/main-info")
	public JsonView opmanagerMainInfo(RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		String locgovCd = mainService.getLocgovCdByUerId(UserUtils.getUserId());

		return JsonViewUtils.success(mainService.getOpmanagerMainInfo(locgovCd));
	}

	/**
	 * 관리자 메인 게시판 현황
	 * @param requestContext
	 * @return
	 */
	@PostMapping("opmanager/main-boardInfo")
	public JsonView opmanagerMainBoardInfo(RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		return JsonViewUtils.success(mainService.getOpmanagerMainBoardInfo());
	}

	/**
	 * 관리자 메인 기부금액 chart
	 * @param requestContext
	 * @return
	 */
	@PostMapping("opmanager/main-amountChart")
	public JsonView opmanagerMainAmountChart(RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		String locgovCd = mainService.getLocgovCdByUerId(UserUtils.getUserId());

		return JsonViewUtils.success(mainService.getOpmanagerMainAmountChart(locgovCd));
	}

	/**
	 * 관리자 메인 기부건수 chart
	 * @param requestContext
	 * @return
	 */
	@PostMapping("opmanager/main-countChart")
	public JsonView opmanagerMainCountChart(RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		String locgovCd = mainService.getLocgovCdByUerId(UserUtils.getUserId());

		return JsonViewUtils.success(mainService.getOpmanagerMainCountChart(locgovCd));
	}

	/**
	 * 관리자 메인 기부금액/기부건수 일주일간 총계 및 평균
	 * @param requestContext
	 * @return
	 */
	@PostMapping("opmanager/main-chartInfo")
	public JsonView opmanagerMainChartInfo(RequestContext requestContext) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		String locgovCd = mainService.getLocgovCdByUerId(UserUtils.getUserId());

		return JsonViewUtils.success(mainService.getOpmanagerMainChartInfo(locgovCd));
	}

    /**
     * 지자체 목록 가져오기(시군구)
     *
     * @param request
     * @return
     */
    @PostMapping("/getLocgovCode")
    public @ResponseBody JsonView getLocgovCode(@RequestParam(name="code", defaultValue = "0") String code, Model model) {
//    	if (SecurityUtils.isLogin()) {
		if (UserUtils.isManagerLogin() || UserUtils.isSellerLogin()) {



        	return JsonViewUtils.success(giveStateService.getLocgovCodeList(code));
    	} else {
    		return JsonViewUtils.failure(MessageUtils.getMessage("M00481"));		// 잘못된 접근입니다.
    	}
	}

	/**
	 * 관리자 메인 하단 테이블 정보 조회
	 * @param requestContext
	 * @return
	 */
	@PostMapping("opmanager/main-search")
	public JsonView opmanagerMainTableInfo(RequestContext requestContext,
			@ModelAttribute("searchParam") GiveState searchParam) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		if (StringUtils.hasLength(searchParam.getShCntrDeStart())) {
			return JsonViewUtils.success(mainService.getOpmanagerMainTableInfo(searchParam));
		} else {
			throw new OpRuntimeException("기부일자가 없습니다.");
		}
	}

	/**
	 * 콜 등록 확인
	 * @param requestContext
	 * @param giveState
	 * @return
	 */

	@PostMapping("opmanager/call-search")
	public JsonView opmanagerMainCallTableInfo(RequestContext requestContext,
			@ModelAttribute("giveState") GiveState giveState) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}


		return JsonViewUtils.success(mainService.getOpmanagerMainCallTableInfo(giveState));
	}

	@GetMapping("manager/notice/list")
	public JsonView getNoticeList(String type) {

		return JsonViewUtils.success(managerRequestService.getNoticeList(type));
	}
	/**
	 *
	 * <pre>
	 * comment       :  답례품 지자체
	 * preMethodName :
	 * author        : rhkdqhr90
	 * date          : 2023. 8. 4.
	 *
	 * </pre>
	 * @param
	 * @return
	 * JsonView
	 */

	@GetMapping("opmanager/gift-search")
	public JsonView getGiftSearch() {
		return JsonViewUtils.success(mainService.opamanagerMainGift());
	}
	/**
	 *
	 * <pre>
	 * comment       :  답례품 수
	 * preMethodName :
	 * author        : rhkdqhr90
	 * date          : 2023. 8. 4.
	 *
	 * </pre>
	 * @param
	 * @return
	 * JsonView
	 */

	@GetMapping("opmanager/gift-amount")
	public JsonView getGiftAmount(GiveState giveState) {
		return JsonViewUtils.success(mainService.opmanageMainAmountGift(giveState));
	}




	@PostMapping("opmanager/get-user")
	public  JsonView getUserDashboard(GiveState giveState) {


		String dateStr = giveState.getShCntrDeStart();
		//기부건수 증감용 날짜 설정
        // SimpleDateFormat을 사용하여 문자열을 Date로 파싱
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate date = LocalDate.parse(dateStr, formatter);

        // 하루를 뺀 새로운 날짜 계산
        LocalDate newDate = date.minusDays(1);

        // 새로운 날짜를 문자열로 변환 (원하는 형식으로)
        String newDateStr = newDate.format(formatter);
		giveState.setShCntrDeEnd(newDateStr);

		return JsonViewUtils.success(mainService.dashBoardReport(giveState));
	}

	/**
	 *
	 * 2026. 1. 21
	 * 관리자 메인 화면
	 * 알림설정 동의 여부 멤버 카운트
	 *
	 */
	@PostMapping("opmanager/main-notification-agree-member")
	public JsonView getNotificationAgreeMemberCount() {
		Map<String, Object>  result = new HashMap<>();

		try {
			result = mainService.notificationAgreeMemberCount();
		} catch (Exception e) {
			log.error("ERROR: getNotificationAgreeMemberCount - 알림동의유저수 카운팅 실패");
		}

		return JsonViewUtils.success(result);
	}
}
