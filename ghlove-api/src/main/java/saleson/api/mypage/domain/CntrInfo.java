package saleson.api.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.mypage.domain.Cntr;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CntrInfo {

	private String cntrSn;
	private String cntrDe;
	private String locgovCode;
    private String upperLocgovNm;
    private String locgovNm;
    private String cntrAmt;
	private String cntrPoint;
	private String cntrBlcePoint;
	private String elctrnPayNo;
	private String sttemntPayDe;
	private String sttemntPayDeCase;
	private String cntrLocgovCode;
	private String mngNo;
	private String pointRate;


	/**
	 * 툭정사업기부 사업명
	 */
	private long prjId;

	/**
	 * 툭정사업기부 사업명
	 */
	private String prjSubject;

	/**
	 * 기부 연계 기관 코드
	 */
	private String detail;

	/**
	 * 연계 기관 코드
	 */
	private String linkInsttCd;

	// 지자체 사업자등록번호
	private String bizRno;

	// 특별재난기부 여부
	private String spelDstrYn;

	public CntrInfo(Cntr cntr) {
		if(cntr !=null) {
			setCntrSn(cntr.getCntrSn());
			setCntrDe(cntr.getCntrDe());
			setLocgovCode(cntr.getLocgovCode());
			setUpperLocgovNm(cntr.getUpperLocgovNm());
			setLocgovNm(cntr.getLocgovNm());
			setCntrAmt(cntr.getCntrAmt());
			setCntrPoint(cntr.getCntrPoint());
			setCntrBlcePoint(cntr.getCntrBlcePoint());
			setElctrnPayNo(cntr.getElctrnPayNo());
			setSttemntPayDe(cntr.getSttemntPayDe());
			setSttemntPayDeCase(cntr.getSttemntPayDeCase());
			setCntrLocgovCode(cntr.getCntrLocgovCode());
			setMngNo(cntr.getMngNo());
			setPointRate(cntr.getPointRate());

			setPrjId(cntr.getPrjId());
			setPrjSubject(cntr.getPrjSubject());
			setDetail(cntr.getDetail());
			setLinkInsttCd(cntr.getLinkInsttCd());
			setBizRno(cntr.getBizRno());
			setSpelDstrYn(cntr.getSpelDstrYn());
		}
	}
}
