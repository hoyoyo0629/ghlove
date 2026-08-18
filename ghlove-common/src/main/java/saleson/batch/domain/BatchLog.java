package saleson.batch.domain;

public class BatchLog {

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
    private String jobName; 	
    private String batchType; 	
    private String executionDate;
    private String startTime; 	
    private String endTime; 	
    private String rsltCodeDesc;
    private String message;

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getBatchType() {
		return batchType;
	}

	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}

	public String getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(String executionDate) {
		this.executionDate = executionDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRsltCodeDesc() {
		return rsltCodeDesc;
	}

	public void setRsltCodeDesc(String rsltCodeDesc) {
		this.rsltCodeDesc = rsltCodeDesc;
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
}
