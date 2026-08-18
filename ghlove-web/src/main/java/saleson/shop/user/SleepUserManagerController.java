package saleson.shop.user;

import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import lombok.RequiredArgsConstructor;
import saleson.shop.user.domain.SleepUser;
import saleson.shop.user.support.SleepUserSearchParam;

@Controller
@RequestMapping("/opmanager/user/sleep-user")
@RequestProperty(title = "휴면회원관리", layout = "default", template="opmanager")
@RequiredArgsConstructor
public class SleepUserManagerController {
	private static final Logger log = LoggerFactory.getLogger(SleepUserManagerController.class);

	/** 휴면회원관리 Service */
	@Autowired
	private SleepUserService sleepUserService;

	/**
	 * 휴면회원관리
	 * @param searchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String sleepUserList(SleepUserSearchParam searchParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartLoginDate(StringUtils.defaultIfEmpty(searchParam.getSrchStartLoginDate(), today));
		searchParam.setSrchEndLoginDate(StringUtils.defaultIfEmpty(searchParam.getSrchEndLoginDate(), today));

		// 휴면회원관리 목록 갯수 조회
		int sleepUserCount = 0;

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(sleepUserCount);
		searchParam.setPagination(pagination);

		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, sleepUserCount);
		model.addAttribute("list"		, Collections.EMPTY_LIST);
		model.addAttribute("searchParam", searchParam);
		return "view:/user/sleep-user/list";
	}

	@PostMapping("/list")
	public String sleepUserListPost(SleepUserSearchParam searchParam, Model model) {
		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setSrchStartLoginDate(StringUtils.defaultIfEmpty(searchParam.getSrchStartLoginDate(), today));
		searchParam.setSrchEndLoginDate(StringUtils.defaultIfEmpty(searchParam.getSrchEndLoginDate(), today));

		// 휴면회원관리 목록 갯수 조회
		int sleepUserCount = sleepUserService.getSleepUserCountByParam(searchParam);

		// 페이징 정보 셋팅
		Pagination pagination = Pagination.getInstance(sleepUserCount);
		searchParam.setPagination(pagination);

		model.addAttribute("pagination"	, pagination);
		model.addAttribute("count"		, sleepUserCount);
		model.addAttribute("list"		, sleepUserService.getSleepUserListByParam(searchParam));
		model.addAttribute("searchParam", searchParam);
		return "view:/user/sleep-user/list";
	}

	/**
	 * 휴면회원 해제
	 * @param sleepUser
	 * @return
	 */
	@PostMapping("/wakeup")
	public JsonView wakeupSleepUser(SleepUser sleepUser) {
		String code = "FAIL";

		try {
			code = sleepUserService.updateWakeupSleepUser(sleepUser);		// 에러코드도 리턴하도록 수정

		} catch (OpRuntimeException e) {
			log.error("ERROR: {}", "========== wakeupSleepUser OpRuntimeException ===========");
			code = "FAIL";
		}

		return JsonViewUtils.success(code);
	}
}
