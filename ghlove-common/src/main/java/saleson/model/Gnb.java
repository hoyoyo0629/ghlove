package saleson.model;


import lombok.Getter;
import lombok.Setter;
import saleson.common.hibenate.converter.BooleanYnConverter;
import saleson.common.utils.CommonUtils;
import saleson.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="OP_GNB")
@Getter
@Setter
public class Gnb extends BaseEntity {

	//사용 안하는 테이블
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String target;

    @Column
    private Integer ordering;

    @Column(length = 1, nullable = false)
    @Convert(converter = BooleanYnConverter.class)
    private boolean displayFlag;

    @PrePersist
    public void prePersist() {
        setDefaultValue();
    }

    @PreUpdate
    public void preUpdate() {
        setDefaultValue();
    }

    public void setDefaultValue() {
        this.ordering = CommonUtils.intNvl(this.ordering);
    }

    public void update(Gnb gnb) {
        if (gnb != null) {
            setTitle(gnb.getTitle());
            setTarget(gnb.getTarget());
        }
    }

}
