package saleson.shop.qna;

import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.onlinepowers.framework.web.domain.ListParam;

import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.domain.QnaAnswer;
import saleson.shop.qna.domain.QnaOpen;
import saleson.shop.qna.domain.QnaOpenFile;
import saleson.shop.qna.support.QnaOpenParam;
import saleson.shop.qna.support.QnaParam;


public interface QnaService {

	/**
	 * 문의 등록
	 * @param qna
	 */
	public void insertQna(Qna qna);

	/**
	 * 문의 수정
	 * @param qna
	 */
	public void updateQna(Qna qna);

	/**
	 * 문의 삭제
	 * @param qna
	 */
	public void deleteQna(Qna qna);

	/**
	 * 문의 답변 등록
	 * @param qnaAnswer
	 */
	public void insertQnaAnswer(QnaAnswer qnaAnswer);

	/**
	 * 문의 답변 수정
	 * @param qnaAnswer
	 */
	public void updateQnaAnswer(QnaAnswer qnaAnswer);

	/**
	 * 문의 답변 삭제
	 * @param qnaAnswer
	 */
	public void deleteQnaAnswer(QnaAnswer qnaAnswer);

	/**
	 * 문의 목록 조회
	 * @param qnaParam
	 * @return
	 */
	public List<Qna> getQnaListByParam(QnaParam qnaParam);

	/**
	 * 문의 조회
	 * @param qnaParam
	 * @return
	 */
	public Qna getQnaByQnaId(int qnaId);

	/**
	 * 문의 목록 카운트
	 * @param qnaParam
	 * @return
	 */
	public int getQnaListCountByParam(QnaParam qnaParam);

	/**
	 * 문의별 답변 조회
	 * @param qnaId
	 * @return
	 */
	public List<QnaAnswer> getQnaAnswerListByQnaId(int qnaId);

	/**
	 * 문의별 답변 조회
	 * @param qnaId
	 * @return
	 */
	public QnaAnswer getQnaAnswerByQnaId(int qnaId);


	/**
	 * 문의 조회_답변id로 문의 조회
	 * @param qnaId
	 * @return
	 */
	public Qna getQnaByQnaAnswerId(int qnaAnswerId);

	/**
	 * 문의 답변 조회_답변id로 답변 조회
	 * @param qnaId
	 * @return
	 */
	public QnaAnswer getQnaAnswerByQnaAnswerId(Qna qnaParam);


	/**
	 * 문의 페이징 정보 셋팅
	 * @param qnaParam
	 */
	public void setQnaListPagination(QnaParam qnaParam);

	/**
	 * 문의 목록형식으로 삭제
	 * @param listparam
	 */
	public void deleteQnaData(ListParam listparam);

	/**
	 * 해당 문의 답변수 업데이트
	 * @param qnaId
	 */
	public void updateQnaAnswerCount(int qnaId);

	/**
	 * 문의 암호화
	 * @param qna
	 */
	void encryptQnaData(Qna qna);

	/**
	 * QNA 카운트
	 * @param qnaOpenParam
	 * @return
	 */
	public int getFrontQnaOpenListCount(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 질문 카운트
	 * @param qnaOpenParam
	 * @return
	 */
	public int getFrontQnaOpenQuestionListCount(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 리스트(관리자)
	 * @param qnaOpenParam
	 * @return
	 */
	public List<QnaOpen> getFrontQnaOpenManagerList(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 카운트(관리자)
	 * @param qnaOpenParam
	 * @return
	 */
	public int getFrontQnaOpenManagerListCount(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 리스트
	 * @param qnaOpenParam
	 * @return
	 */
	public List<QnaOpen> getFrontQnaOpenList(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 상세
	 * @param qnaOpenParam
	 * @return
	 */
	public QnaOpen getFrontQnaOpenDetail(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 파일 목록 조회
	 * @param qnaOpenParam
	 * @return
	 */
	public List<QnaOpenFile> getFrontQnaOpenFileList(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 파일 상세 조회
	 * @param qnaOpenParam
	 * @return
	 */
	public QnaOpenFile getFrontQnaOpenFileDetail(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 등록
	 * @param qnaOpen
	 * @return
	 */
	public String insertQnaOpen(QnaOpen qnaOpen) throws RuntimeException;

	/**
	 * QNA 삭제
	 * @param qnaOpenParam
	 * @return
	 */
	public String deleteQnaOpen(QnaOpenParam qnaOpenParam) throws RuntimeException;

	/**
	 * QNA 조회수 증가
	 * @param qnaOpenParam
	 * @return
	 */
	public int addHitCount(QnaOpenParam qnaOpenParam) throws RuntimeException;

	/**
	 * QNA 수정
	 * @param qnaOpen
	 * @return
	 */
	public String updateQnaOpen(QnaOpen qnaOpen) throws RuntimeException;

	/**
	 * 첨부파일 삭제
	 * @param id
	 * @return
	 */
	public void deleteItemImageByItemId(int fileId);

	/**
	 * 첨부파일 삭제
	 * @param id
	 * @return
	 */
	public void deleteItemImageByItemId(String fileId);

	/**
	 * Qna 답변에 대한 메세지 전송
	 * @param qnaId
	 */
	public void sendSmsQnaAnswer(int qnaId);

	/**
	 * QnA 5분이내 등록 방지
	 *
	 * @param qnaOpen
	 * @return
	 */
	public int getFiveMinuteCheck(QnaOpen qnaOpen);

	/**
	 * 스트리밍 엑셀다운로드
	 *
	 * @param	qnaParam
	 * @throws	Exception
	 */
	public SXSSFWorkbook streamQnaItemData(QnaParam qnaParam) throws Exception;
}
