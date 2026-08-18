package saleson.api.qna.domain;

import org.springframework.util.ObjectUtils;

import com.onlinepowers.framework.util.DateUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.qna.domain.QnaOpen;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QnaOpenInfo {
	private Integer qnaId;			// 문의 ID
	private Integer qnaAnswerId;	// 문의 답변 ID
	private String createdDate;		// 등록일
	private Long userId;			// 회원 ID
	private String userName;		// 회원명
	private String qnaDetailType;	// QNA 상세 타입 (Q:질문, A:답변)
	private String subject;			// 제목
	private Integer hits;			// 조회수
	private String secretFlag;		// 비밀글 여부 (Y:비밀글, N:일반)
	private String question;		// 내용
	private Integer answerCount;	// 문의답변 수
	private String qnaGroup;		// 1차 분류 (공통코드 codeType : QNA_GROUPS)
	private String prevQnaInfo;		// 이전글 QNA 정보 	(ex: qnaId|qnaAnswerId|subject)
	private String nextQnaInfo;		// 다음글 QNA 정보 	(ex: qnaId|qnaAnswerId|subject)
	private String myCheck;			// 본인글 확인
	private Long qnaUserId;			// QNA 작성자 UserId(답변글)
	private String qnaSecretFlag;	// QNA 비밀글여부(답변글)
	
	public QnaOpenInfo(QnaOpen qnaOpen) {
		if(qnaOpen != null) {
			setQnaId(CommonUtils.intNvl(qnaOpen.getQnaId()));
			setQnaAnswerId(CommonUtils.intNvl(qnaOpen.getQnaAnswerId()));
			
			String createdDate = CommonUtils.dataNvl(qnaOpen.getCreatedDate());
			if (!ObjectUtils.isEmpty(createdDate)) {
				createdDate = DateUtils.date(createdDate);
			}
			
			userId = UserUtils.getUserId();
			
			if ("Y".equals(qnaOpen.getSecretFlag())) {
                if (userId.equals(qnaOpen.getUserId())) {
                	qnaOpen.setMyCheck("Y");
                } else {
                	qnaOpen.setSubject("비밀글입니다.");
                	qnaOpen.setQuestion("비밀글입니다.");
                	qnaOpen.setMyCheck("N");
                }
            } else {
            	qnaOpen.setMyCheck("N");
            }
			
			String detailType = qnaOpen.getQnaDetailType();
        	if("A".equals(detailType)) {
        		if("Y".equals(qnaOpen.getQnaSecretFlag())) {
        			qnaOpen.setSecretFlag("Y");
        		}
        		if(userId.equals(qnaOpen.getQnaUserId())) {
            		qnaOpen.setMyCheck("Y");
        		}
        	}
			
			setCreatedDate(createdDate);
			setUserId(CommonUtils.longNvl(qnaOpen.getUserId()));
			setUserName(CommonUtils.dataNvl(qnaOpen.getUserName()));
			setQnaDetailType(CommonUtils.dataNvl(qnaOpen.getQnaDetailType()));
			setSubject(CommonUtils.dataNvl(qnaOpen.getSubject()));
			setHits(CommonUtils.intNvl(qnaOpen.getHits()));
			setSecretFlag(CommonUtils.dataNvl(qnaOpen.getSecretFlag()));
			setQuestion(CommonUtils.dataNvl(qnaOpen.getQuestion()));
			setAnswerCount(CommonUtils.intNvl(qnaOpen.getAnswerCount()));
			setQnaGroup(CommonUtils.dataNvl(qnaOpen.getQnaGroup()));
			setPrevQnaInfo(CommonUtils.dataNvl(qnaOpen.getPrevQnaInfo()));
			setNextQnaInfo(CommonUtils.dataNvl(qnaOpen.getNextQnaInfo()));
			setMyCheck(CommonUtils.dataNvl(qnaOpen.getMyCheck()));
			setQnaUserId(qnaOpen.getQnaUserId());
			setQnaSecretFlag(CommonUtils.dataNvl(qnaOpen.getSecretFlag()));
			setUserId(null);
			setQnaUserId(null);
		}
	}
}
