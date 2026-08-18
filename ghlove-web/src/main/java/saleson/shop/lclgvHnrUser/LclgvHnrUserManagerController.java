package saleson.shop.lclgvHnrUser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.common.enumeration.IdType;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.designateddonation.domain.PrjImageExplain;
import saleson.shop.lclgvHnrUser.domain.LclgvHnrUserMng;
import saleson.shop.lclgvHnrUser.support.LclgvHnrUserMngParam;
import saleson.shop.lclgvHnrUser.support.LclgvHnrUserViewHistExcelView;
import saleson.shop.user.LocgovService;

@Controller
@RequestMapping("/opmanager/lclgvHnrUser")
@RequestProperty(title="지자체 명예 사용자관리", layout="default")
public class LclgvHnrUserManagerController {

	private static final Logger log = LoggerFactory.getLogger(LclgvHnrUserManagerController.class);

	@Autowired
	private LclgvHnrUserMngService lclgvHnrUserMngService;

	@Autowired
	CodeService codeService;

	@Autowired
	private LocgovService locgovService;


	/**
	 * 지자체 명예 사용자 목록 조회
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/lclgvHnrUserMng/list")
	public String selectLclgvHnrUserMngList(Model model, RequestContext requestContext, LclgvHnrUserMngParam lclgvHnrUserMngParam) {
		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());
			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
//			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(requestContext.getUser().getUserId());
//			String locgovCd = locgovObj.get("LOCGOV_CODE").toString();
			if (UserUtils.getUser() == null) {
				return ViewUtils.redirect("/opmanager", "로그인 상태가 아닙니다.");
			}
			String locgovCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			return ViewUtils.redirect("/opmanager/lclgvHnrUser/lclgvHnrUserMng/form/"+locgovCd);
		}

		int count = 0;
		Pagination pagination = Pagination.getInstance(count, lclgvHnrUserMngParam.getItemsPerPage());
		lclgvHnrUserMngParam.setPagination(pagination);
		model.addAttribute("lclgvHnrUserMngList", Collections.EMPTY_LIST);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("lclgvHnrUserMngParam",lclgvHnrUserMngParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.view();
	}

	@PostMapping("/lclgvHnrUserMng/list")
	public String serchLclgvHnrUserMngList(Model model, RequestContext requestContext, LclgvHnrUserMngParam lclgvHnrUserMngParam) {
		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());
			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
//			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(requestContext.getUser().getUserId());
//			String locgovCd = locgovObj.get("LOCGOV_CODE").toString();
			if (UserUtils.getUser() == null) {
				return ViewUtils.redirect("/opmanager", "로그인 상태가 아닙니다.");
			}
			String locgovCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			return ViewUtils.redirect("/opmanager/lclgvHnrUser/lclgvHnrUserMng/form/"+locgovCd);
		}

		int count = lclgvHnrUserMngService.selectLclgvHnrUserMngListCnt(lclgvHnrUserMngParam);
		Pagination pagination = Pagination.getInstance(count, lclgvHnrUserMngParam.getItemsPerPage());
		lclgvHnrUserMngParam.setPagination(pagination);
		model.addAttribute("lclgvHnrUserMngList", lclgvHnrUserMngService.selectLclgvHnrUserMngList(lclgvHnrUserMngParam));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("lclgvHnrUserMngParam",lclgvHnrUserMngParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.view();
	}

	/**
	 * 지자체 명예 사용자 목록 - 수정 화면
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping(value = {"/lclgvHnrUserMng/form/{lclgvCd}", "/lclgvHnrUserMng/form"})
	public String selectLclgvHnrUserMngDetail(@PathVariable(required = false) String lclgvCd,
			Model model, LclgvHnrUserMngParam lclgvHnrUserMngParam,  RequestContext requestContext) {

		if(!StringUtils.hasLength(lclgvCd)) {
			if(UserUtils.getUser() != null && (SecurityUtils.hasRole("ROLE_ADMIN_5") || SecurityUtils.hasRole("ROLE_ADMIN_6"))) {
				// 지자체명
				lclgvCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			} else {
				return ViewUtils.redirect("/opmanager", "지자체 정보가 없습니다.");
			}
		}
		lclgvHnrUserMngParam.setLclgvCd(lclgvCd);

		// 이미지 설명
		List<PrjImageExplain> prjImageExplain = lclgvHnrUserMngService.selectImgDescListByPrjId(lclgvHnrUserMngParam.getLclgvCd());

		if(!CollectionUtils.isEmpty(prjImageExplain)) {
			lclgvHnrUserMngParam.setPrjImageExplain(prjImageExplain);
		}

		model.addAttribute("lclgvHnrUserMng", lclgvHnrUserMngService.selectLclgvHnrUserMngDetail(lclgvHnrUserMngParam));
		model.addAttribute("lclgvHnrUserMngParam", lclgvHnrUserMngParam);
		model.addAttribute("hnrUserSlctnSeCdList", CodeUtils.getCodeList("HNR_USER_SLCTN_SE_CD"));	// 지자체코드

		return ViewUtils.getView("/lclgvHnrUser/lclgvHnrUserMng/form");
	}

	/**
	 * 지자체 명예 사용자 목록 - 수정
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/lclgvHnrUserMng/form/update")
	public String updateLclgvHnrUserMng(Model model, LclgvHnrUserMng lclgvHnrUserMng,
			@RequestParam(value="prjImageFiles[]", required=false) MultipartFile[] prjImageFiles
			,  RequestContext requestContext) {
		String msg = "";
		lclgvHnrUserMng.setLastRgtrId(requestContext.getUser().getUserId());
		lclgvHnrUserMng.setHnrUserRwrd(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "hnrUserRwrd"));

		try {
			if (lclgvHnrUserMngService.updateLclgvHnrUserMng(lclgvHnrUserMng, prjImageFiles) > 0) {
					msg = "저장에 성공했습니다.";
			} else {
					msg = "저장에 실패했습니다.";
			}
		} catch (IOException e) {
			log.error(getClass().getName() +  " saveCatalogContent error :: ", e);
			throw new UserException("저장에 실패했습니다.");
		}

		String role = "";
		User user = UserUtils.getUser();
		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());
			if ("ROLE_ADMIN_3".equals(userRole.getAuthority()) || "ROLE_ADMIN_4".equals(userRole.getAuthority()) || "ROLE_ADMIN_1".equals(userRole.getAuthority()) || "ROLE_ADMIN_2".equals(userRole.getAuthority())) {
				role = "mois";
			} else if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
//			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(requestContext.getUser().getUserId());
//			String locgovCd = locgovObj.get("LOCGOV_CODE").toString();
			if (UserUtils.getUser() == null) {
				return ViewUtils.redirect("/opmanager", "로그인 상태가 아닙니다.");
			}
			String locgovCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			return ViewUtils.redirect("/opmanager/lclgvHnrUser/lclgvHnrUserMng/form/"+locgovCd, msg);
		} else {
			return ViewUtils.redirect("/opmanager/lclgvHnrUser/lclgvHnrUserMng/list", msg);
		}

	}

	/**
	 * 지자체 명예 사용자 이미지 배경 삭제
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/lclgvHnrUserMng/form/deleteItemFile")
	@ResponseBody
	public int deleteItemFile(@RequestParam (name = "lclgvCd") String lclgvCd ) {
		return lclgvHnrUserMngService.deleteItemFile(lclgvCd);
	}

	/**
	 * 기부혜택증 열람현황
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/lclgvHnrUserViewHist/list")
	public String lclgvHnrUserViewHist(Model model, RequestContext requestContext, LclgvHnrUserMngParam lclgvHnrUserMngParam) {
		String role = "";
		User user = UserUtils.getUser();

		String today = DateUtils.getToday("yyyyMMdd");

		lclgvHnrUserMngParam.setStartDate(org.apache.commons.lang.StringUtils.defaultIfEmpty(lclgvHnrUserMngParam.getStartDate(), today));
		lclgvHnrUserMngParam.setEndDate(org.apache.commons.lang.StringUtils.defaultIfEmpty(lclgvHnrUserMngParam.getEndDate(), today));

		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());
			if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
//			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(requestContext.getUser().getUserId());
//			String locgovCd = locgovObj.get("LOCGOV_CODE").toString();
			if (UserUtils.getUser() == null) {
				return ViewUtils.redirect("/opmanager", "로그인 상태가 아닙니다.");
			}
			String locgovCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			lclgvHnrUserMngParam.setLclgvCd(locgovCd);

		//	return ViewUtils.redirect("/opmanager/lclgvHnrUser/lclgvHnrUserMng/form/"+locgovCd);
		}

		int count = 0;
		Pagination pagination = Pagination.getInstance(count, lclgvHnrUserMngParam.getItemsPerPage());
		lclgvHnrUserMngParam.setPagination(pagination);
		model.addAttribute("lclgvHnrUserMngList", Collections.EMPTY_LIST);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("lclgvHnrUserMngParam",lclgvHnrUserMngParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.view();
	}

	@PostMapping("/lclgvHnrUserViewHist/list")
	public String searchLclgvHnrUserViewHist(Model model, RequestContext requestContext, LclgvHnrUserMngParam lclgvHnrUserMngParam) {
		String role = "";
		User user = UserUtils.getUser();

		String today = DateUtils.getToday("yyyyMMdd");

		lclgvHnrUserMngParam.setStartDate(org.apache.commons.lang.StringUtils.defaultIfEmpty(lclgvHnrUserMngParam.getStartDate(), today));
		lclgvHnrUserMngParam.setEndDate(org.apache.commons.lang.StringUtils.defaultIfEmpty(lclgvHnrUserMngParam.getEndDate(), today));

		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());
			if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
//			HashMap<String, Object> locgovObj = giveStateService.getLocgovCode(requestContext.getUser().getUserId());
//			String locgovCd = locgovObj.get("LOCGOV_CODE").toString();
			if (UserUtils.getUser() == null) {
				return ViewUtils.redirect("/opmanager", "로그인 상태가 아닙니다.");
			}
			String locgovCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			lclgvHnrUserMngParam.setLclgvCd(locgovCd);

		//	return ViewUtils.redirect("/opmanager/lclgvHnrUser/lclgvHnrUserMng/form/"+locgovCd);
		}

		int count = lclgvHnrUserMngService.lclgvHnrUserViewHistCnt(lclgvHnrUserMngParam);
		Pagination pagination = Pagination.getInstance(count, lclgvHnrUserMngParam.getItemsPerPage());
		lclgvHnrUserMngParam.setPagination(pagination);
		model.addAttribute("lclgvHnrUserMngList", lclgvHnrUserMngService.lclgvHnrUserViewHist(lclgvHnrUserMngParam));
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
		model.addAttribute("lclgvHnrUserMngParam",lclgvHnrUserMngParam);
		model.addAttribute("pagination", pagination);

		return ViewUtils.view();
	}

	/**
	 * 기부혜택증 열람현황 엑셀
	 * @return
	 */
	@GetMapping("/lclgvHnrUserViewHist/list/excel")
	public ModelAndView lclgvHnrUserViewHistExcel(LclgvHnrUserMngParam lclgvHnrUserMngParam, Model model, RequestContext requestContext) {

		String role = "";
		User user = UserUtils.getUser();

		for (UserRole userRole : user.getUserRoles()) {
			log.info("userRole >>> " + userRole.getAuthority());
			if ("ROLE_ADMIN_5".equals(userRole.getAuthority()) || "ROLE_ADMIN_6".equals(userRole.getAuthority())) {	// 지자체담당자(정,부)
				role = "locgov";
			}
		}

		if ("locgov".equals(role)) {
			// 지자체명
			String locgovCd = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			lclgvHnrUserMngParam.setLclgvCd(locgovCd);

		}

		ModelAndView mav = new ModelAndView(new LclgvHnrUserViewHistExcelView());
		mav.addObject("title", "기부혜택증 열람현황 엑셀");
		mav.addObject("list", lclgvHnrUserMngService.lclgvHnrUserViewHist(lclgvHnrUserMngParam));
//		mav.addObject("date", requestContext.getQueryString());
		return mav;
	}

}