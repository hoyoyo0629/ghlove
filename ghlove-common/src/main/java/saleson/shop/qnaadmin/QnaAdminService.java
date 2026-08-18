package saleson.shop.qnaadmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.onlinepowers.framework.context.RequestContext;
import com.onlinepowers.framework.exception.UserException;
import com.onlinepowers.framework.web.domain.ListParam;

import saleson.shop.qnaadmin.domain.QnaAdmin;
import saleson.shop.qnaadmin.domain.QnaAdminAnswer;
import saleson.shop.qnaadmin.domain.QnaAdminAnswerFile;
import saleson.shop.qnaadmin.domain.QnaAdminFile;
import saleson.shop.qnaadmin.support.QnaAdminListParam;
import saleson.shop.qnaadmin.support.QnaAdminParam;


public interface QnaAdminService {
	
	/**
	 * 문의 등록
	 * @param qnaAdmin
	 */
	public void insertQnaAdmin(QnaAdmin qnaAdmin);
	
	/**
	 * 문의 수정
	 * @param qnaAdmin
	 * @param request
	 * @throws IllegalArgumentException 
	 */
	public void updateQnaAdmin(QnaAdmin qnaAdmin) throws IllegalArgumentException;
	
	/**
	 * 문의 삭제
	 * @param qnaAdmin
	 * @param request
	 * @throws IllegalArgumentException 
	 */
	public void deleteQnaAdmin(QnaAdmin qnaAdmin) throws IllegalArgumentException;
	
	/**
	 * 문의 답변 등록
	 * @param qnaAdminAnswer
	 */
	public void insertQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer, String locgovCode);
	
	/**
	 * 문의 답변 수정
	 * @param qnaAdminAnswer
	 * @throws IllegalArgumentException 
	 */
	public void updateQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer) throws IllegalArgumentException;
	
	/**
	 * 문의 답변 삭제
	 * @param qnaAdminAnswer
	 * @throws IllegalArgumentException 
	 */
	public void deleteQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer) throws IllegalArgumentException;
	
	/**
	 * 문의 목록 조회
	 * @param qnaAdminParam
	 * @param request
	 * @return
	 */
	public List<QnaAdmin> getQnaAdminListByParam(QnaAdminParam qnaAdminParam);
	
	/**
	 * 문의 조회
	 * @param qnaAdminParam
	 * @return
	 */
	public QnaAdmin getQnaAdminByQnaAdminId(long qnaAdminId);
	
	/**
	 * 문의 목록 카운트
	 * @param qnaAdminParam
	 * @param request
	 * @return
	 */
	public int getQnaAdminListCountByParam(QnaAdminParam qnaAdminParam);
	
	/**
	 * 문의별 답변 조회
	 * @param qnaAdminId
	 * @return
	 */
	public List<QnaAdminAnswer> getQnaAdminAnswerListByQnaAdminId(long qnaAdminId);

	/**
	 * 문의별 답변 조회
	 * @param qnaAdminId
	 * @return
	 */
	public QnaAdminAnswer getQnaAdminAnswerByQnaAdminId(long qnaAdminId);
	
	/**
	 * 문의 페이징 정보 셋팅
	 * @param qnaAdminParam
	 */
	public void setQnaAdminListPagination(QnaAdminParam qnaAdminParam);
	
	/**
	 * 문의 목록형식으로 삭제
	 * @param listparam
	 * @param request
	 * @throws IllegalArgumentException 
	 */
//	public void deleteQnaAdminData(ListParam listparam) throws IllegalArgumentException;
	
	/**
	 * 해당 문의 답변수 업데이트
	 * @param qnaAdminId
	 */
	public void updateQnaAdminAnswerCount(long qnaAdminId);

	/**
	 * 문의 암호화
	 * @param qnaAdmin
	 */
	public void encryptQnaAdminData(QnaAdmin qnaAdmin);
	
	/**
	 * 지자체 코드 가져오기
	 * @param request
	 */
//	public String getLocgovCode(HttpServletRequest request);
	
	/**
	 * 지자체 문의 파일목록 가져오기
	 * @param qnaAdmin
	 */
	public List<QnaAdminFile> getQnaAdminFileList(QnaAdmin qnaAdmin);
	
	/**
	 * 관리자 문의답변 파일목록 가져오기
	 * @param qnaAdminAnswer
	 */
	public List<QnaAdminAnswerFile> getQnaAdminAnswerFileList(QnaAdminAnswer qnaAdminAnswer);
	
	/**
	 * 지자체 문의 파일 다운로드
	 * @param qnaAdminId
	 * @param qnaAdminFileId
	 */
	public ResponseEntity<byte[]> downloadQnaAdminFile(long qnaAdminId, long qnaAdminFileId);
	
	/**
	 * 관리자 문의답변 파일 다운로드
	 * @param qnaAdminAnswerId
	 * @param qnaAdminAnswerFileId
	 */
	public ResponseEntity<byte[]> downloadQnaAdminAnswerFile(long qnaAdminAnswerId, long qnaAdminAnswerFileId);
	
	/**
	 * 문의 첨부파일 개별 삭제
	 * @param qnaAdminId
	 * @param qnaAdminFileId
	 * @param request
	 * @param isDeleteQnaAdmin
	 * @throws IllegalArgumentException 
	 */
	public List<QnaAdminFile> deleteQnaAdminFile(long qnaAdminId, long qnaAdminFileId, boolean isDeleteQnaAdmin, String locgovCode) throws IllegalArgumentException;
	
	/**
	 * 답변 첨부파일 개별 삭제
	 * @param qnaAdminId
	 * @param qnaAdminAnswerId
	 * @param qnaAdminAnswerFileId
	 * @param isDeleteQnaAdminAnswer
	 * @throws IllegalArgumentException
	 */
	public List<QnaAdminAnswerFile> deleteQnaAdminAnswerFile(long qnaAdminId
			, long qnaAdminAnswerId, long qnaAdminAnswerFileId, boolean isDeleteQnaAdminAnswer) throws IllegalArgumentException;

	/**
	 * 문의 답변아이디로 답변 조회 조회
	 * @param qnaAdminAnswerId
	 * @return
	 */
	public QnaAdminAnswer getQnaAdminAnswerByQnaAdminAnswerId(long qnaAdminAnswerId);
	
	/**
	 * 문의 삭제
	 * @param qnaAdminListParam
	 * @param request
	 * @throws UserException 
	 */
	public void deleteQnaAdminList(QnaAdminListParam qnaAdminListParam) throws UserException;
}
