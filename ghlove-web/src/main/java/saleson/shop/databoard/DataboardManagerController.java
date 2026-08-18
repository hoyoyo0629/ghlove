package saleson.shop.databoard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.NotAjaxRequestException;
import com.onlinepowers.framework.i18n.support.CodeResolver;
import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.SecurityUtils;
import com.onlinepowers.framework.util.ViewUtils;
import com.onlinepowers.framework.web.bind.annotation.RequestProperty;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.onlinepowers.framework.web.servlet.view.JsonView;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.common.Const;
import saleson.common.utils.CommonUtils;
import saleson.seller.main.SellerService;
import saleson.seller.main.support.SellerParam;
import saleson.shop.databoard.DataboardService;
import saleson.shop.databoard.domain.Databoard;
import saleson.shop.databoard.domain.DataboardFile;
import saleson.shop.databoard.support.DataboardParam;
import saleson.shop.notice.domain.Notice;
import saleson.shop.notice.support.NoticeParam;

@Controller
@RequestMapping("/opmanager/data-board/**")
@RequestProperty(title="자료실", layout="default", template="opmanager")
public class DataboardManagerController {
    private static final Logger log = LoggerFactory.getLogger(DataboardManagerController.class);

    @Autowired
    private DataboardService databoardService;

    @Autowired
    private CodeResolver codeResolver;

    @Autowired
    private SellerService sellerService;

    @GetMapping("list")
    public String list(RequestContext requestContext, @ModelAttribute("searchParam") DataboardParam databoardParam , Model model) {

		String today1 = DateUtils.getToday("yyyyMMdd");
		databoardParam.setStartCreateDate(StringUtils.defaultIfEmpty(databoardParam.getStartCreateDate(), today1));
		databoardParam.setEndCreateDate(StringUtils.defaultIfEmpty(databoardParam.getEndCreateDate(), today1));

        int count = 0;

        Pagination pagination = Pagination.getInstance(count, databoardParam.getItemsPerPage());
        databoardParam.setPagination(pagination);
        //List<Code> subCategoryCode = CodeUtils.getCodeList("NOTICE_SUB_CATEGORY");
        //List<Code> categoryTeamCode = CodeUtils.getCodeList("NOTICE_CATEGORY_TEAM");

        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

        model.addAttribute("list", Collections.EMPTY_LIST);
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);
        //model.addAttribute("subCategoryCode", subCategoryCode);
        //model.addAttribute("categoryTeamCode", categoryTeamCode);

        return "view:/data-board/list";

    }

    @PostMapping("list")
    public String searchList(RequestContext requestContext, @ModelAttribute("searchParam") DataboardParam databoardParam , Model model) {

		String today1 = DateUtils.getToday("yyyyMMdd");
		databoardParam.setStartCreateDate(StringUtils.defaultIfEmpty(databoardParam.getStartCreateDate(), today1));
		databoardParam.setEndCreateDate(StringUtils.defaultIfEmpty(databoardParam.getEndCreateDate(), today1));

        int count = databoardService.getDataboardCount(databoardParam);

        Pagination pagination = Pagination.getInstance(count, databoardParam.getItemsPerPage());
        databoardParam.setPagination(pagination);
        //List<Code> subCategoryCode = CodeUtils.getCodeList("NOTICE_SUB_CATEGORY");
        //List<Code> categoryTeamCode = CodeUtils.getCodeList("NOTICE_CATEGORY_TEAM");

        String today = DateUtils.getToday(Const.DATE_FORMAT);

        model.addAttribute("today", today);
        model.addAttribute("week", DateUtils.addYearMonthDay(today, 0, 0, -7));
        model.addAttribute("month1", DateUtils.addYearMonthDay(today, 0, -1, 0));
        model.addAttribute("month2", DateUtils.addYearMonthDay(today, 0, -2, 0));

        model.addAttribute("list", databoardService.getDataboardList(databoardParam));
        model.addAttribute("pagination", pagination);
        model.addAttribute("count", count);
        //model.addAttribute("subCategoryCode", subCategoryCode);
        //model.addAttribute("categoryTeamCode", categoryTeamCode);

        return "view:/data-board/list";

    }

	@GetMapping("create")
	public String create(RequestContext requestContext, Model model, Databoard databoard){

		SellerParam sellerParam = new SellerParam();
		//sellerParam.setStatusCode("2");
		//model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		return ViewUtils.view();
	}


    @PostMapping("create")
    public String createAction(RequestContext requestContext,
    		Databoard databoard,
    		@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles){

        databoard.setUserName(SecurityUtils.getCurrentUser().getUserName());
        databoard.setItemDetailImageFiles(detailImageFiles);
        databoardService.insertDataboard(databoard);

        return ViewUtils.redirect("/opmanager/data-board/list","등록되었습니다.");
    }

	@GetMapping("edit/{dataId}")
	public String edit(RequestContext requestContext, Model model, @PathVariable("dataId") int dataId, @ModelAttribute("searchParam") DataboardParam searchParam ){


		SellerParam sellerParam = new SellerParam();
		sellerParam.setStatusCode("2");
		//model.addAttribute("sellerList", sellerService.getSellerListByParam(sellerParam));
		model.addAttribute("databoard", databoardService.getDataboard(dataId));
		//model.addAttribute("noticeSellerList", databoardService.getNoticeSellerList(dataId));
		model.addAttribute("searchParam", searchParam);

		return ViewUtils.view();
	}


    @PostMapping("edit/{dataId}")
    public String editAction(RequestContext requestContext, Databoard databoard,
    		@RequestParam(value="detailImageFiles[]", required=false) MultipartFile[] detailImageFiles){
        databoard.setUserName(SecurityUtils.getCurrentUser().getUserName());
        databoard.setItemDetailImageFiles(detailImageFiles);
        databoardService.updateDataboard(databoard);

        return ViewUtils.redirect("/opmanager/data-board/list", MessageUtils.getMessage("M00289"));
    }

    @PostMapping("delete/{dataId}")
    public JsonView delete(RequestContext requestContext, @PathVariable int dataId) {
        if (!requestContext.isAjaxRequest()) {
            throw new NotAjaxRequestException();
        }

        try {
            databoardService.deleteDataboard(dataId);
        } catch (RuntimeException e) {
        	return JsonViewUtils.exception("삭제에 실패했습니다.");
        } catch (Exception e) {
            return JsonViewUtils.exception("삭제에 실패했습니다.");
        }
        return JsonViewUtils.success();
    }

    @PostMapping("/deleteDataboard")
    public JsonView databoardListDelete(RequestContext requestContext, DataboardParam databoardParam) {
        String code = "";

        try {
            code = databoardService.databoardListDelete(databoardParam);
        } catch (RuntimeException e) {
        	log.error("ERROR: {}", e.getStackTrace()[0]);
        } catch (Exception e) {
            log.error("ERROR: {}", "에러가 발생했습니다.");
        }

        return JsonViewUtils.success(code);
    }

    /**
     * /** Date 조회
     *
     * @param requestContextCREATED_DATE
     * @return
     */
    @PostMapping("search-date")
    public JsonView searchDate(RequestContext requestContext, Model model,
                               @RequestParam(value = "date", required = false) int date,
                               @RequestParam(value = "type", required = false) String type) {

        int month = 0;
        int day = 0;

        if (type.equals("week")) {
            day = date;
        } else if (type.equals("month")) {
            month = date;
        }

        String nowDate = DateUtils.getToday(Const.DATE_FORMAT);
        String searchDate = DateUtils.addYearMonthDay(nowDate, 0, month, day);

        model.addAttribute("nowDate", nowDate);
        model.addAttribute("searchDate", searchDate);

        return JsonViewUtils.success();
    }

    /**
	 * 파일를 삭제한다.
	 * @param requestContext
	 * @param itemId
	 * @return
	 */
	@PostMapping("delete-item-image")
	public JsonView deleteItemImage(RequestContext requestContext, @RequestParam("itemId") String itemId) {
		if (!requestContext.isAjaxRequest()) {
			throw new NotAjaxRequestException();
		}

		databoardService.deleteItemImageByItemId(itemId);

		return JsonViewUtils.success();
	}

	/**
	 * 첨부파일 다운로드
	 * @param dataFileId
	 * @return
	 */
	@GetMapping("/file-download/{dataFileId}")
	public ResponseEntity<?> fileDownload(@PathVariable String dataFileId) throws IOException {
		DataboardFile databoardFile = databoardService.getFrontDataboardFileDetail(dataFileId);
		File file = new File(CommonUtils.dataNvl(databoardFile.getFileSrc()));

		if (file.exists()) {
			HttpHeaders header = new HttpHeaders();

			String fileName  = URLEncoder.encode(CommonUtils.dataNvl(databoardFile.getOrgFileName()), StandardCharsets.UTF_8);

			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");

			try (FileInputStream fis = new FileInputStream(file)) {
//				InputStreamResource resource3 = new InputStreamResource(fis);
				byte[] bytes = FileCopyUtils.copyToByteArray(fis);
				ByteArrayResource resource = new ByteArrayResource(bytes);

				return ResponseEntity.ok()
						.headers(header)
						.contentLength(file.length())
						.contentType(MediaType.parseMediaType("application/octet-stream"))
//						.body(resource3);
						.body(resource);
			} catch (IOException e) {
				throw e;
			}
		}

		return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
	}
}
