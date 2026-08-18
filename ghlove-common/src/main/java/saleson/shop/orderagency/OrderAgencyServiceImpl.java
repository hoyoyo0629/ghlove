package saleson.shop.orderagency;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.security.mapper.SecurityMapper;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ThreadContextUtils;
import com.onlinepowers.framework.web.domain.SearchParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.privacy.pCrypto;

import saleson.common.enumeration.IdType;
import saleson.common.security.api.JwtCode;
import saleson.common.security.api.JwtTokenService;
import saleson.common.security.crypto.RsaCryptor;
import saleson.common.utils.JwtUtils;
import saleson.common.utils.UserUtils;
import saleson.model.UserEntity;
import saleson.shop.disposable.TempDataService;
import saleson.shop.disposable.domain.TempData;
import saleson.shop.log.LoginLogService;
import saleson.shop.mypage.MyPageService;
import saleson.shop.mypage.domain.CntrPoint;
import saleson.shop.mypage.support.CntrPointParam;
import saleson.shop.order.support.OrderParam;
import saleson.shop.orderagency.domain.OrderAgencyInfo;
import saleson.shop.orderagency.domain.OrderAgencyLoginConfirmInfo;
import saleson.shop.orderagency.domain.OrderAgencyManagerInfo;
import saleson.shop.orderagency.domain.OrderAgencyOrderListInfo;
import saleson.shop.orderagency.domain.OrderAgentOrderData;
import saleson.shop.orderagency.domain.OrderAgentResult;
import saleson.shop.orderagency.entity.AgencyPrivateKeyInfo;
import saleson.shop.orderagency.repository.AgencyPrivateKeyInfoRepository;
import saleson.shop.user.LocgovService;
import saleson.shop.user.ManagerRequestService;
import saleson.shop.user.UserRepository;
import saleson.shop.user.UserService;

@Service("orderAgencyService")
@EnableJpaAuditing				// 엔티티 객체 데이터 변경 감지1
public class OrderAgencyServiceImpl implements OrderAgencyService {

	// 암호화 키 만료 시간(분)
	private final int ENC_EXPIRE_MINUTES = 30;

	// 로그인 만료 시간(분)
	private final int LOGIN_EXPIRE_MINUTES = 120;

	@Autowired
	private AgencyPrivateKeyInfoRepository agencyPrivateKeyInfoRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ManagerRequestService managerRequestService;

	@Autowired
	private OrderAgencyMapper orderAgencyMapper;

	@Autowired
	private JwtTokenService jwtTokenService;

//	@Autowired
//	private ShopSecurityService shopSecurityService;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private SecurityMapper securityMapper;

//	@Autowired
//	private ManagerRequestMapper managerRequestMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private MyPageService myPageService;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private TempDataService tempEncDataService;

	/**
	 * 주문대행용 복호화 키 저장
	 */
	@Override
	public void saveAgencyPrivateKeyInfo(AgencyPrivateKeyInfo agencyPrivateKeyInfo) {
		if (StringUtils.hasLength(agencyPrivateKeyInfo.getUserSessionId())) {
			agencyPrivateKeyInfoRepository.save(agencyPrivateKeyInfo);
		}
	}

	/**
	 * 주문대행용 복호화 키 조회
	 */
	@Override
	public AgencyPrivateKeyInfo selectAgencyPrivateKeyInfo(String userSessionId) {
		AgencyPrivateKeyInfo result = agencyPrivateKeyInfoRepository.findByUserSessionId(userSessionId);
		if (result != null) {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime saveDate = result.getFrstRegDt().toLocalDateTime();
			if (now.isAfter(saveDate.plusMinutes(ENC_EXPIRE_MINUTES))) {
				result = null;
			}
		}
		return result;
	}

	/**
	 * 대행주문 관리자 로그인 처리 및 검증용 관리자 정보/기부자 전화번호 저장
	 */
	@Override
	public OrderAgentResult selectAgencyLoginInfo(String userSessionId, OrderAgencyInfo orderAgencyInfo) {
		OrderAgentResult result = new OrderAgentResult();

		AgencyPrivateKeyInfo agencyPrivateKeyInfo = selectAgencyPrivateKeyInfo(userSessionId);
		if (agencyPrivateKeyInfo == null) {
			result = selectPublicKey(userSessionId);
			result.setRstCd("KEY_EXPIRED");
			result.setRstMsg("암호화키 만료");
		} else {
			String privateKey = agencyPrivateKeyInfo.getPrivateKey();
			String encLoginId = orderAgencyInfo.getLoginId();
			String encPwd = orderAgencyInfo.getPassword();
			String encCntrbtrMobile = orderAgencyInfo.getCntrbtrMobile();

			if (!StringUtils.hasLength(encLoginId)
					|| !StringUtils.hasLength(encPwd)
					|| !StringUtils.hasLength(encCntrbtrMobile)) {
				result.setRstCd("INVALID_DATA");
				result.setRstMsg("로그인 아이디, 비밀번호, 기부자 전화번호를 모두 입력해주세요.");
				return result;
			}

			String loginId = RsaCryptor.decrypt(encLoginId, privateKey);
			String pwd = RsaCryptor.decrypt(encPwd, privateKey);

			User user = managerRequestService.getManagerByLoginId(loginId);

			if (user == null) {
				result.setRstCd("LOGIN_FAIL");
				result.setRstMsg("아이디 또는 비밀번호를 확인해주세요.");
			} else {
				String dbEncPwd = "";
				try {
					dbEncPwd = pCrypto.Encrypt("hash.5", pwd, "");
				} catch (UnsupportedEncodingException e) {
					result.setRstCd("DB_ENCRYPT_FAIL");
					result.setRstMsg("DB 조회시 필요한 암호화 실패, 고객센터로 문의바랍니다.");
					return result;
				}
				if ("9".equals(user.getStatusCode())
						&& dbEncPwd.equals(user.getPassword())) {
					result.setRstCd("SUCCESS");
					result.setRstMsg("관리자 로그인 완료");

					OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo = loginAgencyLoginConfirmInfo(orderAgencyInfo, user, userSessionId, privateKey);
					result.setAuthToken(orderAgencyLoginConfirmInfo.getValidAccessCd());
				} else {
					result.setRstCd("LOGIN_FAIL");
					result.setRstMsg("아이디 또는 비밀번호를 확인해주세요.");
//					managerRequestMapper.updateManagerLoginFailCount(user.getUserId());
				}
			}
		}

		return result;
	}

	/**
	 * 대행주문 로그인시 사용할 암호화 키 조회
	 */
	@Override
	public OrderAgentResult selectPublicKey(String userSessionId) {
		OrderAgentResult result = new OrderAgentResult();

		agencyPrivateKeyInfoRepository.deleteByUserSessionId(userSessionId);
		AgencyPrivateKeyInfo info = new AgencyPrivateKeyInfo();
		Map<String, String> keyPair = RsaCryptor.createKeypairAsString();
		info.setUserSessionId(userSessionId);
		info.setPrivateKey(keyPair.get("privateKeyStr"));
		info.setPublicKey(keyPair.get("publicKeyStr"));

		saveAgencyPrivateKeyInfo(info);

		result.setRstCd("SUCCESS");
		result.setRstMsg("발급완료");
		result.setPublicKey(keyPair.get("publicKeyStr"));

		return result;
	}

	/**
	 * 헤더에서 사용자 구분 키 조회
	 */
	@Override
	public String getSalesonId(HttpServletRequest request) {
		Object obj = request.getHeader("salesonid");
		Gson gson = new Gson();

//		return request.getHeader("salesonid").toString();
		return gson.toJson(obj);
	}

	/**
	 * 암호화된 토큰 복호화
	 */
	@Override
	public String selectOrderAgencyDecryptedToken(String salesonId, String encToken) {
		AgencyPrivateKeyInfo keyInfo = selectAgencyPrivateKeyInfo(salesonId);
		if (keyInfo == null) {
			return "";
		}
		return RsaCryptor.decrypt(encToken, keyInfo.getPrivateKey());
	}

	/**
	 * 토큰 암호화
	 */
	@Override
	public String selectOrderAgencyEncryptedToken(String salesonId, String token) {
		AgencyPrivateKeyInfo keyInfo = selectAgencyPrivateKeyInfo(salesonId);
		if (keyInfo == null) {
			return "";
		}
		return RsaCryptor.encrypt(token, keyInfo.getPrivateKey());
	}

	/**
	 * 검증용 관리자 정보 / 기부자 전화번호 저장
	 */
	@Override
	public void insertAgencyLoginConfirmInfo(OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo) {
		orderAgencyMapper.deleteAgencyLoginConfirmInfo(orderAgencyLoginConfirmInfo);
		orderAgencyMapper.insertAgencyLoginConfirmInfo(orderAgencyLoginConfirmInfo);
	}

	/**
	 * 토큰 생성, 로그인 실패 횟수 초기화 및 검증용 관리자 정보 / 기부자 전화번호 저장 로직
	 */
	@Override
	public OrderAgencyLoginConfirmInfo loginAgencyLoginConfirmInfo(OrderAgencyInfo orderAgencyInfo, User manager, String sessionId, String privateKey) {
		HttpServletRequest request = ThreadContextUtils.getRequestContext().getRequest();

		try {
			UserEntity userEntity = userRepository.findByLoginId(pCrypto.Encrypt("normal", manager.getLoginId(), ""));

			String loginType = "ROLE_AGENCY";
			String token = jwtTokenService.getJwtToken(
	                loginType,
	                userEntity.getLoginId().trim(),
	                userEntity.getPassword().trim(),
	                JwtUtils.getClientIpAddress(request)
	        );

			String sign = (String)JwtUtils.getValueByToken(token, JwtUtils.getCode(JwtCode.JWT_CLAIM_SIGN));
	        userService.saveLoginSessionForUser(sign, userEntity.getUserId());

			OrderAgencyLoginConfirmInfo agencyLoginConfirmInfo = new OrderAgencyLoginConfirmInfo();
			agencyLoginConfirmInfo.setUserSessionId(sessionId);
			agencyLoginConfirmInfo.setValidAccessCd(pCrypto.Encrypt("normal", token, ""));
			agencyLoginConfirmInfo.setManagerId(manager.getUserId());
			agencyLoginConfirmInfo.setCntrbtrMobile(RsaCryptor.decrypt(orderAgencyInfo.getCntrbtrMobile(), privateKey).replaceAll("-", ""));
			agencyLoginConfirmInfo.setPrivateKey(privateKey);

			insertAgencyLoginConfirmInfo(agencyLoginConfirmInfo);

			securityMapper.updateClearLoginFailCountForManager(manager.getLoginId());
			loginLogService.insertLoginLogByManager(request, manager.getLoginId(), true);

			return agencyLoginConfirmInfo;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 사용자 구분 키, 암호화된 토큰, 기부자 전화번호로 검증 정보 조회(로그인 여부 확인)
	 * return 기부자 유저아이디
	 */
	@Override
	public Long checkValidAgencyLogin(HttpServletRequest request) {
		String token = JwtUtils.getToken(request);
		String salesonId = getSalesonId(request);

		OrderAgencyLoginConfirmInfo param = new OrderAgencyLoginConfirmInfo();
		param.setUserSessionId(salesonId);
		param.setValidAccessCd(token);

		OrderAgencyLoginConfirmInfo loginInfo = orderAgencyMapper.selectAgencyLoginConfirmInfo(param);

		if (loginInfo == null) {
			throw new OpRuntimeException("로그인 후 진행해주세요.");
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime saveDate = loginInfo.getFrstRegDt().toLocalDateTime();
		if (now.isAfter(saveDate.plusMinutes(LOGIN_EXPIRE_MINUTES))) {
			loginInfo = null;
		}

		if (loginInfo == null) {
			throw new OpRuntimeException("로그인 후 진행해주세요.");
		} else if (loginInfo.getCntrbtrId() == null || loginInfo.getCntrbtrId() == null || loginInfo.getCntrbtrId() < 1) {
			throw new OpRuntimeException("기부자 정보가 없습니다. 다시 로그인 후 진행해주세요.");
		}

		return loginInfo.getCntrbtrId();
	}

	@Override
	public void updateCntrbtrMobileInfo(OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo, String cntrbtrMobile, String mberCi) {
		OrderAgencyLoginConfirmInfo loginInfo = orderAgencyMapper.selectAgencyLoginConfirmInfo(orderAgencyLoginConfirmInfo);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime saveDate = loginInfo.getFrstRegDt().toLocalDateTime();
		if (now.isAfter(saveDate.plusMinutes(LOGIN_EXPIRE_MINUTES))) {
			loginInfo = null;
		}

		if (loginInfo == null) {
			throw new OpRuntimeException("로그인 정보가 없습니다.");
		}

		String decCntrbtrMobile = RsaCryptor.decrypt(cntrbtrMobile, loginInfo.getPrivateKey()).replaceAll("-", "");

		if (StringUtils.hasLength(decCntrbtrMobile) && decCntrbtrMobile.equals(loginInfo.getCntrbtrMobile())) {
			String decMberCi = RsaCryptor.decrypt(mberCi, loginInfo.getPrivateKey());

			//System.out.println("=============== mberCi :: " + mberCi);
			//System.out.println("=============== privateKey :: " + loginInfo.getPrivateKey());
			//System.out.println("=============== decMberCi :: " + decMberCi);

			UserEntity userEntity = userRepository.findByMberCi(decMberCi);
			if (userEntity == null || userEntity.getUserId() < 1) {
				throw new OpRuntimeException("기부자 정보가 없습니다.");
			}
			orderAgencyLoginConfirmInfo.setCntrbtrId(userEntity.getUserId());
			orderAgencyMapper.updateCntrbtrMobileInfo(orderAgencyLoginConfirmInfo);
		} else {
			throw new OpRuntimeException("본인인증한 전화번호와 로그인시 입력한 전화번호가 다릅니다.");
		}
	}

	@Override
	public List<CntrPoint> getOrderAgencyCntrPointList(long userId, SearchParam searchParam) {
		CntrPointParam param = new CntrPointParam();
		param.setUserId(userId);
		Pagination pagination = searchParam.getPagination();
		if (pagination == null) {
			pagination = Pagination.getInstance(myPageService.getCntrPointCnt(param), searchParam.getItemsPerPage());
			pagination.setCurrentPage(searchParam.getPage());
		}
		searchParam.setPagination(pagination);

		return myPageService.getCntrPointInfo(param);
	}

	@Override
	public User getOrderAgencyCntrbtrByUserId(long userId) {
		return userService.getUserByUserId(userId);
	}

	@Override
	public User getOrderAgencyCntrbtrInfoByRequest(HttpServletRequest request) {
		String token = JwtUtils.getToken(request);
		String salesonId = getSalesonId(request);

		OrderAgencyLoginConfirmInfo param = new OrderAgencyLoginConfirmInfo();
		param.setUserSessionId(salesonId);
		param.setValidAccessCd(token);

		OrderAgencyLoginConfirmInfo loginInfo = orderAgencyMapper.selectAgencyLoginConfirmInfo(param);

		if (loginInfo != null && loginInfo.getCntrbtrId() > 0) {
			return getOrderAgencyCntrbtrByUserId(loginInfo.getCntrbtrId());
		} else {
			return null;
		}
	}

	@Override
	public OrderAgencyManagerInfo selectOrderAgencyManagerInfo(long managerId) {
		return orderAgencyMapper.selectOrderAgencyManagerInfo(managerId);
	}

	@Override
	public List<OrderAgencyOrderListInfo> selectOrderAgencyOrderList(OrderParam orderParam) {
		if (UserUtils.hasMasterManagerRole()) {
			orderParam.setUserId(0);
		} else if (UserUtils.hasLocgovManagerRole()) {
			orderParam.setUserId(UserUtils.getUser().getUserId());
			orderParam.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
		} else {
			throw new OpRuntimeException("잘못된 접근입니다.");
		}

		int itemsPerPage = orderParam.getItemsPerPage();
		int totalCnt = orderAgencyMapper.selectOrderAgencyOrderCnt(orderParam);

		Pagination pagination = Pagination.getInstance(totalCnt, itemsPerPage);
		int page = orderParam.getPage();
		if (page < 1) {
			page = 1;
		}
		pagination.setCurrentPage(page);
		orderParam.setPagination(pagination);

		List<OrderAgencyOrderListInfo> list = orderAgencyMapper.selectOrderAgencyOrderList(orderParam);

		return list;
	}

	@Override
	public String selectOrderAgencyTempDataId(OrderAgentOrderData orderAgentOrderData) {
		TempData data = tempEncDataService.insertTempData(orderAgentOrderData);
		if (data == null) {
			return "";
		}
		return data.getDataId();
	}

	@Override
	public OrderAgentOrderData selectOrderAgencyTempDataByDataId(String dataId) {
		TempData tempDataParam = new TempData();
		tempDataParam.setDataId(dataId);
		Object obj = tempEncDataService.getTempData(tempDataParam, OrderAgentOrderData.class);
		if (obj != null && obj instanceof OrderAgentOrderData) {
			return (OrderAgentOrderData) obj;
		} else {
			return null;
		}
	}



}
