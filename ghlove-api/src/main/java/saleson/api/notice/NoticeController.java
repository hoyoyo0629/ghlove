package saleson.api.notice;

import com.onlinepowers.framework.web.pagination.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.notice.domain.NoticeInfo;
import saleson.shop.notice.NoticeService;
import saleson.shop.notice.domain.Notice;
import saleson.shop.notice.support.NoticeParam;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

@RestController("ApiNoticeController")
@RequestMapping("/api/notice")
public class NoticeController {
	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

	@Autowired
	NoticeService noticeService;

	/**
	 * 공지사항 목록 조회
	 * @param noticeParam
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> list(NoticeParam noticeParam) {
		ResponseEntity<?> result = null;

		try {
			if (noticeParam.getPage() <= 0) {
				noticeParam.setPage(1);
			}

			noticeParam.setUseYn("Y");
			int totalCount = noticeService.getFrontNoticeListCount(noticeParam);

			Pagination pagination = Pagination.getInstance(totalCount, noticeParam.getItemsPerPage());
			noticeParam.setPagination(pagination);

			List<Notice> list = noticeService.getFrontNoticeList(noticeParam);

			List<NoticeInfo> infoList = new ArrayList<>();
			if (!list.isEmpty()) {
				for (Notice notice : list) {
					infoList.add(new NoticeInfo(notice));
				}
			}

			result = ApiResponseEntity
						.data()
						.put("totalCount", totalCount)
						.list(infoList)
						.pagination(pagination)
						.ok();

		} catch (RuntimeException e) {
			logger.error("NoticeController List",e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 공지사항 조회수 증가
	 * @param noticeParam
	 * @return
	 */
	@PostMapping("/hits")
	public ResponseEntity<?> updateHits(@RequestBody @Valid Notice notice) {
		ResponseEntity<?> result = null;

		try {
			noticeService.addHitCount(notice.getNoticeId());
			result = ApiResponseEntity.data().ok();
		} catch (RuntimeException e) {
			logger.error("NoticeController hits",e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}
	
	/**
	 * 공지사항 상세
	 * @param noticeParam
	 * @return
	 */
	@PostMapping("/detail")
	public ResponseEntity<?> detail(@RequestBody @Valid Notice notice) {
		ResponseEntity<?> result = null;

		try {
			NoticeInfo detail = new NoticeInfo(noticeService.getFrontNotice(notice.getNoticeId()));
			result = ApiResponseEntity.data().put("detail", detail).ok();
		} catch (RuntimeException e) {
			logger.error("NoticeController detail",e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}


	/**
	 * 지자체 기금사업 소개 조회
	 * @param noticeParam
	 * @return
	 */
	@GetMapping("/donation/list")
	public ResponseEntity<?> donationList(NoticeParam noticeParam) {
		ResponseEntity<?> result = null;

		try {
			if (noticeParam.getPage() <= 0) {
				noticeParam.setPage(1);
			}

			noticeParam.setUseYn("Y");
			noticeParam.setBoardCode("DONATION");
			int totalCount = noticeService.getFrontNoticeListCount(noticeParam);

			Pagination pagination = Pagination.getInstance(totalCount, noticeParam.getItemsPerPage());
			noticeParam.setPagination(pagination);

			List<Notice> list = noticeService.getFrontNoticeList(noticeParam);

			List<NoticeInfo> infoList = new ArrayList<>();
			if (!list.isEmpty()) {
				for (Notice notice : list) {
					infoList.add(new NoticeInfo(notice));
				}
			}

			result = ApiResponseEntity
						.data()
						.put("totalCount", totalCount)
						.list(infoList)
						.pagination(pagination)
						.ok();

		} catch (RuntimeException e) {
			logger.error("NoticeController List",e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}
	
}
