package saleson.shop.oz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/opmanager/oz/")
@RequestProperty(layout="default")
public class OzViewTestController {

	/**
	 * 접속 ip 리스트
	 * @return
	 */
	@GetMapping("viewer-test")
	public String index(Model model) {

//		accessParam.setDisplayFlag("Y");
//		int count = accessService.getAllowIpCount(accessParam);
//
//		Pagination pagination = Pagination.getInstance(count, 10);
//		accessParam.setPagination(pagination);
//
//		List<Access> list = accessService.getAllowIpList(accessParam);
//
//		model.addAttribute("list", list);
//		model.addAttribute("pagination", pagination);
//		model.addAttribute("count", count);

		return ViewUtils.view();
	}
}
