package saleson.shop.user;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.user.domain.GeneralCustomerSecede;
import saleson.shop.user.domain.GeneralCustomerSecedeResult;
import saleson.shop.user.support.GeneralCustomerCntrSearchParam;
import saleson.shop.user.support.GeneralCustomerPointSearchParam;
import saleson.shop.user.support.GeneralCustomerSearchParam;

@Controller
@RequestMapping("/opmanager/user/customer")
@RequestProperty(title = "회원관리", layout = "default", template="opmanager")
@RequiredArgsConstructor
public class GeneralCustomerManagerController {
	private static final Logger log = LoggerFactory.getLogger(GeneralCustomerManagerController.class);

	/** 일반회원관리 Service */
	@Autowired
	private GeneralCustomerService generalCustomerService;

	/** 비밀번호 암호화 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 일반회원관리 목록 조회
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String generalCustomerList(GeneralCustomerSearchParam searchParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartCreated(StringUtils.defaultIfEmpty(searchParam.getSrchStartCreated(), today));
		searchParam.setSrchEndCreated(StringUtils.defaultIfEmpty(searchParam.getSrchEndCreated(), today));

		// 일반회원관리 목록 갯수 조회
		int customerCount = 0; //generalCustomerService.getGeneralCustomerCountByParam(searchParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(customerCount);
		searchParam.setPagination(pagination);

		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, customerCount);
		model.addAttribute("list"		, Collections.EMPTY_LIST);
		model.addAttribute("searchParam", searchParam);
		return "view:/user/customer/list";
	}

	@PostMapping("/list")
	public String generalCustomerListPost(GeneralCustomerSearchParam searchParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartCreated(StringUtils.defaultIfEmpty(searchParam.getSrchStartCreated(), today));
		searchParam.setSrchEndCreated(StringUtils.defaultIfEmpty(searchParam.getSrchEndCreated(), today));

		// 일반회원관리 목록 갯수 조회
		int customerCount = generalCustomerService.getGeneralCustomerCountByParam(searchParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(customerCount);
		searchParam.setPagination(pagination);

		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, customerCount);
		model.addAttribute("list"		, generalCustomerService.getGeneralCustomerListByParam(searchParam));
		model.addAttribute("searchParam", searchParam);
		return "view:/user/customer/list";
	}

	/**
	 * 일반회원관리 상세 조회
	 * @param searchParam
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/details/{userId}")
	public String generalCustomerDetails(GeneralCustomerSearchParam searchParam, @PathVariable("userId") Long userId, Model model) {
		model.addAttribute("details"		, generalCustomerService.getGeneralCustomerDetails(searchParam));
		model.addAttribute("cumclativeTotal", generalCustomerService.getGeneralCustomerCumulativeTotal(userId));
		return "view:/user/customer/details";
	}

	/**
	 * 일반회원관리 상세 > 개인정보 열람 (비밀번호 확인 팝업)
	 * @return
	 */
	@GetMapping("/popup/password/{userId}")
	@RequestProperty(title = "비밀번호 확인", layout = "base")
	public String generalCustomerPasswordConfirm(@PathVariable("userId") Long userId, Model model) {
		model.addAttribute("userId", userId);
		return ViewUtils.getView("/user/popup/customer-password-confirm");
	}

	/**
	 * 일반회원관리 상세 > 개인정보 열람 (비밀번호 확인 후)
	 * @param searchParam
	 * @param userId
	 * @param model
	 * @return
	 */
	@PostMapping("/popup/access/{userId}")
	@RequestProperty(title = "개인정보 열람", layout = "base")
	public String generalCustomerInformationAccess(GeneralCustomerSearchParam searchParam, @PathVariable("userId") Long userId, Model model) {

		// 로그인 사용자 비밀번호 조회
		String password = generalCustomerService.getPasswordByUserId(UserUtils.getUser().getUserId());

		// 비밀번호 암호화
		String targetPassword = CommonUtils.dataNvl(searchParam.getPassword());
		try {
			targetPassword = pCrypto.Encrypt("hash.5", targetPassword, "");
		} catch (UnsupportedEncodingException e) {
			log.error("GeneralCustomerManagerController :: generalCustomerInformationAccess");
		}

		// 비밀번호 비교
		if(passwordEncoder.matches(targetPassword, password)) {
			model.addAttribute("details", generalCustomerService.getGeneralCustomerDetailsNoMasking(searchParam));
		} else {
			return ViewUtils.redirect("/opmanager/user/customer/popup/password/"+userId, "비밀번호가 일치하지 않습니다.");
		}

		return ViewUtils.getView("/user/popup/customer-info-access");
	}

	/**
	 * 일반회원관리 상세 > 기부내역 목록 조회
	 * @param searchParam
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/details/{userId}/cntr-list")
	@RequestProperty(layout="blank")
	public String generalCustomerCntrList(GeneralCustomerCntrSearchParam searchParam, @PathVariable("userId") Long userId, Model model) {

		// 기부내역 목록 갯수 조회
		int cntrCount = generalCustomerService.getGeneralCustomerCntrCountByParam(searchParam);

		// 페이징
		Pagination pagination = Pagination.getInstance(cntrCount);
		searchParam.setPagination(pagination);
		pagination.setLink("javascript:getCntrList("+ Pagination.REPLACE_PAGE_PATTERN + ")");

		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, cntrCount);
		model.addAttribute("list"		, generalCustomerService.getGeneralCustomerCntrListByParam(searchParam));
		return "view:/user/customer/cntr-list";
	}

	/**
	 * 일반회원관리 상세 > 포인트 내역 목록 조회
	 * @param searchParam
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/details/{userId}/point-list")
	@RequestProperty(layout="blank")
	public String generalCustomerPointList(GeneralCustomerPointSearchParam searchParam, @PathVariable("userId") Long userId, Model model) {

		// 포인트 목록 갯수 조회
		int pointCount = generalCustomerService.getGeneralCustomerPointCountByParam(searchParam);

		// 페이징
		Pagination pagination = Pagination.getInstance(pointCount);
		searchParam.setPagination(pagination);
		pagination.setLink("javascript:getPointList("+ Pagination.REPLACE_PAGE_PATTERN + ")");

		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, pointCount);
		model.addAttribute("list"		, generalCustomerService.getGeneralCustomerPointListByParam(searchParam));
		return "view:/user/customer/point-list";
	}

	/**
	 * 일반회원관리 상세 > 회원탈퇴
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/popup/secede/{userId}")
	@RequestProperty(title = "회원탈퇴", layout = "base")
	public String generalCustomerSecede(@PathVariable("userId") Long userId, Model model) {
		model.addAttribute("userId", userId);
		model.addAttribute("cumclativeTotal", generalCustomerService.getGeneralCustomerCumulativeTotal(userId));
		return ViewUtils.getView("/user/popup/customer-secede-write");
	}

	/**
	 * 일반회원관리 상세 > 회원탈퇴 처리
	 * @param generalCustomerSecede
	 * @return
	 */
	@PostMapping("/secede")
	public JsonView generalCustomerSecedeProcess(GeneralCustomerSecede generalCustomerSecede) {
		GeneralCustomerSecedeResult result = new GeneralCustomerSecedeResult();
		result.setCode("FAIL");
		result.setIsLogout("N");

		try {

			// 회원탈퇴
			generalCustomerSecede.setLeaveUserId(UserUtils.getUser().getUserId());
			result = generalCustomerService.updateGeneralCustomerSecedeProcess(generalCustomerSecede);

			// 로그아웃 여부
			result.setIsLogout(UserUtils.getUser().getUserId() == generalCustomerSecede.getUserId() ? "Y" : "N");

		} catch (RuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "========== generalCustomerSecedeProcess RuntimeException ===========");
		}

		return JsonViewUtils.success(result);
	}

	@GetMapping("/delivery/{userId}")
	@RequestProperty(title = "배송지 조회", layout = "base")
	public String userDeliveryList(@PathVariable("userId") Long userId, Model model) {
		model.addAttribute("list", generalCustomerService.userDeliveryList(userId));
		return ViewUtils.getView("/user/popup/delivery");
	}
}