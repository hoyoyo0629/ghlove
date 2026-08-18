package saleson.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.model.DateAudit;

import javax.persistence.*;

@Entity
@Table(name="OP_SELLER_USER_LOGIN")
@SequenceGenerator(
		name = "SELLER_USER_LOGIN_SEQ_GENERATOR"
		, sequenceName = "OP_SELLER_USER_LOGIN_SEQ"
		, initialValue = 2153000
		, allocationSize = 1)
@Getter @Setter
@NoArgsConstructor
public class SellerUserLogin extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SELLER_USER_LOGIN_SEQ_GENERATOR")
    private Long id;

    @Column(updatable = false, nullable = false)
    private Long userId;

    @Column(length = 1000, updatable = false, nullable = false)
    private String sessionId;

    public SellerUserLogin(Long userId, String sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }
}
