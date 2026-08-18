package saleson.shop.remittance;

import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import saleson.common.opmanager.count.OpmanagerCount;
import saleson.shop.order.addpayment.domain.OrderAddPayment;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.domain.OrderShipping;
import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.domain.RemittanceConfirm;
import saleson.shop.remittance.domain.RemittanceConfirmDetail;
import saleson.shop.remittance.domain.RemittanceDetail;
import saleson.shop.remittance.domain.RemittanceExpected;
import saleson.shop.remittance.domain.RemittanceFile;
import saleson.shop.remittance.support.RemittanceFileSupport;
import saleson.shop.remittance.support.RemittanceParam;

public interface RemittanceService {

	/**
	 * 정산 마감 내역
	 * @param param
	 * @return
	 */
	public List<Remittance> getRemittanceFinishingListByParam(RemittanceParam param);

	/**
	 * 정산 마감 상세
	 * @param param
	 * @return
	 */
	public List<RemittanceDetail> getRemittanceFinishingDetailListByParam(RemittanceParam param);

	/**
	 * 정산 확정 내역
	 * @param param
	 * @return
	 */
	public List<RemittanceConfirm> getRemittanceConfirmListByParam(RemittanceParam param);

	/**
	 * 판매자 정산 확정 내역
	 * @param param
	 * @return
	 */
	public RemittanceConfirm getRemittanceConfirmByParam(RemittanceParam param);

	/**
	 * 판매자 정산 마감 처리
	 * @param param
	 */
	public void remittanceFinishingProcess(RemittanceParam param);

	/**
	 * 판매자별 정산 목록 리스트 - 상품
	 * @param param
	 * @return
	 */
	public List<RemittanceConfirmDetail> getRemittanceConfirmDetailListByParam(RemittanceParam param);

	/**
	 * 판매자별 정산 대기 조회 일자별
	 * @param param
	 * @return
	 */
	public List<RemittanceExpected> getRemittanceExpectedListByParam(RemittanceParam param);

	/**
	 * 판매자별 정산 대기 목록 리스트 - 상품
	 * @param param
	 * @return
	 */
	public List<OrderItem> getRemittanceItemExpectedDetailListByParam(RemittanceParam param);

	/**
	 * 업체별 정산 확정 - 리스트(상품)
	 * @param param
	 */
	public void updateRemittanceItemExpectedForList(RemittanceParam param);

	/**
	 * 업체별 정산 확정 - 리스트(배송비)
	 * @param param
	 */
	public void updateRemittanceShippingExpectedForList(RemittanceParam param);

	/**
	 * 업체별 정산 확정 - 리스트(추가금)
	 * @param param
	 */
	public void updateRemittanceAddPaymentExpectedForList(RemittanceParam param);

	/**
	 * 상품 정산 수정 - 상품 상세
	 * @param param
	 */
	public void updateRemittanceItemExpected(RemittanceParam param);

	/**
	 * 배송비 정산 수정 - 배송비 상세
	 * @param param
	 */
	public void updateRemittanceShippingExpected(RemittanceParam param);

	/**
	 * 추가금 정산 수정 - 추가금 상세
	 * @param param
	 */
	public void updateRemittanceAddPaymentExpected(RemittanceParam param);

	/**
	 * 판매자별 정산 대기 목록 리스트 - 배송비
	 * @param param
	 * @return
	 */
	public List<OrderShipping> getRemittanceShippingExpectedDetailListByParam(RemittanceParam param);

	/**
	 * 판매자별 정산 대기 목록 리스트 - 추가금
	 * @param param
	 * @return
	 */
	public List<OrderAddPayment> getRemittanceAddPaymentExpectedDetailListByParam(RemittanceParam param);

	/**
	 * 이상우 [2017-05-11 추가]
	 * 관리자 메인 정산예정, 확정 카운트
	 * @return
	 */
	public List<OpmanagerCount> getOpmanagerRemittanceCountAll(RemittanceParam remittanceParam);

	/**
	 * 판매자 정산 확정 확인 처리
	 * @param param
	 */
	public void remittanceConfirmCheckProcess(RemittanceParam param);

	/**
	 * 판매자 입금 확인 처리
	 * @param param
	 */
	public void remittanceFinishCheckProcess(RemittanceParam param);


	/**
	 * 정산 마감 상세화면 내 사용할 정산확정일 조회
	 * @param param
	 */
	public RemittanceConfirmDetail selectRemittanceDateForDetail(RemittanceParam param);

	/**
	 * 정산 확정 처리
	 * @param param
	 * @return
	 */
	public void expectedListProcessNew(RemittanceParam param);

	/**
	 * 판매자별 정산 목록 리스트 - 상품
	 * @param param
	 * @return
	 */
	public List<RemittanceConfirmDetail> getRemittanceConfirmDetailListByParamNew(RemittanceParam param);

	/**
	 * 지자체 지급 처리
	 * @param param
	 */
	public void updateRemittancePayProcess(RemittanceParam param);

	/**
	 * 판매자 마감 처리
	 * @param param
	 */
	public void updateRemittanceFinishingProcessNew(RemittanceParam param);

	/**
	 * 정산 마감 내역
	 * @param param
	 * @return
	 */
	public List<Remittance> getRemittanceFinishingListByParamNew(RemittanceParam param);

	/**
	 * 정산 마감 상세
	 * @param param
	 * @return
	 */
	public List<RemittanceDetail> getRemittanceFinishingDetailListByParamNew(RemittanceParam param);

	/**
	 * 정산 확인 첨부파일 조회
	 * @param param
	 * @return
	 */
	public RemittanceFileSupport getRemittanceFileList(RemittanceParam param);

	/**
	 * 정산 확인 첨부파일 추가
	 * @param param
	 * @return
	 */
	public RemittanceFileSupport addRemittanceFiles(RemittanceParam param, List<MultipartFile> files);

	/**
	 * 첨부파일 다운로드
	 * @param param
	 * @return
	 */
	public ResponseEntity<byte[]> fileDownload(RemittanceParam param);

	/**
	 * 첨부파일 다운로드
	 * @param param
	 * @return
	 */
	public RemittanceFileSupport deleteRemittanceFile(RemittanceParam param);

	/**
	 * 첨부파일 다운로드
	 * @param param
	 * @return
	 */
	public ResponseEntity<byte[]> allFileDownload(RemittanceParam param);

	/**
	 * 정산 항목 아이디로 조회
	 * @param param
	 * @return
	 */
	public Remittance getRemittanceInfoById(long remittanceId);

	/**
	 * 답례품 제공자 정산예정확인 문자 전송
	 */
	public void sendRemittanceExpectedMsg();

	/**
	 * 정산의 주문상세 품목
	 * @param detailParam
	 * @return
	 */
	public List<RemittanceDetail> getRemittanceConfirmItemDetails(RemittanceParam detailParam);

	/**
	 * 정산 예정 내역 엑셀다운로드
	 * @param remittanceParam
	 */
	public SXSSFWorkbook streamRemittanceExpectedData(RemittanceParam remittanceParam);

	/**
	 * 정산 예정 내역 엑셀다운로드
	 * @param remittanceParam
	 */
	public SXSSFWorkbook streamRemittanceConfirmedData(RemittanceParam remittanceParam);

	/**
	 * 정산 예정 내역 엑셀다운로드
	 * @param remittanceParam
	 */
	public SXSSFWorkbook streamRemittanceFinishedData(RemittanceParam remittanceParam);
}
