package saleson.shop.qustnr.support;

import java.util.List;
import java.util.Objects;

import lombok.Data;
import saleson.shop.qustnr.domain.QustnrIem;
import saleson.shop.qustnr.domain.QustnrQesitm;

@Data
public class QustnarData {
	private long qustnrSn;
	private long qustnrQesitmSn;
	private long qustnrIemSn;
	private long qestnSn;
	private String qestnTyCode;
	private String etcAnswerCn; // 주관식 설문답변에 사용
	private int parentSn;
	private int iemSn;
	private String iemCn;
	private String qestnCn;
	private int userCnt;
	private int selected;

	public QustnrQesitm getQustnrQesitm() {
		return QustnrQesitm.builder()
						   .qustnrSn(this.qustnrSn)
						   .qustnrQesitmSn(this.qustnrQesitmSn)
						   .qustnrSn(this.qustnrSn)
						   .qestnCn(this.qestnCn)
						   .selected(this.selected)
						   .qestnTyCode(this.qestnTyCode)
						   .etcAnswerCn(this.etcAnswerCn)
						   .parentSn(this.parentSn)
						   .build();
	}

	public QustnrIem getQustnrIem() {
		return QustnrIem.builder()
					    .qustnrSn(this.qustnrSn)
					    .qustnrQesitmSn(this.qustnrQesitmSn)
					    .qustnrIemSn(this.qustnrIemSn)
					    .iemSn(this.iemSn)
					    .iemCn(this.iemCn)
					    .userCnt(this.userCnt)
					    .build();
	}

	@Override
	public int hashCode() {
		return Objects.hash(qustnrQesitmSn, qustnrSn);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QustnarData other = (QustnarData) obj;
		return qustnrQesitmSn == other.qustnrQesitmSn && qustnrSn == other.qustnrSn;
	}



}
