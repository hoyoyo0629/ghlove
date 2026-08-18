package saleson.shop.user;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import lombok.RequiredArgsConstructor;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.user.domain.PersonInCharge;
import saleson.shop.user.domain.PersonInChargeResult;
import saleson.shop.user.support.PersonInChargeSearchParam;

@Controller
@RequestMapping("/opmanager/user/locgov-charger")
@RequestProperty(title = "지자체 담당자 관리", layout = "default", template="opmanager")
@RequiredArgsConstructor
public class LocgovPersonInChargeManagerController {
	private static final Logger log = LoggerFactory.getLogger(LocgovPersonInChargeManagerController.class);

	/** 담당자 관리 Service */
	@Autowired
	private PersonInChargeService personInChargeService;

	/** 지자체관리 Service */
	@Autowired
	private LocgovService locgovService;

	/** 공통코드 Service */
	@Autowired
	private CodeService codeService;

	/**
	 * 지자체 담당자 관리 목록 조회
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String locgovChargerList(PersonInChargeSearchParam searchParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartCreated(StringUtils.defaultIfEmpty(searchParam.getSrchStartCreated(), today));
		searchParam.setSrchEndCreated(StringUtils.defaultIfEmpty(searchParam.getSrchEndCreated(), today));

		// 로그인 사용자 권한 & ID 저장
		searchParam.setAdminRole(personInChargeService.getLoginUserAdminAuthority());
		searchParam.setLoginUserId(UserUtils.getUser().getUserId());

		// 조회 조건 셋팅
		searchParam.setArrAuthority("ROLE_ADMIN_5,ROLE_ADMIN_6".split(","));

		// 지자체 담당자 관리 목록 갯수 조회
		int chargerCount = 0;

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(chargerCount);
		searchParam.setPagination(pagination);

		model.addAttribute("adminRole"	, searchParam.getAdminRole());
		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, chargerCount);
		model.addAttribute("list"		, Collections.EMPTY_LIST);
		model.addAttribute("searchParam", searchParam);
		return "view:/opmanager/user/locgov-charger/list";
	}

	@PostMapping("/list")
	public String locgovChargerListPost(PersonInChargeSearchParam searchParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartCreated(StringUtils.defaultIfEmpty(searchParam.getSrchStartCreated(), today));
		searchParam.setSrchEndCreated(StringUtils.defaultIfEmpty(searchParam.getSrchEndCreated(), today));

		// 로그인 사용자 권한 & ID 저장
		searchParam.setAdminRole(personInChargeService.getLoginUserAdminAuthority());
		searchParam.setLoginUserId(UserUtils.getUser().getUserId());

		// 조회 조건 셋팅
		searchParam.setArrAuthority("ROLE_ADMIN_5,ROLE_ADMIN_6".split(","));

		// 지자체 담당자 관리 목록 갯수 조회
		int chargerCount = personInChargeService.getLocgovChargerCountByParam(searchParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(chargerCount);
		searchParam.setPagination(pagination);

		model.addAttribute("adminRole"	, searchParam.getAdminRole());
		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, chargerCount);
		model.addAttribute("list"		, personInChargeService.getLocgovChargerListByParam(searchParam));
		model.addAttribute("searchParam", searchParam);
		return "view:/opmanager/user/locgov-charger/list";
	}

	/**
	 * 지자체 담당자 삭제
	 * @param charger
	 * @return
	 */
	@PostMapping("/delete")
	public JsonView locgovChargerDelete(PersonInCharge charger) {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");
		result.setIsLogout("N");

		try {
			String adminRole = personInChargeService.getLoginUserAdminAuthority();
			String allowedRole = "ROLE_ADMIN_1,ROLE_ADMIN_2,ROLE_ADMIN_3,ROLE_ADMIN_4,ROLE_ADMIN_5";

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
			log.error("ERROR: {}", "============ locgovChargerDelete RuntimeException ============");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 지자체 담당자 수정
	 * @param model
	 * @param userId
	 * @return
	 */
	@GetMapping("/edit/{userId}")
	public String locgovChargerEdit(Model model, PersonInChargeSearchParam searchParam
			, @PathVariable("userId") Long userId) {

		// 조회 조건 셋팅
		searchParam.setArrAuthority("ROLE_ADMIN_5,ROLE_ADMIN_6".split(","));

		// 지자체 담당자 상세 조회
		PersonInCharge details = personInChargeService.getChargerDetails(searchParam);

		// 지자체 담당자가 로그인 한 경우 본인 지자체 담당자만 조회 가능
		String adminRole = personInChargeService.getLoginUserAdminAuthority();

		if("ROLE_ADMIN_5".equals(adminRole) || "ROLE_ADMIN_6".equals(adminRole)) {
			Code locgovCodeDetails = locgovService.getLoginUserLocgovCodeDetails(UserUtils.getUser().getUserId());
			if(locgovCodeDetails != null && locgovCodeDetails.getId() != null && details != null && details.getLocgovCode() != null
					&& !locgovCodeDetails.getId().equals(details.getLocgovCode()) || details == null) {
				return ViewUtils.redirect("/opmanager/user/locgov-charger/list");
			}
		};

		// 상위 지자체 코드 목록 조회
		CodeParam codeParam = new CodeParam();
		codeParam.setCodeType("WDR");
		List<Code> upperLocgovCodeList = codeService.getCodeChildList(codeParam);

		model.addAttribute("loginUserId"		, UserUtils.getUser().getUserId());
		model.addAttribute("adminRole"			, adminRole);
		model.addAttribute("details"			, details);
		model.addAttribute("upperLocgovCodeList", upperLocgovCodeList);
		return "view:/opmanager/user/locgov-charger/edit";
	}

	/**
	 * 하위 지자체 정보 조회
	 * @param upperLocgovCode
	 * @return
	 */
	@GetMapping("/locgov/{upperLocgovCode}/list")
	public JsonView locgovList(@PathVariable String upperLocgovCode) {
		return JsonViewUtils.success(personInChargeService.getLocgovList(upperLocgovCode));
	}

	/**
	 * 지자체 담당자 수정 처리
	 * @param personInCharge
	 * @return
	 */
	@PostMapping("/edit")
	public JsonView locgovChargerEditProcess(PersonInCharge personInCharge) {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		try {

			// 지자체 주관리자 수정인데, 기존에 이미 2명이 등록된 경우
			if("ROLE_ADMIN_5".equals(personInCharge.getAuthority())
					&& personInChargeService.getLocgovMainPersonInChargeCount(personInCharge) >= 2) {
				result.setCode("ERR_MAIN_CNT");

			} else {

				// [GGSR-26-366] 주관리자 + 중지인 경우 부관리자로 변경
				if ("ROLE_ADMIN_5".equals(personInCharge.getAuthority())
						&& personInCharge.getStatusCode() == 2) {
					personInCharge.setAuthority("ROLE_ADMIN_6");
				}
				// 지자체 담당자 수정
				result = personInChargeService.updatePersonInCharge(personInCharge);
			}

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "============= locgovChargerEditProcess RuntimeException ==============");
		}

		return JsonViewUtils.success(result);
	}
}
