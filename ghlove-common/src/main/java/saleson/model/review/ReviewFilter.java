package saleson.model.review;

import lombok.*;
import saleson.model.base.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name="OP_REVIEW_FILTER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class ReviewFilter extends BaseEntity {

	//사용 안하는 테이블
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long filterGroupId;

    @Column(nullable = false)
    private Integer categoryId;

    @Column
    private Integer ordering;

}
