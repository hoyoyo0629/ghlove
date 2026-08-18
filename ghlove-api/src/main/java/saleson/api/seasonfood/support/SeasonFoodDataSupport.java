package saleson.api.seasonfood.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import saleson.api.item.domain.ItemList;
import saleson.api.seasonfood.domain.SeasonFood;
import saleson.shop.item.domain.Item;
import saleson.shop.seasonalfood.domain.SeasonalFood;

@Component
public class SeasonFoodDataSupport {

    public List<SeasonFood> bindList(List<SeasonalFood> list) {
        List<SeasonFood> resultList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (SeasonalFood item : list) {
                resultList.add(new SeasonFood(item.getSeasonalFoodMonth(), item.getSeasonalFoodKeyword()));
            }
        }
        return resultList;
    }
}
