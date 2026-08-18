package saleson.shop.qustnr.domain;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QustnrQesitm {

	private long qustnrSn;
	private long qustnrQesitmSn;
	private long qestnSn;
	private int parentSn;
	private String qestnTyCode;
	private String qestnCn;
	private String answerChoiseCo;
	private long frstRegisterId;
	private String frstRegistPnttm;
	private long lastUpdusrId;
	private String lastUpdtPnttm;
	private int selected;
	private String etcAnswerCn;// 주관식 답변 트랜스 DATA

	List<QustnrIem> qustnrIem;


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QustnrQesitm other = (QustnrQesitm) obj;
		return qustnrQesitmSn == other.qustnrQesitmSn && qustnrSn == other.qustnrSn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(qustnrQesitmSn, qustnrSn);
	}

}
