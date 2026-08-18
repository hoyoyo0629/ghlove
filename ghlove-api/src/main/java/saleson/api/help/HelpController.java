package saleson.api.help;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.help.domain.HelpInfo;
import saleson.common.file.service.CustomFileService;
import saleson.common.utils.CommonUtils;
import saleson.shop.help.HelpService;
import saleson.shop.help.domain.Help;
import saleson.shop.help.support.HelpParam;

@RestController("ApiHelpController")
@RequestMapping("/api/help")
public class HelpController {
	private static final Logger log = LoggerFactory.getLogger(HelpController.class);

	@Autowired
	private CustomFileService customFileService;

	/** 도움말 Service */
	@Autowired
	HelpService helpService;

	/**
	 * 도움말 목록 조회
	 * @param helpParam
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> list(HelpParam helpParam) {
		ResponseEntity<?> result = null;

		try {
			if (helpParam.getPage() <= 0) {
				helpParam.setPage(1);
			}

			int totalCount = helpService.getFrontHelpListCount(helpParam);

			Pagination pagination = Pagination.getInstance(totalCount, helpParam.getItemsPerPage());
			helpParam.setPagination(pagination);

			List<Help> list = helpService.getFrontHelpList(helpParam);

			List<HelpInfo> infoList = new ArrayList<>();
			if(!list.isEmpty()) {
				for(Help help : list) {
					infoList.add(new HelpInfo(help));
				}
			}

			result = ApiResponseEntity
					.data()
					.put("totalCount", totalCount)
					.list(infoList)
					.pagination(pagination)
					.ok();

		} catch (RuntimeException e) {
			log.error("[/api/help] ERROR : 도움말 목록 조회");
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 도움말 상세 조회
	 * @param helpParam
	 * @return
	 */
	@GetMapping("/detail")
	public ResponseEntity<?> detail(HelpParam helpParam) {
		ResponseEntity<?> result = null;
		Help helpDetail = null;

		try {

			helpDetail = helpService.getFrontHelpDetail(helpParam);

			result = ApiResponseEntity
					.data()
					.put("detail", new HelpInfo(helpDetail))
					.ok();

		} catch (RuntimeException e) {
			log.error("[/api/help/detail] ERROR : 도움말 상세 조회");
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 첨부파일 다운로드
	 * @param dataFileId
	 * @return
	 */
	@GetMapping("/file-download/{mnlSn}")
	public ResponseEntity<?> fileDownload(@PathVariable Integer mnlSn, HelpParam helpParam) throws IOException {
		Help helpDetail = helpService.getFrontHelpDetail(helpParam);
		File file = new File(CommonUtils.dataNvl(helpDetail.getFileSrc()));

		if (file.exists()) {
			// 파일명
			return customFileService.getFileDownloadByGhlove(CommonUtils.dataNvl(helpDetail.getOrginlFileNm()), file);
		}

		return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
	}

	/**
	 * 첨부파일 정보 조회
	 * @param helpParam
	 * @return
	 */
	@GetMapping("/comm/file-info")
	public ResponseEntity<?> commFileDownload(HelpParam helpParam) {
		ResponseEntity<?> result = null;
		Help fileInfo = null;

		try {

			fileInfo = helpService.getFrontHelpFileInfo(helpParam);

			result = ApiResponseEntity
					.data()
					.put("fileInfo", new HelpInfo(fileInfo))
					.ok();

		} catch (RuntimeException e) {
			log.error("[/api/comm/file-info] ERROR : 도움말 첨부파일 정보 조회");
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}
}
