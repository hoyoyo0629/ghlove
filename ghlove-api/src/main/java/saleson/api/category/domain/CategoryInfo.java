package saleson.api.category.domain;

import saleson.shop.categories.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryInfo {
    private String categoryId;
    private String url;
    private String name;
    private String code;
    private String link;

    private List<CategoryInfo> childCategories = new ArrayList<>();

    public CategoryInfo() {}

    public CategoryInfo(Category category) {
        if (category != null) {
            this.categoryId = category.getCategoryId();
            this.url = category.getUrl();
            this.name = category.getName();
            this.code = category.getCode();
            this.link = category.getLink();

            for (Category chilidCategory : category.getChildCategories()) {
                CategoryInfo categoryInfo = new CategoryInfo(chilidCategory);
                this.childCategories.add(categoryInfo);
            }
        }
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<CategoryInfo> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryInfo> childCategories) {
        this.childCategories = childCategories;
    }
}
