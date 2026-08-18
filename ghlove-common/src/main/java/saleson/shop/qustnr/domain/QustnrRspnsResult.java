package saleson.shop.qustnr.domain;

import lombok.Data;

@Data
public class QustnrRspnsResult {
	
	private long qustnrSn;
	private long qustnrQesitmSn;
	private long qustnrIemSn;
	private long userId;
	private String respondAnswerCn;
	private String etcAnswerCn;
	
}
