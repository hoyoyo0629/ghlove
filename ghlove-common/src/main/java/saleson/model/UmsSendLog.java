package saleson.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onlinepowers.framework.exception.OpRuntimeException;
import com.onlinepowers.framework.notification.message.OpMessage;
import com.onlinepowers.framework.util.JsonViewUtils;
import com.onlinepowers.framework.util.StringUtils;
import lombok.*;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import saleson.common.enumeration.UmsType;
import saleson.common.notification.domain.UmsExpansionInfo;
import saleson.common.notification.domain.UmsTemplate;
import saleson.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="OP_UMS_SEND_LOG")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class UmsSendLog extends BaseEntity {

	private static final Logger log = LoggerFactory.getLogger(UmsSendLog.class);
	

    public UmsSendLog(String templateCode, UmsType umsType, String title, String message, String sendType, String applyCode) {
        this.templateCode = templateCode;
        this.umsType = umsType;
        this.sendType = sendType;
        this.title = title;
        this.message = message;
        this.applyCode = applyCode;
    }

    public UmsSendLog(OpMessage opMessage, UmsType umsType) {
        if (opMessage != null) {

            String applyCode = "-";
            String templateCode = "-";
            String sendType = "";

            try {
                String[] bcc = opMessage.getBcc();

                templateCode = bcc[0];
                applyCode = bcc[1];

                try {
                    String jsonUmsExpansionInfo = bcc[3];
                    UmsExpansionInfo expansionInfo = null;
                    if (!ObjectUtils.isEmpty(jsonUmsExpansionInfo)) {
                        expansionInfo = (UmsExpansionInfo) JsonViewUtils.jsonToObject(jsonUmsExpansionInfo, new TypeReference<UmsExpansionInfo>() {});
                    }

                    if (expansionInfo != null) {
                        this.targetUserId = expansionInfo.getTargetUserId();
                    }
                } catch (OpRuntimeException ignore) {
                	log.error(getClass().getName() + " UmsSendLog constructor error1", ignore);
                }

            } catch (OpRuntimeException ignore) {
            	log.error(getClass().getName() + " UmsSendLog constructor error2", ignore);
            }

            if (umsType == UmsType.MESSAGE) {
                sendType = opMessage.getSmsType();
            }

            this.templateCode = templateCode;
            this.umsType = umsType;
            this.sendType = sendType;
            this.title = opMessage.getTitle();
            this.message = opMessage.getMessage();
            this.applyCode = applyCode;
        }
    }

    //사용 안하는 테이블
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 50, updatable = false, nullable = false)
    private String templateCode;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, updatable = false, nullable = false)
    private UmsType umsType;

    // SMS 발송타입
    private String sendType;

    @Column(length = 255)
    private String title;

    // 발송문구
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String message;

    // 카카오톡 승인 코드
    @Column(length=100)
    private String applyCode;

    @Column
    private Long targetUserId;
}
