package saleson.shop.tempprocess;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.common.enumeration.IdType;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.order.claimapply.support.ClaimApplyParam;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderPayment;
import saleson.shop.order.infra.OrderEncryptor;
import saleson.shop.order.infra.OrderListEncryptor;
import saleson.shop.order.infra.OrderParamEncryptor;
import saleson.shop.order.infra.OrderPaymentEncryptor;
import saleson.shop.order.infra.OrderShippingInfoEncryptor;
import saleson.shop.order.support.OrderException;
import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.domain.RemittanceConfirm;
import saleson.shop.remittance.domain.RemittanceConfirmDetail;
import saleson.shop.remittance.domain.RemittanceConfirmEncryptor;
import saleson.shop.remittance.support.RemittanceException;
import saleson.shop.remittance.support.RemittanceParam;
import saleson.shop.tempprocess.domain.HoldOrderList;
import saleson.shop.tempprocess.domain.HoldOrderListParam;
import saleson.shop.user.LocgovService;

@Slf4j
@RequiredArgsConstructor
@Service("tempProcessService")
public class TempProcessServiceImpl extends EgovAbstractServiceImpl implements TempProcessService {

	@Autowired
	private LocgovService locgovService;	// 지자체 관리	
	
	@Autowired
	private TempProcessMapper tempProcessMapper;

	private final OrderParamEncryptor orderParamEncryptor;

	private final OrderListEncryptor orderListEncryptor;

	private final OrderEncryptor orderEncryptor;

	private final OrderShippingInfoEncryptor orderShippingInfoEncryptor;
	
	private final OrderPaymentEncryptor orderPaymentEncryptor;

	@Autowired
	private RemittanceConfirmEncryptor remittanceConfirmEncryptor;
	
	@Override
	public List<HoldOrderList> getTempOrderListByParam(HoldOrderListParam holdOrderListParam) {
		
		if (UserUtils.isManagerLogin() && "LOC".equals(locgovService.getLoginUserAdminRoleCheck())) {
			// 지자체관리자일 경우 지자체코드 필요
			holdOrderListParam.setShWdr(locgovService.getUpperLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER)));
			holdOrderListParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}		

		holdOrderListParam.encrypt(orderParamEncryptor);
		int totalCount = tempProcessMapper.getTempAllOrderCountByParamForManager(holdOrderListParam);

		Pagination pagination = Pagination.getInstance(totalCount, holdOrderListParam.getItemsPerPage());
		holdOrderListParam.setPagination(pagination);

		List<HoldOrderList> list = tempProcessMapper.getTempAllOrderListByParamForManager(holdOrderListParam);
		decryptOrderList(list);
		holdOrderListParam.decrypt(orderParamEncryptor);

		return list;
	}

	@Override
	public List<RemittanceConfirm> getTempRemittanceConfirmListByParam(RemittanceParam param) {		
		if (ShopUtils.isSellerPage()) {
			param.setConditionType("SELLER_LIST");
		} else {
			param.setConditionType("LIST");
		}
		int totalCount = tempProcessMapper.getTempRemittanceConfirmCountByParamNew(param);
		
		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		param.setPagination(pagination);

		List<RemittanceConfirm> list = tempProcessMapper.getTempRemittanceConfirmListByParamNew(param);

		list.forEach(rc -> {
			rc.decrypt(remittanceConfirmEncryptor, ShopUtils.needMasking());
		});

		return list;
	}

	private void decryptOrderList(List<HoldOrderList> list) {
		if (list != null) {
			list.forEach(ol -> {
				ol.decrypt(orderListEncryptor, ShopUtils.needMasking());
			});
		}
	}

	@Override
	public void setModelOrderDetail(Model model, String rcOrderCode, String mode) {
		HoldOrderListParam holdOrderListParam = new HoldOrderListParam();
		String[] orderCodes = rcOrderCode.split("-");
		holdOrderListParam.setOrderCode(orderCodes[0]);
		holdOrderListParam.setOrderSequence(0);

		holdOrderListParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			holdOrderListParam.setSellerId(SellerUtils.getSellerId());
			holdOrderListParam.setConditionType("SELLER");
		}

		Order order = getOrderByParam(holdOrderListParam);
		if (order == null) {
			throw new PageNotFoundException();
		}

		bindItemDetail(model, holdOrderListParam, "");

		model.addAttribute("order", order);
		model.addAttribute("isSellerPage", ShopUtils.isSellerPage());

		model.addAttribute("mode", mode);
	}
	


	/**
	 * 주문상세에 상품관련 정보를 조회 - 클레임
	 * @param model
	 * @param HoldOrderListParam
	 */
	private void bindItemDetail(Model model, HoldOrderListParam holdOrderListParam, String pageType) {

		ClaimApplyParam claimApplyParam = new ClaimApplyParam();
		claimApplyParam.setOrderCode(holdOrderListParam.getOrderCode());
		claimApplyParam.setOrderSequence(holdOrderListParam.getOrderSequence());

		claimApplyParam.setConditionType("OPMANAGER");
		if (ShopUtils.isSellerPage()) {
			claimApplyParam.setConditionType("SELLER");
			claimApplyParam.setSellerId(SellerUtils.getSellerId());
		}

		if (UserUtils.isManagerLogin() && !isUpperAdmin()) {
			// 지자체관리자일 경우 지자체코드 필요
			claimApplyParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}

//		model.addAttribute("activeExchanges", orderClaimApplyService.getActiveExchangeListByParam(claimApplyParam));
//		model.addAttribute("exchangeHistorys", orderClaimApplyService.getExchangeHistoryListByParam(claimApplyParam));
//
//		model.addAttribute("activeCancels", orderClaimApplyService.getActiveCancelListByParam(claimApplyParam));
//		model.addAttribute("cancelHistorys", orderClaimApplyService.getCancelHistoryListByParam(claimApplyParam));
//
//		model.addAttribute("activeReturns", orderClaimApplyService.getActiveReturnListByParam(claimApplyParam));
//		model.addAttribute("returnHistorys", orderClaimApplyService.getReturnHistoryListByParam(claimApplyParam));
//
//		model.addAttribute("deliveryCompanyList", deliveryCompanyService.getActiveDeliveryCompanyListAll());

		model.addAttribute("cancelClaimReasons", CodeUtils.getCodeInfoList("CANCEL_REASON"));	// 취소사유
		model.addAttribute("exchangeClaimReasons", CodeUtils.getCodeInfoList("EXCHANGE_REASON"));	// 교환사유
		model.addAttribute("returnClaimReasons", CodeUtils.getCodeInfoList("RETURN_REASON"));	// 환불사유

		String viewTabIndex = "0";
		if ("cancel".equals(pageType)) {
			viewTabIndex = "1";
		} else if ("return".equals(pageType)) {
			viewTabIndex = "2";
		} else if ("exchange".equals(pageType)) {
			viewTabIndex = "3";
		}

		model.addAttribute("pageType", pageType);
		model.addAttribute("viewTabIndex", viewTabIndex);
//		model.addAttribute("orderLogs", orderClaimApplyService.getOrderLogList(claimApplyParam));

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

	
	public Order getOrderByParam(HoldOrderListParam holdOrderListParam) {

        Order order = getByParam(holdOrderListParam);

        if (order == null) {
            return null;
        }

        /* 교환, 반품 거절 사유 상품별로 불러오기 */
//        for (OrderShippingInfo shippingInfos : order.getOrderShippingInfos()) {
//            for (OrderItem orderItem : shippingInfos.getOrderItems()) {
//                if("59".equals(orderItem.getOrderStatus())) { //교환 거절
//                    orderItem.setClaimRefusalReasonText(orderClaimApplyMapper.getClaimRefusalReasonText(orderItem));
//                } else if ("69".equals(orderItem.getOrderStatus())) {
//                    orderItem.setClaimRefusalReasonText(orderClaimApplyMapper.getClaimRefusalReasonText(orderItem));
//                }
//                
//                if (orderItem.getDeliveryCompanyId() > 0 && StringUtils.isEmpty(orderItem.getDeliveryCompanyUrl())) {
//                	DeliveryCompany deliveryCompany = deliveryCompanyMapper.getDeliveryCompanyById(orderItem.getDeliveryCompanyId());
//                	orderItem.setDeliveryCompanyUrl(deliveryCompany.getDeliveryCompanyUrl());
//                }
//            }
//        }

        bindOrder(order, holdOrderListParam);
//        setOrderItemOther(order);

        return order;

    }
	
	public Order getByParam(HoldOrderListParam param) {
        Order order = tempProcessMapper.getOrderByParam(param);

        if (order != null) {
            decryptData(order);
        }
        
        if (order == null) {
            throw new OrderException("주문내역이 존재하지 않습니다.");
        }        

        return order;
    }
	
	public void decryptData(Order order) {
        if (order != null) {
        	try {
            	order.setUserName(pCrypto.Decrypt("normal", order.getUserName(), "", 0));	
        	} catch (UnsupportedEncodingException e) {
        		log.error("decryptData error", e);
        	}
            try {
                order.setLoginId(pCrypto.Decrypt("normal", order.getLoginId(), "", 0));
            } catch (UnsupportedEncodingException e) {
            	log.error("decryptData error", e);
            }
            order.decrypt(orderEncryptor, ShopUtils.needMasking());

            if (order.getOrderPayments() != null && !order.getOrderPayments().isEmpty()) {
                order.getOrderPayments().forEach(op -> op.decrypt(orderPaymentEncryptor, ShopUtils.needMasking()));
            }

            if (order.getOrderShippingInfos() != null && !order.getOrderShippingInfos().isEmpty()) {
                order.getOrderShippingInfos().forEach(osi -> osi.decrypt(orderShippingInfoEncryptor, ShopUtils.needMasking()));
            }
        }
    }
	
	public List<OrderPayment> getOrderPaymentListByParam(HoldOrderListParam holdOrderListParam) {
        List<OrderPayment> orderPayments = tempProcessMapper.getOrderPaymentListByParam(holdOrderListParam);

        orderPayments.forEach(op -> {
            op.decrypt(orderPaymentEncryptor, ShopUtils.needMasking());
        });

        return orderPayments;
    }
	
    public void bindOrder(Order order, HoldOrderListParam holdOrderListParam) {
        // 주문 결제정보 조회
        order.setOrderPayments(getOrderPaymentListByParam(holdOrderListParam));
    }

	@Override
	public List<RemittanceConfirmDetail> getRemittanceConfirmDetailListByParamNew(RemittanceParam param, boolean isExcel) {
		param.setConditionType("DETAIL");
		int totalCount = tempProcessMapper.getTempRemittanceConfirmDetailCountByParamNew(param);
		
		if (isExcel) {
			param.setPage(1);
			Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
			pagination.setItemsPerPage(Integer.MAX_VALUE);
			
			param.setPagination(pagination);
		} else {
			Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
			param.setPagination(pagination);
		}
		
		return tempProcessMapper.getTempRemittanceConfirmDetailList(param);
	}

	@Override
	public Remittance getRemittanceInfoById(long remittanceId) {
		RemittanceParam param = new RemittanceParam();
		param.setRemittanceId(remittanceId);
		return tempProcessMapper.getTempRemittanceInfoById(param);
	}
	

	// 확정 확인(판매자)
	@Override
	public void remittanceConfirmProcess(RemittanceParam param) {
		if (param.getId() == null || param.getId().length == 0) {
			throw new RemittanceException("ERR");
		}
		
		
		tempProcessMapper.updateTempRemittanceConfirmCheckByParam(param);
	}

	@Override
	public void tempOrderListProcess(HoldOrderListParam holdOrderListParam) {
		if ("S".equalsIgnoreCase(holdOrderListParam.getInputStatus()) && UserUtils.isSellerLogin()) {		// 승인 요청
			tempOrderListProcessData(holdOrderListParam);
		} else if (("R".equalsIgnoreCase(holdOrderListParam.getInputStatus())
						|| "M".equalsIgnoreCase(holdOrderListParam.getInputStatus()))
				&& UserUtils.isManagerLogin()) {		// 승인완료 또는 승인반려
			tempOrderListProcessData(holdOrderListParam);
		} else {
			throw new OpRuntimeException("권한이 없습니다.");
		}
	}
	
	private void tempOrderListProcessData(HoldOrderListParam holdOrderListParam) {
		for (String rcOrderCode : holdOrderListParam.getId()) {
			String[] infos = rcOrderCode.split("-");
			HoldOrderListParam param = new HoldOrderListParam();
			param.setOrderCode(infos[0]);
			param.setFiller5(infos[1]);
			param.setInputStatus(holdOrderListParam.getInputStatus());
			
			tempProcessMapper.insertHoldOrderListLog(param);
			tempProcessMapper.updateHoldOrderListByParam(param);

			if ("M".equalsIgnoreCase(holdOrderListParam.getInputStatus())) {		// 승인완료시 케이스별 추가 처리
				
				tempProcessMapper.insertHoldToOrderItem(param);
				tempProcessMapper.insertHoldToOrder(param);
				tempProcessMapper.insertHoldToPayment(param);
				
				HoldOrderList holdOrderItem = tempProcessMapper.selectHoldOrderStatus(param);
				
				switch (holdOrderItem.getOrderStatus()) {
					case "50":
					case "55":
						tempProcessMapper.insertHoldOrderExchangeInfo(holdOrderItem);
						break;
					case "60":
					case "65":
						tempProcessMapper.insertHoldOrderReturnInfo(holdOrderItem);
						tempProcessMapper.insertHoldOrderRefundInfo(holdOrderItem);
						break;
					case "70":
					case "75":
						tempProcessMapper.insertHoldOrderCancelInfo(holdOrderItem);
						break;
				}
			}
		}
	}
	
	
}