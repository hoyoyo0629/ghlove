package saleson.shop.qna;


import java.util.List;

import saleson.shop.databoard.domain.DataboardFile;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.domain.QnaAnswer;
import saleson.shop.qna.domain.QnaOpen;
import saleson.shop.qna.domain.QnaOpenFile;
import saleson.shop.qna.support.QnaOpenParam;
import saleson.shop.qna.support.QnaParam;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

@Mapper("qnaMapper")
 public interface QnaMapper {

	/**
	 * 문의 등록
	 * @param qna
	 */
	public int insertQna(Qna qna);

	/**
	 * 문의 수정
	 * @param qna
	 */
	public int updateQna(Qna qna);

	/**
	 * 문의 삭제
	 * @param qna
	 */
	public int deleteQna(Qna qna);

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
	 * 해당 문의 답변수 업데이트
	 * @param qnaId
	 */
	public void updateQnaAnswerCount(int qnaId);

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
	 * QNA 조회수 증가
	 * @param qnaOpenParam
	 * @return
	 */
	public int addQnaHitCount(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 답변 조회수 증가
	 * @param qnaOpenParam
	 * @return
	 */
	public int addQnaAnswerHitCount(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 파일 목록 조회
	 * @param qnaOpenParam
	 * @return
	 */
	public List<QnaOpenFile> getFrontQnaFileList(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 답변 파일 목록 조회
	 * @param qnaOpenParam
	 * @return
	 */
	public List<QnaOpenFile> getFrontQnaAnswerFileList(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 파일 상세 조회
	 * @param qnaOpenParam
	 * @return
	 */
	public QnaOpenFile getFrontQnaFileDetail(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 답변 파일 상세 조회
	 * @param qnaOpenParam
	 * @return
	 */
	public QnaOpenFile getFrontQnaAnswerFileDetail(QnaOpenParam qnaOpenParam);

	/**
	 * QNA 파일 등록
	 * @param file
	 * @return
	 */
	public int insertQnaFile(QnaOpenFile file);

	/**
	 * QNA 파일 삭제
	 * @param file
	 * @return
	 */
	public int deleteQnaFile(QnaOpenFile file);

	/**
	 * QNA ANSWER 파일 삭제
	 * @param file
	 * @return
	 */
	public int deleteQnaAnswerFile(QnaOpenFile file);

	/**
	 * QNA 삭제
	 * @param qna
	 * @return
	 */
	public int deleteFrontQna(Qna qna);

	/**
	 * QNA 수정
	 * @param setQnaData
	 * @return
	 */
	public int updateFrontQnaOpen(Qna setQnaData);

	/**
	 * QNA 파일 재정렬
	 * @param qnaOpenParam
	 * @return
	 */
	public int updateQnaFileOrdering(Qna qna);

	void insertQnaAnswerFile(QnaOpenFile qnaOpenFile);

	/**
	 * 해당 문의 답변수 업데이트 0
	 * @param qnaAnswerId
	 */
	public void updateQnaAnswerCountZero(int qnaAnswerId);

	/**
	 * sms 전송 사용자 정보
	 * @param qnaId
	 * @return
	 */
	public GiveUserSmsInfo getQnaUserInfo(int qnaId);

	/**
	 * 5분이내 문의 등록 체크
	 *
	 * @param qnaOpen
	 * @return
	 */
	public int getFiveMinuteCheck(QnaOpen qnaOpen);


	/**
	 * 문의 조회
	 * @param qnaParam
	 * @return
	 */
	public Qna getQnaByQnaAnswerId(int qnaAnswerId);

	/**
	 * 문의별 답변 조회
	 * @param qnaId
	 * @return
	 */
	public QnaAnswer getQnaAnswerByQnaAnswerId(Qna qnaParam);

}
