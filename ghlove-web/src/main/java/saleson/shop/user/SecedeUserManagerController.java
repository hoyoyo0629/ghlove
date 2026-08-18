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
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;

import lombok.RequiredArgsConstructor;
import saleson.shop.user.support.SecedeUserSearchParam;

@Controller
@RequestMapping("/opmanager/user/secede-user")
@RequestProperty(title = "회원탈퇴관리", layout = "default", template="opmanager")
@RequiredArgsConstructor
public class SecedeUserManagerController {
	private static final Logger log = LoggerFactory.getLogger(SecedeUserManagerController.class);

	/** 회원탈퇴관리 Service */
	@Autowired
	private SecedeUserService secedeUserService;

	/**
	 * 회원탈퇴관리 목록 조회
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String secedeUserList(SecedeUserSearchParam searchParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartLeaveDate(StringUtils.defaultIfEmpty(searchParam.getSrchStartLeaveDate(), today));
		searchParam.setSrchEndLeaveDate(StringUtils.defaultIfEmpty(searchParam.getSrchEndLeaveDate(), today));

		// 회원탈퇴관리 목록 갯수 조회
		int secedeUserCount = 0;

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(secedeUserCount);
		searchParam.setPagination(pagination);

		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, secedeUserCount);
		model.addAttribute("list"		, Collections.EMPTY_LIST);
		model.addAttribute("searchParam", searchParam);
		return "view:/user/secede-user/list";
	}

	@PostMapping("/list")
	public String secedeUserListPost(SecedeUserSearchParam searchParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartLeaveDate(StringUtils.defaultIfEmpty(searchParam.getSrchStartLeaveDate(), today));
		searchParam.setSrchEndLeaveDate(StringUtils.defaultIfEmpty(searchParam.getSrchEndLeaveDate(), today));

		// 회원탈퇴관리 목록 갯수 조회
		int secedeUserCount = secedeUserService.getSecedeUserCountByParam(searchParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(secedeUserCount);
		searchParam.setPagination(pagination);

		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, secedeUserCount);
		model.addAttribute("list"		, secedeUserService.getSecedeUserListByParam(searchParam));
		model.addAttribute("searchParam", searchParam);
		return "view:/user/secede-user/list";
	}

	/**
	 * 회원탈퇴관리 목록 > 탈퇴사유 (팝업)
	 * @param userId
	 * @param model
	 * @return
	 */
	@GetMapping("/popup/reason-details/{userId}")
	@RequestProperty(title = "탈퇴사유", layout = "base")
	public String secedeReasonDetails(@PathVariable("userId") Long userId, Model model) {
		model.addAttribute("details", secedeUserService.getSecedeUserDetails(userId));
		return ViewUtils.getView("/user/popup/secede-reason-details");
	}
}
