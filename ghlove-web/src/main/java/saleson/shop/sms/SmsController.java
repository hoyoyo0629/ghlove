package saleson.shop.sms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.SearchParam;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.common.enumeration.SmsType;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.SmsIpsParam;
import saleson.common.sms.domain.TifIpsSndngMDisplay;

@Controller
@RequestMapping("/opmanager/sms-log")
@RequestProperty(title="문자 이력 조회", layout="default", template="opmanager")
public class SmsController {

	@Autowired
	private SmsIpsService smsIpsService;
	
	/**
	 * 문자 전송 이력
	 * @return
	 */
	@GetMapping("/list")
	public String smsLogList(SmsIpsParam searchParam, Model model, RequestContext requestContext) {
		if (!SecurityUtils.isManager()) {
			throw new PageNotFoundException("권한이 없습니다.");
		}
		if (searchParam == null) {
			searchParam = new SmsIpsParam();
		}
		
		if (searchParam.getItemsPerPage() == 0) {
			searchParam.setItemsPerPage(10);
		}
		if (searchParam.getPage() == 0) {
			searchParam.setPage(1);
		}
		
//		if ("SMS_TYPE".equalsIgnoreCase(searchParam.getWhere())) {
//			searchParam.setSvcIds(searchParam.getQuery());
//		} else {
			searchParam.setSvcIdsAll();
//		}
		
		Pagination pagination = Pagination.getInstance(smsIpsService.getSmsSendCnt(searchParam), searchParam.getItemsPerPage());
		pagination.setCurrentPage(searchParam.getPage());
		
		searchParam.setPagination(pagination);
		
		List<TifIpsSndngMDisplay> list = smsIpsService.getSmsSendList(searchParam);
		
		model.addAttribute("smsLogList", list);
		model.addAttribute("searchParam", searchParam);
		model.addAttribute("pagination", pagination);
		model.addAttribute("smsTypes", SmsType.values());
		return ViewUtils.view();
	}
	
	@PostMapping("/list")
	public String smsLogListPost(SmsIpsParam searchParam, Model model, RequestContext requestContext) {
		return smsLogList(searchParam,model,requestContext);
	}
	
}
