package saleson.shop.stylebook.support;

import com.onlinepowers.framework.util.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.*;
import org.springframework.util.ObjectUtils;
import saleson.common.web.Param;
import saleson.model.stylebook.QStyleBook;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class StyleBookDto extends Param {

    private Long id;

    public Predicate getPredicate() {
        BooleanBuilder builder = new BooleanBuilder();
        QStyleBook styleBook = QStyleBook.styleBook;

        if (!ObjectUtils.isEmpty(getWhere()) && !ObjectUtils.isEmpty(getQuery())) {
            switch (getWhere()) {
                case "ALL":
                    builder.and(styleBook.title.like("%"+getQuery()+"%").or(styleBook.content.like("%"+getQuery()+"%")));
                    break;
                case "TITLE":
                    builder.and(styleBook.title.like("%"+getQuery()+"%"));
                    break;
                case "CONTENT":
                    builder.and(styleBook.content.like("%"+getQuery()+"%"));
                    break;
                default:
            }
        }

        return builder;
    }

}
