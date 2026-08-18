package saleson.shop.user;

import java.util.Collections;

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
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import lombok.RequiredArgsConstructor;
import saleson.common.utils.UserUtils;
import saleson.shop.user.domain.PersonInCharge;
import saleson.shop.user.domain.PersonInChargeResult;
import saleson.shop.user.support.PersonInChargeSearchParam;

@Controller
@RequestMapping("/opmanager/user/oper-charger")
@RequestProperty(title = "운영자관리", layout = "default", template="opmanager")
@RequiredArgsConstructor
public class OperPersonInChargeManagerController {
	private static final Logger log = LoggerFactory.getLogger(OperPersonInChargeManagerController.class);

	/** 담당자 관리 Service */
	@Autowired
	private PersonInChargeService personInChargeService;

	/**
	 * 운영관리자 목록 조회
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String operChargerList(PersonInChargeSearchParam searchParam, Model model) {

		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartCreated(StringUtils.defaultIfEmpty(searchParam.getSrchStartCreated(), today));
		searchParam.setSrchEndCreated(StringUtils.defaultIfEmpty(searchParam.getSrchEndCreated(), today));

		// 로그인 사용자 권한 & ID 저장
		searchParam.setAdminRole(personInChargeService.getLoginUserAdminAuthority());
		searchParam.setLoginUserId(UserUtils.getUser().getUserId());

		// 조회 조건 셋팅
		searchParam.setArrAuthority("ROLE_ADMIN_1,ROLE_ADMIN_2,ROLE_ADMIN_3,ROLE_ADMIN_4".split(","));

		// 운영관리자 목록 갯수 조회
		int chargerCount = 0;

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(chargerCount);
		searchParam.setPagination(pagination);

		model.addAttribute("adminRole"	, searchParam.getAdminRole());
		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, chargerCount);
		model.addAttribute("list"		, Collections.EMPTY_LIST);
		model.addAttribute("searchParam", searchParam);
		return "view:/opmanager/user/oper-charger/list";
	}

	@PostMapping("/list")
	public String operChargerListPost(PersonInChargeSearchParam searchParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartCreated(StringUtils.defaultIfEmpty(searchParam.getSrchStartCreated(), today));
		searchParam.setSrchEndCreated(StringUtils.defaultIfEmpty(searchParam.getSrchEndCreated(), today));

		// 로그인 사용자 권한 & ID 저장
		searchParam.setAdminRole(personInChargeService.getLoginUserAdminAuthority());
		searchParam.setLoginUserId(UserUtils.getUser().getUserId());

		// 조회 조건 셋팅
		searchParam.setArrAuthority("ROLE_ADMIN_1,ROLE_ADMIN_2,ROLE_ADMIN_3,ROLE_ADMIN_4".split(","));

		// 운영관리자 목록 갯수 조회
		int chargerCount = personInChargeService.getChargerCountByParam(searchParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(chargerCount);
		searchParam.setPagination(pagination);

		model.addAttribute("adminRole"	, searchParam.getAdminRole());
		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, chargerCount);
		model.addAttribute("list"		, personInChargeService.getChargerListByParam(searchParam));
		model.addAttribute("searchParam", searchParam);
		return "view:/opmanager/user/oper-charger/list";
	}

	/**
	 * 운영관리자 삭제
	 * @param charger
	 * @return
	 */
	@PostMapping("/delete")
	public JsonView operChargerDelete(PersonInCharge charger) {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");
		result.setIsLogout("N");

		try {
			String adminRole = personInChargeService.getLoginUserAdminAuthority();
			String allowedRole = "ROLE_ADMIN_1,ROLE_ADMIN_3";

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
			log.error("ERROR: {}", "=========== operChargerDelete RuntimeException ============");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 운영관리자 수정
	 * @param model
	 * @param userId
	 * @return
	 */
	@GetMapping("/edit/{userId}")
	public String operChargerEdit(Model model, PersonInChargeSearchParam searchParam
			, @PathVariable("userId") Long userId) {

		// 조회 조건 셋팅
		searchParam.setArrAuthority("ROLE_ADMIN_1,ROLE_ADMIN_2,ROLE_ADMIN_3,ROLE_ADMIN_4".split(","));

		// 운영관리자 상세 조회
		PersonInCharge details = personInChargeService.getChargerDetails(searchParam);

		model.addAttribute("loginUserId"	, UserUtils.getUser().getUserId());
		model.addAttribute("adminRole"		, personInChargeService.getLoginUserAdminAuthority());
		model.addAttribute("details"		, details);
		return "view:/opmanager/user/oper-charger/edit";
	}

	/**
	 * 운영관리자 수정 처리
	 * @param personInCharge
	 * @return
	 */
	@PostMapping("/edit")
	public JsonView operChargerEditProcess(PersonInCharge personInCharge) {
		PersonInChargeResult result = new PersonInChargeResult();
		result.setCode("FAIL");

		try {

			// 행안부 주담당자 수정인데, 기존에 이미 2명이 등록된 경우
			if("ROLE_ADMIN_3".equals(personInCharge.getAuthority())
					&& personInChargeService.getGovMainPersonInChargeCount(personInCharge) >= 2) {
				result.setCode("ERR_MAIN_CNT");

			} else {

				// 운영관리자 수정
				result = personInChargeService.updatePersonInCharge(personInCharge);
			}

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "========== operChargerEditProcess Exception ===========");
		}

		return JsonViewUtils.success(result);
	}
}
