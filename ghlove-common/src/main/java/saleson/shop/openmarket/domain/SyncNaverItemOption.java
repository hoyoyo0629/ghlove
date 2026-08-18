package saleson.shop.openmarket.domain;

import com.onlinepowers.framework.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyncNaverItemOption {

    private int itemId;
    private int itemOptionId;
    private String name1;
    private String name2;
    private String name3;

    public void setName(Set<String> set1,Set<String> set2, Set<String> set3) {

        if (StringUtils.hasText(getName1())) {
            set1.add(getName1());
        }

        if (StringUtils.hasText(getName2())) {
            set2.add(getName2());
        }

        if (StringUtils.hasText(getName3())) {
            set3.add(getName3());
        }
    }
}
