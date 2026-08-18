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
public class CntrPoint {

	private String locgovCode;
    private String upperLocgovNm;
    private String locgovNm;
	private String cntrSn;
	private String cntrPoint;
	private String cntrUsePoint;
	private String cntrBlcePoint;

	//기부 포인트 상세 정보 추가
	private String cntrDe;
	private String userName;
	private String loginId;
	private String cntrAmt;
	private String orderCode;
	private String urlAdres;
	private String userGrade;

	/**
	 * 기부 연계 기관명
	 * 다수의 기부 연계 기관명이 존재하기 때문에 아래와 같이 출력 됨.
	 * 예) 고향사랑e음 등 1 건
	 */
	private String detail;


}
