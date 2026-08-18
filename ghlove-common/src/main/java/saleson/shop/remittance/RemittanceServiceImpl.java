package saleson.shop.remittance;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import saleson.common.Const;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.SmsType;
import saleson.common.file.ExcelCellStyleUtils;
import saleson.common.file.FileDownloadCustom;
import saleson.common.file.infra.FileStorage;
import saleson.common.opmanager.count.OpmanagerCount;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.domain.Seller;
import saleson.seller.main.domain.SellerEncryptor;
import saleson.shop.order.addpayment.domain.OrderAddPayment;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.domain.OrderShipping;
import saleson.shop.remittance.domain.*;
import saleson.shop.remittance.support.*;
import saleson.shop.user.domain.SellerUser;

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
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RequiredArgsConstructor
@Service("remittanceService")
public class RemittanceServiceImpl extends EgovAbstractServiceImpl implements RemittanceService {
	@Autowired
	private RemittanceMapper remittanceMapper;

	@Autowired
	private SellerService sellerService;

	@Autowired
	private SequenceService sequenceService;

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
	public List<Remittance> getRemittanceFinishingListByParam(RemittanceParam param) {
		int totalCount = remittanceMapper.getRemittanceFinishingCountByParam(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		List<Remittance> list = remittanceMapper.getRemittanceFinishingListByParam(param);
		list.forEach(r -> {
			r.decrypt(remittanceEncryptor, ShopUtils.needMasking());
		});

		return list;
	}

	@Override
	public List<RemittanceDetail> getRemittanceFinishingDetailListByParam(RemittanceParam param) {

		int totalCount = remittanceMapper.getRemittanceFinishingDetailCountByParam(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		return remittanceMapper.getRemittanceFinishingDetailListByParam(param);

	}

	@Override
	public List<RemittanceExpected> getRemittanceExpectedListByParam(RemittanceParam param) {
		if (UserUtils.isSellerLogin()) {
			if(UserUtils.getSeller().getSellerId() > 0) {
				param.setSellerId(UserUtils.getSeller().getSellerId());
			}
			param.setConditionType("SELLER_LIST");
		}

		if (param.getPage() == 0) {
			param.setPage(1);
		}

		int totalCount = remittanceMapper.getRemittanceExpectedCountByParam(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		List<RemittanceExpected> list = remittanceMapper.getRemittanceExpectedListByParam(param);

		list.forEach(re -> {
			re.decrypt(remittanceExpectedEncryptor, ShopUtils.needMasking());
		});

		return list;
	}

	@Override
	public RemittanceConfirm getRemittanceConfirmByParam(RemittanceParam param) {
		param.setConditionType("DETAIL");
		param.setPagination(null);

		List<RemittanceConfirm> list =  remittanceMapper.getRemittanceConfirmListByParam(param);
		if (list == null) {
			return null;
		}

		if (list.size() > 1) {
			throw new RemittanceException("");
		}

		return list.get(0);
	}

	@Override
	public void remittanceFinishingProcess(RemittanceParam param) {

		Seller seller = sellerService.getSellerById(param.getSellerId());
		if (seller == null) {
			throw new RemittanceException("");
		}

		// 정산 입금처리
		long remittanceId = sequenceService.getId("OP_REMITTANCE");
		seller.encrypt(sellerEncryptor);
		remittanceMapper.insertRemittanceMaster(new Remittance(seller, param, remittanceId));

		// 정산 입금 상세 등록
		param.setRemittanceId(remittanceId);
		param.setConditionType("NO_CANCEL");
		remittanceMapper.insertRemittanceDetail(param);


		// 주문 상품정보 입금처리
		remittanceMapper.updateItemRemittanceFinishingByParam(param);

		// 배송비 입금처리
		remittanceMapper.updateShippingRemittanceFinishingByParam(param);

		// 배송비 입금처리
		remittanceMapper.updateAddPaymentRemittanceFinishingByParam(param);

		// 입금처리 정보 금액 검증 - 검증 실패시 입금처리 롤백
		Long amount = remittanceMapper.getRemittanceFinishingAmountValidateByParam(param);

		if (param.getConfirmAmount() != amount) {
			throw new RemittanceException("/seller/remittance/confirm/list", "정산금액 불일치, 검증 실패하였습니다.");
		}
	}

	@Override
	public List<RemittanceConfirmDetail> getRemittanceConfirmDetailListByParam(RemittanceParam param) {

		param.setConditionType("DETAIL");
		int totalCount = remittanceMapper.getRemittanceConfirmDetailCountByParam(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		return remittanceMapper.getRemittanceConfirmDetailListByParam(param);
	}

	@Override
	public List<RemittanceConfirm> getRemittanceConfirmListByParam(RemittanceParam param) {

		if (ShopUtils.isSellerPage()) {
			param.setConditionType("SELLER_LIST");
		} else {
			param.setConditionType("LIST");
		}
//		int totalCount = remittanceMapper.getRemittanceConfirmCountByParam(param);
		int totalCount = remittanceMapper.getRemittanceConfirmCountByParamNew(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

//		List<RemittanceConfirm> list = remittanceMapper.getRemittanceConfirmListByParam(param);
		List<RemittanceConfirm> list = remittanceMapper.getRemittanceConfirmListByParamNew(param);

		list.forEach(rc -> {
			rc.decrypt(remittanceConfirmEncryptor, ShopUtils.needMasking());
		});

		return list;

	}

	@Override
	public List<OrderItem> getRemittanceItemExpectedDetailListByParam(RemittanceParam param) {
		int totalCount = remittanceMapper.getRemittanceItemExpectedDetailCountByParam(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		List<OrderItem> list = remittanceMapper.getRemittanceItemExpectedDetailListByParam(param);

		return list;
	}

	@Override
	public void updateRemittanceItemExpectedForList(RemittanceParam param) {
		String errorUrl = "/opmanager/remittance/expected/list";

		if (param.getId() != null) {
			String remittanceDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			for(String key : param.getId()) {
				String[] temp = StringUtils.delimitedListToStringArray(key, "^^^");
				if (temp.length != 3) {
					throw new RemittanceException(errorUrl);
				}

				// 화면에서 받지 않고 서버에서 세팅
//				EditItemRemittance edit = param.getEditItemRemittanceMap().get(key);
//				if (edit == null) {
//					throw new RemittanceException(errorUrl);
//				}
				EditItemRemittance edit = new EditItemRemittance();

				edit.setSellerId(Integer.parseInt(temp[0]));
				edit.setStartDate(temp[1] + "00");
				edit.setEndDate(temp[2] + "99");

				edit.setRemittanceDate(remittanceDate);		// 정산예정일, 확정일 별도로 저장되도록 추가

//				if (DateUtils.validDate(edit.getRemittanceExpectedDate())) {
				if (DateUtils.validDate(edit.getRemittanceDate())) {
					edit.setConditionType("LIST");
					remittanceMapper.updateRemittanceItemExpected(edit);
				}

			}
		}
	}

	@Override
	public void updateRemittanceShippingExpectedForList(RemittanceParam param) {
		String errorUrl = "/opmanager/remittance/expected/list";

		if (param.getId() != null) {

			String remittanceDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			for(String key : param.getId()) {

				String[] temp = StringUtils.delimitedListToStringArray(key, "^^^");
				if (temp.length != 3) {
					throw new RemittanceException(errorUrl);
				}

				// 화면에서 받지 않고 서버에서 세팅
//				EditItemRemittance editTemp = param.getEditItemRemittanceMap().get(key);
//				if (editTemp == null) {
//					throw new RemittanceException(errorUrl);
//				}

				EditShippingRemittance edit = new EditShippingRemittance();
//				edit.setRemittanceExpectedDate(editTemp.getRemittanceExpectedDate());
				edit.setSellerId(Integer.parseInt(temp[0]));
				edit.setStartDate(temp[1] + "00");
				edit.setEndDate(temp[2] + "99");
				edit.setRemittanceDate(remittanceDate);		// 정산예정일, 확정일 별도로 저장되도록 추가

//				if (DateUtils.validDate(edit.getRemittanceExpectedDate())) {
				if (DateUtils.validDate(edit.getRemittanceDate())) {
					edit.setConditionType("LIST");
					remittanceMapper.updateRemittanceShippingExpected(edit);
				}

			}
		}
	}

	@Override
	public void updateRemittanceAddPaymentExpectedForList(RemittanceParam param) {
		String errorUrl = "/opmanager/remittance/expected/list";

		if (param.getId() != null) {

			String remittanceDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			for(String key : param.getId()) {

				String[] temp = StringUtils.delimitedListToStringArray(key, "^^^");
				if (temp.length != 3) {
					throw new RemittanceException(errorUrl);
				}

				// 화면에서 받지 않고 서버에서 세팅
//				EditItemRemittance editTemp = param.getEditItemRemittanceMap().get(key);
//				if (editTemp == null) {
//					throw new RemittanceException(errorUrl);
//				}

				EditAddPaymentRemittance edit = new EditAddPaymentRemittance();
//				edit.setRemittanceExpectedDate(editTemp.getRemittanceExpectedDate());
				edit.setSellerId(Integer.parseInt(temp[0]));
				edit.setStartDate(temp[1] + "00");
				edit.setEndDate(temp[2] + "99");

				edit.setRemittanceDate(remittanceDate);		// 정산예정일, 확정일 별도로 저장되도록 추가

//				if (DateUtils.validDate(edit.getRemittanceExpectedDate())) {
				if (DateUtils.validDate(edit.getRemittanceDate())) {
					edit.setConditionType("LIST");
					remittanceMapper.updateRemittanceAddPaymentExpected(edit);
				}

			}
		}
	}

	@Override
	public void updateRemittanceShippingExpected(RemittanceParam param) {

		String errorUrl = "/opmanager/remittance/expected/detail/shipping/" + param.getSellerId() + "/" + param.getStartDate() + "/" + param.getEndDate();

		if (param.getId() != null) {

			String remittanceDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			for(String key : param.getId()) {
				String[] temp = StringUtils.delimitedListToStringArray(key, "^^^");
				if (temp.length != 3) {
					throw new RemittanceException(errorUrl);
				}

//				EditShippingRemittance edit = param.getEditShippingRemittanceMap().get(key);
//				if (edit == null) {
//					throw new RemittanceException(errorUrl);
//				}
				EditShippingRemittance edit = new EditShippingRemittance();
				edit.setOrderCode(temp[0]);
				edit.setOrderSequence(Integer.parseInt(temp[1]));
				edit.setShippingSequence(Integer.parseInt(temp[2]));
				edit.setRemittanceDate(remittanceDate);

//				if (DateUtils.validDate(edit.getRemittanceExpectedDate())) {
				if (DateUtils.validDate(edit.getRemittanceDate())) {
					edit.setConditionType(param.getConditionType().toUpperCase());
					remittanceMapper.updateRemittanceShippingExpected(edit);
				}

			}

		}

	}

	@Override
	public void updateRemittanceAddPaymentExpected(RemittanceParam param) {

//		String errorUrl = "/opmanager/remittance/expected/detail/add-payment/" + param.getSellerId() + "/" + param.getStartDate() + "/" + param.getEndDate();

		if (param.getId() != null) {

			String remittanceDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			for(String key : param.getId()) {

//				EditAddPaymentRemittance edit = param.getEditAddPaymentRemittanceMap().get(key);
//				if (edit == null) {
//					throw new RemittanceException(errorUrl);
//				}

				EditAddPaymentRemittance edit = new EditAddPaymentRemittance();
				edit.setAddPaymentId(Integer.parseInt(key));

				edit.setRemittanceDate(remittanceDate);

//				if (DateUtils.validDate(edit.getRemittanceExpectedDate())) {
				if (DateUtils.validDate(edit.getRemittanceDate())) {
					edit.setConditionType(param.getConditionType().toUpperCase());
					remittanceMapper.updateRemittanceAddPaymentExpected(edit);
				}

			}

		}

	}

	@Override
	public void updateRemittanceItemExpected(RemittanceParam param) {

		String errorUrl = "/opmanager/remittance/expected/detail/item/" + param.getSellerId() + "/" + param.getStartDate() + "/" + param.getEndDate();

		if (param.getId() != null) {

			String remittanceDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			for(String key : param.getId()) {
				String[] temp = StringUtils.delimitedListToStringArray(key, "^^^");
				if (temp.length != 3) {
					throw new RemittanceException(errorUrl);
				}

				EditItemRemittance edit = param.getEditItemRemittanceMap().get(key);
				if (edit == null) {
					throw new RemittanceException(errorUrl);
				}

				edit.setOrderCode(temp[0]);
				edit.setOrderSequence(Integer.parseInt(temp[1]));
				edit.setItemSequence(Integer.parseInt(temp[2]));

				edit.setRemittanceDate(remittanceDate);		// 정산예정일, 확정일 별도로 저장되도록 추가

//				if (DateUtils.validDate(edit.getRemittanceExpectedDate())) {
				if (DateUtils.validDate(edit.getRemittanceDate())) {
					edit.setConditionType(param.getConditionType().toUpperCase());
					remittanceMapper.updateRemittanceItemExpected(edit);
				}

			}

		}

	}

	@Override
	public List<OrderShipping> getRemittanceShippingExpectedDetailListByParam(RemittanceParam param) {
		int totalCount = remittanceMapper.getRemittanceShippingExpectedDetailCountByParam(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		List<OrderShipping> list = remittanceMapper.getRemittanceShippingExpectedDetailListByParam(param);

		return list;
	}

	@Override
	public List<OrderAddPayment> getRemittanceAddPaymentExpectedDetailListByParam(RemittanceParam param) {
		int totalCount = remittanceMapper.getRemittanceAddPaymentExpectedDetailCountByParam(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		List<OrderAddPayment> list = remittanceMapper.getRemittanceAddPaymentExpectedDetailListByParam(param);

		return list;
	}

	@Override
	public List<OpmanagerCount> getOpmanagerRemittanceCountAll(RemittanceParam remittanceParam) {

		return remittanceMapper.getOpmanagerRemittanceCountAll(remittanceParam);
	}

	// 확정 확인(판매자)
	@Override
	public void remittanceConfirmCheckProcess(RemittanceParam param) {
		/*
		 * Seller seller = sellerService.getSellerById(param.getSellerId()); if (seller
		 * == null) { throw new RemittanceException(""); }
		 */
		if (param.getId() != null && param.getId().length == 1) {
//			String[] temp = StringUtils.delimitedListToStringArray(param.getId()[0], "^^^");
//			param.setSellerId(Long.valueOf(temp[0]));
//			param.setStartDate(temp[1]);
			param.setRemittanceId(Long.valueOf(param.getId()[0]));
		} else {
			throw new RemittanceException("ERR");
		}

		// 상품 확정 확인
//		remittanceMapper.updateItemRemittanceConfirmCheckByParam(param);

		// 배송비 확정 확인
//		remittanceMapper.updateShippingRemittanceConfirmCheckByParam(param);

		// 추가금 확정 확인
//		remittanceMapper.updateAddPaymentRemittanceConfirmCheckByParam(param);

		remittanceMapper.updateRemittanceConfirmCheckByParam(param);
	}

	@Override
	public void remittanceFinishCheckProcess(RemittanceParam param) {
		if (param.getId() == null
				|| param.getId().length <= 0
				|| param.getFinishingRemittanceMap() == null) {
			throw new RemittanceException("ERROR");
		}
		HashMap<String, FinishingRemittance> data = param.getFinishingRemittanceMap();
		for(String key : param.getId()) {
			FinishingRemittance finishingRemittance = data.get(key);
			RemittanceParam params = new RemittanceParam();
			params.setSellerId(finishingRemittance.getSellerId());
			params.setRemittanceId(Integer.valueOf(key));

			remittanceMapper.updateRemitanceFinishCheck(param);
			remittanceMapper.updateItemRemitanceFinishCheck(param);
			remittanceMapper.updateShippingRemitanceFinishCheck(param);
			remittanceMapper.updateAddPaymentRemitanceFinishCheck(param);
		}

	}

	@Override
	public RemittanceConfirmDetail selectRemittanceDateForDetail(RemittanceParam param) {
		return remittanceMapper.selectRemittanceDateForDetail(param);
	}

	@Override
	public void expectedListProcessNew(RemittanceParam param) {
		if (param.getId() != null) {
			LocalDate now = LocalDate.now();
			String remittanceDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			param.setStartDate(remittanceDate);
			for(String key : param.getId()) {
				String[] temp = StringUtils.delimitedListToStringArray(key, "^^^");
//				Long confirmAmount = Double.valueOf(temp[2]).longValue();
				if (temp.length != 3) {
					throw new RemittanceException("ERROR");
				}

				Seller seller = sellerService.getSellerById(Integer.parseInt(temp[0]));
				if (seller == null) {
					throw new RemittanceException("");
				}

				int remittanceId = sequenceService.getId("OP_REMITTANCE");

				EditItemRemittance edit = new EditItemRemittance();

				edit.setSellerId(Integer.parseInt(temp[0]));
				edit.setRemittanceExpectedDate(temp[1]);
				LocalDate expectedDate = LocalDate.parse(temp[1], DateTimeFormatter.ofPattern("yyyyMMdd"));
				if (now.getYear() < expectedDate.getYear()) {
					throw new RemittanceException("", "이번달 정산예정일 항목까지만 정산 가능합니다.");
				} else if (now.getYear() == expectedDate.getYear() && now.getMonthValue() < expectedDate.getMonthValue()) {
					throw new RemittanceException("", "이번달 정산예정일 항목까지만 정산 가능합니다.");
				}

				edit.setRemittanceDate(remittanceDate);		// 정산예정일, 확정일 별도로 저장되도록 추가
				edit.setRemittanceId(remittanceId);

				remittanceMapper.updateRemittanceItemExpectedNew(edit);

				remittanceMapper.updateRemittanceShippingExpectedNew(edit);

				remittanceMapper.updateRemittanceAddPaymentExpectedNew(edit);

				seller.encrypt(sellerEncryptor);
				param.setConfirmAmount(Double.valueOf(temp[2]));

				Remittance remittance = new Remittance(seller, param, remittanceId);
				remittance.encrypt(remittanceEncryptor);
				remittanceMapper.insertRemittanceMasterNew(remittance);

				param.setRemittanceId(remittanceId);
				param.setConditionType("NO_CANCEL");
				remittanceMapper.insertRemittanceDetailNew(param);

				// 입금처리 정보 금액 검증 - 검증 실패시 입금처리 롤백 - 주석 처리(이관 데이터 불일치)
//				Long amount = remittanceMapper.getRemittanceFinishingAmountValidateByParam(param);
//
//				if (confirmAmount == null || !confirmAmount.equals(amount)) {
//					throw new RemittanceException("", "정산금액 불일치, 검증 실패하였습니다.");
//				}

				// TODO :: 국민비서 발송 처리(정산 확정) 확인필요
				try {
					RemittanceParam smsParam = new RemittanceParam();
					smsParam.setRemittanceId(remittanceId);
					smsParam.setLocgovCode(param.getLocgovCode());
					SellerUser su = remittanceMapper.getSellerByRemittanceInfo(smsParam);
					if (su != null && StringUtils.hasLength(su.getPhoneNumber()) && StringUtils.hasLength(su.getMberCi()) && "0".equals(su.getReceiveSms())) {
						List<ReceiverInfo> receiverInfos = new ArrayList<>();
						ReceiverInfo receiverInfo = new ReceiverInfo();
						receiverInfo.setSmsType(SmsType.CALCULATE_CONFIRM);
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
					log.error("RemittanceServiceImpl expectedListProcessNew send sms error", e);
				}
			}
		}

	}

	@Override
	public List<RemittanceConfirmDetail> getRemittanceConfirmDetailListByParamNew(RemittanceParam param) {

		param.setConditionType("DETAIL");
		int totalCount = remittanceMapper.getRemittanceConfirmDetailCountByParamNew(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		return remittanceMapper.getRemittanceConfirmDetailListByParamNew(param);
	}

	@Override
	public void updateRemittancePayProcess(RemittanceParam param) {
		remittanceMapper.updateRemittancePayProcess(param);

		// TODO :: 국민비서 발송 처리(정산 마감) 확인필요
		try {
			SellerUser su = remittanceMapper.getSellerByRemittanceInfo(param);
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

	@Override
	public void updateRemittanceFinishingProcessNew(RemittanceParam param) {
		String[] id = param.getId();
		if (id != null && id.length > 0) {
			RemittanceParam value = new RemittanceParam();
			value.setSellerId(SellerUtils.getSellerId());
			for (String remittanceId : id) {
				value.setRemittanceId(Long.valueOf(remittanceId));
				remittanceMapper.updateRemittanceFinishingProcessNew(value);


			}
		}
	}

	@Override
	public List<Remittance> getRemittanceFinishingListByParamNew(RemittanceParam param) {
		int totalCount = remittanceMapper.getRemittanceFinishingCountByParamNew(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		List<Remittance> list = remittanceMapper.getRemittanceFinishingListByParamNew(param);
		list.forEach(r -> {
			r.decrypt(remittanceEncryptor, ShopUtils.needMasking());
		});

		return list;
	}

	@Override
	public List<RemittanceDetail> getRemittanceFinishingDetailListByParamNew(RemittanceParam param) {

		int totalCount = remittanceMapper.getRemittanceFinishingDetailCountByParamNew(param);

		Pagination pagination = Pagination.getInstance(totalCount, param.getItemsPerPage());
		pagination.setItemsPerPage(param.getItemsPerPage());
		param.setPagination(pagination);

		return remittanceMapper.getRemittanceFinishingDetailListByParamNew(param);
	}

	@Override
	public RemittanceFileSupport getRemittanceFileList(RemittanceParam param) {
		RemittanceFileSupport info = new RemittanceFileSupport();
		RemittanceFile maxSequenceInfo = remittanceMapper.getRemittanceFileMaxSequence(param);
		if (maxSequenceInfo == null || maxSequenceInfo.getRemittanceId() == 0) {
			throw new RemittanceException("", "정산 정보가 없습니다.");			// 정산 정보 없음
		} else if ("3".equals(maxSequenceInfo.getStatusCode()) || "4".equals(maxSequenceInfo.getStatusCode())) {
			throw new RemittanceException("", "정산 확정 상태가 아닙니다.");			// 정산 확정 상태가 아님
		}
		info.setRemittanceId(maxSequenceInfo.getRemittanceId());
		info.setRemittanceStatusCode(maxSequenceInfo.getRemittanceStatusCode());
		info.setRemittanceFileList(remittanceMapper.getRemittanceFileListForSeller(param));
		return info;
	}

	@Override
	public RemittanceFileSupport addRemittanceFiles(RemittanceParam param, List<MultipartFile> files) {
		RemittanceFile maxSequenceInfo = remittanceMapper.getRemittanceFileMaxSequence(param);
		if (maxSequenceInfo == null || maxSequenceInfo.getRemittanceId() == 0) {
			throw new RemittanceException("", "정산 정보가 없습니다.");			// 정산 정보 없음
		} else if ("3".equals(maxSequenceInfo.getStatusCode()) || "4".equals(maxSequenceInfo.getStatusCode())) {
			throw new RemittanceException("", "정산 확정 상태가 아닙니다.");			// 정산 확정 상태가 아님
		}

		checkFileSize(param, files);

		List<RemittanceFile> insertList = new ArrayList<>();
		int maxSeq = maxSequenceInfo.getFileSeq();
		long remittanceId = maxSequenceInfo.getRemittanceId();
		String uploadFilePath = getUploadFilePath(remittanceId);
		long userId = UserUtils.getUserId();
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());

		String saveUploadPath = getFilePathForSave(remittanceId);
		for (MultipartFile multipartFile : files) {
//			File saveFile = saveFile(uploadFilePath, multipartFile);
			String fileName = saveFile(uploadFilePath, multipartFile);
			String originalFilename = multipartFile.getOriginalFilename();
//			if (saveFile != null) {
			if (!fileName.isEmpty()) {
				RemittanceFile insertData = new RemittanceFile();
				insertData.setRemittanceId(remittanceId);
				insertData.setFileSeq(++maxSeq);
//				insertData.setFileName(saveFile.getName());
//				insertData.setOrgFileName(multipartFile.getOriginalFilename());
				insertData.setFileName(fileName);
				insertData.setOrgFileName(originalFilename);
				insertData.setFrstRegisterId(userId);
				insertData.setFrstRegistPnttm(now);

				insertData.setPathName(saveUploadPath);

				insertList.add(insertData);
			}
		}
		if (insertList != null && !insertList.isEmpty()) {
			remittanceMapper.insertRemittanceFile(insertList);
			remittanceConfirmCheckProcess(param);
		}

		return getRemittanceFileList(param);
	}

	private String getUploadFilePath(long remittanceId) {
		return new StringBuffer().append(SalesonProperty.getUploadSaveFolder())
				.append("/remittance/")
				.append(remittanceId)
				.append("/")
				.toString();
	}

	private String getFilePathForSave(long remittanceId) {
		return new StringBuffer()
				.append("/remittance/")
				.append(remittanceId)
				.append("/")
				.toString();
	}

	private String saveFile(String uploadPath, MultipartFile multipartFile) {
		fileService.makeUploadPath(uploadPath);

		File saveFile = getFile(multipartFile, uploadPath, 1);
		String fileName = saveFile.getName();
		try {
			fileStorage.upload(multipartFile.getBytes(), saveFile);
		} catch (IOException e) {
			log.error("RemittanceServiceImpl saveFile error", e);
//			saveFile = null;
			fileName = "";
		}
		return fileName;
	}

	private File getFile(MultipartFile multipartFile, String uploadPath, int seq) {
		String ext = FileUtils.getExtension(multipartFile.getOriginalFilename());

		boolean isAvailable = false;
		for (String string : AVAILABLE_EXTENSION) {
			if (string.equalsIgnoreCase(ext)) {
				isAvailable = true;
				break;
			}
		}
		if (!isAvailable) {
			throw new RemittanceException("", ext + " 파일은 업로드 불가합니다.");
		}

		String fileName = DateUtils.getToday(Const.DATENANO_FORMAT) + "." + ext;
		fileName = FileUtils.getNewFileName(uploadPath, fileName);
		File saveFile = new File(uploadPath + "/" + fileName);
		if (saveFile != null && saveFile.exists()) {
			if (seq > 10) {		// 10회 실패할 경우
				return null;
			} else {
				return getFile(multipartFile, uploadPath, ++seq);
			}
		} else {
			return saveFile;
		}
	}

	@Override
	public ResponseEntity<byte[]> fileDownload(RemittanceParam param) {
		RemittanceFile remittanceFile = remittanceMapper.getRemittanceFile(param);
		if (remittanceFile != null) {
			try {
				return FileDownloadCustom.fileDownloadCustom(SalesonProperty.getUploadSaveFolder()
						+ remittanceFile.getPathName() + "/" + remittanceFile.getFileName(), remittanceFile.getOrgFileName());
			} catch (IOException e) {
				log.error("RemittanceServiceImpl fileDownload error", e);
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public RemittanceFileSupport deleteRemittanceFile(RemittanceParam param) {
		RemittanceFile remittanceFile = remittanceMapper.getRemittanceFile(param);
		if (remittanceFile != null) {
			fileStorage.delete(remittanceFile.getPathName(), remittanceFile.getFileName());
			int result = remittanceMapper.deleteRemittanceFile(param);
			if (result != 1) {
				throw new RuntimeException();
			}
		}

		return getRemittanceFileList(param);
	}


	private void checkFileSize(RemittanceParam param, List<MultipartFile> files) {
		int maxSize = 10;
		long fileSize = 0;
		List<RemittanceFile> savedFiles = getRemittanceFileList(param).getRemittanceFileList();
		if (savedFiles != null && !savedFiles.isEmpty()) {
			for (RemittanceFile remittanceFile : savedFiles) {
				File file = new File(SalesonProperty.getUploadSaveFolder()
						+ remittanceFile.getPathName() + "/" + remittanceFile.getFileName());
				if (file != null && file.exists()) {
					fileSize += file.length();
				}
			}
		}
		if (files != null && !files.isEmpty()) {
			for (MultipartFile multipartFile : files) {
				fileSize += multipartFile.getSize();
			}
		}

		if (fileSize > maxSize * 1024 * 1024) {			// 업로드 용량 10 메가 제한
			throw new RemittanceException("", "첨부파일은 " + maxSize + "MB 까지 업로드 가능합니다.");			// 10mb 초과
		}
	}

	// 일괄 압축 후 다운로드
	@Override
	public ResponseEntity<byte[]> allFileDownload(RemittanceParam param) {
		List<RemittanceFile> files = remittanceMapper.getRemittanceFileListForSeller(param);

		if (files != null && !files.isEmpty()) {
			File zipFile = new File(getUploadFilePath(param.getRemittanceId()), "data.zip");
			byte[] buf = new byte[4096];
			try (FileOutputStream fos = new FileOutputStream(zipFile);
				ZipOutputStream zos = new ZipOutputStream(fos);) {

				List<String> addFileNames = new ArrayList<>();

				for (RemittanceFile remittanceFile : files) {
					String path = SalesonProperty.getUploadSaveFolder() + remittanceFile.getPathName();
					String fileName = remittanceFile.getFileName();
					File file = new File(path, fileName);

					String orgFileName = remittanceFile.getOrgFileName();

					orgFileName = getOrgFileName(addFileNames, orgFileName, 0);

					try (FileInputStream fis = new FileInputStream(file);){
						ZipEntry ze = new ZipEntry(orgFileName);
						zos.putNextEntry(ze);

						int len;
						while ((len = fis.read(buf)) > 0) {
							zos.write(buf, 0, len);
						}

						zos.closeEntry();
					} catch (IOException e) {
						log.error("RemittanceServiceImpl allFileDownload FileInputStream error", e);
					}
				}
			} catch (IOException e) {
				log.error("RemittanceServiceImpl allFileDownload ZipOutputStream error", e);
			}

			byte[] bytes = null;
			try {
				bytes = Files.readAllBytes(zipFile.toPath());
			} catch (IOException e) {
				log.error("RemittanceServiceImpl allFileDownload readAllBytes error", e);
				return null;
			}

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			httpHeaders.setContentLength(bytes.length);
			httpHeaders.setContentDispositionFormData("attachment", zipFile.getName());

			zipFile.delete();

			return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
		}
		return null;
	}

	// 중복된 파일명 있을 경우 파일명 뒤에 _숫자 추가
	private String getOrgFileName(List<String> addFileNames, String orgFileName, int cnt) {
		boolean exist = false;

		for (String string : addFileNames) {
			if (orgFileName.equals(string)) {
				exist = true;
				break;
			}
		}

		if (exist) {
			int pointPosition = orgFileName.lastIndexOf(".");
			String ext = orgFileName.substring(pointPosition + 1);
			String name = orgFileName.substring(0, pointPosition);
			if (name.indexOf("_") > -1) {
				name = orgFileName.substring(0, orgFileName.lastIndexOf("_"));
			}
			orgFileName = name + "_" + cnt + "." + ext;

			orgFileName = getOrgFileName(addFileNames, orgFileName, ++cnt);
		}

		addFileNames.add(orgFileName);
		return orgFileName;
	}

	@Override
	public Remittance getRemittanceInfoById(long remittanceId) {
		RemittanceParam param = new RemittanceParam();
		param.setRemittanceId(remittanceId);
		return remittanceMapper.getRemittanceInfoById(param);
	}

	/**
	 * 답례품 제공자 정산예정확인 문자 전송 배치 매월 1일 9시
	 */
	@Override
	public void sendRemittanceExpectedMsg() {
		LocalDate now = LocalDate.now();
		RemittanceParam param = new RemittanceParam();
		String expectedDate = "";
		String msgDate = "";

		expectedDate += now.getYear();
		msgDate += now.getYear();
		if (now.getMonthValue() < 10) {
			expectedDate += "0" + now.getMonthValue();
			msgDate += "년 0" + now.getMonthValue();
		} else {
			expectedDate += now.getMonthValue();
			msgDate += "년 " + now.getMonthValue();
		}
		expectedDate += "08";			// 정산일 8일로 동일
		param.setStartDate(expectedDate);

		List<SellerUser> list = remittanceMapper.getRemittanceExpectedMsgSellerList(param);

		if (list == null || list.isEmpty()) {
			return;
		}
		List<ReceiverInfo> receiverInfos = new ArrayList<>();

		for (SellerUser sellerUser : list) {
			if (StringUtils.hasLength(sellerUser.getMberCi()) && StringUtils.hasLength(sellerUser.getPhoneNumber()) && "0".equals(sellerUser.getReceiveSms())) {
				ReceiverInfo receiverInfo = new ReceiverInfo();
				receiverInfo.setSmsType(SmsType.CALCULATE_CHECK);
				receiverInfo.setPrvcIdntfcInfo(sellerUser.getMberCi());
				receiverInfo.setSndngCntnts(sellerUser.getUserName() + "|" + msgDate + "|" + sellerUser.getPhoneNumber().replaceAll("-", ""));

				receiverInfos.add(receiverInfo);
			}
		}

		smsIpsService.insertTifIpsSndngM(receiverInfos);

	}

	@Override
	public List<RemittanceDetail> getRemittanceConfirmItemDetails(RemittanceParam remittanceParam) {
		// TODO Auto-generated method stub
		return  remittanceMapper.getRemittanceConfirmItemDetails(remittanceParam);
	}

	@Override
	public SXSSFWorkbook streamRemittanceExpectedData(RemittanceParam remittanceParam) {
		if (UserUtils.isSellerLogin()) {
			if(UserUtils.getSeller().getSellerId() > 0) {
				remittanceParam.setSellerId(UserUtils.getSeller().getSellerId());
			}
			remittanceParam.setConditionType("SELLER_LIST");
		}

		Pagination pagination = Pagination.getInstance(0);
		remittanceParam.setPagination(pagination);

		// 엑셀 다운로드 workbook setting
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;

		// 전체 데이터 count
		remittanceParam.getPagination().setItemsPerPage(pageSize);
		remittanceParam.getPagination().setCurrentPage(offset);

		int totalCount = remittanceMapper.getRemittanceExpectedCountByParam(remittanceParam);

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// workbook 생성을 위한 offset 및 pageSize 설정
		pageSize 		= 1000;
		offset 			= 1;

		remittanceParam.getPagination().setItemsPerPage(pageSize);
		remittanceParam.getPagination().setCurrentPage(offset);

		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("REMITTANCE_EXPECTED_LIST");

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
		cellStyle.title(titleRow, 0, "정산예정내역");
		Integer lastColIndex;

		// 엑셀 시트 프레임 설정(컬럼 크기 및 컬럼 명 세팅)
		if (auth == "SYSTEM") {
			sheet.setColumnWidth(0, 3000);
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 8000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 5000);
			sheet.setColumnWidth(8, 3000);
			sheet.setColumnWidth(9, 15000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No");
			cellStyle.header(header, 1, "지자체");
			cellStyle.header(header, 2, "상호명");
			cellStyle.header(header, 3, "대표자명");
			cellStyle.header(header, 4, "휴대폰");
			cellStyle.header(header, 5, "판매가");
			cellStyle.header(header, 6, "정산금액");
			cellStyle.header(header, 7, "정산예정일");
			cellStyle.header(header, 8, "주문건수");
			cellStyle.header(header, 9, "품목");

			lastColIndex = header.getLastCellNum() - 1;
		} else if (auth == "SELLER") {
			sheet.setColumnWidth(0, 3000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 15000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No");
			cellStyle.header(header, 1, "대표자명");
			cellStyle.header(header, 2, "휴대폰");
			cellStyle.header(header, 3, "판매가");
			cellStyle.header(header, 4, "정산금액");
			cellStyle.header(header, 5, "정산예정일");
			cellStyle.header(header, 6, "주문건수");
			cellStyle.header(header, 7, "품목");

			lastColIndex = header.getLastCellNum() - 1;
		} else {
			sheet.setColumnWidth(0, 3000);
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 3000);
			sheet.setColumnWidth(8, 15000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "No");
			cellStyle.header(header, 1, "상호명");
			cellStyle.header(header, 2, "대표자명");
			cellStyle.header(header, 3, "휴대폰");
			cellStyle.header(header, 4, "판매가");
			cellStyle.header(header, 5, "정산금액");
			cellStyle.header(header, 6, "정산예정일");
			cellStyle.header(header, 7, "주문건수");
			cellStyle.header(header, 8, "품목");

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
			List<RemittanceExpected> remittanceExptectedList = remittanceMapper.getRemittanceExpectedListByParam(remittanceParam);
			remittanceExptectedList.forEach(remittanceExptectedItem -> {
				remittanceExptectedItem.decrypt(remittanceExpectedEncryptor, ShopUtils.needMasking());
			});

			if (remittanceExptectedList.isEmpty()) break;

			for (RemittanceExpected remittanceExptectedItem : remittanceExptectedList) {
				// 정산의 주문상세 품목을 엑셀로추가
				RemittanceParam remittanceDetailParam = new RemittanceParam();

				remittanceDetailParam.setSellerId(remittanceExptectedItem.getSellerId());
				remittanceDetailParam.setStartDate(remittanceExptectedItem.getRemittanceExpectedDate());
				remittanceDetailParam.setEndDate(remittanceExptectedItem.getRemittanceExpectedDate());

				List<OrderItem> orderList = remittanceMapper.getRemittanceItemExpectedDetailListByParam(remittanceDetailParam);
				String rsltContent = orderList.stream().map(detail -> detail.getItemName()).collect(Collectors.joining(", "));

				Row row = sheet.createRow(rowNum++);
				int cellCount = 0;
				row.setHeight((short) 400);
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(totalCount--));
				if ("SYSTEM".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceExptectedItem.getLocgovNm());
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceExptectedItem.getCompanyName());
				}
				cellStyle.data(row, cellCount++, remittanceExptectedItem.getRepresentativeName());
				cellStyle.data(row, cellCount++, remittanceExptectedItem.getPhoneNumber());
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(remittanceExptectedItem.getItemRemittanceAmount() + remittanceExptectedItem.getShippingTotalAmount() + remittanceExptectedItem.getAddPaymentTotalAmount()) + " P");
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(remittanceExptectedItem.getItemRemittanceAmount() + remittanceExptectedItem.getShippingTotalAmount() + remittanceExptectedItem.getAddPaymentTotalAmount()) + " 원");
				cellStyle.data(row, cellCount++, DateUtils.date(remittanceExptectedItem.getRemittanceExpectedDate()));
				cellStyle.data(row, cellCount++, remittanceExptectedItem.getOrCnt());
				cellStyle.data(row, cellCount++, rsltContent);
			}

			// 객체참조시점 issue로 변수 설정
			remittanceParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}

		return workbook;
	}

	@Override
	public SXSSFWorkbook streamRemittanceConfirmedData(RemittanceParam remittanceParam) {
		if (ShopUtils.isSellerPage()) {
			remittanceParam.setConditionType("SELLER_LIST");
		} else {
			remittanceParam.setConditionType("LIST");
		}

		Pagination pagination = Pagination.getInstance(0);
		remittanceParam.setPagination(pagination);

		// 엑셀 다운로드 workbook setting
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;

		// 전체 데이터 count
		remittanceParam.getPagination().setItemsPerPage(pageSize);
		remittanceParam.getPagination().setCurrentPage(offset);

		int totalCount = remittanceMapper.getRemittanceConfirmCountByParamNew(remittanceParam);

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// workbook 생성을 위한 offset 및 pageSize 설정
		pageSize 		= 1000;
		offset 			= 1;

		remittanceParam.getPagination().setItemsPerPage(pageSize);
		remittanceParam.getPagination().setCurrentPage(offset);

		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("REMITTANCE_CONFIRMED_LIST");

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
		cellStyle.title(titleRow, 0, "정산확정내역");
		Integer lastColIndex;

		// 엑셀 시트 프레임 설정(컬럼 크기 및 컬럼 명 세팅)
		if (auth == "SYSTEM") {
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 8000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 5000);
			sheet.setColumnWidth(8, 15000);
			sheet.setColumnWidth(9, 3000);
			sheet.setColumnWidth(10, 15000);
			sheet.setColumnWidth(11, 3000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "정산아이디");
			cellStyle.header(header, 1, "지자체");
			cellStyle.header(header, 2, "상호명");
			cellStyle.header(header, 3, "대표자명");
			cellStyle.header(header, 4, "휴대폰");
			cellStyle.header(header, 5, "판매가");
			cellStyle.header(header, 6, "정산금액");
			cellStyle.header(header, 7, "정산확정일");
			cellStyle.header(header, 8, "계좌번호");
			cellStyle.header(header, 9, "주문건수");
			cellStyle.header(header, 10, "주문품목");
			cellStyle.header(header, 11, "상태");

			lastColIndex = header.getLastCellNum() - 1;
		} else if (auth == "SELLER") {
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 3000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "정산아이디");
			cellStyle.header(header, 1, "확정일자");
			cellStyle.header(header, 2, "총 판매액");
			cellStyle.header(header, 3, "총 정산금액");
			cellStyle.header(header, 4, "상태");

			lastColIndex = header.getLastCellNum() - 1;
		} else {
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 15000);
			sheet.setColumnWidth(8, 3000);
			sheet.setColumnWidth(9, 15000);
			sheet.setColumnWidth(10, 3000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "정산아이디");
			cellStyle.header(header, 1, "상호명");
			cellStyle.header(header, 2, "대표자명");
			cellStyle.header(header, 3, "휴대폰");
			cellStyle.header(header, 4, "판매가");
			cellStyle.header(header, 5, "정산금액");
			cellStyle.header(header, 6, "정산확정일");
			cellStyle.header(header, 7, "계좌번호");
			cellStyle.header(header, 8, "주문건수");
			cellStyle.header(header, 9, "주문품목");
			cellStyle.header(header, 10, "상태");

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
			List<RemittanceConfirm> remittanceConfirmedList = remittanceMapper.getRemittanceConfirmListByParamNew(remittanceParam);
			if (remittanceConfirmedList.isEmpty()) break;

			remittanceConfirmedList.forEach(remittanceConfirmItem -> {
				remittanceConfirmItem.decrypt(remittanceConfirmEncryptor, ShopUtils.needMasking());
			});

			for (RemittanceConfirm remittanceConfirm : remittanceConfirmedList) {
				// 정산의 주문상세 품목을 엑셀로추가
				RemittanceParam remittanceDetailParam = new RemittanceParam();

				remittanceDetailParam.setRemittanceId(Long.parseLong(remittanceConfirm.getRemittanceId()));

				List<RemittanceDetail> remittanceDetailList = remittanceMapper.getRemittanceConfirmItemDetails(remittanceDetailParam);
				String rsltContent = remittanceDetailList.stream().map(detail -> detail.getItemName()).distinct().collect(Collectors.joining(", "));

				Row row = sheet.createRow(rowNum++);
				int cellCount = 0;
				row.setHeight((short) 400);
				cellStyle.data(row, cellCount++, remittanceConfirm.getRemittanceId());
				if ("SYSTEM".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceConfirm.getLocgovNm());
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceConfirm.getCompanyName());
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceConfirm.getRepresentativeName());
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceConfirm.getPhoneNumber());
				}
				if ("SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, DateUtils.date(remittanceConfirm.getConfirmDate()));
				}
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(remittanceConfirm.getItemTotalSupplyAmount()) + " P");
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(remittanceConfirm.getFinishingAmount()) + " 원");
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, DateUtils.date(remittanceConfirm.getConfirmDate()));
				}
				if (!"SELLER".equalsIgnoreCase(auth)) { // 계쫘정보
					StringBuffer sf = new StringBuffer();
					sf.append("[");
					sf.append(remittanceConfirm.getBankName());
					sf.append("] ");
					sf.append(remittanceConfirm.getBankAccountNumber());
					sf.append(" (");
					sf.append(remittanceConfirm.getBankInName());
					sf.append(")");
					cellStyle.data(row, cellCount++, sf.toString());
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceConfirm.getRemiDetailCnt());
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, rsltContent);
				}
				if ("4".equals(remittanceConfirm.getRemittanceStatusCode())) {
					cellStyle.data(row, cellCount++, "정산확인");
				} else {
					cellStyle.data(row, cellCount++, "정산미확인");
				}
			}

			// 객체참조시점 issue로 변수 설정
			remittanceParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}
		return workbook;
	}

	@Override
	public SXSSFWorkbook streamRemittanceFinishedData(RemittanceParam remittanceParam) {
		Pagination pagination = Pagination.getInstance(0);
		remittanceParam.setPagination(pagination);

		// 엑셀 다운로드 workbook setting
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;

		// 전체 데이터 count
		remittanceParam.getPagination().setItemsPerPage(pageSize);
		remittanceParam.getPagination().setCurrentPage(offset);

		int totalCount = remittanceMapper.getRemittanceFinishingCountByParamNew(remittanceParam);

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// workbook 생성을 위한 offset 및 pageSize 설정
		pageSize 		= 1000;
		offset 			= 1;

		remittanceParam.getPagination().setItemsPerPage(pageSize);
		remittanceParam.getPagination().setCurrentPage(offset);

		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		Sheet sheet = workbook.createSheet("REMITTANCE_FINISHED_LIST");

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
		cellStyle.title(titleRow, 0, "정산마감내역");
		Integer lastColIndex;

		// 엑셀 시트 프레임 설정(컬럼 크기 및 컬럼 명 세팅)
		if (auth == "SYSTEM") {
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 8000);
			sheet.setColumnWidth(3, 3000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 5000);
			sheet.setColumnWidth(8, 15000);
			sheet.setColumnWidth(9, 3000);
			sheet.setColumnWidth(10, 15000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "정산아이디");
			cellStyle.header(header, 1, "지자체");
			cellStyle.header(header, 2, "상호명");
			cellStyle.header(header, 3, "대표자명");
			cellStyle.header(header, 4, "휴대폰");
			cellStyle.header(header, 5, "판매가");
			cellStyle.header(header, 6, "정산금액");
			cellStyle.header(header, 7, "입금확인일");
			cellStyle.header(header, 8, "계좌번호");
			cellStyle.header(header, 9, "주문건수");
			cellStyle.header(header, 10, "품목");

			lastColIndex = header.getLastCellNum() - 1;
		} else if (auth == "SELLER") {
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 5000);
			sheet.setColumnWidth(2, 5000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 3000);
			sheet.setColumnWidth(7, 15000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "정산아이디");
			cellStyle.header(header, 1, "확정일");
			cellStyle.header(header, 2, "지급일");
			cellStyle.header(header, 3, "총 판매액");
			cellStyle.header(header, 4, "총 정산금액");
			cellStyle.header(header, 5, "상태");
			cellStyle.header(header, 6, "주문건수");
			cellStyle.header(header, 7, "품목");

			lastColIndex = header.getLastCellNum() - 1;
		} else {
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 5000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 5000);
			sheet.setColumnWidth(6, 5000);
			sheet.setColumnWidth(7, 15000);
			sheet.setColumnWidth(8, 3000);
			sheet.setColumnWidth(9, 15000);

			Row header = sheet.createRow(rowNum++);
			header.setHeight((short) 512);
			cellStyle.header(header, 0, "정산아이디");
			cellStyle.header(header, 1, "상호명");
			cellStyle.header(header, 2, "대표자명");
			cellStyle.header(header, 3, "휴대폰");
			cellStyle.header(header, 4, "판매가");
			cellStyle.header(header, 5, "정산금액");
			cellStyle.header(header, 6, "입금확인일");
			cellStyle.header(header, 7, "계좌번호");
			cellStyle.header(header, 8, "주문건수");
			cellStyle.header(header, 9, "품목");

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
			List<Remittance> remittanceFinishedList = remittanceMapper.getRemittanceFinishingListByParamNew(remittanceParam);
			if (remittanceFinishedList.isEmpty()) break;
			remittanceFinishedList.forEach(remittanceFinishedItem -> {
				remittanceFinishedItem.decrypt(remittanceEncryptor, ShopUtils.needMasking());
			});

			for (Remittance remittanceFinish : remittanceFinishedList) {
				// 정산의 주문상세 품목을 엑셀로추가
				RemittanceParam remittanceDetailParam = new RemittanceParam();

				remittanceDetailParam.setRemittanceId(remittanceFinish.getRemittanceId());

				List<RemittanceDetail> remittanceDetailList = remittanceMapper.getRemittanceConfirmItemDetails(remittanceDetailParam);
				String rsltContent = remittanceDetailList.stream().map(detail -> detail.getItemName()).collect(Collectors.joining(", "));

				Row row = sheet.createRow(rowNum++);
				int cellCount = 0;
				row.setHeight((short) 400);
				cellStyle.data(row, cellCount++, StringUtils.long2string(remittanceFinish.getRemittanceId()));
				if ("SYSTEM".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceFinish.getLocgovNm());
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceFinish.getCompanyName());
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceFinish.getRepresentativeName());
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, remittanceFinish.getPhoneNumber());
				}
				if ("SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, DateUtils.date(remittanceFinish.getConfirmDate()));
				}
				if ("SELLER".equalsIgnoreCase(auth)) {
					cellStyle.data(row, cellCount++, DateUtils.date(remittanceFinish.getPaymentDate()));
				}
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(remittanceFinish.getSalesAmount()) + " P");
				cellStyle.data(row, cellCount++, StringUtils.numberFormat(remittanceFinish.getFinishingAmount()) + " 원");
				if ("9".equals(remittanceFinish.getRemittanceStatusCode())) {
					if ("SELLER".equalsIgnoreCase(auth)) {
						cellStyle.data(row, cellCount++, "완료");
					} else {
						cellStyle.data(row, cellCount++, DateUtils.date(remittanceFinish.getFinishingDate()));
					}
				} else {
					cellStyle.data(row, cellCount++, "입금 확인 대기");
				}
				if (!"SELLER".equalsIgnoreCase(auth)) {
					StringBuffer sf = new StringBuffer();
					sf.append("[");
					sf.append(remittanceFinish.getBankName());
					sf.append("] ");
					sf.append(remittanceFinish.getBankAccountNumber());
					sf.append(" (");
					sf.append(remittanceFinish.getBankInName());
					sf.append(")");
					cellStyle.data(row, cellCount++, sf.toString());
				}

				cellStyle.data(row, cellCount++, remittanceFinish.getOrCnt());
				cellStyle.data(row, cellCount++, rsltContent);
			}

			// 객체참조시점 issue로 변수 설정
			remittanceParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}
		return workbook;
	}
}
