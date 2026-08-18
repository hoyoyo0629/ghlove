package saleson.shop.donation;


import java.util.stream.Stream;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.util.ObjectUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import saleson.common.enumeration.FaqType;
import saleson.common.web.Param;
import saleson.model.QFaq;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class DonationDto extends Param {

	@NotNull
	private FaqType faqType;

	@NotEmpty
	private String title;

	@NotEmpty
	private String content;
	private int hit;

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
		return builder;
	}
}
