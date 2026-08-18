package saleson.shop.qustnr.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QustnrIem {

	private long qustnrSn;
	private long qustnrQesitmSn;
	private long qustnrIemSn;
	private int iemSn;
	private String iemCn;
	private int parentSn;
	private String etcAnswerAt;
	private String etcAnswerCn; // 주관식 설문답변에 사용
	private long frstRegisterId;
	private String frstRegistPnttm;
	private long lastUpdusrId;
	private String lastUpdtPnttm;

	private int userCnt;

}
