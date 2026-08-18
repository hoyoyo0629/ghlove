package saleson.model.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.SpringSecurityCoreVersion;
import saleson.common.utils.LocalDateUtils;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public abstract class GhLoveBaseEntity implements Serializable {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	/**
     * 최초 등록자 ID
     */
    @CreatedBy
    @Column(name = "FRST_REGISTER_ID")
    private Long frstRegisterId;

    /**
     * 최초 등록 시점
     */
    @CreatedDate
    @Column(name = "FRST_REGIST_PNTTM")
    private LocalDateTime frstRegistPnttm;

    /**
     * 최종 수정자 ID
     */
    @LastModifiedBy
    @Column(name = "LAST_UPDUSR_ID")
    private Long lastUpdusrId;

    /**
     * 최종 수정 시점
     */
    @LastModifiedDate
    @Column(name = "LAST_UPDT_PNTTM")
    private LocalDateTime lastUpdtPnttm;

	public String getCreatedDate() {
		return LocalDateUtils.getDate(frstRegistPnttm);
	}

	public String getCreatedDateTime() {
		return LocalDateUtils.getDateTime(frstRegistPnttm);
	}

	public String getUpdatedDate() {
		return LocalDateUtils.getDate(lastUpdtPnttm);
	}

	public String getUpdatedDateTime() {
		return LocalDateUtils.getDateTime(lastUpdtPnttm);
	}

}
