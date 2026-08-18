package saleson.seller.user;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.binding.BindingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.isms.ConfigIsmsService;
import com.onlinepowers.framework.security.mapper.SecurityMapper;
import com.onlinepowers.framework.security.service.SecurityService;
import com.onlinepowers.framework.security.userdetails.OpUserDetails;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import com.privacy.pCrypto;

import saleson.common.enumeration.AuthorityType;
import saleson.common.security.crypto.SellerPwSalt;
import saleson.common.utils.RandomStringUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.UserUtils;
import saleson.model.user.SellerUserLogin;
import saleson.seller.main.domain.PkiSellerLogin;
import saleson.seller.main.domain.Seller;
import saleson.shop.log.LoginLogService;
import saleson.shop.sendsmslog.SendSmsLogService;
import saleson.shop.sendsmslog.domain.SendSmsLog;
import saleson.shop.smsconfig.domain.SmsConfig;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.SellerUser;
import saleson.shop.user.domain.UserCriteriaEncryptor;
import saleson.shop.user.domain.UserEncryptor;
import saleson.shop.user.support.UserSearchParam;
import saleson.shop.userrole.UserRoleService;

@Service("sellerUserService")
public class SellerUserServiceImpl implements SellerUserService {

	private static final Logger log = LoggerFactory.getLogger(SellerUserServiceImpl.class);

    @Autowired
    private SellerUserMapper sellerUserMapper;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private SendSmsLogService sendSmsLogService;

    @Autowired
    private UserEncryptor userEncryptor;

    @Autowired
    private UserCriteriaEncryptor userCriteriaEncryptor;


    @Autowired
    private SellerUserLoginRepository sellerUserLoginRepository;


    @Autowired
    private SecurityService securityService;

    @Autowired
    private SecurityMapper securityMapper;

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private SellerPwSalt sellerPwSalt;

	@Autowired
	private ConfigIsmsService configIsmsService;


    @Override
    public int insertSellerUser(long sellerId, User user) throws UserException {
        return insertSellerUser(sellerId, user, false);
    }

    @Override
    public int insertSellerMasterUser(long sellerId, User user) throws UserException {
        return insertSellerUser(sellerId, user, true);
    }

    private int insertSellerUser(long sellerId, User user, boolean isMaster) throws UserException {

        if (sellerId <= 0) {
            throw new UserException("판매자 정보가 없습니다.");
        }

//        long userId = sequenceService.getLong("OP_USER");
        long userId = userService.selectNewUserId();

        String enctyptPassword = "";

		try {
			enctyptPassword = pCrypto.Encrypt("hash.5", sellerPwSalt.getStringForSalt() + user.getLoginId() + user.getPassword().trim(), "").trim();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}

        user.setUserId(userId);
        user.setPassword(enctyptPassword);
        user.setPasswordExpiredDate(userService.getPasswordExpiredDate());
        user.setStatusCode("9");

        user.encrypt(userEncryptor);

        int insertCount =  sellerUserMapper.insertSellerUser(user);

        if (insertCount > 0) {

            UserRole userRole = new UserRole();
            userRole.setUserId(userId);

            String baseAuthority = AuthorityType.SELLER.getCode();
            // ROLE_SELLER 추가
            userRole.setAuthority(baseAuthority);
            userRoleService.insertUserRole(userRole);

            // 해당 판매자 ROLE 추가
            userRole.setAuthority(baseAuthority+"_"+sellerId);
            userRoleService.insertUserRole(userRole);

            // 엑셀 다운로드 권한 추가
            userRole.setAuthority("ROLE_EXCEL");
            userRoleService.insertUserRole(userRole);

            if (isMaster) {
                userRole.setAuthority(AuthorityType.SELLER_MASTER.getCode());
                userRoleService.insertUserRole(userRole);
            }

        }

        return insertCount;
    }


    @Override
    public int updateSellerUser(User user) throws UserException{
    	return sellerUserMapper.updateSellerUser(user);
    }


    @Override
    public int updateSellerUser(long sellerId, User user) throws UserException{

        if (!isSellerUserBySellerId(sellerId, user.getUserId())) {
            throw new UserException("존재하지 않는 사용자 입니다.");
        }

        if (!ObjectUtils.isEmpty(user.getPassword())) {
            String enctyptPassword = passwordEncoder.encode(user.getPassword().trim());
            user.setPassword(enctyptPassword);
        }

        user.encrypt(userEncryptor);

        return sellerUserMapper.updateSellerUser(user);
    }

    @Override
    public int deleteSellerUserByList(long sellerId, ListParam listParam) throws UserException{

        int count = 0;

        if (listParam.getId() != null) {
            for (String id : listParam.getId())
                if (StringUtils.isNotEmpty(id)) {

                    long userId = Long.parseLong(id);

                    if (!isSellerUserBySellerId(sellerId, userId)) {
                        throw new UserException("존재하지 않는 사용자 입니다.");
                    }

                    count += sellerUserMapper.deleteSellerUserById(userId);

            }
        }

        return count;
    }

    @Override
    public int getSellerUserListCount(UserSearchParam userSearchParam) {
        return sellerUserMapper.getSellerUserListCount(userSearchParam);
    }

    @Override
    public List<User> getSellerUserList(UserSearchParam userSearchParam) {
        userSearchParam.encrypt(userCriteriaEncryptor);
        List<User> users = sellerUserMapper.getSellerUserList(userSearchParam);
        userSearchParam.decrypt(userCriteriaEncryptor);
        users.forEach(u -> u.decrypt(userEncryptor, false));
        return users;
    }

    @Override
    public User getSellerUserById(long sellerId, long userId) throws UserException{

        User user = sellerUserMapper.getSellerUserById(userId);

        if (user == null) {
        	throw new UserException("존재하지 않는 사용자 입니다.");
        }

        user.decrypt(userEncryptor, false);

        if (sellerId != 0 || user.getUserRoles() != null) {
	        if(!isAuthorityForSellerUser(sellerId, user.getUserRoles())){
	            throw new UserException("존재하지 않는 사용자 입니다.");
	        }
        }

        return user;
    }

    @Override
    public User getSellerUserByLoginId(long sellerId, String loginId) throws UserException{

        User user = sellerUserMapper.getSellerUserByLoginId(loginId);

        if(!isAuthorityForSellerUser(sellerId, user.getUserRoles())){
            throw new UserException("존재하지 않는 사용자 입니다.");
        }

        return user;
    }

    private boolean isAuthorityForSellerUser(long sellerId, List<UserRole> userRoles) {

        String sellerRole = "ROLE_SELLER_" + sellerId;

        if (!ValidationUtils.isNull(userRoles) && !userRoles.isEmpty()) {
            for (UserRole role : userRoles) {
                if (sellerRole.equals(role.getAuthority())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isSellerUserBySellerId(long sellerId, long userId) throws  UserException {
        return !ValidationUtils.isNull(this.getSellerUserById(sellerId, userId));
    }

    @Override
    public boolean isDuplicateSellerUserByLoginId(String loginId) {
        return sellerUserMapper.getDuplicateSellerUserByLoginId(loginId) > 0;
    }

    @Override
    public void updateTempPasswordForSellerUser(long userId) throws UserException {
        User user = sellerUserMapper.getSellerUserById(userId);

        if (user.getPhoneNumber() == null) {
            throw new UserException("핸드폰 번호가 없습니다.");
        } else {

            String tempPassword = RandomStringUtils.getRandomString("!",7,10);

            User tempUser = new User();

            tempUser.setUserId(userId);
            tempUser.setPasswordType("T");
            tempUser.setPassword(passwordEncoder.encode(tempPassword));
            tempUser.setPasswordExpiredDate(userService.getPasswordExpiredDate());

            int count = sellerUserMapper.updatePasswordForSellerUser(tempUser);

            if (count > 0) {

                SmsConfig smsConfig = new SmsConfig();
                smsConfig.setBuyerSendFlag("Y");
                smsConfig.setBuyerContent("임시비밀번호\n"+tempPassword);
                smsConfig.setSmsType("sms");

                SendSmsLog sendSmsLog = new SendSmsLog();

                sendSmsLog.setContent(smsConfig.getBuyerContent());
                sendSmsLog.setSendType("SELLER_USER_TEMP_PASSOWRD");
                sendSmsLogService.sendSms(smsConfig, sendSmsLog, user.getPhoneNumber());

            }
        }
    }

    @Override
    public void updatePasswordForSellerUser(User user, String passowrd, String changePassowrd) throws UserException {
        String passwordOrg = null;
        String password = null;
        if (changePassowrd.indexOf(user.getLoginId()) > -1) {
			throw new UserException("비밀번호에 아이디가 포함될수 없습니다.");
		}

    	try {
//    		passwordOrg = pCrypto.Encrypt("hash.5", passowrd, "");
    		passwordOrg = pCrypto.Encrypt("hash.5", sellerPwSalt.getStringForSalt() + user.getLoginId() + passowrd, "").trim();
    		password = pCrypto.Encrypt("hash.5", sellerPwSalt.getStringForSalt() + user.getLoginId() + changePassowrd, "").trim();
		} catch (UnsupportedEncodingException e) {
			throw new UserException("비밀번호가 변경되지 않았습니다.");
		}

        if (passwordEncoder.matches(passwordOrg, user.getPassword().trim())) {
            if (passwordEncoder.matches(password, user.getPassword().trim())) {
                throw new UserException("변경 하려는 비밀번호가 동일합니다.");
            }

            User tempUser = new User();
            tempUser.setUserId(user.getUserId());
            tempUser.setPasswordType("N");
            tempUser.setPassword(password);
            tempUser.setPasswordExpiredDate(userService.getPasswordExpiredDate());
            int count = sellerUserMapper.updatePasswordForSellerUser(tempUser);
            if (count == 0) {
                throw new UserException("비밀번호가 변경되지 않았습니다.");
            }
        } else {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }

    }

    @Override
    public void updatePasswordForSellerUser(User pUser, String changePassowrd) throws UserException {
    	String password = null;
    	if (changePassowrd.indexOf(pUser.getLoginId()) > -1) {
			throw new UserException("비밀번호에 아이디가 포함될수 없습니다.");
		}

    	try {
    		password = pCrypto.Encrypt("hash.5", sellerPwSalt.getStringForSalt() + pUser.getLoginId() + changePassowrd, "").trim();
		} catch (UnsupportedEncodingException e) {
			throw new UserException("비밀번호가 변경되지 않았습니다.");
		}

    	//User user = sellerUserMapper.getSellerUserById(pUser.getUserId());
        if (passwordEncoder.matches(password, pUser.getPassword().trim())) {
            throw new UserException("변경 하려는 비밀번호가 동일합니다.");
        }

        SellerUser tempUser = new SellerUser();
        tempUser.setUserId(pUser.getUserId());
        tempUser.setPasswordType("N");
        tempUser.setPassword(password);
        tempUser.setPasswordExpiredDate(userService.getPasswordExpiredDate());
        tempUser.setPwdChgSellerYn("N");
        int count = sellerUserMapper.updatePasswordForSellerUser2(tempUser);
        if (count == 0) {
            throw new UserException("비밀번호가 변경되지 않았습니다.");
        }
    }

    @Override
    public long getSellerUserIdByLoginId(String loginId) {
    	try {
    		return sellerUserMapper.getSellerUserIdByLoginId(loginId);
    	} catch (BindingException e) {
    		return 0;
    	}
    }

    @Override
    public void updateSellerUserPassword(User user) {
        sellerUserMapper.updateSellerUserPassword(user);
    }

    @Override
    public void saveLoginSession(HttpSession session, long userId) {
        if (!ObjectUtils.isEmpty(session)) {
            saveLoginSession(session.getId(), userId);
        }
    }

    @Override
    public void saveLoginSession(String sessionId, long userId) {
        sellerUserLoginRepository.deleteSellerUserLoginsByUserId(userId);
        sellerUserLoginRepository.save(new SellerUserLogin(userId, sessionId));
    }


    @Override
    public List<SellerUserLogin> getLoginSessionByUserId(long userId) {
        return sellerUserLoginRepository.findSellerUserLoginsByUserId(userId);
    }

	@Override
	public SellerUser getSellerUserByLoginIdAndPwd(String loginId, String password) {
		SellerUser param = new SellerUser();
		param.setLoginId(loginId);

		param.setPassword(password);			// 쿼리에 암호화 처리 되어있어서 수정
		SellerUser sellerUser = sellerUserMapper.getSellerUserByLoginIdAndPwd(param);
		return sellerUser;
	}

	@Override
	public void updatePasswordForSellerLogin(SellerUser sellerUser) throws UserException {
		try {
			sellerUser.setPassword(pCrypto.Encrypt("hash.5", sellerPwSalt.getStringForSalt() + sellerUser.getLoginId() + sellerUser.getPassword().trim(), "").trim());
		} catch (UnsupportedEncodingException e) {
			log.error("updatePasswordForSellerLogin error", e);
		}
		sellerUser.setPasswordType("N");
		sellerUser.setPasswordExpiredDate(userService.getPasswordExpiredDate());
		sellerUser.setPwdChgSellerYn("N");
        int count = sellerUserMapper.updatePasswordForSellerUserLogin(sellerUser);
        if (count != 1) {
            throw new UserException("문제가 발생했습니다.");
        }
	}

	@Override
	public void loginProcess(SellerUser sellerUser, Seller seller, HttpServletRequest request) {
		try {
	//		PkiManagerLoginResult result = new PkiManagerLoginResult();
	//		result.setCode("FAIL");
	//
	//		// 1. DN 정보 확인
	//		if("".equals(CommonUtils.dataNvl(pkiManagerLogin.getMberDn()))) {
	//			result.setCode("ERR_DN");
	//			return result;
	//		}
	//
	//		// 2. 사용자 정보 확인
	//		User user = userMapper.getUserByMberDn(pkiManagerLogin.getMberDn());
	//		if(user == null) {
	//			result.setCode("ERR_USER_NOT");
	//			return result;
	//		}
	//
	//		// 3. 관리자 정보 확인
	//		User manager = managerReqeustMapper.getManagerByLoginId(user.getLoginId());
	//		if(manager == null) {
	//			result.setCode("ERR_MNG_NOT");
	//			return result;
	//		}
	//
	//		// 4. 관리자 상태값 체크
	//		if(!"9".equals(String.valueOf(manager.getStatusCode()))) {
	//			result.setStatusCode(String.valueOf(manager.getStatusCode()));
	//			result.setCode("ERR_STS");
	//			return result;
	//		}
	//
	//		// 5. 로그인 사용자 정보 조회
	//		String loginId = CommonUtils.dataNvl(manager.getLoginId()) + OpKeyHolder.OPMANAGER_LOGIN_KEY;
	//		UserDetails userDetails = userDetailsService.loadUserByUsername(loginId);

			// 5-1. 아이디, 권한 설정
			User user = securityService.getSellerUserByLoginId(sellerUser.getLoginId());

			List<GrantedAuthority> authList = new ArrayList<>();
			if (user.getUserRoles() != null) {
				for (UserRole userRole : user.getUserRoles()) {
					GrantedAuthority auth = new SimpleGrantedAuthority(userRole.getAuthority());
					authList.add(auth);
				}
			}

			UserDetails userDetails = new OpUserDetails(user, authList, false, null, false, false);
			Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(newAuth);

			// 5-2. 세션 설정
			HttpSession session = request.getSession(true);
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, newAuth);

			// 6. 세션 저장
			saveLoginSession(request.getSession(), UserUtils.getSellerUserId());

			// 7. 로그인 카운트 증가, 실패 카운트 초기화, 로그인 로그 추가
			//securityMapper.updateSellerUserLoginCount(user);
//			securityService.updateClearLoginFailCountForSellerUser(sellerUser.getLoginId());

			sellerUserMapper.clearLoginFailCnt(seller.getLoginId());

//			loginLogService.insertLoginLogBySeller(request, true);
			loginLogService.insertLoginLogBySeller2(request, seller.getLoginId(), true, "");

			// seller 정보 세팅
			if (seller != null && seller.getSellerId() > 0) {
				Object princial = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				((OpUserDetails) princial).getUser().setObject(seller);
			} else {
				throw new UserException();
			}

			// 세션 타임아웃 시간 세팅
			int timeoutMinute = 60;
			try {
				timeoutMinute = Integer.valueOf(configIsmsService.getIsmsConfigValueByKey("SESSION_TIMEOUT_MANAGER"));
			} catch (NumberFormatException e) {
				log.error(getClass().getName() + " :: loginProcess timeoutMinute error :: ", e);
			}
			UserUtils.getUser().setSessionTimeout(timeoutMinute);

	//
	//		// 8. 결과값
	//		result.setCode("SUCC");
	//
	//		return result;

		} catch (RuntimeException e) {
			log.error("loginProcess error", e);
			Authentication newAuth = new UsernamePasswordAuthenticationToken("anonymousUser", null);
			SecurityContextHolder.getContext().setAuthentication(newAuth);
			throw new UserException("로그인 처리 중 문제가 발생했습니다.");
		}
	}
	/**
	 * PKI 폼 유효성 조회
	 * @param pkiSellerLogin
	 * @return
	 */
	@Override
	public PkiSellerLogin getPkiSellerFormValid(PkiSellerLogin pkiSellerLogin) {
		PkiSellerLogin result = new PkiSellerLogin();
		result.setCode("FAIL");
		result.setErrMsg("잠시후 다시 시도해주세요.");

		PkiSellerLogin userInfo = sellerUserMapper.getPkiSellerFormValid(pkiSellerLogin);

		if(userInfo != null) {
			result.setCode("SUCC");
			if (StringUtils.isEmpty(userInfo.getMberDn())) {
				result.setMberDn("N");
				result.setErrMsg("등록된 공동인증서가 없습니다. 등록 후 로그인 해 주시기 바랍니다.");
			} else {
				result.setMberDn("Y");
			}
			if (StringUtils.isEmpty(userInfo.getMberFinDn())) {
				result.setMberFinDn("N");
				result.setErrMsg("등록된 금융인증서가 없습니다. 등록 후 로그인 해 주시기 바랍니다.");
			} else {
				result.setMberFinDn("Y");
			}
		}

		return result;
	}

	@Override
	public PkiSellerLogin updatePkiManagerMberDn(PkiSellerLogin pkiSellerLogin) {
		PkiSellerLogin result = new PkiSellerLogin();
		if (pkiSellerLogin == null) {
			result.setCode("FAIL");
			result.setErrMsg("인증 정보가 없습니다.");
		} else {
			if (StringUtils.hasLength(pkiSellerLogin.getMberDn()) || StringUtils.hasLength(pkiSellerLogin.getMberFinDn())) {
				String mode = pkiSellerLogin.getMode();

				pkiSellerLogin.setMode(mode);
				int updateResult = sellerUserMapper.updatePkiManagerMberDn(pkiSellerLogin);			// 아이디 조회하여 인증서 정보 등록
				if (updateResult != 1) {
					throw new UserException("입력된 정보가 올바르지 않습니다.");
				}

				result.setCode("SUCC");
			} else {
				result.setCode("FAIL");
				result.setErrMsg("인증 정보가 없습니다.");
			}
		}

		return result;
	}

	@Override
	public List<SellerUser> getSellerUserByMberDn(PkiSellerLogin pkiSellerLogin) {
		return sellerUserMapper.getSellerUserByMberDn(pkiSellerLogin);
	}

	@Override
	public int updateLoginFailCnt(String loginId) {
		return sellerUserMapper.updateLoginFailCnt(loginId);
	}

	@Override
	public int modSellerMberCi(PkiSellerLogin pkiSellerLogin) {
		return sellerUserMapper.modSellerMberCi(pkiSellerLogin);
	}

	@Override
	public int delSellerMberCi() {
		return sellerUserMapper.delSellerMberCi(UserUtils.getUser().getUserId());
	}

	@Override
	public SellerUser getSellerUserByLoginIdForSms(String loginId) throws UserException {
		return sellerUserMapper.getSellerUserByLoginIdForSms(loginId);
	}

	/**
	 * 판매자 LOGIN_ID로 이메일 UPDATE 등록(op_seller_user)
	 * @param Seller seller
	 * @return
	 */
	@Override
	public int updateSellerUserInfo(Seller seller) {
		int isUpdated = sellerUserMapper.updateSellerUserInfo(seller);
		return isUpdated;
	}
}
