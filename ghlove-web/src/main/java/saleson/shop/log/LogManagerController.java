package saleson.shop.log;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dreamsecurity.magice2e.util.Log;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.SearchParam;
import com.onlinepowers.framework.web.opmanager.role.RoleService;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.common.utils.CommonUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.UserUtils;
import saleson.seller.main.domain.Seller;
import saleson.shop.donation.NextBugaRequestLogDto;
import saleson.shop.donation.NextBugaResponseLogDto;
import saleson.shop.donation.NextSunapRequestLogDto;
import saleson.shop.donation.NextSunapResponseLogDto;
import saleson.shop.donation.NgDonationService;
import saleson.shop.log.support.ActionLogParam;
import saleson.shop.log.support.GifSeoulParam;
import saleson.shop.log.support.LoginLogParam;
import saleson.shop.log.support.PrivacyLogHistParam;
import saleson.shop.log.support.PrivacyLogParam;
import saleson.shop.log.support.RelayLogParam;

@Controller
@RequestMapping("/opmanager/log")
@RequestProperty(title="로그관리", layout="default")
public class LogManagerController {

	private static final Logger logger = LoggerFactory.getLogger(LogManagerController.class);

	@Autowired
	private LoginLogService loginLogService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private ActionLogService actionLogService;

	@Autowired
	private ExceldownloadLogService exceldownloadLogService;

	@Autowired
	private PrivacyAccessLogHistService privacyAccessLogHistService;

	@Autowired
	private RelayLogService relayLogService;

	@Autowired
	private NgDonationService ngDonationService;

	/**
	 * 로그인 로그 관리
	 * @param model
	 * @param loginLogParam
	 * @return
	 */
	@GetMapping("/login-log")
	@RequestProperty(title="로그인 로그 관리")
	public String loginLog(Model model, LoginLogParam loginLogParam) {
		String today = DateUtils.getToday("yyyyMMdd");
		loginLogParam.setSrchStartLoginDate(StringUtils.defaultIfEmpty(loginLogParam.getSrchStartLoginDate(), today));
		loginLogParam.setSrchEndLoginDate(StringUtils.defaultIfEmpty(loginLogParam.getSrchEndLoginDate(), today));

		// 기본 목록수 (row: 20)
		if(CommonUtils.intNvl(loginLogParam.getItemsPerPageTemp()) == 0) {
			loginLogParam.setItemsPerPage(20);
		}

		// 로그인 로그 목록 갯수 조회
		int logCount = loginLogService.getLoginLogListCountByParam(loginLogParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(logCount, loginLogParam.getItemsPerPage());
		loginLogParam.setPagination(pagination);

		// 운영자 역할 목록
		SearchParam roleParam = new SearchParam();
		roleParam.setConditionType("ROLE_ADMIN");
		model.addAttribute("adminMenuRoles", roleService.getRoleListByParam(roleParam));

		model.addAttribute("count", logCount);
		model.addAttribute("list", loginLogService.getLoginLogListByParam(loginLogParam));
		model.addAttribute("loginLogParam", loginLogParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.getView("/log/login-log-list");
	}

	@PostMapping("/login-log")
	@RequestProperty(title="로그인 로그 관리")
	public String searchLoginLog(Model model, LoginLogParam loginLogParam) {
		String today = DateUtils.getToday("yyyyMMdd");
		loginLogParam.setSrchStartLoginDate(StringUtils.defaultIfEmpty(loginLogParam.getSrchStartLoginDate(), today));
		loginLogParam.setSrchEndLoginDate(StringUtils.defaultIfEmpty(loginLogParam.getSrchEndLoginDate(), today));

		// 기본 목록수 (row: 20)
		if(CommonUtils.intNvl(loginLogParam.getItemsPerPageTemp()) == 0) {
			loginLogParam.setItemsPerPage(20);
		}

		// 로그인 로그 목록 갯수 조회
		int logCount = loginLogService.getLoginLogListCountByParam(loginLogParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(logCount, loginLogParam.getItemsPerPage());
		loginLogParam.setPagination(pagination);

		// 운영자 역할 목록
		SearchParam roleParam = new SearchParam();
		roleParam.setConditionType("ROLE_ADMIN");
		model.addAttribute("adminMenuRoles", roleService.getRoleListByParam(roleParam));

		model.addAttribute("count", logCount);
		model.addAttribute("list", loginLogService.getLoginLogListByParam(loginLogParam));
		model.addAttribute("loginLogParam", loginLogParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.getView("/log/login-log-list");
	}


	/**
	 * 로그인 로그 관리 > 상세보기
	 * @param model
	 * @param loginLogId
	 * @return
	 */
	@GetMapping("/login-log/details/{loginLogId}")
	@RequestProperty(title="로그인 로그 관리 상세")
	public String loginLogDetails(Model model, @PathVariable("loginLogId") Integer loginLogId) {
		model.addAttribute("details", loginLogService.getLoginLogDetailsDateInfo(loginLogId));
		return ViewUtils.getView("/log/login-log-details");
	}

	/**
	 * 로그인 로그 관리 > 상세보기 > 메뉴사용이력
	 * @param model
	 * @param loginLogId
	 * @return
	 */
	@GetMapping("/login-log/details/{loginLogId}/action-log-list")
	@RequestProperty(layout="blank", title="관리자 메뉴사용이력")
	public String actionLogList(Model model, ActionLogParam actionLogParam) {

		// 관리자 메뉴사용이력 관리 갯수 조회
		int actionCount = actionLogService.getManagerActionLogListCountByParam(actionLogParam);

		// 페이징
		Pagination pagination = Pagination.getInstance(actionCount, actionLogParam.getItemsPerPage());
		actionLogParam.setPagination(pagination);
		pagination.setLink("javascript:getActionLogList("+ Pagination.REPLACE_PAGE_PATTERN + ", "+ actionLogParam.getItemsPerPage() +")");

		model.addAttribute("count", actionCount);
		model.addAttribute("list", actionLogService.getManagerActionLogListByParam(actionLogParam));
		model.addAttribute("actionLogParam", actionLogParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.getView("/log/action-log-list");
	}

	/**
	 * 로그인 로그 관리 (Old)
	 * @param model
	 * @param loginLogParam
	 * @return
	 */
	@GetMapping("/login-log-old")
	@RequestProperty(title="로그인 로그 관리")
	public String loginLogOld(Model model, LoginLogParam loginLogParam) {

		Pagination pagination = Pagination.getInstance(loginLogService.getLoginLogListCountByParam(loginLogParam));
		loginLogParam.setPagination(pagination);

		model.addAttribute("list", loginLogService.getLoginLogListByParam(loginLogParam));
		model.addAttribute("loginLogParam", loginLogParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.getView("/log/login-log-list-old");
	}

	/**
	 * 사용자 로그인 로그 관리
	 * @param model
	 * @param loginLogParam
	 * @return
	 */
	@GetMapping("/user/login-log")
	@RequestProperty(title="사용자 로그인 로그 관리")
	public String userLoginLog(Model model, LoginLogParam loginLogParam) {
		String today = DateUtils.getToday("yyyyMMdd");
		loginLogParam.setSrchStartLoginDate(StringUtils.defaultIfEmpty(loginLogParam.getSrchStartLoginDate(), today));
		loginLogParam.setSrchEndLoginDate(StringUtils.defaultIfEmpty(loginLogParam.getSrchEndLoginDate(), today));

		// 기본 목록수 (row: 20)
		if(CommonUtils.intNvl(loginLogParam.getItemsPerPageTemp()) == 0) {
			loginLogParam.setItemsPerPage(20);
		}

		// 로그인 로그 목록 갯수 조회
		int logCount = loginLogService.getUserLoginLogListCountByParam(loginLogParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(logCount, loginLogParam.getItemsPerPage());
		loginLogParam.setPagination(pagination);

		model.addAttribute("count", logCount);
		model.addAttribute("list", loginLogService.getUserLoginLogListByParam(loginLogParam));
		model.addAttribute("loginLogParam", loginLogParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.getView("/log/user/login-log-list");
	}

	@PostMapping("/user/login-log")
	@RequestProperty(title="사용자 로그인 로그 관리")
	public String searchUserLoginLog(Model model, LoginLogParam loginLogParam) {
		String today = DateUtils.getToday("yyyyMMdd");
		loginLogParam.setSrchStartLoginDate(StringUtils.defaultIfEmpty(loginLogParam.getSrchStartLoginDate(), today));
		loginLogParam.setSrchEndLoginDate(StringUtils.defaultIfEmpty(loginLogParam.getSrchEndLoginDate(), today));

		// 기본 목록수 (row: 20)
		if(CommonUtils.intNvl(loginLogParam.getItemsPerPageTemp()) == 0) {
			loginLogParam.setItemsPerPage(20);
		}

		// 로그인 로그 목록 갯수 조회
		int logCount = loginLogService.getUserLoginLogListCountByParam(loginLogParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(logCount, loginLogParam.getItemsPerPage());
		loginLogParam.setPagination(pagination);

		model.addAttribute("count", logCount);
		model.addAttribute("list", loginLogService.getUserLoginLogListByParam(loginLogParam));
		model.addAttribute("loginLogParam", loginLogParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.getView("/log/user/login-log-list");
	}

	/**
	 * 사용자 로그인 로그 관리 > 상세보기
	 * @param model
	 * @param loginLogId
	 * @return
	 */
	@GetMapping("/user/login-log/details/{loginLogId}")
	@RequestProperty(title="사용자 로그인 로그 관리 상세")
	public String userLoginLogDetails(Model model, @PathVariable("loginLogId") Integer loginLogId) {
		model.addAttribute("details", loginLogService.getUserLoginLogDetailsDateInfo(loginLogId));
		return ViewUtils.getView("/log/user/login-log-details");
	}

	/**
	 * 사용자 로그인 로그 관리 > 상세보기 > 메뉴사용이력
	 * @param model
	 * @param loginLogId
	 * @return
	 */
	@GetMapping("/user/login-log/details/{loginLogId}/action-log-list")
	@RequestProperty(layout="blank", title="사용자 메뉴사용이력")
	public String userActionLogList(Model model, ActionLogParam actionLogParam) {

		// 관리자 메뉴사용이력 관리 갯수 조회
		int actionCount = actionLogService.getUserActionLogListCountByParam(actionLogParam);

		// 페이징
		Pagination pagination = Pagination.getInstance(actionCount, actionLogParam.getItemsPerPage());
		actionLogParam.setPagination(pagination);
		pagination.setLink("javascript:getActionLogList("+ Pagination.REPLACE_PAGE_PATTERN + ", "+ actionLogParam.getItemsPerPage() +")");

		model.addAttribute("count", actionCount);
		model.addAttribute("list", actionLogService.getUserActionLogListByParam(actionLogParam));
		model.addAttribute("actionLogParam", actionLogParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.getView("/log/user/action-log-list");
		}

	@GetMapping("/exceldownload-reason")
	@RequestProperty(title = "엑셀 다운로드 사유", layout = "base")
	public String exceldownloadLogConfirm() {
		return ViewUtils.getView("/popup/popup_reason");
	}

	/**
	 * 엑셀다운로드 사유 관리
	 * @param model
	 * @param PrivacyAccessLog
	 * @return
	 */
	@GetMapping("/exceldownload-log")
	@RequestProperty(title="엑셀다운로드 사유 관리")
	public String exceldownloadLog(@ModelAttribute PrivacyLogParam privacyLogParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		privacyLogParam.setSrchStartLogDate(StringUtils.defaultIfEmpty(privacyLogParam.getSrchStartLogDate(), today));
		privacyLogParam.setSrchEndLogDate(StringUtils.defaultIfEmpty(privacyLogParam.getSrchEndLogDate(), today));

		// 기본 목록수 (row: 20)
		if(CommonUtils.intNvl(privacyLogParam.getItemsPerPageTemp()) == 0) {
			privacyLogParam.setItemsPerPage(20);
		}

		//엑셀 다운로드 사유 행안부/시스템 담당자가 아니면 본인꺼만 보이도록 수정
		User user = UserUtils.getUser();
		Seller seller = SellerUtils.getSeller();
		//ROLE_ADMIN_1	시스템주담당자
		//ROLE_ADMIN_2	시스템부담당자
		//ROLE_ADMIN_3	행안부주담당자
		//ROLE_ADMIN_4	행안부부담당자
		//ROLE_ADMIN_5	지자체주담당자
		//ROLE_ADMIN_6	지자체부담당자
		int adminChk = 0;
		if(SellerUtils.isSellerLogin()) {
			for (UserRole userRole : user.getUserRoles()) {
				if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())|| "ROLE_ADMIN_3".equals(userRole.getAuthority())|| "ROLE_ADMIN_4".equals(userRole.getAuthority())) {
					adminChk++;
				}
			}
			if(adminChk == 0) {
				if (seller == null) {
					throw new UserException("권한이 없습니다.");
				}
				privacyLogParam.setAdminUserId(seller.getSellerId());
			}
		}else {
			for (UserRole userRole : user.getUserRoles()) {
				if ("ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())|| "ROLE_ADMIN_3".equals(userRole.getAuthority())|| "ROLE_ADMIN_4".equals(userRole.getAuthority())) {
					adminChk++;
				}
			}
			if(adminChk == 0) privacyLogParam.setAdminUserId(user.getUserId());
		}

		// 로그인 로그 목록 갯수 조회
		int logCount = exceldownloadLogService.getExceldownloadLogListCountByParam(privacyLogParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(logCount, privacyLogParam.getItemsPerPage());
		privacyLogParam.setPagination(pagination);

		// 운영자 역할 목록
		SearchParam roleParam = new SearchParam();
		roleParam.setConditionType("ROLE_ADMIN");
		model.addAttribute("adminMenuRoles", roleService.getRoleListByParam(roleParam));

		model.addAttribute("count", logCount);
		model.addAttribute("list", exceldownloadLogService.getExceldownloadLogListByParam(privacyLogParam));
		model.addAttribute("loginLogParam", privacyLogParam);
		model.addAttribute("pagination", privacyLogParam.getPagination());

		return ViewUtils.getView("/log/exceldownload-log-list");
	}

	/**
	 * 엑셀다운로드 사유 목록 > 사유 (팝업)
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/exceldownload-log/popup/{id}")
	@RequestProperty(title = "엑셀다운로드 사유", layout = "base")
	public String exceldownloadLogDetail(@PathVariable("id") Long id, Model model) {
		PrivacyLogParam privacyLogParam = exceldownloadLogService.getExceldownloadLogDetail(id);

		Boolean succChk = false;
		User user = UserUtils.getUser();
		if(user.getUserId() == privacyLogParam.getManagerId()) succChk = true;

		model.addAttribute("detail", privacyLogParam);
		model.addAttribute("succChk", succChk);

		return ViewUtils.getView("/log/popup/exceldownload-log-detail");
	}

	/**
	 * 엑셀다운로드 사유 목록 > 사유 (팝업)
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/exceldownload-log/popup/hist/{id}")
	@RequestProperty(title = "엑셀다운로드 사유 수정 이력", layout = "base")
	public String exceldownloadLogHistDetail(@PathVariable("id") Long id, Model model) {

		List<PrivacyLogHistParam> privacyAccessLogHistList = privacyAccessLogHistService.getPrivacyAccessLogHistListByParam(id);
		int privacyCnt = privacyAccessLogHistService.getPrivacyAccessLogHistListCountByParam(id);

		PrivacyLogHistParam privacyLogHistParam = new PrivacyLogHistParam();

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(privacyCnt, privacyLogHistParam.getItemsPerPage());
		privacyLogHistParam.setPagination(pagination);

		model.addAttribute("count", privacyCnt);
		model.addAttribute("list", privacyAccessLogHistList);
		model.addAttribute("pagination", pagination);

		return ViewUtils.getView("/log/popup/exceldownload-log-hist");
	}

	@GetMapping("/relay-log")
	@RequestProperty(title="연계 로그 관리")
	public String relayLog(Model model, RelayLogParam relayLogParam) {
		try {
			String todays = DateUtils.getToday("yyyyMMdd");
			relayLogParam.setSrchStartLogDate(StringUtils.defaultIfEmpty(relayLogParam.getSrchStartLogDate(), todays));
			relayLogParam.setSrchEndLogDate(StringUtils.defaultIfEmpty(relayLogParam.getSrchEndLogDate(), todays));

			// 기본 목록수 (row: 20)
			if(CommonUtils.intNvl(relayLogParam.getItemsPerPageTemp()) == 0) {
				relayLogParam.setItemsPerPage(20);
			}
			List<Code> cmmnCodeList = CodeUtils.getCodeList("RELAY_TYPE");

			// 로그인 로그 목록 갯수 조회
			int logCount = relayLogService.selectRelayLogListCount(relayLogParam);

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(logCount, relayLogParam.getItemsPerPage());
			relayLogParam.setPagination(pagination);
			List<RelayLogParam> list = relayLogService.selectRelayLogList(relayLogParam);

			// 운영자 역할 목록
			SearchParam roleParam = new SearchParam();
			roleParam.setConditionType("ROLE_ADMIN");
			model.addAttribute("adminMenuRoles", roleService.getRoleListByParam(roleParam));
			model.addAttribute("count", logCount);
			model.addAttribute("list", list);
			model.addAttribute("cmmnCodeList", cmmnCodeList);
			model.addAttribute("pagination", pagination);
			model.addAttribute("relayLogParam", relayLogParam);
		} catch(UserException e) {
			logger.error(getClass().getName() + " relayLog error", e);
		}

		return ViewUtils.getView("/log/relay-log-list");
	}

	@PostMapping("/relay-log")
	@RequestProperty(title="연계 로그 관리")
	public String searchRelayLog(Model model, RelayLogParam relayLogParam) {
		try {
			String todays = DateUtils.getToday("yyyyMMdd");
			relayLogParam.setSrchStartLogDate(StringUtils.defaultIfEmpty(relayLogParam.getSrchStartLogDate(), todays));
			relayLogParam.setSrchEndLogDate(StringUtils.defaultIfEmpty(relayLogParam.getSrchEndLogDate(), todays));

			// 기본 목록수 (row: 20)
			if(CommonUtils.intNvl(relayLogParam.getItemsPerPageTemp()) == 0) {
				relayLogParam.setItemsPerPage(20);
			}
			List<Code> cmmnCodeList = CodeUtils.getCodeList("RELAY_TYPE");

			// 로그인 로그 목록 갯수 조회
			int logCount = relayLogService.selectRelayLogListCount(relayLogParam);

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(logCount, relayLogParam.getItemsPerPage());
			relayLogParam.setPagination(pagination);
			List<RelayLogParam> list = relayLogService.selectRelayLogList(relayLogParam);

			// 운영자 역할 목록
			SearchParam roleParam = new SearchParam();
			roleParam.setConditionType("ROLE_ADMIN");
			model.addAttribute("adminMenuRoles", roleService.getRoleListByParam(roleParam));
			model.addAttribute("count", logCount);
			model.addAttribute("list", list);
			model.addAttribute("cmmnCodeList", cmmnCodeList);
			model.addAttribute("pagination", pagination);
			model.addAttribute("relayLogParam", relayLogParam);
		} catch(UserException e) {
			logger.error(getClass().getName() + " relayLog error", e);
		}

		return ViewUtils.getView("/log/relay-log-list");
	}


	@GetMapping("/gif-stnd-buga")
	@RequestProperty(title="연계 로그 관리")
	public String gifStndBuga(@ModelAttribute NextBugaRequestLogDto nextBugaRequestLogDto, Model model) {

		try {
			String todays = DateUtils.getToday("yyyyMMdd");
			// 문자열(시작/종료일)이 공백, null인 경우 todays 반환
			nextBugaRequestLogDto.setSrchStartLogDate(StringUtils.defaultIfEmpty(nextBugaRequestLogDto.getSrchStartLogDate(), todays));
			nextBugaRequestLogDto.setSrchEndLogDate(StringUtils.defaultIfEmpty(nextBugaRequestLogDto.getSrchEndLogDate(), todays));


			// 기본 목록수 (row: 20)
			if(CommonUtils.intNvl(nextBugaRequestLogDto.getItemsPerPageTemp()) == 0) {
				nextBugaRequestLogDto.setItemsPerPage(20);
			}

			// 로그인 로그 목록 갯수 조회
			int logCount = ngDonationService.selectNextBugaLogCount(nextBugaRequestLogDto);

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(logCount, nextBugaRequestLogDto.getItemsPerPage());
			nextBugaRequestLogDto.setPagination(pagination);
			List<NextBugaResponseLogDto> list = ngDonationService.selectNextBugaLogList(nextBugaRequestLogDto);


			// 운영자 역할 목록
			SearchParam roleParam = new SearchParam();
			roleParam.setConditionType("ROLE_ADMIN");
			model.addAttribute("adminMenuRoles", roleService.getRoleListByParam(roleParam));
			model.addAttribute("count", logCount);
			model.addAttribute("list", list);
			model.addAttribute("pagination", pagination);
			model.addAttribute("nextBugaRequestLogDto", nextBugaRequestLogDto);
		} catch(UserException e) {
			logger.error(getClass().getName() + " gifStndBuga error", e);
		}

		return ViewUtils.getView("/log/gif-stnd-buga-list");
	}

	@GetMapping("/gif-stnd-sunap")
	@RequestProperty(title="연계 로그 관리")
	public String gifStndSunap(@ModelAttribute NextSunapRequestLogDto nextSunapRequestLogDto, Model model) {
		try {
			String todays = DateUtils.getToday("yyyyMMdd");
			nextSunapRequestLogDto.setSrchStartLogDate(StringUtils.defaultIfEmpty(nextSunapRequestLogDto.getSrchStartLogDate(), todays));
			nextSunapRequestLogDto.setSrchEndLogDate(StringUtils.defaultIfEmpty(nextSunapRequestLogDto.getSrchEndLogDate(), todays));

			// 기본 목록수 (row: 20)
			if(CommonUtils.intNvl(nextSunapRequestLogDto.getItemsPerPageTemp()) == 0) {
				nextSunapRequestLogDto.setItemsPerPage(20);
			}

			// 로그인 로그 목록 갯수 조회
			int logCount = ngDonationService.selectNextSunapLogCount(nextSunapRequestLogDto);

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(logCount, nextSunapRequestLogDto.getItemsPerPage());
			nextSunapRequestLogDto.setPagination(pagination);
			List<NextSunapResponseLogDto> list = ngDonationService.selectNextSunapLogList(nextSunapRequestLogDto);

			// 운영자 역할 목록
			SearchParam roleParam = new SearchParam();
			roleParam.setConditionType("ROLE_ADMIN");
			model.addAttribute("adminMenuRoles", roleService.getRoleListByParam(roleParam));
			model.addAttribute("count", logCount);
			model.addAttribute("list", list);
			model.addAttribute("pagination", pagination);
			model.addAttribute("nextSunapRequestLogDto", nextSunapRequestLogDto);
		} catch(UserException e) {
			logger.error(getClass().getName() + " gifStndSunap error", e);
		}

		return ViewUtils.getView("/log/gif-stnd-sunap-list");
	}

	@GetMapping("/gif-seoul-buga")
	@RequestProperty(title="연계 로그 관리")
	public String gifSeoulBuga(@ModelAttribute GifSeoulParam gifSeoulParam, Model model) {
		try {
			String todays = DateUtils.getToday("yyyyMMdd");
			gifSeoulParam.setSrchStartLogDate(StringUtils.defaultIfEmpty(gifSeoulParam.getSrchStartLogDate(), todays));
			gifSeoulParam.setSrchEndLogDate(StringUtils.defaultIfEmpty(gifSeoulParam.getSrchEndLogDate(), todays));

			// 기본 목록수 (row: 20)
			if(CommonUtils.intNvl(gifSeoulParam.getItemsPerPageTemp()) == 0) {
				gifSeoulParam.setItemsPerPage(20);
			}

			// 로그인 로그 목록 갯수 조회
			int logCount = ngDonationService.selectGifSeoulBugaCount(gifSeoulParam);

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(logCount, gifSeoulParam.getItemsPerPage());
			gifSeoulParam.setPagination(pagination);
			List<GifSeoulParam> list = ngDonationService.selectGifSeoulBugaList(gifSeoulParam);

			// 운영자 역할 목록
			SearchParam roleParam = new SearchParam();
			roleParam.setConditionType("ROLE_ADMIN");
			model.addAttribute("adminMenuRoles", roleService.getRoleListByParam(roleParam));
			model.addAttribute("count", logCount);
			model.addAttribute("list", list);
			model.addAttribute("pagination", pagination);
			model.addAttribute("gifStndParam", gifSeoulParam);
		} catch(UserException e) {
			logger.error(getClass().getName() + " gifSeoulBuga error", e);
		}

		return ViewUtils.getView("/log/gif-seoul-buga-list");
	}

	@GetMapping("/gif-seoul-sunap")
	@RequestProperty(title="연계 로그 관리")
	public String gifSeoulSunap(@ModelAttribute GifSeoulParam gifSeoulParam, Model model) {
		try {
			String todays = DateUtils.getToday("yyyyMMdd");
			gifSeoulParam.setSrchStartLogDate(StringUtils.defaultIfEmpty(gifSeoulParam.getSrchStartLogDate(), todays));
			gifSeoulParam.setSrchEndLogDate(StringUtils.defaultIfEmpty(gifSeoulParam.getSrchEndLogDate(), todays));

			// 기본 목록수 (row: 20)
			if(CommonUtils.intNvl(gifSeoulParam.getItemsPerPageTemp()) == 0) {
				gifSeoulParam.setItemsPerPage(20);
			}

			// 로그인 로그 목록 갯수 조회
			int logCount = ngDonationService.selectGifSeoulSunapCount(gifSeoulParam);

			// 페이징 정보 셋팅
			Pagination pagination = Pagination.getInstance(logCount, gifSeoulParam.getItemsPerPage());
			gifSeoulParam.setPagination(pagination);
			List<GifSeoulParam> list = ngDonationService.selectGifSeoulSunapList(gifSeoulParam);

			// 운영자 역할 목록
			SearchParam roleParam = new SearchParam();
			roleParam.setConditionType("ROLE_ADMIN");
			model.addAttribute("adminMenuRoles", roleService.getRoleListByParam(roleParam));
			model.addAttribute("count", logCount);
			model.addAttribute("list", list);
			model.addAttribute("pagination", pagination);
			model.addAttribute("gifSeoulParam", gifSeoulParam);
		} catch(UserException e) {
			logger.error(getClass().getName() + " gifSeoulSunap error", e);
		}

		return ViewUtils.getView("/log/gif-seoul-sunap-list");
	}

	/**
	 * 엑셀다운로드 사유 관리
	 * @param model
	 * @param PrivacyAccessLog
	 * @return
	 */
	@PostMapping("/exceldownload-log")
	@RequestProperty(title="엑셀다운로드 사유 관리")
	public String exceldownloadLogPost(@ModelAttribute PrivacyLogParam privacyLogParam, Model model) {
		return exceldownloadLog(privacyLogParam, model);
	}

	@PostMapping("/gif-seoul-buga")
	@RequestProperty(title="연계 로그 관리")
	public String gifSeoulBugaPost(@ModelAttribute GifSeoulParam gifSeoulParam, Model model) {
		return gifSeoulBuga(gifSeoulParam, model);
	}

	@PostMapping("/gif-seoul-sunap")
	@RequestProperty(title="연계 로그 관리")
	public String gifSeoulSunapPost(@ModelAttribute GifSeoulParam gifSeoulParam, Model model) {
		return gifSeoulSunap(gifSeoulParam, model);
	}

	@PostMapping("/gif-stnd-buga")
	@RequestProperty(title="연계 로그 관리")
	public String gifStndBugaPost(@ModelAttribute NextBugaRequestLogDto nextBugaRequestLogDto, Model model) {
		return gifStndBuga(nextBugaRequestLogDto, model);
	}

	@PostMapping("/gif-stnd-sunap")
	@RequestProperty(title="연계 로그 관리")
	public String gifStndSunapPost(@ModelAttribute NextSunapRequestLogDto nextSunapRequestLogDto, Model model) {
		return gifStndSunap(nextSunapRequestLogDto, model);
	}


}
