package saleson.shop.qna;

import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.FileUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.web.domain.ListParam;
import com.onlinepowers.framework.web.pagination.Pagination;
import com.privacy.pCrypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import saleson.common.enumeration.SmsType;
import saleson.common.file.ExcelCellStyleUtils;
import saleson.common.file.infra.FileStorage;
import saleson.common.file.support.ThumbUtils;
import saleson.common.sms.SmsIpsService;
import saleson.common.sms.domain.ReceiverInfo;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.item.ItemService;
import saleson.shop.item.domain.ItemReview;
import saleson.shop.mailconfig.MailConfigService;
import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.domain.QnaAnswer;
import saleson.shop.qna.domain.QnaAnswerEncryptor;
import saleson.shop.qna.domain.QnaCriteriaEncryptor;
import saleson.shop.qna.domain.QnaEncryptor;
import saleson.shop.qna.domain.QnaOpen;
import saleson.shop.qna.domain.QnaOpenFile;
import saleson.shop.qna.support.QnaOpenParam;
import saleson.shop.qna.support.QnaParam;
import saleson.shop.sendmaillog.SendMailLogService;
import saleson.shop.user.UserService;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service("qnaService")
public class QnaServiceImple extends EgovAbstractServiceImpl implements QnaService{
	private static final Logger log = LoggerFactory.getLogger(QnaServiceImple.class);
	private final QnaMapper qnaMapper;
	private final SequenceService sequenceService;
	private final FileService fileService;
	private final FileStorage fileStorage;
	private final QnaCriteriaEncryptor qnaCriteriaEncryptor;
	private final QnaEncryptor qnaEncryptor;
	private final QnaAnswerEncryptor qnaAnswerEncryptor;
	private final SmsIpsService smsIpsService;

	@Override
	public void insertQna(Qna qna) {
		qna.setUserId(UserUtils.getUserId());
		qna.setQnaId(sequenceService.getId("OP_QNA"));

		// 1:1 문의 이미지가 존재한다면
		if(qna.getQnaImageFile() != null){
			if(qna.getQnaImageFile().getSize() > 0){
				MultipartFile multipartFile = qna.getQnaImageFile();

				String[] ITEM_DEFAULT_IMAGE_SAVE_SIZE = new String[] {"500x-1", "150x-1"};

				String fileExtension = FileUtils.getExtension(multipartFile.getOriginalFilename());
				String defaultFileName = qna.getQnaId() + "." + fileExtension;

				// 1. 업로드 경로설정
				String uploadPath = qna.getUploadPath();
				fileService.makeUploadPath(uploadPath);

				// 2. 파일명 중복파일 삭제
				fileStorage.delete(uploadPath, ShopUtils.unescapeHtml(defaultFileName));

				// 2-1. 새로운 파일명.
				defaultFileName = FileUtils.getNewFileName(uploadPath, defaultFileName);

				// 3. 저장될 파일
				File saveFile = new File(uploadPath + File.separator + defaultFileName);

				// 4. 섬네일 사이즈
				String[] thumbnailSize = StringUtils.delimitedListToStringArray(ITEM_DEFAULT_IMAGE_SAVE_SIZE[0], "x");

				// 생성.
				try {
					ThumbUtils.create(multipartFile, saveFile, Integer.parseInt(thumbnailSize[0]), Integer.parseInt(thumbnailSize[1]), fileStorage);
				} catch (IOException e) {
					log.error("ThumbUtils.create(... Exception : {}", e.getMessage(), e);
				}

				// 대표 이미지명
				qna.setQnaImage(defaultFileName);
			}
		}

		qna.encrypt(qnaEncryptor);
		qnaMapper.insertQna(qna);
	}

	@Override
	public void updateQna(Qna qna) {
		qna.encrypt(qnaEncryptor);
		qnaMapper.updateQna(qna);
	}

	@Override
	public void deleteQna(Qna qna) {
		QnaAnswer qnaAnswer = qnaMapper.getQnaAnswerByQnaId(qna.getQnaId());
		if (qnaAnswer == null || qnaAnswer.getQnaAnswerId() <= 0) {		// 답변 등록되지 않은거만 삭제하도록 수정
			qnaMapper.deleteQna(qna);
		}
//		qnaMapper.deleteQna(qna);
	}

	@Override
	public void insertQnaAnswer(QnaAnswer qnaAnswer) {
		qnaAnswer.setQnaAnswerId(sequenceService.getId("OP_QNA_ANSWER"));
		qnaMapper.insertQnaAnswer(qnaAnswer);
	}

	@Override
	public void updateQnaAnswer(QnaAnswer qnaAnswer) {
		qnaMapper.updateQnaAnswer(qnaAnswer);
	}

	@Override
	public void deleteQnaAnswer(QnaAnswer qnaAnswer) {
		qnaMapper.deleteQnaAnswer(qnaAnswer);

		// answercount updqte
		qnaMapper.updateQnaAnswerCount(qnaAnswer.getQnaId());
	}

	@Override
	public List<Qna> getQnaListByParam(QnaParam qnaParam) {
		qnaParam.encrypt(qnaCriteriaEncryptor);

		List<Qna> list = qnaMapper.getQnaListByParam(qnaParam);
		list.forEach(q -> q.decrypt(qnaEncryptor));

		qnaParam.decrypt(qnaCriteriaEncryptor);
		return list;
	}

	@Override
	public Qna getQnaByQnaId(int qnaId) {
		Qna qna = qnaMapper.getQnaByQnaId(qnaId);
		qna.decrypt(qnaEncryptor);
		return qna;
	}

	@Override
	public int getQnaListCountByParam(QnaParam qnaParam) {
		return qnaMapper.getQnaListCountByParam(qnaParam);
	}

	@Override
	public List<QnaAnswer> getQnaAnswerListByQnaId(int qnaId) {
		return qnaMapper.getQnaAnswerListByQnaId(qnaId);
	}

	@Override
	public QnaAnswer getQnaAnswerByQnaId(int qnaId) {
		QnaAnswer qnaAnswer = qnaMapper.getQnaAnswerByQnaId(qnaId);
		if(qnaAnswer != null) {
			qnaAnswer.decrypt(qnaAnswerEncryptor, ShopUtils.needMasking(UserUtils.getUser().getUserId()));
		}
		return qnaAnswer;
	}

	@Override
	public void setQnaListPagination(QnaParam qnaParam){

		// LIMIT 값이 존재하면 페이징 처리 안함
		if (qnaParam.getLimit() <= 0) {

			int count  = qnaMapper.getQnaListCountByParam(qnaParam);
			Pagination pagination = Pagination.getInstance(count, qnaParam.getItemsPerPage());

			ShopUtils.setPaginationInfo(pagination, qnaParam.getConditionType(), qnaParam.getPage());

			qnaParam.setPagination(pagination);

		}

	}

	@Override
	public void deleteQnaData(ListParam listparam) {

		if (listparam.getId() != null) {

			for (String qnaId : listparam.getId()) {
				QnaAnswer qnaAnswer = qnaMapper.getQnaAnswerByQnaId(Integer.valueOf(qnaId));
				if (qnaAnswer == null || qnaAnswer.getQnaAnswerId() <= 0) {		// 답변 등록되지 않은거만 삭제하도록 수정
					Qna qna = new Qna();
					qna.setQnaId(Integer.parseInt(qnaId));
					qnaMapper.deleteQna(qna);
				}
			}
		}
	}

	@Override
	public void updateQnaAnswerCount(int qnaId) {
		qnaMapper.updateQnaAnswerCount(qnaId);
	}


	@Override
	public void encryptQnaData(Qna qna) {
		// 암호화
		if (qna != null) {
			qna.encrypt(qnaEncryptor);
		}
	}

	/**
	 * Q&A 카운트
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public int getFrontQnaOpenListCount(QnaOpenParam qnaOpenParam) {
		return qnaMapper.getFrontQnaOpenListCount(qnaOpenParam);
	}

	/**
	 * Q&A 질문 카운트
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public int getFrontQnaOpenQuestionListCount(QnaOpenParam qnaOpenParam) {
		return qnaMapper.getFrontQnaOpenQuestionListCount(qnaOpenParam);
	}

	/**
	 * QNA 리스트
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public List<QnaOpen> getFrontQnaOpenList(QnaOpenParam qnaOpenParam) {
		return qnaMapper.getFrontQnaOpenList(qnaOpenParam);
	}

	/**
	 * Q&A 카운트(관리자)
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public int getFrontQnaOpenManagerListCount(QnaOpenParam qnaOpenParam) {
		return qnaMapper.getFrontQnaOpenManagerListCount(qnaOpenParam);
	}

	/**
	 * QNA 리스트(관리자)
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public List<QnaOpen> getFrontQnaOpenManagerList(QnaOpenParam qnaOpenParam) {
		return qnaMapper.getFrontQnaOpenManagerList(qnaOpenParam);
	}

	/**
	 * QNA 상세
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public QnaOpen getFrontQnaOpenDetail(QnaOpenParam qnaOpenParam) {
		return qnaMapper.getFrontQnaOpenDetail(qnaOpenParam);
	}

	/**
	 * QNA 조회수 증가
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public int addHitCount(QnaOpenParam qnaOpenParam) throws RuntimeException {
		int nResult = 0;

		// 조회수 증가
		String qnaAnswerId = String.valueOf(qnaOpenParam.getQnaAnswerId());
		if("null".equals(qnaAnswerId) || "0".equals(qnaAnswerId) || "".equals(qnaAnswerId)) {
			nResult = qnaMapper.addQnaHitCount(qnaOpenParam);
		} else {
			nResult = qnaMapper.addQnaAnswerHitCount(qnaOpenParam);
		}

		return nResult;
	}

	/**
	 * QNA 파일 목록 조회
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public List<QnaOpenFile> getFrontQnaOpenFileList(QnaOpenParam qnaOpenParam) {
		List<QnaOpenFile> fileList = null;

		String qnaAnswerId = String.valueOf(qnaOpenParam.getQnaAnswerId());
		if("null".equals(qnaAnswerId) || "0".equals(qnaAnswerId) || "".equals(qnaAnswerId)) {
			fileList = qnaMapper.getFrontQnaFileList(qnaOpenParam);
		} else {
			fileList = qnaMapper.getFrontQnaAnswerFileList(qnaOpenParam);
		}

		return fileList;
	}

	/**
	 * QNA 파일 상세 조회
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public QnaOpenFile getFrontQnaOpenFileDetail(QnaOpenParam qnaOpenParam) {
		QnaOpenFile fileDetail = null;

		if("Q".equals(qnaOpenParam.getQnaDetailType())) {
			fileDetail = qnaMapper.getFrontQnaFileDetail(qnaOpenParam);
		} else {
			fileDetail = qnaMapper.getFrontQnaAnswerFileDetail(qnaOpenParam);
		}

		return fileDetail;
	}

	/**
	 * QNA 등록
	 * @param qnaOpen
	 * @return
	 */
	@Override
	public String insertQnaOpen(QnaOpen qnaOpen) throws RuntimeException {
		// 0. 결과값
		String code = "FAIL";

		// 1. 등록 데이터
		qnaOpen.setQnaId(sequenceService.getId("OP_QNA"));
		qnaOpen.setUserName(UserUtils.getUser().getUserName());
		qnaOpen.setUserId(UserUtils.getUser().getUserId());
		qnaOpen.setEmail(UserUtils.getUser().getEmail());

		if (ObjectUtils.isEmpty(UserUtils.getUser().getEmail())) {
			qnaOpen.setEmail("");
		}

		// 2. 첨부파일 저장 (물리적)
		List<QnaOpenFile> qnaOpenFileList = new ArrayList<>();
		if(qnaOpen.getQnaOpenFileList() != null && qnaOpen.getQnaOpenFileList().size() > 0) {
			List<MultipartFile> uploadFileList = qnaOpen.getQnaOpenFileList();

			for(int i=0; i < uploadFileList.size(); i++) {
				MultipartFile multipartFile = uploadFileList.get(i);
				Integer qnaFileId = sequenceService.getId("OP_QNA_FILE");

				if(multipartFile != null && multipartFile.getSize() > 0) {
					String fileExtension = FileUtils.getExtension(multipartFile.getOriginalFilename());
					String defaultFileName = qnaFileId+"."+fileExtension;
					int maxSize = 5 * 1024 * 1024;

					// 2-1. 확장자 확인
					final String[] AVAILABLE_EXTENSION = {"jpg","jpeg","gif","png","hwp","doc","docx","xls","xlsx","ppt","pptx","pdf"};
					boolean extenstion_check = false;
					for (int j = 0; j < AVAILABLE_EXTENSION.length; j++) {
						if (fileExtension.equals(AVAILABLE_EXTENSION[j])) {
							extenstion_check = true;
						}
					}

					// 2-2. 확장자 및 사이즈 확인
					if (extenstion_check) {
						if(maxSize > multipartFile.getSize()) {

							// 2-3. 업로드 경로 설정
							String uploadPath = qnaOpen.getUploadPath();

							log.debug("qna fileUpload FilePath : {} ", uploadPath);

							fileService.makeUploadPath(uploadPath);

							// 2-4. 파일명 중복파일 삭제
							fileStorage.delete(uploadPath, ShopUtils.unescapeHtml(defaultFileName));

							// 2-5. 새로운 파일명
							defaultFileName = FileUtils.getNewFileName(uploadPath, defaultFileName);

							// 2-6. 저장될 파일
							File saveFile = new File(uploadPath + File.separator + defaultFileName);

							// 2-7. 파일저장 (물리적)
							try {
								fileStorage.upload(multipartFile.getBytes(), saveFile);
							} catch (IOException e) {
								log.error("ERROR: {}", getClass().getName() + " :: insertQnaOpen Exception ===========");
							}

							// 2-8. 파일저장 정보 셋팅 (논리적)
							QnaOpenFile qnaOpenFile = new QnaOpenFile();
							qnaOpenFile.setQnaFileId(String.valueOf(qnaFileId));
							qnaOpenFile.setQnaId(qnaOpen.getQnaId());
							qnaOpenFile.setFileName(defaultFileName);
							qnaOpenFile.setFileTy(fileExtension);
							qnaOpenFile.setOrgFileName(multipartFile.getOriginalFilename());
							qnaOpenFileList.add(qnaOpenFile);

						} else {
							code = "ERR_FILE_SIZE";
						}
					} else {
						code = "ERR_FILE_EXT";
					}
				}
			}
		}

		// 3. QNA 등록
		int nResult = qnaMapper.insertQna(this.setQnaData(qnaOpen, "I"));

		// 4. QNA 파일 등록 및 결과값 셋팅
		if(nResult> 0) {
			if(qnaOpenFileList != null && qnaOpenFileList.size() > 0) {
				for(QnaOpenFile file : qnaOpenFileList) {
					qnaMapper.insertQnaFile(file);
				};
			}

			code = "SUCC";
		};

		return code;
	}

	/**
	 * QNA 등록/수정 데이터 셋팅
	 * @param qnaOpen
	 * @return
	 */
	private Qna setQnaData(QnaOpen qnaOpen, String type) {
		Qna qna = new Qna();
		qna.setQnaId(qnaOpen.getQnaId());
		qna.setQnaGroup(qnaOpen.getQnaGroup());
		qna.setSubject(qnaOpen.getSubject());
		qna.setQuestion(qnaOpen.getQuestion());
		qna.setUserName(qnaOpen.getUserName());
		qna.setEmail(qnaOpen.getEmail());
		qna.setSecretFlag(qnaOpen.getSecretFlag());

		if("I".equals(type)) {
			qna.setQnaType(qnaOpen.getQnaType());
			qna.setUserId(qnaOpen.getUserId());
			qna.setAnswerCount(0);
			//qna.setSecretFlag("N");
		}

		qna.encrypt(qnaEncryptor);
		return qna;
	}

	/**
	 * QNA 삭제
	 * @param qnaOpenParam
	 * @return
	 */
	@Override
	public String deleteQnaOpen(QnaOpenParam qnaOpenParam) throws RuntimeException {
		String code = "FAIL";

		// 1. 파일 확인
		List<QnaOpenFile> fileList = qnaMapper.getFrontQnaFileList(qnaOpenParam);

		// 2. QNA 파일 삭제
		if(fileList != null && fileList.size() > 0) {
			for(QnaOpenFile file : fileList) {
				qnaMapper.deleteQnaFile(file);
			}
		}

		// 3. QNA 삭제
		Qna qna = new Qna();
		qna.setQnaId(qnaOpenParam.getQnaId());
		if(qnaMapper.deleteFrontQna(qna) > 0) {
			code = "SUCC";
		}

		// 4. 물리적 파일 삭제
		try {
			if(fileList != null && fileList.size() > 0) {
				for(QnaOpenFile file : fileList) {
					fileStorage.delete(file.getUploadPath(), ShopUtils.unescapeHtml(file.getFileName()));
				}
			}
		} catch (RuntimeException e) {
			log.error("ERROR: {}", getClass().getName() + " :: deleteQnaOpen Exception ===========");
		}

		return code;
	}

	/**
	 * QNA 수정
	 * @param qnaOpen
	 * @return
	 */
	@Override
	public String updateQnaOpen(QnaOpen qnaOpen) throws RuntimeException {
		// 0. 결과값
		String code = "FAIL";

		// 1. 수정 데이터
		qnaOpen.setUserName(UserUtils.getUser().getUserName());
		qnaOpen.setEmail(UserUtils.getUser().getEmail());

		if (ObjectUtils.isEmpty(UserUtils.getUser().getEmail())) {
			qnaOpen.setEmail("");
		}

		// 2. 첨부파일 저장 (물리적)
		List<QnaOpenFile> qnaOpenFileList = new ArrayList<>();
		if(qnaOpen.getQnaOpenFileList() != null && qnaOpen.getQnaOpenFileList().size() > 0) {
			List<MultipartFile> uploadFileList = qnaOpen.getQnaOpenFileList();

			for(int i=0; i < uploadFileList.size(); i++) {
				MultipartFile multipartFile = uploadFileList.get(i);
				Integer qnaFileId = sequenceService.getId("OP_QNA_FILE");

				if(multipartFile != null && multipartFile.getSize() > 0) {
					String fileExtension = FileUtils.getExtension(multipartFile.getOriginalFilename());
					String defaultFileName = qnaFileId+"."+fileExtension;
					int maxSize = 5 * 1024 * 1024;

					// 2-1. 확장자 확인
					final String[] AVAILABLE_EXTENSION = {"jpg","jpeg","gif","png","hwp","doc","docx","xls","xlsx","ppt","pptx","pdf"};
					boolean extenstion_check = false;
					for (int j = 0; j < AVAILABLE_EXTENSION.length; j++) {
						if (fileExtension.equals(AVAILABLE_EXTENSION[j])) {
							extenstion_check = true;
						}
					}

					// 2-2. 확장자 및 사이즈 확인
					if (extenstion_check) {
						if(maxSize > multipartFile.getSize()) {

							// 2-3. 업로드 경로 설정
							String uploadPath = qnaOpen.getUploadPath();
							fileService.makeUploadPath(uploadPath);

							// 2-4. 파일명 중복파일 삭제
							fileStorage.delete(uploadPath, ShopUtils.unescapeHtml(defaultFileName));

							// 2-5. 새로운 파일명
							defaultFileName = FileUtils.getNewFileName(uploadPath, defaultFileName);

							// 2-6. 저장될 파일
							File saveFile = new File(uploadPath + File.separator + defaultFileName);

							// 2-7. 파일저장 (물리적)
							try {
								fileStorage.upload(multipartFile.getBytes(), saveFile);
							} catch (IOException e) {
								log.error("ERROR: {}", getClass().getName() + " :: updateQnaOpen Exception ===========");
							}

							// 2-8. 파일저장 정보 셋팅 (논리적)
							QnaOpenFile qnaOpenFile = new QnaOpenFile();
							qnaOpenFile.setQnaFileId(String.valueOf(qnaFileId));
							qnaOpenFile.setQnaId(qnaOpen.getQnaId());
							qnaOpenFile.setFileName(defaultFileName);
							qnaOpenFile.setFileTy(fileExtension);
							qnaOpenFile.setOrgFileName(multipartFile.getOriginalFilename());
							qnaOpenFileList.add(qnaOpenFile);

						} else {
							code = "ERR_FILE_SIZE";
						}
					} else {
						code = "ERR_FILE_EXT";
					}
				}
			}
		}

		// 3. QNA 수정
		int nResult = qnaMapper.updateFrontQnaOpen(this.setQnaData(qnaOpen, "U"));

		// 4. QNA 파일 등록&삭제 및 결과값 셋팅
		if(nResult> 0) {

			// 4-1. 파일 등록
			if(qnaOpenFileList != null && qnaOpenFileList.size() > 0) {
				for(QnaOpenFile file : qnaOpenFileList) {
					qnaMapper.insertQnaFile(file);
				};
			}

			// 4-2. 파일 삭제
			if(qnaOpen.getDelQnaOpenFileList() != null && qnaOpen.getDelQnaOpenFileList().size() > 0) {
				QnaOpenParam qnaOpenParam = new QnaOpenParam();
				for(Integer qnaFileId : qnaOpen.getDelQnaOpenFileList()) {
					qnaOpenParam.setQnaFileId(String.valueOf(qnaFileId));
					qnaMapper.deleteQnaFile(qnaMapper.getFrontQnaFileDetail(qnaOpenParam));
				}
			}

			// 4-3. 파일 순번 재정렬
			Qna qna = new Qna();
			qna.setQnaId(qnaOpen.getQnaId());
			qnaMapper.updateQnaFileOrdering(qna);

			// 4-4. 파일 물리적 삭제
			try {
				if(qnaOpen.getDelQnaOpenFileList() != null && qnaOpen.getDelQnaOpenFileList().size() > 0) {
					QnaOpenParam qnaOpenParam = new QnaOpenParam();
					for(Integer qnaFileId : qnaOpen.getDelQnaOpenFileList()) {
						qnaOpenParam.setQnaFileId(String.valueOf(qnaFileId));
						QnaOpenFile file = qnaMapper.getFrontQnaFileDetail(qnaOpenParam);

						if(file != null) {
							fileStorage.delete(file.getUploadPath(), ShopUtils.unescapeHtml(file.getFileName()));
						}
					}
				}
			} catch (RuntimeException e) {
				log.error("ERROR: {}", getClass().getName() + " :: updateQnaOpen Exception ===========");
			}

			// 4-4. 결과값
			code = "SUCC";
		};

		return code;
	}

	@Override
    public void deleteItemImageByItemId(int fileId) {
		QnaOpenParam qnaOpenParam = new QnaOpenParam();
		qnaOpenParam.setQnaFileId(String.valueOf(fileId));
		//QnaOpenFile file = qnaMapper.getFrontQnaAnswerFileDetail(qnaOpenParam);
		QnaOpenFile file = qnaMapper.getFrontQnaFileDetail(qnaOpenParam);

		QnaAnswer item = new QnaAnswer();
        String listImage = item.getUploadPath() + "/" + ShopUtils.unescapeHtml(file.getFileName());
        log.debug(">>> listImage : {}", listImage);

        fileStorage.delete(listImage);

        // 데이터 삭제
        //qnaMapper.deleteQnaAnswerFile(file);
        qnaMapper.deleteQnaFile(file);
    }

	@Override
    public void deleteItemImageByItemId(String fileId) {
		try {
			fileId = URLDecoder.decode(fileId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error(">>> deleteItemImageByItemId 인코딩 에러 ", e.toString());
		}
		QnaOpenParam qnaOpenParam = new QnaOpenParam();
		qnaOpenParam.setQnaFileId(String.valueOf(fileId));
		//QnaOpenFile file = qnaMapper.getFrontQnaAnswerFileDetail(qnaOpenParam);
		QnaOpenFile file = qnaMapper.getFrontQnaFileDetail(qnaOpenParam);

		QnaAnswer item = new QnaAnswer();
        String listImage = item.getUploadPath() + "/" + ShopUtils.unescapeHtml(file.getFileName());
        log.debug(">>> listImage : {}", listImage);

        fileStorage.delete(listImage);

        // 데이터 삭제
        //qnaMapper.deleteQnaAnswerFile(file);
        qnaMapper.deleteQnaFile(file);
    }

	@Override
	public void sendSmsQnaAnswer(int qnaId) {
		// TODO Auto-generated method stub
		GiveUserSmsInfo info = qnaMapper.getQnaUserInfo(qnaId);
		smsIpsService.giveSendSms(Arrays.asList(info), SmsType.QNA);

	}

	@Override
	public int getFiveMinuteCheck(QnaOpen qnaOpen) {
		// TODO Auto-generated method stub
		qnaOpen.setUserId(UserUtils.getUserId());
		return qnaMapper.getFiveMinuteCheck(qnaOpen);
	}

	/**
	 * 스트리밍 엑셀다운로드
	 *
	 * @param	qnaParam
	 * @throws	Exception
	 */
	@Override
	public SXSSFWorkbook streamQnaItemData(QnaParam qnaParam) throws Exception {
		// Cursor<DTO>가 스트리밍 방식으로 사용하기에는 적절하나, CUBRID DB에서는 사용이 불가
		// JDBC API 기반의 데이터 접근 방식 : Cursor
		int pageSize	= 0;
		int offset 		= 0;
		int rowNum 		= 0;
		int limit		= 0;

		// 전체 데이터 count
		qnaParam.getPagination().setItemsPerPage(pageSize);
		qnaParam.getPagination().setCurrentPage(offset);

		int totalCount = qnaMapper.getQnaListCountByParam(qnaParam);

		// totalCount에 따른 반복 횟수 설정(1000(row)으로 나눈 몫(올림))
		limit = (int) Math.ceil((double) totalCount / 1000);

		// Cursor 를 이용한 스트리밍 방식의 엑셀 다운로드 불가
		// pageSize 와 offset 을 설정하여, 반복문 실행(1회 반복 : 1000 row)
		pageSize 		= 1000;
		offset 			= 1;

		qnaParam.getPagination().setItemsPerPage(pageSize);
		qnaParam.getPagination().setCurrentPage(offset);

		// SXSSF	: window size = 100
		// workbook	: 엑셀생성을 위한 내부 문서 모델
		// 메모리 적재 최대 100 row 로 설정, 나머지는 disk 로 flush
		// disk 저장 파일은 압축
		SXSSFWorkbook workbook = new SXSSFWorkbook(100);
		workbook.setCompressTempFiles(true);

		// 엑셀 시트 이름 설정
		Sheet sheet = workbook.createSheet("REVIEW_DATA");

		// 엑셀 스타일 설정 초기화
		ExcelCellStyleUtils cellStyle = new ExcelCellStyleUtils(workbook);

		// 타이틀 설정
		Row titleRow = sheet.createRow(rowNum++);
		titleRow.setHeight((short) 800);
		cellStyle.title(titleRow, 0, "답레품 QnA 목록");
		Integer lastColIndex;

		// 셀 폭 설정(고정값)
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 15000);
		sheet.setColumnWidth(4, 2000);
		sheet.setColumnWidth(5, 10000);
		sheet.setColumnWidth(6, 3000);
		sheet.setColumnWidth(7, 4000);
		sheet.setColumnWidth(8, 5000);
		sheet.setColumnWidth(9, 5000);

		// 데이터 Header 값 설정
		Row header = sheet.createRow(rowNum++);
		header.setHeight((short) 512);
		cellStyle.header(header, 0, "No");
		cellStyle.header(header, 1, "지자체");
		cellStyle.header(header, 2, "문의유형");
		cellStyle.header(header, 3, "제목");
		cellStyle.header(header, 4, "작성자");
		cellStyle.header(header, 5, "상호명");
		cellStyle.header(header, 6, "답변상태");
		cellStyle.header(header, 7, "비밀글여부");
		cellStyle.header(header, 8, "작성일");
		cellStyle.header(header, 9, "답변일");

		// title row cell merging
		lastColIndex = header.getLastCellNum() - 1;
		sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), 0, lastColIndex));

		// 최대 1,000,000 row 까지만 입력
		boolean exceedTotalRow = false;

		// 1000 개 row 조회 및 엑셀 시트 내용 구성(반복)
		while (!exceedTotalRow) {
			if (totalCount == 0) break;

			// 엑셀 시트 구성 데이터 조회(1000건) 및 전처리
			qnaParam.encrypt(qnaCriteriaEncryptor);

			List<Qna> qnaList = qnaMapper.getQnaListByParam(qnaParam);
			qnaList.forEach(q -> q.decrypt(qnaEncryptor, false));

			qnaParam.decrypt(qnaCriteriaEncryptor);

			List<Code> qnaGroups = CodeUtils.getCodeList("QNA_GROUPS");

			for (Qna qnaCheck : qnaList) {
				for (Code code : qnaGroups) {
					if (qnaCheck.getQnaGroup().equals(code.getId())) {
						qnaCheck.setQnaGroup(code.getLabel());
					}
				}
			}

			if (qnaList.isEmpty()) break;

			// 1000건 데이터를 엑셀 시트에 입력 가능하도록 row 단위의 전처리 실행
			for (Qna qna : qnaList) {
				// workbook의 row 생성 및 각 건수별 데이터 입력/저장
				// 생성된 row가 앞서 설정한 최댓값 100 row 가 넘어가게 되면, 가장 먼저 생성된 row 는 디스크에 flush
				// flush는 row가 새로 생성되는 시점에서 메모리 적재  row 수 를 확인하고 설정 값 이상이 되는 경우 실행
				Row row = sheet.createRow(rowNum++);
				row.setHeight((short) 400);
				cellStyle.data(row, 0, StringUtils.numberFormat(totalCount--));
				cellStyle.data(row, 1, qna.getLocgovName());
				cellStyle.data(row, 2, qna.getQnaGroup());
				cellStyle.data(row, 3, qna.getItemUserCode() + "_" + qna.getSubject());
				cellStyle.data(row, 4, qna.getUserName());
				cellStyle.data(row, 5, qna.getSellerCompanyName());
				cellStyle.data(row, 6, qna.getQnaAnswer().getQnaAnswerId() > 0 ? "Y" : "N");
				cellStyle.data(row, 7, qna.getSecretFlag());
				cellStyle.data(row, 8, qna.getCreatedDate());
				cellStyle.data(row, 9, qna.getQnaAnswer().getQnaAnswerId() > 0 ? qna.getQnaAnswer().getAnswerDate() : "");
			}

			// 다음 1000 row 조회를 위한 offset 설정
			// 쿼리문
			// LIMIT (#{pagination.currentPage} - 1) * #{pagination.itemsPerPage}, #{pagination.itemsPerPage}
			qnaParam.getPagination().setCurrentPage(++offset);

			// 최대 백만 row 까지만(이상은 엑셀 파일에 작성 불가) 또는 전체 갯수(1000단위)까지만 데이터 조회 및 저장
			if (offset > 1000 || offset > limit) {
				exceedTotalRow = true;
			}
		}

		return workbook;
	}

	@Override
	public Qna getQnaByQnaAnswerId(int qnaAnswerId) {
		Qna qna = qnaMapper.getQnaByQnaAnswerId(qnaAnswerId);
		qna.decrypt(qnaEncryptor);
		return qna;
	}

	@Override
	public QnaAnswer getQnaAnswerByQnaAnswerId(Qna qnaParam) {
		QnaAnswer qnaAnswer = qnaMapper.getQnaAnswerByQnaAnswerId(qnaParam);
		if(qnaAnswer != null) {
			qnaAnswer.decrypt(qnaAnswerEncryptor, ShopUtils.needMasking(UserUtils.getUser().getUserId()));
		}
		return qnaAnswer;
	}


}