package saleson.api.display.support;

import org.springframework.stereotype.Component;
import saleson.api.display.domain.ItemList;
import saleson.shop.display.domain.DisplayItem;
import saleson.shop.item.domain.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class DisplayDataSupport {

    public List<ItemList> getItemListByItem(List<Item> list) {
        List<ItemList> resultList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {

            for (Item item : list) {
                resultList.add(new ItemList(item));
            }
        }
        return resultList;
    }

    public List<ItemList> getItemListByDisplayItem(List<DisplayItem> list) {
        List<ItemList> resultList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {

            for (DisplayItem item : list) {
                resultList.add(new ItemList(item));
            }
        }
        return resultList;
    }

}
