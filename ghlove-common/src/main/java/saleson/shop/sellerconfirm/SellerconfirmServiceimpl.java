package saleson.shop.sellerconfirm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.IdType;
import saleson.common.enumeration.SmsType;
import saleson.common.file.FileDownloadCustom;
import saleson.common.file.infra.FileStorage;
import saleson.common.opmanager.count.OpmanagerCount;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.model.OrderGiftItem;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.seller.main.domain.SellerEncryptor;
import saleson.shop.sellerconfirm.support.SellerconfirmParam;
import saleson.shop.item.domain.api.ItemInfo;
import saleson.shop.order.addpayment.domain.OrderAddPayment;
import saleson.shop.order.api.ApiOrderList;
import saleson.shop.order.api.OrderDetail;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.domain.OrderList;
import saleson.shop.order.domain.OrderPgData;
import saleson.shop.order.domain.OrderShipping;
import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.order.domain.OrderSupporter;
import saleson.shop.order.infra.OrderListEncryptor;
import saleson.shop.order.infra.OrderParamEncryptor;
import saleson.shop.order.support.OrderParam;
import saleson.shop.remittance.RemittanceMapper;
import saleson.shop.remittance.RemittanceServiceImpl;
import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.domain.RemittanceConfirm;
import saleson.shop.remittance.domain.RemittanceConfirmDetail;
import saleson.shop.remittance.domain.RemittanceConfirmEncryptor;
import saleson.shop.remittance.domain.RemittanceDetail;
import saleson.shop.remittance.domain.RemittanceEncryptor;
import saleson.shop.remittance.domain.RemittanceExpected;
import saleson.shop.remittance.domain.RemittanceExpectedEncryptor;
import saleson.shop.user.LocgovService;
import saleson.shop.user.domain.SellerUser;

@Slf4j
@RequiredArgsConstructor
@Service("sellerconfirmService")
public class SellerconfirmServiceimpl extends EgovAbstractServiceImpl implements SellerconfirmService{
	
	private final OrderParamEncryptor orderParamEncryptor;
	private final OrderSupporter orderSupporter;
	private final OrderListEncryptor orderListEncryptor;
	
	@Autowired
	private SellerconfirmMapper sellerconfirmMapper;
	
	@Autowired
	private LocgovService locgovService;	// 지자체 관리	
	
	@Autowired
	private RemittanceEncryptor remittanceEncryptor;

	@Autowired
	private RemittanceExpectedEncryptor remittanceExpectedEncryptor;

	@Autowired
	private RemittanceConfirmEncryptor remittanceConfirmEncryptor;

	@Autowired
	private SellerEncryptor sellerEncryptor;

	private final FileService fileService;
	private final FileStorage fileStorage;
	
	private final String[] AVAILABLE_EXTENSION = {"jpg", "jpeg", "gif", "bmp", "png", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "pdf", "tif", "tiff", "hwp", "zip"};

	@Autowired
	private SmsIpsService smsIpsService;

	@Override
	public List<RemittanceConfirm> getSellerconfirmConfirmListByParam(SellerconfirmParam param) {
		
		param.setConditionType("LIST");
		int totalCount = sellerconfirmMapper.getsellerconfirmConfirmCountByParam(param);
		
		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		param.setPagination(pagination);

		List<RemittanceConfirm> list = sellerconfirmMapper.getsellerconfirmConfirmListByParam(param);

		list.forEach(rc -> {
			rc.decrypt(remittanceConfirmEncryptor, ShopUtils.needMasking());
		});

		return list;
		
	}
	
	@Override
	public void updateSellerconfirmPayProcess(SellerconfirmParam param) {
		sellerconfirmMapper.updatesellerconfirmPayProcess(param);
		
		// TODO :: 국민비서 발송 처리(정산 마감) 확인필요
		try {
			SellerUser su = sellerconfirmMapper.getSellerBysellerconfirmInfo(param);
			if (su != null && StringUtils.hasLength(su.getMberCi()) && StringUtils.hasLength(su.getPhoneNumber()) && "0".equals(su.getReceiveSms())) {
				List<ReceiverInfo> receiverInfos = new ArrayList<>();
				ReceiverInfo receiverInfo = new ReceiverInfo();
				LocalDate now = LocalDate.now();
				receiverInfo.setSmsType(SmsType.CALCULATE_CLOSE);
				receiverInfo.setPrvcIdntfcInfo(su.getMberCi());
				String yearMonth = "";
				yearMonth += now.getYear();
				if (now.getMonthValue() < 10) {
					yearMonth += "년 0" + now.getMonthValue();
				} else {
					yearMonth += "년 " + now.getMonthValue();
				}
				receiverInfo.setSndngCntnts(su.getUserName() + "|" + yearMonth + "|" + su.getPhoneNumber().replaceAll("-", ""));
				receiverInfos.add(receiverInfo);
				smsIpsService.insertTifIpsSndngM(receiverInfos);
			}
		} catch (OpRuntimeException e) {
			log.error("RemittanceServiceImpl updateRemittancePayProcess send sms error", e);
		}
	}
	
	/**
	 * 관리자 주문 전체 리스트 조회
	 * @param orderParam
	 * @return
	 */
	@Override
	public List<OrderList> getAllSellerConfirmOrderListByParamForManager(OrderParam orderParam) {
		
		if (UserUtils.isManagerLogin() && "LOC".equals(locgovService.getLoginUserAdminRoleCheck())) {
			// 지자체관리자일 경우 지자체코드 필요
			orderParam.setShWdr(locgovService.getUpperLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER)));
			orderParam.setShLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		}		

		orderParam.encrypt(orderParamEncryptor);
		int totalCount = sellerconfirmMapper.getAllSellerConfirmOrderCountByParamForManager(orderParam);

		//if (orderParam.getItemsPerPage() == 10) {
		//	orderParam.setItemsPerPage(50);
		//}

		Pagination pagination = Pagination.getInstance(totalCount, orderParam.getItemsPerPage());
		orderParam.setPagination(pagination);

		List<OrderList> list = sellerconfirmMapper.getAllSellerConfirmOrderListByParamForManager(orderParam);
		decryptOrderList(list);
		orderParam.decrypt(orderParamEncryptor);

		// 기타 주문 상품 정보 세팅 (사은품, 세트상품)
		setOrderItemOther(list);

		return list;

	}
	
	private void decryptOrderList(List<OrderList> list) {
		if (list != null) {
			list.forEach(ol -> {
				ol.decrypt(orderListEncryptor, ShopUtils.needMasking());
			});
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

//	private void setOrderItemOther(Order order) {
//		orderSupporter.setOrderItemOther(order);
//	}

	@Override
	public Order getOrderByParam(OrderParam orderParam) {
		return orderSupporter.getOrderByParam(orderParam);
	}
	
}
