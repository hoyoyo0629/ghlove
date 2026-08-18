package saleson.api.user;

import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dreamsecurity.magice2e.util.Log;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.StringUtils;

import saleson.api.auth.domain.UserInfo;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.user.domain.OnepassUserDomainInfo;
import saleson.api.user.domain.UserByCI;
import saleson.api.user.domain.UserDomainInfo;
import saleson.common.utils.RandomStringUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.user.JoinService;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.PolicyInfo;
import saleson.shop.user.domain.UserDetail;
import saleson.shop.user.domain.UserInfoByCI;
import saleson.shop.user.domain.UserParent;
import saleson.shop.user.support.AuthInfo;

@RestController("ApiJoinController")
@RequestMapping("/api/join")
public class JoinController {

	private static final Logger log = LoggerFactory.getLogger(JoinController.class);

	@Autowired
	private JoinService joinService;

	@Autowired
	private UserService userService;

	@Autowired
	private SequenceService sequenceService;

	/**
	 * 회원가입을 위한 입력폼으로 이동
	 * */
	@PostMapping("/entryForm")
	public ResponseEntity entryForm(@RequestBody LocGovInfo locGovInfo) {
		ResponseEntity result = null;

		joinService.getLocGovList(locGovInfo);

		try {

			result = ApiResponseEntity.data()
					.put("phoneCodes", CodeUtils.getCodeInfoList("PHONE"))
					.put("emailCodes", CodeUtils.getCodeInfoList("EMAIL"))
					.put("locGovList", CodeUtils.getCodeInfoList("WDR"))
					.put("categoryList", joinService.getCategoryList())
					.put("status", HttpStatus.OK).ok();
		}catch(UserException e){
            log.error("[entryForm] ERROR : {}", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 회원가입을 위한 입력폼으로 이동
	 * */
	@PostMapping("/getSubLocGov")
	public ResponseEntity getSubLocGov(@RequestBody LocGovInfo locGovInfo) {
		ResponseEntity result = null;

		try {

			result = ApiResponseEntity.data()
					.put("subLocGovList", joinService.getLocGovList(locGovInfo))
					.put("status", HttpStatus.OK).ok();
		}catch(UserException e){
            log.error("[getSubLocGov] ERROR : {}", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 회원가입을 위한 아이디 중복 확인
	 * */
	@PostMapping("/getUserInfoByUserId")
	public ResponseEntity getUserInfoByUserId(@RequestBody String userID) {
		ResponseEntity result = null;

		User user = new User();

		if (userID == null) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        }

		try {

            user.setLoginId(userID);

			// 가입 불가 아이디 체크
            String checkResult = userService.checkDuplication(user);
            int idCnt = 0;
            if ("isOccupiedId".equals(checkResult)) {
            	idCnt = 1;
            }

			result = ApiResponseEntity.data()
					.put("idCnt", idCnt)
					.put("status", HttpStatus.OK).ok();
		}catch(UserException e){
            log.error("[getUserInfoByUserId] ERROR : {}", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
     * 회원가입 API
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid UserDomainInfo userInfo , BindingResult bindingResult) {
        if (userInfo == null) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        }

        ResponseEntity result = null;
        User user = new User();
        UserDetail userDetail = new UserDetail();

        try {

        	if(bindingResult.hasErrors()
        		|| ObjectUtils.isEmpty(userInfo.getMberCi())
        		|| ObjectUtils.isEmpty(userInfo.getMberDi())
        		|| ObjectUtils.isEmpty(userInfo.getBirthdayFull()) ) {
        		return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        	}

        	if(ObjectUtils.isEmpty(userInfo.getPassword())) {
        		return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        	}

            userModifyDataSet(userInfo, userDetail);

            user.setLoginId(userInfo.getLoginId());
            user.setPassword(userInfo.getPassword());
            user.setUserName(userInfo.getUserName());
            user.setEmail(userInfo.getEmail());
            user.setUserDetail(userDetail);

            // 인증여부 체크
            if (!userInfo.isAuth()) {
                return ApiResponseEntity.error(ApiError.NOT_EXIST_AUTH);
            }

            //기존 ci가입 여부 확인
            AuthInfo authInfo = new AuthInfo();
            authInfo.setMberCi(userInfo.getMberCi());
            UserInfoByCI ciInfo = userService.getUserInfoByCi(authInfo);

            if(ciInfo!=null) {
            	return ApiResponseEntity.error(ApiError.DUPLICATION_CI_JOIN_USER);
            }

            // 가입 불가 아이디 체크
            String checkResult = userService.checkDuplication(user);
            if ("isOccupiedId".equals(checkResult)) {
                return ApiResponseEntity.error(ApiError.DUPLICATION_LOGIN_ID);
            }

//            long userId = sequenceService.getLong("OP_USER");
            long userId = userService.selectNewUserId();
            user.setUserId(userId);
            userDetail.setUserId(userId);


//            userService.insertUserAndUserDetail(user, userDetail);

            // 2022.02.13 일반회원가입 코드 100 추가
            userDetail.setLoginPathCode("100");
            joinService.insertUserAndUserDetail(user, userDetail);

        	//if(userInfo.getParentResponse() != null || !"".equals(userInfo.getParentResponse())) {
        	if(!"".equals(userInfo.getParentResponse())) {
        		ObjectMapper mapper = new ObjectMapper();
        		JSONObject jsonObject = new JSONObject();

        		LinkedHashMap<String, String> parentResponseMap = (LinkedHashMap<String, String>) userInfo.getParentResponse();
        		for (String key : parentResponseMap.keySet()) {
        			if(key== "mberCi") {
        				jsonObject.put("ci", parentResponseMap.get(key));
        			}else if(key== "mberDi") {
        				jsonObject.put("di", parentResponseMap.get(key));
        			}else if(key == "real_name") {
        				jsonObject.put("userName", parentResponseMap.get(key));
        			}else if(key == "fgnGbn") {
        				jsonObject.put("national_info", parentResponseMap.get(key));
        			}else if(key == "gender") {
        				jsonObject.put("gender", parentResponseMap.get(key));
        			}else if(key == "certDate") {
        			}else {
        				jsonObject.put(key, parentResponseMap.get(key));
        			}
        	    }

    	        UserParent userParent  = mapper.readValue(jsonObject.toJSONString(), UserParent.class);
    	        userParent.setUserId(userId);
    	    	userParent.setParentId(sequenceService.getId("OP_USER_PARENT"));
    	        userService.insertUserParent(userParent);
        	}

            result = ApiResponseEntity.data()
            			.put("status", HttpStatus.OK)
            			.put("userId", userId)
            			.ok();
        } catch (JsonProcessingException e) {
        	log.error("[/api/auth/join] ERROR : {}", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 회원가입 API
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/onepassJoin")
    public ResponseEntity onepassJoin(@RequestBody @Valid OnepassUserDomainInfo userInfo , BindingResult bindingResult) {
        if (userInfo == null) {
            return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        }

        ResponseEntity result = null;
        User user = new User();
        UserDetail userDetail = new UserDetail();

        try {

        	if(bindingResult.hasErrors()
        		|| ObjectUtils.isEmpty(userInfo.getMberCi())
        		|| ObjectUtils.isEmpty(userInfo.getMberDi())
        		|| ObjectUtils.isEmpty(userInfo.getBirthdayFull()) ) {
        		return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        	}

        	if(!"Y".equals(userInfo.getIsOnepass())) {
        		return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        	}

    		if(ObjectUtils.isEmpty(userInfo.getUserKey())) {
    			return ApiResponseEntity.error(ApiError.BAD_REQUEST);
    		}

    		//비밀번호 난수
			userInfo.setPassword(RandomStringUtils.getRandomString("", 4, 8));

        	if(ObjectUtils.isEmpty(userInfo.getPassword())) {
        		return ApiResponseEntity.error(ApiError.BAD_REQUEST);
        	}

            userModifyDataSet(userInfo, userDetail);

            //랜덤 아이디 세팅
            user.setLoginId(randomUserId());
            user.setPassword(userInfo.getPassword());
            user.setUserName(userInfo.getUserName());
            user.setEmail(userInfo.getEmail());
            user.setUserDetail(userDetail);

    		if(ObjectUtils.isEmpty(user.getLoginId())) {
    			return ApiResponseEntity.error(ApiError.BAD_REQUEST);
    		}

            // 인증여부 체크
            if (!userInfo.isAuth()) {
                return ApiResponseEntity.error(ApiError.NOT_EXIST_AUTH);
            }

            //기존 ci가입 여부 확인
            AuthInfo authInfo = new AuthInfo();
            authInfo.setMberCi(userInfo.getMberCi());
            UserInfoByCI ciInfo = userService.getUserInfoByCi(authInfo);

            if(ciInfo!=null) {
            	return ApiResponseEntity.error(ApiError.DUPLICATION_CI_JOIN_USER);
            }

            // 가입 불가 아이디 체크
//            String checkResult = userService.checkDuplication(user);
//            if ("isOccupiedId".equals(checkResult)) {
//                return ApiResponseEntity.error(ApiError.DUPLICATION_LOGIN_ID);
//            }

//            long userId = sequenceService.getLong("OP_USER");
            long userId = userService.selectNewUserId();
            user.setUserId(userId);
            userDetail.setUserId(userId);

//          userService.insertUserAndUserDetail(user, userDetail);

            // 2023.02.13 원패스 가입 시 loginPathCode 추가
            userDetail.setLoginPathCode("300");
            joinService.insertUserAndUserDetail(user, userDetail);

            result = ApiResponseEntity.data()
            			.put("status", HttpStatus.OK)
            			.put("userId", userId)
            			.ok();
        } catch (UserException e) {
        	log.error("[/api/auth/join] ERROR : {}", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    @PostMapping("/getPolicyInfo")
	public ResponseEntity getPolicyInfo(@RequestBody PolicyInfo policyInfo) {
		ResponseEntity result = null;

		try {

			//약관
			PolicyInfo policy = joinService.getPolicyInfo(PolicyInfo.POLICY_TYPE_AGREEMENT_POLICY);

			//개인정보처리방침
			PolicyInfo privacy = joinService.getPolicyInfo(PolicyInfo.POLICY_TYPE_PRIVACY_POLICY);

			//개인정보수집 제3자 이용 동의
			PolicyInfo otherAgree = joinService.getPolicyInfo(PolicyInfo.POLICY_TYPE_OTHER_POLICY);

			//개인정보 수집·이용 동의
			PolicyInfo collectionAgree = joinService.getPolicyInfo(PolicyInfo.POLICY_TYPE_COLLECTION_POLICY);

			result = ApiResponseEntity.data()
					.put("policy", policy)
					.put("privacy", privacy)
					.put("collectionAgree", collectionAgree)
					.put("otherAgree", otherAgree)
					.put("status", HttpStatus.OK).ok();
		}catch(UserException e){
            log.error("[getPolicyInfo] ERROR : {}", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

    private void userModifyDataSet(UserDomainInfo userInfo, UserDetail userDetail) {
        userDetail.setUserId(UserUtils.getUserId());
        userDetail.setTelNumber(userInfo.getTelNumber());

        if (!ObjectUtils.isEmpty(userInfo.getPhoneNumber())) {
            userDetail.setPhoneNumber(ShopUtils.phoneNumberPattern(userInfo.getPhoneNumber()));
        }

        userDetail.setPost(userInfo.getPost());

        if (!ObjectUtils.isEmpty(userInfo.getNewPost())) {
            userDetail.setNewPost(userInfo.getNewPost());
        }

        if (!ObjectUtils.isEmpty(userInfo.getAddress())) {
            userDetail.setAddress(userInfo.getAddress());
        }

        if (!ObjectUtils.isEmpty(userInfo.getAddressDetail())) {
            userDetail.setAddressDetail(userInfo.getAddressDetail());
        }

//        if (!ObjectUtils.isEmpty(userInfo.getBirthdayYear())){
//            userDetail.setBirthdayDay(userInfo.getBirthdayDay());
//        }

        if (!ObjectUtils.isEmpty(userInfo.getBirthdayType())) {
            userDetail.setBirthdayType(userInfo.getBirthdayType());
        }

        if (!ObjectUtils.isEmpty(userInfo.getGender())) {
            userDetail.setGender(userInfo.getGender());
        }

        if (!ObjectUtils.isEmpty(userInfo.getReceiveEmail())) {
            userDetail.setReceiveEmail(userInfo.getReceiveEmail());
        }
        if (!ObjectUtils.isEmpty(userInfo.getReceiveSms())) {
            userDetail.setReceiveSms(userInfo.getReceiveSms());
        }

        if (!ObjectUtils.isEmpty(userInfo.getReceivePbanc())) {
            userDetail.setReceivePbanc(userInfo.getReceivePbanc());
        }

        /*20260120 추가*/
        if (!ObjectUtils.isEmpty(userInfo.getReceiveKakao())) {
            userDetail.setReceiveKakao(userInfo.getReceiveKakao());
        }
        if(!ObjectUtils.isEmpty(userInfo.getLocGovList())) {
        	userDetail.setLocGovList(userInfo.getLocGovList());
        }

        if(!ObjectUtils.isEmpty(userInfo.getRtnpsntList())) {
        	userDetail.setRtnpsntList(userInfo.getRtnpsntList());
        }

        if(!ObjectUtils.isEmpty(userInfo.getMberCi())) {
        	userDetail.setMberCi(userInfo.getMberCi());
        }

        if(!ObjectUtils.isEmpty(userInfo.getMberDi())) {
        	userDetail.setMberDi(userInfo.getMberDi());
        }

        if(!ObjectUtils.isEmpty(userInfo.getMberDn())) {
        	userDetail.setMberDn(userInfo.getMberDn());
        }

        if(!ObjectUtils.isEmpty(userInfo.getMberFinDn())) {
        	userDetail.setMberFinDn(userInfo.getMberFinDn());
        }

        if(!ObjectUtils.isEmpty(userInfo.getLocgovCode())) {
        	userDetail.setLocgovCode(userInfo.getLocgovCode());
        }

//        if(!ObjectUtils.isEmpty(userInfo.getBirthday())) {
//        	userDetail.setBirthday(userInfo.getBirthday());
//        }

        if(!ObjectUtils.isEmpty(userInfo.getBirthdayFull())) {
        	userDetail.setBirthdayFull(userInfo.getBirthdayFull());
        }

        if(!ObjectUtils.isEmpty(userInfo.getUserKey())) {
        	userDetail.setUserKey(userInfo.getUserKey());
        }
    }

    private void userModifyDataSet(OnepassUserDomainInfo userInfo, UserDetail userDetail) {
        userDetail.setUserId(UserUtils.getUserId());
        userDetail.setTelNumber(userInfo.getTelNumber());

        if (!ObjectUtils.isEmpty(userInfo.getPhoneNumber())) {
            userDetail.setPhoneNumber(ShopUtils.phoneNumberPattern(userInfo.getPhoneNumber()));
        }

        userDetail.setPost(userInfo.getPost());

        if (!ObjectUtils.isEmpty(userInfo.getNewPost())) {
            userDetail.setNewPost(userInfo.getNewPost());
        }

        if (!ObjectUtils.isEmpty(userInfo.getAddress())) {
            userDetail.setAddress(userInfo.getAddress());
        }

        if (!ObjectUtils.isEmpty(userInfo.getAddressDetail())) {
            userDetail.setAddressDetail(userInfo.getAddressDetail());
        }

//        if (!ObjectUtils.isEmpty(userInfo.getBirthdayYear())){
//            userDetail.setBirthdayDay(userInfo.getBirthdayDay());
//        }

        if (!ObjectUtils.isEmpty(userInfo.getBirthdayType())) {
            userDetail.setBirthdayType(userInfo.getBirthdayType());
        }

        if (!ObjectUtils.isEmpty(userInfo.getGender())) {
            userDetail.setGender(userInfo.getGender());
        }

        if (!ObjectUtils.isEmpty(userInfo.getReceiveEmail())) {
            userDetail.setReceiveEmail(userInfo.getReceiveEmail());
        }

        if (!ObjectUtils.isEmpty(userInfo.getReceiveSms())) {
            userDetail.setReceiveSms(userInfo.getReceiveSms());
        }

        if (!ObjectUtils.isEmpty(userInfo.getReceivePbanc())) {
            userDetail.setReceivePbanc(userInfo.getReceivePbanc());
        }

//        if (!ObjectUtils.isEmpty(userInfo.getReceivePush())) {
//            userDetail.setReceivePush(userInfo.getReceivePush());
//        }

        if(!ObjectUtils.isEmpty(userInfo.getLocGovList())) {
        	userDetail.setLocGovList(userInfo.getLocGovList());
        }

        if(!ObjectUtils.isEmpty(userInfo.getRtnpsntList())) {
        	userDetail.setRtnpsntList(userInfo.getRtnpsntList());
        }

        if(!ObjectUtils.isEmpty(userInfo.getMberCi())) {
        	userDetail.setMberCi(userInfo.getMberCi());
        }

        if(!ObjectUtils.isEmpty(userInfo.getMberDi())) {
        	userDetail.setMberDi(userInfo.getMberDi());
        }

        if(!ObjectUtils.isEmpty(userInfo.getLocgovCode())) {
        	userDetail.setLocgovCode(userInfo.getLocgovCode());
        }

//        if(!ObjectUtils.isEmpty(userInfo.getBirthday())) {
//        	userDetail.setBirthday(userInfo.getBirthday());
//        }

        if(!ObjectUtils.isEmpty(userInfo.getBirthdayFull())) {
        	userDetail.setBirthdayFull(userInfo.getBirthdayFull());
        }

        if(!ObjectUtils.isEmpty(userInfo.getUserKey())) {
        	userDetail.setUserKey(userInfo.getUserKey());
        }
    }

    /**
     * 일반 회원가입 휴대폰 인증 정보 확인
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/checkMobileAuth")
    public ResponseEntity checkMobileAuth(@RequestBody AuthInfo authInfo) {
        ResponseEntity result = null;

        try {
    		UserInfoByCI userInfo = userService.getUserInfoByCi(authInfo);

    		if(!authInfo.isUnderAge()) { //front isUnderAge : true 시 보호자 인증으로 넘기, false면 일반 회원으로 회원체크
    			if(userInfo!=null) { // 이미 회원이면 튕겨냄
        			return ApiResponseEntity.error(ApiError.DUPLICATION_CI_JOIN_USER);
        		}
    		}

            result = ApiResponseEntity.data()
            		.put("status", HttpStatus.OK).ok();
        } catch (UserException e) {
            log.error("[/api/join/checkMobileAuth] ERROR : {}", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 원패스 회원 가입 휴대폰 인증 체크
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/checkOnepassMobileAuth")
    public ResponseEntity checkOnepassMobileAuth(@RequestBody AuthInfo authInfo) {
        ResponseEntity result = null;

        try {

        	UserInfoByCI userInfo = userService.getUserInfoByCi(authInfo);

        	if(!authInfo.isUnderAge()) { //front isUnderAge : true 시 보호자 인증으로 넘기, false면 일반 회원으로 회원체크
    			if(userInfo!=null) { // 이미 회원이면 튕겨냄
        			return ApiResponseEntity.error(ApiError.DUPLICATION_CI_JOIN_USER);
        		}
    		}

            result = ApiResponseEntity.data()
            		.put("status", HttpStatus.OK).ok();
        } catch (UserException e) {
            log.error("[/api/join/checkOnepassMobileAuth] ERROR : {}", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    /**
     * 원패스 회원 가입 아이디 랜덤 생성 테스트
     *
     * @param userInfo
     * @return
     */
    @PostMapping("/testRandomId")
    public ResponseEntity testRandomId(@RequestBody AuthInfo authInfo) {
        ResponseEntity result = null;
        String ramdomId= "";
        try {

        	ramdomId = randomUserId();

            result = ApiResponseEntity.data()
            		.put("ramdomId", ramdomId)
            		.put("status", HttpStatus.OK).ok();
        } catch (UserException e) {
            log.error("[/api/join/checkOnepassMobileAuth] ERROR : {}", e);
            result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
        }

        return result;
    }

    private String randomUserId() {
    	String ramdomId = "";
        int count = 0;
        while(count ==0) {
//      	  ramdomId = randomId();
        	SecureRandom random = new SecureRandom();
        	StringBuffer temp = new StringBuffer();
        	temp.append((char) ((int) (random.nextInt(26)) + 97));
        	ramdomId = RandomStringUtils.getRandomString(temp.toString(), 3, 7).toLowerCase();
            User user = new User();
            user.setLoginId(ramdomId);
            String checkResult = userService.checkDuplication(user);

           if (!"isOccupiedId".equals(checkResult)) {
        	  count = 1;
           }
        }
		return ramdomId;
    }

    private String randomId() {
    	StringBuffer temp = new StringBuffer();
		SecureRandom rnd = new SecureRandom();

		for(int i=0; i<8; i++) {
			if(i==0) {
				temp.append((char) ((int) (rnd.nextInt(26)) + 97));
				continue;
			}
			int rndIdx = rnd.nextInt(2);
			switch(rndIdx) {
				case 0:
					temp.append((char) ((int) (rnd.nextInt(26)) + 97));
				break;
				case 1:
					temp.append(rnd.nextInt(10));
				break;
			}
		}
		return temp.toString();
    }
}
