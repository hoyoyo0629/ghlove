package saleson.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.model.DateAudit;

import javax.persistence.*;

@Entity
@Table(name="OP_USER_LOGIN")
@Getter @Setter
@NoArgsConstructor
public class UserLogin extends DateAudit {

	//20231011 현재 잘되고 있어서 추가 안함
    @Id
    @GeneratedValue
    private Long id;

    @Column(updatable = false, nullable = false)
    private Long userId;

    @Column(length = 1000, updatable = false, nullable = false)
    private String sessionId;

    public UserLogin(Long userId, String sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }
}
