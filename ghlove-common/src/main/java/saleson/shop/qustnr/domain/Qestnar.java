package saleson.shop.qustnr.domain;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import saleson.shop.qustnr.support.QustnarData;

@Data
public class Qestnar {

	private long qustnrSn;
	private String qustnrSj;
	private String qustnrPurps;
	private String qustnrBgnDe;
	private String qustnrEndDe;
	private long frstRegisterId;
	private String frstRegistPnttm;
	private long lastUpdusrId;
	private String lastUpdtPnttm;
	private long maxQesitm;
	private String isShow;

	private String srvyTrgt;
	private String checkQustnrCode;
	private String checkQustnrMsg;

	private int qustnrCnt;
	private int regCnt;

	private int parentSn;

	private List<QustnrQesitm> qustnrQesitm;

	public void setQustnrQesitmList(List<QustnarData> list) {
		this.qustnrQesitm = list.stream().distinct().map(q -> q.getQustnrQesitm()).collect(Collectors.toList());

		if (this.qustnrQesitm != null && this.qustnrQesitm.size() > 0) {
			for (QustnrQesitm q : this.qustnrQesitm) {
				q.setQustnrIem(list.stream().filter(a -> a.getQustnrSn() == q.getQustnrSn() && a.getQustnrQesitmSn() == q.getQustnrQesitmSn()).map(a -> a.getQustnrIem()).collect(Collectors.toList()));
			}
		}

	}

}