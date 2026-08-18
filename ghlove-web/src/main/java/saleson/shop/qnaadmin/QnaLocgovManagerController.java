package saleson.shop.qnaadmin;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.PageNotFoundException;
import com.onlinepowers.framework.util.*;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import saleson.common.enumeration.IdType;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.code.CodeService;
import saleson.shop.code.domain.Code;
import saleson.shop.code.support.CodeParam;
//import saleson.common.security.crypto.Cryptor;
//import saleson.common.security.masking.DataMasking;
import saleson.shop.mailconfig.domain.MailConfig;
import saleson.shop.qnaadmin.domain.QnaAdmin;
import saleson.shop.qnaadmin.domain.QnaAdminAnswer;
import saleson.shop.qnaadmin.domain.QnaAdminAnswerFile;
import saleson.shop.qnaadmin.domain.QnaAdminFile;
import saleson.shop.qnaadmin.support.QnaAdminParam;
import saleson.shop.user.LocgovService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/seller/qna-locgov")
@RequestProperty(title="지자체 문의", layout="default", template="seller")
public class QnaLocgovManagerController {

	private static final Logger log = LoggerFactory.getLogger(QnaLocgovManagerController.class);

	@Autowired
	private QnaAdminService qnaAdminService;

	@Autowired
	private LocgovService locgovService;

	@Autowired
	private CodeService codeService;

	// 판매자 지자체 문의 목록
	@GetMapping(value="/list")
	public String qnaAdminList(QnaAdminParam qnaAdminParam, Model model
								, @RequestParam(value="itemsPerPage"
								, required=false, defaultValue = "") String itemsPerPageStr
								, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}
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

//		qnaAdminParam.setSellerId(SellerUtils.getSellerId());
		qnaAdminParam.setUserId(UserUtils.getUser().getUserId());

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
		return ViewUtils.getView("/qna-locgov/list");
	}

	@PostMapping(value="/list")
	public String searchQnaAdminList(QnaAdminParam qnaAdminParam, Model model
								, @RequestParam(value="itemsPerPage"
								, required=false, defaultValue = "") String itemsPerPageStr
								, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}
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

//		qnaAdminParam.setSellerId(SellerUtils.getSellerId());
		qnaAdminParam.setUserId(UserUtils.getUser().getUserId());

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
		return ViewUtils.getView("/qna-locgov/list");
	}

	// 판매자 지자체 문의 작성 화면
	@GetMapping("/create")
	public String qnaAdminInsert(QnaAdmin qnaAdmin, Model model, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}

        CodeParam param = new CodeParam();
        param.setCodeType("QNA_GROUPS");
        List<Code> qnaGroups = codeService.getCodeList(param);

		List<Code> qnaGroupsLocgov = new ArrayList<>();
		for (Code code : qnaGroups) {
			if (code.getExtentionCode() != null && code.getExtentionCode().contains("locgov")) {
				qnaGroupsLocgov.add(code);
			}
		}

		model.addAttribute("qnaAdmin", qnaAdmin);
		model.addAttribute("qnaGroups", qnaGroupsLocgov);		// 문의 유형
		return ViewUtils.getView("/qna-locgov/form");
	}

	// 판매자 지자체 문의 작성 저장
	@PostMapping("/create")
	public String qnaAdminInsertAction(QnaAdmin qnaAdmin
			, MailConfig mailConfig, Model model, QnaAdminParam qnaAdminParam, MultipartHttpServletRequest request
			, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}

//		qnaAdminParam.setSellerId(SellerUtils.getSellerId());
		qnaAdminParam.setUserId(UserUtils.getUser().getUserId());

		List<MultipartFile> files = request.getFiles("addFile");
		qnaAdmin.setAddFiles(files);
		qnaAdmin.setLocgovCode(locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER));
		qnaAdminService.insertQnaAdmin(qnaAdmin);

		return ViewUtils.redirect("/seller/qna-locgov/list", MessageUtils.getMessage("M00288")); // 등록되었습니다.
	}

	// 판매자 지자체 문의 상세
	@GetMapping(value="/view/{qnaAdminId}")
	public String qnaAdminView(@ModelAttribute("qnaAdmin") QnaAdmin qnaAdminParam, @PathVariable("qnaAdminId") long qnaAdminId, Model model
								, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
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

		model.addAttribute("qnaAdmin", qnaAdmin);
		model.addAttribute("qnaAdminAnswer", qnaAdminAnswer);
		model.addAttribute("qnaAdminFiles", qnaAdminFiles);
		model.addAttribute("qnaAdminAnswerFiles", qnaAdminAnswerFiles);
		model.addAttribute("qnaGroups", qnaGroupsLocgov);		// 문의 유형
		return ViewUtils.getView("/qna-locgov/view");
	}

	// 판매자 지자체 문의 작성내용 삭제
	@GetMapping(value="/deleteQnaAdmin/{qnaAdminId}")
	public String qnaAdminDelete(@ModelAttribute("qnaAdmin") QnaAdmin qnaAdmin, @PathVariable("qnaAdminId") long qnaAdminId
			, Model model, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}

//		qnaAdmin.setSellerId(SellerUtils.getSellerId());
		qnaAdmin.setUserId(UserUtils.getUser().getUserId());

		try {
			qnaAdminService.deleteQnaAdmin(qnaAdmin);
			return ViewUtils.redirect("/seller/qna-locgov/list", MessageUtils.getMessage("M00205")); // 삭제되었습니다.
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " qnaAdminDelete error ");
			return ViewUtils.redirect("/seller/qna-locgov/list", "실패했습니다.");
		}
	}

	// 판매자 지자체 문의 파일 다운로드
	@GetMapping(value="/downloadQnaAdminFile/{qnaAdminId}/{qnaAdminFileId}")
	@ResponseBody
	public ResponseEntity<byte[]> downloadQnaAdminFile(@PathVariable("qnaAdminId") long qnaAdminId, @PathVariable("qnaAdminFileId") long qnaAdminFileId, Model model
			, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}
		return qnaAdminService.downloadQnaAdminFile(qnaAdminId, qnaAdminFileId);
	}

	// 판매자 지자체 문의 수정 화면
	@GetMapping("/edit/{qnaAdminId}")
	public String qnaAdminEdit(Model model, RequestContext requestContext, @PathVariable("qnaAdminId") long qnaAdminId) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}
		QnaAdmin qnaAdmin = qnaAdminService.getQnaAdminByQnaAdminId(qnaAdminId);
		List<QnaAdminFile> qnaAdminFiles = qnaAdminService.getQnaAdminFileList(qnaAdmin);

        CodeParam param = new CodeParam();
        param.setCodeType("QNA_GROUPS");
        List<Code> qnaGroups = codeService.getCodeList(param);

		List<Code> qnaGroupsLocgov = new ArrayList<>();
		for (Code code : qnaGroups) {
			if (code.getExtentionCode() != null && code.getExtentionCode().contains("locgov")) {
				qnaGroupsLocgov.add(code);
			}
		}

		model.addAttribute("qnaAdmin", qnaAdmin);
		model.addAttribute("qnaAdminFiles", qnaAdminFiles);
		model.addAttribute("qnaGroups", qnaGroupsLocgov);		// 문의 유형
		return ViewUtils.getView("/qna-locgov/edit");
	}

	// 판매자 지자체 문의 수정
	@PostMapping("/edit/{qnaAdminId}")
	public String qnaAdminEditAction(QnaAdmin qnaAdmin,
			MailConfig mailConfig, Model model, QnaAdminParam qnaAdminParam, MultipartHttpServletRequest request
			, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}
		List<MultipartFile> files = request.getFiles("addFile");
		qnaAdmin.setAddFiles(files);
//		qnaAdmin.setLocgovCode(locgovService.getLocgovCodeByOpId(SellerUtils.getSellerId(), IdType.SELLER));

//		qnaAdminParam.setSellerId(SellerUtils.getSellerId());
		qnaAdminParam.setUserId(UserUtils.getUser().getUserId());

		try {
			qnaAdminService.updateQnaAdmin(qnaAdmin);
			return ViewUtils.redirect("/seller/qna-locgov/list", MessageUtils.getMessage("M00289")); // 수정되었습니다.
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " qnaAdminEditAction error ");
			return ViewUtils.redirect("/seller/qna-locgov/list", "실패했습니다.");
		}
	}

	// 판매자 지자체 문의 첨부파일 삭제
	@PostMapping(value="/deleteQnaAdminFile")
	@ResponseBody
	public JsonView qnaAdminDeleteFile(@RequestParam String qnaAdminId
			, @RequestParam String qnaAdminFileId
			, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}
		JsonView result = null;
		try {
			result = JsonViewUtils.success(qnaAdminService.deleteQnaAdminFile(Long.valueOf(qnaAdminId), Long.valueOf(qnaAdminFileId), false, ""));
		} catch (OpRuntimeException e) {
			log.error(getClass().getName() + " qnaAdminEditAction error ");
			//result = JsonViewUtils.failure(e.getErrorMessage());		// 권한이 없습니다.
			result = JsonViewUtils.failure("권한이 없습니다.");
		}
		return result;
	}

	// 관리자 답변 첨부파일 파일 다운로드
	@GetMapping(value="/downloadQnaAdminAnswerFile/{qnaAdminAnswerId}/{qnaAdminAnswerFileId}")
	@ResponseBody
	public ResponseEntity<byte[]> downloadQnaAdminAnswerFile(@PathVariable("qnaAdminAnswerId") long qnaAdminAnswerId
			, @PathVariable("qnaAdminAnswerFileId") long qnaAdminAnswerFileId, Model model
			, RequestContext requestContext) {
		if (!SellerUtils.isSellerLogin()) {
			throw new PageNotFoundException();
		}
		return qnaAdminService.downloadQnaAdminAnswerFile(qnaAdminAnswerId, qnaAdminAnswerFileId);
	}
}
