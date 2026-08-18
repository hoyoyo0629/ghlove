package saleson.api.qna.domain;

import com.onlinepowers.framework.repository.Code;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.qna.domain.Qna;
import saleson.shop.qna.domain.QnaAnswer;
import saleson.shop.qna.domain.QnaOpenFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaInfo {

    public QnaInfo(Qna qna) {
        baseQnaInfo(qna, 0);
    }

    public QnaInfo(Qna qna, long userId) {
        baseQnaInfo(qna, userId);
    }

    private void baseQnaInfo(Qna qna, long userId) {

        if (qna != null) {

            setUserId(qna.getUserId());

            setQnaId(qna.getQnaId());
            setQnaGroup(qna.getQnaGroup());
            setQnaType(qna.getQnaType());
            setCreatedDate(qna.getCreatedDate());

            setMaskUserName(UserUtils.masking(qna.getUserName(), "name"));

            if (!ObjectUtils.isEmpty(qna.getItemImage())) {
                setItemImage(ShopUtils.loadImage(qna.getItemUserCode(), qna.getItemImage(), "M"));
            }

            if (!ObjectUtils.isEmpty(qna.getQnaImage())) {
                setImage(qna.getImageSrc());
            }

            setItemId(qna.getItemId());
            setItemName(qna.getItemName());
            setItemUserCode(qna.getItemUserCode());

            if ("Y".equals(qna.getSecretFlag())) {
                if (userId == qna.getUserId()) {
                    setContent(qna);
                } else {		// 임시 추가함(제목이 없어서 화면단에서 오류 발생)
                	qna.setSubject("비밀글입니다.");
                	qna.setQuestion("비밀글입니다.");
                	
                	QnaAnswer regQnaAnswr = qna.getQnaAnswer();
                	if ( regQnaAnswr != null) {
                		regQnaAnswr.setTitle("비밀글입니다.");
                		regQnaAnswr.setAnswer("비밀글입니다.");
                	}
                	setContent(qna);
                }
            } else {
                setContent(qna);
            }

            setSecretFlag(qna.getSecretFlag());

            setQnaGroupLabel(getGroupLabel(qna.getQnaGroup()));
            setQnaOpenFileList(qna.getQnaOpenFileList());
            setQnaOpenAnswerFileList(qna.getQnaOpenAnswerFileList());
            setLocgovName(qna.getLocgovName());
            
            setRownum(qna.getRownum());
            if (qna.getQnaAnswer() != null && "ADMIN".equalsIgnoreCase(qna.getQnaAnswer().getRoleNm())) {
                setSellerCompanyName("관리자");
            } else {
                setSellerCompanyName(qna.getSellerCompanyName());
            }
        }
    }

    private String getGroupLabel(String qnaGroup) {
        String label = "기타문의";
        List<Code> qnaGroups = CodeUtils.getCodeList("QNA_GROUPS");

        for (Code code : qnaGroups) {
            if (!ObjectUtils.isEmpty(qnaGroup) && qnaGroup.equals(code.getId())) {
                label = code.getLabel();
                break;
            }
        }

        return label;
    }

    private void setContent(Qna qna) {
        setSubject(qna.getSubject());
        setQuestion(qna.getQuestion());
        setAnswerCount(qna.getAnswerCount());
        setQnaAnswer(qna.getQnaAnswer());

    }

    private long userId;
    private int qnaId;
    private String qnaGroup;
    private String qnaType;
    private String qnaGroupLabel;
    private String createdDate;
    private String subject;
    private String question;
    private int itemId;
    private String itemName;
    private String itemUserCode;
    private String itemImage;
    private String maskUserName;
    private int answerCount;
    private String secretFlag;
    private String image;
    private String locgovName;
    QnaAnswer qnaAnswer;
    private List<QnaOpenFile> qnaOpenFileList;
    private List<QnaOpenFile> qnaOpenAnswerFileList;
    
    private long rownum;
    
    private String sellerCompanyName;
}
