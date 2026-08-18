package saleson.batch.job;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.onlinepowers.framework.common.ServiceType;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.notification.NotificationService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;

import saleson.batch.sweettracker.SmartDeliveryBatchService;
import saleson.common.bix5.Bix5Service;
import saleson.common.enumeration.OrderLogType;
import saleson.common.nuri2.Nuri2NrmsgData;
import saleson.common.nuri2.Nuri2Service;
import saleson.common.opmanager.count.OpmanagerMainCount;
import saleson.common.utils.LocalDateUtils;
import saleson.shop.cart.CartMapper;
import saleson.shop.config.ConfigMapper;
import saleson.shop.config.domain.Config;
import saleson.shop.designateddonation.DesignatedDonationService;
import saleson.shop.donation.NgDonationBatchService;
import saleson.shop.give.givestate.domain.CallState;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.givepointexpiration.GivePointExpirationService;
import saleson.shop.group.GroupService;
import saleson.shop.group.domain.Group;
import saleson.shop.item.ItemMapper;
import saleson.shop.item.ItemService;
import saleson.shop.main.MainMapper;
import saleson.shop.main.MainService;
import saleson.shop.nhapi.NhApiBatchService;
import saleson.shop.offgive.OffgiveBatchService;
import saleson.shop.order.OrderBatchService;
import saleson.shop.order.OrderMapper;
import saleson.shop.order.OrderService;
import saleson.shop.order.claimapply.OrderClaimApplyService;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.payment.OrderPaymentMapper;
import saleson.shop.order.shipping.OrderShippingMapper;
import saleson.shop.order.support.OrderParam;
import saleson.shop.point.PointService;
import saleson.shop.point.support.OrderPointParam;
import saleson.shop.remittance.RemittanceMapper;
import saleson.shop.remittance.RemittanceService;
import saleson.shop.statistics.ShopStatisticsBatchService;
import saleson.shop.user.UserMapper;
import saleson.shop.user.UserService;
import saleson.shop.userlevel.UserLevelMapper;
import saleson.shop.userlevel.domain.UserLevel;

@Service("jobService")
public class JobServiceImpl extends EgovAbstractServiceImpl implements JobService {


	private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

	/*
	@Autowired
	private SchedulingMapperBatch schedulingMapperBatch;

	@Autowired
	private SchedulingMapper schedulingMapper;
	*/

	@Autowired
	SequenceService sequenceService;

	@Autowired
	@Qualifier("mailService")
	NotificationService mailService;

	@Autowired
	OrderBatchService orderBatchService;

	@Autowired
	ConfigMapper configMapper;

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	UserService userService;

	@Autowired
	private PointService pointService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private OrderShippingMapper orderShippingMapper;

	@Autowired
	private RemittanceMapper remittanceMapper;

	@Autowired
	private OrderService  orderService;

	@Autowired
	GroupService groupService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	UserLevelMapper userLevelMapper;

	@Autowired
	OrderClaimApplyService orderClaimApplyService;

	@Autowired
	OrderPaymentMapper orderPaymentMapper;

	@Autowired
	private GivePointExpirationService givePointExpirationService;

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private RemittanceService remittanceService;

	@Autowired
	private NgDonationBatchService ngDonationBatchService;

	@Autowired
	private MainMapper mainMapper;

	@Autowired
	private NhApiBatchService nhApiBatchService;

	@Autowired
	private MainService mainService;

	@Autowired
	private ShopStatisticsBatchService shopStatisticsBatchService;

	@Autowired
	private OffgiveBatchService offgiveBatchService;

	@Autowired
	private SmartDeliveryBatchService smartDeliveryBatchService;

	@Autowired
	private DesignatedDonationService designatedDonationService;

	@Autowired
	private Nuri2Service nuri2Service;

	@Autowired
	private Bix5Service bix5Service;

	@Override
	public void itemKeywordBatch() {
		log.debug("BATCH EXCUTION");
		//itemService.setKeywordDaily();
	}

	@Override
	public void autoConfirmPurchaseBatch() {
		log.debug("$$$$$$$$[배치] 구매확정 - 오픈 시 주석 해제 !!!");

		Config shopConfig = configMapper.getShopConfig(Config.SHOP_CONFIG_ID);
		String confirmPurchaseDate = shopConfig.getConfirmPurchaseDate();	// 구매확정일 기준. 7일

		OrderParam orderParam = new OrderParam();
		orderParam.setConfirmPurchaseDate(confirmPurchaseDate);
		orderParam.setConditionType("AUTO_CONFIRM");

		/*
			주문상태코드 = 1 (정상)
			환불유무 = N
			주문상태 (35:배송완료, 58: 교환배송완료, 59:교환거절, 69:반품거절)
			배송완료일이 7일이상 된것
		*/
		List<OrderItem> list = orderShippingMapper.getBuyConfirmDelayListByParam(orderParam);

		if (list != null) {


			// 중복되지 않게 addUserId
			HashSet<Long> userIds = new HashSet<>();
			for (OrderItem orderItem : list) {

				userIds.add(orderItem.getUserId());

				orderParam.setOrderCode(orderItem.getOrderCode());
				orderParam.setOrderSequence(orderItem.getOrderSequence());
				orderParam.setItemSequence(orderItem.getItemSequence());

				// 구매확정일 조회
				orderParam.setRemittanceDate(remittanceMapper.getRemittanceDateBySellerId(orderItem.getSellerId()));

				// 구매확정
				if (orderShippingMapper.updateConfirmPurchase(orderParam) > 0) {

					// 배송비 정보에 정산 예정일을 업데이트
					orderParam.setShippingSequence(orderItem.getShippingSequence());
					orderShippingMapper.updateShippingRemittanceDate(orderParam);

					// 주문 로그
					orderService.insertOrderLog(
							OrderLogType.ORDER_BATCH,
							orderItem.getOrderCode(),
							orderItem.getOrderSequence(),
							orderItem.getItemSequence(),
							orderItem.getOrderStatus()
					);
				}
			}

			// 포인트 적립
			OrderPointParam opp = new OrderPointParam();
			for (Long userId : userIds) {
				opp.setUserId(userId);
				pointService.savePointByOrderPointParam(opp);
			}

		}
	}

	@Override
	public void expirationPointBatch() {
		log.debug("$$$$$$$$[배치] 포인트 만료 - 오픈 시 주석 해제 !!!");
		pointService.expirationPoint();
	}

	@Override
	public void expirationPointSendMessageBatch() {
		givePointExpirationService.givePointExpirationMailSend();
	}

	@Override
	public void sendMailToInactiveUserBatch() {

		if (!ServiceType.PRODUCTION) {
			userService.sendSleepUserMail();
		}
	}

	@Override
	public void processInactiveUserBatch() {
		try {
			userService.setSleepUser();
		} catch (OpRuntimeException e) {
			log.error("[processInactiveUserBatch] [배치] 휴면계정 전환처리 오류");
		}

	}

	@Override
	public void updateSleepManager() {
		try {
			userService.updateSleepManager();
		} catch (OpRuntimeException e) {
			log.error("[updateSleepManager] [배치] 관리자 휴면계정 전환처리 오류");
		}

	}

	@Override
	public void sendOrderSmsToSellerBatch() {			// 미사용 주석처리
	}

	@Override
	public void updateItemOptionSoldoutBatch() {
		itemService.updateItemOptionSoldout();
	}

	@Override
	public void itemRankingType1Batch() {			// 미사용 주석처리
	}

	@Override
	public void itemRankingType2Batch() {			// 미사용 주석처리
	}

	@Override
	public void itemRankingType3Batch() {			// 미사용 주석처리
	}

	@Override
	public void couponRegularBatch() {			// 미사용 주석처리

	}


	@Override
	public void userLevelBatch() {
		// 배치 실행일로 부터 1일 전 (배치는 새벽에 실행. 만료일은 어제)

		//String expirationDate = LocalDateUtils.localDateToString(LocalDate.now().minusDays(1L));
		//userLevelBatch(expirationDate);
	}

	@Override
	public void userLevelBatch(String expirationDate) {
		// 1. Group, UserLevel 정보 조회
		List<Group> groups = groupService.getGroupsAndUserLevelsAll();

		for (Group group : groups) {

			// 2. 등급 조건에 맞는 회원 정보를 조회하여 등급 업데이트 (등급 재산정 대상 회원 기준)
			int i = 0;
			boolean shouldExcuteLastUserLevel = true;
			for (UserLevel userLevel : group.getUserLevels()) {
				i++;

				userLevel.setExpirationDate(expirationDate);

				// 2.1. 해당 등급의 조건에 맞는 회원수를 조회 (주문데이터 등) - 로그 일괄 등록을 위해 필요 (데이터가 없는 경우 insert 오류 방지)
				//      제일 낮은 등급의 구매금액 시작 조건(priceStart) == 0 인 경우 나머지 회원(등급 산정이 안된 모든 회원을 제일 낮은 등급으로 일괄처리
				if (i == group.getUserLevels().size() && userLevel.getPriceStart() == 0) {
					userLevel.setUserLevelProcessType("ALL_OTHER");
					shouldExcuteLastUserLevel = false;		// 마지막 등급의 금액 시작이 0이 아닌 경우는 등급없음 상태로 처리해야함.
				}
				processUserLevel(userLevel);
			}


			// 3. 등급 조건에 해당하지 않는 회원은 '등급없음'으로 업데이트 (등급 재산정 대상 회원 기준)
			if (shouldExcuteLastUserLevel) {
				UserLevel userLevel = new UserLevel();
				userLevel.setExpirationDate(expirationDate);
				userLevel.setGroupCode(group.getGroupCode());
				userLevel.setLevelId(0);
				userLevel.setLevelName("등급없음");
				userLevel.setRetentionPeriod(1);                // 등급없음은 1달간 유지

				// 3.1. 해당 그룹의 회원이 등급 산정이 안된 경우 일괄 업데이트 (userLevel = 0 : 등급없음, 등급 유지 기간 1달)
				processUserLevel(userLevel);
			}
		}
	}

	/**
	 * 회원 등급 업데이트
	 * @param userLevel
	 */
	private void processUserLevel(UserLevel userLevel) {


		// 회원 등급 로그 일괄 업데이트
		int updateCount = userMapper.insertUserLevelLogByUserLevel(userLevel);

		// 2. 업데이트 데이터가 있는 경우 - 해당 등급의 조건에 맞는 회원을 조회하여 일괄 업데이트
		if (updateCount > 0) {
			// 2.1. 회원 등급 로그 일괄 업데이트
			userMapper.updateUserDetailByUserLevel(userLevel);
		}
	}

	@Override
	public void cancelWaitingDepositOrderBatch() {			// 미사용 주석처리

	}

	@Override
	public void autoCompleteKeywordBatch() {
		//
	}

	@Override
	public void updateKakaoAlimTalkBatch() {			// 미사용 주석처리
	}

	@Override
	public void updateUserCampaignBatch() {			// 미사용 주석처리

//		try {
//			campaignService.insertCampaignBatch();
//		} catch (OpRuntimeException e) {
//			log.error("[updateUserCampaignBatch] 캠페인용 유저 배치 오류 : {}", e);
//		}
	}

	@Override
	public void sendCampaignMessageBatch() {			// 미사용 주석처리

//		try {
//			campaignService.insertCampaignMessageBatch();
//		} catch (OpRuntimeException e) {
//			log.error("[sendCampaignMessageBatch] 캠페인용 예약발송 발송처리 배치 오류 : {}", e);
//		}
	}

	@Override
	public void updateCampaignSentBatch() {			// 미사용 주석처리

	}

	@Override
	public void autoConfirmPurchaseUmsBatch() {			// 미사용 주석처리
	}

	@Override
	public void autoConfirmPurchaseRequestUmsBatch() {			// 미사용 주석처리

	}

    @Override
    public void deleteOrderTempInfoBatch() {

        try {
            Config config = configMapper.getShopConfig(Config.SHOP_CONFIG_ID);
            int retentionPeriod = config.getRetentionPeriod();

            // 주문 임시 저장 정보 삭제
            orderMapper.deleteOrderTempForBatch(retentionPeriod);
            orderMapper.deleteOrderItemBuyTempForBatch(retentionPeriod);
            orderMapper.deleteOrderPaymentBuyTempForBatch(retentionPeriod);
            orderMapper.deleteOrderShippingBuyTempForBatch(retentionPeriod);

        } catch (OpRuntimeException e) {
            log.error("[deleteOrderTempInfoBatch] 임시 주문 정보 삭제처리 배치 오류 : [{}] - {}", e);
        }
    }

	@Override
	public void expirationCouponSendMessageBatch() {			// 미사용 주석처리
	}

	public void expirationPointMessageBatch() {			// 미사용 주석처리
	}

	@Override
	public void updateLockForManagerBatch() {
		try {
			userService.updateLockForManager();
		} catch (OpRuntimeException e) {
			log.error("[updateLockForManagerBatch] 관리자 잠금 배치 오류 오류 : [{}] - {}", e);
		} catch (Exception e) {
			log.error("[updateLockForManagerBatch] 관리자 잠금 배치 오류 오류 : [{}] - {}", e);
		}
	}

	@Override
	public void expirationCntrPointBatch() {

		try {
			givePointExpirationService.givePointExpiration();
		} catch (OpRuntimeException e) {
			log.error("[expirationCntrPointBatch] [배치] 기부 포인트 만료 오류", e);
		}
	}

	/**
	 * 기부 데이터 미결재분 삭제(익월 자정까지 미결재시 삭제 처리)
	 */
	@Override
	public void deleteCntrDataBatch() {

		try {
			ngDonationBatchService.deleteCntrData();
		} catch (OpRuntimeException e) {
			log.error("[deleteCntrDataBatch] [배치] 기부 데이터 미결재분 삭제 배치 오류 : [{}] - {}", e.getErrorMessage(), e);
		}
	}

	/**
	 * 국세청 전자기부영수증 데이터 처리(online)
	 */
	@Override
	public void sendNtsEreceiptOnBatch() {

		try {
			ngDonationBatchService.sendNtsEreceiptOnBatch();
		} catch (OpRuntimeException e) {
			log.error("[sendNtsEreceiptOnBatch] [배치] 국세청 전자기부영수증 데이터(online) 처리 배치 오류 : [{}] - {}", e);
		}
	}


	/**
	 * 국세청 전자기부영수증 데이터 처리(offline)
	 */
	@Override
	public void sendNtsEreceiptOffBatch() {

		try {
			ngDonationBatchService.sendNtsEreceiptOffBatch();
		} catch (OpRuntimeException e) {
			log.error("[sendNtsEreceiptOffBatch] [배치] 국세청 전자기부영수증 데이터(offline) 처리 배치 오류 : [{}] - {}", e);
		}
	}

	@Override
	public void sendNtsEreceiptBatchOld() {

		try {
			ngDonationBatchService.sendNtsEreceiptBatchOld();
		} catch (OpRuntimeException e) {
			log.error("[sendNtsEreceiptOffBatch] [배치] 국세청 전자기부영수증 데이터 old 처리 배치 오류 : [{}] - {}", e);
		}
	}

	/**
	 * 국세청 영수증처리 테스트
	 */
	@Override
	public void sendNtsEreceiptOnBatchTest() {

		try {
			ngDonationBatchService.sendNtsEreceiptOnBatchTest();
		} catch (OpRuntimeException e) {
			log.error("[sendNtsEreceiptOnBatch] [배치] 국세청 전자기부영수증 데이터(online) 처리 배치 오류 : [{}] - {}", e);
		}
	}

	/**
	 * 지방세외(현세대) 미납건 데이터 삭제 처리
	 */
	@Override
	public void deleteNotSunapStndBatch() {

		try {
			ngDonationBatchService.deleteNotSunapStndBatch();
		} catch (OpRuntimeException e) {
			log.error("[deleteNotSunapStndBatch] [배치] 지방세외(현세대) 미납건 데이터 삭제 처리 배치 오류 : [{}] - {}", e);
		}
	}

	/**
	 * 배송완료 처리	(30:배송중 -> 35, 55:교환배송중 -> 58)
	 * 2023.02.09
	 */
	@Override
	public void autoShippingCompleteBatch() {

		Config shopConfig = configMapper.getShopConfig(Config.SHOP_CONFIG_ID);
		String shippingCompleteDate = shopConfig.getShippingCompleteDate();	// 배송완료일 기준. 7일

		OrderParam orderParam = new OrderParam();
		orderParam.setShippingCompleteDate(shippingCompleteDate);

		/*
			주문상태코드 = 1 (정상)
			환불유무 = N
			주문상태 (30:배송중 -> 35, 55:교환배송중 -> 58)
			배송시작일이 7일이상 된것
		*/
		List<OrderItem> list = orderShippingMapper.getBuyShippingCompleteListByParam(orderParam);

		if (list != null) {
			for (OrderItem orderItem : list) {
				orderParam.setOrderCode(orderItem.getOrderCode());
				orderParam.setOrderSequence(orderItem.getOrderSequence());
				orderParam.setItemSequence(orderItem.getItemSequence());

				// 배송완료 코드 셋팅 (30:배송중 -> 35:배송완료, 55:교환배송중 -> 58:교환배송완료)
				orderParam.setChangeOrderStatus("30".equals(orderParam.getOrderStatus()) ? "35" : "58");

				// 배송완료
				if (orderShippingMapper.updateShppingComplete(orderParam) > 0) {

					// 교환배송완료 시 교환요청내역 클레임상태 04(완료) 로 업데이트
					if ("55".equals(orderParam.getOrderStatus())) {
						orderShippingMapper.updateOpOrderExchangeApplyClaimStatus(orderParam);
					}

					// 주문 로그
					orderService.insertOrderLog(
//							OrderLogType.SHIPPING_BATCH,		// 로그 남지 않아서 수정
							OrderLogType.ORDER_BATCH,
							orderItem.getOrderCode(),
							orderItem.getOrderSequence(),
							orderItem.getItemSequence(),
							orderItem.getOrderStatus()
					);
				}
			}
		}
	}

	/**
	 * 자동 배송완료처리(스마트택배)
	 * 30:배송중 -> 35, 55:교환배송중 -> 58
	 * 2025.11.14
	 */
	@Override
	public void autoSmartDeliveryShippingCompleteBatch() {

		/* 배송완료일 기준 추출 */
		Config shopConfig = configMapper.getShopConfig(Config.SHOP_CONFIG_ID);
		String setSmartDeliveryCriteriaDate = shopConfig.getSmartDeliveryCriteriaDate();	// 배송조회 기준. 2일

		OrderParam orderParam = new OrderParam();
		orderParam.setShippingCompleteDate(setSmartDeliveryCriteriaDate);

		/* 총 count 조회 */
		int listCount = orderShippingMapper.getBuyShippingCompleteListCount(orderParam);
		int listPerPage = (int) Math.ceil(listCount / 100.0);


		int successCount = 0;

		/* 100건씩 조회하여 update */
		for(int i = 0; i < listPerPage; i++) {
			orderParam.setPage(i * 100);
			successCount += smartDeliveryBatchService.smartDeliveryShippingCompleteBatch(orderParam);
		}

		log.info("[스마트택배 완료] SUCCESS COUNT {} 건, FAIL COUNT {} 건 ",successCount, listCount-successCount);
	}



	/**
	 * 2023.02.22 상품 NEW 삭제 배치
	 */
	@Override
	public void deleteItemNewBatch() {

		try {
			itemMapper.deleteItemNew();
		} catch (OpRuntimeException e) {
			log.error("[deleteItemNewBatch] [배치] 2023.02.22 상품 NEW 삭제 배치 오류 : [{}] - {}", e);
		}
	}

	/**
	 * 2023.03.24 장바구니 30일 지난 데이터 삭제 처리
	 */
	@Override
	public void deleteCartBatch() {

		try {
			cartMapper.deleteCartBatch();
		} catch (OpRuntimeException e) {
			log.error("[deleteCartBatch] [배치] 2023.03.24 장바구니 30일 지난 데이터 삭제 처리 배치 오류 : [{}] - {}", e);
		}

	}

	/**
	 * 답례품 제공자 정산예정확인 문자 전송 배치 매월 1일 9시
	 */
	@Override
	public void sendRemittanceExpectedMsgBatch() {
		remittanceService.sendRemittanceExpectedMsg();
 	}

	@Override
	public void confirmSunapForDeletedListBatch() {

		try {
			ngDonationBatchService.confirmSunapForDeletedList();
		} catch (OpRuntimeException e) {
			log.error("[confirmSunapForDeletedList] [배치] 취소된 기부건 재 수납 배치 오류 : [{}]", e);
		}

	}

	@Override
	public void confirmdonationForReportBatch() {

		GiveState searchParam = new GiveState();
		 try {
			  Map<String, Object> boardMap = new HashMap<>();
			  String today = DateUtils.getToday();
			  searchParam.setShCntrDeStart(today);
			  long gift =  mainService.opmanageMainAmountGift(searchParam);

			  searchParam.setShCntrDeStart(today);
			  List<OpmanagerMainCount> mainInfo =mainService.getOpmanagerMainTableInfo(searchParam);

			  CallState callInfo = mainService.getOpmanagerMainCallTableInfo(searchParam);
			  for(OpmanagerMainCount main: mainInfo) {
				  boardMap.put(main.getId(),main.getCount());
			  }
			  String locgovCd = null;
			  Map<String, String> param = new HashMap<>();
			  param.put("today", LocalDateUtils.localDateToString(LocalDate.now()));
			  param.put("locgovCd", locgovCd);
			  param.put("userRole", "SYS");

			  List<OpmanagerMainCount> user = mainMapper.getOpmanagerMainInfo(param);

			  for(OpmanagerMainCount amount: user) {
				  if(amount.getId().equals("all-subscribers")) {
					  boardMap.put("userAmt", amount.getCount());
				  }
			  }
			  boardMap.put("createdDate",DateUtils.getToday("yyyyMMdd"));
			  boardMap.put("callKookmin", callInfo.getCallKookmin());
			  boardMap.put("callNhbank", callInfo.getCallNhbank());
			  boardMap.put("callLov", callInfo.getCallLov());
			  boardMap.put("callGiver", callInfo.getCallGiver());
			  boardMap.put("callAmt", callInfo.getCallTotal());
			  boardMap.put("present", gift);
			  boardMap.put("deleteDate",today);
			  mainMapper.deleteDashBoardReport(boardMap);
			  mainMapper.confirmDonationForReportBatch(boardMap);




		 } catch(OpRuntimeException e) {
			 log.error("[confirmdonationForReport] [배치] 기부내역,콜수 현황 6시 기준 배치 오류: [{}]",e);

		 }
	}

	@Override
	public void userBirthdayDecBatch() {
		try {
			userService.setUserBirthdayDecList();
		} catch (OpRuntimeException e) {
			log.error("[visitStatsBatch] [배치] : [{}]", e);
		}

	}

	@Override
	public void userCntrForNhBatch() {
		try {
			nhApiBatchService.setUserCntrForNh();
		} catch (OpRuntimeException e) {
			log.error("[setUserCntrForNh] [배치] : [{}]", e);
		}
	}

	@Override
	public void userCntrLocForNhBatch() {
		try {
			nhApiBatchService.setUserCntrLocForNh();
		} catch (OpRuntimeException e) {
			log.error("[userCntrLocForNhBatch] [배치] : [{}]", e);
		}
	}

	@Override
	public void locForNhBatch() {
		try {
			nhApiBatchService.setLocForNh();
		} catch (OpRuntimeException e) {
			log.error("[locForNhBatch] [배치] : [{}]", e);
		}
	}

	@Override
	public void getNoBugaLocgovListBatch() {
		ngDonationBatchService.getNoBugaLocgovList();
	}

	@Override
	public void cntrTaxTempBatch() {
		ngDonationBatchService.cntrTaxTempBatch();
	}

	@Override
	public void cntrSunapBatch() {
		ngDonationBatchService.cntrSunapBatch();
	}

	@Override
	public void opUserBirthdayStatBatch() {
		try {
			userService.setOpUserBirthdayStat();
		} catch (OpRuntimeException e) {
			log.error("[setOpUserBirthdayStat] [배치] : [{}]", e);
		}

	}

	/**
	 * 월간 통계: 매월 1일 7시
	 */
	@Override
	public void statisticsReportBatch() {
		try {
			shopStatisticsBatchService.setStatisticsReport();
		} catch (OpRuntimeException e) {
			log.error("[setStatisticsReport] [배치] : [{}]", e);
		}

	}

	/**
	 * 연간 통계: 매년 2월 28일
	 */
	@Override
	public void statisticsYearReportBatch() {
		log.info(">> statisticsYearReportBatch() >>");
		try {
			shopStatisticsBatchService.setStatisticsYearReport();
		} catch (OpRuntimeException e) {
			log.error("[statisticsYearReportBatch] [배치] : [{}]", e);
		}
	}

	/**
	 * 연간 통계: 매년 2월 28일 - 연령별 월별
	 */
	@Override
	public void statisticsYearReportAgeMonthBatch() {
		log.info(">> statisticsYearReportBatch() >>");
		try {
			shopStatisticsBatchService.statisticsYearReportAgeMonthBatch();
		} catch (OpRuntimeException e) {
			log.error("[statisticsYearReportBatch] [배치] : [{}]", e);
		}
	}

	/**
	 * 5분에 한번씩 세외 실시간 확인 후 수납 처리
	 */
	@Override
	public void fiveMinuteSunapCheckBatch() {
		log.info(">> fiveMinuteSunapCheckBatch() >>");
		try {
			ngDonationBatchService.cntrNonSunapFiveMinSunapCheck();
		} catch (OpRuntimeException e) {
			log.error("[cntrNonSunapTenSecList] [배치] : [{}]", e);
		}
	}

	@Override
	public void dayCompleteSunapCheckBatch() {
		log.info(">> dayCompleteSunapCheckBatch() >>");
		try {
			ngDonationBatchService.cntrNonSunapdayCompleteSunapCheck();
		} catch (OpRuntimeException e) {
			log.error("[dayCompleteSunapCheckBatch] [배치] : [{}]", e);
		}
	}


	@Override
	public void itemDisplayContorlBatch() {
		log.info(">> itemDisplayContorlBatch() >>");
		try {
			ngDonationBatchService.itemDisplayContorlCheck();
		} catch (OpRuntimeException e) {
			log.error("[itemDisplayContorlBatch] [배치] : [{}]", e);
		}

	}

	@Override
	public void allDayOffItemUpdateBatch() {
		log.info(">>->> process allDayOffItemUpdateBatch() >>->>");
		try {
			offgiveBatchService.ItemStatusUpdateBatch();
		} catch (OpRuntimeException e) {
			log.error("[allDayOffItemUpdateBatch] [배치] : [{}]", e.getErrorCode());
		}
	}

	/**
	 * 총 기부금
	 * 매일 00시 10분 동작
	 * 작년 1월 1일 ~ 당일 - 1년 - 1일 총 기부금 : last_year_gramt
	 * 올해 1월 1일 ~ 당일 -1일 총 기부금 : now_year_gramt
	 * */
	@Override
	public void dailyGramtInsertBatch() {
		// 총 기부금 테이블(G_DY_GRAMT)에 Insert 하기 위한 데이터 Map
		Map<Object, Object> sendData = new HashMap<>();
		// 작년 총 기부금
		Long lastYearGramt = mainMapper.getGiveTotalAmt("prev");
		// 올해 총 기부금
		Long nowYearGramt = mainMapper.getGiveTotalAmt("now");
		sendData.put("lastYearGramt", lastYearGramt);
		sendData.put("nowYearGramt", nowYearGramt);

		try {
			// 총 기부금 테이블에 작년, 올해 총기부금 Insert
			log.info("■■■■■■■■ DaliyGramtInsertBatch() ■■■■■■■■");
			mainMapper.insertDyGramtInfo(sendData);
		}catch(Exception e) {
			log.error("[dailyGramtInsert] [배치] : [{}]", e);
		}
	}

	/**
	 * 넷퍼넬 대기자 수 배치
	 * n분마다 mainServiceImpl의 Cache 업데이트
	 * */
	@Override
	public void updateWaitUserCacheBatch() {
		try {
			log.error("■■■■■■■■ updateWaitUserCacheBatch() ■■■■■■■■");
			mainService.updateWaitUser();
			log.error("■■■■■■■■ Update Time : {} ■■■■■■■■", LocalDateTime.now());
		}catch(Exception e) {
			log.error("■■■■■■■■ updateWaitUserCacheBatch Error■■■■■■■■,  [배치] : [{}]", e);
		}
	}

	/**
	 * 특정기부사업 상태 진행 -> 종료 배치
	 * 특정기부사업 목표금액이 완료시 30분마다 한번씩 체크후
	 * 종료상태로 변경
	 */
	@Override
	public void dsgnBizStatusUpdateBatch() {
		try {
			designatedDonationService.updateDesinatedDonationStatus();
			log.error("■■■■■■■■ Update Time : {} ■■■■■■■■", LocalDateTime.now());
		} catch (Exception e) {
			log.error("■■■■■■■■ dsgnBizStatusUpdateBatch Error ■■■■■■■■, [배치] : [{}]", e);
		}
	}

	@Override
	public void sendNuri2PresentOrder() {

		// 고객명단
		List<Map<String,Object>> order_list = nuri2Service.selectPresentOrderList();

		for (Map<String, Object> order_info : order_list) {

			Nuri2NrmsgData nuri2NrmsgData = new Nuri2NrmsgData();

			nuri2NrmsgData.setAltTemplateCode("KR002");
			// 수신번호
			nuri2NrmsgData.setPhone(order_info.get("PHONE_NUM").toString());
			// ALT_JSON
			nuri2NrmsgData.setAltJson(nuri2NrmsgData.getOrderContent("TOTAL_PRESENT_ORDER", order_info.get("TOTAL_CNT").toString()) );
			// XMS_TEST
			nuri2NrmsgData.setXmsText(nuri2NrmsgData.getOrderContent("TOTAL_PRESENT_ORDER", order_info.get("TOTAL_CNT").toString()) );

			try {
				nuri2Service.insertAlimtalk(nuri2NrmsgData);
			} catch (Exception e) {
				log.error("■■■■■■■■ sendNuri2PresentOrder Error ■■■■■■■■, [배치] : [{}]", e);
			}
		}
		log.info("■■■■■■■■ sendNuri2PresentOrder Success");
	}

	/**
	 * BIX5 통계를 위한 배치
	 * 1. 기부금액(전년비)
	 * 2. 기부횟수(전년비)
	 */
	@Override
	public void bix5CntrAmt() {
		bix5Service.bix5CntrAmt();
	};

	/**
	 * 3. 평균 기부금액 비교
	 */

	/**
	 * 4. 전년대비 월별 기부금액
	 */
	@Override
	public void bix5CntrAmtMonth() {
		bix5Service.bix5CntrAmtMonth();
	};

	/**
	 * 5. 대상별 기부금액
	 */
	@Override
	public void bix5CntrAmtBiz() {
		bix5Service.bix5CntrAmtBiz();
	};

	/**
	 * 6. 특정사업별 모금현황
	 */
	@Override
	public void bix5CntrBiz() {
		bix5Service.bix5CntrBiz();
	};

	/**
	 * 7. 기부 접수창구별 모금액
	 */
	@Override
	public void bix5CntrPath(){
		bix5Service.bix5CntrPath();
	};

	/**
	 * 8. 기부 금액별 건수
	 */
	@Override
	public void bix5CntrAmtCat() {
		bix5Service.bix5CntrAmtCat();
	};

	/**
	 * 9. 연령대별 기부 건수
	 */
	@Override
	public void bix5CntrOld(){
		bix5Service.bix5CntrOld();
	};

	/**
	 * 10. 기부자 주소지 기준 모금 순위 TOP 10
	 */
	@Override
	public void bix5CntrAddr(){
		bix5Service.bix5CntrAddr();
	}

	/**
	 * 11. 판매 포인트(전년비)
	 */
	@Override
	public void bix5GiftUsePoint() {
		bix5Service.bix5GiftUsePoint();
	}
	/**
	 * 12. 답례품 종 개수
	 */
	@Override
	public void bix5GiftCnt() {
		bix5Service.bix5GiftCnt();
	}
	/**
	 * 13. 평균 판매 포인트 비교
	 */
	@Override
	public void bix5GiftUsePointCpr() {
		bix5Service.bix5GiftUsePointCpr();
	}

	/**
	 * 14. 답례품 등록 현황-카테고리 기준
	 */
	@Override
	public void bix5GiftCat() {
		bix5Service.bix5GiftCat();
	};

	/**
	 * 15. 답례품 등록 현황(포인트 구간 기준)
	 */
	@Override
	public void bix5GiftPoint() {
		bix5Service.bix5GiftPoint();
	};

	/**
	 * 16. 카테고리별 판매수량
	 */
	@Override
	public void bix5GiftCatSell() {
		bix5Service.bix5GiftCatSell();
	};

	/**
	 * 17. 답례품 제공자 발송 소요 시간 TOP 5
	 */
	@Override
	public void bix5GiftDeli() {
		bix5Service.bix5GiftDeli();
	};

	/**
	 * 18. 총 잔여 포인트(소진율)
	 */
	@Override
	public void bix5PointUse() {
		bix5Service.bix5PointUse();
	};

	/**
	 * 19. 연간 발생 포인트
	 */
	@Override
	public void bix5PointUseYear() {
		bix5Service.bix5PointUseYear();
	};

	/**
	 * 20. 포인트 현황
	 */
	@Override
	public void bix5PointYear() {
		bix5Service.bix5PointYear();
	};
}
