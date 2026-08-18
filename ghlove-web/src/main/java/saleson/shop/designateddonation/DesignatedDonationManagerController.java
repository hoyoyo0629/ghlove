package saleson.shop.designateddonation;
import saleson.shop.user.LocgovService;
import saleson.shop.user.PersonInChargeService;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import saleson.common.enumeration.IdType;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.code.domain.Code;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.domain.DesignatedDonationNotice;
import saleson.shop.designateddonation.domain.DesignatedPart;
import saleson.shop.designateddonation.domain.DesignatedStat;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequest;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequestResult;
import saleson.shop.designateddonation.domain.DsgncntrConfirmLogParam;
import saleson.shop.designateddonation.domain.PrjImage;
import saleson.shop.designateddonation.domain.PrjImageExplain;
import saleson.shop.designateddonation.support.DesignatedDonatioinExcelView;
import saleson.shop.designateddonation.support.DesignatedDonationNoticeParam;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.designateddonation.support.DesignatedPartParam;
import saleson.shop.designateddonation.support.DsgnCntrManagerRequestSearchParam;
import saleson.shop.designateddonation.support.LocgovSearchParam;
import saleson.shop.representativebanner.RepresentativeBannerService;
import saleson.shop.representativebanner.domain.RepresentativeBanner;
import saleson.shop.representativebanner.domain.RepresentativeBannerListParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/opmanager/designated-donation")
@RequestProperty(title="지정기부", layout="default", template="opmanager")
public class DesignatedDonationManagerController {

	private static final Logger log = LoggerFactory.getLogger(DesignatedDonationManagerController.class);

	@Autowired
	private LocgovService locgovService;	// 지자체 관리

	@Autowired
	private DesignatedDonationService designatedDonationService;

	@Autowired
	private PersonInChargeService personInChargeService;



	/**
	 * 지정기부 목록 조회
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@GetMapping("/list")
	public String selectDesignatedDonationList(Model model, RequestContext requestContext, DesignatedDonationSearchParam designatedDonationSearchParam) {
		long partId = designatedDonationService.checkDsgncntrAuthPartInfo();
		if (partId == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}

		designatedDonationSearchParam.setDsgncntrPartId(partId);

		LocalDate now = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		if (!StringUtils.hasLength(designatedDonationSearchParam.getPrjStDt()) && !StringUtils.hasLength(designatedDonationSearchParam.getPrjEdDt())) {
			designatedDonationSearchParam.setPrjStDt(format.format(now));
			designatedDonationSearchParam.setPrjEdDt(format.format(now));
		} else if (!StringUtils.hasLength(designatedDonationSearchParam.getPrjStDt())) {
			designatedDonationSearchParam.setPrjStDt(designatedDonationSearchParam.getPrjEdDt());
		} else if (!StringUtils.hasLength(designatedDonationSearchParam.getPrjEdDt())) {
			designatedDonationSearchParam.setPrjEdDt(designatedDonationSearchParam.getPrjStDt());
		}

		if (!StringUtils.hasLength(designatedDonationSearchParam.getUpperLocgovCode()) && StringUtils.hasLength(designatedDonationSearchParam.getLocgovCode())) {
			designatedDonationSearchParam.setUpperLocgovCode(locgovService.getUpperLocgovCode(designatedDonationSearchParam.getLocgovCode()));
		}


		List<DesignatedDonation> designatedDonationList = Collections.EMPTY_LIST;

		LocgovSearchParam locgovSearchParam = new LocgovSearchParam();
		locgovSearchParam.setShLocgovCode(designatedDonationSearchParam.getLocgovCode());
		locgovSearchParam.setShWdr(designatedDonationSearchParam.getUpperLocgovCode());
		locgovSearchParam.setFrDt(designatedDonationSearchParam.getPrjStDt());
		locgovSearchParam.setToDt(designatedDonationSearchParam.getPrjEdDt());
		String bsnsType = designatedDonationSearchParam.getBsnsType();
		if (!StringUtils.hasLength(bsnsType)) {
			bsnsType = "0";
		}
		locgovSearchParam.setBsnsType(bsnsType);
		String prjStatus = designatedDonationSearchParam.getPrjStatus();
		if (!StringUtils.hasLength(prjStatus)) {
			prjStatus = "0";
		}
		locgovSearchParam.setPrjStatus(prjStatus);
		locgovSearchParam.setDisplayFlag(designatedDonationSearchParam.getDisplayFlag());
		locgovSearchParam.setSearchKeyword(designatedDonationSearchParam.getSearchKeyword());

		DesignatedStat designatedStat = new DesignatedStat();

		model.addAttribute("designatedDonationList", designatedDonationList);
		model.addAttribute("designatedDonationSearchParam", designatedDonationSearchParam);
		model.addAttribute("bsnsTypeList", designatedDonationService.getDesignatedDonationBsnsTypes());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("pagination", designatedDonationSearchParam.getPagination());
		model.addAttribute("designatedStat", designatedStat);
		return ViewUtils.view();
	}

	/**
	 * 지정기부 목록 조회
	 * @param model
	 * @param requestContext
	 * @return
	 */
	@PostMapping("/list")
	public String searchSelectDesignatedDonationList(Model model, RequestContext requestContext, DesignatedDonationSearchParam designatedDonationSearchParam) {
		long partId = designatedDonationService.checkDsgncntrAuthPartInfo();
		if (partId == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}

		designatedDonationSearchParam.setDsgncntrPartId(partId);

		LocalDate now = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		if (!StringUtils.hasLength(designatedDonationSearchParam.getPrjStDt()) && !StringUtils.hasLength(designatedDonationSearchParam.getPrjEdDt())) {
			designatedDonationSearchParam.setPrjStDt(format.format(now));
			designatedDonationSearchParam.setPrjEdDt(format.format(now));
		} else if (!StringUtils.hasLength(designatedDonationSearchParam.getPrjStDt())) {
			designatedDonationSearchParam.setPrjStDt(designatedDonationSearchParam.getPrjEdDt());
		} else if (!StringUtils.hasLength(designatedDonationSearchParam.getPrjEdDt())) {
			designatedDonationSearchParam.setPrjEdDt(designatedDonationSearchParam.getPrjStDt());
		}

		if (!StringUtils.hasLength(designatedDonationSearchParam.getUpperLocgovCode()) && StringUtils.hasLength(designatedDonationSearchParam.getLocgovCode())) {
			designatedDonationSearchParam.setUpperLocgovCode(locgovService.getUpperLocgovCode(designatedDonationSearchParam.getLocgovCode()));
		}


		List<DesignatedDonation> designatedDonationList = designatedDonationService.selectDesignatedDonationList(designatedDonationSearchParam);

		LocgovSearchParam locgovSearchParam = new LocgovSearchParam();
		locgovSearchParam.setShLocgovCode(designatedDonationSearchParam.getLocgovCode());
		locgovSearchParam.setShWdr(designatedDonationSearchParam.getUpperLocgovCode());
		locgovSearchParam.setFrDt(designatedDonationSearchParam.getPrjStDt());
		locgovSearchParam.setToDt(designatedDonationSearchParam.getPrjEdDt());
		String bsnsType = designatedDonationSearchParam.getBsnsType();
		if (!StringUtils.hasLength(bsnsType)) {
			bsnsType = "0";
		}
		locgovSearchParam.setBsnsType(bsnsType);
		String prjStatus = designatedDonationSearchParam.getPrjStatus();
		if (!StringUtils.hasLength(prjStatus)) {
			prjStatus = "0";
		}
		locgovSearchParam.setPrjStatus(prjStatus);
		locgovSearchParam.setDisplayFlag(designatedDonationSearchParam.getDisplayFlag());
		locgovSearchParam.setSearchKeyword(designatedDonationSearchParam.getSearchKeyword());

		DesignatedStat designatedStat = designatedDonationService.selectDesignatedLocgovStat(locgovSearchParam);

		model.addAttribute("designatedDonationList", designatedDonationList);
		model.addAttribute("designatedDonationSearchParam", designatedDonationSearchParam);
		model.addAttribute("bsnsTypeList", designatedDonationService.getDesignatedDonationBsnsTypes());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("pagination", designatedDonationSearchParam.getPagination());
		model.addAttribute("designatedStat", designatedStat);
		return ViewUtils.view();
	}

	/**
	 * 지정기부 수정
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/form/{prjId}")
	public String selectDesignatedDonationInfo(@PathVariable("prjId") long prjId, RequestContext requestContext, Model model) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}

		DesignatedDonation designatedDonation = new DesignatedDonation();

		designatedDonation.setPrjId(prjId);
		model.addAttribute("adminRole", personInChargeService.getLoginUserAdminAuthority());
		invokeItemForm(designatedDonation, "edit", model);

		return ViewUtils.view();
	}

	/**
	 * 지정기부 수정 처리
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/form/{prjId}")
	public String updateDesignatedDonationInfo(
						RequestContext requestContext
						, @PathVariable("prjId") long prjId
						, DesignatedDonation designatedDonation,
						@RequestParam(value="prjImageFiles[]", required=false) MultipartFile[] prjImageFiles) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		designatedDonation.setPrjCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "prjCn"));	// pjpark93 2023-11-29 smarteditor의 내용 값을 화면에서 encdoe하였으므로 decode 해서 db에 저장해야함

		designatedDonation.setPrjId(prjId);
		try {
			designatedDonationService.saveDesignatedDonationInfo(designatedDonation, prjImageFiles);
		} catch (OpRuntimeException e) {
			throw new UserException(e.getErrorMessage());
		}

		return ViewUtils.redirect("/opmanager/designated-donation/list", MessageUtils.getMessage("M00289"));// 수정되었습니다.
	}

	/**
	 * 지정기부 등록
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@GetMapping("/form")
	public String makeDesignatedDonationInfo(DesignatedDonation designatedDonation, Model model) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}

		//model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		//model.addAttribute("business_type", CodeUtils.getCodeList("BUSINESS_TYPE"));
		//model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		model.addAttribute("adminRole", personInChargeService.getLoginUserAdminAuthority());

		invokeItemForm(designatedDonation, "create", model);
		//SecurityUtils.hasRole("ROLE_ADMIN_6"); boolean

		return ViewUtils.view();
	}

	/**
	 * 지정기부 등록 처리
	 * @param requestContext
	 * @param model
	 * @return
	 */
	@PostMapping("/form")
	public String insertDesignatedDonationInfo(RequestContext requestContext
			,DesignatedDonation designatedDonation,
			@RequestParam(value="prjImageFiles[]", required=false) MultipartFile[] prjImageFiles) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		designatedDonation.setPrjCn(ShopUtils.getStringParamDecodeUtf8(requestContext.getRequest(), "prjCn"));	// pjpark93 2023-11-29 smarteditor의 내용 값을 화면에서 encdoe하였으므로 decode 해서 db에 저장해야함
		designatedDonation.setPrjId((long) 0);
		designatedDonationService.saveDesignatedDonationInfo(designatedDonation , prjImageFiles);

		return ViewUtils.redirect("/opmanager/designated-donation/list", MessageUtils.getMessage("M00288"));// 등록되었습니다.
	}

	/**
	 * 지정기부 등록 & 수정 폼
	 * @param DesignatedDonation
	 * @param model
	 * @return
	 */
	protected DesignatedDonation invokeItemForm(DesignatedDonation designatedDonation, String mode, Model model) {

//		long prjId = designatedDonation.getPrjId();
//
//		String loc = new String();
//		StringBuilder gov = new StringBuilder();
//		String locgov = new String();
//		String locgov2 = new String();



		if ("create".equals(mode)) { //지정기부 신규 등록 일 경우
			//접속 관리자 정보로 지역 코드 세팅
			if (UserUtils.hasLocgovManagerRole()) {
				designatedDonation.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
				designatedDonation.setUpperLocgovCode(designatedDonation.getLocgovCode().substring(0, 2)+"000");
				if (UserUtils.hasDsgncntrManagerRole()) {
					long partId = designatedDonationService.checkDsgncntrAuthPartInfo();
					designatedDonation.setDsgncntrPartId(partId);
				}

				DesignatedPartParam partParam = new DesignatedPartParam();
				partParam.setLocgovCode(designatedDonation.getLocgovCode());
				partParam.setUseYn("Y");
				partParam.setItemsPerPage(Integer.MAX_VALUE);
				List<DesignatedPart> partList = designatedDonationService.selectDsgncntrPartMngList(partParam);
				model.addAttribute("dsgncntrPartList", partList);
			}
		} else if ("edit".equals(mode)) { //지정기부 수정 일 경우
			DesignatedDonationSearchParam param = new DesignatedDonationSearchParam();
			param.setPrjId(designatedDonation.getPrjId());
			designatedDonation = designatedDonationService.selectDesignatedDonationDetail(param);

			if (designatedDonation == null || designatedDonation.getPrjId() == 0) {
				throw new UserException("특정사업기부 사업 정보가 없습니다.");
			}

			if (UserUtils.hasLocgovManagerRole()) {
				if ( (UserUtils.getUser() == null || CommonUtils.longNvl(UserUtils.getUser().getUserId()) == 0)
						|| !CommonUtils.dataNvl(designatedDonation.getLocgovCode()).equals(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER))

					) {
					throw new UserException("지자체 정보가 일치하지 않습니다.");
				}
			}

			// 이미지 설명
			List<PrjImageExplain> prjImageExplain = designatedDonationService.selectImgDescListByPrjId(designatedDonation.getPrjId());

			if(!CollectionUtils.isEmpty(prjImageExplain)) {
				designatedDonation.setPrjImageExplain(prjImageExplain);
			}

			DesignatedPartParam partParam = new DesignatedPartParam();
			partParam.setLocgovCode(designatedDonation.getLocgovCode());
			partParam.setUseYn("Y");
			partParam.setItemsPerPage(Integer.MAX_VALUE);
			List<DesignatedPart> partList = designatedDonationService.selectDsgncntrPartMngList(partParam);
			model.addAttribute("dsgncntrPartList", partList);
		}
		model.addAttribute("mode", mode);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("business_type", CodeUtils.getCodeList("BUSINESS_TYPE"));
		model.addAttribute("designatedDonation", designatedDonation);

		return designatedDonation;
	}

	/**
	 * 지정기부 지자체별 통계
	 * @param designatedDonationSearchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/analysis/locgov")
	public String locgovStat(Model model, @ModelAttribute("locgovSearchParam") LocgovSearchParam locgovSearchParam) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		String today = DateUtils.getToday("yyyyMMdd");
		if (StringUtils.isEmpty(locgovSearchParam.getFrDt())) {
			locgovSearchParam.setFrDt(today);
		}

		if (StringUtils.isEmpty(locgovSearchParam.getToDt())) {
			locgovSearchParam.setToDt(today);
		}

		if(StringUtils.isEmpty(locgovSearchParam.getShWdr())) {
			locgovSearchParam.setShWdr("");
		}

		if(StringUtils.isEmpty(locgovSearchParam.getShLocgovCode())) {
			locgovSearchParam.setShLocgovCode("");
		}

		if(StringUtils.isEmpty(locgovSearchParam.getBsnsType())) {
			locgovSearchParam.setBsnsType("0");
		}

		if(StringUtils.isEmpty(locgovSearchParam.getPrjStatus())) {
			locgovSearchParam.setPrjStatus("0");
		}

		int count = 0;

		Pagination pagination = Pagination.getInstance(count, locgovSearchParam.getItemsPerPage());
		locgovSearchParam.setPagination(pagination);

		DesignatedStat summaryData = new DesignatedStat();
		List<DesignatedStat> list = Collections.EMPTY_LIST;


		model.addAttribute("summaryData", summaryData);
		model.addAttribute("list", list);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		model.addAttribute("locgovSearchParam", locgovSearchParam);
		model.addAttribute("bsnsTypeList", designatedDonationService.getDesignatedDonationBsnsTypes());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));

		return ViewUtils.view();
	}
	@PostMapping("/analysis/locgov")
	public String searchLocgovStat(Model model, @ModelAttribute("locgovSearchParam") LocgovSearchParam locgovSearchParam) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		String today = DateUtils.getToday("yyyyMMdd");
		if (StringUtils.isEmpty(locgovSearchParam.getFrDt())) {
			locgovSearchParam.setFrDt(today);
		}

		if (StringUtils.isEmpty(locgovSearchParam.getToDt())) {
			locgovSearchParam.setToDt(today);
		}

		if(StringUtils.isEmpty(locgovSearchParam.getShWdr())) {
			locgovSearchParam.setShWdr("");
		}

		if(StringUtils.isEmpty(locgovSearchParam.getShLocgovCode())) {
			locgovSearchParam.setShLocgovCode("");
		}

		if(StringUtils.isEmpty(locgovSearchParam.getBsnsType())) {
			locgovSearchParam.setBsnsType("0");
		}

		if(StringUtils.isEmpty(locgovSearchParam.getPrjStatus())) {
			locgovSearchParam.setPrjStatus("0");
		}

		int count = designatedDonationService.selectDesignatedLocgovCntrStatCount(locgovSearchParam);

		Pagination pagination = Pagination.getInstance(count, locgovSearchParam.getItemsPerPage());
		locgovSearchParam.setPagination(pagination);

		DesignatedStat summaryData = designatedDonationService.selectDesignatedLocgovStat(locgovSearchParam);
		List<DesignatedStat> list = designatedDonationService.selectDesignatedLocgovCntrStat(locgovSearchParam);


		model.addAttribute("summaryData", summaryData);
		model.addAttribute("list", list);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		model.addAttribute("locgovSearchParam", locgovSearchParam);
		model.addAttribute("bsnsTypeList", designatedDonationService.getDesignatedDonationBsnsTypes());
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));

		return ViewUtils.view();
	}

	/**
	 * 지정기부 월별 통계
	 * @param designatedDonationSearchParam
	 * @param model
	 * @return
	 */
	@GetMapping("/analysis/month")
	public String monthStat(Model model, @ModelAttribute("locgovSearchParam") LocgovSearchParam locgovSearchParam) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		model.addAttribute("locgovSearchParam", locgovSearchParam);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));

		if (StringUtils.isEmpty(locgovSearchParam.getSelYear())) {
			LocalDate now = LocalDate.now();
			locgovSearchParam.setSelYear(String.valueOf(now.getYear()));
		}

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		return ViewUtils.view();
	}

	@PostMapping("/analysis/month")
	public String searchMonthStat(Model model, @ModelAttribute("locgovSearchParam") LocgovSearchParam locgovSearchParam) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		model.addAttribute("locgovSearchParam", locgovSearchParam);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));

		if (StringUtils.isEmpty(locgovSearchParam.getSelYear())) {
			LocalDate now = LocalDate.now();
			locgovSearchParam.setSelYear(String.valueOf(now.getYear()));
		}

		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));
		return ViewUtils.view();
	}

	/**
	 * 지정기부 이미지를 삭제한다.
	 * @param requestContext
	 * @param dsgncntrPrjImageId
	 * @return
	 */
	@PostMapping("delete-prj-details-image")
	public JsonView deletePrjDetailsImage(RequestContext requestContext, @RequestParam("dsgncntrPrjImageId") int itemImageId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}

		designatedDonationService.deletePrjImageById(itemImageId);

		return JsonViewUtils.success();
	}

	/**
	 * 지정기부 월별 사업별 갬페인 진행건 비율 조회(사업별 갬페인 진행건 비율 조회 ajax)
	 * @param locgovSearchParam : 검색조건
	 * @return JsonView : 사업구분, 취약계층, 문화/예술, 자원봉사, 복리증진, 합계
	 */
	@PostMapping("/analysis/month/campaign")
	public JsonView designatedLocgovMonthCampaign(RequestContext requestContext, LocgovSearchParam locgovSearchParam) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}
		return JsonViewUtils.success(designatedDonationService.selectDesignatedLocgovMonthCampaign(locgovSearchParam));
	}

	/**
	 * 지정기부 월별 모금액 비율(모금액 비율 ajax)
	 * @param locgovSearchParam : 검색조건
	 * @return JsonView : 목표모금액, 취약계층, 문화/예술, 자원봉사, 복리증진, 합계
	 */
	@PostMapping("/analysis/month/amountraised")
	public JsonView designatedLocgovMonthAmountRaised(RequestContext requestContext, LocgovSearchParam locgovSearchParam) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}
		return JsonViewUtils.success(designatedDonationService.selectDesignatedLocgovMonthAmountRaised(locgovSearchParam));
	}

	/**
	 * 지정기부 월별 모금액 추이(모금액 추이 ajax)
	 * @param locgovSearchParam : 검색조건
	 * @return JsonView : 월별 목표금액, 모금액, 참여자수, 캠페인 건수
	 */
	@PostMapping("/analysis/month/amount")
	public JsonView selectDesignatedLocgovMonthAmount(RequestContext requestContext, LocgovSearchParam locgovSearchParam) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}
		return JsonViewUtils.success(designatedDonationService.selectDesignatedLocgovMonthAmount(locgovSearchParam));
	}



	@Autowired
	private RepresentativeBannerService representativeBannerService;


	/**
	 * 배너 목록
	 * @param model
	 * @return
	 */
	@GetMapping("/banner/list")
	public String list(Model model, RepresentativeBannerListParam listParam) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		listParam.setProcessType("DESIGNATED_DONATION");
		model.addAttribute("bannerList", representativeBannerService.getRepresentativeBannerList(listParam));
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());
		return ViewUtils.getView("/designated-donation/banner/list");

	}

	/**
	 * 대표 배너 순서 변경
	 * @param listParam
	 * @return
	 */
	@PostMapping("/banner/change-ordering")
	public JsonView changeGroupOrdering(RepresentativeBannerListParam listParam) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}
		representativeBannerService.updateRepresentativeBanneOrdering(listParam);
		return JsonViewUtils.success();
	}

	/**
	 * 대표 배너 등록/수정
	 * @param model
	 * @param representativeBannerId
	 * @return
	 */
	@GetMapping("/banner/form/{representativeBannerId}")
	public String form(Model model, @PathVariable("representativeBannerId") int representativeBannerId) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}

		model.addAttribute("banner", representativeBannerService.getRepresentativeBannerInfo(representativeBannerId));
		model.addAttribute("adminRole", locgovService.getLoginUserAdminRoleCheck());

		return ViewUtils.getView("/designated-donation/banner/form");

	}

	/**
	 * 대표 배너 등록/수정 처리
	 * @param model
	 * @param representativeBannerId
	 * @param representativeBanner
	 * @return
	 */
	@PostMapping("/banner/form/{representativeBannerId}")
	public String formAction(Model model, @PathVariable("representativeBannerId") int representativeBannerId,
			RepresentativeBanner representativeBanner) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		representativeBanner.setProcessType("DESIGNATED_DONATION");
		representativeBanner.setRepresentativeBannerId(representativeBannerId);

		if (representativeBanner != null) {
			representativeBannerService.editRepresentativeBanner(representativeBanner);
		}

		return ViewUtils.redirect("/opmanager/designated-donation/banner/list", MessageUtils.getMessage("M00406"));	// 저장되었습니다.
	}

	/**
	 * 대표 배너 삭제
	 * @param representativeBannerId
	 * @param deleteFlag
	 * @return
	 */
	@PostMapping("/banner/delete")
	public JsonView deleteRepresentativeBanner(@RequestParam("representativeBannerId") int representativeBannerId,
			@RequestParam("deleteFlag") String deleteFlag, RepresentativeBanner representativeBanner) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}
		representativeBanner.setRepresentativeBannerId(representativeBannerId);
		representativeBanner.setDeleteFlag(deleteFlag);
		representativeBannerService.deleteRepresentativeBanner(representativeBanner);

		return JsonViewUtils.success();
	}

	/**
	 * 목록데이터 상태변경
	 * @param flag
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
	@PostMapping("list/update-label/{flag}")
	public JsonView updateListDataByLabel(@PathVariable("flag") String flag, RequestContext requestContext, DesignatedDonation designateddonation) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}
		if (UserUtils.hasMasterManagerRole() || UserUtils.hasLocgovManagerRole()) {
			if (flag.equals("2") || flag.equals("9")) {//모금상태값변경
				if (UserUtils.hasDsgncntrManagerRole()) {			// 지정기부 권한일 경우 승인 처리 필요
					if ("2".equals(flag)) {
						return JsonViewUtils.failure("권한이 없습니다.(승인 처리 필요)");
					}
				}
				designateddonation.setPrjStatus(flag);
			} else {//공개비공개여부
				designateddonation.setDisplayFlag(flag);
			}
		} else {
			return JsonViewUtils.failure("권한이 없습니다.");
		}

		designatedDonationService.updateListDataByLabel(designateddonation);

		return JsonViewUtils.success();
	}

	@PostMapping("bsnsSubType")
	public @ResponseBody List<Code> getBsnsSubType(@RequestParam(name="code", defaultValue = "0") String code, Model model) {
		return designatedDonationService.getDesignatedDonationBsnsSubTypes(code);
	}


	/**
	 * 지정기부 공지내역 목록 조회
	 * @param model
	 * @param requestContext
	 * @param param
	 * @return
	 */
	@GetMapping("/list/notice/{prjId}")
	public String selectDesignatedDonationNoticeList(Model model, RequestContext requestContext, DesignatedDonationSearchParam param) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		DesignatedDonation designatedDonation = designatedDonationService.selectDesignatedDonationDetail(param);

		if (param.getPage() == 0) {
			param.setPage(1);
		}
		if (param.getItemsPerPage() < 10) {
			param.setItemsPerPage(10);
		}

		List<DesignatedDonationNotice> list = designatedDonationService.selectPrjNoticeList(param);

		model.addAttribute("designatedDonation", designatedDonation);
		model.addAttribute("prjNoticeList", list);

		return ViewUtils.getView("/designated-donation/notice/list");
	}

	/**
	 * 지정기부 공지내역 목록 조회
	 * @param model
	 * @param requestContext
	 * @param param
	 * @return
	 */
	@PostMapping("/list/notice/{prjId}")
	public String searchSelectDesignatedDonationNoticeList(Model model, RequestContext requestContext, DesignatedDonationSearchParam param) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		DesignatedDonation designatedDonation = designatedDonationService.selectDesignatedDonationDetail(param);

		if (param.getPage() == 0) {
			param.setPage(1);
		}
		if (param.getItemsPerPage() < 10) {
			param.setItemsPerPage(10);
		}

		List<DesignatedDonationNotice> list = designatedDonationService.selectPrjNoticeList(param);

		model.addAttribute("designatedDonation", designatedDonation);
		model.addAttribute("prjNoticeList", list);

		return ViewUtils.getView("/designated-donation/notice/list");
	}

	/**
	 * 지정기부 공지내역 등록 화면
	 * @param model
	 * @param requestContext
	 * @param param
	 * @return
	 */
	@GetMapping("/list/notice/form/{prjId}/{prjNoticeId}")
	public String createDesignatedDonationNotice(Model model, RequestContext requestContext, DesignatedDonationNoticeParam param) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		if (designatedDonationService.hasDesignatedDonationAuth(param.getPrjId())) {
			if (param.getPrjNoticeId() == 0) {
				DesignatedDonationNotice notice = new DesignatedDonationNotice();
				notice.setPrjId(param.getPrjId());
				notice.setPrjNoticeId(0);
				model.addAttribute("prjNotice", notice);
			} else {
				model.addAttribute("prjNotice", designatedDonationService.selectPrjNoticeDetail(param));
			}

			return ViewUtils.getView("/designated-donation/notice/form");
		} else {
			return ViewUtils.redirect("", "권한이 없습니다.");
		}
	}


	/**
	 * 지정기부 공지내역 저장
	 * @param model
	 * @param requestContext
	 * @param param
	 * @return
	 */
	@PostMapping("/list/notice/save")
	public String saveDesignatedDonationNoticeDetail(Model model, RequestContext requestContext
													, @ModelAttribute(value="prjNotice") DesignatedDonationNotice designatedDonationNotice) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		if (designatedDonationService.hasDesignatedDonationAuth(designatedDonationNotice.getPrjId())) {
			long prjNoticeId = designatedDonationNotice.getPrjNoticeId();

			designatedDonationService.savePrjNoticeInfo(designatedDonationNotice);

			String msg;
			if (prjNoticeId > 0) {
				msg = MessageUtils.getMessage("M00289");// 수정되었습니다.
			} else {
				msg = MessageUtils.getMessage("M00288");// 등록되었습니다.
			}

			return ViewUtils.redirect("/opmanager/designated-donation/list/notice/" + designatedDonationNotice.getPrjId(), msg);// 수정되었습니다.
		} else {
			return ViewUtils.redirect("", "권한이 없습니다.");
		}
	}


	/**
	 * 지정기부 공지내역 파일삭제
	 * @param model
	 * @param requestContext
	 * @param param
	 * @return
	 */
	@PostMapping("/list/notice/delete-file")
	public JsonView deletePrjNoticeFile(Model model, RequestContext requestContext
													, DesignatedDonationNoticeParam designatedDonationNoticeParam) {
		Map<String, String> result = new HashMap<>();
		String msg = "";
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			result.put("result", "F");
			result.put("msg", msg);
			return JsonViewUtils.success(result);
		}
		if (designatedDonationService.hasDesignatedDonationAuth(designatedDonationNoticeParam.getPrjId())) {

			try {
				int resultCnt = designatedDonationService.deletePrjNoticeFile(designatedDonationNoticeParam);
				if (resultCnt == 1) {
					result.put("result", "S");
					return JsonViewUtils.success(result);
				}
			} catch (OpRuntimeException e) {
				msg = e.getErrorMessage();
			}

		} else {
			msg = "권한이 없습니다.";
		}
		result.put("result", "F");
		result.put("msg", msg);
		return JsonViewUtils.success(result);
	}


	/**
	 * 지정기부 공지내역 파일 다운로드
	 * @param model
	 * @param requestContext
	 * @param param
	 * @return
	 */
	@GetMapping("/list/notice/download-file")
	public ResponseEntity<byte[]> downloadPrjNoticeFile(Model model, RequestContext requestContext
													, DesignatedDonationNoticeParam designatedDonationNoticeParam) {
		return designatedDonationService.downloadPrjNoticeFile(designatedDonationNoticeParam);
	}

	/**
	 * 관리자 권한 승인관리 목록 조회
	 * @param model
	 * @param userId
	 * @return
	 */
	@GetMapping("/request/list")
	@RequestProperty(title = "관리자권한요청", layout = "default", template="opmanager")
	public String managerRequestList(DsgnCntrManagerRequestSearchParam searchParam, Model model) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}

		model.addAttribute("list"			, Collections.EMPTY_LIST);
		model.addAttribute("searchParam"	, searchParam);

		return ViewUtils.getView("/designated-donation/request/list");
	}

	/**
	 * 관리자 권한 승인관리 목록 조회
	 * @param model
	 * @param userId
	 * @return
	 */
	@PostMapping("/request/list")
	@RequestProperty(title = "관리자권한요청", layout = "default", template="opmanager")
	public String searchManagerRequestList(DsgnCntrManagerRequestSearchParam searchParam, Model model) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}

		model.addAttribute("list"			, designatedDonationService.getManagerRequestListByParam(searchParam));
		model.addAttribute("searchParam"	, searchParam);

		return ViewUtils.getView("/designated-donation/request/list");
	}

	/**
	 * 관리자 권한 승인관리 상세 정보 조회 (팝업창)
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("/request/popup/details/{userId}/{reqstSn}")
	public String managerRequestDetails(Model model,
			@PathVariable("userId") Long userId,
			@PathVariable("reqstSn") Integer reqstSn) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}

		// Path 값 검색조건 추가
		DsgnCntrManagerRequestSearchParam searchParam = new DsgnCntrManagerRequestSearchParam();
		searchParam.setUserId(userId);
		searchParam.setReqstSn(reqstSn);

		// 권한 없는 접근일 경우 상세 정보 비노출
		if(designatedDonationService.getManagerRequestDetailsAuthCount(searchParam) > 0) {
			DsgnCntrManagerRequest dsgnCntrManagerRequest = designatedDonationService.getManagerRequestDetails(searchParam);
			model.addAttribute("details", dsgnCntrManagerRequest);

			DesignatedPartParam partParam = new DesignatedPartParam();
			partParam.setUseYn("Y");
			partParam.setItemsPerPage(Integer.MAX_VALUE);
			partParam.setLocgovCode(dsgnCntrManagerRequest.getLocgovCode());

			model.addAttribute("partList", designatedDonationService.selectDsgncntrPartMngList(partParam));
		}

		return ViewUtils.getView("/designated-donation/request/manager-request-details");
	}

	/**
	 * 관리자 권한 승인관리 확인 - 승인/거절
	 * @param managerRequest
	 * @return
	 */
	@PostMapping("/request/confirm")
	public JsonView managerRequestConfirmProcess(DsgnCntrManagerRequest managerRequest) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}

		// 결과값
		DsgnCntrManagerRequestResult result = new DsgnCntrManagerRequestResult();
		result.setCode("ERR");

		try {
			// 수정자 ID 셋팅
			managerRequest.setLastUpdusrId(UserUtils.getUser().getUserId());

			// 관리자 권한 승인관리 - '거절' 프로세스
			if("300".equals(managerRequest.getConfmSttusCode())) {
				result = designatedDonationService.updateManagerRequestReject(managerRequest);

			// 관리자 권한 승인관리 - '승인' 프로세스
			} else if("100".equals(managerRequest.getConfmSttusCode())) {
				result = designatedDonationService.updateManagerRequestApproval(managerRequest);
			}

		} catch (OpRuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "=========== managerRequestConfirmProcess OpRuntimeException =============");
		}

		return JsonViewUtils.success(result);
	}

	/**
	 * 관리자 권한 승인관리 상세 정보 조회 (팝업창)
	 * @param model
	 * @param userId
	 * @return
	 */
	@RequestProperty(layout="base")
	@GetMapping("/request/popup/history/{userId}")
	public String managerRequestHistory(Model model, @PathVariable long userId) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}

		DsgnCntrManagerRequestSearchParam searchParam = new DsgnCntrManagerRequestSearchParam();
		searchParam.setUserId(userId);

		DesignatedPart param = new DesignatedPart();
		param.setUseYn("Y");

		DesignatedPartParam partParam = new DesignatedPartParam();
		partParam.setUseYn("Y");
		partParam.setItemsPerPage(Integer.MAX_VALUE);

		model.addAttribute("list", designatedDonationService.getManagerRequestHistory(searchParam));
		model.addAttribute("partList", designatedDonationService.selectDsgncntrPartMngList(partParam));

		return ViewUtils.getView("/designated-donation/request/manager-request-history");
	}

	/**
	 * 목록데이터 상태변경
	 * @param flag
	 * @param requestContext
	 * @param itemListParam
	 * @return
	 */
	@PostMapping("list/notice/update-label/{flag}")
	public JsonView updateNoticeListDataByLabel(@PathVariable("flag") String flag, RequestContext requestContext, DesignatedDonationNotice designatedDonationNotice) {

		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}
		designatedDonationNotice.setDisplayYn(flag);
		designatedDonationService.updateNoticeListDataByLabel(designatedDonationNotice);

		return JsonViewUtils.success();
	}

	@PostMapping("part/list")
	public String searchSelectDsgncntrPartMngList(Model model, RequestContext requestContext, DesignatedPartParam param) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		List<DesignatedPart> list = designatedDonationService.selectDsgncntrPartMngList(param);

		model.addAttribute("list", list);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("param", param);
		model.addAttribute("totalItems", param.getPagination().getTotalItems());

		return ViewUtils.getView("/designated-donation/part/list");
	}

	@GetMapping("part/list")
	public String selectDsgncntrPartMngList(Model model, RequestContext requestContext, DesignatedPartParam param) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		List<DesignatedPart> list = designatedDonationService.selectDsgncntrPartMngList(param);

		model.addAttribute("list", list);
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));
		model.addAttribute("param", param);
		model.addAttribute("totalItems", param.getPagination().getTotalItems());

		return ViewUtils.getView("/designated-donation/part/list");
	}



	/**
	 * 지정기부 사업부서정보 등록 화면
	 * @param model
	 * @param requestContext
	 * @param param
	 * @return
	 */
	@GetMapping("/part/form/{dsgncntrPartId}")
	public String createDesignatedPart(Model model, RequestContext requestContext, @PathVariable long dsgncntrPartId) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		if (dsgncntrPartId > 0) {
			DesignatedPartParam param = new DesignatedPartParam();
			param.setDsgncntrPartId(dsgncntrPartId);
			model.addAttribute("designatedPart", designatedDonationService.selectDsgncntrPartMngDetail(param));
		} else {
			DesignatedPart designatedPart = new DesignatedPart();
			if (!UserUtils.hasMasterManagerRole()) {
				designatedPart.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
				designatedPart.setUpperLocgovCode(designatedPart.getLocgovCode().substring(0, 2) + "000");
			}
			model.addAttribute("designatedPart", designatedPart);
		}
		model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));

		return ViewUtils.getView("/designated-donation/part/form");
	}


	/**
	 * 지정기부 공지내역 저장
	 * @param model
	 * @param requestContext
	 * @param param
	 * @return
	 */
	@PostMapping("/part/save")
	public String saveDesignatedPart(Model model, RequestContext requestContext
													, @ModelAttribute(value="designatedPart") DesignatedPart designatedPart) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		long designatedPartId = designatedPart.getDsgncntrPartId();
		String msg = "";
		int resultCnt;
		if (designatedPartId > 0) {
			resultCnt = designatedDonationService.updateDsgncntrPartMng(designatedPart);
			msg = MessageUtils.getMessage("M00289");		// 수정되었습니다.
		} else {
			resultCnt = designatedDonationService.insertDsgncntrPartMng(designatedPart);
			msg = MessageUtils.getMessage("M00288");		// 등록되었습니다.
		}
		if (resultCnt == 0) {
			msg = "저장에 실패했습니다.";
		}
		return ViewUtils.redirect("/opmanager/designated-donation/part/list", msg);

	}

	/**
	 * 관리자 권한 승인관리 부서 변경
	 * @param managerRequest
	 * @return
	 */
	@PostMapping("/request/partUpdate")
	public JsonView managerRequestPartUpdate(DesignatedPart designatedPart) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return JsonViewUtils.failure("부서정보가 없어서 처리가 불가능합니다.");
		}

		// 결과값
		DsgnCntrManagerRequestResult result = new DsgnCntrManagerRequestResult();
		result.setCode("ERR");

		try {

			designatedDonationService.updatePartInfo(designatedPart, result);

		} catch (OpRuntimeException e) {
//			log.error("ERROR: {}", e.getMessage(), e);
			log.error("ERROR: {}", "=========== managerRequestPartUpdate OpRuntimeException =============");
		}

		return JsonViewUtils.success(result);
	}

	// 부서 목록 조회
	@PostMapping("/partList")
	public JsonView getDsgncntrPartList(DesignatedPartParam param) {
		if (StringUtils.hasLength(param.getLocgovCode())) {
			param.setUseYn("Y");
			param.setItemsPerPage(Integer.MAX_VALUE);
			return JsonViewUtils.success(designatedDonationService.selectDsgncntrPartMngList(param));
		}
		return JsonViewUtils.success(new ArrayList<>());
	}

	// 부서 목록 조회
	@RequestProperty(layout="log")
	@GetMapping("/popup-confirm-log/{prjId}")
	public String getConfirmLog(DsgncntrConfirmLogParam param, @PathVariable("prjId") long prjId, RequestContext requestContext, Model model) {
		if (designatedDonationService.checkDsgncntrAuthPartInfo() == 0) {
			return ViewUtils.redirect("/opmanager", "부서정보가 없어서 처리가 불가능합니다.");
		}
		param.setPrjId(prjId);

		model.addAttribute("list", designatedDonationService.selectDsgncntrConfirmLog(param));
		model.addAttribute("param", param);
		model.addAttribute("pagination", param.getPagination());

		return ViewUtils.getView("/designated-donation/popup-confirm-log");
	}

	/**
	 * 엑셀 다운로드
	 * @return
	 */
	@GetMapping(value="/list/excel-download")
	public ModelAndView downloadExcelProcessByList(DesignatedDonationSearchParam designatedDonationSearchParam) {
		ModelAndView mav = new ModelAndView(new DesignatedDonatioinExcelView());

		LocalDate now = LocalDate.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
		if (!StringUtils.hasLength(designatedDonationSearchParam.getPrjStDt()) && !StringUtils.hasLength(designatedDonationSearchParam.getPrjEdDt())) {
			designatedDonationSearchParam.setPrjStDt(format.format(now));
			designatedDonationSearchParam.setPrjEdDt(format.format(now));
		} else if (!StringUtils.hasLength(designatedDonationSearchParam.getPrjStDt())) {
			designatedDonationSearchParam.setPrjStDt(designatedDonationSearchParam.getPrjEdDt());
		} else if (!StringUtils.hasLength(designatedDonationSearchParam.getPrjEdDt())) {
			designatedDonationSearchParam.setPrjEdDt(designatedDonationSearchParam.getPrjStDt());
		}

		if (!StringUtils.hasLength(designatedDonationSearchParam.getUpperLocgovCode()) && StringUtils.hasLength(designatedDonationSearchParam.getLocgovCode())) {
			designatedDonationSearchParam.setUpperLocgovCode(locgovService.getUpperLocgovCode(designatedDonationSearchParam.getLocgovCode()));
		}

		designatedDonationSearchParam.setItemsPerPage(Integer.MAX_VALUE);

		mav.addObject("designatedDonationSearchParam", designatedDonationSearchParam);
		mav.addObject("designatedDonationList", designatedDonationService.selectDesignatedDonationList(designatedDonationSearchParam));
		mav.addObject("bsnsTypeList", designatedDonationService.getDesignatedDonationBsnsTypes());

		return mav;
	}

	/**
	 * 특장사업 기부하기 미리보기
	 * @param prjId
	 * @param model
	 * @return
	 */
	@RequestProperty(title="타이틀" ,layout="blank")
	@GetMapping("/preview")
	public String preview(@RequestParam("prjId") long prjId, RequestContext requestContext, DesignatedDonation designatedDonation, Model model) {

		designatedDonation.setPrjId(prjId);

		DesignatedDonationSearchParam param = new DesignatedDonationSearchParam();
		param.setPrjId(designatedDonation.getPrjId());
		designatedDonation = designatedDonationService.selectDesignatedDonationDetailPreview(param);

		//이미지 사이즈 조정(L)
        if (designatedDonation.getPrjImages().size() > 0) {
            for (PrjImage prjImage : designatedDonation.getPrjImages()) {
                if (!ObjectUtils.isEmpty(prjImage.getImageName())) {
                	prjImage.setImageName(ShopUtils.loadImagePreview(String.valueOf(prjImage.getPrjId()), prjImage.getImageName(), "L"));
                }
            }
        }

        //날짜형식 수정
        String prjStdt = designatedDonation.getPrjStDt();
        String prjEdDt = designatedDonation.getPrjEdDt();

        designatedDonation.setPrjStDt(prjStdt.substring(0, 4)+ '-'+ prjStdt.substring(4, 6) + '-' + prjStdt.substring(6));
        designatedDonation.setPrjEdDt(prjEdDt.substring(0, 4)+ '-'+ prjEdDt.substring(4, 6) + '-' + prjEdDt.substring(6));

		model.addAttribute("designatedDonation",designatedDonation);

		return ViewUtils.view();
	}


}
