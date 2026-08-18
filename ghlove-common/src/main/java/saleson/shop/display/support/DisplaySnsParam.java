package saleson.shop.display.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class DisplaySnsParam extends SearchParam {
    private int snsId;
    private String snsType;
    private String searchStartDate;
    private String searchEndDate;

    private String[] snsIds;
    private int startOrdering;

    public int getSnsId() {
        return snsId;
    }

    public void setSnsId(int snsId) {
        this.snsId = snsId;
    }

    public String getSnsType() {
        return snsType;
    }

    public void setSnsType(String snsType) {
        this.snsType = snsType;
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

    public String[] getSnsIds() {
    	if (snsIds == null) {
    		return null;
    	} else {
    		int length = snsIds.length;
    		String[] array = new String[length];
    		for(int i = 0 ; i < length ; i++) {
    			array[i] = snsIds[i];
    		}
    		return array;
    	}
    }

    public void setSnsIds(String[] snsIds) {
    	if (snsIds == null) {
    		this.snsIds = null;
    	} else {
    		int length = snsIds.length;
    		this.snsIds = new String[length];
    		for(int i = 0 ; i < length ; i++) {
    			this.snsIds[i] = snsIds[i];
    		}
    	}
    }

    public int getStartOrdering() {
        return startOrdering;
    }

    public void setStartOrdering(int startOrdering) {
        this.startOrdering = startOrdering;
    }
}
