package saleson.shop.seasonalfood;

import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.shop.seasonalfood.domain.SeasonalFood;
import saleson.shop.seasonalfood.support.SeasonalFoodParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/opmanager/seasonal-food")
@RequestProperty(title="제철식품관 관리", layout="default", template="opmanager")
public class SeasonalFoodManagerController {
	
	private static final Logger log = LoggerFactory.getLogger(SeasonalFoodManagerController.class);
	
	@Autowired
	private SeasonalFoodService seasonalFoodService;
	
	// 제철식품관 관리 페이지
	@GetMapping(value="/list")
	public String seasonalKeywordList(Model model, HttpServletRequest request) {
		List<SeasonalFood> list = seasonalFoodService.getSeasonalFood();
		model.addAttribute("seasonFoodData", list);
		return ViewUtils.getView("/seasonal-food/list");
	}
	
	// 제철식품관 수정 팝업
	@RequestProperty(layout="base")
	@GetMapping(value="/popup-month")
	public String seasonalKeywordPopMonth(Model model, HttpServletRequest request) {		
		return ViewUtils.getView("/seasonal-food/popup-month");
	}
	
	// 제철식품관 등록 팝업
	@RequestProperty(layout="base")
	@GetMapping(value="/popup-keyword")
	public String seasonalKeywordPopKeyword(Model model, HttpServletRequest request) {
		return ViewUtils.getView("/seasonal-food/popup-keyword");
	}
		
	// 제철식품관 저장
	@PostMapping(value="/saveSeasonalFood")
	@ResponseBody
	public JsonView saveSeasonalKeywordPopKeyword(Model model, HttpServletRequest request
			, @RequestBody SeasonalFoodParam data) {
		JsonView result = null;
		List<SeasonalFood> params = data.getSeasonalFoods();
		for (SeasonalFood seasonalFood : params) {
			seasonalFood.setSeasonalFoodKeyword(removeInvalidChar(seasonalFood.getSeasonalFoodKeyword()));
		}
		
		try {
			result = JsonViewUtils.success(seasonalFoodService.saveSeasonalFood(params));
		} catch (RuntimeException e) {
//			log.error(e.getMessage());
//			result = JsonViewUtils.failure(MessageUtils.getMessage("저장에 실패했습니다."));		// 저장에 실패했습니다.
			result = JsonViewUtils.failure("저장에 실패했습니다.");		// 저장에 실패했습니다.
		}
		return result;
	}
	
	// 제철식품관 키워드 한글, 영어, 숫자, 콤마 제외한 문자 제거, empty 값 제거
	private String removeInvalidChar(String keyword) {
		if (keyword == null) {
			return "";
		}
		keyword = keyword.replaceAll("[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9,]", "");
		
		String[] keywordArr = keyword.split(",");
		
		List<String> validKeywordList = new ArrayList<>();
		for (String string : keywordArr) {
			if (!StringUtils.isEmpty(string)) {
				validKeywordList.add(string);
			}
		}
		
		return String.join(",", validKeywordList.toArray(new String[validKeywordList.size()]));
	}
	
	
}
