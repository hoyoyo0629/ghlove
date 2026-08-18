package saleson.batch.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class BatchLogParam extends SearchParam {

    private String id;
    private String batchLogId;
    private String logName;
    private String logMethod;
    private String triggerType;
    private String triggerRepeatSeconds;
    private String triggerCronExpression;
    private String batchStatus;
    private String batchExcuteDate;
    private String batchApplyFlag;

    private String searchType;
    private String triType;
    private String batchType;
    private String searchDateType;
    private String searchStartDate;
    private String searchEndDate;
    
    
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

	public String getSearchDateType() {
		return searchDateType;
	}

	public void setSearchDateType(String searchDateType) {
		this.searchDateType = searchDateType;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchLogId() {
        return batchLogId;
    }

    public void setBatchLogId(String batchLogId) {
        this.batchLogId = batchLogId;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getLogMethod() {
        return logMethod;
    }

    public void setLogMethod(String logMethod) {
        this.logMethod = logMethod;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerRepeatSeconds() {
        return triggerRepeatSeconds;
    }

    public void setTriggerRepeatSeconds(String triggerRepeatSeconds) {
        this.triggerRepeatSeconds = triggerRepeatSeconds;
    }

    public String getTriggerCronExpression() {
        return triggerCronExpression;
    }

    public void setTriggerCronExpression(String triggerCronExpression) {
        this.triggerCronExpression = triggerCronExpression;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public String getBatchExcuteDate() {
        return batchExcuteDate;
    }

    public void setBatchExcuteDate(String batchExcuteDate) {
        this.batchExcuteDate = batchExcuteDate;
    }

    public String getBatchApplyFlag() {
        return batchApplyFlag;
    }

    public void setBatchApplyFlag(String batchApplyFlag) {
        this.batchApplyFlag = batchApplyFlag;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getTriType() {
        return triType;
    }

    public void setTriType(String triType) {
        this.triType = triType;
    }

    public String getBatchType() {
        return batchType;
    }

    public void setBatchType(String batchType) {
        this.batchType = batchType;
    }
}
