package saleson.model;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.security.core.SpringSecurityCoreVersion;
import saleson.common.enumeration.FaqType;
import saleson.model.base.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OP_FAQ")
@SequenceGenerator(
		name = "FAQ_SEQ_GENERATOR"
		, sequenceName = "OP_FAQ_SEQ"
		, initialValue = 630000
		, allocationSize = 1)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = "id", callSuper = false)
public class Faq extends BaseEntity {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAQ_SEQ_GENERATOR")
	private Long id;

	@Enumerated(EnumType.STRING)
	private FaqType faqType;

	private String title;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	private String content;
	private int hit;
	private String useYn = "Y";
}
