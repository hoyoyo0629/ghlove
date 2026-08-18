package saleson.shop.representativebanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.shop.representativebanner.domain.RepresentativeBanner;
import saleson.shop.representativebanner.domain.RepresentativeBannerListParam;
import saleson.shop.user.LocgovService;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/opmanager/representative-banner")
@RequestProperty(title="대표 배너 관리", layout="default")
public class RepresentativeBannerManagerController {
	
	private static final Logger log = LoggerFactory.getLogger(RepresentativeBannerManagerController.class);
	
	@Autowired
	private RepresentativeBannerService representativeBannerService;
	
	@Autowired
	private LocgovService locgovService;	
	
	/**
	 * 목록
	 * @param model
	 * @return
	 */
	@GetMapping("list")
	public String list(Model model, RepresentativeBannerListParam listParam) {
		listParam.setProcessType("ITEM");
		model.addAttribute("bannerList", representativeBannerService.getRepresentativeBannerList(listParam));
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return ViewUtils.getView("/representative-banner/list");
		
	}	
	
	/**
	 * 대표 배너 순서 변경
	 * @param listParam
	 * @return
	 */
	@PostMapping("/change-ordering")
	public JsonView changeGroupOrdering(RepresentativeBannerListParam listParam) {
		representativeBannerService.updateRepresentativeBanneOrdering(listParam);
		return JsonViewUtils.success();
	}	
	
	/**
	 * 대표 배너 등록/수정
	 * @param model
	 * @param representativeBannerId
	 * @return
	 */
	@GetMapping("form/{representativeBannerId}")
	public String form(Model model, @PathVariable("representativeBannerId") int representativeBannerId) {
		
		model.addAttribute("banner", representativeBannerService.getRepresentativeBannerInfo(representativeBannerId));
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		
		return ViewUtils.getView("/representative-banner/form");
		
	}	
	
	/**
	 * 대표 배너 등록/수정 처리
	 * @param model
	 * @param representativeBannerId
	 * @param representativeBanner
	 * @return
	 */
	@PostMapping("form/{representativeBannerId}")
	public String formAction(Model model, @PathVariable("representativeBannerId") int representativeBannerId,
			RepresentativeBanner representativeBanner) {
		representativeBanner.setProcessType("ITEM");
		representativeBanner.setRepresentativeBannerId(representativeBannerId);
		representativeBannerService.editRepresentativeBanner(representativeBanner);
		
		return ViewUtils.redirect("/opmanager/representative-banner/list", MessageUtils.getMessage("M00406"));	// 저장되었습니다.
	}	
	
	/**
	 * 대표 배너 삭제
	 * @param representativeBannerId
	 * @param deleteFlag
	 * @return
	 */
	@PostMapping("/delete")
	public JsonView deleteRepresentativeBanner(@RequestParam("representativeBannerId") int representativeBannerId, 
			@RequestParam("deleteFlag") String deleteFlag, RepresentativeBanner representativeBanner) {
		representativeBanner.setRepresentativeBannerId(representativeBannerId);
		representativeBanner.setDeleteFlag(deleteFlag);
		representativeBannerService.deleteRepresentativeBanner(representativeBanner);
		
		return JsonViewUtils.success();
	}
}
