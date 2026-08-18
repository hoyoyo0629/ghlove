package saleson.api.category.domain;

import saleson.shop.categories.domain.Category;
import saleson.shop.categories.domain.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupInfo {
    private String url;
    private String name;
    private String itemList;
    
    private String link;
    private String description;		// 카테고리 이미지 경로
    private String descriptionM;	// 카테고리 이미지 경로 (모바일)

    private List<CategoryInfo> categories = new ArrayList<>();

    public GroupInfo() {}

    public GroupInfo(Group group) {
        if (group != null) {
            this.url = group.getUrl();
            this.name = group.getName();
            this.itemList = group.getItemList();
            this.link = group.getLink();
            this.description = group.getDescription();
            this.descriptionM = group.getDescriptionM();
            for (Category category : group.getCategories()) {
                CategoryInfo categoryInfo = new CategoryInfo(category);
                this.categories.add(categoryInfo);
            }
        }
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
    public List<CategoryInfo> getCategories() {
        return categories;
    }
    public void setCategories(List<CategoryInfo> categories) {
        this.categories = categories;
    }
    public String getItemList() {
        return itemList;
    }
    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionM() {
		return descriptionM;
	}

	public void setDescriptionM(String descriptionM) {
		this.descriptionM = descriptionM;
	}
	
}
