package saleson.shop.qna;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import saleson.common.Const;
import saleson.common.enumeration.IdType;
import saleson.common.notification.ApplicationInfoService;
import saleson.common.notification.UnifiedMessagingService;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.userauth.UserAuthService;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.common.file.ExcelDownloadView;
import saleson.seller.main.support.SellerParam;
import saleson.shop.mailconfig.MailConfigService;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.domain.QnaAnswer;
import saleson.shop.qna.support.QnaParam;
import saleson.shop.sendmaillog.SendMailLogService;
import saleson.shop.ums.UmsService;
import saleson.shop.user.LocgovService;
import saleson.shop.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/opmanager/qna-item")
@RequestProperty(title = "", layout = "default")
public class QnaItemManagerController {
	private static final Logger log = LoggerFactory.getLogger(QnaItemManagerController.class);
	@Autowired
	private QnaService qnaService;

	@Autowired
	private SendMailLogService sendMailLogService;

	@Autowired
	private MailConfigService mailConfigService;

	@Autowired
	private UserService userService;

//	@Autowired
//	private SendSmsLogService sendSmsLogService;
//
//	@Autowired
//	private SmsConfigService smsConfigService;
//
//	@Autowired
//	private SellerService sellerService;

	@Autowired
	private UmsService umsService;

	@Autowired
	private UnifiedMessagingService unifiedMessagingService;

	@Autowired
	private ApplicationInfoService applicationInfoService;

	@Autowired
	private Cryptor cryptor;

	@Autowired
	private DataMasking dataMasking;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private UserAuthService userAuthService;

	@GetMapping(value="/list")
	public String answerList(QnaParam qnaParam, Model model) {
		String today1 = DateUtils.getToday("yyyyMMdd");
		qnaParam.setSearchStartDate(StringUtils.defaultIfEmpty(qnaParam.getSearchStartDate(), today1));
		qnaParam.setSearchEndDate(StringUtils.defaultIfEmpty(qnaParam.getSearchEndDate(), today1));

		if (!UserUtils.isManagerLogin() && !UserUtils.isSellerLogin()) {
			return ViewUtils.redirect("/opmanager/", "로그인 상태가 아닙니다.");
		}

		if (ShopUtils.isSellerPage()) {
			qnaParam.setSigungu(locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER));
			qnaParam.setSellerId(SellerUtils.getSellerId());
			if (!org.springframework.util.StringUtils.hasLength(qnaParam.getSigungu())) {
				return ViewUtils.redirect("/seller/", "지자체 정보가 없습니다.");
			}
		} else if (ShopUtils.isOpmanagerPage() && !isUpperAdmin()) {
			qnaParam.setSigungu(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			if (!org.springframework.util.StringUtils.hasLength(qnaParam.getSigungu())) {
				return ViewUtils.redirect("/opmanager/", "지자체 정보가 없습니다.");
			}
		} else if (ShopUtils.isOpmanagerPage() && isUpperAdmin()) {
			// 지자체 선택한 항목으로 조회
		} else {
			if (ShopUtils.isSellerPage()) {
				return ViewUtils.redirect("/seller/", "올바른 접근이 아닙니다.");
			} else {
				return ViewUtils.redirect("/opmanager/", "올바른 접근이 아닙니다.");
			}
		}

		qnaParam.setQnaType(Qna.QNA_GROUP_TYPE_ITEM);
		qnaService.setQnaListPagination(qnaParam);

		List<Qna> itemQnaLists = Collections.EMPTY_LIST;

		List<Code> qnaGroups = CodeUtils.getCodeList("QNA_GROUPS");

		model.addAttribute("qnaGroups", qnaGroups);

		for (Qna qnaCheck : itemQnaLists) {

			for (Code code : qnaGroups) {

				if (qnaCheck.getQnaGroup().equals(code.getId())) {
					qnaCheck.setQnaGroup(code.getLabel());
				}
			}
		}

		List<Code> qnaTypes = new ArrayList<>();
		for (Code code : qnaGroups) {
			List<Code> qnaGroup = CodeUtils.getCodeList("QNA_GROUP_" + code.getValue());
			for (Code qnaType : qnaGroup) {
				qnaTypes.add(qnaType);
			}
		}

		SellerParam sellerParam = new SellerParam();
		sellerParam.setStatusCode("2");
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("itemListCount", 0);
		model.addAttribute("itemQnaLists", itemQnaLists);
		model.addAttribute("qna", qnaParam);
		model.addAttribute("pagination", qnaParam.getPagination());
		model.addAttribute("qnaTypes", qnaTypes);

		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 시도 지자체 리스트

		return ViewUtils.getView("/qna-item/list");
	}

	@PostMapping(value="/list")
	public String answerListPost(QnaParam qnaParam, Model model) {
		String today1 = DateUtils.getToday("yyyyMMdd");
		qnaParam.setSearchStartDate(StringUtils.defaultIfEmpty(qnaParam.getSearchStartDate(), today1));
		qnaParam.setSearchEndDate(StringUtils.defaultIfEmpty(qnaParam.getSearchEndDate(), today1));

		if (!UserUtils.isManagerLogin() && !UserUtils.isSellerLogin()) {
			return ViewUtils.redirect("/opmanager/", "로그인 상태가 아닙니다.");
		}

		if (ShopUtils.isSellerPage()) {
			qnaParam.setSigungu(locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER));
			qnaParam.setSellerId(SellerUtils.getSellerId());
			if (!org.springframework.util.StringUtils.hasLength(qnaParam.getSigungu())) {
				return ViewUtils.redirect("/seller/", "지자체 정보가 없습니다.");
			}
		} else if (ShopUtils.isOpmanagerPage() && !isUpperAdmin()) {
			qnaParam.setSigungu(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			if (!org.springframework.util.StringUtils.hasLength(qnaParam.getSigungu())) {
				return ViewUtils.redirect("/opmanager/", "지자체 정보가 없습니다.");
			}
		} else if (ShopUtils.isOpmanagerPage() && isUpperAdmin()) {
			// 지자체 선택한 항목으로 조회
		} else {
			if (ShopUtils.isSellerPage()) {
				return ViewUtils.redirect("/seller/", "올바른 접근이 아닙니다.");
			} else {
				return ViewUtils.redirect("/opmanager/", "올바른 접근이 아닙니다.");
			}
		}

		qnaParam.setQnaType(Qna.QNA_GROUP_TYPE_ITEM);
		qnaService.setQnaListPagination(qnaParam);

		List<Qna> itemQnaLists = qnaService.getQnaListByParam(qnaParam);

		List<Code> qnaGroups = CodeUtils.getCodeList("QNA_GROUPS");

		model.addAttribute("qnaGroups", qnaGroups);

		for (Qna qnaCheck : itemQnaLists) {

			for (Code code : qnaGroups) {

				if (qnaCheck.getQnaGroup().equals(code.getId())) {
					qnaCheck.setQnaGroup(code.getLabel());
				}
			}
		}

		List<Code> qnaTypes = new ArrayList<>();
		for (Code code : qnaGroups) {
			List<Code> qnaGroup = CodeUtils.getCodeList("QNA_GROUP_" + code.getValue());
			for (Code qnaType : qnaGroup) {
				qnaTypes.add(qnaType);
			}
		}

		SellerParam sellerParam = new SellerParam();
		sellerParam.setStatusCode("2");
//		model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("itemListCount", qnaParam.getPagination().getTotalItems());
		model.addAttribute("itemQnaLists", itemQnaLists);
		model.addAttribute("qna", qnaParam);
		model.addAttribute("pagination", qnaParam.getPagination());
		model.addAttribute("qnaTypes", qnaTypes);

		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 시도 지자체 리스트

		return ViewUtils.getView("/qna-item/list");
	}

	@PostMapping("delete")
	public JsonView deleteListData(RequestContext requestContext, ListParam listParam) {

		 if (!requestContext.isAjaxRequest()) {
			 throw new NotAjaxRequestException();
		 }
		 qnaService.deleteQnaData(listParam);

		 return JsonViewUtils.success();
	}

//	@GetMapping(value="/view/{qnaId}")
//	public String answerView(@ModelAttribute("qna") Qna qnaParam, @PathVariable("qnaId") int qnaId, Model model) {
//
//		Qna qna = qnaService.getQnaByQnaId(qnaId);
//		if (qna == null) {
//			return ViewUtils.redirect(RequestContextUtils.getPreviousUri(), "Q&A 대상 상품이 삭제된 게시글입니다.");
//		}
//
//		if (ShopUtils.isSellerPage() && qna.getSellerId() != SellerUtils.getSellerId()) {
//			throw new PageNotFoundException();
//		}
//
//		QnaAnswer qnaAnswer = qnaService.getQnaAnswerByQnaId(qnaId);
//		//User adminUser = userService.getUserByUserId(UserUtils.getManagerId());
//		model.addAttribute("qna", qna);
//		model.addAttribute("qnaParam", qnaParam);
//		model.addAttribute("qnaAnswer", qnaAnswer);
//		//model.addAttribute("adminUser", adminUser);
//		return ViewUtils.getView("/qna-item/view");
//	}

	@GetMapping(value="/delete/{qnaId}")
	public String qnaDelete(@PathVariable("qnaId") int qnaId, RequestContext requestContext) {
		Qna qna = new Qna();
		qna.setQnaId(qnaId);
		qnaService.deleteQna(qna);
		String requestUri = requestContext.getRequestUri();
		String redirectUri = "/opmanager/qna-item/list";
		if(ShopUtils.isSellerPage(requestUri)){
			redirectUri = "/seller/qna-item/list";
		}
		return ViewUtils.redirect(redirectUri);
	}

	// 상품 문의 상세 조회
	@GetMapping("/answer/{qnaId}")
	public String answerInsert(Qna qnaParam, @PathVariable("qnaId") int qnaId, Model model, RequestContext requestContext) {
		Qna qna = qnaService.getQnaByQnaId(qnaId);
		QnaAnswer qnaAnswer =  new QnaAnswer();
		if(qnaParam.getAnswerId() != 0) {
			qnaAnswer = qnaService.getQnaAnswerByQnaAnswerId(qnaParam);
		}else {
			qnaAnswer = qnaService.getQnaAnswerByQnaId(qnaId);
		}

		List<Code> qnaGroups = CodeUtils.getCodeList("QNA_GROUPS");

		for (Code code : qnaGroups) {
			if (qna.getQnaGroup().equals(code.getId())) {
				qna.setQnaGroup(code.getLabel());
			}
		}

		model.addAttribute("qnaParam", qnaParam);
		//User adminUser = userService.getUserByUserId(UserUtils.getManagerId());

		long userId = UserUtils.getManagerId();

        if (ShopUtils.isSellerPage() && qna.getSellerId() != SellerUtils.getSellerId()) {
            throw new PageNotFoundException();
        }


		if( qnaAnswer == null ){
			qnaAnswer = new QnaAnswer();
		}

		model.addAttribute("qna", qna);
		model.addAttribute("qnaAnswer", qnaAnswer);


		String requestUri = requestContext.getRequestUri();

		if(ShopUtils.isSellerPage(requestUri)){
			model.addAttribute("seller", SellerUtils.getSeller());
		}else{
			model.addAttribute("userId", userId);
		}

		return ViewUtils.getView("/qna-item/answer");
	}

	// 상품 문의 답변 등록/수정
	@PostMapping("/answer/{qnaId}")
	public String qnaAnswerAction(@PathVariable("qnaId") int qnaId, QnaAnswer qnaAnswer,
			MailConfig mailConfig, Model model, QnaParam qnaParam, RequestContext requestContext) {

//		Qna qna = null;
		qnaAnswer.setSendMailFlag(qnaAnswer.getSendMailFlag() == null ? "N": "Y");
		qnaAnswer.setSendSmsFlag(qnaAnswer.getSendSmsFlag() == null ? "N": "Y");

		qnaAnswer.setUserId(SecurityUtils.getCurrentUser().getUserId());

		boolean isReg = false;
		if (qnaAnswer.getQnaAnswerId() > 0) {
			qnaService.updateQnaAnswer(qnaAnswer);
		} else {
			qnaService.insertQnaAnswer(qnaAnswer);
			isReg = true;
		}

		qnaService.updateQnaAnswerCount(qnaId);

//		if ("Y".equals(qnaAnswer.getSendMailFlag())) {
//
//			qna = qnaService.getQnaByQnaId(qnaId);
//			qnaService.encryptQnaData(qna);
//
//			mailConfig.setTemplateId("qna_complete");
//			QnaCompleteMail qnaComplete = new QnaCompleteMail(qna, mailConfigService.getMailConfigByTemplateId(mailConfig.getTemplateId()), cryptor, dataMasking);
//
//			SendMailLog sendMailLog = new SendMailLog();
//			//sendMailLog.setVendorId(UserUtils.getVendorId());
//			sendMailLog.setSendType("qna_complete");
//			sendMailLog.setUserId(qna.getUserId());
//
//			sendMailLogService.sendMail(qnaComplete.getMailConfig(), sendMailLog, qna.getEmail(), qna.getUserName());
//
//		}
//
//		if ("Y".equals(qnaAnswer.getSendSmsFlag())) {
//
//			String phoneNumber = "";
//
//			if (qna == null) {
//				qna = qnaService.getQnaByQnaId(qnaId);
//			}
//
//			// 회원 번호가 있는경우
//			if (qna.getUserId() > 0) {
//
//				UserDetail userDetail = userService.getUserDetail(qna.getUserId()); //문의한 사람 폰넘버
//				if (userDetail != null) {
//					phoneNumber = userDetail.getPhoneNumber();
//				}
//			}
//
//			if (StringUtils.isNotEmpty(phoneNumber)) {
//                String templateCode = "qna_complete";
//
//                Ums ums = umsService.getUms(templateCode);
//				ApplicationInfo applicationInfo = applicationInfoService.getApplicationInfo(qna.getUserId());
//
//                unifiedMessagingService.sendMessage(new QnaComplete(ums, phoneNumber, qna, applicationInfo, cryptor, dataMasking));
//			}
//		}

		try {		// 문자 발송
			qnaService.sendSmsQnaAnswer(qnaId);
		} catch (OpRuntimeException e) {
			log.error("qnaAnswerAction send sms error", e);
		}

		String requestUri = requestContext.getRequestUri();
		String redirectUri = "/opmanager/qna-item/list";
		if(ShopUtils.isSellerPage(requestUri)){
			redirectUri = "/seller/qna-item/list";
		}
		if (isReg) {
			return ViewUtils.redirect(redirectUri, MessageUtils.getMessage("M00492")); // 답변이 등록되었습니다.
		} else {
			return ViewUtils.redirect(redirectUri, MessageUtils.getMessage("답변이 수정되었습니다.")); // 답변이 수정되었습니다.
		}
	}

	// 상품문의 답변 삭제
	@GetMapping(value="/answer/delete/{qnaId}")
	public String answerDelete(@PathVariable("qnaId") int qnaId, RequestContext requestContext) {
		QnaAnswer qnaAnswer = qnaService.getQnaAnswerByQnaId(qnaId);
		qnaService.deleteQnaAnswer(qnaAnswer);
		String requestUri = requestContext.getRequestUri();
		String redirectUri = "/opmanager/qna-item/list";
		if(ShopUtils.isSellerPage(requestUri)){
			redirectUri = "/seller/qna-item/list";
		}
		return ViewUtils.redirect(redirectUri, MessageUtils.getMessage("M00205"));		// 삭제 되었습니다.
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

    /**
	 * 답례품 QnA 리스트 엑셀 다운로드
	 * @param qnaParam
	 * @return
	 */
	@SuppressWarnings({ "resource", "finally" })
	@GetMapping(value="/download-excel")
	public ModelAndView searchQnaItemListExcelDownlaod(
		QnaParam qnaParam,
		RedirectAttributes redirectAttributes,
		HttpSession httpSession
	) {
		String today1 = DateUtils.getToday("yyyyMMdd");
		qnaParam.setSearchStartDate(StringUtils.defaultIfEmpty(qnaParam.getSearchStartDate(), today1));
		qnaParam.setSearchEndDate(StringUtils.defaultIfEmpty(qnaParam.getSearchEndDate(), today1));

		if (!UserUtils.isManagerLogin() && !UserUtils.isSellerLogin()) {
			redirectAttributes.addFlashAttribute("message", "로그인 상태가 아닙니다.");
			return new ModelAndView("redirect:/opmanager/login-main");
		}

		if (ShopUtils.isSellerPage()) {
			qnaParam.setSigungu(locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER));
			qnaParam.setSellerId(SellerUtils.getSellerId());
			if (!org.springframework.util.StringUtils.hasLength(qnaParam.getSigungu())) {
				redirectAttributes.addFlashAttribute("message", "지자체 정보가 없습니다.");
				return new ModelAndView("redirect:/seller/");
			}
		} else if (ShopUtils.isOpmanagerPage() && !isUpperAdmin()) {
			qnaParam.setSigungu(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			if (!org.springframework.util.StringUtils.hasLength(qnaParam.getSigungu())) {
				redirectAttributes.addFlashAttribute("message", "지자체 정보가 없습니다.");
				return new ModelAndView("redirect:/opmanager/");
			}
		} else if (ShopUtils.isOpmanagerPage() && isUpperAdmin()) {
			// 지자체 선택한 항목으로 조회
		} else {
			if (ShopUtils.isSellerPage()) {
				redirectAttributes.addFlashAttribute("message", "올바른 접근이 아닙니다.");
				return new ModelAndView("redirect:/seller/");
			} else {
				redirectAttributes.addFlashAttribute("message", "올바른 접근이 아닙니다.");
				return new ModelAndView("redirect:/opmanager/");
			}
		}

		qnaParam.setQnaType(Qna.QNA_GROUP_TYPE_ITEM);
		qnaService.setQnaListPagination(qnaParam);

		// SXSSFWorkbook 인스턴스 생성 사유
		// - 엑셀 암호화로 인해 itemService에서 response를 종료 불가
		// - itemService에서 생성된 workbook을 controller통해 ItemReviewExcelView로 일괄 전송
		// - ItemReviewExcelView에서 엑셀 파일 생성 및 암호화, 파일 전송 일괄 수행
		SXSSFWorkbook workbook = new SXSSFWorkbook();

		// 사용자 다운로드 파일 명
		// ALL_ITEM_REVIEW_20260115142019.xlsx
		String fileName
			= "답례품QnA목록_"
			+ DateUtils.getToday(Const.DATETIME_FORMAT)
			+ ".xlsx"
		;

		try {
			workbook = qnaService.streamQnaItemData(qnaParam);
		} finally {
			/** ModelAndView에서 view이름이 아닌 객체(ItemReviewExcelView)를 담게된 경우,
			 * ModelAndView를 반환받은 DispatcherServlet에서 ViewResolver를 실행시키지 않고 객체를 실행
			 */
			// ItemReviewExcelView에 workbook과 암호화에 사용할 비밀번호를 담아서 전송
			ModelAndView mav = new ModelAndView(new ExcelDownloadView(userAuthService));
			mav.addObject("workbook", workbook);
			mav.addObject("fileName", fileName);

			return mav;
		}
	}
}
