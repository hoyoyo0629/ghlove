package saleson.shop.faq;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.*;
import org.springframework.util.ObjectUtils;
import saleson.common.enumeration.FaqType;
import saleson.common.web.Param;
import saleson.model.QFaq;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.stream.Stream;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class FaqDto extends Param {

	@NotNull
	private FaqType faqType;

	@NotEmpty
	private String title;

	@NotEmpty
	private String content;
	private int hit;

	private String useYn;

	public Predicate getPredicate() {
		QFaq faq = QFaq.faq;
		BooleanBuilder builder = new BooleanBuilder();

		if (getQuery() != null && !getQuery().isEmpty()) {

			if ("all".equalsIgnoreCase(getWhere())) {
				builder.and(
						faq.title.contains(getQuery())
						.or(faq.content.contains(getQuery()))
				);

			} else if ("title".equalsIgnoreCase(getWhere())) {
				builder.and(faq.title.contains(getQuery()));

			} else if ("content".equalsIgnoreCase(getWhere())) {
				builder.and(faq.content.contains(getQuery()));

			}
		}

		if (getFaqType() != null) {
				Stream.of(FaqType.values())
						.filter(f -> f == getFaqType())
						.forEach(faqType ->
							builder.and(faq.faqType.eq(faqType))
						);
		}

		if (!ObjectUtils.isEmpty(getTitle())) {
			builder.and(faq.title.eq(getTitle()));
		}

		if(!ObjectUtils.isEmpty(getUseYn())) {
			builder.and(faq.useYn.eq(getUseYn()));
		}

		return builder;
	}
}
