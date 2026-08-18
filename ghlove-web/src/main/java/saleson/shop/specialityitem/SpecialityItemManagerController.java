package saleson.shop.specialityitem;

import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;

import saleson.common.enumeration.IdType;
import saleson.common.utils.UserUtils;
import saleson.shop.specialityitem.domain.SpecialityItem;
import saleson.shop.specialityitem.domain.SpecialityItemManage;
import saleson.shop.specialityitem.support.SpecialityItemParam;
import saleson.shop.user.LocgovService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/opmanager/speciality-item")
@RequestProperty(title="특산물관 관리", layout="default", template="opmanager")
public class SpecialityItemManagerController {

	private static final Logger log = LoggerFactory.getLogger(SpecialityItemManagerController.class);

	@Autowired
	private SpecialityItemService specialityItemService;

	@Autowired
	private LocgovService locgovService;

	// 특산물관 관리 목록 페이지
	@GetMapping(value="/list")
	public String specialityItemList(SpecialityItemParam searchParams, Model model, HttpServletRequest request) {
		if (isUpperAdmin()) {		// 시스템, 행안부 권한
			model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드
			model.addAttribute("searchParams", searchParams);			// 검색 파라미터
			model.addAttribute("specialityItemManageList", Collections.EMPTY_LIST);		// 리스트
			model.addAttribute("specialityItemManageCount", 0);			// 리스트 개수

			return ViewUtils.getView("/speciality-item/list");
		} else {
			searchParams = new SpecialityItemParam();
			searchParams.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));

			List<SpecialityItemManage> specialityItemManageList = specialityItemService.getSpecialityItemManageList(searchParams);
			SpecialityItemManage specialityItemManage;
			if (specialityItemManageList == null || specialityItemManageList.isEmpty()) {
//				specialityItemManage = specialityItemService.insertSpecialityItemManage(searchParams);
				specialityItemManage = new SpecialityItemManage();
				specialityItemManage.setLocgovCode(searchParams.getLocgovCode());
			} else {
				specialityItemManage = specialityItemManageList.get(0);
			}

			return ViewUtils.redirect("/opmanager/speciality-item/form/" + String.valueOf(specialityItemManage.getSpecialityItemManageId()));		// 시스템, 행안부 권한 아니면 수정페이지 이동
		}
	}

	@PostMapping(value="/list")
	public String specialityItemListPost(SpecialityItemParam searchParams, Model model, HttpServletRequest request) {
		if (isUpperAdmin()) {		// 시스템, 행안부 권한
			model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드
			model.addAttribute("searchParams", searchParams);			// 검색 파라미터
			model.addAttribute("specialityItemManageList", specialityItemService.getSpecialityItemManageList(searchParams));		// 리스트
			model.addAttribute("specialityItemManageCount", specialityItemService.getSpecialityItemManageCount());			// 리스트 개수

			return ViewUtils.getView("/speciality-item/list");
		} else {
			searchParams = new SpecialityItemParam();
			searchParams.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));

			List<SpecialityItemManage> specialityItemManageList = specialityItemService.getSpecialityItemManageList(searchParams);
			SpecialityItemManage specialityItemManage;
			if (specialityItemManageList == null || specialityItemManageList.isEmpty()) {
//				specialityItemManage = specialityItemService.insertSpecialityItemManage(searchParams);
				specialityItemManage = new SpecialityItemManage();
				specialityItemManage.setLocgovCode(searchParams.getLocgovCode());
			} else {
				specialityItemManage = specialityItemManageList.get(0);
			}

			return ViewUtils.redirect("/opmanager/speciality-item/form/" + String.valueOf(specialityItemManage.getSpecialityItemManageId()));		// 시스템, 행안부 권한 아니면 수정페이지 이동
		}
	}

	// 특산물관 관리 등록 페이지
	@GetMapping(value="/form/{specialityItemId}")
	public String getSpecialityItem(@PathVariable long specialityItemId, Model model, HttpServletRequest request) {
		SpecialityItemParam searchParams = new SpecialityItemParam();
		searchParams.setSpecialityItemManageId(specialityItemId);
		searchParams.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));

		if (StringUtils.isEmpty(searchParams.getLocgovCode()) && !isUpperAdmin()) {
			throw new PageNotFoundException();
		}

		List<SpecialityItemManage> manageList = specialityItemService.getSpecialityItemManageList(searchParams);

		List<SpecialityItem> itemList = specialityItemService.getSpecialityItemListByParam(searchParams);

		if (manageList != null && !manageList.isEmpty()) {
			model.addAttribute("specialityItemManage", manageList.get(0));
		} else {
			SpecialityItemManage specialityItemManage = new SpecialityItemManage();
			specialityItemManage.setLocgovCode(searchParams.getLocgovCode());
			model.addAttribute("specialityItemManage", specialityItemManage);
		}
		model.addAttribute("specialityItemList", itemList);

		return ViewUtils.getView("/speciality-item/form");
	}


	// 특산물관 저장
	@PostMapping(value="/form/{specialityItemId}")
	public String saveSeasonalKeywordPopKeyword(SpecialityItemManage specialityItemManage, Model model, HttpServletRequest request) {
		try {
			specialityItemManage.setKeywords(removeInvalidChar(specialityItemManage.getKeywords()));
			specialityItemManage.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUserId(), IdType.MANAGER));
			specialityItemService.updateSpecialityItemManage(specialityItemManage);
			return ViewUtils.redirect("/opmanager/speciality-item/form/" + Long.valueOf(specialityItemManage.getSpecialityItemManageId()), MessageUtils.getMessage("M00406"));	// 저장되었습니다.
		} catch (RuntimeException e) {
			log.error("saveSeasonalKeywordPopKeyword error", e);
//			throw new RuntimeException(MessageUtils.getMessage("저장에 실패했습니다."));		// 저장에 실패했습니다.
			throw new RuntimeException("저장에 실패했습니다.");		// 저장에 실패했습니다.
		}
	}


	// 특산물관 키워드 한글, 영어, 숫자, 콤마 제외한 문자 제거, empty 값 제거
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

	private boolean isUpperAdmin() {
		if (SecurityUtils.hasRole("ROLE_ADMIN_1")
				|| SecurityUtils.hasRole("ROLE_ADMIN_2")
				|| SecurityUtils.hasRole("ROLE_ADMIN_3")
				|| SecurityUtils.hasRole("ROLE_ADMIN_4")) {
			return true;
		}
		return false;
	}
}
