package saleson.shop.welfarecenter.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.user.domain.ManagerRequest;

@Getter
@Setter
@NoArgsConstructor
public class WlfrCntrMngManagerRequest extends ManagerRequest{

	// 행정 복지 센터 아이디
	private long pbadmsWlfrCntrId;

	// 지자체 코드
	private String lclgvCd;
	
	// 소속코드
	private String psitnCode;
	
	// 소속명
	private String psitnNm;
	
	// 행정 복지 센터 명
	private String pbadmsWlfrCntrNm;

	// 행정 복지 센터 코드
	private String pbadmsWlfrCntrCd;

	// 사용 여부
	private String useYn;

	// 최초 등록자 아이디
	private long frstRgtrId;

	// 최초 등록 일시
	private String frstRegDt;

	// 최종 등록자 아이디
	private long lastRgtrId;

	// 최종 등록 일시
	private String lastRegDt;

	// 권한요청한 사용자의 현재 지자체 코드
	private String locgovCode;

	// 삭제여부에 보여줄 상태
	private String active;

	// 상위 지자체명
	private String upperLocgovNm;

	// 지자체명
	private String locgovNm;
	
	private String managerLocgovCode;

	public void setExtendsCopy(ManagerRequest managerRequest) {
		if (managerRequest != null) {
			setUserId(managerRequest.getUserId());
			setReqstSn(managerRequest.getReqstSn());
			setLoginId(managerRequest.getLoginId());
			setLocgovCode(managerRequest.getLocgovCode());
			setReqstSeCode(managerRequest.getReqstSeCode());
			setPsitnCode(managerRequest.getPsitnCode());
			setPsitnNm(managerRequest.getPsitnNm());
			setPsitnDeptNm(managerRequest.getPsitnDeptNm());
			setOfcpsNm(managerRequest.getOfcpsNm());
			setCttpc(managerRequest.getCttpc());
			setConfmSttusCode(managerRequest.getConfmSttusCode());
			setRejectResn(managerRequest.getRejectResn());
			setFrstRegisterId(managerRequest.getFrstRegisterId());
			setFrstRegistPnttm(managerRequest.getFrstRegistPnttm());
			setLastUpdusrId(managerRequest.getLastUpdusrId());
			setLastUpdtPnttm(managerRequest.getLastUpdtPnttm());
			setPassword(managerRequest.getPassword());

			setUserName(managerRequest.getUserName());
			setEmail(managerRequest.getEmail());
			setPhoneNumber(managerRequest.getPhoneNumber());
			setBirthday(managerRequest.getBirthday());
			setReceiveEmail(managerRequest.getReceiveEmail());

			setConfmSttusNm(managerRequest.getConfmSttusNm());
			setReqstSeNm(managerRequest.getReqstSeNm());
			setLocgovNm(managerRequest.getLocgovNm());
			setUpperlocgovNm(managerRequest.getUpperlocgovNm());
			setUpperLocgovCode(managerRequest.getUpperLocgovCode());
			setBankNm(managerRequest.getBankNm());

			setUpdIdRole(managerRequest.getUpdIdRole());
		}
	}
}

