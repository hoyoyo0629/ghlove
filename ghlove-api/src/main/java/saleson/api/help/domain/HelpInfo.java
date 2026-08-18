package saleson.api.help.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.utils.CommonUtils;
import saleson.shop.help.domain.Help;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpInfo {
	private Integer mnlSn;				// 매뉴얼 일련번호
	private String menuSeCode;			// 메뉴 구분 코드
	private String menuUrl;				// 메뉴 URL
	private String menuNm;				// 메뉴 명
	private String fileNm;				// 파일 명
	private String orginlFileNm;		// 원본 파일 명
	private String fileTy;				// 파일 유형
	private Integer inqireCo;			// 조회 수
	private String menuSj;				// 메뉴 제목
	private String menuCn;				// 메뉴 내용
	private Long frstRegisterId;		// 최초 등록자 ID
	private String frstRegistPnttm;		// 최초 등록 시점
	private Long lastUpdusrId;			// 최종 수정자 ID
	private String lastUpdtPnttm;		// 최종 수정 시점
	private String userName;			// 작성자명
	
	public HelpInfo(Help help) {
		if(help != null) {
			setMnlSn(CommonUtils.intNvl(help.getMnlSn()));
			setMenuSeCode(CommonUtils.dataNvl(help.getMenuSeCode()));
			setMenuUrl(CommonUtils.dataNvl(help.getMenuUrl()));
			setMenuNm(CommonUtils.dataNvl(help.getMenuNm()));
			setFileNm(CommonUtils.dataNvl(help.getFileNm()));
			setOrginlFileNm(CommonUtils.dataNvl(help.getOrginlFileNm()));
			setFileTy(CommonUtils.dataNvl(help.getFileTy()));
			setInqireCo(CommonUtils.intNvl(help.getInqireCo()));
			setMenuSj(CommonUtils.dataNvl(help.getMenuSj()));
			setMenuCn(CommonUtils.dataNvl(help.getMenuCn()));
			setFrstRegisterId(CommonUtils.longNvl(help.getFrstRegisterId()));
			setFrstRegistPnttm(CommonUtils.dataNvl(help.getFrstRegistPnttm()));
			setLastUpdusrId(CommonUtils.longNvl(help.getLastUpdusrId()));
			setLastUpdtPnttm(CommonUtils.dataNvl(help.getLastUpdtPnttm()));
			setUserName(CommonUtils.dataNvl(help.getUserName()));
		}
	}
}
