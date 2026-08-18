package saleson.shop.designateddonation.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.user.domain.ManagerRequest;

@Getter
@Setter
@NoArgsConstructor
public class DsgnCntrManagerRequest extends ManagerRequest {
	
	// 승인상태에서 참조할 지정기부 부서정보
	private long dsgncntrPartId;
	
	// 권한요청한 사용자의 현재 지자체 코드
	private String managerLocgovCode;
	
	// 삭제여부에 보여줄 상태
	private String active;
	
	// 대기상태에서 참조할 지정기부 부서정보
	private long reqDsgncntrPartId;
	
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
