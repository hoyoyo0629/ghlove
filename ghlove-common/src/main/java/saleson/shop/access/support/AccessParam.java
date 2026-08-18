package saleson.shop.access.support;

import com.onlinepowers.framework.web.domain.SearchParam;

public class AccessParam extends SearchParam {

    private int[] id;
    private String accessType;
    private String displayFlag;

    private String searchStartDate;
    private String searchEndDate;


    public int[] getId() {
    	if (id == null) {
    		return null;
    	} else {
    		int length = id.length;
    		int[] array = new int[length];
    		for(int i = 0 ; i < length ; i++) {
    			array[i] = id[i];
    		}
    		return array;
    	}
    }

    public void setId(int[] id) {
    	if (id == null) {
    		this.id = null;
    	} else {
    		int length = id.length;
    		this.id = new int[length];
    		for(int i = 0 ; i < length ; i++) {
    			this.id[i] = id[i];
    		}
    	}
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(String displayFlag) {
        this.displayFlag = displayFlag;
    }

    public String getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(String searchStartDate) {
        this.searchStartDate = searchStartDate;
    }

    public String getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(String searchEndDate) {
        this.searchEndDate = searchEndDate;
    }
}
