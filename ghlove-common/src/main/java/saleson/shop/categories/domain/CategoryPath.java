package saleson.shop.categories.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPath {
    private int categoryId;
    private int categoryGroupId;
    private String categoryName;
    private String categoryUrl;
    private String categoryLevel;
}
