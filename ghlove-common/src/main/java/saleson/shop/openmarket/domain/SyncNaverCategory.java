package saleson.shop.openmarket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SyncNaverCategory {
    private int itemCategoryId;
    private int categoryId;
    private int ordering;
}
