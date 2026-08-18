package saleson.shop.qnaadmin;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import saleson.common.enumeration.IdType;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
import saleson.shop.qnaadmin.domain.QnaAdmin;
import saleson.shop.qnaadmin.domain.QnaAdminAnswer;
import saleson.shop.qnaadmin.domain.QnaAdminAnswerFile;
import saleson.shop.qnaadmin.domain.QnaAdminFile;
import saleson.shop.qnaadmin.support.QnaAdminListParam;
import saleson.shop.qnaadmin.support.QnaAdminParam;
import saleson.shop.user.LocgovService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/opmanager/qna-admin")
@RequestProperty(title="관리자 문의", layout="default", template="opmanager")
public class QnaAdminManagerController {

	private static final Logger log = LoggerFactory.getLogger(QnaAdminManagerController.class);

	@Autowired
	private QnaAdminService qnaAdminService;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private CodeService codeService;

	// 관리자 관리자 문의 목록
	@GetMapping(value="/list")
	public String qnaAdminList(QnaAdminParam qnaAdminParam, Model model
								, @RequestParam(value="itemsPerPage"
								, required=false, defaultValue = "") String itemsPerPageStr
								, HttpServletRequest request
								, RequestContext requestContext) {
		if (!UserUtils.isManagerLogin()) {
			throw new PageNotFoundException();
		}
		String today1 = DateUtils.getToday("yyyyMMdd");
		qnaAdminParam.setSearchStartDate(StringUtils.defaultIfEmpty(qnaAdminParam.getSearchStartDate(), today1));
		qnaAdminParam.setSearchEndDate(StringUtils.defaultIfEmpty(qnaAdminParam.getSearchEndDate(), today1));

		int itemsPerPage;
		switch (itemsPerPageStr) {
			case "1":		// test 용
			case "10":
			case "20":
			case "50":
			case "100":
				itemsPerPage = Integer.valueOf(itemsPerPageStr);
				break;
			default:
				itemsPerPage = 10;
				break;
		}

		if (!isUpperAdmin()) {
			qnaAdminParam.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			if (StringUtils.isEmpty(qnaAdminParam.getLocgovCode())) {
				throw new PageNotFoundException();
			}
		}

		int qnaCount = 0;

		Pagination pagination = Pagination.getInstance(qnaCount, itemsPerPage);
		qnaAdminParam.setItemsPerPage(itemsPerPage);
		qnaAdminParam.setPagination(pagination);

        CodeParam param = new CodeParam();
        param.setCodeType("QNA_GROUPS");
        List<Code> qnaGroups = codeService.getCodeList(param);

		List<QnaAdmin> qnaAdminList = Collections.EMPTY_LIST;

		for (QnaAdmin qnaAdmin : qnaAdminList) {
			label: for (Code code : qnaGroups) {
				if (code.getId().equals(qnaAdmin.getQnaGroup())) {
					qnaAdmin.setQnaGroupName(code.getLabel());
					break label;
				}
			}
		}

		List<Code> qnaGroupsLocgov = new ArrayList<>();
		for (Code code : qnaGroups) {
			if (code.getExtentionCode() != null && code.getExtentionCode().contains("locgov")) {
				qnaGroupsLocgov.add(code);
			}
		}

		model.addAttribute("qnaGroups", qnaGroupsLocgov);		// 문의 유형
		model.addAttribute("qnaAdminList", qnaAdminList);		// 조회 목록
		model.addAttribute("qnaCount", qnaCount);
		model.addAttribute("pagination", pagination);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드

		return ViewUtils.getView("/qna-admin/list");
	}

	@PostMapping(value="/list")
	public String qnaAdminListPost(QnaAdminParam qnaAdminParam, Model model
								, @RequestParam(value="itemsPerPage"
								, required=false, defaultValue = "") String itemsPerPageStr
								, HttpServletRequest request
								, RequestContext requestContext) {
		if (!UserUtils.isManagerLogin()) {
			throw new PageNotFoundException();
		}
		String today1 = DateUtils.getToday("yyyyMMdd");
		qnaAdminParam.setSearchStartDate(StringUtils.defaultIfEmpty(qnaAdminParam.getSearchStartDate(), today1));
		qnaAdminParam.setSearchEndDate(StringUtils.defaultIfEmpty(qnaAdminParam.getSearchEndDate(), today1));

		int itemsPerPage;
		switch (itemsPerPageStr) {
			case "1":		// test 용
			case "10":
			case "20":
			case "50":
			case "100":
				itemsPerPage = Integer.valueOf(itemsPerPageStr);
				break;
			default:
				itemsPerPage = 10;
				break;
		}

		if (!isUpperAdmin()) {
			qnaAdminParam.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));
			if (StringUtils.isEmpty(qnaAdminParam.getLocgovCode())) {
				throw new PageNotFoundException();
			}
		}

		int qnaCount = qnaAdminService.getQnaAdminListCountByParam(qnaAdminParam);

		Pagination pagination = Pagination.getInstance(qnaCount, itemsPerPage);
		qnaAdminParam.setItemsPerPage(itemsPerPage);
		qnaAdminParam.setPagination(pagination);

        CodeParam param = new CodeParam();
        param.setCodeType("QNA_GROUPS");
        List<Code> qnaGroups = codeService.getCodeList(param);

		List<QnaAdmin> qnaAdminList = qnaAdminService.getQnaAdminListByParam(qnaAdminParam);

		for (QnaAdmin qnaAdmin : qnaAdminList) {
			label: for (Code code : qnaGroups) {
				if (code.getId().equals(qnaAdmin.getQnaGroup())) {
					qnaAdmin.setQnaGroupName(code.getLabel());
					break label;
				}
			}
		}

		List<Code> qnaGroupsLocgov = new ArrayList<>();
		for (Code code : qnaGroups) {
			if (code.getExtentionCode() != null && code.getExtentionCode().contains("locgov")) {
				qnaGroupsLocgov.add(code);
			}
		}

		model.addAttribute("qnaGroups", qnaGroupsLocgov);		// 문의 유형
		model.addAttribute("qnaAdminList", qnaAdminList);		// 조회 목록
		model.addAttribute("qnaCount", qnaCount);
		model.addAttribute("pagination", pagination);
		model.addAttribute("sido", CodeUtils.getCodeList("WDR"));		// 지자체 시도 코드

		return ViewUtils.getView("/qna-admin/list");
	}

	// 판매자 지자체 문의 작성내용 삭제
	@GetMapping(value="/deleteQnaAdmin/{qnaAdminId}")
	public String qnaAdminDelete(@ModelAttribute("qnaAdmin") QnaAdmin qnaAdmin, @PathVariable("qnaAdminId") long qnaAdminId
			, Model model, HttpServletRequest request) {
		if (!UserUtils.isManagerLogin()) {
			return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("권한이 없습니다.")); // 권한이 없습니다.
		}

		String locgovCode = "";
		if (!isUpperAdmin()) {
			locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			qnaAdmin.setLocgovCode(locgovCode);
			if (StringUtils.isEmpty(qnaAdmin.getLocgovCode())) {
				return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("권한이 없습니다.")); // 권한이 없습니다.
			}
		}

		try {
			if (UserUtils.isManagerLogin()) {
				qnaAdmin = qnaAdminService.getQnaAdminByQnaAdminId(qnaAdminId);
				if (qnaAdmin.getLocgovCode().equals(locgovCode)) {
					qnaAdminService.deleteQnaAdmin(qnaAdmin);
					return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("M00205")); // 삭제되었습니다.
				}
			}
			return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("권한이 없습니다.")); // 권한이 없습니다.
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " qnaAdminDelete error", e);
			//return ViewUtils.redirect("/opmanager/qna-admin/list", e.getErrorMessage());
			return ViewUtils.redirect("/opmanager/qna-admin/list","실패했습니다..");
		}
	}

	// 관리자 답변 첨부파일 파일 다운로드
	@GetMapping(value="/downloadQnaAdminAnswerFile/{qnaAdminAnswerId}/{qnaAdminAnswerFileId}")
	@ResponseBody
	public ResponseEntity<byte[]> downloadQnaAdminAnswerFile(@PathVariable("qnaAdminAnswerId") long qnaAdminAnswerId
			, @PathVariable("qnaAdminAnswerFileId") long qnaAdminAnswerFileId, Model model) {

		if (!UserUtils.isManagerLogin()) {
			throw new PageNotFoundException();
		}

		return qnaAdminService.downloadQnaAdminAnswerFile(qnaAdminAnswerId, qnaAdminAnswerFileId);
	}

	// 관리자 답변 수정 화면
	@GetMapping("/edit/{qnaAdminId}")
	public String qnaAdminEdit(Model model, RequestContext requestContext
			, @PathVariable("qnaAdminId") long qnaAdminId) {
		if (!UserUtils.isManagerLogin()) {
			return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("권한이 없습니다.")); // 권한이 없습니다.
		}

		QnaAdmin qnaAdmin = qnaAdminService.getQnaAdminByQnaAdminId(qnaAdminId);
		QnaAdminAnswer qnaAdminAnswer = qnaAdminService.getQnaAdminAnswerByQnaAdminId(qnaAdminId);

		List<QnaAdminFile> qnaAdminFiles = qnaAdminService.getQnaAdminFileList(qnaAdmin);
		List<QnaAdminAnswerFile> qnaAdminAnswerFiles = qnaAdminService.getQnaAdminAnswerFileList(qnaAdminAnswer);

        CodeParam param = new CodeParam();
        param.setCodeType("QNA_GROUPS");
        List<Code> qnaGroups = codeService.getCodeList(param);

		for (Code code : qnaGroups) {
			if (code.getId().equals(qnaAdmin.getQnaGroup())) {
				qnaAdmin.setQnaGroupName(code.getLabel());
				break;
			}
		}

		List<Code> qnaGroupsLocgov = new ArrayList<>();
		for (Code code : qnaGroups) {
			if (code.getExtentionCode() != null && code.getExtentionCode().contains("locgov")) {
				qnaGroupsLocgov.add(code);
			}
		}

		if (qnaAdminAnswer == null) {
			qnaAdminAnswer = new QnaAdminAnswer();
			qnaAdminAnswer.setAnswerDate(Timestamp.valueOf(LocalDateTime.now()));

		}

		if (qnaAdminAnswerFiles == null) {
			qnaAdminAnswerFiles = new ArrayList<>();
		}

		model.addAttribute("qnaAdmin", qnaAdmin);
		model.addAttribute("qnaAdminAnswer", qnaAdminAnswer);
		model.addAttribute("qnaAdminFiles", qnaAdminFiles);
		model.addAttribute("qnaAdminAnswerFiles", qnaAdminAnswerFiles);
		model.addAttribute("qnaGroups", qnaGroupsLocgov);		// 문의 유형

		return ViewUtils.getView("/qna-admin/form");
	}

	// 관리자 관리자 답변 수정
	@PostMapping("/edit/{qnaAdminId}")
	public String qnaAdminEditAction(
			/* QnaAdmin qnaAdmin, */ QnaAdminAnswer qnaAdminAnswer
			/* , MailConfig mailConfig */, Model model, QnaAdminParam qnaAdminParam, MultipartHttpServletRequest request) {
		if (!UserUtils.isManagerLogin()) {
			return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("권한이 없습니다.")); // 권한이 없습니다.
		}

		List<MultipartFile> files = request.getFiles("addFile");
		qnaAdminAnswer.setAddFiles(files);

		String locgovCode = "";
		if (!isUpperAdmin()) {
			locgovCode = locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER);
			if (StringUtils.isEmpty(locgovCode)) {
				return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("권한이 없습니다.")); // 권한이 없습니다.
			}
		}

		try {
			if (qnaAdminAnswer.getQnaAdminAnswerId() > 0) {
				qnaAdminService.updateQnaAdminAnswer(qnaAdminAnswer);
				return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("M00289")); // 수정되었습니다.
			} else {
				qnaAdminService.insertQnaAdminAnswer(qnaAdminAnswer, locgovCode);
				return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("M00288")); // 등록되었습니다.
			}
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " qnaAdminEditAction error", e);
			return ViewUtils.redirect("/opmanager/qna-admin/list", "실패했습니다.");
		}
	}

	// 관리자 관리자 답변 첨부파일 삭제
	@PostMapping(value="/deleteQnaAdminAnswerFile")
	@ResponseBody
	public JsonView qnaAdminDeleteFile(@RequestParam String qnaAdminAnswerId
			, @RequestParam String qnaAdminAnswerFileId, HttpServletRequest request) {
		if (!UserUtils.isManagerLogin()) {
			throw new PageNotFoundException();
		}
		JsonView result = null;
		try {
			QnaAdminAnswer qnaAdminAnswer = qnaAdminService.getQnaAdminAnswerByQnaAdminAnswerId(Long.valueOf(qnaAdminAnswerId));
			result = JsonViewUtils.success(qnaAdminService.deleteQnaAdminAnswerFile(
					qnaAdminAnswer.getQnaAdminId(), Long.valueOf(qnaAdminAnswerId), Long.valueOf(qnaAdminAnswerFileId), false));
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " qnaAdminDeleteFile error", e);
			result = JsonViewUtils.failure("실패했습니다.");
		}
		return result;
	}

	// 관리자 답변 작성내용 삭제
	@GetMapping(value="/deleteQnaAdminAnswer/{qnaAdminAnswerId}")
	public String qnaAdminAnswerDelete(@ModelAttribute("qnaAdminAnswer") QnaAdminAnswer qnaAdminAnswer, @PathVariable("qnaAdminAnswerId") long qnaAdminAnswerId
			, Model model, HttpServletRequest request) {
		if (!UserUtils.isManagerLogin()) {
			throw new PageNotFoundException();
		}
		try {
			qnaAdminAnswer = qnaAdminService.getQnaAdminAnswerByQnaAdminAnswerId(qnaAdminAnswerId);
			qnaAdminService.deleteQnaAdminAnswer(qnaAdminAnswer);
			return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("M00205")); // 삭제되었습니다.
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " qnaAdminAnswerDelete error", e);
			return ViewUtils.redirect("/opmanager/qna-admin/list","실패했습니다.");
		}
	}

	// 판매자 지자체 문의 작성내용 삭제
	@PostMapping(value="/qnaAdmin/delete")
	public JsonView qnaAdminListDelete(QnaAdminListParam param, Model model, HttpServletRequest request) {
		if (!UserUtils.isManagerLogin()) {
			throw new PageNotFoundException();
		}
		try {
			param.setLocgovCode(locgovService.getLocgovCodeByOpId(UserUtils.getUser().getUserId(), IdType.MANAGER));

			qnaAdminService.deleteQnaAdminList(param);
//			return ViewUtils.redirect("/opmanager/qna-admin/list", MessageUtils.getMessage("M00205")); // 삭제되었습니다.
			return JsonViewUtils.success(MessageUtils.getMessage("M00205"));
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " qnaAdminListDelete error", e);
//			return ViewUtils.redirect("/opmanager/qna-admin/list", e.getErrorMessage());
			return JsonViewUtils.failure(e.getErrorMessage());
		}
	}

	// 판매자 지자체 문의 파일 다운로드
	@GetMapping(value="/downloadQnaAdminFile/{qnaAdminId}/{qnaAdminFileId}")
	@ResponseBody
	public ResponseEntity<byte[]> downloadQnaAdminFile(@PathVariable("qnaAdminId") long qnaAdminId, @PathVariable("qnaAdminFileId") long qnaAdminFileId, Model model
			, RequestContext requestContext) {
		if (!UserUtils.isManagerLogin()) {
			throw new PageNotFoundException();
		}
		return qnaAdminService.downloadQnaAdminFile(qnaAdminId, qnaAdminFileId);
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
