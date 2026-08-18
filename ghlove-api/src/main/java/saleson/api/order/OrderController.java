package saleson.api.order;

import com.google.gson.Gson;
import com.onlinepowers.framework.common.ServiceType;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.security.userdetails.OpUserDetails;
import com.onlinepowers.framework.security.userdetails.OpUserDetailsService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import saleson.api.cart.domain.CartInfo;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.utils.CommonUtils;
import saleson.shop.order.OrderService;
import saleson.shop.order.api.ApiOrderList;
import saleson.shop.order.api.OrderDetail;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.OrderCodePrefix;
import saleson.common.enumeration.mapper.EnumMapper;
import saleson.common.security.api.JwtCode;
import saleson.common.security.api.JwtTokenService;
import saleson.common.security.api.TokenAuthUserInfo;
import saleson.common.security.authentication.JwtTokenAuthenticationToken;
import saleson.common.utils.JwtUtils;
import saleson.common.utils.OrderUtils;
import saleson.common.utils.PointUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.ConfigPg;
import saleson.model.OrderCancelFail;
import saleson.shop.cart.CartService;
import saleson.shop.cart.domain.Cart;
import saleson.shop.cart.support.CartParam;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.coupon.domain.OrderCoupon;
import saleson.shop.give.givepoint.domain.GivePoint;
import saleson.shop.google.analytics.GoogleAnalyticsService;
import saleson.shop.order.claimapply.OrderClaimApplyService;
import saleson.shop.order.claimapply.domain.OrderCancelApply;
import saleson.shop.order.claimapply.domain.*;
import saleson.shop.order.claimapply.support.ExchangeApply;
import saleson.shop.order.claimapply.support.ReturnApply;
import saleson.shop.order.domain.*;
import saleson.shop.order.givepoint.OrderGivePointService;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.order.log.OrderLogService;
import saleson.shop.order.pg.PgService;
import saleson.shop.order.pg.config.ConfigPgService;
import saleson.shop.order.pg.domain.PgData;
import saleson.shop.order.pg.domain.ReturnUrlParam;
import saleson.shop.order.refund.OrderRefundService;
import saleson.shop.order.refund.domain.OrderRefund;
import saleson.shop.order.support.OrderException;
import saleson.shop.order.support.OrderParam;
import saleson.shop.orderCancelFail.OrderCancelFailService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.userdelivery.UserDeliveryService;

import saleson.shop.userlevel.UserLevelService;
import saleson.shop.userlevel.domain.UserLevel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;


@Controller("ApiOrderController")
@RequestMapping("/api/order")
@RequestProperty(template="mobile", layout="default")
public class OrderController {

    private static Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserDeliveryService userDeliveryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private OrderClaimApplyService orderClaimApplyService;

    @Autowired
    private EnumMapper enumMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    Environment environment;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private UserService userService;

//    @Autowired
//    @Qualifier("nicepayService")
//    private PgService nicepayService;

    @Autowired
    private ConfigPgService configPgService;

//    @Autowired
//    private GoogleAnalyticsService googleAnalyticsService;

    @Autowired
    private OpUserDetailsService userDetailsService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private OrderLogService orderLogService;

    @Autowired
    private OrderCancelFailService orderCancelFailService;

    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private OrderGivePointService orderGivePointService;

    @GetMapping("")
    public ResponseEntity list(HttpServletRequest request, OrderParam orderParam) {

        if (!(UserUtils.isUserLogin() || UserUtils.isGuestLogin())) {
            return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
        }

        ResponseEntity result = null;
        List<ApiOrderList> list = null;
        Pagination pagination = null;

        int orderCount = 0;

        if (orderParam == null) {
            orderParam = new OrderParam();
        } else {
            // 상품명이 존재 할 시 상품명 검색
            if (!ObjectUtils.isEmpty(orderParam.getQuery())) {
                orderParam.setWhere("ITEM_NAME");
            }
        }

        orderParam.setSearchDateType("OI.CREATED_DATE");
        orderParam.setSearchStartDate(orderParam.getSearchStartDate().replaceAll("-", ""));
        orderParam.setSearchEndDate(orderParam.getSearchEndDate().replaceAll("-", ""));

        // 회원, 비회원 로그인 구분
        if (UserUtils.isUserLogin()) {
            orderParam.setUserId(UserUtils.getUserId());
        } else if (UserUtils.isGuestLogin()) {
            User user = UserUtils.getGuestLogin();
            if (user == null) {
            	return ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "사용자 정보가 없습니다.");
            }
            UserDetail userDetail = (UserDetail) user.getUserDetail();

            orderParam.setGuestUserName(user.getUserName());
            orderParam.setGuestPhoneNumber(userDetail.getPhoneNumber());
        }

        try {
            list = orderService.getApiOrderList(orderParam);
            pagination = orderParam.getPagination();
            result = ApiResponseEntity.data().list(list).pagination(pagination).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    @GetMapping("/detail")
    public ResponseEntity details(HttpServletRequest request, OrderParam orderSearchParam) {
        ResponseEntity result = null;
        OrderDetail orderDetail = null;
        try {

            orderLogService.put(orderSearchParam.getOrderCode());

            orderSearchParam.setUserId(UserUtils.getUserId());
            orderDetail = orderService.getApiOrderDetail(orderSearchParam);
            List<OrderGivePoint> givePointList = orderGivePointService.getGiveBlcePointListByUserId(UserUtils.getUserId());
            result = ApiResponseEntity.data().put("order", orderDetail)
            								.put("phoneList", CodeUtils.getCodeInfoList("PHONE"))
                    						.put("givePointList", givePointList).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    @PostMapping("/cancel")
    public ResponseEntity cancel(HttpServletRequest request, HttpSession session, @RequestBody(required = false) OrderParam orderParam){
        ResponseEntity result = null;
        try {
            orderClaimApplyService.orderCancelAllProcess(orderParam, request);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();

        } catch(OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }

//        try {
//            googleAnalyticsService.allRefund(request, orderParam.getOrderCode(), orderParam.getOrderSequence());
//        } catch (Exception e) {
//            log.error("Google Analytics Service All Refund Error", e);
//        }

        return result;
    }

    // 주문서
    @PostMapping("/payment-step")
    public ResponseEntity paymentStep(HttpServletRequest request) throws Exception {
        ResponseEntity result = null;

        OrderParam orderParam = new OrderParam();
        String errorMsg = "";	// 에러메시지

        try {

            HashMap<String, BuyPayment> buyPayments = orderService.getPaymentType();

            if (buyPayments.keySet().isEmpty()) {
                throw new OrderException(ApiError.BAD_BUY_NO_ITEM, "/cart");
            }

            long userId = 0;
            ApiResponseEntity.Builder data = new ApiResponseEntity.Builder();

            if (UserUtils.isUserLogin()) {
                userId = UserUtils.getUser().getUserId();
                data = ApiResponseEntity.data().put("userDeliveryList", userDeliveryService.getUserDeliveryList(userId));
            } else {
            	throw new OrderException(ApiError.NO_LOGIN, "/");
            }

            orderParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
            orderParam.setUserId(userId);

            Buy buy = orderService.getBuyForStep1(orderParam, UserUtils.getUser());

            if (buy == null) {
            	errorMsg = "결제가능 상품이 없습니다.";
            	throw new OrderException(ApiError.BAD_BUY_NO_ITEM, "/cart");
            }

            buy.setBuyPayments(buyPayments);

            orderLogService.put(buy.getOrderCode());


            List<CodeInfo> telCodeList = CodeUtils.getCodeInfoList("TEL");
            List<CodeInfo> phoneCodeList = CodeUtils.getCodeInfoList("PHONE");
            List<CodeInfo> emailCodeList = CodeUtils.getCodeInfoList("EMAIL");

            List<OrderGivePoint> givePointList = orderGivePointService.getGiveBlcePointListByUserId(userId);

            //성인 여부 체크
            boolean check = UserUtils.isAdult();
            String AdultYn = "N";
            if(check) {
            	AdultYn = "Y";
            }

            result = data.put("buy", buy)
                    .put("userData", buy.getUserData())
                    .put("telCodes", telCodeList)
                    .put("phoneCodes", phoneCodeList)
                    .put("emailCodes", emailCodeList)
                    .put("givePointList", givePointList)
                    .put("adultYn", AdultYn)
                    .ok();

        } catch(OrderException e){

        	log.error("OrderController paymentStep OrderException :: " + errorMsg, e);
        	throw e;
        } catch(OpRuntimeException e){
        	log.error("OrderController paymentStep OpRuntimeException :: " + errorMsg, e);
            throw e;
        }
        return result;
    }

    /**
     * 주문정보 임시 저장
     * @param request
     * @param session
     * @param buy
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity save(HttpServletRequest request,
                               HttpSession session, @RequestBody(required = false) Buy buy) {

        ResponseEntity result = null;
        Enumeration params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = (String)params.nextElement();
            // System.out.println("Attribute1 Name - "+paramName+", Value - "+request.getParameter(paramName));
        }

        long userId = 0;
        if (UserUtils.isUserLogin()) {
            userId = UserUtils.getUserId();
        } else {
        	return result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "로그인 상태가 아닙니다.");
        }

        try {
            // 네이버페이 returnParam 에 전달할 token, sessionId 암호화 처리
            String token = JwtUtils.getToken(request);
            String sessionId = ShopUtils.getSalesOnIdByHeader(request);
            String encryptedString = CipherUtils.encrypt(token + "|" + sessionId);

            String orderCode = orderService.getNewOrderCode(OrderCodePrefix.FRONT);
//            buy.setOrderCode(orderService.getNewOrderCode(OrderCodePrefix.FRONT));
            buy.setOrderCode(orderCode);

//            orderLogService.put(buy.getOrderCode());
            orderLogService.put(orderCode);

            buy.setOrderCode(orderCode);
            buy.setUserId(userId);
            buy.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
            buy.setDeviceType(buy.getDeviceType());
            buy.setRealDeviceType(DeviceUtils.resolveDevice(request));
            buy.setUserIp(CommonUtils.getClientIp(request));

            result = ApiResponseEntity.data()
                    .put("data", orderService.saveOrderTemp(session, buy))
                    .put("encryptedString", encryptedString).ok();

        } catch (OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
            log.error("OrderController save1 error :: ", e);
        } catch (Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, ApiError.SYSTEM_ERROR.getDescription());
            log.error("OrderController save2 error :: ", e);
        }

        return result;
    }

    // 결제하기 - PG
    @PostMapping("/pay")
    public ResponseEntity pay(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                              PgData pgData) {
        ResponseEntity result = null;
        Map<String, Object> resultMap = new HashMap<>();
        OrderParam orderParam = new OrderParam();

        pgData.setRequest(request);
        pgData.setResponse(response);

        orderLogService.put(pgData.getOrderCode());

        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = (String) params.nextElement();
            log.debug("[" + pgData.getOrderCode() + "] [Api OrderController] {} = {}", paramName, request.getParameter(paramName));
        }

        log.debug("[" + pgData.getOrderCode() + "] [Api OrderController] {} = {}", "pgData", pgData.toString());

        String orderCode = null;
        int orderSequence = 0;
        try {

            orderParam.setOrderCode(pgData.getOrderCode());
            orderParam.setUserId(UserUtils.getUserId());
            orderParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));

            orderCode = orderService.insertOrder(orderParam, pgData, session, request);
            orderSequence = Integer.parseInt(orderCode.split("/")[0]);
            orderParam.setOrderSequence(orderSequence);

            resultMap.put("orderCode", orderCode.split("/")[1]);
            resultMap.put("orderSequence", orderSequence);
            result = ApiResponseEntity.data().put("orderInfo", resultMap).ok();

        } catch (OpRuntimeException e) {
            if (e.getErrorMessage().indexOf("설정된 결제 방법이 없습니다") > 0) {
                log.error("[/api/order/pay] ERROR : {}", ApiError.BAD_REQUEST.getDescription(), e);
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
            } else if (e.getErrorMessage().indexOf("금액오류(200원 이하 이체불가)") > 0) {
                log.error("[/api/order/pay] ERROR : {}", ApiError.BAD_REQUEST_PAY_MIN_FAIL.getDescription(), e);
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_PAY_MIN_FAIL, e.getErrorMessage());
            } else {
                log.error("[/api/order/pay] ERROR : {}", ApiError.BAD_REQUEST.getDescription(), e);
                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "ERROR-30: System Error (시스템 에러)");
            }
            log.error("OrderController pay error :: ", e);
        }
        return result;
    }

 // 결제하기 - PG
    @PostMapping("/giveGoodsSavePay")
    public ResponseEntity giveGoodsSavePay(HttpServletRequest request,
            				HttpSession session, @RequestBody(required = false) Buy buy) {
    	ResponseEntity result = null;
//        Enumeration params = request.getParameterNames();
//        while(params.hasMoreElements()){
//            String paramName = (String)params.nextElement();
//            // System.out.println("Attribute1 Name - "+paramName+", Value - "+request.getParameter(paramName));
//        }

        long userId = 0;
        if (UserUtils.isUserLogin()) {
            userId = UserUtils.getUserId();
        } else {
        	result = ApiResponseEntity.data()
                    .put("errMsg", "로그인 상태가 아닙니다.")
                    .ok();
//        	return ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "로그인 상태가 아닙니다.");
        	return result;
        }

        try {
            // 네이버페이 returnParam 에 전달할 token, sessionId 암호화 처리
            String orderCode = orderService.getNewOrderCode(OrderCodePrefix.FRONT);

            orderLogService.put(buy.getOrderCode());

            buy.setOrderCode(orderCode);
            buy.setUserId(userId);
            buy.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
            buy.setDeviceType(buy.getDeviceType());
            buy.setRealDeviceType(DeviceUtils.resolveDevice(request));
            buy.setUserIp(CommonUtils.getClientIp(request));

            result = ApiResponseEntity.data()
                    .put("data", orderService.saveGiveOrderTempAndPay(session, buy, request))		// 결제 로직
                    .ok();

        } catch (OrderException e){
//        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        	result = ApiResponseEntity.data()
                    .put("errMsg", e.getErrorMessage())
                    .ok();
            log.error("OrderController giveGoodsSavePay error1 :: ", e);
        } catch (OpRuntimeException | NullPointerException e){
        	if (e instanceof OpRuntimeException) {
//                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, ((OpRuntimeException) e).getErrorMessage());
            	result = ApiResponseEntity.data()
                        .put("errMsg", ((OpRuntimeException) e).getErrorMessage())
                        .ok();
        	} else {
//        		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "문제가 발생했습니다.");
            	result = ApiResponseEntity.data()
                        .put("errMsg", "문제가 발생했습니다.")
                        .ok();
        	}
            log.error("OrderController giveGoodsSavePay error2 :: ", e);
        }

        return result;

    }

    // 결제하기 - PG
    @PostMapping("/redirect-pay")
    public String redirectPay(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                              PgData pgData) {

        Map<String, Object> resultMap = new HashMap<>();
        OrderParam orderParam = new OrderParam();

        pgData.setRequest(request);
        pgData.setResponse(response);


        String redirect = "";
        String failUrl = pgData.getFailUrl();
        String successUrl = pgData.getSuccessUrl();

        try {

            String sessionId = ShopUtils.getSalesOnIdByHeader(request);
            String orderCode = pgData.getOrderCode();
            int orderSequence = 0;

            orderLogService.put(orderCode);

            long userId = UserUtils.getUserId();
            ReturnUrlParam returnUrlParam = OrderUtils.decodeUrlSafeBase64(pgData.getReturnUrlParam());

            if (returnUrlParam != null) {
                failUrl = returnUrlParam.getFailUrl();
                successUrl = returnUrlParam.getSuccessUrl();
                sessionId = returnUrlParam.getSalesonId();


                if ("USER".equals(returnUrlParam.getTokenType())) {
                    String salesonToken = returnUrlParam.getToken();
                    String loginId = getLoginIdByToken(salesonToken);
                    User user = userService.getUserByLoginId(loginId);

                    if (user != null) {
                        userId = user.getUserId();
                    }

                    if (salesonToken == null || user == null) {
                    	throw new IllegalArgumentException("토큰정보가 유효 하지 않습니다.");
                    }

                    if (!validateToken(request, salesonToken, user)) {
                        throw new IllegalArgumentException("토큰정보가 유효 하지 않습니다.");
                    }

                    UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);
                    JwtTokenAuthenticationToken authenticationToken = new JwtTokenAuthenticationToken(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    UserDetail userDetail = userService.getUserDetail(securityService.getCurrentUserId());

                    // 회원 Level이 설정되어있는경우 해당 Level 정보를 조회해서 상품별 할인율을 구한다.
                    // 상품 상세, 장바구니, 주문을 재외한 페이지에서 등급별 할인을 Session으로 처리하기 위함
                    if (userDetail.getLevelId() > 0) {
                        UserLevel userLevel = userLevelService.getUserLevelById(userDetail.getLevelId());
                        if (userLevel != null) {
                            userDetail.setUserLevelDiscountRate(userLevel.getDiscountRate());
                            userDetail.setUserLevelPointRate(userLevel.getPointRate());
                        }
                    }

                    Object princial = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                    if (princial instanceof UserDetails) {
                        ((OpUserDetails) princial).setUserDetail(userDetail);
                    }
                }
            }

            orderParam.setOrderCode(orderCode);
            orderParam.setUserId(userId);
            orderParam.setSessionId(sessionId);

            orderCode = orderService.insertOrder(orderParam, pgData, session, request);
            orderSequence = Integer.parseInt(orderCode.split("/")[0]);
            orderParam.setOrderSequence(orderSequence);

            resultMap.put("orderCode", orderCode.split("/")[1]);
            resultMap.put("orderSequence", orderSequence);

            StringBuffer sb = new StringBuffer();

            sb.append("?orderCode="+orderCode.split("/")[1]);
            sb.append("&orderSequence="+orderSequence);

            redirect = successUrl+sb.toString();

        } catch (OpRuntimeException e) {
            if (e.getErrorMessage().indexOf("설정된 결제 방법이 없습니다") > 0) {
                log.error("[/api/order/pay] ERROR : {}", ApiError.BAD_REQUEST.getDescription(), e);
            } else if (e.getErrorMessage().indexOf("금액오류(200원 이하 이체불가)") > 0) {
                log.error("[/api/order/pay] ERROR : {}", ApiError.BAD_REQUEST_PAY_MIN_FAIL.getDescription(), e);
            } else {
                log.error("[/api/order/pay] ERROR : {}", ApiError.SYSTEM_ERROR.getDescription(), e);
            }

            redirect = failUrl;
        }
        return "redirect:" + redirect;
    }

    private Map<String, Object> payInfoDataSet(Buy buyParam){
        Map<String, Object> resultMap = new HashMap<>();
        Buyer buyer = buyParam.getBuyer();
        Receiver receiver = buyParam.getReceivers().get(0);

        /*
        String bankVirtualNo = "";
        String bankInName = "";
        String bankExpirationDate = "";
        if(buyParam.getBuyPayments() != null && buyParam.getPaymentType().equals("bank")){
            bankVirtualNo = buyParam.getBuyPayments().get("bank").getBankVirtualNo();
            bankInName = buyParam.getBuyPayments().get("bank").getBankInName();
            bankExpirationDate = buyParam.getBuyPayments().get("bank").getBankExpirationDate();
        }*/

        resultMap.put("buyerUserName", buyer.getUserName());    // 주문자명
        resultMap.put("buyerMobile1", buyer.getMobile1());  // 주문자 연락처1
        resultMap.put("buyerMobile2", buyer.getMobile2());  // 주문자 연락처2
        resultMap.put("buyerMobile3", buyer.getMobile3());  // 주문자 연락처3
        resultMap.put("buyerEmail", buyer.getEmail());  // 이메일

        resultMap.put("receiveName", receiver.getReceiveName());  // 받으시는분
        resultMap.put("receiveZipcode", receiver.getReceiveZipcode());  // 구 우편번호
        resultMap.put("receiveNewZipcode", receiver.getReceiveNewZipcode());  // 신 우편번호
        resultMap.put("receiveAddress", receiver.getReceiveAddress());  // 배송지 주소

        resultMap.put("receiveSido", receiver.getReceiveSido());        // 시도
        resultMap.put("receiveSigungu", receiver.getReceiveSigungu());  // 시군구
        resultMap.put("receiveEupmyeondong", receiver.getReceiveEupmyeondong());    // 읍면동

        resultMap.put("receiveAddressDetail", receiver.getReceiveAddressDetail());  // 배송지 상세주소
        resultMap.put("receiveMobile1", receiver.getReceiveMobile1());  // 받는사람 모바일 연락처1
        resultMap.put("receiveMobile2", receiver.getReceiveMobile2());  // 받는사람 모바일 연락처2
        resultMap.put("receiveMobile3", receiver.getReceiveMobile3());  // 받는사람 모바일 연락처3
        resultMap.put("receivePhone1", receiver.getReceivePhone1());  // 받는사람 전화번호 연락처1
        resultMap.put("receivePhone2", receiver.getReceivePhone2());  // 받는사람 전화번호 연락처2
        resultMap.put("receivePhone3", receiver.getReceivePhone3());  // 받는사람 전화번호 연락처3
        resultMap.put("content", receiver.getContent());  // 배송시 요구사항
        /*resultMap.put("paymentType", buyParam.getPaymentType());*/  // 결제수단(card, vbank, bank, hp)
        resultMap.put("cashbillType", buyParam.getCashbillType());  // 영수증 타입(PERSONAL:개인소득, BUSINESS:사업자, NONE:발급안함)


        Map<String, Object> cashbillInfo = buyParam.getCashbillInfo();
        if(cashbillInfo != null){
            resultMap.put("cashbillNumber1", cashbillInfo.get("cashbillNumber1"));
            resultMap.put("cashbillNumber2", cashbillInfo.get("cashbillNumber2"));
            resultMap.put("cashbillNumber3", cashbillInfo.get("cashbillNumber3"));
        }

        /*resultMap.put("bankVirtualNo", bankVirtualNo);  // 계좌번호
        resultMap.put("bankInName", bankInName);  // 입금자명
        resultMap.put("bankExpirationDate", bankExpirationDate); // 입금예정일*/

        resultMap.put("useCouponKeys", buyParam.getUseCouponKeys());    // 쿠폰 Key
        resultMap.put("pointAmount",buyParam.getBuyPayments().get("point").getAmount()); // 포인트

        String couponList = "";
        Gson gson = new Gson();
        /*if(buyParam.getBuyCouponInfoList() != null && buyParam.getBuyCouponInfoList().size() > 0){
            couponList = gson.toJson(buyParam.getBuyCouponInfoList());
        }*/
        resultMap.put("buyCouponInfoList", gson.toJson(couponList)); // 결제용 쿠폰 데이터

        List<Map<String, Object>> usCouponList = null;
        String usCoupons = "";
        // API 용 배송비 쿠폰
        /*if(buyParam.getShippingCouponList() != null && buyParam.getShippingCouponList().size() > 0){
            usCouponList = new ArrayList<>();
            for(int i = 0; i < buyParam.getShippingCouponList().size(); i++){
                Map<String, Object> scMap = buyParam.getShippingCouponList().get(i);
                Map<String, Object> uscoupon = new HashMap<>();
                uscoupon.put(scMap.get("key").toString(), scMap.get("value").toString());
                usCouponList.add(uscoupon);
            }
            usCoupons = gson.toJson(usCouponList);
        }*/

        resultMap.put("usCoupons", usCoupons);
        return resultMap;
    }

    @PostMapping("/buy")
    public ResponseEntity buy(HttpServletRequest request, @RequestBody(required = false) CartInfo cartInfo) {
        ResponseEntity result = null;
        OrderDetail orderDetail = null;
        OrderParam orderParam = new OrderParam();
        try {
            long userId = 0;
            if (UserUtils.isUserLogin()) {
                userId = UserUtils.getUserId();
            } else {
            	return result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "로그인 상태가 아닙니다.");
            }


            orderLogService.put("");

            cartInfo.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
            cartInfo.setUserId(userId);

            // 구매데이터 저장(바로주문&장바구니 주문)
            buyCartData(cartInfo);

            // 비회원 약관동의 - 로그인은 해당 로직 처리 X
            if (!cartInfo.isNoMemberLogin()) {
				/*
				 * HashMap<String, BuyPayment> buyPayments = orderService.getPaymentType(); if
				 * (buyPayments.keySet().isEmpty()) { throw new
				 * OrderException(MessageUtils.getMessage("M01256"), "/"); }
				 */

                orderParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
                orderParam.setUserId(userId);

                orderDetail = orderService.getApiBuyForStep1(orderParam);
//                orderDetail.setBuyPayments(buyPaymentsTypeCheck(buyPayments));
                orderDetail.setBuyPayments(buyPaymentsTypeCheck(new HashMap<>()));
            }

            result = ApiResponseEntity.data().put("paymentInfo", orderDetail).ok();

        } catch(OrderException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    private HashMap<String, BuyPayment> buyPaymentsTypeCheck(HashMap<String, BuyPayment> buyPayments){
        HashMap<String, BuyPayment> resultMap = new HashMap<>();
//        resultMap.put("bank", buyPayments.get("bank"));
        resultMap.put("point", buyPayments.get("point"));
        return resultMap;
    }

    private Map<String, Object> configOrderData(Config shopConfig){
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("pointUseMax",shopConfig.getPointUseMax());
        resultMap.put("pointUseMin",shopConfig.getPointUseMin());
        resultMap.put("pointUseRatio", shopConfig.getPointUseRatio());
        resultMap.put("minimumPaymentAmount",shopConfig.getMinimumPaymentAmount());

        return resultMap;
    }

    private void buyCartData(CartInfo cartInfo) {
        if (cartInfo.getCartIds() != null && !cartInfo.getCartIds().isEmpty()) {     // 장바구니 주문 일 경우
            CartParam cartParam = new CartParam();
            cartParam.setUserId(cartInfo.getUserId());
            cartParam.setSessionId(cartInfo.getSessionId());
            cartParam.setCartIds(cartInfo.getCartIds());
            cartParam.setCampaignCode(cartInfo.getCampaignCode());
            cartService.copyCartToOrderItemTemp(cartParam);
        } else {    // 바로주문 일 경우
            Cart cart = new Cart();
            cart.setUserId(cartInfo.getUserId());
            cart.setSessionId(cartInfo.getSessionId());
            cart.setCampaignCode(cartInfo.getCampaignCode());

            cart.setArrayRequiredItems(cartInfo.getArrayRequiredItems());
            cart.setItemSets(cartInfo.getItemSets());
            // 필수 추가정보
            cart.setTextOption(cartInfo.getTextOption());

            cartService.immediatelyBuy(cart);
        }
    }

    /**
     * 사용 가능한 쿠폰 리스트
     * @param request
     * @return
     */
    @GetMapping("/coupons")
    public ResponseEntity itemCoupons(HttpServletRequest request){
        ResponseEntity result = null;
        OrderParam orderParam = new OrderParam();
        orderParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
        orderParam.setUserId(UserUtils.getUserId());
        List<Map<String, Object>> resultList = new ArrayList<>();
        List<OrderCoupon> ocList = null;
        String itemUserCode = "";

        try {
            Buy buy = orderService.getBuyForStep1(orderParam, UserUtils.getUser());
            if (buy.getReceivers().size() > 0){
                List<Shipping> igList = buy.getReceivers().get(0).getItemGroups();
                if(!igList.isEmpty()){
                    for(int i = 0; i < igList.size(); i++){
                        Map<String, Object> resultMap = new HashMap<>();
                        if(igList.get(i).getBuyItem() == null){
                            itemUserCode = igList.get(i).getBuyItems().get(0).getItemUserCode();
                            ocList = igList.get(i).getBuyItems().get(0).getItemCoupons();
                        } else {
                            itemUserCode = igList.get(i).getBuyItem().getItemUserCode();
                            ocList = igList.get(i).getBuyItem().getItemCoupons();
                        }
                        resultMap.put("itemUserCode", itemUserCode);
                        resultMap.put("itemCoupons", ocList);
                        resultList.add(resultMap);
                    }
                }
            }
            result = ApiResponseEntity.data().put("itemCoupons",resultList).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    @PostMapping("/shpping-complete")
    public ResponseEntity ShppingComplete(HttpServletRequest request, @RequestBody(required = false) OrderParam orderSearchParam){
        ResponseEntity result = null;
        try {
            orderSearchParam.setUserId(UserUtils.getUserId());
            orderService.updateShppingComplete(orderSearchParam);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    @PostMapping("/confirm-purchase")
    public ResponseEntity confirmPurchase(HttpServletRequest request, @RequestBody(required = false) OrderParam orderSearchParam){
        ResponseEntity result = null;
        try {
            orderSearchParam.setUserId(UserUtils.getUserId());
            orderService.updateConfirmPurchase(orderSearchParam);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    // 반품신청 팝업 정보
    @GetMapping("/return-apply")
    public ResponseEntity returnApply(HttpServletRequest request, OrderParam orderSearchParam){
        ResponseEntity result = null;
        ReturnApplyInfo info = null;
        try {
            orderSearchParam.setUserId(UserUtils.getUserId());
            info = orderService.getReturnApplyInfo(orderSearchParam);
            result = ApiResponseEntity.data().put("returnInfo", info).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    // 반품신청
    @PostMapping("/return-apply")
    public ResponseEntity returnApplyProcess(HttpServletRequest request, @RequestBody(required = false) ReturnApply returnApply){
        ResponseEntity result = null;
        try {
            orderClaimApplyService.insertOrderReturnApply(returnApply);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    // 교환신청 팝업 정보
    @GetMapping("/exchange-apply")
    public ResponseEntity exchangeApply(HttpServletRequest request, OrderParam orderSearchParam){
        ResponseEntity result = null;
        ExchangeApplyInfo info = null;
        try {
            orderSearchParam.setUserId(UserUtils.getUserId());
            info = orderService.getExchangeApplyInfo(orderSearchParam);
            result = ApiResponseEntity.data().put("exchangeInfo", info).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    // 교환신청
    @PostMapping("/exchange-apply")
    public ResponseEntity exchangeApplyProcess(HttpServletRequest request, @RequestBody(required = false) ExchangeApply exchangeApply){
        ResponseEntity result = null;
        try {
            orderClaimApplyService.insertOrderExchangeApply(exchangeApply);
            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    // 취소신청 팝업정보
    @GetMapping("/cancel-apply")
    public ResponseEntity cancelApply(HttpServletRequest request, OrderParam orderParam){
        ResponseEntity result = null;
        CancelApplyInfo cancelApplyInfo = null;
        try {
            if (UserUtils.isUserLogin()) {
                orderParam.setUserId(UserUtils.getUserId());
//            } else if (UserUtils.isGuestLogin()) {
//                User user = UserUtils.getGuestLogin();
//                UserDetail userDetail = (UserDetail) user.getUserDetail();
//
//                orderParam.setGuestUserName(user.getUserName());
//                orderParam.setGuestPhoneNumber(userDetail.getPhoneNumber());
//            } else {
//                throw new PageNotFoundException();
            } else {
            	return result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "로그인 상태가 아닙니다.");
            }
            cancelApplyInfo = orderService.getCancelApplyInfo(orderParam);
            result = ApiResponseEntity.data().put("cancelApplyInfo", cancelApplyInfo).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    // 취소신청상품 부분정보
    @PostMapping("/refund-amount")
    public ResponseEntity refundAmount(HttpServletRequest request, @RequestBody(required = false) ClaimApply claimApply){
        ResponseEntity result = null;
        RefundInfo refundInfo = new RefundInfo();
        try {
            claimApply.setClaimType("1");
            OrderRefund orderRefund = orderRefundService.getOrderCancelRefundForUser(claimApply);
            if (orderRefund == null) {
                throw new PageNotFoundException();
            }

            if (ShopUtils.isMobilePage()) {
                RequestContextUtils.setTemplate("mobile");
            }

            ConfigPg configPg = configPgService.getConfigPg();
            String pgType = "";

            if (configPg != null) {
                pgType = configPg.getPgType().getCode().toLowerCase();
            } else {
                pgType = SalesonProperty.getPgService();
            }

            // 2017.05.25 Kspay 현금영수증정보 추가
            if("kspay".equals(pgType)) {
//			int cashReceiptId = cashReceiptMapper.getCashReceiptIdByParam(orderRefund.getOrderCode()) == null ? 0 : cashReceiptMapper.getCashReceiptIdByParam(orderRefund.getOrderCode());
//			model.addAttribute("cashReceiptId", cashReceiptId);
            }

            int claimApplyQuantity = 0;
            OrderPgData orderPgData = orderService.getOrderPgDataByOrderCode(orderRefund.getOrderCode());

            int claimApplyAmount = 0;

            String partCancel = "0";

            for (OrderCancelApply orderCancelApply : claimApply.getOrderCancelApplys()) {
                claimApplyAmount += orderCancelApply.getClaimApplyAmount();
                claimApplyQuantity += orderCancelApply.getClaimApplyQuantity();
            }

            if (claimApply.getOrder().getPayAmount() != claimApplyAmount) {
                partCancel = "1";
            }

            refundInfo.setPartCancel(partCancel);
            refundInfo.setOrderPgData(orderPgData);

            // orderRefund API 데이터 구성
            Map<String, Object> orderRefundApiInfo = new HashMap<>();
            orderRefundApiInfo.put("returnAmount", orderRefund.getGroups().get(0).getReturnAmount());
            orderRefundApiInfo.put("totalReturnAmount", orderRefund.getTotalReturnAmount());
            orderRefundApiInfo.put("isAutoCancel", orderRefund.isAutoCancel());
            orderRefundApiInfo.put("totalAddShippingAmount", orderRefund.getTotalAddShippingAmount());
            orderRefundApiInfo.put("totalItemReturnAmount", orderRefund.getTotalItemReturnAmount());
            orderRefundApiInfo.put("isWriteBankInfo", orderRefund.isWriteBankInfo());
            orderRefundApiInfo.put("claimApplyQuantity", claimApplyQuantity);
            orderRefundApiInfo.put("totalOrderQuantity", orderRefund.getTotalOrderQuantity());

            refundInfo.setOrderRefundApiInfo(orderRefundApiInfo);

            result = ApiResponseEntity.data().put("refundInfo", refundInfo).ok();

        } catch(OpRuntimeException e) {
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    // 취소 신청
    @PostMapping("/cancel-apply")
    public ResponseEntity cancelProcess(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) ClaimApply claimApply){
        ResponseEntity result = null;
        try {
            claimApply.setRequest(request);
            claimApply.setResponse(response);
//            orderClaimApplyService.insertOrderCancelApply(claimApply);

            if ("1".equals(claimApply.getClaimRefundType())) {
            	orderClaimApplyService.giveGoodsInsertOrderCancelApply(claimApply, true);		// 기부 포인트 즉시 환불 처리
            } else if ("2".equals(claimApply.getClaimRefundType())) {
            	orderClaimApplyService.giveGoodsInsertOrderCancelApply(claimApply, false);		// 주문취소 신청만 처리
            }

            result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
        } catch (OrderException e) {
            log.error("[/api/order/cancel/process] ERROR : {}", e.getErrorMessage(), e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        } catch (Exception e) {
            //result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getMessage());
        	log.error("[/api/order/cancel-apply] OrderCode", claimApply.getOrderCode().toString());
        	log.error("[/api/order/cancel-apply] ERROR-30", e.getMessage(), e);
        	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "ERROR-30: System Error (시스템 에러)");
        }

//        try {
//
//            String refundCode = claimApply.getRefundCode();
//            Order order = claimApply.getOrder();
//            if ("1".equals(claimApply.getClaimRefundType()) && !ObjectUtils.isEmpty(refundCode) && order != null) {
//
//                OrderRefund orderRefund = orderRefundService.getOrderRefundByCode(refundCode);
//                if (orderRefund != null) {
//                    googleAnalyticsService.partRefund(request, order, orderRefund);
//                }
//            }
//
//
//        } catch (Exception e) {
//            log.error("Google Analytics Service Part Refund Error", e);
//        }

        return result;
    }

    private String getLoginIdByToken(String token) throws IllegalArgumentException{

        try {
            return (String) JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_ID));
        } catch (RuntimeException e) {
            String errorMessage = "유효한 토큰 정보가 아닙니다.";
            log.error(errorMessage + " {}", token);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * 이지페이 결제창 응답결과 페이지
     * @param session
     * @param request
     * @param model
     * @return
     */
    @GetMapping(value="/easypay/easypay_request")
    @RequestProperty(layout="base")
    public String easypayRequest(HttpSession session, HttpServletRequest request, Model model) {

        return ViewUtils.view();
    }

    /**
     * 나이스페이 가상계좌 입금 통보
     * @return
     */
//    @GetMapping("nicepay-vacct")
//    public @ResponseBody String nicepayVacct(PgData pgData, HttpServletRequest request) {
//        return handleNicepayResponse(pgData, request);
//    }
//
//    @PostMapping("nicepay-vacct")
//    public @ResponseBody String nicepayVacct2(PgData pgData, HttpServletRequest request) {
//        return handleNicepayResponse(pgData, request);
//    }
//
//    private String handleNicepayResponse(PgData pgData, HttpServletRequest request) {
//        if (pgData == null) {
//            return "ERROR";
//        }
//
//        Enumeration params = request.getParameterNames();
//        while (params.hasMoreElements()) {
//            String paramName = (String) params.nextElement();
//            log.debug("[NICEPAY] {} = {} ", paramName, request.getParameter(paramName));
//        }
//
//        try {
//            pgData.setRequest(request);
//
//            orderLogService.put(pgData.getOrderCode(), true);
//
//            return nicepayService.confirmationOfPayment(pgData);
//        } catch (Exception e) {
//            //log.error("ERROR: {}", e.getMessage(), e);
//        	log.error("ERROR-30: System Error (시스템 에러)");
//            return "ERROR";
//        }
//    }

    private boolean validateToken(HttpServletRequest request, String token, User user) throws IllegalArgumentException {

        try {

            String loginType = (String) JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_LOGIN_TYPE));

            TokenAuthUserInfo tokenAuthUserInfo = new TokenAuthUserInfo(
                    loginType,
                    user.getLoginId(),
                    user.getPassword(),
                    JwtUtils.getJti(token)
            );

            return tokenService.validateToken(token, tokenAuthUserInfo);
        } catch (RuntimeException e) {
            String errorMessage = "유효한 토큰 정보가 아닙니다.";
            log.error(errorMessage + " {}", token);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @GetMapping("/naverpay/payment")
    public String naverpayPayment(HttpSession session, HttpServletRequest request,
                                  @RequestParam("orderCode") String orderCode,
                                  @RequestParam(value = "paymentId", required = false) String paymentId,
                                  @RequestParam("amount") String amount,
                                  @RequestParam("encryptedString") String encryptedString,
                                  @RequestParam(value = "resultCode") String resultCode,
                                  @RequestParam(value = "resultMessage", required = false) String resultMessage) throws Exception {

        String redirect = "";
        String naverpayErrorCode = "";
        String failUrl = SalesonProperty.getSalesonUrlFrontend() + "/order/step1.html?naverpayErrorCode=";
        String successUrl = SalesonProperty.getSalesonUrlFrontend() + "/order/step2.html";

        String decryptedString = CipherUtils.decrypt(encryptedString);
        String sessionId = decryptedString.split("\\|")[1];

        if ("Fail".equals(resultCode)) {
            naverpayErrorCode = resultMessage;
            redirect = failUrl + naverpayErrorCode;;

        } else {
            OrderParam orderParam = new OrderParam();

            PgData pgData = new PgData();
            pgData.setOrderCode(orderCode);
            pgData.setPaymentId(paymentId);
            pgData.setAmount(amount);

            if (ServiceType.LOCAL) {
                failUrl = "http://localhost/order/step1.html?naverpayErrorCode=";
                successUrl = "http://localhost/order/step2.html";
            }

            int orderSequence = 0;

            try {
                orderParam.setOrderCode(pgData.getOrderCode());
                orderParam.setUserId(UserUtils.getUserId());
                orderParam.setSessionId(sessionId);

                orderCode = orderService.insertOrder(orderParam, pgData, session, request);
                orderSequence = Integer.parseInt(orderCode.split("/")[0]);
                orderParam.setOrderSequence(orderSequence);

                StringBuffer sb = new StringBuffer();

                sb.append("?orderCode=" + orderCode.split("/")[1]);
                sb.append("&orderSequence=" + orderSequence);

                redirect = successUrl + sb.toString();

            } catch (OrderException oe) {
                if (null != oe.getOrderCancelFail()) {
                    OrderCancelFail orderCancelFail = oe.getOrderCancelFail();
                    orderCancelFailService.save(orderCancelFail);
                }

                if (oe.getErrorMessage().indexOf("결제 요청 실패") > -1) {
                    log.error("[/api/order/pay] ERROR : {}",  oe.getErrorMessage());
                	log.error("ERROR-59: 결제 요청 실패");

                    ApiResponseEntity.Builder builder = new ApiResponseEntity.Builder();
                    builder.status(HttpStatus.BAD_REQUEST);
                    builder.put("status", HttpStatus.BAD_REQUEST.value());
                    builder.put("code", HttpStatus.BAD_REQUEST.name());
                    builder.put("message","ERROR-59: 결제 요청 실패");			//builder.put("message",oe.getMessage());
                    builder.put("description", "ERROR-59: 결제 요청 실패");		//builder.put("description", oe.getMessage());

                    log.error("[/api/order/pay] ERROR : {}", ApiError.SYSTEM_ERROR.getDescription(), oe);
                    naverpayErrorCode = "SYSTEM_ERROR";

                } else if (oe.getErrorMessage().indexOf("금액오류(200원 이하 이체불가)") > -1) {
                    log.error("[/api/order/pay] ERROR : {}", ApiError.BAD_REQUEST_PAY_MIN_FAIL.getDescription(), oe);
                    naverpayErrorCode = "BAD_REQUEST_PAY_MIN_FAIL";
                } else {
                    log.error("[/api/order/pay] ERROR : {}", ApiError.SYSTEM_ERROR.getDescription(), oe);
                    naverpayErrorCode = "SYSTEM_ERROR";
                }

                redirect = failUrl + naverpayErrorCode;

            } catch (Exception e) {
                log.error("[/api/order/pay] ERROR : {}", ApiError.SYSTEM_ERROR.getDescription(), e);
                naverpayErrorCode = "SYSTEM_ERROR";

                redirect = failUrl + naverpayErrorCode;
            }
        }

        return "redirect:" + redirect;
    }
}