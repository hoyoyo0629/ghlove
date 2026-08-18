package saleson.model;

import lombok.*;
import saleson.common.hibenate.converter.BooleanYnConverter;
import saleson.model.base.BaseEntity;
import saleson.shop.policy.domain.Policy;

import javax.persistence.*;

@Entity
@Table(name = "OP_USER_AGREE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class UserAgree extends BaseEntity {

	//사용 안하는 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int policyId;

    @Column
    private String loginId;

    @Column
    private Long userId;

    @Column(length = 1)
    @Convert(converter = BooleanYnConverter.class)
    private boolean agree;

    @Column
    private String title;

    @Column(length = 1)
    private String policyType;

    public UserAgree(Policy policy, String loginId, Long userId, boolean agree) {

        if (policy != null) {
            this.policyId = policy.getPolicyId();
            this.title = policy.getTitle();
            this.policyType = policy.getPolicyType();
        }

        this.loginId = loginId;
        this.userId = userId;
        this.agree = agree;

    }
}
