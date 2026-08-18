package saleson.shop.qnaadmin;

import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.file.service.FileService;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.sequence.service.SequenceService;
import com.onlinepowers.framework.web.pagination.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import saleson.common.configuration.SalesonProperty;
import saleson.common.file.FileDownloadCustom;
import saleson.common.file.infra.FileStorage;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.qnaadmin.domain.QnaAdmin;
import saleson.shop.qnaadmin.domain.QnaAdminAnswer;
import saleson.shop.qnaadmin.domain.QnaAdminAnswerFile;
import saleson.shop.qnaadmin.domain.QnaAdminCriteriaEncryptor;
import saleson.shop.qnaadmin.domain.QnaAdminEncryptor;
import saleson.shop.qnaadmin.domain.QnaAdminFile;
import saleson.shop.qnaadmin.support.QnaAdminListParam;
import saleson.shop.qnaadmin.support.QnaAdminParam;
import saleson.shop.user.UserService;
import saleson.shop.user.domain.UserEncryptor;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("qnaAdminService")
public class QnaAdminServiceImpl extends EgovAbstractServiceImpl implements QnaAdminService{
	
	private final QnaAdminMapper qnaAdminMapper;
	private final SequenceService sequenceService;
	private final FileService fileService;
	private final FileStorage fileStorage;
	private final QnaAdminCriteriaEncryptor qnaAdminCriteriaEncryptor;
	private final QnaAdminEncryptor qnaAdminEncryptor;
	
	@Autowired
	private UserService userService;
	
    @Autowired
    private UserEncryptor userEncryptor;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertQnaAdmin(QnaAdmin qnaAdmin) {
		LocalDateTime now = LocalDateTime.now();
		User user = UserUtils.getUser();
		qnaAdmin.setUserId(user.getUserId());
		qnaAdmin.setUserName(user.getUserName());
		qnaAdmin.setEmail(user.getEmail());
		qnaAdmin.setQnaAdminId(sequenceService.getId("G_QNA_ADMIN"));
		qnaAdmin.setSellerId(SellerUtils.getSellerId());
		
		qnaAdmin.setCreatedDate(Timestamp.valueOf(now));
		
		if (StringUtils.isEmpty(qnaAdmin.getLocgovCode())) {
			throw new OpRuntimeException("권한이 없습니다.");
		}
		
		// 첨부파일 존재시
		if (qnaAdmin.getAddFiles() != null && !qnaAdmin.getAddFiles().isEmpty()) {
			try {
				for (MultipartFile multipartFile : qnaAdmin.getAddFiles()) {
					if (!multipartFile.isEmpty() && !multipartFile.getOriginalFilename().isEmpty()) {
						QnaAdminFile qnaAdminFile = new QnaAdminFile();
						qnaAdminFile.setQnaAdminId(qnaAdmin.getQnaAdminId());
						qnaAdminFile.setFileService(fileService);
						qnaAdminFile.setFileStorage(fileStorage);
						qnaAdminFile.setQnaAdminFileId(sequenceService.getId("G_QNA_ADMIN_FILE"));
						qnaAdminFile.setMultipartFile(multipartFile);
						qnaAdminFile.setCreatedDate(Timestamp.valueOf(now));
						qnaAdminMapper.insertQnaAdminFile(qnaAdminFile);
					}
				}
			} catch (OpRuntimeException e) {
				log.error("======== insertQnaAdminFile error ========== :: ");
				throw new OpRuntimeException("첨부파일 저장 오류입니다.");	// 첨부파일 저장 오류입니다.
			}
		}
		try {
			qnaAdmin.encrypt(qnaAdminEncryptor);
			qnaAdminMapper.insertQnaAdmin(qnaAdmin);
		} catch (OpRuntimeException e) {
			log.error("======== insertQnaAdmin error ========== :: ");
			throw new OpRuntimeException("첨부파일 저장 오류입니다.");		// 지자체 문의 저장 오류입니다.
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateQnaAdmin(QnaAdmin qnaAdmin) {
		User user = UserUtils.getUser();
		// 사용자 일치 검증
		if (!isQnaAdminWriter(qnaAdmin.getQnaAdminId(), qnaAdmin.getLocgovCode())) {
//			return;
			throw new OpRuntimeException("권한이 없습니다.");		// 권한이 없습니다.
		}
		LocalDateTime nowDt = LocalDateTime.now();
		Timestamp now = Timestamp.valueOf(nowDt);

		QnaAdminAnswer qnaAdminAnswer = qnaAdminMapper.getQnaAdminAnswerByQnaAdminId(qnaAdmin.getQnaAdminId());
		if (qnaAdminAnswer != null && qnaAdminAnswer.getQnaAdminAnswerId() > 0) {
			throw new OpRuntimeException("지자체 문의 저장 오류입니다.");		// 지자체 문의 저장 오류입니다.
		}
		
		qnaAdmin.setUserId(user.getUserId());
		qnaAdmin.setUserName(user.getUserName());
		qnaAdmin.setEmail(user.getEmail());
		
		// 첨부파일 존재시
		if (qnaAdmin.getAddFiles() != null && !qnaAdmin.getAddFiles().isEmpty()) {
			try {
				for (MultipartFile multipartFile : qnaAdmin.getAddFiles()) {
					if (!multipartFile.isEmpty() && !multipartFile.getOriginalFilename().isEmpty()) {
						QnaAdminFile qnaAdminFile = new QnaAdminFile();
						qnaAdminFile.setQnaAdminId(qnaAdmin.getQnaAdminId());
						qnaAdminFile.setFileService(fileService);
						qnaAdminFile.setFileStorage(fileStorage);
						qnaAdminFile.setQnaAdminFileId(sequenceService.getId("G_QNA_ADMIN_FILE"));
						qnaAdminFile.setMultipartFile(multipartFile);
						qnaAdminFile.setCreatedDate(now);
						
						qnaAdminMapper.insertQnaAdminFile(qnaAdminFile);
					}
				}
			} catch (OpRuntimeException e) {
				log.error("======== updateQnaAdmin error ========== :: ");
				throw new OpRuntimeException("첨부파일 저장 오류입니다.");		// 첨부파일 저장 오류입니다.
			}
		}
		try {
			qnaAdmin.encrypt(qnaAdminEncryptor);
			qnaAdminMapper.updateQnaAdmin(qnaAdmin);
		} catch (OpRuntimeException e) {
			log.error("======== updateQnaAdmin error ========== :: ");
			throw new OpRuntimeException("지자체 문의 저장 오류입니다.");		// 지자체 문의 수정 오류입니다.
		}
	}

	@Override
	public void deleteQnaAdmin(QnaAdmin qnaAdmin) {
		// 사용자 일치 검증
		if (!isQnaAdminWriter(qnaAdmin.getQnaAdminId(), qnaAdmin.getLocgovCode())) {
			throw new OpRuntimeException("권한이 없습니다.");		// 권한이 없습니다.
		}
		
		QnaAdminAnswer qnaAdminAnswer = qnaAdminMapper.getQnaAdminAnswerByQnaAdminId(qnaAdmin.getQnaAdminId());
		if (qnaAdminAnswer != null && qnaAdminAnswer.getQnaAdminAnswerId() > 0) {
			throw new OpRuntimeException("답변이 존재하여 삭제가 불가능합니다.");		// 답변이 존재하여 삭제가 불가능합니다.
		}
		
		List<QnaAdminFile> fileList = qnaAdminMapper.getQnaAdminFileList(qnaAdmin);
		if (fileList != null && !fileList.isEmpty()) {
			for (QnaAdminFile qnaAdminFile : fileList) {
				deleteQnaAdminFile(qnaAdmin.getQnaAdminId(), qnaAdminFile.getQnaAdminFileId(), true, qnaAdmin.getLocgovCode());
			}
		}
		qnaAdminMapper.deleteQnaAdmin(qnaAdmin);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer, String locgovCode)  {
		LocalDateTime nowDt = LocalDateTime.now();
		Timestamp now = Timestamp.valueOf(nowDt);
		
		qnaAdminAnswer.setUserId(UserUtils.getUser().getUserId());
		qnaAdminAnswer.setQnaAdminAnswerId(sequenceService.getId("G_QNA_ADMIN_ANSWER"));
		qnaAdminAnswer.setAnswerDate(now);
		
		QnaAdmin qnaAdmin = qnaAdminMapper.getQnaAdminByQnaAdminId(qnaAdminAnswer.getQnaAdminId());
		if (qnaAdmin == null || qnaAdmin.getQnaAdminId() <= 0) {
			throw new OpRuntimeException("답변 등록할 문의가 없습니다.");		// 답변 등록할 문의가 없습니다.
		}

		if (StringUtils.isEmpty(locgovCode) || !qnaAdmin.getLocgovCode().equals(locgovCode)) {
			throw new OpRuntimeException("권한이 없습니다.");
		}
		
		// 첨부파일 존재시
		if (qnaAdminAnswer.getAddFiles() != null && !qnaAdminAnswer.getAddFiles().isEmpty()) {
			try {
				for (MultipartFile multipartFile : qnaAdminAnswer.getAddFiles()) {
					if (!multipartFile.isEmpty() && !multipartFile.getOriginalFilename().isEmpty()) {
						QnaAdminAnswerFile qnaAdminAnswerFile = new QnaAdminAnswerFile();
						qnaAdminAnswerFile.setQnaAdminId(qnaAdminAnswer.getQnaAdminId());		// 파일 저장 경로 용도
						qnaAdminAnswerFile.setQnaAdminAnswerId(qnaAdminAnswer.getQnaAdminAnswerId());
						qnaAdminAnswerFile.setFileService(fileService);
						qnaAdminAnswerFile.setFileStorage(fileStorage);
						qnaAdminAnswerFile.setQnaAdminAnswerFileId(sequenceService.getId("G_QNA_ADMIN_ANSWER_FILE"));
						qnaAdminAnswerFile.setMultipartFile(multipartFile);
						qnaAdminAnswerFile.setCreatedDate(now);
						
						qnaAdminMapper.insertQnaAdminAnswerFile(qnaAdminAnswerFile);
					}
				}
			} catch (OpRuntimeException e) {
				log.error("======== updateQnaAdminAnswer error ========== :: ");
				throw new OpRuntimeException("첨부파일 저장 오류입니다.");	// 첨부파일 저장 오류입니다.
			}
		}
		try {
			qnaAdminMapper.insertQnaAdminAnswer(qnaAdminAnswer);
		} catch (OpRuntimeException e) {
			log.error("======== updateQnaAdminAnswer error ========== :: ");
			throw new OpRuntimeException("관리자 문의 저장 오류입니다.");		// 관리자 문의 저장 오류입니다.
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer) {
		// 사용자 일치 검증
		if (!isQnaAdminAnswerWriter(qnaAdminAnswer.getQnaAdminAnswerId())) {
//			return;
			throw new OpRuntimeException("권한이 없습니다.");		// 권한이 없습니다.
		}
		LocalDateTime nowDt = LocalDateTime.now();
		Timestamp now = Timestamp.valueOf(nowDt);
		qnaAdminAnswer.setUserId(UserUtils.getUser().getUserId());
		
		// 첨부파일 존재시
		if (qnaAdminAnswer.getAddFiles() != null && !qnaAdminAnswer.getAddFiles().isEmpty()) {
			try {
				for (MultipartFile multipartFile : qnaAdminAnswer.getAddFiles()) {
					if (!multipartFile.isEmpty() && !multipartFile.getOriginalFilename().isEmpty()) {
						QnaAdminAnswerFile qnaAdminAnswerFile = new QnaAdminAnswerFile();
						qnaAdminAnswerFile.setQnaAdminId(qnaAdminAnswer.getQnaAdminId());		// 파일 저장 경로 용도
						qnaAdminAnswerFile.setQnaAdminAnswerId(qnaAdminAnswer.getQnaAdminAnswerId());
						qnaAdminAnswerFile.setFileService(fileService);
						qnaAdminAnswerFile.setFileStorage(fileStorage);
						qnaAdminAnswerFile.setQnaAdminAnswerFileId(sequenceService.getId("G_QNA_ADMIN_ANSWER_FILE"));
						qnaAdminAnswerFile.setMultipartFile(multipartFile);
						qnaAdminAnswerFile.setCreatedDate(now);
						
						qnaAdminMapper.insertQnaAdminAnswerFile(qnaAdminAnswerFile);
					}
				}
			} catch (OpRuntimeException e) {
				log.error("======== updateQnaAdminAnswer error ========== :: ");
				throw new OpRuntimeException("첨부파일 저장 오류입니다.");	// 첨부파일 저장 오류입니다.
			}
		}
		try {
			qnaAdminMapper.updateQnaAdminAnswer(qnaAdminAnswer);
		} catch (OpRuntimeException e) {
			log.error("======== updateQnaAdminAnswer error ========== :: ");
			throw new OpRuntimeException("관리자 문의 수정 오류입니다.");		// 관리자 문의 수정 오류입니다.
		}
	}

	@Override
	public void deleteQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer) {
		// 사용자 일치 검증
		if (!isQnaAdminAnswerWriter(qnaAdminAnswer.getQnaAdminAnswerId())) {
//			return;
			throw new OpRuntimeException("권한이 없습니다.");		// 권한이 없습니다.
		}
		List<QnaAdminAnswerFile> fileList = qnaAdminMapper.getQnaAdminAnswerFileList(qnaAdminAnswer);
		if (fileList != null && !fileList.isEmpty()) {
			for (QnaAdminAnswerFile qnaAdminAnswerFile : fileList) {
				deleteQnaAdminAnswerFile(qnaAdminAnswer.getQnaAdminId()
						, qnaAdminAnswer.getQnaAdminAnswerId(), qnaAdminAnswerFile.getQnaAdminAnswerFileId(), true);
			}
		}
		qnaAdminMapper.deleteQnaAdminAnswer(qnaAdminAnswer);
	}

	@Override
	public List<QnaAdmin> getQnaAdminListByParam(QnaAdminParam qnaAdminParam) {
		qnaAdminParam.encrypt(qnaAdminCriteriaEncryptor);
		
		List<QnaAdmin> list = qnaAdminMapper.getQnaAdminListByParam(qnaAdminParam);
		list.forEach(q -> q.decrypt(qnaAdminEncryptor));

		qnaAdminParam.decrypt(qnaAdminCriteriaEncryptor);
		return list;
	}

	@Override
	public QnaAdmin getQnaAdminByQnaAdminId(long qnaAdminId) {
		QnaAdmin qnaAdmin = qnaAdminMapper.getQnaAdminByQnaAdminId(qnaAdminId);
		qnaAdmin.decrypt(qnaAdminEncryptor);
		return qnaAdmin;
	}
	
	@Override
	public int getQnaAdminListCountByParam(QnaAdminParam qnaAdminParam) {
		return qnaAdminMapper.getQnaAdminListCountByParam(qnaAdminParam);
	}

	@Override
	public List<QnaAdminAnswer> getQnaAdminAnswerListByQnaAdminId(long qnaAdminId) {
		return qnaAdminMapper.getQnaAdminAnswerListByQnaAdminId(qnaAdminId);
	}
	
	@Override
	public QnaAdminAnswer getQnaAdminAnswerByQnaAdminId(long qnaAdminId) {
		QnaAdminAnswer result = qnaAdminMapper.getQnaAdminAnswerByQnaAdminId(qnaAdminId);
		
		if (result != null) {
			User writer = userService.getUserByUserId(result.getUserId());
			writer.decrypt(userEncryptor, false);
			result.setUserNm(writer.getUserName());
		}
		
		return result;
	}
	
	@Override
	public void setQnaAdminListPagination(QnaAdminParam qnaAdminParam){
		
		// LIMIT 값이 존재하면 페이징 처리 안함
//		if (qnaAdminParam.getLimit() <= 0) {
		
			int count  = qnaAdminMapper.getQnaAdminListCountByParam(qnaAdminParam);
			Pagination pagination = Pagination.getInstance(count, qnaAdminParam.getItemsPerPage());
			
			ShopUtils.setPaginationInfo(pagination, qnaAdminParam.getConditionType(), qnaAdminParam.getPage());
			
			qnaAdminParam.setPagination(pagination);
			
//		}
		
	}	
	
//	@Override
//	public void deleteQnaAdminData(ListParam listparam, HttpServletRequest request) {
//		if (listparam.getId() != null) {
//			for (String qnaAdminId : listparam.getId()) {
//				QnaAdmin qnaAdmin = qnaAdminMapper.getQnaAdminByQnaAdminId(Long.valueOf(qnaAdminId));
//				deleteQnaAdmin(qnaAdmin, request);
//			}	
//		}
//	}
	
	@Override
	public void updateQnaAdminAnswerCount(long qnaAdminId) {
		qnaAdminMapper.updateQnaAdminAnswerCount(qnaAdminId);
	}


	@Override
	public void encryptQnaAdminData(QnaAdmin qnaAdmin) {
		// 암호화
		if (qnaAdmin != null) {
			qnaAdmin.encrypt(qnaAdminEncryptor);
		}
	}

	@Override
	public List<QnaAdminFile> getQnaAdminFileList(QnaAdmin qnaAdmin) {
		if (qnaAdmin == null) {
			return new ArrayList<>();
		}
		List<QnaAdminFile> searchResult = qnaAdminMapper.getQnaAdminFileList(qnaAdmin);
		if (searchResult == null) {
			searchResult = new ArrayList<>();
		}
		return searchResult;
	}

	@Override
	public List<QnaAdminAnswerFile> getQnaAdminAnswerFileList(QnaAdminAnswer qnaAdminAnswer) {
		if (qnaAdminAnswer == null) {
			return new ArrayList<>();
		}
		List<QnaAdminAnswerFile> searchResult = qnaAdminMapper.getQnaAdminAnswerFileList(qnaAdminAnswer);
		if (searchResult == null) {
			searchResult = new ArrayList<>();
		}
		return searchResult;
	}

	@Override
	public ResponseEntity<byte[]> downloadQnaAdminFile(long qnaAdminId, long qnaAdminFileId) {
		try {
			QnaAdminFile qnaAdminFile = qnaAdminMapper.getQnaAdminFile(qnaAdminFileId);
			qnaAdminFile.setQnaAdminId(qnaAdminId);			// 파일 경로 추출에 필요
			ResponseEntity<byte[]> data = null;
			if (StringUtils.isEmpty(qnaAdminFile.getPathName())) {
				data = FileDownloadCustom.fileDownloadCustom(qnaAdminFile.getUploadPath() + "/" + qnaAdminFile.getFileName(), qnaAdminFile.getOrgFileName());
			} else {
				data = FileDownloadCustom.fileDownloadCustom(SalesonProperty.getUploadSaveFolder() + "/" + qnaAdminFile.getPathName() + "/" + qnaAdminFile.getFileName(), qnaAdminFile.getOrgFileName());
			}
			return data;
		} catch (IOException e) {
			log.error("================= downloadQnaAdminFile error:: ================");
			return null;
		}
	}

	@Override
	public ResponseEntity<byte[]> downloadQnaAdminAnswerFile(long qnaAdminAnswerId, long qnaAdminAnswerFileId) {
		try {
			QnaAdminAnswer qnaAdminAnswer = getQnaAdminAnswerByQnaAdminAnswerId(qnaAdminAnswerId);
			QnaAdminAnswerFile qnaAdminAnswerFile = qnaAdminMapper.getQnaAdminAnswerFile(qnaAdminAnswerFileId);
			qnaAdminAnswerFile.setQnaAdminId(qnaAdminAnswer.getQnaAdminId());			// 파일 경로 추출에 필요
			ResponseEntity<byte[]> data = null;
			if (StringUtils.isEmpty(qnaAdminAnswerFile.getPathName())) {
				data = FileDownloadCustom.fileDownloadCustom(qnaAdminAnswerFile.getUploadPath() + "/" + qnaAdminAnswerFile.getFileName(), qnaAdminAnswerFile.getOrgFileName());
			} else {
				data = FileDownloadCustom.fileDownloadCustom(SalesonProperty.getUploadSaveFolder() + "/" + qnaAdminAnswerFile.getPathName() + "/" + qnaAdminAnswerFile.getFileName(), qnaAdminAnswerFile.getOrgFileName());
			}
			return data;
		} catch (IOException e) {
			log.error("================= downloadQnaAdminAnswerFile error:: ================");
			return null;
		}
	}

	@Override
	public List<QnaAdminFile> deleteQnaAdminFile(long qnaAdminId, long qnaAdminFileId, boolean isDeleteQnaAdmin, String locgovCode) {
		if (isQnaAdminWriter(qnaAdminId, locgovCode)) {
			QnaAdminFile qnaAdminFile = qnaAdminMapper.getQnaAdminFile(qnaAdminFileId);
			qnaAdminFile.setQnaAdminId(qnaAdminId);
			qnaAdminMapper.deleteQnaAdminFile(qnaAdminFile);
			
			fileStorage.delete(qnaAdminFile.getUploadPath() + "/" + qnaAdminFile.getFileName());
		} else {
			throw new OpRuntimeException("권한이 없습니다.");		// 권한이 없습니다.
		}
		if (isDeleteQnaAdmin) {
			return null;
		}
		return qnaAdminMapper.getQnaAdminFileList(qnaAdminMapper.getQnaAdminByQnaAdminId(qnaAdminId));
	}
	
	private boolean isQnaAdminWriter(long qnaAdminId, String locgovCode) {
		User user = UserUtils.getUser();
		QnaAdmin savedQnaAdmin = qnaAdminMapper.getQnaAdminByQnaAdminId(qnaAdminId);
		
		// 사용자 일치 검증
		if (user.getUserId() == savedQnaAdmin.getUserId()) {
			return true;
		} else if (UserUtils.isManagerLogin() && savedQnaAdmin.getLocgovCode().equals(locgovCode)) {		// 관리자 체크
			return true;
		}
		return false;
	}
	
	private boolean isQnaAdminAnswerWriter(long qnaAdminAnswerId) {
		User user = UserUtils.getUser();
		QnaAdminAnswer savedQnaAdminAnswer = getQnaAdminAnswerByQnaAdminAnswerId(qnaAdminAnswerId);
		
		// 사용자 일치 검증
		if (user.getUserId() == savedQnaAdminAnswer.getUserId()) {
			return true;
		}
		return false;
	}

	@Override
	public List<QnaAdminAnswerFile> deleteQnaAdminAnswerFile(long qnaAdminId
			, long qnaAdminAnswerId, long qnaAdminAnswerFileId, boolean isDeleteQnaAdminAnswer) {
		if (isQnaAdminAnswerWriter(qnaAdminAnswerId)) {
			QnaAdminAnswerFile qnaAdminAnswerFile = qnaAdminMapper.getQnaAdminAnswerFile(qnaAdminAnswerFileId);
			qnaAdminAnswerFile.setQnaAdminId(qnaAdminId);
			qnaAdminAnswerFile.setQnaAdminAnswerId(qnaAdminAnswerId);
			qnaAdminMapper.deleteQnaAdminAnswerFile(qnaAdminAnswerFile);
			
			fileStorage.delete(qnaAdminAnswerFile.getUploadPath() + "/" + qnaAdminAnswerFile.getFileName());
		} else {
			throw new OpRuntimeException("권한이 없습니다.");		// 권한이 없습니다.
		}
		if (isDeleteQnaAdminAnswer) {
			return new ArrayList<>();
		}
		return qnaAdminMapper.getQnaAdminAnswerFileList(getQnaAdminAnswerByQnaAdminAnswerId(qnaAdminAnswerId));
	}

	@Override
	public QnaAdminAnswer getQnaAdminAnswerByQnaAdminAnswerId(long qnaAdminAnswerId) {
		return qnaAdminMapper.getQnaAdminAnswerByQnaAdminAnswerId(qnaAdminAnswerId);
	}

	@Override
	public void deleteQnaAdminList(QnaAdminListParam qnaAdminListParam) throws UserException {
		if (qnaAdminListParam.getId() != null && qnaAdminListParam.getId().length > 0) {
			for (String qnaId : qnaAdminListParam.getId()) {
				QnaAdmin qnaAdmin = new QnaAdmin();
				qnaAdmin.setQnaAdminId(Long.valueOf(qnaId));
				qnaAdmin.setLocgovCode(qnaAdminListParam.getLocgovCode());
				deleteQnaAdmin(qnaAdmin);
			}
		}
	}
}
