package saleson.api.qna;

import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.web.pagination.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import saleson.api.common.ApiResponseEntity;
import saleson.api.common.enumerated.ApiError;
import saleson.api.common.enumerated.DonationError;
import saleson.api.common.exception.DonationException;
import saleson.api.qna.domain.QnaOpenInfo;
import saleson.api.qna.support.QnaDataSupport;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.EmojiUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.banword.BanWordService;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.Item;
import saleson.shop.qna.QnaService;
import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.domain.QnaOpen;
import saleson.shop.qna.domain.QnaOpenFile;
import saleson.shop.qna.support.QnaOpenParam;
import saleson.shop.qna.support.QnaParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController("ApiQnaController")
@RequestMapping("/api/qna")
public class QnaController {
	private static final Logger log = LoggerFactory.getLogger(QnaController.class);

	@Autowired
	private QnaService qnaService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private QnaDataSupport qnaDataSupport;

	@Autowired
	private BanWordService banWordService;

	@Autowired
	private DataMasking dataMasking;

	/**
	 * 1:1문의 리스트
	 * */
	@GetMapping("/inquiry")
	public ResponseEntity<?> inquiry(HttpServletRequest request, QnaParam qnaParam) {
		ResponseEntity<?> result = null;

		if (qnaParam == null) {
			qnaParam = new QnaParam();
		}

		if (!UserUtils.isUserLogin()) {
			return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
		}

		qnaParam.setQnaType(Qna.QNA_GROUP_TYPE_INDIVIDUAL);
		qnaParam.setUserId(UserUtils.getUserId());
		qnaParam.setWhere("QUESTION");
		qnaParam.setUseYn("Y");

		try {
			List<Qna> qnaList = qnaService.getQnaListByParam(qnaParam);

			for(Qna qna : qnaList) {
				QnaOpenParam qnaOpenParam = new QnaOpenParam();
				qnaOpenParam.setQnaId(qna.getQnaId());
				qna.setQnaOpenFileList(qnaService.getFrontQnaOpenFileList(qnaOpenParam));

				if(qna.getQnaAnswer() != null && CommonUtils.intNvl(qna.getQnaAnswer().getQnaAnswerId()) > 0) {
					qnaOpenParam.setQnaAnswerId(qna.getQnaAnswer().getQnaAnswerId());
					qna.setQnaOpenAnswerFileList(qnaService.getFrontQnaOpenFileList(qnaOpenParam));
				}
			}

			result = ApiResponseEntity.data()
					.put("qnaGroups", CodeUtils.getCodeList("QNA_GROUPS"))
					.list(qnaDataSupport.qnaDataSet(qnaList, UserUtils.getUserId()))
					.ok();

		} catch (RuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 1:1문의 작성
	 * */
	@PostMapping("/inquiry")
	public ResponseEntity<?> addInquiry(QnaOpen qnaOpen) {
		ResponseEntity<?> result = null;

		try {

			// 1. 로그인 여부 확인
			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			// 2. 이모티콘 제거 (얼굴 모양, 등등)
			qnaOpen.setSubject(EmojiUtils.removeEmoticon(qnaOpen.getSubject()));
			qnaOpen.setQuestion(EmojiUtils.removeEmoticon(qnaOpen.getQuestion()));

			// 3. 금기어 확인
			String subjectBanWord = banWordService.getCheckedBanWord(qnaOpen.getSubject());
			String questionBanWord = banWordService.getCheckedBanWord(qnaOpen.getQuestion());

			// 5분이내 글등록 불가
			int resultCheck = qnaService.getFiveMinuteCheck(qnaOpen);

			if(resultCheck > 0 ) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST, "문의사항 재등록은\n5분후 가능 합니다.");
			}

			if (!ObjectUtils.isEmpty(subjectBanWord)) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST, "제목에 금지어가 포함되어 있습니다.["+subjectBanWord+"]");
			}

			if (!ObjectUtils.isEmpty(questionBanWord)) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST, "내용에 금지어가 포함되어 있습니다.["+questionBanWord+"]");
			}

			// 4. QNA 등록
			qnaOpen.setQnaType(Qna.QNA_GROUP_TYPE_INDIVIDUAL);
			String code = qnaService.insertQnaOpen(qnaOpen);

			// 5. 결과값
			if("SUCC".equals(code)) {
				result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
			} else if("ERR_FILE_SIZE".equals(code)) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST, "업로드 가능한 최대 용량 : 5MB 입니다.");
			} else if("ERR_FILE_EXT".equals(code)) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST, "유효하지 않은 파일입니다.");
			} else {
				result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
			}

		} catch (RuntimeException e) {
			log.error("[/api/qna/inquiry] ERROR : 1:1문의 작성", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 1:1문의 삭제
	 * */
	@PostMapping("/delete-inquiry")
	public ResponseEntity deleteInquiry(HttpServletRequest request, @RequestBody(required = false) QnaParam qnaParam) {
		ResponseEntity result = null;
		int qnaId = Integer.parseInt(qnaParam.getQnaId());
		try {

			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			Qna tempQna = qnaService.getQnaByQnaId(qnaId);

			if (UserUtils.getUserId() != tempQna.getUserId()) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST_INQUIRY_DELETE_FAIL_USER);
			}

			if (tempQna.getAnswerCount() > 0) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST_INQUIRY_DELETE_FAIL);
			} else {
				Qna qna = new Qna();
				qna.setQnaId(qnaId);
				qnaService.deleteQna(qna);
				result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
			}
		} catch (RuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}
		return result;
	}

	/**
	 * 상품문의(고객센터 QnA) 리스트
	 * */
	@GetMapping("/item-inquiry")
	public ResponseEntity itemInquiry(HttpServletRequest request, QnaParam qnaParam) {
		ResponseEntity result = null;
		List<Qna> list = null;
		Pagination pagination = null;
		if (qnaParam == null) {
			qnaParam = new QnaParam();
		}
		if (qnaParam != null && qnaParam.getSearchStartDate() != null || qnaParam.getQuery() != null) {
			qnaParam.setWhere("ITEM_NAME");
			qnaParam.setQnaType(Qna.QNA_GROUP_TYPE_ITEM);
			qnaParam.setUserId(UserUtils.getUserId());
			qnaParam.setSearchStartDate(qnaParam.getSearchStartDate().replaceAll("-", ""));
			qnaParam.setSearchEndDate(qnaParam.getSearchEndDate().replaceAll("-", ""));
		}

		try {

			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			pagination = Pagination.getInstance(qnaService.getQnaListCountByParam(qnaParam));
			qnaParam.setPagination(pagination);
			list = qnaService.getQnaListByParam(qnaParam);
			result = ApiResponseEntity.data().list(qnaDataSupport.qnaDataSet(list, UserUtils.getUserId())).pagination(pagination).put("status", HttpStatus.OK).ok();
		} catch (RuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}
		return result;
	}

	/**
	 * 상품문의(고객센터 QnA) 작성 API (itemId, secretFlag, subject, question, qnaGroup)
	 *
	 * @param qna
	 * @return
	 */
	@PostMapping("/item-inquiry")
	public ResponseEntity addItemInquiry(@RequestBody @Valid Qna qna, BindingResult bindingResult) {
		ResponseEntity result = null;
		Item item = null;

		try {
			if (bindingResult.hasErrors()) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST);
			}

			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			item = itemService.getItemBy(qna.getItemId());

			if (item != null) {

				// 이모티콘 제거 (얼굴 모양, 등등)
				qna.setSubject(EmojiUtils.removeEmoticon(qna.getSubject()));
				qna.setQuestion(EmojiUtils.removeEmoticon(qna.getQuestion()));

				qna.setEmail(UserUtils.getEmail());

				if (ObjectUtils.isEmpty(UserUtils.getEmail())) {
					qna.setEmail("");
				}

				qna.setUserName(UserUtils.getUser().getUserName());
				qna.setQnaType(Qna.QNA_GROUP_TYPE_ITEM);
				qna.setSellerId(item.getSellerId());
				qna.setItemId(item.getItemId());

				qnaService.insertQna(qna);

				result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
			} else {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST_NO_ITEM);
			}
		} catch (RuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}
		return result;
	}

	/**
	 * 상품문의(고객센터 QnA) 삭제
	 * */
	@PostMapping("/delete-item-inquiry")
	public ResponseEntity deleteItemInquiry(HttpServletRequest request, @RequestBody(required = false) QnaParam qnaParam) {
		ResponseEntity result = null;
		int qnaId = Integer.parseInt(qnaParam.getQnaId());
		try {

			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			Qna tempQna = qnaService.getQnaByQnaId(qnaId);

			if (UserUtils.getUserId() != tempQna.getUserId()) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST_INQUIRY_DELETE_FAIL_USER);
			}

			if (tempQna.getAnswerCount() > 0) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST_INQUIRY_DELETE_FAIL);
			} else {
				Qna qna = new Qna();
				qna.setQnaId(qnaId);
				qnaService.deleteQna(qna);
				result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
			}
		} catch (RuntimeException e) {
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}
		return result;
	}

	/**
	 * QNA 목록 조회
	 * @param qnaParam
	 * @return
	 */
	@GetMapping("/qna-open")
	public ResponseEntity<?> qnaOpenList(QnaOpenParam qnaOpenParam) {
		ResponseEntity<?> result = null;

		//Loginid가 확인되지 않을 경우 비정상적인 접근으로 처리
		if(UserUtils.getLoginId()== null) {
			return result = ApiResponseEntity.error(ApiError.BAD_REQUEST);
		}
		try {
			if (qnaOpenParam.getPage() <= 0) {
				qnaOpenParam.setPage(1);
			}

			int totalCount = qnaService.getFrontQnaOpenListCount(qnaOpenParam);
			int questionCount = qnaService.getFrontQnaOpenQuestionListCount(qnaOpenParam);

			Pagination pagination = Pagination.getInstance(totalCount, qnaOpenParam.getItemsPerPage());
			qnaOpenParam.setPagination(pagination);

			List<QnaOpen> list = qnaService.getFrontQnaOpenList(qnaOpenParam);

			List<QnaOpenInfo> infoList = new ArrayList<>();
			if (!list.isEmpty()) {
				for(QnaOpen qnaOpen : list) {
					if(!"".equals(CommonUtils.dataNvl(qnaOpen.getUserName()))) {
						qnaOpen.setUserName(dataMasking.mask(qnaOpen.getUserName(), Masking.NAME));
					}
					/*if("N".equals(qnaOpen.getQnaSecretFlag())) {
						infoList.add(new QnaOpenInfo(qnaOpen));
					}else if(UserUtils.getUserId() == qnaOpen.getQnaUserId()) {
						infoList.add(new QnaOpenInfo(qnaOpen));
					}*/
					infoList.add(new QnaOpenInfo(qnaOpen));

				}
			}
			//int count = Collections.frequency(infoList, "A");
			//int totalCount = infoList.size();

			//Pagination pagination = Pagination.getInstance(totalCount, qnaOpenParam.getItemsPerPage());
			//qnaOpenParam.setPagination(pagination);

			result = ApiResponseEntity
					.data()
					.put("totalCount", totalCount)
					.put("questionCount", questionCount)
					.list(infoList)
					.pagination(pagination)
					.ok();

		} catch (RuntimeException e) {
			log.error("[/api/qna/qna-open] ERROR : QNA 목록 조회", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * QNA 상세
	 * @param qnaOpenParam
	 * @return
	 */
	@GetMapping("/qna-detail")
	public ResponseEntity<?> qnaOpenDetail(QnaOpenParam qnaOpenParam) {
		ResponseEntity<?> result = null;
		QnaOpen qnaOpenDetail = null;
		List<QnaOpenFile> qnaOpenFileList = null;

		try {

			// QNA 상세 조회
			qnaOpenDetail = qnaService.getFrontQnaOpenDetail(qnaOpenParam);

			if (("Y".equals(qnaOpenDetail.getSecretFlag()) || "Y".equals(qnaOpenDetail.getQnaSecretFlag()))
					&& UserUtils.getUserId() != qnaOpenDetail.getQnaUserId()) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST_INQUIRY_FAIL_USER);
			}

			// QNA 파일 목록 조회
			qnaOpenFileList = qnaService.getFrontQnaOpenFileList(qnaOpenParam);

			// 글쓴이 여부
			String writerFlag = "N";
			Long loginUser = CommonUtils.longNvl(UserUtils.getUserId());
			if(loginUser != null && loginUser != 0 && qnaOpenDetail != null
					&& String.valueOf(qnaOpenDetail.getUserId()).equals(String.valueOf(loginUser))) {
				writerFlag = "Y";
			}

			// 작성자 성명 마스킹처리
			if(qnaOpenDetail != null && !"".equals(CommonUtils.dataNvl(qnaOpenDetail.getUserName()))) {
				qnaOpenDetail.setUserName(dataMasking.mask(qnaOpenDetail.getUserName(), Masking.NAME));
			}

			result = ApiResponseEntity
					.data()
					.put("writerFlag", writerFlag)
					.put("detail", new QnaOpenInfo(qnaOpenDetail))
					.put("fileList", qnaOpenFileList)
					.ok();

		} catch (RuntimeException e) {
			log.error("[/api/qna/qna-detail] ERROR : QNA 상세", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * 첨부파일 다운로드
	 * @param qnaOpenParam
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/qna-open/file-download/{qnaFileId}")
	public ResponseEntity<?> fileDownload(QnaOpenParam qnaOpenParam) throws IOException {
		QnaOpenFile qnaOpenFile = qnaService.getFrontQnaOpenFileDetail(qnaOpenParam);
		File file = new File(CommonUtils.dataNvl(qnaOpenFile.getFileSrc()));

		if (file.exists()) {
			// 파일명
			String fileName = URLEncoder.encode(CommonUtils.dataNvl(qnaOpenFile.getOrgFileName()), StandardCharsets.UTF_8);

			// 파일헤더
			HttpHeaders header = new HttpHeaders();
			header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+fileName);
			header.add("Cache-Control", "no-cache, no-store, must-revalidate");
			header.add("Pragma", "no-cache");
			header.add("Expires", "0");

			// [시큐어코딩] InputStreamResource 은 spring에 의해서 자원해제됨.
			try(FileInputStream fis = new FileInputStream(file)){
//				InputStreamResource resource3 = new InputStreamResource(new FileInputStream(file));

				byte[] bytes = FileCopyUtils.copyToByteArray(fis);
				ByteArrayResource resource = new ByteArrayResource(bytes);

				return ResponseEntity.ok()
						.headers(header)
						.contentLength(file.length())
						.contentType(MediaType.parseMediaType("application/octet-stream"))
//						.body(resource3);
						.body(resource);
			} catch (IOException e) {
				throw new IOException(e);
			}
		}

		return ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
	}

	/**
	 * QNA 분류 코드 조회
	 * @param qnaOpenParam
	 * @return
	 */
	@GetMapping("/qna-group")
	public ResponseEntity<?> qnaGroupList() {
		ResponseEntity<?> result = null;

		try {
			result = ApiResponseEntity.data()
						.list(CodeUtils.getCodeList("QNA_GROUPS")).ok();
		} catch (RuntimeException e) {
			log.error("[/api/qna/qna-group] ERROR : QNA 분류 코드 조회", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * QNA 등록
	 * @param qnaOpen
	 * @return
	 */
	@PostMapping("/qna-open")
	public ResponseEntity<?> addQnaOpen(QnaOpen qnaOpen) {
		ResponseEntity<?> result = null;

		try {

			// 1. 로그인 여부 확인
			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			// 2. 이모티콘 제거 (얼굴 모양, 등등)
			qnaOpen.setSubject(EmojiUtils.removeEmoticon(qnaOpen.getSubject()));
			qnaOpen.setQuestion(EmojiUtils.removeEmoticon(qnaOpen.getQuestion()));

			// 3. 금기어 확인
			String subjectBanWord = banWordService.getCheckedBanWord(qnaOpen.getSubject());
			String questionBanWord = banWordService.getCheckedBanWord(qnaOpen.getQuestion());

			if (!ObjectUtils.isEmpty(subjectBanWord)) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST, "제목에 금지어가 포함되어 있습니다.["+subjectBanWord+"]");
			}

			if (!ObjectUtils.isEmpty(questionBanWord)) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST, "내용에 금지어가 포함되어 있습니다.["+questionBanWord+"]");
			}

			// 4. QNA 등록
			qnaOpen.setQnaType(Qna.QNA_GROUP_TYPE_QNA);
			String code = qnaService.insertQnaOpen(qnaOpen);

			// 5. 결과값
			if("SUCC".equals(code)) {
				result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
			} else if("ERR_FILE_SIZE".equals(code)) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST, "업로드 가능한 최대 용량 : 5MB 입니다.");
			} else if("ERR_FILE_EXT".equals(code)) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST, "유효하지 않은 파일입니다.");
			} else {
				result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
			}

		} catch (RuntimeException e) {
			log.error("[/api/qna/qna-open] ERROR : QNA 등록", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * QNA 삭제
	 * @param qnaOpenParam
	 * @return
	 */
	@PostMapping("/qna-open/delete")
	public ResponseEntity<?> deleteQnaOpen(@RequestBody @Valid QnaOpenParam qnaOpenParam) {
		ResponseEntity<?> result = null;
		Integer qnaId = CommonUtils.intNvl(qnaOpenParam.getQnaId());

		try {

			// 1. 로그인 체크
			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			// 2. 작성자 확인
			Qna tempQna = qnaService.getQnaByQnaId(qnaId);
			if (UserUtils.getUserId() != tempQna.getUserId()) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST_INQUIRY_DELETE_FAIL_USER);
			}

			// 3. 답변이 존재하는지 확인
			if (tempQna.getAnswerCount() > 0) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST_INQUIRY_DELETE_FAIL);

			// 4. 문의 삭제
			} else {
				String code = qnaService.deleteQnaOpen(qnaOpenParam);

				if("SUCC".equals(code)) {
					result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
				} else if("ERR_NOT_DEL".equals(code)) {
					result = ApiResponseEntity.error(ApiError.BAD_REQUEST, "답변완료되어 삭제할 수 없습니다.");
				} else {
					result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
				}
			}

		} catch (RuntimeException e) {
			log.error("[/api/qna/qna-open/delete] ERROR : QNA 삭제", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * QNA 조회수 증가
	 * @param qnaOpenParam
	 * @return
	 */
	@PostMapping("/qna-open/hits")
	public ResponseEntity<?> updateHits(@RequestBody @Valid QnaOpenParam qnaOpenParam) {
		ResponseEntity<?> result = null;

		try {
			qnaService.addHitCount(qnaOpenParam);
			result = ApiResponseEntity.data().ok();
		} catch (RuntimeException e) {
			log.error("[/api/qna/qna-open/hits] ERROR : QNA 조회수 증가", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}

	/**
	 * QNA 수정
	 * @param qnaOpen
	 * @return
	 */
	@PostMapping("/qna-open/update")
	public ResponseEntity<?> updateQnaOpen(QnaOpen qnaOpen) {
		ResponseEntity<?> result = null;

		try {

			// 1. 로그인 여부 확인
			if (!UserUtils.isUserLogin()) {
				return ApiResponseEntity.error(ApiError.UNAUTHORIZED);
			}

			// 2. 작성자 확인
			Qna tempQna = qnaService.getQnaByQnaId(qnaOpen.getQnaId());
			if (UserUtils.getUserId() != tempQna.getUserId()) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST_INQUIRY_DELETE_FAIL_USER);
			}

			// 3. 답변이 존재하는지 확인
			if (tempQna.getAnswerCount() > 0) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST_INQUIRY_DELETE_FAIL);
			}

			// 4. 이모티콘 제거 (얼굴 모양, 등등)
			qnaOpen.setSubject(EmojiUtils.removeEmoticon(qnaOpen.getSubject()));
			qnaOpen.setQuestion(EmojiUtils.removeEmoticon(qnaOpen.getQuestion()));

			// 5. 금기어 확인
			String subjectBanWord = banWordService.getCheckedBanWord(qnaOpen.getSubject());
			String questionBanWord = banWordService.getCheckedBanWord(qnaOpen.getQuestion());

			if (!ObjectUtils.isEmpty(subjectBanWord)) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST, "제목에 금지어가 포함되어 있습니다.["+subjectBanWord+"]");
			}

			if (!ObjectUtils.isEmpty(questionBanWord)) {
				return ApiResponseEntity.error(ApiError.BAD_REQUEST, "내용에 금지어가 포함되어 있습니다.["+questionBanWord+"]");
			}

			// 6. QNA 수정
			String code = qnaService.updateQnaOpen(qnaOpen);

			// 7. 결과값
			if("SUCC".equals(code)) {
				result = ApiResponseEntity.data().put("status", HttpStatus.OK).ok();
			} else if("ERR_FILE_SIZE".equals(code)) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST, "업로드 가능한 최대 용량 : 5MB 입니다.");
			} else if("ERR_FILE_EXT".equals(code)) {
				result = ApiResponseEntity.error(ApiError.BAD_REQUEST, "유효하지 않은 파일입니다.");
			} else {
				result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
			}

		} catch (RuntimeException e) {
			log.error("[/api/qna/qna-open/update] ERROR : QNA 수정", e);
			result = ApiResponseEntity.error(ApiError.SYSTEM_ERROR);
		}

		return result;
	}
}
