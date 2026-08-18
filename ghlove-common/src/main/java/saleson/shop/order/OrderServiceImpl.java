package saleson.shop.order;

import static saleson.common.Const.DATETIME_FORMAT;
import static saleson.common.Const.DATE_FORMAT;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lgcns.kmpay.dto.DealApproveDto;
import com.onlinepowers.framework.common.ServiceType;
import com.onlinepowers.framework.context.util.RequestContextUtils;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.CommonUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.DeviceUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.NumberUtils;
import com.onlinepowers.framework.util.PoiUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.api.common.enumerated.ApiError;
import saleson.common.alimtalk.Alimtalk;
import saleson.common.alimtalk.AlimtalkService;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.CashbillIssueType;
import saleson.common.enumeration.CashbillStatus;
import saleson.common.enumeration.CashbillType;
import saleson.common.enumeration.IdType;
import saleson.common.enumeration.LogType;
import saleson.common.enumeration.OrderCodePrefix;
import saleson.common.enumeration.OrderLogType;
import saleson.common.enumeration.SmsType;
import saleson.common.enumeration.TaxType;
import saleson.common.enumeration.UserType;
import saleson.common.file.ExcelCellStyleUtils;
import saleson.common.nuri2.Nuri2NrmsgData;
import saleson.common.nuri2.Nuri2Service;
import saleson.common.opmanager.count.OpmanagerCount;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.utils.JwtUtils;
import saleson.common.utils.PaycoUtils;
import saleson.common.utils.PointUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.Cashbill;
import saleson.model.CashbillIssue;
import saleson.model.ConfigPg;
import saleson.model.GiftItem;
import saleson.model.OrderCancelFail;
import saleson.model.OrderGiftItem;
import saleson.seller.main.SellerMapper;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.seller.user.SellerUserService;
import saleson.shop.accountnumber.AccountNumberService;
import saleson.shop.cart.CartMapper;
import saleson.shop.cart.domain.OrderQuantity;
import saleson.shop.cart.support.CartParam;
import saleson.shop.categories.CategoriesMapper;
import saleson.shop.categories.domain.ProductsRepCategories;
import saleson.shop.config.ConfigLogService;
import saleson.shop.config.ConfigService;
import saleson.shop.config.domain.Config;
import saleson.shop.coupon.CouponService;
import saleson.shop.coupon.domain.Coupon;
import saleson.shop.coupon.domain.OrderCoupon;
import saleson.shop.coupon.domain.api.BuyCouponList;
import saleson.shop.coupon.domain.api.BuyShippingCouponInfo;
import saleson.shop.coupon.support.UserCouponParam;
import saleson.shop.deliverycompany.DeliveryCompanyMapper;
import saleson.shop.deliverycompany.DeliveryCompanyService;
import saleson.shop.deliverycompany.domain.DeliveryCompany;
import saleson.shop.giftitem.GiftItemService;
import saleson.shop.item.ItemMapper;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemOption;
import saleson.shop.item.domain.ItemSet;
import saleson.shop.item.domain.api.ItemInfo;
import saleson.shop.naverpay.NaverPaymentApi;
import saleson.shop.order.api.ApiOrderList;
import saleson.shop.order.api.OrderDetail;
import saleson.shop.order.api.PaymentInfo;
import saleson.shop.order.api.ShippingInfo;
import saleson.shop.order.claimapply.OrderClaimApplyMapper;
import saleson.shop.order.claimapply.domain.AdminClaimApply;
import saleson.shop.order.claimapply.domain.AdminClaimApplyItem;
import saleson.shop.order.claimapply.domain.CancelApplyInfo;
import saleson.shop.order.claimapply.domain.ClaimApply;
import saleson.shop.order.claimapply.domain.ExchangeApplyInfo;
import saleson.shop.order.claimapply.domain.OrderCancelApply;
import saleson.shop.order.claimapply.domain.OrderExchangeApply;
import saleson.shop.order.claimapply.domain.OrderReturnApply;
import saleson.shop.order.claimapply.domain.ReturnApplyInfo;
import saleson.shop.order.claimapply.support.ClaimException;
import saleson.shop.order.claimapply.support.ExchangeApply;
import saleson.shop.order.claimapply.support.ReturnApply;
import saleson.shop.order.domain.Buy;
import saleson.shop.order.domain.BuyItem;
import saleson.shop.order.domain.BuyPayment;
import saleson.shop.order.domain.BuyQuantity;
import saleson.shop.order.domain.Buyer;
import saleson.shop.order.domain.GiftOrderVo;
import saleson.shop.order.domain.ItemPrice;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderAgencyOrderLog;
import saleson.shop.order.domain.OrderCount;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.domain.OrderList;
import saleson.shop.order.domain.OrderLog;
import saleson.shop.order.domain.OrderPayment;
import saleson.shop.order.domain.OrderPgData;
import saleson.shop.order.domain.OrderPrice;
import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.order.domain.OrderSupporter;
import saleson.shop.order.domain.Receiver;
import saleson.shop.order.domain.Shipping;
import saleson.shop.order.domain.ShippingCoupon;
import saleson.shop.order.domain.WaitingDepositDetail;
import saleson.shop.order.giftitem.OrderGiftItemService;
import saleson.shop.order.givepoint.OrderGivePointService;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.order.infra.BuyerEncryptor;
import saleson.shop.order.infra.OrderEncryptor;
import saleson.shop.order.infra.OrderExchangeApplyEncryptor;
import saleson.shop.order.infra.OrderListEncryptor;
import saleson.shop.order.infra.OrderParamEncryptor;
import saleson.shop.order.infra.OrderPaymentEncryptor;
import saleson.shop.order.infra.OrderReturnApplyEncryptor;
import saleson.shop.order.infra.OrderShippingInfoEncryptor;
import saleson.shop.order.payment.OrderPaymentMapper;
import saleson.shop.order.pg.PgService;
import saleson.shop.order.pg.cj.domain.CjResult;
import saleson.shop.order.pg.config.ConfigPgService;
import saleson.shop.order.pg.domain.PgData;
import saleson.shop.order.pg.easypay.domain.EasypayRequest;
import saleson.shop.order.pg.kcp.domain.KcpRequest;
import saleson.shop.order.pg.payco.domain.PayApprovalResult;
import saleson.shop.order.pg.payco.domain.PaymentDetail;
import saleson.shop.order.pg.payco.domain.ReservationResponse;
import saleson.shop.order.shipping.OrderShippingMapper;
import saleson.shop.order.shipping.support.ShippingParam;
import saleson.shop.order.shipping.support.ShippingReadyParam;
import saleson.shop.order.support.ChangePayment;
import saleson.shop.order.support.EditPayment;
import saleson.shop.order.support.GivePayOrder;
import saleson.shop.order.support.NewPayment;
import saleson.shop.order.support.OrderException;
import saleson.shop.order.support.OrderManagerException;
import saleson.shop.order.support.OrderParam;
import saleson.shop.order.support.StockDeduction;
import saleson.shop.order.support.StockRestoration;
import saleson.shop.orderagency.OrderAgencyService;
import saleson.shop.orderagency.domain.OrderAgencyManagerInfo;
import saleson.shop.point.PointService;
import saleson.shop.point.domain.AvailablePoint;
import saleson.shop.point.domain.Point;
import saleson.shop.point.domain.PointUsed;
import saleson.shop.point.support.OrderPointParam;
import saleson.shop.receipt.ReceiptService;
import saleson.shop.receipt.support.CashbillIssueRepository;
import saleson.shop.receipt.support.CashbillParam;
import saleson.shop.receipt.support.CashbillRepository;
import saleson.shop.receipt.support.CashbillResponse;
import saleson.shop.remittance.RemittanceMapper;
import saleson.shop.shipmentreturn.ShipmentReturnMapper;
import saleson.shop.shipmentreturn.domain.ShipmentReturn;
import saleson.shop.shipmentreturn.support.ShipmentReturnParam;
import saleson.shop.slave.SlaveOrderMapper;
import saleson.shop.user.LocgovService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.SellerUser;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserDetailEncryptor;
import saleson.shop.userdelivery.UserDeliveryService;
import saleson.shop.userdelivery.domain.UserDelivery;
import saleson.shop.userlevel.UserLevelMapper;

@Slf4j
@RequiredArgsConstructor
@Service("orderService")
public class OrderServiceImpl extends EgovAbstractServiceImpl implements OrderService {

	public static final String ERROR_ORDER_CODE = "ERROR: ORDER_CODE({})";
	public static final String ERROR_MARKER = "ERROR: {}";
	public static final int CASHBILL_AUTO_ISSUED_PRICE = 100000;
	public static final String ROLE_ISMS = "ROLE_ISMS";

	private final OrderMapper orderMapper;
	private final SlaveOrderMapper slaveOrderMapper;
	private final OrderPaymentMapper orderPaymentMapper;
	private final OrderShippingMapper orderShippingMapper;
	private final AccountNumberService accountNumberService;
	private final ConfigService configService;
	private final PointService pointService;
	private final UserDeliveryService userDeliveryService;
	private final CouponService couponService;
	private final SequenceService sequenceService;
	private final CategoriesMapper categoriesMapper;
	private final CartMapper cartMapper;
	private final ItemMapper itemMapper;
	private final DeliveryCompanyService deliveryCompanyService;
	private final ShipmentReturnMapper shipmentReturnMapper;
	private final DeliveryCompanyMapper deliveryCompanyMapper;
	private final RemittanceMapper remittanceMapper;
	private final UserLevelMapper userLevelMapper;
	private final UserService userService;
	private final OrderClaimApplyMapper orderClaimApplyMapper;
	private final SellerMapper sellerMapper;
	private final ItemService itemService;
	private final OrderLogRepository orderLogRepository;
	private final ConfigLogService configLogService;
    private final ReceiptService receiptService;
    private final CashbillRepository cashbillRepository;
    private final CashbillIssueRepository cashbillIssueRepository;
	private final GiftItemService giftItemService;
	private final OrderGiftItemService orderGiftItemService;
	private final Environment environment;
    private final ConfigPgService configPgService;
    private final NaverPaymentApi naverPaymentApi;
	private final OrderEncryptor orderEncryptor;
	private final BuyerEncryptor buyerEncryptor;
	private final OrderShippingInfoEncryptor orderShippingInfoEncryptor;
	private final OrderPaymentEncryptor orderPaymentEncryptor;
	private final OrderReturnApplyEncryptor orderReturnApplyEncryptor;
	private final OrderExchangeApplyEncryptor orderExchangeApplyEncryptor;
	private final OrderListEncryptor orderListEncryptor;
	private final OrderParamEncryptor orderParamEncryptor;
	private final OrderMessageService orderMessageService;
	private final OrderSupporter orderSupporter;

	private final OrderAgencyService orderAgencyService;

	@Autowired
	private SmsIpsService smsIpsService;

	@Autowired
	private OrderGivePointService orderGivePointService;

	@Autowired
	private LocgovService locgovService;	// 지자체 관리

	// @Qualifier("inicisService")
	private final PgService inicisService;
	// @Qualifier("lgDacomService")
	private final PgService lgDacomService;
	// @Qualifier("cjService")
	private final PgService cjService;
	// @Qualifier("paycoService")
	private final PgService paycoService;
	// @Qualifier("kakaopayService")
	private final PgService kakaopayService;
	// @Qualifier("kspayService")
	private final PgService kspayService;
	// @Qualifier("kcpService")
	private final PgService kcpService;
	// @Qualifier("easypayService")
	private final PgService easypayService;
	// @Qualifier("nicepayService")
	private final PgService nicepayService;

	@Autowired
	private UserDetailEncryptor userDetailEncryptor;

	@Autowired
	private SellerUserService sellerUserService;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private AlimtalkService alimtalkService;

	@Autowired
	private Nuri2Service nuri2Service;

	@Override
	public OrderShippingInfo getOrderShippingInfoByParam(OrderParam orderParam) {
		OrderShippingInfo orderShippingInfo = orderMapper.getOrderShippingInfoByParam(orderParam);

		if (orderShippingInfo != null) {
			orderShippingInfo.decrypt(orderShippingInfoEncryptor, ShopUtils.needMasking());
		}
		return orderShippingInfo;
	}

	@Override
	public OrderItem getOrderItemByParam(OrderParam orderParam) {
		return orderMapper.getOrderItemByParam(orderParam);
	}

	@Override
	public List<OrderItem> getOrderItemListByParam(OrderParam orderParam) {
		return orderMapper.getOrderItemListByParam(orderParam);
	}

	@Override
	public String getIslandTypeByZipcode(String zipcode) {

		if (ObjectUtils.isEmpty(zipcode)) {
			return "";
		}

		return orderMapper.getIslandTypeByZipcode(zipcode);
	}

	/**
	 * 취소 신청시 환불 계좌 정보를 입력받아야 하는가?
	 * @param payments
	 * @return
	 */
	private boolean getIsWriteBankInfo(List<OrderPayment> payments) {
		boolean isWriteBankInfo = false;
		for(OrderPayment orderPayment : payments) {
			if ("bank".equals(orderPayment.getApprovalType()) || "vbank".equals(orderPayment.getApprovalType())) {
				if (orderPayment.getRemainingAmount() > 0) {
					isWriteBankInfo = true;
					break;
				}
			} else {
				// 부분취소가 불가능한 주문이 있는경우 환불 계좌를 입력받아야 함.
				//if ("N".equals(orderPayment.getPartCancelFlag())) {
				//	isWriteBankInfo = true;
				//	break;
				//}
			}
		}

		return isWriteBankInfo;
	}

	/**
	 * 취소 신청시 환불 계좌 은행 리스트 KEY
	 * @param payments
	 * @return
	 */
	private String getBankListKey(List<OrderPayment> payments) {
		String bankListKey = "DEFAULT_BANK_LIST";

		for(OrderPayment orderPayment : payments) {
			if ("vbank".equals(orderPayment.getApprovalType())) {
				OrderPgData orderPgData = orderPayment.getOrderPgData();
				if (orderPgData != null) {
					if ("cj".equals(orderPgData.getPgServiceType())) {
						bankListKey = "CJ_BANK_LIST";
						break;
					}
				}
			}
		}

		return bankListKey;
	}

	@Override
	public List<OrderPayment> getWaitingDepositListByParam(OrderParam orderParam) {

		orderParam.encrypt(orderParamEncryptor);

		int totalCount = orderPaymentMapper.getWaitingDepositCountByParam(orderParam);
		String conditionType = orderParam.getConditionType();
		if (orderParam.getItemsPerPage() == 10) {
			orderParam.setItemsPerPage(50);
		}

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		orderParam.setPagination(pagination);
		orderParam.setLanguage(CommonUtils.getLanguage());

		List<OrderPayment> list = orderPaymentMapper.getWaitingDepositListByParam(orderParam);
		if (list != null) {
			for (OrderPayment payment : list) {
				isPaymentVerificationCancel(payment);

				payment.decrypt(orderPaymentEncryptor, ShopUtils.needMasking());

				// 엑셀다운로드가 아닌 경우 무조건 마스킹
				/*if (!orderParam.isDownloadExcel()) {
					waitingDepositMaskingDataSet(payment);
				} else if (!(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS"))) {
					waitingDepositMaskingDataSet(payment);
				}*/
			}
		}

		orderParam.decrypt(orderParamEncryptor);

		return list;
	}

	private void waitingDepositMaskingDataSet(OrderPayment payment) {
		String loginId = payment.getLoginId();				// 아이디
		String userName = payment.getUserName();			// 회원명
		String buyerName = payment.getBuyerName();			// 주문자
		//String bankInName = payment.getBankInName();		// 입금자명의
		String bankVirtualNo = payment.getBankVirtualNo();	// 입금계좌

		if(loginId != null){
			payment.setLoginId(UserUtils.reMasking(loginId, "email"));
		}
		if(userName != null){
			payment.setUserName(UserUtils.reMasking(userName, "name"));
		}
		if(buyerName != null){
			payment.setBuyerName(UserUtils.reMasking(buyerName, "name"));
		}
		/*if(bankInName != null){
			payment.setBankInName(UserUtils.reMasking(bankInName, "name"));
		}*/
		if(bankVirtualNo != null){
			payment.setBankVirtualNo(UserUtils.reMasking(bankVirtualNo, "account"));
		}
	}

	/**
	 * 입금 확인취소 가능여부 체크
	 * @param payment
	 */
	private void isPaymentVerificationCancel(OrderPayment payment) {
		OrderParam param = new OrderParam();
		param.setOrderCode(payment.getOrderCode());
		param.setOrderSequence(payment.getOrderSequence());

		param.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			param.setSellerId(SellerUtils.getSellerId());
			param.setConditionType("SELLER");
		}

		Order order = getByParam(param);

		int newOrderCount = 0;
		int itemCount = 0;
		for(OrderShippingInfo info : order.getOrderShippingInfos()) {
			for(OrderItem orderItem : info.getOrderItems()) {

				if (ShopUtils.checkOrderStatusChange("payment-verification-cancel", orderItem.getOrderStatus())) {
					newOrderCount++;
				}

				itemCount++;
			}
		}

		boolean isPaymentVerificationCancel = (newOrderCount == itemCount) ? true : false;
		payment.setPaymentVerificationCancel(isPaymentVerificationCancel);
	}

	private Order getByParam(OrderParam param) {
		return orderSupporter.getByParam(param);
	}

	@Override
	public void waitingDepositListUpdate(String mode, OrderParam orderParam) {

		if (orderParam.getId() == null) {
			throw new OrderManagerException();
		}

		if ("cancel".equals(mode)) {
			// 입금 확인전 주문 취소
			for(String id : orderParam.getId()) {

			}
		} else if ("confirm".equals(mode)) {

			// 입금 확인
			for(String id : orderParam.getId()) {

				String[] temp = StringUtils.delimitedListToStringArray(id, OrderPayment.PAYMENT_KEY_DIVISION_STRING);

				if (temp.length != 3) {
					continue;
				}

				orderParam.setOrderCode(temp[0]);
				orderParam.setOrderSequence(Integer.parseInt(temp[1]));
				orderParam.setPaymentSequence(Integer.parseInt(temp[2]));

				OrderPayment orderPayment = orderMapper.getOrderPaymentByParam(orderParam);
				if (orderPayment == null) {
					continue;
				}

				orderPayment.decrypt(orderPaymentEncryptor, false);


				orderParam.setAdminUserName(UserUtils.getUser().getUserName());
				orderParam.setPayAmount(orderPayment.getAmount());

				// 주문 로그 추가로 인해 임시 조회
                List<OrderItem> logOrderItems
                        = this.getOrderItemListForOrderLog(orderParam.getOrderCode(), orderParam.getOrderSequence());

				if (orderPaymentMapper.updateConfirmationOfPaymentStep1(orderParam) > 0){
					if (orderPaymentMapper.updateConfirmationOfPaymentStep2(orderParam) > 0) {
						orderPaymentMapper.updateConfirmationOfPaymentStep3(orderParam);



                        // 주문 로그
                        try {
                            for (OrderItem orderItem : logOrderItems) {

                                this.insertOrderLog(OrderLogType.WAITING_DEPOSIT,
                                        orderParam.getOrderCode(),
                                        orderParam.getOrderSequence(),
                                        orderItem.getItemSequence(),
                                        orderItem.getOrderStatus());
                            }
                        } catch (RuntimeException e) {
//                            log.error(ERROR_MARKER, e.getMessage(), e);
                            log.error(ERROR_MARKER, getClass().getName() + " :: waitingDepositListUpdate RuntimeException ==========", e);
                        }


					} else {
						throw new OrderManagerException();
					}
				} else {
					throw new OrderManagerException();
				}

				ConfigPg configPg = configPgService.getConfigPg();
				String autoCashReceipt = "";

				if (configPg != null) {
					autoCashReceipt = configPg.isUseAutoCashReceipt() ? "Y" : "N";
				} else {
					autoCashReceipt = environment.getProperty("pg.autoCashReceipt");
				}

				if(!"Y".equals(autoCashReceipt)) {
					// 현금영수증 발행
					CashbillParam cashbillParam = new CashbillParam();

					cashbillParam.setWhere("orderCode");
					cashbillParam.setQuery(orderParam.getOrderCode());

					Iterable<CashbillIssue> cashbillIssues = cashbillIssueRepository.findAll(cashbillParam.getPredicate());

					log.debug("[CASHBILL] START ---------------------------------------------");
					log.debug("[CASHBILL] cashbillIssues Size :  {}", ((List<CashbillIssue>) cashbillIssues).size());

					CashbillResponse response = null;

					for (CashbillIssue cashbillIssue : cashbillIssues) {
						response = receiptService.receiptIssue(cashbillIssue);

						if (response == null) {
							log.debug("[CASHBILL] ERROR >> PG 통신오류(응답없음)");
							throw new OrderException("PG 통신오류(응답없음)");
						}

						log.debug("[CASHBILL] cashbillIssue :  {}", cashbillIssue);
						log.debug("[CASHBILL] CashbillResponse response.isSuccess() :  {}", response.isSuccess());
						if (response.isSuccess()) {
							cashbillIssue.setIssuedDate(DateUtils.getToday(DATETIME_FORMAT));
							cashbillIssue.setUpdatedDate(DateUtils.getToday(DATETIME_FORMAT));
							cashbillIssue.setCashbillStatus(CashbillStatus.ISSUED);
							cashbillIssue.setMgtKey(response.getMgtKey());

							if (UserUtils.isUserLogin() || UserUtils.isManagerLogin()) {
								cashbillIssue.setUpdateBy(UserUtils.getUser().getUserName() + "(" + UserUtils.getLoginId() + ")");
							} else {
								cashbillIssue.setUpdateBy("비회원");
							}

							cashbillIssueRepository.save(cashbillIssue);

						} else {
							log.debug("[CASHBILL] ERROR >> {} : {}", response.getResponseCode(), response.getResponseMessage());
							throw new OrderException("현금영수증 발행 오류 - " +response.getResponseCode() + " : " + response.getResponseMessage());
						}
					}
					log.debug("[CASHBILL] END ---------------------------------------------");
					try {

						OrderParam orderSearchParam = new OrderParam();
						orderSearchParam.setOrderCode(orderPayment.getOrderCode());
						orderSearchParam.setConditionType("OPMANAGER");
						Order order = this.getOrderByParam(orderSearchParam);

						orderMessageService.sendOrderMessageTx(order, "order_cready_payment", ShopUtils.getConfig());

					} catch(RuntimeException e) {
//						log.error(ERROR_MARKER, e.getMessage(), e);
                        log.error(ERROR_MARKER, getClass().getName() + " :: waitingDepositListUpdate RuntimeException2 ==========", e);
					}
				}
            }
        } else {
            throw new OrderManagerException();
        }

	}



	@Override
	public void paymentVerificationCancel(OrderParam orderParam) {

		List<OrderPayment> list = getOrderPaymentListByParam(orderParam);

        if (list == null || list.isEmpty()) {
            throw new OrderManagerException("입금확인 취소 처리중 오류가 발생했습니다. ( " + orderParam.getOrderCode() + "의 결제정보를 찾을 수 없습니다. )");
        }

        for (OrderPayment orderPayment : list) {
            if ("bank".equals(orderPayment.getApprovalType())) {
                isPaymentVerificationCancel(orderPayment);
                if (orderPayment.isPaymentVerificationCancel() == false) {
                    throw new OrderManagerException("Error. 입금 확인취소가 적용되지 않았습니다.\n해당 요청건의 상품 처리상태가 신규주문 상태가 아닌것이 있습니다.");
                }
            }
        }

		orderParam.setAdminUserName(UserUtils.getUser().getUserName());

		if ( orderMapper.updateConfirmationOfPaymentCancelStep1(orderParam) == 0) {
			throw new OrderManagerException("Error. 입금 확인취소가 적용되지 않았습니다.\n해당 요청건의 상품 처리상태가 신규주문 상태가 아닌것이 있습니다.");
		} else {
			if ( orderMapper.updateConfirmationOfPaymentCancelStep2(orderParam) == 0) {
				throw new OrderManagerException("Error. 입금 확인취소가 적용되지 않았습니다.\n해당 요청건의 상품 처리상태가 신규주문 상태가 아닌것이 있습니다.");
			} else{
			    //2017.05.16 Son Jun-Eu - OP_ORDER_ITEM 테이블의 관련 내용 UPDATE
				if ( orderMapper.updateConfirmationOfPaymentCancelStep3(orderParam) == 0) {
					throw new OrderManagerException("Error. 입금 확인취소가 적용되지 않았습니다.\n해당 요청건의 상품 처리상태가 신규주문 상태가 아닌것이 있습니다.");
				}
			}
		}

		ConfigPg configPg = configPgService.getConfigPg();
		String autoCashReceipt = "";

		if (configPg != null) {
			autoCashReceipt = configPg.isUseAutoCashReceipt() ? "Y" : "N";
		} else {
			autoCashReceipt = environment.getProperty("pg.autoCashReceipt");
		}

		if(!"Y".equals(autoCashReceipt)) {
			List<Cashbill> cashbills = cashbillRepository.findAllByOrderCode(orderParam.getOrderCode());

			if (!cashbills.isEmpty()) {
				// 현금영수증 발행취소
				CashbillResponse cancelResponse = receiptService.cancelCashbill(orderParam.getOrderCode());

				if (!cancelResponse.isSuccess()) {
					throw new OrderManagerException("발급된 현금영수증 취소요청중 오류가 발생하였습니다. ( " + cancelResponse.getResponseMessage() + " )");
				}
			}
		}
	}

	@Override
	public List<WaitingDepositDetail> getWaitingDepositDetailByParam(OrderParam orderParam) {

		//return orderMapper.getWaitingDepositDetailByParam(orderParam);
		return null;
	}

	@Override
	public List<OrderList> getNewOrderListByParam(OrderParam orderParam) {

		// 검색일자를 주문일로 디폴트
		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CREATED_DATE");
		}

		if (UserUtils.isManagerLogin()/* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		orderParam.encrypt(orderParamEncryptor);
		int totalCount = orderShippingMapper.getNewOrderCountByParam(orderParam);

		//if (orderParam.getItemsPerPage() == 10) {
		//	orderParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		if (orderParam.isDownloadExcel()) {
			pagination.setItemsPerPage(Integer.MAX_VALUE);
			orderParam.setItemsPerPage(Integer.MAX_VALUE);
		}
		orderParam.setPagination(pagination);
		orderParam.setLanguage(CommonUtils.getLanguage());

		List<OrderList> list = orderShippingMapper.getNewOrderListByParam(orderParam);
		decryptOrderList(list);

		/*for (OrderList order : list) {
			if (!orderParam.isDownloadExcel()) {
				newOrderMaskingDataSet(order);
			} else if (!(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS"))) {
				newOrderMaskingDataSet(order);
			}
		}*/

		// 기타 주문 상품 정보 세팅 (사은품, 세트상품)
		setOrderItemOther(list);
		orderParam.decrypt(orderParamEncryptor);

		return list;
	}

	private void newOrderMaskingDataSet(OrderList order){
		String userName = order.getUserName();
		String mobile = order.getMobile();
		String receiveName = order.getReceiveName();
		String receiveMobile = order.getReceiveMobile();
		String receiveZipCode = order.getReceiveNewZipcode();
		String receiveAddress = order.getReceiveAddress();
		String receiveAddressDetail = order.getReceiveAddressDetail();

		if(userName != null){
			order.setUserName(UserUtils.reMasking(userName, "name"));
		}
		if(mobile != null){
			order.setMobile(UserUtils.reMasking(mobile, "tel"));
		}
		if(receiveName != null){
			order.setReceiveName(UserUtils.reMasking(receiveName, "name"));
		}
		if(receiveMobile != null){
			order.setReceiveMobile(UserUtils.reMasking(receiveMobile, "tel"));
		}
		if(receiveZipCode != null){
			order.setReceiveZipcode(UserUtils.reMasking(receiveZipCode, "zipCode"));
		}
		if(receiveAddress != null){
			order.setReceiveAddress(UserUtils.reMasking(receiveAddress, "addr"));
		}
		if(receiveAddressDetail != null){
			order.setReceiveAddressDetail(UserUtils.reMasking(receiveAddressDetail, "addrDetail"));
		}
	}

	@Override
	public void newOrderListUpdate(String mode, ShippingReadyParam shippingReadyParam, OrderItem orderItem, OrderParam orderParam) {

		// CJH 2016.10.20 수량이 0인 주문이 생성되지 않도록
		if (orderItem.getShippingReadyPossibleQuantity() <= 0) {
			return;
		}

		if ("shipping-direct".equals(mode)) {
			// 출고 가능 수량과 출고 지시 수량이 같으면 그냥 출고 수량 업데이트
//			if (orderItem.getShippingReadyPossibleQuantity() == shippingReadyParam.getQuantity()) {

				if (orderShippingMapper.updateShippingDirect(shippingReadyParam) == 0) {
					throw new OrderException();
				}
//			} else {		// 일부만 배송처리하는 케이스는 없어서 주석처리 : 2023.12.06.
//
//				// 출고 지시 수량만큼 정보 복사
//				orderItem.setQuantity(shippingReadyParam.getQuantity());
//				orderShippingMapper.copyOrderItemForShippingDirect(orderItem);
//
//				if (orderMapper.updateOrderItemQuantity(orderItem) == 0) {
//					throw new OrderException();
//				}
//
//			}
		} else if ("shipping-ready".equals(mode)) {

			// 출고 가능 수량과 출고 지시 수량이 같으면 그냥 출고 수량 업데이트
//			if (orderItem.getShippingReadyPossibleQuantity() == shippingReadyParam.getQuantity()) {
				if (orderShippingMapper.updateShippingReady(shippingReadyParam) == 0) {
					throw new OrderException();
				}
//			} else {		// 일부만 배송처리하는 케이스는 없어서 주석처리 : 2023.12.06.
//
//				// 출고 지시 수량만큼 정보 복사
//				orderItem.setQuantity(shippingReadyParam.getQuantity());
//				orderShippingMapper.copyOrderItemForShippingReady(orderItem);
//
//				if (orderMapper.updateOrderItemQuantity(orderItem) == 0) {
//					throw new OrderException();
//				}
//
//			}
		} else if ("deposit-check-cancel".equals(mode)) {
			//입금확인 취소
			paymentVerificationCancel(orderParam);
		} else {
			throw new OrderException();
		}

		// 주문 로그
		this.insertOrderLog(OrderLogType.ORDER_PAYMENT,
				orderItem.getOrderCode(),
				orderItem.getOrderSequence(),
				orderItem.getItemSequence(),
				orderItem.getOrderStatus());
	}

	@Override
	public List<OrderList> getNewOrderMobileListByParam(OrderParam orderParam) {

		// 검색일자를 주문일로 디폴트
		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CREATED_DATE");
		}

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		orderParam.encrypt(orderParamEncryptor);
		int totalCount = orderShippingMapper.getNewOrderMobileCountByParam(orderParam);

		//if (orderParam.getItemsPerPage() == 10) {
		//	orderParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		orderParam.setPagination(pagination);
		orderParam.setLanguage(CommonUtils.getLanguage());

		List<OrderList> list = orderShippingMapper.getNewOrderMobileListByParam(orderParam);
		decryptOrderList(list);

		/*for (OrderList order : list) {
			if (!orderParam.isDownloadExcel()) {
				newOrderMaskingDataSet(order);
			} else if (!(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS"))) {
				newOrderMaskingDataSet(order);
			}
		}*/

		// 기타 주문 상품 정보 세팅 (사은품, 세트상품)
		setOrderItemOther(list);
		orderParam.decrypt(orderParamEncryptor);

		return list;
	}

	@Override
	public List<OrderList> getShippingReadyListByParam(OrderParam orderParam) {

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		orderParam.encrypt(orderParamEncryptor);

		// 주문일자 검색을 배송준비중 상태 변경으로 디폴트
		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OS.SHIPPING_READY_DATE");
		}

		int totalCount = orderShippingMapper.getShippingReadyCountByParam(orderParam);

		//if (orderParam.getItemsPerPage() == 10) {
		//	orderParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		if (orderParam.isDownloadExcel()) {
			pagination.setItemsPerPage(Integer.MAX_VALUE);
			orderParam.setItemsPerPage(Integer.MAX_VALUE);
		}
		orderParam.setPagination(pagination);
		orderParam.setLanguage(CommonUtils.getLanguage());

		List<OrderList> list = orderShippingMapper.getShippingReadyListByParam(orderParam);
		decryptOrderListNoneMasking(list);

		/*for (OrderList order : list) {
			if (!orderParam.isDownloadExcel()) {
				shippingReadyMaskingDataSet(order);
			} else if (!(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS"))) {
				shippingReadyMaskingDataSet(order);
			}
		}*/

		// 기타 주문 상품 정보 세팅 (사은품, 세트상품)
		setOrderItemOther(list);
		orderParam.decrypt(orderParamEncryptor);

		return list;
	}

	private void shippingReadyMaskingDataSet(OrderList order){
		String buyerName = order.getUserName();	// 주문자
		String buyerPhone = order.getMobile();	// 주문자 핸드폰
		String receiveName = order.getReceiveName();	// 수령인
		String receiveMobile = order.getReceiveMobile();	// 수령인 전화번호
		String receiveZipCode = order.getReceiveZipcode();	// 우편번호
		String receiveNewZipCode = order.getReceiveNewZipcode(); // 신 우편번호
		String receiveAddress = order.getReceiveAddress();	// 주소
		String receiveAddressDetail = order.getReceiveAddressDetail();	//상세주소

		if (buyerName != null) {
			order.setUserName(UserUtils.reMasking(buyerName, "name"));
		}
		if (buyerPhone != null) {
			order.setMobile(UserUtils.reMasking(buyerPhone, "tel"));
		}
		if (receiveName != null) {
			order.setReceiveName(UserUtils.reMasking(receiveName, "name"));
		}
		if (receiveMobile != null) {
			order.setReceiveMobile(UserUtils.reMasking(receiveMobile, "tel"));
		}
		if (receiveZipCode != null) {
			order.setReceiveZipcode(UserUtils.reMasking(receiveZipCode, "zipCode"));
		}
		if (receiveNewZipCode != null) {
			order.setReceiveNewZipcode(UserUtils.reMasking(receiveNewZipCode, "newZipCode"));
		}
		if (receiveAddress != null) {
			order.setReceiveAddress(UserUtils.reMasking(receiveAddress, "addr"));
		}
		if (receiveAddressDetail != null) {
			order.setReceiveAddressDetail(UserUtils.reMasking(receiveAddressDetail, "addrDetail"));
		}

	}

	@Override
	public void shippingReadyListUpdate(String mode, ShippingParam shippingParam, OrderItem orderItem, OrderParam orderMessageParam) {

		if ("shipping-cancel".equals(mode)) {

			if (orderShippingMapper.updateShippingReadyCancel(shippingParam) == 0) {
				throw new OrderException();
			}

			if (!"N".equals(orderItem.getEscrowStatus())) {
				throw new OrderException();
			}

		} else if ("shipping-start".equals(mode) || "shipping-start-send-message".equals(mode)) {

			if (!(shippingParam.getDeliveryCompanyId() > 0
				&& StringUtils.isNotEmpty(shippingParam.getDeliveryNumber()))) {
				throw new OrderException();
			}

			DeliveryCompany deliveryCompany = deliveryCompanyService.getDeliveryCompanyById(shippingParam.getDeliveryCompanyId());

			if (deliveryCompany == null) {
				throw new OrderException();
			}

			shippingParam.setDeliveryCompanyName(deliveryCompany.getDeliveryCompanyName());
			shippingParam.setDeliveryCompanyUrl(deliveryCompany.getDeliveryCompanyUrl());

			if (orderShippingMapper.updateShippingStart(shippingParam) == 0) {
				throw new OrderException();
			}

            ConfigPg configPg = configPgService.getConfigPg();

            if (orderShippingMapper.getPreShippingCount(shippingParam) == 0) {
                OrderPgData orderPgData = this.getOrderPgDataByOrderCode(shippingParam.getOrderCode());
                if (orderPgData != null && "naverpay".equals(orderPgData.getPgServiceType())) {
                    MultiValueMap<String, Object> pointRequestMap = new LinkedMultiValueMap<String, Object>();
                    pointRequestMap.add("paymentId", orderPgData.getPgKey());

                    if(configPg == null || ObjectUtils.isEmpty(pointRequestMap)) {
                    	throw new OrderException();
                    }
                    naverPaymentApi.point(pointRequestMap, configPg);
                }
            }

			// 메시지 전송
//			if ("shipping-start-send-message".equals(mode)) {
//				try {
//
//					OrderParam orderSearchParam = new OrderParam();
//					orderSearchParam.setOrderCode(orderItem.getOrderCode());
//					orderSearchParam.setConditionType("OPMANAGER");
//					Order order = this.getOrderByParam(orderSearchParam);
//
//					String deliveryNumber = shippingParam.getDeliveryNumber();
//					if (order != null) {
//
//						String key = shippingParam.getKey().substring(0, shippingParam.getKey().lastIndexOf(OrderItem.ITEM_KEY_DIVISION_STRING));
//						List<String> orders = new ArrayList<>();
//
//						for (int i=0; i<orderMessageParam.getId().length; i++) {
//							if (orderMessageParam.getId()[i].startsWith(key)
//									&& deliveryNumber.equals(orderMessageParam.getShippings().get(i).getDeliveryNumber())) {
//								orders.add(orderMessageParam.getId()[i]);
//							}
//						}
//						order.setMessageTargetItemSequences(orders.toArray(new String[orders.size()]));
//						order.setMessageTargetDeliveryCompanyName(deliveryCompany.getDeliveryCompanyName());
//						order.setMessageTargetDeliveryNumber(deliveryNumber);
//
//						orderMessageService.sendOrderMessageTx(order, "order_delivering", ShopUtils.getConfig());
//					}
//
//				} catch(RuntimeException e) {
//					log.error(ERROR_ORDER_CODE, orderItem.getOrderCode(), e);
//				}
//			}
//
//
//			String pgType = "";
//			String useEscrow = "";
//
//			if (configPg != null) {
//				pgType = configPg.getPgType().getCode().toLowerCase();
//				useEscrow = configPg.isUseEscroow() ? "Y" : "N";
//			} else {
//				pgType = SalesonProperty.getPgService();
//				useEscrow = environment.getProperty("pg.useEscrow");
//			}
//
//            // 에스크로 사용시
//            if ("Y".equals(useEscrow) && "Y".equals(orderItem.getEscrowStatus())) {
//                //이니시스 에스크로 결제완료 상태 시 PG사에 배송등록 요청
//                if ("inicis".equals(pgType)) {
//
//                    // 배송등록 요청 정보 가져오기
//                    HashMap<String, Object> paramMap = new HashMap<>();
//
//                    OrderParam orderParam = new OrderParam();
//                    orderParam.setOrderCode(orderItem.getOrderCode());
//                    orderParam.setOrderSequence(orderItem.getOrderSequence());
//                    orderParam.setItemSequence(orderItem.getItemSequence());
//                    orderParam.setShippingSequence(orderItem.getShippingSequence());
//
//                    paramMap = orderMapper.getInicisDeliveryInfoByParam(orderParam);
//
//                    // 무통장 입금(bank) 시 오류로인해 회피 처리 (임시 2018.02.06-KYK)
//                    if (paramMap != null) {
//
//                        //배송준비중 목록에서 배송시작 누를경우 배송등록을 신규로 설정
//                        paramMap.put("dlv_report", "I");
//
//                        if(paramMap.get("PAY_SHIPPING").toString().equals("0"))
//                            paramMap.put("dlv_charge", "SH");
//                        else
//                            paramMap.put("dlv_charge", "BH");
//
//                        boolean isSuccess = inicisService.delivery(paramMap);
//
//                        // 2017.7.05 Son Jun-Eu - 에스크로 배송등록 확인 후 상태 갱신
//                        orderParam.setEscrowStatus("20");
//                        orderMapper.updateEscrowStatus(orderParam);
//
//                    }
//
//
//                }else if("kcp".equals(pgType)){
//                    // 배송등록 요청 정보 가져오기
//                    HashMap<String, Object> paramMap = new HashMap<>();
//
//                    OrderParam orderParam = new OrderParam();
//                    orderParam.setOrderCode(orderItem.getOrderCode());
//                    orderParam.setOrderSequence(orderItem.getOrderSequence());
//                    orderParam.setItemSequence(orderItem.getItemSequence());
//                    orderParam.setShippingSequence(orderItem.getShippingSequence());
//
//                    paramMap.put("tno",orderMapper.getTidByParam(shippingParam.getOrderCode()));
//                    paramMap.put("deli_numb",shippingParam.getDeliveryNumber());
//                    paramMap.put("deli_corp",shippingParam.getDeliveryCompanyName());
//
//                    boolean isSuccess = kcpService.delivery(paramMap);
//
//                    orderParam.setEscrowStatus("20");
//                    orderMapper.updateEscrowStatus(orderParam);
//                }
//            }


    		// 국민비서 알림 전송 (답례품 배송시)
    		try {
    			User user = userService.getUserByUserId(orderItem.getUserId());
    			UserDetail userDetail = (UserDetail) user.getUserDetail();
				userDetail.decrypt(userDetailEncryptor, false);
    			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber().replaceAll("-", "")) && StringUtils.hasLength(userDetail.getMberCi())) {
	    			List<ReceiverInfo> receiverInfos = new ArrayList<>();
	    			ReceiverInfo receiverInfo = new ReceiverInfo();
	    			receiverInfo.setSmsType(SmsType.PRESENT_SHIPPING);
	    			receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
	    			StringBuilder sb = new StringBuilder();
	    			sb.append(user.getUserName());	// 이름
	    			sb.append("|");
	    			LocalDateTime dt = LocalDateTime.parse(orderItem.getCreatedDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	    			receiverInfo.setMsgDateTime(dt);
	    			sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
	    			sb.append("|");
	    			sb.append(orderItem.getOrderCode());	// 주문번호
	    			sb.append("|");
	    			sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));		// 상품명
	    			if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
	    				sb.append("(");
	    				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
	    				sb.append(")");
	    			}
	    			sb.append("|");
	    			sb.append(shippingParam.getDeliveryCompanyName());	// 배송사명
					sb.append("|");
					sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
	    			receiverInfo.setSndngCntnts(sb.toString());
	    			receiverInfos.add(receiverInfo);
	    			smsIpsService.insertTifIpsSndngM(receiverInfos);
    			}
    		} catch (NullPointerException | ClassCastException e) {
    			log.error(getClass().getName() +  " :: shippingReadyListUpdate send sms error", e);
    		}


    		// 국민비서 알림 전송 (답례품 배송시) - 답례품 제공자 추가
    		try {
//    			User user = userService.getUserByUserId(orderItem.getUserId());
//    			UserDetail userDetail = (UserDetail) user.getUserDetail();
//				userDetail.decrypt(userDetailEncryptor, false);
//    			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber().replaceAll("-", "")) && StringUtils.hasLength(userDetail.getMberCi())) {
//	    			List<ReceiverInfo> receiverInfos = new ArrayList<>();
//	    			ReceiverInfo receiverInfo = new ReceiverInfo();
//	    			receiverInfo.setSmsType(SmsType.PRESENT_SHIPPING);
//	    			receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
//	    			StringBuilder sb = new StringBuilder();
//	    			sb.append(user.getUserName());	// 이름
//	    			sb.append("|");
//	    			LocalDateTime dt = LocalDateTime.parse(orderItem.getCreatedDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//	    			receiverInfo.setMsgDateTime(dt);
//	    			sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
//	    			sb.append("|");
//	    			sb.append(orderItem.getOrderCode());	// 주문번호
//	    			sb.append("|");
//	    			sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()));		// 상품명
//	    			if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
//	    				sb.append("(");
//	    				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
//	    				sb.append(")");
//	    			}
//	    			sb.append("|");
//	    			sb.append(shippingParam.getDeliveryCompanyName());	// 배송사명
//					sb.append("|");
//					sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
//	    			receiverInfo.setSndngCntnts(sb.toString());
//	    			receiverInfos.add(receiverInfo);
//	    			smsIpsService.insertTifIpsSndngM(receiverInfos);
//    			}
    		} catch (NullPointerException | ClassCastException e) {
    			log.error(getClass().getName() +  " :: shippingReadyListUpdate send sms error", e);
    		}

		} else if ("shipping-mobile-direct".equals(mode)) {			// 모바일 발송 완료 처리

			if (!StringUtils.isNotEmpty(shippingParam.getMobileNumber())) {
					throw new OrderException();
			}

			if (orderShippingMapper.updateShippingMobileDirect(shippingParam) == 0) {
				throw new OrderException();
			}

			// 국민비서 알림 전송 (답례품 배송시)
    		try {
    			User user = userService.getUserByUserId(orderItem.getUserId());
    			UserDetail userDetail = (UserDetail) user.getUserDetail();
				userDetail.decrypt(userDetailEncryptor, false);
    			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber().replaceAll("-", "")) && StringUtils.hasLength(userDetail.getMberCi())) {
	    			List<ReceiverInfo> receiverInfos = new ArrayList<>();
	    			ReceiverInfo receiverInfo = new ReceiverInfo();
	    			receiverInfo.setSmsType(SmsType.PRESENT_SHIPPING);
	    			receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
	    			StringBuilder sb = new StringBuilder();
	    			sb.append(user.getUserName());	// 이름
	    			sb.append("|");
	    			LocalDateTime dt = LocalDateTime.parse(orderItem.getCreatedDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	    			receiverInfo.setMsgDateTime(dt);
	    			sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
	    			sb.append("|");
	    			sb.append(orderItem.getOrderCode());	// 주문번호
	    			sb.append("|");
	    			sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));		// 상품명
	    			if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
	    				sb.append("(");
	    				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
	    				sb.append(")");
	    			}
	    			sb.append("|");
//	    			sb.append(shippingParam.getDeliveryCompanyName());	// 배송사명
	    			sb.append("모바일발송");	// 배송사명
					sb.append("|");
					sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
	    			receiverInfo.setSndngCntnts(sb.toString());
	    			receiverInfos.add(receiverInfo);
	    			smsIpsService.insertTifIpsSndngM(receiverInfos);
    			}
    		} catch (NullPointerException | ClassCastException e) {
    			log.error(getClass().getName() +  " :: shippingReadyListUpdate mobile send sms error", e);
    		}
		} else if ("mobile-temp-save".equals(mode)) {			// 모바일 발송정보 임시저장

			if (!StringUtils.isNotEmpty(shippingParam.getMobileNumber())) {
					throw new OrderException();
			}

			if (orderShippingMapper.updateTempShippingMobile(shippingParam) == 0) {
				throw new OrderException();
			}

		} else {
			throw new OrderException("올바르지 않은 접근입니다.");
		}

		// 주문 로그
		this.insertOrderLog(OrderLogType.ORDER_SHIPPING,
				orderItem.getOrderCode(),
				orderItem.getOrderSequence(),
				orderItem.getItemSequence(),
				orderItem.getOrderStatus());
	}

	//배송준비중 목록에서 엑셀업로드로 배송정보 일괄 업데이트[2017-09-05]minae.yun
	@Override
	public Map<String, Object> shippingReadyExcelUpload(OrderParam orderParam, MultipartFile multipartFile, boolean isMobile) {

		if (multipartFile == null) {
			throw new UserException(MessageUtils.getMessage("M01532")); // 파일을 선택해 주세요.
		}

		String fileName = multipartFile.getOriginalFilename();
		String fileExtension = FileUtils.getExtension(fileName);

		// 확장자 체크
		if (!(fileExtension.equalsIgnoreCase("xlsx"))) {
			throw new UserException(MessageUtils.getMessage("M01533"));	// 엑셀 파일(.xlsx)만 업로드가 가능합니다.
		}

		// 엑셀 셀 읽기 : http://poi.apache.org/spreadsheet/quick-guide.html#CellContents 자세한 건 여기서 확인 - skc
		XSSFWorkbook wb = null;

		String excelUploadReport = "";
		try {
			wb = new XSSFWorkbook(multipartFile.getInputStream());
			Map<String, Object> resp = new HashMap<>();
			if (wb.getNumberOfSheets() > 1) {
				resp = processShippingReadyExcelSheet(null, isMobile);
				excelUploadReport = "업로드 파일 : " + multipartFile.getOriginalFilename() + "\n";
				excelUploadReport += "업로드 파일의 sheet는 한개로 통합부탁드립니다.";
			} else {
				resp = processShippingReadyExcelSheet(wb.getSheetAt(0), isMobile);
				excelUploadReport = "업로드 파일 : " + multipartFile.getOriginalFilename() + "\n";
				excelUploadReport += (String) resp.get("result");
			}
			resp.replace("result", resp.get("result"), excelUploadReport);
			return resp;

		} catch (IOException e) {
//			String message = MessageUtils.getMessage("M01534") + "(" + e.getMessage()+ ")";
//			log.error("{}", message , e);
//			String message = MessageUtils.getMessage("M01534");
            log.error(ERROR_MARKER, getClass().getName() + " :: shippingReadyExcelUpload IOException ==========", e);
//			throw new UserException(message); // 엑셀 파일 로드 시 오류가 발생하였습니다.
            throw new UserException("엑셀 파일 로드 시 오류가 발생하였습니다.");
		} catch (Exception e) {
//			String message = MessageUtils.getMessage("M01534") + "(" + e.getMessage()+ ")";
//			log.error("{}", message, e);
//			String message = MessageUtils.getMessage("M01534");
            log.error(ERROR_MARKER, getClass().getName() + " :: shippingReadyExcelUpload Exception ==========", e);

//			throw new UserException(message); // 엑셀 파일 로드 시 오류가 발생하였습니다.
			throw new UserException("엑셀 파일 로드 시 오류가 발생하였습니다."); // 엑셀 파일 로드 시 오류가 발생하였습니다.
		}
	}

	//엑셀에서 배송정보(택배사, 운송장번호)받아서 업데이트[2017-09-05]minae.yun
	private Map<String, Object> processShippingReadyExcelSheet(XSSFSheet sheet, boolean isMobile) {

		Map<String, Object> resp = new HashMap<>();
		String result = "";
		if (sheet == null) {
			resp.put("result",  result);
			return resp;
		}

		StringBuffer executionLog = new StringBuffer();

		List<HashMap<String, String>> cellReferences = new ArrayList<>();

//		List<String> key = new ArrayList<>();

		int rowDataCount = 0;			// 데이터 수 (타이틀, 헤더 제외)
		int rowErrorCount = 0;			// 오류 수
		for (Row row : sheet) {
			int rowIndex = row.getRowNum() + 1;

			if(rowIndex == 2) {
				// 헤더 - 타이틀 가져오기
				for (Cell cell : row) {
					CellReference cellReference = new CellReference(cell);

					HashMap<String, String> cellInfo = new HashMap<>();
					cellInfo.put("title", ShopUtils.getString(cell).replace("(*)", ""));
					cellInfo.put("colString", cellReference.convertNumToColString(cell.getColumnIndex()));

					cellReferences.add(cellInfo);
				}
			}

			if (row.getRowNum() < 2) {
				continue;
			}

			// 해당 로우의 셀 값이 전부 비어있는 경우는 SKIP
			if (PoiUtils.isEmptyAllCell(row)) {
				continue;
			}

			//주문번호
			String orderCode = ShopUtils.getString(row.getCell(0));
			//아이템 주문 순번
			int itemSequence = Integer.parseInt(ShopUtils.getString(row.getCell(1)));
			//배송정보
			DeliveryCompany deliveryCompany;
			ShippingParam shippingParam = new ShippingParam();

			shippingParam.setOrderCode(orderCode);

			int cellErrorCount = 0;
			for (Cell cell : row) {
				HashMap<String, String> cellReference = null;
				try {
					cellReference = cellReferences.get(cell.getColumnIndex());
				} catch (RuntimeException e) {
//					log.error("cellReferences.get(cell.getColumnIndex()) : {}", e.getMessage(), e);
		            log.error("cellReferences.get(cell.getColumnIndex()) : {}", getClass().getName() + " :: processShippingReadyExcelSheet RuntimeException ==========", e);
				}
				if (cellReference == null) {
					continue;
				}
				cellReference.put("rowIndex", Integer.toString(rowIndex));

				if (isMobile) {
					// 임시로 직접수령 처리
					deliveryCompany = deliveryCompanyService.getDeliveryCompanyById(29);
					shippingParam.setDeliveryCompanyId(deliveryCompany.getDeliveryCompanyId());
					shippingParam.setDeliveryCompanyName(deliveryCompany.getDeliveryCompanyName());
					shippingParam.setDeliveryCompanyUrl(deliveryCompany.getDeliveryCompanyUrl());

					switch (cell.getColumnIndex()) {
						case 11:	// 모바일번호
							String deliveryNumber = ShopUtils.getString(cell);
							if (!ObjectUtils.isEmpty(deliveryNumber)) {
								shippingParam.setDeliveryNumber(deliveryNumber);
							} else {
//								executionLog.append(PoiUtils.log(cellReference, "모바일번호가 입력되지 않았습니다."));
								executionLog.append(orderCode + "(" + itemSequence + ") : 모바일번호가 입력되지 않았습니다.\n");
								cellErrorCount++;
								break;
							}
							break;

						default:
							break;
					}
				} else {
					switch (cell.getColumnIndex()) {

	//					case 11:	// 택배사
						case 13:	// 택배사
							int deliverCompanyId = ShopUtils.getInt(cell);
							deliveryCompany = deliveryCompanyService.getDeliveryCompanyById(deliverCompanyId);
							if (deliveryCompany != null) {
								shippingParam.setDeliveryCompanyId(deliverCompanyId);
								shippingParam.setDeliveryCompanyName(deliveryCompany.getDeliveryCompanyName());
								shippingParam.setDeliveryCompanyUrl(deliveryCompany.getDeliveryCompanyUrl());
							} else {
//								executionLog.append(PoiUtils.log(cellReference, "잘못된 택배사를 입력하셨습니다."));
								executionLog.append(orderCode + "(" + itemSequence + ") : 잘못된 택배사를 입력하셨습니다.\n");
								cellErrorCount++;
								break;
							}
							break;

	//					case 12:	// 운송장번호
						case 14:	// 운송장번호
							String deliveryNumber = ShopUtils.getString(cell);
							if (!ObjectUtils.isEmpty(deliveryNumber)) {
								shippingParam.setDeliveryNumber(deliveryNumber);
							} else {
//								executionLog.append(PoiUtils.log(cellReference, "운송장번호가 입력되지 않았습니다."));
								executionLog.append(orderCode + "(" + itemSequence + ") : 운송장번호가 입력되지 않았습니다.\n");
								cellErrorCount++;
								break;
							}
							break;

						default:
							break;
					}
				}

			} // cell

			// 해당 행의 각각의 Cell의 오류를 체크하여 오류가 있는 경우 데이터를 처리하지 않음.
			if (cellErrorCount > 0) {
				rowErrorCount++;
				continue;
			}

			if (shippingParam.getDeliveryCompanyId() > 0 && !ObjectUtils.isEmpty(shippingParam.getDeliveryNumber())) {

				shippingParam.setAdminUserName(UserUtils.getManagerName());
				shippingParam.setConditionType("OPMANAGER");
				if (ShopUtils.isSellerPage()) {
					shippingParam.setSellerId(SellerUtils.getSellerId());
					shippingParam.setConditionType("SELLER");
				}

				shippingParam.setOrderSequence(0);
				shippingParam.setItemSequence(itemSequence);

				if (isMobile) {
					shippingParam.setMode("MOBILE");
					shippingParam.setMobileNumber(shippingParam.getDeliveryNumber());
				} else {
					shippingParam.setMode("SHIPPING");
				}

				//배송중으로 업데이트
				if (orderShippingMapper.updateShippingStart(shippingParam) == 0) {
//					throw new OrderException();
					executionLog.append(orderCode + "(" + itemSequence + ") : 처리 가능한 상태가 아닙니다.\n");
					rowErrorCount++;
					continue;
				}

				// 메세지 발송용 키 세팅
//				String devision = OrderItem.ITEM_KEY_DIVISION_STRING;
//				key.add(new StringBuilder(orderCode).append(devision)
//						.append(shippingParam.getDeliveryNumber()).append(devision)
//						.append(itemSequence).append(devision)
//						.append(shippingParam.getDeliveryCompanyName()).toString());

				OrderParam orderParam = new OrderParam();
				orderParam.setOrderCode(orderCode);
				orderParam.setOrderSequence(0);
				orderParam.setItemSequence(itemSequence);

				orderParam.setConditionType("OPMANAGER");
				if (ShopUtils.isSellerPage()) {
					orderParam.setSellerId(SellerUtils.getSellerId());
					orderParam.setConditionType("SELLER");
				}

				OrderItem orderItem = getOrderItemByParam(orderParam);



	    		// 국민비서 알림 전송 (답례품 배송시)
	    		try {
	    			User user = userService.getUserByUserId(orderItem.getUserId());
	    			UserDetail userDetail = (UserDetail) user.getUserDetail();
					userDetail.decrypt(userDetailEncryptor, false);
	    			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber().replaceAll("-", "")) && StringUtils.hasLength(userDetail.getMberCi())) {
		    			List<ReceiverInfo> receiverInfos = new ArrayList<>();
		    			ReceiverInfo receiverInfo = new ReceiverInfo();
		    			receiverInfo.setSmsType(SmsType.PRESENT_SHIPPING);
		    			receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
		    			StringBuilder sb = new StringBuilder();
		    			sb.append(user.getUserName());	// 이름
		    			sb.append("|");
		    			LocalDateTime dt = LocalDateTime.parse(orderItem.getCreatedDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		    			receiverInfo.setMsgDateTime(dt);
		    			sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
		    			sb.append("|");
		    			sb.append(orderItem.getOrderCode());	// 주문번호
		    			sb.append("|");
		    			sb.append(StringEscapeUtils.unescapeHtml(orderItem.getItemName()).replaceAll("\\|", "-"));		// 상품명
		    			if (StringUtils.hasLength(orderItem.getOptions()) && !orderItem.getOptions().contains("|")) {
		    				sb.append("(");
		    				sb.append(StringEscapeUtils.unescapeHtml(orderItem.getOptions()));	// 옵션명
		    				sb.append(")");
		    			}
		    			sb.append("|");
		    			if (isMobile) {
		    				sb.append("모바일발송");
		    			} else {
			    			sb.append(shippingParam.getDeliveryCompanyName());	// 배송사명
		    			}
						sb.append("|");
						sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
		    			receiverInfo.setSndngCntnts(sb.toString());
		    			receiverInfos.add(receiverInfo);
		    			smsIpsService.insertTifIpsSndngM(receiverInfos);
	    			}
	    		} catch (NullPointerException | ClassCastException e) {
	    			log.error(getClass().getName() +  " :: shippingReadyListUpdate send sms error", e);
	    		}

	    		// 주문 로그
	    		this.insertOrderLog(OrderLogType.ORDER_SHIPPING,
	    				orderItem.getOrderCode(),
	    				orderItem.getOrderSequence(),
	    				orderItem.getItemSequence(),
	    				"20");
			}

			rowDataCount++;

		} // row

		// 처리결과
//		result = "\n<p class=\"sheet\"><span>[배송정보 등록]</span> Total:" + rowDataCount
//			+ ", Process:" + (rowDataCount - rowErrorCount)
//			+ ", Error:" + rowErrorCount + "</p>\n";
//
//		if (rowErrorCount > 0) {
//			result += "<p class=\"line\">----------------------------------------------------------------------------</p>\n";
//			result += executionLog.toString();
//			result += "\n";
//		}

		result = "\n[배송정보 등록]\n전체:" + (rowDataCount + rowErrorCount)
			+ ", 처리건수:" + rowDataCount
			+ ", 오류건수:" + rowErrorCount + "\n";

		if (rowErrorCount > 0) {
			result += executionLog.toString();
			result += "\n";
		}


		resp.put("result", result);
//		resp.put("key", key);

		return resp;
	}

	@Override
	public void sendOrderDeliveryMessageByExcelUpload(List<String> keyList) {

		/*
			key ==> orderCode///itemSequence///deliveryNumber///deliveryCompanyName
			ex) K00000001///0///123456789///CJ대한통운
		*/
		for (String key : keyList) {

			String devision = OrderItem.ITEM_KEY_DIVISION_STRING;
			String[] keyArray = key.split(devision);
			String orderCode = keyArray[0];
			String deliveryNumber = keyArray[1];
			String deliveryCompanyName = keyArray[3];

			if (StringUtils.isNotEmpty(orderCode)) {
				OrderParam orderSearchParam = new OrderParam();
				orderSearchParam.setOrderCode(orderCode);
				orderSearchParam.setConditionType("OPMANAGER");
				Order order = this.getOrderByParam(orderSearchParam);

				if (order != null) {

					// 상품코드, 운송장번호까지만 얻어오기
					int divisionIdx = org.apache.commons.lang.StringUtils.ordinalIndexOf(key, devision, 2);
					String code = key.substring(0, divisionIdx) + devision;

					List<String> orders = new ArrayList<>();

					// 엑셀파일 읽을 때 생성한 keyList로 함께 배송하는 상품의 상품 순번 얻어오기
					for (int i=0; i<keyList.size(); i++) {
						if (keyList.get(i).startsWith(code)) {
							orders.add(keyList.get(i).split(devision)[2]);
						}
					}

					order.setMessageTargetItemSequences(orders.toArray(new String[orders.size()]));
					order.setMessageTargetDeliveryCompanyName(deliveryCompanyName);
					order.setMessageTargetDeliveryNumber(deliveryNumber);

					// 메일, 메시지 발송
					orderMessageService.sendOrderMessageTx(order, "order_delivering", ShopUtils.getConfig());

				}

			}

		}

	}

	@Override
	public List<OrderList> getShippingListByParam(OrderParam orderParam) {

		// 주문일자 검색을 배송시작일자로 디폴트
		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.SHIPPING_DATE");
		}

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		orderParam.encrypt(orderParamEncryptor);

		int totalCount = orderShippingMapper.getShippingCountByParam(orderParam);

		//if (orderParam.getItemsPerPage() == 10) {
		//	orderParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		if (orderParam.isDownloadExcel()) {
			pagination.setItemsPerPage(Integer.MAX_VALUE);
			orderParam.setItemsPerPage(Integer.MAX_VALUE);
		}
		orderParam.setPagination(pagination);
		orderParam.setLanguage(CommonUtils.getLanguage());

		List<OrderList> list = orderShippingMapper.getShippingListByParam(orderParam);
		decryptOrderList(list);

		/*if (list != null) {
			for (OrderList order : list) {
				if (!orderParam.isDownloadExcel()) {
					shippingMaskingDataSet(order);
				} else if (!(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS"))) {
					shippingMaskingDataSet(order);
				}
			}
		}*/

		// 기타 주문 상품 정보 세팅 (사은품, 세트상품)
		setOrderItemOther(list);
		orderParam.decrypt(orderParamEncryptor);

		return list;
	}

	private void shippingMaskingDataSet(OrderList order){
		String buyerName = order.getUserName();
		String buyerPhone = order.getMobile();
		String receiveName = order.getReceiveName();
		String receiveMobile = order.getReceiveMobile();
		String receiveZipCode = order.getReceiveZipcode();
		String receiveNewZipCode = order.getReceiveNewZipcode();
		String receiveAddress = order.getReceiveAddress();
		String receiveAddressDetail = order.getReceiveAddressDetail();

		if (buyerName != null) {
			order.setUserName(UserUtils.reMasking(buyerName, "name"));
		}
		if (buyerPhone != null) {
			order.setMobile(UserUtils.reMasking(buyerPhone, "tel"));
		}
		if (receiveName != null) {
			order.setReceiveName(UserUtils.reMasking(receiveName, "name"));
		}
		if (receiveMobile != null) {
			order.setReceiveMobile(UserUtils.reMasking(receiveMobile, "tel"));
		}
		if (receiveZipCode != null) {
			order.setReceiveZipcode(UserUtils.reMasking(receiveZipCode, "zipCode"));
		}
		if (receiveNewZipCode != null) {
			order.setReceiveNewZipcode(UserUtils.reMasking(receiveNewZipCode, "newZipCode"));
		}
		if (receiveAddress != null) {
			order.setReceiveAddress(UserUtils.reMasking(receiveAddress, "addr"));
		}
		if (receiveAddressDetail != null) {
			order.setReceiveAddressDetail(UserUtils.reMasking(receiveAddressDetail, "addrDetail"));
		}
	}

	@Override
	public void shippingListUpdate(String mode, OrderParam orderParam, ShippingParam shippingParam) {
		if (orderParam.getId() == null) {
			throw new OrderManagerException();
		}

		// 주문 로그 기록을 위해 임시 호출
		OrderItem logOrderItem = this.getOrderItemForOrderLog(shippingParam.getOrderCode(),
				shippingParam.getOrderSequence(), shippingParam.getItemSequence());

		//운송장번호 입력 취소(배송 취소) [2017-09-06]minae.yun
		if ("cancel-delivery".equals(mode)) {

			// 2017.10.20 juneu.son 에스크로 주문건의 경우 배송취소 불가능
			if(!"N".equals(orderMapper.getOrderItemByEscrow(orderParam.getOrderCode())))
				throw new OrderManagerException("에스크로 결제 주문은 배송취소를 할 수 없습니다.");

			orderShippingMapper.updateShippingCancel(shippingParam);
		} else {
			throw new OrderManagerException();
		}

		// 주문 로그
		try {
			this.insertOrderLog(
					OrderLogType.ORDER_SHIPPING,
					shippingParam.getOrderCode(),
					shippingParam.getOrderSequence(),
					shippingParam.getItemSequence(),
					logOrderItem.getOrderStatus());
		} catch (RuntimeException e) {
//			log.error(ERROR_MARKER, e.getMessage(), e);
            log.error(ERROR_MARKER, getClass().getName() + " :: shippingListUpdate RuntimeException ==========", e);
		}
	}

	@Override
	public List<OrderList> getShippingFinishListByParam(OrderParam orderParam) {
		orderParam.encrypt(orderParamEncryptor);

		// 검색일자를 배송완료일로 디폴트
		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.SHIPPING_FINISH_DATE");
		}

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		int totalCount = orderShippingMapper.getShippingFinishCountByParam(orderParam);

		//if (orderParam.getItemsPerPage() == 10) {
		//	orderParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		if (orderParam.isDownloadExcel()) {
			pagination.setItemsPerPage(Integer.MAX_VALUE);
			orderParam.setItemsPerPage(Integer.MAX_VALUE);
		}
		orderParam.setPagination(pagination);
		orderParam.setLanguage(CommonUtils.getLanguage());

		List<OrderList> list = orderShippingMapper.getShippingFinishListByParam(orderParam);
		decryptOrderList(list);

		/*if (list != null) {
			for (OrderList order : list) {
				if (!orderParam.isDownloadExcel()) {
					shippingMaskingDataSet(order);
				} else if (!(SecurityUtils.hasRole("ROLE_EXCEL") && SecurityUtils.hasRole("ROLE_ISMS"))) {
					shippingMaskingDataSet(order);
				}
			}
		}*/

		// 기타 주문 상품 정보 세팅 (사은품, 세트상품)
		setOrderItemOther(list);
		orderParam.decrypt(orderParamEncryptor);

		return list;
	}

	@Override
	public List<OrderList> getConfirmListByParam(OrderParam orderParam) {

		// 주문일자 검색을 구매확정 일자로 디폴트
		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OI.CONFIRM_DATE");
		}

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		orderParam.encrypt(orderParamEncryptor);
		int totalCount = orderShippingMapper.getConfirmCountByParam(orderParam);

		//if (orderParam.getItemsPerPage() == 10) {
		//	orderParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		if (orderParam.isDownloadExcel()) {
			pagination.setItemsPerPage(Integer.MAX_VALUE);
			orderParam.setItemsPerPage(Integer.MAX_VALUE);
		}
		orderParam.setPagination(pagination);
		orderParam.setLanguage(CommonUtils.getLanguage());

		List<OrderList> list = orderShippingMapper.getConfirmListByParam(orderParam);
		decryptOrderList(list);

		// 기타 주문 상품 정보 세팅 (사은품, 세트상품)
		setOrderItemOther(list);
		orderParam.decrypt(orderParamEncryptor);

		return list;

	}

	@Override
	public void makeStockRestorationMap(HashMap<String, Integer> map, OrderItem orderItem, int quantity) {
		if (orderItem == null) {
			return;
		}

		// 재고량 복원
		int itemId = orderItem.getItemId();

		Item item = null;
		try {
			item = itemService.getItemById(itemId);
		} catch(RuntimeException e) {
//			log.warn("상품 정보가 조회되지 않음 ({}), {}", itemId, e.getMessage(), e);
            log.error("상품 정보가 조회되지 않음 ({}), {}", itemId, getClass().getName() + " :: makeStockRestorationMap RuntimeException ==========", e);
		}

		if (item == null) {
			return;
		}

		if (!ObjectUtils.isEmpty(orderItem.getOptions())) {
			List<ItemOption> buyOptions = ShopUtils.getRequiredItemOptions(item, orderItem.getOptions());
			if (buyOptions != null) {
				for (ItemOption itemOption : buyOptions) {
					String stockKey = "";
					if ("T".equals(itemOption.getOptionType())) {
						if ("Y".equals(item.getStockFlag())) {
							if (ObjectUtils.isEmpty(item.getStockCode()) == false) {
								stockKey = "STOCK||" + orderItem.getSellerId() + "||" + item.getStockCode();
							} else {
								stockKey = "ITEM||" + item.getItemId();
							}
						}
					} else {
						if ("Y".equals(itemOption.getOptionStockFlag())) {
							if (ObjectUtils.isEmpty(itemOption.getOptionStockCode()) == false) {
								stockKey = "STOCK||" + orderItem.getSellerId() + "||" + itemOption.getOptionStockCode();
							} else {
								stockKey = "OPTION||" + itemOption.getItemOptionId();
							}
						}
					}

					if (!"".equals(stockKey)) {
						int totalQuantity = quantity;
						if (map.get(stockKey) != null) {
							totalQuantity += map.get(stockKey);
						}

						map.put(stockKey, totalQuantity);
					}
				}
			}
		} else {
			if ("Y".equals(item.getStockFlag())) {
				String stockKey = "";
				if (ObjectUtils.isEmpty(item.getStockCode()) == false) {
					stockKey = "STOCK||" + orderItem.getSellerId() + "||" + item.getStockCode();
				} else {
					stockKey = "ITEM||" + item.getItemId();
				}

				int totalQuantity = quantity;
				if (map.get(stockKey) != null) {
					totalQuantity += map.get(stockKey);
				}

				map.put(stockKey, totalQuantity);
			}
		}
	}

	/**
	 * 재고량 복원
	 * @param map
	 * @return
	 */
	@Override
	public void stockRestoration(HashMap<String, Integer> map) {
		try {
			if (map == null) {
				return;
			}

			List<StockRestoration> stocks = new ArrayList<>();
			for(String key : map.keySet()) {
				int quantity = map.get(key);
				stocks.add(new StockRestoration(key, quantity));
			}

			for(StockRestoration stock : stocks) {

				if ("STOCK".equals(stock.getStockRestorationType())) {
					orderMapper.updateStockRestorationForItem(stock);
					orderMapper.updateStockRestorationForOption(stock);
				} else if ("ITEM".equals(stock.getStockRestorationType())) {
					orderMapper.updateStockRestorationForItem(stock);
				} else if ("OPTION".equals(stock.getStockRestorationType())) {
					orderMapper.updateStockRestorationForOption(stock);
				}

			}
		} catch (RuntimeException e) {

			log.error("재고량 복원 오류", e);
			if (ServiceType.LOCAL) {
				throw new OrderException("재고량 복원 오류");
			}
		}
	}

	@Override
	public int getOrderCountByParam(OrderParam orderParam) {
		return orderMapper.getOrderCountByParam(orderParam);
	}

	@Override
	public List<OrderList> getOrderListByParamForManager(OrderParam orderParam) {
		int totalCount = orderMapper.getOrderCountByParamForManager(orderParam);

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		orderParam.setPagination(pagination);


		List<OrderList> list = orderMapper.getOrderListByParamForManager(orderParam);
		decryptOrderList(list);
		return list;

	}

	private void decryptOrderList(List<OrderList> list) {
		if (list != null) {
			list.forEach(ol -> {
				ol.decrypt(orderListEncryptor, ShopUtils.needMasking());
			});
		}
	}

	// 엑셀 다운로드 시 마스킹 불필요
	private void decryptOrderListNoneMasking(List<OrderList> list) {
		if (list != null) {
			list.forEach(ol -> {
				ol.decrypt(orderListEncryptor, false);
			});
		}
	}

	@Override
	public List<Order> getOrderListByParam(OrderParam orderParam) {

		int totalCount = orderMapper.getOrderCountByParam(orderParam);

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());

		ShopUtils.setPaginationInfo(pagination, orderParam.getConditionType(), orderParam.getPage());
		/*
		if (ShopUtils.isMobilePage() && "DEFAULT_LIST".equals(orderParam.getConditionType())) {
			if (orderParam.getPage() > 1) {
				pagination.setStartRownum((int) 0);
				pagination.setEndRownum((int) pagination.getItemsPerPage() * orderParam.getPage());
			}
		}
		*/

		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		orderParam.setPagination(pagination);

		List<Order> list = getListByParam(orderParam);

		if (list == null) {
			return null;
		}

		HashSet<String> orderCodeSet = new HashSet<>();
		list.stream().forEach(order -> orderCodeSet.add(order.getOrderCode()));

//		String[] orderCodes = orderCodeSet.toArray(new String[orderCodeSet.size()]);
//
//		List<OrderGiftItem> orderGiftItems = orderGiftItemService.getOrderGiftItemListByOrderCodes(orderCodes);

		for (Order order : list) {
			bindOrder(order, orderParam);

//			for (OrderShippingInfo orderShippingInfo : order.getOrderShippingInfos()) {
//				for (OrderItem orderItem : orderShippingInfo.getOrderItems()) {
//					// 신상품
//					orderItem.setOrderGiftItemList(filterOrderGiftItems(orderGiftItems, orderItem.getOrderCode(), orderItem.getOrderSequence(), orderItem.getItemSequence()));
//				}
//			}
		}

		return list;
	}

	private List<Order> getListByParam(OrderParam orderParam) {
		List<Order> list = orderMapper.getOrderListByParam(orderParam);
		list.forEach(o -> decryptData(o));
		return list;
	}

	private void decryptData(Order order) {
		orderSupporter.decryptData(order);
	}

	@Override
	public Order getOrderByParam(OrderParam orderParam) {
		return orderSupporter.getOrderByParam(orderParam);
	}

	/**
	 * 주문정보 세팅
	 * @param order
	 * @param orderParam
	 */
	private void bindOrder(Order order, OrderParam orderParam) {
		orderSupporter.bindOrder(order, orderParam);
	}

	@Override
	public int getShippingCoupon(long userId, int addPoint) {

		// 비회원
		if (userId == 0) {
			if (!UserUtils.isUserLogin()) {
				return 0;
			}

			userId = UserUtils.getUserId();
		}

		// 회원 사용 가능 포인트 조회
		AvailablePoint availablePoint = pointService.getAvailablePointByUserId(userId, PointUtils.SHIPPING_COUPON_CODE);
		int retentionPoint = availablePoint.getAvailablePoint() + addPoint; // 보유 포인트
		// 0보다 작은경우가 있을수도..
		if (retentionPoint <= 0) {
			return 0;
		}

		return retentionPoint;
	}

	@Override
	public int getRetentionPoint(long userId, int addPoint) {

		// 비회원
		if (userId == 0) {
			if (!UserUtils.isUserLogin()) {
				return 0;
			}

			userId = UserUtils.getUserId();
		}

		Config shopConfig = ShopUtils.getConfig();

		// 회원 사용 가능 포인트 조회
		AvailablePoint availablePoint = pointService.getAvailablePointByUserId(userId, PointUtils.DEFAULT_POINT_CODE);
		int retentionPoint = availablePoint.getAvailablePoint() + addPoint; // 보유 포인트
		// 0보다 작은경우가 있을수도..
		if (retentionPoint <= 0) {
			return 0;
		}

		// 보유 포인트보다 사용 가능 최대 포인트가 큰경우
		int pointUseMax = shopConfig.getPointUseMax();
		int pointUseMin = shopConfig.getPointUseMin();

		// 최대 사용 가능 포인트가 0인경우는 포인트기능 OFF.. 사용 가능포인트는 0임.
		if (pointUseMax == 0) {
			return 0;
		}

		// 최대 사용 가능 포인트가 0보다 작은경우는 재한 없음.
		if (pointUseMax > 0) {
			// 최대 사용 가능 포인트가 0보다 큰경우 보유 포인트가 더 크면 최대 사용 가능 포인트로 포인트 셋팅
			if (retentionPoint > pointUseMax) {
				retentionPoint = shopConfig.getPointUseMax();
			}
		}

		// 보유 포인트가 최저 사용 가능 포인트보다 작은경우 사용 가능포인트 0
		if (retentionPoint < pointUseMin) {
			return 0;
		}

		return retentionPoint;
	}

	@Override
	public List<OrderCount> getOpmanagerOrderCountAll() {

		OrderParam orderParam = new OrderParam();
		orderParam.setConditionType("OPMANAGER");

		return orderMapper.getOrderCountListByParam(orderParam);
	}

	@Override
	public List<OrderCount> getSellerOrderCountAll() {

		OrderParam orderParam = new OrderParam();
		orderParam.setConditionType("SELLER");
		orderParam.setSellerId(SellerUtils.getSellerId());
		List<OrderCount> list = orderMapper.getOrderCountListByParam(orderParam);
//		return orderMapper.getOrderCountListByParam(orderParam);
		return list;
	}

	@Override
	public List<OrderCount> getUserOrderCountAll() {

		OrderParam orderParam = new OrderParam();
		orderParam.setConditionType("FRONT");
		orderParam.setUserId(UserUtils.getUserId());

		return orderMapper.getOrderCountListByParam(orderParam);
	}

	@Override
	public List<OrderCount> getOpmanagerOrderCountAllByMonth(int month) {
		OrderParam orderParam = new OrderParam();
		orderParam.setConditionType("OPMANAGER");

		setOrderCountAllParam(orderParam, month);

		return orderMapper.getOrderCountListByParam(orderParam);
	}

	@Override
	public List<OrderCount> getSellerOrderCountAllByMonth(int month) {
		OrderParam orderParam = new OrderParam();
		orderParam.setConditionType("SELLER");
		orderParam.setSellerId(SellerUtils.getSellerId());

		setOrderCountAllParam(orderParam, month);
		List<OrderCount> list = orderMapper.getOrderCountListByParam(orderParam);
//		return orderMapper.getOrderCountListByParam(orderParam);
		return list;
	}

	@Override
	public List<OrderCount> getUserOrderCountAllByMonth(int month) {

		OrderParam orderParam = new OrderParam();
		orderParam.setConditionType("FRONT");
		orderParam.setUserId(UserUtils.getUserId());

		setOrderCountAllParam(orderParam, month);

		return orderMapper.getOrderCountListByParam(orderParam);
	}

	private void setOrderCountAllParam(OrderParam orderParam, int month) {

		if (month > 0) {

			month = month > 0 ? month * -1 : month;

			String today = DateUtils.getToday(DATE_FORMAT);
			orderParam.setSearchEndDate(today);
			orderParam.setSearchStartDate(DateUtils.addMonth(today, month));
		}
	}

	@Override
	public HashMap<String, BuyPayment> getPaymentType() {
		HashMap<String, BuyPayment> map = new HashMap<String, BuyPayment>();
		// 결제 방법 포인트로 강제 세팅..
//		Config shopConfig = configService.getShopConfig(Config.SHOP_CONFIG_ID);
		ConfigPg configPg = configPgService.getConfigPg();
		/*
		if ("Y".equals(shopConfig.getPaymentBank())) {
			List<AccountNumber> accountNumberList = null;
			if (shopConfig.getPaymentBank() != null) {
				accountNumberList = accountNumberService.getUseAccountNumberListAll();
				if (!accountNumberList.isEmpty()) {
					map.put("bank", new BuyPayment("bank", accountNumberList, configPg));
				}
			}
		}

		if (UserUtils.isUserLogin()) {
			for(String ptCode : PointUtils.getPointTypes()) {
				map.put(ptCode, new BuyPayment(ptCode, configPg));
			}
		}

		if ("Y".equals(shopConfig.getPaymentCard())) {
			map.put("card", new BuyPayment("card", configPg));
		}

		if ("Y".equals(shopConfig.getPaymentHp())) {
			map.put("hp", new BuyPayment("hp", configPg));
		}

		if ("Y".equals(shopConfig.getPaymentRealtime())) {
			map.put("realtimebank", new BuyPayment("realtimebank", configPg));
		}

		if ("Y".equals(shopConfig.getPaymentVbank())) {
			map.put("vbank", new BuyPayment("vbank", configPg));
		}

		if ("Y".equals(shopConfig.getPaymentEscrow())) {
			map.put("escrow", new BuyPayment("escrow", configPg));
		}

		if (configPg.isUseNpayPayment()) {
            map.put("naverpay", new BuyPayment("naverpay", configPg));
        }

		// [Kakaopay] 관리자 결제 정보 설정 DB에는 추가하지 않음.
		//map.put("kakaopay", new BuyPayment("kakaopay", configPg));

		map.put("payco", new BuyPayment("payco", configPg));*/

		map.put(PointUtils.GIVE_CODE, new BuyPayment(PointUtils.GIVE_CODE, configPg));		// 결제 방법 기부포인트로 강제 세팅..

		return map;

	}

	@Override
	public Buy getBuyForStep1(OrderParam orderParam, User user) {
		Buy buy = this.getOrderTemp(orderParam);
		UserDelivery defaultUserDelivery = userDeliveryService.getDefaultUserDelivery();
		if (buy == null) {

			buy = new Buy();
			Buyer buyer = new Buyer();
			Receiver receiver = new Receiver();

			boolean isDeliverySet = false;
			if (defaultUserDelivery != null) {
				isDeliverySet = true;
				//				receiver.setReceiveCompanyName(defaultUserDelivery.getCompanyName());
				receiver.setReceiveName(defaultUserDelivery.getUserName());
				receiver.setReceiveMobile(defaultUserDelivery.getMobile());
				receiver.setReceivePhone(defaultUserDelivery.getPhone());
				receiver.setReceiveNewZipcode(defaultUserDelivery.getNewZipcode());
				receiver.setReceiveZipcode(defaultUserDelivery.getZipcode());
				receiver.setReceiveAddress(defaultUserDelivery.getAddress());
				receiver.setReceiveAddressDetail(defaultUserDelivery.getAddressDetail());
				receiver.setReceiveSido(defaultUserDelivery.getSido());
				receiver.setReceiveSigungu(defaultUserDelivery.getSigungu());
				receiver.setReceiveEupmyeondong(defaultUserDelivery.getEupmyeondong());
			}

			// 회원 로그인 상태일경우 회원 정보를 가져옴..
//			if (UserUtils.isUserLogin()) {
			if (user != null && user.getUserId() > 0) {
//				User user = UserUtils.getUser();
				UserDetail userDetail = userService.getUserDetail(user.getUserId());

				buyer.setUserName(user.getUserName());
				if (isDeliverySet == false) {
					receiver.setReceiveName(user.getUserName());
				}

                buyer.setLoginId(user.getLoginId());
				buyer.setEmail(user.getEmail());
				if (userDetail != null) {
					//					buyer.setCompanyName(userDetail.getCompanyName());

					buyer.setMobile(userDetail.getPhoneNumber());
					buyer.setPhone(userDetail.getTelNumber());
					buyer.setNewZipcode(userDetail.getNewPost());
					buyer.setZipcode(userDetail.getPost());
					buyer.setZipcode1(userDetail.getPost1());
					buyer.setZipcode2(userDetail.getPost2());
					buyer.setAddress(userDetail.getAddress());
					buyer.setAddressDetail(userDetail.getAddressDetail());

//					buyer.setSido(ShopUtils.getSido(userDetail.getAddress()));
//					buyer.setSigungu(ShopUtils.getSigungu(userDetail.getAddress()));
//					buyer.setEupmyeondong(ShopUtils.getEupmyeondong(userDetail.getAddress()));

					if (isDeliverySet == false) {
						//receiver.setReceiveCompanyName(userDetail.getCompanyName());
						receiver.setReceiveMobile(userDetail.getPhoneNumber());
						receiver.setReceivePhone(userDetail.getTelNumber());
						receiver.setReceiveNewZipcode(userDetail.getNewPost());
						receiver.setReceiveZipcode(userDetail.getPost());
						receiver.setReceiveZipcode1(userDetail.getPost1());
						receiver.setReceiveZipcode2(userDetail.getPost2());
						receiver.setReceiveAddress(userDetail.getAddress());

//						receiver.setReceiveSido(ShopUtils.getSido(userDetail.getAddress()));
//						receiver.setReceiveSigungu(ShopUtils.getSigungu(userDetail.getAddress()));
//						receiver.setReceiveEupmyeondong(ShopUtils.getEupmyeondong(userDetail.getAddress()));

						receiver.setReceiveAddressDetail(userDetail.getAddressDetail());
					}
				}
			}

			buy.setUserId(orderParam.getUserId());
			buy.setSessionId(orderParam.getSessionId());
			buy.setBuyer(buyer);

			// step1에 들어올때는 배송지 정보가 1개임
			List<Receiver> receivers = new ArrayList<>();
			receiver.setShippingIndex(0);
			receivers.add(receiver);
			buy.setReceivers(receivers);
		}

		List<BuyItem> items = this.getOrderItemTempList(orderParam);
		if (items == null || items.isEmpty()) {
			throw new OrderException(ApiError.BAD_BUY_NO_ITEM, "/cart");
		}

		// CJH 추가 구성품이 있는경우 복수배송지 선택 불가
		boolean isAdditionItem = false;
		for (BuyItem item : items) {
			item.setOptionsOriginal(item.getOptions());
			item.setOptions(ShopUtils.viewOptionTextNoUl(item.getOptions()));

//			if ("Y".equals(item.getAdditionItemFlag())) {
//				isAdditionItem = true;
//			}
		}

		buy.setAdditionItem(isAdditionItem);

		HashMap<Long, String> sellerMap = new HashMap<>();
		for (Receiver receiver : buy.getReceivers()) {

			receiver.setItems(items);

			// 상품쿠폰 적용
			receiver.itemCouponUsed(false, buy, receiver.getShippingIndex());

			// 배송지별 구매상품 초기화
			List<BuyQuantity> buyQuantitys = new ArrayList<>();
			for (BuyItem buyItem : items) {
				BuyQuantity buyQuantity = new BuyQuantity();
				buyQuantity.setItemSequence(buyItem.getItemSequence());
				buyQuantity.setQuantity(buyItem.getItemPrice().getQuantity());
				buyQuantitys.add(buyQuantity);

				// 개인정보 제 3자 제공 및 수집 이용동의
				if ("2".equals(buyItem.getItem().getShippingType())) {
					Seller seller = sellerMapper.getSellerById(buyItem.getSellerId());
					if (seller != null) {
						sellerMap.put(seller.getSellerId(), seller.getSellerName());
					}
				}
			}

			receiver.setBuyQuantitys(buyQuantitys);

			String islandType = "";
//			if (ObjectUtils.isEmpty(receiver.getReceiveZipcode()) == false) {
//				islandType = orderMapper.getIslandTypeByZipcode(receiver.getReceiveZipcode());
//			}

			receiver.setShipping(islandType);
		}

		String sellerNames = "";
		for (Long sellerId : sellerMap.keySet()) {
			sellerNames += (ObjectUtils.isEmpty(sellerNames) ? "" : ", ") + sellerMap.get(sellerId);
		}
		buy.setSellerNames(sellerNames);

		// 결제금액 재계산
		buy.setOrderPrice(0, configService.getShopConfig(Config.SHOP_CONFIG_ID));

		// 회원 사용가능 포인트 조회
//		buy.setRetentionPoint(this.getRetentionPoint(orderParam.getUserId(), 0));

		// 회원 사용가능 배송비 할인 쿠폰
//		buy.setShippingCoupon(this.getShippingCoupon(orderParam.getUserId(), 0));

		// 회원 기본배송지 정보 저장
		buy.setDefaultUserDelivery(defaultUserDelivery);

		return buy;
	}

	@Override
	public Buy getOrderTemp(OrderParam orderParam) {
		return orderMapper.getOrderTemp(orderParam);
	}

	private void setOrderItemSetInfo(BuyItem buyItem, List<BuyItem> buyItemSets, HashMap<String, Integer> buySetQuantityMap) {
		// 세트상품 재고차감 + 재고검증용
		HashMap<String, HashMap<String, Integer>> stockSetMap = ShopUtils.makeStockSetMap(buyItem, buyItemSets);

		if (stockSetMap == null) {
			stockSetMap = new HashMap<>();
		}

		if (buySetQuantityMap == null) {
			buySetQuantityMap = new HashMap<>();
		}

		// 세트상품의 구성품이 변경되었는지 체크
		List<ItemSet> itemSets = itemMapper.getItemSetListByItemId(buyItem.getItemId());

		// 세트 정보 없을경우 구매 불가
		if (buyItemSets == null || buyItemSets.isEmpty() || itemSets == null || itemSets.isEmpty()) {
			buyItem.setAvailableForSaleFlag("N");
			buyItem.setSystemComment(buyItem.getItemName() + "상품의 세트 구성이 없습니다.");
		} else {
			for (BuyItem buyItemSet : buyItemSets) {
				Item item = buyItemSet.getItem();
				if (item == null || buyItemSet == null) {
					continue;
				}

				// 구매 세트 정보와 실제 세트 정보 비교하여 구성품이 달라졌는지 체크
				boolean isDelete = true;
				for (ItemSet itemSet : itemSets) {
					if (itemSet != null && buyItemSet.getItemId() == itemSet.getItemId() && buyItemSet.getItemPrice().getQuantity() == itemSet.getQuantity()) {
						isDelete = false;
						break;
					}
				}

				// 세트 구성품이 변경되었으면 구매 불가 (pass)
				if (isDelete) {
					buyItemSet.setAvailableForSaleFlag("N");
					buyItemSet.setSystemComment(buyItem.getItemName() + "상품의 세트 구성이 변경되어 구매가 불가능합니다. (" + buyItemSet.getItemName() + ")");
				} else {
					// 1. 세트상품의 옵션 설정
					item.setItemOptions(itemMapper.getItemOptionList(item.getItemId()));
					buyItemSet.setOptionList(ShopUtils.getRequiredItemOptions(item, buyItemSet.getOptions()));

					// 2. 세트상품 재고 차감 정보 생성
					ShopUtils.setBuySetQuantityMap(buyItem, buyItemSet, stockSetMap, buySetQuantityMap);

					// 3. 상품에 옵션이 있고 상품 옵션 구성이 null인경우 상품의 구성이 변경됨..
					if ("Y".equals(item.getItemOptionFlag())) {
						if (item.getItemOptions() != null) {
							if (buyItemSet.getOptionList() == null) {
								buyItemSet.setAvailableForSaleFlag("N");
								buyItemSet.setSystemComment(MessageUtils.getMessage("M00485") + MessageUtils.getMessage("M00484"));
							}
						}
					}

					if ("Y".equals(buyItemSet.getAvailableForSaleFlag())) {
						if (!ObjectUtils.isEmpty(buyItemSet.getOptions())) {
							// 장바구니에는 상품의 옵션정보가 선택되어있으나 상품의 구성이 변경됨.. ex) 상품의 옵션 삭제등..
							if (buyItemSet.getOptionList() == null) {
								buyItemSet.setAvailableForSaleFlag("N");
								buyItemSet.setSystemComment(MessageUtils.getMessage("M00485") + MessageUtils.getMessage("M00484"));
							}
						}
					}

					if ("Y".equals(buyItemSet.getAvailableForSaleFlag())) {
						// 적립금은 로그인한 회원에게만 부여됨..
						if (UserUtils.isUserLogin()) {
							buyItemSet.setPointPolicy(pointService.getPointPolicyByItemId(item.getItemId()));
						}

						// 상품 금액 계산
						buyItemSet.setItemPrice(new ItemPrice(buyItemSet));
					}
				}
			}
		}
	}

	@Override
	public void insertOrderItemTemp(BuyItem buyItem) {
		orderMapper.insertOrderItemTemp(buyItem);
	}

	@Override
	public void insertOrderItemSetTemp(BuyItem buyItem) {
		orderMapper.insertOrderItemSetTemp(buyItem);
	}

	@Override
	public void deleteOrderItemTemp(long userId, String sessionId) {

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(userId);
		orderParam.setSessionId(sessionId);

		orderMapper.deleteOrderItemTemp(orderParam);
	}

	@Override
	public void deleteOrderItemSetTemp(long userId, String sessionId) {

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(userId);
		orderParam.setSessionId(sessionId);

		orderMapper.deleteOrderItemSetTemp(orderParam);
	}

	@Override
	public void deleteOrderTemp(long userId, String sessionId) {
		Buy buy = new Buy();
		buy.setUserId(userId);
		buy.setSessionId(sessionId);
		orderMapper.deleteOrderTemp(buy);
	}

	private void setOrderItemInfo(List<BuyItem> list, OrderParam orderParam,
								  HashMap<String, Integer> buyQuantityMap, HashMap<String, Integer> buySetQuantityMap) {

		// 상품에 사용가능한 쿠폰 조회
//		List<OrderCoupon> orderCouponListForItemAll = null;
//		List<OrderCoupon> orderCouponListForItem = null;
//
//		String couponTarget = orderParam.isMobile() ? "MOBILE" : "WEB";
//		orderParam.setViewTarget(couponTarget);
//
//		UserLevel userLevel = null;
//
//		if (UserUtils.isUserLogin()) {
//			orderCouponListForItem = couponService.getUserCouponListForItemTarget(list, orderParam.getUserId(), orderParam.getViewTarget());
//			orderCouponListForItemAll = couponService.getUserCouponListForItemAll(orderParam.getUserId(), orderParam.getViewTarget());
//
//			User user = userService.getUserByUserId(UserUtils.getUserId());
//			UserDetail userDetail = (UserDetail) user.getUserDetail();
//
//			// 회원 등급별 포인트 적립액 조회
//			userLevel = userLevelMapper.getUserLevelById(userDetail.getLevelId());
//		}

		HashMap<String, Integer> buyQuantityItemUserCodeMap = new HashMap<>();

		// 재고 차감 + 재고 검증용
		HashMap<String, HashMap<String, Integer>> stockMap = ShopUtils.makeStockMap(list);

		if (stockMap == null) {
			stockMap = new HashMap<>();
		}

		if (buyQuantityMap == null) {
			buyQuantityMap = new HashMap<>();
		}

		for (BuyItem buyItem : list) {
			Item item = buyItem.getItem();
			if (item == null) {
				continue;
			}
			buyItem.setPayDate(buyItem.getCreatedDate());			// 결제일
			buyItem.setSalesDate(buyItem.getCreatedDate());			// 매출일자
			buyItem.setRevenueSalesStatus(ShopUtils.getConfig().getRevenueSalesStatus());				// 매출 기준 상태 => 0 : 주문 등록시, 10 : 결제 확인, 20 : 배송 준비
//			buyItem.setEscrowStatus("10");		// 주문상품테이블 필수값이라서 고정 세팅함, 결제 완료 => 에스크로 진행상태(0: 입금대기 10: 결제완료 20: 배송등록완료 30: 구매확정확인 40: 구매거부확인 50: 구매확정승인 60: 구매거부승인 70: 구매확정 후 상점 임의환불 승인)
			buyItem.setEscrowStatus("N");		// 현금 결제 플래그라서 N 처리


			item.setItemOptions(itemMapper.getItemOptionList(item.getItemId()));
			item.setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItemImage(), "S"));

			// 1. 상품의 필수 옵션을 구성
			if (!StringUtils.hasLength(buyItem.getOptionsOriginal())) {
				if (StringUtils.hasLength(buyItem.getOptions())) {
					buyItem.setOptionList(ShopUtils.getRequiredItemOptions(item, buyItem.getOptions()));
				}
			} else {
				buyItem.setOptionList(ShopUtils.getRequiredItemOptions(item, buyItem.getOptionsOriginal()));
			}

			String itemUserCode = item.getItemUserCode();

			if(itemUserCode != null && buyItem != null && buyItem.getItemPrice() != null) {
				if (buyQuantityItemUserCodeMap.get(itemUserCode) == null) {
					buyQuantityItemUserCodeMap.put(itemUserCode, buyItem.getItemPrice().getQuantity());
				} else {
					buyQuantityItemUserCodeMap.put(itemUserCode, buyQuantityItemUserCodeMap.get(itemUserCode) + buyItem.getItemPrice().getQuantity());
				}
			}

			// 2. 상품의 최대 구매 가능 수량을 책정하여 판매 불가 상품인지 검증 - 필수 선택 옵션정보도 포함하여 검증
			ShopUtils.setBuyQuantityMap(buyItem, stockMap, buyQuantityMap);

			boolean isError = false;
			if (buyQuantityItemUserCodeMap.get(itemUserCode) != null) {
				if (item.getOrderMinQuantity() > buyQuantityItemUserCodeMap.get(itemUserCode)) {
					isError = true;
				}
			}

			if (isError) {
				throw new OrderException("해당 상품의 최소 구매가능 수량은 "+ item.getOrderMinQuantity() +"개 입니다.", "/cart");
			}

			OrderQuantity orderQuantity = buyItem.getOrderQuantity();
			boolean isSoldOut = orderQuantity.getMaxQuantity() == 0 ? true : false;

			// 3. 상품이 판매 종료된 상품인지 검사
			if (isSoldOut) {
				throw new OrderException(item.getItemName() + "상품의 재고가 없습니다.", "/cart");
			} else {
				// 세트상품
//				if ("Y".equals(buyItem.getSetItemFlag())) {
//					this.setOrderItemSetInfo(buyItem, buyItem.getItemSets(), buySetQuantityMap);
//				} else {
					// 상품에 옵션이 있고 상품 옵션 구성이 null인경우 상품의 구성이 변경됨..
					if ("Y".equals(item.getItemOptionFlag())) {
						if (item.getItemOptions() != null) {
							if (buyItem.getOptionList() == null && !StringUtils.hasLength(buyItem.getOptionsOriginal())) {
								buyItem.setAvailableForSaleFlag("N");
								buyItem.setSystemComment(MessageUtils.getMessage("M00485") + " " + buyItem.getItemName() + MessageUtils.getMessage("M00484"));
							}
						}
					}

					if ("Y".equals(buyItem.getAvailableForSaleFlag())) {
						if (StringUtils.isNotEmpty(buyItem.getOptions())) {
							// 장바구니에는 상품의 옵션정보가 선택되어있으나 상품의 구성이 변경됨.. ex) 상품의 옵션 삭제등..
							if (buyItem.getOptionList() == null && !StringUtils.hasLength(buyItem.getOptionsOriginal())) {
								buyItem.setAvailableForSaleFlag("N");
								buyItem.setSystemComment(MessageUtils.getMessage("M00485") + " " + buyItem.getItemName() + MessageUtils.getMessage("M00484"));
							}
						}
					}
//				}
			}

//			buyItem.setUserLevel(userLevel);
//
//			if ("Y".equals(buyItem.getAvailableForSaleFlag())) {
//				// 적립금은 로그인한 회원에게만 부여됨..
//				if (UserUtils.isUserLogin()) {
//					buyItem.setPointPolicy(pointService.getPointPolicyByItemId(item.getItemId()));
//				}
//
//				// 상품 금액 개산
				buyItem.setItemPrice(new ItemPrice(buyItem));
//
//				// 전체 상품에 사용가능 쿠폰 - 일반 쿠폰
//				buyItem.setItemCoupons(orderCouponListForItemAll, true);
//
//				// 해당 상품에 사용가능 쿠폰 - 일반 쿠폰
//				buyItem.setItemCoupons(orderCouponListForItem, false);
//			}
//
//			// 사은품
//			if ("Y".equals(item.getFreeGiftFlag())) {
//				try {
//					List<GiftItem> freeGiftItemList = giftItemService.getGiftItemListForFront(item.getItemId());
//					buyItem.setFreeGiftItemText(ShopUtils.makeGiftItemText(freeGiftItemList));
//					buyItem.setFreeGiftItemList(ShopUtils.conventGiftItemInfoList(freeGiftItemList));
//				} catch (RuntimeException e) {
////					log.error(ERROR_MARKER, e.getMessage(), e);
//		            log.error(ERROR_MARKER, getClass().getName() + " :: setOrderItemInfo RuntimeException ==========", e);
//				}
//			}
		}
	}

	@Override
	public List<BuyItem> getOrderItemTempList(OrderParam orderParam) {
		List<BuyItem> list = orderMapper.getOrderItemTempList(orderParam);

		for (BuyItem item : list) {
			// 세트상품이 있다면
			if ("Y".equals(item.getSetItemFlag())) {
				orderParam.setSetItemSequence(item.getItemSequence());
				item.setItemSets(orderMapper.getOrderItemSetTempList(orderParam));
			}
		}

		if (ValidationUtils.isNull(list)) {
			return null;
		}

		setOrderItemInfo(list, orderParam, null, null);

		return list;
	}

	// 결제 금액 검증
	private void payAmountVerification(Buy buy, OrderPrice postOrderPrice) {
		OrderPrice orderPrice = buy.getOrderPrice();

		Config shopConfig = ShopUtils.getConfig();
		if (postOrderPrice.getTotalPointDiscountAmount() > 0) {

			// 비회원이 포인트 사용?
			if (!buy.getIsLogin()) {
				throw new OrderException(MessageUtils.getMessage("M00481")); // 잘못된 접근입니다.
			}

			// 사용 가능한 포인트 금액보다 더 많이씀..
			int retentionPoint = this.getRetentionPoint(buy.getUserId(), 0);
			if (postOrderPrice.getTotalPointDiscountAmount() > retentionPoint) {
				throw new OrderException(MessageUtils.getMessage("M01261"));	// 해당 주문에서 사용 가능한 포인트를 다시 확인해주세요.
			}


			int pointUseMax = shopConfig.getPointUseMax();
			int pointUseMin = shopConfig.getPointUseMin();

			if (pointUseMax == 0) {
				throw new OrderException(MessageUtils.getMessage("M01262"));	// 포인트사용 기능은 현재 사이트에서 재공되지 않습니다.
			}

			if (postOrderPrice.getTotalPointDiscountAmount() < pointUseMin) {
				throw new OrderException(MessageUtils.getMessage("M01261"));
			}

			if (postOrderPrice.getTotalPointDiscountAmount() != orderPrice.getTotalPointDiscountAmount()) {
				throw new OrderException(MessageUtils.getMessage("M01263"));	// 포인트 사용 금액이 잘못 되었습니다.
			}
		}

		if (postOrderPrice.getTotalShippingCouponUseCount() > 0) {
			int shippingCoupon = this.getShippingCoupon(buy.getUserId(), 0);
			if (postOrderPrice.getTotalShippingCouponUseCount() > shippingCoupon) {
				throw new OrderException("사용가능한 배송비 쿠폰 수량을 확인해주세요.");
			}

			if (postOrderPrice.getTotalShippingCouponDiscountAmount()  != orderPrice.getTotalShippingCouponDiscountAmount()) {
				throw new OrderException("베송비 쿠폰 사용액을 확인해주세요.");
			}
		}

		int totalCouponDiscountAmount = postOrderPrice.getTotalItemCouponDiscountAmount() + postOrderPrice.getTotalCartCouponDiscountAmount();


		if (totalCouponDiscountAmount != orderPrice.getTotalCouponDiscountAmount()) {
			throw new OrderException(MessageUtils.getMessage("M01264"));	// 쿠폰 사용 금액이 잘못 되었습니다.
		}


		if (postOrderPrice.getOrderPayAmount() != orderPrice.getOrderPayAmount()) {
			throw new OrderException(MessageUtils.getMessage("M01265"));	// 결제 금액이 변경 되어 주문을 계속 진행하실수 없습니다.
		}

		int minimumPaymentAmount = shopConfig.getMinimumPaymentAmount();
		if (minimumPaymentAmount > 0) {
			if (orderPrice.getOrderPayAmount() < minimumPaymentAmount) {
				throw new OrderException(MessageUtils.getMessage("M01046") +" (" + NumberUtils.formatNumber(minimumPaymentAmount, "#,##0") + ") "+ MessageUtils.getMessage("M01266"));	//최소 결제 가능 금액(1,000)보다 결제 시도 하시는 금액이 작아 결제를 진행하실수 없습니다.
			}
		}
	}

	private OrderPrice newOrderPrice(OrderPrice orderPrice) {

		OrderPrice newOrderPrice = new OrderPrice();
		newOrderPrice.setTotalItemCouponDiscountAmount(orderPrice.getTotalItemCouponDiscountAmount());
		newOrderPrice.setTotalCartCouponDiscountAmount(orderPrice.getTotalCartCouponDiscountAmount());
		newOrderPrice.setTotalPointDiscountAmount(orderPrice.getTotalPointDiscountAmount());
		newOrderPrice.setOrderPayAmount(orderPrice.getOrderPayAmount());
		newOrderPrice.setTotalShippingCouponUseCount(orderPrice.getTotalShippingCouponUseCount());
		newOrderPrice.setTotalShippingCouponDiscountAmount(orderPrice.getTotalShippingCouponDiscountAmount());

		return newOrderPrice;
	}

	@Override
	public HashMap<String, Object> saveOrderTemp(HttpSession session, Buy buy) {

		String escrowStatus = "N";

		OrderParam orderParam = new OrderParam();
		orderParam.setSessionId(buy.getSessionId());
		orderParam.setUserId(buy.getUserId());
		orderParam.setViewTarget(buy.getDeviceType());

		OrderPrice orderPrice = buy.getOrderPrice();

        String createdDate = DateUtils.getToday(DATETIME_FORMAT);

		// Post로 넘어온 결제 정보로 새로운 객채를 생성하여 계산된 결제 금액과 동일한지 체크함
		OrderPrice postOrderPrice = this.newOrderPrice(orderPrice);
		List<BuyItem> list = orderMapper.getOrderItemTempList(orderParam);
		if (list == null) {
			throw new OrderException("주문 가능 상품이 없습니다.", "/cart");
//		} else {
//			for (BuyItem item : list) {
//				// 세트상품이 있는 경우 세트 temp 셋
//				if ("Y".equals(item.getSetItemFlag())) {
//					orderParam.setSetItemSequence(item.getItemSequence());
//					item.setItemSets(orderMapper.getOrderItemSetTempList(orderParam));
//				}
//			}
		}

		// 배송지 지정되지 않은 상품있는지 체크용
		HashMap<String, Integer> checkQuantityTotal = new HashMap<>();
		for (BuyItem buyItem : list) {
			if (buyItem != null) {
				ItemPrice itemPrice = buyItem.getItemPrice();
				if (itemPrice != null) {
					String key = "item-" + buyItem.getItemSequence();

					int addCount = 0;
					if (checkQuantityTotal.get(key) != null) {
						addCount = checkQuantityTotal.get(key);
					}

					checkQuantityTotal.put(key, itemPrice.getQuantity() + addCount);
				}
			}
		}

		// 복합 배송지로인한 상품정보 재정의
		int shippingIndex = 0;
		for(Receiver receiver : buy.getReceivers()) {

			receiver.setShippingIndex(shippingIndex);
			List<BuyItem> items = new ArrayList<>();

			for(BuyQuantity buyQuantity : receiver.getBuyQuantitys()) {

				for(BuyItem buyItem : list) {
					if (buyQuantity.getItemSequence() == buyItem.getItemSequence()) {

						// 복사해야됨...ㅠㅠ
						BuyItem cloneObject;

						try {

							String checkKey = "item-" + buyQuantity.getItemSequence();
							int buyTotalCount = checkQuantityTotal.get(checkKey);
							if (buyTotalCount - buyQuantity.getQuantity() == 0) {
								checkQuantityTotal.remove(checkKey);
							} else {

								if (buyTotalCount - buyQuantity.getQuantity() > 0) {
									checkQuantityTotal.put(checkKey, buyTotalCount - buyQuantity.getQuantity());
								} else {

									// 장바구니에 담겨있는 수량보다 구매시도 수량이 많은경우 멈춰!!
									throw new OrderException("장바구니에 담겨있는 수량보다 구매시도 수량이 많습니다.");
								}
							}


							cloneObject = (BuyItem) buyItem.clone();
							if(cloneObject.getItemPrice() != null && !ObjectUtils.isEmpty(buyQuantity.getQuantity())) {
								cloneObject.getItemPrice().setQuantity(buyQuantity.getQuantity());
							}

							items.add(cloneObject);

						} catch (CloneNotSupportedException e) {
//							throw new OrderException(e.getMessage(), e);
							throw new OrderException(e);
						}

						break;
					}
				}

			}

			// 상품정보 셋팅
			setOrderItemInfo(items, orderParam, null, null);

			// 구매가능여부 채크
			ShopUtils.buyVerification(items, items.size());

			receiver.setItems(items);

			// 상품쿠폰 적용
			receiver.itemCouponUsed(true, buy, receiver.getShippingIndex());

			// 구매 상품 정책별 그룹
			String zipcode = receiver.getReceiveZipcode();
			if (ObjectUtils.isEmpty(zipcode)) {
				zipcode = receiver.getReceiveZipcode();
			}

			receiver.setShipping(orderMapper.getIslandTypeByZipcode(zipcode));
			shippingIndex++;
		}

		if (checkQuantityTotal.keySet().size() > 0) {
			throw new OrderException("배송지가 지정되지 않은 상품이 있습니다.");
		}

		// 사용 포인트 적용
		orderPrice.setTotalPointDiscountAmount(postOrderPrice.getTotalPointDiscountAmount());

		// 배송비 할인쿠폰 적용 - 복수 배송지 사용하면 해당 기능 사용안됨
		int shippingCouponCount = 0;
		int totalShippingCouponDiscountAmount = 0;

		List<ShippingCoupon> insertShippingCoupons = new ArrayList<>();
		if (buy.getReceivers().size() <= 1 && UserUtils.isUserLogin()) {
			if (buy.getUseShippingCoupon() != null) {
				List<Shipping> shippings = buy.getReceivers().get(0).getItemGroups();
				for(String key : buy.getUseShippingCoupon().keySet()) {
					ShippingCoupon sCoupon = buy.getUseShippingCoupon().get(key);

					if ("Y".equals(sCoupon.getUseFlag())) {
						for(Shipping shipping : shippings) {

							// 2016.4.27 CJH - 상품 개당 배송비일때 쿠폰 사용 못함[고객사요청]
							if ("5".equals(shipping.getShippingType())) {
								continue;
							}

							sCoupon.setUseCouponCount(1);
							sCoupon.setUserId(UserUtils.getUserId());
							sCoupon.setDiscountAmount(shipping.getRealShipping());

							if (shipping.getRealShipping() > 0) {
								if (shipping.getShippingGroupCode().equals(sCoupon.getShippingGroupCode())) {
									shipping.setDiscountShipping(shipping.getRealShipping() - shipping.getAddDeliveryCharge());

									shipping.setPayShipping(shipping.getRealShipping() - shipping.getDiscountShipping());
									shippingCouponCount++;
									totalShippingCouponDiscountAmount += shipping.getDiscountShipping();
									insertShippingCoupons.add(sCoupon);
									break;
								}
							}
						}
					}
				}

			}
		}

		orderPrice.setTotalShippingCouponUseCount(shippingCouponCount);
		orderPrice.setTotalShippingCouponDiscountAmount(totalShippingCouponDiscountAmount);
		buy.setShippingCoupon(shippingCouponCount);

		buy.setOrderPrice(0, configService.getShopConfig(Config.SHOP_CONFIG_ID));

		HashMap<String, BuyPayment> payments = new HashMap<String, BuyPayment>();
		try {
			// 결제금액 검증
			this.payAmountVerification(buy, postOrderPrice);

			int notMixPayTypeCount = 0;
			int totalPayAmount = 0;
			int totalPointPayAmount = 0;
			HashMap<String, BuyPayment> buyPayments = buy.getBuyPayments();
			for(String key : buyPayments.keySet()) {
				BuyPayment buyPayment = buyPayments.get(key);

				//에스크로 사용여부 확인
				if(buyPayment.getEscrowStatus() != null && buyPayment.getEscrowStatus().equals("0"))
					escrowStatus = buyPayment.getEscrowStatus();

				if (buyPayment.getAmount() > 0) {

					if (ArrayUtils.contains(buy.getNotMixPayType(), key)) {
						notMixPayTypeCount++;
					}

					if (PointUtils.isPointType(key)) {
						totalPointPayAmount += buyPayment.getAmount();
					} else {
						totalPayAmount += buyPayment.getAmount();
					}

					payments.put(key, buyPayment);
				}
			}

			if (buy.getOrderPrice().getTotalPointDiscountAmount() != totalPointPayAmount) {
				throw new OrderException(MessageUtils.getMessage("M00246") + " 결제금액을 확인해주세요.");
			}

			// 결제금액 한번더 검증
			postOrderPrice.setOrderPayAmount(totalPayAmount);
			this.payAmountVerification(buy, postOrderPrice);

			if (notMixPayTypeCount > 1) {
				throw new OrderException("복합 결제가 불가능한 결제 방식을 1개이상 선택하여 결제가 진행되지 않았습니다.");
			}



		} catch (OrderException oe) {
			throw new OrderException(oe.getErrorMessage(), oe.getRedirectUrl(), oe);
		}

		Buyer buyer = buy.getBuyer();
		buyer.processHyphen();

		// 기존 주문 임시 데이터 삭제 - 주문자 정보등만 삭제 한다. * 상품 임시 데이터는 유지
		orderMapper.deleteOrderTemp(buy);

		String productName = buy.getItems().get(0).getItem().getItemName();
		if (buy.getItems().size() > 1) {
			productName += " 외 " + (buy.getItems().size() - 1) + "개";
		}

		if (!UserUtils.isUserLogin()) {

			Receiver receiver = buy.getReceivers().get(0);

			// 비회원의 경우 주문자정보 주소 입력란이 없음.. 받는사람 주소로 대신함
			buyer.setSido(receiver.getReceiveSido());
			buyer.setSigungu(receiver.getReceiveSigungu());
			buyer.setEupmyeondong(receiver.getReceiveEupmyeondong());
			buyer.setNewZipcode(receiver.getReceiveNewZipcode());
			buyer.setZipcode(receiver.getReceiveZipcode());
			buyer.setAddress(receiver.getReceiveAddress());
			buyer.setAddressDetail(receiver.getReceiveAddressDetail());

		}

		HashMap<String, Object> map = new HashMap<>();
		map.put("productName", productName);
		map.put("userName", buyer.getUserName());
		map.put("email", buyer.getEmail());
		map.put("mobile", buyer.getMobile());
		map.put("orderCode", buy.getOrderCode());


		int totalTaxFreeAmount = buy.getOrderPrice().getTaxFreeAmount();

		ConfigPg configPg = configPgService.getConfigPg();
		List<String> savePaymentType = new ArrayList<>();
		for(String key : payments.keySet()) {
			BuyPayment buyPayment = payments.get(key);
			buyPayment.setData(key, configPg);

			int taxFreeAmount = 0;

			if (totalTaxFreeAmount > 0) {
				if (!PointUtils.isPointType(key)) {
					taxFreeAmount = totalTaxFreeAmount < buyPayment.getAmount() ? totalTaxFreeAmount : buyPayment.getAmount();
					totalTaxFreeAmount -= taxFreeAmount;
				}
			}

			buyPayment.setTaxFreeAmount(taxFreeAmount);

			// PG사 init이 Buy로 구현된경우 결제 금액을 전달하기위한 용도
			buy.setPgPayAmount(buyPayment.getAmount());
			if ("card".equals(key) || "vbank".equals(key) || "realtimebank".equals(key) || "escrow".equals(key)
				|| "hp".equals(key)) {

				PgData data = new PgData();

				data.setMid(buyPayment.getMid());
				data.setKeypass(buyPayment.getKey());

				data.setOrderCode(buy.getOrderCode());
				data.setAmount(Integer.toString(buyPayment.getAmount()));
				data.setTaxFreeAmount(Integer.toString(buyPayment.getTaxFreeAmount()));
				data.setDeviceType(buy.getDeviceType());
				data.setInstalment(configPg.getInstalment());
				data.setCardCode(configPg.getCardCode());

				String pgType = buyPayment.getServiceType();
				HashMap<String, Object> pgData = null;
				if ("inicis".equals(pgType)) {
					data.setOrderCode(buy.getOrderCode());
					data.setGoodname(productName);
					data.setBuyertel(buyer.getMobile());
					data.setBuyername(buyer.getUserName());
					data.setBuyeremail(buyer.getEmail());
					data.setUserID(Long.toString(UserUtils.getUserId()));
					data.setSessionkey(buy.getSessionId());
					data.setApprovalType(key);

					pgData = inicisService.init(data, session);
				} else if ("lgdacom".equals(pgType)) {

					/**
					 * 결제 수단 코드
					 * SC0010 : 신용카드
					 * SC0030 : 계좌이체
					 * SC0040 : 무통장입금
					 * SC0060 : 휴대폰
					 * SC0070 : 유선전화결제
					 * SC0090 : OK캐쉬백
					 * SC0111 : 문화상품권
					 * SC0112 : 게임문화 상품권
					 */

					data.setLGD_BUYER(buyer.getUserName());
					data.setLGD_BUYEREMAIL(buyer.getEmail());
					data.setLGD_CUSTOM_USABLEPAY(lgDacomService.getPayType(key));
					data.setLGD_PRODUCTINFO(productName);
					data.setApprovalType(key);

					pgData = lgDacomService.init(data, session);
				} else if ("cj".equals(pgType)) {

					if (UserUtils.isUserLogin()) {
						data.setUserID(UserUtils.getLoginId());
					} else {
						data.setUserID("Guest");
					}

					data.setApprovalType(key);
					data.setUserEmail(buyer.getEmail());
					data.setUsername(buyer.getUserName());
					data.setUserPhone(buyer.getMobile());
					data.setGoodname(productName);
					pgData = cjService.init(data, session);
				} else if ("kspay".equals(pgType)) {

					data.setApprovalType(key);
					data.setUserEmail(buyer.getEmail());
					data.setUsername(buyer.getUserName());
					data.setUserPhone(buyer.getMobile().replace("-", ""));
					data.setGoodname(productName);
					data.setOrderCode(buy.getOrderCode());
					pgData = kspayService.init(data, session);
				} else if ("kcp".equals(pgType)) {

					KcpRequest kcpRequest = new KcpRequest();

					// 제품명이 30으로 제한이 걸려있음
					String goodName = StringUtils.strcut(buy.getItems().get(0).getItem().getItemName(), 20);
					if (buy.getItems().size() > 1) {
						goodName += " 외 " + (buy.getItems().size() - 1) + "개";
					}

					kcpRequest.setPay_method(key);
					kcpRequest.setOrdr_idxx(buy.getOrderCode());
					kcpRequest.setGood_name(goodName);
					kcpRequest.setGood_mny(StringUtils.integer2string(orderPrice.getOrderPayAmount()));
					kcpRequest.setBuyr_name(buyer.getUserName());
					kcpRequest.setBuyr_mail(buyer.getEmail());
					kcpRequest.setBuyr_tel1(buyer.getPhone1()+buyer.getPhone2()+buyer.getPhone3());
					kcpRequest.setBuyr_tel2(buyer.getMobile1()+buyer.getMobile2()+buyer.getMobile3());

					data.setKcpRequest(kcpRequest);

					pgData = kcpService.init(data, session);
				} else if("easypay".equals(pgType)) {
					EasypayRequest easypayRequest = new EasypayRequest();

					easypayRequest.setEp_order_no(buy.getOrderCode());
					easypayRequest.setEp_product_nm(productName);
					easypayRequest.setEp_product_amt(Integer.toString(buy.getPgPayAmount()));
					easypayRequest.setEp_user_id(buyer.getLoginId());
					easypayRequest.setEp_memb_user_no(Long.toString(buyer.getUserId()));
					easypayRequest.setEp_user_nm(buyer.getUserName());
					easypayRequest.setEp_user_mail(buyer.getEmail());
					easypayRequest.setEp_user_phone1(buyer.getPhone());
					easypayRequest.setEp_user_phone2(buyer.getMobile());
					easypayRequest.setEp_user_addr(buyer.getAddress());
					easypayRequest.setEp_pay_type(key);

					data.setEasypayRequest(easypayRequest);

					pgData = easypayService.init(data, session);
				} else if("nicepay".equals(pgType)) {

					// 휴대폰 결제일 경우 length 체크 (필드 길이 에러 발생)
					if ("hp".equals(key)) {
						productName = buy.getItems().get(0).getItem().getItemName();
						if (productName.length() > 20) {
							productName = StringUtils.strcut(productName,20);
						}

						if (buy.getItems().size() > 1) {
							productName += " 외 " + (buy.getItems().size() - 1) + "개";
						}
					}

					data.setApprovalType(key);
					data.setGoodsName(productName);
					data.setAmt(Integer.toString(buy.getPgPayAmount()));
					data.setBuyerName(buyer.getUserName());
					data.setBuyerTel(buyer.getMobile());
					data.setBuyerEmail(buyer.getEmail());
					data.setMoid(buy.getOrderCode());
					data.setUserIP(buy.getUserIp());

					HttpServletRequest request = RequestContextUtils.getRequestContext().getRequest();
					String token = "";

					try {
						token = JwtUtils.getToken(request);
					} catch (RuntimeException ignore) {token = "";}

					data.setSalesonId(ShopUtils.getSalesOnIdByHeader(request));
					data.setSalesonToken(token);
					data.setSalesonTokenType(UserUtils.isUserLogin() ? "USER" : "");
					data.setSuccessUrl(buy.getSuccessUrl());
					data.setFailUrl(buy.getFailUrl());

					pgData = nicepayService.init(data, session);
				}

				map.put("pgData", pgData);

			} else if ("payco".equals(key)) {
				map.put("payco", paycoService.init(buy, session));

			} else if ("kakaopay".equals(key)) {

				String requestDealApproveUrl = environment.getProperty("kakaopay.web.path")
					+ environment.getProperty("kakaopay.msg.name");
				String MERCHANT_ID = environment.getProperty("kakaopay.mid");
				String merchantEncKey = environment.getProperty("kakaopay.merchant.enc.key");
				String merchantHashKey = environment.getProperty("kakaopay.merchant.hash.key");
				String certifiedFlag = "CN";

				String PR_TYPE = "WPM";		// 결제 요청 타입 - WPM: WEB결제, MPM: Mobile결제
				String channelType = "4";	// 채널타입 - 2:모바일웹결제 채널, 4: PC TMS 결제 채널

				if (DeviceUtils.MOBILE.equals(buy.getRealDeviceType())) {
					PR_TYPE = "MPM";		// 결제 요청 타입 - WPM: WEB결제, MPM: Mobile결제
					channelType = "2";
				}

				String requestorName = "";	// ???
				String requestorTel = "";	// ???


				String MERCHANT_TXN_NUM = buy.getOrderCode();	// 가맹점 거래번호.(주문번호)
				String PRODUCT_NAME = productName; 	// 상품명
				String AMOUNT = Integer.toString(buyPayment.getAmount());
				String serviceAmt = "0";
				String supplyAmt = "0";
				String goodsVat = "0";
				String CURRENCY = "KRW";
				String RETURN_URL = "";
				String offerPeriod = "";
				String offerPeriodFlag = "N";


				String possiCard = "";
				String fixedInt = "";
				String maxInt = "";
				String noIntYN = "N";
				String noIntOpt = "";
				String pointUseYN = "N";
				String blockCard = "";
				String blockBin = "";

				// 전문 Parameter DTO 객체 생성
				DealApproveDto approveDto = new DealApproveDto();


				// 필수값 SETTING
				approveDto.setRequestDealApproveUrl(requestDealApproveUrl); // 결제요청을 위한 URL
				approveDto.setMerchantEncKey(merchantEncKey); // 가맹점의 EncKey
				approveDto.setMerchantHashKey(merchantHashKey); // 가맹점의 HashKey

				approveDto.setCertifiedFlag(certifiedFlag); // WEB결제로 신청할시에 필수 'CN'
				approveDto.setPrType(PR_TYPE);
				approveDto.setChannelType(channelType); // TMS 및 방판 결제시 필수

				approveDto.setRequestorName(requestorName);
				approveDto.setRequestorTel(requestorTel);

				approveDto.setMerchantID(MERCHANT_ID);
				approveDto.setMerchantTxnNum(MERCHANT_TXN_NUM);

				//approveDto.setProductName(new String(PRODUCT_NAME,"UTF-8"));
				approveDto.setProductName(PRODUCT_NAME);

				approveDto.setAmount(AMOUNT);
				approveDto.setServiceAmt(serviceAmt);
				approveDto.setSupplyAmt(supplyAmt);
				approveDto.setGoodsVat(goodsVat);

				approveDto.setCurrency(CURRENCY);
				approveDto.setReturnUrl(RETURN_URL);

				approveDto.setOfferPeriod(offerPeriod);
				approveDto.setOfferPeriodFlag(offerPeriodFlag);

				approveDto.setPossiCard(possiCard);
				approveDto.setFixedInt(fixedInt);
				approveDto.setMaxInt(maxInt);
				approveDto.setNoIntYN(noIntYN);
				approveDto.setNoIntOpt(noIntOpt);
				approveDto.setPointUseYN(pointUseYN);
				approveDto.setBlockCard(blockCard);

				approveDto.setBlockBin(blockBin);

				map.put("kakaopay", kakaopayService.init(approveDto, session));
            } else if ("naverpay".equals(key)) {
                HashMap<String, Object> pgData = new HashMap<>();
                pgData.put("productName", buy.getItems().get(0).getItem().getItemName());
                pgData.put("totalPayAmount", buy.getPgPayAmount());
                pgData.put("taxScopeAmount", buy.getPgPayAmount());
                pgData.put("taxExScopeAmount", 0);

                List<HashMap<String,Object>> productItems = new ArrayList<HashMap<String,Object>>();
                int productCount = 0;
                for (BuyItem item: buy.getItems()) {
                    if (productItems.size() > 0) {

                        boolean isDuplicateItem = false;
                        int duplicateIndex = 0;

                        for(int i=0;i<productItems.size();i++) {
                            String itemCode = productItems.get(i).get("uid").toString();

                            if (itemCode.equals(item.getItemUserCode())) {
                                duplicateIndex = i;
                                isDuplicateItem = true;
                            }
                        }

                        if (isDuplicateItem) {
                            HashMap<String,Object> productItem = productItems.get(duplicateIndex);
                            productItem.put("count", (int)productItem.get("count") + item.getItemPrice().getQuantity());
                            productCount += item.getItemPrice().getQuantity();
                        } else {
                            HashMap<String,Object> newProductItem = new HashMap<String,Object>();
                            newProductItem.put("name", item.getItemName());
                            newProductItem.put("count", item.getItemPrice().getQuantity());
                            newProductItem.put("categoryType", "PRODUCT");
                            newProductItem.put("categoryId", "GENERAL");
                            newProductItem.put("uid", item.getItemUserCode());
                            productItems.add(newProductItem);
                            productCount += item.getItemPrice().getQuantity();
                        }
                    } else {
                        HashMap<String,Object> newProductItem = new HashMap<String,Object>();
                        newProductItem.put("name", item.getItemName());
                        newProductItem.put("count", item.getItemPrice().getQuantity());
                        newProductItem.put("categoryType", "PRODUCT");
                        newProductItem.put("categoryId", "GENERAL");
                        newProductItem.put("uid", item.getItemUserCode());
                        productItems.add(newProductItem);
                        productCount += item.getItemPrice().getQuantity();
                    }

                }
                pgData.put("productCount", productCount);
                pgData.put("productItems", productItems);

                map.put("naverpay", pgData);
            }


			buyPayment.setOrderCode(buy.getOrderCode());
            buyPayment.setCreatedDate(createdDate);

			orderMapper.insertOrderPaymentBuyTemp(buyPayment);

			savePaymentType.add(key);
		}

		map.put("savePaymentType", savePaymentType);

		try {
		    // 영수증 신청시 필요한 사업자등록번호, 휴대전화번호등을 조합
            String cashbillCode = "010-000-1234";

            if (CashbillType.BUSINESS == buy.getCashbill().getCashbillType()) {
                cashbillCode = buy.getCashbill().getCashbillCode();
            } else if (CashbillType.PERSONAL == buy.getCashbill().getCashbillType()) {
                cashbillCode = StringUtils.phoneNumberPattern(buy.getCashbill().getCashbillCode());
            }

            buy.getCashbill().setCashbillCode(cashbillCode);
            buy.setCreatedDate(createdDate);

			orderMapper.insertOrderTemp(buy);
		} catch (RuntimeException e) {
//			log.error(ERROR_MARKER, e.getMessage(), e);
			log.error(ERROR_MARKER, getClass().getName() + " :: saveOrderTemp RuntimeException =============", e);

			String errorMessage = "주문처리에 실패 했습니다. 잠시 후 다시 시도해 주십시오. - ETC";

			if (e instanceof DuplicateKeyException) {
				errorMessage = "주문처리가 잠시 지연되고 있습니다. 잠시 후 다시 시도해 주십시오. - OrderCode";
			}

			throw new OrderException(errorMessage, e);

		}

		// 배송비 쿠폰 사용 임시 저장
		if (!insertShippingCoupons.isEmpty()) {
			for(ShippingCoupon sCoupon : insertShippingCoupons) {
				sCoupon.setOrderCode(buy.getOrderCode());
				orderMapper.insertOrderShippingCouponBuyTemp(sCoupon);
			}
		}

		for (Receiver receiver : buy.getReceivers()) {

			receiver.setUserId(buy.getUserId());
			receiver.setOrderCode(buy.getOrderCode());
			receiver.setSessionId(buy.getSessionId());
            receiver.setCreatedDate(createdDate);

			receiver.processHyphen();
			orderMapper.insertOrderShippingBuyTemp(receiver);

			// 주문상품 복사
			for (BuyItem buyItem : receiver.getItems()) {
				buyItem.setOrderCode(buy.getOrderCode());
				buyItem.setShippingIndex(receiver.getShippingIndex());
				buyItem.setCampaignCode(buy.getCampaignCode());
				buyItem.setCreatedDate(createdDate);

				if (!escrowStatus.equals("N")) {
					orderMapper.insertOrderItemBuyTempForEscrow(buyItem);
				} else {
					orderMapper.insertOrderItemBuyTemp(buyItem);

					// 세트상품이 있는 경우
					if (buyItem.getItemSets() != null && !buyItem.getItemSets().isEmpty()) {
						int setItemSequence = 0;
						for (BuyItem buyItemSet : buyItem.getItemSets()) {
							buyItemSet.setOrderCode(buy.getOrderCode());
							buyItemSet.setItemSequence(setItemSequence++);
							orderMapper.insertOrderItemSetBuyTemp(buyItemSet);
						}
					}
				}
			}
		}

		// 주문자 정보 기본정보로 저장 체크시 2017-05-18 yulsun.yoo - 사용자 정보 변경하는 부분이라 주석처리(24.01.10)
//		if ("1".equals(buy.getDefaultBuyerCheck())) {
//			buy.getBuyer().setUserId(buy.getUserId());
//			userService.updateUserDetailForOrder(buy.getBuyer());
//		}

		return map;
	}

	@Override
	public Buy getOrderCouponData(Buy buy) {

		if (!UserUtils.isUserLogin()) {
			return null;
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(UserUtils.getUserId());
		orderParam.setViewTarget(buy.getDeviceType());

		List<BuyItem> list = this.getOrderItemTempList(orderParam);

		if (list == null) {
			return null;
		}

		int shippingIndex = 0;
		for(Receiver receiver : buy.getReceivers()) {

			receiver.setShippingIndex(shippingIndex++);

			List<BuyItem> items = new ArrayList<>();

			for(BuyQuantity buyQuantity : receiver.getBuyQuantitys()) {

				for(BuyItem buyItem : list) {
					if (buyQuantity.getItemSequence() == buyItem.getItemSequence()) {

						// 복사해야됨...ㅠㅠ
						BuyItem cloneObject;

						try {

							int itemQuantity = buyQuantity.getQuantity();

							cloneObject = (BuyItem) buyItem.clone();
							cloneObject.getItemPrice().setQuantity(itemQuantity);

							// 2017.03.07 youngki.kim 쿠폰 적용이 해당 배송지 상품의 수량 대로 되도록 수정
							List<OrderCoupon> orderCouponList = new ArrayList<>();

							List<OrderCoupon> cloneObjectItemCouponList = cloneObject.getItemCoupons();

							if (ValidationUtils.isNotNull(cloneObjectItemCouponList) && !cloneObjectItemCouponList.isEmpty()) {

								for (OrderCoupon coupon : cloneObjectItemCouponList) {

									// 2: 구매 수량만큼 할인

									if ("2".equals(coupon.getCouponConcurrently())) {
										coupon.setDiscountAmount(coupon.getDiscountPrice() * itemQuantity);
									}

									orderCouponList.add(coupon);
								}

								cloneObject.setItemCoupons(orderCouponList);

							} else {
								cloneObject.setItemCoupons(cloneObjectItemCouponList);
							}

							items.add(cloneObject);

						} catch (CloneNotSupportedException e) {
							throw new OrderException(e.getMessage(), e);
						}

						break;
					}
				}

			}

			// 상품쿠폰 적용
			receiver.itemCouponUsed(false, buy, receiver.getShippingIndex());

			receiver.setItems(items);

			receiver.setShipping("");
		}

		buy.setOrderPrice(0, configService.getShopConfig(Config.SHOP_CONFIG_ID));

		return buy;
	}

	@Override
	public String getNewOrderCode(OrderCodePrefix orderCodePrefix) {
		String prefix = orderCodePrefix.getCode();
//		int orderCodeId = sequenceService.getId("OP_ORDER_CODE");
		long orderCodeId = orderMapper.getOrderCodeNum();
		return prefix + String.format("%010d", orderCodeId);
	}

	@Override
	public String insertOrder(OrderParam orderParam, Object pgData, HttpSession session, HttpServletRequest request) {

		if (pgData != null) {
			if (!(pgData instanceof PgData || pgData instanceof ReservationResponse || pgData instanceof CjResult)) {
				throw new OrderException("다른 객체가 들어옴");
			}
		}

		// PG 결제
		ConfigPg configPg = configPgService.getConfigPg();
		String pgType = "";
		String autoCashReceipt = "";

		if (configPg != null) {
			pgType = configPg.getPgType().getCode().toLowerCase();
			autoCashReceipt = configPg.isUseAutoCashReceipt() ? "Y" : "N";
		} else {
			pgType = SalesonProperty.getPgService();
			autoCashReceipt = environment.getProperty("pg.autoCashReceipt");
		}

		if (ObjectUtils.isEmpty(orderParam.getOrderCode())){
			if ("easypay".equals(pgType)) {
				orderParam.setOrderCode(request.getParameter("sp_order_no"));
			} else if ("nicepay".equals(pgType)) {
				orderParam.setOrderCode(request.getParameter("Moid"));
			}
		}

		Buy buy = this.getOrderTemp(orderParam);

		if (buy == null) {
			throw new OrderException(MessageUtils.getMessage("M00481"), "/");
		}

		List<BuyPayment> payments = orderMapper.getOrderPaymentBuyTempList(orderParam);
		if (payments == null) {
			throw new OrderException(MessageUtils.getMessage("M00481"), "/");
		}

		String orderCode = buy.getOrderCode();

		String escrowStatus = orderMapper.getOrderItemTempByEscrow(orderCode);

		int orderSequence = 0;

		OrderPrice orderPrice = buy.getOrderPrice();

		// 10 : 결제 완료, 0 : 입금대기
		String orderStatus = "10";
		boolean isBankPayment = false;
		boolean isPointPayment = false;
		boolean isNaverpay = false;

		int bankPayAmount = 0; // 미수금
		for(BuyPayment buyPayment : payments) {

			if ("bank".equals(buyPayment.getApprovalType())
				|| "ourvbank".equals(buyPayment.getApprovalType())
				|| "offlinepay".equals(buyPayment.getApprovalType())
				|| "ars".equals(buyPayment.getApprovalType())
				|| "vbank".equals(buyPayment.getApprovalType())) {

				orderStatus = "0";
				isBankPayment = true;
			}

			if ("bank".equals(buyPayment.getApprovalType())
				|| "ourvbank".equals(buyPayment.getApprovalType())
				|| "ars".equals(buyPayment.getApprovalType())
				|| "vbank".equals(buyPayment.getApprovalType())) {

				bankPayAmount += buyPayment.getAmount();
			}

			if ("point".equals(buyPayment.getApprovalType())) {
				isPointPayment = true;
			}

			if ("naverpay".equals(buyPayment.getApprovalType())) {
				isNaverpay = true;
			}
		}

		// 초기 주문상태 Set
		buy.setOrderStatus(orderStatus);

		// OP_ORDER_TEMP에 저장된 결제 금액 정보
		OrderPrice saveOrderPrice = this.newOrderPrice(orderPrice);

		// 주문 요청 상품들을 조회
		orderParam.setOrderCode(orderCode);

		List<Receiver> list = orderMapper.getOrderShippingBuyTempList(orderParam);
		if (ValidationUtils.isNull(list)) {
			throw new OrderException("주문 가능 상품이 없습니다.", "/cart");
		}

		for (Receiver receiver : list) {
			orderParam.setShippingIndex(receiver.getShippingIndex());
			List<BuyItem> buyItems = orderMapper.getOrderBuyItemTempList(orderParam);

			if (buyItems.isEmpty()) {
				throw new OrderException("주문 가능 상품이 없습니다.", "/cart");
			}

			// 입점 업체 수수료 일때 판매자 정보에 설정된 수수료를 조회해서 셋팅
			for (BuyItem buyItem : buyItems) {
				Item item = buyItem.getItem();
				if ("1".equals(item.getCommissionType())) {
					Seller seller = sellerMapper.getSellerById(item.getSellerId());
					if (seller != null) {
						item.setCommissionRate(seller.getCommissionRate());
					}
				}

				// 세트상품 조회 세팅
				if ("Y".equals(buyItem.getSetItemFlag())) {
					orderParam.setSetItemSequence(buyItem.getItemSequence());
					buyItem.setItemSets(orderMapper.getOrderBuyItemSetTempList(orderParam));
				}
			}

			receiver.setItems(buyItems);
		}

		buy.setReceivers(list);

		HashMap<String, Integer> buyQuantityMap = new HashMap<>();
		HashMap<String, Integer> buySetQuantityMap = new HashMap<>();
		setOrderItemInfo(buy.getItems(), orderParam, buyQuantityMap, buySetQuantityMap);

		if (buy.getReceivers() != null) {
			for (Receiver receiver : buy.getReceivers()) {

				// 상품쿠폰 적용
				receiver.itemCouponUsed(false, buy, receiver.getShippingIndex());

				// 구매 상품 정책별 그룹
				String zipcode = receiver.getReceiveZipcode();

				if (ObjectUtils.isEmpty(zipcode)) {
					zipcode = receiver.getReceiveZipcode();
				}

				receiver.setShipping(orderMapper.getIslandTypeByZipcode(zipcode));
			}
		}

		// 재고 차감 목록
		buy.setStockMap(buyQuantityMap);

		// 세트 재고 차감 목록
		buy.setStockSetMap(buySetQuantityMap);

		// 구매가능여부 채크
		ShopUtils.buyVerification(buy.getItems(), buy.getItems().size());

		// 사용한 포인트
		orderPrice.setTotalPointDiscountAmount(saveOrderPrice.getTotalPointDiscountAmount());

		// 배송비 할인쿠폰 적용
		if (UserUtils.isUserLogin() == true) {

			int shippingCouponCount = 0;
			int totalShippingCouponDiscountAmount = 0;
			if (buy.getReceivers().size() <= 1) {
				List<ShippingCoupon> shippingCoupons = orderMapper.getOrderShippingCouponBuyTemp(orderParam);
				if (!shippingCoupons.isEmpty()) {
					List<Shipping> shippings = buy.getReceivers().get(0).getItemGroups();

					for(ShippingCoupon sCoupon : shippingCoupons) {
						for(Shipping shipping : shippings) {
							if (shipping.getRealShipping() == sCoupon.getDiscountAmount()
								&& shipping.getShippingGroupCode().equals(sCoupon.getShippingGroupCode())) {

								shipping.setShippingCouponCount(1);
								shipping.setDiscountShipping(shipping.getRealShipping() - shipping.getAddDeliveryCharge());
								shipping.setPayShipping(shipping.getRealShipping() - shipping.getDiscountShipping());

								shippingCouponCount += shipping.getShippingCouponCount();
								totalShippingCouponDiscountAmount += shipping.getDiscountShipping();
								break;
							}
						}
					}
				}
			}

			buy.setShippingCoupon(shippingCouponCount);
			saveOrderPrice.setTotalShippingCouponUseCount(shippingCouponCount);
			saveOrderPrice.setTotalShippingCouponDiscountAmount(totalShippingCouponDiscountAmount);
		}

		buy.setOrderPrice(bankPayAmount, configService.getShopConfig(Config.SHOP_CONFIG_ID));

		try {
			// 결제금액 검증
			this.payAmountVerification(buy, saveOrderPrice);
		} catch (OrderException oe) {
			throw new OrderException(oe.getErrorMessage(), oe.getRedirectUrl(), oe);
		}

		List<OrderPgData> successOrderPgDatas = new ArrayList<>();

		try {

			Buyer buyer = buy.getBuyer();
			buyer.setIp(saleson.common.utils.CommonUtils.getClientIp(request));
			buyer.setOrderCode(orderCode);
			buyer.setUserId(buy.getUserId());
			if (UserUtils.isUserLogin()) {
				buyer.setLoginId(UserUtils.getLoginId());
			}

			buyer.setOrderPrice(orderPrice);

			// 데이터 암호화
			buyer.encrypt(buyerEncryptor);
			orderMapper.insertOrder(buyer);

			int shippingInfoSequence = 0;
			int shippingSequence = 0;
			int itemSequence = 0;
			orderSequence = buyer.getOrderSequence();

			for(Receiver receiver : buy.getReceivers()) {

				// 주문상품 배송지 정보를 저장
				OrderShippingInfo orderShippingInfo = new OrderShippingInfo(orderCode, orderSequence, shippingInfoSequence++, receiver);

				// 데이터 암호화
				orderShippingInfo.encrypt(orderShippingInfoEncryptor);
				orderMapper.insertOrderShippingInfo(orderShippingInfo);

				for(Shipping shipping : receiver.getItemGroups()) {
					shipping.setOrderCode(orderCode);
					shipping.setOrderSequence(orderSequence);
					shipping.setShippingSequence(shippingSequence++);

					orderMapper.insertOrderShipping(shipping);

					// 1개상품용 쿠폰일 경우 메일링에 해당 상품 분리적용으로 인해 buyItems 생성
					List<BuyItem> buyItems = new ArrayList<>();

					if (shipping.isSingleShipping()) {
						BuyItem buyItem = shipping.getBuyItem();

						// 묶음배송 처리 데이터 추가
						if (!ObjectUtils.isEmpty(shipping.getShipmentGroupCode())) {
							buyItem.setShipmentGroupCode(shipping.getShipmentGroupCode());
						}

						// 상품 정보 세팅
						this.setOrderItemForBuy(itemSequence, buyItems, buyItem, buy, shipping, orderShippingInfo, escrowStatus);

						// 네이버페이로 결제할 경우 포인트 적립 안함.
						if (isNaverpay) {
							buyItem.getItemPrice().setEarnPoint(0);
							buyItem.getItemPrice().setSellerPoint(0);
						}
					} else {
						for (BuyItem buyItem : shipping.getBuyItems()) {

							// 묶음배송 처리 데이터 추가
							buyItem.setShipmentGroupCode(shipping.getShipmentGroupCode());

							// 상품 정보 세팅
							this.setOrderItemForBuy(itemSequence + buyItems.size(), buyItems, buyItem, buy, shipping, orderShippingInfo, escrowStatus);

							// 네이버페이로 결제할 경우 포인트 적립 안함.
							if (isNaverpay) {
								buyItem.getItemPrice().setEarnPoint(0);
								buyItem.getItemPrice().setSellerPoint(0);
							}
						}
					}

					itemSequence += buyItems.size();

					this.insertOrderItem(buyItems);
					shipping.setBuyItems(buyItems);
				}
			}

			int paymentSequence = 0;

			buy.setPayments(payments);

			int cnt = 1;
			int totCnt = payments.size();

			for (BuyPayment buyPayment : payments) {
				OrderPgData orderPgData = null;

				String approvalType = buyPayment.getApprovalType();

				OrderPayment orderPayment = new OrderPayment();
				orderPayment.setOrderCode(orderCode);
				orderPayment.setOrderSequence(orderSequence);
				orderPayment.setPaymentSequence(paymentSequence++);
				if ("bank".equals(approvalType)) {

					orderPayment.setBankVirtualNo(buyPayment.getBankVirtualNo());
					orderPayment.setBankInName(buyPayment.getBankInName());
					orderPayment.setBankDate(buyPayment.getBankExpirationDate());
					orderPayment.setNowPaymentFlag("N");

                    // 세금계산서, 현금영수증 데이터 저장
                    receiptDataSave(buy, totCnt, cnt);
				} else if (PointUtils.isPointType(approvalType)) {
                    // 현금성 포인트일 경우 포인트 금액만큼 현금영수증 발급
					if(PointUtils.isPossibleToIssueReceipt(approvalType)){
                        receiptDataSave(buy, totCnt, cnt);
					}

					orderPayment.setOrderPgDataId(0);
					orderPayment.setApprovalType(approvalType);
					orderPayment.setPayDate(DateUtils.getToday(DATETIME_FORMAT));
					orderPayment.setAmount(buyPayment.getAmount());
					orderPayment.setTaxFreeAmount(buyPayment.getAmount());
					orderPayment.setNowPaymentFlag("Y");

					PointUsed pointUsed = new PointUsed();
					pointUsed.setOrderCode(orderCode);
					pointUsed.setPoint(buyPayment.getAmount());
					pointUsed.setDetails("주문번호 :" + orderCode + " 사용");

					// 포인트 사용 처리
					pointService.deductedPoint(pointUsed, buy.getUserId(), approvalType);

				} else {

					if ("card".equals(approvalType) || "vbank".equals(approvalType) || "realtimebank".equals(approvalType)
						|| "escrow".equals(approvalType) || "hp".equals(approvalType)) {
						if ("inicis".equals(pgType)) {

							((PgData) pgData).setMid(buyPayment.getMid());
							((PgData) pgData).setKeypass(buyPayment.getKey());

							/**
							 * 모바일 이니시스 결제 요청후 승인 타입
							 * 신용카드 : wcard
							 * 휴대폰 : mobile
							 * 문화상품권 : culture
							 * 해피머니상품권 : hpmn
							 * 스마트문상 : dgcl
							 *
							 * ...... vbank도 nextUrl을 사용하내...
							 */
							if ((ShopUtils.isMobilePage() || ((PgData) pgData).isMobilePage())
								&& ("realtimebank".equals(approvalType) || "card".equals(approvalType) || "vbank".equals(approvalType) || "escrow".equals(approvalType) || "hp".equals(approvalType))) {
								((PgData) pgData).setTransactionType("1");
							} else if (!ShopUtils.isMobilePage() && "webStandard".equals(environment.getProperty("pg.inipay.web.type"))) {
								((PgData) pgData).setTransactionType("1");
							}

							((PgData) pgData).setTaxFreeAmount(Integer.toString(buyPayment.getTaxFreeAmount()));
							((PgData) pgData).setAmount(Integer.toString(buyPayment.getAmount()));
							((PgData) pgData).setApprovalType(approvalType);

							orderPgData = inicisService.pay(pgData, session);

							if ("vbank".equals(approvalType) || "escrow".equals(approvalType)) {
								String bankVirtualNo = ShopUtils.getBankName(pgType, orderPgData.getBankCode()) + " 계좌번호 : ";
								bankVirtualNo += orderPgData.getBankVirtualNo();
								orderPayment.setBankVirtualNo(bankVirtualNo);
								orderPayment.setBankInName(orderPgData.getBankInName());

								String closeDate = orderPgData.getBankDate();
								if (StringUtils.isNotEmpty(closeDate)) {
									if (closeDate.length() == 14) {
										closeDate = closeDate.substring(0, 8);
									}

									if (closeDate.length() != 8) {
										closeDate = "";
									}
								}

								// 만료일이 없으면 5일만 더해서 보여주자..
								if (ObjectUtils.isEmpty(closeDate)) {
									closeDate = DateUtils.addDay(DateUtils.getToday(DATE_FORMAT), 5);
								}

								orderPayment.setBankDate(closeDate);

								// 입금 요청 메일 전송용 데이터 구성
								buyPayment.setBankVirtualNo(bankVirtualNo);
								buyPayment.setBankExpirationDate(DateUtils.date(closeDate));
								buyPayment.setBankInName(orderPgData.getBankInName());
							}

							if ("vbank".equals(approvalType) || "realtimebank".equals(approvalType)) {
                                receiptDataSave(buy, totCnt, cnt);
							}

						} else if ("lgdacom".equals(pgType)) {
							((PgData) pgData).setAmount(Integer.toString(buyPayment.getAmount()));
							((PgData) pgData).setApprovalType(approvalType);
							orderPgData = lgDacomService.pay(pgData, session);
							orderPgData.setPgPaymentType(lgDacomService.getPayType(approvalType));

							// 에스크로 flag 처리
							if ("Y".equals(orderPgData.getEscrowStatus())) {
								orderParam.setEscrowStatus("Y");
								orderMapper.updateEscrowStatus(orderParam);
							}

							if ("vbank".equals(approvalType)) {
								String bankVirtualNo = orderPgData.getBankName() + " 계좌번호 : ";
								bankVirtualNo += orderPgData.getBankVirtualNo();
								orderPayment.setBankVirtualNo(bankVirtualNo);
								orderPayment.setBankInName(orderPgData.getBankInName());

								String closeDate = orderPgData.getBankDate();
								orderPayment.setBankDate(closeDate);

								// 입금 요청 메일 전송용 데이터 구성
								buyPayment.setBankVirtualNo(bankVirtualNo);
								buyPayment.setBankExpirationDate(DateUtils.date(closeDate));
								buyPayment.setBankInName(orderPgData.getBankInName());
							}


						} else if ("cj".equals(pgType)) {
							orderPgData = cjService.pay(pgData, session);

							if ("vbank".equals(approvalType)) {

								String bankVirtualNo = "[" + ((CjResult) pgData).getCJSBankName() + "] 계좌번호 : ";
								bankVirtualNo += ((CjResult) pgData).getCJSAccountNo();
								orderPayment.setBankVirtualNo(bankVirtualNo);
								orderPayment.setBankInName(((CjResult) pgData).getCJSAccountOWNER());

								String closeDate = ((CjResult) pgData).getCloseDate();
								if (StringUtils.isNotEmpty(closeDate)) {
									if (closeDate.length() == 14) {
										closeDate = closeDate.substring(0, 8);
									}

									if (closeDate.length() != 8) {
										closeDate = "";
									}
								}

								// 만료일이 없으면 5일만 더해서 보여주자..
								if (ObjectUtils.isEmpty(closeDate)) {
									closeDate = DateUtils.addDay(DateUtils.getToday(DATE_FORMAT), 5);
								}

								orderPayment.setBankDate(closeDate);
							}
						} else if ("kspay".equals(pgType)) {
							((PgData) pgData).setAmount(Integer.toString(buyPayment.getAmount()));
							((PgData) pgData).setApprovalType(approvalType);
							orderPgData = kspayService.pay(pgData, session);
							orderPgData.setPgPaymentType(((PgData) pgData).getSndPaymethod());

						} else if ("kcp".equals(pgType)) {
							KcpRequest kcpRequest = new KcpRequest(request);
							kcpRequest.setPay_method(approvalType);
							orderPgData = kcpService.pay(kcpRequest, session);

							if ("vbank".equals(approvalType) || "escrow".equals(approvalType)) {
								String bankVirtualNo = "[" + orderPgData.getBankName() + "]" + " 계좌번호 : ";
								bankVirtualNo += orderPgData.getBankVirtualNo();
								orderPayment.setBankVirtualNo(bankVirtualNo);
								orderPayment.setBankInName(orderPgData.getBankInName());

								String closeDate = orderPgData.getBankDate();
								if (StringUtils.isNotEmpty(closeDate)) {
									if (closeDate.length() == 14) {
										closeDate = closeDate.substring(0, 8);
									}

									if (closeDate.length() != 8) {
										closeDate = "";
									}
								}

								// 만료일이 없으면 5일만 더해서 보여주자..
								if (ObjectUtils.isEmpty(closeDate)) {
									closeDate = DateUtils.addDay(DateUtils.getToday(DATE_FORMAT), 5);
								}

								orderPayment.setBankDate(closeDate);

								// 입금 요청 메일 전송용 데이터 구성
								buyPayment.setBankVirtualNo(bankVirtualNo);
								buyPayment.setBankExpirationDate(DateUtils.date(closeDate));
								buyPayment.setBankInName(orderPgData.getBankInName());
							}
						} else if("easypay".equals(pgType)) {
							EasypayRequest easypayRequest = new EasypayRequest(request);
							((PgData)pgData).setEasypayRequest(easypayRequest);

							orderPgData = easypayService.pay(pgData, session);

							if ("vbank".equals(approvalType) || "escrow".equals(approvalType)) {
								String bankVirtualNo = "[" + orderPgData.getBankName() + "]" + " 계좌번호 : ";
								bankVirtualNo += orderPgData.getBankVirtualNo();
								orderPayment.setBankVirtualNo(bankVirtualNo);
								orderPayment.setBankInName(orderPgData.getBankInName());

								String closeDate = orderPgData.getBankDate();
								if (StringUtils.isNotEmpty(closeDate)) {
									if (closeDate.length() == 14) {
										closeDate = closeDate.substring(0, 8);
									}

									if (closeDate.length() != 8) {
										closeDate = "";
									}
								}

								// 만료일이 없으면 5일만 더해서 보여주자..
								if (ObjectUtils.isEmpty(closeDate)) {
									closeDate = DateUtils.addDay(DateUtils.getToday(DATE_FORMAT), 5);
								}

								orderPayment.setBankDate(closeDate);

								// 입금 요청 메일 전송용 데이터 구성
								buyPayment.setBankVirtualNo(bankVirtualNo);
								buyPayment.setBankExpirationDate(DateUtils.date(closeDate));
								buyPayment.setBankInName(orderPgData.getBankInName());
							}
						} else if("nicepay".equals(pgType)) {
							((PgData) pgData).setApprovalType(approvalType);
							orderPgData = nicepayService.pay(pgData, session);

							if (!orderPgData.isSuccess()) {
								throw new OrderException(orderPgData.getErrorMessage(), "/order/step1");
							}

							if ("card".equals(approvalType)) {
								orderPayment.setCardEasyType(orderPgData.getCardEasyType());
							}

							if ("vbank".equals(approvalType) || "escrow".equals(approvalType)) {
								String bankVirtualNo = ShopUtils.getBankName(pgType, orderPgData.getBankCode()) + " 계좌번호 : ";
								bankVirtualNo += orderPgData.getBankVirtualNo();
								orderPayment.setBankVirtualNo(bankVirtualNo);
								orderPayment.setBankInName(orderPgData.getBankInName());

								String closeDate = orderPgData.getBankDate();
								if (StringUtils.isNotEmpty(closeDate)) {
									if (closeDate.length() == 14) {
										closeDate = closeDate.substring(0, 8);
									}

									if (closeDate.length() != 8) {
										closeDate = "";
									}
								}

								// 만료일이 없으면 5일만 더해서 보여주자..
								if (ObjectUtils.isEmpty(closeDate)) {
									closeDate = DateUtils.addDay(DateUtils.getToday(DATE_FORMAT), 5);
								}

								orderPayment.setBankDate(closeDate);

								// 입금 요청 메일 전송용 데이터 구성
								buyPayment.setBankVirtualNo(bankVirtualNo);
								buyPayment.setBankExpirationDate(DateUtils.date(closeDate));
								buyPayment.setBankInName(orderPgData.getBankInName());

								// 가상계좌 환불 서비스에 가입 되어있으면 부분취소 가능.
								String useVbankRefundService = "";
								if (configPg != null) {
									useVbankRefundService = configPg.isUseVbackRefundService() ? "Y" : "N";
								} else {
									useVbankRefundService = environment.getProperty("pg.useVbank.refundService");
								}
								orderPgData.setPartCancelFlag(useVbankRefundService);
							}

							if ("vbank".equals(approvalType) || "realtimebank".equals(approvalType)) {
								receiptDataSave(buy, totCnt, cnt);
							}

						}

						if (!orderPgData.isSuccess() && !"RECOVERY".equals(orderParam.getPayMode())) {
							throw new OrderException(orderPgData.getErrorMessage(), "/order/step1");
						}

						successOrderPgDatas.add(orderPgData);

						// PG 서비스 타입
                        orderPgData.setOrderCode(buy.getOrderCode());
						orderPgData.setPgServiceType(pgType);
						orderPgData.setOrderPgDataId(sequenceService.getId("OP_ORDER_PG_DATA"));
						orderPgData.setOrderCode(orderCode);
						orderPgData.setPgAmount(buyPayment.getAmount());
						orderPgData.setPgServiceMid(buyPayment.getMid());
						orderPgData.setPgServiceKey(buyPayment.getKey());

						orderMapper.insertOrderPgData(orderPgData);

						orderPayment.setOrderPgDataId(orderPgData.getOrderPgDataId());

						orderPayment.setNowPaymentFlag("Y");
						if ("vbank".equals(approvalType)
							|| ("inicis".equals(pgType) && "escrow".equals(approvalType))) {

							orderPayment.setNowPaymentFlag("N");
						}


					} else if ("payco".equals(approvalType)) {
						orderPgData = paycoService.pay(pgData, session);

						if (!orderPgData.isSuccess()) {
							throw new OrderException(orderPgData.getErrorMessage(), "/order/step1");
						}

						// PAYCO 결제의 경우 0으로 떨어져도 무통장 입금은 아닐수도 있는데....
						// 결제 상태가 10인경우에만 체크하도록 하자.
						if ("10".equals(orderStatus)) {
							orderStatus = orderPgData.isPaymentCompletion() ? "10" : "0";
						}

						orderPayment.setNowPaymentFlag("N");
						if ("10".equals(orderStatus)) {

							orderPayment.setNowPaymentFlag("Y");
						}

						// PG 서비스 타입
                        orderPgData.setOrderCode(buy.getOrderCode());
						orderPgData.setPgServiceType(approvalType);
						orderPgData.setOrderPgDataId(sequenceService.getId("OP_ORDER_PG_DATA"));
						orderMapper.insertOrderPgData(orderPgData);

						// 혹시모를 주문취소를 위한 주문취소 금액 셋팅해 놓도록 하자
						orderPgData.setRemainAmount(buyPayment.getAmount());
						successOrderPgDatas.add(orderPgData);

						orderPayment.setOrderPgDataId(orderPgData.getOrderPgDataId());

					} else if ("kakaopay".equals(approvalType)) {
						orderPgData = kakaopayService.pay(pgData, session);

						if (!orderPgData.isSuccess()) {
							throw new OrderException(orderPgData.getErrorMessage(), "/order/step1");
						}

						// 즉시결제 여부
						orderPayment.setNowPaymentFlag("Y");

						// PG 서비스 타입
                        orderPgData.setOrderCode(buy.getOrderCode());
						orderPgData.setPgServiceType(approvalType);
						orderPgData.setOrderPgDataId(sequenceService.getId("OP_ORDER_PG_DATA"));
						orderMapper.insertOrderPgData(orderPgData);

						// 혹시모를 주문취소를 위한 주문취소 금액 셋팅해 놓도록 하자
						orderPgData.setRemainAmount(buyPayment.getAmount());
						successOrderPgDatas.add(orderPgData);

						orderPayment.setOrderPgDataId(orderPgData.getOrderPgDataId());

                    } else if ("naverpay".equals(approvalType)) {
                        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<>();
                        requestMap.add("paymentId", ((PgData) pgData).getPaymentId());
                        requestMap.add("amount", ((PgData) pgData).getAmount());

                        orderPgData = naverPaymentApi.pay(requestMap, configPg);
                        if (!orderPgData.isSuccess()) {
                            throw new OrderException(orderPgData.getErrorMessage(), "/order/step1");
                        }

                        successOrderPgDatas.add(orderPgData);

                        if (orderPgData.isAmountModification()) {
                            throw new OrderException("주문금액과 결제금액이 일치하지 않습니다.", "/order/step1");
                        }

                        // PG 서비스 타입
                        orderPgData.setPgServiceType("naverpay");
                        orderPgData.setOrderPgDataId(sequenceService.getId("OP_ORDER_PG_DATA"));

                        orderMapper.insertOrderPgData(orderPgData);

                        // 즉시결제 여부
                        orderPayment.setNowPaymentFlag("Y");
                        orderPayment.setOrderPgDataId(orderPgData.getOrderPgDataId());
                    }
				}

				orderPayment.setApprovalType(approvalType);
				orderPayment.setAmount(buyPayment.getAmount());
				orderPayment.setTaxFreeAmount(buyPayment.getTaxFreeAmount());

				if ("Y".equals(orderPayment.getNowPaymentFlag())) {
					orderPayment.setRemainingAmount(buyPayment.getAmount());
					orderPayment.setPayDate(DateUtils.getToday(DATETIME_FORMAT));
				}

				orderPayment.setPaymentType("1");
				orderPayment.setDeviceType(buy.getDeviceType());


				// 데이터 암호화
				orderPayment.encrypt(orderPaymentEncryptor);
				orderMapper.insertOrderPayment(orderPayment);

                // 현금영수증 발행 - 현금영수증 발행 가능한 포인트 및 실시간 계좌이체만 (은행입금, 가상계좌는 입금이 확인되는 시점에 발행)
				// orderPgData.isCashReceiptIssued() -> 계좌이체 현금영수증 발행여부 확인값 (중복 발급 방지), 나이스페이 외 다른 PG 사용시 pay 로직내 해당 값 세팅 필요
				if (orderPgData != null && !orderPgData.isCashReceiptIssued()
						&& (PointUtils.isPossibleToIssueReceipt(approvalType) || "realtimebank".equals(approvalType))) {
                    CashbillParam cashbillParam = new CashbillParam();

                    cashbillParam.setWhere("orderCode");
                    cashbillParam.setQuery(orderParam.getOrderCode());

                    Iterable<CashbillIssue> cashbillIssues = cashbillIssueRepository.findAll(cashbillParam.getPredicate());

                    log.debug("[CASHBILL] START ---------------------------------------------");
                    log.debug("[CASHBILL] cashbillIssues Size :  {}", ((List<CashbillIssue>) cashbillIssues).size());

                    CashbillResponse response = null;
					String cashbillService = "";

					if (configPg != null) {
						cashbillService = configPg.getCashbillServiceType().getCode().toLowerCase();
					} else {
						cashbillService = environment.getProperty("cashbill.service");
					}

                    for (CashbillIssue cashbillIssue : cashbillIssues) {

                        if ("popbill".equals(cashbillService)) {
                            response = receiptService.receiptIssue(cashbillIssue);
                        } else if ("inicis".equals(cashbillService)) {
                            Cashbill cashbill = cashbillIssue.getCashbill();

                            cashbillParam.setCashbillStatus(cashbillIssue.getCashbillStatus());
                            cashbillParam.setAmount(cashbillIssue.getAmount());
                            cashbillParam.setItemName(cashbillIssue.getItemName());
                            cashbillParam.setTaxType(cashbillIssue.getTaxType());
                            cashbillParam.setCashbillCode(cashbill.getCashbillCode());
                            cashbillParam.setCashbillType(cashbill.getCashbillType());
                            cashbillParam.setCustomerName(cashbill.getCustomerName());
                            cashbillParam.setOrderCode(orderParam.getOrderCode());

                            response = inicisService.cashReceiptIssued(cashbillParam);
						} else if ("nicepay".equals(cashbillService)) {
							Cashbill cashbill = cashbillIssue.getCashbill();

							cashbillParam.setCashbillStatus(cashbillIssue.getCashbillStatus());
							cashbillParam.setAmount(cashbillIssue.getAmount());
							cashbillParam.setItemName(cashbillIssue.getItemName());
							cashbillParam.setTaxType(cashbillIssue.getTaxType());
							cashbillParam.setCashbillCode(cashbill.getCashbillCode());
							cashbillParam.setCashbillType(cashbill.getCashbillType());
							cashbillParam.setCustomerName(cashbill.getCustomerName());
							cashbillParam.setEmail(buyer.getEmail());
							cashbillParam.setOrderCode(orderParam.getOrderCode());

                        	response = nicepayService.cashReceiptIssued(cashbillParam);
						}

						if (response == null) {
							log.debug("[CASHBILL] ERROR >> PG 통신오류(응답없음)");
							throw new OrderException("PG 통신오류(응답없음)");
						}

						log.debug("[CASHBILL] cashbillIssue :  {}", cashbillIssue);
						log.debug("[CASHBILL] CashbillResponse response.isSuccess() :  {}", response.isSuccess());
						if (response.isSuccess()) {
							cashbillIssue.setIssuedDate(DateUtils.getToday(DATETIME_FORMAT));
							cashbillIssue.setUpdatedDate(DateUtils.getToday(DATETIME_FORMAT));
							cashbillIssue.setCashbillStatus(CashbillStatus.ISSUED);
							cashbillIssue.setMgtKey(response.getMgtKey());

							if (UserUtils.isUserLogin() || UserUtils.isManagerLogin()) {
								cashbillIssue.setUpdateBy(UserUtils.getUser().getUserName() + "(" + UserUtils.getLoginId() + ")");
							} else {
								cashbillIssue.setUpdateBy("비회원");
							}

							cashbillIssueRepository.save(cashbillIssue);

						} else {
							log.debug("[CASHBILL] ERROR >> {} : {}", response.getResponseCode(), response.getResponseMessage());
							throw new OrderException(response.getResponseCode() + " : " + response.getResponseMessage());
						}
                    }
                    log.debug("[CASHBILL] END ---------------------------------------------");
                }

				cnt++;
			}

			// 배송비 쿠폰
			if (buy.getShippingCoupon() > 0) {

				PointUsed pointUsed = new PointUsed();
				pointUsed.setOrderCode(orderCode);
				pointUsed.setPoint(buy.getShippingCoupon());
				pointUsed.setDetails("주문번호 :" + orderCode + " 사용");

				pointService.deductedPoint(pointUsed, buy.getUserId(), PointUtils.SHIPPING_COUPON_CODE);
			}

		} catch (OrderException e) {

//			log.error(e.getMessage(), e);
			log.error(getClass().getName() + " :: insertOrder OrderException =============", e);

			// DB 처리 에러가 발행하면 PG 취소함
			if (successOrderPgDatas != null) {
				for(OrderPgData orderPgData : successOrderPgDatas) {

					String approvalType = orderPgData.getApprovalType();
					orderPgData.setCancelReason("결제 중 오류로 주문 취소");

					if (orderPgData.isSuccess()) {
						boolean isCancelSuccess = false;
						if ("inicis".equals(orderPgData.getPgServiceType())) {
							isCancelSuccess = inicisService.cancel(orderPgData);

						} else if ("lgdacom".equals(orderPgData.getPgServiceType())) {
							isCancelSuccess = lgDacomService.cancel(orderPgData);

						} else if ("payco".equals(orderPgData.getPgServiceType())) {
							orderPgData.setRemainAmount(orderPrice.getOrderPayAmount());
							isCancelSuccess = paycoService.cancel(orderPgData);

						} else if ("kakaopay".equals(orderPgData.getPgServiceType())) {
							orderPgData.setRemainAmount(orderPrice.getOrderPayAmount());
							isCancelSuccess = kakaopayService.cancel(orderPgData);

						} else if ("cj".equals(orderPgData.getPgServiceType())) {
							//isCancelSuccess = cjService.cancel(orderPgData);

							//CJ PG는 RedirectUrl에서 실패시 취소 하도록 하자!!
							isCancelSuccess = true;
						} else if ("kspay".equals(orderPgData.getPgServiceType())) {
							isCancelSuccess = kspayService.cancel(orderPgData);

						} else if ("kcp".equals(orderPgData.getPgServiceType())) {
							orderPgData.setOrderCode(orderCode);
							isCancelSuccess = kcpService.cancel(orderPgData);

						} else if("easypay".equals(orderPgData.getPgServiceType())) {
							orderPgData.setOrderCode(orderCode);
							isCancelSuccess = easypayService.cancel(orderPgData);

						} else if("nicepay".equals(orderPgData.getPgServiceType())) {
							orderPgData.setOrderCode(orderCode);
							orderPgData.setRequest(request);
							orderPgData.setCancelAmount(orderPgData.getPgAmount());
							isCancelSuccess = nicepayService.cancel(orderPgData);
						} else if("naverpay".equals(orderPgData.getPgServiceType())) {
                            orderPgData.setCancelAmount(orderPgData.getPgAmount());
                            orderPgData = naverPaymentApi.cancel(orderPgData, configPg);

                            isCancelSuccess = orderPgData.isSuccess();
                        }

						if (isCancelSuccess == false) {
                            // 결제 취소 실패!!
                            // System.out.println("TID -> " + orderPgData.getPgKey() + " -> 결제취소 실패!!");
                            OrderCancelFail orderCancelFail = new OrderCancelFail();

                            orderCancelFail.setUpdateData(orderPgData);
                            orderCancelFail.setPgServiceType(orderPgData.getPgServiceType());

                            if (UserUtils.isManagerLogin()) {
                                orderCancelFail.setCancelRequester("2");
                            } else {
                                orderCancelFail.setCancelRequester("1");
                            }

                            throw new OrderException("결제취소 실패", "/order/step1", orderCancelFail, e);
						}

						// 현금영수증 취소
						receiptService.cancelCashbill(orderCode);
					}
				}
			}

//			throw new OrderException("결제 처리 중 에러가 발생하여 거래가 취소되었습니다. " + e.getMessage(), "/order/step1", e);
			throw new OrderException("주문 처리 중 에러가 발생하여 취소되었습니다. " + e.getErrorMessage(), "/order/step1", e);
			//throw new OrderException("결제 처리도중 에러가 발생하여 거래가 취소 되었습니다. 카드결제의 경우 승인취소 문자를 받지 못하신 경우 고객센터로 연락 바랍니다.", "/order/step1");
		} catch (OpRuntimeException e) {

			log.error("OrderService.inserOrder 처리 중 오류 발생", e);

			// DB 처리 에러가 발행하면 PG 취소함
			if (successOrderPgDatas != null) {
				for(OrderPgData orderPgData : successOrderPgDatas) {
                    orderPgData.setCancelReason("결제 중 오류로 주문 취소");

					String approvalType = orderPgData.getApprovalType();

					if (orderPgData.isSuccess()) {
						boolean isCancelSuccess = false;
						if ("inicis".equals(orderPgData.getPgServiceType())) {
							isCancelSuccess = inicisService.cancel(orderPgData);

						} else if ("lgdacom".equals(orderPgData.getPgServiceType())) {
							isCancelSuccess = lgDacomService.cancel(orderPgData);

						} else if ("payco".equals(orderPgData.getPgServiceType())) {
							orderPgData.setRemainAmount(orderPrice.getOrderPayAmount());
							isCancelSuccess = paycoService.cancel(orderPgData);

						} else if ("kakaopay".equals(orderPgData.getPgServiceType())) {
							orderPgData.setRemainAmount(orderPrice.getOrderPayAmount());
							isCancelSuccess = kakaopayService.cancel(orderPgData);

						} else if ("cj".equals(orderPgData.getPgServiceType())) {
							//isCancelSuccess = cjService.cancel(orderPgData);

							//CJ PG는 RedirectUrl에서 실패시 취소 하도록 하자!!
							isCancelSuccess = true;
						} else if ("kspay".equals(orderPgData.getPgServiceType())) {
							isCancelSuccess = kspayService.cancel(orderPgData);

						} else if ("kcp".equals(orderPgData.getPgServiceType())) {
							orderPgData.setOrderCode(orderCode);
							isCancelSuccess = kcpService.cancel(orderPgData);

						} else if("easypay".equals(orderPgData.getPgServiceType())) {
							orderPgData.setOrderCode(orderCode);
							isCancelSuccess = easypayService.cancel(orderPgData);

						} else if("nicepay".equals(orderPgData.getPgServiceType())) {
							orderPgData.setOrderCode(orderCode);
                            orderPgData.setRequest(request);
                            orderPgData.setCancelAmount(orderPgData.getPgAmount());
                            orderPgData.setMessage("주문취소");
							isCancelSuccess = nicepayService.cancel(orderPgData);
						} else if("naverpay".equals(orderPgData.getPgServiceType())) {
                            orderPgData.setCancelAmount(orderPgData.getPgAmount());
                            orderPgData = naverPaymentApi.cancel(orderPgData, configPg);

                            isCancelSuccess = orderPgData.isSuccess();
                        }

						if (isCancelSuccess == false) {
                            // 결제 취소 실패!!
                            log.debug("TID -> " + orderPgData.getPgKey() + " -> 결제취소 실패!!");
                            OrderCancelFail orderCancelFail = new OrderCancelFail();

                            orderCancelFail.setUpdateData(orderPgData);
                            orderCancelFail.setPgServiceType(orderPgData.getPgServiceType());

                            if (UserUtils.isManagerLogin()) {
                                orderCancelFail.setCancelRequester("2");
                            } else {
                                orderCancelFail.setCancelRequester("1");
                            }

                            throw new OrderException("결제취소 실패", "/order/step1", orderCancelFail, e);
						}

						// 현금영수증 취소
						receiptService.cancelCashbill(orderCode);
					}
				}
			}

//			throw new OrderException("결제 처리 중 에러가 발생하여 거래가 취소되었습니다. " + e.getMessage(), "/order/step1", e);
			throw new OrderException("주문 처리 중 에러가 발생하여 취소되었습니다. " + e.getErrorMessage(), "/order/step1", e);
			//throw new OrderException("결제 처리도중 에러가 발생하여 거래가 취소 되었습니다. 카드결제의 경우 승인취소 문자를 받지 못하신 경우 고객센터로 연락 바랍니다.", "/order/step1");
		}

		try {
			// 주문서 작성 임시 저장 정보 삭제
			orderMapper.deleteOrderItemTemp(orderParam);

			// 주문서(세트상품) 임시 저장 정보 삭제
			orderMapper.deleteOrderItemSetTemp(orderParam);

			// 주문 임시 저장 정보 삭제
			orderMapper.deleteOrderTemp(buy);
			orderMapper.deleteOrderItemBuyTemp(buy);
			orderMapper.deleteOrderShippingBuyTemp(buy);
			orderMapper.deleteOrderPaymentBuyTemp(buy);

			// 주문이 완료된 장바구니 상품들 삭제
			List<Integer> itemIds = new ArrayList<>();
			for (BuyItem buyItem : buy.getItems()) {
				itemIds.add(buyItem.getItemId());
			}

			// 장바구니 삭제
			if (!itemIds.isEmpty()) {
				CartParam cartParam = new CartParam();
				cartParam.setUserId(buy.getUserId());
				cartParam.setSessionId(buy.getSessionId());
				cartParam.setItemIds(itemIds);

				// 로컬에서는 테스트를 위해서 장바구니를 삭제 하지 않는다.
				if (!ServiceType.LOCAL) {
					cartMapper.deleteCartByItemIds(cartParam);
				}
			}

		} catch(RuntimeException e) {
//			log.error("주문 처리 후 임시 데이터 삭제시 ERROR: {}", e.getMessage(), e);
			log.error("주문 처리 후 임시 데이터 삭제시 ERROR: {}", getClass().getName() + " :: insertOrder RuntimeException1 ============", e);
		}

		// Message 발송
		try {
			orderMessageService.sendOrderMessageTx(buy);
		} catch(RuntimeException e) {
//			log.error("주문 메시지 발송 ERROR: {}", e.getMessage(), e);
			log.error("주문 메시지 발송 ERROR: {}", getClass().getName() + " :: insertOrder RuntimeException2 ============", e);
		}

		// 재고 차감
		try {
			HashMap<String, Integer> stockMap = buy.getStockMap();
			HashMap<String, Integer> stockSetMap = buy.getStockSetMap();
			if (stockMap == null) {
				return orderSequence + "/" + orderCode;
			}

			this.updateStockDeduction(stockMap);

			// 세트상품 재고차감
			this.updateStockDeduction(stockSetMap);
		} catch(RuntimeException e) {
//			log.error("재고 차감 ERROR: {}", e.getMessage(), e);
			log.error("재고 차감 ERROR: {}", getClass().getName() + " :: insertOrder RuntimeException3 ============", e);
		}

		// 로그인시 주소록 저장 선택시 기본 배송지 저장
		if (UserUtils.isUserLogin()) {
			if ("Y".equals(buy.getSaveDeliveryFlag())) {

				Receiver receiver = buy.getReceivers().get(0);

				UserDelivery userDelivery = new UserDelivery();
				userDelivery.setUserId(buy.getUserId());
				userDelivery.setDefaultFlag("Y");

				String title = buy.getSaveDeliveryName();
				if (ObjectUtils.isEmpty(title) == false) {
					title = title.trim();
					if ("".equals(title)) {
						title= receiver.getReceiveName();
					}
				} else {
					title = receiver.getReceiveName();
				}

				userDelivery.setTitle(title);
				userDelivery.setUserName(receiver.getReceiveName());
				userDelivery.setPhone(receiver.getReceivePhone());
				userDelivery.setMobile(receiver.getReceiveMobile());
				userDelivery.setNewZipcode(receiver.getReceiveNewZipcode());
				userDelivery.setZipcode(receiver.getReceiveZipcode());
				userDelivery.setSido(receiver.getReceiveSido());
				userDelivery.setSigungu(receiver.getReceiveSido());
				userDelivery.setEupmyeondong(receiver.getReceiveEupmyeondong());
				userDelivery.setAddress(receiver.getReceiveAddress());
				userDelivery.setAddressDetail(receiver.getReceiveAddressDetail());

				userDeliveryService.insertUserDelivery(userDelivery);
			}
		}

		return orderSequence + "/" + orderCode;
	}

	/**
	 * 상품 재고 차감
	 * @param stockMap
	 */
	@Override
	public void updateStockDeduction(HashMap<String, Integer> stockMap) {
		try {
			if (stockMap == null) {
				return;
			}

			List<StockDeduction> list = new ArrayList<>();
			for (String key : stockMap.keySet()) {
				int quantity = stockMap.get(key);
				list.add(new StockDeduction(key, quantity));
			}

			for (StockDeduction stock : list) {

				if ("STOCK".equals(stock.getStockDeductionType())) {
					orderMapper.updateStockDeductionForItem(stock);
					orderMapper.updateStockDeductionForOption(stock);
				} else if ("ITEM".equals(stock.getStockDeductionType())) {
					orderMapper.updateStockDeductionForItem(stock);
				} else if ("OPTION".equals(stock.getStockDeductionType())) {
					orderMapper.updateStockDeductionForOption(stock);
				}

			}

		} catch(RuntimeException e) {
//			log.error(ERROR_MARKER, e.getMessage(), e);
			log.error(ERROR_MARKER, getClass().getName() + " :: updateStockDeduction RuntimeException =============", e);
		}
	}

	/**
	 * 상품 정보를 구성
	 * @param itemSequence
	 * @param buyItems
	 * @param buyItem
	 * @param buy
	 * @param shipping
	 * @param orderShippingInfo
	 * @param escrowStatus
	 */
	private void setOrderItemForBuy(int itemSequence, List<BuyItem> buyItems, BuyItem buyItem, Buy buy, Shipping shipping,
									OrderShippingInfo orderShippingInfo, String escrowStatus) {

		buyItem.setOrderCode(buy.getOrderCode());
		buyItem.setItemSequence(itemSequence);
		buyItem.setOrderSequence(shipping.getOrderSequence());
		buyItem.setShippingInfoSequence(orderShippingInfo.getShippingInfoSequence());
		buyItem.setOrderStatus(buy.getOrderStatus());
		buyItem.setDeviceType(buy.getDeviceType());

		buyItem.setBuyShipping(shipping);

		// 10 : 결제완료 인경우 결제확인일을 기록
		if ("10".equals(buy.getOrderStatus())) {
			buyItem.setPayDate(DateUtils.getToday(DATETIME_FORMAT));
			escrowStatus = escrowStatus.equals("N") ? escrowStatus : "10";	//에스크로 사용시 에스크로 상태를 결제완료(10)로 변경
		} else {
			buyItem.setPayDate("00000000000000");
		}

		buyItem.setEscrowStatus(escrowStatus);

		Item item = buyItem.getItem();

		if ("Y".equals(buyItem.getAdditionItemFlag())) {
			item.setItemName("┗(추가상품)" + item.getItemName());
		}

		buyItem.setShippingReturn(shipping.getShippingReturn());
		buyItem.setShippingSequence(shipping.getShippingSequence());

		buyItem.setUserId(buy.getUserId());

		//0 : 상품 준비중 - 신규 주문
		buyItem.setOrderItemStatus("0");
		String guestFlag = "N";
		if (buy.getUserId() == 0) {
			guestFlag = "Y";
		}

		buyItem.setSellerId(item.getSellerId());
		buyItem.setGuestFlag(guestFlag);

		// 판매 카테고리 정보를 기록
		ProductsRepCategories productsRepCategories = categoriesMapper.getProductsRepCategoriesByItemId(buyItem.getItemId());
		if (ValidationUtils.isNotNull(productsRepCategories)) {
			buyItem.setCategoryTeamId(productsRepCategories.getCategoryTeamId());
			buyItem.setCategoryGroupId(productsRepCategories.getCategoryGroupId());
			buyItem.setCategoryId(productsRepCategories.getCategoryId());
		}

		// 매출 기준일을 등록??
		Config shopConfig = ShopUtils.getConfig();
		buyItem.setRevenueSalesStatus(shopConfig.getRevenueSalesStatus());
		if (buy.getOrderStatus().equals(shopConfig.getRevenueSalesStatus())) { // 10 : 결제확인
			buyItem.setSalesDate(DateUtils.getToday(DATETIME_FORMAT));
		} else if ("0".equals(shopConfig.getRevenueSalesStatus())) { // 주문등록시 매출?
			buyItem.setSalesDate(DateUtils.getToday(DATETIME_FORMAT));
		}

		// 사은품
		if ("Y".equals(item.getFreeGiftFlag())) {
			buyItem.setFreeGiftName(item.getFreeGiftName());

			try {
				List<GiftItem> freeGiftItemList = giftItemService.getGiftItemListForFront(buyItem.getItemId());
				buyItem.setFreeGiftItemText(ShopUtils.makeGiftItemText(freeGiftItemList));
				buyItem.setFreeGiftItemList(ShopUtils.conventGiftItemInfoList(freeGiftItemList));
			} catch (RuntimeException e) {
//				log.error(ERROR_MARKER, e.getMessage(), e);
				log.error(ERROR_MARKER, getClass().getName() + " :: setOrderItemForBuy RuntimeException ============", e);
			}
		}

		// 출고지, 반송지
		buyItem.setShipmentId(item.getShipmentId());
		buyItem.setShipmentReturnId(item.getShipmentReturnId());

		// 택배사
		buyItem.setDeliveryCompanyName(item.getDeliveryCompanyName());

		// 결제시 포인트 사용하면 지급하지 않는 설정이라면...
        int usePoint = buy.getOrderPrice().getTotalPointDiscountAmount();
        // 2: 결제시 포인트 사용하면 포인트을 지급하지 않음
		if ("2".equals(shopConfig.getPointSaveType()) && usePoint > 0) {
			if (buyItem.getItemPrice() != null) {
				buyItem.getItemPrice().setEarnPoint(0);
				buyItem.getItemPrice().setSellerPoint(0);
			}
		}

		ItemPrice itemPrice = new ItemPrice(buyItem);
		int totalQuantity = 0;
		if(!ObjectUtils.isEmpty(buyItem.getItemPrice())) {
            itemPrice = buyItem.getItemPrice();
		}
		if(!ObjectUtils.isEmpty(itemPrice.getQuantity())) {
			totalQuantity = itemPrice.getQuantity();
		}

		// 상품쿠폰 사용처리
		OrderCoupon itemCoupon = buyItem.getUsedCoupon();

		if (itemCoupon != null) {

			itemCoupon.setOrderCode(buyItem.getOrderCode());
			couponService.updateCouponUserUseProcessByOrderCouponUser(itemCoupon);

			// 1개 수량만 적용 & 수량 1이상일 경우 상품 데이터 분리
			if (!"2".equals(itemCoupon.getCouponConcurrently()) && totalQuantity > 1) {
				try {
					itemPrice.setQuantity(1);
					buyItem.setItemPrice(itemPrice);
					itemPrice = new ItemPrice(buyItem);

					itemPrice.setCouponDiscountPrice(itemCoupon.getDiscountPrice());
					itemPrice.setCouponDiscountAmount(itemCoupon.getDiscountAmount());
					buyItem.setItemPrice(itemPrice);

					// 쿠폰 적용 상품 추가 (수량 1)
					buyItems.add(buyItem);

					BuyItem cloneBuyItem = (BuyItem) buyItem.clone();
					ItemPrice cloneItemPrice = new ItemPrice(cloneBuyItem);
					if(!ObjectUtils.isEmpty(cloneBuyItem.getItemPrice())) {
						cloneItemPrice = cloneBuyItem.getItemPrice();
					}

					cloneBuyItem.setItemSequence(++itemSequence);
					cloneItemPrice.setQuantity(totalQuantity - 1);

					cloneBuyItem.setItemPrice(cloneItemPrice);
					cloneBuyItem.setItemPrice(new ItemPrice(cloneBuyItem));
					cloneBuyItem.setCouponUserId(0);

					// 묶음배송 처리 데이터 추가
					cloneBuyItem.setShipmentGroupCode(shipping.getShipmentGroupCode());

					// 일반 상품 추가 (나머지)
					buyItems.add(cloneBuyItem);
				} catch (CloneNotSupportedException e) {
//					log.error(ERROR_MARKER, e.getMessage(), e);
					log.error(ERROR_MARKER, getClass().getName() + " :: setOrderItemForBuy CloneNotSupportedException ============", e);
					throw new OrderException("문제가 발생했습니다.");
				}
			} else {
				buyItems.add(buyItem);
			}
		} else {
			buyItems.add(buyItem);
		}

	}

	@Override
	public String pgConfirmationOfPayment(PgData pgData) {
		ConfigPg configPg = configPgService.getConfigPg();
		String pgType = "";

		if (configPg != null) {
			pgType = configPg.getPgType().getCode().toLowerCase();
		} else {
			pgType = SalesonProperty.getPgService();
		}

		String resultCode = "";
		String orderCode = "";
		String pgKey = "";
		int payAmount = 0;
		boolean isUpdate = false;
		if ("inicis".equals(pgType)) {
			resultCode = inicisService.confirmationOfPayment(pgData);
		} else if ("lgdacom".equals(pgType)) {

			orderCode = pgData.getLGD_OID();
			pgKey = pgData.getLGD_TID();
			resultCode = lgDacomService.confirmationOfPayment(pgData);

			if (StringUtils.isNotEmpty(pgData.getLGD_AMOUNT())) {
				payAmount = Integer.parseInt(pgData.getLGD_AMOUNT());
			}


			// 무통장 입금성공일때만 업데이트한다. 나머지 상태는 뭘해야 되지??
			if ("0000".equals(pgData.getLGD_RESPCODE().trim())) {
				if( "I".equals( pgData.getLGD_CASFLAG().trim() ) ) {
					isUpdate = true;
				}
			}
		}else if ("kspay".equals(pgType)) {
			resultCode = kspayService.confirmationOfPayment(pgData);
		}

		if (ObjectUtils.isEmpty(orderCode)) {
			return "주문번호가 넘어오지 않았습니다.";
		}

		if (ObjectUtils.isEmpty(pgKey)) {
			return "거래번호가 넘어오지 않았습니다.";
		}

		if (payAmount == 0) {
			return "결제금액이 넘어오지 않았습니다.";
		}

		if ("OK".equals(resultCode)) {

			if (isUpdate) {
				try {

					OrderParam orderParam = new OrderParam();
					orderParam.setOrderCode(orderCode);
					orderParam.setPgKey(pgKey);
					orderParam.setPayAmount(payAmount);

					if (orderPaymentMapper.updateConfirmationOfPaymentStep1(orderParam) > 0){
						if (orderPaymentMapper.updateConfirmationOfPaymentStep2(orderParam) > 0) {
							orderPaymentMapper.updateConfirmationOfPaymentStep3(orderParam);
						} else {
							throw new OrderManagerException();
						}
					} else {
						throw new OrderManagerException();
					}

				} catch (RuntimeException e) {
//					log.error(ERROR_MARKER, e.getMessage(), e);
					log.error(ERROR_MARKER, getClass().getName() + " :: pgConfirmationOfPayment RuntimeException ===========", e);
					return "결제결과 상점 DB처리에 실패하였습니다.";
				}
			}
		}


		return resultCode;
	}

	@Override
	public String cjPgConfirmationOfPayment(CjResult cjResult) {

		String orderCode = cjResult.getCJSShopOrderNo();
		String pgKey = cjResult.getCJSTradeID();
		String payAmount = cjResult.getCJSAmountTotal();

		if (ObjectUtils.isEmpty(orderCode)) {
			return "9999";
		}

		if (ObjectUtils.isEmpty(pgKey)) {
			return "9999";
		}

		if (ObjectUtils.isEmpty(payAmount)) {
			return "9999";
		}

		try {

			OrderParam orderParam = new OrderParam();
			orderParam.setOrderCode(orderCode);
			orderParam.setPgKey(pgKey);
			orderParam.setPayAmount(Integer.parseInt(payAmount));

			if (orderPaymentMapper.updateConfirmationOfPaymentStep1(orderParam) > 0){
				if (orderPaymentMapper.updateConfirmationOfPaymentStep2(orderParam) > 0) {
					orderPaymentMapper.updateConfirmationOfPaymentStep3(orderParam);
				} else {
					throw new OrderManagerException();
				}
			} else {
				throw new OrderManagerException();
			}

		} catch (RuntimeException e) {
//			log.error(ERROR_MARKER, e.getMessage(), e);
			log.error(ERROR_MARKER, getClass().getName() + " :: cjPgConfirmationOfPayment RuntimeException ==========", e);
			return "9999";
		}

		return "0000";
	}

	@Override
	public String paycoConfirmationOfPayment(String jsonString) {

		try {
			PayApprovalResult payApprovalResult = (PayApprovalResult) JsonViewUtils.jsonToObject(jsonString, new TypeReference<PayApprovalResult>(){});

			boolean isUpdate = false;
			int payAmount = 0;
			for(PaymentDetail payment : payApprovalResult.getPaymentDetails()) {

				// 무통장
				if ("02".equals(payment.getPaymentMethodCode()) && "Y".equals(payApprovalResult.getPaymentCompletionYn())) {
					isUpdate = true;

				}

				// 결제 수단 전체를 더해야 할까?
				payAmount += payment.getPaymentAmt();
			}

			if (isUpdate) {
				OrderParam orderParam = new OrderParam();
				orderParam.setOrderCode(payApprovalResult.getSellerOrderReferenceKey());
				orderParam.setPgKey(payApprovalResult.getOrderNo());
				orderParam.setPayAmount(payAmount);

				if (orderPaymentMapper.updateConfirmationOfPaymentStep1(orderParam) > 0){
					if (orderPaymentMapper.updateConfirmationOfPaymentStep2(orderParam) > 0) {
						orderPaymentMapper.updateConfirmationOfPaymentStep3(orderParam);
					} else {
						throw new OrderManagerException();
					}
				} else {
					throw new OrderManagerException();
				}
			}

			return "OK";
		} catch(RuntimeException e) {
//			log.error(ERROR_MARKER, e.getMessage(), e);
			log.error(ERROR_MARKER, getClass().getName() + " :: paycoConfirmationOfPayment RuntimeException ===========", e);

			return "ERROR";
		}
	}

	@Override
	public void updateShppingComplete(OrderParam orderParam) {
		OrderItem orderItem = orderMapper.getOrderItemByParam(orderParam);
		if (orderItem == null) {
			throw new OrderException("잘못된 접근 입니다.");
		}

		if (!ShopUtils.checkOrderStatusChange("shpping-complete", orderItem.getOrderStatus())) {
			throw new OrderException("배송완료를 할수 있는 상태가 아닙니다.");
		}

		if (orderShippingMapper.updateShppingComplete(orderParam) == 0) {
			throw new OrderException("배송완료를 할수 있는 상태가 아닙니다.\n 주문상태를 확인해주세요.");
		}

		// 주문로그
		this.insertOrderLog(
				OrderLogType.ORDER_SHIPPING_COMPLETE,
				orderItem.getOrderCode(),
				orderItem.getOrderSequence(),
				orderItem.getItemSequence(),
				orderItem.getOrderStatus()
		);
	}

	@Override
	public void updateConfirmPurchase(OrderParam orderParam) {
		OrderItem orderItem = orderMapper.getOrderItemByParam(orderParam);
		if (orderItem == null) {
			throw new OrderException("잘못된 접근 입니다.");
		}

		if (!ShopUtils.checkOrderStatusChange("confirm", orderItem.getOrderStatus())) {
			throw new OrderException("상품 구매확정을 할수 있는 상태가 아닙니다.");
		}

		// 정산 예정일 등록
		orderParam.setRemittanceDate(remittanceMapper.getRemittanceDateBySellerId(orderItem.getSellerId()));

		if (orderShippingMapper.updateConfirmPurchase(orderParam) == 0) {
			throw new OrderException("상품 구매확정을 할수 있는 상태가 아닙니다.\n 주문상태를 확인해주세요.");
		}

		UserDetail userDetail = UserUtils.getUserDetail();
		UserCouponParam userCouponParam = new UserCouponParam();
		userCouponParam.setUserLevelId(userDetail.getLevelId());

		String orderCode = orderParam.getOrderCode();
		List<String> orderStatusOriginalList = orderParam.getOrderStatusList();

		//첫구매 확인을 위해 임시 셋팅
		orderParam.setOrderCode(null);
		List<String> orderStatusList = new ArrayList<>();
		orderStatusList.add("40"); //구매확정 상태 셋팅
		orderParam.setOrderStatusList(orderStatusList);

		//첫구매확정 쿠폰 자동발행[2017-09-15]minae.yun
		if (orderMapper.getOrderCountByParam(orderParam) == 1) {

			userCouponParam.setCouponTargetTimeType("5");
			List<Coupon> firstOrderCouponList = couponService.getCouponByTargetTimeType(userCouponParam);

			if (firstOrderCouponList != null && firstOrderCouponList.size() != 0) {
				for (Coupon coupon : firstOrderCouponList) {
					userCouponParam.setCouponId(coupon.getCouponId());
					userCouponParam.setUserId(orderParam.getUserId());
					couponService.userCouponDownload(userCouponParam);
				}
			}
		}
		//첫구매 확인 후 다시 값 셋팅
		orderParam.setOrderCode(orderCode);
		orderParam.setOrderStatusList(orderStatusOriginalList);

		//상품 구매 후 발행 쿠폰 자동발행[2017-09-18]minae.yun
		userCouponParam.setCouponTargetTimeType("4");
		List<Coupon> afterOrderCouponList = couponService.getCouponByTargetTimeType(userCouponParam);

		if (afterOrderCouponList != null && afterOrderCouponList.size() != 0) {
			for (Coupon coupon : afterOrderCouponList) {
				userCouponParam.setCouponId(coupon.getCouponId());
				userCouponParam.setUserId(orderParam.getUserId());
				couponService.userCouponDownload(userCouponParam);
			}
		}

		// 배송비 정보에 정산 예정일을 업데이트
		orderParam.setShippingSequence(orderItem.getShippingSequence());
		orderShippingMapper.updateShippingRemittanceDate(orderParam);


        // 포인트 적립
        OrderPointParam opp = new OrderPointParam();
        opp.setUserId(orderParam.getUserId());
        pointService.savePointByOrderPointParam(opp);


		// 주문로그
		this.insertOrderLog(
				OrderLogType.ORDER_CONFIRM,
				orderItem.getOrderCode(),
				orderItem.getOrderSequence(),
				orderItem.getItemSequence(),
				orderItem.getOrderStatus()
		);
	}

	private String getAdminUser(){

		String adminUser = "";

		if (SellerUtils.isSellerLogin()) {
			if(SellerUtils.getSeller().getSellerName() != null) {
				adminUser = SellerUtils.getSeller().getSellerName();
			}
		} else {
			adminUser = UserUtils.getUser().getUserName();
		}

		return adminUser;
	}

	@Override
	public void saveOrderInfo(Order order) {

		if (order.getOrderShippingInfos() == null) {
			throw new OrderException();
		}

		for(OrderShippingInfo info : order.getOrderShippingInfos()) {

			info.setOrderCode(order.getOrderCode());
			info.setOrderSequence(order.getOrderSequence());

			orderMapper.updateOrderShippingInfo(info);

		}

	}

	@Override
	public void changeShippingNumber(ShippingParam shippingParam) {

		if (shippingParam.getDeliveryCompanyId() == 0
			|| ObjectUtils.isEmpty(shippingParam.getDeliveryNumber())) {
			throw new OrderException("잘못된 접근입니다.");
		}

		DeliveryCompany deliveryCompany = deliveryCompanyService.getDeliveryCompanyById(shippingParam.getDeliveryCompanyId());

		if (deliveryCompany == null) {
			throw new OrderException();
		}

		shippingParam.setDeliveryCompanyName(deliveryCompany.getDeliveryCompanyName());
		shippingParam.setDeliveryCompanyUrl(deliveryCompany.getDeliveryCompanyUrl());
		if (orderShippingMapper.updateShippingNumber(shippingParam) == 0) {
			throw new OrderException();
		}

	}

	@Override
	public void updateAdminMemo(Order order) {
		orderMapper.updateAdminMemo(order);
	}

	/** 구매확정 확인 */
	@Override
	public int getOrderItemCntForReview(OrderItem orderItem) {
		return orderMapper.getOrderItemCntForReview(orderItem);
	}

	@Override
	public String getOrderPaymentByPgDataForInipayVacct(PgData pgData, String deviceType) {

		OrderPayment orderPayment = null;
		if ("WEB".equals(deviceType)) {
			orderPayment = orderPaymentMapper.getOrderPaymentByPgDataForInipayVacct(pgData);
		} else if ("MOBILE".equals(deviceType)) {
			orderPayment = orderPaymentMapper.getOrderPaymentByPgDataForInipayVacctForMobile(pgData);
		}
		if (orderPayment == null) {
			return "99";
		}

		if ("WEB".equals(deviceType)) {
			if (orderPayment.getAmount() != Integer.parseInt(pgData.getAmt_input())) {
				return "99";
			}
		} else if ("MOBILE".equals(deviceType)) {
			if (orderPayment.getAmount() != Integer.parseInt(pgData.getP_AMT())) {
				return "99";
			}
		}

		if(orderPayment.getOrderStatus().equals("0")){

			OrderParam orderParam = new OrderParam();
			orderParam.setOrderCode(orderPayment.getOrderCode());
			orderParam.setOrderSequence(orderPayment.getOrderSequence());
			orderParam.setPaymentSequence(orderPayment.getPaymentSequence());
			orderParam.setAdminUserName("system");
			orderParam.setPayAmount(orderPayment.getAmount());

			if (orderPaymentMapper.updateConfirmationOfPaymentStep1(orderParam) > 0){
				if (orderPaymentMapper.updateConfirmationOfPaymentStep2(orderParam) > 0) {
					orderPaymentMapper.updateConfirmationOfPaymentStep3(orderParam);
					if(!orderPayment.getEscrowStatus().equals("N")){	//에스크로면 에스크로 상태 결제완료로 변경
						orderParam.setEscrowStatus("10");
						orderMapper.updateEscrowStatus(orderParam);
					}

				} else {
					throw new OrderManagerException();
				}
			} else {
				throw new OrderManagerException();
			}

			try {

				OrderParam orderSearchParam = new OrderParam();
				orderSearchParam.setOrderCode(orderPayment.getOrderCode());
				orderSearchParam.setConditionType("OPMANAGER");
				Order order = this.getOrderByParam(orderSearchParam);

                // 현금영수증 발행
                CashbillParam cashbillParam = new CashbillParam();

                cashbillParam.setWhere("orderCode");
                cashbillParam.setQuery(orderParam.getOrderCode());

                Iterable<CashbillIssue> cashbillIssues = cashbillIssueRepository.findAll(cashbillParam.getPredicate());

                log.debug("[CASHBILL] START ---------------------------------------------");
                log.debug("[CASHBILL] cashbillIssues Size :  {}", ((List<CashbillIssue>) cashbillIssues).size());

                CashbillResponse response = null;

                for (CashbillIssue cashbillIssue : cashbillIssues) {

                    if ("popbill".equals(environment.getProperty("cashbill.service"))) {
                        response = receiptService.receiptIssue(cashbillIssue);
                    } else if ("inicis".equals(environment.getProperty("cashbill.service"))) {
                        Cashbill cashbill = cashbillIssue.getCashbill();

                        cashbillParam.setCashbillStatus(cashbillIssue.getCashbillStatus());
                        cashbillParam.setAmount(cashbillIssue.getAmount());
                        cashbillParam.setItemName(cashbillIssue.getItemName());
                        cashbillParam.setTaxType(cashbillIssue.getTaxType());
                        cashbillParam.setCashbillCode(cashbill.getCashbillCode());
                        cashbillParam.setCashbillType(cashbill.getCashbillType());
                        cashbillParam.setCustomerName(cashbill.getCustomerName());
                        cashbillParam.setOrderCode(orderParam.getOrderCode());

                        String email = this.getEmailByOrderCode(orderSearchParam);

                        // 이니시스 현금영수증 발행시 이메일이 필수항목
                        cashbillParam.setEmail(email);

                        response = inicisService.cashReceiptIssued(cashbillParam);
                    }

                    if (response == null) {
                        log.debug("[CASHBILL] ERROR >> PG 통신오류(응답없음)");
                        throw new OrderException("PG 통신오류(응답없음)");
                    }

                    log.debug("[CASHBILL] cashbillIssue :  {}", cashbillIssue);
                    log.debug("[CASHBILL] CashbillResponse response.isSuccess() :  {}", response.isSuccess());
                    if (response.isSuccess()) {
                        cashbillIssue.setIssuedDate(DateUtils.getToday(DATETIME_FORMAT));
                        cashbillIssue.setUpdatedDate(DateUtils.getToday(DATETIME_FORMAT));
                        cashbillIssue.setCashbillStatus(CashbillStatus.ISSUED);
                        cashbillIssue.setMgtKey(response.getMgtKey());

                        if (UserUtils.isUserLogin() || UserUtils.isManagerLogin()) {
                            cashbillIssue.setUpdateBy(UserUtils.getUser().getUserName() + "(" + UserUtils.getLoginId() + ")");
                        } else {
                            cashbillIssue.setUpdateBy("비회원");
                        }

                        cashbillIssueRepository.save(cashbillIssue);

                    } else {
                        log.debug("[CASHBILL] ERROR >> {} : {}", response.getResponseCode(), response.getResponseMessage());
                        throw new OrderException(response.getResponseCode() + " : " + response.getResponseMessage());
                    }
                }
                log.debug("[CASHBILL] END ---------------------------------------------");

				orderMessageService.sendOrderMessageTx(order, "order_cready_payment", ShopUtils.getConfig());

			} catch(OpRuntimeException e) {
				log.error("[CASHBILL] ERROR: {}", orderPayment.getOrderCode(), e);
				throw new OrderException(e.getErrorMessage(), e);
			}
		} else {	//입금대기상태가 아닌데 입금통보가 오는 경우 주문번호,날짜등을 로그로 기록.
			//Make Log
			String logLoot = environment.getProperty("pg.inipay.home");
			String logPath = logLoot+"/notiLog/"+DateUtils.getToday()+"/";
			String fileNm = orderPayment.getOrderCode()+".log";
			String content = "["+DateUtils.getToday("yyyy-MM-dd HH:mm:ss")+"] "+orderPayment.getOrderCode()+" - PG사로부터 입금통보 수신확인";

			File dir = new File(logPath);
			//디렉토리가 없으면 생성
			if(!dir.isDirectory()){
				dir.mkdirs();
			}

			//파일에 내용 쓰기
			FileWriter fw = null;
			BufferedWriter out = null;
			try{
				fw = new FileWriter(new File(logPath+fileNm), true);
				fw.write(content);

				out = new BufferedWriter(fw);
				out.newLine();

				fw.flush();

			} catch (IOException e) {
				log.error(ERROR_ORDER_CODE, orderPayment.getOrderCode(), e);
			    throw new OrderException("[ERROR] " + orderPayment.getOrderCode(), e);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
//						log.error("BufferedWriter close : {}", e.getMessage(), e);
						log.error("BufferedWriter close : {}", getClass().getName() + " :: getOrderPaymentByPgDataForInipayVacct IOException1 ===========", e);
					}
				}

				if (fw != null) {
					try {
						fw.close();
					} catch (IOException e) {
//						log.error("FileWriter close : {}", e.getMessage(), e);
						log.error("BufferedWriter close : {}", getClass().getName() + " :: getOrderPaymentByPgDataForInipayVacct IOException2 ===========", e);
					}
				}
			}
		}

		return "OK";
	}

	@Override
	public void changePayment(EditPayment editPayment) {

		ConfigPg configPg = configPgService.getConfigPg();
		String pgType = "";

		if (configPg != null) {
			pgType = configPg.getPgType().getCode();
		} else {
			pgType = SalesonProperty.getPgService();
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(editPayment.getOrderCode());
		orderParam.setOrderSequence(editPayment.getOrderSequence());
		orderParam.setPayChangeType("Y");

		orderParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			orderParam.setSellerId(SellerUtils.getSellerId());
			orderParam.setConditionType("SELLER");
		}

		Order order = this.getOrderByParam(orderParam);
		if (order == null) {
			throw new PageNotFoundException();
		}

        editPayment.setUserId(order.getUserId());

		List<OrderPayment> payments = getOrderPaymentListByParam(orderParam);
		if (payments == null) {
			throw new PageNotFoundException();
		}

		if (editPayment.getChangePayments() != null) {
			for(ChangePayment changePayment : editPayment.getChangePayments()) {

				int paymentSequence = changePayment.getPaymentSequence();
				int cancelAmount = changePayment.getCancelAmount();
				if (changePayment.getCancelAmount() > 0) {

					OrderPayment eqPayment = null;
					for(OrderPayment p : payments) {
						if (p.getPaymentSequence() == paymentSequence) {
							eqPayment = p;
							break;
						}
					}

					if (eqPayment == null) {
						throw new OrderException();
					}

                    if ("bank".equals(eqPayment.getApprovalType())
                            || "realtimebank".equals(eqPayment.getApprovalType())
                            || "vbank".equals(eqPayment.getApprovalType()) ) {
                        editPayment.setCashbillReissueFlag(true);
                    }

					if (eqPayment.getRemainingAmount() < cancelAmount) {
						throw new OrderException("결제잔액을 확인하세요.");
					}

					OrderPgData orderPgData = eqPayment.getOrderPgData();
					String approvalType = eqPayment.getApprovalType();
					if (PointUtils.isPointType(approvalType)) {

						if (order.getUserId() == 0) {
							throw new OrderException("비회원 포인트 사용 에러.");
						}

						Point point = new Point();
						point.setPoint(cancelAmount);
						point.setUserId(order.getUserId());
						point.setReason("주문번호 :" + order.getOrderCode() + " 포인트 사용 취소");
						point.setPointType(approvalType);
						point.setOrderCode(order.getOrderCode()); //[2017-05-11]minae.yun
						pointService.earnPoint("return", point);

					} else if (orderPgData.getOrderPgDataId() > 0) {

						orderPgData.setReturnAccountNo(changePayment.getReturnBankVirtualNo());
						orderPgData.setReturnBankName(changePayment.getReturnBankName());
						orderPgData.setReturnName(changePayment.getReturnBankInName());
                        orderPgData.setCancelReason(editPayment.getRefundReason());

						boolean isSuccess = false;

						// 취소 금액과 잔여액이 같으면 전체 취소
						boolean isPartCancel = cancelAmount == eqPayment.getRemainingAmount() ? false : true;

						// 2017.05.25 Son Jun-Eu - Kspay도 한번 부분취소하면 부분취소만 할수있음.
						if ("kspay".equals(orderPgData.getPgServiceType())) {
							if(!"N".equals(orderPgData.getPartCancelDetail()))
								isPartCancel = true;
							else if(eqPayment.getRemainingAmount() != eqPayment.getTotalPaymentAmount() && eqPayment.getApprovalType().equals("realtimebank"))	//실시간계좌이체는 부분취소시 PG취소 안함
								isPartCancel = true;
						}

						// 2017.09.04 Son Jun-Eu Kcp 계좌이체,가상계좌에 부분취소 이력이 있으면 이후에도 계속 부분취소
						if ("kcp".equals(orderPgData.getPgServiceType())) {
							if(!"CARD".equals(orderPgData.getPgPaymentType()) && "PART_CANCEL".equals(orderPgData.getPartCancelDetail()))
								isPartCancel = true;
						}

						if (("inicis".equals(orderPgData.getPgServiceType())	// CJH 2016.12.07 - 이니시스의 경우 부분취소를 한번하면 부분취소만 되는듯!!
								|| "nicepay".equals(orderPgData.getPgServiceType())
								|| "lgdacom".equals(orderPgData.getPgServiceType()))
								&& "PART_CANCEL".equals(orderPgData.getPartCancelDetail())) {

							isPartCancel = true;
						}

						orderPgData.setCancelAmount(cancelAmount);
						orderPgData.setRemainAmount(eqPayment.getRemainingAmount() - cancelAmount);

						String useVbankRefundService = "";

						if (configPg != null) {
							useVbankRefundService = configPg.isUseVbackRefundService() ? "Y" : "N";
						} else {
							useVbankRefundService = environment.getProperty("pg.useVbank.refundService");
						}

						// 가상계좌 취소요청시 pg가상계좌 환불서비스 사용 안할경우 pg연동취소 사용안함.
						if ("vbank".equals(eqPayment.getApprovalType()) && "N".equals(useVbankRefundService)){
							isSuccess = true;

						} else if(!"N".equals(eqPayment.getEscrowStatus())){
							if (eqPayment.getEscrowStatus().equals("40")) {
								if("inicis".equals(orderPgData.getPgServiceType())){
									// 2017.07.05 Son Jun-Eu - 이니시스 에스크로 결제인 경우 PG사에 구매거절 확인 전문송신
									List<String> param = new ArrayList<>();

									param.add(orderPgData.getPgKey());
									param.add(environment.getProperty("pg.inipay.escrow.mid"));
									param.add(UserUtils.getManagerName());

									//									isSuccess = inicisService.escrowDenyConfirm(param);	// 현재 에스크로 배송등록 이후에는 무조건
									isSuccess = true;
								}
							}else if (eqPayment.getEscrowStatus().equals("20")) {
								isSuccess = true;

							}
						} else if (isPartCancel == false) {
							// PG 취소 - 즉시 취소
							if ("inicis".equals(orderPgData.getPgServiceType())) {
								isSuccess = inicisService.cancel(orderPgData);
							} else if ("lgdacom".equals(orderPgData.getPgServiceType())) {
								isSuccess = lgDacomService.cancel(orderPgData);
							} else if ("kakaopay".equals(orderPgData.getPgServiceType())) {
								orderPgData.setRemainAmount(cancelAmount);
								isSuccess = kakaopayService.cancel(orderPgData);
							} else if ("payco".equals(orderPgData.getPgServiceType())) {
								orderPgData.setRemainAmount(cancelAmount);
								isSuccess = paycoService.cancel(orderPgData);
							} else if ("kspay".equals(orderPgData.getPgServiceType())) {
								isSuccess = kspayService.cancel(orderPgData);
							} else if ("kcp".equals(orderPgData.getPgServiceType())) {
								isSuccess = kcpService.cancel(orderPgData);
							} else if ("easypay".equals(orderPgData.getPgServiceType())) {
								isSuccess = easypayService.cancel(orderPgData);
							} else if ("nicepay".equals(orderPgData.getPgServiceType())) {
								orderPgData.setRequest(editPayment.getRequest());
								orderPgData.setResponse(editPayment.getResponse());
								isSuccess = nicepayService.cancel(orderPgData);
                            } else if ("naverpay".equals(orderPgData.getPgServiceType())) {
                                orderPgData.setCancelReason(editPayment.getRefundReason());
                                orderPgData = naverPaymentApi.cancel(orderPgData, configPg);
                                isSuccess = orderPgData.isSuccess();
                            }

						} else {

							if ("inicis".equals(orderPgData.getPgServiceType())) {
								orderPgData = inicisService.partCancel(orderPgData);
							} else if ("lgdacom".equals(orderPgData.getPgServiceType())) {
								orderPgData = lgDacomService.partCancel(orderPgData);
							} else if ("kakaopay".equals(orderPgData.getPgServiceType())) {
								orderPgData.setRemainAmount(cancelAmount);
								orderPgData = kakaopayService.partCancel(orderPgData);
							} else if ("payco".equals(orderPgData.getPgServiceType())) {

								OrderItem item = new OrderItem();
								for (OrderShippingInfo orderShippingInfo : order.getOrderShippingInfos()) {
									for (OrderItem orderItem : orderShippingInfo.getOrderItems()) {
										item.setItemUserCode(orderItem.getItemUserCode());
									}
								}

								orderPgData.setPaycoCancelProducts(PaycoUtils.makePaycoCancelProducts(item, cancelAmount, order.getShippingTotalAmount(), orderPgData.getPgProcInfo()));
								orderPgData = paycoService.partCancel(orderPgData);
							} else if ("kspay".equals(orderPgData.getPgServiceType())) {
								if(!approvalType.equals("realtimebank"))
									orderPgData = kspayService.partCancel(orderPgData);
								else
									orderPgData.setSuccess(true);

							} else if ("kcp".equals(orderPgData.getPgServiceType())) {
								orderPgData = kcpService.partCancel(orderPgData);
							} else if ("easypay".equals(orderPgData.getPgServiceType())) {
								orderPgData = easypayService.partCancel(orderPgData);
							} else if ("nicepay".equals(orderPgData.getPgServiceType())) {
								orderPgData.setRequest(editPayment.getRequest());
								orderPgData.setResponse(editPayment.getResponse());
								orderPgData = nicepayService.partCancel(orderPgData);
                            } else if ("naverpay".equals(orderPgData.getPgServiceType())) {
                                orderPgData.setCancelReason(editPayment.getRefundReason());
                                orderPgData = naverPaymentApi.cancel(orderPgData, configPg);
                            }

							isSuccess = orderPgData.isSuccess();

						}

						if (isSuccess) {

                            eqPayment.setCancelAmount(orderPgData.getCancelAmount());
                            eqPayment.setRemainingAmount(orderPgData.getRemainAmount());

							OrderPayment orderPayment = new OrderPayment(order, eqPayment.getApprovalType());

                            if(isPartCancel) {
                                // 2017.07.12 Son Jun-Eu 부분취소시 취소금액, 결제잔액 등록
                                eqPayment.setCancelAmount(orderPgData.getCancelAmount());
                                eqPayment.setRemainingAmount(orderPgData.getRemainAmount());

                                orderMapper.updateOrderPgData(orderPgData);
                            }

							orderPaymentMapper.updateOrderPaymentForCancel(eqPayment);

							orderPayment.setCardEasyType(eqPayment.getCardEasyType());
							orderPayment.setPaymentType("2");
							orderPayment.setCancelAmount(cancelAmount);
							orderPayment.setPayDate(DateUtils.getToday(DATETIME_FORMAT));
							orderPayment.setNowPaymentFlag("Y");
							orderPayment.setDeviceType("ADMIN");
							orderPayment.setBankInName(changePayment.getReturnBankInName());
							if (ObjectUtils.isEmpty(changePayment.getReturnBankName()) == false) {
								List<CodeInfo> list = ShopUtils.getBankListByKey(orderPgData.getPgServiceType());
								for(CodeInfo code : list) {
									if (code.getKey().getId().equals(changePayment.getReturnBankName())) {
										orderPayment.setReturnBankName(code.getLabel());
										break;
									}
								}
							}
							orderPayment.setBankVirtualNo(orderPayment.getReturnBankName()+" "+changePayment.getReturnBankVirtualNo());


							// 데이터 암호화
							orderPayment.encrypt(orderPaymentEncryptor);
							orderPaymentMapper.insertOrderPayment(orderPayment);

							continue;

						} else {

							String errorMessage = "";
							if (orderPgData.getErrorMessage() != null && !orderPgData.getErrorMessage().isEmpty()) {
								errorMessage = " (" + orderPgData.getErrorMessage() + ")";
							}
							throw new OrderException("[ERROR] PG 취소가 정상적으로 처리되지 않았습니다. " + errorMessage);
						}
					}

					OrderPayment orderPayment = new OrderPayment(eqPayment, approvalType);
					orderPayment.setDeviceType("ADMIN");
					orderPayment.setCancelAmount(eqPayment.getCancelAmount() + cancelAmount);
					orderPayment.setRemainingAmount(eqPayment.getRemainingAmount() - cancelAmount);
					orderPaymentMapper.updateOrderPaymentForCancel(orderPayment);

					orderPayment.setCardEasyType(eqPayment.getCardEasyType());
					orderPayment.setPaymentType("2");
					orderPayment.setOrderPgDataId(eqPayment.getOrderPgDataId());
					orderPayment.setCancelAmount(cancelAmount);
                    orderPayment.setRemainingAmount(0);
					orderPayment.setPayDate(DateUtils.getToday(DATETIME_FORMAT));
					orderPayment.setNowPaymentFlag("Y");
					orderPayment.setBankInName(changePayment.getReturnBankInName());

					if (ObjectUtils.isEmpty(changePayment.getReturnBankName()) == false) {
						List<CodeInfo> list = ShopUtils.getBankListByKey(orderPgData.getPgServiceType());
						for(CodeInfo code : list) {
							if (code.getKey().getId().equals(changePayment.getReturnBankName())) {
								orderPayment.setReturnBankName(code.getLabel());
								break;
							}
						}
					}
					orderPayment.setBankVirtualNo(orderPayment.getReturnBankName()+" "+changePayment.getReturnBankVirtualNo());

					// 데이터 암호화
					orderPayment.encrypt(orderPaymentEncryptor);
					orderPaymentMapper.insertOrderPayment(orderPayment);

//					// 환불시 환불 정보 수정 2017-04-10 yulsun.yoo
//					orderParam.setReturnBankName(orderPayment.getReturnBankName());
//					orderParam.setReturnBankInName(changePayment.getReturnBankInName());
//					orderParam.setReturnVirtualNo(changePayment.getReturnBankVirtualNo());
//
//					// 데이터 암호화
//					orderParam.encrypt(orderParamEncryptor);
//					orderMapper.updateOrderReturnInfo(orderParam);

				}
			}
		}

		if (editPayment.getDeletePaymentIds() != null) {
			for(int paymentSequence : editPayment.getDeletePaymentIds()) {

				orderParam.setPaymentSequence(paymentSequence);

				// 1. [SKC] OrderPgData 조회
				OrderPgData orderPgData = orderPaymentMapper.getOrderPgDataByOrderParam(orderParam);

				// 2. orderPgData 가 가상계좌 결제인 경우 (가상계좌 취소인 경우 PG 결제 요청 취소 처리)
				if (orderPgData != null && "vbank".equals(orderPgData.getPgPaymentType().toLowerCase())) {
					boolean isCanceled = inicisService.cancel(orderPgData);

					if (!isCanceled) {
						throw new OrderException("가상계좌 취소(입금요청 취소)가 실패하였습니다. ");
					}
				}

				orderPaymentMapper.deleteOrderPayment(orderParam);

			}
		}

		if (editPayment.getNewPayments() != null) {

			for(NewPayment payment : editPayment.getNewPayments()) {

				if (payment.getAmount() > 0) {

					String approvalType = payment.getApprovalType();
					if (PointUtils.isPointType(approvalType)) {

						if (order.getUserId() == 0) {
							throw new OrderException("비회원 포인트 사용 에러.");
						}

						PointUsed pointUsed = new PointUsed();
						pointUsed.setOrderCode(order.getOrderCode());
						pointUsed.setPoint(payment.getAmount());
						pointUsed.setDetails("주문번호 :" + order.getOrderCode() + " 사용");

						pointService.deductedPoint(pointUsed, order.getUserId(), approvalType);

					}

					OrderPayment orderPayment = new OrderPayment(order, approvalType);
					orderPayment.setDeviceType("ADMIN");
					orderPayment.setAmount(payment.getAmount());
					orderPayment.setNowPaymentFlag("N");
					orderPayment.setPaymentType("1");

					if (!"bank".equals(approvalType)) {
						orderPayment.setPayDate(DateUtils.getToday(DATETIME_FORMAT));
						orderPayment.setRemainingAmount(payment.getAmount());
						orderPayment.setNowPaymentFlag("Y");
					}

					// 데이터 암호화
					orderPayment.encrypt(orderPaymentEncryptor);
					orderPaymentMapper.insertOrderPayment(orderPayment);
				}
			}

		}


		// [SKC] 결제 수단과 상관 없이 은행 입금으로 환불 처리하는 경우 -> bank / 취소로 등록함.
		if (editPayment.getRefundAmount() != null && editPayment.getRefundAmount() > 0) {

			OrderPayment orderPayment = new OrderPayment(order, "bank");
			orderPayment.setPaymentType("2");
			orderPayment.setDeviceType("ADMIN");
			orderPayment.setAmount(0);
			orderPayment.setTaxFreeAmount(0);
			orderPayment.setCancelAmount(editPayment.getRefundAmount());
			orderPayment.setRemainingAmount(0);
			orderPayment.setPayDate(DateUtils.getToday(DATETIME_FORMAT));			// paydate는 현재 시간으로 설정.
			orderPayment.setNowPaymentFlag("N");
			orderPayment.setRefundFlag("Y");

			StringBuffer sb = new StringBuffer();
			sb.append("[");
			sb.append(ShopUtils.getBankName(pgType, editPayment.getRefundBankName()));
			sb.append("] ");
			sb.append(editPayment.getRefundAccountNumber());
			sb.append(" (");
			sb.append(editPayment.getRefundAccountName());
			sb.append(")");

			orderPayment.setPaymentSummary(sb.toString());

			// 데이터 암호화
			orderPayment.encrypt(orderPaymentEncryptor);
			orderPaymentMapper.insertOrderPayment(orderPayment);

		}


		//orderPaymentMapper.updateTotalPayAmount(orderParam);
        orderMapper.updateConfirmationOfPaymentCancelStep2(orderParam);
	}

	@Override
	public void adminClaimApply(AdminClaimApply adminClaimApply) {			// 취소/반품/교환 요청

		if (adminClaimApply.getAdminClaimApplyKey() == null) {
			return;
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(adminClaimApply.getOrderCode());
		orderParam.setOrderSequence(adminClaimApply.getOrderSequence());
		orderParam.setConditionType("OPMANAGER");

		if (ShopUtils.isSellerPage()) {
			orderParam.setConditionType("SELLER");
			orderParam.setSellerId(SellerUtils.getSellerId());
		}

		Order order = getOrderByParam(orderParam);
		if (order == null) {
			throw new OrderException();
		}

		if ("1".equals(adminClaimApply.getClaimType())) { // 취소		// claimType 1: 취소, 2: 반품, 3: 교환

			OrderCancelApply apply = adminClaimApply.getOrderCancelApply();

			for (String key : adminClaimApply.getAdminClaimApplyKey()) {

				AdminClaimApplyItem item = adminClaimApply.getItemMap().get(key);
				if (item == null) {
					continue;
				}

				orderParam.setItemSequence(item.getItemSequence());
				OrderItem orderItem = orderMapper.getOrderItemByParam(orderParam);

				if (orderItem == null) {
					continue;
				}

				if ("40".equals(orderItem.getOrderStatus())) {		// 구매확정건은 처리 안함
					continue;
				}

				// 세트상품 셋
				orderParam.setSetItemSequence(orderItem.getItemSequence());

				List<OrderItem> itemSets = orderMapper.getOrderItemSetList(orderParam);
				orderItem.setItemSets(itemSets);

				if (item.getQuantity() <= 0) {
					continue;
				}

				if (!ShopUtils.checkOrderStatusChange("cancel", orderItem.getOrderStatus())) {
					continue;
				}

				if (orderItem.getOrderQuantity() < apply.getClaimApplyQuantity() + orderItem.getClaimQuantity()) {
					continue;
				}

				// DB에 저장된 수량을 가져다가 신청수량을 빼서 비교하기
				orderItem.setQuantity(orderItem.getOrderQuantity() - item.getQuantity());

				apply.setClaimApplyQuantity(item.getQuantity());
				apply.setItemSequence(item.getItemSequence());

				apply.setCancelReasonDetail(adminClaimApply.getCancelClaimReasonDetail());
				apply.setCancelReason(adminClaimApply.getCancelClaimReason());
				apply.setCancelReasonText(adminClaimApply.getCancelClaimReasonText());

				// 잔여 수량이 없으면 기존정보 수정
				if (orderItem.getQuantity() == 0) {
					orderClaimApplyMapper.insertOrderCancelApply(apply);
					orderClaimApplyMapper.updateClaimQuantityForCancelApply(apply);
				} else {
					orderClaimApplyMapper.copyOrderItemForCancelApply(apply);
					orderClaimApplyMapper.updateOrderItemQuantityForCancel(apply);
					// 중요!!
					apply.setItemSequence(apply.getCopyItemSequence());
					orderClaimApplyMapper.insertOrderCancelApply(apply);
					adminClaimApply.changeAdminClaimApplyKey(apply);

				}

				// ====== 세트상품 수량 설정 ======================
				if (itemSets != null && !itemSets.isEmpty()) {
					int copySetItemSequence = orderClaimApplyMapper.getMaxSetItemSequence(orderParam);
					apply.setSetClaimCode(apply.getClaimCode());
					apply.setCopySetItemSequence(copySetItemSequence);

					for (OrderItem itemSet : itemSets) {
						apply.setSetItemSequence(itemSet.getSetItemSequence());
						apply.setItemSequence(itemSet.getItemSequence());
						apply.setClaimApplyQuantity(itemSet.getQuantity());

						// 잔여 수량이 없으면 기존정보 수정
						if (orderItem.getQuantity() == 0) {
							orderClaimApplyMapper.insertOrderSetCancelApply(apply);
							orderClaimApplyMapper.updateSetClaimQuantityForCancelApply(apply);
						} else {
							// 부분취소 시 row 추가 생성
							orderClaimApplyMapper.copyOrderItemSetForCancelApply(apply);

							apply.setSetItemSequence(copySetItemSequence);
							orderClaimApplyMapper.insertOrderSetCancelApply(apply);
						}
					}
				}

				// 주문 로그
				this.insertOrderLog(OrderLogType.CLAIM_CANCEL,
							apply.getOrderCode(),
							apply.getOrderSequence(),
							apply.getItemSequence(),
							orderItem.getOrderStatus());
			}
		} else if ("2".equals(adminClaimApply.getClaimType())) {

			OrderReturnApply apply = adminClaimApply.getOrderReturnApply();
			for(String key : adminClaimApply.getAdminClaimApplyKey()) {

				AdminClaimApplyItem item = adminClaimApply.getItemMap().get(key);
				if (item == null) {
					continue;
				}

				orderParam.setItemSequence(item.getItemSequence());
				OrderItem orderItem = orderMapper.getOrderItemByParam(orderParam);

				if (orderItem == null) {
					continue;
				}

				if ("40".equals(orderItem.getOrderStatus())) {		// 구매확정건은 처리 안함
					continue;
				}

				// 세트상품 셋
				orderParam.setSetItemSequence(orderItem.getItemSequence());

				List<OrderItem> itemSets = orderMapper.getOrderItemSetList(orderParam);
				orderItem.setItemSets(itemSets);

				// 환불 신청 전 주문상태 저장
				apply.setPreviousOrderStatus(orderItem.getOrderStatus());

				if (item.getQuantity() <= 0) {
					continue;
				}

				if (!ShopUtils.checkOrderStatusChange("return", orderItem.getOrderStatus())) {
					continue;
				}

				if (orderItem.getOrderQuantity() < apply.getClaimApplyQuantity() + orderItem.getClaimQuantity()) {
					continue;
				}

				// DB에 저장된 수량을 가져다가 신청수량을 빼서 비교하기
				orderItem.setQuantity(orderItem.getQuantity() - item.getQuantity());

				apply.setClaimApplyQuantity(item.getQuantity());
				apply.setItemSequence(item.getItemSequence());

				orderParam.setShippingInfoSequence(orderItem.getShippingInfoSequence());
				apply.setReturnShippingInfo(getOrderShippingInfoByParam(orderParam));

				apply.setReturnReasonDetail(adminClaimApply.getReturnClaimReasonDetail());
				apply.setReturnReason(adminClaimApply.getReturnClaimReason());
				apply.setReturnReasonText(adminClaimApply.getReturnClaimReasonText());

				ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();
				shipmentReturnParam.setShipmentReturnId(orderItem.getShipmentReturnId());
				shipmentReturnParam.setItemId(orderItem.getItemId());
				shipmentReturnParam.setSellerId(orderItem.getSellerId());

				ShipmentReturn shipmentReturn;
				if (shipmentReturnParam.getShipmentReturnId() == 0
						&& shipmentReturnParam.getItemId() == 0
						&& shipmentReturnParam.getSellerId() == 0){
					shipmentReturn = null;
				} else {
					shipmentReturn = shipmentReturnMapper.getShipmentReturnByParam(shipmentReturnParam);
				}

				if (shipmentReturn != null) {
					apply.setShipmentReturnId(shipmentReturn.getShipmentReturnId());
				}

				// 1 : 본사반송, 2 업체 반송
//				long sellerId = "2".equals(orderItem.getShipmentReturnType()) ? orderItem.getSellerId() : SellerUtils.DEFAULT_OPMANAGER_SELLER_ID;
				long sellerId = orderItem.getSellerId();
				apply.setShipmentReturnSellerId(sellerId);

				// 잔여 수량이 없으면 기존정보 수정
				if (orderItem.getQuantity() == 0) {
					// 데이터 암호화
					apply.encrypt(orderReturnApplyEncryptor);
					orderClaimApplyMapper.insertOrderReturnApply(apply);
					orderClaimApplyMapper.updateClaimQuantityForReturnApply(apply);
				} else {
					orderClaimApplyMapper.copyOrderItemForReturnApply(apply);
					orderClaimApplyMapper.updateOrderItemQuantityForReturn(apply);

					// 중요!!
					apply.setItemSequence(apply.getCopyItemSequence());

					// 데이터 암호화
					apply.encrypt(orderReturnApplyEncryptor);
					orderClaimApplyMapper.insertOrderReturnApply(apply);
				}

				// ====== 세트상품 수량 설정 ======================
				if (itemSets != null && !itemSets.isEmpty()) {
					int copySetItemSequence = orderClaimApplyMapper.getMaxSetItemSequence(orderParam);
					apply.setSetClaimCode(apply.getClaimCode());
					apply.setCopySetItemSequence(copySetItemSequence);

					for (OrderItem itemSet : itemSets) {
						apply.setSetItemSequence(itemSet.getSetItemSequence());
						apply.setItemSequence(itemSet.getItemSequence());
						apply.setClaimApplyQuantity(itemSet.getQuantity());

						// 잔여 수량이 없으면 기존정보 수정
						if (orderItem.getQuantity() == 0) {
							orderClaimApplyMapper.insertOrderSetReturnApply(apply);
							orderClaimApplyMapper.updateSetClaimQuantityForReturnApply(apply);
						} else {
							// 부분취소 시 row 추가 생성
							orderClaimApplyMapper.copyOrderItemSetForReturnApply(apply);

							apply.setSetItemSequence(copySetItemSequence);
							orderClaimApplyMapper.insertOrderSetReturnApply(apply);
						}
					}
				}

				// 주문 로그
				this.insertOrderLog(OrderLogType.CLAIM_RETURN,
						apply.getOrderCode(),
						apply.getOrderSequence(),
						apply.getItemSequence(),
						orderItem.getOrderStatus());
			}
		} else {

			OrderExchangeApply apply = adminClaimApply.getOrderExchangeApply();
			for(String key : adminClaimApply.getAdminClaimApplyKey()) {

				AdminClaimApplyItem item = adminClaimApply.getItemMap().get(key);
				if (item == null) {
					continue;
				}

				orderParam.setItemSequence(item.getItemSequence());
				OrderItem orderItem = orderMapper.getOrderItemByParam(orderParam);

				if (orderItem == null) {
					continue;
				}

				if ("40".equals(orderItem.getOrderStatus())) {		// 구매확정건은 처리 안함
					continue;
				}

				// 세트상품 셋
				orderParam.setSetItemSequence(orderItem.getItemSequence());

				List<OrderItem> itemSets = orderMapper.getOrderItemSetList(orderParam);
				orderItem.setItemSets(itemSets);

				if (item.getQuantity() <= 0) {
					continue;
				}

				if (!ShopUtils.checkOrderStatusChange("exchange", orderItem.getOrderStatus())) {
					continue;
				}

				if (orderItem.getOrderQuantity() < apply.getClaimApplyQuantity() + orderItem.getClaimQuantity()) {
					continue;
				}

				// DB에 저장된 수량을 가져다가 신청수량을 빼서 비교하기
				orderItem.setQuantity(orderItem.getQuantity() - item.getQuantity());

				apply.setClaimApplyQuantity(item.getQuantity());
				apply.setItemSequence(item.getItemSequence());

				apply.setExchangeReasonDetail(adminClaimApply.getExchangeClaimReasonDetail());
				apply.setExchangeReason(adminClaimApply.getExchangeClaimReason());
				apply.setExchangeReasonText(adminClaimApply.getExchangeClaimReasonText());

				orderParam.setShippingInfoSequence(orderItem.getShippingInfoSequence());
				apply.setExchangeShippingInfo(getOrderShippingInfoByParam(orderParam));

				ShipmentReturnParam shipmentReturnParam = new ShipmentReturnParam();
				shipmentReturnParam.setShipmentReturnId(orderItem.getShipmentReturnId());
				ShipmentReturn shipmentReturn = shipmentReturnMapper.getShipmentReturnByParam(shipmentReturnParam);
				if (shipmentReturn != null) {
					apply.setShipmentReturnId(shipmentReturn.getShipmentReturnId());
				}

				// 1 : 본사반송, 2 업체 반송
				long sellerId = "2".equals(orderItem.getShipmentReturnType()) ? orderItem.getSellerId() : SellerUtils.DEFAULT_OPMANAGER_SELLER_ID;
				apply.setShipmentReturnSellerId(sellerId);

				// 잔여 수량이 없으면 기존정보 수정
				if (orderItem.getQuantity() == 0) {

					// 데이터 암호화
					apply.encrypt(orderExchangeApplyEncryptor);
					orderClaimApplyMapper.insertOrderExchangeApply(apply);
					orderClaimApplyMapper.updateClaimQuantityForExchangeApply(apply);
				} else {
					orderClaimApplyMapper.copyOrderItemForExchangeApply(apply);
					orderClaimApplyMapper.updateOrderItemQuantityForExchange(apply);

					// 중요!!
					apply.setItemSequence(apply.getCopyItemSequence());

					// 데이터 암호화
					apply.encrypt(orderExchangeApplyEncryptor);
					orderClaimApplyMapper.insertOrderExchangeApply(apply);
				}

				// ====== 세트상품 수량 설정 ======================
				if (itemSets != null && !itemSets.isEmpty()) {
					int copySetItemSequence = orderClaimApplyMapper.getMaxSetItemSequence(orderParam);
					apply.setSetClaimCode(apply.getClaimCode());
					apply.setCopySetItemSequence(copySetItemSequence);

					for (OrderItem itemSet : itemSets) {
						apply.setSetItemSequence(itemSet.getSetItemSequence());
						apply.setItemSequence(itemSet.getItemSequence());
						apply.setClaimApplyQuantity(itemSet.getQuantity());

						// 잔여 수량이 없으면 기존정보 수정
						if (orderItem.getQuantity() == 0) {
							orderClaimApplyMapper.insertOrderSetExchangeApply(apply);
							orderClaimApplyMapper.updateSetClaimQuantityForExchangeApply(apply);
						} else {
							// 부분취소 시 row 추가 생성
							orderClaimApplyMapper.copyOrderItemSetForExchangeApply(apply);

							apply.setSetItemSequence(copySetItemSequence);
							orderClaimApplyMapper.insertOrderSetExchangeApply(apply);
						}
					}
				}

				// 주문 로그
				this.insertOrderLog(OrderLogType.CLAIM_EXCHANGE,
						apply.getOrderCode(),
						apply.getOrderSequence(),
						apply.getItemSequence(),
						orderItem.getOrderStatus());
			}
		}
	}

	/**
	 * 주문상태별 개수
	 */
	@Override
	public OrderCount getOrderCountsByUserId(long userId) {
		return orderMapper.getOrderCountsByUserId(userId);
	}

	@Override
	public List<OpmanagerCount> getOpmanagerShippingDelayCountAll(HashMap<String, Object> map) {

		return orderMapper.getOpmanagerShippingDelayCountAll(map);
	}

	@Override
	public String getTidByParam(String orderCode) {
		return orderMapper.getTidByParam(orderCode);
	}

	@Override
	public void insertOrderLog(OrderLogType logType,
							   String orderCode, int orderSequence, int itemSequence, String orgOrderStatus) {

		// 로그 사용 여부에 따라 로그 기록
		if (logType != null && configLogService.isUsedConfigLogs(LogType.ORDER_STATUS, logType.getCode())) {

			try {

				UserType userType = this.getUserTypeForOrderLog();
				OrderItem orderItem;

				if (OrderLogType.ORDER_BATCH.equals(logType)) {
					OrderParam orderParam = new OrderParam();
					orderParam.setOrderCode(orderCode);
					orderParam.setOrderSequence(orderSequence);
					orderParam.setItemSequence(itemSequence);
					orderParam.setConditionType("OPMANAGER");
					orderItem = orderMapper.getOrderItemByParam(orderParam);
					userType = UserType.BATCH;
				} else {
					orderItem = getOrderItemForOrderLog(orderCode, orderSequence, itemSequence);
				}


				if (!ValidationUtils.isNull(orderItem)) {
					String ip = "";
					try {
						HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
						// ip = JwtUtils.getClientIpAddress(request);
                        ip = saleson.common.utils.CommonUtils.getClientIp(request);
					} catch (RuntimeException e) {
						log.info("[insertOrderLog] 배치 처리 등과 같이 request가 없는 처리인 경우 IP는 조회할 수 없습니다.");
					}

					OrderLog orderLog = new OrderLog();

					orderLog.setLogType(logType);
					orderLog.setOrderCode(orderCode);
					orderLog.setOrderSequence(orderSequence);
					orderLog.setItemName(orderItem.getItemName());
					orderLog.setItemSequence(orderItem.getItemSequence());
					orderLog.setOrderStatus(ShopUtils.getOrderStatusLabel(orderItem.getOrderStatus()));
					orderLog.setOrgOrderStatus(ShopUtils.getOrderStatusLabel(orgOrderStatus));
//					orderLog.setIp(ip);
					orderLog.setIp(pCrypto.Encrypt("normal", ip, ""));
					orderLog.setUserType(userType);
					if (UserType.BATCH.equals(userType)){
						orderLog.setCreatedBy("SYSTEM");
						orderLog.setUpdatedBy("SYSTEM");
					} else {
						orderLog.setCreatedBy(pCrypto.Encrypt("normal", getOrderLogLoginId(), ""));
						orderLog.setUpdatedBy(pCrypto.Encrypt("normal", getOrderLogLoginId(), ""));
					}

					orderLogRepository.save(orderLog);
				}

			} catch (RuntimeException | UnsupportedEncodingException e) {
//				log.error(ERROR_MARKER, e.getMessage(), e);
				log.error(ERROR_MARKER, getClass().getName() + " :: insertOrderLog RuntimeException ==========", e);
			}

		}
	}

	private UserType getUserTypeForOrderLog() {
		UserType userType = null;

		Seller seller = SellerUtils.getSeller();
		if (ValidationUtils.isNotNull(seller) && ShopUtils.isSellerPage()) {
			userType = UserType.SELLER;
		} else {
			if (UserUtils.isManagerLogin() && ShopUtils.isOpmanagerPage()) {
				userType = UserType.MANAGER;
			} else {
				if (UserUtils.isUserLogin()) {
					userType = UserType.USER;
				} else {
					userType = UserType.GUEST;
				}
			}
		}

		return userType;
	}

	private void setConditionTypeForOrderLog(OrderParam orderParam) {
		String conditionType = "";

		Seller seller = SellerUtils.getSeller();
		if (ValidationUtils.isNotNull(seller) && ShopUtils.isSellerPage()) {
			conditionType = "SELLER";
			orderParam.setSellerId(SellerUtils.getSellerId());
		} else {
			if (0 < UserUtils.getManagerId()) {
				conditionType = "OPMANAGER";
			} else {
				conditionType = "";
			}
		}
		orderParam.setConditionType(conditionType);
	}

	@Override
	public OrderItem getOrderItemForOrderLog(String orderCode, int orderSequence, int itemSequence) {
		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(orderCode);
		orderParam.setOrderSequence(orderSequence);
		orderParam.setItemSequence(itemSequence);
		this.setConditionTypeForOrderLog(orderParam);

		return orderMapper.getOrderItemByParam(orderParam);
	}

    @Override
    public List<OrderItem> getOrderItemListForOrderLog(String orderCode, int orderSequence) {

        OrderParam orderParam = new OrderParam();
        orderParam.setOrderCode(orderCode);
        orderParam.setOrderSequence(orderSequence);
        this.setConditionTypeForOrderLog(orderParam);

        return orderMapper.getOrderItemListByParam(orderParam);
    }

	@Override
	public Iterable<OrderLog> getOrderLogListByOrderCode(OrderLog orderLog) {
//		return orderLogRepository.findAll(orderLog.getPredicate());
		Iterable<OrderLog> searchResult = orderLogRepository.findAll(orderLog.getPredicate());
		if (searchResult != null) {
			for (OrderLog logInfo : searchResult) {
				if (logInfo != null) {
					try {
						logInfo.setIp(pCrypto.Decrypt("normal", logInfo.getIp(), "", 0));
					} catch (UnsupportedEncodingException e) {
						log.error(getClass().getName() + " :: getOrderLogListByOrderCode UnsupportedEncodingException ==========", e);
					}
				}
			}
		}
		return searchResult;
	}

	private String getOrderLogLoginId() {

		Seller seller = SellerUtils.getSeller();
//		if (ValidationUtils.isNotNull(seller) && ShopUtils.isSellerPage()) {
		if (seller != null && ShopUtils.isSellerPage()) {
			return new StringBuilder()
					.append(seller.getSellerName())
					.append("(")
					.append(seller.getLoginId())
					.append(")")
					.toString();
		}

		if (UserUtils.isManagerLogin() && ShopUtils.isOpmanagerPage()) {
			return new StringBuilder()
					.append(UserUtils.getManagerName())
					.append("(")
					.append(UserUtils.getLoginId())
					.append(")")
					.toString();
		}

		if (UserUtils.isUserLogin()) {
			return new StringBuilder()
					.append(UserUtils.getUser().getUserName())
					.append("(")
					.append(UserUtils.getLoginId())
					.append(")")
					.toString();
		}

		return "";
	}

	@Override
	public List<OrderList> getAllOrderListByParamForManager(OrderParam orderParam) {

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		orderParam.encrypt(orderParamEncryptor);
		int totalCount = slaveOrderMapper.getAllOrderCountByParamForManager(orderParam);

		//if (orderParam.getItemsPerPage() == 10) {
		//	orderParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		orderParam.setPagination(pagination);

		List<OrderList> list = slaveOrderMapper.getAllOrderListByParamForManager(orderParam);
		decryptOrderList(list);
		orderParam.decrypt(orderParamEncryptor);

		// 기타 주문 상품 정보 세팅 (사은품, 세트상품)
		setOrderItemOther(list);

		return list;

	}

	/**
	 * 관리자 오프라인 주문 전체 리스트 조회
	 * @param orderParam
	 * @return
	 */
	@Override
	public List<OrderList> getOfflineAllOrderListByParamForManager(OrderParam orderParam) {

		if (UserUtils.isManagerLogin()) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		orderParam.encrypt(orderParamEncryptor);
		int totalCount = slaveOrderMapper.getOfflineAllOrderCountByParamForManager(orderParam);

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		pagination.setItemsPerPage(orderParam.getItemsPerPage());
		orderParam.setPagination(pagination);

		List<OrderList> list = slaveOrderMapper.getOfflineAllOrderListByParamForManager(orderParam);
		decryptOrderList(list);
		orderParam.decrypt(orderParamEncryptor);

		// 기타 주문 상품 정보 세팅 (사은품, 세트상품)
		setOrderItemOther(list);

		return list;

	}

    private void receiptDataSave(Buy buy, int totCnt, int cnt) {

        if (totCnt == cnt) {
            long pointAmount = 0;

            // 포인트 사용금액 확인
            for (BuyPayment payment : buy.getPayments()) {
                if (PointUtils.isPointType(payment.getApprovalType())) {
                    pointAmount = (long) payment.getAmount();
                }
            }

            Boolean isSuccess = false;

            Cashbill cashbill = buy.getCashbill();

			ConfigPg configPg = configPgService.getConfigPg();
			String cashbillService = "";

			if (configPg != null) {
				cashbillService = configPg.getCashbillServiceType().getCode().toLowerCase();
			} else {
				cashbillService = environment.getProperty("cashbill.service");
			}

			cashbill.setPgService(cashbillService);
            cashbill.setOrderCode(buy.getOrderCode());
            cashbill.setCustomerName(buy.getBuyer().getUserName());
            cashbill.setCreatedDate(DateUtils.getToday(DATETIME_FORMAT));

            if (UserUtils.isUserLogin() || UserUtils.isManagerLogin()) {
                cashbill.setCreatedBy(UserUtils.getUser().getUserName() + "(" + UserUtils.getLoginId() + ")");
            } else {
                cashbill.setCreatedBy("비회원");
            }

            CashbillIssueType cashbillIssueType = CashbillIssueType.NORMAL;

            if (CashbillType.NONE == buy.getCashbill().getCashbillType()
                    && buy.getOrderPrice().getOrderPayAmount() >= CASHBILL_AUTO_ISSUED_PRICE) {     // 현금영수증 발급신청은 안했지만 10만원이상 상품구매시
                cashbill.setCashbillCode("0100001234");
                cashbill.setCashbillType(CashbillType.PERSONAL);

                cashbillRepository.save(cashbill);

                isSuccess = true;

                cashbillIssueType = CashbillIssueType.TEMP;
            } else if (CashbillType.NONE != buy.getCashbill().getCashbillType() && buy.getCashbill().getCashbillType() != null ) {// 현금영수증 발급신청했을 경우
                cashbillRepository.save(cashbill);

                isSuccess = true;
            }

            if (isSuccess) {
                CashbillIssue taxCashbillIssue = new CashbillIssue();
                CashbillIssue taxFreeCashbillIssue = new CashbillIssue();

                long taxCashbillAmount = 0;
                long taxFreeCashbillAmount = 0;

                // 배송비 현금영수증
                if (buy.getOrderPrice().getTotalShippingAmount() > 0) {
                    taxCashbillAmount += (long) buy.getOrderPrice().getTotalShippingAmount();
                }

                // 상품별 과세/면세 구분해서 현금영수증 금액에 추가
                for (BuyItem item : buy.getItems()) {
                	if(item.getItemPrice() != null) {
	                    if ("1".equals(item.getItem().getTaxType())) {
	                        taxCashbillAmount += (long) item.getItemPrice().getSaleAmount();
	                    } else {
	                        taxFreeCashbillAmount += (long) item.getItemPrice().getSaleAmount();
	                    }
                	}
                }

                if (taxCashbillAmount <= pointAmount) {
                    pointAmount -= taxCashbillAmount;
                    taxCashbillAmount = 0;

                    taxFreeCashbillAmount -= pointAmount;
                    pointAmount = 0;
                } else {
                    taxCashbillAmount -= pointAmount;
                    pointAmount = 0;
                }

				if (taxCashbillAmount > 0) {
					taxCashbillIssue.setCashbill(cashbill);
					taxCashbillIssue.setAmount((long) taxCashbillAmount);
					taxCashbillIssue.setCashbillStatus(CashbillStatus.PENDING);
					taxCashbillIssue.setItemName(buy.getOrderCode() + "(과세)");
					taxCashbillIssue.setCreatedDate(DateUtils.getToday(DATETIME_FORMAT));
					taxCashbillIssue.setCashbillIssueType(cashbillIssueType);
					taxCashbillIssue.setUpdateBy(buy.getBuyer().getUserName() + "(" + buy.getBuyer().getLoginId() + ")");
					taxCashbillIssue.setUpdatedDate(DateUtils.getToday(DATETIME_FORMAT));
					taxCashbillIssue.setTaxType(TaxType.CHARGE);

					cashbillIssueRepository.save(taxCashbillIssue);
				}

				if (taxFreeCashbillAmount > 0) {
					taxFreeCashbillIssue.setCashbill(cashbill);
					taxFreeCashbillIssue.setAmount((long) taxFreeCashbillAmount);
					taxFreeCashbillIssue.setCashbillStatus(CashbillStatus.PENDING);
					taxFreeCashbillIssue.setItemName(buy.getOrderCode() + "(면세)");
					taxFreeCashbillIssue.setCreatedDate(DateUtils.getToday(DATETIME_FORMAT));
					taxFreeCashbillIssue.setCashbillIssueType(cashbillIssueType);
					taxFreeCashbillIssue.setUpdateBy(buy.getBuyer().getUserName() + "(" + buy.getBuyer().getLoginId() + ")");
					taxFreeCashbillIssue.setUpdatedDate(DateUtils.getToday(DATETIME_FORMAT));
					taxFreeCashbillIssue.setTaxType(TaxType.FREE);

					cashbillIssueRepository.save(taxFreeCashbillIssue);
				}

            } // if(isSuccess) E
        }
    }

    @Override
    public String getEmailByOrderCode(OrderParam orderParam) {
        return orderMapper.getEmailByOrderCode(orderParam);
    }

	/**
	 * 주문 상품 등록
	 * @param buyItems
	 */
	private void insertOrderItem(List<BuyItem> buyItems) {

		for (BuyItem buyItem : buyItems) {
			try {
				orderMapper.insertOrderItem(buyItem);

				// 세트상품 셋
				if ("Y".equals(buyItem.getSetItemFlag())) {
					for (BuyItem buyItemSet : buyItem.getItemSets()) {
						buyItemSet.setOrderCode(buyItem.getOrderCode());
						buyItemSet.setOrderSequence(buyItem.getOrderSequence());
						buyItemSet.setSetItemSequence(buyItem.getItemSequence());
						buyItemSet.setGuestFlag(buyItem.getGuestFlag());
						orderMapper.insertOrderItemSet(buyItemSet);
					}
				}

				orderGiftItemService
						.insertOrderGiftItemByByItemId(buyItem.getOrderCode(), buyItem.getOrderSequence(), buyItem.getItemSequence(),
								buyItem.getItemId());
			} catch (OpRuntimeException e) {
				String code = buyItem.getOrderCode() + "-" + buyItem.getOrderSequence() + "-" + buyItem.getItemSequence();
				log.error(ERROR_MARKER, e);
				throw new OrderException("주문 상품 등록시 오류가 발생했습니다. : " + code);
			}
		}
	}

	/**
	 * 주문 상품 등록
	 * @param buyItems
	 */
	private void insertOrderItemNew(List<BuyItem> buyItems, List<Shipping> shippings, boolean isAgencyOrder) {
		for (BuyItem buyItem : buyItems) {
			try {
				for (Shipping shipping : shippings) {
					if (buyItem.getItemId() == shipping.getBuyItem().getItemId()) {
						buyItem.setShippingSequence(buyItem.getItemSequence());
						shipping.setShippingSequence(buyItem.getItemSequence());
						shipping.setOrderCode(buyItem.getOrderCode());
						orderMapper.insertOrderShipping(shipping);
						break;
					}
				}

				buyItem.setOptions(buyItem.getOptionsDisplay());

				orderMapper.insertOrderItem(buyItem);

				// 세트상품 셋
//				if ("Y".equals(buyItem.getSetItemFlag())) {
//					for (BuyItem buyItemSet : buyItem.getItemSets()) {
//						buyItemSet.setOrderCode(buyItem.getOrderCode());
//						buyItemSet.setOrderSequence(buyItem.getOrderSequence());
//						buyItemSet.setSetItemSequence(buyItem.getItemSequence());
//						buyItemSet.setGuestFlag(buyItem.getGuestFlag());
//						orderMapper.insertOrderItemSet(buyItemSet);
//					}
//				}
//				사은품 세팅
//				orderGiftItemService
//						.insertOrderGiftItemByByItemId(buyItem.getOrderCode(), buyItem.getOrderSequence(), buyItem.getItemSequence(),
//								buyItem.getItemId());

				insertOrderLog(OrderLogType.ORDER_PAYMENT, buyItem.getOrderCode(), buyItem.getOrderSequence(), buyItem.getShippingSequence(), buyItem.getOrderCode());
				if (isAgencyOrder) {		// 대행주문시 별도 로그 추가
					OrderAgencyOrderLog orderAgencyOrderLog = new OrderAgencyOrderLog();
					orderAgencyOrderLog.setOrderCode(buyItem.getOrderCode());
					orderAgencyOrderLog.setOrderSequence(buyItem.getOrderSequence());
					orderAgencyOrderLog.setItemSequence(buyItem.getItemSequence());
					orderAgencyOrderLog.setUserId(buyItem.getUserId());
					orderAgencyOrderLog.setManagerId(UserUtils.getUser().getUserId());
					orderAgencyOrderLog.setManagerNm(UserUtils.getUser().getUserName());

					if (SecurityUtils.hasRole("ROLE_ADMIN_11")) {
						OrderAgencyManagerInfo orderAgencyManagerInfo = orderAgencyService.selectOrderAgencyManagerInfo(UserUtils.getUser().getUserId());
						orderAgencyOrderLog.setPbadmsWlfrCntrId(orderAgencyManagerInfo.getPbadmsWlfrCntrId());
					} else {
						orderAgencyOrderLog.setPbadmsWlfrCntrId(0);
					}

					orderAgencyOrderLog.setManagerLclgvCd(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));

					orderMapper.insertOrderAgencyOrderLog(orderAgencyOrderLog);
				}
			} catch (RuntimeException e) {
				String code = buyItem.getOrderCode() + "-" + buyItem.getOrderSequence() + "-" + buyItem.getItemSequence();
//				log.error(ERROR_MARKER, e.getMessage(), e);
				log.error(ERROR_MARKER, getClass().getName() + " :: insertOrderItem RuntimeException ===========", e);
				throw new OrderException("주문 상품 등록시 오류가 발생했습니다. : " + code);
			}
		}
	}

	@Override
	public void setOrderItemOther(List<OrderList> orderList) {
		orderSupporter.setOrderItemOther(orderList);
	}

	@Override
	public void setOrderItemOther(OrderItem orderItem) {
		orderSupporter.setOrderItemOther(orderItem);
	}

	private void setOrderItemOther(Order order) {
		orderSupporter.setOrderItemOther(order);
	}

	private List<OrderGiftItem> filterOrderGiftItems(List<OrderGiftItem> orderGiftItems, String orderCode, int orderSequence, int itemSequence) {
		return orderSupporter.filterOrderGiftItems(orderGiftItems, orderCode, orderSequence, itemSequence);
	}

	@Override
	public List<OrderItem> filterOrderItemSets(List<OrderItem> orderItemSets, String orderCode, int orderSequence, int itemSequence) {
		return orderSupporter.filterOrderItemSets(orderItemSets, orderCode, orderSequence, itemSequence);
	}

	@Override
	public void setOrderGiftItemForOrderItem(List<OrderGiftItem> orderGiftItems, OrderItem orderItem) {

		String orderCode = orderItem.getOrderCode();
		int orderSequence = orderItem.getOrderSequence();
		int itemSequence = orderItem.getItemSequence();

		List<OrderGiftItem> giftItems = orderGiftItems.stream()
				.filter(
						giftItem -> orderCode.equals(giftItem.getOrderCode())
								&& orderSequence == giftItem.getOrderSequence()
								&& itemSequence == giftItem.getItemSequence()
				).collect(Collectors.toList());

		orderItem.setOrderGiftItemList(giftItems);

	}

	@Override
	public void setOrderGiftItemForOrderItem(OrderItem orderItem) {

		if (orderItem != null) {
			List<OrderGiftItem> orderGiftItemList = orderGiftItemService.getOrderGiftItemListByOrderCode(orderItem.getOrderCode());
			setOrderGiftItemForOrderItem(orderGiftItemList, orderItem);
		}

	}

	@Override
	public OrderPgData getOrderPgDataByOrderCode(String orderCode) {
		return orderMapper.getOrderPgDataByOrderCode(orderCode);
	}

	@Override
	public List<ApiOrderList> getApiOrderList(OrderParam orderParam) {
		List<ApiOrderList> resultList = new ArrayList<>();

//		int totalCount = orderMapper.getOrderCountByParam(orderParam);
//
//		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
//		pagination.setItemsPerPage(orderParam.getItemsPerPage());
//
//		ShopUtils.setPaginationInfo(pagination, orderParam.getConditionType(), orderParam.getPage());
//
//		orderParam.setPagination(pagination);

		List<Order> list = getOrderListByParam(orderParam);

		if (list == null) {
			return null;
		}

		HashSet<String> orderCodeSet = new HashSet<>();
		list.stream().forEach(order -> orderCodeSet.add(order.getOrderCode()));

//		String[] orderCodes = orderCodeSet.toArray(new String[orderCodeSet.size()]);
//
//		List<OrderGiftItem> orderGiftItems = orderGiftItemService.getOrderGiftItemListByOrderCodes(orderCodes);

		for (int i = 0; i < list.size(); i++) {
			bindOrder(list.get(i), orderParam);

//			for (OrderShippingInfo orderShippingInfo : list.get(i).getOrderShippingInfos()) {
//				for (OrderItem orderItem : orderShippingInfo.getOrderItems()) {
//					// 신상품
//					orderItem.setOrderGiftItemList(filterOrderGiftItems(orderGiftItems, orderItem.getOrderCode(), orderItem.getOrderSequence(), orderItem.getItemSequence()));
//				}
//			}

			ApiOrderList order = new ApiOrderList();
			List<OrderItem> orderItems = list.get(i).getOrderShippingInfos().get(0).getOrderItems();
			List<ItemInfo> items = new ArrayList<>();
			order.setOrderCode(list.get(i).getOrderCode());
			order.setOrderSequence(list.get(i).getOrderSequence());
			order.setCreatedDate(list.get(i).getCreatedDate());

			for (OrderItem orderItem : orderItems) {
				ItemInfo itemInfo = new ItemInfo();
				itemInfo.setItemUserCode(orderItem.getItemUserCode());
				itemInfo.setImageSrc(ShopUtils.loadImage(orderItem.getImageSrc(), "M"));
				itemInfo.setItemName(orderItem.getItemName());
				itemInfo.setOptions(ShopUtils.viewItemOptions(orderItem.getSetItemFlag(), orderItem.getOptions()));
				// 필수 추가정보
				itemInfo.setTextOption(orderItem.getTextOption());

				itemInfo.setItemReturnFlag(orderItem.getItemReturnFlag());
				itemInfo.setItemId(orderItem.getItemId());
				itemInfo.setQuantity(orderItem.getQuantity());
				itemInfo.setItemAmount(orderItem.getItemAmount());
				itemInfo.setOrderStatus(orderItem.getOrderStatus());
				itemInfo.setOrderStatusLabel(orderItem.getOrderStatusLabel());
				itemInfo.setClaimRefusalReasonText(orderItem.getClaimRefusalReasonText());

				itemInfo.setItemSequence(orderItem.getItemSequence());
				itemInfo.setShippingSequence(orderItem.getShippingSequence());

				itemInfo.setBrand(orderItem.getBrand());
				itemInfo.setLocgovNm(orderItem.getLocgovNm());

				itemInfo.setDeliveryCompanyId(orderItem.getDeliveryCompanyId());
				itemInfo.setDeliveryCompanyName(orderItem.getDeliveryCompanyName());
				itemInfo.setDeliveryCompanyUrl(orderItem.getDeliveryCompanyUrl());
				itemInfo.setDeliveryNumber(orderItem.getDeliveryNumber());

				itemInfo.setFreeGiftName(saleson.common.utils.CommonUtils.dataNvl(orderItem.getFreeGiftName()));
				itemInfo.setFreeGiftItemList(ShopUtils.conventOrderGiftItemInfoList(orderItem.getOrderGiftItemList()));
				itemInfo.setFreeGiftItemText(ShopUtils.makeOrderGiftItemText(orderItem.getOrderGiftItemList()));

				itemInfo.setSetItemFlag(orderItem.getSetItemFlag());
				itemInfo.setMobileItemYn(orderItem.getMobileItemYn());
				itemInfo.setCancelClaimStatus(orderItem.getCancelClaimStatus());
				itemInfo.setCancelRefusalReasonText(orderItem.getCancelRefusalReasonText());

				itemInfo.setTelephoneNumber(orderItem.getTelephoneNumber());
				itemInfo.setPhoneNumber(orderItem.getPhoneNumber());;

				items.add(itemInfo);
			}
			order.setItems(items);
			resultList.add(order);
		}
		return resultList;
	}

	@Override
	public OrderDetail getApiOrderDetail(OrderParam orderParam) {
		OrderDetail OrderDetail = null;
		Order order = getByParam(orderParam);
		if (order == null) {
			return null;
		}

		/* 교환, 반품 거절 사유 상품별로 불러오기 */
		for (OrderShippingInfo shippingInfos : order.getOrderShippingInfos()) {
			for (OrderItem orderItem : shippingInfos.getOrderItems()) {
				if ("59".equals(orderItem.getOrderStatus())) { //교환 거절
					orderItem.setClaimRefusalReasonText(orderClaimApplyMapper.getClaimRefusalReasonText(orderItem));
				} else if ("69".equals(orderItem.getOrderStatus())) {
					orderItem.setClaimRefusalReasonText(orderClaimApplyMapper.getClaimRefusalReasonText(orderItem));
				}
			}
		}

		bindOrder(order, orderParam);
		setOrderItemOther(order);
		OrderDetail = orderDetailDataSet(order);

		return OrderDetail;
	}

	private OrderDetail orderDetailDataSet(Order order){
		OrderDetail orderDetail = new OrderDetail();
		ShippingInfo shippingInfo = new ShippingInfo();
		List<PaymentInfo> paymentList = new ArrayList<>();

		List<ItemInfo> item = new ArrayList<>();

		// 주문정보
		orderDetail.setOrderCode(order.getOrderCode());                     // 주문코드
		orderDetail.setCreatedDate(order.getCreatedDate());                   // 주문일자

		orderDetail.setTotalItemAmount(order.getTotalItemAmount());               // 상품금액
		orderDetail.setTotalShippingAmount(order.getTotalShippingAmount());           // 배송비
		orderDetail.setTotalDiscountAmount(order.getTotalDiscountAmount());           // 할인금액
		orderDetail.setTotalSetDiscountAmount(order.getTotalSetDiscountAmount());		// 세트할인금액
		orderDetail.setTotalCouponDiscountAmount(order.getTotalCouponDiscountAmount());     // 쿠폰할인금액
		orderDetail.setTotalUserLevelDiscountAmount(order.getTotalUserLevelDiscountAmount());  // 회원할인금액
		orderDetail.setTotalCancelAmount(order.getTotalCancelAmount());		// 취소포인트
		orderDetail.setTotalOrderAmount(order.getTotalOrderAmount()-order.getTotalCancelAmount());              // 총 결제금액
		orderDetail.setUserName(order.getUserName());		// 회원명
		orderDetail.setLoginId(order.getLoginId());			// 로그인ID
		orderDetail.setLocgovNm(order.getLocgovNm());		// 지자체
		orderDetail.setMobile(order.getMobile());			// 전화번호

		OrderShippingInfo orderShippingInfo = order.getOrderShippingInfos().get(0);

		// OrderPayment orderPayment = order.getOrderPayments().get(0);

		// 배송정보
		shippingInfo.setReceiveName(orderShippingInfo.getReceiveName());     // 받으시는분
		shippingInfo.setReceiveMobile(orderShippingInfo.getReceiveMobile());   // 휴대폰번호
		shippingInfo.setReceivePhone(orderShippingInfo.getReceivePhone());    // 전화번호
		shippingInfo.setMemo(orderShippingInfo.getMemo());            // 배송시 요구사항
		shippingInfo.setReceiveNewZipcode(orderShippingInfo.getReceiveNewZipcode());// 신주소 우편번호
		shippingInfo.setReceiveZipcode(orderShippingInfo.getReceiveZipcode());  // 구주소 우편번호
		shippingInfo.setReceiveAddress(orderShippingInfo.getReceiveAddress());  // 주소
		shippingInfo.setReceiveAddressDetail(orderShippingInfo.getReceiveAddressDetail());// 상세주소
		shippingInfo.setPayShipping(orderShippingInfo.getOrderItems().get(0).getOrderShipping().getPayShipping());     // 배송비(0원 일 시 무료)

		List<OrderItem> orderItems = order.getOrderShippingInfos().get(0).getOrderItems();
		for (OrderItem orderItem : orderItems) {
			ItemInfo itemInfo = new ItemInfo();
			itemInfo.setItemId(orderItem.getItemId());
			itemInfo.setItemUserCode(orderItem.getItemUserCode());
			itemInfo.setImageSrc(ShopUtils.loadImage(orderItem.getImageSrc(), "M"));
			itemInfo.setItemName(orderItem.getItemName());
			itemInfo.setOptions(ShopUtils.viewItemOptions(orderItem.getSetItemFlag(), orderItem.getOptions()));
			// 필수 추가정보
			itemInfo.setTextOption(orderItem.getTextOption());

			itemInfo.setItemReturnFlag(orderItem.getItemReturnFlag());

			itemInfo.setItemSequence(orderItem.getItemSequence());
			itemInfo.setShippingSequence(orderItem.getShippingSequence());

			itemInfo.setEarnPoint(orderItem.getEarnPoint());
			itemInfo.setEarnPointFlag(orderItem.getEarnPointFlag());

			itemInfo.setQuantity(orderItem.getQuantity());
			itemInfo.setItemAmount(orderItem.getItemAmount());
			itemInfo.setOrderStatus(orderItem.getOrderStatus());
			itemInfo.setOrderStatusLabel(orderItem.getOrderStatusLabel());
			itemInfo.setClaimRefusalReasonText(orderItem.getClaimRefusalReasonText());

			itemInfo.setDeliveryCompanyId(orderItem.getDeliveryCompanyId());
			itemInfo.setDeliveryCompanyName(orderItem.getDeliveryCompanyName());
			itemInfo.setDeliveryCompanyUrl(orderItem.getDeliveryCompanyUrl());
			itemInfo.setDeliveryNumber(orderItem.getDeliveryNumber());

			itemInfo.setRealShipping(orderItem.getOrderShipping().getRealShipping());

			itemInfo.setFreeGiftName(saleson.common.utils.CommonUtils.dataNvl(orderItem.getFreeGiftName()));
			itemInfo.setFreeGiftItemList(ShopUtils.conventOrderGiftItemInfoList(orderItem.getOrderGiftItemList()));
			itemInfo.setFreeGiftItemText(ShopUtils.makeOrderGiftItemText(orderItem.getOrderGiftItemList()));

			itemInfo.setSetItemFlag(orderItem.getSetItemFlag());
			itemInfo.setLocgovNm(orderItem.getLocgovNm());
			itemInfo.setLocgovCode(orderItem.getLocgovCode());
			itemInfo.setMobileItemYn(orderItem.getMobileItemYn());
			itemInfo.setCancelClaimStatus(orderItem.getCancelClaimStatus());
			itemInfo.setCancelRefusalReasonText(orderItem.getCancelRefusalReasonText());

			itemInfo.setCompanyName(orderItem.getCompanyName());

			item.add(itemInfo);
		}

		List<OrderPayment> orderPayments = order.getOrderPayments();
		if (!orderPayments.isEmpty()) {
			for (OrderPayment orderPayment : orderPayments) {
				PaymentInfo paymentInfo = new PaymentInfo();
				paymentInfo.setApprovalType(orderPayment.getApprovalType());     // 결제수단
				paymentInfo.setApprovalTypeLabel(orderPayment.getApprovalTypeLabel());
				paymentInfo.setPaymentType(orderPayment.getPaymentType());      // 결제구분 (1:결제, 2:취소)
				paymentInfo.setAmount(orderPayment.getAmount());           // 금액
				paymentInfo.setRemainingAmount(orderPayment.getRemainingAmount());  // 잔여액
				paymentInfo.setCancelAmount(orderPayment.getCancelAmount());     // 취소금액
				paymentInfo.setPayDate(orderPayment.getPayDate());          // 결제일
				paymentInfo.setBankVirtualNo(orderPayment.getBankVirtualNo());    // 계좌번호
				paymentInfo.setBankInName(orderPayment.getBankInName());       // 입금예정자
				paymentInfo.setBankDate(orderPayment.getBankDate());       // 입금예정일(만료일)
				paymentInfo.setPayInfo(orderPayment.getPayInfo());			// 결제정보
				paymentList.add(paymentInfo);
			}
		}
		orderDetail.setShippingInfo(shippingInfo);
		orderDetail.setPaymentList(paymentList);
		orderDetail.setItem(item);

		return orderDetail;
	}

	@Override
	public OrderDetail getApiBuyForStep1(OrderParam orderParam) {
		OrderDetail result = null;
		Buy buy = this.getOrderTemp(orderParam);
		UserDelivery defaultUserDelivery = userDeliveryService.getDefaultUserDelivery();
		if (buy == null) {

			buy = new Buy();
			Buyer buyer = new Buyer();
			Receiver receiver = new Receiver();

			boolean isDeliverySet = false;
			if (defaultUserDelivery != null) {
				isDeliverySet = true;
//				receiver.setReceiveCompanyName(defaultUserDelivery.getCompanyName());
				receiver.setReceiveName(defaultUserDelivery.getUserName());
				receiver.setReceiveMobile(defaultUserDelivery.getMobile());
				receiver.setReceivePhone(defaultUserDelivery.getPhone());
				receiver.setReceiveNewZipcode(defaultUserDelivery.getNewZipcode());
				receiver.setReceiveZipcode(defaultUserDelivery.getZipcode());
				receiver.setReceiveAddress(defaultUserDelivery.getAddress());
				receiver.setReceiveAddressDetail(defaultUserDelivery.getAddressDetail());
				receiver.setReceiveSido(defaultUserDelivery.getSido());
				receiver.setReceiveSigungu(defaultUserDelivery.getSigungu());
				receiver.setReceiveEupmyeondong(defaultUserDelivery.getEupmyeondong());
			}

			// 회원 로그인 상태일경우 회원 정보를 가져옴..
			if (UserUtils.isUserLogin()) {

				User user = UserUtils.getUser();
				UserDetail userDetail = userService.getUserDetail(user.getUserId());
				//UserDetail userDetail = (UserDetail) user.getUserDetail();

				buyer.setUserName(user.getUserName());
				if (isDeliverySet == false) {
					receiver.setReceiveName(user.getUserName());
				}

				buyer.setEmail(user.getEmail());
				if (userDetail != null) {
//					buyer.setCompanyName(userDetail.getCompanyName());

					buyer.setMobile(userDetail.getPhoneNumber());
					buyer.setPhone(userDetail.getTelNumber());
					buyer.setNewZipcode(userDetail.getNewPost());
					buyer.setZipcode(userDetail.getPost());
					buyer.setZipcode1(userDetail.getPost1());
					buyer.setZipcode2(userDetail.getPost2());
					buyer.setAddress(userDetail.getAddress());
					buyer.setAddressDetail(userDetail.getAddressDetail());

					buyer.setSido(ShopUtils.getSido(userDetail.getAddress()));
					buyer.setSigungu(ShopUtils.getSigungu(userDetail.getAddress()));
					buyer.setEupmyeondong(ShopUtils.getEupmyeondong(userDetail.getAddress()));

					if (isDeliverySet == false) {
						//receiver.setReceiveCompanyName(userDetail.getCompanyName());
						receiver.setReceiveMobile(userDetail.getPhoneNumber());
						receiver.setReceivePhone(userDetail.getTelNumber());
						receiver.setReceiveNewZipcode(userDetail.getNewPost());
						receiver.setReceiveZipcode(userDetail.getPost());
						receiver.setReceiveZipcode1(userDetail.getPost1());
						receiver.setReceiveZipcode2(userDetail.getPost2());
						receiver.setReceiveAddress(userDetail.getAddress());

						receiver.setReceiveSido(ShopUtils.getSido(userDetail.getAddress()));
						receiver.setReceiveSigungu(ShopUtils.getSigungu(userDetail.getAddress()));
						receiver.setReceiveEupmyeondong(ShopUtils.getEupmyeondong(userDetail.getAddress()));

						receiver.setReceiveAddressDetail(userDetail.getAddressDetail());
					}
				}


			}

			buy.setUserId(orderParam.getUserId());
			buy.setSessionId(orderParam.getSessionId());
			buy.setBuyer(buyer);

			// step1에 들어올때는 배송지 정보가 1개임
			List<Receiver> receivers = new ArrayList<>();
			receiver.setShippingIndex(0);
			receivers.add(receiver);
			buy.setReceivers(receivers);
		}

		List<BuyItem> items = this.getOrderItemTempList(orderParam);
		if (items == null) {
			throw new OrderException("결제가능 상품이 없습니다.", "/cart");
		}

		// CJH 추가 구성품이 있는경우 복수배송지 선택 불가
		boolean isAdditionItem = false;
		for(BuyItem item : items) {
			if ("Y".equals(item.getAdditionItemFlag())) {
				isAdditionItem = true;
			}
		}

		buy.setAdditionItem(isAdditionItem);


		HashMap<Long, String> sellerMap = new HashMap<>();
		for(Receiver receiver : buy.getReceivers()) {

			receiver.setItems(items);

			// 상품쿠폰 적용
			receiver.itemCouponUsed(false, buy, receiver.getShippingIndex());

			// 배송지별 구매상품 초기화
			List<BuyQuantity> buyQuantitys = new ArrayList<>();
			for(BuyItem buyItem : items) {
				BuyQuantity buyQuantity = new BuyQuantity();
				buyQuantity.setItemSequence(buyItem.getItemSequence());
				buyQuantity.setQuantity(buyItem.getItemPrice().getQuantity());
				buyQuantitys.add(buyQuantity);

				// 개인정보 제 3자 제공 및 수집 이용동의
				if ("2".equals(buyItem.getItem().getShippingType())) {
					Seller seller = sellerMapper.getSellerById(buyItem.getSellerId());
					if (seller != null) {
						sellerMap.put(seller.getSellerId(), seller.getSellerName());
					}
				}
			}

			receiver.setBuyQuantitys(buyQuantitys);

			String islandType = "";
			if (ObjectUtils.isEmpty(receiver.getReceiveZipcode()) == false) {
				islandType = orderMapper.getIslandTypeByZipcode(receiver.getReceiveZipcode());
			}

			receiver.setShipping(islandType);
		}

		String sellerNames = "";
		for(Long sellerId : sellerMap.keySet()) {
			sellerNames += (ObjectUtils.isEmpty(sellerNames) ? "" : ", ") + sellerMap.get(sellerId);
		}
		buy.setSellerNames(sellerNames);

		// 결제금액 재계산
		buy.setOrderPrice(0, configService.getShopConfig(Config.SHOP_CONFIG_ID));

		// 회원 사용가능 포인트 조회
		buy.setRetentionPoint(this.getRetentionPoint(orderParam.getUserId(), 0));

		// 회원 사용가능 배송비 할인 쿠폰
		buy.setShippingCoupon(this.getShippingCoupon(orderParam.getUserId(), 0));

		// 회원 기본배송지 정보 저장
		buy.setDefaultUserDelivery(defaultUserDelivery);

		result = orderPaymentInfoSet(buy);
		return result;
	}

	private OrderDetail orderPaymentInfoSet(Buy buy){
		OrderDetail orderDetail = new OrderDetail();
		List<ItemInfo> items = new ArrayList<>();       // 상품정보
		ShippingInfo shippingInfo = new ShippingInfo(); // 배송정보
		PaymentInfo paymentInfo = new PaymentInfo();    // 결제정보
		List<BuyShippingCouponInfo> shippingCouponList = new ArrayList<>(); // 배송비쿠폰 리스트
		Receiver receiver = buy.getReceivers().get(0);

		orderDetail.setOrderCode("");
		orderDetail.setCreatedDate("");
		orderDetail.setUserName(buy.getBuyer().getUserName()); // 주문자
		orderDetail.setMobile(buy.getBuyer().getMobile()); // 연락처

		// 결제금액정보
		orderDetail.setOrderPrice(buy.getOrderPrice());
		orderDetail.setBuyQuantitys(receiver.getBuyQuantitys());

		// singleShipping == true 면 shipping.buyItem / 아니면 shipping.buyItems
		// 개별배송상품여부 isSingleShipping

		// 상품정보
		if (receiver != null && !receiver.getItemGroups().isEmpty()) {
			for (Shipping shipping : receiver.getItemGroups()) {
				if (shipping.getBuyItems() != null && !shipping.getBuyItems().isEmpty()) {
					for (BuyItem buyItem : shipping.getBuyItems()) {
						ItemInfo item = new ItemInfo();

						item.setItemName(buyItem.getItemName());     		// 상품명
						item.setItemUserCode(buyItem.getItemUserCode());	// 상품코드
						item.setItemId(buyItem.getItemId());        		// 상품아이디

						if (buyItem.getOptionList() != null && !buyItem.getOptionList().isEmpty()) {
							// 상품옵션 ID
							item.setOptionId(buyItem.getOptionList().get(0).getItemOptionId());
						}

						item.setImageSrc(ShopUtils.loadImage(buyItem.getItemUserCode(), buyItem.getItem().getItemImage(), "M"));  // 상품이미지
						if(buyItem != null && buyItem.getItemPrice() != null) {
							item.setQuantity(buyItem.getItemPrice().getQuantity());
						}
						if(buyItem != null && buyItem.getItemPrice() != null) {
							item.setItemAmount(buyItem.getItemPrice().getSumPrice());
						}
						item.setOrderStatus("");
						item.setClaimRefusalReasonText("");

						item.setOrderSequence(buyItem.getOrderSequence());
						item.setItemSequence(buyItem.getItemSequence());
						item.setShippingSequence(buyItem.getShippingSequence());
						item.setShippingInfoSequence(buyItem.getShippingInfoSequence());

						/*item.setUseIslandFlag(buyItem.getItem().getUseIslandFlag());*/

						item.setShippingItemCount(shipping.getShippingItemCount());	// 박스당 제한수량
						item.setShippingFreeAmount(shipping.getShippingFreeAmount()); // 조건부 무료배송금액
						item.setShippingExtraCharge1(shipping.getShippingExtraCharge1()); // 제주도 추가 배송비
						item.setShippingExtraCharge2(shipping.getShippingExtraCharge2()); //도서산간 추가배송비
						item.setShipping(shipping.getShipping()); // 배송비
						item.setShippingType(shipping.getShippingType()); // 배송비구분(1: 무료배송, 2: 판매자조건부, 3:출고지조건부, 4:상품조건부, 5:개당배송비, 6:고정배송비)

						if(buyItem.getItemPrice() != null) {
							item.setBaseAmountForShipping(buyItem.getItemPrice().getBaseAmountForShipping());
						}

						item.setRealShipping(shipping.getRealShipping());
						item.setShippingGroupCode(shipping.getShippingGroupCode());
						item.setSingleShipping(shipping.isSingleShipping());

						// 사은품
						item.setFreeGiftName(buyItem.getFreeGiftName());
						item.setFreeGiftItemList(buyItem.getFreeGiftItemList());
						item.setFreeGiftItemText(buyItem.getFreeGiftItemText());

						items.add(item);

						BuyShippingCouponInfo couponInfo = new BuyShippingCouponInfo();
						couponInfo.setShippingGroupCode(shipping.getShippingGroupCode());
						couponInfo.setShippingPaymentType(shipping.getShippingPaymentType());
						couponInfo.setShippingSequence(shipping.getShippingSequence());
						couponInfo.setShippingType(shipping.getShippingType());
						couponInfo.setItemId(buyItem.getItemId());
						couponInfo.setOptionId(item.getOptionId());
						shippingCouponList.add(couponInfo);
					}
				} else {
					BuyItem buyItem = shipping.getBuyItem();
					ItemInfo item = new ItemInfo();

					item.setItemName(buyItem.getItemName());     	 	// 상품명
					item.setItemUserCode(buyItem.getItemUserCode());  	// 상품코드
					item.setItemId(buyItem.getItemId());       			// 상품아이디

					if (buyItem.getOptionList() != null && !buyItem.getOptionList().isEmpty()) {
						// 상품옵션 ID
						item.setOptionId(buyItem.getOptionList().get(0).getItemOptionId());
					}

					item.setImageSrc(ShopUtils.loadImage(buyItem.getItemUserCode(), buyItem.getItem().getItemImage(), "M"));  // 상품이미지
					if(buyItem != null && buyItem.getItemPrice() != null) {
						item.setQuantity(buyItem.getItemPrice().getQuantity());
					}
					if(buyItem != null && buyItem.getItemPrice() != null) {
						item.setItemAmount(buyItem.getItemPrice().getSumPrice());
					}
					item.setOrderStatus("");
					item.setClaimRefusalReasonText("");

					item.setOrderSequence(buyItem.getOrderSequence());
					item.setItemSequence(buyItem.getItemSequence());
					item.setShippingSequence(buyItem.getShippingSequence());
					item.setShippingInfoSequence(buyItem.getShippingInfoSequence());

					/*item.setUseIslandFlag(buyItem.getItem().getUseIslandFlag());*/

					item.setShippingItemCount(shipping.getShippingItemCount());	// 박스당 제한수량
					item.setShippingFreeAmount(shipping.getShippingFreeAmount()); // 조건부 무료배송금액
					item.setShippingExtraCharge1(shipping.getShippingExtraCharge1()); // 제주도 추가 배송비
					item.setShippingExtraCharge2(shipping.getShippingExtraCharge2()); //도서산간 추가배송비
					item.setShipping(shipping.getShipping()); // 배송비
					item.setShippingType(shipping.getShippingType()); // 배송비구분(1: 무료배송, 2: 판매자조건부, 3:출고지조건부, 4:상품조건부, 5:개당배송비, 6:고정배송비)

					if(buyItem != null && buyItem.getItemPrice() != null) {
						item.setBaseAmountForShipping(buyItem.getItemPrice().getBaseAmountForShipping());
					}

					item.setRealShipping(shipping.getRealShipping());
					item.setShippingGroupCode(shipping.getShippingGroupCode());
					item.setSingleShipping(shipping.isSingleShipping());

					// 사은품
					item.setFreeGiftName(buyItem.getFreeGiftName());
					item.setFreeGiftItemList(buyItem.getFreeGiftItemList());
					item.setFreeGiftItemText(buyItem.getFreeGiftItemText());

					items.add(item);

					// 배송비 쿠폰 세팅
					BuyShippingCouponInfo couponInfo = new BuyShippingCouponInfo();
					couponInfo.setShippingGroupCode(shipping.getShippingGroupCode());
					couponInfo.setShippingPaymentType(shipping.getShippingPaymentType());
					couponInfo.setShippingSequence(shipping.getShippingSequence());
					couponInfo.setShippingType(shipping.getShippingType());
					couponInfo.setItemId(buyItem.getItemId());
					couponInfo.setOptionId(item.getOptionId());
					shippingCouponList.add(couponInfo);
				}
			}
		}

		/*
			sh.getShippingFreeAmount(); // 조건부 무료배송금액
			sh.getShippingExtraCharge1();// 제주도 추가 배송비
			sh.getShippingExtraCharge2();//도서산간 추가배송비
			sh.getShipping();   // 배송비
			sh.getShippingType();   // 배송비구분(1: 무료배송, 2: 판매자조건부, 3:출고지조건부, 4:상품조건부, 5:개당배송비, 6:고정배송비)
		*/
/*

		// 배송정보
		shippingInfo.setReceiveName(JJgUtils.dataNvl(receiver.getReceiveName())); // 배송받는사람
		shippingInfo.setReceiveSido(JJgUtils.dataNvl(receiver.getReceiveSido())); // 시도
		shippingInfo.setReceiveSigungu(JJgUtils.dataNvl(receiver.getReceiveSigungu())); // 시군구
		shippingInfo.setReceiveEupmyeondong(JJgUtils.dataNvl(receiver.getReceiveEupmyeondong())); // 읍면동
		shippingInfo.setReceiveZipcode(JJgUtils.dataNvl(receiver.getReceiveZipcode())); // 구 주소 우편번호
		shippingInfo.setReceiveNewZipcode(JJgUtils.dataNvl(receiver.getReceiveNewZipcode())); // 신 주소 우편번호
		shippingInfo.setReceiveAddress(JJgUtils.dataNvl(receiver.getReceiveAddress())); // 주소
		shippingInfo.setReceiveAddressDetail(JJgUtils.dataNvl(receiver.getReceiveAddressDetail())); // 상세주소
		shippingInfo.setReceiveMobile1(JJgUtils.dataNvl(receiver.getReceiveMobile1()));   // 휴대폰 번호 1
		shippingInfo.setReceiveMobile2(JJgUtils.dataNvl(receiver.getReceiveMobile2()));   // 휴대폰 번호 2
		shippingInfo.setReceiveMobile3(JJgUtils.dataNvl(receiver.getReceiveMobile3()));   // 휴대폰 번호 3
		shippingInfo.setMemo(JJgUtils.dataNvl(receiver.getContent())); // 배송시 요구사항

		if(!(shippingInfo.getReceiveMobile1().equals("") && shippingInfo.getReceiveMobile2().equals("") && shippingInfo.getReceiveMobile3().equals(""))){
			shippingInfo.setReceiveMobile(shippingInfo.getReceiveMobile1()+"-"+shippingInfo.getReceiveMobile2()+"-"+shippingInfo.getReceiveMobile3());
		} else {
			shippingInfo.setReceiveMobile("");
		}
*/

		shippingInfo.setReceivePhone("");

		// 할인정보
		orderDetail.setRetentionPoint(buy.getRetentionPoint()); // 보유포인트(적립금)

		// 토탈
/*
- totalItemSaleAmount : 34,700원 : 총 상품 할인 금액
- totalShippingAmount : 2,500원 : 총 배송비
- orderPayAmount : 37,200원 : 총 결제 예정 금액
- payAmount : 37,200원 : 지불 금액 (총 결제 예정 금액 동일)
- orderPayAmountTotal : 37,200원 :총 주문 금액
* */
		// 상품금액
		orderDetail.setTotalItemAmount(buy.getOrderPrice().getTotalItemPrice());
		// 할인금액
		orderDetail.setTotalDiscountAmount(buy.getOrderPrice().getTotalDiscountAmount());
		// 배송비
		orderDetail.setTotalShippingAmount(buy.getOrderPrice().getTotalShippingAmount());
		// 결제예정금액
		orderDetail.setTotalOrderAmount(buy.getOrderPrice().getOrderPayAmount());

		buy.getOrderPrice().getTotalItemCouponDiscountAmount();
		buy.getOrderPrice().getTotalCartCouponDiscountAmount();
		buy.getOrderPrice().getTotalCouponDiscountAmount();
		buy.getOrderPrice().getTotalPointDiscountAmount();
		buy.getOrderPrice().getTotalShippingCouponUseCount();
		buy.getOrderPrice().getTotalShippingCouponDiscountAmount();
		buy.getOrderPrice().getTotalItemSaleAmount();
		buy.getOrderPrice().getTotalShippingAmount();
		buy.getOrderPrice().getOrderPayAmount();
		buy.getOrderPrice().getOrderPayAmountTotal();
		buy.getOrderPrice().getPayAmount();
		buy.getOrderPrice().getTotalUserLevelDiscountAmount();

		// 결제정보
		orderDetail.setItem(items);
		orderDetail.setShippingInfo(shippingInfo);

		// 결제정보 임시 초기화
		paymentInfo.setApprovalType("");
		paymentInfo.setPaymentType("");
		paymentInfo.setAmount(0);
		paymentInfo.setRemainingAmount(0);
		paymentInfo.setCancelAmount(0);
		paymentInfo.setPayDate("");
		paymentInfo.setBankVirtualNo("");
		paymentInfo.setBankInName("");

		orderDetail.setShippingIndex(buy.getReceivers().get(0).getShippingIndex());

		// 적용가능 쿠폰 리스트 설정
		orderDetail.setCouponList(apiOrderCouponData(getOrderCouponData(buy)));

		// 배송지 쿠폰
		orderDetail.setShippingCoupon(buy.getShippingCoupon());
		orderDetail.setShippingCouponList(shippingCouponList);

		orderDetail.setPaymentInfo(paymentInfo);

		return orderDetail;
	}

	private List<BuyCouponList> apiOrderCouponData(Buy buy){
		List<BuyCouponList> resultList = new ArrayList<>();

		if (buy == null) {
			return resultList;
		}

		List<OrderCoupon> couponList = new ArrayList<>();
		// buy.getReceivers().get(0).getItemGroups().get(0).getBuyItem().getItemCoupons()
		Receiver receiver = buy.getReceivers().get(0);
		if(receiver.getItemGroups().size() > 0){
			boolean singleShipping = false;
			for(int i = 0; i < receiver.getItemGroups().size(); i++){
				BuyCouponList bcListInfo = null;
				singleShipping = receiver.getItemGroups().get(i).isSingleShipping();
				BuyItem bi = null;
				if(singleShipping){
					bcListInfo = new BuyCouponList();
					couponList = new ArrayList<>();
					bi = receiver.getItemGroups().get(i).getBuyItem();
					if(bi.getItemCoupons() != null && bi.getItemCoupons().size() > 0){
						for(int ij = 0; ij < bi.getItemCoupons().size(); ij++){
							couponList.add(bi.getItemCoupons().get(ij));
						}
					}
					bcListInfo.setItemId(bi.getItemId());
					if(bi.getOptionList() != null){
						bcListInfo.setItemOptionId(bi.getOptionList().get(0).getItemOptionId());
					} else {
						bcListInfo.setItemOptionId(0);
					}
					bcListInfo.setCouponList(couponList);
					resultList.add(bcListInfo);
				} else {
					List<BuyItem> jjbi = receiver.getItemGroups().get(i).getBuyItems();
					for(int j = 0; j < jjbi.size(); j++){
						bcListInfo = new BuyCouponList();
						couponList = new ArrayList<>();
						bi = jjbi.get(j);
						if(bi.getItemCoupons() != null && bi.getItemCoupons().size() > 0){
							for(int ij = 0; ij < bi.getItemCoupons().size(); ij++){
								couponList.add(bi.getItemCoupons().get(ij));
							}
						}
						bcListInfo.setItemId(bi.getItemId());
						if(bi.getOptionList() != null){
							bcListInfo.setItemOptionId(bi.getOptionList().get(0).getItemOptionId());
						} else {
							bcListInfo.setItemOptionId(0);
						}

						bcListInfo.setCouponList(couponList);
						resultList.add(bcListInfo);
					}
				}
			}
		}
		return resultList;
	}
	@Override
	public ReturnApplyInfo getReturnApplyInfo(OrderParam orderParam) {

		ReturnApplyInfo info = new ReturnApplyInfo();
		ReturnApply returnApply = new ReturnApply();
		OrderItem orderItem = getOrderItemByParam(orderParam);

		List<OrderPayment> list = getOrderPaymentListByParam(orderParam);

		boolean paymentType = list.stream().anyMatch(
				type -> type.getApprovalType().equals("bank")
						|| type.getApprovalType().equals("vbank"));

		info.setPaymentType(paymentType);

		if (orderItem == null) {
			throw new PageNotFoundException();
		}

		if (orderItem != null) {
			List<OrderGiftItem> orderGiftItemList = orderGiftItemService.getOrderGiftItemListByOrderCode(orderItem.getOrderCode());
			setOrderGiftItemForOrderItem(orderGiftItemList, orderItem);
			orderItem.setOptions(ShopUtils.viewItemOptions(orderItem.getSetItemFlag(), orderItem.getOptions()));
		}

		if (!ShopUtils.checkOrderStatusChange("return", orderItem.getOrderStatus())) {
			throw new OrderException("상품을 환불 신청 하실수 있는 상태가 아닙니다.");
		}

		orderParam.setShippingInfoSequence(orderItem.getShippingInfoSequence());
		OrderShippingInfo osInfo = getOrderShippingInfoByParam(orderParam);
		returnApply.setReturnApply(osInfo);
		returnApply.setOrderItem(orderItem);
		returnApply.setApplyQuantity(orderItem.getQuantity());
		returnApply.setShipmentReturnId(orderItem.getShipmentReturnId());

		if (!ObjectUtils.isEmpty(returnApply.getReturnReservePhone()) && !returnApply.getReturnReservePhone().equals("--")) {
			returnApply.setReturnReservePhone1(returnApply.getReturnReservePhone().split("-")[0]);
		}

		if (!ObjectUtils.isEmpty(returnApply.getReturnReserveMobile()) && !returnApply.getReturnReserveMobile().equals("--")) {
			returnApply.setReturnReserveMobile1(returnApply.getReturnReserveMobile().split("-")[0]);
		}

		info.setDeliveryCompanyList(deliveryCompanyMapper.getActiveDeliveryCompanyListAll());
		info.setClaimReasons(CodeUtils.getCodeInfoList("RETURN_REASON"));
		info.setReturnApply(returnApply);

		return info;
	}

	private List<OrderPayment> getOrderPaymentListByParam(OrderParam orderParam) {
		return orderSupporter.getOrderPaymentListByParam(orderParam);
	}

	@Override
	public ExchangeApplyInfo getExchangeApplyInfo(OrderParam orderParam) {
		ExchangeApplyInfo applyInfo = new ExchangeApplyInfo();
		ExchangeApply exchangeApply = new ExchangeApply();

		OrderItem orderItem = getOrderItemByParam(orderParam);
		if (orderItem == null) {
			throw new PageNotFoundException();
		}

		if (orderItem != null) {
			List<OrderGiftItem> orderGiftItemList = orderGiftItemService.getOrderGiftItemListByOrderCode(orderItem.getOrderCode());
			setOrderGiftItemForOrderItem(orderGiftItemList, orderItem);
			orderItem.setOptions(ShopUtils.viewItemOptions(orderItem.getSetItemFlag(), orderItem.getOptions()));
		}

		if (!ShopUtils.checkOrderStatusChange("exchange", orderItem.getOrderStatus())) {
			throw new OrderException("상품을 교환 신청 하실수 있는 상태가 아닙니다.");
		}

		orderParam.setShippingInfoSequence(orderItem.getShippingInfoSequence());
		OrderShippingInfo info = getOrderShippingInfoByParam(orderParam);
		exchangeApply.setExchangeApply(info);
		exchangeApply.setOrderItem(orderItem);
		exchangeApply.setApplyQuantity(orderItem.getQuantity());
		exchangeApply.setShipmentReturnId(orderItem.getShipmentReturnId());

		if(!ObjectUtils.isEmpty(exchangeApply.getExchangeReceivePhone()) && !exchangeApply.getExchangeReceivePhone().equals("--")) {
			//exchangeApply.setExchangeReceiveMobile1(exchangeApply.getExchangeReceivePhone().split("-")[0]);
		}

		if(!ObjectUtils.isEmpty(exchangeApply.getExchangeReceiveMobile()) && !exchangeApply.getExchangeReceiveMobile().equals("--")) {
			exchangeApply.setExchangeReceiveMobile1(exchangeApply.getExchangeReceiveMobile().split("-")[0]);
		}

		applyInfo.setDeliveryCompanyList(deliveryCompanyMapper.getActiveDeliveryCompanyListAll());
		applyInfo.setClaimReasons(CodeUtils.getCodeInfoList("EXCHANGE_REASON"));
		applyInfo.setExchangeApply(exchangeApply);

		return applyInfo;
	}

	@Override
	public CancelApplyInfo getCancelApplyInfo(OrderParam orderParam) {
		CancelApplyInfo cancelApplyInfo = new CancelApplyInfo();
		ClaimApply claimApply = new ClaimApply();

		claimApply.setItemSequence(orderParam.getItemSequence());

		Order order = getOrderByParam(orderParam);
		claimApply.setClaimType("1");
		if (order == null) {
			throw new PageNotFoundException();
		}

		// CJH 2016.11.13 사용자가 클릭하고 들어온 상품의 주문상태와 동일한것만 화면에 노출하는 조건 추가
		String userClickItemStatus = "";
		for(OrderShippingInfo info : order.getOrderShippingInfos()) {
			for(OrderItem orderItem : info.getOrderItems()) {
				if (orderItem.getItemSequence() == claimApply.getItemSequence()) {
					userClickItemStatus = orderItem.getOrderStatus();
					break;
				}
			}

			if (ObjectUtils.isEmpty(userClickItemStatus) == false) {
				break;
			}
		}

		if (ObjectUtils.isEmpty(userClickItemStatus)) {
			throw new ClaimException();
		}

		int count = 0;
		for(OrderShippingInfo info : order.getOrderShippingInfos()) {
			for(OrderItem orderItem : info.getOrderItems()) {

				// 신청
				if ("1".equals(claimApply.getClaimType())) {

					// 주문 취소 신청은 배송준비중, 결제 확인일때 가능함
					if (userClickItemStatus.equals(orderItem.getOrderStatus())) {
						orderItem.setClaimApplyFlag("Y");
						count++;

						orderItem.setClaimApplyItemKey(Integer.toString(orderItem.getItemSequence()));
					}

				} else if ("4".equals(claimApply.getClaimType())) { // CJH 2016.11.13 4번은 검증이 되지 않았습니다. 사용하지 말아주세요.

					// 즉시 주문 취소 - 결제 확인 상품만 가능
					if ("0".equals(orderItem.getOrderStatus()) || "10".equals(orderItem.getOrderStatus())) {
						//orderItem.setClaimApplyFlag("Y");
						//count++;

						orderItem.setClaimApplyItemKey(Integer.toString(orderItem.getItemSequence()));
					}
				}

				if (orderItem != null) {
					List<OrderGiftItem> orderGiftItemList = orderGiftItemService.getOrderGiftItemListByOrderCode(orderItem.getOrderCode());
					setOrderGiftItemForOrderItem(orderGiftItemList, orderItem);

					orderItem.setOptions(ShopUtils.viewItemOptions(orderItem.getSetItemFlag(), orderItem.getOptions()));
				}
			}
		}

		if (count == 0) {
			throw new PageNotFoundException();
		}

		Order claimOrder = new Order();
		claimOrder.setOrderShippingInfos(order.getOrderShippingInfos());
		claimApply.setOrder(claimOrder);

		cancelApplyInfo.setUserClickItemStatus(userClickItemStatus);
		cancelApplyInfo.setClaimReasons(CodeUtils.getCodeInfoList("CANCEL_REASON"));
		cancelApplyInfo.setClaimApply(claimApply);

		return cancelApplyInfo;
	}

	@Override
	public Buyer getBuyerByOrderCode(String orderCode) {
		return orderMapper.getBuyerByOrderCode(orderCode);
	}


	@Override
	public Order getOrderForKcpConfirm(OrderPayment orderPayment) {



		OrderParam orderParam = new OrderParam();
		orderParam.setOrderCode(orderPayment.getOrderCode());
		orderParam.setOrderSequence(orderPayment.getOrderSequence());
		orderParam.setPaymentSequence(orderPayment.getPaymentSequence());
		orderParam.setAdminUserName("system");
		orderParam.setPayAmount(orderPayment.getAmount());
		orderParam.setConditionType("OPMANAGER");

		if (orderPaymentMapper.updateConfirmationOfPaymentStep1(orderParam) > 0){
			if (orderPaymentMapper.updateConfirmationOfPaymentStep2(orderParam) > 0) {
				orderPaymentMapper.updateConfirmationOfPaymentStep3(orderParam);
				if(!orderPayment.getEscrowStatus().equals("N")){	//에스크로면 에스크로 상태 결제완료로 변경
					orderParam.setEscrowStatus("10");
					orderMapper.updateEscrowStatus(orderParam);
				}
			} else {
				throw new OrderManagerException();
			}
		} else {
			throw new OrderManagerException();
		}


		return getOrderByParam(orderParam);
	}

	@Override
	public void updateEscrowStatus(OrderParam orderParam) {
		orderMapper.updateEscrowStatus(orderParam);
	}

	@Override
	public int getNewOrderCountByParam(OrderParam orderParam) {
		return orderShippingMapper.getNewOrderCountByParam(orderParam);
	}


	// 주문 로직, 주문 임시저장과 주문저장 동시 처리(통장 입금, 네이버 페이 등 별도 결제 로직이 없으므로 바로 진행)
	@Override
//	@Transactional(rollbackFor = RuntimeException.class, isolation = Isolation.READ_COMMITTED)
	public HashMap<String, Object> saveGiveOrderTempAndPay(HttpSession session, Buy buy, HttpServletRequest request) throws RuntimeException {
		List<BuyItem> buyItems = buy.getItems();

		String createdDate = DateUtils.getToday(DATETIME_FORMAT);

		long userId = UserUtils.getUserId();

		buy.getBuyer().setNewZipcode(buy.getBuyer().getZipcode());
		buy.setCreatedDate(createdDate);
		// 결제 전 검증 및 결제정보 임시 저장
		// 지자체별 상품 분류
		List<GivePayOrder> buyLocgovs = new ArrayList<>();

		if(buyItems != null) {
			for (BuyItem buyItem : buyItems) {
				// 옵션값 디스플레이용 => 로직 체크용으로 변경
				buyItem.setOptionsDisplay(buyItem.getOptions());
				buyItem.setOptions(buyItem.getOptionsOriginal());

				if (buyItem.getLocgovCode() != null && !buyItem.getLocgovCode().trim().isEmpty()) {
					boolean existLocgov = false;
					locgov: for (GivePayOrder givePayOrder : buyLocgovs) {
						if (buyItem.getLocgovCode().equals(givePayOrder.getLocgovCode())) {
							givePayOrder.addBuyItemList(buyItem);
							existLocgov = true;
							break locgov;
						}
					}
					if (!existLocgov) {
						GivePayOrder givePayOrder = new GivePayOrder();
						givePayOrder.setLocgovCode(buyItem.getLocgovCode());
						givePayOrder.addBuyItemList(buyItem);
						buyLocgovs.add(givePayOrder);
					}
				}
			}
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(userId);
		orderParam.setViewTarget(buy.getDeviceType());

		// 데이터 검증
		List<OrderGivePoint> pointList = orderGivePointService.getGiveBlcePointListByUserId(userId);
		payAmountVerificationGivePoint(buyLocgovs, buy, orderParam, pointList);

		// 기존 주문 임시 데이터 삭제 - 주문자 정보등만 삭제 한다. * 상품 임시 데이터는 유지
		orderMapper.deleteOrderTemp(buy);

		List<BuyPayment> payments = new ArrayList<>();

		// 지자체, 상품 종류 상관없이 1개로 처리
		BuyPayment paramBuyPayment = new BuyPayment();
		paramBuyPayment.setOrderCode(buy.getOrderCode());
		paramBuyPayment.setApprovalType("give-point");
		paramBuyPayment.setServiceType("mall");
		paramBuyPayment.setTaxFreeAmount(0);
		paramBuyPayment.setCreatedDate(createdDate);
		for (GivePayOrder givePayOrder : buyLocgovs) {
			paramBuyPayment.setAmount(paramBuyPayment.getAmount() + givePayOrder.getSumPrice());
			paramBuyPayment.setTaxFreeAmount(paramBuyPayment.getTaxFreeAmount() + givePayOrder.getTaxFreeSumPrice());
		}
		orderMapper.insertOrderPaymentBuyTemp(paramBuyPayment);
		payments.add(paramBuyPayment);

		orderMapper.insertOrderTemp(buy);

		for (Receiver receiver : buy.getReceivers()) {

			receiver.setUserId(buy.getUserId());
			receiver.setOrderCode(buy.getOrderCode());
			receiver.setSessionId(buy.getSessionId());
            receiver.setCreatedDate(createdDate);

			receiver.processHyphen();
			orderMapper.insertOrderShippingBuyTemp(receiver);

			// 주문상품 복사
			for (BuyItem buyItem : receiver.getItems()) {
				buyItem.setOrderCode(buy.getOrderCode());
				buyItem.setShippingIndex(receiver.getShippingIndex());
				buyItem.setCampaignCode(buy.getCampaignCode());
				buyItem.setCreatedDate(createdDate);

				buyItem.setOrderStatus("10");		// ShopUtils.getOrderStatusLabel
				if (UserUtils.isUserLogin()) {
					buyItem.setGuestFlag("N");
				} else {
					throw new OrderException();
				}

				buyItem.setOptions(buyItem.getOptionsDisplay());		// 옵션 값 화면 표시 형태로 저장

				orderMapper.insertOrderItemBuyTemp(buyItem);

				buyItem.setOptions(buyItem.getOptionsOriginal());		// 옵션 값 로직 체크용으로 변경
			}
		}

		// 주문자 정보 기본정보로 저장 체크시 2017-05-18 yulsun.yoo
		if ("1".equals(buy.getDefaultBuyerCheck())) {
			buy.getBuyer().setUserId(buy.getUserId());
			userService.updateUserDetailForOrder(buy.getBuyer());
		}



		// 이후 결제 처리

		String orderCode = buy.getOrderCode();

		HashMap<String, Integer> buyQuantityMap = new HashMap<>();
		HashMap<String, Integer> buySetQuantityMap = new HashMap<>();
//		setOrderItemInfo(buy.getItems(), orderParam, buyQuantityMap, buySetQuantityMap);
		setOrderItemInfo(buyItems, orderParam, buyQuantityMap, buySetQuantityMap);

		if (buy.getReceivers() != null) {
			for (Receiver receiver : buy.getReceivers()) {

				// 상품쿠폰 적용
				receiver.itemCouponUsed(false, buy, receiver.getShippingIndex());

				// 구매 상품 정책별 그룹
				String zipcode = receiver.getReceiveZipcode();

				if (ObjectUtils.isEmpty(zipcode)) {
					zipcode = receiver.getReceiveZipcode();
				}

				receiver.setShipping(orderMapper.getIslandTypeByZipcode(zipcode));
			}
		}

		// 재고 차감 목록
		buy.setStockMap(buyQuantityMap);

		// 세트 재고 차감 목록
		buy.setStockSetMap(buySetQuantityMap);

		// 구매가능여부 체크
//		ShopUtils.buyVerification(buy.getItems(), buy.getItems().size());
//		buyGiveGoodsVerification(buy.getItems(), buy.getItems().size());

//		buy.setOrderPrice(bankPayAmount, configService.getShopConfig(Config.SHOP_CONFIG_ID));

		// 기부포인트 차감
		for (GivePayOrder givePayOrder : buyLocgovs) {
			long locgovAmount = givePayOrder.getSumPrice();
			OrderGivePoint param = new OrderGivePoint();
			param.setUserId(userId);
			param.setCntrLocgovCode(givePayOrder.getLocgovCode());

			List<OrderGivePoint> locgovPointList = orderGivePointService.getGiveBlcePointListByUserIdAndLocgov(param);

			locgovPay: for (OrderGivePoint orderGivePoint : locgovPointList) {
				long leftPoint = orderGivePoint.getCntrBlcePoint();
				orderGivePoint.setUserId(userId);
				orderGivePoint.setUseCn("답례품 구매");
				orderGivePoint.setUseSeCode("1");		// 1 : 사용, 2 : 소멸, 3 : 탈퇴
				orderGivePoint.setOrderCode(buy.getOrderCode());

				if (locgovAmount > leftPoint) {			// 결제할 금액이 잔여 포인트보다 클 경우
					orderGivePoint.setCntrUsePoint(leftPoint);		// 사용금액은 잔여포인트 전체
					orderGivePoint.setCntrBlcePoint(0);
					locgovAmount -= leftPoint;
					if (locgovAmount < 0) {
						throw new OrderException("답례품 주문 처리중 문제가 발생 하였습니다.");
					}
				} else {
					orderGivePoint.setCntrUsePoint(locgovAmount);		// 사용금액은 남은 결제 금액 전체
					leftPoint -= locgovAmount;
					locgovAmount = 0;
					if (leftPoint < 0) {
						throw new OrderException("기부포인트가 부족합니다.");
					}
					orderGivePoint.setCntrBlcePoint(leftPoint);
				}

				orderGivePoint.setLastUpdtPnttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
				orderGivePoint.setLastUpdusrId(UserUtils.getUser().getUserId());

				//orderGivePointService.updateGiveBlcePoint(orderGivePoint);			// 잔여포인트 업데이트 로직 제외

				orderGivePoint.setFrstRegisterId(UserUtils.getUser().getUserId());
				orderGivePointService.insertGiveUsePoint(orderGivePoint);

				// TODO :: 기부 사용포인트 이력


				if (locgovAmount == 0) {
					break locgovPay;
				}
			}
		}

		// 품절상태로 변경될 경우 장바구니에서 해당 상품 삭제
		CartParam cartParam = new CartParam();
		for (BuyItem buyItem : buyItems) {
			if (isChangeSoldoutItem(buyItem.getItemId(), buyItem.getItemPrice().getQuantity())) {
				cartParam.addItemIds(buyItem.getItemId());
			}
		}

		if (cartParam.getItemIds() != null && !cartParam.getItemIds().isEmpty()) {
			cartMapper.deleteCartByItemIds(cartParam);
		}
		// 품절상태로 변경될 경우 장바구니에서 해당 상품 삭제

		// 모바일 상품 존재할 경우 분할 작업 처리....
		int maxItemSequence = 0;
		boolean existMobileItem = false;
		boolean isAdultUser = UserUtils.isAdult();
		for (BuyItem buyItem : buyItems) {
			Item item = itemService.getItemBy(buyItem.getItemUserCode());
			buyItem.setAdultItemYn(item.getAdultItemYn());
			if (!isAdultUser && "Y".equalsIgnoreCase(item.getAdultItemYn())) {
				throw new OrderException("19세 이상 로그인 사용자만 주문 가능합니다.");
			}
			buyItem.setMobileItemYn(item.getMobileItemYn());
			if ("Y".equalsIgnoreCase(item.getMobileItemYn())) {
				existMobileItem = true;
				buyItem.setCopyMobileItemCnt(buyItem.getItemPrice().getQuantity() - 1);
				buyItem.setItemPriceQuantity(1);
//				buyItem.setCampaignCode("MOBILE");
			}
			if (maxItemSequence < buyItem.getItemSequence()) {
				maxItemSequence = buyItem.getItemSequence();
			}
			if ("2".equals(item.getTaxType())) {
				buyItem.setTaxFreeYn("Y");
			} else {
				buyItem.setTaxFreeYn("N");
			}
		}

		if (existMobileItem) {
			List<BuyItem> mobileCopyItems = new ArrayList<>();
			for (BuyItem buyItem : buyItems) {
				if ("Y".equalsIgnoreCase(buyItem.getMobileItemYn()) && buyItem.getCopyMobileItemCnt() > 0) {
					try {
						for(long l = 0 ; l < buyItem.getCopyMobileItemCnt() ; l++) {
							BuyItem copyItem = (BuyItem) buyItem.clone();
							maxItemSequence++;
							copyItem.setItemSequence(maxItemSequence);
//							copyItem.setCampaignCode("MOBILE");
							mobileCopyItems.add(copyItem);
						}
					} catch (CloneNotSupportedException e) {
						throw new OrderException("주문 답례품 처리중 문제가 발생 하였습니다.");
					}
				}
			}

			if (!mobileCopyItems.isEmpty()) {
				buyItems.addAll(mobileCopyItems);
			}
		}
		// 모바일 상품 존재할 경우 분할 작업 처리....

		int orderSequence;

		try {

			Buyer buyer = buy.getBuyer();
			buyer.setIp(saleson.common.utils.CommonUtils.getClientIp(request));
			buyer.setOrderCode(orderCode);
			buyer.setUserId(buy.getUserId());
			buyer.setLoginId(UserUtils.getLoginId());

			// 데이터 암호화
			buyer.encrypt(buyerEncryptor);

			buyer.setOrderPrice(buy.getOrderPrice());

			orderMapper.insertOrder(buyer);

			int shippingInfoSequence = 0;
//			int shippingSequence = 0;
//			int itemSequence = 0;
			orderSequence = buyer.getOrderSequence();

			for(Receiver receiver : buy.getReceivers()) {

				// 주문상품 배송지 정보를 저장
				OrderShippingInfo orderShippingInfo = new OrderShippingInfo(orderCode, orderSequence, shippingInfoSequence++, receiver);

				// 수령자 이름 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveName())) {
					throw new OrderException("받으시는분 이름을 입력해주세요.");
				}

				// 휴대폰 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveMobile())) {
					throw new OrderException("배송지 휴대폰번호를 입력해주세요.");
				}

				// 우편번호 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveZipcode()) && !existMobileItem) {
					throw new OrderException("배송지 우편번호를 입력해주세요.");
				}

				// 주소 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveAddress()) && !existMobileItem) {
					throw new OrderException("배송지 주소를 입력해주세요.");
				}

				// 상세주소 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveAddressDetail()) && !existMobileItem) {
					throw new OrderException("배송지 상세주소를 입력해주세요.");
				}

				// 데이터 암호화
				orderShippingInfo.encrypt(orderShippingInfoEncryptor);
				orderMapper.insertOrderShippingInfo(orderShippingInfo);

//				for(Shipping shipping : receiver.getItemGroups()) {		// insertOrderItem 에서 처리하도록 변경
//					shipping.setOrderCode(orderCode);
//					shipping.setOrderSequence(orderSequence);
//					shipping.setShippingSequence(shippingSequence++);
//
//					orderMapper.insertOrderShipping(shipping);
//				}

				insertOrderItemNew(buyItems, receiver.getItemGroups(), false);
			}

//			insertOrderLog(OrderLogType.ORDER_PAYMENT, orderCode, orderSequence, shippingSequence, orderCode);		// insertOrderItem 에서 처리하도록 변경

			int paymentSequence = 0;

			buy.setPayments(payments);

//			int cnt = 1;
//			int totCnt = payments.size();

			for (BuyPayment buyPayment : payments) {
//				OrderPgData orderPgData = null;

				String approvalType = buyPayment.getApprovalType();

				OrderPayment orderPayment = new OrderPayment();
				orderPayment.setOrderCode(orderCode);
				orderPayment.setOrderSequence(orderSequence);
				orderPayment.setPaymentSequence(paymentSequence++);


				orderPayment.setApprovalType(approvalType);
				orderPayment.setAmount(buyPayment.getAmount());
				orderPayment.setTaxFreeAmount(buyPayment.getTaxFreeAmount());

				orderPayment.setNowPaymentFlag("Y");		// 즉시 결제 처리

				if ("Y".equals(orderPayment.getNowPaymentFlag())) {
					orderPayment.setRemainingAmount(buyPayment.getAmount());
					orderPayment.setPayDate(DateUtils.getToday(DATETIME_FORMAT));
				}

				orderPayment.setPaymentType("1");
				orderPayment.setDeviceType(buy.getDeviceType());

				// 데이터 암호화
				orderPayment.encrypt(orderPaymentEncryptor);
				orderMapper.insertOrderPayment(orderPayment);
			}

		} catch (OrderException e) {
			throw new OrderException(e.getErrorMessage(), "/order/step1", e);
		} catch (RuntimeException e) {
			throw new OrderException("주문 처리 중 에러가 발생하여 취소되었습니다.", "/order/step1", e);
		}

		try {
			// 주문서 작성 임시 저장 정보 삭제
			orderMapper.deleteOrderItemTemp(orderParam);

			// 주문서(세트상품) 임시 저장 정보 삭제
			orderMapper.deleteOrderItemSetTemp(orderParam);

			// 주문 임시 저장 정보 삭제
			orderMapper.deleteOrderTemp(buy);
			orderMapper.deleteOrderItemBuyTemp(buy);
			orderMapper.deleteOrderShippingBuyTemp(buy);
			orderMapper.deleteOrderPaymentBuyTemp(buy);

			// 주문이 완료된 장바구니 상품들 삭제
			List<Integer> itemIds = new ArrayList<>();
			for (BuyItem buyItem : buy.getItems()) {
				itemIds.add(buyItem.getItemId());
			}

			// 장바구니 삭제
			if (!itemIds.isEmpty()) {
				cartParam = new CartParam();
				cartParam.setUserId(buy.getUserId());
				cartParam.setSessionId(buy.getSessionId());
				cartParam.setItemIds(itemIds);

				// 로컬에서는 테스트를 위해서 장바구니를 삭제 하지 않는다.
//				if (!ServiceType.LOCAL) {
					cartMapper.deleteCartByItemIds(cartParam);
//				}
			}

		} catch(RuntimeException e) {
//					log.error("주문 처리 후 임시 데이터 삭제시 ERROR: {}", e.getMessage(), e);
			log.error("주문 처리 후 임시 데이터 삭제시 ERROR: {}", getClass().getName() + " :: insertOrder RuntimeException1 ============", e);
		}

		// Message 발송
//		try {
//			orderMessageService.sendOrderMessageTx(buy);
//		} catch(RuntimeException e) {
////					log.error("주문 메시지 발송 ERROR: {}", e.getMessage(), e);
//			log.error("주문 메시지 발송 ERROR: {}", getClass().getName() + " :: insertOrder RuntimeException2 ============", e);
//		}

		// 재고 차감
		try {
			HashMap<String, Integer> stockMap = buy.getStockMap();
			HashMap<String, Integer> stockSetMap = buy.getStockSetMap();
//			if (stockMap == null) {
//				return orderSequence + "/" + orderCode;
//			}

			this.updateStockDeduction(stockMap);

			// 세트상품 재고차감
			this.updateStockDeduction(stockSetMap);
		} catch(RuntimeException e) {
//					log.error("재고 차감 ERROR: {}", e.getMessage(), e);
			log.error("재고 차감 ERROR: {}", getClass().getName() + " :: insertOrder RuntimeException3 ============", e);
		}

		// 로그인시 주소록 저장 선택시 기본 배송지 저장
		if (UserUtils.isUserLogin()) {
			if ("Y".equals(buy.getSaveDeliveryFlag())) {

				Receiver receiver = buy.getReceivers().get(0);

				UserDelivery userDelivery = new UserDelivery();
				userDelivery.setUserId(buy.getUserId());
				userDelivery.setDefaultFlag("Y");

				String title = buy.getSaveDeliveryName();
				if (ObjectUtils.isEmpty(title) == false) {
					title = title.trim();
					if ("".equals(title)) {
						title= receiver.getReceiveName();
					}
				} else {
					title = receiver.getReceiveName();
				}

				userDelivery.setTitle(title);
				userDelivery.setUserName(receiver.getReceiveName());
				userDelivery.setPhone(receiver.getReceivePhone());
				userDelivery.setMobile(receiver.getReceiveMobile());
				userDelivery.setNewZipcode(receiver.getReceiveNewZipcode());
				userDelivery.setZipcode(receiver.getReceiveZipcode());
				userDelivery.setSido(receiver.getReceiveSido());
				userDelivery.setSigungu(receiver.getReceiveSido());
				userDelivery.setEupmyeondong(receiver.getReceiveEupmyeondong());
				userDelivery.setAddress(receiver.getReceiveAddress());
				userDelivery.setAddressDetail(receiver.getReceiveAddressDetail());

				userDeliveryService.insertUserDelivery(userDelivery);
			}
		}

		// TODO :: 결제 후 포인트 검증 - update 전 data 가 조회되어 주석 처리
		/*List<OrderGivePoint> pointListValidate = orderGivePointService.getGiveBlcePointListByUserId(userId);

		for (GivePayOrder givePayOrder : buyLocgovs) {
			long locgovSumPrice = givePayOrder.getSumPrice();
			long checkCnt = 0;
			locgovCheck: for (OrderGivePoint beforePayOrderGivePoint : pointList) {
				if (beforePayOrderGivePoint.getCntrLocgovCode().equals(givePayOrder.getLocgovCode())) {
					for (OrderGivePoint afterPayOrderGivePoint : pointListValidate) {
						if (afterPayOrderGivePoint.getCntrLocgovCode().equals(givePayOrder.getLocgovCode())) {
							long beforeCntrBlcePoint = beforePayOrderGivePoint.getCntrBlcePoint();
							long afterCntrBlcePoint = afterPayOrderGivePoint.getCntrBlcePoint();
							if (afterCntrBlcePoint < 0) {
								throw new OrderException("잔여 포인트 마이너스");
							}
							if (beforeCntrBlcePoint == (afterCntrBlcePoint + locgovSumPrice)) {
								// 정상
								checkCnt++;
								break locgovCheck;
							} else {
								throw new OrderException("결제 전 포인트 값 != 결제 후 포인트 값 + 총 구매 포인트 값");
							}
						}
					}
				}
			}
			if (checkCnt == 0) {
				throw new OrderException("결제 후 구매 검증 이상");
			}
		}
		*/

		// 국민비서 알림 전송 - 구매자
		try {
			User user = userService.getUserByUserId(userId);
			UserDetail userDetail = (UserDetail) user.getUserDetail();
			userDetail.decrypt(userDetailEncryptor, false);
			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {			// 문자 수신 동의했을 경우
				List<ReceiverInfo> receiverInfos = new ArrayList<>();
				ReceiverInfo receiverInfo = new ReceiverInfo();
				receiverInfo.setSmsType(SmsType.PRESENT_ORDER);
				receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
				StringBuilder sb = new StringBuilder();
				sb.append(user.getUserName());
				sb.append("|");
				sb.append(receiverInfo.getLocalDateTimeToStr());
				sb.append("|");
				sb.append(orderCode);
				sb.append("|");
				BuyItem buyItem = buyItems.get(0);
				sb.append(StringEscapeUtils.unescapeHtml(buyItem.getItemName()).replaceAll("\\|", "-"));
				if (StringUtils.hasLength(buyItem.getOptionsDisplay()) && !buyItem.getOptionsDisplay().contains("|")) {
					sb.append("(");
					sb.append(StringEscapeUtils.unescapeHtml(buyItem.getOptionsDisplay()));
					sb.append(")");
				}
				if (buyItems.size() > 1) {
					sb.append(" 등");
				}
				sb.append("|");
				sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
				receiverInfo.setSndngCntnts(sb.toString());
				receiverInfos.add(receiverInfo);
				smsIpsService.insertTifIpsSndngM(receiverInfos);
			}
		} catch (NullPointerException | ClassCastException e) {
			log.error(getClass().getName() +  " :: saveGiveOrderTempAndPay send user sms error", e);
		}

		// 같은 상품 여러 건일 경우 한번만 발송하도록 변경(답례품 제공자)

		List<BuyItem> sendSmsItemList = new ArrayList<>();
		// 국민비서 알림 전송 - 답례품 제공자
		for (BuyItem buyItem : buyItems) {
			boolean exist = false;

			seller : for (BuyItem sendSmsItem : sendSmsItemList) {
				if (buyItem.getSellerId() == sendSmsItem.getSellerId() && buyItem.getItemId() == sendSmsItem.getItemId()) {
					exist = true;
					break seller;
				}
			}

			if (!exist) {
				sendSmsItemList.add(buyItem);
			}
		}

		for (BuyItem sendSmsItem : sendSmsItemList) {
			try {
				User user = userService.getUserByUserId(userId);

				Seller seller = sellerService.getSellerById(sendSmsItem.getSellerId());
				SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdForSms(seller.getLoginId());
				if ("0".equals(sellerUser.getReceiveSms()) && StringUtils.hasLength(sellerUser.getPhoneNumber()) && StringUtils.hasLength(sellerUser.getMberCi())) {			// 문자 수신 동의했을 경우

					List<ReceiverInfo> receiverInfos = new ArrayList<>();
					ReceiverInfo receiverInfo = new ReceiverInfo();
					receiverInfo.setSmsType(SmsType.PRESENT_ORDER);
					receiverInfo.setPrvcIdntfcInfo(sellerUser.getMberCi());
					StringBuilder sb = new StringBuilder();
					sb.append(user.getUserName());
					sb.append("|");
					sb.append(receiverInfo.getLocalDateTimeToStr());
					sb.append("|");
					sb.append(orderCode);
					sb.append("|");
					sb.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getItemName()).replaceAll("\\|", "-"));
					if (StringUtils.hasLength(sendSmsItem.getOptionsDisplay()) && !sendSmsItem.getOptionsDisplay().contains("|")) {
						sb.append("(");
						sb.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getOptionsDisplay()));
						sb.append(")");
					}
					sb.append("|");
					sb.append(sellerUser.getPhoneNumber().replaceAll("-", ""));
					receiverInfo.setSndngCntnts(sb.toString());
					receiverInfos.add(receiverInfo);
					smsIpsService.insertTifIpsSndngM(receiverInfos);

					/* 팝빌 카카오 알림톡 전송 */
//					Alimtalk alimtalk = new Alimtalk();
//					alimtalk.setTemplateCode("026030000361");
//					alimtalk.setReceiverNum(sellerUser.getPhoneNumber());
//					alimtalk.setReceiverName(user.getUserName());
//					alimtalk.setOrderDate(alimtalk.getLocalDateTimeToStr());
//					alimtalk.setOrderNo(orderCode);

//					// 아이템(옵션)
//					StringBuilder orderItemName = new StringBuilder();
//					orderItemName.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getItemName()).replaceAll("\\|", "-"));
//					if (StringUtils.hasLength(sendSmsItem.getOptionsDisplay()) && !sendSmsItem.getOptionsDisplay().contains("|")) {
//						orderItemName.append("(");
//						orderItemName.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getOptionsDisplay()));
//						orderItemName.append(")");
//					}
//					alimtalk.setOrderName(orderItemName.toString());
//					alimtalkService.sendAlimtalk(alimtalk);

//					/* 국자원 모바일 메신저 알림톡 전송 */
//					Nuri2NrmsgData nuri2NrmsgData = new Nuri2NrmsgData();
//
//					// 아이템(옵션)
//					StringBuilder orderItemName = new StringBuilder();
//					orderItemName.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getItemName()).replaceAll("\\|", "-"));
//					if (StringUtils.hasLength(sendSmsItem.getOptionsDisplay()) && !sendSmsItem.getOptionsDisplay().contains("|")) {
//						orderItemName.append("(");
//						orderItemName.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getOptionsDisplay()));
//						orderItemName.append(")");
//					}
//
//					// 템플릿코드
//					nuri2NrmsgData.setAltTemplateCode("KR002");
//					// 수신번호
//					nuri2NrmsgData.setPhone(sellerUser.getPhoneNumber().replaceAll("-", ""));
//					// ALT_JSON
//					nuri2NrmsgData.setAltJson(nuri2NrmsgData.getContent("PRESENT_ORDER", user.getUserName(), receiverInfo.getLocalDateTimeToStr(), orderCode, orderItemName.toString(), ""));
//					// XMS_TEST
//					nuri2NrmsgData.setXmsText(nuri2NrmsgData.getContent("PRESENT_ORDER", user.getUserName(), receiverInfo.getLocalDateTimeToStr(), orderCode, orderItemName.toString(), ""));
//
//					nuri2Service.insertAlimtalk(nuri2NrmsgData);

				}
			} catch (NullPointerException | ClassCastException e) {
				log.error(getClass().getName() +  " :: saveGiveOrderTempAndPay send seller sms error :: sellerId :: " + sendSmsItem.getSellerId(), e);
			} catch (Exception e) {
				log.error(getClass().getName() +  " :: saveGiveOrderTempAndPay send seller sms error :: sellerId :: " + sendSmsItem.getSellerId(), e);
			}
		}

		HashMap<String, Object> result = new HashMap<>();
		result.put("orderCode", buy.getOrderCode());
		result.put("orderSequence", orderSequence);
		return result;
	}


	// 오프라인 답례품 주문 처리
	@Override
	@Transactional(rollbackFor = RuntimeException.class, isolation = Isolation.READ_COMMITTED)
	public void saveOffGiveOrder(GiftOrderVo paramVo) throws Exception {

		List<OrderShippingInfo> orderShippingInfoList = new ArrayList<OrderShippingInfo>(); //배송정보List
		Map<String, Object> userMap = new HashMap<>();

		long userId 	= paramVo.getUserId();
		String userName = paramVo.getReceiver().getReceiveName();
		//String loginId	= paramVo.getLoginId();
		String loginId	= null;
		int itemSequence = 0;

		/**
		 * DateUtils 치환
		 */
		Date now = new Date();
		String createdDate = new SimpleDateFormat("yyyyMMddHHmmss").format(now);


		/**
		 * user정보
		 */
		userMap = orderMapper.getUserByMberCi(paramVo.getMbrCi());

		if (userMap == null) {
			loginId	= String.valueOf(paramVo.getLoginId());
		}else {
			loginId	= String.valueOf(userMap.get("login_id"));
		}
		/**
		 * 1. 주문번호생성
		 */
		String orderCode = getNewOrderCode(OrderCodePrefix.FRONT);

		List<Receiver> receivers = new ArrayList<Receiver>();
		List<BuyItem> items = new ArrayList<BuyItem>();

		/**
		 * KSH 수령인이 1명뿐이라고 가정함.
		 */
		Receiver receiver = paramVo.getReceiver();
		receiver.setReceiveNewZipcode(receiver.getReceiveZipcode()); //KSH NewZipcode -> zipCode 똑같이 넣어줌.
		receiver.setItems(items);
		receivers.add(receiver);

		/**
		 * KSH receiver > receivers > buy
		 */
		Buy buy = new Buy();
		buy.setOrderCode(orderCode); //주문번호
		buy.setReceivers(receivers); //수령인(List지만 1명)

		/**
		 * 3. 기존 프로세스의 buyItems 형태 만들어주기.
		 *
		 * 참고: for (BuyItem buyItem : receiver.getItems())
		 */
		List<BuyItem> buyItems = receivers.get(0).getItems();

		/**
		 *  step1에 들어올때는 배송지 정보가 1개임 (참고 없어도됨)
		 */
		receivers.get(0).setShippingIndex(0);

		/**
		 * 4. orderItems 처리. (cart의 방식을 채용)
		 */
		String[] arrayRequiredItems = paramVo.getOrderItems();

		if (arrayRequiredItems.length > 0) {

			/**
			 * [0]:ItemCode [1]:수량 [2]:itemOption
			 */
			for (int i = 0; i < arrayRequiredItems.length; i++) {
				String itemString = arrayRequiredItems[i];
				String[] itemInfo = StringUtils.delimitedListToStringArray(itemString, "||");

				if (itemInfo.length != 3) {
					continue;
				}

				int itemId 	 = Integer.parseInt(itemInfo[0]); //item코드
//					String itemUserCode = itemInfo[0];
				int quantity = Integer.parseInt(itemInfo[1]); //수량
				Item item 	 = orderMapper.getItemById(itemId);
//					Item item 	 = mapper.getItemByItemUserCode(itemUserCode);

				/**
				 * itemOption 파싱
				 */
				String optionsText = itemInfo[2];
				String options = "";

				ItemOption selectedItemOption = null;

				if (!"".equals(optionsText)) {
					item.setItemOptions(orderMapper.getItemOptionList(item.getItemId()));
					String[] optionList = StringUtils.delimitedListToStringArray(optionsText, "^^^");

					for (String optionText : optionList) {
						String[] optionInfo = StringUtils.delimitedListToStringArray(optionText, "```");
						if (optionInfo.length != 2 || "```".equals(optionsText)) {		// 옵션 없는 경우 필터 추가
							continue;
						}

						int optionId = Integer.parseInt(optionInfo[0]);
						String text = !"".equals(optionInfo[1]) ? optionInfo[1] : "";

						for (ItemOption itemOption : item.getItemOptions()) {
							if (itemOption.getItemOptionId() == optionId) {
								if (!"".equals(options)) {
									options += "^^^";
								}
								selectedItemOption = itemOption;
								options += makeOptionText(item, itemOption, text);
								break;
							}
						}
					}
				}

				/**
				 * BuyItems에 객체 담기.
				 * item > buyItem > buyItems
				 */
				BuyItem buyItem = new BuyItem();

				buyItem.setItemId(item.getItemId());
				buyItem.setOptionsOriginal(options);
				buyItem.setOptions(ShopUtils.viewOptionTextNoUl(options));
				buyItem.setItemSequence(itemSequence++); //KSH itemSequence는 set하는부분없어서 buyItem에 수동으로 추가해준다.
				buyItem.setUserId(userId); //KSH userId -> (buyer의 userId)
				buyItem.setItemName(item.getItemName());
				//오프라인 답례품을 위한 기부일련번호 추가
				buyItem.setFiller9(paramVo.getLinkMngKey());

				/**
				 * KSH item에 Seller 담기
				 */
//					long sellerId = item.getSeller().getSellerId();			// item 에 seller 가 세팅되지 않는 경우가 있어서 수정
				long sellerId = item.getSellerId();
				Seller seller = orderMapper.getSellerById(sellerId);
				item.setSeller(seller);

				buyItem.setItem(item);

				/**
				 * KSH 판매자 지역구분코드 수동으로 추가해줌. (buy.items -> item.getLocgovCode에서 가져옴..)
				 */
				buyItem.setLocgovCode(nvl(seller.getLocgovCode()));
				buyItem.setLocgovNm(nvl(seller.getLocgovNm()));
				buyItem.setSellerId(sellerId);
				buyItem.setCompanyName(seller.getCompanyName());

				buyItem.setItemUserCode(item.getItemUserCode());
				buyItem.setDeliveryType(item.getDeliveryType()); //1:본사배송 2:업체배송

				buyItem.setShipmentId(item.getShipmentId()); // 출고지, 반송지
				buyItem.setShipmentReturnId(item.getShipmentReturnId()); //택배사
				buyItem.setDeliveryCompanyName(item.getDeliveryCompanyName());
				buyItem.setDeliveryCompanyId(item.getDeliveryCompanyId());


				/**
				 * KSH itemPrice 관련 수동생성
				 */
				ItemPrice itemPrice = new ItemPrice();
				itemPrice.setQuantity(quantity);
				itemPrice.setItemSalePrice(item.getSalePrice());
				if (selectedItemOption != null && selectedItemOption.getItemOptionId() > 0) {
					itemPrice.setOptionPrice(selectedItemOption.getOptionPrice());
				} else {
					itemPrice.setOptionPrice(0);
				}
				buyItem.setItemPrice(itemPrice); //KSH BuyItem > itemPrice null때문 수동으로 넣어줌.
				//buyItem.setItemPrice(new ItemPrice(buyItem));

				buyItems.add(buyItem);
			}
		}

		// 지자체별 상품 분류
		List<GivePayOrder> buyLocgovs = new ArrayList<>();

		if (buyItems != null) {
			for (BuyItem buyItem : buyItems) {
				// 옵션값 디스플레이용 => 로직 체크용으로 변경
//					buyItem.setOptionsDisplay(buyItem.getOptions()); //KSH 필요없으면 지울것. front파싱가능하게 || 붙여줌
//					buyItem.setOptions(ShopUtils.viewOptionTextNoUl(buyItem.getOptionsOriginal()));

				if (buyItem.getLocgovCode() != null && !buyItem.getLocgovCode().trim().isEmpty()) {
					boolean existLocgov = false;
					locgov: for (GivePayOrder givePayOrder : buyLocgovs) {
						if (buyItem.getLocgovCode().equals(givePayOrder.getLocgovCode())) {
							givePayOrder.addBuyItemList(buyItem);
							existLocgov = true;
							break locgov;
						}
					}
					if (!existLocgov) {
						GivePayOrder givePayOrder = new GivePayOrder();
						givePayOrder.setLocgovCode(buyItem.getLocgovCode());
						givePayOrder.addBuyItemList(buyItem);
						buyLocgovs.add(givePayOrder);
					}
				}
			}
		}


		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(userId);


		/**
		 * KSH 결제관련(포인트)
		 */

		// KSH payAmountVerificationGivePoint 메서드 태우기 위한 초기화 필요함.. buy.getBuyQuantitys
		// 배송지별 구매상품 초기화
		List<BuyQuantity> buyQuantitys = new ArrayList<>();
		for (BuyItem buyItem : items) {
			BuyQuantity buyQuantity = new BuyQuantity();
			buyQuantity.setItemSequence(buyItem.getItemSequence());
			buyQuantity.setQuantity(buyItem.getItemPrice().getQuantity());
			buyQuantitys.add(buyQuantity);
		}

		receivers.get(0).setBuyQuantitys(buyQuantitys);

		// 데이터 검증
		List<OrderGivePoint> pointList = orderMapper.getGiveBlcePointListByUserId(userId); //KSH 사용자 아이디로 기부 잔여포인트 조회
		/**
		 * KSH 장바구니검증 프로세스같은데 포인트체크만 진행하도록 함.
		 */
		offPayAmountVerificationGivePoint(buyLocgovs, buy, orderParam, pointList);

		List<BuyPayment> payments = new ArrayList<>();

		// 지자체, 상품 종류 상관없이 1개로 처리
		BuyPayment paramBuyPayment = new BuyPayment();
		paramBuyPayment.setOrderCode(buy.getOrderCode());
		paramBuyPayment.setApprovalType("give-point");
		paramBuyPayment.setServiceType("mall");
		paramBuyPayment.setTaxFreeAmount(0);
		paramBuyPayment.setCreatedDate(createdDate);
		for (GivePayOrder givePayOrder : buyLocgovs) {
			paramBuyPayment.setAmount(paramBuyPayment.getAmount() + givePayOrder.getSumPrice());
			paramBuyPayment.setTaxFreeAmount(paramBuyPayment.getTaxFreeAmount() + givePayOrder.getTaxFreeSumPrice());
		}

		payments.add(paramBuyPayment);


		for (Receiver buyReceiver : buy.getReceivers()) {

			buyReceiver.setUserId(buy.getUserId());
			buyReceiver.setOrderCode(buy.getOrderCode());
			buyReceiver.setCreatedDate(createdDate);
			//buyReceiver.processHyphen(); //KSH processHyphen() 필요없음.


			// 주문상품 복사
			for (BuyItem buyItem : buyReceiver.getItems()) {
				buyItem.setOrderCode(buy.getOrderCode());
				buyItem.setShippingIndex(buyReceiver.getShippingIndex());
				buyItem.setCampaignCode(buy.getCampaignCode());
				buyItem.setCreatedDate(createdDate);
				buyItem.setOrderStatus("0"); // ShopUtils.getOrderStatusLabel
				buyItem.setGuestFlag("N"); //KSH 로그인개념 없다는 가정. 손님모드이면 구매가 안되나본데.

//				buyItem.setOptions(buyItem.getOptionsDisplay());  // 옵션 값 화면 표시 형태로 저장
//				buyItem.setOptions(ShopUtils.viewOptionTextNoUl(buyItem.getOptionsOriginal())); // 옵션 값 로직 체크용으로 변경
			}
		}

		/**
		 * 5. 재고관련 상품배송정책
		 */
		HashMap<String, Integer> buyQuantityMap = new HashMap<>();
		HashMap<String, Integer> buySetQuantityMap = new HashMap<>();
		setOrderItemInfo(buyItems, /*orderParam, */buyQuantityMap, buySetQuantityMap);

		if (buy.getReceivers() != null) {
			for (Receiver buyReceiver : buy.getReceivers()) {

				// 구매 상품 정책별 그룹
				String zipcode = buyReceiver.getReceiveZipcode();

				if (ObjectUtils.isEmpty(zipcode)) {
					zipcode = buyReceiver.getReceiveZipcode();
				}

				/**
				 * KSH setOrderItemInfo에서 나온 buyReceiver items셋팅
				 */
				buyReceiver.setItems(buyItems);

				/**
				 * KSH itemGroups 여기서 설정. buyReceiver에 items값이 있어야된다.
				 */
				buyReceiver.setShipping(orderMapper.getIslandTypeByZipcode(zipcode));
			}
		}

		// 모바일 상품 존재할 경우 분할 작업 처리....
		int maxItemSequence = 0;
		boolean existMobileItem = false;
//		boolean isAdultUser = True;

		for (BuyItem buyItem : buyItems) {
			/**
			 * KSH 파일서버 item이 statusCode가 1이 아닌게 있음.
			 */
			Item item = orderMapper.getItemByItemUserCode(buyItem.getItemUserCode());
			if (item != null) {

				buyItem.setAdultItemYn(item.getAdultItemYn());
//				if (!isAdultUser && "Y".equalsIgnoreCase(item.getAdultItemYn())) {
//					//throw new Exception("19세 이상 로그인 사용자만 주문 가능합니다.");
//					throw new Exception("0330");
//				}

				buyItem.setMobileItemYn(item.getMobileItemYn());

				if ("Y".equalsIgnoreCase(item.getMobileItemYn())) {
					existMobileItem = true;
					buyItem.setCopyMobileItemCnt(buyItem.getItemPrice().getQuantity() - 1);
					buyItem.setItemPriceQuantity(1);
				}
				if (maxItemSequence < buyItem.getItemSequence()) {
					maxItemSequence = buyItem.getItemSequence();
				}
				if ("2".equals(item.getTaxType())) {
					buyItem.setTaxFreeYn("Y");
				} else {
					buyItem.setTaxFreeYn("N");
				}
			}
		}

		if (existMobileItem) {
			List<BuyItem> mobileCopyItems = new ArrayList<>();
			for (BuyItem buyItem : buyItems) {
				if ("Y".equalsIgnoreCase(buyItem.getMobileItemYn()) && buyItem.getCopyMobileItemCnt() > 0) {
					try {
						for(long l = 0 ; l < buyItem.getCopyMobileItemCnt() ; l++) {
							BuyItem copyItem = (BuyItem) buyItem.clone();
							maxItemSequence++;
							copyItem.setItemSequence(maxItemSequence);
							mobileCopyItems.add(copyItem);
						}
					} catch (NullPointerException e) {
						//throw new Exception("주문 답례품 처리중 문제가 발생 하였습니다.");
						throw new Exception("0310"); //주문에 실패 했습니다.
					} catch (Exception e) {
						//throw new Exception("주문 답례품 처리중 문제가 발생 하였습니다.");
						throw new Exception("0310"); //주문에 실패 했습니다.
					}
				}
			}

			if (!mobileCopyItems.isEmpty()) {
				buyItems.addAll(mobileCopyItems);
			}
		}


		int orderSequence;
		Buyer buyer = paramVo.getBuyer();
		buyer.setUserId(userId);
		buyer.setUserName(userName);
		buyer.setOrderCode(orderCode);
		buyer.setLoginId(loginId);
		buyer.setPhone(paramVo.getReceiver().getReceiveMobile());
		buyer.setMobile(paramVo.getReceiver().getReceiveMobile());
		if (userMap != null ) {
			buyer.setEmail(String.valueOf(userMap.get("email")));
		}

		//KSH front로부터 0으로 셋팅하더라.
		OrderPrice orderPrice = new OrderPrice();
		orderPrice.setPayAmount(0);
		orderPrice.setOrderPayAmountTotal(0);

		//buyer.setOrderPrice(buy.getOrderPrice());
		buyer.setOrderPrice(orderPrice);

		/**
		 * KSH op_order > buyer의정보(email, mobile)값만 받고있음.
		 */
		orderMapper.insertOffOrder(buyer);

		int shippingInfoSequence = 0;
		orderSequence = buyer.getOrderSequence();

		for(Receiver buyReceiver : buy.getReceivers()) {

			// 주문상품 배송지 정보를 저장
			OrderShippingInfo orderShippingInfo = new OrderShippingInfo(orderCode, orderSequence, shippingInfoSequence++, buyReceiver);

			// 수령자 이름 체크로직 추가
			if (!StringUtils.hasLength(orderShippingInfo.getReceiveName())) {
				//throw new Exception("받으시는분 이름을 입력해주세요.");
				throw new Exception("0331");
			}

			// 휴대폰 체크로직 추가
			if (!StringUtils.hasLength(orderShippingInfo.getReceiveMobile())) {
				//throw new Exception("배송지 휴대폰번호를 입력해주세요.");
				throw new Exception("0332");
			}

			// 우편번호 체크로직 추가
			if (!StringUtils.hasLength(orderShippingInfo.getReceiveZipcode()) && !existMobileItem) {
				//throw new Exception("배송지 우편번호를 입력해주세요.");
				throw new Exception("0333");
			}

			// 주소 체크로직 추가
			if (!StringUtils.hasLength(orderShippingInfo.getReceiveAddress()) && !existMobileItem) {
				//throw new Exception("배송지 주소를 입력해주세요.");
				throw new Exception("0334");
			}

			// 상세주소 체크로직 추가
			if (!StringUtils.hasLength(orderShippingInfo.getReceiveAddressDetail()) && !existMobileItem) {
				//throw new Exception("배송지 상세주소를 입력해주세요.");
				throw new Exception("0335");
			}

			orderMapper.insertOffOrderShippingInfo(orderShippingInfo);
			orderShippingInfoList.add(orderShippingInfo);

			/**
			 * KSH getItemGroups가 null인경우도있다.
			 */
			insertOffOrderItemNew(buyItems, buyReceiver.getItemGroups(), paramVo);
		}


		/**
		 * KSH 결제관련(포인트)
		 */
		int paymentSequence = 0;
		buy.setPayments(payments);

		try {
			for (BuyPayment buyPayment : payments) {

				String approvalType = buyPayment.getApprovalType();

				OrderPayment orderPayment = new OrderPayment();
				orderPayment.setOrderCode(orderCode);
				orderPayment.setOrderSequence(orderSequence);
				orderPayment.setPaymentSequence(paymentSequence++);

				orderPayment.setApprovalType(approvalType);
				orderPayment.setAmount(buyPayment.getAmount());
				orderPayment.setTaxFreeAmount(buyPayment.getTaxFreeAmount());

				orderPayment.setNowPaymentFlag("Y"); // 즉시 결제 처리

				if ("Y".equals(orderPayment.getNowPaymentFlag())) {
					orderPayment.setRemainingAmount(buyPayment.getAmount());
					orderPayment.setPayDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
				}

				orderPayment.setPaymentType("1");
				orderPayment.setDeviceType(buy.getDeviceType());

				orderMapper.insertOrderPayment(orderPayment);
			}

		} catch (NullPointerException e) {
			throw e;
		} catch (Exception e) {
			throw new Exception();
		}

		/**
		 * 재고 차감
		 */
		HashMap<String, Integer> stockMap = buy.getStockMap();
		HashMap<String, Integer> stockSetMap = buy.getStockSetMap();
//			if (stockMap == null) {
//				return orderSequence + "/" + orderCode;
//			}

		this.updateStockDeduction(stockMap);
		// 세트상품 재고차감
		this.updateStockDeduction(stockSetMap);




	}

	// 기부포인트 주문 관련 결제 가능 검증
	private void payAmountVerificationGivePoint(List<GivePayOrder> buyLocgovs, Buy buy, OrderParam orderParam, List<OrderGivePoint> pointList) {
		if (!UserUtils.isUserLogin()) {
			throw new OrderException(MessageUtils.getMessage("M00481")); // 잘못된 접근입니다.
		}

//		List<BuyItem> list = orderMapper.getOrderItemTempList(orderParam);
		List<BuyItem> list = getOrderItemTempList(orderParam);
		if (list == null) {
			throw new OrderException("주문 가능 상품이 없습니다.", "/cart");
		}

		// 배송지 지정되지 않은 상품있는지 체크용
		HashMap<String, Integer> checkQuantityTotal = new HashMap<>();
		for (BuyItem buyItem : list) {
			if (buyItem != null) {
				ItemPrice itemPrice = buyItem.getItemPrice();
				if (itemPrice != null) {
					String key = "item-" + buyItem.getItemSequence();

					int addCount = 0;
					if (checkQuantityTotal.get(key) != null) {
						addCount = checkQuantityTotal.get(key);
					}

					checkQuantityTotal.put(key, itemPrice.getQuantity() + addCount);
				}
			}
		}
		// 주문 수량 체크
		int shippingIndex = 0;
		for(Receiver receiver : buy.getReceivers()) {
			receiver.setShippingIndex(shippingIndex);
			List<BuyItem> items = new ArrayList<>();

			for(BuyQuantity buyQuantity : receiver.getBuyQuantitys()) {

				for(BuyItem buyItem : list) {
					if (buyQuantity.getItemSequence() == buyItem.getItemSequence()) {

						BuyItem cloneObject;

						try {

							String checkKey = "item-" + buyQuantity.getItemSequence();
							int buyTotalCount = checkQuantityTotal.get(checkKey);
							if (buyTotalCount - buyQuantity.getQuantity() == 0) {
								checkQuantityTotal.remove(checkKey);
							} else {

								if (buyTotalCount - buyQuantity.getQuantity() > 0) {
									checkQuantityTotal.put(checkKey, buyTotalCount - buyQuantity.getQuantity());
								} else {

									// 장바구니에 담겨있는 수량보다 구매시도 수량이 많은 경우
									throw new OrderException("장바구니에 담겨있는 수량보다 구매시도 수량이 많습니다.");
								}
							}

							cloneObject = (BuyItem) buyItem.clone();

							cloneObject.getItemPrice().setQuantity(buyQuantity.getQuantity());

							items.add(cloneObject);

						} catch (CloneNotSupportedException e) {
							throw new OrderException(e);
						}

						break;
					}
				}

			}

			// 구매가능여부 체크
//			ShopUtils.buyVerification(items, items.size());
			buyGiveGoodsVerification(items, items.size());
		}

		if (checkQuantityTotal.keySet().size() > 0) {
			throw new OrderException("배송지가 지정되지 않은 상품이 있습니다.");
		}

		// 포인트 구매 가능 검증
		int buyLocgovsSize = buyLocgovs.size();
		int pointCnt = 0;
		for (OrderGivePoint orderGivePoint : pointList) {
			for (GivePayOrder givePayOrder : buyLocgovs) {
				if (givePayOrder.getLocgovCode().equals(orderGivePoint.getCntrLocgovCode())) {
					pointCnt++;
					if (givePayOrder.getSumPrice() > orderGivePoint.getCntrBlcePoint()) {		// 포인트 부족
						throw new OrderException("기부포인트가 부족합니다.");
					}
				}
			}
		}

		if (buyLocgovsSize != pointCnt) {		// 일부 자치구 포인트 없음
			throw new OrderException("기부포인트가 부족합니다.");
		}
	}



	/**
	 * 상품의 동시 구매 가능 상태를 체크 (재고, 동시 구매 가능 상품 여부등)
	 * @param list
	 * @param inventoryVerificationCount
	 * @return
	 */
	@Override
	public void buyGiveGoodsVerification(List<BuyItem> list, int inventoryVerificationCount) {
		if (list == null || list.isEmpty()) {
			throw new OrderException(MessageUtils.getMessage("M00419")); // 장바구니에 담긴 상품이 없습니다.
		}

		// 재고량을 검사
		int successCount = 0;
		for (BuyItem buyItem : list) {

			Item item = buyItem.getItem();
			OrderQuantity orderQuantity = buyItem.getOrderQuantity();
			int buyQuantity = 0;
			if(buyItem.getItemPrice() != null) {
				buyQuantity = buyItem.getItemPrice().getQuantity();
			}

			// 판매 불가
			if ("N".equals(buyItem.getAvailableForSaleFlag())) {
				if (StringUtils.hasLength(buyItem.getSystemComment())) {
					throw new OrderException(buyItem.getSystemComment());	// 상품의 정보가 변경되어 구매가 불가능합니다.
				} else {
					throw new OrderException(buyItem.getItem().getItemName() + " 답례품의 옵션 구성이 변경되었거나 재고가 없습니다.");	// 상품의 정보가 변경되어 구매가 불가능합니다.
				}
			}

			// 상품에 옵션이 있고 상품 옵션 구성이 null인경우 상품의 구성이 변경됨..
			if ("Y".equals(item.getItemOptionFlag())) {
				if (item.getItemOptions() != null && buyItem.getOptionList() == null) {
					throw new OrderException(item.getItemName() + " 답례품의 옵션 구성이 변경되었습니다.");
				}
			}

			// 일반상품
			if (!ObjectUtils.isEmpty(buyItem.getOptions())) {
				// 장바구니에는 상품의 옵션정보가 선택되어있으나 상품의 구성이 변경됨.. ex) 상품의 옵션 삭제등..
				if (buyItem.getOptionList() == null) {
					throw new OrderException(item.getItemName() + "답례품의 옵션 구성이 변경되었습니다.");
				}
			}

			// 최대 구매가능 수량이 0이면 재고가 없거나 품절된 상품임
			if (orderQuantity.getMaxQuantity() == 0) {
				throw new OrderException(MessageUtils.getMessage("M00483")); // 이쪽의 상품은 재고가 없습니다.
			}

			// 최대 구매가능 수량이 -1인경우 무제한 상품
			if (orderQuantity.getMaxQuantity() != -1) {

				// 옵션이 없는 상품 검사 - 상품 ID가 같은 상품의 주문 수량을 더하여 해당 상품의 주문 가능 상태를 채크함
				if ("N".equals(item.getItemOptionFlag())) {
					if ("Y".equals(item.getStockFlag())) {
						int sumBuyQuantity = 0;
						for (BuyItem checkBuyItem : list) {
							Item checkItem = checkBuyItem.getItem();
							if ("N".equals(checkItem.getItemOptionFlag())) {
								if(checkBuyItem != null && checkBuyItem.getItemPrice() != null && buyItem != null) {
									if (buyItem.getItemId() == checkBuyItem.getItemId() && !ObjectUtils.isEmpty(checkBuyItem.getItemPrice().getQuantity())) {
										sumBuyQuantity += checkBuyItem.getItemPrice().getQuantity();
									}
								}
							}
						}
						if (sumBuyQuantity > item.getStockQuantity()) {
							throw new OrderException(MessageUtils.getMessage("M00483")); // 이쪽의 상품은 재고가 없습니다.
						}
					} else {
						if (buyQuantity > orderQuantity.getMaxQuantity()) {
							throw new OrderException(item.getItemName() + " 답례품의 최대 구매가능수량을 확인해주세요.");
						}
					}
				} else {
					// 옵션이 있는 상품 검사 - 옵션 ID가 같은 상품들의 재고량을 더하여 해당 상품의 주문 가능 상태를 채크
					for (ItemOption option : buyItem.getOptionList()) {
						int sumBuyQuantity = 0;
						int sumBuyOptionQuantity = 0;
						for (BuyItem checkBuyItem : list) {
							List<ItemOption> checkOptionList = checkBuyItem.getOptionList();
							if(checkBuyItem != null && checkBuyItem.getItemPrice() != null && buyItem != null) {
								if (buyItem.getItemId() == checkBuyItem.getItemId() && !ObjectUtils.isEmpty(checkBuyItem.getItemPrice().getQuantity())) {
									sumBuyQuantity += checkBuyItem.getItemPrice().getQuantity();
								}
							}

							if ("Y".equals(option.getOptionStockFlag()) && !ObjectUtils.isEmpty(checkOptionList)) {
								for (ItemOption checkOption : checkOptionList) {
									if(checkBuyItem != null && checkBuyItem.getItemPrice() != null && buyItem != null) {
										if (checkOption.getItemOptionId() == option.getItemOptionId() && !ObjectUtils.isEmpty(checkBuyItem.getItemPrice().getQuantity())) {
											sumBuyOptionQuantity += checkBuyItem.getItemPrice().getQuantity();
										}
									}
								}
							}
						}

//						if (sumBuyQuantity > orderQuantity.getMaxQuantity()) {
						if (sumBuyQuantity > item.getOrderMaxQuantity() && item.getOrderMaxQuantity() > 0) {
							throw new OrderException(item.getItemName() + "답례품의 최대 구매가능수량을 확인해주세요.");
						}

						if ("Y".equals(option.getOptionStockFlag()) && sumBuyOptionQuantity > option.getOptionStockQuantity()) {
							throw new OrderException(MessageUtils.getMessage("M00483")); // 이쪽의 상품은 재고가 없습니다.
						}
					}
				}
			}

			successCount++;
		}

		if (!(inventoryVerificationCount == successCount)) {
			throw new OrderException(MessageUtils.getMessage("M00486"));	// 주문 상품 처리중 오류가 발생 하였습니다. \n주문을 다시 시도해 주세요.
		}
	}

	// 주문취소시 기부 포인트 환급 로직
	@Override
	public void refundGiveGoodsPoint(OrderPayment orderPayment, OrderCancelApply orderCancelApply, List<OrderGivePoint> useGivePoints, List<OrderGivePoint> remainGivePoints) {

		int refundPoint = orderCancelApply.getClaimApplyAmount();

//		OrderGivePoint orderGivePointParam = new OrderGivePoint();
//		orderGivePointParam.setUserId(orderCancelApply.getUserId());
//		orderGivePointParam.setCntrLocgovCode(orderCancelApply.getOrderItem().getLocgovCode());
//		orderGivePointParam.setOrderCode(orderPayment.getOrderCode());

		int paymentCancelAmount = orderPayment.getCancelAmount();
		int paymentRemainingAmount = orderPayment.getRemainingAmount();

//		orderGivePointParam.setCntrLocgovCode(orderCancelApply.getOrderItem().getLocgovCode());
//		List<OrderGivePoint> useGivePoints = orderGivePointService.getGiveUsePoint(orderGivePointParam);

		for (OrderGivePoint useGivePoint : useGivePoints) {
			if (useGivePoint.getOrderCode().equals(orderCancelApply.getOrderCode())) {
				long usePoint = useGivePoint.getCntrUsePoint();
				useGivePoint.setOrderCode(orderCancelApply.getOrderCode());

//				OrderGivePoint remainGivePoint = orderGivePointService.getGiveBlcePointListByCntrSn(useGivePoint);
				OrderGivePoint remainGivePoint = null;
				for (OrderGivePoint givePoint : remainGivePoints) {
					if (useGivePoint.getCntrSn().equalsIgnoreCase(givePoint.getCntrSn())
							&& (givePoint.getCntrPoint() - givePoint.getCntrBlcePoint() > 0)) {
						remainGivePoint = givePoint;
						break;
					}
				}
//				if (remainGivePoint == null) {
//					throw new OrderException("문제가 발생했습니다.");
//				}
				if (remainGivePoint != null) {
					long cntrBlcePoint = remainGivePoint.getCntrBlcePoint();

					if (refundPoint < usePoint) {		// 환불해야하는 포인트가 사용포인트보다 작을 경우
						usePoint -= refundPoint;

						useGivePoint.setCntrUsePoint(usePoint);

						cntrBlcePoint += refundPoint;
						remainGivePoint.setCntrBlcePoint(cntrBlcePoint);

						paymentCancelAmount += refundPoint;
						paymentRemainingAmount -= refundPoint;

						useGivePoint.setLastUpdtPnttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
						useGivePoint.setLastUpdusrId(UserUtils.getUser().getUserId());
						orderGivePointService.updateGiveUsePoint(useGivePoint);		// 사용내역 수정

						refundPoint = 0;
					} else {
						refundPoint -= usePoint;

						cntrBlcePoint += usePoint;
						remainGivePoint.setCntrBlcePoint(cntrBlcePoint);

						paymentCancelAmount += usePoint;
						paymentRemainingAmount -= usePoint;

						useGivePoint.setOrderCode(orderPayment.getOrderCode());
						orderGivePointService.deleteGiveUsePoint(useGivePoint);		// 사용내역 삭제
					}

					remainGivePoint.setLastUpdtPnttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
					remainGivePoint.setLastUpdusrId(UserUtils.getUser().getUserId());

					//orderGivePointService.updateGiveBlcePoint(remainGivePoint);		// 잔여포인트 수정			// 잔여포인트 업데이트 로직 제외

					if (refundPoint == 0) {
						break;
					}
				}
			}
		}


		// 국민비서 알림 전송 (답례품취소 완료시)
		try {
			User user = userService.getUserByUserId(orderCancelApply.getUserId());
			UserDetail userDetail = (UserDetail) user.getUserDetail();
			userDetail.decrypt(userDetailEncryptor, false);
			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {
				List<ReceiverInfo> receiverInfos = new ArrayList<>();
				ReceiverInfo receiverInfo = new ReceiverInfo();
				receiverInfo.setSmsType(SmsType.PRESENT_CANCEL_COMPLETE);
				receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
				StringBuilder sb = new StringBuilder();
				sb.append(user.getUserName());	// 이름
				sb.append("|");
				sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
				sb.append("|");
				sb.append(orderCancelApply.getOrderCode());	// 주문번호
				sb.append("|");
				sb.append(StringEscapeUtils.unescapeHtml(orderCancelApply.getOrderItem().getItemName()).replaceAll("\\|", "-"));		// 답례품명
    			if (StringUtils.hasLength(orderCancelApply.getOrderItem().getOptions()) && !orderCancelApply.getOrderItem().getOptions().contains("|")) {
    				sb.append("(");
    				sb.append(StringEscapeUtils.unescapeHtml(orderCancelApply.getOrderItem().getOptions()));	// 옵션명
    				sb.append(")");
    			}
				sb.append("|");
				sb.append(orderCancelApply.getCancelReasonText().equals("기타") ? orderCancelApply.getCancelReasonDetail() : orderCancelApply.getCancelReasonText());		// 사유
				sb.append("|");
				sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
				receiverInfo.setSndngCntnts(sb.toString());
				receiverInfos.add(receiverInfo);
				smsIpsService.insertTifIpsSndngM(receiverInfos);
			}
		} catch (NullPointerException | ClassCastException e) {
			log.error(getClass().getName() +  " :: refundGiveGoodsPoint send sms error", e);
		}


		// 국민비서 알림 전송 (답례품취소 완료시) - 답례품 제공자
		try {
//			User user = userService.getUserByUserId(orderCancelApply.getUserId());
//			SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdForSms(UserUtils.getLoginId());
//			if ("0".equals(sellerUser.getReceiveSms()) && StringUtils.hasLength(sellerUser.getPhoneNumber()) && StringUtils.hasLength(sellerUser.getMberCi())) {
//				List<ReceiverInfo> receiverInfos = new ArrayList<>();
//				ReceiverInfo receiverInfo = new ReceiverInfo();
//				receiverInfo.setSmsType(SmsType.PRESENT_CANCEL_COMPLETE);
//				receiverInfo.setPrvcIdntfcInfo(sellerUser.getMberCi());
//				StringBuilder sb = new StringBuilder();
//				sb.append(user.getUserName());	// 이름
//				sb.append("|");
//				sb.append(receiverInfo.getLocalDateTimeToStr());	// 신청일시
//				sb.append("|");
//				sb.append(orderCancelApply.getOrderCode());	// 주문번호
//				sb.append("|");
//				sb.append(StringEscapeUtils.unescapeHtml(orderCancelApply.getOrderItem().getItemName()));		// 답례품명
//    			if (StringUtils.hasLength(orderCancelApply.getOrderItem().getOptions()) && !orderCancelApply.getOrderItem().getOptions().contains("|")) {
//    				sb.append("(");
//    				sb.append(StringEscapeUtils.unescapeHtml(orderCancelApply.getOrderItem().getOptions()));	// 옵션명
//    				sb.append(")");
//    			}
//				sb.append("|");
//				sb.append(orderCancelApply.getCancelReasonText().equals("기타") ? orderCancelApply.getCancelReasonDetail() : orderCancelApply.getCancelReasonText());		// 사유
//				sb.append("|");
//				sb.append(sellerUser.getPhoneNumber().replaceAll("-", ""));
//				receiverInfo.setSndngCntnts(sb.toString());
//				receiverInfos.add(receiverInfo);
//				smsIpsService.insertTifIpsSndngM(receiverInfos);
//			}
		} catch (NullPointerException | ClassCastException e) {
			log.error(getClass().getName() +  " :: refundGiveGoodsPoint send sms error", e);
		}

		orderPayment.setCancelAmount(paymentCancelAmount);
		orderPayment.setRemainingAmount(paymentRemainingAmount);
		orderPayment.setRefundFlag("Y");
		orderPaymentMapper.updateOrderPaymentForCancel(orderPayment);		// 결제 데이터 수정

	}

	@Override
	public long getOrderItemCntForMyPage(long userId) {
		return orderMapper.getOrderItemCntForMyPage(userId);
	}

	@Override
	public long getOrderItemClaimCntForMyPage(long userId) {
		return orderMapper.getOrderItemClaimCntForMyPage(userId);
	}

	@Override
	public boolean isChangeSoldoutItem(long itemId, int orderQuantity) {		// TODO :: 테스트 필요
		Item item = orderMapper.getSoldoutItemCheckData(itemId);
		if (item == null || item.getItemId() == 0) {			// 재고 연동을 하지 않거나 재고를 연동하지 않는 옵션이 있는 경우
			return false;
		} else {
			if (item.getOptionStockQuantity() != null && "Y".equalsIgnoreCase(item.getItemOptionFlag())) {		// 옵션 재고 잔여량과 주문 수량이 같은 경우 품절
				return Integer.valueOf(item.getOptionStockQuantity()[0]) == orderQuantity;
			} else {		// 재고 잔여량과 주문 수량이 같은 경우 품절
				return item.getStockQuantity() == orderQuantity;
			}
		}
	}

	@Override
	public void refundGiveGoodsPoint2(List<OrderGivePoint> cancelRequestPointList, List<OrderGivePoint> giveUsePointList, List<OrderGivePoint> givePointList) {
		for (OrderGivePoint cancelRequestPoint : cancelRequestPointList) {			// 자치구별 취소 요청 포인트
			long cancelPoint = cancelRequestPoint.getCntrBlcePoint();
			String cancelLocgov = cancelRequestPoint.getCntrLocgovCode();
			cancel : for (OrderGivePoint giveUsePoint : giveUsePointList) {							// 사용 포인트
				String useLocgov = giveUsePoint.getCntrLocgovCode();
				if (cancelLocgov.equals(useLocgov)) {			// 취소요청 포인트와 사용포인트가 일치 할 경우
					long usePoint = giveUsePoint.getCntrUsePoint();
					long refundPoint = 0;
					if (usePoint > cancelPoint) {				// 사용한 포인트가 취소할 포인트보다 많을 경우
						usePoint -= cancelPoint;
						refundPoint = cancelPoint;
						cancelPoint = 0;
						giveUsePoint.setCntrUsePoint(usePoint);
						orderGivePointService.updateGiveUsePoint(giveUsePoint);		// 사용 포인트 업데이트
					} else {			// 사용한 포인트가 취소할 포인트보다 같거나 작을 경우
						cancelPoint -= usePoint;
						refundPoint = usePoint;
						usePoint = 0;
						orderGivePointService.deleteGiveUsePoint(giveUsePoint);		// 사용 포인트 삭제
					}

					giveUsePoint.setAddExpired(true);

					givePoint : for (OrderGivePoint givePoint : givePointList) {
						if (giveUsePoint.getCntrSn().equals(givePoint.getCntrSn())) {
							givePoint.setCntrBlcePoint(givePoint.getCntrBlcePoint() + refundPoint);
							if (givePoint.getCntrPoint() < givePoint.getCntrBlcePoint()) {
								throw new OrderException();
							}
							//orderGivePointService.updateGiveBlcePoint(givePoint);			// 사용포인트 업데이트			// 잔여포인트 업데이트 로직 제외
							break givePoint;
						}
					}

					if (cancelPoint == 0) {
						break cancel;
					}
				}
			}

			if (cancelPoint > 0) {
				throw new OrderException("취소 처리중 문제가 발생했습니다.");
			}
		}

	}





	// 주문 로직, 주문 임시저장과 주문저장 동시 처리(통장 입금, 네이버 페이 등 별도 결제 로직이 없으므로 바로 진행)
	@Override
//	@Transactional(rollbackFor = RuntimeException.class, isolation = Isolation.READ_COMMITTED)
	public HashMap<String, Object> orderAgencySaveGiveOrderTempAndPay(HttpSession session, Buy buy, HttpServletRequest request, User user) throws RuntimeException {
		List<BuyItem> buyItems = buy.getItems();

		String createdDate = DateUtils.getToday(DATETIME_FORMAT);

		long userId = user.getUserId();

		buy.getBuyer().setNewZipcode(buy.getBuyer().getZipcode());
		buy.setCreatedDate(createdDate);
		// 결제 전 검증 및 결제정보 임시 저장
		// 지자체별 상품 분류
		List<GivePayOrder> buyLocgovs = new ArrayList<>();

		if(buyItems != null) {
			for (BuyItem buyItem : buyItems) {
				// 옵션값 디스플레이용 => 로직 체크용으로 변경
				buyItem.setOptionsDisplay(buyItem.getOptions());
				buyItem.setOptions(buyItem.getOptionsOriginal());

				if (buyItem.getLocgovCode() != null && !buyItem.getLocgovCode().trim().isEmpty()) {
					boolean existLocgov = false;
					locgov: for (GivePayOrder givePayOrder : buyLocgovs) {
						if (buyItem.getLocgovCode().equals(givePayOrder.getLocgovCode())) {
							givePayOrder.addBuyItemList(buyItem);
							existLocgov = true;
							break locgov;
						}
					}
					if (!existLocgov) {
						GivePayOrder givePayOrder = new GivePayOrder();
						givePayOrder.setLocgovCode(buyItem.getLocgovCode());
						givePayOrder.addBuyItemList(buyItem);
						buyLocgovs.add(givePayOrder);
					}
				}
			}
		}

		OrderParam orderParam = new OrderParam();
		orderParam.setUserId(userId);
		orderParam.setViewTarget(buy.getDeviceType());

		// 데이터 검증
		List<OrderGivePoint> pointList = orderGivePointService.getGiveBlcePointListByUserId(userId);
		payAmountVerificationGivePoint(buyLocgovs, buy, orderParam, pointList);

		// 기존 주문 임시 데이터 삭제 - 주문자 정보등만 삭제 한다. * 상품 임시 데이터는 유지
		orderMapper.deleteOrderTemp(buy);

		List<BuyPayment> payments = new ArrayList<>();

		// 지자체, 상품 종류 상관없이 1개로 처리
		BuyPayment paramBuyPayment = new BuyPayment();
		paramBuyPayment.setOrderCode(buy.getOrderCode());
		paramBuyPayment.setApprovalType("give-point");
		paramBuyPayment.setServiceType("mall");
		paramBuyPayment.setTaxFreeAmount(0);
		paramBuyPayment.setCreatedDate(createdDate);
		for (GivePayOrder givePayOrder : buyLocgovs) {
			paramBuyPayment.setAmount(paramBuyPayment.getAmount() + givePayOrder.getSumPrice());
			paramBuyPayment.setTaxFreeAmount(paramBuyPayment.getTaxFreeAmount() + givePayOrder.getTaxFreeSumPrice());
		}
		orderMapper.insertOrderPaymentBuyTemp(paramBuyPayment);
		payments.add(paramBuyPayment);

		orderMapper.insertOrderTemp(buy);

		for (Receiver receiver : buy.getReceivers()) {

			receiver.setUserId(buy.getUserId());
			receiver.setOrderCode(buy.getOrderCode());
			receiver.setSessionId(buy.getSessionId());
            receiver.setCreatedDate(createdDate);

			receiver.processHyphen();
			orderMapper.insertOrderShippingBuyTemp(receiver);

			// 주문상품 복사
			for (BuyItem buyItem : receiver.getItems()) {
				buyItem.setOrderCode(buy.getOrderCode());
				buyItem.setShippingIndex(receiver.getShippingIndex());
				buyItem.setCampaignCode(buy.getCampaignCode());
				buyItem.setCreatedDate(createdDate);

				buyItem.setOrderStatus("10");		// ShopUtils.getOrderStatusLabel
				if (UserUtils.isUserLogin()) {
					buyItem.setGuestFlag("N");
				} else {
					throw new OrderException();
				}

				buyItem.setOptions(buyItem.getOptionsDisplay());		// 옵션 값 화면 표시 형태로 저장

				orderMapper.insertOrderItemBuyTemp(buyItem);

				buyItem.setOptions(buyItem.getOptionsOriginal());		// 옵션 값 로직 체크용으로 변경
			}
		}

		// 주문자 정보 기본정보로 저장 체크시 2017-05-18 yulsun.yoo
		if ("1".equals(buy.getDefaultBuyerCheck())) {
			buy.getBuyer().setUserId(buy.getUserId());
			userService.updateUserDetailForOrder(buy.getBuyer());
		}



		// 이후 결제 처리

		String orderCode = buy.getOrderCode();

		HashMap<String, Integer> buyQuantityMap = new HashMap<>();
		HashMap<String, Integer> buySetQuantityMap = new HashMap<>();
//		setOrderItemInfo(buy.getItems(), orderParam, buyQuantityMap, buySetQuantityMap);
		setOrderItemInfo(buyItems, orderParam, buyQuantityMap, buySetQuantityMap);

		if (buy.getReceivers() != null) {
			for (Receiver receiver : buy.getReceivers()) {

				// 상품쿠폰 적용
				receiver.itemCouponUsed(false, buy, receiver.getShippingIndex());

				// 구매 상품 정책별 그룹
				String zipcode = receiver.getReceiveZipcode();

				if (ObjectUtils.isEmpty(zipcode)) {
					zipcode = receiver.getReceiveZipcode();
				}

				receiver.setShipping(orderMapper.getIslandTypeByZipcode(zipcode));
			}
		}

		// 재고 차감 목록
		buy.setStockMap(buyQuantityMap);

		// 세트 재고 차감 목록
		buy.setStockSetMap(buySetQuantityMap);

		// 구매가능여부 체크
//		ShopUtils.buyVerification(buy.getItems(), buy.getItems().size());
//		buyGiveGoodsVerification(buy.getItems(), buy.getItems().size());

//		buy.setOrderPrice(bankPayAmount, configService.getShopConfig(Config.SHOP_CONFIG_ID));

		// 기부포인트 차감
		for (GivePayOrder givePayOrder : buyLocgovs) {
			long locgovAmount = givePayOrder.getSumPrice();
			OrderGivePoint param = new OrderGivePoint();
			param.setUserId(userId);
			param.setCntrLocgovCode(givePayOrder.getLocgovCode());

			List<OrderGivePoint> locgovPointList = orderGivePointService.getGiveBlcePointListByUserIdAndLocgov(param);

			locgovPay: for (OrderGivePoint orderGivePoint : locgovPointList) {
				long leftPoint = orderGivePoint.getCntrBlcePoint();
				orderGivePoint.setUserId(userId);
				orderGivePoint.setUseCn("답례품 구매");
				orderGivePoint.setUseSeCode("1");		// 1 : 사용, 2 : 소멸, 3 : 탈퇴
				orderGivePoint.setOrderCode(buy.getOrderCode());

				if (locgovAmount > leftPoint) {			// 결제할 금액이 잔여 포인트보다 클 경우
					orderGivePoint.setCntrUsePoint(leftPoint);		// 사용금액은 잔여포인트 전체
					orderGivePoint.setCntrBlcePoint(0);
					locgovAmount -= leftPoint;
					if (locgovAmount < 0) {
						throw new OrderException("답례품 주문 처리중 문제가 발생 하였습니다.");
					}
				} else {
					orderGivePoint.setCntrUsePoint(locgovAmount);		// 사용금액은 남은 결제 금액 전체
					leftPoint -= locgovAmount;
					locgovAmount = 0;
					if (leftPoint < 0) {
						throw new OrderException("기부포인트가 부족합니다.");
					}
					orderGivePoint.setCntrBlcePoint(leftPoint);
				}

				orderGivePoint.setLastUpdtPnttm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
				orderGivePoint.setLastUpdusrId(UserUtils.getUser().getUserId());

				//orderGivePointService.updateGiveBlcePoint(orderGivePoint);			// 잔여포인트 업데이트 로직 제외

				orderGivePoint.setFrstRegisterId(UserUtils.getUser().getUserId());
				orderGivePointService.insertGiveUsePoint(orderGivePoint);

				// TODO :: 기부 사용포인트 이력


				if (locgovAmount == 0) {
					break locgovPay;
				}
			}
		}

		// 품절상태로 변경될 경우 장바구니에서 해당 상품 삭제
		CartParam cartParam = new CartParam();
		for (BuyItem buyItem : buyItems) {
			if (isChangeSoldoutItem(buyItem.getItemId(), buyItem.getItemPrice().getQuantity())) {
				cartParam.addItemIds(buyItem.getItemId());
			}
		}

		if (cartParam.getItemIds() != null && !cartParam.getItemIds().isEmpty()) {
			cartMapper.deleteCartByItemIds(cartParam);
		}
		// 품절상태로 변경될 경우 장바구니에서 해당 상품 삭제

		// 모바일 상품 존재할 경우 분할 작업 처리....
		int maxItemSequence = 0;
		boolean existMobileItem = false;
		boolean isAdultUser = UserUtils.isAdult();
		for (BuyItem buyItem : buyItems) {
			Item item = itemService.getItemBy(buyItem.getItemUserCode());
			buyItem.setAdultItemYn(item.getAdultItemYn());
			if (!isAdultUser && "Y".equalsIgnoreCase(item.getAdultItemYn())) {
				throw new OrderException("19세 이상 로그인 사용자만 주문 가능합니다.");
			}
			buyItem.setMobileItemYn(item.getMobileItemYn());
			if ("Y".equalsIgnoreCase(item.getMobileItemYn())) {
				existMobileItem = true;
				buyItem.setCopyMobileItemCnt(buyItem.getItemPrice().getQuantity() - 1);
				buyItem.setItemPriceQuantity(1);
//				buyItem.setCampaignCode("MOBILE");
			}
			if (maxItemSequence < buyItem.getItemSequence()) {
				maxItemSequence = buyItem.getItemSequence();
			}
			if ("2".equals(item.getTaxType())) {
				buyItem.setTaxFreeYn("Y");
			} else {
				buyItem.setTaxFreeYn("N");
			}
		}

		if (existMobileItem) {
			List<BuyItem> mobileCopyItems = new ArrayList<>();
			for (BuyItem buyItem : buyItems) {
				if ("Y".equalsIgnoreCase(buyItem.getMobileItemYn()) && buyItem.getCopyMobileItemCnt() > 0) {
					try {
						for(long l = 0 ; l < buyItem.getCopyMobileItemCnt() ; l++) {
							BuyItem copyItem = (BuyItem) buyItem.clone();
							maxItemSequence++;
							copyItem.setItemSequence(maxItemSequence);
//							copyItem.setCampaignCode("MOBILE");
							mobileCopyItems.add(copyItem);
						}
					} catch (CloneNotSupportedException e) {
						throw new OrderException("주문 답례품 처리중 문제가 발생 하였습니다.");
					}
				}
			}

			if (!mobileCopyItems.isEmpty()) {
				buyItems.addAll(mobileCopyItems);
			}
		}
		// 모바일 상품 존재할 경우 분할 작업 처리....

		int orderSequence;

		try {

			Buyer buyer = buy.getBuyer();
			buyer.setIp(saleson.common.utils.CommonUtils.getClientIp(request));
			buyer.setOrderCode(orderCode);
			buyer.setUserId(buy.getUserId());
			buyer.setLoginId(user.getLoginId());

			// 데이터 암호화
			buyer.encrypt(buyerEncryptor);

			buyer.setOrderPrice(buy.getOrderPrice());

			orderMapper.insertOrder(buyer);

			int shippingInfoSequence = 0;
//			int shippingSequence = 0;
//			int itemSequence = 0;
			orderSequence = buyer.getOrderSequence();

			for(Receiver receiver : buy.getReceivers()) {

				// 주문상품 배송지 정보를 저장
				OrderShippingInfo orderShippingInfo = new OrderShippingInfo(orderCode, orderSequence, shippingInfoSequence++, receiver);

				// 수령자 이름 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveName())) {
					throw new OrderException("받으시는분 이름을 입력해주세요.");
				}

				// 휴대폰 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveMobile())) {
					throw new OrderException("배송지 휴대폰번호를 입력해주세요.");
				}

				// 우편번호 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveZipcode()) && !existMobileItem) {
					throw new OrderException("배송지 우편번호를 입력해주세요.");
				}

				// 주소 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveAddress()) && !existMobileItem) {
					throw new OrderException("배송지 주소를 입력해주세요.");
				}

				// 상세주소 체크로직 추가
				if (!StringUtils.hasLength(orderShippingInfo.getReceiveAddressDetail()) && !existMobileItem) {
					throw new OrderException("배송지 상세주소를 입력해주세요.");
				}

				// 데이터 암호화
				orderShippingInfo.encrypt(orderShippingInfoEncryptor);
				orderMapper.insertOrderShippingInfo(orderShippingInfo);

//				for(Shipping shipping : receiver.getItemGroups()) {		// insertOrderItem 에서 처리하도록 변경
//					shipping.setOrderCode(orderCode);
//					shipping.setOrderSequence(orderSequence);
//					shipping.setShippingSequence(shippingSequence++);
//
//					orderMapper.insertOrderShipping(shipping);
//				}

				insertOrderItemNew(buyItems, receiver.getItemGroups(), true);
			}

//			insertOrderLog(OrderLogType.ORDER_PAYMENT, orderCode, orderSequence, shippingSequence, orderCode);		// insertOrderItem 에서 처리하도록 변경

			int paymentSequence = 0;

			buy.setPayments(payments);

//			int cnt = 1;
//			int totCnt = payments.size();

			for (BuyPayment buyPayment : payments) {
//				OrderPgData orderPgData = null;

				String approvalType = buyPayment.getApprovalType();

				OrderPayment orderPayment = new OrderPayment();
				orderPayment.setOrderCode(orderCode);
				orderPayment.setOrderSequence(orderSequence);
				orderPayment.setPaymentSequence(paymentSequence++);


				orderPayment.setApprovalType(approvalType);
				orderPayment.setAmount(buyPayment.getAmount());
				orderPayment.setTaxFreeAmount(buyPayment.getTaxFreeAmount());

				orderPayment.setNowPaymentFlag("Y");		// 즉시 결제 처리

				if ("Y".equals(orderPayment.getNowPaymentFlag())) {
					orderPayment.setRemainingAmount(buyPayment.getAmount());
					orderPayment.setPayDate(DateUtils.getToday(DATETIME_FORMAT));
				}

				orderPayment.setPaymentType("1");
				orderPayment.setDeviceType(buy.getDeviceType());

				// 데이터 암호화
				orderPayment.encrypt(orderPaymentEncryptor);
				orderMapper.insertOrderPayment(orderPayment);
			}

		} catch (OrderException e) {
			throw new OrderException(e.getErrorMessage(), "/order/step1", e);
		} catch (RuntimeException e) {
			throw new OrderException("주문 처리 중 에러가 발생하여 취소되었습니다.", "/order/step1", e);
		}

		try {
			// 주문서 작성 임시 저장 정보 삭제
			orderMapper.deleteOrderItemTemp(orderParam);

			// 주문서(세트상품) 임시 저장 정보 삭제
			orderMapper.deleteOrderItemSetTemp(orderParam);

			// 주문 임시 저장 정보 삭제
			orderMapper.deleteOrderTemp(buy);
			orderMapper.deleteOrderItemBuyTemp(buy);
			orderMapper.deleteOrderShippingBuyTemp(buy);
			orderMapper.deleteOrderPaymentBuyTemp(buy);

			// 주문이 완료된 장바구니 상품들 삭제
			List<Integer> itemIds = new ArrayList<>();
			for (BuyItem buyItem : buy.getItems()) {
				itemIds.add(buyItem.getItemId());
			}

			// 장바구니 삭제
			if (!itemIds.isEmpty()) {
				cartParam = new CartParam();
				cartParam.setUserId(buy.getUserId());
				cartParam.setSessionId(buy.getSessionId());
				cartParam.setItemIds(itemIds);

				// 로컬에서는 테스트를 위해서 장바구니를 삭제 하지 않는다.
//				if (!ServiceType.LOCAL) {
					cartMapper.deleteCartByItemIds(cartParam);
//				}
			}

		} catch(RuntimeException e) {
//					log.error("주문 처리 후 임시 데이터 삭제시 ERROR: {}", e.getMessage(), e);
			log.error("주문 처리 후 임시 데이터 삭제시 ERROR: {}", getClass().getName() + " :: insertOrder RuntimeException1 ============", e);
		}

		// Message 발송
//		try {
//			orderMessageService.sendOrderMessageTx(buy);
//		} catch(RuntimeException e) {
////					log.error("주문 메시지 발송 ERROR: {}", e.getMessage(), e);
//			log.error("주문 메시지 발송 ERROR: {}", getClass().getName() + " :: insertOrder RuntimeException2 ============", e);
//		}

		// 재고 차감
		try {
			HashMap<String, Integer> stockMap = buy.getStockMap();
			HashMap<String, Integer> stockSetMap = buy.getStockSetMap();
//			if (stockMap == null) {
//				return orderSequence + "/" + orderCode;
//			}

			this.updateStockDeduction(stockMap);

			// 세트상품 재고차감
			this.updateStockDeduction(stockSetMap);
		} catch(RuntimeException e) {
//					log.error("재고 차감 ERROR: {}", e.getMessage(), e);
			log.error("재고 차감 ERROR: {}", getClass().getName() + " :: insertOrder RuntimeException3 ============", e);
		}

		// 로그인시 주소록 저장 선택시 기본 배송지 저장
//		if (UserUtils.isUserLogin()) {
//			if ("Y".equals(buy.getSaveDeliveryFlag())) {
//
//				Receiver receiver = buy.getReceivers().get(0);
//
//				UserDelivery userDelivery = new UserDelivery();
//				userDelivery.setUserId(buy.getUserId());
//				userDelivery.setDefaultFlag("Y");
//
//				String title = buy.getSaveDeliveryName();
//				if (ObjectUtils.isEmpty(title) == false) {
//					title = title.trim();
//					if ("".equals(title)) {
//						title= receiver.getReceiveName();
//					}
//				} else {
//					title = receiver.getReceiveName();
//				}
//
//				userDelivery.setTitle(title);
//				userDelivery.setUserName(receiver.getReceiveName());
//				userDelivery.setPhone(receiver.getReceivePhone());
//				userDelivery.setMobile(receiver.getReceiveMobile());
//				userDelivery.setNewZipcode(receiver.getReceiveNewZipcode());
//				userDelivery.setZipcode(receiver.getReceiveZipcode());
//				userDelivery.setSido(receiver.getReceiveSido());
//				userDelivery.setSigungu(receiver.getReceiveSido());
//				userDelivery.setEupmyeondong(receiver.getReceiveEupmyeondong());
//				userDelivery.setAddress(receiver.getReceiveAddress());
//				userDelivery.setAddressDetail(receiver.getReceiveAddressDetail());
//
//				userDeliveryService.insertUserDelivery(userDelivery);
//			}
//		}

		// TODO :: 결제 후 포인트 검증 - update 전 data 가 조회되어 주석 처리
		/*List<OrderGivePoint> pointListValidate = orderGivePointService.getGiveBlcePointListByUserId(userId);

		for (GivePayOrder givePayOrder : buyLocgovs) {
			long locgovSumPrice = givePayOrder.getSumPrice();
			long checkCnt = 0;
			locgovCheck: for (OrderGivePoint beforePayOrderGivePoint : pointList) {
				if (beforePayOrderGivePoint.getCntrLocgovCode().equals(givePayOrder.getLocgovCode())) {
					for (OrderGivePoint afterPayOrderGivePoint : pointListValidate) {
						if (afterPayOrderGivePoint.getCntrLocgovCode().equals(givePayOrder.getLocgovCode())) {
							long beforeCntrBlcePoint = beforePayOrderGivePoint.getCntrBlcePoint();
							long afterCntrBlcePoint = afterPayOrderGivePoint.getCntrBlcePoint();
							if (afterCntrBlcePoint < 0) {
								throw new OrderException("잔여 포인트 마이너스");
							}
							if (beforeCntrBlcePoint == (afterCntrBlcePoint + locgovSumPrice)) {
								// 정상
								checkCnt++;
								break locgovCheck;
							} else {
								throw new OrderException("결제 전 포인트 값 != 결제 후 포인트 값 + 총 구매 포인트 값");
							}
						}
					}
				}
			}
			if (checkCnt == 0) {
				throw new OrderException("결제 후 구매 검증 이상");
			}
		}
		*/

		// 국민비서 알림 전송 - 구매자
		try {
//			User user = userService.getUserByUserId(userId);
			UserDetail userDetail = (UserDetail) user.getUserDetail();
			userDetail.decrypt(userDetailEncryptor, false);
			if ("0".equals(userDetail.getReceiveSms()) && StringUtils.hasLength(userDetail.getPhoneNumber()) && StringUtils.hasLength(userDetail.getMberCi())) {			// 문자 수신 동의했을 경우
				List<ReceiverInfo> receiverInfos = new ArrayList<>();
				ReceiverInfo receiverInfo = new ReceiverInfo();
				receiverInfo.setSmsType(SmsType.PRESENT_ORDER);
				receiverInfo.setPrvcIdntfcInfo(userDetail.getMberCi());
				StringBuilder sb = new StringBuilder();
				sb.append(user.getUserName());
				sb.append("|");
				sb.append(receiverInfo.getLocalDateTimeToStr());
				sb.append("|");
				sb.append(orderCode);
				sb.append("|");
				BuyItem buyItem = buyItems.get(0);
				sb.append(StringEscapeUtils.unescapeHtml(buyItem.getItemName()).replaceAll("\\|", "-"));
				if (StringUtils.hasLength(buyItem.getOptionsDisplay()) && !buyItem.getOptionsDisplay().contains("|")) {
					sb.append("(");
					sb.append(StringEscapeUtils.unescapeHtml(buyItem.getOptionsDisplay()));
					sb.append(")");
				}
				if (buyItems.size() > 1) {
					sb.append(" 등");
				}
				sb.append("|");
				sb.append(userDetail.getPhoneNumber().replaceAll("-", ""));
				receiverInfo.setSndngCntnts(sb.toString());
				receiverInfos.add(receiverInfo);
				smsIpsService.insertTifIpsSndngM(receiverInfos);
			}
		} catch (NullPointerException | ClassCastException e) {
			log.error(getClass().getName() +  " :: saveGiveOrderTempAndPay send user sms error", e);
		}

		// 같은 상품 여러 건일 경우 한번만 발송하도록 변경(답례품 제공자)

		List<BuyItem> sendSmsItemList = new ArrayList<>();
		// 국민비서 알림 전송 - 답례품 제공자
		for (BuyItem buyItem : buyItems) {
			boolean exist = false;

			seller : for (BuyItem sendSmsItem : sendSmsItemList) {
				if (buyItem.getSellerId() == sendSmsItem.getSellerId() && buyItem.getItemId() == sendSmsItem.getItemId()) {
					exist = true;
					break seller;
				}
			}

			if (!exist) {
				sendSmsItemList.add(buyItem);
			}
		}

		for (BuyItem sendSmsItem : sendSmsItemList) {
			try {
//				User user = userService.getUserByUserId(userId);

				Seller seller = sellerService.getSellerById(sendSmsItem.getSellerId());
				SellerUser sellerUser = sellerUserService.getSellerUserByLoginIdForSms(seller.getLoginId());
				if ("0".equals(sellerUser.getReceiveSms()) && StringUtils.hasLength(sellerUser.getPhoneNumber()) && StringUtils.hasLength(sellerUser.getMberCi())) {			// 문자 수신 동의했을 경우
					List<ReceiverInfo> receiverInfos = new ArrayList<>();
					ReceiverInfo receiverInfo = new ReceiverInfo();
					receiverInfo.setSmsType(SmsType.PRESENT_ORDER);
					receiverInfo.setPrvcIdntfcInfo(sellerUser.getMberCi());
					StringBuilder sb = new StringBuilder();
					sb.append(user.getUserName());
					sb.append("|");
					sb.append(receiverInfo.getLocalDateTimeToStr());
					sb.append("|");
					sb.append(orderCode);
					sb.append("|");
					sb.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getItemName()).replaceAll("\\|", "-"));
					if (StringUtils.hasLength(sendSmsItem.getOptionsDisplay()) && !sendSmsItem.getOptionsDisplay().contains("|")) {
						sb.append("(");
						sb.append(StringEscapeUtils.unescapeHtml(sendSmsItem.getOptionsDisplay()));
						sb.append(")");
					}
					sb.append("|");
					sb.append(sellerUser.getPhoneNumber().replaceAll("-", ""));
					receiverInfo.setSndngCntnts(sb.toString());
					receiverInfos.add(receiverInfo);
					smsIpsService.insertTifIpsSndngM(receiverInfos);
				}
			} catch (NullPointerException | ClassCastException e) {
				log.error(getClass().getName() +  " :: saveGiveOrderTempAndPay send seller sms error :: sellerId :: " + sendSmsItem.getSellerId(), e);
			}
		}

		HashMap<String, Object> result = new HashMap<>();
		result.put("orderCode", buy.getOrderCode());
		result.put("orderSequence", orderSequence);
		return result;
	}

	/**
	 * 옵션 구성별 문자열을 생성
	 *
	 * @param item
	 * @param itemOption
	 * @param optionText
	 * @return
	 */
	public String makeOptionText(Item item, ItemOption itemOption, String optionText) {
		String option = "";
		boolean addPrice = true;
		if ("S".equals(itemOption.getOptionType())) {
			option = "S||" + itemOption.getOptionName1() + "||" + itemOption.getOptionName2();
		} else if ("S2".equals(itemOption.getOptionType())) {
			option = "S2||" + item.getItemOptionTitle1() + "||" + itemOption.getOptionName1() + "||"
					+ item.getItemOptionTitle2() + "||" + itemOption.getOptionName2();
		} else if ("S3".equals(itemOption.getOptionType())) {
			option = "S3||" + item.getItemOptionTitle1() + "||" + itemOption.getOptionName1() + "||"
					+ item.getItemOptionTitle2() + "||" + itemOption.getOptionName2() + "||"
					+ item.getItemOptionTitle3() + "||" + itemOption.getOptionName3();
		} else if ("T".equals(itemOption.getOptionType())) {
			option = "T||" + itemOption.getOptionName1();
			if (!"".equals(optionText)) {
				option += "||" + optionText;
			} else {
				addPrice = false;
			}
		}

		if (addPrice) {
			option += "||" + itemOption.getExtraPrice();
		}
		return option;
	}

	private String nvl(Object value) {
		return (value == null) ? "" : String.valueOf(value);
	}

	/**
	 * 오프라인 주문 상품 등록
	 * @param buyItems
	 * @throws Exception
	 */
	private void insertOffOrderItemNew(List<BuyItem> buyItems, List<Shipping> shippings, GiftOrderVo paramVo) throws Exception {
		/**
		 * KSH
		 * pk_op_order_shipping_ order_code_ order_sequence_ shipping_sequence
		 * key: {'K0000500293', 0, 0}
		 */

		for (BuyItem buyItem : buyItems) {
			try {
				/**
				 * KSH buyItem별 shipping에 넣어주는 듯. itemSequence가 증가해야함. 수동으로 넣어줌.
				 */
				for (Shipping shipping : shippings) {
					if (buyItem.getItemId() == shipping.getBuyItem().getItemId()) {
						buyItem.setShippingSequence(buyItem.getItemSequence());  //
						shipping.setShippingSequence(buyItem.getItemSequence()); //
						shipping.setOrderCode(buyItem.getOrderCode());
						orderMapper.insertOrderShipping(shipping);
						break;
					}
				}


				//주문자 ID 를 LOGIN ID로 세팅
				buyItem.setUpdatedAdminUserName(paramVo.getUpdatedAdminUserName());
				/**
				 * KSH op_order_item
				 */
				orderMapper.insertOrderItem(buyItem);

//				if (isAgencyOrder) {		// 대행주문시 별도 로그 추가
//					OrderAgencyOrderLog orderAgencyOrderLog = new OrderAgencyOrderLog();
//					orderAgencyOrderLog.setOrderCode(buyItem.getOrderCode());
//					orderAgencyOrderLog.setOrderSequence(buyItem.getOrderSequence());
//					orderAgencyOrderLog.setItemSequence(buyItem.getItemSequence());
//					orderAgencyOrderLog.setUserId(buyItem.getUserId());
//					orderAgencyOrderLog.setManagerId(UserUtils.getUser().getUserId());
//					orderAgencyOrderLog.setManagerNm(UserUtils.getUser().getUserName());
//
//					if (SecurityUtils.hasRole("ROLE_ADMIN_11")) {
//						OrderAgencyManagerInfo orderAgencyManagerInfo = orderAgencyService.selectOrderAgencyManagerInfo(UserUtils.getUser().getUserId());
//						orderAgencyOrderLog.setPbadmsWlfrCntrId(orderAgencyManagerInfo.getPbadmsWlfrCntrId());
//					} else {
//						orderAgencyOrderLog.setPbadmsWlfrCntrId(0);
//					}
//
//					orderAgencyOrderLog.setManagerLclgvCd(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
//
//					orderMapper.insertOrderAgencyOrderLog(orderAgencyOrderLog);
//				}
//
//				// 로그 처리
//				GiftOrderCompleteVo logParam = new GiftOrderCompleteVo();
//				logParam.setInsCode(paramVo.getInsCode());
//				logParam.setTraceId(paramVo.getTraceId());
//				logParam.setMbrCi(paramVo.getMbrCi());
//				logParam.setOrderCode(buyItem.getOrderCode());
//				logParam.setItemSequence(buyItem.getItemSequence());
//				orderService.insertOrderLog(OrderLogType.ORDER_PAYMENT, logParam, "");

			} catch (NullPointerException e) {
				throw new Exception("0310"); //주문에 실패 했습니다.
			} catch (Exception e) {
				throw new Exception("0310"); //주문에 실패 했습니다.
			}
		}
	}

	private void setOrderItemInfo(List<BuyItem> list, /*OrderParam orderParam,*/ HashMap<String, Integer> buyQuantityMap,
			HashMap<String, Integer> buySetQuantityMap) throws Exception {

		HashMap<String, Integer> buyQuantityItemUserCodeMap = new HashMap<>();

		// 재고 차감 + 재고 검증용
		HashMap<String, HashMap<String, Integer>> stockMap = ShopUtils.makeStockMap(list);

		if (stockMap == null) {
			stockMap = new HashMap<>();
		}

		if (buyQuantityMap == null) {
			buyQuantityMap = new HashMap<>();
		}

		for (BuyItem buyItem : list) {
			Item item = buyItem.getItem();
			if (item == null) {
				continue;
			}

			buyItem.setPayDate(buyItem.getCreatedDate());	// 결제일
			buyItem.setSalesDate(buyItem.getCreatedDate());	// 매출일자
			/**
			 * KSH 0으로 하드코딩
			 */
			buyItem.setRevenueSalesStatus("0");	// 매출 기준 상태 => 0 : 주문 등록시, 10 : 결제 확인, 20 : 배송 준비
			buyItem.setEscrowStatus("N");		// 현금 결제 플래그라서 N 처리

			/**
			 * KSH 코드중복으로 불필요
			 */
			//item.setItemOptions(mapper.getItemOptionList(item.getItemId()));
			item.setItemImage(ShopUtils.loadImage(item.getItemUserCode(), item.getItemImage(), "S"));

			// 1. 상품의 필수 옵션을 구성
			if (!StringUtils.hasLength(buyItem.getOptionsOriginal())) {
				if (StringUtils.hasLength(buyItem.getOptions())) {
					buyItem.setOptionList(ShopUtils.getRequiredItemOptions(item, buyItem.getOptions()));
				}
			} else {
				buyItem.setOptionList(ShopUtils.getRequiredItemOptions(item, buyItem.getOptionsOriginal()));
			}

			String itemUserCode = item.getItemUserCode();

			if(itemUserCode != null && buyItem != null && buyItem.getItemPrice() != null) {
				if (buyQuantityItemUserCodeMap.get(itemUserCode) == null) {
					buyQuantityItemUserCodeMap.put(itemUserCode, buyItem.getItemPrice().getQuantity());
				} else {
					buyQuantityItemUserCodeMap.put(itemUserCode, buyQuantityItemUserCodeMap.get(itemUserCode) + buyItem.getItemPrice().getQuantity());
				}
			}

			// 2. 상품의 최대 구매 가능 수량을 책정하여 판매 불가 상품인지 검증 - 필수 선택 옵션정보도 포함하여 검증
			ShopUtils.setBuyQuantityMap(buyItem, stockMap, buyQuantityMap);

			boolean isError = false;
			if (buyQuantityItemUserCodeMap.get(itemUserCode) != null) {
				if (item.getOrderMinQuantity() > buyQuantityItemUserCodeMap.get(itemUserCode)) {
					isError = true;
				}
			}

			if (isError) {
				//throw new OrderException("해당 상품의 최소 구매가능 수량은 " + item.getOrderMinQuantity() + "개 입니다.", "/cart");
				throw new Exception("0310"); //주문에 실패 했습니다.
			}

			OrderQuantity orderQuantity = buyItem.getOrderQuantity();
			boolean isSoldOut = orderQuantity.getMaxQuantity() == 0 ? true : false;

			// 3. 상품이 판매 종료된 상품인지 검사
			if (isSoldOut) {
				//throw new OrderException(item.getItemName() + "상품의 재고가 없습니다.", "/cart");
				throw new Exception("0300"); //재고가 부족합니다.
			} else {

				// 상품에 옵션이 있고 상품 옵션 구성이 null인경우 상품의 구성이 변경됨..
				if ("Y".equals(item.getItemOptionFlag())) {
					if (item.getItemOptions() != null) {
						if (buyItem.getOptionList() == null) {
							buyItem.setAvailableForSaleFlag("N");
						}
					}
				}

				if ("Y".equals(buyItem.getAvailableForSaleFlag())) {
					if (StringUtils.hasLength(buyItem.getOptions())) {
						// 장바구니에는 상품의 옵션정보가 선택되어있으나 상품의 구성이 변경됨.. ex) 상품의 옵션 삭제등..
						if (buyItem.getOptionList() == null) {
							buyItem.setAvailableForSaleFlag("N");
						}
					}
				}
			}

			/**
			 * KSH 상품 금액 계산 <<<<
			 */
			buyItem.setItemPrice(new ItemPrice(buyItem));
		}
	}


	// 기부포인트 주문 관련 결제 가능 검증
	private void offPayAmountVerificationGivePoint(List<GivePayOrder> buyLocgovs, Buy buy, OrderParam orderParam, List<OrderGivePoint> pointList) throws Exception {

		List<BuyItem> list = getOrderItemTempList(buy.getReceivers().get(0).getItems());
		if (list == null) {
			//throw new Exception("주문 가능 상품이 없습니다.");
			throw new Exception("0300"); //재고가 부족합니다.
		}

		// 배송지 지정되지 않은 상품있는지 체크용
		HashMap<String, Integer> checkQuantityTotal = new HashMap<>();
		for (BuyItem buyItem : list) {
			if (buyItem != null) {
				ItemPrice itemPrice = buyItem.getItemPrice();
				if (itemPrice != null) {
					String key = "item-" + buyItem.getItemSequence();

					int addCount = 0;
					if (checkQuantityTotal.get(key) != null) {
						addCount = checkQuantityTotal.get(key);
					}

					checkQuantityTotal.put(key, itemPrice.getQuantity() + addCount);
				}
			}
		}
		// 주문 수량 체크
		int shippingIndex = 0;
		for(Receiver receiver : buy.getReceivers()) {
			receiver.setShippingIndex(shippingIndex);
			List<BuyItem> items = new ArrayList<>();

			for(BuyQuantity buyQuantity : receiver.getBuyQuantitys()) {

				for(BuyItem buyItem : list) {
					if (buyQuantity.getItemSequence() == buyItem.getItemSequence()) {

						BuyItem cloneObject;

						try {

							String checkKey = "item-" + buyQuantity.getItemSequence();
							int buyTotalCount = checkQuantityTotal.get(checkKey);
							if (buyTotalCount - buyQuantity.getQuantity() == 0) {
								checkQuantityTotal.remove(checkKey);
							} else {

								if (buyTotalCount - buyQuantity.getQuantity() > 0) {
									checkQuantityTotal.put(checkKey, buyTotalCount - buyQuantity.getQuantity());
								} else {

									// 장바구니에 담겨있는 수량보다 구매시도 수량이 많은 경우
									//throw new Exception("장바구니에 담겨있는 수량보다 구매시도 수량이 많습니다.");
									throw new Exception("0310"); //주문에 실패 했습니다.

								}
							}

							cloneObject = (BuyItem) buyItem.clone();

							cloneObject.getItemPrice().setQuantity(buyQuantity.getQuantity());

							items.add(cloneObject);

						} catch (CloneNotSupportedException e) {
							throw new Exception(e);
						}

						break;
					}
				}

			}

			/**
			 * KSH Temp테이블 insert를 안하므로 진행불가. (검증로직 다 버려야되나?)
			 */
			buyGiveGoodsVerification(items, items.size());
		}

		/**
		 * KSH Temp에서 파생된 logic
		 */
		if (checkQuantityTotal.keySet().size() > 0) {
			//throw new Exception("배송지가 지정되지 않은 상품이 있습니다.");
			throw new Exception("0337"); //배송지가 지정되지 않은 상품이 있습니다.
		}

		// 포인트 구매 가능 검증
		int buyLocgovsSize = buyLocgovs.size();
		int pointCnt = 0;
		for (OrderGivePoint orderGivePoint : pointList) {
			for (GivePayOrder givePayOrder : buyLocgovs) {
				if (givePayOrder.getLocgovCode().equals(orderGivePoint.getCntrLocgovCode())) {
					pointCnt++;
//					if (givePayOrder.getSumPrice() > orderGivePoint.getCntrBlcePoint()) {	// 포인트 부족
//						//throw new Exception("기부포인트가 부족합니다.");
//						throw new Exception("0329");
//					}
				}
			}
		}

//		if (buyLocgovsSize != pointCnt) {	// 일부 자치구 포인트 없음
//			//throw new Exception("기부포인트가 부족합니다.");
//			throw new Exception("0329");
//		}
	}

	public List<BuyItem> getOrderItemTempList(List<BuyItem> list) throws Exception {

		if (list == null) {
			return null;
		}

		setOrderItemInfo(list, /*orderParam,*/ null, null);

		return list;
	}

	@Override
	public boolean getMobileFlag(OrderParam orderParam) {
		boolean mobileFlag = false;
		List<String> isMobiles = orderMapper.getIsMobiles(orderParam);
		// 현재 주문번호 기준, 모두 모바일 배송일 경우 isMobile = true;
		mobileFlag = isMobiles.stream().allMatch(isMobile -> "Y".equals(isMobile));
		return mobileFlag;
	}

	/**
	 * 전체주문목록 스트리밍 엑셀다운로드
	 *
	 * @param	orderParam
	 * @throws	Exception
	 */
	@Override
	public SXSSFWorkbook streamAllOrderData(OrderParam orderParam) {

		if (UserUtils.isManagerLogin() /* && "LOC".equals(locgovService.getLoginUserAdminRoleCheck()) */) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		orderParam.encrypt(orderParamEncryptor);
		// 페지이제이션 세팅을 위한 조회(row 1개 조회)
		int settingPagination = slaveOrderMapper.getAllOrderCountByParamForManager(orderParam);

		Pagination pagination = Pagination.getInstance(settingPagination, orderParam.getItemsPerPage());
		orderParam.setPagination(pagination);


		// 엑셀 다운로드 workbook setting
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;
		String now;

		// 전체 데이터 count
		orderParam.getPagination().setItemsPerPage(pageSize);
		orderParam.getPagination().setCurrentPage(offset);

		// 조회 시점 일치
		now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		orderParam.setCreatedDate(now);

		int totalCount = slaveOrderMapper.getAllOrderCountByParamForManager(orderParam);

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// workbook 생성을 위한 offset 및 pageSize 설정
		pageSize 		= 1000;
		offset 			= 1;

		orderParam.getPagination().setItemsPerPage(pageSize);
		orderParam.getPagination().setCurrentPage(offset);

		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("ALL_ORDER_LIST");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 권한에 따른 엑셀 설정
		String auth;
		if (SecurityUtils.hasRole("ROLE_ADMIN_1")
				|| SecurityUtils.hasRole("ROLE_ADMIN_2")
				|| SecurityUtils.hasRole("ROLE_ADMIN_3")
				|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			auth = "SYSTEM";
		} else if (SellerUtils.isSellerLogin()) {
			auth = "SELLER";
		} else {
			auth = "";
		}

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);;
		cellStyle.title(titleRow, 0, "전체주문 목록");
		Integer lastColIndex;

		// 엑셀 시트 프레임 설정(컬럼 크기 및 컬럼 명 세팅)
		if (auth == "SYSTEM") {
			sheet.setColumnWidth(0, 2000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 5000);
			sheet.setColumnWidth(8, 10000);
			// sheet.setColumnWidth(9, 10000);
			sheet.setColumnWidth(9, 10000);
			sheet.setColumnWidth(10, 8000);
			sheet.setColumnWidth(11, 2000);
			sheet.setColumnWidth(12, 3000);
			sheet.setColumnWidth(13, 3000);
			sheet.setColumnWidth(14, 10000);
			sheet.setColumnWidth(15, 5000);
			sheet.setColumnWidth(16, 5000);
			sheet.setColumnWidth(17, 3000);
			sheet.setColumnWidth(18, 10000);
			sheet.setColumnWidth(19, 3000);
			sheet.setColumnWidth(20, 10000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No");
			cellStyle.header(header, 1, "지자체");
			cellStyle.header(header, 2, "주문일");
			cellStyle.header(header, 3, "주문번호");
			cellStyle.header(header, 4, "주문자");
			cellStyle.header(header, 5, "아이디");
			cellStyle.header(header, 6, "수취인");
			cellStyle.header(header, 7, "상호명");
			cellStyle.header(header, 8, "판매자 연락처");
			// cellStyle.header(header, 9, "카테고리");
			cellStyle.header(header, 9, "답례품정보");
			cellStyle.header(header, 10, "답례품옵션");
			cellStyle.header(header, 11, "수량");
			cellStyle.header(header, 12, "판매가");
			cellStyle.header(header, 13, "주문상태");
			cellStyle.header(header, 14, "배송정보");
			cellStyle.header(header, 15, "필수추가정보");	// 20260325 필수 추가정보 isb 수정
			cellStyle.header(header, 16, "핸드폰번호");
			cellStyle.header(header, 17, "우편번호");
			cellStyle.header(header, 18, "주소(도로명)");
			cellStyle.header(header, 19, "생년월일");
			cellStyle.header(header, 20, "배송요청사항");

			lastColIndex = header.getLastCellNum() - 1;
		} else if (auth == "SELLER") {
			sheet.setColumnWidth(0, 2000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			// sheet.setColumnWidth(6, 10000);
			sheet.setColumnWidth(6, 10000);
			sheet.setColumnWidth(7, 8000);
			sheet.setColumnWidth(8, 2000);
			sheet.setColumnWidth(9, 3000);
			sheet.setColumnWidth(10, 3000);
			sheet.setColumnWidth(11, 10000);
			sheet.setColumnWidth(12, 5000);	// 20260325 필수 추가정보 isb 수정
			sheet.setColumnWidth(13, 5000);
			sheet.setColumnWidth(14, 3000);
			sheet.setColumnWidth(15, 10000);
			sheet.setColumnWidth(16, 10000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No");
			cellStyle.header(header, 1, "주문일");
			cellStyle.header(header, 2, "주문번호");
			cellStyle.header(header, 3, "주문자");
			cellStyle.header(header, 4, "아이디");
			cellStyle.header(header, 5, "수취인");
			// cellStyle.header(header, 6, "카테고리");
			cellStyle.header(header, 6, "답례품정보");
			cellStyle.header(header, 7, "답례품옵션");
			cellStyle.header(header, 8, "수량");
			cellStyle.header(header, 9, "판매가");
			cellStyle.header(header, 10, "주문상태");
			cellStyle.header(header, 11, "배송정보");
			cellStyle.header(header, 12, "필수추가정보");// 20260325 필수 추가정보 isb 수정
			cellStyle.header(header, 13, "핸드폰번호");
			cellStyle.header(header, 14, "우편번호");
			cellStyle.header(header, 15, "주소(도로명)");
			cellStyle.header(header, 16, "배송요청사항");

			lastColIndex = header.getLastCellNum() - 1;
		} else {
			sheet.setColumnWidth(0, 2000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 10000);
			// sheet.setColumnWidth(8, 10000);
			sheet.setColumnWidth(8, 10000);
			sheet.setColumnWidth(9, 8000);
			sheet.setColumnWidth(10, 2000);
			sheet.setColumnWidth(11, 3000);
			sheet.setColumnWidth(12, 3000);
			sheet.setColumnWidth(13, 10000);
			sheet.setColumnWidth(14, 5000);// 20260325 필수 추가정보 isb 수정
			sheet.setColumnWidth(15, 5000);
			sheet.setColumnWidth(16, 3000);
			sheet.setColumnWidth(17, 10000);
			sheet.setColumnWidth(18, 3000);
			sheet.setColumnWidth(19, 10000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No");
			cellStyle.header(header, 1, "주문일");
			cellStyle.header(header, 2, "주문번호");
			cellStyle.header(header, 3, "주문자");
			cellStyle.header(header, 4, "아이디");
			cellStyle.header(header, 5, "수취인");
			cellStyle.header(header, 6, "상호명");
			cellStyle.header(header, 7, "판매자 연락처");
			// cellStyle.header(header, 8, "카테고리");
			cellStyle.header(header, 8, "답례품정보");
			cellStyle.header(header, 9, "답례품옵션");
			cellStyle.header(header, 10, "수량");
			cellStyle.header(header, 11, "판매가");
			cellStyle.header(header, 12, "주문상태");
			cellStyle.header(header, 13, "배송정보");
			cellStyle.header(header, 14, "필수추가정보");// 20260325 필수 추가정보 isb 수정
			cellStyle.header(header, 15, "핸드폰번호");
			cellStyle.header(header, 16, "우편번호");
			cellStyle.header(header, 17, "주소(도로명)");
			cellStyle.header(header, 18, "생년월일");
			cellStyle.header(header, 19, "배송요청사항");

			lastColIndex = header.getLastCellNum() - 1;
		}

		// 최상단 row 병합
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while (!exceedTotalRow) {
			if (totalCount == 0) break;
			// 엑셀 시트 구성 데이터 조회(1000건) 및 전처리
			List<OrderList> orderList = slaveOrderMapper.getAllOrderListByParamForManager(orderParam);
			decryptOrderListNoneMasking(orderList);
			setOrderItemOther(orderList);

			if (orderList.isEmpty()) break;

			if (auth == "SYSTEM") {
				for (OrderList orderItem : orderList) {
					Row row = sheet.createRow(rowNum++);
					row.setHeight((short) 400);
					cellStyle.data(row, 0, StringUtils.numberFormat(totalCount--));
					cellStyle.data(row, 1, orderItem.getLocgovNm());
					cellStyle.data(row, 2, DateUtils.datetime(orderItem.getCreatedDate()));
					cellStyle.data(row, 3, orderItem.getOrderCode());
					cellStyle.data(row, 4, orderItem.getUserName());
					cellStyle.data(row, 5, orderItem.getLoginId());
					cellStyle.data(row, 6, orderItem.getReceiveNameDec());
					cellStyle.data(row, 7, orderItem.getCompanyName());
					cellStyle.data(row, 8, orderItem.getTelephoneNumber());
					// cellStyle.data(row, 9, orderItem.getCategoryName());
					cellStyle.data(row, 9, orderItem.getItemName());
					cellStyle.data(row, 10, orderItem.getOptions());
					cellStyle.data(row, 11, StringUtils.numberFormat(orderItem.getQuantity()));
					cellStyle.data(row, 12, StringUtils.numberFormat(orderItem.getSaleAmount()));
					cellStyle.data(row, 13, orderItem.getOrderStatusLabel());
					if ("Y".equalsIgnoreCase(orderItem.getMobileItemYn())) {
						if (StringUtils.hasLength(orderItem.getMobileNumber())) {
							cellStyle.data(row, 14, "모바일 : " + orderItem.getMobileNumber());
						} else {
							cellStyle.data(row, 14, "모바일 번호 없음");
						}
					} else {
						if (StringUtils.hasLength(orderItem.getDeliveryNumber())) {
							cellStyle.data(row, 14, "송장번호 : " + orderItem.getDeliveryNumber() + "(" + orderItem.getDeliveryCompanyName() + ")");
						} else {
							cellStyle.data(row, 14, "송장 번호 없음");
						}
					}
					cellStyle.data(row, 15, orderItem.getTextOption());		// 20260325 필수 추가정보 isb 수정
					cellStyle.data(row, 16, orderItem.getMobileDec());
					cellStyle.data(row, 17, orderItem.getReceiveZipcode());
					cellStyle.data(row, 18, orderItem.getReceiveAddressDec());
					cellStyle.data(row, 19, orderItem.getBirthday());
					String memo = orderItem.getMemo();
					if(memo != null && !"".equals(memo)) {
						cellStyle.data(row, 20, memo);
					} else {
						cellStyle.data(row, 20, "배송 요청 사항 없음");
					}
				}
			} else if (auth == "SELLER") {
				for (OrderList orderItem : orderList) {
					Row row = sheet.createRow(rowNum++);
					row.setHeight((short) 400);
					cellStyle.data(row, 0, StringUtils.numberFormat(totalCount--));
					cellStyle.data(row, 1, DateUtils.datetime(orderItem.getCreatedDate()));
					cellStyle.data(row, 2, orderItem.getOrderCode());
					cellStyle.data(row, 3, orderItem.getUserName());
					cellStyle.data(row, 4, orderItem.getLoginId());
					cellStyle.data(row, 5, orderItem.getReceiveNameDec());
					// cellStyle.data(row, 6, orderItem.getCategoryName());
					cellStyle.data(row, 6, orderItem.getItemName());
					cellStyle.data(row, 7, orderItem.getOptions());
					cellStyle.data(row, 8, StringUtils.numberFormat(orderItem.getQuantity()));
					cellStyle.data(row, 9, StringUtils.numberFormat(orderItem.getSaleAmount()));
					cellStyle.data(row, 10, orderItem.getOrderStatusLabel());
					if ("Y".equalsIgnoreCase(orderItem.getMobileItemYn())) {
						if (StringUtils.hasLength(orderItem.getMobileNumber())) {
							cellStyle.data(row, 11, "모바일 : " + orderItem.getMobileNumber());
						} else {
							cellStyle.data(row, 11, "모바일 번호 없음");
						}
					} else {
						if (StringUtils.hasLength(orderItem.getDeliveryNumber())) {
							cellStyle.data(row, 11, "송장번호 : " + orderItem.getDeliveryNumber() + "(" + orderItem.getDeliveryCompanyName() + ")");
						} else {
							cellStyle.data(row, 11, "송장 번호 없음");
						}
					}
					cellStyle.data(row, 12, orderItem.getTextOption());	// 20260325 필수 추가정보 isb 수정
					cellStyle.data(row, 13, orderItem.getMobileDec());
					cellStyle.data(row, 14, orderItem.getReceiveZipcode());
					cellStyle.data(row, 15, orderItem.getReceiveAddressDec());
					String memo = orderItem.getMemo();
					if(memo != null && !"".equals(memo)) {
						cellStyle.data(row, 16, memo);
					} else {
						cellStyle.data(row, 16, "배송 요청 사항 없음");
					}
				}
			} else {
				for (OrderList orderItem : orderList) {
					Row row = sheet.createRow(rowNum++);
					row.setHeight((short) 400);
					cellStyle.data(row, 0, StringUtils.numberFormat(totalCount--));
					cellStyle.data(row, 1, DateUtils.datetime(orderItem.getCreatedDate()));
					cellStyle.data(row, 2, orderItem.getOrderCode());
					cellStyle.data(row, 3, orderItem.getUserName());
					cellStyle.data(row, 4, orderItem.getLoginId());
					cellStyle.data(row, 5, orderItem.getReceiveNameDec());
					cellStyle.data(row, 6, orderItem.getCompanyName());
					cellStyle.data(row, 7, orderItem.getTelephoneNumber());
					// cellStyle.data(row, 8, orderItem.getCategoryName());
					cellStyle.data(row, 8, orderItem.getItemName());
					cellStyle.data(row, 9, orderItem.getOptions());
					cellStyle.data(row, 10, StringUtils.numberFormat(orderItem.getQuantity()));
					cellStyle.data(row, 11, StringUtils.numberFormat(orderItem.getSaleAmount()));
					cellStyle.data(row, 12, orderItem.getOrderStatusLabel());
					if ("Y".equalsIgnoreCase(orderItem.getMobileItemYn())) {
						if (StringUtils.hasLength(orderItem.getMobileNumber())) {
							cellStyle.data(row, 13, "모바일 : " + orderItem.getMobileNumber());
						} else {
							cellStyle.data(row, 13, "모바일 번호 없음");
						}
					} else {
						if (StringUtils.hasLength(orderItem.getDeliveryNumber())) {
							cellStyle.data(row, 13, "송장번호 : " + orderItem.getDeliveryNumber() + "(" + orderItem.getDeliveryCompanyName() + ")");
						} else {
							cellStyle.data(row, 13, "송장 번호 없음");
						}
					}
					cellStyle.data(row, 14, orderItem.getTextOption());		// 20260325 필수 추가정보 isb 수정
					cellStyle.data(row, 15, orderItem.getMobileDec());
					cellStyle.data(row, 16, orderItem.getReceiveZipcode());
					cellStyle.data(row, 17, orderItem.getReceiveAddressDec());
					cellStyle.data(row, 18, orderItem.getBirthday());
					String memo = orderItem.getMemo();
					if(memo != null && !"".equals(memo)) {
						cellStyle.data(row, 19, memo);
					} else {
						cellStyle.data(row, 19, "배송 요청 사항 없음");
					}
				}
			}

			// 객체참조시점 issue로 변수 설정
			orderParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}

		// oderParam 암호화
		orderParam.decrypt(orderParamEncryptor);

		return workbook;

	}

	/**
	 * 구매확정 스트리밍 엑셀다운로드
	 *
	 * @param	orderParam
	 * @throws	Exception
	 */
	@Override
	public SXSSFWorkbook streamConfirmOrderData(OrderParam orderParam) {
		if (UserUtils.isManagerLogin()) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);

			if (StringUtils.hasLength(locgovCode)) {
				// 지자체관리자일 경우 지자체코드 필요
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}

		orderParam.encrypt(orderParamEncryptor);

		Pagination pagination = Pagination.getInstance(0);
		orderParam.setPagination(pagination);
		orderParam.setLanguage(CommonUtils.getLanguage());

		// 엑셀 다운로드 workbook setting
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;
		String now;

		// 전체 데이터 count
		orderParam.getPagination().setItemsPerPage(pageSize);
		orderParam.getPagination().setCurrentPage(offset);

		// 조회 시점 일치
		now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		orderParam.setCreatedDate(now);

		int totalCount = orderShippingMapper.getConfirmCountByParam(orderParam);

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// workbook 생성을 위한 offset 및 pageSize 설정
		pageSize 		= 1000;
		offset 			= 1;

		orderParam.getPagination().setItemsPerPage(pageSize);
		orderParam.getPagination().setCurrentPage(offset);

		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("CONFIRM_ORDER_LIST");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 권한에 따른 엑셀 설정
		String auth;
		if (SecurityUtils.hasRole("ROLE_ADMIN_1")
				|| SecurityUtils.hasRole("ROLE_ADMIN_2")
				|| SecurityUtils.hasRole("ROLE_ADMIN_3")
				|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			auth = "SYSTEM";
		} else if (SellerUtils.isSellerLogin()) {
			auth = "SELLER";
		} else {
			auth = "";
		}

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);;
		cellStyle.title(titleRow, 0, "구매확정 목록");
		Integer lastColIndex;

		// 엑셀 시트 프레임 설정(컬럼 크기 및 컬럼 명 세팅)
		if (auth == "SYSTEM") {
			sheet.setColumnWidth(0, 3000);
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 8000);
			sheet.setColumnWidth(8, 8000);
			sheet.setColumnWidth(9, 15000);
			sheet.setColumnWidth(10, 2000);
			sheet.setColumnWidth(11, 3000);
			sheet.setColumnWidth(12, 10000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No");
			cellStyle.header(header, 1, "지자체");
			cellStyle.header(header, 2, "구매확정일");
			cellStyle.header(header, 3, "주문번호");
			cellStyle.header(header, 4, "주문자");
			cellStyle.header(header, 5, "아이디");
			cellStyle.header(header, 6, "수취인");
			cellStyle.header(header, 7, "상호명");
			cellStyle.header(header, 8, "카테고리");
			cellStyle.header(header, 9, "답례품정보");
			cellStyle.header(header, 10, "수량");
			cellStyle.header(header, 11, "판매가");
			cellStyle.header(header, 12, "배송정보");

			lastColIndex = header.getLastCellNum() - 1;
		} else if (auth == "SELLER") {
			sheet.setColumnWidth(0, 3000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 8000);
			sheet.setColumnWidth(7, 15000);
			sheet.setColumnWidth(8, 2000);
			sheet.setColumnWidth(9, 3000);
			sheet.setColumnWidth(10, 10000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No");
			cellStyle.header(header, 1, "구매확정일");
			cellStyle.header(header, 2, "주문번호");
			cellStyle.header(header, 3, "주문자");
			cellStyle.header(header, 4, "아이디");
			cellStyle.header(header, 5, "수취인");
			cellStyle.header(header, 6, "카테고리");
			cellStyle.header(header, 7, "답례품정보");
			cellStyle.header(header, 8, "수량");
			cellStyle.header(header, 9, "판매가");
			cellStyle.header(header, 10, "배송정보");

			lastColIndex = header.getLastCellNum() - 1;
		} else {
			sheet.setColumnWidth(0, 3000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 8000);
			sheet.setColumnWidth(7, 8000);
			sheet.setColumnWidth(8, 15000);
			sheet.setColumnWidth(9, 2000);
			sheet.setColumnWidth(10, 3000);
			sheet.setColumnWidth(11, 10000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No");
			cellStyle.header(header, 1, "구매확정일");
			cellStyle.header(header, 2, "주문번호");
			cellStyle.header(header, 3, "주문자");
			cellStyle.header(header, 4, "아이디");
			cellStyle.header(header, 5, "수취인");
			cellStyle.header(header, 6, "상호명");
			cellStyle.header(header, 7, "카테고리");
			cellStyle.header(header, 8, "답례품정보");
			cellStyle.header(header, 9, "수량");
			cellStyle.header(header, 10, "판매가");
			cellStyle.header(header, 11, "배송정보");

			lastColIndex = header.getLastCellNum() - 1;
		}

		// 최상단 row 병합
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while (!exceedTotalRow) {
			if (totalCount == 0) break;
			// 엑셀 시트 구성 데이터 조회(1000건) 및 전처리
			List<OrderList> orderList = orderShippingMapper.getConfirmListByParam(orderParam);
			decryptOrderListNoneMasking(orderList);
			setOrderItemOther(orderList);

			if (orderList.isEmpty()) break;

			for (OrderList orderItem : orderList) {
				Row row = sheet.createRow(rowNum++);
				int cellCount = 0;
				row.setHeight((short) 400);
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(totalCount--));
				if ("SYSTEM".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, orderItem.getLocgovNm());
				}
				cellStyle.data(row, cellCount++, DateUtils.datetime(orderItem.getConfirmDate()));
				cellStyle.data(row, cellCount++, orderItem.getOrderCode());
				cellStyle.data(row, cellCount++, orderItem.getUserName());
				cellStyle.data(row, cellCount++, orderItem.getLoginId());
				cellStyle.data(row, cellCount++, orderItem.getReceiveName());
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, orderItem.getCompanyName());
				}
				cellStyle.data(row, cellCount++, orderItem.getCategoryName());
				String itemName = orderItem.getItemName() + "[" + orderItem.getItemUserCode() + "]";
				if (StringUtils.hasLength(orderItem.getOptions())) {
					itemName += "(" + orderItem.getOptions() + ")";
				}
				if (StringUtils.hasLength(orderItem.getTextOption())) {				// 20260325 필수 추가정보 isb
					itemName += "(필수추가정보:" + orderItem.getTextOption() + ")";
				}
				cellStyle.data(row, cellCount++, itemName);
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(orderItem.getQuantity()) + "개");
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(orderItem.getSaleAmount()));
				if ("Y".equalsIgnoreCase(orderItem.getMobileItemYn())) {
					if (StringUtils.hasLength(orderItem.getMobileNumber())) {
						cellStyle.data(row, cellCount++, "모바일 : " + orderItem.getMobileNumber());
					} else {
						cellStyle.data(row, cellCount++, "모바일 번호 없음");
					}
				} else {
					if (StringUtils.hasLength(orderItem.getDeliveryNumber())) {
						cellStyle.data(row, cellCount++, "송장번호 : " + orderItem.getDeliveryNumber() + "(" + orderItem.getDeliveryCompanyName() + ")");
					} else {
						cellStyle.data(row, cellCount++, "송장 번호 없음");
					}
				}
			}

			// 객체참조시점 issue로 변수 설정
			orderParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}

		// oderParam 암호화
		orderParam.decrypt(orderParamEncryptor);

		return workbook;
	}

	@Override
	public SXSSFWorkbook streamShippingReadyData(OrderParam orderParam, boolean isMobile) {
		if (UserUtils.isManagerLogin()) {
			String locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			if (StringUtils.hasLength(locgovCode)) {
				orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovCode));
				orderParam.setShLocgovCode(locgovCode);
			}
		}
		orderParam.encrypt(orderParamEncryptor);
		if (ObjectUtils.isEmpty(orderParam.getSearchDateType())) {
			orderParam.setSearchDateType("OS.SHIPPING_READY_DATE");
		}

		Pagination pagination = Pagination.getInstance(0);
		orderParam.setPagination(pagination);
		orderParam.setLanguage(CommonUtils.getLanguage());

		// 엑셀 다운로드 workbook setting
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;

		// 전체 데이터 count
		orderParam.getPagination().setItemsPerPage(pageSize);
		orderParam.getPagination().setCurrentPage(offset);

		int totalCount = orderShippingMapper.getShippingReadyCountByParam(orderParam);

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// workbook 생성을 위한 offset 및 pageSize 설정
		pageSize 		= 1000;
		offset 			= 1;

		orderParam.getPagination().setItemsPerPage(pageSize);
		orderParam.getPagination().setCurrentPage(offset);

		//택배사 목록 가져오기
		List<DeliveryCompany> deliveryCompanyList =  deliveryCompanyMapper.getActiveDeliveryCompanyListAll();
		String companyList = "";
		for(DeliveryCompany company : deliveryCompanyList) {
			companyList += company.getDeliveryCompanyId()+":"+company.getDeliveryCompanyName()+System.getProperty("line.separator");
		}

		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);
		Sheet sheet;

		if (isMobile) {
			sheet = workbook.createSheet("SHIPPING_READY_MOBILE_LIST");
		} else {
			sheet = workbook.createSheet("SHIPPING_READY_LIST");
		}

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);;
		if (isMobile) {
			cellStyle.title(titleRow, 0, "발송준비(모바일)목록");
		} else {
			cellStyle.title(titleRow, 0, "배송준비목록");
		}
		Integer lastColIndex;

		if (isMobile) {
			// 엑셀 시트 프레임 설정(컬럼 크기 및 컬럼 명 세팅)
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 3000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 15000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 3000);
			sheet.setColumnWidth(8, 3000);
			sheet.setColumnWidth(9, 8000);
			sheet.setColumnWidth(10, 10000);
			sheet.setColumnWidth(11, 10000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "주문번호");
			cellStyle.header(header, 1, "주문순번");
			cellStyle.header(header, 2, "주문자명");
			cellStyle.header(header, 3, "주문자핸드폰");
			cellStyle.header(header, 4, "상품명(옵션)");
			cellStyle.header(header, 5, "상품번호");
			cellStyle.header(header, 6, "수량");
			cellStyle.header(header, 7, "개당 가격");
			cellStyle.header(header, 8, "수령인");
			cellStyle.header(header, 9, "수령인전화번호");
			cellStyle.header(header, 10, "배송시 요청사항");
			cellStyle.header(header, 11, "모바일번호");

			lastColIndex = header.getLastCellNum() - 1;
		} else {
			// 엑셀 시트 프레임 설정(컬럼 크기 및 컬럼 명 세팅)
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 3000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 8000);
			sheet.setColumnWidth(4, 15000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 3000);
			sheet.setColumnWidth(8, 3000);
			sheet.setColumnWidth(9, 8000);
			sheet.setColumnWidth(10, 5000);
			sheet.setColumnWidth(11, 15000);
			sheet.setColumnWidth(12, 10000);
			sheet.setColumnWidth(13, 10000);
			sheet.setColumnWidth(14, 10000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "주문번호");
			cellStyle.header(header, 1, "주문순번");
			cellStyle.header(header, 2, "주문자명");
			cellStyle.header(header, 3, "주문자핸드폰");
			cellStyle.header(header, 4, "상품명(옵션)");
			cellStyle.header(header, 5, "상품번호");
			cellStyle.header(header, 6, "수량");
			cellStyle.header(header, 7, "개당 가격");
			cellStyle.header(header, 8, "수령인");
			cellStyle.header(header, 9, "수령인전화번호");
			cellStyle.header(header, 10, "수령인우편번호");
			cellStyle.header(header, 11, "수령인주소");
			cellStyle.header(header, 12, "배송시 요청사항");
			cellStyle.header(header, 13, "택배사(업로드 등록시 숫자로 입력)", "택배사" + System.getProperty("line.separator") + companyList, sheet);
			cellStyle.header(header, 14, "운송장번호");

			lastColIndex = header.getLastCellNum() - 1;
		}

		// 최상단 row 병합
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while (!exceedTotalRow) {
			if (totalCount == 0) break;
			// 엑셀 시트 구성 데이터 조회(1000건) 및 전처리
			List<OrderList> orderList = orderShippingMapper.getShippingReadyListByParam(orderParam);
			decryptOrderListNoneMasking(orderList);
			setOrderItemOther(orderList);

			if (orderList.isEmpty()) break;

			for (OrderList orderItem : orderList) {
				Row row = sheet.createRow(rowNum++);
				int cellCount = 0;
				row.setHeight((short) 400);
				cellStyle.data(row, cellCount++, orderItem.getOrderCode());
				cellStyle.data(row, cellCount++, Integer.toString(orderItem.getItemSequence()));
				cellStyle.data(row, cellCount++, orderItem.getUserName());
				cellStyle.data(row, cellCount++, orderItem.getMobile());
				cellStyle.data(row, cellCount++, orderItem.getItemName() + (!ObjectUtils.isEmpty(orderItem.getOptions()) ? " [" + ShopUtils.viewItemOptions(orderItem.getSetItemFlag(), orderItem.getOptions()).replaceAll("<br/>", "\n") + "]" : ""));
				cellStyle.data(row, cellCount++, orderItem.getItemUserCode());
				cellStyle.data(row, cellCount++, String.valueOf(orderItem.getQuantity()));
				cellStyle.data(row, cellCount++, String.valueOf(orderItem.getSalePrice()));
				cellStyle.data(row, cellCount++, orderItem.getReceiveName());
				cellStyle.data(row, cellCount++, orderItem.getReceiveMobile());
				if (!isMobile) {
					cellStyle.data(row, cellCount++, orderItem.getReceiveNewZipcode());
					cellStyle.data(row, cellCount++, orderItem.getReceiveAddress() + " " + orderItem.getReceiveAddressDetail());
				}
				cellStyle.data(row, cellCount++, orderItem.getMemo());
				if (isMobile) {
					cellStyle.data(row, cellCount++, orderItem.getReceiveMobile());
				} else {
					cellStyle.data(row, cellCount++, "");
					cellStyle.data(row, cellCount++, "");
				}
			}

			// 객체참조시점 issue로 변수 설정
			orderParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}
		// oderParam 암호화
		orderParam.decrypt(orderParamEncryptor);

		return workbook;
	}
}