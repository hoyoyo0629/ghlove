package saleson.shop.qnaadmin;


import java.util.List;

import saleson.shop.qnaadmin.domain.QnaAdmin;
import saleson.shop.qnaadmin.domain.QnaAdminAnswer;
import saleson.shop.qnaadmin.domain.QnaAdminAnswerFile;
import saleson.shop.qnaadmin.domain.QnaAdminFile;
import saleson.shop.qnaadmin.support.QnaAdminParam;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("qnaAdminMapper")
 public interface QnaAdminMapper {
	
	/**
	 * 문의 등록
	 * @param qnaAdmin
	 */
	public void insertQnaAdmin(QnaAdmin qnaAdmin);
	
	/**
	 * 문의 수정
	 * @param qnaAdmin
	 */
	public void updateQnaAdmin(QnaAdmin qnaAdmin);
	
	/**
	 * 문의 삭제
	 * @param qnaAdmin
	 */
	public void deleteQnaAdmin(QnaAdmin qnaAdmin);
	
	/**
	 * 문의 답변 등록
	 * @param qnaAdminAnswer
	 */
	public void insertQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer);
	
	/**
	 * 문의 답변 수정
	 * @param qnaAdminAnswer
	 */
	public void updateQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer);
	
	/**
	 * 문의 답변 삭제
	 * @param qnaAdminAnswer
	 */
	public void deleteQnaAdminAnswer(QnaAdminAnswer qnaAdminAnswer);
	
	/**
	 * 문의 목록 조회
	 * @param qnaAdminParam
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
	 * 답변 조회
	 * @param qnaAdminAnswerId
	 * @return
	 */
	public QnaAdminAnswer getQnaAdminAnswerByQnaAdminAnswerId(long qnaAdminAnswerId);
	
	/**
	 * 해당 문의 답변수 업데이트
	 * @param qnaAdminId
	 */
	public void updateQnaAdminAnswerCount(long qnaAdminId);
	
	/**
	 * 지자체 문의 첨부파일 추가
	 * @param qnaAdminFile
	 */
	public int insertQnaAdminFile(QnaAdminFile qnaAdminFile);
	
	/**
	 * 지자체 문의 첨부파일 삭제
	 * @param qnaAdminFile
	 */
	public int deleteQnaAdminFile(QnaAdminFile qnaAdminFile);
	
	/**
	 * 지자체 문의 첨부파일목록 조회
	 * @param qnaAdmin
	 */
	public List<QnaAdminFile> getQnaAdminFileList(QnaAdmin qnaAdmin);
	
	/**
	 * 지자체 문의 첨부파일 조회
	 * @param qnaAdminFileId
	 */
	public QnaAdminFile getQnaAdminFile(long qnaAdminFileId);
	
	/**
	 * 관리자 답변 첨부파일 추가
	 * @param qnaAdminAnswerFile
	 */
	public int insertQnaAdminAnswerFile(QnaAdminAnswerFile qnaAdminAnswerFile);
	
	/**
	 * 관리자 답변 첨부파일 삭제
	 * @param qnaAdminAnswerFile
	 */
	public int deleteQnaAdminAnswerFile(QnaAdminAnswerFile qnaAdminAnswerFile);
	
	/**
	 * 관리자 답변 첨부파일목록 조회
	 * @param qnaAdminAnswer
	 */
	public List<QnaAdminAnswerFile> getQnaAdminAnswerFileList(QnaAdminAnswer qnaAdminAnswer);
	
	/**
	 * 관리자 답변 첨부파일 조회
	 * @param qnaAdminAnswerFileId
	 */
	public QnaAdminAnswerFile getQnaAdminAnswerFile(long qnaAdminAnswerFileId);
	
}
