package saleson.shop.qustnr;

import java.sql.SQLException;

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

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.shop.qustnr.domain.Qestnar;
import saleson.shop.qustnr.domain.QustnrQesitm;
import saleson.shop.qustnr.support.QustnrSearchParam;

@Controller
@RequestMapping("/opmanager/qustnr/**")
@RequestProperty(title="설문 관리", layout="default", template="opmanager")
public class QustnrManagerController {

	private static final Logger log = LoggerFactory.getLogger(QustnrManagerController.class);

	@Autowired
	private QustnrService qustnrService;

	/**
	 * 시스템 관리 > 시스템 관리 > 설문 관리
	 * @param model
	 * @param searchParam : 검색조건
	 * @return list : 기부인원 통계 리스트
	 * @return searchParam : 검색조건
	 */
	@GetMapping("list")
	public String list(Model model, @ModelAttribute("searchParam") QustnrSearchParam searchParam) {


		int count = qustnrService.getQustnrListCnt(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", qustnrService.getQustnrList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		model.addAttribute("searchParam", searchParam);

		return "view:/qustnr/list";
	}
	
	@PostMapping("list")
	public String listPost(Model model, @ModelAttribute("searchParam") QustnrSearchParam searchParam) {
		return list(model,searchParam);
	}

	/**
	 * 시스템 관리 > 시스템 관리 > 설문 관리 > 등록
	 * @param model
	 * @param searchParam : 검색조건
	 *
	 */
	@GetMapping("create")
	public String create(Model model) {
		return "view:/qustnr/form";
	}

	@PostMapping("create")
	public JsonView create(@RequestBody Qestnar detail) {

		try {
			return JsonViewUtils.success(qustnrService.insertQustnr(detail));
		} catch (NullPointerException e) {
			return JsonViewUtils.failure("등록 중 오류가 발생했습니다.");
		}

	}


	@GetMapping("{qustnrSn}")
	public String edit(Model model, @PathVariable long qustnrSn) {

		model.addAttribute("qestnar", qustnrService.getQustnr(qustnrSn));

		return "view:/qustnr/form";
	}

	@PostMapping("{qustnrSn}")
	public JsonView edit(@RequestBody Qestnar detail, @PathVariable long qustnrSn) {

		detail.setQustnrSn(qustnrSn);

		try {
			return JsonViewUtils.success(qustnrService.editQustnr(detail));
		} catch (NullPointerException e) {
			return JsonViewUtils.failure("수정 중 오류가 발생했습니다.");
		}

	}

	/**
	 * 설문 삭제
	 * @param requestContext
	 * @param listParam
	 * @return
	 */
	@PostMapping("/delete")
	public JsonView deleteQustrn(RequestContext requestContext, ListParam listParam) {

		if (!requestContext.isAjaxRequest()) {
		    throw new NotAjaxRequestException();
		}
		try {
			qustnrService.deleteQustrn(listParam);
			return JsonViewUtils.success();

		} catch (NullPointerException e) {
			return JsonViewUtils.failure("삭제 중 오류가 발생했습니다.");
		}

		//deliveryCompanyService.deleteDeliveryCompanyById(listParam);
	}

	@GetMapping("{qustnrSn}/result")
	public String result(Model model, @PathVariable long qustnrSn) {

		model.addAttribute("total", qustnrService.getQustnrRspnsResultCnt(qustnrSn));
		model.addAttribute("qestnar", qustnrService.getQustnr(qustnrSn));

		return "view:/qustnr/result";
	}

}
