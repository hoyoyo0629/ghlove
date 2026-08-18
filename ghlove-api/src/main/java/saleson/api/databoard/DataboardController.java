package saleson.api.databoard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.onlinepowers.framework.util.DateUtils;
import com.onlinepowers.framework.web.pagination.Pagination;

import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.databoard.domain.DataboardInfo;
import saleson.common.opmanager.count.OpmanagerMainCount;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.LocalDateUtils;
import saleson.shop.databoard.DataboardService;
import saleson.shop.databoard.domain.Databoard;
import saleson.shop.databoard.domain.DataboardFile;
import saleson.shop.databoard.support.DataboardParam;
import saleson.shop.give.givestate.domain.CallState;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.main.MainMapper;
import saleson.shop.main.MainService;

@RestController("ApiDataboardController")
@RequestMapping("/api/data-board")
public class DataboardController {
	private static final Logger log = LoggerFactory.getLogger(DataboardController.class);

	@Autowired
	private MainService mainService;

	@Autowired
	private MainMapper mainMapper;

	/** 자료실 Service */
	@Autowired
	DataboardService databoardService;

	/**
	 * 자료실 목록 조회
	 * @param databoardParam
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> list(DataboardParam databoardParam) {
		ResponseEntity<?> result = null;

		try {
			if(databoardParam.getPage() <= 0) {
				databoardParam.setPage(1);
			}

			int totalCount = databoardService.getFrontDataboardListCount(databoardParam);

			Pagination pagination = Pagination.getInstance(totalCount, databoardParam.getItemsPerPage());
			databoardParam.setPagination(pagination);

			List<Databoard> list = databoardService.getFrontDataboardList(databoardParam);

			List<DataboardInfo> infoList = new ArrayList<>();
			if (!list.isEmpty()) {
				for (Databoard databoard : list) {
					infoList.add(new DataboardInfo(databoard));
				}
			}

			result = ApiResponseEntity
					.data()
					.put("totalCount", totalCount)
					.list(infoList)
					.pagination(pagination)
					.ok();
		} catch (RuntimeException e) {
			log.error("DataboardController List {}",e.getStackTrace()[0]);
		} catch (Exception e) {
			log.error("DataboardController List",e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 자료실 상세 조회
	 * @param databoardParam
	 * @return
	 */
	@GetMapping("/detail")
	public ResponseEntity<?> detail(DataboardParam databoardParam)  {
		ResponseEntity<?> result = null;
		Databoard databoardDetail = null;
		List<DataboardFile> databoardFileList = null;

		try {
			// id 값 null일 경우, 목록으로 리다이렉션
			if (databoardParam.getDataId() == null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(URI.create("/api/data-board"));
				return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
			}


			// 자료실 상세 조회
			databoardDetail = databoardService.getFrontDataboardDetail(databoardParam.getDataId());

			// 자료실 첨부 파일
			databoardFileList = databoardService.getFrontDataboardFileList(databoardParam.getDataId());

			result = ApiResponseEntity
					.data()
					.put("detail", new DataboardInfo(databoardDetail))
					.put("fileList", databoardFileList)
					.ok();
		} catch (RuntimeException e) {
			log.error("DataboardController Detail{}",e.getStackTrace()[0]);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		} catch (Exception e) {
			log.error("DataboardController Detail",e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
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
			// 파일명
			String fileName = URLEncoder.encode(CommonUtils.dataNvl(databoardFile.getOrgFileName()), StandardCharsets.UTF_8);

			// 파일헤더
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");

			try(FileInputStream fis = new FileInputStream(file)) {
//				InputStreamResource resource3 = new InputStreamResource(fis);
				byte[] bytes = FileCopyUtils.copyToByteArray(fis);
				ByteArrayResource resource = new ByteArrayResource(bytes);

				return ResponseEntity.ok()
							.headers(header)
							.contentLength(file.length())
							.contentType(MediaType.parseMediaType("application/octet-stream"))
//							.body(resource3);
							.body(resource);
			} catch (IOException e) {
				throw e;
			}


		}

		return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
	}

	/**
	 * 보고용 문자 서비스
	 * @param
	 * @return
	 */
	@GetMapping("/dash-board")
	public ResponseEntity<Map<String,Object>> reportDashborad(GiveState searchParam) throws IOException {
	  Map<String, Object> boardMap = new HashMap<>();
	  String today = DateUtils.getToday();

	  long gift =  mainService.opmanageMainAmountGift(searchParam);

	  searchParam.setShCntrDeStart(today);
	  List<OpmanagerMainCount> mainInfo =mainService.getOpmanagerMainTableInfo(searchParam);

	  CallState callInfo = mainService.getOpmanagerMainCallTableInfo(searchParam);
	  for(OpmanagerMainCount main: mainInfo) {
		  boardMap.put(main.getId(),main.getCount());
	  }
	  String locgovCd = null;
	  Map<String, String> param = new HashMap<>();
	  param.put("today", LocalDateUtils.localDateToString(LocalDate.now()));
	  param.put("locgovCd", locgovCd);
	  param.put("userRole", "SYS");

	  List<OpmanagerMainCount> user = mainMapper.getOpmanagerMainInfo(param);

	  for(OpmanagerMainCount amount: user) {
		  if(amount.getId().equals("all-subscribers")) {
			  boardMap.put("전체회원", amount.getCount());
		  }
	  }

	  boardMap.put("대국민콜수", callInfo.getCallKookmin());
	  boardMap.put("농협콜수", callInfo.getCallNhbank());
	  boardMap.put("지자체콜수", callInfo.getCallLov());
	  boardMap.put("답례품콜수", callInfo.getCallGiver());
	  boardMap.put("총콜수", callInfo.getCallTotal());
	  boardMap.put("amount", gift);



	return new ResponseEntity<>(boardMap,HttpStatus.OK);

	}
}
