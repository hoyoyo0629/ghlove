package saleson.shop.mypage.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.mypage.domain.IntrstLocGov;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cntr {

	private String cntrSn;
	private String cntrDe;
	private String locgovCode;
    private String upperLocgovNm;
    private String locgovNm;
    private String cntrAmt;
	private String cntrPoint;
	private String elctrnPayNo;
	private String sttemntPayDe;
	private String sttemntPayDeCase;
	private String cntrLocgovCode;
	private String mngNo;
	private String pointRate;
	private String cntrBlcePoint;


	/**
	 * 특정사업기부 사업명
	 */
	private long prjId;

	/**
	 * 특정사업기부 사업명
	 */
	private String prjSubject;

	/**
	 * 기부 연계 기관명
	 */
	private String detail;

	/**
	 * 연계 기관 코드
	 */
	private String linkInsttCd;

	// 지자체 사업자등록번호
	private String bizRno;

	// 포멧 없는 오리지널 기부 날짜
	private String originCntrDe;

	// 특별재난기부 여부
	private String spelDstrYn;
}
