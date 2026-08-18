package saleson.shop.sellerconfirm;

import java.util.List;
import saleson.shop.order.domain.OrderList;
import saleson.shop.order.support.OrderParam;
import saleson.shop.remittance.domain.RemittanceConfirm;
import saleson.shop.sellerconfirm.support.SellerconfirmParam;
import saleson.shop.user.domain.SellerUser;
import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("sellerconfirmMapper")
public interface SellerconfirmMapper {
	
	/**
	 * 판매자별 정산 확정 내역
	 * @param param
	 * @return
	 */
	List<RemittanceConfirm> getsellerconfirmConfirmListByParam(SellerconfirmParam param);
	
	/**
	 * 판매자별 정산 확정 내역 카운트
	 * @param param
	 * @return
	 */
	int getsellerconfirmConfirmCountByParam(SellerconfirmParam param);
	
	int getAllSellerConfirmOrderCountByParamForManager(OrderParam orderParam);
	
	List<OrderList> getAllSellerConfirmOrderListByParamForManager(OrderParam orderParam);
	
	/**
	 * 정산 지급 처리
	 * @param param
	 */
	void updatesellerconfirmPayProcess(SellerconfirmParam param);
	
	/**
	 * 정산 정보로 판매자 ci 조회
	 * @param param
	 */
	SellerUser getSellerBysellerconfirmInfo(SellerconfirmParam param);
	
	//int getAllSellerConfirmOrderCountByParamForManager(OrderParam orderParam);
	
	//List<OrderList> getAllSellerConfirmOrderCountByParamForManager(OrderParam orderParam);
}
