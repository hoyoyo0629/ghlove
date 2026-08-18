package saleson.api.category.domain;

import saleson.shop.categories.domain.Group;
import saleson.shop.categories.domain.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamInfo {
    private String url;
    private String name;
    private List<GroupInfo> groups = new ArrayList<>();
    

    public TeamInfo() {}

    public TeamInfo(Team team) {
        if (team != null) {
            this.url = team.getUrl();
            this.name = team.getName();

            for (Group group : team.getGroups()) {
                GroupInfo groupInfo = new GroupInfo(group);
                this.groups.add(groupInfo);
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

    public List<GroupInfo> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupInfo> groups) {
        this.groups = groups;
    }
}
