package saleson.api.qna.support;

import org.springframework.stereotype.Component;
import saleson.api.qna.domain.QnaInfo;
import saleson.shop.qna.domain.Qna;

import java.util.ArrayList;
import java.util.List;

@Component
public class QnaDataSupport {

    public List<QnaInfo> qnaDataSet(List<Qna> list, long userId) {
        List<QnaInfo> resultList = new ArrayList<>();

        if (list != null && !list.isEmpty()) {
            for (Qna qna : list) {
                resultList.add(new QnaInfo(qna, userId));
            }
        }

        return resultList;
    }
}
