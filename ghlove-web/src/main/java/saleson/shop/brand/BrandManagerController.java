package saleson.shop.brand;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import saleson.common.enumeration.IdType;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.brand.domain.Brand;
import saleson.shop.brand.support.BrandParam;
import saleson.shop.user.LocgovService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/opmanager/brand")
@RequestProperty(title="브랜드 관리", layout="default", template="opmanager")
public class BrandManagerController {
	private static final Logger log = LoggerFactory.getLogger(BrandManagerController.class);
	
	@Autowired
	private BrandService brandService;

	@Autowired
	private LocgovService locgovService;
	
	@GetMapping("list")
	public String brandList(BrandParam brandParam, Model model
			, @RequestParam(required = false) String perPageCntStr) {
		int perPageCnt;
		try {
			perPageCnt = Integer.valueOf(perPageCntStr);
			switch (perPageCnt) {
			case 1:
			case 10:
			case 20:
			case 50:
			case 100:
				break;
			default:
				perPageCnt = 10;
				break;
			}
		} catch(RuntimeException e) {
			perPageCnt = 10;
		}
		
		int brandCount = brandService.getBrandCount(brandParam);
		
		Pagination pagination = Pagination.getInstance(brandCount);
		
		pagination.setItemsPerPage(perPageCnt);
		
		brandParam.setPagination(pagination);
		
		List<Brand> brandList = brandService.getBrandList(brandParam);
		
//		List<Code> codeList = CodeUtils.getCodeList("LOCGOV_CODE");		// 지자체 시군구 코드
		
		model.addAttribute("brandCount", StringUtils.numberFormat(String.valueOf(brandCount)));
		model.addAttribute("brandList", brandList);
		model.addAttribute("brandParam", brandParam);
		model.addAttribute("pagination", pagination);
		model.addAttribute("itemsPageCnt", perPageCnt);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드
				
		return ViewUtils.view();
	}
	
	@PostMapping("list")
	public String searchBrandList(BrandParam brandParam, Model model
			, @RequestParam(required = false) String perPageCntStr) {
		int perPageCnt;
		try {
			perPageCnt = Integer.valueOf(perPageCntStr);
			switch (perPageCnt) {
			case 1:
			case 10:
			case 20:
			case 50:
			case 100:
				break;
			default:
				perPageCnt = 10;
				break;
			}
		} catch(RuntimeException e) {
			perPageCnt = 10;
		}
		
		int brandCount = brandService.getBrandCount(brandParam);
		
		Pagination pagination = Pagination.getInstance(brandCount);
		
		pagination.setItemsPerPage(perPageCnt);
		
		brandParam.setPagination(pagination);
		
		List<Brand> brandList = brandService.getBrandList(brandParam);
		
//		List<Code> codeList = CodeUtils.getCodeList("LOCGOV_CODE");		// 지자체 시군구 코드
		
		model.addAttribute("brandCount", StringUtils.numberFormat(String.valueOf(brandCount)));
		model.addAttribute("brandList", brandList);
		model.addAttribute("brandParam", brandParam);
		model.addAttribute("pagination", pagination);
		model.addAttribute("itemsPageCnt", perPageCnt);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드
				
		return ViewUtils.view();
	}
	
	@GetMapping("create")
	public String brandCreate(Brand brand, Model model) {
		
		model.addAttribute("brand", brand);
		
		return ViewUtils.getManagerView("/brand/form");
	}
	
	
	@PostMapping("create")
	public String brandCreateAction(Brand brand, HttpServletRequest request) {
		// 로그인 사용자 지자체 코드 가져오기
		if (ShopUtils.isSellerPage(request.getRequestURL().toString())) {
			brand.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.SELLER_USER));
		} else {
			brand.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));
		}
		
		brandService.insertBrand(brand);
		return ViewUtils.redirect("/opmanager/brand/list", MessageUtils.getMessage("M00288"));	// 등록되었습니다. 
	}
	
	
	@GetMapping("edit/{brandId}")
	public String brandEdit(@PathVariable("brandId") int brandId, Model model) {
		
		Brand brand = brandService.getBrandById(brandId);
			
		model.addAttribute("brand", brand);
		
		/* return ViewUtils.getManagerView("/brand/form"); */
		return ViewUtils.getManagerView("/brand/edit");
	}
	
	
	@GetMapping("removeImg/{brandId}")
	public String removeImg(@PathVariable("brandId") int brandId) {
		
		Brand brand = brandService.getBrandById(brandId);
		brandService.deleteBrandImg(brand);
		
		return ViewUtils.redirect("/opmanager/brand/edit/" + String.valueOf(brandId));
	}
	
	
	@PostMapping("edit/{brandId}")
	public String brandEditAction(@PathVariable("brandId") int brandId, Brand brand) {
		
		brandService.updateBrand(brand);
		
		return ViewUtils.redirect("/opmanager/brand/list/", MessageUtils.getMessage("M00289"));	// 수정되었습니다.
	}
	
	@GetMapping("delete/{brandId}")
	public String brandDelete(@PathVariable("brandId") int brandId) throws Exception {
		Brand brand = brandService.getBrandById(brandId);
		brandService.deleteBrandById(brandId);
		
		brandService.deleteBrandImg(brand);
		
		return ViewUtils.redirect("/opmanager/brand/list", MessageUtils.getMessage("M00205")); //삭제되었습니다. 
	}
	
	
	/**
	 * 팝업리스트 삭제
	 * @param requestContext
	 * @param listParam
	 * @return
	 */
	@PostMapping("delete-list")
	public JsonView deleteListData(RequestContext requestContext, ListParam listParam) {

		if (!requestContext.isAjaxRequest()) { 
		    throw new NotAjaxRequestException();
		}
		
		brandService.deleteBrandData(listParam);
		return JsonViewUtils.success();  
	}
}
