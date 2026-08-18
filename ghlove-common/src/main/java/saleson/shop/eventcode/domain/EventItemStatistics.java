package saleson.shop.eventcode.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.CommonUtils;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventItemStatistics {

    private String itemName;
    private String itemUserCode;
    private int listViewCount;
    private int itemViewCount;
    private double clickedRate;
    private int orderCount;
    private double orderRate;

    public EventItemStatistics(Map<String, Object> map) {

        if (map != null) {
            setItemName(CommonUtils.dataNvl(map.get("ITEM_NAME")));
            setItemUserCode(CommonUtils.dataNvl(map.get("ITEM_USER_CODE")));
            setListViewCount(CommonUtils.bigDecimalNvl(convert(map,"LIST_VIEW_COUNT")).intValue());
            setItemViewCount(CommonUtils.bigDecimalNvl(convert(map,"ITEM_VIEW_COUNT")).intValue());
            setOrderCount(CommonUtils.bigDecimalNvl(convert(map,"ORDER_COUNT")).intValue());
            setOrderRate(CommonUtils.bigDecimalNvl(convert(map,"ORDER_RATE")).doubleValue());
            setClickedRate(CommonUtils.bigDecimalNvl(convert(map,"CLICKED_RATE")).doubleValue());
        }
    }

    private BigDecimal convert(Map<String, Object> map, String key) {

        if (ObjectUtils.isEmpty(map)) {
            return BigDecimal.ZERO;
        }

        Object o = map.get(key);

        if (ObjectUtils.isEmpty(o)) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(String.valueOf(o));
    }
}
