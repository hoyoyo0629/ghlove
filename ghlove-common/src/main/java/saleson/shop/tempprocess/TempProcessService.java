package saleson.shop.tempprocess;

import java.util.List;

import org.springframework.ui.Model;

import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.domain.RemittanceConfirm;
import saleson.shop.remittance.domain.RemittanceConfirmDetail;
import saleson.shop.remittance.support.RemittanceParam;
import saleson.shop.tempprocess.domain.HoldOrderList;
import saleson.shop.tempprocess.domain.HoldOrderListParam;

public interface TempProcessService {
	
	
	/**
	 * 주문 내역
	 * @param orderParam
	 * @return
	 */
	public List<HoldOrderList> getTempOrderListByParam(HoldOrderListParam holdOrderListParam);
	
	/**
	 * 정산 확정 내역
	 * @param param
	 * @return
	 */
	public List<RemittanceConfirm> getTempRemittanceConfirmListByParam(RemittanceParam param);
	
	
	void setModelOrderDetail(Model model, String rcOrderCode, String mode);
	
	/**
	 * 판매자별 정산 목록 리스트 - 상품
	 * @param param
	 * @return
	 */
	public List<RemittanceConfirmDetail> getRemittanceConfirmDetailListByParamNew(RemittanceParam param, boolean isExcel);
	
	/**
	 * 정산 항목 아이디로 조회
	 * @param param
	 * @return
	 */
	public Remittance getRemittanceInfoById(long remittanceId);
	
	
	public void remittanceConfirmProcess(RemittanceParam param);
	
	
	void tempOrderListProcess(HoldOrderListParam holdOrderListParam);
	
}
