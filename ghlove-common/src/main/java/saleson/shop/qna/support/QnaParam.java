package saleson.shop.qna.support;

import com.onlinepowers.framework.web.domain.SearchParam;
import java.util.*;
import saleson.common.utils.CommonUtils;

@SuppressWarnings("serial")
public class QnaParam extends SearchParam {
	private int itemId;
	private String itemUserCode;
	//2014.12.31 
	private String searchStartDate;
	private String searchEndDate;
	private List<String> sendMailFlag;
	private String qnaGroup;
	private String qnaType;
	private long userId;
	private int answerCount;
	private long sellerId;
	//2015.1.12
	private String displayFlag;
	private String useYn;
	
	private String[] id;
	
	private String qnaId;

	private String dataStatusCode;
	
	private String sido;			// 시도 검색값
	private String sigungu;			// 시군구 검색값
	
	private String qnaAnswerCode;
	
	public QnaParam() {}
	
	public QnaParam(String qnaType) {
		this.qnaType = qnaType;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
	public String getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public String getSearchEndDate() {
		return searchEndDate;
	}

	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}

	public List<String> getSendMailFlag() {
		return sendMailFlag;
	}

	public void setSendMailFlag(List<String> sendMailFlag) {
		this.sendMailFlag = sendMailFlag;
	}
	
	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

	public String[] getId() {
		return CommonUtils.copy(id);
	}

	public void setId(String[] id) {
		this.id = CommonUtils.copy(id);
	}

	public String getQnaGroup() {
		return qnaGroup;
	}

	public void setQnaGroup(String qnaGroup) {
		this.qnaGroup = qnaGroup;
	}

	public String getQnaType() {
		return qnaType;
	}

	public void setQnaType(String qnaType) {
		this.qnaType = qnaType;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getSellerId() {
		return sellerId;
	}

	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	public String getQnaId() {
		return qnaId;
	}

	public void setQnaId(String qnaId) {
		this.qnaId = qnaId;
	}

	public String getDataStatusCode() {
		return dataStatusCode;
	}

	public void setDataStatusCode(String dataStatusCode) {
		this.dataStatusCode = dataStatusCode;
	}

	public String getItemUserCode() {
		return itemUserCode;
	}

	public void setItemUserCode(String itemUserCode) {
		this.itemUserCode = itemUserCode;
	}

	public String getSido() {
		return sido;
	}

	public void setSido(String sido) {
		this.sido = sido;
	}

	public String getSigungu() {
		return sigungu;
	}

	public void setSigungu(String sigungu) {
		this.sigungu = sigungu;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public String getQnaAnswerCode() {
		return qnaAnswerCode;
	}

	public void setQnaAnswerCode(String qnaAnswerCode) {
		this.qnaAnswerCode = qnaAnswerCode;
	}
}
