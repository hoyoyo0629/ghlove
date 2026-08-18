package saleson.shop.welfarecenter;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.shop.give.givestate.GiveStateService;
import saleson.shop.user.LocgovService;
import saleson.shop.welfarecenter.domain.WlfrCntrMng;
import saleson.shop.welfarecenter.support.WlfrCntrMngParam;
import saleson.shop.welfarecenter.domain.WlfrCntrMngManagerRequest;
import saleson.shop.welfarecenter.domain.WlfrCntrMngManagerRequestResult;
import saleson.shop.welfarecenter.support.WlfrCntrMngManagerRequestSearchParam;
import saleson.common.enumeration.IdType;
import saleson.common.utils.UserUtils;

@Controller
@RequestMapping("/opmanager/welfareCenter/**")
@RequestProperty(title="행정복지센터 관리", layout="default", template="opmanager")
public class WelfareCenterManagerController {
	private static final Logger log = LoggerFactory.getLogger(WelfareCenterManagerController.class);

	@Autowired
	private WelfareCenterService welfareCenterService;
	@Autowired
	private LocgovService locgovService;
	@Autowired
	private GiveStateService giveStateService;


	/**
	 * 행정복지센터 권한승인 목록 조회
	 * @param model
	 * @param WlfrCntrMngManagerRequestSearchParam
	 * @return
	 */
	@GetMapping("/list")
	@RequestProperty(title = "관리자권한요청", layout = "default", template="opmanager")
	public String selectWlfrCntrMngList(WlfrCntrMngManagerRequestSearchParam searchParam, Model model) {

		model.addAttribute("list"			, welfareCenterService.getManagerRequestListByParam(searchParam));
		model.addAttribute("searchParam"	, searchParam);

		return ViewUtils.getView("/welfareCenter/list");
	}

	/**
	 * 관리자 권한 승인관리 상세 정보 조회 (팝업창)
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("/popup/details/{userId}/{reqstSn}/{lclgvCd}/{active}")
	public String wlfrCntrMngDetails(Model model,
			@PathVariable("userId") Long userId,
			@PathVariable("reqstSn") Integer reqstSn,
			@PathVariable("lclgvCd") String lclgvCd,
			@PathVariable("active") String active) {

		// Path 값 검색조건 추가
		WlfrCntrMngManagerRequestSearchParam searchParam = new WlfrCntrMngManagerRequestSearchParam();
		searchParam.setUserId(userId);
		searchParam.setReqstSn(reqstSn);

		WlfrCntrMngManagerRequest wlfrCntrMngManagerRequest = welfareCenterService.getManagerRequestDetails(searchParam);
		wlfrCntrMngManagerRequest.setActive(active);
		model.addAttribute("details", wlfrCntrMngManagerRequest);

		WlfrCntrMngParam param = new WlfrCntrMngParam();
		param.setLclgvCd(lclgvCd);
		model.addAttribute("list", welfareCenterService.selectWlfrCntrMngList(param));
		return ViewUtils.getView("/welfareCenter/manager-request-details");
	}

	/**
	 * 관리자 권한 승인관리 확인 - 승인/거절
	 * @param managerRequest
	 * @return
	 */
	@PostMapping("/confirm")
	public JsonView managerRequestConfirmProcess(WlfrCntrMngManagerRequest managerRequest) {
		// 결과값
		WlfrCntrMngManagerRequestResult result = new WlfrCntrMngManagerRequestResult();
		result.setCode("ERR");

		try {
			// 수정자 ID 셋팅
			managerRequest.setLastUpdusrId(UserUtils.getUser().getUserId());
			// 관리자 권한 승인관리 - '거절' 프로세스
			if("300".equals(managerRequest.getConfmSttusCode())) {
				result = welfareCenterService.updateManagerRequestReject(managerRequest);

			// 관리자 권한 승인관리 - '승인' 프로세스
			} else if("100".equals(managerRequest.getConfmSttusCode())) {
				result = welfareCenterService.updateManagerRequestApproval(managerRequest);
			}

		} catch (OpRuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "=========== managerRequestConfirmProcess OpRuntimeException =============");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 관리자 권한 승인관리 확인 - 승인/거절
	 * @param managerRequest
	 * @return
	 */
	@PostMapping("/update/{reqstSn}/{psitnCode}/{psitnNm}")
	@ResponseBody
	public int managerRequestUpdateProcess(WlfrCntrMngManagerRequest managerRequest,
			@PathVariable("reqstSn") Integer reqstSn,
			@PathVariable("psitnCode") String psitnCode,
			@PathVariable("psitnNm") String psitnNm) {

		// 수정자 ID 셋팅
		managerRequest.setLastUpdusrId(UserUtils.getUser().getUserId());
		managerRequest.setPsitnCode(psitnCode);
		managerRequest.setReqstSn(reqstSn);
		managerRequest.setPsitnNm(psitnNm);

		// 행정복지센터 권한 업데이트 프로세스

		return welfareCenterService.updateManagerRequest(managerRequest);
	}


	// 부서 목록 조회
	@PostMapping("/wlfrCntrMngList")
	public JsonView wlfrCntrMngList(WlfrCntrMngParam param, RequestContext requestContext) {
		if (StringUtils.hasLength(param.getLclgvCd())) {
			return JsonViewUtils.success(welfareCenterService.selectWlfrCntrMngList(param));
		}
		return JsonViewUtils.success(new ArrayList<>());
	}

	/**
	 * 관리자 권한 승인관리 상세 정보 조회 (팝업창)
	 * @param model
	 * @param userId
	 * @return
	 */
	@GetMapping("/opmanager/welfareCenter/popup/history/{userId}")
	@RequestProperty(title = "행정복지센터 권한 이력 팝업", layout = "base", template = "opmanager")
	public String managerRequestHistory(Model model, @PathVariable long userId) {

		// Path 값 검색조건 추가
		WlfrCntrMngManagerRequestSearchParam searchParam = new WlfrCntrMngManagerRequestSearchParam();
		searchParam.setUserId(userId);

		model.addAttribute("list", welfareCenterService.getWlfrCntrMngHistory(searchParam));

		return ViewUtils.getView("/user/popup/manager-request-history");
	}


	/**
	 * 행정복지센터 관리 조회
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/main")
	@RequestProperty(title = "행정복지센터 관리", layout = "default", template="opmanager")
	public String selectWlfrCntrMngList(WlfrCntrMngParam param, Model model) {

		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
		/*
			ROLE_ADMIN_1	시스템주담당자
			ROLE_ADMIN_2	시스템부담당자
			ROLE_ADMIN_3	행안부주담당자
			ROLE_ADMIN_4	행안부부담당자
			ROLE_ADMIN_5	지자체주담당자
			ROLE_ADMIN_6	지자체부담당자
		*/
			if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			String locgovCode = locgovService.getLocgovCodeByOpId(user.getUserId(), IdType.MANAGER);
			param.setLclgvCd(locgovCode != null ? locgovCode : "");

			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			Code code = CodeUtils.getCode("LOCGOV_CODE", locgovObj.get("LOCGOV_CODE").toString());

			param.setUpperLocgovCode(locgovObj.get("UPPER_LOCGOV_CODE").toString());
			param.setLclgvCd(locgovObj.get("LOCGOV_CODE").toString());

			model.addAttribute("locgovNm", locgovObj.get("UPPER_LOCGOV_NM").toString() + code.getLabel());
		}

	    int count = welfareCenterService.selectWlfrCntrMngListCnt(param);

	    Pagination pagination = Pagination.getInstance(count, param.getItemsPerPage());
	    param.setPagination(pagination);

	    model.addAttribute("list", welfareCenterService.selectWlfrCntrMngList(param));
	    model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
	    model.addAttribute("pagination", param.getPagination());
	    model.addAttribute("wlfrCntrMngParam", param);

	    return ViewUtils.getView("/welfareCenter/main");
	}



	/**
	 * 행정복지센터 등록 화면 조회
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/form")
	@RequestProperty(title = "행정복지센터 등록", layout = "default", template="opmanager")
	public String wlfrCntrMngInfo(Model model) {
		WlfrCntrMng wlfrCntrMng = new WlfrCntrMng();

		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(user.getUserId());
			wlfrCntrMng.setUpperLocgovCode(locgovObj.get("UPPER_LOCGOV_CODE").toString());
			wlfrCntrMng.setLclgvCd(locgovObj.get("LOCGOV_CODE").toString());
		}

		model.addAttribute("wlfrCntrMng", wlfrCntrMng);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));

		return ViewUtils.getView("/welfareCenter/form");
	}


	/**
	 * 행정복지센터 등록
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/form")
	public String insertWlfrCntrMng(WlfrCntrMng param, Model model) {

	    boolean isDuplicate = welfareCenterService.isDuplicateWlfrCntrMng(param);

	    if (isDuplicate) {
	    	// 중복인 경우
	    	String msg = "이미 등록된 행정복지센터입니다.";
	    	return ViewUtils.redirect("/opmanager/welfareCenter/form", msg);
	    } else {
	    	// 중복이 아닌 경우
	    	welfareCenterService.insertWlfrCntrMng(param);
	    	return ViewUtils.redirect("/opmanager/welfareCenter/main", MessageUtils.getMessage("M00288")); // 등록되었습니다.
	    }
	}


	/**
	 * 행정복지센터 관리 상세 화면 조회
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/form/{pbadmsWlfrCntrId}")
	@RequestProperty(title = "행정복지센터 관리 상세", layout = "default", template = "opmanager")
	public String selectwlfrCntrMngDetail(@PathVariable("pbadmsWlfrCntrId") long pbadmsWlfrCntrId,
	                                      @ModelAttribute WlfrCntrMngParam param, Model model) {
	    // Param에 ID 설정
	    param.setPbadmsWlfrCntrId(pbadmsWlfrCntrId);

	    WlfrCntrMng wlfrCntrMng = welfareCenterService.selectwlfrCntrMngDetail(pbadmsWlfrCntrId);

	    if (wlfrCntrMng != null) {
	        model.addAttribute("wlfrCntrMng", wlfrCntrMng);
	    }
	    model.addAttribute("param", param);
	    model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));

	    return ViewUtils.getView("/welfareCenter/form");
	}

	/**
	 * 행정복지센터 수정
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/form/{pbadmsWlfrCntrId}")
	public String updatetWlfrCntrMng(@PathVariable("pbadmsWlfrCntrId") long pbadmsWlfrCntrId,
									@ModelAttribute WlfrCntrMngParam param, Model model) {

		param.setPbadmsWlfrCntrId(pbadmsWlfrCntrId);
		welfareCenterService.updatetWlfrCntrMng(param);

	    return ViewUtils.redirect("/opmanager/welfareCenter/main", MessageUtils.getMessage("M00288")); // 등록되었습니다.
	}

}
