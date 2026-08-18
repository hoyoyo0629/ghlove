package saleson.shop.log.domain;

import lombok.Data;
import saleson.shop.log.support.ChangeTypeEnum;

@Data
public class ChangeLog {

    private int changeLogId;
    private long userId;
    private long managerId;
    private String parameter;
    private String createdDate;
    private String remoteAddr;
    private ChangeTypeEnum changeType;


    /* 생성자의 매개변수가 다른 상황을 위한 빌더패턴 */
    public static class Builder {
        private long userId;
        private long managerId;
        private String parameter;
        private String remoteAddr;
        private ChangeTypeEnum changeType = ChangeTypeEnum.CREATE;

        public Builder(long managerId, String parameter, String remoteAddr) {   // 기본값
            this.managerId = managerId;
            this.parameter = parameter;
            this.remoteAddr = remoteAddr;
        }

        public Builder setUserId(long value) {
            userId = value;
            return this;
        }

        public Builder setChangeType(ChangeTypeEnum value) {
            changeType = value;
            return this;
        }

        public ChangeLog build() {
            return new ChangeLog(this);
        }

    }

    private ChangeLog(Builder builder) {
        userId = builder.userId;
        managerId = builder.managerId;
        parameter = builder.parameter;
        remoteAddr = builder.remoteAddr;
        changeType = builder.changeType;
    }


}
