package saleson.shop.gnb.support;

import com.onlinepowers.framework.util.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.ShopUtils;
import saleson.common.web.Param;

import static saleson.model.QGnb.gnb;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GnbCriteria extends Param {

    private Long id;
    private String title;
    private String target;
    private boolean displayFlag;
    private String searchDisplayFlag;

    public Predicate getPredicate() {
        BooleanBuilder builder = new BooleanBuilder();

        if (!ObjectUtils.isEmpty(getQuery())) {
            if ("title".equals(getWhere())) {
                builder.and(gnb.title.contains(getQuery()));
            }
        }

        if (!ObjectUtils.isEmpty(getSearchDisplayFlag())) {
            builder.and(gnb.displayFlag.eq("Y".equals(getSearchDisplayFlag())));
        }

        return builder;
    }
}
