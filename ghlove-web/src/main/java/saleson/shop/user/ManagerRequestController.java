package saleson.shop.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequest;
import saleson.shop.offgive.OffgiveService;
import saleson.shop.welfarecenter.support.WlfrCntrMngParam;
import saleson.shop.user.domain.ManagerRequest;
import saleson.shop.user.domain.ManagerRequestResult;
import saleson.shop.user.domain.UserInfoBySign;
import saleson.shop.user.support.ManagerRequestSearchParam;
import saleson.shop.user.support.SignInfo;
import saleson.shop.welfarecenter.WelfareCenterService;

@Controller
@RequestProperty(title = "관리자 권한 요청", layout = "base")
@RequiredArgsConstructor
public class ManagerRequestController {
	private static final Logger log = LoggerFactory.getLogger(ManagerRequestController.class);

	/** 관리자 신청 Service */
	@Autowired
	private ManagerRequestService managerRequestService;

	/** 공통코드 Service */
	@Autowired
	private CodeService codeService;

	/** 패스워드 Service */
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	private WelfareCenterService welfareCenterService;

	/**
	 * 관리자 권한 요청 (비로그인)
	 * 참고사항 : OpmanagerHandlerInterceptor.java > isManagerPage 에 해당 URL 추가
	 * @param model
	 * @param loginId
	 * @param password
	 * @return
	 */
	@PostMapping("/opmanager/manager-request/form")
	public String managerRequestForm(Model model
			, @RequestParam(value = "loginId", required = false) String loginId
			, @RequestParam(value = "password", required = false) String password) {

		// 0. 비밀번호 암호화
		try {
			password = pCrypto.Encrypt("hash.5", password, "");
		} catch (UnsupportedEncodingException e) {
			log.error("ERROR: {}", getClass().getName() + " :: managerRequestForm UnsupportedEncodingException ===========");
		}

		// 1. 신청자 정보 조회
		ManagerRequest user = managerRequestService.getUserInfoByLoginId(loginId);
		if (ValidationUtils.isNull(user) || !passwordEncoder.matches(CommonUtils.dataNvl(password), user.getPassword())) {
			return ViewUtils.redirect("/opmanager/login", "사용자 정보를 다시 확인해주세요.");
		}

		// 2. 신청자 정보
		model.addAttribute("userId"		, user.getUserId());			// 회원 ID
		model.addAttribute("loginId"	, user.getLoginId());			// 로그인 ID
		model.addAttribute("userName"	, user.getUserName());			// 이름
		model.addAttribute("phoneNumber", user.getPhoneNumber());		// 휴대폰번호
		model.addAttribute("email"		, user.getEmail());				// 이메일
		model.addAttribute("birthday"	, user.getBirthday());			// 생년월일

		// 3. 공통코드 조회 (REQST_SE_CODE : 소속구분, WDR : 광역지자체, OFF_BANK_LIST : 은행)
		CodeParam codeParam = new CodeParam();
		codeParam.setCodeType("REQST_SE_CODE");
		model.addAttribute("reqstSeCodeList", codeService.getCodeChildList(codeParam));

//		WlfrCntrMngParam wlfrCntrMngParam = new WlfrCntrMngParam();
//		model.addAttribute("wlfrCntrMngList", welfareCenterService.selectWlfrCntrMngList(wlfrCntrMngParam));

		codeParam.setCodeType("WDR");
		model.addAttribute("upperLocgovCodeList", codeService.getCodeChildList(codeParam));

		codeParam.setCodeType("OFF_BANK_LIST");
		model.addAttribute("bankCodeList", codeService.getCodeChildList(codeParam));

		// 공통코드 (핸드폰, 전화번호, 은행코드)
		codeParam.setCodeType("PHONE");
		model.addAttribute("phoneCodeList", codeService.getCodeChildList(codeParam));

		codeParam.setCodeType("TEL");
		model.addAttribute("telCodeList", codeService.getCodeChildList(codeParam));

		return ViewUtils.getView("/user/manager-request/form");
	}

	@PostMapping("/opmanager/manager-request/pkiForm")
	public String managerRequestPkiForm(Model model, @RequestParam(value = "mberDn", required = false) String mberDn) {

		SignInfo signInfo = new SignInfo();
		signInfo.setMberDn(mberDn);

		UserInfoBySign checkSign = userService.checkSignForUser(signInfo);

		// 1. 신청자 정보 조회
		if (ValidationUtils.isNull(checkSign) || StringUtils.defaultIfEmpty(checkSign.getLoginId(), "").equals("") ) {
			return ViewUtils.redirect("/opmanager/login", "사용자 정보를 다시 확인해주세요. ");
		}

		// 1. 신청자 정보 조회
		ManagerRequest user = managerRequestService.getUserInfoByLoginId(checkSign.getLoginId());

		// 2. 신청자 정보
		model.addAttribute("userId"		, user.getUserId());			// 회원 ID
		model.addAttribute("loginId"	, user.getLoginId());			// 로그인 ID
		model.addAttribute("userName"	, user.getUserName());			// 이름
		model.addAttribute("phoneNumber", user.getPhoneNumber());		// 휴대폰번호
		model.addAttribute("email"		, user.getEmail());				// 이메일
		model.addAttribute("birthday"	, user.getBirthday());			// 생년월일

		// 3. 공통코드 조회 (REQST_SE_CODE : 소속구분, WDR : 광역지자체, OFF_BANK_LIST : 은행)
		CodeParam codeParam = new CodeParam();
		codeParam.setCodeType("REQST_SE_CODE");
		model.addAttribute("reqstSeCodeList", codeService.getCodeChildList(codeParam));

		codeParam.setCodeType("WDR");
		model.addAttribute("upperLocgovCodeList", codeService.getCodeChildList(codeParam));

		codeParam.setCodeType("OFF_BANK_LIST");
		model.addAttribute("bankCodeList", codeService.getCodeChildList(codeParam));

		return ViewUtils.getView("/user/manager-request/form");
	}

	@PostMapping("/opmanager/manager-request/pkiFormFin")
	public String managerRequestPkiFormFin(Model model, @RequestParam(value = "mberFinDn", required = false) String mberFinDn) {

		SignInfo signInfo = new SignInfo();
		signInfo.setMberFinDn(mberFinDn);

		UserInfoBySign checkSign = userService.checkSignForUser(signInfo);

		// 1. 신청자 정보 조회
		if (ValidationUtils.isNull(checkSign) || StringUtils.defaultIfEmpty(checkSign.getLoginId(), "").equals("") ) {
			return ViewUtils.redirect("/opmanager/login", "사용자 정보를 다시 확인해주세요. ");
		}

		// 1. 신청자 정보 조회
		ManagerRequest user = managerRequestService.getUserInfoByLoginId(checkSign.getLoginId());

		// 2. 신청자 정보
		model.addAttribute("userId"		, user.getUserId());			// 회원 ID
		model.addAttribute("loginId"	, user.getLoginId());			// 로그인 ID
		model.addAttribute("userName"	, user.getUserName());			// 이름
		model.addAttribute("phoneNumber", user.getPhoneNumber());		// 휴대폰번호
		model.addAttribute("email"		, user.getEmail());				// 이메일
		model.addAttribute("birthday"	, user.getBirthday());			// 생년월일

		// 3. 공통코드 조회 (REQST_SE_CODE : 소속구분, WDR : 광역지자체, OFF_BANK_LIST : 은행)
		CodeParam codeParam = new CodeParam();
		codeParam.setCodeType("REQST_SE_CODE");
		model.addAttribute("reqstSeCodeList", codeService.getCodeChildList(codeParam));

		codeParam.setCodeType("WDR");
		model.addAttribute("upperLocgovCodeList", codeService.getCodeChildList(codeParam));

		codeParam.setCodeType("OFF_BANK_LIST");
		model.addAttribute("bankCodeList", codeService.getCodeChildList(codeParam));

		return ViewUtils.getView("/user/manager-request/form");
	}

	/**
	 * 관리자 권한 요청 > 지자체 조회 (비로그인)
	 * @param model
	 * @param codeType
	 * @return
	 */
	@PostMapping("/opmanager/manager-request/code-child/list")
	public JsonView locgovCodeList(Model model, @RequestParam("codeType") String codeType) {

		// 소속 지자체 코드 조회
		CodeParam codeParam = new CodeParam();
		codeParam.setCodeType(codeType);
		List<Code> list = codeService.getCodeChildList(codeParam);

		return JsonViewUtils.success(list);
	}

	/**
	 * 관리자 권한 요청 > 관리자신청 등록 처리 (비로그인)
	 * @param managerRequest
	 * @return
	 */
	@PostMapping("/opmanager/manager-request/create")
//	public JsonView managerRequestCreateProcess(ManagerRequest managerRequest) {
	public JsonView managerRequestCreateProcess(DsgnCntrManagerRequest managerRequest) {
		int nResult = 0;

		try {
			nResult = managerRequestService.insertManagerRequest(managerRequest);
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "============ managerRequestCreateProcess RuntimeException =============");
		}

		return JsonViewUtils.success(nResult > 0 ? "SUCC" : "FAIL");
	}

	/**
	 * 관리자 권한 승인관리 목록 조회
	 * @param model
	 * @param userId
	 * @return
	 */
	@GetMapping("/opmanager/manager-request/list")
	@RequestProperty(title = "관리자권한요청", layout = "default", template="opmanager")
	public String managerRequestList(ManagerRequestSearchParam searchParam, Model model) {

		// 관리자 권한 승인관리 목록 총 갯수 조회
		int managerRequestCount = managerRequestService.getManagerRequestCountByParam(searchParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(managerRequestCount);
		searchParam.setPagination(pagination);

		model.addAttribute("pagination"		, pagination);
		model.addAttribute("searchParam"	, searchParam);
		model.addAttribute("count"			, managerRequestCount);
		model.addAttribute("list"			, managerRequestService.getManagerRequestListByParam(searchParam));

		return "view:/user/manager-request/list";
	}

	@PostMapping("/opmanager/manager-request/list")
	@RequestProperty(title = "관리자권한요청", layout = "default", template="opmanager")
	public String managerRequestListPost(ManagerRequestSearchParam searchParam, Model model) {
		return managerRequestList(searchParam, model);
	}

	/**
	 * 관리자 권한 승인관리 상세 정보 조회 (팝업창)
	 * @param model
	 * @param userId
	 * @return
	 */
	@GetMapping("/opmanager/manager-request/popup/details/{userId}/{reqstSn}")
	public String managerRequestDetails(Model model,
			@PathVariable("userId") Long userId,
			@PathVariable("reqstSn") Integer reqstSn) {

		// Path 값 검색조건 추가
		ManagerRequestSearchParam searchParam = new ManagerRequestSearchParam();
		searchParam.setUserId(userId);
		searchParam.setReqstSn(reqstSn);

		// 권한 없는 접근일 경우 상세 정보 비노출
		if(managerRequestService.getManagerRequestDetailsAuthCount(searchParam) > 0) {
			model.addAttribute("details", managerRequestService.getManagerRequestDetails(searchParam));
		}

		return ViewUtils.getView("/user/popup/manager-request-details");
	}

	/**
	 * 관리자 권한 승인관리 확인 - 승인/거절
	 * @param managerRequest
	 * @return
	 */
	@PostMapping("/opmanager/manager-request/confirm")
	public JsonView managerRequestConfirmProcess(ManagerRequest managerRequest) {

		// 결과값
		ManagerRequestResult result = new ManagerRequestResult();
		result.setCode("ERR");

		try {
			// 수정자 ID 셋팅
			managerRequest.setLastUpdusrId(UserUtils.getUser().getUserId());

			// 관리자 권한 승인관리 - '거절' 프로세스
			if("300".equals(managerRequest.getConfmSttusCode())) {
				result = managerRequestService.updateManagerRequestReject(managerRequest);

			// 관리자 권한 승인관리 - '승인' 프로세스
			} else if("100".equals(managerRequest.getConfmSttusCode())) {
				result = managerRequestService.updateManagerRequestApproval(managerRequest);
			}

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "=========== managerRequestConfirmProcess RuntimeException =============");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 관리자 권한 승인관리 상세 정보 조회 (팝업창)
	 * @param model
	 * @param userId
	 * @return
	 */
	@GetMapping("/opmanager/manager-request/popup/history/{userId}")
	public String managerRequestHistory(Model model, @PathVariable long userId) {

		// Path 값 검색조건 추가
		ManagerRequestSearchParam searchParam = new ManagerRequestSearchParam();
		searchParam.setUserId(userId);

		model.addAttribute("list", managerRequestService.getManagerRequestHistory(searchParam));

		return ViewUtils.getView("/user/popup/manager-request-history");
	}
}
