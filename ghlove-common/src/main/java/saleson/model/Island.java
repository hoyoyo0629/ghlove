package saleson.model;

import lombok.Getter;
import lombok.Setter;
import saleson.common.enumeration.IslandType;
import saleson.model.base.BaseEntity;

import javax.persistence.*;


@Entity
@Table(name="OP_ISLAND")
@SequenceGenerator(
		name = "ISLAND_SEQ_GENERATOR"
		, sequenceName = "OP_ISLAND_SEQ"
		, initialValue = 1700
		, allocationSize = 1)
//@EntityListeners(value= {AuditingEntityListener.class})
@Getter @Setter
public class Island extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ISLAND_SEQ_GENERATOR")
	private Long id;

	@Column(length = 7)
	private String zipcode;

	@Column(length = 255)
	private String address;

	@Column(length = 20)
    @Enumerated(EnumType.STRING)
	private IslandType islandType;

}
