package saleson.shop.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.security.mapper.SecurityMapper;
import com.onlinepowers.framework.security.userdetails.OpUserDetailsService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import saleson.api.common.enumerated.UserAdminRole;
import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.FaqType;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.designateddonation.DesignatedDonationMapper;
import saleson.shop.designateddonation.DesignatedDonationService;
import saleson.shop.designateddonation.domain.DesignatedPart;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequest;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequestResult;
import saleson.shop.designateddonation.support.DesignatedPartParam;
import saleson.shop.log.ManagerHistService;
import saleson.shop.mailconfig.MailConfigService;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.mailconfig.support.ManagerRequestApprovalMail;
import saleson.shop.mailconfig.support.ManagerRequestRejectMail;
import saleson.shop.notice.domain.ManagerNotice;
import saleson.shop.user.domain.GeneralCustomer;
import saleson.shop.user.domain.ManagerRequest;
import saleson.shop.user.domain.ManagerRequestCriteriaEncryptor;
import saleson.shop.user.domain.ManagerRequestEncryptor;
import saleson.shop.user.domain.ManagerRequestResult;
import saleson.shop.user.domain.PersonInCharge;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserDetailEncryptor;
import saleson.shop.user.domain.UserEncryptor;
import saleson.shop.user.support.GeneralCustomerSearchParam;
import saleson.shop.user.support.ManagerRequestSearchParam;
import saleson.shop.user.support.PersonInChargeSearchParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Service("managerRequestService")
public class ManagerRequestServiceImpl extends EgovAbstractServiceImpl implements ManagerRequestService {
	private static final Logger log = LoggerFactory.getLogger(ManagerRequestServiceImpl.class);

	private final UserMapper userMapper;
	private final UserEncryptor userEncryptor;
	private final UserDetailEncryptor userDetailEncryptor;
	private final PasswordEncoder passwordEncoder;
	private final ManagerRequestMapper managerRequestMapper;
	private final ManagerRequestEncryptor managerRequestEncryptor;
	private final ManagerRequestCriteriaEncryptor managerRequestCriteriaEncryptor;
	private final SecurityMapper securityMapper;
	private final MailConfigService mailConfigService;
	private final Cryptor cryptor;
	private final DataMasking dataMasking;
	private final GeneralCustomerMapper generalCustomerMapper;
	private final UserService userService;

	private final DesignatedDonationMapper designatedDonationMapper;

	private final PersonInChargeMapper personInChargeMapper;

	private final ManagerHistService managerHistService;

	/**
	 * 관리자 정보 조회
	 * @param loginId
	 * @return
	 */
	@Override
	public User getManagerByLoginId(String loginId) {
		User user = managerRequestMapper.getManagerByLoginId(loginId);
		// 복호화
		decryptData(user);
		return user;
	}

	/****
	 * 오프라인 관리자 상세 정보 조회
	 * @param userId
	 * @return
	 */
	public PersonInCharge getOFFManagerByUserId(Long userId) {
		PersonInCharge details = new PersonInCharge();
		try {
			PersonInChargeSearchParam adminSearchParam = new PersonInChargeSearchParam();
			adminSearchParam.setUserId(userId);
			adminSearchParam.setArrAuthority("ROLE_ADMIN_7,ROLE_ADMIN_8".split(","));
			// 운영관리자 상세 조회
			details = personInChargeMapper.getChargerDetails(adminSearchParam);
		} catch (RuntimeException e1) {
			log.error("ERROR: {}", getClass().getName() + " :: getLoginOFFManagerDetail RuntimeException ===========");
			return null;
		}
		return details;
	}

	/**
	 * 오프라인 담당자 로그인 메시지
	 * @return
	 * */
	@Override
	public String getMsgLoginOFFManager() {
		String msg = "";
		String role = "";
		User user = UserUtils.getUser();

		PersonInCharge details = new PersonInCharge();
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_7".equals(userRole.getAuthority()) || "ROLE_ADMIN_8".equals(userRole.getAuthority())) {
				role = userRole.getAuthority();
				details = getOFFManagerByUserId(user.getUserId());
				break;
			}
		}

		// 오프라인 담당자 아님
		if(role.isEmpty() || details == null) {
			return msg;
		}

		String psitnNm = details.getPsitnNm();
		String userNm = details.getUserName();
		msg = details.getBankNm() +" " + psitnNm+"지점 "+ userNm + "님으로 로그인 하셨습니다.\\n해당 정보가 다를 경우 정보를 수정해주시기 바랍니다.";
		if(psitnNm == null || (userNm == null || userNm == "")) {
			msg = "오프라인담당자 미입력 정보가 있습니다.\\n회원정보를 수정해주시기 바랍니다.";
		}

		return msg;
	}

	/**
	 * 로그인 사용자 관리자 확인(기존)
	 * 금융인증서 적용 시 삭제 예정
	 * @param loginId
	 * @param password
	 * @return
	 */
	@Override
	public ManagerRequestResult getLoginUserValid(HttpServletRequest request, String loginId, String password) {

		// 0. 결과값
		ManagerRequestResult result = new ManagerRequestResult();
		result.setCode("SUCC");

		try {

			// 1. 관리자 회원인지 확인
			User manager = this.getManagerByLoginId(loginId);

			// 2. 로그인한 아이디가 관리자에 존재하는 경우
			if(manager != null) {
				GeneralCustomerSearchParam searchParam = new GeneralCustomerSearchParam();
				searchParam.setUserId(manager.getUserId());
				GeneralCustomer customer = generalCustomerMapper.getGeneralCustomerDetailsNotStatusCode(searchParam);
				List<UserRole> userRoles = userMapper.getManagerRoleListByUserId(manager.getUserId());

				Optional<UserRole> roleSearch = userRoles.stream()
						.filter(x -> x.getAuthority().equals("ROLE_ADMIN_7") || x.getAuthority().equals("ROLE_ADMIN_8")).findFirst();

				//지자체 담당자는 아이디, 비밀번호 로그인 안되도록 2023-08-31
				Optional<UserRole> roleLocgovSearch = userRoles.stream()
						.filter(x -> x.getAuthority().equals("ROLE_ADMIN_5") || x.getAuthority().equals("ROLE_ADMIN_6")).findFirst();

				String todayfm = new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()));
				String daySet 		= "20230901";

				/*if(daySet.compareTo(todayfm) <= 0) {
					if(roleLocgovSearch.isPresent()) {
						result.setCode("ERR_LOCGOV");
						return result;
					}
				}*/
				//지자체 담당자는 아이디, 비밀번호 로그인 안되도록 2023-08-31

				if(!roleSearch.isPresent() && StringUtils.defaultIfEmpty((String) customer.getMberCi(), "").equals("")) {
					result.setCode("EMPTY_CI");
					return result;
				}

				// 2-1. 계정 잠김 확인
				if(CommonUtils.intNvl(manager.getLoginFailCount()) >= 5) {
					result.setCode("ERR_ROCK");
					return result;
				}

				// 2-2. 입력된 패스워드가 다른 경우
				if(!passwordEncoder.matches(pCrypto.Encrypt("hash.5", password, ""), manager.getPassword())) {
					managerRequestMapper.updateManagerLoginFailCount(manager.getUserId());
					result.setCode("ERR_MNG_PASS");
					return result;

				// 2-3. 유효한 패스워드인지 확인 (PASS_CHANGE_F: 초기 비밀번호 설정, PASS_CHANGE_I: 관리자 비밀번호 초기화, PASS_CHANGE_C: 관리자 비밀번호 변경)
				} else {
					String passwordType = managerRequestMapper.getPasswordChangeTypeByUserId(manager.getUserId());

					if(passwordType != null && !"".equals(passwordType)) {
						result.setCode(passwordType);
						return result;
					}
					// srhan. 20251021. ID/PW 넣고 로그인 버튼 누르면 인증번호 넣지 않고 새로고침해도 로그인되는 버그 수정
//					String txt = userService.setSessionByManager(request, manager);
//					result.setCode(txt);

					return result;


				}

			}

			// 3. 일반회원인지 확인
			String deLoginStr = "";
			try {
				deLoginStr = pCrypto.Encrypt("normal", loginId, "");
			} catch (UnsupportedEncodingException e) {
				log.error(getClass().getName() + " getLoginUserValid UnsupportedEncodingException error", e);
			}
			User user = securityMapper.getUserByLoginId(deLoginStr);

			if(user != null) {
				// 3-1. 입력된 패스워드가 다른 경우
				if(!passwordEncoder.matches(pCrypto.Encrypt("hash.5", password, ""), user.getPassword())) {
					result.setCode("ERR_USER_PASS");
				} else {

					// 4. 관리자 신청 이력 조회
					ManagerRequestSearchParam searchParam = new ManagerRequestSearchParam();
					searchParam.setUserId(user.getUserId());
					searchParam.setConfmSttusCode("200");
					searchParam.encrypt(managerRequestCriteriaEncryptor);

					List<ManagerRequest> managerRequestList = managerRequestMapper.getManagerRequestList(searchParam);
					searchParam.decrypt(managerRequestCriteriaEncryptor);

					if(managerRequestList == null || managerRequestList.size() == 0) {
						result.setUserId(user.getUserId());
						result.setCode("REQ_NOT");
					} else {
						result.setCode("REQ");
					}
				}

			} else {
				result.setCode("ERR_USER_NOT");
			}

		} catch (UnsupportedEncodingException e) {
			log.error("ERROR: {}", getClass().getName() + " :: getLoginUserValid UnsupportedEncodingException ===========");
			result.setCode("ERR");
		} catch (RuntimeException e1) {
			log.error("ERROR: {}", getClass().getName() + " :: getLoginUserValid RuntimeException ===========");
			result.setCode("ERR");
		}

		return result;
	}

	/**
	 * 로그인 사용자 관리자 확인
	 * @param loginId
	 * @param password
	 * @return
	 */
	@Override
	public ManagerRequestResult getLoginUserValid(HttpServletRequest request,String loginId, String password, String managerUse) {

		// 0. 결과값
		ManagerRequestResult result = new ManagerRequestResult();
		result.setCode("SUCC");

		try {

			// 1. 관리자 회원인지 확인
			User manager = this.getManagerByLoginId(loginId);

			// 2. 로그인한 아이디가 관리자에 존재하는 경우
			if(manager != null) {
				// 2-1. 계정 잠김 확인
				if(CommonUtils.intNvl(manager.getLoginFailCount()) >= 5) {
					result.setCode("ERR_ROCK");
					return result;
				}

				// 2-2. 입력된 패스워드가 다른 경우
				if(!passwordEncoder.matches(pCrypto.Encrypt("hash.5", password, ""), manager.getPassword())) {
					managerRequestMapper.updateManagerLoginFailCount(manager.getUserId());
					result.setCode("ERR_MNG_PASS");

					return result;

				// 2-3. 유효한 패스워드인지 확인 (PASS_CHANGE_F: 초기 비밀번호 설정, PASS_CHANGE_I: 관리자 비밀번호 초기화, PASS_CHANGE_C: 관리자 비밀번호 변경)
				} else {
					String passwordType = managerRequestMapper.getPasswordChangeTypeByUserId(manager.getUserId());

					if(passwordType != null && !"".equals(passwordType)) {
						result.setCode(passwordType);

						return result;
					}


					if ("Y".equals(managerUse)) {
						UserAdminRole role = UserAdminRole.findByUserRole(managerRequestMapper.getUserRole(manager.getUserId()));

						// 개발 중에 예외처리
						if (!UserAdminRole.OFF.equals(role)) {
							result.setCode(role.name());
							return result;
						}
					}

				}

				return result;
			}

			// 3. 일반회원인지 확인
			String deLoginStr = "";
			try {
				deLoginStr = pCrypto.Encrypt("normal", loginId, "");
			} catch (UnsupportedEncodingException e) {
				log.error(getClass().getName() + " getLoginUserValid UnsupportedEncodingException error", e);
			}
			User user = securityMapper.getUserByLoginId(deLoginStr);

			if(user != null) {
				// 3-1. 입력된 패스워드가 다른 경우
				if(!passwordEncoder.matches(pCrypto.Encrypt("hash.5", password, ""), user.getPassword())) {
					result.setCode("ERR_USER_PASS");
				} else {

					// 4. 관리자 신청 이력 조회
					ManagerRequestSearchParam searchParam = new ManagerRequestSearchParam();
					searchParam.setUserId(user.getUserId());
					searchParam.setConfmSttusCode("200");
					searchParam.encrypt(managerRequestCriteriaEncryptor);

					List<ManagerRequest> managerRequestList = managerRequestMapper.getManagerRequestList(searchParam);
					searchParam.decrypt(managerRequestCriteriaEncryptor);

					if(managerRequestList == null || managerRequestList.size() == 0) {
						result.setUserId(user.getUserId());
						result.setCode("REQ_NOT");
					} else {
						result.setCode("REQ");
					}
				}

			} else {
				result.setCode("ERR_USER_NOT");
			}

		} catch (UnsupportedEncodingException e) {
			log.error("ERROR: {}", getClass().getName() + " :: getLoginUserValid UnsupportedEncodingException ===========");
			result.setCode("ERR");
		} catch (RuntimeException e1) {
			log.error("ERROR: {}", getClass().getName() + " :: getLoginUserValid RuntimeException ===========");
			result.setCode("ERR");
		}

		return result;
	}

	/**
	 * 관리자 신청 등록
	 * @param managerRequest
	 * @return
	 */
	@Override
	public int insertManagerRequest(ManagerRequest managerRequest) {
		int nResult = 0;
		managerRequest.setConfmSttusCode("200");
		managerRequest.setFrstRegisterId(managerRequest.getUserId());
		managerRequest.setLastUpdusrId(managerRequest.getUserId());
		managerRequest.encrypt(managerRequestEncryptor);
		nResult = managerRequestMapper.insertManagerRequest(managerRequest);
//		if(nResult > 0 && "ROLE_ADMIN_8".equals(managerRequest.getReqstSeCode())) {//오프라인담당자일경우 지점연락처 저장
//			personInChargeMapper.updateUserDetailOffPersonTel(managerRequest);
//		}
		return nResult;
	}

	/**
	 * 관리자 권한 승인관리 목록 갯수 조회
	 * @param managerRequestSearchParam
	 * @return
	 */
	@Override
	public int getManagerRequestCountByParam(ManagerRequestSearchParam searchParam) {
		int totalCount = 0;

		// 조회 가능 소속구분 셋팅
		String reqstSeCode = this.getReqstSeCode(false);

		// 관리자 권한 승인관리 목록 갯수 조회
		if(reqstSeCode != null && !"".equals(reqstSeCode)) {
			searchParam.setReqstSeCode(reqstSeCode);
			searchParam.setSuperUserId(UserUtils.getUser().getUserId());
			searchParam.encrypt(managerRequestCriteriaEncryptor);
			totalCount = managerRequestMapper.getManagerRequestCountByParam(searchParam);
			searchParam.decrypt(managerRequestCriteriaEncryptor);
		}

		return totalCount;
	}

	/**
	 * 관리자 권한 승인관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<ManagerRequest> getManagerRequestListByParam(ManagerRequestSearchParam searchParam) {
		List<ManagerRequest> managerRequestList = null;

		// 조회 가능 소속구분 셋팅
		String reqstSeCode = this.getReqstSeCode(false);

		// 관리자 권한 승인관리 목록 갯수 조회
		if(reqstSeCode != null && !"".equals(reqstSeCode)) {
			searchParam.setReqstSeCode(reqstSeCode);
			searchParam.setSuperUserId(UserUtils.getUser().getUserId());
			searchParam.encrypt(managerRequestCriteriaEncryptor);
			managerRequestList = managerRequestMapper.getManagerRequestListByParam(searchParam);
			searchParam.decrypt(managerRequestCriteriaEncryptor);
		}

		return managerRequestList;
	}

	/**
	 * 로그인 사용자의 슈퍼담당자 권한으로 조회 가능 소속구분 조회
	 * @return
	 */
	private String getReqstSeCode(boolean isDsgnCntr) {
		String reqstSeCode = "";

		// 로그인 사용자 권한 목록 조회
		List<UserRole> userRoleList = UserUtils.getUser().getUserRoles();

		// 로그인 사용자 슈퍼담당자 권한 체크
		if(userRoleList != null) {
			for(UserRole userRole : userRoleList) {
				String auth = userRole.getAuthority();
				if(auth != null && auth.indexOf("ROLE_ADMIN_") > -1) {
					switch(auth) {
						case "ROLE_ADMIN_1" :
							reqstSeCode = "ALL";
							break;
						case "ROLE_ADMIN_3" :
							reqstSeCode = "ALL";
							break;
						case "ROLE_ADMIN_5" :
							if (isDsgnCntr) {
								reqstSeCode = "ROLE_ADMIN_10";
							} else {
								reqstSeCode = "ROLE_ADMIN_6";
							}
							break;
						case "ROLE_ADMIN_7" :
							reqstSeCode = "ROLE_ADMIN_8";
							break;
						default :
							break;
					};

					break;
				};
			};
		}

		return reqstSeCode;
	}

	/**
	 * 관리자 권한 승인관리 상세 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public ManagerRequest getManagerRequestDetails(ManagerRequestSearchParam searchParam) {
		return managerRequestMapper.getManagerRequestDetails(searchParam);
	}

	/**
	 * 관리자 권한 승인관리 - '거절'
	 * @param managerRequest
	 * @return
	 */
	@Override
	public ManagerRequestResult updateManagerRequestReject(ManagerRequest managerRequest) {
		ManagerRequestResult result = new ManagerRequestResult();
		result.setCode("ERR");


		/*************************************************************************
		 *  0. 상세 정보 조회
		 *************************************************************************/
		ManagerRequestSearchParam searchParam = new ManagerRequestSearchParam();
		searchParam.setUserId(managerRequest.getUserId());
		searchParam.setReqstSn(managerRequest.getReqstSn());
		ManagerRequest details = managerRequestMapper.getManagerRequestDetails(searchParam);


		/*************************************************************************
		 *  1. 유효성 검사
		 *************************************************************************/
		// 1-1. 유효한 승인관리 정보인지 확인
		if(details == null) {
			result.setCode("ERR_NOT_FOUND");
			return result;
		};

		// 1-2. 현재 '대기' 상태인지 확인
		if(!"200".equals(details.getConfmSttusCode())) {
			result.setCode("ERR_SYNC_STTUS");
			return result;
		};

		// 1-3. '거절사유' 존재하는지 확인
		if("".equals(managerRequest.getRejectResn())) {
			result.setCode("ERR_NOT_RESN");
			return result;
		}


		/*************************************************************************
		 *  2. '거절' 처리
		 *************************************************************************/
		int nResult = managerRequestMapper.updateManagerRequest(managerRequest);
		if(nResult > 0) result.setCode("SUCC");


		/*************************************************************************
		 *  3. 사용자 메일 발송 여부(0: 수신, 1: 비수신) 확인 - 현재 보류
		 *************************************************************************/
		if(nResult > 0 && "0".equals(details.getReceiveEmail())) {
			details.setRejectResn(managerRequest.getRejectResn());
			this.sendMail(details, "reject");
		}

		return result;
	}

	/**
	 * 관리자 권한 승인관리 - '승인'
	 * @param managerRequest
	 * @return
	 */
	@Override
	public ManagerRequestResult updateManagerRequestApproval(ManagerRequest managerRequest) {
		ManagerRequestResult result = new ManagerRequestResult();
		result.setCode("ERR");


		/*************************************************************************
		 *  0. 상세 정보 조회
		 *************************************************************************/
		ManagerRequestSearchParam searchParam = new ManagerRequestSearchParam();
		searchParam.setUserId(managerRequest.getUserId());
		searchParam.setReqstSn(managerRequest.getReqstSn());
		ManagerRequest details = managerRequestMapper.getManagerRequestDetails(searchParam);


		/*************************************************************************
		 *  1. 유효성 검사
		 *************************************************************************/
		// 1-1. 유효한 승인관리 정보인지 확인
		if(details == null) {
			result.setCode("ERR_NOT_FOUND");
			return result;
		};

		// 1-2. 현재 '대기' 상태인지 확인
		if(!"200".equals(details.getConfmSttusCode())) {
			result.setCode("ERR_SYNC_STTUS");
			return result;
		};

		// 1-3. 중복 아이디 체크
		User manager = managerRequestMapper.getManagerByLoginId(details.getLoginId());
		if(manager != null) {
			result.setCode("ERR_DUP_ID");
			return result;
		}

		// 1-4. 요청 사용자 유효성 체크
		String statusCode = CommonUtils.dataNvl(managerRequestMapper.getUserStatusCodeByUserId(managerRequest.getUserId()));
		if("".equals(statusCode)) {
			result.setCode("ERR_USER");
			return result;
		}

		// 1-5. 요청 사용자 상태값 확인 (9: 정상, 3: 탈퇴, 4: 휴면계정)
		if(!"9".equals(statusCode)) {
			result.setCode("ERR_STS");
			result.setStatusCode(statusCode);
			return result;
		}


		/*************************************************************************
		 *  2. '승인' 처리
		 *************************************************************************/
		// 2-1. 관리자 권한 승인관리 - '승인' 처리
		int nResult = managerRequestMapper.updateManagerRequest(managerRequest);

		// 2-2. 사용자 정보를 관리자 테이블로 이관
		if(nResult > 0) {
			nResult = managerRequestMapper.insertManagerByUserSync(managerRequest);
		}

		// 2-3. 권한 셋팅
		if(nResult > 0) {
			// 관리자 권한 셋팅
			UserRole userRole = new UserRole();
			userRole.setUserId(managerRequest.getUserId());
			userRole.setAuthority("ROLE_OPMANAGER");
			userMapper.insertUserRole(userRole);

			// 부담당자 권한 셋팅 (지자체 & 행안부 & 시스템 & 오프라인)
			userRole.setUserId(managerRequest.getUserId());
			userRole.setAuthority(details.getReqstSeCode());
			userMapper.insertUserRole(userRole);

			// 결과값 셋팅
			result.setCode("SUCC");

			// 2-4. [GGSR-26-369] 이력저장
			managerHistService.insertManagerHist(managerRequest, "H");

		} else {
			throw new RuntimeException();
		}


		/*************************************************************************
		 *  3. 사용자 메일 발송 여부(0: 수신, 1: 비수신) 확인
		 *************************************************************************/
		if(nResult > 0 && "0".equals(details.getReceiveEmail())) {
			this.sendMail(details, "approval");
		}

		return result;
	}

	/**
	 * 관리자 권한 승인관리 메일 발송 - 승인 / 거절
	 * @param details
	 * @param mailType (approval: 승인, reject: 거절)
	 * @return
	 */
	private String sendMail(ManagerRequest details, String mailType) {
		StringBuffer outResult = new StringBuffer();

		try {
			// 0. 유효성 검사
			if("".equals(CommonUtils.dataNvl(details.getUserName())) || "".equals(CommonUtils.dataNvl(details.getEmail()))
					|| "".equals(CommonUtils.dataNvl(details.getLoginId())) || "".equals(CommonUtils.dataNvl(details.getReqstSeCode()))
					|| "".equals(mailType)) {
				return "FAIL";
			}

			// 1. 메일 템플릿 조회
			String templateId = "manager_request_"+mailType;
			MailConfig mailConfig = mailConfigService.getMailConfigByTemplateId(templateId);
			if (mailConfig == null || !"Y".equals(mailConfig.getBuyerSendFlag())) return "FAIL";

			// 2. 메일 템플릿 대체코드 변환 데이터 셋팅
			ManagerRequest managerRequest = new ManagerRequest();
			managerRequest.setLoginId(CommonUtils.dataNvl(details.getLoginId()));
			managerRequest.setReqstSeCode(CommonUtils.dataNvl(details.getReqstSeCode()));
			managerRequest.setLocgovNm(CommonUtils.dataNvl(details.getLocgovNm()));
			managerRequest.setUpperlocgovNm(CommonUtils.dataNvl(details.getUpperlocgovNm()));
			managerRequest.setRejectResn(CommonUtils.dataNvl(details.getRejectResn()).replaceAll("(\r\n|\n)", "<br/>"));

			// 3. 메일 템플릿 대체코드 변환 적용
			MailConfig mConfig = null;
			if("approval".equals(mailType)) {
				ManagerRequestApprovalMail mail = new ManagerRequestApprovalMail(managerRequest, mailConfig, cryptor, dataMasking);
				mConfig = mail.getMailConfig();
			} else {
				ManagerRequestRejectMail mail = new ManagerRequestRejectMail(managerRequest, mailConfig, cryptor, dataMasking);
				mConfig = mail.getMailConfig();
			}

			// 4. 메일 전송 데이터 셋팅
			String emsSendUserName = SalesonProperty.getEmsSendUsername();
			String emsSendEmail = SalesonProperty.getEmsSendEmail();
			String categoryNm = "approval".equals(mailType) ? "관리자권한승인" : "관리자권한거절";
			String linkNm = "approval".equals(mailType) ? "관리자권한승인" : "관리자권한거절";

			Map<String, Object> mailDataMap = new HashMap<>();
			mailDataMap.put("title", ShopUtils.unescapeHtml(CommonUtils.dataNvl(mConfig.getBuyerSubject())));
			mailDataMap.put("content", ShopUtils.unescapeHtml(CommonUtils.dataNvl(mConfig.getBuyerContent())));
			mailDataMap.put("sendInfo", emsSendEmail+ " "+ emsSendUserName);
			mailDataMap.put("rcvInfo", details.getEmail()+" "+details.getUserName());
			mailDataMap.put("sendDate", "");
			mailDataMap.put("sendType", "");
			mailDataMap.put("categoryNm", categoryNm);
			mailDataMap.put("linkNm", linkNm);
			mailDataMap.put("memo", "");

			Map<String, Object> mailMap = new HashMap<>();
			mailMap.put("data", mailDataMap);

			// 5. 메일 전송 데이터 문자로 변환
			ObjectMapper mapper = new ObjectMapper();
			String jsonValue = mapper.writeValueAsString(mailMap);

			// 6. 메일 발송 셋팅
			URL url = new URL(SalesonProperty.getEmsHost());

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);

			try (
					InputStream is = conn.getInputStream();
					InputStreamReader isr = new InputStreamReader(is, "UTF-8");
					OutputStream os = conn.getOutputStream();
					BufferedReader in = new BufferedReader(isr);
					) {

				os.write(jsonValue.getBytes("UTF-8"));
				os.flush();

				// 7.리턴된 결과 읽기
				String inputLine = null;

				while ((inputLine = in.readLine()) != null) {
					outResult.append(inputLine);
				}

			} catch (IOException e) {
				log.error("ERROR: {}", getClass().getName() + " :: sendMail IOException ===========");

			} finally {
				if(conn != null) conn.disconnect();
			}

		} catch (IOException e1) {
			log.error("ERROR: {}", getClass().getName() + " :: sendMail IOException ===========");
		} catch (RuntimeException e2) {
			log.error("ERROR: {}", getClass().getName() + " :: sendMail RuntimeException ===========");
		}

		return outResult.toString();
	}

	/**
	 * 관리자 권한 승인관리 상세 조회 접근 권한 체크
	 * @param searchParam
	 * @param requestContext
	 * @return
	 */
	@Override
	public int getManagerRequestDetailsAuthCount(ManagerRequestSearchParam searchParam) {
		if (FaqType.F_CNTR_DESIGNATED.name().equals(searchParam.getConditionType())) {
			searchParam.setReqstSeCode(getReqstSeCode(true));
		} else {
			searchParam.setReqstSeCode(this.getReqstSeCode(false));
		}
		searchParam.setSuperUserId(UserUtils.getUser().getUserId());
		searchParam.encrypt(managerRequestCriteriaEncryptor);
		int nResult = managerRequestMapper.getManagerRequestDetailsAuthCount(searchParam);
		searchParam.decrypt(managerRequestCriteriaEncryptor);
		return nResult;
	}

	/**
	 * 회원 정보 조회
	 * @param loginId
	 * @return
	 */
	@Override
	public ManagerRequest getUserInfoByLoginId(String loginId) {
		return managerRequestMapper.getUserInfoByLoginId(loginId);
	}

	/**
	 * 컬럼 데이터 복호화
	 * @param user
	 */
	private void decryptData(User user) {
		// 복호화
		if (user != null) {
			user.decrypt(userEncryptor, ShopUtils.needMasking());

			if (user.getUserDetail() != null) {
				UserDetail userDetail = (UserDetail) user.getUserDetail();
				userDetail.decrypt(userDetailEncryptor, ShopUtils.needMasking());
			}
		}
	}

	@Override
	public List<ManagerRequest> getManagerRequestHistory(ManagerRequestSearchParam searchParam) {
		return managerRequestMapper.getManagerRequestHistory(searchParam);
	}

	@Override
	public List<ManagerNotice> getNoticeList(String type) {
		// TODO Auto-generated method stub
		return managerRequestMapper.getNoticeList(type);
	}
}
