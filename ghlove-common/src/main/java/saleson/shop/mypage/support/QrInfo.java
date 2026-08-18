package saleson.shop.mypage.support;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import lombok.Data;
import saleson.common.utils.QrUtils;
import saleson.common.utils.UserUtils;

@Data
public class QrInfo {
	
	// qr 정상 생성 여부
	private boolean isSuccess;
	
	// 오류 코드
	private String errCode;
	
	// 오류 메시지
	private String errMsg;
	
	private LocalDateTime expireTime;
	
	private int expireSeconds = 35;
	
	// 명예기부 정보
	List<HonorInfo> honorInfos;
	
	public void setHonorInfos(List<HonorInfo> honorInfos) {
		if (honorInfos == null) {
			this.honorInfos = null;
		} else {
			this.honorInfos = new ArrayList<>();
			LocalDateTime startTime = LocalDateTime.now();
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			expireTime = startTime.plusSeconds(expireSeconds);
			for (HonorInfo honorInfo : honorInfos) {
				HonorInfo copyData = new HonorInfo();
				copyData.setGrade(honorInfo.getGrade());
				copyData.setLocgovCode(honorInfo.getLocgovCode());
				copyData.setLocNm(honorInfo.getLocNm());
				copyData.setUserLocNm(honorInfo.getUserLocNm());
				copyData.setGoldCnt(honorInfo.getGoldCnt());
				copyData.setSilverCnt(honorInfo.getSilverCnt());
				copyData.setBronzeCnt(honorInfo.getBronzeCnt());
				copyData.setUserName(UserUtils.getUser().getUserName());
				copyData.setMakeQrDateTime(startTime.format(format));
				this.honorInfos.add(copyData);
			}
			setQrCode(startTime, format);
		}
	}
	
	public List<HonorInfo> getHonorInfos() {
		if (honorInfos == null) {
			return null;
		} else {
			List<HonorInfo> copyHonorInfos = new ArrayList<>();
			for (HonorInfo honorInfo : honorInfos) {
				HonorInfo copyData = new HonorInfo();
				copyData.setGrade(honorInfo.getGrade());
				copyData.setLocgovCode(honorInfo.getLocgovCode());
				copyData.setLocNm(honorInfo.getLocNm());
				copyData.setUserLocNm(honorInfo.getUserLocNm());
				copyData.setGoldCnt(honorInfo.getGoldCnt());
				copyData.setSilverCnt(honorInfo.getSilverCnt());
				copyData.setBronzeCnt(honorInfo.getBronzeCnt());
				copyData.setQrCode(honorInfo.getQrCode());
				copyData.setUserName(honorInfo.getUserName());
				copyData.setMakeQrDateTime(honorInfo.getMakeQrDateTime());
				copyHonorInfos.add(copyData);
			}
			
			return copyHonorInfos;
		}
	}
	
	public void setQrCode(LocalDateTime startTime, DateTimeFormatter format) {
		if (honorInfos != null && !honorInfos.isEmpty()) {
			
			for (HonorInfo honorInfo : honorInfos) {
				honorInfo.setQrCode(makeQrCode(honorInfo, startTime, format));
			}
		}
	}
	
	public String makeQrCode(HonorInfo honorInfo, LocalDateTime startTime, DateTimeFormatter format) {
		if (UserUtils.getUser().getUserId() > 0) {
			if (StringUtils.hasLength(honorInfo.getLocgovCode())
					&& StringUtils.hasLength(honorInfo.getGrade())) {
				
				StringBuffer buf = new StringBuffer();
				buf.append("이름 : " + UserUtils.getUser().getUserName());
				buf.append("\n명예등급 : " + makeGradeName(honorInfo.getGrade()));
				buf.append("\n거주시도 : " + honorInfo.getUserLocNm());
				buf.append("\n기부자치구 : " + honorInfo.getLocNm());
				buf.append("\n유효시작일시 : " + startTime.format(format));
				buf.append("\n유효만료일시 : " + startTime.plusSeconds(expireSeconds).format(format));
				
				return QrUtils.getQrCodeBase64String(buf.toString());
			}
		}
		return "";
	}
	
	public String makeGradeName(String grade) {
		String gradeName = "";
		if (StringUtils.hasLength(grade)) {
			switch (grade) {
				case "GOLD":
					gradeName = "특급 명예";
				break;
				case "SILVER":
					gradeName = "최우수 명예";
				break;
				case "BRONZE":
					gradeName = "우수 명예";
				break;
			}
		}
		return gradeName;
	}
	
}
