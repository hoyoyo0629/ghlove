package saleson.api.orderagency;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DeviceUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.api.cart.domain.CartInfo;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.item.domain.ItemDetailInfo;
import saleson.api.item.domain.SellerInfo;
import saleson.api.item.support.ItemDataSupport;
import saleson.api.mypage.domain.CntrPointInfo;
import saleson.common.enumeration.OrderCodePrefix;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.JwtUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.seller.main.domain.SellerEncryptor;
import saleson.shop.cart.CartService;
import saleson.shop.cart.domain.Cart;
import saleson.shop.cart.support.CartParam;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.item.ItemFrontService;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.mypage.domain.CntrPoint;
import saleson.shop.order.OrderService;
import saleson.shop.order.api.OrderDetail;
import saleson.shop.order.domain.Buy;
import saleson.shop.order.domain.BuyPayment;
import saleson.shop.order.givepoint.OrderGivePointService;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.order.log.OrderLogService;
import saleson.shop.order.support.OrderException;
import saleson.shop.order.support.OrderParam;
import saleson.shop.orderagency.OrderAgencyService;
import saleson.shop.orderagency.domain.OrderAgencyInfo;
import saleson.shop.orderagency.domain.OrderAgencyLoginConfirmInfo;
import saleson.shop.orderagency.domain.OrderAgencyManagerInfo;
import saleson.shop.orderagency.domain.OrderAgentOrderData;
import saleson.shop.orderagency.domain.OrderAgentResult;
import saleson.shop.point.support.OrderPointParam;
import saleson.shop.user.domain.UserDetail;


@RestController("orderAgencyController")
@RequestMapping("/api/order-agency")
public class OrderAgencyController {

    private static final Logger log = LoggerFactory.getLogger(OrderAgencyController.class);
    
    @Autowired
    private OrderAgencyService orderAgencyService;
    
    @Autowired
    private OrderGivePointService orderGivePointService;
    
    @Autowired
    private ItemFrontService itemFrontService;
    
    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemDataSupport itemDataSupport;
    
    @Autowired
    private SellerEncryptor sellerEncryptor;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private CodeService codeService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderLogService orderLogService;
    
    @Autowired
    private CartService cartService;
    

    /**
     * 화면에서 사용할 암호화 키 조회
     * @param request
     * @return
     */
	@GetMapping("/getPublicKey")
	public ResponseEntity<?> getPublicKey(HttpServletRequest request) {
		String salesonid = orderAgencyService.getSalesonId(request);
		OrderAgentResult result = new OrderAgentResult();
		if (StringUtils.hasLength(salesonid)) {
			result = orderAgencyService.selectPublicKey(salesonid);
		} else {		// 진행 불가
			result.setRstCd("NONE_SESSION_ID");
			result.setRstMsg("권한이 없습니다.");
		}
		return ApiResponseEntity.data().put("result", result).ok();
	}

	/**
	 * 로그인 처리(관리자 아이디 / 비밀번호 체크, 토큰 발급, 기부자 전화번호 저장)
	 * @param request
	 * @param orderAgencyInfo
	 * @return
	 */
	@PostMapping("/checkAgencyLoginInfo")
	public ResponseEntity<?> checkAgencyLoginInfo(HttpServletRequest request, @RequestBody(required = true) OrderAgencyInfo orderAgencyInfo) {
		String salesonid = orderAgencyService.getSalesonId(request);
		OrderAgentResult result = new OrderAgentResult();
		if (StringUtils.hasLength(salesonid)) {
			result = orderAgencyService.selectAgencyLoginInfo(salesonid, orderAgencyInfo);
		} else {		// 진행 불가
			result.setRstCd("NONE_SESSION_ID");
			result.setRstMsg("권한이 없습니다.");
		}
		return ApiResponseEntity.data().put("result", result).ok();
	}

	/**
	 * 로그인 입력 정보와 기부자 본인인증 정보 검증
	 * @param request
	 * @param orderAgencyInfo
	 * @return
	 */
	@PostMapping("/saveAgencyCntrbtrInfo")
	public ResponseEntity<?> saveAgencyCntrbtrInfo(HttpServletRequest request, @RequestBody(required = true) OrderAgencyInfo orderAgencyInfo) {
		String salesonid = orderAgencyService.getSalesonId(request);
		
		OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo = new OrderAgencyLoginConfirmInfo();
		orderAgencyLoginConfirmInfo.setUserSessionId(salesonid);
		orderAgencyLoginConfirmInfo.setValidAccessCd(JwtUtils.getToken(request));
		
		OrderAgentResult result = new OrderAgentResult();
		try {
			orderAgencyService.updateCntrbtrMobileInfo(orderAgencyLoginConfirmInfo, orderAgencyInfo.getCntrbtrMobile(), orderAgencyInfo.getMberCi());
			result.setRstCd("SUCCESS");
		} catch (OpRuntimeException e) {
			result.setRstCd("FAIL");
			result.setRstMsg(e.getErrorMessage());
		}
		
		return ApiResponseEntity.data().put("result", result).ok();
	}
	


    /**
     * 상품 상세정보 API (itemUserCode)
     *
     * @param request
     * @param itemUserCode
     * @return
     */
    @PostMapping("/item-detail/{itemUserCode}")
    public ResponseEntity details(HttpServletRequest request, @PathVariable("itemUserCode") String itemUserCode) {
        
        if (!UserUtils.isUserLogin()) {
        	return ApiResponseEntity.error(ApiError.NO_LOGIN);
        }
    	
        ResponseEntity result = null;
        
        if (itemFrontService.isItemRestrict()) {
        	return ApiResponseEntity.error(ApiError.RESTRICT_ITEM);
        }

//        PointPolicy pointPolicy = null;
//        List<ItemOther> itemOthers = null;

        try {
            Item item = itemService.getItemByItemUserCode(itemUserCode);
            
            if (item == null || "N".equalsIgnoreCase(item.getDisplayFlag())) {
                //throw new OrderException("주문 가능한 상품이 없습니다.");
            	return ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            }
            

            itemDataSupport.setItemDataNvl(item);
            
            String adultErr = "N";
            if (/*"Y".equalsIgnoreCase(item.getAdultItemYn()) &&*/ !isAdult(request)) {
            	adultErr = "Y";
            }

            // 지급 포인트 정보
            Config config = configService.getShopConfigCache(Config.SHOP_CONFIG_ID);

            OrderPointParam orderPointParam = new OrderPointParam();
            orderPointParam.setItemId(item.getItemId());
            orderPointParam.setRepeatDayEndTime(config.getRepeatDayEndTime());
            orderPointParam.setRepeatDayStartTime(config.getRepeatDayStartTime());

//            pointPolicy = pointService.getPointPolicyByOrderPointParam(orderPointParam);

            // 판매자 정보
            Seller seller = sellerService.getSellerById(item.getSellerId());
            seller.decrypt(sellerEncryptor, false);

            CodeParam param = new CodeParam();
            param.setCodeType("QNA_GROUPS");
            List<Code> qnaGroups = codeService.getCodeList(param);
            
            List<Code> qnaGroupsItem = new ArrayList<>();
            
            for (Code code : qnaGroups) {
				if (code.getExtentionCode() != null && code.getExtentionCode().contains("item")) {
					qnaGroupsItem.add(code);
				}
			}
            
            String userName = "";
            if (SecurityUtils.isLogin()) {
            	userName = UserUtils.getUser().getUserName();
            }
            
            // 같이 구매한 상품 목록
//            itemOthers = itemService.getItemOtherList(item.getItemId());
            result = ApiResponseEntity.data()
                    .put("item", new ItemDetailInfo(item))
//                    .put("pointPolicy", pointPolicyDataSet(pointPolicy))
//                    .put("earnPoint", new ItemEarnPoint(pointPolicy, item))
//                    .put("seller", seller)
                    .put("seller", new SellerInfo(seller))
                    .put("userId",UserUtils.getUserId())
                    .put("userName", userName)
                    .put("breadcrumbs", item.getBreadcrumbs())
//                    .put("cardBenefits", cardBenefitsService.getTodayCardBenefits(DateUtils.getToday()))
//                    .put("reviewFilters", getReviewFilterInfos(item))
                    .put("config", config)
                    .put("qnaGroups", qnaGroupsItem)
                    .put("adultErr", adultErr)
                    .put("imgDescList", itemService.getItemImagesExplain(item.getItemId()))
                    .put("imgDescListLinkView", itemFrontService.getItemImagesExplainByLinkView(itemUserCode))
//                    .list(itemOthers)
                    .ok();

//        } catch (OrderException e) {
//            if (e.getErrorMessage().equals("주문 가능한 상품이 없습니다.")) {
//                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
//            } else {
//                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
//            }
        } catch (OpRuntimeException e) {
            if (e.getErrorMessage().equals("답례품정보가 없습니다.") || e.getErrorMessage().equals("상품정보가 없습니다.")) {
                result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
            } else {
                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
            }
        }
        return result;
    }
	

	/**
	 * 로그인 입력 정보와 기부자 본인인증 정보 검증
	 * @param request
	 * @param orderAgencyInfo
	 * @return
	 */
	@PostMapping("/getOrderAgencyCntrPointList")
	public ResponseEntity<?> getOrderAgencyCntrPointList(HttpServletRequest request, @RequestBody(required = true) SearchParam searchParam) {
        
        if (!UserUtils.isUserLogin()) {
        	return ApiResponseEntity.error(ApiError.NO_LOGIN);
        }
        
		OrderAgentResult result = new OrderAgentResult();
		
		long userId = 0;
		try {
			userId = orderAgencyService.checkValidAgencyLogin(request);
		} catch (OpRuntimeException e) {
			result.setRstCd("FAIL");
			result.setRstMsg(e.getErrorMessage());
			
			return ApiResponseEntity.data()
					.put("result", result).ok();
		}
		
		List<CntrPoint> list = null;
		try {
			list = orderAgencyService.getOrderAgencyCntrPointList(userId, searchParam);
			result.setRstCd("SUCCESS");
		} catch (OpRuntimeException e) {
			result.setRstCd("FAIL");
			result.setRstMsg(e.getErrorMessage());
		}
		
		return ApiResponseEntity.data()
				.put("result", result)
				.put("list", list)
				.put("userName", orderAgencyService.getOrderAgencyCntrbtrByUserId(userId).getUserName())
				.put("page", searchParam.getPagination().getCurrentPage())
				.put("totalPages", searchParam.getPagination().getTotalPages())
				.put("totalItems", searchParam.getPagination().getTotalItems())
				.put("itemsPerPage", searchParam.getPagination().getItemsPerPage()).ok();
	}
	

	
    @GetMapping("/item/getUserCntrPoint")
    public ResponseEntity getGntrPoint(HttpServletRequest request){
        ResponseEntity result = null;

        User userInfo = orderAgencyService.getOrderAgencyCntrbtrInfoByRequest(request);
        
        if (userInfo == null) {
        	return ApiResponseEntity.error(ApiError.NOT_JOIN_USER);
        }
        
        try {
//        	long userId = UserUtils.getUser().getUserId();
        	
        	List<OrderGivePoint> pointList;
        	if (userInfo.getUserId() > 0) {
        		pointList = orderGivePointService.getGiveBlcePointListByUserId(userInfo.getUserId());
        	} else {
        		pointList = new ArrayList<>();
        	}

            List<CntrPointInfo> infoList = new ArrayList<>();

            if(!pointList.isEmpty()) {
            	for(OrderGivePoint cntrPoint : pointList) {
            		CntrPointInfo info = new CntrPointInfo();
            		info.setLocgovCode(cntrPoint.getCntrLocgovCode());
            		info.setCntrSn(cntrPoint.getCntrSn());
            		info.setCntrPoint(String.valueOf(cntrPoint.getCntrPoint()));
            		info.setCntrUsePoint(String.valueOf(cntrPoint.getCntrUsePoint()));
            		info.setCntrBlcePoint(String.valueOf(cntrPoint.getCntrBlcePoint()));
            		infoList.add(info);
            	}
            }

            result = ApiResponseEntity.data()
            		.list(infoList)
            		.ok();
            
        } catch (UserException e) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST, e.getErrorMessage());
        } catch(Exception e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }
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
            	throw new OrderException(ApiError.BAD_BUY_NO_ITEM, "/admin/order-agency/cntr-list.html");
            }

//            Config shopConfig = configService.getShopConfig(Config.SHOP_CONFIG_ID);

            User userInfo = orderAgencyService.getOrderAgencyCntrbtrInfoByRequest(request);
            
            if (userInfo == null || userInfo.getUserId() <= 0) {
            	throw new OrderException(ApiError.BAD_BUY_NO_ITEM, "/admin/order-agency/cntr-list.html");
            }
            
            long userId = userInfo.getUserId();
            ApiResponseEntity.Builder data = new ApiResponseEntity.Builder();

            orderParam.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
            orderParam.setUserId(userId);

            Buy buy = orderService.getBuyForStep1(orderParam, userInfo);
            
            if (buy == null) {
            	errorMsg = "결제가능 상품이 없습니다.";
//                throw new OrderException("결제가능 상품이 없습니다.", "/cart");
            	throw new OrderException(ApiError.BAD_BUY_NO_ITEM, "/admin/order-agency/cntr-list.html");
            }

            buy.setBuyPayments(buyPayments);

            orderLogService.put(buy.getOrderCode());

//            ConfigPg configPg = configPgService.getConfigPg();
//            String useEscrow = "";
//            String autoCashReceipt = "";
//            Boolean isUseNpayPayment = false;
/*
            if (configPg != null) {
                isUseNpayPayment = configPg.isUseNpayPayment();
                useEscrow = configPg.isUseEscroow() ? "Y" : "N";
                autoCashReceipt = configPg.isUseAutoCashReceipt() ? "Y" : "N";
            } else {
                useEscrow = environment.getProperty("pg.useEscrow");
                autoCashReceipt = environment.getProperty("pg.autoCashReceipt");
            }
            */
            List<CodeInfo> telCodeList = CodeUtils.getCodeInfoList("TEL");
            List<CodeInfo> phoneCodeList = CodeUtils.getCodeInfoList("PHONE");
            List<CodeInfo> emailCodeList = CodeUtils.getCodeInfoList("EMAIL");
//            phoneCodeList.addAll(telCodeList);
            
            List<OrderGivePoint> givePointList = orderGivePointService.getGiveBlcePointListByUserId(userId);
            result = data.put("buy", buy)
//                    .put("useCoupon", ShopUtils.useCoupon())
                    .put("userData", buy.getUserData())
//                    .put("config", configOrderData(shopConfig))
//                    .put("isUseNpayPayment",isUseNpayPayment)
//                    .put("useEscrow",useEscrow)
//                    .put("autoCashReceipt", autoCashReceipt)
                    .put("telCodes", telCodeList)
                    .put("phoneCodes", phoneCodeList)
                    .put("emailCodes", emailCodeList)
                    .put("givePointList", givePointList)
//                    .put("cashbillTypes", enumMapper.get("CashbillType"))
                    .ok();

        } catch(OrderException e){
//        	if (StringUtils.isEmpty(errorMsg) && !StringUtils.isEmpty(e.getErrorMessage())) {
//        		errorMsg = e.getErrorMessage();
//        	}
        	log.error("OrderController paymentStep OrderException :: " + errorMsg, e);
//            if(errorMsg.indexOf("설정된 결제 방법이 없습니다") > -1){
//                result = ApiResponseEntity.error(ApiError.BAD_REQUEST, errorMsg);
//            } else if (errorMsg.indexOf("결제가능 상품이 없습니다.") > -1) {
//                result = ApiResponseEntity.error(ApiError.BAD_BUY_NO_ITEM, errorMsg);
//            } else {
//                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, errorMsg);
//            }
        	throw e;
        } catch(OpRuntimeException e){
//        	if (StringUtils.isEmpty(errorMsg) && !StringUtils.isEmpty(e.getErrorMessage())) {
//        		errorMsg = e.getErrorMessage();
//        	}
        	log.error("OrderController paymentStep OpRuntimeException :: " + errorMsg, e);
//            if(errorMsg.indexOf("설정된 결제 방법이 없습니다") > -1){
//                result = ApiResponseEntity.error(ApiError.BAD_REQUEST, errorMsg);
//            } else if (errorMsg.indexOf("결제가능 상품이 없습니다.") > -1) {
//                result = ApiResponseEntity.error(ApiError.BAD_BUY_NO_ITEM, errorMsg);
//            } else {
//                result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, errorMsg);
//            }
            throw e;
        }
        return result;
    }

    @PostMapping("/buy")
    public ResponseEntity buy(HttpServletRequest request, @RequestBody(required = false) CartInfo cartInfo) {
        ResponseEntity result = null;
        OrderDetail orderDetail = null;
        OrderParam orderParam = new OrderParam();
        
        User userInfo = orderAgencyService.getOrderAgencyCntrbtrInfoByRequest(request);
        long userId = 0;
        
        if (userInfo != null && userInfo.getUserId() > 0) {
            userId = userInfo.getUserId();
        } else {
          	return ApiResponseEntity.data()
                    .put("errMsg", "로그인 상태가 아닙니다.")
                    .ok();
        }
        
        try {

            orderLogService.put("");

            cartInfo.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
            cartInfo.setUserId(userId);

            // 구매데이터 저장(바로주문&장바구니 주문)
            buyCartData(cartInfo);

            result = ApiResponseEntity.data().put("paymentInfo", orderDetail).ok();

        } catch(OrderException e) {
          	result = ApiResponseEntity.data()
                    .put("errMsg", e.getErrorMessage())
                    .ok();
        }
        return result;
    }

    
    // 결제하기 - PG
	@PostMapping("/giveGoodsSavePay")
	public ResponseEntity giveGoodsSavePay(HttpServletRequest request,
           				HttpSession session, @RequestBody(required = false) Buy buy) {
	   ResponseEntity result = null;
//           Enumeration params = request.getParameterNames();
//           while(params.hasMoreElements()){
//               String paramName = (String)params.nextElement();
//               // System.out.println("Attribute1 Name - "+paramName+", Value - "+request.getParameter(paramName));
//           }

	   User userInfo = orderAgencyService.getOrderAgencyCntrbtrInfoByRequest(request);
       if (userInfo == null || userInfo.getUserId() <= 0) {
          	result = ApiResponseEntity.data()
                    .put("errMsg", "로그인 상태가 아닙니다.")
                    .ok();
//            	return ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "로그인 상태가 아닙니다.");
        	return result;
       }
       
       try {
           // 네이버페이 returnParam 에 전달할 token, sessionId 암호화 처리
           String orderCode = orderService.getNewOrderCode(OrderCodePrefix.FRONT);

           orderLogService.put(buy.getOrderCode());

           buy.setOrderCode(orderCode);
           buy.setUserId(userInfo.getUserId());
           buy.setSessionId(ShopUtils.getSalesOnIdByHeader(request));
           buy.setDeviceType(buy.getDeviceType());
           buy.setRealDeviceType(DeviceUtils.resolveDevice(request));
           buy.setUserIp(CommonUtils.getClientIp(request));

           result = ApiResponseEntity.data()
                   .put("data", orderService.orderAgencySaveGiveOrderTempAndPay(session, buy, request, userInfo))		// 결제 로직
                   .ok();

       } catch (OrderException e){
//           	result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
    	   result = ApiResponseEntity.data()
                   .put("errMsg", e.getErrorMessage())
                   .ok();
           log.error("OrderController giveGoodsSavePay error1 :: ", e);
       } catch (OpRuntimeException | NullPointerException e){
    	   if (e instanceof OpRuntimeException) {
//                   result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, ((OpRuntimeException) e).getErrorMessage());
    		   result = ApiResponseEntity.data()
                       .put("errMsg", ((OpRuntimeException) e).getErrorMessage())
                       .ok();
    	   } else {
//           		result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "문제가 발생했습니다.");
    		   result = ApiResponseEntity.data()
                       .put("errMsg", "문제가 발생했습니다.")
                       .ok();
    	   }
           log.error("OrderController giveGoodsSavePay error2 :: ", e);
       }

       return result;
	}

    @PostMapping("/order-detail")
    public ResponseEntity orderDetails(HttpServletRequest request, OrderParam orderSearchParam) {
        ResponseEntity result = null;
        OrderDetail orderDetail = null;
        try {
        	User userInfo = orderAgencyService.getOrderAgencyCntrbtrInfoByRequest(request);
        	if (userInfo == null || userInfo.getUserId() <= 0) {
        		result = ApiResponseEntity.data()
        				.put("errMsg", "로그인 상태가 아닙니다.")
        				.ok();
	//                	return ApiResponseEntity.error(ApiError.SYSTEM_ERROR, "로그인 상태가 아닙니다.");
        		return result;
        	}
        	
            orderLogService.put(orderSearchParam.getOrderCode());

            orderSearchParam.setUserId(userInfo.getUserId());
            orderDetail = orderService.getApiOrderDetail(orderSearchParam);
            
            Item item = itemService.getItemBy(orderDetail.getItem().get(0).getItemId());
            
            Seller seller = sellerService.getSellerById(item.getSellerId());
            seller.decrypt(sellerEncryptor, false);
            
            Seller sellerData = new Seller();
            sellerData.setCompanyName(seller.getCompanyName());
            sellerData.setPhoneNumber(seller.getPhoneNumber());
            sellerData.setBusinessLocation(seller.getBusinessLocation());
            sellerData.setAddressDetail(seller.getAddressDetail());
            
            OrderAgencyManagerInfo manager = orderAgencyService.selectOrderAgencyManagerInfo(UserUtils.getUser().getUserId());
            
            if (manager == null) {
            	throw new OpRuntimeException("로그인 관리자 정보가 없습니다.");
            }
            
            List<OrderGivePoint> givePointList = orderGivePointService.getGiveBlcePointListByUserId(userInfo.getUserId());
            result = ApiResponseEntity.data().put("order", orderDetail)
                    						.put("givePointList", givePointList)
                    						.put("manager", manager)
                    						.put("seller", sellerData).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
    }

    @PostMapping("/order-detail-data")
    public ResponseEntity orderDetailsByDataId(HttpServletRequest request, @RequestParam(name = "dataId", required = true) String dataId) {
        ResponseEntity result = null;
        OrderDetail orderDetail = null;
        try {
        	OrderAgentOrderData orderAgentOrderData = orderAgencyService.selectOrderAgencyTempDataByDataId(dataId);
        	
            orderLogService.put(orderAgentOrderData.getOrderCode());
            
            OrderParam orderParam = new OrderParam();
            
            orderParam.setOrderCode(orderAgentOrderData.getOrderCode());
            orderParam.setUserId(orderAgentOrderData.getUserId());

            orderDetail = orderService.getApiOrderDetail(orderParam);
            
            Item item = itemService.getItemBy(orderDetail.getItem().get(0).getItemId());
            
            Seller seller = sellerService.getSellerById(item.getSellerId());
            seller.decrypt(sellerEncryptor, false);
            
            Seller sellerData = new Seller();
            sellerData.setCompanyName(seller.getCompanyName());
            sellerData.setPhoneNumber(seller.getPhoneNumber());
            sellerData.setBusinessLocation(seller.getBusinessLocation());
            sellerData.setAddressDetail(seller.getAddressDetail());
            
            OrderAgencyManagerInfo manager = orderAgencyService.selectOrderAgencyManagerInfo(orderAgentOrderData.getManagerId());
            
            if (manager == null) {
            	throw new OpRuntimeException("관리자 정보가 없습니다.");
            }
            
            List<OrderGivePoint> givePointList = orderGivePointService.getGiveBlcePointListByUserId(orderParam.getUserId());
            result = ApiResponseEntity.data().put("order", orderDetail)
                    						.put("givePointList", givePointList)
                    						.put("manager", manager)
                    						.put("seller", sellerData).ok();
        } catch(OpRuntimeException e){
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR, e.getErrorMessage());
        }
        return result;
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

            cartService.immediatelyBuy(cart);
        }
    }
	
	private boolean isAdult(HttpServletRequest request) {
		User userInfo = orderAgencyService.getOrderAgencyCntrbtrInfoByRequest(request);
		UserDetail userDetail = (UserDetail) userInfo.getUserDetail();
		LocalDate now = LocalDate.now();
		LocalDate parsedBirthDate = LocalDate.parse(userDetail.getBirthday(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		int americanAge = now.minusYears(parsedBirthDate.getYear()).getYear();
		return americanAge >= 19;
	}
	
}
