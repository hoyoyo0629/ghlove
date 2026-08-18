package saleson.shop.give.giveoperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.onlinepowers.framework.enumeration.JavaScript;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.api.common.enumerated.UserAdminRole;
import saleson.common.Const;
import saleson.common.utils.UserUtils;
import saleson.shop.code.domain.Code;
import saleson.shop.give.giveoperation.domain.LocManagerCheck;
import saleson.shop.give.giveoperation.domain.CtbnyOpratn;
import saleson.shop.give.giveoperation.domain.CtbnyOpratnFile;
import saleson.shop.give.giveoperation.domain.CtbnyOpratnSearchParam;
import saleson.shop.give.giveoperation.domain.GiveOperation;
import saleson.shop.give.giveoperation.domain.GiveOperationSearchParam;
import saleson.shop.give.giveoperation.support.GiveOperationDetailExcelView;
import saleson.shop.give.giveoperation.support.GiveOperationExcelView;
import saleson.shop.give.statistics.domain.GiveStatisticsDetailSearch;
import saleson.shop.user.LocgovService;


@Controller
@RequestMapping("/opmanager/give/give-operation/**")
@RequestProperty(title="기부금 운용현황", layout="default", template="opmanager")
public class GiveOperationManagerController {

	@Autowired
	private GiveOperationService giveOperationService;

	@GetMapping("list")
	public String giveOperationList(@ModelAttribute("searchParam") GiveOperationSearchParam searchParam, Model model) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);

		if (ac.getIsLoc() && ac.isPass()) {
			searchParam.setShLocgovCode(ac.getLocgovCode());
			model.addAttribute("locgovFullNm", ac.getLocgovNm());
			// 합계
			model.addAttribute("total", new GiveOperation());
		} else if (ac.getIsLoc() && !ac.isPass()) {
			return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());
		} else {
			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
			// 합계
			model.addAttribute("total",new GiveOperation());
		}


		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}


		int count = 0;

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", Collections.EMPTY_LIST);
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		// 코드
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도
		//model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		model.addAttribute("searchParam",searchParam);

		return "view:/give/give-operation/list";
	}

	@PostMapping("list")
	public String searchGiveOperationList(@ModelAttribute("searchParam") GiveOperationSearchParam searchParam, Model model) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);

		if (ac.getIsLoc() && ac.isPass()) {
			searchParam.setShLocgovCode(ac.getLocgovCode());
			model.addAttribute("locgovFullNm", ac.getLocgovNm());
			// 합계
			model.addAttribute("total", giveOperationService.getGiveOperationTotalCnt(searchParam));
		} else if (ac.getIsLoc() && !ac.isPass()) {
			return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());
		} else {
			model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드
			// 합계
			model.addAttribute("total", giveOperationService.getGiveOperationTotalCnt(null));
		}


		if (searchParam.getShCntrYear() == null || "".equals(searchParam.getShCntrYear())) {
			LocalDate now = LocalDate.now();
			searchParam.setShCntrYear(String.valueOf(now.getYear()));
		}


		int count = giveOperationService.getGiveOperationListTotalCnt(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", giveOperationService.getGiveOperationList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);

		// 코드
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도
		//model.addAttribute("wdr", CodeUtils.getCodeList("WDR"));	// 지자체코드

		model.addAttribute("searchParam",searchParam);

		return "view:/give/give-operation/list";
	}

	@GetMapping("list/excel")
	public ModelAndView giveOperationListExcel(@ModelAttribute("searchParam") GiveOperationSearchParam searchParam) {
		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(null, null);
		if (ac.getIsLoc()) searchParam.setShLocgovCode(ac.getLocgovCode());

		ModelAndView mav = new ModelAndView(new GiveOperationExcelView());

		searchParam.setConditionType("EXCEL_DOWNLOAD");
		searchParam.setPagination(null);

		mav.addObject("list", giveOperationService.getGiveOperationList(searchParam));

		return mav;
	}

	@GetMapping("list/{locgovCode}")
	public String giveOperationListByLocgov(@ModelAttribute("searchParam") GiveOperationSearchParam searchParam, @PathVariable String locgovCode, Model model) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/give-operation/list/{locgovCode}");

		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());


		searchParam.setShLocgovCode(locgovCode);

		int count = giveOperationService.getGiveOperationListTotalCnt(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", giveOperationService.getGiveOperationList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		model.addAttribute("total", giveOperationService.getGiveOperationTotalCnt(searchParam));

		String id = StringUtils.defaultIfBlank(locgovCode, "00").trim().substring(0,2) + "000";
		model.addAttribute("upperLocgov", CodeUtils.getCode("WDR", id));
		model.addAttribute("locgov", CodeUtils.getCode("LOCGOV_CODE", locgovCode));

		model.addAttribute("searchParam",searchParam);
		model.addAttribute("yyyy", CodeUtils.getCodeList("YYYY"));	// 년도
		return "view:/give/give-operation/list_year";
	}

	@GetMapping("list/{locgovCode}/detail")
	public String giveOperationDetail(@ModelAttribute("searchParam") GiveOperationSearchParam searchParam, @PathVariable String locgovCode, Model model) {


		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/give-operation/list/{locgovCode}/detail");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		model.addAttribute("adminRole", ac.getAdminRole());
		return "view:/give/give-operation/detail";
	}

	@PostMapping("list/{locgovCode}/detail")
	public String searchGiveOperationDetail(@ModelAttribute("searchParam") GiveOperationSearchParam searchParam, @PathVariable String locgovCode, Model model) {

		String today = DateUtils.getToday("yyyyMMdd");
		searchParam.setShCntrDeStart(StringUtils.defaultIfEmpty(searchParam.getShCntrDeStart(), today));
		searchParam.setShCntrDeEnd(StringUtils.defaultIfEmpty(searchParam.getShCntrDeEnd(), today));

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/give-operation/list/{locgovCode}/detail");
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());

		/*if (searchParam.getShCntrYear() != null && !"".equals(searchParam.getShCntrYear())) {
			searchParam.setShCntrDeStart(searchParam.getShCntrYear() + "0101");
			searchParam.setShCntrDeEnd(searchParam.getShCntrYear() + "1231");
		}*/

		searchParam.setShLocgovCode(locgovCode);

		int count = giveOperationService.getCtbnyOpratnListTotalCnt(searchParam);

		Pagination pagination = Pagination.getInstance(count, searchParam.getItemsPerPage());
		searchParam.setPagination(pagination);

		model.addAttribute("list", giveOperationService.getCtbnyOpratnList(searchParam));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);


		model.addAttribute("total", giveOperationService.getGiveOperationTotalCnt(searchParam));
		model.addAttribute("searchTotal", giveOperationService.getCtbnyOpratnListAmt(searchParam));

		String id = StringUtils.defaultIfBlank(locgovCode,"00").trim().substring(0,2) + "000";
		model.addAttribute("upperLocgov", CodeUtils.getCode("WDR", id));
		model.addAttribute("locgov", CodeUtils.getCode("LOCGOV_CODE", locgovCode));

		model.addAttribute("searchParam", searchParam);
		model.addAttribute("adminRole", ac.getAdminRole());
		return "view:/give/give-operation/detail";
	}

	@GetMapping("list/{locgovCode}/excel")
	public ModelAndView giveOperationDetailExcel(@ModelAttribute("searchParam") GiveOperationSearchParam searchParam, @PathVariable String locgovCode) {
		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, null);
		if (ac.getIsLoc()) searchParam.setShLocgovCode(ac.getLocgovCode());

		ModelAndView mav = new ModelAndView(new GiveOperationDetailExcelView());

		searchParam.setConditionType("EXCEL_DOWNLOAD");
		searchParam.setPagination(null);
		searchParam.setShLocgovCode(locgovCode);

		mav.addObject("list", giveOperationService.getCtbnyOpratnList(searchParam));

		return mav;
	}

	@GetMapping("list/{locgovCode}/popup")
	@RequestProperty(layout = "base")
	public String operationPopup(Model model, @PathVariable String locgovCode, HttpServletResponse response) throws Exception {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/give-operation/list/{locgovCode}/popup");

		// 지자체 인원만 등록 가능
		if (!ac.getIsLoc()) return "view:/give/give-operation/popup/alert";
		else if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());


		model.addAttribute("locgovCode", locgovCode);
		model.addAttribute("cntrUse", CodeUtils.getCodeList("CNTR_USE"));	// 지출사업목적
		model.addAttribute("adminRole", ac.getAdminRole());
		return "view:/give/give-operation/popup/form";
	}

	@PostMapping("list/{locgovCode}/popup")
	public JsonView insertCtbnyOpratn(CtbnyOpratn ctbnyOpratn, @PathVariable String locgovCode, @RequestParam(value = "operationFiles", required = false) MultipartFile[] operationFiles) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, null);

		if (!ac.getIsLoc()) return JsonViewUtils.failure(MessageUtils.getMessage("지자체 관리자만 등록이 가능합니다."));
		else if (ac.getIsLoc() && !ac.isPass()) return JsonViewUtils.failure(ac.getMessage());


		ctbnyOpratn.setFrstRegisterId(UserUtils.getUser().getUserId());
		ctbnyOpratn.setLocgovCode(locgovCode);
		ctbnyOpratn.setOperationFiles(operationFiles);

		String message = "";

		try {
			message = giveOperationService.insertCtbnyOpratn(ctbnyOpratn);
			return JsonViewUtils.success(message);
		} catch (UserException e) {
			return JsonViewUtils.failure(MessageUtils.getMessage("등록 중 오류가 발생했습니다."));
		}


	}

	@GetMapping("list/{locgovCode}/popup/{registSn}")
	@RequestProperty(layout = "base")
	public String ctbnyOpratnUpdatePopup(Model model, @PathVariable String locgovCode, @PathVariable Long registSn) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, "/opmanager/give/give-operation/list/{locgovCode}/popup/" + registSn);
		if (ac.getIsLoc() && !ac.isPass()) return ViewUtils.redirect(ac.getReturnUrl(), ac.getMessage());


		model.addAttribute("detail", giveOperationService.getCtbnyOpratnDetail(registSn));
		model.addAttribute("locgovCode", locgovCode);
		model.addAttribute("cntrUse", CodeUtils.getCodeList("CNTR_USE"));	// 지출사업목적
		model.addAttribute("adminRole", ac.getAdminRole());
		return "view:/give/give-operation/popup/form";
	}

	@PostMapping("list/{locgovCode}/popup/{registSn}")
	public JsonView updateCtbnyOpratn(CtbnyOpratn ctbnyOpratn, @PathVariable String locgovCode, @PathVariable Long registSn, @RequestParam(value = "operationFiles", required = false) MultipartFile[] operationFiles) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, null);

		// 지자체 인원만 수정 가능
		if (!ac.getIsLoc()) return JsonViewUtils.failure(MessageUtils.getMessage("지자체 관리자만 등록이 가능합니다."));
		else if (ac.getIsLoc() && !ac.isPass()) return JsonViewUtils.failure(ac.getMessage());

		ctbnyOpratn.setLastUpdusrId(UserUtils.getUser().getUserId());
		ctbnyOpratn.setLocgovCode(locgovCode);
		ctbnyOpratn.setRegistSn(registSn);
		ctbnyOpratn.setOperationFiles(operationFiles);

		try {
			return JsonViewUtils.success(giveOperationService.updateCtbnyOpratn(ctbnyOpratn));
		} catch (RuntimeException e) {
			return JsonViewUtils.failure("등록 중 오류가 발생했습니다.");
		} catch (Exception e) {
			// TODO: handle exception
			return JsonViewUtils.failure("등록 중 오류가 발생했습니다.");
		}
	}

	@PostMapping("{locgovCode}/file/delete")
	public JsonView deleteCtbnyOpratnFile(CtbnyOpratnFile ctbnyOpratnFile, @PathVariable String locgovCode) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, null);

		// 지자체 인원만 수정 가능
		if (!ac.getIsLoc()) return JsonViewUtils.failure(MessageUtils.getMessage("지자체 관리자만 이용 가능합니다."));
		else if (ac.getIsLoc() && !ac.isPass()) return JsonViewUtils.failure(ac.getMessage());


		String message = "";
		try {
			message = giveOperationService.deleteCtbnyOpratnFile(ctbnyOpratnFile);
			return JsonViewUtils.success(message);
		} catch (RuntimeException e) {
			e.getStackTrace();
			return JsonViewUtils.failure("삭제 중 오류가 발생했습니다.");
		} catch (Exception e) {
			// TODO: handle exception
			return JsonViewUtils.failure("삭제 중 오류가 발생했습니다.");
		}

	}

	@GetMapping("file/download/all/{registSn}")
	public ResponseEntity<StreamingResponseBody> downloadFiles(@PathVariable Long registSn) throws URISyntaxException {


		CtbnyOpratn co = giveOperationService.getCtbnyOpratnDetail(registSn);


		if (co == null || co.getFileList() == null) return new ResponseEntity<StreamingResponseBody>(HttpStatus.NOT_FOUND);
		if (co.getFileList().size() == 1) {
			URI uri = new URI("/opmanager/give/give-operation/file/download/" + co.getFileList().get(0).getRegistFileId());
			HttpHeaders hh = new HttpHeaders();
			hh.setLocation(uri);
			return new ResponseEntity<StreamingResponseBody>(hh, HttpStatus.SEE_OTHER);
		}

		//List<File> files = co.getFileList().stream().map(s -> new File(co.getUploadPath(),s.getFileNm())).collect(Collectors.toList());

		String d = DateUtils.getToday(Const.DATE_FORMAT);

		String fileName = d + "_" + co.getBsnsNm() + "_첨부파일.zip";
		try {
			return ResponseEntity.ok()
					  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"))
					  .body(out -> {
						  var zipOutputStream = new ZipOutputStream(out);

						  for (CtbnyOpratnFile f : co.getFileList()) {
							File file = new File(co.getUploadPath(), f.getFileNm());
		                    zipOutputStream.putNextEntry(new ZipEntry(f.getOrginlFileNm()));
		                    FileInputStream fileInputStream = new FileInputStream(file);

		                    fileInputStream.transferTo(zipOutputStream);

		                    //IOUtils.copy(fileInputStream, zipOutputStream);

		                    fileInputStream.close();
		                    zipOutputStream.closeEntry();
						  }

			                zipOutputStream.close();
					  });
		} catch (RuntimeException e) {
			return new ResponseEntity<StreamingResponseBody>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			return new ResponseEntity<StreamingResponseBody>(HttpStatus.INTERNAL_SERVER_ERROR);
		}


	}

	@GetMapping("file/download/{registFileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long registFileId) {

		CtbnyOpratnFile cof = giveOperationService.getCtbnyOpratnFile(registFileId);

		if (cof == null) return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);

		String fullPath = new CtbnyOpratn().getUploadPath() + File.separator + cof.getFileNm();

		Resource resource = new FileSystemResource(fullPath);

		if (!resource.exists()) return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);

		HttpHeaders header = new HttpHeaders();
		Path filePath = null;

		try {
			String contentType = Files.probeContentType(Paths.get(fullPath));
			if (contentType == null || "".equals(contentType)) contentType = "application/octet-stream";

			return ResponseEntity.ok()
								 .contentType(MediaType.parseMediaType(contentType))
								 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ new String(cof.getOrginlFileNm().getBytes("UTF-8"), "ISO-8859-1"))
								 .body(resource);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("list/{locgovCode}/popup/{registSn}/delete")
	public JsonView deleteCtbnyOpratn(@PathVariable String locgovCode, @PathVariable Long registSn) {

		LocManagerCheck ac = giveOperationService.findAdminRoleAndLocgov(locgovCode, null);

		// 지자체 인원만 수정 가능
		if (!ac.getIsLoc()) return JsonViewUtils.failure(MessageUtils.getMessage("지자체 관리자만 이용 가능합니다."));
		else if (ac.getIsLoc() && !ac.isPass()) return JsonViewUtils.failure(ac.getMessage());

		try {
			return JsonViewUtils.success(giveOperationService.deleteCtbnyOpratn(registSn));
		} catch (RuntimeException e) {
			return JsonViewUtils.failure("삭제 중 오류가 발생했습니다.");
		} catch (Exception e) {
			// TODO: handle exception
			return JsonViewUtils.failure("삭제 중 오류가 발생했습니다.");
		}

	}


	@PostMapping("list/{locgovCode}")
	public String giveOperationListByLocgovPost(@ModelAttribute("searchParam") GiveOperationSearchParam searchParam, @PathVariable String locgovCode, Model model) {
		return giveOperationListByLocgov(searchParam, locgovCode, model);
	}

}
