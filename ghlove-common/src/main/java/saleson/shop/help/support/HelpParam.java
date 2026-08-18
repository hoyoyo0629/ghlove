package saleson.shop.help.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class HelpParam extends SearchParam {
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
	
	public Integer getMnlSn() {
		return mnlSn;
	}
	public void setMnlSn(Integer mnlSn) {
		this.mnlSn = mnlSn;
	}
	public String getMenuSeCode() {
		return menuSeCode;
	}
	public void setMenuSeCode(String menuSeCode) {
		this.menuSeCode = menuSeCode;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getMenuNm() {
		return menuNm;
	}
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public String getOrginlFileNm() {
		return orginlFileNm;
	}
	public void setOrginlFileNm(String orginlFileNm) {
		this.orginlFileNm = orginlFileNm;
	}
	public String getFileTy() {
		return fileTy;
	}
	public void setFileTy(String fileTy) {
		this.fileTy = fileTy;
	}
	public Integer getInqireCo() {
		return inqireCo;
	}
	public void setInqireCo(Integer inqireCo) {
		this.inqireCo = inqireCo;
	}
	public String getMenuSj() {
		return menuSj;
	}
	public void setMenuSj(String menuSj) {
		this.menuSj = menuSj;
	}
	public String getMenuCn() {
		return menuCn;
	}
	public void setMenuCn(String menuCn) {
		this.menuCn = menuCn;
	}
	public Long getFrstRegisterId() {
		return frstRegisterId;
	}
	public void setFrstRegisterId(Long frstRegisterId) {
		this.frstRegisterId = frstRegisterId;
	}
	public String getFrstRegistPnttm() {
		return frstRegistPnttm;
	}
	public void setFrstRegistPnttm(String frstRegistPnttm) {
		this.frstRegistPnttm = frstRegistPnttm;
	}
	public Long getLastUpdusrId() {
		return lastUpdusrId;
	}
	public void setLastUpdusrId(Long lastUpdusrId) {
		this.lastUpdusrId = lastUpdusrId;
	}
	public String getLastUpdtPnttm() {
		return lastUpdtPnttm;
	}
	public void setLastUpdtPnttm(String lastUpdtPnttm) {
		this.lastUpdtPnttm = lastUpdtPnttm;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
}
