package saleson.shop.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import lombok.RequiredArgsConstructor;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.Const;
import saleson.common.file.ExcelDownloadView;
import saleson.common.userauth.UserAuthService;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.code.support.CodeParam;
import saleson.shop.user.domain.PersonInCharge;
import saleson.shop.user.domain.PersonInChargeResult;
import saleson.shop.user.support.OffPersonInChargeExcelView;
import saleson.shop.user.support.PersonInChargeSearchParam;

@Controller
@RequestMapping("/opmanager/user/off-charger")
@RequestProperty(title = "오프라인 담당자", layout = "default", template="opmanager")
@RequiredArgsConstructor
public class OffPersonInChargeManagerController {
	private static final Logger log = LoggerFactory.getLogger(OffPersonInChargeManagerController.class);

	/** 담당자 관리 Service */
	@Autowired
	private PersonInChargeService personInChargeService;

	/** 공통코드 Service */
	@Autowired
	private CodeService codeService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserAuthService userAuthService;

	/**
	 * 오프라인 담당자 목록 조회
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String offChargerList(PersonInChargeSearchParam searchParam, Model model) {
		String adminRole = personInChargeService.getLoginUserAdminAuthority();

		// 1. 시스템&행안부&오프라인 주담당자만 목록 정보 조회
		if("ROLE_ADMIN_1".equals(adminRole) || "ROLE_ADMIN_2".equals(adminRole)
				|| "ROLE_ADMIN_3".equals(adminRole) || "ROLE_ADMIN_4".equals(adminRole) || "ROLE_ADMIN_7".equals(adminRole)) {

			// 로그인 사용자 권한 & ID 저장
			searchParam.setAdminRole(adminRole);
			searchParam.setLoginUserId(UserUtils.getUser().getUserId());

			// 조회 조건 셋팅
			searchParam.setArrAuthority("ROLE_ADMIN_7,ROLE_ADMIN_8".split(","));

			// 정렬 조건 셋팅
			searchParam.setOrderBy("BANK_NM");

			// 오프라인 담당자관리 목록 갯수 조회
			int chargerCount = 0;

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(chargerCount);
			searchParam.setPagination(pagination);

			model.addAttribute("adminRole"	, searchParam.getAdminRole());
			model.addAttribute("pagination"	, pagination);
			model.addAttribute("count"		, chargerCount);
			model.addAttribute("list"		, Collections.EMPTY_LIST);
			model.addAttribute("searchParam", searchParam);
			return "view:/opmanager/user/off-charger/list";

		// 2. 오프라인 부담당자는 상세 화면 조회
		} else if("ROLE_ADMIN_8".equals(adminRole)) {
			return ViewUtils.redirect("/opmanager/user/off-charger/edit/"+UserUtils.getUser().getUserId());

		} else {
			return ViewUtils.redirect("/opmanager");
		}

	}

	@PostMapping("/list")
	public String offChargerListPost(PersonInChargeSearchParam searchParam, Model model) {
		String adminRole = personInChargeService.getLoginUserAdminAuthority();

		// 1. 시스템&행안부&오프라인 주담당자만 목록 정보 조회
		if("ROLE_ADMIN_1".equals(adminRole) || "ROLE_ADMIN_2".equals(adminRole)
				|| "ROLE_ADMIN_3".equals(adminRole) || "ROLE_ADMIN_4".equals(adminRole) || "ROLE_ADMIN_7".equals(adminRole)) {

			// 로그인 사용자 권한 & ID 저장
			searchParam.setAdminRole(adminRole);
			searchParam.setLoginUserId(UserUtils.getUser().getUserId());

			// 조회 조건 셋팅
			searchParam.setArrAuthority("ROLE_ADMIN_7,ROLE_ADMIN_8".split(","));

			// 정렬 조건 셋팅
			searchParam.setOrderBy("BANK_NM");

			// 오프라인 담당자관리 목록 갯수 조회
			int chargerCount = personInChargeService.getChargerCountByParam(searchParam);

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(chargerCount);
			searchParam.setPagination(pagination);

			model.addAttribute("adminRole"	, searchParam.getAdminRole());
			model.addAttribute("pagination"	, pagination);
			model.addAttribute("count"		, chargerCount);
			model.addAttribute("list"		, personInChargeService.getChargerListByParam(searchParam));
			model.addAttribute("searchParam", searchParam);
			return "view:/opmanager/user/off-charger/list";

		// 2. 오프라인 부담당자는 상세 화면 조회
		} else if("ROLE_ADMIN_8".equals(adminRole)) {
			return ViewUtils.redirect("/opmanager/user/off-charger/edit/"+UserUtils.getUser().getUserId());

		} else {
			return ViewUtils.redirect("/opmanager");
		}
	}

	/**
	 * 오프라인 담당자 목록 > 전체 목록 엑셀 다운로드
	 * @param searchParam
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping("/list/download-excel")
	public ModelAndView downloadExcelProcessByList(PersonInChargeSearchParam searchParam) {
		// 로그인 사용자 권한 & ID 저장
		searchParam.setAdminRole(personInChargeService.getLoginUserAdminAuthority());
		searchParam.setLoginUserId(UserUtils.getUser().getUserId());

		// 조회 조건 셋팅
		searchParam.setArrAuthority("ROLE_ADMIN_7,ROLE_ADMIN_8".split(","));

		// 정렬 조건 셋팅
		searchParam.setOrderBy("BANK_NM");

		// 오프라인 담당자관리 목록 갯수 조회
		int chargerCount = personInChargeService.getChargerCountByParam(searchParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(chargerCount);
		searchParam.setPagination(pagination);

		// 엑셀 다운로드
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		String fileName
			= "오프라인담당자회원목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = personInChargeService.streamChargerData(searchParam, chargerCount);
		} finally {
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}

	/**
	 * 오프라인 담당자 목록 > 상태코드 수정 (사용, 중지)
	 * @param personInCharge
	 * @return
	 */
	@PostMapping("/status/edit")
	public JsonView statusCodeEdit(PersonInCharge personInCharge) {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		try {
			if(personInCharge.getUserIdList() == null || personInCharge.getUserIdList().size() == 0) {
				result.setCode("ERR");

			} else if(personInChargeService.updateStatusCode(personInCharge) > 0) {
				result.setCode("SUCC");
			}
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "============ statusCodeEdit RuntimeException ===========");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 오프라인 담당자 수정
	 * @param model
	 * @param userId
	 * @return
	 */
	@GetMapping("/edit/{userId}")
	public String offChargerEdit(Model model, PersonInChargeSearchParam searchParam
			, @PathVariable("userId") Long userId) {
		String adminRole = personInChargeService.getLoginUserAdminAuthority();

		if("ROLE_ADMIN_8".equals(adminRole)
				&& !String.valueOf(userId).equals(String.valueOf(UserUtils.getUser().getUserId()))) {
			return ViewUtils.redirect("/opmanager/user/off-charger/edit/"+UserUtils.getUser().getUserId());
		}

		// 조회 조건 셋팅
		searchParam.setArrAuthority("ROLE_ADMIN_7,ROLE_ADMIN_8".split(","));

		// 운영관리자 상세 조회
		PersonInCharge details = personInChargeService.getChargerDetails(searchParam);

		// 공통코드 (핸드폰, 전화번호, 은행코드)
		CodeParam codeParam = new CodeParam();
		codeParam.setCodeType("PHONE");
		model.addAttribute("phoneCodeList", codeService.getCodeChildList(codeParam));

		codeParam.setCodeType("TEL");
		model.addAttribute("telCodeList", codeService.getCodeChildList(codeParam));

		if("ROLE_ADMIN_1".equals(adminRole) || "ROLE_ADMIN_2".equals(adminRole)
				|| "ROLE_ADMIN_3".equals(adminRole) || "ROLE_ADMIN_4".equals(adminRole)) {
			codeParam.setCodeType("OFF_BANK_LIST");
			model.addAttribute("bankCodeList", codeService.getCodeChildList(codeParam));
		}

		model.addAttribute("loginUserId"	, UserUtils.getUser().getUserId());
		model.addAttribute("adminRole"		, adminRole);
		model.addAttribute("details"		, details);
		return "view:/opmanager/user/off-charger/edit";
	}

	/**
	 * 오프라인담당자 수정 처리
	 * @param personInCharge
	 * @return
	 */
	@PostMapping("/edit")
	public JsonView offChargerEditProcess(PersonInCharge personInCharge) {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		try {

			// 오프라인 주담당자로 수정
			if("ROLE_ADMIN_7".equals(personInCharge.getAuthority())) {
				// 오프라인 주담당자 인원 조회
				int mCnt = personInChargeService.getOffPersonInChargeMainCount(personInCharge);

				if (SecurityUtils.hasRole("ROLE_ADMIN_7")) { // 수정을 요청한 사람이 오프라인 주관리자인 경우

					if (mCnt > 2) {
						// 2명 초과시 불가
						result.setCode("ERR_MAIN_CNT");
					} else if (mCnt == 2) {
						// 2명인 경우 권한 이관 알림 필요
						if (!personInCharge.getUserId().equals(UserUtils.getUser().getUserId())) {
							result.setCode("NEED_AUTH_SWAP");
						} else {
							// 주관리자가 자신의 정보를 업데이트 함
							result = personInChargeService.updateOffPersonInCharge(personInCharge);
						}
					} else {
						// 주관리자가 자신의 정보를 업데이트 하거나 인원 초과가 안된 경우 정보 수정
						result = personInChargeService.updateOffPersonInCharge(personInCharge);
					}
				} else  {

					// 시스템관리자가 업데이트 하는 경우에는, 강등해야할 주관리자를 지정할수 없으므로 2명인 경우부터 막음
					if (mCnt >= 2) {
						// 2명 초과시 불가
						result.setCode("ERR_MAIN_CNT");
					} else {
						result = personInChargeService.updateOffPersonInCharge(personInCharge);
					}
				}

			} else {
				result = personInChargeService.updateOffPersonInCharge(personInCharge);
			}
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "============ offChargerEditProcess RuntimeException ============");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 오프라인담당자 권한 이관 처리
	 * @param personInCharge
	 * @return
	 */
	@PostMapping("/auth-swap")
	public JsonView offChargerAuthSwapProcess(PersonInCharge personInCharge) {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		try {

			// 오프라인 주담당자로 수정
			if("ROLE_ADMIN_7".equals(personInCharge.getAuthority())) {
				if (!personInCharge.getUserId().equals(UserUtils.getUser().getUserId())) {
					PersonInChargeSearchParam chargeSearchParam = new PersonInChargeSearchParam();
					chargeSearchParam.setArrAuthority("ROLE_ADMIN_7,ROLE_ADMIN_8".split(","));
					chargeSearchParam.setUserId(UserUtils.getUser().getUserId());

					PersonInCharge shouldDowngrade = new PersonInCharge();
					shouldDowngrade = personInChargeService.getChargerDetails(chargeSearchParam);
					shouldDowngrade.setAuthority("ROLE_ADMIN_8"); // 부관리자
					shouldDowngrade.setStatusCode(Long.valueOf("2")); // 중지

					List<String> userIds = new ArrayList<String>();
					userIds.add(String.valueOf(shouldDowngrade.getUserId()));
					shouldDowngrade.setUserIdList(userIds);
					personInChargeService.updateStatusCode(shouldDowngrade);
					personInChargeService.updateOffPersonInCharge(shouldDowngrade);

					result = personInChargeService.updateOffPersonInCharge(personInCharge);
					result.setCode("AUTH_CHANGED_LOGOUT");
				}
			}

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "============ offChargerAuthSwapProcess RuntimeException ============");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 오프라인담당자 삭제
	 * @param charger
	 * @return
	 */
	@PostMapping("/delete")
	public JsonView offChargerDelete(PersonInCharge charger) {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");
		result.setIsLogout("N");

		try {
			String adminRole = personInChargeService.getLoginUserAdminAuthority();
			String allowedRole = "ROLE_ADMIN_1,ROLE_ADMIN_2,ROLE_ADMIN_3,ROLE_ADMIN_4,ROLE_ADMIN_7";

			if(allowedRole.indexOf(adminRole) > -1) {

				// 담당자 삭제
				result = personInChargeService.deleteCharger(charger);

				// 로그아웃 여부
				result.setIsLogout(charger.getUserIdList().contains(String.valueOf(UserUtils.getUser().getUserId())) ? "Y" : "N");

			} else {
				result.setCode("ERR_NOT_ALLOW");
			}
		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "========== offChargerDelete RuntimeException ==========");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 비밀번호 초기화 및 신규 비밀번호 발급
	 * @param charger
	 * @param model
	 * @return
	 */
	@GetMapping("/popup/password-init/{userId}")
	@RequestProperty(title = "비밀번호 초기화 및 신규 비밀번호 발급", layout = "base")
	public String passwordInit(PersonInCharge charger, Model model) {
		model.addAttribute("password", personInChargeService.updatePasswordInit(charger));
		return ViewUtils.getView("/user/popup/charger-password-init");
	}

	/**
	 * 오프라인 담당자 등록
	 * @param model
	 * @param
	 * @return
	 */
	@GetMapping("/create")
	public String offChargerCreate(Model model) {
		String adminRole = personInChargeService.getLoginUserAdminAuthority();


//		// 조회 조건 셋팅
//		searchParam.setArrAuthority("ROLE_ADMIN_7,ROLE_ADMIN_8".split(","));
//
//		// 운영관리자 상세 조회
//		PersonInCharge details = personInChargeService.getChargerDetails(searchParam);

		// 공통코드 (핸드폰, 전화번호, 은행코드)
		CodeParam codeParam = new CodeParam();
		codeParam.setCodeType("PHONE");
		model.addAttribute("phoneCodeList", codeService.getCodeChildList(codeParam));

		codeParam.setCodeType("TEL");
		model.addAttribute("telCodeList", codeService.getCodeChildList(codeParam));

		if("ROLE_ADMIN_1".equals(adminRole) || "ROLE_ADMIN_2".equals(adminRole)
				|| "ROLE_ADMIN_3".equals(adminRole) || "ROLE_ADMIN_4".equals(adminRole) || "ROLE_ADMIN_7".equals(adminRole)) {
			codeParam.setCodeType("OFF_BANK_LIST");
			model.addAttribute("bankCodeList", codeService.getCodeChildList(codeParam));
		}

		model.addAttribute("loginUserId"	, UserUtils.getUser().getUserId());
		model.addAttribute("adminRole"		, adminRole);
//		model.addAttribute("details"		, details);
		return "view:/opmanager/user/off-charger/form";
	}

	/**
	 * 오프라인담당자 등록 처리
	 * @param personInCharge
	 * @return
	 */
	@PostMapping("/createProcess")
	public JsonView offChargerCreateProcess(PersonInCharge personInCharge) {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		try {
			result = personInChargeService.insertOffPersonInCharge(personInCharge);

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "============ offChargerEditProcess RuntimeException ============");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 오프라인담당자 가입을 위한 아이디 중복 확인
	 * @param personInCharge
	 * @return
	 */
	@PostMapping("/getUserInfoByUserId")
	@ResponseBody
	public JsonView getUserInfoByUserId(@RequestParam String loginId) {
		PersonInChargeResult result = new PersonInChargeResult();

		User user = new User();

		if (loginId == null) {
			result.setCode("FAIL");
        }
		try {
            user.setLoginId(loginId);

			// 가입 불가 아이디 체크
            String checkResult = userService.checkDuplication(user);
            int idCnt = 0;
            if ("isOccupiedId".equals(checkResult)) {
            	idCnt = 1;
            }

            if(idCnt == 1) {
            	result.setCode("SUCC");
            }else {
            	result.setCode("FAIL");
            }


		}catch(UserException e){
            log.error("[getUserInfoByUserId] ERROR : {}", e);

		}

		return JsonViewUtils.success(result);
	}
}
