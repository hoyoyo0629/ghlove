package saleson.shop.orderagency;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.shop.mypage.domain.CntrPoint;
import saleson.shop.order.support.OrderParam;
import saleson.shop.orderagency.domain.OrderAgencyInfo;
import saleson.shop.orderagency.domain.OrderAgencyLoginConfirmInfo;
import saleson.shop.orderagency.domain.OrderAgencyManagerInfo;
import saleson.shop.orderagency.domain.OrderAgencyOrderListInfo;
import saleson.shop.orderagency.domain.OrderAgentOrderData;
import saleson.shop.orderagency.domain.OrderAgentResult;
import saleson.shop.orderagency.entity.AgencyPrivateKeyInfo;

public interface OrderAgencyService {

	public void saveAgencyPrivateKeyInfo(AgencyPrivateKeyInfo agencyPrivateKeyInfo);
	
	public AgencyPrivateKeyInfo selectAgencyPrivateKeyInfo(String userSessionId);
	
	public OrderAgentResult selectAgencyLoginInfo(String userSessionId, OrderAgencyInfo orderAgencyInfo);
	
	public OrderAgentResult selectPublicKey(String userSessionId);
	
	public String getSalesonId(HttpServletRequest request);
	
	public String selectOrderAgencyDecryptedToken(String salesonId, String encToken);
	
	public String selectOrderAgencyEncryptedToken(String salesonId, String token);
	
	public OrderAgencyLoginConfirmInfo loginAgencyLoginConfirmInfo(OrderAgencyInfo orderAgencyInfo, User manager, String sessionId, String privateKey);
	
	public void insertAgencyLoginConfirmInfo(OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo);
	
	public Long checkValidAgencyLogin(HttpServletRequest request);
	
	public void updateCntrbtrMobileInfo(OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo, String cntrbtrMobile, String mberCi);
	
	public List<CntrPoint> getOrderAgencyCntrPointList(long userId, SearchParam searchParam);

	public User getOrderAgencyCntrbtrByUserId(long userId);
	
	public User getOrderAgencyCntrbtrInfoByRequest(HttpServletRequest request);

	public OrderAgencyManagerInfo selectOrderAgencyManagerInfo(long managerId);

	public List<OrderAgencyOrderListInfo> selectOrderAgencyOrderList(OrderParam orderParam);
	
	public String selectOrderAgencyTempDataId(OrderAgentOrderData orderAgentOrderData);
	
	public OrderAgentOrderData selectOrderAgencyTempDataByDataId(String dataId);
	
}
