package saleson.shop.notice.support;

import com.onlinepowers.framework.web.domain.SearchParam;
import saleson.common.utils.CommonUtils;

import java.util.List;

@SuppressWarnings("serial")
public class NoticeParam extends SearchParam {
	private int noticeId;
	private String subCategory;
	private String categoryTeam;
	private String subject;
	private String noticeFlag;
	private int visibleType;
	private long sellerId;
	private String useYn;
	
	// 조회용 팀코드
	private String[] teamCodes;
	private String locgovCode;
	private String startCreateDate;
	private String endCreateDate;

	// 기금사업 소개 조회용 추가
	private String boardCode;
	// 기금사업 소개 조회용 추가
	
	private List<String> noticeList;
	

	/* 지자체공지 삭제시 사용 */
	private List<String> locgovNoticeList;	// 지자체 공지 목록

	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	public String getNoticeFlag() {
		return noticeFlag;
	}
	public void setNoticeFlag(String noticeFlag) {
		this.noticeFlag = noticeFlag;
	}
	public int getVisibleType() {
		return visibleType;
	}
	public void setVisibleType(int visibleType) {
		this.visibleType = visibleType;
	}
	public String getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}
	public String getCategoryTeam() {
		return categoryTeam;
	}
	public void setCategoryTeam(String categoryTeam) {
		this.categoryTeam = categoryTeam;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String[] getTeamCodes() {
		return CommonUtils.copy(teamCodes);
	}
	public void setTeamCodes(String[] teamCodes) {
		this.teamCodes = CommonUtils.copy(teamCodes);
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	public String getLocgovCode() {	return locgovCode; }
	public void setLocgovCode(String locgovCode) { this.locgovCode = locgovCode; }

	public String getStartCreateDate() { return startCreateDate; }
	public void setStartCreateDate(String startCreateDate) { this.startCreateDate = startCreateDate; }
	public String getEndCreateDate() { return endCreateDate; }
	public void setEndCreateDate(String endCreateDate) { this.endCreateDate = endCreateDate; }

	public List<String> getLocgovNoticeList() { return locgovNoticeList; }
	public void setLocgovNoticeList(List<String> locgovNoticeList) { this.locgovNoticeList = locgovNoticeList; }

	public List<String> getNoticeList() {
		return noticeList;
	}
	public void setNoticeList(List<String> noticeList) {
		this.noticeList = noticeList;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	

	// 기금사업 소개 조회용 추가
	public String getBoardCode() {
		return boardCode;
	}
	public void setBoardCode(String boardCode) {
		this.boardCode = boardCode;
	}
	// 기금사업 소개 조회용 추가
	
}
