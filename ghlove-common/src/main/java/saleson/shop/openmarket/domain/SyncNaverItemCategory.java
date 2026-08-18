package saleson.shop.openmarket.domain;

import com.onlinepowers.framework.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;
import saleson.shop.categories.domain.Category;
import saleson.shop.categories.domain.Group;
import saleson.shop.categories.domain.Team;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class SyncNaverItemCategory {

    private String first;
    private String second;
    private String third;
    private String fourth;

    public SyncNaverItemCategory(String first, String second, String third, String fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
}
